package com.hiveplace.task_manager.config.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Component
public class AuthManager implements ReactiveAuthenticationManager {
    final private JWTService jwtService;
    final private ReactiveUserDetailsService userDetailsService;

    public AuthManager(JWTService jwtService, ReactiveUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .cast(BearerToken.class)
                .flatMap(auth -> {
                    String username = jwtService.getUsername(auth.getCredentials());
                    Mono<UserDetails> founderUser = userDetailsService.findByUsername(username).defaultIfEmpty(new UserDetails() {
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
                    Mono<Authentication> authenticatedUser = founderUser.flatMap(u -> {
                        if(u.getUsername() == null)
                            Mono.error(new IllegalArgumentException("User not found in auth manager"));
                        if (jwtService.validate(u, auth.getCredentials()))
                            return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(u.getUsername(), u.getPassword(), u.getAuthorities()));
                        Mono.error(new IllegalArgumentException("Invalid token"));
                        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(u.getUsername(), u.getPassword(), u.getAuthorities()));
                    });
                    return authenticatedUser;
                });
    }
}
