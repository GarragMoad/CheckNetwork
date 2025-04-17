package com.example.backend.controllers;

import com.example.backend.services.GrafanaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
        return grafanaService.getDashboardUrl(title);
    }
}

