# 🎯 VISIÓN COMPLETA DEL PROYECTO CODEFLOWX GOVERN

**Fecha:** Octubre 2025  
**Estado:** Activo en Desarrollo  
**Versión:** 1.1.0

---

## 📊 RESUMEN EJECUTIVO DEL PROYECTO

**CodeflowX AI Governance** es una plataforma enterprise para el gobierno completo del ciclo de vida de la IA, desde datasets hasta modelos en producción, con orquestación BPMN, motor de reglas Drools, y automatización inteligente.

### Indicadores Clave del Proyecto

| Métrica | Valor | Detalle |
|---------|-------|---------|
| **Archivos Java** | 588 | Todos los componentes backend |
| **ViewModels** | 49 | Pantallas de UI (CRUD + BPMN) |
| **Delegates** | 65 | Lógica de negocio por proceso |
| **Procesos BPMN** | 17 | Flujos de governance automatizados |
| **Reglas Drools** | 11 DRL | 120+ reglas de negocio |
| **Pantallas ZUL** | 742 | Interface de usuario |
| **Entidades JPA** | 489 | Modelo de datos completo |

---

## 🏗️ ARQUITECTURA DEL PROYECTO

### Stack Tecnológico

```
┌─────────────────────────────────────────────────────────────┐
│  LAYER 1: PRESENTATION (ZKoss + Bootstrap 5)              │
│  • 742 pantallas .zul                                     │
│  • 49 ViewModels (MVVM pattern)                            │
│  • Bootstrap 5 para estilos                              │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│  LAYER 2: ORCHESTRATION (Flowable BPMN 6.8.1)             │
│  • 17 Procesos BPMN 2.0 Certificables                     │
│  • ParallelGateway, BusinessRuleTask, Error Boundaries    │
│  • Timer Boundary Events (SLAs)                           │
│  • MailTask (notificaciones)                              │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│  LAYER 3: BUSINESS RULES (Drools Engine)                  │
│  • 11 Fact Objects                                        │
│  • 11 DRL files (120+ reglas)                              │
│  • Modificables sin redeploy                               │
│  • Versionadas y auditables                                │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│  LAYER 4: SERVICES (Spring Boot 2.7.3)                     │
│  • 65 Java Delegates (JavaDelegate)                        │
│  • 13 Services de negocio                                  │
│  • TaskManagementService                                   │
│  • ComplianceCheckService                                  │
│  • RiskAssessmentService                                   │
│  • DroolsRulesService                                      │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│  LAYER 5: PERSISTENCE (PostgreSQL + JPA)                   │
│  • 489 Entidades JPA (generadas por Enart Framework)      │
│  • PostgreSQL 14+                                           │
│  • MinIO (Object Storage)                                  │
│  • Qdrant (Vector DB)                                      │
└─────────────────────────────────────────────────────────────┘
```

---

## 📁 ESTRUCTURA DE DIRECTORIOS

```
suinsit.nova.web/
├── src/main/
│   ├── java/com/codeflowx/
│   │   ├── framework/              # Framework base
│   │   │   └── zkoss/
│   │   │       └── BaseFront.java  # Clase base para ViewModels
│   │   ├── govern/
│   │   │   ├── jpa/                 # Entidades JPA (489)
│   │   │   ├── viewmodel/           # CRUD tradicional (49 ViewModels)
│   │   │   │   ├── agents/         # Gestión de agentes
│   │   │   │   ├── analytics/      # Dashboards y KPIs
│   │   │   │   ├── catalog/        # Catálogo
│   │   │   │   ├── models/        # Gestión de modelos
│   │   │   │   └── ...            # CRUD normal
│   │   │   └── workflow/            # BPMN + Drools
│   │   │       ├── delegates/      # 65 Java Delegates
│   │   │       ├── viewmodels/    # 25 ViewModels BPMN (User Tasks)
│   │   │       ├── drools/         # Reglas Drools (11 DRL)
│   │   │       └── services/       # Services
│   │   └── platform/
│   │       └── viewmodels/
│   │           └── playground/   # 🆕 NUEVO: Sandbox (Experimentación)
│   │               ├── prompts/   # Testear prompts
│   │               ├── models/    # Interactuar con modelos
│   │               └── agents/    # Testear agentes
│   ├── resources/
│   │   ├── processes/              # 17 archivos .bpmn
│   │   ├── rules/                   # Configuración Drools
│   │   └── application.yml          # Configuración principal
│   └── webapp/
│       └── console/
│           ├── bpmn/               # 25 pantallas BPMN
│           └── ...                  # 742 pantallas .zul totales
└── docs/                           # Documentación completa

📚 nocode.service/                   # PROYECTO DE LIBRERÍAS
├── entities/                       # 489 entidades JPA
│   └── @Entidad (Enart Framework)
└── sources/sql/                   # SQL Scripts
    ├── functions/                  # Wrappers simplificados
    ├── procedures/                 # Wrappers simplificados
    └── views/                      # Vistas SQL

📊 suinsit.nova.web/sql/            # SCRIPTS SQL DEL PROYECTO
├── 00_drop_tables.sql             # DROP de todas las tablas
├── 01_create_tables.sql           # CREATE (161 tablas)
├── 02_indexes.sql                 # 740 índices
├── 03_foreign_keys.sql            # 93 Foreign Keys
├── 04_comments.sql                # Documentación
├── 05_triggers.sql                # Triggers auditoría
├── functions/                     # SQL Functions (wrappers)
│   ├── agents/                    # fn_calculate_agent_efficiency
│   ├── governance/
│   ├── infrastructure/
│   └── models/
├── procedures/                    # SQL Procedures (wrappers)
│   ├── agents/                    # sp_execute_agent_workflow
│   ├── governance/
│   └── ...
└── views/                         # SQL Views
```
```

### 🎯 Separación de Responsabilidades

**1. govern/viewmodel/** → **CRUD tradicional**
- Purpose: Gestión de master data (Agent, Model, Prompt, etc.)
- Files: 49 ViewModels
- Example: `AgentsDetailViewModel.java`
- Persist: ✅ Si, guarda en BD

**2. govern/workflow/viewmodels/** → **SOLO pantallas BPMN**
- Purpose: User Tasks durante procesos BPMN
- Files: 25 ViewModels
- Example: `AgentApprovalHumanOverrideViewModel.java`
- Persist: ⚠️ Solo durante el proceso

**3. platform/viewmodels/playground/** → **Experimentación (🆕)**
- Purpose: Sandbox para experimentar
- Files: Nuevos (a crear)
- Example: `PromptPlaygroundViewModel.java`
- Persist: ❌ No (modo sandbox)

---

## 🎯 PROCESOS BPMN (17)

### Aprobación (3)
1. **agent-approval-v1.bpmn** - Aprobación de agentes IA
2. **model-approval-v1.bpmn** - Aprobación de modelos ML
3. **prompt-approval-v1.bpmn** - Aprobación de prompts

### Detección (4)
4. **bias-detection-v1.bpmn** - Detección de sesgo
5. **drift-detection-v1.bpmn** - Detección de drift
6. **performance-degradation-v1.bpmn** - Detección de degradación
7. **alert-response-v1.bpmn** - Respuesta a alertas

### Evaluación (3)
8. **llm-evaluation-v1.bpmn** - Evaluación de LLMs
9. **rag-evaluation-v1.bpmn** - Evaluación de sistemas RAG
10. **model-evaluation-v1.bpmn** - Evaluación de modelos

### Automatización (2)
11. **deployment-automation-v1.bpmn** - Automatización de despliegues
12. **incident-response-rca-v1.bpmn** - Root Cause Analysis

### Governance (5)
13. **compliance-monitoring-v1.bpmn** - Monitoreo de compliance
14. **ethics-review-v1.bpmn** - Revisión ética
15. **risk-assessment-v1.bpmn** - Evaluación de riesgos
16. **dataset-quality-v1.bpmn** - Calidad de datasets
17. **model-retraining-orchestration-v1.bpmn** - Orquestación de re-entrenamiento

---

## 🔧 COMPONENTES CLAVE

### 1. ViewModels (49 archivos)

**Propósito:** Gestionan el estado de la UI siguiendo el patrón MVVM de ZKoss.

**Estructura típica:**
```java
@ViewModel
public class AgentApprovalWorkflowViewModel extends BaseFront {
    
    @WireVariable
    private AgentApprovalWorkflowService service;
    
    private List<AgentApproval> approvalsList;
    
    @NotifyChange("approvalsList")
    public void loadApprovals() {
        // Cargar datos
    }
    
    public void approveAgent(Long agentId) {
        service.approveAgent(agentId);
        // Actualizar UI
    }
}
```

**Categorías:**
- **Agents** - Gestión de agentes IA
- **Analytics** - Dashboards y KPIs
- **Catalog** - Catálogo de modelos
- **Dashboard** - Panel principal
- **Governance** - Gobierno y compliance
- **Models** - Gestión de modelos ML
- **Monitoring** - Monitoreo en tiempo real
- **Training** - Experimentos y entrenamiento

### 2. Java Delegates (65 archivos)

**Propósito:** Contienen la lógica de negocio ejecutada por Flowable.

**Ejemplo:**
```java
@Slf4j
@Component("riskAssessmentDelegate")
public class RiskAssessmentDelegate implements JavaDelegate {
    
    @Autowired
    private RiskAssessmentService service;
    
    @Override
    public void execute(DelegateExecution execution) {
        Long agentId = (Long) execution.getVariable("agentId");
        RiskAssessment result = service.assessRisk(agentId);
        
        execution.setVariable("riskScore", result.getScore());
        execution.setVariable("riskLevel", result.getLevel());
        
        log.info("Risk assessment completed for agentId: {}", agentId);
    }
}
```

**Tipos de Delegates:**
- **Assessment** - Evaluaciones (Risk, Compliance, Ethics)
- **Detection** - Detección (Bias, Drift, Performance)
- **Approval** - Aprobaciones automáticas
- **Notification** - Notificaciones
- **Storage** - Guardado de datos
- **Evaluation** - Evaluaciones técnicas

### 3. Services (13+ servicios)

**Principales:**
- `TaskManagementService` - Gestión de tareas BPMN
- `DroolsRulesService` - Ejecución de reglas
- `ComplianceCheckService` - Verificación de compliance
- `RiskAssessmentService` - Evaluación de riesgos
- `BusinessService` - CRUD genérico (heredado de BaseFront)

### 4. Entidades JPA (489 archivos)

**Generadas por:** Enart Framework (nuestro framework custom)

**Estructura:**
```java
@Entity
@Table(name = "agent_approval")
public class AgentApproval {
    private Long id;
    private Long agentId;
    private String status;
    private Integer riskScore;
    private Integer complianceScore;
    // ... más campos
}
```

**Organización:**
- Funciones SQL → `functions/`
- Procedimientos → `procedures/`
- Vistas → `views/`
- Tablas → JPA entities

---

## 🔄 FLUJOS DE TRABAJO

### Ejemplo: Agent Approval Flow

```
1. START
   ↓
2. ParallelGateway (Fork)
   ├─→ [Risk Assessment] → riskScore
   ├─→ [Compliance Check] → complianceScore
   └─→ [Ethics Review] → ethicsScore
   ↓
3. Join (esperar las 3 evaluaciones)
   ↓
4. BusinessRuleTask (Drools)
   - Input: AgentApprovalFact
   - Rules: agent-scoring.drl
   - Output: decision (AUTO_APPROVE | HITL | AUTO_REJECT)
   ↓
5. ExclusiveGateway
   ├─→ AUTO_APPROVE → [AutoApproveAgentDelegate] → End
   ├─→ HITL → [UserTask] → [Human Decision] → End
   └─→ AUTO_REJECT → [AutoRejectAgentDelegate] → End
```

---

## 🎨 FRONTEND (ZKoss + Bootstrap)

### Patrón de UI

**Antes (ZKoss layout components):**
```xml
<borderlayout>
  <north>...</north>
  <center>...</center>
  <south>...</south>
</borderlayout>
```

**Ahora (HTML + Bootstrap):**
```xml
<div class="row">
  <div class="col-12">
    <div class="card">
      <!-- Content -->
    </div>
  </div>
</div>
```

**Beneficios:**
- ✅ Más fácil de migrar a React/Angular
- ✅ Estilos consistentes con Bootstrap
- ✅ Mejor mantenibilidad

### Pantallas Principales

- **Dashboard** - Panel de control principal
- **Task Inbox** - Bandeja de tareas BPMN
- **Agent Approval** - Aprobación de agentes
- **Model Catalog** - Catálogo de modelos
- **Analytics** - Dashboards y métricas
- **Compliance** - Monitoreo de compliance

---

## 🧪 MODO DEMO

### Implementación

**Características:**
- Datos simulados cuando Flowable está deshabilitado
- Mock data realista (nombres, roles, fechas)
- Auto-detección de modo mock
- Sin dependencia de BPMN engine para demos

**Configuración:**
```yaml
# application.yml
flowable:
  enabled: false

bpmn:
  service:
    mock-mode: true
```

### Uso en ViewModels

```java
@WireVariable(required = false)
private TaskManagementService taskManagementService;

public void loadTasks() {
    if (taskManagementService == null) {
        // Auto-detect mock mode
        loadMockTasks();
    } else {
        // Load real tasks
        tasks = taskManagementService.getTasks();
    }
}
```

---

## 📚 DOCUMENTACIÓN

### Documentos Disponibles

1. **MANUAL_DESARROLLADOR_PROCESOS_BPMN.md** (685 líneas)
   - Manual técnico completo de los 17 procesos
   - Referencia de Java Delegates
   - Buenas prácticas

2. **ARQUITECTURA_CODEFLOWX_GOVERN.md** (542 líneas)
   - Arquitectura general del sistema
   - Stack tecnológico
   - Estadísticas del proyecto

3. **PRESENTACION_COMERCIAL_DEVELOPERS.md** (1,956 líneas)
   - Manual comercial con 22 procesos
   - Casos de uso por industria
   - Argumentos de venta

4. **ARQUITECTURA_ADAPTACION_MODELOS_V1.md** (619 líneas)
   - Arquitectura de adaptación de modelos
   - Nuevos procesos v1.1.0

5. **DOCUMENTACION_TECNICA_PROCESOS_BPMN.md** (83 líneas)
   - Documentación técnica resumida

---

## 🔮 ROADMAP (Próximos Pasos)

### Versión 1.2.0
- [ ] Microservicio BPMN separado
- [ ] Migración total a Bootstrap (eliminar ZKoss)
- [ ] 5 nuevos procesos de adaptación

### Versión 1.3.0
- [ ] Framework-as-Software (FaaS)
- [ ] 62 DMN tables
- [ ] 8 CMMN cases

### Objetivo Final
- **67 Procesos BPMN** totales
- **Plataforma "Salesforce de AI Governance"**
- **Posicionamiento de mercado líder**

---

## 💡 DECISIONES ARQUITECTÓNICAS CLAVE

### 1. Flowable vs Spring Boot Version

**Problema:** Flowable 7.x requiere Spring Boot 3.x, pero usamos 2.7.3

**Solución:** 
- Frontend usa Flowable 6.8.1
- Microservicio BPMN usará Flowable 7.x
- Compatibilidad vía REST API

### 2. ViewModels Delgados

**Principio:** ViewModels solo para UI, lógica de negocio en Services/Delegates

**Implementación:**
- ViewModels usan Services
- Delegates implementan business logic
- Drools maneja reglas de negocio

### 3. SQL Functions/Procedures

**Realidad:** Son wrappers sin lógica de negocio

**Propósito:** Invocados vía entidades JPA generadas

**Arquitectura:**
- Delegates ejecutan funciones SQL
- Business logic en Delegates/Drools
- SQL solo como abstracción de datos

### 4. UI Migration Strategy

**Estrategia:** Migrar de ZKoss a React/Angular

**Tácticas:**
- Usar HTML estándar + Bootstrap
- Evitar componentes ZKoss específicos
- Preparar ViewModels para API REST

---

## 📊 MÉTRICAS DEL PROYECTO

```
PROYECTO CODEFLOWX GOVERN - ESTADÍSTICAS

LÍNEAS DE CÓDIGO:
├─ Java: ~60,000 líneas
├─ XML (ZUL): ~35,000 líneas
├─ BPMN: ~8,000 líneas
├─ Drools (DRL): ~1,500 líneas
├─ YML/Config: ~500 líneas
└─ Documentación: ~5,000 líneas

TOTAL: ~110,000 líneas de código

TESTS:
├─ Unit Tests: Pendiente
├─ Integration Tests: Pendiente
└─ E2E Tests: Pendiente

DOCUMENTACIÓN:
├─ Técnica: 5 documentos (~2,500 líneas)
├─ Arquitectura: 3 documentos (~1,800 líneas)
├─ Comercial: 4 documentos (~3,000 líneas)
└─ READMEs: 8 documentos (~500 líneas)

TOTAL DOCUMENTACIÓN: ~7,800 líneas
```

---

## 🎯 CONVENCIONES DEL PROYECTO

### Nombres de Archivos
- **BPMN:** `{entity}-{action}-v{version}.bpmn`
- **Delegates:** `{Action}{Entity}Delegate.java`
- **ViewModels:** `{Entity}{Action}ViewModel.java`
- **DRL:** `{entity}-{action}.drl`
- **Facts:** `{Entity}Fact.java`

### Configuración
- YAML: `application.yml`
- Logging: `logback-spring.xml`
- Database: `schema.sql`

### Testing
- BaseFront heredado por ViewModels
- Mock services para desarrollo
- Logs DEBUG para troubleshooting

---

## 🚀 PRÓXIMAS ACCIONES

1. **Completar microservicio BPMN**
2. **Aumentar cobertura de tests**
3. **Implementar Framework-as-Software**
4. **Migrar a React/Angular**
5. **Escalar a 67 procesos totales**

---

**FIN DEL DOCUMENTO**

