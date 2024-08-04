package com.hiveplace.task_manager.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hiveplace.task_manager.entity.Task;
import com.hiveplace.task_manager.enums.TaskStatus;
import lombok.Builder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Builder
public record TaskDTO(
        String id,
        String description,
        TaskStatus status,
        FilePart anexo,
        LocalDateTime dateTimeCreated,
        LocalDateTime dateTimeUpdate
        ) {
        public Task toEntity(){
                return Task.builder()
                        .description(this.description)
                        .status(this.status)
                        .build();
        }

        public static TaskDTO fromEntity(Task task){
                return TaskDTO.builder()
                        .id(task.getId())
                        .description(task.getDescription())
                        .status(task.getStatus())
                        .dateTimeCreated(task.getDateTimeCreated())
                        .dateTimeUpdate(task.getDateTimeUpdate())
                        .build();
        }
}
