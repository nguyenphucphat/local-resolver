package com.ia03.exception;

import lombok.NonNull;

public class EntityAlreadyExistException extends BaseInternalException {
  private static final String CODE = "IA03.EX.004";
  private static final String MESSAGE = "The entity already exist";

  public EntityAlreadyExistException(@NonNull String message, @NonNull String code) {
    super(message, code);
  }

  public EntityAlreadyExistException() {
    super(MESSAGE, CODE);
  }

  public EntityAlreadyExistException(@NonNull String message) {
    super(message, CODE);
  }
}
