package com.example.todolist.api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtUtils {

  private JWTVerifier jwtVerifier;

  private Algorithm algorithm;

  public JwtUtils(String secret) {
    this.algorithm = Algorithm.HMAC256(secret);
    this.jwtVerifier = JWT.require(algorithm).build();
  }

  public String generateToken(AuthUser authUser, Date expireDate) {
    Date now = new Date();
    JWTCreator.Builder builder = JWT.create()
        .withClaim("id", authUser.getId())
        .withClaim("authority", authUser.getAuthority())
        .withIssuedAt(now);

    if (expireDate != null)
      builder.withExpiresAt(expireDate);

    String token = builder.sign(algorithm);
    log.debug("token:{}", token);
    return token;
  }

  public String generateToken(Long id, String authotiry, Date expireDt) {
    AuthUser authUser = new AuthUser(id, authotiry);
    return generateToken(authUser, expireDt);
  }

  public void checkToken(String token) {
    try {
      jwtVerifier.verify(token);
    } catch (TokenExpiredException ex) {
      throw new AuthTokenExpiredException(ex);
    } catch (JWTVerificationException ex) {
      throw new AuthTokenInvalidException(ex);
    }

  }

  public AuthUser parseToken(String token) {
    checkToken(token);
    try {
      DecodedJWT jwt = JWT.decode(token);
      Long id = jwt.getClaim("id").asLong();
      String authority = jwt.getClaim("authority").asString();
      AuthUser authUser = new AuthUser(id, authority);
      return authUser;
    } catch (JWTDecodeException ex) {
      throw new AuthTokenInvalidException(ex);
    }
  }

}
