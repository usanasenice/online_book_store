package com.bookstore.usanase.controllers;

import com.bookstore.usanase.model.User;
import com.bookstore.usanase.repository.UserRepository;
import com.bookstore.usanase.services.EmailService;
import com.bookstore.usanase.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.mail.MessagingException;
import java.util.UUID;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) throws MessagingException, javax.mail.MessagingException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        user.setEmailVerified(false);
        String verificationCode = UUID.randomUUID().toString();
        user.setVerificationCode(verificationCode);
        userRepository.save(user);
        emailService.sendVerificationEmail(user.getEmail(), verificationCode);
        return ResponseEntity.ok("Verification code sent to email");
    }
    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String code) {
        User user = userRepository.findByVerificationCode(code);
        if (user != null) {
            user.setEmailVerified(true);
            user.setVerificationCode(null);
            userRepository.save(user);
            return ResponseEntity.ok(jwtService.generateToken(user));
        }
        return ResponseEntity.badRequest().body("Invalid verification code");
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null && passwordEncoder.matches(user.getPassword(), existingUser.getPassword()) && existingUser.isEmailVerified()) {
            return ResponseEntity.ok(jwtService.generateToken(existingUser));
        }
        return ResponseEntity.badRequest().body("Invalid credentials or email not verified");
    }
}
