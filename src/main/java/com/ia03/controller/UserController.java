package com.ia03.controller;

import com.ia03.annotation.IsAuthenticated;
import com.ia03.projection.request.LoginRequest;
import com.ia03.projection.request.RegisterUserRequest;
import com.ia03.projection.response.BaseResponse;
import com.ia03.projection.response.LoginResponse;
import com.ia03.projection.response.UserResponse;
import com.ia03.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping("/register")
  @Operation(tags = "User", summary = "Register a new user")
  @ResponseStatus(HttpStatus.CREATED)
  public BaseResponse<LoginResponse> register(@RequestBody @Valid RegisterUserRequest request) {
    return BaseResponse.of(userService.register(request));
  }

  @PostMapping("/login")
  @Operation(tags = "User", summary = "Login a user")
  @ResponseStatus(HttpStatus.OK)
  public BaseResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
    return BaseResponse.of(userService.login(request));
  }

  @GetMapping("/get-me")
  @Operation(tags = "User", summary = "Get the current user")
  @ResponseStatus(HttpStatus.OK)
  @IsAuthenticated
  public BaseResponse<UserResponse> getMe() {
    return BaseResponse.of(userService.getMe());
  }

  @GetMapping()
  @Operation(tags = "User", summary = "Get all users")
  @ResponseStatus(HttpStatus.OK)
  public BaseResponse<List<UserResponse>> getAllUsers() {
    return BaseResponse.of(userService.getAllUsers());
  }

  @DeleteMapping("/{id}")
  @Operation(tags = "User", summary = "Delete a user")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public BaseResponse<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);

    return BaseResponse.ok();
  }
}
