package com.example.todolist;

import com.example.todolist.core.model.Task;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestData {

 public static Task newTask(String description) {
   Task task = new Task();
   task.setDescription(description);
   task.setCreatedAt(new Date());
   return task;
 }
}
