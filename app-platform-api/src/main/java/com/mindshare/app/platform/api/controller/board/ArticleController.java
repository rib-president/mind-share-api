package com.mindshare.app.platform.api.controller.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindshare.app.platform.api.dto.board.ArticleCreateRequestDto;
import com.mindshare.app.platform.api.dto.board.ArticleDetailResponseDto;
import com.mindshare.app.platform.api.dto.board.ArticleListResponseDto;
import com.mindshare.app.platform.api.dto.board.ArticleUpdateRequestDto;
import com.mindshare.app.platform.api.service.board.ArticleService;
import io.client.core.dto.ArticleListRequestDto;
import io.client.core.dto.CreateResponseDto;
import io.client.core.dto.ListItemResponseDto;
import io.client.core.dto.SuccessResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;

@RestController
@RequestMapping("/v1/board/articles")
@RequiredArgsConstructor
public class ArticleController {
  private final ArticleService service;
  private final ObjectMapper mapper;

  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  public CreateResponseDto<String> createOne(@Valid @RequestBody ArticleCreateRequestDto body) {
    return service.createOne(body);
  }

  @GetMapping("/{articleId}")
  @ResponseStatus(HttpStatus.OK)
  public ArticleDetailResponseDto getOne(@PathVariable("articleId") BigInteger articleId
//      , @CookieValue(name = "viewedArticles", required = false) String viewedArticlesCookie, HttpServletResponse response
  ) {
    return service.getOne(articleId
//        , viewedArticlesCookie, response
    );
  }

  @PatchMapping("/{articleId}")
  @ResponseStatus(HttpStatus.OK)
  public SuccessResponseDto updateOne(@PathVariable("articleId") BigInteger articleId,
                                      @Valid @RequestBody ArticleUpdateRequestDto body) {
    return service.updateOne(articleId, body);
  }

  @DeleteMapping("/{articleId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> deleteOne(@PathVariable("articleId") BigInteger articleId) {
    service.deleteOne(articleId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("")
  @ResponseStatus(HttpStatus.OK)
  public ListItemResponseDto<ArticleListResponseDto> getMany(@RequestParam(value = "category", required = false) String category,
                                                             @PageableDefault(size = 10, sort = "createdDatetime", direction = Sort.Direction.DESC)Pageable pageable) {
    return service.getMany(category, pageable);
  }

  @GetMapping("/search")
  @ResponseStatus(HttpStatus.OK)
  public ListItemResponseDto<ArticleListResponseDto> getManySearch(@RequestParam Map<String, String> queryParam,
                                                                   @PageableDefault(size = 10, sort = "createdDatetime", direction = Sort.Direction.DESC)Pageable pageable) {

    ArticleListRequestDto query = mapper.convertValue(queryParam, ArticleListRequestDto.class);
    return service.getManySearch(query, pageable);
  }

}
