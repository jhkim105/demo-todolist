package com.example.todolist.api.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
public final class SecurityUtils {

  private SecurityUtils() {
  }


  public static String getCurrentUserId() {
    return getCurrentUser().getId();
  }

  public static AuthUser getCurrentUser() {
    AuthUser currentUser = (AuthUser) getCurrentUserDetails();
    if (currentUser == null) {
      throw new AccessDeniedException("User not found in security context.");
    }
    return currentUser;
  }

  public static AuthUser getCurrentUserSilently() {
    AuthUser user = null;
    try {
      user = getCurrentUser();
    } catch (RuntimeException ex) {
      log.debug("exception ignored. message:{}", ex.getMessage());
      // ignored
    }
    return user;
  }

  public static UserDetails getCurrentUserDetails() {
    UserDetails userDetails = null;
    SecurityContext ctx = SecurityContextHolder.getContext();
    Authentication auth = ctx.getAuthentication();
    if (auth == null) {
      return null;
    }

    AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
    boolean signupUser = resolver.isAnonymous(auth);
    if (!signupUser) {
      if (auth.getPrincipal() instanceof UserDetails) {
        userDetails = (UserDetails) auth.getPrincipal();
      } else if (auth.getDetails() instanceof UserDetails) {
        userDetails = (UserDetails) auth.getDetails();
      }
    }

    return userDetails;
  }

}