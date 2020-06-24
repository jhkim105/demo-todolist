package com.example.todolist;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.todolist.core.repository")
@EntityScan(basePackages = "com.example.todolist.core.model")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaConfig {
  @Bean
  public SpringSecurityAuditorAware auditorAware() {
    return new SpringSecurityAuditorAware();
  }
}
