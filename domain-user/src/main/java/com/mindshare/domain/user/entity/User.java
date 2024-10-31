package com.mindshare.domain.user.entity;

import com.mindshare.domain.system.entity.Category;
import com.mindshare.domain.user.embeddedkey.UserCategoryId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user__user")
@Entity
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id", columnDefinition = "BIGINT UNSIGNED AUTO_INCREMENT COMMENT '사용자 PK'")
  private BigInteger userId;

  @Column(name = "email_address", nullable = false, unique = true, columnDefinition = "VARCHAR(32) COMMENT '이메일 주소'")
  private String emailAddress;

  @Column(name = "phone_number", unique = true, nullable = false, columnDefinition = "VARCHAR(11) COMMENT '핸드폰 번호'")
  private String phoneNumber;

  @Column(name = "password", nullable = false, columnDefinition = "VARCHAR(60) COMMENT '암호화된 비밀번호'")
  private String password;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_type_id", nullable = false, columnDefinition = "TINYINT UNSIGNED COMMENT '사용자 타입 FK'")
  private UserType userType;

  @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(32) COMMENT '사용자 이름'")
  private String name;

  @Column(name = "birth_date", columnDefinition = "DATE COMMENT '생년월일'")
  private Date birthDate;

  @Column(name = "alarm_agreed_datetime", columnDefinition = "TIMESTAMP(6) COMMENT '알림 수신 동의 일시'")
  private Timestamp alarmAgreedDatetime;

  @Column(name = "created_datetime", columnDefinition = "TIMESTAMP(6) DEFAULT NOW(6) NOT NULL COMMENT '생성일'")
  private Timestamp createdDatetime;

  @Column(name = "updated_datetime", columnDefinition = "TIMESTAMP(6) DEFAULT NOW(6) ON UPDATE NOW(6) NOT NULL COMMENT '변경일'")
  private Timestamp updatedDatetime;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserCategory> userCategories;

  public void addUserCategory(Category category) {
    UserCategory userCategory = new UserCategory(this, category);

    this.userCategories.add(userCategory);
  }
}
