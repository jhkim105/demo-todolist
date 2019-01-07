package com.example.todolist;

import com.example.todolist.core.model.Task;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;

@Slf4j
public final class JsonTest {

  @Test
  public void convertToString() {
    Task task = TestUtils.newTask(1L, false,"API 구현");
    task.setSuperTaskIds(Arrays.asList("@1", "@2"));
    log.debug(TestUtils.toJsonString(task));
  }

}
