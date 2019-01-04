package com.example.todolist.service;

import com.example.todolist.model.*;
import com.example.todolist.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;

import java.util.*;

@Service
public class TaskServiceImpl implements TaskService {

  @Autowired
  private TaskRepository repository;

  @Override
  public Page<Task> findPaginated(int page, int size) {
    PageRequest pageRequest = PageRequest.of(page, size);
    return repository.findAll(pageRequest);
  }

  @Override
  public Task save(Task task) {
    List<Long> superTaskIds = task.getSuperTaskIds();
    task.getSuperTasks().clear();
    if (!CollectionUtils.isEmpty(superTaskIds)) {
      addSuperTasks(task, superTaskIds);
    }

    return repository.save(task);
  }

  private void addSuperTasks(Task task, List<Long> superTaskIds) {
    if (CollectionUtils.isEmpty(superTaskIds)) {
      return;
    }

    for (Long superTaskId : superTaskIds) {
      addSuperTask(task, superTaskId);
    }

  }

  private void addSuperTask(Task task, Long superTaskId) {
    Task superTask = repository.getOne(superTaskId);
    if (superTask == null) {
      throw new RuntimeException(String.format("Task[%s] not found.", superTaskId));
    }
    task.getSuperTasks().add(superTask);
  }

  @Override
  public Task finish(Long id) {
    try {
      Task task = repository.findById(id).get();
      if (task.isFinished()) {
        throw new RuntimeException("Already finished.");
      }
      if (task.existsUnfinishedSubTasks()) {
        throw new RuntimeException("Unfinished sub tasks exists");
      }

      task.processFinish();
      return repository.save(task);

    } catch (NoSuchElementException ex) {
      throw new RuntimeException(String.format("Task[%s] not found.", id), ex);
    }
  }
}
