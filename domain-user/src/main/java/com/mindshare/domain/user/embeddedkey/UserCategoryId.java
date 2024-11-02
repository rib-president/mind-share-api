package com.mindshare.domain.user.embeddedkey;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

@Embeddable
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCategoryId implements Serializable {
  private BigInteger userId;
  private Integer categoryId;

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(!(o instanceof UserCategoryId)) return false;
    UserCategoryId userCategoryId = (UserCategoryId) o;
    return Objects.equals(userId, userCategoryId.userId)
        && Objects.equals(categoryId, userCategoryId.categoryId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, categoryId);
  }
}
