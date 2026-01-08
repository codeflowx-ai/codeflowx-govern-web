# 🎨 GUÍA DE DESARROLLO FRONTEND - AGENTES DE IA

**Versión:** 1.0
**Fecha:** Enero 2025
**Audiencia:** Desarrolladores Frontend, UI/UX Engineers
**Proyecto:** CodeFlowX AI Governance Platform

---

## 🎯 PROPÓSITO

Esta guía define cómo desarrollar funcionalidades frontend para el módulo de agentes de IA, incluyendo arquitectura, patrones, componentes y mejores prácticas.

---

## 🏗️ ARQUITECTURA DEL FRONTEND

### Estructura de Directorios

```
app/(app)/governance/agents/
├── registry/                    # Registro de agentes
│   ├── [id]/                   # Detalle de agente (con tabs)
│   │   ├── page.tsx
│   │   ├── ReviewRulesTab.tsx
│   │   ├── ComplianceTab.tsx
│   │   ├── CertificationTab.tsx
│   │   └── RetirementTab.tsx
│   └── page.tsx                 # Listado de agentes
├── approval/                    # Aprobaciones
│   ├── overview/
│   │   └── page.tsx
│   └── create/
│       └── page.tsx
├── governance/                  # Políticas de gobierno
│   ├── overview/
│   │   └── page.tsx
│   ├── create/
│   │   └── page.tsx
│   └── [id]/
│       └── page.tsx
├── compliance/                  # Cumplimiento
│   ├── overview/
│   │   └── page.tsx
│   ├── create/
│   │   └── page.tsx
│   └── [id]/
│       └── page.tsx
└── ...
```

### Flujo de Datos

```
Component (React)
    ↓ useState/useEffect
API Call (fetch)
    ↓
Backend REST API
    ↓
Response (JSON)
    ↓
State Update
    ↓
UI Re-render
```

---

## 📝 CONVENCIONES DE NOMENCLATURA

### Archivos y Directorios

- **Páginas:** `page.tsx` (Next.js App Router)
- **Componentes:** PascalCase (ej: `ReviewRulesTab.tsx`)
- **Directorios:** kebab-case (ej: `bias-detection/`)
- **Rutas dinámicas:** `[id]/page.tsx`

### Componentes React

- **Nombre de componente:** PascalCase (ej: `AgentDetailPage`)
- **Props interface:** `ComponentNameProps` (ej: `AgentDetailPageProps`)
- **Hooks personalizados:** `use` + PascalCase (ej: `useAgentData`)

**Ejemplo:**
```typescript
interface AgentDetailPageProps {
  // Props si es necesario
}

export default function AgentDetailPage(props: AgentDetailPageProps) {
  // Implementación
}
```

### Interfaces TypeScript

- **Nombre:** PascalCase (ej: `Agent`, `AgentApproval`)
- **Campos:** camelCase (ej: `agentUuid`, `agentName`)

**Ejemplo:**
```typescript
interface Agent {
  id: number;
  uuid?: string;
  name: string;
  description: string;
  status: string;
  version: string;
  domain: string;
  createdAt: string;
  lastModified: string;
}
```

---

## 🔨 DESARROLLO PASO A PASO

### 1. Crear Página de Overview (Listado)

**Ubicación:** `app/(app)/governance/agents/{module}/overview/page.tsx`

**Template:**
```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { Eye, Plus } from "lucide-react";
import { SimpleModal } from "@/components/ui/SimpleModal";

interface MyEntity {
  id: number;
  agentUuid: string;
  agentName: string;
  name: string;
  status: string;
  createdAt: string;
}

export default function MyEntityOverviewPage() {
  const { t, mounted } = useTranslation();
  const router = useRouter();
  const [entities, setEntities] = useState<MyEntity[]>([]);
  const [filteredEntities, setFilteredEntities] = useState<MyEntity[]>([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedEntity, setSelectedEntity] = useState<MyEntity | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [loading, setLoading] = useState(true);

  // Cargar datos
  useEffect(() => {
    loadData();
  }, []);

  // Filtrar datos
  useEffect(() => {
    if (searchTerm.trim() === "") {
      setFilteredEntities(entities);
    } else {
      const filtered = entities.filter((entity) =>
        entity.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        entity.agentName.toLowerCase().includes(searchTerm.toLowerCase())
      );
      setFilteredEntities(filtered);
    }
  }, [searchTerm, entities]);

  const loadData = async () => {
    try {
      setLoading(true);
      const response = await fetch("/api/v1/agents/my-entity/list");
      if (response.ok) {
        const data = await response.json();
        setEntities(data);
        setFilteredEntities(data);
      }
    } catch (error) {
      console.error("Error loading entities:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleView = (entity: MyEntity) => {
    setSelectedEntity(entity);
    setIsModalOpen(true);
  };

  const handleCreate = () => {
    router.push("/governance/agents/my-entity/create");
  };

  if (!mounted || loading) {
    return <div>Cargando...</div>;
  }

  return (
    <div className="container mx-auto p-6">
      <div className="mb-6 flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold">My Entities</h1>
          <p className="text-muted-foreground">Gestión de entidades</p>
        </div>
        <Button onClick={handleCreate}>
          <Plus className="mr-2 h-4 w-4" />
          Registrar Entidad
        </Button>
      </div>

      {/* Estadísticas */}
      <div className="mb-6 grid grid-cols-1 gap-4 md:grid-cols-4">
        <Card>
          <CardBody>
            <div className="text-2xl font-bold">{entities.length}</div>
            <div className="text-sm text-muted-foreground">Total</div>
          </CardBody>
        </Card>
        {/* Más estadísticas */}
      </div>

      {/* Búsqueda y filtros */}
      <Card className="mb-6">
        <CardBody>
          <Input
            placeholder="Buscar por nombre o agente..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </CardBody>
      </Card>

      {/* Tabla */}
      <Card>
        <CardHeader>
          <CardTitle>Listado de Entidades</CardTitle>
        </CardHeader>
        <CardBody>
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr>
                  <th>Agente</th>
                  <th>Nombre</th>
                  <th>Estado</th>
                  <th>Fecha</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {filteredEntities.map((entity) => (
                  <tr key={entity.id}>
                    <td>{entity.agentName}</td>
                    <td>{entity.name}</td>
                    <td>
                      <span className={`badge badge-${entity.status.toLowerCase()}`}>
                        {entity.status}
                      </span>
                    </td>
                    <td>{new Date(entity.createdAt).toLocaleDateString()}</td>
                    <td>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => handleView(entity)}
                      >
                        <Eye className="h-4 w-4" />
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </CardBody>
      </Card>

      {/* Modal de visualización */}
      <SimpleModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title="Detalles de Entidad"
      >
        {selectedEntity && (
          <div className="space-y-4">
            <div>
              <strong>Agente:</strong> {selectedEntity.agentName}
            </div>
            <div>
              <strong>Nombre:</strong> {selectedEntity.name}
            </div>
            <div>
              <strong>Estado:</strong> {selectedEntity.status}
            </div>
            <div>
              <strong>Fecha:</strong> {new Date(selectedEntity.createdAt).toLocaleString()}
            </div>
          </div>
        )}
      </SimpleModal>
    </div>
  );
}
```

---

### 2. Crear Página de Creación

**Ubicación:** `app/(app)/governance/agents/{module}/create/page.tsx`

**Template:**
```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { ArrowLeft, Save } from "lucide-react";

interface Agent {
  id: number;
  uuid: string;
  name: string;
}

export default function CreateMyEntityPage() {
  const { t, mounted } = useTranslation();
  const router = useRouter();
  const [loading, setLoading] = useState(false);
  const [agents, setAgents] = useState<Agent[]>([]);

  const [formData, setFormData] = useState({
    agentUuid: "",
    name: "",
    status: "DRAFT",
  });

  useEffect(() => {
    loadAgents();
  }, []);

  const loadAgents = async () => {
    try {
      const response = await fetch("/api/v1/agents/registry/list");
      if (response.ok) {
        const data = await response.json();
        setAgents(data.items || []);
      }
    } catch (error) {
      console.error("Error loading agents:", error);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      // Obtener usuario actual
      const userResponse = await fetch("/api/auth/me");
      const user = await userResponse.json();

      const payload = {
        ...formData,
        createdBy: user?.username || "system",
      };

      const response = await fetch("/api/v1/agents/my-entity", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        const data = await response.json();
        router.push(`/governance/agents/my-entity/${data.id}`);
      } else {
        const error = await response.json();
        alert(`Error: ${error.message || "Error al crear entidad"}`);
      }
    } catch (error) {
      console.error("Error creating entity:", error);
      alert("Error al crear entidad");
    } finally {
      setLoading(false);
    }
  };

  const handleSaveDraft = async () => {
    setFormData({ ...formData, status: "DRAFT" });
    await handleSubmit(new Event("submit") as any);
  };

  const handleSubmitForApproval = async () => {
    setFormData({ ...formData, status: "PENDING" });
    await handleSubmit(new Event("submit") as any);

    // Lanzar proceso BPMN
    try {
      await fetch("/api/bpmn/process/start", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          processKey: "my-entity-approval-v1",
          variables: {
            entityId: formData.agentUuid,
            status: "PENDING",
          },
        }),
      });
    } catch (error) {
      console.error("Error launching BPMN process:", error);
    }
  };

  if (!mounted) {
    return <div>Cargando...</div>;
  }

  return (
    <div className="container mx-auto p-6">
      <div className="mb-6 flex items-center justify-between">
        <div className="flex items-center gap-4">
          <Button
            variant="ghost"
            onClick={() => router.back()}
          >
            <ArrowLeft className="mr-2 h-4 w-4" />
            Volver
          </Button>
          <div>
            <h1 className="text-3xl font-bold">Crear Entidad</h1>
            <p className="text-muted-foreground">Registrar nueva entidad</p>
          </div>
        </div>
        <div className="flex gap-2">
          <Button
            variant="outline"
            onClick={handleSaveDraft}
            disabled={loading}
          >
            Guardar Borrador
          </Button>
          <Button
            onClick={handleSubmitForApproval}
            disabled={loading}
          >
            Enviar para Aprobación
          </Button>
        </div>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Información de la Entidad</CardTitle>
        </CardHeader>
        <CardBody>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
              <div>
                <Label htmlFor="agentUuid">Agente *</Label>
                <Select
                  value={formData.agentUuid}
                  onValueChange={(value) =>
                    setFormData({ ...formData, agentUuid: value })
                  }
                >
                  <SelectTrigger>
                    <SelectValue placeholder="Seleccionar agente" />
                  </SelectTrigger>
                  <SelectContent>
                    {agents
                      .filter((a) => a.status === "ACTIVE")
                      .map((agent) => (
                        <SelectItem key={agent.id} value={agent.uuid}>
                          {agent.name}
                        </SelectItem>
                      ))}
                  </SelectContent>
                </Select>
              </div>

              <div>
                <Label htmlFor="name">Nombre *</Label>
                <Input
                  id="name"
                  value={formData.name}
                  onChange={(e) =>
                    setFormData({ ...formData, name: e.target.value })
                  }
                  required
                />
              </div>

              <div>
                <Label htmlFor="status">Estado</Label>
                <Select
                  value={formData.status}
                  onValueChange={(value) =>
                    setFormData({ ...formData, status: value })
                  }
                  disabled
                >
                  <SelectTrigger>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="DRAFT">Borrador</SelectItem>
                    <SelectItem value="PENDING">Pendiente</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </div>
          </form>
        </CardBody>
      </Card>
    </div>
  );
}
```

---

### 3. Crear Página de Detalle/Edición

**Ubicación:** `app/(app)/governance/agents/{module}/[id]/page.tsx`

**Template:**
```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { useState, useEffect } from "react";
import { useParams, useSearchParams, useRouter } from "next/navigation";
import { ArrowLeft, Save, Edit } from "lucide-react";

interface MyEntity {
  id: number;
  agentUuid: string;
  agentName: string;
  name: string;
  status: string;
  createdAt: string;
  updatedAt: string;
  createdBy: string;
  updatedBy: string;
}

export default function MyEntityDetailPage() {
  const { t, mounted } = useTranslation();
  const params = useParams();
  const searchParams = useSearchParams();
  const router = useRouter();
  const id = params.id as string;
  const isEditMode = searchParams.get("edit") === "true";

  const [entity, setEntity] = useState<MyEntity | null>(null);
  const [formData, setFormData] = useState<Partial<MyEntity>>({});
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (id && id !== "new") {
      loadEntity();
    }
  }, [id]);

  useEffect(() => {
    if (entity) {
      setFormData(entity);
    }
  }, [entity]);

  const loadEntity = async () => {
    try {
      setLoading(true);
      const response = await fetch(`/api/v1/agents/my-entity/${id}`);
      if (response.ok) {
        const data = await response.json();
        setEntity(data);
      }
    } catch (error) {
      console.error("Error loading entity:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSave = async () => {
    setSaving(true);
    try {
      const userResponse = await fetch("/api/auth/me");
      const user = await userResponse.json();

      const payload = {
        ...formData,
        updatedBy: user?.username || "system",
      };

      const response = await fetch(`/api/v1/agents/my-entity/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        router.push(`/governance/agents/my-entity/${id}`);
      } else {
        const error = await response.json();
        alert(`Error: ${error.message || "Error al actualizar"}`);
      }
    } catch (error) {
      console.error("Error updating entity:", error);
      alert("Error al actualizar entidad");
    } finally {
      setSaving(false);
    }
  };

  if (!mounted || loading) {
    return <div>Cargando...</div>;
  }

  if (!entity) {
    return <div>Entidad no encontrada</div>;
  }

  return (
    <div className="container mx-auto p-6">
      <div className="mb-6 flex items-center justify-between">
        <div className="flex items-center gap-4">
          <Button variant="ghost" onClick={() => router.back()}>
            <ArrowLeft className="mr-2 h-4 w-4" />
            Volver
          </Button>
          <div>
            <h1 className="text-3xl font-bold">
              {isEditMode ? "Editar Entidad" : "Detalles de Entidad"}
            </h1>
            <p className="text-muted-foreground">{entity.name}</p>
          </div>
        </div>
        {!isEditMode && (
          <Button onClick={() => router.push(`?edit=true`)}>
            <Edit className="mr-2 h-4 w-4" />
            Editar
          </Button>
        )}
        {isEditMode && (
          <div className="flex gap-2">
            <Button variant="outline" onClick={() => router.push(`/governance/agents/my-entity/${id}`)}>
              Cancelar
            </Button>
            <Button onClick={handleSave} disabled={saving}>
              <Save className="mr-2 h-4 w-4" />
              Guardar Cambios
            </Button>
          </div>
        )}
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Información General</CardTitle>
        </CardHeader>
        <CardBody>
          <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
            <div>
              <Label>Agente</Label>
              {isEditMode ? (
                <Input value={formData.agentName || ""} disabled />
              ) : (
                <div className="text-sm">{entity.agentName}</div>
              )}
            </div>

            <div>
              <Label>Nombre</Label>
              {isEditMode ? (
                <Input
                  value={formData.name || ""}
                  onChange={(e) =>
                    setFormData({ ...formData, name: e.target.value })
                  }
                />
              ) : (
                <div className="text-sm">{entity.name}</div>
              )}
            </div>

            <div>
              <Label>Estado</Label>
              {isEditMode ? (
                <Input value={formData.status || ""} disabled />
              ) : (
                <div className="text-sm">{entity.status}</div>
              )}
            </div>
          </div>
        </CardBody>
      </Card>

      {/* Sección de Auditoría */}
      <Card className="mt-6">
        <CardHeader>
          <CardTitle>Auditoría</CardTitle>
        </CardHeader>
        <CardBody>
          <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
            <div>
              <Label>Creado por</Label>
              <div className="text-sm">{entity.createdBy}</div>
            </div>
            <div>
              <Label>Fecha de creación</Label>
              <div className="text-sm">
                {new Date(entity.createdAt).toLocaleString()}
              </div>
            </div>
            <div>
              <Label>Actualizado por</Label>
              <div className="text-sm">{entity.updatedBy}</div>
            </div>
            <div>
              <Label>Fecha de actualización</Label>
              <div className="text-sm">
                {new Date(entity.updatedAt).toLocaleString()}
              </div>
            </div>
          </div>
        </CardBody>
      </Card>
    </div>
  );
}
```

---

### 4. Crear Componente de Tab

**Ubicación:** `app/(app)/governance/agents/registry/[id]/MyTab.tsx`

**Template:**
```typescript
"use client";

import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { useState, useEffect } from "react";

interface MyTabProps {
  agentId: number;
  agentUuid: string;
}

export default function MyTab({ agentId, agentUuid }: MyTabProps) {
  const [data, setData] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, [agentId]);

  const loadData = async () => {
    try {
      setLoading(true);
      const response = await fetch(
        `/api/v1/agents/my-resource?agentUuid=${agentUuid}`
      );
      if (response.ok) {
        const result = await response.json();
        setData(result);
      }
    } catch (error) {
      console.error("Error loading data:", error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div>Cargando...</div>;
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle>Mi Tab</CardTitle>
      </CardHeader>
      <CardBody>
        {/* Contenido del tab */}
        <div className="space-y-4">
          {data.map((item) => (
            <div key={item.id}>{item.name}</div>
          ))}
        </div>
      </CardBody>
    </Card>
  );
}
```

---

## 🎨 COMPONENTES UI

### Componentes Disponibles

```typescript
// Botones
import { Button } from "@/components/ui/button";

// Cards
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";

// Inputs
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Label } from "@/components/ui/label";

// Select
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

// Tabs
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";

// Badges
import { Badge } from "@/components/ui/badge";

// Modal
import { SimpleModal } from "@/components/ui/SimpleModal";
```

---

## 🔄 ESTADO Y DATOS

### useState

```typescript
const [data, setData] = useState<MyType[]>([]);
const [loading, setLoading] = useState(false);
const [error, setError] = useState<string | null>(null);
```

### useEffect

```typescript
// Cargar datos al montar
useEffect(() => {
  loadData();
}, []);

// Cargar datos cuando cambia un parámetro
useEffect(() => {
  if (id) {
    loadData();
  }
}, [id]);

// Filtrar datos cuando cambia el término de búsqueda
useEffect(() => {
  const filtered = data.filter((item) =>
    item.name.toLowerCase().includes(searchTerm.toLowerCase())
  );
  setFilteredData(filtered);
}, [searchTerm, data]);
```

### useMemo

```typescript
const filteredData = useMemo(() => {
  return data.filter((item) =>
    item.name.toLowerCase().includes(searchTerm.toLowerCase())
  );
}, [data, searchTerm]);
```

---

## 🌐 LLAMADAS A API

### Fetch Básico

```typescript
const loadData = async () => {
  try {
    setLoading(true);
    const response = await fetch("/api/v1/agents/my-entity/list");
    if (response.ok) {
      const data = await response.json();
      setData(data);
    } else {
      const error = await response.json();
      console.error("Error:", error.message);
    }
  } catch (error) {
    console.error("Error loading data:", error);
  } finally {
    setLoading(false);
  }
};
```

### POST Request

```typescript
const handleSubmit = async (payload: any) => {
  try {
    const response = await fetch("/api/v1/agents/my-entity", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(payload),
    });

    if (response.ok) {
      const data = await response.json();
      router.push(`/governance/agents/my-entity/${data.id}`);
    } else {
      const error = await response.json();
      alert(`Error: ${error.message}`);
    }
  } catch (error) {
    console.error("Error:", error);
    alert("Error al guardar");
  }
};
```

### PUT Request

```typescript
const handleUpdate = async (id: number, payload: any) => {
  try {
    const response = await fetch(`/api/v1/agents/my-entity/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(payload),
    });

    if (response.ok) {
      await loadData(); // Recargar datos
    } else {
      const error = await response.json();
      alert(`Error: ${error.message}`);
    }
  } catch (error) {
    console.error("Error:", error);
    alert("Error al actualizar");
  }
};
```

---

## 🧭 NAVEGACIÓN

### useRouter

```typescript
import { useRouter } from "next/navigation";

const router = useRouter();

// Navegar a una ruta
router.push("/governance/agents/my-entity/123");

// Navegar hacia atrás
router.back();

// Navegar con query params
router.push("/governance/agents/my-entity/123?edit=true");
```

### useParams

```typescript
import { useParams } from "next/navigation";

const params = useParams();
const id = params.id as string;
```

### useSearchParams

```typescript
import { useSearchParams } from "next/navigation";

const searchParams = useSearchParams();
const isEditMode = searchParams.get("edit") === "true";
```

---

## 🌍 INTERNACIONALIZACIÓN

### useTranslation

```typescript
import { useTranslation } from "@/app/config/i18n";

const { t, mounted } = useTranslation();

// Usar traducción
<h1>{t("agents.title")}</h1>

// Esperar a que esté montado
if (!mounted) {
  return <div>Cargando...</div>;
}
```

---

## 📋 CHECKLIST DE DESARROLLO

### Al Crear Nueva Funcionalidad

- [ ] Crear estructura de directorios correcta
- [ ] Crear página de overview con listado
- [ ] Crear página de creación
- [ ] Crear página de detalle/edición
- [ ] Implementar interfaces TypeScript
- [ ] Implementar llamadas a API
- [ ] Implementar manejo de estado
- [ ] Implementar validación de formularios
- [ ] Implementar manejo de errores
- [ ] Agregar loading states
- [ ] Agregar internacionalización
- [ ] Agregar navegación correcta
- [ ] Probar en diferentes tamaños de pantalla
- [ ] Probar flujos completos

---

## 🚨 MEJORES PRÁCTICAS

1. **Siempre usar "use client":**
   - Agregar `"use client"` al inicio de componentes que usan hooks

2. **Manejar estados de carga:**
   - Mostrar loading mientras se cargan datos
   - Deshabilitar botones durante operaciones

3. **Validar datos:**
   - Validar formularios antes de enviar
   - Mostrar mensajes de error claros

4. **Manejar errores:**
   - Capturar errores en try/catch
   - Mostrar mensajes de error al usuario
   - Loggear errores en consola

5. **Optimizar rendimiento:**
   - Usar `useMemo` para cálculos costosos
   - Usar `useCallback` para funciones que se pasan como props
   - Evitar re-renders innecesarios

6. **Accesibilidad:**
   - Usar labels en formularios
   - Usar aria-labels cuando sea necesario
   - Asegurar contraste de colores

7. **Responsive:**
   - Usar grid responsivo (grid-cols-1 md:grid-cols-2)
   - Probar en móvil y desktop

---

## 📚 REFERENCIAS

- **Next.js App Router:** https://nextjs.org/docs/app
- **React Hooks:** https://react.dev/reference/react
- **TypeScript:** https://www.typescriptlang.org/
- **Tailwind CSS:** https://tailwindcss.com/
- **shadcn/ui:** https://ui.shadcn.com/

---

**Última Actualización:** Enero 2025
**Versión:** 1.0
**Estado:** ✅ Operativo
