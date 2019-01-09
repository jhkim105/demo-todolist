package com.example.todolist.api.controller;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.thymeleaf.util.ArrayUtils;

import java.io.Serializable;

@Data
public class PageRequestVO implements Serializable {

  private static final long serialVersionUID = -5222754347473542809L;

  private int page;

  private int size;

  private String[] orders;

  private Sort.Direction direction;

  public PageRequest toPageRequet() {
    PageRequest pageRequest;
    if (ArrayUtils.isEmpty(orders)) {
      pageRequest = PageRequest.of(this.page, this.size);
    } else {
      if (direction == null) {
        direction = Sort.Direction.DESC;
      }
      pageRequest = PageRequest.of(this.page, this.size, this.direction, this.orders);
    }

    return pageRequest;
  }
}
