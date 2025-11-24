# PROMPT: INC-INT-006 - Retry con Backoff Exponencial para Llamadas API Externas

**Incidencia:** INC-INT-006  
**Prioridad:** 🟡 MEDIA  
**Artículo EU AI Act:** Art. 15 (Robustez)  
**Esfuerzo Estimado:** 1-2 días  
**Tipo:** Java - Backend  
**Documento Relacionado:** `AUDITORIA_INTEGRACION_SIN_SUSTITUCION.md`

---

## CONTEXTO

Las llamadas a APIs de Databricks, Snowflake, Azure ML no implementan retry con backoff exponencial para manejar errores transitorios (timeouts, rate limits, errores 5xx). Esto puede causar falsos negativos si hay errores temporales de red.

**Riesgo:**
- Falsos negativos si hay errores transitorios de red
- No se aprovecha el retry automático para mejorar resiliencia
- Puede requerir intervención manual si falla temporalmente

**Ubicación Actual:**
- `DatabricksConnectorService.java` - sin retry
- `SnowflakeConnectorService.java` - sin retry (aunque JDBC puede tener retry interno)
- `AzureMLConnectorService.java` - sin retry

---

## REQUISITOS

1. **Implementar retry con Spring Retry** para llamadas API externas
2. **Backoff exponencial** configurable por plataforma
3. **Manejo de excepciones específicas** (5xx, timeouts, rate limits)
4. **Logging** de reintentos
5. **Métricas** de reintentos

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Habilitar Spring Retry

**Añadir dependencia Maven:**

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.springframework.retry</groupId>
    <artifactId>spring-retry</artifactId>
    <version>2.0.3</version>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aspects</artifactId>
    <version>5.3.23</version>
</dependency>
```

**Habilitar en configuración:**

```java
@Configuration
@EnableRetry
public class RetryConfiguration {
}
```

### 2. Modificar `DatabricksConnectorService.java`

```java
package com.codeflowx.govern.business.integrations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.net.SocketTimeoutException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DatabricksConnectorService {

    /**
     * Lista modelos desde Databricks con retry automático.
     * 
     * Retry en caso de:
     * - WebClientResponseException (5xx)
     * - SocketTimeoutException
     * - Rate limit errors (429)
     */
    @Retryable(
        value = {
            WebClientResponseException.InternalServerError.class,
            WebClientResponseException.ServiceUnavailable.class,
            WebClientResponseException.TooManyRequests.class,
            SocketTimeoutException.class
        },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2, maxDelay = 10000)
    )
    public JsonNode invokeListModels(ExternalPlatformIntegration platform) {
        log.debug("Invoking Databricks API (attempt may be retried)");

        WebClient client = buildClient(platform);
        
        return client
            .get()
            .uri("/api/2.0/mlflow/registered-models/list")
            .retrieve()
            .bodyToMono(JsonNode.class)
            .block(Duration.ofMillis(timeoutMs));
    }

    /**
     * Handler cuando todos los reintentos fallan.
     */
    @Recover
    public JsonNode recoverListModels(
            Exception ex,
            ExternalPlatformIntegration platform) {
        
        log.error("❌ All retry attempts failed for Databricks API: {}", ex.getMessage());
        
        // Actualizar estado plataforma
        platform.setEplsyncstatus("ERROR");
        platform.setEpllasterror("API call failed after retries: " + ex.getMessage());
        
        // Lanzar excepción para que el servicio lo maneje
        throw new RuntimeException("Databricks API call failed after retries", ex);
    }

    /**
     * Sync modelos con retry para cada operación.
     */
    @Retryable(
        value = {RuntimeException.class},
        maxAttempts = 2,  // Menos reintentos para sync completo
        backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void syncModelsFromDatabricks(Long platformId) {
        // ... código existente de sync ...
    }
}
```

### 3. Modificar `SnowflakeConnectorService.java`

```java
@Service
@RequiredArgsConstructor
public class SnowflakeConnectorService {

    /**
     * Ejecuta query Snowflake con retry.
     * 
     * Nota: JDBC puede tener retry interno, pero añadimos retry adicional
     * para manejar errores de conexión.
     */
    @Retryable(
        value = {
            SQLException.class,
            SQLTimeoutException.class
        },
        maxAttempts = 2,  // Snowflake ya tiene retry interno
        backoff = @Backoff(delay = 2000)
    )
    public List<ExternalDataset> catalogDatasetsFromSnowflake(
            String database, String schema, Long platformId) {
        
        log.debug("Cataloging Snowflake datasets (may retry on failure)");

        try (Connection conn = getConnection(database, schema)) {
            // ... código existente ...
        } catch (SQLException e) {
            log.warn("SQL error (will retry): {}", e.getMessage());
            throw new RuntimeException("Snowflake query failed", e);
        }
    }

    @Recover
    public List<ExternalDataset> recoverCatalogDatasets(
            Exception ex,
            String database, String schema, Long platformId) {
        
        log.error("❌ All retry attempts failed for Snowflake catalog: {}", ex.getMessage());
        return List.of(); // Devolver lista vacía en lugar de fallar completamente
    }
}
```

### 4. Modificar `AzureMLConnectorService.java`

```java
@Service
@RequiredArgsConstructor
public class AzureMLConnectorService {

    /**
     * Lista deployments Azure ML con retry.
     */
    @Retryable(
        value = {
            HttpException.class,  // Azure SDK
            IOException.class,
            SocketTimeoutException.class
        },
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000, multiplier = 2, maxDelay = 8000)
    )
    public List<AzureDeployment> listDeployments() {
        log.debug("Listing Azure ML deployments (may retry on failure)");
        
        // ... código existente ...
    }

    /**
     * Obtiene métricas de deployment con retry.
     */
    @Retryable(
        value = {HttpException.class, IOException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public AzureDeploymentMetrics getDeploymentMetrics(
            String endpointName, String deploymentName, int days) {
        
        // ... código existente ...
    }
}
```

### 5. Configuración por Plataforma

**Añadir a `application.properties`:**

```properties
# Retry configuration
retry.databricks.max-attempts=3
retry.databricks.delay-ms=1000
retry.databricks.multiplier=2
retry.databricks.max-delay-ms=10000

retry.snowflake.max-attempts=2
retry.snowflake.delay-ms=2000

retry.azure-ml.max-attempts=3
retry.azure-ml.delay-ms=2000
retry.azure-ml.multiplier=2
retry.azure-ml.max-delay-ms=8000
```

### 6. Añadir Métricas de Retry

**Crear interceptor para métricas:**

```java
@Component
@Slf4j
@RequiredArgsConstructor
public class RetryMetricsInterceptor implements MethodInterceptor {

    private final MeterRegistry meterRegistry;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String methodName = invocation.getMethod().getName();
        String className = invocation.getMethod().getDeclaringClass().getSimpleName();
        
        Timer.Sample sample = Timer.start(meterRegistry);
        int attemptCount = 0;
        
        try {
            Object result = invocation.proceed();
            sample.stop(Timer.builder("retry.attempt.success")
                .tag("class", className)
                .tag("method", methodName)
                .register(meterRegistry));
            return result;
        } catch (Exception e) {
            attemptCount++;
            sample.stop(Timer.builder("retry.attempt.failure")
                .tag("class", className)
                .tag("method", methodName)
                .tag("exception", e.getClass().getSimpleName())
                .register(meterRegistry));
            throw e;
        }
    }
}
```

---

## VALIDACIÓN

### Tests Unitarios

**Crear:** `DatabricksConnectorServiceRetryTest.java`

```java
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@EnableRetry
class DatabricksConnectorServiceRetryTest {

    @Autowired
    private DatabricksConnectorService databricksConnector;

    @MockBean
    private WebClient.Builder webClientBuilder;

    @Test
    void testRetryOnServerError() {
        // Given - Simular error 5xx en primeros 2 intentos
        when(webClient.get())
            .thenThrow(new WebClientResponseException.InternalServerError(
                500, "Internal Server Error", null, null, null))
            .thenThrow(new WebClientResponseException.InternalServerError(
                500, "Internal Server Error", null, null, null))
            .thenReturn(createMockResponse());

        // When
        JsonNode result = databricksConnector.invokeListModels(platform);

        // Then - Debe haber reintentado y finalmente obtener resultado
        assertThat(result).isNotNull();
        verify(webClient, times(3)).get(); // 1 inicial + 2 reintentos
    }

    @Test
    void testRetryExhausted() {
        // Given - Simular error persistente
        when(webClient.get())
            .thenThrow(new WebClientResponseException.InternalServerError(
                500, "Internal Server Error", null, null, null));

        // When/Then - Debe lanzar excepción después de todos los reintentos
        assertThatThrownBy(() -> databricksConnector.invokeListModels(platform))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("after retries");
    }
}
```

---

## CUMPLIMIENTO EU AI ACT

| Artículo | Cobertura |
|----------|-----------|
| **Art. 15** (Robustez) | ✅ Resiliencia ante errores transitorios mediante retry |

---

**Prioridad:** 🟡 **MEDIA**  
**Fecha Límite:** Febrero 2026  
**Responsable:** Java Backend Team

---

**Estado:** ✅ COMPLETADO

