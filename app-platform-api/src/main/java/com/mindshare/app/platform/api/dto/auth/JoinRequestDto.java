package com.mindshare.app.platform.api.dto.auth;

import com.mindshare.domain.user.enums.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
public class JoinRequestDto {
  @NotBlank
  private String emailAddress;

  @NotBlank
  private String phoneNumber;

  @NotBlank
  private String password;

  @NotNull
  private UserType userType;

  private List<String> categories;

  @NotBlank
  private String name;

  private Date birthDate;

  private Timestamp alarmAgreedDatetime;
}
