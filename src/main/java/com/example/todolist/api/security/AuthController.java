package com.example.todolist.api.security;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "auth")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController  {

  private final AuthService authService;

  @GetMapping("/token")
  public AuthTokenResponseVO token(String username, String password) {
    return authService.getAuthToken(username, password);
  }

  @GetMapping("/check_token")
  public void checkToken(@RequestParam String token) {
    authService.checkToken(token);
  }

  @GetMapping("/refresh_token")
  public AuthRefreshTokenResponseVO refreshToken(@RequestParam String refreshToken) {
    return authService.getRefreshToken(refreshToken);
  }

}
