# EvaluationHistoryService

**Ubicación:** `com.codeflowx.govern.business.evaluation.EvaluationHistoryService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Gestiona historial completo de evaluaciones técnicas de modelos y sistemas RAG para trazabilidad y auditoría.

---

## 🎯 Responsabilidades

- Consultar historial de evaluaciones
- Calcular estadísticas y tendencias
- Archivar evaluaciones antiguas según políticas de retención

---

## 📚 API Pública

### `getModelEvaluationHistory(Long modelId, Integer limit)`

Obtiene historial de evaluaciones de un modelo.

**Parámetros:**
- `modelId`: ID del modelo
- `limit`: Número máximo de resultados (null = sin límite)

**Retorna:** `List<ModelEvaluation>` ordenadas por fecha descendente

---

### `getRagEvaluationHistory(Long ragSystemId, Integer limit)`

Obtiene historial de evaluaciones de un sistema RAG.

---

### `getModelEvaluationStatistics(Long modelId)`

Calcula estadísticas del historial.

**Retorna:** `Map<String, Object>` con:
- `totalEvaluations`: Número total
- `averageScore`: Score promedio
- `lastEvaluationDate`, `lastEvaluationScore`
- `trend`: "IMPROVING", "DECLINING", "STABLE"

---

### `archiveOldEvaluations(Integer retentionDays)`

Archiva evaluaciones antiguas.

**Parámetros:**
- `retentionDays`: Días de retención

**Retorna:** `int` - Número de evaluaciones archivadas

---

## 📖 Referencias

- **Prompt:** INC-014
- **ViewModels:** ModelsDetailViewModel, RagSystemsDetailViewModel

---

**Última actualización:** 25 de noviembre de 2025
