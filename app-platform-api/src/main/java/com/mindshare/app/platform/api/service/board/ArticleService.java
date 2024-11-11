package com.mindshare.app.platform.api.service.board;

import com.mindshare.app.platform.api.dto.board.ArticleCreateRequestDto;
import com.mindshare.app.platform.api.dto.board.ArticleDetailResponseDto;
import com.mindshare.app.platform.api.dto.board.ArticleListResponseDto;
import com.mindshare.app.platform.api.dto.board.ArticleUpdateRequestDto;
import com.mindshare.app.platform.api.securityUser.SecurityUser;
import io.client.core.dto.ArticleListRequestDto;
import io.client.core.dto.CreateResponseDto;
import io.client.core.dto.ListItemResponseDto;
import io.client.core.dto.SuccessResponseDto;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;

public interface ArticleService {
  CreateResponseDto<String> createOne(ArticleCreateRequestDto body);

  ArticleDetailResponseDto getOne(BigInteger articleId, SecurityUser securityUser);

  SuccessResponseDto updateOne(BigInteger articleId, ArticleUpdateRequestDto body);

  void deleteOne(BigInteger articleId);

  ListItemResponseDto<ArticleListResponseDto> getMany(String category, Pageable pageable);

  ListItemResponseDto<ArticleListResponseDto> getManySearch(ArticleListRequestDto query, Pageable pageable);
}
