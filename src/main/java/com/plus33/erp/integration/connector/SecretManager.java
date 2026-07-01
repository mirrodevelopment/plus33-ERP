package com.plus33.erp.integration.connector;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class SecretManager {
    private final Map<String, String> simulatedSecrets = new HashMap<>();

    public SecretManager() {
        simulatedSecrets.put("partner-service-creds", "{\"apiKey\":\"SECRET-PARTNER-XYZ\",\"apiSecret\":\"987654321\"}");
        simulatedSecrets.put("sftp-creds", "{\"username\":\"sftpuser\",\"password\":\"sftp-pass-123\"}");
        simulatedSecrets.put("db-creds", "{\"username\":\"root\",\"password\":\"dbpassword\"}");
    }

    public String resolveSecret(String credentialReference) {
        if (credentialReference == null) return null;
        return simulatedSecrets.getOrDefault(credentialReference, "{}");
    }

    public void storeSecret(String key, String value) {
        simulatedSecrets.put(key, value);
    }
}
