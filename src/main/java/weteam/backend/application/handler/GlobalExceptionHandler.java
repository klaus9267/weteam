package weteam.backend.application.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.application.handler.exception.ExceptionError;
import weteam.backend.application.slack.SlackService;

import java.io.IOException;
import java.util.HashMap;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {
  private final SlackService slackService;

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  protected ExceptionError handleException(final Exception e, HttpServletRequest request) {
    logRequestDetails(request, e, "Exception");
    sendSlackMessage(request, e, HttpStatus.INTERNAL_SERVER_ERROR);
    return buildExceptionError(e, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  protected ExceptionError handleRuntime(final RuntimeException e, HttpServletRequest request) {
    logRequestDetails(request, e, "RuntimeException");
    sendSlackMessage(request, e, HttpStatus.INTERNAL_SERVER_ERROR);
    return buildExceptionError(e, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(IOException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  protected ExceptionError handleIO(final IOException e, HttpServletRequest request) {
    logRequestDetails(request, e, "IOException");
    sendSlackMessage(request, e, HttpStatus.INTERNAL_SERVER_ERROR);
    return buildExceptionError(e, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(CustomException.class)
  protected ResponseEntity<ExceptionError> handleCustom(final CustomException e, HttpServletRequest request) {
    log.error("status code : " + e.getCustomErrorCode().getHttpStatus());
    logRequestDetails(request, e, "CustomException");
    ExceptionError error = buildExceptionError(e, e.getCustomErrorCode().getHttpStatus());
    return ResponseEntity.status(e.getCustomErrorCode().getHttpStatus()).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ExceptionError handleMethodArgumentNotValid(final MethodArgumentNotValidException e, HttpServletRequest request) {
    logRequestDetails(request, e, "MethodArgumentNotValidException");
    return buildExceptionError(e, HttpStatus.BAD_REQUEST);
  }

  private ExceptionError buildExceptionError(Exception exception, HttpStatus status) {
    return ExceptionError.builder()
        .message(exception.getMessage())
        .statusMessage(status.getReasonPhrase())
        .statusCode(status.value())
        .build();
  }

  private void logRequestDetails(HttpServletRequest request, Exception e, String exception) {
    String method = request.getMethod();
    String requestURI = request.getRequestURI();
    String queryString = request.getQueryString();
    String sessionId = request.getRemoteUser();
    String fullRequestPath = queryString != null ? requestURI + "?" + queryString : requestURI;

    log.error("");
    log.error("Request {} '{}' who {}", method, fullRequestPath, sessionId);
    log.error(exception, e);
    log.error("");
    log.error("-------------------------------------------------------------------------------------------------------------------");
  }

  private void sendSlackMessage(HttpServletRequest request, Exception e, HttpStatus status) {
    String method = request.getMethod();
    String requestURI = request.getRequestURI();
    String queryString = request.getQueryString();
    String sessionId = request.getRemoteUser();
    String fullRequestPath = queryString != null ? requestURI + "?" + queryString : requestURI;

    String title = method + " | " + fullRequestPath + " | " + sessionId;
    String subtitle = status.getReasonPhrase() + " | " + status.value();
    HashMap<String, String> content = new HashMap<>();
    content.put(subtitle, e.getMessage());

    slackService.sendMessage(title, content);
  }
}
