package com.example.backend.services;

import com.example.backend.entities.Node;
import com.example.backend.repositories.NodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class NetworkDiscoveryService {

    private final NodeRepository NodeRepository;
    public List<Node> scanNetwork(String subnet) {
        List<Node> nodes = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec("nmap -sn " + subnet);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            String lastIp = null;

            while ((line = reader.readLine()) != null) {
                if (line.contains("Nmap scan report for")) {
                    String[] parts = line.split(" ");
                    String ip = parts[parts.length - 1];
                    lastIp = ip;
                } else if (line.contains("MAC Address")) {
                    String hostname = line.split(" ")[2]; // Extrait le nom si disponible
                    if (lastIp != null) {
                        Node node = new Node(lastIp, hostname);
                        this.NodeRepository.save(node);
                        nodes.add(node);
                        lastIp = null;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nodes;
    }
}
