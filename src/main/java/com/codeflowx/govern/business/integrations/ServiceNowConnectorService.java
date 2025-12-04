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
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Conector ServiceNow ITSM para sincronizar change requests / incidentes como datasets gobernados.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceNowConnectorService {

    private final ExternalIntegrationBusinessService integrationService;
    private final ObjectMapper objectMapper;
    private final WebClient.Builder webClientBuilder;

    @Value("${govern.integrations.servicenow.timeout-ms:30000}")
    private long timeoutMs;

    public List<ExternalDataset> syncTickets(Long platformId) {
        ExternalPlatformIntegration platform = integrationService.findPlatformById(platformId);
        if (platform == null || !"SERVICENOW".equalsIgnoreCase(platform.getEplplatformtype())) {
            throw new IllegalArgumentException("Plataforma ServiceNow no encontrada");
        }
        ServiceNowCredentials credentials = resolveCredentials(platform);
        WebClient client = webClientBuilder.clone()
            .baseUrl(credentials.instanceUrl())
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + buildBasicAuth(credentials))
            .build();

        try {
            String uri = String.format(Locale.ROOT, "/api/now/table/%s?sysparm_query=%s&sysparm_limit=%d",
                credentials.table(), credentials.query(), credentials.limit());
            JsonNode response = client.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block(Duration.ofMillis(timeoutMs));

            List<ExternalDataset> synced = new ArrayList<>();
            if (response != null && response.has("result") && response.get("result").isArray()) {
                for (JsonNode ticket : response.get("result")) {
                    ExternalDataset dataset = upsertTicket(platform, ticket, credentials.table());
                    synced.add(dataset);
                }
            }
            integrationService.updateSyncStatus(platformId, "SUCCESS", null);
            log.info("ServiceNow sincronizado: {} tickets para {}", synced.size(), platform.getEplplatformname());
            return synced;
        } catch (Exception ex) {
            log.error("Error sincronizando ServiceNow {}", platform.getEplplatformname(), ex);
            integrationService.updateSyncStatus(platformId, "ERROR", ex.getMessage());
            throw new IllegalStateException("Error sincronizando ServiceNow", ex);
        }
    }

    public boolean testConnection(ExternalPlatformIntegration platform) {
        try {
            ServiceNowCredentials credentials = resolveCredentials(platform);
            WebClient client = webClientBuilder.clone()
                .baseUrl(credentials.instanceUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + buildBasicAuth(credentials))
                .build();
            client.get()
                .uri(String.format(Locale.ROOT, "/api/now/table/%s?sysparm_limit=1", credentials.table()))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block(Duration.ofMillis(timeoutMs));
            return true;
        } catch (Exception ex) {
            log.warn("Test conexión ServiceNow falló para {}: {}", platform.getEplplatformname(), ex.getMessage());
            return false;
        }
    }

    private ExternalDataset upsertTicket(ExternalPlatformIntegration platform, JsonNode ticket, String table) {
        String sysId = ticket.path("sys_id").asText(UUID.randomUUID().toString());
        String externalId = String.format(Locale.ROOT, "servicenow://%s/%s", table, sysId);
        ExternalDataset existing = integrationService.findDatasetByExternalId(platform.getIdxexternalplatform(), externalId);
        boolean isNew = existing == null;
        ExternalDataset dataset = isNew ? new ExternalDataset() : existing;
        Timestamp now = Timestamp.from(Instant.now());

        if (isNew) {
            dataset.setPlatform(platform);
            dataset.setIduuid(UUID.randomUUID().toString());
            dataset.setExdcreatedat(now);
        }

        dataset.setExdexternalid(externalId);
        dataset.setExdname(ticket.path("short_description").asText(ticket.path("number").asText(sysId)));
        dataset.setExdsourcelocation(String.format(Locale.ROOT, "%s/api/now/table/%s/%s", platform.getEplhosturl(), table, sysId));
        dataset.setExdrecordcount(null);
        dataset.setExdsizebytes(null);
        dataset.setExdlastqualitycheck(now);
        dataset.setExdmetadata(ticket.toString());
        dataset.setExdupdatedat(now);
        integrationService.saveExternalDataset(dataset);
        return dataset;
    }

    private ServiceNowCredentials resolveCredentials(ExternalPlatformIntegration platform) {
        JsonNode metadata = readJson(platform.getEplmetadata());
        String instanceUrl = metadata.path("instanceUrl").asText(StringUtils.trimToEmpty(platform.getEplhosturl()));
        String username = metadata.path("username").asText();
        String password = metadata.path("password").asText();
        String token = metadata.path("token").asText();
        String table = metadata.path("table").asText("change_request");
        String query = metadata.path("query").asText("stateNOT INclosed");
        int limit = metadata.path("limit").asInt(100);
        if (StringUtils.isAnyBlank(instanceUrl, username) ||
            (StringUtils.isBlank(password) && StringUtils.isBlank(token))) {
            throw new IllegalStateException("Metadata ServiceNow incompleta (instanceUrl, username, password/token)");
        }
        return new ServiceNowCredentials(instanceUrl, username, password, token, table, query, limit);
    }

    private String buildBasicAuth(ServiceNowCredentials credentials) {
        String secret = StringUtils.isNotBlank(credentials.password()) ? credentials.password() : credentials.token();
        String raw = credentials.username() + ":" + secret;
        return Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    private JsonNode readJson(String json) {
        if (StringUtils.isBlank(json)) {
            return objectMapper.createObjectNode();
        }
        try {
            return objectMapper.readTree(json);
        } catch (Exception ex) {
            log.warn("JSON ServiceNow inválido: {}", ex.getMessage());
            return objectMapper.createObjectNode();
        }
    }

    private record ServiceNowCredentials(
        String instanceUrl,
        String username,
        String password,
        String token,
        String table,
        String query,
        int limit
    ) {}
}

