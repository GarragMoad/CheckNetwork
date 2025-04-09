package com.example.backend.controllers;

import com.example.backend.entities.Node;
import com.example.backend.services.NetworkDiscoveryService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Set;

@Controller
public class NetworkWebSocketController {
    private final NetworkDiscoveryService networkDiscoveryService;

    public NetworkWebSocketController(NetworkDiscoveryService networkDiscoveryService) {
        this.networkDiscoveryService = networkDiscoveryService;
    }

    @MessageMapping("/request-network-status")
    @SendTo("/topic/network-status")
    public Set<Node> sendNetworkStatus() {
        return networkDiscoveryService.getAllNodes();
    }
}
