package com.example.todolist.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "tu_todo_parent")
@Data
@EqualsAndHashCode(callSuper = false, of = {"id"})
@NoArgsConstructor
public class RelatedTodo implements Serializable  {

  private static final long serialVersionUID = -4183416231680889616L;

  @EmbeddedId
  private Id id = new Id();

  @ManyToOne
  @JoinColumn(name = "todo_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Todo todo;

  @ManyToOne
  @JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Todo parentTodo;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode(of = {"todoId", "relatedTodoId"})
  @ToString
  @Embeddable
  public static class Id implements Serializable {

    private static final long serialVersionUID = 5723885340777039623L;

    @Column(name = "todo_id")
    private String todoId;

    @Column(name = "parent_id")
    private String parentId;

  }
}
