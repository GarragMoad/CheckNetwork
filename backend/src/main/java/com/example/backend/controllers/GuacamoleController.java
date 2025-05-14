package com.example.backend.controllers;

import com.example.backend.dto.GuacamoleLoginResponse;
import com.example.backend.services.GuacamoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/guacamole")
public class GuacamoleController {

    @Autowired
    private GuacamoleService guacamoleService;
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Map<String, Object>> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {

        // Authentification
        Map<String, Object> authResponse = guacamoleService.authenticate(username, password);
        String token = (String) authResponse.get("authToken");

        // Récupération des connexions
        List<Map<String, Object>> connections = guacamoleService.listConnections(token);

        // Construction de la réponse
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("connections", connections);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/connect")
    public ResponseEntity<Map<String, String>> getConnectionUrl(@RequestParam String connectionId, @RequestParam String token) {
        String url = guacamoleService.generateConnectionUrl(connectionId, token);
        return ResponseEntity.ok(Map.of("url", url));
    }

    @GetMapping("/connect/by-ip")
    public ResponseEntity<Map<String, String>> getConnectionByIp(@RequestParam String ip) {
        String url = guacamoleService.getConnectionUrlByIp(ip);

        Map<String, String> response = new HashMap<>();
        response.put("url", url);

        return ResponseEntity.ok(response);
    }

}
