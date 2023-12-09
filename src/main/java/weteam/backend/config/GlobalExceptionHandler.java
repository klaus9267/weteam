package weteam.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import weteam.backend.config.dto.ApiMetaData;
import weteam.backend.config.dto.ExceptionMetaData;
import weteam.backend.config.dto.ExceptionResponse;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ExceptionMetaData<?> handleRuntimeException(final RuntimeException e) {
        return ExceptionMetaData.builder()
                                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                                .data(new ExceptionResponse(null, e.getMessage()))
                                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ExceptionMetaData<?> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        List<ExceptionResponse> errorResponseList = e.getBindingResult()
                                                     .getAllErrors()
                                                     .stream()
                                                     .map(error -> ExceptionResponse.builder().field(error.getCodes()[1]).message(error.getDefaultMessage()).build())
                                                     .toList();
        return ExceptionMetaData.builder().httpStatus(HttpStatus.BAD_REQUEST).data(errorResponseList).build();
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ExceptionMetaData<?> handleDuplicateKeyException(DuplicateKeyException e) {
        return ExceptionMetaData.builder().httpStatus(HttpStatus.BAD_REQUEST).data(e.getMessage()).build();
    }
}
