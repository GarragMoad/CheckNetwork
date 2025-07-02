package com.example.backend.controllers;

import com.example.backend.services.GrafanaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/dashboards")
public class DashboardController {

    private final GrafanaService grafanaService;

    public DashboardController(GrafanaService grafanaService) {
        this.grafanaService = grafanaService;
    }

    @GetMapping("/search")
    public Optional<String> searchDashboard(@RequestParam String title) {
        String dashboardUrl = String.valueOf(grafanaService.getDashboardUrl(title));
        System.out.println("Dashboard URL: " + dashboardUrl);
        return grafanaService.getDashboardUrl(title);
    }

    @GetMapping("/dashboard-json")
    public ResponseEntity<?> getDashboardJson(@RequestParam String title) {
        Optional<Map<String, Object>> json = grafanaService.getDashboardJsonByTitle(title);
        return json.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

