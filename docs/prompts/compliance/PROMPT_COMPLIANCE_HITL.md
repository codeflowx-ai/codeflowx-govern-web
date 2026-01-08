# 👤 PROMPT DE IMPLEMENTACIÓN - SUPERVISIÓN HUMANA HITL (Art. 14)

**Módulo:** Compliance - HITL Supervision
**Artículo EU AI Act:** Art. 14 - Human Oversight
**Fecha:** Diciembre 2025
**Estado:** ⏳ Pendiente de implementación completa
**Esfuerzo Estimado:** 3-4 días

---

## 📋 RESUMEN DEL MÓDULO

### **Objetivo**
Implementar el dashboard completo de supervisión humana (HITL - Human In The Loop) según el Art. 14 del EU AI Act. El sistema debe consolidar todas las intervenciones humanas (agentes, modelos, prompts) y mostrar métricas de supervisión.

### **Pantallas Requeridas**

| # | Ruta Next.js | Estado | Descripción | Prioridad |
|---|--------------|-------|-------------|-----------|
| 1 | `app/(app)/governance/compliance/hitl-supervision/page.tsx` | ✅ Existe | Supervisión humana | 🔴 Alta |
| 2 | `app/(app)/bpmn/forms/hitl-sla-reminder/page.tsx` | ✅ Existe | Recordatorio SLA HITL | 🔴 Alta |

---

## 🏗️ ARQUITECTURA Y DEPENDENCIAS

### **Pantallas ZUL Originales**
- **ZUL Principal:** `console/gobierno/governance/hitl-supervision.zul`
  - ViewModel: `HitlSupervisionViewModel`

### **ViewModels Java**
- **HitlSupervisionViewModel** ✅
  - **Paquete:** `com.codeflowx.govern.viewmodel.governance`
  - **Archivo:** `com/codeflowx/govern/viewmodel/governance/HitlSupervisionViewModel.java`
  - **Servicios Usados:**
    - `@WireVariable BusinessService businessService` - Servicios generales
  - **Funcionalidades:**
    - Consolidar todas las intervenciones humanas (agentes, modelos, prompts)
    - Mostrar registro de supervisión, auditoría, intervenciones
    - Métricas HITL (tiempo promedio, tasa de aprobación, SLA cumplimiento)
    - Integración con ViewModels BPMN:
      - `AgentApprovalHumanOverrideViewModel`
      - `PromptHumanReviewViewModel`
      - `HitlSlaReminderViewModel`

- **HitlSlaReminderViewModel** ✅
  - **Paquete:** `com.codeflowx.govern.viewmodel.bpmn`
  - **Archivo:** `com/codeflowx/govern/viewmodel/bpmn/HitlSlaReminderViewModel.java`
  - **Tipo:** User Task BPMN (hitl-sla-reminder)

### **Entidades JPA**
- **HitlSupervision** - `com.codeflowx.govern.entity.compliance.HitlSupervision`
  - **Tabla:** `GOVHITLSUPERVISIONS` (prefijo `GOV`)
  - **Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/HitlSupervision.java`
  - **Campos principales:**
    - `IDXHITLSUPERVISION` (Long, PK) - ID autonumérico
    - `IDXPROJECT` (Long, FK) - Referencia a proyecto
    - `HITLSUPERVISIONTYPE` (String) - Tipo: PRE_DEPLOYMENT, IN_LOOP, POST_DEPLOYMENT, OVERRIDE
    - `HITLCONFIGURATION` (JSONB) - Configuración de supervisión
    - `HITLSLA` (Integer) - SLA en horas
    - `HITLCREATEDAT` (Timestamp) - Fecha creación
    - `IDUUID` (String) - UUID único

- **HitlDecision** - `com.codeflowx.govern.entity.compliance.HitlDecision`
  - **Tabla:** `GOVHITLDECISIONS` (prefijo `GOV`)
  - **Campos principales:**
    - `IDXHITLDECISION` (Long, PK) - ID autonumérico
    - `IDXHITLSUPERVISION` (Long, FK) - Referencia a supervisión
    - `IDXENTITY` (Long) - ID de entidad (Agent, Model, Prompt)
    - `HITLENTITYTYPE` (String) - Tipo: AGENT, MODEL, PROMPT
    - `HITLDECISION` (String) - Decisión: APPROVED, REJECTED, MODIFIED
    - `HITLDECISIONREASON` (String) - Razón de la decisión
    - `HITLRESPONSETIME` (Integer) - Tiempo de respuesta en minutos
    - `HITLDECISIONDATE` (Timestamp) - Fecha de decisión
    - `HITLUSERID` (String) - Usuario que tomó la decisión
    - `IDUUID` (String) - UUID único

### **Business Services Disponibles**
- **HitlSupervisionService** ✅
  - **Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/HitlSupervisionService.java`
  - **Métodos principales:**
    - `configureSupervision(Long projectId, HitlConfiguration config)` - Configura supervisión
      - INSERT: `INSERT INTO govhitlsupervisions (...)`
    - `getPendingInterventions(Long projectId)` - Obtiene intervenciones pendientes
      - Consulta: `SELECT * FROM govhitldecisions WHERE hitldecision IS NULL AND ...`
    - `recordDecision(Long supervisionId, HitlDecisionData data)` - Registra decisión humana
      - INSERT: `INSERT INTO govhitldecisions (...)`
      - Calcula tiempo de respuesta
    - `calculateMetrics(Long projectId)` - Calcula métricas HITL
      - Tiempo promedio de respuesta
      - Tasa de aprobación
      - SLA cumplimiento
      - Retorna: `HitlMetrics`

### **Servicios CRUD**
- **HitlSupervisionService** (CRUD)
- **HitlDecisionService** (CRUD)

### **Lógica de Negocio Detallada**

#### **Configuración de Supervisión:**
```java
// Implementado en HitlSupervisionService.configureSupervision()
public HitlSupervision configureSupervision(Long projectId, HitlConfiguration config) {
    HitlSupervision supervision = new HitlSupervision();
    supervision.setIdxproject(projectId);
    supervision.setHitlsupervisiontype(config.getType());
    supervision.setHitlconfiguration(config.toJson());
    supervision.setHitlsla(config.getSlaHours());
    supervision.setHitlcreatedat(new Timestamp(System.currentTimeMillis()));

    return save(supervision);
}
```

#### **Cálculo de Métricas:**
```java
// Implementado en HitlSupervisionService.calculateMetrics()
public HitlMetrics calculateMetrics(Long projectId) {
    List<HitlDecision> decisions = getDecisions(projectId);

    // Tiempo promedio de respuesta
    double avgResponseTime = decisions.stream()
        .mapToInt(HitlDecision::getHitlresponsetime)
        .average()
        .orElse(0.0);

    // Tasa de aprobación
    long approved = decisions.stream()
        .filter(d -> "APPROVED".equals(d.getHitldecision()))
        .count();
    double approvalRate = (double) approved / decisions.size();

    // SLA cumplimiento
    HitlSupervision supervision = getSupervision(projectId);
    int slaHours = supervision.getHitlsla();
    long slaCompliant = decisions.stream()
        .filter(d -> d.getHitlresponsetime() <= (slaHours * 60))
        .count();
    double slaCompliance = (double) slaCompliant / decisions.size();

    return new HitlMetrics(avgResponseTime, approvalRate, slaCompliance);
}
```

---

## 📱 PANTALLA 1: Dashboard HITL

### **Ruta:** `app/(app)/governance/compliance/hitl-supervision/page.tsx`

**Estado Actual:** ✅ Existe pero incompleta

### **Funcionalidades Requeridas:**

1. **Métricas Principales:**
   - Tiempo promedio de respuesta
   - Tasa de aprobación
   - SLA cumplimiento
   - Intervenciones pendientes

2. **Intervenciones Pendientes:**
   - Lista de intervenciones que requieren decisión humana
   - Filtros: por tipo (AGENT, MODEL, PROMPT), SLA
   - Alertas de SLA próximo a vencer

3. **Registro de Decisiones:**
   - Historial de decisiones humanas
   - Filtros: por tipo, decisión, usuario, fecha
   - Visualización de tiempos de respuesta

4. **Configuración:**
   - Configurar puntos de supervisión
   - Configurar SLA por tipo
   - Tipos de supervisión:
     - Pre-deployment review
     - In-loop supervision
     - Post-deployment review
     - Override capabilities

### **Mock Data:**

```typescript
// app/(app)/governance/data/mockHitl.ts
export const mockHitlData = {
  metrics: {
    averageResponseTime: 2.5, // horas
    approvalRate: 0.85,
    slaCompliance: 0.92,
    pendingInterventions: 12
  },
  pendingInterventions: [
    {
      id: 1,
      type: "AGENT_APPROVAL",
      entityType: "Agent",
      entityId: 123,
      entityName: "Credit Scoring Agent",
      status: "PENDING",
      createdAt: "2025-12-01T10:00:00Z",
      slaDeadline: "2025-12-01T14:00:00Z",
      slaHours: 4,
      timeRemaining: 2.5 // horas
    },
    {
      id: 2,
      type: "PROMPT_REVIEW",
      entityType: "Prompt",
      entityId: 456,
      entityName: "Credit Scoring Prompt",
      status: "PENDING",
      createdAt: "2025-12-01T11:00:00Z",
      slaDeadline: "2025-12-01T15:00:00Z",
      slaHours: 4,
      timeRemaining: 3.5 // horas
    }
  ],
  recentDecisions: [
    {
      id: 1,
      type: "MODEL_DEPLOYMENT",
      entityType: "Model",
      entityId: 789,
      entityName: "Credit Scoring Model v1.0",
      decision: "APPROVED",
      decisionReason: "Model meets all quality criteria",
      responseTime: 1.5, // horas
      decisionDate: "2025-11-30T15:00:00Z",
      userId: "user1"
    }
  ]
};
```

---

## 📱 PANTALLA 2: Recordatorio SLA HITL (BPMN)

### **Ruta:** `app/(app)/bpmn/forms/hitl-sla-reminder/page.tsx`

**Estado Actual:** ✅ Existe pero incompleta

### **Funcionalidades Requeridas:**

1. **Información de la Intervención:**
   - Datos de la entidad (Agent, Model, Prompt)
   - Tipo de supervisión requerida
   - SLA y tiempo restante

2. **Recordatorio:**
   - Alerta de SLA próximo a vencer
   - Información de la intervención pendiente
   - Enlace a la intervención

3. **Acciones:**
   - Ir a intervención
   - Extender SLA (si está permitido)
   - Marcar como recordado

### **Mock Data:**

```typescript
export const mockHitlSlaReminder = {
  interventionId: 1,
  entityType: "Agent",
  entityId: 123,
  entityName: "Credit Scoring Agent",
  supervisionType: "AGENT_APPROVAL",
  slaHours: 4,
  createdAt: "2025-12-01T10:00:00Z",
  slaDeadline: "2025-12-01T14:00:00Z",
  timeRemaining: 1.5, // horas
  urgency: "HIGH" // LOW, MEDIUM, HIGH, CRITICAL
};
```

---

## 🎨 ESTILOS "WOW FACTOR"

### **Estructura Base:**

```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import DevelopmentBanner from "@/components/ui/development-banner";
import { UserCheck, Clock } from "lucide-react";

export default function HitlSupervisionPage() {
  const { t } = useTranslation();

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-cyan-400/30 rounded-full animate-pulse" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        {/* Header */}
        <div className="text-center space-y-2">
          <div className="flex items-center justify-center gap-3 mb-4">
            <UserCheck className="w-8 h-8 text-cyan-500" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-cyan-500 to-cyan-700 bg-clip-text text-transparent">
              {t("compliance.hitl.title", "Human Oversight (HITL)")}
            </h1>
          </div>
        </div>

        {/* Contenido */}
      </div>
    </div>
  );
}
```

---

## 📝 TRADUCCIONES REQUERIDAS

Añadir en `app/config/i18n/modules/governance/compliance.ts`:

```typescript
hitl: {
  title: {
    es: "Supervisión Humana (HITL)",
    en: "Human Oversight (HITL)"
  },
  supervisionTypes: {
    PRE_DEPLOYMENT: {
      es: "Revisión Pre-Despliegue",
      en: "Pre-Deployment Review"
    },
    IN_LOOP: {
      es: "Supervisión In-Loop",
      en: "In-Loop Supervision"
    },
    POST_DEPLOYMENT: {
      es: "Revisión Post-Despliegue",
      en: "Post-Deployment Review"
    },
    OVERRIDE: {
      es: "Capacidades de Override",
      en: "Override Capabilities"
    }
  },
  decisions: {
    APPROVED: { es: "Aprobado", en: "Approved" },
    REJECTED: { es: "Rechazado", en: "Rejected" },
    MODIFIED: { es: "Modificado", en: "Modified" }
  }
}
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### **Pantalla 1: Dashboard (Ya existe - Completar)**
- [ ] Revisar pantalla existente
- [ ] Añadir métricas principales
- [ ] Implementar intervenciones pendientes
- [ ] Añadir registro de decisiones
- [ ] Implementar configuración de supervisión
- [ ] Añadir mock data
- [ ] Crear API routes mock

### **Pantalla 2: SLA Reminder (Ya existe - Completar)**
- [ ] Revisar pantalla existente
- [ ] Implementar recordatorio de SLA
- [ ] Añadir acciones (ir a intervención, extender SLA)
- [ ] Añadir mock data
- [ ] Crear API routes mock

### **General**
- [ ] Añadir traducciones completas
- [ ] Verificar estilos "Wow Factor"
- [ ] Probar funcionalidad completa
- [ ] Documentar integración con backend

---

## 🔗 REFERENCIAS

- **Documentación Compliance:** `docs/prompts/BUSINESS_LOGIC_COMPLIANCE.md`
- **Prompt Migración:** `docs/prompts/MIGRACION_COMPLIANCE_HITL.md`
- **Inventario Pantallas:** `docs/PANTALLAS_GOVERNANCE_COMPLIANCE_AI_ACT.md`
- **Artículo EU AI Act:** Art. 14

---

**Última actualización:** Diciembre 2025
**Estado:** Listo para implementación en paralelo
