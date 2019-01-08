package com.example.todolist;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
@WebMvcTestExclude
public class JpaConfig {
}
