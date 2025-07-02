package com.example.backend.services;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GuacamoleService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String GUACAMOLE_URL = "http://localhost:8888/guacamole";
    private final String USERNAME = "moad";
    private final String PASSWORD = "1966";

    public Map<String, Object> authenticate(String username, String password) {
        String authUrl = GUACAMOLE_URL + "/api/tokens";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("username", username);
        body.add("password", password);

        ResponseEntity<Map> response = restTemplate.postForEntity(authUrl, new HttpEntity<>(body, headers), Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody(); // Contient authToken et autres infos
        }
        throw new RuntimeException("Authentication failed");
    }

    public List<Map<String, Object>> listConnections(String token) {
        String url = GUACAMOLE_URL + "/api/session/data/mysql/connections";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Guacamole-Token", token);

        ResponseEntity<Map<String, Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<Map<String, Map<String, Object>>>() {}
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return new ArrayList<>(response.getBody().values());
        }
        throw new RuntimeException("Failed to fetch connections");
    }

    public String getConnectionIdByIp(String token, String targetIp) {
        List<Map<String, Object>> connections = listConnections(token);

        for (Map<String, Object> connection : connections) {
            String name = (String) connection.get("name");
            System.out.println("Comparing IP: " + targetIp + " with name: " + name);

            if (targetIp.equals(name)) {
                String debug= (String) connection.get("identifier");
                System.out.println("Found Identifier: " + debug);
                return (String) connection.get("identifier");
            }
        }
        throw new RuntimeException("No connection found for IP: " + targetIp);
    }



    public String generateConnectionUrl(String connectionId, String token) {
        return GUACAMOLE_URL + "/#/client/" + connectionId + "?token=" + token;
    }

    public String getConnectionUrlByIp(String targetIp) {
        System.out.println("GuacamoleService: getConnectionUrlByIp");
        Map<String, Object> auth = authenticate(USERNAME, PASSWORD);
        String token = (String) auth.get("authToken");

        String connectionId = getConnectionIdByIp(token, targetIp);
        return generateConnectionUrl(connectionId, token);
    }

}