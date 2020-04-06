package dev.ivanov.parser;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class FieldProcessorImpl<T> implements FieldProcessor<T> {
    private final Map<Class<?>, FieldHandler<?, T>> handleFieldMap;

    public FieldProcessorImpl(List<FieldHandler<?, T>> fieldHandlers) {
        this.handleFieldMap = fieldHandlers.stream()
                .collect(toMap(FieldHandler::getFieldType, identity()));
    }

    @Override
    @SneakyThrows
    public Object process(Field field, Object target, T value) {
        var handler = handleFieldMap.get(field.getType());
        if (handler == null) return null;
        var calculatedField = handler.process(value);
        if (calculatedField == null) return null;
        field.set(target, calculatedField);
        return this;
    }
}
