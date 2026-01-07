# Prompt de Migración - Compliance - Classification (Art. 6)

## Contexto del Módulo

**Módulo:** Compliance
**Tipo de Pantalla:** Formulario de Clasificación
**Total de Pantallas:** 1
**Artículo EU AI Act:** Art. 6 - Clasificación de Sistemas de Alto Riesgo

---

## Documentación Funcional Asociada

**Documento:** `docs/funcional/compliance/01_REORGANIZACION_PANTALLAS_COMPLIANCE.md`

**Ubicación:** `suinsit.nova.web/docs/funcional/compliance/01_REORGANIZACION_PANTALLAS_COMPLIANCE.md`

### Resumen de la Documentación

Esta pantalla permite clasificar proyectos de IA en sistemas de alto riesgo según el **Anexo III del EU AI Act (Art. 6)**. Incluye:
- 8 categorías principales del Anexo III
- 25+ subcategorías específicas
- Sugerencia automática con IA
- Justificación obligatoria de clasificación
- Actualización automática de campo `PRJISHIGHRISK` en Project
- Trigger de workflow BPMN si es alto riesgo

---

## Arquitectura del Módulo

### ViewModel Identificado

#### HighRiskClassifierViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.compliance`
- **Archivo:** `com/codeflowx/govern/viewmodel/compliance/HighRiskClassifierViewModel.java`
- **Líneas:** ~994 líneas
- **Servicios Usados:**
  - `ModelService` - Gestión de modelos
  - `ProjectService` - Gestión de proyectos
  - `RuntimeService` (Flowable) - Workflows BPMN
- **Entidades Usadas:**
  - `Project` - Proyecto a clasificar
  - `AnnexIIICategory` - Categorías Anexo III
- **Mock Mode:** ❌ No (usa servicios reales)
- **Funcionalidades Principales:**
  - `loadAnnexIIICategories()` - Carga 8 categorías principales
  - `loadSubcategories(String categoryCode)` - Carga subcategorías
  - `suggestCategoryWithAI()` - Sugerencia automática con IA
  - `classifyProject()` - Clasifica proyecto y actualiza `PRJISHIGHRISK`
  - `triggerHighRiskWorkflow()` - Dispara workflow BPMN si es alto riesgo

### Entidades JPA Principales

- **Project** - `com.codeflowx.govern.entity.projects.Project`
  - Campo: `PRJISHIGHRISK` (Boolean) - Indica si es sistema de alto riesgo
  - Campo: `PRJANNEXIIICATEGORY` (String) - Código categoría Anexo III
  - Campo: `PRJANNEXIIISUBCATEGORIES` (JSONB) - Subcategorías seleccionadas
  - Campo: `PRJCLASSIFICATIONJUSTIFICATION` (String) - Justificación

- **AnnexIIICategory** - `com.codeflowx.govern.entity.compliance.AnnexIIICategory`
  - Catálogo de 8 categorías principales y 25+ subcategorías

### Business Services Disponibles

- **AnnexIIICategoryBusinessService** (si existe)
  - `getMainCategories()` - Obtiene 8 categorías principales
  - `getSubcategories(String categoryCode)` - Obtiene subcategorías
  - `suggestCategories(String projectDescription)` - Sugerencia automática

### Microservicios Java Disponibles

Los ViewModels pueden usar los siguientes clientes Java para llamar a microservicios:

1. **AIGovernanceClient** (Factoría)
   - Gateway: `http://api-leka-govern:8000` (K8s) o `http://localhost:8000` (local)
   - Proporciona acceso a múltiples clientes especializados

**Ubicación de Clientes:**
- `nocode.service/codeflowx.govern.nocode.client/src/main/java/com/codeflowx/governance/client/`

---

## ⚠️ IMPORTANTE: Revisar Pantallas Existentes

**ANTES de migrar, verificar si la pantalla ya existe en Next.js:**

### 1. Páginas Existentes en `app/(app)/`

**Ruta esperada:** `app/(app)/governance/compliance/classification/page.tsx`

**Estado:** ❌ **NO EXISTE** - Crear nueva página

### 2. Entradas en el Menú (`app/config/modules.ts`)

**Entrada existente:** ✅ **SÍ EXISTE**
- Nombre: "Classification (Art. 6)"
- Ruta: `/governance/compliance/classification`
- Icono: `Tag`
- Roles: `["admin", "project_manager", "agencia", "it", "oem", "business_admin"]`

### 3. Acción a Realizar

**Crear nueva página:**
1. ✅ **Crear nueva página** en `app/(app)/governance/compliance/classification/page.tsx`
2. ✅ **Implementar con estilos "Wow Factor"** (ver sección de estilos)
3. ✅ **Verificar entrada en menú** en `app/config/modules.ts` (ya existe)
4. ✅ **Agregar traducciones** en `app/config/i18n.ts` si faltan campos

---

## Pantalla a Migrar

### 1. high-risk-classifier
- **Archivo ZUL:** `console/gobierno/compliance/high-risk-classifier.zul`
- **ViewModel Asociado:** `HighRiskClassifierViewModel.java`
- **Servicio Backend:** `ProjectService`, `ModelService`
- **Entidad JPA:** `Project`, `AnnexIIICategory`
- **Tipo:** Formulario de clasificación
- **Ruta Next.js:** `/governance/compliance/classification`

---

## Estrategia de Migración

### 1. Identificación de Componentes

**ViewModel Java:** `HighRiskClassifierViewModel.java`
- Ubicación: `com.codeflowx.govern.viewmodel.compliance.HighRiskClassifierViewModel`
- Servicios inyectados:
  - `@WireVariable ModelService modelService`
  - `@WireVariable ProjectService projectService`
  - `@WireVariable RuntimeService runtimeService` (Flowable BPMN)

**Entidades JPA:**
- `Project` - Proyecto a clasificar
- `AnnexIIICategory` - Categorías Anexo III

**Mock Mode:** No tiene modo mock, usar datos mock en Next.js

### 2. Patrón de Migración

#### Para Formulario de Clasificación:
- Formulario multi-step:
  1. Información del proyecto (solo lectura)
  2. Sugerencia automática con IA (opcional)
  3. Selección de categoría principal (8 opciones)
  4. Selección de subcategorías (multi-select)
  5. Justificación obligatoria (textarea)
  6. Confirmación y clasificación
- Validaciones:
  - Categoría principal obligatoria
  - Al menos una subcategoría obligatoria
  - Justificación obligatoria (mínimo 50 caracteres)
- Mock data para:
  - Lista de categorías Anexo III (8 categorías)
  - Lista de subcategorías por categoría
  - Sugerencia automática con IA (simulada)
- API routes mock: `/api/compliance/classification/classify`

### 3. Configuración de Mock

**Objetivo:** Que cuando se ponga operativa solo haya que desactivar el mock sin revisar cada pantalla

#### Estrategia de Mock Centralizado

**1. Variable de Entorno:**
```bash
# .env.local
NEXT_PUBLIC_USE_MOCK=true
```

**2. Configuración Centralizada en Next.js:**
```typescript
// app/(app)/governance/compliance/classification/page.tsx
import { governanceService } from '@/app/(app)/governance/services/governanceService';

// Usar servicio con detección automática de modo demo
const categories = await governanceService.getAnnexIIICategories();
```

**3. Mock Data:**
```typescript
// app/(app)/governance/data/mockData.ts
export const mockAnnexIIICategories = [
  {
    code: "A3_1",
    name: "Biometric identification and categorisation systems",
    description: "AI systems intended to be used for biometric identification...",
    subcategories: [
      { code: "A3_1_1", name: "Real-time remote biometric identification" },
      { code: "A3_1_2", name: "Post-remote biometric identification" },
      // ... más subcategorías
    ]
  },
  // ... 7 categorías más
];
```

---

## Estructura de la Página Next.js

### Ruta: `app/(app)/governance/compliance/classification/page.tsx`

```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Tag } from "lucide-react";
import { useState } from "react";

export default function ClassificationPage() {
  const { t } = useTranslation();
  const [selectedCategory, setSelectedCategory] = useState<string>("");
  const [selectedSubcategories, setSelectedSubcategories] = useState<string[]>([]);
  const [justification, setJustification] = useState<string>("");
  const [aiSuggestion, setAiSuggestion] = useState<string | null>(null);

  // Mock data
  const categories = mockAnnexIIICategories;
  const currentProject = {
    id: 1,
    name: "AI Credit Scoring System",
    description: "Sistema de scoring crediticio basado en IA"
  };

  const handleClassify = async () => {
    // TODO: Implementar llamada a API
    console.log("Clasificando proyecto:", {
      projectId: currentProject.id,
      category: selectedCategory,
      subcategories: selectedSubcategories,
      justification
    });
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        {/* Header */}
        <div className="text-center space-y-2">
          <div className="flex items-center justify-center gap-3 mb-4">
            <Tag className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("compliance.classification.title", "High-Risk AI Systems Classifier")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t("compliance.classification.subtitle", "Clasificador de sistemas de IA de alto riesgo según EU AI Act Anexo III (Art. 6)")}
          </p>
        </div>

        {/* Información del Proyecto */}
        <div className="card backdrop-blur-md bg-background/60 border-border/50">
          <div className="card-header">
            <h3 className="text-lg font-semibold">Project Information</h3>
          </div>
          <div className="card-body">
            <div className="grid grid-cols-2 gap-4">
              <div>
                <p className="text-sm text-muted-foreground">Project</p>
                <p className="font-medium">{currentProject.name}</p>
              </div>
              <div>
                <p className="text-sm text-muted-foreground">ID</p>
                <p className="font-medium">{currentProject.id}</p>
              </div>
              <div className="col-span-2">
                <p className="text-sm text-muted-foreground">Description</p>
                <p className="text-sm">{currentProject.description}</p>
              </div>
            </div>
          </div>
        </div>

        {/* Sugerencia Automática IA */}
        {aiSuggestion && (
          <div className="card backdrop-blur-md bg-background/60 border-border/50 border-blue-500/50">
            <div className="card-header">
              <h3 className="text-lg font-semibold">AI Suggestion</h3>
            </div>
            <div className="card-body">
              <p className="text-sm">{aiSuggestion}</p>
              <button onClick={() => setSelectedCategory(aiSuggestion)}>
                Use Suggestion
              </button>
            </div>
          </div>
        )}

        {/* Selección de Categoría */}
        <div className="card backdrop-blur-md bg-background/60 border-border/50">
          <div className="card-header">
            <h3 className="text-lg font-semibold">Select Main Category (Annex III)</h3>
          </div>
          <div className="card-body">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {categories.map((category) => (
                <div
                  key={category.code}
                  className={`p-4 border rounded-lg cursor-pointer transition-all ${
                    selectedCategory === category.code
                      ? "border-primary bg-primary/10"
                      : "border-border hover:border-primary/50"
                  }`}
                  onClick={() => setSelectedCategory(category.code)}
                >
                  <h4 className="font-semibold">{category.name}</h4>
                  <p className="text-sm text-muted-foreground">{category.description}</p>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Selección de Subcategorías */}
        {selectedCategory && (
          <div className="card backdrop-blur-md bg-background/60 border-border/50">
            <div className="card-header">
              <h3 className="text-lg font-semibold">Select Subcategories</h3>
            </div>
            <div className="card-body">
              <div className="space-y-2">
                {categories
                  .find((c) => c.code === selectedCategory)
                  ?.subcategories.map((subcat) => (
                    <label key={subcat.code} className="flex items-center space-x-2">
                      <input
                        type="checkbox"
                        checked={selectedSubcategories.includes(subcat.code)}
                        onChange={(e) => {
                          if (e.target.checked) {
                            setSelectedSubcategories([...selectedSubcategories, subcat.code]);
                          } else {
                            setSelectedSubcategories(
                              selectedSubcategories.filter((c) => c !== subcat.code)
                            );
                          }
                        }}
                      />
                      <span>{subcat.name}</span>
                    </label>
                  ))}
              </div>
            </div>
          </div>
        )}

        {/* Justificación */}
        <div className="card backdrop-blur-md bg-background/60 border-border/50">
          <div className="card-header">
            <h3 className="text-lg font-semibold">Justification (Required)</h3>
          </div>
          <div className="card-body">
            <textarea
              className="w-full min-h-[150px] p-3 border rounded-lg"
              placeholder="Explain why this system is classified as high-risk..."
              value={justification}
              onChange={(e) => setJustification(e.target.value)}
              required
              minLength={50}
            />
            <p className="text-sm text-muted-foreground mt-2">
              Minimum 50 characters required
            </p>
          </div>
        </div>

        {/* Botón de Clasificación */}
        <div className="flex justify-end">
          <button
            className="px-6 py-2 bg-primary text-primary-foreground rounded-lg hover:bg-primary/90 transition-all"
            onClick={handleClassify}
            disabled={!selectedCategory || selectedSubcategories.length === 0 || justification.length < 50}
          >
            Classify as High-Risk System
          </button>
        </div>
      </div>
    </div>
  );
}
```

---

## Traducciones Requeridas

Añadir en `app/config/i18n.ts`:

```typescript
compliance: {
  classification: {
    title: {
      es: "Clasificador de Sistemas de IA de Alto Riesgo",
      en: "High-Risk AI Systems Classifier"
    },
    subtitle: {
      es: "Clasificador de sistemas de IA de alto riesgo según EU AI Act Anexo III (Art. 6)",
      en: "High-risk AI systems classifier according to EU AI Act Annex III (Art. 6)"
    },
    // ... más traducciones
  }
}
```

---

## Checklist de Implementación

- [ ] Crear página `app/(app)/governance/compliance/classification/page.tsx`
- [ ] Implementar formulario multi-step
- [ ] Añadir mock data para categorías Anexo III
- [ ] Implementar validaciones (categoría, subcategorías, justificación)
- [ ] Añadir sugerencia automática con IA (mock)
- [ ] Implementar estilos "Wow Factor"
- [ ] Añadir traducciones en `app/config/i18n.ts`
- [ ] Verificar entrada en menú (ya existe)
- [ ] Probar funcionalidad completa
- [ ] Documentar API endpoints para integración futura

---

**Última actualización:** Noviembre 2025
**Estado:** Listo para implementación
