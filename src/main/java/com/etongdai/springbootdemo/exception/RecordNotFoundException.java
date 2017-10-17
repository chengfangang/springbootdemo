package com.etongdai.springbootdemo.exception;

/**
 * @author hotleave
 */
public class RecordNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 4199926891398934017L;
  private static final String TEMPALTE = "您要查找的记录不存在: %s";

  public RecordNotFoundException(String hint) {
    super(String.format(TEMPALTE, hint));
  }
}
