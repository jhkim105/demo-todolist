package com.example.todolist.api.security;

import com.example.todolist.core.model.User;
import com.example.todolist.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.NoSuchElementException;

@Service
public class AuthService {

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  public AuthTokenResponseVO getAuthToken(String username, String password) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new NoSuchElementException(String.format("[%s] not found", username));
    }

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new IllegalArgumentException("password not matched.");
    }

    AuthUser authUser = new AuthUser(user);
    ZonedDateTime today = ZonedDateTime.now();
    String authToken = jwtUtils.generateToken(authUser, Date.from(today.plusDays(JwtConfig.AUTH_TOKEN_EXPIRE_DAY).toInstant()));
    String refreshToken = jwtUtils.generateToken(authUser, Date.from(today.plusDays(JwtConfig.REFRESH_TOKEN_EXPIRE_DAY).toInstant()));
    AuthTokenResponseVO responseVO = new AuthTokenResponseVO(authToken, refreshToken);
    return responseVO;
  }

  @Transactional(readOnly = true)
  public AuthRefreshTokenResponseVO getRefreshToken(String refreshToken) {
    AuthUser authUser = jwtUtils.parseToken(refreshToken);
    String authToken = jwtUtils.generateToken(authUser, Date.from(ZonedDateTime.now().plusDays(JwtConfig.REFRESH_TOKEN_EXPIRE_DAY).toInstant()));
    return new AuthRefreshTokenResponseVO(authToken);
  }

  public void checkToken(String token) {
    jwtUtils.checkToken(token);
  }
}
