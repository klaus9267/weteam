package weteam.backend.application.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import weteam.backend.application.handler.exception.ExceptionError;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  protected ExceptionError handleRuntime(final RuntimeException e, HttpServletRequest request) {
    logRequestDetails(request, e);
    return buildExceptionError(e, HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ExceptionError handleMethodArgumentNotValid(final MethodArgumentNotValidException e, HttpServletRequest request) {
    logRequestDetails(request, e);
    return buildExceptionError(e, HttpStatus.BAD_REQUEST);
  }
  
  private ExceptionError buildExceptionError(Exception exception, HttpStatus status) {
    return ExceptionError.builder()
                         .message(exception.getMessage())
                         .statusMessage(status.getReasonPhrase())
                         .statusCode(status.value())
                         .build();
  }
  
  private void logRequestDetails(HttpServletRequest request, Exception e) {
    String method = request.getMethod();
    String requestURI = request.getRequestURI();
    String queryString = request.getQueryString();
    String sessionId = request.getRemoteUser();
    String fullRequestPath = queryString != null ? requestURI + "?" + queryString : requestURI;
    
    log.error("");
    log.error("Request {} '{}' who {}", method, fullRequestPath, sessionId);
    log.error(e.toString());
    log.error("");
    log.error("-------------------------------------------------------------------------------------------------------------------");
  }
}
