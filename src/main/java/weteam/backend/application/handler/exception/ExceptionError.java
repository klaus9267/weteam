package weteam.backend.application.handler.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;

public record ExceptionError(String message, String statusMessage, Integer statusCode) {
    @Builder
    public ExceptionError(String message, String statusMessage, Integer statusCode) {
        this.message = message;
        this.statusMessage = statusMessage;
        this.statusCode = statusCode != null ? statusCode : HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
}
