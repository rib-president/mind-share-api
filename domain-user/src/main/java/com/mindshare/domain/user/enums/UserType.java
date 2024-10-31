package com.mindshare.domain.user.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum UserType {
  EXPERT("EXPERT", "전문가"), STUDENT("STUDENT", "학습자");

  private String name;
  private String label;

  UserType(String name, String label) {
    this.name = name;
    this.label = label;
  }

  @JsonCreator
  public static UserType from(String label) {
    for(UserType type : UserType.values()) {
      if(type.getLabel().equals(label)) {
        return type;
      }
    }
    throw new IllegalArgumentException();
  }
}
