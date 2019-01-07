package com.example.todolist.api.controller;

import com.example.todolist.core.model.Task;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;

@Data
public class TaskUpdateRequestVO implements Serializable {

  private static final long serialVersionUID = -4085771386684174993L;

  private Long id;

  private String description;

  private String superTaskIdsLabel;

  public Task fillTask(Task task) {
    task.setDescription(this.description);
    if (StringUtils.isNotBlank(superTaskIdsLabel)) {
      task.setSuperTaskIds(Arrays.asList(StringUtils.split(superTaskIdsLabel, ",")));
    }

    return task;
  }
}
