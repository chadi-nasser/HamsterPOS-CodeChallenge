package dev.chadinasser.hamsterpos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {
    private HttpStatus status;
    private T data;
    private ErrorDto error;

    public ResponseDto(HttpStatus status, T data) {
        this.status = status;
        this.data = data;
    }

    public ResponseDto(HttpStatus status, ErrorDto error) {
        this.status = status;
        this.error = error;
    }

    public ResponseEntity<ResponseDto<T>> toResponseEntity() {
        return ResponseEntity.status(this.status).body(this);
    }
}
