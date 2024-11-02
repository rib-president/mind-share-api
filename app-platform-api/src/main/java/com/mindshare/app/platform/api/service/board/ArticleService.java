package com.mindshare.app.platform.api.service.board;

import com.mindshare.app.platform.api.dto.board.ArticleCreateRequestDto;
import com.mindshare.app.platform.api.dto.board.ArticleDetailResponseDto;
import io.client.core.dto.CreateResponseDto;
import jakarta.servlet.http.HttpServletResponse;

import java.math.BigInteger;

public interface ArticleService {
  CreateResponseDto<String> createOne(ArticleCreateRequestDto body);

  ArticleDetailResponseDto getOne(BigInteger articleId, String viewedArticlesCookie,
                                  HttpServletResponse response);
}
