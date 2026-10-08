package com.springk48.taskmanager.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springk48.taskmanager.dto.UserResponse;
import com.springk48.taskmanager.exception.UserNotFoundException;
import com.springk48.taskmanager.model.User;
import com.springk48.taskmanager.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public UserResponse getUserByID(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("utilisateur non trouvé"));
        return toResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public UserResponse updateUser(Long id, String newName, String newEmail, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("utilisateur non trouvé"));

        if (newName != null && !newName.isEmpty()) {
            user.setUsername(newName);
        }

        if (newEmail != null && !newEmail.isEmpty()) {
            user.setEmail(newEmail);
        }

        if (newPassword != null && !newPassword.isEmpty()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        return toResponse(user);
    }

    public void deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException("utilisateur introuvable");
        }

    }

    public UserResponse toResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getRole(),
                user.getUsername(),
                user.getEmail());
    }

}
