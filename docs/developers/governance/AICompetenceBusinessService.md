# AICompetenceBusinessService

**Ubicación:** `com.codeflowx.govern.business.governance.AICompetenceBusinessService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Gestiona competencias y awareness según ISO/IEC 42001 Clauses 7.2/7.3. Permite rastrear competencias de personas, registros de entrenamiento y análisis de gaps.

---

## 🎯 Responsabilidades

- Gestionar competencias de personas
- Registrar entrenamientos y certificaciones
- Realizar análisis de gaps de competencias
- Gestionar niveles de awareness (NONE, BASIC, INTERMEDIATE, ADVANCED)

---

## 📚 API Pública

### `findAllCompetences()`

Obtiene todas las competencias ordenadas por rol y nombre.

**Retorna:** `List<AICCompetence>`

---

### `findTrainingRecords(Long competenceId)`

Obtiene registros de entrenamiento para una competencia.

**Retorna:** `List<AITTrainingRecord>` ordenados por fecha descendente

---

### `assessCompetenceGap(Long competenceId)`

Realiza análisis de gap de competencias.

**Retorna:** `GapAnalysisResult` con:
- `currentLevel`: Nivel actual
- `requiredLevel`: Nivel requerido
- `gap`: Diferencia
- `recommendations`: Recomendaciones de entrenamiento

---

## 📖 Referencias

- **ISO/IEC 42001 Clauses 7.2/7.3:** Competence and Awareness
- **ViewModels:** Competencias y entrenamientos

---

**Última actualización:** 25 de noviembre de 2025
