package com.mindshare.domain.board.entity;

import com.mindshare.domain.system.entity.Category;
import com.mindshare.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigInteger;
import java.sql.Timestamp;

@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "board__article")
@Entity
public class Article {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "article_id", columnDefinition = "BIGINT UNSIGNED AUTO_INCREMENT COMMENT '게시글 PK'")
  private BigInteger articleId;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
  @JoinColumn(name = "category_id", nullable = false, columnDefinition = "TINYINT UNSIGNED COMMENT '분야 FK'")
  private Category category;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "author_id", columnDefinition = "BIGINT UNSIGNED COMMENT '작성자 FK'")
  private User author;

  @Column(name = "author_name", nullable = false, columnDefinition = "VARCHAR(32) COMMENT '작성자 이름'")
  private String authorName;

  @Column(name = "title", nullable = false, columnDefinition = "VARCHAR(64) COMMENT '제목'")
  private String title;

  @Column(name = "content", nullable = false, columnDefinition = "TEXT COMMENT '내용'")
  private String content;

  @ColumnDefault("0")
  @Column(name = "read_count", columnDefinition = "INT NOT NULL COMMENT '조회수'")
  private Long readCount;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "parent_id", columnDefinition = "BIGINT UNSIGNED COMMENT '원글 FK'")
  private Article parent;

  @Column(name = "created_datetime", columnDefinition = "TIMESTAMP(6) DEFAULT NOW(6) NOT NULL COMMENT '생성일'")
  private Timestamp createdDatetime;

  @Column(name = "updated_datetime", columnDefinition = "TIMESTAMP(6) DEFAULT NOW(6) ON UPDATE NOW(6) NOT NULL COMMENT '변경일'")
  private Timestamp updatedDatetime;
}
