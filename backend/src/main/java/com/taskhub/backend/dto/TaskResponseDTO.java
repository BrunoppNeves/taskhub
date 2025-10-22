package com.taskhub.backend.dto;

import com.taskhub.backend.entity.TaskStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
@Setter

public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Long userId;
    private LocalDateTime createdAt;

    public TaskResponseDTO() {}

    public TaskResponseDTO(Long id, String title, String description, TaskStatus status, Long userId, LocalDateTime createdAt){
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.userId = userId;
        this.createdAt = createdAt;
    }
}
