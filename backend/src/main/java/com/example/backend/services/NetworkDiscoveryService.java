package com.example.backend.services;

import com.example.backend.entities.Node;
import com.example.backend.repositories.NodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service

public interface NetworkDiscoveryService  {

    public Set<Node> scanNetwork(String subnet);

    Set<Node> getAllNodes();
}
