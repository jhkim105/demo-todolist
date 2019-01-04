package com.example.todolist.service;

import com.example.todolist.model.*;
import com.example.todolist.repository.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.context.annotation.*;
import org.springframework.test.context.junit4.*;

import java.util.*;

import static org.assertj.core.api.Java6Assertions.*;

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
    task.setFinished(false);
    Task subTask1 = new Task();
    subTask1.setDescription("Sub Task1");
    subTask1.setFinished(true);
   task.getSubTasks().add(subTask1);

    Mockito.when(taskRepository.findById(task.getId()))
        .thenReturn(Optional.of(task));

    Mockito.when(taskRepository.save(task))
        .thenReturn(task);
  }

  @Test
  public void finish() throws Exception {
    Task task = taskService.finish(111L);

    assertThat(task.isFinished())
        .isTrue();
  }

}