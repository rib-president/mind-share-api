package com.mindshare.app.platform.api.dto.board;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Setter
@Getter
public class ArticleCreateRequestDto {
  @NotBlank
  private String category;

  @NotBlank
  private String title;

  @NotBlank
  private String content;

  private BigInteger parentId;

}
