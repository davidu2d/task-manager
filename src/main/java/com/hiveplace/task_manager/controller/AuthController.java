package com.hiveplace.task_manager.controller;

import com.hiveplace.task_manager.model.AuthResponse;
import com.hiveplace.task_manager.model.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

public interface AuthController {
    @Operation(summary = "Create a new token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/token")
    ResponseEntity<Mono<AuthResponse>> token(@ModelAttribute("user") LoginUser user);
}
