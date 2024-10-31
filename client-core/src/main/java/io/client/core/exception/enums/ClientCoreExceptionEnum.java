package io.client.core.exception.enums;

import io.system.core.exception.PublicException;
import org.springframework.http.HttpStatus;

public enum ClientCoreExceptionEnum implements PublicException {
  // 액세스 토큰 만료
  EXPIRED_ACCESS_TOKEN("E401000001",HttpStatus.UNAUTHORIZED),
  EXPIRED_REFRESH_TOKEN("E401000002", HttpStatus.UNAUTHORIZED);

  private final String code;
  private final HttpStatus status;

  ClientCoreExceptionEnum(String code, HttpStatus status) {
    this.code = code;
    this.status = status;
  }

  @Override
  public String getCode() {
    return this.code;
  }

  @Override
  public HttpStatus getStatus() {
    return this.status;
  }
}
