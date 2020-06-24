package com.example.todolist.core.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.todolist.core.model.Task;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private TaskRepository taskRepository;

  @Test
  public void testFindById() {
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

  @Test
  public void testFindById_notFound() {
    Assertions.assertThrows(NoSuchElementException.class, () -> {
      Optional<Task> taskOptional = taskRepository.findById(1212l);
      taskOptional.get();
    });
  }

  @Test
  public void testFindAllByDescriptionLike() {
    Page<Task> page = taskRepository.findAllByDescriptionLike(PageRequest.of(2, 2), "집안%");
    assertThat(page.getTotalElements()).isEqualTo(1);
  }

  @Test
  public void testFindAll() {
    Page<Task> page = taskRepository.findAll(PageRequest.of(0, 10), "", false);
    assertThat(page.getContent().size()).isEqualTo(4);
    assertThat(page.getTotalElements()).isEqualTo(4);
  }

  @Test
  public void testFindAll_검색어_집_완료되지않은일감만_2페이지_조회() {
    Page<Task> page = taskRepository.findAll(PageRequest.of(1, 2), "청", true);
    assertThat(page.getContent().size()).isEqualTo(0);
    assertThat(page.getTotalElements()).isEqualTo(2);
  }

}