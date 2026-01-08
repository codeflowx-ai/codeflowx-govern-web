# 🏷️ PROMPT DE IMPLEMENTACIÓN - CLASIFICACIÓN (Art. 6 + Anexo III)

**Módulo:** Compliance - Classification
**Artículo EU AI Act:** Art. 6 - Clasificación de Sistemas de Alto Riesgo + Anexo III
**Fecha:** Diciembre 2025
**Estado:** ⏳ Pendiente de implementación completa
**Esfuerzo Estimado:** 3-4 días

---

## 📋 RESUMEN DEL MÓDULO

### **Objetivo**
Implementar el clasificador de sistemas de IA de alto riesgo según el Art. 6 y Anexo III del EU AI Act. El sistema debe permitir clasificar proyectos en categorías de alto riesgo y actualizar automáticamente el campo `PRJISHIGHRISK` en la entidad Project.

### **Pantallas Requeridas**

| # | Ruta Next.js | Estado | Descripción | Prioridad |
|---|--------------|-------|-------------|-----------|
| 1 | `app/(app)/governance/compliance/classification/page.tsx` | ✅ Existe | Clasificador de alto riesgo | 🔴 Alta |
| 2 | `app/(app)/governance/compliance/classification/annex-iii-categories/page.tsx` | ❌ No existe | Catálogo categorías Anexo III | 🟡 Media |

---

## 🏗️ ARQUITECTURA Y DEPENDENCIAS

### **Pantallas ZUL Originales**
- **ZUL Principal:** `console/gobierno/compliance/high-risk-classifier.zul`
  - ViewModel: `HighRiskClassifierViewModel`
  - Ubicación: `src/main/webapp/console/gobierno/compliance/high-risk-classifier.zul`

### **ViewModels Java**
- **HighRiskClassifierViewModel** ✅
  - **Paquete:** `com.codeflowx.govern.viewmodel.compliance`
  - **Archivo:** `com/codeflowx/govern/viewmodel/compliance/HighRiskClassifierViewModel.java`
  - **Ubicación:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/compliance/HighRiskClassifierViewModel.java`
  - **Líneas:** ~994 líneas (según documentación)
  - **Servicios Usados:**
    - `@WireVariable ModelService modelService` - Gestión de modelos
    - `@WireVariable ProjectService projectService` - Gestión de proyectos
    - `@WireVariable RuntimeService runtimeService` - Workflows BPMN (Flowable)
  - **Métodos Principales:**
    - `loadAnnexIIICategories()` - Carga 8 categorías principales
    - `loadSubcategories(String categoryCode)` - Carga subcategorías
    - `suggestCategoryWithAI()` - Sugerencia automática con IA
    - `doClassify()` - Clasifica proyecto y actualiza `PRJISHIGHRISK`
    - `validateJustificationQuality(String justification)` - Valida calidad justificación (mín 100 chars, 2 keywords riesgo)
    - `triggerHighRiskWorkflow(Long projectId)` - Dispara workflow BPMN si es alto riesgo

### **Entidades JPA**
- **Project** - `com.codeflowx.govern.entity.projects.Project`
  - **Tabla:** `PRJPROJECTS` (prefijo `PRJ`)
  - **Campos relacionados con clasificación:**
    - `PRJISHIGHRISK` (Boolean) - Indica si es sistema de alto riesgo
    - `PRJANNEXIIICATEGORY` (String) - Código categoría Anexo III (ej: "A3_1", "A3_2")
    - `PRJANNEXIIISUBCATEGORIES` (JSONB) - Subcategorías seleccionadas
    - `PRJCLASSIFICATIONJUSTIFICATION` (String) - Justificación (mínimo 100 caracteres)
    - `PRJCLASSIFICATIONDATE` (Timestamp) - Fecha de clasificación
    - `PRJCLASSIFIEDBY` (String) - Usuario que clasificó

- **AnnexIIICategory** - `com.codeflowx.govern.entity.compliance.AnnexIIICategory`
  - **Tabla:** `GOVANNEXIIICATEGORIES` (prefijo `GOV`)
  - **Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/AnnexIIICategory.java`
  - **Catálogo de 8 categorías principales:**
    1. A3_1: Biometric identification and categorisation
    2. A3_2: Management and operation of critical infrastructure
    3. A3_3: Education and vocational training
    4. A3_4: Employment, workers management and access to self-employment
    5. A3_5: Access to and enjoyment of essential private services and public services and benefits
    6. A3_6: Law enforcement
    7. A3_7: Migration, asylum and border control management
    8. A3_8: Administration of justice and democratic processes
  - **25+ subcategorías** asociadas

### **Business Services Disponibles**
- **HighRiskClassifierBusinessService** (si existe)
  - `classifySystem(Long projectId, ClassificationData data)` - Clasifica proyecto
  - `suggestCategoryWithAI(Long projectId)` - Sugerencia automática con IA
  - `getAnnexIIICategories()` - Obtiene catálogo completo
  - `triggerHighRiskWorkflow(Long projectId)` - Dispara workflow BPMN si es alto riesgo

### **Servicios CRUD**
- **ProjectService** - Actualización de proyecto
- **AnnexIIICategoryService** - CRUD de categorías

### **Lógica de Negocio Detallada**

#### **Validación de Justificación (INC-004):**
```java
// Implementado en HighRiskClassifierViewModel.validateJustificationQuality()
public boolean validateJustificationQuality(String justification) {
    // 1. Mínimo 100 caracteres
    if (justification.length() < 100) {
        return false;
    }

    // 2. Debe mencionar la categoría seleccionada
    if (!justification.contains(selectedCategory.getName())) {
        return false;
    }

    // 3. Debe contener al menos 2 palabras clave de riesgo
    List<String> riskKeywords = Arrays.asList("riesgo", "alto riesgo", "vulnerable", "crítico");
    long keywordCount = riskKeywords.stream()
        .filter(keyword -> justification.toLowerCase().contains(keyword))
        .count();

    return keywordCount >= 2;
}
```

#### **Clasificación de Proyecto:**
```java
// Implementado en HighRiskClassifierViewModel.doClassify()
public void doClassify() {
    // 1. Validar justificación
    if (!validateJustificationQuality(justification)) {
        throw new ValidationException("Justificación no cumple requisitos de calidad");
    }

    // 2. Actualizar proyecto
    project.setPrjishighrisk(true);
    project.setPrjannexiiicategory(selectedCategory.getCode());
    project.setPrjannexiiisubcategories(selectedSubcategories);
    project.setPrjclassificationjustification(justification);
    project.setPrjclassificationdate(new Timestamp(System.currentTimeMillis()));
    project.setPrjclassifiedby(getCurrentUser());

    projectService.save(project);

    // 3. Trigger workflow BPMN si es alto riesgo
    if (project.getPrjishighrisk()) {
        triggerHighRiskWorkflow(project.getId());
    }
}
```

---

## 🔌 ESPECIFICACIÓN DE ENDPOINTS REST PARA MICROSERVICIOS

### **Base URL:** `/api/compliance/classification`

### **Autenticación:**
- Todos los endpoints requieren autenticación JWT
- Header: `Authorization: Bearer <token>`
- Roles requeridos según endpoint

---

### **1. POST `/api/compliance/classification/classify`**

**Descripción:** Clasifica un proyecto como sistema de alto riesgo según el Anexo III del EU AI Act.

**Roles permitidos:** `admin`, `project_manager`, `compliance_officer`

**Request Body:**
```json
{
  "projectId": 1001,
  "category": "A3_5",
  "subcategories": ["A3_5_1", "A3_5_2"],
  "justification": "El sistema evalúa la solvencia crediticia de personas naturales para préstamos y servicios financieros, lo cual está cubierto por la categoría A3_5 del Anexo III. El sistema procesa datos financieros personales y toma decisiones automatizadas que afectan significativamente el acceso a crédito, representando un alto riesgo para los derechos fundamentales de los usuarios."
}
```

**DTO Request (Java):**
```java
public class ClassificationRequest {
    @NotNull(message = "Project ID is required")
    @Min(value = 1, message = "Project ID must be positive")
    private Long projectId;

    @NotBlank(message = "Category code is required")
    @Pattern(regexp = "^A3_[1-8]$", message = "Category must be A3_1 to A3_8")
    private String category;

    @NotEmpty(message = "At least one subcategory is required")
    @Size(min = 1, max = 10, message = "Between 1 and 10 subcategories allowed")
    private List<@Pattern(regexp = "^A3_[1-8]_[0-9]+$") String> subcategories;

    @NotBlank(message = "Justification is required")
    @Size(min = 100, max = 5000, message = "Justification must be between 100 and 5000 characters")
    private String justification;
}
```

**Validaciones:**
1. `projectId` debe existir en la base de datos
2. `category` debe ser una de las 8 categorías válidas (A3_1 a A3_8)
3. `subcategories` deben pertenecer a la categoría seleccionada
4. `justification` debe tener mínimo 100 caracteres
5. `justification` debe mencionar la categoría seleccionada
6. `justification` debe contener al menos 2 palabras clave de riesgo

**Response 200 OK:**
```json
{
  "success": true,
  "message": "Project classified successfully as high-risk system",
  "data": {
    "projectId": 1001,
    "isHighRisk": true,
    "category": "A3_5",
    "categoryName": "Access to and enjoyment of essential private services and public services and benefits",
    "subcategories": ["A3_5_1", "A3_5_2"],
    "classificationDate": "2025-01-15T10:30:00Z",
    "classifiedBy": "maria.gonzalez",
    "workflowTriggered": true,
    "workflowInstanceId": "wf-instance-12345"
  }
}
```

**DTO Response (Java):**
```java
public class ClassificationResponse {
    private Boolean success;
    private String message;
    private ClassificationData data;

    public static class ClassificationData {
        private Long projectId;
        private Boolean isHighRisk;
        private String category;
        private String categoryName;
        private List<String> subcategories;
        private LocalDateTime classificationDate;
        private String classifiedBy;
        private Boolean workflowTriggered;
        private String workflowInstanceId;
    }
}
```

**Códigos de Error:**
- `400 Bad Request`: Validación fallida (justificación corta, categoría inválida, etc.)
- `404 Not Found`: Proyecto no encontrado
- `403 Forbidden`: Usuario sin permisos
- `409 Conflict`: Proyecto ya clasificado (opcional, según reglas de negocio)
- `500 Internal Server Error`: Error del servidor

**Lógica de Negocio:**
1. Validar request según reglas anteriores
2. Buscar proyecto por ID
3. Validar justificación con `validateJustificationQuality()`
4. Actualizar campos en entidad `Project`:
   - `PRJISHIGHRISK = true`
   - `PRJANNEXIIICATEGORY = category`
   - `PRJANNEXIIISUBCATEGORIES = subcategories` (JSONB)
   - `PRJCLASSIFICATIONJUSTIFICATION = justification`
   - `PRJCLASSIFICATIONDATE = now()`
   - `PRJCLASSIFIEDBY = currentUser()`
5. Guardar proyecto con `ProjectService.save()`
6. Disparar workflow BPMN con `triggerHighRiskWorkflow(projectId)`
7. Retornar respuesta con datos actualizados

---

### **2. POST `/api/compliance/classification/suggest`**

**Descripción:** Genera sugerencia automática de categoría y subcategorías usando IA basada en la descripción del proyecto.

**Roles permitidos:** `admin`, `project_manager`, `compliance_officer`

**Request Body:**
```json
{
  "projectId": 1001
}
```

**DTO Request (Java):**
```java
public class AISuggestionRequest {
    @NotNull(message = "Project ID is required")
    @Min(value = 1, message = "Project ID must be positive")
    private Long projectId;
}
```

**Response 200 OK:**
```json
{
  "success": true,
  "data": {
    "suggestedCategory": "A3_5",
    "suggestedCategoryName": "Access to and enjoyment of essential private services and public services and benefits",
    "suggestedSubcategories": ["A3_5_1", "A3_5_2"],
    "confidence": 0.92,
    "reasoning": "El sistema evalúa la solvencia crediticia de personas naturales para préstamos y servicios financieros, lo cual está cubierto por la categoría A3_5 del Anexo III. Las subcategorías A3_5_1 (Credit scoring) y A3_5_2 (Insurance pricing) son relevantes dado que el sistema procesa datos financieros personales y toma decisiones automatizadas que afectan significativamente el acceso a crédito."
  }
}
```

**DTO Response (Java):**
```java
public class AISuggestionResponse {
    private Boolean success;
    private AISuggestionData data;

    public static class AISuggestionData {
        private String suggestedCategory;
        private String suggestedCategoryName;
        private List<String> suggestedSubcategories;
        private Double confidence; // 0.0 a 1.0
        private String reasoning;
    }
}
```

**Códigos de Error:**
- `400 Bad Request`: Project ID inválido
- `404 Not Found`: Proyecto no encontrado
- `403 Forbidden`: Usuario sin permisos
- `503 Service Unavailable`: Servicio de IA no disponible
- `500 Internal Server Error`: Error del servidor

**Lógica de Negocio:**
1. Validar que proyecto existe
2. Obtener descripción del proyecto
3. Llamar a servicio de IA (LLM) con descripción
4. Analizar respuesta de IA y mapear a categorías Anexo III
5. Calcular nivel de confianza
6. Generar reasoning explicativo
7. Retornar sugerencia

---

### **3. GET `/api/compliance/classification/categories`**

**Descripción:** Obtiene el catálogo completo de categorías y subcategorías del Anexo III.

**Roles permitidos:** Todos los usuarios autenticados

**Query Parameters:**
- `expandSubcategories` (boolean, default: false): Incluir subcategorías en respuesta
- `categoryCode` (string, optional): Filtrar por código de categoría específica
- `activeOnly` (boolean, default: true): Solo categorías activas

**Response 200 OK:**
```json
{
  "success": true,
  "data": {
    "categories": [
      {
        "code": "A3_1",
        "name": "Biometric identification and categorisation systems",
        "description": "AI systems intended to be used for biometric identification of natural persons",
        "active": true,
        "subcategories": [
          {
            "code": "A3_1_1",
            "name": "Real-time remote biometric identification",
            "description": "Real-time identification in public spaces for law enforcement purposes"
          },
          {
            "code": "A3_1_2",
            "name": "Post-remote biometric identification",
            "description": "Identification after the fact from recorded data"
          }
        ]
      }
    ],
    "total": 8
  }
}
```

**DTO Response (Java):**
```java
public class CategoriesResponse {
    private Boolean success;
    private CategoriesData data;

    public static class CategoriesData {
        private List<CategoryDTO> categories;
        private Integer total;
    }

    public static class CategoryDTO {
        private String code;
        private String name;
        private String description;
        private Boolean active;
        private List<SubcategoryDTO> subcategories;
    }

    public static class SubcategoryDTO {
        private String code;
        private String name;
        private String description;
    }
}
```

**Códigos de Error:**
- `401 Unauthorized`: No autenticado
- `500 Internal Server Error`: Error del servidor

**Lógica de Negocio:**
1. Obtener todas las categorías de `AnnexIIICategory`
2. Aplicar filtros (activeOnly, categoryCode)
3. Si `expandSubcategories=true`, incluir subcategorías
4. Retornar lista ordenada por código

---

### **4. GET `/api/compliance/classification/projects`**

**Descripción:** Obtiene lista paginada de proyectos clasificados con filtros y búsqueda.

**Roles permitidos:** `admin`, `project_manager`, `compliance_officer`, `governance_manager`, `auditor`, `viewer`

**Query Parameters:**
- `page` (int, default: 0): Número de página (0-indexed)
- `size` (int, default: 10): Tamaño de página (máximo 100)
- `sort` (string, default: "classificationDate,desc"): Campo y dirección de ordenamiento
- `category` (string, optional): Filtrar por código de categoría (A3_1 a A3_8)
- `status` (string, optional): Filtrar por estado (`active`, `pending_review`, `archived`)
- `search` (string, optional): Búsqueda en nombre, descripción o ID de proyecto
- `classifiedBy` (string, optional): Filtrar por usuario que clasificó
- `dateFrom` (date, optional): Fecha de clasificación desde (ISO 8601)
- `dateTo` (date, optional): Fecha de clasificación hasta (ISO 8601)

**Response 200 OK:**
```json
{
  "success": true,
  "data": {
    "projects": [
      {
        "id": 1001,
        "name": "AI Credit Scoring System",
        "description": "Sistema de scoring crediticio basado en IA para evaluación de préstamos",
        "category": "A3_5",
        "categoryName": "Access to and enjoyment of essential private services and public services and benefits",
        "subcategories": ["A3_5_1", "A3_5_2"],
        "justification": "El sistema evalúa la solvencia crediticia...",
        "classifiedDate": "2025-01-15T10:30:00Z",
        "classifiedBy": "maria.gonzalez",
        "status": "active",
        "modelsCount": 2
      }
    ],
    "pagination": {
      "page": 0,
      "size": 10,
      "totalElements": 25,
      "totalPages": 3,
      "first": true,
      "last": false
    }
  }
}
```

**DTO Response (Java):**
```java
public class ClassifiedProjectsResponse {
    private Boolean success;
    private ProjectsData data;

    public static class ProjectsData {
        private List<ClassifiedProjectDTO> projects;
        private PaginationInfo pagination;
    }

    public static class ClassifiedProjectDTO {
        private Long id;
        private String name;
        private String description;
        private String category;
        private String categoryName;
        private List<String> subcategories;
        private String justification;
        private LocalDateTime classifiedDate;
        private String classifiedBy;
        private String status; // active, pending_review, archived
        private Integer modelsCount;
    }

    public static class PaginationInfo {
        private Integer page;
        private Integer size;
        private Long totalElements;
        private Integer totalPages;
        private Boolean first;
        private Boolean last;
    }
}
```

**Códigos de Error:**
- `400 Bad Request`: Parámetros de paginación inválidos
- `401 Unauthorized`: No autenticado
- `403 Forbidden`: Usuario sin permisos
- `500 Internal Server Error`: Error del servidor

**Lógica de Negocio:**
1. Validar parámetros de paginación
2. Construir query dinámica con filtros
3. Aplicar búsqueda en nombre, descripción, ID
4. Aplicar ordenamiento
5. Ejecutar query paginada
6. Mapear resultados a DTOs
7. Retornar con información de paginación

---

### **5. GET `/api/compliance/classification/projects/{projectId}`**

**Descripción:** Obtiene detalles completos de un proyecto clasificado.

**Roles permitidos:** `admin`, `project_manager`, `compliance_officer`, `governance_manager`, `auditor`, `viewer`

**Path Parameters:**
- `projectId` (long): ID del proyecto

**Response 200 OK:**
```json
{
  "success": true,
  "data": {
    "id": 1001,
    "name": "AI Credit Scoring System",
    "description": "Sistema de scoring crediticio basado en IA para evaluación de préstamos y acceso a servicios financieros",
    "category": "A3_5",
    "categoryName": "Access to and enjoyment of essential private services and public services and benefits",
    "subcategories": [
      {
        "code": "A3_5_1",
        "name": "Credit scoring",
        "description": "Evaluation of creditworthiness for loans"
      },
      {
        "code": "A3_5_2",
        "name": "Insurance pricing",
        "description": "Determination of insurance premiums"
      }
    ],
    "justification": "El sistema evalúa la solvencia crediticia de personas naturales para préstamos y servicios financieros, lo cual está cubierto por la categoría A3_5 del Anexo III...",
    "classifiedDate": "2025-01-15T10:30:00Z",
    "classifiedBy": "maria.gonzalez",
    "classifiedByName": "María González",
    "status": "active",
    "models": [
      {
        "id": 1,
        "name": "Credit Risk Model v1.0"
      },
      {
        "id": 2,
        "name": "Credit Risk Model v2.0"
      }
    ],
    "workflowInstanceId": "wf-instance-12345",
    "workflowStatus": "active"
  }
}
```

**DTO Response (Java):**
```java
public class ClassifiedProjectDetailResponse {
    private Boolean success;
    private ClassifiedProjectDetailDTO data;

    public static class ClassifiedProjectDetailDTO {
        private Long id;
        private String name;
        private String description;
        private String category;
        private String categoryName;
        private List<SubcategoryDetailDTO> subcategories;
        private String justification;
        private LocalDateTime classifiedDate;
        private String classifiedBy;
        private String classifiedByName;
        private String status;
        private List<ModelDTO> models;
        private String workflowInstanceId;
        private String workflowStatus;
    }

    public static class SubcategoryDetailDTO {
        private String code;
        private String name;
        private String description;
    }

    public static class ModelDTO {
        private Long id;
        private String name;
    }
}
```

**Códigos de Error:**
- `404 Not Found`: Proyecto no encontrado o no clasificado
- `401 Unauthorized`: No autenticado
- `403 Forbidden`: Usuario sin permisos
- `500 Internal Server Error`: Error del servidor

---

### **6. PUT `/api/compliance/classification/projects/{projectId}`**

**Descripción:** Actualiza la clasificación de un proyecto existente (reclasificación).

**Roles permitidos:** `admin`, `project_manager`, `compliance_officer`

**Path Parameters:**
- `projectId` (long): ID del proyecto

**Request Body:**
```json
{
  "category": "A3_4",
  "subcategories": ["A3_4_1"],
  "justification": "Nueva justificación actualizada con al menos 100 caracteres que explique por qué se ha cambiado la clasificación del proyecto a esta nueva categoría del Anexo III..."
}
```

**DTO Request (Java):**
```java
public class UpdateClassificationRequest {
    @NotBlank(message = "Category code is required")
    @Pattern(regexp = "^A3_[1-8]$", message = "Category must be A3_1 to A3_8")
    private String category;

    @NotEmpty(message = "At least one subcategory is required")
    @Size(min = 1, max = 10, message = "Between 1 and 10 subcategories allowed")
    private List<@Pattern(regexp = "^A3_[1-8]_[0-9]+$") String> subcategories;

    @NotBlank(message = "Justification is required")
    @Size(min = 100, max = 5000, message = "Justification must be between 100 and 5000 characters")
    private String justification;
}
```

**Response 200 OK:**
```json
{
  "success": true,
  "message": "Project classification updated successfully",
  "data": {
    "projectId": 1001,
    "isHighRisk": true,
    "category": "A3_4",
    "categoryName": "Employment, workers management and access to self-employment",
    "subcategories": ["A3_4_1"],
    "classificationDate": "2025-01-20T14:30:00Z",
    "classifiedBy": "juan.perez",
    "previousCategory": "A3_5",
    "workflowTriggered": true
  }
}
```

**Códigos de Error:**
- `400 Bad Request`: Validación fallida
- `404 Not Found`: Proyecto no encontrado
- `403 Forbidden`: Usuario sin permisos
- `500 Internal Server Error`: Error del servidor

**Lógica de Negocio:**
1. Validar request
2. Buscar proyecto y verificar que existe clasificación previa
3. Validar justificación
4. Actualizar campos (mantener historial si es requerido)
5. Si cambió la categoría, puede requerir nuevo workflow
6. Retornar respuesta con datos actualizados

---

### **7. GET `/api/compliance/classification/projects/statistics`**

**Descripción:** Obtiene estadísticas agregadas de proyectos clasificados.

**Roles permitidos:** Todos los usuarios autenticados

**Query Parameters:**
- `dateFrom` (date, optional): Fecha desde para estadísticas
- `dateTo` (date, optional): Fecha hasta para estadísticas

**Response 200 OK:**
```json
{
  "success": true,
  "data": {
    "totalProjects": 25,
    "activeProjects": 18,
    "pendingReviewProjects": 5,
    "archivedProjects": 2,
    "byCategory": {
      "A3_1": 3,
      "A3_2": 2,
      "A3_3": 1,
      "A3_4": 5,
      "A3_5": 8,
      "A3_6": 2,
      "A3_7": 2,
      "A3_8": 2
    },
    "byStatus": {
      "active": 18,
      "pending_review": 5,
      "archived": 2
    }
  }
}
```

**DTO Response (Java):**
```java
public class ClassificationStatisticsResponse {
    private Boolean success;
    private StatisticsData data;

    public static class StatisticsData {
        private Long totalProjects;
        private Long activeProjects;
        private Long pendingReviewProjects;
        private Long archivedProjects;
        private Map<String, Long> byCategory;
        private Map<String, Long> byStatus;
    }
}
```

---

## 🔄 INTEGRACIONES CON OTROS MICROSERVICIOS

### **1. Servicio de Proyectos (Project Service)**
- **Endpoint:** `PUT /api/projects/{projectId}`
- **Uso:** Actualizar campos de clasificación en entidad Project
- **Campos actualizados:**
  - `PRJISHIGHRISK`
  - `PRJANNEXIIICATEGORY`
  - `PRJANNEXIIISUBCATEGORIES`
  - `PRJCLASSIFICATIONJUSTIFICATION`
  - `PRJCLASSIFICATIONDATE`
  - `PRJCLASSIFIEDBY`

### **2. Servicio BPMN (Workflow Service)**
- **Endpoint:** `POST /api/bpmn/workflows/trigger`
- **Uso:** Disparar workflow de alto riesgo cuando se clasifica un proyecto
- **Request:**
  ```json
  {
    "workflowKey": "high-risk-classification-workflow",
    "businessKey": "project-{projectId}",
    "variables": {
      "projectId": 1001,
      "category": "A3_5",
      "isHighRisk": true
    }
  }
  ```

### **3. Servicio de IA (AI Service)**
- **Endpoint:** `POST /api/ai/classify`
- **Uso:** Generar sugerencia automática de categoría
- **Request:**
  ```json
  {
    "text": "Project description...",
    "context": "classification",
    "model": "gpt-4"
  }
  ```

### **4. Servicio de Catálogo (Catalog Service)**
- **Endpoint:** `GET /api/catalog/models?projectId={projectId}`
- **Uso:** Obtener modelos asociados a un proyecto para mostrar en detalles

---

## 🛡️ MANEJO DE ERRORES ESTÁNDAR

### **Estructura de Error Response:**
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Justification must be at least 100 characters",
    "details": [
      {
        "field": "justification",
        "message": "Size must be between 100 and 5000",
        "rejectedValue": "Short text"
      }
    ],
    "timestamp": "2025-01-15T10:30:00Z"
  }
}
```

### **Códigos de Error Comunes:**
- `VALIDATION_ERROR`: Error de validación de datos
- `PROJECT_NOT_FOUND`: Proyecto no encontrado
- `CATEGORY_NOT_FOUND`: Categoría no válida
- `INSUFFICIENT_PERMISSIONS`: Usuario sin permisos
- `WORKFLOW_ERROR`: Error al disparar workflow
- `AI_SERVICE_ERROR`: Error en servicio de IA
- `INTERNAL_SERVER_ERROR`: Error interno del servidor

---

## 📱 PANTALLA 1: Clasificador de Alto Riesgo

### **Ruta:** `app/(app)/governance/compliance/classification/page.tsx`

**Estado Actual:** ✅ Existe pero incompleta

### **Funcionalidades Requeridas:**

1. **Información del Proyecto:**
   - Mostrar datos del proyecto a clasificar (solo lectura)
   - Nombre, descripción, modelos asociados

2. **Sugerencia Automática con IA:**
   - Botón "Sugerir Categoría con IA"
   - Análisis automático de descripción del proyecto
   - Sugerencia de categoría principal y subcategorías

3. **Selección de Categoría Principal:**
   - 8 categorías principales del Anexo III:
     - A3_1: Biometric identification and categorisation
     - A3_2: Management and operation of critical infrastructure
     - A3_3: Education and vocational training
     - A3_4: Employment, workers management and access to self-employment
     - A3_5: Access to and enjoyment of essential private services and public services and benefits
     - A3_6: Law enforcement
     - A3_7: Migration, asylum and border control management
     - A3_8: Administration of justice and democratic processes

4. **Selección de Subcategorías:**
   - Multi-select de subcategorías según categoría principal
   - Al menos una subcategoría obligatoria

5. **Justificación Obligatoria:**
   - Textarea con mínimo 50 caracteres
   - Explicación de por qué es sistema de alto riesgo

6. **Confirmación y Clasificación:**
   - Botón "Clasificar como Alto Riesgo"
   - Actualización automática de `PRJISHIGHRISK = true`
   - Trigger de workflow BPMN si es alto riesgo
   - Generación de reporte PDF opcional

### **Mock Data:**

```typescript
// app/(app)/governance/data/mockClassification.ts
export const mockAnnexIIICategories = [
  {
    code: "A3_1",
    name: "Biometric identification and categorisation systems",
    description: "AI systems intended to be used for biometric identification...",
    subcategories: [
      { code: "A3_1_1", name: "Real-time remote biometric identification" },
      { code: "A3_1_2", name: "Post-remote biometric identification" },
      { code: "A3_1_3", name: "Biometric categorisation" }
    ]
  },
  {
    code: "A3_2",
    name: "Management and operation of critical infrastructure",
    description: "AI systems intended to be used as safety components...",
    subcategories: [
      { code: "A3_2_1", name: "Road traffic and water supply" },
      { code: "A3_2_2", name: "Gas, oil, electricity" },
      { code: "A3_2_3", name: "Digital infrastructure" }
    ]
  }
  // ... 6 categorías más
];

export const mockProject = {
  id: 1,
  name: "AI Credit Scoring System",
  description: "Sistema de scoring crediticio basado en IA para evaluación de préstamos",
  models: [
    { id: 1, name: "Credit Risk Model v1.0" }
  ],
  currentClassification: null
};

export const mockAISuggestion = {
  suggestedCategory: "A3_4",
  suggestedSubcategories: ["A3_4_1", "A3_4_2"],
  confidence: 0.92,
  reasoning: "El sistema evalúa candidatos para empleo y acceso a autoempleo, lo cual está cubierto por la categoría A3_4 del Anexo III."
};
```

### **API Routes Mock:**

```typescript
// app/api/compliance/classification/classify/route.ts
export async function POST(request: Request) {
  const { projectId, category, subcategories, justification } = await request.json();

  // Mock: Simular clasificación
  return Response.json({
    success: true,
    projectId,
    isHighRisk: true,
    category,
    subcategories,
    workflowTriggered: true
  });
}

// app/api/compliance/classification/suggest/route.ts
export async function POST(request: Request) {
  const { projectId } = await request.json();

  // Mock: Retornar sugerencia
  return Response.json(mockAISuggestion);
}
```

---

## 📱 PANTALLA 2: Catálogo Categorías Anexo III

### **Ruta:** `app/(app)/governance/compliance/classification/annex-iii-categories/page.tsx`

**Estado Actual:** ❌ No existe - **CREAR**

### **Funcionalidades Requeridas:**

1. **Vista de Catálogo:**
   - Listado de las 8 categorías principales
   - Expandir/colapsar para ver subcategorías
   - Búsqueda por nombre o código

2. **Información Detallada:**
   - Descripción completa de cada categoría
   - Lista de subcategorías asociadas
   - Ejemplos de sistemas que caen en cada categoría

3. **Gestión del Catálogo (Solo Admin):**
   - Crear nueva categoría/subcategoría
   - Editar descripciones
   - Activar/Desactivar categorías

### **Mock Data:**

```typescript
export const mockAnnexIIICatalog = {
  categories: mockAnnexIIICategories,
  examples: {
    "A3_1": [
      "Facial recognition systems in airports",
      "Biometric access control systems"
    ],
    "A3_4": [
      "AI-powered resume screening",
      "Automated job matching systems"
    ]
  }
};
```

---

## 🎨 ESTILOS "WOW FACTOR"

### **Estructura Base:**

```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Tag, Sparkles } from "lucide-react";

export default function ClassificationPage() {
  const { t } = useTranslation();

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-yellow-400/30 rounded-full animate-pulse" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        {/* Header */}
        <div className="text-center space-y-2">
          <div className="flex items-center justify-center gap-3 mb-4">
            <Tag className="w-8 h-8 text-yellow-500" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-yellow-500 to-yellow-700 bg-clip-text text-transparent">
              {t("compliance.classification.title", "High-Risk Classification")}
            </h1>
          </div>
        </div>

        {/* Contenido */}
      </div>
    </div>
  );
}
```

---

## 📝 TRADUCCIONES REQUERIDAS

Añadir en `app/config/i18n/modules/governance/compliance.ts`:

```typescript
classification: {
  title: {
    es: "Clasificación de Sistemas de Alto Riesgo",
    en: "High-Risk Systems Classification"
  },
  subtitle: {
    es: "Clasificador según EU AI Act Art. 6 y Anexo III",
    en: "Classifier according to EU AI Act Art. 6 and Annex III"
  },
  categories: {
    A3_1: {
      es: "Identificación y categorización biométrica",
      en: "Biometric identification and categorisation"
    }
    // ... más categorías
  }
}
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### **Frontend - Pantalla 1: Clasificador (Ya existe - Completar)**
- [x] Revisar pantalla existente
- [x] Añadir información del proyecto
- [x] Implementar sugerencia automática con IA (mock)
- [x] Completar selección de categorías (8 principales)
- [x] Implementar selección de subcategorías (multi-select)
- [x] Añadir validación de justificación (mínimo 100 caracteres)
- [x] Implementar clasificación y actualización de proyecto
- [x] Añadir trigger de workflow BPMN (mock)
- [x] Añadir mock data completo
- [x] Crear API routes mock
- [x] Implementar navegación CRUD completa
- [x] Añadir botón "Volver" y navegación

### **Frontend - Pantalla 2: Catálogo (Crear nueva)**
- [x] Crear página `annex-iii-categories/page.tsx`
- [x] Implementar listado de 8 categorías
- [x] Añadir vista expandible de subcategorías
- [x] Implementar búsqueda y filtros
- [x] Implementar CRUD completo (crear, editar, eliminar)
- [x] Añadir mock data
- [x] Crear API routes mock
- [x] Añadir entrada al menú

### **Frontend - Pantalla 3: Lista de Proyectos Clasificados**
- [x] Crear página `projects/page.tsx`
- [x] Implementar tabla con paginación
- [x] Añadir filtros (categoría, estado, búsqueda)
- [x] Implementar estadísticas
- [x] Añadir botones de acción (Nuevo, Editar)
- [x] Integrar navegación con pantalla de clasificación

### **Backend - Microservicio de Clasificación**
- [ ] Crear controlador REST `ClassificationController`
- [ ] Implementar endpoint `POST /api/compliance/classification/classify`
  - [ ] Validaciones de request
  - [ ] Lógica de clasificación
  - [ ] Actualización de entidad Project
  - [ ] Integración con workflow BPMN
- [ ] Implementar endpoint `POST /api/compliance/classification/suggest`
  - [ ] Integración con servicio de IA
  - [ ] Mapeo de respuesta a categorías
- [ ] Implementar endpoint `GET /api/compliance/classification/categories`
  - [ ] Filtros y paginación
  - [ ] Inclusión de subcategorías
- [ ] Implementar endpoint `GET /api/compliance/classification/projects`
  - [ ] Paginación avanzada
  - [ ] Filtros múltiples
  - [ ] Búsqueda full-text
- [ ] Implementar endpoint `GET /api/compliance/classification/projects/{id}`
  - [ ] Detalles completos
  - [ ] Información de modelos asociados
- [ ] Implementar endpoint `PUT /api/compliance/classification/projects/{id}`
  - [ ] Validaciones
  - [ ] Actualización de clasificación
  - [ ] Manejo de historial (si aplica)
- [ ] Implementar endpoint `GET /api/compliance/classification/projects/statistics`
  - [ ] Agregaciones por categoría
  - [ ] Agregaciones por estado
- [ ] Crear DTOs (Request/Response)
  - [ ] `ClassificationRequest`
  - [ ] `ClassificationResponse`
  - [ ] `AISuggestionRequest/Response`
  - [ ] `ClassifiedProjectsResponse`
  - [ ] `ClassificationStatisticsResponse`
- [ ] Implementar validaciones con Bean Validation
- [ ] Implementar manejo de errores estándar
- [ ] Integración con servicio de proyectos
- [ ] Integración con servicio BPMN
- [ ] Integración con servicio de IA
- [ ] Tests unitarios de endpoints
- [ ] Tests de integración

### **Backend - Servicios de Negocio**
- [ ] Crear `HighRiskClassifierBusinessService`
  - [ ] Método `classifySystem()`
  - [ ] Método `suggestCategoryWithAI()`
  - [ ] Método `validateJustificationQuality()`
  - [ ] Método `triggerHighRiskWorkflow()`
- [ ] Crear `AnnexIIICategoryService`
  - [ ] CRUD completo
  - [ ] Búsqueda y filtros
- [ ] Integrar con `ProjectService` para actualización

### **General**
- [x] Añadir traducciones completas (ES, EN, FR, DE, IT, PT)
- [x] Verificar estilos "Wow Factor"
- [x] Probar funcionalidad completa frontend
- [ ] Documentar integración con backend
- [ ] Crear documentación OpenAPI/Swagger
- [ ] Configurar autenticación/autorización en endpoints

---

## 🔗 REFERENCIAS

- **Documentación Compliance:** `docs/prompts/BUSINESS_LOGIC_COMPLIANCE.md`
- **Prompt Migración:** `docs/prompts/MIGRACION_COMPLIANCE_CLASSIFICATION.md`
- **Inventario Pantallas:** `docs/PANTALLAS_GOVERNANCE_COMPLIANCE_AI_ACT.md`
- **Artículo EU AI Act:** Art. 6 + Anexo III

---

**Última actualización:** Diciembre 2025
**Estado:** Listo para implementación en paralelo
