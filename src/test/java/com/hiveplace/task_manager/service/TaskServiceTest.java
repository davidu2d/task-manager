package com.hiveplace.task_manager.service;

import com.hiveplace.task_manager.entity.Task;
import com.hiveplace.task_manager.enums.TaskStatus;
import com.hiveplace.task_manager.model.TaskDTO;
import com.hiveplace.task_manager.repository.TaskRepository;
import com.hiveplace.task_manager.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private FileService fileService;
    @Mock
    private MessageService messageService;
    @Mock
    private FilePart anexo;
    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void shouldSaveTask() throws IOException {
        TaskDTO task = TaskDTO.builder()
                .description("task tesk")
                .status(TaskStatus.NAO_CONCLUIDA)
                .build();
        Task taskEntity = task.toEntity();
        when(taskRepository.insert(any(Task.class))).thenReturn(Mono.just(taskEntity));
        doNothing().when(fileService).uploadFile(any(), any());
        doNothing().when(messageService).sendMessage(any(Task.class));

        Mono<TaskDTO> result = taskService.save(task, anexo);

        StepVerifier.create(result)
                .expectNextMatches(Objects::nonNull)
                .expectComplete()
                .verify();

        Mockito.verify(taskRepository).insert(any(Task.class));
    }

    @Test
    void shouldSaveTaskAndThowsException() throws IOException {
        TaskDTO task = TaskDTO.builder()
                .description("task tesk")
                .status(TaskStatus.NAO_CONCLUIDA)
                .build();
        Task taskEntity = task.toEntity();

        when(taskRepository.insert(any(Task.class))).thenReturn(Mono.just(taskEntity));
        doThrow(RuntimeException.class).when(fileService).uploadFile(any(), any());

        Mono<TaskDTO> result = taskService.save(task, anexo);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException)
                .verify();

        Mockito.verify(taskRepository).insert(any(Task.class));
    }
}