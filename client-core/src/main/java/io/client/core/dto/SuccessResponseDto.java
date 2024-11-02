package io.client.core.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class SuccessResponseDto {
  private Boolean success;
}
