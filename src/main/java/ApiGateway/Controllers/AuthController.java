package ApiGateway.Controllers;

import ApiGateway.DTO.LoginRequest;
import ApiGateway.DTO.User;
import ApiGateway.Jwt.JwtUtil;
import ApiGateway.Repositories.UserRepo;
import ApiGateway.Services.AuthServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthServices authServices;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    public AuthController(AuthServices authService) {
        this.authServices = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        return authServices.login(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String token) {
        try {
            String username = jwtUtil.extractUsername(token);
            if (username != null && jwtUtil.validateTokenIgnoreExpiration(token)) {
                Optional<User> user = userRepo.findByUsername(username);
                if (user.isPresent()) {
                    String newToken = jwtUtil.generateToken(username, user.get().getRoles());
                    return ResponseEntity.ok(Collections.singletonMap("token", newToken));
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
    }
}

