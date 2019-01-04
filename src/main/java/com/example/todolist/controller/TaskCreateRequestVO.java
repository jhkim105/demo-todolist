package com.example.todolist.controller;

import com.example.todolist.model.Task;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TaskCreateRequestVO implements Serializable {

  private static final long serialVersionUID = -7528052965360517601L;

  private String description;

  private List<String> superTaskIds;

  public Task toTask() {
    Task task = new Task();
    task.setDescription(description);
    task.setSuperTaskIds(this.superTaskIds);
    return task;
  }
}
