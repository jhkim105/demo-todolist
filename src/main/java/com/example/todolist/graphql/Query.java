package com.example.todolist.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.example.todolist.core.model.Task;
import com.example.todolist.core.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Query implements GraphQLQueryResolver {

  @Autowired
  private TaskRepository taskRepository;

  public List<Task> getTasks() {

    return taskRepository.findAll();
  }
}