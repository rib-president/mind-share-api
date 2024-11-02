package io.client.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.system.core.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class ArticleListRequestDto {
  private String category;

  @JsonProperty("authorName:like")
  private String authorName;

  @JsonProperty("keyword:like")
  private String keyword;

  @JsonProperty("title:like")
  private String title;

  @JsonProperty("content:like")
  private String content;

  private Boolean hasChildren;

  private Timestamp createdDatetimeGte;
  private Timestamp createdDatetimeLte;

  @JsonProperty("createdDate:gte")
  public void setCreatedDatetimeGte(String createdDate) {
    if(createdDate != null) {
      this.createdDatetimeGte = DateUtils.getFirstDateTime(createdDate);
    }
  }

  @JsonProperty("createdDate:lte")
  public void setCreatedDatetimeLte(String createdDate) {
    this.createdDatetimeLte = DateUtils.getLastDateTime(createdDate);
  }
}

