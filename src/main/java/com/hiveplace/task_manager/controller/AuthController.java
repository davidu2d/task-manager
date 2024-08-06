package com.hiveplace.task_manager.controller;

import com.hiveplace.task_manager.dto.AuthResponse;
import com.hiveplace.task_manager.dto.LoginUser;
import com.hiveplace.task_manager.config.security.JWTService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Tag(name = "Auth Resource")
@RestController
@RequestMapping("v1/auth")
public class AuthController {
    final ReactiveUserDetailsService userDetailsService;
    final JWTService jwtService;
    final PasswordEncoder encoder;

    public AuthController(ReactiveUserDetailsService userDetailsService, JWTService jwtService, PasswordEncoder encoder) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.encoder = encoder;
    }

    @PostMapping("/token")
    public Mono<ResponseEntity<AuthResponse>> token(@RequestBody LoginUser user){
        Mono<UserDetails> foundUser = userDetailsService.findByUsername(user.username())
                .defaultIfEmpty(new UserDetails() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        return null;
                    }

                    @Override
                    public String getPassword() {
                        return null;
                    }

                    @Override
                    public String getUsername() {
                        return null;
                    }

                    @Override
                    public boolean isAccountNonExpired() {
                        return false;
                    }

                    @Override
                    public boolean isAccountNonLocked() {
                        return false;
                    }

                    @Override
                    public boolean isCredentialsNonExpired() {
                        return false;
                    }

                    @Override
                    public boolean isEnabled() {
                        return false;
                    }
                });
        return foundUser.map(u -> {
            if (u.getUsername() == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthResponse("", "User not found. Please register before logging in"));
            if (encoder.matches(user.password(), u.getPassword()))
                return ResponseEntity.ok(new AuthResponse(jwtService.generate(u.getUsername()),"Success"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("","Invalid Credentials"));
        });
    }

    @GetMapping("/protected")
    public Mono<ResponseEntity<AuthResponse>> auth(){
        return Mono.just(ResponseEntity.ok(new AuthResponse("Welcome to endpoint protected", "Success")));
    }
}
