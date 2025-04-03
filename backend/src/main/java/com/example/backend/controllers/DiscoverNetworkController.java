package com.example.backend.controllers;

import com.example.backend.entities.Node;
import com.example.backend.services.NetworkDiscoveryService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/network")
@CrossOrigin(origins = "http://localhost:4200")
public class DiscoverNetworkController {
    private final NetworkDiscoveryService networkDiscoveryService;
    public DiscoverNetworkController(NetworkDiscoveryService networkDiscoveryService) {
        this.networkDiscoveryService = networkDiscoveryService;
    }
    @GetMapping("/scan")
    public List<Node> scanNetwork(@RequestParam String subnet) {
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node("192.168.0.1","host1"));
        nodes.add(new Node("192.168.0.2","host2"));
        nodes.add(new Node("192.168.0.3","host3"));
        return nodes;
        //return this.networkDiscoveryService.scanNetwork(subnet);
    }

}
