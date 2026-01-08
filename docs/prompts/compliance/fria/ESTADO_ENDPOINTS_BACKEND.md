# Estado de Endpoints Backend para Pantallas FRIA

## 📋 Resumen

Este documento detalla el estado de implementación de los endpoints de backend y la lógica de negocio para las nuevas pantallas FRIA.

## ✅ Endpoints Implementados

### 1. Endpoints Básicos de FRIA (Ya existentes)

#### BFF Controller: `FriaController.java`
- ✅ `POST /api/v1/fria/create` - Crear nueva evaluación FRIA
- ✅ `PUT /api/v1/fria/{friaId}/step/{stepNumber}` - Actualizar paso del wizard
- ✅ `POST /api/v1/fria/{friaId}/calculate-risk` - Calcular riesgo final
- ✅ `POST /api/v1/fria/{friaId}/notify-authority` - Notificar a autoridades
- ✅ `POST /api/v1/fria/{friaId}/cross-validate` - Validación cruzada
- ✅ `GET /api/v1/fria/{friaId}` - Obtener detalle de FRIA
- ✅ `GET /api/v1/fria/assessments` - Listar evaluaciones FRIA (con filtros)

### 2. Lógica de Negocio Implementada

- ✅ `FriaService` - Servicio BFF que agrega datos de múltiples microservicios
- ✅ `FriaAssessmentBusinessService` - Lógica de negocio en el microservicio FRIA
- ✅ Integración con Python (`leka-fria-generator` y `codeflowx-governance-api`)
- ✅ Resilience4j (Circuit Breaker, Retry, TimeLimiter)

## ❌ Endpoints Faltantes para Nueva Pantalla de Proyectos

### Pantalla: `/governance/compliance/fria/projects`

**Estado actual del frontend:**
- Usa datos mock (`mockProjectsWithFrias`)
- No hace llamadas a API reales

**Endpoint necesario:**

```
GET /api/v1/fria/projects
```

**Parámetros:**
- `page` (int, default: 0)
- `size` (int, default: 20)
- `status` (String, optional) - Filtrar por estado de última FRIA
- `search` (String, optional) - Búsqueda por nombre/descripción/categoría

**Respuesta esperada:**
```json
{
  "projects": [
    {
      "id": 1001,
      "name": "AI Credit Scoring System",
      "description": "Sistema de evaluación crediticia basado en IA",
      "category": "B.1",
      "totalFrias": 3,
      "latestFria": {
        "id": 1,
        "projectId": 1001,
        "version": "v2.1",
        "createdAt": "2025-12-15T10:00:00Z",
        "updatedAt": "2025-12-15T14:30:00Z",
        "status": "NOTIFIED",
        "completenessScore": 1.0,
        "finalRisk": 0.85,
        "riskLevel": "high",
        "notified": true,
        "notificationDate": "2025-12-15T14:30:00Z",
        "approved": true,
        "approvalDate": "2025-12-15T12:00:00Z",
        "createdBy": "Juan Pérez"
      },
      "frias": [
        // Lista completa de todas las evaluaciones FRIA del proyecto
      ]
    }
  ],
  "pagination": {
    "page": 0,
    "size": 20,
    "totalElements": 3,
    "totalPages": 1
  },
  "statistics": {
    "totalProjects": 3,
    "totalFrias": 6,
    "notified": 2,
    "draft": 1
  }
}
```

## 🔧 Implementación Requerida

### 1. DTO de Respuesta

**Archivo:** `codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/FriaProjectsListResponseDto.java`

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriaProjectsListResponseDto {
    private List<ProjectWithFriasDto> projects;
    private PaginationDto pagination;
    private FriaProjectsStatisticsDto statistics;
}
```

### 2. DTO de Proyecto con FRIA

**Archivo:** `codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/ProjectWithFriasDto.java`

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectWithFriasDto {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Integer totalFrias;
    private FriaAssessmentSummaryDto latestFria;
    private List<FriaAssessmentSummaryDto> frias;
}
```

### 3. Endpoint en BFF Controller

**Archivo:** `codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/controller/FriaController.java`

```java
@GetMapping("/projects")
@Operation(
    summary = "Listar proyectos con evaluaciones FRIA",
    description = "Obtiene una lista paginada de proyectos con sus evaluaciones FRIA agrupadas."
)
public Mono<ResponseEntity<FriaProjectsListResponseDto>> listProjects(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size,
    @RequestParam(required = false) String status,
    @RequestParam(required = false) String search) {

    return friaService.listProjectsWithFrias(page, size, status, search)
        .map(ResponseEntity::ok)
        .onErrorResume(error -> {
            log.error("Error listando proyectos con FRIA", error);
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        });
}
```

### 4. Método en FriaService

**Archivo:** `codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/service/FriaService.java`

```java
/**
 * Lista proyectos con sus evaluaciones FRIA agrupadas.
 * Llama a: FRIA Service + Project Service
 */
Mono<FriaProjectsListResponseDto> listProjectsWithFrias(int page, int size,
                                                       String status, String search);
```

### 5. Implementación en FriaServiceImpl

**Archivo:** `codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/service/impl/FriaServiceImpl.java`

La implementación debe:
1. Obtener lista de proyectos (del Project Service)
2. Para cada proyecto, obtener sus evaluaciones FRIA (del FRIA Service)
3. Agrupar y ordenar evaluaciones por fecha
4. Calcular estadísticas
5. Aplicar filtros (status, search)
6. Paginar resultados

### 6. Endpoint en Microservicio FRIA (opcional)

Si se necesita un endpoint específico en el microservicio FRIA:

**Archivo:** `codeflowx-governance-fria-service/src/main/java/com/codeflowx/govern/fria/controller/FriaController.java`

```java
@GetMapping("/by-project/{projectId}")
public Mono<ResponseEntity<List<FriaAssessmentDto>>> getFriasByProject(
    @PathVariable Long projectId) {
    // Retorna todas las evaluaciones FRIA de un proyecto
}
```

## 📝 Notas

1. **Frontend actual:** La pantalla de proyectos usa datos mock y necesita ser actualizada para llamar al nuevo endpoint.

2. **Agregación de datos:** El BFF debe agregar datos de:
   - Project Service (información de proyectos)
   - FRIA Service (evaluaciones FRIA)
   - Posiblemente Classification Service (categorías de proyectos)

3. **Performance:** Considerar caché para proyectos que no cambian frecuentemente.

4. **Filtros:** El endpoint debe soportar filtros por:
   - Estado de la última FRIA
   - Búsqueda por nombre/descripción/categoría del proyecto

## 🎯 Prioridad

**ALTA** - La pantalla de proyectos es la única visible en el sidebar y es crítica para la gestión de evaluaciones FRIA.
