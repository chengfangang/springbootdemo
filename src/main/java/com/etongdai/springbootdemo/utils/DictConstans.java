package com.etongdai.springbootdemo.utils;

/**
 * @author hotleave
 */
public final class DictConstans {
  public static final String API_VERSION = "/api/v1";

  /**
   * 被黑名单拦截
   */
  public static final String ERROR_BLOCK_BY_BLACK_LIST = "0001";

  /**
   * 未找到相应的记录
   */
  public static final String ERROR_RECORD_NOT_FOUND = "0404";

  /**
   * SpringBoot内部错误
   */
  public static final String ERROR_INTERNAL = "9999";

  /**
   * 数据有效性校验错误
   */
  public static final String ERROR_VALIDATION_FAIL = "9998";

  private DictConstans() {
  }
}
