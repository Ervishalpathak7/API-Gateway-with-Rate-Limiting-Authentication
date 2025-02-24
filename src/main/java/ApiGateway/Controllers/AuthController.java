package ApiGateway.Controllers;

import ApiGateway.DTO.LoginRequest;
import ApiGateway.Services.AuthServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthServices authServices;

    @Autowired
    public AuthController(AuthServices authService) {
        this.authServices = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        return authServices.login(loginRequest.getUsername(), loginRequest.getPassword());
    }
}
