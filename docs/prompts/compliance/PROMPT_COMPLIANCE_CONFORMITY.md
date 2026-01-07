# ✅ PROMPT DE IMPLEMENTACIÓN - EVALUACIÓN DE CONFORMIDAD (Art. 43 + Anexo VI)

**Módulo:** Compliance - Conformity Assessment
**Artículo EU AI Act:** Art. 43 - Evaluación de Conformidad + Anexo VI
**Fecha:** Diciembre 2025
**Estado:** ⏳ Pendiente de implementación completa
**Esfuerzo Estimado:** 4-5 días

---

## 📋 RESUMEN DEL MÓDULO

### **Objetivo**
Implementar el sistema completo de evaluación de conformidad según el Art. 43 y Anexo VI del EU AI Act. El sistema debe ejecutar 4 steps de evaluación (QMS, Documentación, Consistencia) y calcular un score overall para determinar si el sistema está listo para certificación.

### **Pantallas Requeridas**

| # | Ruta Next.js | Estado | Descripción | Prioridad |
|---|--------------|-------|-------------|-----------|
| 1 | `app/(app)/governance/compliance/dashboard/page.tsx` | ✅ Existe | Dashboard de evaluaciones | 🔴 Alta |
| 2 | `app/(app)/governance/compliance/conformity-review/page.tsx` | ✅ Existe | Revisión de conformidad (BPMN) | 🔴 Alta |
| 3 | `app/(app)/governance/compliance/conformity-declaration-manager/page.tsx` | ✅ Existe | Gestión declaraciones | 🔴 Alta |
| 4 | `app/(app)/governance/compliance/assessments/[id]/page.tsx` | ❌ No existe | Detalle de evaluación | 🟡 Media |

---

## 🏗️ ARQUITECTURA Y DEPENDENCIAS

### **Pantallas ZUL Originales**
- **ZUL Dashboard:** `console/platform/governance/compliance/assessment.zul`
  - ViewModel: `ComplianceAssessmentViewModel`
- **ZUL Review:** `console/bpmn/compliance-review-form.zul`
  - ViewModel: `ComplianceReviewViewModel`
- **ZUL Declaration:** `console/gobierno/compliance/conformity-declaration-manager.zul`
  - ViewModel: `ConformityDeclarationManagerViewModel`

### **ViewModels Java**
- **ComplianceAssessmentViewModel** ✅
  - **Paquete:** `com.codeflowx.platform.viewmodel.compliance`
  - **Archivo:** `com/codeflowx/platform/viewmodel/compliance/ComplianceAssessmentViewModel.java`
  - **Servicios Usados:**
    - `@WireVariable ComplianceAssessmentService complianceAssessmentService`
    - `@WireVariable ComplianceDashboardService complianceDashboardService`
  - **Métodos Principales:**
    - `loadAssessments()` - Carga lista de evaluaciones
    - `createAssessment(Long projectId)` - Crea nueva evaluación
    - `executeStep2QmsCheck(Long assessmentId)` - Ejecuta step 2 (QMS)
    - `executeStep3DocReview(Long assessmentId)` - Ejecuta step 3 (Documentación)
    - `calculateOverallScore(Long assessmentId)` - Calcula score overall

- **ComplianceReviewViewModel** ✅
  - **Paquete:** `com.codeflowx.govern.workflow.viewmodels`
  - **Archivo:** `com/codeflowx/govern/workflow/viewmodels/ComplianceReviewViewModel.java`
  - **Tipo:** User Task BPMN (compliance-review)
  - **Servicios Usados:**
    - `@WireVariable ModelService modelService`
    - `@WireVariable TaskService taskService` (Flowable)

- **ConformityDeclarationManagerViewModel** ✅
  - **Paquete:** `com.codeflowx.govern.viewmodel.compliance`
  - **Archivo:** `com/codeflowx/govern/viewmodel/compliance/ConformityDeclarationManagerViewModel.java`
  - **Servicios Usados:**
    - `@WireVariable ComplianceAssessmentService complianceAssessmentService`
  - **Entidades Usadas:** `ComplianceAssessment`

### **Entidades JPA**
- **ComplianceAssessment** - `com.codeflowx.govern.entity.compliance.ComplianceAssessment`
  - **Tabla:** `COMCOMPLIANCEASSESSMENTS` (prefijo `COM`)
  - **Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/ComplianceAssessment.java`
  - **Campos principales:**
    - `IDXCOMPLIANCEASSESSMENT` (Long, PK) - ID autonumérico
    - `IDXPROJECT` (Long, FK) - Referencia a proyecto
    - `COMASSESSMENTTYPE` (String) - Tipo de evaluación
    - `COMASSESSMENTDATE` (Timestamp) - Fecha de evaluación
    - `COMSTEP1SCORE` (BigDecimal) - Score step 1 (0.00 - 1.00)
    - `COMSTEP2QMSSCORE` (BigDecimal) - Score step 2 QMS (0.00 - 1.00)
    - `COMSTEP3DOCSCORE` (BigDecimal) - Score step 3 Documentación (0.00 - 1.00)
    - `COMSTEP4CONSISTENCYSCORE` (BigDecimal) - Score step 4 Consistencia (0.00 - 1.00)
    - `COMOVERALLSCORE` (BigDecimal) - Score overall (promedio ponderado)
    - `COMREADYFORCERTIFICATION` (Boolean) - Listo para certificación (overallScore >= 0.80)
    - `COMANNEXVICOMPLIANT` (Boolean) - Cumple Anexo VI
    - `COMCERTIFICATEID` (String) - ID de certificado (si existe)
    - `COMCREATEDAT` (Timestamp) - Fecha creación
    - `COMCREATEDBY` (String) - Usuario creador
    - `IDUUID` (String) - UUID único

### **Business Services Disponibles**
- **ComplianceAssessmentBusinessService** ✅
  - **Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/ComplianceAssessmentBusinessService.java`
  - **Métodos principales:**
    - `createAssessment(Long projectId, String assessmentType, String executedBy)` - Crea evaluación
      - Consulta: `SELECT * FROM prjprojects WHERE idxproject = ? AND prjishighrisk = true`
      - INSERT: `INSERT INTO comcomplianceassessments (...)`
    - `executeStep2QmsCheck(Long assessmentId, String executedBy)` - Ejecuta step 2
      - Llama: `qualityManagementSystemBusinessService.calculateQmsComplianceScore(projectId)`
      - Actualiza: `UPDATE comcomplianceassessments SET comstep2qmsscore = ? WHERE idxcomplianceassessment = ?`
    - `executeStep3DocReview(Long assessmentId, String executedBy)` - Ejecuta step 3
      - Llama: `technicalDocumentationBusinessService.calculateDocumentationScore(projectId)`
      - Actualiza: `UPDATE comcomplianceassessments SET comstep3docscore = ? WHERE idxcomplianceassessment = ?`
    - `calculateOverallScore(Long assessmentId)` - Calcula score overall
      - Fórmula: `overallScore = (step2QmsScore + step3DocScore + step4ConsistencyScore) / 3`
      - Actualiza: `UPDATE comcomplianceassessments SET comoverallscore = ?, comreadyforcertification = ? WHERE idxcomplianceassessment = ?`
      - `readyForCertification = (overallScore >= 0.80)`

### **Servicios CRUD**
- **ComplianceAssessmentService** ✅
  - **Ubicación:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/compliance/ComplianceAssessmentService.java`
  - **Métodos:** `findAll()`, `findById()`, `save()`, `delete()`

### **Lógica de Negocio Detallada**

#### **Evaluación de Conformidad (4 Steps según Anexo VI):**

**Step 1: Verificación Inicial**
- Validar que proyecto sea alto riesgo
- Verificar que tenga FRIA completada
- Verificar que tenga clasificación Anexo III

**Step 2: QMS Check (Art. 17)**
```java
// Implementado en ComplianceAssessmentBusinessService.executeStep2QmsCheck()
public void executeStep2QmsCheck(Long assessmentId, String executedBy) {
    ComplianceAssessment assessment = findById(assessmentId);
    Project project = projectService.findById(assessment.getIdxproject());

    // Calcular score QMS (13 módulos)
    BigDecimal qmsScore = qualityManagementSystemBusinessService.calculateQmsComplianceScore(
        project.getId()
    );

    assessment.setComstep2qmsscore(qmsScore);
    assessment.setComupdatedby(executedBy);
    assessment.setComupdatedat(new Timestamp(System.currentTimeMillis()));
    save(assessment);
}
```

**Step 3: Documentación Técnica (Art. 11, Anexo IV)**
```java
// Implementado en ComplianceAssessmentBusinessService.executeStep3DocReview()
public void executeStep3DocReview(Long assessmentId, String executedBy) {
    ComplianceAssessment assessment = findById(assessmentId);
    Project project = projectService.findById(assessment.getIdxproject());

    // Calcular score de documentación (11 secciones Anexo IV)
    BigDecimal docScore = technicalDocumentationBusinessService.calculateDocumentationScore(
        project.getId()
    );

    assessment.setComstep3docscore(docScore);
    assessment.setComupdatedby(executedBy);
    assessment.setComupdatedat(new Timestamp(System.currentTimeMillis()));
    save(assessment);
}
```

**Step 4: Verificación de Consistencia**
- Validar que scores de steps 2 y 3 sean consistentes
- Verificar que no haya contradicciones entre QMS y documentación
- Score = promedio de consistencia

**Cálculo de Score Overall:**
```java
// Implementado en ComplianceAssessmentBusinessService.calculateOverallScore()
public BigDecimal calculateOverallScore(Long assessmentId) {
    ComplianceAssessment assessment = findById(assessmentId);

    BigDecimal step2 = assessment.getComstep2qmsscore() != null ?
        assessment.getComstep2qmsscore() : BigDecimal.ZERO;
    BigDecimal step3 = assessment.getComstep3docscore() != null ?
        assessment.getComstep3docscore() : BigDecimal.ZERO;
    BigDecimal step4 = assessment.getComstep4consistencyscore() != null ?
        assessment.getComstep4consistencyscore() : BigDecimal.ZERO;

    BigDecimal sum = step2.add(step3).add(step4);
    BigDecimal avg = sum.divide(new BigDecimal(3), 4, RoundingMode.HALF_UP);

    assessment.setComoverallscore(avg);
    assessment.setComreadyforcertification(avg.compareTo(new BigDecimal("0.80")) >= 0);
    assessment.setComannexvicompliant(avg.compareTo(new BigDecimal("0.80")) >= 0);
    save(assessment);

    return avg;
}
```

---

## 📱 PANTALLA 1: Dashboard de Evaluaciones

### **Ruta:** `app/(app)/governance/compliance/dashboard/page.tsx`

**Estado Actual:** ✅ Existe pero incompleta

### **Funcionalidades Requeridas:**

1. **Métricas Principales:**
   - Total de evaluaciones
   - Evaluaciones completadas
   - Evaluaciones listas para certificación
   - Score promedio overall

2. **Listado de Evaluaciones:**
   - Tabla con evaluaciones
   - Columnas: Proyecto, Fecha, Step 2 Score, Step 3 Score, Overall Score, Estado
   - Filtros: por proyecto, estado, rango de fechas
   - Acciones: Ver detalle, Continuar evaluación

3. **Gráficos:**
   - Distribución de scores
   - Evolución temporal de evaluaciones
   - Gaps más comunes

### **Mock Data:**

```typescript
// app/(app)/governance/data/mockConformityAssessment.ts
export const mockConformityAssessments = {
  metrics: {
    total: 25,
    completed: 18,
    readyForCertification: 12,
    averageScore: 0.82
  },
  assessments: [
    {
      id: 1,
      projectId: 1,
      projectName: "AI Credit Scoring System",
      assessmentDate: "2025-12-01T10:00:00Z",
      step2QmsScore: 0.85,
      step3DocScore: 0.80,
      step4ConsistencyScore: 0.90,
      overallScore: 0.85,
      readyForCertification: true,
      status: "COMPLETED"
    },
    {
      id: 2,
      projectId: 2,
      projectName: "Facial Recognition System",
      assessmentDate: "2025-12-02T14:30:00Z",
      step2QmsScore: 0.70,
      step3DocScore: 0.65,
      step4ConsistencyScore: null,
      overallScore: null,
      readyForCertification: false,
      status: "IN_PROGRESS"
    }
  ]
};
```

---

## 📱 PANTALLA 2: Revisión de Conformidad (BPMN)

### **Ruta:** `app/(app)/governance/compliance/conformity-review/page.tsx`

**Estado Actual:** ✅ Existe pero incompleta

### **Funcionalidades Requeridas:**

1. **Información del Proyecto:**
   - Datos del proyecto en evaluación
   - Estado actual de la evaluación

2. **Revisión de Steps:**
   - Step 2: QMS Check - Mostrar score y gaps
   - Step 3: Documentación - Mostrar score y secciones faltantes
   - Step 4: Consistencia - Verificar coherencia

3. **Decisiones:**
   - Aprobar evaluación
   - Rechazar con comentarios
   - Solicitar correcciones

### **Mock Data:**

```typescript
export const mockConformityReview = {
  assessmentId: 1,
  project: {
    id: 1,
    name: "AI Credit Scoring System"
  },
  steps: {
    step2: {
      score: 0.85,
      gaps: []
    },
    step3: {
      score: 0.80,
      missingSections: ["Section 8: Cybersecurity Measures"]
    },
    step4: {
      score: 0.90,
      consistencyIssues: []
    }
  },
  overallScore: 0.85,
  readyForCertification: true
};
```

---

## 📱 PANTALLA 3: Gestión de Declaraciones de Conformidad

### **Ruta:** `app/(app)/governance/compliance/conformity-declaration-manager/page.tsx`

**Estado Actual:** ✅ Existe pero incompleta

### **Funcionalidades Requeridas:**

1. **Listado de Declaraciones:**
   - Tabla con declaraciones generadas
   - Filtros: por proyecto, fecha, estado

2. **Generación de Declaración:**
   - Información del proveedor
   - Información del sistema IA
   - Base de conformidad (conformity basis)
   - Cumplimiento por artículo (Art. 9-15)
   - Firma digital
   - Generación de PDF

3. **Gestión:**
   - Ver declaración
   - Descargar PDF
   - Regenerar declaración

### **Mock Data:**

```typescript
export const mockConformityDeclarations = [
  {
    id: 1,
    assessmentId: 1,
    projectName: "AI Credit Scoring System",
    providerName: "ACME Corporation",
    generatedAt: "2025-12-01T15:00:00Z",
    articles: {
      art9: true,  // Risk Management
      art10: true, // Data Governance
      art11: true, // Technical Documentation
      art12: true, // Record-Keeping
      art13: true, // Transparency
      art14: true, // Human Oversight
      art15: true  // Accuracy, Robustness, Cybersecurity
    },
    pdfUrl: "/api/compliance/declarations/1/pdf"
  }
];
```

---

## 📱 PANTALLA 4: Detalle de Evaluación

### **Ruta:** `app/(app)/governance/compliance/assessments/[id]/page.tsx`

**Estado Actual:** ❌ No existe - **CREAR**

### **Funcionalidades Requeridas:**

1. **Vista Completa de la Evaluación:**
   - Información del proyecto
   - Scores de cada step
   - Score overall
   - Estado de certificación

2. **Detalles por Step:**
   - Step 2: Detalle de QMS con gaps
   - Step 3: Detalle de documentación con secciones faltantes
   - Step 4: Análisis de consistencia

3. **Acciones:**
   - Continuar evaluación (si IN_PROGRESS)
   - Generar declaración de conformidad (si readyForCertification)
   - Exportar reporte PDF

---

## 🎨 ESTILOS "WOW FACTOR"

### **Estructura Base:**

```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import DevelopmentBanner from "@/components/ui/development-banner";
import { CheckCircle2, FileCheck } from "lucide-react";

export default function ConformityAssessmentPage() {
  const { t } = useTranslation();

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-green-400/30 rounded-full animate-pulse" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        {/* Header */}
        <div className="text-center space-y-2">
          <div className="flex items-center justify-center gap-3 mb-4">
            <FileCheck className="w-8 h-8 text-green-500" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-green-500 to-green-700 bg-clip-text text-transparent">
              {t("compliance.conformity.title", "Conformity Assessment")}
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
conformity: {
  title: {
    es: "Evaluación de Conformidad",
    en: "Conformity Assessment"
  },
  steps: {
    step2: {
      title: {
        es: "Verificación QMS (Art. 17)",
        en: "QMS Check (Art. 17)"
      }
    },
    step3: {
      title: {
        es: "Revisión Documentación Técnica (Art. 11)",
        en: "Technical Documentation Review (Art. 11)"
      }
    }
  }
}
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### **Pantalla 1: Dashboard (Ya existe - Completar)**
- [ ] Revisar pantalla existente
- [ ] Añadir métricas principales
- [ ] Implementar listado con filtros
- [ ] Añadir gráficos de distribución
- [ ] Añadir mock data
- [ ] Crear API routes mock

### **Pantalla 2: Review (Ya existe - Completar)**
- [ ] Revisar pantalla existente
- [ ] Implementar revisión de steps
- [ ] Añadir decisiones (aprobar/rechazar)
- [ ] Añadir mock data
- [ ] Crear API routes mock

### **Pantalla 3: Declaration Manager (Ya existe - Completar)**
- [ ] Revisar pantalla existente
- [ ] Implementar generación de declaración
- [ ] Añadir cumplimiento por artículo
- [ ] Añadir mock data
- [ ] Crear API routes mock

### **Pantalla 4: Detail (Crear nueva)**
- [ ] Crear página `assessments/[id]/page.tsx`
- [ ] Implementar vista completa
- [ ] Añadir detalles por step
- [ ] Añadir acciones
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
- **Inventario Pantallas:** `docs/PANTALLAS_GOVERNANCE_COMPLIANCE_AI_ACT.md`
- **Artículo EU AI Act:** Art. 43 + Anexo VI

---

**Última actualización:** Diciembre 2025
**Estado:** Listo para implementación en paralelo
