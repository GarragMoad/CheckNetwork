package com.example.backend.controllers;

import com.example.backend.entities.Node;
import com.example.backend.services.NetworkDiscoverServiceNmap;
import com.example.backend.services.NetworkDiscoveryService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/network")
@CrossOrigin(origins = "http://localhost:4200")
public class DiscoverNetworkController {

    static String subnet="192.168.60.0/24";
    private final NetworkDiscoveryService networkDiscoveryService;
    public DiscoverNetworkController(NetworkDiscoverServiceNmap networkDiscoveryService) {
        this.networkDiscoveryService = networkDiscoveryService;
    }
    @GetMapping("/scan")
    public Set<Node> scanNetwork() {
        return this.networkDiscoveryService.scanNetwork(DiscoverNetworkController.subnet);
    }

}
