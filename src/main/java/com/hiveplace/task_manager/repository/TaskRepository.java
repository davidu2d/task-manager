package com.hiveplace.task_manager.repository;

import com.hiveplace.task_manager.entity.Task;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.awt.print.Pageable;

@Repository
public interface TaskRepository extends ReactiveMongoRepository<Task, String> {
    Flux<Task> findAllBy(Pageable pageable);
}
