package com.codeflowx.govern.business.integrations;

import com.codeflowx.govern.entity.integrations.ExternalDataset;
import com.codeflowx.govern.entity.integrations.ExternalPlatformIntegration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Servicio para enviar jobs de evaluación (bias/quality) a Spark vía Livy sin mover datos.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SparkEvaluationService {

    private final ExternalIntegrationBusinessService integrationService;
    private final ObjectMapper objectMapper;
    private final WebClient.Builder webClientBuilder;

    /**
     * Envía un script PySpark a Livy para evaluar sesgo sobre un dataset remoto (S3/HDFS).
     */
    public SparkEvaluationResult submitBiasEvaluation(Long platformId, String datasetPath, List<String> protectedAttributes) {
        ExternalPlatformIntegration platform = integrationService.findPlatformById(platformId);
        if (platform == null || !"SPARK".equalsIgnoreCase(platform.getEplplatformtype())) {
            throw new IllegalArgumentException("Plataforma Spark no encontrada");
        }
        JsonNode metadata = readJson(platform.getEplmetadata());
        String livyUrl = metadata.path("livyUrl").asText();
        if (StringUtils.isBlank(livyUrl)) {
            throw new IllegalStateException("Metadata Spark incompleta (livyUrl)");
        }
        String outputPath = metadata.path("resultsPath").asText("s3://codeflowx-results/bias_eval_" + UUID.randomUUID());
        String pysparkCode = buildBiasEvaluationScript(datasetPath, protectedAttributes, outputPath);

        try {
            JsonNode session = createSession(livyUrl);
            int sessionId = session.path("id").asInt();
            waitForSessionReady(livyUrl, sessionId);
            JsonNode statement = submitStatement(livyUrl, sessionId, pysparkCode);
            waitForStatement(livyUrl, sessionId, statement.path("id").asInt());
            JsonNode results = fetchResults(outputPath, metadata);
            return new SparkEvaluationResult(outputPath, results.toString());
        } catch (Exception ex) {
            log.error("Error ejecutando Spark bias evaluation", ex);
            throw new IllegalStateException("Error ejecutando evaluación Spark", ex);
        }
    }

    private JsonNode createSession(String livyUrl) {
        return webClientBuilder.build()
            .post()
            .uri(livyUrl + "/sessions")
            .body(BodyInserters.fromValue(Map.of("kind", "pyspark")))
            .retrieve()
            .bodyToMono(JsonNode.class)
            .block();
    }

    private void waitForSessionReady(String livyUrl, int sessionId) throws InterruptedException {
        WebClient client = webClientBuilder.build();
        while (true) {
            JsonNode state = client.get()
                .uri(livyUrl + "/sessions/" + sessionId)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
            String sessionState = state.path("state").asText();
            if ("idle".equalsIgnoreCase(sessionState)) {
                return;
            }
            if ("error".equalsIgnoreCase(sessionState) || "dead".equalsIgnoreCase(sessionState)) {
                throw new IllegalStateException("Sesión Livy en estado " + sessionState);
            }
            Thread.sleep(1000L);
        }
    }

    private JsonNode submitStatement(String livyUrl, int sessionId, String code) {
        return webClientBuilder.build()
            .post()
            .uri(livyUrl + "/sessions/" + sessionId + "/statements")
            .body(BodyInserters.fromValue(Map.of("code", code)))
            .retrieve()
            .bodyToMono(JsonNode.class)
            .block();
    }

    private void waitForStatement(String livyUrl, int sessionId, int statementId) throws InterruptedException {
        WebClient client = webClientBuilder.build();
        while (true) {
            JsonNode state = client.get()
                .uri(livyUrl + "/sessions/" + sessionId + "/statements/" + statementId)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
            String statementState = state.path("state").asText();
            if ("available".equalsIgnoreCase(statementState)) {
                JsonNode output = state.path("output");
                if (output.has("status") && "error".equalsIgnoreCase(output.path("status").asText())) {
                    throw new IllegalStateException("Evaluación Spark falló: " + output.path("evalue").asText());
                }
                return;
            }
            if ("error".equalsIgnoreCase(statementState)) {
                throw new IllegalStateException("Statement Livy en error");
            }
            Thread.sleep(1000L);
        }
    }

    private JsonNode fetchResults(String outputPath, JsonNode metadata) {
        try {
            if (outputPath.startsWith("s3://")) {
                String bucket = outputPath.substring(5, outputPath.indexOf('/', 5));
                String key = outputPath.substring(outputPath.indexOf('/', 5) + 1) + "/part-00000.json";
                JsonNode aws = metadata.path("aws");
                String region = aws.path("region").asText();
                String accessKey = aws.path("accessKey").asText();
                String secretKey = aws.path("secretKey").asText();
                try (S3Client s3 = S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                    .build()) {
                    try (var response = s3.getObject(builder -> builder.bucket(bucket).key(key))) {
                        byte[] data = response.readAllBytes();
                        return objectMapper.readTree(new String(data, StandardCharsets.UTF_8));
                    }
                }
            }
            throw new UnsupportedOperationException("Solo se soporta lectura de resultados en S3");
        } catch (Exception ex) {
            throw new IllegalStateException("No fue posible leer resultados Spark", ex);
        }
    }

    private String buildBiasEvaluationScript(String datasetPath, List<String> protectedAttributes, String outputPath) {
        String attributesList = protectedAttributes == null || protectedAttributes.isEmpty()
            ? "[]"
            : protectedAttributes.stream().map(attr -> "\"" + attr + "\"").reduce((a, b) -> a + ", " + b).map(s -> "[" + s + "]").orElse("[]");
        return "from pyspark.sql import SparkSession\n"
            + "from pyspark.sql import functions as F\n"
            + "spark = SparkSession.builder.appName('CodeflowX Bias Evaluation').getOrCreate()\n"
            + "df = spark.read.parquet('" + datasetPath + "')\n"
            + "protected_attrs = " + attributesList + "\n"
            + "results = {}\n"
            + "for attr in protected_attrs:\n"
            + "    groups = df.groupBy(attr).agg(F.avg('prediction').alias('positive_rate')).collect()\n"
            + "    group_map = {row[attr]: row['positive_rate'] for row in groups}\n"
            + "    if len(group_map) > 1:\n"
            + "        max_rate = max(group_map.values())\n"
            + "        min_rate = min(group_map.values())\n"
            + "        disparity = (max_rate - min_rate) / max_rate if max_rate else 0\n"
            + "    else:\n"
            + "        disparity = 0\n"
            + "    results[attr] = {\n"
            + "        'groups': group_map,\n"
            + "        'disparity': disparity,\n"
            + "        'bias_detected': disparity > 0.1\n"
            + "    }\n"
            + "spark.createDataFrame([results]).write.mode('overwrite').json('" + outputPath + "')\n"
            + "spark.stop()";
    }

    public record SparkEvaluationResult(String outputPath, String metricsJson) {}
}
