package com.example.backend.repositories;

import com.example.backend.entities.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {
    Node findByIp(String ip);

    Node findByHostname(String hostname);

    Node findByStatus(String status);
}
