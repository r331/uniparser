package dev.ivanov.parser;

public interface FieldHandler<T, P> {
    T process(P value);

    Class<T> getFieldType();
}
