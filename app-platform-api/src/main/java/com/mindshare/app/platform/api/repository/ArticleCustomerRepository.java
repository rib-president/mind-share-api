package com.mindshare.app.platform.api.repository;

import com.mindshare.domain.board.entity.Article;
import io.client.core.dto.ArticleListRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleCustomerRepository {
  Page<Article> findManyWithCriteria(ArticleListRequestDto criteria, Pageable pageable);
}
