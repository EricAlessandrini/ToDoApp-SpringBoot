package com.ega.to_do_app.tasks.domain.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class TaskNotFoundException extends RuntimeException{

    private final HttpStatus httpStatus;

    public TaskNotFoundException(String message, HttpStatus status) {
        super(message);
        this.httpStatus = status;
    }

}
