package com.ega.to_do_app.tasks.domain.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskNotFoundException extends RuntimeException{

    private final String code;
    private final String message;
    private final HttpStatus status;
    private final LocalDateTime timestamp;

    public TaskNotFoundException() {
        this.code = TaskExceptionCatalog.TASK_NOT_FOUND.getExceptionCode();
        this.message = TaskExceptionCatalog.TASK_NOT_FOUND.getExceptionMessage();
        this.status = HttpStatus.BAD_REQUEST;
        this.timestamp = LocalDateTime.now();
    }

}
