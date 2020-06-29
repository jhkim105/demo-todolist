package com.example.todolist.api.controller;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;

import com.example.todolist.TestUtils;
import com.example.todolist.WebMvcTestExclude;
import com.example.todolist.api.service.TaskService;
import com.example.todolist.core.model.Task;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = TaskController.class)
@ComponentScan(excludeFilters = @ComponentScan.Filter(WebMvcTestExclude.class))
@Slf4j
public class TaskControllerTest {

  @Autowired
  private WebTestClient webClient;

  @MockBean
  private TaskService taskService;

  @Test
  public void testList() throws Exception {
    // given
    Task task1= TestUtils.newTask(1L, false, "task1");
    Task task2= TestUtils.newTask(2L, false, "task2");

    List<Task> taskList = Arrays.asList(task1, task2);

    Page<Task> page = new PageImpl(taskList, PageRequest.of(0, 10), 2);
    given(taskService.findAll(PageRequest.of(0, 10), new SearchVO())).willReturn(page);

    //when
    WebTestClient.ResponseSpec responseSpec = webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/tasks")
            .queryParam("page", "0")
            .queryParam("size", "10")
            .build())
        .accept(MediaType.APPLICATION_JSON)
        .attribute("page", "0").attribute("size", "10").exchange();

    // then
    responseSpec.expectBody()
        .jsonPath("$.content", hasSize(2)).hasJsonPath()
        .jsonPath("$.totalElements").isEqualTo(2)
        .jsonPath("$.content[0].createdAt").isEqualTo(getFormattedDate(task1.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"))
        .jsonPath("$.content[0].updatedAt").isEqualTo(getFormattedDate(task1.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"))
        .jsonPath("$.content[0].description").isEqualTo(task1.getDescription());

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
    WebTestClient.ResponseSpec responseSpec =
        webClient.get().uri(String.format("/tasks/%s", task.getId()))
            .accept(MediaType.APPLICATION_JSON)
            .attribute("page", "0")
            .attribute("size", "10").exchange();

    // then
    responseSpec
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.description").isEqualTo(taskVO.getDescription())
        .jsonPath("$.superTaskIdsLabel").isEqualTo(taskVO.getSuperTaskIdsLabel());
  }
//
//  @Test
//  public void testCreate() throws Exception {
//    // given
//    Task task = TestUtils.newTask(null,false, "task");
//    Task returnTask = TestUtils.newTask(1L, false, "task");
//    given(taskService.save(task)).willReturn(returnTask);
//
//    TaskCreateRequestVO taskCreateRequestVO = new TaskCreateRequestVO();
//    taskCreateRequestVO.setDescription(task.getDescription());
//
//    String body = TestUtils.toJsonString(taskCreateRequestVO);
//
//    // when
//    ResultActions resultActions = mockMvc.perform(post("/tasks")
//        .contentType(MediaType.APPLICATION_JSON_UTF8)
//        .accept(MediaType.APPLICATION_JSON_UTF8)
//        .content(body))
//        .andDo(print());
//
//    // then
//    resultActions.andExpect(jsonPath("$.description", is(task.getDescription())));
//
//  }
//
//  @Test
//  public void testUpdate() throws Exception {
//    // given
//    Task task = TestUtils.newTask(1L, false, "task");
//    given(taskService.save(task)).willReturn(task);
//    given(taskService.findOne(task.getId())).willReturn(task);
//
//    TaskUpdateRequestVO taskUpdateRequestVO = new TaskUpdateRequestVO();
//    taskUpdateRequestVO.setId(task.getId());
//    taskUpdateRequestVO.setDescription(task.getDescription());
//
//    String body = TestUtils.toJsonString(taskUpdateRequestVO);
//
//    // when
//    ResultActions resultActions = mockMvc.perform(put("/tasks")
//        .contentType(MediaType.APPLICATION_JSON_UTF8)
//        .accept(MediaType.APPLICATION_JSON_UTF8)
//        .content(body))
//        .andDo(print());
//
//    // then
//    resultActions.andExpect(jsonPath("$.description", is(task.getDescription())));
//  }
//
//  @Test
//  public void testClose() throws Exception {
//    // given
//    Task task = TestUtils.newTask(1L, true, "task");
//    given(taskService.close(task.getId())).willReturn(task);
//
//    // when
//    ResultActions resultActions = mockMvc.perform(post(String.format("/tasks/%d/close", task.getId()))
//        .contentType(MediaType.APPLICATION_JSON_UTF8)
//        .accept(MediaType.APPLICATION_JSON_UTF8))
//        .andDo(print());
//
//    // then
//    resultActions.andExpect(status().isOk());
//  }
//
//  @Test
//  public void testOpen() throws Exception {
//    // given
//    Task task = TestUtils.newTask(1L, false, "task");
//    given(taskService.open(task.getId())).willReturn(task);
//
//    // when
//    ResultActions resultActions = mockMvc.perform(post(String.format("/tasks/%d/open", task.getId()))
//        .contentType(MediaType.APPLICATION_JSON_UTF8)
//        .accept(MediaType.APPLICATION_JSON_UTF8))
//        .andDo(print());
//
//    // then
//    resultActions.andExpect(status().isOk());
//  }
//

}
