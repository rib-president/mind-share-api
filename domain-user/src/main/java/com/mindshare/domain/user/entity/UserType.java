package com.mindshare.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user__user_type")
@Entity
public class UserType {
  @Id
  @Column(name = "user_type_id", columnDefinition = "TINYINT UNSIGNED COMMENT '사용자 타입 PK'")
  private Integer userTypeId;

  @Column(name = "name", nullable = false, unique = true, columnDefinition = "VARCHAR(16) COMMENT '타입명'")
  private String name;

  @Column(name = "label", nullable = false, unique = true, columnDefinition = "VARCHAR(8) COMMENT '라벨'")
  private String label;

}
