package com.ia03.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class RequestLoggerInterceptor implements HandlerInterceptor {
  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    return true;
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    log.info("{} {} {}", request.getMethod(), request.getRequestURL(), response.getStatus());
  }
}
