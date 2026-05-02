package com.azevedo.barberflow.security;

import com.azevedo.barberflow.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtService {

    private final String SECRET = "your-secret-super-secure-key-that-must-be-very-long";

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
    }

    public String extractUserId(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isValid(String token, UserDetails userDetails) {
        return extractUserId(token).equals(userDetails.getUsername());
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
