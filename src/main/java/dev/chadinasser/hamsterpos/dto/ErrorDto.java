package dev.chadinasser.hamsterpos.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorDto {
    private String message;
    private String path;
    private LocalDateTime timestamp;

    public ErrorDto(String message, String path) {
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
}
