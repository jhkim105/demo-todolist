package com.example.todolist.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tu_task")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"id"})
@ToString(exclude = {"superTasks", "subTasks"})
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Task extends BaseTraceableEntity<Long> {

  private static final long serialVersionUID = -1249432540792537720L;

  public static final String SUPER_TASK_PREFIX = "@";

  @Id
  @GeneratedValue
  private Long id;

  private String description;

  @Column(nullable = false)
  @ColumnDefault("0")
  private boolean closed;

  @Transient
  private List<String> superTaskIds;

  @JoinTable(name = "tu_related_task",
      joinColumns = {@JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)},
      inverseJoinColumns = {@JoinColumn(name = "super_task_id", referencedColumnName = "id", nullable = false)})
  @ManyToMany
  private Set<Task> superTasks = new HashSet<>();

  @ManyToMany(mappedBy = "superTasks")
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
    if (CollectionUtils.isEmpty(this.subTasks))
      return false;

    for (Task subTask : this.subTasks) {
      if (subTask.isOpened()) {
        return true;
      }
    }
    return false;
  }

  @Transient
  public String getRefId() {
    return SUPER_TASK_PREFIX + this.id;
  }
}
