# PROMPT: INC-INT-008 - Testing End-to-End de Integraciones

**Incidencia:** INC-INT-008  
**Prioridad:** 🟡 MEDIA  
**Artículo EU AI Act:** Art. 15 (Robustez, Validación)  
**Esfuerzo Estimado:** 4-5 días  
**Tipo:** Java - Testing  
**Documento Relacionado:** `AUDITORIA_INTEGRACION_SIN_SUSTITUCION.md`

---

## CONTEXTO

No se encuentran tests end-to-end que validen el flujo completo de sincronización con plataformas externas, incluyendo casos de error y recuperación. Esto impide validar que las integraciones funcionan correctamente en conjunto.

**Riesgo:**
- No se puede validar que las integraciones funcionan correctamente en conjunto
- Riesgo de regresiones no detectadas
- No se puede validar recuperación ante fallos

**Ubicación Actual:**
- Falta tests E2E para integraciones
- Solo tests unitarios básicos
- No hay tests de recuperación

---

## REQUISITOS

1. **Crear tests E2E con Testcontainers** para validar flujo completo
2. **Tests de recuperación** ante fallos de API externa
3. **Mock de APIs externas** (WireMock o similar)
4. **Pipeline CI/CD** que ejecute tests E2E
5. **Tests de sincronización bidireccional** (CodeflowX ↔ Plataforma externa)

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Añadir Dependencias Testcontainers

**Añadir a `pom.xml`:**

```xml
<!-- Testcontainers -->
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>testcontainers</artifactId>
    <version>1.19.1</version>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <version>1.19.1</version>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>1.19.1</version>
    <scope>test</scope>
</dependency>

<!-- WireMock para mock APIs externas -->
<dependency>
    <groupId>org.wiremock</groupId>
    <artifactId>wiremock-jre8-standalone</artifactId>
    <version>2.35.0</version>
    <scope>test</scope>
</dependency>
```

### 2. Crear Test E2E Databricks

**Ubicación:** `suinsit.nova.web/src/test/java/com/codeflowx/govern/integration/DatabricksIntegrationE2ETest.java`

```java
package com.codeflowx.govern.integration;

import com.codeflowx.govern.business.integrations.DatabricksConnectorService;
import com.codeflowx.govern.entity.integrations.ExternalModel;
import com.codeflowx.govern.entity.integrations.ExternalPlatformIntegration;
import com.codeflowx.govern.service.external.ExternalIntegrationBusinessService;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests E2E para integración con Databricks.
 * 
 * Valida flujo completo:
 * 1. Configurar plataforma externa
 * 2. Sincronizar modelos desde Databricks
 * 3. Verificar que modelos están en CodeflowX
 * 4. Notificar aprobación a Databricks
 * 5. Verificar que tags están en Databricks
 */
@SpringBootTest
@Testcontainers
class DatabricksIntegrationE2ETest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("codeflowx_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private DatabricksConnectorService databricksConnector;

    @Autowired
    private ExternalIntegrationBusinessService integrationService;

    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        // Iniciar WireMock para mock de API Databricks
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8089);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void testSyncModelsFromDatabricks_E2E() {
        // 1. Configurar plataforma externa
        ExternalPlatformIntegration platform = createMockPlatform();
        integrationService.savePlatform(platform);

        // 2. Mock respuesta Databricks API
        wireMockServer.stubFor(get(urlEqualTo("/api/2.0/mlflow/registered-models/list"))
            .withHeader("Authorization", matching("Bearer.*"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                      "registered_models": [
                        {
                          "name": "test-model-1",
                          "latest_versions": [
                            {
                              "version": "1",
                              "stage": "Production"
                            }
                          ]
                        }
                      ]
                    }
                    """)));

        // 3. Ejecutar sync
        databricksConnector.syncModelsFromDatabricks(platform.getIdxexternalplatform());

        // 4. Verificar que modelos están en CodeflowX
        List<ExternalModel> models = integrationService.findExternalModelsByPlatform(
            platform.getIdxexternalplatform()
        );

        assertThat(models).hasSize(1);
        assertThat(models.get(0).getExmexternalname()).isEqualTo("test-model-1");
        assertThat(models.get(0).getExmsyncstatus()).isEqualTo("SYNCED");

        // 5. Verificar que se llamó a la API de Databricks
        wireMockServer.verify(
            getRequestedFor(urlEqualTo("/api/2.0/mlflow/registered-models/list"))
                .withHeader("Authorization", matching("Bearer.*"))
        );
    }

    @Test
    void testNotifyApprovalToDatabricks_E2E() {
        // 1. Configurar plataforma y modelo
        ExternalPlatformIntegration platform = createMockPlatform();
        integrationService.savePlatform(platform);

        ExternalModel model = createMockModel(platform);
        integrationService.saveExternalModel(model);

        // 2. Mock set tag API
        wireMockServer.stubFor(post(urlEqualTo("/api/2.0/mlflow/model-versions/set-tag"))
            .willReturn(aResponse().withStatus(200)));

        // 3. Notificar aprobación
        databricksConnector.notifyApprovalToDatabricks(
            model.getIdxexternalmodel(), "APPROVED"
        );

        // 4. Verificar que se llamó a set-tag con tags correctos
        wireMockServer.verify(
            postRequestedFor(urlEqualTo("/api/2.0/mlflow/model-versions/set-tag"))
                .withRequestBody(matchingJsonPath("$.key", equalTo("codeflowx_approval")))
                .withRequestBody(matchingJsonPath("$.value", equalTo("APPROVED")))
        );
    }

    @Test
    void testSyncRecovery_AfterApiFailure() {
        // 1. Configurar plataforma
        ExternalPlatformIntegration platform = createMockPlatform();
        integrationService.savePlatform(platform);

        // 2. Mock error 500 en primeros 2 intentos, luego éxito
        wireMockServer.stubFor(get(urlEqualTo("/api/2.0/mlflow/registered-models/list"))
            .inScenario("Retry scenario")
            .whenScenarioStateIs(STARTED)
            .willReturn(aResponse().withStatus(500))
            .willSetStateTo("Retry 1"));

        wireMockServer.stubFor(get(urlEqualTo("/api/2.0/mlflow/registered-models/list"))
            .inScenario("Retry scenario")
            .whenScenarioStateIs("Retry 1")
            .willReturn(aResponse().withStatus(500))
            .willSetStateTo("Retry 2"));

        wireMockServer.stubFor(get(urlEqualTo("/api/2.0/mlflow/registered-models/list"))
            .inScenario("Retry scenario")
            .whenScenarioStateIs("Retry 2")
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("{\"registered_models\": []}")));

        // 3. Ejecutar sync (debe recuperarse después de reintentos)
        databricksConnector.syncModelsFromDatabricks(platform.getIdxexternalplatform());

        // 4. Verificar que finalmente se obtuvo respuesta exitosa
        wireMockServer.verify(
            getRequestedFor(urlEqualTo("/api/2.0/mlflow/registered-models/list"))
                .atLeast(3) // Al menos 3 intentos (1 inicial + 2 reintentos)
        );

        // 5. Verificar que plataforma está en estado SUCCESS
        ExternalPlatformIntegration updated = integrationService.findPlatformById(
            platform.getIdxexternalplatform()
        );
        assertThat(updated.getEplsyncstatus()).isEqualTo("SUCCESS");
    }

    @Test
    void testSyncFailure_UpdatesPlatformStatus() {
        // 1. Configurar plataforma
        ExternalPlatformIntegration platform = createMockPlatform();
        integrationService.savePlatform(platform);

        // 2. Mock error persistente
        wireMockServer.stubFor(get(urlEqualTo("/api/2.0/mlflow/registered-models/list"))
            .willReturn(aResponse()
                .withStatus(401)
                .withBody("{\"error\":\"Unauthorized\"}")));

        // 3. Ejecutar sync (debe fallar)
        assertThatThrownBy(() -> 
            databricksConnector.syncModelsFromDatabricks(platform.getIdxexternalplatform())
        ).isInstanceOf(RuntimeException.class);

        // 4. Verificar que plataforma está en estado ERROR
        ExternalPlatformIntegration updated = integrationService.findPlatformById(
            platform.getIdxexternalplatform()
        );
        assertThat(updated.getEplsyncstatus()).isEqualTo("ERROR");
        assertThat(updated.getEpllasterror()).isNotNull();
    }

    private ExternalPlatformIntegration createMockPlatform() {
        ExternalPlatformIntegration platform = new ExternalPlatformIntegration();
        platform.setIduuid(UUID.randomUUID().toString());
        platform.setEplplatformtype("DATABRICKS");
        platform.setEplplatformname("Databricks Test");
        platform.setEplhosturl("http://localhost:8089");
        platform.setEplapiToken("test-token");
        platform.setEplsyncenabled(true);
        platform.setEplsyncFrequencyHours(1);
        platform.setEplcreatedat(new Timestamp(System.currentTimeMillis()));
        return platform;
    }

    private ExternalModel createMockModel(ExternalPlatformIntegration platform) {
        ExternalModel model = new ExternalModel();
        model.setIduuid(UUID.randomUUID().toString());
        model.setPlatform(platform);
        model.setExmexternalid("test-model-1:1");
        model.setExmexternalname("test-model-1");
        model.setExmexternalversion("1");
        model.setExmcreatedat(new Timestamp(System.currentTimeMillis()));
        return model;
    }
}
```

### 3. Crear Test E2E Snowflake

**Similar estructura para Snowflake:**

```java
@SpringBootTest
@Testcontainers
class SnowflakeIntegrationE2ETest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("codeflowx_test");

    @Test
    void testCatalogDatasetsFromSnowflake_E2E() {
        // Similar estructura pero con mock JDBC o Testcontainers Snowflake
    }
}
```

### 4. Configurar Pipeline CI/CD

**Crear:** `.github/workflows/test-e2e-integrations.yml`

```yaml
name: E2E Integration Tests

on:
  pull_request:
    paths:
      - '**/integrations/**'
      - '**/*ConnectorService.java'
  workflow_dispatch:

jobs:
  e2e-tests:
    runs-on: ubuntu-latest
    
    services:
      postgres:
        image: postgres:15
        env:
          POSTGRES_DB: codeflowx_test
          POSTGRES_USER: test
          POSTGRES_PASSWORD: test
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5

    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      
      - name: Run E2E Tests
        run: |
          mvn test -Dtest=*IntegrationE2ETest
        env:
          SPRING_DATASOURCE_URL: jdbc:postgresql://localhost:5432/codeflowx_test
          SPRING_DATASOURCE_USERNAME: test
          SPRING_DATASOURCE_PASSWORD: test
```

---

## VALIDACIÓN

### Checklist de Tests E2E

- ✅ Test sync desde plataforma externa → CodeflowX
- ✅ Test notificación desde CodeflowX → plataforma externa
- ✅ Test recuperación ante fallos de API
- ✅ Test actualización de estado de plataforma
- ✅ Test validación de integridad de metadata
- ✅ Test rate limiting en webhooks
- ✅ Test retry con backoff exponencial

---

## CUMPLIMIENTO EU AI ACT

| Artículo | Cobertura |
|----------|-----------|
| **Art. 15** (Robustez, Validación) | ✅ Validación mediante tests E2E de integraciones |

---

**Prioridad:** 🟡 **MEDIA**  
**Fecha Límite:** Febrero 2026  
**Responsable:** QA Team + Java Backend Team

---

**Estado:** ✅ COMPLETADO

