# 📋 PROMPT DE IMPLEMENTACIÓN - QMS (Art. 17)

**Módulo:** Compliance - Quality Management System
**Artículo EU AI Act:** Art. 17 - Sistema de Gestión de Calidad
**Fecha:** Diciembre 2025
**Estado:** ⏳ Pendiente de implementación completa
**Esfuerzo Estimado:** 3-4 días

---

## 📋 RESUMEN DEL MÓDULO

### **Objetivo**
Implementar el dashboard completo de QMS (Quality Management System) según el Art. 17 del EU AI Act. El sistema debe calcular scores de compliance por módulo (13 módulos) y detectar gaps.

### **Pantallas Requeridas**

| # | Ruta Next.js | Estado | Descripción | Prioridad |
|---|--------------|-------|-------------|-----------|
| 1 | `app/(app)/governance/compliance/qms/page.tsx` | ✅ Existe | Dashboard QMS | 🔴 Alta |
| 2 | `app/(app)/governance/compliance/conformity-review/page.tsx` | ✅ Existe | Revisión gaps QMS (BPMN) | 🔴 Alta |

---

## 🏗️ ARQUITECTURA Y DEPENDENCIAS

### **Pantallas ZUL Originales**
- **ZUL Dashboard:** `console/gobierno/governance/qms-compliance.zul`
  - ViewModel: No identificado específicamente
- **ZUL Review:** `console/bpmn/review-qms-gaps-form.zul`
  - ViewModel: `ReviewQmsGapsViewModel`

### **ViewModels Java**
- **ReviewQmsGapsViewModel** ✅
  - **Paquete:** `com.codeflowx.govern.viewmodel.compliance`
  - **Archivo:** `com/codeflowx/govern/viewmodel/compliance/ReviewQmsGapsViewModel.java`
  - **Tipo:** User Task BPMN (reviewQmsGaps)
  - **Servicios Usados:**
    - `@WireVariable ModelService modelService`
    - `@WireVariable TaskService taskService` (Flowable)
    - `@WireVariable QualityManagementSystemBusinessService qualityManagementSystemBusinessService`

### **Entidades JPA**
- **QualityManagementSystem** - `com.codeflowx.govern.entity.compliance.QualityManagementSystem`
  - **Tabla:** `GOVQUALITYMANAGEMENTSYSTEMS` (prefijo `GOV`)
  - **Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/QualityManagementSystem.java`
  - **Campos principales:**
    - `IDXQUALITYMANAGEMENTSYSTEM` (Long, PK) - ID autonumérico
    - `IDXPROJECT` (Long, FK) - Referencia a proyecto
    - `QMSOVERALLSCORE` (BigDecimal) - Score overall QMS (0.00 - 1.00)
    - `QMSMODULESCORES` (JSONB) - Scores por módulo (13 módulos)
    - `QMSGAPS` (JSONB) - Gaps detectados
    - `QMSCOMPLIANCESTRATEGY` (String) - Estrategia de compliance
    - `QMSCREATEDAT` (Timestamp) - Fecha creación
    - `IDUUID` (String) - UUID único

### **Business Services Disponibles**
- **QualityManagementSystemBusinessService** ✅
  - **Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/QualityManagementSystemBusinessService.java`
  - **Métodos principales:**
    - `calculateQmsComplianceScore(Long projectId)` - Calcula score QMS overall
      - Calcula score por cada uno de los 13 módulos
      - Score overall = promedio de scores de módulos
      - Retorna: `BigDecimal` (0.00 - 1.00)
    - `getQmsGaps(Long projectId)` - Obtiene gaps QMS
      - Compara scores de módulos con threshold (0.80)
      - Identifica módulos con score < 0.80
      - Retorna: `List<QmsGap>`
    - `getComplianceStrategy(Long projectId)` - Obtiene estrategia compliance
      - Consulta: `SELECT qmscompliancestrategy FROM govqualitymanagementsystems WHERE idxproject = ?`
    - `updateComplianceStrategy(Long projectId, String strategy)` - Actualiza estrategia
      - Actualiza: `UPDATE govqualitymanagementsystems SET qmscompliancestrategy = ? WHERE idxproject = ?`

### **13 Módulos QMS según Art. 17:**
1. **Estrategia de compliance** - Estrategia general de cumplimiento
2. **Gestión de riesgos** - Procesos de identificación y mitigación de riesgos
3. **Gestión de datos** - Gobernanza y calidad de datos
4. **Diseño y desarrollo** - Procesos de diseño y desarrollo de sistemas IA
5. **Validación y testing** - Procedimientos de validación y pruebas
6. **Documentación técnica** - Gestión de documentación técnica (Art. 11)
7. **Supervisión humana** - Medidas de supervisión humana (Art. 14)
8. **Acciones correctoras** - Gestión de acciones correctoras
9. **Post-market monitoring** - Monitoreo post-mercado (Art. 20)
10. **Gestión de cambios** - Control de cambios en sistemas IA
11. **Gestión de proveedores** - Gestión de proveedores y subcontratistas
12. **Auditoría interna** - Procesos de auditoría interna
13. **Gestión de no conformidades** - Gestión de no conformidades

### **Servicios CRUD**
- **QualityManagementSystemService** ✅
  - **Ubicación:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/compliance/QualityManagementSystemService.java`
  - **Métodos:** `findAll()`, `findById()`, `save()`, `delete()`

### **Lógica de Negocio Detallada**

#### **Cálculo de Score QMS:**
```java
// Implementado en QualityManagementSystemBusinessService.calculateQmsComplianceScore()
public BigDecimal calculateQmsComplianceScore(Long projectId) {
    Project project = projectService.findById(projectId);

    // Calcular score por cada módulo (13 módulos)
    Map<String, BigDecimal> moduleScores = new HashMap<>();

    moduleScores.put("COMPLIANCE_STRATEGY", calculateComplianceStrategyScore(projectId));
    moduleScores.put("RISK_MANAGEMENT", calculateRiskManagementScore(projectId));
    moduleScores.put("DATA_GOVERNANCE", calculateDataGovernanceScore(projectId));
    moduleScores.put("DESIGN_DEVELOPMENT", calculateDesignDevelopmentScore(projectId));
    moduleScores.put("VALIDATION_TESTING", calculateValidationTestingScore(projectId));
    moduleScores.put("TECHNICAL_DOCUMENTATION", calculateTechnicalDocumentationScore(projectId));
    moduleScores.put("HUMAN_OVERSIGHT", calculateHumanOversightScore(projectId));
    moduleScores.put("CORRECTIVE_ACTIONS", calculateCorrectiveActionsScore(projectId));
    moduleScores.put("POST_MARKET_MONITORING", calculatePostMarketMonitoringScore(projectId));
    moduleScores.put("CHANGE_MANAGEMENT", calculateChangeManagementScore(projectId));
    moduleScores.put("SUPPLIER_MANAGEMENT", calculateSupplierManagementScore(projectId));
    moduleScores.put("INTERNAL_AUDIT", calculateInternalAuditScore(projectId));
    moduleScores.put("NON_CONFORMITY_MANAGEMENT", calculateNonConformityManagementScore(projectId));

    // Calcular score overall (promedio)
    BigDecimal sum = moduleScores.values().stream()
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    BigDecimal overallScore = sum.divide(new BigDecimal(13), 4, RoundingMode.HALF_UP);

    // Guardar en QMS
    QualityManagementSystem qms = getOrCreateQms(projectId);
    qms.setQmsmodulescores(moduleScores.toJson());
    qms.setQmsoverallscore(overallScore);
    save(qms);

    return overallScore;
}
```

#### **Detección de Gaps:**
```java
// Implementado en QualityManagementSystemBusinessService.getQmsGaps()
public List<QmsGap> getQmsGaps(Long projectId) {
    QualityManagementSystem qms = getQms(projectId);
    Map<String, BigDecimal> moduleScores = parseModuleScores(qms.getQmsmodulescores());

    List<QmsGap> gaps = new ArrayList<>();
    BigDecimal threshold = new BigDecimal("0.80");

    for (Map.Entry<String, BigDecimal> entry : moduleScores.entrySet()) {
        if (entry.getValue().compareTo(threshold) < 0) {
            QmsGap gap = new QmsGap();
            gap.setModule(entry.getKey());
            gap.setCurrentScore(entry.getValue());
            gap.setTargetScore(threshold);
            gap.setGap(threshold.subtract(entry.getValue()));
            gaps.add(gap);
        }
    }

    return gaps;
}
```

---

## 📱 PANTALLA 1: Dashboard QMS

### **Ruta:** `app/(app)/governance/compliance/qms/page.tsx`

**Estado Actual:** ✅ Existe pero incompleta

### **Funcionalidades Requeridas:**

1. **Métricas Principales:**
   - Score QMS overall (0.00 - 1.00)
   - Número de módulos cumpliendo (score >= 0.80)
   - Número de gaps detectados
   - Estado de compliance

2. **Scores por Módulo:**
   - Lista de 13 módulos con sus scores
   - Indicador visual (verde/amarillo/rojo) según score
   - Gaps destacados

3. **Gráficos:**
   - Distribución de scores por módulo
   - Evolución temporal del score overall
   - Top gaps más críticos

4. **Plan de Mejora:**
   - Lista de gaps con acciones recomendadas
   - Priorización de gaps
   - Seguimiento de mejoras

### **Mock Data:**

```typescript
// app/(app)/governance/data/mockQms.ts
export const mockQmsData = {
  projectId: 1,
  projectName: "AI Credit Scoring System",
  overallScore: 0.82,
  complianceStatus: "COMPLIANT",
  modules: [
    {
      name: "COMPLIANCE_STRATEGY",
      displayName: "Estrategia de Compliance",
      score: 0.90,
      threshold: 0.80,
      gaps: []
    },
    {
      name: "RISK_MANAGEMENT",
      displayName: "Gestión de Riesgos",
      score: 0.85,
      threshold: 0.80,
      gaps: []
    },
    {
      name: "DATA_GOVERNANCE",
      displayName: "Gestión de Datos",
      score: 0.75,
      threshold: 0.80,
      gaps: [
        {
          description: "Faltan métricas de calidad de datos",
          severity: "MEDIUM"
        }
      ]
    },
    {
      name: "TECHNICAL_DOCUMENTATION",
      displayName: "Documentación Técnica",
      score: 0.80,
      threshold: 0.80,
      gaps: []
    }
    // ... 9 módulos más
  ],
  gaps: [
    {
      module: "DATA_GOVERNANCE",
      currentScore: 0.75,
      targetScore: 0.80,
      gap: 0.05,
      description: "Faltan métricas de calidad de datos",
      severity: "MEDIUM",
      recommendedActions: [
        "Implementar dashboard de métricas de calidad",
        "Definir KPIs de calidad de datos"
      ]
    }
  ]
};
```

---

## 📱 PANTALLA 2: Revisión de Gaps QMS (BPMN)

### **Ruta:** `app/(app)/governance/compliance/conformity-review/page.tsx`

**Estado Actual:** ✅ Existe pero incompleta

### **Funcionalidades Requeridas:**

1. **Información del Proyecto:**
   - Datos del proyecto
   - Score QMS overall actual

2. **Revisión de Gaps:**
   - Lista de gaps detectados
   - Descripción de cada gap
   - Acciones recomendadas
   - Priorización

3. **Decisiones:**
   - Aprobar QMS (si no hay gaps críticos)
   - Solicitar correcciones
   - Marcar gaps como aceptables (con justificación)

### **Mock Data:**

```typescript
export const mockQmsGapsReview = {
  projectId: 1,
  projectName: "AI Credit Scoring System",
  overallScore: 0.82,
  gaps: [
    {
      module: "DATA_GOVERNANCE",
      currentScore: 0.75,
      targetScore: 0.80,
      gap: 0.05,
      description: "Faltan métricas de calidad de datos",
      severity: "MEDIUM",
      recommendedActions: [
        "Implementar dashboard de métricas de calidad",
        "Definir KPIs de calidad de datos"
      ]
    }
  ],
  decision: null // PENDING, APPROVED, CORRECTIONS_REQUIRED
};
```

---

## 🎨 ESTILOS "WOW FACTOR"

### **Estructura Base:**

```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import DevelopmentBanner from "@/components/ui/development-banner";
import { ClipboardCheck, TrendingUp } from "lucide-react";

export default function QmsPage() {
  const { t } = useTranslation();

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-indigo-400/30 rounded-full animate-pulse" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        {/* Header */}
        <div className="text-center space-y-2">
          <div className="flex items-center justify-center gap-3 mb-4">
            <ClipboardCheck className="w-8 h-8 text-indigo-500" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-indigo-500 to-indigo-700 bg-clip-text text-transparent">
              {t("compliance.qms.title", "Quality Management System")}
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
qms: {
  title: {
    es: "Sistema de Gestión de Calidad (QMS)",
    en: "Quality Management System (QMS)"
  },
  modules: {
    COMPLIANCE_STRATEGY: {
      es: "Estrategia de Compliance",
      en: "Compliance Strategy"
    },
    RISK_MANAGEMENT: {
      es: "Gestión de Riesgos",
      en: "Risk Management"
    }
    // ... más módulos
  },
  gaps: {
    title: {
      es: "Gaps Detectados",
      en: "Detected Gaps"
    }
  }
}
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### **Pantalla 1: Dashboard (Ya existe - Completar)**
- [ ] Revisar pantalla existente
- [ ] Añadir métricas principales
- [ ] Implementar scores por módulo (13 módulos)
- [ ] Añadir detección de gaps
- [ ] Implementar plan de mejora
- [ ] Añadir gráficos
- [ ] Añadir mock data
- [ ] Crear API routes mock

### **Pantalla 2: Review (Ya existe - Completar)**
- [ ] Revisar pantalla existente
- [ ] Implementar revisión de gaps
- [ ] Añadir decisiones (aprobar/correcciones)
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
- **Prompt Migración:** `docs/prompts/MIGRACION_COMPLIANCE_QMS.md`
- **Inventario Pantallas:** `docs/PANTALLAS_GOVERNANCE_COMPLIANCE_AI_ACT.md`
- **Artículo EU AI Act:** Art. 17

---

**Última actualización:** Diciembre 2025
**Estado:** Listo para implementación en paralelo
