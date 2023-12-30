package weteam.backend.application.handler.exception;

import weteam.backend.application.Message;

public class BadRequestException extends RuntimeException{
    public BadRequestException(Message message) {
        super(message.getMessage());
    }
}
