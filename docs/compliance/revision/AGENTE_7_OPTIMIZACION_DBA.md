# GUÍA AGENTE 7 - OPTIMIZACIÓN Y DBA

**Agente:** Backend + DBA
**Equipo:** Equipo 5 - Optimización y Performance
**Duración:** 8-10 horas
**Objetivo:** Optimizar consultas, crear benchmarks y configuraciones de métricas

---

## 📋 INCIDENCIAS ASIGNADAS

| ID | Descripción | Esfuerzo | Prioridad | Estado Inicial |
|----|-------------|----------|-----------|----------------|
| **INC-010-011** | Optimización de Consultas | 2h | 🟡 ALTA | 🔴 PENDIENTE |
| **INC-007-DS** | Benchmarks industria | 2h | 🟡 MEDIA | 🔴 PENDIENTE |
| **INC-010** | Umbrales configurables métricas | 1h | 🟡 MEDIA | 🔴 PENDIENTE |
| **INC-014** | Retención histórica evaluaciones | 1h | 🟡 MEDIA | 🔴 PENDIENTE |
| **INC-009-005** | Optimización queries | 2h | 🟢 BAJA | 🔴 PENDIENTE |

**Total:** 5 incidencias, ~8 horas

---

## 📚 DOCUMENTOS DE REFERENCIA

### Prompts Específicos:
1. **INC-010-011:**
   - `/docs/compliance/gaps/prompts/dba/INC-010-011_optimizacion_consultas.md`
   - **Tipo:** DBA - Vistas materializadas TimescaleDB

### Documentación General:
- **Arquitectura EnArt:** `/docs/compliance/PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md`
- **Plan Nocturno:** `/docs/compliance/revision/PLAN_TRABAJO_NOCTURNO.md`
- **Seguimiento:** `/docs/compliance/gaps/SEGUIMIENTO_INCIDENCIAS.md`
- **Distribución:** `/docs/compliance/revision/DISTRIBUCION_INCIDENCIAS_AGENTES.md`

### Referencias Técnicas:
- **Scripts SQL:** `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/`
- **Tablas Catalog:** `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/tablas.md`
- **BusinessServices:** `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/`

---

## 🏗️ ARQUITECTURA ENART - CONVENCIONES

**Ver documento AGENTE_1_VALIDACIONES_CRITICAS.md sección "ARQUITECTURA ENART" para convenciones completas.**

**Resumen rápido:**
- Prefijos de 3 caracteres para tablas
- PK autonumérica siempre
- Tercera forma normal
- KISS y SOLID

---

## 📁 ESTRUCTURA DE DIRECTORIOS

### **Scripts SQL:**
```
/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/
├── optimization/
│   ├── materialized_views_pmm.sql      ← CREAR (INC-010-011)
│   ├── indexes_gin_aggregates.sql      ← CREAR (INC-009-005)
│   └── benchmarks_industry.sql         ← CREAR (INC-007-DS)
├── configuration/
│   ├── metric_thresholds.sql           ← CREAR (INC-010)
│   └── evaluation_history.sql          ← CREAR (INC-014)
```

### **BusinessServices:**
```
/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/
├── governance/
│   ├── BenchmarkIndustryService.java   ← CREAR (INC-007-DS)
│   ├── MetricThresholdService.java     ← CREAR (INC-010)
│   └── EvaluationHistoryService.java   ← CREAR (INC-014)
```

---

## 🔧 IMPLEMENTACIÓN POR INCIDENCIA

### **INC-010-011: Optimización de Consultas**

#### **Archivos a Crear:**

1. **Script SQL:**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/optimization/materialized_views_pmm.sql`

#### **Funcionalidades:**
- Vistas materializadas para consultas PMM frecuentes
- Optimización con TimescaleDB
- Índices adicionales si es necesario

#### **Checklist:**
- [ ] Leer prompt completo: `INC-010-011_optimizacion_consultas.md`
- [ ] Crear vistas materializadas según prompt
- [ ] Crear índices necesarios
- [ ] Probar rendimiento de consultas
- [ ] Documentar vistas creadas

---

### **INC-007-DS: Benchmarks Industria**

#### **Archivos a Crear:**

1. **Entidad:**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/catalogs/Benchmark.java`
   - Tabla: `CORBENCHMARKS` (prefijo `COR`, según prompt específico)

2. **BusinessService:**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/governance/BenchmarkIndustryService.java`

3. **Script SQL:**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/optimization/benchmarks_industry.sql`

#### **Funcionalidades:**
- Tabla de benchmarks por sector industrial
- Comparación con métricas del proyecto
- Alertas si métricas están por debajo del benchmark

#### **Checklist:**
- [ ] Crear entidad según prompt
- [ ] Crear BusinessService
- [ ] Crear script SQL
- [ ] Poblar tabla con benchmarks iniciales
- [ ] Integrar con servicios de métricas
- [ ] Documentar BusinessService

---

### **INC-010: Umbrales Configurables Métricas**

#### **Archivos Existentes:**

1. **Entidad:**
   - ✅ `AlertThreshold.java` - Tabla `ALRALERTTHRESHOLDS` (prefijo `ALR`)
   - Ubicación: `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/AlertThreshold.java`

2. **BusinessService:**
   - ✅ `AlertThresholdService.java` - Ya existe
   - Ubicación: `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/AlertThresholdService.java`

3. **Script SQL:**
   - ✅ `alert_thresholds.sql` - Ya existe
   - Ubicación: `nocode.service.entitys/src/main/resources/sql/alert_thresholds.sql`

#### **Funcionalidades:**
- ✅ Configuración de umbrales por tipo de métrica (DRIFT, PERFORMANCE, USER_SATISFACTION, BIAS, ACCURACY, LATENCY)
- ✅ Configuración por proyecto/modelo
- ✅ Thresholds configurables (warning, critical)
- ✅ Severidad y estado (ACTIVE/INACTIVE)

#### **Estado:**
✅ **YA IMPLEMENTADO** - La entidad `AlertThreshold` cumple con los requisitos de INC-010.

#### **Acción Requerida:**
- [x] Verificar que entidad cumple requisitos (✅ CUMPLE)
- [ ] Verificar integración con servicios de métricas PMM
- [ ] Documentar uso en servicios de monitoreo
- [ ] Actualizar documentación si es necesario

---

### **INC-014: Retención Histórica Evaluaciones**

#### **Archivos a Crear:**

1. **JSON EnArt:**
   - `sources/json/tables/EVHEVALUATIONHISTORIES.json`

2. **Entidad (Generada):**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/evaluation/EvaluationHistory.java`
   - Tabla: `EVHEVALUATIONHISTORIES` (prefijo `EVH`)

3. **BusinessService:**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/evaluation/EvaluationHistoryBusinessService.java`
   - **Nota:** Ya existe `EvaluationHistoryService` que consulta tablas existentes, este es para historial consolidado

4. **Script SQL:**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/configuration/evaluation_history.sql`

#### **Funcionalidades:**
- Historial completo de evaluaciones (MODEL, DATASET, RAG, COMPLIANCE, FRIA, QMS)
- Retención configurable (STANDARD, EXTENDED, PERMANENT)
- Archivado automático de evaluaciones antiguas
- Consulta de histórico optimizada

#### **Checklist:**
- [ ] Crear JSON EnArt según prompt expandido
- [ ] Generar Entity.java con generador Python
- [ ] Crear BusinessService completo
- [ ] Crear script SQL con índices apropiados
- [ ] Implementar lógica de retención y archivado
- [ ] Integrar con servicios de evaluación existentes
- [ ] Documentar BusinessService

---

### **INC-009-005: Optimización Queries**

#### **Archivos a Crear:**

1. **Script SQL:**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/optimization/indexes_gin_aggregates.sql`

#### **Funcionalidades:**
- Índices GIN para búsquedas JSONB
- Agregados materializados
- Optimización de queries frecuentes

#### **Checklist:**
- [ ] Analizar queries lentas
- [ ] Crear índices GIN necesarios
- [ ] Crear agregados materializados
- [ ] Probar rendimiento
- [ ] Documentar optimizaciones

---

## ✅ CHECKLIST FINAL

### **Antes de Empezar:**
- [ ] Leer prompts completos
- [ ] Verificar entidades existentes
- [ ] Analizar queries actuales para optimización

### **Durante Implementación:**
- [ ] Seguir convenciones EnArt
- [ ] Probar rendimiento de optimizaciones
- [ ] Verificar que índices mejoran queries

### **Después de Implementación:**
- [ ] Compilar sin errores
- [ ] Ejecutar scripts SQL en entorno de prueba
- [ ] Verificar rendimiento mejorado
- [ ] Actualizar `tablas.md`
- [ ] Actualizar `SEGUIMIENTO_INCIDENCIAS.md`
- [ ] Documentar BusinessServices en `/docs/developers/`

---

## 📝 REGISTRO DE ARCHIVOS CREADOS

### **INC-010-011: Optimización de Consultas**

**Estado:** ✅ **COMPLETADO**

**Archivos Creados:**
- [x] Script SQL: `nocode.service.entitys/src/main/resources/sql/optimization/materialized_views_pmm.sql`

**Nota Importante:**
- Script ajustado para usar campos JSONB (`MONCONTEXT`, `MONMETADATA`) para extraer `IDXPROJECT` e `IDXMODEL`
- Las tablas `MONMONITORINGMETRICS` y `MONMONITORINGALERTS` no tienen estos campos directamente
- Índices GIN creados para optimizar búsquedas en JSONB
- Continuous aggregates para métricas diarias, semanales y mensuales
- Vistas materializadas para dashboard PMM

---

### **INC-007-DS: Benchmarks Industria**

**Estado:** ✅ **COMPLETADO**

**Archivos Creados:**
- [x] JSON EnArt: `suinsit.nova.web/sources/json/tables/CORBENCHMARKS.json`
- [x] Entidad: `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/catalogs/Benchmark.java` (tabla `CORBENCHMARKS`, prefijo `COR`)
- [x] BusinessService: `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/governance/BenchmarkComparisonService.java`
- [x] Script SQL: `nocode.service.entitys/src/main/resources/sql/optimization/benchmarks_industry.sql`

**Nota:** Usa nomenclatura del prompt específico (`CORBENCHMARKS` según prompt INC-007_benchmarks_industria.md)

---

### **INC-010: Umbrales Configurables Métricas**

**Estado:** ✅ **YA IMPLEMENTADO**

**Archivos Existentes:**
- [x] Entidad: `AlertThreshold.java` - Tabla `ALRALERTTHRESHOLDS`
- [x] BusinessService: `AlertThresholdService.java`
- [x] Script SQL: `alert_thresholds.sql`

**Acción Requerida:**
- [ ] Verificar integración con servicios de métricas PMM
- [ ] Documentar uso si es necesario

---

### **INC-014: Retención Histórica Evaluaciones**

**Estado:** ✅ **COMPLETADO**

**Archivos Creados:**
- [x] JSON EnArt: `suinsit.nova.web/sources/json/tables/EVHEVALUATIONHISTORIES.json`
- [x] BusinessService: `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/evaluation/EvaluationHistoryBusinessService.java`
- [x] Script SQL: `nocode.service.entitys/src/main/resources/sql/configuration/evaluation_history.sql`

**Nota:**
- Prompt expandido con arquitectura EnArt completa. Ver prompt específico actualizado.
- La entidad `EvaluationHistory.java` se generará automáticamente desde el JSON EnArt usando el generador Python
- BusinessService implementado con métodos para almacenar, consultar y archivar evaluaciones

---

### **INC-009-005: Optimización Queries**

**Estado:** ✅ **COMPLETADO**

**Archivos Creados:**
- [x] Script SQL: `nocode.service.entitys/src/main/resources/sql/optimization/indexes_gin_aggregates.sql`

**Contenido:**
- Índices GIN para campos JSONB frecuentes en `AIOTELEMETRY`
- Continuous aggregate por hora para estadísticas de telemetría
- Refresh policies automáticas

---

## 🔗 INTEGRACIONES REQUERIDAS

### **Servicios Existentes:**
- `PostMarketMonitoringService` - Para optimización PMM
- `ModelEvaluationService` - Para histórico de evaluaciones
- `GovernanceMetricService` - Para umbrales de métricas

---

---

## ✅ ESTADO FINAL - IMPLEMENTACIÓN COMPLETA

**Fecha de Finalización:** 25 de noviembre de 2025

### **Resumen de Archivos Creados:**

#### INC-010-011: Optimización de Consultas PMM
- ✅ Script SQL: `materialized_views_pmm.sql` (con continuous aggregates, vistas materializadas, índices GIN)

#### INC-007-DS: Benchmarks de Industria
- ✅ JSON EnArt: `CORBENCHMARKS.json`
- ✅ Entidad: `Benchmark.java`
- ✅ BusinessService: `BenchmarkComparisonService.java`
- ✅ Script SQL: `benchmarks_industry.sql`

#### INC-009-005: Optimización de Queries de Telemetría
- ✅ Script SQL: `indexes_gin_aggregates.sql` (índices GIN + continuous aggregates)

#### INC-014: Retención Histórica Evaluaciones
- ✅ JSON EnArt: `EVHEVALUATIONHISTORIES.json`
- ✅ BusinessService: `EvaluationHistoryBusinessService.java` (refactorizado para usar EvaluationHistoryService)
- ✅ Script SQL: `evaluation_history.sql`
- ℹ️ La entidad se generará automáticamente desde JSON EnArt

#### INC-010: Umbrales Configurables
- ✅ Ya implementado (no requiere cambios)

### **Próximos Pasos:**
1. Ejecutar scripts SQL en base de datos
2. Generar entidad `EvaluationHistory.java` desde JSON EnArt (si se necesita nueva tabla)
3. Integrar BusinessServices en ViewModels/Delegates correspondientes
4. Verificar que la entidad existente `EvaluationHistory` (GOVEVAHISTORY) sea compatible o crear nueva según JSON

**Última Actualización:** 25 de noviembre de 2025
