package com.taskhub.backend.controller;

import com.taskhub.backend.entity.Task;
import com.taskhub.backend.entity.User;
import com.taskhub.backend.service.TaskService;
import com.taskhub.backend.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @PostMapping
    public Task createTask(@RequestBody @Valid Task task) {
        return taskService.createTask(task);
    }

    @GetMapping("/user")
    public List<Task> getTasksByUser(@RequestParam Long userId) {
        User user = userService.getUserById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return taskService.getTasksByUser(user);
    }

    @GetMapping("/{taskId}")
    public Task getTaskById(@PathVariable Long taskId) {
        return taskService.getTaskById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
    }

    @PutMapping("/{taskId}")
    public Task updateTask(@PathVariable Long taskId, @RequestBody @Valid Task task){
        task.setId(taskId);
        return taskService.updateTask(task);
    }

    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
    }


}
