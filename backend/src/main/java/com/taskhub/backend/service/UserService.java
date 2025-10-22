package com.taskhub.backend.service;

import com.taskhub.backend.entity.User;
import com.taskhub.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id){
        return userRepository.getUserById(id);
    }

    public Optional<User> getUserByUsername(String username){
        return userRepository.getUserByUsername(username);
    }

    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public User updateUser(User user){
        if(user.getPassword() != null && !user.getPassword().isBlank()){
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
        }
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public User findByUsername(String username){
        return userRepository.getUserByUsername(username).orElse(null);
    }
}

