package ApiGateway.Jwt;

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
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET;

    private static SecretKey key;

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

    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            System.out.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    // Extract Username
    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public Collection<? extends GrantedAuthority> extractAuthorities(String jwtToken) {
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
}