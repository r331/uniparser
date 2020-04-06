package dev.ivanov.parser;

import dev.ivanov.parser.annotation.Parsed;
import lombok.SneakyThrows;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.apache.commons.lang3.reflect.FieldUtils.getAllFieldsList;

public class Parser<T, F> implements AutoCloseable, Iterable<T> {
    private final Class<T> beanType;
    private final List<Pair<Field, List<? extends Annotation>>> fields;
    private final List<List<Class<? extends Annotation>>> fieldClasses;
    private final AnnotationProcessor<F> annotationProcessor;
    private final FieldProcessor<F> fieldProcessor;
    private final RawEntityReader<F> rawEntityReader;

    public static <T, F> ParserBuilder<T, F> builder() {
        return new ParserBuilder<>();
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private boolean hasNext;

            @Override
            public boolean hasNext() {
                hasNext = rawEntityReader.hasNext();
                return hasNext;
            }

            @Override
            @SneakyThrows
            public T next() {
                if (!hasNext) throw new NoSuchElementException();

                List<F> row = rawEntityReader.next();
                T bean = beanType.getDeclaredConstructor().newInstance();
                updateFields(bean, row);
                return bean;
            }
        };
    }

    @Override
    public void close() throws Exception {
        rawEntityReader.close();
    }

    private void updateFields(T bean, List<F> fieldValues) throws IllegalAccessException {
        for (int i = 0; i < fields.size() && i < fieldValues.size(); i++) {
            Field field = fields.get(i).getFirst();
            List<? extends Annotation> annotations = fields.get(i).getSecond();
            F rawData = fieldValues.get(i);
            if (checkPresentCustomAnnotationOnField(fieldClasses.get(i), annotationProcessor.getAnnotations()))
                fieldProcessor.process(field, bean, rawData);
            else
                setFieldWithAnnotationRules(bean, field, annotations, rawData);
        }
    }

    private void setFieldWithAnnotationRules(T bean,
                                             Field field,
                                             List<? extends Annotation> annotations,
                                             F rawData) throws IllegalAccessException {
        for (Annotation annotation : annotations) {
            Object result = annotationProcessor.process(annotation, rawData);
            if (result == null) continue;
            field.set(bean, result);
            break;
        }
    }

    private boolean checkPresentCustomAnnotationOnField(List<Class<? extends Annotation>> annotations,
                                                        Set<Class<? extends Annotation>> custom) {
        return annotations.stream().noneMatch(custom::contains);
    }

    private void addFields() {
        getAllFieldsList(beanType)
                .forEach(field -> {
                    field.setAccessible(true);
                    Annotation[] annotations = field.getAnnotations();
                    for (Annotation annotation : annotations) {
                        if (annotation.annotationType() != Parsed.class)
                            continue;
                        Parsed parsed = (Parsed) annotation;
                        fields.add(parsed.index(), new Pair<>(field, asList(annotations)));
                        break;
                    }
                    fieldClasses.add(transformAnnotations(annotations));
                });
    }

    private List<Class<? extends Annotation>> transformAnnotations(Annotation[] annotations) {
        return stream(annotations).map(Annotation::annotationType).collect(toUnmodifiableList());
    }

    public static class ParserBuilder<T, F> {
        private Class<T> beanType;

        private AnnotationProcessor<F> annotationProcessor;
        private List<AnnotationHandler> annotationHandlers = new ArrayList<>();

        private FieldProcessor<F> filedProcessor;
        private List<FieldHandler<?, F>> fieldHandlers = new ArrayList<>();

        private RawEntityReader<F> rawEntityReader;

        public ParserBuilder<T, F> withAnnotationHandler(List<AnnotationHandler> annotationHandlers) {
            this.annotationHandlers.addAll(annotationHandlers);
            return this;
        }

        public ParserBuilder<T, F> withFieldHandler(List<FieldHandler<?, F>> fieldHandlers) {
            this.fieldHandlers.addAll(fieldHandlers);
            return this;
        }

        public ParserBuilder<T, F> to(Class<T> beanType) {
            this.beanType = beanType;
            return this;
        }

        public ParserBuilder<T, F> withAnnotationProcessor(AnnotationProcessor<F> annotationProcessor) {
            this.annotationProcessor = annotationProcessor;
            return this;
        }

        public ParserBuilder<T, F> withRawEntityReader(RawEntityReader<F> rawEntityReader) {
            this.rawEntityReader = rawEntityReader;
            return this;
        }

        public ParserBuilder<T, F> from(RawEntityReader<F> rawEntityReader) {
            this.rawEntityReader = rawEntityReader;
            return this;
        }

        public Parser<T, F> build() {
            requireNonNull(beanType);
            requireNonNull(rawEntityReader);
            if (annotationProcessor == null)
                annotationProcessor = new AnnotationProcessorImpl<>(annotationHandlers);
            if (filedProcessor == null)
                filedProcessor = new FieldProcessorImpl<F>(fieldHandlers);
            return new Parser<>(rawEntityReader, beanType, annotationProcessor, filedProcessor);
        }
    }

    private Parser(RawEntityReader<F> rawEntityReader, Class<T> beanType,
                   AnnotationProcessor<F> annotationProcessor,
                   FieldProcessor<F> fieldProcessor) {
        this.rawEntityReader = rawEntityReader;
        this.rawEntityReader.next(); // read header
        this.beanType = beanType;
        fieldClasses = new ArrayList<>();
        fields = new ArrayList<>();
        addFields();
        this.annotationProcessor = annotationProcessor;
        this.fieldProcessor = fieldProcessor;
    }
}
