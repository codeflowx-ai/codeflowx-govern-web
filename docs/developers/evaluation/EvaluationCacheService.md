# EvaluationCacheService

**Ubicación:** `com.codeflowx.govern.business.evaluation.EvaluationCacheService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Proporciona caché en memoria para evaluaciones técnicas mejorando performance al evitar consultas repetidas a BD.

---

## 🎯 Responsabilidades

- Cachear evaluaciones con TTL de 1 hora
- Invalidar caché cuando se actualiza una evaluación
- Limpiar entradas expiradas automáticamente

---

## ⚙️ Configuración

**TTL:** 1 hora (3600000 ms) - Configurable

**Implementación:** ConcurrentHashMap (en memoria)
**Recomendación Producción:** Usar Redis para caché distribuido

---

## 📚 API Pública

### `getCachedModelEvaluation(Long modelId, String evaluationType)`

Obtiene evaluación desde caché o BD.

**Parámetros:**
- `modelId`: ID del modelo
- `evaluationType`: Tipo de evaluación

**Retorna:** `ModelEvaluation` (desde caché si disponible, sino desde BD)

---

### `getCachedRagEvaluation(Long ragSystemId, String evaluationType)`

Obtiene evaluación RAG desde caché o BD.

---

### `invalidateModelEvaluationCache(Long modelId)`

Invalida caché de un modelo.

---

### `invalidateRagEvaluationCache(Long ragSystemId)`

Invalida caché de un sistema RAG.

---

### `cleanExpiredCache()`

Limpia entradas de caché expiradas.

**Uso:** Ejecutar periódicamente (job scheduler)

---

## 📖 Referencias

- **Prompt:** INC-022
- **ViewModels:** Todos los ViewModels con evaluaciones

---

**Última actualización:** 25 de noviembre de 2025
