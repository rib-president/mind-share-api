package com.mindshare.app.platform.api.repository;

import com.mindshare.domain.board.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface ArticleRepository extends JpaRepository<Article, BigInteger> {
  Page<Article> findAllByParentIsNull(Pageable pageable);

  Long countAllByParentIsNull();

  Page<Article> findAllByParentIsNullAndCategory_Label(String label, Pageable pageable);

  Long countAllByParentIsNullAndCategory_Label(String label);
}
