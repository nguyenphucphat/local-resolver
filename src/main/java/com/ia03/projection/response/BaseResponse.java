package com.ia03.projection.response;

import java.util.Collections;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BaseResponse<T> {
  private T data;
  private String message;
  private String stackTrace;
  private String exceptionCode;

  private static final String SUCCEED_REQUEST_MESSAGE = "Success";

  public static <T> BaseResponse<T> of(T data) {
    return BaseResponse.<T>builder().data(data).message(SUCCEED_REQUEST_MESSAGE).build();
  }

  public static <T> BaseResponse<T> ok() {
    return BaseResponse.<T>builder().message(SUCCEED_REQUEST_MESSAGE).build();
  }

  public static <T> BaseResponse<T> error(
      String exceptionCode, String exceptionMessage, String stackTrace) {
    return BaseResponse.<T>builder()
        .message(exceptionMessage)
        .exceptionCode(exceptionCode)
        .stackTrace(stackTrace)
        .build();
  }

  public static <T> BaseResponse<T> empty() {
    return BaseResponse.<T>builder()
        .data((T) Collections.emptyList())
        .message(SUCCEED_REQUEST_MESSAGE)
        .build();
  }
}
