package weteam.backend.application.handler.exception;

import weteam.backend.application.Message;

public class NotFoundException extends RuntimeException{
    public NotFoundException(Message message){
        super(message.getMessage());
    }
}
