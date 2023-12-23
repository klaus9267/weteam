package weteam.backend.application.handler.exception;

import weteam.backend.application.ExceptionMessage;

public class BadRequestException extends RuntimeException{
    public BadRequestException(ExceptionMessage message) {
        super(message.getMessage());
    }
}
