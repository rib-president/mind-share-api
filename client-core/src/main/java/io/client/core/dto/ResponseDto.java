package io.client.core.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.ProblemDetail;

@Builder
@Getter
@ToString
public class ResponseDto {
  private Object id;
  private String uri;
  private Object data;
  private ProblemDetail error;
}
