package com.example.todolist.core.model;


import static com.example.todolist.core.model.BaseEntity.ID_LENGTH;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass()
@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTraceableEntity<K extends Serializable> extends BaseCreateTraceableEntity<K> {

  private static final long serialVersionUID = 2379933612991151510L;

    @Column(name = "updated_at")
    @LastModifiedDate
    protected Date updatedAt;

    @Column(name = "updated_by",  length = ID_LENGTH)
    @LastModifiedBy
    protected String updatedBy;

}