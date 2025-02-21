package com.ega.to_do_app.tasks.domain.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class MismatchedDatesException extends RuntimeException {
  private final String code;
  private final String message;
  private final HttpStatus status;
  private final LocalDateTime timestamp;

  public MismatchedDatesException() {
    this.code = TaskExceptionCatalog.MISMATCHED_DATES.getExceptionCode();
    this.message = TaskExceptionCatalog.MISMATCHED_DATES.getExceptionMessage();
    this.status = HttpStatus.BAD_REQUEST;
    this.timestamp = LocalDateTime.now();
  }
}
