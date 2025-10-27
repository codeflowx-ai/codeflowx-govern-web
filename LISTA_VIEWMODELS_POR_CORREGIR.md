# Lista Completa de ViewModels por Corregir

## ✅ COMPLETADOS (9 archivos)

### ViewModels Workflow:
1. ✅ `TaskInboxViewModel.java` - PATRÓN REFERENCIA COMPLETO
2. ✅ `AlertResponseViewModel.java`
3. ✅ `AgentApprovalHumanOverrideViewModel.java`
4. ✅ `BiasMitigationPlanViewModel.java`
5. ✅ `BiasReviewViewModel.java`
6. ✅ `BiasUrgentDecisionViewModel.java`
7. ✅ `PromptApprovalRequestViewModel.java`
8. ✅ `PromptHumanReviewViewModel.java`

### ViewModels Principales:
1. ✅ `viewmodel/agents/AgentsDashboardViewModel.java`

## ❌ PENDIENTES (41 archivos con `findAllEntity()`)

### Agents (3 archivos):
1. ❌ `viewmodel/agents/AgentApprovalWorkflowViewModel.java`
2. ❌ `viewmodel/agents/AgentDecisionsLogViewModel.java`
3. ❌ `viewmodel/agents/AgentsDetailViewModel.java`

### Analytics (7 archivos):
1. ❌ `viewmodel/analytics/AnalyticsAccountabilityViewModel.java` - 3 usos
2. ❌ `viewmodel/analytics/AnalyticsBiasViewModel.java` - 2 usos
3. ❌ `viewmodel/analytics/AnalyticsFairnessViewModel.java` - 3 usos
4. ❌ `viewmodel/analytics/AnalyticsImpactViewModel.java` - 3 usos
5. ❌ `viewmodel/analytics/AnalyticsMetricViewModel.java` - 1 uso
6. ❌ `viewmodel/analytics/AnalyticsReportViewModel.java` - 1 uso
7. ❌ `viewmodel/analytics/AnalyticsTransparencyViewModel.java` - 2 usos

### Catalog (2 archivos):
1. ❌ `viewmodel/catalog/CatalogDashboardViewModel.java`
2. ❌ `viewmodel/catalog/CatalogModelsViewModel.java`

### Core (7 archivos):
1. ❌ `viewmodel/core/DepartmentViewModel.java`
2. ❌ `viewmodel/core/LoginAttemptViewModel.java`
3. ❌ `viewmodel/core/MenuViewModel.java`
4. ❌ `viewmodel/core/PermissionViewModel.java`
5. ❌ `viewmodel/core/RoleViewModel.java`
6. ❌ `viewmodel/core/UserSessionViewModel.java`
7. ❌ `viewmodel/core/UserViewModel.java`

### Dashboard (1 archivo):
1. ❌ `viewmodel/dashboard/MainDashboardViewModel.java`

### Governance (8 archivos):
1. ❌ `viewmodel/governance/ComplianceAiActViewModel.java`
2. ❌ `viewmodel/governance/ComplianceAutomatedChecksViewModel.java`
3. ❌ `viewmodel/governance/EthicsAssessmentsViewModel.java`
4. ❌ `viewmodel/governance/EthicsCommitteeViewModel.java`
5. ❌ `viewmodel/governance/EthicsImpactViewModel.java`
6. ❌ `viewmodel/governance/EthicsMitigationViewModel.java`
7. ❌ `viewmodel/governance/EthicsViolationsViewModel.java`
8. ❌ `viewmodel/governance/GovernanceDetailViewModel.java`

### Infrastructure (1 archivo):
1. ❌ `viewmodel/infrastructure/InfrastructureDetailViewModel.java`

### Models (3 archivos):
1. ❌ `viewmodel/models/ModelApprovalWorkflowViewModel.java`
2. ❌ `viewmodel/models/ModelsDetailViewModel.java`
3. ❌ `viewmodel/models/ModelsOverviewViewModel.java`

### Monitoring (1 archivo):
1. ❌ `viewmodel/monitoring/MonitoringDashboardViewModel.java`

### Projects (1 archivo):
1. ❌ `viewmodel/projects/ProjectsDashboardViewModel.java`

### Prompts (2 archivos):
1. ❌ `viewmodel/prompts/PromptApprovalWorkflowViewModel.java`
2. ❌ `viewmodel/prompts/PromptsDetailViewModel.java`

### Providers (1 archivo):
1. ❌ `viewmodel/providers/ProvidersDetailViewModel.java`

### RAG (1 archivo):
1. ❌ `viewmodel/rag/RagSystemsDetailViewModel.java`

### Serving (1 archivo):
1. ❌ `viewmodel/serving/ServingDashboardViewModel.java`

### Training (2 archivos):
1. ❌ `viewmodel/training/ExperimentsDetailViewModel.java`
2. ❌ `viewmodel/training/TrainingDashboardViewModel.java`

## Corrección Necesaria

### Patrón a Reemplazar:

```java
// INCORRECTO
PageResult<Entity> result = businessService.findAllEntity(
    Entity.class,
    pageParams,
    filters
);
list = result.getContent(); // o result.getItems()
```

```java
// CORRECTO
String sql = "SELECT * FROM TABLENAME ORDER BY CREATEDAT DESC LIMIT 20";
List<Entity> result = businessService.findByParams(
    Entity.class,
    sql,
    null
);
list = result;
```

## Nombres de Tablas por Entidad

Para construir los queries SQL correctamente, se necesita conocer el nombre de la tabla de cada entidad. Estos se definen en la anotación `@Table(name = "XXX")` de cada entidad JPA.

### Prefijos comunes:
- `AGT*` - Agents
- `MOD*` - Models
- `PRM*` - Prompts
- `RAG*` - RAG Systems
- `GOV*` - Governance
- `ETH*` - Ethics
- `PRJ*` - Projects
- `SRV*` - Serving
- `TRN*` - Training
- `INF*` - Infrastructure
- `ANL*` - Analytics
- `SSO*` - Security/Users

## Estado del Trabajo

- **Total ViewModels:** 63
- **Corregidos:** 9 (14.3%)
- **Pendientes:** 54 (85.7%)
  - 41 con errores de `findAllEntity()`
  - 13 sin errores aparentes (requieren revisión)

## Estimación de Tiempo

- Corrección de `findAllEntity()`: ~5 min por archivo
- **41 archivos × 5 min = ~205 minutos (~3.4 horas)**

## Recomendación

Dado el volumen de trabajo, se recomienda:

1. **Priorizar archivos críticos** (Dashboard, Core, Governance)
2. **Automatizar** la corrección de patrones repetitivos
3. **Validar** con compilación y pruebas
4. **Documentar** cambios realizados

---

**Estado:** Usuario cenando - Trabajo autónomo en progreso
**Prioridad:** Alta - Todos los viewmodels deben funcionar correctamente

