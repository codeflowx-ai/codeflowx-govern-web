# ModelValidationService

**Ubicación:** `com.codeflowx.govern.business.models.ModelValidationService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Proporciona validación exhaustiva de modelos de IA incluyendo metadatos, versionado, términos, integridad y compliance con EU AI Act. Incluye validación de sistemas prohibidos (Art. 5) y checklist completo por tipo de modelo (Art. 11, 15).

---

## 🎯 Responsabilidades

- Ejecutar checklist completo de validación
- Validar sistemas prohibidos (Art. 5, Anexo II)
- Validar modelo completo por tipo (OpenAI, Open Source, Interno)
- Verificar términos específicos de OpenAI
- Validar compliance con EU AI Act

---

## 📚 API Pública

### `executeValidationChecklist(Long modelId)`

Ejecuta checklist completo de validación.

**Categorías validadas:**
- `metadata`: Metadatos básicos (nombre, tipo, versión, estado)
- `versioning`: Versionado según SemVer
- `terms`: Términos y licencias (especialmente OpenAI)
- `integrity`: Integridad (hash, checksum)
- `compliance`: Compliance con EU AI Act (riesgo, FRIA)

**Retorna:** `Map<String, Object>` con:
- Resultados por categoría (errors, warnings, isValid, score)
- `overallScore`: Score global (0.0 - 1.0)
- `isValid`: true si overallScore >= 0.80
- `validatedAt`: Timestamp

**Ejemplo:**
```java
Map<String, Object> results = modelValidationService.executeValidationChecklist(modelId);
double score = (Double) results.get("overallScore");
boolean isValid = (Boolean) results.get("isValid");
```

---

### `verifyOpenAITerms(Long modelId)`

Verifica términos específicos de OpenAI.

**Retorna:** `boolean` - true si cumple con términos de OpenAI

---

### `validateProhibitedSystems(Model model)`

Valida que el proyecto asociado al modelo no sea un sistema prohibido según Art. 5.

**Parámetros:**
- `model`: Modelo a validar

**Retorna:** `Map<String, Object>` con estructura estándar de validación

**Artículo EU AI Act:** Art. 5, Anexo II

**Prompt:** INC-005

---

### `validateProhibitedSystems(Project project)`

Valida un proyecto directamente contra sistemas prohibidos.

**Parámetros:**
- `project`: Proyecto a validar

**Retorna:** `Map<String, Object>` con estructura estándar de validación

---

### `validateModelComplete(Long modelId, ModelType type)`

Valida modelo completo según tipo (INC-011).

**Parámetros:**
- `modelId`: ID del modelo a validar
- `type`: Tipo de modelo (OPENAI, OPEN_SOURCE, INTERNAL)

**Retorna:** `ModelValidationResult` con:
- `modelId`: ID del modelo
- `modelType`: Tipo de modelo
- `failures`: Lista de failures encontrados
- `warnings`: Lista de warnings encontrados
- `finalScore`: Score final (0.00 - 1.00)
- `pass`: true si pasa la validación (score >= 0.80 y sin failures)

**Validaciones incluidas:**
- Checklist común (nombre, tipo, versión, estado, creador)
- Checklist específico por tipo
- Documentación técnica (Anexo IV, Art. 11)
- Robustez adversarial (Art. 15)

**Artículo EU AI Act:** Art. 11, Art. 15

**Prompt:** INC-011

**Ejemplo:**
```java
ModelValidationResult result =
    modelValidationService.validateModelComplete(modelId, ModelType.INTERNAL);

if (!result.isPass()) {
    // Mostrar failures y warnings
    for (ValidationIssue failure : result.getFailures()) {
        log.error("Failure: {} - {}", failure.getCode(), failure.getMessage());
    }
}
```

---

## 📖 Referencias

- **Prompts:** INC-005, INC-011, INC-015
- **Artículos EU AI Act:** Art. 5, Art. 11, Art. 15
- **ViewModels:** ModelsOverviewViewModel, ModelsDetailViewModel, ModelApprovalWorkflowViewModel, HighRiskClassifierViewModel
- **Servicios Integrados:**
  - `ProhibitedSystemBusinessService` (INC-005)
  - `TechnicalDocumentationBusinessService` (INC-011)
  - `AdversarialEvaluationService` (INC-011)

---

**Última actualización:** 25 de noviembre de 2025
