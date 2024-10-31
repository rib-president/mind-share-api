package io.client.core.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@RestControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(AuthenticationException.class)
  protected ResponseEntity<ProblemDetail> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
    ProblemDetail body = createProblemDetail(ex, HttpStatus.UNAUTHORIZED, ex.getMessage(),
        null, null, request);
    body.setType(URI.create(request.getDescription(false)));

    return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AccessDeniedException.class)
  protected ResponseEntity<ProblemDetail> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
    ProblemDetail body = createProblemDetail(ex, HttpStatus.FORBIDDEN, ex.getMessage(),
        request.getDescription(false), new Object[]{}, request);
    body.setType(URI.create(request.getDescription(false)));

    return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
  }
}
