package com.example.backend.security;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
    // Getters & Setters
}
