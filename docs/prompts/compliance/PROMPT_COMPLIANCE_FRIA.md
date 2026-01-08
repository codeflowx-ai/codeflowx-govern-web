# 📋 PROMPT DE IMPLEMENTACIÓN - FRIA (Art. 27 + Anexo IX)

**Módulo:** Compliance - FRIA (Fundamental Rights Impact Assessment)
**Artículo EU AI Act:** Art. 27 - Evaluación de Impacto en Derechos Fundamentales + Anexo IX
**Fecha:** Diciembre 2025
**Estado:** ⏳ Pendiente de implementación completa
**Esfuerzo Estimado:** 4-5 días

---

## 📋 RESUMEN DEL MÓDULO

### **Objetivo**
Implementar el wizard completo de evaluación FRIA según el Art. 27 del EU AI Act. El wizard consta de 6 pasos correspondientes a los elementos mandatorios del Art. 27.1.a-f, y calcula el riesgo final según el Anexo IX.

### **Pantallas Requeridas**

| # | Ruta Next.js | Estado | Descripción | Prioridad |
|---|--------------|-------|-------------|-----------|
| 1 | `app/(app)/governance/compliance/fria/page.tsx` | ✅ Existe | Wizard FRIA (6 pasos) | 🔴 Alta |
| 2 | `app/(app)/governance/compliance/fria/assessments/page.tsx` | ❌ No existe | Listado de FRIAs | 🟡 Media |
| 3 | `app/(app)/governance/compliance/fria/[id]/page.tsx` | ❌ No existe | Detalle de FRIA | 🟡 Media |

---

## 🏗️ ARQUITECTURA Y DEPENDENCIAS

### **Pantallas ZUL Originales**
- **ZUL Principal:** `console/gobierno/compliance/fria-wizard.zul`
  - ViewModel: `FriaWizardViewModel`
  - Ubicación: `src/main/webapp/console/gobierno/compliance/fria-wizard.zul`

### **ViewModels Java**
- **FriaWizardViewModel** ✅
  - **Paquete:** `com.codeflowx.govern.viewmodel.compliance`
  - **Archivo:** `com/codeflowx/govern/viewmodel/compliance/FriaWizardViewModel.java`
  - **Ubicación:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/compliance/FriaWizardViewModel.java`
  - **Líneas:** ~700 líneas (según documentación)
  - **Servicios Usados:**
    - `@WireVariable ProjectService projectService` - Gestión de proyectos
    - `@WireVariable FriaAssessmentBusinessService friaBusinessService` - Lógica de negocio FRIA
  - **Métodos Principales:**
    - `nextStep()` - Avanza al siguiente paso
    - `previousStep()` - Retrocede al paso anterior
    - `saveStepData()` - Guarda datos del paso actual
    - `calculateCompletenessScore()` - Calcula score de completitud (0.00 - 1.00)
    - `generateFria()` - Completa FRIA y calcula riesgo final
    - `notifyAuthority()` - Notifica a autoridades competentes (Art. 27.3)
    - `integrateWithDpia()` - Integra con DPIA (Art. 27.4)

### **Entidades JPA**
- **FriaAssessment** - `com.codeflowx.govern.entity.compliance.FriaAssessment`
  - **Tabla:** `FRIAFUNDAMENTALRIGHTSASSESSMENTS` (prefijo `FRA`)
  - **Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/FriaAssessment.java`
  - **Campos principales:**
    - `IDXFRIAASSESSMENT` (Long, PK) - ID autonumérico
    - `IDXPROJECT` (Long, FK) - Referencia a proyecto
    - `FRAPROCESSDESCRIPTION` (String, Art. 27.1.a) - Descripción de procesos
    - `FRAUSAGEPERIOD` (String, Art. 27.1.b) - Período de uso
    - `FRAUSAGEFREQUENCY` (String, Art. 27.1.b) - Frecuencia de uso
    - `FRAAFFECTEDCATEGORIES` (JSONB, Art. 27.1.c) - Categorías afectadas
    - `FRARISKS` (JSONB, Art. 27.1.d) - Riesgos específicos
    - `FRAHUMANOVERSIGHT` (String, Art. 27.1.e) - Medidas supervisión humana
    - `FRAMITIGATIONMEASURES` (JSONB, Art. 27.1.f) - Medidas de mitigación
    - `FRAFINALRISK` (BigDecimal) - Riesgo final calculado según Anexo IX
    - `FRACOMPLETENESSSCORE` (BigDecimal) - Score de completitud (0.00 - 1.00)
    - `FRASTATUS` (String) - Estado: DRAFT, COMPLETED, NOTIFIED
    - `FRAAUTHORITYNOTIFIED` (Boolean) - Si fue notificado a autoridades (Art. 27.3)
    - `FRAAUTHORITYNOTIFIEDAT` (Timestamp) - Fecha de notificación
    - `FRADPIAID` (String) - ID de DPIA vinculado (Art. 27.4)
    - `FRACREATEDAT` (Timestamp) - Fecha creación
    - `FRACREATEDBY` (String) - Usuario creador
    - `IDUUID` (String) - UUID único

- **FriaRisk** - Riesgos específicos (almacenados en JSONB `FRARISKS`)
  - Estructura JSON:
    ```json
    {
      "id": 1,
      "type": "Discrimination",
      "severity": "high",
      "probability": 0.75,
      "impact": "high",
      "description": "..."
    }
    ```

- **MitigationMeasure** - Medidas de mitigación (almacenadas en JSONB `FRAMITIGATIONMEASURES`)
  - Estructura JSON:
    ```json
    {
      "id": 1,
      "description": "...",
      "effectiveness": 0.80,
      "associatedRiskId": 1
    }
    ```

### **Business Services Disponibles**
- **FriaAssessmentBusinessService** ✅
  - **Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/FriaAssessmentBusinessService.java`
  - **Métodos principales:**
    - `createFria(Long projectId, String createdBy)` - Crea evaluación FRIA
      - Consulta: `SELECT * FROM prjprojects WHERE idxproject = ?`
      - Validación: Proyecto no tiene FRIA activa
      - INSERT: `INSERT INTO friafundamentalrightsassessments (...)`
    - `updateFriaSection(Long friaId, String section, Object data)` - Actualiza sección
    - `calculateCompletenessScore(Long friaId)` - Calcula score completitud
      - Fórmula: (pasos completados / 6) * 100
    - `calculateFinalRisk(Long friaId)` - Calcula riesgo final según Anexo IX
      - Fórmula: `Risk = (Severity × Probability × Impact) × (1 - Mitigation Effectiveness)`
      - Consulta: `SELECT * FROM friafundamentalrightsassessments WHERE idxfriaassessment = ?`
    - `validateFriaComplete(Long friaId)` - Valida completitud FRIA
      - Verifica que todos los 6 elementos Art. 27.1 estén completos
    - `notifyAuthority(Long friaId)` - Notifica a autoridades (Art. 27.3)
      - Actualiza: `UPDATE friafundamentalrightsassessments SET fraauthoritynotified = true, fraauthoritynotifiedat = ? WHERE idxfriaassessment = ?`
      - Llamada microservicio (COMENTADA): `leka-fria-generator: POST /api/fria/notify-authority`
    - `integrateWithDpia(Long friaId, String dpiaId)` - Integra con DPIA (Art. 27.4)
      - Actualiza: `UPDATE friafundamentalrightsassessments SET fradpiaid = ? WHERE idxfriaassessment = ?`

### **Servicios CRUD**
- **FriaAssessmentService** ✅
  - **Ubicación:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/compliance/FriaAssessmentService.java`
  - **Métodos:** `findAll()`, `findById()`, `save()`, `delete()`
- **ProjectService** - Gestión de proyectos

### **Lógica de Negocio Detallada**

#### **Cálculo de Riesgo Final (Anexo IX - INC-008):**
```java
// Implementado en FriaAssessmentBusinessService.calculateFinalRisk()
public BigDecimal calculateFinalRisk(FriaAssessment fria) {
    // Parsear riesgos y medidas desde JSONB
    List<FriaRisk> risks = parseRisks(fria.getFriarisks());
    List<MitigationMeasure> measures = parseMeasures(fria.getFriamitigationmeasures());

    BigDecimal totalRisk = BigDecimal.ZERO;

    for (FriaRisk risk : risks) {
        // Calcular riesgo bruto: Severity × Probability × Impact
        BigDecimal severityScore = getSeverityScore(risk.getSeverity()); // LOW=0.25, MEDIUM=0.50, HIGH=0.75, CRITICAL=1.0
        BigDecimal impactScore = getImpactScore(risk.getImpact()); // Bajo=0.25, Medio=0.50, Alto=0.75
        BigDecimal probability = risk.getProbability(); // 0.0 - 1.0

        BigDecimal rawRisk = severityScore
            .multiply(probability)
            .multiply(impactScore);

        // Aplicar efectividad de mitigación
        BigDecimal mitigationEffectiveness = getMitigationEffectiveness(risk.getId(), measures);
        BigDecimal adjustedRisk = rawRisk.multiply(
            BigDecimal.ONE.subtract(mitigationEffectiveness)
        );

        totalRisk = totalRisk.add(adjustedRisk);
    }

    // Normalizar dividiendo por número de riesgos
    if (!risks.isEmpty()) {
        totalRisk = totalRisk.divide(
            new BigDecimal(risks.size()),
            4,
            RoundingMode.HALF_UP
        );
    }

    return totalRisk;
}
```

#### **Validación Cruzada con Métricas Técnicas (INC-007):**
```java
// PENDIENTE: Implementar cuando microservicio esté disponible
// Llamada microservicio Python (COMENTADA):
/*
FriaCrossValidationResult validation = friaValidationClient.crossValidate(friaId);
if (validation.getConsistencyScore() < 0.70) {
    throw new ValidationException(
        "CRITICAL: FRIA no coincide con métricas técnicas reales. " +
        "Score de consistencia: " + validation.getConsistencyScore()
    );
}
*/
```

---

## 📱 PANTALLA 1: Wizard FRIA (6 Pasos)

### **Ruta:** `app/(app)/governance/compliance/fria/page.tsx`

**Estado Actual:** ✅ Existe pero incompleta

### **Estructura del Wizard (6 Pasos):**

#### **Step 1: Descripción de Procesos (Art. 27.1.a)**
- Textarea para describir procesos donde se usa el sistema IA
- Mínimo 50 caracteres
- Validación: descripción completa y clara

#### **Step 2: Período y Frecuencia de Uso (Art. 27.1.b)**
- Campo fecha inicio/fin para período de uso
- Select para frecuencia: Diaria, Semanal, Mensual, Continua, Otra
- Validación: ambos campos obligatorios

#### **Step 3: Categorías de Personas Afectadas (Art. 27.1.c)**
- Multi-select de categorías:
  - Empleados
  - Clientes
  - Ciudadanos
  - Grupos vulnerables (menores, discapacitados, etc.)
- Campo adicional para grupos vulnerables específicos
- Validación: al menos una categoría

#### **Step 4: Riesgos Específicos (Art. 27.1.d)**
- Lista de riesgos con:
  - Tipo de riesgo (Discriminación, Privacidad, Transparencia, etc.)
  - Severidad (Baja, Media, Alta, Crítica)
  - Probabilidad (0.0 - 1.0)
  - Impacto (Bajo, Medio, Alto)
  - Descripción
- Botón "Añadir Riesgo"
- Validación: al menos un riesgo

#### **Step 5: Supervisión Humana (Art. 27.1.e)**
- Textarea para describir medidas de supervisión humana (HITL)
- Tipos de supervisión:
  - Pre-deployment review
  - In-loop supervision
  - Post-deployment review
  - Override capabilities
- Validación: descripción mínima 50 caracteres

#### **Step 6: Medidas de Mitigación (Art. 27.1.f)**
- Lista de medidas de mitigación con:
  - Descripción
  - Efectividad estimada (0.0 - 1.0)
  - Riesgo asociado (relación con Step 4)
- Botón "Añadir Medida"
- Validación: al menos una medida

### **Funcionalidades Adicionales:**

1. **Progress Bar:**
   - Indicador visual de paso actual (1/6, 2/6, etc.)
   - Score de completitud en tiempo real

2. **Navegación:**
   - Botones Previous/Next
   - Validación antes de avanzar
   - Guardado automático al avanzar

3. **Cálculo de Riesgo Final:**
   - Fórmula según Anexo IX: `Risk = (Severity × Probability × Impact) × (1 - Mitigation Effectiveness)`
   - Cálculo automático al completar Step 6
   - Visualización de riesgo final (Bajo, Medio, Alto, Crítico)

4. **Notificación a Autoridades:**
   - Botón "Notificar Autoridades" (Art. 27.3)
   - Solo si riesgo final >= 0.75

5. **Integración con DPIA:**
   - Campo opcional para vincular DPIA existente (Art. 27.4)

### **Mock Data:**

```typescript
// app/(app)/governance/data/mockFria.ts
export const mockFriaWizard = {
  project: {
    id: 1,
    name: "AI Credit Scoring System"
  },
  step1: {
    processDescription: "The AI system is used for automated credit scoring in loan applications. It analyzes applicant data including financial history, employment status, and demographic information to determine creditworthiness."
  },
  step2: {
    usagePeriod: {
      start: "2024-01-01",
      end: "2025-12-31"
    },
    usageFrequency: "daily"
  },
  step3: {
    affectedCategories: ["Loan applicants", "Credit card applicants"],
    vulnerableGroups: ["Low-income individuals", "Recent immigrants"]
  },
  step4: {
    risks: [
      {
        id: 1,
        type: "Discrimination",
        severity: "high",
        probability: 0.75,
        impact: "high",
        description: "Risk of discrimination based on protected characteristics such as race, gender, or age"
      },
      {
        id: 2,
        type: "Privacy",
        severity: "medium",
        probability: 0.50,
        impact: "medium",
        description: "Risk of unauthorized access to sensitive financial data"
      }
    ]
  },
  step5: {
    humanOversight: "Human reviewers will review all loan rejections and decisions with scores below 0.6. Pre-deployment review required for model updates. In-loop supervision for high-value loans (>€50,000)."
  },
  step6: {
    mitigationMeasures: [
      {
        id: 1,
        description: "Regular bias audits every quarter",
        effectiveness: 0.80,
        associatedRiskId: 1
      },
      {
        id: 2,
        description: "Encryption and access controls for data",
        effectiveness: 0.90,
        associatedRiskId: 2
      }
    ]
  }
};

export const mockFriaCalculation = {
  completenessScore: 0.95,
  finalRisk: 0.42, // Calculado según Anexo IX
  riskLevel: "MEDIUM",
  readyForNotification: false
};
```

### **API Routes Mock:**

```typescript
// app/api/compliance/fria/create/route.ts
export async function POST(request: Request) {
  const { projectId } = await request.json();
  return Response.json({
    success: true,
    friaId: 1,
    status: "DRAFT"
  });
}

// app/api/compliance/fria/[friaId]/step/[stepNumber]/route.ts
export async function PUT(request: Request, { params }: { params: { friaId: string, stepNumber: string } }) {
  const data = await request.json();
  return Response.json({
    success: true,
    completenessScore: 0.95
  });
}

// app/api/compliance/fria/[friaId]/calculate-risk/route.ts
export async function POST(request: Request, { params }: { params: { friaId: string } }) {
  return Response.json(mockFriaCalculation);
}

// app/api/compliance/fria/[friaId]/notify-authority/route.ts
export async function POST(request: Request, { params }: { params: { friaId: string } }) {
  return Response.json({
    success: true,
    notifiedAt: new Date().toISOString()
  });
}
```

---

## 📱 PANTALLA 2: Listado de FRIAs

### **Ruta:** `app/(app)/governance/compliance/fria/assessments/page.tsx`

**Estado Actual:** ❌ No existe - **CREAR**

### **Funcionalidades Requeridas:**

1. **Tabla de FRIAs:**
   - Columnas: Proyecto, Fecha creación, Estado, Score completitud, Riesgo final, Acciones
   - Filtros: por proyecto, estado, rango de fechas, nivel de riesgo
   - Búsqueda por nombre de proyecto

2. **Estados:**
   - DRAFT - En progreso
   - COMPLETED - Completada
   - NOTIFIED - Notificada a autoridades

3. **Acciones:**
   - Ver detalle
   - Continuar wizard (si DRAFT)
   - Notificar autoridades (si COMPLETED y riesgo alto)
   - Exportar PDF

### **Mock Data:**

```typescript
export const mockFriaAssessments = [
  {
    id: 1,
    projectId: 1,
    projectName: "AI Credit Scoring System",
    createdAt: "2025-12-01T10:00:00Z",
    status: "COMPLETED",
    completenessScore: 0.95,
    finalRisk: 0.42,
    riskLevel: "MEDIUM"
  },
  {
    id: 2,
    projectId: 2,
    projectName: "Facial Recognition System",
    createdAt: "2025-12-02T14:30:00Z",
    status: "DRAFT",
    completenessScore: 0.60,
    finalRisk: null,
    riskLevel: null
  }
];
```

---

## 📱 PANTALLA 3: Detalle de FRIA

### **Ruta:** `app/(app)/governance/compliance/fria/[id]/page.tsx`

**Estado Actual:** ❌ No existe - **CREAR**

### **Funcionalidades Requeridas:**

1. **Vista Completa del FRIA:**
   - Mostrar todos los 6 pasos completados
   - Visualización de riesgos y medidas de mitigación
   - Gráfico de riesgo final

2. **Información Adicional:**
   - Score de completitud
   - Fecha de creación y última actualización
   - Estado de notificación a autoridades

3. **Acciones:**
   - Editar FRIA (si DRAFT)
   - Notificar autoridades
   - Exportar PDF
   - Vincular DPIA

---

## 🎨 ESTILOS "WOW FACTOR"

### **Estructura Base del Wizard:**

```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import DevelopmentBanner from "@/components/ui/development-banner";
import { FileText, ChevronLeft, ChevronRight } from "lucide-react";
import { useState } from "react";

export default function FriaWizardPage() {
  const { t } = useTranslation();
  const [currentStep, setCurrentStep] = useState(1);
  const totalSteps = 6;

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        {/* Header */}
        <div className="text-center space-y-2">
          <div className="flex items-center justify-center gap-3 mb-4">
            <FileText className="w-8 h-8 text-blue-500" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-blue-500 to-blue-700 bg-clip-text text-transparent">
              {t("compliance.fria.title", "FRIA Assessment")}
            </h1>
          </div>
        </div>

        {/* Progress Bar */}
        <div className="card backdrop-blur-md bg-background/60 border-border/50">
          <div className="card-body">
            <div className="flex items-center justify-between mb-2">
              <span className="text-sm font-medium">
                Step {currentStep} of {totalSteps}
              </span>
              <span className="text-sm text-muted-foreground">
                Completeness: 95%
              </span>
            </div>
            <div className="w-full bg-gray-200 rounded-full h-2">
              <div
                className="bg-blue-500 h-2 rounded-full transition-all"
                style={{ width: `${(currentStep / totalSteps) * 100}%` }}
              />
            </div>
          </div>
        </div>

        {/* Wizard Content */}
        {/* ... */}
      </div>
    </div>
  );
}
```

---

## 📝 TRADUCCIONES REQUERIDAS

Añadir en `app/config/i18n/modules/governance/compliance.ts`:

```typescript
fria: {
  title: {
    es: "Evaluación de Impacto en Derechos Fundamentales (FRIA)",
    en: "Fundamental Rights Impact Assessment (FRIA)"
  },
  steps: {
    step1: {
      title: {
        es: "Descripción de Procesos",
        en: "Process Description"
      },
      description: {
        es: "Art. 27.1.a - Describe los procesos donde se usa el sistema IA",
        en: "Art. 27.1.a - Describe processes where AI system is used"
      }
    }
    // ... más steps
  }
}
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### **Pantalla 1: Wizard (Ya existe - Completar)**
- [ ] Revisar pantalla existente
- [ ] Implementar Step 1 (Descripción de procesos)
- [ ] Implementar Step 2 (Período y frecuencia)
- [ ] Implementar Step 3 (Categorías afectadas)
- [ ] Implementar Step 4 (Riesgos específicos)
- [ ] Implementar Step 5 (Supervisión humana)
- [ ] Implementar Step 6 (Medidas de mitigación)
- [ ] Añadir progress bar y score de completitud
- [ ] Implementar cálculo de riesgo final (Anexo IX)
- [ ] Añadir notificación a autoridades
- [ ] Añadir mock data completo
- [ ] Crear API routes mock

### **Pantalla 2: Listado (Crear nueva)**
- [ ] Crear página `assessments/page.tsx`
- [ ] Implementar tabla con filtros
- [ ] Añadir acciones (ver, continuar, notificar, exportar)
- [ ] Añadir mock data
- [ ] Crear API routes mock
- [ ] Añadir entrada al menú

### **Pantalla 3: Detalle (Crear nueva)**
- [ ] Crear página `[id]/page.tsx`
- [ ] Implementar vista completa del FRIA
- [ ] Añadir visualización de riesgos
- [ ] Añadir acciones (editar, notificar, exportar)
- [ ] Añadir mock data
- [ ] Crear API routes mock

### **General**
- [ ] Añadir traducciones completas
- [ ] Verificar estilos "Wow Factor"
- [ ] Probar funcionalidad completa del wizard
- [ ] Documentar integración con backend

---

## 🔗 REFERENCIAS

- **Documentación Compliance:** `docs/prompts/BUSINESS_LOGIC_COMPLIANCE.md`
- **Prompt Migración:** `docs/prompts/MIGRACION_COMPLIANCE_FRIA.md`
- **Inventario Pantallas:** `docs/PANTALLAS_GOVERNANCE_COMPLIANCE_AI_ACT.md`
- **Artículo EU AI Act:** Art. 27 + Anexo IX

---

**Última actualización:** Diciembre 2025
**Estado:** Listo para implementación en paralelo
