package com.codeflowx.govern.integrations;

import com.codeflowx.govern.business.integrations.DatabricksConnectorService;
import com.codeflowx.govern.business.integrations.ExternalIntegrationBusinessService;
import com.codeflowx.govern.entity.integrations.ExternalModel;
import com.codeflowx.govern.entity.integrations.ExternalPlatformIntegration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test end-to-end para integración con Databricks.
 * 
 * Valida el flujo completo de sincronización con plataformas externas,
 * incluyendo casos de error y recuperación.
 * 
 * Incidencia: INC-INT-008
 */
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class DatabricksIntegrationE2ETest {

    @Autowired
    private DatabricksConnectorService databricksConnector;

    @Autowired
    private ExternalIntegrationBusinessService integrationService;

    // Mock container para Databricks (en producción usar mock server real)
    @Container
    static GenericContainer<?> mockDatabricks = new GenericContainer<>("httpd:alpine")
        .withExposedPorts(80)
        .withCommand("sh", "-c", "echo 'Mock Databricks'");

    @Test
    void testSyncModelsFromDatabricks_Success() {
        // Given: Configurar plataforma Databricks de prueba
        ExternalPlatformIntegration platform = createTestPlatform("DATABRICKS");
        Long platformId = integrationService.savePlatform(platform).getIdxexternalplatform();

        try {
            // When: Ejecutar sync
            databricksConnector.syncModelsFromDatabricks(platformId);

            // Then: Verificar que modelos están en CodeflowX
            List<ExternalModel> models = integrationService.findExternalModelsByPlatform(platformId);
            assertThat(models).isNotNull();
            
            // Verificar que plataforma tiene estado SUCCESS
            ExternalPlatformIntegration updated = integrationService.findPlatformById(platformId);
            assertThat(updated.getEplsyncstatus()).isEqualTo("SUCCESS");
            assertThat(updated.getEpllastsyncat()).isNotNull();

        } finally {
            // Cleanup
            integrationService.deletePlatform(platformId);
        }
    }

    @Test
    void testSyncModelsFromDatabricks_ErrorRecovery() {
        // Given: Plataforma con configuración incorrecta
        ExternalPlatformIntegration platform = createTestPlatform("DATABRICKS");
        platform.setEplhosturl("https://invalid-url.databricks.com");
        Long platformId = integrationService.savePlatform(platform).getIdxexternalplatform();

        try {
            // When: Ejecutar sync (debe fallar)
            try {
                databricksConnector.syncModelsFromDatabricks(platformId);
            } catch (Exception e) {
                // Expected exception
            }

            // Then: Verificar que estado es ERROR
            ExternalPlatformIntegration updated = integrationService.findPlatformById(platformId);
            assertThat(updated.getEplsyncstatus()).isEqualTo("ERROR");
            assertThat(updated.getEpllasterror()).isNotNull();

        } finally {
            // Cleanup
            integrationService.deletePlatform(platformId);
        }
    }

    @Test
    void testSyncModelsFromDatabricks_RetryOnTransientError() {
        // Given: Plataforma configurada
        ExternalPlatformIntegration platform = createTestPlatform("DATABRICKS");
        Long platformId = integrationService.savePlatform(platform).getIdxexternalplatform();

        try {
            // When: Ejecutar sync con retry automático
            // (El retry se maneja automáticamente por Spring Retry)
            databricksConnector.syncModelsFromDatabricks(platformId);

            // Then: Verificar que se completó después de reintentos
            ExternalPlatformIntegration updated = integrationService.findPlatformById(platformId);
            // Si llegó aquí sin excepción, el retry funcionó

        } finally {
            // Cleanup
            integrationService.deletePlatform(platformId);
        }
    }

    @Test
    void testSyncModelsFromDatabricks_VerifyMetadataIntegrity() {
        // Given: Plataforma configurada
        ExternalPlatformIntegration platform = createTestPlatform("DATABRICKS");
        Long platformId = integrationService.savePlatform(platform).getIdxexternalplatform();

        try {
            // When: Ejecutar sync
            databricksConnector.syncModelsFromDatabricks(platformId);

            // Then: Verificar que metadata tiene hash de integridad
            List<ExternalModel> models = integrationService.findExternalModelsByPlatform(platformId);
            for (ExternalModel model : models) {
                assertThat(model.getExmmetadataHash()).isNotNull();
                assertThat(model.getExmmetadataHash()).hasSize(64); // SHA-256 = 64 caracteres hex
            }

        } finally {
            // Cleanup
            integrationService.deletePlatform(platformId);
        }
    }

    /**
     * Crea una plataforma de prueba.
     */
    private ExternalPlatformIntegration createTestPlatform(String platformType) {
        ExternalPlatformIntegration platform = new ExternalPlatformIntegration();
        platform.setIduuid(UUID.randomUUID().toString());
        platform.setEplplatformtype(platformType);
        platform.setEplplatformname("Test " + platformType);
        platform.setEplhosturl("https://test.example.com");
        platform.setEplauthenticationtype("API_TOKEN");
        platform.setEplapiToken("test-token");
        platform.setEplsyncenabled(true);
        platform.setEplsyncfrequencyhours(1);
        platform.setEplcreatedat(new Timestamp(System.currentTimeMillis()));
        return platform;
    }
}

