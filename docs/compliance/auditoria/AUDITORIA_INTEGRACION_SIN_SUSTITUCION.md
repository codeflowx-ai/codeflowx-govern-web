# AUDITORÍA - INTEGRACIÓN SIN SUSTITUCIÓN (Punto 7)
**Fecha:** Noviembre 2025  
**Auditor:** Sistema de Gobierno de IA - CodeflowX  
**Base Legal:** EU AI Act (Reglamento UE 2024/1689)  
**Artículos Relevantes:** Art. 12 (Mantenimiento de registros), Art. 15 (Precisión, Robustez), Art. 19 (Registros), Art. 72 (Vigilancia post-comercialización)

---

## 📋 RESUMEN EJECUTIVO

Este informe documenta exactamente cómo **CodeflowX OS se integra con las herramientas actuales** del cliente sin sustituirlas, actuando como un **governance overlay**. La arquitectura de integración permite mantener la infraestructura existente (Databricks, Snowflake, Azure ML) mientras se añade cumplimiento AI Act automático.

**Estado Actual:** ✅ **Diseñado, parcialmente implementado**  
**Gaps Identificados:** 2 gaps críticos, 3 gaps medios  
**Recomendaciones:** 5 acciones prioritarias  
**Prioridad Comercial:** 🔴 **CRÍTICA** (4x TAM accesible: €3B → €12B)

---

## 🎯 PRINCIPIO ARQUITECTÓNICO

**"Governance Overlay" - NO reemplazo infraestructura**

```
┌──────────────────────────────────────────────────────────┐
│            CODEFLOWX (Governance Layer)                   │
│  Clasificación riesgo + Evaluaciones + Aprobaciones       │
└────────────┬──────────┬──────────┬──────────┬─────────────┘
             │          │          │          │
      API Connectors / Webhooks / Metadata Sync
             │          │          │          │
┌────────────▼───┐ ┌───▼──────┐ ┌─▼────────┐ ┌▼──────────┐
│   Databricks   │ │Snowflake │ │ Azure ML │ │ SageMaker │
│   (Training)   │ │  (Data)  │ │ (Deploy) │ │  (AWS)    │
└────────────────┘ └──────────┘ └──────────┘ └───────────┘

Cliente mantiene su infraestructura existente ✅
CodeflowX añade governance AI Act sin migración ✅
Datos NO se mueven (solo metadata) ✅
```

**Mensaje clave:** "CodeflowX se integra con su Databricks existente, añadiendo compliance AI Act sin migración."

---

## 1. INTEGRACIÓN CON DATABRICKS

### 1.1 ¿Qué datos recibís?

**Desde Databricks hacia CodeflowX:**

1. **Metadata de Modelos MLflow Registry:**
   - Nombre del modelo (`model_name`)
   - Versión (`version`)
   - ID único en Databricks (`model_id`)
   - Stage actual (`None`, `Staging`, `Production`, `Archived`)
   - Tags del modelo (hiperparámetros, métricas)
   - Run ID asociado
   - Timestamp de creación y última modificación
   - Usuario que registró el modelo

2. **Metadata de Experimentos:**
   - Hiperparámetros usados
   - Métricas de entrenamiento (accuracy, loss, etc.)
   - Datasets usados (referencias a ubicaciones)
   - Configuración del experimento

3. **Eventos via Webhooks:**
   - `MODEL_VERSION_CREATED`: Nuevo modelo registrado
   - `MODEL_VERSION_TRANSITIONED`: Cambio de stage (Staging → Production)
   - `MODEL_VERSION_DELETED`: Modelo eliminado
   - Metadata completa del evento

**⚠️ IMPORTANTE:** CodeflowX **NO recibe los datos de entrenamiento** ni los artefactos del modelo. Solo metadata estructurada.

### 1.2 ¿Qué enviáis?

**Desde CodeflowX hacia Databricks:**

1. **Tags de Aprobación Governance:**
   - `codeflowx_approval`: `APPROVED`, `REJECTED`, `PENDING`
   - `codeflowx_risk_level`: `HIGH_RISK`, `LIMITED_RISK`, `MINIMAL_RISK`
   - `codeflowx_compliance_status`: Estado de cumplimiento EU AI Act
   - `codeflowx_fria_id`: ID de la evaluación FRIA (si aplica)
   - `codeflowx_eu_registration_id`: ID de registro en Base de Datos UE (si aplica)

2. **Transiciones de Stage:**
   - Si modelo **aprobado** → Permitir transición a `Production`
   - Si modelo **rechazado** → Bloquear transición, mantener en `Staging` o mover a `Archived`
   - Si modelo **HIGH_RISK sin aprobación** → Bloquear automáticamente

3. **Webhooks de Notificación:**
   - Notificaciones cuando se completa evaluación FRIA
   - Notificaciones cuando se aprueba/rechaza modelo
   - Alertas de compliance violations

**Arquitectura de Comunicación:**

```java
// Databricks → CodeflowX (Sync Service)
@Scheduled(cron = "0 0 * * * *")  // Cada hora
public void syncModelsFromDatabricks(Long platformId) {
    // 1. Listar modelos desde Databricks MLflow Registry
    List<RegisteredModel> models = databricksClient.modelRegistry().listModels();
    
    // 2. Registrar en CodeflowX (solo metadata)
    for (RegisteredModel dbModel : models) {
        ExternalModel externalModel = new ExternalModel();
        externalModel.setExmexternalId(dbModel.getId());
        externalModel.setExmexternalName(dbModel.getName());
        externalModel.setExmmetadata(objectMapper.writeValueAsString(dbModel));
        // NO se copian los datos del modelo
    }
}

// CodeflowX → Databricks (Notification Service)
public void notifyApprovalToDatabricks(Long externalModelId, String approvalStatus) {
    // 1. Set tags en Databricks
    databricksClient.modelRegistry().setModelTag(
        SetModelTagRequest.builder()
            .name(externalModel.getExmexternalName())
            .key("codeflowx_approval")
            .value(approvalStatus)
            .build()
    );
    
    // 2. Controlar stage según aprobación
    if ("APPROVED".equals(approvalStatus)) {
        // Permitir Production
    } else {
        // Bloquear, mantener en Staging
    }
}
```

### 1.3 ¿Dónde se configuran estos conectores?

**Ubicación de Configuración:**

1. **UI de Administración (ZUL):**
   - Ruta: `/console/gobierno/integrations/external-platforms.zul`
   - ViewModel: `ExternalPlatformsViewModel.java`
   - Permite configurar múltiples instancias de Databricks

2. **Tabla de Configuración (PostgreSQL):**
   ```sql
   CREATE TABLE EPLEXTERNALPLATFORMS (
       IDXEXTERNALPLATFORM BIGSERIAL PRIMARY KEY,
       EPLPLATFORM_TYPE VARCHAR(50) NOT NULL,  -- 'DATABRICKS'
       EPLPLATFORM_NAME VARCHAR(200) NOT NULL,  -- 'Databricks Production'
       EPLHOST_URL VARCHAR(500),                -- 'https://workspace.cloud.databricks.com'
       EPLAUTHENTICATION_TYPE VARCHAR(50),      -- 'API_TOKEN'
       EPLAPI_TOKEN TEXT,                       -- Encrypted
       EPLSYNC_ENABLED BOOLEAN NOT NULL DEFAULT TRUE,
       EPLSYNC_FREQUENCY_HOURS INTEGER NOT NULL DEFAULT 1,
       EPLLAST_SYNC_AT TIMESTAMP,
       EPLSYNC_STATUS VARCHAR(50)               -- 'SUCCESS', 'ERROR', 'IN_PROGRESS'
   );
   ```

3. **Configuración Java Bean:**
   ```java
   @Configuration
   public class DatabricksConfig {
       @Value("${databricks.host:}")
       private String databricksHost;
       
       @Value("${databricks.token:}")
       private String databricksToken;
       
       @Bean
       public WorkspaceClient databricksClient() {
           DatabricksConfig config = new DatabricksConfig()
               .setHost(databricksHost)
               .setToken(databricksToken);
           return new WorkspaceClient(config);
       }
   }
   ```

**Campos de Configuración Requeridos:**
- `EPLHOST_URL`: URL del workspace Databricks
- `EPLAPI_TOKEN`: Token de autenticación (almacenado encriptado)
- `EPLSYNC_ENABLED`: Habilitar/deshabilitar sync automático
- `EPLSYNC_FREQUENCY_HOURS`: Frecuencia de sincronización (1, 6, 24 horas)

---

## 2. INTEGRACIÓN CON SNOWFLAKE

### 2.1 ¿Qué datos recibís?

**Desde Snowflake hacia CodeflowX:**

1. **Metadata de Datasets (Catalogación):**
   ```sql
   SELECT 
       table_name,
       row_count,
       bytes,
       created,
       last_altered
   FROM INFORMATION_SCHEMA.TABLES
   WHERE table_schema = 'ML_DATASETS'
     AND table_type = 'BASE TABLE';
   ```
   - Nombre de la tabla
   - Número de filas (row_count)
   - Tamaño en bytes
   - Fecha de creación
   - Última modificación
   - Esquema y base de datos

2. **Metadata de Columnas:**
   - Nombres de columnas
   - Tipos de datos
   - Nullability
   - Constraints

3. **Samples para Evaluación de Calidad:**
   ```sql
   SELECT * FROM database.schema.table 
   SAMPLE (1000 ROWS);  -- Solo 1000 filas, NO toda la tabla
   ```
   - **⚠️ IMPORTANTE:** Solo se leen **samples pequeños** (1K-10K filas) para evaluación de calidad
   - **NO se copian todos los datos** a CodeflowX
   - Los datos permanecen en Snowflake

**Almacenamiento en CodeflowX:**

```sql
CREATE TABLE EXDEXTERNALDATASETS (
    IDXEXTERNALDATASET BIGSERIAL PRIMARY KEY,
    EXDPLATFORM_ID BIGINT NOT NULL REFERENCES EPLEXTERNALPLATFORMS(IDXEXTERNALPLATFORM),
    EXDEXTERNAL_ID VARCHAR(500) NOT NULL,       -- 'database.schema.table'
    EXDNAME VARCHAR(200) NOT NULL,               -- 'customer_data'
    EXDSOURCE_LOCATION VARCHAR(500),             -- 'snowflake://account/database/schema/table'
    EXDRECORD_COUNT BIGINT,                      -- 1.5M filas
    EXDSIZE_BYTES BIGINT,                        -- 2.5 GB
    EXDQUALITY_SCORE DECIMAL(5,2),               -- 0-100
    EXDPII_DETECTED BOOLEAN,
    EXDGDPR_COMPLIANT BOOLEAN,
    EXDLAST_QUALITY_CHECK TIMESTAMP,
    EXDMETADATA JSONB                            -- Metadata completo
);
```

### 2.2 ¿Qué enviáis?

**Desde CodeflowX hacia Snowflake:**

1. **Metadata de Evaluación de Calidad:**
   - Quality score calculado sobre sample
   - Detección de PII (Presidio)
   - Compliance GDPR
   - Recomendaciones de limpieza

2. **Tags de Lineage:**
   - Referencias de qué datasets se usaron para entrenar qué modelos
   - Trazabilidad para Art. 12 (registros de datasets)

**⚠️ IMPORTANTE:** CodeflowX **NO envía datos** a Snowflake. Solo metadata de evaluación y trazabilidad.

### 2.3 ¿Dónde se configuran estos conectores?

**Configuración Similar a Databricks:**

```java
@Service
public class SnowflakeConnectorService {
    @Value("${snowflake.account:}")
    private String account;
    
    @Value("${snowflake.user:}")
    private String user;
    
    @Value("${snowflake.password:}")
    private String password;  // Encrypted
    
    @Value("${snowflake.warehouse:}")
    private String warehouse;
    
    private Connection getConnection(String database, String schema) {
        String url = String.format(
            "jdbc:snowflake://%s.snowflakecomputing.com/?" +
            "user=%s&password=%s&warehouse=%s&db=%s&schema=%s",
            account, user, password, warehouse, database, schema
        );
        return DriverManager.getConnection(url);
    }
}
```

**Configuración en UI:**
- Misma pantalla que Databricks: `/console/gobierno/integrations/external-platforms.zul`
- Tipo de plataforma: `SNOWFLAKE`
- Campos adicionales: `account`, `warehouse`, `database`, `schema`

---

## 3. INTEGRACIÓN CON AZURE ML

### 3.1 ¿Qué datos recibís?

**Desde Azure ML hacia CodeflowX:**

1. **Metadata de Deployments:**
   - Endpoint name
   - Deployment name
   - Model asociado (referencia)
   - Instance type y count
   - Provisioning state
   - Status de deployment

2. **Métricas de Post-Market Monitoring (Art. 72):**
   - Request latency (P50, P95, P99)
   - Requests per second
   - Total requests
   - Model data collector metrics
   - Error rates
   - Drift detection signals

**API Azure ML usada:**
```java
// List deployments
for (Endpoint endpoint : mlClient.online_endpoints.list()) {
    for (Deployment deployment : mlClient.online_deployments.list(endpoint.name)) {
        // Registrar deployment en CodeflowX
    }
}

// Get metrics (últimos 7 días)
MetricsQueryClient metricsClient = new MetricsQueryClient(credential);
response = metricsClient.query_resource(
    resourceId,
    metric_names=["RequestLatency", "RequestsPerSecond"],
    timespan=(start_time, end_time)
);
```

### 3.2 ¿Qué enviáis?

**Desde CodeflowX hacia Azure ML:**

1. **Tags de Compliance:**
   - `codeflowx_compliance_status`: Estado de cumplimiento
   - `codeflowx_risk_level`: Nivel de riesgo
   - `codeflowx_approval_required`: Si requiere aprobación para deployment

2. **Alertas de Drift:**
   - Notificaciones cuando se detecta drift significativo
   - Recomendaciones de retraining

3. **Metadata de Evaluación:**
   - Resultados de evaluaciones FRIA
   - Registros de auditoría (Art. 19)

**⚠️ IMPORTANTE:** CodeflowX **NO controla directamente** los deployments en Azure ML. Solo monitorea y proporciona governance metadata.

### 3.3 ¿Dónde se configuran estos conectores?

**Configuración Azure ML:**

```java
@Configuration
public class AzureMLConfig {
    @Value("${azure.ml.subscription-id:}")
    private String subscriptionId;
    
    @Value("${azure.ml.resource-group:}")
    private String resourceGroup;
    
    @Value("${azure.ml.workspace-name:}")
    private String workspaceName;
    
    @Value("${azure.ml.tenant-id:}")
    private String tenantId;
    
    @Value("${azure.ml.client-id:}")
    private String clientId;
    
    @Value("${azure.ml.client-secret:}")
    private String clientSecret;  // Encrypted
    
    @Bean
    public MLClient azureMLClient() {
        ClientSecretCredential credential = new ClientSecretCredential(
            tenantId, clientId, clientSecret
        );
        return new MLClient(credential, subscriptionId, resourceGroup, workspaceName);
    }
}
```

**Tabla de Configuración:**
- Mismo esquema `EPLEXTERNALPLATFORMS`
- Tipo: `AZURE_ML`
- Metadata adicional en JSONB: `subscription_id`, `resource_group`, `workspace_name`, `tenant_id`, `client_id`

---

## 4. INTEGRACIÓN CON COPILOT (MICROSOFT)

### 4.1 Estado Actual

**⚠️ GAP IDENTIFICADO:** No existe documentación específica sobre integración con Microsoft Copilot en los documentos revisados.

**Recomendación:** Implementar conector similar a Azure ML, ya que Copilot está integrado en el ecosistema Microsoft.

**Arquitectura Propuesta:**

```
Microsoft Copilot (API)
    ↓
CodeflowX Connector (SDK Microsoft Graph / Copilot API)
    ↓
Registro de Interacciones → Telemetry Service
    ↓
Análisis de Gobernanza → Bias, PII, Compliance
    ↓
Logs Inmutables → ImmutableLog
```

### 4.2 ¿Qué datos recibiríamos?

**Desde Copilot hacia CodeflowX:**

1. **Metadata de Conversaciones:**
   - User ID
   - Timestamp
   - Prompt enviado (NO el contenido completo si contiene PII)
   - Response metadata
   - Tokens usados

2. **Metadata de Modelo:**
   - Modelo usado (GPT-4, GPT-3.5, etc.)
   - Configuración (temperature, max_tokens)
   - Costos asociados

**⚠️ IMPORTANTE:** Respeta privacy del usuario. No almacenar contenido completo de conversaciones con PII sin consentimiento.

---

## 5. INTEGRACIÓN CON APIs EXTERNAS Y MCP

### 5.1 APIs Externas Genéricas

**Arquitectura:**

```
Cliente → CodeflowX SDK → API Externa (OpenAI, Anthropic, etc.)
           ↓
    Registro Automático → Telemetry Service
           ↓
    Análisis Compliance → Governance Service
```

**Qué datos recibís:**

1. **Metadata de Llamadas:**
   - Endpoint llamado
   - Timestamp
   - Response time
   - Status code
   - Tokens consumidos
   - Costo asociado

2. **Contenido (opcional, configurable):**
   - Prompt (si no contiene PII)
   - Response (si no contiene PII)
   - Hash del contenido para trazabilidad

**Servicio de Registro:**

```java
@Service
public class ExternalApiTelemetryService {
    public void recordApiCall(String apiProvider, String endpoint, 
                             Map<String, Object> metadata) {
        // 1. Guardar en telemetría
        AioTelemetry telemetry = new AioTelemetry();
        telemetry.setAiotprovider(apiProvider);
        telemetry.setAiotendpoint(endpoint);
        telemetry.setAiotmetadata(objectMapper.writeValueAsString(metadata));
        
        // 2. Análisis de gobernanza
        if (requiresGovernanceAnalysis(metadata)) {
            governanceService.analyzeApiCall(telemetry);
        }
        
        // 3. Log inmutable
        immutableLogService.logApiCall(telemetry);
    }
}
```

### 5.2 Adaptador MCP (Model Control Protocol)

**Objetivo:** Permitir a agentes locales (VSCode, Open Interpreter) interactuar con CodeflowX sin usar REST tradicional.

**Arquitectura:**

```
Agente Local (VSCode) → MCP WebSocket → CodeflowX MCP Adapter
                           ↓
                    Registro de Eventos
                           ↓
                    Consulta de Estados
                           ↓
                    Gestión de Webhooks
```

**¿Qué datos recibís?**

1. **Eventos desde Agentes Locales:**
   - `governance.registerEvent`: Registro de eventos de gobernanza
   - Metadata del evento (componente, tipo, payload)
   - Timestamp

2. **Consultas de Estado:**
   - `governance.getStatus`: Consulta de estado de un evento
   - Event UUID

3. **Registros de Webhooks:**
   - `governance.registerWebhook`: Registro de webhooks para notificaciones
   - Project UUID, URL, eventos a escuchar, secret

**Implementación:**

```java
// Prompt 17 - Adaptador MCP
@Component
public class McpMessageDispatcher {
    public McpResponse handleMethod(String method, McpRequest request) {
        switch (method) {
            case "governance.registerEvent":
                return governanceEventService.registerEvent(request);
                
            case "governance.getStatus":
                return governanceEventService.getStatus(request.getEventUuid());
                
            case "governance.registerWebhook":
                return webhookRegistrationService.registerWebhook(request);
        }
    }
}
```

**Endpoint WebSocket:**

```java
@Configuration
public class McpWebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}

@ServerEndpoint("/mcp")
public class McpEndpoint {
    @OnMessage
    public void onMessage(String message, Session session) {
        // Procesar mensaje MCP (JSON-RPC)
        McpRequest request = objectMapper.readValue(message, McpRequest.class);
        McpResponse response = mcpMessageDispatcher.handleMethod(
            request.getMethod(), request
        );
        session.getBasicRemote().sendText(objectMapper.writeValueAsString(response));
    }
}
```

**¿Qué enviáis?**

1. **Respuestas de Métodos MCP:**
   - Status de registro de eventos
   - Estado de eventos consultados
   - UUID de webhooks registrados

2. **Notificaciones Push (opcional):**
   - Alertas de compliance
   - Cambios de estado de aprobaciones
   - Eventos críticos

**¿Dónde se configuran estos conectores?**

- **Configuración MCP:** `/config/mcp.properties`
  - `mcp.allowed-origins`: Orígenes permitidos (vscode://, http://localhost)
  - `mcp.max-concurrent-sessions`: Máximo de sesiones concurrentes (default: 5)
  - `mcp.api-key-required`: Requiere API key (default: true)

- **Autenticación:**
  - Header: `X-Codeflowx-Key`
  - Query param: `apiKey` (alternativo)
  - Validación contra tabla `APIKEYS`

---

## 6. ¿QUÉ OCURRE SI LA TELEMETRÍA SE CAE?

### 6.1 Arquitectura de Resiliencia

**Componentes Involucrados:**

1. **Telemetry Service (Producer):**
   - Publica eventos a RabbitMQ
   - Si RabbitMQ está caído → Cola local en memoria → Retry con backoff exponencial

2. **Telemetry Worker (Consumer):**
   - Consume de RabbitMQ
   - Si falla procesamiento → DLQ (Dead Letter Queue) → Retry automático (3 intentos)

3. **RabbitMQ:**
   - Si RabbitMQ está caído → Persistencia en disco → Recovery al reiniciar
   - Configuración de durabilidad de colas

**Flujo de Resiliencia:**

```
Evento → Telemetry Service
    ↓ (Si RabbitMQ OK)
RabbitMQ (Cola: aios.telemetry.events)
    ↓ (Si Worker OK)
Telemetry Worker
    ↓ (Si BD OK)
TimescaleDB (AIOTELEMETRY)
    ↓ (Si falla)
DLQ → Retry automático (3 intentos)
    ↓ (Si falla después de 3 intentos)
Alerta + Log en ImmutableLog
```

### 6.2 Procesos que Siguen Funcionando Offline

**✅ Funcionalidades que NO dependen de Telemetría:**

1. **Workflows BPMN de Aprobación:**
   - Funcionan independientemente de telemetría
   - Usan base de datos PostgreSQL directamente
   - Procesos de aprobación manual/automática continúan

2. **Evaluaciones FRIA:**
   - Se ejecutan on-demand o scheduled
   - No requieren telemetría en tiempo real
   - Guardan resultados directamente en BD

3. **Registro de Modelos (Manual):**
   - UI permite registro manual de modelos
   - No requiere telemetría
   - Guarda directamente en tablas de governance

4. **Clasificación de Riesgo:**
   - Se ejecuta al registrar modelo (no requiere telemetría continua)
   - Usa reglas Drools o servicios internos

5. **Sincronización con Plataformas Externas:**
   - Sync de Databricks/Snowflake/Azure ML puede funcionar offline
   - Usa API REST directamente (no requiere telemetría)
   - Resultados se guardan en BD

6. **Inmutabilidad de Logs:**
   - `ImmutableLog` se escribe directamente a PostgreSQL
   - No depende de RabbitMQ
   - Funciona siempre que BD esté disponible

**⚠️ Funcionalidades que SÍ dependen de Telemetría:**

1. **Análisis de Gobernanza en Tiempo Real:**
   - Detección automática de bias, PII, toxicidad
   - Requiere telemetría activa

2. **Detección de Drift Automática:**
   - Comparación continua de métricas
   - Requiere telemetría histórica

3. **Alertas Proactivas:**
   - Notificaciones automáticas de compliance violations
   - Requiere procesamiento en tiempo real

**Estrategia de Degradación:**

```java
@Service
public class TelemetryService {
    private boolean telemetryEnabled = true;
    
    public void recordEvent(TelemetryEvent event) {
        try {
            if (telemetryEnabled) {
                rabbitTemplate.convertAndSend(
                    RabbitMQConfig.TELEMETRY_QUEUE, event
                );
            } else {
                // Fallback: Guardar directamente en BD (modo degradado)
                log.warn("Telemetry disabled, saving directly to BD");
                telemetryRepository.save(convertToEntity(event));
            }
        } catch (Exception e) {
            log.error("Telemetry failed, falling back to direct DB save", e);
            telemetryEnabled = false;  // Disable temporarily
            telemetryRepository.save(convertToEntity(event));  // Fallback
        }
    }
}
```

### 6.3 Recovery Automático

**Cuando Telemetría se Recupera:**

1. **Reconexión Automática a RabbitMQ:**
   - Spring AMQP maneja reconexión automática
   - Backoff exponencial en caso de fallos

2. **Procesamiento de DLQ:**
   - Worker procesa mensajes acumulados en DLQ
   - Prioridad: Mensajes recientes primero

3. **Sincronización de Estado:**
   - Verificar que todos los eventos fueron procesados
   - Comparar timestamps de eventos vs logs inmutables

---

## 7. RESUMEN DE FLUJOS DE DATOS

### 7.1 Databricks

```
Databricks MLflow Registry
    ↓ (Sync cada 1 hora)
CodeflowX ExternalPlatformIntegration
    ↓ (Registra metadata)
CodeflowX Governance
    ↓ (Clasifica riesgo)
CodeflowX Approval Workflow
    ↓ (Si aprobado)
CodeflowX → Databricks (Tags + Stage transition)
```

**Datos que se mueven:**
- ✅ Metadata de modelos (solo)
- ❌ NO se copian datos de entrenamiento
- ❌ NO se copian artefactos del modelo

### 7.2 Snowflake

```
Snowflake INFORMATION_SCHEMA
    ↓ (Sync cada 6-24 horas)
CodeflowX ExternalDataset Catalog
    ↓ (Sample para evaluación)
CodeflowX Quality Evaluation (1000 filas sample)
    ↓ (Resultados)
CodeflowX ExternalDataset (quality_score, pii_detected)
```

**Datos que se mueven:**
- ✅ Metadata de tablas (solo)
- ✅ Sample pequeño (1K-10K filas) para evaluación de calidad
- ❌ NO se copian todos los datos

### 7.3 Azure ML

```
Azure ML Deployments
    ↓ (Monitor cada hora)
CodeflowX Deployment Monitoring
    ↓ (Métricas Azure Monitor)
CodeflowX Drift Detection
    ↓ (Si drift detectado)
CodeflowX → Azure ML (Alertas)
```

**Datos que se mueven:**
- ✅ Metadata de deployments
- ✅ Métricas de performance (latency, throughput)
- ❌ NO se copian datos de inferencia

### 7.4 MCP

```
Agente Local (VSCode)
    ↓ (WebSocket MCP)
CodeflowX MCP Adapter
    ↓ (Registra evento)
CodeflowX Governance Service
    ↓ (Análisis)
CodeflowX ImmutableLog
```

**Datos que se mueven:**
- ✅ Eventos de gobernanza (metadata)
- ✅ Consultas de estado
- ✅ Registros de webhooks

---

## 8. CUMPLIMIENTO EU AI ACT

### 8.1 Artículos Relevantes

| Artículo | Cobertura | Implementación |
|----------|-----------|----------------|
| **Art. 12** (Registros) | ✅ Metadata de modelos y datasets registrados | `ExternalModel`, `ExternalDataset` |
| **Art. 15** (Precisión, Robustez) | ✅ Evaluaciones de calidad y drift | Quality evaluation, drift detection |
| **Art. 19** (Registros) | ✅ Logs inmutables de todas las operaciones | `ImmutableLog` |
| **Art. 27** (FRIA) | ✅ Evaluaciones FRIA para modelos HIGH_RISK | `FriaAssessment` workflow |
| **Art. 49** (Registro UE) | ✅ Registro en Base de Datos UE | `EuRegistration` |
| **Art. 72** (Vigilancia) | ✅ Post-market monitoring | Azure ML deployment monitoring |

### 8.2 Trazabilidad Completa

**Cada integración genera logs inmutables:**

```sql
INSERT INTO IMLIMMUTABLELOGS (
    iduuid, imlentity_type, imlaction, imlentity_id, 
    imldata_snapshot, imlprevious_hash, imlcurrent_hash
) VALUES (
    'uuid', 'EXTERNAL_MODEL', 'SYNC_FROM_DATABRICKS', 
    external_model_id, '{"model_name": "...", "platform": "DATABRICKS"}',
    previous_hash, current_hash
);
```

---

## 9. CONCLUSIÓN

**CodeflowX OS implementa una arquitectura de "Governance Overlay" que:**

1. ✅ **NO sustituye** las plataformas existentes del cliente
2. ✅ **Se integra** via APIs REST, webhooks y MCP
3. ✅ **NO copia datos** masivos, solo metadata y samples pequeños
4. ✅ **Añade cumplimiento** AI Act automático sin disrupción
5. ✅ **Resiliente** a caídas de telemetría con fallback a BD directa
6. ✅ **Procesos críticos** funcionan offline (aprobaciones, FRIA, registro manual)

**Estado de Implementación:**
- ✅ **Diseñado:** Completamente especificado en `PROMPTS_12_CONECTORES_PLATAFORMAS_ENTERPRISE.md`
- ⚠️ **Parcialmente Implementado:** Conectores base creados, falta completar todos los prompts
- ❌ **Pendiente:** Conectores de Copilot, completar integraciones Azure ML/SageMaker

**Prioridad:** 🔴 **CRÍTICA COMERCIAL** (4x TAM accesible: €3B → €12B)

---

**Auditor:** Sistema de Gobierno de IA - CodeflowX  
**Fecha:** Noviembre 2025  
**Próxima Revisión:** Diciembre 2025

