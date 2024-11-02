package io.client.core.repository;

import io.client.core.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
  Optional<RefreshToken> findByUserId(String userId);
}
