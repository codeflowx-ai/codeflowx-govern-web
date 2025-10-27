# 🔍 Análisis Completo: JPAs Faltantes en ViewModels

## 📊 Resumen Ejecutivo

**Total ViewModels analizados:** 54  
**Total imports de JPAs inexistentes:** 72  
**Archivos afectados:** 29

---

## ❌ JPAs Inexistentes por Categoría

### 🔴 VIEWS Faltantes (48)

#### agents/ (4)
- `AgentComplianceStatus` (entity.views.agents)
- `AgentDeploymentStatus` (entity.views.agents)
- `AgentHealthDashboard` (entity.views.agents)
- `AgentPerformanceMetrics` (entity.views.agents)

#### analytics/ (2)
- `AnalyticsOverview` (entity.views.analytics)
- `AnalyticsTrends` (entity.views.analytics)

#### core/ (4)
- `AdminDashboardSummary` (entity.views.core)
- `SecurityAuditSummary` (entity.views.core)
- `SystemHealthOverview` (entity.views.core)
- `UserActivitySummary` (entity.views.core)

#### governance/ (8)
- `GovernanceDashboardSummary` (entity.views.governance)
- `ComplianceByFramework` (entity.views.governance)
- `PolicyEvaluationTrends` (entity.views.governance)
- `RiskAssessmentMatrix` (entity.views.governance)
- `GovernanceMetricsSummary` (entity.views.governance)
- `GovernanceOverview` (entity.views.governance)
- `ComplianceGapsAnalysis` (entity.views.governance)

#### infrastructure/ (4)
- `InfrastructureMetricsSummary` (entity.views.infrastructure)
- `InfrastructureOverview` (entity.views.infrastructure)

#### models/ (2)
- `ModelsMetricsSummary` (entity.views.models)
- `ModelsOverview` (entity.views.models)

#### monitoring/ (4)
- `AlertSummary` (entity.views.monitoring)
- `AnomalyHeatmap` (entity.views.monitoring)
- `MetricsVisualization` (entity.views.monitoring)
- `MonitoringDashboard` (entity.views.monitoring)

#### projects/ (5)
- `ProjectCostBreakdown` (entity.views.projects)
- `ProjectFinancialSummary` (entity.views.projects)
- `ProjectPortfolioDashboard` (entity.views.projects)
- `ProjectResourceAllocation` (entity.views.projects)
- `ProjectRiskAssessment` (entity.views.projects)

#### prompts/ (2)
- `PromptsOverview` (entity.views.prompts)
- `PromptsMetricsSummary` (entity.views.prompts)

#### providers/ (3)
- `PromptsOverview` (entity.views.prompts) ← Error de copy/paste
- `ProvidersMetricsSummary` (entity.views.providers)
- `ProvidersOverview` (entity.views.providers)

#### rag/ (1)
- `RagOverview` (entity.views.rag)

#### serving/ (6)
- `DeploymentStatus` (entity.views.serving)
- `EndpointAnalytics` (entity.views.serving)
- `ServingCostBreakdown` (entity.views.serving)
- `ServingErrorAnalysis` (entity.views.serving)
- `ServingPerformanceDashboard` (entity.views.serving)
- `ServingSlaCompliance` (entity.views.serving)

#### training/ (7)
- `ExperimentLeaderboard` (entity.views.training)
- `HpoProgressDashboard` (entity.views.training)
- `TrainingCostAnalysis` (entity.views.training)
- `TrainingMetricsSummary` (entity.views.training)
- `TrainingOverview` (entity.views.training)
- `TrainingResourceUtilization` (entity.views.training)
- `TrainingMetricsSummary` (entity.views.training) ← Duplicado

---

### 🔴 PROCEDURES Faltantes (11)

#### governance/ (4)
- `RunComplianceCheck` (entity.procedures.governance)
- `BulkComplianceCheck` (entity.procedures.governance)
- `EvaluatePolicy` (entity.procedures.governance)

#### infrastructure/ (1)
- `ExecuteHealthCheck` (entity.procedures.infrastructure)

#### models/ (1)
- `CreateModelVersion` (entity.procedures.models)

#### prompts/ (1)
- `VersionPrompt` (entity.procedures.prompts)

#### rag/ (1)
- `IngestDocuments` (entity.procedures.rag)

---

### 🔴 FUNCTIONS Faltantes (6)

#### governance/ (1)
- `CalculatePolicyScore` (entity.functions.governance)

#### infrastructure/ (1)
- `CalculateResourceEfficiency` (entity.functions.infrastructure)

#### models/ (2)
- `CalculateDrift` (entity.functions.models)
- `ValidateModel` (entity.functions.models)

#### prompts/ (1)
- `CalculatePromptEffectiveness` (entity.functions.prompts)

#### rag/ (1)
- `CalculateIndexHealth` (entity.functions.rag)

---

### 🔴 TABLES Faltantes (7)

#### agents/ (1)
- `AgentTask` (entity.agents)

---

## 📋 ViewModels Afectados (Detallado)

### agents/ (3 archivos)
- **AgentDecisionsLogViewModel.java** - 1 JPA faltante (AgentTask)
- **AgentsDashboardViewModel.java** - 4 VIEWS faltantes
- **AgentsDetailViewModel.java** - 2 VIEWS faltantes

### analytics/ (2 archivos)
- **AnalyticsOverviewViewModel.java** - 1 VIEW faltante
- **AnalyticsTrendsViewModel.java** - 1 VIEW faltante

### core/ (4 archivos)
- **AdminDashboardViewModel.java** - 1 VIEW faltante
- **SecurityAuditViewModel.java** - 1 VIEW faltante
- **SystemHealthViewModel.java** - 1 VIEW faltante
- **UserActivityViewModel.java** - 1 VIEW faltante

### dashboard/ (1 archivo)
- **MainDashboardViewModel.java** - 5 VIEWS faltantes

### governance/ (4 archivos)
- **ComplianceAiActViewModel.java** - 1 PROCEDURE faltante
- **ComplianceAutomatedChecksViewModel.java** - 1 VIEW + 2 PROCEDURES faltantes
- **GovernanceDashboardViewModel.java** - 4 VIEWS faltantes
- **GovernanceDetailViewModel.java** - 1 FUNCTION + 2 PROCEDURES faltantes
- **GovernanceOverviewViewModel.java** - 2 VIEWS faltantes

### infrastructure/ (2 archivos)
- **InfrastructureDetailViewModel.java** - 1 FUNCTION + 1 PROCEDURE faltantes
- **InfrastructureOverviewViewModel.java** - 2 VIEWS faltantes

### models/ (2 archivos)
- **ModelsDetailViewModel.java** - 2 FUNCTIONS + 1 PROCEDURE faltantes
- **ModelsOverviewViewModel.java** - 2 VIEWS faltantes

### monitoring/ (1 archivo)
- **MonitoringDashboardViewModel.java** - 4 VIEWS faltantes

### projects/ (1 archivo)
- **ProjectsDashboardViewModel.java** - 5 VIEWS faltantes

### prompts/ (2 archivos)
- **PromptsDetailViewModel.java** - 1 FUNCTION + 1 PROCEDURE faltantes
- **PromptsOverviewViewModel.java** - 2 VIEWS faltantes

### providers/ (1 archivo)
- **ProvidersOverviewViewModel.java** - 3 VIEWS faltantes (1 con nombre incorrecto)

### rag/ (2 archivos)
- **RagSystemsDetailViewModel.java** - 1 FUNCTION + 1 PROCEDURE faltantes
- **RagSystemsOverviewViewModel.java** - 1 VIEW faltante

### serving/ (1 archivo)
- **ServingDashboardViewModel.java** - 6 VIEWS faltantes

### training/ (2 archivos)
- **ExperimentsDetailViewModel.java** - 1 VIEW faltante
- **TrainingDashboardViewModel.java** - 6 VIEWS faltantes

---

## 🔧 Acciones Requeridas

### Opción 1: Crear las JPAs Faltantes (Recomendado)
1. Crear las 48 VIEWS en PostgreSQL y sus JPAs correspondientes
2. Crear las 11 PROCEDURES en PostgreSQL y sus JPAs correspondientes
3. Crear las 6 FUNCTIONS en PostgreSQL y sus JPAs correspondientes
4. Crear la TABLE `AgentTask` faltante

### Opción 2: Eliminar/Comentar Código No Funcional
1. Comentar imports de JPAs inexistentes
2. Comentar métodos que usan estas JPAs
3. Agregar TODO con descripción de funcionalidad esperada

### Opción 3: Usar Queries Nativas Temporales
1. Reemplazar uso de JPAs inexistentes con `findByParams()` usando SQL directo
2. Comentar código de PROCEDURES/FUNCTIONS hasta crear las JPAs

---

## ⚠️ Problemas Críticos Adicionales

### 1. Copy/Paste Errors
- **ProvidersOverviewViewModel** importa `PromptsOverview` en lugar de `ProvidersOverview`

### 2. Uso Incorrecto de callProcedure()
- **AgentApprovalWorkflowViewModel** línea 164: 
  ```java
  businessService.callProcedure("sp_auto_approve_artifact", params);
  ```
  ❌ Incorrecto - debe recibir un bean JPA, no String

### 3. JPAs Duplicadas
- `TrainingMetricsSummary` aparece duplicado en TrainingDashboardViewModel

---

## 📈 Prioridad de Corrección

### Alta Prioridad (Dashboards - 3 archivos)
1. MainDashboardViewModel - 5 VIEWS faltantes
2. MonitoringDashboardViewModel - 4 VIEWS faltantes
3. GovernanceDashboardViewModel - 4 VIEWS faltantes

### Media Prioridad (Overview ViewModels - 8 archivos)
4. ServingDashboardViewModel - 6 VIEWS
5. TrainingDashboardViewModel - 6 VIEWS
6. ProjectsDashboardViewModel - 5 VIEWS
7. Resto de Overview ViewModels

### Baja Prioridad (Detail ViewModels con FUNCTIONS/PROCEDURES)
8. ViewModels que usan FUNCTIONS/PROCEDURES no críticas

---

## 📊 Estadísticas

| Categoría | Cantidad | % |
|-----------|----------|---|
| VIEWS | 48 | 67% |
| PROCEDURES | 11 | 15% |
| FUNCTIONS | 6 | 8% |
| TABLES | 7 | 10% |
| **TOTAL** | **72** | **100%** |

---

**Fecha:** 26 de octubre de 2025  
**Generado por:** Análisis automático de imports  
**Proyecto:** suinsit.nova.web


