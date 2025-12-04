package com.codeflowx.govern.business.integrations;

import com.codeflowx.govern.entity.integrations.ExternalPlatformIntegration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.Datapoint;
import software.amazon.awssdk.services.cloudwatch.model.Dimension;
import software.amazon.awssdk.services.cloudwatch.model.GetMetricStatisticsRequest;
import software.amazon.awssdk.services.cloudwatch.model.GetMetricStatisticsResponse;
import software.amazon.awssdk.services.sagemaker.SageMakerClient;
import software.amazon.awssdk.services.sagemaker.model.DescribeEndpointRequest;
import software.amazon.awssdk.services.sagemaker.model.ListEndpointsRequest;
import software.amazon.awssdk.services.sagemaker.model.ListEndpointsResponse;
import software.amazon.awssdk.services.sagemaker.model.SageMakerException;

/**
 * Conector AWS SageMaker para monitoreo Art. 72.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SageMakerConnectorService {

    private final ExternalIntegrationBusinessService integrationService;
    private final ObjectMapper objectMapper;

    /**
     * Lista endpoints activos en una cuenta SageMaker.
     */
    public List<SageMakerEndpointSummary> listEndpoints(Long platformId) {
        ExternalPlatformIntegration platform = requireSageMakerPlatform(platformId);
        AwsCredentials credentials = resolveCredentials(platform);
        try (SageMakerClient client = buildSageMakerClient(credentials)) {
            ListEndpointsResponse response = client.listEndpoints(ListEndpointsRequest.builder().maxResults(100).build());
            List<SageMakerEndpointSummary> endpoints = new ArrayList<>();
            response.endpoints().forEach(endpoint -> {
                endpoints.add(new SageMakerEndpointSummary(
                    endpoint.endpointName(),
                    endpoint.endpointStatusAsString(),
                    endpoint.creationTime(),
                    describeProductionVariants(client, endpoint.endpointName())
                ));
            });
            return endpoints;
        } catch (SageMakerException ex) {
            log.error("Error listando endpoints SageMaker", ex);
            throw new IllegalStateException("No fue posible listar endpoints SageMaker", ex);
        }
    }

    /**
     * Obtiene métricas CloudWatch (latencia, invocaciones) de un endpoint SageMaker.
     */
    public SageMakerMetrics monitorEndpoint(Long platformId, String endpointName, int hours) {
        ExternalPlatformIntegration platform = requireSageMakerPlatform(platformId);
        AwsCredentials credentials = resolveCredentials(platform);
        Instant endTime = Instant.now();
        Instant startTime = endTime.minusSeconds(hours > 0 ? hours * 3600L : 24 * 3600L);

        try (CloudWatchClient cloudWatch = buildCloudWatchClient(credentials)) {
            double avgLatency = queryMetric(cloudWatch, endpointName, "ModelLatency", startTime, endTime, "Average");
            double maxLatency = queryMetric(cloudWatch, endpointName, "ModelLatency", startTime, endTime, "Maximum");
            double invocations = queryMetric(cloudWatch, endpointName, "Invocations", startTime, endTime, "Sum");
            return new SageMakerMetrics(avgLatency, maxLatency, invocations, startTime, endTime);
        } catch (Exception ex) {
            log.error("Error consultando métricas SageMaker {}", endpointName, ex);
            throw new IllegalStateException("No fue posible consultar métricas SageMaker", ex);
        }
    }

    public DriftDetectionResult detectAnomaly(SageMakerMetrics current, SageMakerMetrics baseline, double latencyThresholdPct) {
        double baselineLatency = Optional.ofNullable(baseline).map(SageMakerMetrics::averageLatencyMs).orElse(0.0);
        double currentLatency = Optional.ofNullable(current).map(SageMakerMetrics::averageLatencyMs).orElse(0.0);
        double increase = 0.0;
        boolean drift = false;
        if (baselineLatency > 0 && currentLatency > 0) {
            increase = ((currentLatency - baselineLatency) / baselineLatency) * 100.0;
            drift = increase > latencyThresholdPct;
        }
        return new DriftDetectionResult(drift, increase, drift ? "LATENCY_DEGRADATION" : "OK");
    }

    private String describeProductionVariants(SageMakerClient client, String endpointName) {
        try {
            return objectMapper.writeValueAsString(
                client.describeEndpoint(DescribeEndpointRequest.builder().endpointName(endpointName).build())
                    .productionVariants()
            );
        } catch (Exception ex) {
            log.warn("No fue posible serializar variantes SageMaker {}: {}", endpointName, ex.getMessage());
            return "[]";
        }
    }

    private double queryMetric(CloudWatchClient cloudWatch, String endpointName, String metricName,
                               Instant startTime, Instant endTime, String statistic) {
        GetMetricStatisticsRequest request = GetMetricStatisticsRequest.builder()
            .namespace("AWS/SageMaker")
            .metricName(metricName)
            .dimensions(Dimension.builder().name("EndpointName").value(endpointName).build())
            .startTime(startTime)
            .endTime(endTime)
            .period(300)
            .statistics(statistic)
            .build();
        GetMetricStatisticsResponse response = cloudWatch.getMetricStatistics(request);
        List<Datapoint> datapoints = response.datapoints();
        if (datapoints == null || datapoints.isEmpty()) {
            return 0.0;
        }
        return datapoints.stream()
            .map(datapoint -> switch (statistic) {
                case "Maximum" -> datapoint.maximum();
                case "Sum" -> datapoint.sum();
                default -> datapoint.average();
            })
            .filter(value -> value != null)
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);
    }

    private ExternalPlatformIntegration requireSageMakerPlatform(Long platformId) {
        ExternalPlatformIntegration platform = integrationService.findPlatformById(platformId);
        if (platform == null) {
            throw new IllegalArgumentException("Plataforma SageMaker no encontrada: " + platformId);
        }
        if (!"SAGEMAKER".equalsIgnoreCase(platform.getEplplatformtype())) {
            throw new IllegalArgumentException("La plataforma no es de tipo SAGEMAKER");
        }
        return platform;
    }

    private AwsCredentials resolveCredentials(ExternalPlatformIntegration platform) {
        JsonNode metadata = readJson(platform.getEplmetadata());
        String region = metadata.path("region").asText();
        String accessKey = metadata.path("accessKey").asText(StringUtils.trimToEmpty(platform.getEplclientid()));
        String secretKey = metadata.path("secretKey").asText(StringUtils.trimToEmpty(platform.getEplclientsecret()));
        if (StringUtils.isAnyBlank(region, accessKey, secretKey)) {
            throw new IllegalStateException("Metadata SageMaker incompleta (region, accessKey, secretKey)");
        }
        return new AwsCredentials(region, accessKey, secretKey);
    }

    private SageMakerClient buildSageMakerClient(AwsCredentials credentials) {
        return SageMakerClient.builder()
            .region(Region.of(credentials.region()))
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(credentials.accessKey(), credentials.secretKey())))
            .build();
    }

    private CloudWatchClient buildCloudWatchClient(AwsCredentials credentials) {
        return CloudWatchClient.builder()
            .region(Region.of(credentials.region()))
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(credentials.accessKey(), credentials.secretKey())))
            .build();
    }

    private JsonNode readJson(String json) {
        if (StringUtils.isBlank(json)) {
            return objectMapper.createObjectNode();
        }
        try {
            return objectMapper.readTree(json);
        } catch (Exception ex) {
            log.warn("Metadata SageMaker inválida: {}", ex.getMessage());
            return objectMapper.createObjectNode();
        }
    }

    public record SageMakerEndpointSummary(String endpointName, String status, Instant creationTime, String productionVariantsJson) {}

    public record SageMakerMetrics(double averageLatencyMs, double maximumLatencyMs, double totalInvocations,
                                   Instant from, Instant to) {}

    public record DriftDetectionResult(boolean driftDetected, double latencyIncreasePct, String status) {}

    private record AwsCredentials(String region, String accessKey, String secretKey) {}
}
