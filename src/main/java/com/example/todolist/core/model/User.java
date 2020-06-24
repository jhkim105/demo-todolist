package com.example.todolist.core.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tu_user")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false, of = {"id"})
public class User extends BaseTraceableEntity<String> {

  @Id
  @Column(length = BaseEntity.ID_LENGTH)
  @GenericGenerator(name = "system-uuid", strategy = "uuid2")
  @GeneratedValue(generator = "system-uuid")
  private String id;

  private String username;

  private String password;

  @ElementCollection(targetClass = Authority.class)
  @JoinTable(name = "tu_user_authorities", joinColumns = {@JoinColumn(name = "user_id")})
  @Column(name = "authority", nullable = false, length = 50)
  @Enumerated(EnumType.STRING)
  private Set<Authority> authorities = new HashSet<>();

}
