package com.example.todolist.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.*;
import org.springframework.util.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.*;
import java.util.*;

@Entity
@Table(name = "tu_task")
@Data
@EqualsAndHashCode(callSuper = false, of = {"id"})
@NoArgsConstructor
public class Task implements Serializable {

  private static final long serialVersionUID = -1249432540792537720L;

  @Id
  @GeneratedValue
  private Long id;

  private String description;

  @Column(nullable = false)
  @ColumnDefault("0")
  private boolean finished;

  @Column(name = "created_at")
  private Date createdAt;

  @Column(name = "updated_at")
  private Date updatedAt;

  @Transient
  private List<Long> superTaskIds;


  @JoinTable(name = "tu_related_task",
      joinColumns = {@JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)},
      inverseJoinColumns = {@JoinColumn(name = "super_task_id", referencedColumnName = "id", nullable = false)})
  @ManyToMany
  private Set<Task> superTasks = new HashSet<>();

  @ManyToMany(mappedBy = "superTasks")
  @JsonIgnore
  private Set<Task> subTasks = new HashSet<>();

  public void processFinish() {
    this.finished = true;
  }

  @Transient
  public boolean isNotFinished() {
    return !this.isFinished();
  }

  @Transient
  public boolean existsUnfinishedSubTasks() {
    if (CollectionUtils.isEmpty(subTasks))
      return false;

    return subTasks.stream().filter(Task::isNotFinished).count() > 0l;
  }
}
