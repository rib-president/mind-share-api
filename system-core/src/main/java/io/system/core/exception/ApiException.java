package io.system.core.exception;

public class ApiException extends RuntimeException{
  private PublicException e;

  public ApiException(PublicException e) {
    super(e.getCode() + " : " + e.getStatus());
  }
}
