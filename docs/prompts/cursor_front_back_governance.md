# PROMPT: MIGRACIÓN DE PANTALLAS DE GOVERNANCE - POLÍTICAS

## 📋 DESCRIPCIÓN DEL PROMPT

Este prompt (`cursor_end_governance.md`) realiza la **migración de pantallas ZUL a Next.js** para el módulo de **Governance - Políticas**. Crea dos pantallas principales para la gestión de políticas de gobierno.

---

## 🎯 PROPÓSITO

Migrar las pantallas de gestión de políticas desde el sistema legacy (ZUL/Java) a Next.js con:
- Estilos "Wow Factor" (inspirado en Star Trek)
- Mock data para funcionamiento inmediato
- API routes preparadas para integración futura
- Internacionalización (español/inglés)
- Estructura moderna y responsive

---

## 📦 PANTALLAS CREADAS

### 1. **Vista General de Políticas** (`governance/overview`)

**Ubicación:** `app/(app)/governance/overview/page.tsx`

**Funcionalidad:**
- ✅ **Listado de políticas** con tabla paginada
- ✅ **Métricas principales:**
  - Total de políticas
  - Políticas activas
  - Total de violaciones
  - Score de cumplimiento
- ✅ **Filtros avanzados:**
  - Búsqueda por nombre
  - Filtro por tipo (SECURITY, COMPLIANCE, ETHICS, PRIVACY, OPERATIONAL)
  - Filtro por estado (ACTIVE, INACTIVE, DRAFT, UNDER_REVIEW, ARCHIVED)
  - Filtro por nivel de enforcement (MANDATORY, RECOMMENDED, OPTIONAL)
- ✅ **Acciones:**
  - Ver detalle de política
  - Crear nueva política
  - Eliminar política
- ✅ **Paginación** con navegación
- ✅ **Badges visuales** para tipos, estados y scores

**API Routes:**
- `GET /api/governance/policies` - Listar políticas con filtros y paginación
- `DELETE /api/governance/policies/[id]` - Eliminar política

**Mock Data:**
- 5 políticas de ejemplo
- Métricas calculadas automáticamente

---

### 2. **Detalle/Edición de Políticas** (`governance/detail/[id]`)

**Ubicación:** `app/(app)/governance/detail/[id]/page.tsx`

**Funcionalidad:**
- ✅ **Formulario completo** para crear/editar políticas:
  - Nombre, versión, tipo, categoría
  - Nivel de enforcement
  - Descripción, fechas (efectiva/expiración)
  - Estado, metadata
- ✅ **Gestión de reglas** (Policy Rules):
  - Listado de reglas asociadas
  - Condiciones y acciones
  - Prioridad y estado activo/inactivo
- ✅ **Evaluaciones** (Policy Evaluations):
  - Historial de evaluaciones
  - Fechas y resultados
  - Status de evaluación
- ✅ **Violaciones** (Policy Violations):
  - Listado de violaciones detectadas
  - Severidad y estado
  - Fechas de detección y resolución
- ✅ **Evaluaciones de cumplimiento** (Compliance Assessments):
  - Áreas de cumplimiento
  - Scores y hallazgos
- ✅ **Estadísticas de la política:**
  - Total de reglas
  - Total de evaluaciones
  - Total de violaciones
  - Score actual de la política
  - Score promedio de cumplimiento
  - Última evaluación
- ✅ **Acciones:**
  - Guardar política (crear/actualizar)
  - Evaluar política
  - Verificar cumplimiento
  - Navegar a overview

**API Routes:**
- `GET /api/governance/policies/[id]` - Obtener política con reglas, evaluaciones, violaciones
- `GET /api/governance/policies/new` - Obtener formulario vacío para nueva política
- `POST /api/governance/policies` - Crear nueva política
- `PUT /api/governance/policies/[id]` - Actualizar política
- `POST /api/governance/policies/[id]/evaluate` - Ejecutar evaluación
- `POST /api/governance/policies/[id]/compliance-check` - Verificar cumplimiento

**Mock Data:**
- 1 política de ejemplo con datos completos
- 2 reglas asociadas
- 2 evaluaciones
- 2 violaciones
- 2 evaluaciones de cumplimiento
- Estadísticas calculadas

---

## 🏗️ ARQUITECTURA

### Estructura de Archivos Creados

```
app/(app)/governance/
├── overview/
│   └── page.tsx                    # Vista general de políticas
└── detail/
    └── [id]/
        └── page.tsx               # Detalle/edición de políticas

app/api/governance/policies/
├── route.ts                        # GET (listar), POST (crear)
└── [id]/
    └── route.ts                    # GET (detalle), PUT (actualizar), DELETE
    └── evaluate/
        └── route.ts                # POST (evaluar política)
    └── compliance-check/
        └── route.ts                # POST (verificar cumplimiento)
```

### Componentes UI Utilizados

- `@/components/ui/card` - Cards con glassmorphism
- `@/components/ui/badge` - Badges con gradientes
- `@/components/ui/button` - Botones con estilos modernos
- `@/components/ui/input` - Inputs con estilos
- `@/components/ui/textarea` - Textareas
- `@/components/ui/development-banner` - Banner de desarrollo
- `lucide-react` - Iconos modernos

### Estilos "Wow Factor"

- ✅ Gradientes sutiles en backgrounds
- ✅ Partículas flotantes animadas
- ✅ Glassmorphism en cards y banners
- ✅ Gradientes en títulos
- ✅ Animaciones suaves
- ✅ Efectos hover en cards
- ✅ Badges con colores temáticos

---

## 🔌 INTEGRACIÓN CON BACKEND

### Estado Actual: **MOCK MODE**

Las pantallas funcionan con **mock data** para permitir desarrollo y testing sin backend.

### Desactivación de Mock (Futuro)

Cuando el backend esté disponible, cambiar en `.env.local`:
```bash
NEXT_PUBLIC_USE_MOCK=false
```

### Endpoints Backend Esperados

**Microservicio:** `leka-govern` (puerto 8000)

**Endpoints:**
- `GET /api/v1/governance/policies` - Listar políticas
- `GET /api/v1/governance/policies/{id}` - Obtener política
- `POST /api/v1/governance/policies` - Crear política
- `PUT /api/v1/governance/policies/{id}` - Actualizar política
- `DELETE /api/v1/governance/policies/{id}` - Eliminar política
- `POST /api/v1/governance/policies/{id}/evaluate` - Evaluar política
- `POST /api/v1/governance/policies/{id}/compliance-check` - Verificar cumplimiento

### Entidades JPA Relacionadas

- **Policy** - Entidad principal de políticas
- **PolicyRule** - Reglas asociadas a políticas
- **PolicyEvaluation** - Evaluaciones de políticas
- **PolicyViolation** - Violaciones detectadas
- **ComplianceAssessment** - Evaluaciones de cumplimiento

---

## 📊 DATOS Y MODELOS

### Interfaces TypeScript

**GovernancePolicy:**
```typescript
{
  idxpolicy: number;
  policyname: string;
  policytype: string;        // SECURITY, COMPLIANCE, ETHICS, PRIVACY, OPERATIONAL
  enforcementlevel: string;   // MANDATORY, RECOMMENDED, OPTIONAL
  version: number;
  status: string;             // ACTIVE, INACTIVE, DRAFT, UNDER_REVIEW, ARCHIVED
  totalviolations: number;
  avgcompliancescore: number;
}
```

**Policy (Detalle):**
```typescript
{
  idxpolicy?: number;
  name: string;
  version: number;
  policytype: string;
  category: string;
  enforcementlevel: string;
  description: string;
  effectivedate: string;
  expirationdate: string;
  status: string;
  metadata: string;
}
```

**PolicyRule:**
```typescript
{
  idxpolicyrule: number;
  name: string;
  conditiontext: string;
  actiontext: string;
  priority: number;
  isactive: boolean;
}
```

---

## 🌐 INTERNACIONALIZACIÓN

### Traducciones Incluidas

**Español/Inglés** para:
- Títulos y subtítulos
- Labels de formularios
- Mensajes de botones
- Estados y tipos
- Mensajes de error/éxito
- Placeholders

**Ubicación:** `app/config/i18n.ts`

---

## 📍 NAVEGACIÓN Y MENÚ

### Rutas Disponibles

- `/governance/overview` - Vista general
- `/governance/detail/[id]` - Detalle (editar)
- `/governance/detail/new` - Crear nueva política

### Entrada en Menú

**Ubicación:** `app/config/modules.ts`

Las pantallas deben estar registradas en el menú del módulo `governance` para acceso desde el sidebar.

---

## ✅ ESTADO ACTUAL

### Pantallas Implementadas

- ✅ `app/(app)/governance/overview/page.tsx` - **COMPLETADO**
- ✅ `app/(app)/governance/detail/[id]/page.tsx` - **COMPLETADO**

### API Routes Implementadas

- ✅ `app/api/governance/policies/route.ts` - **COMPLETADO**
- ✅ `app/api/governance/policies/[id]/route.ts` - **COMPLETADO**

### Estado en Backup

- ❌ **NO están en el folder backup**
- ✅ **Están en el código principal** (`app/(app)/governance/`)

**Nota:** Las pantallas de governance en el backup (`backup/app-app/`) son de otros módulos:
- `backup/app-app/agents/governance/` - Governance de agentes
- `backup/app-app/training/governance/` - Governance de entrenamiento
- `backup/app-app/monitoring/distributed/governance/` - Governance de monitoreo

---

## 🔄 FLUJO DE USUARIO

1. **Usuario accede a `/governance/overview`**
   - Ve listado de políticas con métricas
   - Puede filtrar y buscar
   - Puede crear nueva política

2. **Usuario hace clic en una política**
   - Navega a `/governance/detail/[id]`
   - Ve información completa
   - Puede editar y guardar

3. **Usuario crea nueva política**
   - Navega a `/governance/detail/new`
   - Completa formulario
   - Guarda y regresa a overview

4. **Usuario ejecuta acciones**
   - Evaluar política
   - Verificar cumplimiento
   - Ver reglas, evaluaciones, violaciones

---

## 📝 NOTAS IMPORTANTES

1. **Mock Data:** Todas las pantallas funcionan con datos mock para desarrollo
2. **Estilos:** Mantienen el estilo "Wow Factor" consistente con el resto de la aplicación
3. **Responsive:** Diseño adaptativo para móvil, tablet y desktop
4. **Validaciones:** Formularios incluyen validaciones básicas
5. **Error Handling:** Manejo de errores en llamadas API
6. **Loading States:** Estados de carga durante peticiones

---

## 🚀 PRÓXIMOS PASOS

1. **Integración Backend:** Conectar con microservicio `leka-govern`
2. **Validaciones Avanzadas:** Agregar validaciones más robustas
3. **Testing:** Crear tests unitarios y de integración
4. **Documentación:** Documentar APIs y flujos
5. **Optimizaciones:** Mejorar rendimiento y UX

---

**Última actualización:** Diciembre 2025
**Estado:** ✅ Implementado y funcional con mock data
