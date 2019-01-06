package com.example.todolist.core.repository;

import com.example.todolist.core.model.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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