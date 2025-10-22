package com.taskhub.backend.controller;

import com.taskhub.backend.dto.TaskRequestDTO;
import com.taskhub.backend.dto.TaskResponseDTO;
import com.taskhub.backend.entity.Task;
import com.taskhub.backend.entity.User;
import com.taskhub.backend.exception.TaskNotFoundException;
import com.taskhub.backend.exception.UserNotFoundException;
import com.taskhub.backend.security.JwtUtil;
import com.taskhub.backend.service.TaskService;
import com.taskhub.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
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

    public TaskController(TaskService taskService, UserService userService, JwtUtil jwtUtil, HttpServletRequest request) {
        this.taskService = taskService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.request = request;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody @Valid Task task) {
        Long loggedUserId = getLoggedUserId();
        User user = userService.getUserById(loggedUserId).orElseThrow(() -> new UserNotFoundException(loggedUserId));
        task.setUser(user);
        Task createTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createTask);
    }

    @GetMapping("/user")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByUser() {
        Long loggedUserId = getLoggedUserId();
        User user = userService.getUserById(loggedUserId)
                .orElseThrow(() -> new UserNotFoundException(loggedUserId));

        List<TaskResponseDTO> tasks = taskService.getTasksByUser(user).stream().map(
                task -> new TaskResponseDTO(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getUser().getId(),
                        task.getCreatedAt()
                )
        ).collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@Valid @PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        TaskResponseDTO response = new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getUser().getId(),
                task.getCreatedAt()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody @Valid Task task){
        Long loggedUserId = getLoggedUserId();

        System.out.println("Usuario logado: " + loggedUserId);

        Task taskToUpdate = taskService.getTaskById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));

        if(!taskToUpdate.getUser().getId().equals(loggedUserId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User user = userService.getUserById(loggedUserId).orElseThrow(() -> new UserNotFoundException(loggedUserId));

        task.setId(taskId);
        task.setUser(user);
        Task updatedTask =  taskService.updateTask(task);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        Long loggedUserId = getLoggedUserId();
        Task task = taskService.getTaskById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        if (!task.getUser().getId().equals(loggedUserId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
