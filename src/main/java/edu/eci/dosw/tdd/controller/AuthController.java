package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.dto.AuthDTO;
import edu.eci.dosw.tdd.controller.dto.RegisterDTO;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.security.JwtService;
import edu.eci.dosw.tdd.core.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthDTO authDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authDTO.getUsername(),
                        authDTO.getPassword()
                )
        );

        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER")
                .replace("ROLE_", "");

        String token = jwtService.generateToken(authentication.getName(), role);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterDTO registerDTO) {
        User user = new User(
                registerDTO.getName(),
                0L,
                registerDTO.getUsername(),
                registerDTO.getPassword(),
                "USER"
        );
        userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}