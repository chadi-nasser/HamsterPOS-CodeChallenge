package dev.chadinasser.hamsterpos.exception;

public class InvalidRefreshTokenException extends IllegalArgumentException {
    public InvalidRefreshTokenException() {
        super("The provided refresh token is invalid or has expired.");
    }
}
