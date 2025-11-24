# PROMPT: INC-012-007 - Justificación de Rollback en ImmutableLog

**Incidencia:** INC-012-007  
**Prioridad:** 🟡 MEDIA  
**Artículo EU AI Act:** Art. 12 EU AI Act  
**Esfuerzo Estimado:** 2 días  
**Tipo:** Java

---

## CONTEXTO

Cuando se ejecuta rollback automático por fallo en despliegue, el sistema registra el evento pero no incluye justificación detallada del motivo del rollback en el ImmutableLog. Esto dificulta el análisis post-mortem y la trazabilidad completa requerida por el EU AI Act.

**Ubicación Actual:**
- Delegate: `RollbackModelDelegate.java`
- Entidad: `ImmutableLog` (tabla `IMLIMMUTABLELOGS`)
- Proceso BPMN: `deployment-automation-v1.bpmn`

**Problema:**
- Falta de trazabilidad completa de rollbacks
- Dificultad para análisis post-mortem
- No se registra motivo detallado del rollback

---

## REQUISITOS

1. **Registrar motivo detallado** del rollback en ImmutableLog
2. **Incluir métricas de health check** al momento del fallo
3. **Incluir logs de error** del despliegue
4. **Registrar con categoría** `DEPLOYMENT_ROLLBACK`
5. **Incluir data snapshot completo** del estado al momento del rollback

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Modificar Delegate: `RollbackModelDelegate.java`

**Ubicación:** `com.codeflowx.govern.delegate.bpmn.RollbackModelDelegate`

```java
package com.codeflowx.govern.delegate.bpmn;

import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.entity.serving.ModelDeployment;
import com.codeflowx.govern.entity.logging.ImmutableLog;
import com.codeflowx.govern.service.BusinessService;
import com.codeflowx.govern.service.logging.ImmutableLogService;
import com.codeflowx.govern.service.serving.DeploymentHealthCheckService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Delegate para ejecutar rollback de modelo
 * Requisito: INC-012-007 - Art. 12 EU AI Act (Justificación rollback en ImmutableLog)
 */
@Component("rollbackModelDelegate")
public class RollbackModelDelegate implements JavaDelegate {
    
    private static final Logger log = LoggerFactory.getLogger(RollbackModelDelegate.class);
    
    @Autowired
    private BusinessService businessService;
    
    @Autowired
    private ImmutableLogService immutableLogService;
    
    @Autowired
    private DeploymentHealthCheckService deploymentHealthCheckService;
    
    @Override
    public void execute(DelegateExecution execution) {
        Long deploymentId = (Long) execution.getVariable("deploymentId");
        String rollbackReason = (String) execution.getVariable("rollbackReason");
        
        if (deploymentId == null) {
            log.error("Deployment ID is required for rollback");
            return;
        }
        
        log.info("Ejecutando rollback para deployment: {}", deploymentId);
        
        // Obtener deployment
        ModelDeployment deployment = businessService.findById(ModelDeployment.class, deploymentId);
        if (deployment == null) {
            log.error("Deployment not found: {}", deploymentId);
            return;
        }
        
        // Obtener modelo
        Model model = businessService.findById(Model.class, deployment.getModelId());
        
        // Obtener motivo del rollback (si no viene en variable, intentar inferirlo)
        String detailedReason = determineRollbackReason(execution, deployment, rollbackReason);
        
        // Obtener métricas de health check al momento del fallo
        Map<String, Object> healthCheckMetrics = getHealthCheckMetricsAtFailure(deployment);
        
        // Obtener logs de error del despliegue
        String deploymentErrorLogs = getDeploymentErrorLogs(deployment);
        
        // Ejecutar rollback (lógica existente)
        performRollback(deployment);
        
        // NUEVO: Registrar en ImmutableLog con justificación detallada
        registerRollbackInImmutableLog(
            deployment, 
            model, 
            detailedReason, 
            healthCheckMetrics, 
            deploymentErrorLogs,
            execution
        );
        
        log.info("Rollback completado para deployment: {}", deploymentId);
    }
    
    /**
     * Determina motivo detallado del rollback
     */
    private String determineRollbackReason(DelegateExecution execution, 
                                         ModelDeployment deployment, 
                                         String rollbackReason) {
        if (rollbackReason != null && !rollbackReason.trim().isEmpty()) {
            return rollbackReason;
        }
        
        // Intentar inferir motivo desde variables de ejecución
        String errorType = (String) execution.getVariable("errorType");
        String errorMessage = (String) execution.getVariable("errorMessage");
        
        StringBuilder reason = new StringBuilder();
        reason.append("Rollback automático ejecutado. ");
        
        if (errorType != null) {
            reason.append("Tipo de error: ").append(errorType).append(". ");
        }
        
        if (errorMessage != null) {
            reason.append("Mensaje: ").append(errorMessage).append(". ");
        }
        
        // Verificar si fue por health check
        Boolean healthCheckFailed = (Boolean) execution.getVariable("healthCheckFailed");
        if (healthCheckFailed != null && healthCheckFailed) {
            reason.append("Health check falló. ");
        }
        
        // Verificar si fue por timeout
        Boolean timeout = (Boolean) execution.getVariable("timeout");
        if (timeout != null && timeout) {
            reason.append("Timeout durante despliegue. ");
        }
        
        return reason.toString();
    }
    
    /**
     * Obtiene métricas de health check al momento del fallo
     */
    private Map<String, Object> getHealthCheckMetricsAtFailure(ModelDeployment deployment) {
        Map<String, Object> metrics = new HashMap<>();
        
        try {
            // Obtener último health check antes del fallo
            Map<String, Object> lastHealthCheck = deploymentHealthCheckService
                .getLastHealthCheckBeforeFailure(deployment.getId());
            
            if (lastHealthCheck != null) {
                metrics.put("timestamp", lastHealthCheck.get("timestamp"));
                metrics.put("status", lastHealthCheck.get("status"));
                metrics.put("responseTime", lastHealthCheck.get("responseTime"));
                metrics.put("errorRate", lastHealthCheck.get("errorRate"));
                metrics.put("cpuUsage", lastHealthCheck.get("cpuUsage"));
                metrics.put("memoryUsage", lastHealthCheck.get("memoryUsage"));
                metrics.put("requestCount", lastHealthCheck.get("requestCount"));
            } else {
                metrics.put("status", "No health check data available");
            }
        } catch (Exception e) {
            log.warn("Error obteniendo métricas de health check: {}", e.getMessage());
            metrics.put("error", "Error retrieving health check metrics: " + e.getMessage());
        }
        
        return metrics;
    }
    
    /**
     * Obtiene logs de error del despliegue
     */
    private String getDeploymentErrorLogs(ModelDeployment deployment) {
        try {
            // Obtener logs de error desde DeploymentLog
            String query = "SELECT srl_message FROM SRVDEPLOYMENTLOGS " +
                          "WHERE srl_deployment_id = ? AND srl_level = 'ERROR' " +
                          "ORDER BY srl_recorded_at DESC LIMIT 50";
            List<String> errorLogs = businessService.findBySQL(String.class, query, deployment.getId());
            
            return String.join("\n", errorLogs);
        } catch (Exception e) {
            log.warn("Error obteniendo logs de error: {}", e.getMessage());
            return "Error retrieving error logs: " + e.getMessage();
        }
    }
    
    /**
     * Ejecuta rollback (lógica existente)
     */
    private void performRollback(ModelDeployment deployment) {
        // Lógica existente de rollback
        // TODO: Mantener lógica actual
        deployment.setStatus("ROLLED_BACK");
        businessService.save(deployment);
    }
    
    /**
     * Registra rollback en ImmutableLog con justificación detallada
     * Requisito: INC-012-007 - Art. 12 EU AI Act
     */
    private void registerRollbackInImmutableLog(ModelDeployment deployment,
                                               Model model,
                                               String detailedReason,
                                               Map<String, Object> healthCheckMetrics,
                                               String deploymentErrorLogs,
                                               DelegateExecution execution) {
        try {
            // Construir data snapshot completo
            Map<String, Object> dataSnapshot = new HashMap<>();
            dataSnapshot.put("deploymentId", deployment.getId());
            dataSnapshot.put("modelId", deployment.getModelId());
            dataSnapshot.put("modelName", model != null ? model.getModname() : "N/A");
            dataSnapshot.put("deploymentEnvironment", deployment.getEnvironment());
            dataSnapshot.put("deploymentVersion", deployment.getVersion());
            dataSnapshot.put("rollbackReason", detailedReason);
            dataSnapshot.put("rollbackTimestamp", new Timestamp(System.currentTimeMillis()));
            dataSnapshot.put("healthCheckMetrics", healthCheckMetrics);
            dataSnapshot.put("deploymentErrorLogs", deploymentErrorLogs);
            dataSnapshot.put("processInstanceId", execution.getProcessInstanceId());
            dataSnapshot.put("executionId", execution.getId());
            
            // Obtener usuario que inició despliegue (si disponible)
            String userId = (String) execution.getVariable("userId");
            String userName = (String) execution.getVariable("userName");
            
            // Registrar en ImmutableLog
            immutableLogService.createLogEntry(
                "MODEL_DEPLOYMENT",
                deployment.getId(),
                "DEPLOYMENT_ROLLBACK",
                userId != null ? Long.parseLong(userId) : null,
                userName != null ? userName : "SYSTEM",
                dataSnapshot
            );
            
            log.info("Rollback registrado en ImmutableLog para deployment: {}", deployment.getId());
            
        } catch (Exception e) {
            log.error("Error registrando rollback en ImmutableLog: {}", e.getMessage(), e);
            // No lanzar excepción para no interrumpir rollback
        }
    }
}
```

### 2. Modificar Service: `ImmutableLogService.java`

**Ubicación:** `com.codeflowx.govern.service.logging.ImmutableLogService`

Asegurar que método `createLogEntry` acepta data snapshot:

```java
/**
 * Crea entrada en ImmutableLog
 */
public void createLogEntry(String entityType,
                          Long entityId,
                          String action,
                          Long userId,
                          String userName,
                          Map<String, Object> dataSnapshot) {
    // Serializar data snapshot a JSON
    ObjectMapper mapper = new ObjectMapper();
    String dataJson;
    try {
        dataJson = mapper.writeValueAsString(dataSnapshot);
    } catch (Exception e) {
        log.error("Error serializando data snapshot: {}", e.getMessage());
        dataJson = "{}";
    }
    
    // Obtener hash anterior
    String previousHash = getLastHash();
    
    // Calcular hash actual
    String currentHash = calculateHash(previousHash, entityType, entityId, action, userId, dataJson);
    
    // Crear entrada
    ImmutableLog log = new ImmutableLog();
    log.setImlentitytype(entityType);
    log.setImlentityid(entityId);
    log.setImlaction(action);
    log.setImluserid(userId);
    log.setImlusername(userName);
    log.setImldata(dataJson);
    log.setImlprevioushash(previousHash);
    log.setImlcurrenthash(currentHash);
    log.setImltimestampepoch(System.currentTimeMillis());
    
    businessService.save(log);
}
```

### 3. Añadir Service para Health Check (si no existe)

**Ubicación:** `com.codeflowx.govern.service.serving.DeploymentHealthCheckService`

```java
/**
 * Obtiene último health check antes del fallo
 */
public Map<String, Object> getLastHealthCheckBeforeFailure(Long deploymentId) {
    String query = "SELECT * FROM DEPLOYMENTHEALTHCHECKS " +
                  "WHERE deployment_id = ? AND status != 'HEALTHY' " +
                  "ORDER BY checked_at DESC LIMIT 1";
    // TODO: Implementar según estructura real
    return new HashMap<>();
}
```

---

## VALIDACIONES ADICIONALES RECOMENDADAS

1. **Añadir categorización** de tipos de rollback (health check, timeout, error, etc.)
2. **Incluir stack trace completo** si hay excepción
3. **Añadir métricas de rendimiento** del despliegue antes del fallo
4. **Registrar versión anterior** a la que se hace rollback

---

## PRUEBAS REQUERIDAS

1. **Test 1:** Ejecutar rollback por health check fallido → Verificar que ImmutableLog incluye métricas de health check
2. **Test 2:** Ejecutar rollback por timeout → Verificar que ImmutableLog incluye motivo "timeout"
3. **Test 3:** Ejecutar rollback por error → Verificar que ImmutableLog incluye logs de error
4. **Test 4:** Verificar que data snapshot incluye toda la información relevante
5. **Test 5:** Verificar que hash chain se mantiene correcto después de registrar rollback
6. **Test 6:** Verificar que categoría es "DEPLOYMENT_ROLLBACK"

---

## REFERENCIAS

- **Art. 12 EU AI Act:** Registro y Trazabilidad
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_012_PUESTA_PRODUCCION.md#inc-012-007`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_012_PUESTA_PRODUCCION.md`

---

## NOTAS DE IMPLEMENTACIÓN

- Ajustar obtención de métricas según estructura real de health checks
- Ajustar obtención de logs según estructura real de DeploymentLog
- Considerar hacer rollback asíncrono para no bloquear proceso
- Añadir notificación al propietario del modelo cuando ocurre rollback
- Considerar hacer análisis automático de patrones de rollback

---

**Estado:** ✅ COMPLETADO

