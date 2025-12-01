# REVISIÓN COMPLETA VIEWMODELS - MAPEO CON PROMPTS

**Fecha:** 25 de noviembre de 2025
**Objetivo:** Revisar y mapear TODOS los ViewModels (91 totales) con sus prompts correspondientes, ZULs, servicios, clases de negocio y llamadas a microservicios Python.

---

## 📊 RESUMEN EJECUTIVO

| Categoría | Total ViewModels | Con Prompt | Sin Prompt | % Cobertura |
|-----------|-----------------|-----------|-----------|------------|
| **Governance** | 16 | 16 | 0 | 100% |
| **Compliance** | 12 | 12 | 0 | 100% |
| **FRIA** | 4 | 4 | 0 | 100% |
| **EU Registration** | 4 | 4 | 0 | 100% |
| **Models** | 10 | 8 | 2 | 80% |
| **RAG** | 4 | 4 | 0 | 100% |
| **Prompts** | 3 | 3 | 0 | 100% |
| **Training** | 2 | 2 | 0 | 100% |
| **Serving** | 1 | 1 | 0 | 100% |
| **Projects** | 2 | 2 | 0 | 100% |
| **Analytics** | 9 | 5 | 4 | 55.6% |
| **Agents** | 4 | 2 | 2 | 50% |
| **Monitoring** | 1 | 1 | 0 | 100% |
| **Integrations** | 1 | 1 | 0 | 100% |
| **Incident** | 4 | 4 | 0 | 100% |
| **Infrastructure** | 2 | 2 | 0 | 100% |
| **Providers** | 2 | 2 | 0 | 100% |
| **Catalog** | 2 | 2 | 0 | 100% |
| **Core** | 5 | 3 | 2 | 60% |
| **Dashboard** | 1 | 1 | 0 | 100% |
| **Education** | 1 | 1 | 0 | 100% |
| **TOTAL** | **91** | **76** | **15** | **83.5%** |

---

## 📋 VIEWMODELS POR CATEGORÍA

### 1. GOVERNANCE (16 ViewModels)

#### 1.1 AIObjectivesViewModel
- **Prompt:** PROMPTS_05 - B.1
- **ZUL:** `console/gobierno/governance/ai_objectives_management.zul`
- **BusinessService:** AIObjectivesBusinessService
- **Micros Python:** ❌

#### 1.2 AICompetenceViewModel
- **Prompt:** PROMPTS_05 - B.2
- **ZUL:** `console/gobierno/governance/ai_competence_management.zul`
- **BusinessService:** AICompetenceBusinessService
- **Micros Python:** ❌

#### 1.3 ComplianceAiActViewModel
- **Prompt:** PROMPTS_05 - A.1
- **ZUL:** `console/platform/governance/compliance/page.zul` (inferida)
- **BusinessService:** ❌
- **Micros Python:** ⚠️ RAG Service (pendiente)

#### 1.4 ComplianceAutomatedChecksViewModel
- **Prompt:** PROMPTS_05 - A.2
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 1.5 GovernanceDashboardViewModel
- **Prompt:** PROMPTS_05 - C.1
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 1.6 GovernanceOverviewViewModel
- **Prompt:** PROMPTS_05 - C.2
- **ZUL:** `console/gobierno/governance/governance-overview.zul`
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 1.7 GovernanceDetailViewModel
- **Prompt:** PROMPTS_05 - C.3
- **ZUL:** `console/gobierno/governance/governance-detail.zul`
- **BusinessService:** ❌ (usa funciones/procedimientos)
- **Micros Python:** ❌

#### 1.8 EthicsAssessmentsViewModel
- **Prompt:** PROMPTS_05 - A.3
- **ZUL:** `console/platform/views/governance/ethics-reviews-dashboard-overview.zul` (inferida)
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 1.9 EthicsCommitteeViewModel
- **Prompt:** PROMPTS_05 - A.3 (extendido)
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 1.10 EthicsImpactViewModel
- **Prompt:** PROMPTS_05 - A.3 (extendido)
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 1.11 EthicsMitigationViewModel
- **Prompt:** PROMPTS_05 - A.3 (extendido)
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 1.12 EthicsViolationsViewModel
- **Prompt:** PROMPTS_05 - A.3 (extendido)
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 1.13 AIMSImprovementViewModel
- **Prompt:** PROMPTS_05 - A.4
- **ZUL:** `console/platform/governance/aims_improvement.zul`
- **BusinessService:** ⚠️ Requiere refactorización (usa EntityManager)
- **Micros Python:** ❌

#### 1.14 AIMSNonConformityViewModel
- **Prompt:** PROMPTS_05 - A.4
- **ZUL:** `console/platform/governance/aims_nonconformity.zul`
- **BusinessService:** ⚠️ Requiere refactorización (usa EntityManager)
- **Micros Python:** ❌

#### 1.15 AIMSPerformanceViewModel
- **Prompt:** PROMPTS_05 - A.4
- **ZUL:** `console/platform/governance/aims_performance.zul`
- **BusinessService:** ⚠️ Requiere refactorización (usa EntityManager)
- **Micros Python:** ❌

#### 1.16 ISO42001ControlsViewModel
- **Prompt:** PROMPTS_05 - A.5
- **ZUL:** `console/platform/governance/iso42001_controls.zul`
- **BusinessService:** ⚠️ Requiere refactorización (usa EntityManager)
- **Micros Python:** ❌

---

### 2. COMPLIANCE (12 ViewModels)

#### 2.1 HighRiskClassifierViewModel
- **Prompt:** PROMPTS_03 - C.1
- **ZUL:** No identificada explícitamente
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-prompt-governance (pendiente - INC-002)

#### 2.2 FriaWizardViewModel
- **Prompt:** PROMPTS_03 - C.2
- **ZUL:** No identificada explícitamente
- **BusinessService:** ❌
- **Micros Python:** ✅ leka-fria-generator (8012)

#### 2.3 AIActDocumentationGeneratorViewModel
- **Prompt:** PROMPTS_02 - leka-technical-documentation-generator
- **ZUL:** `console/gobierno/compliance/ai-act-documentation-generator.zul`
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-technical-documentation-generator (8008) - pendiente

#### 2.4 CompleteDocumentationViewModel
- **Prompt:** PROMPTS_02 - leka-technical-documentation-generator
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-technical-documentation-generator (8008) - pendiente

#### 2.5 InitiateConformityAssessmentViewModel
- **Prompt:** PROMPTS_02 - leka-conformity-assessment
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-conformity-assessment (8009) - pendiente

#### 2.6 ConformityDeclarationManagerViewModel
- **Prompt:** PROMPTS_02 - leka-eu-declaration-generator
- **ZUL:** `console/gobierno/compliance/conformity-declaration-manager.zul`
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-eu-declaration-generator (8010) - pendiente

#### 2.7 ConformityReviewViewModel
- **Prompt:** PROMPTS_02 - leka-conformity-assessment
- **ZUL:** `console/gobierno/compliance/conformity-review-form.zul`
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-conformity-assessment (8009) - pendiente

#### 2.8 ApproveConformityAssessmentViewModel
- **Prompt:** PROMPTS_02 - leka-conformity-assessment
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-conformity-assessment (8009) - pendiente

#### 2.9 ReviewQmsGapsViewModel
- **Prompt:** PROMPTS_02 - leka-conformity-assessment
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-conformity-assessment (8009) - pendiente

#### 2.10 FinalReviewViewModel
- **Prompt:** PROMPTS_02 - leka-conformity-assessment
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-conformity-assessment (8009) - pendiente

#### 2.11 EURegistrationStatusViewModel
- **Prompt:** PROMPTS_05 - A.6
- **ZUL:** `console/gobierno/compliance/eu-registration-status.zul`
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 2.12 SectorDashboardViewModel
- **Prompt:** PROMPTS_26 - Metamodelo Sectorial
- **ZUL:** `console/gobierno/compliance/sector-dashboard.zul`
- **BusinessService:** ❌
- **Micros Python:** ❌

---

### 3. FRIA (4 ViewModels)

#### 3.1 CompleteMissingElementsViewModel
- **Prompt:** PROMPTS_03 - C.2 (extendido)
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-fria-generator (8012) - pendiente

#### 3.2 DefineModificationsViewModel
- **Prompt:** PROMPTS_03 - C.2 (extendido)
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-fria-generator (8012) - pendiente

#### 3.3 DeployerApprovalViewModel
- **Prompt:** PROMPTS_03 - C.2 (extendido)
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 3.4 EnhancedReviewViewModel
- **Prompt:** PROMPTS_03 - C.2 (extendido)
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-fria-generator (8012) - pendiente

---

### 4. EU REGISTRATION (4 ViewModels)

#### 4.1 EuRegistrationFormViewModel
- **Prompt:** PROMPTS_05 - A.6
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 4.2 FixValidationErrorsViewModel
- **Prompt:** PROMPTS_05 - A.6 (extendido)
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 4.3 ManualResolutionViewModel
- **Prompt:** PROMPTS_05 - A.6 (extendido)
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 4.4 ReviewRegistrationPackageViewModel
- **Prompt:** PROMPTS_05 - A.6 (extendido)
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

---

### 5. MODELS (10 ViewModels)

#### 5.1 ModelsOverviewViewModel
- **Prompt:** PROMPTS_05 - C.4
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 5.2 ModelsDetailViewModel
- **Prompt:** PROMPTS_05 - C.4
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 5.3 ModelApprovalWorkflowViewModel
- **Prompt:** PROMPTS_04 - BPMN workflows
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-model-wrapper, leka-bias-detection-service (pendiente)

#### 5.4 ModelAdaptationRecommendationViewModel
- **Prompt:** PROMPTS_06 - MLOPS Adapters Finetuning
- **ZUL:** No identificada
- **BusinessService:** ModelAdaptationBusinessService
- **Micros Python:** ⚠️ leka-model-wrapper (pendiente)

#### 5.5 DefineAdapterParametersViewModel
- **Prompt:** PROMPTS_06 - MLOPS Adapters Finetuning
- **ZUL:** No identificada
- **BusinessService:** ModelAdaptationBusinessService
- **Micros Python:** ❌

#### 5.6 ReviewAdapterApprovalViewModel
- **Prompt:** PROMPTS_06 - MLOPS Adapters Finetuning
- **ZUL:** `console/gobierno/models/final_finetuning_approval.zul` (inferida)
- **BusinessService:** ModelAdaptationBusinessService
- **Micros Python:** ❌

#### 5.7 JustifyFineTuningViewModel
- **Prompt:** PROMPTS_06 - MLOPS Adapters Finetuning
- **ZUL:** `console/gobierno/models/justify_finetuning.zul`
- **BusinessService:** ModelAdaptationBusinessService
- **Micros Python:** ❌

#### 5.8 FinalFineTuningApprovalViewModel
- **Prompt:** PROMPTS_06 - MLOPS Adapters Finetuning
- **ZUL:** `console/gobierno/models/final_finetuning_approval.zul`
- **BusinessService:** ModelAdaptationBusinessService
- **Micros Python:** ❌

#### 5.9 FixComplianceGapsViewModel
- **Prompt:** PROMPTS_06 - MLOPS Adapters Finetuning
- **ZUL:** No identificada
- **BusinessService:** ModelAdaptationBusinessService
- **Micros Python:** ❌

#### 5.10 ModelLineageTreeViewModel
- **Prompt:** PROMPTS_06 - MLOPS Adapters Finetuning
- **ZUL:** No identificada
- **BusinessService:** ModelAdaptationBusinessService
- **Micros Python:** ❌

---

### 6. RAG (4 ViewModels)

#### 6.1 RagSystemsOverviewViewModel
- **Prompt:** PROMPTS_11 - Integración Qdrant MinIO OpenSearch
- **ZUL:** `console/gobierno/rag/rag-systems-overview.zul`
- **BusinessService:** ❌
- **Micros Python:** ✅ leka-rag-evaluation (vía AIGovernanceClient)

#### 6.2 RagSystemsDetailViewModel
- **Prompt:** PROMPTS_11 - Integración Qdrant MinIO OpenSearch
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ✅ leka-rag-evaluation (vía AIGovernanceClient)

#### 6.3 RagClientPoliciesOverviewViewModel
- **Prompt:** PROMPTS_11 - Integración Qdrant MinIO OpenSearch
- **ZUL:** `console/gobierno/rag/rag-client-policies-overview.zul`
- **BusinessService:** ❌
- **Micros Python:** ✅ leka-rag-evaluation (vía AIGovernanceClient)

#### 6.4 RagClientPoliciesDetailViewModel
- **Prompt:** PROMPTS_11 - Integración Qdrant MinIO OpenSearch
- **ZUL:** `console/gobierno/rag/rag-client-policies-detail.zul`
- **BusinessService:** ❌
- **Micros Python:** ✅ leka-rag-evaluation (vía AIGovernanceClient)

---

### 7. PROMPTS (3 ViewModels)

#### 7.1 PromptsOverviewViewModel
- **Prompt:** PROMPTS_01 - leka-prompt-governance (extensión)
- **ZUL:** `console/platform/prompts/registry/page.zul`
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-prompt-governance (8003) - pendiente

#### 7.2 PromptsDetailViewModel
- **Prompt:** PROMPTS_01 - leka-prompt-governance (extensión)
- **ZUL:** `console/gobierno/prompts/prompts-detail.zul`
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-prompt-governance (8003) - pendiente

#### 7.3 PromptApprovalWorkflowViewModel
- **Prompt:** PROMPTS_04 - BPMN workflows
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-prompt-governance (8003) - pendiente

---

### 8. TRAINING (2 ViewModels)

#### 8.1 TrainingDashboardViewModel
- **Prompt:** PROMPTS_06 - MLOPS Adapters Finetuning
- **ZUL:** `console/platform/training/governance/dashboard.zul`
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 8.2 ExperimentsDetailViewModel
- **Prompt:** PROMPTS_06 - MLOPS Adapters Finetuning
- **ZUL:** `console/platform/training/governance/overview.zul` (inferida)
- **BusinessService:** ❌
- **Micros Python:** ❌

---

### 9. SERVING (1 ViewModel)

#### 9.1 ServingDashboardViewModel
- **Prompt:** PROMPTS_46 - Discovery Serving
- **ZUL:** `console/gobierno/serving/serving-dashboard.zul`
- **BusinessService:** ❌
- **Micros Python:** ❌

---

### 10. PROJECTS (2 ViewModels)

#### 10.1 ProjectsDashboardViewModel
- **Prompt:** PROMPTS_05 - J1.4 (Project AI Inventory Extension)
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 10.2 ProjectAIInventoryViewModel
- **Prompt:** PROMPTS_05 - J1.4
- **ZUL:** `console/platform/projects/project-ai-inventory.zul`
- **BusinessService:** ❌
- **Micros Python:** ❌

---

### 11. ANALYTICS (9 ViewModels)

#### 11.1 AnalyticsOverviewViewModel
- **Prompt:** PROMPTS_FAAS_METRICAS_UI
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-llm-evaluation (pendiente)

#### 11.2 AnalyticsBiasViewModel
- **Prompt:** PROMPTS_FAAS_METRICAS_UI
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-bias-detection-service (pendiente)

#### 11.3 AnalyticsFairnessViewModel
- **Prompt:** PROMPTS_FAAS_METRICAS_UI
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-bias-detection-service (pendiente)

#### 11.4 AnalyticsTransparencyViewModel
- **Prompt:** PROMPTS_FAAS_METRICAS_UI
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-llm-evaluation (pendiente)

#### 11.5 AnalyticsAccountabilityViewModel
- **Prompt:** PROMPTS_FAAS_METRICAS_UI
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 11.6 AnalyticsImpactViewModel
- **Prompt:** PROMPTS_FAAS_METRICAS_UI
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 11.7 AnalyticsMetricViewModel
- **Prompt:** PROMPTS_FAAS_METRICAS_UI
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-llm-evaluation (pendiente)

#### 11.8 AnalyticsReportViewModel
- **Prompt:** PROMPTS_FAAS_METRICAS_UI
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 11.9 AnalyticsTrendsViewModel
- **Prompt:** PROMPTS_FAAS_METRICAS_UI
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-llm-evaluation (pendiente)

---

### 12. AGENTS (4 ViewModels)

#### 12.1 AgentsDashboardViewModel
- **Prompt:** PROMPTS_FAAS_MICROS
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-agent-monitoring (pendiente)

#### 12.2 AgentsDetailViewModel
- **Prompt:** PROMPTS_FAAS_MICROS
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-agent-monitoring (pendiente)

#### 12.3 AgentApprovalWorkflowViewModel
- **Prompt:** PROMPTS_04 - BPMN workflows
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-agent-monitoring (pendiente)

#### 12.4 AgentDecisionsLogViewModel
- **Prompt:** PROMPTS_FAAS_MICROS
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-agent-monitoring (pendiente)

---

### 13. MONITORING (1 ViewModel)

#### 13.1 MonitoringDashboardViewModel
- **Prompt:** PROMPTS_FAAS_METRICAS_UI
- **ZUL:** `console/gobierno/monitoring/monitoring-overview.zul`
- **BusinessService:** ❌
- **Micros Python:** ⚠️ leka-llm-evaluation, leka-agent-monitoring (pendiente)

---

### 14. INTEGRATIONS (1 ViewModel)

#### 14.1 ExternalPlatformsViewModel
- **Prompt:** PROMPTS_12 - Conectores Plataformas Enterprise
- **ZUL:** `console/gobierno/integrations/external-platforms.zul`
- **BusinessService:** ❌
- **Micros Python:** ❌

---

### 15. INCIDENT (4 ViewModels)

#### 15.1 DocumentIncidentDetailsViewModel
- **Prompt:** PROMPTS_04 - BPMN workflows
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 15.2 RootCauseAnalysisViewModel
- **Prompt:** PROMPTS_04 - BPMN workflows
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 15.3 DefineCorrectiveActionsViewModel
- **Prompt:** PROMPTS_04 - BPMN workflows
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 15.4 VerifyIncidentResolutionViewModel
- **Prompt:** PROMPTS_04 - BPMN workflows
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

---

### 16. INFRASTRUCTURE (2 ViewModels)

#### 16.1 InfrastructureOverviewViewModel
- **Prompt:** PROMPTS_10 - Python Multi Framework Consolidado
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 16.2 InfrastructureDetailViewModel
- **Prompt:** PROMPTS_10 - Python Multi Framework Consolidado
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

---

### 17. PROVIDERS (2 ViewModels)

#### 17.1 ProvidersOverviewViewModel
- **Prompt:** PROMPTS_12 - Conectores Plataformas Enterprise
- **ZUL:** `console/gobierno/providers/providers-overview.zul`
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 17.2 ProvidersDetailViewModel
- **Prompt:** PROMPTS_12 - Conectores Plataformas Enterprise
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

---

### 18. CATALOG (2 ViewModels)

#### 18.1 CatalogDashboardViewModel
- **Prompt:** PROMPTS_46 - Discovery Serving
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 18.2 CatalogModelsViewModel
- **Prompt:** PROMPTS_46 - Discovery Serving
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

---

### 19. CORE (5 ViewModels)

#### 19.1 AdminDashboardViewModel
- **Prompt:** PROMPTS_18 - Postgres Governance
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 19.2 MenuViewModel
- **Prompt:** PROMPTS_00 - Playbook
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 19.3 SecurityAuditViewModel
- **Prompt:** PROMPTS_18 - Postgres Governance
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 19.4 SystemHealthViewModel
- **Prompt:** PROMPTS_18 - Postgres Governance
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 19.5 UserActivityViewModel
- **Prompt:** PROMPTS_18 - Postgres Governance
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

---

### 20. DASHBOARD (1 ViewModel)

#### 20.1 DashboardViewModel
- **Prompt:** PROMPTS_FAAS_METRICAS_UI
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

#### 20.2 MainDashboardViewModel
- **Prompt:** PROMPTS_FAAS_METRICAS_UI
- **ZUL:** No identificada
- **BusinessService:** ❌
- **Micros Python:** ❌

---

### 21. EDUCATION (1 ViewModel)

#### 21.1 EduConsentHubViewModel
- **Prompt:** PROMPTS_05 - Education Consent
- **ZUL:** `console/gobierno/compliance/education/edu-consent-hub.zul`
- **BusinessService:** ❌
- **Micros Python:** ❌

---

## 🔍 VIEWMODELS SIN PROMPT ASIGNADO (15 ViewModels)

### Analytics (4):
1. AnalyticsAccountabilityViewModel
2. AnalyticsImpactViewModel
3. AnalyticsReportViewModel
4. AnalyticsTrendsViewModel (parcial - tiene prompt pero no confirmado)

### Agents (2):
5. AgentApprovalWorkflowViewModel (parcial - tiene prompt BPMN pero no específico)
6. AgentDecisionsLogViewModel

### Core (2):
7. MenuViewModel (parcial - tiene prompt genérico)
8. AdminDashboardViewModel (parcial - tiene prompt genérico)

### Models (2):
9. ModelsOverviewViewModel (parcial - tiene prompt genérico)
10. ModelsDetailViewModel (parcial - tiene prompt genérico)

### Dashboard (1):
11. DashboardViewModel (parcial - tiene prompt genérico)

---

## 📄 PROMPTS SIN VIEWMODEL CORRESPONDIENTE

### PROMPTS_15_GOVERNANCE_API_REST.md
- **Tipo:** API REST (no requiere ViewModel)
- **Estado:** ⏳ Pendiente

### PROMPTS_17_ADAPTADOR_MCP.md
- **Tipo:** Adaptador MCP (no requiere ViewModel)
- **Estado:** ⏳ Pendiente

---

## 🗂️ ZULs SIN VIEWMODEL CONFIRMADO

1. `console/platform/views/governance/dataset-quality-dashboard-overview.zul` - Sin ViewModel
2. Múltiples ZULs de BPMN forms sin ViewModels específicos

---

## 🔧 CLASES DE NEGOCIO IDENTIFICADAS

### Business Services en uso (3):
1. **AIObjectivesBusinessService** - AIObjectivesViewModel
2. **AICompetenceBusinessService** - AICompetenceViewModel
3. **ModelAdaptationBusinessService** - ModelAdaptationRecommendationViewModel y relacionados

### Business Services faltantes (requeridos):
4. **AIMSImprovementBusinessService** - AIMSImprovementViewModel
5. **AIMSNonConformityBusinessService** - AIMSNonConformityViewModel
6. **AIMSPerformanceBusinessService** - AIMSPerformanceViewModel
7. **ISO42001ControlsBusinessService** - ISO42001ControlsViewModel

---

## 🐍 LLAMADAS A MICROSERVICIOS PYTHON (AIGovernanceClient)

### Integraciones implementadas (2):
1. ✅ **FriaWizardViewModel → leka-fria-generator (8012)**
   - **Cliente:** `AIGovernanceClient.friaGenerator()`
   - **Métodos:** `generateAssessment()`, `analyzeFundamentalRights()`, `integrateWithDPIA()`, `crossValidate()`
   - **Estado:** ✅ Implementado

2. ✅ **RagSystemsViewModel → leka-rag-evaluation (8004)**
   - **Cliente:** `AIGovernanceClient.ragEvaluation()`
   - **Métodos:** `evaluateRetrieval()`, `evaluateAnswer()`, `evaluateContextRelevance()`, `evaluateFullPipeline()`
   - **Estado:** ✅ Implementado (vía AIGovernanceClient)

---

### Integraciones pendientes (20 ViewModels):

#### 1. ComplianceAiActViewModel → leka-rag-evaluation (8004)
- **Cliente:** `AIGovernanceClient.ragEvaluation()`
- **Método requerido:** `evaluateFullPipeline()` o query personalizado
- **Uso:** Cargar requisitos del EU AI Act desde documentación oficial
- **Estado:** ⚠️ Documentado en código (método `loadRequirementsFromRAG()`), no implementado
- **Prioridad:** 🟡 Media

#### 2. HighRiskClassifierViewModel → leka-prompt-governance (8003)
- **Cliente:** `AIGovernanceClient.promptGovernance()`
- **Método requerido:** `evaluateSafety()` o nuevo endpoint de clasificación
- **Uso:** Validación de confianza de sugerencia IA (INC-002)
- **Estado:** ⚠️ Pendiente (TODO en código línea 394)
- **Prioridad:** 🔴 Alta

#### 3. AIActDocumentationGeneratorViewModel → leka-technical-documentation-generator (8008)
- **Cliente:** ⚠️ **NO DISPONIBLE en AIGovernanceClient** (requiere nuevo cliente)
- **Microservicio:** leka-technical-documentation-generator (puerto 8008)
- **Métodos requeridos:** Generación de documentación técnica Anexo IV
- **Estado:** ⚠️ Microservicio existe pero no hay cliente en AIGovernanceClient
- **Prioridad:** 🔴 Alta

#### 4. CompleteDocumentationViewModel → leka-technical-documentation-generator (8008)
- **Cliente:** ⚠️ **NO DISPONIBLE en AIGovernanceClient** (requiere nuevo cliente)
- **Microservicio:** leka-technical-documentation-generator (puerto 8008)
- **Métodos requeridos:** Generación de documentación técnica completa
- **Estado:** ⚠️ Microservicio existe pero no hay cliente en AIGovernanceClient
- **Prioridad:** 🔴 Alta

#### 5. InitiateConformityAssessmentViewModel → leka-conformity-assessment (8009)
- **Cliente:** `AIGovernanceClient.conformityAssessment()`
- **Métodos requeridos:** `assessAnnexVI()`, `generateChecklist()`, `validateEvidence()`
- **Uso:** Iniciar evaluación de conformidad Anexo VI
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🔴 Alta

#### 6. ConformityReviewViewModel → leka-conformity-assessment (8009)
- **Cliente:** `AIGovernanceClient.conformityAssessment()`
- **Métodos requeridos:** `assessAnnexVI()`, `validateEvidence()`
- **Uso:** Revisar evaluación de conformidad
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🔴 Alta

#### 7. ApproveConformityAssessmentViewModel → leka-conformity-assessment (8009)
- **Cliente:** `AIGovernanceClient.conformityAssessment()`
- **Métodos requeridos:** `assessAnnexVI()`, `validateEvidence()`
- **Uso:** Aprobar evaluación de conformidad
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🔴 Alta

#### 8. ReviewQmsGapsViewModel → leka-conformity-assessment (8009)
- **Cliente:** `AIGovernanceClient.conformityAssessment()`
- **Métodos requeridos:** `assessAnnexVI()`, `generateChecklist()`
- **Uso:** Revisar gaps del QMS
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🔴 Alta

#### 9. FinalReviewViewModel → leka-conformity-assessment (8009)
- **Cliente:** `AIGovernanceClient.conformityAssessment()`
- **Métodos requeridos:** `assessAnnexVI()`, `validateEvidence()`
- **Uso:** Revisión final de conformidad
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🔴 Alta

#### 10. ConformityDeclarationManagerViewModel → leka-eu-declaration-generator (8010)
- **Cliente:** ⚠️ **NO DISPONIBLE en AIGovernanceClient** (requiere nuevo cliente)
- **Microservicio:** leka-eu-declaration-generator (puerto 8010)
- **Métodos requeridos:** Generación de declaración UE de conformidad Anexo V
- **Estado:** ⚠️ Microservicio existe pero no hay cliente en AIGovernanceClient
- **Prioridad:** 🔴 Alta

#### 11. CompleteMissingElementsViewModel → leka-fria-generator (8012)
- **Cliente:** `AIGovernanceClient.friaGenerator()`
- **Métodos requeridos:** `generateAssessment()`, `analyzeFundamentalRights()`
- **Uso:** Completar elementos faltantes en FRIA
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🟡 Media

#### 12. DefineModificationsViewModel → leka-fria-generator (8012)
- **Cliente:** `AIGovernanceClient.friaGenerator()`
- **Métodos requeridos:** `generateAssessment()`, `crossValidate()`
- **Uso:** Definir modificaciones en FRIA
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🟡 Media

#### 13. EnhancedReviewViewModel → leka-fria-generator (8012)
- **Cliente:** `AIGovernanceClient.friaGenerator()`
- **Métodos requeridos:** `generateAssessment()`, `crossValidate()`, `integrateWithDPIA()`
- **Uso:** Revisión mejorada de FRIA
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🟡 Media

#### 14. PromptsOverviewViewModel → leka-prompt-governance (8003)
- **Cliente:** `AIGovernanceClient.promptGovernance()`
- **Métodos requeridos:** `evaluateSafety()`, `evaluateEffectiveness()`, `detectPiiLeakage()`
- **Uso:** Evaluación de seguridad y efectividad de prompts
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🟡 Media

#### 15. PromptsDetailViewModel → leka-prompt-governance (8003)
- **Cliente:** `AIGovernanceClient.promptGovernance()`
- **Métodos requeridos:** `evaluateSafety()`, `evaluateEffectiveness()`, `detectPiiLeakage()`, `compareVersions()`, `analyzeCost()`
- **Uso:** Análisis detallado de prompts con comparación de versiones
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🟡 Media

#### 16. PromptApprovalWorkflowViewModel → leka-prompt-governance (8003)
- **Cliente:** `AIGovernanceClient.promptGovernance()`
- **Métodos requeridos:** `evaluateSafety()`, `validateTemplate()`, `validateOutputFormat()`
- **Uso:** Validación de prompts en workflow de aprobación
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🟡 Media

#### 17. ModelApprovalWorkflowViewModel → leka-model-wrapper (8006) + leka-bias-detection-service (8001)
- **Clientes:** `AIGovernanceClient.modelWrapper()`, `AIGovernanceClient.biasDetection()`
- **Métodos requeridos:**
  - `modelWrapper().invoke()` - Validación de modelo
  - `biasDetection().analyzeBias()` - Análisis de sesgo
  - `biasDetection().testRobustness()` - Pruebas de robustez adversarial
- **Uso:** Validación completa de modelo en workflow de aprobación
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🔴 Alta

#### 18. ModelAdaptationRecommendationViewModel → leka-model-wrapper (8006)
- **Cliente:** `AIGovernanceClient.modelWrapper()`
- **Métodos requeridos:** `listAvailable()`, `compareResponses()`
- **Uso:** Comparar modelos disponibles para adaptación
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🟡 Media

#### 19. AnalyticsOverviewViewModel → leka-llm-evaluation (8002)
- **Cliente:** `AIGovernanceClient.llmEvaluation()`
- **Métodos requeridos:** `evaluateHallucination()`, `evaluateQuality()`, `benchmarkEvaluations()`
- **Uso:** Métricas generales de evaluación LLM
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🟡 Media

#### 20. AnalyticsBiasViewModel → leka-bias-detection-service (8001)
- **Cliente:** `AIGovernanceClient.biasDetection()`
- **Métodos requeridos:** `analyzeBias()`, `benchmarkFairness()`, `detectDrift()`
- **Uso:** Análisis de sesgo y equidad
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🟡 Media

#### 21. AnalyticsFairnessViewModel → leka-bias-detection-service (8001)
- **Cliente:** `AIGovernanceClient.biasDetection()`
- **Métodos requeridos:** `analyzeBias()`, `benchmarkFairness()`, `explainPredictions()`
- **Uso:** Análisis de equidad y explicabilidad
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🟡 Media

#### 22. AnalyticsTransparencyViewModel → leka-llm-evaluation (8002)
- **Cliente:** `AIGovernanceClient.llmEvaluation()`
- **Métodos requeridos:** `evaluateFactualGrounding()`, `evaluateConsistency()`
- **Uso:** Evaluación de transparencia y consistencia
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🟡 Media

#### 23. AnalyticsMetricViewModel → leka-llm-evaluation (8002)
- **Cliente:** `AIGovernanceClient.llmEvaluation()`
- **Métodos requeridos:** `evaluateHallucination()`, `evaluateToxicity()`, `evaluateBiasText()`, `evaluateQuality()`
- **Uso:** Métricas completas de evaluación LLM
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🟡 Media

#### 24. AnalyticsTrendsViewModel → leka-llm-evaluation (8002)
- **Cliente:** `AIGovernanceClient.llmEvaluation()`
- **Métodos requeridos:** `benchmarkEvaluations()`, `abTestPrompts()`
- **Uso:** Análisis de tendencias y benchmarks
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🟡 Media

#### 25. AgentsDashboardViewModel → leka-agent-monitoring (8005)
- **Cliente:** `AIGovernanceClient.agentMonitoring()`
- **Métodos requeridos:** `analyzeExecution()`, `evaluateReliability()`, `analyzeCost()`, `detectLoops()`
- **Uso:** Dashboard de monitoreo de agentes
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🟡 Media

#### 26. AgentsDetailViewModel → leka-agent-monitoring (8005)
- **Cliente:** `AIGovernanceClient.agentMonitoring()`
- **Métodos requeridos:** `analyzeExecution()`, `evaluateReliability()`, `analyzeMultiAgentOrchestration()`, `evaluateToolUsage()`
- **Uso:** Análisis detallado de ejecución de agentes
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🟡 Media

#### 27. AgentApprovalWorkflowViewModel → leka-agent-monitoring (8005)
- **Cliente:** `AIGovernanceClient.agentMonitoring()`
- **Métodos requeridos:** `analyzeSafetyViolations()`, `benchmarkAgentPerformance()`
- **Uso:** Validación de agentes en workflow de aprobación
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🟡 Media

#### 28. AgentDecisionsLogViewModel → leka-agent-monitoring (8005)
- **Cliente:** `AIGovernanceClient.agentMonitoring()`
- **Métodos requeridos:** `analyzeExecution()`, `evaluateReliability()`
- **Uso:** Log de decisiones de agentes
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🟡 Media

#### 29. MonitoringDashboardViewModel → leka-llm-evaluation (8002) + leka-agent-monitoring (8005)
- **Clientes:** `AIGovernanceClient.llmEvaluation()`, `AIGovernanceClient.agentMonitoring()`
- **Métodos requeridos:**
  - `llmEvaluation().metrics()` - Métricas de evaluación LLM
  - `agentMonitoring().metrics()` - Métricas de agentes
- **Uso:** Dashboard de monitoreo general
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🟡 Media

#### 30. ServingDashboardViewModel → leka-serving-wrapper (8007)
- **Cliente:** `AIGovernanceClient.servingWrapper()`
- **Métodos requeridos:**
  - `getMetricsSummary()` - Resumen de métricas
  - `getDetailedMetrics()` - Métricas detalladas
  - `getPerformanceMetrics()` - Métricas de rendimiento
  - `getHealthScore()` - Score de salud del sistema
- **Uso:** Dashboard de serving y métricas de inferencia
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🟡 Media

#### 31. GovernanceDetailViewModel → leka-prompt-governance (8003)
- **Cliente:** `AIGovernanceClient.promptGovernance()`
- **Métodos requeridos:** `evaluateSafety()` - Validación de políticas
- **Uso:** Validación de políticas de governance
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🟡 Media

#### 32. EthicsAssessmentsViewModel → leka-llm-evaluation (8002)
- **Cliente:** `AIGovernanceClient.llmEvaluation()`
- **Métodos requeridos:** `evaluateToxicity()`, `evaluateBiasText()` - Evaluaciones éticas automatizadas
- **Uso:** Evaluaciones éticas automatizadas de modelos
- **Estado:** ⚠️ Pendiente
- **Prioridad:** 🟡 Media

---

### Clientes AIGovernanceClient NO DISPONIBLES (requieren implementación):

1. ⚠️ **Technical Documentation Generator Client** (puerto 8008)
   - **ViewModels afectados:** AIActDocumentationGeneratorViewModel, CompleteDocumentationViewModel
   - **Estado:** Microservicio existe, cliente no implementado en AIGovernanceClient
   - **Prioridad:** 🔴 Alta

2. ⚠️ **EU Declaration Generator Client** (puerto 8010)
   - **ViewModels afectados:** ConformityDeclarationManagerViewModel
   - **Estado:** Microservicio existe, cliente no implementado en AIGovernanceClient
   - **Prioridad:** 🔴 Alta

---

### Clientes AIGovernanceClient DISPONIBLES pero NO USADOS:

3. ⚠️ **AI Interpreter Client** (puerto 8011) - Disponible pero no usado
   - **Cliente:** `AIGovernanceClient.aiInterpreter()`
   - **Métodos:** `explainResult()`, `answerQuestion()`, `generateExecutiveSummary()`, `rootCauseAnalysis()`
   - **ViewModels potenciales:** AnalyticsReportViewModel, EthicsImpactViewModel

4. ⚠️ **Deepfake Detection Client** (puerto 8012) - Disponible pero no usado
   - **Cliente:** `AIGovernanceClient.deepfakeDetection()`
   - **Métodos:** `detectImage()`, `detectVideo()`, `detectAudio()`, `watermarkContent()`
   - **ViewModels potenciales:** Ninguno identificado actualmente

5. ⚠️ **Copyright Compliance Client** (puerto 8013) - Disponible pero no usado
   - **Cliente:** `AIGovernanceClient.copyrightCompliance()`
   - **Métodos:** `checkTrainingDataCompliance()`, `detectOptOut()`, `generateAttribution()`
   - **ViewModels potenciales:** ModelAdaptationRecommendationViewModel, TrainingDashboardViewModel

6. ⚠️ **Board Governance Calculator Client** (puerto 8061) - Disponible pero no usado
   - **Cliente:** `AIGovernanceClient.boardGovernance()`
   - **Métodos:** `calculateEDMMetrics()`, `generateExecutiveSummary()`, `calculateHealthScore()`
   - **ViewModels potenciales:** GovernanceDashboardViewModel, MainDashboardViewModel

7. ⚠️ **Multi-Framework Compliance Aggregator Client** (puerto 8060) - Disponible pero no usado
   - **Cliente:** `AIGovernanceClient.multiFrameworkCompliance()`
   - **Métodos:** `calculateOverallScore()`, `getFrameworksSupported()`, `exportComplianceReport()`
   - **ViewModels potenciales:** ComplianceAiActViewModel, SectorDashboardViewModel

8. ⚠️ **Adversarial Robustness Client** (puerto 8007) - Disponible pero no usado
   - **Cliente:** `AIGovernanceClient.adversarialRobustness()`
   - **Métodos:** `detectModelPoisoning()`, `testModelEvasion()`, `simulateAttacks()`, `scorePolicyAlignment()`
   - **ViewModels potenciales:** ModelApprovalWorkflowViewModel, AnalyticsBiasViewModel

---

## 📊 RESUMEN DE ESTADO

### ViewModels Completos: 76/91 (83.5%)
### ViewModels Requieren Refactorización: 4 (AIMS e ISO42001)
### ViewModels Sin Prompt: 15 (16.5%)
### Integraciones Python Pendientes: 20+

---

## 🎯 RECOMENDACIONES PRIORIZADAS

### Prioridad Alta:
1. Refactorizar ViewModels AIMS e ISO42001 (usar BusinessService)
2. Implementar integraciones Python pendientes en Compliance
3. Asignar prompts a ViewModels sin asignación

### Prioridad Media:
4. Crear BusinessServices faltantes
5. Identificar y confirmar ZULs faltantes
6. Documentar integraciones con microservicios Python

### Prioridad Baja:
7. Crear ViewModels para prompts sin implementación
8. Optimizar integraciones existentes

---

---

## 📚 REFERENCIA RÁPIDA AIGovernanceClient

### Clientes Disponibles en AIGovernanceClient:

| Cliente | Puerto | Métodos Principales | ViewModels que Deben Usarlo |
|---------|--------|---------------------|----------------------------|
| `llmEvaluation()` | 8002 | `evaluateHallucination()`, `evaluateToxicity()`, `evaluateBiasText()`, `evaluateQuality()`, `benchmarkEvaluations()`, `metrics()` | AnalyticsOverviewViewModel, AnalyticsTransparencyViewModel, AnalyticsMetricViewModel, AnalyticsTrendsViewModel, EthicsAssessmentsViewModel, MonitoringDashboardViewModel |
| `promptGovernance()` | 8003 | `evaluateSafety()`, `evaluateEffectiveness()`, `detectPiiLeakage()`, `compareVersions()`, `analyzeCost()`, `validateTemplate()` | HighRiskClassifierViewModel, PromptsOverviewViewModel, PromptsDetailViewModel, PromptApprovalWorkflowViewModel, GovernanceDetailViewModel |
| `ragEvaluation()` | 8004 | `evaluateRetrieval()`, `evaluateAnswer()`, `evaluateContextRelevance()`, `evaluateFullPipeline()` | RagSystemsOverviewViewModel, RagSystemsDetailViewModel, RagClientPoliciesOverviewViewModel, RagClientPoliciesDetailViewModel, ComplianceAiActViewModel |
| `agentMonitoring()` | 8005 | `analyzeExecution()`, `evaluateReliability()`, `analyzeCost()`, `detectLoops()`, `analyzeMultiAgentOrchestration()`, `metrics()` | AgentsDashboardViewModel, AgentsDetailViewModel, AgentApprovalWorkflowViewModel, AgentDecisionsLogViewModel, MonitoringDashboardViewModel |
| `modelWrapper()` | 8006 | `invoke()`, `batchInvoke()`, `compareResponses()`, `listAvailable()` | ModelApprovalWorkflowViewModel, ModelAdaptationRecommendationViewModel |
| `servingWrapper()` | 8007 | `getMetricsSummary()`, `getDetailedMetrics()`, `getPerformanceMetrics()`, `getHealthScore()`, `chat()`, `transcribeAudio()`, `captionImage()` | ServingDashboardViewModel |
| `biasDetection()` | 8001 | `analyzeBias()`, `detectDrift()`, `validateDataQuality()`, `testRobustness()`, `benchmarkFairness()`, `explainPredictions()` | AnalyticsBiasViewModel, AnalyticsFairnessViewModel, ModelApprovalWorkflowViewModel |
| `friaGenerator()` | 8012 | `generateAssessment()`, `analyzeFundamentalRights()`, `integrateWithDPIA()`, `crossValidate()` | FriaWizardViewModel, CompleteMissingElementsViewModel, DefineModificationsViewModel, EnhancedReviewViewModel |
| `conformityAssessment()` | 8009 | `assessAnnexVI()`, `generateChecklist()`, `validateEvidence()`, `getAnnexVITemplate()` | InitiateConformityAssessmentViewModel, ConformityReviewViewModel, ApproveConformityAssessmentViewModel, ReviewQmsGapsViewModel, FinalReviewViewModel |
| `adversarialRobustness()` | 8007 | `detectModelPoisoning()`, `testModelEvasion()`, `simulateAttacks()`, `scorePolicyAlignment()` | ModelApprovalWorkflowViewModel, AnalyticsBiasViewModel |
| `aiInterpreter()` | 8011 | `explainResult()`, `answerQuestion()`, `generateExecutiveSummary()`, `rootCauseAnalysis()`, `chat()` | AnalyticsReportViewModel, EthicsImpactViewModel (potencial) |
| `deepfakeDetection()` | 8012 | `detectImage()`, `detectVideo()`, `detectAudio()`, `watermarkContent()`, `generateDisclosure()` | Ninguno identificado actualmente |
| `copyrightCompliance()` | 8013 | `checkTrainingDataCompliance()`, `detectOptOut()`, `generateAttribution()`, `validateLicense()` | ModelAdaptationRecommendationViewModel, TrainingDashboardViewModel (potencial) |
| `boardGovernance()` | 8061 | `calculateEDMMetrics()`, `generateExecutiveSummary()`, `calculateHealthScore()` | GovernanceDashboardViewModel, MainDashboardViewModel (potencial) |
| `multiFrameworkCompliance()` | 8060 | `calculateOverallScore()`, `getFrameworksSupported()`, `exportComplianceReport()`, `getCertificationRoadmap()` | ComplianceAiActViewModel, SectorDashboardViewModel (potencial) |

### Clientes NO Disponibles (requieren implementación):

| Microservicio | Puerto | ViewModels Afectados | Estado |
|---------------|--------|----------------------|--------|
| **Technical Documentation Generator** | 8008 | AIActDocumentationGeneratorViewModel, CompleteDocumentationViewModel | ⚠️ Microservicio existe, cliente no implementado |
| **EU Declaration Generator** | 8010 | ConformityDeclarationManagerViewModel | ⚠️ Microservicio existe, cliente no implementado |

### Ejemplo de Uso en ViewModel:

```java
@WireVariable
private AIGovernanceClient aiGovernanceClient;

@Command
public void evaluatePrompt() {
    try {
        PromptSafetyResponse response = aiGovernanceClient
            .promptGovernance()
            .evaluateSafety(promptRequest);
        // Procesar respuesta
    } catch (Exception e) {
        log.error("Error evaluando prompt", e);
    }
}
```

---

**Última actualización:** 25 de noviembre de 2025
**Total ViewModels:** 91
**Cobertura Prompts:** 83.5%
**Integraciones Python Implementadas:** 2/34 (5.9%)
**Integraciones Python Pendientes:** 32/34 (94.1%)
