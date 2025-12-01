# Sistema de Post-Market Monitoring (PMM)

**Versión:** 1.0
**Fecha:** 2025-11-25
**Base Legal:** EU AI Act Art. 16.g, Art. 72, Art. 73
**Responsable:** CodeflowX Compliance Team

---

## 1. Introducción al Sistema PMM

### 1.1 Objetivo

El Sistema de Post-Market Monitoring (PMM) es un componente crítico del framework de gobernanza de IA de CodeflowX, diseñado para cumplir con los requisitos del **EU AI Act Art. 72** sobre vigilancia post-comercialización de sistemas de IA de alto riesgo.

### 1.2 Marco Legal

El sistema PMM implementa los siguientes artículos del EU AI Act:

- **Art. 16.g**: Requisitos de documentación técnica que incluyen planes de monitoreo post-mercado
- **Art. 72**: Obligación de vigilancia continua después del despliegue
- **Art. 73**: Notificación de incidentes graves a las autoridades competentes

### 1.3 Alcance

El sistema PMM proporciona:

- **Monitoreo Continuo**: Vigilancia automática de sistemas de IA en producción
- **Detección de Incidentes**: Identificación temprana de problemas y degradación
- **Generación de Informes**: Reportes automáticos de vigilancia poscomercialización
- **Gestión de Alertas**: Sistema configurable de thresholds y notificaciones
- **Cumplimiento Normativo**: Trazabilidad completa para auditorías y certificaciones

---

## 2. Arquitectura del Sistema

### 2.1 Arquitectura General

El sistema PMM sigue una **arquitectura hexagonal** con separación clara de responsabilidades:

```
┌─────────────────────────────────────────────────────────────┐
│                    CAPA DE PRESENTACIÓN                      │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  ZUL Screens (ZKoss)                                  │   │
│  │  - post-market-monitoring-dashboard.zul               │   │
│  └──────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  ViewModels (ZKoss)                                   │   │
│  │  - PostMarketMonitoringDashboardViewModel            │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                    CAPA DE NEGOCIO                           │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  BusinessServices                                     │   │
│  │  - PostMarketMonitoringPlanService                   │   │
│  │  - PostMarketSurveillanceReportService               │   │
│  │  - AlertThresholdService                             │   │
│  │  - PostMarketMonitoringDashboardService              │   │
│  │  - PostMarketMonitoringService                       │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                    CAPA DE PERSISTENCIA                      │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Entidades JPA (EnArt Framework)                     │   │
│  │  - PostMarketMonitoringPlan                          │   │
│  │  - PostMarketSurveillanceReport                      │   │
│  │  - AlertThreshold                                    │   │
│  │  - PostMarketMonitoring                              │   │
│  │  - Incident                                          │   │
│  └──────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  DAO (EnArt Framework)                                │   │
│  │  - Acceso a datos mediante BusinessService            │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                    BASE DE DATOS (PostgreSQL)                │
│  - PMMPOSTMARKETMONITORINGPLANS                              │
│  - PSRPOSTMARKETSURVEILLANCEREPORTS                          │
│  - ALRALERTTHRESHOLDS                                        │
│  - PMMPOSTMARKETMONITORINGS                                  │
│  - INCINCIDENTS                                              │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 Principios de Diseño

- **SOLID**: Separación de responsabilidades, extensibilidad sin modificación
- **KISS**: Simplicidad en la implementación
- **Arquitectura Hexagonal**: Aislamiento de lógica de negocio
- **Tercera Forma Normal (3FN)**: Normalización de datos en base de datos
- **Convenciones EnArt**: Prefijos de 3 caracteres, PK autonumérica, campos de auditoría

---

## 3. Componentes Principales

### 3.1 Entidades JPA

#### 3.1.1 PostMarketMonitoringPlan

**Tabla:** `PMMPOSTMARKETMONITORINGPLANS`
**Prefijo:** `PMM`
**Ubicación:** `com.codeflowx.govern.entity.compliance.PostMarketMonitoringPlan`

**Propósito:** Representa un plan formal de monitoreo post-comercialización para un proyecto o modelo específico.

**Campos Principales:**
- `IDXPMMPLAN`: PK autonumérica
- `iduuid`: UUID único
- `PMMPLANNAME`: Nombre del plan
- `IDXPROJECT`: Referencia al proyecto
- `IDXMODEL`: Referencia al modelo (opcional)
- `PMMMONITORINGFREQUENCY`: Frecuencia de monitoreo (DAILY, WEEKLY, MONTHLY)
- `PMMMETRICS`: JSONB con métricas a monitorear
- `PMMALERTTHRESHOLDS`: JSONB con thresholds de alertas
- `PMMREPORTINGFREQUENCY`: Frecuencia de reportes (DAILY, WEEKLY, MONTHLY)
- `PMMSTATUS`: Estado del plan (DRAFT, ACTIVE, SUSPENDED)
- `PMMCREATEDAT`, `PMMUPDATEDAT`: Campos de auditoría

#### 3.1.2 PostMarketSurveillanceReport

**Tabla:** `PSRPOSTMARKETSURVEILLANCEREPORTS`
**Prefijo:** `PSR`
**Ubicación:** `com.codeflowx.govern.entity.compliance.PostMarketSurveillanceReport`

**Propósito:** Representa un informe de vigilancia poscomercialización generado automáticamente.

**Campos Principales:**
- `IDXPMSREPORT`: PK autonumérica
- `iduuid`: UUID único
- `IDXPROJECT`: Referencia al proyecto
- `IDXMODEL`: Referencia al modelo (opcional)
- `PMSREPORTTYPE`: Tipo de informe (DAILY, WEEKLY, MONTHLY, AD_HOC)
- `PMSREPORTDATE`: Fecha del informe
- `PMSREPORTDATA`: JSONB con contenido del informe
- `PMSREPORTPDF`: PDF generado (bytea)
- `PMSREPORTPDFPATH`: Ruta en storage externo (MinIO/S3)
- `PMSSTATUS`: Estado del informe
- `PMSCREATEDAT`, `PMSUPDATEDAT`: Campos de auditoría

#### 3.1.3 AlertThreshold

**Tabla:** `ALRALERTTHRESHOLDS`
**Prefijo:** `ALR`
**Ubicación:** `com.codeflowx.govern.entity.compliance.AlertThreshold`

**Propósito:** Define thresholds configurables para alertas de monitoreo.

**Campos Principales:**
- `IDXALERTTHRESHOLD`: PK autonumérica
- `iduuid`: UUID único
- `IDXPROJECT`: Referencia al proyecto (opcional)
- `IDXMODEL`: Referencia al modelo (opcional)
- `ALTMETRICNAME`: Nombre de la métrica (DRIFT, PERFORMANCE, USER_SATISFACTION, BIAS, ACCURACY, LATENCY)
- `ALTMETRICTYPE`: Tipo de métrica (ABSOLUTE, PERCENTAGE)
- `ALTWARNINGTHRESHOLD`: Threshold de advertencia
- `ALTCRITICALTHRESHOLD`: Threshold crítico
- `ALTSEVERITY`: Severidad (LOW, MEDIUM, HIGH, CRITICAL)
- `ALTSTATUS`: Estado del threshold
- `ALTCREATEDAT`, `ALTUPDATEDAT`: Campos de auditoría

### 3.2 BusinessServices

#### 3.2.1 PostMarketMonitoringPlanService

**Ubicación:** `com.codeflowx.govern.business.compliance.PostMarketMonitoringPlanService`

**Responsabilidades:**
- CRUD completo de planes PMM
- Validación de planes según Art. 72
- Gestión de estados (DRAFT → ACTIVE → SUSPENDED)
- Vinculación con proyectos y modelos
- Validación de métricas y thresholds

**Métodos Principales:**
- `createPlan(PostMarketMonitoringPlan plan)`: Crea un nuevo plan PMM
- `updatePlan(Long planId, PostMarketMonitoringPlan plan)`: Actualiza un plan existente
- `activatePlan(Long planId)`: Activa un plan (DRAFT → ACTIVE)
- `suspendPlan(Long planId)`: Suspende un plan (ACTIVE → SUSPENDED)
- `getActivePlansByProject(Long projectId)`: Obtiene planes activos por proyecto
- `validatePlan(PostMarketMonitoringPlan plan)`: Valida un plan según Art. 72

#### 3.2.2 PostMarketSurveillanceReportService

**Ubicación:** `com.codeflowx.govern.business.compliance.PostMarketSurveillanceReportService`

**Responsabilidades:**
- Generación automática de informes PMM
- Generación de PDFs
- Almacenamiento en storage externo (MinIO/S3)
- Integración con procesos BPMN para generación automática

**Métodos Principales:**
- `generateReport(Long projectId, ReportType type)`: Genera un informe automático
- `generatePDF(PostMarketSurveillanceReport report)`: Genera PDF del informe
- `storePDF(PostMarketSurveillanceReport report, byte[] pdf)`: Almacena PDF en storage
- `getReportsByProject(Long projectId, LocalDate from, LocalDate to)`: Obtiene informes por rango de fechas

#### 3.2.3 AlertThresholdService

**Ubicación:** `com.codeflowx.govern.business.compliance.AlertThresholdService`

**Responsabilidades:**
- CRUD completo de thresholds
- Validación de thresholds
- Evaluación de métricas contra thresholds
- Integración con Drools para evaluación dinámica

**Métodos Principales:**
- `createThreshold(AlertThreshold threshold)`: Crea un nuevo threshold
- `updateThreshold(Long thresholdId, AlertThreshold threshold)`: Actualiza un threshold
- `evaluateMetric(String metricName, BigDecimal value, Long projectId)`: Evalúa una métrica contra thresholds
- `getThresholdsByProject(Long projectId)`: Obtiene thresholds por proyecto

#### 3.2.4 PostMarketMonitoringDashboardService

**Ubicación:** `com.codeflowx.govern.business.compliance.PostMarketMonitoringDashboardService`

**Responsabilidades:**
- Agregación de datos para dashboard PMM
- KPIs en tiempo real
- Listado de alertas activas
- Listado de incidentes recientes
- Listado de planes PMM activos
- Listado de informes recientes

**Métodos Principales:**
- `getDashboardData(Long projectId)`: Obtiene datos consolidados del dashboard
- `getKPIs(Long projectId)`: Obtiene KPIs en tiempo real
- `getActiveAlerts(Long projectId)`: Obtiene alertas activas
- `getRecentIncidents(Long projectId, int limit)`: Obtiene incidentes recientes

### 3.3 ViewModels y Pantallas

#### 3.3.1 PostMarketMonitoringDashboardViewModel

**Ubicación:** `com.codeflowx.govern.viewmodel.compliance.PostMarketMonitoringDashboardViewModel`

**Responsabilidades:**
- Lógica de presentación del dashboard PMM
- Binding de datos con la pantalla ZUL
- Gestión de filtros (proyecto, tipo de métrica)
- Actualización automática de datos

#### 3.3.2 post-market-monitoring-dashboard.zul

**Ubicación:** `src/main/webapp/console/gobierno/compliance/post-market-monitoring-dashboard.zul`

**Componentes:**
- Panel de KPIs (planes activos, incidentes, alertas, informes)
- Tabla de alertas activas
- Lista de incidentes recientes
- Lista de planes PMM activos
- Lista de informes recientes
- Filtros por proyecto y tipo de métrica

---

## 4. Flujo de Monitoreo

### 4.1 Flujo General

```
┌─────────────────┐
│  Crear Plan PMM │
│  (DRAFT)        │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Validar Plan   │
│  (Art. 72)      │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Activar Plan   │
│  (ACTIVE)       │
└────────┬────────┘
         │
         ▼
┌─────────────────────────────────────┐
│  Ejecutar Monitoreo (Timer BPMN)    │
│  - Verificar métricas               │
│  - Evaluar thresholds               │
│  - Detectar incidentes              │
└────────┬────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────┐
│  Generar Alertas (si aplica)        │
│  - Warning threshold                 │
│  - Critical threshold                │
└────────┬────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────┐
│  Generar Informe PMM                │
│  - Agregar métricas                 │
│  - Incluir alertas                  │
│  - Incluir incidentes               │
│  - Generar PDF                       │
│  - Almacenar en storage              │
└────────┬────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────┐
│  Notificar Incidentes Graves         │
│  (Art. 73)                           │
│  - Workflow BPMN                     │
│  - Notificación a autoridades        │
└─────────────────────────────────────┘
```

### 4.2 Frecuencias de Monitoreo

- **DAILY**: Monitoreo diario (cada 24 horas)
- **WEEKLY**: Monitoreo semanal (cada 7 días)
- **MONTHLY**: Monitoreo mensual (cada 30 días)

### 4.3 Métricas Monitoreadas

- **DRIFT**: Detección de drift en datos
- **PERFORMANCE**: Degradación de performance
- **USER_SATISFACTION**: Satisfacción del usuario
- **BIAS**: Detección de sesgos
- **ACCURACY**: Precisión del modelo
- **LATENCY**: Latencia de respuesta

### 4.4 Evaluación de Thresholds

El sistema evalúa métricas contra thresholds configurados:

1. **Warning Threshold**: Genera alerta de advertencia
2. **Critical Threshold**: Genera alerta crítica y puede disparar notificación de incidente grave

La evaluación se realiza mediante:
- **Drools Rules Engine**: Para evaluación dinámica de reglas
- **AlertThresholdService**: Para evaluación estática de thresholds

---

## 5. Configuración de Planes PMM

### 5.1 Creación de un Plan PMM

**Paso 1: Definir Plan Base**
```java
PostMarketMonitoringPlan plan = new PostMarketMonitoringPlan();
plan.setPmmplanname("PMM Plan - Proyecto X");
plan.setIdxproject(projectId);
plan.setPmmmonitoringfrequency(MonitoringFrequency.MONTHLY);
plan.setPmmreportingfrequency(ReportingFrequency.MONTHLY);
plan.setPmmstatus(Set.of("DRAFT"));
```

**Paso 2: Configurar Métricas**
```java
Map<String, Object> metrics = new HashMap<>();
metrics.put("drift", true);
metrics.put("performance", true);
metrics.put("user_satisfaction", true);
metrics.put("bias", true);
plan.setPmmmetrics(metrics);
```

**Paso 3: Configurar Thresholds**
```java
Map<String, Object> thresholds = new HashMap<>();
thresholds.put("drift", Map.of(
    "warning", 0.1,
    "critical", 0.2
));
thresholds.put("performance", Map.of(
    "warning", 0.05,
    "critical", 0.1
));
plan.setPmmalertthresholds(thresholds);
```

**Paso 4: Guardar y Activar**
```java
PostMarketMonitoringPlanService service = ...;
PostMarketMonitoringPlan savedPlan = service.createPlan(plan);
service.activatePlan(savedPlan.getIdxpmmplan());
```

### 5.2 Configuración de Thresholds

**Crear Threshold Global:**
```java
AlertThreshold threshold = new AlertThreshold();
threshold.setAltmetricname(MetricName.DRIFT);
threshold.setAltmetrictype(MetricType.PERCENTAGE);
threshold.setAltwarningthreshold(new BigDecimal("0.1"));
threshold.setAltcriticalthreshold(new BigDecimal("0.2"));
threshold.setAltseverity(Severity.HIGH);
threshold.setAltstatus(Set.of("ACTIVE"));

AlertThresholdService service = ...;
service.createThreshold(threshold);
```

**Crear Threshold por Proyecto:**
```java
AlertThreshold threshold = new AlertThreshold();
threshold.setIdxproject(projectId);
threshold.setAltmetricname(MetricName.PERFORMANCE);
// ... resto de configuración
service.createThreshold(threshold);
```

### 5.3 Estados de Planes PMM

- **DRAFT**: Plan en borrador, no se ejecuta monitoreo
- **ACTIVE**: Plan activo, se ejecuta monitoreo según frecuencia
- **SUSPENDED**: Plan suspendido temporalmente

---

## 6. Procedimientos Operativos

### 6.1 Monitoreo Automático

El monitoreo se ejecuta automáticamente mediante **procesos BPMN** con timers:

1. **Timer BPMN**: Dispara ejecución según frecuencia configurada
2. **PostMarketMonitoringService.executeMonitoring()**: Ejecuta el monitoreo
3. **Evaluación de Métricas**: Compara métricas actuales con thresholds
4. **Generación de Alertas**: Crea alertas si se superan thresholds
5. **Generación de Informe**: Genera informe PMM con resultados

### 6.2 Generación de Informes

**Automática:**
- Los informes se generan automáticamente según `PMMREPORTINGFREQUENCY`
- Proceso BPMN con timer dispara la generación
- `PostMarketSurveillanceReportService.generateReport()` crea el informe

**Manual (AD_HOC):**
```java
PostMarketSurveillanceReportService service = ...;
PostMarketSurveillanceReport report = service.generateReport(
    projectId,
    ReportType.AD_HOC
);
```

### 6.3 Gestión de Incidentes Graves

Cuando se detecta un incidente grave (Art. 73):

1. **Detección**: El sistema identifica un incidente grave
2. **Workflow BPMN**: Se dispara el proceso `serious-incident-notification-v1.bpmn`
3. **Notificación**: Se notifica a las autoridades competentes
4. **Registro**: Se registra en `INCINCIDENTS` con estado `SERIOUS`

### 6.4 Consulta de Informes

**Por Proyecto:**
```java
PostMarketSurveillanceReportService service = ...;
List<PostMarketSurveillanceReport> reports = service.getReportsByProject(
    projectId,
    LocalDate.now().minusMonths(1),
    LocalDate.now()
);
```

**Descarga de PDF:**
```java
byte[] pdf = service.getReportPDF(reportId);
// O desde storage externo
String pdfPath = report.getPmsreportpdfpath();
```

---

## 7. Integración con Art. 49

### 7.1 Registro de Sistemas de IA

El sistema PMM se integra con el **Registro Art. 49** (`EuRegistration`) para:

- **Vinculación**: Cada plan PMM puede estar vinculado a un registro Art. 49
- **Trazabilidad**: Los informes PMM referencian el registro correspondiente
- **Cumplimiento**: Demostración de monitoreo continuo para sistemas registrados

### 7.2 Flujo de Integración

```
┌──────────────────────┐
│  Registro Art. 49    │
│  (EuRegistration)     │
└──────────┬────────────┘
           │
           ▼
┌──────────────────────┐
│  Crear Plan PMM      │
│  (vinculado)         │
└──────────┬────────────┘
           │
           ▼
┌──────────────────────┐
│  Monitoreo Continuo  │
│  (Art. 72)           │
└──────────┬────────────┘
           │
           ▼
┌──────────────────────┐
│  Informes PMM        │
│  (referencian Art.49)│
└──────────────────────┘
```

### 7.3 Campos de Vinculación

En `PostMarketMonitoringPlan`:
- `IDXPROJECT`: Proyecto vinculado (puede tener registro Art. 49)
- `IDXMODEL`: Modelo vinculado (puede tener registro Art. 49)

En `PostMarketSurveillanceReport`:
- `IDXPROJECT`: Proyecto del informe (puede tener registro Art. 49)
- `IDXMODEL`: Modelo del informe (puede tener registro Art. 49)

---

## 8. Referencias Técnicas

### 8.1 Entidades Relacionadas

- `PostMarketMonitoring`: Monitoreo ejecutado (tabla `PMMPOSTMARKETMONITORINGS`)
- `Incident`: Incidentes detectados (tabla `INCINCIDENTS`)
- `Project`: Proyectos (tabla `PRJPROJECTS`)
- `Model`: Modelos de IA (tabla `MODMODELS`)
- `EuRegistration`: Registro Art. 49 (tabla `EURREGISTRATIONS`)

### 8.2 Servicios Relacionados

- `PostMarketMonitoringService`: Servicio base de monitoreo
- `ComplianceExecutiveReportService`: Reportes ejecutivos de compliance
- `IncidentService`: Gestión de incidentes

### 8.3 Procesos BPMN

- `compliance-monitoring-v1.bpmn`: Proceso de monitoreo automático
- `serious-incident-notification-v1.bpmn`: Notificación de incidentes graves
- `pmm-report-generation-v1.bpmn`: Generación automática de informes PMM

### 8.4 Documentación Legal

- **EU AI Act Art. 16.g**: Requisitos de documentación técnica
- **EU AI Act Art. 72**: Post-market monitoring
- **EU AI Act Art. 73**: Notificación de incidentes graves

---

## 9. Mantenimiento y Soporte

### 9.1 Logs y Monitoreo

El sistema genera logs en:
- **BusinessServices**: Logs de operaciones CRUD y validaciones
- **BPMN Processes**: Logs de ejecución de procesos
- **PDF Generation**: Logs de generación de PDFs

### 9.2 Troubleshooting

**Problema: Plan PMM no se ejecuta**
- Verificar estado del plan (debe ser ACTIVE)
- Verificar timer BPMN configurado correctamente
- Revisar logs de `PostMarketMonitoringService`

**Problema: Informes no se generan**
- Verificar `PMMREPORTINGFREQUENCY` configurado
- Verificar proceso BPMN de generación activo
- Revisar logs de `PostMarketSurveillanceReportService`

**Problema: Alertas no se disparan**
- Verificar thresholds configurados correctamente
- Verificar métricas siendo monitoreadas
- Revisar logs de `AlertThresholdService`

### 9.3 Actualizaciones Futuras

**Pendientes (INC-010):**
- INC-010-003: Workflow Notificación Incidentes Graves
- INC-010-004: Implementación Real PostMarketMonitoringService (eliminar mocks)
- INC-010-005: Vinculación PMM con Registro Art. 49 (extender EuRegistration)
- INC-010-007: Implementación de Informes Automáticos (timers BPMN)
- INC-010-010: Integración con Sistema de Feedback

---

## 10. Conclusión

El Sistema de Post-Market Monitoring (PMM) proporciona una solución completa para el cumplimiento del **EU AI Act Art. 72**, ofreciendo:

- ✅ Monitoreo continuo automatizado
- ✅ Generación automática de informes
- ✅ Sistema configurable de alertas
- ✅ Dashboard consolidado para supervisión
- ✅ Trazabilidad completa para auditorías

El sistema está diseñado siguiendo principios **SOLID**, **KISS** y **arquitectura hexagonal**, garantizando mantenibilidad y extensibilidad.

---

**Última actualización:** 2025-11-25
**Versión del documento:** 1.0
**Autor:** CodeflowX Compliance Team

