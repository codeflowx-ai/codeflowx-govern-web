# 🔌 INTEGRACIÓN BACKEND - HITL SUPERVISION

**Módulo:** Compliance - HITL Supervision (Art. 14 EU AI Act)
**Fecha:** Diciembre 2025
**Estado:** ✅ Frontend implementado - Pendiente microservicio backend

---

## 📋 RESUMEN

El módulo de HITL Supervision está implementado en el frontend siguiendo la arquitectura establecida. Las API routes están preparadas para integrarse con el microservicio de backend cuando esté disponible.

---

## 🏗️ ARQUITECTURA DE INTEGRACIÓN

### Flujo de Comunicación

```
Frontend (Next.js)
    ↓ HTTP Request
API Route (app/api/compliance/hitl/...)
    ↓ Verifica USE_MOCK
    ├─ USE_MOCK=true → Retorna datos mock
    └─ USE_MOCK=false → HTTP Request
        ↓
BFF (codeflowx.govern.bff.compliance)
    ↓ HTTP/WebClient (Reactivo)
Microservicio (codeflowx-governance-hitl-service)
    ↓ Mono.fromCallable()
Servicio de Negocio (HitlSupervisionService)
    ↓
Repositorio (HitlSupervisionRepository, HitlDecisionRepository)
    ↓
Base de Datos (PostgreSQL - GOVHITLSUPERVISIONS, GOVHITLDECISIONS)
```

---

## 📡 API ROUTES IMPLEMENTADAS

### 1. Dashboard HITL

**Ruta Frontend:** `GET /api/compliance/hitl/dashboard`

**Endpoint Backend Esperado:** `GET /api/v1/hitl/dashboard?projectId={projectId}`

**BFF:** `codeflowx.govern.bff.compliance` (enruta a microservicio)
**Microservicio:** `codeflowx-governance-hitl-service`

**Descripción:** Obtiene métricas y datos consolidados del dashboard de HITL.

**Query Params:**
- `projectId` (opcional): Filtrar por proyecto

**Response:**
```json
{
  "success": true,
  "data": {
    "metrics": {
      "averageResponseTime": 2.5,
      "approvalRate": 0.85,
      "slaCompliance": 0.92,
      "pendingInterventions": 12,
      "totalInterventions": 150,
      "interventionsByType": { "AGENT": 45, "MODEL": 62, "PROMPT": 43 },
      "interventionsByStatus": { "PENDING": 12, "IN_REVIEW": 8, "APPROVED": 105, "REJECTED": 25 }
    },
    "pendingInterventions": [...],
    "recentDecisions": [...],
    "supervisionConfig": [...]
  }
}
```

---

### 2. Intervenciones HITL

**Ruta Frontend:** `GET /api/compliance/hitl/interventions`

**Endpoint Backend Esperado:** `GET /api/v1/hitl/interventions?projectId={projectId}&status={status}&type={type}`

**Microservicio:** `codeflowx-governance-hitl-service`

**Descripción:** Obtiene intervenciones HITL con filtros opcionales.

**Query Params:**
- `projectId` (opcional): Filtrar por proyecto
- `status` (opcional): Filtrar por estado (PENDING, IN_REVIEW, APPROVED, REJECTED)
- `type` (opcional): Filtrar por tipo de entidad (Agent, Model, Prompt)

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "type": "AGENT_APPROVAL",
      "entityType": "Agent",
      "entityId": 123,
      "entityName": "Credit Scoring Agent",
      "status": "PENDING",
      "createdAt": "2025-12-01T10:00:00Z",
      "slaDeadline": "2025-12-01T14:00:00Z",
      "slaHours": 4,
      "timeRemaining": 2.5,
      "urgency": "MEDIUM"
    }
  ]
}
```

---

### 3. Registrar Decisión HITL

**Ruta Frontend:** `POST /api/compliance/hitl/interventions`

**Endpoint Backend Esperado:** `POST /api/v1/hitl/interventions`

**Microservicio:** `codeflowx-governance-hitl-service`

**Descripción:** Registra una decisión humana para una intervención HITL.

**Body:**
```json
{
  "interventionId": 1,
  "decision": "APPROVED",
  "reason": "Approved after review",
  "userId": "user@example.com"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "type": "AGENT_APPROVAL",
    "entityType": "Agent",
    "entityId": 123,
    "entityName": "Credit Scoring Agent",
    "decision": "APPROVED",
    "decisionReason": "Approved after review",
    "responseTime": 2.5,
    "decisionDate": "2025-12-01T12:30:00Z",
    "userId": "user@example.com"
  }
}
```

---

### 4. Configuración HITL

**Ruta Frontend:** `GET /api/compliance/hitl/config`

**Endpoint Backend Esperado:** `GET /api/v1/hitl/config?projectId={projectId}`

**Microservicio:** `codeflowx-governance-hitl-service`

**Descripción:** Obtiene la configuración de supervisión HITL.

**Query Params:**
- `projectId` (opcional): Filtrar por proyecto

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "type": "PRE_DEPLOYMENT",
      "enabled": true,
      "slaHours": 8,
      "requiredRoles": ["governance-admin", "compliance-officer"],
      "autoEscalation": true,
      "escalationHours": 12
    }
  ]
}
```

---

### 5. Actualizar Configuración HITL

**Ruta Frontend:** `POST /api/compliance/hitl/config`

**Endpoint Backend Esperado:** `POST /api/v1/hitl/config`

**Microservicio:** `codeflowx-governance-hitl-service`

**Descripción:** Actualiza la configuración de supervisión HITL.

**Body:**
```json
{
  "type": "PRE_DEPLOYMENT",
  "enabled": true,
  "slaHours": 8,
  "requiredRoles": ["governance-admin", "compliance-officer"],
  "autoEscalation": true,
  "escalationHours": 12
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "type": "PRE_DEPLOYMENT",
    "enabled": true,
    "slaHours": 8,
    "requiredRoles": ["governance-admin", "compliance-officer"],
    "autoEscalation": true,
    "escalationHours": 12
  }
}
```

---

## 🔧 CONFIGURACIÓN

### Variables de Entorno

```bash
# .env.local
NEXT_PUBLIC_USE_MOCK=true  # true para usar datos mock, false para backend real
NEXT_PUBLIC_BACKEND_URL=http://localhost:8090  # URL base del backend
```

### Archivo de Configuración

**Ubicación:** `app/config/mock.ts`

```typescript
export const USE_MOCK = process.env.NEXT_PUBLIC_USE_MOCK === 'true';
export const BACKEND_BASE_URL = process.env.NEXT_PUBLIC_BACKEND_URL || 'http://localhost:8080';

export const BACKEND_ENDPOINTS = {
  compliance: {
    hitl: {
      dashboard: `${BACKEND_BASE_URL}/api/v1/hitl/dashboard`,
      interventions: `${BACKEND_BASE_URL}/api/v1/hitl/interventions`,
      config: `${BACKEND_BASE_URL}/api/v1/hitl/config`,
    },
  },
};
```

---

## 🏗️ MICROSERVICIO BACKEND REQUERIDO

### Estructura Esperada

Seguir la plantilla de referencia: `codeflowx-governance-classification-service`

**Nombre del Microservicio:** `codeflowx-governance-hitl-service`

**Puerto:** `8091` (configurable)

**Estructura:**

```
codeflowx-governance-hitl-service/
├── pom.xml
├── src/main/
│   ├── java/com/codeflowx/govern/hitl/
│   │   ├── HitlServiceApplication.java
│   │   ├── controller/
│   │   │   └── HitlController.java  # REST Controller reactivo
│   │   ├── service/
│   │   │   └── HitlAIService.java  # Servicios adicionales (si aplica)
│   │   ├── config/
│   │   │   └── WebClientConfig.java
│   │   └── exception/
│   │       └── GlobalExceptionHandler.java
│   └── resources/
│       └── application.yml
└── README.md
```

### Dependencias Requeridas

```xml
<dependencies>
  <!-- WebFlux (NO spring-boot-starter-web) -->
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
  </dependency>

  <!-- JPA -->
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>

  <!-- Servicios de Negocio -->
  <dependency>
    <groupId>com.codeflowx.govern</groupId>
    <artifactId>codeflowx.govern.business</artifactId>
  </dependency>

  <!-- Repositorios -->
  <dependency>
    <groupId>com.codeflowx.govern</groupId>
    <artifactId>codeflowx.govern.repository</artifactId>
  </dependency>

  <!-- DTOs -->
  <dependency>
    <groupId>com.codeflowx.govern</groupId>
    <artifactId>codeflowx.govern.nocode.dtos</artifactId>
  </dependency>

  <!-- Entidades -->
  <dependency>
    <groupId>com.codeflowx.govern</groupId>
    <artifactId>nocode.service.entitys</artifactId>
  </dependency>

  <!-- OpenAPI (WebFlux) -->
  <dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webflux-ui</artifactId>
  </dependency>
</dependencies>
```

### Controller Reactivo

```java
@RestController
@RequestMapping("/api/v1/hitl")
@RequiredArgsConstructor
@Slf4j
public class HitlController {

    private final HitlSupervisionService businessService;

    @GetMapping("/dashboard")
    public Mono<ResponseEntity<HitlDashboardDto>> getDashboard(
            @RequestParam(required = false) Long projectId) {
        return Mono.fromCallable(() -> businessService.getDashboard(projectId))
            .subscribeOn(Schedulers.boundedElastic())
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error retrieving HITL dashboard", error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    @GetMapping("/interventions")
    public Mono<ResponseEntity<List<HitlInterventionDto>>> getInterventions(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type) {
        return Mono.fromCallable(() -> businessService.getInterventions(projectId, status, type))
            .subscribeOn(Schedulers.boundedElastic())
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error retrieving HITL interventions", error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    @PostMapping("/interventions")
    public Mono<ResponseEntity<HitlDecisionDto>> recordDecision(
            @RequestBody HitlDecisionRequest request) {
        return Mono.fromCallable(() -> businessService.recordDecision(request))
            .subscribeOn(Schedulers.boundedElastic())
            .map(ResponseEntity::ok)
            .onErrorResume(error -> {
                log.error("Error recording HITL decision", error);
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
    }

    // Más endpoints...
}
```

---

## 📊 ENTIDADES JPA

### Tablas Requeridas

1. **GOVHITLSUPERVISIONS** (prefijo `GOV`)
   - `IDXHITLSUPERVISION` (Long, PK) - ID autonumérico
   - `IDXPROJECT` (Long, FK) - Referencia a proyecto
   - `HITLSUPERVISIONTYPE` (String) - Tipo: PRE_DEPLOYMENT, IN_LOOP, POST_DEPLOYMENT, OVERRIDE
   - `HITLCONFIGURATION` (JSONB) - Configuración de supervisión
   - `HITLSLA` (Integer) - SLA en horas
   - `HITLCREATEDAT` (Timestamp) - Fecha creación
   - `IDUUID` (String) - UUID único

2. **GOVHITLDECISIONS** (prefijo `GOV`)
   - `IDXHITLDECISION` (Long, PK) - ID autonumérico
   - `IDXHITLSUPERVISION` (Long, FK) - Referencia a supervisión
   - `IDXENTITY` (Long) - ID de entidad (Agent, Model, Prompt)
   - `HITLENTITYTYPE` (String) - Tipo: AGENT, MODEL, PROMPT
   - `HITLDECISION` (String) - Decisión: APPROVED, REJECTED, MODIFIED
   - `HITLDECISIONREASON` (String) - Razón de la decisión
   - `HITLRESPONSETIME` (Integer) - Tiempo de respuesta en minutos
   - `HITLDECISIONDATE` (Timestamp) - Fecha de decisión
   - `HITLUSERID` (String) - Usuario que tomó la decisión
   - `IDUUID` (String) - UUID único

**Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/`

---

## ✅ CHECKLIST DE INTEGRACIÓN

### Frontend (✅ Completado)

- [x] API routes implementadas con soporte MOCK/PRODUCCIÓN
- [x] Configuración centralizada de endpoints
- [x] Manejo de errores
- [x] Validaciones de request
- [x] Documentación de endpoints

### Backend (⏳ Pendiente)

- [ ] Crear microservicio `codeflowx-governance-hitl-service`
- [ ] Implementar controller reactivo con todos los endpoints
- [ ] Crear/verificar servicios de negocio en `codeflowx.govern.business`
- [ ] Crear/verificar repositorios en `codeflowx.govern.repository`
- [ ] Verificar entidades JPA (GOVHITLSUPERVISIONS, GOVHITLDECISIONS)
- [ ] Crear/verificar DTOs en `codeflowx.govern.nocode.dtos`
- [ ] Configurar `application.yml` con puerto y configuración
- [ ] Agregar módulo al `pom.xml` padre
- [ ] Documentar endpoints con Swagger/OpenAPI
- [ ] Probar integración end-to-end

---

## 🔄 ACTIVACIÓN DE BACKEND

Para activar el backend real, solo hay que cambiar una variable:

```bash
# .env.local
NEXT_PUBLIC_USE_MOCK=false
NEXT_PUBLIC_BFF_URL=http://localhost:8090
```

**No se requiere modificar código en las pantallas** - el sistema automáticamente usará el backend real a través del BFF.

### Pasos de Verificación

1. **Verificar BFF:** Asegurarse de que el BFF esté corriendo y enrutando correctamente
2. **Verificar Microservicio:** Asegurarse de que el microservicio esté corriendo en el puerto configurado (8091)
3. **Probar Endpoints:** Usar Swagger UI del microservicio para verificar que los endpoints respondan correctamente

---

## 📚 REFERENCIAS

- **Arquitectura Frontend:** `docs/ARQUITECTURA_FRONTEND.md`
- **Documentación Microservicio:** `docs/prompts/compliance/BACKEND_HITL_SERVICE.md`
- **Plantilla Microservicio:** `nocode.service/codeflowx-governance-classification-service/`
- **Prompt Implementación:** `docs/prompts/compliance/PROMPT_COMPLIANCE_HITL.md`
- **Entidades JPA:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/`

---

**Última actualización:** Diciembre 2025
**Estado:** Frontend listo - Backend documentado y listo para implementación
