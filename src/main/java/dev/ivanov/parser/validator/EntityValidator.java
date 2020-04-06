package dev.ivanov.parser.validator;


import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;


public interface EntityValidator {
    Logger log = LoggerFactory.getLogger(EntityValidator.class);

    void validate(Object entity);

    default boolean isValid(Object o) {
        try {
            validate(o);
            return true;
        } catch (Exception e) {
            log.warn("Bad record: " + o);
            return false;
        }
    }
}