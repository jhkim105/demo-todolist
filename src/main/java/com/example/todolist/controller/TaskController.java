package com.example.todolist.controller;

import com.example.todolist.model.*;
import com.example.todolist.service.*;
import lombok.*;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

  private final TaskService service;

  @GetMapping
  public List<Task> findPaginated(@RequestParam("page") int page, @RequestParam("size") int size) {
    Page<Task> resultPage = service.findPaginated(page, size);
    if (page > resultPage.getTotalPages()) {
      throw new RuntimeException(String.format("requested page[%s] is bigger than totalPages[%s]",
          page, resultPage.getTotalPages()));
    }

    return resultPage.getContent();
  }

  @PostMapping
  public Task create(@RequestBody Task task) {
    return service.save(task);
  }

  @PutMapping
  public Task update(@RequestBody Task task) {
    return service.save(task);
  }

  @GetMapping(path="/{id}/finish")
  public Task finish(@PathVariable Long id) {
    return service.finish(id);
  }
}
