package com.mindshare.app.platform.api.service.board.impl;

import com.mindshare.app.platform.api.dto.board.ArticleCreateRequestDto;
import com.mindshare.app.platform.api.dto.board.ArticleDetailResponseDto;
import com.mindshare.app.platform.api.dto.board.ArticleListResponseDto;
import com.mindshare.app.platform.api.dto.board.ArticleUpdateRequestDto;
import com.mindshare.app.platform.api.repository.ArticleRepository;
import com.mindshare.app.platform.api.repository.CategoryRepository;
import com.mindshare.app.platform.api.securityUser.SecurityUser;
import com.mindshare.app.platform.api.service.SecurityServiceImpl;
import com.mindshare.app.platform.api.service.board.ArticleService;
import com.mindshare.domain.board.entity.Article;
import com.mindshare.domain.system.entity.Category;
import com.mindshare.domain.user.entity.User;
import io.client.core.aop.annotation.DistributedLock;
import io.client.core.dto.ArticleListRequestDto;
import io.client.core.dto.CreateResponseDto;
import io.client.core.dto.ListItemResponseDto;
import io.client.core.dto.SuccessResponseDto;
import io.system.core.exception.ApiException;
import io.system.core.exception.enums.ApiExceptionEnum;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

  private final ArticleRepository articleRepository;
  private final CategoryRepository categoryRepository;

  @Value("${cookie.article.view_count}")
  private String COOKIE_NAME;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public CreateResponseDto<String> createOne(ArticleCreateRequestDto body) {

    // 현재 사용자 정보 가져오기
    User author = this.getUser();

    // 분야 가져오기
    Category category = categoryRepository.findByLabel(body.getCategory())
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.NOT_FOUND));

    // 원글 가져오기(답글 등록인 경우)
    Article parent = null;
    if (body.getParentId() != null) {
      parent = articleRepository.findById(body.getParentId())
          .orElseThrow(() -> new ApiException(ApiExceptionEnum.ARTICLE_NOT_FOUND));

      // 답글이 원글과 다른 카테고리라면 에러
      if (!parent.getCategory().equals(category)) {
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

  @Override
  @DistributedLock(key = "#articleId")
  public ArticleDetailResponseDto getOne(BigInteger articleId
//                                        , String viewedArticlesCookie,
//                                         HttpServletResponse response
  ) {

    // 사용자 pk 가져오기
    User user = this.getUser();

    // 게시글 가져오기
    Article article = articleRepository.findById(articleId)
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.ARTICLE_NOT_FOUND));

//    // 조회수 증가: 읽은 게시물 pk를 쿠키에 [1],[2] 포맷으로 저장하여 무제한 증가 제한
//    String articleCookieValue = "[" + articleId.toString() + "]";
//    if(!this.isViewedArticle(viewedArticlesCookie, articleCookieValue)) {
//      // 이미 읽은 게시물이 아니라면 조회수 증가
//      article.increaseViewCount();
//      articleRepository.save(article);
//
//      // 현재 게시물 pk 쿠키에 저장
//      response.addCookie(this.createCookie(viewedArticlesCookie, articleCookieValue));
//    }
    article.increaseViewCount();
    articleRepository.save(article);

    return this.getDetailResponse(article, user);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public SuccessResponseDto updateOne(BigInteger articleId, ArticleUpdateRequestDto body) {

    // 사용자 정보 가져오기
    User user = this.getUser();

    // 게시글 가져오기
    Article article = articleRepository.findById(articleId)
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.ARTICLE_NOT_FOUND));

    // 본인 글만 수정 가능
    if(!article.isMine(user)) {
      throw new ApiException(ApiExceptionEnum.ARTICLE_REQUEST_FORBIDDEN);
    }

    // 게시글 수정
    article.update(body.getTitle(), body.getContent());
    articleRepository.save(article);

    return SuccessResponseDto.builder()
        .success(true)
        .build();
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteOne(BigInteger articleId) {

    // 사용자 정보 가져오기
    User user = this.getUser();

    // 게시글 정보 가져오기
    Article article = articleRepository.findById(articleId)
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.ARTICLE_NOT_FOUND));

    // 본인 게시글만 삭제 가능
    if(!article.isMine(user)) {
      throw new ApiException(ApiExceptionEnum.ARTICLE_REQUEST_FORBIDDEN);
    }

    // 게시글 삭제
    articleRepository.delete(article);

  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ListItemResponseDto<ArticleListResponseDto> getMany(String category, Pageable pageable) {

    Page<Article> pageItems;
    Long total;
    if(category == null) {
      // 전체 리스트 조회(답글 제외)
      pageItems = articleRepository.findAllByParentIsNull(pageable);
      total = articleRepository.countAllByParentIsNull();
    } else {
      // 분야별 리스트 조회(답글 제외)
      pageItems = articleRepository.findAllByParentIsNullAndCategory_Label(category, pageable);
      total = articleRepository.countAllByParentIsNullAndCategory_Label(category);
    }
    List<Article> articles = pageItems.toList();

    // response dto
    List<ArticleListResponseDto> items = this.getListResponse(articles);

    return ListItemResponseDto.<ArticleListResponseDto>builder()
        .items(items)
        .total(total)
        .count(items.size())
        .size(pageable.getPageSize())
        .page(pageable.getPageNumber())
        .build();
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ListItemResponseDto<ArticleListResponseDto> getManySearch(ArticleListRequestDto query, Pageable pageable) {

    Page<Article> pageItems = articleRepository.findManyWithCriteria(query, pageable);
    List<Article> articles = pageItems.toList();

    // response dto
    List<ArticleListResponseDto> items = this.getListResponse(articles);

    return ListItemResponseDto.<ArticleListResponseDto>builder()
        .items(items)
        .total(pageItems.getTotalElements())
        .count(pageItems.getNumberOfElements())
        .size(pageable.getPageSize())
        .page(pageable.getPageNumber())
        .build();
  }

  private User getUser() {
    SecurityUser securityUser = SecurityServiceImpl.getSecurityUserByToken();
    return securityUser.getUser();
  }

  private ArticleDetailResponseDto getDetailResponse(Article article, User user) {

    // 원글 response dto 생성
    ArticleDetailResponseDto.ArticleDetailResponseDtoBuilder builder = ArticleDetailResponseDto.builder();
    builder
        .id(article.getArticleId().toString())
        .category(article.getCategory().getLabel())
        .authorName(article.getAuthorName())
        .isMine(article.isMine(user))
        .title(article.getTitle())
        .content(article.getContent())
        .readCount(article.getViewCount())
        .createdDatetime(article.getCreatedDatetime())
        .updatedDatetime(article.getUpdatedDatetime());

    List<ArticleDetailResponseDto> children = new ArrayList<>();
    for(Article child : article.getChildren()) {
      // 답글 response dto 생성
      children.add(this.getDetailResponse(child, user));
    }
    builder.children(children);

    return builder.build();
  }

  private Boolean isViewedArticle(String cookieValue, String articleCookieValue) {
    if(cookieValue == null) {
      return false;
    }

    String[] cookieValues = cookieValue.split("-");
    return Arrays.stream(cookieValues).toList()
        .contains(articleCookieValue);
  }

  private Cookie createCookie(String currentCookieValue, String addedCookieValue) {
    String cookieValue;
    if(currentCookieValue == null) {
      cookieValue = addedCookieValue;
    } else {
      if(currentCookieValue.length() > 4096){
        // 쿠키의 최대 크기보다 크면 오래된 것부터 삭제
        currentCookieValue = currentCookieValue.substring(currentCookieValue.indexOf("-") + 1);
      }

      cookieValue = currentCookieValue + "-" + addedCookieValue;
    }


    Cookie cookie = new Cookie(COOKIE_NAME, cookieValue);
    cookie.setMaxAge(60 * 60 * 4); // 4시간
    cookie.setPath("/");  // path 설정
    return cookie;
  }

  private List<ArticleListResponseDto> getListResponse(List<Article> articles) {
    // response dto
    return articles.stream()
        .map(article -> ArticleListResponseDto.builder()
            .id(article.getArticleId().toString())
            .category(article.getCategory().getLabel())
            .authorName(article.getAuthorName())
            .title(article.getTitle())
            .viewCount(article.getViewCount())
            .childrenCount(article.getChildren().size())
            .createdDatetime(article.getCreatedDatetime())
            .updatedDatetime(article.getUpdatedDatetime())
            .build())
        .toList();
  }
}
