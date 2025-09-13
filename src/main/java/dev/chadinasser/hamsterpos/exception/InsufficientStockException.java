package dev.chadinasser.hamsterpos.exception;

public class InsufficientStockException extends IllegalArgumentException {
    public InsufficientStockException(String productName, int available, int requested) {
        super(String.format("Insufficient stock for product '%s'. Available: %d, Requested: %d",
              productName, available, requested));
    }
}
