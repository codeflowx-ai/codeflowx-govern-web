# Auditoría: Servicios Business y ViewModels Asociados

**Fecha:** 2025-11-24
**Objetivo:** Mapear servicios business con sus ViewModels correspondientes y revisar lógica de negocio según prompts de compliance

---

## 📋 Resumen Ejecutivo

**Total Servicios Business:** 23
**Total ViewModels que usan Business Services:** 5 identificados directamente + múltiples indirectos

### Categorías de Servicios Business:

1. **Compliance (6 servicios)** - EU AI Act
2. **Governance (2 servicios)** - ISO/IEC 42001
3. **Models (1 servicio)** - Model Adaptation
4. **Integrations (12 servicios)** - Conectores externos
5. **Catalogs (1 servicio)** - Anexo III
6. **Logging (1 servicio)** - Immutable Logs

---

## 🔍 Servicios Business por Categoría

### 1. COMPLIANCE (EU AI Act)

#### 1.1. ComplianceAssessmentBusinessService
- **Ubicación:** `com.codeflowx.govern.business.compliance.ComplianceAssessmentBusinessService`
- **Propósito:** Gestiona evaluaciones de conformidad según EU AI Act Art. 43, Anexo VI
- **Lógica de Negocio:**
  - Crea evaluaciones de conformidad
  - Ejecuta 4 steps: QMS check, Doc review, Consistency check, Final assessment
  - Calcula scores de cumplimiento
  - Integra con QualityManagementSystemBusinessService y TechnicalDocumentationBusinessService
- **ViewModels Asociados:**
  - ⚠️ **InitiateConformityAssessmentViewModel** (`compliance/InitiateConformityAssessmentViewModel.java`)
    - **Estado:** 🔴 NO USA el Business Service (usa `BusinessService` genérico y BPMN)
    - **Acción requerida:** Migrar para usar `ComplianceAssessmentBusinessService.createAssessment()`
  - ❓ **ConformityReviewViewModel** - Pendiente verificar
  - ❓ **ConformityDeclarationManagerViewModel** - Pendiente verificar
  - ❓ **FinalReviewViewModel** - Pendiente verificar
- **Prompt Origen:** `PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md` - Prompt A.1
- **Entidad Relacionada:** `ComplianceAssessment`

#### 1.2. FriaAssessmentBusinessService
- **Ubicación:** `com.codeflowx.govern.business.compliance.FriaAssessmentBusinessService`
- **Propósito:** Gestiona evaluaciones de impacto en derechos fundamentales según Art. 27
- **Lógica de Negocio:**
  - Crea FRIA assessments
  - Actualiza secciones específicas (PROCESS_DESCRIPTION, USAGE_PERIOD, etc.)
  - Calcula completeness score (0.00 - 1.00) verificando 6 elementos mandatorios Art. 27.1
  - Valida integración con DPIA
  - Gestiona notificaciones a autoridades
- **ViewModels Asociados:**
  - ⚠️ **FriaWizardViewModel** (`compliance/FriaWizardViewModel.java`)
    - **Estado:** 🔴 NO USA el Business Service (usa `BusinessService` genérico y `ProjectService`)
    - **Acción requerida:** Migrar para usar:
      - `FriaAssessmentBusinessService.createFria()`
      - `FriaAssessmentBusinessService.updateFriaSection()`
      - `FriaAssessmentBusinessService.calculateCompletenessScore()`
  - ❓ **HighRiskClassifierViewModel** - Pendiente verificar
- **Prompt Origen:** `PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md` - Prompt A.2
- **Entidad Relacionada:** `FriaAssessment`

#### 1.3. EuRegistrationBusinessService
- **Ubicación:** `com.codeflowx.govern.business.compliance.EuRegistrationBusinessService`
- **Propósito:** Gestiona registro de sistemas IA en Base de Datos UE según Art. 49 y Anexo VIII
- **Lógica de Negocio:**
  - Crea registros (draft)
  - Actualiza submission data (JSON)
  - Envía a Base de Datos UE
  - Gestiona reintentos y validaciones
  - Maneja sistemas sensibles (registro no público)
  - Gestiona registro nacional para infraestructuras críticas
- **ViewModels Asociados:**
  - ⚠️ **EuRegistrationFormViewModel** (`euregistration/EuRegistrationFormViewModel.java`)
    - **Estado:** 🔴 NO USA el Business Service (usa `BusinessService` genérico y BPMN)
    - **Acción requerida:** Migrar para usar:
      - `EuRegistrationBusinessService.createRegistration()`
      - `EuRegistrationBusinessService.updateSubmissionData()`
  - ❓ **ReviewRegistrationPackageViewModel** - Pendiente verificar
  - ❓ **FixValidationErrorsViewModel** - Pendiente verificar
  - ❓ **ManualResolutionViewModel** - Pendiente verificar
  - ❓ **EURegistrationStatusViewModel** - Pendiente verificar
- **Prompt Origen:** `PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md` - Prompt A.3
- **Entidad Relacionada:** `EuRegistration`

#### 1.4. QualityManagementSystemBusinessService
- **Ubicación:** `com.codeflowx.govern.business.compliance.QualityManagementSystemBusinessService`
- **Propósito:** Implementa Quality Management System (QMS) según EU AI Act Art. 17
- **Lógica de Negocio:**
  - Implementa 13 módulos mandatorios del QMS:
    - a) Estrategia cumplimiento normativo
    - b) Control y verificación diseño
    - c) Desarrollo y aseguramiento calidad
    - d) Examen, prueba, validación
    - e) Especificaciones técnicas/normas
    - f) Sistemas gestión de datos
    - g) Sistema gestión riesgos (integración Art. 9)
    - h) Vigilancia poscomercialización (integración Art. 72)
    - i) Notificación incidentes graves (integración Art. 73)
    - j) Comunicación autoridades
    - k) Registro documentación
    - l) Gestión recursos
    - m) Marco rendición cuentas
  - Calcula QMS compliance score
- **ViewModels Asociados:**
  - ❓ **PENDIENTE IDENTIFICAR** - Buscar ViewModels en `compliance/` que usen este servicio
  - Posibles candidatos:
    - `ReviewQmsGapsViewModel`
    - `SectorDashboardViewModel`
- **Prompt Origen:** `PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md` - Prompt B.1
- **Entidad Relacionada:** N/A (servicio de cálculo, no entidad directa)

#### 1.5. TechnicalDocumentationBusinessService
- **Ubicación:** `com.codeflowx.govern.business.compliance.TechnicalDocumentationBusinessService`
- **Propósito:** Gestiona documentación técnica según EU AI Act Art. 11
- **Lógica de Negocio:**
  - Calcula documentation score
  - Valida completitud de documentación técnica
  - Gestiona versiones de documentación
- **ViewModels Asociados:**
  - ❓ **PENDIENTE IDENTIFICAR** - Buscar ViewModels en `compliance/` que usen este servicio
  - Posibles candidatos:
    - `CompleteDocumentationViewModel`
- **Prompt Origen:** `PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md` - Prompt B.2
- **Entidad Relacionada:** `AIActTechnicalDocumentation`

#### 1.6. AnnexIIICategoryBusinessService
- **Ubicación:** `com.codeflowx.govern.business.catalogs.AnnexIIICategoryBusinessService`
- **Propósito:** Gestiona categorías del Anexo III (sistemas de alto riesgo)
- **Lógica de Negocio:**
  - Clasifica sistemas según Anexo III
  - Determina si un sistema es de alto riesgo
- **ViewModels Asociados:**
  - ❓ **PENDIENTE IDENTIFICAR**
- **Prompt Origen:** `PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md` - Prompt C.1
- **Entidad Relacionada:** `AnnexIIICategory`

---

### 2. GOVERNANCE (ISO/IEC 42001)

#### 2.1. AICompetenceBusinessService
- **Ubicación:** `com.codeflowx.govern.business.governance.AICompetenceBusinessService`
- **Propósito:** Gestión de competencias y awareness según ISO/IEC 42001 Clauses 7.2/7.3
- **Lógica de Negocio:**
  - Gestiona competencias de personal
  - Calcula gap analysis
  - Genera dashboards de competencias
  - Gestiona registros de entrenamiento
- **ViewModels Asociados:**
  - ✅ **AICompetenceViewModel** (`governance/AICompetenceViewModel.java`)
    - Usa: `AICompetenceBusinessService`
    - Usa también: `AICCompetenceService`, `AITTrainingRecordService`, `ModelService`
    - Métodos business usados: `getCompetenceDashboard()`, `calculateGapAnalysis()`
- **Prompt Origen:** `PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md` - Prompt D.1
- **Entidad Relacionada:** `AICCompetence`, `AITTrainingRecord`

#### 2.2. AIObjectivesBusinessService
- **Ubicación:** `com.codeflowx.govern.business.governance.AIObjectivesBusinessService`
- **Propósito:** Gestión de objetivos IA según ISO/IEC 42001 Clause 6.2
- **Lógica de Negocio:**
  - Gestiona objetivos de IA
  - Calcula progreso de objetivos
  - Genera reportes de objetivos
- **ViewModels Asociados:**
  - ✅ **AIObjectivesViewModel** (`governance/AIObjectivesViewModel.java`)
    - Usa: `AIObjectivesBusinessService`
    - Usa también: `AIObjectiveService`
    - Métodos business usados: `getObjectivesReport()`
- **Prompt Origen:** `PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md` - Prompt D.2
- **Entidad Relacionada:** `AIObjective`

---

### 3. MODELS

#### 3.1. ModelAdaptationBusinessService
- **Ubicación:** `com.codeflowx.govern.business.models.ModelAdaptationBusinessService`
- **Propósito:** Gestión de adaptación de modelos según EU AI Act Art. 51-55 (GPAI Downstream Providers)
- **Lógica de Negocio:**
  - Genera recomendaciones de adaptación (Adapters, Fine-Tuning, Quantization, Merge)
  - Calcula árbol de linaje de modelos
  - Evalúa estrategias de adaptación
  - Gestiona configuraciones de adaptadores
- **ViewModels Asociados:**
  - ✅ **ModelAdaptationRecommendationViewModel** (`models/ModelAdaptationRecommendationViewModel.java`)
    - Usa: `ModelAdaptationBusinessService`
    - Usa también: `ModelService`, `ModelAdaptationStrategyService`
    - Métodos business usados: `generateAdaptationRecommendation()`, `getModelLineageTree()`
  - ✅ **ModelLineageTreeViewModel** (`models/ModelLineageTreeViewModel.java`)
    - Usa: `ModelAdaptationBusinessService`
    - Métodos business usados: `getModelLineageTree()`
- **Prompt Origen:** `PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md` - Prompt E.1
- **Entidad Relacionada:** `Model`, `ModelAdaptationStrategy`

---

### 3.1. RAGEvaluationService (⚠️ UBICACIÓN INCORRECTA)
- **Ubicación ACTUAL:** `com.codeflowx.govern.service.rag.RAGEvaluationService` ⚠️
- **Ubicación CORRECTA:** `com.codeflowx.govern.business.rag.RAGEvaluationService` ✅
- **Propósito:** Servicio para evaluación RAG con integración al microservicio Python
- **Lógica de Negocio:**
  - Evalúa pipeline completo de RAG (retrieval, answer, faithfulness, etc.)
  - Evalúa calidad del retrieval
  - Evalúa calidad de respuestas generadas
  - Valida políticas antes de generar respuesta (EU AI Act Art. 10)
  - Ejecuta benchmarking del sistema RAG
  - Gestiona cola de validaciones humanas
  - Calcula nivel de riesgo basado en scores
  - Convierte documentos a formato esperado
  - **Integra con microservicio Python** (puerto 8004 via gateway 8000)
- **ViewModels Asociados:**
  - ✅ **RAGEvaluationViewModel** (`evaluation/rag/RAGEvaluationViewModel.java`)
    - Usa: `RAGEvaluationService`
    - Métodos business usados: `evaluateFullPipeline()`, `validatePolicies()`, `convertToRetrievedDocuments()`
  - ✅ **RAGEvaluationOverviewViewModel** (`evaluation/rag/RAGEvaluationOverviewViewModel.java`)
    - Usa: `RAGEvaluationService`
- **Prompt Origen:** Probablemente `PROMPTS_11_INTEGRACION_QDRANT_MINIO_OPENSEARCH.md` o prompts relacionados con RAG
- **Entidad Relacionada:** `RagEvaluation` (pero este servicio NO es CRUD, es integración)
- **⚠️ PROBLEMA:** Este servicio está en `service.rag` pero debería estar en `business.rag` porque:
  1. Tiene lógica de negocio compleja (cálculo de risk level)
  2. Integra con microservicios externos (Python)
  3. No es un servicio CRUD simple
  4. Es similar a otros servicios en `business` como `ModelAdaptationBusinessService`

---

### 4. INTEGRATIONS (Conectores Externos)

#### 4.1. ExternalIntegrationBusinessService
- **Ubicación:** `com.codeflowx.govern.business.integrations.ExternalIntegrationBusinessService`
- **Propósito:** Servicio base para acceso a plataformas, modelos y datasets externos
- **Lógica de Negocio:**
  - Gestiona integraciones externas
  - Acceso a datos de plataformas externas
- **ViewModels Asociados:**
  - ✅ **ExternalPlatformsViewModel** (`integrations/ExternalPlatformsViewModel.java`)
    - Usa: `ExternalIntegrationBusinessService` y múltiples conectores
- **Prompt Origen:** `PROMPTS_12_CONECTORES_PLATAFORMAS_ENTERPRISE.md`
- **Entidad Relacionada:** `ExternalPlatformIntegration`, `ExternalModel`, `ExternalDataset`

#### 4.2-4.12. Conectores Específicos (11 servicios)
- **AzureMLConnectorService**
- **DataLakeCatalogService**
- **DatabricksConnectorService**
- **FabricConnectorService**
- **IbmWatsonxConnectorService**
- **JiraConnectorService**
- **PurviewConnectorService**
- **SageMakerConnectorService**
- **ServiceNowConnectorService**
- **SnowflakeConnectorService**
- **SparkEvaluationService**
- **VertexAIConnectorService**

**ViewModels Asociados:**
- ✅ **ExternalPlatformsViewModel** (`integrations/ExternalPlatformsViewModel.java`)
  - Usa todos los conectores listados arriba

---

### 5. LOGGING

#### 5.1. ImmutableLoggingBusinessService
- **Ubicación:** `com.codeflowx.govern.business.logging.ImmutableLoggingBusinessService`
- **Propósito:** Gestión de logs inmutables según EU AI Act Art. 19 y Art. 12
- **Lógica de Negocio:**
  - Crea logs inmutables con hash chains
  - Registra eventos automáticamente
  - Gestiona integridad de logs
- **ViewModels Asociados:**
  - ❓ **PENDIENTE IDENTIFICAR**
- **Prompt Origen:** `PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md` - Prompt F.1
- **Entidad Relacionada:** `ImmutableLog`

---

## 📊 Mapeo Completo: Servicios Business → ViewModels

| Servicio Business | ViewModel(s) | Estado | Métodos Business Usados |
|-------------------|--------------|--------|------------------------|
| `ComplianceAssessmentBusinessService` | ⚠️ `InitiateConformityAssessmentViewModel` | 🔴 NO USA (debe migrar) | `createAssessment()` |
| `FriaAssessmentBusinessService` | ⚠️ `FriaWizardViewModel` | 🔴 NO USA (debe migrar) | `createFria()`, `updateFriaSection()`, `calculateCompletenessScore()` |
| `EuRegistrationBusinessService` | ⚠️ `EuRegistrationFormViewModel` | 🔴 NO USA (debe migrar) | `createRegistration()`, `updateSubmissionData()` |
| `QualityManagementSystemBusinessService` | ❓ Pendiente | 🔴 NO IDENTIFICADO | - |
| `TechnicalDocumentationBusinessService` | ❓ Pendiente | 🔴 NO IDENTIFICADO | - |
| `AnnexIIICategoryBusinessService` | ❓ Pendiente | 🔴 NO IDENTIFICADO | - |
| `AICompetenceBusinessService` | ✅ `AICompetenceViewModel` | ✅ IDENTIFICADO | `getCompetenceDashboard()`, `calculateGapAnalysis()` |
| `AIObjectivesBusinessService` | ✅ `AIObjectivesViewModel` | ✅ IDENTIFICADO | `getObjectivesReport()` |
| `ModelAdaptationBusinessService` | ✅ `ModelAdaptationRecommendationViewModel`<br>✅ `ModelLineageTreeViewModel` | ✅ IDENTIFICADO | `generateAdaptationRecommendation()`, `getModelLineageTree()` |
| `RAGEvaluationService` ⚠️ | ✅ `RAGEvaluationViewModel`<br>✅ `RAGEvaluationOverviewViewModel` | ⚠️ UBICACIÓN INCORRECTA | `evaluateFullPipeline()`, `validatePolicies()`, `convertToRetrievedDocuments()` |
| `ExternalIntegrationBusinessService` + 11 conectores | ✅ `ExternalPlatformsViewModel` | ✅ IDENTIFICADO | Múltiples métodos de conectores |
| `ImmutableLoggingBusinessService` | ❓ Pendiente | 🔴 NO IDENTIFICADO | - |

---

## 🔍 Análisis de ViewModels Compliance

### ViewModels en `compliance/` que PODRÍAN usar Business Services:

1. **InitiateConformityAssessmentViewModel**
   - Probable uso: `ComplianceAssessmentBusinessService.createAssessment()`

2. **ConformityReviewViewModel**
   - Probable uso: `ComplianceAssessmentBusinessService.executeStep2QmsCheck()`, `executeStep3DocReview()`, etc.

3. **ConformityDeclarationManagerViewModel**
   - Probable uso: `ComplianceAssessmentBusinessService.finalizeAssessment()`

4. **FriaWizardViewModel**
   - Probable uso: `FriaAssessmentBusinessService.createFria()`, `updateFriaSection()`, `calculateCompletenessScore()`

5. **HighRiskClassifierViewModel**
   - Probable uso: `AnnexIIICategoryBusinessService` para clasificar sistemas

6. **ReviewQmsGapsViewModel**
   - Probable uso: `QualityManagementSystemBusinessService.calculateQmsComplianceScore()`

7. **CompleteDocumentationViewModel**
   - Probable uso: `TechnicalDocumentationBusinessService.calculateDocumentationScore()`

8. **EURegistrationStatusViewModel**
   - Probable uso: `EuRegistrationBusinessService.submitToEuDatabase()`, `getRegistrationStatus()`

9. **EuRegistrationFormViewModel**
   - Probable uso: `EuRegistrationBusinessService.createRegistration()`, `updateSubmissionData()`

10. **ReviewRegistrationPackageViewModel**
    - Probable uso: `EuRegistrationBusinessService.validateSubmissionData()`

11. **FixValidationErrorsViewModel**
    - Probable uso: `EuRegistrationBusinessService.getValidationErrors()`

12. **ManualResolutionViewModel**
    - Probable uso: `EuRegistrationBusinessService.manualResolve()`

13. **SectorDashboardViewModel**
    - Probable uso: `QualityManagementSystemBusinessService`, `ComplianceAssessmentBusinessService`

14. **FinalReviewViewModel**
    - Probable uso: `ComplianceAssessmentBusinessService.finalizeAssessment()`

---

## 📝 Próximos Pasos

### 1. Verificación de ViewModels Compliance
- [ ] Revisar cada ViewModel en `compliance/` para identificar uso de Business Services
- [ ] Documentar métodos business usados en cada ViewModel
- [ ] Verificar que la lógica de negocio coincide con los prompts

### 2. Verificación de ViewModels FRIA
- [ ] Revisar ViewModels en `fria/` para identificar uso de `FriaAssessmentBusinessService`

### 3. Verificación de ViewModels EU Registration
- [ ] Revisar ViewModels en `euregistration/` para identificar uso de `EuRegistrationBusinessService`

### 4. Verificación de ViewModels Logging
- [ ] Buscar ViewModels que usen `ImmutableLoggingBusinessService`

### 5. Documentación de Lógica de Negocio
- [ ] Para cada ViewModel identificado, documentar:
  - Métodos business usados
  - Flujo de negocio
  - Referencia al prompt origen
  - Entidades relacionadas

---

## 📚 Referencias

### Prompts de Compliance:
- `PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md` - Entidades y servicios principales
- `PROMPTS_12_CONECTORES_PLATAFORMAS_ENTERPRISE.md` - Conectores externos
- `INDEX_EU_AI_ACT_COMPLIANCE.md` - Índice general de compliance

### Entidades Relacionadas:
- `ComplianceAssessment` - Evaluaciones de conformidad
- `FriaAssessment` - Evaluaciones de impacto en derechos fundamentales
- `EuRegistration` - Registros en Base de Datos UE
- `AIActTechnicalDocumentation` - Documentación técnica
- `AICCompetence` - Competencias
- `AIObjective` - Objetivos IA
- `Model` - Modelos
- `ModelAdaptationStrategy` - Estrategias de adaptación
- `ImmutableLog` - Logs inmutables

---

## ⚠️ Notas Importantes

1. **Los Business Services contienen lógica de negocio compleja** que NO debe migrarse a servicios CRUD simples
2. **Los ViewModels deben mantener el uso de Business Services** para operaciones de negocio
3. **Los servicios CRUD (`*Service`) se usan para operaciones básicas** (create, update, delete, find)
4. **Los Business Services se usan para operaciones complejas** (cálculos, validaciones, integraciones)
5. **Algunos ViewModels pueden usar AMBOS tipos de servicios:**
   - Business Services para lógica de negocio
   - Services CRUD para operaciones básicas de entidades

## 🔴 Hallazgos Críticos

### 1. ViewModels que NO están usando Business Services (deben migrarse):

1. **InitiateConformityAssessmentViewModel**
   - ❌ Actualmente usa: `BusinessService` genérico + BPMN
   - ✅ Debe usar: `ComplianceAssessmentBusinessService.createAssessment()`

2. **FriaWizardViewModel**
   - ❌ Actualmente usa: `BusinessService` genérico + `ProjectService`
   - ✅ Debe usar: `FriaAssessmentBusinessService` (create, update, calculate)

3. **EuRegistrationFormViewModel**
   - ❌ Actualmente usa: `BusinessService` genérico + BPMN
   - ✅ Debe usar: `EuRegistrationBusinessService` (create, update)

### 2. Servicios con Ubicación Incorrecta:

1. **RAGEvaluationService**
   - ⚠️ **Ubicación actual:** `com.codeflowx.govern.service.rag.RAGEvaluationService`
   - ✅ **Ubicación correcta:** `com.codeflowx.govern.business.rag.RAGEvaluationService`
   - **Razón:** Tiene lógica de negocio compleja e integra con microservicios Python
   - **ViewModels afectados:**
     - `RAGEvaluationViewModel` - Debe actualizar import
     - `RAGEvaluationOverviewViewModel` - Debe actualizar import
   - **Acción requerida:**
     1. Mover archivo de `service/rag/` a `business/rag/`
     2. Cambiar package a `com.codeflowx.govern.business.rag`
     3. Renombrar a `RAGEvaluationBusinessService` (opcional, pero recomendado para consistencia)
     4. Actualizar imports en ViewModels

### Acción Requerida:

1. **ViewModels deben ser actualizados** para usar los Business Services correspondientes en lugar de `BusinessService` genérico
2. **RAGEvaluationService debe moverse** a `business/rag/` y renombrarse a `RAGEvaluationBusinessService` para mantener consistencia con otros servicios business

---

**Última actualización:** 2025-11-24
**Estado:** 🔴 En progreso - Pendiente verificación completa de ViewModels
