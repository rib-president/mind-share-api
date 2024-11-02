package com.mindshare.app.platform.api.service.board.impl;

import com.mindshare.app.platform.api.dto.board.ArticleCreateRequestDto;
import com.mindshare.app.platform.api.repository.ArticleRepository;
import com.mindshare.app.platform.api.repository.CategoryRepository;
import com.mindshare.app.platform.api.repository.UserRepository;
import com.mindshare.app.platform.api.securityUser.SecurityUser;
import com.mindshare.app.platform.api.service.SecurityServiceImpl;
import com.mindshare.app.platform.api.service.board.ArticleService;
import com.mindshare.domain.board.entity.Article;
import com.mindshare.domain.system.entity.Category;
import com.mindshare.domain.user.entity.User;
import io.client.core.dto.CreateResponseDto;
import io.system.core.exception.ApiException;
import io.system.core.exception.enums.ApiExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

  private final ArticleRepository articleRepository;
  private final UserRepository userRepository;
  private final CategoryRepository categoryRepository;

  @Override
  public CreateResponseDto<String> createOne(ArticleCreateRequestDto body) {

    // 현재 사용자 pk 가져오기
    BigInteger authorId = this.getUserId();

    // 현재 사용자 정보 가져오기
    User author = userRepository.findById(authorId)
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.USER_NOT_FOUND));

    // 분야 가져오기
    Category category = categoryRepository.findByLabel(body.getCategory())
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.NOT_FOUND));

    // 원글 가져오기(답글 등록인 경우)
    Article parent = null;
    if(body.getParentId() != null) {
      parent = articleRepository.findById(body.getParentId())
          .orElseThrow(() -> new ApiException(ApiExceptionEnum.ARTICLE_NOT_FOUND));

      // 답글이 원글과 다른 카테고리라면 에러
      if(!parent.getCategory().equals(category)) {
        throw new ApiException(ApiExceptionEnum.CHILD_ARTICLE_CATEGORY_NOT_MATCH);
      }
    }

    // 게시글 생성
    Article article = Article.builder()
        .category(category)
        .author(author)
        .authorName(author.getName())
        .title(body.getTitle())
        .content(body.getContent())
        .parent(parent)
        .build();
    articleRepository.save(article);

    return CreateResponseDto.<String>builder()
        .id(article.getArticleId().toString())
        .build();
  }

  private BigInteger getUserId() {
    SecurityUser securityUser = SecurityServiceImpl.getSecurityUserByToken();
    return securityUser.getId();
  }
}
