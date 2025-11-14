package com.codeflowx.govern.business.integrations;

import com.codeflowx.govern.entity.integrations.ExternalDataset;
import com.codeflowx.govern.entity.integrations.ExternalPlatformIntegration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Conector Snowflake para catalogación y evaluación de calidad sin mover datos (solo metadata / samples controlados).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SnowflakeConnectorService {

    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("^[A-Za-z0-9_\"$]+$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern PHONE_PATTERN = Pattern.compile(".*\\d{3}[-\\s]?\\d{3}[-\\s]?\\d{4}.*");

    private final ExternalIntegrationBusinessService integrationService;
    private final ObjectMapper objectMapper;

    @Value("${govern.integrations.snowflake.sample-size:1000}")
    private int defaultSampleSize;

    /**
     * Catalogación de datasets Snowflake (INFORMATION_SCHEMA.TABLES) sin copiar datos.
     */
    public List<ExternalDataset> catalogDatasets(Long platformId) {
        ExternalPlatformIntegration platform = integrationService.findPlatformById(platformId);
        if (platform == null) {
            throw new IllegalArgumentException("Plataforma Snowflake no encontrada: " + platformId);
        }
        if (!"SNOWFLAKE".equalsIgnoreCase(platform.getEplplatformtype())) {
            throw new IllegalArgumentException("La plataforma seleccionada no es de tipo SNOWFLAKE");
        }
        SnowflakeCredentials credentials = resolveCredentials(platform);
        List<String> schemas = credentials.schemas();
        List<ExternalDataset> persisted = new ArrayList<>();

        try {
            for (String schema : schemas) {
                try (Connection connection = openConnection(credentials, credentials.database(), schema)) {
                    String sql = "SELECT table_name, row_count, bytes, created, last_altered "
                        + "FROM " + quote(credentials.database()) + ".INFORMATION_SCHEMA.TABLES "
                        + "WHERE table_schema = ? AND table_type = 'BASE TABLE'";
                    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                        stmt.setString(1, schema.toUpperCase(Locale.ROOT));
                        try (ResultSet rs = stmt.executeQuery()) {
                            while (rs.next()) {
                                ExternalDataset dataset = upsertDataset(platform, credentials, schema, rs);
                                persisted.add(dataset);
                            }
                        }
                    }
                } catch (SQLException ex) {
                    log.error("Error catalogando schema {} en Snowflake", schema, ex);
                    throw new IllegalStateException("Error catalogando Snowflake schema " + schema, ex);
                }
            }
            integrationService.updateSyncStatus(platformId, "SUCCESS", null);
            log.info("Catalogación Snowflake completada para {}: {} datasets", platform.getEplplatformname(), persisted.size());
            return persisted;
        } catch (RuntimeException ex) {
            integrationService.updateSyncStatus(platformId, "ERROR", ex.getMessage());
            throw ex;
        }
    }

    /**
     * Evalúa calidad de un dataset usando SAMPLE en Snowflake. No copia datos completos.
     */
    public SnowflakeQualityResult evaluateDatasetQuality(Long datasetId, int sampleSize) {
        ExternalDataset dataset = integrationService.findDatasetById(datasetId);
        if (dataset == null) {
            throw new IllegalArgumentException("Dataset externo no encontrado: " + datasetId);
        }
        ExternalPlatformIntegration platform = Optional.ofNullable(dataset.getPlatform())
            .orElseThrow(() -> new IllegalStateException("El dataset no tiene plataforma asociada"));
        if (!"SNOWFLAKE".equalsIgnoreCase(platform.getEplplatformtype())) {
            throw new IllegalArgumentException("El dataset no pertenece a una plataforma Snowflake");
        }
        SnowflakeCredentials credentials = resolveCredentials(platform);
        String[] identifiers = parseDatasetExternalId(dataset.getExdexternalid());
        String database = identifiers[0];
        String schema = identifiers[1];
        String table = identifiers[2];
        int sample = sampleSize > 0 ? sampleSize : defaultSampleSize;

        try (Connection connection = openConnection(credentials, database, schema)) {
            String sql = String.format(
                "SELECT * FROM %s.%s.%s SAMPLE (%d ROWS)",
                quote(database), quote(schema), quote(table), sample);

            List<String> potentialPii = new ArrayList<>();
            Set<String> uniqueRows = new HashSet<>();
            int duplicates = 0;
            int totalRows = 0;
            long nullValues = 0;

            try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();
                List<String> columnNames = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    columnNames.add(meta.getColumnName(i));
                }

                while (rs.next()) {
                    totalRows++;
                    StringBuilder rowBuilder = new StringBuilder();
                    for (int i = 1; i <= columnCount; i++) {
                        Object value = rs.getObject(i);
                        if (value == null) {
                            nullValues++;
                        } else {
                            String text = value.toString();
                            detectPii(columnNames.get(i - 1), text, potentialPii);
                        }
                        rowBuilder.append(value).append('|');
                    }
                    String rowSignature = rowBuilder.toString();
                    if (!uniqueRows.add(rowSignature)) {
                        duplicates++;
                    }
                }

                double completenessPct = computeCompleteness(totalRows, columnCount, nullValues);
                double duplicatesPct = totalRows > 0 ? (duplicates * 100.0) / totalRows : 0.0;

                SnowflakeQualityResult result = new SnowflakeQualityResult(
                    database,
                    schema,
                    table,
                    totalRows,
                    columnCount,
                    completenessPct,
                    duplicates,
                    duplicatesPct,
                    List.copyOf(new HashSet<>(potentialPii))
                );

                updateDatasetQuality(dataset, result);
                return result;
            }
        } catch (SQLException ex) {
            log.error("Error evaluando calidad Snowflake {}", dataset.getExdexternalid(), ex);
            throw new IllegalStateException("Error evaluando calidad Snowflake", ex);
        }
    }

    private ExternalDataset upsertDataset(ExternalPlatformIntegration platform, SnowflakeCredentials credentials, String schema, ResultSet rs) throws SQLException {
        String tableName = rs.getString("table_name");
        long rowCount = rs.getLong("row_count");
        long sizeBytes = rs.getLong("bytes");
        Timestamp created = rs.getTimestamp("created");
        Timestamp lastAltered = rs.getTimestamp("last_altered");

        String externalId = credentials.database() + "." + schema + "." + tableName;
        ExternalDataset existing = integrationService.findDatasetByExternalId(platform.getIdxexternalplatform(), externalId);
        boolean isNew = existing == null;
        ExternalDataset dataset = isNew ? new ExternalDataset() : existing;

        if (isNew) {
            dataset.setPlatform(platform);
            dataset.setIduuid(java.util.UUID.randomUUID().toString());
            dataset.setExdcreatedat(Timestamp.from(Instant.now()));
        }

        dataset.setExdexternalid(externalId);
        dataset.setExdname(tableName);
        dataset.setExdsourcelocation(String.format("snowflake://%s/%s/%s/%s", credentials.account(), credentials.database(), schema, tableName));
        dataset.setExdrecordcount(rowCount);
        dataset.setExdsizebytes(sizeBytes);
        dataset.setExdlastqualitycheck(lastAltered);
        dataset.setExdmetadata(buildDatasetMetadata(rowCount, sizeBytes, created, lastAltered));
        dataset.setExdupdatedat(Timestamp.from(Instant.now()));

        integrationService.saveExternalDataset(dataset);
        return dataset;
    }

    private String buildDatasetMetadata(long rowCount, long sizeBytes, Timestamp created, Timestamp lastAltered) {
        try {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("row_count", rowCount);
            node.put("size_bytes", sizeBytes);
            if (created != null) {
                node.put("created", created.toInstant().toString());
            }
            if (lastAltered != null) {
                node.put("last_altered", lastAltered.toInstant().toString());
            }
            return objectMapper.writeValueAsString(node);
        } catch (Exception ex) {
            log.warn("No fue posible serializar metadata Snowflake: {}", ex.getMessage());
            return "{}";
        }
    }

    private void updateDatasetQuality(ExternalDataset dataset, SnowflakeQualityResult result) {
        try {
            dataset.setExdqualityscore(result.completenessPct());
            dataset.setExdpiidetected(!result.potentialPiiColumns().isEmpty());
            dataset.setExdgdprcompliant(result.potentialPiiColumns().isEmpty());
            dataset.setExdlastqualitycheck(Timestamp.from(Instant.now()));
            dataset.setExdmetadata(objectMapper.writeValueAsString(result.toMap()));
            integrationService.saveExternalDataset(dataset);
        } catch (Exception ex) {
            log.warn("No fue posible actualizar metadata de calidad Snowflake: {}", ex.getMessage());
        }
    }

    private void detectPii(String columnName, String value, List<String> potentialPii) {
        if (StringUtils.isBlank(value)) {
            return;
        }
        String nameUpper = columnName.toUpperCase(Locale.ROOT);
        if (nameUpper.contains("EMAIL") || EMAIL_PATTERN.matcher(value).matches()) {
            potentialPii.add(columnName + "::EMAIL");
        } else if (nameUpper.contains("PHONE") || PHONE_PATTERN.matcher(value).matches()) {
            potentialPii.add(columnName + "::PHONE");
        } else if (nameUpper.contains("SSN") || nameUpper.contains("DNI")) {
            potentialPii.add(columnName + "::ID");
        }
    }

    private double computeCompleteness(int totalRows, int columnCount, long nullValues) {
        if (totalRows <= 0 || columnCount <= 0) {
            return 100.0;
        }
        long totalCells = (long) totalRows * columnCount;
        double completeness = 1.0 - ((double) nullValues / totalCells);
        return Math.round(completeness * 10000.0) / 100.0;
    }

    private Connection openConnection(SnowflakeCredentials credentials, String database, String schema) throws SQLException {
        Properties props = new Properties();
        props.put("user", credentials.user());
        props.put("password", credentials.password());
        if (StringUtils.isNotBlank(credentials.warehouse())) {
            props.put("warehouse", credentials.warehouse());
        }
        if (StringUtils.isNotBlank(credentials.role())) {
            props.put("role", credentials.role());
        }
        if (StringUtils.isNotBlank(database)) {
            props.put("db", database);
        }
        if (StringUtils.isNotBlank(schema)) {
            props.put("schema", schema);
        }
        String url = credentials.host();
        if (StringUtils.isBlank(url)) {
            url = String.format("jdbc:snowflake://%s.snowflakecomputing.com", credentials.account());
        }
        return DriverManager.getConnection(url, props);
    }

    private SnowflakeCredentials resolveCredentials(ExternalPlatformIntegration platform) {
        JsonNode metadata = parseMetadata(platform.getEplmetadata());
        String account = metadata.path("account").asText(StringUtils.trimToEmpty(platform.getEpltenantid()));
        String host = metadata.path("host").asText(null);
        String url = metadata.path("url").asText(null);
        String user = metadata.path("user").asText(StringUtils.trimToEmpty(platform.getEplclientid()));
        String password = metadata.path("password").asText(StringUtils.trimToEmpty(platform.getEplclientsecret()));
        String warehouse = metadata.path("warehouse").asText(null);
        String role = metadata.path("role").asText(null);
        String database = metadata.path("database").asText(null);

        if (StringUtils.isBlank(account) && StringUtils.isNotBlank(host)) {
            account = host.replace("https://", "")
                .replace("http://", "")
                .replace("jdbc:snowflake://", "")
                .replace(".snowflakecomputing.com", "");
        }
        if (StringUtils.isBlank(user)) {
            throw new IllegalStateException("Snowflake user no definido en la plataforma");
        }
        if (StringUtils.isBlank(password)) {
            throw new IllegalStateException("Snowflake password no definido en la plataforma");
        }
        if (StringUtils.isBlank(database)) {
            throw new IllegalStateException("Snowflake database no definido en metadata (clave 'database')");
        }

        List<String> schemas = new ArrayList<>();
        JsonNode schemasNode = metadata.path("schemas");
        if (schemasNode.isArray()) {
            schemasNode.forEach(node -> schemas.add(node.asText()));
        }
        if (schemas.isEmpty()) {
            String defaultSchema = metadata.path("schema").asText("PUBLIC");
            schemas.add(defaultSchema);
        }

        String resolvedHost = url != null ? url : host;
        if (StringUtils.isNotBlank(resolvedHost) && !resolvedHost.startsWith("jdbc:")) {
            resolvedHost = resolvedHost.startsWith("http")
                ? resolvedHost.replace("https://", "jdbc:snowflake://").replace("http://", "jdbc:snowflake://")
                : "jdbc:snowflake://" + resolvedHost;
        }

        return new SnowflakeCredentials(
            account,
            resolvedHost,
            user,
            password,
            warehouse,
            role,
            database,
            List.copyOf(schemas)
        );
    }

    public boolean testConnection(ExternalPlatformIntegration platform) {
        if (platform == null || !"SNOWFLAKE".equalsIgnoreCase(platform.getEplplatformtype())) {
            return false;
        }
        try {
            SnowflakeCredentials credentials = resolveCredentials(platform);
            List<String> schemas = credentials.schemas();
            String schema = schemas.isEmpty() ? "PUBLIC" : schemas.get(0);
            try (Connection connection = openConnection(credentials, credentials.database(), schema);
                 Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT 1")) {
                return rs.next();
            }
        } catch (Exception ex) {
            log.warn("Test conexión Snowflake falló para {}: {}", platform.getEplplatformname(), ex.getMessage());
            return false;
        }
    }

    private JsonNode parseMetadata(String metadataJson) {
        if (StringUtils.isBlank(metadataJson)) {
            return objectMapper.createObjectNode();
        }
        try {
            return objectMapper.readTree(metadataJson);
        } catch (Exception ex) {
            log.warn("Metadata Snowflake inválida: {}", ex.getMessage());
            return objectMapper.createObjectNode();
        }
    }

    private String[] parseDatasetExternalId(String externalId) {
        if (StringUtils.isBlank(externalId) || !externalId.contains(".")) {
            throw new IllegalArgumentException("External ID Snowflake inválido: " + externalId);
        }
        String[] parts = externalId.split("\\.");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Se requieren database.schema.table en externalId: " + externalId);
        }
        return new String[]{parts[0], parts[1], parts[2]};
    }

    private String quote(String identifier) {
        if (StringUtils.isBlank(identifier)) {
            throw new IllegalArgumentException("Identificador Snowflake vacío");
        }
        if (!IDENTIFIER_PATTERN.matcher(identifier).matches()) {
            throw new IllegalArgumentException("Identificador Snowflake inválido: " + identifier);
        }
        return identifier;
    }

    public record SnowflakeQualityResult(
        String database,
        String schema,
        String table,
        int rowsSampled,
        int columnCount,
        double completenessPct,
        int duplicatesCount,
        double duplicatesPct,
        List<String> potentialPiiColumns
    ) {
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("database", database);
            map.put("schema", schema);
            map.put("table", table);
            map.put("rows_sampled", rowsSampled);
            map.put("column_count", columnCount);
            map.put("completeness_pct", completenessPct);
            map.put("duplicates_count", duplicatesCount);
            map.put("duplicates_pct", duplicatesPct);
            map.put("pii_columns", potentialPiiColumns);
            return map;
        }
    }

    private record SnowflakeCredentials(
        String account,
        String host,
        String user,
        String password,
        String warehouse,
        String role,
        String database,
        List<String> schemas
    ) {
    }
}
