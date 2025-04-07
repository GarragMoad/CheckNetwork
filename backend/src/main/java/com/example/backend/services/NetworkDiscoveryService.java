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

public interface NetworkDiscoveryService  {

    public List<Node> scanNetwork(String subnet);
}
