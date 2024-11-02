package com.mindshare.app.platform.api.controller.board;

import com.mindshare.app.platform.api.dto.board.ArticleCreateRequestDto;
import com.mindshare.app.platform.api.service.board.ArticleService;
import io.client.core.dto.CreateResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/board/articles")
@RequiredArgsConstructor
public class ArticleController {
  private final ArticleService service;

  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  public CreateResponseDto<String> createOne(@Valid @RequestBody ArticleCreateRequestDto body) {
    return service.createOne(body);
  }
}
