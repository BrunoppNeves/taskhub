package com.taskhub.backend.controller;

import com.taskhub.backend.dto.UserRequestDTO;
import com.taskhub.backend.entity.User;
import com.taskhub.backend.security.JwtUtil;
import com.taskhub.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@RequestBody @Valid UserRequestDTO dto){
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRoles(dto.getRoles());

        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String username, @RequestParam String password) {
        try {
            User user = userService.findByUsername(username);
            
            if (user == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Usuário não encontrado");
                return ResponseEntity.status(401).body(error);
            }

            if (!passwordEncoder.matches(password, user.getPassword())) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Senha incorreta");
                return ResponseEntity.status(401).body(error);
            }

            String token = jwtUtil.generateToken(username, user.getId());
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", user.getId());
            response.put("username", username);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Erro interno do servidor");
            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Token não fornecido");
                return ResponseEntity.status(401).body(error);
            }

            String token = authHeader.substring(7);
            if (jwtUtil.isTokenValid(token)) {
                Map<String, Object> response = new HashMap<>();
                response.put("valid", true);
                response.put("userId", jwtUtil.getUserId(token));
                response.put("username", jwtUtil.extractClaims(token).getSubject());
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Token inválido");
                return ResponseEntity.status(401).body(error);
            }
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Erro ao validar token");
            return ResponseEntity.status(500).body(error);
        }
    }
}