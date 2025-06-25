package com.example.backend.entities;

public class SnmpStatus {

    private String ip;
    private boolean snmpRunning;

    public SnmpStatus(String ip, boolean snmpRunning) {
        this.ip = ip;
        this.snmpRunning = snmpRunning;
    }

    // Getters
    public String getIp() { return ip; }
    public boolean isSnmpRunning() { return snmpRunning; }
}
