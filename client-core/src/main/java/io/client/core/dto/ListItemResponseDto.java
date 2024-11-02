package io.client.core.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ListItemResponseDto <T> {
  private Long total;
  private Integer count;
  private Integer size;
  private Integer page;

  private List<T> items;

  @Builder
  public ListItemResponseDto(Long total, Integer count, Integer size, Integer page, List<T> items) {
    this.total = total;
    this.count = count;
    this.size = size;
    this.page = page + 1;
    this.items = items;
  }
}
