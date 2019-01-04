package com.example.todolist.controller;

import com.example.todolist.model.*;
import com.example.todolist.service.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.web.servlet.*;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TaskService taskService;

  @Test
  public void findPaginated() throws Exception {
    Task task1= new Task();
    task1.setDescription("task1");
    Task task2= new Task();
    task1.setDescription("task2");

    List<Task> taskList = Arrays.asList(task1, task2);

    Page<Task> page = new PageImpl(taskList, PageRequest.of(0, 2), 2);
    given(taskService.findPaginated(0, 2)).willReturn(page);

    mockMvc.perform(get("/tasks")
        .param("page", "0")
        .param("size", "2")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].description", is(task1.getDescription())));
  }
}