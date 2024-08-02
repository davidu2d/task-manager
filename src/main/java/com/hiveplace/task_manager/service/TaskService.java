package com.hiveplace.task_manager.service;

import com.hiveplace.task_manager.entity.Task;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {
    Mono<Task> save(Task task);
    Flux<Task> findAll(String status, Pageable pageable);
    Mono<Task> update(String id, Task task);
    Mono<Void> delete(String id);
}
