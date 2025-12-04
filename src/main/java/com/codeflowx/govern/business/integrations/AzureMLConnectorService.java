package com.codeflowx.govern.business.integrations;

import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.monitor.query.MetricsQueryClient;
import com.azure.monitor.query.MetricsQueryClientBuilder;
import com.azure.monitor.query.models.Metric;
import com.azure.monitor.query.models.MetricValue;
import com.azure.monitor.query.models.MetricsQueryResult;
import com.codeflowx.govern.entity.integrations.ExternalModel;
import com.codeflowx.govern.entity.integrations.ExternalPlatformIntegration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Conector Azure ML para monitorear despliegues online y detectar degradaciones (Art. 72).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AzureMLConnectorService {

    private static final String MANAGEMENT_SCOPE = "https://management.azure.com/.default";
    private static final String AZURE_ML_API_VERSION = "2023-04-01";

    private final ExternalIntegrationBusinessService integrationService;
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    /**
     * Lista despliegues online configurados en Azure ML Workspace.
     */
    public List<AzureDeploymentSummary> listDeployments(Long platformId) {
        ExternalPlatformIntegration platform = requireAzurePlatform(platformId);
        AzureCredentials credentials = resolveCredentials(platform);
        String token = acquireToken(credentials);
        String baseUrl = String.format(Locale.ROOT,
            "https://management.azure.com/subscriptions/%s/resourceGroups/%s/providers/Microsoft.MachineLearningServices/workspaces/%s",
            credentials.subscriptionId(), credentials.resourceGroup(), credentials.workspaceName());

        try {
            JsonNode response = buildClient(token)
                .get()
                .uri(baseUrl + "/onlineEndpoints?api-version=" + AZURE_ML_API_VERSION)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block(Duration.ofSeconds(30));

            if (response == null || !response.has("value")) {
                return Collections.emptyList();
            }
            List<AzureDeploymentSummary> results = new ArrayList<>();
            for (JsonNode endpoint : response.get("value")) {
                String endpointName = endpoint.path("name").asText();
                JsonNode deployments = endpoint.path("properties").path("deployments");
                if (deployments.isArray()) {
                    for (JsonNode deployment : deployments) {
                        results.add(new AzureDeploymentSummary(
                            endpointName,
                            deployment.path("name").asText(),
                            deployment.path("properties").path("modelId").asText(null),
                            deployment.path("properties").path("provisioningState").asText(null)
                        ));
                    }
                }
            }
            return results;
        } catch (Exception ex) {
            log.error("Error listando deployments Azure ML para {}", platform.getEplplatformname(), ex);
            throw new IllegalStateException("No fue posible listar deployments Azure ML", ex);
        }
    }

    /**
     * Obtiene métricas de latencia y throughput del deployment.
     */
    public AzureDeploymentMetrics monitorDeployment(Long platformId, String endpointName, String deploymentName, int days) {
        ExternalPlatformIntegration platform = requireAzurePlatform(platformId);
        AzureCredentials credentials = resolveCredentials(platform);
        MetricsQueryClient metricsClient = buildMetricsClient(credentials);
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime from = now.minusDays(days > 0 ? days : 7);

        String resourceId = String.format(Locale.ROOT,
            "/subscriptions/%s/resourceGroups/%s/providers/Microsoft.MachineLearningServices/workspaces/%s/onlineEndpoints/%s/deployments/%s",
            credentials.subscriptionId(), credentials.resourceGroup(), credentials.workspaceName(), endpointName, deploymentName);

        try {
            MetricsQueryResult result = metricsClient.queryResource(
                resourceId,
                List.of("RequestLatency", "RequestsPerSecond"),
                from,
                null,
                Duration.ofHours(1)
            );

            double latencyP95 = extractPercentile(result.getMetrics(), "RequestLatency", 95);
            double rpsAvg = extractAverage(result.getMetrics(), "RequestsPerSecond");

            return new AzureDeploymentMetrics(latencyP95, rpsAvg, from, now);
        } catch (Exception ex) {
            log.error("Error consultando métricas Azure ML {} / {}", endpointName, deploymentName, ex);
            throw new IllegalStateException("No fue posible consultar métricas Azure ML", ex);
        }
    }

    /**
     * Detecta drift comparando métricas actuales frente a baseline.
     */
    public DriftDetectionResult detectDrift(AzureDeploymentMetrics current, AzureDeploymentMetrics baseline, double latencyThresholdPct) {
        double baselineLatency = Optional.ofNullable(baseline).map(AzureDeploymentMetrics::latencyP95).orElse(0.0);
        double currentLatency = Optional.ofNullable(current).map(AzureDeploymentMetrics::latencyP95).orElse(0.0);
        double increase = 0.0;
        boolean drift = false;
        if (baselineLatency > 0 && currentLatency > 0) {
            increase = ((currentLatency - baselineLatency) / baselineLatency) * 100.0;
            drift = increase > latencyThresholdPct;
        }
        return new DriftDetectionResult(drift, increase, drift ? "LATENCY_DEGRADATION" : "OK");
    }

    /**
     * Notifica a Azure ML que un modelo externo cambió de estado (APPROVED/REJECTED).
     */
    public void tagDeploymentWithApproval(Long externalModelId, String approvalStatus) {
        ExternalModel externalModel = integrationService.findExternalModelById(externalModelId);
        if (externalModel == null) {
            throw new IllegalArgumentException("Modelo externo no encontrado: " + externalModelId);
        }
        ExternalPlatformIntegration platform = Optional.ofNullable(externalModel.getPlatform())
            .orElseThrow(() -> new IllegalStateException("Modelo sin plataforma asociada"));
        if (!"AZURE_ML".equalsIgnoreCase(platform.getEplplatformtype())) {
            throw new IllegalArgumentException("El modelo no pertenece a una plataforma Azure ML");
        }
        AzureCredentials credentials = resolveCredentials(platform);
        String token = acquireToken(credentials);
        String endpointName = Optional.ofNullable(externalModel.getExmmetadata())
            .map(meta -> readJson(meta).path("endpoint_name").asText(null))
            .orElse(null);
        String deploymentName = Optional.ofNullable(externalModel.getExmmetadata())
            .map(meta -> readJson(meta).path("deployment_name").asText(null))
            .orElse(externalModel.getExmexternalname());

        if (StringUtils.isAnyBlank(endpointName, deploymentName)) {
            log.warn("No se encontraron metadatos de endpoint/deployment en modelo externo {}", externalModelId);
            return;
        }

        String baseUrl = String.format(Locale.ROOT,
            "https://management.azure.com/subscriptions/%s/resourceGroups/%s/providers/Microsoft.MachineLearningServices/workspaces/%s/onlineEndpoints/%s/deployments/%s?api-version=%s",
            credentials.subscriptionId(), credentials.resourceGroup(), credentials.workspaceName(), endpointName, deploymentName, AZURE_ML_API_VERSION);

        try {
            ObjectMapper mapper = objectMapper;
            JsonNode payload = mapper.createObjectNode()
                .putObject("tags")
                .put("codeflowx_approval", approvalStatus);

            buildClient(token)
                .patch()
                .uri(baseUrl)
                .bodyValue(mapper.writeValueAsString(payload))
                .retrieve()
                .bodyToMono(Void.class)
                .block(Duration.ofSeconds(30));
        } catch (Exception ex) {
            log.warn("Error etiquetando deployment Azure ML {}: {}", deploymentName, ex.getMessage());
        }
    }

    private ExternalPlatformIntegration requireAzurePlatform(Long platformId) {
        ExternalPlatformIntegration platform = integrationService.findPlatformById(platformId);
        if (platform == null) {
            throw new IllegalArgumentException("Plataforma Azure ML no encontrada: " + platformId);
        }
        if (!"AZURE_ML".equalsIgnoreCase(platform.getEplplatformtype())) {
            throw new IllegalArgumentException("La plataforma no es de tipo AZURE_ML");
        }
        return platform;
    }

    private AzureCredentials resolveCredentials(ExternalPlatformIntegration platform) {
        JsonNode metadata = readJson(platform.getEplmetadata());
        String subscription = metadata.path("subscriptionId").asText();
        String resourceGroup = metadata.path("resourceGroup").asText();
        String workspace = metadata.path("workspaceName").asText();
        String tenant = metadata.path("tenantId").asText(StringUtils.trimToEmpty(platform.getEpltenantid()));
        String clientId = metadata.path("clientId").asText(StringUtils.trimToEmpty(platform.getEplclientid()));
        String clientSecret = metadata.path("clientSecret").asText(StringUtils.trimToEmpty(platform.getEplclientsecret()));
        if (StringUtils.isAnyBlank(subscription, resourceGroup, workspace, tenant, clientId, clientSecret)) {
            throw new IllegalStateException("Metadata Azure ML incompleta (subscriptionId, resourceGroup, workspaceName, tenantId, clientId, clientSecret obligatorios)");
        }
        return new AzureCredentials(subscription, resourceGroup, workspace, tenant, clientId, clientSecret);
    }

    private String acquireToken(AzureCredentials credentials) {
        ClientSecretCredential credential = new ClientSecretCredentialBuilder()
            .tenantId(credentials.tenantId())
            .clientId(credentials.clientId())
            .clientSecret(credentials.clientSecret())
            .build();
        return credential.getToken(new TokenRequestContext().addScopes(MANAGEMENT_SCOPE))
            .blockOptional(Duration.ofSeconds(30))
            .map(accessToken -> accessToken.getToken())
            .orElseThrow(() -> new IllegalStateException("No fue posible obtener token Azure"));
    }

    private MetricsQueryClient buildMetricsClient(AzureCredentials credentials) {
        ClientSecretCredential credential = new ClientSecretCredentialBuilder()
            .tenantId(credentials.tenantId())
            .clientId(credentials.clientId())
            .clientSecret(credentials.clientSecret())
            .build();
        return new MetricsQueryClientBuilder()
            .credential(credential)
            .buildClient();
    }

    private WebClient buildClient(String bearerToken) {
        return webClientBuilder.clone()
            .defaultHeader("Authorization", "Bearer " + bearerToken)
            .build();
    }

    private double extractPercentile(List<Metric> metrics, String metricName, int percentile) {
        return metrics.stream()
            .filter(metric -> metric.getName().equalsIgnoreCase(metricName))
            .flatMap(metric -> metric.getTimeseries().stream())
            .flatMap(series -> series.getValues().stream())
            .map(MetricValue::getPercentile95)
            .filter(value -> value != null)
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);
    }

    private double extractAverage(List<Metric> metrics, String metricName) {
        return metrics.stream()
            .filter(metric -> metric.getName().equalsIgnoreCase(metricName))
            .flatMap(metric -> metric.getTimeseries().stream())
            .flatMap(series -> series.getValues().stream())
            .map(MetricValue::getAverage)
            .filter(value -> value != null)
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);
    }

    private JsonNode readJson(String json) {
        if (StringUtils.isBlank(json)) {
            return objectMapper.createObjectNode();
        }
        try {
            return objectMapper.readTree(json);
        } catch (Exception ex) {
            log.warn("JSON inválido para metadata Azure ML: {}", ex.getMessage());
            return objectMapper.createObjectNode();
        }
    }

    public record AzureDeploymentSummary(String endpointName, String deploymentName, String modelId, String provisioningState) {}

    public record AzureDeploymentMetrics(double latencyP95, double requestsPerSecondAvg, OffsetDateTime from, OffsetDateTime to) {}

    public record DriftDetectionResult(boolean driftDetected, double latencyIncreasePct, String status) {}

    private record AzureCredentials(String subscriptionId, String resourceGroup, String workspaceName,
                                    String tenantId, String clientId, String clientSecret) {}
}
