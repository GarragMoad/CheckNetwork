package com.example.backend.dto;

import java.util.List;
import java.util.Map;

public class GuacamoleLoginResponse {
    private String token;
    private List<Map<String, Object>> connections;

    // Constructeur
    public GuacamoleLoginResponse(String token, List<Map<String, Object>> connections) {
        this.token = token;
        this.connections = connections;
    }

    // Getters & Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Map<String, Object>> getConnections() {
        return connections;
    }

    public void setConnections(List<Map<String, Object>> connections) {
        this.connections = connections;
    }
}
