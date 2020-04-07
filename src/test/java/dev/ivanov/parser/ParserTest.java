package dev.ivanov.parser;

import java.util.List;

public class ParserTest {
    public static <T, F> Parser<T, F> getParser(Class<T> clazz,
                                         RawEntityReader<F> rawEntityReader,
                                         List<AnnotationHandler> annotationHandlerMap,
                                         List<FieldHandler<?, F>> fieldHandlers) {
        return Parser.<T, F>builder()
                .withRawEntityReader(rawEntityReader)
                .withAnnotationProcessor(new AnnotationProcessorImpl<>(annotationHandlerMap))
                .withFieldHandler(fieldHandlers)
                .to(clazz)
                .build();
    }
}
