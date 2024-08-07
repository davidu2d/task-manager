package com.hiveplace.task_manager.model;

import lombok.Builder;

@Builder
public record LoginUser(
        String username,
        String password
) {
}

