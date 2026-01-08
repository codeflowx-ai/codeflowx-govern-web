# 🔌 INTEGRACIÓN BACKEND - QMS (Art. 17)

**Módulo:** Compliance - Quality Management System
**Artículo EU AI Act:** Art. 17
**Fecha:** Diciembre 2025
**Estado:** ✅ Frontend listo, pendiente integración backend

---

## 📋 RESUMEN

El módulo QMS está completamente implementado en el frontend con:
- ✅ Dashboard QMS con los 13 módulos
- ✅ Revisión de gaps QMS (BPMN)
- ✅ API Routes creadas (soporte mock/real)
- ✅ Integración lista para conectar con backend

---

## 🏗️ ARQUITECTURA DE INTEGRACIÓN

### Flujo de Datos

```
Frontend (Next.js)
    ↓ HTTP
API Routes (app/api/governance/compliance/qms/)
    ↓
BFF (codeflowx.govern.bff.compliance) [Reactivo - WebFlux]
    ↓ HTTP/WebClient (Reactivo)
Microservicio QMS (codeflowx-governance-qms-service) [Reactivo - WebFlux]
    ↓ Mono.fromCallable()
Servicio de Negocio (QualityManagementSystemBusinessService) [Síncrono - JPA]
    ↓
Repositorio (QualityManagementSystemRepository) [Síncrono - JPA]
    ↓
Entidad JPA (QualityManagementSystem)
```

---

## 📡 API ROUTES IMPLEMENTADAS

### 1. GET /api/governance/compliance/qms

**Descripción:** Obtiene los datos del QMS para un proyecto

**Query Params:**
- `projectId` (requerido): ID del proyecto

**Respuesta Mock:**
```json
{
  "success": true,
  "data": {
    "projectId": 1,
    "projectName": "AI Credit Scoring System",
    "overallScore": 0.82,
    "complianceStatus": "PARTIAL",
    "modules": [...],
    "gaps": [...],
    "improvementPlan": [...],
    "historicalScores": [...]
  }
}
```

**Backend Endpoint Esperado:**
- `GET ${BFF_BASE_URL}/api/v1/qms?projectId={projectId}`

**Ubicación:** `app/api/governance/compliance/qms/route.ts`

---

### 2. POST /api/governance/compliance/qms

**Descripción:** Calcula o actualiza el score QMS para un proyecto

**Body:**
```json
{
  "projectId": 1,
  "action": "calculate" // "calculate" | "update"
}
```

**Respuesta Mock:**
```json
{
  "success": true,
  "message": "QMS score calculated successfully",
  "data": { ... }
}
```

**Backend Endpoints Esperados:**
- `POST ${BFF_BASE_URL}/api/v1/qms/calculate` (para calcular)
- `PUT ${BFF_BASE_URL}/api/v1/qms` (para actualizar)

**Ubicación:** `app/api/governance/compliance/qms/route.ts`

---

### 3. GET /api/governance/compliance/qms/gaps

**Descripción:** Obtiene los gaps detectados del QMS para un proyecto

**Query Params:**
- `projectId` (requerido): ID del proyecto

**Respuesta Mock:**
```json
{
  "success": true,
  "data": {
    "gaps": [...],
    "overallScore": 0.82,
    "projectId": 1
  }
}
```

**Backend Endpoint Esperado:**
- `GET ${BFF_BASE_URL}/api/v1/qms/gaps?projectId={projectId}`

**Ubicación:** `app/api/governance/compliance/qms/gaps/route.ts`

---

### 4. POST /api/governance/compliance/qms/review

**Descripción:** Procesa una revisión de gaps QMS (aprobar o solicitar correcciones)

**Body:**
```json
{
  "projectId": 1,
  "reviewerName": "Juan Pérez",
  "reviewNotes": "Revisión completa del QMS...",
  "decision": "APPROVED", // "APPROVED" | "CORRECTIONS_REQUIRED"
  "taskId": "task-123" // Opcional: ID de tarea BPMN
}
```

**Respuesta Mock:**
```json
{
  "success": true,
  "message": "QMS approved successfully",
  "data": {
    "projectId": 1,
    "decision": "APPROVED",
    "reviewerName": "Juan Pérez",
    "reviewNotes": "...",
    "reviewedAt": "2025-12-15T10:30:00Z",
    "taskCompleted": true
  }
}
```

**Backend Endpoint Esperado:**
- `POST ${BFF_BASE_URL}/api/v1/qms/review`

**Ubicación:** `app/api/governance/compliance/qms/review/route.ts`

---

## 🔧 CONFIGURACIÓN

### Variables de Entorno

```bash
# .env.local
NEXT_PUBLIC_USE_MOCK=true  # Activar/desactivar mocks
NEXT_PUBLIC_BFF_URL=http://localhost:8090  # URL del BFF
```

**Ubicación:** `app/config/mock.ts`

---

## 📦 MICROSERVICIO BACKEND REQUERIDO

### Estructura Esperada

```
codeflowx-governance-qms-service/
├── pom.xml
├── src/main/
│   ├── java/com/codeflowx/govern/qms/
│   │   ├── QmsServiceApplication.java
│   │   ├── controller/
│   │   │   └── QmsController.java
│   │   └── config/
│   │       └── WebClientConfig.java
│   └── resources/
│       └── application.yml
```

### Endpoints del Microservicio

#### 1. GET /api/v1/qms

**Descripción:** Obtiene datos del QMS para un proyecto

**Query Params:**
- `projectId` (Long, requerido)

**Respuesta:**
```json
{
  "projectId": 1,
  "projectName": "AI Credit Scoring System",
  "overallScore": 0.82,
  "complianceStatus": "PARTIAL",
  "modules": [
    {
      "name": "COMPLIANCE_STRATEGY",
      "code": "CS",
      "displayName": "Estrategia de Compliance",
      "score": 0.90,
      "threshold": 0.80,
      "gaps": []
    },
    // ... 12 módulos más
  ],
  "gaps": [...],
  "improvementPlan": [...],
  "historicalScores": [...]
}
```

#### 2. POST /api/v1/qms/calculate

**Descripción:** Calcula el score QMS para un proyecto

**Body:**
```json
{
  "projectId": 1
}
```

**Respuesta:**
```json
{
  "projectId": 1,
  "overallScore": 0.82,
  "modules": [...],
  "calculatedAt": "2025-12-15T10:30:00Z"
}
```

#### 3. GET /api/v1/qms/gaps

**Descripción:** Obtiene gaps detectados del QMS

**Query Params:**
- `projectId` (Long, requerido)

**Respuesta:**
```json
{
  "gaps": [
    {
      "module": "DATA_GOVERNANCE",
      "currentScore": 0.75,
      "targetScore": 0.80,
      "gap": 0.05,
      "description": "Faltan métricas de calidad de datos",
      "severity": "MEDIUM",
      "recommendedActions": [...]
    }
  ],
  "overallScore": 0.82,
  "projectId": 1
}
```

#### 4. POST /api/v1/qms/review

**Descripción:** Procesa revisión de gaps QMS

**Body:**
```json
{
  "projectId": 1,
  "reviewerName": "Juan Pérez",
  "reviewNotes": "Revisión completa...",
  "decision": "APPROVED",
  "taskId": "task-123"
}
```

**Respuesta:**
```json
{
  "projectId": 1,
  "decision": "APPROVED",
  "reviewerName": "Juan Pérez",
  "reviewNotes": "...",
  "reviewedAt": "2025-12-15T10:30:00Z",
  "taskCompleted": true
}
```

---

## 🔄 SERVICIOS DE NEGOCIO REQUERIDOS

### QualityManagementSystemBusinessService

**Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/QualityManagementSystemBusinessService.java`

**Métodos Requeridos:**

```java
// Calcular score QMS overall
BigDecimal calculateQmsComplianceScore(Long projectId);

// Obtener gaps QMS
List<QmsGap> getQmsGaps(Long projectId);

// Obtener datos completos del QMS
QualityManagementSystem getQmsData(Long projectId);

// Procesar revisión de gaps
void processQmsReview(Long projectId, String reviewerName,
                     String reviewNotes, String decision, String taskId);
```

---

## 📊 ENTIDADES JPA

### QualityManagementSystem

**Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/QualityManagementSystem.java`

**Tabla:** `GOVQUALITYMANAGEMENTSYSTEMS`

**Campos Principales:**
- `IDXQUALITYMANAGEMENTSYSTEM` (Long, PK)
- `IDXPROJECT` (Long, FK)
- `QMSOVERALLSCORE` (BigDecimal)
- `QMSMODULESCORES` (JSONB)
- `QMSGAPS` (JSONB)
- `QMSCOMPLIANCESTRATEGY` (String)
- `QMSCREATEDAT` (Timestamp)
- `IDUUID` (String)

---

## ✅ CHECKLIST DE INTEGRACIÓN

### Frontend
- [x] API Routes creadas con soporte mock/real
- [x] Pantallas actualizadas para usar API routes
- [x] Manejo de errores implementado
- [x] Fallback a mock data en caso de error

### Backend (Pendiente)
- [ ] Crear microservicio `codeflowx-governance-qms-service`
- [ ] Implementar controller reactivo con endpoints
- [ ] Configurar WebClient para comunicación
- [ ] Implementar servicios de negocio
- [ ] Crear/verificar repositorio
- [ ] Configurar application.yml
- [ ] Agregar al pom.xml padre
- [ ] Documentar con Swagger/OpenAPI
- [ ] Probar endpoints con Swagger UI

### BFF (Pendiente)
- [ ] Crear módulo BFF para QMS
- [ ] Configurar rutas en BFF
- [ ] Implementar proxy a microservicio

### Testing
- [ ] Probar integración end-to-end
- [ ] Verificar manejo de errores
- [ ] Validar formato de respuestas
- [ ] Probar con datos reales

---

## 🔗 REFERENCIAS

- **Documentación Arquitectura:** `docs/ARQUITECTURA_FRONTEND.md`
- **Prompt QMS:** `docs/prompts/compliance/PROMPT_COMPLIANCE_QMS.md`
- **Plantilla Microservicio:** `nocode.service/codeflowx-governance-classification-service/`
- **Servicios de Negocio:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/`

---

**Última actualización:** Diciembre 2025
**Estado:** Frontend listo, pendiente implementación backend



