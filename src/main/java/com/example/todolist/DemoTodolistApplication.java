package com.example.todolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@SpringBootApplication
public class DemoTodolistApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoTodolistApplication.class, args);
  }

}

