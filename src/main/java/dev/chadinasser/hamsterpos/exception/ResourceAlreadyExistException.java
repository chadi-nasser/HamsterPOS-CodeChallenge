package dev.chadinasser.hamsterpos.exception;

public class ResourceAlreadyExistException extends ResourceBaseException {
    public ResourceAlreadyExistException(String resourceName, String fieldName, Object fieldValue) {
        super(resourceName, fieldName, fieldValue);
    }

    @Override
    public String message() {
        return String.format("%s already exists with %s : '%s'", getResourceName(), getFieldName(), getFieldValue());
    }
}
