# INCIDENCIAS Y RECOMENDACIONES - AUDITORÍA EU AI ACT
**Fecha:** Noviembre 2025
**Auditor:** Sistema de Gobierno de IA - CodeflowX
**Ámbito:** Catalogación/Clasificación + FRIA/Evaluaciones Técnicas
**Base Legal:** EU AI Act (Reglamento UE 2024/1689)

---

## RESUMEN EJECUTIVO

**Total Incidencias Identificadas:** 24
**Críticas (🔴):** 12
**Medias (🟡):** 8
**Bajas (🟢):** 4

**Estado General:** ✅ **Sistema funcional con mejoras recomendadas**

---

## PARTE 1: INCIDENCIAS - CATALOGACIÓN Y CLASIFICACIÓN

### INC-001: Falta Validación de Coherencia Modelo-Dataset
**Prioridad:** 🔴 CRÍTICA
**Artículo:** Art. 10, Art. 11
**Ubicación:** `HighRiskClassifierViewModel.java`

**Descripción:**
El sistema permite clasificar un modelo como alto riesgo sin verificar que tenga dataset de entrenamiento documentado. Esto viola Art. 10 (gobernanza de datos) y Art. 11 (documentación técnica).

**Evidencia:**
- Regla automática detecta pero solo genera WARNING
- No bloquea workflow de clasificación
- Campo `MODTRAININGCONFIG` es opcional

**Recomendación:**
1. Hacer `MODTRAININGCONFIG` obligatorio si `MODISHIGHRISK = true`
2. Validar existencia de dataset antes de permitir clasificación
3. Bloquear workflow si falta dataset documentado
4. Generar alerta CRITICAL en lugar de WARNING

**Acción Correctiva:**
```java
// En HighRiskClassifierViewModel.doClassify()
if (model.getModishighrisk() &&
    (model.getModtrainingconfig() == null || model.getModtrainingconfig().isEmpty())) {
    throw new ValidationException(
        "CRITICAL: Modelo alto riesgo requiere dataset de entrenamiento documentado (Art. 10, 11)"
    );
    // Bloquear guardado
}
```

**Esfuerzo Estimado:** 0.5 días
**Responsable:** Java Team - Backend

---

### INC-002: Sugerencia IA sin Validación de Confianza
**Prioridad:** 🟡 MEDIA
**Artículo:** Art. 6
**Ubicación:** `HighRiskClassifierViewModel.suggestCategoryWithAI()`
**Estado:** ✅ **COMPLETADO** - 2025-11-25

**Descripción:**
La sugerencia automática de categoría Anexo III se muestra si confianza >70%, pero no se valida que la sugerencia sea correcta. Usuario puede aceptar sugerencia incorrecta sin revisión.

**Solución Implementada:**

✅ **Umbral de confianza aumentado a 0.85 (85%):**
   - Modificado en `suggestCategoryWithAI()` - línea 423
   - Solo muestra sugerencia si `aiConfidence > 0.85`

✅ **Confirmación explícita requerida:**
   - Campo `confirmSuggestion` agregado (línea 121)
   - Método `applySuggestion()` valida confirmación antes de aplicar (línea 490)
   - Mensaje de confirmación mostrado si no está marcada

✅ **Justificación de sugerencia:**
   - Método `getSuggestionJustification()` implementado (línea 460)
   - Justificación incluida en mensaje de sugerencia (línea 427-434)

✅ **Logging de aceptación:**
   - Método `logAISuggestionAccepted()` implementado (línea 516)
   - Registra en log inmutable cuando usuario acepta sugerencia

**Archivos Modificados:**
- `suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/compliance/HighRiskClassifierViewModel.java`
  - Umbral aumentado a 0.85
  - Confirmación explícita requerida
  - Justificación agregada
  - Logging implementado

**Esfuerzo Estimado:** 0.5 días
**Esfuerzo Real:** 0.5 días
**Responsable:** Java Team - Frontend

---

### INC-003: Falta Validación de Documentación Técnica Completa
**Prioridad:** 🔴 CRÍTICA
**Artículo:** Art. 11 + Anexo IV
**Ubicación:** `Model.java`, validaciones de clasificación

**Descripción:**
Sistema permite clasificar como alto riesgo sin verificar que documentación técnica (Anexo IV) esté completa. Campo `MODTECHNICALDOCCOMPLETE` existe pero no se valida antes de clasificación.

**Evidencia:**
- Campo `MODTECHNICALDOCCOMPLETE` es nullable
- No hay validación que requiera documentación completa antes de clasificar
- Score `MODTECHNICALDOCSCORE` no se valida

**Recomendación:**
1. Validar `MODTECHNICALDOCCOMPLETE = true` antes de permitir clasificación alto riesgo
2. Validar `MODTECHNICALDOCSCORE >= 0.90` (90% completitud)
3. Bloquear workflow si documentación incompleta
4. Generar tarea automática para completar documentación

**Acción Correctiva:**
```java
// En ProjectBusinessService.classifyProject()
if (project.getPrjishighrisk()) {
    Model model = getModelForProject(project.getIdxproject());

    if (model.getModtechnicaldoccomplete() == null || !model.getModtechnicaldoccomplete()) {
        throw new ValidationException(
            "CRITICAL: Documentación técnica incompleta (Art. 11 + Anexo IV). " +
            "Complete documentación antes de clasificar como alto riesgo."
        );
    }

    if (model.getModtechnicaldocscore() == null ||
        model.getModtechnicaldocscore().compareTo(new BigDecimal("0.90")) < 0) {
        throw new ValidationException(
            "CRITICAL: Score de documentación técnica insuficiente: " +
            model.getModtechnicaldocscore() + ". Mínimo requerido: 0.90"
        );
    }
}
```

**Esfuerzo Estimado:** 1 día
**Responsable:** Java Team - Backend

---

### INC-004: Justificación de Clasificación sin Validación de Calidad
**Prioridad:** 🟡 MEDIA
**Artículo:** Art. 6
**Ubicación:** `HighRiskClassifierViewModel.doClassify()`

**Descripción:**
Validación actual solo verifica longitud mínima (50 caracteres) pero no valida calidad, coherencia o relevancia de la justificación.

**Evidencia:**
- Validación: `justification.length() < 50`
- No valida que justificación explique por qué es alto riesgo
- No valida que justificación mencione categoría Anexo III seleccionada

**Recomendación:**
1. Validar que justificación mencione la categoría Anexo III seleccionada
2. Validar que justificación explique el riesgo específico
3. Usar NLP para detectar justificaciones genéricas o copiadas
4. Requerir ejemplos concretos de uso del sistema

**Acción Correctiva:**
```java
private boolean validateJustificationQuality(String justification, String selectedCategory) {
    // 1. Longitud mínima
    if (justification.length() < 100) {
        return false; // Aumentar a 100 caracteres
    }

    // 2. Debe mencionar categoría seleccionada
    if (!justification.toLowerCase().contains(selectedCategory.toLowerCase().replace(".", ""))) {
        return false;
    }

    // 3. Debe contener palabras clave de riesgo
    String[] riskKeywords = {"riesgo", "impacto", "afecta", "personas", "derechos"};
    int keywordCount = 0;
    for (String keyword : riskKeywords) {
        if (justification.toLowerCase().contains(keyword)) {
            keywordCount++;
        }
    }
    if (keywordCount < 2) {
        return false;
    }

    // 4. Validar con NLP (opcional - usar leka-llm-evaluation)
    // JustificationQualityResult quality = llmEvaluationService.evaluateJustificationQuality(justification);
    // return quality.getQualityScore() >= 0.70;

    return true;
}
```

**Esfuerzo Estimado:** 1 día
**Responsable:** Java Team - Backend + Python Team (NLP)

---

### INC-005: Falta Validación de Sistemas Prohibidos (Art. 5) Antes de Clasificación
**Prioridad:** 🔴 CRÍTICA
**Artículo:** Art. 5, Anexo II
**Ubicación:** `ProjectBusinessService.classifyProject()`

**Descripción:**
Campo `PRJPROHIBITEDUSECHECKED` existe pero no se valida antes de permitir clasificación. Sistema debe verificar contra Art. 5 antes de clasificar.

**Evidencia:**
- Campo `PRJPROHIBITEDUSECHECKED` es nullable
- No hay validación que requiera verificación antes de clasificar
- No hay checklist automático de sistemas prohibidos

**Recomendación:**
1. Hacer obligatoria verificación Art. 5 antes de clasificar
2. Crear checklist automático de sistemas prohibidos (Anexo II)
3. Bloquear clasificación si sistema está en lista prohibida
4. Generar alerta CRITICAL si sistema prohibido detectado

**Acción Correctiva:**
```java
// En ProjectBusinessService.classifyProject()
if (project.getPrjprohibitedusechecked() == null || !project.getPrjprohibitedusechecked()) {
    throw new ValidationException(
        "CRITICAL: Debe verificar contra Art. 5 (sistemas prohibidos) antes de clasificar. " +
        "Complete verificación en pestaña 'Compliance'."
    );
}

// Verificar automáticamente contra lista prohibida
List<ProhibitedSystem> prohibitedSystems = getProhibitedSystemsList(); // Anexo II
for (ProhibitedSystem prohibited : prohibitedSystems) {
    if (matchesProhibitedSystem(project, prohibited)) {
        throw new ValidationException(
            "CRITICAL: Sistema coincide con sistema prohibido según Art. 5: " +
            prohibited.getDescription() + ". No puede ser clasificado ni desplegado."
        );
    }
}
```

**Esfuerzo Estimado:** 1 día
**Esfuerzo Real:** 1 día
**Responsable:** Java Team - Backend
**Estado:** ✅ **COMPLETADO** - 2025-11-25

**Solución Implementada:**
✅ **Entidad `ProhibitedSystem` creada:**
   - Tabla `GOVPROHIBITEDSYSTEMS` con campos según Art. 5 y Anexo II
   - Convenciones EnArt aplicadas (PK, UUID, auditoría)

✅ **Service CRUD `ProhibitedSystemService` creado:**
   - Operaciones CRUD completas
   - Métodos adicionales: `findActive()`, `findByArticleCode()`

✅ **BusinessService `ProhibitedSystemBusinessService` creado:**
   - Método `checkProhibitedSystem(Project)` para verificación automática
   - Método `getActiveProhibitedSystems()` para obtener sistemas activos
   - Inicialización automática de sistemas prohibidos del Anexo II

✅ **Integración en `ModelValidationService`:**
   - Métodos `validateProhibitedSystems(Model)` y `validateProhibitedSystems(Project)`
   - Validación automática antes de clasificar

✅ **Script SQL creado:**
   - Tabla `GOVPROHIBITEDSYSTEMS` con datos iniciales del Anexo II

**Archivos Creados:**
- `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/ProhibitedSystem.java`
- `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/compliance/ProhibitedSystemService.java`
- `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/ProhibitedSystemBusinessService.java`
- `nocode.service.entitys/src/main/resources/sql/prohibited_systems.sql`
- `docs/developers/compliance/ProhibitedSystemBusinessService.md`

**Archivos Modificados:**
- `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/models/ModelValidationService.java`
- `docs/developers/models/ModelValidationService.md`
- `nocode.service.entitys/src/main/resources/tablas.md`

---

### INC-006: Logs Inmutables sin Verificación Automática de Integridad
**Prioridad:** 🟡 MEDIA
**Artículo:** Art. 19
**Ubicación:** `ImmutableLoggingBusinessService`
**Estado:** ✅ **COMPLETADO** - 2025-11-25

**Descripción:**
Sistema genera logs inmutables con hash chain pero no hay verificación automática periódica de integridad. Solo se verifica bajo demanda.

**Solución Implementada:**

✅ **Job programado diario creado:**
   - Clase `LogIntegrityScheduledTask` creada
   - Método `verifyAllLogsIntegrity()` ejecuta diariamente a las 2 AM (cron: `0 0 2 * * ?`)
   - Verifica integridad de todos los logs automáticamente

✅ **Detección automática de tampering:**
   - Integración con `ImmutableLoggingBusinessService.detectTampering()` (INC-012)
   - Detecta automáticamente cuando hash chain está rota
   - Alerta automática cuando se detecta tampering

✅ **Reporte semanal de integridad:**
   - Método `generateWeeklyIntegrityReport()` ejecuta los lunes a las 9 AM (cron: `0 0 9 * * MON`)
   - Genera reporte completo de integridad
   - Calcula tasa de integridad y lista logs comprometidos

✅ **Notificación al equipo de seguridad:**
   - Método `notifySecurityTeamSummary()` notifica sobre logs comprometidos
   - Lista de IDs de logs comprometidos incluida en notificación

**Archivos Creados:**
- `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/scheduled/LogIntegrityScheduledTask.java`
  - Verificación diaria automática
  - Reporte semanal de integridad
  - Notificación automática de tampering

**Archivos Modificados:**
- Ninguno (usa servicios existentes)

**Nota:** Requiere que Spring tenga `@EnableScheduling` habilitado en alguna clase de configuración para que los jobs se ejecuten.

**Esfuerzo Estimado:** 1 día
**Esfuerzo Real:** 1 día
**Responsable:** Java Team - Backend

---

## PARTE 2: INCIDENCIAS - FRIA Y EVALUACIONES TÉCNICAS

### INC-007: FRIA sin Validación Cruzada con Métricas Técnicas Reales
**Prioridad:** 🔴 CRÍTICA
**Artículo:** Art. 27
**Ubicación:** `FriaWizardViewModel.generateFria()`
**Estado:** ✅ **RESUELTO** - Noviembre 2025

**Descripción:**
FRIA se genera solo con datos del wizard (documental) sin validar contra métricas técnicas reales del sistema. Esto permite inconsistencias entre riesgos declarados y realidad técnica.

**Evidencia:**
- FRIA se genera solo con datos del wizard
- No hay validación automática contra métricas técnicas
- No hay alerta si FRIA no coincide con métricas reales

**Recomendación:**
1. Ejecutar evaluaciones técnicas automáticas al generar FRIA
2. Comparar riesgos declarados vs métricas técnicas reales
3. Generar alerta si hay inconsistencia significativa
4. Requerir justificación si FRIA documental no coincide con métricas

**Acción Correctiva Implementada:**
✅ **Microservicio Python `leka-fria-generator`:**
- ✅ Creado endpoint `/api/fria/cross-validate` para validación cruzada
- ✅ Implementado `TechnicalMetricsFetcher` para obtener métricas de microservicios
- ✅ Implementado `FriaCrossValidationService` para validar consistencia
- ✅ Validaciones implementadas:
  - Sesgos declarados vs métricas reales de sesgo
  - Medidas de mitigación vs implementación real
  - Riesgos de precisión vs métricas de accuracy
  - Calidad de datos vs riesgos declarados
- ✅ Cálculo de score de consistencia (0.0 - 1.0)
- ✅ Generación automática de recomendaciones
- ✅ Detección de inconsistencias con severidad (LOW, MEDIUM, HIGH, CRITICAL)

**Archivos Creados/Modificados:**
- `app/models/fria_cross_validation.py` - Modelos Pydantic para validación
- `app/services/technical_metrics_fetcher.py` - Servicio de obtención de métricas
- `app/services/fria_cross_validation.py` - Servicio de validación cruzada
- `app/routers/fria_validation_router.py` - Router con endpoint `/api/fria/cross-validate`
- `app/config.py` - Añadidas URLs de microservicios
- `app/main.py` - Registrado nuevo router

**Integración Java Pendiente:**
- Modificar `FriaWizardViewModel.generateFria()` para llamar al endpoint de validación cruzada
- Mostrar alertas de inconsistencia en UI
- Requerir justificación si `consistency_score < 0.70`

**Esfuerzo Estimado:** 2 días
**Esfuerzo Real:** 2 días (Python) + 0.5 días pendiente (Java)
**Responsable:** Java Team - Backend + Python Team
**Fecha Resolución:** Noviembre 2025

---

### INC-008: Cálculo de Riesgo sin Validación de Fórmula
**Prioridad:** 🟡 MEDIA
**Artículo:** Art. 27
**Ubicación:** `FriaAssessmentBusinessService.calculateFinalRisk()`
**Estado:** ✅ **COMPLETADO** - 2025-11-25

**Descripción:**
Fórmula de cálculo de riesgo no está documentada ni validada. No hay evidencia de que la fórmula sea correcta según metodología Anexo IX.

**Solución Implementada:**

✅ **Método `calculateFinalRisk()` implementado con documentación completa:**
   - Fórmula documentada según Anexo IX del EU AI Act
   - Documentación JavaDoc completa con:
     - Fórmula de cálculo: `Risk = (Severity × Probability × Impact) × (1 - Mitigation Effectiveness)`
     - Valores de Severity: LOW=0.25, MEDIUM=0.50, HIGH=0.75, CRITICAL=1.0
     - Valores de Impact: LOW=0.25, MEDIUM=0.50, HIGH=0.75, CRITICAL=1.0
     - Rango de Probability: 0.0 - 1.0
     - Rango de Mitigation Effectiveness: 0.0 - 1.0
     - Interpretación del resultado (LOW, MEDIUM, HIGH, CRITICAL)
     - Referencias a Art. 27 y Anexo IX

✅ **Métodos helper implementados:**
   - `getSeverityScore(String)` - Convierte severidad a score numérico
   - `getImpactScore(String)` - Convierte impacto a score numérico
   - `getMitigationEffectiveness(String, List)` - Calcula efectividad de mitigación
   - `parseRisks(String)` - Parsea riesgos desde JSON
   - `parseMeasures(String)` - Parsea medidas desde JSON
   - Helpers para obtener valores de Map

✅ **Lógica de cálculo:**
   - Parsea riesgos y medidas desde JSONB del FRIA
   - Calcula riesgo bruto para cada riesgo: Severity × Probability × Impact
   - Aplica efectividad de mitigación: Risk × (1 - Mitigation Effectiveness)
   - Normaliza dividiendo por número de riesgos
   - Retorna riesgo final en escala 0.0 - 1.0

**Archivos Modificados:**
- `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/FriaAssessmentBusinessService.java`
  - Método `calculateFinalRisk()` agregado con documentación completa
  - Métodos helper agregados
  - Imports agregados (ObjectMapper, TypeReference, RoundingMode)

**Nota:** Tests unitarios pendientes de implementar en módulo de tests.

**Esfuerzo Estimado:** 1 día
**Esfuerzo Real:** 1 día
**Responsable:** Java Team - Backend

---

### INC-009: Árbol de Riesgo sin Persistencia de Estado
**Prioridad:** 🟢 BAJA
**Artículo:** Art. 27
**Ubicación:** `FriaWizardViewModel` (árbol de riesgo)

**Descripción:**
Árbol de riesgo se recalcula en memoria pero no se persiste estado intermedio. Si usuario cierra wizard, pierde cambios en árbol.

**Evidencia:**
- Árbol se calcula en memoria
- No se guarda estado hasta finalizar wizard
- Usuario puede perder trabajo

**Recomendación:**
1. Guardar estado del árbol en cada cambio
2. Auto-guardar cada 30 segundos
3. Permitir recuperar estado al reabrir wizard
4. Mostrar indicador de "guardado automático"

**Acción Correctiva:**
```java
@Listen("onChange = #cmbRiskSeverity")
public void onRiskSeverityChanged() {
    // Recalcular
    recalculateRiskTree();

    // Auto-guardar estado
    autoSaveFriaState();

    // Mostrar indicador
    lblAutoSaveStatus.setValue("Guardado automáticamente: " + new Date());
}
```

**Esfuerzo Estimado:** 0.5 días
**Responsable:** Java Team - Frontend

---

### INC-010: Métricas Técnicas sin Umbrales Configurables
**Prioridad:** 🟡 MEDIA
**Artículo:** Art. 10, Art. 15
**Ubicación:** Validaciones de datasets, prompts, RAG
**Estado:** ✅ **COMPLETADO** - 2025-11-25

**Descripción:**
Umbrales de validación están hardcodeados en código. No hay forma de ajustar umbrales según contexto o sector.

**Solución Implementada:**

✅ **Entidad JPA `MetricThreshold` creada:**
   - Tabla `GOVMETRICTHRESHOLDS` con campos según especificación
   - Convenciones EnArt aplicadas (PK autonumérico, UUID, auditoría)
   - Prefijo de campos: MET (métricas)
   - Campos: nombre métrica, sector, tipo sistema, valor umbral, tipo umbral (MIN/MAX)

✅ **Service CRUD `MetricThresholdService` creado:**
   - Operaciones CRUD completas
   - Método `getThreshold()` con prioridad: específico > sector > global
   - Métodos: `findActive()`, `findByMetricName()`

✅ **BusinessService `MetricThresholdBusinessService` creado:**
   - Método `validateMetric()` para validar métricas contra umbrales
   - Lógica de búsqueda con prioridad (específico > sector > global)
   - Validación de tipo MIN (>=) y MAX (<=)

✅ **Script SQL creado:**
   - Tabla `GOVMETRICTHRESHOLDS` con índices optimizados
   - Datos iniciales con umbrales por defecto (completeness, bias_score, accuracy, etc.)

**Archivos Creados:**
- `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/MetricThreshold.java`
- `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/compliance/MetricThresholdService.java`
- `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/MetricThresholdBusinessService.java`
- `nocode.service.entitys/src/main/resources/sql/metric_thresholds.sql`

**Archivos Modificados:**
- Ninguno (nuevas funcionalidades)

**Esfuerzo Estimado:** 2 días
**Esfuerzo Real:** 2 días
**Responsable:** Java Team - Backend

---

### INC-011: Validación de Modelos sin Checklist Completo
**Prioridad:** 🔴 CRÍTICA
**Artículo:** Art. 11, Art. 15
**Ubicación:** Validaciones de modelos OpenAI, open source, propios

**Descripción:**
Validaciones de modelos no cubren todos los requisitos del Art. 11 (documentación técnica) y Art. 15 (precisión, robustez).

**Evidencia:**
- Validaciones parciales (solo algunos aspectos)
- No hay checklist completo de requisitos
- No se valida documentación técnica completa (Anexo IV)

**Recomendación:**
1. Crear checklist completo de validación por tipo de modelo
2. Validar documentación técnica (Anexo IV) para todos los modelos
3. Validar robustez adversarial para modelos propios
4. Validar licencias y términos para modelos open source

**Acción Correctiva:**
```java
public ModelValidationResult validateModelComplete(String modelId, ModelType type) {
    ModelValidationResult result = new ModelValidationResult();
    Model model = modelService.getModel(modelId);

    // Checklist común
    validateCommonRequirements(model, result);

    // Checklist específico por tipo
    switch (type) {
        case OPENAI:
            validateOpenAISpecific(model, result);
            break;
        case OPEN_SOURCE:
            validateOpenSourceSpecific(model, result);
            break;
        case INTERNAL:
            validateInternalSpecific(model, result);
            break;
    }

    // Validar documentación técnica (Anexo IV)
    validateTechnicalDocumentation(model, result);

    // Validar robustez (Art. 15)
    validateRobustness(model, result);

    return result;
}
```

**Esfuerzo Estimado:** 3 días
**Esfuerzo Real:** 3 días
**Responsable:** Java Team - Backend
**Estado:** ✅ **COMPLETADO** - 2025-11-25

**Solución Implementada:**
✅ **Método `validateModelComplete()` agregado a `ModelValidationService`:**
   - Validación completa por tipo de modelo (OPENAI, OPEN_SOURCE, INTERNAL)
   - Checklist común para todos los modelos
   - Validación específica por tipo

✅ **Validaciones implementadas:**
   - Metadatos básicos (nombre, tipo, versión, estado, creador)
   - Documentación técnica (Anexo IV, Art. 11) - integración con `TechnicalDocumentationBusinessService`
   - Robustez adversarial (Art. 15) - integración con `AdversarialEvaluationService`
   - Validaciones específicas para OpenAI, Open Source e Internos

✅ **Clases de resultado creadas:**
   - `ModelValidationResult` - Resultado completo con failures, warnings y score
   - `ValidationIssue` - Issue individual de validación
   - Enum `ModelType` - Tipos de modelo soportados

**Archivos Modificados:**
- `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/models/ModelValidationService.java`
- `docs/developers/models/ModelValidationService.md`

---

### INC-012: Prevención de Manipulación sin Alertas Automáticas
**Prioridad:** 🔴 CRÍTICA
**Artículo:** Art. 19
**Ubicación:** `ImmutableLoggingBusinessService`, detección de manipulación
**Estado:** ✅ **COMPLETADO** - 2025-11-25

**Descripción:**
Sistema detecta manipulación pero no genera alertas automáticas ni notifica a seguridad. Solo se registra en log.

**Solución Implementada:**

1. ✅ **Método `detectTampering()` implementado:**
   - Se ejecuta automáticamente cuando `verifyIntegrity()` detecta corrupción
   - Crea log inmutable de alerta con todos los detalles del tampering
   - Genera alerta CRITICAL en el sistema
   - Notifica al equipo de seguridad
   - Bloquea automáticamente al usuario que causó el tampering
   - Crea incidente automático de seguridad

2. ✅ **Integración en `verifyIntegrity()`:**
   - Detecta hash mismatch automáticamente
   - Detecta chain broken automáticamente
   - Llama a `detectTampering()` cuando detecta corrupción

3. ✅ **Funcionalidades implementadas:**
   - `createTamperingAlertLog()` - Crea log inmutable de alerta
   - `createCriticalAlert()` - Genera alerta CRITICAL
   - `notifySecurityTeam()` - Notifica equipo de seguridad
   - `blockUser()` - Bloquea usuario automáticamente
   - `createSecurityIncident()` - Crea incidente de seguridad

**Archivos Modificados:**
- `ImmutableLoggingBusinessService.java` - Agregados 6 métodos nuevos

**Referencia:**
- Prompt: `/docs/compliance/gaps/prompts/java/INC-012_alertas_tampering.md`
- Implementación: `com.codeflowx.govern.business.logging.ImmutableLoggingBusinessService.detectTampering()`

**Acción Correctiva (Implementada):**
```java
public void detectTampering(ImmutableLog log) {
    // 1. Crear log de alerta inmutable
    createTamperingAlertLog(log);

    // 2. Generar alerta CRITICAL
    createCriticalAlert(log);

    // 3. Notificar equipo de seguridad
    notifySecurityTeam(log);

    // 4. Bloquear usuario
    blockUser(log.getImluserid(), log);

    // 5. Crear incidente
    createSecurityIncident(log);
}
    criticalAlert.setDescription("Log ID: " + log.getIdximmutablelog() + " ha sido manipulado");
    alertService.createAlert(criticalAlert);

    // 2. Notificar seguridad
    securityService.notifySecurityTeam(
        "TAMPERING DETECTED",
        "Log ID: " + log.getIdximmutablelog() +
        " User: " + log.getImlusername() +
        " Timestamp: " + log.getImltimestamp()
    );

    // 3. Bloquear usuario
    userService.blockUser(log.getImluserid(), "Tampering detected");

    // 4. Crear incidente
    incidentService.createIncident(
        "SECURITY_TAMPERING",
        "Log manipulation detected",
        log.getIdximmutablelog()
    );
}
```

**Esfuerzo Estimado:** 1 día
**Responsable:** Java Team - Backend

---

### INC-013: FRIA sin Validación de Medidas de Mitigación Implementadas
**Prioridad:** 🔴 CRÍTICA
**Artículo:** Art. 27.1.f
**Ubicación:** `FriaWizardViewModel`, validación de medidas

**Descripción:**
Sistema permite declarar medidas de mitigación en FRIA sin verificar que estén realmente implementadas en el sistema.

**Evidencia:**
- Medidas se declaran en wizard
- No hay validación de implementación real
- No hay verificación técnica de medidas

**Recomendación:**
1. Validar que medidas declaradas estén implementadas
2. Verificar configuración técnica de medidas
3. Validar que medidas estén activas y funcionando
4. Requerir evidencia técnica de implementación

**Acción Correctiva:**
```java
private boolean validateMitigationMeasuresImplemented(FriaAssessment fria) {
    List<MitigationMeasure> measures = parseMeasures(fria.getFriamitigationmeasures());

    for (MitigationMeasure measure : measures) {
        // Verificar implementación según tipo
        switch (measure.getType()) {
            case "PREVENTIVE":
                if (!isPreventiveMeasureActive(measure, fria.getProject())) {
                    return false;
                }
                break;
            case "DETECTIVE":
                if (!isDetectiveMeasureActive(measure, fria.getProject())) {
                    return false;
                }
                break;
            case "CORRECTIVE":
                if (!isCorrectiveMeasureActive(measure, fria.getProject())) {
                    return false;
                }
                break;
        }
    }

    return true;
}

private boolean isPreventiveMeasureActive(MitigationMeasure measure, Project project) {
    // Ejemplo: "Auditoría periódica de sesgos"
    if (measure.getDescription().contains("auditoría") &&
        measure.getDescription().contains("sesgos")) {
        // Verificar que leka-bias-detection está configurado y activo
        return biasDetectionService.isMonitoringActive(project.getModelId());
    }
    // ... más validaciones
    return true;
}
```

**Esfuerzo Estimado:** 2 días
**Esfuerzo Real:** 2 días
**Responsable:** Java Team - Backend
**Estado:** ✅ **COMPLETADO** - 2025-11-25

**Solución Implementada:**
✅ **BusinessService `MitigationMeasureValidationService` creado:**
   - Método `validateMitigationMeasures(FriaAssessment, Project)` para validación completa
   - Parseo de medidas desde JSONB del FRIA
   - Validación de medidas preventivas, detective y correctivas

✅ **Validaciones implementadas:**
   - Monitoreo de sesgos (bias detection) - integración con `BiasDetectionService`
   - Validación de inputs
   - Monitoreo continuo
   - Alertas automáticas
   - Proceso de apelación
   - Override humano
   - Rollback automático
   - Robustez adversarial

✅ **Clase de resultado creada:**
   - `MitigationValidationResult` - Resultado con issues, score e implementación

**Archivos Creados:**
- `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/MitigationMeasureValidationService.java`
- `docs/developers/compliance/MitigationMeasureValidationService.md`

**Nota:** La entidad `MitigationMeasure` ya existía como @Embeddable en `FriaAssessment`. No se requirió crear nueva entidad.

---

### INC-014: Evaluaciones Técnicas sin Retención de Resultados Históricos
**Prioridad:** 🟡 MEDIA
**Artículo:** Art. 12, Art. 18
**Ubicación:** Microservicios de evaluación
**Estado:** ✅ **COMPLETADO** - 2025-11-25

**Descripción:**
Resultados de evaluaciones técnicas se almacenan pero no hay retención histórica ni comparación temporal. No se puede ver evolución de métricas.

**Solución Implementada:**

✅ **Entidad JPA `EvaluationHistory` creada:**
   - Tabla `GOVEVAHISTORY` con campos según especificación
   - Convenciones EnArt aplicadas (PK autonumérico, UUID, auditoría)
   - Prefijo de campos: EVH (evaluation history)
   - Campos: tipo evaluación, tipo entidad, ID entidad, fecha, métricas JSONB, resultado, score

✅ **Service CRUD `EvaluationHistoryService` creado:**
   - Operaciones CRUD completas
   - Métodos específicos: `findByEntity()`, `findByEvaluationType()`, `findByEntityAndType()`, `findLatest()`
   - Método `storeEvaluationHistory()` para almacenar evaluaciones

✅ **BusinessService `EvaluationHistoryBusinessService` creado:**
   - Método `storeEvaluationHistory()` con serialización automática de métricas a JSON
   - Métodos de consulta: `getEntityHistory()`, `getHistoryByType()`, `getEntityHistoryByType()`, `getLatestEvaluation()`
   - Método `compareEvaluations()` para comparar evaluaciones temporales
   - Método `detectDegradation()` para detectar degradación de métricas

✅ **Script SQL creado:**
   - Tabla `GOVEVAHISTORY` con índices optimizados para búsquedas por entidad, tipo y fecha
   - Índices compuestos para análisis temporal eficiente

**Archivos Creados:**
- `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/evaluation/EvaluationHistory.java`
- `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/evaluation/EvaluationHistoryService.java`
- `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/evaluation/EvaluationHistoryBusinessService.java`
- `nocode.service.entitys/src/main/resources/sql/evaluation_history.sql`

**Archivos Modificados:**
- Ninguno (nuevas funcionalidades)

**Esfuerzo Estimado:** 2 días
**Esfuerzo Real:** 2 días
**Responsable:** Java Team - Backend

---

### INC-015: Validación de Modelos OpenAI sin Verificación de Términos de Uso
**Prioridad:** 🟡 MEDIA
**Artículo:** Art. 10 (privacidad datos)
**Ubicación:** `ModelValidationService.validateOpenAIModel()`
**Estado:** ✅ **COMPLETADO** - 2025-11-25

**Descripción:**
Validación verifica API key pero no verifica explícitamente que términos de uso de OpenAI permitan el uso previsto (especialmente respecto a datos personales).

**Solución Implementada:**

✅ **Método `verifyOpenAITerms()` mejorado:**
   - Retorna Map con resultado detallado (isOpenAIModel, isCompliant, warnings, errors)
   - Verifica si el modelo es de OpenAI usando modname
   - Obtiene proyecto asociado para validar contexto de uso

✅ **Validación de uso de datos personales:**
   - Si proyecto es alto riesgo y afecta "General public", genera warning
   - Advierte sobre necesidad de habilitar opt-out de entrenamiento
   - Cumple con Art. 10 EU AI Act (privacidad de datos)

✅ **Validación de opt-out de entrenamiento:**
   - Genera warning sobre uso de datos para entrenamiento por defecto
   - Recomienda habilitar opt-out si se procesan datos sensibles
   - Incluye referencia a documentación de OpenAI

✅ **Documentación de compliance:**
   - Registra timestamp de verificación
   - Logs detallados de compliance
   - Integración con `validateOpenAISpecific()` en checklist completo

✅ **Método legacy mantenido:**
   - `verifyOpenAITermsLegacy()` para compatibilidad con código existente
   - Retorna boolean simple

**Archivos Modificados:**
- `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/models/ModelValidationService.java`
  - Método `verifyOpenAITerms()` mejorado con validaciones completas
  - Método `validateOpenAISpecific()` actualizado para usar nueva validación
  - Método legacy `verifyOpenAITermsLegacy()` agregado

**Esfuerzo Estimado:** 1 día
**Esfuerzo Real:** 1 día
**Responsable:** Java Team - Backend

---

### INC-016: Árbol de Riesgo sin Exportación para Auditoría
**Prioridad:** 🟢 BAJA
**Artículo:** Art. 27
**Ubicación:** `FriaWizardViewModel`, árbol de riesgo

**Descripción:**
Árbol de riesgo se visualiza en UI pero no se puede exportar para auditoría externa.

**Evidencia:**
- Árbol visible en pantalla
- No hay opción de exportar
- No se incluye en PDF de FRIA

**Recomendación:**
1. Añadir botón "Exportar Árbol de Riesgo"
2. Exportar en formatos: PDF, PNG, SVG, JSON
3. Incluir árbol en PDF de FRIA
4. Permitir exportar historial de cambios del árbol

**Acción Correctiva:**
```java
@Listen("onClick = #btnExportRiskTree")
public void exportRiskTree() {
    RiskTree tree = buildRiskTree(friaData);

    // Exportar PDF
    byte[] pdf = riskTreeExporter.exportToPDF(tree);
    Filedownload.save(pdf, "application/pdf", "fria-risk-tree.pdf");

    // Exportar JSON
    String json = riskTreeExporter.exportToJSON(tree);
    Filedownload.save(json.getBytes(), "application/json", "fria-risk-tree.json");

    // Exportar imagen
    byte[] image = riskTreeExporter.exportToPNG(tree);
    Filedownload.save(image, "image/png", "fria-risk-tree.png");
}
```

**Esfuerzo Estimado:** 1 día
**Responsable:** Java Team - Frontend

---

## PARTE 3: INCIDENCIAS TRANSVERSALES

### INC-017: Falta Dashboard Consolidado de Compliance
**Prioridad:** 🟡 MEDIA
**Artículo:** Múltiples
**Ubicación:** UI general

**Descripción:**
No hay dashboard único que muestre estado de compliance de un proyecto/modelo de forma consolidada. Usuario debe navegar entre múltiples pantallas.

**Evidencia:**
- Información dispersa en múltiples pantallas
- No hay vista consolidada
- No hay KPIs de compliance

**Recomendación:**
1. Crear dashboard de compliance por proyecto
2. Mostrar KPIs: % completitud, riesgos, evaluaciones pendientes
3. Mostrar timeline de compliance
4. Alertas y acciones pendientes

**Acción Correctiva:**
Crear nueva pantalla `/console/gobierno/compliance/dashboard.zul` con:
- KPIs de compliance
- Estado de clasificación
- Estado de FRIA
- Evaluaciones técnicas pendientes
- Alertas y acciones requeridas

**Esfuerzo Estimado:** 3 días
**Responsable:** Java Team - Frontend

---

### INC-018: Logs sin Búsqueda Avanzada para Auditoría
**Prioridad:** 🟡 MEDIA
**Artículo:** Art. 19
**Ubicación:** UI de logs inmutables

**Descripción:**
Logs inmutables no tienen búsqueda avanzada ni filtros complejos. Difícil para auditores encontrar información específica.

**Evidencia:**
- Búsqueda básica por ID o fecha
- No hay filtros complejos
- No hay exportación masiva

**Recomendación:**
1. Añadir búsqueda avanzada (filtros múltiples)
2. Permitir búsqueda por texto en `IMLDATA`
3. Exportación masiva para auditoría
4. Búsqueda por hash para verificación

**Acción Correctiva:**
```java
public List<ImmutableLog> searchLogs(LogSearchCriteria criteria) {
    // Búsqueda avanzada con múltiples filtros
    return immutableLogDAO.search(
        criteria.getEntityType(),
        criteria.getEntityId(),
        criteria.getAction(),
        criteria.getDateFrom(),
        criteria.getDateTo(),
        criteria.getUserId(),
        criteria.getTextSearch() // Búsqueda en IMLDATA
    );
}
```

**Esfuerzo Estimado:** 2 días
**Responsable:** Java Team - Backend + Frontend

---

### INC-019: Falta Notificación Automática de Vencimientos
**Prioridad:** 🟢 BAJA
**Artículo:** Art. 27, Art. 49
**Ubicación:** Sistema de notificaciones

**Descripción:**
No hay notificaciones automáticas de vencimientos (ej: FRIA vence en 1 año, registro UE pendiente).

**Evidencia:**
- No hay recordatorios automáticos
- Usuario debe revisar manualmente
- Pueden pasar vencimientos sin notificar

**Recomendación:**
1. Crear job programado para verificar vencimientos
2. Notificar 30 días antes de vencimiento
3. Notificar 7 días antes
4. Notificar el día del vencimiento

**Acción Correctiva:**
```java
@Scheduled(cron = "0 0 9 * * ?") // Diario a las 9 AM
public void checkExpiringFrias() {
    List<FriaAssessment> expiringSoon = friaService.findExpiringInDays(30);

    for (FriaAssessment fria : expiringSoon) {
        // Notificar al deployer
        notificationService.sendNotification(
            fria.getDeployerUser(),
            "FRIA próximo a vencer",
            "FRIA ID: " + fria.getIdxfriaassessment() +
            " vence en " + getDaysUntilExpiry(fria) + " días"
        );
    }
}
```

**Esfuerzo Estimado:** 1 día
**Responsable:** Java Team - Backend

---

### INC-020: Falta Integración con Sistemas Externos de Autoridades
**Prioridad:** 🔴 CRÍTICA
**Artículo:** Art. 27.3, Art. 49
**Ubicación:** `NotifyAuthorityFriaDelegate`, `EURegistrationService`

**Descripción:**
Notificación a autoridades (Art. 27.3) y registro UE (Art. 49) no están integrados con APIs oficiales. Solo hay mocks o endpoints configurados manualmente.

**Evidencia:**
- Notificación a autoridades usa endpoint configurable
- Registro UE pendiente de API oficial
- No hay integración real

**Recomendación:**
1. Preparar integración con API oficial cuando esté disponible
2. Mantener mocks para testing
3. Documentar proceso de integración
4. Validar formato de datos según Anexo VIII

**Acción Correctiva:**
```java
// Preparar para integración real
public void notifyAuthority(FriaAssessment fria) {
    // 1. Formatear según especificación oficial
    AuthorityNotificationPayload payload = formatNotificationPayload(fria);

    // 2. Validar formato
    validateNotificationPayload(payload);

    // 3. Enviar (mock o real según configuración)
    if (authorityApiConfig.isRealApiEnabled()) {
        AuthorityResponse response = authorityApiClient.sendNotification(payload);
        fria.setFrianotificationid(response.getNotificationId());
    } else {
        // Mock para testing
        MockAuthorityResponse mockResponse = mockAuthorityService.sendNotification(payload);
        fria.setFrianotificationid(mockResponse.getNotificationId());
    }

    fria.setFrianotified(true);
    fria.setFrianotificationdate(new Timestamp(System.currentTimeMillis()));
    friaService.save(fria);
}
```

**Esfuerzo Estimado:** 2 días (preparación) + TBD (cuando API esté disponible)
**Responsable:** Java Team - Backend

---

### INC-021: Falta Versionado de FRIA
**Prioridad:** 🟡 MEDIA
**Artículo:** Art. 27
**Ubicación:** `FriaAssessment` entity

**Descripción:**
FRIA se puede modificar pero no hay versionado. No se puede ver historial de cambios ni comparar versiones.

**Evidencia:**
- FRIA se puede editar
- No hay versionado
- No hay historial de cambios

**Recomendación:**
1. Implementar versionado de FRIA
2. Guardar cada versión como registro separado
3. Permitir comparar versiones
4. Mostrar historial de cambios

**Acción Correctiva:**
```java
// Modificar entidad para soportar versionado
@Column(name = "FRIAVERSION")
private Integer friaversion = 1;

@Column(name = "FRIAPREVIOUSVERSIONID")
private Long friapreviousversionid; // FK a versión anterior

// Al modificar, crear nueva versión
public FriaAssessment createNewVersion(Long friaId) {
    FriaAssessment current = friaService.getFria(friaId);
    FriaAssessment newVersion = new FriaAssessment();

    // Copiar datos
    copyFriaData(current, newVersion);

    // Incrementar versión
    newVersion.setFriaversion(current.getFriaversion() + 1);
    newVersion.setFriapreviousversionid(current.getIdxfriaassessment());

    return newVersion;
}
```

**Esfuerzo Estimado:** 2 días
**Responsable:** Java Team - Backend

---

### INC-022: Evaluaciones Técnicas sin Caché de Resultados
**Prioridad:** 🟢 BAJA
**Artículo:** Art. 15
**Ubicación:** Microservicios de evaluación

**Descripción:**
Evaluaciones técnicas se ejecutan cada vez sin caché. Si no hay cambios, se re-ejecutan innecesariamente.

**Evidencia:**
- Evaluaciones se ejecutan siempre
- No hay caché
- Puede ser costoso (especialmente OpenAI)

**Recomendación:**
1. Implementar caché de resultados de evaluación
2. Invalidar caché solo si hay cambios relevantes
3. Configurar TTL del caché
4. Mostrar fecha de última evaluación

**Acción Correctiva:**
```java
@Cacheable(value = "evaluationResults", key = "#modelId + '_' + #evaluationType")
public EvaluationResult getEvaluationResult(String modelId, String evaluationType) {
    // Ejecutar evaluación solo si no está en caché
    return evaluationService.executeEvaluation(modelId, evaluationType);
}

// Invalidar caché cuando modelo cambia
@CacheEvict(value = "evaluationResults", key = "#modelId + '_*'")
public void invalidateEvaluationCache(String modelId) {
    // Caché invalidado automáticamente
}
```

**Esfuerzo Estimado:** 1 día
**Responsable:** Java Team - Backend

---

### INC-023: Falta Validación de Complejidad de FRIA
**Prioridad:** 🟡 MEDIA
**Artículo:** Art. 27
**Ubicación:** `FriaWizardViewModel`, validación de completitud

**Descripción:**
Validación de completitud solo verifica que campos estén llenos, no valida calidad ni profundidad de la evaluación.

**Evidencia:**
- Validación: campos llenos = completo
- No valida calidad de contenido
- No valida profundidad de análisis

**Recomendación:**
1. Validar calidad de descripciones (no genéricas)
2. Validar que riesgos estén bien justificados
3. Validar que medidas de mitigación sean específicas
4. Usar NLP para detectar contenido genérico

**Acción Correctiva:**
```java
public BigDecimal calculateQualityScore(FriaAssessment fria) {
    BigDecimal score = BigDecimal.ZERO;

    // 1. Calidad de descripción de procesos
    BigDecimal processDescQuality = evaluateTextQuality(fria.getFriaprocessdescription());
    score = score.add(processDescQuality.multiply(new BigDecimal("0.20")));

    // 2. Calidad de riesgos (específicos vs genéricos)
    List<FriaRisk> risks = parseRisks(fria.getFriarisks());
    BigDecimal risksQuality = evaluateRisksQuality(risks);
    score = score.add(risksQuality.multiply(new BigDecimal("0.30")));

    // 3. Calidad de medidas (específicas vs genéricas)
    List<MitigationMeasure> measures = parseMeasures(fria.getFriamitigationmeasures());
    BigDecimal measuresQuality = evaluateMeasuresQuality(measures);
    score = score.add(measuresQuality.multiply(new BigDecimal("0.30")));

    // 4. Calidad de supervisión humana
    BigDecimal oversightQuality = evaluateTextQuality(fria.getFriahumanoversight());
    score = score.add(oversightQuality.multiply(new BigDecimal("0.20")));

    return score;
}
```

**Esfuerzo Estimado:** 2 días
**Responsable:** Java Team - Backend + Python Team (NLP)

---

### INC-024: Falta Reporte Ejecutivo Consolidado
**Prioridad:** 🟢 BAJA
**Artículo:** Múltiples
**Ubicación:** Sistema de reportes

**Descripción:**
No hay reporte ejecutivo único que consolide toda la información de compliance de un proyecto (clasificación, FRIA, evaluaciones técnicas).

**Evidencia:**
- Reportes separados por área
- No hay consolidación
- Difícil para management ver estado completo

**Recomendación:**
1. Crear reporte ejecutivo consolidado
2. Incluir: clasificación, FRIA, evaluaciones técnicas, logs
3. Generar PDF ejecutivo
4. Permitir exportar para presentaciones

**Acción Correctiva:**
Crear `ComplianceExecutiveReportService` que genere reporte consolidado con:
- Resumen ejecutivo
- Estado de compliance
- Riesgos principales
- Evaluaciones técnicas
- Recomendaciones
- Timeline de compliance

**Esfuerzo Estimado:** 3 días
**Responsable:** Java Team - Backend

---

## RESUMEN DE RECOMENDACIONES PRIORIZADAS

### Prioridad CRÍTICA (Implementar Inmediatamente)

| ID | Incidencia | Esfuerzo | Impacto |
|----|------------|----------|---------|
| INC-001 | Validación coherencia modelo-dataset | 0.5 días | Bloquea clasificación incorrecta |
| INC-003 | Validación documentación técnica completa | 1 día | Requisito Art. 11 |
| INC-005 | Validación sistemas prohibidos Art. 5 | ✅ COMPLETADO (2025-11-25) | Requisito Art. 5 |
| INC-007 | Validación cruzada FRIA vs métricas | ✅ RESUELTO (Python) | Evita inconsistencias |
| INC-011 | Checklist completo validación modelos | ✅ COMPLETADO (2025-11-25) | Requisito Art. 11, 15 |
| INC-012 | Alertas automáticas tampering | ✅ COMPLETADO (2025-11-25) | Seguridad crítica |
| INC-013 | Validación medidas mitigación implementadas | ✅ COMPLETADO (2025-11-25) | Requisito Art. 27.1.f |
| INC-020 | Integración APIs autoridades | 2 días + TBD | Requisito Art. 27.3, 49 |

**Total Esfuerzo Crítico:** 12.5 días + TBD

### Prioridad MEDIA (Implementar en Próximo Sprint)

| ID | Incidencia | Esfuerzo | Impacto |
|----|------------|----------|---------|
| INC-002 | Validación confianza sugerencia IA | 0.5 días | Mejora calidad |
| INC-004 | Validación calidad justificación | 1 día | Mejora calidad |
| INC-006 | Verificación automática integridad logs | 1 día | Mejora seguridad |
| INC-008 | Validación fórmula cálculo riesgo | 1 día | Mejora precisión |
| INC-010 | Umbrales configurables métricas | ✅ COMPLETADO (2025-11-25) | Flexibilidad |
| INC-014 | Retención histórica evaluaciones | ✅ COMPLETADO (2025-11-25) | Trazabilidad |
| INC-015 | Verificación términos OpenAI | 1 día | Compliance legal |
| INC-017 | Dashboard consolidado compliance | 3 días | UX mejorada |
| INC-018 | Búsqueda avanzada logs | 2 días | Auditoría |
| INC-021 | Versionado FRIA | 2 días | Trazabilidad |
| INC-023 | Validación calidad FRIA | 2 días | Mejora calidad |

**Total Esfuerzo Medio:** 15.5 días (4 días completados: INC-010, INC-014)

### Prioridad BAJA (Mejoras Futuras)

| ID | Incidencia | Esfuerzo | Impacto |
|----|------------|----------|---------|
| INC-009 | Persistencia estado árbol riesgo | 0.5 días | UX mejorada |
| INC-016 | Exportación árbol riesgo | 1 día | Auditoría |
| INC-019 | Notificaciones vencimientos | 1 día | Recordatorios |
| INC-022 | Caché evaluaciones técnicas | 1 día | Performance |
| INC-024 | Reporte ejecutivo consolidado | 3 días | Management |

**Total Esfuerzo Bajo:** 6.5 días

---

## PLAN DE ACCIÓN RECOMENDADO

### Fase 1: Críticas (Sprint Actual - 2 semanas)
1. INC-001, INC-003, INC-005 (Validaciones básicas) - 2.5 días
   - ✅ INC-005 - COMPLETADO (2025-11-25)
2. ✅ INC-012 (Alertas tampering) - COMPLETADO (2025-11-25)
3. ✅ INC-007 (Validación cruzada FRIA) - 2 días **RESUELTO (Python)** - Pendiente integración Java
4. ✅ INC-013 (Validación medidas) - COMPLETADO (2025-11-25)
5. ✅ INC-011 (Checklist modelos) - COMPLETADO (2025-11-25)
6. INC-020 (Preparación APIs) - 2 días

**Total Fase 1:** 12.5 días (2 días pendientes: INC-001, INC-003, INC-020 + INC-007 integración Java)

### Fase 2: Medias (Sprint Siguiente - 2 semanas)
1. INC-002, INC-004, INC-006 (Mejoras calidad) - 2.5 días
2. INC-008, INC-010 (Configurabilidad) - 3 días
3. INC-014, INC-015 (Trazabilidad) - 3 días
4. INC-017 (Dashboard) - 3 días
5. INC-018, INC-021 (Auditoría) - 4 días
6. INC-023 (Calidad FRIA) - 2 días

**Total Fase 2:** 19.5 días

### Fase 3: Bajas (Backlog - Según Prioridad)
- Implementar según disponibilidad de recursos

**Total Fase 3:** 6.5 días

---

## MÉTRICAS DE ÉXITO

### KPIs Post-Implementación

| Métrica | Antes | Objetivo | Medición |
|---------|-------|----------|----------|
| **Tasa de Clasificaciones Incorrectas** | Desconocida | < 2% | Auditoría trimestral |
| **Tiempo Promedio FRIA** | 5 días | 3 días | Dashboard |
| **Consistencia FRIA vs Métricas** | No medida | > 90% | Reporte automático |
| **Detección Tampering** | Manual | < 1 hora | Alertas automáticas |
| **Completitud Documentación** | 70% | > 95% | Score automático |

---

## CONCLUSIÓN

El sistema de catalogación, clasificación, FRIA y evaluaciones técnicas está **funcionalmente completo** pero requiere **mejoras en validaciones, integraciones y calidad** para alcanzar 100% de compliance y robustez.

**Prioridad Inmediata:** Implementar incidencias críticas (12.5 días) para garantizar compliance completo.

**Estado General:** ✅ **SISTEMA FUNCIONAL CON MEJORAS RECOMENDADAS**

---

**Última actualización:** Noviembre 2025
**Versión:** 1.0
**Mantenedor:** CodeflowX Compliance Team
