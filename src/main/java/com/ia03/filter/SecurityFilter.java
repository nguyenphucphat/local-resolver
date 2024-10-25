package com.ia03.filter;

import com.ia03.exception.InvalidTokenException;
import com.ia03.projection.response.BaseResponse;
import com.ia03.service.JsonService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
  private final JsonService jsonService;

  private static final String UN_AUTHORIZED_MESSAGE =
      "Can not authorize the user due to invalid token";

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (Exception exception) {
      if (exception instanceof InvalidTokenException)
        handleJwtException(response, (InvalidTokenException) exception);
      else handleUnknownException(response, exception);
    }
  }

  private void byPassCors(@NonNull HttpServletResponse response) {
    // TODO: Restrict when releasing to the production
    response.setHeader("Access-Control-Allow-Origin", "*");
  }

  @SneakyThrows
  private void handleJwtException(
      @NonNull HttpServletResponse response, InvalidTokenException exception) {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response
        .getWriter()
        .write(
            jsonService.toJson(
                BaseResponse.builder()
                    .data(null)
                    .message(UN_AUTHORIZED_MESSAGE)
                    .exceptionCode(exception.getCode())
                    .stackTrace(Arrays.toString(exception.getStackTrace()))
                    .build()));
  }

  @SneakyThrows
  private void handleUnknownException(@NonNull HttpServletResponse response, Exception exception) {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    response
        .getWriter()
        .write(
            jsonService.toJson(
                BaseResponse.error(
                    ExceptionUtils.getRootCauseMessage(exception),
                    ExceptionUtils.getRootCauseMessage(exception),
                    Arrays.toString(exception.getStackTrace()))));
  }
}
