package weteam.backend.application.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.webjars.NotFoundException;
import weteam.backend.application.common.ExceptionMetaData;
import weteam.backend.application.common.ExceptionResponse;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ExceptionMetaData<?> handleRuntime(final RuntimeException e) {
        return buildExceptionMetaData(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ExceptionMetaData<?> handleMethodArgumentNotValid(final MethodArgumentNotValidException e) {
        List<ExceptionResponse> errorResponseList = e.getBindingResult()
                                                     .getAllErrors()
                                                     .stream()
                                                     .map(error -> ExceptionResponse.builder().field(error.getCodes()[1]).message(error.getDefaultMessage()).build())
                                                     .toList();
        return ExceptionMetaData.builder().httpStatus(HttpStatus.BAD_REQUEST).message("validation exception").data(errorResponseList).build();
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ExceptionMetaData<?> handleDuplicateKey(DuplicateKeyException e) {
        return buildExceptionMetaData(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ExceptionMetaData<?> handleNotFound(NotFoundException e) {
        return buildExceptionMetaData(e, HttpStatus.NOT_FOUND);
    }

    private ExceptionMetaData<?> buildExceptionMetaData(Exception exception, HttpStatus status) {
        return ExceptionMetaData.builder()
                                .message(exception.getMessage())
                                .httpStatus(status)
                                .build();
    }
}
