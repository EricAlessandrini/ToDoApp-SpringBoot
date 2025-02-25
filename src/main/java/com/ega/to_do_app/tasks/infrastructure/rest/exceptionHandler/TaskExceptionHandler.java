package com.ega.to_do_app.tasks.infrastructure.rest.exceptionHandler;

import com.ega.to_do_app.tasks.domain.exceptions.InvalidInputException;
import com.ega.to_do_app.tasks.domain.exceptions.MismatchedDatesException;
import com.ega.to_do_app.tasks.domain.exceptions.TaskExceptionCatalog;
import com.ega.to_do_app.tasks.domain.exceptions.TaskNotFoundException;
import com.ega.to_do_app.tasks.infrastructure.rest.dtos.ExceptionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "com.ega.to_do_app.tasks.infrastructure.rest")
public class TaskExceptionHandler {

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ExceptionResponseDto> taskNameInvalidExceptionHandler(InvalidInputException ex) {
        ExceptionResponseDto response = ExceptionResponseDto.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .timestamp(ex.getTimestamp())
                .build();
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> taskNotFoundExceptionHandler(TaskNotFoundException ex) {
        ExceptionResponseDto response = ExceptionResponseDto.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .timestamp(ex.getTimestamp())
                .build();
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> invalidArgumentsExceptionHandler(MethodArgumentNotValidException ex) {
        BindingResult results = ex.getBindingResult();

        Map<String, String> exceptionDetails = new HashMap<>();
        results.getFieldErrors().forEach(
                fieldError -> exceptionDetails.put(
                        fieldError.getField(),
                        fieldError.getDefaultMessage())
        );

        ExceptionResponseDto response = ExceptionResponseDto.builder()
                .code(TaskExceptionCatalog.INVALID_FIELD_DATA.getExceptionCode())
                .message(TaskExceptionCatalog.INVALID_FIELD_DATA.getExceptionMessage())
                .details(exceptionDetails)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MismatchedDatesException.class)
    public ResponseEntity<ExceptionResponseDto> mismatchedDatesExceptionHandler(MismatchedDatesException ex) {
        ExceptionResponseDto response = ExceptionResponseDto.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .timestamp(ex.getTimestamp())
                .build();

        return new ResponseEntity<>(response, ex.getStatus());
    }

}
