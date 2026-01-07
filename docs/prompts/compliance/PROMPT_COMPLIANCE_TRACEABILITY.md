# 🔗 PROMPT DE IMPLEMENTACIÓN - TRAZABILIDAD (Art. 12, 19)

**Módulo:** Compliance - Traceability
**Artículo EU AI Act:** Art. 12 - Record-Keeping + Art. 19 - Logs Inmutables
**Fecha:** Diciembre 2025
**Estado:** ⏳ Pendiente de implementación completa
**Esfuerzo Estimado:** 3-4 días

---

## 📋 RESUMEN DEL MÓDULO

### **Objetivo**
Implementar el dashboard completo de trazabilidad según el Art. 12 y Art. 19 del EU AI Act. El sistema debe consolidar logs, prompts, outputs y decisiones para mostrar trazabilidad completa modelo-dataset-output.

### **Pantallas Requeridas**

| # | Ruta Next.js | Estado | Descripción | Prioridad |
|---|--------------|-------|-------------|-----------|
| 1 | `app/(app)/governance/compliance/traceability/page.tsx` | ✅ Existe | Trazabilidad y evidencias | 🔴 Alta |
| 2 | `app/(app)/governance/compliance/traceability/[entityType]/[id]/page.tsx` | ❌ No existe | Trazabilidad de entidad específica | 🟡 Media |

---

## 🏗️ ARQUITECTURA Y DEPENDENCIAS

### **Pantallas ZUL Originales**
- **ZUL Principal:** `console/gobierno/compliance/traceability-evidence.zul`
  - ViewModel: `TraceabilityEvidenceViewModel`

### **ViewModels Java**
- **TraceabilityEvidenceViewModel** ✅
  - **Paquete:** `com.codeflowx.govern.viewmodel.compliance`
  - **Archivo:** `com/codeflowx/govern/viewmodel/compliance/TraceabilityEvidenceViewModel.java`
  - **Servicios Usados:**
    - `@WireVariable ImmutableLoggingBusinessService immutableLoggingBusinessService` - Logs inmutables
    - `@WireVariable BusinessService businessService` - Servicios generales
  - **Funcionalidades:**
    - Consolidar logs, prompts, outputs, decisiones
    - Mostrar trazabilidad completa modelo-dataset-output
    - Integración con telemetría (opcional)

### **Entidades JPA**
- **ImmutableLog** - `com.codeflowx.govern.entity.logging.ImmutableLog`
  - **Tabla:** `GOVIMMUTABLELOGS` (prefijo `GOV`)
  - **Usado para:** Registro automático de eventos críticos (Art. 12, 19)
  - Ver detalles en `PROMPT_COMPLIANCE_IMMUTABLE_LOGS.md`

- **Model** - `com.codeflowx.govern.entity.models.Model`
  - **Tabla:** `MODMODELS` (prefijo `MOD`)
  - **Campos relacionados:**
    - `MODTRAININGDATASETID` (Long) - ID del dataset de entrenamiento
    - `MODVERSION` (String) - Versión del modelo

- **Project** - `com.codeflowx.govern.entity.projects.Project`
  - **Tabla:** `PRJPROJECTS` (prefijo `PRJ`)
  - **Usado para:** Trazabilidad a nivel de proyecto

### **Business Services Disponibles**
- **ImmutableLoggingBusinessService** ✅
  - **Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/ImmutableLoggingBusinessService.java`
  - **Métodos principales:**
    - `getEntityLogsWithVerification(String entityType, Long entityId)` - Obtiene logs con verificación
      - Consulta: `SELECT * FROM govimmutablelogs WHERE imlentitytype = ? AND imlentityid = ? ORDER BY imltimestamp`
      - Verifica integridad de hash chain
      - Retorna: `List<ImmutableLogWithVerification>`
    - `verifyIntegrity(Long startId, Long endId)` - Verifica integridad
      - Ver detalles en `PROMPT_COMPLIANCE_IMMUTABLE_LOGS.md`

- **TraceabilityService** ✅
  - **Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/TraceabilityService.java`
  - **Métodos principales:**
    - `getModelTraceability(Long modelId)` - Obtiene trazabilidad de modelo
      - Obtiene: logs, dataset, versiones, decisiones
      - Retorna: `ModelTraceability`
    - `getProjectTraceability(Long projectId)` - Obtiene trazabilidad de proyecto
      - Obtiene: modelos, logs, decisiones, outputs
      - Retorna: `ProjectTraceability`
    - `getAgentTraceability(Long agentId)` - Obtiene trazabilidad de agente
      - Obtiene: ejecuciones, prompts, outputs, decisiones
      - Retorna: `AgentTraceability`
    - `exportTraceabilityEvidence(Long entityId, String entityType)` - Exporta evidencias
      - Genera JSON/PDF con toda la trazabilidad
      - Incluye verificación de integridad

### **Servicios CRUD**
- **ImmutableLogService** (CRUD)
- **ModelService** (CRUD)
- **ProjectService** (CRUD)

### **Lógica de Negocio Detallada**

#### **Trazabilidad Modelo-Dataset-Output:**
```java
// Implementado en TraceabilityService.getModelTraceability()
public ModelTraceability getModelTraceability(Long modelId) {
    Model model = modelService.findById(modelId);

    // Obtener dataset de entrenamiento
    Dataset trainingDataset = datasetService.findById(model.getModtrainingdatasetid());

    // Obtener logs inmutables del modelo
    List<ImmutableLog> logs = immutableLoggingBusinessService.getLogChain("Model", modelId);

    // Obtener versiones del modelo
    List<ModelVersion> versions = modelService.getVersions(modelId);

    // Obtener decisiones HITL relacionadas
    List<HitlDecision> decisions = hitlDecisionService.findByEntity("Model", modelId);

    // Obtener outputs generados
    List<ModelOutput> outputs = modelOutputService.findByModel(modelId);

    return new ModelTraceability(
        model,
        trainingDataset,
        logs,
        versions,
        decisions,
        outputs
    );
}
```

#### **Exportación de Evidencias:**
```java
// Implementado en TraceabilityService.exportTraceabilityEvidence()
public TraceabilityEvidence exportTraceabilityEvidence(Long entityId, String entityType) {
    TraceabilityData data = null;

    switch (entityType) {
        case "Model":
            data = getModelTraceability(entityId);
            break;
        case "Project":
            data = getProjectTraceability(entityId);
            break;
        case "Agent":
            data = getAgentTraceability(entityId);
            break;
    }

    // Verificar integridad de logs
    IntegrityVerificationResult integrity = immutableLoggingBusinessService
        .verifyIntegrity(data.getLogs());

    // Generar evidencias
    TraceabilityEvidence evidence = new TraceabilityEvidence();
    evidence.setEntityType(entityType);
    evidence.setEntityId(entityId);
    evidence.setData(data);
    evidence.setIntegrityVerification(integrity);
    evidence.setExportDate(new Timestamp(System.currentTimeMillis()));

    return evidence;
}
```

---

## 📱 PANTALLA 1: Dashboard de Trazabilidad

### **Ruta:** `app/(app)/governance/compliance/traceability/page.tsx`

**Estado Actual:** ✅ Existe pero incompleta

### **Funcionalidades Requeridas:**

1. **Vista Consolidada:**
   - Selección de entidad (Model, Project, Agent)
   - Visualización de trazabilidad completa
   - Filtros: por tipo de entidad, fecha, usuario

2. **Trazabilidad Modelo-Dataset-Output:**
   - Visualización de cadena: Modelo → Dataset → Output
   - Logs asociados a cada elemento
   - Decisiones HITL relacionadas
   - Verificación de integridad

3. **Evidencias:**
   - Lista de evidencias de cumplimiento
   - Verificación de integridad de hash chain
   - Exportación de evidencias (JSON, PDF)

4. **Gráficos:**
   - Timeline de eventos
   - Relaciones entre entidades
   - Métricas de trazabilidad

### **Mock Data:**

```typescript
// app/(app)/governance/data/mockTraceability.ts
export const mockTraceability = {
  model: {
    id: 123,
    name: "Credit Scoring Model v1.0",
    version: "1.0"
  },
  dataset: {
    id: 456,
    name: "Training Dataset v1.0",
    version: "1.0"
  },
  logs: [
    {
      id: 1,
      type: "MODEL_TRAINING",
      timestamp: "2025-11-01T10:00:00Z",
      description: "Model training started",
      hash: "abc123...",
      previousHash: "0"
    },
    {
      id: 2,
      type: "MODEL_DEPLOYMENT",
      timestamp: "2025-12-01T10:30:00Z",
      description: "Model deployed to production",
      hash: "def456...",
      previousHash: "abc123..."
    }
  ],
  decisions: [
    {
      id: 1,
      type: "MODEL_APPROVAL",
      decision: "APPROVED",
      timestamp: "2025-11-30T15:00:00Z",
      userId: "user1"
    }
  ],
  outputs: [
    {
      id: 1,
      timestamp: "2025-12-01T11:00:00Z",
      input: "...",
      output: "...",
      confidence: 0.95
    }
  ],
  integrityVerification: {
    score: 1.00,
    status: "INTEGRITY_OK",
    verifiedLogs: 100,
    totalLogs: 100
  }
};
```

---

## 📱 PANTALLA 2: Trazabilidad de Entidad Específica

### **Ruta:** `app/(app)/governance/compliance/traceability/[entityType]/[id]/page.tsx`

**Estado Actual:** ❌ No existe - **CREAR**

### **Funcionalidades Requeridas:**

1. **Información de la Entidad:**
   - Datos completos de la entidad
   - Tipo: Model, Project, Agent

2. **Trazabilidad Completa:**
   - Logs inmutables asociados
   - Relaciones con otras entidades
   - Decisiones HITL
   - Outputs generados

3. **Verificación de Integridad:**
   - Verificación de hash chain
   - Score de integridad
   - Alertas si hay problemas

4. **Exportación:**
   - Exportar evidencias de trazabilidad
   - Generar PDF de trazabilidad
   - Descargar JSON completo

### **Mock Data:**

```typescript
export const mockEntityTraceability = {
  entityType: "Model",
  entityId: 123,
  entityName: "Credit Scoring Model v1.0",
  traceability: mockTraceability,
  relatedEntities: [
    {
      type: "Dataset",
      id: 456,
      name: "Training Dataset v1.0",
      relationship: "TRAINED_WITH"
    },
    {
      type: "Project",
      id: 1,
      name: "AI Credit Scoring System",
      relationship: "BELONGS_TO"
    }
  ]
};
```

---

## 🎨 ESTILOS "WOW FACTOR"

### **Estructura Base:**

```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Network, Link } from "lucide-react";

export default function TraceabilityPage() {
  const { t } = useTranslation();

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-pink-400/30 rounded-full animate-pulse" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        {/* Header */}
        <div className="text-center space-y-2">
          <div className="flex items-center justify-center gap-3 mb-4">
            <Network className="w-8 h-8 text-pink-500" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-pink-500 to-pink-700 bg-clip-text text-transparent">
              {t("compliance.traceability.title", "Traceability & Evidence")}
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
traceability: {
  title: {
    es: "Trazabilidad y Evidencias",
    en: "Traceability & Evidence"
  },
  entityTypes: {
    Model: {
      es: "Modelo",
      en: "Model"
    },
    Project: {
      es: "Proyecto",
      en: "Project"
    },
    Agent: {
      es: "Agente",
      en: "Agent"
    }
  },
  relationships: {
    TRAINED_WITH: {
      es: "Entrenado con",
      en: "Trained with"
    },
    BELONGS_TO: {
      es: "Pertenece a",
      en: "Belongs to"
    }
  }
}
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### **Pantalla 1: Dashboard (Ya existe - Completar)**
- [ ] Revisar pantalla existente
- [ ] Implementar vista consolidada
- [ ] Añadir trazabilidad modelo-dataset-output
- [ ] Implementar visualización de evidencias
- [ ] Añadir gráficos de timeline
- [ ] Añadir mock data
- [ ] Crear API routes mock

### **Pantalla 2: Detalle Entidad (Crear nueva)**
- [ ] Crear página `[entityType]/[id]/page.tsx`
- [ ] Implementar trazabilidad completa
- [ ] Añadir verificación de integridad
- [ ] Implementar exportación
- [ ] Añadir mock data
- [ ] Crear API routes mock
- [ ] Añadir entrada al menú

### **General**
- [ ] Añadir traducciones completas
- [ ] Verificar estilos "Wow Factor"
- [ ] Probar funcionalidad completa
- [ ] Documentar integración con backend

---

## 🔗 REFERENCIAS

- **Documentación Compliance:** `docs/prompts/BUSINESS_LOGIC_COMPLIANCE.md`
- **Prompt Migración:** `docs/prompts/MIGRACION_COMPLIANCE_TRACEABILITY.md`
- **Prompt Logs Inmutables:** `docs/prompts/compliance/PROMPT_COMPLIANCE_IMMUTABLE_LOGS.md`
- **Inventario Pantallas:** `docs/PANTALLAS_GOVERNANCE_COMPLIANCE_AI_ACT.md`
- **Artículo EU AI Act:** Art. 12, 19

---

**Última actualización:** Diciembre 2025
**Estado:** Listo para implementación en paralelo
