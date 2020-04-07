package dev.ivanov.parser.csv;

import dev.ivanov.parser.FieldHandler;

public class StringFieldHandler implements FieldHandler<String, String> {
    @Override
    public String process(String value) {
        return value;
    }

    @Override
    public Class<String> getFieldType() {
        return String.class;
    }
}
