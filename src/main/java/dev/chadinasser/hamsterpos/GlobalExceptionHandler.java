package dev.chadinasser.hamsterpos;

import dev.chadinasser.hamsterpos.dto.ErrorDto;
import dev.chadinasser.hamsterpos.dto.ResponseDto;
import dev.chadinasser.hamsterpos.exception.ResourceAlreadyExistException;
import dev.chadinasser.hamsterpos.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<ResponseDto<ErrorDto>> createErrorResponse(Exception e, String url, HttpStatus status) {
        ResponseDto<ErrorDto> responseDto = new ResponseDto<>(
                status,
                new ErrorDto(
                        e.getMessage(),
                        url
                )
        );
        return responseDto.toResponseEntity();
    }

    @ExceptionHandler({ResourceAlreadyExistException.class})
    public ResponseEntity<ResponseDto<ErrorDto>> handleException(ResourceAlreadyExistException e, HttpServletRequest request) {
        return createErrorResponse(e, request.getRequestURI(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ResponseDto<ErrorDto>> handleException(ResourceNotFoundException e, HttpServletRequest request) {
        return createErrorResponse(e, request.getRequestURI(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ResponseDto<ErrorDto>> handleException(HttpMessageNotReadableException ignored, HttpServletRequest request) {
        return createErrorResponse(new Exception("Malformed JSON request"), request.getRequestURI(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseDto<ErrorDto>> handleException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String errorMessage = e.getBindingResult().getAllErrors().stream().reduce("", (msg, err) -> msg + err.getDefaultMessage() + "; ", String::concat);
        if (errorMessage.endsWith("; ")) {
            errorMessage = errorMessage.substring(0, errorMessage.length() - 2);
        }
        return createErrorResponse(new Exception(errorMessage), request.getRequestURI(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ResponseDto<ErrorDto>> handleException(Exception e, HttpServletRequest request) {
        return createErrorResponse(new Exception("An unexpected error occurred"), request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
