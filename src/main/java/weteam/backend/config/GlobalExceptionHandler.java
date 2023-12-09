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
import weteam.backend.config.dto.ErrorResponse;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse handleRuntimeException(final RuntimeException e) {
        return ErrorResponse.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).defaultMessage(e.getMessage()).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiMetaData<List<ErrorResponse>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        List<ObjectError> errorList = e.getBindingResult().getAllErrors();
        List<ErrorResponse> errorResponseList = errorList.stream()
                                                         .map(error -> ErrorResponse.builder().field(error.getCodes()[1]).defaultMessage(error.getDefaultMessage()).build())
                                                         .toList();
        errorList.forEach(error -> log.error(error.toString()));
        return new ApiMetaData<>(HttpStatus.BAD_REQUEST, errorResponseList);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleDuplicateKeyException(DuplicateKeyException e) {
        return ErrorResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).defaultMessage(e.getMessage()).build();
    }
}
