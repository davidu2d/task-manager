package com.hiveplace.task_manager.service.impl;

import com.hiveplace.task_manager.entity.Task;
import com.hiveplace.task_manager.enums.TaskStatus;
import com.hiveplace.task_manager.model.TaskDTO;
import com.hiveplace.task_manager.repository.TaskRepository;
import com.hiveplace.task_manager.service.FileService;
import com.hiveplace.task_manager.service.MessageService;
import com.hiveplace.task_manager.service.TaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ReactiveMongoTemplate mongoTemplate;

    private final FileService fileService;

    private final MessageService messageService;

    public TaskServiceImpl(
            TaskRepository taskRepository,
            ReactiveMongoTemplate mongoTemplate,
            FileService fileService,
            MessageService messageService
    ){
        this.taskRepository = taskRepository;
        this.mongoTemplate = mongoTemplate;
        this.fileService = fileService;
        this.messageService = messageService;
    }
    @PreAuthorize("hasRole('USER')")
    @Override
    public Mono<TaskDTO> save(TaskDTO task, FilePart anexo) {
        var newTask = task.toEntity();
        newTask.setDateTimeCreated(LocalDateTime.now());
        newTask.setStatus(TaskStatus.NAO_CONCLUIDA);
        var taskSaved = taskRepository.insert(newTask);
        return taskSaved.map(ta -> {
            try {
                if (anexo != null){
                    fileService.uploadFile(ta.getId(), anexo);
                }
                messageService.sendMessage(ta);
                return TaskDTO.fromEntity(ta);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    @PreAuthorize("hasRole('USER')")
    @Override
    public Flux<TaskDTO> findAll(String status, Pageable pageable) {
        Query query = new Query();
        if(status != null && !status.isEmpty())
            query.addCriteria(Criteria.where("status").is(status));
        query.with(pageable);
        var response = mongoTemplate.find(query, Task.class);
        return response.map(TaskDTO::fromEntity);
    }
    @PreAuthorize("hasRole('USER')")
    @Override
    public Mono<TaskDTO> update(String id, TaskDTO task) {
        return taskRepository.findById(id)
                .flatMap(recoveredTask -> {
                    var dateCreted = recoveredTask.getDateTimeCreated();
                    BeanUtils.copyProperties(task, recoveredTask);
                    recoveredTask.setDateTimeCreated(dateCreted);
                    recoveredTask.setDateTimeUpdate(LocalDateTime.now());
                    return taskRepository.save(recoveredTask);
                }).map(TaskDTO::fromEntity);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Mono<Void> delete(String id) {
        return taskRepository.deleteById(id);
    }
}
