package com.taskhub.backend.service;

import com.taskhub.backend.entity.Task;
import com.taskhub.backend.entity.TaskStatus;
import com.taskhub.backend.entity.User;
import com.taskhub.backend.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public Task createTask(Task task){
        return taskRepository.save(task);
    }

    public List<Task> getTasksByUser(User user){
        return taskRepository.findByUser(user);
    }
    
    public Optional<Task> getTaskById(Long id){
        return taskRepository.findById(id);
    }
    
    public List<Task> getTasksByUserAndStatus(User user, TaskStatus taskStatus){
        return taskRepository.findByUserAndStatus(user, String.valueOf(taskStatus));
    }
    
    @Transactional
    public Task updateTask(Task task){
        if(task.getUser().getId() == null){
            throw new IllegalArgumentException("Task must have a user assigned");
        }
        return taskRepository.save(task);
    }
    
    @Transactional
    public void deleteTask(Long id){
        if(!taskRepository.existsById(id)){
            throw new EntityNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    
}
