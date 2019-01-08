package com.example.todolist.api.security;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthTokenResponseVO implements Serializable {

  private static final long serialVersionUID = 3417144000325211448L;

  private String authToken;

  private String refreshToken;

  public AuthTokenResponseVO(String accessToken, String refreshToken) {
    this.authToken = accessToken;
    this.refreshToken = refreshToken;
  }
}
