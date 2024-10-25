package com.ia03.exception;

import lombok.NonNull;

public class PermissionDeniedException extends BaseInternalException {
  private static final String CODE = "IA03.EX.002";
  private static final String MESSAGE =
      "The current user credential does not have the permission to access resources";

  public PermissionDeniedException(@NonNull String message, @NonNull String code) {
    super(message, code);
  }

  public PermissionDeniedException() {
    super(MESSAGE, CODE);
  }

  public PermissionDeniedException(@NonNull String message) {
    super(message, CODE);
  }
}
