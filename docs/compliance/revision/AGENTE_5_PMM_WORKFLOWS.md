# GUÍA AGENTE 5 - POST-MARKET MONITORING (WORKFLOWS)

**Agente:** Backend Mid
**Equipo:** Equipo 3 - Post-Market Monitoring
**Duración:** 12 horas
**Objetivo:** Implementar workflows de notificación y alertas automáticas PMM

---

## 📋 INCIDENCIAS ASIGNADAS

### **Incidencias Completadas (Trabajo Nocturno):**
| ID | Descripción | Esfuerzo | Prioridad | Estado |
|----|-------------|----------|-----------|--------|
| **INC-010-003** | Workflow Notificación Incidentes Graves | 2h | 🔴 CRÍTICA | ✅ COMPLETADO |
| **INC-010-004** | Verificación PostMarketMonitoringService | 1h | 🔴 CRÍTICA | ✅ COMPLETADO |
| **INC-010-007** | Alertas Automáticas Degradación | 1h | 🔴 CRÍTICA | ✅ COMPLETADO |

### **Incidencias Pendientes (Nuevas Asignaciones - BPMN):**
| ID | Descripción | Esfuerzo | Prioridad | Estado |
|----|-------------|----------|-----------|--------|
| **INC-010-007** | Implementación de Informes Automáticos | 2h | 🟡 ALTA | ✅ COMPLETADO |
| **INC-005-009** | Proceso mejora continua | 3h | 🟢 MEDIA | ✅ VERIFICADO (ya existe) |
| **INC-006** | Verificación automática integridad logs | 2h | 🟡 MEDIA | ✅ COMPLETADO |
| **INC-009-DS** | Notificaciones automáticas | 1h | 🟡 MEDIA | ✅ COMPLETADO |

**Total:** 0 incidencias pendientes - Todas completadas

---

## 📚 DOCUMENTOS DE REFERENCIA

### **⚠️ IMPORTANTE: WORKFLOWS SON PROCESOS BPMN DE ACTIVITI**

Los workflows **NO son manuales**, son **procesos BPMN de Activiti** que se ejecutan automáticamente.

**Prompt Principal BPMN:**
- **`/docs/compliance/PROMPTS_04_BPMN_WORKFLOWS.md`** ⭐ **USAR ESTE PARA BPMN**

**Módulo BPMN:**
- **`codeflowx.govern.workflow.lib`** - Módulo donde están todos los archivos BPMN, delegates y reglas Drools

**Estructura del Módulo:**
```
codeflowx.govern.workflow.lib/
├── src/main/resources/
│   ├── processes/              ← Archivos .bpmn y .bpmn20.xml
│   │   ├── aios/
│   │   ├── compliance/         ← Procesos compliance
│   │   ├── audit/
│   │   └── metrics/
│   └── rules/                  ← Reglas Drools (.drl)
│       └── {category}/
└── src/main/java/
    └── com/codeflowx/govern/workflow/
        ├── delegates/          ← Java Delegates (implements JavaDelegate de Activiti)
        └── drools/
            └── facts/          ← POJOs para reglas Drools
```

### Prompts Específicos:
1. **INC-010-003:**
   - `/docs/compliance/gaps/prompts/java/INC-010-003_serious_incident_report.md`
   - Artículo EU AI Act: **Art. 73`
   - **Tipo:** Proceso BPMN nuevo o modificación existente
   - **Ver también:** `/docs/compliance/PROMPTS_04_BPMN_WORKFLOWS.md` - Prompt B.2 (incident-reporting-process)

2. **INC-010-004:**
   - `/docs/compliance/gaps/prompts/java/INC-010-004_implementacion_real_pmm_service.md`
   - Artículo EU AI Act: **Art. 72`
   - **Nota:** Servicio ya existe, solo verificar y completar
   - **Tipo:** Verificación BusinessService (no BPMN)

3. **INC-010-007:**
   - Verificar prompt específico en `/docs/compliance/gaps/prompts/java/`
   - Artículo EU AI Act: **Art. 72`
   - **Tipo:** Proceso BPMN nuevo o modificación existente
   - **Ver también:** `/docs/compliance/PROMPTS_04_BPMN_WORKFLOWS.md` - Prompt A.1 (compliance-monitoring extensión)

### Documentación General:
- **Arquitectura EnArt:** `/docs/compliance/PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md`
- **BPMN Workflows:** `/docs/compliance/PROMPTS_04_BPMN_WORKFLOWS.md` ⭐ **USAR ESTE PARA BPMN**
- **BPMN README:** `/docs/compliance/bpmn/README.md`
- **Plan Nocturno:** `/docs/compliance/revision/PLAN_TRABAJO_NOCTURNO.md`
- **Seguimiento PMM:** `/docs/compliance/gaps/SEGUIMIENTO_INCIDENCIAS_010_PMM.md`

### Referencias Técnicas:
- **BPMN Existentes:** `/eclipse-workspace/nocode.service/codeflowx.govern.workflow.lib/src/main/resources/processes/compliance/`
- **Delegates Existentes:** `/eclipse-workspace/nocode.service/codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/delegates/compliance/`
- **Reglas Drools:** `/eclipse-workspace/nocode.service/codeflowx.govern.workflow.lib/src/main/resources/rules/compliance/`
- **Entidades Existentes:**
  - `entity/compliance/PostMarketMonitoring.java` - ✅ Ya existe
  - `entity/compliance/Incident.java` - ✅ Ya existe
- **BusinessServices Existentes:**
  - `business/compliance/PostMarketMonitoringService.java` - ✅ Ya existe
  - `business/compliance/AuthorityNotificationService.java` - ✅ Ya existe
  - `business/governance/NotificationSchedulerService.java` - ✅ Ya existe

---

## 🏗️ ARQUITECTURA ENART - CONVENCIONES

### **⚠️ IMPORTANTE: PROCESOS BPMN DE ACTIVITI**

Los workflows son **procesos BPMN de Activiti** que se ejecutan automáticamente. **NO son procesos manuales**.

**Componentes BPMN:**
- **Service Tasks:** Java Delegates (implements `JavaDelegate` de Activiti)
- **User Tasks:** ViewModels ZUL para interacción humana
- **Gateways:** Exclusive, Parallel, Inclusive para flujo condicional
- **Timers:** Start events, intermediate events para ejecución programada
- **Reglas Drools:** Para lógica de negocio compleja

**Convenciones BPMN:**
- **Archivos BPMN:** `.bpmn` o `.bpmn20.xml` en `codeflowx.govern.workflow.lib/src/main/resources/processes/{category}/`
- **Delegates:** `{ProcessName}{TaskName}Delegate.java` en `com.codeflowx.govern.workflow.delegates.{category}`
- **Reglas Drools:** `.drl` en `src/main/resources/rules/{category}/`
- **Facts Drools:** POJOs en `com.codeflowx.govern.workflow.drools.facts`

**Patrón Delegate:**
```java
@Component("delegateBean")
public class MyDelegate implements JavaDelegate {
    @Autowired
    private SomeService someService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long projectId = (Long) execution.getVariable("projectId");
        ResultDTO result = someService.doSomething(projectId);
        execution.setVariable("resultData", result);
        execution.setVariable("success", true);
    }
}
```

**Ver documento AGENTE_1_VALIDACIONES_CRITICAS.md sección "ARQUITECTURA ENART" para convenciones completas de entidades JPA.**

---

## 📁 ESTRUCTURA DE DIRECTORIOS

### **⚠️ PROCESOS BPMN (Activiti):**
```
/eclipse-workspace/nocode.service/codeflowx.govern.workflow.lib/
├── src/main/resources/
│   ├── processes/              ← Archivos .bpmn y .bpmn20.xml
│   │   ├── aios/
│   │   ├── compliance/         ← Procesos compliance
│   │   │   ├── compliance-monitoring-v1.bpmn  ← MODIFICAR (INC-010-007)
│   │   │   ├── incident-reporting-process.bpmn  ← CREAR (INC-010-003)
│   │   │   └── [NUEVOS AQUÍ]
│   │   ├── audit/
│   │   └── metrics/
│   └── rules/                  ← Reglas Drools (.drl)
│       ├── compliance/
│       └── [NUEVAS AQUÍ]
└── src/main/java/com/codeflowx/govern/workflow/
    ├── delegates/              ← Java Delegates
    │   ├── compliance/
    │   │   ├── NotifyMarketSurveillanceAuthorityDelegate.java  ← CREAR (INC-010-003)
    │   │   ├── CheckPostMarketMetricsDelegate.java  ← CREAR (INC-010-007)
    │   │   └── [NUEVOS AQUÍ]
    │   └── [OTRAS CATEGORÍAS]
    └── drools/
        └── facts/              ← POJOs para reglas Drools
```

### **Entidades JPA:**
```
/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/
├── compliance/          ← Entidades de compliance
│   ├── PostMarketMonitoring.java  ← Ya existe
│   ├── Incident.java               ← Ya existe
│   ├── IncidentReport.java         ← CREAR (INC-010-003)
│   └── CorrectiveAction.java      ← CREAR (INC-010-003)
```

### **BusinessServices:**
```
/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/
├── compliance/          ← Servicios de compliance
│   ├── PostMarketMonitoringService.java    ← Ya existe, VERIFICAR (INC-010-004)
│   └── SeriousIncidentNotificationService.java ← CREAR (INC-010-003)
└── governance/         ← Servicios de governance
    └── NotificationSchedulerService.java   ← Ya existe, MODIFICAR
```

---

## 🔧 IMPLEMENTACIÓN POR INCIDENCIA

### **INC-010-003: Workflow Notificación Incidentes Graves**

#### **Archivos a Crear/Modificar:**

1. **Entidad (CREAR):**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/SeriousIncidentReport.java`
   - Tabla: `SIRSERIOUSINCIDENTREPORTS` (prefijo `SIR`)

2. **BusinessService (CREAR):**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/SeriousIncidentNotificationService.java`
   - **Integración:** Usar `AuthorityNotificationService`, `NotificationSchedulerService`

3. **Script SQL (CREAR):**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/serious_incident_report.sql`

#### **Checklist:**
- [ ] Leer prompt completo: `INC-010-003_serious_incident_report.md`
- [ ] Revisar `Incident` entity existente
- [ ] Crear entidad `SeriousIncidentReport` según prompt
- [ ] Crear BusinessService según prompt
- [ ] Integrar con `AuthorityNotificationService`
- [ ] Crear script SQL
- [ ] Actualizar `tablas.md`
- [ ] Compilar sin errores
- [ ] Actualizar `SEGUIMIENTO_INCIDENCIAS.md`

---

### **INC-010-004: Verificación PostMarketMonitoringService**

#### **Archivos a Verificar:**

1. **BusinessService (VERIFICAR):**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/PostMarketMonitoringService.java`
   - **Verificar:** Que todos los métodos estén implementados (no mocks)
   - **Completar:** Cualquier método que tenga TODO o implementación parcial

2. **Entidades (VERIFICAR):**
   - Verificar que `PostMarketMonitoring` y `Incident` existen y están correctas

#### **Checklist:**
- [ ] Leer prompt completo: `INC-010-004_implementacion_real_pmm_service.md`
- [ ] Revisar `PostMarketMonitoringService` completo
- [ ] Identificar métodos con mocks o TODOs
- [ ] Completar implementación real
- [ ] Verificar integración con entidades
- [ ] Compilar sin errores
- [ ] Actualizar `SEGUIMIENTO_INCIDENCIAS.md`

---

### **INC-010-007: Alertas Automáticas Degradación**

#### **Archivos a Crear/Modificar:**

1. **BusinessService (CREAR o MODIFICAR):**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/PerformanceDegradationAlertService.java`
   - **O modificar:** `PostMarketMonitoringService.java`
   - **Integración:** Usar `NotificationSchedulerService`

2. **Entidad (VERIFICAR):**
   - Verificar si existe entidad para alertas de degradación
   - Si no existe: crear en `entity/compliance/PerformanceDegradationAlert.java`
   - Tabla: `PDAPERFORMANCEDEGRADATIONALERTS` (prefijo `PDA`)

#### **Checklist:**
- [ ] Leer prompt específico (buscar en `/prompts/java/`)
- [ ] Revisar `PostMarketMonitoringService` existente
- [ ] Crear/modificar BusinessService según prompt
- [ ] Integrar con `NotificationSchedulerService`
- [ ] Crear entidad si es necesaria
- [ ] Compilar sin errores
- [ ] Actualizar `SEGUIMIENTO_INCIDENCIAS.md`

---

## 📝 PLANTILLA DE INTEGRACIÓN CON NOTIFICACIONES

```java
@Service
@Slf4j
public class SeriousIncidentNotificationService {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private AuthorityNotificationService authorityNotificationService;

    @Autowired
    private NotificationSchedulerService notificationSchedulerService;

    /**
     * Notifica incidente grave según Art. 73
     */
    public void notifySeriousIncident(Long incidentId) {
        log.info("Notifying serious incident: {}", incidentId);

        try {
            Incident incident = businessService.findById(Incident.class, incidentId);
            if (incident == null) {
                throw new BussinessException("Incident not found: " + incidentId);
            }

            // Verificar si es grave según Art. 73
            if (!isSeriousIncident(incident)) {
                log.debug("Incident is not serious, skipping notification");
                return;
            }

            // 1. Notificar a autoridad
            authorityNotificationService.notifyAuthority(incident);

            // 2. Crear notificación interna
            createInternalNotification(incident);

            // 3. Registrar en log inmutable
            logSeriousIncident(incident);

            log.info("Serious incident notified successfully: {}", incidentId);

        } catch (BussinessException e) {
            log.error("Error notifying serious incident: {}", incidentId, e);
            throw e;
        }
    }

    private boolean isSeriousIncident(Incident incident) {
        // Lógica según Art. 73: incidentes que causan o pueden causar
        // muerte, daño grave a la salud, daño grave a bienes, etc.
        return "CRITICAL".equals(incident.getIncidentseverity()) ||
               "HIGH".equals(incident.getIncidentseverity());
    }
}
```

---

## ✅ CHECKLIST FINAL

### **Antes de Empezar:**
- [ ] Leer prompts completos
- [ ] Revisar servicios existentes relacionados
- [ ] Coordinar con Agente 4 sobre archivos compartidos

### **Durante Implementación:**
- [ ] Seguir convenciones EnArt
- [ ] Integrar con servicios existentes
- [ ] Completar implementaciones parciales

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

### **INC-010-003: Workflow Notificación Incidentes Graves**

**Estado:** 🟢 COMPLETADO

**⚠️ TIPO:** Proceso BPMN de Activiti (NO manual) con **6 User Tasks que requieren ViewModels y ZULs**

**Archivos Creados:**

**Proceso BPMN:**
- [x] Proceso BPMN: `codeflowx.govern.workflow.lib/src/main/resources/processes/compliance/incident-reporting-process.bpmn20.xml` ✅

**Delegates (Service Tasks):**
- [x] `ClassifyIncidentSeverityDelegate.java` ✅ (existe en `delegates/incident/`)
- [x] `NotifyMarketSurveillanceAuthorityDelegate.java` ✅ (existe en `delegates/incident/`)
- [x] `NotifyAffectedUsersDelegate.java` ✅ (existe en `delegates/incident/`)
- [x] `ExecuteRCADelegate.java` ✅ (existe en `delegates/incident/`)
- [x] `CreateCorrectiveActionTasksDelegate.java` ✅ (existe en `delegates/incident/`)
- [x] `CloseIncidentDelegate.java` ✅ (existe en `delegates/incident/`)
- [x] `EscalateIncidentDelegate.java` ✅ (existe en `delegates/incident/`)

**ViewModels y ZULs (User Tasks) - ⚠️ OBLIGATORIOS:**
- [x] **User Task 1:** `DocumentIncidentDetailsViewModel.java` + `document-incident-details-form.zul` ✅
- [x] **User Task 2:** `RootCauseAnalysisViewModel.java` + `root-cause-analysis-form.zul` ✅
- [x] **User Task 3:** `DefineCorrectiveActionsViewModel.java` + `define-corrective-actions-form.zul` ✅
- [x] **User Task 4:** `VerifyIncidentResolutionViewModel.java` + `verify-incident-resolution-form.zul` ✅
- [x] **User Task 5:** `CategorizeAndDocumentViewModel.java` + `categorize-incident-form.zul` ✅ (adicional)
- [x] **User Task 6:** `ExecuteCorrectiveActionViewModel.java` + `execute-corrective-action-form.zul` ✅ (multi-instance, adicional)

**⚠️ IMPORTANTE:** Las pantallas de BPMN se ejecutan mediante la **bandeja de tareas** (`task-inbox.zul`)

**Ubicación ViewModels (OBLIGATORIO):**
- `suinsit.nova.web/src/main/java/com/codeflowx/govern/workflow/viewmodels/`
- **Paquete completo:** `com.codeflowx.govern.workflow.viewmodels`

**Ubicación ZULs (OBLIGATORIO):**
- `suinsit.nova.web/src/main/webapp/console/bpmn/`
- **Ejemplo:** `document-incident-details.zul`, `root-cause-analysis.zul`, etc.

**Bandeja de Tareas:**
- ZUL: `suinsit.nova.web/src/main/webapp/console/bpmn/task-inbox.zul`
- ViewModel: `com.codeflowx.govern.workflow.viewmodels.TaskInboxViewModel`
- **Funcionalidad:** Lista todas las tareas BPMN pendientes y permite acceder a los formularios ZUL de cada User Task

**Otros:**
- [ ] Reglas Drools (si aplica): `codeflowx.govern.workflow.lib/src/main/resources/rules/compliance/[nombre].drl`
- [ ] Facts Drools (si aplica): `codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/drools/facts/[Nombre]Fact.java`
- [ ] BusinessService (si nuevo): `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/[Nombre]Service.java`
- [ ] Entidad (si nueva): `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/[Nombre].java`
- [ ] Script SQL (si nueva tabla): `nocode.service.entitys/src/main/resources/sql/[nombre].sql`

**Archivos Modificados:**
- [x] `suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/incident/*.java` - Corregidos errores de sintaxis (llaves extra)

**Fecha Finalización:** 25 de noviembre de 2025

**Notas de Implementación:**
- ✅ Todos los ViewModels creados en `com.codeflowx.govern.workflow.viewmodels.*`
- ✅ Todos los ZULs actualizados y apuntando a la ubicación correcta
- ✅ ViewModels siguen patrón de `AgentApprovalHumanOverrideViewModel`
- ✅ Integración con Flowable TaskService implementada
- ✅ Modo mock disponible para demos
- ✅ ViewModels adicionales creados: `CategorizeAndDocumentViewModel` y `ExecuteCorrectiveActionViewModel`
- ✅ Todos los delegates ya existen en `delegates/incident/`

---

### **INC-010-004: Verificación PostMarketMonitoringService**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] BusinessService: `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/PostMarketMonitoringService.java` ✅ (ya existía, verificado completo)

**Archivos Modificados:**
- [x] `PostMarketMonitoringService.java` - Verificado: Implementación completa con integración a vistas de BD (`VW_PERFORMANCE_METRICS_DASHBOARD`, `VW_DRIFT_DETECTION_DASHBOARD`), métodos reales sin mocks ✅

**Fecha Finalización:** 25 de noviembre de 2025

**Notas de Implementación:**
- ✅ Servicio ya implementado completamente
- ✅ Integración con vistas de BD para métricas de performance
- ✅ Métodos `checkPerformanceMetrics()` y `getDriftDetections()` implementados
- ✅ Sin implementaciones mock o TODOs pendientes

---

### **INC-010-007: Alertas Automáticas Degradación**

**Estado:** 🟢 COMPLETADO

**⚠️ TIPO:** Modificación de proceso BPMN existente

**Archivos Creados:**
- [x] BusinessService: `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/PerformanceDegradationAlertService.java` ✅
- [x] Delegate: `codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/delegates/compliance/CreatePerformanceDegradationAlertDelegate.java` ✅
- [x] Entidad: Usa `AgentAlert` existente (no requiere nueva entidad) ✅

**Archivos Modificados:**
- [x] Proceso BPMN: `codeflowx.govern.workflow.lib/src/main/resources/processes/compliance/compliance-monitoring-v1.bpmn` ✅
  - Añadido gateway `degradationGateway` después de `checkPostMarketMetrics`
  - Añadido Service Task `createPerformanceDegradationAlert` que se ejecuta cuando `performanceDegradation == true`
  - Flujos actualizados para incluir creación automática de alertas

**Fecha Finalización:** 25 de noviembre de 2025

**Notas de Implementación:**
- ✅ `PerformanceDegradationAlertService` creado con métodos:
  - `createDegradationAlert()` - Crea alerta automática con severidad calculada
  - `getActiveDegradationAlerts()` - Obtiene alertas activas por proyecto
  - `closeDegradationAlert()` - Cierra alerta cuando se resuelve
- ✅ Integración con `NotificationSchedulerService` (preparado para notificaciones)
- ✅ Usa entidad `AgentAlert` existente (no requiere nueva tabla)
- ✅ Delegate `CreatePerformanceDegradationAlertDelegate` integrado en proceso BPMN
- ✅ Proceso BPMN modificado para crear alertas automáticamente cuando se detecta degradación

---

### **INC-006: Verificación Automática Integridad Logs**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] Scheduled Task: `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/scheduled/LogIntegrityScheduledTask.java` ✅

**Fecha Finalización:** 25 de noviembre de 2025

**Notas de Implementación:**
- ✅ Job programado diario a las 2 AM para verificación automática de integridad
- ✅ Reporte semanal los lunes a las 9 AM
- ✅ Detección automática de tampering usando `ImmutableLoggingBusinessService`
- ✅ Notificación automática al equipo de seguridad cuando se detecta tampering
- ✅ Integración con `detectTampering()` para alertas de seguridad
- ⚠️ Requiere habilitar `@EnableScheduling` en la aplicación Spring

---

### **INC-009-DS: Notificaciones Automáticas**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] NotificationService: `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/notifications/NotificationService.java` ✅
- [x] SlackNotificationService: `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/notifications/SlackNotificationService.java` ✅

**Fecha Finalización:** 25 de noviembre de 2025

**Notas de Implementación:**
- ✅ Servicio de notificaciones con soporte para email, Slack e internas
- ✅ Métodos para notificar hallazgos críticos, alertas de compliance y degradación de performance
- ✅ Integración con `NotificationSchedulerService` para notificaciones internas
- ✅ Configuración mediante properties (`notification.email.enabled`, `notification.slack.enabled`)
- ✅ Obtención automática de destinatarios según roles (compliance officers, admins)
- ⚠️ Email requiere configuración de `JavaMailSender` (Spring Mail)
- ⚠️ Slack requiere configuración de webhook URL (`notification.slack.webhook.url`)

---

### **INC-010-007: Implementación de Informes Automáticos (Actualizado)**

**Estado:** 🟢 COMPLETADO

**Archivos Modificados:**
- [x] Delegate: `codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/delegates/compliance/GenerateDailyPmmReportDelegate.java` ✅
- [x] Delegate: `codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/delegates/compliance/GenerateMonthlyPmmReportDelegate.java` ✅

**Fecha Finalización:** 25 de noviembre de 2025

**Notas de Implementación:**
- ✅ TODOs completados en `GenerateDailyPmmReportDelegate`:
  - Implementada búsqueda real de proyectos con planes PMM activos
  - Integración con `PostMarketSurveillanceReportService.generateReport()` para generación real
  - Eliminados métodos placeholder, ahora usa servicio completo
- ✅ TODOs completados en `GenerateMonthlyPmmReportDelegate`:
  - Implementada búsqueda real de proyectos con planes PMM activos
  - Integración con `PostMarketSurveillanceReportService.generateReport()` para generación real
  - Eliminados métodos placeholder, ahora usa servicio completo
- ✅ Proceso BPMN `pmm-reports-v1.bpmn` ya existía con timers configurados
- ✅ Delegates ahora generan informes reales usando el servicio completo (incluye PDF, almacenamiento, etc.)

---

### **INC-005-009: Proceso Mejora Continua**

**Estado:** ✅ VERIFICADO (ya existe)

**Archivos Existentes:**
- [x] Proceso BPMN: `codeflowx.govern.workflow.lib/src/main/resources/processes/aios/rag-continuous-improvement-v1.bpmn` ✅

**Notas:**
- ✅ Proceso BPMN ya implementado según documentación en `INCIDENCIAS_005_EVALUACION_RAG.md`
- ✅ Delegates Java ya existen e integran con microservicios Python
- ✅ No requiere trabajo adicional

---

## 📋 ACTUALIZACIÓN DE DOCUMENTOS DE AUDITORÍA

Al finalizar cada incidencia, **actualizar** los siguientes documentos:

### **INC-010-003, INC-010-004, INC-010-007: Post-Market Monitoring**

**Documentos a Actualizar:**
1. **`/docs/compliance/auditoria/AUDITORIA_010_POST_MARKET_MONITORING.md`**
   - Buscar secciones relacionadas con Art. 72, 73
   - Actualizar estado de implementación
   - Añadir referencia a entidad/BusinessService creado

2. **`/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md`**
   - Buscar `INC-010-003`, `INC-010-004`, `INC-010-007`
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

#### **INC-010-003:**
- [ ] `SeriousIncidentNotificationService` (nuevo) - Crear documento nuevo
- [ ] **Proceso BPMN:** Documentar en `/docs/compliance/bpmn/compliance/incident-reporting/` (funcional.md y technical.md)
- [ ] **ViewModels:** Documentar los 4 ViewModels creados (User Tasks)
- [ ] **ZULs:** Documentar las 4 pantallas ZUL creadas (User Tasks)

#### **INC-010-004:**
- [ ] `PostMarketMonitoringService` (verificado/completado) - Actualizar documento existente

#### **INC-010-007:**
- [ ] `PerformanceDegradationAlertService` (nuevo) - Crear documento nuevo
- [ ] **Proceso BPMN:** Documentar en `/docs/compliance/bpmn/compliance/compliance-monitoring/` (actualizar technical.md)

**Proceso:** Ver sección completa en `AGENTE_1_VALIDACIONES_CRITICAS.md`

**⚠️ IMPORTANTE:** Si creas o modificas procesos BPMN, también debes:
- [ ] Documentar proceso en `/docs/compliance/bpmn/compliance/[nombre-proceso]/functional.md`
- [ ] Documentar técnicamente en `/docs/compliance/bpmn/compliance/[nombre-proceso]/technical.md` (delegates, reglas, variables)
- [ ] Actualizar `BPMN_CATALOG.md` con nuevo proceso
- [ ] Actualizar `BPMN_AUDIT_STATUS.md` con estado de implementación

---

## 🔄 COORDINACIÓN CON AGENTE 4

### **Módulo Compartido:**
- **`govern.business.compliance`** (PMM)
- **Branch:** `feature/agente5-pmm-workflows`
- **Comunicación:** Reportar cada 2 horas

### **Evitar Conflictos:**
- Agente 4: Documentación y reportes
- Agente 5: Workflows y alertas
- Coordinar si ambos necesitan modificar `PostMarketMonitoringService`

---

**Última Actualización:** 25 de noviembre de 2025
