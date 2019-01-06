package com.example.todolist;

import com.example.todolist.core.model.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;

@Slf4j
public final class JsonTest {

  @Test
  public void convertToString() {
    Task task = TestData.newTask("API 구현");
    task.setSuperTaskIds(Arrays.asList("@1", "@2"));
    log.debug(toString(task));
  }

  private String toString(Object obj) {
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



}
