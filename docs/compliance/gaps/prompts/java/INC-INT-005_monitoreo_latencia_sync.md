# PROMPT: INC-INT-005 - Monitoreo de Latencia de Sincronización

**Incidencia:** INC-INT-005  
**Prioridad:** 🟠 ALTA  
**Artículo EU AI Act:** Art. 72 (Vigilancia post-comercialización)  
**Esfuerzo Estimado:** 3-4 días  
**Tipo:** Java - Backend (Observabilidad)  
**Documento Relacionado:** `AUDITORIA_INTEGRACION_SIN_SUSTITUCION.md`

---

## CONTEXTO

No existe métrica ni alerta para detectar cuando la sincronización con plataformas externas está tardando más de lo esperado o fallando silenciosamente. Esto impide detectar problemas de performance o disponibilidad de las plataformas externas.

**Riesgo:**
- No se detecta si Databricks/Snowflake está lento o caído
- Sync puede fallar sin que nadie se dé cuenta
- Datos de governance pueden estar desactualizados
- No se puede medir SLA de sincronización

**Ubicación Actual:**
- `ExternalPlatformSyncService.java` - sync sin métricas de latencia
- `DatabricksConnectorService.java` - sync sin timing
- Falta dashboard de monitoreo

---

## REQUISITOS

1. **Añadir métricas Prometheus** para latencia de sync por plataforma
2. **Alertas Grafana** para syncs lentos o fallidos
3. **Dashboard de monitoreo** en UI mostrando métricas de sync
4. **Logging estructurado** de latencia de syncs
5. **Job de verificación** de syncs pendientes

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Modificar `ExternalPlatformSyncService.java`

**Añadir métricas Prometheus:**

```java
package com.codeflowx.govern.service.external;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * Servicio de orquestación de sync con plataformas externas.
 * 
 * Añade métricas de latencia y monitoreo.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalPlatformSyncService {

    private final MeterRegistry meterRegistry;
    private final ExternalIntegrationBusinessService integrationService;
    private final DatabricksConnectorService databricksConnector;
    private final SnowflakeConnectorService snowflakeConnector;
    // ... otros conectores ...

    /**
     * Sincroniza una plataforma externa con métricas de latencia.
     * 
     * @param platformId ID de la plataforma
     */
    @Transactional
    public void syncPlatform(Long platformId) {
        ExternalPlatformIntegration platform = integrationService.findPlatformById(platformId);
        if (platform == null) {
            log.warn("Platform {} not found", platformId);
            return;
        }

        String platformType = platform.getEplplatformtype();
        long startTime = System.currentTimeMillis();

        // Actualizar estado
        platform.setEplsyncstatus("IN_PROGRESS");
        platform.setEpllastsyncat(new Timestamp(startTime));
        integrationService.savePlatform(platform);

        // Sincronizar con métricas
        Timer.Sample sample = Timer.start(meterRegistry);
        boolean success = false;
        String errorMessage = null;

        try {
            log.info("🔄 Starting sync for platform: {} ({})", 
                    platform.getEplplatformname(), platformType);

            // Ejecutar sync según tipo de plataforma
            switch (platformType.toUpperCase()) {
                case "DATABRICKS":
                    databricksConnector.syncModelsFromDatabricks(platformId);
                    break;
                case "SNOWFLAKE":
                    snowflakeConnector.catalogDatasetsFromSnowflake(platformId);
                    break;
                // ... otros casos ...
                default:
                    log.warn("Platform type not supported: {}", platformType);
                    return;
            }

            success = true;
            long duration = System.currentTimeMillis() - startTime;

            // Registrar métrica de éxito
            sample.stop(Timer.builder("sync.external.platform.duration")
                .tag("platform_type", platformType)
                .tag("platform_name", platform.getEplplatformname())
                .tag("status", "success")
                .description("Duration of external platform sync")
                .register(meterRegistry));

            // Registrar métrica de latencia
            meterRegistry.counter("sync.external.platform.count",
                "platform_type", platformType,
                "status", "success"
            ).increment();

            log.info("✅ Platform sync completed: {} ({}ms)", 
                    platform.getEplplatformname(), duration);

            // Verificar latencia (alerta si > 5 minutos)
            if (duration > 5 * 60 * 1000) { // 5 minutos
                log.warn("⚠️ Slow sync detected: platform={}, duration={}ms", 
                        platform.getEplplatformname(), duration);
                alertSlowSync(platform, duration);
            }

            // Actualizar estado éxito
            platform.setEplsyncstatus("SUCCESS");
            platform.setEpllasterror(null);

        } catch (Exception e) {
            success = false;
            errorMessage = e.getMessage();
            long duration = System.currentTimeMillis() - startTime;

            log.error("❌ Platform sync failed: {} ({}ms) - {}", 
                     platform.getEplplatformname(), duration, errorMessage, e);

            // Registrar métrica de error
            sample.stop(Timer.builder("sync.external.platform.duration")
                .tag("platform_type", platformType)
                .tag("platform_name", platform.getEplplatformname())
                .tag("status", "error")
                .description("Duration of external platform sync")
                .register(meterRegistry));

            meterRegistry.counter("sync.external.platform.count",
                "platform_type", platformType,
                "status", "error"
            ).increment();

            // Alertar error
            alertSyncError(platform, errorMessage);

            // Actualizar estado error
            platform.setEplsyncstatus("ERROR");
            platform.setEpllasterror(errorMessage);

        } finally {
            platform.setEpllastsyncat(new Timestamp(System.currentTimeMillis()));
            integrationService.savePlatform(platform);
        }
    }

    /**
     * Sincroniza todas las plataformas habilitadas.
     */
    @Scheduled(cron = "0 0 * * * *")  // Cada hora
    public void syncAllPlatforms() {
        log.info("🔄 Starting sync for all enabled platforms");

        List<ExternalPlatformIntegration> platforms = integrationService
            .findPlatformsBySyncEnabled(true);

        int successCount = 0;
        int errorCount = 0;

        for (ExternalPlatformIntegration platform : platforms) {
            try {
                syncPlatform(platform.getIdxexternalplatform());
                successCount++;
            } catch (Exception e) {
                errorCount++;
                log.error("Error syncing platform {}: {}", 
                         platform.getEplplatformname(), e.getMessage());
            }
        }

        log.info("✅ Sync all platforms completed: {} success, {} errors", 
                successCount, errorCount);

        // Registrar métricas agregadas
        meterRegistry.counter("sync.external.platform.batch.success").increment(successCount);
        meterRegistry.counter("sync.external.platform.batch.errors").increment(errorCount);
    }

    /**
     * Envía alerta cuando sync es lento.
     */
    private void alertSlowSync(ExternalPlatformIntegration platform, long durationMs) {
        // Registrar en ImmutableLog
        String details = String.format(
            "{\"platform\":\"%s\",\"duration_ms\":%d,\"threshold_ms\":300000}",
            platform.getEplplatformname(), durationMs
        );

        // TODO: Integrar con ImmutableLogService
        log.warn("⚠️ Slow sync alert: {}", details);

        // TODO: Enviar notificación a administradores (email, Slack, etc.)
    }

    /**
     * Envía alerta cuando sync falla.
     */
    private void alertSyncError(ExternalPlatformIntegration platform, String error) {
        // Registrar en ImmutableLog
        String details = String.format(
            "{\"platform\":\"%s\",\"error\":\"%s\"}",
            platform.getEplplatformname(), error
        );

        log.error("❌ Sync error alert: {}", details);

        // TODO: Enviar notificación a administradores
    }

    /**
     * Verifica syncs pendientes (stuck IN_PROGRESS > 15 minutos).
     */
    @Scheduled(cron = "0 */15 * * * ?")  // Cada 15 minutos
    public void checkStuckSyncs() {
        log.debug("Checking for stuck syncs...");

        List<ExternalPlatformIntegration> stuckPlatforms = integrationService
            .findPlatformsBySyncStatusAndLastSyncAge("IN_PROGRESS", 15); // > 15 minutos

        for (ExternalPlatformIntegration platform : stuckPlatforms) {
            log.warn("⚠️ Stuck sync detected: platform={}, last_sync={}", 
                    platform.getEplplatformname(), platform.getEpllastsyncat());

            // Marcar como error
            platform.setEplsyncstatus("ERROR");
            platform.setEpllasterror("Sync stuck in IN_PROGRESS state for > 15 minutes");
            integrationService.savePlatform(platform);

            // Alertar
            alertSyncError(platform, "Sync stuck in IN_PROGRESS state");
        }

        if (!stuckPlatforms.isEmpty()) {
            log.warn("Found {} stuck syncs", stuckPlatforms.size());
        }
    }
}
```

### 2. Añadir Métricas al Repositorio

**Modificar `ExternalIntegrationBusinessService.java`:**

```java
public List<ExternalPlatformIntegration> findPlatformsBySyncStatusAndLastSyncAge(
        String syncStatus, int ageMinutes) {
    
    Timestamp threshold = new Timestamp(
        System.currentTimeMillis() - (ageMinutes * 60 * 1000L)
    );

    String jpql = """
        SELECT p FROM ExternalPlatformIntegration p
        WHERE p.eplsyncstatus = :syncStatus
        AND p.epllastsyncat < :threshold
        """;

    return entityManager.createQuery(jpql, ExternalPlatformIntegration.class)
        .setParameter("syncStatus", syncStatus)
        .setParameter("threshold", threshold)
        .getResultList();
}
```

### 3. Crear Dashboard Grafana JSON

**Ubicación:** `monitoring/grafana/dashboards/external-platforms-sync.json`

```json
{
  "dashboard": {
    "title": "External Platforms Sync Monitoring",
    "panels": [
      {
        "title": "Sync Duration by Platform",
        "targets": [
          {
            "expr": "histogram_quantile(0.95, rate(sync_external_platform_duration_bucket[5m]))",
            "legendFormat": "{{platform_type}} - P95"
          },
          {
            "expr": "histogram_quantile(0.50, rate(sync_external_platform_duration_bucket[5m]))",
            "legendFormat": "{{platform_type}} - P50"
          }
        ]
      },
      {
        "title": "Sync Success/Error Rate",
        "targets": [
          {
            "expr": "rate(sync_external_platform_count{status=\"success\"}[5m])",
            "legendFormat": "Success - {{platform_type}}"
          },
          {
            "expr": "rate(sync_external_platform_count{status=\"error\"}[5m])",
            "legendFormat": "Error - {{platform_type}}"
          }
        ]
      },
      {
        "title": "Last Successful Sync",
        "targets": [
          {
            "expr": "time() - sync_external_platform_last_success_timestamp",
            "legendFormat": "{{platform_name}} - seconds since last success"
          }
        ]
      }
    ],
    "alerting": {
      "alerts": [
        {
          "name": "Slow Sync Warning",
          "condition": "histogram_quantile(0.95, rate(sync_external_platform_duration_bucket[5m])) > 300000",
          "message": "Sync duration P95 > 5 minutes for platform {{platform_type}}"
        },
        {
          "name": "Sync Failed Critical",
          "condition": "rate(sync_external_platform_count{status=\"error\"}[1h]) > 0.1",
          "message": "Sync failure rate > 10% in last hour"
        },
        {
          "name": "Stuck Sync",
          "condition": "time() - sync_external_platform_last_success_timestamp > 900",
          "message": "No successful sync in last 15 minutes"
        }
      ]
    }
  }
}
```

### 4. Crear Dashboard UI (ViewModel)

**Crear:** `ExternalPlatformsMonitoringViewModel.java`

```java
package com.codeflowx.govern.viewmodel.integrations;

import com.codeflowx.govern.entity.integrations.ExternalPlatformIntegration;
import com.codeflowx.govern.service.external.ExternalPlatformSyncService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@VariableResolver(DelegatingVariableResolver.class)
public class ExternalPlatformsMonitoringViewModel {

    @WireVariable
    private ExternalIntegrationBusinessService integrationService;

    @WireVariable
    private ExternalPlatformSyncService syncService;

    @Getter @Setter
    private List<ExternalPlatformIntegration> platforms = new ArrayList<>();

    @Getter @Setter
    private Map<String, Object> syncMetrics = new HashMap<>();

    @Init
    public void init() {
        loadPlatforms();
        loadSyncMetrics();
    }

    @Command
    @NotifyChange({"platforms", "syncMetrics"})
    public void refresh() {
        loadPlatforms();
        loadSyncMetrics();
    }

    private void loadPlatforms() {
        platforms = integrationService.findAllPlatforms();
    }

    private void loadSyncMetrics() {
        // Obtener métricas desde Prometheus o BD
        syncMetrics = new HashMap<>();
        
        long totalPlatforms = platforms.size();
        long successCount = platforms.stream()
            .filter(p -> "SUCCESS".equals(p.getEplsyncstatus()))
            .count();
        long errorCount = platforms.stream()
            .filter(p -> "ERROR".equals(p.getEplsyncstatus()))
            .count();
        long inProgressCount = platforms.stream()
            .filter(p -> "IN_PROGRESS".equals(p.getEplsyncstatus()))
            .count();

        syncMetrics.put("totalPlatforms", totalPlatforms);
        syncMetrics.put("successCount", successCount);
        syncMetrics.put("errorCount", errorCount);
        syncMetrics.put("inProgressCount", inProgressCount);
        syncMetrics.put("successRate", totalPlatforms > 0 ? 
                       (double) successCount / totalPlatforms * 100 : 0);
    }

    @Command
    @NotifyChange("platforms")
    public void syncNow(@BindingParam("platform") ExternalPlatformIntegration platform) {
        try {
            syncService.syncPlatform(platform.getIdxexternalplatform());
            loadPlatforms();
        } catch (Exception e) {
            log.error("Error syncing platform: {}", e.getMessage(), e);
        }
    }
}
```

---

## VALIDACIÓN

### Tests Unitarios

**Crear:** `ExternalPlatformSyncServiceTest.java`

```java
@ExtendWith(MockitoExtension.class)
class ExternalPlatformSyncServiceTest {

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private ExternalIntegrationBusinessService integrationService;

    @Mock
    private DatabricksConnectorService databricksConnector;

    @InjectMocks
    private ExternalPlatformSyncService syncService;

    @Test
    void testSyncPlatform_Success() {
        // Given
        ExternalPlatformIntegration platform = createMockPlatform();
        when(integrationService.findPlatformById(1L)).thenReturn(platform);

        // When
        syncService.syncPlatform(1L);

        // Then
        verify(databricksConnector).syncModelsFromDatabricks(1L);
        verify(integrationService).savePlatform(argThat(p -> 
            "SUCCESS".equals(p.getEplsyncstatus())
        ));
    }

    @Test
    void testSyncPlatform_Error() {
        // Given
        ExternalPlatformIntegration platform = createMockPlatform();
        when(integrationService.findPlatformById(1L)).thenReturn(platform);
        doThrow(new RuntimeException("Connection failed"))
            .when(databricksConnector).syncModelsFromDatabricks(1L);

        // When
        syncService.syncPlatform(1L);

        // Then
        verify(integrationService).savePlatform(argThat(p -> 
            "ERROR".equals(p.getEplsyncstatus())
        ));
    }
}
```

---

## CUMPLIMIENTO EU AI ACT

| Artículo | Cobertura |
|----------|-----------|
| **Art. 72** (Vigilancia) | ✅ Monitoreo continuo de sincronización con plataformas externas |

---

**Prioridad:** 🟠 **ALTA**  
**Fecha Límite:** Enero 2026  
**Responsable:** Java Backend Team + Observability Team

---

**Estado:** ✅ COMPLETADO

