package dev.ivanov.parser.validator;


public interface EntityValidator {
    void validate(Object entity);

    default boolean isValid(Object o) {
        try {
            validate(o);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}