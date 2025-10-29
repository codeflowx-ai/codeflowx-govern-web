# 🏗️ ARQUITECTURA CODEFLOWX GOVERN

**Versión:** 1.0  
**Fecha Creación:** Octubre 2025  
**Estado:** ACTIVO - Se actualiza constantemente  
**Proyecto:** CodeflowX AI Governance Platform

---

## 📑 TABLA DE CONTENIDOS

1. [Visión General](#visión-general)
2. [Stack Tecnológico](#stack-tecnológico)
3. [Capas de Arquitectura](#capas-de-arquitectura)
4. [Componentes Principales](#componentes-principales)
5. [Flujo de Datos](#flujo-de-datos)
6. [Patrones de Diseño](#patrones-de-diseño)
7. [Estructura de Paquetes](#estructura-de-paquetes)
8. [Reglas y Convicciones](#reglas-y-convicciones)
9. [Roadmap Arquitectónico](#roadmap-arquitectónico)

---

## 🎯 VISIÓN GENERAL

CodeflowX Govern es una **plataforma de gobernanza de IA** que combina:

- ✅ **BPMN** (Flowable) para orquestación de procesos complejos
- ✅ **Drools** para reglas de negocio declarativas
- ✅ **Spring Boot** para servicios backend
- ✅ **ZKoss** para UI responsive
- ✅ **PostgreSQL** para persistencia

**Filosofía:** Separation of Concerns con capas claramente definidas.

---

## 💻 STACK TECNOLÓGICO

```
┌─────────────────────────────────────────────────────┐
│  CAPA DE PRESENTACIÓN                               │
│  • ZKoss Framework (ZUL + MVVM)                   │
│  • Bootstrap 5 + CSS Custom                        │
│  • 410 ViewModels autogenerados (CRUD)             │
│  • 25 ViewModels de BPMN (User Tasks)              │
└─────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────┐
│  CAPA DE ORQUESTACIÓN                               │
│  • Flowable BPMN Engine 6.8.1                      │
│  • 17 Procesos BPMN 2.0 Avanzados                  │
│  • ParallelGateway, BusinessRuleTask, etc.         │
└─────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────┐
│  CAPA DE REGLAS DE NEGOCIO                          │
│  • Drools Rules Engine 7.x                         │
│  • 11 Fact Objects + 11 DRL (120+ reglas)          │
│  • Modificable sin redeploy                        │
└─────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────┐
│  CAPA DE SERVICIOS                                  │
│  • Spring Boot 2.7.3                               │
│  • 65 JavaDelegates (lógica de negocio)            │
│  • 13 Services de orquestación                     │
│  • Integration con Python ML Services              │
└─────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────┐
│  CAPA DE PERSISTENCIA                               │
│  • PostgreSQL 14+                                  │
│  • 489 Entidades JPA autogeneradas                 │
│  • BusinessService (CRUD)                          │
│  • Views y Functions SQL                           │
└─────────────────────────────────────────────────────┘
```

---

## 🏛️ CAPAS DE ARQUITECTURA

### **1. PRESENTATION LAYER**
**Responsabilidad:** UI para usuarios finales

**Componentes:**
- `platform/viewmodel/` - 410 ViewModels CRUD autogenerados
- `workflow/viewmodels/` - ~25 ViewModels para User Tasks BPMN
- Archivos `.zul` - Templates ZKoss
- CSS/Bootstrap - Estilos

**Patrón:** MVVM (Model-View-ViewModel)

**Regla de Oro:** ViewModels delgados, sin lógica de negocio.

---

### **2. ORCHESTRATION LAYER**
**Responsabilidad:** Coordinar flujos complejos de negocio

**Componentes:**
- Procesos BPMN en `src/main/resources/processes/`
- Flowable Engine integrado con Spring
- User Tasks para revisión humana (HITL)

**Características:**
- 80% de procesos automatizados con IA
- 20% Human-in-the-Loop (HITL)
- SLA automáticos con Timer Boundary Events

**Procesos Críticos:**
- `agent-approval-v1.bpmn` - Aprobación de agentes IA
- `model-approval-v1.bpmn` - Aprobación de modelos ML
- `bias-detection-v1.bpmn` - Detección de sesgos
- `compliance-monitoring-v1.bpmn` - Monitoreo de compliance

---

### **3. RULES LAYER**
**Responsabilidad:** Reglas de decisión declarativas

**Componentes:**
- DRL files en `workflow/drools/`
- Fact Objects en `workflow/drools/facts/`
- `DroolsRulesService` para ejecución

**Filosofía:**
> No hardcode de lógica en código Java. Reglas modificables sin redeploy.

**Ejemplo:**
```drl
rule "Auto-Approve Agent - High Confidence"
when
    $fact : AgentApprovalFact(
        riskScore >= 90,
        complianceScore >= 90,
        ethicsScore >= 90
    )
then
    $fact.setDecision("AUTO_APPROVE");
end
```

---

### **4. SERVICES LAYER**
**Responsabilidad:** Lógica de negocio compleja

**Componentes:**
- `workflow/delegates/` - 80+ JavaDelegates para Service Tasks
- `workflow/services/` - Servicios de orquestación

**Principales Services:**
- `ComplianceCheckService` - Verificación de compliance (RAG + LLM)
- `RiskAssessmentService` - Evaluación de riesgos
- `BiasDetectionService` - Detección de sesgos
- `ModelInferenceService` - Integración con LLMs

**Delegates Críticos:**
- `AIRiskAssessmentDelegate` - Risk scoring con IA
- `ComplianceCheckDelegate` - Compliance check
- `CalculateAgentScoreDelegate` - Scoring con Drools
- `AutoApproveAgentDelegate` - Auto-aprobación

---

### **5. PERSISTENCE LAYER**
**Responsabilidad:** Acceso a datos

**Componentes:**
- 489 Entidades JPA en `nocode.service/nocode.service.entitys/`
- BusinessService - Capa de abstracción CRUD
- Views SQL para KPIs y analíticas
- Functions/Procedures SQL (wrappers disponibles)

**Patrón:**
```java
@WireVariable
private BusinessService businessService;

// Uso genérico
pageResult = businessService.findAllEntity(Agent.class, pageParams, criterias);
```

**Entidades Principales:**
- Agents: 22 entidades (Agent, AgentApproval, AgentMonitoring, etc.)
- Models: 14 entidades (Model, ModelVersion, ModelMetrics, etc.)
- Training: 21 entidades (Experiment, Run, Metrics, etc.)
- Governance: 14 entidades (Policy, ComplianceAssessment, etc.)

---

## 🔧 COMPONENTES PRINCIPALES

### **1. BusinessService**
**Ubicación:** `codeflowx.nocode.persist.BusinessService`

**Responsabilidad:**
- CRUD genérico para todas las entidades
- Criteria-based queries
- Paginación automática

**API:**
```java
public <T> PageResult<T> findAllEntity(Class<T> entityClass, PageParams params, Criterias criterias);
public <T> T findById(Class<T> entityClass, Long id);
public <T> void save(T entity);
public <T> void delete(T entity);
```

---

### **2. BaseFront**
**Ubicación:** `com.codeflowx.framework.zkoss.BaseFront`

**Responsabilidad:**
- Clase base para ViewModels
- Proporciona servicios comunes (BusinessService, Context, Environment, etc.)
- Logging de actividades

**Herencia:**
```java
public class DashboardViewModel extends BaseFront<DashboardViewModel> {
    // Hereda automáticamente:
    // - BusinessService
    // - Context
    // - Environment
    // - logActivity()
}
```

---

### **3. DroolsRulesService**
**Ubicación:** `com.codeflowx.govern.workflow.services.DroolsRulesService`

**Responsabilidad:**
- Ejecutar reglas Drools
- Gestionar sesiones KIE
- Integración con BusinessRuleTask

**API:**
```java
public void executeAgentApprovalRules(Object fact);
public void executeModelApprovalRules(Object fact);
public void executeBiasDetectionRules(Object fact);
```

---

## 🔄 FLUJO DE DATOS

### **Proceso Típico: Agent Approval**

```
1. Usuario inicia proceso → Flowable
   ↓
2. ParallelGateway lanza 3 evaluaciones en paralelo:
   - AIRiskAssessmentDelegate
   - ComplianceCheckDelegate  
   - AIEthicalReviewDelegate
   ↓
3. Delegates guardan en BusinessService:
   - AgentApproval.agtriskassessment
   - AgentApproval.agtcompliancecheck
   - AgentApproval.agtethicsreview
   ↓
4. BusinessRuleTask ejecuta Drools:
   - DroolsRulesService
   - agent-scoring.drl (6 reglas)
   - Retorna decision (AUTO_APPROVE/HITL_REQUIRED/AUTO_REJECT)
   ↓
5. ExclusiveGateway decide ruta:
   - Si AUTO_APPROVE → AutoApproveAgentDelegate
   - Si HITL_REQUIRED → UserTask (AgentApprovalHumanOverrideViewModel)
   - Si AUTO_REJECT → AutoRejectAgentDelegate
   ↓
6. Update a Agent en BusinessService
```

---

## 🎨 PATRONES DE DISEÑO

### **1. Service Task Pattern**
**Cuándo:** Automatizar evaluación con IA

**Estructura:**
```java
@Component("myDelegate")
public class MyDelegate implements JavaDelegate {
    @Autowired
    private MyService myService;
    @Autowired
    private BusinessService businessService;
    
    @Override
    public void execute(DelegateExecution execution) {
        // 1. Ejecutar lógica
        Result result = myService.doSomething();
        
        // 2. Guardar en JPA
        businessService.save(entity);
        
        // 3. Guardar en variables del proceso
        execution.setVariable("result", result.toJson());
    }
}
```

---

### **2. User Task Pattern**
**Cuándo:** Requiere decisión humana

**Estructura:**
```java
// ViewModel
public class MyTaskViewModel extends BaseFront {
    @WireVariable
    private TaskService taskService;
    
    private String taskId;
    
    @AfterCompose
    public void afterCompose() {
        // 1. Obtener variables
        Map<String, Object> vars = taskService.getVariables(taskId);
    }
    
    @Command
    public void completeTask() {
        // 2. Completar tarea con decisión
        taskService.complete(taskId, Map.of("decision", "approve"));
    }
}
```

---

### **3. Drools Rules Pattern**
**Cuándo:** Decisión basada en múltiples criterios

**Estructura:**
```java
// 1. Crear Fact
AgentApprovalFact fact = new AgentApprovalFact();
fact.setRiskScore(riskScore);
fact.setComplianceScore(complianceScore);
fact.setEthicsScore(ethicsScore);

// 2. Ejecutar reglas
droolsService.executeAgentApprovalRules(fact);

// 3. Obtener decisión
String decision = fact.getDecision();
```

---

## 📁 ESTRUCTURA DE PAQUETES

```
src/main/java/com/codeflowx/
├── govern/
│   ├── workflow/
│   │   ├── delegates/           # 80+ Delegates
│   │   │   ├── AIRiskAssessmentDelegate.java
│   │   │   ├── ComplianceCheckDelegate.java
│   │   │   └── ...
│   │   ├── services/            # Services de negocio
│   │   │   ├── ComplianceCheckService.java
│   │   │   ├── RiskAssessmentService.java
│   │   │   └── DroolsRulesService.java
│   │   ├── drools/              # Reglas Drools
│   │   │   ├── facts/           # Fact Objects
│   │   │   ├── agent/           # DRL de agentes
│   │   │   ├── bias/            # DRL de sesgo
│   │   │   └── ...
│   │   └── viewmodels/          # ViewModels de User Tasks
│   │       ├── AgentApprovalHumanOverrideViewModel.java
│   │       └── ...
│   ├── entity/                  # Entidades JPA (489)
│   │   ├── agents/
│   │   ├── models/
│   │   └── ...
│   └── viewmodel/               # ViewModels de pantallas
│       ├── DashboardViewModel.java
│       └── ...
├── platform/
│   └── viewmodel/               # 410 ViewModels CRUD autogenerados
│       ├── agents/
│       ├── models/
│       └── ...
└── framework/
    └── zkoss/
        └── BaseFront.java        # Clase base para ViewModels
```

**Recursos:**
```
src/main/resources/
├── processes/                   # 17 procesos BPMN
│   ├── agent-approval-v1.bpmn
│   ├── model-approval-v1.bpmn
│   └── ...
└── rules/                       # DRL files
    └── (carpetas por módulo)
```

---

## 📜 REGLAS Y CONVICCIONES

### **Regla #1: No Lógica de Negocio en ViewModels**
❌ MAL:
```java
public class DashboardViewModel {
    public void calculateKPI() {
        // Cálculos complejos aquí
    }
}
```

✅ BIEN:
```java
public class DashboardViewModel {
    @Autowired
    private KPICalculationService kpiService;
    
    public void loadData() {
        kpi = kpiService.calculateKPIs();
    }
}
```

---

### **Regla #2: No Hardcode de Reglas en Código Java**
❌ MAL:
```java
if (score >= 90 && compliant && ethics >= 90) {
    decision = "AUTO_APPROVE";
}
```

✅ BIEN:
```java
// Dejar que Drools decida
droolsService.executeAgentApprovalRules(fact);
String decision = fact.getDecision();
```

---

### **Regla #3: Delegates Usan BusinessService**
✅ SIEMPRE:
```java
@Component("myDelegate")
public class MyDelegate implements JavaDelegate {
    @Autowired
    private BusinessService businessService;
    
    @Override
    public void execute(DelegateExecution execution) {
        Entity entity = businessService.findById(Entity.class, id);
        entity.setField(value);
        businessService.save(entity);
    }
}
```

---

### **Regla #4: ViewModels Autogenerados Solo CRUD**
✅ PATRÓN:
```java
public class AgentOverviewViewModel extends MasterPage {
    @WireVariable
    private BusinessService businessService;
    
    @Command
    public void loadData() {
        pageResult = businessService.findAllEntity(
            Agent.class, 
            pageParams, 
            criterias
        );
    }
}
```

---

### **Regla #5: Nomenclatura de Entidades**
**Prefijo de 3 letras + Descripción**

Ejemplos:
- `AGTAGENTS` → Agents
- `MODMODELS` → Models
- `TRNEXPERIMENTS` → Training
- `GOVPOLICIES` → Governance

**Migrar a Spring Boot 3 cuando sea posible:**
- Flowable 7.x requiere Spring Boot 3
- Plan: Microservicio para Flowable

---

## 🚀 ROADMAP ARQUITECTÓNICO

### **Corto Plazo (V1.0)**
- ✅ Eliminar duplicación DashboardViewModel vs MainDashboardViewModel
- ✅ Documentar todos los ViewModels
- ✅ Optimizar consultas N+1

### **Medio Plazo (V1.1)**
- 🎯 Implementar Model Adaptation Governance (5 nuevos procesos BPMN)
- 🎯 Migrar a Spring Boot 3.x
- 🎯 Extraer Flowable a microservicio

### **Largo Plazo (V2.0)**
- 🔮 Framework-as-Software para nuevos compliance
- 🔮 Real-time streaming de métricas
- 🔮 Multi-tenant architecture

---

## 📞 MANTENIMIENTO

**Este documento se actualiza continuamente.**

**Última actualización:** [Actualizar tras cada cambio]  
**Responsable:** Equipo de Arquitectura  
**Review:** Cada sprint

---

**Estado:** 🟢 ARQUITECTURA ESTABLE Y FUNCIONANDO

