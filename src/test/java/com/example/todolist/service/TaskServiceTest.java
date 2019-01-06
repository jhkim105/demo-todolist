package com.example.todolist.service;

import com.example.todolist.api.service.TaskService;
import com.example.todolist.api.service.TaskServiceImpl;
import com.example.todolist.core.model.Task;
import com.example.todolist.core.repository.TaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
public class TaskServiceTest {

  @TestConfiguration
  static class EmployeeServiceImplTestContextConfiguration {

    @Bean
    public TaskService taskService() {
      return new TaskServiceImpl() {
      };
    }
  }

  @Autowired
  private TaskService taskService;

  @MockBean
  private TaskRepository taskRepository;




  @Test
  public void close() throws Exception {
    // given
    final long sampleTaskId = -1;
    Task sampleTask = newTask(sampleTaskId, false, "This is open task.");
    Task sampleSubTask = newTask(-2, true, "This is closed sub task");
    sampleTask.getSubTasks().add(sampleSubTask);

    Mockito.when(taskRepository.findById(sampleTask.getId()))
        .thenReturn(Optional.of(sampleTask));

    Mockito.when(taskRepository.save(sampleTask))
        .thenReturn(sampleTask);

    // when
    Task task = taskService.close(sampleTaskId);

    // then
    assertThat(task.isClosed())
        .isTrue();
  }

  @Test(expected = IllegalStateException.class)
  public void close_fail_existsOpenSubTasks() throws Exception {
    // given
    final long sampleTaskId = -1;
    Task sampleTask = newTask(sampleTaskId, false, "This is open task.");
    Task sampleSubTask = newTask(-2, false, "This is open sub task");
    sampleTask.getSubTasks().add(sampleSubTask);

    Mockito.when(taskRepository.findById(sampleTask.getId()))
        .thenReturn(Optional.of(sampleTask));

    Mockito.when(taskRepository.save(sampleTask))
        .thenReturn(sampleTask);

    // when
    Task task = taskService.close(sampleTaskId);

    // then
    assertThat(task.isClosed())
        .isFalse();
  }

  @Test(expected = IllegalStateException.class)
  public void close_fail_alreadyClosedTask() {
    // given
    final long sampleTaskId = -1;
    Task sampleTask = newTask(sampleTaskId, true, "This is closed task.");

    Mockito.when(taskRepository.findById(sampleTask.getId()))
        .thenReturn(Optional.of(sampleTask));

    Mockito.when(taskRepository.save(sampleTask))
        .thenReturn(sampleTask);

    // when
    Task task = taskService.close(sampleTaskId);
  }

  @Test
  public void open() {
    // given
    final long sampleTaskId = -1;
    Task sampleTask = newTask(sampleTaskId, true, "This is closed task.");

    Mockito.when(taskRepository.findById(sampleTask.getId()))
        .thenReturn(Optional.of(sampleTask));

    Mockito.when(taskRepository.save(sampleTask))
        .thenReturn(sampleTask);

    // when
    Task task = taskService.open(sampleTaskId);
  }

  @Test(expected = IllegalStateException.class)
  public void open_fail_alreadyOpen() {
    // given
    final long sampleTaskId = -1;
    Task sampleTask = newTask(sampleTaskId, false, "This is open task.");

    Mockito.when(taskRepository.findById(sampleTask.getId()))
        .thenReturn(Optional.of(sampleTask));

    Mockito.when(taskRepository.save(sampleTask))
        .thenReturn(sampleTask);

    // when
    Task task = taskService.open(sampleTaskId);
  }

  private Task newTask(long id, boolean closed, String description) {
    Task task = new Task();
    task.setDescription(description);
    task.setId(id);
    task.setClosed(closed);
    return task;
  }

}