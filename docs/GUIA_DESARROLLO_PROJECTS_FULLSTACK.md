# Guía de desarrollo (Frontend + Backend) — Módulo Projects

**Fecha:** Dic 2025
**Repositorio Frontend:** `codeflowx-studio` (Next.js App Router)
**Repositorio Backend (workspace):** `nocode-service` (multi-módulo Java/Spring + entidades NoCode)

---

## 1) Objetivo y alcance

Esta guía documenta **dónde está el “controller” de Projects hoy**, cómo se conectan **pantallas → BFF → backend**, qué partes usan **mocks**, y qué endpoints **están documentados vs realmente implementados**.

> Importante: en CodeflowX existen **módulos funcionales distintos** con pantallas propias. Que haya páginas en `app/(app)/projects/**` **no implica** que todas pertenezcan al módulo “Projects” del menú.

---

## 2) Frontend (codeflowx-studio)

### 2.1 Navegación y “source of truth” del menú

- **Módulos + menú**: `app/config/modules.ts`
  - `modulesConfig[]` define qué módulos ve un rol.
  - `getMenuByModule("Projects")` define **solo** las entradas válidas del módulo Projects.
- **Sidebar**: `components/layout/sidebar.tsx`
  - Renderiza el menú del módulo activo con `getMenuByModule(activeModule)`.
  - Filtra por roles desde `localStorage.user.roles`.
- **Header (selector de módulo)**: `components/layout/Header.tsx`
  - Lista módulos disponibles por roles (`getModulesByRoles`).

### 2.2 Mocks y URLs del backend

El control central para activar/desactivar mocks y configurar URLs está en:

- `app/config/mock.ts`
  - `USE_MOCK`: se activa por defecto en dev si no se deshabilita explícitamente.
  - `NEXT_PUBLIC_BACKEND_URL` → `BACKEND_BASE_URL`
  - `NEXT_PUBLIC_BFF_URL` → `BFF_BASE_URL`

**Variables clave**

- `USE_MOCK=true|false` (server)
- `NEXT_PUBLIC_USE_MOCK=true|false` (client)
- `NEXT_PUBLIC_BACKEND_URL=http://...` (server-side fetch usa esta base en varios routes)
- `NEXT_PUBLIC_BFF_URL=http://...` (BFF)

### 2.3 “Controller” de Projects en el frontend (BFF/Proxy)

En Next.js, el “controller” equivalente suele ser un **API route**:

- `app/api/governance/projects/route.ts`
  - `GET /api/governance/projects?search=...`
  - Si `USE_MOCK` → devuelve `mockProjects`.
  - Si no → hace fetch a: `GET ${BACKEND_BASE_URL}/api/v1/projects?page=0&size=1000&search=...`
  - Normaliza la respuesta para soportar varias formas (`items`, `projects`, array).

**Conclusión práctica**

Hoy, el frontend asume que existe **un backend** que expone `GET /api/v1/projects` (listado). Si el backend no lo expone, la única forma de que funcione es con `USE_MOCK=true`.

---

## 3) Backend (nocode-service)

### 3.1 Qué hay “de verdad” (código) vs qué está “especificado”

Hay 3 piezas distintas a no confundir:

1) **API Edge/OpenAPI**
   - `nocode-service/codeflowx-governance-api/src/main/resources/openapi/governance-api.yaml`
   - Aquí aparece `@RequestMapping("/api/v1/projects")` para operaciones **de evaluate/webhooks** (no CRUD).

2) **Controller implementado en Edge API**
   - `nocode-service/codeflowx-governance-api/src/main/java/com/codeflowx/governance/api/controller/ProjectEvaluationController.java`
   - Implementa `POST /api/v1/projects/{projectUuid}/evaluate`

3) **Documento de “portal-backend” (especificación funcional/target)**
   - `codeflowx-studio/docs/portal-backend/04_PROJECT_MANAGEMENT_MODULE.md`
   - Lista un set enorme de endpoints CRUD y sub-recursos:
     - `GET /api/v1/projects`
     - `GET /api/v1/projects/{id}`
     - `POST/PUT/DELETE ...`
     - `.../members`, `.../technologies`, `.../documents`, etc.

> A día de hoy, en el workspace `nocode-service` **no se ve** (por búsqueda de anotaciones Spring) un `@RestController` que implemente el CRUD de `GET /api/v1/projects` como el que asume el frontend.

### 3.2 Modelo de datos de Projects (entidades)

Existen entidades en:

- `nocode-service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/projects/*`
  - Ej.: `Project.java`, `ProjectMember.java`, `ProjectDomain.java`, etc.
- Existen también **vistas** (para dashboards):
  - `nocode-service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/views/projects/*`
  - Ej.: `ProjectPortfolioDashboard.java`, `ProjectCostBreakdown.java`, etc.

**Nota sobre convención de tablas**

En el backend actual se observan tablas tipo `PRJPROJECTS` (mayúsculas, sin guion bajo).
En la convención recomendada del proyecto (KISS/3FN + prefijo funcional), el estándar deseado sería algo como `prj_projects` con PK autonumérica.

---

## 4) Mapa actual de rutas (pantallas) vs APIs

### 4.1 Pantallas del módulo Projects (menú “válido”)

Definidas como válidas en `app/config/modules.ts` (módulo “Projects”):

- `/projects/portfolio-dashboard`
- `/projects/list`
- `/projects/integrations`
- `/projects/external-sync`
- `/projects/resource-allocation`
- `/projects/cost-breakdown`
- `/projects/roi-analysis`

### 4.2 Estado actual del consumo de datos

- Varias pantallas en `app/(app)/projects/**` están implementadas como **UI demo** con datos mock en el propio `page.tsx`.
- La **única** ruta de API claramente orientada a “listar proyectos” está en:
  - `GET /api/governance/projects` → proxy a `GET /api/v1/projects` (backend) o mocks.

### 4.3 Tabla rápida (pantalla → fuente de datos actual)

| Pantalla | Ruta | Fuente de datos hoy | Observación |
|---|---|---|---|
| Portfolio Dashboard | `/projects/portfolio-dashboard` | `mockMetrics` en página | UI demo |
| Projects List | `/projects/list` | `mockProjects` en página | UI demo (no usa BFF) |
| Project Detail | `/projects/[id]` | mock local | UI demo |
| Integrations Dashboard | `/projects/integrations` | mock local | UI demo |
| External Tools Sync | `/projects/external-sync` | mock local | UI demo |
| Resource Allocation | `/projects/resource-allocation` | mock local | UI demo |
| Cost Breakdown | `/projects/cost-breakdown` | mock local | UI demo |
| ROI Analysis | `/projects/roi-analysis` | mock local | UI demo |

> Nota: existen otras páginas bajo `app/(app)/projects/**` con mocks, pero **no deben asumirse “válidas” del módulo Projects** sin confirmación funcional (menú + ownership por módulo).

---

## 5) Cómo confirmar “a qué endpoint apunta Projects”

Checklist rápido:

1) Revisa si en el navegador (Network) se llama a:
   - `GET /api/governance/projects`
2) Si el resultado es mock:
   - `USE_MOCK` / `NEXT_PUBLIC_USE_MOCK` están activos (dev por defecto).
3) Si no es mock y falla:
   - El frontend está intentando `GET ${NEXT_PUBLIC_BACKEND_URL}/api/v1/projects`
   - Confirma si ese endpoint existe en el backend que estás levantando.

---

## 6) Recomendación de arquitectura (Hexagonal) para implementar el CRUD faltante

Si el objetivo es materializar el “Project Management Module” (del doc portal-backend) y alinear con KISS/SOLID/hexagonal:

- **Adapter inbound (REST Controller)**: `ProjectsController`
  - `GET /api/v1/projects`
  - `GET /api/v1/projects/{id}`
  - `POST /api/v1/projects`
  - etc.
- **Application Service (use-cases)**:
  - `ListProjectsUseCase`, `CreateProjectUseCase`, `UpdateProjectUseCase`, ...
- **Domain**:
  - `Project` aggregate (o entidad si es CRUD simple) + invariantes.
- **Adapter outbound (Repository)**:
  - `ProjectRepository` (Spring Data / NoCode repository) + mappers DTO↔Domain.

**Contrato mínimo para desbloquear el frontend**

Para satisfacer `app/api/governance/projects/route.ts`, basta con que el backend exponga:

- `GET /api/v1/projects?search=&page=&size=`
  - Respuesta con `items` o `projects` o lista directa.
  - Campos: `id|idxproject|projectId` y `name|projectName`.

---

## 7) Entregable recomendado (documentación)

Cuando documentes “todo”, separa claramente:

- **“Implementado (código)”**: controllers reales + rutas reales.
- **“Especificado (target)”**: docs `portal-backend`/OpenAPI.
- **“Mock/Demo”**: pantallas con `mock*` en `page.tsx` o `USE_MOCK`.

---

## 8) Próximos pasos sugeridos

1) Decidir **dónde vive el Project Management CRUD**:
   - ¿Un “portal-backend” separado? ¿Un microservicio `project-management-service`?
2) Implementar al menos `GET /api/v1/projects` en el backend correcto (o ajustar el proxy del frontend al endpoint real).
3) Migrar pantallas clave (`/projects/list`) a consumir el proxy `/api/governance/projects` en vez de mocks embebidos.
