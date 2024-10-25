package com.ia03.config;

import com.ia03.filter.AuthenticationFilter;
import com.ia03.filter.SecurityFilter;
import com.ia03.interceptor.RequestLoggerInterceptor;
import com.ia03.interceptor.SecurityEntryInterceptor;
import com.ia03.security.SecurityUserService;
import com.ia03.service.JsonService;
import com.ia03.service.JwtService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {
  private static final String CORS_PATH_PATTERNS = "/**";
  private static final String[] CORS_ALLOWED_ORIGINS = {"*"};
  private static final String[] CORS_ALLOWED_HEADERS = {"*"};
  private static final String[] CORS_EXPOSED_HEADERS = {"*"};
  private static final String[] CORS_ALLOWED_METHODS = {
    "GET", "PUT", "POST", "DELETE", "PATCH", "OPTIONS"
  };

  private final JwtService jwtService;
  private final JsonService jsonService;
  private final SecurityUserService securityUserService;
  private final SecurityEntryInterceptor securityEntryPointInterceptor;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationFilter authenticationFilter() {
    return new AuthenticationFilter(jwtService, securityUserService);
  }

  @Bean
  public SecurityFilter securityFilter() {
    return new SecurityFilter(jsonService);
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    var authenticationProvider = new DaoAuthenticationProvider();

    authenticationProvider.setUserDetailsService(this.securityUserService);
    authenticationProvider.setPasswordEncoder(passwordEncoder());

    return authenticationProvider;
  }

  @Bean
  @SneakyThrows
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Override
  public void addCorsMappings(@NonNull CorsRegistry registry) {
    registry
        .addMapping(CORS_PATH_PATTERNS)
        .allowedOrigins(CORS_ALLOWED_ORIGINS)
        .allowedHeaders(CORS_ALLOWED_HEADERS)
        .exposedHeaders(CORS_EXPOSED_HEADERS)
        .allowedMethods(CORS_ALLOWED_METHODS);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(
            exception -> exception.authenticationEntryPoint(securityEntryPointInterceptor))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(request -> request.anyRequest().permitAll());

    http.authenticationProvider(authenticationProvider());

    http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    http.addFilterBefore(securityFilter(), AuthenticationFilter.class);

    return http.build();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new RequestLoggerInterceptor());
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {}
}
