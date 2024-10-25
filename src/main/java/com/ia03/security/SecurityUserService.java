package com.ia03.security;

import com.ia03.entity.user.User;
import com.ia03.exception.EntityNotFoundException;
import com.ia03.exception.PermissionDeniedException;
import com.ia03.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityUserService implements UserDetailsService {
  private final UserRepository userRepository;

  private static final String ANONYMOUS_USER = "anonymousUser";

  @SneakyThrows
  public static User getCurrentUser() {
    var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (ANONYMOUS_USER.equals(principal)) throw new PermissionDeniedException();
    var securityUser = (SecurityUser) principal;

    return securityUser.getUser();
  }

  @SneakyThrows
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user =
        this.userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);

    return SecurityUser.build(user);
  }
}
