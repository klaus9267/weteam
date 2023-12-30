package weteam.backend.application.handler.exception;

import weteam.backend.application.Message;

public class DuplicateKeyException extends RuntimeException {
    public DuplicateKeyException(Message message) {
        super(message.getMessage());
    }
}
