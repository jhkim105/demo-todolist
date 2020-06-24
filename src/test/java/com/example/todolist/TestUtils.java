package com.example.todolist;

import com.example.todolist.core.model.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtils {

  public static String toJsonString(Object obj) {
    ObjectMapper mapper = new ObjectMapper();
    String result;
    try {
      result = mapper.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(String.format("convert to json string error..error:%s, object:%s",
          e.toString(), obj.toString()), e);
    }
    return result;
  }

  public static Task newTask(Long id, boolean closed, String description) {
    Task task = new Task();
    task.setDescription(description);
    task.setId(id);
    task.setClosed(closed);
    return task;
  }
}
