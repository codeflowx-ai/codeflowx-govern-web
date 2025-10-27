# Progreso: Corrección ViewModels Principales

## ✅ COMPLETADOS (1/33)

### Catalog (1/2)
- ✅ **CatalogDashboardViewModel.java**
  - Reemplazados 6 usos de `findAllEntity()` con `findByParams()`
  - Cambio de `PageResult<Entity>` a `List<Entity>`
  - Ya tiene `@Destroy`

## 🔄 PENDIENTES (32/33)

### Catalog (1/2 restantes)
- CatalogModelsViewModel.java

### Dashboard (1)
- MainDashboardViewModel.java

### Governance (8)
- ComplianceAutomatedChecksViewModel.java
- EthicsAssessmentsViewModel.java
- EthicsCommitteeViewModel.java
- EthicsImpactViewModel.java
- EthicsMitigationViewModel.java
- EthicsViolationsViewModel.java
- GovernanceDetailViewModel.java
- GovernanceOverviewViewModel.java

### Infrastructure (2)
- InfrastructureDetailViewModel.java
- InfrastructureOverviewViewModel.java

### Models (3)
- ModelApprovalWorkflowViewModel.java
- ModelsDetailViewModel.java
- ModelsOverviewViewModel.java

### Monitoring (1)
- MonitoringDashboardViewModel.java

### Projects (1)
- ProjectsDashboardViewModel.java

### Prompts (3)
- PromptApprovalWorkflowViewModel.java
- PromptsDetailViewModel.java
- PromptsOverviewViewModel.java

### Providers (1)
- ProvidersDetailViewModel.java

### RAG (1)
- RagSystemsDetailViewModel.java

### Serving (1)
- ServingDashboardViewModel.java

### Training (2)
- ExperimentsDetailViewModel.java
- TrainingDashboardViewModel.java

### Analytics (2) - CON ERRORES DETECTADOS
- AnalyticsMetricViewModel.java
- AnalyticsReportViewModel.java

### Core (5) - CON ERRORES DETECTADOS
- DepartmentViewModel.java
- MenuViewModel.java
- PermissionViewModel.java
- RoleViewModel.java
- UserViewModel.java

**Nota:** Los archivos de Analytics y Core parecen tener `findAllEntity()` residual en comentarios o código muerto.
