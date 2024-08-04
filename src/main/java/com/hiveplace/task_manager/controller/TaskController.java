package com.hiveplace.task_manager.controller;

import com.hiveplace.task_manager.dto.TaskDTO;
import com.hiveplace.task_manager.entity.Task;
import com.hiveplace.task_manager.service.TaskService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("v1/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Mono<ResponseEntity<TaskDTO>> save(@ModelAttribute("task") TaskDTO task
    ){
        return taskService.save(task, task.anexo())
                .map(ta -> ResponseEntity.status(HttpStatus.CREATED).body(ta))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<Task>>> findAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "dateTimeCreated") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection,
            @RequestParam(required = false) String status
    ){
        return taskService.findAll(
                        status,
                        PageRequest.of(
                                page,
                                size,
                                Sort.by(sortDirection.equalsIgnoreCase("asc") ? Sort.Order.asc(sortBy) : Sort.Order.desc(sortBy))))
                .collectList()
                .flatMap(tasks -> tasks.isEmpty()
                        ? Mono.just(ResponseEntity.noContent().build())
                        : Mono.just(ResponseEntity.ok(Flux.fromIterable(tasks))));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Task>> update(@PathVariable String id, @RequestBody Task task){
        return taskService.update(id, task)
                .map(ta -> ResponseEntity.ok(ta))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable String id){
        return taskService.delete(id)
                .then(Mono.just(ResponseEntity.ok().build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }
}