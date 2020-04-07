package dev.ivanov.parser.csv;

import dev.ivanov.parser.FieldHandler;

import static java.lang.Integer.parseInt;

public class IntegerFieldHandler implements FieldHandler<Integer, String> {
    @Override
    public Integer process(String value) {
        try {
            return parseInt(value);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    @Override
    public Class<Integer> getFieldType() {
        return Integer.class;
    }
}