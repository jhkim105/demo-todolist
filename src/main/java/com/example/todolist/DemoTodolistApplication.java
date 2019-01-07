package com.example.todolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DemoTodolistApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoTodolistApplication.class, args);
  }

}

