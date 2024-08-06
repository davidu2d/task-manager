package com.hiveplace.task_manager.dto;

import lombok.Builder;

@Builder
public record LoginUser(
        String username,
        String password
) {
}

