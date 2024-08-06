package com.hiveplace.task_manager.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.Instant;

@Service
public class JWTService {
    final private SecretKey key;
    final private JwtParser parser;

    public JWTService() {
        this.key = Keys.hmacShaKeyFor("hiveplacehiveplacehiveplacehiveplacehiveplacehiveplacehiveplacehiveplacehiveplacehiveplace".getBytes());
        this.parser = Jwts.parserBuilder().setSigningKey(this.key).build();
    }

    public String generate(String username){
        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(900)))
                .signWith(key);
        return builder.compact();
    }

    public String getUsername(String token){
        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validate (UserDetails user, String token){
        Claims claims = parser.parseClaimsJws(token).getBody();
        boolean unexpired = claims.getExpiration().after(Date.from(Instant.now()));
        return unexpired && user.getUsername() == claims.getSubject();
    }
}
