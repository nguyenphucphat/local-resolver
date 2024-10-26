package com.ia03.service.impl;

import com.ia03.entity.user.User;
import com.ia03.exception.EntityAlreadyExistException;
import com.ia03.exception.EntityNotFoundException;
import com.ia03.projection.request.LoginRequest;
import com.ia03.projection.request.RegisterUserRequest;
import com.ia03.projection.response.LoginResponse;
import com.ia03.projection.response.UserResponse;
import com.ia03.repository.UserRepository;
import com.ia03.security.SecurityUser;
import com.ia03.security.SecurityUserService;
import com.ia03.service.JwtService;
import com.ia03.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  @Override
  @SneakyThrows
  public LoginResponse login(LoginRequest request) {
    if (!userRepository.existsByUsername(request.getUsername())) {
      throw new EntityNotFoundException("Username not found");
    }

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    String token = jwtService.generateToken(authentication);

    return LoginResponse.builder().token(token).build();
  }

  @Override
  @SneakyThrows
  public LoginResponse register(RegisterUserRequest request) {
    if (userRepository.existsByUsername(request.getUsername())) {
      throw new EntityAlreadyExistException("Username already exists");
    }

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new EntityAlreadyExistException("Email already exists");
    }

    User user =
        User.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .name(request.getName())
            .build();

    User saved = userRepository.save(user);
    SecurityUser securityUser = SecurityUser.build(saved);
    Authentication authentication =
        new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());

    return LoginResponse.builder().token(jwtService.generateToken(authentication)).build();
  }

  @Override
  public UserResponse getMe() {
    User currentUser = SecurityUserService.getCurrentUser();

    return UserResponse.fromDomain(currentUser);
  }

  @Override
  public List<UserResponse> getAllUsers() {
    List<User> users =
        Optional.ofNullable(userRepository.findAllByOrderByIdAsc()).orElse(new ArrayList<>());

    System.out.println("users: " + users);

    return users.stream().map(UserResponse::fromDomain).collect(Collectors.toList());
  }

  @Override
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }
}
