package com.springk48.taskmanager.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;
}
