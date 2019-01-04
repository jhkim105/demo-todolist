package com.example.todolist;

import com.example.todolist.model.*;
import com.fasterxml.jackson.databind.*;
import lombok.extern.slf4j.*;
import org.junit.*;

import java.util.*;

@Slf4j
public final class JsonTest {

  @Test
  public void convertToString() {
    Task task = TestData.newTask("API 구현");
    task.setSuperTaskIds(Arrays.asList(-1L, -2L));
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
