# INCIDENCIAS Y RECOMENDACIONES - POST MARKET MONITORING (PMM)
**Fecha:** Diciembre 2025  
**Auditor:** Sistema de Gobierno de IA - CodeflowX  
**Base Legal:** EU AI Act Art. 72, Art. 16.g, Art. 16.h, Art. 73  
**Documento Relacionado:** `AUDITORIA_010_POST_MARKET_MONITORING.md`

---

## RESUMEN EJECUTIVO

**Total Incidencias:** 15  
**Críticas:** 6  
**Altas:** 5 (1 resuelta: INC-010-007)  
**Medias:** 4 (1 resuelta: INC-010-012)  

**Cumplimiento Actual:** ⚠️ **53%** (mejorado desde 45%)  
**Cumplimiento Objetivo:** ✅ **100%** (Certification Ready)  
**Última Actualización:** 2025-01-21 (INC-010-007 resuelta)

---

## INCIDENCIAS CRÍTICAS (Certification Blocker)

### INC-010-001: Falta Documentación Formal del Sistema PMM
**Artículo:** EU AI Act Art. 16.g  
**Prioridad:** 🔴 **CRÍTICA**  
**Impacto:** Certification Blocker  
**Referencia:** GAP-017

**Descripción:**
No existe documentación formal del sistema de Post Market Monitoring según Art. 16.g. El sistema está implementado parcialmente pero no está documentado como requiere el AI Act.

**Evidencia:**
- ✅ Servicio `PostMarketMonitoringService` implementado
- ✅ Proceso BPMN `compliance-monitoring-v1.bpmn` documentado
- ❌ Falta entidad `PostMarketMonitoringPlan`
- ❌ Falta documento formal del plan PMM por proyecto/modelo

**Recomendación:**
1. **Crear entidad JPA `PostMarketMonitoringPlan`:**
   ```sql
   CREATE TABLE PMMPOSTMARKETMONITORINGPLANS (
       iduuid UUID UNIQUE,
       IDXPMMPLAN BIGSERIAL PRIMARY KEY,
       PMMPLANNAME VARCHAR(255) NOT NULL,
       IDXPROJECT BIGINT NOT NULL,
       IDXMODEL BIGINT,
       PMMMONITORINGFREQUENCY VARCHAR(50) NOT NULL, -- DAILY, WEEKLY, MONTHLY
       PMMMETRICS JSONB NOT NULL, -- Lista de métricas a monitorear
       PMMALERTTHRESHOLDS JSONB NOT NULL, -- Thresholds por métrica
       PMMREPORTINGFREQUENCY VARCHAR(50) NOT NULL, -- DAILY, WEEKLY, MONTHLY
       PMMSTATUS TEXT[] NOT NULL, -- DRAFT, ACTIVE, SUSPENDED
       PMMCREATEDAT TIMESTAMP NOT NULL,
       PMMUPDATEDAT TIMESTAMP
   );
   ```

2. **Crear servicio `PostMarketMonitoringPlanService`:**
   - CRUD de planes PMM
   - Validación de planes según Art. 72
   - Vinculación con proyectos y modelos

3. **Crear ViewModel y pantalla ZUL:**
   - Gestión de planes PMM
   - Asignación de planes a proyectos/modelos
   - Visualización de planes activos

4. **Documentar sistema PMM:**
   - Manual técnico del sistema
   - Guía de configuración de planes
   - Procedimientos operativos

**Esfuerzo Estimado:** 3 días  
**Responsable:** Backend Team + Documentation Team

---

### INC-010-002: Falta Generación Automática de Post-Market Surveillance Report
**Artículo:** EU AI Act Art. 72  
**Prioridad:** 🔴 **CRÍTICA**  
**Impacto:** Certification Blocker  
**Referencia:** GAP-017

**Descripción:**
No existe generación automática de informes de vigilancia poscomercialización según Art. 72. Los informes son requeridos para demostrar supervisión continua.

**Evidencia:**
- ✅ Métricas almacenadas en `MONMONITORINGMETRICS`
- ✅ Alertas almacenadas en `MONMONITORINGALERTS`
- ❌ No hay generación automática de informes
- ❌ No hay template de informe

**Recomendación:**
1. **Crear entidad `PostMarketSurveillanceReport`:**
   ```sql
   CREATE TABLE PMSPOSTMARKETSURVEILLANCEREPORTS (
       iduuid UUID UNIQUE,
       IDXPMSREPORT BIGSERIAL PRIMARY KEY,
       IDXPROJECT BIGINT NOT NULL,
       IDXMODEL BIGINT,
       PMSREPORTTYPE VARCHAR(50) NOT NULL, -- DAILY, WEEKLY, MONTHLY, AD_HOC
       PMSREPORTDATE DATE NOT NULL,
       PMSREPORTDATA JSONB NOT NULL, -- Contenido del informe
       PMSREPORTPDF BYTEA, -- PDF generado
       PMSSTATUS TEXT[] NOT NULL, -- DRAFT, GENERATED, APPROVED
       PMSCREATEDAT TIMESTAMP NOT NULL
   );
   ```

2. **Crear servicio `PostMarketSurveillanceReportService`:**
   - Generación automática de informes (diario, semanal, mensual)
   - Template de informe con secciones requeridas:
     - Resumen ejecutivo
     - Métricas monitoreadas
     - Alertas generadas
     - Incidentes detectados
     - Tendencias y análisis
     - Acciones correctivas
   - Generación de PDF
   - Almacenamiento en storage (MinIO/S3)

3. **Integrar con proceso BPMN:**
   - Timer para generación automática
   - Notificación a stakeholders
   - Aprobación de informes mensuales

4. **Crear endpoint REST:**
   - `GET /api/v1/pmm/reports` - Listar informes
   - `GET /api/v1/pmm/reports/{id}/pdf` - Descargar PDF
   - `POST /api/v1/pmm/reports/generate` - Generar informe ad-hoc

**Esfuerzo Estimado:** 4 días  
**Responsable:** Backend Team + Reporting Team

---

### INC-010-003: Falta Workflow Completo de Notificación de Incidentes Graves
**Artículo:** EU AI Act Art. 73  
**Prioridad:** 🔴 **CRÍTICA**  
**Impacto:** Certification Blocker

**Descripción:**
No existe implementación completa del workflow de notificación de incidentes graves según Art. 73. Los proveedores deben notificar a las autoridades competentes cualquier incidente grave.

**Evidencia:**
- ✅ Proceso BPMN `incident-reporting-process.bpmn` documentado
- ❌ Falta entidad `SeriousIncidentReport`
- ❌ Falta definición de umbrales para "incidente grave"
- ❌ Falta integración con autoridades competentes

**Recomendación:**
1. **Crear entidad `SeriousIncidentReport`:**
   ```sql
   CREATE TABLE SIRSERIOUSINCIDENTREPORTS (
       iduuid UUID UNIQUE,
       IDXSIRREPORT BIGSERIAL PRIMARY KEY,
       IDXPROJECT BIGINT NOT NULL,
       IDXMODEL BIGINT,
       SIRINCIDENTTYPE VARCHAR(100) NOT NULL, -- DEATH, SERIOUS_INJURY, DATA_BREACH, etc.
       SIRINCIDENTDESCRIPTION TEXT NOT NULL,
       SIRIMPACTASSESSMENT JSONB NOT NULL, -- Evaluación de impacto
       SIRROOTCAUSE TEXT, -- Causa raíz (si disponible)
       SIRCORRECTIVEACTIONS JSONB NOT NULL, -- Acciones correctivas
       SIRNOTIFICATIONSTATUS TEXT[] NOT NULL, -- PENDING, NOTIFIED, ACKNOWLEDGED
       SIRNOTIFIEDAT TIMESTAMP,
       SIRAUTHORITYCONTACT JSONB, -- Información de contacto autoridad
       SIRCREATEDAT TIMESTAMP NOT NULL
   );
   ```

2. **Definir umbrales para "incidente grave":**
   - Muerte o lesión grave
   - Violación masiva de datos personales
   - Degradación crítica de performance (>50%)
   - Drift extremo (>60%)
   - Compromiso de seguridad
   - Violación de derechos fundamentales

3. **Crear servicio `SeriousIncidentReportService`:**
   - Detección automática de incidentes graves
   - Generación de reporte
   - Notificación a autoridades (API o email)
   - Tracking de estado de notificación

4. **Integrar con proceso BPMN:**
   - Detección automática en `compliance-monitoring-v1.bpmn`
   - Workflow de notificación
   - Escalación si no se notifica en tiempo (< 15 días según Art. 73)

5. **Crear configuración de autoridades:**
   - Entidad `CompetentAuthority` con contactos
   - Configuración por país/sector
   - Templates de notificación

**Esfuerzo Estimado:** 5 días  
**Responsable:** Backend Team + Compliance Team

---

### INC-010-004: Implementación Mock en PostMarketMonitoringService
**Artículo:** EU AI Act Art. 72  
**Prioridad:** 🔴 **CRÍTICA**  
**Impacto:** Funcionalidad no operativa

**Descripción:**
El servicio `PostMarketMonitoringService` tiene implementación mock con TODOs. No realiza verificaciones reales de métricas post-market.

**Evidencia:**
```java
// TODO: Implementar verificación real de métricas post-market
// Drift detection, performance degradation, user satisfaction
PostMarketResult result = new PostMarketResult();
result.setDriftDetected(false);
result.setPerformanceDegradation(false);
// ... valores hardcodeados
```

**Recomendación:**
1. **Integrar con `leka-bias-detection-service`:**
   - Endpoint `/api/drift/detect`
   - Comparación con baseline histórico
   - Cálculo de métricas estadísticas (KS test, PSI)

2. **Integrar con `aio-telemetry-service`:**
   - Consulta de métricas en tiempo real
   - Comparación con baseline de performance
   - Alertas por degradación

3. **Integrar con sistema de feedback:**
   - Consulta de feedback de usuarios
   - Análisis de sentimiento
   - Cálculo de satisfacción

4. **Implementar cálculo real de métricas:**
   - Drift score basado en estadísticas reales
   - Performance score basado en métricas reales
   - User satisfaction score basado en feedback real

**Esfuerzo Estimado:** 3 días  
**Responsable:** Backend Team + MLOps Team

---

### INC-010-005: Falta Vinculación PMM con Registro Art. 49
**Artículo:** EU AI Act Art. 16.h  
**Prioridad:** 🔴 **CRÍTICA**  
**Impacto:** Certification Blocker

**Descripción:**
El sistema PMM no está vinculado con el registro en Base de Datos UE según Art. 49. Art. 16.h requiere registrar el sistema de conformidad.

**Evidencia:**
- ✅ Entidad `EuRegistration` implementada
- ❌ Falta campo en `EuRegistration` para vincular PMM
- ❌ Falta validación de PMM en proceso de registro

**Recomendación:**
1. **Extender entidad `EuRegistration`:**
   - Agregar campo `REGPMMPLANID` (FK a `PMMPOSTMARKETMONITORINGPLANS`)
   - Agregar campo `REGPMMSTATUS` (ACTIVE, SUSPENDED)
   - Validar que PMM esté activo antes de registrar

2. **Actualizar proceso de registro:**
   - Validar existencia de plan PMM activo
   - Incluir información de PMM en submission a BD UE
   - Actualizar registro si PMM cambia

3. **Crear vista de relación:**
   - Vista que muestre registro + PMM plan
   - Dashboard de cumplimiento Art. 16.h

**Esfuerzo Estimado:** 2 días  
**Responsable:** Backend Team

---

### INC-010-006: Falta Entidad para Configuración de Thresholds
**Artículo:** EU AI Act Art. 72  
**Prioridad:** 🔴 **CRÍTICA**  
**Impacto:** Flexibilidad y configuración

**Descripción:**
No existe entidad para configurar thresholds de alertas por proyecto/modelo. Los thresholds están hardcodeados en reglas Drools.

**Evidencia:**
- ✅ Reglas Drools con thresholds fijos
- ❌ No hay configuración por proyecto/modelo
- ❌ No hay configuración por tipo de métrica

**Recomendación:**
1. **Crear entidad `AlertThreshold`:**
   ```sql
   CREATE TABLE ALTALERTTHRESHOLDS (
       iduuid UUID UNIQUE,
       IDXALERTTHRESHOLD BIGSERIAL PRIMARY KEY,
       IDXPROJECT BIGINT,
       IDXMODEL BIGINT,
       ALTMETRICNAME VARCHAR(100) NOT NULL, -- DRIFT, PERFORMANCE, USER_SATISFACTION
       ALTMETRICTYPE VARCHAR(50) NOT NULL, -- ABSOLUTE, PERCENTAGE
       ALTWARNINGTHRESHOLD DECIMAL NOT NULL,
       ALTCRITICALTHRESHOLD DECIMAL NOT NULL,
       ALTSEVERITY VARCHAR(50) NOT NULL, -- LOW, MEDIUM, HIGH, CRITICAL
       ALTSTATUS TEXT[] NOT NULL, -- ACTIVE, INACTIVE
       ALTCREATEDAT TIMESTAMP NOT NULL,
       ALTUPDATEDAT TIMESTAMP
   );
   ```

2. **Crear servicio `AlertThresholdService`:**
   - CRUD de thresholds
   - Validación de thresholds
   - Aplicación de thresholds en monitoreo

3. **Integrar con reglas Drools:**
   - Cargar thresholds dinámicamente
   - Aplicar thresholds en evaluación de alertas

4. **Crear UI para configuración:**
   - Pantalla de configuración de thresholds
   - Templates por tipo de modelo/sector

**Esfuerzo Estimado:** 2 días  
**Responsable:** Backend Team + Frontend Team

---

## INCIDENCIAS ALTAS

### INC-010-007: Falta Implementación de Informes Automáticos
**Artículo:** EU AI Act Art. 72  
**Prioridad:** 🟡 **ALTA**  
**Estado:** ✅ **RESUELTO** - 2025-01-21

**Descripción:**
No hay generación automática de informes diarios/mensuales. Solo estaba documentado pero no implementado.

**Solución Implementada:**
1. ✅ **Proceso BPMN:** `pmm-reports-v1.bpmn` implementado con timers automáticos
   - Timer diario: Generación automática cada día a las 01:00
   - Timer mensual: Generación automática el día 1 de cada mes a las 02:00
   - Workflow completo para informes diarios y mensuales
   - Aprobación manual requerida para informes mensuales

2. ✅ **Delegates Java implementados:**
   - `GenerateDailyPmmReportDelegate`: Genera informes diarios para todos los proyectos activos
   - `GenerateMonthlyPmmReportDelegate`: Genera informes mensuales con período completo
   - `NotifyPmmReportDelegate`: Notifica automáticamente a stakeholders cuando se generan informes

3. ✅ **Características:**
   - Generación automática programada mediante timers BPMN
   - Procesamiento de múltiples proyectos en paralelo
   - Notificación automática a stakeholders por informe
   - Almacenamiento de metadatos en variables BPMN
   - Manejo de errores robusto (continúa con otros proyectos si uno falla)

4. ✅ **Integración:**
   - Integrado con `PostMarketMonitoringService` para obtener métricas
   - Integrado con `BusinessService` para obtener proyectos activos
   - Preparado para integración con servicios de generación de PDF y storage

**Archivos:**
- `nocode.service/codeflowx.govern.workflow.lib/src/main/resources/processes/compliance/pmm-reports-v1.bpmn` - Proceso BPMN
- `nocode.service/codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/delegates/compliance/GenerateDailyPmmReportDelegate.java` - Delegate diario
- `nocode.service/codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/delegates/compliance/GenerateMonthlyPmmReportDelegate.java` - Delegate mensual
- `nocode.service/codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/delegates/compliance/NotifyPmmReportDelegate.java` - Delegate notificación

**Referencia:** 
- Ver prompt: `docs/compliance/gaps/prompts/bpmn/INC-010-007_informes_automaticos.md`

**Esfuerzo Estimado:** 2 días ✅ **COMPLETADO**

---

### INC-010-008: Falta Dashboard de Supervisión Continua
**Artículo:** EU AI Act Art. 72  
**Prioridad:** 🟡 **ALTA**

**Descripción:**
Dashboard mencionado en documentación pero requiere verificación de implementación UI.

**Recomendación:**
1. Verificar si existe dashboard
2. Si no existe, crear dashboard con:
   - Métricas en tiempo real
   - Tendencias históricas
   - Alertas activas
   - Estado de proyectos/modelos
3. Exportación CSV/PDF

**Esfuerzo Estimado:** 3 días

---

### INC-010-009: Falta API REST para Consulta de Histórico
**Artículo:** EU AI Act Art. 72  
**Prioridad:** 🟡 **ALTA**

**Descripción:**
No hay endpoints REST para consultar histórico de métricas y alertas.

**Recomendación:**
1. Crear endpoints:
   - `GET /api/v1/pmm/metrics/history`
   - `GET /api/v1/pmm/alerts/history`
   - `GET /api/v1/pmm/incidents/history`
2. Filtros por proyecto, modelo, fecha, tipo
3. Paginación y ordenamiento

**Esfuerzo Estimado:** 2 días

---

### INC-010-010: Falta Integración con Sistema de Feedback
**Artículo:** EU AI Act Art. 72  
**Prioridad:** 🟡 **ALTA**

**Descripción:**
No hay integración con sistema de feedback de usuarios para cálculo de satisfacción.

**Recomendación:**
1. Crear entidad `UserFeedback`
2. Integrar con sistema de tickets/incidentes
3. Implementar análisis de sentimiento
4. Calcular métrica de satisfacción

**Esfuerzo Estimado:** 3 días

---

### INC-010-011: Falta Optimización de Consultas con Vistas Materializadas
**Artículo:** EU AI Act Art. 72  
**Prioridad:** 🟡 **ALTA**

**Descripción:**
Consultas de histórico pueden ser lentas sin optimización.

**Recomendación:**
1. Crear vistas materializadas con TimescaleDB
2. Continuous aggregates para métricas
3. Índices estratégicos
4. Refresh policies

**Esfuerzo Estimado:** 2 días

---

## INCIDENCIAS MEDIAS

### INC-010-012: Falta Análisis de Sentimiento en Feedback
**Prioridad:** 🟢 **MEDIA**

**Recomendación:**
1. Integrar servicio de análisis de sentimiento
2. Clasificar feedback (positivo, neutro, negativo)
3. Calcular score de satisfacción

**Esfuerzo Estimado:** 1 día

---

### INC-010-013: Falta Visualización de Tendencias Avanzadas
**Prioridad:** 🟢 **MEDIA**

**Recomendación:**
1. Gráficos de tendencias temporales
2. Comparación con baseline
3. Predicción de tendencias

**Esfuerzo Estimado:** 2 días

---

### INC-010-014: Falta Configuración de Frecuencias por Proyecto
**Prioridad:** 🟢 **MEDIA**

**Recomendación:**
1. Permitir configurar frecuencia de monitoreo por proyecto
2. No solo 24h fijo
3. UI para configuración

**Esfuerzo Estimado:** 1 día

---

### INC-010-015: Falta Integración con Sistemas Externos (Azure ML, SageMaker)
**Prioridad:** 🟢 **MEDIA**

**Descripción:**
Hay documentación de conectores pero requiere verificación de implementación.

**Recomendación:**
1. Verificar implementación de conectores
2. Completar si falta
3. Integrar con PMM

**Esfuerzo Estimado:** 2 días

---

## PLAN DE ACCIÓN

### Fase 1: Críticas (Certification Blocker) - 19 días
1. INC-010-001: Documentación Formal PMM (3 días)
2. INC-010-002: Generación Automática Reportes (4 días)
3. INC-010-003: Workflow Incidentes Graves (5 días)
4. INC-010-004: Implementación Real Servicio (3 días)
5. INC-010-005: Vinculación Art. 49 (2 días)
6. INC-010-006: Configuración Thresholds (2 días)

### Fase 2: Altas - 12 días
7. ~~INC-010-007: Informes Automáticos (2 días)~~ ✅ **COMPLETADO** - 2025-01-21
8. INC-010-008: Dashboard Supervisión (3 días)
9. INC-010-009: API REST Histórico (2 días)
10. INC-010-010: Integración Feedback (3 días)
11. INC-010-011: Optimización Consultas (2 días)

### Fase 3: Medias - 6 días
12. INC-010-012: Análisis Sentimiento (1 día)
13. INC-010-013: Visualizaciones Avanzadas (2 días)
14. INC-010-014: Configuración Frecuencias (1 día)
15. INC-010-015: Integración Externa (2 días)

**Total Esfuerzo:** 37 días  
**Timeline Recomendado:** 6-8 semanas

---

## MÉTRICAS DE SEGUIMIENTO

**KPIs:**
- % de incidencias resueltas
- % de cumplimiento EU AI Act
- Tiempo promedio de resolución
- Número de incidentes graves detectados
- Número de reportes generados

**Revisión:** Semanal durante Fase 1, quincenal en Fases 2-3

---

**Fin del Documento de Incidencias y Recomendaciones**


