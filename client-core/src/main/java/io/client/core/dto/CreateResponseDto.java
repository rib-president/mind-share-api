package io.client.core.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CreateResponseDto <T> {
  private T id;
}
