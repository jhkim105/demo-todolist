package com.example.todolist.repository;

import com.example.todolist.model.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.jdbc.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.test.context.junit4.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // TODO: embedded db 사용시 제거
public class TaskRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private TaskRepository taskRepository;

  @Test
  public void findById() {
    // given
    Task task = new Task();
    task.setDescription("test description");
    task.setCreatedAt(new Date());
    task = entityManager.persist(task);
    entityManager.flush();

    // when
    Optional<Task> taskOptional = taskRepository.findById(task.getId());

    // then
    assertThat(taskOptional.get().getDescription())
        .isEqualTo(task.getDescription());
  }

  @Test(expected = NoSuchElementException.class)
  public void findById_notFound() {
    // when
    Optional<Task> taskOptional = taskRepository.findById(1212l);
    taskOptional.get();

  }

}