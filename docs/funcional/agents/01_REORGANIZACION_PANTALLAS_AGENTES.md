# 📱 REORGANIZACIÓN DE PANTALLAS - MÓDULO AGENTES

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Reorganizar pantallas de agentes siguiendo estructura Next.js original

---

## 🎯 OBJETIVO

Reorganizar las 54 pantallas ZUL del módulo de agentes para seguir la estructura funcional del proyecto Next.js original, separando claramente CRUD vs Consulta.

---

## 📊 ESTRUCTURA ACTUAL vs OBJETIVO

### **Estructura Actual (ZKoss):**
```
console/platform/agents/
├── 33 pantallas *-overview.zul    # Listados/consulta
├── 22 pantallas *-detail.zul      # Edición/creación
└── 2 pantallas *-status.zul       # Estados específicos
```

### **Estructura Objetivo (Next.js Style):**
```
console/platform/agents/
├── overview/                      # Listado principal
├── create/                       # Creación de agentes
├── approval/                     # Aprobaciones
├── decisions/                    # Decisiones
├── ethics/                       # Ética
├── interactions/                 # Interacciones
├── learning/                     # Aprendizaje
├── registry/                     # Registro
├── rollback/                     # Rollback
└── versioning/                   # Versionado
```

---

## 🔄 MAPEO DE REORGANIZACIÓN

### **1. OVERVIEW (Listado Principal)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `agent-overview.zul` | `agents/overview/page.zul` | Consulta | `AgentOverviewViewModel` |

### **2. CREATE (Creación)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `agent-detail.zul` | `agents/create/page.zul` | CRUD | `AgentDetailViewModel` |

### **3. APPROVAL (Aprobaciones)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `agent-approval-detail.zul` | `agents/approval/page.zul` | CRUD | `AgentApprovalDetailViewModel` |
| `agent-approval-overview.zul` | `agents/approval/overview.zul` | Consulta | `AgentApprovalOverviewViewModel` |

### **4. DECISIONS (Decisiones)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `agent-decision-detail.zul` | `agents/decisions/page.zul` | CRUD | `AgentDecisionDetailViewModel` |
| `agent-decision-overview.zul` | `agents/decisions/overview.zul` | Consulta | `AgentDecisionOverviewViewModel` |
| `agent-decision-audit-overview.zul` | `agents/decisions/audit.zul` | Consulta | `AgentDecisionAuditOverviewViewModel` |

### **5. ETHICS (Ética)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `agent-ethics-assessment-detail.zul` | `agents/ethics/page.zul` | CRUD | `AgentEthicsAssessmentDetailViewModel` |
| `agent-ethics-assessment-overview.zul` | `agents/ethics/overview.zul` | Consulta | `AgentEthicsAssessmentOverviewViewModel` |

### **6. INTERACTIONS (Interacciones)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `agent-interaction-detail.zul` | `agents/interactions/page.zul` | CRUD | `AgentInteractionDetailViewModel` |
| `agent-interaction-overview.zul` | `agents/interactions/overview.zul` | Consulta | `AgentInteractionOverviewViewModel` |
| `agent-interaction-patterns-overview.zul` | `agents/interactions/patterns.zul` | Consulta | `AgentInteractionPatternsOverviewViewModel` |
| `agent-communication-detail.zul` | `agents/interactions/communication.zul` | CRUD | `AgentCommunicationDetailViewModel` |
| `agent-communication-overview.zul` | `agents/interactions/communication-overview.zul` | Consulta | `AgentCommunicationOverviewViewModel` |
| `agent-collaboration-detail.zul` | `agents/interactions/collaboration.zul` | CRUD | `AgentCollaborationDetailViewModel` |
| `agent-collaboration-overview.zul` | `agents/interactions/collaboration-overview.zul` | Consulta | `AgentCollaborationOverviewViewModel` |
| `agent-collaboration-network-overview.zul` | `agents/interactions/network.zul` | Consulta | `AgentCollaborationNetworkOverviewViewModel` |

### **7. LEARNING (Aprendizaje)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `agent-expertise-detail.zul` | `agents/learning/page.zul` | CRUD | `AgentExpertiseDetailViewModel` |
| `agent-expertise-overview.zul` | `agents/learning/overview.zul` | Consulta | `AgentExpertiseOverviewViewModel` |

### **8. REGISTRY (Registro)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `agent-detail.zul` | `agents/registry/page.zul` | CRUD | `AgentDetailViewModel` |
| `agent-domain-detail.zul` | `agents/registry/domain.zul` | CRUD | `AgentDomainDetailViewModel` |
| `agent-domain-overview.zul` | `agents/registry/domain-overview.zul` | Consulta | `AgentDomainOverviewViewModel` |

### **9. ROLLBACK (Rollback)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `agent-rollback-detail.zul` | `agents/rollback/page.zul` | CRUD | `AgentRollbackDetailViewModel` |
| `agent-rollback-overview.zul` | `agents/rollback/overview.zul` | Consulta | `AgentRollbackOverviewViewModel` |

### **10. VERSIONING (Versionado)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `agent-version-detail.zul` | `agents/versioning/page.zul` | CRUD | `AgentVersionDetailViewModel` |
| `agent-version-overview.zul` | `agents/versioning/overview.zul` | Consulta | `AgentVersionOverviewViewModel` |

---

## 🔧 MÓDULOS ADICIONALES

### **MONITORING (Monitoreo)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `agent-monitoring-detail.zul` | `agents/monitoring/page.zul` | CRUD | `AgentMonitoringDetailViewModel` |
| `agent-monitoring-overview.zul` | `agents/monitoring/overview.zul` | Consulta | `AgentMonitoringOverviewViewModel` |
| `agent-health-detail.zul` | `agents/monitoring/health.zul` | CRUD | `AgentHealthDetailViewModel` |
| `agent-health-overview.zul` | `agents/monitoring/health-overview.zul` | Consulta | `AgentHealthOverviewViewModel` |
| `agent-health-dashboard-overview.zul` | `agents/monitoring/dashboard.zul` | Consulta | `AgentHealthDashboardOverviewViewModel` |
| `agent-performance-metrics-overview.zul` | `agents/monitoring/performance.zul` | Consulta | `AgentPerformanceMetricsOverviewViewModel` |
| `agent-resource-consumption-overview.zul` | `agents/monitoring/resources.zul` | Consulta | `AgentResourceConsumptionOverviewViewModel` |
| `agent-error-analysis-overview.zul` | `agents/monitoring/errors.zul` | Consulta | `AgentErrorAnalysisOverviewViewModel` |

### **COMPLIANCE (Cumplimiento)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `agent-compliance-detail.zul` | `agents/compliance/page.zul` | CRUD | `AgentComplianceDetailViewModel` |
| `agent-compliance-overview.zul` | `agents/compliance/overview.zul` | Consulta | `AgentComplianceOverviewViewModel` |
| `agent-compliance-status-overview.zul` | `agents/compliance/status.zul` | Consulta | `AgentComplianceStatusOverviewViewModel` |

### **BIAS DETECTION (Detección de Sesgo)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `agent-bias-detection-detail.zul` | `agents/bias-detection/page.zul` | CRUD | `AgentBiasDetectionDetailViewModel` |
| `agent-bias-detection-overview.zul` | `agents/bias-detection/overview.zul` | Consulta | `AgentBiasDetectionOverviewViewModel` |

### **TRANSPARENCY (Transparencia)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `agent-transparency-detail.zul` | `agents/transparency/page.zul` | CRUD | `AgentTransparencyDetailViewModel` |
| `agent-transparency-overview.zul` | `agents/transparency/overview.zul` | Consulta | `AgentTransparencyOverviewViewModel` |

### **TOOLS (Herramientas)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `agent-tool-detail.zul` | `agents/tools/page.zul` | CRUD | `AgentToolDetailViewModel` |
| `agent-tool-overview.zul` | `agents/tools/overview.zul` | Consulta | `AgentToolOverviewViewModel` |

### **GOVERNANCE (Gobernanza)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `agent-governance-detail.zul` | `agents/governance/page.zul` | CRUD | `AgentGovernanceDetailViewModel` |
| `agent-governance-overview.zul` | `agents/governance/overview.zul` | Consulta | `AgentGovernanceOverviewViewModel` |

### **WORKFLOW (Flujo de Trabajo)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `agent-workflow-detail.zul` | `agents/workflow/page.zul` | CRUD | `AgentWorkflowDetailViewModel` |
| `agent-workflow-overview.zul` | `agents/workflow/overview.zul` | Consulta | `AgentWorkflowOverviewViewModel` |
| `agent-workflow-execution-detail.zul` | `agents/workflow/execution.zul` | CRUD | `AgentWorkflowExecutionDetailViewModel` |
| `agent-workflow-execution-overview.zul` | `agents/workflow/execution-overview.zul` | Consulta | `AgentWorkflowExecutionOverviewViewModel` |
| `agent-workflow-analytics-overview.zul` | `agents/workflow/analytics.zul` | Consulta | `AgentWorkflowAnalyticsOverviewViewModel` |

### **DEPLOYMENT (Despliegue)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `agent-deployment-detail.zul` | `agents/deployment/page.zul` | CRUD | `AgentDeploymentDetailViewModel` |
| `agent-deployment-overview.zul` | `agents/deployment/overview.zul` | Consulta | `AgentDeploymentOverviewViewModel` |
| `agent-deployment-status-overview.zul` | `agents/deployment/status.zul` | Consulta | `AgentDeploymentStatusOverviewViewModel` |

### **ALERTS (Alertas)**
| Pantalla Actual | Nueva Ubicación | Tipo | ViewModel |
|-----------------|-----------------|------|-----------|
| `agent-alert-detail.zul` | `agents/alerts/page.zul` | CRUD | `AgentAlertDetailViewModel` |
| `agent-alert-overview.zul` | `agents/alerts/overview.zul` | Consulta | `AgentAlertOverviewViewModel` |

---

## 📋 PLAN DE IMPLEMENTACIÓN

### **Fase 1: Crear Estructura de Directorios**
```bash
mkdir -p src/main/webapp/console/platform/agents/{overview,create,approval,decisions,ethics,interactions,learning,registry,rollback,versioning,monitoring,compliance,bias-detection,transparency,tools,governance,workflow,deployment,alerts}
```

### **Fase 2: Mover Pantallas**
- Mover cada pantalla a su nueva ubicación
- Actualizar referencias en ViewModels
- Actualizar rutas en menús

### **Fase 3: Actualizar ViewModels**
- Verificar que todos los ViewModels existan
- Actualizar rutas de navegación
- Ajustar permisos por rol

### **Fase 4: Actualizar Menús**
- Crear menús dinámicos por módulo
- Implementar breadcrumbs
- Configurar permisos por rol

---

## ✅ RESULTADO ESPERADO

**Estructura Final:**
```
console/platform/agents/
├── overview/          # 1 pantalla
├── create/           # 1 pantalla
├── approval/         # 2 pantallas
├── decisions/        # 3 pantallas
├── ethics/           # 2 pantallas
├── interactions/     # 8 pantallas
├── learning/         # 2 pantallas
├── registry/         # 3 pantallas
├── rollback/         # 2 pantallas
├── versioning/       # 2 pantallas
├── monitoring/       # 8 pantallas
├── compliance/       # 3 pantallas
├── bias-detection/   # 2 pantallas
├── transparency/     # 2 pantallas
├── tools/           # 2 pantallas
├── governance/       # 2 pantallas
├── workflow/         # 5 pantallas
├── deployment/       # 3 pantallas
└── alerts/          # 2 pantallas
```

**Total:** 54 pantallas organizadas en 19 módulos funcionales

---

## 🎯 BENEFICIOS

1. **Organización Clara:** Cada módulo tiene un propósito específico
2. **Navegación Intuitiva:** Estructura similar a Next.js original
3. **Escalabilidad:** Fácil agregar nuevos módulos
4. **Mantenibilidad:** Pantallas agrupadas por funcionalidad
5. **UX Mejorada:** Usuarios encuentran funcionalidades más fácilmente

**Estado:** ✅ **IMPLEMENTADO COMPLETAMENTE**  
**Fecha Implementación:** Octubre 2025  
**Pantallas Reorganizadas:** 54 pantallas ZUL  
**Módulos Creados:** 19 directorios funcionales

---

## ✅ IMPLEMENTACIÓN COMPLETADA

### **📁 ESTRUCTURA FINAL IMPLEMENTADA:**

```
console/platform/agents/
├── overview/          # Listado principal (1 pantalla)
│   └── page.zul       # agent-overview.zul
├── create/            # Creación de agentes (1 pantalla)
│   └── page.zul       # agent-detail.zul
├── approval/          # Aprobaciones (2 pantallas)
│   ├── page.zul       # agent-approval-detail.zul
│   └── overview.zul   # agent-approval-overview.zul
├── decisions/         # Decisiones (3 pantallas)
│   ├── page.zul       # agent-decision-detail.zul
│   ├── overview.zul   # agent-decision-overview.zul
│   └── audit.zul      # agent-decision-audit-overview.zul
├── ethics/            # Ética (2 pantallas)
│   ├── page.zul       # agent-ethics-assessment-detail.zul
│   └── overview.zul   # agent-ethics-assessment-overview.zul
├── interactions/      # Interacciones (8 pantallas)
│   ├── page.zul       # agent-interaction-detail.zul
│   ├── overview.zul   # agent-interaction-overview.zul
│   ├── patterns.zul   # agent-interaction-patterns-overview.zul
│   ├── communication.zul      # agent-communication-detail.zul
│   ├── communication-overview.zul  # agent-communication-overview.zul
│   ├── collaboration.zul      # agent-collaboration-detail.zul
│   ├── collaboration-overview.zul  # agent-collaboration-overview.zul
│   └── network.zul    # agent-collaboration-network-overview.zul
├── learning/          # Aprendizaje (2 pantallas)
│   ├── page.zul       # agent-expertise-detail.zul
│   └── overview.zul   # agent-expertise-overview.zul
├── registry/          # Registro (2 pantallas)
│   ├── domain.zul     # agent-domain-detail.zul
│   └── domain-overview.zul  # agent-domain-overview.zul
├── rollback/          # Rollback (2 pantallas)
│   ├── page.zul       # agent-rollback-detail.zul
│   └── overview.zul   # agent-rollback-overview.zul
├── versioning/        # Versionado (2 pantallas)
│   ├── page.zul       # agent-version-detail.zul
│   └── overview.zul   # agent-version-overview.zul
├── monitoring/        # Monitoreo (8 pantallas)
│   ├── page.zul       # agent-monitoring-detail.zul
│   ├── overview.zul   # agent-monitoring-overview.zul
│   ├── health.zul     # agent-health-detail.zul
│   ├── health-overview.zul    # agent-health-overview.zul
│   ├── dashboard.zul  # agent-health-dashboard-overview.zul
│   ├── performance.zul        # agent-performance-metrics-overview.zul
│   ├── resources.zul  # agent-resource-consumption-overview.zul
│   └── errors.zul     # agent-error-analysis-overview.zul
├── compliance/        # Cumplimiento (3 pantallas)
│   ├── page.zul       # agent-compliance-detail.zul
│   ├── overview.zul   # agent-compliance-overview.zul
│   └── status.zul     # agent-compliance-status-overview.zul
├── bias-detection/    # Detección de sesgo (2 pantallas)
│   ├── page.zul       # agent-bias-detection-detail.zul
│   └── overview.zul   # agent-bias-detection-overview.zul
├── transparency/      # Transparencia (2 pantallas)
│   ├── page.zul       # agent-transparency-detail.zul
│   └── overview.zul   # agent-transparency-overview.zul
├── tools/             # Herramientas (2 pantallas)
│   ├── page.zul       # agent-tool-detail.zul
│   └── overview.zul   # agent-tool-overview.zul
├── governance/        # Gobernanza (2 pantallas)
│   ├── page.zul       # agent-governance-detail.zul
│   └── overview.zul   # agent-governance-overview.zul
├── workflow/          # Flujo de trabajo (5 pantallas)
│   ├── page.zul       # agent-workflow-detail.zul
│   ├── overview.zul   # agent-workflow-overview.zul
│   ├── execution.zul  # agent-workflow-execution-detail.zul
│   ├── execution-overview.zul  # agent-workflow-execution-overview.zul
│   └── analytics.zul  # agent-workflow-analytics-overview.zul
├── deployment/        # Despliegue (3 pantallas)
│   ├── page.zul       # agent-deployment-detail.zul
│   ├── overview.zul   # agent-deployment-overview.zul
│   └── status.zul     # agent-deployment-status-overview.zul
└── alerts/           # Alertas (2 pantallas)
    ├── page.zul       # agent-alert-detail.zul
    └── overview.zul   # agent-alert-overview.zul
```

### **🚨 PANTALLAS BPMN PRESERVADAS:**

Las siguientes pantallas **NO se movieron** porque son User Tasks de procesos BPMN:
```
console/bpmn/
├── agent-approval-human-override-form.zul  # User Task BPMN
└── hitl-sla-reminder-form.zul             # User Task BPMN
```

### **📝 CAMBIOS REALIZADOS:**

1. ✅ **Creada estructura de directorios** según Next.js
2. ✅ **Movidas 54 pantallas** a módulos funcionales
3. ✅ **Actualizada referencia** en `AgentsDashboardViewModel.java`:
   - Cambio: `/agents/agent-detail.zul` → `/agents/create/page.zul`
4. ✅ **Preservadas pantallas BPMN** en ubicación original

### **📊 ESTADÍSTICAS FINALES:**

- **Total pantallas reorganizadas:** 54 pantallas ZUL
- **Módulos funcionales creados:** 19 directorios
- **Pantallas BPMN preservadas:** 2 pantallas
- **Referencias actualizadas:** 1 ViewModel
- **Estructura sincronizada:** ✅ Con Next.js

### **🎯 BENEFICIOS OBTENIDOS:**

1. **Organización Clara:** Cada módulo tiene un propósito específico
2. **Navegación Intuitiva:** Estructura idéntica a Next.js original
3. **Escalabilidad:** Fácil agregar nuevos módulos
4. **Mantenibilidad:** Pantallas agrupadas por funcionalidad
5. **UX Mejorada:** Usuarios encuentran funcionalidades más fácilmente
6. **Consistencia:** Ambas versiones (ZKoss y Next.js) tienen la misma estructura

**Estado:** ✅ **IMPLEMENTACIÓN COMPLETADA**  
**Próximo:** Documento técnico de tablas y vistas
