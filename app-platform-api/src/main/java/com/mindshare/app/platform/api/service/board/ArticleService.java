package com.mindshare.app.platform.api.service.board;

import com.mindshare.app.platform.api.dto.board.ArticleCreateRequestDto;
import io.client.core.dto.CreateResponseDto;

public interface ArticleService {
  CreateResponseDto<String> createOne(ArticleCreateRequestDto body);
}
