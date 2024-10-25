package com.ia03.service;

import org.springframework.security.core.Authentication;

public interface JwtService {
  Boolean validateToken(String token);

  String generateToken(Authentication authentication);

  String generateToken(String principal, Long jwtExpirationMs);

  String extractCredential(String token);

  String extractPrincipal(Authentication authentication);
}
