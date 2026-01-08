# 📘 Guía de Desarrollo Frontend — Módulo Prompts (CodeflowX Studio)

**Ámbito:** pantallas Next.js (App Router) + API routes + menú + i18n del módulo **Prompts**.
**Repositorio:** `codeflowx-studio/`
**Objetivo:** que cualquier dev pueda mantener y evolucionar el módulo Prompts con consistencia (KISS, SOLID) y alineado a la arquitectura descrita en `docs/ARQUITECTURA_FRONTEND.md`.

---

## ✅ Mapa de pantallas (Next.js App Router)

Directorio: `codeflowx-studio/app/(app)/governance/prompts/`

Pantallas detectadas:
- `page.tsx` → **Listado principal** `/governance/prompts`
- `[id]/page.tsx` → **Detalle** `/governance/prompts/[id]`
- `[id]/edit/page.tsx` → **Edición** `/governance/prompts/[id]/edit`
- `register/page.tsx` → **Crear/Editar** `/governance/prompts/register` (usa query param `id` y/o rutas)
- `validation/overview/page.tsx` → **Validation Overview**
- `versioning/overview/page.tsx` → **Versioning Overview**
- `templates/performance/page.tsx` → **Performance**
- `testing/page.tsx` → **Testing** (extra)

---

## 🔌 API Routes (Next.js)

### API routes “mock” (namespace `/api/prompts/*`)

Directorio: `codeflowx-studio/app/api/prompts/`

Estado: **mock data** (dev/demo).
- `register/route.ts` (POST mock)
- `[id]/route.ts` (GET/PUT/DELETE mock)
- `[id]/versions/route.ts` (GET mock)
- `[id]/validations/route.ts` (GET/POST mock)
- `validation/overview/route.ts` (GET mock)
- `versioning/overview/route.ts` (GET mock)
- `templates/performance/route.ts` (GET mock)

### API routes “governance/prompts” (proxy a backend, parcialmente alineadas)

Directorio: `codeflowx-studio/app/api/governance/prompts/`

Detectadas:
- `route.ts` (GET/POST) → usa `PROMPTS_SERVICE_URL` y llama a `/api/v1/governance/prompts` (**ojo: path no coincide con backend `/api/v1/prompts`**)
- `[id]/route.ts` (GET/PUT/DELETE) → idem `/api/v1/governance/prompts/{id}`
- `[id]/agents/route.ts`, `[id]/models/route.ts`, `[id]/rags/route.ts` → usan `NEXT_PUBLIC_GATEWAY_URL` y llaman a `/api/v1/prompts/...` (alineado)
- `[id]/test/*` → mezcla `/api/v1/governance/prompts/{id}` y `/api/v1/prompts/{id}/test` (revisar coherencia)

---

## 🧭 Menú y roles

Archivo: `codeflowx-studio/app/config/modules.ts`

El módulo “Prompts” expone estos ítems:
- `/governance/prompts` (default)
- `/governance/prompts/validation/overview`
- `/governance/prompts/versioning/overview`
- `/governance/prompts/templates/performance`

Regla (según `docs/ARQUITECTURA_FRONTEND.md`): el `name` debe existir en traducciones de sidebar (`layout.sidebar.menuItems.*`).

---

## 🌐 i18n

En la app, las traducciones se combinan desde `codeflowx-studio/app/config/i18n/index.ts`.

Para Prompts existe el módulo:
- `codeflowx-studio/app/config/i18n/modules/prompts.ts` → claves bajo `prompts.*`

⚠️ En las pantallas de Prompts se observan claves del estilo `governance.prompts.*` y `prompts.*` mezcladas. Para evitar “missing translations”:
- Opción A (recomendada KISS): estandarizar UI a `prompts.*`
- Opción B: crear un puente en i18n que exponga también `governance.prompts.*`

---

## ⚙️ Variables de entorno (frontend)

Variables usadas por el módulo:
- **`PROMPTS_SERVICE_URL`**: base URL del servicio/bff al que apuntan varias API routes en `app/api/governance/prompts/*` (default `http://localhost:8084`)
- **`NEXT_PUBLIC_GATEWAY_URL`**: base URL del gateway (default `http://localhost:8080`) usado en relaciones (agents/models/rags)
- (Arquitectura general) `NEXT_PUBLIC_USE_MOCK` para mocks globales (ver `docs/ARQUITECTURA_FRONTEND.md`), aunque Prompts hoy usa mocks “hardcodeados” en páginas y en `/api/prompts/*`.

---

## 🧱 Patrón de UI recomendado (consistencia)

Según `docs/ARQUITECTURA_FRONTEND.md`, las pantallas de create/edit deberían seguir la estructura:
- Línea 1: **título + subtítulo**
- Línea 2: **volver (izq) + acciones (der)**

Aplicación directa:
- `register/page.tsx` y `/[id]/page.tsx` (modo edición) deberían respetarlo para coherencia UX.

---

## 🔄 Flujos de usuario (frontend)

### 1) Listado → Detalle
Pantalla: `/governance/prompts`
- Hoy: listado con **mockData** en el componente.
- Acciones recomendadas para modo productivo:
  - listar por API route (ej. `/api/governance/prompts?page=&size=&...`)
  - navegar a detalle `/governance/prompts/[id]`

### 2) Detalle → Analizar/Evaluar → Validations
Pantalla: `/governance/prompts/[id]`
- Debe disparar:
  - `POST /api/v1/prompts/{id}/analyze` (COMPLIANCE)
  - `POST /api/v1/prompts/{id}/evaluate` (PERFORMANCE)
- Luego refrescar `GET /api/v1/prompts/{id}/validations`.

### 3) Crear / Editar + versionado
Pantalla: `/governance/prompts/register`
- Al crear: `POST /api/v1/prompts/register`
- Al editar:
  - metadata-only: `PUT /api/v1/prompts/{id}`
  - content-changed: `POST /api/v1/prompts/{id}/versions` (o `PUT` si backend auto-versiona por contenido)

⚠️ Importante: hoy hay llamadas que apuntan a endpoints que no existen como API route Next (p.ej. `POST /api/governance/prompts/{id}/versions`).

---

## ✅ Recomendación de “modo productivo” (KISS)

Para alinear con la arquitectura (frontend → BFF/gateway):
- Definir **un único “entrypoint”** para Prompts:
  - Opción 1: Frontend llama siempre a `/api/governance/prompts/*` (API routes proxy) y estas apuntan al gateway/BFF.
  - Opción 2: Frontend llama a `/api/prompts/*` pero estas dejan de ser mock y se convierten en proxy real.

Regla: **no mezclar** mock hardcodeado + proxy + endpoints distintos en la misma pantalla.

---

## 🧪 Testing

El módulo incluye `/governance/prompts/testing` y API routes:
- `POST /api/governance/prompts/{id}/test`
- `POST /api/governance/prompts/{id}/test/judge`
- `POST /api/governance/prompts/{id}/test/compare`

Uso típico:
- ejecutar prompt contra modelo
- comparar vs expected
- (opcional) juez/score

---

## 🧯 Troubleshooting (fallos típicos)

- **404 en API route**: revisar si la pantalla llama `/api/prompts/*` (mock) vs `/api/governance/prompts/*` (proxy) y si existe el `route.ts` correspondiente.
- **Inconsistencia de paths backend**:
  - backend real del microservicio expone `/api/v1/prompts/*`
  - varias API routes Next apuntan a `/api/v1/governance/prompts/*` (debe corregirse o confirmarse que existe en tu gateway)
- **Traducciones no aparecen**: unificar claves (`prompts.*` vs `governance.prompts.*`) o añadir puente.
