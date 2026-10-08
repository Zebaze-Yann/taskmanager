package com.springk48.taskmanager.service;

import com.springk48.taskmanager.model.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springk48.taskmanager.dto.RegisterRequest;
import com.springk48.taskmanager.dto.UserResponse;
import com.springk48.taskmanager.exception.AlreadyExistingEmailException;
import com.springk48.taskmanager.model.User;
import com.springk48.taskmanager.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {

            throw new AlreadyExistingEmailException("email déjà existant");
        }

        User saved = userRepository.save(toUser(request));
        return userService.toResponse(saved);

    }

    private User toUser(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        return user;
    }

}
