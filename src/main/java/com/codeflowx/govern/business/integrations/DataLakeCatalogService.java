package com.codeflowx.govern.business.integrations;

import com.codeflowx.govern.entity.integrations.ExternalDataset;
import com.codeflowx.govern.entity.integrations.ExternalPlatformIntegration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.S3Object;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;

/**
 * Servicio para catalogar datasets en data lakes (S3, Azure Blob, GCS) únicamente mediante metadata.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DataLakeCatalogService {

    private final ExternalIntegrationBusinessService integrationService;
    private final ObjectMapper objectMapper;

    public List<ExternalDataset> catalogS3(Long platformId) {
        ExternalPlatformIntegration platform = integrationService.findPlatformById(platformId);
        if (platform == null || !"S3".equalsIgnoreCase(platform.getEplplatformtype())) {
            throw new IllegalArgumentException("Plataforma S3 no encontrada");
        }
        JsonNode metadata = readJson(platform.getEplmetadata());
        String bucket = metadata.path("bucket").asText();
        String prefix = metadata.path("prefix").asText("");
        String region = metadata.path("region").asText();
        String accessKey = metadata.path("accessKey").asText(StringUtils.trimToEmpty(platform.getEplclientid()));
        String secretKey = metadata.path("secretKey").asText(StringUtils.trimToEmpty(platform.getEplclientsecret()));
        if (StringUtils.isAnyBlank(bucket, region, accessKey, secretKey)) {
            throw new IllegalStateException("Metadata S3 incompleta (bucket, region, accessKey, secretKey)");
        }
        try (S3Client s3Client = S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
            .build()) {

            ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix(prefix)
                .build();
            List<ExternalDataset> datasets = new ArrayList<>();
            s3Client.listObjectsV2Paginator(request).forEach(page -> {
                for (S3Object object : page.contents()) {
                    if (object.key().endsWith("/")) {
                        continue;
                    }
                    ExternalDataset dataset = upsertDataLakeDataset(platform, "S3", bucket, object.key(), object.size(), object.lastModified().toEpochMilli());
                    datasets.add(dataset);
                }
            });
            integrationService.updateSyncStatus(platformId, "SUCCESS", null);
            return datasets;
        } catch (Exception ex) {
            log.error("Error catalogando S3 {}", bucket, ex);
            integrationService.updateSyncStatus(platformId, "ERROR", ex.getMessage());
            throw new IllegalStateException("Error catalogando S3", ex);
        }
    }

    public List<ExternalDataset> catalogAzureBlob(Long platformId) {
        ExternalPlatformIntegration platform = integrationService.findPlatformById(platformId);
        if (platform == null || !"AZURE_BLOB".equalsIgnoreCase(platform.getEplplatformtype())) {
            throw new IllegalArgumentException("Plataforma Azure Blob no encontrada");
        }
        JsonNode metadata = readJson(platform.getEplmetadata());
        String connectionString = metadata.path("connectionString").asText();
        String container = metadata.path("container").asText();
        String prefix = metadata.path("prefix").asText("");
        if (StringUtils.isAnyBlank(connectionString, container)) {
            throw new IllegalStateException("Metadata Azure Blob incompleta (connectionString, container)");
        }
        try {
            BlobServiceClient serviceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
            BlobContainerClient containerClient = serviceClient.getBlobContainerClient(container);
            List<ExternalDataset> datasets = new ArrayList<>();
            for (BlobItem blobItem : containerClient.listBlobs(prefix)) {
                if (blobItem.isPrefix() != null && blobItem.isPrefix()) {
                    continue;
                }
                ExternalDataset dataset = upsertDataLakeDataset(
                    platform,
                    "AZURE_BLOB",
                    container,
                    blobItem.getName(),
                    blobItem.getProperties().getContentLength(),
                    blobItem.getProperties().getLastModified() != null ? blobItem.getProperties().getLastModified().toInstant().toEpochMilli() : System.currentTimeMillis()
                );
                datasets.add(dataset);
            }
            integrationService.updateSyncStatus(platformId, "SUCCESS", null);
            return datasets;
        } catch (Exception ex) {
            log.error("Error catalogando Azure Blob {}", container, ex);
            integrationService.updateSyncStatus(platformId, "ERROR", ex.getMessage());
            throw new IllegalStateException("Error catalogando Azure Blob", ex);
        }
    }

    public List<ExternalDataset> catalogGcs(Long platformId) {
        ExternalPlatformIntegration platform = integrationService.findPlatformById(platformId);
        if (platform == null || !"GCS".equalsIgnoreCase(platform.getEplplatformtype())) {
            throw new IllegalArgumentException("Plataforma GCS no encontrada");
        }
        JsonNode metadata = readJson(platform.getEplmetadata());
        String projectId = metadata.path("projectId").asText();
        String bucketName = metadata.path("bucket").asText();
        String prefix = metadata.path("prefix").asText("");
        String credentialsJson = metadata.path("credentialsJson").asText();
        if (StringUtils.isAnyBlank(projectId, bucketName)) {
            throw new IllegalStateException("Metadata GCS incompleta (projectId, bucket)");
        }
        try {
            StorageOptions.Builder builder = StorageOptions.newBuilder().setProjectId(projectId);
            if (StringUtils.isNotBlank(credentialsJson)) {
                builder.setCredentials(GoogleCredentialsUtils.fromJson(credentialsJson));
            }
            Storage storage = builder.build().getService();
            Bucket bucket = storage.get(bucketName);
            if (bucket == null) {
                throw new IllegalStateException("Bucket GCS no encontrado: " + bucketName);
            }
            List<ExternalDataset> datasets = new ArrayList<>();
            Iterable<Blob> blobs = bucket.list(Storage.BlobListOption.prefix(prefix)).iterateAll();
            for (Blob blob : blobs) {
                if (blob.isDirectory()) {
                    continue;
                }
                ExternalDataset dataset = upsertDataLakeDataset(
                    platform,
                    "GCS",
                    bucketName,
                    blob.getName(),
                    blob.getSize(),
                    blob.getUpdateTime() != null ? blob.getUpdateTime() : System.currentTimeMillis()
                );
                datasets.add(dataset);
            }
            integrationService.updateSyncStatus(platformId, "SUCCESS", null);
            return datasets;
        } catch (Exception ex) {
            log.error("Error catalogando GCS {}", bucketName, ex);
            integrationService.updateSyncStatus(platformId, "ERROR", ex.getMessage());
            throw new IllegalStateException("Error catalogando GCS", ex);
        }
    }

    private ExternalDataset upsertDataLakeDataset(ExternalPlatformIntegration platform, String provider, String container,
                                                  String objectKey, long sizeBytes, long lastModifiedMillis) {
        String externalId = String.format(Locale.ROOT, "%s://%s/%s", provider.toLowerCase(Locale.ROOT), container, objectKey);
        ExternalDataset existing = integrationService.findDatasetByExternalId(platform.getIdxexternalplatform(), externalId);
        boolean isNew = existing == null;
        ExternalDataset dataset = isNew ? new ExternalDataset() : existing;
        if (isNew) {
            dataset.setPlatform(platform);
            dataset.setIduuid(UUID.randomUUID().toString());
            dataset.setExdcreatedat(Timestamp.from(Instant.now()));
        }
        dataset.setExdexternalid(externalId);
        dataset.setExdname(objectKey);
        dataset.setExdsourcelocation(externalId);
        dataset.setExdsizebytes(sizeBytes);
        dataset.setExdrecordcount(null);
        dataset.setExdlastqualitycheck(new Timestamp(lastModifiedMillis));
        dataset.setExdupdatedat(Timestamp.from(Instant.now()));
        try {
            dataset.setExdmetadata(objectMapper.createObjectNode()
                .put("provider", provider)
                .put("container", container)
                .put("object_key", objectKey)
                .put("size_bytes", sizeBytes)
                .put("last_modified", Instant.ofEpochMilli(lastModifiedMillis).toString())
                .toString());
        } catch (Exception ex) {
            log.warn("No fue posible serializar metadata dataset {}: {}", externalId, ex.getMessage());
        }
        integrationService.saveExternalDataset(dataset);
        return dataset;
    }

    private JsonNode readJson(String json) {
        if (StringUtils.isBlank(json)) {
            return objectMapper.createObjectNode();
        }
        try {
            return objectMapper.readTree(json);
        } catch (Exception ex) {
            log.warn("JSON metadata data lake inválido: {}", ex.getMessage());
            return objectMapper.createObjectNode();
        }
    }

    private static final class GoogleCredentialsUtils {
        private GoogleCredentialsUtils() {
        }

        static com.google.auth.Credentials fromJson(String json) {
            try {
                return com.google.auth.oauth2.GoogleCredentials.fromStream(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
            } catch (Exception ex) {
                throw new IllegalStateException("Credenciales GCS inválidas", ex);
            }
        }
    }
}
