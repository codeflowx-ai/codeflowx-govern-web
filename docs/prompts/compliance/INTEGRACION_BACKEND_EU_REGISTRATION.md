# 🔌 INTEGRACIÓN BACKEND - EU REGISTRATION

**Módulo:** Compliance - EU Registration
**Fecha:** Diciembre 2025
**Estado:** ✅ Integración Frontend completada

---

## 📋 RESUMEN

Este documento describe la integración del módulo de Registro UE con el backend según la arquitectura establecida.

---

## 🏗️ ARQUITECTURA DE INTEGRACIÓN

```
Frontend (Next.js)
    ↓ HTTP
API Routes (Next.js) - app/api/compliance/eu-registration/
    ↓ HTTP/WebClient
BFF (codeflowx.govern.bff.compliance) [Reactivo - WebFlux]
    ↓ HTTP/WebClient (Reactivo)
Microservicio: codeflowx-governance-eu-registration-service [Reactivo - WebFlux]
    ↓ Mono.fromCallable() (envuelve llamadas síncronas)
Business Service: EuRegistrationBusinessService [Síncrono - JPA]
    ↓
Repository: EuRegistrationRepository [Síncrono - JPA]
    ↓
Entidad: EuRegistration (REGEUREGISTRATIONS) [nocode.service.entitys]
```

---

## 🔌 API ROUTES (FRONTEND)

### 1. POST /api/compliance/eu-registration

**Descripción:** Guarda o actualiza una sección del formulario de registro UE

**Request:**
```typescript
{
  projectId: number;
  section: "A" | "B" | "C";
  data: object; // Datos de la sección
  registrationId?: number; // Opcional, para actualizar registro existente
}
```

**Response:**
```typescript
{
  success: boolean;
  registrationId: number;
  section: string;
  status: "DRAFT" | "PENDING" | "SUBMITTED" | "REGISTERED" | "REJECTED";
  message?: string;
}
```

**Backend Endpoint:** `POST /api/v1/eu-registrations/section`
**Business Service:** `EuRegistrationBusinessService.updateSubmissionData()`

---

### 2. GET /api/compliance/eu-registration

**Descripción:** Obtiene los datos de registro UE para un proyecto

**Query Parameters:**
- `projectId` (required): ID del proyecto

**Response:**
```typescript
{
  success: boolean;
  data: {
    registrationId: number;
    projectId: number;
    projectName: string;
    status: string;
    registrationType: "STANDARD" | "SENSITIVE" | "NATIONAL";
    sectionA: { ... };
    sectionB: { ... };
    sectionC: { ... };
  };
}
```

**Backend Endpoint:** `GET /api/v1/eu-registrations?projectId={projectId}`
**Business Service:** `EuRegistrationBusinessService.getRegistrationByProjectId()`

---

### 3. POST /api/compliance/eu-registration/submit

**Descripción:** Envía el registro a la Base de Datos UE según Art. 49 y Anexo VIII

**Request:**
```typescript
{
  registrationId: number;
}
```

**Response:**
```typescript
{
  success: boolean;
  registrationId: number;
  status: "SUBMITTED" | "REGISTERED" | "REJECTED";
  euRegistrationId?: string; // ID de registro en BD UE
  submittedAt: string; // ISO 8601
  message?: string;
}
```

**Backend Endpoint:** `POST /api/v1/eu-registrations/{registrationId}/submit`
**Business Service:** `EuRegistrationBusinessService.submitToEuDatabase()`

**Nota:** El método `submitToEuDatabase()` genera el payload JSON según Anexo VIII y llama al microservicio Python para registro en EU Database (actualmente comentado en el código Java).

---

### 4. POST /api/compliance/eu-registration/[id]/resubmit

**Descripción:** Reenvía un registro rechazado a la Base de Datos UE

**Request:** (No body, ID en URL)

**Response:**
```typescript
{
  success: boolean;
  registrationId: number;
  status: "SUBMITTED";
  euRegistrationId?: string;
  submittedAt: string;
  message?: string;
}
```

**Backend Endpoint:** `POST /api/v1/eu-registrations/{registrationId}/resubmit`
**Business Service:** `EuRegistrationBusinessService.submitToEuDatabase()`

---

### 5. GET /api/compliance/eu-registration/status

**Descripción:** Obtiene el listado de todos los registros UE con sus estados

**Query Parameters:**
- `projectId` (optional): Filtrar por proyecto
- `status` (optional): Filtrar por estado (DRAFT, PENDING, SUBMITTED, REGISTERED, REJECTED)
- `type` (optional): Filtrar por tipo (STANDARD, SENSITIVE, NATIONAL)

**Response:**
```typescript
{
  success: boolean;
  data: Array<{
    id: number;
    projectId: number;
    projectName: string;
    registrationType: string;
    status: string;
    submittedAt: string | null;
    euRegistrationId: string | null;
    response: object | null;
  }>;
  total: number;
}
```

**Backend Endpoint:** `GET /api/v1/eu-registrations/status?projectId={projectId}&status={status}&type={type}`
**Business Service:** `EuRegistrationService.findAll()` con filtros

---

## 🎯 MICROSERVICIO BACKEND

### Nombre del Microservicio

**`codeflowx-governance-eu-registration-service`**

### Estructura Esperada

```
codeflowx-governance-eu-registration-service/
├── pom.xml
├── src/main/
│   ├── java/com/codeflowx/govern/euregistration/
│   │   ├── EuRegistrationServiceApplication.java
│   │   ├── controller/
│   │   │   └── EuRegistrationController.java
│   │   ├── config/
│   │   │   └── WebClientConfig.java
│   │   └── exception/
│   │       └── GlobalExceptionHandler.java
│   └── resources/
│       └── application.yml
└── README.md
```

### Endpoints del Controller

```java
@RestController
@RequestMapping("/api/v1/eu-registrations")
@RequiredArgsConstructor
@Slf4j
public class EuRegistrationController {

    private final EuRegistrationBusinessService businessService;

    @PostMapping("/section")
    public Mono<ResponseEntity<EuRegistrationDto>> saveSection(@RequestBody SaveSectionRequest request) {
        // Implementación reactiva
    }

    @GetMapping
    public Mono<ResponseEntity<EuRegistrationDto>> getByProjectId(@RequestParam Long projectId) {
        // Implementación reactiva
    }

    @PostMapping("/{registrationId}/submit")
    public Mono<ResponseEntity<SubmitResponse>> submitToEuDatabase(@PathVariable Long registrationId) {
        // Implementación reactiva
    }

    @PostMapping("/{registrationId}/resubmit")
    public Mono<ResponseEntity<SubmitResponse>> resubmit(@PathVariable Long registrationId) {
        // Implementación reactiva
    }

    @GetMapping("/status")
    public Mono<ResponseEntity<List<EuRegistrationStatusDto>>> getStatus(
        @RequestParam(required = false) Long projectId,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String type
    ) {
        // Implementación reactiva
    }
}
```

---

## 📦 SERVICIOS DE NEGOCIO

### EuRegistrationBusinessService

**Ubicación:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/EuRegistrationBusinessService.java`

**Métodos Principales:**

1. **`createRegistration(Long projectId, String registrationType, String createdBy)`**
   - Crea un nuevo registro
   - Consulta: `SELECT * FROM prjprojects WHERE idxproject = ? AND prjishighrisk = true`
   - INSERT: `INSERT INTO regeuregistrations (...)`

2. **`updateSubmissionData(Long registrationId, String section, Map<String, Object> data)`**
   - Actualiza una sección del formulario
   - UPDATE: `UPDATE regeuregistrations SET regsubmissiondata = ?, regsection = ? WHERE idxregregistration = ?`

3. **`validateSubmissionData(Long registrationId)`**
   - Valida que todas las secciones estén completas
   - Valida formato según Anexo VIII

4. **`submitToEuDatabase(Long registrationId)`**
   - Genera payload JSON según Anexo VIII
   - Llamada microservicio (COMENTADA): `codeflowx-governance-api: POST /api/v1/eu-registrations/register`
   - UPDATE: `UPDATE regeuregistrations SET regstatus = 'SUBMITTED', regsubmittedat = ? WHERE idxregregistration = ?`

5. **`getRegistrationStatus(Long registrationId)`**
   - Obtiene estado del registro
   - Consulta: `SELECT * FROM regeuregistrations WHERE idxregregistration = ?`

---

## 🗄️ ENTIDAD JPA

### EuRegistration

**Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/EuRegistration.java`

**Tabla:** `REGEUREGISTRATIONS` (prefijo `REG`)

**Campos Principales:**
- `IDXREGREGISTRATION` (Long, PK) - ID autonumérico
- `IDXPROJECT` (Long, FK) - Referencia a proyecto
- `REGREGISTRATIONTYPE` (String) - Tipo: STANDARD, SENSITIVE, NATIONAL
- `REGSECTION` (String) - Sección actual: A, B, C
- `REGSUBMISSIONDATA` (JSONB) - Datos de envío según Anexo VIII
- `REGSTATUS` (String) - Estado: DRAFT, PENDING, SUBMITTED, REGISTERED, REJECTED
- `REGREGISTRATIONID` (String) - ID de registro en BD UE (si existe)
- `REGRESPONSE` (JSONB) - Respuesta de la BD UE
- `REGERROR` (String) - Error si falla el registro
- `REGCREATEDAT` (Timestamp) - Fecha creación
- `REGCREATEDBY` (String) - Usuario creador
- `REGSUBMITTEDAT` (Timestamp) - Fecha de envío
- `IDUUID` (String) - UUID único

---

## 🔧 CONFIGURACIÓN

### Variables de Entorno

```bash
# .env.local
NEXT_PUBLIC_USE_MOCK=true  # Activar/desactivar mocks
NEXT_PUBLIC_BFF_URL=http://localhost:8090  # URL del BFF
```

### Configuración Mock

Las API routes verifican `USE_MOCK` desde `@/app/config/mock`:
- Si `USE_MOCK === true`: Retorna datos mock
- Si `USE_MOCK === false`: Llama al BFF usando `BFF_BASE_URL`

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN BACKEND

### Microservicio

- [ ] Crear estructura del microservicio siguiendo plantilla de `codeflowx-governance-classification-service`
- [ ] Configurar `pom.xml` con dependencias WebFlux
- [ ] Crear `EuRegistrationServiceApplication.java`
- [ ] Crear `EuRegistrationController.java` con endpoints reactivos
- [ ] Configurar `application.yml` con puerto único
- [ ] Agregar módulo al `pom.xml` padre

### Business Service

- [ ] Verificar `EuRegistrationBusinessService` existe
- [ ] Implementar métodos faltantes si es necesario
- [ ] Agregar logging con `@Slf4j`
- [ ] Verificar validaciones según Anexo VIII

### Repository

- [ ] Verificar `EuRegistrationRepository` existe
- [ ] Usar `GenericRepository` si solo necesita CRUD
- [ ] Crear queries específicas si es necesario

### DTOs

- [ ] Verificar DTOs en `codeflowx.govern.nocode.dtos`
- [ ] Crear DTOs faltantes en librería compartida si es necesario
- [ ] NO crear DTOs en el microservicio

### Testing

- [ ] Probar endpoints con Swagger UI
- [ ] Verificar integración con frontend
- [ ] Probar modo mock y modo real

---

## 📚 REFERENCIAS

- **Arquitectura Frontend:** `docs/ARQUITECTURA_FRONTEND.md`
- **Prompt de Implementación:** `docs/prompts/compliance/PROMPT_COMPLIANCE_EU_REGISTRATION.md`
- **Plantilla Microservicio:** `nocode.service/codeflowx-governance-classification-service/`
- **Business Service:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/EuRegistrationBusinessService.java`
- **Entidad JPA:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/EuRegistration.java`

---

**Última actualización:** Diciembre 2025
**Estado:** Frontend completado, Backend pendiente de implementación



