package com.ia03.projection.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {
  @NotBlank(message = "Username should not be empty")
  private String username;

  @NotBlank(message = "Password should not be empty")
  @Pattern(
      regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
      message =
          "Password should contain at least 8 characters, one uppercase letter, one lowercase letter, one number, and one special character")
  private String password;
}
