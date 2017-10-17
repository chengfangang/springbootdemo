package com.etongdai.springbootdemo.document;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Mongo
 *
 * @author hotleave
 */
@Document
@Data
@NoArgsConstructor
public class Dict {
  @Id
  private String id;

  /**
   * 编码
   */
  private String code;

  /**
   * 名称
   */
  private String name;

  /**
   * 排序号
   */
  private Integer rank;

  /**
   * 上级ID
   */
  private Long parentId;
}
