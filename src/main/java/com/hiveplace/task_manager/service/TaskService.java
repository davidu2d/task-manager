package com.hiveplace.task_manager.service;

import com.hiveplace.task_manager.dto.TaskDTO;
import com.hiveplace.task_manager.entity.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {
    Mono<TaskDTO> save(TaskDTO task, FilePart anexo);
    Flux<TaskDTO> findAll(String status, Pageable pageable);
    Mono<TaskDTO> update(String id, TaskDTO task);
    Mono<Void> delete(String id);
}
