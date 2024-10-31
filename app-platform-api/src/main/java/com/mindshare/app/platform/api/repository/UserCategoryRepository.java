package com.mindshare.app.platform.api.repository;

import com.mindshare.domain.user.embeddedkey.UserCategoryId;
import com.mindshare.domain.user.entity.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCategoryRepository extends JpaRepository<UserCategory, UserCategoryId> {
}
