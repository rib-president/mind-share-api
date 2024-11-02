package com.mindshare.app.platform.api.repository;

import com.mindshare.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, BigInteger> {
  Optional<User> findByEmailAddress(String emailAddress);
  Optional<User> findByPhoneNumber(String phoneNumber);

  Boolean existsByEmailAddress(String emailAddress);

  Boolean existsByPhoneNumber(String phoneNumber);
}
