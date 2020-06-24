package com.example.todolist.core.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass()
@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimestampEntity<K extends Serializable> extends BaseCreateTimestampEntity<K> {

  private static final long serialVersionUID = -1776653546324533189L;

  @Column(name = "updated_at")
  @LastModifiedDate
  protected Date updatedAt;

}
