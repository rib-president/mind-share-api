package io.system.core.exception;

import org.springframework.http.HttpStatus;

public interface PublicException {
  String getCode();
  HttpStatus getStatus();
}
