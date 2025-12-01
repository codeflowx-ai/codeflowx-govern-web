# AIObjectivesBusinessService

**Ubicación:** `com.codeflowx.govern.business.governance.AIObjectivesBusinessService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Gestiona objetivos de IA según ISO/IEC 42001 Clause 6.2. Permite definir, rastrear y evaluar objetivos de IA con métricas específicas.

---

## 🎯 Responsabilidades

- Crear y gestionar objetivos de IA
- Calcular progreso hacia objetivos
- Evaluar cumplimiento de objetivos
- Gestionar estados (ACTIVE, ACHIEVED, REVISED, DISCONTINUED)

---

## 📚 API Pública

### `findAllObjectives()`

Obtiene todos los objetivos ordenados por categoría y métrica.

**Retorna:** `List<AIObjective>`

---

### `createObjective(AIObjective objective)`

Crea nuevo objetivo de IA.

**Validaciones:**
- Descripción obligatoria
- Categoría obligatoria
- Métrica obligatoria
- Valor objetivo obligatorio
- Responsable obligatorio
- Frecuencia de revisión obligatoria

**Estados:** Por defecto "ACTIVE"

---

### `updateObjectiveProgress(Long objectiveId, BigDecimal currentValue)`

Actualiza progreso hacia el objetivo.

**Parámetros:**
- `objectiveId`: ID del objetivo
- `currentValue`: Valor actual de la métrica

---

### `evaluateObjective(Long objectiveId)`

Evalúa si el objetivo se ha cumplido.

**Retorna:** `Map<String, Object>` con:
- `isAchieved`: Boolean
- `progressPercentage`: Porcentaje de progreso
- `remainingGap`: Diferencia restante

---

## 📖 Referencias

- **ISO/IEC 42001 Clause 6.2:** AI Objectives
- **ViewModels:** Objetivos relacionados con governance

---

**Última actualización:** 25 de noviembre de 2025
