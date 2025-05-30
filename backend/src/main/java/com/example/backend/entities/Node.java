package com.example.backend.entities;

import com.example.backend.Enum.NodeStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
//e

@Entity
@Data
public class Node {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @EqualsAndHashCode.Include
    private String ip;
    private String hostname;
    @Enumerated(EnumType.STRING)
    private NodeStatus status;


    public Node(String lastIp, String hostname) {
        this.ip = lastIp;
        this.hostname = hostname;
        this.status = NodeStatus.UP;
    }

    public Node() {

    }
}
