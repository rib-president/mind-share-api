package com.mindshare.app.platform.api.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DuplicateCheckRequestDto {
  @NotBlank
  private String username;
}
