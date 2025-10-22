package com.taskhub.backend.controller;

import com.taskhub.backend.dto.UserResponseDTO;
import com.taskhub.backend.entity.User;
import com.taskhub.backend.exception.UserNotFoundException;
import com.taskhub.backend.security.JwtUtil;
import com.taskhub.backend.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    private  final HttpServletRequest request;

    private Long getLoggedUserId() {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("JWT Token is missing");
        }
        String token = authHeader.replace("Bearer ", "");
        if (!jwtUtil.isTokenValid(token)) {
            throw new RuntimeException("JWT Token is invalid");
        }
        return jwtUtil.getUserId(token);
    }

    public UserController(UserService userService, JwtUtil jwtUtil, HttpServletRequest request) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.request = request;
    }

    @GetMapping
    public ResponseEntity<UserResponseDTO> findUser() {
        Long loggedUserId = getLoggedUserId();
        User user = userService.getUserById(loggedUserId)
                .orElseThrow(() -> new UserNotFoundException(loggedUserId));

        UserResponseDTO response = new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getRoles()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<UserResponseDTO> updateUser(@Valid @RequestBody UserResponseDTO dto) {
        Long loggedUserId = getLoggedUserId();
        User existingUser = userService.getUserById(loggedUserId).orElseThrow(() -> new UserNotFoundException(loggedUserId));

        existingUser.setUsername(dto.getUsername());
        if (dto.getRoles() != null) {
            existingUser.setRoles(dto.getRoles());
        }

        User updatedUser = userService.updateUser(existingUser);
        UserResponseDTO response = new UserResponseDTO(
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.getRoles()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteUser(){
        Long loggedUserId = getLoggedUserId();
        userService.deleteUser(loggedUserId);
        return ResponseEntity.noContent().build();
    }

}
