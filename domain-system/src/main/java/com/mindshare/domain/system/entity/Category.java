package com.mindshare.domain.system.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "system__category")
@Entity
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "category_id", columnDefinition = "TINYINT UNSIGNED AUTO_INCREMENT COMMENT '분야 PK'")
  private Integer categoryId;

  @Column(name = "name", nullable = false, unique = true, columnDefinition = "VARCHAR(16) COMMENT '타입명'")
  private String name;

  @Column(name = "label", nullable = false, unique = true, columnDefinition = "VARCHAR(8) COMMENT '라벨'")
  private String label;
}
