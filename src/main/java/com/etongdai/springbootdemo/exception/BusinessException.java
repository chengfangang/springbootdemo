package com.etongdai.springbootdemo.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;

/**
 * 业务异常
 *
 * @author hotleave
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {
  private static final long serialVersionUID = -2713520074439690568L;

  private final String code;
  private final Object[] params;

  public BusinessException(String code, Object... params) {
    super(code);

    this.code = code;
    if (params != null && params.length > 0) {
      this.params = Arrays.copyOf(params, params.length);
    } else {
      this.params = null;
    }
  }
}
