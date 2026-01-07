# 👨‍💻 GUÍA PARA DEVELOPERS FRONTEND - HITL SUPERVISION

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Desarrolladores Frontend (Next.js/React)

---

## 📋 ÍNDICE

1. [Arquitectura General](#arquitectura-general)
2. [Endpoints API](#endpoints-api)
3. [Estructura de Componentes](#estructura-de-componentes)
4. [Integración en Next.js](#integración-en-nextjs)
5. [Ejemplos de Uso](#ejemplos-de-uso)
6. [Manejo de Errores](#manejo-de-errores)
7. [Mock Data](#mock-data)
8. [Editor de Texto Enriquecido](#editor-de-texto-enriquecido)

---

## 🏗️ ARQUITECTURA GENERAL

### Flujo de Datos

```
Next.js Frontend
    ↓
API Routes (Next.js)
    ↓
BFF (Backend for Frontend)
    ↓
Business Microservice (HITL Service)
    ↓
Database (PostgreSQL)
```

### Base URL

**API Routes (Next.js):**
```
/api/compliance/hitl
```

**BFF Endpoint Base:**
```
/api/v1/hitl
```

**Configuración:**
- El BFF está configurado en el microservicio `codeflowx.govern.bff.compliance`
- Todos los endpoints son **reactivos** (WebFlux/Mono)
- Usa **Circuit Breaker** y **Retry** para resiliencia

---

## 🔌 ENDPOINTS API

### 1. Dashboard HITL

#### GET `/api/compliance/hitl/dashboard`

**Descripción:** Obtiene métricas principales, intervenciones pendientes y decisiones recientes.

**Response:**
```typescript
interface HitlDashboardResponse {
  success: boolean;
  data: {
    metrics: {
      averageResponseTime: number; // horas
      approvalRate: number; // 0.0 - 1.0
      slaCompliance: number; // 0.0 - 1.0
      pendingInterventions: number;
      totalInterventions: number;
      interventionsByType: Map<string, number>;
      interventionsByStatus: Map<string, number>;
    };
    pendingInterventions: Array<{
      id: number;
      type: string;
      entityType: string;
      entityId: number;
      entityName: string;
      status: "PENDING" | "IN_REVIEW";
      urgency: "CRITICAL" | "HIGH" | "MEDIUM" | "LOW";
      createdAt: string; // ISO 8601
      slaDeadline: string; // ISO 8601
      timeRemaining: number; // horas
    }>;
    recentDecisions: Array<{
      id: number;
      type: string;
      entityType: string;
      entityId: number;
      entityName: string;
      decision: "APPROVED" | "REJECTED" | "MODIFIED";
      decisionReason: string; // HTML
      responseTime: number; // horas
      decisionDate: string; // ISO 8601
      userId: string;
    }>;
    supervisionConfig: Array<{
      type: string;
      enabled: boolean;
      slaHours: number;
      autoEscalation: boolean;
    }>;
  };
}
```

**Ejemplo de Uso:**
```typescript
const loadData = async () => {
  try {
    const response = await fetch("/api/compliance/hitl/dashboard");
    if (response.ok) {
      const result = await response.json();
      if (result.success) {
        setData(result.data);
      }
    }
  } catch (error) {
    console.error("Error loading HITL data:", error);
  }
};
```

---

### 2. Registrar Decisión

#### POST `/api/compliance/hitl/interventions`

**Descripción:** Registra una decisión sobre una intervención HITL.

**Request Body:**
```typescript
interface RecordDecisionRequest {
  interventionId: number;
  decision: "APPROVED" | "REJECTED" | "MODIFIED";
  reason: string; // HTML del editor enriquecido
  userId: string;
}
```

**Response:**
```typescript
interface RecordDecisionResponse {
  success: boolean;
  data: {
    id: number;
    decision: string;
    decisionDate: string;
    // ... otros campos
  };
}
```

**Ejemplo de Uso:**
```typescript
const handleRecordDecision = async (
  interventionId: number,
  decision: string,
  reason: string
) => {
  try {
    const response = await fetch("/api/compliance/hitl/interventions", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        interventionId,
        decision,
        reason,
        userId: "current-user",
      }),
    });

    if (response.ok) {
      await loadData(); // Recargar datos
      setDecisionDialogOpen(false);
    }
  } catch (error) {
    console.error("Error recording decision:", error);
  }
};
```

---

### 3. Configuración de Supervisión

#### GET `/api/compliance/hitl/config`

**Descripción:** Obtiene la configuración de supervisión.

**Response:**
```typescript
interface HitlConfigResponse {
  success: boolean;
  data: Array<{
    type: string;
    enabled: boolean;
    slaHours: number;
    autoEscalation: boolean;
  }>;
}
```

#### POST `/api/compliance/hitl/config`

**Descripción:** Actualiza la configuración de supervisión.

**Request Body:**
```typescript
interface UpdateConfigRequest {
  type: string;
  enabled: boolean;
  slaHours: number;
  autoEscalation: boolean;
}
```

**Ejemplo de Uso:**
```typescript
const handleUpdateConfig = async (config: HitlSupervisionConfig) => {
  try {
    const response = await fetch("/api/compliance/hitl/config", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(config),
    });

    if (response.ok) {
      await loadData();
      setConfigDialogOpen(false);
    }
  } catch (error) {
    console.error("Error updating config:", error);
  }
};
```

---

## 📁 ESTRUCTURA DE COMPONENTES

### Archivo Principal

**Ubicación:** `app/(app)/governance/compliance/hitl-supervision/page.tsx`

**Estructura:**
```typescript
export default function HitlSupervisionPage() {
  // Estados
  const [data, setData] = useState(mockHitlData);
  const [loading, setLoading] = useState(false);
  const [filterStatus, setFilterStatus] = useState<string>("ALL");
  const [filterType, setFilterType] = useState<string>("ALL");
  const [filterDecisionType, setFilterDecisionType] = useState<string>("ALL");
  const [configDialogOpen, setConfigDialogOpen] = useState(false);
  const [decisionDialogOpen, setDecisionDialogOpen] = useState(false);
  const [selectedIntervention, setSelectedIntervention] = useState<HitlIntervention | null>(null);
  const [decisionReason, setDecisionReason] = useState<string>("");
  const [decisionType, setDecisionType] = useState<string>("APPROVED");

  // Efectos
  useEffect(() => {
    loadData();
  }, []);

  // Handlers
  const loadData = async () => { ... };
  const handleRecordDecision = async (...) => { ... };
  const handleUpdateConfig = async (...) => { ... };

  // Filtros
  const filteredInterventions = ...;
  const filteredDecisions = ...;

  // Render helpers
  const getStatusBadge = (status: string) => { ... };
  const getUrgencyBadge = (urgency?: string) => { ... };

  // Render
  return (
    <div>
      {/* Header */}
      {/* Métricas */}
      {/* Tabs */}
      {/* Dialogs */}
    </div>
  );
}
```

### Componentes Utilizados

1. **Card Components:**
   - `Card`, `CardBody`, `CardHeader`, `CardTitle`, `CardContent`
   - Ubicación: `@/components/ui/card`

2. **Dialog Components:**
   - `Dialog`, `DialogContent`, `DialogHeader`, `DialogTitle`, `DialogTrigger`
   - Ubicación: `@/components/ui/dialog`

3. **Form Components:**
   - `Input`, `Label`, `Select`, `Textarea`, `Switch`, `Button`
   - Ubicación: `@/components/ui/*`

4. **Rich Text Editor:**
   - `RichTextEditor`
   - Ubicación: `@/components/ui/rich-text-editor`
   - Librería: `react-quill`

---

## 🔧 INTEGRACIÓN EN NEXT.JS

### Configuración de Mock

**Archivo:** `app/config/mock.ts`

```typescript
export const USE_MOCK = process.env.NEXT_PUBLIC_USE_MOCK === 'true';
export const BFF_BASE_URL = process.env.NEXT_PUBLIC_BFF_BASE_URL || 'http://localhost:8080';
```

### Uso de Mock Data

**Archivo:** `app/(app)/governance/data/mockHitl.ts`

```typescript
export const mockHitlData = {
  metrics: { ... },
  pendingInterventions: [ ... ],
  recentDecisions: [ ... ],
  supervisionConfig: [ ... ]
};
```

### Carga de Datos

```typescript
const loadData = async () => {
  try {
    setLoading(true);
    const response = await fetch("/api/compliance/hitl/dashboard");
    if (response.ok) {
      const result = await response.json();
      if (result.success) {
        setData(result.data);
      }
    }
  } catch (error) {
    console.error("Error loading HITL data:", error);
  } finally {
    setLoading(false);
  }
};
```

---

## 💻 EJEMPLOS DE USO

### Ejemplo 1: Mostrar Intervenciones en Grid

```typescript
<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
  {filteredInterventions?.map((intervention) => {
    const isSlaUrgent = intervention.timeRemaining < 1;
    const isSlaWarning = intervention.timeRemaining < 2 && intervention.timeRemaining >= 1;

    return (
      <Card
        key={intervention.id}
        className={`transition-all hover:shadow-lg ${
          isSlaUrgent
            ? "bg-red-50 dark:bg-red-950 border-red-500"
            : isSlaWarning
            ? "bg-orange-50 dark:bg-orange-950 border-orange-500"
            : "bg-card"
        }`}
      >
        <CardContent className="p-4">
          {/* Contenido de la card */}
        </CardContent>
      </Card>
    );
  })}
</div>
```

### Ejemplo 2: Dialog de Decisión

```typescript
<Dialog open={decisionDialogOpen} onOpenChange={setDecisionDialogOpen}>
  <DialogContent maxWidth="700px">
    <DialogHeader>
      <DialogTitle>Registrar Decisión</DialogTitle>
    </DialogHeader>
    <div className="modal-body">
      <div className="space-y-4">
        <div>
          <Label>Entidad</Label>
          <Input value={selectedIntervention?.entityName} readOnly />
        </div>
        <div>
          <Label>Decisión</Label>
          <Select value={decisionType} onValueChange={setDecisionType}>
            <SelectTrigger>
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="APPROVED">Aprobado</SelectItem>
              <SelectItem value="REJECTED">Rechazado</SelectItem>
              <SelectItem value="MODIFIED">Modificado</SelectItem>
            </SelectContent>
          </Select>
        </div>
        <div>
          <Label>Razón de la Decisión</Label>
          <RichTextEditor
            value={decisionReason}
            onChange={setDecisionReason}
            placeholder="Explica la razón de la decisión..."
            minHeight="350px"
          />
        </div>
      </div>
    </div>
    <div className="modal-footer">
      <Button variant="outline" onClick={() => setDecisionDialogOpen(false)}>
        Cancelar
      </Button>
      <Button onClick={() => handleRecordDecision(...)}>
        Guardar
      </Button>
    </div>
  </DialogContent>
</Dialog>
```

### Ejemplo 3: Filtros

```typescript
<div className="flex items-center gap-2">
  <Select value={filterStatus} onValueChange={setFilterStatus}>
    <SelectTrigger className="w-[150px]">
      <SelectValue />
    </SelectTrigger>
    <SelectContent>
      <SelectItem value="ALL">Todos</SelectItem>
      <SelectItem value="PENDING">Pendiente</SelectItem>
      <SelectItem value="IN_REVIEW">En Revisión</SelectItem>
    </SelectContent>
  </Select>
  <Select value={filterType} onValueChange={setFilterType}>
    <SelectTrigger className="w-[150px]">
      <SelectValue />
    </SelectTrigger>
    <SelectContent>
      <SelectItem value="ALL">Todos</SelectItem>
      <SelectItem value="Agent">Agent</SelectItem>
      <SelectItem value="Model">Model</SelectItem>
      <SelectItem value="Prompt">Prompt</SelectItem>
    </SelectContent>
  </Select>
</div>
```

---

## ⚠️ MANEJO DE ERRORES

### Errores de Carga

```typescript
const loadData = async () => {
  try {
    setLoading(true);
    const response = await fetch("/api/compliance/hitl/dashboard");
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const result = await response.json();
    if (result.success) {
      setData(result.data);
    } else {
      console.error("Error in response:", result.error);
    }
  } catch (error) {
    console.error("Error loading HITL data:", error);
    // Mostrar mensaje de error al usuario
  } finally {
    setLoading(false);
  }
};
```

### Errores de Guardado

```typescript
const handleRecordDecision = async (...) => {
  try {
    const response = await fetch("/api/compliance/hitl/interventions", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ ... }),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || "Error al guardar decisión");
    }

    // Éxito
    await loadData();
    setDecisionDialogOpen(false);
  } catch (error) {
    console.error("Error recording decision:", error);
    // Mostrar mensaje de error al usuario
  }
};
```

---

## 🎨 EDITOR DE TEXTO ENRIQUECIDO

### Componente RichTextEditor

**Ubicación:** `components/ui/rich-text-editor.tsx`

**Librería:** `react-quill`

**Uso:**
```typescript
import { RichTextEditor } from "@/components/ui/rich-text-editor";

<RichTextEditor
  value={decisionReason}
  onChange={setDecisionReason}
  placeholder="Escribe aquí..."
  minHeight="350px"
/>
```

**Funcionalidades:**
- Formato de texto (negrita, cursiva, subrayado, tachado)
- Listas (ordenadas y con viñetas)
- Colores (texto y fondo)
- Alineación
- Enlaces
- Citas y bloques de código
- Limpieza de formato

**Estilos:**
- Adaptado al tema usando variables CSS
- Toolbar con fondo del tema
- Editor con altura configurable

---

## 📦 MOCK DATA

### Estructura de Mock

**Archivo:** `app/(app)/governance/data/mockHitl.ts`

```typescript
export const mockHitlData = {
  metrics: {
    averageResponseTime: 2.5,
    approvalRate: 0.85,
    slaCompliance: 0.92,
    pendingInterventions: 12,
    totalInterventions: 150,
    interventionsByType: {
      "Agent": 45,
      "Model": 60,
      "Prompt": 45
    },
    interventionsByStatus: {
      "PENDING": 8,
      "IN_REVIEW": 4
    }
  },
  pendingInterventions: [ ... ],
  recentDecisions: [ ... ],
  supervisionConfig: [ ... ]
};
```

### Uso de Mock

```typescript
const [data, setData] = useState(mockHitlData);

// En desarrollo, usar mock si USE_MOCK está activado
useEffect(() => {
  if (USE_MOCK) {
    setData(mockHitlData);
  } else {
    loadData();
  }
}, []);
```

---

## 🎯 MEJORES PRÁCTICAS

### 1. **Gestión de Estado**
- Usar `useState` para estado local
- Usar `useEffect` para carga inicial de datos
- Limpiar estado al cerrar dialogs

### 2. **Filtros**
- Aplicar filtros en el cliente (filtrado local)
- Mantener estado de filtros en `useState`
- Actualizar UI reactivamente

### 3. **Dialogs**
- Usar estado para controlar apertura/cierre
- Limpiar formularios al cerrar
- Validar antes de guardar

### 4. **Editor de Texto**
- Guardar HTML directamente del editor
- No sanitizar (el backend lo maneja)
- Usar `minHeight` para altura adecuada

### 5. **Responsive Design**
- Grid de 3 columnas en desktop
- Grid de 2 columnas en tablet
- Grid de 1 columna en móvil

---

## 🔗 REFERENCIAS

- **Arquitectura Frontend:** `docs/ARQUITECTURA_FRONTEND.md`
- **Componentes UI:** `components/ui/*`
- **API Routes:** `app/api/compliance/hitl/*`
- **Mock Data:** `app/(app)/governance/data/mockHitl.ts`

---

**Última actualización:** Diciembre 2025
**Versión del documento:** 1.0
