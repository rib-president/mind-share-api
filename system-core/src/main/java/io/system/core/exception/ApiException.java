package io.system.core.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException{
  private final PublicException e;

  public ApiException(PublicException e) {
    super(e.getCode() + " : " + e.getStatus());
    this.e = e;
  }
}
