package com.etongdai.springbootdemo.exception;

import com.etongdai.springbootdemo.utils.DictConstans;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * Controller 错误处理
 *
 * @author hotleave
 */
@ControllerAdvice
@Slf4j
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
  private final MessageSource messageSource;

  @Autowired
  public CustomResponseEntityExceptionHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  /**
   * 服务器内部错误
   *
   * <p>包括所有Spring内部的错误</p>
   *
   * @see ResponseEntityExceptionHandler#handleException(Exception, WebRequest)
   */
  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    String message = messageSource.getMessage(DictConstans.ERROR_INTERNAL, new Object[]{ex.getMessage()}, request.getLocale());
    log.error(message, ex);

    ErrorMessage errorMessage = new ErrorMessage(DictConstans.ERROR_INTERNAL, message);

    if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
      request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, RequestAttributes.SCOPE_REQUEST);
    }

    return new ResponseEntity<>(errorMessage, headers, status);
  }

  /**
   * 参数校验失败
   *
   * @param ex      被抛出的异常
   * @param request 当前的请求
   * @return 响应内容
   */
  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<ErrorMessage> handleBadRequestException(ConstraintViolationException ex, WebRequest request) {
    String message = getMessage(request.getLocale(), DictConstans.ERROR_VALIDATION_FAIL, getErrorMessage(ex));

    log.error(message);

    return getResponseEntity(DictConstans.ERROR_VALIDATION_FAIL, message, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(RecordNotFoundException.class)
  protected ResponseEntity<ErrorMessage> handleRecordNotFoundException(RecordNotFoundException ex, WebRequest request) {
    String message = ex.getMessage();

    logger.info(message);

    return getResponseEntity(DictConstans.ERROR_RECORD_NOT_FOUND, message, HttpStatus.NOT_FOUND);
  }

  /**
   * 业务异常
   *
   * @param ex      异常
   * @param request 当前请求
   * @return 响应内容
   */
  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<ErrorMessage> handleBadRequestException(BusinessException ex, WebRequest request) {
    String message = getMessage(request.getLocale(), ex.getCode(), ex.getParams());

    log.error("业务异常，code: {}, message: {}", ex.getCode(), message, ex);

    return getResponseEntity(ex.getCode(), message, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private String getErrorMessage(ConstraintViolationException ex) {
    Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

    StringBuilder builder = new StringBuilder();
    for (ConstraintViolation<?> violation : constraintViolations) {
      builder.append(violation.getPropertyPath()).append(":").append(violation.getMessage()).append("; ");
    }
    return builder.toString();
  }

  /**
   * 从ResourceBundle中获取消息的具体内容
   */
  private String getMessage(Locale locale, String code, Object... params) {
    return messageSource.getMessage(code, params, locale);
  }

  private ResponseEntity<ErrorMessage> getResponseEntity(String code, String message, HttpStatus status) {

    ErrorMessage errorMessage = new ErrorMessage(code, message);

    return new ResponseEntity<>(errorMessage, status);
  }
}
