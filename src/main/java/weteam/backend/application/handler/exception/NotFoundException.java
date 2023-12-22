package weteam.backend.application.handler.exception;

import weteam.backend.application.message.ExceptionMessage;

public class NotFoundException extends RuntimeException{
    public NotFoundException(ExceptionMessage message){
        super(message.getMessage());
    }
}
