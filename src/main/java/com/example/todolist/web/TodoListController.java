package com.example.todolist.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TodoListController {

  @GetMapping("/todo-list")
  public String todoList() {
    return "todo-list";
  }

}
