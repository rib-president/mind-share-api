package com.mindshare.app.platform.api.repository;

import com.mindshare.domain.user.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTypeRepository extends JpaRepository<UserType, Integer> {
  Optional<UserType> findByName(String name);
}
