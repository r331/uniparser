package dev.ivanov.parser;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@SuppressWarnings("rawtypes")
public class AnnotationProcessorImpl<T> implements AnnotationProcessor<T> {
    private final Map<Class<? extends Annotation>, AnnotationHandler> handleAnnotationMap;

    public AnnotationProcessorImpl(List<AnnotationHandler> annotationHandlerMap) {
        this.handleAnnotationMap = annotationHandlerMap.stream()
                .collect(toMap(AnnotationHandler::getAnnotationClass, identity()));
    }

    @SuppressWarnings("unchecked")
    public Object process(Annotation annotation, T value) {
        var handler = handleAnnotationMap.get(annotation.annotationType());
        return handler == null ? null : handler.process(annotation, value);
    }

    public Set<Class<? extends Annotation>> getAnnotations() {
        return handleAnnotationMap.keySet();
    }
}
