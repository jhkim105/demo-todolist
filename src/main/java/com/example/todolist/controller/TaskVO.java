package com.example.todolist.controller;

import com.example.todolist.model.Task;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TaskVO implements Serializable{

  private static final long serialVersionUID = 6588242887357638373L;

  private Long id;

  private String description;

  private boolean closed;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
  private Date createdAt;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
  private Date updatedAt;

  private List<String> superTaskIds;

  @JsonProperty
  public String getSuperTaskIdsLabel() {
    if (CollectionUtils.isEmpty(superTaskIds)) {
      return "";
    }
    return StringUtils.join(this.superTaskIds, ",");
  }

  public void setSuperTaskIdsLabel(String superTaskIdsLabel) {
    if (StringUtils.isBlank(superTaskIdsLabel)) {
      return;
    }

    this.superTaskIds = Arrays.asList(StringUtils.split(superTaskIdsLabel, ","));
  }

  public static TaskVO of(Task task) {
    TaskVO vo = new TaskVO();
    BeanUtils.copyProperties(task, vo);

    if (!CollectionUtils.isEmpty(task.getSuperTasks())) {
      vo.setSuperTaskIds(task.getSuperTasks()
          .stream()
          .map(superTask -> Task.SUPER_TASK_PREFIX + superTask.getId())
          .collect(Collectors.toList()));
    }

    return vo;
  }

  public static List<TaskVO> of(List<Task> tasks) {
    if (CollectionUtils.isEmpty(tasks)) {
      return null;
    }

    return tasks.stream().map(task -> TaskVO.of(task)).collect(Collectors.toList());
  }

}
