package com.ega.to_do_app.tasks.domain.exceptions;

import lombok.Getter;

@Getter
public enum TaskExceptionCatalog {

    TASK_NOT_FOUND("T-001", "Task was not found"),
    INVALID_INPUT_RECEIVED("T-002", "Provided task name is not valid for search"),
    INVALID_FIELD_DATA ("T-003", "Invalid field data received, " +
            "check details for more information"),
    MISMATCHED_DATES("T-004", "New start date cannot be after than due date saved");

    private final String exceptionCode;
    private final String exceptionMessage;

    TaskExceptionCatalog(String exceptionCode, String exceptionMessage) {
        this.exceptionCode = exceptionCode;
        this.exceptionMessage = exceptionMessage;
    }

}
