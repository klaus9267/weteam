package weteam.backend.application.handler.exception;

import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class CustomException extends RuntimeException {
  private final CustomErrorCode customErrorCode;
  private final String message;

  public CustomException(final CustomErrorCode customErrorCode, final String message) {
    super(message);
    this.customErrorCode = customErrorCode;
    this.message = message;
  }

  public CustomException(final CustomErrorCode customErrorCode) {
    super(customErrorCode.getMessage());
    this.customErrorCode = customErrorCode;
    this.message = customErrorCode.getMessage();
  }

  public static Supplier<CustomException> notFound(final CustomErrorCode customErrorCode) {
    return () -> new CustomException(customErrorCode);
  }
}
