package com.example.backend.controllers;

import com.example.backend.entities.SnmpStatus;
import com.example.backend.services.SnmpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/snmp")
public class SnmpStatusController {

    private final SnmpService snmpService;

    public SnmpStatusController(SnmpService snmpService) {
        this.snmpService = snmpService;
    }

    @GetMapping("/status")
    public List<SnmpStatus> getStatus() {
        return snmpService.checkHosts();
    }
}
