package com.mindshare.domain.user.entity;

import com.mindshare.domain.system.entity.Category;
import com.mindshare.domain.user.embeddedkey.UserCategoryId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "user__user_category")
@Entity
public class UserCategory {

  @EmbeddedId
  private UserCategoryId id;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
  @MapsId("userId")
  @JoinColumn(name = "user_id", columnDefinition = "BIGINT UNSIGNED COMMENT '사용자 PK'")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
  @MapsId("categoryId")
  @JoinColumn(name = "category_id", columnDefinition = "TINYINT UNSIGNED COMMENT '분야 PK'")
  private Category category;

  public UserCategory(User user, Category category) {
    UserCategoryId id = UserCategoryId.builder()
        .userId(user.getUserId())
        .categoryId(category.getCategoryId())
        .build();

    this.id = id;
    this.user = user;
    this.category = category;
  }
}
