package com.example.todolist.controller;

import com.example.todolist.model.Task;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

@Data
public class TaskCreateRequestVO implements Serializable {

  private static final long serialVersionUID = -7528052965360517601L;

  private String id;

  private String description;

  private String superTaskIdsLabel;

  public Task toTask() {
    Task task = new Task();
    task.setDescription(description);
    if (StringUtils.isBlank(superTaskIdsLabel)) {
      task.setSuperTaskIds(Arrays.asList(StringUtils.split(superTaskIdsLabel, ",")));
    }

    Date now = new Date();
    task.setCreatedAt(now);
    task.setUpdatedAt(now);

    return task;
  }
}
