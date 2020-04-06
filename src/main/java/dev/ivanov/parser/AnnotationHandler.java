package dev.ivanov.parser;

import java.lang.annotation.Annotation;

public interface AnnotationHandler<A extends Annotation, T> {
    Object process(A annotation, T target);

    Class<A> getAnnotationClass();
}