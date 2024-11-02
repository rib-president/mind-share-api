package io.client.core.advice;

import io.system.core.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;

@RestControllerAdvice
public class ExceptionAdvice {
  @ExceptionHandler(AuthenticationException.class)
  protected ResponseEntity<ProblemDetail> handleAuthenticationException(AuthenticationException ex, WebRequest request) {

    ProblemDetail body = createProblemDetail(ex, HttpStatus.UNAUTHORIZED, ex.getMessage(),
        null, null, request);
    body.setType(URI.create(request.getDescription(false)));

//    ErrorResponse response = ErrorResponse.builder(ex, body).build(getMessageSource(), LocaleContextHolder.getLocale());
    return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(AccessDeniedException.class)
  protected ResponseEntity<ProblemDetail> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
    ProblemDetail body = createProblemDetail(ex, HttpStatus.FORBIDDEN, ex.getMessage(),
        request.getDescription(false), new Object[]{}, request);
    body.setType(URI.create(request.getDescription(false)));

//    ErrorResponse response = ErrorResponse.builder(ex, body).build(getMessageSource(), LocaleContextHolder.getLocale());
    return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(ApiException.class)
  protected ResponseEntity<ProblemDetail> handleApiException(ApiException ex, WebRequest request) {
    ex.printStackTrace();

    ProblemDetail body = createProblemDetail(ex, ex.getE().getStatus(), ex.getMessage(),
        request.getDescription(false), new Object[]{}, request);
    body.setType(URI.create(request.getDescription(false)));
    body.setTitle(ex.getE().getName());

    return new ResponseEntity<>(body, ex.getE().getStatus());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
    ex.printStackTrace();

    ProblemDetail body = createProblemDetail(ex, HttpStatus.BAD_REQUEST, ex.getMessage(),
        request.getDescription(false), new Object[]{}, request);
    body.setType(URI.create(request.getDescription(false)));

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  protected ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(HttpMessageNotReadableException ex, WebRequest request) {
    ex.printStackTrace();

    ProblemDetail body = createProblemDetail(ex, HttpStatus.BAD_REQUEST, ex.getMessage(),
        request.getDescription(false), new Object[]{}, request);
    body.setType(URI.create(request.getDescription(false)));

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  protected ResponseEntity<ProblemDetail> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
    ex.printStackTrace();

    ProblemDetail body = createProblemDetail(ex, HttpStatus.NOT_FOUND, ex.getMessage(),
        request.getDescription(false), new Object[]{}, request);
    body.setType(URI.create(request.getDescription(false)));

    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(RuntimeException.class)
  protected ResponseEntity<ProblemDetail> handleRuntimeException(RuntimeException ex, WebRequest request) {
    ex.printStackTrace();

    ProblemDetail body = createProblemDetail(ex, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(),
        request.getDescription(false), new Object[]{}, request);
    body.setType(URI.create(request.getDescription(false)));

    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ProblemDetail> handleException(Exception ex, WebRequest request) {
    ex.printStackTrace();

    ProblemDetail body = createProblemDetail(ex, HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(),
        request.getDescription(false), new Object[]{}, request);
    body.setType(URI.create(request.getDescription(false)));

    return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  private ProblemDetail createProblemDetail(Exception ex, HttpStatusCode status, String defaultDetail, @Nullable String detailMessageCode, @Nullable Object[] detailMessageArguments, WebRequest request) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, defaultDetail);
    problemDetail.setDetail(defaultDetail);
    problemDetail.setInstance(URI.create(request.getDescription(false)));
    return problemDetail;
  }
}
