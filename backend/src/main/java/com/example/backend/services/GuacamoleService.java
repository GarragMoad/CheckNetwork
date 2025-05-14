package com.example.backend.services;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GuacamoleService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String GUACAMOLE_URL = "http://localhost:8080/guacamole";

    public String authenticate(String username, String password) {
        String url = GUACAMOLE_URL + "/api/tokens";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // ✅ bon Content-Type

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", username);
        formData.add("password", password); // ✅ corps du formulaire

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Object token = response.getBody().get("authToken");
            return token != null ? token.toString() : null;
        }

        throw new RuntimeException("Authentication failed");
    }


    public List<Map<String, Object>> listConnections(String token) {
        String url = GUACAMOLE_URL + "/api/session/data/mysql/connections";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Guacamole-Token", token);
        System.out.println("Token: " +headers.toString());

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> data = response.getBody();
            return data.values().stream()
                    .filter(value -> value instanceof Map)
                    .map(value -> (Map<String, Object>) value)
                    .toList();
        }
        throw new RuntimeException("Failed to fetch connections");
    }

    public String generateConnectionUrl(String connectionId, String token) {
        return GUACAMOLE_URL + "/#/client/" + connectionId + "?token=" + token;
    }
}