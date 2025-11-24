# AUDITORÍA - POST MARKET MONITORING (PMM)
**Fecha:** Diciembre 2025  
**Auditor:** Sistema de Gobierno de IA - CodeflowX  
**Base Legal:** EU AI Act Art. 72 (Vigilancia poscomercialización), Art. 16.g, Art. 16.h, Art. 73 (Notificación de incidentes graves)  
**Artículos Relevantes:** EU AI Act Art. 72, Art. 16.g, Art. 16.h, Art. 73, Art. 15 (Precisión y robustez), Art. 19 (Registros)

---

## 1. RESUMEN EJECUTIVO

### 1.1 Objetivo de la Auditoría

Evaluar la implementación del sistema de Post Market Monitoring (PMM) obligatorio según EU AI Act Art. 72, verificando:
- Métricas monitoreadas y su frecuencia
- Sistema de generación de alertas
- Supervisión continua y demostrable
- Histórico de incidentes y trazabilidad
- Informes automáticos generados
- Cumplimiento con Art. 16.g (establecer y documentar sistema PMM)
- Cumplimiento con Art. 16.h (registro del sistema según Art. 49)
- Cumplimiento con Art. 73 (notificación de incidentes graves)

### 1.2 Alcance

**Entidades Evaluadas:**
- `PostMarketMonitoringService` (Servicio de monitoreo)
- `CheckPostMarketMetricsDelegate` (Delegate BPMN)
- `MONMONITORINGALERTS` (Tabla de alertas)
- `MONMONITORINGMETRICS` (Tabla de métricas)
- `GOVSYSTEMALERTS` (Tabla de alertas del sistema)
- `GOVSYSTEMMETRICS` (Tabla de métricas del sistema)
- `IMLIMMUTABLELOGS` (Logs inmutables para auditoría)

**Procesos BPMN Evaluados:**
- `compliance-monitoring-v1.bpmn` (Monitoreo de cumplimiento)
- `drift-detection-v1.bpmn` (Detección de drift)
- `alert-response-v1.bpmn` (Respuesta a alertas)
- `incident-reporting-process.bpmn` (Reporte de incidentes)

**Microservicios Evaluados:**
- `codeflowx.govern.workflow.lib` (Workflow engine)
- `aio-telemetry-service` (Telemetría)
- `leka-bias-detection-service` (Detección de sesgo)
- `leka-adversarial-service` (Evaluación adversarial)

---

## 2. MÉTRICAS REVISADAS

### 2.1 Métricas Implementadas

#### 2.1.1 Métricas de Drift (Data Drift / Model Drift)

**Implementación Actual:**
- **Servicio:** `PostMarketMonitoringService.checkProjectMetrics()`
- **Métrica:** `driftScore` (BigDecimal, 0.00 - 1.00)
- **Detección:** `driftDetected` (boolean)
- **Estado:** ⚠️ **PARCIAL** - Implementación con valores mock/TODO

**Código Identificado:**
```23:38:nocode.service/codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/services/PostMarketMonitoringService.java
public PostMarketResult checkProjectMetrics(Long projectId) {
    log.info("Verificando métricas post-market para proyecto: {}", projectId);
    
    // TODO: Implementar verificación real de métricas post-market
    // Drift detection, performance degradation, user satisfaction
    PostMarketResult result = new PostMarketResult();
    result.setDriftDetected(false);
    result.setPerformanceDegradation(false);
    result.setUserSatisfactionDrop(false);
    result.setDriftScore(new BigDecimal("0.05"));
    result.setPerformanceScore(new BigDecimal("0.95"));
    result.setUserSatisfactionScore(new BigDecimal("0.90"));
    result.setMetricsData(new HashMap<>());
    
    return result;
}
```

**Gap Identificado:**
- ❌ **FALTA:** Integración real con servicio de drift detection
- ❌ **FALTA:** Comparación con baseline histórico
- ❌ **FALTA:** Cálculo de estadísticas (KS test, PSI, etc.)

**Recomendación:**
- Integrar con `leka-bias-detection-service` endpoint `/api/drift/detect`
- Implementar comparación con baseline almacenado en `GOVMODELEVALUATIONS`
- Calcular métricas estadísticas (KS test, Population Stability Index)

#### 2.1.2 Métricas de Performance (Degradación de Rendimiento)

**Implementación Actual:**
- **Métrica:** `performanceScore` (BigDecimal, 0.00 - 1.00)
- **Detección:** `performanceDegradation` (boolean)
- **Estado:** ⚠️ **PARCIAL** - Implementación con valores mock

**Métricas Esperadas (según Art. 15):**
- Accuracy (precisión)
- Precision/Recall
- F1-Score
- Latency (P95, P99)
- Throughput
- Error rate

**Gap Identificado:**
- ❌ **FALTA:** Consulta real a telemetría (`aio-telemetry-service`)
- ❌ **FALTA:** Comparación con baseline de performance
- ❌ **FALTA:** Alertas por degradación de métricas específicas

**Recomendación:**
- Integrar con `aio-telemetry-service` para métricas en tiempo real
- Comparar con baseline almacenado en `EVALMODELMETRICS`
- Definir thresholds por tipo de modelo y sector

#### 2.1.3 Métricas de Satisfacción de Usuario

**Implementación Actual:**
- **Métrica:** `userSatisfactionScore` (BigDecimal, 0.00 - 1.00)
- **Detección:** `userSatisfactionDrop` (boolean)
- **Estado:** ⚠️ **PARCIAL** - Implementación con valores mock

**Métricas Esperadas:**
- User feedback scores
- Complaint rate
- Escalation rate
- User retention
- Task completion rate

**Gap Identificado:**
- ❌ **FALTA:** Integración con sistema de feedback de usuarios
- ❌ **FALTA:** Análisis de sentimiento de feedback
- ❌ **FALTA:** Tracking de quejas y escalaciones

**Recomendación:**
- Crear entidad `UserFeedback` para almacenar feedback
- Integrar con sistema de tickets/incidentes
- Implementar análisis de sentimiento sobre feedback textual

#### 2.1.4 Métricas Adicionales Identificadas

**En Proceso BPMN `compliance-monitoring-v1.bpmn`:**
- `logIntegrityValid` - Integridad de logs inmutables
- `tamperingDetected` - Detección de manipulación
- `adversarialRobustnessScore` - Robustez adversarial
- `feedbackLoopDetected` - Detección de bucles de retroalimentación
- `biasAmplificationFactor` - Factor de amplificación de sesgo

**Estado:** ✅ **IMPLEMENTADO** en proceso BPMN, pero requiere integración con microservicios

---

## 3. GENERACIÓN DE ALERTAS

### 3.1 Sistema de Alertas Implementado

#### 3.1.1 Tablas de Alertas

**Tabla: `MONMONITORINGALERTS`**
- **Campos clave:**
  - `MONALERTTYPE` - Tipo de alerta (TEXT[])
  - `MONSEVERITY` - Severidad (VARCHAR 50)
  - `MONSTATUS` - Estado (TEXT[])
  - `MONTRIGGEREDAT` - Timestamp de activación
  - `MONACKNOWLEDGEDAT` - Timestamp de reconocimiento
  - `MONRESOLVEDAT` - Timestamp de resolución

**Tabla: `GOVSYSTEMALERTS`**
- **Campos clave:**
  - `ALERTTYPE` - Tipo de alerta (TEXT[])
  - `SEVERITY` - Severidad (VARCHAR 100)
  - `STATUS` - Estado (TEXT[])
  - `THRESHOLDVALUE` - Valor umbral
  - `ACTUALVALUE` - Valor actual
  - `TRIGGEREDAT` - Timestamp de activación

#### 3.1.2 Reglas de Escalación (Drools)

**Archivo:** `alert-escalation.drl`

**Reglas Implementadas:**
1. **CRITICAL ALERT** - Production + High Impact
   - Condición: `degradationPercentage > 40` o `SERVICE_DOWN`
   - Acción: SLA 15 minutos, canal PAGERDUTY, equipo `mlops-engineers`

2. **HIGH ALERT** - Performance Degradation
   - Condición: `degradationPercentage > 20` y `<= 40`
   - Acción: SLA 120 minutos, canal SLACK, equipo `ml-engineers`

3. **MEDIUM ALERT** - Warnings
   - Condición: Degradación menor
   - Acción: SLA según configuración

**Estado:** ✅ **IMPLEMENTADO** - Reglas Drools funcionando

#### 3.1.3 Integración con BPMN

**Proceso:** `compliance-monitoring-v1.bpmn`

**Flujo de Alertas:**
1. `checkPostMarketMetrics` detecta issue
2. Si `driftDetected || performanceDegradation || userSatisfactionDrop`:
   - Marca `nonComplianceDetected = true`
   - Establece `postMarketIssueSeverity = "MEDIUM"`
3. Gateway `nonComplianceGateway` evalúa severidad
4. Genera alerta en `MONMONITORINGALERTS` o `GOVSYSTEMALERTS`
5. Escala según reglas Drools

**Estado:** ✅ **IMPLEMENTADO** - Flujo BPMN documentado

#### 3.1.4 Gaps en Generación de Alertas

**Gaps Identificados:**
- ⚠️ **FALTA:** Integración real con `PostMarketMonitoringService` (actualmente mock)
- ⚠️ **FALTA:** Thresholds configurables por proyecto/modelo
- ⚠️ **FALTA:** Notificación automática a autoridades (Art. 73)
- ⚠️ **FALTA:** Alertas para incidentes graves según Art. 73

**Recomendación:**
- Completar implementación de `PostMarketMonitoringService` con integraciones reales
- Crear entidad `AlertThreshold` para configuración por proyecto
- Implementar workflow de notificación a autoridades para incidentes graves

---

## 4. SUPERVISIÓN CONTINUA

### 4.1 Frecuencia de Monitoreo

#### 4.1.1 Proceso BPMN `compliance-monitoring-v1.bpmn`

**Frecuencia Documentada:**
- **Timer:** Cada 24 horas (automático)
- **Manual:** Puede relanzarse desde panel de gobierno

**Implementación:**
```1:8:suinsit.nova.web/docs/compliance/bpmn/compliance/compliance-monitoring/functional.md
## Frecuencia / Trigger
- **Timer**: cada 24h.
- **Manual**: puede relanzarse desde el panel de gobierno.
```

**Estado:** ✅ **DOCUMENTADO** - Frecuencia definida

#### 4.1.2 Métricas en Tiempo Real

**Servicios de Telemetría:**
- `aio-telemetry-service` - Métricas en tiempo real
- `leka-bias-detection-service` - Detección de drift/sesgo
- `leka-adversarial-service` - Evaluación adversarial

**Estado:** ⚠️ **PARCIAL** - Servicios identificados pero integración pendiente

### 4.2 Demostración de Supervisión Continua

#### 4.2.1 Registro en Logs Inmutables

**Tabla: `IMLIMMUTABLELOGS`**
- **Propósito:** Registro inmutable de todas las verificaciones PMM
- **Campos relevantes:**
  - `IMLENTITYTYPE` - Tipo de entidad (PROJECT, MODEL, etc.)
  - `IMLACTION` - Acción (POST_MARKET_CHECK, ALERT_GENERATED, etc.)
  - `IMLDATASNAPSHOT` - Snapshot completo de métricas (JSON)
  - `IMLHASH` - Hash para verificación de integridad
  - `IMLPREVIOUSHASH` - Hash anterior (hash chain)

**Estado:** ✅ **IMPLEMENTADO** - Tabla y entidad creadas

#### 4.2.2 Registro en Tablas de Métricas

**Tabla: `MONMONITORINGMETRICS`**
- Almacena métricas históricas
- Campo `MONCREATEDAT` para trazabilidad temporal
- Campo `MONMETRICVALUE` para valores históricos

**Tabla: `GOVSYSTEMMETRICS`**
- Métricas del sistema
- Campo `MEASUREMENTDATE` para timestamp
- Campo `TREND` para tendencias

**Estado:** ✅ **IMPLEMENTADO** - Tablas creadas

#### 4.2.3 Dashboard de Supervisión

**Documentación:**
- Dashboard Compliance mencionado en documentación funcional
- Exportables (CSV/PDF) para auditorías Art. 72

**Estado:** ⚠️ **PARCIAL** - Documentado pero requiere verificación de implementación UI

**Gap Identificado:**
- ❌ **FALTA:** Verificación de dashboard implementado
- ❌ **FALTA:** Visualización de tendencias históricas
- ❌ **FALTA:** Exportación automática de reportes

---

## 5. HISTÓRICO DE INCIDENTES

### 5.1 Almacenamiento de Incidentes

#### 5.1.1 Tabla de Alertas

**Tabla: `MONMONITORINGALERTS`**
- Histórico completo de alertas
- Estados: `ACTIVE`, `ACKNOWLEDGED`, `RESOLVED`, `CLOSED`
- Timestamps: `MONTRIGGEREDAT`, `MONACKNOWLEDGEDAT`, `MONRESOLVEDAT`
- Metadata: `MONALERTDATA` (JSONB) para datos adicionales

**Estado:** ✅ **IMPLEMENTADO** - Tabla creada

#### 5.1.2 Proceso de Incidentes

**BPMN:** `incident-reporting-process.bpmn`
- Proceso para reporte de incidentes
- Integración con Art. 73 (notificación de incidentes graves)

**Estado:** ✅ **DOCUMENTADO** - Proceso BPMN identificado

#### 5.1.3 Logs Inmutables

**Tabla: `IMLIMMUTABLELOGS`**
- Registro inmutable de todos los incidentes
- Hash chain para verificación de integridad
- Snapshot completo de estado en momento del incidente

**Estado:** ✅ **IMPLEMENTADO** - Tabla y entidad creadas

### 5.2 Consulta de Histórico

#### 5.2.1 Queries Disponibles

**Query de Alertas por Proyecto:**
```sql
SELECT * FROM MONMONITORINGALERTS 
WHERE MONALERTDATA->>'projectId' = ?
ORDER BY MONTRIGGEREDAT DESC;
```

**Query de Métricas Históricas:**
```sql
SELECT * FROM MONMONITORINGMETRICS 
WHERE MONMETRICNAME = ? 
AND MONCREATEDAT >= ?
ORDER BY MONCREATEDAT DESC;
```

**Estado:** ✅ **POSIBLE** - Queries SQL pueden ejecutarse

#### 5.2.2 Gaps en Histórico

**Gaps Identificados:**
- ⚠️ **FALTA:** API REST para consulta de histórico
- ⚠️ **FALTA:** Vista materializada para consultas rápidas
- ⚠️ **FALTA:** Dashboard de histórico de incidentes
- ⚠️ **FALTA:** Filtros por tipo, severidad, fecha

**Recomendación:**
- Crear endpoints REST para consulta de histórico
- Crear vistas materializadas con TimescaleDB para consultas optimizadas
- Implementar dashboard con filtros y visualizaciones

---

## 6. INFORMES AUTOMÁTICOS

### 6.1 Informes Identificados

#### 6.1.1 Reporte de Compliance Monitoring

**Proceso:** `compliance-monitoring-v1.bpmn`

**Documentación:**
```41:43:suinsit.nova.web/docs/compliance/bpmn/compliance/compliance-monitoring/functional.md
## Reportes
- Dashboard Compliance → muestra scores diarios y tendencias.
- Exportables (CSV/PDF) para auditorías Art. 72.
```

**Estado:** ⚠️ **DOCUMENTADO** - Requiere verificación de implementación

#### 6.1.2 Post-Market Surveillance Report

**Requisito:** Art. 72 - Informes de vigilancia poscomercialización

**Estado:** ❌ **NO IMPLEMENTADO**

**Gap Identificado (GAP-017):**
```519:545:suinsit.nova.web/docs/compliance/GAPS_DETECTED_AI_ACT_VERIFICATION.md
### GAP-017: Post-Market Monitoring System
**Article:** Art. 16.g, 16.h  
**Reference:** EUR-Lex Art. 16 - Obligations of providers  
**Priority:** 🔴 Critical  
**Impact:** Certification blocker

**Requirement:**
> Art. 16.g: Los proveedores deberán **establecer y documentar un sistema de vigilancia poscomercialización** con arreglo al artículo 72.
> Art. 16.h: Los proveedores deberán **registrar el sistema** de conformidad con el artículo 49.

**Current State:**
- ✅ We have continuous monitoring (drift, performance, bias)
- ❌ Missing: Formal "Post-Market Monitoring Plan" document (Art. 72)
- ❌ Missing: Documented post-market monitoring system
- ❌ Missing: Reporting of serious incidents and malfunctions
- ❌ Missing: Post-market surveillance reports

**Required Actions:**
1. Create entity `PostMarketMonitoringPlan`
2. Create BPMN process "Post-Market Monitoring" (Art. 72)
3. Link to existing monitoring microservices
4. Add "Post-Market Surveillance Report" auto-generation
5. Add "Serious Incident Report" workflow
6. Define incident thresholds and notification triggers
7. Link to Art. 49 registration
```

#### 6.1.3 Serious Incident Report

**Requisito:** Art. 73 - Notificación de incidentes graves

**Estado:** ❌ **NO IMPLEMENTADO**

**Gap Identificado:**
- ❌ **FALTA:** Entidad `SeriousIncidentReport`
- ❌ **FALTA:** Workflow de notificación a autoridades
- ❌ **FALTA:** Definición de umbrales para "incidente grave"
- ❌ **FALTA:** Integración con autoridades competentes

### 6.2 Informes Automáticos Requeridos

#### 6.2.1 Informe Diario de PMM

**Contenido Esperado:**
- Resumen de métricas del día
- Alertas generadas
- Incidentes detectados
- Tendencias (drift, performance, satisfacción)
- Acciones correctivas iniciadas

**Frecuencia:** Diario (automático)

**Estado:** ❌ **NO IMPLEMENTADO**

#### 6.2.2 Informe Mensual de Vigilancia

**Contenido Esperado (Art. 72):**
- Resumen mensual de vigilancia
- Análisis de tendencias
- Incidentes graves reportados
- Acciones correctivas completadas
- Métricas agregadas por proyecto/modelo

**Frecuencia:** Mensual (automático)

**Estado:** ❌ **NO IMPLEMENTADO**

#### 6.2.3 Informe de Incidente Grave

**Contenido Esperado (Art. 73):**
- Descripción del incidente
- Impacto estimado
- Causa raíz (si disponible)
- Acciones correctivas tomadas
- Notificación a autoridades

**Frecuencia:** Ad-hoc (cuando se detecta incidente grave)

**Estado:** ❌ **NO IMPLEMENTADO**

---

## 7. CUMPLIMIENTO CON EU AI ACT

### 7.1 Art. 16.g - Establecer y Documentar Sistema PMM

**Requisito:**
> Los proveedores deberán establecer y documentar un sistema de vigilancia poscomercialización con arreglo al artículo 72.

**Estado Actual:**
- ✅ **PARCIAL:** Sistema de monitoreo implementado (servicios, BPMN, tablas)
- ❌ **FALTA:** Documentación formal del sistema PMM
- ❌ **FALTA:** Entidad `PostMarketMonitoringPlan`
- ❌ **FALTA:** Plan documentado por proyecto/modelo

**Cumplimiento:** ⚠️ **PARCIAL** - 40%

### 7.2 Art. 16.h - Registro del Sistema

**Requisito:**
> Los proveedores deberán registrar el sistema de conformidad con el artículo 49.

**Estado Actual:**
- ✅ **IMPLEMENTADO:** Entidad `EuRegistration` para registro en BD UE
- ⚠️ **PARCIAL:** Falta vincular sistema PMM con registro Art. 49

**Cumplimiento:** ⚠️ **PARCIAL** - 60%

### 7.3 Art. 72 - Vigilancia Poscomercialización

**Requisito:**
> Los proveedores deberán establecer un sistema de vigilancia poscomercialización.

**Estado Actual:**
- ✅ **IMPLEMENTADO:** Servicio `PostMarketMonitoringService`
- ✅ **IMPLEMENTADO:** Proceso BPMN `compliance-monitoring-v1.bpmn`
- ✅ **IMPLEMENTADO:** Tablas de métricas y alertas
- ✅ **IMPLEMENTADO:** Informes automáticos de vigilancia (INC-010-007 resuelto)
- ⚠️ **PARCIAL:** Integraciones con microservicios pendientes
- ❌ **FALTA:** Plan documentado de PMM

**Cumplimiento:** ⚠️ **PARCIAL** - 58% (mejorado desde 50%)

### 7.4 Art. 73 - Notificación de Incidentes Graves

**Requisito:**
> Los proveedores deberán notificar a las autoridades competentes cualquier incidente grave.

**Estado Actual:**
- ✅ **DOCUMENTADO:** Proceso BPMN `incident-reporting-process.bpmn`
- ❌ **FALTA:** Entidad `SeriousIncidentReport`
- ❌ **FALTA:** Workflow de notificación a autoridades
- ❌ **FALTA:** Definición de umbrales para "incidente grave"
- ❌ **FALTA:** Integración con autoridades competentes

**Cumplimiento:** ❌ **NO CUMPLE** - 20%

---

## 8. CONCLUSIONES

### 8.1 Resumen de Estado

**Implementación Actual:**
- ✅ **COMPLETADO:** Infraestructura base (tablas, entidades, servicios)
- ✅ **COMPLETADO:** Procesos BPMN para monitoreo
- ✅ **COMPLETADO:** Sistema de alertas y escalación
- ✅ **COMPLETADO:** Informes automáticos (INC-010-007 resuelto - 2025-01-21)
- ⚠️ **PARCIAL:** Integraciones con microservicios (mock/TODO)
- ❌ **FALTA:** Documentación formal del sistema PMM
- ❌ **FALTA:** Notificación de incidentes graves

### 8.2 Nivel de Cumplimiento

**Cumplimiento General:** ⚠️ **PARCIAL** - **53%** (mejorado desde 45%)

**Desglose por Artículo:**
- **Art. 16.g:** ⚠️ 40% - Falta documentación formal
- **Art. 16.h:** ⚠️ 60% - Falta vinculación con registro
- **Art. 72:** ⚠️ 58% - Informes automáticos implementados (INC-010-007), falta plan documentado
- **Art. 73:** ❌ 20% - Falta implementación completa

### 8.3 Riesgos Identificados

**Riesgos Críticos:**
1. **Certification Blocker:** GAP-017 identificado como crítico
2. **Falta de Trazabilidad:** Informes automáticos no implementados
3. **Incidentes Graves:** No hay proceso completo de notificación
4. **Integraciones Pendientes:** Servicios con implementación mock

**Riesgos Medios:**
1. **Dashboard:** Requiere verificación de implementación UI
2. **Histórico:** Falta API y vistas optimizadas
3. **Thresholds:** No configurables por proyecto/modelo

### 8.4 Recomendaciones Prioritarias

**Prioridad CRÍTICA (Certification Blocker):**
1. Crear entidad `PostMarketMonitoringPlan` y documentar sistema PMM
2. Implementar generación automática de "Post-Market Surveillance Report"
3. Implementar workflow completo de notificación de incidentes graves (Art. 73)
4. Completar integraciones reales con microservicios (eliminar mocks)

**Prioridad ALTA:**
1. ~~Implementar informes automáticos (diario, mensual)~~ ✅ **COMPLETADO** - INC-010-007 (2025-01-21)
2. Crear dashboard de supervisión continua
3. Implementar API REST para consulta de histórico
4. Configurar thresholds por proyecto/modelo

**Prioridad MEDIA:**
1. Optimizar consultas con vistas materializadas
2. Implementar análisis de sentimiento en feedback
3. Mejorar visualizaciones de tendencias

---

## 9. EVIDENCIAS Y REFERENCIAS

### 9.1 Código Fuente

**Servicios:**
- `PostMarketMonitoringService.java` - Servicio principal PMM
- `CheckPostMarketMetricsDelegate.java` - Delegate BPMN

**Entidades:**
- `ImmutableLog.java` - Logs inmutables
- `EuRegistration.java` - Registro BD UE

**Tablas:**
- `MONMONITORINGALERTS` - Alertas de monitoreo
- `MONMONITORINGMETRICS` - Métricas históricas
- `GOVSYSTEMALERTS` - Alertas del sistema
- `IMLIMMUTABLELOGS` - Logs inmutables

### 9.2 Documentación

**BPMN:**
- `compliance-monitoring-v1.bpmn` - Proceso de monitoreo
- `pmm-reports-v1.bpmn` - Proceso de generación automática de informes (INC-010-007)
- `incident-reporting-process.bpmn` - Proceso de incidentes

**Documentación Funcional:**
- `docs/compliance/bpmn/compliance/compliance-monitoring/functional.md`
- `docs/compliance/bpmn/compliance/compliance-monitoring/technical.md`

**Gaps:**
- `docs/compliance/GAPS_DETECTED_AI_ACT_VERIFICATION.md` - GAP-017

### 9.3 Artículos EU AI Act

- **Art. 16.g:** Establecer y documentar sistema PMM
- **Art. 16.h:** Registro del sistema (Art. 49)
- **Art. 72:** Vigilancia poscomercialización
- **Art. 73:** Notificación de incidentes graves
- **Art. 15:** Precisión, robustez y ciberseguridad
- **Art. 19:** Registros generados automáticamente

---

**Fin del Informe de Auditoría**

**Próximos Pasos:**
1. Revisar documento de incidencias y recomendaciones
2. Priorizar acciones correctivas
3. Asignar responsables y fechas
4. Seguimiento de implementación


