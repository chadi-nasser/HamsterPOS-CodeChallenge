package dev.chadinasser.hamsterpos;

import dev.chadinasser.hamsterpos.dto.ErrorDto;
import dev.chadinasser.hamsterpos.dto.ResponseDto;
import dev.chadinasser.hamsterpos.exception.ResourceAlreadyExistException;
import dev.chadinasser.hamsterpos.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Stream;

@ControllerAdvice
public class GlobalExceptionHandler {
    private ResponseDto<ErrorDto> createErrorResponse(Exception e, String url, HttpStatus status) {
        return new ResponseDto<>(
                status,
                new ErrorDto(
                        List.of(e.getMessage()),
                        url
                )
        );
    }

    @ExceptionHandler({PropertyReferenceException.class})
    public ResponseDto<ErrorDto> handleException(PropertyReferenceException e, HttpServletRequest request) {
        return createErrorResponse(new IllegalArgumentException("Invalid pagination parameters: " + e.getPropertyName()), request.getRequestURI(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({NoResourceFoundException.class})
    public ResponseDto<ErrorDto> handleException(NoResourceFoundException ignored, HttpServletRequest request) {
        return createErrorResponse(new Exception("Resource not found"), request.getRequestURI(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseDto<ErrorDto> handleException(BadCredentialsException ignored, HttpServletRequest request) {
        return createErrorResponse(new Exception("Invalid username or password"), request.getRequestURI(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AuthorizationDeniedException.class})
    public ResponseDto<ErrorDto> handleException(AuthorizationDeniedException ignored, HttpServletRequest request) {
        return createErrorResponse(new Exception("Unauthorized"), request.getRequestURI(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseDto<ErrorDto> handleException(AccessDeniedException ignored, HttpServletRequest request) {
        return createErrorResponse(new Exception("Access denied"), request.getRequestURI(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseDto<ErrorDto> handleException(IllegalArgumentException e, HttpServletRequest request) {
        return createErrorResponse(e, request.getRequestURI(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({ResourceAlreadyExistException.class})
    public ResponseDto<ErrorDto> handleException(ResourceAlreadyExistException e, HttpServletRequest request) {
        return createErrorResponse(e, request.getRequestURI(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseDto<ErrorDto> handleException(ResourceNotFoundException e, HttpServletRequest request) {
        return createErrorResponse(e, request.getRequestURI(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseDto<ErrorDto> handleException(HttpMessageNotReadableException ignored, HttpServletRequest request) {
        return createErrorResponse(new Exception("Malformed JSON request"), request.getRequestURI(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseDto<ErrorDto> handleException(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<String> fieldErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .toList();

        List<String> globalErrors = e.getBindingResult()
                .getGlobalErrors()
                .stream()
                .map(ge -> ge.getObjectName() + ": " + (ge.getDefaultMessage() == null ? "Invalid request" : ge.getDefaultMessage()))
                .toList();

        List<String> errors = Stream.concat(fieldErrors.stream(), globalErrors.stream())
                .distinct()
                .toList();

        ErrorDto errorDto = new ErrorDto(errors, request.getRequestURI());
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, errorDto);
    }

    @ExceptionHandler({Exception.class})
    public ResponseDto<ErrorDto> handleException(Exception ignored, HttpServletRequest request) {
        return createErrorResponse(new Exception("An unexpected error occurred"), request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
