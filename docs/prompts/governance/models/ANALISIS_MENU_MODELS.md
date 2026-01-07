# Análisis del Menú de Models - Estado Actual

**Fecha:** Diciembre 2025

## Estado Actual del Menú Models

El menú Models tiene las siguientes opciones (según `app/config/modules.ts` líneas 1234-1342):

1. ✅ **Registry** - `/models/registry` - **OPERATIVO**
2. ✅ **Providers** - `/models/providers` - **OPERATIVO**
3. ⚠️ **Approval** - `/models/approval` - **PARCIALMENTE OPERATIVO**
4. ⚠️ **Bias Analysis** - `/models/bias-analysis` - **NO OPERATIVO / DATOS INCORRECTOS**
5. ⚠️ **Explainability** - `/models/explainability` - **NO OPERATIVO / DATOS INCORRECTOS**
6. ⚠️ **Performance** - `/models/performance` - **PARCIALMENTE OPERATIVO**
7. ⚠️ **Usage Overview** - `/models/performance/usage-overview` - **POR VERIFICAR**

---

## Análisis Detallado por Pantalla

### ✅ 1. Registry (`/models/registry`)
**Estado:** ✅ **OPERATIVO**
- Pantalla completa con listado, filtros, creación, edición
- Página de detalle con 6 pestañas (General, Versiones, Proyectos, Costes, Métricas, Despliegues)
- UUID automático, versionado semántico
- Mock data funcional

### ✅ 2. Providers (`/models/providers`)
**Estado:** ✅ **OPERATIVO**
- Pantalla completa con listado, filtros, creación
- Página de detalle con 5 pestañas (General, Credenciales, Modelos, Proyectos, Costes)
- Gestión de credenciales con asociación opcional a proyectos
- Mock data funcional

### ⚠️ 3. Approval (`/models/approval`)
**Estado:** ⚠️ **PARCIALMENTE OPERATIVO**
- **Pantalla existe:** `app/(app)/models/approval/page.tsx`
- **Funcionalidad:** Tiene listado de aprobaciones, filtros, modales de aprobación/rechazo
- **Problema:**
  - Según `cursor_front_back_models.md`, debería estar en `governance/models/approval`
  - Funcionalidad parece correcta pero podría necesitar integración con workflow BPMN
  - Mock data presente pero podría necesitar datos reales del workflow

**Recomendación:** ✅ **MANTENER** pero verificar integración con workflow BPMN

### ❌ 4. Bias Analysis (`/models/bias-analysis`)
**Estado:** ❌ **NO OPERATIVO / DATOS INCORRECTOS**
- **Pantalla existe:** `app/(app)/models/bias-analysis/page.tsx`
- **Problema:**
  - Usa interfaz `BiasAnalysis` con campos incorrectos: `userId`, `projectId`, `taskType`, `taskDescription`, `requirements`
  - No tiene campos relevantes para análisis de sesgo: `biasScore`, `detectedBias`, `affectedGroups`, etc.
  - Mock data no corresponde a funcionalidad de análisis de sesgo
  - Según `cursor_front_back_models.md`, debería mostrar:
    - Total de análisis
    - Análisis con sesgo detectado
    - Análisis sin sesgo
    - Score promedio de sesgo
    - Filtros por nombre de modelo, tipo, nivel de sesgo

**Recomendación:** ❌ **REESCRIBIR COMPLETAMENTE** o eliminar si no es necesaria

### ❌ 5. Explainability (`/models/explainability`)
**Estado:** ❌ **NO OPERATIVO / DATOS INCORRECTOS**
- **Pantalla existe:** `app/(app)/models/explainability/page.tsx`
- **Problema:**
  - Usa interfaz `StageTransition` que no tiene sentido para explicabilidad
  - Campos: `fromStage`, `toStage`, `transitionReason`, `archiveExistingVersions`, `transitionedBy`
  - No tiene campos relevantes para explicabilidad: `explainabilityScore`, `explainabilityMethod`, `featureImportance`, etc.
  - Mock data no corresponde a funcionalidad de explicabilidad
  - Según `cursor_front_back_models.md`, debería mostrar:
    - Total de análisis
    - Modelos explicables
    - Modelos no explicables
    - Score promedio
    - Filtros por nombre, tipo, score de explicabilidad

**Recomendación:** ❌ **REESCRIBIR COMPLETAMENTE** o eliminar si no es necesaria

### ⚠️ 6. Performance (`/models/performance`)
**Estado:** ⚠️ **PARCIALMENTE OPERATIVO**
- **Pantalla existe:** `app/(app)/models/performance/page.tsx`
- **Problema:**
  - Usa interfaz `ModelComparison` que es más para comparación de modelos
  - No tiene métricas de rendimiento individuales
  - Según `cursor_front_back_models.md`, debería mostrar:
    - Total de modelos monitoreados
    - Modelos en objetivo
    - Modelos bajo rendimiento
    - Score promedio
    - Métricas de rendimiento por modelo

**Recomendación:** ⚠️ **REVISAR Y MEJORAR** o renombrar si es para comparación

### ⚠️ 7. Usage Overview (`/models/performance/usage-overview`)
**Estado:** ⚠️ **POR VERIFICAR**
- **Pantalla existe:** `app/(app)/models/performance/usage-overview/page.tsx`
- Según `cursor_front_back_models.md`, debería mostrar:
  - Listado de uso de modelos
  - Campos: ID, Request ID, User ID, Project ID, Agent ID, Prompt ID
  - Filtros por tipo y request ID

**Recomendación:** ⚠️ **VERIFICAR IMPLEMENTACIÓN**

---

## Discrepancia de Ubicación

### Problema Identificado

Según `cursor_front_back_models.md`:
- Las pantallas de governance están documentadas como: `governance/models/approval`, `governance/models/bias-analysis`, etc.
- Pero en realidad están implementadas en: `models/approval`, `models/bias-analysis`, etc.

### Ubicación Real vs Documentada

| Pantalla | Documentado en | Implementado en | Menú apunta a |
|----------|---------------|-----------------|---------------|
| Approval | `governance/models/approval` | `models/approval` | `/models/approval` ✅ |
| Bias Analysis | `governance/models/bias-analysis` | `models/bias-analysis` | `/models/bias-analysis` ✅ |
| Explainability | `governance/models/explainability` | `models/explainability` | `/models/explainability` ✅ |
| Performance | `governance/models/performance` | `models/performance` | `/models/performance` ✅ |
| Usage Overview | `governance/models/performance/usage-overview` | `models/performance/usage-overview` | `/models/performance/usage-overview` ✅ |

**Conclusión:** Las pantallas están en la ubicación correcta según el menú (`/models/`), pero la documentación dice que deberían estar en `/governance/models/`. El menú actual es correcto.

---

## Recomendaciones

### Opción 1: Mantener Estructura Actual (Recomendado)
- **Mantener:** Registry, Providers, Approval (mejorar integración)
- **Reescribir:** Bias Analysis, Explainability (con funcionalidad correcta)
- **Revisar:** Performance, Usage Overview
- **Justificación:** Las pantallas de governance de modelos están bien en el módulo Models, ya que son parte de la gestión técnica de modelos

### Opción 2: Mover a Governance
- Mover Approval, Bias Analysis, Explainability, Performance a `/governance/models/`
- Dejar Registry y Providers en `/models/`
- **Justificación:** Separar gestión técnica (models) de gobernanza (governance/models)
- **Inconveniente:** Requiere cambios en rutas, menú, y navegación

### Opción 3: Eliminar Pantallas No Operativas
- Eliminar o comentar del menú: Bias Analysis, Explainability (hasta que se reescriban)
- Mantener: Registry, Providers, Approval
- **Justificación:** No mostrar opciones no funcionales en el menú

---

## Acciones Inmediatas Recomendadas

### Prioridad Alta
1. ✅ **Mantener:** Registry, Providers (ya operativos)
2. ⚠️ **Mejorar:** Approval (verificar integración con workflow)
3. ❌ **Reescribir:** Bias Analysis (completar funcionalidad)
4. ❌ **Reescribir:** Explainability (completar funcionalidad)

### Prioridad Media
5. ⚠️ **Revisar:** Performance (verificar si corresponde o necesita mejoras)
6. ⚠️ **Verificar:** Usage Overview (confirmar funcionalidad)

### Prioridad Baja
7. 📝 **Actualizar:** Documentación `cursor_front_back_models.md` para reflejar ubicación real
8. 📝 **Decidir:** Si mantener todo en `/models/` o separar governance en `/governance/models/`

---

## Pantallas Adicionales a Considerar

### Pantallas Mencionadas en Catalog (No están en menú Models)

Según `cursor_front_back_models.md`, existen estas pantallas en el módulo **Catalog** relacionadas con modelos:

1. **Artifacts** (`/catalog/registry/artifact-overview`) - Artefactos de modelos
2. **Capabilities** (`/catalog/registry/capability-overview`) - Capacidades de modelos
3. **Endpoints** (`/catalog/registry/endpoint-overview`) - Endpoints de modelos
4. **Versioning** (`/catalog/versioning`) - Gestión de versiones de modelos

**Pregunta:** ¿Deberían estas pantallas estar también en el menú de Models, o se mantienen solo en Catalog?

**Recomendación:** Mantener en Catalog ya que son parte del catálogo general, no específicamente del módulo Models.

### Funcionalidades Integradas (No requieren pantallas separadas)

Las siguientes funcionalidades están **integradas** en las pantallas existentes:

1. **Gestión de Versiones** - ✅ Integrado en `/models/registry/[id]` (pestaña Versiones)
2. **Gestión de Endpoints** - ✅ Integrado en `/models/registry/[id]` (pestaña Despliegues)
3. **Gestión de Costes** - ✅ Integrado en `/models/registry/[id]` (pestaña Costes y Consumo)
4. **Métricas de Rendimiento** - ✅ Integrado en `/models/registry/[id]` (pestaña Métricas)

### Pantalla de Governance/Models

Existe una pantalla en `/governance/models/page.tsx` que es un catálogo simple. Esta pantalla **no está en el menú de Models** pero existe.

**Estado:** Pantalla simple con mock data, no está vinculada al menú.

**Recomendación:** Verificar si debe eliminarse o si debe estar en el menú (probablemente sea redundante con Registry).

---

## Conclusiones

1. **Registry y Providers están operativos** ✅
2. **Approval está parcialmente operativo** pero necesita mejoras
3. **Bias Analysis y Explainability NO están operativos** - tienen datos mock incorrectos
4. **Performance y Usage Overview necesitan revisión**
5. **La ubicación `/models/` es correcta** según el menú actual
6. **Faltan pantallas funcionales** para Bias Analysis y Explainability
7. **No sobran pantallas**, pero algunas necesitan reescritura completa
8. **No faltan pantallas nuevas** - todas las funcionalidades principales están cubiertas (integradas o como pantallas separadas)
9. **Pantalla `/governance/models` existe pero no está en menú** - verificar si es necesaria

**Recomendación Final:**
- ✅ Mantener estructura actual (`/models/`)
- ✅ **Bias Analysis REESCRITO** - Ahora operativo con funcionalidad correcta
- ✅ **Explainability REESCRITO** - Ahora operativo con funcionalidad correcta
- ⚠️ Mejorar Approval (integración con workflow BPMN)
- ⚠️ Revisar Performance (verificar si corresponde o necesita mejoras)
- ⚠️ Verificar Usage Overview (confirmar funcionalidad)
- ❓ Decidir sobre `/governance/models/page.tsx` (eliminar si es redundante con Registry)

---

## Resumen de Pantallas Pendientes

### ✅ Prioridad Alta - Completado
1. **Bias Analysis** (`/models/bias-analysis`)
   - Estado: ✅ **REESCRITO** - Interfaz y datos correctos
   - Campos implementados: `modelId`, `modelName`, `modelType`, `biasScore`, `detectedBias`, `biasLevel`, `affectedGroups`, `analysisMethod`, `recommendations`, `status`
   - Métricas: Total, Con Sesgo, Sin Sesgo, Score Promedio
   - Filtros: Por nombre, tipo de modelo, nivel de sesgo

2. **Explainability** (`/models/explainability`)
   - Estado: ✅ **REESCRITO** - Interfaz y datos correctos
   - Campos implementados: `modelId`, `modelName`, `modelType`, `explainabilityScore`, `isExplainable`, `explainabilityMethod`, `featureImportance`, `explanationQuality`, `limitations`, `recommendations`, `status`
   - Métricas: Total, Explicables, No Explicables, Score Promedio
   - Filtros: Por nombre, tipo de modelo, score de explicabilidad

### 🟡 Prioridad Media - Mejoras/Revisión
3. **Approval** (`/models/approval`)
   - Estado: ⚠️ Funcional pero necesita integración con workflow BPMN
   - Acción: Verificar integración con workflow `model-approval-v1`

4. **Performance** (`/models/performance`)
   - Estado: ⚠️ Usa interfaz `ModelComparison` que podría no ser adecuada
   - Acción: Revisar si corresponde o necesita mejoras/renombrar

5. **Usage Overview** (`/models/performance/usage-overview`)
   - Estado: ⚠️ Necesita verificación
   - Acción: Verificar implementación y funcionalidad

### 🟢 Prioridad Baja - Limpieza
6. **`/governance/models/page.tsx`**
   - Estado: ❓ Existe pero no está en menú
   - Acción: Eliminar si es redundante con Registry, o integrar si tiene funcionalidad única

---

## Conclusión: ¿Faltan Pantallas?

**No faltan pantallas nuevas**, pero sí faltan **funcionalidades correctas** en 2 pantallas existentes:

1. ❌ **Bias Analysis** - Existe pero está mal implementada (necesita reescritura)
2. ❌ **Explainability** - Existe pero está mal implementada (necesita reescritura)

**Todas las funcionalidades principales están cubiertas:**
- ✅ Registro de modelos (Registry)
- ✅ Gestión de proveedores (Providers)
- ✅ Aprobaciones (Approval - mejorar integración)
- ✅ Versiones (integrado en Registry detalle)
- ✅ Costes y métricas (integrado en Registry detalle)
- ✅ Despliegues (integrado en Registry detalle)
- ⚠️ Análisis de sesgo (Bias Analysis - reescribir)
- ⚠️ Explicabilidad (Explainability - reescribir)
- ⚠️ Rendimiento (Performance - revisar)

**Total de pantallas pendientes de trabajo:** 3 pantallas
- ✅ 2 reescritas y completadas (Bias Analysis, Explainability)
- ⚠️ 3 requieren mejoras/revisión (Approval, Performance, Usage Overview)
