package com.hiveplace.task_manager.service;

import com.hiveplace.task_manager.dto.TaskDTO;
import com.hiveplace.task_manager.entity.Task;

public interface MessageService {
    void sendMessage(Task task);
}
