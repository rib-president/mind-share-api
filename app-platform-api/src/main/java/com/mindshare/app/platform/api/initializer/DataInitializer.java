package com.mindshare.app.platform.api.initializer;

import com.mindshare.app.platform.api.repository.CategoryRepository;
import com.mindshare.app.platform.api.repository.UserTypeRepository;
import com.mindshare.domain.system.entity.Category;
import com.mindshare.domain.user.entity.UserType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {
  private final CategoryRepository categoryRepository;
  private final UserTypeRepository userTypeRepository;

  @Value("${spring.jpa.hibernate.ddl-auto}")
  private String ddlAuto;

  @PostConstruct
  public void init() {
    if (ddlAuto.startsWith("create")) {
      UserType expert = UserType.builder()
          .userTypeId(1)
          .name("EXPERT")
          .label("전문가")
          .build();

      UserType student = UserType.builder()
          .userTypeId(2)
          .name("STUDENT")
          .label("학습자")
          .build();

      userTypeRepository.saveAllAndFlush(List.of(expert, student));

      Category science = Category.builder()
          .name("SCIENCE")
          .label("과학")
          .build();

      Category math = Category.builder()
          .name("MATH")
          .label("수학")
          .build();

      Category art = Category.builder()
          .name("ART")
          .label("예술")
          .build();

      categoryRepository.saveAllAndFlush(List.of(science, math, art));
    }
  }
}
