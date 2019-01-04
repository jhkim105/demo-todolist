package com.example.todolist.controller;

import com.example.todolist.model.Task;
import com.example.todolist.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "tasks")
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

  private final TaskService service;

  @GetMapping
  @ApiOperation("getList")
  public List<TaskVO> findPaginated(@RequestParam("page") int page, @RequestParam("size") int size) {
    Page<Task> resultPage = service.findPaginated(page, size);
    if (page > resultPage.getTotalPages()) {
      throw new RuntimeException(String.format("requested page[%s] is bigger than totalPages[%s]",
          page, resultPage.getTotalPages()));
    }
    if (CollectionUtils.isEmpty(resultPage.getContent())) {
      return null;
    }

    return TaskVO.of(resultPage.getContent());
  }

  @PostMapping
  public Task create(@RequestBody TaskCreateRequestVO requestVO) {
    return service.save(requestVO.toTask());
  }

  @PutMapping
  public Task update(@RequestBody TaskUpdateRequestVO requestVO) {
    Task task = service.findOne(requestVO.getId());
    task = requestVO.fillTask(task);
    return service.save(task);
  }

  @PostMapping(path="/{id}/close")
  public Task close(@PathVariable Long id) {
    return service.close(id);
  }

  @PostMapping(path="/{id}/open")
  public Task open(@PathVariable Long id) {
    return service.open(id);
  }
}
