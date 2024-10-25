package com.ia03.exception;

import lombok.NonNull;

public class InvalidTokenException extends BaseInternalException {
  private static final String CODE = "IA03.EX.001";
  private static final String MESSAGE = "The jwt token is invalid";

  public InvalidTokenException(@NonNull String message, @NonNull String code) {
    super(message, code);
  }

  public InvalidTokenException() {
    super(MESSAGE, CODE);
  }

  public InvalidTokenException(@NonNull String message) {
    super(message, CODE);
  }
}
