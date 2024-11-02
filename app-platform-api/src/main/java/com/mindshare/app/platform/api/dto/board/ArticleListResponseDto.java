package com.mindshare.app.platform.api.dto.board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@Builder
public class ArticleListResponseDto {
  private String id;

  private String category;

  private String authorName;

  private String title;

  private Long viewCount;
  private Integer childrenCount;

  private Timestamp createdDatetime;
  private Timestamp updatedDatetime;
}
