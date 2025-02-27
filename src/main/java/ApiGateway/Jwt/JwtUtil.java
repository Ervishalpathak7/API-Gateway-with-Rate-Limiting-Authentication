package ApiGateway.Jwt;

import ApiGateway.Caching.TokenCacheService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET;

    private static SecretKey key;
    private final TokenCacheService tokenCacheService;

    public JwtUtil(TokenCacheService tokenCacheService) {
        this.tokenCacheService = tokenCacheService;
    }

    @PostConstruct
    public void init() {
        byte[] decode = Base64.getDecoder().decode(SECRET);
        key = Keys.hmacShaKeyFor(decode);
    }

    // Generate JWT token
    public String generateToken(String username, List<String> roles) {
        Map<String, Object> claims = Map.of("username", username, "roles", roles);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuer("TradeX")
                .signWith(key)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .compact();
    }

    // Validate JWT token and extract username from it
    public String validateToken(String token) {
        try {
            // Check if the token is in the cache
            String cachedUsername = tokenCacheService.getUsernameFromCache(token);

            if (cachedUsername != null) return cachedUsername;

            // Validate the token
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            String username = claimsJws.getBody().getSubject();
            // Cache the token
            tokenCacheService.cacheToken(token, username);
            return username;

        } catch (Exception e) {
            return null;
        }
    }

    // Extract Username
    public String extractUsername(String token) {

        // check if the token is in the cache
        String cachedUsername = tokenCacheService.getUsernameFromCache(token);
        if (cachedUsername != null) return cachedUsername;

        // Extract the username from the token
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public Collection<GrantedAuthority> extractAuthorities(String jwtToken) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken).getBody();
        List<?> rolesRaw = claims.get("roles", List.class);

        List<String> roles = rolesRaw.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .toList();

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

    }

    public boolean validateTokenIgnoreExpiration(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}