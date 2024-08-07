package com.hiveplace.task_manager.controller.impl;

import com.hiveplace.task_manager.controller.TaskController;
import com.hiveplace.task_manager.model.TaskDTO;
import com.hiveplace.task_manager.service.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Tasks Resource")
@RestController
@RequestMapping("v1/tasks")
public class TaskControllerImpl implements TaskController {
    private final TaskService taskService;

    public TaskControllerImpl(TaskService taskService){
        this.taskService = taskService;
    }

    public ResponseEntity<Mono<TaskDTO>> save(TaskDTO task){
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.save(task, task.anexo()));
    }

    public ResponseEntity<Flux<TaskDTO>> findAll(int page, int size, String sortBy, String sortDirection, String status){
        var response = taskService.findAll(
                        status,
                        PageRequest.of(page, size, Sort.by(sortDirection.equalsIgnoreCase("asc") ? Sort.Order.asc(sortBy) : Sort.Order.desc(sortBy))));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Mono<TaskDTO>> update(String id, TaskDTO task){
        return ResponseEntity.ok(taskService.update(id, task));
    }

    public ResponseEntity<Mono<Void>> delete(String id){
        return ResponseEntity.ok(taskService.delete(id).then());
    }
}