package com.mindshare.app.platform.api.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class DuplicateCheckResponseDto {
  private Boolean isAvailable;
}
