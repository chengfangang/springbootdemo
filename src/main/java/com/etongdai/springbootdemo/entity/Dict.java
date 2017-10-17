package com.etongdai.springbootdemo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 数字字典
 *
 * @author hotleave
 */
@Entity
@Table(name = "T_DICT")
@Data
@NoArgsConstructor
@ApiModel("数据字典")
public class Dict implements Serializable {
  private static final long serialVersionUID = 8128617586265843034L;

  public static final Long ROOT = 1L;

  @Id
  @GeneratedValue
  private Long id;

  /**
   * 编码
   */
  @ApiModelProperty(value = "编码", required = true, example = "code")
  @Column(nullable = false, length = 20, unique = true)
  @Pattern(regexp = "\\w{4,20}", message = "编码由4到20位的数字、字母或下划线组成")
  private String code;

  /**
   * 名称
   */
  @ApiModelProperty(value = "名称", required = true, example = "字典名称")
  @Column(nullable = false, length = 100)
  @Length(min = 3, max = 100, message = "名称必须是3到100个字符")
  private String name;

  /**
   * 排序号
   */
  @ApiModelProperty(value = "排序号", required = true, example = "1")
  @Column(nullable = false)
  @NotNull(message = "排序号不能为空")
  private Integer rank;

  /**
   * 上级ID
   */
  @ApiModelProperty(value = "上级ID", required = true, example = "0")
  @NotNull(message = "必须指定上级ID，如果为根节目，请使用0")
  private Long parentId;
}
