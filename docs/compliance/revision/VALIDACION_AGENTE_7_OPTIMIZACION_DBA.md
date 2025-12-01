# VALIDACIÓN AGENTE 7 - OPTIMIZACIÓN Y DBA

**Fecha:** 25 de noviembre de 2025
**Documento Validado:** `AGENTE_7_OPTIMIZACION_DBA.md`
**Validador:** AI Assistant
**Estado General:** ⚠️ **VALIDADO CON OBSERVACIONES**

---

## ✅ INFORMACIÓN DISPONIBLE

### 1. Documentos de Referencia ✅

| Documento | Estado | Ubicación |
|-----------|--------|-----------|
| **INC-010-011 Prompt** | ✅ DISPONIBLE | `/docs/compliance/gaps/prompts/dba/INC-010-011_optimizacion_consultas.md` |
| **INC-009-005 Prompt** | ✅ DISPONIBLE | `/docs/compliance/gaps/prompts/dba/INC-009-005_optimizacion_queries.md` |
| **Arquitectura EnArt** | ✅ DISPONIBLE | `/docs/compliance/PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md` |
| **Plan Nocturno** | ✅ DISPONIBLE | `/docs/compliance/revision/PLAN_TRABAJO_NOCTURNO.md` |

### 2. Estructura de Directorios ✅

| Directorio | Estado | Observaciones |
|------------|--------|---------------|
| `/sql/optimization/` | ✅ EXISTE | Preparado para scripts |
| `/sql/configuration/` | ✅ EXISTE | Preparado para scripts |
| `/business/governance/` | ✅ EXISTE | Preparado para BusinessServices |

### 3. Servicios Existentes ✅

| Servicio | Estado | Ubicación |
|----------|--------|-----------|
| **PostMarketMonitoringService** | ✅ EXISTE | `codeflowx.govern.business/compliance/PostMarketMonitoringService.java` |
| **GovernanceMetricService** | ✅ EXISTE | `codeflowx.govern.services/governance/GovernanceMetricService.java` |
| **MonitoringMetricService** | ✅ EXISTE | `codeflowx.govern.services/monitoring/MonitoringMetricService.java` |
| **ModelEvaluationService** | ✅ EXISTE | `codeflowx.govern.services/evaluation/ModelEvaluationService.java` |
| **AlertThresholdService** | ✅ EXISTE | `codeflowx.govern.business/compliance/AlertThresholdService.java` |

### 4. Entidades Existentes ✅

| Entidad | Estado | Observaciones |
|---------|--------|---------------|
| **AlertThreshold** | ✅ EXISTE | Tabla `ALRALERTTHRESHOLDS`, prefijo `ALR` |
| **MonitoringMetric** | ✅ EXISTE | Tabla `MONMONITORINGMETRICS` |
| **MonitoringAlert** | ✅ EXISTE | Tabla `MONMONITORINGALERTS` |

---

## ⚠️ PROBLEMAS IDENTIFICADOS

### 🔴 CRÍTICO - INC-010-011: Campos Faltantes

**Problema:**
El prompt `INC-010-011_optimizacion_consultas.md` menciona campos `IDXPROJECT` e `IDXMODEL` en las tablas `MONMONITORINGMETRICS` y `MONMONITORINGALERTS`:

```sql
-- Del prompt INC-010-011:
SELECT
    time_bucket('1 day', MONCREATEDAT) AS bucket,
    IDXPROJECT,  -- ❌ NO EXISTE en la entidad
    IDXMODEL,    -- ❌ NO EXISTE en la entidad
    MONMETRICNAME,
    ...
FROM MONMONITORINGMETRICS
```

**Realidad:**
- La entidad `MonitoringMetric.java` **NO tiene** campos `IDXPROJECT` ni `IDXMODEL`
- La entidad `MonitoringAlert.java` **NO tiene** campos `IDXPROJECT` ni `IDXMODEL`

**Campos que SÍ existen:**
- `MonitoringMetric`: `idxmonitoringmetric`, `monmetricname`, `monmetrictype`, `monmetricvalue`, `moncreatedat`, `moncontext` (JSONB), `monmetadata` (JSONB)
- `MonitoringAlert`: `idxmonitoringalert`, `monalertname`, `monalerttype`, `monseverity`, `monstatus`, `montriggeredat`, `monalertdata` (JSONB)

**Solución Requerida:**
1. **Opción A:** Agregar campos `IDXPROJECT` e `IDXMODEL` a las tablas (requiere migración SQL)
2. **Opción B:** Modificar el script para usar `MONCONTEXT`/`MONMETADATA` (JSONB) si los IDs están allí
3. **Opción C:** Ajustar el prompt para usar las tablas correctas que sí tienen estos campos

**Recomendación:** Verificar si los IDs de proyecto/modelo están almacenados en los campos JSONB (`moncontext`, `monmetadata`) antes de agregar columnas.

---

### 🟡 MEDIO - INC-007-DS: Discrepancia en Nombres

**Problema:**
- El documento `AGENTE_7_OPTIMIZACION_DBA.md` especifica:
  - Tabla: `BENBENCHMARKINDUSTRIES` (prefijo `BEN`)
  - Entidad: `BenchmarkIndustry.java`

- El prompt `INC-007_benchmarks_industria.md` especifica:
  - Tabla: `CORBENCHMARKS` (prefijo `COR`)
  - Entidad: `Benchmark.java`

**Solución Requerida:**
Decidir cuál nomenclatura usar y alinear ambos documentos.

**Recomendación:** Seguir el prompt específico (`CORBENCHMARKS`) ya que es más detallado, pero actualizar `AGENTE_7_OPTIMIZACION_DBA.md` para reflejarlo.

---

### 🟡 MEDIO - INC-010: Prompt Muy Básico

**Problema:**
El prompt `INC-010_umbrales_configurables.md` es muy básico (solo 60 líneas) y no especifica:
- Arquitectura EnArt completa (JSON + Entity + BusinessService)
- Nomenclatura exacta de campos
- Integración con servicios existentes
- Scripts SQL completos

**Observación:**
Ya existe la entidad `AlertThreshold` (`ALRALERTTHRESHOLDS`), pero el prompt sugiere crear `GOVMETRICTHRESHOLDS`.

**Solución Requerida:**
- Verificar si `AlertThreshold` cumple con los requisitos
- Si no, crear servicio `MetricThresholdService` que use `AlertThreshold`
- Actualizar prompt o crear servicio adicional según necesidad

---

### 🟡 MEDIO - INC-014: Prompt Muy Básico

**Problema:**
El prompt `INC-014_retencion_historica.md` es muy básico (solo 53 líneas) y no especifica:
- Nomenclatura EnArt (prefijo, tabla exacta)
- Arquitectura completa
- Scripts SQL detallados
- Integración con servicios de evaluación

**Solución Requerida:**
- Definir prefijo y nombre de tabla según convenciones EnArt
- Crear JSON EnArt completo
- Generar Entity.java
- Crear BusinessService completo

---

## ✅ INFORMACIÓN COMPLETA PARA EJECUTAR

### INC-010-011: Optimización de Consultas ⚠️

**Estado:** ⚠️ **REQUIERE AJUSTES**

**Información Disponible:**
- ✅ Prompt completo y detallado
- ✅ Ejemplos de continuous aggregates
- ✅ Scripts SQL completos
- ✅ Funciones de consulta optimizadas

**Información Faltante:**
- ❌ Verificar campos `IDXPROJECT`/`IDXMODEL` en tablas o usar JSONB
- ⚠️ Ajustar scripts según estructura real de tablas

**Acción Requerida:**
1. Verificar estructura real de `MONMONITORINGMETRICS` y `MONMONITORINGALERTS`
2. Ajustar scripts del prompt según campos disponibles
3. Crear vistas materializadas adaptadas

---

### INC-007-DS: Benchmarks Industria ✅

**Estado:** ✅ **LISTO PARA EJECUTAR** (con ajuste de nomenclatura)

**Información Disponible:**
- ✅ Prompt completo con código Java
- ✅ Script SQL con data seed
- ✅ Service completo con lógica de comparación
- ✅ Arquitectura definida

**Acción Requerida:**
1. Decidir nomenclatura: `BENBENCHMARKINDUSTRIES` vs `CORBENCHMARKS`
2. Actualizar `AGENTE_7_OPTIMIZACION_DBA.md` si se usa `CORBENCHMARKS`
3. Crear según prompt (que usa `CORBENCHMARKS`)

---

### INC-010: Umbrales Configurables ⚠️

**Estado:** ⚠️ **REQUIERE DESARROLLO**

**Información Disponible:**
- ✅ Entidad `AlertThreshold` ya existe
- ✅ Service `AlertThresholdService` ya existe
- ✅ Script SQL `alert_thresholds.sql` ya existe

**Información Faltante:**
- ❌ Prompt muy básico
- ❌ No especifica integración con métricas PMM
- ❌ No especifica configuración por proyecto/modelo

**Acción Requerida:**
1. Verificar si `AlertThreshold` cumple todos los requisitos
2. Si cumple: Documentar que ya está implementado
3. Si no cumple: Extender servicio según necesidades

---

### INC-014: Retención Histórica ⚠️

**Estado:** ⚠️ **REQUIERE DESARROLLO COMPLETO**

**Información Disponible:**
- ✅ Prompt básico con estructura simple
- ✅ Servicios de evaluación existen

**Información Faltante:**
- ❌ Prompt muy básico (solo 53 líneas)
- ❌ No especifica nomenclatura EnArt completa
- ❌ No tiene JSON EnArt
- ❌ No tiene BusinessService completo
- ❌ No tiene scripts SQL detallados

**Acción Requerida:**
1. Crear prompt completo siguiendo arquitectura EnArt
2. Definir: Tabla `EVHEVALUATIONHISTORIES` (prefijo `EVH`)
3. Crear JSON EnArt completo
4. Generar Entity.java
5. Crear BusinessService completo
6. Integrar con servicios de evaluación existentes

---

### INC-009-005: Optimización Queries ✅

**Estado:** ✅ **LISTO PARA EJECUTAR**

**Información Disponible:**
- ✅ Prompt completo y detallado (260 líneas)
- ✅ Scripts SQL con índices GIN
- ✅ Continuous aggregates por hora
- ✅ Ejemplos de queries optimizadas
- ✅ Métricas de mejora esperadas

**Acción Requerida:**
1. Crear scripts según prompt
2. Aplicar en base de datos
3. Verificar mejoras de rendimiento

---

## 📋 CHECKLIST DE VALIDACIÓN

### Información General
- [x] Documentos de referencia disponibles
- [x] Estructura de directorios verificada
- [x] Servicios existentes identificados
- [x] Entidades existentes identificadas

### Incidencias Específicas
- [x] INC-010-011: Prompt disponible, requiere ajustes
- [x] INC-007-DS: Prompt disponible, requiere decisión nomenclatura
- [x] INC-010: Entidad existe, prompt básico
- [x] INC-014: Prompt básico, requiere desarrollo completo
- [x] INC-009-005: Prompt completo, listo para ejecutar

### Acciones Previas a Ejecución
- [ ] Resolver discrepancia campos IDXPROJECT/IDXMODEL (INC-010-011)
- [ ] Decidir nomenclatura benchmarks (INC-007-DS)
- [ ] Verificar si AlertThreshold cumple requisitos (INC-010)
- [ ] Crear prompt completo para INC-014 o reutilizar estructura existente

---

## 🎯 RECOMENDACIONES FINALES

### Prioridad Alta (Bloqueantes)

1. **INC-010-011:** Verificar y ajustar campos de tablas antes de crear vistas materializadas
2. **INC-014:** Crear prompt completo antes de implementar (o reutilizar estructura similar existente)

### Prioridad Media

3. **INC-007-DS:** Decidir nomenclatura y alinear documentos
4. **INC-010:** Verificar si implementación actual cumple requisitos

### Prioridad Baja

5. **INC-009-005:** Ejecutar directamente (está completo)

---

## ✅ CONCLUSIÓN

**Estado General:** ⚠️ **VALIDADO CON OBSERVACIONES**

**Resumen:**
- ✅ **3 de 5 incidencias** tienen información completa o suficiente
- ⚠️ **2 de 5 incidencias** requieren ajustes o desarrollo adicional
- ✅ **Estructura y servicios base** están disponibles
- ⚠️ **Algunos prompts son básicos** y requieren expandir siguiendo arquitectura EnArt

**Recomendación:**
El documento puede ejecutarse, pero se requiere:
1. Resolver el tema de campos IDXPROJECT/IDXMODEL para INC-010-011
2. Expandir prompts básicos (INC-010, INC-014) siguiendo arquitectura EnArt
3. Decidir nomenclatura para INC-007-DS

**Tiempo Estimado Adicional:** ✅ **COMPLETADO** - Todas las observaciones han sido resueltas.

---

## ✅ ACTUALIZACIÓN - OBSERVACIONES RESUELTAS

**Fecha Resolución:** 25 de noviembre de 2025

### Problemas Resueltos:

1. ✅ **INC-010-011:** Script ajustado para usar campos JSONB (`MONCONTEXT`, `MONMETADATA`) en lugar de campos directos
2. ✅ **INC-007-DS:** Nomenclatura alineada - usar `CORBENCHMARKS` según prompt específico
3. ✅ **INC-010:** Verificado que `AlertThreshold` cumple requisitos - ya implementado
4. ✅ **INC-014:** Prompt expandido con arquitectura EnArt completa (300+ líneas, JSON + Entity + Service + SQL)

### Estado Final:

| Incidencia | Estado | Observaciones |
|------------|--------|---------------|
| INC-010-011 | ✅ LISTO | Script ajustado para JSONB |
| INC-007-DS | ✅ LISTO | Nomenclatura alineada |
| INC-010 | ✅ YA IMPLEMENTADO | AlertThreshold cumple requisitos |
| INC-014 | ✅ LISTO | Prompt expandido completo |
| INC-009-005 | ✅ LISTO | Prompt completo desde inicio |

**El documento AGENTE_7_OPTIMIZACION_DBA.md está listo para ejecutarse sin bloqueantes.**

---

**Última Actualización:** 25 de noviembre de 2025
**Validador:** AI Assistant
**Estado:** ✅ RESUELTO
