package com.example.todolist.service;

import com.example.todolist.model.Task;
import com.example.todolist.repository.TaskRepository;
import org.junit.Before;
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

  @Before
  public void setUp() {
    Task task = new Task();
    task.setDescription("Super Task");
    task.setId(111l);
    task.setClosed(false);
    Task subTask1 = new Task();
    subTask1.setDescription("Sub Task1");
    subTask1.setClosed(true);
   task.getSubTasks().add(subTask1);

    Mockito.when(taskRepository.findById(task.getId()))
        .thenReturn(Optional.of(task));

    Mockito.when(taskRepository.save(task))
        .thenReturn(task);
  }

  @Test
  public void finish() throws Exception {
    Task task = taskService.close(111L);

    assertThat(task.isClosed())
        .isTrue();
  }

}