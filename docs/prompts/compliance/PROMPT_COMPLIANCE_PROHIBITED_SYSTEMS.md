# 🚫 PROMPT DE IMPLEMENTACIÓN - SISTEMAS PROHIBIDOS (Art. 5)

**Módulo:** Compliance - Prohibited Systems
**Artículo EU AI Act:** Art. 5 - Sistemas Prohibidos
**Fecha:** Diciembre 2025
**Estado:** ⏳ Pendiente de implementación completa
**Esfuerzo Estimado:** 2-3 días

---

## 📋 RESUMEN DEL MÓDULO

### **Objetivo**
Implementar la verificación y gestión de sistemas de IA prohibidos según el Art. 5 del EU AI Act. El sistema debe detectar automáticamente si un proyecto de IA utiliza tecnologías prohibidas y bloquear su despliegue.

### **Pantallas Requeridas**

| # | Ruta Next.js | Estado | Descripción | Prioridad |
|---|--------------|-------|-------------|-----------|
| 1 | `app/(app)/governance/compliance/prohibited-systems/page.tsx` | ✅ Existe | Verificación de sistemas prohibidos | 🔴 Alta |
| 2 | `app/(app)/governance/compliance/prohibited-systems/catalog/page.tsx` | ❌ No existe | Catálogo de sistemas prohibidos | 🟡 Media |
| 3 | `app/(app)/governance/compliance/prohibited-systems/[id]/page.tsx` | ❌ No existe | Detalle de sistema prohibido detectado | 🟡 Media |

---

## 🏗️ ARQUITECTURA Y DEPENDENCIAS

### **Pantallas ZUL Originales**
- **ZUL Principal:** `console/gobierno/compliance/prohibited-systems.zul`
  - ViewModel asociado: No identificado específicamente (puede usar BusinessService directamente)
  - Ubicación: `src/main/webapp/console/gobierno/compliance/prohibited-systems.zul`

### **ViewModels Java (si existen)**
- No se ha identificado un ViewModel específico para esta pantalla
- La funcionalidad puede estar implementada directamente en el ZUL usando BusinessService

### **Entidades JPA**
- **ProhibitedSystem** - `com.codeflowx.govern.entity.compliance.ProhibitedSystem`
  - **Tabla:** `GOVPROHIBITEDSYSTEMS` (prefijo `GOV`)
  - **Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/ProhibitedSystem.java`
  - **Campos principales:**
    - `IDXPROHIBITEDSYSTEM` (Long, PK) - ID autonumérico
    - `PRSNAME` (String) - Nombre del sistema prohibido
    - `PRSDESCRIPTION` (String) - Descripción
    - `PRSCATEGORY` (String) - Categoría según Art. 5:
      - `ART_5_1_A` - Manipulación subliminal
      - `ART_5_1_B` - Explotación de vulnerabilidades
      - `ART_5_1_C` - Scoring social por autoridades públicas
      - `ART_5_1_D` - Identificación biométrica remota en tiempo real
    - `PRSKEYWORDS` (JSONB) - Keywords para detección automática
    - `PRSACTIVE` (Boolean) - Si está activo en el catálogo
    - `PRSCREATEDAT` (Timestamp) - Fecha creación
    - `PRSCREATEDBY` (String) - Usuario creador
    - `IDUUID` (String) - UUID único

### **Business Services Disponibles**
- **ProhibitedSystemBusinessService** ✅
  - **Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/ProhibitedSystemBusinessService.java`
  - **Métodos principales:**
    - `checkProhibitedSystem(Long projectId)` - Verifica si proyecto usa sistema prohibido
      - Consulta BBDD: `SELECT * FROM prjprojects WHERE idxproject = ?`
      - Consulta catálogo: `SELECT * FROM govprohibitedsystems WHERE prsactive = true`
      - Retorna: `ProhibitedSystemCheckResult` con sistemas detectados
    - `getActiveProhibitedSystems()` - Obtiene catálogo activo
      - Consulta: `SELECT * FROM govprohibitedsystems WHERE prsactive = true ORDER BY prsname`
    - `blockDeployment(Long projectId, String reason)` - Bloquea despliegue
      - Actualiza: `UPDATE prjprojects SET prjdeploymentblocked = true, prjblockreason = ? WHERE idxproject = ?`
    - `validateProhibitedSystems(Project project)` - Validación en ModelValidationService
      - Usado en: `ModelValidationService.validateProhibitedSystems()`

### **Servicios CRUD**
- **ProhibitedSystemService** ✅
  - **Ubicación:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/compliance/ProhibitedSystemService.java`
  - **Métodos:** `findAll()`, `findById()`, `save()`, `delete()`

### **Lógica de Negocio Detallada**

#### **Verificación de Sistemas Prohibidos:**
```java
// Pseudocódigo de la lógica
public ProhibitedSystemCheckResult checkProhibitedSystem(Long projectId) {
    // 1. Obtener proyecto
    Project project = projectService.findById(projectId);

    // 2. Obtener catálogo activo
    List<ProhibitedSystem> activeSystems = getActiveProhibitedSystems();

    // 3. Verificar coincidencias por keywords
    List<ProhibitedSystem> detected = new ArrayList<>();
    for (ProhibitedSystem system : activeSystems) {
        if (matchesKeywords(project, system.getKeywords())) {
            detected.add(system);
        }
    }

    // 4. Si se detecta, bloquear despliegue automáticamente
    if (!detected.isEmpty()) {
        blockDeployment(projectId, "Sistema prohibido detectado: " + detected.get(0).getName());
    }

    return new ProhibitedSystemCheckResult(detected, !detected.isEmpty());
}
```

#### **Matching de Keywords:**
- Compara keywords del catálogo con:
  - Nombre del proyecto (`PRJNAME`)
  - Descripción del proyecto (`PRJDESCRIPTION`)
  - Descripción de modelos asociados
  - Tags y categorías del proyecto

---

## 📱 PANTALLA 1: Verificación de Sistemas Prohibidos

### **Ruta:** `app/(app)/governance/compliance/prohibited-systems/page.tsx`

**Estado Actual:** ✅ Existe pero incompleta

### **Funcionalidades Requeridas:**

1. **Verificación Automática:**
   - Botón "Verificar Proyecto" que ejecuta verificación contra catálogo
   - Lista de proyectos verificados con estado (CLEAN, PROHIBITED, WARNING)
   - Indicador visual de riesgo (verde/amarillo/rojo)

2. **Listado de Sistemas Prohibidos Detectados:**
   - Tabla con proyectos que usan sistemas prohibidos
   - Filtros: por categoría, fecha, estado
   - Acciones: Ver detalle, Bloquear despliegue, Marcar como falso positivo

3. **Alertas y Notificaciones:**
   - Alertas cuando se detecta sistema prohibido
   - Notificación automática a responsables del proyecto
   - Bloqueo automático de despliegue si está configurado

### **Mock Data:**

```typescript
// app/(app)/governance/data/mockProhibitedSystems.ts
export const mockProhibitedSystems = {
  projects: [
    {
      id: 1,
      name: "AI Credit Scoring System",
      status: "PROHIBITED",
      detectedSystems: [
        {
          id: 1,
          name: "Social Scoring by Public Authorities",
          category: "Art. 5.1.c",
          detectedAt: "2025-12-01T10:30:00Z",
          confidence: 0.95
        }
      ],
      blocked: true
    },
    {
      id: 2,
      name: "Facial Recognition System",
      status: "WARNING",
      detectedSystems: [
        {
          id: 2,
          name: "Real-time Remote Biometric Identification",
          category: "Art. 5.1.d",
          detectedAt: "2025-12-01T11:00:00Z",
          confidence: 0.75
        }
      ],
      blocked: false
    }
  ],
  catalog: [
    {
      id: 1,
      name: "Social Scoring by Public Authorities",
      category: "Art. 5.1.c",
      description: "AI systems for social scoring by public authorities",
      keywords: ["social scoring", "public authority", "citizen rating"],
      active: true
    },
    {
      id: 2,
      name: "Real-time Remote Biometric Identification",
      category: "Art. 5.1.d",
      description: "Real-time remote biometric identification in publicly accessible spaces",
      keywords: ["biometric", "facial recognition", "real-time", "public space"],
      active: true
    }
  ]
};
```

### **API Routes Mock:**

```typescript
// app/api/compliance/prohibited-systems/route.ts
export async function GET(request: Request) {
  // Mock: Retornar lista de proyectos verificados
  return Response.json(mockProhibitedSystems.projects);
}

export async function POST(request: Request) {
  // Mock: Verificar proyecto
  const { projectId } = await request.json();
  // Simular verificación
  return Response.json({
    success: true,
    detected: true,
    systems: mockProhibitedSystems.projects[0].detectedSystems
  });
}
```

---

## 📱 PANTALLA 2: Catálogo de Sistemas Prohibidos

### **Ruta:** `app/(app)/governance/compliance/prohibited-systems/catalog/page.tsx`

**Estado Actual:** ❌ No existe - **CREAR**

### **Funcionalidades Requeridas:**

1. **Listado de Sistemas Prohibidos:**
   - Tabla con todos los sistemas del catálogo
   - Filtros: por categoría (Art. 5.1.a-d), activo/inactivo
   - Búsqueda por nombre o keywords

2. **Gestión del Catálogo:**
   - Crear nuevo sistema prohibido
   - Editar sistema existente
   - Activar/Desactivar sistema
   - Eliminar sistema (soft delete)

3. **Categorías según Art. 5:**
   - Art. 5.1.a - Manipulación subliminal
   - Art. 5.1.b - Explotación de vulnerabilidades
   - Art. 5.1.c - Scoring social por autoridades públicas
   - Art. 5.1.d - Identificación biométrica remota en tiempo real

### **Mock Data:**

```typescript
export const mockProhibitedSystemsCatalog = [
  {
    id: 1,
    name: "Social Scoring by Public Authorities",
    category: "Art. 5.1.c",
    description: "AI systems for social scoring by public authorities",
    keywords: ["social scoring", "public authority", "citizen rating", "behavioral scoring"],
    active: true,
    createdAt: "2025-01-15T00:00:00Z",
    updatedAt: "2025-12-01T00:00:00Z"
  },
  {
    id: 2,
    name: "Real-time Remote Biometric Identification",
    category: "Art. 5.1.d",
    description: "Real-time remote biometric identification in publicly accessible spaces",
    keywords: ["biometric", "facial recognition", "real-time", "public space", "surveillance"],
    active: true,
    createdAt: "2025-01-15T00:00:00Z",
    updatedAt: "2025-12-01T00:00:00Z"
  }
];
```

---

## 📱 PANTALLA 3: Detalle de Sistema Prohibido Detectado

### **Ruta:** `app/(app)/governance/compliance/prohibited-systems/[id]/page.tsx`

**Estado Actual:** ❌ No existe - **CREAR**

### **Funcionalidades Requeridas:**

1. **Información del Proyecto:**
   - Datos del proyecto que usa sistema prohibido
   - Modelos asociados
   - Estado del proyecto

2. **Detalles de la Detección:**
   - Sistema prohibido detectado
   - Nivel de confianza
   - Keywords que coincidieron
   - Fecha y hora de detección

3. **Acciones:**
   - Bloquear despliegue
   - Marcar como falso positivo
   - Ver evidencia de detección
   - Contactar responsable del proyecto

---

## 🎨 ESTILOS "WOW FACTOR"

Todas las pantallas deben implementar:

- ✅ Gradientes sutiles en backgrounds
- ✅ Partículas flotantes animadas
- ✅ Glassmorphism en cards
- ✅ Animaciones suaves
- ✅ Iconos de Lucide React
- ✅ Badges con colores según estado (verde/amarillo/rojo)

### **Estructura Base:**

```typescript
"use client";

import { useTranslation } from "@/app/config/i18n";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Ban, AlertTriangle } from "lucide-react";

export default function ProhibitedSystemsPage() {
  const { t } = useTranslation();

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-red-400/30 rounded-full animate-pulse" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        {/* Header */}
        <div className="text-center space-y-2">
          <div className="flex items-center justify-center gap-3 mb-4">
            <Ban className="w-8 h-8 text-red-500" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-red-500 to-red-700 bg-clip-text text-transparent">
              {t("compliance.prohibitedSystems.title", "Prohibited Systems")}
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
prohibitedSystems: {
  title: {
    es: "Sistemas Prohibidos",
    en: "Prohibited Systems"
  },
  subtitle: {
    es: "Verificación de sistemas prohibidos según Art. 5 del EU AI Act",
    en: "Prohibited systems verification according to EU AI Act Art. 5"
  },
  // ... más traducciones
}
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### **Pantalla 1: Verificación (Ya existe - Completar)**
- [ ] Revisar pantalla existente
- [ ] Añadir verificación automática
- [ ] Implementar listado de sistemas detectados
- [ ] Añadir alertas y notificaciones
- [ ] Implementar bloqueo de despliegue
- [ ] Añadir mock data
- [ ] Crear API routes mock

### **Pantalla 2: Catálogo (Crear nueva)**
- [ ] Crear página `catalog/page.tsx`
- [ ] Implementar listado con filtros
- [ ] Añadir CRUD de sistemas prohibidos
- [ ] Implementar activación/desactivación
- [ ] Añadir mock data
- [ ] Crear API routes mock
- [ ] Añadir entrada al menú

### **Pantalla 3: Detalle (Crear nueva)**
- [ ] Crear página `[id]/page.tsx`
- [ ] Implementar vista de detalle
- [ ] Añadir acciones (bloquear, falso positivo)
- [ ] Añadir mock data
- [ ] Crear API routes mock

### **General**
- [ ] Añadir traducciones
- [ ] Verificar estilos "Wow Factor"
- [ ] Probar funcionalidad completa
- [ ] Documentar integración con backend

---

## 🔗 REFERENCIAS

- **Documentación Compliance:** `docs/prompts/BUSINESS_LOGIC_COMPLIANCE.md`
- **Inventario Pantallas:** `docs/PANTALLAS_GOVERNANCE_COMPLIANCE_AI_ACT.md`
- **Artículo EU AI Act:** Art. 5 - Prohibited AI practices

---

**Última actualización:** Diciembre 2025
**Estado:** Listo para implementación en paralelo
