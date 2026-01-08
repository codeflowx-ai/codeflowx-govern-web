# 🏢 PROMPT DE IMPLEMENTACIÓN - COMPLIANCE POR SECTOR (Anexo I)

**Módulo:** Compliance - Sector Dashboard
**Artículo EU AI Act:** Anexo I - Sectores Regulados
**Fecha:** Diciembre 2025
**Estado:** ⏳ Pendiente de implementación completa
**Esfuerzo Estimado:** 3-4 días

---

## 📋 RESUMEN DEL MÓDULO

### **Objetivo**
Implementar el dashboard de compliance por sector regulado según el Anexo I del EU AI Act. El sistema debe mostrar métricas de compliance agrupadas por sector regulado, permitiendo identificar proyectos que pertenecen a sectores regulados y su estado de cumplimiento.

### **Pantallas Requeridas**

| # | Ruta Next.js | Estado | Descripción | Prioridad |
|---|--------------|-------|-------------|-----------|
| 1 | `app/(app)/governance/compliance/sector/page.tsx` | ⏳ Pendiente | Dashboard de compliance por sector | 🔴 Alta |

---

## 🏗️ ARQUITECTURA Y DEPENDENCIAS

### **Pantallas ZUL Originales**
- **ZUL Principal:** `console/gobierno/compliance/sector-dashboard.zul`
  - ViewModel: [Identificar en el código ZUL]
  - Ubicación: `src/main/webapp/console/gobierno/compliance/sector-dashboard.zul`

### **Entidades JPA**

#### **Project** - `com.codeflowx.govern.entity.projects.Project`
- **Tabla:** `PRJPROJECTS` (prefijo `PRJ`)
- **Campos relacionados con sector regulado:**
  - `PRJREGULATEDSECTOR` (Boolean) - Indica si pertenece a sector regulado (Anexo I)
  - `PRJANNEXILEGISLATION` (JSONB) - Array de legislaciones del Anexo I aplicables
    - Ejemplos: `["Reglamento (UE) 2017/745", "Directiva 2014/53/UE"]`
  - `PRJISHIGHRISK` (Boolean) - Si es sistema de alto riesgo (Art. 6)
  - `PRJANNEXIIICATEGORY` (String) - Categoría Anexo III si aplica
  - `PRJCOMPLIANCESTATUS` (String) - Estado de compliance (PENDING, IN_PROGRESS, COMPLIANT, NON_COMPLIANT)

#### **ComplianceAssessment** - `com.codeflowx.govern.entity.compliance.ComplianceAssessment`
- **Tabla:** `GOVCOMPLIANCEASSESSMENTS` (prefijo `GOV`)
- **Relación:** `idxproject` → `PRJPROJECTS.idxproject`
- **Campos relevantes:**
  - `idxproject` (Long) - Proyecto asociado
  - `comoverallscore` (BigDecimal) - Score overall de compliance (0.00 - 1.00)
  - `comreadytocertify` (Boolean) - Listo para certificación
  - `comstatus` (String) - Estado de evaluación

#### **FriaAssessment** - `com.codeflowx.govern.entity.compliance.FriaAssessment`
- **Tabla:** `GOVFRIAASSESSMENTS` (prefijo `GOV`)
- **Relación:** `idxproject` → `PRJPROJECTS.idxproject`
- **Campos relevantes:**
  - `idxproject` (Long) - Proyecto asociado
  - `friafinalrisk` (BigDecimal) - Riesgo final calculado
  - `friastatus` (String) - Estado de FRIA

### **Sectores Regulados (Anexo I)**

Según el Anexo I del EU AI Act, los sectores regulados incluyen:

1. **Dispositivos Médicos** - Reglamento (UE) 2017/745 (MDR)
2. **Equipos Radioeléctricos** - Directiva 2014/53/UE (RED)
3. **Maquinaria** - Directiva 2006/42/CE
4. **Vehículos de Motor** - Reglamento (UE) 2018/858
5. **Aviación** - Reglamento (UE) 2018/1139
6. **Ferrocarriles** - Reglamento (UE) 2016/796
7. **Productos de Construcción** - Reglamento (UE) 305/2011
8. **Equipos de Protección Individual** - Reglamento (UE) 2016/425

### **Business Services Disponibles**

#### **ProjectBusinessService**
- `findByRegulatedSector(Boolean regulatedSector)` - Buscar proyectos por sector regulado
- `findByAnnexILegislation(String legislation)` - Buscar proyectos por legislación Anexo I
- `getProjectsBySector()` - Agrupar proyectos por sector

#### **ComplianceAssessmentBusinessService**
- `getComplianceBySector()` - Obtener métricas de compliance agrupadas por sector
- `calculateSectorComplianceScore(String sector)` - Calcular score de compliance por sector

#### **FriaAssessmentBusinessService**
- `getFriaBySector()` - Obtener FRIAs agrupadas por sector
- `calculateSectorRisk(String sector)` - Calcular riesgo promedio por sector

### **Servicios CRUD**
- **ProjectService** - CRUD de proyectos
- **ComplianceAssessmentService** - CRUD de evaluaciones
- **FriaAssessmentService** - CRUD de FRIAs

---

## 📊 LÓGICA DE NEGOCIO - DASHBOARD POR SECTOR

### **1. Métricas Principales del Dashboard**

#### **1.1. Resumen por Sector Regulado**

**Método: `getSectorComplianceSummary()`**

```java
@Service
@Slf4j
public class SectorComplianceBusinessService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ComplianceAssessmentRepository complianceAssessmentRepository;

    @Autowired
    private FriaAssessmentRepository friaAssessmentRepository;

    /**
     * Obtiene resumen de compliance agrupado por sector regulado
     */
    public Map<String, SectorComplianceSummary> getSectorComplianceSummary() {
        log.info("Calculando resumen de compliance por sector");

        // 1. Obtener todos los proyectos en sectores regulados
        List<Project> regulatedProjects = projectRepository.findByPrjregulatedsector(true);

        // 2. Agrupar por legislación Anexo I
        Map<String, List<Project>> projectsBySector = regulatedProjects.stream()
            .filter(p -> p.getPrjannexilegislation() != null)
            .collect(Collectors.groupingBy(project -> {
                // Parsear JSONB y obtener primera legislación
                try {
                    List<String> legislations = objectMapper.readValue(
                        project.getPrjannexilegislation(),
                        new TypeReference<List<String>>() {}
                    );
                    return legislations.isEmpty() ? "UNKNOWN" : legislations.get(0);
                } catch (Exception e) {
                    return "UNKNOWN";
                }
            }));

        // 3. Calcular métricas por sector
        Map<String, SectorComplianceSummary> summary = new HashMap<>();

        for (Map.Entry<String, List<Project>> entry : projectsBySector.entrySet()) {
            String sector = entry.getKey();
            List<Project> projects = entry.getValue();

            SectorComplianceSummary sectorSummary = new SectorComplianceSummary();
            sectorSummary.setSector(sector);
            sectorSummary.setTotalProjects(projects.size());

            // Contar proyectos de alto riesgo
            long highRiskCount = projects.stream()
                .filter(p -> Boolean.TRUE.equals(p.getPrjishighrisk()))
                .count();
            sectorSummary.setHighRiskProjects(highRiskCount);

            // Obtener evaluaciones de compliance
            List<Long> projectIds = projects.stream()
                .map(Project::getIdxproject)
                .collect(Collectors.toList());

            List<ComplianceAssessment> assessments = complianceAssessmentRepository
                .findByIdxprojectIn(projectIds);

            // Calcular score promedio
            double avgScore = assessments.stream()
                .filter(a -> a.getComoverallscore() != null)
                .mapToDouble(a -> a.getComoverallscore().doubleValue())
                .average()
                .orElse(0.0);
            sectorSummary.setAverageComplianceScore(BigDecimal.valueOf(avgScore));

            // Contar listos para certificación
            long readyToCertify = assessments.stream()
                .filter(a -> Boolean.TRUE.equals(a.getComreadytocertify()))
                .count();
            sectorSummary.setReadyToCertify(readyToCertify);

            // Obtener FRIAs
            List<FriaAssessment> frias = friaAssessmentRepository
                .findByIdxprojectIn(projectIds);

            // Calcular riesgo promedio
            double avgRisk = frias.stream()
                .filter(f -> f.getFriafinalrisk() != null)
                .mapToDouble(f -> f.getFriafinalrisk().doubleValue())
                .average()
                .orElse(0.0);
            sectorSummary.setAverageRisk(BigDecimal.valueOf(avgRisk));

            summary.put(sector, sectorSummary);
        }

        return summary;
    }
}
```

**DTO de Respuesta:**
```java
public class SectorComplianceSummary {
    private String sector;
    private Integer totalProjects;
    private Long highRiskProjects;
    private BigDecimal averageComplianceScore;
    private Long readyToCertify;
    private BigDecimal averageRisk;
    // Getters y setters
}
```

#### **1.2. Proyectos por Sector con Estado de Compliance**

**Método: `getProjectsBySectorWithCompliance(String sector)`**

```java
/**
 * Obtiene proyectos de un sector específico con su estado de compliance
 */
public List<ProjectComplianceStatus> getProjectsBySectorWithCompliance(String sector) {
    log.info("Obteniendo proyectos del sector: {}", sector);

    // 1. Buscar proyectos del sector
    List<Project> projects = projectRepository.findByPrjregulatedsector(true)
        .stream()
        .filter(p -> {
            try {
                if (p.getPrjannexilegislation() == null) return false;
                List<String> legislations = objectMapper.readValue(
                    p.getPrjannexilegislation(),
                    new TypeReference<List<String>>() {}
                );
                return legislations.contains(sector);
            } catch (Exception e) {
                return false;
            }
        })
        .collect(Collectors.toList());

    // 2. Enriquecer con información de compliance
    List<ProjectComplianceStatus> result = new ArrayList<>();

    for (Project project : projects) {
        ProjectComplianceStatus status = new ProjectComplianceStatus();
        status.setProjectId(project.getIdxproject());
        status.setProjectName(project.getPrjname());
        status.setIsHighRisk(Boolean.TRUE.equals(project.getPrjishighrisk()));
        status.setAnnexIIICategory(project.getPrjannexiiicategory());

        // Obtener evaluación de compliance más reciente
        Optional<ComplianceAssessment> latestAssessment =
            complianceAssessmentRepository
                .findByIdxprojectOrderByComcreatedatDesc(project.getIdxproject())
                .stream()
                .findFirst();

        if (latestAssessment.isPresent()) {
            ComplianceAssessment assessment = latestAssessment.get();
            status.setComplianceScore(assessment.getComoverallscore());
            status.setReadyToCertify(Boolean.TRUE.equals(assessment.getComreadytocertify()));
            status.setComplianceStatus(assessment.getComstatus());
        } else {
            status.setComplianceStatus("PENDING");
        }

        // Obtener FRIA más reciente
        Optional<FriaAssessment> latestFria = friaAssessmentRepository
            .findByIdxprojectOrderByFriacreatedatDesc(project.getIdxproject())
            .stream()
            .findFirst();

        if (latestFria.isPresent()) {
            status.setFriaRisk(latestFria.get().getFriafinalrisk());
            status.setFriaStatus(latestFria.get().getFriastatus());
        }

        result.add(status);
    }

    return result;
}
```

**DTO de Respuesta:**
```java
public class ProjectComplianceStatus {
    private Long projectId;
    private String projectName;
    private Boolean isHighRisk;
    private String annexIIICategory;
    private BigDecimal complianceScore;
    private Boolean readyToCertify;
    private String complianceStatus;
    private BigDecimal friaRisk;
    private String friaStatus;
    // Getters y setters
}
```

### **2. Consultas BBDD Requeridas**

#### **2.1. Proyectos por Sector Regulado**

```sql
-- Obtener proyectos en sectores regulados con su legislación Anexo I
SELECT
    p.idxproject,
    p.prjname,
    p.prjregulatedsector,
    p.prjannexilegislation,
    p.prjishighrisk,
    p.prjannexiiicategory,
    p.prjcompliancestatus
FROM prjprojects p
WHERE p.prjregulatedsector = true
    AND p.prjannexilegislation IS NOT NULL
ORDER BY p.prjname;
```

#### **2.2. Métricas de Compliance por Sector**

```sql
-- Calcular métricas de compliance agrupadas por sector (legislación Anexo I)
WITH sector_projects AS (
    SELECT
        p.idxproject,
        p.prjname,
        jsonb_array_elements_text(p.prjannexilegislation) AS sector
    FROM prjprojects p
    WHERE p.prjregulatedsector = true
        AND p.prjannexilegislation IS NOT NULL
)
SELECT
    sp.sector,
    COUNT(DISTINCT sp.idxproject) AS total_projects,
    COUNT(DISTINCT CASE WHEN p.prjishighrisk = true THEN sp.idxproject END) AS high_risk_projects,
    AVG(ca.comoverallscore) AS avg_compliance_score,
    COUNT(DISTINCT CASE WHEN ca.comreadytocertify = true THEN ca.idxcomplianceassessment END) AS ready_to_certify,
    AVG(fa.friafinalrisk) AS avg_fria_risk
FROM sector_projects sp
LEFT JOIN prjprojects p ON p.idxproject = sp.idxproject
LEFT JOIN govcomplianceassessments ca ON ca.idxproject = p.idxproject
LEFT JOIN govfriaassessments fa ON fa.idxproject = p.idxproject
GROUP BY sp.sector
ORDER BY sp.sector;
```

#### **2.3. Proyectos sin Compliance Assessment**

```sql
-- Proyectos en sectores regulados sin evaluación de compliance
SELECT
    p.idxproject,
    p.prjname,
    p.prjannexilegislation,
    p.prjishighrisk
FROM prjprojects p
WHERE p.prjregulatedsector = true
    AND p.prjishighrisk = true
    AND NOT EXISTS (
        SELECT 1
        FROM govcomplianceassessments ca
        WHERE ca.idxproject = p.idxproject
    )
ORDER BY p.prjname;
```

---

## 🎨 IMPLEMENTACIÓN FRONTEND

### **Estructura de la Pantalla**

**Ubicación:** `app/(app)/governance/compliance/sector/page.tsx`

### **Componentes Principales**

1. **Header con Título y Descripción**
   - Título: "Compliance por Sector Regulado"
   - Descripción: "Métricas de cumplimiento agrupadas por sectores regulados según Anexo I del EU AI Act"

2. **Cards de Métricas Generales**
   - Total de sectores regulados
   - Total de proyectos en sectores regulados
   - Proyectos de alto riesgo
   - Score promedio de compliance

3. **Tabla/Gráfico de Sectores**
   - Lista de sectores con métricas:
     - Nombre del sector (legislación)
     - Total de proyectos
     - Proyectos de alto riesgo
     - Score promedio de compliance
     - Proyectos listos para certificación
     - Riesgo promedio (FRIA)

4. **Tabla de Proyectos por Sector**
   - Filtro por sector
   - Columnas:
     - Nombre del proyecto
     - Alto riesgo (Sí/No)
     - Categoría Anexo III
     - Score de compliance
     - Estado de compliance
     - Riesgo FRIA
     - Acciones (Ver detalle, Editar)

### **Mock Data**

**Ubicación:** `app/mocks/governance/compliance/sector.json`

```json
{
  "summary": {
    "totalSectors": 8,
    "totalProjects": 45,
    "highRiskProjects": 23,
    "averageComplianceScore": 0.78
  },
  "sectors": [
    {
      "sector": "Reglamento (UE) 2017/745",
      "sectorName": "Dispositivos Médicos",
      "totalProjects": 12,
      "highRiskProjects": 8,
      "averageComplianceScore": 0.82,
      "readyToCertify": 5,
      "averageRisk": 0.65
    },
    {
      "sector": "Directiva 2014/53/UE",
      "sectorName": "Equipos Radioeléctricos",
      "totalProjects": 8,
      "highRiskProjects": 4,
      "averageComplianceScore": 0.75,
      "readyToCertify": 3,
      "averageRisk": 0.58
    }
  ],
  "projects": [
    {
      "projectId": 1,
      "projectName": "Sistema de Diagnóstico Médico IA",
      "sector": "Reglamento (UE) 2017/745",
      "isHighRisk": true,
      "annexIIICategory": "A3_1",
      "complianceScore": 0.85,
      "readyToCertify": true,
      "complianceStatus": "COMPLIANT",
      "friaRisk": 0.62,
      "friaStatus": "COMPLETED"
    }
  ]
}
```

### **API Routes**

**Ubicación:** `app/api/governance/compliance/sector/route.ts`

```typescript
import { NextRequest, NextResponse } from 'next/server';
import { USE_MOCK } from '@/app/config/mock';

export async function GET(request: NextRequest) {
  if (USE_MOCK) {
    const mockData = await import('@/mocks/governance/compliance/sector.json');
    return NextResponse.json(mockData.default);
  }

  // Llamada real a backend
  const response = await fetch('http://backend/api/v1/compliance/sector/summary');
  return NextResponse.json(await response.json());
}
```

**Endpoint específico por sector:**
`app/api/governance/compliance/sector/[sector]/route.ts`

```typescript
export async function GET(
  request: NextRequest,
  { params }: { params: { sector: string } }
) {
  const sector = decodeURIComponent(params.sector);

  if (USE_MOCK) {
    const mockData = await import('@/mocks/governance/compliance/sector.json');
    const projects = mockData.default.projects.filter(
      p => p.sector === sector
    );
    return NextResponse.json({ projects });
  }

  // Llamada real a backend
  const response = await fetch(
    `http://backend/api/v1/compliance/sector/${encodeURIComponent(sector)}/projects`
  );
  return NextResponse.json(await response.json());
}
```

---

## 🔧 IMPLEMENTACIÓN BACKEND

### **1. Crear Business Service**

**Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/SectorComplianceBusinessService.java`

```java
@Service
@Slf4j
public class SectorComplianceBusinessService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ComplianceAssessmentRepository complianceAssessmentRepository;

    @Autowired
    private FriaAssessmentRepository friaAssessmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public Map<String, SectorComplianceSummary> getSectorComplianceSummary() {
        // Implementación según sección 1.1
    }

    public List<ProjectComplianceStatus> getProjectsBySectorWithCompliance(String sector) {
        // Implementación según sección 1.2
    }
}
```

### **2. Crear DTOs**

**Ubicación:** `codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/`

- `SectorComplianceSummaryDto.java`
- `ProjectComplianceStatusDto.java`

### **3. Crear Controller en BFF**

**Ubicación:** `codeflowx.govern.bff.compliance/src/main/java/com/codeflowx/govern/bff/compliance/controller/SectorComplianceController.java`

```java
@RestController
@RequestMapping("/api/v1/compliance/sector")
@RequiredArgsConstructor
@Slf4j
public class SectorComplianceController {

    private final SectorComplianceBusinessService sectorComplianceBusinessService;

    @GetMapping("/summary")
    public Mono<ResponseEntity<Map<String, SectorComplianceSummaryDto>>> getSummary() {
        return Mono.fromCallable(() -> {
                Map<String, SectorComplianceSummary> summary =
                    sectorComplianceBusinessService.getSectorComplianceSummary();
                // Convertir a DTOs
                Map<String, SectorComplianceSummaryDto> dtoMap = summary.entrySet().stream()
                    .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> toDto(e.getValue())
                    ));
                return ResponseEntity.ok(dtoMap);
            })
            .subscribeOn(Schedulers.boundedElastic())
            .onErrorResume(error -> {
                log.error("Error obteniendo resumen por sector", error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    @GetMapping("/{sector}/projects")
    public Mono<ResponseEntity<List<ProjectComplianceStatusDto>>> getProjectsBySector(
            @PathVariable String sector) {
        return Mono.fromCallable(() -> {
                List<ProjectComplianceStatus> projects =
                    sectorComplianceBusinessService.getProjectsBySectorWithCompliance(sector);
                List<ProjectComplianceStatusDto> dtos = projects.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
                return ResponseEntity.ok(dtos);
            })
            .subscribeOn(Schedulers.boundedElastic())
            .onErrorResume(error -> {
                log.error("Error obteniendo proyectos del sector: {}", sector, error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }
}
```

### **4. Configurar URL en BFF**

**Agregar en `application.yml` del BFF:**

```yaml
services:
  # ... otros servicios ...
  # Nota: Este servicio usa repositorios directamente, no requiere microservicio separado
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### **Fase 1: Backend (Business Service)**
- [ ] Crear `SectorComplianceBusinessService` en `codeflowx.govern.business`
- [ ] Implementar método `getSectorComplianceSummary()`
- [ ] Implementar método `getProjectsBySectorWithCompliance()`
- [ ] Crear consultas SQL necesarias
- [ ] Crear DTOs: `SectorComplianceSummaryDto`, `ProjectComplianceStatusDto`
- [ ] Agregar tests unitarios

### **Fase 2: Backend (BFF)**
- [ ] Crear `SectorComplianceController` en BFF
- [ ] Implementar endpoint `GET /api/v1/compliance/sector/summary`
- [ ] Implementar endpoint `GET /api/v1/compliance/sector/{sector}/projects`
- [ ] Agregar manejo de errores
- [ ] Agregar logging

### **Fase 3: Frontend**
- [ ] Crear página `app/(app)/governance/compliance/sector/page.tsx`
- [ ] Implementar cards de métricas generales
- [ ] Implementar tabla/gráfico de sectores
- [ ] Implementar tabla de proyectos por sector
- [ ] Agregar filtros y búsqueda
- [ ] Crear mock data en `app/mocks/governance/compliance/sector.json`
- [ ] Crear API routes mock
- [ ] Agregar traducciones (español/inglés)
- [ ] Agregar entrada al menú en `app/config/modules.ts`
- [ ] Aplicar estilos "Wow Factor"

### **Fase 4: Validación**
- [ ] Verificar que los datos se muestran correctamente
- [ ] Verificar filtros y búsqueda
- [ ] Verificar navegación a detalles de proyecto
- [ ] Verificar responsive design
- [ ] Verificar desactivación de mock (cambiar `NEXT_PUBLIC_USE_MOCK=false`)

---

## 📚 REFERENCIAS

### **Documentación Funcional**
- `suinsit.nova.web/docs/funcional/compliance/01_REORGANIZACION_PANTALLAS_COMPLIANCE.md`
- `codeflowx-studio/docs/prompts/BUSINESS_LOGIC_COMPLIANCE.md`
- `codeflowx-studio/docs/prompts/BUSINESS_LOGIC_PROJECTS.md`

### **Arquitectura**
- `codeflowx-studio/docs/ARQUITECTURA_FRONTEND.md`
- `codeflowx-studio/docs/prompts/MIGRACION_COMPLIANCE_DASHBOARDS.md`

### **Entidades JPA**
- `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/projects/Project.java`
- `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/ComplianceAssessment.java`
- `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/FriaAssessment.java`

### **EU AI Act**
- **Anexo I:** Sectores Regulados
- **Art. 6:** Clasificación de Sistemas de Alto Riesgo
- **Art. 27:** Evaluaciones de Impacto en Derechos Fundamentales (FRIA)
- **Art. 43:** Evaluaciones de Conformidad

---

**Última actualización:** Diciembre 2025
**Mantenido por:** Equipo de Desarrollo CodeflowX
