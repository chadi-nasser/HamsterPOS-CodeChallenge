package dev.chadinasser.hamsterpos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseDto<T> extends ResponseEntity<Object> {
    public ResponseDto(HttpStatus status, T data) {
        super(new ResponseBody<>(status, null, data), status);
    }

    public ResponseDto(HttpStatus status, ErrorDto error) {
        super(new ResponseBody<>(status, error, null), status);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record ResponseBody<T>(HttpStatus status, ErrorDto error, T data) {
    }
}
