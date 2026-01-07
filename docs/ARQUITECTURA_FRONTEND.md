# 🏗️ ARQUITECTURA FRONTEND - CODEFLOWX STUDIO

**Fecha:** Diciembre 2025
**Versión:** 1.0
**Propósito:** Documentación completa de la arquitectura del frontend para desarrolladores y agentes AI

---

## 📋 ÍNDICE

1. [Stack Tecnológico](#stack-tecnológico)
2. [Estructura de Directorios](#estructura-de-directorios)
3. [Gestión de Permisos y Roles](#gestión-de-permisos-y-roles)
4. [Pantallas y Navegación](#pantallas-y-navegación)
5. [Estilos y Diseño](#estilos-y-diseño)
6. [Internacionalización (i18n)](#internacionalización-i18n)
7. [Sistema de Mocks](#sistema-de-mocks)
8. [API y Comunicación](#api-y-comunicación)
9. [Arquitectura de Backend y Microservicios de Negocio](#-arquitectura-de-backend-y-microservicios-de-negocio)
10. [Guías de Desarrollo](#guías-de-desarrollo)

---

## 🛠️ STACK TECNOLÓGICO

### Tecnologías Principales

```typescript
// Stack Core
- Next.js 14+ (App Router)
- React 18+ con TypeScript
- Tailwind CSS + Headless UI
- Zustand (State Management)
- React Query (Data Fetching)
- React Hook Form (Formularios)
- Zod (Validación de Schemas)
```

### Dependencias Clave

- **UI Components:** `@/components/ui/*` (shadcn/ui)
- **Icons:** `lucide-react`
- **Styling:** Tailwind CSS con configuración personalizada
- **Routing:** Next.js App Router con layouts anidados

---

## 📁 ESTRUCTURA DE DIRECTORIOS

```
app/
├── (app)/                    # Rutas protegidas (requieren autenticación)
│   ├── dashboard/           # Dashboard principal
│   ├── governance/          # Módulo de gobernanza
│   │   ├── page.tsx         # Dashboard de governance
│   │   ├── overview/        # Vista general de políticas
│   │   ├── detail/          # Detalle de política
│   │   ├── security/        # Seguridad
│   │   ├── monitoring/      # Monitoreo
│   │   ├── compliance/      # Compliance
│   │   └── ...
│   ├── model-management/    # Gestión de modelos
│   ├── agents/              # Gestión de agentes
│   └── ...
├── (auth)/                  # Rutas de autenticación
├── api/                     # API Routes (Next.js)
│   └── governance/          # Endpoints de governance
├── components/              # Componentes reutilizables
│   └── ui/                  # Componentes UI base (shadcn/ui)
├── config/                  # Configuración
│   ├── i18n/                # Internacionalización
│   │   ├── modules/         # Módulos de traducción
│   │   │   ├── common.ts
│   │   │   ├── governance/  # Traducciones por pantalla
│   │   │   │   ├── dashboard.ts
│   │   │   │   ├── policies-overview.ts
│   │   │   │   └── ...
│   │   │   └── ...
│   │   ├── types.ts
│   │   ├── config.ts
│   │   └── index.ts
│   ├── modules.ts           # Configuración de módulos
│   └── role-redirects.ts   # Redirecciones por rol
├── lib/                     # Utilidades y helpers
│   └── api-client.ts        # Cliente API con soporte mock
├── types/                   # Tipos TypeScript
└── mocks/                   # Datos mock (opcional)
```

---

## 🔐 GESTIÓN DE PERMISOS Y ROLES

### Roles Principales

| Rol | Descripción | Acceso |
|-----|-------------|--------|
| **ADMIN** | Administrador del sistema | Acceso completo |
| **GESTOR_GOBIERNO** | Gestor de gobernanza | Dashboards + Gobierno + BPMN |
| **CIENTIFICO_DATOS** | Científico de datos | Platform + Training + Dashboards técnicos |
| **USUARIO_OPERATIVO** | Usuario operativo | Solo BPMN y dashboards básicos |

### Matriz de Permisos

| Funcionalidad | Admin | Gestor | Científico | Operativo |
|---------------|-------|--------|------------|-----------|
| **Dashboards** | ✅ Todos | ✅ Ejecutivos | ✅ Técnicos | ✅ Básicos |
| **Platform CRUD** | ✅ Completo | ❌ | ✅ Agents/Models | ❌ |
| **Gobierno** | ✅ Completo | ✅ Completo | ❌ | ❌ |
| **BPMN Tasks** | ✅ Todas | ✅ Aprobaciones | ✅ Técnicas | ✅ Asignadas |
| **Configuración** | ✅ Completa | ❌ | ❌ | ❌ |

### Implementación

#### Verificación de Permisos en Componentes

```typescript
// app/lib/permissions.ts
export function hasPermission(user: User, permission: string): boolean {
  return user.roles.some(role =>
    role.permissions.includes(permission)
  );
}

// Uso en componentes
import { hasPermission } from '@/lib/permissions';
import { useAuth } from '@/contexts/AuthContext';

export default function GovernancePage() {
  const { user } = useAuth();

  if (!hasPermission(user, 'GOVERNANCE_VIEW')) {
    return <AccessDenied />;
  }

  return <GovernanceDashboard />;
}
```

#### Redirecciones por Rol

```typescript
// app/config/role-redirects.ts
export const roleRedirects: Record<string, string> = {
  ADMIN: '/dashboard',
  GESTOR_GOBIERNO: '/governance',
  CIENTIFICO_DATOS: '/model-management',
  USUARIO_OPERATIVO: '/bpmn/task-inbox',
};
```

---

## 📱 PANTALLAS Y NAVEGACIÓN

### Estructura de Pantallas por Módulo

#### Governance

| Pantalla | Ruta | Descripción | Roles |
|----------|------|-------------|-------|
| Dashboard | `/governance` | Dashboard principal | Todos |
| Overview | `/governance/overview` | Vista general de políticas | Gestor, Admin |
| Detail | `/governance/detail/[id]` | Detalle de política | Gestor, Admin |
| Security | `/governance/security` | Seguridad y gobernanza | Gestor, Admin |
| Monitoring | `/governance/monitoring` | Monitoreo de gobernanza | Gestor, Admin |
| Compliance | `/governance/compliance` | Compliance | Gestor, Admin |
| Risk Assessment | `/governance/risk-assessment` | Evaluación de riesgos | Gestor, Admin |
| Auto Approval | `/governance/auto-approval` | Aprobación automática | Gestor, Admin |

### Patrones de Navegación

#### Layouts Anidados

```typescript
// app/(app)/governance/layout.tsx
export default function GovernanceLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="governance-layout">
      <GovernanceSidebar />
      <main>{children}</main>
    </div>
  );
}
```

#### Breadcrumbs

```typescript
// Componente de breadcrumbs automático
<Breadcrumbs>
  <Breadcrumb href="/governance">Governance</Breadcrumb>
  <Breadcrumb href="/governance/overview">Overview</Breadcrumb>
</Breadcrumbs>
```

---

## 🎨 ESTILOS Y DISEÑO

### Sistema de Diseño

#### Tailwind CSS

- **Configuración:** `tailwind.config.ts`
- **Variables CSS:** `app/globals.css`
- **Tema:** Modo claro/oscuro soportado

#### Componentes UI

- **Base:** shadcn/ui components
- **Ubicación:** `app/components/ui/`
- **Estilos:** Tailwind CSS con clases utilitarias

### Convenciones de Estilos

#### Estructura Estándar de Pantallas de Creación y Edición

**ESTÁNDAR OBLIGATORIO:** Todas las pantallas de creación y edición deben seguir esta estructura de dos líneas:

##### Línea 1: Título y Subtítulo

La primera línea contiene el título principal y un subtítulo descriptivo:

```typescript
{/* Línea 1: Título y Subtítulo */}
<div>
  <h1 className="text-3xl font-bold">
    {t("governance.data.integrations.create", "Crear Integración")}
  </h1>
  <p className="text-muted-foreground mt-2">
    {t("governance.data.integrations.createDescription", "Configura una nueva integración de datos")}
  </p>
</div>
```

##### Línea 2: Botón Volver (Izquierda) y Botones de Acción (Derecha)

La segunda línea contiene:
- **Izquierda:** Botón "Volver" con icono de flecha y texto
- **Derecha:** Botones de acción (Guardar, Crear, etc.)

```typescript
{/* Línea 2: Botón Volver (izquierda) y Botones de Acción (derecha) */}
<div className="flex items-center justify-between">
  <Button variant="ghost" onClick={() => router.push("/governance/data/integrations")}>
    <ArrowLeft className="mr-2 h-4 w-4" />
    {t("common.back", "Volver")}
  </Button>
  <div className="flex gap-2">
    <Button type="submit" form="integration-form" disabled={saving}>
      {saving ? (
        <>
          <Loader2 className="mr-2 h-4 w-4 animate-spin" />
          Guardando...
        </>
      ) : (
        <>
          <Save className="mr-2 h-4 w-4" />
          Guardar
        </>
      )}
    </Button>
  </div>
</div>
```

##### Estructura Completa de Ejemplo

```typescript
export default function CreateIntegrationPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [saving, setSaving] = useState(false);

  return (
    <div className="w-full py-6 space-y-6 px-6">
      {/* Línea 1: Título y Subtítulo */}
      <div>
        <h1 className="text-3xl font-bold">
          {t("governance.data.integrations.create", "Crear Integración")}
        </h1>
        <p className="text-muted-foreground mt-2">
          {t("governance.data.integrations.createDescription", "Configura una nueva integración de datos")}
        </p>
      </div>

      {/* Línea 2: Botón Volver (izquierda) y Botones de Acción (derecha) */}
      <div className="flex items-center justify-between">
        <Button variant="ghost" onClick={() => router.push("/governance/data/integrations")}>
          <ArrowLeft className="mr-2 h-4 w-4" />
          {t("common.back", "Volver")}
        </Button>
        <div className="flex gap-2">
          <Button type="submit" form="integration-form" disabled={saving}>
            {saving ? (
              <>
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                Guardando...
              </>
            ) : (
              <>
                <Save className="mr-2 h-4 w-4" />
                Guardar
              </>
            )}
          </Button>
        </div>
      </div>

      {/* Formulario */}
      <form id="integration-form" onSubmit={handleSubmit}>
        {/* Contenido del formulario */}
      </form>
    </div>
  );
}
```

##### Reglas de Diseño

1. **Estructura de Dos Líneas:**
   - ✅ **OBLIGATORIO:** Primera línea con título y subtítulo
   - ✅ **OBLIGATORIO:** Segunda línea con botón "Volver" a la izquierda y botones de acción a la derecha
   - ✅ Usar `flex items-center justify-between` para la segunda línea

2. **Botón Volver:**
   - ✅ **OBLIGATORIO:** Debe estar a la izquierda de la segunda línea
   - ✅ **OBLIGATORIO:** Debe incluir icono `ArrowLeft` de `lucide-react`
   - ✅ **OBLIGATORIO:** Debe incluir texto "Volver" (traducido)
   - ✅ Usar `variant="ghost"` para estilo minimalista

3. **Botones de Acción:**
   - ✅ **OBLIGATORIO:** Deben estar a la derecha de la segunda línea
   - ✅ Usar `type="submit"` con `form="[form-id]"` para enlazar con el formulario
   - ✅ Mostrar estado de carga con spinner cuando `saving === true`
   - ✅ Incluir iconos apropiados (Save, Plus, etc.)

4. **Formulario:**
   - ✅ **OBLIGATORIO:** El formulario debe tener un `id` único
   - ✅ Los botones de acción deben usar `form="[form-id]"` para enlazar con el formulario

5. **Espaciado:**
   - ✅ Usar `space-y-6` en el contenedor principal para separar las líneas
   - ✅ Usar `gap-2` en el contenedor de botones de acción

##### Pantallas Afectadas

Este estándar se aplica a **todas** las pantallas de creación y edición:

- ✅ `/governance/data/integrations/create`
- ✅ `/governance/data/integrations/[id]/edit`
- ✅ `/governance/data/origins/create`
- ✅ `/governance/data/origins/[id]/edit`
- ✅ `/governance/data/datasets/create`
- ✅ Cualquier otra pantalla de creación/edición futura

##### Importaciones Necesarias

```typescript
import { Button } from "@/components/ui/button";
import { ArrowLeft, Save, Loader2 } from "lucide-react";
import { useRouter } from "next/navigation";
import { useTranslation } from "@/app/config/i18n";
```

#### Ventanas Emergentes (Diálogos)

**ESTÁNDAR OBLIGATORIO:** Todas las ventanas emergentes (diálogos) deben seguir esta estructura y diseño:

##### Estructura Card (Header, Body, Footer)

Todas las ventanas emergentes deben usar la estructura de Card con tres secciones claramente definidas:

```typescript
<Dialog open={isOpen} onOpenChange={setIsOpen}>
  <DialogContent className="max-w-2xl p-0">
    <Card className="border-0 shadow-none">
      {/* HEADER */}
      <CardHeader className="flex flex-row items-center justify-between pb-4">
        {/* Título y descripción a la izquierda */}
        <div className="flex-1">
          <CardTitle>
            Título del Diálogo
          </CardTitle>
          {/* Descripción opcional */}
          <p className="text-sm text-muted-foreground mt-2">
            Descripción del diálogo
          </p>
        </div>
        {/* Botón X de cerrar a la derecha */}
        <Button
          variant="ghost"
          size="icon"
          className="h-6 w-6"
          onClick={() => setIsOpen(false)}
        >
          <X className="h-4 w-4" />
        </Button>
      </CardHeader>

      {/* BODY */}
      <CardBody className="space-y-4">
        {/* Contenido principal del diálogo */}
        <div>
          <Label>Campo 1</Label>
          <Input />
        </div>
        {/* ... más campos ... */}
      </CardBody>

      {/* FOOTER */}
      <div className="border-t px-6 py-4 flex justify-end gap-2">
        <Button variant="outline" onClick={() => setIsOpen(false)}>
          Cancelar
        </Button>
        <Button onClick={handleSubmit}>
          Guardar
        </Button>
      </div>
    </Card>
  </DialogContent>
</Dialog>
```

##### Reglas de Diseño

1. **Estructura del Header:**
   - ✅ **OBLIGATORIO:** El header debe usar `flex flex-row items-center justify-between`
   - ✅ **OBLIGATORIO:** El título y descripción deben estar en un `div` con `flex-1` a la izquierda
   - ✅ **OBLIGATORIO:** El botón X de cerrar debe estar a la derecha del header
   - El título debe estar en `CardTitle` dentro del div izquierdo
   - La descripción opcional debe estar debajo del título con `text-sm text-muted-foreground mt-2`

2. **Icono X de Cerrar:**
   - ✅ **OBLIGATORIO:** Todas las ventanas emergentes deben tener un botón con icono X (`<X />` de `lucide-react`) en el lado derecho del header
   - Tamaño: `h-6 w-6` para el botón, `h-4 w-4` para el icono
   - Variante: `variant="ghost"` para estilo minimalista
   - Posición: A la derecha del header usando flexbox

3. **Márgenes y Espaciado:**
   - `DialogContent` debe tener `p-0` para eliminar padding por defecto
   - `CardHeader` y `CardBody` deben tener padding interno (`px-6 py-4` o similar)
   - Footer debe tener padding (`px-6 py-4`) y borde superior (`border-t`) para separación visual
   - Espaciado entre elementos: usar `space-y-4` o `space-y-6` según el contenido

4. **Estructura Card:**
   - ✅ **OBLIGATORIO:** Usar `Card` con `border-0 shadow-none` dentro de `DialogContent`
   - ✅ **OBLIGATORIO:** Dividir en `CardHeader`, `CardBody` y footer (div con border-t)
   - El título debe estar en `CardHeader` con `CardTitle` dentro de un div a la izquierda
   - El contenido principal debe estar en `CardBody`
   - Los botones de acción deben estar en el footer

##### Ejemplo Completo

```typescript
import { Dialog, DialogContent } from "@/components/ui/dialog";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { X } from "lucide-react";

export function ExampleDialog() {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <Dialog open={isOpen} onOpenChange={setIsOpen}>
      <DialogContent className="max-w-2xl p-0">
        <Card className="border-0 shadow-none">
          <CardHeader className="flex flex-row items-center justify-between pb-4">
            <div className="flex-1">
              <CardTitle>Crear Nuevo Elemento</CardTitle>
              <p className="text-sm text-muted-foreground mt-2">
                Complete el formulario para crear un nuevo elemento
              </p>
            </div>
            <Button
              variant="ghost"
              size="icon"
              className="h-6 w-6"
              onClick={() => setIsOpen(false)}
            >
              <X className="h-4 w-4" />
            </Button>
          </CardHeader>
          <CardBody className="space-y-4">
            {/* Contenido del formulario */}
          </CardBody>
          <div className="border-t px-6 py-4 flex justify-end gap-2">
            <Button variant="outline" onClick={() => setIsOpen(false)}>
              Cancelar
            </Button>
            <Button onClick={handleSubmit}>
              Guardar
            </Button>
          </div>
        </Card>
      </DialogContent>
    </Dialog>
  );
}
```

##### Importaciones Necesarias

```typescript
import { Dialog, DialogContent } from "@/components/ui/dialog";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { X } from "lucide-react";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
```

#### Clases de Botones

```typescript
// ✅ CORRECTO: Usar clases btn-* solo para botones
<button className="btn-primary">Guardar</button>

// ❌ INCORRECTO: No usar btn-* para spans o divs
<span className="btn-primary">Texto</span>
```

#### Ubicación de Botones de Acción

**REGLA OBLIGATORIA:** Todos los botones de acción (crear, editar, eliminar, guardar, cancelar, clasificar, etc.) deben estar ubicados en la **parte superior derecha** de la pantalla.

```typescript
// ✅ CORRECTO: Botones en la parte superior derecha
<div className="flex items-center justify-between">
  <div>
    <h1>Título de la Pantalla</h1>
    <p>Subtítulo</p>
  </div>
  <div className="flex gap-2">
    <Button variant="ghost" size="sm">Cancelar</Button>
    <Button size="sm">Guardar</Button>
  </div>
</div>

// ✅ CORRECTO: Botón flotante fijo en la parte superior derecha
{isAdmin && (
  <div className="fixed top-20 right-6 z-50">
    <Button
      onClick={handleCreate}
      className="h-10 w-10 p-0 rounded-full shadow-lg"
      title="Crear"
    >
      <Plus className="h-5 w-5" />
    </Button>
  </div>
)}

// ❌ INCORRECTO: Botones en la parte inferior
<div className="flex justify-end gap-2 mt-6">
  <Button>Cancelar</Button>
  <Button>Guardar</Button>
</div>
```

**Estilos de Botones de Acción:**
- Usar botones redondeados (`rounded-full`) para evitar apariencia de formulario web
- Eliminar bordes cuando sea apropiado (`border-0`)
- Usar estilos minimalistas (`variant="ghost"` para acciones secundarias)
- Botones principales con `bg-primary hover:bg-primary/90`

#### Espaciado

- Usar sistema de espaciado de Tailwind: `space-y-6`, `gap-4`, etc.
- Mantener consistencia en márgenes y padding

#### Colores

- Usar variables CSS para colores del tema
- Colores semánticos: `text-primary`, `bg-success`, etc.

---

## 🌐 INTERNACIONALIZACIÓN (i18n)

### Estructura Modular

Las traducciones están organizadas por módulo y por pantalla:

```
app/config/i18n/
├── modules/
│   ├── common.ts              # Traducciones comunes
│   ├── governance/
│   │   ├── dashboard.ts       # Dashboard de governance
│   │   ├── policies-overview.ts
│   │   ├── policy-detail.ts
│   │   ├── security.ts
│   │   ├── monitoring.ts
│   │   └── ...
│   └── ...
```

### Idiomas Soportados

- **es** - Español (idioma por defecto)
- **en** - Inglés
- **fr** - Francés
- **de** - Alemán
- **it** - Italiano
- **pt** - Portugués

### Uso en Componentes

```typescript
import { useTranslation } from '@/app/config/i18n';

export default function GovernanceDashboard() {
  const { t } = useTranslation();

  return (
    <div>
      <h1>{t('governance.dashboard.title')}</h1>
      <p>{t('governance.dashboard.subtitle')}</p>
    </div>
  );
}
```

### Agregar Nuevas Traducciones

1. Crear archivo en `app/config/i18n/modules/[modulo]/[pantalla].ts`
2. Exportar `TranslationModule` con todos los idiomas
3. Importar en `app/config/i18n/modules/[modulo]/index.ts`
4. Actualizar `app/config/i18n/index.ts` si es necesario

**Ver documentación completa:** `app/config/i18n/README.md`

---

## 🎭 SISTEMA DE MOCKS

### Configuración

#### Variable de Entorno

```bash
# .env.local
NEXT_PUBLIC_USE_MOCK=true
```

#### Configuración Centralizada

```typescript
// app/config/mock.ts
export const USE_MOCK = process.env.NEXT_PUBLIC_USE_MOCK === 'true';
```

### Cliente API con Soporte Mock

```typescript
// app/lib/api-client.ts
import { USE_MOCK } from '@/app/config/mock';

export async function apiCall<T>(
  endpoint: string,
  options?: RequestInit
): Promise<T> {
  if (USE_MOCK) {
    // Cargar mock data desde archivo
    const mockData = await import(`@/mocks${endpoint}.json`);
    return mockData.default as T;
  }

  const response = await fetch(`/api${endpoint}`, options);
  if (!response.ok) {
    throw new Error(`API Error: ${response.statusText}`);
  }
  return response.json();
}
```

### Estructura de Mock Data

```
app/mocks/
├── governance/
│   ├── dashboard.json
│   ├── policies/
│   │   ├── list.json
│   │   └── detail.json
│   └── ...
└── ...
```

### Uso en Componentes

```typescript
import { apiCall } from '@/lib/api-client';

export default async function GovernancePage() {
  const policies = await apiCall<Policy[]>('/governance/policies/list');
  // ... renderizar
}
```

### Desactivación de Mock (Operativa)

```bash
# Solo cambiar esta variable
NEXT_PUBLIC_USE_MOCK=false
```

**Objetivo:** Que cuando se ponga operativa solo haya que desactivar el mock sin revisar cada pantalla.

---

## 🔌 API Y COMUNICACIÓN

### API Routes (Next.js)

```
app/api/
├── governance/
│   ├── policies/
│   │   ├── route.ts          # GET /api/governance/policies
│   │   └── [id]/
│   │       └── route.ts       # GET/PUT/DELETE /api/governance/policies/[id]
│   └── ...
```

### Patrón de API Route

```typescript
// app/api/governance/policies/route.ts
import { NextRequest, NextResponse } from 'next/server';
import { USE_MOCK } from '@/app/config/mock';

export async function GET(request: NextRequest) {
  if (USE_MOCK) {
    const mockData = await import('@/mocks/governance/policies/list.json');
    return NextResponse.json(mockData.default);
  }

  // Llamada real a backend
  const response = await fetch('http://backend/api/governance/policies');
  return NextResponse.json(await response.json());
}
```

---

## 🏗️ ARQUITECTURA DE BACKEND Y MICROSERVICIOS DE NEGOCIO

### Stack Tecnológico del Backend

**Versiones:**
- **Spring Boot:** `3.5.8` (última versión estable)
- **Java:** `17+`
- **Spring WebFlux:** Para microservicios reactivos
- **Spring Data JPA:** Para acceso a datos
- **Resilience4j:** `2.1.0` para circuit breakers y retry

**Nota sobre configuración YML:**
- ❌ **NO usar** `@project.version@` en archivos `application.yml` (no soporta interpolación de Maven)
- ✅ **Usar** variables de entorno: `${APP_VERSION:1.0.0}` o valores directos

### Visión General

El backend sigue una arquitectura de microservicios reactivos donde cada módulo funcional tiene su propio microservicio de negocio. El frontend se comunica con estos microservicios a través del BFF (Backend for Frontend).

```
Frontend (Next.js)
    ↓ HTTP/WebClient
BFF (codeflowx.govern.bff.compliance) [Reactivo - WebFlux]
    ↓ HTTP/WebClient (Reactivo) - URLs configuradas en application.yml bajo services:
Microservicios de Negocio [Reactivo - WebFlux]
    ↓ Mono.fromCallable() (envuelve llamadas síncronas)
Servicios de Negocio (codeflowx.govern.business) [Síncrono]
    ↓ Usa directamente Repositorios JPA (o JdbcTemplate para SQL nativo)
    ↓ ProcessRuntimeService (dispara workflows BPMN)
Repositorios (codeflowx.govern.repository) [Spring Data JPA]
    ↓
Entidades JPA (nocode.service.entitys)
```

### Arquitectura de Acceso a Datos

**IMPORTANTE:** Los servicios de negocio trabajan **únicamente** con:
1. **Repositorios JPA** (`codeflowx.govern.repository`) - Para operaciones estándar
2. **JdbcTemplate** - Solo si necesitan SQL nativo o queries complejas que no se pueden hacer con JPA

**NO usar:**
- ❌ DAO (patrón antiguo de EnArt)
- ❌ Acceso directo a EntityManager (excepto casos muy específicos)
- ❌ Queries nativas en los servicios de negocio (usar repositorios o JdbcTemplate)

### Separación de Capas: DTOs vs Entidades

**REGLA FUNDAMENTAL:** Las entidades JPA **NUNCA** deben salir de los servicios de negocio. Toda comunicación entre capas se hace mediante DTOs.

#### En el BFF

**IMPORTANTE:** El BFF **SOLO** trabaja con DTOs:
- ✅ Los controladores del BFF reciben DTOs en los request bodies
- ✅ Los controladores del BFF retornan DTOs en las respuestas
- ✅ Los servicios del BFF trabajan exclusivamente con DTOs
- ❌ **NUNCA** se exponen entidades JPA en el BFF
- ❌ **NUNCA** se importan clases de entidades en el BFF

**REGLA CRÍTICA - Respuestas de API:**
- ✅ **SIEMPRE** se debe retornar un DTO tipado en las respuestas de API
- ✅ Si necesitas retornar un `Map`, debe estar **dentro de un DTO** (como campo del DTO)
- ❌ **NUNCA** retornar `Map<String, Object>` directamente como tipo de retorno
- ❌ **NUNCA** retornar `Mono<Map<String, Object>>` o `ResponseEntity<Map<String, Object>>`
- ❌ **NUNCA** usar `Mono<Map<String, Object>>` en servicios del BFF ni en controladores
- ❌ **NUNCA** usar `List<Map<String, Object>>` en respuestas
- ✅ **SIEMPRE** crear DTOs específicos para cada respuesta (ej: `TelemetryKpisResponseDto`, `TelemetryEventsResponseDto`)

**Ejemplo correcto:**
```java
// ✅ CORRECTO: Retornar DTO tipado
@PostMapping("/block")
public Mono<ResponseEntity<ProhibitedSystemActionResponseDto>> blockDeployment(
        @RequestBody ProhibitedSystemRequestDto request) {
    return prohibitedSystemService.blockDeployment(request.getProjectId(), request.getReason())
        .map(ResponseEntity::ok);
}

// ✅ CORRECTO: Si necesitas un Map, está dentro de un DTO
public class ProhibitedSystemDetectionDetailDto {
    private Long id;
    private Long projectId;
    private Map<String, Object> additionalInfo;  // ✅ Map dentro del DTO
    // ...
}
```

**Ejemplo incorrecto:**
```java
// ❌ INCORRECTO: Retornar Map directamente
@PostMapping("/block")
public Mono<ResponseEntity<Map<String, Object>>> blockDeployment(...) {
    // ❌ NO hacer esto
}

// ❌ INCORRECTO: Retornar Map en el servicio
public Mono<Map<String, Object>> blockDeployment(...) {
    // ❌ NO hacer esto
}
```

**Ejemplo correcto en el BFF:**
```java
@RestController
@RequestMapping("/api/v1/compliance/classification")
@RequiredArgsConstructor
@Slf4j
public class ClassificationController {

    private final ClassificationService classificationService;  // Servicio del BFF

    @PostMapping("/suggest")
    public Mono<ResponseEntity<List<CategorySuggestionResponseDto>>> suggestCategories(
            @Valid @RequestBody CategorySuggestionRequestDto request) {  // ✅ Recibe DTO

        return classificationService.suggestCategories(request)
            .map(ResponseEntity::ok);  // ✅ Retorna DTO
    }
}
```

#### En los Microservicios de Negocio

**IMPORTANTE:** Los controladores de los microservicios de negocio **SOLO** trabajan con DTOs:
- ✅ Los controladores reciben DTOs en los request bodies
- ✅ Los controladores retornan DTOs en las respuestas
- ✅ Los controladores convierten DTOs a entidades antes de llamar a servicios de negocio
- ✅ Los controladores convierten entidades a DTOs antes de retornar respuestas
- ❌ **NUNCA** se exponen entidades JPA directamente en los controladores
- ❌ **NUNCA** se reciben entidades JPA en los endpoints

**REGLA CRÍTICA - Respuestas de API:**
- ✅ **SIEMPRE** se debe retornar un DTO tipado en las respuestas de API
- ✅ Si necesitas retornar un `Map`, debe estar **dentro de un DTO** (como campo del DTO)
- ❌ **NUNCA** retornar `Map<String, Object>` directamente como tipo de retorno
- ❌ **NUNCA** retornar `Mono<Map<String, Object>>` o `ResponseEntity<Map<String, Object>>`
- ❌ **NUNCA** usar `Mono<Map<String, Object>>` en servicios del BFF ni en controladores
- ❌ **NUNCA** usar `List<Map<String, Object>>` en respuestas
- ✅ **SIEMPRE** crear DTOs específicos para cada respuesta (ej: `TelemetryKpisResponseDto`, `TelemetryEventsResponseDto`)

**Ejemplo correcto en microservicio de negocio:**
```java
@RestController
@RequestMapping("/api/v1/classification")
@RequiredArgsConstructor
@Slf4j
public class ClassificationController {

    private final AnnexIIICategoryBusinessService categoryBusinessService;

    @GetMapping("/categories/{code}")
    public Mono<ResponseEntity<AnnexIIICategoryDto>> getCategoryByCode(
            @PathVariable String code) {

        return Mono.fromCallable(() -> categoryBusinessService.getCategoryByCode(code))
            .subscribeOn(Schedulers.boundedElastic())
            .flatMap(category -> {
                if (category == null) {
                    return Mono.just(ResponseEntity.<AnnexIIICategoryDto>notFound().build());
                }
                // ✅ Convertir entidad a DTO antes de retornar
                return Mono.just(ResponseEntity.ok(toDto(category)));
            });
    }

    @PostMapping("/categories")
    public Mono<ResponseEntity<AnnexIIICategoryDto>> createCategory(
            @Valid @RequestBody AnnexIIICategoryDto dto) {  // ✅ Recibe DTO

        return Mono.fromCallable(() -> {
                // ✅ Convertir DTO a entidad antes de llamar al servicio de negocio
                AnnexIIICategory entity = toEntity(dto);
                AnnexIIICategory saved = categoryBusinessService.save(entity);
                // ✅ Convertir entidad a DTO antes de retornar
                return toDto(saved);
            })
            .subscribeOn(Schedulers.boundedElastic())
            .map(ResponseEntity::ok);
    }

    // ✅ Métodos de conversión DTO ↔ Entidad
    private AnnexIIICategoryDto toDto(AnnexIIICategory category) {
        AnnexIIICategoryDto dto = new AnnexIIICategoryDto();
        dto.setIdxannexiiicategory(category.getIdxannexiiicategory());
        dto.setAnncategorycode(category.getAnncategorycode());
        // ... mapear todos los campos
        return dto;
    }

    private AnnexIIICategory toEntity(AnnexIIICategoryDto dto) {
        AnnexIIICategory entity = new AnnexIIICategory();
        entity.setAnncategorycode(dto.getAnncategorycode());
        // ... mapear todos los campos
        return entity;
    }
}
```

#### En los Servicios de Negocio

**IMPORTANTE:** Los servicios de negocio trabajan con entidades JPA internamente:
- ✅ Los servicios de negocio reciben y retornan entidades JPA
- ✅ Los servicios de negocio NO conocen DTOs
- ✅ La conversión DTO ↔ Entidad se hace en los controladores

**Ejemplo correcto en servicio de negocio:**
```java
@Service
@Slf4j
public class AnnexIIICategoryBusinessService {

    @Autowired
    private AnnexIIICategoryRepository repository;

    // ✅ Método recibe y retorna entidad JPA
    public AnnexIIICategory getCategoryByCode(String code) {
        return repository.findByAnncategorycode(code)
            .orElse(null);
    }

    // ✅ Método recibe y retorna entidad JPA
    public AnnexIIICategory save(AnnexIIICategory category) {
        return repository.save(category);
    }
}
```

#### Ubicación de DTOs

**Todos los DTOs están centralizados en:**
- **Módulo:** `codeflowx.govern.nocode.dtos`
- **Ubicación:** `nocode.service/codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/`
- **Estructura:** Organizados por módulo funcional (catalogs, compliance, governance, projects, etc.)

**IMPORTANTE:**
- ✅ **Reutilizar DTOs existentes** de `codeflowx.govern.nocode.dtos`
- ❌ **NO crear DTOs** en el BFF ni en los microservicios
- ❌ **NO duplicar DTOs** - si falta uno, crearlo en el módulo compartido

### Configuración de URLs en el BFF

Las URLs de los microservicios de negocio se configuran en el `application.yml` del BFF bajo la sección `services:`:

```yaml
# codeflowx.govern.bff.compliance/src/main/resources/application.yml
services:
  classification:
    base-url: ${CLASSIFICATION_SERVICE_BASE_URL:http://localhost:8097}
  fria:
    base-url: ${FRIA_SERVICE_BASE_URL:http://localhost:8098}
  immutable-logs:
    base-url: ${IMMUTABLE_LOGS_SERVICE_BASE_URL:http://localhost:8095}
  hitl:
    base-url: ${HITL_SERVICE_BASE_URL:http://localhost:8099}
  technical-docs:
    base-url: ${TECHNICAL_DOCS_SERVICE_BASE_URL:http://localhost:8100}
  # ... más servicios
```

**Ubicación:** `codeflowx.govern.bff.compliance/src/main/resources/application.yml`

### Llamadas a BPMN

**IMPORTANTE:** Las llamadas a BPMN se hacen **desde los servicios de negocio**, NO desde el BFF ni desde los microservicios.

**Arquitectura:**
- Los servicios de negocio usan `BpmnWorkflowClient` (cliente HTTP)
- El cliente llama al microservicio BPMN (`codeflowx.govern.workflow.engine`)
- El microservicio BPMN expone REST API: `POST /api/v1/processes/{processKey}/start`

**Patrón en Servicios de Negocio:**

```java
// codeflowx.govern.business/src/main/java/.../MyBusinessService.java
@Service
public class MyBusinessService {

    @Autowired(required = false)  // Opcional: puede no estar disponible
    private BpmnWorkflowClient bpmnWorkflowClient;

    public MyResult performAction(Long projectId) {
        // 1. Lógica de negocio
        // ...

        // 2. Disparar workflow BPMN si es necesario
        String workflowInstanceId = null;
        if (bpmnWorkflowClient != null && bpmnWorkflowClient.isAvailable()) {
            try {
                Map<String, Object> variables = Map.of(
                    "projectId", projectId,
                    "action", "classification"
                );

                workflowInstanceId = bpmnWorkflowClient.startProcess(
                    "my-workflow-process",
                    variables
                );

                if (workflowInstanceId != null) {
                    log.info("Workflow BPMN disparado: workflowInstanceId={}", workflowInstanceId);
                }
            } catch (Exception e) {
                log.error("Error disparando workflow BPMN", e);
                // No lanzar excepción: no fallar la operación si el workflow falla
            }
        } else {
            log.warn("BpmnWorkflowClient no disponible. Workflow BPMN no se disparará.");
        }

        // 3. Retornar resultado con workflowInstanceId
        MyResult result = new MyResult();
        result.setWorkflowInstanceId(workflowInstanceId);
        return result;
    }
}
```

**Cliente BPMN:**
- **Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/client/BpmnWorkflowClient.java`
- **Configuración:** URL del servicio BPMN vía `codeflowx.bpmn.service.base-url` o `BPMN_SERVICE_BASE_URL`
- **Habilitación:** `codeflowx.bpmn.service.enabled=true` (default)

**Dependencias en servicios de negocio:**

```xml
<!-- codeflowx.govern.business/pom.xml -->
<!-- WebClient ya está incluido para llamadas a microservicios -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
    <optional>true</optional>
</dependency>
```

**Nota:** El `BpmnWorkflowClient` se inyecta como `@Autowired(required = false)` porque puede no estar disponible en todos los contextos. Si no está disponible, el servicio de negocio debe continuar funcionando sin disparar el workflow.

### Integración con Microservicios de Python

**IMPORTANTE:** Tanto los **servicios de negocio** (`codeflowx.govern.business`) como el **BFF** (`codeflowx.govern.bff.compliance`) pueden invocar microservicios de Python (métricas, evaluación, chat, interpreter, etc.) usando los clientes disponibles en el módulo `codeflowx.govern.nocode.client`.

**Casos de uso:**
- **Servicios de Negocio**: Para análisis, evaluación y procesamiento que requiere persistencia en PostgreSQL
- **BFF**: Para funcionalidades que no requieren persistencia o que necesitan respuesta directa al frontend (ej: interpretación de resultados, explicaciones, resúmenes ejecutivos)

#### Clientes Disponibles

El módulo `codeflowx.govern.nocode.client` proporciona clientes especializados para cada microservicio de Python:

- **LLMEvaluationClient**: Evaluación de LLMs (hallucinaciones, toxicidad, calidad)
- **PromptGovernanceClient**: Gobernanza de prompts (seguridad, compliance)
- **RAGEvaluationClient**: Evaluación de sistemas RAG (precisión, recall)
- **AgentMonitoringClient**: Monitoreo de agentes (ejecución, costos, loops)
- **ModelWrapperClient**: Invocación de modelos (OpenAI, Azure, etc.)
- **AIInterpreterClient**: Interpretación de resultados técnicos (explicaciones, resúmenes ejecutivos)
- **BiasDetectionClient**: Detección de sesgos en modelos
- **DeepfakeDetectionClient**: Detección de deepfakes
- **AdversarialRobustnessClient**: Robustez adversarial (ataques, evasión)
- **FRIAGeneratorClient**: Generación de FRIA (Factual Risk Impact Assessment)
- **EUDeclarationGeneratorClient**: Generación de declaraciones de conformidad EU AI Act
- **ISO42001AnnexAAssessorClient**: Evaluación según ISO 42001 Anexo A
- **BoardGovernanceClient**: Gobernanza a nivel de board (métricas, health scores)
- **ConformityAssessmentClient**: Evaluación de conformidad
- **MultiFrameworkComplianceClient**: Compliance multi-framework
- **ServingWrapperClient**: Wrapper para modelos en producción
- **CopyrightComplianceClient**: Compliance de copyright
- **EthicsRiskAssessorClient**: Evaluación de riesgos éticos

#### Configuración

**Ubicación del módulo:** `nocode.service/codeflowx.govern.nocode.client/`

**Dependencia Maven:**
```xml
<dependency>
    <groupId>codeflowx.govern</groupId>
    <artifactId>codeflowx.govern.nocode.client</artifactId>
    <version>1.0.0</version>
</dependency>
```

**Configuración en `application.yml`:**
```yaml
codeflowx:
  leka:
    governance:
      enabled: true
      gateway:
        # URL del Gateway Spring Cloud que enruta a microservicios Python
        url: ${LEKA_GOVERNANCE_GATEWAY_URL:http://api-leka-govern:8000}
      timeout: ${LEKA_GOVERNANCE_TIMEOUT:30000}
      webclient:
        max-in-memory-size: 10485760  # 10MB
      retry:
        enabled: true
        max-attempts: 2
        backoff-delay: 1000
```

**URLs soportadas:**
- Kubernetes (mismo namespace): `http://api-leka-govern:8000`
- Kubernetes (namespace diferente): `http://api-leka-govern.{namespace}.svc.cluster.local:8000`
- Desarrollo local: `http://localhost:8000`

#### Uso en Servicios de Negocio

**Cuándo usar:** Cuando necesitas análisis, evaluación o procesamiento que requiere persistencia en PostgreSQL.

**IMPORTANTE:** Los servicios de negocio son **síncronos** y deben usar **anotaciones de Resilience4j** (`@CircuitBreaker`, `@Retry`, `@TimeLimiter`) para proteger las llamadas a microservicios de Python.

**Patrón de uso con Resilience4j:**

```java
@Service
@Slf4j
public class FriaAssessmentBusinessService {

    @Autowired
    private AIGovernanceClient aiGovernanceClient;  // Cliente principal (factoría)

    @Autowired
    private FriaAssessmentRepository repository;

    /**
     * Ejemplo: Validación cruzada FRIA con métricas técnicas
     * ✅ Método síncrono - Usar anotaciones de Resilience4j
     */
    @CircuitBreaker(name = "aiGovernanceService", fallbackMethod = "crossValidateFallback")
    @Retry(name = "aiGovernanceService")
    @TimeLimiter(name = "aiGovernanceService")
    public CrossValidationResult crossValidate(Long friaId) {
        log.info("Cross-validating FRIA with technical metrics: {}", friaId);

        FriaAssessment fria = repository.findById(friaId)
            .orElseThrow(() -> new IllegalArgumentException("FRIA not found: " + friaId));

        // Verificar si el cliente está disponible
        if (aiGovernanceClient == null) {
            log.warn("AIGovernanceClient no está disponible");
            throw new IllegalStateException("Servicio de validación cruzada no disponible");
        }

        try {
            // 1. Convertir entidad a DTO para el cliente
            FriaDataDTO friaData = convertToFriaDataDTO(fria);

            // 2. Llamar microservicio Python usando cliente especializado
            CrossValidationResult result = aiGovernanceClient
                .friaGenerator()  // Obtener cliente especializado
                .crossValidate(friaData);  // Llamar método específico

            // 3. Persistir resultado en la entidad
            if (result != null) {
                String resultJson = objectMapper.writeValueAsString(result);
                fria.setFriacrossvalidationresult(resultJson);
                fria.setFriaupdatedat(new Timestamp(System.currentTimeMillis()));
                repository.save(fria);

                log.info("Cross-validation completed: consistencyScore={}, isConsistent={}",
                    result.getConsistencyScore(), result.getIsConsistent());
            }

            return result;

        } catch (ServiceUnavailableException e) {
            log.error("Microservicio de Python no disponible", e);
            throw new IllegalStateException("Servicio de validación cruzada no disponible temporalmente");
        } catch (AIGovernanceException e) {
            log.error("Error en microservicio de Python: service={}, status={}",
                e.getService(), e.getStatusCode(), e);
            throw new RuntimeException("Error al validar FRIA: " + e.getMessage());
        }
    }

    /**
     * Método fallback para Circuit Breaker
     */
    private CrossValidationResult crossValidateFallback(Long friaId, Throwable t) {
        log.warn("Fallback ejecutado para cross-validation: friaId={}, error={}", friaId, t.getMessage());
        throw new IllegalStateException("Servicio de validación cruzada no disponible temporalmente");
    }

    /**
     * Ejemplo: Generar FRIA completo usando FRIA Generator Client
     */
    @CircuitBreaker(name = "aiGovernanceService", fallbackMethod = "generateFRIAFallback")
    @Retry(name = "aiGovernanceService")
    @TimeLimiter(name = "aiGovernanceService")
    public FRIAResponse generateFRIA(FRIARequest request) {
        log.info("Generating FRIA for system: {}", request.getAiSystemInfo().getName());

        try {
            // Llamar microservicio Python para generar FRIA
            FRIAResponse response = aiGovernanceClient
                .friaGenerator()
                .generateAssessment(request);

            log.info("FRIA generated successfully: friaId={}, completenessScore={}",
                response.getFriaId(), response.getCompletenessScore());

            return response;

        } catch (ServiceUnavailableException e) {
            log.error("FRIA Generator service unavailable", e);
            throw new IllegalStateException("Servicio de generación FRIA no disponible temporalmente");
        } catch (AIGovernanceException e) {
            log.error("Error generating FRIA: service={}, status={}",
                e.getService(), e.getStatusCode(), e);
            throw new RuntimeException("Error al generar FRIA: " + e.getMessage());
        }
    }

    private FRIAResponse generateFRIAFallback(FRIARequest request, Throwable t) {
        log.warn("Fallback ejecutado para generación FRIA: error={}", t.getMessage());
        throw new IllegalStateException("Servicio de generación FRIA no disponible temporalmente");
    }
}
```

**Configuración de Resilience4j en `application.yml`:**

```yaml
resilience4j:
  circuitbreaker:
    instances:
      aiGovernanceService:  # Nombre debe coincidir con @CircuitBreaker(name = "...")
        registerHealthIndicator: true
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        permittedNumberOfCallsInHalfOpenState: 3
        automaticTransitionFromOpenToHalfOpenEnabled: true
        waitDurationInOpenState: 10s
        failureRateThreshold: 50
  retry:
    instances:
      aiGovernanceService:  # Nombre debe coincidir con @Retry(name = "...")
        maxAttempts: 3
        waitDuration: 1s
        retryExceptions:
          - java.net.ConnectException
          - java.util.concurrent.TimeoutException
  timelimiter:
    instances:
      aiGovernanceService:  # Nombre debe coincidir con @TimeLimiter(name = "...")
        timeoutDuration: 30s  # Timeout para operaciones de Python (pueden tardar más)
```

**Patrón Factory - Obtener Cliente Especializado:**

El `AIGovernanceClient` actúa como **factoría** que retorna clientes especializados:

```java
@Autowired
private AIGovernanceClient aiGovernanceClient;

// Cada método retorna un cliente especializado para un microservicio específico
FRIAGeneratorClient friaClient = aiGovernanceClient.friaGenerator();
LLMEvaluationClient llmClient = aiGovernanceClient.llmEvaluation();
PromptGovernanceClient promptClient = aiGovernanceClient.promptGovernance();
AIInterpreterClient interpreterClient = aiGovernanceClient.aiInterpreter();
BiasDetectionClient biasClient = aiGovernanceClient.biasDetection();
// ... y más clientes disponibles
```

**Ventajas del patrón Factory:**
- ✅ Un solo punto de configuración (`AIGovernanceClient`)
- ✅ Lazy initialization (clientes se crean solo cuando se usan)
- ✅ Código más limpio y fácil de entender
- ✅ Fácil descubrimiento de endpoints disponibles

#### Uso en el BFF

**Cuándo usar:** Para funcionalidades que no requieren persistencia o que necesitan respuesta directa al frontend, como:
- **AI Interpreter**: Explicar resultados técnicos, generar resúmenes ejecutivos, responder preguntas
- **Model Wrapper**: Invocar modelos LLM para funcionalidades en tiempo real
- **Prompt Governance**: Validar prompts antes de enviarlos al frontend
- **Otros servicios**: Cualquier funcionalidad que no requiera persistencia en base de datos

**IMPORTANTE:** El BFF es **reactivo** (retorna `Mono<T>`). Las llamadas a los clientes de Python son **síncronas** (bloqueantes), por lo que deben envolverse en `Mono.fromCallable()` con `Schedulers.boundedElastic()`.

**Patrón de uso en el BFF:**

```java
@RestController
@RequestMapping("/api/v1/compliance")
@RequiredArgsConstructor
@Slf4j
public class ComplianceController {

    @Autowired
    private AIGovernanceClient aiGovernanceClient;  // Cliente principal (factoría)

    /**
     * Ejemplo: Usar AI Interpreter para explicar resultados técnicos
     * ✅ Método reactivo - Envolver llamada síncrona en Mono.fromCallable()
     */
    @PostMapping("/explain-result")
    public Mono<ResponseEntity<ExplanationResponse>> explainResult(
            @RequestBody ExplainRequest request) {

        log.info("Request to explain result: context={}", request.getContext());

        return Mono.fromCallable(() -> {
                try {
                    // Llamar AI Interpreter usando el cliente (llamada síncrona bloqueante)
                    ExplanationResponse response = aiGovernanceClient
                        .aiInterpreter()  // Obtener cliente especializado
                        .explainResult(request);  // Llamar método específico

                    log.info("AI Interpreter explanation generated successfully");
                    return ResponseEntity.ok(response);

                } catch (ServiceUnavailableException e) {
                    log.error("AI Interpreter service unavailable", e);
                    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .build();
                } catch (AIGovernanceException e) {
                    log.error("Error calling AI Interpreter: service={}, status={}",
                        e.getService(), e.getStatusCode(), e);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build();
                }
            })
            .subscribeOn(Schedulers.boundedElastic());  // ✅ Ejecutar en thread pool para operaciones bloqueantes
    }

    /**
     * Ejemplo: Usar Model Wrapper para invocar un LLM desde el BFF
     */
    @PostMapping("/generate-summary")
    public Mono<ResponseEntity<ModelInvokeResponse>> generateSummary(
            @RequestBody ModelInvokeRequest request) {

        log.info("Request to generate summary: model={}", request.getProvider());

        return Mono.fromCallable(() -> {
                try {
                    ModelInvokeResponse response = aiGovernanceClient
                        .modelWrapper()
                        .invoke(request);

                    return ResponseEntity.ok(response);

                } catch (ServiceUnavailableException e) {
                    log.error("Model Wrapper service unavailable", e);
                    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .build();
                } catch (AIGovernanceException e) {
                    log.error("Error calling Model Wrapper: service={}, status={}",
                        e.getService(), e.getStatusCode(), e);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build();
                }
            })
            .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Ejemplo: Usar FRIA Generator para generar PDF de FRIA
     */
    @PostMapping("/fria/generate-pdf")
    public Mono<ResponseEntity<byte[]>> generateFriaPdf(
            @RequestBody FRIARequest request) {

        log.info("Request to generate FRIA PDF: system={}",
            request.getAiSystemInfo().getName());

        return Mono.fromCallable(() -> {
                try {
                    // Generar PDF directamente como array de bytes
                    byte[] pdfBytes = aiGovernanceClient
                        .friaGenerator()
                        .generateAssessmentPdf(request);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDispositionFormData("attachment", "fria-assessment.pdf");

                    return ResponseEntity.ok()
                        .headers(headers)
                        .body(pdfBytes);

                } catch (ServiceUnavailableException e) {
                    log.error("FRIA Generator service unavailable", e);
                    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .build();
                } catch (AIGovernanceException e) {
                    log.error("Error generating FRIA PDF: service={}, status={}",
                        e.getService(), e.getStatusCode(), e);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build();
                }
            })
            .subscribeOn(Schedulers.boundedElastic());
    }
}
```

**Nota sobre Resilience4j en el BFF:**

Aunque los clientes de Python son síncronos, cuando se usan desde el BFF (reactivo), **NO se pueden usar anotaciones de Resilience4j** porque las anotaciones requieren AOP que no funciona bien con tipos reactivos. En su lugar:

- ✅ **Opción 1:** Usar Resilience4j en el servicio de negocio (si la llamada se hace desde ahí)
- ✅ **Opción 2:** Manejar errores con `try-catch` y retornar códigos HTTP apropiados (como en los ejemplos arriba)
- ✅ **Opción 3:** Si necesitas circuit breaker en el BFF, usar operadores reactivos de Resilience4j (ver sección "Resiliencia con Resilience4j")

**Configuración en el BFF:**

1. **Agregar dependencia en `pom.xml`:**
```xml
<dependency>
    <groupId>codeflowx.govern</groupId>
    <artifactId>codeflowx.govern.nocode.client</artifactId>
    <version>1.0.0</version>
</dependency>
```

2. **Configurar en `application.yml`:**
```yaml
codeflowx:
  leka:
    governance:
      enabled: true
      gateway:
        url: ${LEKA_GOVERNANCE_GATEWAY_URL:http://api-leka-govern:8000}
      timeout: ${LEKA_GOVERNANCE_TIMEOUT:30000}
```

3. **El cliente se auto-configura:** El `AIGovernanceClient` se configura automáticamente mediante `AIGovernanceClientConfiguration` del módulo cliente.

**Cliente principal (AIGovernanceClient):**

El `AIGovernanceClient` actúa como **factoría** que retorna clientes especializados:

```java
@Autowired
private AIGovernanceClient aiGovernanceClient;

// Cada método retorna un cliente especializado
aiGovernanceClient.llmEvaluation()        // → LLMEvaluationClient
aiGovernanceClient.promptGovernance()    // → PromptGovernanceClient
aiGovernanceClient.ragEvaluation()      // → RAGEvaluationClient
aiGovernanceClient.agentMonitoring()    // → AgentMonitoringClient
aiGovernanceClient.modelWrapper()       // → ModelWrapperClient
aiGovernanceClient.aiInterpreter()      // → AIInterpreterClient
aiGovernanceClient.biasDetection()      // → BiasDetectionClient
// ... y más
```

#### Guías de Uso

**TODOS los clientes tienen guías de uso detalladas** en el módulo `codeflowx.govern.nocode.client`:

**Ubicación:** `nocode.service/codeflowx.govern.nocode.client/`

**Guías disponibles:**
- `LLMEVALUATION_CLIENT_GUIDE.md` - Guía de uso de LLM Evaluation
- `PROMPT_GOVERNANCE_CLIENT_GUIDE.md` - Guía de uso de Prompt Governance
- `RAGEVALUATION_CLIENT_USAGE.md` - Guía de uso de RAG Evaluation
- `AGENT_MONITORING_CLIENT_GUIDE.md` - Guía de uso de Agent Monitoring
- `MODEL_WRAPPER_CLIENT_GUIDE.md` - Guía de uso de Model Wrapper
- `AI_INTERPRETER_CLIENT_GUIDE.md` - Guía de uso de AI Interpreter
- `BIASDETECTION_CLIENT_GUIDE.md` - Guía de uso de Bias Detection
- `DEEPFAKEDETECTION_CLIENT_GUIDE.md` - Guía de uso de Deepfake Detection
- `ADVERSARIAL_ROBUSTNESS_CLIENT_GUIDE.md` - Guía de uso de Adversarial Robustness
- `FRIA_GENERATOR_CLIENT_GUIDE.md` - Guía de uso de FRIA Generator
- `EU_DECLARATION_GENERATOR_CLIENT_GUIDE.md` - Guía de uso de EU Declaration Generator
- `ISO42001_ANNEX_A_ASSESSOR_CLIENT_GUIDE.md` - Guía de uso de ISO 42001 Annex A Assessor
- `BOARD_GOVERNANCE_CLIENT_GUIDE.md` - Guía de uso de Board Governance
- `CONFORMITY_ASSESSMENT_CLIENT_USAGE.md` - Guía de uso de Conformity Assessment
- `MULTI_FRAMEWORK_COMPLIANCE_CLIENT_GUIDE.md` - Guía de uso de Multi-Framework Compliance
- `SERVING_WRAPPER_CLIENT_USAGE.md` - Guía de uso de Serving Wrapper
- `GUIA_USO_CLIENTE.md` - Guía general de uso del cliente
- `DEVELOPER_GUIDE.md` - Guía completa para desarrolladores
- `DEVELOPER_GUIDE_COMPLETE.md` - Guía completa extendida

**IMPORTANTE:** Antes de usar un cliente, **consultar su guía de uso** para ver:
- Endpoints disponibles
- Request/Response DTOs
- Ejemplos de código
- Configuración específica
- Manejo de errores

#### Arquitectura de Integración

**Desde Servicios de Negocio:**
```
Servicio de Negocio (codeflowx.govern.business)
    ↓ Inyecta AIGovernanceClient
    ↓ Llama a cliente especializado (ej: aiGovernanceClient.llmEvaluation())
    ↓ HTTP/WebClient (Reactivo)
    ↓ Gateway Spring Cloud (puerto 8000)
    ↓ Enruta a microservicio Python específico
Microservicio Python (métricas, evaluación, chat, interpreter, etc.)
    ↓ Retorna JSON response
    ↓
Servicio de Negocio persiste resultados en PostgreSQL vía JPA
```

**Desde el BFF:**
```
BFF (codeflowx.govern.bff.compliance)
    ↓ Inyecta AIGovernanceClient
    ↓ Llama a cliente especializado (ej: aiGovernanceClient.aiInterpreter())
    ↓ HTTP/WebClient (Reactivo)
    ↓ Gateway Spring Cloud (puerto 8000)
    ↓ Enruta a microservicio Python específico
Microservicio Python (interpreter, model-wrapper, etc.)
    ↓ Retorna JSON response
    ↓
BFF retorna respuesta directamente al frontend (sin persistencia)
```

#### Reglas y Mejores Prácticas

**✅ DO (Hacer):**
1. **Usar clientes del módulo:** Siempre usar clientes de `codeflowx.govern.nocode.client`
2. **Consultar guías:** Leer la guía de uso del cliente antes de implementar (ver sección "Guías de Uso")
3. **Manejo de errores:** Capturar `ServiceUnavailableException` y `AIGovernanceException`
4. **Logging:** Registrar llamadas y respuestas para debugging
5. **Persistencia:** Los servicios de negocio deben persistir resultados en PostgreSQL (el BFF no persiste)
6. **Inyección:** Inyectar `AIGovernanceClient` como dependencia normal (no opcional)
7. **BFF para interpretación:** Usar AI Interpreter desde el BFF para explicaciones y resúmenes que no requieren persistencia
8. **Resilience4j en Business Services:** Usar anotaciones (`@CircuitBreaker`, `@Retry`, `@TimeLimiter`) en métodos síncronos
9. **Envolver en Mono en BFF:** Usar `Mono.fromCallable()` con `Schedulers.boundedElastic()` para llamadas síncronas desde el BFF
10. **Patrón Factory:** Usar `aiGovernanceClient.clienteEspecializado()` para obtener clientes específicos

**❌ DON'T (No Hacer):**
1. **NO crear clientes propios:** Usar los clientes del módulo compartido
2. **NO hacer llamadas directas HTTP:** Usar siempre los clientes especializados
3. **NO persistir en el cliente:** El cliente es stateless, la persistencia es responsabilidad del servicio de negocio
4. **NO ignorar errores:** Manejar adecuadamente `ServiceUnavailableException` y otros errores
5. **NO usar anotaciones Resilience4j en métodos reactivos:** Las anotaciones solo funcionan con métodos síncronos
6. **NO bloquear threads reactivos:** Siempre usar `Schedulers.boundedElastic()` para operaciones bloqueantes en el BFF
7. **NO inyectar clientes especializados directamente:** Usar `AIGovernanceClient` como factoría

### Patrón Establecido: Microservicio de Clasificación

El módulo de **Clasificación** (`codeflowx-governance-classification-service`) sirve como **plantilla de referencia** para crear nuevos microservicios de negocio.

**Ubicación:** `nocode.service/codeflowx-governance-classification-service/`

#### Estructura del Microservicio

```
codeflowx-governance-classification-service/
├── pom.xml                                    # Dependencias: WebFlux, Business, Repository, DTOs
├── src/main/
│   ├── java/com/codeflowx/govern/classification/
│   │   ├── ClassificationServiceApplication.java  # App principal con @SpringBootApplication
│   │   ├── controller/
│   │   │   └── ClassificationController.java      # REST Controller reactivo (Mono<ResponseEntity<T>>)
│   │   ├── service/
│   │   │   └── AIClassificationService.java       # Servicios adicionales (IA, integraciones)
│   │   ├── config/
│   │   │   └── WebClientConfig.java               # Configuración de WebClient
│   │   └── exception/
│   │       └── GlobalExceptionHandler.java         # Manejo global de excepciones
│   └── resources/
│       └── application.yml                        # Configuración (WebFlux, JPA, Actuator)
└── README.md                                     # Documentación del microservicio
```

### Proceso para Crear Nuevos Microservicios

#### Paso 1: Revisar Documentación de Negocio

**Ubicaciones:**
- `docs/compliance/` - Requisitos de compliance por industria
- `docs/portal-backend/` - Especificaciones de módulos
- `docs/prompts/compliance/` - Prompts y análisis de cobertura

**Qué buscar:**
- Requisitos funcionales del módulo
- Endpoints necesarios
- Reglas de negocio
- Integraciones requeridas

#### Paso 2: Revisar Documentos de Auditoría

**Ubicaciones:**
- `nocode.service.entitys/src/main/resources/` - Scripts SQL y documentación
- `docs/compliance/IMLIMMUTABLELOGS_TIMESCALE_GUIDE.md` - Guía de logs inmutables

**Qué buscar:**
- Tablas relacionadas con el módulo
- Campos de auditoría requeridos
- Relaciones entre entidades

#### Paso 3: Revisar Entidades JPA y DTOs

**Entidades JPA:**
- Ubicación: `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/`
- Buscar entidades relacionadas con el módulo
- Verificar campos, relaciones y anotaciones JPA
- **IMPORTANTE:** Las entidades JPA solo se usan en servicios de negocio, nunca se exponen en controladores

**DTOs:**
- Ubicación: `codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/`
- **IMPORTANTE:** Reutilizar DTOs existentes, NO crear nuevos
- Si falta un DTO, crearlo en la librería compartida `codeflowx.govern.nocode.dtos`
- Los DTOs deben estar en el mismo paquete que las entidades relacionadas
- **REGLA:** BFF y controladores de microservicios SOLO trabajan con DTOs

**Estructura de DTOs:**
```
codeflowx.govern.nocode.dtos/
├── catalogs/              # Catálogos
├── compliance/            # Compliance
├── governance/            # Gobernanza
├── projects/              # Proyectos
└── ...
```

#### Paso 4: Crear Repositorio (si es necesario)

**Usar Repositorio Genérico si:**
- Solo necesita operaciones CRUD básicas
- No requiere queries complejas
- No necesita joins o agregaciones

```java
// Ejemplo: Usar GenericRepository directamente
// NO es necesario crear un repositorio, usar GenericRepository<MyEntity, Long> en el servicio
```

**Crear Repositorio Específico si:**
- Necesitas queries complejas con `@Query` (JPQL)
- Requieres joins o agregaciones
- Necesitas métodos personalizados con Spring Data JPA

```java
// Ejemplo: Repositorio específico
@Repository
public interface MyEntityRepository extends GenericRepository<MyEntity, Long> {
    @Query("SELECT e FROM MyEntity e WHERE e.status = :status")
    List<MyEntity> findByStatus(@Param("status") String status);

    // Queries más complejas
    @Query("SELECT e FROM MyEntity e JOIN e.relatedEntity r WHERE r.active = true")
    List<MyEntity> findActiveWithRelated();
}
```

**Para SQL Nativo:**
Si necesitas SQL nativo que no se puede hacer con JPQL, usar `JdbcTemplate` en el servicio de negocio (ver Paso 5).

**Ubicación:** `codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/`

#### Paso 5: Crear Servicio de Negocio

**Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/`

**Patrón con Repositorio JPA:**
```java
@Service
@Slf4j
public class MyEntityBusinessService {

    // Opción 1: Repositorio específico (si existe)
    @Autowired
    private MyEntityRepository repository;

    // Opción 2: GenericRepository (si no necesitas queries complejas)
    // @Autowired
    // private GenericRepository<MyEntity, Long> repository;

    @Autowired(required = false)  // Opcional: para BPMN
    private BpmnWorkflowClient bpmnWorkflowClient;

    // Opcional: Solo si necesitas SQL nativo
    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    public List<MyEntity> getAll() {
        log.info("Retrieving all entities");
        return repository.findAll();
    }

    public MyEntity save(MyEntity entity) {
        log.info("Saving entity: {}", entity.getId());
        return repository.save(entity);
    }

    public MyResult performAction(Long entityId) {
        // 1. Lógica de negocio usando repositorio
        Optional<MyEntity> entityOpt = repository.findById(entityId);
        if (entityOpt.isEmpty()) {
            throw new IllegalArgumentException("Entity not found: " + entityId);
        }

        MyEntity entity = entityOpt.get();
        // ... lógica de negocio ...
        entity = repository.save(entity);

        // 2. Disparar workflow BPMN si es necesario
        String workflowInstanceId = triggerWorkflowIfNeeded(entityId);

        // 3. Retornar resultado
        MyResult result = new MyResult();
        result.setEntity(entity);
        result.setWorkflowInstanceId(workflowInstanceId);
        return result;
    }

    // Ejemplo: Usar JdbcTemplate solo si necesitas SQL nativo
    public List<MyEntity> findWithNativeQuery(String condition) {
        if (jdbcTemplate == null) {
            throw new IllegalStateException("JdbcTemplate no disponible");
        }

        String sql = "SELECT * FROM MYENTITY WHERE " + condition;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            MyEntity entity = new MyEntity();
            entity.setId(rs.getLong("id"));
            // ... mapear campos ...
            return entity;
        });
    }

    private String triggerWorkflowIfNeeded(Long entityId) {
        if (bpmnWorkflowClient == null || !bpmnWorkflowClient.isAvailable()) {
            log.warn("BpmnWorkflowClient no disponible");
            return null;
        }

        try {
            Map<String, Object> variables = Map.of("entityId", entityId);
            return bpmnWorkflowClient.startProcess(
                "my-workflow-process",
                variables
            );
        } catch (Exception e) {
            log.error("Error disparando workflow BPMN", e);
            return null;  // No fallar la operación si el workflow falla
        }
    }
}
```

**Reglas:**
- ✅ Usar `@Service` y `@Slf4j`
- ✅ Inyectar repositorios JPA con `@Autowired` (específicos o GenericRepository)
- ✅ Usar `JdbcTemplate` **solo** si necesitas SQL nativo que no se puede hacer con JPA
- ✅ Inyectar `BpmnWorkflowClient` con `@Autowired(required = false)` si se necesita BPMN
- ✅ Agregar logging con `log.info()`, `log.warn()`, `log.error()`
- ✅ Los métodos deben ser **síncronos** (no reactivos)
- ✅ **Disparar workflows BPMN desde los servicios de negocio** usando `BpmnWorkflowClient`, no desde el BFF ni microservicios
- ❌ **NO usar DAO** (patrón antiguo de EnArt)
- ❌ **NO acceder directamente a EntityManager** (usar repositorios)

#### Paso 6: Configurar URL en el BFF

**Agregar la URL del nuevo microservicio en el BFF:**

```yaml
# codeflowx.govern.bff.compliance/src/main/resources/application.yml
services:
  # ... otros servicios ...
  [modulo]:
    base-url: ${[MODULO]_SERVICE_BASE_URL:http://localhost:809X}
```

**También configurar Circuit Breaker y Retry:**

```yaml
resilience4j:
  circuitbreaker:
    instances:
      [modulo]Service:
        registerHealthIndicator: true
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        # ... más configuración
  retry:
    instances:
      [modulo]Service:
        maxAttempts: 3
        waitDuration: 1s
        # ... más configuración
```

#### Paso 7: Crear Microservicio de Negocio

**Seguir la plantilla de `codeflowx-governance-classification-service`:**

1. **Crear estructura del módulo:**
   ```
   codeflowx-governance-[modulo]-service/
   ├── pom.xml
   ├── src/main/
   │   ├── java/.../[modulo]/
   │   └── resources/application.yml
   └── README.md
   ```

2. **Configurar pom.xml:**
   - Parent: `nocode.service`
   - Dependencias:
     - `spring-boot-starter-webflux` (NO `spring-boot-starter-web`)
     - `spring-boot-starter-data-jpa`
     - `codeflowx.govern.business`
     - `codeflowx.govern.repository`
     - `codeflowx.govern.nocode.dtos`
     - `codeflowx.govern.nocode.entitys`
     - `springdoc-openapi-starter-webflux-ui` (NO `webmvc-ui`)
     - `postgresql` (runtime)

3. **Crear Application principal:**
   ```java
   @SpringBootApplication(scanBasePackages = {
       "com.codeflowx.govern.[modulo]",
       "com.codeflowx.govern.business",
       "com.codeflowx.govern.repository"
   })
   @EnableJpaRepositories(basePackages = "com.codeflowx.govern.repository")
   @EntityScan(basePackages = "com.codeflowx.govern.entity")
   public class [Modulo]ServiceApplication {
       public static void main(String[] args) {
           SpringApplication.run([Modulo]ServiceApplication.class, args);
       }
   }
   ```

4. **Crear Controller Reactivo:**
   ```java
   @RestController
   @RequestMapping("/api/v1/[modulo]")
   @RequiredArgsConstructor
   @Slf4j
   public class [Modulo]Controller {

       private final [Modulo]BusinessService businessService;

       @GetMapping("/items")
       public Mono<ResponseEntity<List<[Modulo]Dto>>> getAll() {
           return Mono.fromCallable(() -> businessService.getAll())
               .subscribeOn(Schedulers.boundedElastic())
               // ✅ Convertir entidades a DTOs antes de retornar
               .map(items -> items.stream().map(this::toDto).collect(Collectors.toList()))
               .map(ResponseEntity::ok)
               .onErrorResume(error -> {
                   log.error("Error retrieving items", error);
                   return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
               });
       }

       @PostMapping("/items")
       public Mono<ResponseEntity<[Modulo]Dto>> create(
               @Valid @RequestBody [Modulo]Dto dto) {  // ✅ Recibe DTO
           return Mono.fromCallable(() -> {
                   // ✅ Convertir DTO a entidad antes de llamar al servicio
                   [Modulo]Entity entity = toEntity(dto);
                   [Modulo]Entity saved = businessService.save(entity);
                   // ✅ Convertir entidad a DTO antes de retornar
                   return toDto(saved);
               })
               .subscribeOn(Schedulers.boundedElastic())
               .map(ResponseEntity::ok);
       }

       // ✅ Métodos de conversión DTO ↔ Entidad (OBLIGATORIOS)
       private [Modulo]Dto toDto([Modulo]Entity entity) {
           // Conversión de entidad a DTO
       }

       private [Modulo]Entity toEntity([Modulo]Dto dto) {
           // Conversión de DTO a entidad
       }
   }
   ```

   **IMPORTANTE:**
   - ✅ Los controladores **SOLO** reciben y retornan DTOs
   - ✅ La conversión DTO ↔ Entidad se hace en los controladores
   - ❌ **NUNCA** exponer entidades JPA directamente en los endpoints

5. **Configurar application.yml:**
   ```yaml
   spring:
     application:
       name: governance-[modulo]-service
     webflux:
       base-path: /
     jpa:
       hibernate:
         ddl-auto: none
   server:
     port: ${SERVER_PORT:809X}  # Puerto único por microservicio
   ```

6. **Agregar al pom.xml padre:**
   ```xml
   <modules>
     <!-- ... otros módulos ... -->
     <module>codeflowx-governance-[modulo]-service</module>
   </modules>
   ```

### Resiliencia con Resilience4j: Anotaciones vs Operadores

**IMPORTANTE:** Resilience4j ofrece dos formas de aplicar circuit breakers y retry:
1. **Anotaciones** (`@CircuitBreaker`, `@Retry`) - Para métodos **síncronos**
2. **Operadores reactivos** (`CircuitBreakerOperator`, `RetryOperator`) - Para métodos **reactivos** (Mono/Flux)

#### Cuándo Usar Anotaciones

**✅ Usar anotaciones en:**
- **Servicios de Negocio (Business Services)** - Métodos síncronos que usan repositorios JPA
- **Métodos que retornan tipos síncronos** (no Mono/Flux)

**Ejemplo en Business Service:**

```java
@Service
@Slf4j
public class MyBusinessService {

    @Autowired
    private MyRepository repository;

    @Autowired
    private AIGovernanceClient aiGovernanceClient;

    /**
     * Método síncrono - Usar anotaciones de Resilience4j
     */
    @CircuitBreaker(name = "aiGovernanceService", fallbackMethod = "fallbackAnalysis")
    @Retry(name = "aiGovernanceService")
    @TimeLimiter(name = "aiGovernanceService")
    public MyResult performAnalysis(Long entityId, String prompt) {
        // Llamada a microservicio Python
        PromptSafetyResponse response = aiGovernanceClient
            .promptGovernance()
            .evaluateSafety(safetyRequest);

        // Persistir resultado
        MyEntity entity = repository.findById(entityId).orElseThrow();
        entity.setSafetyScore(response.getSafetyScore());
        return repository.save(entity);
    }

    /**
     * Método fallback para Circuit Breaker
     */
    private MyResult fallbackAnalysis(Long entityId, String prompt, Throwable t) {
        log.warn("Fallback ejecutado para análisis: entityId={}, error={}", entityId, t.getMessage());
        // Retornar resultado por defecto o lanzar excepción
        throw new ServiceUnavailableException("Servicio de análisis no disponible temporalmente");
    }
}
```

**Configuración en application.yml:**

```yaml
resilience4j:
  circuitbreaker:
    instances:
      aiGovernanceService:
        registerHealthIndicator: true
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        permittedNumberOfCallsInHalfOpenState: 3
        automaticTransitionFromOpenToHalfOpenEnabled: true
        waitDurationInOpenState: 10s
        failureRateThreshold: 50
  retry:
    instances:
      aiGovernanceService:
        maxAttempts: 3
        waitDuration: 1s
        retryExceptions:
          - java.net.ConnectException
          - java.util.concurrent.TimeoutException
  timelimiter:
    instances:
      aiGovernanceService:
        timeoutDuration: 5s
```

#### Cuándo Usar Operadores Reactivos

**✅ Usar operadores en:**
- **Servicios del BFF** - Métodos que retornan `Mono<T>` o `Flux<T>`
- **Controladores reactivos** - Endpoints que retornan `Mono<ResponseEntity<T>>`
- **Cualquier método que retorne tipos reactivos**

**IMPORTANTE:** Las anotaciones `@CircuitBreaker` y `@Retry` **NO funcionan** con `Mono`/`Flux` porque requieren AOP que no es compatible con tipos reactivos.

**Ejemplo en BFF Service:**

```java
@Service
@Slf4j
public class FriaServiceImpl implements FriaService {

    private final WebClient webClient;
    private final CircuitBreaker friaServiceCircuitBreaker;
    private final Retry friaServiceRetry;

    @Override
    public Mono<FriaAssessmentDto> createFria(FriaCreateRequestDto request) {
        log.info("Creando evaluación FRIA: projectId={}", request.getProjectId());
        return webClient.post()
                .uri(friaServiceBaseUrl + "/api/v1/fria/create")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(FriaAssessmentDto.class)
                .transformDeferred(CircuitBreakerOperator.of(friaServiceCircuitBreaker))
                .transformDeferred(RetryOperator.of(friaServiceRetry))
                .doOnError(error -> log.error("Error creando FRIA", error));
    }
}
```

**Configuración en application.yml (misma estructura):**

```yaml
resilience4j:
  circuitbreaker:
    instances:
      friaService:
        registerHealthIndicator: true
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        waitDurationInOpenState: 10s
        failureRateThreshold: 50
  retry:
    instances:
      friaService:
        maxAttempts: 3
        waitDuration: 1s
        retryExceptions:
          - java.net.ConnectException
          - java.util.concurrent.TimeoutException
```

#### Anotaciones Disponibles en Resilience4j

**Para métodos síncronos (Business Services):**

1. **`@CircuitBreaker`** - Protege contra fallos en cascada
   ```java
   @CircuitBreaker(name = "serviceName", fallbackMethod = "fallbackMethod")
   ```

2. **`@Retry`** - Reintenta llamadas fallidas
   ```java
   @Retry(name = "serviceName")
   ```

3. **`@TimeLimiter`** - Establece timeout para operaciones
   ```java
   @TimeLimiter(name = "serviceName")
   ```

4. **`@RateLimiter`** - Limita número de llamadas por período
   ```java
   @RateLimiter(name = "serviceName")
   ```

5. **`@Bulkhead`** - Limita concurrencia
   ```java
   @Bulkhead(name = "serviceName")
   ```

**Combinación de anotaciones:**

```java
@CircuitBreaker(name = "aiService", fallbackMethod = "fallback")
@Retry(name = "aiService")
@TimeLimiter(name = "aiService")
@RateLimiter(name = "aiService")
public MyResult callAIService(String input) {
    // Lógica del servicio
}
```

#### Configuración de Circuit Breakers y Retry

**Estructura estándar en application.yml:**

```yaml
resilience4j:
  circuitbreaker:
    instances:
      serviceName:  # Nombre del servicio (debe coincidir con name en @CircuitBreaker)
        registerHealthIndicator: true
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        permittedNumberOfCallsInHalfOpenState: 3
        automaticTransitionFromOpenToHalfOpenEnabled: true
        waitDurationInOpenState: 10s
        failureRateThreshold: 50
        eventConsumerBufferSize: 10
  retry:
    instances:
      serviceName:  # Nombre del servicio (debe coincidir con name en @Retry)
        maxAttempts: 3
        waitDuration: 1s
        retryExceptions:
          - java.net.ConnectException
          - java.util.concurrent.TimeoutException
        ignoreExceptions:
          - com.codeflowx.govern.exception.NotFoundException
  timelimiter:
    instances:
      serviceName:
        timeoutDuration: 5s
  ratelimiter:
    instances:
      serviceName:
        limitForPeriod: 10
        limitRefreshPeriod: 1s
        timeoutDuration: 0
```

#### Resumen: Anotaciones vs Operadores

| Contexto | Tipo de Método | Usar |
|----------|----------------|------|
| **Business Service** | Síncrono (retorna `T`) | ✅ **Anotaciones** (`@CircuitBreaker`, `@Retry`) |
| **BFF Service** | Reactivo (retorna `Mono<T>`) | ✅ **Operadores** (`CircuitBreakerOperator`, `RetryOperator`) |
| **Controller Microservicio** | Reactivo (retorna `Mono<ResponseEntity<T>>`) | ✅ **Operadores** (`CircuitBreakerOperator`, `RetryOperator`) |
| **Controller BFF** | Reactivo (retorna `Mono<ResponseEntity<T>>`) | ✅ **Operadores** (aplicados en el servicio, no en el controller) |

**Regla de oro:**
- **Síncrono = Anotaciones**
- **Reactivo (Mono/Flux) = Operadores**

#### Ejemplo Completo: Controller de Microservicio con Operadores

**En el Controller del Microservicio (reactivo):**

```java
@RestController
@RequestMapping("/api/v1/fria")
@RequiredArgsConstructor
@Slf4j
public class FriaController {

    private final FriaAssessmentBusinessService businessService;

    @PostMapping("/create")
    @Operation(summary = "Crear nueva evaluación FRIA")
    public Mono<ResponseEntity<FriaAssessmentDto>> createFria(
            @Valid @RequestBody FriaCreateRequestDto request) {

        log.info("Request to create FRIA: projectId={}", request.getProjectId());

        // ✅ El controller llama al business service (síncrono)
        // ✅ El business service usa anotaciones @CircuitBreaker, @Retry internamente
        return Mono.fromCallable(() -> {
                FriaAssessment fria = businessService.createFria(
                        request.getProjectId(),
                        request.getDeployerUserId()
                );
                return toDto(fria);
            })
            .subscribeOn(Schedulers.boundedElastic())
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error creating FRIA", error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }
}
```

**En el Business Service (síncrono con anotaciones):**

```java
@Service
@Slf4j
public class FriaAssessmentBusinessService {

    @Autowired
    private AIGovernanceClient aiGovernanceClient;

    /**
     * ✅ Método síncrono - Usar anotaciones de Resilience4j
     */
    @CircuitBreaker(name = "aiGovernanceService", fallbackMethod = "crossValidateFallback")
    @Retry(name = "aiGovernanceService")
    @TimeLimiter(name = "aiGovernanceService")
    public CrossValidationResult crossValidate(Long friaId) {
        // Llamada a microservicio Python
        FriaDataDTO friaData = convertToFriaDataDTO(fria);
        return aiGovernanceClient.friaGenerator().crossValidate(friaData);
    }

    private CrossValidationResult crossValidateFallback(Long friaId, Throwable t) {
        log.warn("Fallback ejecutado: friaId={}, error={}", friaId, t.getMessage());
        throw new IllegalStateException("Servicio no disponible temporalmente");
    }
}
```

**En el BFF Service (reactivo con operadores):**

```java
@Service
@Slf4j
public class FriaServiceImpl implements FriaService {

    private final WebClient webClient;
    private final CircuitBreaker friaServiceCircuitBreaker;
    private final Retry friaServiceRetry;

    @Override
    public Mono<FriaAssessmentDto> createFria(FriaCreateRequestDto request) {
        log.info("Creando evaluación FRIA: projectId={}", request.getProjectId());
        // ✅ Método reactivo - Usar operadores de Resilience4j
        return webClient.post()
                .uri(friaServiceBaseUrl + "/api/v1/fria/create")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(FriaAssessmentDto.class)
                .transformDeferred(CircuitBreakerOperator.of(friaServiceCircuitBreaker))
                .transformDeferred(RetryOperator.of(friaServiceRetry))
                .doOnError(error -> log.error("Error creando FRIA", error));
    }
}
```

**Configuración requerida en application.yml del microservicio:**

```yaml
resilience4j:
  circuitbreaker:
    instances:
      aiGovernanceService:  # Para anotaciones en business services
        registerHealthIndicator: true
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        waitDurationInOpenState: 10s
        failureRateThreshold: 50
  retry:
    instances:
      aiGovernanceService:
        maxAttempts: 3
        waitDuration: 1s
        retryExceptions:
          - java.net.ConnectException
          - java.util.concurrent.TimeoutException
  timelimiter:
    instances:
      aiGovernanceService:
        timeoutDuration: 30s
```

**Configuración requerida en application.yml del BFF:**

```yaml
resilience4j:
  circuitbreaker:
    instances:
      friaService:  # Para operadores en BFF services
        registerHealthIndicator: true
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        waitDurationInOpenState: 10s
        failureRateThreshold: 50
  retry:
    instances:
      friaService:
        maxAttempts: 3
        waitDuration: 1s
        retryExceptions:
          - java.net.ConnectException
          - java.util.concurrent.TimeoutException
```

### Patrón KISS para Servicios BFF con WebClient

**Principio KISS (Keep It Simple, Stupid):** Los servicios del BFF deben ser simples y directos. Evitar complejidad innecesaria.

#### Patrón Estándar para Llamadas WebClient en BFF

**Estructura mínima y simple:**

```java
@Override
public Mono<ResponseDto> methodName(RequestDto request) {
    log.info("Operación: param={}", request.getParam());
    return webClient.post()  // o .get(), .put(), .delete()
            .uri(serviceBaseUrl + "/api/v1/endpoint")
            .bodyValue(request)  // Solo si es POST/PUT
            .retrieve()
            .bodyToMono(ResponseDto.class)
            .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
            .transformDeferred(RetryOperator.of(retry))
            .doOnError(error -> log.error("Error en operación: param={}", request.getParam(), error));
}
```

**Reglas KISS:**

1. **Una línea por operación:** Cada método debe ser una cadena fluida de operaciones
2. **Logging mínimo:** Solo log de inicio y error, sin logs de éxito innecesarios
3. **Sin manejo complejo de errores:** Dejar que los errores se propaguen al controller
4. **Sin métricas en el servicio:** Las métricas se manejan en el controller si es necesario
5. **Query params simples:** Usar `StringBuilder` para construir URIs con query params (suficiente para la mayoría de casos)

**Ejemplo completo:**

```java
@Override
public Mono<Map<String, Object>> listItems(int page, int size, Long projectId) {
    log.debug("Listando items: page={}, size={}, projectId={}", page, size, projectId);

    StringBuilder uri = new StringBuilder(serviceBaseUrl + "/api/v1/items?page=")
            .append(page).append("&size=").append(size);
    if (projectId != null) uri.append("&projectId=").append(projectId);

    return webClient.get()
            .uri(uri.toString())
            .retrieve()
            .bodyToMono(Map.class)
            .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
            .transformDeferred(RetryOperator.of(retry))
            .doOnError(error -> log.error("Error listando items", error));
}
```

**❌ NO hacer (complejidad innecesaria):**

```java
// ❌ Demasiado complejo - métricas, manejo de errores, etc.
Timer.Sample timer = metrics.startTimer();
return webClient.get()
    .uri(uri)
    .retrieve()
    .onStatus(HttpStatus::is4xxClientError, ...)
    .onStatus(HttpStatus::is5xxServerError, ...)
    .bodyToMono(...)
    .doOnSuccess(result -> {
        timer.stop(...);
        metrics.record(...);
        log.info("Éxito...");
    })
    .doOnError(error -> {
        timer.stop(...);
        metrics.recordError(...);
        log.error("Error...");
    });
```

**✅ Hacer (simple y directo):**

```java
// ✅ Simple y directo - KISS
return webClient.get()
    .uri(uri)
    .retrieve()
    .bodyToMono(ResponseDto.class)
    .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
    .transformDeferred(RetryOperator.of(retry))
    .doOnError(error -> log.error("Error: param={}", param, error));
```

**Nota:** El manejo de errores HTTP específicos (404, 400, etc.) y métricas se pueden agregar en el controller si es necesario, pero el servicio del BFF debe mantenerse simple.

### Reglas y Mejores Prácticas

#### ✅ DO (Hacer)

1. **Reutilizar DTOs:** Siempre usar DTOs de `codeflowx.govern.nocode.dtos`
2. **DTOs en BFF y Controladores:** BFF y controladores de microservicios SOLO trabajan con DTOs
3. **Conversión DTO ↔ Entidad:** La conversión se hace en los controladores, nunca en servicios de negocio
4. **Usar Repositorios JPA:** Los business services trabajan SOLO con repositorios de `codeflowx.govern.repository`
5. **Aplicar KISS en BFF:** Mantener servicios del BFF simples y directos (ver patrón arriba)
5. **Usar GenericRepository:** Si solo necesitas CRUD básico, usar `GenericRepository<T, ID>`
6. **Usar JdbcTemplate:** Solo si necesitas SQL nativo que no se puede hacer con JPA
7. **Controller Reactivo:** Todos los endpoints deben devolver `Mono<ResponseEntity<T>>`
8. **Envolver Llamadas Síncronas:** Usar `Mono.fromCallable()` con `Schedulers.boundedElastic()`
9. **Logging:** Agregar logs informativos en servicios y controllers
10. **Manejo de Errores:** Usar `onErrorResume()` para manejo reactivo de errores
11. **Documentación:** Crear README.md con descripción del microservicio
12. **OpenAPI:** Documentar endpoints con anotaciones Swagger
13. **LLAMADAS A PROCESOS bpmn** usar en business services
```java
  @Autowired(required = false)
    private BpmnWorkflowClient bpmnWorkflowClient;   eta es una llamada de ejemplo :
     // Verificar si el cliente BPMN está disponible
        if (bpmnWorkflowClient == null || !bpmnWorkflowClient.isAvailable()) {
            log.warn("BpmnWorkflowClient no está disponible. Workflow BPMN no se disparará.");
            return null;
        }

        try {
            // Preparar variables del workflow
            Map<String, Object> variables = new HashMap<>();
            variables.put("projectId", projectId);
            variables.put("category", category);
            variables.put("isHighRisk", true);
            variables.put("classificationDate", LocalDateTime.now().toString());

            // Disparar workflow BPMN usando el cliente
            String workflowInstanceId = bpmnWorkflowClient.startProcess(
                    "high-risk-classification-workflow",
                    variables
            );

            if (workflowInstanceId != null) {
                log.info("Workflow BPMN disparado exitosamente: projectId={}, category={}, workflowInstanceId={}",
                        projectId, category, workflowInstanceId);
            }

            return workflowInstanceId;
        } catch (Exception e) {
            log.error("Error disparando workflow BPMN para proyecto: {}", projectId, e);
            // No lanzar excepción para no fallar la clasificación si el workflow falla
            return null;
        }
```
#### ❌ DON'T (No Hacer)

1. **NO crear DTOs en el microservicio:** Usar los de la librería compartida
2. **NO exponer entidades JPA:** BFF y controladores NUNCA exponen entidades JPA, solo DTOs
3. **NO recibir entidades en endpoints:** Los controladores NUNCA reciben entidades JPA en request bodies
4. **NO usar Spring MVC:** Siempre usar WebFlux (reactivo)
5. **NO hacer servicios de negocio reactivos:** Mantenerlos síncronos, envolver en Mono
6. **NO bloquear threads:** Usar `Schedulers.boundedElastic()` para operaciones bloqueantes
7. **NO duplicar código:** Reutilizar servicios de negocio existentes
8. **NO crear repositorios específicos innecesarios:** Usar GenericRepository cuando sea suficiente
9. **NO usar DAO:** El patrón DAO de EnArt está deprecado, usar repositorios JPA
10. **NO acceder directamente a EntityManager:** Usar repositorios o JdbcTemplate
11. **NO usar `Map<String, Object>` en respuestas:** SIEMPRE crear DTOs tipados para todas las respuestas de API
12. **NO usar `Mono<Map<String, Object>>`:** SIEMPRE retornar `Mono<DtoResponse>` con DTOs específicos
13. **NO usar `List<Map<String, Object>>`:** SIEMPRE retornar `Mono<List<Dto>>` o `Mono<DtoListResponse>` con DTOs específicos

### Checklist para Nuevo Microservicio

- [ ] Revisado documentación de negocio del módulo
- [ ] Revisado entidades JPA relacionadas
- [ ] Verificado DTOs existentes (o creados en librería compartida)
- [ ] Creado/verificado repositorio JPA (genérico o específico) en `codeflowx.govern.repository`
- [ ] Creado servicio de negocio en `codeflowx.govern.business`
  - [ ] Usa repositorios JPA (NO DAO)
  - [ ] Usa JdbcTemplate solo si necesita SQL nativo
  - [ ] Agregado disparo de workflows BPMN si es necesario (desde servicio de negocio)
- [ ] Configurado URL del microservicio en BFF (`application.yml` → `services:`)
- [ ] Configurado Circuit Breaker y Retry en BFF para el nuevo servicio
- [ ] Creado estructura del microservicio siguiendo plantilla
- [ ] Configurado pom.xml con dependencias correctas (WebFlux)
- [ ] Creado controller reactivo con todos los endpoints
- [ ] Configurado application.yml
- [ ] Agregado módulo al pom.xml padre
- [ ] Creado README.md con documentación
- [ ] Probado endpoints con Swagger UI

### Referencias

- **Plantilla de Referencia:** `nocode.service/codeflowx-governance-classification-service/`
- **Repositorios JPA:** `nocode.service/codeflowx.govern.repository/` (README con filosofía de uso)
- **Servicios de Negocio:** `nocode.service/codeflowx.govern.business/`
- **DTOs:** `nocode.service/codeflowx.govern.nocode.dtos/`
- **Entidades:** `nocode.service/nocode.service.entitys/`
- **Configuración BFF:** `nocode.service/codeflowx.govern.bff.compliance/src/main/resources/application.yml`
- **Cliente BPMN:** `nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/client/BpmnWorkflowClient.java`
- **Workflow Engine (Microservicio):** `nocode.service/codeflowx.govern.workflow.engine/`

### Nota sobre Migración de DAO a Repositorios

Algunos servicios de negocio antiguos aún usan el patrón DAO de EnArt. La arquitectura actual requiere:
- ✅ **Migrar a repositorios JPA** cuando se modifiquen servicios existentes
- ✅ **Usar GenericRepository** para operaciones CRUD básicas
- ✅ **Crear repositorios específicos** solo cuando se necesiten queries complejas
- ✅ **Usar JdbcTemplate** solo para SQL nativo que no se puede hacer con JPA

---

## 📚 GUÍAS DE DESARROLLO

### Índice de Guías

1. [Crear una Nueva Pantalla](#crear-una-nueva-pantalla)
2. [Crear un Nuevo Módulo](#crear-un-nuevo-módulo)
3. [Añadir Menús y Opciones en el Sidebar con Traducciones](#añadir-menús-y-opciones-en-el-sidebar-con-traducciones)

### Crear una Nueva Pantalla

1. **Crear archivo de página:**
   ```typescript
   // app/(app)/governance/nueva-pantalla/page.tsx
   export default function NuevaPantalla() {
     const { t } = useTranslation();
     return <div>{t('governance.nuevaPantalla.title')}</div>;
   }
   ```

2. **Crear traducciones:**
   ```typescript
   // app/config/i18n/modules/governance/nueva-pantalla.ts
   export const nuevaPantallaTranslations: TranslationModule = {
     es: { title: "Nueva Pantalla" },
     en: { title: "New Screen" },
     // ... otros idiomas
   };
   ```

3. **Agregar a index de governance:**
   ```typescript
   // app/config/i18n/modules/governance/index.ts
   import { nuevaPantallaTranslations } from './nueva-pantalla';
   ```

4. **Crear mock data (opcional):**
   ```json
   // app/mocks/governance/nueva-pantalla.json
   { "data": "..." }
   ```

### Crear un Nuevo Módulo

1. Crear directorio en `app/(app)/[modulo]/`
2. Crear traducciones en `app/config/i18n/modules/[modulo]/`
3. Agregar configuración en `app/config/modules.ts`
4. Crear API routes si es necesario

### Añadir Menús y Opciones en el Sidebar con Traducciones

El sidebar se configura en `app/config/modules.ts` y las traducciones se definen en `app/config/i18n/modules/layout.ts`. Sigue estos pasos para añadir una nueva opción de menú:

#### Paso 1: Añadir la Opción de Menú en `modules.ts`

**Ubicación:** `app/config/modules.ts`

En la función `getMenuItemsForModule()`, añade el nuevo elemento de menú dentro del `case` correspondiente al módulo:

```typescript
// app/config/modules.ts
export function getMenuItemsForModule(moduleName: string): MenuItem[] {
  switch (moduleName) {
    case "Compliance":
      return [
        // ... otros elementos existentes ...
        {
          name: "Nueva Opción",  // ⚠️ Este nombre debe coincidir exactamente con la clave de traducción
          href: `/governance/compliance/nueva-opcion`,
          icon: "IconName",  // Nombre del icono de lucide-react (sin "Icon" al final)
          roles: [
            "admin",
            "project_manager",
            "compliance_officer",
            // ... otros roles permitidos
          ],
          isDefault: false,  // Opcional: true si es la página por defecto del módulo
        },
      ];
    // ... otros módulos ...
  }
}
```

**Propiedades del MenuItem:**
- **`name`**: Nombre del menú (debe coincidir exactamente con la clave de traducción)
- **`href`**: Ruta de la página (relativa a la raíz de la app)
- **`icon`**: Nombre del icono de `lucide-react` (ej: `"BarChart3"`, `"Shield"`, `"Tag"`)
- **`roles`**: Array de roles que pueden ver esta opción
- **`isDefault`**: (Opcional) `true` si es la página por defecto del módulo

**Iconos disponibles:** Consulta [lucide-react icons](https://lucide.dev/icons/) para ver todos los iconos disponibles. Usa el nombre del icono sin el prefijo "Icon" (ej: `BarChart3`, no `BarChart3Icon`).

#### Paso 2: Añadir Traducciones en `layout.ts`

**Ubicación:** `app/config/i18n/modules/layout.ts`

Añade la traducción del nombre del menú en la sección `sidebar.menuItems` para todos los idiomas soportados:

```typescript
// app/config/i18n/modules/layout.ts
export const layoutTranslations: TranslationModule = {
  es: {
    sidebar: {
      menuItems: {
        // ... otras traducciones existentes ...
        "Nueva Opción": "Nueva Opción",  // ⚠️ La clave debe coincidir exactamente con el name del MenuItem
      },
    },
  },
  en: {
    sidebar: {
      menuItems: {
        // ... otras traducciones existentes ...
        "Nueva Opción": "New Option",
      },
    },
  },
  fr: {
    sidebar: {
      menuItems: {
        // ... otras traducciones existentes ...
        "Nueva Opción": "Nouvelle Option",
      },
    },
  },
  de: {
    sidebar: {
      menuItems: {
        // ... otras traducciones existentes ...
        "Nueva Opción": "Neue Option",
      },
    },
  },
  it: {
    sidebar: {
      menuItems: {
        // ... otras traducciones existentes ...
        "Nueva Opción": "Nuova Opzione",
      },
    },
  },
  pt: {
    sidebar: {
      menuItems: {
        // ... otras traducciones existentes ...
        "Nueva Opción": "Nova Opção",
      },
    },
  },
};
```

**⚠️ IMPORTANTE:** La clave en `menuItems` debe coincidir **exactamente** con el `name` del `MenuItem` en `modules.ts` (incluyendo mayúsculas, espacios y caracteres especiales).

#### Paso 3: Crear la Página (Opcional)

Si la opción de menú apunta a una nueva página, créala en la ruta especificada en `href`:

```typescript
// app/(app)/governance/compliance/nueva-opcion/page.tsx
import { useTranslation } from '@/app/config/i18n';

export default function NuevaOpcionPage() {
  const { t } = useTranslation();

  return (
    <div>
      <h1>{t('layout.sidebar.menuItems.Nueva Opción')}</h1>
      {/* Contenido de la página */}
    </div>
  );
}
```

#### Ejemplo Completo

**1. Añadir menú en `modules.ts`:**

```typescript
case "Compliance":
  return [
    {
      name: "Compliance Dashboard",
      href: `/governance/compliance/dashboard`,
      icon: "BarChart3",
      roles: ["admin", "compliance_officer"],
      isDefault: true,
    },
    {
      name: "Traceability",  // Nuevo menú
      href: `/governance/compliance/traceability`,
      icon: "Network",
      roles: ["admin", "compliance_officer", "auditor"],
    },
  ];
```

**2. Añadir traducciones en `layout.ts`:**

```typescript
sidebar: {
  menuItems: {
    "Traceability": "Trazabilidad",  // ES
    // ...
  },
},
// ... para otros idiomas:
en: {
  sidebar: {
    menuItems: {
      "Traceability": "Traceability",
    },
  },
},
fr: {
  sidebar: {
    menuItems: {
      "Traceability": "Traçabilité",
    },
  },
},
// ... etc para de, it, pt
```

**3. El componente Sidebar automáticamente:**
- Muestra el icono especificado
- Traduce el nombre usando `t("layout.sidebar.menuItems.${item.name}")`
- Filtra por roles del usuario
- Navega a la ruta especificada

#### Eliminar una Opción de Menú

Para eliminar una opción del sidebar:

1. **Eliminar del array en `modules.ts`:** Remover el objeto `MenuItem` del array correspondiente
2. **Opcional - Eliminar traducciones:** Las traducciones pueden quedarse sin problema, pero puedes eliminarlas de `layout.ts` si quieres limpiar el código

#### Reglas y Mejores Prácticas

**✅ DO (Hacer):**
1. **Coincidencia exacta:** El `name` del `MenuItem` debe coincidir exactamente con la clave en `menuItems`
2. **Todos los idiomas:** Añadir traducciones para todos los idiomas soportados (es, en, fr, de, it, pt)
3. **Iconos de lucide-react:** Usar iconos de la librería `lucide-react`
4. **Roles apropiados:** Especificar los roles que pueden acceder a cada opción
5. **Rutas consistentes:** Seguir el patrón de rutas del módulo (ej: `/governance/compliance/[opcion]`)

**❌ DON'T (No Hacer):**
1. **NO usar nombres hardcodeados:** Siempre usar traducciones, nunca hardcodear textos en el menú
2. **NO duplicar nombres:** Cada `name` debe ser único dentro del mismo módulo
3. **NO usar iconos inexistentes:** Verificar que el icono existe en `lucide-react`
4. **NO olvidar roles:** Si no especificas roles, la opción no será visible para nadie

#### Verificación

Después de añadir una nueva opción de menú:

1. **Verificar que aparece en el sidebar** del módulo correspondiente
2. **Cambiar de idioma** y verificar que la traducción se actualiza correctamente
3. **Verificar permisos** con diferentes roles de usuario
4. **Verificar navegación** al hacer clic en la opción

---

## 📖 REFERENCIAS

### Documentación Relacionada

- **i18n:** `app/config/i18n/README.md`
- **Pantallas Esenciales:** `docs/PANTALLAS_ESENCIALES_USUARIOS.md`
- **Menús y Roles:** `docs/ARQUITECTURA_MENUS_ROLES_CODEFLOWX.md`
- **Mock Data:** `developers/MOCK_DATA_DOCUMENTATION.md`
- **Backend Architecture:** Ver sección [Arquitectura de Backend](#-arquitectura-de-backend-y-microservicios-de-negocio)
- **Microservicio Plantilla:** `nocode.service/codeflowx-governance-classification-service/README.md`

### Recursos Externos

- [Next.js Documentation](https://nextjs.org/docs)
- [Tailwind CSS](https://tailwindcss.com/docs)
- [shadcn/ui](https://ui.shadcn.com/)

---

**Última actualización:** Diciembre 2025
**Mantenido por:** Equipo de Desarrollo CodeflowX
