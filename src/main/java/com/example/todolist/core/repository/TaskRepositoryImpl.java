package com.example.todolist.core.repository;

import com.example.todolist.core.model.QTask;
import com.example.todolist.core.model.Task;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepositoryCustom {

  @Autowired
  private JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<Task> findAll(Pageable pageable, String q, boolean open) {
    QTask task = QTask.task;
    JPAQuery<Task> query = jpaQueryFactory.selectFrom(task);

    if (StringUtils.isNotBlank(q)) {
      query.where(task.description.contains(q));
    }

    if (open) {
      query.where(task.closed.isFalse());
    }

    setOrderBy(pageable, query, task);

    query.limit(pageable.getPageSize()).offset(pageable.getOffset());

    QueryResults<Task> results = query.fetchResults();
    Page<Task> page = new PageImpl<>(results.getResults(), pageable, results.getTotal());

    return page;
  }

  private void setOrderBy(Pageable pageable, JPAQuery<Task> query, QTask task) {
    Sort sort = pageable.getSort();
    Sort.Order order = sort.getOrderFor(Task.Order.ID.prop);
    if (order != null) {
      if (order.getDirection() == Sort.Direction.DESC) {
        query.orderBy(task.id.desc());
      } else {
        query.orderBy(task.id.asc());
      }
    }

    order = sort.getOrderFor(Task.Order.UPDATED_AT.prop);
    if (order != null) {
      if (order.getDirection() == Sort.Direction.DESC) {
        query.orderBy(task.updatedAt.desc());
      } else {
        query.orderBy(task.updatedAt.asc());
      }
    }
  }
}
