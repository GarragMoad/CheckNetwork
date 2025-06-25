package com.example.backend.services;

import com.example.backend.entities.SnmpStatus;
import com.example.backend.entities.TargetVm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class SnmpService {

    private static final String TARGET_FILE = "C:\\Users\\garra\\Documents\\solarwinds\\Network\\Targets\\targets.json";
    private static final String COMMUNITY = "public";
    private static final String OID = "1.3.6.1.2.1.1.1.0"; // sysDescr

    public List<SnmpStatus> checkHosts() {
        List<SnmpStatus> results = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = new FileInputStream("C:\\Users\\garra\\Documents\\solarwinds\\Network\\Targets\\targets.json");

            List<TargetVm> targets = mapper.readValue(inputStream, new TypeReference<List<TargetVm>>() {});
            for (TargetVm vm : targets) {
                String ip = vm.getIp();
                boolean isRunning = runSnmpwalk(ip);
                results.add(new SnmpStatus(ip, isRunning));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }


    private boolean runSnmpwalk(String ip) {
        try {
            String command = "snmpwalk /r:" + ip + " /v:2c /c:public";
            Process process = Runtime.getRuntime().exec(command);

            BufferedReader stdOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            boolean hasValidOutput = false;

            String line;
            while ((line = stdOutput.readLine()) != null) {
                if (line.contains("OID=")) {
                    hasValidOutput = true;
                }
            }

            // Vérifie s'il y a une erreur explicite
            String errLine;
            while ((errLine = stdError.readLine()) != null) {
                if (errLine.toLowerCase().contains("timeout") || errLine.toLowerCase().contains("failed")) {
                    return false;
                }
            }

            return hasValidOutput;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}
