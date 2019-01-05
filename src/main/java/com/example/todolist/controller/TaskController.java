package com.example.todolist.controller;

import com.example.todolist.model.Task;
import com.example.todolist.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

@Api(tags = "tasks")
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

  private final TaskService service;

  @GetMapping
  @ApiOperation("list")
  public Page<TaskVO> findPaginated(@RequestParam("page") int page, @RequestParam("size") int size) {
    Page<Task> taskPage = service.findPaginated(page, size);

    Page<TaskVO> resultPage = new PageImpl<>(TaskVO.of(taskPage.getContent()),
        taskPage.getPageable(), taskPage.getTotalElements());

    return resultPage;
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
