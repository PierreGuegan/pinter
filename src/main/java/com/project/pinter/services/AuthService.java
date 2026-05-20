package com.project.pinter.services;

import com.project.pinter.dto.LoginRequest;
import com.project.pinter.dto.RegisterRequest;
import com.project.pinter.entities.User;
import com.project.pinter.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public void register(RegisterRequest req) {
        System.out.println("REGISTER START");

        User user = new User();
        user.setUsername(req.username);
        user.setEmail(req.email);
        user.setPassword(passwordEncoder.encode(req.password));

        userRepository.save(user);

        System.out.println("USER SAVED");
        System.out.println("EMAIL RAW = [" + req.email + "]");
    }

    public String login(LoginRequest req) {

        User user = userRepository.findByEmail(req.email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(req.password, user.getPassword())) {
            throw new RuntimeException("Bad credentials");
        }

        return jwtService.generateToken(user.getEmail());

    }


}