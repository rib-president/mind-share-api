package com.mindshare.app.platform.api.repository;

import com.mindshare.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

public interface SecurityRepository extends JpaRepository<User, BigInteger> {
  Optional<User> findByUserId(BigInteger userId);
}
