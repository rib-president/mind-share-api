package com.mindshare.app.platform.api.dto.board;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ArticleUpdateRequestDto {

  private String title;
  private String content;
}
