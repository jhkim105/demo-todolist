package com.example.todolist.core.repository;

import com.example.todolist.core.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "tasks", path = "tasks")
public interface TaskRepository extends JpaRepository<Task, Long>, TaskRepositoryCustom{

  Page<Task> findAllByDescriptionLike(Pageable page, String q);
}
