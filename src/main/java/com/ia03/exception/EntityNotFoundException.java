package com.ia03.exception;

import lombok.NonNull;

public class EntityNotFoundException extends BaseInternalException {
  private static final String CODE = "IA03.EX.003";
  private static final String MESSAGE = "The entity could not be found";

  public EntityNotFoundException(@NonNull String message, @NonNull String code) {
    super(message, code);
  }

  public EntityNotFoundException() {
    super(MESSAGE, CODE);
  }

  public EntityNotFoundException(@NonNull String message) {
    super(message, CODE);
  }
}
