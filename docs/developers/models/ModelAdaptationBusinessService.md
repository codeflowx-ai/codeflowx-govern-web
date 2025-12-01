# ModelAdaptationBusinessService

**Ubicación:** `com.codeflowx.govern.business.models.ModelAdaptationBusinessService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Gestiona adaptación de modelos según Art. 51-55 del EU AI Act para GPAI Downstream Providers. Proporciona recomendaciones de estrategias de adaptación basadas en análisis multi-criteria.

---

## 🎯 Responsabilidades

- Recomendar estrategias de adaptación óptimas
- Guardar estrategias de adaptación seleccionadas
- Integrar con microservicio Python para análisis avanzado

---

## ⚙️ Configuración

**Property:** `leka.model.wrapper.url` (default: `http://localhost:8006`)

---

## 📚 API Pública

### `recommendStrategy(AdaptationRequest request)`

Recomienda estrategia de adaptación óptima.

**Parámetros:**
- `request`: `AdaptationRequest` con:
  - `projectId`: ID del proyecto
  - `useCase`: Caso de uso
  - `budgetUsd`: Presupuesto en USD
  - `timeDays`: Tiempo disponible en días
  - `targetPerformance`: Performance objetivo
  - `hardware`: Hardware disponible
  - `priority`: Prioridad

**Retorna:** `AdaptationRecommendation` con:
- `recommendedStrategy`: Estrategia recomendada
- `confidence`: Nivel de confianza
- `alternatives`: Estrategias alternativas

**Integración:** Llama microservicio Python en `/api/model/recommend-adaptation`. Si falla, usa recomendación por defecto.

---

### `saveStrategy(AdaptationRequest request, AdaptationRecommendation recommendation, String selectedStrategy, String selectionReason)`

Guarda estrategia de adaptación en base de datos.

**Retorna:** `ModelAdaptationStrategy` guardada

---

## 📖 Referencias

- **Art. 51-55 EU AI Act:** GPAI Downstream Providers
- **Microservicio:** Python model wrapper service

---

**Última actualización:** 25 de noviembre de 2025
