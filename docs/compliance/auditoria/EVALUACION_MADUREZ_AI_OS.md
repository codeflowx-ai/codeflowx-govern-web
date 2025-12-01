# EVALUACIÓN DE MADUREZ DE CODEFLOWX OS PARA AI OS

**Fecha:** 25 de noviembre de 2025
**Versión:** 1.0
**Evaluador:** Sistema de Análisis Técnico
**Base Legal:** EU AI Act Compliance

---

## 1. ARQUITECTURA ACTUAL

### ¿Qué componentes principales existen hoy en CodeflowX OS?

**Componentes Principales Implementados:**

1. **Framework de Gobernanza (Governance Core)**
   - **Entidades JPA:** 489 entidades en `nocode.service.entitys` (Model, Agent, Dataset, FRIA, ComplianceAssessment, etc.)
   - **BusinessService:** Capa de persistencia genérica (`codeflowx.nocode.persist.BusinessService`) - CRUD para todas las entidades
   - **Repositorios:** Repositorios Spring Data JPA reutilizables (`nocode.service.repository`)
   - **BusinessServices Especializados:** Servicios de negocio en `codeflowx.govern.business`
   - **Procesos BPMN:** 17+ procesos automatizados (Flowable 6.8.1) en `codeflowx.govern.workflow.lib`
   - **Reglas Drools:** 11 paquetes DRL con 120+ reglas de negocio declarativas
   - **Microservicio BPMN:** `codeflowx.govern.bpmn` (puerto 8095) - Orquestación de procesos BPMN
   - **Framework FaaS:** `codeflowx.govern.faas` - 51 verticales sectoriales preconfigurados con workflows BPMN, reglas Drools, dashboards ZUL, ViewModels, métricas y reports por sector

2. **Capa de Orquestación (Orchestration Runtime)**
   - **Delegates Java:** 65+ JavaDelegates para Service Tasks BPMN
   - **Servicios de Integración:** ComplianceCheckService, RiskAssessmentService, BiasDetectionService
   - **AI OS Runtime Services:** `AioDeploymentService`, `AioServiceBindingService`, `AioTelemetryService`, `AioScheduleService` (en implementación según PROMPT_002)

3. **Capa de Experiencia (Experience Layer)**
   - **Framework ZKoss:** `codeflowx.web.zkoss` - Framework completo para UI
   - **ViewModels ZKoss:** 410 ViewModels CRUD autogenerados + 25 ViewModels para User Tasks BPMN
   - **Pantallas ZUL:** 742 pantallas ZUL organizadas por módulo (dashboards/platform/gobierno/bpmn)
   - **BaseFront:** Clase base para ViewModels con servicios comunes (en `codeflowx.web.zkoss`)
   - **Componentes UI:** Componentes ZKoss reutilizables (charts, pivotable, spreadsheet, etc.)

4. **Capa de Persistencia**
   - **PostgreSQL 14+:** Base de datos principal (gobernanza, entidades JPA)
   - **TimescaleDB (codeflowx_telemetry):** Base de datos separada para telemetría en tiempo real
     - **Tabla:** `AIOTELEMETRY` (Hypertable) - Almacena mensajes y respuestas de IA, agentes, LLMs
     - **Características:**
       - Particionamiento automático por tiempo (chunks diarios)
       - Compresión automática (después de 30 días, 5-10x reducción)
       - Retención automática (1 año)
       - pgvector para embeddings y búsqueda semántica
       - Índices GIN para queries eficientes en JSONB
     - **Uso:** Análisis en tiempo real, estadísticas, detección de problemas, disparo de procesos BPMN
   - **Qdrant:** Vector database para RAG (en uso)
   - **MinIO:** Object storage (en uso)
   - **OpenSearch:** Búsqueda y análisis (en uso)

5. **Microservicios Python de Governance (FastAPI) - 20+ microservicios:**
   - **Core Evaluation:**
     - `leka-bias-detection-service` (puerto 8001) - 17 endpoints: bias, drift, data quality, explainability, robustness, privacy, performance, uncertainty, features, model card, label leakage, benchmark fairness
     - `leka-llm-evaluation` (puerto 8002) - 18 endpoints: hallucination, toxicity, bias, quality, prompt injection, instruction following, consistency, factual grounding, cost efficiency, benchmarking, A/B testing, sentiment analysis
     - `leka-prompt-governance` (puerto 8003) - 14 endpoints: safety, effectiveness, PII leakage, version comparison, cost analysis, template validation, context optimization, few-shot evaluation, output format validation, complexity analysis, prompt drift
     - `leka-rag-evaluation` (puerto 8004) - 24 endpoints: retrieval, answer quality, context relevance, full pipeline, index quality, document quality, conversation context, citations, A/B testing, benchmarking, learning, thresholds, policy validation, grounding, user feedback, validation queue
     - `leka-agent-monitoring` (puerto 8005) - 20 endpoints: execution analysis, reliability, cost, loop detection, multi-agent orchestration, tool usage, safety violations, benchmarking, immutable logging, log integrity, human interventions, A/B testing, feedback collection
     - `leka-model-wrapper` (puerto 8006) - 18 endpoints: invoke, batch invoke, compare, list available, adaptation recommendation, adapter config validation, cost estimation, recommendations, benchmarking, smart routing, streaming, A/B testing, performance benchmarking, drift detection, usage analytics
     - `leka-adversarial-robustness` (puerto 8007) - 5 endpoints: model poisoning detection, evasion testing, attack simulation, policy validation
     - `leka-llm-interpreter` (puerto 8011) - 8 endpoints: explain result, answer question, executive summary, root cause analysis, chat, disclosure generation, stats

   - **Compliance & Documentation:**
     - `leka-conformity-assessment` (puerto 8009) - 6 endpoints: Annex VI assessment (4 pasos), checklist generation, evidence validation, template
     - `leka-eu-declaration-generator` (puerto 8010) - 5 endpoints: EU conformity declaration (Annex V), validation, template, hash generation
     - `leka-fria-generator` (puerto 8012) - 7 endpoints: FRIA assessment generation, PDF generation, fundamental rights analysis, DPIA integration, template, cross-validation
     - `leka-iso42001-annex-a-assessor` - 5 endpoints: Annex A assessment (39 controles), control-specific assessment, gap closure plan, controls reference
     - `leka-multi-framework-compliance-aggregator` (puerto 8060) - 7 endpoints: overall compliance score, frameworks supported, compliance report export, certification roadmap

   - **Ethics & Risk:**
     - `leka-ethics-risk-assessor` (puerto 8010) - 5 endpoints: ethical risk assessment, Charter articles mapping, vulnerable groups identification
     - `leka-board-governance-calculator` (puerto 8061) - 5 endpoints: EDM metrics (ISO 38507), executive summary, health score

   - **Content & Copyright:**
     - `leka-copyright-compliance` - 5 endpoints: training data compliance, TDM opt-out detection, attribution generation, license validation
     - `leka-deepfake-detect` (puerto 8012) - 6 endpoints: image/video/audio deepfake detection, watermarking, disclosure generation

   - **Infrastructure:**
     - `leka-serving-wrapper` (puerto 8000) - 52 endpoints: chat, audio, image, usage management, pool management, providers management, plugins management, metrics, health
     - `leka-orchestrator` - Orquestación de microservicios
     - `leka-data-lineage` - Trazabilidad de datos
     - `leka-explainability` - Explicabilidad avanzada
     - `leka-sustainability-metrics` - Métricas de sostenibilidad
     - `leka-compliance-reporter` - Reportes de compliance
     - `leka-customers-portal` - Portal de clientes

   - **Gateway:**
     - **Spring Cloud Gateway** (puerto 8000) - `api-leka-govern` - Gateway unificado para todos los microservicios

   - **Cliente Java Unificado:**
     - `codeflowx.govern.nocode.client` - Cliente REST reactivo (WebClient) con clientes especializados por microservicio
     - **Total:** 199+ endpoints mapeados
     - **Clientes:** BiasDetectionClient, LLMEvaluationClient, PromptGovernanceClient, RAGEvaluationClient, AgentMonitoringClient, ModelWrapperClient, AIInterpreterClient, ConformityAssessmentClient, EUDeclarationGeneratorClient, FRIAGeneratorClient, AdversarialRobustnessClient, CopyrightComplianceClient, DeepfakeDetectionClient, EthicsRiskAssessorClient, BoardGovernanceCalculatorClient, MultiFrameworkComplianceAggregatorClient, ISO42001AnnexAAssessorClient, ServingWrapperClient

6. **Servidores de Inferencia y Serving (`leka-server-serving-*`):**
   - `leka-server-serving` - Servidor principal de serving
   - `leka-server-serving-audio` - Servidor de audio (transcripción, clasificación)
   - `leka-server-serving-documents` - Servidor de documentos
   - `leka-server-serving-evaluation` - Servidor de evaluación
   - `leka-server-serving-providers` - Servidor de proveedores
   - `leka-server-serving-rag` - Servidor RAG
   - `leka-server-serving-text` - Servidor de texto
   - `leka-server-serving-training` - Servidor de entrenamiento
   - `leka-server-serving-vision` - Servidor de visión
   - `leka-server-serving-wrapper` - Wrapper unificado de serving

7. **Servidores de Infraestructura (`leka-server-*`):**
   - `leka-server` - Servidor principal
   - `leka-server-agents` - Servidor de agentes
   - `leka-server-audio` - Servidor de audio
   - `leka-server-connectors` - Servidor de conectores
   - `leka-server-core` - Core del servidor
   - `leka-server-deployments` - Servidor de despliegues
   - `leka-server-documents` - Servidor de documentos
   - `leka-server-endpoint` - Endpoint principal
   - `leka-server-endpoint-evaluation` - Endpoint de evaluación
   - `leka-server-endpoint-training` - Endpoint de entrenamiento
   - `leka-server-generator` - Generador
   - `leka-server-governance` - Servidor de gobernanza
   - `leka-server-metrics` - Servidor de métricas
   - `leka-server-model-loader` - Cargador de modelos
   - `leka-server-orchestrator` - Orquestador
   - `leka-server-plugins` - Servidor de plugins
   - `leka-server-prompts` - Servidor de prompts
   - `leka-server-rag` - Servidor RAG
   - `leka-server-registry` - Registro
   - `leka-server-tools` - Herramientas
   - `leka-server-training` - Servidor de entrenamiento
   - `leka-server-vision` - Servidor de visión
   - `leka-server-workflows` - Servidor de workflows
   - `leka-server-wrapper` - Wrapper del servidor

8. **Microservicios Java Spring Boot:**
   - **APIs REST:**
     - `codeflowx-governance-api` - API REST principal de gobernanza
     - `codeflowx-aios-api` (puerto 8080) - API AI OS completa (OpenAPI/Swagger)
     - `codeflowx-governance-documentation` (puerto 8085) - Generación de documentos PDF (iText + FreeMarker)
     - `codeflowx.govern.bpmn` (puerto 8095) - Microservicio Spring Boot para orquestar BPMN/Drools
       - **Funcionalidad:** Despliegue y ejecución de procesos BPMN
       - **Endpoints:** API REST para gestión de procesos (deploy, start, query)
       - **Auto-despliegue:** Carga automática de procesos desde classpath
       - **Integración:** Flowable 6.8.0 + Redis cache + Spring Security

   - **Telemetría y Eventos (Análisis en Tiempo Real):**
     - `codeflowx-aios-telemetry` (puerto 8096) - Microservicio REST de telemetría
       - **Función:** Recibe eventos HTTP de agentes externos (IA, LLMs, agentes, Copilot, ChatGPT, n8n, MLflow)
       - **Endpoints:**
         - `POST /api/v1/aios/telemetry/events` - Evento individual
         - `POST /api/v1/aios/telemetry/events/batch` - Múltiples eventos (batch)
         - `POST /api/v1/aios/telemetry/mlflow-trace` - Traces MLflow
         - `GET /api/v1/aios/telemetry/health` - Health check
       - **Arquitectura:** Valida payloads → Publica a RabbitMQ (cola `aios.telemetry.events`)

     - `codeflowx-aios-telemetry-worker` - Worker de análisis en tiempo real
       - **Función:** Analiza en tiempo real mensajes y respuestas de IA, agentes, LLMs
       - **Flujo:**
         1. Consume eventos de RabbitMQ
         2. **Análisis independiente por tipo:**
            - **Bias Detection:** Análisis de sesgo en respuestas
            - **Toxicity Detection:** Detección de contenido tóxico
            - **PII Detection:** Detección de información personal identificable
            - **Secret Detection:** Detección de secretos (API keys, passwords)
            - **Data Leakage Detection:** Detección de fuga de datos
            - **Compliance Check:** Verificación de cumplimiento
         3. **Almacenamiento:** Persiste en base de datos separada `codeflowx_telemetry` (TimescaleDB)
         4. **Disparo de Procesos:** Si incumplen reglas, dispara procesos BPMN automáticamente:
            - `bias-detection-v1` - Si se detecta sesgo crítico
            - `alert-response-v1` - Si se detecta contenido tóxico o inapropiado
            - `performance-degradation-v1` - Si se detecta degradación de performance
            - `drift-detection-v1` - Si se detecta drift en distribución
            - `ai-runtime-health-v1` - Si hay múltiples alertas o errores
            - `incident-reporting-process` - Si hay violación crítica de compliance
            - `ai-policy-review-v1` - Si hay violación de política AI-OS
       - **Base de Datos:** TimescaleDB (`codeflowx_telemetry`) - Hypertable particionada por tiempo
         - **Tabla:** `AIOTELEMETRY` - Almacena todos los eventos con:
           - Payload completo (request + response) en JSONB
           - Métricas (latency, tokens, cost) en JSONB
           - Embeddings (pgvector) para búsqueda semántica
           - Flags de análisis ejecutado (bias, toxicity, PII, secrets, etc.)
           - Resultados de análisis de gobernanza en JSONB
           - ID del proceso BPMN disparado (si aplica)
         - **Características:**
           - Particionamiento automático por tiempo (chunks diarios)
           - Compresión automática (después de 30 días, 5-10x reducción)
           - Retención automática (1 año)
           - Índices GIN para JSONB
           - pgvector para búsqueda semántica

     - `codeflowx-governance-events-service` - Servicio de eventos de gobernanza
     - `codeflowx-governance-scheduler-worker` - Worker de tareas programadas

   - **Discovery y Serving:**
     - `codeflowx-discovery-serving` - Servicio de descubrimiento y serving

9. **Módulos de Framework y Persistencia:**
   - **Persistencia:**
     - `codeflowx.nocode.persist` - BusinessService (capa de persistencia genérica)
       - **Función:** CRUD genérico para todas las entidades JPA
       - **Métodos:** findAllEntity, findById, save, update, delete, query, findByParams
       - **Uso:** Usado por todos los ViewModels y Delegates
       - **Framework:** EnArt EntityDao (NO Hibernate JPA estándar)

   - **Repositorios:**
     - `nocode.service.repository` - Repositorios Spring Data JPA reutilizables
       - **RAG:** RagImmutableLogRepository, RagClientPolicyRepository, RagEthicalValueRepository
       - **AIOS:** 10+ repositorios (AioWorkspaceRepository, AioComponentRepository, etc.)
       - **Logging:** ImmutableLogRepository

   - **Framework FaaS (Framework as a Software):**
     - `codeflowx.govern.faas` - Framework FaaS con 51 verticales sectoriales preconfigurados
       - **Función:** Sistema de metamodelos sectoriales que permite adaptar CodeflowX a diferentes sectores sin modificar el esquema de base de datos
       - **Arquitectura:**
         - **Catálogos** (`cor_catalogue`, `cor_catalogitem`) para definir sectores y atributos
         - **Configuraciones** (`cor_configs`) para almacenar frameworks FaaS en YAML/JSON
         - **Políticas Drools** (`cor_policyregistry`) para reglas sectoriales versionadas
         - **Metadatos JSONB** en tablas core para flexibilidad

       - **51 Verticales Incluidos:**
         1. **AGRIFOOD** - Agricultura y Alimentación
         2. **BPO_SERVICES** - Business Process Outsourcing
         3. **CONSTRUCTION** - Construcción
         4. **EDU_SERVICES** - Educación
         5. **ENERGY_UTILITIES** - Energía y Servicios Públicos
         6. **ENV_SERVICES** - Servicios Medioambientales
         7. **FIN_BANKING** - Banca
         8. **FIN_INSURANCE** - Seguros
         9. **FINANCE_CORE** - Finanzas Core
         10. **FINANCE_FINTECH** - Fintech
         11. **FINANCE_INTERMEDIATION** - Intermediación Financiera
         12. **FINANCE_PAYMENTS** - Pagos
         13. **HEALTH_FARMA** - Salud y Farmacia
         14. **HEALTHCARE_CLINICAL** - Clínica
         15. **HEALTHCARE_HOSPITALS** - Hospitales
         16. **HOSPITALITY_EVENTS** - Eventos
         17. **HOSPITALITY_HOTELS** - Hoteles
         18. **HOSPITALITY_RESTAURANTS** - Restaurantes
         19. **HOSPITALITY_TOURISM** - Turismo
         20. **INFO_COMMS** - Información y Comunicaciones
         21. **INFO_DATA_SERVICES** - Servicios de Datos
         22. **INFO_TELECOM** - Telecomunicaciones
         23. **LEGAL_SERVICES** - Servicios Legales
         24. **MANUFACTURING** - Manufactura
         25. **MANUFACTURING_AEROSPACE** - Aeroespacial
         26. **MANUFACTURING_AUTOMOTIVE** - Automotriz
         27. **MANUFACTURING_CHEMICALS** - Química
         28. **MANUFACTURING_ELECTRONICS** - Electrónica
         29. **MANUFACTURING_HEAVY_MACHINERY** - Maquinaria Pesada
         30. **MEDIA_ADVERTISING** - Publicidad
         31. **MEDIA_ENTERTAINMENT** - Entretenimiento
         32. **MEDIA_GAMING** - Gaming
         33. **MEDIA_NEWS_PUBLISHING** - Noticias y Publicación
         34. **MEDIA_STREAMING** - Streaming
         35. **MEDICAL_DEVICES** - Dispositivos Médicos
         36. **MINING_RESOURCES** - Minería y Recursos
         37. **PHARMA_LIFE_SCIENCES** - Farmacia y Ciencias de la Vida
         38. **PRO_SERVICES** - Servicios Profesionales
         39. **PUBLIC_ADMIN** - Administración Pública
         40. **REAL_ESTATE** - Inmobiliaria
         41. **RETAIL_GOVERNANCE** - Retail y Comercio
         42. **SPECIALTY_INSURANCE** - Seguros Especializados
         43. **TECH_AI_ML** - IA y Machine Learning
         44. **TECH_CLOUD_INFRA** - Infraestructura Cloud
         45. **TECH_CYBERSECURITY** - Ciberseguridad
         46. **TECH_IOT** - IoT
         47. **TECH_PLATFORMS** - Plataformas Tecnológicas
         48. **TECH_SAAS** - SaaS
         49. **TRANSPORT_LOGISTICS** - Transporte y Logística
         50. **HR_TALENT** - Talento y RRHH
         51. **BIOTECH** - Biotecnología

       - **Componentes por Vertical:**
         - **Workflows BPMN:** Cada vertical incluye 5+ procesos BPMN estándar:
           - `compliance-monitoring-v1` - Monitoreo de cumplimiento
           - `incident-reporting-process` - Reporte de incidentes
           - `conformity-assessment-process` - Evaluación de conformidad
           - `fria-process` - Proceso FRIA
           - `eu-database-registration-process` - Registro en base de datos EU
           - **Procesos específicos:** Algunos verticales incluyen procesos adicionales (ej: `edu-consent-hub` para educación)

         - **Reglas Drools:**
           - **Base:** `faas-sector-governance.drl` - Reglas comunes de validación de metamodelo
           - **Específicas:** Cada vertical tiene su paquete Drools (ej: `com.codeflowx.rules.faas.education`)
           - **Ejemplo Educación:** 7+ reglas específicas (consentimiento parental, transparencia IA, sesgo en contenido, proctoring, retención de datos, accesibilidad)

         - **Microservicios:**
           - **Mandatorios:** `leka-conformity-assessment`, `leka-fria-generator`, `leka-eu-declaration-generator`, `leka-orchestrator`
           - **Opcionales:** `leka-llm-evaluation`, `leka-bias-detection-service`
           - **Específicos:** Algunos verticales requieren microservicios adicionales

         - **Dashboards ZUL:**
           - **Ubicación:** `/console/gobierno/compliance/faas/<framework>-framework.zul`
           - **Ejemplos:** `finance-core-framework.zul`, `retail-governance-framework.zul`, `info-comms-framework.zul`
           - **ViewModels:** `com.codeflowx.govern.viewmodel.faas.<framework>.<Framework>ViewModel`
           - **Funcionalidad:** Visualización de métricas, KPIs, procesos, capacidades por sector

         - **Métricas y KPIs:**
           - **Catálogo JSON:** `metrics/<framework>/catalog.json`
           - **KPIs por vertical:** `governance.kpi.<sector>.risk_index`, `governance.kpi.art71_compliance_rate`, `governance.kpi.incident_mttr`, `governance.kpi.agreement_coverage`
           - **Datasets:** `datasets/faas/<framework>-dataset.yaml`

         - **Reports:**
           - **Tipo FRIA:** Plantillas de documentos técnicos
           - **Templates:** `templates/fria/default.docx`

       - **Repositorios Git:** Cada vertical tiene su repositorio `codeflowx-faas-<sector>` con:
         - Documentación específica del sector
         - Configuraciones adicionales
         - Extensiones sectoriales

       - **Uso:**
         - **Activación:** Los frameworks se activan mediante "governed projects" reutilizables
         - **Personalización:** Se despliega una base probada y después se personaliza según cliente
         - **Extensibilidad:** Partners pueden crear frameworks sectoriales siguiendo el metamodelo

10. **Módulos de Integración:**
   - **Integración Copilot:**
     - `codeflowx.govern.integration.copilot` - Integración con Microsoft Copilot
       - **MicrosoftCopilotConnectorService:** Sincroniza interacciones Copilot → CodeflowX
       - **Microsoft Graph API:** Obtiene metadata de interacciones
       - **Clasificación:** Clasifica riesgo automáticamente

   - **Integración Seguridad:**
     - `codeflowx.govern.integration.security` - Seguridad e integridad
       - **CredentialEncryptionService:** Cifrado AES-256-GCM de credenciales
       - **MetadataIntegrityService:** Validación SHA-256 de metadata (detección de corrupción)
       - **WebhookRateLimiter:** Rate limiting con Redis para webhooks de plataformas externas

   - **Integración Monitoreo:**
     - `codeflowx.govern.integration.monitoring` - Monitoreo de integraciones
       - **ExternalPlatformSyncService:** Métricas Micrometer para sync de plataformas
       - **Métricas:** Latencia, éxito/error, detección de syncs lentos

   - **Integración Retry:**
     - `codeflowx.govern.integration.retry` - Retry con backoff exponencial
       - **RetryConfig:** RetryTemplates configurados por plataforma
       - **Plataformas:** Databricks (3 intentos), Snowflake (2 intentos), Azure ML (3 intentos)
       - **Backoff:** Exponencial con intervalos configurables

11. **Framework UI:**
   - **ZKoss Framework:**
     - `codeflowx.web.zkoss` - Framework ZKoss para UI
       - **BaseFront:** Clase base para ViewModels con servicios comunes
       - **Componentes UI:** Componentes reutilizables ZKoss
       - **Integración:** EnArt Framework (enart-ui-zkoss, enart-persistence, etc.)
       - **Dependencias:** ZKoss 10.0.2, ZK Charts, ZK Pivotable, Keikai (spreadsheet)

### ¿Cómo se organiza el backend (servicios, microservicios, módulos)?

**Organización del Backend:**

```
nocode.service/
├── nocode.service.entitys/          # 489 Entidades JPA
├── nocode.service.repository/       # Repositorios Spring Data JPA reutilizables
│   ├── RAG: RagImmutableLogRepository, RagClientPolicyRepository, RagEthicalValueRepository
│   ├── AIOS: AioWorkspaceRepository, AioComponentRepository, AioMarketplaceEntryRepository,
│   │        AioAccessTokenRepository, AioCapabilityRepository, AioConsumptionRepository,
│   │        AioDeploymentRepository, AioEventRepository, AioPolicyBindingRepository,
│   │        AioScheduleRepository, AioServiceBindingRepository
│   └── Logging: ImmutableLogRepository
├── codeflowx.nocode.persist/         # BusinessService - Capa de persistencia genérica (CRUD)
│   └── BusinessService: findAllEntity, findById, save, update, delete, query, findByParams, etc.
├── codeflowx.govern.nocode.dtos/     # DTOs compartidos (TelemetryEventRequestDto, etc.)
├── codeflowx.govern.business/        # BusinessServices (lógica de negocio especializada)
├── codeflowx.govern.services/        # Services CRUD
├── codeflowx.govern.workflow.lib/    # BPMN + Delegates + Drools (biblioteca compartida)
├── codeflowx.govern.workflow.engine/ # Motor BPMN (Flowable) - Integración con workflow.lib
├── codeflowx.govern.bpmn/            # Microservicio Spring Boot BPMN (puerto 8095)
│   ├── WorkflowEngineApplication - Aplicación Spring Boot
│   ├── ProcessDeploymentService - Despliegue de procesos BPMN
│   ├── ProcessRuntimeService - Ejecución de procesos
│   ├── ProcessController - API REST para gestión de procesos
│   └── Auto-despliegue de procesos desde classpath
├── codeflowx.govern.faas/            # Framework FaaS - 51 Verticales Sectoriales
│   ├── FaasFrameworkCollection - Carga de frameworks desde YAML
│   ├── 51 Verticales Preconfigurados:
│   │   - AGRIFOOD, BPO_SERVICES, CONSTRUCTION, EDU_SERVICES, ENERGY_UTILITIES
│   │   - ENV_SERVICES, FIN_BANKING, FIN_INSURANCE, FINANCE_CORE, FINANCE_FINTECH
│   │   - FINANCE_INTERMEDIATION, FINANCE_PAYMENTS, HEALTH_FARMA, HEALTHCARE_CLINICAL
│   │   - HEALTHCARE_HOSPITALS, HOSPITALITY_EVENTS, HOSPITALITY_HOTELS
│   │   - HOSPITALITY_RESTAURANTS, HOSPITALITY_TOURISM, INFO_COMMS, INFO_DATA_SERVICES
│   │   - INFO_TELECOM, LEGAL_SERVICES, MANUFACTURING, MANUFACTURING_AEROSPACE
│   │   - MANUFACTURING_AUTOMOTIVE, MANUFACTURING_CHEMICALS, MANUFACTURING_ELECTRONICS
│   │   - MANUFACTURING_HEAVY_MACHINERY, MEDIA_ADVERTISING, MEDIA_ENTERTAINMENT
│   │   - MEDIA_GAMING, MEDIA_NEWS_PUBLISHING, MEDIA_STREAMING, MEDICAL_DEVICES
│   │   - MINING_RESOURCES, PHARMA_LIFE_SCIENCES, PRO_SERVICES, PUBLIC_ADMIN
│   │   - REAL_ESTATE, RETAIL_GOVERNANCE, SPECIALTY_INSURANCE, TECH_AI_ML
│   │   - TECH_CLOUD_INFRA, TECH_CYBERSECURITY, TECH_IOT, TECH_PLATFORMS
│   │   - TECH_SAAS, TRANSPORT_LOGISTICS, HR_TALENT, BIOTECH
│   ├── Componentes por Vertical:
│   │   - Workflows BPMN (5+ procesos estándar + específicos)
│   │   - Reglas Drools (base + específicas por sector)
│   │   - Microservicios (mandatorios + opcionales)
│   │   - Dashboards ZUL (/console/gobierno/compliance/faas/<framework>-framework.zul)
│   │   - ViewModels (com.codeflowx.govern.viewmodel.faas.<framework>)
│   │   - Métricas y KPIs (catalog.json + datasets)
│   │   - Reports (FRIA templates)
│   └── Repositorios: codeflowx-faas-<sector> (51 repositorios Git)
├── codeflowx.govern.integration.copilot/  # Integración Microsoft Copilot
│   └── MicrosoftCopilotConnectorService - Sincroniza interacciones Copilot → CodeflowX
├── codeflowx.govern.integration.security/ # Seguridad e Integridad
│   ├── CredentialEncryptionService - Cifrado AES-256-GCM de credenciales
│   ├── MetadataIntegrityService - Validación SHA-256 de metadata
│   └── WebhookRateLimiter - Rate limiting con Redis para webhooks
├── codeflowx.govern.integration.monitoring/ # Monitoreo de Integraciones
│   └── ExternalPlatformSyncService - Métricas Micrometer para sync de plataformas
├── codeflowx.govern.integration.retry/     # Retry con Backoff Exponencial
│   └── RetryConfig - RetryTemplates para Databricks, Snowflake, Azure ML
├── codeflowx.web.zkoss/              # Framework ZKoss para UI
│   ├── BaseFront - Clase base para ViewModels
│   ├── Componentes UI reutilizables
│   └── Integración con EnArt Framework
├── codeflowx-aios-api/               # API REST AI OS (puerto 8080)
├── codeflowx-governance-api/         # API REST Governance
├── codeflowx-aios-telemetry/         # Microservicio telemetría (puerto 8096) - Recibe eventos HTTP → RabbitMQ
├── codeflowx-aios-telemetry-worker/  # Worker telemetría - Analiza tiempo real → TimescaleDB → Dispara BPMN
├── codeflowx-governance-documentation/ # Generación documentos (puerto 8085)
├── codeflowx.govern.nocode.client/   # Cliente Java unificado (WebClient reactivo)
└── codeflowx-discovery-serving/      # Discovery y serving

git/ (Microservicios Python FastAPI):
├── leka-bias-detection-service/      # Puerto 8001
├── leka-llm-evaluation/              # Puerto 8002
├── leka-prompt-governance/           # Puerto 8003
├── leka-rag-evaluation/              # Puerto 8004
├── leka-agent-monitoring/            # Puerto 8005
├── leka-model-wrapper/               # Puerto 8006
├── leka-adversarial-robustness/      # Puerto 8007
├── leka-conformity-assessment/       # Puerto 8009
├── leka-eu-declaration-generator/    # Puerto 8010
├── leka-llm-interpreter/             # Puerto 8011
├── leka-fria-generator/              # Puerto 8012
├── leka-ethics-risk-assessor/        # Puerto 8010
├── leka-board-governance-calculator/ # Puerto 8061
├── leka-multi-framework-compliance-aggregator/ # Puerto 8060
├── leka-copyright-compliance/
├── leka-deepfake-detect/
├── leka-iso42001-annex-a-assessor/
├── leka-serving-wrapper/             # Puerto 8000 (Gateway)
├── leka-orchestrator/
├── leka-data-lineage/
├── leka-explainability/
├── leka-sustainability-metrics/
├── leka-compliance-reporter/
└── leka-customers-portal/

git/ (Servidores de Inferencia):
├── leka-server-serving/              # Servidor principal
├── leka-server-serving-audio/       # Audio
├── leka-server-serving-documents/    # Documentos
├── leka-server-serving-evaluation/   # Evaluación
├── leka-server-serving-providers/   # Proveedores
├── leka-server-serving-rag/         # RAG
├── leka-server-serving-text/        # Texto
├── leka-server-serving-training/     # Entrenamiento
├── leka-server-serving-vision/       # Visión
└── leka-server-serving-wrapper/      # Wrapper unificado

git/ (Servidores de Infraestructura):
├── leka-server/                      # Servidor principal
├── leka-server-agents/               # Agentes
├── leka-server-audio/                # Audio
├── leka-server-connectors/           # Conectores
├── leka-server-core/                 # Core
├── leka-server-deployments/          # Despliegues
├── leka-server-documents/            # Documentos
├── leka-server-endpoint/             # Endpoint principal
├── leka-server-endpoint-evaluation/  # Endpoint evaluación
├── leka-server-endpoint-training/    # Endpoint entrenamiento
├── leka-server-generator/            # Generador
├── leka-server-governance/           # Gobernanza
├── leka-server-metrics/              # Métricas
├── leka-server-model-loader/         # Cargador modelos
├── leka-server-orchestrator/         # Orquestador
├── leka-server-plugins/              # Plugins
├── leka-server-prompts/              # Prompts
├── leka-server-rag/                  # RAG
├── leka-server-registry/             # Registro
├── leka-server-tools/                # Herramientas
├── leka-server-training/             # Entrenamiento
├── leka-server-vision/               # Visión
├── leka-server-workflows/            # Workflows
└── leka-server-wrapper/              # Wrapper
```

**Arquitectura:**
- **Hexagonal:** Puertos y adaptadores desacoplados
- **Modular:** Separación clara por responsabilidad (24 módulos Maven)
- **Microservicios:** 20+ microservicios Python (FastAPI) + 8+ microservicios Java (Spring Boot)
- **Event-Driven:** RabbitMQ para comunicación asíncrona
- **Gateway:** Spring Cloud Gateway (puerto 8000) como punto de entrada unificado
- **Cliente Unificado:** Cliente Java reactivo (WebClient) con clientes especializados por microservicio
- **Total Endpoints:** 199+ endpoints REST mapeados y documentados
- **Persistencia:**
  - **BusinessService:** Capa de persistencia genérica (CRUD) para todas las entidades
  - **Repositorios:** Spring Data JPA reutilizables por dominio
  - **Framework:** EnArt (NO Hibernate JPA estándar)
- **Framework FaaS (Framework as a Software):**
  - **51 Verticales Preconfigurados:** Frameworks sectoriales completos listos para usar
  - **Componentes por Vertical:**
    - **Workflows BPMN:** 5+ procesos estándar (compliance, incidentes, conformidad, FRIA, registro EU) + procesos específicos
    - **Reglas Drools:** Base común + reglas específicas por sector (ej: educación tiene 7+ reglas específicas)
    - **Microservicios:** Mandatorios (conformity-assessment, fria-generator, eu-declaration-generator, orchestrator) + opcionales
    - **Dashboards ZUL:** Pantallas sectoriales en `/console/gobierno/compliance/faas/<framework>-framework.zul`
    - **ViewModels:** `com.codeflowx.govern.viewmodel.faas.<framework>.<Framework>ViewModel`
    - **Métricas:** Catálogos JSON con KPIs sectoriales (`governance.kpi.<sector>.risk_index`, etc.)
    - **Reports:** Plantillas FRIA y documentos técnicos
  - **Arquitectura:** Catálogos + Configuraciones YAML + Políticas Drools + Metadatos JSONB
  - **Auto-carga:** Carga automática desde classpath al iniciar
  - **Extensibilidad:** Partners pueden crear frameworks sectoriales siguiendo el metamodelo
  - **Repositorios:** 51 repositorios Git (`codeflowx-faas-<sector>`) con documentación y extensiones
- **Integraciones:**
  - **Copilot:** Sincronización con Microsoft Copilot
  - **Seguridad:** Cifrado credenciales, integridad metadata, rate limiting
  - **Monitoreo:** Métricas Micrometer para integraciones
  - **Retry:** Backoff exponencial configurado por plataforma
- **UI Framework:**
  - **ZKoss:** Framework completo para ViewModels y componentes UI
  - **BaseFront:** Clase base con servicios comunes
- **Telemetría en Tiempo Real:**
  - **Microservicio REST:** Recibe eventos HTTP de agentes externos (IA, LLMs, agentes)
  - **Worker Asíncrono:** Analiza en tiempo real cada mensaje/respuesta de forma independiente
  - **Base de Datos Separada:** TimescaleDB (`codeflowx_telemetry`) para alta frecuencia
  - **Disparo Automático:** Si incumplen reglas, dispara procesos BPMN automáticamente

### ¿Existe un servicio centralizado de políticas, evidencias o supervisión?

**Sí, parcialmente implementado:**

1. **Policy Engine:**
   - **Drools Rules Engine:** Motor de reglas centralizado con 11 paquetes DRL (120+ reglas)
   - **AioPolicyEvaluationService:** Servicio de evaluación de políticas AI OS (en `codeflowx.govern.workflow.lib`)
   - **DroolsAioPolicyRulesEngine:** Adaptador Drools para políticas AI OS
   - **Policy Registry:** Metamodelos sectoriales con `policies.json` que enlazan políticas con paquetes Drools
   - **Integración Microservicios:** Políticas aplicadas via múltiples microservicios (leka-prompt-governance, leka-llm-evaluation, etc.)
   - **Estado:** ✅ Implementado para reglas de negocio, ⚠️ En desarrollo para políticas dinámicas AI OS

2. **Evidence Engine:**
   - **ImmutableLoggingBusinessService:** Sistema de logs inmutables con hash chains (Art. 19 EU AI Act)
   - **Tabla IMLIMMUTABLELOGS:** APPEND-ONLY con hash SHA-256 y hash chains
   - **Verificación de Integridad:** Métodos para detectar tampering
   - **Estado:** ✅ Implementado y funcional

3. **Supervisión y Telemetría en Tiempo Real:**
   - **PostMarketMonitoringService:** Monitoreo post-mercado (Art. 72)
   - **ComplianceDashboardService:** Dashboard de compliance
   - **AioTelemetryService:** Telemetría de componentes AI OS
   - **Sistema de Telemetría en Tiempo Real:**
     - **Microservicio:** `codeflowx-aios-telemetry` (puerto 8096) - Recibe eventos HTTP
     - **Worker:** `codeflowx-aios-telemetry-worker` - Analiza en tiempo real mensajes y respuestas
       - **Análisis Independiente:** Cada tipo de evento (IA, agentes, LLMs) se evalúa de forma independiente
       - **Evaluaciones:**
         - Bias detection (sesgo en respuestas)
         - Toxicity detection (contenido tóxico)
         - PII detection (información personal)
         - Secret detection (API keys, passwords)
         - Data leakage detection (fuga de datos)
         - Compliance check (cumplimiento)
       - **Almacenamiento:** Base de datos separada `codeflowx_telemetry` (TimescaleDB)
       - **Disparo Automático:** Si incumplen reglas, dispara procesos BPMN automáticamente
   - **Microservicios de Monitoreo:**
     - `leka-agent-monitoring` (puerto 8005) - Monitoreo de agentes con 20 endpoints
     - `leka-server-metrics` - Servidor de métricas
     - `leka-server-serving-wrapper` (puerto 8000) - 52 endpoints de métricas y monitoreo
   - **Estado:** ✅ Implementado completamente

### ¿Cómo es la arquitectura para onboarding de sistemas IA (modelos, agentes, RAG, copilotos)?

**Arquitectura de Onboarding:**

1. **Proceso BPMN:** `ai-component-onboarding-v1.bpmn`
   - Flujo automatizado de registro y aprobación
   - Integración con procesos de governance existentes

2. **Entidad AI OS:** `AioComponent` (tabla `AIOCOMPONENTS`)
   - Registro maestro de activos IA (MODEL, AGENT, PROMPT, DATASET, PIPELINE, SERVICE)
   - Estados: DRAFT, IN_REVIEW, APPROVED, RETIRED
   - Metadata completa (workspace, owner, risk level, compliance tags)

3. **API REST:** `POST /api/v1/aios/components`
   - Registro programático de componentes
   - Dispara procesos BPMN automáticamente
   - Integración con ImmutableLog

4. **Integración con Entidades Existentes:**
   - `Model` → `AioComponent` (type=MODEL)
   - `Agent` → `AioComponent` (type=AGENT)
   - `FriaAssessment` → Vinculado con `AioComponent`
   - `ComplianceAssessment` → Vinculado con `AioComponent`

5. **Workflow de Aprobación:**
   - Procesos BPMN existentes (`agent-approval-v1.bpmn`, `model-approval-v1.bpmn`) actualizados para usar AI OS
   - Delegates AI OS (`aioComplianceAggregatorDelegate`, `aioPolicyEnforcementDelegate`) integrados

**Estado:** ✅ Implementado parcialmente (PROMPT_002 en desarrollo)

### ¿Existe separación clara entre framework de gobernanza, módulos de análisis, capas de integración y capa API?

**Sí, separación clara implementada:**

1. **Framework de Gobernanza:**
   - **Ubicación:** `codeflowx.govern.business/` + `codeflowx.govern.workflow.lib/`
   - **Responsabilidad:** Lógica de negocio, procesos BPMN, reglas Drools
   - **Entidades:** 489 entidades JPA en `nocode.service.entitys/`
   - **Persistencia:** `codeflowx.nocode.persist.BusinessService` (capa genérica CRUD)
   - **Repositorios:** `nocode.service.repository` (Spring Data JPA reutilizables)
   - **Framework FaaS:** `codeflowx.govern.faas` (metamodelos sectoriales)

2. **Módulos de Análisis:**
   - **Ubicación:** Microservicios Python (puertos 8001-8012)
   - **Responsabilidad:** Evaluaciones ML/LLM (bias, toxicity, hallucination, RAG quality)
   - **Integración:** Vía REST desde BusinessServices Java usando `codeflowx.govern.nocode.client`

3. **Capas de Integración:**
   - **Conectores Externos:**
     - `codeflowx.govern.business/integrations/` - Conectores ML (Azure ML, SageMaker)
     - `codeflowx.govern.integration.copilot/` - Integración Microsoft Copilot
   - **Seguridad:**
     - `codeflowx.govern.integration.security/` - Cifrado credenciales, integridad metadata, rate limiting
   - **Monitoreo:**
     - `codeflowx.govern.integration.monitoring/` - Métricas Micrometer para integraciones
   - **Retry:**
     - `codeflowx.govern.integration.retry/` - Retry con backoff exponencial
   - **Webhooks:** Sistema de webhooks con HMAC (PROMPTS_16)
   - **MCP Adapter:** Adaptador MCP para VSCode/Open Interpreter (PROMPTS_17, en diseño)

4. **Capa API:**
   - **API REST Principal:** `codeflowx-governance-api/`
   - **API AI OS:** `codeflowx-aios-api/` (puerto 8080, OpenAPI/Swagger)
   - **API BPMN:** `codeflowx.govern.bpmn` (puerto 8095) - Gestión de procesos BPMN
   - **Endpoints:** RESTful con autenticación API Key (`X-Codeflowx-Key`)
   - **Documentación:** OpenAPI YAML para generación de SDKs

5. **Capa UI:**
   - **Framework ZKoss:** `codeflowx.web.zkoss` - Framework completo para ViewModels y componentes UI
   - **BaseFront:** Clase base con servicios comunes (BusinessService, Context, Environment)

**Estado:** ✅ Separación clara implementada con 24 módulos Maven organizados

---

## 2. INVENTARIO Y GOBIERNO DE SISTEMAS IA

### ¿Qué tipos de activos de IA detecta o registra el sistema hoy?

**Tipos de Activos Registrados:**

1. **Modelos (MODEL):**
   - LLMs (GPT-4, Claude, Gemini, Mistral)
   - Embeddings
   - Clasificadores
   - Modelos fine-tuned
   - **Entidad:** `Model` (tabla `MODMODELS`)

2. **Agentes (AGENT):**
   - Agentes autónomos
   - Asistentes conversacionales
   - Agentes de orquestación
   - **Entidad:** `Agent` (tabla `AGTAGENTS`)

3. **Prompts (PROMPT):**
   - Plantillas certificadas
   - Prompts de gobernanza
   - Templates de evaluación
   - **Entidad:** `Prompt` (tabla `PRMPROMPTS`)

4. **Datasets (DATASET):**
   - Datos de entrenamiento
   - Datos de evaluación
   - Ground truth
   - **Entidad:** `Dataset` (tabla `DATDATASETS`)

5. **Sistemas RAG (PIPELINE):**
   - Pipelines RAG completos
   - ETL de datos
   - Pipelines de evaluación batch
   - **Entidad:** `RagSystem` (tabla `RAGRAGSYSTEMS`)

6. **Microservicios AI (SERVICE):**
   - Servicios de evaluación
   - Wrappers de modelos
   - APIs de interpretabilidad
   - **Registro:** Via `AioComponent` (type=SERVICE)

**Estado:** ✅ Todos los tipos registrados

### ¿Cómo se clasifican los modelos internos y externos?

**Clasificación Implementada:**

1. **Por Origen:**
   - **Interno:** Modelos propios entrenados/fine-tuned
   - **Externo:** Modelos de proveedores (OpenAI, Anthropic, Google, Mistral)
   - **Campo:** `Model.modprovider` (PROVIDER_INTERNAL, PROVIDER_OPENAI, etc.)

2. **Por Tipo:**
   - **LLM:** Large Language Models
   - **Embedding:** Modelos de embeddings
   - **Classifier:** Clasificadores
   - **Campo:** `Model.modtype`

3. **Por Riesgo (EU AI Act):**
   - **HIGH_RISK:** Sistemas de alto riesgo (Anexo III)
   - **LIMITED_RISK:** Riesgo limitado (Art. 52)
   - **MINIMAL_RISK:** Riesgo mínimo
   - **Campo:** `Model.modrisklevel` + `AnnexIIICategory`

4. **Por Estado:**
   - **DRAFT, IN_REVIEW, APPROVED, RETIRED** (en `AioComponent`)

**Estado:** ✅ Clasificación completa implementada

### ¿Cómo se gestionan agentes, memorias, prompts, RAG y copilotos?

**Gestión Implementada:**

1. **Agentes:**
   - **Entidad:** `Agent` (tabla `AGTAGENTS`)
   - **Proceso BPMN:** `agent-approval-v1.bpmn`
   - **Monitoreo:** `leka-agent-monitoring` (puerto 8005) - 20 endpoints completos
   - **Capacidades:**
     - Detección de loops, análisis multi-agente, evaluación de herramientas
     - Immutable logging (Art. 19), verificación de integridad de logs
     - Análisis de intervenciones humanas (Art. 14)
     - A/B testing, recopilación de feedback, análisis de feedback batch
   - **Servidores:** `leka-server-agents` - Servidor especializado de agentes

2. **Memorias:**
   - **N/A:** No existe gestión explícita de memorias de agentes
   - **Planificado:** En diseño para AI OS Runtime (PROMPT_002)

3. **Prompts:**
   - **Entidad:** `Prompt` (tabla `PRMPROMPTS`)
   - **Proceso BPMN:** `prompt-approval-v1.bpmn`
   - **Evaluación:** `leka-prompt-governance` (puerto 8003) - 14 endpoints
   - **Capacidades:**
     - Safety evaluation (injection, jailbreak, PII leakage)
     - Effectiveness evaluation (claridad, especificidad, calidad)
     - Version comparison, cost analysis
     - Template validation, context optimization, few-shot evaluation
     - Complexity analysis (Art. 13), prompt drift detection (Art. 15.4)
   - **Servidores:** `leka-server-prompts` - Servidor especializado de prompts
   - **Versionado:** SemVer implementado

4. **RAG:**
   - **Entidad:** `RagSystem` (tabla `RAGRAGSYSTEMS`)
   - **Evaluación:** `leka-rag-evaluation` (puerto 8004) - 24 endpoints
   - **Capacidades:**
     - Retrieval evaluation, answer quality, context relevance
     - Full pipeline evaluation, index quality, document quality
     - Conversation context, citations, A/B testing, benchmarking
     - Learning from evaluation, optimal thresholds, policy validation
     - Grounding validation, user feedback, validation queue
     - Bias mitigation, chunking optimization
   - **Métricas:** RAGAS, ARES, benchmarking BEIR/MTEB (implementado)
   - **Proceso BPMN:** `rag-continuous-improvement-v1.bpmn` (mejora continua)
   - **Servidores:**
     - `leka-server-rag` - Servidor RAG
     - `leka-server-serving-rag` - Servidor de serving RAG

5. **Copilotos:**
   - **N/A:** No existe gestión específica de copilotos
   - **Planificado:** Via `AioComponent` (type=AGENT) con metadata específica
   - **Servidores:** `leka-server-agents` podría gestionar copilotos

**Estado:** ✅ Agentes, Prompts y RAG completamente gestionados con microservicios especializados, ⚠️ Memorias y Copilotos en diseño

### ¿Hay detección automática o es declaración manual?

**Modo Mixto:**

1. **Detección Automática:**
   - **Conectores Externos:** Azure ML, SageMaker (implementado, INC-010-015)
   - **Webhooks:** Sistemas externos notifican nuevos modelos/agentes via `POST /api/v1/governance/events`
   - **Sincronización:** Databricks MLflow (planificado, PROMPTS_12)
   - **Servidores de Registro:** `leka-server-registry` - Registro automático de componentes
   - **Estado:** ⚠️ Parcialmente implementado (conectores ML implementados, Databricks pendiente)

2. **Declaración Manual:**
   - **API REST:** `POST /api/v1/aios/components` (registro manual)
   - **UI ZKoss:** ViewModels CRUD para registro manual (410 ViewModels)
   - **Cliente Java:** `codeflowx.govern.nocode.client` - Cliente unificado con métodos especializados
   - **SDK:** Cliente Java implementado, SDKs Python/TypeScript (planificados, PROMPTS_15)
   - **Estado:** ✅ Implementado

**Estado:** ⚠️ Principalmente manual, automatización parcial (conectores ML implementados)

### ¿Existe un motor de metadatos unificado? (tipo "Model Registry")

**Sí, parcialmente implementado:**

1. **AioComponent (AI OS Registry):**
   - **Tabla:** `AIOCOMPONENTS`
   - **Responsabilidad:** Registro maestro unificado de todos los activos IA
   - **Metadata:** Completo (workspace, owner, risk level, compliance tags, capabilities)
   - **Estado:** ✅ Implementado

2. **Entidades Específicas:**
   - `Model` (MODMODELS) - Registro de modelos
   - `Agent` (AGTAGENTS) - Registro de agentes
   - `Prompt` (PRMPROMPTS) - Registro de prompts
   - `Dataset` (DATDATASETS) - Registro de datasets
   - `RagSystem` (RAGRAGSYSTEMS) - Registro de sistemas RAG

3. **Marketplace Interno:**
   - **AioMarketplaceEntry:** Catálogo interno para discovery y reutilización
   - **Proceso BPMN:** `ai-marketplace-publish-v1.bpmn`
   - **Estado:** ✅ Implementado

**Estado:** ✅ Motor de metadatos unificado implementado (AioComponent + entidades específicas)

---

## 3. TRAZABILIDAD Y EVIDENCIAS

### ¿Cómo se generan evidencias?

**Generación de Evidencias:**

1. **Automática:**
   - **ImmutableLoggingBusinessService:** Genera logs inmutables automáticamente para eventos críticos
   - **Procesos BPMN:** Cada paso del workflow genera entrada en ImmutableLog
   - **Delegates:** `aioComplianceAggregatorDelegate`, `aioTelemetryCollectorDelegate` escriben en ImmutableLog
   - **Estado:** ✅ Implementado

2. **Manual:**
   - **API REST:** `POST /api/v1/aios/audit/logs` permite registro manual de operaciones
   - **UI:** ViewModels para registro manual de evidencias
   - **Estado:** ✅ Implementado

3. **Evidencias Específicas:**
   - **FRIA:** `FriaAssessment` genera documentación completa (Art. 27)
   - **Anexo IV:** Generación automática de documentación técnica (microservicio documentación)
   - **Compliance Assessment:** `ComplianceAssessment` genera evidencias de cumplimiento
   - **Estado:** ✅ Implementado

### ¿Existe un sistema de logs inmutables? ¿Cómo funciona?

**Sí, completamente implementado:**

1. **Tabla IMLIMMUTABLELOGS:**
   - **APPEND-ONLY:** Trigger previene UPDATE/DELETE
   - **Hash Chain:** Cada log incluye hash SHA-256 del log anterior
   - **Hash Calculation:** `SHA-256(previousHash + timestampEpoch + entityType + entityId + action + userId + data)`
   - **Primer Log:** Usa hash inicial "0000...0000" (64 ceros)

2. **ImmutableLoggingBusinessService:**
   - **Métodos:**
     - `createLogEntry()`: Crea log inmutable con hash
     - `verifyIntegrity()`: Verifica integridad de hash chain
     - `getEntityLogsWithVerification()`: Obtiene logs con verificación automática
     - `detectTampering()`: Detecta manipulación y genera alertas (INC-012)

3. **Características:**
   - **Hash SHA-256:** Integridad criptográfica
   - **Hash Chain:** Blockchain-style para detectar modificaciones
   - **Verificación Automática:** Métodos para validar cadena completa
   - **Detección de Tampering:** Alertas automáticas cuando se detecta manipulación
   - **Retención:** Configurable (mínimo 6 meses según Art. 19)

4. **Integración:**
   - **Todos los ViewModels:** Usan ImmutableLoggingBusinessService
   - **Procesos BPMN:** Escriben en ImmutableLog automáticamente
   - **API REST:** Endpoint para registro externo

**Estado:** ✅ Sistema completo e implementado (Art. 19 EU AI Act)

### ¿Qué elementos se trazan hoy: prompts, outputs, acciones, modelos, usuarios?

**Elementos Trazados:**

1. **Prompts:**
   - ✅ Registro en `PRMPROMPTS`
   - ✅ Logs inmutables de creación/modificación
   - ✅ Evaluaciones de prompts (leka-prompt-evaluation)
   - ⚠️ No se trazan todos los prompts ejecutados en tiempo real (solo eventos críticos)

2. **Outputs:**
   - ✅ Outputs de evaluaciones (bias, toxicity, hallucination)
   - ✅ Resultados de modelos en evaluaciones batch
   - ⚠️ No se trazan todos los outputs de producción (solo eventos de gobernanza)

3. **Acciones:**
   - ✅ Todas las acciones de gobernanza (CREATE, UPDATE, APPROVE, REJECT)
   - ✅ Acciones de agentes (via telemetría)
   - ✅ Acciones de usuarios (via ImmutableLog)
   - **Estado:** ✅ Implementado

4. **Modelos:**
   - ✅ Registro completo en `MODMODELS` y `AIOCOMPONENTS`
   - ✅ Historial de versiones
   - ✅ Evaluaciones y métricas
   - ✅ Logs inmutables de cambios
   - **Estado:** ✅ Implementado

5. **Usuarios:**
   - ✅ Todas las acciones de usuarios en ImmutableLog
   - ✅ Auditoría de acceso (tabla `CORAUDITLOG`)
   - ✅ Roles y permisos
   - **Estado:** ✅ Implementado

**Estado:** ✅ Trazabilidad completa de elementos críticos, ⚠️ No todos los prompts/outputs en tiempo real (solo eventos de gobernanza)

### ¿Hay un motor dedicado de evidencias o está integrado en otro módulo?

**Integrado en módulo de gobernanza:**

1. **ImmutableLoggingBusinessService:**
   - **Ubicación:** `codeflowx.govern.business/logging/`
   - **Responsabilidad:** Motor de evidencias inmutables
   - **Integración:** Usado por todos los módulos (no es independiente)

2. **Evidencias Específicas:**
   - **FRIA:** `FriaAssessmentBusinessService` genera evidencias FRIA
   - **Compliance:** `ComplianceAssessmentService` genera evidencias de cumplimiento
   - **Documentación:** Microservicio documentación genera Anexo IV

**Estado:** ⚠️ Integrado (no es componente independiente)

### ¿Existe un "Evidence Engine" como componente independiente?

**No, no existe como componente independiente:**

- **Estado Actual:** Evidencias generadas por servicios específicos (ImmutableLoggingBusinessService, FriaAssessmentBusinessService, etc.)
- **Planificado:** No está planificado como componente independiente
- **Arquitectura Actual:** Evidencias integradas en módulos de gobernanza

**Estado:** ❌ No existe como componente independiente

---

## 4. CONTROL DE AGENTES Y AUTONOMÍA

### ¿Puede CodeflowX OS interceptar acciones de agentes?

**Parcialmente implementado:**

1. **Agentes Externos:**
   - **Telemetría:** Agentes externos envían eventos via `POST /api/v1/aios/telemetry/events`
   - **Gobernanza:** `POST /api/v1/governance/events` para eventos de gobernanza
   - **Interceptación:** ⚠️ No interceptación directa, requiere que agentes reporten voluntariamente

2. **Agentes Internos (AI OS Runtime):**
   - **AioDeploymentService:** Gestiona despliegues de agentes
   - **AioTelemetryService:** Recopila telemetría de agentes
   - **Interceptación:** ⚠️ En desarrollo (PROMPT_002)

3. **Proxies/Interceptores:**
   - **N/A:** No existe proxy/interceptor que intercepte automáticamente todas las acciones
   - **Planificado:** No está planificado actualmente

**Estado:** ⚠️ Parcial (requiere reporte voluntario de agentes externos)

### ¿Gobierna memorias, contextos y aprendizaje implícito?

**No implementado:**

1. **Memorias:**
   - **N/A:** No existe gestión de memorias de agentes
   - **Planificado:** En diseño para AI OS Runtime (PROMPT_002)

2. **Contextos:**
   - **RAG:** Gestión de contextos en sistemas RAG (via `RagSystem`)
   - **Agentes:** ⚠️ No existe gestión explícita de contextos de agentes
   - **Planificado:** En diseño

3. **Aprendizaje Implícito:**
   - **N/A:** No existe gestión de aprendizaje implícito
   - **Planificado:** No está planificado actualmente

**Estado:** ❌ No implementado (en diseño)

### ¿Detecta loops autónomos o acciones no supervisadas?

**Sí, parcialmente implementado:**

1. **Detección de Loops:**
   - **Microservicio:** `leka-agent-monitoring` (puerto 8005)
   - **Endpoint:** `POST /api/v1/aios/agent/monitor` con `mode=detect-loops`
   - **Estado:** ✅ Implementado

2. **Acciones No Supervisadas:**
   - **Telemetría:** Análisis de patrones de comportamiento
   - **Alertas:** Generación de alertas cuando se detectan acciones sospechosas
   - **Estado:** ✅ Implementado parcialmente

**Estado:** ✅ Detección de loops implementada, ⚠️ Acciones no supervisadas parcialmente

### ¿Puede bloquear acciones automáticamente según políticas?

**Sí, parcialmente implementado:**

1. **Procesos BPMN:**
   - **Aprobación:** Procesos de aprobación pueden rechazar/bloquear automáticamente
   - **Drools:** Reglas Drools pueden decidir AUTO_REJECT
   - **Estado:** ✅ Implementado

2. **Runtime Bloqueo:**
   - **AioPolicyEnforcementDelegate:** Aplica políticas en tiempo de ejecución
   - **Deployment Actions:** `POST /api/v1/aios/runtime/deployments/{uuid}/actions` permite PAUSE/TERMINATE
   - **Estado:** ✅ Implementado parcialmente

3. **Bloqueo Automático:**
   - **Alertas:** Sistema de alertas puede disparar acciones automáticas
   - **Circuit Breakers:** ⚠️ No implementado explícitamente
   - **Kill Switch:** ⚠️ No implementado explícitamente

**Estado:** ✅ Bloqueo via aprobaciones, ⚠️ Bloqueo runtime parcial

### ¿Existe un "Agent Supervisor"? ¿Cómo funciona?

**No existe como componente independiente:**

1. **Funcionalidad Distribuida:**
   - **AioTelemetryService:** Monitorea agentes
   - **leka-agent-monitoring:** Analiza comportamiento de agentes
   - **Procesos BPMN:** Gestionan ciclo de vida de agentes
   - **Estado:** ⚠️ Funcionalidad distribuida, no centralizada

2. **Planificado:**
   - **N/A:** No está planificado como componente "Agent Supervisor" independiente
   - **Arquitectura:** Supervisión distribuida en múltiples servicios

**Estado:** ❌ No existe como componente independiente (funcionalidad distribuida)

---

## 5. POLICY ENGINE (NUCLEO DE UN AI OS)

### ¿Existe hoy un motor de políticas centralizado?

**Sí, parcialmente implementado:**

1. **Drools Rules Engine:**
   - **Ubicación:** `codeflowx.govern.workflow.lib/`
   - **Responsabilidad:** Motor de reglas centralizado
   - **Paquetes:** 11 paquetes DRL con 120+ reglas
   - **Estado:** ✅ Implementado

2. **AioPolicyEvaluationService:**
   - **Ubicación:** `codeflowx.govern.workflow.lib/runtime/application/service/`
   - **Responsabilidad:** Evaluación de políticas AI OS
   - **Integración:** Drools + Policy Registry
   - **Estado:** ✅ Implementado

3. **Policy Registry:**
   - **Metamodelos Sectoriales:** `metamodel/policies.json` en frameworks FaaS
   - **Enlace:** Políticas con paquetes Drools y condiciones de activación
   - **Estado:** ✅ Implementado

**Estado:** ✅ Motor centralizado implementado (Drools + AioPolicyEvaluationService)

### ¿Qué tipos de políticas soporta (prompts, RAG, agentes, datos, modelos)?

**Tipos de Políticas Soportadas:**

1. **Modelos:**
   - ✅ Políticas de aprobación (model-approval-v1.bpmn)
   - ✅ Políticas de riesgo (Annex III classification)
   - ✅ Políticas de compliance (EU AI Act)
   - **Estado:** ✅ Implementado

2. **Agentes:**
   - ✅ Políticas de aprobación (agent-approval-v1.bpmn)
   - ✅ Políticas de monitoreo (leka-agent-monitoring)
   - ✅ Políticas de seguridad (detección de loops, violaciones)
   - **Estado:** ✅ Implementado

3. **Prompts:**
   - ✅ Políticas de aprobación (prompt-approval-v1.bpmn)
   - ✅ Políticas de seguridad (detección de prompt injection)
   - ✅ Políticas de calidad (evaluación de efectividad)
   - **Estado:** ✅ Implementado

4. **RAG:**
   - ✅ Políticas de calidad (evaluación RAG)
   - ✅ Políticas de mejora continua (rag-continuous-improvement-v1.bpmn)
   - **Estado:** ✅ Implementado

5. **Datos:**
   - ✅ Políticas de calidad de datasets (Art. 10)
   - ✅ Políticas de integridad (hash SHA-256)
   - ✅ Políticas de sesgo (bias detection)
   - **Estado:** ✅ Implementado

**Estado:** ✅ Todos los tipos soportados

### ¿Se pueden aplicar políticas dinámicas por contexto, riesgo o usuario?

**Sí, parcialmente implementado:**

1. **Por Contexto:**
   - **Workspace:** Políticas por workspace (`AioWorkspace`)
   - **Proyecto:** Políticas por proyecto (`PRJPROJECTS`)
   - **Estado:** ✅ Implementado

2. **Por Riesgo:**
   - **Risk Level:** Políticas diferentes según `risk_level` (HIGH_RISK, LIMITED_RISK, MINIMAL_RISK)
   - **Annex III:** Políticas específicas por categoría Annex III
   - **Estado:** ✅ Implementado

3. **Por Usuario:**
   - **Roles:** Políticas aplicadas según roles (AIOS_ADMIN, AIOS_OPERATOR, AIOS_CONSUMER)
   - **Permisos:** Control de acceso basado en roles (INC-011-01)
   - **Estado:** ✅ Implementado

4. **Dinámicas:**
   - **Drools:** Reglas modificables sin redeploy
   - **Policy Registry:** Políticas cargadas dinámicamente desde metamodelos
   - **Estado:** ✅ Implementado

**Estado:** ✅ Políticas dinámicas implementadas

### ¿Puede ejecutar políticas en tiempo real durante el ciclo de IA?

**Sí, parcialmente implementado:**

1. **Tiempo Real:**
   - **AioPolicyEnforcementDelegate:** Aplica políticas durante ejecución BPMN
   - **Telemetría:** Análisis en tiempo real de eventos de telemetría
   - **Estado:** ✅ Implementado parcialmente

2. **Durante Ciclo de IA:**
   - **Onboarding:** Políticas aplicadas durante registro (`ai-component-onboarding-v1.bpmn`)
   - **Evaluación:** Políticas aplicadas durante evaluaciones
   - **Despliegue:** Políticas aplicadas antes de despliegue
   - **Estado:** ✅ Implementado

3. **Runtime:**
   - **AioPolicyEnforcementDelegate:** Aplica políticas en runtime
   - **Interceptación:** ⚠️ No intercepta todas las acciones en tiempo real (requiere reporte voluntario)
   - **Estado:** ⚠️ Parcial

**Estado:** ✅ Políticas en tiempo real parcialmente implementadas

### ¿Existe un "Policy Runtime"? ¿En qué estado está?

**Sí, en desarrollo:**

1. **AioPolicyEvaluationService:**
   - **Ubicación:** `codeflowx.govern.workflow.lib/runtime/application/service/`
   - **Responsabilidad:** Runtime de evaluación de políticas
   - **Estado:** ✅ Implementado

2. **AioPolicyEnforcementDelegate:**
   - **Ubicación:** `codeflowx.govern.workflow.lib/runtime/infrastructure/bpmn/`
   - **Responsabilidad:** Aplicación de políticas en runtime
   - **Estado:** ✅ Implementado

3. **DroolsAioPolicyRulesEngine:**
   - **Ubicación:** `codeflowx.govern.workflow.lib/runtime/infrastructure/drools/`
   - **Responsabilidad:** Motor de reglas para políticas
   - **Estado:** ✅ Implementado

**Estado:** ✅ Policy Runtime implementado (en `codeflowx.govern.workflow.lib/runtime/`)

---

## 6. INTEGRACIONES Y CONECTORES

### ¿Qué conectores existen hoy (M365, Azure OpenAI, Anthropic, n8n, Salesforce, SAP...)?

**Conectores Implementados:**

1. **Azure OpenAI:**
   - ✅ Integración via `leka-model-wrapper` (puerto 8006)
   - ✅ Soporte para GPT-4, GPT-3.5, embeddings
   - **Estado:** ✅ Implementado

2. **Anthropic:**
   - ✅ Integración via `leka-model-wrapper` (puerto 8006)
   - ✅ Soporte para Claude
   - **Estado:** ✅ Implementado

3. **Google (Gemini):**
   - ✅ Integración via `leka-model-wrapper` (puerto 8006)
   - **Estado:** ✅ Implementado

4. **Mistral:**
   - ✅ Integración via `leka-model-wrapper` (puerto 8006)
   - **Estado:** ✅ Implementado

5. **n8n / Zapier / Make:**
   - ✅ Integración via REST API (`POST /api/v1/governance/events`)
   - ✅ Webhooks para notificaciones
   - **Estado:** ✅ Implementado

6. **Azure ML:**
   - ✅ Conector implementado (INC-010-015)
   - ✅ `AzureMlConnector` adaptador
   - **Estado:** ✅ Implementado

7. **SageMaker:**
   - ✅ Conector implementado (INC-010-015)
   - ✅ `SageMakerConnector` adaptador
   - **Estado:** ✅ Implementado

8. **M365, Salesforce, SAP:**
   - ❌ No implementados
   - **Planificado:** No está planificado actualmente

**Estado:** ✅ Conectores principales implementados (LLMs, n8n, Azure ML, SageMaker)

### ¿Cómo se gobierna el uso de esos conectores desde CodeflowX OS?

**Gobierno de Conectores:**

1. **Registro:**
   - **AioComponent:** Conectores registrados como componentes (type=SERVICE)
   - **Metadata:** Información completa de conectores
   - **Estado:** ✅ Implementado

2. **Políticas:**
   - **AioPolicyBinding:** Políticas aplicadas a conectores
   - **Drools:** Reglas de gobierno para uso de conectores
   - **Estado:** ✅ Implementado

3. **Monitoreo:**
   - **Telemetría:** Uso de conectores monitoreado via telemetría
   - **Logs:** Registro en ImmutableLog
   - **Estado:** ✅ Implementado

4. **Control:**
   - **Aprobación:** Conectores requieren aprobación antes de uso
   - **Despliegue:** Control de despliegue via `AioDeploymentService`
   - **Estado:** ✅ Implementado

**Estado:** ✅ Gobierno de conectores implementado

### ¿Existe un "Governed Connector Framework"?

**Sí, parcialmente implementado:**

1. **Interfaz Común:**
   - **ExternalMlPlatformConnector:** Interfaz común para conectores ML (INC-010-015)
   - **Adaptadores:** `AzureMlConnector`, `SageMakerConnector`
   - **Estado:** ✅ Implementado

2. **Configuración:**
   - **ConnectorConfigurationService:** Gestión de configuración de conectores
   - **Validación:** Validación de configuración
   - **Estado:** ✅ Implementado

3. **Framework Completo:**
   - ⚠️ No existe framework completo para todos los tipos de conectores
   - **Planificado:** PROMPTS_12 para conectores enterprise (Databricks, Snowflake)

**Estado:** ⚠️ Framework parcial (solo ML platforms), no completo

### ¿Hay planes para conectores premium o marketplace?

**Sí, planificado:**

1. **Marketplace Interno:**
   - **AioMarketplaceEntry:** Catálogo interno de componentes
   - **Proceso BPMN:** `ai-marketplace-publish-v1.bpmn`
   - **Estado:** ✅ Implementado

2. **Marketplace Premium:**
   - **Planificado:** Marketplace para conectores premium
   - **Estado:** ⚠️ En diseño

3. **Conectores Premium:**
   - **Planificado:** Conectores premium para M365, Salesforce, SAP
   - **Estado:** ⚠️ No planificado actualmente

**Estado:** ✅ Marketplace interno implementado, ⚠️ Premium en diseño

### ¿Cómo se versionan y trazan los conectores?

**Versionado y Trazabilidad:**

1. **Versionado:**
   - **SemVer:** Versionado SemVer para componentes (INC-011-04)
   - **AioComponent:** Campo `version` en componentes
   - **Estado:** ✅ Implementado

2. **Trazabilidad:**
   - **ImmutableLog:** Logs inmutables de cambios en conectores
   - **Historial:** Historial de versiones de conectores
   - **Estado:** ✅ Implementado

3. **Auditoría:**
   - **AuditLog:** Registro de uso de conectores
   - **Telemetría:** Métricas de uso
   - **Estado:** ✅ Implementado

**Estado:** ✅ Versionado y trazabilidad implementados

---

## 7. ORQUESTACIÓN Y AUTOMATIZACIÓN

### ¿Cómo funcionan los flujos de gobernanza hoy?

**Flujos de Gobernanza:**

1. **Procesos BPMN:**
   - **17+ Procesos:** Automatizados con Flowable 6.8.1
   - **User Tasks:** 20% Human-in-the-Loop (HITL)
   - **Service Tasks:** 80% Automatizados con IA
   - **Estado:** ✅ Implementado

2. **Flujos Principales:**
   - `agent-approval-v1.bpmn` - Aprobación de agentes
   - `model-approval-v1.bpmn` - Aprobación de modelos
   - `bias-detection-v1.bpmn` - Detección de sesgos
   - `compliance-monitoring-v1.bpmn` - Monitoreo de compliance
   - `ai-component-onboarding-v1.bpmn` - Onboarding AI OS
   - `ai-marketplace-publish-v1.bpmn` - Publicación en marketplace
   - `ai-policy-review-v1.bpmn` - Revisión de políticas
   - **Estado:** ✅ Implementado

3. **Orquestación:**
   - **Delegates:** 65+ JavaDelegates para lógica de negocio
   - **Drools:** 120+ reglas para decisiones automáticas
   - **Paralelismo:** ParallelGateway para evaluaciones paralelas
   - **Estado:** ✅ Implementado

**Estado:** ✅ Flujos de gobernanza completamente implementados

### ¿Existe motor de tareas? ¿Cómo opera?

**Sí, implementado:**

1. **Flowable BPMN Engine:**
   - **Motor:** Flowable 6.8.1 integrado con Spring Boot
   - **Tareas:** Service Tasks, User Tasks, Business Rule Tasks
   - **Timers:** Timer Boundary Events para SLA
   - **Estado:** ✅ Implementado

2. **TaskService:**
   - **Ubicación:** Flowable TaskService
   - **Responsabilidad:** Gestión de User Tasks
   - **Integración:** ViewModels ZKoss para User Tasks
   - **Estado:** ✅ Implementado

3. **Scheduled Tasks:**
   - **Spring @Scheduled:** Tareas programadas (ej: LogIntegrityScheduledTask)
   - **BPMN Timers:** Timers en procesos BPMN
   - **Estado:** ✅ Implementado

**Estado:** ✅ Motor de tareas implementado (Flowable + Spring Scheduled)

### ¿Hay un "Compliance Automation Engine"?

**Sí, implementado:**

1. **Procesos BPMN de Compliance:**
   - `compliance-monitoring-v1.bpmn` - Monitoreo automático
   - `bias-detection-v1.bpmn` - Detección automática de sesgos
   - `incident-notification-v1.bpmn` - Notificación automática de incidentes
   - **Estado:** ✅ Implementado

2. **Delegates de Compliance:**
   - `AIComplianceCheckDelegate` - Verificación de compliance
   - `aioComplianceAggregatorDelegate` - Agregación de compliance
   - `aioPolicyEnforcementDelegate` - Aplicación de políticas
   - **Estado:** ✅ Implementado

3. **Automatización:**
   - **Evaluaciones Automáticas:** Bias, toxicity, hallucination
   - **Alertas Automáticas:** Cuando se detectan problemas
   - **Reportes Automáticos:** Generación automática de reportes PMM
   - **Estado:** ✅ Implementado

**Estado:** ✅ Compliance Automation Engine implementado (via BPMN + Delegates)

### ¿El sistema ejecuta acciones automáticas basadas en eventos?

**Sí, implementado:**

1. **Eventos:**
   - **API REST:** `POST /api/v1/governance/events` para ingesta de eventos
   - **RabbitMQ:** Eventos publicados en colas para procesamiento asíncrono
   - **Webhooks:** Eventos externos disparan procesos BPMN
   - **Estado:** ✅ Implementado

2. **Acciones Automáticas:**
   - **Procesos BPMN:** Disparados automáticamente por eventos
   - **Delegates:** Ejecutan acciones automáticas (aprobación, rechazo, alertas)
   - **Drools:** Reglas disparan acciones automáticas
   - **Estado:** ✅ Implementado

3. **Ejemplos:**
   - Nuevo modelo → Proceso de aprobación automático
   - Detección de sesgo → Alerta automática
   - Violación de política → Bloqueo automático
   - **Estado:** ✅ Implementado

**Estado:** ✅ Acciones automáticas basadas en eventos implementadas

### ¿Qué nivel de automatización existe hoy (1–5)?

**Nivel 4 (Alto):**

**Justificación:**
- ✅ **80% de procesos automatizados** (Service Tasks con IA)
- ✅ **20% Human-in-the-Loop** (User Tasks para decisiones críticas)
- ✅ **Evaluaciones automáticas** (bias, toxicity, hallucination)
- ✅ **Alertas automáticas** (detección de problemas)
- ✅ **Reportes automáticos** (PMM, compliance)
- ⚠️ **Falta:** Interceptación automática de todas las acciones en tiempo real (requiere reporte voluntario)

**Estado:** ✅ Nivel 4 (Alto) - 80% automatizado

---

## 8. SDK, APIs Y EXTENSIBILIDAD

### ¿Existe un SDK disponible? ¿Qué capacidades tiene?

**Sí, parcialmente implementado:**

1. **Cliente Java (Implementado):**
   - **Ubicación:** `codeflowx.govern.nocode.client`
   - **Framework:** Spring WebFlux + WebClient (reactivo)
   - **Capacidades:**
     - Cliente unificado `AIGovernanceClient` con clientes especializados
     - **BiasDetectionClient:** 17 métodos (bias, drift, data quality, explainability, robustness, privacy, performance, uncertainty, features, model card, label leakage, benchmark fairness, DVC/Git LFS import)
     - **LLMEvaluationClient:** 18 métodos (hallucination, toxicity, bias, quality, prompt injection, instruction following, consistency, factual grounding, cost efficiency, benchmarking, A/B testing, sentiment analysis)
     - **PromptGovernanceClient:** 14 métodos (safety, effectiveness, PII leakage, version comparison, cost analysis, template validation, context optimization, few-shot evaluation, output format validation, complexity analysis, prompt drift)
     - **RAGEvaluationClient:** 24 métodos (retrieval, answer quality, context relevance, full pipeline, index quality, document quality, conversation context, citations, A/B testing, benchmarking, learning, thresholds, policy validation, grounding, user feedback, validation queue, bias mitigation, chunking optimization)
     - **AgentMonitoringClient:** 20 métodos (execution analysis, reliability, cost, loop detection, multi-agent orchestration, tool usage, safety violations, benchmarking, immutable logging, log integrity, human interventions, A/B testing, feedback collection)
     - **ModelWrapperClient:** 18 métodos (invoke, batch invoke, compare, list available, adaptation recommendation, adapter config validation, cost estimation, recommendations, benchmarking, smart routing, streaming, A/B testing, performance benchmarking, drift detection, usage analytics)
     - **AIInterpreterClient:** 8 métodos (explain result, answer question, executive summary, root cause analysis, chat, disclosure generation, stats)
     - **ConformityAssessmentClient:** 6 métodos (Annex VI assessment, checklist generation, evidence validation, template)
     - **EUDeclarationGeneratorClient:** 5 métodos (EU conformity declaration, validation, template, hash generation)
     - **FRIAGeneratorClient:** 7 métodos (FRIA assessment generation, PDF generation, fundamental rights analysis, DPIA integration, template, cross-validation)
     - **AdversarialRobustnessClient:** 5 métodos (model poisoning detection, evasion testing, attack simulation, policy validation)
     - **CopyrightComplianceClient:** 5 métodos (training data compliance, TDM opt-out detection, attribution generation, license validation)
     - **DeepfakeDetectionClient:** 6 métodos (image/video/audio deepfake detection, watermarking, disclosure generation)
     - **EthicsRiskAssessorClient:** 5 métodos (ethical risk assessment, Charter articles mapping, vulnerable groups identification)
     - **BoardGovernanceCalculatorClient:** 5 métodos (EDM metrics, executive summary, health score)
     - **MultiFrameworkComplianceAggregatorClient:** 7 métodos (overall compliance score, frameworks supported, compliance report export, certification roadmap)
     - **ISO42001AnnexAAssessorClient:** 5 métodos (Annex A assessment, control-specific assessment, gap closure plan, controls reference)
     - **ServingWrapperClient:** 52 métodos (chat, audio, image, usage management, pool management, providers management, plugins management, metrics)
   - **Total:** 199+ endpoints mapeados y disponibles
   - **Estado:** ✅ Implementado y funcional

2. **SDK Python:**
   - **Planificado:** PROMPTS_15 (API + SDKs)
   - **Estado:** ⚠️ En diseño

3. **SDK JavaScript/TypeScript:**
   - **Planificado:** PROMPTS_15 (API + SDKs)
   - **Estado:** ⚠️ En diseño

4. **SDK Java (Librería):**
   - **Ubicación:** `git/leka-java-sdk/`
   - **Estado:** ⚠️ Verificar implementación

**Estado:** ✅ Cliente Java completo implementado (199+ endpoints), ⚠️ SDKs Python/TypeScript planificados

### ¿Hay API pública o privada? ¿Qué cubre?

**Sí, API REST implementada:**

1. **API AI OS:**
   - **Ubicación:** `codeflowx-aios-api/`
   - **Documentación:** OpenAPI YAML (`aios-api.yaml`)
   - **Endpoints:** 20+ endpoints REST
   - **Autenticación:** API Key (`X-Codeflowx-Key`)
   - **Estado:** ✅ Implementado

2. **API Governance:**
   - **Ubicación:** `codeflowx-governance-api/`
   - **Endpoints:** Endpoints de gobernanza
   - **Estado:** ✅ Implementado

3. **Cobertura:**
   - Gestión de componentes (`/api/v1/aios/components`)
   - Eventos de gobernanza (`/api/v1/governance/events`)
   - Evaluaciones batch (`/api/v1/projects/{uuid}/evaluate`)
   - Telemetría (`/api/v1/aios/telemetry/events`)
   - Webhooks (`/api/v1/projects/{uuid}/webhooks`)
   - Auditoría (`/api/v1/aios/audit/logs`)
   - Evaluaciones LLM/RAG/Prompts/Agentes
   - **Estado:** ✅ Cobertura completa

**Estado:** ✅ API REST completa implementada

### ¿Pueden los partners crear extensiones?

**Sí, parcialmente:**

1. **Frameworks FaaS:**
   - **Arquitectura:** Frameworks sectoriales reutilizables
   - **Metamodelos:** `metamodel/framework.yaml` y `policies.json`
   - **Extensibilidad:** Partners pueden crear frameworks sectoriales
   - **Estado:** ✅ Implementado

2. **Plugins/Add-ons:**
   - **N/A:** No existe modelo de plugin/add-on explícito
   - **Planificado:** No está planificado actualmente

3. **Extensiones:**
   - **Conectores:** Partners pueden crear conectores (via `ExternalMlPlatformConnector`)
   - **Delegates:** Partners pueden crear delegates personalizados
   - **Reglas Drools:** Partners pueden agregar reglas personalizadas
   - **Estado:** ✅ Parcialmente implementado

**Estado:** ⚠️ Extensiones parciales (frameworks FaaS), no modelo completo de plugins

### ¿Existe un modelo de plugin o add-on?

**No implementado:**

- **Estado Actual:** No existe modelo explícito de plugin/add-on
- **Planificado:** No está planificado actualmente
- **Alternativa:** Frameworks FaaS como modelo de extensión

**Estado:** ❌ No implementado

### ¿Hay un "Developer Console" planificado?

**No planificado:**

- **Estado Actual:** No existe Developer Console
- **Planificado:** No está planificado actualmente
- **Alternativa:** Swagger UI para exploración de API

**Estado:** ❌ No planificado

---

## 9. MCP (MODEL CONTROL POINTS)

### ¿Existen puntos de control estándar para gobernar modelos y agentes?

**Sí, parcialmente implementado:**

1. **Puntos de Control:**
   - **Onboarding:** `ai-component-onboarding-v1.bpmn` (punto de control inicial)
   - **Aprobación:** `agent-approval-v1.bpmn`, `model-approval-v1.bpmn` (puntos de control de aprobación)
   - **Despliegue:** `AioDeploymentService` (punto de control de despliegue)
   - **Runtime:** `AioTelemetryService` (punto de control de monitoreo)
   - **Estado:** ✅ Implementado

2. **Estándar:**
   - ⚠️ No existe estándar explícito de "Model Control Points"
   - **Implementación:** Puntos de control distribuidos en procesos BPMN y servicios

**Estado:** ✅ Puntos de control implementados, ⚠️ No estandarizados explícitamente

### ¿En qué nivel están: diseño, prototipo, beta, producción?

**Producción:**

1. **Procesos BPMN:** ✅ Producción (17+ procesos en uso)
2. **Delegates:** ✅ Producción (65+ delegates en uso)
3. **Servicios AI OS:** ⚠️ Beta (PROMPT_002 en desarrollo)
4. **API REST:** ✅ Producción (endpoints en uso)

**Estado:** ✅ Mayoría en producción, ⚠️ Algunos componentes AI OS en beta

### ¿Qué eventos interceptan?

**Eventos Interceptados:**

1. **Onboarding:**
   - Registro de componentes
   - Cambios de estado (DRAFT → IN_REVIEW → APPROVED)

2. **Evaluación:**
   - Evaluaciones de bias, toxicity, hallucination
   - Evaluaciones de RAG, prompts, agentes

3. **Despliegue:**
   - Acciones de despliegue (PAUSE, RESUME, TERMINATE)
   - Cambios de escala

4. **Runtime:**
   - Eventos de telemetría
   - Violaciones de políticas
   - Alertas de degradación

**Estado:** ✅ Eventos críticos interceptados

### ¿Cómo se implementan?

**Implementación:**

1. **Procesos BPMN:**
   - Service Tasks con Delegates
   - Business Rule Tasks con Drools
   - User Tasks para HITL

2. **Delegates:**
   - JavaDelegates que implementan lógica de negocio
   - Integración con BusinessService
   - Escritura en ImmutableLog

3. **Servicios:**
   - AioPolicyEnforcementDelegate
   - AioTelemetryCollectorDelegate
   - AioComplianceAggregatorDelegate

**Estado:** ✅ Implementación completa

### ¿Son extensibles?

**Sí, extensibles:**

1. **Delegates:**
   - Partners pueden crear delegates personalizados
   - Registro como componentes Spring (@Component)

2. **Reglas Drools:**
   - Partners pueden agregar reglas personalizadas
   - Carga dinámica desde metamodelos

3. **Procesos BPMN:**
   - Partners pueden crear procesos personalizados
   - Reutilización de procesos base

**Estado:** ✅ Extensibles

---

## 10. RUNTIME OS (NÚCLEO DE UN AI OPERATING SYSTEM)

### ¿Existe hoy un "AI Runtime" en CodeflowX OS?

**Sí, parcialmente implementado:**

1. **AI OS Runtime:**
   - **Ubicación:** `codeflowx.govern.workflow.lib/runtime/`
   - **Servicios:**
     - `AioDeploymentService` - Gestión de despliegues
     - `AioServiceBindingService` - Gestión de bindings
     - `AioTelemetryService` - Telemetría
     - `AioScheduleService` - Schedules
   - **Estado:** ✅ Implementado (PROMPT_002)

2. **Runtime Completo:**
   - ⚠️ No existe runtime completo que intercepte todas las acciones
   - **Arquitectura:** Runtime distribuido en múltiples servicios

**Estado:** ✅ AI Runtime parcialmente implementado

### ¿Intercepta prompts, outputs, contextos, decisiones?

**Parcialmente:**

1. **Prompts:**
   - ✅ Interceptados durante evaluaciones (leka-prompt-evaluation)
   - ⚠️ No interceptados en tiempo real para todos los prompts ejecutados

2. **Outputs:**
   - ✅ Interceptados durante evaluaciones (leka-llm-evaluation)
   - ⚠️ No interceptados en tiempo real para todos los outputs de producción

3. **Contextos:**
   - ✅ Interceptados en sistemas RAG (leka-rag-evaluation)
   - ⚠️ No interceptados para todos los contextos de agentes

4. **Decisiones:**
   - ✅ Interceptadas en procesos BPMN
   - ✅ Registradas en ImmutableLog
   - **Estado:** ✅ Implementado

**Estado:** ⚠️ Interceptación parcial (solo eventos de gobernanza, no todo en tiempo real)

### ¿Puede aplicar políticas en tiempo real?

**Sí, parcialmente:**

1. **Tiempo Real:**
   - **AioPolicyEnforcementDelegate:** Aplica políticas durante ejecución
   - **Telemetría:** Análisis en tiempo real de eventos
   - **Estado:** ✅ Implementado parcialmente

2. **Interceptación:**
   - ⚠️ No intercepta todas las acciones en tiempo real (requiere reporte voluntario)
   - **Proxy/Interceptor:** No existe proxy que intercepte automáticamente

**Estado:** ⚠️ Políticas en tiempo real parciales (solo para eventos reportados)

### ¿Puede auditar cada paso del ciclo de vida de IA?

**Sí, para eventos de gobernanza:**

1. **Ciclo de Vida:**
   - ✅ Onboarding: Auditado (ImmutableLog)
   - ✅ Evaluación: Auditado (ImmutableLog)
   - ✅ Aprobación: Auditado (ImmutableLog)
   - ✅ Despliegue: Auditado (ImmutableLog)
   - ✅ Runtime: Auditado parcialmente (telemetría)

2. **Cada Paso:**
   - ✅ Eventos de gobernanza: Auditados completamente
   - ⚠️ Operaciones de producción: No auditadas completamente (solo eventos críticos)

**Estado:** ✅ Auditoría completa de ciclo de vida de gobernanza, ⚠️ No todas las operaciones de producción

### ¿Puede gobernar cualquier modelo, proveedor, agente o RAG?

**Sí, implementado:**

1. **Modelos:**
   - ✅ Cualquier modelo registrado (OpenAI, Anthropic, Google, Mistral, local)
   - ✅ Gobierno via `AioComponent` (type=MODEL)
   - **Estado:** ✅ Implementado

2. **Proveedores:**
   - ✅ Agnóstico de proveedor (leka-model-wrapper)
   - ✅ Gobierno unificado independiente del proveedor
   - **Estado:** ✅ Implementado

3. **Agentes:**
   - ✅ Cualquier agente registrado (interno o externo)
   - ✅ Gobierno via `AioComponent` (type=AGENT)
   - **Estado:** ✅ Implementado

4. **RAG:**
   - ✅ Cualquier sistema RAG registrado
   - ✅ Gobierno via `RagSystem` y `AioComponent` (type=PIPELINE)
   - **Estado:** ✅ Implementado

**Estado:** ✅ Gobierno unificado de cualquier activo IA

---

## 11. ROADMAP INTERNO

### ¿Qué funcionalidades están planificadas en 6, 12 y 24 meses?

**Roadmap (basado en documentos encontrados):**

#### **6 Meses (Q1-Q2 2026):**
1. **SDKs:** Implementación de SDKs Python/Java/TypeScript (PROMPTS_15)
2. **Conectores Enterprise:** Databricks, Snowflake (PROMPTS_12)
3. **MCP Adapter:** Adaptador MCP completo (PROMPTS_17)
4. **Webhooks:** Sistema completo de webhooks (PROMPTS_16)
5. **Microservicios Python:** Completar microservicios pendientes (leka-fria-generator, etc.)

#### **12 Meses (Q3-Q4 2026):**
1. **AI OS Runtime Completo:** Finalizar PROMPT_002 (runtime completo)
2. **Interceptación Tiempo Real:** Proxy/interceptor para interceptación automática
3. **Agent Supervisor:** Componente centralizado de supervisión de agentes
4. **Memorias y Contextos:** Gestión de memorias y contextos de agentes
5. **Marketplace Premium:** Marketplace para conectores premium

#### **24 Meses (2027):**
1. **Multi-tenant:** Arquitectura multi-tenant completa
2. **Real-time Streaming:** Streaming de métricas en tiempo real
3. **Aprendizaje Implícito:** Gestión de aprendizaje implícito de agentes
4. **Conectores Premium:** M365, Salesforce, SAP
5. **Developer Console:** Consola de desarrollo para partners

**Estado:** ⚠️ Roadmap parcialmente documentado

### ¿Qué capacidades de AI OS ya están en diseño?

**Capacidades en Diseño:**

1. **AI OS Runtime (PROMPT_002):**
   - AioDeploymentService (en desarrollo)
   - AioServiceBindingService (en desarrollo)
   - AioTelemetryService (implementado)
   - AioScheduleService (en desarrollo)

2. **MCP Adapter (PROMPTS_17):**
   - Adaptador MCP para VSCode/Open Interpreter
   - WebSocket JSON-RPC
   - **Estado:** ⚠️ En diseño

3. **Conectores Enterprise (PROMPTS_12):**
   - Databricks + MLflow
   - Snowflake
   - Azure ML (parcialmente implementado)
   - SageMaker (parcialmente implementado)

4. **SDKs (PROMPTS_15):**
   - SDK Python
   - SDK Java
   - SDK TypeScript

**Estado:** ⚠️ Varias capacidades en diseño

### ¿Qué piezas están listas para evolucionar?

**Piezas Listas para Evolucionar:**

1. **Framework de Gobernanza:**
   - ✅ Base sólida (489 entidades, BusinessServices, BPMN)
   - ✅ Listo para extender con nuevas capacidades

2. **Procesos BPMN:**
   - ✅ 17+ procesos estables
   - ✅ Listos para agregar nuevos procesos

3. **Microservicios Python:**
   - ✅ Arquitectura estable
   - ✅ Listos para agregar nuevos microservicios

4. **API REST:**
   - ✅ OpenAPI completo
   - ✅ Listo para generar SDKs

5. **ImmutableLog:**
   - ✅ Sistema completo y estable
   - ✅ Listo para extender con nuevos tipos de eventos

**Estado:** ✅ Base sólida lista para evolucionar

### ¿Qué elementos faltan para llegar a un AI Operating System Level 5?

**Elementos Faltantes para AI OS Level 5:**

1. **Interceptación Automática Completa:**
   - ❌ Proxy/interceptor que intercepte automáticamente todas las acciones
   - ❌ No requiere reporte voluntario de agentes externos

2. **Runtime Completo:**
   - ⚠️ AI OS Runtime parcialmente implementado
   - ❌ Falta runtime completo que gobierne todo el ciclo de ejecución

3. **Agent Supervisor Centralizado:**
   - ❌ No existe componente centralizado
   - ⚠️ Funcionalidad distribuida

4. **Gestión de Memorias y Contextos:**
   - ❌ No implementado
   - ⚠️ En diseño

5. **Aprendizaje Implícito:**
   - ❌ No implementado
   - ❌ No planificado

6. **Interceptación Tiempo Real:**
   - ⚠️ Parcial (solo eventos reportados)
   - ❌ Falta interceptación automática de todas las acciones

7. **Developer Console:**
   - ❌ No implementado
   - ❌ No planificado

8. **Modelo de Plugins:**
   - ❌ No implementado
   - ❌ No planificado

**Estado:** ⚠️ Faltan elementos críticos para Level 5

---

## RESUMEN EJECUTIVO

### Estado Actual: **AI OS Level 3-4 (Alto)**

**Fortalezas:**
- ✅ Framework de gobernanza sólido (489 entidades, BPMN, Drools)
- ✅ Sistema de logs inmutables completo (Art. 19 EU AI Act)
- ✅ Procesos automatizados (80% automatización)
- ✅ API REST completa
- ✅ Gobierno unificado de activos IA
- ✅ Políticas dinámicas implementadas

**Debilidades:**
- ⚠️ Interceptación automática incompleta (requiere reporte voluntario)
- ⚠️ AI OS Runtime parcialmente implementado
- ⚠️ No existe Agent Supervisor centralizado
- ⚠️ Gestión de memorias/contextos no implementada
- ⚠️ SDKs no implementados aún

**Recomendaciones:**
1. Completar AI OS Runtime (PROMPT_002)
2. Implementar interceptación automática (proxy/interceptor)
3. Crear Agent Supervisor centralizado
4. Implementar SDKs (PROMPTS_15)
5. Gestión de memorias y contextos

---

**Última Actualización:** 25 de noviembre de 2025
