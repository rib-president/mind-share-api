package io.system.core.exception.enums;

import io.system.core.exception.PublicException;
import org.springframework.http.HttpStatus;

public enum ApiExceptionEnum implements PublicException {
  // 400: 잘못된 문법으로 인하여 서버가 요청을 이해할 수 없음
  BAD_REQUEST("E40000000", HttpStatus.BAD_REQUEST),

  // 401: 비인증(unauthenticated)된 요청 -> 서버는 클라이언트가 누군지 모름
  UNAUTHORIZED("E40100000", HttpStatus.UNAUTHORIZED),

  // 403: 콘텐츠에 접근할 권리 없음 -> 서버는 클라이언트가 누군지 알고 있음
  FORBIDDEN("E40300000", HttpStatus.FORBIDDEN),

  // 404: 요청받은 리소스를 찾을 수 없음
  NOT_FOUND("E40444444", HttpStatus.NOT_FOUND),

  // 405: 제거되었거나 사용할 수 없는 메소드 요청
  METHOD_NOT_ALLOWED("E40500000", HttpStatus.METHOD_NOT_ALLOWED),

  // 406: 정해진 규격에 따른 컨텐츠가 없을 때
  NOT_ACCEPTABLE("E40600000", HttpStatus.NOT_ACCEPTABLE),

  // 409: 요청이 현재 서버의 상태와 충돌
  CONFLICT("E40900000", HttpStatus.CONFLICT),

  // 422: 요청은 올바르지만 문법 오류로 인하여 실행할 수 없음
  UNPROCESSABLE_ENTITY("E42200000", HttpStatus.UNPROCESSABLE_ENTITY),

  // 500: 서버가 처리방법을 모르는 상황 발생
  INTERNAL_SERVER_ERROR("E50000000", HttpStatus.INTERNAL_SERVER_ERROR);

  private final String code;
  private final HttpStatus status;

  ApiExceptionEnum(String code, HttpStatus status) {
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
