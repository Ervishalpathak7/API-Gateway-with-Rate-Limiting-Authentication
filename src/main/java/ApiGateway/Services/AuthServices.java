package ApiGateway.Services;

import ApiGateway.DTO.User;
import ApiGateway.Jwt.JwtUtil;
import ApiGateway.Repositories.UserRepo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {

    private final UserRepo userCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServices(UserRepo userCredentialRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.userCredentialRepository = userCredentialRepository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<String> login(String username, String password) {

        // Find the user by username
        User user = userCredentialRepository.findByUsername(username).orElse(null);

        // if user is null return invalid credentials
        if (user == null) {
            throw new UsernameNotFoundException("Invalid Credentials");
        }

        // Check the password
        if (passwordEncoder.matches(password, user.getPassword())) {
            // Generate JWT token
            String token = jwtUtil.generateToken(user.getUsername(), user.getRoles());

            // Set the token in the response header
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .headers(headers)
                    .body("User logged in successfully");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
    }
}
