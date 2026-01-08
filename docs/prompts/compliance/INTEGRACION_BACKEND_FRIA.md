# INTEGRACIÓN BACKEND - MÓDULO FRIA

**Fecha:** Diciembre 2025
**Estado:** ✅ API Routes actualizadas según arquitectura

---

## 📋 RESUMEN

Todas las API routes del módulo FRIA han sido actualizadas para seguir la arquitectura establecida:

1. ✅ Verificación de `USE_MOCK` para activar/desactivar mocks
2. ✅ Llamadas al backend real cuando `USE_MOCK=false`
3. ✅ Uso de `BFF_BASE_URL` para comunicación con microservicios
4. ✅ Manejo de errores consistente
5. ✅ Documentación de endpoints en cada route

---

## 🔌 API ROUTES IMPLEMENTADAS

### 1. Crear FRIA
**Route:** `POST /api/compliance/fria/create`
**Backend:** `codeflowx-governance-fria-service`
**Endpoint Backend:** `POST /api/v1/fria/create`

```typescript
// Mock: Retorna friaId aleatorio
// Backend: Crea evaluación FRIA en base de datos
```

### 2. Actualizar Paso del Wizard
**Route:** `PUT /api/compliance/fria/[friaId]/step/[stepNumber]`
**Backend:** `codeflowx-governance-fria-service`
**Endpoint Backend:** `PUT /api/v1/fria/{friaId}/step/{stepNumber}`

```typescript
// Mock: Calcula completitud básica
// Backend: Guarda datos del paso y calcula completitud real
```

### 3. Calcular Riesgo Final
**Route:** `POST /api/compliance/fria/[friaId]/calculate-risk`
**Backend:** `codeflowx-governance-fria-service`
**Endpoint Backend:** `POST /api/v1/fria/{friaId}/calculate-risk`

```typescript
// Mock: Calcula riesgo según Anexo IX (fórmula implementada)
// Backend: Usa FriaAssessmentBusinessService.calculateFinalRisk()
```

### 4. Notificar Autoridades
**Route:** `POST /api/compliance/fria/[friaId]/notify-authority`
**Backend:** `codeflowx-governance-fria-service`
**Endpoint Backend:** `POST /api/v1/fria/{friaId}/notify-authority`

```typescript
// Mock: Simula notificación
// Backend: Llama a FriaAssessmentBusinessService.notifyAuthority()
// Integración Python (pendiente): leka-fria-generator
```

### 5. Validación Cruzada (INC-007)
**Route:** `POST /api/compliance/fria/[friaId]/cross-validate`
**Backend:** `codeflowx-governance-fria-service`
**Endpoint Backend:** `POST /api/v1/fria/{friaId}/cross-validate`

```typescript
// Mock: Simula validación cruzada
// Backend: Llama a microservicio Python leka-fria-generator
// Microservicio Python: POST /api/fria/cross-validate
```

### 6. Obtener Detalle FRIA
**Route:** `GET /api/compliance/fria/[friaId]`
**Backend:** `codeflowx-governance-fria-service`
**Endpoint Backend:** `GET /api/v1/fria/{friaId}`

```typescript
// Mock: Retorna datos mock completos
// Backend: Consulta base de datos y retorna FRIA completo
```

### 7. Listar Evaluaciones FRIA
**Route:** `GET /api/compliance/fria/assessments`
**Backend:** `codeflowx-governance-fria-service`
**Endpoint Backend:** `GET /api/v1/fria/assessments`

```typescript
// Mock: Retorna lista mock con filtros
// Backend: Consulta base de datos con filtros (status, search, etc.)
```

---

## ⚙️ CONFIGURACIÓN

### Variables de Entorno

```bash
# .env.local

# Activar/desactivar mocks
NEXT_PUBLIC_USE_MOCK=true

# URL del BFF (Backend for Frontend)
NEXT_PUBLIC_BFF_URL=http://localhost:8090

# URL del backend (alternativa si no hay BFF)
NEXT_PUBLIC_BACKEND_URL=http://localhost:8080
```

### Archivo de Configuración

**Ubicación:** `app/config/mock.ts`

```typescript
export const USE_MOCK = process.env.NEXT_PUBLIC_USE_MOCK === 'true';
export const BFF_BASE_URL = process.env.NEXT_PUBLIC_BFF_URL || 'http://localhost:8090';
export const BACKEND_BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL || 'http://localhost:8080';
```

---

## 🏗️ ARQUITECTURA DE COMUNICACIÓN

```
Frontend (Next.js)
    ↓ HTTP Request
API Route (app/api/compliance/fria/...)
    ↓ Verifica USE_MOCK
    ├─ USE_MOCK=true → Retorna datos mock
    └─ USE_MOCK=false → HTTP Request
        ↓
BFF (codeflowx.govern.bff.compliance)
    ↓ HTTP/WebClient (Reactivo)
Microservicio (codeflowx-governance-fria-service)
    ↓ Mono.fromCallable()
Servicio de Negocio (FriaAssessmentBusinessService)
    ↓
Repositorio (FriaAssessmentRepository)
    ↓
Base de Datos (PostgreSQL)
```

---

## 📝 PATRÓN DE API ROUTE

Todas las API routes siguen este patrón:

```typescript
import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, BFF_BASE_URL } from "@/app/config/mock";

export async function METHOD(request: NextRequest) {
  try {
    if (USE_MOCK) {
      // Retornar datos mock
      return NextResponse.json(mockData);
    }

    // Llamada real al backend
    const response = await fetch(`${BFF_BASE_URL}/api/v1/fria/...`, {
      method: "METHOD",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      throw new Error(`Backend error: ${response.statusText}`);
    }

    return NextResponse.json(await response.json());
  } catch (error) {
    console.error("Error:", error);
    return NextResponse.json(
      { success: false, error: "Error message" },
      { status: 500 }
    );
  }
}
```

---

## 🔄 ACTIVACIÓN DE BACKEND REAL

Para activar el backend real, solo hay que cambiar una variable:

```bash
# .env.local
NEXT_PUBLIC_USE_MOCK=false
```

**No se requiere modificar código en las pantallas** - el sistema automáticamente usará el backend real.

---

## 📚 MICROSERVICIOS REQUERIDOS

### Microservicio Principal
- **Nombre:** `codeflowx-governance-fria-service`
- **Puerto:** `809X` (definir puerto único)
- **Tecnología:** Spring Boot WebFlux (Reactivo)
- **Endpoints:** Ver sección "API Routes Implementadas"

### Microservicios Python (Integración Pendiente)
- **leka-fria-generator:** Validación cruzada y generación de documentos
- **codeflowx-governance-api:** Notificaciones a autoridades

---

## ✅ CHECKLIST DE INTEGRACIÓN

### Frontend (Completado)
- [x] Archivo de configuración `app/config/mock.ts` creado
- [x] Todas las API routes actualizadas con patrón USE_MOCK
- [x] Documentación de endpoints en cada route
- [x] Manejo de errores consistente
- [x] Variables de entorno documentadas

### Backend (Pendiente)
- [ ] Crear microservicio `codeflowx-governance-fria-service`
- [ ] Implementar endpoints según especificaciones
- [ ] Integrar con `FriaAssessmentBusinessService`
- [ ] Configurar BFF para routing
- [ ] Integrar con microservicios Python (cuando estén disponibles)

---

## 🔗 REFERENCIAS

- **Arquitectura Frontend:** `docs/ARQUITECTURA_FRONTEND.md`
- **Lógica de Negocio:** `docs/prompts/BUSINESS_LOGIC_COMPLIANCE.md`
- **Plantilla Microservicio:** `nocode.service/codeflowx-governance-classification-service/`

---

**Última actualización:** Diciembre 2025
**Estado:** Frontend listo - Pendiente implementación backend
