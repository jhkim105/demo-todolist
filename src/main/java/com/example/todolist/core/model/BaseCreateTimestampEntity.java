package com.example.todolist.core.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass()
@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseCreateTimestampEntity<K extends Serializable> extends BaseEntity<K> {

  private static final long serialVersionUID = -8801727337312553249L;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    protected Date createdAt;

}
