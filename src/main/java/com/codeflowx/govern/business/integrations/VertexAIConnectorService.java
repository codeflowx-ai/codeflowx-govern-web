package com.codeflowx.govern.business.integrations;

import com.codeflowx.govern.entity.integrations.ExternalModel;
import com.codeflowx.govern.entity.integrations.ExternalPlatformIntegration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Conector Google Vertex AI para sincronizar modelos sin mover data (solo metadata).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VertexAIConnectorService {

    private static final String GOOGLE_SCOPE = "https://www.googleapis.com/auth/cloud-platform";

    private final ExternalIntegrationBusinessService integrationService;
    private final ObjectMapper objectMapper;
    private final WebClient.Builder webClientBuilder;

    @Value("${govern.integrations.vertex.timeout-ms:30000}")
    private long timeoutMs;

    public List<ExternalModel> syncModels(Long platformId) {
        ExternalPlatformIntegration platform = integrationService.findPlatformById(platformId);
        if (platform == null || !"VERTEX_AI".equalsIgnoreCase(platform.getEplplatformtype())) {
            throw new IllegalArgumentException("Plataforma Vertex AI no encontrada");
        }
        VertexCredentials credentials = resolveCredentials(platform);
        String token = acquireToken(credentials);
        WebClient client = webClientBuilder.clone()
            .baseUrl(String.format(Locale.ROOT, "https://%s-aiplatform.googleapis.com/v1", credentials.location()))
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .build();

        try {
            String uri = String.format(Locale.ROOT, "/projects/%s/locations/%s/models", credentials.projectId(), credentials.location());
            JsonNode response = client.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block(Duration.ofMillis(timeoutMs));

            List<ExternalModel> synced = new ArrayList<>();
            if (response != null && response.has("models") && response.get("models").isArray()) {
                for (JsonNode modelNode : response.get("models")) {
                    ExternalModel model = upsertModel(platform, modelNode);
                    synced.add(model);
                }
            }
            integrationService.updateSyncStatus(platformId, "SUCCESS", null);
            log.info("Vertex AI sincronizado: {} modelos importados para {}", synced.size(), platform.getEplplatformname());
            return synced;
        } catch (Exception ex) {
            log.error("Error sincronizando Vertex AI {}", platform.getEplplatformname(), ex);
            integrationService.updateSyncStatus(platformId, "ERROR", ex.getMessage());
            throw new IllegalStateException("Error sincronizando Vertex AI", ex);
        }
    }

    public boolean testConnection(ExternalPlatformIntegration platform) {
        try {
            VertexCredentials credentials = resolveCredentials(platform);
            acquireToken(credentials);
            return true;
        } catch (Exception ex) {
            log.warn("Test conexión Vertex AI falló para {}: {}", platform.getEplplatformname(), ex.getMessage());
            return false;
        }
    }

    private ExternalModel upsertModel(ExternalPlatformIntegration platform, JsonNode modelNode) {
        String resourceName = modelNode.path("name").asText();
        String modelId = extractModelId(resourceName);
        if (StringUtils.isBlank(modelId)) {
            modelId = UUID.randomUUID().toString();
        }
        ExternalModel existing = integrationService.findExternalModel(platform.getIdxexternalplatform(), modelId);
        boolean isNew = existing == null;
        ExternalModel model = isNew ? new ExternalModel() : existing;
        Timestamp now = Timestamp.from(Instant.now());

        if (isNew) {
            model.setPlatform(platform);
            model.setIduuid(UUID.randomUUID().toString());
            model.setExmcreatedat(now);
        }

        model.setExmexternalid(modelId);
        model.setExmexternalname(modelNode.path("displayName").asText(modelId));
        model.setExmexternalversion(modelNode.path("versionId").asText(null));
        model.setExmexternalurl(resourceName);
        model.setExmlastsyncedat(now);
        model.setExmsyncstatus("SYNCED");
        model.setExmmetadata(modelNode.toString());
        integrationService.saveExternalModel(model);
        return model;
    }

    private VertexCredentials resolveCredentials(ExternalPlatformIntegration platform) {
        JsonNode metadata = readJson(platform.getEplmetadata());
        String projectId = metadata.path("projectId").asText();
        String location = metadata.path("location").asText("us-central1");
        String serviceAccountKey = metadata.path("serviceAccountKey").asText();
        if (StringUtils.isBlank(projectId)) {
            throw new IllegalStateException("Metadata Vertex AI incompleta (projectId)");
        }
        return new VertexCredentials(projectId, location, serviceAccountKey);
    }

    private String acquireToken(VertexCredentials credentials) {
        try {
            GoogleCredentials googleCredentials = loadCredentials(credentials.serviceAccountKey())
                .createScoped(List.of(GOOGLE_SCOPE));
            googleCredentials.refreshIfExpired();
            return Optional.ofNullable(googleCredentials.getAccessToken())
                .map(token -> token.getTokenValue())
                .orElseThrow(() -> new IllegalStateException("Token Vertex AI vacío"));
        } catch (Exception ex) {
            throw new IllegalStateException("No fue posible obtener token Google Vertex AI", ex);
        }
    }

    private GoogleCredentials loadCredentials(String serviceAccountKey) throws Exception {
        if (StringUtils.isBlank(serviceAccountKey)) {
            return GoogleCredentials.getApplicationDefault();
        }
        byte[] keyBytes = serviceAccountKey.trim().startsWith("{")
            ? serviceAccountKey.getBytes(StandardCharsets.UTF_8)
            : Base64.getDecoder().decode(serviceAccountKey);
        return GoogleCredentials.fromStream(new ByteArrayInputStream(keyBytes));
    }

    private String extractModelId(String resourceName) {
        if (StringUtils.isBlank(resourceName)) {
            return null;
        }
        String[] parts = resourceName.split("/");
        return parts.length > 0 ? parts[parts.length - 1] : resourceName;
    }

    private JsonNode readJson(String json) {
        if (StringUtils.isBlank(json)) {
            return objectMapper.createObjectNode();
        }
        try {
            return objectMapper.readTree(json);
        } catch (Exception ex) {
            log.warn("JSON Vertex AI inválido: {}", ex.getMessage());
            return objectMapper.createObjectNode();
        }
    }

    private record VertexCredentials(String projectId, String location, String serviceAccountKey) {}
}

