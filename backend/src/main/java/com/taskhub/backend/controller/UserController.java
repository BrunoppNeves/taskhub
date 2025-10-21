package com.taskhub.backend.controller;

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

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user){
        User cretedUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(cretedUser);
    }

    @GetMapping
    public ResponseEntity<User> findUser() {
        Long loggedUserId = getLoggedUserId();

        User user = userService.getUserById(loggedUserId)
                .orElseThrow(() -> new UserNotFoundException(loggedUserId));
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user){
        Long loggedUserId = getLoggedUserId();
        User getUser = userService.getUserByUsername(user.getUsername()).orElseThrow(() -> new UserNotFoundException(null));

        if(!loggedUserId.equals(getUser.getId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        user.setId(loggedUserId);
        User updateUser = userService.updateUser(user);
        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteUser(){
        Long loggedUserId = getLoggedUserId();
        userService.deleteUser(loggedUserId);
        return ResponseEntity.noContent().build();
    }

}
