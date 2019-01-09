package com.example.todolist.core.repository;

import com.example.todolist.core.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

  Page<Task> findAllByDescriptionLike(Pageable page, String q);
}
