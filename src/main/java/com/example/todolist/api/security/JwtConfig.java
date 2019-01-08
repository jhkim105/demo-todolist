package com.example.todolist.api.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

  public static final long AUTH_TOKEN_EXPIRE_DAY = 1;
  public static final long REFRESH_TOKEN_EXPIRE_DAY = 30;

  @Value("${jwt.key}")
  private String jwtKey;

  @Bean
  public JwtUtils jwtUtils() {
    return new JwtUtils(jwtKey);
  }

}
