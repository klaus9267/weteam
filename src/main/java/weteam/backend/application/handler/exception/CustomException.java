package weteam.backend.application.handler.exception;

import lombok.Getter;
import weteam.backend.application.CustomErrorCode;

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
}
