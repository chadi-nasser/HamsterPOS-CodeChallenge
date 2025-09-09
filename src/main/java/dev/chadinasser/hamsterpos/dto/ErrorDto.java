package dev.chadinasser.hamsterpos.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ErrorDto {
    private List<String> errors;
    private String path;
    private LocalDateTime timestamp;

    public ErrorDto(List<String> errors, String path) {
        this.errors = errors;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
}
