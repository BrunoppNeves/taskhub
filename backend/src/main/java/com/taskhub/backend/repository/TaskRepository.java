package com.taskhub.backend.repository;

import com.taskhub.backend.entity.Task;
import com.taskhub.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);

    List<Task> findByUserAndStatus(User user, String status);
}
