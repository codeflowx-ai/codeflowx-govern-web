package com.codeflowx.govern.business.integrations;

import com.codeflowx.govern.entity.integrations.ExternalDataset;
import com.codeflowx.govern.entity.integrations.ExternalPlatformIntegration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Conector Jira Cloud para sincronizar issues de gobernanza (riesgos, tareas de aprobación).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JiraConnectorService {

    private final ExternalIntegrationBusinessService integrationService;
    private final ObjectMapper objectMapper;
    private final WebClient.Builder webClientBuilder;

    @Value("${govern.integrations.jira.timeout-ms:30000}")
    private long timeoutMs;

    public List<ExternalDataset> syncIssues(Long platformId) {
        ExternalPlatformIntegration platform = integrationService.findPlatformById(platformId);
        if (platform == null || !"JIRA".equalsIgnoreCase(platform.getEplplatformtype())) {
            throw new IllegalArgumentException("Plataforma Jira no encontrada");
        }
        JiraCredentials credentials = resolveCredentials(platform);
        WebClient client = webClientBuilder.clone()
            .baseUrl(credentials.baseUrl())
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + buildBasicAuth(credentials))
            .defaultHeader(HttpHeaders.ACCEPT, "application/json")
            .build();

        try {
            String jql = String.format("project=%s ORDER BY updated DESC", credentials.projectKey());
            String uri = UriComponentsBuilder.fromPath("/rest/api/3/search")
                .queryParam("jql", jql)
                .queryParam("maxResults", credentials.maxResults())
                .build(false)
                .toUriString();

            JsonNode response = client.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block(Duration.ofMillis(timeoutMs));

            List<ExternalDataset> synced = new ArrayList<>();
            if (response != null && response.has("issues") && response.get("issues").isArray()) {
                for (JsonNode issue : response.get("issues")) {
                    ExternalDataset dataset = upsertIssue(platform, issue, credentials.projectKey());
                    synced.add(dataset);
                }
            }
            integrationService.updateSyncStatus(platformId, "SUCCESS", null);
            log.info("Jira sincronizado: {} issues para {}", synced.size(), platform.getEplplatformname());
            return synced;
        } catch (Exception ex) {
            log.error("Error sincronizando Jira {}", platform.getEplplatformname(), ex);
            integrationService.updateSyncStatus(platformId, "ERROR", ex.getMessage());
            throw new IllegalStateException("Error sincronizando Jira", ex);
        }
    }

    public boolean testConnection(ExternalPlatformIntegration platform) {
        try {
            JiraCredentials credentials = resolveCredentials(platform);
            WebClient client = webClientBuilder.clone()
                .baseUrl(credentials.baseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + buildBasicAuth(credentials))
                .build();
            client.get()
                .uri("/rest/api/3/project/" + credentials.projectKey())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block(Duration.ofMillis(timeoutMs));
            return true;
        } catch (Exception ex) {
            log.warn("Test conexión Jira falló para {}: {}", platform.getEplplatformname(), ex.getMessage());
            return false;
        }
    }

    private ExternalDataset upsertIssue(ExternalPlatformIntegration platform, JsonNode issue, String projectKey) {
        String issueId = issue.path("id").asText(UUID.randomUUID().toString());
        String key = issue.path("key").asText(issueId);
        String externalId = String.format(Locale.ROOT, "jira://%s/%s", projectKey, key);
        ExternalDataset existing = integrationService.findDatasetByExternalId(platform.getIdxexternalplatform(), externalId);
        boolean isNew = existing == null;
        ExternalDataset dataset = isNew ? new ExternalDataset() : existing;
        Timestamp now = Timestamp.from(Instant.now());

        if (isNew) {
            dataset.setPlatform(platform);
            dataset.setIduuid(UUID.randomUUID().toString());
            dataset.setExdcreatedat(now);
        }

        JsonNode fields = issue.path("fields");
        dataset.setExdexternalid(externalId);
        dataset.setExdname(fields.path("summary").asText(key));
        dataset.setExdsourcelocation(String.format(Locale.ROOT, "%s/browse/%s", platform.getEplhosturl(), key));
        dataset.setExdrecordcount(null);
        dataset.setExdsizebytes(null);
        dataset.setExdlastqualitycheck(now);
        dataset.setExdmetadata(issue.toString());
        dataset.setExdupdatedat(now);
        integrationService.saveExternalDataset(dataset);
        return dataset;
    }

    private JiraCredentials resolveCredentials(ExternalPlatformIntegration platform) {
        JsonNode metadata = readJson(platform.getEplmetadata());
        String baseUrl = metadata.path("baseUrl").asText(StringUtils.trimToEmpty(platform.getEplhosturl()));
        String email = metadata.path("email").asText();
        String apiToken = metadata.path("apiToken").asText(StringUtils.trimToEmpty(platform.getEplapitoken()));
        String projectKey = metadata.path("projectKey").asText();
        int maxResults = metadata.path("maxResults").asInt(50);
        if (StringUtils.isAnyBlank(baseUrl, email, apiToken, projectKey)) {
            throw new IllegalStateException("Metadata Jira incompleta (baseUrl, email, apiToken, projectKey)");
        }
        return new JiraCredentials(baseUrl, email, apiToken, projectKey, maxResults);
    }

    private String buildBasicAuth(JiraCredentials credentials) {
        String raw = credentials.email() + ":" + credentials.apiToken();
        return Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    private JsonNode readJson(String json) {
        if (StringUtils.isBlank(json)) {
            return objectMapper.createObjectNode();
        }
        try {
            return objectMapper.readTree(json);
        } catch (Exception ex) {
            log.warn("JSON Jira inválido: {}", ex.getMessage());
            return objectMapper.createObjectNode();
        }
    }

    private record JiraCredentials(String baseUrl, String email, String apiToken, String projectKey, int maxResults) {}
}

