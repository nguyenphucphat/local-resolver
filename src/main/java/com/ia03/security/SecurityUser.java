package com.ia03.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ia03.entity.user.User;
import java.util.Collection;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SecurityUser implements UserDetails {
  private String username;
  @JsonIgnore private String password;
  private Collection<? extends GrantedAuthority> authorities;
  private User user;

  private static final String ROLE_PREFIX = "ROLE_%s";

  public static SecurityUser build(User user) {
    // TODO: Map User to Security User
    GrantedAuthority authority = buildRole(user.getRole().name());
    return new SecurityUser(
        user.getUsername(), user.getPassword(), Collections.singletonList(authority), user);
  }

  private static GrantedAuthority buildRole(String role) {
    return new SimpleGrantedAuthority(String.format(ROLE_PREFIX, role));
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return String.valueOf(this.username);
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
