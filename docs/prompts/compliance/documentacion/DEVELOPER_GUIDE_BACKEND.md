# 👨‍💻 GUÍA PARA DEVELOPERS BACKEND - TECHNICAL DOCUMENTATION

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Desarrolladores Backend (Java/Spring Boot)

---

## 📋 ÍNDICE

1. [Arquitectura General](#arquitectura-general)
2. [Estructura de Capas](#estructura-de-capas)
3. [Servicios de Negocio](#servicios-de-negocio)
4. [Entidades JPA](#entidades-jpa)
5. [Repositorios](#repositorios)
6. [BFF (Backend for Frontend)](#bff-backend-for-frontend)
7. [Microservicio de Negocio](#microservicio-de-negocio)
8. [Flujos de Negocio](#flujos-de-negocio)
9. [Validaciones y Reglas](#validaciones-y-reglas)
10. [Generación de PDF](#generación-de-pdf)
11. [Índices y Constraints de Base de Datos](#índices-y-constraints-de-base-de-datos)
12. [Scripts SQL](#scripts-sql)

---

## 🏗️ ARQUITECTURA GENERAL

### Capas de la Aplicación

```
Frontend (Next.js)
    ↓
API Routes (Next.js)
    ↓
BFF (Backend for Frontend)
    ↓
Business Microservice (Technical Docs Service)
    ↓
Business Services (Lógica de Negocio)
    ↓
Repositories (JPA)
    ↓
Database (PostgreSQL)
```

### Componentes Principales

1. **BFF (Backend for Frontend)**
   - Ubicación: `codeflowx.govern.bff.compliance`
   - Responsabilidad: Agregar datos, optimizar respuestas para frontend
   - Tecnología: Spring WebFlux (Reactivo)

2. **Business Microservice**
   - Ubicación: `codeflowx-governance-technical-docs-service`
   - Responsabilidad: Endpoints REST para Technical Documentation
   - Tecnología: Spring WebFlux (Reactivo)

3. **Business Services**
   - Ubicación: `codeflowx.govern.business`
   - Responsabilidad: Lógica de negocio, validaciones, reglas
   - Tecnología: Spring Boot (Transaccional)

4. **Entities & Repositories**
   - Ubicación: `nocode.service.entitys`, `codeflowx.govern.repository`
   - Responsabilidad: Persistencia de datos
   - Tecnología: JPA/Hibernate

---

## 📁 ESTRUCTURA DE CAPAS

### 1. BFF Layer

**Ubicación:** `nocode.service/codeflowx.govern.bff.compliance/`

**Componentes:**
- **Controller:** `controller/TechnicalDocsController.java`
  - Expone endpoints REST para frontend
  - Maneja requests HTTP
  - Retorna DTOs optimizados

- **Service:** `service/TechnicalDocsService.java` (interface)
  - Define contratos de servicio

- **Service Implementation:** `service/impl/TechnicalDocsServiceImpl.java`
  - Implementa llamadas a microservicio de negocio
  - Usa WebClient para comunicación reactiva
  - Implementa Circuit Breaker y Retry (Resilience4j)

**Ejemplo:**
```java
@RestController
@RequestMapping("/api/v1/technical-docs")
public class TechnicalDocsController {
    private final TechnicalDocsService technicalDocsService;

    @GetMapping("/{modelId}")
    public Mono<ResponseEntity<TechnicalDocumentationDto>> getDocumentation(
            @PathVariable Long modelId) {
        return technicalDocsService.getDocumentation(modelId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
```

---

### 2. Business Microservice

**Ubicación:** `nocode.service/codeflowx-governance-technical-docs-service/`

**Componentes:**
- **Controller:** `controller/TechnicalDocsController.java`
  - Endpoints REST reactivos
  - Convierte entidades a DTOs
  - Manejo de errores

- **Application:** `TechnicalDocsServiceApplication.java`
  - Clase principal Spring Boot
  - Configuración del microservicio

**Ejemplo:**
```java
@RestController
@RequestMapping("/api/v1/technical-docs")
public class TechnicalDocsController {
    private final TechnicalDocumentationBusinessService businessService;

    @GetMapping("/{modelId}")
    public Mono<ResponseEntity<TechnicalDocumentationDto>> getDocumentation(
            @PathVariable Long modelId) {
        return Mono.fromCallable(() -> {
                AIActTechnicalDocumentation doc = businessService.getTechnicalDocumentation(modelId);
                return toDto(doc);
            })
            .subscribeOn(Schedulers.boundedElastic())
            .map(ResponseEntity::ok);
    }
}
```

---

### 3. Business Services

**Ubicación:** `nocode.service/codeflowx.govern.business/`

**Clase Principal:** `TechnicalDocumentationBusinessService.java`

**Responsabilidades:**
- Lógica de negocio para documentación técnica
- Validaciones y reglas de negocio
- Cálculos (score, completitud)
- Generación automática de contenido
- Generación de PDF
- Integración con BPMN

**Métodos Principales:**

```java
@Service
public class TechnicalDocumentationBusinessService {

    // Obtener o crear documentación
    public AIActTechnicalDocumentation getOrCreateTechnicalDocumentation(Long modelId);

    // Obtener documentación
    public AIActTechnicalDocumentation getTechnicalDocumentation(Long modelId);

    // Calcular score de completitud
    public BigDecimal calculateDocumentationScore(Long modelId);

    // Validar completitud Anexo IV
    public boolean validateAnexoIVCompleteness(Long modelId);

    // Generar documentación automática
    public AIActTechnicalDocumentation generateDocumentation(Long modelId);

    // Generar PDF
    public String generatePdf(Long modelId);

    // Actualizar sección específica
    public AIActTechnicalDocumentation updateSection(Long modelId, String sectionName, String content);

    // Actualizar documentación completa
    public AIActTechnicalDocumentation updateDocumentation(Long modelId, AIActTechnicalDocumentation updatedDoc);

    // Obtener lista de modelos
    public List<AIActTechnicalDocumentation> getAllModelDocumentations();

    // Marcar como completo (lanza BPMN)
    public AIActTechnicalDocumentation markAsComplete(Long modelId);

    // Calcular secciones completas
    public int getCompletedSectionsCount(Long modelId);

    // Obtener secciones faltantes
    public List<String> getMissingSections(Long modelId);

    // Lanzar workflow BPMN
    public String triggerConformityAssessmentWorkflow(Long modelId);
}
```

---

## 📊 ENTIDADES JPA

### AIActTechnicalDocumentation

**Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/governance/AIActTechnicalDocumentation.java`

**Tabla:** `GOVAIACTTECHNICALDOCS`

**Índices y Constraints:**

La entidad incluye los siguientes índices para optimizar consultas frecuentes:

```java
@Table(
    name = "GOVAIACTTECHNICALDOCS",
    indexes = {
        @Index(name = "idx_techdocs_entity", columnList = "ENTITY_TYPE,ENTITY_ID"),
        @Index(name = "idx_techdocs_entity_type", columnList = "ENTITY_TYPE"),
        @Index(name = "idx_techdocs_entity_id", columnList = "ENTITY_ID"),
        @Index(name = "idx_techdocs_status", columnList = "STATUS"),
        @Index(name = "idx_techdocs_system_name", columnList = "SYSTEM_NAME"),
        @Index(name = "idx_techdocs_version", columnList = "VERSION"),
        @Index(name = "idx_techdocs_created_at", columnList = "CREATED_AT"),
        @Index(name = "idx_techdocs_updated_at", columnList = "UPDATED_AT"),
        @Index(name = "idx_techdocs_generated_at", columnList = "GENERATED_AT"),
        @Index(name = "idx_techdocs_status_entity", columnList = "STATUS,ENTITY_TYPE"),
        @Index(name = "idx_techdocs_status_created", columnList = "STATUS,CREATED_AT")
    },
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_techdocs_entity",
            columnNames = {"ENTITY_TYPE", "ENTITY_ID"}
        )
    }
)
```

**Constraint Único:**
- `uk_techdocs_entity`: Garantiza que solo exista una documentación técnica por entidad (ENTITY_TYPE + ENTITY_ID)

**Campos Principales:**

```java
@Entity
@Table(name = "GOVAIACTTECHNICALDOCS")
public class AIActTechnicalDocumentation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDX_TECHNICAL_DOC")
    private Long idxTechnicalDoc;

    @Column(name = "ENTITYTYPE")
    private String entityType; // "MODEL"

    @Column(name = "ENTITYID")
    private Long entityId; // modelId

    @Column(name = "SYSTEMNAME")
    private String systemName;

    @Column(name = "VERSION")
    private String version;

    // Secciones del Anexo IV
    @Column(name = "SYSTEMDESCRIPTION", columnDefinition = "TEXT")
    private String systemDescription; // GENERAL_DESCRIPTION

    @Column(name = "DEVELOPMENTPROCESS", columnDefinition = "TEXT")
    private String developmentProcess; // SYSTEM_ARCHITECTURE

    @Column(name = "DATAGOVERNANCE", columnDefinition = "TEXT")
    private String dataGovernance; // DATA_GOVERNANCE

    @Column(name = "RISKMANAGEMENT", columnDefinition = "TEXT")
    private String riskManagement; // RISK_MANAGEMENT

    @Column(name = "HUMANOVERSIGHT", columnDefinition = "TEXT")
    private String humanOversight; // HUMAN_OVERSIGHT

    @Column(name = "ACCURACYROBUSTNESS", columnDefinition = "TEXT")
    private String accuracyRobustness; // ACCURACY_ROBUSTNESS

    @Column(name = "CYBERSECURITYMEASURES", columnDefinition = "TEXT")
    private String cybersecurityMeasures; // CYBERSECURITY

    @Column(name = "VALIDATIONPROCEDURES", columnDefinition = "TEXT")
    private String validationProcedures; // QUALITY_CONTROL (parte)

    @Column(name = "TESTINGPROCEDURES", columnDefinition = "TEXT")
    private String testingProcedures; // QUALITY_CONTROL (parte)

    @Column(name = "MONITORINGMEASURES", columnDefinition = "TEXT")
    private String monitoringMeasures; // POST_MARKET_MONITORING

    // Campos adicionales
    @Column(name = "INTENDEDPURPOSE", columnDefinition = "TEXT")
    private String intendedPurpose;

    @Column(name = "PDFPATH")
    private String pdfPath;

    @Column(name = "STATUS")
    private String status; // "DRAFT", "APPROVED", etc.

    @Column(name = "GENERATEDAT")
    private Timestamp generatedAt;

    @Column(name = "GENERATEDBY")
    private String generatedBy;

    @Column(name = "CREATEDAT")
    private Timestamp createdAt;

    @Column(name = "UPDATEDAT")
    private Timestamp updatedAt;
}
```

**Callbacks JPA:**
```java
@PrePersist
protected void onCreate() {
    Timestamp now = new Timestamp(System.currentTimeMillis());
    if (createdAt == null) {
        createdAt = now;
    }
    if (updatedAt == null) {
        updatedAt = now;
    }
}

@PreUpdate
protected void onUpdate() {
    updatedAt = new Timestamp(System.currentTimeMillis());
}
```

---

## 🔍 REPOSITORIOS

### TechnicalDocumentationRepository

**Ubicación:** `codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/compliance/TechnicalDocumentationRepository.java`

**Interfaz:**
```java
@Repository
public interface TechnicalDocumentationRepository extends JpaRepository<AIActTechnicalDocumentation, Long> {

    // Buscar por tipo de entidad e ID
    Optional<AIActTechnicalDocumentation> findByEntityTypeAndEntityId(String entityType, Long entityId);

    // Buscar todas las documentaciones
    @Query("SELECT d FROM AIActTechnicalDocumentation d WHERE d.entityType = 'MODEL'")
    List<AIActTechnicalDocumentation> findAllModelDocumentations();

    // Buscar por estado
    List<AIActTechnicalDocumentation> findByStatus(String status);
}
```

---

## 🔄 FLUJOS DE NEGOCIO

### 1. Generar Documentación Automática

**Flujo:**
```
1. Usuario hace clic en "Generar Automáticamente"
2. Frontend → POST /api/v1/technical-docs/{modelId}/generate
3. BFF → TechnicalDocsService.generateDocumentation()
4. Microservicio → TechnicalDocsController.generateDocumentation()
5. Business Service → generateDocumentation()
   - Obtiene o crea documentación
   - Genera contenido para cada sección desde datos del modelo
   - Guarda en base de datos
6. Retorna DTO con documentación generada
```

**Implementación:**
```java
public AIActTechnicalDocumentation generateDocumentation(Long modelId) {
    AIActTechnicalDocumentation doc = getOrCreateTechnicalDocumentation(modelId);

    // Generar contenido para cada sección
    if (doc.getSystemDescription() == null || doc.getSystemDescription().isEmpty()) {
        doc.setSystemDescription(generateGeneralDescription(modelId));
    }
    // ... más secciones

    doc.setGeneratedAt(new Timestamp(System.currentTimeMillis()));
    doc.setGeneratedBy("SYSTEM");
    doc.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

    return repository.save(doc);
}
```

---

### 2. Validar Completitud

**Flujo:**
```
1. Usuario hace clic en "Validar Completitud"
2. Frontend → POST /api/v1/technical-docs/{modelId}/validate
3. BFF → TechnicalDocsService.validateCompleteness()
4. Microservicio → TechnicalDocsController.validateCompleteness()
5. Business Service → validateAnexoIVCompleteness()
   - Obtiene documentación
   - Parsea secciones
   - Verifica completitud de cada sección (11 secciones)
   - Calcula score
6. Retorna TechnicalDocValidationResultDto
```

**Implementación:**
```java
public boolean validateAnexoIVCompleteness(Long modelId) {
    AIActTechnicalDocumentation doc = getTechnicalDocumentation(modelId);
    Map<String, Boolean> sections = parseSections(doc);

    // Verificar que todas las secciones estén completas
    boolean isComplete = sections.values().stream()
        .allMatch(complete -> complete != null && complete);

    return isComplete;
}

private Map<String, Boolean> parseSections(AIActTechnicalDocumentation doc) {
    Map<String, Boolean> sections = new HashMap<>();

    sections.put("GENERAL_DESCRIPTION", isNotEmpty(doc.getSystemDescription()));
    sections.put("SYSTEM_ARCHITECTURE", isNotEmpty(doc.getDevelopmentProcess()));
    sections.put("DATA_GOVERNANCE", isNotEmpty(doc.getDataGovernance()));
    // ... más secciones

    return sections;
}

private boolean isNotEmpty(String value) {
    return value != null && !value.trim().isEmpty() && value.trim().length() > 50;
}
```

---

### 3. Calcular Score de Completitud

**Implementación:**
```java
public BigDecimal calculateDocumentationScore(Long modelId) {
    AIActTechnicalDocumentation doc = getTechnicalDocumentation(modelId);
    Map<String, Boolean> sections = parseSections(doc);

    int completedSections = 0;
    for (Boolean complete : sections.values()) {
        if (complete != null && complete) {
            completedSections++;
        }
    }

    // Score = secciones completas / 11
    BigDecimal score = new BigDecimal(completedSections)
        .divide(new BigDecimal(11), 4, RoundingMode.HALF_UP);

    return score;
}
```

---

### 4. Generar PDF

**Dependencia:** Apache PDFBox 3.0.3

**Implementación:**
```java
public String generatePdf(Long modelId) {
    AIActTechnicalDocumentation doc = getTechnicalDocumentation(modelId);

    // Crear directorio si no existe
    Path pdfDir = Paths.get(pdfDirectory);
    if (!Files.exists(pdfDir)) {
        Files.createDirectories(pdfDir);
    }

    // Generar nombre de archivo
    String fileName = "technical-docs-model-" + modelId + "-" +
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".pdf";
    Path pdfPath = pdfDir.resolve(fileName);

    // Crear documento PDF
    try (PDDocument document = new PDDocument()) {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            // Escribir contenido
            writeTitle(contentStream, "EU AI ACT - TECHNICAL DOCUMENTATION");
            writeSection(contentStream, "1. GENERAL DESCRIPTION", doc.getSystemDescription());
            // ... más secciones
        }

        // Guardar PDF
        document.save(pdfPath.toFile());
    }

    // Guardar ruta en la entidad
    String relativePath = "/api/compliance/technical-docs/" + modelId + "/pdf/download?file=" + fileName;
    doc.setPdfPath(relativePath);
    doc.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    repository.save(doc);

    return relativePath;
}
```

**Configuración:**
```yaml
# application.yml
app:
  technical-docs:
    pdf:
      directory: ${PDF_DIRECTORY:./pdfs}
```

---

### 5. Marcar como Completo y Lanzar BPMN

**Flujo:**
```
1. Usuario hace clic en "Marcar como Completo"
2. Frontend → POST /api/v1/technical-docs/{modelId}/complete
3. BFF → TechnicalDocsService.markAsComplete()
4. Microservicio → TechnicalDocsController.markAsComplete()
5. Business Service → markAsComplete()
   - Valida que esté completo
   - Actualiza estado a "APPROVED"
   - Llama a triggerConformityAssessmentWorkflow()
6. Business Service → triggerConformityAssessmentWorkflow()
   - Prepara variables del workflow
   - Llama a BpmnWorkflowClient.startProcess("conformity-assessment-process", variables)
7. Retorna DTO actualizado
```

**Implementación:**
```java
public AIActTechnicalDocumentation markAsComplete(Long modelId) {
    // Validar que esté completo
    boolean isComplete = validateAnexoIVCompleteness(modelId);
    if (!isComplete) {
        throw new IllegalStateException("Documentation is not complete. Cannot mark as complete.");
    }

    AIActTechnicalDocumentation doc = getTechnicalDocumentation(modelId);
    doc.setStatus("APPROVED");
    doc.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    doc = repository.save(doc);

    // Lanzar proceso BPMN de conformidad
    triggerConformityAssessmentWorkflow(modelId);

    return doc;
}

public String triggerConformityAssessmentWorkflow(Long modelId) {
    if (bpmnWorkflowClient == null || !bpmnWorkflowClient.isAvailable()) {
        log.warn("BpmnWorkflowClient no está disponible.");
        return null;
    }

    try {
        Map<String, Object> variables = new HashMap<>();
        variables.put("modelId", modelId);
        variables.put("entityType", "MODEL");
        variables.put("documentationComplete", true);
        variables.put("completionDate", LocalDateTime.now().toString());

        String workflowInstanceId = bpmnWorkflowClient.startProcess(
                "conformity-assessment-process",
                variables
        );

        return workflowInstanceId;
    } catch (Exception e) {
        log.error("Error disparando workflow BPMN para modelo: {}", modelId, e);
        return null;
    }
}
```

---

## ✅ VALIDACIONES Y REGLAS

### Validaciones de Secciones

**Reglas:**
1. Una sección se considera completa si:
   - El contenido no es null
   - El contenido no está vacío
   - El contenido tiene más de 50 caracteres

2. Las 11 secciones obligatorias son:
   - GENERAL_DESCRIPTION
   - SYSTEM_ARCHITECTURE
   - DATA_GOVERNANCE
   - RISK_MANAGEMENT
   - HUMAN_OVERSIGHT
   - ACCURACY_ROBUSTNESS
   - CYBERSECURITY
   - QUALITY_CONTROL (validationProcedures + testingProcedures)
   - POST_MARKET_MONITORING
   - CONFORMITY_ASSESSMENT (derivado de status)
   - RECORD_KEEPING (derivado de pdfPath)

### Validaciones de Negocio

1. **No se puede marcar como completo si:**
   - No todas las secciones están completas
   - El score es menor a 1.00

2. **Generación automática:**
   - Solo genera contenido si la sección está vacía o es null
   - No sobrescribe contenido existente

3. **Actualización de secciones:**
   - Solo actualiza si el contenido es válido (no null, no vacío)
   - Recalcula score después de actualizar

---

## 📦 DTOs

### DTOs Principales

**TechnicalDocumentationDto:**
```java
@Data
@NoArgsConstructor
public class TechnicalDocumentationDto {
    private Long idxTechnicalDoc;
    private String entityType;
    private Long entityId;
    private String systemName;
    private String version;
    private String systemDescription;
    private String intendedPurpose;
    private String developmentProcess;
    private String dataGovernance;
    private String validationProcedures;
    private String testingProcedures;
    private String monitoringMeasures;
    private String humanOversight;
    private String riskManagement;
    private String accuracyRobustness;
    private String cybersecurityMeasures;
    private String pdfPath;
    private String status;
    private LocalDateTime generatedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal overallScore;
    private Boolean isComplete;
    private List<TechnicalDocSectionDto> sections;
}
```

**TechnicalDocumentationSummaryDto:**
```java
@Data
@NoArgsConstructor
public class TechnicalDocumentationSummaryDto {
    private Long modelId;
    private String modelName;
    private BigDecimal overallScore;
    private Boolean isComplete;
    private Integer completedSections;
    private Integer totalSections;
    private String pdfUrl;
    private LocalDateTime lastUpdated;
}
```

**TechnicalDocValidationResultDto:**
```java
@Data
@NoArgsConstructor
public class TechnicalDocValidationResultDto {
    private Long modelId;
    private Boolean isComplete;
    private BigDecimal overallScore;
    private Integer completedSections;
    private Integer totalSections;
    private List<String> missingSections;
    private LocalDateTime validationDate;
}
```

**TechnicalDocPdfResultDto:**
```java
@Data
@NoArgsConstructor
public class TechnicalDocPdfResultDto {
    private Long modelId;
    private String pdfUrl;
    private LocalDateTime generatedAt;
    private Long size;
}
```

**TechnicalDocSectionUpdateDto:**
```java
@Data
@NoArgsConstructor
public class TechnicalDocSectionUpdateDto {
    private String content;
}
```

---

## 🔧 CONFIGURACIÓN

### Application Properties

```yaml
# Microservicio
server:
  port: 8100

spring:
  application:
    name: governance-technical-docs-service
  datasource:
    url: ${DATASOURCE_URL:jdbc:postgresql://localhost:5432/nocode}
    username: ${DATASOURCE_USERNAME:nocode}
    password: ${DATASOURCE_PASSWORD:nocode}
  jpa:
    hibernate:
      ddl-auto: validate  # En producción usar 'validate' o 'none'
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
        show_sql: false

app:
  technical-docs:
    pdf:
      directory: ${PDF_DIRECTORY:./pdfs}
```

### BFF Configuration

```yaml
# BFF
services:
  technicalDocsService:
    base-url: ${TECHNICAL_DOCS_SERVICE_URL:http://localhost:8100}
```

---

## 📊 ÍNDICES Y CONSTRAINTS DE BASE DE DATOS

### Índices Definidos

La entidad JPA incluye los siguientes índices para optimizar consultas:

1. **idx_techdocs_entity** (compuesto): `ENTITY_TYPE, ENTITY_ID`
   - **Propósito:** Búsqueda rápida de documentación por entidad
   - **Uso:** `findByEntityTypeAndEntityId()`

2. **idx_techdocs_entity_type**: `ENTITY_TYPE`
   - **Propósito:** Filtrado por tipo de entidad

3. **idx_techdocs_entity_id**: `ENTITY_ID`
   - **Propósito:** Búsqueda por ID de entidad

4. **idx_techdocs_status**: `STATUS`
   - **Propósito:** Filtrado por estado (DRAFT, APPROVED, etc.)

5. **idx_techdocs_system_name**: `SYSTEM_NAME`
   - **Propósito:** Búsqueda por nombre de sistema

6. **idx_techdocs_version**: `VERSION`
   - **Propósito:** Filtrado por versión

7. **idx_techdocs_created_at**: `CREATED_AT`
   - **Propósito:** Ordenación por fecha de creación

8. **idx_techdocs_updated_at**: `UPDATED_AT`
   - **Propósito:** Ordenación por fecha de actualización

9. **idx_techdocs_generated_at**: `GENERATED_AT`
   - **Propósito:** Consultas por fecha de generación

10. **idx_techdocs_status_entity** (compuesto): `STATUS, ENTITY_TYPE`
    - **Propósito:** Filtrado combinado por estado y tipo

11. **idx_techdocs_status_created** (compuesto): `STATUS, CREATED_AT`
    - **Propósito:** Ordenación por estado y fecha

### Constraints Únicos

1. **uk_techdocs_entity**: `ENTITY_TYPE, ENTITY_ID`
   - **Propósito:** Garantizar que solo exista una documentación técnica por entidad
   - **Validación:** Previene duplicados a nivel de base de datos

### Actualización Automática del Schema

JPA/Hibernate actualizará automáticamente el schema si `ddl-auto` está configurado como `update` o `create`:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update  # Solo en desarrollo
```

**⚠️ ADVERTENCIA:** En producción, usar `validate` o `none` y aplicar scripts SQL manualmente.

---

## 🧪 TESTING

### Unit Tests

```java
@ExtendWith(MockitoExtension.class)
class TechnicalDocumentationBusinessServiceTest {

    @Mock
    private TechnicalDocumentationRepository repository;

    @Mock
    private BpmnWorkflowClient bpmnWorkflowClient;

    @InjectMocks
    private TechnicalDocumentationBusinessService service;

    @Test
    void testCalculateDocumentationScore() {
        // Arrange
        Long modelId = 1001L;
        AIActTechnicalDocumentation doc = new AIActTechnicalDocumentation();
        doc.setSystemDescription("Description...");
        // ... más secciones completas

        when(repository.findByEntityTypeAndEntityId("MODEL", modelId))
            .thenReturn(Optional.of(doc));

        // Act
        BigDecimal score = service.calculateDocumentationScore(modelId);

        // Assert
        assertEquals(new BigDecimal("1.00"), score);
    }
}
```

---

## 📚 REFERENCIAS

- **Business Service:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/TechnicalDocumentationBusinessService.java`
- **BFF Controller:** `codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/controller/TechnicalDocsController.java`
- **Microservicio Controller:** `codeflowx-governance-technical-docs-service/src/main/java/com/codeflowx/govern/technicaldocs/controller/TechnicalDocsController.java`
- **Repository:** `codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/compliance/TechnicalDocumentationRepository.java`
- **Entity:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/governance/AIActTechnicalDocumentation.java`
- **Scripts SQL:** Ver sección [Scripts SQL](#scripts-sql)

---

**Última actualización:** Diciembre 2025
**Versión del documento:** 1.0
