package com.example.todolist.api.service;

import com.example.todolist.core.model.Task;
import org.springframework.data.domain.Page;

public interface TaskService {
  Page<Task> findPaginated(int page, int size);

  Task save(Task task);

  Task close(Long id);

  Task open(Long id);

  Task findOne(Long id);

}