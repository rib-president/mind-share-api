package com.mindshare.domain.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArticle is a Querydsl query type for Article
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QArticle extends EntityPathBase<Article> {

    private static final long serialVersionUID = -989066641L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QArticle article = new QArticle("article");

    public final NumberPath<java.math.BigInteger> articleId = createNumber("articleId", java.math.BigInteger.class);

    public final com.mindshare.domain.user.entity.QUser author;

    public final StringPath authorName = createString("authorName");

    public final com.mindshare.domain.system.entity.QCategory category;

    public final ListPath<Article, QArticle> children = this.<Article, QArticle>createList("children", Article.class, QArticle.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final DateTimePath<java.sql.Timestamp> createdDatetime = createDateTime("createdDatetime", java.sql.Timestamp.class);

    public final QArticle parent;

    public final StringPath title = createString("title");

    public final DateTimePath<java.sql.Timestamp> updatedDatetime = createDateTime("updatedDatetime", java.sql.Timestamp.class);

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public QArticle(String variable) {
        this(Article.class, forVariable(variable), INITS);
    }

    public QArticle(Path<? extends Article> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QArticle(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QArticle(PathMetadata metadata, PathInits inits) {
        this(Article.class, metadata, inits);
    }

    public QArticle(Class<? extends Article> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new com.mindshare.domain.user.entity.QUser(forProperty("author"), inits.get("author")) : null;
        this.category = inits.isInitialized("category") ? new com.mindshare.domain.system.entity.QCategory(forProperty("category")) : null;
        this.parent = inits.isInitialized("parent") ? new QArticle(forProperty("parent"), inits.get("parent")) : null;
    }

}

