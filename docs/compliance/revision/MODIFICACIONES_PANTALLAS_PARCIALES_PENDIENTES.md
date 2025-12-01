# MODIFICACIONES REQUERIDAS EN MAPEO_PROMPTS_VIEWMODELS_GOVERNANCE_CORREGIDO.md

**Fecha:** 25 de noviembre de 2025
**Objetivo:** Identificar qué modificaciones se requieren en `MAPEO_PROMPTS_VIEWMODELS_GOVERNANCE_CORREGIDO.md` para las pantallas parciales y pendientes de la demo.

---

## 📊 RESUMEN EJECUTIVO

| Pantalla Demo | Estado | Prompts Asociados | Modificaciones Requeridas |
|---------------|--------|-------------------|--------------------------|
| **7. Supervisión Humana (HITL)** | ⚠️ PARCIAL | 5 prompts HITL | 1 modificación (INC-HITL-009) |
| **8. Trazabilidad y Evidencias** | ⚠️ PARCIAL | 1 prompt | 1 modificación (INC-008-001) |
| **10. Documentación / Reporte** | ⚠️ PARCIAL | 2 prompts | 1 modificación (INC-024) |
| **11. Pantalla Final** | 🔴 PENDIENTE | 0 prompts | 0 modificaciones |

**Total Modificaciones Requeridas:** 3 prompts necesitan actualización

---

## 🔍 ANÁLISIS DETALLADO

### 1. 🟩 Pantalla 7: Supervisión Humana (HITL/HOTL)

**Estado:** ⚠️ PARCIAL (existen 4 ViewModels individuales, falta dashboard consolidado)
**ViewModels a Crear:** `HitlSupervisionViewModel`

#### Prompts Asociados:

| Prompt | Estado Actual en MAPEO | Modificación Requerida |
|--------|------------------------|------------------------|
| **INC-HITL-001** | ⚠️ PENDIENTE DE REVISIÓN (línea 223) | ✅ Ya marcado correctamente |
| **INC-HITL-002** | ⚠️ PENDIENTE DE REVISIÓN (línea 228) | ✅ Ya marcado correctamente |
| **INC-HITL-003** | ⚠️ PENDIENTE DE REVISIÓN (línea 233) | ✅ Ya marcado correctamente |
| **INC-HITL-005** | ⚠️ PENDIENTE DE REVISIÓN (línea 238) | ✅ Ya marcado correctamente |
| **INC-HITL-009** | ❌ **NO TIENE ESTADO** (línea 90-92) | 🔴 **REQUIERE MODIFICACIÓN** |

#### Modificación Requerida para INC-HITL-009:

**Ubicación Actual (líneas 90-92):**
```markdown
15. **INC-HITL-009:** Dashboard Métricas HITL
    - **Componente:** Java + Frontend
    - **ViewModels Afectados:** MonitoringDashboardViewModel, GovernanceDashboardViewModel
```

**Modificación Propuesta:**
```markdown
15. **INC-HITL-009:** Dashboard Métricas HITL
    - **Componente:** Java + Frontend
    - **ViewModels Afectados:** MonitoringDashboardViewModel, GovernanceDashboardViewModel, **HitlSupervisionViewModel** (pendiente crear)
    - **Estado:** ⚠️ **PENDIENTE DE REVISIÓN** - Requiere crear dashboard consolidado de supervisión humana
    - **Relación con Demo:** Pantalla 7 - Supervisión Humana (HITL/HOTL)
```

**Justificación:**
- INC-HITL-009 menciona "Dashboard Métricas HITL" que es exactamente lo que necesita la pantalla 7
- El dashboard consolidado `HitlSupervisionViewModel` debe incluir métricas HITL
- Debe marcarse como pendiente de revisión para consistencia con otros prompts HITL

---

### 2. 🟩 Pantalla 8: Trazabilidad y Evidencias

**Estado:** ⚠️ PARCIAL (existen ViewModels individuales, falta vista consolidada)
**ViewModels a Crear:** `TraceabilityEvidenceViewModel`

#### Prompts Asociados:

| Prompt | Estado Actual en MAPEO | Modificación Requerida |
|--------|------------------------|------------------------|
| **INC-008-001** | ❌ **NO TIENE ESTADO** (línea 114-116) | 🔴 **REQUIERE MODIFICACIÓN** |

#### Modificación Requerida para INC-008-001:

**Ubicación Actual (líneas 114-116):**
```markdown
20. **INC-008-001:** Trazabilidad Completa Modelo-Dataset-Output
    - **Componente:** Java Backend + SQL
    - **ViewModels Afectados:** Todos los ViewModels que muestran trazabilidad (ModelLineageTreeViewModel, ProjectsDashboardViewModel)
```

**Modificación Propuesta:**
```markdown
20. **INC-008-001:** Trazabilidad Completa Modelo-Dataset-Output
    - **Componente:** Java Backend + SQL
    - **ViewModels Afectados:** Todos los ViewModels que muestran trazabilidad (ModelLineageTreeViewModel, ProjectsDashboardViewModel), **TraceabilityEvidenceViewModel** (pendiente crear)
    - **Estado:** ⚠️ **PENDIENTE** - Requiere crear vista consolidada de trazabilidad y evidencias
    - **Relación con Demo:** Pantalla 8 - Trazabilidad y Evidencias
    - **Integración:** Requiere integración con `ImmutableLoggingBusinessService` y telemetría
```

**Justificación:**
- INC-008-001 menciona "Trazabilidad Completa" que es exactamente lo que necesita la pantalla 8
- La vista consolidada `TraceabilityEvidenceViewModel` debe mostrar trazabilidad modelo-dataset-output
- Debe marcarse como pendiente para reflejar que falta la vista consolidada

---

### 3. 🟩 Pantalla 10: Documentación Automática / Reporte Final

**Estado:** ⚠️ PARCIAL (existen generadores individuales, falta reporte consolidado)
**ViewModels a Crear:** `ComplianceReportViewModel`

#### Prompts Asociados:

| Prompt | Estado Actual en MAPEO | Modificación Requerida |
|--------|------------------------|------------------------|
| **INC-008-002** | 🔴 PENDIENTE (Crítica) (línea 118-120) | ✅ Ya marcado correctamente |
| **INC-024** | ❌ **NO TIENE ESTADO** (línea 305-306) | 🔴 **REQUIERE MODIFICACIÓN** |

#### Modificación Requerida para INC-024:

**Ubicación Actual (líneas 305-306):**
```markdown
67. **INC-024:** Reporte Ejecutivo Consolidado (ComplianceExecutiveReportService)
    - **ViewModels Afectados:** AnalyticsReportViewModel, GovernanceDashboardViewModel
```

**Modificación Propuesta:**
```markdown
67. **INC-024:** Reporte Ejecutivo Consolidado (ComplianceExecutiveReportService)
    - **Componente:** Java Backend + Frontend
    - **ViewModels Afectados:** AnalyticsReportViewModel, GovernanceDashboardViewModel, **ComplianceReportViewModel** (pendiente crear)
    - **Estado:** ⚠️ **PENDIENTE** - Requiere crear reporte consolidado para demo
    - **Relación con Demo:** Pantalla 10 - Documentación Automática / Reporte Final
    - **BusinessService:** ComplianceExecutiveReportService (pendiente crear o verificar)
```

**Justificación:**
- INC-024 menciona "Reporte Ejecutivo Consolidado" que es exactamente lo que necesita la pantalla 10
- El reporte consolidado `ComplianceReportViewModel` debe incluir exportación a PDF y evidencias
- Debe marcarse como pendiente para reflejar que falta el reporte consolidado

---

### 4. 🔴 Pantalla 11: Pantalla Final / Cierre

**Estado:** 🔴 PENDIENTE (requiere crear ZUL simple)
**ViewModels a Crear:** Ninguno (pantalla estática)

#### Prompts Asociados:

| Prompt | Estado Actual en MAPEO | Modificación Requerida |
|--------|------------------------|------------------------|
| **Ninguno** | - | ✅ No requiere modificaciones |

**Justificación:**
- Es una pantalla estática sin lógica de negocio
- No requiere prompts asociados
- No requiere modificaciones en el documento de mapeo

---

## 📝 RESUMEN DE MODIFICACIONES

### Modificaciones a Realizar en `MAPEO_PROMPTS_VIEWMODELS_GOVERNANCE_CORREGIDO.md`:

1. **INC-HITL-009 (línea 90-92):**
   - ✅ Agregar `HitlSupervisionViewModel` a ViewModels Afectados
   - ✅ Agregar estado: "⚠️ PENDIENTE DE REVISIÓN"
   - ✅ Agregar nota sobre relación con demo

2. **INC-008-001 (línea 114-116):**
   - ✅ Agregar `TraceabilityEvidenceViewModel` a ViewModels Afectados
   - ✅ Agregar estado: "⚠️ PENDIENTE"
   - ✅ Agregar nota sobre relación con demo e integración

3. **INC-024 (línea 305-306):**
   - ✅ Agregar `ComplianceReportViewModel` a ViewModels Afectados
   - ✅ Agregar componente: "Java Backend + Frontend"
   - ✅ Agregar estado: "⚠️ PENDIENTE"
   - ✅ Agregar nota sobre relación con demo

---

## 🎯 IMPACTO EN DEMO

### Pantallas que Requieren Implementación:

| Pantalla | ViewModel a Crear | Prompts Relacionados | Esfuerzo |
|----------|-------------------|---------------------|----------|
| **7. Supervisión Humana** | `HitlSupervisionViewModel` | INC-HITL-001, INC-HITL-002, INC-HITL-003, INC-HITL-005, **INC-HITL-009** | 5-7 días |
| **8. Trazabilidad** | `TraceabilityEvidenceViewModel` | **INC-008-001** | 5-7 días |
| **10. Reporte** | `ComplianceReportViewModel` | INC-008-002, **INC-024** | 4-6 días |
| **11. Cierre** | Ninguno | Ninguno | 0.5 días |

**Total Esfuerzo:** 15-21 días

---

## ✅ CHECKLIST DE MODIFICACIONES

- [ ] Modificar INC-HITL-009 (línea 90-92)
- [ ] Modificar INC-008-001 (línea 114-116)
- [ ] Modificar INC-024 (línea 305-306)
- [ ] Verificar que todos los prompts HITL estén marcados como "PENDIENTE DE REVISIÓN"
- [ ] Verificar que INC-008-002 esté marcado como "PENDIENTE (Crítica)"

---

**Última actualización:** 25 de noviembre de 2025
**Total Modificaciones Requeridas:** 3 prompts
