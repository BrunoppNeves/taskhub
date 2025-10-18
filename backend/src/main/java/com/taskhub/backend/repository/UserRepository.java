package com.taskhub.backend.repository;

import com.taskhub.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> getUserById(Long id);

    Optional<User> getUserByUsername(String username);
}
