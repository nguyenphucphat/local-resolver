package com.ia03.service.impl;

import com.ia03.exception.InvalidTokenException;
import com.ia03.security.SecurityUser;
import com.ia03.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private Long jwtExpirationMs;

  @SneakyThrows
  @Override
  public Boolean validateToken(String token) {
    if (token.isEmpty()) return false;
    try {
      Jwts.parserBuilder().setSigningKey(generateSecretKey()).build().parse(token);
    } catch (MalformedJwtException
        | ExpiredJwtException
        | UnsupportedJwtException
        | IllegalArgumentException exception) {
      throw new InvalidTokenException();
    }
    return true;
  }

  @Override
  public String generateToken(Authentication authentication) {
    return this.generateToken(extractPrincipal(authentication), jwtExpirationMs);
  }

  @Override
  public String generateToken(String principal, Long jwtExpirationMs) {
    return Jwts.builder()
        .setSubject(principal)
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(generateSecretKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  @Override
  public String extractCredential(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(generateSecretKey())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  @Override
  public String extractPrincipal(Authentication authentication) {
    return ((SecurityUser) authentication.getPrincipal()).getUsername();
  }

  private Key generateSecretKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.jwtSecret));
  }
}
