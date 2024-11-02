package com.mindshare.app.platform.api.dto.board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@Builder
public class ArticleDetailResponseDto {
  private String id;

  private String category;

  private String authorName;
  private Boolean isMine;

  private String title;
  private String content;

  private Long readCount;

  private Timestamp createdDatetime;
  private Timestamp updatedDatetime;

  private List<ArticleDetailResponseDto> children;
}
