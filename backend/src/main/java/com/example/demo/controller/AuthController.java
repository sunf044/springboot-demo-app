package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, AuthenticationManager authManager, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.authManager = authManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
    
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
    
        return ResponseEntity.ok("Registered");
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        System.out.println("✅ /auth/login called");
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.email, request.password));
        User user = userRepository.findByEmail(request.email).orElseThrow();
        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        // Set refresh token to HttpOnly cookie
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // Set true in prod
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        return new LoginResponse(accessToken, refreshToken);
    }

    @PostMapping("/refresh")
    public LoginResponse refreshToken(@CookieValue("refreshToken") String refreshToken) {
        String email = jwtUtil.validateToken(refreshToken);
        User user = userRepository.findByEmail(email).orElseThrow();
        String newAccess = jwtUtil.generateAccessToken(user.getEmail(), user.getRole());
        String newRefresh = jwtUtil.generateRefreshToken(user.getEmail());
        return new LoginResponse(newAccess, newRefresh);
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        // Clear cookie
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

}
