package com.mindshare.app.platform.api.dto.auth;

import com.mindshare.app.platform.api.enums.LoginType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDto {
  @NotNull
  private LoginType loginType;

  @NotBlank
  private String username;

  @NotBlank
  private String password;

}
