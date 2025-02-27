package ApiGateway.Security;

import ApiGateway.AuthFilters.JwtAuthenticationFilter;
import ApiGateway.Caching.TokenCacheService;
import ApiGateway.Jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final TokenCacheService tokenCacheService;

    @Autowired
    public SecurityConfig(JwtUtil jwtUtil, TokenCacheService tokenCacheService) {
        this.jwtUtil = jwtUtil;
        this.tokenCacheService = tokenCacheService;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)      // Disable default login form
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)      // Disable basic authentication
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/login").permitAll()
                        .pathMatchers("/users").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, tokenCacheService), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
