package com.hiveplace.task_manager.controller.impl;

import com.hiveplace.task_manager.controller.AuthController;
import com.hiveplace.task_manager.model.AuthResponse;
import com.hiveplace.task_manager.model.LoginUser;
import com.hiveplace.task_manager.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Tag(name = "Auth Resource")
@RestController
@RequestMapping("v1/auth")
public class AuthControllerImpl implements AuthController {
    final private AuthService authService;

    public AuthControllerImpl(AuthService authService) {
        this.authService = authService;
    }

    public ResponseEntity<Mono<AuthResponse>> token(LoginUser user){
        return ResponseEntity.status(HttpStatus.OK).body(authService.authenticate(user));
    }
}
