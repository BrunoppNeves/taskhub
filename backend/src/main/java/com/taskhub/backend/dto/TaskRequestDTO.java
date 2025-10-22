package com.taskhub.backend.dto;

import com.taskhub.backend.entity.TaskStatus;
import com.taskhub.backend.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class TaskRequestDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    private TaskStatus status;

    private User user;
}
