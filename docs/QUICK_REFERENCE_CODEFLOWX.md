# ⚡ QUICK REFERENCE - CODEFLOWX GOVERN

**Para cuando pierdas el contexto del proyecto**

---

## 📦 PROYECTO RÁPIDO

**Nombre:** CodeflowX AI Governance Platform  
**Stack:** Spring Boot 2.7.3 + Flowable BPMN 6.8.1 + ZKoss + PostgreSQL  
**Ubicación:** `/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web`  
**Estado:** Desarrollo Activo (v1.1.0)

---

## 🎯 QUÉ ES ESTO

Plataforma enterprise para gobierno completo de IA:
- ✅ 17 Procesos BPMN automatizados
- ✅ Motor de reglas Drools (120+ reglas)
- ✅ 489 Entidades JPA
- ✅ 49 ViewModels (UI)
- ✅ 65 Java Delegates (lógica de negocio)
- ✅ 742 Pantallas ZUL
- ✅ Governance end-to-end de IA

### 📊 Comparación con Next.js Original
| Aspecto | ZKoss (Actual) | Next.js (Original) |
|---------|----------------|-------------------|
| **Pantallas** | 742 ZUL | ~200 páginas TSX |
| **Estructura** | 4 directorios principales | 15 módulos funcionales |
| **Organización** | Por tipo (dashboards/platform/gobierno/bpmn) | Por funcionalidad (agents/models/governance) |
| **BPMN** | 25 pantallas específicas | Integrado en módulos |
| **Dashboard** | 10 dashboards especializados | 1 dashboard principal |
| **CRUD** | Overview + Detail por entidad | Páginas individuales por acción |

---

## 📁 RUTAS CRÍTICAS

### Código Principal (ZKoss)
```bash
src/main/java/com/codeflowx/
├── govern/viewmodel/              # CRUD + Details (Master Data)
│   ├── agents/AgentsDetailViewModel.java
│   ├── agents/AgentsDashboardViewModel.java
│   └── ... (49 ViewModels para gestión)
├── govern/workflow/viewmodels/    # SOLO pantallas BPMN (User Tasks)
│   ├── TaskInboxViewModel.java
│   ├── AgentApprovalHumanOverrideViewModel.java
│   └── ... (25 ViewModels de BPMN)
├── govern/workflow/delegates/     # 65 Delegates (lógica negocio)
├── govern/workflow/drools/        # Reglas Drools (11 DRL)
└── platform/viewmodels/playground/  # NUEVO: Experimentación (sandbox)
    ├── prompts/PromptPlaygroundViewModel.java
    ├── models/ModelPlaygroundViewModel.java
    └── ...
```

### Pantallas UI (ZKoss)
```bash
src/main/webapp/console/
├── dashboards/                    # 10 dashboards (visualizaciones)
│   ├── dashboard.zul              # Dashboard principal
│   ├── agentdashboard.zul        # Dashboard de agentes
│   ├── compliance.zul             # Dashboard de compliance
│   └── ...
├── platform/                      # CRUD Manual (no BPMN)
│   ├── agents/                    # Overview + Detail
│   ├── models/                    # Overview + Detail
│   ├── prompts/                   # Overview + Detail
│   └── ...
├── gobierno/                      # Gobernanza específica
│   ├── analytics/                 # Métricas y reportes
│   ├── compliance/                # Cumplimiento normativo
│   ├── ethics/                    # Evaluaciones éticas
│   └── core/                      # Roles y permisos
└── bpmn/                         # 25 pantallas BPMN (User Tasks)
    ├── task-inbox.zul             # Bandeja de tareas
    ├── agent-approval-human-override-form.zul
    └── ...
```

### Referencia Next.js (Original)
```bash
codeflowx-govern-demo-1.1.0/app/(app)/
├── dashboard/                     # Dashboard principal
├── agents/                        # Gestión de agentes
│   ├── approval/                  # Aprobación de agentes
│   ├── create/                    # Creación de agentes
│   ├── decisions/                 # Decisiones de agentes
│   ├── ethics/                    # Ética de agentes
│   ├── interactions/              # Interacciones
│   ├── learning/                  # Aprendizaje
│   ├── overview/                  # Vista general
│   ├── registry/                  # Registro
│   ├── rollback/                  # Rollback
│   └── versioning/                # Versionado
├── models/                        # Gestión de modelos
│   ├── approval/                  # Aprobación de modelos
│   ├── bias-analysis/             # Análisis de sesgo
│   ├── compliance/                # Cumplimiento
│   ├── create/                    # Creación
│   ├── dependencies/              # Dependencias
│   ├── explainability/            # Explicabilidad
│   ├── overview/                  # Vista general
│   ├── performance/               # Rendimiento
│   ├── registry/                  # Registro
│   ├── rollback/                  # Rollback
│   └── versioning/                # Versionado
├── governance/                    # Gobernanza
│   ├── auto-approval/             # Auto-aprobación
│   ├── compliance/                # Cumplimiento
│   ├── monitoring/                # Monitoreo
│   ├── policies/                  # Políticas
│   ├── risk-assessment/          # Evaluación de riesgo
│   ├── security/                  # Seguridad
│   └── services/                  # Servicios
├── compliance/                    # Cumplimiento específico
│   ├── ai-act/                    # AI Act
│   ├── audit/                     # Auditoría
│   ├── automated-checks/          # Verificaciones automáticas
│   ├── frameworks/                # Marcos normativos
│   ├── reports/                   # Reportes
│   └── risk-assessment/          # Evaluación de riesgo
├── analytics/                     # Analíticas
│   ├── accountability/            # Responsabilidad
│   ├── bias/                      # Sesgo
│   ├── fairness/                  # Equidad
│   ├── impact/                    # Impacto
│   └── transparency/               # Transparencia
├── ethics/                        # Ética
│   ├── assessments/               # Evaluaciones
│   ├── committee/                 # Comité
│   ├── impact/                    # Impacto
│   ├── mitigation/                # Mitigación
│   └── violations/                # Violaciones
├── evaluation/                    # Evaluación
│   ├── ab-testing/                # A/B Testing
│   ├── hitl/                      # Human-in-the-Loop
│   ├── llm-evaluation/            # Evaluación LLM
│   ├── metrics/                   # Métricas
│   ├── pipelines/                 # Pipelines
│   ├── rag-evaluation/            # Evaluación RAG
│   ├── reports/                   # Reportes
│   ├── security/                  # Seguridad
│   ├── services/                  # Servicios
│   └── test-cases/                # Casos de prueba
├── monitoring/                    # Monitoreo
│   ├── alerts/                    # Alertas
│   ├── explainability/            # Explicabilidad
│   ├── performance/               # Rendimiento
│   ├── real-time/                 # Tiempo real
│   ├── trends/                    # Tendencias
│   └── trust/                     # Confianza
├── prompts/                       # Gestión de prompts
│   ├── approval/                  # Aprobación
│   ├── ethics/                    # Ética
│   ├── overview/                  # Vista general
│   ├── registry/                  # Registro
│   ├── rollback/                  # Rollback
│   ├── security/                  # Seguridad
│   ├── templates/                 # Plantillas
│   ├── validation/                # Validación
│   └── versioning/                # Versionado
├── rag/                           # Sistemas RAG
│   ├── access-control/            # Control de acceso
│   ├── bias-detection/            # Detección de sesgo
│   ├── compliance/                # Cumplimiento
│   ├── data-sources/              # Fuentes de datos
│   ├── monitoring/                # Monitoreo
│   ├── overview/                  # Vista general
│   ├── quality-control/           # Control de calidad
│   ├── registry/                  # Registro
│   ├── rollback/                  # Rollback
│   └── versioning/                # Versionado
├── projects/                      # Gestión de proyectos
│   ├── create/                    # Creación
│   ├── edit/                      # Edición
│   └── settings/                  # Configuración
├── mlflow/                        # MLflow Integration
│   ├── artifacts/                 # Artefactos
│   ├── comparison/                # Comparación
│   ├── evaluations/               # Evaluaciones
│   ├── experiments/               # Experimentos
│   ├── overview/                  # Vista general
│   ├── runs/                      # Ejecuciones
│   ├── sync/                      # Sincronización
│   └── webhooks/                  # Webhooks
├── catalog/                       # Catálogo
│   ├── agents/                    # Agentes
│   ├── components/                # Componentes
│   ├── data/                      # Datos
│   ├── models/                    # Modelos
│   ├── prompts/                   # Prompts
│   ├── services/                  # Servicios
│   └── types/                     # Tipos
└── admin/                         # Administración
    ├── explainability-techniques/  # Técnicas de explicabilidad
    └── page.tsx                   # Panel de administración
```

### Proyectos Relacionados
```bash
nocode.service/                     # 📚 PROYECTO DE LIBRERÍAS REUTILIZABLES
├── sources/sql/                   # SQL Scripts
│   ├── functions/                 # SQL Functions (wrappers)
│   ├── procedures/                # SQL Procedures (wrappers)
│   └── views/                     # SQL Views
└── JPA Entities/                  # 489 entidades JPA generadas
    └── entities/                  # Wrappeadas por Enart Framework
```

### Recursos
```bash
src/main/resources/
├── processes/     # 17 archivos .bpmn
├── rules/        # Configuración Drools
└── application.yml

src/main/webapp/console/
└── *.zul         # 742 pantallas UI

sql/                              # 📊 SCRIPTS SQL
├── 00_drop_tables.sql            # DROP de todas las tablas
├── 01_create_tables.sql          # CREATE TABLE (161 tablas)
├── 02_indexes.sql                # 740 índices
├── 03_foreign_keys.sql           # 93 Foreign Keys
├── 04_comments.sql               # Documentación SQL
├── 05_triggers.sql               # Triggers de auditoría
├── functions/                    # SQL Functions (wrappers simplificados)
│   ├── agents/                   # fn_calculate_agent_efficiency, etc.
│   ├── governance/
│   ├── infrastructure/
│   ├── models/
│   └── ...
├── procedures/                    # SQL Procedures (wrappers simplificados)
│   ├── agents/                   # sp_execute_agent_workflow, etc.
│   ├── governance/
│   └── ...
├── views/                        # SQL Views
└── mock_data/                    # Datos de prueba
```

### Documentación
```bash
docs/
├── VISION_PROYECTO_CODEFLOWX_GOVERN.md         # VISIÓN COMPLETA
├── MANUAL_DESARROLLADOR_PROCESOS_BPMN.md        # Manual técnico
├── ARQUITECTURA_CODEFLOWX_GOVERN.md             # Arquitectura
└── README_DOCS.md                               # Índice
```

---

## 🔧 COMANDOS ÚTILES

### Navegación Rápida
```bash
# Contar ViewModels
find src/main/java/com/codeflowx/govern/viewmodel -name "*ViewModel.java" | wc -l

# Contar Delegates
find src/main/java/com/codeflowx/govern/workflow/delegates -name "*.java" | wc -l

# Contar procesos BPMN
find src/main/resources/processes -name "*.bpmn" | wc -l

# Ver archivos Java totales
find src/main/java/com/codeflowx -name "*.java" | wc -l
```

### Git Útil
```bash
# Ver historial reciente
git log --oneline -10

# Ver cambios en archivos markdown
git log --all --oneline --name-status | grep "\.md"
```

---

## 🏗️ ARQUITECTURA (RESUMEN)

```
Frontend (ZKoss)
    ↓
Flowable BPMN (17 procesos)
    ↓
Java Delegates (65)
    ↓
Services + Drools (reglas)
    ↓
PostgreSQL (489 JPA entities)
```

### 📐 ARQUITECTURA DE VIEWMODELS

```
com.codeflowx.govern.viewmodel/           # CRUD tradicional
├─ agents/AgentsDetailViewModel.java
├─ agents/AgentsDashboardViewModel.java
└─ ... (49 ViewModels para gestión)

com.codeflowx.govern.workflow.viewmodels/ # SOLO pantallas BPMN
├─ TaskInboxViewModel.java
├─ AgentApprovalHumanOverrideViewModel.java
└─ ... (25 ViewModels de User Tasks BPMN)

com.codeflowx.platform.viewmodels/playground/  # Sandbox (NUEVO)
├─ prompts/PromptPlaygroundViewModel.java
├─ models/ModelPlaygroundViewModel.java
└─ ... (Experimentación, no persiste BD)
```

### 📄 ARQUITECTURA DE PANTALLAS ZUL

```
src/main/webapp/console/
├── bpmn/                            # 🎯 PROCESOS BPMN (25 pantallas)
│   ├── task-inbox.zul               # Bandeja de tareas
│   ├── agent-approval-human-override-form.zul
│   ├── model-approval-human-override-form.zul
│   ├── compliance-review-form.zul
│   └── ... (User Tasks de procesos)
│
├── platform/                        # 📝 CRUD PLATFORM (Listado + Detalle)
│   ├── agents/                      # AgentsDetail + List
│   ├── models/                      # ModelsDetail + List
│   ├── prompts/                     # PromptsDetail + List
│   ├── playground/                  # 🆕 Sandbox (experimentación)
│   └── ... (gestión de datos)
│
├── gobierno/                        # 🛡️ GOVERNANCE (Alta/Consulta)
│   ├── governance/                  # governance-detail.zul
│   ├── analytics/
│   ├── compliance/
│   └── ... (governance específicas)
│
└── dashboards/                      # 📊 DASHBOARDS
    ├── dashboard.zul                # Dashboard principal
    ├── main-dashboard.zul
    ├── agentdashboard.zul
    ├── compliance.zul
    ├── costdashboard.zul
    ├── executive.zul
    └── ... (10 dashboards)
```

**Propósito de cada directorio:**
- `bpmn/` → **User Tasks BPMN** (formularios durante procesos)
- `platform/` → **CRUD tradicional** (alta/edición/consulta normal)
- `gobierno/` → **Alta/consulta governance** (governance específico)
- `dashboards/` → **Visualizaciones y métricas**
- `platform/playground/` → **Sandbox** (experimentación sin persistir)

**Flujo típico:**
1. Usuario → ViewModel (CRUD)
2. ViewModel → Service
3. Service → Inicia proceso BPMN
4. BPMN → Llama Delegate
5. Delegate → Ejecuta lógica
6. Delegate → Ejecuta Drools (opcional)
7. BPMN → Actualiza estado
8. BPMN → Notifica ViewModel (Workflow)

---

## 📝 PATRONES CLAVE

### ViewModel Típico
```java
@ViewModel
public class AgentApprovalWorkflowViewModel extends BaseFront {
    
    @WireVariable
    private AgentApprovalWorkflowService service;
    
    private List<AgentApproval> approvalsList;
    
    @NotifyChange("approvalsList")
    public void loadApprovals() {
        // Lógica
    }
}
```

### Delegate Típico
```java
@Slf4j
@Component("myDelegate")
public class MyDelegate implements JavaDelegate {
    
    @Autowired
    private MyService service;
    
    @Override
    public void execute(DelegateExecution execution) {
        Long id = (Long) execution.getVariable("entityId");
        Result result = service.doWork(id);
        execution.setVariable("result", result);
    }
}
```

### Regla Drools Típica
```drl
rule "My Rule"
when
  $fact : MyFact(score >= 80)
then
  $fact.setDecision("APPROVED");
end
```

---

## 📚 PROYECTO nocode.service (Librerías)

**Ubicación:** `/mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service`  
**Propósito:** Proyecto de librerías reutilizables (JPA entities, DTOs, SQL wrappers)

### Estructura
```
nocode.service/
├── sources/sql/                    # Scripts SQL
│   ├── functions/                  # SQL Functions (wrappers)
│   ├── procedures/                 # SQL Procedures (wrappers)
│   └── views/                      # SQL Views
└── entities/                        # 489 entidades JPA generadas
    └── JPA con @Entidad (Enart Framework)
```

### Uso
- ✅ Las entidades se importan como dependencia Maven
- ✅ SQL functions/procedures son **wrappers simplificados** (sin lógica de negocio)
- ✅ Se usan para: **actualizaciones masivas, cálculos simples en BD**
- ✅ La lógica real está en **Delegates** y **Drools**
- ✅ Wrappeadas por Enart Framework (framework custom)
- ✅ Ver ejemplos en: `suinsit.nova.web/sql/functions/` y `sql/procedures/`

**Convención:**
- **Functions:** `fn_*` → Retornan valores (cálculos)
- **Procedures:** `sp_*` → Ejecutan acciones (operaciones)

---

## 🎯 PROCESOS BPMN

| # | Nombre | Propósito |
|---|--------|-----------|
| 1 | agent-approval-v1.bpmn | Aprobar agentes IA |
| 2 | model-approval-v1.bpmn | Aprobar modelos ML |
| 3 | prompt-approval-v1.bpmn | Aprobar prompts |
| 4 | bias-detection-v1.bpmn | Detectar sesgo |
| 5 | drift-detection-v1.bpmn | Detectar drift |
| 6 | performance-degradation-v1.bpmn | Detectar degradación |
| 7 | alert-response-v1.bpmn | Responder alertas |
| 8 | llm-evaluation-v1.bpmn | Evaluar LLMs |
| 9 | rag-evaluation-v1.bpmn | Evaluar RAG |
| 10 | model-evaluation-v1.bpmn | Evaluar modelos |
| 11 | deployment-automation-v1.bpmn | Automatizar despliegue |
| 12 | incident-response-rca-v1.bpmn | RCA de incidentes |
| 13 | compliance-monitoring-v1.bpmn | Monitoreo compliance |
| 14 | ethics-review-v1.bpmn | Revisión ética |
| 15 | risk-assessment-v1.bpmn | Evaluar riesgos |
| 16 | dataset-quality-v1.bpmn | Calidad datasets |
| 17 | model-retraining-orchestration-v1.bpmn | Re-entrenamiento |

---

## 🐛 DEBUGGING RÁPIDO

### Ver logs de Flowable
```yaml
# application.yml
logging:
  level:
    org.flowable: DEBUG
```

### Ver logs de Drools
```yaml
logging:
  level:
    org.drools: DEBUG
```

### Mock Mode (para demos)
```yaml
flowable:
  enabled: false
  
bpmn:
  service:
    mock-mode: true
```

---

## 📚 DOCUMENTOS ÚTILES

| Documento | Para | Líneas |
|-----------|------|--------|
| `VISION_PROYECTO_CODEFLOWX_GOVERN.md` | Visión completa | 526 |
| `MANUAL_DESARROLLADOR_PROCESOS_BPMN.md` | Técnico 17 procesos | 686 |
| `ARQUITECTURA_CODEFLOWX_GOVERN.md` | Arquitectura detallada | 542 |
| `PRESENTACION_COMERCIAL_DEVELOPERS.md` | Manual comercial | 1,956 |

---

## ⚠️ DECISIONES IMPORTANTES

### 1. Flowable Versioning
- **Problema:** Flowable 7.x requiere Spring Boot 3.x
- **Solución:** Frontend usa 6.8.1, microservicio usará 7.x
- **Archivo:** `pom.xml` línea `flowable.version=6.8.1`

### 2. ViewModels Delgados
- **Principio:** Solo UI, lógica en Services/Delegates
- **Clase base:** `BaseFront.java`
- **Patrón:** ViewModel → Service → Delegate

### 3. SQL Functions
- **Realidad:** Son wrappers sin lógica
- **Lógica real:** En Delegates y Drools
- **Ubicación:** `nocode.service/entities/`

### 4. UI Migration
- **Estrategia:** Migrar de ZKoss a React/Angular
- **Acción:** Usar HTML estándar + Bootstrap
- **Evitar:** Componentes ZKoss específicos

---

## 🔍 BUSCAR CÓDIGO

### Buscar ViewModels
```bash
find src/main/java/com/codeflowx/govern/viewmodel -name "*.java"
```

### Buscar Delegates
```bash
find src/main/java/com/codeflowx/govern/workflow/delegates -name "*.java"
```

### Buscar procesos BPMN
```bash
find src/main/resources/processes -name "*.bpmn"
```

### Buscar en código Java
```bash
# Buscar "AgentApproval" en todo el proyecto
grep -r "AgentApproval" src/main/java/

# Buscar clases que implementan JavaDelegate
grep -r "implements JavaDelegate" src/main/java/
```

---

## 🎨 CONVENCIONES

### Nombres de Archivos
- **BPMN:** `{entity}-{action}-v{version}.bpmn`
- **Delegates:** `{Action}{Entity}Delegate.java`
- **ViewModels:** `{Entity}{Action}ViewModel.java`
- **DRL:** `{entity}-{action}.drl`

### Anotaciones
```java
@Component("delegateName")        // En Delegates
@ViewModel                        // En ViewModels
@WireVariable                     // Inyección ZKoss
@NotifyChange("propertyName")    // Notificar cambio UI
```

---

## 🚀 ACCIONES COMUNES

### Añadir un nuevo proceso
1. Crear `.bpmn` en `src/main/resources/processes/`
2. Crear Delegate(s) en `delegates/`
3. Crear reglas Drools (si aplica) en `drools/`
4. Crear ViewModel en `viewmodel/`
5. Crear pantalla `.zul` en `webapp/`

### Debuggear un proceso
1. Verificar logs: `logging.level.org.flowable=DEBUG`
2. Buscar variables: `execution.getVariable()`
3. Ver procesos activos: Flowable UI
4. Ver instancias: `ACT_RU_EXECUTION` table

### Mockear datos
```java
@WireVariable(required = false)
private MyService service;

public void load() {
    if (service == null) {
        loadMockData();
    }
}
```

---

## 📞 CONTACTO RÁPIDO

- **Documentación:** Ver `docs/README_DOCS.md`
- **Arquitectura:** Ver `docs/VISION_PROYECTO_CODEFLOWX_GOVERN.md`
- **Procesos:** Ver `docs/MANUAL_DESARROLLADOR_PROCESOS_BPMN.md`
- **Código:** `src/main/java/com/codeflowx/`

---

## 🔑 MEMORIA RÁPIDA

```
PROYECTO: CodeflowX AI Governance
STACK: Spring Boot 2.7.3 + Flowable 6.8.1 + ZKoss + PostgreSQL
ARCHIVOS: 588 Java, 742 ZUL, 17 BPMN, 65 Delegates
ESTADO: Desarrollo activo v1.1.0
```

---

**Última actualización:** Octubre 2025  
**Versión Quick Reference:** 1.0

---

## ✅ CONFIRMACIÓN - PROCESO DE ALTA

### 📊 Estructura de Pantallas (Comprendido):

```
console/bpmn/         → 25 pantallas de User Tasks BPMN
console/platform/     → CRUD tradicional (listado + detalle)
console/gobierno/     → Alta/consulta de governance
console/dashboards/   → 10 dashboards visuales diferentes
```

### 🎯 Proceso de Alta (Comprendido):

**Alta Híbrida con Drools:**
1. Usuario crea entidad (Agent/Model/Prompt)
2. Alta directa con validación simple → estado DRAFT
3. Drools decide: AUTO_APPROVE | NEED_APPROVAL | AUTO_REJECT
4. Si NEED_APPROVAL → Inicia proceso BPMN completo
5. Lógica compleja en Delegates y Drools (no en ViewModels)

### 📈 Dashboards (Comprendido):

- **DashboardViewModel.java** → Dashboard principal con tareas
- **ViewModels** en: `govern/viewmodel/dashboard/`
- **ZUL** en: `dashboards/` (10 dashboards: agentdashboard, compliance, cost, executive, etc.)

**✅ TODO COMPRENDIDO Y DOCUMENTADO**

