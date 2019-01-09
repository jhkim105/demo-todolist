package com.example.todolist.api.controller;

import com.example.todolist.TestUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void testList() throws Exception {
    // when
    ResultActions resultActions = mockMvc.perform(get("/tasks")
        .param("page", "0")
        .param("size", "10")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print());

    // then
    resultActions.andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(4)))
        .andExpect(jsonPath("$.totalElements", is(4)));
  }

  @Test
  public void testList_검색어_청_으로_검색() throws Exception {
    // when
    ResultActions resultActions = mockMvc.perform(get("/tasks")
        .param("page", "0")
        .param("size", "10")
        .param("q", "청")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print());

    // then
    resultActions.andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(2)))
        .andExpect(jsonPath("$.totalElements", is(2)));
  }


  @Test
  public void testGet() throws Exception {
    Long taskId = 1L;
    // when
    ResultActions resultActions = mockMvc.perform(get(String.format("/tasks/%d", taskId))
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print());

    // then
    resultActions.andExpect(status().isOk())
        .andExpect(jsonPath("$.description", is("집안일")))
        .andExpect(jsonPath("$.superTaskIdsLabel", is("")));
  }

  @Test
  public void testCreate() throws Exception {
    // when
    TaskCreateRequestVO taskCreateRequestVO = new TaskCreateRequestVO();
    taskCreateRequestVO.setDescription("TEST TASK");
    String body = TestUtils.toJsonString(taskCreateRequestVO);

    ResultActions resultActions = mockMvc.perform(post("/tasks")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .content(body))
        .andDo(print());

    // then
    resultActions.andExpect(jsonPath("$.description", Matchers.is(taskCreateRequestVO.getDescription())));

  }

  @Test
  public void testUpdate() throws Exception {
    // when
    TaskUpdateRequestVO taskUpdateRequestVO = new TaskUpdateRequestVO();
    taskUpdateRequestVO.setId(1L);
    taskUpdateRequestVO.setDescription("UPDATED TASK");

    String body = TestUtils.toJsonString(taskUpdateRequestVO);
    ResultActions resultActions = mockMvc.perform(put("/tasks")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .content(body))
        .andDo(print());

    // then
    resultActions.andExpect(jsonPath("$.description", Matchers.is(taskUpdateRequestVO.getDescription())));
  }


  @Test
  public void testUpdate_fail_자기자신을_상위일감으로_할수없다() throws Exception {
    // when
    TaskUpdateRequestVO taskUpdateRequestVO = new TaskUpdateRequestVO();
    taskUpdateRequestVO.setId(1L);
    taskUpdateRequestVO.setDescription("UPDATED TASK");
    taskUpdateRequestVO.setSuperTaskIdsLabel("@1");

    String body = TestUtils.toJsonString(taskUpdateRequestVO);
    ResultActions resultActions = mockMvc.perform(put("/tasks")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .content(body))
        .andDo(print());

    // then
    resultActions.andExpect(status().isBadRequest());
  }

  @Test
  public void testUpdate_fail_하위일감을_상위일감으로_할수없다() throws Exception {
    // when
    TaskUpdateRequestVO taskUpdateRequestVO = new TaskUpdateRequestVO();
    taskUpdateRequestVO.setId(1L);
    taskUpdateRequestVO.setDescription("UPDATED TASK");
    taskUpdateRequestVO.setSuperTaskIdsLabel("@2");

    String body = TestUtils.toJsonString(taskUpdateRequestVO);
    ResultActions resultActions = mockMvc.perform(put("/tasks")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .content(body))
        .andDo(print());

    // then
    resultActions.andExpect(status().isBadRequest());
  }

  @Test
  public void testClose() throws Exception {
    // when
    Long taskId = 2L;
    ResultActions resultActions = mockMvc.perform(post(String.format("/tasks/%d/close", taskId))
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaType.APPLICATION_JSON_UTF8))
        .andDo(print());

    // then
    resultActions.andExpect(status().isOk());
  }

  @Test
  public void testClose_fail_하위일감이_모두종료되어야_종료할수있다() throws Exception {
    // when
    Long taskId = 1L;
    ResultActions resultActions = mockMvc.perform(post(String.format("/tasks/%d/close", taskId))
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaType.APPLICATION_JSON_UTF8))
        .andDo(print());

    // then
    resultActions.andExpect(status().isConflict());
  }

  @Test
  public void testOpen_fail() throws Exception {
    Long taskId = 2L;
    // when
    ResultActions resultActions = mockMvc.perform(post(String.format("/tasks/%d/open", taskId))
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaType.APPLICATION_JSON_UTF8))
        .andDo(print());

    // then
    resultActions.andExpect(status().isConflict());
  }

}