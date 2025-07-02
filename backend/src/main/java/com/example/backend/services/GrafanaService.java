package com.example.backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GrafanaService {

    @Value("${grafana.url}")
    private String grafanaUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public Optional<String> getDashboardUrl(String title) {
        String url = grafanaUrl + "/api/search?query=" + title;

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "1966");
        headers.setAccept(MediaType.parseMediaTypes("application/json"));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                List.class
        );

        List<Map<String, Object>> dashboards = response.getBody();

        if (dashboards == null || dashboards.isEmpty()) return Optional.empty();

        for (Map<String, Object> dash : dashboards) {
            String dashTitle = (String) dash.get("title");
            if (title.equalsIgnoreCase(dashTitle)) {
                String uid = (String) dash.get("uid");
                String uri = (String) dash.get("uri");
                String slug = uri.split("/")[1];
                return Optional.of(grafanaUrl + "/d/" + uid + "/" + slug + "?orgId=1");
            }
        }

        return Optional.empty();
    }

    public Optional<Map<String, Object>> getDashboardJsonByTitle(String title) {
        // Étape 1 : Chercher le dashboard par titre
        String searchUrl = grafanaUrl + "/api/search?query=" + title;

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "1966"); // ← Remplacer par config sécurisée en prod
        headers.setAccept(MediaType.parseMediaTypes("application/json"));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List> searchResponse = restTemplate.exchange(
                searchUrl,
                HttpMethod.GET,
                entity,
                List.class
        );

        List<Map<String, Object>> dashboards = searchResponse.getBody();

        if (dashboards == null || dashboards.isEmpty()) return Optional.empty();

        for (Map<String, Object> dash : dashboards) {
            String dashTitle = (String) dash.get("title");
            if (title.equalsIgnoreCase(dashTitle)) {
                String uid = (String) dash.get("uid");

                // Étape 2 : Récupérer le dashboard complet par UID
                String dashboardUrl = grafanaUrl + "/api/dashboards/uid/" + uid;

                ResponseEntity<Map> dashboardResponse = restTemplate.exchange(
                        dashboardUrl,
                        HttpMethod.GET,
                        entity,
                        Map.class
                );

                Map<String, Object> dashboardJson = dashboardResponse.getBody();
                return Optional.ofNullable(dashboardJson);
            }
        }

        return Optional.empty();
    }


}
