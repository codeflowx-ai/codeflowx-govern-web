package com.codeflowx.govern.business.developer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servicio para gestión de API Keys
 */
@Slf4j
@Service
public class ApiKeyService {

    private final Map<String, Map<String, Object>> apiKeys = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    /**
     * Genera nueva API key
     */
    public Map<String, Object> generateApiKey() {
        String apiKey = "cfx_" + generateRandomString(32);
        String apiKeyId = UUID.randomUUID().toString();

        Map<String, Object> keyData = new HashMap<>();
        keyData.put("id", apiKeyId);
        keyData.put("api_key", apiKey);
        keyData.put("created_at", LocalDateTime.now().toString());
        keyData.put("last_used", null);
        keyData.put("status", "ACTIVE");

        apiKeys.put(apiKeyId, keyData);

        log.info("API key generada: {}", apiKeyId);
        return keyData;
    }

    /**
     * Lista API keys
     */
    public List<Map<String, Object>> listApiKeys() {
        return new ArrayList<>(apiKeys.values());
    }

    /**
     * Elimina API key
     */
    public void deleteApiKey(String apiKeyId) {
        apiKeys.remove(apiKeyId);
        log.info("API key eliminada: {}", apiKeyId);
    }

    /**
     * Valida API key
     */
    public boolean validateApiKey(String apiKey) {
        return apiKeys.values().stream()
                .anyMatch(key -> apiKey.equals(key.get("api_key")) && "ACTIVE".equals(key.get("status")));
    }

    /**
     * Genera string aleatorio
     */
    private String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
