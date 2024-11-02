package com.mindshare.app.platform.api.repository;

import com.mindshare.domain.board.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface ArticleRepository extends JpaRepository<Article, BigInteger> {

}
