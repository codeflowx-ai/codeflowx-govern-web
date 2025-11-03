# PROMPTS - MODIFICACIÓN JAVA BACKEND EXISTENTE
## EU AI ACT COMPLIANCE - Extensión Entidades, Servicios y UI

**Equipo:** Java Team - Backend Existente  
**Fecha:** 15 de noviembre de 2025  
**Objetivo:** Extender backend Java existente con campos, servicios y UI para EU AI Act compliance  
**Esfuerzo Estimado:** 10-12 días (con 3-4 chats en paralelo)

---

## 🏗️ ARQUITECTURA BACKEND ACTUAL (ENART)

**Framework:** EnArt (NO Hibernate/JPA estándar)

**Estructura:**
```
codeflowx-govern/
├── sources/json/tables/ (161 entidades JSON EnArt)
├── sources/jpa/ (Entities Java generadas automáticamente)
│   ├── agents/
│   ├── governance/
│   ├── training/
│   ├── projects/
│   ├── models/
│   └── monitoring/
├── business/ (BusinessServices - usa DAO EnArt)
│   ├── agents/
│   ├── compliance/
│   └── ...
├── viewmodel/ (ViewModels ZKoss)
│   ├── agents/
│   ├── compliance/
│   └── ...
├── workflow/ (BPMN delegates)
└── console/zul/ (500 pantallas .zul)
```

**Arquitectura:**
```
Entity JSON → Generador Python → Entity.java → BusinessService (DAO) → ViewModel ZKoss → ZUL
```

**Packages Principales:**
- `sources/jpa/agents` (22 entidades)
- `sources/jpa/governance` (26 entidades)
- `sources/jpa/training` (21 entidades)
- `sources/jpa/projects` (16 entidades)
- `sources/jpa/models` (16 entidades)
- `sources/jpa/monitoring` (entidades)

**UI:** ZKoss (500 pantallas .zul, ViewModels Java MVVM)

**Nomenclatura ENART:**
- Tablas: `PREFIJO + NOMBRE` (ej: `DRFDRIFTDETECTIONS`)
- PK: `IDX + NOMBRE_SINGULAR` (ej: `IDXDRIFTDETECTION`)
- Campos: `PREFIJO + NOMBRE` (ej: `DRFTYPE`, `DRFSCORE`)
- UUID: `iduuid` (estándar todas las tablas)

**NO USAR en esta fase:**
- ❌ Repository (EnArt usa DAO)
- ❌ REST Controllers (SDK después)

---

## 📋 GRUPOS DE PROMPTS

### **GRUPO A: EXTENSIÓN ENTIDADES EXISTENTES (4-5 días)**

---

### **PROMPT A.1 - Extensión Entidad Model.java**

**Contexto:**
Entidad existente `Model.java` en package `com.codeflowx.govern.entity.models` que representa modelos de IA.

**Nuevos Campos EU AI Act:**

**Art. 11 + Anexo IV - Documentación técnica:**
1. `MODTECHNICALDOCURL` - URL documentación técnica (Anexo IV)
2. `MODTECHNICALDOCVERSION` - Versión doc técnica
3. `MODTECHNICALDOCCOMPLETE` - Boolean si Anexo IV completo
4. `MODTECHNICALDOCSCORE` - Score completitud (0-1)

**Art. 15.1 - Precisión y métricas:**
5. `MODACCURACYLEVEL` - Nivel precisión declarado
6. `MODPERFORMANCEMETRICS` - JSON con métricas (JSONB)

**Art. 6 - Clasificación alto riesgo:**
7. `MODISHIGHRISK` - Boolean si es alto riesgo
8. `MODANNEXIIICATEGORY` - Categoría Anexo III (si alto riesgo)
9. `MODANNEXIIISUBCATEGORY` - Subcategoría específica
10. `MODRISKCATEGORYJUSTIFICATION` - TEXT justificación clasificación

**Art. 51 - GPAI:**
11. `MODISGPAI` - Boolean si es GPAI
12. `MODGPAIFLOPSTRAINING` - FLOPs entrenamiento (BigDecimal)
13. `MODGPAISYSTEMICRISK` - Boolean si riesgo sistémico (>10^25 FLOPs)

**Prompt Específico:**

```
Necesito extender la entidad JPA Model.java existente en com.codeflowx.govern.entity.models con nuevos campos para EU AI Act compliance.

ENTIDAD ACTUAL:
- Package: com.codeflowx.govern.entity.models
- Tabla: MODEL (o similar - verificar)
- PK: IDXMODEL (verificar nomenclatura)
- Campos actuales: nombre, versión, estado, etc. (leer Entity actual)

NUEVOS CAMPOS A AÑADIR:

// Documentación Técnica (Art. 11, Anexo IV)
@Column(name = "MODTECHNICALDOCURL", length = 500)
private String technicalDocumentationUrl;

@Column(name = "MODTECHNICALDOCVERSION", length = 20)
private String technicalDocumentationVersion;

@Column(name = "MODTECHNICALDOCCOMPLETE")
private Boolean technicalDocumentationComplete = false;

@Column(name = "MODTECHNICALDOCSCORE", precision = 3, scale = 2)
private BigDecimal technicalDocumentationScore;

// Precisión y Rendimiento (Art. 15)
@Column(name = "MODACCURACYLEVEL", length = 50)
private String accuracyLevel;  // HIGH, MEDIUM, SPECIFIED

@Column(name = "MODPERFORMANCEMETRICS", columnDefinition = "JSONB")
@Type(JsonBinaryType.class)
private Map<String, Object> performanceMetrics;

// Clasificación Riesgo (Art. 6)
@Column(name = "MODISHIGHRISK")
private Boolean isHighRisk = false;

@Column(name = "MODANNEXIIICATEGORY", length = 10)
private String annexIIICategory;  // III.1, III.2, ..., III.8

@Column(name = "MODANNEXIIISUBCATEGORY", length = 10)
private String annexIIISubcategory;  // III.4.a, III.5.b, etc.

@Column(name = "MODRISKCATEGORYJUSTIFICATION", columnDefinition = "TEXT")
private String riskCategoryJustification;

// GPAI (Art. 51)
@Column(name = "MODISGPAI")
private Boolean isGPAI = false;

@Column(name = "MODGPAIFLOPSTRAINING", precision = 30, scale = 0)
private BigDecimal gpaiFlopsTraining;  // Operaciones coma flotante

@Column(name = "MODGPAISYSTEMICRISK")
private Boolean gpaiSystemicRisk = false;

INSTRUCCIONES (Arquitectura EnArt):
1. Localizar JSON EnArt existente: sources/json/tables/MODMODELS.json
2. Añadir 13 campos nuevos al array "fields" del JSON
3. Ejecutar generador Python para regenerar Model.java:
   python src/generators/java_entity_generator.py sources/json/tables/MODMODELS.json
4. Crear migration SQL (ALTER TABLE):
   ALTER TABLE MODMODELS ADD COLUMN MODTECHNICALDOCURL VARCHAR(500);
   ALTER TABLE MODMODELS ADD COLUMN MODISHIGHRISK BOOLEAN DEFAULT FALSE;
   ... (etc para los 13 campos)
5. Extender ModelBusinessService si necesario con métodos:
   - updateTechnicalDocumentation(modelId, url, score)
   - classifyAsHighRisk(modelId, category)
   - markAsGPAI(modelId, flops)
6. NO crear Repository (EnArt usa DAO)
7. NO crear Controller REST (SDK después)

MANTENER:
- JSON EnArt estructura existente
- Campos actuales intactos
- Entity.java se regenera automáticamente
- BusinessService existente funcional
```

**Artículos Cubiertos:** Art. 6, Art. 11, Art. 15, Art. 51  
**Esfuerzo:** 1 día  
**Prioridad:** 🔴 Crítica

---

### **PROMPT A.2 - Extensión Entidad Project.java**

**Contexto:**
Entidad existente `Project.java` que representa proyectos de IA.

**Nuevos Campos EU AI Act:**

**Art. 6 - Clasificación proyecto:**
1. `PRJISHIGHRISK` - Boolean si proyecto es alto riesgo
2. `PRJANNEXIIICATEGORIES` - Array categorías (JSON) - puede ser múltiple
3. `PRJCLASSIFICATIONDATE` - Timestamp clasificación
4. `PRJCLASSIFICATIONAUTHOR` - Quién clasificó

**Anexo I - Sector regulado:**
5. `PRJREGULATEDSECTOR` - Boolean si sector regulado
6. `PRJANNEXILEGISLATION` - Array legislaciones Anexo I (JSON)

**Anexo II - Validación sistemas prohibidos:**
7. `PRJPROHIBITEDUSECHECKED` - Boolean si se validó contra Art. 5
8. `PRJPROHIBITEDUSEJUSTIFICATION` - TEXT justificación

**Art. 49 - Registro:**
9. `PRJEUREGISTRATIONID` - ID registro BBDD UE
10. `PRJEUREGISTRATIONDATE` - Fecha registro
11. `PRJEUREGISTRATIONSTATUS` - PENDING, REGISTERED, REJECTED

**Prompt Específico:**

```
Necesito extender la entidad JPA Project.java existente en com.codeflowx.govern.entity.projects con campos de clasificación de riesgo y registro según EU AI Act.

ENTIDAD ACTUAL:
- Package: com.codeflowx.govern.entity.projects
- Tabla: PROJECT o similar (verificar)
- PK: IDXPROJECT (verificar)
- Prefijo campos: PRJ

NUEVOS CAMPOS A AÑADIR:

// Clasificación Alto Riesgo (Art. 6)
@Column(name = "PRJISHIGHRISK")
private Boolean isHighRisk = false;

@Column(name = "PRJANNEXIIICATEGORIES", columnDefinition = "JSONB")
@Type(JsonBinaryType.class)
private List<String> annexIIICategories;  // ["III.4.a", "III.5.b"]

@Column(name = "PRJCLASSIFICATIONDATE")
@Temporal(TemporalType.TIMESTAMP)
private Date classificationDate;

@Column(name = "PRJCLASSIFICATIONAUTHOR", length = 100)
private String classificationAuthor;

// Sector Regulado (Anexo I)
@Column(name = "PRJREGULATEDSECTOR")
private Boolean regulatedSector = false;

@Column(name = "PRJANNEXILEGISLATION", columnDefinition = "JSONB")
@Type(JsonBinaryType.class)
private List<String> annexILegislation;  // ["MDR_2017_745", "IVDR_2017_746"]

// Validación Sistemas Prohibidos (Art. 5, Anexo II)
@Column(name = "PRJPROHIBITEDUSECHECKED")
private Boolean prohibitedUseChecked = false;

@Column(name = "PRJPROHIBITEDUSEJUSTIFICATION", columnDefinition = "TEXT")
private String prohibitedUseJustification;

// Registro Base Datos UE (Art. 49)
@Column(name = "PRJEUREGISTRATIONID", length = 100, unique = true)
private String euRegistrationId;

@Column(name = "PRJEUREGISTRATIONDATE")
@Temporal(TemporalType.TIMESTAMP)
private Date euRegistrationDate;

@Column(name = "PRJEUREGISTRATIONSTATUS", length = 20)
private String euRegistrationStatus;  // PENDING, REGISTERED, REJECTED

INSTRUCCIONES (Arquitectura EnArt):
1. Localizar JSON EnArt existente: sources/json/tables/PRJPROJECTS.json (o similar)
2. Añadir 11 campos nuevos al array "fields" del JSON
3. Ejecutar generador Python regenerar Project.java:
   python src/generators/java_entity_generator.py sources/json/tables/PRJPROJECTS.json
4. Migration SQL (ALTER TABLE):
   ALTER TABLE PRJPROJECTS ADD COLUMN PRJISHIGHRISK BOOLEAN DEFAULT FALSE;
   ALTER TABLE PRJPROJECTS ADD COLUMN PRJANNEXIIICATEGORIES JSONB;
   ... (etc 11 campos)
5. Extender ProjectBusinessService con métodos:
   - classifyProject(projectId, category)
   - validateAgainstProhibited(projectId)
   - registerInEuDatabase(projectId)
6. NO crear Repository (EnArt usa DAO)
7. NO crear Controller REST (SDK después)

MANTENER:
- JSON EnArt estructura existente
- Campos actuales intactos
- Entity regenerado automáticamente
```

**Artículos Cubiertos:** Art. 5, Art. 6, Art. 49, Anexo I, Anexo II, Anexo III  
**Esfuerzo:** 1 día  
**Prioridad:** 🔴 Crítica

---

### **PROMPT A.3 - Extensión Entidad Evaluation.java**

**Contexto:**
Entidad que almacena evaluaciones de modelos/prompts/sistemas.

**Nuevos Campos EU AI Act:**

**Art. 15 - Evaluaciones robustez adversarial:**
1. `EVALADVERSARIALTESTEDBOOLEAN` - Si se hizo test adversarial
2. `EVALADVERSARIALROBUSTNESSSCORE` - Score robustez (0-1)
3. `EVALADVERSARIALRESULTS` - JSON con resultados detallados

**Art. 15.4 - Feedback loop bias:**
4. `EVALFEEDBACKLOOPTESTED` - Boolean
5. `EVALFEEDBACKLOOPBIASSCORE` - Score (0-1)

**Prompt Específico:**

```
Necesito extender la entidad Evaluation.java (o crear si no existe) en package models/evaluations con campos para evaluaciones adversariales y feedback loop bias.

UBICACIÓN:
- Package: com.codeflowx.govern.entity.evaluations (o models)
- Tabla: EVALUATION o EVALUATIONS (verificar)
- Prefijo: EVAL

NUEVOS CAMPOS:

// Robustez Adversarial (Art. 15.5)
@Column(name = "EVALADVERSARIALTESTED")
private Boolean adversarialTested = false;

@Column(name = "EVALADVERSARIALROBUSTNESS", precision = 3, scale = 2)
private BigDecimal adversarialRobustnessScore;  // 0.00 - 1.00

@Column(name = "EVALADVERSARIALRESULTS", columnDefinition = "JSONB")
@Type(JsonBinaryType.class)
private Map<String, Object> adversarialResults;
// Contiene: {
//   "fgsm_success_rate": 0.23,
//   "pgd_success_rate": 0.08,
//   "overall_robustness": 0.88,
//   "vulnerable_classes": ["class_3", "class_7"]
// }

// Feedback Loop Bias (Art. 15.4)
@Column(name = "EVALFEEDBACKLOOPTESTED")
private Boolean feedbackLoopTested = false;

@Column(name = "EVALFEEDBACKLOOPBIASSCORE", precision = 3, scale = 2)
private BigDecimal feedbackLoopBiasScore;

@Column(name = "EVALFEEDBACKLOOPRESULTS", columnDefinition = "JSONB")
@Type(JsonBinaryType.class)
private Map<String, Object> feedbackLoopResults;

// Evaluación Integral (agregado)
@Column(name = "EVALCOMPLIANCE SCORE", precision = 3, scale = 2)
private BigDecimal complianceScore;  // Score agregado de todas las evaluaciones

INSTRUCCIONES (Arquitectura EnArt):
1. Localizar JSON EnArt existente: sources/json/tables/EVALEVALUATIONS.json (o similar)
2. Añadir 6 campos nuevos al array "fields" del JSON
3. Ejecutar generador Python regenerar Evaluation.java
4. Migration SQL (ALTER TABLE)
5. Extender EvaluationBusinessService con métodos:
   - runAdversarialEvaluation(modelId) - llama micro Python
   - runFeedbackLoopEvaluation(modelId) - llama micro Python
   - calculateComplianceScore(evaluationId)
6. NO crear Repository (EnArt usa DAO)
7. NO crear Controller REST (SDK después)
```

**Artículos Cubiertos:** Art. 15.4, Art. 15.5  
**Esfuerzo:** 1 día  
**Prioridad:** 🔴 Crítica

---

### **GRUPO B: BUSINESSSERVICES (3-4 días)**

---

### **PROMPT B.1 - BusinessService QMS (Quality Management System)**

**Contexto:**
Sistema de Gestión de Calidad según Art. 17 (13 módulos integrados).

**Prompt Específico:**

```
Necesito CREAR BusinessService QualityManagementSystemBusinessService para implementar Art. 17 del EU AI Act con 13 módulos integrados.

ESPECIFICACIONES:
- Package: com.codeflowx.govern.business.compliance
- Clase: QualityManagementSystemBusinessService.java
- Arquitectura: EnArt (usa DAO, NO Repository)

FUNCIONALIDAD PRINCIPAL:

@Service
@Slf4j
public class QualityManagementSystemBusinessService {
    
    @Autowired
    private DAO dao;
    
    // Módulo a) Estrategia cumplimiento normativo
    public QmsComplianceStrategyDTO getComplianceStrategy(Long projectId);
    public void updateComplianceStrategy(Long projectId, QmsComplianceStrategyDTO strategy);
    
    // Módulo b) Control y verificación diseño
    public QmsDesignControlDTO getDesignControl(Long projectId);
    
    // Módulo c) Desarrollo y aseguramiento calidad
    public QmsQualityAssuranceDTO getQualityAssurance(Long projectId);
    
    // Módulo d) Examen, prueba, validación
    public QmsTestValidationDTO getTestValidation(Long projectId);
    public List<TestExecutionDTO> getTestHistory(Long projectId);
    
    // Módulo e) Especificaciones técnicas/normas
    public List<TechnicalStandardDTO> getAppliedStandards(Long projectId);
    public void addStandard(Long projectId, String standardId);
    
    // Módulo f) Sistemas gestión de datos
    public QmsDataManagementDTO getDataManagement(Long projectId);
    
    // Módulo g) Sistema gestión riesgos (integración Art. 9)
    public RiskManagementSystemDTO getRiskManagementSystem(Long projectId);
    
    // Módulo h) Vigilancia poscomercialización (integración Art. 72)
    public PostMarketMonitoringDTO getPostMarketMonitoring(Long projectId);
    
    // Módulo i) Notificación incidentes graves (integración Art. 73)
    public List<SeriousIncidentDTO> getSeriousIncidents(Long projectId);
    
    // Módulo j) Comunicación autoridades
    public List<AuthorityCommunicationDTO> getAuthorityCommunications(Long projectId);
    
    // Módulo k) Registro documentación
    public DocumentationRegistryDTO getDocumentationRegistry(Long projectId);
    
    // Módulo l) Gestión recursos
    public ResourceManagementDTO getResourceManagement(Long projectId);
    
    // Módulo m) Marco rendición cuentas
    public AccountabilityFrameworkDTO getAccountabilityFramework(Long projectId);
    
    // Evaluación integral QMS
    public QmsComplianceReportDTO generateQmsReport(Long projectId);
    public BigDecimal calculateQmsComplianceScore(Long projectId);
}

RESPONSABILIDADES:
- Orquestar los 13 módulos del QMS
- Integrar con otros servicios (Risk, Incident, PostMarket, etc.)
- Generar reportes compliance QMS
- Validar completitud de cada módulo
- Calcular score agregado (0-100)

INTEGRACIÓN:
- RiskManagementService (ya existe probablemente)
- IncidentReportingService (crear si no existe)
- PostMarketMonitoringService (crear si no existe)
- DocumentationService (integrar con micros Python)

DTOS A CREAR:
- QmsComplianceStrategyDTO
- QmsDesignControlDTO
- QmsQualityAssuranceDTO
- QmsTestValidationDTO
- QmsDataManagementDTO
- QmsComplianceReportDTO (agregado)
- etc.

VIEWMODEL ZKoss (para pantallas QMS):
- QmsReportViewModel.java
- QmsModuleDetailViewModel.java
- Pantallas .zul correspondientes

INTEGRACIÓN:
- BusinessService usa DAO EnArt
- ViewModel usa BusinessService
- Pantallas ZUL para UI interna
- NO Controllers REST (SDK después)

SEGUIR:
- Arquitectura EnArt
- SOLID principles
- KISS
- Naming conventions actuales
```

**Artículos Cubiertos:** Art. 17 (completo - 13 módulos)  
**Esfuerzo:** 3-4 días  
**Prioridad:** 🔴 MUY CRÍTICA (QMS es fundación)

---

### **PROMPT B.2 - BusinessService Immutable Logging**

**Contexto:**
BusinessService para crear y verificar logs inmutables con hash chains según Art. 19.

**Prompt Específico:**

```
Necesito CREAR BusinessService ImmutableLoggingBusinessService para gestionar logs inmutables con cryptographic hash chains según Art. 19 EU AI Act.

ESPECIFICACIONES:
- Package: com.codeflowx.govern.business.logging
- Clase: ImmutableLoggingBusinessService.java
- Arquitectura: EnArt (usa DAO, NO Repository)

FUNCIONALIDAD:

@Service
@Slf4j
public class ImmutableLoggingBusinessService {
    
    @Autowired
    private DAO dao;
    
    /**
     * Crea log entry inmutable con hash del anterior
     */
    public ImmutableLogDTO createLogEntry(ImmutableLogDTO logData);
    
    /**
     * Verifica integridad de cadena de logs
     */
    public LogIntegrityReportDTO verifyLogIntegrity(Long startLogId, Long endLogId);
    
    /**
     * Verifica integridad log individual
     */
    public Boolean verifyLogEntry(Long logId);
    
    /**
     * Obtiene logs por proyecto/modelo con verificación
     */
    public List<ImmutableLogDTO> getLogsWithIntegrityCheck(Long entityId, String entityType);
    
    /**
     * Genera hash para nuevo log
     */
    private String calculateLogHash(ImmutableLogDTO logData, String previousHash);
}

ALGORITMO HASH CHAIN:
current_hash = SHA-256(
    previous_hash + 
    timestamp + 
    entity_id + 
    entity_type + 
    action + 
    user_id + 
    data_json
)

TABLA ASOCIADA:
Crear entidad ImmutableLog.java:
- Tabla: IMLIMMUTABLELOGS
- Prefijo: IML
- Campos:
  - IDXIMMUTABLELOG (PK autonumérico)
  - IMLPREVIOUSHASH (VARCHAR 64)
  - IMLCURRENTHASH (VARCHAR 64)
  - IMLTIMESTAMP (TIMESTAMP)
  - IMLENTITYID (BIGINT)
  - IMLENTITYTYPE (VARCHAR 50) - PROJECT, MODEL, EVALUATION, etc.
  - IMLACTION (VARCHAR 100) - CREATE, UPDATE, DELETE, EVALUATE, etc.
  - IMLUSERID (FK a User)
  - IMLDATA (JSONB) - Datos completos de la acción
  - IMLVERIFIED (BOOLEAN) - Si se verificó integridad
  - IMLINTEGRITY (VARCHAR 20) - VALID, TAMPERED, UNVERIFIED

MÉTODOS UTILIDAD:
- SHA-256 hashing (Java Security)
- JSON serialization determinista (para hashing consistente)
- Chain validation (recorrer logs secuencialmente)

VIEWMODEL ZKoss (para UI logs):
- ImmutableLogViewerViewModel.java
- Pantalla audit_log_viewer.zul
- Verificación integridad en pantalla

INTEGRACIÓN:
- BusinessService usa DAO EnArt para queries
- dao.insert() para nuevos logs
- dao.findBySQL() para consultas
- NO Controllers REST (SDK después)

IMPORTANTE:
- Logs NUNCA se modifican (solo INSERT via DAO)
- Trigger BD previene UPDATE/DELETE
- Índices para búsqueda eficiente
- Particionamiento por fecha si volumen alto
- BusinessService implementa lógica hash chain
```

**Artículos Cubiertos:** Art. 19, Art. 12  
**Esfuerzo:** 2 días  
**Prioridad:** 🔴 MUY CRÍTICA

---

### **GRUPO C: VIEWMODELS Y UI ZUL (3-4 días)**

---

### **PROMPT C.1 - ViewModel + ZUL para Clasificador Alto Riesgo**

**Contexto:**
UI para clasificar proyectos según Anexo III (8 categorías, 25 subcategorías).

**Prompt Específico:**

```
Necesito CREAR ViewModel Java + pantalla ZUL para clasificador de sistemas de alto riesgo según Anexo III del EU AI Act.

ESPECIFICACIONES ViewModel:
- Package: com.codeflowx.govern.viewmodel.compliance
- Clase: HighRiskClassifierViewModel.java
- Extends: SelectorComposer<Component>
- Patrón: MVVM (Model-View-ViewModel ZKoss)

FUNCIONALIDAD ViewModel:

@VariableResolver(DelegatingVariableResolver.class)
public class HighRiskClassifierViewModel extends SelectorComposer<Component> {
    
    @Wire
    private Window winHighRiskClassifier;
    
    @Wire
    private Listbox lstAnnexIIICategories;
    
    @Wire
    private Listbox lstSubcategories;
    
    @Wire
    private Textbox txtJustification;
    
    @Wire
    private Button btnClassify;
    
    @Wire
    private Label lblAISuggestion;
    
    private Project currentProject;
    private List<AnnexIIICategoryDTO> categories;
    
    @WireVariable
    private AnnexIIICategoryBusinessService annexIIIBusinessService;
    
    @WireVariable
    private ProjectBusinessService projectBusinessService;
    
    @Listen("onCreate = #winHighRiskClassifier")
    public void doAfterCompose(Component comp) {
        // Cargar 8 categorías Anexo III (usa DAO)
        categories = annexIIIBusinessService.getAllCategories();
        loadCategoriesToListbox();
        
        // Si proyecto ya clasificado, pre-cargar
        if (currentProject.getIsHighRisk()) {
            preloadClassification();
        }
        
        // Llamar micro Python para sugerencia automática
        suggestCategoryWithAI();
    }
    
    @Listen("onSelect = #lstAnnexIIICategories")
    public void onCategorySelected() {
        // Cargar subcategorías de categoría seleccionada
        String selectedCategory = getSelectedCategory();
        List<String> subcategories = annexIIIBusinessService.getSubcategories(selectedCategory);
        loadSubcategoriesToListbox(subcategories);
    }
    
    @Listen("onClick = #btnClassify")
    public void doClassify() {
        // Validar selección
        if (!validateSelection()) {
            Messagebox.show("Selecciona categoría y subcategoría", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        
        // Guardar clasificación (usa DAO)
        projectBusinessService.classifyProject(currentProject.getIdxproject(), getSelectedCategories());
        
        // Trigger BPMN si necesario
        if (currentProject.getIsHighRisk()) {
            bpmnService.startProcess("high_risk_compliance_workflow", currentProject.getIdxproject());
        }
        
        Messagebox.show("Proyecto clasificado como ALTO RIESGO", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
        winHighRiskClassifier.detach();
    }
    
    /**
     * Llama micro Python para sugerencia automática de categoría
     */
    private void suggestCategoryWithAI() {
        try {
            ProjectDescriptionDTO desc = new ProjectDescriptionDTO();
            desc.setName(currentProject.getName());
            desc.setDescription(currentProject.getDescription());
            desc.setPurpose(currentProject.getPurpose());
            
            // Llamar leka-prompt-governance o nuevo micro classification
            AICategoryString suggestionDTO = pythonClient.callClassificationSuggestion(desc);
            
            if (suggestionDTO.getConfidence() > 0.7) {
                lblAISuggestion.setValue(
                    "Sugerencia IA: " + suggestionDTO.getCategory() + 
                    " (" + suggestionDTO.getConfidencePercent() + "% confianza)"
                );
                lblAISuggestion.setStyle("color: green; font-weight: bold;");
            }
        } catch (Exception e) {
            // Si falla IA, continuar con selección manual
            lblAISuggestion.setValue("Clasificación manual");
        }
    }
}

DATOS A CARGAR:

AnnexIIIService debe proporcionar:
- 8 categorías principales:
  1. Biometría
  2. Infraestructuras críticas
  3. Educación
  4. Empleo
  5. Servicios esenciales
  6. Garantía cumplimiento Derecho
  7. Migración/asilo
  8. Administración justicia
  
- 25 subcategorías (ej: III.4.a, III.4.b, III.5.a, etc.)

UI ZUL (high_risk_classifier.zul):

<?xml version="1.0" encoding="UTF-8"?>
<window id="winHighRiskClassifier" title="Clasificador de Sistemas de Alto Riesgo - Anexo III"
        border="normal" width="800px" apply="com.codeflowx.govern.viewmodel.compliance.HighRiskClassifierViewModel">
    
    <vlayout spacing="10px">
        
        <!-- Info proyecto -->
        <groupbox>
            <caption label="Información del Proyecto"/>
            <grid>
                <rows>
                    <row>
                        <label value="Proyecto:"/>
                        <label id="lblProjectName"/>
                    </row>
                    <row>
                        <label value="Descripción:"/>
                        <label id="lblProjectDesc" multiline="true"/>
                    </row>
                </rows>
            </grid>
        </groupbox>
        
        <!-- Sugerencia IA -->
        <groupbox>
            <caption label="Sugerencia Automática (IA)"/>
            <label id="lblAISuggestion" style="font-size: 14px;"/>
        </groupbox>
        
        <!-- Selección categoría -->
        <groupbox>
            <caption label="Categoría Anexo III (Seleccionar)"/>
            <listbox id="lstAnnexIIICategories" rows="5" mold="select">
                <listhead>
                    <listheader label="Categoría"/>
                    <listheader label="Descripción"/>
                </listhead>
            </listbox>
        </groupbox>
        
        <!-- Subcategoría -->
        <groupbox>
            <caption label="Subcategoría Específica"/>
            <listbox id="lstSubcategories" rows="4" mold="select" checkmark="true" multiple="true">
                <listhead>
                    <listheader label="Subcategoría"/>
                    <listheader label="Descripción"/>
                </listhead>
            </listbox>
        </groupbox>
        
        <!-- Justificación -->
        <groupbox>
            <caption label="Justificación de Clasificación (Obligatorio)"/>
            <textbox id="txtJustification" rows="4" width="100%" placeholder="Explica por qué este sistema cae en esta categoría..."/>
        </groupbox>
        
        <!-- Botones -->
        <hbox spacing="10px" pack="end">
            <button id="btnClassify" label="Clasificar como ALTO RIESGO" sclass="btn-primary"/>
            <button id="btnCancel" label="Cancelar" sclass="btn-secondary"/>
        </hbox>
        
    </vlayout>
</window>

BUSINESSSERVICES A INTEGRAR:
1. AnnexIIICategoryBusinessService - Proporciona categorías/subcategorías (usa DAO)
2. ProjectBusinessService - Guarda clasificación (usa DAO)
3. PythonMicroserviceClient - Llama sugerencia IA (RestTemplate)
4. BpmnProcessService - Inicia workflow si alto riesgo

ARQUITECTURA VIEWMODEL:
- @WireVariable para inyectar BusinessServices
- BusinessServices usan DAO EnArt internamente
- ViewModel NO accede DAO directamente
- ViewModel → BusinessService → DAO → BD

IMPORTANTE:
- Multi-select para subcategorías (un proyecto puede estar en varias)
- Validación: al menos 1 categoría + 1 subcategoría
- Justification obligatorio (min 50 caracteres)
- Confirmar antes de guardar (es decisión crítica)
- NO crear Controller REST (solo ViewModel + ZUL)
```

**Artículos Cubiertos:** Art. 6, Anexo III  
**Esfuerzo:** 2 días  
**Prioridad:** 🔴 Crítica

---

### **PROMPT C.2 - ViewModel + ZUL para FRIA Wizard**

**Contexto:**
Wizard paso a paso para crear FRIA (Fundamental Rights Impact Assessment) según Art. 27.

**Prompt Específico:**

```
Necesito CREAR ViewModel Java + Wizard ZUL multi-paso para generar FRIA según Art. 27 del EU AI Act.

ESPECIFICACIONES ViewModel:
- Package: com.codeflowx.govern.viewmodel.compliance
- Clase: FriaWizardViewModel.java

WIZARD STEPS (6 pasos = 6 elementos Art. 27.1):

Step 1: Descripción Procesos
- Input: Textbox multiline
- Campo: Process description donde se usa el sistema IA

Step 2: Período y Frecuencia
- Input: Date range + número frecuencia
- Campos: Start date, end date, usage frequency

Step 3: Categorías Personas Afectadas
- Input: Multi-select con grupos predefinidos + custom
- Opciones: General public, Employees, Customers, Vulnerable groups, etc.
- Checkbox: Include vulnerable groups (elderly, children, disabilities, etc.)

Step 4: Riesgos Específicos
- Input: Grid editable para añadir riesgos
- Columnas: Risk description, Affected group, Severity (HIGH/MED/LOW), Probability, Impact
- Botón: Añadir riesgo

Step 5: Medidas Supervisión Humana
- Input: Checkboxes + textbox
- Opciones: HITL enabled, Override capability, Training provided, etc.
- Describe: Cómo se implementa supervisión

Step 6: Medidas Mitigación
- Input: Grid para medidas preventivas/detectivas/correctivas
- Tipos: Preventive, Detective, Corrective
- Descripción medida

FUNCIONALIDAD ViewModel:

public class FriaWizardViewModel extends SelectorComposer<Component> {
    
    @Wire
    private Window winFriaWizard;
    
    @Wire
    private Include incStep;
    
    private int currentStep = 1;
    private FriaDataDTO friaData = new FriaDataDTO();
    
    @Listen("onClick = #btnNext")
    public void goNext() {
        // Validar step actual
        if (!validateCurrentStep()) {
            return;
        }
        
        // Guardar datos step actual
        savefCurrentStepData();
        
        // Avanzar
        currentStep++;
        if (currentStep <= 6) {
            loadStep(currentStep);
        } else {
            // Finalizar - generar FRIA
            generateFria();
        }
    }
    
    @Listen("onClick = #btnPrevious")
    public void goPrevious() {
        if (currentStep > 1) {
            currentStep--;
            loadStep(currentStep);
        }
    }
    
    private void generateFria() {
        try {
            // Llamar micro Python leka-fria-generator
            FriaReportDTO report = pythonClient.generateFria(friaData);
            
            // Guardar en BD
            FriaAssessment assessment = new FriaAssessment();
            assessment.setProject(currentProject);
            assessment.setReportData(report);
            assessment.setComplianceScore(report.getCompletenessScore());
            friaService.save(assessment);
            
            // Mostrar resultado
            showFriaReport(report);
            
            // Notificar autoridad si obligatorio
            if (isAuthorityNotificationRequired()) {
                notifyMarketSurveillanceAuthority(assessment);
            }
            
        } catch (Exception e) {
            Messagebox.show("Error generando FRIA: " + e.getMessage());
        }
    }
}

ZUL WIZARD:
- Wizard con 6 pasos
- Barra progreso visual
- Navegación: Previous, Next, Cancel
- Validación en cada paso
- Resumen final antes de generar
- Mostrar score completitud
- Opción descargar PDF

INTEGRACIÓN BUSINESSSERVICES:
- FriaAssessmentBusinessService (usa DAO EnArt)
- PythonMicroserviceClient (llama leka-fria-generator)
- ProjectBusinessService (obtener proyecto)
- NotificationBusinessService (notificar autoridades)

ARQUITECTURA:
- ViewModel → BusinessService → DAO → BD
- BusinessService llama micros Python si necesario
- NO crear Controller REST (solo ViewModel + ZUL)

DTOS:
- FriaDataDTO (acumula datos de 6 pasos)
- FriaReportDTO (respuesta de Python micro)
- FriaAssessmentDTO (para BD)
```

**Artículos Cubiertos:** Art. 27 (FRIA completa)  
**Esfuerzo:** 2-3 días  
**Prioridad:** 🔴 Crítica

---

## 📊 RESUMEN PROMPTS - JAVA BACKEND EXISTENTE

| Prompt | Componente | Tipo | Arquitectura | Esfuerzo | Prioridad |
|--------|------------|------|--------------|----------|-----------|
| **A.1** | Model.java | Entity Extension | JSON EnArt + 13 campos | 1 día | 🔴 Crítica |
| **A.2** | Project.java | Entity Extension | JSON EnArt + 11 campos | 1 día | 🔴 Crítica |
| **A.3** | Evaluation.java | Entity Extension | JSON EnArt + 6 campos | 1 día | 🔴 Crítica |
| **B.1** | QualityManagementSystemBusinessService | BusinessService | DAO + 13 módulos Art. 17 | 3-4 días | 🔴 MUY CRÍTICA |
| **B.2** | ImmutableLoggingBusinessService | BusinessService | DAO + Hash chains Art. 19 | 2 días | 🔴 MUY CRÍTICA |
| **C.1** | HighRiskClassifierViewModel + ZUL | UI ZKoss | ViewModel + Pantalla | 2-3 días | 🔴 Crítica |
| **C.2** | FriaWizardViewModel + ZUL | UI ZKoss | ViewModel + Wizard 6 pasos | 2-3 días | 🔴 Crítica |

**ARQUITECTURA:**
- Entities: Extender JSON EnArt + regenerar .java
- BusinessServices: Usan DAO EnArt (NO Repository)
- ViewModels: ZKoss MVVM pattern
- Pantallas: ZUL
- NO Controllers REST (SDK después)

**TOTAL ESFUERZO:** 12-15 días  
**CON 3 CHATS PARALELOS:** 4-5 días reales (o 30-40 min con velocidad actual 🚀)

---

## 🎯 DISTRIBUCIÓN TRABAJO PARALELO

### **CHAT JAVA-1 - Entities (Crítico):**
- Prompt A.1 (Model.java extension)
- Prompt A.2 (Project.java extension)
- Prompt A.3 (Evaluation.java extension)
- **Esfuerzo:** 3 días

### **CHAT JAVA-2 - Services Core (Muy Crítico):**
- Prompt B.1 (QualityManagementSystemService)
- Prompt B.2 (ImmutableLoggingService)
- **Esfuerzo:** 5-6 días

### **CHAT JAVA-3 - UI ZKoss:**
- Prompt C.1 (HighRiskClassifierViewModel + ZUL)
- Prompt C.2 (FriaWizardViewModel + ZUL)
- **Esfuerzo:** 4-6 días

**Timeline con 3 chats:** 5-6 días reales

---

## 📋 ESTÁNDARES Y CONVENCIONES

### **Nomenclatura ENART (Obligatoria):**

```
TABLAS: PREFIJO (3 chars) + NOMBRE (sin guiones bajos, MAYÚSCULAS)
  ✅ DRFDRIFTDETECTIONS
  ✅ IMLIMMUTABLELOGS
  ✅ FRIAFUNDAMENTALRIGHTSASSESSMENTS
  ❌ drift_detections (no snake_case)

COLUMNAS: PREFIJO + NOMBRE
  ✅ DRFTYPE, DRFSCORE, DRFSEVERITY
  ✅ IMLPREVIOUSHASH, IMLCURRENTHASH
  ❌ drf_type, drf_score (no snake_case)

PK: IDX + NOMBRE_SINGULAR
  ✅ IDXDRIFTDETECTION
  ✅ IDXIMMUTABLELOG
  ❌ id, drf_id (no genéricos)

UUID: iduuid (campo estándar)
  ✅ @Column(name = "iduuid", unique = true, nullable = false)
```

### **Tipos de Datos:**

```
NÚMEROS DECIMALES (scores, ratios):
@Column(name = "FIELDSCORE", precision = 3, scale = 2)
private BigDecimal fieldScore;  // 0.00 - 1.00

NÚMEROS GRANDES (FLOPs):
@Column(name = "FIELDFLOPS", precision = 30, scale = 0)
private BigDecimal fieldFlops;

JSON:
@Column(name = "FIELDDATA", columnDefinition = "JSONB")
@Type(JsonBinaryType.class)
private Map<String, Object> fieldData;

ENUMS:
@Enumerated(EnumType.STRING)
@Column(name = "FIELDSTATUS", length = 20)
private StatusEnum fieldStatus;
```

### **Relaciones:**

```
MANY-TO-ONE (FK):
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "IDXPROJECT", nullable = false)
private Project project;

ONE-TO-MANY (inversa):
@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
private List<Evaluation> evaluations = new ArrayList<>();
```

---

## ⚠️ MANTENER INTACTO

**NO ROMPER:**
- ✅ JSONs EnArt existentes (161 entidades)
- ✅ Entities Java generadas (sources/jpa/)
- ✅ Tablas BD actuales (~170 tablas)
- ✅ Relaciones FK existentes
- ✅ ViewModels actuales (500 pantallas)
- ✅ BusinessServices existentes

**SÍ EXTENDER:**
- ✅ Añadir campos a JSONs EnArt existentes
- ✅ Regenerar Entity.java con generador Python
- ✅ Nuevos campos BD (ALTER TABLE)
- ✅ Nuevos BusinessServices (DAO EnArt)
- ✅ Nuevos ViewModels (.java + .zul)

**NO CREAR en esta fase:**
- ❌ Repository (EnArt usa DAO)
- ❌ REST Controllers (SDK después)

**ARQUITECTURA CORRECTA:**
```
JSON EnArt (extender) → Generador Python → Entity.java → BusinessService (DAO) → ViewModel ZKoss → ZUL
```

---

**Fin Documento 3 de 5**

