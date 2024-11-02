package com.mindshare.app.platform.api.repository.impl;

import com.mindshare.app.platform.api.repository.ArticleCustomerRepository;
import com.mindshare.domain.board.entity.Article;
import com.mindshare.domain.board.entity.QArticle;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.client.core.dto.ArticleListRequestDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ArticleCustomerRepositoryImpl implements ArticleCustomerRepository {
  @PersistenceContext
  private EntityManager em;
  private final JPAQueryFactory factory;

  QArticle article = QArticle.article;

  @Override
  public Page<Article> findManyWithCriteria(ArticleListRequestDto criteria, Pageable pageable) {

    JPAQuery<Article> query = factory.selectFrom(article);
    JPAQuery<Long> countQuery = factory.select(article.count()).from(article);

    // 원글만 가져오기
    query.where(article.parent.isNull());
    countQuery.where(article.parent.isNull());

    // dynamic where
    query.where(categoryEq(criteria.getCategory()),
        authorNameContains(criteria.getAuthorName()),
        keywordContains(criteria.getKeyword()),
        titleContains(criteria.getTitle()),
        contentContains(criteria.getContent()),
        hasChildren(criteria.getHasChildren()),
        between(criteria.getCreatedDatetimeGte(), criteria.getCreatedDatetimeLte()));

     countQuery.where(categoryEq(criteria.getCategory()),
        authorNameContains(criteria.getAuthorName()),
        keywordContains(criteria.getKeyword()),
        titleContains(criteria.getTitle()),
        contentContains(criteria.getContent()),
        hasChildren(criteria.getHasChildren()),
        between(criteria.getCreatedDatetimeGte(), criteria.getCreatedDatetimeLte()));

    // pagination
    List<Article> articles = query.offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .orderBy(getOrderBy(pageable).toArray(new OrderSpecifier[]{}))
        .fetch();
    Long total = countQuery.fetchOne();

    return new PageImpl<>(articles, pageable, total);
  }

  private BooleanExpression categoryEq(String category) {
    if (category == null) {
      return null;
    }
    return article.category.label.eq(category);
  }

  private BooleanExpression authorNameContains(String authorName) {
    if (authorName == null) {
      return null;
    }
    return article.authorName.contains(authorName);
  }

  private BooleanExpression keywordContains(String keyword) {
    if (keyword == null) {
      return null;
    }
    return article.title.contains(keyword).or(article.content.contains(keyword));
  }

  private BooleanExpression titleContains(String title) {
    if (title == null) {
      return null;
    }
    return article.title.contains(title);
  }

  private BooleanExpression contentContains(String content) {
    if (content == null) {
      return null;
    }
    return article.content.contains(content);
  }

  private BooleanExpression hasChildren(Boolean hasChildren) {
    if(hasChildren == null) {
      return null;
    }

    BooleanExpression be;
    if(hasChildren) {
      be = article.children.size().gt(0);
    } else {
      be = article.children.size().eq(0);
    }
    return be;
  }

  private BooleanExpression between(Timestamp datetimeGte, Timestamp datetimeLte) {
    BooleanExpression be = null;
    if(datetimeGte != null) {
      be = article.createdDatetime.gt(datetimeGte);
    }

    if(datetimeLte != null) {
      be = article.createdDatetime.lt(datetimeLte);
    }

    return be;
  }

  private List<OrderSpecifier<?>> getOrderBy(Pageable pageable) {
    List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
    for (Sort.Order order : pageable.getSort()) {
      Order direction = order.isAscending() ? Order.ASC : Order.DESC;
      switch (order.getProperty()) {
        case "id"->
          orderSpecifiers.add(new OrderSpecifier<>(direction, article.articleId));
        case "category"->
          orderSpecifiers.add(new OrderSpecifier<>(direction, article.category.label));
        case "authorName"->
          orderSpecifiers.add(new OrderSpecifier<>(direction, article.authorName));
        case "title"->
            orderSpecifiers.add(new OrderSpecifier<>(direction, article.title));
        case "viewCount"->
            orderSpecifiers.add(new OrderSpecifier<>(direction, article.viewCount));
        case "childrenCount"->
            orderSpecifiers.add(new OrderSpecifier<>(direction, article.children.size()));
        case "createdDatetime"->
            orderSpecifiers.add(new OrderSpecifier<>(direction, article.createdDatetime));
        case "updatedDatetime"->
            orderSpecifiers.add(new OrderSpecifier<>(direction, article.updatedDatetime));
      }
    }
    return orderSpecifiers;
  }

}
