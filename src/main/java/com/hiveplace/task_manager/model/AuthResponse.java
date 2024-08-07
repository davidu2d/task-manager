package com.hiveplace.task_manager.model;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record AuthResponse(
        String token,
        String message,
        HttpStatus status
) {
}
