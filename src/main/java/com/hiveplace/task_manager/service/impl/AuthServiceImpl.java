package com.hiveplace.task_manager.service.impl;

import com.hiveplace.task_manager.config.security.JWTService;
import com.hiveplace.task_manager.model.AuthResponse;
import com.hiveplace.task_manager.model.LoginUser;
import com.hiveplace.task_manager.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    final private ReactiveUserDetailsService userDetailsService;
    final private JWTService jwtService;
    final private PasswordEncoder encoder;

    public AuthServiceImpl(
            ReactiveUserDetailsService userDetailsService,
            JWTService jwtService,
            PasswordEncoder encoder
    ) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.encoder = encoder;
    }

    @Override
    public Mono<AuthResponse> authenticate(LoginUser user) {
        log.info("User: "+ user);
        Mono<UserDetails> foundUser = userDetailsService.findByUsername(user.username());
        return foundUser.map(u -> {
            if (u.getUsername() == null)
                return new AuthResponse("", "User not found. Please register before logging in", HttpStatus.NOT_FOUND);
            if (encoder.matches(user.password(), u.getPassword())){
                var token = jwtService.generate(u.getUsername());
                return new AuthResponse(token,"Success", HttpStatus.OK);
            }
            return new AuthResponse("","Invalid Credentials", HttpStatus.UNAUTHORIZED);
        });
    }
}
