package dev.ivanov.parser;

import java.lang.reflect.Field;

public interface FieldProcessor<T> {
    Object process(Field field, Object target, T value);
}
