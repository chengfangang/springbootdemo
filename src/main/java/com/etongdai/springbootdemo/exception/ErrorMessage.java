package com.etongdai.springbootdemo.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 错误信息
 *
 * <p>反馈给客户端</p>
 */
@Data
@NoArgsConstructor
public class ErrorMessage {

  private long timestamp;
  private String code;
  private String message;

  public ErrorMessage(final String code, final String message) {
    this.code = code;
    this.message = message;
    timestamp = System.currentTimeMillis();
  }
}
