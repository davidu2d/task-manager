package com.hiveplace.task_manager.entity;

import com.hiveplace.task_manager.enums.TaskStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "tasks")
public class Task {

    @Id
    private String id;

    private String description;

    private TaskStatus status;

    private LocalDateTime dateTimeCreated;

    private LocalDateTime dateTimeUpdate;
}
