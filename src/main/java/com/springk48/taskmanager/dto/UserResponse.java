package com.springk48.taskmanager.dto;

import com.springk48.taskmanager.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private Role role;
    private String username;
    private String email;

}
