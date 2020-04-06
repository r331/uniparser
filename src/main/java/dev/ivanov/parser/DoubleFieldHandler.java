package dev.ivanov.parser;

import static java.lang.Double.parseDouble;

public class DoubleFieldHandler implements FieldHandler<Double, String> {
    @Override
    public Double process(String value) {
        try {
            return parseDouble(value);
        } catch (NumberFormatException ex) {
            return 0.0;
        }
    }

    @Override
    public Class<Double> getFieldType() {
        return Double.class;
    }
}