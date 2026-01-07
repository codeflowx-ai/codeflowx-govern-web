# 🔍 REVISIÓN DE INTEGRACIÓN BACKEND - MÓDULO CLASSIFICATION

**Fecha:** Enero 2025
**Estado:** ⚠️ Requiere ajustes para alineación completa con backend

---

## 📋 RESUMEN DE REVISIÓN

### ✅ **Aspectos Correctamente Implementados:**

1. **Estructura de Datos (Mock):**
   - ✅ Interfaces TypeScript alineadas con DTOs Java
   - ✅ `ClassificationDetails` mapea correctamente campos de `Project`
   - ✅ `ClassificationRequest` alineado con `ClassificationRequestDto.java`
   - ✅ Códigos de categorías correctos: `III.1` a `III.8` (no `A3_1` a `A3_8`)

2. **Campos de Entidad Project:**
   - ✅ `PRJISHIGHRISK` → `prjishighrisk` (Boolean)
   - ✅ `PRJANNEXIIICATEGORIES` → `prjannexiiicategories` (JSONB)
   - ✅ `PRJCLASSIFICATIONDATE` → `prjclassificationdate` (ISO 8601)
   - ✅ `PRJCLASSIFICATIONAUTHOR` → `prjclassificationauthor` (String)
   - ✅ `PRJPROHIBITEDUSECHECKED` → `prjprohibitedusechecked` (Boolean)
   - ✅ `PRJREGULATEDSECTOR` → `prjregulatedsector` (Boolean)

3. **API Routes Mock:**
   - ✅ Estructura básica correcta
   - ✅ Endpoints definidos según especificación

---

## ⚠️ **INCONSISTENCIAS DETECTADAS:**

### **1. Campo PRJANNEXIIICATEGORY vs PRJANNEXIIICATEGORIES**

**Documento Original (PROMPT_COMPLIANCE_CLASSIFICATION.md):**
```java
- PRJANNEXIIICATEGORY (String) - Código categoría Anexo III (ej: "A3_1", "A3_2")
- PRJANNEXIIISUBCATEGORIES (JSONB) - Subcategorías seleccionadas
```

**Implementación Actual (mockClassification.ts):**
```typescript
prjannexiiicategories?: string; // JSONB - formato: {"category":"III.5","subcategories":["III.5.b","III.5.c"]}
```

**⚠️ PROBLEMA:** El documento menciona dos campos separados, pero la implementación usa un solo campo JSONB que contiene ambos.

**✅ SOLUCIÓN RECOMENDADA:**
- Usar solo `PRJANNEXIIICATEGORIES` (JSONB) como está implementado
- Formato: `{"category":"III.5","subcategories":["III.5.b","III.5.c"]}`
- Actualizar documentación para reflejar esto

---

### **2. Campo PRJCLASSIFICATIONJUSTIFICATION**

**Documento Original:**
```java
- PRJCLASSIFICATIONJUSTIFICATION (String) - Justificación (mínimo 100 caracteres)
```

**Implementación Actual:**
```typescript
justification?: string; // Se almacenará en metadata o se añadirá como campo
```

**⚠️ PROBLEMA:** El campo `justification` no está mapeado explícitamente a `PRJCLASSIFICATIONJUSTIFICATION`.

**✅ SOLUCIÓN:**
- Añadir `prjclassificationjustification?: string;` a `ClassificationDetails`
- Mapear correctamente en el request/response

---

### **3. Códigos de Categorías: A3_X vs III.X**

**Documento Original:**
- Menciona códigos `A3_1` a `A3_8`
- Ejemplos en especificación de endpoints usan `A3_5`

**Implementación Actual:**
- Usa códigos `III.1` a `III.8`
- Subcategorías: `III.5.b`, `III.5.c`, etc.

**✅ DECISIÓN:** La implementación actual (`III.X`) es correcta según el EU AI Act oficial. El documento debe actualizarse.

---

### **4. Estructura de Response del Endpoint `/classify`**

**Especificación del Documento:**
```json
{
  "success": true,
  "message": "Project classified successfully as high-risk system",
  "data": {
    "projectId": 1001,
    "isHighRisk": true,
    "category": "A3_5",
    "categoryName": "...",
    "subcategories": ["A3_5_1", "A3_5_2"],
    "classificationDate": "2025-01-15T10:30:00Z",
    "classifiedBy": "maria.gonzalez",
    "workflowTriggered": true,
    "workflowInstanceId": "wf-instance-12345"
  }
}
```

**Implementación Actual (route.ts):**
```typescript
const result = {
  success: true,
  projectId,
  isHighRisk: true,
  category,
  subcategories,
  justification,
  classificationDate: new Date().toISOString(),
  classifiedBy: "current-user",
  workflowTriggered: true,
};
```

**⚠️ PROBLEMA:** Falta estructura `data` wrapper y algunos campos.

**✅ SOLUCIÓN:**
```typescript
return NextResponse.json({
  success: true,
  message: "Project classified successfully as high-risk system",
  data: {
    projectId,
    isHighRisk: true,
    category,
    categoryName: selectedCategoryData?.anncategoryname,
    subcategories,
    classificationDate: new Date().toISOString(),
    classifiedBy: "current-user", // En producción: obtener del contexto
    workflowTriggered: true,
    workflowInstanceId: workflowInstanceId || undefined,
  }
});
```

---

### **5. Validación de Justificación en Frontend vs Backend**

**Frontend (page.tsx):**
```typescript
const validateJustificationQuality = (justification: string, categoryName: string): string[] => {
  // 1. Mínimo 100 caracteres
  // 2. Debe mencionar la categoría seleccionada
  // 3. Debe contener al menos 2 palabras clave de riesgo
}
```

**Backend (según documento):**
```java
public boolean validateJustificationQuality(String justification) {
  // Misma lógica
}
```

**✅ ESTADO:** Correcto, validación duplicada es aceptable (frontend para UX, backend para seguridad).

---

### **6. Campo PRJCLASSIFIEDBY vs PRJCLASSIFICATIONAUTHOR**

**Documento Original:**
```java
- PRJCLASSIFIEDBY (String) - Usuario que clasificó
```

**Implementación Actual:**
```typescript
prjclassificationauthor?: string; // PRJCLASSIFICATIONAUTHOR
```

**⚠️ PROBLEMA:** Nombre de campo inconsistente.

**✅ SOLUCIÓN:** Verificar en entidad Project cuál es el nombre real del campo y usar ese.

---

## 🔧 **AJUSTES REQUERIDOS:**

### **1. Actualizar API Route `/classify`**

```typescript
// app/api/compliance/classification/classify/route.ts
import { NextResponse } from "next/server";

export async function POST(request: Request) {
  try {
    const body: ClassificationRequest = await request.json();
    const { projectId, category, subcategories, justification, prohibitedUseChecked } = body;

    // Validaciones básicas
    if (!projectId || !category || !subcategories || subcategories.length === 0 || !justification) {
      return NextResponse.json(
        {
          success: false,
          error: {
            code: "VALIDATION_ERROR",
            message: "Missing required fields",
            details: []
          }
        },
        { status: 400 }
      );
    }

    // Validar formato de categoría (III.1 a III.8)
    if (!/^III\.[1-8]$/.test(category)) {
      return NextResponse.json(
        {
          success: false,
          error: {
            code: "VALIDATION_ERROR",
            message: "Invalid category code",
            details: [{ field: "category", message: "Category must be III.1 to III.8" }]
          }
        },
        { status: 400 }
      );
    }

    // Validar justificación (mínimo 100 caracteres)
    if (justification.length < 100) {
      return NextResponse.json(
        {
          success: false,
          error: {
            code: "VALIDATION_ERROR",
            message: "Justification must be at least 100 characters",
            details: [{ field: "justification", message: "Size must be between 100 and 5000" }]
          }
        },
        { status: 400 }
      );
    }

    // Simular procesamiento
    await new Promise((resolve) => setTimeout(resolve, 1000));

    // Obtener nombre de categoría (en producción, desde base de datos)
    const categoryName = "Access to and enjoyment of essential private services..."; // Mock

    // Simular workflow BPMN
    const workflowInstanceId = `wf-instance-${Date.now()}`;

    // Mock: Simular clasificación exitosa
    const result = {
      success: true,
      message: "Project classified successfully as high-risk system",
      data: {
        projectId,
        isHighRisk: true,
        category,
        categoryName,
        subcategories,
        classificationDate: new Date().toISOString(),
        classifiedBy: "current-user", // En producción, obtener del contexto de autenticación
        workflowTriggered: true,
        workflowInstanceId,
      }
    };

    return NextResponse.json(result);
  } catch (error) {
    console.error("Error in classification API:", error);
    return NextResponse.json(
      {
        success: false,
        error: {
          code: "INTERNAL_SERVER_ERROR",
          message: "Internal server error",
          timestamp: new Date().toISOString()
        }
      },
      { status: 500 }
    );
  }
}
```

### **2. Actualizar ClassificationDetails Interface**

```typescript
export interface ClassificationDetails {
  // Campos principales según ProjectDto.java
  prjishighrisk?: boolean;
  prjannexiiicategories?: string; // JSONB: {"category":"III.5","subcategories":["III.5.b"]}
  prjclassificationdate?: string; // ISO 8601
  prjclassificationauthor?: string; // O PRJCLASSIFIEDBY según entidad

  // Añadir campo faltante:
  prjclassificationjustification?: string; // Justificación (CLOB)

  // Anexo I - Sector Regulado
  prjregulatedsector?: boolean;
  prjannexilegislation?: string; // JSONB: ["Reglamento (UE) 2017/745"]

  // Art. 5 - Sistemas Prohibidos
  prjprohibitedusechecked?: boolean;
  prjprohibitedusejustification?: string;

  // Art. 49 - Registro Base Datos UE
  prjeuregistrationid?: string;
  prjeuregistrationdate?: string;
  prjeuregistrationstatus?: string;

  // Campos helper para UI (derivados)
  category?: string;
  subcategories?: string[];
  justification?: string; // Helper: mapea a prjclassificationjustification

  // Campos adicionales para UI
  nextReviewDate?: string;
  confidence?: number;
  internalNotes?: string;
  complianceOfficer?: string;
  workflowInstanceId?: string;
  workflowStatus?: string;
  previousClassifications?: Array<{
    category: string;
    date: string;
    by: string;
    reason: string;
  }>;
}
```

### **3. Actualizar Frontend para Mapear Correctamente**

En `page.tsx`, al enviar el request:

```typescript
const classificationRequest: ClassificationRequest = {
  projectId: data.project.id!,
  category: data.selectedCategory!,
  subcategories: data.selectedSubcategories,
  justification: data.justification || "",
  prohibitedUseChecked: data.prjprohibitedusechecked,
  prohibitedUseJustification: data.prjprohibitedusejustification,
  regulatedSector: data.prjregulatedsector,
  annexILegislation: data.prjannexilegislation ? JSON.parse(data.prjannexilegislation) : undefined,
};
```

Al recibir la respuesta, actualizar `ClassificationDetails`:

```typescript
// Mapear respuesta a ClassificationDetails
setData({
  ...data,
  prjishighrisk: result.data.isHighRisk,
  prjannexiiicategories: createAnnexIIICategoriesJSONB(
    result.data.category,
    result.data.subcategories
  ),
  prjclassificationdate: result.data.classificationDate,
  prjclassificationauthor: result.data.classifiedBy,
  prjclassificationjustification: justification, // Añadir este campo
  workflowInstanceId: result.data.workflowInstanceId,
  workflowStatus: result.data.workflowTriggered ? "active" : undefined,
});
```

---

## 📝 **CHECKLIST DE INTEGRACIÓN:**

- [ ] Verificar nombre exacto del campo en entidad Project: `PRJCLASSIFIEDBY` vs `PRJCLASSIFICATIONAUTHOR`
- [ ] Confirmar si existe campo `PRJCLASSIFICATIONJUSTIFICATION` en Project
- [ ] Actualizar API route `/classify` con estructura de respuesta correcta
- [ ] Añadir `prjclassificationjustification` a `ClassificationDetails`
- [ ] Actualizar mapeo en frontend al recibir respuesta
- [ ] Verificar formato JSONB de `PRJANNEXIIICATEGORIES`
- [ ] Actualizar documentación con códigos correctos (`III.X` en lugar de `A3_X`)
- [ ] Añadir manejo de errores estándar según especificación
- [ ] Implementar autenticación JWT en API routes (cuando backend esté listo)
- [ ] Añadir validación de roles en endpoints

---

## 🔗 **REFERENCIAS:**

- **Documento Principal:** `docs/prompts/compliance/PROMPT_COMPLIANCE_CLASSIFICATION.md`
- **Mock Data:** `app/(app)/governance/data/mockClassification.ts`
- **API Routes:** `app/api/compliance/classification/`
- **Pantalla Principal:** `app/(app)/governance/compliance/classification/page.tsx`

---

**Última actualización:** Enero 2025
**Próximos pasos:** Implementar ajustes y verificar con backend real



