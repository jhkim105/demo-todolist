package com.example.todolist.api.security;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthRefreshTokenResponseVO implements Serializable {

  private static final long serialVersionUID = -2293429942468208216L;

  private String authToken;

  public AuthRefreshTokenResponseVO(String accessToken) {
    this.authToken = accessToken;
  }
}
