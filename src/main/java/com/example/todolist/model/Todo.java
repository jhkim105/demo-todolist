package com.example.todolist.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tu_todo")
@Data
@EqualsAndHashCode(callSuper = false, of = {"id"})
@NoArgsConstructor
public class Todo implements Serializable {

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

  @OneToMany(mappedBy = "parentTodo", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private Set<RelatedTodo> childTodos = new HashSet<>();

}
