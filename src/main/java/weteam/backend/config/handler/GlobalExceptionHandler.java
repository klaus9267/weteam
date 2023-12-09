package weteam.backend.config.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import weteam.backend.config.dto.ErrorResponse;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        log.error(e.getClass().toString());
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List< ObjectError> errorList = e.getBindingResult().getAllErrors();
        List<ErrorResponse> errorResponseList = errorList.stream().map(error->{
            return ErrorResponse.builder()
                                .field(error.getCodes()[1])
                                .defaultMessage(error.getDefaultMessage())
                                .build();
        }).toList();

        errorList.forEach(error -> log.error(error.toString()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseList);
    }
}
