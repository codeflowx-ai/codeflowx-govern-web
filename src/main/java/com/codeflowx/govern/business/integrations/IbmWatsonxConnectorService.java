package com.codeflowx.govern.business.integrations;

import com.codeflowx.govern.entity.integrations.ExternalModel;
import com.codeflowx.govern.entity.integrations.ExternalPlatformIntegration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Conector IBM watsonx.ai / Watson Machine Learning.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IbmWatsonxConnectorService {

    private static final String IAM_TOKEN_URL = "https://iam.cloud.ibm.com/identity/token";

    private final ExternalIntegrationBusinessService integrationService;
    private final ObjectMapper objectMapper;
    private final WebClient.Builder webClientBuilder;

    @Value("${govern.integrations.ibm.timeout-ms:30000}")
    private long timeoutMs;

    public List<ExternalModel> syncModels(Long platformId) {
        ExternalPlatformIntegration platform = integrationService.findPlatformById(platformId);
        if (platform == null || !"IBM_WATSONX".equalsIgnoreCase(platform.getEplplatformtype())) {
            throw new IllegalArgumentException("Plataforma IBM watsonx no encontrada");
        }
        WatsonCredentials credentials = resolveCredentials(platform);
        String token = acquireToken(credentials);
        WebClient client = webClientBuilder.clone()
            .baseUrl(credentials.instanceUrl())
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .defaultHeader("ml-instance-id", credentials.instanceId())
            .build();

        try {
            String query = String.format(Locale.ROOT, "/ml/v4/models?project_id=%s&version=%s",
                urlEncode(credentials.projectId()), urlEncode(credentials.apiVersion()));
            JsonNode response = client.get()
                .uri(query)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block(Duration.ofMillis(timeoutMs));

            List<ExternalModel> synced = new ArrayList<>();
            if (response != null && response.has("resources") && response.get("resources").isArray()) {
                for (JsonNode resource : response.get("resources")) {
                    ExternalModel model = upsertModel(platform, resource);
                    synced.add(model);
                }
            }
            integrationService.updateSyncStatus(platformId, "SUCCESS", null);
            log.info("IBM watsonx sincronizado: {} modelos para {}", synced.size(), platform.getEplplatformname());
            return synced;
        } catch (Exception ex) {
            log.error("Error sincronizando IBM watsonx {}", platform.getEplplatformname(), ex);
            integrationService.updateSyncStatus(platformId, "ERROR", ex.getMessage());
            throw new IllegalStateException("Error sincronizando IBM watsonx", ex);
        }
    }

    public boolean testConnection(ExternalPlatformIntegration platform) {
        try {
            WatsonCredentials credentials = resolveCredentials(platform);
            acquireToken(credentials);
            return true;
        } catch (Exception ex) {
            log.warn("Test conexión IBM watsonx falló para {}: {}", platform.getEplplatformname(), ex.getMessage());
            return false;
        }
    }

    private ExternalModel upsertModel(ExternalPlatformIntegration platform, JsonNode resource) {
        JsonNode metadata = resource.path("metadata");
        String modelId = metadata.path("id").asText(UUID.randomUUID().toString());
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
        model.setExmexternalname(metadata.path("name").asText(modelId));
        model.setExmexternalversion(metadata.path("asset").path("version").asText(null));
        model.setExmexternalurl(metadata.path("href").asText(null));
        model.setExmlastsyncedat(now);
        model.setExmsyncstatus("SYNCED");
        model.setExmmetadata(resource.toString());
        integrationService.saveExternalModel(model);
        return model;
    }

    private String acquireToken(WatsonCredentials credentials) {
        WebClient client = webClientBuilder.clone()
            .baseUrl(IAM_TOKEN_URL)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .build();

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "urn:ibm:params:oauth:grant-type:apikey");
        form.add("apikey", credentials.apiKey());

        JsonNode response = client.post()
            .bodyValue(form)
            .retrieve()
            .bodyToMono(JsonNode.class)
            .block(Duration.ofMillis(timeoutMs));

        return Optional.ofNullable(response)
            .map(node -> node.path("access_token").asText(null))
            .filter(StringUtils::isNotBlank)
            .orElseThrow(() -> new IllegalStateException("No fue posible obtener token IBM IAM"));
    }

    private WatsonCredentials resolveCredentials(ExternalPlatformIntegration platform) {
        JsonNode metadata = readJson(platform.getEplmetadata());
        String instanceUrl = metadata.path("instanceUrl").asText();
        String instanceId = metadata.path("instanceId").asText();
        String projectId = metadata.path("projectId").asText();
        String apiKey = metadata.path("apiKey").asText(StringUtils.trimToEmpty(platform.getEplapitoken()));
        String apiVersion = metadata.path("apiVersion").asText("2023-10-01");
        if (StringUtils.isAnyBlank(instanceUrl, instanceId, projectId, apiKey)) {
            throw new IllegalStateException("Metadata IBM watsonx incompleta (instanceUrl, instanceId, projectId, apiKey)");
        }
        return new WatsonCredentials(instanceUrl, instanceId, projectId, apiKey, apiVersion);
    }

    private JsonNode readJson(String json) {
        if (StringUtils.isBlank(json)) {
            return objectMapper.createObjectNode();
        }
        try {
            return objectMapper.readTree(json);
        } catch (Exception ex) {
            log.warn("JSON watsonx inválido: {}", ex.getMessage());
            return objectMapper.createObjectNode();
        }
    }

    private String urlEncode(String value) {
        return URLEncoder.encode(StringUtils.defaultString(value), StandardCharsets.UTF_8);
    }

    private record WatsonCredentials(String instanceUrl, String instanceId, String projectId, String apiKey, String apiVersion) {}
}

