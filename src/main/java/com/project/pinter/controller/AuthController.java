package com.project.pinter.controller;

import com.project.pinter.dto.AuthResponse;
import com.project.pinter.dto.LoginRequest;
import com.project.pinter.dto.RegisterRequest;
import com.project.pinter.entities.User;
import com.project.pinter.repositories.UserRepository;
import com.project.pinter.services.AuthService;
import com.project.pinter.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest req) {
        System.out.println(">>> REGISTER CONTROLLER HIT");
        authService.register(req);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {
        String token = authService.login(req);
        return new AuthResponse(token);
    }

    @GetMapping("/me")
    public User me(@RequestHeader("Authorization") String token) {
        String email = jwtService.extractEmail(token.replace("Bearer ", ""));
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }



}
