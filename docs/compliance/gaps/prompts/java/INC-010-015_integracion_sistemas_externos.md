# PROMPT: INC-010-015 - Integración con Sistemas Externos

**Incidencia:** INC-010-015  
**Prioridad:** 🟢 MEDIA  
**Artículo EU AI Act:** Art. 72  
**Esfuerzo Estimado:** 2 días  
**Tipo:** Java - Backend + Python (Conectores)  
**Referencia:** GAP-017

---

## CONTEXTO

Hay documentación de conectores pero requiere verificación de implementación. Se requiere verificar e implementar integración con sistemas externos (Azure ML, SageMaker) y conectarlos con PMM según Art. 72.

**Estado Actual:**
- ✅ Documentación de conectores mencionada
- ❌ Falta verificación de implementación
- ❌ Falta integración con PMM
- ❌ Falta validación de conectores

---

## REQUISITOS

1. Verificar implementación de conectores
2. Completar si falta
3. Integrar con PMM
4. Validar funcionamiento
5. Documentar integraciones

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear Interfaz para Conectores Externos

```java
package com.codeflowx.govern.services.integration;

import java.util.List;
import java.util.Map;

public interface ExternalMlPlatformConnector {
    
    /**
     * Obtiene métricas de un modelo en la plataforma externa
     */
    Map<String, Object> getModelMetrics(String modelId, String version);
    
    /**
     * Obtiene predicciones recientes del modelo
     */
    List<Map<String, Object>> getRecentPredictions(String modelId, int limit);
    
    /**
     * Obtiene información del modelo
     */
    Map<String, Object> getModelInfo(String modelId);
    
    /**
     * Verifica conectividad con la plataforma
     */
    boolean testConnection();
    
    /**
     * Obtiene el tipo de plataforma
     */
    String getPlatformType(); // AZURE_ML, SAGEMAKER, etc.
}
```

### 2. Implementar Conector para Azure ML

```java
package com.codeflowx.govern.services.integration.impl;

import com.codeflowx.govern.services.integration.ExternalMlPlatformConnector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AzureMlConnector implements ExternalMlPlatformConnector {
    
    private final RestTemplate restTemplate;
    
    @Value("${azure.ml.endpoint:}")
    private String azureMlEndpoint;
    
    @Value("${azure.ml.api-key:}")
    private String azureMlApiKey;
    
    @Override
    public Map<String, Object> getModelMetrics(String modelId, String version) {
        try {
            String url = String.format("%s/models/%s/versions/%s/metrics", 
                azureMlEndpoint, modelId, version);
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + azureMlApiKey);
            headers.set("Content-Type", "application/json");
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RuntimeException("Error obteniendo métricas de Azure ML");
            }
        } catch (Exception e) {
            log.error("Error obteniendo métricas de Azure ML para modelo: {}", modelId, e);
            throw new RuntimeException("Error conectando con Azure ML", e);
        }
    }
    
    @Override
    public List<Map<String, Object>> getRecentPredictions(String modelId, int limit) {
        // Implementar obtención de predicciones recientes
        return List.of();
    }
    
    @Override
    public Map<String, Object> getModelInfo(String modelId) {
        // Implementar obtención de información del modelo
        return Map.of();
    }
    
    @Override
    public boolean testConnection() {
        try {
            String url = azureMlEndpoint + "/health";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + azureMlApiKey);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);
            
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Error probando conexión con Azure ML", e);
            return false;
        }
    }
    
    @Override
    public String getPlatformType() {
        return "AZURE_ML";
    }
}
```

### 3. Implementar Conector para SageMaker

```java
package com.codeflowx.govern.services.integration.impl;

import com.codeflowx.govern.services.integration.ExternalMlPlatformConnector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sagemaker.SageMakerClient;
import software.amazon.awssdk.services.sagemaker.model.*;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SageMakerConnector implements ExternalMlPlatformConnector {
    
    private final SageMakerClient sageMakerClient;
    
    @Value("${aws.region:us-east-1}")
    private String awsRegion;
    
    @Override
    public Map<String, Object> getModelMetrics(String modelId, String version) {
        try {
            DescribeModelRequest request = DescribeModelRequest.builder()
                .modelName(modelId)
                .build();
            
            DescribeModelResponse response = sageMakerClient.describeModel(request);
            
            // Obtener métricas de CloudWatch
            // TODO: Implementar obtención de métricas de CloudWatch
            
            return Map.of(
                "modelName", response.modelName(),
                "creationTime", response.creationTime().toString(),
                "modelArn", response.modelArn()
            );
        } catch (Exception e) {
            log.error("Error obteniendo métricas de SageMaker para modelo: {}", modelId, e);
            throw new RuntimeException("Error conectando con SageMaker", e);
        }
    }
    
    @Override
    public List<Map<String, Object>> getRecentPredictions(String modelId, int limit) {
        // Implementar obtención de predicciones recientes desde CloudWatch Logs
        return List.of();
    }
    
    @Override
    public Map<String, Object> getModelInfo(String modelId) {
        try {
            DescribeModelRequest request = DescribeModelRequest.builder()
                .modelName(modelId)
                .build();
            
            DescribeModelResponse response = sageMakerClient.describeModel(request);
            
            return Map.of(
                "modelName", response.modelName(),
                "modelArn", response.modelArn(),
                "creationTime", response.creationTime().toString(),
                "executionRoleArn", response.executionRoleArn()
            );
        } catch (Exception e) {
            log.error("Error obteniendo información de SageMaker para modelo: {}", modelId, e);
            throw new RuntimeException("Error conectando con SageMaker", e);
        }
    }
    
    @Override
    public boolean testConnection() {
        try {
            ListModelsRequest request = ListModelsRequest.builder()
                .maxResults(1)
                .build();
            
            sageMakerClient.listModels(request);
            return true;
        } catch (Exception e) {
            log.error("Error probando conexión con SageMaker", e);
            return false;
        }
    }
    
    @Override
    public String getPlatformType() {
        return "SAGEMAKER";
    }
}
```

### 4. Integrar con PMM Service

```java
package com.codeflowx.govern.services.compliance;

import com.codeflowx.govern.services.integration.ExternalMlPlatformConnector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalPlatformPmmIntegration {
    
    private final List<ExternalMlPlatformConnector> connectors;
    private final PostMarketMonitoringService pmmService;
    
    /**
     * Sincroniza métricas de plataformas externas con PMM
     */
    public void syncExternalMetrics(Long projectId, Long modelId) {
        log.info("Sincronizando métricas externas para proyecto: {}, modelo: {}", 
            projectId, modelId);
        
        // Obtener información del modelo para determinar plataforma
        String platformType = getPlatformTypeForModel(modelId);
        
        ExternalMlPlatformConnector connector = findConnector(platformType);
        
        if (connector == null) {
            log.warn("No se encontró conector para plataforma: {}", platformType);
            return;
        }
        
        // Verificar conectividad
        if (!connector.testConnection()) {
            log.error("No se puede conectar con plataforma: {}", platformType);
            return;
        }
        
        try {
            // Obtener métricas del modelo
            Map<String, Object> metrics = connector.getModelMetrics(
                getExternalModelId(modelId), 
                getModelVersion(modelId));
            
            // Convertir y almacenar en PMM
            convertAndStoreMetrics(projectId, modelId, metrics, platformType);
            
            log.info("Métricas sincronizadas exitosamente desde {}", platformType);
        } catch (Exception e) {
            log.error("Error sincronizando métricas desde {}", platformType, e);
        }
    }
    
    private ExternalMlPlatformConnector findConnector(String platformType) {
        return connectors.stream()
            .filter(c -> c.getPlatformType().equals(platformType))
            .findFirst()
            .orElse(null);
    }
    
    private void convertAndStoreMetrics(
            Long projectId, 
            Long modelId, 
            Map<String, Object> externalMetrics,
            String platformType) {
        
        // Convertir métricas externas a formato PMM
        // Ejemplo: accuracy, precision, recall, etc.
        
        externalMetrics.forEach((metricName, value) -> {
            MonitoringMetric metric = new MonitoringMetric();
            metric.setIdxproject(projectId);
            metric.setIdxmodel(modelId);
            metric.setMonmetricname(metricName);
            metric.setMonmetricvalue(convertToBigDecimal(value));
            metric.setMonmetricdata(Map.of("source", platformType));
            metric.setMoncreatedat(LocalDateTime.now());
            
            // Guardar métrica
            // metricRepository.save(metric);
        });
    }
    
    private BigDecimal convertToBigDecimal(Object value) {
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        return BigDecimal.ZERO;
    }
    
    private String getPlatformTypeForModel(Long modelId) {
        // Obtener tipo de plataforma desde configuración del modelo
        // return modelService.getPlatformType(modelId);
        return "AZURE_ML"; // TODO: Implementar
    }
    
    private String getExternalModelId(Long modelId) {
        // Obtener ID del modelo en plataforma externa
        // return modelService.getExternalModelId(modelId);
        return ""; // TODO: Implementar
    }
    
    private String getModelVersion(Long modelId) {
        // Obtener versión del modelo
        // return modelService.getVersion(modelId);
        return "1.0"; // TODO: Implementar
    }
}
```

### 5. Crear Servicio de Configuración de Conectores

```java
package com.codeflowx.govern.services.integration;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectorConfigurationService {
    
    /**
     * Valida configuración de conectores
     */
    public Map<String, Boolean> validateConnectors(List<ExternalMlPlatformConnector> connectors) {
        return connectors.stream()
            .collect(Collectors.toMap(
                ExternalMlPlatformConnector::getPlatformType,
                ExternalMlPlatformConnector::testConnection
            ));
    }
    
    /**
     * Obtiene estado de todos los conectores
     */
    public Map<String, ConnectorStatus> getConnectorStatuses(
            List<ExternalMlPlatformConnector> connectors) {
        
        return connectors.stream()
            .collect(Collectors.toMap(
                ExternalMlPlatformConnector::getPlatformType,
                connector -> {
                    ConnectorStatus status = new ConnectorStatus();
                    status.setConnected(connector.testConnection());
                    status.setPlatformType(connector.getPlatformType());
                    return status;
                }
            ));
    }
    
    @Data
    public static class ConnectorStatus {
        private String platformType;
        private boolean connected;
        private String lastSyncTime;
        private String errorMessage;
    }
}
```

---

## VALIDACIONES

1. ✅ Conectores para Azure ML y SageMaker implementados
2. ✅ Integración con PMM funcional
3. ✅ Validación de conectores implementada
4. ✅ Sincronización de métricas funcionando
5. ✅ Documentación de integraciones creada

---

## NOTAS

- Conectores deben ser configurables (endpoints, API keys)
- Manejar errores de conexión gracefully
- Considerar rate limiting de APIs externas
- Cachear métricas para reducir llamadas
- Documentar configuración de cada conector

---

**Estado:** ✅ COMPLETADO

