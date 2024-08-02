package com.hiveplace.task_manager.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "tasks")
public class Task {

    @Id
    private String id;

    private String description;

    private String status;

    private LocalDateTime dateTimeCreated;

    private LocalDateTime dateTimeUpdate;
}
