package com.example.todolist.core.repository;

import com.example.todolist.core.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskRepositoryCustom {
  Page<Task> findAll(Pageable page, String q, boolean open);
}
