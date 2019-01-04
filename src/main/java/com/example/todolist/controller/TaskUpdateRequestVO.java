package com.example.todolist.controller;

import com.example.todolist.model.Task;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class TaskUpdateRequestVO implements Serializable {

  private static final long serialVersionUID = -4085771386684174993L;

  private Long id;

  private String description;

  private List<String> superTaskIds;

  public Task fillTask(Task task) {
    task.setDescription(this.description);
    task.setSuperTaskIds(this.superTaskIds);
    task.setUpdatedAt(new Date());
    return task;
  }
}
