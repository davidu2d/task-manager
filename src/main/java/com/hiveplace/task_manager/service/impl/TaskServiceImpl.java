package com.hiveplace.task_manager.service.impl;

import com.hiveplace.task_manager.entity.Task;
import com.hiveplace.task_manager.repository.TaskRepository;
import com.hiveplace.task_manager.service.TaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ReactiveMongoTemplate mongoTemplate;

    public TaskServiceImpl(TaskRepository taskRepository, ReactiveMongoTemplate mongoTemplate){
        this.taskRepository = taskRepository;
        this.mongoTemplate = mongoTemplate;
    }
    @Override
    public Mono<Task> save(Task task) {
        task.setDateTimeCreated(LocalDateTime.now());
        return taskRepository.insert(task);
    }
    @Override
    public Flux<Task> findAll(String status, Pageable pageable) {
        Query query = new Query();
        if(status != null && !status.isEmpty())
            query.addCriteria(Criteria.where("status").is(status));
        query.with(pageable);
        return mongoTemplate.find(query, Task.class);
    }

    @Override
    public Mono<Task> update(String id, Task task) {
        return taskRepository.findById(id)
                .flatMap(recoveredTask -> {
                    BeanUtils.copyProperties(task, recoveredTask);
                    recoveredTask.setDateTimeUpdate(LocalDateTime.now());
                    return taskRepository.save(recoveredTask);
                });
    }

    @Override
    public Mono<Void> delete(String id) {
        return taskRepository.deleteById(id);
    }
}
