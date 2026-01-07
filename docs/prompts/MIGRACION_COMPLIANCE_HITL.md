# Prompt de Migración - Compliance - HITL Supervision (Art. 14)

## Contexto del Módulo

**Módulo:** Compliance
**Tipo de Pantalla:** Dashboard de Supervisión Humana
**Total de Pantallas:** 1
**Artículo EU AI Act:** Art. 14 - Human Oversight

---

## Arquitectura del Módulo

### ViewModel Identificado

#### HitlSupervisionViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.governance`
- **Archivo:** `com/codeflowx/govern/viewmodel/governance/HitlSupervisionViewModel.java`
- **Servicios Usados:**
  - `BusinessService` - Servicios generales
- **Funcionalidades:**
  - Consolidar todas las intervenciones humanas (agentes, modelos, prompts)
  - Mostrar registro de supervisión, auditoría, intervenciones
  - Métricas HITL (tiempo promedio, tasa de aprobación, SLA cumplimiento)
  - Integración con ViewModels BPMN:
    - `AgentApprovalHumanOverrideViewModel`
    - `PromptHumanReviewViewModel`
    - `HitlSlaReminderViewModel`

### Entidades JPA Principales

- **Agent** - Agentes de IA
- **Model** - Modelos de IA
- **Prompt** - Prompts

---

## Pantalla a Migrar

- **Archivo ZUL:** `console/gobierno/governance/hitl-supervision.zul`
- **ViewModel Asociado:** `HitlSupervisionViewModel.java`
- **Ruta Next.js:** `/governance/compliance/hitl-supervision`

---

## Estrategia de Migración

### Dashboard HITL:
- Métricas consolidadas de supervisión humana
- Lista de intervenciones pendientes
- Registro de decisiones humanas
- SLA cumplimiento
- Filtros por tipo (agente, modelo, prompt)

### Mock Data:
```typescript
export const mockHitlData = {
  metrics: {
    averageResponseTime: "2.5 hours",
    approvalRate: 0.85,
    slaCompliance: 0.92,
    pendingInterventions: 12
  },
  interventions: [
    {
      id: 1,
      type: "AGENT_APPROVAL",
      entityId: 123,
      entityName: "Credit Scoring Agent",
      status: "PENDING",
      createdAt: "2025-11-20T10:30:00Z",
      slaDeadline: "2025-11-20T14:30:00Z"
    }
  ]
};
```

---

**Última actualización:** Noviembre 2025
**Estado:** Listo para implementación
