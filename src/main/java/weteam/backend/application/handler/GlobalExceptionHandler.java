package weteam.backend.application.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JsonParseException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.webjars.NotFoundException;
import weteam.backend.application.handler.exception.BadRequestException;
import weteam.backend.application.handler.exception.ExceptionError;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ExceptionError handleRuntime(final RuntimeException e) {
        log.warn(e.getClass().toString());
        log.warn(e.getMessage());
        return buildExceptionError(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ExceptionError handleMethodArgumentNotValid(final MethodArgumentNotValidException e) {
        log.warn(e.getMessage());
        return buildExceptionError(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ExceptionError handleBadRequest(final BadRequestException e) {
        log.warn(e.getMessage());
        return buildExceptionError(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ExceptionError handleDuplicateKey(DuplicateKeyException e) {
        log.warn(e.getMessage());
        return buildExceptionError(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ExceptionError handleNotFound(NotFoundException e) {
        log.warn(e.getMessage());
        return buildExceptionError(e, HttpStatus.NOT_FOUND);
    }

    private ExceptionError buildExceptionError(Exception exception, HttpStatus status) {
        return ExceptionError
                .builder()
                .message(exception.getMessage())
                .statusMessage(status.getReasonPhrase())
                .statusCode(status.value())
                .build();
    }
}
