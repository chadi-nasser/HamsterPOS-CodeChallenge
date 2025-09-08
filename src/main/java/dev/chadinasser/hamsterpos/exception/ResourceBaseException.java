package dev.chadinasser.hamsterpos.exception;

import lombok.Getter;

@Getter
public abstract class ResourceBaseException extends RuntimeException {
    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    public ResourceBaseException(String resourceName, String fieldName, Object fieldValue) {
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    @Override
    public String getMessage() {
        return message();
    }

    public abstract String message();
}
