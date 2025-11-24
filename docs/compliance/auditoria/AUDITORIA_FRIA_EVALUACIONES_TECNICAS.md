# AUDITORÍA - FRIA Y EVALUACIONES TÉCNICAS
**Fecha:** Noviembre 2025  
**Auditor:** Sistema de Gobierno de IA - CodeflowX  
**Base Legal:** EU AI Act Art. 27, Art. 11, Art. 15, Anexo IX  
**Artículos Relevantes:** Art. 27 (FRIA), Art. 11 (Documentación Técnica), Art. 15 (Precisión y Robustez)

---

## 1. PROCESO FRIA COMPLETO

### 1.1 Flujo General

El proceso FRIA (Fundamental Rights Impact Assessment) se ejecuta mediante un **Wizard de 6 pasos** que corresponde a los 6 elementos mandatorios del Art. 27.1:

```
PASO 1: Descripción de Procesos (Art. 27.1.a)
    ↓
PASO 2: Período y Frecuencia (Art. 27.1.b)
    ↓
PASO 3: Categorías Personas Afectadas (Art. 27.1.c)
    ↓
PASO 4: Riesgos Específicos (Art. 27.1.d)
    ↓
PASO 5: Supervisión Humana (Art. 27.1.e)
    ↓
PASO 6: Medidas de Mitigación (Art. 27.1.f)
    ↓
GENERACIÓN: Documento FRIA + Cálculo de Riesgo
    ↓
APROBACIÓN: HITL Review (si impacto alto)
    ↓
NOTIFICACIÓN: Autoridad (Art. 27.3)
```

### 1.2 Punto de Entrada

**Pantalla Principal:** `/console/gobierno/compliance/fria-wizard.zul`

**ViewModel:** `FriaWizardViewModel.java`  
**Ubicación:** `com.codeflowx.govern.viewmodel.compliance.FriaWizardViewModel`

**BPMN Process:** `fria-process.bpmn20.xml`

---

## 2. CAMPOS SOLICITADOS EN FRIA

### 2.1 Paso 1: Descripción de Procesos (Art. 27.1.a)

**Campo Obligatorio:** ✅ SÍ

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `FRIAPROCESSDESCRIPTION` | TEXT | ✅ SÍ | Descripción detallada de los procesos donde se usa el sistema IA |

**Validación:**
- Mínimo 100 caracteres
- Debe describir el contexto de uso
- Debe explicar el propósito del sistema

**Ejemplo:**
```
"Sistema de scoring crediticio utilizado por el departamento de riesgos 
para evaluar solicitudes de préstamos personales. El sistema analiza 
historial crediticio, ingresos, gastos y otros factores financieros 
para asignar un score de 0-1000 que determina aprobación/rechazo."
```

### 2.2 Paso 2: Período y Frecuencia (Art. 27.1.b)

**Campos Obligatorios:** ✅ SÍ

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `FRIAUSAGEPERIOD` | VARCHAR(200) | ✅ SÍ | Período de uso (ej: "2025-01-01 a 2025-12-31") |
| `FRIAUSAGEFREQUENCY` | VARCHAR(200) | ✅ SÍ | Frecuencia de uso (ej: "Diario", "1000 solicitudes/día") |

**Validación:**
- Período debe ser válido (fecha inicio < fecha fin)
- Frecuencia debe ser numérica o descriptiva

### 2.3 Paso 3: Categorías Personas Afectadas (Art. 27.1.c)

**Campos Obligatorios:** ✅ SÍ

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `FRIAAFFECTEDCATEGORIES` | JSONB | ✅ SÍ | Array de categorías: ["General public", "Employees", "Customers", "Vulnerable groups"] |
| `FRIAVULNERABLEGROUPSINCLUDED` | BOOLEAN | ✅ SÍ | Si incluye grupos vulnerables (menores, discapacitados, etc.) |

**Categorías Predefinidas:**
- General public
- Employees
- Customers
- Vulnerable groups (elderly, children, disabilities, minorities)
- Job applicants
- Students
- Patients
- Citizens

**Validación:**
- Mínimo 1 categoría seleccionada
- Si vulnerable groups = true, debe especificar cuáles

### 2.4 Paso 4: Riesgos Específicos (Art. 27.1.d)

**Campo Obligatorio:** ✅ SÍ

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `FRIARISKS` | JSONB | ✅ SÍ | Array de objetos FriaRisk con: description, affectedGroup, severity, probability, impact |

**Estructura JSON:**
```json
[
  {
    "riskId": "RISK-001",
    "description": "Discriminación por género en scoring crediticio",
    "affectedGroup": "Women",
    "severity": "HIGH",
    "probability": 0.35,
    "impact": "CRITICAL",
    "charterArticle": "Art. 21 (No discriminación)"
  },
  {
    "riskId": "RISK-002",
    "description": "Violación de privacidad por procesamiento de datos sensibles",
    "affectedGroup": "All applicants",
    "severity": "MEDIUM",
    "probability": 0.20,
    "impact": "HIGH",
    "charterArticle": "Art. 7-8 (Privacidad)"
  }
]
```

**Validación:**
- Mínimo 1 riesgo identificado
- Severity: LOW, MEDIUM, HIGH, CRITICAL
- Probability: 0.0 - 1.0
- Impact: LOW, MEDIUM, HIGH, CRITICAL

### 2.5 Paso 5: Supervisión Humana (Art. 27.1.e)

**Campos Obligatorios:** ✅ SÍ

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `FRIAHUMANOVERSIGHT` | TEXT | ✅ SÍ | Descripción de medidas de supervisión humana |
| `FRIAHITLENABLED` | BOOLEAN | ✅ SÍ | Si Human-in-the-Loop está habilitado |

**Validación:**
- Descripción mínima 50 caracteres
- Debe explicar cómo se implementa supervisión
- Debe describir capacidad de override/intervención

**Ejemplo:**
```
"Supervisión humana implementada mediante:
- Revisión manual de scores < 600 o > 900 (10% de casos)
- Capacidad de override por analista de riesgos senior
- Dashboard de monitoreo en tiempo real
- Alertas automáticas para anomalías
- Training mensual para supervisores"
```

### 2.6 Paso 6: Medidas de Mitigación (Art. 27.1.f)

**Campo Obligatorio:** ✅ SÍ

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `FRIAMITIGATIONMEASURES` | JSONB | ✅ SÍ | Array de objetos MitigationMeasure con: type, description, effectiveness |

**Estructura JSON:**
```json
[
  {
    "measureId": "MIT-001",
    "type": "PREVENTIVE",
    "description": "Auditoría periódica de sesgos con leka-bias-detection-service",
    "effectiveness": 0.85,
    "riskId": "RISK-001"
  },
  {
    "measureId": "MIT-002",
    "type": "DETECTIVE",
    "description": "Monitoreo continuo de métricas de equidad",
    "effectiveness": 0.75,
    "riskId": "RISK-001"
  },
  {
    "measureId": "MIT-003",
    "type": "CORRECTIVE",
    "description": "Proceso de apelación para decisiones automáticas",
    "effectiveness": 0.90,
    "riskId": "RISK-001"
  }
]
```

**Tipos de Medidas:**
- PREVENTIVE: Previenen el riesgo
- DETECTIVE: Detectan el riesgo cuando ocurre
- CORRECTIVE: Corrigen el impacto del riesgo

**Validación:**
- Mínimo 1 medida por riesgo identificado
- Effectiveness: 0.0 - 1.0
- Debe estar vinculada a un riesgo específico

---

## 3. CAMPOS OBLIGATORIOS SEGÚN AI ACT

### 3.1 Art. 27.1 - 6 Elementos Mandatorios

Todos los campos de los 6 pasos son **OBLIGATORIOS** según Art. 27.1:

| Elemento | Campo | Artículo | Obligatorio |
|----------|-------|----------|-------------|
| a) Descripción procesos | `FRIAPROCESSDESCRIPTION` | Art. 27.1.a | ✅ SÍ |
| b) Período y frecuencia | `FRIAUSAGEPERIOD`, `FRIAUSAGEFREQUENCY` | Art. 27.1.b | ✅ SÍ |
| c) Categorías personas | `FRIAAFFECTEDCATEGORIES`, `FRIAVULNERABLEGROUPSINCLUDED` | Art. 27.1.c | ✅ SÍ |
| d) Riesgos específicos | `FRIARISKS` | Art. 27.1.d | ✅ SÍ |
| e) Supervisión humana | `FRIAHUMANOVERSIGHT`, `FRIAHITLENABLED` | Art. 27.1.e | ✅ SÍ |
| f) Medidas mitigación | `FRIAMITIGATIONMEASURES` | Art. 27.1.f | ✅ SÍ |

### 3.2 Campos Adicionales Obligatorios

| Campo | Artículo | Obligatorio | Descripción |
|-------|----------|-------------|-------------|
| `FRIAART27COMPLIANT` | Art. 27 | ✅ SÍ | Boolean que indica si cumple Art. 27 |
| `FRIACOMPLETENESSCORE` | Art. 27 | ✅ SÍ | Score 0-1 de completitud (mínimo 0.90) |
| `FRIAAPPROVED` | Art. 27 | ✅ SÍ (si alto impacto) | Aprobación HITL requerida si `FRIAIMPACTSEVERITY == HIGH` |
| `FRIANOTIFIED` | Art. 27.3 | ✅ SÍ (si alto impacto) | Notificación autoridad si impacto alto |

### 3.3 Validación de Completitud

**Regla:** `FRIACOMPLETENESSCORE >= 0.90` para considerar FRIA completa

**Cálculo:**
```java
public BigDecimal calculateCompletenessScore(FriaAssessment fria) {
    int totalFields = 6; // 6 elementos Art. 27.1
    int completedFields = 0;
    
    if (fria.getFriaprocessdescription() != null && !fria.getFriaprocessdescription().isEmpty()) completedFields++;
    if (fria.getFriausageperiod() != null && fria.getFriausagefrequency() != null) completedFields++;
    if (fria.getFriaaffectedcategories() != null) completedFields++;
    if (fria.getFriarisks() != null && !fria.getFriarisks().isEmpty()) completedFields++;
    if (fria.getFriahumanoversight() != null && fria.getFriahitlenabled() != null) completedFields++;
    if (fria.getFriamitigationmeasures() != null && !fria.getFriamitigationmeasures().isEmpty()) completedFields++;
    
    return BigDecimal.valueOf(completedFields).divide(BigDecimal.valueOf(totalFields), 2, RoundingMode.HALF_UP);
}
```

---

## 4. CÁLCULO DE RIESGO FINAL

### 4.1 Fórmula de Riesgo Agregado

**Cálculo:** Riesgo Final = f(Severity, Probability, Impact, Mitigation Effectiveness)

```java
public BigDecimal calculateFinalRisk(FriaAssessment fria) {
    List<FriaRisk> risks = parseRisks(fria.getFriarisks());
    List<MitigationMeasure> measures = parseMeasures(fria.getFriamitigationmeasures());
    
    BigDecimal totalRisk = BigDecimal.ZERO;
    
    for (FriaRisk risk : risks) {
        // Severity score: LOW=0.25, MEDIUM=0.50, HIGH=0.75, CRITICAL=1.0
        BigDecimal severityScore = getSeverityScore(risk.getSeverity());
        
        // Probability (0-1)
        BigDecimal probability = risk.getProbability();
        
        // Impact score: LOW=0.25, MEDIUM=0.50, HIGH=0.75, CRITICAL=1.0
        BigDecimal impactScore = getImpactScore(risk.getImpact());
        
        // Risk before mitigation
        BigDecimal rawRisk = severityScore
            .multiply(probability)
            .multiply(impactScore);
        
        // Apply mitigation effectiveness
        BigDecimal mitigationEffectiveness = getMitigationEffectiveness(risk.getRiskId(), measures);
        BigDecimal mitigatedRisk = rawRisk.multiply(
            BigDecimal.ONE.subtract(mitigationEffectiveness)
        );
        
        totalRisk = totalRisk.add(mitigatedRisk);
    }
    
    // Normalize to 0-1 scale
    return totalRisk.divide(BigDecimal.valueOf(risks.size()), 2, RoundingMode.HALF_UP);
}
```

### 4.2 Determinación de Impact Severity

**Lógica:**
```java
public String determineImpactSeverity(BigDecimal finalRisk) {
    if (finalRisk.compareTo(new BigDecimal("0.75")) >= 0) {
        return "CRITICAL";
    } else if (finalRisk.compareTo(new BigDecimal("0.50")) >= 0) {
        return "HIGH";
    } else if (finalRisk.compareTo(new BigDecimal("0.25")) >= 0) {
        return "MEDIUM";
    } else {
        return "LOW";
    }
}
```

### 4.3 Charter Articles Affected

**Análisis Automático:**
```java
public List<String> analyzeCharterArticles(FriaAssessment fria) {
    List<String> articles = new ArrayList<>();
    
    List<FriaRisk> risks = parseRisks(fria.getFriarisks());
    
    for (FriaRisk risk : risks) {
        // Análisis por keywords y categorías
        if (risk.getDescription().toLowerCase().contains("discrimin") || 
            risk.getAffectedGroup().equals("Vulnerable groups")) {
            articles.add("Art. 21 (No discriminación)");
        }
        
        if (risk.getDescription().toLowerCase().contains("privacidad") || 
            risk.getDescription().toLowerCase().contains("datos personales")) {
            articles.add("Art. 7-8 (Privacidad y protección de datos)");
        }
        
        if (fria.getFriavulnerablegroupsincluded() && 
            risk.getAffectedGroup().contains("children")) {
            articles.add("Art. 24 (Derechos del niño)");
        }
        
        if (risk.getSeverity().equals("CRITICAL") && 
            risk.getImpact().equals("CRITICAL")) {
            articles.add("Art. 1 (Dignidad humana)");
        }
    }
    
    return articles.stream().distinct().collect(Collectors.toList());
}
```

---

## 5. ÁRBOL DE RIESGO Y ACTUALIZACIÓN DINÁMICA

### 5.1 Estructura del Árbol de Riesgo

**Visualización:** Dashboard interactivo en `/console/gobierno/compliance/fria-risk-tree.zul`

**Estructura:**
```
RIESGO FINAL (0.65)
├── RIESGO 1: Discriminación (0.40)
│   ├── Severity: HIGH (0.75)
│   ├── Probability: 0.35
│   ├── Impact: CRITICAL (1.0)
│   ├── Raw Risk: 0.26
│   └── Mitigated Risk: 0.04 (mitigation 85%)
│       └── Medidas:
│           ├── MIT-001: Auditoría sesgos (85% efectividad)
│           └── MIT-002: Monitoreo continuo (75% efectividad)
│
├── RIESGO 2: Privacidad (0.25)
│   ├── Severity: MEDIUM (0.50)
│   ├── Probability: 0.20
│   ├── Impact: HIGH (0.75)
│   ├── Raw Risk: 0.08
│   └── Mitigated Risk: 0.02 (mitigation 75%)
│       └── Medidas:
│           └── MIT-003: Encriptación datos (75% efectividad)
│
└── RIESGO 3: Transparencia (0.20)
    ├── Severity: LOW (0.25)
    ├── Probability: 0.30
    ├── Impact: MEDIUM (0.50)
    ├── Raw Risk: 0.04
    └── Mitigated Risk: 0.01 (mitigation 75%)
        └── Medidas:
            └── MIT-004: Explicabilidad (75% efectividad)
```

### 5.2 Actualización Dinámica

**Trigger:** Cualquier cambio en parámetros recalcula el árbol automáticamente

**Eventos que disparan recálculo:**
1. Cambio en severity de un riesgo
2. Cambio en probability de un riesgo
3. Cambio en impact de un riesgo
4. Añadir/eliminar medida de mitigación
5. Cambio en effectiveness de una medida

**Código ViewModel:**
```java
@Listen("onChange = #cmbRiskSeverity")
public void onRiskSeverityChanged() {
    // Recalcular riesgo afectado
    FriaRisk risk = getSelectedRisk();
    risk.setSeverity(getSelectedSeverity());
    
    // Recalcular riesgo final
    BigDecimal newRisk = calculateRiskForRisk(risk);
    risk.setCalculatedRisk(newRisk);
    
    // Recalcular riesgo total
    BigDecimal totalRisk = calculateFinalRisk(friaData);
    friaData.setFinalRisk(totalRisk);
    
    // Actualizar árbol visual
    refreshRiskTree();
    
    // Guardar en log inmutable
    logRiskChange("SEVERITY_CHANGED", risk.getRiskId(), newRisk);
}
```

### 5.3 Visualización del Árbol

**Componente ZUL:**
```xml
<tree id="treeRisks" model="@bind(vm.riskTreeModel)">
    <treecols>
        <treecol label="Riesgo" width="300px"/>
        <treecol label="Severity" width="100px"/>
        <treecol label="Probability" width="100px"/>
        <treecol label="Impact" width="100px"/>
        <treecol label="Raw Risk" width="100px"/>
        <treecol label="Mitigated Risk" width="120px"/>
        <treecol label="Mitigation" width="150px"/>
    </treecols>
    <template name="model">
        <treeitem>
            <treerow>
                <treecell label="@bind(each.riskDescription)"/>
                <treecell label="@bind(each.severity)" style="@bind(each.severityStyle)"/>
                <treecell label="@bind(each.probability)"/>
                <treecell label="@bind(each.impact)"/>
                <treecell label="@bind(each.rawRisk)" style="font-weight: bold;"/>
                <treecell label="@bind(each.mitigatedRisk)" style="color: green; font-weight: bold;"/>
                <treecell label="@bind(each.mitigationEffectiveness)"/>
            </treerow>
            <treechildren render="true">
                <treeitem forEach="@bind(each.measures)">
                    <treerow>
                        <treecell label="@bind(each.measureDescription)" style="padding-left: 20px;"/>
                        <treecell colspan="6"/>
                    </treerow>
                </treeitem>
            </treechildren>
        </treeitem>
    </template>
</tree>
```

---

## 6. MÉTRICAS TÉCNICAS PARA DATASETS, PROMPTS Y RAG

### 6.1 Métricas para Datasets

**Microservicio:** `leka-bias-detection-service` (Port 8001)

**Endpoint:** `/api/tabular/evaluate-data-quality`

**Métricas Obligatorias (Art. 10):**

| Métrica | Endpoint | Umbral Mínimo | Descripción |
|---------|----------|---------------|-------------|
| **Completitud** | `/evaluate-data-quality` | ≥ 0.95 | Porcentaje de valores no nulos |
| **Consistencia** | `/evaluate-data-quality` | ≥ 0.90 | Valores dentro de rangos esperados |
| **Exactitud** | `/evaluate-data-quality` | ≥ 0.90 | Valores correctos según reglas de negocio |
| **Sesgo Demográfico** | `/analyze-bias` | ≤ 0.10 | Diferencia estadística entre grupos |
| **Representatividad** | `/evaluate-data-quality` | ≥ 0.85 | Distribución representativa de población |
| **Privacidad (k-anonymity)** | `/evaluate-privacy` | k ≥ 5 | Mínimo 5 registros con mismos atributos |
| **Privacidad (l-diversity)** | `/evaluate-privacy` | l ≥ 2 | Mínimo 2 valores distintos en atributos sensibles |
| **Drift Temporal** | `/detect-drift` | ≤ 0.15 | Cambio en distribución (KS test) |

**Validación Automática:**
```java
public boolean validateDatasetQuality(String datasetId) {
    DataQualityResult result = biasDetectionService.evaluateDataQuality(datasetId);
    
    boolean pass = true;
    List<String> failures = new ArrayList<>();
    
    if (result.getCompleteness() < 0.95) {
        pass = false;
        failures.add("Completitud insuficiente: " + result.getCompleteness());
    }
    
    if (result.getBiasScore() > 0.10) {
        pass = false;
        failures.add("Sesgo demográfico detectado: " + result.getBiasScore());
    }
    
    if (result.getKAnonymity() < 5) {
        pass = false;
        failures.add("k-anonymity insuficiente: " + result.getKAnonymity());
    }
    
    if (!pass) {
        logValidationFailure(datasetId, failures);
    }
    
    return pass;
}
```

### 6.2 Métricas para Prompts

**Microservicio:** `leka-prompt-governance` (Port 8003)

**Endpoints:**
- `/api/prompt/evaluate-safety`
- `/api/prompt/detect-pii-leakage`
- `/api/prompt/evaluate-effectiveness`

**Métricas Obligatorias:**

| Métrica | Endpoint | Umbral Mínimo | Descripción |
|---------|----------|---------------|-------------|
| **Safety Score** | `/evaluate-safety` | ≥ 0.90 | Ausencia de contenido peligroso |
| **PII Leakage** | `/detect-pii-leakage` | 0 (ninguno) | Detección de datos personales |
| **Prompt Injection** | `/evaluate-safety` | 0 (ninguno) | Detección de intentos de inyección |
| **Effectiveness** | `/evaluate-effectiveness` | ≥ 0.80 | Capacidad de generar respuestas útiles |
| **Toxicity** | `/evaluate-safety` | ≤ 0.05 | Contenido tóxico (hate speech, etc.) |
| **Context Window Usage** | `/evaluate-effectiveness` | ≤ 0.90 | Uso eficiente del contexto |

**Validación Automática:**
```java
public boolean validatePrompt(String promptId) {
    PromptSafetyResult safety = promptGovernanceService.evaluateSafety(promptId);
    PIILeakageResult pii = promptGovernanceService.detectPIILeakage(promptId);
    EffectivenessResult effectiveness = promptGovernanceService.evaluateEffectiveness(promptId);
    
    boolean pass = true;
    
    if (safety.getSafetyScore() < 0.90) {
        pass = false;
        logValidationFailure(promptId, "Safety score insuficiente: " + safety.getSafetyScore());
    }
    
    if (pii.getPiiCount() > 0) {
        pass = false;
        logValidationFailure(promptId, "PII detectado: " + pii.getPiiCount() + " entidades");
    }
    
    if (safety.getPromptInjectionAttempts() > 0) {
        pass = false;
        logValidationFailure(promptId, "Vulnerable a prompt injection");
    }
    
    return pass;
}
```

### 6.3 Métricas para RAG

**Microservicio:** `leka-rag-evaluation` (Port 8004)

**Endpoints:**
- `/api/rag/evaluate-retrieval-quality`
- `/api/rag/evaluate-answer-quality`
- `/api/rag/evaluate-citation-accuracy`

**Métricas Obligatorias:**

| Métrica | Endpoint | Umbral Mínimo | Descripción |
|---------|----------|---------------|-------------|
| **Retrieval Precision** | `/evaluate-retrieval-quality` | ≥ 0.80 | Documentos relevantes recuperados |
| **Retrieval Recall** | `/evaluate-retrieval-quality` | ≥ 0.75 | Porcentaje de documentos relevantes encontrados |
| **Answer Accuracy** | `/evaluate-answer-quality` | ≥ 0.85 | Respuestas correctas |
| **Citation Accuracy** | `/evaluate-citation-accuracy` | ≥ 0.90 | Citas correctas y verificables |
| **Grounding Score** | `/evaluate-answer-quality` | ≥ 0.80 | Respuestas basadas en documentos |
| **Latency (P95)** | `/evaluate-retrieval-quality` | ≤ 2000ms | Tiempo de respuesta percentil 95 |
| **Hallucination Rate** | `/evaluate-answer-quality` | ≤ 0.05 | Respuestas sin base en documentos |

**Validación Automática:**
```java
public boolean validateRAGSystem(String ragSystemId) {
    RetrievalQualityResult retrieval = ragEvaluationService.evaluateRetrievalQuality(ragSystemId);
    AnswerQualityResult answer = ragEvaluationService.evaluateAnswerQuality(ragSystemId);
    CitationAccuracyResult citation = ragEvaluationService.evaluateCitationAccuracy(ragSystemId);
    
    boolean pass = true;
    
    if (retrieval.getPrecision() < 0.80) {
        pass = false;
        logValidationFailure(ragSystemId, "Precisión de recuperación insuficiente");
    }
    
    if (answer.getAccuracy() < 0.85) {
        pass = false;
        logValidationFailure(ragSystemId, "Precisión de respuestas insuficiente");
    }
    
    if (citation.getAccuracy() < 0.90) {
        pass = false;
        logValidationFailure(ragSystemId, "Precisión de citas insuficiente");
    }
    
    if (answer.getHallucinationRate() > 0.05) {
        pass = false;
        logValidationFailure(ragSystemId, "Tasa de alucinaciones demasiado alta");
    }
    
    return pass;
}
```

---

## 7. VALIDACIÓN TÉCNICA DE MODELOS

### 7.1 Modelos OpenAI (Comerciales)

**Validaciones Específicas:**

| Validación | Método | Descripción |
|-----------|--------|-------------|
| **API Key Seguridad** | Verificación credenciales | Validar que API key está encriptada y rotada periódicamente |
| **Rate Limiting** | Monitoreo llamadas | Controlar límites de uso y costos |
| **Data Privacy** | Análisis de términos | Verificar que OpenAI no usa datos para entrenamiento (opción opt-out) |
| **Model Version** | Tracking versión | Registrar versión exacta del modelo usado |
| **Cost Tracking** | Monitoreo costos | Tracking de costos por request |
| **Latency Monitoring** | Métricas tiempo respuesta | P95, P99 de latencia |
| **Error Handling** | Manejo errores API | Retry logic, fallback strategies |

**Código:**
```java
public ModelValidationResult validateOpenAIModel(String modelId) {
    ModelValidationResult result = new ModelValidationResult();
    
    // 1. Verificar API key security
    if (!isApiKeyEncrypted(modelId)) {
        result.addFailure("API key no está encriptada");
    }
    
    // 2. Verificar términos de uso
    OpenAITerms terms = getOpenAITerms(modelId);
    if (!terms.isDataOptOutEnabled()) {
        result.addWarning("Datos pueden ser usados para entrenamiento OpenAI");
    }
    
    // 3. Verificar rate limits
    RateLimitStatus rateLimit = checkRateLimits(modelId);
    if (rateLimit.getUsage() > 0.90) {
        result.addWarning("Rate limit cerca del máximo: " + rateLimit.getUsage());
    }
    
    // 4. Verificar costos
    BigDecimal monthlyCost = calculateMonthlyCost(modelId);
    if (monthlyCost.compareTo(new BigDecimal("1000")) > 0) {
        result.addWarning("Costos mensuales altos: $" + monthlyCost);
    }
    
    return result;
}
```

### 7.2 Modelos Open Source

**Validaciones Específicas:**

| Validación | Método | Descripción |
|-----------|--------|-------------|
| **Licencia** | Verificación licencia | Validar compatibilidad con uso comercial |
| **Model Card** | Revisión documentación | Verificar que existe model card completo |
| **Checksum Verification** | Hash verification | Verificar integridad del modelo descargado |
| **Bias Documentation** | Revisión sesgos | Verificar documentación de sesgos conocidos |
| **Training Data** | Revisión dataset | Verificar documentación de datos de entrenamiento |
| **Vulnerabilities** | Security scan | Escanear vulnerabilidades conocidas |
| **Performance Benchmarks** | Comparación benchmarks | Verificar rendimiento vs. documentación |

**Código:**
```java
public ModelValidationResult validateOpenSourceModel(String modelId) {
    ModelValidationResult result = new ModelValidationResult();
    
    OpenSourceModel model = getModel(modelId);
    
    // 1. Verificar licencia
    if (!isLicenseCompatible(model.getLicense())) {
        result.addFailure("Licencia incompatible: " + model.getLicense());
    }
    
    // 2. Verificar model card
    if (model.getModelCard() == null || model.getModelCard().isEmpty()) {
        result.addFailure("Model card no disponible");
    }
    
    // 3. Verificar checksum
    String expectedHash = model.getExpectedChecksum();
    String actualHash = calculateModelHash(modelId);
    if (!expectedHash.equals(actualHash)) {
        result.addFailure("Checksum no coincide - modelo puede estar corrupto");
    }
    
    // 4. Verificar sesgos documentados
    if (model.getBiasDocumentation() == null) {
        result.addWarning("Sesgos no documentados");
    }
    
    // 5. Security scan
    List<SecurityVulnerability> vulnerabilities = securityScanService.scanModel(modelId);
    if (!vulnerabilities.isEmpty()) {
        result.addFailure("Vulnerabilidades de seguridad detectadas: " + vulnerabilities.size());
    }
    
    return result;
}
```

### 7.3 Modelos Propios (Internos)

**Validaciones Específicas:**

| Validación | Método | Descripción |
|-----------|--------|-------------|
| **Training Data Quality** | Evaluación dataset | Validar calidad y representatividad |
| **Bias Analysis** | Análisis sesgos | Detección de sesgos en entrenamiento |
| **Performance Metrics** | Benchmarking | Accuracy, precision, recall, F1 |
| **Robustness Testing** | Adversarial testing | FGSM, PGD attacks |
| **Documentation Completeness** | Revisión documentación | Art. 11 + Anexo IV completo |
| **Version Control** | Git tracking | Control de versiones del modelo |
| **Reproducibility** | Reproducibilidad | Seeds, hiperparámetros documentados |

**Código:**
```java
public ModelValidationResult validateInternalModel(String modelId) {
    ModelValidationResult result = new ModelValidationResult();
    
    Model model = modelService.getModel(modelId);
    
    // 1. Verificar dataset de entrenamiento
    String datasetId = model.getTrainingDatasetId();
    if (!validateDatasetQuality(datasetId)) {
        result.addFailure("Dataset de entrenamiento no cumple calidad mínima");
    }
    
    // 2. Análisis de sesgos
    BiasAnalysisResult bias = biasDetectionService.analyzeBias(modelId);
    if (bias.getBiasScore() > 0.10) {
        result.addFailure("Sesgo detectado en modelo: " + bias.getBiasScore());
    }
    
    // 3. Performance metrics
    PerformanceMetrics metrics = modelEvaluationService.getPerformanceMetrics(modelId);
    if (metrics.getAccuracy() < 0.85) {
        result.addWarning("Accuracy por debajo del umbral: " + metrics.getAccuracy());
    }
    
    // 4. Robustness testing
    AdversarialRobustnessResult robustness = adversarialService.testRobustness(modelId);
    if (robustness.getRobustnessScore() < 0.80) {
        result.addFailure("Robustez adversarial insuficiente: " + robustness.getRobustnessScore());
    }
    
    // 5. Documentación técnica
    if (!model.getModtechnicaldoccomplete()) {
        result.addFailure("Documentación técnica incompleta (Art. 11)");
    }
    
    return result;
}
```

---

## 8. DIFERENCIA: FRIA DOCUMENTAL vs EVALUACIÓN TÉCNICA REAL

### 8.1 FRIA Documental

**Definición:** Evaluación basada en **información declarativa** proporcionada por el usuario (deployer).

**Características:**
- ✅ Basada en wizard de 6 pasos (datos ingresados manualmente)
- ✅ Análisis de riesgos basado en descripciones textuales
- ✅ Cálculo de riesgo basado en severity/probability/impact declarados
- ✅ Medidas de mitigación descritas pero no verificadas
- ✅ Genera documento PDF para auditoría

**Limitaciones:**
- ❌ No verifica que las medidas de mitigación estén realmente implementadas
- ❌ No valida métricas técnicas reales del sistema
- ❌ Depende de la honestidad y conocimiento del usuario

**Ubicación:** Tabla `FRIAFUNDAMENTALRIGHTSASSESSMENTS`

### 8.2 Evaluación Técnica Real

**Definición:** Evaluación basada en **métricas técnicas objetivas** obtenidas de ejecución real del sistema.

**Características:**
- ✅ Basada en métricas de microservicios (leka-bias-detection, leka-llm-evaluation, etc.)
- ✅ Análisis de sesgos reales en datasets y modelos
- ✅ Medición de robustez adversarial real
- ✅ Validación de calidad de datos real
- ✅ Monitoreo continuo post-despliegue

**Componentes:**
- **Dataset Quality:** Métricas reales de calidad (completitud, sesgo, privacidad)
- **Model Performance:** Accuracy, precision, recall reales
- **Bias Detection:** Sesgos detectados estadísticamente
- **Adversarial Robustness:** Resultados de tests adversariales reales
- **Privacy Analysis:** k-anonymity, l-diversity reales

**Ubicación:** Tablas `GOVMODELEVALUATIONS`, `DRFDRIFTDETECTIONS`, logs de microservicios

### 8.3 Integración FRIA + Evaluación Técnica

**Estado:** ✅ **IMPLEMENTADO** - Noviembre 2025 (INC-007)

**Proceso Integrado:**

```
1. FRIA Documental (Wizard)
   ↓
2. Validación Técnica Automática
   ├── Dataset Quality Check
   ├── Model Performance Check
   ├── Bias Detection
   ├── Adversarial Robustness
   └── Privacy Analysis
   ↓
3. Comparación y Validación Cruzada
   ├── ¿Coinciden riesgos declarados con métricas reales?
   ├── ¿Medidas de mitigación están implementadas?
   └── ¿Scores de riesgo son consistentes?
   ↓
4. Reporte Integrado
   ├── FRIA Documental (PDF)
   └── Evaluación Técnica (Métricas + Gráficos)
```

**Implementación:**

**Microservicio:** `leka-fria-generator` (Port 8012)

**Endpoint:** `POST /api/fria/cross-validate`

**Funcionalidad:**
- ✅ Obtiene métricas técnicas automáticamente de microservicios
- ✅ Compara riesgos declarados vs métricas reales
- ✅ Detecta inconsistencias con severidad (LOW, MEDIUM, HIGH, CRITICAL)
- ✅ Calcula score de consistencia (0.0 - 1.0)
- ✅ Genera recomendaciones automáticas
- ✅ Requiere justificación si `consistency_score < 0.70`

**Validaciones Implementadas:**
1. **Sesgos:** Compara riesgos de sesgo declarados vs `bias_score` real
2. **Mitigaciones:** Verifica que medidas declaradas estén implementadas
3. **Precisión:** Compara riesgos de precisión vs `accuracy` real
4. **Calidad Datos:** Compara riesgos de calidad vs `completeness` real

**Código Java (Pendiente de Integración):**
```java
public FriaIntegratedReport generateIntegratedReport(Long friaId) {
    FriaAssessment fria = friaService.getFria(friaId);
    Project project = fria.getProject();
    
    // 1. FRIA Documental
    FriaDocumentalReport docReport = generateFriaDocument(fria);
    
    // 2. Evaluación Técnica Real
    TechnicalEvaluationReport techReport = new TechnicalEvaluationReport();
    
    // Dataset quality
    DataQualityResult datasetQuality = biasDetectionService.evaluateDataQuality(
        project.getTrainingDatasetId()
    );
    techReport.setDatasetQuality(datasetQuality);
    
    // Model performance
    PerformanceMetrics modelPerformance = modelEvaluationService.getPerformanceMetrics(
        project.getModelId()
    );
    techReport.setModelPerformance(modelPerformance);
    
    // Bias detection
    BiasAnalysisResult bias = biasDetectionService.analyzeBias(project.getModelId());
    techReport.setBiasAnalysis(bias);
    
    // Adversarial robustness
    AdversarialRobustnessResult robustness = adversarialService.testRobustness(
        project.getModelId()
    );
    techReport.setAdversarialRobustness(robustness);
    
    // 3. Validación Cruzada (LLAMAR A MICROSERVICIO PYTHON)
    CrossValidationResult validation = friaCrossValidationClient.crossValidate(
        friaData, technicalMetrics
    );
    
    // 4. Reporte Integrado
    FriaIntegratedReport integrated = new FriaIntegratedReport();
    integrated.setDocumentalReport(docReport);
    integrated.setTechnicalReport(techReport);
    integrated.setCrossValidation(validation);
    integrated.setConsistencyScore(validation.getConsistencyScore());
    
    return integrated;
}
```

**Código Python (Implementado):**
```python
# Endpoint: POST /api/fria/cross-validate
@router.post("/cross-validate", response_model=CrossValidationResult)
async def cross_validate_fria(
    fria_data: FriaDataDTO,
    technical_metrics: TechnicalMetricsDTO = None,
) -> CrossValidationResult:
    # 1. Obtener métricas técnicas si no se proporcionan
    if technical_metrics is None:
        metrics_fetcher = get_technical_metrics_fetcher()
        technical_metrics = await metrics_fetcher.fetch_all_metrics(...)
    
    # 2. Ejecutar validación cruzada
    validation_service = get_fria_cross_validation_service()
    result = await validation_service.validate(fria_data, technical_metrics)
    
    return result
```

---

## 9. PREVENCIÓN DE MANIPULACIÓN POR PARTNERS

### 9.1 Controles de Integridad

**1. Logs Inmutables con Hash Chain**

**Tabla:** `IMLIMMUTABLELOGS`

**Proceso:**
- Cada evaluación genera log inmutable
- Hash chain previene modificación
- Triggers PostgreSQL bloquean UPDATE/DELETE

**Código:**
```java
public void logFriaEvaluation(FriaAssessment fria, String action) {
    ImmutableLog log = new ImmutableLog();
    
    // Obtener hash anterior
    String previousHash = getLastLogHash(fria.getIdxfriaassessment());
    log.setImlprevioushash(previousHash);
    
    // Calcular hash actual
    String data = serializeFriaData(fria);
    String currentHash = calculateSHA256(data + previousHash);
    log.setImlcurrenthash(currentHash);
    
    // Guardar (append-only)
    immutableLogDAO.save(log);
}
```

**2. Verificación de Integridad Automática**

**Endpoint:** `/api/compliance/verify-fria-integrity/{friaId}`

**Proceso:**
```java
public IntegrityVerificationResult verifyFriaIntegrity(Long friaId) {
    List<ImmutableLog> logs = getLogsForFria(friaId);
    
    boolean valid = true;
    List<String> issues = new ArrayList<>();
    
    for (int i = 1; i < logs.size(); i++) {
        ImmutableLog current = logs.get(i);
        ImmutableLog previous = logs.get(i - 1);
        
        // Verificar hash chain
        String expectedPreviousHash = previous.getImlcurrenthash();
        if (!current.getImlprevioushash().equals(expectedPreviousHash)) {
            valid = false;
            issues.add("Hash chain roto en log " + current.getIdximmutablelog());
        }
        
        // Verificar hash actual
        String calculatedHash = calculateHash(current.getImldata() + current.getImlprevioushash());
        if (!current.getImlcurrenthash().equals(calculatedHash)) {
            valid = false;
            issues.add("Hash actual no coincide en log " + current.getIdximmutablelog());
        }
    }
    
    return new IntegrityVerificationResult(valid, issues);
}
```

### 9.2 Controles de Acceso

**1. Roles y Permisos**

| Rol | Permisos FRIA | Restricciones |
|-----|---------------|---------------|
| **Deployer** | Crear, editar FRIA (solo su proyecto) | No puede aprobar |
| **Compliance Officer** | Revisar, aprobar FRIA | No puede modificar datos originales |
| **Auditor** | Solo lectura + export | No puede modificar nada |
| **Partner** | Solo lectura (si tiene acceso) | No puede modificar ni aprobar |

**2. Auditoría de Accesos**

**Tabla:** `IMLIMMUTABLELOGS` (todos los accesos se registran)

```java
@PreAuthorize("hasRole('COMPLIANCE_OFFICER')")
public void approveFria(Long friaId) {
    // Log acceso
    logAccess("APPROVE_FRIA", friaId, getCurrentUser());
    
    // Verificar que no ha sido modificado desde última revisión
    if (hasBeenModifiedSinceLastReview(friaId)) {
        throw new SecurityException("FRIA modificado después de revisión - requiere nueva revisión");
    }
    
    // Aprobar
    friaService.approve(friaId);
}
```

### 9.3 Validación de Coherencia

**1. Comparación con Métricas Técnicas**

**Proceso:** Si FRIA documental no coincide con métricas técnicas, se genera alerta

```java
public void validateFriaConsistency(Long friaId) {
    FriaAssessment fria = friaService.getFria(friaId);
    TechnicalEvaluationReport techReport = getTechnicalEvaluation(fria.getProject().getIdxproject());
    
    // Comparar riesgos declarados vs métricas reales
    List<FriaRisk> declaredRisks = parseRisks(fria.getFriarisks());
    
    for (FriaRisk declaredRisk : declaredRisks) {
        // Buscar métrica técnica correspondiente
        TechnicalMetric correspondingMetric = findCorrespondingMetric(declaredRisk, techReport);
        
        if (correspondingMetric != null) {
            // Validar coherencia
            if (!isConsistent(declaredRisk, correspondingMetric)) {
                // Alerta de inconsistencia
                createInconsistencyAlert(friaId, declaredRisk, correspondingMetric);
            }
        }
    }
}
```

**2. Detección de Cambios Sospechosos**

**Proceso:** Monitoreo de cambios que reducen riesgo sin justificación técnica

```java
public void detectSuspiciousChanges(Long friaId) {
    List<ImmutableLog> logs = getLogsForFria(friaId);
    
    for (int i = 1; i < logs.size(); i++) {
        ImmutableLog current = logs.get(i);
        ImmutableLog previous = logs.get(i - 1);
        
        FriaAssessment currentFria = deserializeFriaData(current.getImldata());
        FriaAssessment previousFria = deserializeFriaData(previous.getImldata());
        
        // Detectar reducción de riesgo sin justificación
        BigDecimal currentRisk = calculateFinalRisk(currentFria);
        BigDecimal previousRisk = calculateFinalRisk(previousFria);
        
        if (currentRisk.compareTo(previousRisk) < 0 && 
            currentRisk.subtract(previousRisk).abs().compareTo(new BigDecimal("0.20")) > 0) {
            // Reducción significativa de riesgo
            if (!hasTechnicalJustification(current, previous)) {
                // Alerta sospechosa
                createSuspiciousChangeAlert(friaId, current, previous);
            }
        }
    }
}
```

### 9.4 Firmas Digitales y Timestamps

**1. Timestamp Externo (RFC 3161)**

**Campo:** `IMLEXTERNALTIMESTAMP` en `IMLIMMUTABLELOGS`

**Proceso:**
```java
public void addExternalTimestamp(ImmutableLog log) {
    // Obtener timestamp de autoridad externa (blockchain/TSA)
    ExternalTimestamp timestamp = timestampAuthorityService.getTimestamp(log.getImlcurrenthash());
    
    log.setImlexternaltimestamp(timestamp.getProof());
    
    // Verificar que timestamp es válido
    if (!timestampAuthorityService.verifyTimestamp(timestamp)) {
        throw new SecurityException("Timestamp externo no válido");
    }
}
```

**2. Firmas de Aprobación**

**Proceso:** Aprobaciones requieren firma digital

```java
public void approveFriaWithSignature(Long friaId, String digitalSignature) {
    // Verificar firma
    if (!digitalSignatureService.verifySignature(friaId, digitalSignature, getCurrentUser())) {
        throw new SecurityException("Firma digital no válida");
    }
    
    // Aprobar
    friaService.approve(friaId);
    
    // Guardar firma en log inmutable
    logApprovalWithSignature(friaId, digitalSignature);
}
```

---

## 10. LOGS GENERADOS POR EVALUACIONES

### 10.1 Logs de FRIA

**Tabla:** `IMLIMMUTABLELOGS`

**Eventos Registrados:**

| Acción | Entity Type | Entity ID | Data Snapshot |
|--------|-------------|-----------|---------------|
| `FRIA_CREATED` | FRIA | friaId | JSON completo del FRIA inicial |
| `FRIA_STEP_COMPLETED` | FRIA | friaId | Paso completado + datos ingresados |
| `FRIA_RISK_ADDED` | FRIA | friaId | Riesgo añadido + cálculo actualizado |
| `FRIA_RISK_MODIFIED` | FRIA | friaId | Riesgo modificado + valores anteriores/nuevos |
| `FRIA_MITIGATION_ADDED` | FRIA | friaId | Medida añadida + recálculo riesgo |
| `FRIA_SCORE_CALCULATED` | FRIA | friaId | Score calculado + fórmula aplicada |
| `FRIA_DOCUMENT_GENERATED` | FRIA | friaId | URL documento + versión |
| `FRIA_APPROVED` | FRIA | friaId | Aprobador + fecha + firma |
| `FRIA_NOTIFIED` | FRIA | friaId | Autoridad notificada + ID notificación |

**Ejemplo de Log:**
```json
{
  "entityType": "FRIA",
  "entityId": 12345,
  "action": "FRIA_RISK_MODIFIED",
  "userId": 67890,
  "userName": "deployer.user",
  "data": {
    "friaId": 12345,
    "riskId": "RISK-001",
    "previousSeverity": "MEDIUM",
    "newSeverity": "HIGH",
    "previousRisk": 0.15,
    "newRisk": 0.35,
    "previousTotalRisk": 0.45,
    "newTotalRisk": 0.65,
    "reason": "Nuevo análisis detectó mayor probabilidad de discriminación",
    "timestamp": "2025-11-15T14:30:00Z"
  },
  "timestamp": "2025-11-15T14:30:00Z",
  "previousHash": "abc123...",
  "currentHash": "def456..."
}
```

### 10.2 Logs de Evaluaciones Técnicas

**Tabla:** `IMLIMMUTABLELOGS` + logs de microservicios

**Eventos Registrados:**

| Acción | Entity Type | Microservicio | Data Snapshot |
|--------|-------------|---------------|---------------|
| `DATASET_QUALITY_EVALUATED` | DATASET | leka-bias-detection | Métricas calidad + resultados |
| `BIAS_ANALYSIS_COMPLETED` | MODEL | leka-bias-detection | Sesgos detectados + scores |
| `PROMPT_SAFETY_EVALUATED` | PROMPT | leka-prompt-governance | Safety score + PII detectado |
| `RAG_EVALUATION_COMPLETED` | RAG | leka-rag-evaluation | Retrieval + answer + citation metrics |
| `ADVERSARIAL_TEST_COMPLETED` | MODEL | leka-adversarial-robustness | Robustness score + attack results |
| `MODEL_VALIDATION_COMPLETED` | MODEL | leka-llm-evaluation | Performance metrics + validation result |

**Ejemplo de Log Técnico:**
```json
{
  "entityType": "MODEL",
  "entityId": 54321,
  "action": "BIAS_ANALYSIS_COMPLETED",
  "userId": null,
  "userName": "system",
  "data": {
    "modelId": 54321,
    "datasetId": 98765,
    "biasMetrics": {
      "demographicParity": 0.12,
      "equalizedOdds": 0.08,
      "calibration": 0.15
    },
    "affectedGroups": ["Women", "Minorities"],
    "statisticalTest": "KS_TEST",
    "pValue": 0.003,
    "recommendation": "BIAS_DETECTED - Requiere mitigación",
    "timestamp": "2025-11-15T15:00:00Z"
  },
  "timestamp": "2025-11-15T15:00:00Z",
  "previousHash": "ghi789...",
  "currentHash": "jkl012..."
}
```

### 10.3 Logs de Validación Cruzada

**Eventos Registrados:**

| Acción | Descripción |
|--------|-------------|
| `FRIA_TECHNICAL_VALIDATION_STARTED` | Inicio validación cruzada FRIA vs métricas técnicas |
| `FRIA_INCONSISTENCY_DETECTED` | Inconsistencia detectada entre FRIA y métricas |
| `FRIA_CONSISTENCY_VERIFIED` | FRIA verificado como consistente con métricas |
| `FRIA_MANIPULATION_ALERT` | Alerta de posible manipulación detectada |

---

## 11. RESUMEN EJECUTIVO PARA AUDITOR

### 11.1 Proceso Completo FRIA

1. **Inicio:** Deployer inicia FRIA desde wizard
2. **Wizard 6 Pasos:** Completa 6 elementos Art. 27.1
3. **Cálculo Automático:** Sistema calcula riesgo final y impact severity
4. **Generación Documento:** Microservicio Python genera PDF
5. **Validación Técnica:** Sistema ejecuta evaluaciones técnicas automáticas
6. **Validación Cruzada:** Compara FRIA documental vs métricas técnicas
7. **Aprobación HITL:** Si impacto alto, requiere aprobación humana
8. **Notificación:** Si impacto alto, notifica autoridad (Art. 27.3)
9. **Registro:** Todo queda registrado en logs inmutables

### 11.2 Evidencias Disponibles

✅ **FRIA Documental:** PDF generado + registro en `FRIAFUNDAMENTALRIGHTSASSESSMENTS`  
✅ **Evaluaciones Técnicas:** Métricas en microservicios + logs inmutables  
✅ **Logs Inmutables:** Hash chain completo en `IMLIMMUTABLELOGS`  
✅ **Validación Cruzada:** Reportes de consistencia FRIA vs métricas  
✅ **Árbol de Riesgo:** Visualización interactiva con actualización dinámica  
✅ **Prevención Manipulación:** Hash chains, firmas digitales, timestamps externos

### 11.3 Cumplimiento EU AI Act

✅ **Art. 27:** FRIA completo con 6 elementos mandatorios  
✅ **Art. 27.3:** Notificación autoridad si impacto alto  
✅ **Art. 11:** Documentación técnica requerida  
✅ **Art. 15:** Métricas de precisión y robustez  
✅ **Art. 19:** Logs inmutables con hash chain  
✅ **Anexo IX:** Metodología FRIA según Anexo IX

**Estado:** ✅ **LISTO PARA AUDITORÍA**

---

**Última actualización:** Noviembre 2025  
**Versión:** 1.0  
**Mantenedor:** CodeflowX Compliance Team

