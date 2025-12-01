package com.codeflowx.govern.viewmodel.aios.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cliente REST para Agent Supervisor Service.
 *
 * <p>Proporciona métodos para interactuar con el microservicio codeflowx-agent-supervisor.</p>
 */
@Slf4j
@Component
public class AgentSupervisorClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public AgentSupervisorClient(
            @Value("${agent.supervisor.api.url:http://localhost:8087/api/v1/supervisor}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
        log.info("AgentSupervisorClient initialized: baseUrl={}", baseUrl);
    }

    /**
     * Ejecuta una acción sobre un agente (PAUSE, RESUME, TERMINATE).
     *
     * @param agentId ID del agente
     * @param action Acción a ejecutar
     * @param reason Razón de la acción
     * @return Respuesta con actionId y status
     */
    public Map<String, Object> executeAction(String agentId, String action, String reason) {
        try {
            String url = baseUrl + "/" + agentId + "/actions";

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("action", action);
            requestBody.put("reason", reason != null ? reason : "Manual action from UI");

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            // TODO: Añadir autenticación (JWT, API Key, etc.)

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

            return response.getBody() != null ? response.getBody() : new HashMap<>();
        } catch (Exception e) {
            log.error("Error executing action {} on agent {}", action, agentId, e);
            throw new RuntimeException("Failed to execute action: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene el estado runtime de un agente.
     *
     * @param agentId ID del agente
     * @return Estado del agente con metadata
     */
    public Map<String, Object> getStatus(String agentId) {
        try {
            String url = baseUrl + "/" + agentId + "/status";
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            return response.getBody() != null ? response.getBody() : new HashMap<>();
        } catch (Exception e) {
            log.error("Error getting status for agent {}", agentId, e);
            throw new RuntimeException("Failed to get status: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene una memoria del agente.
     *
     * @param agentId ID del agente
     * @param namespace Namespace de la memoria
     * @param key Clave de la memoria
     * @return Memoria decifrada
     */
    public Map<String, Object> getMemory(String agentId, String namespace, String key) {
        try {
            String url = baseUrl + "/" + agentId + "/memory?namespace=" + namespace + "&key=" + key;
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            return response.getBody() != null ? response.getBody() : new HashMap<>();
        } catch (Exception e) {
            log.error("Error getting memory for agent {}", agentId, e);
            throw new RuntimeException("Failed to get memory: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene el historial versionado de una memoria.
     *
     * @param agentId ID del agente
     * @param namespace Namespace de la memoria
     * @param key Clave de la memoria
     * @return Lista de versiones
     */
    public List<Map<String, Object>> getMemoryHistory(String agentId, String namespace, String key) {
        try {
            String url = baseUrl + "/" + agentId + "/memory/history?namespace=" + namespace + "&key=" + key;
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            if (response.getBody() != null && response.getBody().containsKey("versions")) {
                return (List<Map<String, Object>>) response.getBody().get("versions");
            }
            return List.of();
        } catch (Exception e) {
            log.error("Error getting memory history for agent {}", agentId, e);
            throw new RuntimeException("Failed to get memory history: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina una memoria del agente.
     *
     * @param agentId ID del agente
     * @param namespace Namespace de la memoria
     * @param key Clave de la memoria
     */
    public void deleteMemory(String agentId, String namespace, String key) {
        try {
            String url = baseUrl + "/" + agentId + "/memory?namespace=" + namespace + "&key=" + key;
            restTemplate.delete(url);
        } catch (Exception e) {
            log.error("Error deleting memory for agent {}", agentId, e);
            throw new RuntimeException("Failed to delete memory: " + e.getMessage(), e);
        }
    }
}
