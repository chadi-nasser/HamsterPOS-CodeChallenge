package dev.chadinasser.hamsterpos.exception;

public class ResourceNotFoundException extends ResourceBaseException {
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(resourceName, fieldName, fieldValue);
    }

    @Override
    public String message() {
        return String.format("%s not found with %s : '%s'", getResourceName(), getFieldName(), getFieldValue());
    }
}
