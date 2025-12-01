# GUÍAS PARA AGENTES - TRABAJO NOCTURNO

**Fecha:** 25 de noviembre de 2025
**Objetivo:** Resolver ~80 incidencias en trabajo nocturno autónomo

---

## 📚 DOCUMENTOS DISPONIBLES

### **Planificación General:**
1. **PLAN_TRABAJO_NOCTURNO.md** - Plan completo con cronograma y organización
2. **PLAN_ACCION_INCIDENCIAS.md** - Plan estratégico de 4 fases
3. **DISTRIBUCION_INCIDENCIAS_AGENTES.md** ⭐ NUEVO - Distribución completa de todas las incidencias pendientes entre 8 agentes

### **Guías por Agente:**
3. **AGENTE_1_VALIDACIONES_CRITICAS.md** - Agente 1 (Backend Senior)
   - ✅ INC-005, INC-011, INC-013 (COMPLETADAS)
   - 🔴 INC-007, INC-005-DS, INC-002, INC-004, INC-008 (NUEVAS)

4. **AGENTE_2_VALIDACIONES_SOPORTE.md** - Agente 2 (Backend Mid)
   - ✅ INC-012, INC-005-002 (COMPLETADAS)
   - 🔴 INC-005-004, INC-005-005, INC-005-006, INC-005-007, INC-010-012 (NUEVAS - Python)

5. **AGENTE_3_INTEGRACIONES_APIS.md** - Agente 3 (Backend Senior + Integraciones)
   - ✅ INC-010-005 (COMPLETADA)
   - 🔴 INC-020, INC-010-010, INC-010-015, INC-015, INC-021 (NUEVAS)

6. **AGENTE_4_PMM_DOCUMENTACION.md** - Agente 4 (Backend Senior)
   - ✅ INC-010-001, INC-010-002, INC-010-006 (COMPLETADAS)
   - 🔴 INC-010-004, INC-010-008, INC-010-009, INC-010-013, INC-010-014, INC-017 (NUEVAS)

7. **AGENTE_5_PMM_WORKFLOWS.md** - Agente 5 (Backend Mid)
   - ✅ INC-010-003, INC-010-004, INC-010-007 (COMPLETADAS)
   - 🔴 INC-010-007, INC-005-009, INC-006, INC-009-DS (NUEVAS - BPMN)

8. **AGENTE_6_FRONTEND_VISUALIZACIONES.md** ⭐ NUEVO - Agente 6 (Frontend + Backend)
   - 🔴 INC-003-DS, INC-008-DS, INC-018, INC-016, INC-024

9. **AGENTE_7_OPTIMIZACION_DBA.md** ⭐ NUEVO - Agente 7 (Backend + DBA)
   - 🔴 INC-010-011, INC-007-DS, INC-010, INC-014, INC-009-005

10. **AGENTE_8_FUNCIONALIDADES_OPCIONALES.md** ⭐ NUEVO - Agente 8 (Backend)
    - 🔴 INC-009, INC-019, INC-022, INC-010-DS, INC-009-001

11. **AGENTE_9_EVIDENCE_ENGINE.md** ⭐ NUEVO - Agente 9 (Backend Senior - Microservicios)
    - 🔴 Prompt 1: Evidence Engine API REST
    - Microservicio Spring Boot `codeflowx-governance-evidence-engine` (puerto 8086)
    - Reutiliza `ImmutableLoggingBusinessService` como librería
    - Endpoints: create, verify, query, export, chain

12. **AGENTE_10_SDKS_MCP_DEVELOPER_CONSOLE.md** ⭐ NUEVO - Agente 10 (Full Stack - Python/TypeScript + ZKoss)
    - 🔴 Prompt 2: SDKs, MCP estándar y Developer Console
    - SDKs Python y TypeScript para agentes externos
    - Especificación MCP (Model Control Points)
    - Developer Console en ZKoss (ViewModel + ZUL)
    - Plugin model (.cfx-plugin)

13. **AGENTE_11_AGENT_SUPERVISOR_RUNTIME.md** ⭐ NUEVO - Agente 11 (Backend Senior - Microservicios + BPMN + Proxy)
    - 🔴 Prompt 3: Agent Supervisor Runtime
    - `AioPolicyEnforcementDelegate` (JavaDelegate para BPMN)
    - Microservicio `codeflowx-agent-supervisor` (puerto 8087)
    - Proxy/Interceptor `aios-proxy` (sidecar/reverse-proxy)
    - Memory Store (Postgres + MinIO cifrado)
    - Kill-Switch por políticas

14. **AGENTE_12_PLAN_NEGOCIO_DATA_ROOM.md** ⭐ NUEVO - Agente 12 (Business Analyst / Investment Analyst)
    - 🔴 Plan de Negocio y Data Room para Inversores
    - Análisis de información y consolidación de documentación
    - Creación de plan de negocio completo
    - Preparación de data room profesional
    - Proyecciones financieras y análisis de mercado
    - Materiales de presentación para inversores

15. **AGENTE_13_DATA_ROOM_EXECUTOR.md** ⭐ NUEVO - Agente 13 (Business Analyst / Investment Analyst - Ejecutor)
    - 🔴 Ejecutor de Data Room para Inversores
    - Trabaja directamente con el usuario ejecutando las tareas del Agente 12
    - Mentalidad crítica de VC
    - 3 inversores esperando documentación (URGENTE)
    - Lee `AGENTE_12_PLAN_NEGOCIO_DATA_ROOM.md` como referencia principal

### **Python/Microservicios:**
8. **PYTHON_MICROSERVICIOS.md** - Equipo Python/MLOps
   - Microservicios requeridos por incidencias Java
   - Endpoints y formatos esperados

### **Evolución AI OS (Nuevos Prompts):**
9. **EVOLUCION_AIOS_OVERLAY.md** ⭐ NUEVO - Documento con 3 prompts técnicos para evolución de AI OS
   - Prompt 1: Evidence Engine API REST (Agente 9)
   - Prompt 2: SDKs, MCP y Developer Console (Agente 10)
   - Prompt 3: Agent Supervisor Runtime (Agente 11)

---

## 🎯 INICIO RÁPIDO

### **Para cada Agente:**

1. **Leer tu guía específica:**
   - Agente 1: `AGENTE_1_VALIDACIONES_CRITICAS.md`
   - Agente 2: `AGENTE_2_VALIDACIONES_SOPORTE.md`
   - Agente 3: `AGENTE_3_INTEGRACIONES_APIS.md`
   - Agente 4: `AGENTE_4_PMM_DOCUMENTACION.md`
   - Agente 5: `AGENTE_5_PMM_WORKFLOWS.md`
   - Agente 6: `AGENTE_6_FRONTEND_VISUALIZACIONES.md`
   - Agente 7: `AGENTE_7_OPTIMIZACION_DBA.md`
   - Agente 8: `AGENTE_8_FUNCIONALIDADES_OPCIONALES.md`
   - Agente 9: `AGENTE_9_EVIDENCE_ENGINE.md` ⭐ NUEVO
   - Agente 10: `AGENTE_10_SDKS_MCP_DEVELOPER_CONSOLE.md` ⭐ NUEVO
   - Agente 11: `AGENTE_11_AGENT_SUPERVISOR_RUNTIME.md` ⭐ NUEVO
   - Agente 12: `AGENTE_12_PLAN_NEGOCIO_DATA_ROOM.md` ⭐ NUEVO
   - Agente 13: `AGENTE_13_DATA_ROOM_EXECUTOR.md` ⭐ NUEVO

2. **Revisar plan general:**
   - `PLAN_TRABAJO_NOCTURNO.md` - Cronograma y checkpoints

3. **Seguir checklist de tu guía:**
   - Leer prompts específicos
   - Crear/modificar entidades según convenciones EnArt
   - Crear/modificar BusinessServices
   - Crear scripts SQL si es necesario
   - Compilar y verificar
   - Actualizar documentación

---

## 📋 CONVENCIONES ENART (RESUMEN)

### **Nomenclatura:**
- **Tablas:** Prefijo 3 caracteres + NOMBRE (MAYÚSCULAS)
- **PK:** `IDX` + `NOMBREENTIDAD`
- **UUID:** Campo `iduuid` obligatorio (VARCHAR(36))
- **Auditoría:** `createdat`, `updatedat` (TIMESTAMP)

### **Estructura:**
- **Entidades:** `/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/`
- **BusinessServices:** `/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/`
- **SQL:** `/nocode.service.entitys/src/main/resources/sql/`

### **Principios:**
- Tercera forma normal (3FN)
- KISS (Keep It Simple, Stupid)
- SOLID
- Arquitectura hexagonal

---

## 🔗 ENLACES RÁPIDOS

### **Documentación:**
- **Prompts:** `/docs/compliance/gaps/prompts/java/`
- **Seguimiento:** `/docs/compliance/gaps/SEGUIMIENTO_INCIDENCIAS.md`
- **Arquitectura:** `/docs/compliance/PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md`

### **Código:**
- **Entidades:** `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/`
- **BusinessServices:** `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/`
- **SQL:** `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/`

---

## ✅ CHECKLIST INICIAL (Todos los Agentes)

- [ ] Leer tu guía específica completa
- [ ] Leer `PLAN_TRABAJO_NOCTURNO.md`
- [ ] Revisar convenciones EnArt
- [ ] Setup del entorno de desarrollo
- [ ] Verificar acceso a repositorios
- [ ] Revisar prompts asignados
- [ ] Identificar dependencias entre incidencias

---

## 📋 TAREAS OBLIGATORIAS AL FINALIZAR CADA INCIDENCIA

Cada agente **DEBE** completar estas tareas al finalizar cada incidencia:

### 1. **Registrar Archivos Creados**
- Marcar incidencia como completada en tu documento de agente
- Registrar todas las rutas de archivos creados:
  - **Entidades JPA** (si aplica)
  - **BusinessServices** (si aplica)
  - **Scripts SQL** (si aplica)
  - **ViewModels** (si aplica - **OBLIGATORIO para dashboards**)
  - **Pantallas ZUL** (si aplica - **OBLIGATORIO para dashboards**)
  - **Procesos BPMN** (si aplica - para workflows)
  - **Delegates** (si aplica - para workflows)
  - **Reglas Drools** (si aplica - para workflows)
- Indicar archivos modificados con descripción del cambio

**⚠️ IMPORTANTE - GENERACIÓN DE DOCUMENTOS (JAVA):**

Muchas incidencias requieren **generación de documentos PDF, reportes o justificaciones**.

**Implementación:** Se hace con **Java**, siguiendo el patrón de microservicios Java del proyecto.

**Opciones de Implementación:**

**Opción 1: Microservicio Java (Recomendado)**
- **Microservicio:** `codeflowx.govern.documentation` (siguiendo convención del proyecto)
- **Framework:** Spring Boot
- **Librerías:** iText (o Apache PDFBox) + FreeMarker
- **Endpoint:** `POST /api/documentation/generate-report`
- **Ubicación:** Seguir estructura de otros microservicios Java del proyecto

**Opción 2: Servicio dentro del módulo business**
- **Servicio:** `DocumentGenerationService` en `codeflowx.govern.business`
- **Librerías:** iText (o Apache PDFBox) + FreeMarker
- **Integración directa** en BusinessServices

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

**Templates:**
- Ubicación: `src/main/resources/templates/reports/*.ftl` (FreeMarker)
- NO hardcoded - templates en recursos externos

**Incidencias que requieren generación de documentos:**
- INC-010-001: Documentación Formal Sistema PMM (Agente 4) - Opcional
- INC-010-002: Generación Automática Post-Market Surveillance Report (Agente 4) - **OBLIGATORIO PDF**
- INC-024: Reporte ejecutivo consolidado (no asignada) - **OBLIGATORIO PDF**
- INC-011: Checklist completo validación modelos (Agente 1) - Puede requerir justificaciones
- INC-013: Validación medidas mitigación (Agente 1) - Puede requerir justificaciones

**Patrón de integración (Opción 1 - Microservicio Java):**
```java
@Autowired
private RestTemplate restTemplate;

@Value("${documentation.service.url:http://localhost:8080}")
private String documentationServiceUrl;

// 1. Preparar datos desde BD
Map<String, Object> reportData = prepareReportData(projectId);

// 2. Llamar microservicio Java
Map<String, Object> request = Map.of(
    "report_type", "post_market_surveillance",
    "report_data", reportData,
    "template_name", "pmm_surveillance_report"
);

ResponseEntity<byte[]> response = restTemplate.postForEntity(
    documentationServiceUrl + "/api/documentation/generate-report",
    request,
    byte[].class
);

// 3. Recibir PDF generado
byte[] pdfBytes = response.getBody();

// 4. Almacenar en entidad o storage
```

**Patrón de integración (Opción 2 - Servicio directo):**
```java
@Autowired
private DocumentGenerationService documentService;

// 1. Preparar datos desde BD
Map<String, Object> reportData = prepareReportData(projectId);

// 2. Generar PDF directamente
byte[] pdfBytes = documentService.generateReportPDF(
    "pmm_surveillance_report.ftl",
    reportData
);

// 3. Almacenar en entidad o storage
```

---

**⚠️ IMPORTANTE:** Las siguientes incidencias **SIEMPRE requieren** ViewModels y ZULs:

**Dashboards:**
- INC-010-006: Dashboard PMM Consolidado (Agente 4)
- INC-017: Dashboard consolidado compliance (no asignada)
- INC-010-008: Dashboard Supervisión Continua (no asignada)
- INC-010-013: Visualización tendencias avanzadas (no asignada)
- INC-024: Reporte ejecutivo consolidado (no asignada)

**Requisitos para Dashboards:**
- ViewModel en `suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/[module]/[Nombre]ViewModel.java`
- Pantalla ZUL en `suinsit.nova.web/src/main/webapp/console/gobierno/[module]/[nombre].zul`
- BusinessService que proporciona los datos

**Procesos BPMN con User Tasks:**
- INC-010-003: Workflow Notificación Incidentes Graves (Agente 5) - **4 User Tasks requieren ViewModels y ZULs**
- Otros procesos BPMN también pueden tener User Tasks que requieren ViewModels/ZULs

**Requisitos para User Tasks BPMN:**
- **ViewModel:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/workflow/viewmodels/[Nombre]ViewModel.java`
  - **Paquete obligatorio:** `com.codeflowx.govern.workflow.viewmodels`
- **Pantalla ZUL:** `suinsit.nova.web/src/main/webapp/console/bpmn/[nombre].zul`
  - **Directorio obligatorio:** `console/bpmn/`
- **Bandeja de Tareas:** Las pantallas se ejecutan mediante `task-inbox.zul` que lista todas las tareas BPMN pendientes
- Cada User Task del proceso BPMN requiere su propio ViewModel y ZUL

### 2. **Actualizar Documentos de Auditoría**
- Actualizar documento de auditoría asociado en `/docs/compliance/auditoria/`
- Actualizar documento de incidencias en `/docs/compliance/auditoria/INCIDENCIAS_*.md`
- Actualizar `SEGUIMIENTO_INCIDENCIAS.md` en `/docs/compliance/gaps/`

### 3. **Documentar BusinessServices**
- Crear/actualizar documento en `/docs/developers/[categoria]/[NombreBusinessService].md`
- Seguir formato estándar (ver ejemplo en `AGENTE_1_VALIDACIONES_CRITICAS.md`)
- Actualizar índice en `/docs/developers/README.md`

**Ver secciones detalladas en cada documento de agente:**
- `AGENTE_1_VALIDACIONES_CRITICAS.md` - Sección completa con ejemplos
- `AGENTE_2_VALIDACIONES_SOPORTE.md` - Sección adaptada
- `AGENTE_3_INTEGRACIONES_APIS.md` - Sección adaptada
- `AGENTE_4_PMM_DOCUMENTACION.md` - Sección adaptada
- `AGENTE_5_PMM_WORKFLOWS.md` - Sección adaptada
- `AGENTE_9_EVIDENCE_ENGINE.md` - Microservicio Spring Boot
- `AGENTE_10_SDKS_MCP_DEVELOPER_CONSOLE.md` - SDKs Python/TypeScript + ZKoss
- `AGENTE_11_AGENT_SUPERVISOR_RUNTIME.md` - Microservicio + BPMN + Proxy

---

## 🚀 AGENTES DE EVOLUCIÓN AI OS (NUEVOS)

Los agentes 9, 10 y 11 trabajan en los **prompts de evolución de AI OS** definidos en `EVOLUCION_AIOS_OVERLAY.md`. Estos prompts están orientados a convertir el framework actual en un **AI OS completo, extensible e interoperable**.

## 💼 AGENTE DE PLAN DE NEGOCIO Y DATA ROOM (NUEVO)

El **Agente 12** define las tareas y estrategia para la preparación de **plan de negocio y data room para inversores**, consolidando toda la información disponible sobre CodeflowX y creando materiales profesionales para la ronda de inversión de €250K (€100K ENISA + €150K del fondo).

El **Agente 13** es el ejecutor que trabaja directamente con el usuario, leyendo el documento del Agente 12 como referencia principal y ejecutando las tareas con mentalidad crítica de VC. **3 inversores están esperando la documentación** (URGENTE CRÍTICO).

### **Diferencias con Agentes 1-8:**

- **No trabajan en incidencias específicas**, sino en **prompts técnicos de evolución**
- **Enfoque arquitectónico:** Mejoras estructurales del sistema
- **Integración con ecosistema:** SDKs, MCP, Developer Console, Supervisor
- **Documentación principal:** `EVOLUCION_AIOS_OVERLAY.md` (no prompts de incidencias)

### **Coordinación:**

- **Agente 9** debe completarse primero (Evidence Engine es base para SDKs)
- **Agente 10** puede trabajar en paralelo con Agente 11 (SDKs y Supervisor son independientes)
- **Agente 11** requiere que Agente 9 esté avanzado (usa ImmutableLoggingBusinessService)

---

**Última Actualización:** [FECHA]
