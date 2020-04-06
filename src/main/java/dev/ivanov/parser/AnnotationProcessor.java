package dev.ivanov.parser;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface AnnotationProcessor<T> {
    Object process(Annotation annotation, T value);

    Set<Class<? extends Annotation>> getAnnotations();
}
