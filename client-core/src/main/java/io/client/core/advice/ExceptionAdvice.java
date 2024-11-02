package io.client.core.advice;

import io.system.core.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;

@RestControllerAdvice
public class ExceptionAdvice {

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
