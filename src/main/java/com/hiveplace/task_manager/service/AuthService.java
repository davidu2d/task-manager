package com.hiveplace.task_manager.service;

import com.hiveplace.task_manager.model.AuthResponse;
import com.hiveplace.task_manager.model.LoginUser;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<AuthResponse> authenticate(LoginUser loginUser);
}
