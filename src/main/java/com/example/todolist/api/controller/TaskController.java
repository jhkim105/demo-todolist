package com.example.todolist.api.controller;

import com.example.todolist.api.service.TaskService;
import com.example.todolist.core.model.Task;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@Api(tags = "tasks")
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

  private final TaskService service;

  @GetMapping
  @ApiOperation("list")
  public Page<TaskVO> list(PageRequestVO pageRequestVO) {
    Page<Task> taskPage = service.findAll(pageRequestVO.toPageRequet());

    Page<TaskVO> resultPage = new PageImpl<>(TaskVO.of(taskPage.getContent()),
        taskPage.getPageable(), taskPage.getTotalElements());

    return resultPage;
  }

  @GetMapping("/{id}")
  @ApiOperation("get")
  public TaskVO get(@PathVariable Long id) {
    Task task = service.findOne(id);

    return TaskVO.of(task);
  }

  @PostMapping
  public TaskVO create(@RequestBody TaskCreateRequestVO requestVO) {
    Task task = service.save(requestVO.toTask());
    return TaskVO.of(task);
  }

  @PutMapping
  public TaskVO update(@RequestBody TaskUpdateRequestVO requestVO) {
    Task task = service.findOne(requestVO.getId());
    task = requestVO.fillTask(task);
    task = service.save(task);
    return TaskVO.of(task);
  }

  @PostMapping(path="/{id}/close")
  public TaskVO close(@PathVariable Long id) {
    Task task = service.close(id);
    return TaskVO.of(task);
  }

  @PostMapping(path="/{id}/open")
  public TaskVO open(@PathVariable Long id) {
    Task task = service.open(id);
    return TaskVO.of(task);
  }
}
