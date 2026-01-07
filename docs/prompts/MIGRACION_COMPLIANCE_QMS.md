# Prompt de Migración - Compliance - QMS (Art. 17)

## Contexto del Módulo

**Módulo:** Compliance
**Tipo de Pantalla:** Revisión de Gaps QMS
**Total de Pantallas:** 1
**Artículo EU AI Act:** Art. 17 - Quality Management System

---

## Arquitectura del Módulo

### ViewModel Identificado

#### ReviewQmsGapsViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.compliance`
- **Archivo:** `com/codeflowx/govern/viewmodel/compliance/ReviewQmsGapsViewModel.java`
- **Servicios Usados:**
  - `ModelService` - Gestión de modelos
  - `TaskService` (Flowable) - Tareas BPMN
  - `QualityManagementSystemBusinessService` - Lógica de negocio QMS
- **Tipo:** User Task BPMN (reviewQmsGaps)

### Entidades JPA Principales

- **QualityManagementSystem** - Sistema de gestión de calidad
- **Project** - Proyecto asociado

### Business Services Disponibles

- **QualityManagementSystemBusinessService** ✅
  - `calculateQmsComplianceScore(Long projectId)` - Calcula score QMS (0.00 - 1.00)
  - `getQmsGaps(Long projectId)` - Obtiene gaps QMS
  - `getComplianceStrategy(Long projectId)` - Obtiene estrategia compliance
  - **13 módulos QMS:**
    1. Risk Management
    2. Data Governance
    3. Technical Documentation
    4. Record-Keeping
    5. Transparency
    6. Human Oversight
    7. Accuracy & Robustness
    8. Cybersecurity
    9. Quality Control
    10. Post-Market Monitoring
    11. Corrective Actions
    12. Conformity Assessment
    13. Continuous Improvement

---

## Pantalla a Migrar

- **Archivo ZUL:** `console/bpmn/review-qms-gaps-form.zul`
- **ViewModel Asociado:** `ReviewQmsGapsViewModel.java`
- **Ruta Next.js:** `/governance/compliance/qms`

---

## Estrategia de Migración

### Dashboard QMS:
- Score QMS overall (0.00 - 1.00)
- Score por módulo (13 módulos)
- Lista de gaps detectados
- Plan de mejora continua
- Decisiones: RETRY o CANCEL

### Mock Data:
```typescript
export const mockQmsData = {
  overallScore: 0.82,
  modules: [
    { name: "Risk Management", score: 0.90, gaps: [] },
    { name: "Data Governance", score: 0.75, gaps: ["Missing data quality metrics"] },
    // ... 11 módulos más
  ],
  gaps: [
    { module: "Data Governance", description: "Missing data quality metrics", severity: "medium" }
  ]
};
```

---

**Última actualización:** Noviembre 2025
**Estado:** Listo para implementación
