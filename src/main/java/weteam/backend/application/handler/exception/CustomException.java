package weteam.backend.application.handler.exception;

import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class CustomException extends RuntimeException {
  private final ErrorCode errorCode;
  private final String message;

  public CustomException(final ErrorCode errorCode, final String message) {
    super(message);
    this.errorCode = errorCode;
    this.message = message;
  }

  public CustomException(final ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.message = errorCode.getMessage();
  }
}
