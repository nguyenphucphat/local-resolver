package com.ia03.interceptor;

import com.ia03.exception.BaseInternalException;
import com.ia03.projection.response.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
  private static final String VALIDATION_VIOLATION_EXCEPTION_CODE =
      "VALIDATION_VIOLATION_EXCEPTION";
  private static final String MESSAGE_NOT_READABLE_EXCEPTION_CODE =
      "MESSAGE_NOT_READABLE_EXCEPTION";
  private static final String CONSTRAINT_VIOLATION_EXCEPTION_CODE =
      "CONSTRAINT_VIOLATION_EXCEPTION";
  private static final String INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION_CODE =
      "INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION_CODE";

  private static final String ACCESS_DENIED_EXCEPTION_CODE = "ACCESS_DENIED_EXCEPTION";
  private static final String ACCESS_DENIED_EXCEPTION_MESSAGE =
      "You can not access this resource due to credential permission";

  private static final String BAD_CREDENTIAL_EXCEPTION_CODE = "BAD_CREDENTIAL_EXCEPTION";
  private static final String BAD_CREDENTIAL_EXCEPTION_MESSAGE =
      "The username or password is wrong, please try again";

  private String getRootCauseMessage(Exception exception) {
    return ExceptionUtils.getRootCauseMessage(exception);
  }

  private String getExceptionStackTrace(Exception exception) {
    return Arrays.toString(exception.getStackTrace());
  }

  private ResponseEntity<Object> wrapWithResponse(
      String code, String message, String stackTrace, HttpStatus status) {
    return new ResponseEntity<>(BaseResponse.error(code, message, stackTrace), status);
  }

  @ExceptionHandler(BaseInternalException.class)
  public ResponseEntity<Object> handleBaseDomainException(BaseInternalException exception) {
    return wrapWithResponse(
        exception.getCode(),
        getRootCauseMessage(exception),
        getExceptionStackTrace(exception),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException exception) {
    return wrapWithResponse(
        BAD_CREDENTIAL_EXCEPTION_CODE,
        BAD_CREDENTIAL_EXCEPTION_MESSAGE,
        getExceptionStackTrace(exception),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Object> handleRuntimeException(RuntimeException exception) {
    var rootCause = ExceptionUtils.getRootCause(exception);
    var isInstanceOfBaseDomainException = rootCause instanceof BaseInternalException;
    var isInstanceOfBadCredentialsException = rootCause instanceof BadCredentialsException;

    if (isInstanceOfBaseDomainException)
      return this.handleBaseDomainException((BaseInternalException) rootCause);
    else if (isInstanceOfBadCredentialsException)
      return this.handleBadCredentialsException((BadCredentialsException) exception.getCause());

    return wrapWithResponse(
        String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR),
        getRootCauseMessage(exception),
        getExceptionStackTrace(exception),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> handleConstraintViolationException(
      ConstraintViolationException exception) {
    return wrapWithResponse(
        CONSTRAINT_VIOLATION_EXCEPTION_CODE,
        getRootCauseMessage(exception),
        getExceptionStackTrace(exception),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InternalAuthenticationServiceException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> handleInternalAuthenticationServiceException(
      InternalAuthenticationServiceException exception) {
    var rootCause = ExceptionUtils.getRootCause(exception);
    if (rootCause instanceof BaseInternalException domainRootCause) {
      return wrapWithResponse(
          domainRootCause.getCode(),
          domainRootCause.getMessage(),
          getExceptionStackTrace(exception),
          HttpStatus.BAD_REQUEST);
    }

    return wrapWithResponse(
        INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION_CODE,
        getRootCauseMessage(exception),
        getExceptionStackTrace(exception),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Object> handleAccessDeniedException(
      HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    return wrapWithResponse(
        ACCESS_DENIED_EXCEPTION_CODE,
        ACCESS_DENIED_EXCEPTION_MESSAGE,
        getExceptionStackTrace(exception),
        HttpStatus.FORBIDDEN);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      @NonNull MethodArgumentNotValidException exception,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
    return wrapWithResponse(
        VALIDATION_VIOLATION_EXCEPTION_CODE,
        exception.getBindingResult().getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(", ")),
        getExceptionStackTrace(exception),
        HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      @NonNull HttpMessageNotReadableException exception,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
    return wrapWithResponse(
        MESSAGE_NOT_READABLE_EXCEPTION_CODE,
        getRootCauseMessage(exception),
        getExceptionStackTrace(exception),
        HttpStatus.BAD_REQUEST);
  }
}
