package com.example.todolist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tu_task")
@Data
@EqualsAndHashCode(callSuper = false, of = {"id"})
@ToString(exclude = {"subTasks"})
@NoArgsConstructor
public class Task implements Serializable {

  private static final long serialVersionUID = -1249432540792537720L;

  public static final String SUPER_TASK_PREFIX = "@";

  @Id
  @GeneratedValue
  private Long id;

  private String description;

  @Column(nullable = false)
  @ColumnDefault("0")
  private boolean closed;

  @Column(name = "created_at")
  private Date createdAt;

  @Column(name = "updated_at")
  private Date updatedAt;

  @Transient
  private List<String> superTaskIds;


  @JoinTable(name = "tu_related_task",
      joinColumns = {@JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)},
      inverseJoinColumns = {@JoinColumn(name = "super_task_id", referencedColumnName = "id", nullable = false)})
  @ManyToMany
  @JsonIgnore
  private Set<Task> superTasks = new HashSet<>();

  @ManyToMany(mappedBy = "superTasks")
  @JsonIgnore
  @ApiModelProperty(hidden = true)
  private Set<Task> subTasks = new HashSet<>();

  public void close() {
    this.updatedAt = new Date();
    this.closed = true;
  }

  public void open() {
    this.updatedAt = new Date();
    this.closed = false;
  }

  @Transient
  public boolean isOpened() {
    return !this.isClosed();
  }

  @Transient
  public boolean existsOpenedSubTasks() {
    if (CollectionUtils.isEmpty(subTasks))
      return false;

    return subTasks.stream().filter(Task::isOpened).count() > 0l;
  }
}
