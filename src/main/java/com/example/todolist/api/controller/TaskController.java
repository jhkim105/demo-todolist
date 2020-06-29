package com.example.todolist.api.controller;

import com.example.todolist.api.service.TaskService;
import com.example.todolist.core.model.Task;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Api(tags = "tasks")
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

  private final TaskService service;

//  @GetMapping
//  @ApiOperation("list")
//  public Page<TaskVO> list(PageRequestVO pageRequestVO, SearchVO searchVO) {
//    Page<Task> taskPage = service.findAll(pageRequestVO.toPageRequet(), searchVO);
//
//    Page<TaskVO> resultPage = new PageImpl<>(TaskVO.of(taskPage.getContent()),
//        taskPage.getPageable(), taskPage.getTotalElements());
//
//    return resultPage;
//  }

  @GetMapping("/{id}")
  @ApiOperation("get")
  @Transactional
  public Mono<TaskVO> get(@PathVariable Long id) {
    Task task = service.findOne(id);
    return Mono.just(TaskVO.of(task));
  }
//
//  @PostMapping
//  public TaskVO create(@RequestBody TaskCreateRequestVO requestVO) {
//    Task task = service.save(requestVO.toTask());
//    return TaskVO.of(task);
//  }
//
//  @PutMapping
//  public TaskVO update(@RequestBody TaskUpdateRequestVO requestVO) {
//    Task task = service.findOne(requestVO.getId());
//    task = requestVO.fillTask(task);
//    task = service.save(task);
//    return TaskVO.of(task);
//  }
//
//  @DeleteMapping("/{id}")
//  @ApiOperation("delete")
//  public ResponseEntity delete(@PathVariable Long id) {
//    service.delete(id);
//    return new ResponseEntity(HttpStatus.OK);
//  }
//
//  @PostMapping(path="/{id}/close")
//  public TaskVO close(@PathVariable Long id) {
//    Task task = service.close(id);
//    return TaskVO.of(task);
//  }
//
//  @PostMapping(path="/{id}/open")
//  public TaskVO open(@PathVariable Long id) {
//    Task task = service.open(id);
//    return TaskVO.of(task);
//  }
}
