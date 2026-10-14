package com.springk48.taskmanager.service;

import com.springk48.taskmanager.model.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springk48.taskmanager.dto.LoginRequest;
import com.springk48.taskmanager.dto.LoginResponse;
import com.springk48.taskmanager.dto.RegisterRequest;
import com.springk48.taskmanager.dto.UserResponse;
import com.springk48.taskmanager.exception.AlreadyExistingEmailException;
import com.springk48.taskmanager.exception.InvalidCredentialsException;
import com.springk48.taskmanager.model.User;
import com.springk48.taskmanager.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {

            throw new AlreadyExistingEmailException("email déjà existant");
        }

        User saved = userRepository.save(toUser(request));
        return userService.toResponse(saved);

    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("donnée invalide"));

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            String token = jwtService.generateToken(user);
            return new LoginResponse(token, userService.toResponse(user));

        } else {
            throw new InvalidCredentialsException("donnée invalide");
        }

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
