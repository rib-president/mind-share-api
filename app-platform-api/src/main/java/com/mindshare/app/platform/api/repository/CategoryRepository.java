package com.mindshare.app.platform.api.repository;

import com.mindshare.domain.system.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
  Optional<Category> findByLabel(String label);
}
