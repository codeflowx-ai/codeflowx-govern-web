# 👨‍💻 GUÍA PARA DEVELOPERS BACKEND - CLASIFICACIÓN

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Desarrolladores Backend (Java/Spring Boot)
**Módulo:** Compliance - Classification (Art. 6 + Anexo III EU AI Act)

---

## 📋 ÍNDICE

1. [Arquitectura General](#arquitectura-general)
2. [Estructura de Capas](#estructura-de-capas)
3. [Servicios de Negocio](#servicios-de-negocio)
4. [Entidades JPA](#entidades-jpa)
5. [Repositorios](#repositorios)
6. [BFF (Backend for Frontend)](#bff-backend-for-frontend)
7. [Microservicio de Clasificación](#microservicio-de-clasificación)
8. [Flujos de Negocio](#flujos-de-negocio)
9. [Integración BPMN](#integración-bpmn)
10. [Integración con IA](#integración-con-ia)
11. [Validaciones y Reglas](#validaciones-y-reglas)
12. [DTOs y Mapeo](#dtos-y-mapeo)
13. [Manejo de Errores](#manejo-de-errores)
14. [Testing](#testing)
15. [Configuración](#configuración)

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
Classification Microservice (Opcional)
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
   - Responsabilidad: Agregar datos, optimizar respuestas para frontend, disparar workflows BPMN
   - Tecnología: Spring WebFlux (Reactivo)
   - Puerto: `8083`

2. **Classification Microservice** (Opcional)
   - Ubicación: `codeflowx-governance-classification-service`
   - Responsabilidad: Endpoints REST para clasificación
   - Tecnología: Spring WebFlux (Reactivo)
   - Puerto: `8095`

3. **Business Services**
   - Ubicación: `codeflowx.govern.business`
   - Responsabilidad: Lógica de negocio, validaciones, reglas
   - Tecnología: Spring Boot (Transaccional)
   - Servicios principales:
     - `HighRiskClassifierBusinessService` - Clasificación de alto riesgo
     - `AnnexIIICategoryBusinessService` - Gestión de categorías Anexo III
     - `ProhibitedSystemBusinessService` - Validación Art. 5
     - `AIClassificationService` - Sugerencias con IA

4. **Entities & Repositories**
   - Ubicación: `nocode.service.entitys`, `codeflowx.govern.repository`
   - Responsabilidad: Persistencia de datos
   - Tecnología: JPA/Hibernate
   - Entidades principales:
     - `Project` - Proyectos con campos de clasificación
     - `AnnexIIICategory` - Catálogo de categorías Anexo III
     - `ProhibitedSystem` - Sistemas prohibidos (Art. 5)

---

## 📁 ESTRUCTURA DE CAPAS

### 1. BFF Layer

**Ubicación:** `nocode.service/codeflowx.govern.bff.compliance/`

**Componentes:**

#### Controller: `controller/ClassificationController.java`

Expone endpoints REST para frontend, maneja requests HTTP, retorna DTOs optimizados.

**Endpoints disponibles:**

- `POST /api/compliance/classification/classify` - Clasificar proyecto
- `GET /api/compliance/classification/categories` - Obtener catálogo de categorías
- `GET /api/compliance/classification/projects` - Listar proyectos clasificados (paginado)
- `GET /api/compliance/classification/projects/{id}` - Obtener proyecto clasificado
- `PUT /api/compliance/classification/projects/{id}` - Actualizar clasificación
- `GET /api/compliance/classification/projects/statistics` - Estadísticas
- `POST /api/compliance/classification/suggest` - Sugerencia con IA

**Ejemplo:**

```java
@RestController
@RequestMapping("/api/compliance/classification")
@RequiredArgsConstructor
@Tag(name = "Classification", description = "Clasificación de sistemas de alto riesgo")
public class ClassificationController {

    private final ClassificationService classificationService;

    @PostMapping("/classify")
    @Operation(summary = "Clasificar proyecto como sistema de alto riesgo")
    public Mono<ResponseEntity<ClassificationResponseDto>> classify(
            @Valid @RequestBody ClassificationRequestDto request) {
        return classificationService.classify(request)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> {
                    log.error("Error clasificando proyecto", error);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }
}
```

#### Service: `service/ClassificationService.java` (interface)

Define contratos de servicio para el BFF.

#### Service Implementation: `service/impl/ClassificationServiceImpl.java`

Implementa llamadas a servicios de negocio, usa WebClient para comunicación reactiva, implementa Circuit Breaker y Retry (Resilience4j), dispara workflows BPMN después de clasificación.

**Ejemplo:**

```java
@Service
@RequiredArgsConstructor
public class ClassificationServiceImpl implements ClassificationService {

    private final HighRiskClassifierBusinessService businessService;
    private final BpmnWorkflowClient bpmnWorkflowClient;

    @Override
    public Mono<ClassificationResponseDto> classify(ClassificationRequestDto request) {
        return Mono.fromCallable(() -> {
            // Llamar al servicio de negocio (bloqueante)
            ClassificationResult result = businessService.classifySystem(
                request.getProjectId(),
                request
            );

            // Disparar workflow BPMN
            String workflowInstanceId = bpmnWorkflowClient.startClassificationWorkflow(
                request.getProjectId(),
                result.getCategory(),
                result.getIsHighRisk()
            );

            // Convertir a DTO
            return convertToDto(result, workflowInstanceId);
        })
        .subscribeOn(Schedulers.boundedElastic());
    }
}
```

---

### 2. Business Services Layer

**Ubicación:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/`

#### HighRiskClassifierBusinessService

**Responsabilidad:** Lógica de negocio central para clasificación de sistemas de alto riesgo.

**Métodos principales:**

##### `classifySystem(Long projectId, ClassificationRequestDto request)`

Clasifica un proyecto como sistema de alto riesgo.

**Parámetros:**
- `projectId`: ID del proyecto a clasificar
- `request`: DTO con datos de clasificación

**Retorna:** `ClassificationResult` con datos de la clasificación

**Flujo de ejecución:**

1. **Validar que el proyecto existe**
   ```java
   Optional<Project> projectOpt = projectRepository.findById(projectId);
   if (projectOpt.isEmpty()) {
       throw new IllegalArgumentException("Project not found: " + projectId);
   }
   ```

2. **Validar categoría**
   - Verificar que la categoría existe
   - Verificar que es una categoría principal (Level 1, III.1 a III.8)
   ```java
   AnnexIIICategory category = categoryBusinessService.getCategoryByCode(request.getCategory());
   if (category == null || !Boolean.TRUE.equals(category.getAnnislevel1())) {
       throw new IllegalArgumentException("Invalid category code: " + request.getCategory());
   }
   ```

3. **Validar subcategorías**
   - Verificar que las subcategorías pertenecen a la categoría principal
   ```java
   validateSubcategories(request.getCategory(), request.getSubcategories());
   ```

4. **Validar Art. 5 - Sistemas Prohibidos**
   - Verificar que se ha marcado la verificación de sistemas prohibidos
   ```java
   if (request.getProhibitedUseChecked() == null || !request.getProhibitedUseChecked()) {
       throw new IllegalArgumentException("Must verify that system is not prohibited under Art. 5");
   }
   ```

5. **Validar justificación**
   - Mínimo 100 caracteres
   - Debe mencionar la categoría seleccionada
   - Debe contener al menos 2 palabras clave de riesgo
   ```java
   validateJustificationQuality(request.getJustification(), category.getAnncategoryname());
   ```

6. **Crear JSONB de categorías Anexo III**
   ```java
   String annexIIICategoriesJson = createAnnexIIICategoriesJSONB(
       request.getCategory(),
       request.getSubcategories()
   );
   // Formato: {"category":"III.5","subcategories":["III.5.b","III.5.c"]}
   ```

7. **Actualizar proyecto**
   ```java
   project.setPrjishighrisk(true);
   project.setPrjannexiiicategories(annexIIICategoriesJson);
   project.setPrjclassificationdate(new Timestamp(System.currentTimeMillis()));
   project.setPrjclassificationauthor(getCurrentUser());
   project.setPrjprohibitedusechecked(request.getProhibitedUseChecked());
   projectRepository.save(project);
   ```

8. **Retornar resultado**
   ```java
   ClassificationResult result = new ClassificationResult();
   result.setProjectId(projectId);
   result.setIsHighRisk(true);
   result.setCategory(request.getCategory());
   result.setCategoryName(category.getAnncategoryname());
   result.setSubcategories(request.getSubcategories());
   result.setClassificationDate(LocalDateTime.now());
   result.setClassifiedBy(getCurrentUser());
   result.setJustification(request.getJustification());
   return result;
   ```

**Excepciones lanzadas:**
- `IllegalArgumentException`: Si alguna validación falla
- `RuntimeException`: Si hay error al crear JSONB

**Transaccionalidad:** `@Transactional` - Todo el método se ejecuta en una transacción

##### `validateJustificationQuality(String justification, String categoryName)`

Valida la calidad de la justificación según reglas de negocio (INC-004).

**Parámetros:**
- `justification`: Justificación a validar
- `categoryName`: Nombre de la categoría seleccionada

**Validaciones realizadas:**

1. **Longitud mínima:** 100 caracteres
   ```java
   if (justification.length() < 100) {
       throw new IllegalArgumentException("Justification must be at least 100 characters");
   }
   ```

2. **Mencionar categoría:** Debe contener el nombre de la categoría
   ```java
   if (!justificationLower.contains(categoryNameLower)) {
       throw new IllegalArgumentException("Justification must mention the selected category");
   }
   ```

3. **Palabras clave de riesgo:** Al menos 2 palabras clave
   ```java
   List<String> RISK_KEYWORDS = Arrays.asList(
       "riesgo", "alto riesgo", "vulnerable", "crítico", "critical",
       "risk", "high risk", "vulnerable", "critical", "impacto",
       "afecta", "afecta significativamente", "derechos fundamentales"
   );

   long keywordCount = RISK_KEYWORDS.stream()
       .filter(keyword -> justificationLower.contains(keyword.toLowerCase()))
       .count();

   if (keywordCount < 2) {
       throw new IllegalArgumentException("Justification must contain at least 2 risk keywords");
   }
   ```

**Excepciones lanzadas:**
- `IllegalArgumentException`: Si alguna validación falla

##### `validateSubcategories(String categoryCode, List<String> subcategories)`

Valida que las subcategorías pertenezcan a la categoría principal.

**Parámetros:**
- `categoryCode`: Código de la categoría principal
- `subcategories`: Lista de códigos de subcategorías

**Flujo:**
1. Obtener subcategorías válidas de la categoría
2. Verificar que cada subcategoría proporcionada esté en la lista válida
3. Lanzar excepción si alguna subcategoría es inválida

##### `createAnnexIIICategoriesJSONB(String category, List<String> subcategories)`

Crea el JSONB de categorías Anexo III para almacenar en la base de datos.

**Formato JSON:**
```json
{
  "category": "III.5",
  "subcategories": ["III.5.b", "III.5.c"]
}
```

**Retorna:** String con JSON serializado

##### `updateMetadataWithJustification(Project project, String justification)`

Actualiza el campo `metadata` del proyecto con la justificación de clasificación.

**Nota:** La justificación se almacena en `metadata.classificationJustification` ya que no hay un campo específico en la entidad `Project`.

##### `getCurrentUser()`

Obtiene el usuario actual del contexto de seguridad.

**TODO:** Integrar con Spring Security o contexto de autenticación.

**Actual:** Retorna `"system"` como valor por defecto.

---

#### AnnexIIICategoryBusinessService

**Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/catalogs/AnnexIIICategoryBusinessService.java`

**Responsabilidad:** Gestión del catálogo de categorías Anexo III.

**Métodos principales:**

##### `getMainCategories()`

Obtiene las 8 categorías principales (Level 1) del Anexo III.

**Retorna:** `List<AnnexIIICategory>` con las 8 categorías principales (III.1 a III.8)

**Ejemplo:**
```java
List<AnnexIIICategory> categories = categoryBusinessService.getMainCategories();
// Retorna: III.1, III.2, III.3, III.4, III.5, III.6, III.7, III.8
```

##### `getSubcategories(String categoryCode)`

Obtiene las subcategorías de una categoría principal.

**Parámetros:**
- `categoryCode`: Código de la categoría principal (ej: "III.5")

**Retorna:** `List<AnnexIIICategory>` con las subcategorías

**Ejemplo:**
```java
List<AnnexIIICategory> subcategories = categoryBusinessService.getSubcategories("III.5");
// Retorna: III.5.a, III.5.b, III.5.c, etc.
```

##### `getCategoryByCode(String code)`

Obtiene una categoría o subcategoría por su código.

**Parámetros:**
- `code`: Código de categoría o subcategoría

**Retorna:** `AnnexIIICategory` o `null` si no existe

**Flujo:**
1. Buscar como categoría principal
2. Si no se encuentra, buscar como subcategoría
3. Retornar `null` si no se encuentra

##### `suggestCategories(String projectDescription)`

Sugiere categorías basado en la descripción del proyecto usando keywords.

**Parámetros:**
- `projectDescription`: Descripción del proyecto

**Retorna:** `List<CategorySuggestion>` ordenadas por relevancia (top 3)

**Algoritmo:**
1. Obtener todas las categorías activas
2. Calcular relevancia basada en keywords
3. Ordenar por relevancia descendente
4. Retornar top 3

---

#### ProhibitedSystemBusinessService

**Responsabilidad:** Validación de sistemas prohibidos según Art. 5 del EU AI Act.

**Métodos principales:**

##### `checkProhibitedSystem(Project project)`

Verifica si un proyecto está en la lista de sistemas prohibidos.

**Parámetros:**
- `project`: Proyecto a verificar

**Retorna:** `boolean` - `true` si está prohibido, `false` si no

**Flujo:**
1. Obtener sistemas prohibidos activos
2. Comparar características del proyecto con sistemas prohibidos
3. Retornar resultado

##### `getActiveProhibitedSystems()`

Obtiene todos los sistemas prohibidos activos del Anexo II.

**Retorna:** `List<ProhibitedSystem>`

---

#### AIClassificationService

**Ubicación:** `codeflowx-governance-classification-service/src/main/java/com/codeflowx/govern/classification/service/AIClassificationService.java`

**Responsabilidad:** Generación de sugerencias de categorías usando IA/LLM.

**Métodos principales:**

##### `suggestCategory(String projectDescription)`

Genera sugerencia automática de categoría usando IA.

**Parámetros:**
- `projectDescription`: Descripción del proyecto

**Retorna:** `CategorySuggestionResponse` con:
- Categoría sugerida
- Score de confianza (0-1)
- Justificación de la sugerencia

**Umbral de confianza:** 0.85 (85%) - Solo se muestra sugerencia si confianza >= 0.85

**Integración:**
- Usa `AIGovernanceClient` para invocar modelos LLM (GPT-4, Claude, etc.)
- Fallback a sugerencias basadas en keywords si IA no está disponible

---

### 3. Entities Layer

**Ubicación:** `nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/`

#### Project

**Tabla:** `PRJPROJECTS`

**Campos relacionados con clasificación:**

```java
// Clasificación Alto Riesgo (Art. 6)
@Column(name = "PRJISHIGHRISK", nullable = true)
private Boolean prjishighrisk; // true si es de alto riesgo

@Column(name = "PRJANNEXIIICATEGORIES", nullable = true)
private String prjannexiiicategories; // JSONB: {"category":"III.5","subcategories":["III.5.b"]}

@Column(name = "PRJCLASSIFICATIONDATE", nullable = true)
private Timestamp prjclassificationdate; // Fecha de clasificación

@Column(name = "PRJCLASSIFICATIONAUTHOR", nullable = true, length = 100)
private String prjclassificationauthor; // Usuario que clasificó

// Validación Sistemas Prohibidos (Art. 5)
@Column(name = "PRJPROHIBITEDUSECHECKED", nullable = true)
private Boolean prjprohibitedusechecked; // true si se verificó Art. 5

@Column(name = "PRJPROHIBITEDUSEJUSTIFICATION", nullable = true)
private String prjprohibitedusejustification; // Justificación opcional

// Sector Regulado (Anexo I)
@Column(name = "PRJREGULATEDSECTOR", nullable = true)
private Boolean prjregulatedsector; // true si está en sector regulado

@Column(name = "PRJANNEXILEGISLATION", nullable = true)
private String prjannexilegislation; // JSONB con legislación Anexo I

// Metadata (almacena justificación)
@Column(name = "METADATA", nullable = true)
private String metadata; // JSONB: {"classificationJustification":"..."}
```

**Relaciones:**
- No hay relaciones directas con `AnnexIIICategory` (se usa JSONB)
- Relación con `Model` (uno a muchos) para validar datasets

#### AnnexIIICategory

**Tabla:** `ANNANNEXIIICATEGORIES`

**Campos principales:**

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "IDXANNEXIIICATEGORY", nullable = false)
private Long idxannexiiicategory; // PK

@Column(name = "iduuid", unique = true, nullable = false, length = 36)
private String iduuid; // UUID estándar

// Category Info
@Column(name = "ANNCATEGORYCODE", length = 10, unique = true, nullable = false)
private String anncategorycode; // "III.1", "III.2", ..., "III.8"

@Column(name = "ANNCATEGORYNAME", length = 200, nullable = false)
private String anncategoryname; // "Biometría", "Infraestructuras críticas", etc.

@Column(name = "ANNCATEGORYDESCRIPTION", columnDefinition = "TEXT")
private String anncategorydescription;

// Subcategorías
@Column(name = "ANNSUBCATEGORYCODE", length = 10)
private String annsubcategorycode; // "III.4.a", "III.4.b", etc. (NULL si es categoría padre)

@Column(name = "ANNSUBCATEGORYNAME", length = 300)
private String annsubcategoryname;

@Column(name = "ANNSUBCATEGORYDESCRIPTION", columnDefinition = "TEXT")
private String annsubcategorydescription;

// Jerarquía
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "ANNPARENTCATEGORY")
private AnnexIIICategory parentCategory; // NULL si es categoría root

@Column(name = "ANNISLEVEL1")
private Boolean annislevel1 = false; // TRUE para 8 categorías principales

@Column(name = "ANNISLEVEL2")
private Boolean annislevel2 = false; // TRUE para subcategorías

// EU AI Act Reference
@Column(name = "ANNANNEXIIISECTION", length = 500)
private String annannexiiisection; // Texto literal del Anexo III

@Column(name = "ANNARTICLEREFERENCE", length = 100)
private String annarticlereference; // "Art. 6.2 + Anexo III punto 4"

// Keywords para clasificación automática IA
@Column(name = "ANNKEYWORDS", columnDefinition = "TEXT")
private String annkeywords; // JSON array de keywords para ML

// Metadata
@Column(name = "ANNACTIVE")
private Boolean annactive = true;

@Column(name = "ANNDISPLAYORDER")
private Integer anndisplayorder;

@Column(name = "ANNCREATEDAT")
private Timestamp anncreatedat;
```

**Relaciones:**
- `@ManyToOne` con `AnnexIIICategory` (self-reference) para jerarquía padre-hijo

#### ProhibitedSystem

**Tabla:** `GOVPROHIBITEDSYSTEMS`

**Campos principales:**

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "IDXPROHIBITEDSYSTEM", nullable = false)
private Long idxprohibitedsystem; // PK

@Column(name = "PROHIBITEDCODE", length = 50, unique = true, nullable = false)
private String prohibitedcode; // Código del sistema prohibido

@Column(name = "PROHIBITEDNAME", length = 200, nullable = false)
private String prohibitedname; // Nombre del sistema prohibido

@Column(name = "PROHIBITEDDESCRIPTION", columnDefinition = "TEXT")
private String prohibiteddescription; // Descripción

@Column(name = "PROHIBITEDARTICLE", length = 50)
private String prohibitedarticle; // "Art. 5.1.a", "Art. 5.1.b", etc.

@Column(name = "PROHIBITEDACTIVE")
private Boolean prohibitedactive = true; // true si está activo

@Column(name = "PROHIBITEDKEYWORDS", columnDefinition = "TEXT")
private String prohibitedkeywords; // JSON array de keywords para detección automática
```

---

### 4. Repositories Layer

**Ubicación:** `nocode.service/codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/`

#### ProjectRepository

**Interface:** `com.codeflowx.govern.repository.compliance.ProjectRepository`

**Métodos principales:**

```java
public interface ProjectRepository extends GenericRepository<Project, Long> {

    // Buscar proyectos de alto riesgo
    List<Project> findByPrjishighriskTrue();

    // Buscar por categoría (usando JSONB)
    @Query(value = "SELECT * FROM PRJPROJECTS WHERE PRJANNEXIIICATEGORIES::jsonb->>'category' = :categoryCode",
           nativeQuery = true)
    List<Project> findByCategory(@Param("categoryCode") String categoryCode);

    // Buscar proyectos clasificados en rango de fechas
    List<Project> findByPrjclassificationdateBetween(Timestamp from, Timestamp to);

    // Buscar por autor de clasificación
    List<Project> findByPrjclassificationauthor(String author);
}
```

**Uso:**
```java
@Autowired
private ProjectRepository projectRepository;

// Buscar proyecto por ID
Optional<Project> project = projectRepository.findById(projectId);

// Buscar proyectos de alto riesgo
List<Project> highRiskProjects = projectRepository.findByPrjishighriskTrue();
```

#### AnnexIIICategoryRepository

**Interface:** `com.codeflowx.govern.repository.catalogs.AnnexIIICategoryRepository`

**Métodos principales:**

```java
public interface AnnexIIICategoryRepository extends GenericRepository<AnnexIIICategory, Long> {

    // Buscar categorías principales (Level 1)
    @Query("SELECT c FROM AnnexIIICategory c WHERE c.annislevel1 = true AND c.annactive = true ORDER BY c.anndisplayorder")
    List<AnnexIIICategory> findMainCategories();

    // Buscar subcategorías por categoría padre
    @Query("SELECT c FROM AnnexIIICategory c WHERE c.parentCategory.idxannexiiicategory = :parentId AND c.annactive = true")
    List<AnnexIIICategory> findSubcategoriesByParentId(@Param("parentId") Long parentId);

    // Buscar por código de categoría
    Optional<AnnexIIICategory> findByAnncategorycode(String code);

    // Buscar por código de subcategoría
    Optional<AnnexIIICategory> findByAnnsubcategorycode(String code);

    // Buscar categorías activas
    List<AnnexIIICategory> findByAnnactiveTrue();
}
```

**Uso:**
```java
@Autowired
private AnnexIIICategoryRepository categoryRepository;

// Obtener categorías principales
List<AnnexIIICategory> mainCategories = categoryRepository.findMainCategories();

// Obtener subcategorías
List<AnnexIIICategory> subcategories = categoryRepository.findSubcategoriesByParentId(categoryId);
```

---

## 🔄 FLUJOS DE NEGOCIO

### Flujo 1: Clasificación de Proyecto

```
1. Frontend envía POST /api/compliance/classification/classify
   ↓
2. BFF ClassificationController recibe request
   ↓
3. ClassificationServiceImpl.classify() se ejecuta
   ↓
4. HighRiskClassifierBusinessService.classifySystem() se ejecuta:
   a. Validar proyecto existe
   b. Validar categoría
   c. Validar subcategorías
   d. Validar Art. 5 (sistemas prohibidos)
   e. Validar justificación
   f. Crear JSONB de categorías
   g. Actualizar proyecto en BD
   ↓
5. BFF dispara workflow BPMN (opcional)
   ↓
6. BFF retorna ClassificationResponseDto al frontend
```

### Flujo 2: Obtención de Categorías

```
1. Frontend envía GET /api/compliance/classification/categories
   ↓
2. BFF ClassificationController recibe request
   ↓
3. ClassificationServiceImpl.getCategories() se ejecuta
   ↓
4. AnnexIIICategoryBusinessService.getMainCategories() se ejecuta
   ↓
5. AnnexIIICategoryRepository.findMainCategories() consulta BD
   ↓
6. BFF retorna lista de categorías al frontend
```

### Flujo 3: Sugerencia con IA

```
1. Frontend envía POST /api/compliance/classification/suggest
   ↓
2. BFF ClassificationController recibe request
   ↓
3. ClassificationServiceImpl.suggest() se ejecuta
   ↓
4. AIClassificationService.suggestCategory() se ejecuta:
   a. Invocar AIGovernanceClient con descripción del proyecto
   b. Analizar respuesta del LLM
   c. Calcular score de confianza
   d. Si confianza >= 0.85, retornar sugerencia
   e. Si confianza < 0.85 o IA no disponible, usar fallback con keywords
   ↓
5. BFF retorna CategorySuggestionResponseDto al frontend
```

---

## 🔗 INTEGRACIÓN BPMN

### Disparo de Workflow

Después de clasificar un proyecto, el BFF dispara un workflow BPMN para automatizar procesos posteriores.

**Ubicación:** `ClassificationServiceImpl.classify()`

**Ejemplo:**

```java
// Después de clasificar
ClassificationResult result = businessService.classifySystem(projectId, request);

// Disparar workflow BPMN
String workflowInstanceId = bpmnWorkflowClient.startClassificationWorkflow(
    result.getProjectId(),
    result.getCategory(),
    result.getIsHighRisk(),
    result.getClassificationDate()
);

// Variables del workflow:
// - projectId: Long
// - category: String
// - isHighRisk: Boolean
// - classificationDate: LocalDateTime
```

**Workflow BPMN:** `classification-workflow.bpmn`

**Procesos automatizados:**
1. Notificación a stakeholders
2. Creación de tareas de compliance
3. Integración con módulo FRIA (si aplica)
4. Integración con módulo PMM (si aplica)
5. Registro en EU Registration (si aplica)

---

## 🤖 INTEGRACIÓN CON IA

### AIClassificationService

**Ubicación:** `codeflowx-governance-classification-service/src/main/java/com/codeflowx/govern/classification/service/AIClassificationService.java`

**Responsabilidad:** Generar sugerencias de categorías usando modelos LLM.

**Integración:**

```java
@Service
public class AIClassificationService {

    private final AIGovernanceClient aiGovernanceClient;

    public CategorySuggestionResponse suggestCategory(String projectDescription) {
        // Invocar modelo LLM
        String prompt = buildClassificationPrompt(projectDescription);
        AIResponse response = aiGovernanceClient.invokeLLM(prompt);

        // Analizar respuesta
        CategorySuggestion suggestion = parseAIResponse(response);

        // Validar confianza
        if (suggestion.getConfidence() >= 0.85) {
            return suggestion;
        } else {
            // Fallback a keywords
            return suggestByKeywords(projectDescription);
        }
    }
}
```

**Modelos soportados:**
- GPT-4
- Claude (Anthropic)
- Llama 2/3
- Otros modelos compatibles con `AIGovernanceClient`

**Umbral de confianza:** 0.85 (85%)

**Fallback:** Si la IA no está disponible o la confianza es baja, se usa `AnnexIIICategoryBusinessService.suggestCategories()` basado en keywords.

---

## ✅ VALIDACIONES Y REGLAS

### Validaciones Implementadas

#### 1. Validación de Proyecto Existe

**Ubicación:** `HighRiskClassifierBusinessService.classifySystem()`

```java
Optional<Project> projectOpt = projectRepository.findById(projectId);
if (projectOpt.isEmpty()) {
    throw new IllegalArgumentException("Project not found: " + projectId);
}
```

#### 2. Validación de Categoría

**Ubicación:** `HighRiskClassifierBusinessService.classifySystem()`

```java
AnnexIIICategory category = categoryBusinessService.getCategoryByCode(request.getCategory());
if (category == null || !Boolean.TRUE.equals(category.getAnnislevel1())) {
    throw new IllegalArgumentException("Invalid category code: " + request.getCategory() +
            ". Must be a main category (III.1 to III.8)");
}
```

**Reglas:**
- La categoría debe existir
- Debe ser una categoría principal (Level 1)
- Código debe estar en formato "III.X" donde X es 1-8

#### 3. Validación de Subcategorías

**Ubicación:** `HighRiskClassifierBusinessService.validateSubcategories()`

```java
List<AnnexIIICategory> validSubcategories = categoryBusinessService.getSubcategories(categoryCode);
Set<String> validSubcategoryCodes = new HashSet<>();
for (AnnexIIICategory subcat : validSubcategories) {
    validSubcategoryCodes.add(subcat.getAnnsubcategorycode());
}

for (String subcategoryCode : subcategories) {
    if (!validSubcategoryCodes.contains(subcategoryCode)) {
        throw new IllegalArgumentException("Invalid subcategory: " + subcategoryCode);
    }
}
```

**Reglas:**
- Cada subcategoría debe pertenecer a la categoría principal
- Código debe estar en formato "III.X.Y" donde Y es letra (a, b, c, etc.)

#### 4. Validación Art. 5 (Sistemas Prohibidos)

**Ubicación:** `HighRiskClassifierBusinessService.classifySystem()`

```java
if (request.getProhibitedUseChecked() == null || !request.getProhibitedUseChecked()) {
    throw new IllegalArgumentException(
            "Must verify that system is not prohibited under Art. 5 of EU AI Act");
}
```

**Reglas:**
- `prohibitedUseChecked` debe ser `true`
- Verificación obligatoria antes de clasificar

#### 5. Validación de Justificación

**Ubicación:** `HighRiskClassifierBusinessService.validateJustificationQuality()`

**Reglas:**
- Mínimo 100 caracteres
- Debe mencionar la categoría seleccionada
- Debe contener al menos 2 palabras clave de riesgo

**Palabras clave de riesgo:**
```java
List<String> RISK_KEYWORDS = Arrays.asList(
    "riesgo", "alto riesgo", "vulnerable", "crítico", "critical",
    "risk", "high risk", "vulnerable", "critical", "impacto",
    "afecta", "afecta significativamente", "derechos fundamentales"
);
```

#### 6. Validación de Coherencia Modelo-Dataset (INC-001)

**Ubicación:** Integración con `ModelValidationService`

**Reglas:**
- El proyecto debe tener modelos asociados
- Cada modelo debe tener dataset de entrenamiento documentado
- Validación antes de permitir clasificación

**Nota:** Esta validación se realiza desde el BFF antes de llamar a `classifySystem()`.

#### 7. Validación de Documentación Técnica (INC-003)

**Ubicación:** Integración con `TechnicalDocumentationBusinessService`

**Reglas:**
- `MODTECHNICALDOCCOMPLETE = true`
- `MODTECHNICALDOCSCORE >= 0.90` (90% completitud)

**Nota:** Esta validación se realiza desde el BFF antes de llamar a `classifySystem()`.

---

## 📦 DTOs Y MAPEO

### DTOs Principales

#### ClassificationRequestDto

**Ubicación:** `codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/ClassificationRequestDto.java`

```java
public class ClassificationRequestDto {
    private Long projectId;
    private String category; // "III.5"
    private List<String> subcategories; // ["III.5.b", "III.5.c"]
    private String justification; // Mínimo 100 caracteres
    private Boolean prohibitedUseChecked; // true obligatorio
    private String prohibitedUseJustification; // Opcional
    private Boolean regulatedSector; // Opcional
    private List<String> annexILegislation; // Opcional
}
```

#### ClassificationResponseDto

**Ubicación:** `codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/ClassificationResponseDto.java`

```java
public class ClassificationResponseDto {
    private Boolean success;
    private String message;
    private ClassificationDataDto data;
}

public class ClassificationDataDto {
    private Long projectId;
    private Boolean isHighRisk;
    private String category;
    private String categoryName;
    private List<String> subcategories;
    private LocalDateTime classificationDate;
    private String classifiedBy;
    private Boolean workflowTriggered;
    private String workflowInstanceId;
    private Boolean euRegistrationRequired;
    private List<String> nextSteps;
}
```

#### AnnexIIICategoryDto

**Ubicación:** `codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/catalogs/AnnexIIICategoryDto.java`

```java
public class AnnexIIICategoryDto {
    private Long idxannexiiicategory;
    private String iduuid;
    private String anncategorycode;
    private String anncategoryname;
    private String anncategorydescription;
    private String annsubcategorycode;
    private String annsubcategoryname;
    private String annsubcategorydescription;
    private Boolean annislevel1;
    private Boolean annislevel2;
    private Long annparentcategory;
    private String annannexiiisection;
    private String annarticlereference;
    private String annkeywords;
    private Boolean annactive;
    private Integer anndisplayorder;
}
```

### Mapeo Entidad → DTO

**Ubicación:** `ClassificationServiceImpl` (métodos de conversión)

**Ejemplo:**

```java
private ClassificationResponseDto convertToDto(ClassificationResult result, String workflowInstanceId) {
    ClassificationResponseDto response = new ClassificationResponseDto();
    response.setSuccess(true);
    response.setMessage("Project classified successfully");

    ClassificationDataDto data = new ClassificationDataDto();
    data.setProjectId(result.getProjectId());
    data.setIsHighRisk(result.getIsHighRisk());
    data.setCategory(result.getCategory());
    data.setCategoryName(result.getCategoryName());
    data.setSubcategories(result.getSubcategories());
    data.setClassificationDate(result.getClassificationDate());
    data.setClassifiedBy(result.getClassifiedBy());
    data.setWorkflowTriggered(workflowInstanceId != null);
    data.setWorkflowInstanceId(workflowInstanceId);

    response.setData(data);
    return response;
}
```

---

## 🚨 MANEJO DE ERRORES

### Excepciones Personalizadas

#### IllegalArgumentException

**Cuándo se lanza:**
- Validaciones de negocio fallan
- Datos inválidos en request

**Ejemplo:**
```java
if (justification.length() < 100) {
    throw new IllegalArgumentException("Justification must be at least 100 characters");
}
```

#### RuntimeException

**Cuándo se lanza:**
- Errores inesperados (serialización JSON, etc.)

**Ejemplo:**
```java
try {
    return objectMapper.writeValueAsString(jsonMap);
} catch (JsonProcessingException e) {
    log.error("Error creating Annex III categories JSONB", e);
    throw new RuntimeException("Error creating Annex III categories JSONB", e);
}
```

### Manejo en BFF

**Ubicación:** `ClassificationController`

```java
return classificationService.classify(request)
    .map(ResponseEntity::ok)
    .onErrorResume(error -> {
        if (error instanceof IllegalArgumentException) {
            return Mono.just(ResponseEntity.badRequest().build());
        }
        log.error("Error clasificando proyecto", error);
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    });
```

### Códigos HTTP

- `200 OK`: Operación exitosa
- `400 Bad Request`: Request inválido o validación fallida
- `404 Not Found`: Proyecto no encontrado
- `500 Internal Server Error`: Error del servidor
- `503 Service Unavailable`: Servicio de IA no disponible

---

## 🧪 TESTING

### Tests Unitarios

**Ubicación:** `codeflowx.govern.business/src/test/java/com/codeflowx/govern/business/compliance/`

#### HighRiskClassifierBusinessServiceTest

```java
@SpringBootTest
@Transactional
class HighRiskClassifierBusinessServiceTest {

    @Autowired
    private HighRiskClassifierBusinessService service;

    @Test
    void testClassifySystem_Success() {
        // Arrange
        ClassificationRequestDto request = new ClassificationRequestDto();
        request.setProjectId(1L);
        request.setCategory("III.5");
        request.setSubcategories(Arrays.asList("III.5.b"));
        request.setJustification("Este sistema debe clasificarse como de alto riesgo porque afecta significativamente...");
        request.setProhibitedUseChecked(true);

        // Act
        ClassificationResult result = service.classifySystem(1L, request);

        // Assert
        assertNotNull(result);
        assertTrue(result.getIsHighRisk());
        assertEquals("III.5", result.getCategory());
    }

    @Test
    void testValidateJustificationQuality_TooShort() {
        // Arrange
        String justification = "Short";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            service.validateJustificationQuality(justification, "Biometría");
        });
    }
}
```

### Tests de Integración

**Ubicación:** `codeflowx.govern.bff.compliance/src/test/java/com/codeflowx/govern/bff/compliance/`

#### ClassificationControllerIntegrationTest

```java
@SpringBootTest
@AutoConfigureWebTestClient
class ClassificationControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testClassify_Success() {
        ClassificationRequestDto request = new ClassificationRequestDto();
        request.setProjectId(1L);
        request.setCategory("III.5");
        // ... otros campos

        webTestClient.post()
            .uri("/api/compliance/classification/classify")
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk()
            .expectBody(ClassificationResponseDto.class)
            .value(response -> {
                assertTrue(response.getSuccess());
            });
    }
}
```

---

## ⚙️ CONFIGURACIÓN

### Variables de Entorno

**Archivo:** `application.yml` o `application.properties`

```yaml
# BFF Configuration
server:
  port: 8083

# Database
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/governance
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

# Resilience4j (Circuit Breaker)
resilience4j:
  circuitbreaker:
    instances:
      classificationBusinessService:
        registerHealthIndicator: true
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        permittedNumberOfCallsInHalfOpenState: 3
        automaticTransitionFromOpenToHalfOpenEnabled: true
        waitDurationInOpenState: 10s
        failureRateThreshold: 50
        eventConsumerBufferSize: 10

# BPMN Workflow
bpmn:
  workflow:
    classification:
      processKey: "classification-workflow"
      url: http://localhost:8080/engine-rest

# AI Service
ai:
  governance:
    client:
      url: http://localhost:8090
      timeout: 30s
      confidence-threshold: 0.85
```

### Configuración de Circuit Breaker

**Ubicación:** `codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/config/ResilienceConfig.java`

```java
@Configuration
public class ResilienceConfig {

    @Bean
    public CircuitBreaker classificationBusinessServiceCircuitBreaker() {
        return CircuitBreaker.of("classificationBusinessService",
            CircuitBreakerConfig.custom()
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .build());
    }
}
```

---

## 📚 REFERENCIAS

### Documentación Relacionada

- `DEVELOPER_GUIDE_FRONTEND.md` - Guía de desarrollo frontend
- `ESTADO_IMPLEMENTACION_CLASSIFICATION.md` - Estado de implementación
- `VERIFICACION_INCIDENCIAS_AUDITORIA.md` - Verificación de incidencias
- `ANALISIS_COBERTURA_ART6.md` - Análisis de cobertura Art. 6

### Código Fuente

- **Business Services:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/`
- **BFF:** `codeflowx.govern.bff.compliance/`
- **Entities:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/`
- **Repositories:** `codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/`
- **DTOs:** `codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/`

---

**Última Actualización:** Diciembre 2025
**Versión:** 1.0
**Estado:** ✅ Operativo y listo para producción
