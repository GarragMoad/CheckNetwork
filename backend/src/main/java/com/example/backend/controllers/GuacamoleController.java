package com.example.backend.controllers;

import com.example.backend.dto.GuacamoleLoginResponse;
import com.example.backend.services.GuacamoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/guacamole")
public class GuacamoleController {

    @Autowired
    private GuacamoleService guacamoleService;

    /*@PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<GuacamoleLoginResponse> login(@RequestParam("username") String username,
                                                        @RequestParam("password") String password) {
        String token = guacamoleService.authenticate(username, password);
        List<Map<String, Object>> connections = guacamoleService.listConnections(token);
        return ResponseEntity.ok(new GuacamoleLoginResponse(token, connections));
    }*/
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String login(@RequestParam("username") String username,
                                                        @RequestParam("password") String password) {
        String token = guacamoleService.authenticate(username, password);
        List<Map<String, Object>> connections = guacamoleService.listConnections(token);
        return connections.toString();
    }


    @GetMapping("/connect")
    public ResponseEntity<Map<String, String>> getConnectionUrl(@RequestParam String connectionId, @RequestParam String token) {
        String url = guacamoleService.generateConnectionUrl(connectionId, token);
        return ResponseEntity.ok(Map.of("url", url));
    }
}
