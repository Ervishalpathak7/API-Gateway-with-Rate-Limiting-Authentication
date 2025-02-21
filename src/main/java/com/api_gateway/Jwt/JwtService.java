package com.api_gateway.Jwt;

import com.api_gateway.Entity.User;
import com.api_gateway.Exception.InternalServer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private static String SECRET;
    private static SecretKey key;

    @PostConstruct
    public void init() {
        byte[] decode = Base64.getDecoder().decode(SECRET);
        key = Keys.hmacShaKeyFor(decode);
    }

    // Generate JWT token
    public String generateToken(User user) {
        Map<String, Object> claims = Map.of("username", user.getUsername(), "roles", user.getRole());
        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .signWith(key)
                .issuer("api_gateway")
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .compact();
    }

    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().
                    verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return !claimsJws.getPayload().getExpiration().before(new Date());
        } catch (Exception e) {
            throw new InternalServer("JWT parser failed to parse");
        }
    }

    // Extract Username
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Collection<? extends GrantedAuthority> extractAuthorities(String jwtToken) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();

        List<?> rolesRaw = claims.get("roles", List.class);

        List<String> roles = rolesRaw.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .toList();

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

    }


}
