package com.example.todolist.api.controller;

import com.example.todolist.TestUtils;
import com.example.todolist.api.service.TaskService;
import com.example.todolist.core.model.Task;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
@Slf4j
public class TaskControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TaskService taskService;

  @Test
  public void testList() throws Exception {
    // given
    Task task1= TestUtils.newTask(1L, false, "task1");
    Task task2= TestUtils.newTask(2L, false, "task2");

    List<Task> taskList = Arrays.asList(task1, task2);

    Page<Task> page = new PageImpl(taskList, PageRequest.of(0, 10), 2);
    given(taskService.findPaginated(0, 10)).willReturn(page);

    // when
    ResultActions resultActions = mockMvc.perform(get("/tasks")
        .param("page", "0")
        .param("size", "10")
        .contentType(MediaType.APPLICATION_JSON));
    resultActions.andDo(print());

    // then
    resultActions.andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(2)))
        .andExpect(jsonPath("$.totalElements", CoreMatchers.is(2)))
        .andExpect(jsonPath("$.content[0].description", is(task1.getDescription())))
        .andExpect(jsonPath("$.content[0].createdAt", is(getFormattedDate(task1.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"))))
        .andExpect(jsonPath("$.content[0].updatedAt", is(getFormattedDate(task1.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"))))
        .andExpect(jsonPath("$.content[0].description", is(task1.getDescription())))

        .andExpect(jsonPath("$.content[1].description", is(task2.getDescription())));
  }

  private String getFormattedDate(Date date, String pattern) {
    ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.of("Asia/Seoul"));
    return zonedDateTime.format(DateTimeFormatter.ofPattern(pattern));
  }

  @Test
  public void testGet() throws Exception {
    // given
    Task task = TestUtils.newTask(1L, false, "task1");
    Task superTask = TestUtils.newTask(2L, false, "super task");
    task.getSuperTasks().add(superTask);
    TaskVO taskVO = TaskVO.of(task);
    given(taskService.findOne(task.getId())).willReturn(task);

    // when
    ResultActions resultActions = mockMvc.perform(get(String.format("/tasks/%s", task.getId()))
        .contentType(MediaType.APPLICATION_JSON));
    resultActions.andDo(print());

    // then
    resultActions.andExpect(status().isOk())
        .andExpect(jsonPath("$.description", is(taskVO.getDescription())))
        .andExpect(jsonPath("$.superTaskIdsLabel", is(taskVO.getSuperTaskIdsLabel())));
  }

  @Test
  public void testCreate() throws Exception {
    // given
    Task task = TestUtils.newTask(null,false, "task");
    Task returnTask = TestUtils.newTask(1L, false, "task");
    given(taskService.save(task)).willReturn(returnTask);

    TaskCreateRequestVO taskCreateRequestVO = new TaskCreateRequestVO();
    taskCreateRequestVO.setDescription(task.getDescription());

    String body = TestUtils.toJsonString(taskCreateRequestVO);

    // when
    ResultActions resultActions = mockMvc.perform(post("/tasks")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .content(body));
    resultActions.andDo(print());

    // then
    resultActions.andExpect(jsonPath("$.description", is(task.getDescription())));

  }

  @Test
  public void testUpdate() throws Exception {
    // given
    Task task = TestUtils.newTask(1L, false, "task");
    given(taskService.save(task)).willReturn(task);
    given(taskService.findOne(task.getId())).willReturn(task);

    TaskUpdateRequestVO taskUpdateRequestVO = new TaskUpdateRequestVO();
    taskUpdateRequestVO.setId(task.getId());
    taskUpdateRequestVO.setDescription(task.getDescription());

    String body = TestUtils.toJsonString(taskUpdateRequestVO);

    // when
    ResultActions resultActions = mockMvc.perform(put("/tasks")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .content(body));
    resultActions.andDo(print());

    // then
    resultActions.andExpect(jsonPath("$.description", is(task.getDescription())));
  }

  @Test
  public void testClose() throws Exception {
    // given
    Task task = TestUtils.newTask(1L, true, "task");
    given(taskService.close(task.getId())).willReturn(task);

    // when
    ResultActions resultActions = mockMvc.perform(post(String.format("/tasks/%s/close", task.getId()))
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaType.APPLICATION_JSON_UTF8));
    resultActions.andDo(print());

    // then
    resultActions.andExpect(status().isOk());
  }

  @Test
  public void testOpen() throws Exception {
    // given
    Task task = TestUtils.newTask(1L, false, "task");
    given(taskService.open(task.getId())).willReturn(task);

    // when
    ResultActions resultActions = mockMvc.perform(post(String.format("/tasks/%s/open", task.getId()))
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaType.APPLICATION_JSON_UTF8));
    resultActions.andDo(print());

    // then
    resultActions.andExpect(status().isOk());
  }


}