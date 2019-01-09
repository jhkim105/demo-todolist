package com.example.todolist.api.service;

import com.example.todolist.api.controller.SearchVO;
import com.example.todolist.core.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

  Page<Task> findAll(Pageable pageable);

  Page<Task> findAll(Pageable pageable, SearchVO searchVO);

  Task save(Task task);

  Task close(Long id);

  Task open(Long id);

  Task findOne(Long id);

  void delete(Long id);
}
