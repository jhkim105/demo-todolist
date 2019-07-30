package com.example.todolist.api.service;

import com.example.todolist.TestUtils;
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
  static class TestContextConfiguration {

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

  @Test(expected = IllegalStateException.class)
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

    // when
    Task task = taskService.close(sampleTaskId);

    // then
    assertThat(task.isClosed())
        .isFalse();
  }

  @Test(expected = IllegalStateException.class)
  public void testClose_fail_alreadyClosedTask() {
    // given
    final long sampleTaskId = -1;
    Task sampleTask = TestUtils.newTask(sampleTaskId, true, "This is closed task.");

    Mockito.when(taskRepository.findById(sampleTask.getId()))
        .thenReturn(Optional.of(sampleTask));

    Mockito.when(taskRepository.save(sampleTask))
        .thenReturn(sampleTask);

    // when
    taskService.close(sampleTaskId);
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

  @Test(expected = IllegalStateException.class)
  public void testOpen_fail_alreadyOpen() {
    // given
    final long sampleTaskId = -1;
    Task sampleTask = TestUtils.newTask(sampleTaskId, false, "This is open task.");

    Mockito.when(taskRepository.findById(sampleTask.getId()))
        .thenReturn(Optional.of(sampleTask));

    Mockito.when(taskRepository.save(sampleTask))
        .thenReturn(sampleTask);

    // when
    taskService.open(sampleTaskId);
  }


}