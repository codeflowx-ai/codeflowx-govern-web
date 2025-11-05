# PROMPTS_12: CONECTORES PLATAFORMAS ENTERPRISE
## Integración Databricks, Snowflake, Azure ML, SageMaker, Spark

**Fecha:** 5 Noviembre 2025  
**Propósito:** Integrar CodeflowX como governance overlay sobre infraestructura ML enterprise existente  
**Prioridad:** 🔴 **CRÍTICA COMERCIAL** (80% clientes enterprise usan estas plataformas)  
**Impacto:** 4x TAM accesible (€3B → €12B)

---

## 🎯 VISIÓN ARQUITECTURA

**Principio:** **Governance Overlay** (NO reemplazo infraestructura)

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

**Total prompts:** 12 prompts
- **Databricks:** 3 prompts (conector, sync, webhooks)
- **Snowflake:** 2 prompts (catalogación, quality)
- **Azure ML / SageMaker:** 2 prompts (deployment monitoring)
- **S3 / Azure Blob / GCS:** 2 prompts (catalogación data lakes)
- **Apache Spark:** 1 prompt (evaluation jobs)
- **Framework genérico:** 2 prompts (entity, orchestration)

---

## 📦 GRUPO A: DATABRICKS + MLFLOW

### **PROMPT 1: Conector Databricks + MLflow Registry**

**Objetivo:** Sincronizar modelos registrados en Databricks MLflow Registry con CodeflowX governance

**Arquitectura:**
```
Databricks MLflow Registry
    ↓ API REST (list models, get metadata)
CodeflowX sync service (cada 1 hora)
    ↓ Registra modelos nuevos
CodeflowX governance
    ↓ Clasifica riesgo + evalúa
    ↓ Workflow aprobación
CodeflowX notifica → Databricks
    ↓ Tag approval status
Databricks permite/bloquea deployment
```

**Crear:**

**1. Dependency Maven:**

```xml
<!-- pom.xml -->
<dependency>
    <groupId>com.databricks</groupId>
    <artifactId>databricks-sdk-java</artifactId>
    <version>0.20.0</version>
</dependency>
```

**2. DatabricksConfig:**

```java
// config/DatabricksConfig.java
package com.codeflowx.govern.config.external;

import com.databricks.sdk.WorkspaceClient;
import com.databricks.sdk.core.DatabricksConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabricksConfig {
    
    @Value("${databricks.host:}")
    private String databricksHost;
    
    @Value("${databricks.token:}")
    private String databricksToken;
    
    @Bean
    public WorkspaceClient databricksClient() {
        if (databricksHost == null || databricksHost.isEmpty()) {
            return null;  // Databricks integration disabled
        }
        
        DatabricksConfig config = new DatabricksConfig()
            .setHost(databricksHost)
            .setToken(databricksToken);
        
        return new WorkspaceClient(config);
    }
}
```

**3. Entity ExternalPlatformIntegration:**

```java
// entity/integrations/ExternalPlatformIntegration.java
package com.codeflowx.govern.entity.integrations;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "EPLEXTERNALPLATFORMS")
public class ExternalPlatformIntegration {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXEXTERNALPLATFORM")
    private Long idxexternalplatform;
    
    @Column(name = "IDUUID", nullable = false, length = 36, unique = true)
    private String iduuid;
    
    @Column(name = "EPLPLATFORM_TYPE", nullable = false, length = 50)
    private String eplplatformType;  // DATABRICKS, SNOWFLAKE, AZURE_ML, SAGEMAKER, S3
    
    @Column(name = "EPLPLATFORM_NAME", nullable = false, length = 200)
    private String eplplatformName;
    
    @Column(name = "EPLHOST_URL", length = 500)
    private String eplhostUrl;
    
    @Column(name = "EPLAUTHENTICATION_TYPE", length = 50)
    private String eplauthenticationType;  // API_TOKEN, OAUTH, SERVICE_PRINCIPAL
    
    @Column(name = "EPLAPI_TOKEN", columnDefinition = "TEXT")
    private String eplapiToken;  // Encrypted
    
    @Column(name = "EPLCLIENT_ID", length = 200)
    private String eplclientId;
    
    @Column(name = "EPLCLIENT_SECRET", columnDefinition = "TEXT")
    private String eplclientSecret;  // Encrypted
    
    @Column(name = "EPLTENANTCLIENT_ID", length = 200)
    private String epltenantId;
    
    @Column(name = "EPLSYNC_ENABLED", nullable = false)
    private Boolean eplsyncEnabled;
    
    @Column(name = "EPLSYNC_FREQUENCY_HOURS", nullable = false)
    private Integer eplsyncFrequencyHours;  // 1, 6, 24
    
    @Column(name = "EPLLAST_SYNC_AT")
    private Timestamp epllastSyncAt;
    
    @Column(name = "EPLSYNC_STATUS", length = 50)
    private String eplsyncStatus;  // SUCCESS, ERROR, IN_PROGRESS
    
    @Column(name = "EPLLAST_ERROR", columnDefinition = "TEXT")
    private String epllastError;
    
    @Column(name = "EPLMETADATA", columnDefinition = "JSONB")
    private String eplmetadata;
    
    @Column(name = "EPLCREATED_AT", nullable = false)
    private Timestamp eplcreatedAt;
    
    // Getters/setters...
}
```

**4. Entity ExternalModel:**

```java
// entity/integrations/ExternalModel.java
package com.codeflowx.govern.entity.integrations;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "EXMEXTERNALMODELS")
public class ExternalModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXEXTERNALMODEL")
    private Long idxexternalmodel;
    
    @Column(name = "IDUUID", nullable = false, length = 36, unique = true)
    private String iduuid;
    
    @Column(name = "EXMPLATFORM_ID", nullable = false)
    private Long exmplatformId;  // FK → EPLEXTERNALPLATFORMS
    
    @Column(name = "EXMCODEFLOWX_MODEL_ID")
    private Long exmcodeflowxModelId;  // FK → MODELS (si registrado en CodeflowX)
    
    @Column(name = "EXMEXTERNAL_ID", nullable = false, length = 200)
    private String exmexternalId;  // ID en plataforma externa (Databricks model ID)
    
    @Column(name = "EXMEXTERNAL_NAME", nullable = false, length = 200)
    private String exmexternalName;
    
    @Column(name = "EXMEXTERNAL_VERSION", length = 50)
    private String exmexternalVersion;
    
    @Column(name = "EXMEXTERNAL_URL", length = 500)
    private String exmexternalUrl;
    
    @Column(name = "EXMRISK_LEVEL", columnDefinition = "TEXT[]")
    private String[] exmriskLevel;  // Clasificación CodeflowX
    
    @Column(name = "EXMAPPROVAL_STATUS", length = 50)
    private String exmapprovalStatus;  // PENDING, APPROVED, REJECTED
    
    @Column(name = "EXMAPPROVAL_ID")
    private Long exmapprovalId;  // FK → Approval workflow
    
    @Column(name = "EXMSYNC_STATUS", length = 50)
    private String exmsyncStatus;  // SYNCED, OUT_OF_SYNC, ERROR
    
    @Column(name = "EXMLAST_SYNCED_AT")
    private Timestamp exmlastSyncedAt;
    
    @Column(name = "EXMMETADATA", columnDefinition = "JSONB")
    private String exmmetadata;  // Metadata completo de plataforma externa
    
    @Column(name = "EXMCREATED_AT", nullable = false)
    private Timestamp exmcreatedAt;
    
    // Getters/setters...
}
```

**5. DatabricksConnectorService:**

```java
// service/external/DatabricksConnectorService.java
package com.codeflowx.govern.service.external;

import com.databricks.sdk.WorkspaceClient;
import com.databricks.sdk.service.ml.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class DatabricksConnectorService {
    
    @Autowired
    private WorkspaceClient databricksClient;
    
    @Autowired
    private ExternalModelRepository externalModelRepository;
    
    @Autowired
    private RiskClassificationService riskClassificationService;
    
    /**
     * Sync models FROM Databricks TO CodeflowX
     */
    @Transactional
    public void syncModelsFromDatabricks(Long platformId) {
        try {
            log.info("🔄 Syncing models from Databricks platform: {}", platformId);
            
            // 1. List registered models en Databricks
            List<RegisteredModel> databricksModels = databricksClient.modelRegistry()
                .listModels()
                .iterator()
                .toList();
            
            log.info("Found {} models in Databricks", databricksModels.size());
            
            for (RegisteredModel dbModel : databricksModels) {
                // 2. Check si ya existe en CodeflowX
                ExternalModel existing = externalModelRepository.findByExternalId(
                    dbModel.getId()
                );
                
                if (existing == null) {
                    // 3. Nuevo modelo → Registrar en CodeflowX
                    ExternalModel externalModel = new ExternalModel();
                    externalModel.setIduuid(UUID.randomUUID().toString());
                    externalModel.setExmplatformId(platformId);
                    externalModel.setExmexternalId(dbModel.getId());
                    externalModel.setExmexternalName(dbModel.getName());
                    externalModel.setExmexternalVersion(dbModel.getLatestVersions().get(0).getVersion());
                    externalModel.setExmexternalUrl(dbModel.getPermissionLevel().toString());
                    externalModel.setExmapprovalStatus("PENDING");
                    externalModel.setExmsyncStatus("SYNCED");
                    externalModel.setExmlastSyncedAt(new Timestamp(System.currentTimeMillis()));
                    externalModel.setExmcreatedAt(new Timestamp(System.currentTimeMillis()));
                    
                    // Save
                    externalModelRepository.save(externalModel);
                    
                    log.info("✅ Model registered from Databricks: {}", dbModel.getName());
                    
                    // 4. Trigger clasificación riesgo automática
                    String[] riskLevel = riskClassificationService.classifyExternalModel(
                        externalModel.getIdxexternalmodel()
                    );
                    
                    externalModel.setExmriskLevel(riskLevel);
                    externalModelRepository.save(externalModel);
                    
                    // 5. Si HIGH_RISK → Trigger FRIA workflow
                    if (Arrays.asList(riskLevel).contains("HIGH_RISK")) {
                        startFriaWorkflowForExternalModel(externalModel.getIdxexternalmodel());
                    }
                    
                } else {
                    // Modelo ya existe → Update metadata
                    existing.setExmlastSyncedAt(new Timestamp(System.currentTimeMillis()));
                    existing.setExmsyncStatus("SYNCED");
                    externalModelRepository.save(existing);
                }
            }
            
            log.info("✅ Databricks sync completed: {} models processed", databricksModels.size());
            
        } catch (Exception e) {
            log.error("❌ Error syncing models from Databricks: {}", e.getMessage());
            throw new RuntimeException("Error syncing Databricks models", e);
        }
    }
    
    /**
     * Notify approval status TO Databricks
     */
    public void notifyApprovalToDatabricks(Long externalModelId, String approvalStatus) {
        try {
            ExternalModel externalModel = externalModelRepository.findById(externalModelId)
                .orElseThrow(() -> new RuntimeException("External model not found"));
            
            log.info("📤 Notifying Databricks approval status: {} for model: {}", 
                     approvalStatus, externalModel.getExmexternalName());
            
            // 1. Set tag en Databricks
            databricksClient.modelRegistry().setModelTag(
                SetModelTagRequest.builder()
                    .name(externalModel.getExmexternalName())
                    .key("codeflowx_approval")
                    .value(approvalStatus)
                    .build()
            );
            
            databricksClient.modelRegistry().setModelTag(
                SetModelTagRequest.builder()
                    .name(externalModel.getExmexternalName())
                    .key("codeflowx_risk_level")
                    .value(String.join(",", externalModel.getExmriskLevel()))
                    .build()
            );
            
            // 2. Transition stage según approval
            String targetStage = approvalStatus.equals("APPROVED") 
                ? "Production" 
                : "Archived";  // Bloquear si rechazado
            
            databricksClient.modelRegistry().transitionStage(
                TransitionModelVersionStageRequest.builder()
                    .name(externalModel.getExmexternalName())
                    .version(externalModel.getExmexternalVersion())
                    .stage(Stage.fromValue(targetStage))
                    .archiveExistingVersions(false)
                    .build()
            );
            
            log.info("✅ Databricks notified: stage={}, approval={}", 
                     targetStage, approvalStatus);
            
        } catch (Exception e) {
            log.error("❌ Error notifying Databricks: {}", e.getMessage());
            throw new RuntimeException("Error notifying Databricks", e);
        }
    }
    
    private void startFriaWorkflowForExternalModel(Long externalModelId) {
        // TODO: Integrar con BPMN fria-assessment-workflow
        log.info("🔄 Starting FRIA workflow for external model: {}", externalModelId);
    }
}
```

**6. Migration SQL:**

```sql
-- V1.XX__external_platform_integration.sql

-- Tabla: External platforms configuration
CREATE TABLE IF NOT EXISTS EPLEXTERNALPLATFORMS (
    iduuid UUID UNIQUE,
    IDXEXTERNALPLATFORM BIGSERIAL PRIMARY KEY,
    EPLPLATFORM_TYPE VARCHAR(50) NOT NULL,  -- DATABRICKS, SNOWFLAKE, AZURE_ML, SAGEMAKER, S3
    EPLPLATFORM_NAME VARCHAR(200) NOT NULL,
    EPLHOST_URL VARCHAR(500),
    EPLAUTHENTICATION_TYPE VARCHAR(50),     -- API_TOKEN, OAUTH, SERVICE_PRINCIPAL
    EPLAPI_TOKEN TEXT,                       -- Encrypted
    EPLCLIENT_ID VARCHAR(200),
    EPLCLIENT_SECRET TEXT,                   -- Encrypted
    EPLTENANT_ID VARCHAR(200),
    EPLSYNC_ENABLED BOOLEAN NOT NULL DEFAULT TRUE,
    EPLSYNC_FREQUENCY_HOURS INTEGER NOT NULL DEFAULT 1,
    EPLLAST_SYNC_AT TIMESTAMP,
    EPLSYNC_STATUS VARCHAR(50),              -- SUCCESS, ERROR, IN_PROGRESS
    EPLLAST_ERROR TEXT,
    EPLMETADATA JSONB,
    EPLCREATED_AT TIMESTAMP NOT NULL,
    EPLUPDATED_AT TIMESTAMP
);

-- Tabla: External models synced
CREATE TABLE IF NOT EXISTS EXMEXTERNALMODELS (
    iduuid UUID UNIQUE,
    IDXEXTERNALMODEL BIGSERIAL PRIMARY KEY,
    EXMPLATFORM_ID BIGINT NOT NULL REFERENCES EPLEXTERNALPLATFORMS(IDXEXTERNALPLATFORM),
    EXMCODEFLOWX_MODEL_ID BIGINT REFERENCES MODELS(IDXMODEL),
    EXMEXTERNAL_ID VARCHAR(200) NOT NULL,
    EXMEXTERNAL_NAME VARCHAR(200) NOT NULL,
    EXMEXTERNAL_VERSION VARCHAR(50),
    EXMEXTERNAL_URL VARCHAR(500),
    EXMRISK_LEVEL TEXT[],
    EXMAPPROVAL_STATUS VARCHAR(50),          -- PENDING, APPROVED, REJECTED
    EXMAPPROVAL_ID BIGINT,
    EXMSYNC_STATUS VARCHAR(50),              -- SYNCED, OUT_OF_SYNC, ERROR
    EXMLAST_SYNCED_AT TIMESTAMP,
    EXMMETADATA JSONB,
    EXMCREATED_AT TIMESTAMP NOT NULL,
    EXMUPDATED_AT TIMESTAMP
);

-- Índices
CREATE INDEX idx_external_platforms_type ON EPLEXTERNALPLATFORMS(EPLPLATFORM_TYPE);
CREATE INDEX idx_external_platforms_sync_enabled ON EPLEXTERNALPLATFORMS(EPLSYNC_ENABLED);
CREATE INDEX idx_external_models_platform ON EXMEXTERNALMODELS(EXMPLATFORM_ID);
CREATE INDEX idx_external_models_external_id ON EXMEXTERNALMODELS(EXMEXTERNAL_ID);
CREATE INDEX idx_external_models_approval_status ON EXMEXTERNALMODELS(EXMAPPROVAL_STATUS);
CREATE INDEX idx_external_models_codeflowx ON EXMEXTERNALMODELS(EXMCODEFLOWX_MODEL_ID);
```

**7. Scheduled job sync:**

```java
// scheduled/DatabricksSyncScheduler.java
@Component
public class DatabricksSyncScheduler {
    
    @Autowired
    private DatabricksConnectorService databricksConnectorService;
    
    @Autowired
    private ExternalPlatformIntegrationRepository platformRepository;
    
    /**
     * Sync modelos Databricks cada 1 hora (configurable por platform)
     */
    @Scheduled(cron = "0 0 * * * *")  // Cada hora
    public void syncDatabricksPlatforms() {
        List<ExternalPlatformIntegration> platforms = platformRepository
            .findByPlatformTypeAndSyncEnabled("DATABRICKS", true);
        
        for (ExternalPlatformIntegration platform : platforms) {
            try {
                databricksConnectorService.syncModelsFromDatabricks(
                    platform.getIdxexternalplatform()
                );
            } catch (Exception e) {
                log.error("Error syncing platform {}: {}", platform.getIdxexternalplatform(), e);
            }
        }
    }
}
```

**Verificar:**
- Databricks client bean creado
- Sync models FROM Databricks funciona
- Clasificación riesgo automática
- Notify approval TO Databricks funciona
- Tags en Databricks actualizados
- Stage transition funciona

---

### **PROMPT 2: Webhooks Databricks → CodeflowX**

**Objetivo:** Recibir webhooks de Databricks cuando modelo nuevo, deployment, etc.

**Crear:**

```python
# leka-webhooks-service/main.py (nuevo microservicio FastAPI)
from fastapi import FastAPI, HTTPException, Request, Header
from pydantic import BaseModel
from typing import Dict, Any, Optional
import structlog
import hmac
import hashlib

logger = structlog.get_logger(__name__)

app = FastAPI(title="CodeflowX Webhooks Service", version="1.0.0")

class DatabricksWebhookEvent(BaseModel):
    event_type: str  # MODEL_VERSION_CREATED, MODEL_VERSION_TRANSITIONED, etc.
    model_name: str
    version: str
    run_id: Optional[str] = None
    stage: Optional[str] = None
    timestamp: str
    user_id: Optional[str] = None

class DatabricksWebhookService:
    def __init__(self, codeflowx_api_url, db):
        self.codeflowx_api = codeflowx_api_url
        self.db = db
    
    async def handle_model_version_created(self, event: DatabricksWebhookEvent):
        """Handle nuevo modelo registrado en Databricks"""
        
        logger.info(
            "Databricks model created",
            model_name=event.model_name,
            version=event.version
        )
        
        # 1. Fetch metadata completo de Databricks
        model_metadata = await self.fetch_databricks_model_metadata(
            event.model_name,
            event.version
        )
        
        # 2. Register en CodeflowX
        response = requests.post(
            f"{self.codeflowx_api}/api/external-models/register",
            json={
                "platform_type": "DATABRICKS",
                "external_id": model_metadata["id"],
                "external_name": event.model_name,
                "external_version": event.version,
                "metadata": model_metadata
            }
        )
        
        external_model_id = response.json()["id"]
        
        logger.info(f"Model registered in CodeflowX: {external_model_id}")
        
        # 3. Trigger clasificación riesgo
        await self.trigger_risk_classification(external_model_id)
        
        return {"status": "processed", "external_model_id": external_model_id}
    
    async def handle_model_version_transitioned(self, event: DatabricksWebhookEvent):
        """Handle cambio stage modelo (None → Staging → Production)"""
        
        logger.info(
            "Databricks model transitioned",
            model_name=event.model_name,
            new_stage=event.stage
        )
        
        # Si intenta pasar a Production → Verificar aprobación CodeflowX
        if event.stage == "Production":
            # Check si aprobado en CodeflowX
            external_model = self.db.query(
                "SELECT * FROM EXMEXTERNALMODELS WHERE EXMEXTERNAL_NAME = %s",
                (event.model_name,)
            ).fetchone()
            
            if not external_model:
                logger.warning(f"Model {event.model_name} not registered in CodeflowX")
                return {"status": "model_not_registered"}
            
            if external_model["exmapproval_status"] != "APPROVED":
                logger.error(
                    f"Model {event.model_name} NOT APPROVED in CodeflowX - blocking deployment"
                )
                
                # Revertir stage a Staging (bloquear Production)
                await self.revert_databricks_stage(event.model_name, event.version, "Staging")
                
                return {
                    "status": "blocked",
                    "reason": "Model not approved by CodeflowX governance"
                }
        
        return {"status": "allowed"}

# Endpoints
@app.post("/webhooks/databricks")
async def databricks_webhook(
    request: Request,
    event: DatabricksWebhookEvent,
    x_databricks_signature: Optional[str] = Header(None)
):
    """Webhook receiver Databricks events"""
    
    try:
        # 1. Verify signature (security)
        if x_databricks_signature:
            await verify_databricks_signature(request, x_databricks_signature)
        
        # 2. Route event type
        if event.event_type == "MODEL_VERSION_CREATED":
            result = await webhook_service.handle_model_version_created(event)
        elif event.event_type == "MODEL_VERSION_TRANSITIONED":
            result = await webhook_service.handle_model_version_transitioned(event)
        else:
            logger.warning(f"Unknown event type: {event.event_type}")
            result = {"status": "ignored"}
        
        return result
        
    except Exception as e:
        logger.error(f"Error processing Databricks webhook: {e}")
        raise HTTPException(status_code=500, detail=str(e))

async def verify_databricks_signature(request: Request, signature: str):
    """Verify webhook signature para seguridad"""
    
    body = await request.body()
    secret = os.getenv("DATABRICKS_WEBHOOK_SECRET")
    
    expected_signature = hmac.new(
        secret.encode(),
        body,
        hashlib.sha256
    ).hexdigest()
    
    if not hmac.compare_digest(signature, expected_signature):
        raise HTTPException(status_code=401, detail="Invalid signature")

@app.get("/health")
async def health_check():
    return {"status": "healthy", "service": "webhooks-service"}
```

**Configurar webhook en Databricks:**

```bash
# Databricks CLI (configurar webhook apuntar a CodeflowX)
databricks webhooks create \
  --events MODEL_VERSION_CREATED,MODEL_VERSION_TRANSITIONED \
  --http-url-spec '{"url": "https://codeflowx.cliente.com/webhooks/databricks"}' \
  --description "CodeflowX governance integration"
```

**Verificar:**
- Webhook configurado en Databricks
- CodeflowX recibe eventos
- Modelo nuevo → Registrado automáticamente
- Transition Production → Verificación aprobación
- Bloqueo funciona si no aprobado

---

### **PROMPT 3: UI Gestión Plataformas Externas**

**Objetivo:** Pantalla ZUL para configurar/gestionar conexiones plataformas externas

**Crear:**

**1. ViewModel:**

```java
// viewmodel/integrations/ExternalPlatformsViewModel.java
package com.codeflowx.govern.viewmodel.integrations;

import com.codeflowx.govern.entity.integrations.ExternalPlatformIntegration;
import com.codeflowx.govern.service.external.DatabricksConnectorService;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.util.List;

@VariableResolver(DelegatingVariableResolver.class)
public class ExternalPlatformsViewModel {
    
    @WireVariable
    private BusinessService businessService;
    
    @WireVariable
    private DatabricksConnectorService databricksConnectorService;
    
    @Getter @Setter
    private List<ExternalPlatformIntegration> platforms;
    
    @Getter @Setter
    private ExternalPlatformIntegration selectedPlatform;
    
    @Getter @Setter
    private String platformType;
    
    @Getter @Setter
    private String platformName;
    
    @Getter @Setter
    private String hostUrl;
    
    @Getter @Setter
    private String apiToken;
    
    @Init
    public void init() {
        loadPlatforms();
    }
    
    private void loadPlatforms() {
        platforms = businessService.findAll(ExternalPlatformIntegration.class);
    }
    
    @Command
    @NotifyChange({"platforms", "selectedPlatform"})
    public void addPlatform() {
        ExternalPlatformIntegration platform = new ExternalPlatformIntegration();
        platform.setIduuid(UUID.randomUUID().toString());
        platform.setPlatformType(platformType);
        platform.setPlatformName(platformName);
        platform.setHostUrl(hostUrl);
        platform.setAuthenticationType("API_TOKEN");
        platform.setApiToken(apiToken);  // TODO: Encrypt
        platform.setSyncEnabled(true);
        platform.setSyncFrequencyHours(1);
        platform.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        
        businessService.save(platform);
        
        loadPlatforms();
        
        Clients.showNotification("Platform added successfully", "info", null, null, 2000);
    }
    
    @Command
    @NotifyChange("platforms")
    public void syncNow(@BindingParam("platform") ExternalPlatformIntegration platform) {
        try {
            if ("DATABRICKS".equals(platform.getPlatformType())) {
                databricksConnectorService.syncModelsFromDatabricks(platform.getIdxexternalplatform());
            }
            // TODO: Other platforms
            
            Clients.showNotification("Sync completed", "info", null, null, 2000);
            loadPlatforms();
            
        } catch (Exception e) {
            Clients.showNotification("Sync error: " + e.getMessage(), "error", null, null, 3000);
        }
    }
    
    @Command
    @NotifyChange("platforms")
    public void testConnection(@BindingParam("platform") ExternalPlatformIntegration platform) {
        // Test connection to external platform
        boolean success = databricksConnectorService.testConnection(platform);
        
        String message = success ? "Connection OK" : "Connection Failed";
        String type = success ? "info" : "error";
        
        Clients.showNotification(message, type, null, null, 2000);
    }
}
```

**2. Pantalla ZUL:**

```xml
<!-- src/main/webapp/console/gobierno/integrations/external-platforms.zul -->
<?page title="External Platforms Integration" contentType="text/html;charset=UTF-8"?>
<zk>
    <window title="External Platforms Integration" border="normal" width="100%" 
            apply="org.zkoss.bind.BindComposer"
            viewModel="@id('vm') @init('com.codeflowx.govern.viewmodel.integrations.ExternalPlatformsViewModel')">
        
        <!-- Toolbar -->
        <toolbar>
            <toolbarbutton label="Add Platform" iconSclass="z-icon-plus" 
                          onClick="@command('showAddPlatformDialog')" />
            <toolbarbutton label="Refresh" iconSclass="z-icon-refresh" 
                          onClick="@command('init')" />
        </toolbar>
        
        <!-- Grid plataformas configuradas -->
        <grid model="@load(vm.platforms)" emptyMessage="No external platforms configured">
            <columns>
                <column label="Platform Type" width="150px" />
                <column label="Name" width="200px" />
                <column label="Host URL" />
                <column label="Sync Status" width="120px" />
                <column label="Last Sync" width="180px" />
                <column label="Actions" width="250px" />
            </columns>
            <template name="model" var="platform">
                <row>
                    <label value="@load(platform.eplplatformType)" />
                    <label value="@load(platform.eplplatformName)" />
                    <label value="@load(platform.eplhostUrl)" />
                    <label value="@load(platform.eplsyncStatus)" 
                           style="@load(platform.eplsyncStatus eq 'SUCCESS' ? 'color:green' : 'color:red')" />
                    <label value="@load(platform.epllastSyncAt) @converter('formatedDate', format='yyyy-MM-dd HH:mm')" />
                    <hbox spacing="5px">
                        <button label="Sync Now" iconSclass="z-icon-refresh" 
                               onClick="@command('syncNow', platform=platform)" />
                        <button label="Test" iconSclass="z-icon-check" 
                               onClick="@command('testConnection', platform=platform)" />
                        <button label="Edit" iconSclass="z-icon-edit" 
                               onClick="@command('edit', platform=platform)" />
                        <button label="Delete" iconSclass="z-icon-trash" 
                               onClick="@command('delete', platform=platform)" />
                    </hbox>
                </row>
            </template>
        </grid>
        
        <!-- External models synced -->
        <separator height="20px" />
        <label value="External Models Synced" style="font-weight:bold; font-size:16px" />
        
        <listbox model="@load(vm.externalModels)" emptyMessage="No external models synced">
            <listhead>
                <listheader label="Platform" />
                <listheader label="Model Name" />
                <listheader label="Version" />
                <listheader label="Risk Level" />
                <listheader label="Approval Status" />
                <listheader label="Last Sync" />
                <listheader label="Actions" />
            </listhead>
            <template name="model" var="model">
                <listitem>
                    <listcell label="@load(model.platform.eplplatformName)" />
                    <listcell label="@load(model.exmexternalName)" />
                    <listcell label="@load(model.exmexternalVersion)" />
                    <listcell>
                        <label value="@load(model.exmriskLevel)" 
                               style="@load(model.exmriskLevel contains 'HIGH_RISK' ? 'color:red; font-weight:bold' : '')" />
                    </listcell>
                    <listcell>
                        <label value="@load(model.exmapprovalStatus)" 
                               style="@load(model.exmapprovalStatus eq 'APPROVED' ? 'color:green' : (model.exmapprovalStatus eq 'REJECTED' ? 'color:red' : 'color:orange'))" />
                    </listcell>
                    <listcell label="@load(model.exmlastSyncedAt) @converter('formatedDate', format='yyyy-MM-dd HH:mm')" />
                    <listcell>
                        <button label="View Details" onClick="@command('viewModelDetails', model=model)" />
                    </listcell>
                </listitem>
            </template>
        </listbox>
    </window>
</zk>
```

**Verificar:**
- UI pantalla funciona
- Add platform OK
- Sync now button trigger sync
- Test connection funciona
- Lista external models muestra datos

---

## 📦 GRUPO B: SNOWFLAKE (DATA CATALOG)

### **PROMPT 4: Conector Snowflake Catalogación Datasets**

**Objetivo:** Catalogar datasets en Snowflake SIN copiar datos (solo metadata)

**Crear:**

```java
// service/external/SnowflakeConnectorService.java
package com.codeflowx.govern.service.external;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

@Slf4j
@Service
public class SnowflakeConnectorService {
    
    @Value("${snowflake.account:}")
    private String account;
    
    @Value("${snowflake.user:}")
    private String user;
    
    @Value("${snowflake.password:}")
    private String password;
    
    @Value("${snowflake.warehouse:}")
    private String warehouse;
    
    private Connection getConnection(String database, String schema) throws SQLException {
        String url = String.format(
            "jdbc:snowflake://%s.snowflakecomputing.com/?" +
            "user=%s&password=%s&warehouse=%s&db=%s&schema=%s",
            account, user, password, warehouse, database, schema
        );
        
        return DriverManager.getConnection(url);
    }
    
    /**
     * Catalog datasets Snowflake (metadata only)
     */
    @Transactional
    public List<ExternalDataset> catalogDatasetsFromSnowflake(
        String database,
        String schema,
        Long platformId
    ) {
        List<ExternalDataset> datasets = new ArrayList<>();
        
        try (Connection conn = getConnection(database, schema)) {
            log.info("📊 Cataloging datasets from Snowflake: {}.{}", database, schema);
            
            // Query INFORMATION_SCHEMA (metadata only - NO copia datos)
            String sql = """
                SELECT 
                    table_name,
                    row_count,
                    bytes,
                    created,
                    last_altered
                FROM INFORMATION_SCHEMA.TABLES
                WHERE table_schema = ?
                  AND table_type = 'BASE TABLE'
            """;
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, schema);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String tableName = rs.getString("table_name");
                long rowCount = rs.getLong("row_count");
                long sizeBytes = rs.getLong("bytes");
                Timestamp created = rs.getTimestamp("created");
                
                // Register dataset en CodeflowX (metadata only)
                ExternalDataset dataset = new ExternalDataset();
                dataset.setIduuid(UUID.randomUUID().toString());
                dataset.setExdplatformId(platformId);
                dataset.setExdexternalId(String.format("%s.%s.%s", database, schema, tableName));
                dataset.setExdname(tableName);
                dataset.setExdsourceLocation(String.format("snowflake://%s/%s/%s/%s", 
                                                           account, database, schema, tableName));
                dataset.setExdrecordCount(rowCount);
                dataset.setExdsizeBytes(sizeBytes);
                dataset.setExdcreatedAt(created);
                
                businessService.save(dataset);
                datasets.add(dataset);
                
                log.info("✅ Dataset cataloged: {} ({} rows, {} MB)", 
                         tableName, rowCount, sizeBytes / 1024 / 1024);
            }
            
            log.info("✅ Cataloged {} datasets from Snowflake", datasets.size());
            
        } catch (Exception e) {
            log.error("❌ Error cataloging Snowflake datasets: {}", e.getMessage());
            throw new RuntimeException("Error cataloging Snowflake", e);
        }
        
        return datasets;
    }
    
    /**
     * Evaluate data quality (sample only - NO full scan)
     */
    public Map<String, Object> evaluateDataQuality(
        String database,
        String schema,
        String tableName,
        int sampleSize
    ) {
        try (Connection conn = getConnection(database, schema)) {
            log.info("📊 Evaluating data quality for {}.{}.{} (sample {})", 
                     database, schema, tableName, sampleSize);
            
            // SAMPLE small subset (NO copia toda la tabla)
            String sql = String.format(
                "SELECT * FROM %s.%s.%s SAMPLE (%d ROWS)",
                database, schema, tableName, sampleSize
            );
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData metadata = rs.getMetaData();
            
            // Analyze sample
            int columnCount = metadata.getColumnCount();
            int rowCount = 0;
            int nullCount = 0;
            Set<String> uniqueValues = new HashSet<>();
            
            while (rs.next()) {
                rowCount++;
                for (int i = 1; i <= columnCount; i++) {
                    Object value = rs.getObject(i);
                    if (value == null) {
                        nullCount++;
                    } else {
                        uniqueValues.add(value.toString());
                    }
                }
            }
            
            // Calculate metrics (sobre sample, no full table)
            double completeness = (1.0 - (double) nullCount / (rowCount * columnCount)) * 100;
            
            Map<String, Object> qualityMetrics = new HashMap<>();
            qualityMetrics.put("sample_size", rowCount);
            qualityMetrics.put("completeness_pct", completeness);
            qualityMetrics.put("column_count", columnCount);
            qualityMetrics.put("null_values", nullCount);
            qualityMetrics.put("unique_values", uniqueValues.size());
            
            log.info("✅ Quality evaluated: completeness={:.2f}%", completeness);
            
            return qualityMetrics;
            
        } catch (Exception e) {
            log.error("❌ Error evaluating Snowflake data quality: {}", e.getMessage());
            throw new RuntimeException("Error evaluating quality", e);
        }
    }
}
```

**Entity ExternalDataset:**

```java
// entity/integrations/ExternalDataset.java
@Entity
@Table(name = "EXDEXTERNALDATASETS")
public class ExternalDataset {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXEXTERNALDATASET")
    private Long idxexternaldataset;
    
    @Column(name = "IDUUID", nullable = false, unique = true)
    private String iduuid;
    
    @Column(name = "EXDPLATFORM_ID", nullable = false)
    private Long exdplatformId;  // FK → EPLEXTERNALPLATFORMS
    
    @Column(name = "EXDEXTERNAL_ID", nullable = false, length = 500)
    private String exdexternalId;  // database.schema.table en Snowflake
    
    @Column(name = "EXDNAME", nullable = false, length = 200)
    private String exdname;
    
    @Column(name = "EXDSOURCE_LOCATION", length = 500)
    private String exdsourceLocation;  // snowflake://account/database/schema/table
    
    @Column(name = "EXDRECORD_COUNT")
    private Long exdrecordCount;
    
    @Column(name = "EXDSIZE_BYTES")
    private Long exdsizeBytes;
    
    @Column(name = "EXDQUALITY_SCORE")
    private Double exdqualityScore;  // 0-100
    
    @Column(name = "EXDPII_DETECTED")
    private Boolean exdpiiDetected;
    
    @Column(name = "EXDGDPR_COMPLIANT")
    private Boolean exdgdprCompliant;
    
    @Column(name = "EXDLAST_QUALITY_CHECK")
    private Timestamp exdlastQualityCheck;
    
    @Column(name = "EXDMETADATA", columnDefinition = "JSONB")
    private String exdmetadata;
    
    @Column(name = "EXDCREATED_AT", nullable = false)
    private Timestamp exdcreatedAt;
    
    // Getters/setters...
}
```

**Migration SQL:**

```sql
-- Tabla: External datasets
CREATE TABLE IF NOT EXISTS EXDEXTERNALDATASETS (
    iduuid UUID UNIQUE,
    IDXEXTERNALDATASET BIGSERIAL PRIMARY KEY,
    EXDPLATFORM_ID BIGINT NOT NULL REFERENCES EPLEXTERNALPLATFORMS(IDXEXTERNALPLATFORM),
    EXDEXTERNAL_ID VARCHAR(500) NOT NULL,
    EXDNAME VARCHAR(200) NOT NULL,
    EXDSOURCE_LOCATION VARCHAR(500),
    EXDRECORD_COUNT BIGINT,
    EXDSIZE_BYTES BIGINT,
    EXDQUALITY_SCORE DECIMAL(5,2),           -- 0-100
    EXDPII_DETECTED BOOLEAN,
    EXDGDPR_COMPLIANT BOOLEAN,
    EXDLAST_QUALITY_CHECK TIMESTAMP,
    EXDMETADATA JSONB,
    EXDCREATED_AT TIMESTAMP NOT NULL,
    EXDUPDATED_AT TIMESTAMP
);

CREATE INDEX idx_external_datasets_platform ON EXDEXTERNALDATASETS(EXDPLATFORM_ID);
CREATE INDEX idx_external_datasets_external_id ON EXDEXTERNALDATASETS(EXDEXTERNAL_ID);
CREATE INDEX idx_external_datasets_quality ON EXDEXTERNALDATASETS(EXDQUALITY_SCORE);
```

**Verificar:**
- Conexión Snowflake OK (JDBC)
- Catalogación datasets funciona
- Metadata extraído (filas, size, created)
- Quality evaluation sobre sample (NO full scan)
- Datasets guardados en CodeflowX

---

### **PROMPT 5: Microservicio Python Data Quality Snowflake**

**Objetivo:** Evaluar calidad datos Snowflake usando samples (PII, bias, quality) sin copiar datos

**Crear:**

```python
# leka-data-quality-service/connectors/snowflake_connector.py
import snowflake.connector
from typing import Dict, Any, List
import pandas as pd
from presidio_analyzer import AnalyzerEngine
import structlog

logger = structlog.get_logger(__name__)

class SnowflakeDataQualityService:
    """Evaluate data quality en Snowflake usando samples (NO copia datos)"""
    
    def __init__(self, account, user, password, warehouse):
        self.conn_params = {
            'account': account,
            'user': user,
            'password': password,
            'warehouse': warehouse
        }
        self.pii_analyzer = AnalyzerEngine()
    
    def evaluate_dataset_quality(
        self,
        database: str,
        schema: str,
        table: str,
        sample_size: int = 1000
    ) -> Dict[str, Any]:
        """
        Evaluate quality usando sample (NO full scan)
        
        Returns:
            Dict con métricas calidad
        """
        
        conn = snowflake.connector.connect(**self.conn_params)
        cursor = conn.cursor()
        
        try:
            # 1. Get table metadata
            cursor.execute(f"""
                SELECT column_name, data_type
                FROM {database}.INFORMATION_SCHEMA.COLUMNS
                WHERE table_schema = '{schema}'
                  AND table_name = '{table}'
            """)
            
            columns_info = cursor.fetchall()
            column_names = [c[0] for c in columns_info]
            
            logger.info(f"Evaluating {database}.{schema}.{table} ({len(column_names)} columns)")
            
            # 2. Sample data (NO copia toda tabla)
            cursor.execute(f"""
                SELECT * FROM {database}.{schema}.{table}
                SAMPLE ({sample_size} ROWS)
            """)
            
            sample_data = cursor.fetchall()
            df = pd.DataFrame(sample_data, columns=column_names)
            
            logger.info(f"Sample loaded: {len(df)} rows")
            
            # 3. Calculate quality metrics
            completeness = (1 - df.isnull().sum().sum() / (df.shape[0] * df.shape[1])) * 100
            
            # 4. Detect PII (Presidio)
            pii_detected = False
            pii_columns = []
            
            for col in df.select_dtypes(include=['object']).columns:
                sample_text = " ".join(df[col].dropna().astype(str).head(100).tolist())
                pii_results = self.pii_analyzer.analyze(sample_text, language='es')
                
                if pii_results:
                    pii_detected = True
                    pii_columns.append({
                        "column": col,
                        "pii_types": [r.entity_type for r in pii_results]
                    })
            
            # 5. Detect duplicates
            duplicates = df.duplicated().sum()
            
            # 6. Basic statistics
            numeric_cols = df.select_dtypes(include=['number']).columns
            stats = {}
            if len(numeric_cols) > 0:
                stats = df[numeric_cols].describe().to_dict()
            
            result = {
                "database": database,
                "schema": schema,
                "table": table,
                "sample_size": len(df),
                "total_columns": len(column_names),
                "completeness_pct": round(completeness, 2),
                "pii_detected": pii_detected,
                "pii_columns": pii_columns,
                "duplicates_count": int(duplicates),
                "duplicates_pct": round((duplicates / len(df)) * 100, 2),
                "statistics": stats,
                "column_types": {col: str(dtype) for col, dtype in df.dtypes.items()}
            }
            
            logger.info(f"✅ Quality evaluated: completeness={completeness:.2f}%, PII={pii_detected}")
            
            return result
            
        except Exception as e:
            logger.error(f"Error evaluating Snowflake quality: {e}")
            raise
        finally:
            cursor.close()
            conn.close()

# Endpoint FastAPI
@app.post("/api/data-quality/evaluate-snowflake")
async def evaluate_snowflake_dataset(
    database: str,
    schema: str,
    table: str,
    sample_size: int = 1000
):
    """Evaluate data quality Snowflake dataset"""
    
    try:
        result = snowflake_quality_service.evaluate_dataset_quality(
            database, schema, table, sample_size
        )
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
```

**Verificar:**
- Conexión Snowflake OK
- Sample query funciona (solo 1K filas, NO toda tabla)
- Quality metrics calculados
- PII detection funciona
- Resultados guardados CodeflowX

---

## 📦 GRUPO C: AZURE ML / SAGEMAKER (DEPLOYMENT)

### **PROMPT 6: Conector Azure ML Deployment Monitoring**

**Objetivo:** Monitorear modelos deployados en Azure ML para post-market monitoring (Art. 72)

**Crear:**

```python
# leka-deployment-monitoring-service/connectors/azure_ml_connector.py
from azure.ai.ml import MLClient
from azure.identity import DefaultAzureCredential, ClientSecretCredential
from typing import Dict, Any, List
import structlog

logger = structlog.get_logger(__name__)

class AzureMLConnectorService:
    """Monitor deployments Azure ML para Art. 72 post-market monitoring"""
    
    def __init__(self, subscription_id, resource_group, workspace_name, tenant_id, client_id, client_secret):
        credential = ClientSecretCredential(
            tenant_id=tenant_id,
            client_id=client_id,
            client_secret=client_secret
        )
        
        self.ml_client = MLClient(
            credential=credential,
            subscription_id=subscription_id,
            resource_group_name=resource_group,
            workspace_name=workspace_name
        )
    
    def list_deployed_models(self) -> List[Dict[str, Any]]:
        """List modelos deployados en Azure ML"""
        
        deployments = []
        
        for endpoint in self.ml_client.online_endpoints.list():
            for deployment in self.ml_client.online_deployments.list(endpoint.name):
                deployments.append({
                    "endpoint_name": endpoint.name,
                    "deployment_name": deployment.name,
                    "model": deployment.model,
                    "instance_type": deployment.instance_type,
                    "instance_count": deployment.instance_count,
                    "status": deployment.provisioning_state
                })
        
        logger.info(f"Found {len(deployments)} deployments in Azure ML")
        return deployments
    
    def get_deployment_metrics(
        self,
        endpoint_name: str,
        deployment_name: str,
        days: int = 7
    ) -> Dict[str, Any]:
        """Get métricas deployment para drift detection"""
        
        # Query Azure Monitor metrics
        from azure.monitor.query import MetricsQueryClient
        from datetime import datetime, timedelta
        
        metrics_client = MetricsQueryClient(self.ml_client.credential)
        
        # Resource ID
        resource_id = f"/subscriptions/{self.ml_client.subscription_id}/" \
                     f"resourceGroups/{self.ml_client.resource_group_name}/" \
                     f"providers/Microsoft.MachineLearningServices/" \
                     f"workspaces/{self.ml_client.workspace_name}/" \
                     f"onlineEndpoints/{endpoint_name}/deployments/{deployment_name}"
        
        # Query métricas (últimos 7 días)
        end_time = datetime.now()
        start_time = end_time - timedelta(days=days)
        
        response = metrics_client.query_resource(
            resource_id,
            metric_names=["RequestLatency", "RequestsPerSecond", "ModelDataCollector"],
            timespan=(start_time, end_time),
            granularity=timedelta(hours=1)
        )
        
        metrics = {
            "endpoint_name": endpoint_name,
            "deployment_name": deployment_name,
            "period_days": days,
            "latency_p95": None,
            "requests_per_second_avg": None,
            "total_requests": None
        }
        
        for metric in response.metrics:
            if metric.name == "RequestLatency":
                # Calculate P95 from timeseries
                values = [ts.average for ts in metric.timeseries[0].data if ts.average]
                if values:
                    metrics["latency_p95"] = sorted(values)[int(len(values) * 0.95)]
            elif metric.name == "RequestsPerSecond":
                values = [ts.average for ts in metric.timeseries[0].data if ts.average]
                if values:
                    metrics["requests_per_second_avg"] = sum(values) / len(values)
        
        logger.info(f"Metrics retrieved for {deployment_name}")
        return metrics
    
    def detect_drift_azure_ml(
        self,
        endpoint_name: str,
        deployment_name: str,
        baseline_metrics: Dict[str, Any]
    ) -> Dict[str, Any]:
        """Detect drift comparando métricas actuales vs baseline"""
        
        # Get current metrics
        current_metrics = self.get_deployment_metrics(endpoint_name, deployment_name)
        
        # Compare
        drift_detected = False
        drift_details = {}
        
        if baseline_metrics.get("latency_p95") and current_metrics.get("latency_p95"):
            latency_increase_pct = (
                (current_metrics["latency_p95"] - baseline_metrics["latency_p95"]) 
                / baseline_metrics["latency_p95"] 
                * 100
            )
            
            if latency_increase_pct > 30:  # >30% increase = drift
                drift_detected = True
                drift_details["latency_degradation"] = {
                    "baseline": baseline_metrics["latency_p95"],
                    "current": current_metrics["latency_p95"],
                    "increase_pct": latency_increase_pct
                }
        
        return {
            "drift_detected": drift_detected,
            "drift_details": drift_details,
            "recommendation": "RETRAIN_REQUIRED" if drift_detected else "OK"
        }

# Endpoint FastAPI
@app.post("/api/deployment/monitor-azure-ml")
async def monitor_azure_ml_deployment(
    endpoint_name: str,
    deployment_name: str
):
    """Monitor deployment Azure ML para Art. 72"""
    
    try:
        metrics = azure_ml_connector.get_deployment_metrics(
            endpoint_name,
            deployment_name
        )
        return metrics
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
```

**Verificar:**
- Conexión Azure ML OK
- List deployments funciona
- Get metrics funciona
- Drift detection funciona
- Integración con CodeflowX drift monitoring

---

### **PROMPT 7: Conector SageMaker (AWS)**

**Objetivo:** Similar Azure ML pero para AWS SageMaker

**Crear:**

```python
# leka-deployment-monitoring-service/connectors/sagemaker_connector.py
import boto3
from typing import Dict, Any, List
import structlog

logger = structlog.get_logger(__name__)

class SageMakerConnectorService:
    """Monitor deployments SageMaker para Art. 72"""
    
    def __init__(self, aws_access_key, aws_secret_key, region):
        self.sagemaker = boto3.client(
            'sagemaker',
            aws_access_key_id=aws_access_key,
            aws_secret_access_key=aws_secret_key,
            region_name=region
        )
        
        self.cloudwatch = boto3.client(
            'cloudwatch',
            aws_access_key_id=aws_access_key,
            aws_secret_access_key=aws_secret_key,
            region_name=region
        )
    
    def list_endpoints(self) -> List[Dict[str, Any]]:
        """List endpoints SageMaker"""
        
        response = self.sagemaker.list_endpoints()
        
        endpoints = []
        for endpoint_summary in response['Endpoints']:
            endpoint_name = endpoint_summary['EndpointName']
            
            # Get endpoint details
            endpoint_details = self.sagemaker.describe_endpoint(
                EndpointName=endpoint_name
            )
            
            endpoints.append({
                "endpoint_name": endpoint_name,
                "endpoint_arn": endpoint_details['EndpointArn'],
                "status": endpoint_details['EndpointStatus'],
                "creation_time": endpoint_details['CreationTime'].isoformat(),
                "last_modified": endpoint_details['LastModifiedTime'].isoformat()
            })
        
        logger.info(f"Found {len(endpoints)} SageMaker endpoints")
        return endpoints
    
    def get_endpoint_metrics(
        self,
        endpoint_name: str,
        days: int = 7
    ) -> Dict[str, Any]:
        """Get CloudWatch metrics para endpoint"""
        
        from datetime import datetime, timedelta
        
        end_time = datetime.now()
        start_time = end_time - timedelta(days=days)
        
        # Query CloudWatch metrics
        metrics = {}
        
        # Latency metric
        latency_response = self.cloudwatch.get_metric_statistics(
            Namespace='AWS/SageMaker',
            MetricName='ModelLatency',
            Dimensions=[
                {'Name': 'EndpointName', 'Value': endpoint_name},
                {'Name': 'VariantName', 'Value': 'AllTraffic'}
            ],
            StartTime=start_time,
            EndTime=end_time,
            Period=3600,  # 1 hora
            Statistics=['Average', 'Maximum']
        )
        
        if latency_response['Datapoints']:
            datapoints = sorted(latency_response['Datapoints'], key=lambda x: x['Timestamp'])
            metrics['avg_latency_ms'] = sum(d['Average'] for d in datapoints) / len(datapoints)
            metrics['max_latency_ms'] = max(d['Maximum'] for d in datapoints)
        
        # Invocation metric
        invocation_response = self.cloudwatch.get_metric_statistics(
            Namespace='AWS/SageMaker',
            MetricName='Invocations',
            Dimensions=[
                {'Name': 'EndpointName', 'Value': endpoint_name}
            ],
            StartTime=start_time,
            EndTime=end_time,
            Period=3600,
            Statistics=['Sum']
        )
        
        if invocation_response['Datapoints']:
            metrics['total_invocations'] = sum(d['Sum'] for d in invocation_response['Datapoints'])
        
        logger.info(f"Metrics retrieved for endpoint {endpoint_name}")
        return metrics

# Endpoint FastAPI
@app.post("/api/deployment/monitor-sagemaker")
async def monitor_sagemaker_endpoint(endpoint_name: str):
    """Monitor endpoint SageMaker para Art. 72"""
    
    try:
        metrics = sagemaker_connector.get_endpoint_metrics(endpoint_name)
        return metrics
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
```

**Verificar:**
- Conexión SageMaker OK (boto3)
- List endpoints funciona
- CloudWatch metrics funciona
- Integración drift detection

---

## 📦 GRUPO D: DATA LAKES (S3, AZURE BLOB, GCS)

### **PROMPT 8: Conector S3 / Azure Blob / GCS Catalogación**

**Objetivo:** Catalogar datasets en data lakes SIN copiar (solo metadata: ubicación, tamaño, formato)

**Crear:**

```python
# leka-data-catalog-service/connectors/data_lake_connector.py
import boto3
from azure.storage.blob import BlobServiceClient
from google.cloud import storage
from typing import Dict, Any, List
import structlog

logger = structlog.get_logger(__name__)

class S3CatalogService:
    """Catalog datasets S3 (metadata only)"""
    
    def __init__(self, aws_access_key, aws_secret_key, region):
        self.s3 = boto3.client(
            's3',
            aws_access_key_id=aws_access_key,
            aws_secret_access_key=aws_secret_key,
            region_name=region
        )
    
    def catalog_bucket(
        self,
        bucket_name: str,
        prefix: str = "",
        file_extensions: List[str] = ['.parquet', '.csv', '.json']
    ) -> List[Dict[str, Any]]:
        """Catalog datasets en bucket S3"""
        
        datasets = []
        
        # List objects (metadata)
        paginator = self.s3.get_paginator('list_objects_v2')
        pages = paginator.paginate(Bucket=bucket_name, Prefix=prefix)
        
        for page in pages:
            for obj in page.get('Contents', []):
                key = obj['Key']
                
                # Filter por extensión
                if any(key.endswith(ext) for ext in file_extensions):
                    datasets.append({
                        "external_id": f"s3://{bucket_name}/{key}",
                        "name": key.split('/')[-1],
                        "source_location": f"s3://{bucket_name}/{key}",
                        "size_bytes": obj['Size'],
                        "last_modified": obj['LastModified'].isoformat(),
                        "format": key.split('.')[-1].upper(),
                        "bucket": bucket_name,
                        "key": key
                    })
        
        logger.info(f"Cataloged {len(datasets)} datasets from S3 bucket {bucket_name}")
        return datasets

class AzureBlobCatalogService:
    """Similar para Azure Blob Storage"""
    
    def __init__(self, connection_string):
        self.blob_service = BlobServiceClient.from_connection_string(connection_string)
    
    def catalog_container(
        self,
        container_name: str,
        prefix: str = ""
    ) -> List[Dict[str, Any]]:
        """Catalog datasets en Azure Blob container"""
        
        container_client = self.blob_service.get_container_client(container_name)
        
        datasets = []
        for blob in container_client.list_blobs(name_starts_with=prefix):
            datasets.append({
                "external_id": f"https://{self.blob_service.account_name}.blob.core.windows.net/{container_name}/{blob.name}",
                "name": blob.name.split('/')[-1],
                "source_location": f"azure://{container_name}/{blob.name}",
                "size_bytes": blob.size,
                "last_modified": blob.last_modified.isoformat(),
                "format": blob.name.split('.')[-1].upper()
            })
        
        logger.info(f"Cataloged {len(datasets)} datasets from Azure Blob {container_name}")
        return datasets

# Endpoint
@app.post("/api/data-catalog/catalog-s3")
async def catalog_s3_bucket(
    bucket_name: str,
    prefix: str = "",
    platform_id: int = None
):
    """Catalog S3 bucket datasets"""
    
    try:
        datasets = s3_catalog.catalog_bucket(bucket_name, prefix)
        
        # Register en CodeflowX
        for dataset in datasets:
            register_external_dataset(platform_id, dataset)
        
        return {"datasets_cataloged": len(datasets), "datasets": datasets}
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
```

**Verificar:**
- S3 list objects funciona
- Azure Blob list blobs funciona
- Catalogación NO descarga archivos (solo metadata)
- Datasets registrados CodeflowX

---

## 📦 GRUPO E: APACHE SPARK (EVALUATION JOBS)

### **PROMPT 9: Conector Spark Submit Evaluation Jobs**

**Objetivo:** Ejecutar evaluaciones big data (bias, quality) en Spark cluster cliente SIN mover datos

**Crear:**

```python
# leka-spark-evaluations-service/spark_job_service.py
from pylivy import LivyClient
from typing import Dict, Any
import structlog
import time

logger = structlog.get_logger(__name__)

class SparkEvaluationService:
    """Submit evaluation jobs a Spark cluster cliente (sin mover datos)"""
    
    def __init__(self, livy_url):
        self.livy = LivyClient(livy_url)
    
    def submit_bias_evaluation_job(
        self,
        dataset_path: str,  # s3://bucket/path o hdfs://path
        protected_attributes: List[str],
        output_path: str
    ) -> str:
        """
        Submit Spark job para evaluar sesgos en dataset masivo
        
        Job ejecuta en cluster cliente (datos NO salen)
        """
        
        # PySpark script (ejecuta en cluster cliente)
        pyspark_code = f"""
from pyspark.sql import SparkSession
from pyspark.sql import functions as F
import json

spark = SparkSession.builder.appName("CodeflowX Bias Evaluation").getOrCreate()

# 1. Load dataset (puede ser TB - Spark lo maneja)
df = spark.read.parquet("{dataset_path}")

print(f"Dataset loaded: {{df.count()}} rows, {{len(df.columns)}} columns")

# 2. Evaluate bias por protected attribute
bias_results = {{}}

protected_attrs = {protected_attributes}

for attr in protected_attrs:
    # Demographic parity
    positive_rate_by_group = df.groupBy(attr) \\
        .agg(F.avg("prediction").alias("positive_rate")) \\
        .collect()
    
    groups = {{row[attr]: row["positive_rate"] for row in positive_rate_by_group}}
    
    # Calculate disparity
    if len(groups) > 1:
        max_rate = max(groups.values())
        min_rate = min(groups.values())
        disparity = (max_rate - min_rate) / max_rate if max_rate > 0 else 0
    else:
        disparity = 0
    
    bias_results[attr] = {{
        "groups": groups,
        "disparity": disparity,
        "bias_detected": disparity > 0.1  # >10% disparity = bias
    }}

# 3. Write results (solo métricas, no datos)
output_df = spark.createDataFrame([bias_results])
output_df.write.mode("overwrite").json("{output_path}")

print(f"Bias evaluation completed. Results: {output_path}")
"""
        
        # Submit job via Livy
        session = self.livy.create_session(kind='pyspark')
        session.wait()  # Wait session ready
        
        # Execute PySpark code
        statement = session.run(pyspark_code)
        statement.wait()  # Wait completion
        
        logger.info(f"Spark job completed: {statement.output}")
        
        # Read results from output_path
        results = self._read_results_from_storage(output_path)
        
        return results
    
    def _read_results_from_storage(self, output_path: str) -> Dict[str, Any]:
        """Read results JSON from S3/HDFS"""
        
        if output_path.startswith("s3://"):
            # Read from S3
            import boto3
            s3 = boto3.client('s3')
            bucket, key = output_path.replace("s3://", "").split("/", 1)
            obj = s3.get_object(Bucket=bucket, Key=key + "/part-00000.json")
            return json.loads(obj['Body'].read())
        else:
            # Read from HDFS or local
            with open(output_path + "/part-00000.json") as f:
                return json.load(f)

# Endpoint
@app.post("/api/evaluations/spark-bias-evaluation")
async def spark_bias_evaluation(
    dataset_path: str,
    protected_attributes: List[str]
):
    """Submit bias evaluation job a Spark"""
    
    output_path = f"s3://codeflowx-results/bias_eval_{uuid.uuid4()}"
    
    try:
        results = spark_service.submit_bias_evaluation_job(
            dataset_path,
            protected_attributes,
            output_path
        )
        
        return {
            "status": "completed",
            "bias_results": results,
            "output_path": output_path
        }
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
```

**Verificar:**
- Livy client conecta a Spark cluster
- Submit job funciona
- Job ejecuta en cluster cliente (NO copia datos a CodeflowX)
- Results retrieved
- Métricas guardadas CodeflowX

---

## 📦 GRUPO F: ORQUESTACIÓN + UI

### **PROMPT 10: Service Orquestación Sync Multi-Plataforma**

**Objetivo:** Service Java orquestar sync con todas las plataformas (scheduler + orchestration)

**Crear:**

```java
// service/external/ExternalPlatformOrchestrationService.java
package com.codeflowx.govern.service.external;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
public class ExternalPlatformOrchestrationService {
    
    @Autowired
    private DatabricksConnectorService databricksConnector;
    
    @Autowired
    private SnowflakeConnectorService snowflakeConnector;
    
    @Autowired
    private BusinessService businessService;
    
    /**
     * Sync ALL external platforms (scheduled cada hora)
     */
    @Scheduled(cron = "0 0 * * * *")  // Cada hora
    public void syncAllPlatforms() {
        log.info("🔄 Starting sync all external platforms");
        
        // Get platforms con sync enabled
        List<ExternalPlatformIntegration> platforms = businessService
            .findByProperty(ExternalPlatformIntegration.class, "eplsyncEnabled", true);
        
        for (ExternalPlatformIntegration platform : platforms) {
            try {
                syncPlatform(platform);
            } catch (Exception e) {
                log.error("Error syncing platform {}: {}", 
                         platform.getEplplatformName(), e.getMessage());
                
                // Update error status
                platform.setEplsyncStatus("ERROR");
                platform.setEpllastError(e.getMessage());
                businessService.save(platform);
            }
        }
        
        log.info("✅ Sync all platforms completed");
    }
    
    private void syncPlatform(ExternalPlatformIntegration platform) {
        log.info("Syncing platform: {} ({})", 
                 platform.getEplplatformName(), 
                 platform.getEplplatformType());
        
        // Update status
        platform.setEplsyncStatus("IN_PROGRESS");
        businessService.save(platform);
        
        try {
            // Route por tipo
            switch (platform.getEplplatformType()) {
                case "DATABRICKS":
                    databricksConnector.syncModelsFromDatabricks(
                        platform.getIdxexternalplatform()
                    );
                    break;
                    
                case "SNOWFLAKE":
                    snowflakeConnector.catalogDatasetsFromSnowflake(
                        platform.getEplmetadata(),  // database, schema
                        platform.getIdxexternalplatform()
                    );
                    break;
                    
                // TODO: AZURE_ML, SAGEMAKER, etc.
                
                default:
                    log.warn("Platform type not supported: {}", platform.getEplplatformType());
            }
            
            // Update success
            platform.setEplsyncStatus("SUCCESS");
            platform.setEpllastSyncAt(new Timestamp(System.currentTimeMillis()));
            platform.setEpllastError(null);
            businessService.save(platform);
            
            log.info("✅ Platform synced: {}", platform.getEplplatformName());
            
        } catch (Exception e) {
            throw e;  // Re-throw para manejo arriba
        }
    }
    
    /**
     * Sync single platform on-demand
     */
    public void syncPlatformNow(Long platformId) {
        ExternalPlatformIntegration platform = businessService.findById(
            ExternalPlatformIntegration.class,
            platformId
        );
        
        if (platform == null) {
            throw new RuntimeException("Platform not found: " + platformId);
        }
        
        syncPlatform(platform);
    }
}
```

**Verificar:**
- Scheduler funciona (sync cada hora)
- Sync all platforms funciona
- Sync single platform on-demand funciona
- Error handling correcto
- Status actualizado PostgreSQL

---

### **PROMPT 11: BPMN External Model Approval Workflow**

**Objetivo:** Workflow BPMN para aprobar modelos externos (Databricks, Azure ML) con notificación a plataforma

**Crear:**

```xml
<!-- bpmn/external-model-approval-workflow.bpmn -->
<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
             targetNamespace="http://codeflowx.com/govern/bpmn">
  
  <process id="external-model-approval-workflow" name="External Model Approval Workflow" isExecutable="true">
    
    <!-- Start Event -->
    <startEvent id="start" name="External Model Registered" />
    
    <!-- Service Task: Clasificar riesgo -->
    <serviceTask id="classifyRisk" name="Classify Risk (AI Act)" 
                 flowable:delegateExpression="${classifyExternalModelRiskDelegate}" />
    
    <!-- Exclusive Gateway: ¿HIGH_RISK? -->
    <exclusiveGateway id="riskGateway" name="Risk Level?" />
    
    <!-- Service Task: FRIA (si HIGH_RISK) -->
    <serviceTask id="executeFRIA" name="Execute FRIA (Art. 27)" 
                 flowable:delegateExpression="${executeFriaDelegate}" />
    
    <!-- User Task: Approval (si HIGH_RISK) -->
    <userTask id="approveModel" name="Approve Model (Compliance Officer)" 
              flowable:candidateGroups="compliance_officers" />
    
    <!-- Service Task: Notify Databricks approval -->
    <serviceTask id="notifyDatabricks" name="Notify Platform (Approve/Reject)" 
                 flowable:delegateExpression="${notifyExternalPlatformDelegate}" />
    
    <!-- Service Task: Auto-approve (si LIMITED_RISK) -->
    <serviceTask id="autoApprove" name="Auto-Approve (Low Risk)" 
                 flowable:delegateExpression="${autoApproveExternalModelDelegate}" />
    
    <!-- End Event -->
    <endEvent id="end" name="Approval Process Completed" />
    
    <!-- Sequence flows -->
    <sequenceFlow sourceRef="start" targetRef="classifyRisk" />
    <sequenceFlow sourceRef="classifyRisk" targetRef="riskGateway" />
    
    <sequenceFlow sourceRef="riskGateway" targetRef="executeFRIA">
      <conditionExpression>${riskLevel == 'HIGH_RISK'}</conditionExpression>
    </sequenceFlow>
    
    <sequenceFlow sourceRef="riskGateway" targetRef="autoApprove">
      <conditionExpression>${riskLevel != 'HIGH_RISK'}</conditionExpression>
    </sequenceFlow>
    
    <sequenceFlow sourceRef="executeFRIA" targetRef="approveModel" />
    <sequenceFlow sourceRef="approveModel" targetRef="notifyDatabricks" />
    <sequenceFlow sourceRef="autoApprove" targetRef="notifyDatabricks" />
    <sequenceFlow sourceRef="notifyDatabricks" targetRef="end" />
    
  </process>
</definitions>
```

**Delegates:**

```java
// workflow/delegates/external/NotifyExternalPlatformDelegate.java
@Component("notifyExternalPlatformDelegate")
public class NotifyExternalPlatformDelegate implements JavaDelegate {
    
    @Autowired
    private DatabricksConnectorService databricksConnector;
    
    @Override
    public void execute(DelegateExecution execution) {
        Long externalModelId = (Long) execution.getVariable("externalModelId");
        String approvalStatus = (String) execution.getVariable("approvalStatus");
        String platformType = (String) execution.getVariable("platformType");
        
        log.info("📤 Notifying external platform: {} - approval: {}", 
                 platformType, approvalStatus);
        
        // Route por tipo plataforma
        switch (platformType) {
            case "DATABRICKS":
                databricksConnector.notifyApprovalToDatabricks(externalModelId, approvalStatus);
                break;
            
            // TODO: AZURE_ML, SAGEMAKER, etc.
            
            default:
                log.warn("Platform type not supported for notification: {}", platformType);
        }
        
        log.info("✅ External platform notified");
    }
}
```

**Verificar:**
- Workflow deployado Camunda/Motor BPMN
- Trigger workflow cuando modelo externo registrado
- Clasificación riesgo automática
- FRIA ejecuta si HIGH_RISK
- User task aparece para Compliance Officer
- Aprobación → Notifica Databricks
- Rechazo → Bloquea en Databricks

---

### **PROMPT 12: Dashboard External Platforms Monitoring**

**Objetivo:** Dashboard ZUL para monitorear status sync plataformas externas, modelos pendientes aprobación

**Crear:**

```java
// viewmodel/integrations/ExternalPlatformsDashboardViewModel.java
@VariableResolver(DelegatingVariableResolver.class)
public class ExternalPlatformsDashboardViewModel {
    
    @WireVariable
    private BusinessService businessService;
    
    @Getter
    private Map<String, Object> dashboardData;
    
    @Init
    public void init() {
        loadDashboardData();
    }
    
    private void loadDashboardData() {
        dashboardData = new HashMap<>();
        
        // 1. Total platforms configured
        long totalPlatforms = businessService.count(ExternalPlatformIntegration.class);
        dashboardData.put("totalPlatforms", totalPlatforms);
        
        // 2. Platforms sync status
        long platformsSuccess = businessService.countByProperty(
            ExternalPlatformIntegration.class, "eplsyncStatus", "SUCCESS"
        );
        long platformsError = businessService.countByProperty(
            ExternalPlatformIntegration.class, "eplsyncStatus", "ERROR"
        );
        
        dashboardData.put("platformsSuccess", platformsSuccess);
        dashboardData.put("platformsError", platformsError);
        
        // 3. External models by approval status
        long modelsPending = businessService.countByProperty(
            ExternalModel.class, "exmapprovalStatus", "PENDING"
        );
        long modelsApproved = businessService.countByProperty(
            ExternalModel.class, "exmapprovalStatus", "APPROVED"
        );
        long modelsRejected = businessService.countByProperty(
            ExternalModel.class, "exmapprovalStatus", "REJECTED"
        );
        
        dashboardData.put("modelsPending", modelsPending);
        dashboardData.put("modelsApproved", modelsApproved);
        dashboardData.put("modelsRejected", modelsRejected);
        
        // 4. External models by risk level
        // (query custom - count models con HIGH_RISK)
        long modelsHighRisk = businessService.countWithCriteria(
            ExternalModel.class,
            "exmriskLevel @> ARRAY['HIGH_RISK']::text[]"
        );
        
        dashboardData.put("modelsHighRisk", modelsHighRisk);
        
        // 5. Recent sync activity (últimos 10)
        List<ExternalPlatformIntegration> recentSyncs = businessService
            .findOrderBy(ExternalPlatformIntegration.class, "epllastSyncAt", false, 10);
        
        dashboardData.put("recentSyncs", recentSyncs);
    }
}
```

**Pantalla ZUL:**

```xml
<!-- external-platforms-dashboard.zul -->
<window title="External Platforms Dashboard" width="100%">
    
    <!-- KPIs -->
    <hlayout spacing="20px">
        <div style="border:1px solid #ddd; padding:15px; border-radius:5px;">
            <label value="Total Platforms" style="font-size:12px; color:#666;" />
            <label value="@load(vm.dashboardData.totalPlatforms)" 
                   style="font-size:32px; font-weight:bold; color:#333;" />
        </div>
        
        <div style="border:1px solid #ddd; padding:15px; border-radius:5px;">
            <label value="Models Pending Approval" style="font-size:12px; color:#666;" />
            <label value="@load(vm.dashboardData.modelsPending)" 
                   style="font-size:32px; font-weight:bold; color:#FF9800;" />
        </div>
        
        <div style="border:1px solid #ddd; padding:15px; border-radius:5px;">
            <label value="Models HIGH RISK" style="font-size:12px; color:#666;" />
            <label value="@load(vm.dashboardData.modelsHighRisk)" 
                   style="font-size:32px; font-weight:bold; color:#F44336;" />
        </div>
        
        <div style="border:1px solid #ddd; padding:15px; border-radius:5px;">
            <label value="Platforms OK" style="font-size:12px; color:#666;" />
            <label value="@load(vm.dashboardData.platformsSuccess)" 
                   style="font-size:32px; font-weight:bold; color:#4CAF50;" />
        </div>
    </hlayout>
    
    <!-- Recent sync activity -->
    <separator height="20px" />
    <label value="Recent Sync Activity" style="font-weight:bold; font-size:16px;" />
    
    <listbox model="@load(vm.dashboardData.recentSyncs)">
        <listhead>
            <listheader label="Platform" />
            <listheader label="Type" />
            <listheader label="Status" />
            <listheader label="Last Sync" />
            <listheader label="Error" />
        </listhead>
        <template name="model" var="platform">
            <listitem>
                <listcell label="@load(platform.eplplatformName)" />
                <listcell label="@load(platform.eplplatformType)" />
                <listcell>
                    <label value="@load(platform.eplsyncStatus)" 
                           style="@load(platform.eplsyncStatus eq 'SUCCESS' ? 'color:green' : 'color:red')" />
                </listcell>
                <listcell label="@load(platform.epllastSyncAt) @converter('formatedDate')" />
                <listcell label="@load(platform.epllastError)" 
                         style="color:red; font-size:11px;" />
            </listitem>
        </template>
    </listbox>
</window>
```

**Verificar:**
- Dashboard muestra KPIs correctos
- Recent sync activity actualizada
- Real-time updates (refresh cada minuto)

---

## ✅ RESUMEN PROMPTS_12

**Total prompts:** 12 prompts

### **Databricks (3):**
1. Conector + sync models bidireccional
2. Webhooks receiver
3. UI gestión plataformas

### **Snowflake (2):**
4. Catalogación datasets (metadata only)
5. Data quality evaluation (sample)

### **Azure ML / SageMaker (2):**
6. Azure ML deployment monitoring
7. SageMaker deployment monitoring

### **Data Lakes (1):**
8. S3 / Azure Blob / GCS catalogación

### **Spark (1):**
9. Submit evaluation jobs (big data sin mover datos)

### **Framework (3):**
10. Orquestación sync multi-plataforma
11. BPMN external model approval workflow
12. Dashboard monitoring

---

## 📊 IMPACTO COMERCIAL

**Sin PROMPTS_12:**
- TAM: €3B (solo clientes greenfield)
- Pitch: "Plataforma all-in-one" (cliente debe migrar) ❌

**Con PROMPTS_12:**
- TAM: **€12B** (clientes greenfield + enterprise con infraestructura)
- Pitch: "Governance overlay sobre su Databricks" (cliente NO migra) ✅
- **4x más mercado accesible**

---

## 🎯 BENEFICIOS ARQUITECTURA OVERLAY

**Para cliente enterprise:**
1. ✅ Mantienen inversión existente (Databricks $100K/año)
2. ✅ Datos NO se mueven (permanecen en Snowflake)
3. ✅ Workflow ML Engineers NO cambia
4. ✅ Compliance AI Act automático añadido
5. ✅ ROI: $500K ahorro vs desarrollo interno

**Para CodeflowX:**
1. ✅ 4x más TAM ($12B vs $3B)
2. ✅ Diferenciador vs competidores (requieren migración)
3. ✅ Pitch potente ("nos integramos, no reemplazamos")
4. ✅ Menos fricción venta (no disrupción cliente)

---

**Última actualización:** 5 Noviembre 2025  
**Prioridad:** 🔴 **CRÍTICA COMERCIAL**  
**Estimación:** 15-20 días (con 3-4 chats = 6-8 días reales)  
**Impacto:** 4x TAM accesible (€3B → €12B)
