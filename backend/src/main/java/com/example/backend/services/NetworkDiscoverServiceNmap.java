package com.example.backend.services;

import com.example.backend.Enum.NodeStatus;
import com.example.backend.entities.Node;
import com.example.backend.repositories.NodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@EnableScheduling
public class NetworkDiscoverServiceNmap implements NetworkDiscoveryService {
    private final NodeRepository nodeRepository;

    @Override
    public Set<Node> getAllNodes() {
        return new HashSet<>(nodeRepository.findAll());
    }

    @Scheduled(fixedRateString = "120000")
    public void periodicNetworkScan() {
        this.scanNetwork("192.168.60.0/24");
        System.out.println("Scan périodique du réseau effectué.");
    }

    @Override
    public Set<Node> scanNetwork(String subnet) {
        // Récupérer les nodes existants depuis la base de données
        Set<Node> existingNodes = new HashSet<>(nodeRepository.findAll());

        // Scanner le réseau pour obtenir les nodes actifs
        Set<Node> scannedNodes = scanNetworkWithNmap(subnet);

        // Comparer et mettre à jour les nodes
        Set<Node> nodesToUpdate = compareAndUpdateNodes(existingNodes, scannedNodes);

        // Sauvegarder les modifications
        nodeRepository.saveAll(nodesToUpdate);

        // Retourner l'état actuel du réseau
        return new HashSet<>(nodeRepository.findAll());
    }

    private Set<Node> scanNetworkWithNmap(String subnet) {
        Set<Node> scannedNodes = new HashSet<>();
        try {
            Process process = Runtime.getRuntime().exec("nmap -sn " + subnet);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            String currentIp = null;

            while ((line = reader.readLine()) != null) {
                if (line.contains("Nmap scan report for")) {
                    String[] parts = line.split(" ");
                    currentIp = parts[parts.length - 1].replaceAll("[()]", "");
                    System.out.println("IP détectée: " + currentIp);
                } else if (line.contains("MAC Address") && currentIp != null) {
                    String mac = line.split(" ")[2];
                    scannedNodes.add(new Node(currentIp, mac));
                    currentIp = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scannedNodes;
    }

    private Set<Node> compareAndUpdateNodes(Set<Node> existingNodes, Set<Node> scannedNodes) {
        Set<Node> nodesToUpdate = new HashSet<>();

        // Créer une map des nodes existants par IP pour un accès rapide
        Map<String, Node> existingNodesMap = existingNodes.stream()
                .collect(Collectors.toMap(Node::getIp, Function.identity()));

        // 1. Traiter les nodes scannés
        for (Node scannedNode : scannedNodes) {
            Node existingNode = existingNodesMap.get(scannedNode.getIp());

            if (existingNode != null) {
                // Node existe - vérifier si le statut doit être mis à jour
                if (existingNode.getStatus() != NodeStatus.UP) {
                    existingNode.setStatus(NodeStatus.UP);
                    nodesToUpdate.add(existingNode);
                }
                existingNodesMap.remove(scannedNode.getIp()); // Marquer comme traité
            } else {
                // Nouveau node - l'ajouter
                scannedNode.setStatus(NodeStatus.UP);
                nodesToUpdate.add(scannedNode);
            }
        }

        // 2. Traiter les nodes existants non retrouvés dans le scan
        existingNodesMap.values().forEach(notFoundNode -> {
            if (notFoundNode.getStatus() != NodeStatus.DOWN) {
                notFoundNode.setStatus(NodeStatus.DOWN);
                nodesToUpdate.add(notFoundNode);
            }
        });

        return nodesToUpdate;
    }
}