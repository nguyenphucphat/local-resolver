package com.ia03.entity.user;

import com.ia03.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class User extends BaseEntity {
  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "name", nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  @Builder.Default
  private UserRole role = UserRole.USER;
}
