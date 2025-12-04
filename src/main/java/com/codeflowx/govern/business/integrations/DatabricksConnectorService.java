package com.codeflowx.govern.business.integrations;

import com.codeflowx.govern.entity.integrations.ExternalModel;
import com.codeflowx.govern.entity.integrations.ExternalPlatformIntegration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Conector Databricks + MLflow Registry para sincronizar modelos con CodeflowX governance.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DatabricksConnectorService {

    private final ExternalIntegrationBusinessService integrationService;
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    @Value("${govern.integrations.databricks.timeout-ms:30000}")
    private long timeoutMs;

    private static final String PLATFORM_TYPE = "DATABRICKS";

    /**
     * Sincroniza modelos registrados en Databricks MLflow Registry hacia CodeflowX.
     */
    public void syncModelsFromDatabricks(Long platformId) {
        ExternalPlatformIntegration platform = integrationService.findPlatformById(platformId);
        if (platform == null) {
            log.warn("Plataforma externa {} no encontrada", platformId);
            return;
        }
        if (!PLATFORM_TYPE.equalsIgnoreCase(platform.getEplplatformtype())) {
            log.debug("Plataforma {} no es Databricks (tipo={})", platform.getEplplatformname(), platform.getEplplatformtype());
            return;
        }
        if (!Boolean.TRUE.equals(platform.getEplsyncenabled())) {
            log.info("Plataforma Databricks {} tiene sync deshabilitado", platform.getEplplatformname());
            return;
        }
        try {
            JsonNode response = invokeListModels(platform);
            ArrayNode modelsNode = response != null && response.has("registered_models") && response.get("registered_models").isArray()
                ? (ArrayNode) response.get("registered_models")
                : objectMapper.createArrayNode();

            int processed = 0;
            Iterator<JsonNode> iterator = modelsNode.iterator();
            while (iterator.hasNext()) {
                JsonNode modelNode = iterator.next();
                processModel(platform, modelNode);
                processed++;
            }

            integrationService.updateSyncStatus(platformId, "SUCCESS", null);
            log.info("Sincronización Databricks completada para {}: {} modelos procesados", platform.getEplplatformname(), processed);
        } catch (Exception ex) {
            log.error("Error sincronizando Databricks {}", platform.getEplplatformname(), ex);
            integrationService.updateSyncStatus(platformId, "ERROR", ex.getMessage());
            throw new IllegalStateException("Error sincronizando modelos Databricks", ex);
        }
    }

    /**
     * Notifica el estado de aprobación al Registry de Databricks usando tags y transición de stage.
     */
    public void notifyApprovalToDatabricks(Long externalModelId, String approvalStatus) {
        ExternalModel externalModel = integrationService.findExternalModelById(externalModelId);
        if (externalModel == null) {
            throw new IllegalArgumentException("Modelo externo no encontrado: " + externalModelId);
        }
        ExternalPlatformIntegration platform = externalModel.getPlatform();
        if (platform == null) {
            throw new IllegalStateException("La plataforma asociada al modelo externo es nula");
        }
        WebClient client = buildClient(platform);

        String modelName = externalModel.getExmexternalname();
        String version = Optional.ofNullable(externalModel.getExmexternalversion()).orElse("1");
        String riskLevels = Optional.ofNullable(externalModel.getExmrisklevel()).orElse("[\"LIMITED_RISK\"]");

        try {
            // Tag approval
            client.post()
                .uri("/api/2.0/mlflow/registered-models/set-tag")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.createObjectNode()
                    .put("name", modelName)
                    .put("key", "codeflowx_approval")
                    .put("value", approvalStatus))
                .retrieve()
                .bodyToMono(Void.class)
                .block(Duration.ofMillis(timeoutMs));

            // Tag risk levels
            client.post()
                .uri("/api/2.0/mlflow/registered-models/set-tag")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.createObjectNode()
                    .put("name", modelName)
                    .put("key", "codeflowx_risk_level")
                    .put("value", riskLevels))
                .retrieve()
                .bodyToMono(Void.class)
                .block(Duration.ofMillis(timeoutMs));

            // Transition stage
            String targetStage = "APPROVED".equalsIgnoreCase(approvalStatus) ? "Production" : "Archived";
            client.post()
                .uri("/api/2.0/mlflow/model-versions/transition-stage")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.createObjectNode()
                    .put("name", modelName)
                    .put("version", version)
                    .put("stage", targetStage)
                    .put("archive_existing_versions", false))
                .retrieve()
                .bodyToMono(Void.class)
                .block(Duration.ofMillis(timeoutMs));

            log.info("Databricks notificado para modelo {} v{} → {}", modelName, version, targetStage);
        } catch (Exception ex) {
            log.error("Error notificando aprobación Databricks para {}", modelName, ex);
            throw new IllegalStateException("No fue posible notificar aprobación a Databricks", ex);
        }
    }

    /**
     * Test de conectividad con Databricks (verifica credenciales y host).
     */
    public boolean testConnection(ExternalPlatformIntegration platform) {
        if (platform == null || !PLATFORM_TYPE.equalsIgnoreCase(platform.getEplplatformtype())) {
            return false;
        }
        try {
            JsonNode response = buildClient(platform)
                .get()
                .uri("/api/2.0/mlflow/experiments/list")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block(Duration.ofMillis(timeoutMs));
            return response != null;
        } catch (Exception ex) {
            log.warn("Test conexión Databricks falló para {}: {}", platform.getEplplatformname(), ex.getMessage());
            return false;
        }
    }

    private JsonNode invokeListModels(ExternalPlatformIntegration platform) {
        return buildClient(platform)
            .get()
            .uri(uriBuilder -> uriBuilder
                .path("/api/2.0/mlflow/registered-models/list")
                .queryParam("max_results", 200)
                .build())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(JsonNode.class)
            .block(Duration.ofMillis(timeoutMs));
    }

    private void processModel(ExternalPlatformIntegration platform, JsonNode modelNode) {
        String modelName = modelNode.path("name").asText();
        if (StringUtils.isBlank(modelName)) {
            return;
        }
        JsonNode latestVersions = modelNode.path("latest_versions");
        JsonNode selectedVersion = latestVersions.isArray() && latestVersions.size() > 0 ? latestVersions.get(0) : null;
        String version = selectedVersion != null ? selectedVersion.path("version").asText(null) : null;
        String externalId = version != null ? modelName + ":" + version : modelName;

        ExternalModel existing = integrationService.findExternalModel(platform.getIdxexternalplatform(), externalId);
        boolean isNew = existing == null;
        ExternalModel model = isNew ? new ExternalModel() : existing;

        if (isNew) {
            model.setPlatform(platform);
            model.setIduuid(UUID.randomUUID().toString());
            model.setExmcreatedat(new Timestamp(System.currentTimeMillis()));
            model.setExmapprovalstatus("PENDING");
        }

        model.setExmexternalid(externalId);
        model.setExmexternalname(modelName);
        model.setExmexternalversion(version);
        model.setExmexternalurl(buildModelUrl(platform.getEplhosturl(), modelName));
        model.setExmsyncstatus("SYNCED");
        model.setExmlastsyncedat(new Timestamp(System.currentTimeMillis()));
        model.setExmmetadata(writeMetadata(modelNode));
        model.setExmrisklevel(resolveRiskLevel(selectedVersion));

        integrationService.saveExternalModel(model);
    }

    private WebClient buildClient(ExternalPlatformIntegration platform) {
        String host = StringUtils.trimToEmpty(platform.getEplhosturl());
        if (!host.startsWith("http://") && !host.startsWith("https://")) {
            host = "https://" + host;
        }
        String token = StringUtils.trimToEmpty(platform.getEplapitoken());
        if (StringUtils.isBlank(token)) {
            throw new IllegalStateException("La plataforma Databricks no tiene token configurado");
        }
        WebClient.Builder builder = webClientBuilder.clone();
        builder.baseUrl(host);
        builder.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        builder.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        builder.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return builder.build();
    }

    private String resolveRiskLevel(JsonNode latestVersion) {
        try {
            if (latestVersion != null && latestVersion.has("tags")) {
                JsonNode tags = latestVersion.get("tags");
                if (tags.isArray()) {
                    for (JsonNode tag : tags) {
                        String key = tag.path("key").asText();
                        if ("codeflowx_risk_level".equalsIgnoreCase(key)) {
                            String value = tag.path("value").asText();
                            if (StringUtils.isNotBlank(value)) {
                                return value;
                            }
                        }
                    }
                }
            }
            if (latestVersion != null) {
                String stage = latestVersion.path("current_stage").asText();
                if ("Production".equalsIgnoreCase(stage)) {
                    return objectMapper.writeValueAsString(List.of("HIGH_RISK"));
                }
            }
            return objectMapper.writeValueAsString(List.of("LIMITED_RISK"));
        } catch (Exception ex) {
            log.warn("No fue posible calcular riesgo Databricks: {}", ex.getMessage());
            return "[\"LIMITED_RISK\"]";
        }
    }

    private String writeMetadata(JsonNode node) {
        try {
            return objectMapper.writeValueAsString(node);
        } catch (Exception ex) {
            log.warn("No fue posible serializar metadata Databricks: {}", ex.getMessage());
            return "{}";
        }
    }

    private String buildModelUrl(String host, String modelName) {
        if (StringUtils.isBlank(host)) {
            return null;
        }
        String base = host.endsWith("/") ? host.substring(0, host.length() - 1) : host;
        return String.format("%s/#mlflow/models/%s", base, modelName);
    }
}
