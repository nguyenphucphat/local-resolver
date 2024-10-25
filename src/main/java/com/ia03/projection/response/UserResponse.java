package com.ia03.projection.response;

import com.ia03.entity.user.User;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
  private Long id;
  private String name;
  private String email;
  private LocalDateTime createdAt;

  public static UserResponse fromDomain(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .name(user.getName())
        .email(user.getEmail())
        .createdAt(user.getCreatedAt())
        .build();
  }
}
