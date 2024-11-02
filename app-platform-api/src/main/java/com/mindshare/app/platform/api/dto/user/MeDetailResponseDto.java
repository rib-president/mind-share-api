package com.mindshare.app.platform.api.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@Builder
public class MeDetailResponseDto {
  @NotBlank
  private String emailAddress;

  @NotBlank
  private String phoneNumber;

  @NotBlank
  private String userType;

  private List<String> categories;

  @NotBlank
  private String name;

  private Date birthDate;

  private Timestamp alarmAgreedDatetime;
}
