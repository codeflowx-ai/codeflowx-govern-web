# 📊 Análisis Completo de ViewModels y BusinessService

## 🎯 Patrones Identificados

Después de revisar los ViewModels uno a uno, he identificado los siguientes patrones de uso de `BusinessService`:

### ✅ **ViewModels CORRECTOS** (ya funcionan bien)

#### 1. **MainDashboardViewModel** - ✅ PERFECTO
**Ubicación:** `com.codeflowx.govern.viewmodel.dashboard.MainDashboardViewModel`

**Patrones correctos:**
```java
// VIEWs - usa findAllView()
PageResult<AdminDashboardSummary> result = businessService.findAllView(
    AdminDashboardSummary.class, pageParams, criterias);
PageResult<AgentHealthDashboard> agentResult = businessService.findAllView(
    AgentHealthDashboard.class, pageParams, criterias);
PageResult<GovernanceDashboardSummary> govResult = businessService.findAllView(
    GovernanceDashboardSummary.class, pageParams, criterias);

// TABLEs - usa findAllEntity()
PageResult<ModuleStats> result = businessService.findAllEntity(
    ModuleStats.class, pageParams, criterias);
PageResult<QuickAction> result = businessService.findAllEntity(
    QuickAction.class, pageParams, criterias);
PageResult<CostMetrics> result = businessService.findAllEntity(
    CostMetrics.class, pageParams, criterias);
```

#### 2. **GovernanceDetailViewModel** - ✅ PERFECTO
**Ubicación:** `com.codeflowx.govern.viewmodel.governance.GovernanceDetailViewModel`

**Patrones correctos:**
```java
// TABLEs - usa findAllEntity()
PageResult<PolicyRule> result = businessService.findAllEntity(
    PolicyRule.class, pageParams, criterias);
PageResult<PolicyEvaluation> result = businessService.findAllEntity(
    PolicyEvaluation.class, pageParams, criterias);

// FUNCTIONS - usa callFuction()
EvaluatePolicy function = new EvaluatePolicy();
function = businessService.callFuction(function);

CalculatePolicyScore function = new CalculatePolicyScore();
function = businessService.callFuction(function);

// PROCEDURES - usa callProcedure()
RunComplianceCheck procedure = new RunComplianceCheck();
procedure = businessService.callProcedure(procedure);
```

### ❌ **ViewModels con PROBLEMAS** (necesitan corrección)

#### 1. **AgentApprovalWorkflowViewModel** - ❌ PROBLEMAS
**Ubicación:** `com.codeflowx.govern.viewmodel.agents.AgentApprovalWorkflowViewModel`

**Problemas identificados:**
```java
// ❌ PROBLEMA: Usa findByParams() en lugar de findAllEntity()
String sql1 = "SELECT * FROM AGTAGENTAPPROVALS ORDER BY AGTCREATEDAT DESC LIMIT 20";
List<AgentApproval> result1 = businessService.findByParams(
    AgentApproval.class, sql1, null);

String sql2 = "SELECT * FROM AGTAGENTS ORDER BY AGTCREATEDAT DESC LIMIT 20";
List<Agent> result2 = businessService.findByParams(
    Agent.class, sql2, null);

// ❌ PROBLEMA: Usa executeUpdate() en lugar de callProcedure()
String sql = "CALL sp_auto_approve_artifact(?, ?, ?)";
businessService.executeUpdate(sql, params);
```

**Corrección necesaria:**
```java
// ✅ CORRECTO: Usar findAllEntity() para TABLEs
PageParams pageParams = PageParams.builder()
    .maxRows(20)
    .pageActual(1)
    .rowActual(0)
    .build();
PageResult<AgentApproval> result1 = businessService.findAllEntity(
    AgentApproval.class, pageParams, new Criterias());
PageResult<Agent> result2 = businessService.findAllEntity(
    Agent.class, pageParams, new Criterias());

// ✅ CORRECTO: Crear JPA para procedure y usar callProcedure()
// TODO: Crear AutoApproveArtifactProcedure JPA
// AutoApproveArtifactProcedure proc = new AutoApproveArtifactProcedure();
// proc.setPArtifactType("AGENT");
// proc.setPArtifactId(agentId);
// proc.setPApprovalCriteria("AUTO_APPROVED");
// businessService.callProcedure(proc);
```

## 🔍 **Análisis por Tipo de Entidad**

### **TABLEs** (usar `findAllEntity()`)
- `Agent` - `com.codeflowx.govern.entity.agents.Agent`
- `AgentApproval` - `com.codeflowx.govern.entity.agents.AgentApproval`
- `Policy` - `com.codeflowx.govern.entity.governance.Policy`
- `PolicyRule` - `com.codeflowx.govern.entity.governance.PolicyRule`
- `PolicyEvaluation` - `com.codeflowx.govern.entity.governance.PolicyEvaluation`
- `PolicyViolation` - `com.codeflowx.govern.entity.governance.PolicyViolation`
- `ComplianceAssessment` - `com.codeflowx.govern.entity.governance.ComplianceAssessment`
- `ModuleStats` - `com.codeflowx.govern.entity.dashboard.ModuleStats`
- `QuickAction` - `com.codeflowx.govern.entity.dashboard.QuickAction`
- `CostMetrics` - `com.codeflowx.govern.entity.dashboard.CostMetrics`
- `TokenMetrics` - `com.codeflowx.govern.entity.dashboard.TokenMetrics`

### **VIEWs** (usar `findAllView()`)
- `AdminDashboardSummary` - `com.codeflowx.govern.entity.views.core.AdminDashboardSummary`
- `AgentHealthDashboard` - `com.codeflowx.govern.entity.views.agents.AgentHealthDashboard`
- `AgentComplianceStatus` - `com.codeflowx.govern.entity.views.agents.AgentComplianceStatus`
- `AgentDeploymentStatus` - `com.codeflowx.govern.entity.views.agents.AgentDeploymentStatus`
- `AgentPerformanceMetrics` - `com.codeflowx.govern.entity.views.agents.AgentPerformanceMetrics`
- `GovernanceDashboardSummary` - `com.codeflowx.govern.entity.views.governance.GovernanceDashboardSummary`
- `ServingPerformanceDashboard` - `com.codeflowx.govern.entity.views.serving.ServingPerformanceDashboard`
- `HpoProgressDashboard` - `com.codeflowx.govern.entity.views.training.HpoProgressDashboard`
- `ModelsMetricsSummary` - `com.codeflowx.govern.entity.views.models.ModelsMetricsSummary`
- `ModelsOverview` - `com.codeflowx.govern.entity.views.models.ModelsOverview`

### **FUNCTIONS** (usar `callFuction()`)
- `CalculatePolicyScore` - `com.codeflowx.govern.entity.functions.governance.CalculatePolicyScore`
- `EvaluatePolicy` - `com.codeflowx.govern.entity.functions.governance.EvaluatePolicy`

### **PROCEDURES** (usar `callProcedure()`)
- `RunComplianceCheck` - `com.codeflowx.govern.entity.procedures.governance.RunComplianceCheck`
- `AutoApproveArtifact` - **FALTA CREAR JPA** para `sp_auto_approve_artifact`

## 🚀 **Plan de Corrección**

### **Fase 1: ViewModels con problemas menores**
1. **AgentApprovalWorkflowViewModel** - Cambiar `findByParams()` por `findAllEntity()`
2. **Otros ViewModels** - Revisar uno por uno

### **Fase 2: Procedures faltantes**
1. **Crear JPA para `AutoApproveArtifact`** - `sp_auto_approve_artifact`
2. **Reemplazar `executeUpdate()`** por `callProcedure()`

### **Fase 3: Verificación completa**
1. **Revisar todos los ViewModels** restantes
2. **Identificar más procedures/functions** faltantes
3. **Aplicar correcciones** sistemáticamente

## 📋 **ViewModels Pendientes de Revisar**

### **Agents (8 archivos):**
- `AgentDecisionsLogViewModel.java`
- `AgentHealthDashboardViewModel.java`
- `AgentMonitoringDashboardViewModel.java`
- `AgentPerformanceDashboardViewModel.java`
- `AgentTransparencyDashboardViewModel.java`
- `AgentWorkflowExecutionViewModel.java`
- `AgentWorkflowViewModel.java`
- `AgentsDashboardViewModel.java`

### **Catalog (2 archivos):**
- `CatalogDashboardViewModel.java`
- `CatalogModelsViewModel.java`

### **Otros módulos:**
- `Analytics*ViewModel.java` (múltiples)
- `Core*ViewModel.java` (múltiples)
- `Governance*ViewModel.java` (múltiples)
- `Models*ViewModel.java` (múltiples)
- `Prompts*ViewModel.java` (múltiples)

## 🎯 **Próximo Paso**

Continuar revisando ViewModels uno a uno para identificar:
1. **Uso incorrecto** de `findByParams()` vs `findAllEntity()`/`findAllView()`
2. **Procedures/functions** que usan `executeUpdate()` en lugar de `callProcedure()`/`callFuction()`
3. **JPAs faltantes** que necesitan ser creadas

---

**Estado:** 🔍 Análisis en progreso  
**ViewModels revisados:** 3 de ~50  
**Problemas identificados:** 1 ViewModel con problemas  
**Próximo:** Continuar revisando ViewModels individualmente
