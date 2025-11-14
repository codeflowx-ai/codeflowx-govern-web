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
 * Conector Microsoft Fabric para sincronizar workspaces, lakehouses y pipelines como datasets gobernados.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FabricConnectorService {

    private static final String FABRIC_SCOPE = "https://analysis.windows.net/powerbi/api/.default";

    private final ExternalIntegrationBusinessService integrationService;
    private final ObjectMapper objectMapper;
    private final WebClient.Builder webClientBuilder;

    public List<ExternalDataset> syncFabricAssets(Long platformId) {
        ExternalPlatformIntegration platform = integrationService.findPlatformById(platformId);
        if (platform == null || !"FABRIC".equalsIgnoreCase(platform.getEplplatformtype())) {
            throw new IllegalArgumentException("Plataforma Fabric no encontrada");
        }
        FabricCredentials credentials = resolveCredentials(platform);
        String token = acquireToken(credentials);
        WebClient client = webClientBuilder.clone()
            .baseUrl(credentials.apiBase())
            .defaultHeader("Authorization", "Bearer " + token)
            .build();

        try {
            JsonNode workspaces = client.get()
                .uri("/workspaces")
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

            List<ExternalDataset> datasets = new ArrayList<>();
            if (workspaces != null && workspaces.has("value")) {
                for (JsonNode workspace : workspaces.get("value")) {
                    String workspaceId = workspace.path("id").asText();
                    String workspaceName = workspace.path("displayName").asText();
                    datasets.add(syncWorkspaceAsset(platform, workspace, "FABRIC_WORKSPACE"));

                    JsonNode items = client.get()
                        .uri(String.format(Locale.ROOT, "/workspaces/%s/items", workspaceId))
                        .retrieve()
                        .bodyToMono(JsonNode.class)
                        .block();
                    if (items != null && items.has("value")) {
                        for (JsonNode item : items.get("value")) {
                            datasets.add(syncFabricItem(platform, workspaceName, item));
                        }
                    }
                }
            }
            integrationService.updateSyncStatus(platformId, "SUCCESS", null);
            return datasets;
        } catch (Exception ex) {
            log.error("Error sincronizando Fabric {}", platform.getEplplatformname(), ex);
            integrationService.updateSyncStatus(platformId, "ERROR", ex.getMessage());
            throw new IllegalStateException("Error sincronizando Microsoft Fabric", ex);
        }
    }

    public boolean testConnection(ExternalPlatformIntegration platform) {
        try {
            FabricCredentials credentials = resolveCredentials(platform);
            acquireToken(credentials);
            return true;
        } catch (Exception ex) {
            log.warn("Test conexión Fabric falló para {}: {}", platform.getEplplatformname(), ex.getMessage());
            return false;
        }
    }

    private ExternalDataset syncWorkspaceAsset(ExternalPlatformIntegration platform, JsonNode workspace, String type) {
        String workspaceId = workspace.path("id").asText();
        String name = workspace.path("displayName").asText("Workspace" + workspaceId);
        String externalId = String.format(Locale.ROOT, "fabric://workspace/%s", workspaceId);

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
        dataset.setExdsourcelocation(externalId);
        dataset.setExdupdatedat(Timestamp.from(Instant.now()));
        dataset.setExdmetadata(workspace.toString());
        integrationService.saveExternalDataset(dataset);
        return dataset;
    }

    private ExternalDataset syncFabricItem(ExternalPlatformIntegration platform, String workspaceName, JsonNode item) {
        String itemId = item.path("id").asText(UUID.randomUUID().toString());
        String itemType = item.path("type").asText("Unknown");
        String name = item.path("displayName").asText(itemId);
        String externalId = String.format(Locale.ROOT, "fabric://item/%s", itemId);

        ExternalDataset existing = integrationService.findDatasetByExternalId(platform.getIdxexternalplatform(), externalId);
        boolean isNew = existing == null;
        ExternalDataset dataset = isNew ? new ExternalDataset() : existing;
        if (isNew) {
            dataset.setPlatform(platform);
            dataset.setIduuid(UUID.randomUUID().toString());
            dataset.setExdcreatedat(Timestamp.from(Instant.now()));
        }
        dataset.setExdexternalid(externalId);
        dataset.setExdname(name + " (" + itemType + ")");
        dataset.setExdsourcelocation(String.format(Locale.ROOT, "Workspace:%s", workspaceName));
        dataset.setExdupdatedat(Timestamp.from(Instant.now()));
        dataset.setExdmetadata(item.toString());
        integrationService.saveExternalDataset(dataset);
        return dataset;
    }

    private FabricCredentials resolveCredentials(ExternalPlatformIntegration platform) {
        JsonNode metadata = readJson(platform.getEplmetadata());
        String tenantId = metadata.path("tenantId").asText(StringUtils.trimToEmpty(platform.getEpltenantid()));
        String clientId = metadata.path("clientId").asText(StringUtils.trimToEmpty(platform.getEplclientid()));
        String clientSecret = metadata.path("clientSecret").asText(StringUtils.trimToEmpty(platform.getEplclientsecret()));
        String apiBase = metadata.path("apiBase").asText("https://api.fabric.microsoft.com/v1");
        if (StringUtils.isAnyBlank(tenantId, clientId, clientSecret)) {
            throw new IllegalStateException("Metadata Fabric incompleta (tenantId, clientId, clientSecret)");
        }
        return new FabricCredentials(tenantId, clientId, clientSecret, apiBase);
    }

    private String acquireToken(FabricCredentials credentials) {
        ClientSecretCredential credential = new ClientSecretCredentialBuilder()
            .tenantId(credentials.tenantId())
            .clientId(credentials.clientId())
            .clientSecret(credentials.clientSecret())
            .build();
        return credential.getToken(new TokenRequestContext().addScopes(FABRIC_SCOPE))
            .blockOptional()
            .map(accessToken -> accessToken.getToken())
            .orElseThrow(() -> new IllegalStateException("No fue posible obtener token Microsoft Fabric"));
    }

    private JsonNode readJson(String json) {
        if (StringUtils.isBlank(json)) {
            return objectMapper.createObjectNode();
        }
        try {
            return objectMapper.readTree(json);
        } catch (Exception ex) {
            log.warn("JSON Fabric inválido: {}", ex.getMessage());
            return objectMapper.createObjectNode();
        }
    }

    private record FabricCredentials(String tenantId, String clientId, String clientSecret, String apiBase) {}
}
