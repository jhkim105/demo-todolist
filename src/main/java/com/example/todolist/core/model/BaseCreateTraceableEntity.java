package com.example.todolist.core.model;

import static com.example.todolist.core.model.BaseEntity.ID_LENGTH;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass()
@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseCreateTraceableEntity<K extends Serializable> extends BaseCreateTimestampEntity<K> {

  private static final long serialVersionUID = -6279828398703077543L;

  @Column(name = "created_by", updatable = false, length = ID_LENGTH)
  @CreatedBy
  protected String createdBy;

}
