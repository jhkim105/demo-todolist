package com.example.todolist.api.service;

import com.example.todolist.api.controller.SearchVO;
import com.example.todolist.core.model.Task;
import com.example.todolist.core.repository.TaskRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskServiceImpl implements TaskService {

  @Autowired
  private TaskRepository repository;

  @Override
  public Page<Task> findAll(Pageable page) {
    Page<Task> taskPage = repository.findAll(page);
    if (page.getPageNumber() > taskPage.getTotalPages()) {
      throw new IllegalArgumentException(String.format("requested page[%s] is bigger than totalPages[%s]",
          page, taskPage.getTotalPages()));
    }
    return taskPage;

  }

  @Override
  public Page<Task> findAll(Pageable page, SearchVO searchVO) {
    Page<Task> taskPage = repository.findAll(page, searchVO.getQ(), searchVO.isOpen());;

    if (page.getPageNumber() > taskPage.getTotalPages()) {
      throw new IllegalArgumentException(String.format("requested page[%s] is bigger than totalPages[%s]",
          page, taskPage.getTotalPages()));
    }
    return taskPage;
  }

  @Override
  public Task save(Task task) {
    List<String> superTaskIds = task.getSuperTaskIds();
    checkSuperTaskIds(task, superTaskIds);
    task.getSuperTasks().clear();
    if (!CollectionUtils.isEmpty(superTaskIds)) {
      addSuperTasks(task, superTaskIds);
    }

    return repository.save(task);
  }

  private void checkSuperTaskIds(Task task, List<String> superTaskIds) {
    if (CollectionUtils.isEmpty(superTaskIds)) {
      return;
    }

    if (superTaskIds.contains(String.valueOf(task.getRefId()))) {
      throw new IllegalArgumentException("Can't save itself as parent.");
    }

    if (!CollectionUtils.isEmpty(task.getSubTasks())) {
      for (Task subTask : task.getSubTasks()) {
        if (superTaskIds.contains(String.valueOf(subTask.getRefId()))) {
          throw new IllegalArgumentException("Can't have children as parent.");
        }
      }
    }

  }

  private void addSuperTasks(Task task, List<String> superTaskIds) {
    if (CollectionUtils.isEmpty(superTaskIds)) {
      return;
    }

    for (String superTaskId : superTaskIds) {
      addSuperTask(task, superTaskId);
    }

  }

  private void addSuperTask(Task task, String superTaskId) {
    Long id = Long.valueOf(StringUtils.removeStart(superTaskId, Task.SUPER_TASK_PREFIX));
    Task superTask = repository.getOne(id);
    if (superTask == null) {
      throw new NoSuchElementException(String.format("Task[%s] not found.", id));
    }
    task.getSuperTasks().add(superTask);
  }

  @Override
  public Task close(Long id) {
    Task task = findOne(id);
    if (task.isClosed()) {
      throw new IllegalStateException("Already closed.");
    }
    if (task.existsOpenedSubTasks()) {
      throw new IllegalStateException("Opened sub tasks exists");
    }

    task.close();
    return repository.save(task);
  }

  @Override
  public Task open(Long id) {
    Task task = findOne(id);
    if (task.isOpened()) {
      throw new IllegalStateException("Already Opened.");
    }

    task.open();
    return repository.save(task);
  }

  @Override
  public Task findOne(Long id) {
    try {
      return repository.findById(id).get();
    } catch(NoSuchElementException ex) {
      throw new NoSuchElementException(String.format("Task[%s] not found.", id));
    }
  }

}
