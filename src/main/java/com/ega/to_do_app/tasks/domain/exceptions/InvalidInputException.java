package com.ega.to_do_app.tasks.domain.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class InvalidInputException extends RuntimeException {

    private final String code;
    private final String message;
    private final HttpStatus status;
    private final LocalDateTime timestamp;

    public InvalidInputException() {
        this.code = TaskExceptionCatalog.INVALID_INPUT_RECEIVED.getExceptionCode();
        this.message = TaskExceptionCatalog.INVALID_INPUT_RECEIVED.getExceptionMessage();
        this.status = HttpStatus.BAD_REQUEST;
        this.timestamp = LocalDateTime.now();
    }

}
