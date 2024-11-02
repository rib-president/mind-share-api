package com.mindshare.app.platform.api.dto.me;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindshare.domain.user.enums.UserType;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
public class MeUpdateRequestDto {
  private String emailAddress;

  private String phoneNumber;

  private String password;

  private UserType userType;

  private List<String> categories;
  @JsonIgnore
  private Boolean isCategoriesDirty = false;

  private String name;

  private Date birthDate;
  @JsonIgnore
  private Boolean isBirthDateDirty = false;

  private Timestamp alarmAgreedDatetime;
  @JsonIgnore
  private Boolean isAlarmAgreedDatetimeDirty = false;

  public void setCategories(List<String> categories) {
    this.categories = categories;
    this.isCategoriesDirty = true;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
    this.isBirthDateDirty = true;
  }

  public void setAlarmAgreedDatetime(Timestamp alarmAgreedDatetime) {
    this.alarmAgreedDatetime = alarmAgreedDatetime;
    this.isAlarmAgreedDatetimeDirty = true;
  }
}
