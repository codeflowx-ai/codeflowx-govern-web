# 📋 INTEGRACIÓN FRONTEND - MÓDULOS QMS

**Fecha:** Diciembre 2025
**Estado:** ✅ Backend implementado - Pendiente integración frontend

---

## 🎯 RESUMEN

Se han implementado **5 módulos QMS principales** con entidades JPA, repositorios y lógica de negocio completa:

1. **Módulo A: Compliance Strategy** - Estrategia de cumplimiento normativo
2. **Módulo B: Design Control** - Control y verificación de diseño
3. **Módulo C: Quality Assurance** - Aseguramiento de calidad
4. **Módulo D: Test Validation** - Validación y pruebas
5. **Módulo E: Technical Standards** - Normas técnicas aplicadas

---

## 🔧 BACKEND IMPLEMENTADO

### **Entidades JPA Creadas:**

1. `QmsComplianceStrategy` - Tabla: `GOVQMSCOMPLIANCESTRATEGY`
2. `QmsDesignReview` - Tabla: `GOVQMSDESIGNREVIEWS`
3. `QmsQualityMetrics` - Tabla: `GOVQMSQUALITYMETRICS`
4. `QmsTestExecution` - Tabla: `GOVQMSTESTEXECUTIONS`
5. `QmsProjectStandard` - Tabla: `GOVQMSPROJECTSTANDARDS`

### **Repositorios JPA Creados:**

- `QmsComplianceStrategyRepository`
- `QmsDesignReviewRepository`
- `QmsQualityMetricsRepository`
- `QmsTestExecutionRepository`
- `QmsProjectStandardRepository`

### **Métodos del Business Service Implementados:**

#### **Módulo A: Compliance Strategy**
- ✅ `getComplianceStrategy(projectId)` - Obtiene estrategia desde BD
- ✅ `updateComplianceStrategy(projectId, strategy)` - Guarda/actualiza estrategia

#### **Módulo B: Design Control**
- ✅ `getDesignControl(projectId)` - Calcula score desde revisiones almacenadas
- ✅ `registerDesignReview(projectId, reviewType, outcome)` - Registra revisión

#### **Módulo C: Quality Assurance**
- ✅ `getQualityAssurance(projectId)` - Obtiene métricas desde BD
- ✅ `updateQualityMetrics(projectId, metrics)` - Actualiza métricas

#### **Módulo D: Test Validation**
- ✅ `getTestValidation(projectId)` - Calcula score desde ejecuciones
- ✅ `getTestHistory(projectId)` - Obtiene historial de tests
- ✅ `registerTestExecution(projectId, testName, result, coverage)` - Registra test

#### **Módulo E: Technical Standards**
- ✅ `getAppliedStandards(projectId)` - Obtiene normas aplicadas
- ✅ `addStandard(projectId, standardId, name, version)` - Añade norma
- ✅ `verifyStandardCompliance(projectId, standardId)` - Verifica compliance

---

## 📱 CÓMO INCORPORAR AL FRONTEND

### **Opción 1: Página de Detalle por Módulo**

Crear páginas individuales para cada módulo donde el usuario pueda:
- Ver información del módulo
- Editar/actualizar datos
- Ver historial

**Estructura sugerida:**
```
/governance/compliance/qms/[projectId]/modules
  - /compliance-strategy
  - /design-control
  - /quality-assurance
  - /test-validation
  - /technical-standards
```

### **Opción 2: Modal/Dialog por Módulo**

Desde el dashboard QMS, al hacer clic en un módulo, abrir un modal con:
- Información del módulo
- Formulario para editar
- Historial de cambios

### **Opción 3: Pestañas en Dashboard QMS**

Añadir pestañas en `/governance/compliance/qms?projectId=X`:
- Tab "Overview" (actual)
- Tab "Compliance Strategy"
- Tab "Design Control"
- Tab "Quality Assurance"
- Tab "Test Validation"
- Tab "Technical Standards"

---

## 🔌 ENDPOINTS A CREAR EN EL CONTROLLER

### **1. Compliance Strategy**

```java
GET /api/v1/qms/compliance-strategy?projectId={id}
POST /api/v1/qms/compliance-strategy
PUT /api/v1/qms/compliance-strategy/{id}
```

**DTOs necesarios:**
- `QmsComplianceStrategyDto` (Request/Response)

### **2. Design Control**

```java
GET /api/v1/qms/design-control?projectId={id}
POST /api/v1/qms/design-reviews
GET /api/v1/qms/design-reviews?projectId={id}
```

**DTOs necesarios:**
- `QmsDesignControlDto`
- `QmsDesignReviewDto` (Request/Response)

### **3. Quality Assurance**

```java
GET /api/v1/qms/quality-assurance?projectId={id}
POST /api/v1/qms/quality-metrics
GET /api/v1/qms/quality-metrics?projectId={id}
```

**DTOs necesarios:**
- `QmsQualityAssuranceDto`
- `QmsQualityMetricsDto` (Request/Response)

### **4. Test Validation**

```java
GET /api/v1/qms/test-validation?projectId={id}
POST /api/v1/qms/test-executions
GET /api/v1/qms/test-executions?projectId={id}
GET /api/v1/qms/test-history?projectId={id}
```

**DTOs necesarios:**
- `QmsTestValidationDto`
- `QmsTestExecutionDto` (Request/Response)

### **5. Technical Standards**

```java
GET /api/v1/qms/standards?projectId={id}
POST /api/v1/qms/standards
DELETE /api/v1/qms/standards/{id}
GET /api/v1/qms/standards/{standardId}/compliance?projectId={id}
```

**DTOs necesarios:**
- `QmsProjectStandardDto` (Request/Response)

---

## 📝 EJEMPLO DE IMPLEMENTACIÓN FRONTEND

### **Página de Compliance Strategy**

```typescript
// app/(app)/governance/compliance/qms/[projectId]/modules/compliance-strategy/page.tsx

"use client";

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";

interface ComplianceStrategy {
  projectId: number;
  strategyDefined: boolean;
  strategyDescription: string;
  applicableRegulations: string[];
  complianceScore: number;
}

export default function ComplianceStrategyPage() {
  const params = useParams();
  const projectId = Number(params.projectId);
  const [strategy, setStrategy] = useState<ComplianceStrategy | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadStrategy();
  }, [projectId]);

  const loadStrategy = async () => {
    try {
      const response = await fetch(
        `/api/governance/compliance/qms/compliance-strategy?projectId=${projectId}`
      );
      const result = await response.json();
      if (result.success) {
        setStrategy(result.data);
      }
    } catch (error) {
      console.error("Error loading strategy:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSave = async (updatedStrategy: ComplianceStrategy) => {
    try {
      const response = await fetch(
        `/api/governance/compliance/qms/compliance-strategy`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            projectId,
            ...updatedStrategy,
          }),
        }
      );
      const result = await response.json();
      if (result.success) {
        setStrategy(result.data);
        // Opcional: Recalcular score QMS
        await fetch(`/api/governance/compliance/qms/calculate`, {
          method: "POST",
          body: JSON.stringify({ projectId }),
        });
      }
    } catch (error) {
      console.error("Error saving strategy:", error);
    }
  };

  // ... UI del formulario
}
```

### **API Route para Compliance Strategy**

```typescript
// app/api/governance/compliance/qms/compliance-strategy/route.ts

import { NextRequest, NextResponse } from "next/server";

export async function GET(request: NextRequest) {
  const projectId = request.nextUrl.searchParams.get("projectId");

  // Llamar al BFF
  const response = await fetch(
    `${process.env.BFF_BASE_URL}/api/v1/qms/compliance-strategy?projectId=${projectId}`
  );

  const data = await response.json();
  return NextResponse.json({ success: true, data });
}

export async function POST(request: NextRequest) {
  const body = await request.json();

  // Llamar al BFF
  const response = await fetch(
    `${process.env.BFF_BASE_URL}/api/v1/qms/compliance-strategy`,
    {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body),
    }
  );

  const data = await response.json();
  return NextResponse.json({ success: true, data });
}
```

---

## 🎨 COMPONENTES UI SUGERIDOS

### **1. Card de Módulo con Score**

```typescript
<Card>
  <CardHeader>
    <CardTitle>Compliance Strategy</CardTitle>
    <Badge>{score}%</Badge>
  </CardHeader>
  <CardBody>
    <p>Strategy Defined: {strategyDefined ? "Yes" : "No"}</p>
    <Button onClick={() => router.push(`/qms/${projectId}/modules/compliance-strategy`)}>
      Manage
    </Button>
  </CardBody>
</Card>
```

### **2. Formulario de Estrategia**

```typescript
<form onSubmit={handleSubmit}>
  <Input
    label="Strategy Description"
    value={strategyDescription}
    onChange={(e) => setStrategyDescription(e.target.value)}
  />
  <Select
    label="Applicable Regulations"
    multiple
    value={regulations}
    options={["EU AI Act", "GDPR", "ISO/IEC 42001"]}
  />
  <Button type="submit">Save Strategy</Button>
</form>
```

### **3. Lista de Design Reviews**

```typescript
{reviews.map((review) => (
  <Card key={review.id}>
    <CardBody>
      <p>Type: {review.type}</p>
      <p>Outcome: {review.outcome}</p>
      <p>Date: {formatDate(review.date)}</p>
    </CardBody>
  </Card>
))}
<Button onClick={() => openAddReviewDialog()}>
  Add Review
</Button>
```

---

## 📊 FLUJO DE DATOS

```
Frontend (Next.js)
    ↓
API Route (/api/governance/compliance/qms/...)
    ↓
BFF (Spring WebFlux)
    ↓
Microservicio QMS
    ↓
Business Service
    ↓
Repository JPA
    ↓
Base de Datos
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN FRONTEND

### **Fase 1: Endpoints Backend**
- [ ] Crear DTOs para cada módulo
- [ ] Añadir endpoints en `QmsController` (microservicio)
- [ ] Añadir métodos en `QmsService` (BFF)
- [ ] Añadir endpoints en `QmsController` (BFF)

### **Fase 2: API Routes Frontend**
- [ ] Crear `/api/governance/compliance/qms/compliance-strategy/route.ts`
- [ ] Crear `/api/governance/compliance/qms/design-control/route.ts`
- [ ] Crear `/api/governance/compliance/qms/quality-assurance/route.ts`
- [ ] Crear `/api/governance/compliance/qms/test-validation/route.ts`
- [ ] Crear `/api/governance/compliance/qms/standards/route.ts`

### **Fase 3: Páginas Frontend**
- [ ] Crear página de Compliance Strategy
- [ ] Crear página de Design Control
- [ ] Crear página de Quality Assurance
- [ ] Crear página de Test Validation
- [ ] Crear página de Technical Standards

### **Fase 4: Integración en Dashboard**
- [ ] Añadir enlaces a módulos desde dashboard QMS
- [ ] Mostrar scores actualizados en tiempo real
- [ ] Añadir indicadores visuales de completitud

---

## 🔗 NAVEGACIÓN SUGERIDA

Desde la página de proyectos QMS:
1. Click en "Ver Dashboard" → `/governance/compliance/qms?projectId={id}`
2. En el dashboard, cada módulo tiene un botón "Gestionar"
3. Click en "Gestionar" → `/governance/compliance/qms/[projectId]/modules/[module-name]`

---

## 📚 REFERENCIAS

- **Backend:** `QualityManagementSystemBusinessService.java`
- **Repositorios:** `codeflowx.govern.repository.compliance.*`
- **Entidades:** `codeflowx.govern.entity.compliance.Qms*`
- **Controller:** `QmsController.java` (microservicio y BFF)

---

**Última actualización:** Diciembre 2025
**Estado:** Backend 100% implementado - Frontend pendiente
