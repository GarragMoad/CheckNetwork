package com.example.backend.services;

import com.example.backend.Enum.NodeStatus;
import com.example.backend.entities.Node;
import com.example.backend.repositories.NodeRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
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
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public Set<Node> getAllNodes() {
        return new HashSet<>(nodeRepository.findAll());
    }

    @Scheduled(fixedRateString = "120000")
    public void periodicNetworkScan() {
        Set<Node> updatedNodes =this.scanNetwork("192.168.60.0/24");

        // Envoyer les nodes mis à jour via WebSocket
        messagingTemplate.convertAndSend("/topic/network-status", updatedNodes);
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

        Set<Node> allNodes = new HashSet<>(nodeRepository.findAll());
        System.out.println("Nodes détecter" + allNodes);
        exportNodesAsJson(allNodes, "C:/Users/garra/Documents/solarwinds/Network/Targets/targets.json");

        // Retourner l'état actuel du réseau
        return new HashSet<>(nodeRepository.findAll());
    }

    private Set<Node> scanNetworkWithNmap(String subnet) {
        Set<Node> scannedNodes = new HashSet<>();
        Set<String> excludedIps = Set.of("192.168.60.1"); // ← IPs à ignorer (machine hôte ici)

        try {
            Process process = Runtime.getRuntime().exec("nmap -sn " + subnet);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            String currentIp = null;
            String currentHost = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Nmap scan report for")) {
                    String[] parts = line.split(" ");
                    if (parts.length >= 5 && line.contains("(") && line.contains(")")) {
                        currentHost = parts[4];
                        currentIp = parts[5].replaceAll("[()]", "");
                    } else {
                        currentHost = null;
                        currentIp = parts[4];
                    }

                    // Filtrer l’IP hôte
                    if (excludedIps.contains(currentIp)) {
                        System.out.println("IP ignorée : " + currentIp);
                        continue;
                    }

                    System.out.println("Hôte détecté : " + (currentHost != null ? currentHost : "<inconnu>") + " - IP : " + currentIp);
                    scannedNodes.add(new Node(currentIp, currentHost));
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
    public void exportNodesAsJson(Set<Node> nodes, String outputPath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(outputPath)) {
            gson.toJson(nodes, writer);
            System.out.println("✔️ Fichier JSON écrit : " + outputPath);
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de l'écriture du fichier JSON : " + e.getMessage());
        }
    }

}