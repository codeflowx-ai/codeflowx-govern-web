# 📄 PROMPT DE IMPLEMENTACIÓN - DOCUMENTACIÓN TÉCNICA (Art. 11 + Anexo IV)

**Módulo:** Compliance - Technical Documentation
**Artículo EU AI Act:** Art. 11 - Documentación Técnica + Anexo IV
**Fecha:** Diciembre 2025
**Estado:** ⏳ Pendiente de implementación completa
**Esfuerzo Estimado:** 3-4 días

---

## 📋 RESUMEN DEL MÓDULO

### **Objetivo**
Implementar el generador y gestor de documentación técnica según el Art. 11 y Anexo IV del EU AI Act. El sistema debe validar completitud de 11 secciones y generar documentación automática.

### **Pantallas Requeridas**

| # | Ruta Next.js | Estado | Descripción | Prioridad |
|---|--------------|-------|-------------|-----------|
| 1 | `app/(app)/governance/compliance/technical-docs/page.tsx` | ✅ Existe | Gestión documentación técnica | 🔴 Alta |
| 2 | `app/(app)/governance/compliance/conformity-review/page.tsx` | ✅ Existe | Completar documentación (BPMN) | 🔴 Alta |

---

## 🏗️ ARQUITECTURA Y DEPENDENCIAS

### **Pantallas ZUL Originales**
- **ZUL Principal:** `console/gobierno/compliance/technical-documentation.zul`
  - ViewModel: `AIActDocumentationGeneratorViewModel`
- **ZUL Review:** `console/bpmn/complete-documentation-form.zul`
  - ViewModel: `CompleteDocumentationViewModel`

### **ViewModels Java**
- **AIActDocumentationGeneratorViewModel** ✅
  - **Paquete:** `com.codeflowx.govern.viewmodel.compliance`
  - **Archivo:** `com/codeflowx/govern/viewmodel/compliance/AIActDocumentationGeneratorViewModel.java`
  - **Servicios Usados:**
    - `@WireVariable ModelService modelService` - Gestión de modelos
  - **Métodos Principales:**
    - `loadModelDocumentation(Long modelId)` - Carga documentación del modelo
    - `validateAnexoIVCompleteness(Long modelId)` - Valida completitud Anexo IV
    - `generateDocumentation(Long modelId)` - Genera documentación automática
    - `calculateDocumentationScore(Long modelId)` - Calcula score de documentación

- **CompleteDocumentationViewModel** ✅
  - **Paquete:** `com.codeflowx.govern.viewmodel.compliance`
  - **Archivo:** `com/codeflowx/govern/viewmodel/compliance/CompleteDocumentationViewModel.java`
  - **Tipo:** User Task BPMN (completeDocumentation)
  - **Servicios Usados:**
    - `@WireVariable ModelService modelService`
    - `@WireVariable TaskService taskService` (Flowable)

### **Entidades JPA**
- **Model** - `com.codeflowx.govern.entity.models.Model`
  - **Tabla:** `MODMODELS` (prefijo `MOD`)
  - **Campos relacionados con documentación:**
    - `MODTECHNICALDOCURL` (String) - URL documentación técnica
    - `MODTECHNICALDOCCOMPLETE` (Boolean) - Si Anexo IV completo
    - `MODTECHNICALDOCSCORE` (BigDecimal) - Score completitud (0.00 - 1.00)
    - `MODTECHNICALDOCSECTIONS` (JSONB) - Estado de secciones Anexo IV

- **TechnicalDocumentation** - `com.codeflowx.govern.entity.compliance.TechnicalDocumentation`
  - **Tabla:** `GOVAIAACTTECHNICALDOCS` (prefijo `GOV`)
  - **Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/TechnicalDocumentation.java`
  - **Campos principales:**
    - `IDXTECHNICALDOC` (Long, PK) - ID autonumérico
    - `IDXMODEL` (Long, FK) - Referencia a modelo
    - `TECHSECTIONS` (JSONB) - Secciones Anexo IV con contenido
    - `TECHCOMPLETENESSSCORE` (BigDecimal) - Score completitud
    - `TECHISCOMPLETE` (Boolean) - Si está completo
    - `TECHPDFURL` (String) - URL del PDF generado
    - `TECHCREATEDAT` (Timestamp) - Fecha creación
    - `IDUUID` (String) - UUID único

### **Business Services Disponibles**
- **TechnicalDocumentationBusinessService** ✅
  - **Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/TechnicalDocumentationBusinessService.java`
  - **Métodos principales:**
    - `calculateDocumentationScore(Long modelId)` - Calcula score documentación
      - Valida completitud de 11 secciones Anexo IV
      - Score = (secciones completas / 11) * 100
      - Retorna: `BigDecimal` (0.00 - 1.00)
    - `validateAnexoIVCompleteness(Long modelId)` - Valida completitud Anexo IV
      - Verifica que las 11 secciones estén completas
      - Retorna: `boolean`
    - `generateDocumentation(Long modelId)` - Genera documentación automática
      - Genera contenido para secciones desde datos del modelo
      - Guarda en `TechnicalDocumentation`
    - `generatePdf(Long modelId)` - Genera PDF de documentación
      - Genera PDF desde secciones completas
      - Guarda URL en `TechnicalDocumentation.techpdfurl`

### **11 Secciones Anexo IV:**
1. **General description** - Descripción general del sistema
2. **System architecture** - Arquitectura del sistema
3. **Data governance** - Gobernanza de datos
4. **Risk management** - Gestión de riesgos
5. **Human oversight** - Supervisión humana
6. **Accuracy & robustness** - Precisión y robustez
7. **Cybersecurity** - Ciberseguridad
8. **Quality control** - Control de calidad
9. **Post-market monitoring** - Monitoreo post-mercado
10. **Conformity assessment** - Evaluación de conformidad
11. **Record-keeping** - Mantenimiento de registros

### **Servicios CRUD**
- **TechnicalDocumentationService** ✅
  - **Ubicación:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/compliance/TechnicalDocumentationService.java`
  - **Métodos:** `findAll()`, `findById()`, `save()`, `delete()`

### **Lógica de Negocio Detallada**

#### **Cálculo de Score de Documentación:**
```java
// Implementado en TechnicalDocumentationBusinessService.calculateDocumentationScore()
public BigDecimal calculateDocumentationScore(Long modelId) {
    TechnicalDocumentation doc = getTechnicalDocumentation(modelId);
    Map<String, Boolean> sections = parseSections(doc.getTechsections());

    int completedSections = 0;
    for (Boolean complete : sections.values()) {
        if (complete) {
            completedSections++;
        }
    }

    BigDecimal score = new BigDecimal(completedSections)
        .divide(new BigDecimal(11), 4, RoundingMode.HALF_UP);

    // Actualizar modelo
    Model model = modelService.findById(modelId);
    model.setModtechnicaldocscore(score);
    model.setModtechnicaldoccomplete(score.compareTo(new BigDecimal("1.00")) == 0);
    modelService.save(model);

    // Actualizar documentación
    doc.setTechcompletenessscore(score);
    doc.setTechiscomplete(score.compareTo(new BigDecimal("1.00")) == 0);
    save(doc);

    return score;
}
```

#### **Generación Automática de Documentación:**
```java
// Implementado en TechnicalDocumentationBusinessService.generateDocumentation()
public TechnicalDocumentation generateDocumentation(Long modelId) {
    Model model = modelService.findById(modelId);
    TechnicalDocumentation doc = getOrCreateTechnicalDocumentation(modelId);

    Map<String, String> sections = new HashMap<>();

    // Generar contenido para cada sección desde datos del modelo
    sections.put("GENERAL_DESCRIPTION", generateGeneralDescription(model));
    sections.put("SYSTEM_ARCHITECTURE", generateSystemArchitecture(model));
    sections.put("DATA_GOVERNANCE", generateDataGovernance(model));
    sections.put("RISK_MANAGEMENT", generateRiskManagement(model));
    sections.put("HUMAN_OVERSIGHT", generateHumanOversight(model));
    sections.put("ACCURACY_ROBUSTNESS", generateAccuracyRobustness(model));
    sections.put("CYBERSECURITY", generateCybersecurity(model));
    sections.put("QUALITY_CONTROL", generateQualityControl(model));
    sections.put("POST_MARKET_MONITORING", generatePostMarketMonitoring(model));
    sections.put("CONFORMITY_ASSESSMENT", generateConformityAssessment(model));
    sections.put("RECORD_KEEPING", generateRecordKeeping(model));

    doc.setTechsections(sections.toJson());
    return save(doc);
}
```

---

## 📱 PANTALLA 1: Gestión Documentación Técnica

### **Ruta:** `app/(app)/governance/compliance/technical-docs/page.tsx`

**Estado Actual:** ✅ Existe pero incompleta

### **Funcionalidades Requeridas:**

1. **Listado de Modelos:**
   - Tabla con modelos y su estado de documentación
   - Columnas: Modelo, Score, Completitud, Acciones
   - Filtros: por modelo, score, completitud

2. **Gestión de Documentación:**
   - Ver documentación del modelo
   - Editar secciones individuales
   - Generar documentación automática
   - Validar completitud
   - Generar PDF

3. **Visualización de Secciones:**
   - Lista de 11 secciones Anexo IV
   - Indicador de completitud por sección
   - Editor de contenido por sección

### **Mock Data:**

```typescript
// app/(app)/governance/data/mockTechnicalDocs.ts
export const mockTechnicalDocs = {
  modelId: 123,
  modelName: "Credit Scoring Model v1.0",
  overallScore: 0.82,
  isComplete: false,
  sections: [
    {
      id: 1,
      name: "GENERAL_DESCRIPTION",
      displayName: "General Description",
      complete: true,
      score: 1.0,
      content: "The AI system is a credit scoring model..."
    },
    {
      id: 2,
      name: "SYSTEM_ARCHITECTURE",
      displayName: "System Architecture",
      complete: true,
      score: 0.95,
      content: "The system architecture consists of..."
    },
    {
      id: 3,
      name: "DATA_GOVERNANCE",
      displayName: "Data Governance",
      complete: false,
      score: 0.60,
      content: "Data governance procedures include..."
    }
    // ... 8 secciones más
  ],
  pdfUrl: null
};
```

---

## 📱 PANTALLA 2: Completar Documentación (BPMN)

### **Ruta:** `app/(app)/governance/compliance/conformity-review/page.tsx`

**Estado Actual:** ✅ Existe pero incompleta

### **Funcionalidades Requeridas:**

1. **Información del Modelo:**
   - Datos del modelo
   - Score actual de documentación

2. **Completar Secciones Faltantes:**
   - Lista de secciones incompletas
   - Editor para cada sección
   - Validación de contenido

3. **Acciones:**
   - Guardar cambios
   - Validar completitud
   - Generar PDF
   - Marcar como completo

### **Mock Data:**

```typescript
export const mockCompleteDocumentation = {
  modelId: 123,
  modelName: "Credit Scoring Model v1.0",
  currentScore: 0.82,
  incompleteSections: [
    {
      name: "DATA_GOVERNANCE",
      displayName: "Data Governance",
      currentContent: "Data governance procedures include...",
      required: true
    }
  ],
  canComplete: false // Requiere todas las secciones completas
};
```

---

## 🎨 ESTILOS "WOW FACTOR"

### **Estructura Base:**

```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import DevelopmentBanner from "@/components/ui/development-banner";
import { FileText, BookOpen } from "lucide-react";

export default function TechnicalDocsPage() {
  const { t } = useTranslation();

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-teal-400/30 rounded-full animate-pulse" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        {/* Header */}
        <div className="text-center space-y-2">
          <div className="flex items-center justify-center gap-3 mb-4">
            <FileText className="w-8 h-8 text-teal-500" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-teal-500 to-teal-700 bg-clip-text text-transparent">
              {t("compliance.technicalDocs.title", "Technical Documentation")}
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
technicalDocs: {
  title: {
    es: "Documentación Técnica",
    en: "Technical Documentation"
  },
  sections: {
    GENERAL_DESCRIPTION: {
      es: "Descripción General",
      en: "General Description"
    },
    SYSTEM_ARCHITECTURE: {
      es: "Arquitectura del Sistema",
      en: "System Architecture"
    }
    // ... más secciones
  }
}
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### **Pantalla 1: Gestión (Ya existe - Completar)**
- [ ] Revisar pantalla existente
- [ ] Implementar listado de modelos
- [ ] Añadir gestión de secciones (11 secciones)
- [ ] Implementar generación automática
- [ ] Añadir validación de completitud
- [ ] Implementar generación de PDF
- [ ] Añadir mock data
- [ ] Crear API routes mock

### **Pantalla 2: Completar (Ya existe - Completar)**
- [ ] Revisar pantalla existente
- [ ] Implementar editor de secciones
- [ ] Añadir validación de contenido
- [ ] Implementar generación de PDF
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
- **Prompt Migración:** `docs/prompts/MIGRACION_COMPLIANCE_TECHNICAL_DOCS.md`
- **Inventario Pantallas:** `docs/PANTALLAS_GOVERNANCE_COMPLIANCE_AI_ACT.md`
- **Artículo EU AI Act:** Art. 11 + Anexo IV

---

**Última actualización:** Diciembre 2025
**Estado:** Listo para implementación en paralelo
