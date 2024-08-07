package com.hiveplace.task_manager.controller;

import com.hiveplace.task_manager.model.TaskDTO;
import com.hiveplace.task_manager.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class TaskControllerImplTest {
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private TaskService taskService;

    @Test
    @DisplayName("Test enpoint save with unauthorization response")
    void notShouldSaveTask() {
        when(taskService.save(any(TaskDTO.class), any())).thenReturn(Mono.just(TaskDTO.builder().build()));

        webTestClient.post()
                .uri("v1/tasks")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromFormData("description", "task priority high"))
                .exchange()
                .expectStatus().isUnauthorized();
    }
}