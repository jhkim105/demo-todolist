package com.example.todolist;

import com.example.todolist.api.security.AuthUser;
import com.example.todolist.api.security.SecurityUtils;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;

public class SpringSecurityAuditorAware implements AuditorAware {

  @Override
  public Optional getCurrentAuditor() {
    AuthUser user = SecurityUtils.getCurrentUserSilently();
    return user == null ? Optional.empty() : Optional.of(user.getId());
  }
}
