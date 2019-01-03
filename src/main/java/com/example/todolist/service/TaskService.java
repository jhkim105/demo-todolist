package com.example.todolist.service;

import com.example.todolist.model.*;
import org.springframework.data.domain.*;

public interface TaskService {
  Page<Task> findPaginated(int page, int size);

  Task save(Task task);

  Task finish(Long id);
}
