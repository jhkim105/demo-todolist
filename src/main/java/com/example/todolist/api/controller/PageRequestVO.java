package com.example.todolist.api.controller;

import com.example.todolist.core.model.Task;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

@Data
public class PageRequestVO implements Serializable {

  private static final long serialVersionUID = -5222754347473542809L;

  private int page;

  private int size;

  private Task.Order order;

  private Sort.Direction direction;

  public PageRequest toPageRequet() {
    PageRequest pageRequest;
    if (order == null) {
      pageRequest = PageRequest.of(this.page, this.size);
    } else {
      if (direction == null) {
        direction = Sort.Direction.DESC;
      }
      pageRequest = PageRequest.of(this.page, this.size, this.direction, order.prop);
    }

    return pageRequest;
  }

//  public enum Order {
//    ID("id"), CREATED_AT("created_at");
//    String prop;
//    Order(String prop) {
//      this.prop = prop;
//    }
//  }
}
