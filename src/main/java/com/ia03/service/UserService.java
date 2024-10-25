package com.ia03.service;

import com.ia03.projection.request.LoginRequest;
import com.ia03.projection.request.RegisterUserRequest;
import com.ia03.projection.response.LoginResponse;
import com.ia03.projection.response.UserResponse;
import java.util.List;

public interface UserService {
  LoginResponse login(LoginRequest request);

  LoginResponse register(RegisterUserRequest request);

  UserResponse getMe();

  List<UserResponse> getAllUsers();

  void deleteUser(Long id);
}
