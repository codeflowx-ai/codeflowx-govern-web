# 🏗️ ESTRATEGIA DE IMPLEMENTACIÓN - MÓDULOS PENDIENTES NEXT.JS

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Estrategia técnica para implementar módulos faltantes del Next.js en ZKoss

---

## 🎯 ARQUITECTURA DE IMPLEMENTACIÓN

### **Componentes Principales:**

1. **📦 Módulo Maven:** `nocode.service.entitys` - Entidades JPA y DTOs
2. **🐍 Backend Python:** `leka-server` - Servicios de IA y procesamiento
3. **☕ Frontend ZKoss:** `suinsit.nova.web` - Pantallas y ViewModels
4. **🔗 SDK de Comunicación:** Mono con gRPC o REST

---

## 📦 MÓDULO MAVEN: `nocode.service.entitys`

### **Estructura Actual:**
```
src/main/java/com/codeflowx/govern/entity/
├── agents/           # Entidades de agentes
├── analytics/        # Entidades de analytics
├── evaluation/       # Entidades de evaluación
├── governance/       # Entidades de gobierno
├── infrastructure/  # Entidades de infraestructura
├── models/          # Entidades de modelos
├── monitoring/      # Entidades de monitoreo
├── platform/       # Entidades de plataforma
├── projects/       # Entidades de proyectos
├── prompts/        # Entidades de prompts
├── rag/            # Entidades de RAG
├── serving/        # Entidades de serving
├── training/       # Entidades de entrenamiento
└── views/          # Entidades de vistas
```

### **Nuevas Entidades Requeridas:**

#### **1. PLAYGROUND**
```java
// src/main/java/com/codeflowx/govern/entity/playground/
├── PlaygroundSession.java      # Sesión de playground
├── PlaygroundChat.java         # Chat del playground
├── PlaygroundImage.java        # Imágenes del playground
├── PlaygroundVoice.java        # Voz del playground
├── PlaygroundTranslation.java  # Traducción del playground
└── PlaygroundRouting.java      # Routing del playground
```

#### **2. INFRASTRUCTURE**
```java
// src/main/java/com/codeflowx/govern/entity/infrastructure/
├── InfrastructureProvider.java    # Proveedores de infraestructura
├── InfrastructureCredential.java  # Credenciales de infraestructura
├── InfrastructureDeployment.java  # Despliegues de infraestructura
├── InfrastructureGpuInstance.java # Instancias GPU
├── InfrastructureKubernetes.java  # Clusters Kubernetes
├── InfrastructureResource.java    # Recursos de infraestructura
└── InfrastructureCost.java        # Costos de infraestructura
```

#### **3. DOMAIN INGESTION**
```java
// src/main/java/com/codeflowx/govern/entity/domainingestion/
├── DomainIngestion.java        # Ingestión de dominios
├── DomainIngestionSource.java  # Fuentes de ingesta
├── DomainIngestionRule.java    # Reglas de ingesta
├── DomainIngestionMonitor.java # Monitoreo de ingesta
└── DomainIngestionQuality.java # Calidad de ingesta
```

#### **4. TECHNOLOGY MANAGEMENT**
```java
// src/main/java/com/codeflowx/govern/entity/technology/
├── Technology.java             # Tecnologías
├── TechnologyModel.java       # Modelos especializados
├── TechnologyArchitecture.java # Arquitecturas
├── TechnologyEvaluation.java  # Evaluación de tecnologías
└── TechnologyData.java        # Datos tecnológicos
```

#### **5. RAG TRAINING**
```java
// src/main/java/com/codeflowx/govern/entity/ragtraining/
├── RagTraining.java            # Entrenamiento RAG
├── RagTrainingConfig.java     # Configuración de entrenamiento
├── RagTrainingDocument.java   # Documentos de entrenamiento
├── RagTrainingService.java    # Servicios de entrenamiento
└── RagTrainingWebscraping.java # Webscraping para entrenamiento
```

#### **6. DATA SOURCES**
```java
// src/main/java/com/codeflowx/govern/entity/datasources/
├── DataSource.java            # Fuentes de datos
├── DataSourceApi.java        # APIs de fuentes de datos
├── DataSourceDatabase.java   # Bases de datos
├── DataSourceDocument.java   # Documentos
└── DataSourceWebscraping.java # Webscraping
```

#### **7. MODEL EVALUATION**
```java
// src/main/java/com/codeflowx/govern/entity/modelevaluation/
├── ModelEvaluation.java       # Evaluación de modelos
├── ModelEvaluationAbTest.java # Pruebas A/B
├── ModelEvaluationActivity.java # Actividad de evaluación
├── ModelEvaluationData.java  # Datos de evaluación
├── ModelEvaluationHitl.java  # Human-in-the-Loop
├── ModelEvaluationPipeline.java # Pipelines de evaluación
├── ModelEvaluationReport.java # Reportes de evaluación
├── ModelEvaluationSecurity.java # Seguridad de evaluación
└── ModelEvaluationService.java # Servicios de evaluación
```

#### **8. TRAINING CENTER**
```java
// src/main/java/com/codeflowx/govern/entity/trainingcenter/
├── TrainingCenter.java        # Centro de entrenamiento
├── TrainingCenterMetric.java # Métricas del centro
├── TrainingCenterNavigation.java # Navegación del centro
└── TrainingCenterStatus.java # Estado del centro
```

---

## 🗄️ SCRIPTS SQL

### **Ubicación:** `src/main/resources/sql/`

#### **1. PLAYGROUND**
```sql
-- playground_sessions.sql
CREATE TABLE playground_sessions (
    idxplaygroundsession BIGINT PRIMARY KEY,
    session_name VARCHAR(255),
    session_type VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    status VARCHAR(20)
);

-- playground_chats.sql
CREATE TABLE playground_chats (
    idxplaygroundchat BIGINT PRIMARY KEY,
    idxplaygroundsession BIGINT,
    message TEXT,
    response TEXT,
    created_at TIMESTAMP,
    FOREIGN KEY (idxplaygroundsession) REFERENCES playground_sessions(idxplaygroundsession)
);
```

#### **2. INFRASTRUCTURE**
```sql
-- infrastructure_providers.sql
CREATE TABLE infrastructure_providers (
    idxinfrastructureprovider BIGINT PRIMARY KEY,
    provider_name VARCHAR(100),
    provider_type VARCHAR(50),
    configuration JSON,
    status VARCHAR(20),
    created_at TIMESTAMP
);

-- infrastructure_credentials.sql
CREATE TABLE infrastructure_credentials (
    idxinfrastructurecredential BIGINT PRIMARY KEY,
    idxinfrastructureprovider BIGINT,
    credential_name VARCHAR(255),
    credential_type VARCHAR(50),
    encrypted_value TEXT,
    FOREIGN KEY (idxinfrastructureprovider) REFERENCES infrastructure_providers(idxinfrastructureprovider)
);
```

#### **3. DOMAIN INGESTION**
```sql
-- domain_ingestions.sql
CREATE TABLE domain_ingestions (
    idxdomainingestion BIGINT PRIMARY KEY,
    domain_name VARCHAR(255),
    ingestion_type VARCHAR(50),
    configuration JSON,
    status VARCHAR(20),
    created_at TIMESTAMP
);

-- domain_ingestion_sources.sql
CREATE TABLE domain_ingestion_sources (
    idxdomainingestionsource BIGINT PRIMARY KEY,
    idxdomainingestion BIGINT,
    source_name VARCHAR(255),
    source_type VARCHAR(50),
    connection_config JSON,
    FOREIGN KEY (idxdomainingestion) REFERENCES domain_ingestions(idxdomainingestion)
);
```

---

## 🐍 BACKEND PYTHON: `leka-server`

### **Servicios Requeridos:**

#### **1. PLAYGROUND SERVICE**
```python
# services/playground_service.py
class PlaygroundService:
    def create_chat_session(self, session_data: dict) -> dict
    def process_chat_message(self, message: str, session_id: str) -> dict
    def generate_image(self, prompt: str) -> dict
    def process_voice(self, audio_data: bytes) -> dict
    def translate_text(self, text: str, target_language: str) -> dict
```

#### **2. INFRASTRUCTURE SERVICE**
```python
# services/infrastructure_service.py
class InfrastructureService:
    def manage_providers(self, provider_data: dict) -> dict
    def deploy_resources(self, deployment_config: dict) -> dict
    def monitor_costs(self, cost_params: dict) -> dict
    def scale_resources(self, scaling_config: dict) -> dict
```

#### **3. DOMAIN INGESTION SERVICE**
```python
# services/domain_ingestion_service.py
class DomainIngestionService:
    def configure_ingestion(self, config: dict) -> dict
    def start_webscraping(self, scraping_config: dict) -> dict
    def upload_documents(self, documents: list) -> dict
    def monitor_ingestion(self, monitoring_params: dict) -> dict
```

---

## 🔗 SDK DE COMUNICACIÓN

### **Implementación con Mono:**

#### **1. PLAYGROUND SDK**
```java
// src/main/java/com/codeflowx/govern/sdk/playground/
public class PlaygroundSdk {
    private final Mono<PlaygroundServiceGrpc.PlaygroundServiceBlockingStub> stub;
    
    public Mono<PlaygroundSessionDto> createChatSession(PlaygroundSessionDto session) {
        return Mono.fromCallable(() -> stub.block().createChatSession(session));
    }
    
    public Mono<PlaygroundChatDto> processMessage(PlaygroundChatDto message) {
        return Mono.fromCallable(() -> stub.block().processMessage(message));
    }
}
```

#### **2. INFRASTRUCTURE SDK**
```java
// src/main/java/com/codeflowx/govern/sdk/infrastructure/
public class InfrastructureSdk {
    private final Mono<InfrastructureServiceGrpc.InfrastructureServiceBlockingStub> stub;
    
    public Mono<InfrastructureProviderDto> manageProvider(InfrastructureProviderDto provider) {
        return Mono.fromCallable(() -> stub.block().manageProvider(provider));
    }
    
    public Mono<InfrastructureDeploymentDto> deployResources(InfrastructureDeploymentDto deployment) {
        return Mono.fromCallable(() -> stub.block().deployResources(deployment));
    }
}
```

---

## 📋 PLAN DE IMPLEMENTACIÓN

### **Fase 1: Entidades JPA (1 semana)**
1. **Crear entidades** en `nocode.service.entitys`
2. **Generar scripts SQL** en `src/main/resources/sql/`
3. **Configurar relaciones** JPA entre entidades
4. **Integrar con governance** existente

### **Fase 2: Backend Python (2 semanas)**
1. **Implementar servicios** en `leka-server`
2. **Crear APIs gRPC/REST** para comunicación
3. **Integrar con modelos** de IA existentes
4. **Implementar procesamiento** de datos

### **Fase 3: SDK de Comunicación (1 semana)**
1. **Crear SDKs** con Mono para cada módulo
2. **Implementar comunicación** gRPC/REST
3. **Manejar errores** y timeouts
4. **Implementar retry** y circuit breakers

### **Fase 4: Frontend ZKoss (2 semanas)**
1. **Crear pantallas** siguiendo estructura Next.js
2. **Implementar ViewModels** con SDKs
3. **Integrar con governance** existente
4. **Implementar navegación** y breadcrumbs

---

## 🔧 CONSIDERACIONES TÉCNICAS

### **Integración con Governance:**
- ✅ **Reutilizar entidades** existentes cuando sea posible
- ✅ **Extender funcionalidades** de governance
- ✅ **Mantener consistencia** con patrones actuales
- ✅ **Integrar auditoría** y compliance

### **Patrones JPA:**
- ✅ **Usar @Entity** y @Table
- ✅ **Implementar @Id** y @GeneratedValue
- ✅ **Configurar relaciones** @OneToMany, @ManyToOne
- ✅ **Usar @JsonIgnore** para evitar recursión
- ✅ **Implementar @Audited** para auditoría

### **Comunicación Backend:**
- ✅ **Usar Mono** para operaciones asíncronas
- ✅ **Implementar timeout** y retry
- ✅ **Manejar errores** gracefully
- ✅ **Logging** detallado para debugging

---

## 🎯 PRÓXIMOS PASOS

1. **Revisar entidades existentes** en `nocode.service.entitys`
2. **Identificar reutilización** posible
3. **Crear nuevas entidades** siguiendo patrones
4. **Generar scripts SQL** correspondientes
5. **Implementar servicios** en `leka-server`
6. **Crear SDKs** de comunicación
7. **Implementar pantallas** ZKoss

---

## 📚 REFERENCIAS

- **Módulo Entidades:** `/mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service/nocode.service.entitys/`
- **Backend Python:** `leka-server` (pendiente de ubicación)
- **Frontend ZKoss:** `/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/`
- **Guía Implementación:** `docs/GUIA_IMPLEMENTACION_MODULOS_PENDIENTES.md`

---

**Esta estrategia proporciona la hoja de ruta técnica completa para implementar los módulos faltantes del Next.js en ZKoss, integrando todo con el sistema de governance existente.**

