package com.example.todolist.api.service;

import com.example.todolist.TestUtils;
import com.example.todolist.core.model.Task;
import com.example.todolist.core.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.util.Optional;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Java6Assertions.assertThat;

@ExtendWith({SpringExtension.class})
public class TaskServiceTest {

  @TestConfiguration
  static class TestContextConfiguration {

    @Bean
    public TaskService taskService() {
      return new TaskServiceImpl();
    }
  }

  @Autowired
  private TaskService taskService;

  @MockBean
  private TaskRepository taskRepository;

  @Test
  public void testClose() {
    // given
    final long sampleTaskId = 1L;
    Task sampleTask = TestUtils.newTask(sampleTaskId, false, "This is open task.");
    Task sampleSubTask = TestUtils.newTask(2L, true, "This is closed sub task");
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

  @Test
  public void testClose_fail_existsOpenSubTasks() {
    // given
    final long sampleTaskId = 1L;
    Task sampleTask = TestUtils.newTask(sampleTaskId, false, "This is open task.");
    Task sampleSubTask = TestUtils.newTask(2L, false, "This is open sub task");
    sampleTask.getSubTasks().add(sampleSubTask);

    Mockito.when(taskRepository.findById(sampleTask.getId()))
        .thenReturn(Optional.of(sampleTask));

    Mockito.when(taskRepository.save(sampleTask))
        .thenReturn(sampleTask);


    // when, then
    Assertions.assertThrows(IllegalStateException.class, () -> taskService.close(sampleTaskId));

  }

  @Test
  public void testClose_fail_alreadyClosedTask() {
    // given
    final long sampleTaskId = -1;
    Task sampleTask = TestUtils.newTask(sampleTaskId, true, "This is closed task.");

    Mockito.when(taskRepository.findById(sampleTask.getId()))
        .thenReturn(Optional.of(sampleTask));

    Mockito.when(taskRepository.save(sampleTask))
        .thenReturn(sampleTask);

    // when, then
    Assertions.assertThrows(IllegalStateException.class, () -> taskService.close(sampleTaskId));
  }

  @Test
  public void testOpen() {
    // given
    final long sampleTaskId = -1;
    Task sampleTask = TestUtils.newTask(sampleTaskId, true, "This is closed task.");

    Mockito.when(taskRepository.findById(sampleTask.getId()))
        .thenReturn(Optional.of(sampleTask));

    Mockito.when(taskRepository.save(sampleTask))
        .thenReturn(sampleTask);

    // when
    taskService.open(sampleTaskId);
  }

  @Test
  public void testOpen_fail_alreadyOpen() {
    // given
    final long sampleTaskId = -1;
    Task sampleTask = TestUtils.newTask(sampleTaskId, false, "This is open task.");

    Mockito.when(taskRepository.findById(sampleTask.getId()))
        .thenReturn(Optional.of(sampleTask));

    Mockito.when(taskRepository.save(sampleTask))
        .thenReturn(sampleTask);

    // when, then
    Assertions.assertThrows(IllegalStateException.class, () -> taskService.open(sampleTaskId));

  }


}