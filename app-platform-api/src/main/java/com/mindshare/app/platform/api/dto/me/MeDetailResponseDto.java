package com.mindshare.app.platform.api.dto.me;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

//  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
  private Timestamp alarmAgreedDatetime;
}
