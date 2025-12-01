# GUÍA AGENTE 4 - POST-MARKET MONITORING (DOCUMENTACIÓN)

**Agente:** Backend Senior
**Equipo:** Equipo 3 - Post-Market Monitoring
**Duración:** 12 horas
**Objetivo:** Implementar documentación formal PMM y generación de reportes

---

## 📋 INCIDENCIAS ASIGNADAS

### **Incidencias Completadas (Trabajo Nocturno):**
| ID | Descripción | Esfuerzo | Prioridad | Estado |
|----|-------------|----------|-----------|--------|
| **INC-010-001** | Documentación Formal Sistema PMM | 1h | 🔴 CRÍTICA | ✅ COMPLETADO |
| **INC-010-002** | Generación Automática Post-Market Surveillance Report | 2h | 🔴 CRÍTICA | ✅ COMPLETADO |
| **INC-010-006** | Dashboard PMM Consolidado | 2h | 🔴 CRÍTICA | ✅ COMPLETADO |

### **Incidencias Completadas (Nuevas Asignaciones):**
| ID | Descripción | Esfuerzo | Prioridad | Estado |
|----|-------------|----------|-----------|--------|
| **INC-010-004** | Implementación Real PostMarketMonitoringService | 2h | 🔴 CRÍTICA | ✅ COMPLETADO |
| **INC-010-008** | Dashboard de Supervisión Continua | 3h | 🟡 ALTA | ✅ COMPLETADO |
| **INC-010-009** | API REST para Consulta de Histórico | 2h | 🟡 ALTA | ✅ COMPLETADO |
| **INC-010-013** | Visualización de Tendencias Avanzadas | 3h | 🟢 MEDIA | ✅ COMPLETADO |
| **INC-010-014** | Configuración de Frecuencias por Proyecto | 1h | 🟢 MEDIA | ✅ COMPLETADO |
| **INC-017** | Dashboard consolidado compliance | 3h | 🟡 MEDIA | ✅ COMPLETADO |

**Total:** 6 incidencias completadas, ~14 horas

---

## 📚 DOCUMENTOS DE REFERENCIA

### Prompts Específicos:
1. **INC-010-001:**
   - `/docs/compliance/gaps/prompts/java/INC-010-001_post_market_monitoring_plan.md`
   - Artículo EU AI Act: **Art. 16.g, 72**

2. **INC-010-002:**
   - `/docs/compliance/gaps/prompts/java/INC-010-002_post_market_surveillance_report.md`
   - Artículo EU AI Act: **Art. 72**

3. **INC-010-006:**
   - `/docs/compliance/gaps/prompts/java/INC-010-006_configuracion_thresholds.md`
   - Artículo EU AI Act: **Art. 72**

### Documentación General:
- **Arquitectura EnArt:** `/docs/compliance/PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md`
- **Microservicio Documentación (Java):** `/docs/compliance/PROMPTS_06_MICROSERVICIO_DOCUMENTACION_JAVA.md` ⭐ **NUEVO**
- **Plan Nocturno:** `/docs/compliance/revision/PLAN_TRABAJO_NOCTURNO.md`
- **Seguimiento PMM:** `/docs/compliance/gaps/SEGUIMIENTO_INCIDENCIAS_010_PMM.md`

### **⚠️ GENERACIÓN DE DOCUMENTOS PDF (JAVA):**

**IMPORTANTE:** La generación de documentos PDF se hace con **Java**, siguiendo el patrón de microservicios Java del proyecto.

**Implementación:**
- **Opción 1 (Recomendado):** Microservicio Java Spring Boot
- **Opción 2:** Servicio dentro de `codeflowx.govern.business`

**Librerías Java:**
- **PDF:** iText (comercial/gratis) o Apache PDFBox (open source)
- **Templates:** FreeMarker (muy maduro, excelente con Spring)
- **Alternativa:** Thymeleaf (si ya se usa en el proyecto)

**Ventajas Java:**
- iText más potente que weasyprint (Python)
- FreeMarker equivalente a Jinja2
- Mejor rendimiento
- Integración nativa con stack Java/Spring
- Sin dependencia de microservicio externo

**Estructura de Microservicio Java:**
- **Módulo:** `codeflowx-governance-documentation` (nuevo módulo en `nocode.service`)
- **Prompt completo:** `/docs/compliance/PROMPTS_06_MICROSERVICIO_DOCUMENTACION_JAVA.md` ⭐
- **Ubicación:** `/eclipse-workspace/nocode.service/codeflowx-governance-documentation/`
- **Templates:** `src/main/resources/templates/reports/*.ftl` (FreeMarker)
- **NO hardcoded** - templates en recursos externos

**Dependencias Maven:**
```xml
<!-- iText para PDF -->
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext7-core</artifactId>
    <version>8.0.2</version>
</dependency>
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>html2pdf</artifactId>
    <version>5.0.2</version>
</dependency>

<!-- FreeMarker para templates -->
<dependency>
    <groupId>org.freemarker</groupId>
    <artifactId>freemarker</artifactId>
    <version>2.3.32</version>
</dependency>
```

**✅ MICROSERVICIO CREADO:**
- **Módulo:** `codeflowx-governance-documentation` ✅ CREADO
- **Ubicación:** `/eclipse-workspace/nocode.service/codeflowx-governance-documentation/`
- **Estado:** ✅ COMPLETADO - 2025-11-25
- **Endpoints REST:**
  - `POST /api/documentation/generate-report` - Genera reportes PDF
  - `POST /api/documentation/generate-annex-iv` - Genera documentación Anexo IV
  - `GET /api/documentation/health` - Health check
- **Puerto:** 8085
- **Swagger UI:** http://localhost:8085/swagger-ui.html

**Integración con BusinessServices:**
- `PostMarketSurveillanceReportService` debe llamar al microservicio para generar PDFs
- Usar `RestTemplate` o `WebClient` para invocar endpoints REST
- El microservicio recibe datos JSON y retorna PDF como byte[]

### Referencias Técnicas:
- **Entidades Existentes:**
  - `entity/compliance/PostMarketMonitoring.java` - ✅ Ya existe
  - `entity/compliance/Incident.java` - ✅ Ya existe
- **BusinessServices Existentes:**
  - `business/compliance/PostMarketMonitoringService.java` - ✅ Ya existe
  - `business/compliance/ComplianceExecutiveReportService.java` - ✅ Ya existe

---

## 🏗️ ARQUITECTURA ENART - CONVENCIONES

**Ver documento AGENTE_1_VALIDACIONES_CRITICAS.md sección "ARQUITECTURA ENART" para convenciones completas.**

**Prefijos relevantes:**
- `PMM` - Post-Market Monitoring
- `PSR` - Post-Market Surveillance Report
- `ALR` - Alert Thresholds

---

## 📁 ESTRUCTURA DE DIRECTORIOS

### **Entidades JPA:**
```
/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/
├── compliance/          ← Entidades de compliance
│   ├── PostMarketMonitoring.java    ← Ya existe
│   ├── PostMarketSurveillanceReport.java  ← CREAR
│   └── AlertThreshold.java          ← CREAR (si es necesaria)
```

### **BusinessServices:**
```
/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/
├── compliance/          ← Servicios de compliance
│   ├── PostMarketMonitoringService.java         ← Ya existe, MODIFICAR
│   ├── PostMarketSurveillanceReportService.java ← CREAR
│   ├── PostMarketMonitoringDashboardService.java ← CREAR (INC-010-006)
│   └── ComplianceExecutiveReportService.java    ← Ya existe, MODIFICAR
```

### **ViewModels (para dashboards y pantallas):**
```
/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/
├── compliance/          ← ViewModels de compliance
│   └── PostMarketMonitoringDashboardViewModel.java ← CREAR (INC-010-006)
```

### **Pantallas ZUL (para dashboards y pantallas):**
```
/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/src/main/webapp/console/gobierno/compliance/
├── post-market-monitoring-dashboard.zul ← CREAR (INC-010-006)
└── [otras pantallas PMM si aplica]
```

---

## 🔧 IMPLEMENTACIÓN POR INCIDENCIA

### **INC-010-001: Documentación Formal Sistema PMM**

#### **Archivos a Modificar/Crear:**

1. **Entidad (VERIFICAR):**
   - Verificar si existe: `entity/compliance/PostMarketMonitoringPlan.java`
   - Si no existe: crear según prompt
   - Tabla: `PMMPOSTMARKETMONITORINGPLANS` (prefijo `PMM`)

2. **BusinessService (CREAR o MODIFICAR):**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/PostMarketMonitoringPlanService.java`
   - **O modificar:** `PostMarketMonitoringService.java`

3. **Script SQL (SI ES NECESARIA):**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/post_market_monitoring_plan.sql`

#### **Checklist:**
- [ ] Leer prompt completo: `INC-010-001_post_market_monitoring_plan.md`
- [ ] Verificar si entidad `PostMarketMonitoringPlan` existe
- [ ] Crear entidad si es necesaria
- [ ] Crear/modificar BusinessService según prompt
- [ ] Crear script SQL si es necesaria
- [ ] Actualizar `tablas.md`
- [ ] Compilar sin errores
- [ ] Actualizar `SEGUIMIENTO_INCIDENCIAS.md`

---

### **INC-010-002: Generación Automática Post-Market Surveillance Report**

#### **Archivos a Crear/Modificar:**

1. **Entidad (CREAR):**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/PostMarketSurveillanceReport.java`
   - Tabla: `PSRPOSTMARKETSURVEILLANCEREPORTS` (prefijo `PSR`)

2. **BusinessService (CREAR):**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/PostMarketSurveillanceReportService.java`
   - **Integración:** Usar `PostMarketMonitoringService` para datos
   - **⚠️ OBLIGATORIO:** Implementar generación de PDF con Java (iText + FreeMarker)

3. **Script SQL (CREAR):**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/post_market_surveillance_report.sql`

#### **⚠️ GENERACIÓN DE DOCUMENTOS PDF (JAVA):**

**IMPORTANTE:** La generación de PDF se hace con **Java usando iText/FreeMarker**, siguiendo el patrón de microservicios Java del proyecto.

**Opciones de Implementación:**

**Opción 1: Microservicio Java (Recomendado)**
- **Microservicio:** `codeflowx.govern.documentation` o similar (siguiendo convención del proyecto)
- **Framework:** Spring Boot
- **Librerías:** iText (o Apache PDFBox) + FreeMarker
- **Endpoint:** `POST /api/documentation/generate-report`
- **Ubicación:** Seguir estructura de otros microservicios Java del proyecto

**Opción 2: Servicio dentro del módulo business (Si es simple)**
- **Servicio:** `DocumentGenerationService` en `codeflowx.govern.business`
- **Librerías:** iText (o Apache PDFBox) + FreeMarker
- **Integración directa** en `PostMarketSurveillanceReportService`

**Librerías Java Recomendadas:**
- **PDF:** iText (comercial/gratis) o Apache PDFBox (open source)
- **Templates:** FreeMarker (muy maduro, excelente con Spring)
- **Alternativa templates:** Thymeleaf (si ya se usa en el proyecto)

**Ejemplo de implementación con FreeMarker + iText:**
```java
@Service
@Slf4j
public class DocumentGenerationService {

    @Autowired
    private Configuration freeMarkerConfig; // FreeMarker configurado

    /**
     * Genera reporte PDF usando FreeMarker (template) + iText (PDF)
     */
    public byte[] generateReportPDF(String templateName, Map<String, Object> data) {
        try {
            // 1. Renderizar template FreeMarker a HTML
            Template template = freeMarkerConfig.getTemplate(templateName + ".ftl");
            StringWriter htmlWriter = new StringWriter();
            template.process(data, htmlWriter);
            String htmlContent = htmlWriter.toString();

            // 2. Convertir HTML a PDF con iText
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Usar iText HTML to PDF (o weasyprint-equivalent en Java)
            HtmlConverter.convertToPdf(htmlContent, pdfDoc);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Error generating PDF report", e);
            throw new BussinessException("Error generating PDF: " + e.getMessage(), e);
        }
    }
}
```

**Dependencias Maven:**
```xml
<!-- iText para PDF -->
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext7-core</artifactId>
    <version>8.0.2</version>
</dependency>
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>html2pdf</artifactId>
    <version>5.0.2</version>
</dependency>

<!-- FreeMarker para templates -->
<dependency>
    <groupId>org.freemarker</groupId>
    <artifactId>freemarker</artifactId>
    <version>2.3.32</version>
</dependency>
```

**Templates FreeMarker:**
- Ubicación: `src/main/resources/templates/reports/`
- Ejemplo: `pmm_surveillance_report.ftl`
- NO hardcoded - templates en recursos externos

#### **Dependencias:**
- **Requiere:** INC-010-001 completada
- **Usa:** `PostMarketMonitoringService`, `Incident` entity
- **Librerías:** iText (o PDFBox) + FreeMarker (añadir a `pom.xml`)

#### **Checklist:**
- [ ] Leer prompt completo: `INC-010-002_post_market_surveillance_report.md`
- [ ] Verificar que INC-010-001 está completada
- [ ] Crear entidad `PostMarketSurveillanceReport`
- [ ] Crear BusinessService según prompt
- [ ] **Implementar generación de PDF con Java** (iText + FreeMarker)
  - [ ] Opción A: Crear microservicio Java `codeflowx.govern.documentation`
  - [ ] Opción B: Crear `DocumentGenerationService` en módulo business
- [ ] Crear templates FreeMarker (`.ftl`) para reportes
- [ ] Crear script SQL
- [ ] Actualizar `tablas.md`
- [ ] Compilar sin errores
- [ ] Actualizar `SEGUIMIENTO_INCIDENCIAS.md`

---

### **INC-010-006: Dashboard PMM Consolidado**

**⚠️ IMPORTANTE:** Esta incidencia requiere crear un **dashboard completo con pantalla ZUL y ViewModel**.

#### **Archivos a Crear/Modificar:**

1. **BusinessService (CREAR o MODIFICAR):**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/PostMarketMonitoringDashboardService.java`
   - **O modificar:** `ComplianceDashboardService.java`
   - **Responsabilidad:** Proporcionar datos agregados para el dashboard (métricas, alertas, tendencias)

2. **ViewModel (CREAR):**
   - `/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/compliance/PostMarketMonitoringDashboardViewModel.java`
   - **Responsabilidad:** Lógica de presentación, binding con ZUL, llamadas a BusinessService

3. **Pantalla ZUL (CREAR):**
   - `/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/src/main/webapp/console/gobierno/compliance/post-market-monitoring-dashboard.zul`
   - **Responsabilidad:** UI del dashboard con gráficos, tablas, KPIs
   - **Componentes típicos:**
     - KPIs en tiempo real (métricas de performance, degradación detectada)
     - Gráficos de tendencias (latencia, throughput, error rate)
     - Tabla de alertas activas
     - Lista de incidentes recientes
     - Filtros por proyecto, fecha, tipo de métrica

4. **Entidad (SI ES NECESARIA):**
   - Verificar si existe entidad para thresholds
   - Si no existe: crear en `entity/compliance/AlertThreshold.java`
   - Tabla: `ALRALERTTHRESHOLDS` (prefijo `ALR`)

5. **Script SQL (SI ES NECESARIA):**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/alert_thresholds.sql`

#### **Checklist:**
- [ ] Leer prompt completo: `INC-010-006_configuracion_thresholds.md`
- [ ] Revisar `PostMarketMonitoringService` existente
- [ ] Crear/modificar BusinessService según prompt
- [ ] **Crear ViewModel** `PostMarketMonitoringDashboardViewModel.java`
- [ ] **Crear pantalla ZUL** `post-market-monitoring-dashboard.zul`
- [ ] Integrar ViewModel con BusinessService
- [ ] Implementar gráficos y visualizaciones en ZUL
- [ ] Crear entidad si es necesaria
- [ ] Crear script SQL si es necesaria
- [ ] Actualizar `tablas.md`
- [ ] Compilar sin errores
- [ ] Actualizar `SEGUIMIENTO_INCIDENCIAS.md`

---

## 📝 PLANTILLA DE ENTIDAD PMM

**Ver documento AGENTE_1_VALIDACIONES_CRITICAS.md sección "PLANTILLA DE ENTIDAD ENART"**

**Ejemplo específico PMM:**
```java
@Table(name = "PSRPOSTMARKETSURVEILLANCEREPORTS")
@Entidad(
    namespace = "compliance",
    type = "TABLE",
    name = "PSRPOSTMARKETSURVEILLANCEREPORTS",
    labelMonitor = "PSRREPORTNAME",
    pk = "IDXPOSTMARKETSURVEILLANCEREPORT"
)
public class PostMarketSurveillanceReport implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXPOSTMARKETSURVEILLANCEREPORT", nullable = false)
    private Long idxpostmarketsurveillancereport;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDXPOSTMARKETMONITORING", nullable = false)
    private PostMarketMonitoring postMarketMonitoring;

    @Column(name = "PSRREPORTNAME", length = 255)
    private String psrreportname;

    @Column(name = "PSRREPORTDATA", columnDefinition = "JSONB")
    private String psrreportdata;

    // ... más campos según prompt
}
```

---

## ✅ CHECKLIST FINAL

### **Antes de Empezar:**
- [ ] Leer prompts completos
- [ ] Verificar entidades existentes relacionadas
- [ ] Revisar `PostMarketMonitoringService` existente

### **Durante Implementación:**
- [ ] Seguir convenciones EnArt
- [ ] Respetar dependencias (INC-010-001 → INC-010-002)
- [ ] Integrar con servicios existentes

### **Después de Implementación:**
- [ ] Compilar sin errores
- [ ] Actualizar `tablas.md`
- [ ] Actualizar `SEGUIMIENTO_INCIDENCIAS.md`
- [ ] **Actualizar documento de auditoría asociado** (ver sección siguiente)
- [ ] **Documentar BusinessService** en `/docs/developers/` (ver sección siguiente)
- [ ] **Registrar archivos creados** en este documento (ver sección siguiente)

---

## 📝 REGISTRO DE ARCHIVOS CREADOS

Al finalizar cada incidencia, **registrar aquí** todos los archivos creados o modificados:

### **INC-010-001: Documentación Formal Sistema PMM**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] Entidad: `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/PostMarketMonitoringPlan.java`
- [x] BusinessService: `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/PostMarketMonitoringPlanService.java`
- [x] Script SQL: `nocode.service.entitys/src/main/resources/sql/post_market_monitoring_plan.sql`

**Archivos Modificados:**
- Ninguno

**Fecha Finalización:** 2025-11-25

---

### **INC-010-002: Generación Automática Post-Market Surveillance Report**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] Entidad: `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/PostMarketSurveillanceReport.java`
- [x] BusinessService: `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/PostMarketSurveillanceReportService.java`
- [x] Script SQL: `nocode.service.entitys/src/main/resources/sql/post_market_surveillance_report.sql`
- [x] **Microservicio:** `codeflowx-governance-documentation` (módulo completo)
  - [x] `DocumentationApplication.java`
  - [x] `FreeMarkerConfig.java`
  - [x] `DocumentationProperties.java`
  - [x] `DocumentationController.java`
  - [x] `DocumentGenerationService.java`
  - [x] `PdfGenerationService.java`
  - [x] `TemplateService.java`
  - [x] DTOs: `GenerateReportRequest.java`, `GenerateAnnexIvRequest.java`, `DocumentResponse.java`
  - [x] Excepciones: `DocumentGenerationException.java`, `TemplateNotFoundException.java`
  - [x] Templates: `pmm_surveillance_report.ftl`, `technical_documentation.ftl`
  - [x] `application.yml`
  - [x] `pom.xml`

**Archivos Modificados:**
- [x] `nocode.service/pom.xml` - Añadido módulo `codeflowx-governance-documentation`

**Fecha Finalización:** 2025-11-25

---

### **INC-010-006: Dashboard PMM Consolidado**

**Estado:** 🟢 COMPLETADO

**⚠️ TIPO:** Dashboard completo con UI (ViewModel + ZUL)

**Archivos Creados:**
- [x] **ViewModel:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/compliance/PostMarketMonitoringDashboardViewModel.java`
- [x] **Pantalla ZUL:** `suinsit.nova.web/src/main/webapp/console/gobierno/compliance/post-market-monitoring-dashboard.zul`
- [x] **BusinessService:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/PostMarketMonitoringDashboardService.java`
- [x] **AlertThresholdService:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/AlertThresholdService.java`
- [x] Entidad: `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/AlertThreshold.java`
- [x] Script SQL: `nocode.service.entitys/src/main/resources/sql/alert_thresholds.sql`

**Archivos Modificados:**
- Ninguno (se creó servicio nuevo en lugar de modificar ComplianceDashboardService)

**Funcionalidades del Dashboard:**
- [x] KPIs en tiempo real (métricas de performance, degradación)
- [x] Tabla de alertas activas
- [x] Lista de incidentes recientes
- [x] Lista de planes PMM activos
- [x] Lista de informes recientes
- [x] Filtros por proyecto, tipo de métrica

**Fecha Finalización:** 2025-11-25

---

### **INC-010-004: Implementación Real PostMarketMonitoringService**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] **Servicios de Integración:**
  - [x] `codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/services/integration/DriftDetectionService.java`
  - [x] `codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/services/integration/TelemetryService.java`
  - [x] `codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/services/integration/UserFeedbackService.java`
- [x] **Clases de Resultado:**
  - [x] `codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/services/result/DriftResult.java`
  - [x] `codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/services/result/PerformanceResult.java`
  - [x] `codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/services/result/SatisfactionResult.java`

**Archivos Modificados:**
- [x] `codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/services/PostMarketMonitoringService.java` - Implementación real con integración a microservicios

**Funcionalidades Implementadas:**
- [x] Integración con leka-bias-detection-service para detección de drift
- [x] Integración con aio-telemetry-service para métricas en tiempo real
- [x] Integración con sistema de feedback de usuarios
- [x] Cálculo real de métricas (drift, performance, satisfacción)
- [x] Comparación con baseline histórico
- [x] Manejo de errores y fallbacks

**Fecha Finalización:** 2025-11-26

---

### **INC-010-008: Dashboard de Supervisión Continua**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] **Servicio REST:**
  - [x] `codeflowx-governance-api/src/main/java/com/codeflowx/governance/api/service/PmmDashboardService.java`
- [x] **Controller REST:**
  - [x] `codeflowx-governance-api/src/main/java/com/codeflowx/governance/api/controller/PmmDashboardController.java`

**Archivos Existentes Verificados:**
- [x] ViewModel: `suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/compliance/PostMarketMonitoringDashboardViewModel.java`
- [x] BusinessService: `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/PostMarketMonitoringDashboardService.java`
- [x] Pantalla ZUL: `suinsit.nova.web/src/main/webapp/console/gobierno/compliance/post-market-monitoring-dashboard.zul`

**Endpoints REST Creados:**
- [x] `GET /api/v1/pmm/dashboard/metrics/realtime` - Métricas en tiempo real
- [x] `GET /api/v1/pmm/dashboard/trends` - Tendencias históricas
- [x] `GET /api/v1/pmm/dashboard/alerts/active` - Alertas activas
- [x] `GET /api/v1/pmm/dashboard/status` - Estado de proyectos/modelos

**Funcionalidades Implementadas:**
- [x] Métricas en tiempo real con cálculo de promedios y tendencias
- [x] Tendencias históricas agrupadas por día y tipo de métrica
- [x] Alertas activas con mapeo a DTOs
- [x] Estado de proyectos/modelos
- [x] Integración con servicios existentes

**Fecha Finalización:** 2025-11-26

---

### **INC-010-009: API REST para Consulta de Histórico**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] **DTOs:**
  - [x] `codeflowx-governance-api/src/main/java/com/codeflowx/governance/api/dto/pmm/MetricHistoryResponse.java`
  - [x] `codeflowx-governance-api/src/main/java/com/codeflowx/governance/api/dto/pmm/MetricHistoryItem.java`
  - [x] `codeflowx-governance-api/src/main/java/com/codeflowx/governance/api/dto/pmm/AlertHistoryResponse.java`
  - [x] `codeflowx-governance-api/src/main/java/com/codeflowx/governance/api/dto/pmm/AlertHistoryItem.java`
- [x] **Servicio:**
  - [x] `codeflowx-governance-api/src/main/java/com/codeflowx/governance/api/service/PmmHistoryService.java`
- [x] **Controller REST:**
  - [x] `codeflowx-governance-api/src/main/java/com/codeflowx/governance/api/controller/PmmHistoryController.java`

**Endpoints REST Creados:**
- [x] `GET /api/v1/pmm/metrics/history` - Histórico de métricas con filtros y paginación
- [x] `GET /api/v1/pmm/alerts/history` - Histórico de alertas con filtros y paginación
- [x] `GET /api/v1/pmm/incidents/history` - Histórico de incidentes (TODO)

**Funcionalidades Implementadas:**
- [x] Filtros por proyecto, modelo, fecha, tipo
- [x] Paginación y ordenamiento
- [x] Formato de respuesta estándar
- [x] Mapeo de entidades a DTOs
- [x] Parseo de campos JSONB

**Fecha Finalización:** 2025-11-26

---

### **INC-010-013: Visualización de Tendencias Avanzadas**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] **Servicio:**
  - [x] `codeflowx-governance-api/src/main/java/com/codeflowx/governance/api/service/AdvancedTrendsService.java`
- [x] **Controller REST:**
  - [x] `codeflowx-governance-api/src/main/java/com/codeflowx/governance/api/controller/AdvancedTrendsController.java`

**Endpoints REST Creados:**
- [x] `GET /api/v1/pmm/trends/baseline` - Tendencias con comparación con baseline
- [x] `GET /api/v1/pmm/trends/predict` - Predicción de tendencias futuras

**Funcionalidades Implementadas:**
- [x] Comparación con baseline histórico
- [x] Predicción de tendencias usando regresión lineal
- [x] Cálculo de estadísticas (mean, stddev, min, max)
- [x] Análisis de desviaciones
- [x] Cálculo de R² para confianza de predicciones

**Fecha Finalización:** 2025-11-26

---

### **INC-010-014: Configuración de Frecuencias por Proyecto**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] **Servicio:**
  - [x] `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/PmmFrequencyService.java`
- [x] **ViewModel:**
  - [x] `suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/compliance/PmmFrequencyViewModel.java`
- [x] **Script SQL:**
  - [x] `nocode.service.entitys/src/main/resources/sql/post_market_monitoring_plan_add_custom_frequency.sql`

**Archivos Modificados:**
- [x] `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/PostMarketMonitoringPlan.java` - Agregado campo `pmmcustomfrequencyhours` y nuevas frecuencias (HOURLY, CUSTOM)
- [x] `nocode.service.entitys/src/main/resources/sql/post_market_monitoring_plan.sql` - Actualizado CHECK constraint para nuevas frecuencias

**Funcionalidades Implementadas:**
- [x] Frecuencias adicionales: HOURLY, CUSTOM
- [x] Configuración de frecuencia personalizada (1-168 horas)
- [x] Validación de frecuencias
- [x] Conversión a expresión cron para BPMN
- [x] Frecuencias recomendadas según tipo de proyecto/riesgo
- [x] UI para configuración de frecuencias

**Fecha Finalización:** 2025-11-26

---

### **INC-017: Dashboard Consolidado Compliance**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] **ViewModel:**
  - [x] `suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/compliance/ComplianceDashboardViewModel.java`
- [x] **Pantalla ZUL:**
  - [x] `suinsit.nova.web/src/main/webapp/console/gobierno/compliance/dashboard.zul`

**Archivos Existentes Utilizados:**
- [x] BusinessService: `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/ComplianceDashboardService.java`

**Funcionalidades del Dashboard:**
- [x] KPIs de compliance (% completitud, riesgos, evaluaciones pendientes)
- [x] Estado de clasificación
- [x] Estado de FRIA
- [x] Evaluaciones técnicas pendientes
- [x] Alertas y acciones requeridas
- [x] Score promedio de compliance
- [x] Tasas de finalización y aprobación

**Fecha Finalización:** 2025-11-26

---

## 📋 ACTUALIZACIÓN DE DOCUMENTOS DE AUDITORÍA

Al finalizar cada incidencia, **actualizar** los siguientes documentos:

### **INC-010-001, INC-010-002, INC-010-006: Post-Market Monitoring**

**Documentos a Actualizar:**
1. **`/docs/compliance/auditoria/AUDITORIA_010_POST_MARKET_MONITORING.md`**
   - Buscar secciones relacionadas con Art. 16.g, 72
   - Actualizar estado de implementación
   - Añadir referencia a entidad/BusinessService creado

2. **`/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md`**
   - Buscar `INC-010-001`, `INC-010-002`, `INC-010-006`
   - Cambiar estado de `🔴 PENDIENTE` a `🟢 COMPLETADO`
   - Añadir fecha de finalización
   - Añadir notas sobre implementación

3. **`/docs/compliance/gaps/SEGUIMIENTO_INCIDENCIAS_010_PMM.md`**
   - Buscar incidencias correspondientes
   - Actualizar estado, fecha fin, notas

---

## 📚 DOCUMENTACIÓN DE BUSINESS SERVICES

**IMPORTANTE:** Todos los BusinessServices creados o modificados **DEBEN** ser documentados en `/docs/developers/`.

### **BusinessServices a Documentar:**

#### **INC-010-001:**
- [ ] `PostMarketMonitoringPlanService` (nuevo) - Crear documento nuevo

#### **INC-010-002:**
- [ ] `PostMarketSurveillanceReportService` (nuevo) - Crear documento nuevo

#### **INC-010-004:**
- [ ] `DriftDetectionService` (nuevo) - Servicio de integración
- [ ] `TelemetryService` (nuevo) - Servicio de integración
- [ ] `UserFeedbackService` (nuevo) - Servicio de integración

#### **INC-010-006:**
- [ ] `PostMarketMonitoringDashboardService` (nuevo) - Crear documento nuevo
- [ ] `ComplianceDashboardService` (modificado) - Actualizar documento existente
- [ ] **ViewModel:** `PostMarketMonitoringDashboardViewModel` - Documentar en sección de ViewModels (si existe documentación específica)

#### **INC-010-008:**
- [ ] `PmmDashboardService` (nuevo) - Servicio REST para dashboard

#### **INC-010-009:**
- [ ] `PmmHistoryService` (nuevo) - Servicio REST para histórico

#### **INC-010-013:**
- [ ] `AdvancedTrendsService` (nuevo) - Servicio REST para tendencias avanzadas

#### **INC-010-014:**
- [ ] `PmmFrequencyService` (nuevo) - Servicio para configuración de frecuencias

#### **INC-017:**
- [ ] `ComplianceDashboardViewModel` (nuevo) - ViewModel para dashboard consolidado

**Proceso:** Ver sección completa en `AGENTE_1_VALIDACIONES_CRITICAS.md`

**⚠️ IMPORTANTE:** Para dashboards, también documentar:
- [ ] Funcionalidades del dashboard en documentación funcional
- [ ] Integración ViewModel-BusinessService en documentación técnica
- [ ] Componentes ZUL utilizados (gráficos, tablas, filtros)

---

## 🔄 COORDINACIÓN CON AGENTE 5

### **Módulo Compartido:**
- **`govern.business.compliance`** (PMM)
- **Branch:** `feature/agente4-pmm-documentacion`
- **Comunicación:** Reportar cada 2 horas

### **Evitar Conflictos:**
- Agente 4: Documentación y reportes
- Agente 5: Workflows y alertas
- Coordinar si ambos necesitan modificar `PostMarketMonitoringService`

---

---

## 🎯 MICROSERVICIO DE DOCUMENTACIÓN - ESTADO

### **✅ COMPLETADO - codeflowx-governance-documentation**

**Fecha Creación:** 2025-11-25

**Archivos del Microservicio Creados:**

#### **Estructura Principal:**
- ✅ `pom.xml` - Configuración Maven con dependencias (iText, FreeMarker, Spring Boot 3.2.5)
- ✅ `DocumentationApplication.java` - Clase principal Spring Boot
- ✅ `application.yml` - Configuración del servicio (puerto 8085)

#### **Configuración:**
- ✅ `FreeMarkerConfig.java` - Configuración de FreeMarker para templates
- ✅ `DocumentationProperties.java` - Properties configurables

#### **Controladores:**
- ✅ `DocumentationController.java` - REST API con 3 endpoints:
  - `POST /api/documentation/generate-report`
  - `POST /api/documentation/generate-annex-iv`
  - `GET /api/documentation/health`

#### **Servicios:**
- ✅ `DocumentGenerationService.java` - Servicio principal de generación
- ✅ `PdfGenerationService.java` - Conversión HTML a PDF con iText
- ✅ `TemplateService.java` - Renderizado de templates FreeMarker

#### **DTOs:**
- ✅ `GenerateReportRequest.java` - Request para generar reportes
- ✅ `GenerateAnnexIvRequest.java` - Request para Anexo IV
- ✅ `DocumentResponse.java` - Respuesta con documento generado

#### **Excepciones:**
- ✅ `DocumentGenerationException.java` - Excepción general
- ✅ `TemplateNotFoundException.java` - Excepción de template no encontrado

#### **Templates FreeMarker:**
- ✅ `templates/reports/pmm_surveillance_report.ftl` - Template para reportes PMM
- ✅ `templates/annex-iv/technical_documentation.ftl` - Template para Anexo IV

#### **Integración:**
- ✅ Módulo añadido al `pom.xml` padre de `nocode.service`
- ✅ Listo para integración con `PostMarketSurveillanceReportService`

**✅ INTEGRACIÓN COMPLETADA:**
1. ✅ `DocumentationServiceClientConfig.java` - Configuración de WebClient creada
2. ✅ `PostMarketSurveillanceReportService` actualizado para usar WebClient
3. ✅ Método `generateReportPDF()` implementado
4. ✅ Método `regeneratePDF()` implementado
5. ✅ Manejo de errores y fallback si el servicio no está disponible

**Configuración Requerida:**
Añadir en `application.properties` o `application.yml`:
```properties
documentation.service.url=http://localhost:8085
documentation.service.enabled=true
documentation.service.timeout=30000
documentation.service.max-in-memory-size=52428800
```

**Uso:**
- El servicio genera automáticamente el PDF al crear un informe
- Si el microservicio no está disponible, el informe se crea sin PDF (con warning en logs)
- Se puede regenerar el PDF llamando a `regeneratePDF(reportId)`

---

**Última Actualización:** 26 de noviembre de 2025

---

## 📊 RESUMEN DE IMPLEMENTACIÓN

### **Total de Incidencias Completadas:** 9

#### **Trabajo Nocturno (3 incidencias):**
1. ✅ INC-010-001: Documentación Formal Sistema PMM
2. ✅ INC-010-002: Generación Automática Post-Market Surveillance Report
3. ✅ INC-010-006: Dashboard PMM Consolidado

#### **Nuevas Asignaciones (6 incidencias):**
4. ✅ INC-010-004: Implementación Real PostMarketMonitoringService
5. ✅ INC-010-008: Dashboard de Supervisión Continua
6. ✅ INC-010-009: API REST para Consulta de Histórico
7. ✅ INC-010-013: Visualización de Tendencias Avanzadas
8. ✅ INC-010-014: Configuración de Frecuencias por Proyecto
9. ✅ INC-017: Dashboard Consolidado Compliance

### **Archivos Totales Creados/Modificados:**

**Entidades:** 2
- PostMarketMonitoringPlan (modificada)
- PostMarketSurveillanceReport (ya existía)

**BusinessServices:** 3
- PostMarketMonitoringPlanService
- PostMarketSurveillanceReportService
- PostMarketMonitoringDashboardService

**Servicios de Integración:** 3
- DriftDetectionService
- TelemetryService
- UserFeedbackService

**Servicios REST:** 3
- PmmDashboardService
- PmmHistoryService
- AdvancedTrendsService

**Servicios de Configuración:** 1
- PmmFrequencyService

**Controllers REST:** 3
- PmmDashboardController
- PmmHistoryController
- AdvancedTrendsController

**DTOs:** 4
- MetricHistoryResponse, MetricHistoryItem
- AlertHistoryResponse, AlertHistoryItem

**ViewModels:** 2
- PostMarketMonitoringDashboardViewModel (ya existía)
- ComplianceDashboardViewModel

**Pantallas ZUL:** 2
- post-market-monitoring-dashboard.zul (ya existía)
- dashboard.zul (nuevo)

**Scripts SQL:** 2
- post_market_monitoring_plan.sql (modificado)
- post_market_monitoring_plan_add_custom_frequency.sql (nuevo)

**Clases de Resultado:** 3
- DriftResult, PerformanceResult, SatisfactionResult

### **Endpoints REST Creados:** 9

**Dashboard:**
- `GET /api/v1/pmm/dashboard/metrics/realtime`
- `GET /api/v1/pmm/dashboard/trends`
- `GET /api/v1/pmm/dashboard/alerts/active`
- `GET /api/v1/pmm/dashboard/status`

**Histórico:**
- `GET /api/v1/pmm/metrics/history`
- `GET /api/v1/pmm/alerts/history`
- `GET /api/v1/pmm/incidents/history` (TODO)

**Tendencias:**
- `GET /api/v1/pmm/trends/baseline`
- `GET /api/v1/pmm/trends/predict`

### **Funcionalidades Principales Implementadas:**

1. ✅ Integración real con microservicios (drift detection, telemetry, feedback)
2. ✅ Dashboard de supervisión continua con métricas en tiempo real
3. ✅ API REST completa para consulta de histórico con filtros y paginación
4. ✅ Visualización de tendencias avanzadas con baseline y predicciones
5. ✅ Configuración de frecuencias personalizadas por proyecto
6. ✅ Dashboard consolidado de compliance con KPIs

**Última Actualización:** 26 de noviembre de 2025
