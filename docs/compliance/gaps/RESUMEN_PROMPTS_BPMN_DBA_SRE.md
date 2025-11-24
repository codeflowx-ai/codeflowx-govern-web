# RESUMEN PROMPTS BPMN, DBA Y SRE

**Fecha:** Diciembre 2025  
**Total Prompts:** 5 (2 BPMN + 2 DBA + 1 SRE)

---

## GLOSARIO

### BPMN (Business Process Model and Notation)
**Qué es:** Lenguaje estándar para modelar procesos de negocio. En este proyecto se usa **Flowable** como motor BPMN para orquestar workflows de compliance.

**Ejemplos de uso:**
- Workflow de clasificación de alto riesgo
- Proceso de generación de FRIA
- Workflow de aprobación de modelos
- Proceso de Post Market Monitoring

**Archivos:** Procesos definidos en archivos `.bpmn20.xml` que se ejecutan en el motor Flowable.

### DBA (Database Administrator)
**Qué es:** Administración y optimización de bases de datos. Incluye:
- Creación de índices para mejorar performance
- Optimización de queries SQL
- Configuración de vistas materializadas
- Triggers y constraints de base de datos

**Ejemplos de uso:**
- Índices GIN para campos JSONB
- Vistas materializadas para agregaciones
- Constraints para validación de datos
- Triggers para auditoría

**Archivos:** Scripts SQL en `sql-scripts/` o `src/main/resources/sql/`

### SRE (Site Reliability Engineering)
**Qué es:** Ingeniería de confiabilidad del sitio. Se enfoca en:
- Monitoreo de capacidad y rendimiento
- Tests de carga y stress testing
- Documentación de límites del sistema
- Métricas de disponibilidad y latencia

**Ejemplos de uso:**
- Tests de carga con JMeter/Gatling
- Documentación de throughput máximo
- Métricas de latencia bajo carga
- Health checks y alertas

**Archivos:** Documentación técnica, scripts de testing, configuraciones de monitoreo

---

## PROMPTS BPMN (2)

### 🔴 `compliance-monitoring-v1.bpmn`
**Prompts:**
1. ✅ **INC-010-007:** Implementación Informes Automáticos
   - **Descripción:** Implementar timers BPMN para generación automática de informes diarios/mensuales de Post Market Monitoring
   - **Prioridad:** 🟡 ALTA
   - **Esfuerzo:** 2 días
   - **Componente:** Proceso BPMN `compliance-monitoring-v1.bpmn`
   - **📄 Documento Auditoría:** `AUDITORIA_010_POST_MARKET_MONITORING.md`
   - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md` (INC-010-007)
   - **Detalles:**
     - Timer diario a las 01:00 para informe diario
     - Timer mensual el día 1 a las 02:00 para informe mensual
     - Integración con `PostMarketSurveillanceReportService`
     - Notificación automática a stakeholders

### 🔴 `fria-process.bpmn` o `mejora-continua.bpmn`
**Prompts:**
2. ✅ **INC-005-009:** Mejora Continua Workflow
   - **Descripción:** Implementar workflow BPMN para proceso de mejora continua (A/B testing, feedback loops)
   - **Prioridad:** 🟢 MEDIA
   - **Esfuerzo:** 3 días
   - **Componente:** Nuevo proceso BPMN o modificación de proceso existente
   - **📄 Documento Auditoría:** `AUDITORIA_005_EVALUACION_RAG.md`
   - **📋 Documento Incidencias:** `INCIDENCIAS_005_EVALUACION_RAG.md` (INC-005-009)
   - **Detalles:**
     - Workflow para A/B testing de modelos
     - Proceso de recolección de feedback
     - Decisión automática de actualización de modelos
     - Integración con `leka-agent-monitoring`

---

## PROMPTS DBA (2)

### 🔴 Optimización de Queries de Telemetría
**Prompts:**
1. ✅ **INC-009-005:** Optimización Queries Telemetría
   - **Descripción:** Crear índices GIN adicionales para campos JSONB frecuentes y continuous aggregates por hora
   - **Prioridad:** 🟢 BAJA
   - **Esfuerzo:** 1 día
   - **Componente:** Base de datos PostgreSQL
   - **Tabla:** `AIOTELEMETRY`
   - **📄 Documento Auditoría:** `AUDITORIA_009_MONITORIZACION_TIEMPO_REAL.md`
   - **📋 Documento Incidencias:** `INCIDENCIAS_009_MONITORIZACION_TIEMPO_REAL.md` (INC-009-005)
   - **Detalles:**
     - Índices GIN para `TELPAYLOAD->>'severity'`
     - Índices GIN para `TELPAYLOAD->>'event_type'`
     - Índices GIN para `TELPAYLOAD->>'interaction_id'`
     - Continuous aggregates por hora usando TimescaleDB
     - Índices compuestos para queries frecuentes

### 🔴 Optimización de Consultas Post Market Monitoring
**Prompts:**
2. ✅ **INC-010-011:** Optimización Consultas PMM
   - **Descripción:** Crear vistas materializadas y índices para optimizar consultas de Post Market Monitoring
   - **Prioridad:** 🟡 ALTA
   - **Esfuerzo:** 2 días
   - **Componente:** Base de datos PostgreSQL
   - **Tablas:** `MONMONITORINGMETRICS`, `MONMONITORINGALERTS`, `PMMPOSTMARKETMONITORINGPLANS`
   - **📄 Documento Auditoría:** `AUDITORIA_010_POST_MARKET_MONITORING.md`
   - **📋 Documento Incidencias:** `INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md` (INC-010-011)
   - **Detalles:**
     - Vistas materializadas para agregaciones por proyecto/modelo
     - Índices en campos de fecha para consultas temporales
     - Índices en campos de estado para filtros frecuentes
     - Optimización de queries de dashboard

---

## PROMPTS SRE (1)

### 🔴 Documentación de Capacidad del Sistema
**Prompts:**
1. ✅ **INC-009-001:** Documentación Formal Capacidad
   - **Descripción:** Realizar tests de carga formales y documentar límites reales medidos del sistema de telemetría
   - **Prioridad:** 🟡 MEDIA
   - **Esfuerzo:** 3-4 días
   - **Componente:** Documentación técnica + Tests de carga
   - **📄 Documento Auditoría:** `AUDITORIA_009_MONITORIZACION_TIEMPO_REAL.md`
   - **📋 Documento Incidencias:** `INCIDENCIAS_009_MONITORIZACION_TIEMPO_REAL.md` (INC-009-001)
   - **Detalles:**
     - Tests de carga con JMeter/Gatling/k6
     - Documentar throughput máximo sostenido
     - Documentar latencia bajo carga (p95, p99)
     - Documentar punto de degradación
     - Documentar recovery time después de picos
     - Métricas de error rate bajo diferentes cargas
     - Configuración de Prometheus y Grafana para monitoreo

---

## RESUMEN POR PRIORIDAD

### 🟡 ALTAS (2)
- INC-010-007: Informes automáticos (BPMN)
- INC-010-011: Optimización consultas PMM (DBA)

### 🟡 MEDIAS (2)
- INC-005-009: Mejora continua workflow (BPMN)
- INC-009-001: Documentación capacidad (SRE)

### 🟢 BAJAS (1)
- INC-009-005: Optimización queries telemetría (DBA)

---

## DEPENDENCIAS ENTRE COMPONENTES

```
BPMN Workflows
  ├── INC-010-007 → Java Service (PostMarketSurveillanceReportService)
  └── INC-005-009 → Python Microservice (leka-agent-monitoring)

DBA Optimizaciones
  ├── INC-009-005 → Mejora queries de telemetría
  └── INC-010-011 → Mejora queries de PMM

SRE Tests
  └── INC-009-001 → Documenta límites para todos los componentes
```

---

## ORDEN DE IMPLEMENTACIÓN RECOMENDADO

### Fase 1: Altas (Semana 1)
1. **INC-010-011** (DBA) - Optimización consultas PMM - Bloquea performance
2. **INC-010-007** (BPMN) - Informes automáticos - Requerido para compliance

### Fase 2: Medias (Semana 2)
3. **INC-009-001** (SRE) - Documentación capacidad - Requerido para auditoría
4. **INC-005-009** (BPMN) - Mejora continua - Mejora operativa

### Fase 3: Bajas (Semana 3)
5. **INC-009-005** (DBA) - Optimización telemetría - Mejora performance

---

## REFERENCIAS A DOCUMENTOS DE AUDITORÍA

### Documentos de Auditoría Principales:
- **`AUDITORIA_009_MONITORIZACION_TIEMPO_REAL.md`** - Monitorización tiempo real (INC-009-001, INC-009-005)
- **`AUDITORIA_010_POST_MARKET_MONITORING.md`** - Post Market Monitoring (INC-010-007, INC-010-011)
- **`AUDITORIA_005_EVALUACION_RAG.md`** - Evaluación RAG (INC-005-009)

### Documentos de Incidencias y Recomendaciones:
- **`INCIDENCIAS_009_MONITORIZACION_TIEMPO_REAL.md`** - Incidencias monitorización (INC-009-001, INC-009-005)
- **`INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md`** - Incidencias PMM (INC-010-007, INC-010-011)
- **`INCIDENCIAS_005_EVALUACION_RAG.md`** - Incidencias RAG (INC-005-009)

### Ubicación de Documentos:
Todos los documentos están en: `/docs/compliance/auditoria/`

---

## NOTAS TÉCNICAS

### BPMN (Flowable)
- Los procesos se definen en archivos `.bpmn20.xml`
- Se despliegan automáticamente al iniciar la aplicación
- Los timers usan expresiones cron: `0 0 1 * * ?` (diario a las 01:00)
- Los servicios Java se invocan mediante `serviceTask` con `delegateExpression`

### DBA (PostgreSQL)
- Los índices GIN son eficientes para búsquedas en JSONB
- Las vistas materializadas se refrescan periódicamente
- Los continuous aggregates requieren extensión TimescaleDB
- Los scripts SQL se ejecutan como migraciones o patches

### SRE (Testing y Monitoreo)
- Los tests de carga se ejecutan en entornos de staging
- Las métricas se recopilan con Prometheus
- Los dashboards se crean en Grafana
- La documentación se mantiene en formato Markdown

---

**Última actualización:** Diciembre 2025

