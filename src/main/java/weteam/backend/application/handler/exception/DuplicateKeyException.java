package weteam.backend.application.handler.exception;

import weteam.backend.application.ExceptionMessage;

public class DuplicateKeyException extends RuntimeException {
    public DuplicateKeyException(ExceptionMessage message) {
        super(message.getMessage());
    }
}
