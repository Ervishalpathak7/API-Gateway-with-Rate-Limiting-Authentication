package ApiGateway.AuthFilters;

import ApiGateway.Caching.TokenCacheService;
import ApiGateway.Jwt.JwtUtil;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtUtil jwtUtil;
    private final TokenCacheService tokenCacheService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil, TokenCacheService tokenCacheService) {
        this.jwtUtil = jwtUtil;
        this.tokenCacheService = tokenCacheService;
    }

    @Override
    @NonNull
    public Mono<Void> filter(
            ServerWebExchange exchange,
            @NonNull WebFilterChain chain) {

        // Get the request
        ServerHttpRequest request = exchange.getRequest();

        // Get the token from the header
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            String username = jwtUtil.validateToken(token);

            if (username != null) {
                Collection<GrantedAuthority> grantedAuthorities = jwtUtil.extractAuthorities(token);

                // Convert roles to a comma-separated string
                String roles = grantedAuthorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","));

                // Set authentication context
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);

                SecurityContext context = new SecurityContextImpl(auth);

                // Add roles to the request headers
                exchange.getRequest().mutate().headers(httpHeaders -> {
                    httpHeaders.set("X-User-Roles", roles);
                    httpHeaders.set("X-Username", username);
                });

                return chain.filter(exchange)
                        .contextWrite(securityContext -> securityContext.put(SecurityContext.class, context));
            } else {
                return unauthorizedResponse(exchange.getResponse());
            }
        }
        return chain.filter(exchange);
    }

    private Mono<Void> unauthorizedResponse(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
}
