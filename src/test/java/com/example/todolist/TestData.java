package com.example.todolist;

import com.example.todolist.model.*;
import lombok.*;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestData {

 public static Task newTask(String description) {
   Task task = new Task();
   task.setDescription(description);
   task.setCreatedAt(new Date());
   return task;
 }
}
