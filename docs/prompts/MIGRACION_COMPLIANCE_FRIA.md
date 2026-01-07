# Prompt de Migración - Compliance - FRIA Assessment (Art. 27)

## Contexto del Módulo

**Módulo:** Compliance
**Tipo de Pantalla:** Wizard Multi-Step
**Total de Pantallas:** 1
**Artículo EU AI Act:** Art. 27 - Fundamental Rights Impact Assessment

---

## Documentación Funcional Asociada

**Documento:** `docs/funcional/compliance/01_REORGANIZACION_PANTALLAS_COMPLIANCE.md`

**Ubicación:** `suinsit.nova.web/docs/funcional/compliance/01_REORGANIZACION_PANTALLAS_COMPLIANCE.md`

### Resumen de la Documentación

Esta pantalla implementa un **Wizard de 6 pasos** para realizar evaluaciones FRIA (Fundamental Rights Impact Assessment) según el **Art. 27 del EU AI Act**. Cada paso corresponde a uno de los 6 elementos mandatorios del Art. 27.1:

1. **Step 1:** Descripción de procesos donde se usa el sistema IA (Art. 27.1.a)
2. **Step 2:** Período y frecuencia de uso (Art. 27.1.b)
3. **Step 3:** Categorías de personas afectadas (Art. 27.1.c)
4. **Step 4:** Riesgos específicos a derechos fundamentales (Art. 27.1.d)
5. **Step 5:** Medidas de supervisión humana (Art. 27.1.e)
6. **Step 6:** Medidas de mitigación (Art. 27.1.f)

Al finalizar, genera FRIA completo, calcula score de completitud y puede notificar autoridades.

---

## Arquitectura del Módulo

### ViewModel Identificado

#### FriaWizardViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.compliance`
- **Archivo:** `com/codeflowx/govern/viewmodel/compliance/FriaWizardViewModel.java`
- **Servicios Usados:**
  - `ProjectService` - Gestión de proyectos
  - `FriaAssessmentBusinessService` - Lógica de negocio FRIA
- **Entidades Usadas:**
  - `FriaAssessment` - Evaluación FRIA
  - `Project` - Proyecto asociado
- **Mock Mode:** ❌ No (usa servicios reales)
- **Funcionalidades Principales:**
  - `nextStep()` - Avanza al siguiente paso
  - `previousStep()` - Retrocede al paso anterior
  - `saveStepData()` - Guarda datos del paso actual
  - `calculateCompletenessScore()` - Calcula score de completitud (0.00 - 1.00)
  - `completeFria()` - Completa FRIA y calcula riesgo final
  - `notifyAuthority()` - Notifica a autoridades competentes

### Entidades JPA Principales

- **FriaAssessment** - `com.codeflowx.govern.entity.compliance.FriaAssessment`
  - `FRAPROCESSDESCRIPTION` (Art. 27.1.a) - Descripción de procesos
  - `FRAUSAGEPERIOD` (Art. 27.1.b) - Período de uso
  - `FRAUSAGEFREQUENCY` (Art. 27.1.b) - Frecuencia de uso
  - `FRAAFFECTEDCATEGORIES` (Art. 27.1.c) - Categorías afectadas (JSONB)
  - `FRARISKS` (Art. 27.1.d) - Riesgos específicos (JSONB)
  - `FRAHUMANOVERSIGHT` (Art. 27.1.e) - Medidas supervisión humana
  - `FRAMITIGATIONMEASURES` (Art. 27.1.f) - Medidas de mitigación
  - `FRAFINALRISK` - Riesgo final calculado (BigDecimal)
  - `FRACOMPLETENESSSCORE` - Score de completitud (0.00 - 1.00)

### Business Services Disponibles

- **FriaAssessmentBusinessService** ✅
  - `createFria(Long projectId, String createdBy)` - Crea evaluación FRIA
  - `updateFriaSection(Long friaId, String section, Object data)` - Actualiza sección
  - `calculateCompletenessScore(Long friaId)` - Calcula score completitud
  - `calculateFinalRisk(Long friaId)` - Calcula riesgo final según Anexo IX
  - `validateFriaComplete(Long friaId)` - Valida completitud FRIA
  - `notifyAuthority(Long friaId)` - Notifica a autoridades
  - `integrateWithDpia(Long friaId, String dpiaId)` - Integra con DPIA (Art. 27.4)

---

## ⚠️ IMPORTANTE: Revisar Pantallas Existentes

**ANTES de migrar, verificar si la pantalla ya existe en Next.js:**

### 1. Páginas Existentes en `app/(app)/`

**Ruta esperada:** `app/(app)/governance/compliance/fria/page.tsx`

**Estado:** ❌ **NO EXISTE** - Crear nueva página

### 2. Entradas en el Menú (`app/config/modules.ts`)

**Entrada existente:** ✅ **SÍ EXISTE**
- Nombre: "FRIA Assessment (Art. 27)"
- Ruta: `/governance/compliance/fria`
- Icono: `FileText`
- Roles: `["admin", "project_manager", "agencia", "it", "oem", "business_admin"]`

---

## Pantalla a Migrar

### 1. fria-wizard
- **Archivo ZUL:** `console/gobierno/compliance/fria-wizard.zul`
- **ViewModel Asociado:** `FriaWizardViewModel.java`
- **Servicio Backend:** `FriaAssessmentBusinessService`, `ProjectService`
- **Entidad JPA:** `FriaAssessment`, `Project`
- **Tipo:** Wizard multi-step
- **Ruta Next.js:** `/governance/compliance/fria`

---

## Estrategia de Migración

### 1. Patrón de Migración

#### Para Wizard Multi-Step:
- Implementar componente de wizard con 6 pasos
- Navegación: Previous/Next buttons
- Progress bar mostrando paso actual
- Validación por paso antes de avanzar
- Guardado automático de datos al avanzar
- Cálculo de score de completitud en tiempo real
- Mock data para cada paso del wizard
- API routes mock: `/api/compliance/fria/[step]`

### 2. Estructura del Wizard

```typescript
// Estructura del wizard
const wizardSteps = [
  {
    id: 1,
    title: "Process Description",
    description: "Art. 27.1.a - Describe processes where AI system is used",
    component: ProcessDescriptionStep,
    validation: (data) => data.processDescription.length >= 50
  },
  {
    id: 2,
    title: "Usage Period & Frequency",
    description: "Art. 27.1.b - Period and frequency of use",
    component: UsagePeriodStep,
    validation: (data) => data.usagePeriod && data.usageFrequency
  },
  {
    id: 3,
    title: "Affected Categories",
    description: "Art. 27.1.c - Categories of persons affected",
    component: AffectedCategoriesStep,
    validation: (data) => data.affectedCategories.length > 0
  },
  {
    id: 4,
    title: "Specific Risks",
    description: "Art. 27.1.d - Specific risks to fundamental rights",
    component: SpecificRisksStep,
    validation: (data) => data.risks.length > 0
  },
  {
    id: 5,
    title: "Human Oversight",
    description: "Art. 27.1.e - Human oversight measures",
    component: HumanOversightStep,
    validation: (data) => data.humanOversight.length >= 50
  },
  {
    id: 6,
    title: "Mitigation Measures",
    description: "Art. 27.1.f - Mitigation measures",
    component: MitigationMeasuresStep,
    validation: (data) => data.mitigationMeasures.length >= 50
  }
];
```

### 3. Mock Data

```typescript
// app/(app)/governance/data/mockData.ts
export const mockFriaData = {
  step1: {
    processDescription: "The AI system is used for automated credit scoring in loan applications..."
  },
  step2: {
    usagePeriod: "2024-01-01 to 2025-12-31",
    usageFrequency: "daily"
  },
  step3: {
    affectedCategories: [
      "Loan applicants",
      "Credit card applicants",
      "Mortgage applicants"
    ]
  },
  step4: {
    risks: [
      {
        type: "Discrimination",
        severity: "high",
        description: "Risk of discrimination based on protected characteristics"
      }
    ]
  },
  step5: {
    humanOversight: "Human reviewers will review all loan rejections..."
  },
  step6: {
    mitigationMeasures: "Regular bias audits, fairness testing, transparency measures..."
  }
};
```

---

## Estructura de la Página Next.js

### Ruta: `app/(app)/governance/compliance/fria/page.tsx`

```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import DevelopmentBanner from "@/components/ui/development-banner";
import { FileText } from "lucide-react";
import { useState } from "react";

export default function FriaWizardPage() {
  const { t } = useTranslation();
  const [currentStep, setCurrentStep] = useState(1);
  const [friaData, setFriaData] = useState(mockFriaData);
  const [completenessScore, setCompletenessScore] = useState(0.0);

  const totalSteps = 6;

  const handleNext = () => {
    if (currentStep < totalSteps) {
      setCurrentStep(currentStep + 1);
      // Calcular score de completitud
      calculateCompletenessScore();
    }
  };

  const handlePrevious = () => {
    if (currentStep > 1) {
      setCurrentStep(currentStep - 1);
    }
  };

  const calculateCompletenessScore = () => {
    // TODO: Implementar cálculo de score
    const score = 0.67; // Mock
    setCompletenessScore(score);
  };

  const handleComplete = async () => {
    // TODO: Implementar llamada a API
    console.log("Completing FRIA:", friaData);
  };

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
            <FileText className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("compliance.fria.title", "FRIA Assessment")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t("compliance.fria.subtitle", "Fundamental Rights Impact Assessment - Art. 27")}
          </p>
        </div>

        {/* Progress Bar */}
        <div className="card backdrop-blur-md bg-background/60 border-border/50">
          <div className="card-body">
            <div className="flex items-center justify-between mb-2">
              <span className="text-sm font-medium">
                Step {currentStep} of {totalSteps}
              </span>
              <span className="text-sm text-muted-foreground">
                Completeness: {(completenessScore * 100).toFixed(0)}%
              </span>
            </div>
            <div className="w-full bg-gray-200 rounded-full h-2">
              <div
                className="bg-primary h-2 rounded-full transition-all"
                style={{ width: `${(currentStep / totalSteps) * 100}%` }}
              />
            </div>
          </div>
        </div>

        {/* Wizard Content */}
        <div className="card backdrop-blur-md bg-background/60 border-border/50">
          <div className="card-body">
            {/* Render step component based on currentStep */}
            {currentStep === 1 && <ProcessDescriptionStep data={friaData.step1} />}
            {currentStep === 2 && <UsagePeriodStep data={friaData.step2} />}
            {currentStep === 3 && <AffectedCategoriesStep data={friaData.step3} />}
            {currentStep === 4 && <SpecificRisksStep data={friaData.step4} />}
            {currentStep === 5 && <HumanOversightStep data={friaData.step5} />}
            {currentStep === 6 && <MitigationMeasuresStep data={friaData.step6} />}
          </div>
        </div>

        {/* Navigation Buttons */}
        <div className="flex justify-between">
          <button
            className="px-6 py-2 border rounded-lg hover:bg-background/50 transition-all"
            onClick={handlePrevious}
            disabled={currentStep === 1}
          >
            Previous
          </button>
          {currentStep < totalSteps ? (
            <button
              className="px-6 py-2 bg-primary text-primary-foreground rounded-lg hover:bg-primary/90 transition-all"
              onClick={handleNext}
            >
              Next
            </button>
          ) : (
            <button
              className="px-6 py-2 bg-primary text-primary-foreground rounded-lg hover:bg-primary/90 transition-all"
              onClick={handleComplete}
            >
              Complete FRIA
            </button>
          )}
        </div>
      </div>
    </div>
  );
}
```

---

## Checklist de Implementación

- [ ] Crear página `app/(app)/governance/compliance/fria/page.tsx`
- [ ] Implementar componente wizard con 6 pasos
- [ ] Crear componentes para cada paso del wizard
- [ ] Implementar validación por paso
- [ ] Añadir progress bar y score de completitud
- [ ] Añadir mock data para cada paso
- [ ] Implementar estilos "Wow Factor"
- [ ] Añadir traducciones en `app/config/i18n.ts`
- [ ] Verificar entrada en menú (ya existe)
- [ ] Probar funcionalidad completa del wizard
- [ ] Documentar API endpoints para integración futura

---

**Última actualización:** Noviembre 2025
**Estado:** Listo para implementación
