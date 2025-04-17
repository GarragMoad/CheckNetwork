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
        System.out.println("URL: " + url);

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

}
