package com.codeflowx.govern.business.integrations;

import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.codeflowx.govern.entity.integrations.ExternalDataset;
import com.codeflowx.govern.entity.integrations.ExternalPlatformIntegration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Conector Microsoft Purview para consumir catálogo y linaje sin duplicar datos.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PurviewConnectorService {

    private static final String PURVIEW_SCOPE = "https://purview.azure.net/.default";

    private final ExternalIntegrationBusinessService integrationService;
    private final ObjectMapper objectMapper;
    private final WebClient.Builder webClientBuilder;

    /**
     * Sincroniza activos del catálogo Purview como datasets externos.
     */
    public List<ExternalDataset> syncCatalog(Long platformId) {
        ExternalPlatformIntegration platform = integrationService.findPlatformById(platformId);
        if (platform == null || !"PURVIEW".equalsIgnoreCase(platform.getEplplatformtype())) {
            throw new IllegalArgumentException("Plataforma Purview no encontrada");
        }
        PurviewCredentials credentials = resolveCredentials(platform);
        String token = acquireToken(credentials);
        WebClient client = webClientBuilder.clone()
            .baseUrl(String.format(Locale.ROOT, "https://%s.purview.azure.com", credentials.accountName()))
            .defaultHeader("Authorization", "Bearer " + token)
            .build();

        try {
            JsonNode requestBody = objectMapper.createObjectNode()
                .put("keywords", "*")
                .putObject("limit").put("count", 100);

            JsonNode response = client.post()
                .uri("/catalog/api/search/query?api-version=2023-09-01")
                .bodyValue(requestBody.toString())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

            List<ExternalDataset> datasets = new ArrayList<>();
            if (response != null && response.has("value") && response.get("value").isArray()) {
                for (JsonNode asset : response.get("value")) {
                    ExternalDataset dataset = upsertDataset(platform, asset);
                    datasets.add(dataset);
                }
            }
            integrationService.updateSyncStatus(platformId, "SUCCESS", null);
            return datasets;
        } catch (Exception ex) {
            log.error("Error sincronizando Purview {}", platform.getEplplatformname(), ex);
            integrationService.updateSyncStatus(platformId, "ERROR", ex.getMessage());
            throw new IllegalStateException("Error sincronizando Purview", ex);
        }
    }

    public boolean testConnection(ExternalPlatformIntegration platform) {
        try {
            PurviewCredentials credentials = resolveCredentials(platform);
            acquireToken(credentials);
            return true;
        } catch (Exception ex) {
            log.warn("Test conexión Purview falló para {}: {}", platform.getEplplatformname(), ex.getMessage());
            return false;
        }
    }

    private ExternalDataset upsertDataset(ExternalPlatformIntegration platform, JsonNode asset) {
        String guid = asset.path("id").asText(UUID.randomUUID().toString());
        String name = asset.path("name").asText("asset" + guid);
        String type = asset.path("entityType").asText("ASSET");
        String qualifiedName = asset.path("qualifiedName").asText(name);
        String externalId = String.format(Locale.ROOT, "purview://%s/%s", platform.getIduuid(), guid);

        ExternalDataset existing = integrationService.findDatasetByExternalId(platform.getIdxexternalplatform(), externalId);
        boolean isNew = existing == null;
        ExternalDataset dataset = isNew ? new ExternalDataset() : existing;
        if (isNew) {
            dataset.setPlatform(platform);
            dataset.setIduuid(UUID.randomUUID().toString());
            dataset.setExdcreatedat(Timestamp.from(Instant.now()));
        }
        dataset.setExdexternalid(externalId);
        dataset.setExdname(name);
        dataset.setExdsourcelocation(qualifiedName);
        dataset.setExdrecordcount(null);
        dataset.setExdsizebytes(null);
        dataset.setExdlastqualitycheck(Timestamp.from(Instant.now()));
        dataset.setExdupdatedat(Timestamp.from(Instant.now()));
        try {
            dataset.setExdmetadata(objectMapper.createObjectNode()
                .put("purview_guid", guid)
                .put("type", type)
                .put("qualified_name", qualifiedName)
                .put("description", asset.path("description").asText(""))
                .put("classification", asset.path("classificationText").asText(""))
                .put("data_source", asset.path("dataSourceId").asText(""))
                .toString());
        } catch (Exception ex) {
            log.warn("No fue posible serializar metadata Purview {}: {}", guid, ex.getMessage());
        }
        integrationService.saveExternalDataset(dataset);
        return dataset;
    }

    private PurviewCredentials resolveCredentials(ExternalPlatformIntegration platform) {
        JsonNode metadata = readJson(platform.getEplmetadata());
        String accountName = metadata.path("accountName").asText();
        String tenantId = metadata.path("tenantId").asText(StringUtils.trimToEmpty(platform.getEpltenantid()));
        String clientId = metadata.path("clientId").asText(StringUtils.trimToEmpty(platform.getEplclientid()));
        String clientSecret = metadata.path("clientSecret").asText(StringUtils.trimToEmpty(platform.getEplclientsecret()));
        if (StringUtils.isAnyBlank(accountName, tenantId, clientId, clientSecret)) {
            throw new IllegalStateException("Metadata Purview incompleta (accountName, tenantId, clientId, clientSecret)");
        }
        return new PurviewCredentials(accountName, tenantId, clientId, clientSecret);
    }

    private String acquireToken(PurviewCredentials credentials) {
        ClientSecretCredential credential = new ClientSecretCredentialBuilder()
            .tenantId(credentials.tenantId())
            .clientId(credentials.clientId())
            .clientSecret(credentials.clientSecret())
            .build();
        return credential.getToken(new TokenRequestContext().addScopes(PURVIEW_SCOPE))
            .blockOptional()
            .map(accessToken -> accessToken.getToken())
            .orElseThrow(() -> new IllegalStateException("No fue posible obtener token Purview"));
    }

    private JsonNode readJson(String json) {
        if (StringUtils.isBlank(json)) {
            return objectMapper.createObjectNode();
        }
        try {
            return objectMapper.readTree(json);
        } catch (Exception ex) {
            log.warn("JSON Purview inválido: {}", ex.getMessage());
            return objectMapper.createObjectNode();
        }
    }

    private record PurviewCredentials(String accountName, String tenantId, String clientId, String clientSecret) {}
}
