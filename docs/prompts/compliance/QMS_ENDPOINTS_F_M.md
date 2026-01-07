# 📋 Endpoints REST QMS - Módulos F-M

**Fecha:** Diciembre 2025
**Estado:** ✅ Implementado al 100%

---

## 🎯 RESUMEN

Se han implementado **8 nuevos endpoints REST** para los módulos F-M del QMS, completando la cobertura de los 13 módulos mandatorios según Art. 17 EU AI Act.

---

## 📡 ENDPOINTS IMPLEMENTADOS

### **MÓDULO F: GESTIÓN DE DATOS**

#### `GET /api/v1/qms/data-management?projectId={projectId}`
**Descripción:** Obtiene la gestión de datos QMS para un proyecto.

**Parámetros:**
- `projectId` (query, requerido): ID del proyecto

**Respuesta:**
```json
{
  "projectId": 1,
  "dataGovernanceDefined": true,
  "dataQualityScore": 0.85,
  "datasets": ["dataset1", "dataset2"]
}
```

**Funcionamiento:**
1. Llama al servicio de negocio `getDataManagement(projectId)`
2. Busca en BD la entidad `QmsDataManagement` para el proyecto
3. Si no existe, crea una nueva instancia con valores por defecto
4. Convierte la entidad JPA a DTO y retorna

---

### **MÓDULO G: GESTIÓN DE RIESGOS**

#### `GET /api/v1/qms/risk-management?projectId={projectId}`
**Descripción:** Obtiene el sistema de gestión de riesgos QMS.

**Parámetros:**
- `projectId` (query, requerido): ID del proyecto

**Respuesta:**
```json
{
  "projectId": 1,
  "riskAssessmentComplete": true,
  "identifiedRisks": 5,
  "mitigatedRisks": 3,
  "overallRiskScore": 0.60
}
```

**Funcionamiento:**
1. Consulta la tabla `GOVQMSRISKREGISTER` para obtener riesgos del proyecto
2. Calcula estadísticas: total de riesgos, riesgos mitigados
3. Calcula score: `mitigatedRisks / totalRisks`
4. Retorna objeto `RiskManagementSystem` con las métricas

#### `GET /api/v1/qms/risks?projectId={projectId}`
**Descripción:** Lista todos los riesgos registrados para un proyecto.

**Parámetros:**
- `projectId` (query, requerido): ID del proyecto

**Respuesta:**
```json
[
  {
    "id": 1,
    "projectId": 1,
    "riskDescription": "Riesgo de sesgo en datos de entrenamiento",
    "riskSeverity": "HIGH",
    "riskLikelihood": "MEDIUM",
    "riskMitigated": false,
    "mitigationMeasures": null,
    "createdAt": "2025-12-01T10:00:00",
    "updatedAt": "2025-12-01T10:00:00"
  }
]
```

#### `POST /api/v1/qms/risks`
**Descripción:** Registra un nuevo riesgo en el sistema.

**Body:**
```json
{
  "projectId": 1,
  "riskDescription": "Descripción del riesgo",
  "riskSeverity": "HIGH",
  "riskLikelihood": "MEDIUM"
}
```

**Funcionamiento:**
1. Crea nueva entidad `QmsRiskRegister`
2. Guarda en tabla `GOVQMSRISKREGISTER`
3. Retorna 200 OK

---

### **MÓDULO H: VIGILANCIA POSCOMERCIALIZACIÓN**

#### `GET /api/v1/qms/post-market-monitoring?projectId={projectId}`
**Descripción:** Obtiene el sistema de vigilancia poscomercialización.

**Parámetros:**
- `projectId` (query, requerido): ID del proyecto

**Respuesta:**
```json
{
  "projectId": 1,
  "monitoringActive": true,
  "monitoringPlanDefined": true,
  "incidentsDetected": 2
}
```

**Funcionamiento:**
1. Consulta tabla `GOVPOSTMARKETMONITORING` para obtener registros de monitoreo
2. Consulta tabla `GOVQMSSERIOUSINCIDENTS` para contar incidentes
3. Determina si el monitoreo está activo (tiene registros)
4. Retorna objeto `PostMarketMonitoring` con métricas

---

### **MÓDULO I: INCIDENTES GRAVES (Art. 73 EU AI Act)**

#### `GET /api/v1/qms/serious-incidents?projectId={projectId}`
**Descripción:** Lista incidentes graves del proyecto. **CRÍTICO:** Deben notificarse a autoridades en 15 días.

**Parámetros:**
- `projectId` (query, requerido): ID del proyecto

**Respuesta:**
```json
[
  {
    "id": 1,
    "projectId": 1,
    "incidentType": "Discrimination",
    "incidentDescription": "El sistema mostró sesgo discriminatorio",
    "incidentSeverity": "CRITICAL",
    "incidentDate": "2025-12-01T10:00:00",
    "authorityNotified": false,
    "authorityNotifiedAt": null,
    "createdAt": "2025-12-01T10:00:00",
    "updatedAt": "2025-12-01T10:00:00"
  }
]
```

**Funcionamiento:**
1. Consulta tabla `GOVQMSSERIOUSINCIDENTS` filtrada por `projectId`
2. Ordena por fecha descendente
3. Convierte entidades JPA a DTOs
4. Retorna lista de incidentes

#### `POST /api/v1/qms/serious-incidents`
**Descripción:** Registra un nuevo incidente grave. **CRÍTICO:** Debe notificar autoridad en 15 días según Art. 73.

**Body:**
```json
{
  "projectId": 1,
  "incidentType": "Discrimination",
  "incidentDescription": "Descripción detallada del incidente",
  "incidentSeverity": "CRITICAL"
}
```

**Funcionamiento:**
1. Crea nueva entidad `QmsSeriousIncident`
2. Guarda en tabla `GOVQMSSERIOUSINCIDENTS`
3. **IMPORTANTE:** El sistema debe programar notificación a autoridad dentro de 15 días
4. Retorna el DTO creado

---

### **MÓDULO J: COMUNICACIONES CON AUTORIDADES**

#### `GET /api/v1/qms/authority-communications?projectId={projectId}`
**Descripción:** Lista comunicaciones con autoridades.

**Parámetros:**
- `projectId` (query, requerido): ID del proyecto

**Respuesta:**
```json
[
  {
    "id": 1,
    "projectId": 1,
    "communicationType": "Notification",
    "communicationContent": "Notificación de incidente grave",
    "authorityName": "EU AI Office",
    "communicationDate": "2025-12-01T10:00:00",
    "createdAt": "2025-12-01T10:00:00",
    "updatedAt": "2025-12-01T10:00:00"
  }
]
```

#### `POST /api/v1/qms/authority-communications`
**Descripción:** Registra una nueva comunicación con una autoridad.

**Body:**
```json
{
  "projectId": 1,
  "communicationType": "Notification",
  "communicationContent": "Contenido de la comunicación",
  "authorityName": "EU AI Office"
}
```

**Funcionamiento:**
1. Crea nueva entidad `QmsAuthorityCommunication`
2. Guarda en tabla `GOVQMSAUTHORITYCOMMUNICATIONS`
3. Retorna el DTO creado

---

### **MÓDULO K: REGISTRO DE DOCUMENTACIÓN**

#### `GET /api/v1/qms/documentation-registry?projectId={projectId}`
**Descripción:** Obtiene el registro completo de documentación técnica.

**Parámetros:**
- `projectId` (query, requerido): ID del proyecto

**Respuesta:**
```json
{
  "projectId": 1,
  "totalDocuments": 10,
  "completeDocuments": 8,
  "documentationScore": 0.80,
  "documents": [
    "Technical Documentation - v1.0",
    "Risk Assessment - v2.0"
  ]
}
```

**Funcionamiento:**
1. Consulta tabla `GOVQMSTECHNICALDOCUMENTS` para el proyecto
2. Cuenta total de documentos y documentos completos
3. Calcula score: `completeDocuments / totalDocuments`
4. Retorna objeto `DocumentationRegistry` con métricas

#### `GET /api/v1/qms/technical-documents?projectId={projectId}`
**Descripción:** Lista todos los documentos técnicos del proyecto.

**Parámetros:**
- `projectId` (query, requerido): ID del proyecto

**Respuesta:**
```json
[
  {
    "id": 1,
    "projectId": 1,
    "documentType": "Technical Documentation",
    "documentUrl": "https://example.com/doc.pdf",
    "documentVersion": "v1.0",
    "documentComplete": true,
    "createdAt": "2025-12-01T10:00:00",
    "updatedAt": "2025-12-01T10:00:00"
  }
]
```

#### `POST /api/v1/qms/technical-documents`
**Descripción:** Registra un nuevo documento técnico.

**Body:**
```json
{
  "projectId": 1,
  "documentType": "Technical Documentation",
  "documentUrl": "https://example.com/doc.pdf",
  "documentVersion": "v1.0"
}
```

**Tipos de documentos soportados:**
- Technical Documentation
- Risk Assessment
- FRIA (Fundamental Rights Impact Assessment)
- Conformity Declaration
- Otros documentos técnicos

---

### **MÓDULO L: GESTIÓN DE RECURSOS**

#### `GET /api/v1/qms/resource-management?projectId={projectId}`
**Descripción:** Obtiene la gestión de recursos QMS.

**Parámetros:**
- `projectId` (query, requerido): ID del proyecto

**Respuesta:**
```json
{
  "id": 1,
  "projectId": 1,
  "humanResourcesAllocated": 5,
  "technicalResourcesAllocated": 3,
  "resourceAdequacyScore": 0.80
}
```

**Funcionamiento:**
1. Consulta tabla `GOVQMSRESOURCEMANAGEMENT` para el proyecto
2. Si no existe, retorna valores por defecto (0 recursos, score 0)
3. Retorna DTO con recursos asignados y score de adecuación

#### `POST /api/v1/qms/resource-management`
**Descripción:** Actualiza la asignación de recursos del proyecto.

**Body:**
```json
{
  "projectId": 1,
  "humanResourcesAllocated": 5,
  "technicalResourcesAllocated": 3,
  "resourceAdequacyScore": null
}
```

**Funcionamiento:**
1. Busca o crea entidad `QmsResourceManagement`
2. Actualiza recursos humanos y técnicos
3. Calcula score de adecuación automáticamente:
   - Si hay recursos: score base 0.50
   - Si >= 2 humanos y >= 1 técnico: score 0.80
   - Si >= 5 humanos y >= 3 técnicos: score 1.00
4. Guarda en tabla `GOVQMSRESOURCEMANAGEMENT`
5. Retorna DTO actualizado

---

### **MÓDULO M: MARCO DE RENDICIÓN DE CUENTAS**

#### `GET /api/v1/qms/accountability-framework?projectId={projectId}`
**Descripción:** Obtiene el marco de accountability QMS.

**Parámetros:**
- `projectId` (query, requerido): ID del proyecto

**Respuesta:**
```json
{
  "projectId": 1,
  "frameworkDefined": true,
  "responsiblePersons": [
    "Juan Pérez (Data Governance Lead)",
    "María García (Model Development Lead)"
  ],
  "accountabilityScore": 0.80
}
```

**Funcionamiento:**
1. Consulta tabla `GOVQMSACCOUNTABILITYASSIGNMENTS` para el proyecto
2. Si hay asignaciones, `frameworkDefined = true`
3. Calcula score: `min(assignments.size(), 5) / 5` (máximo 1.0)
4. Retorna objeto `AccountabilityFramework` con métricas

#### `GET /api/v1/qms/accountability-assignments?projectId={projectId}`
**Descripción:** Lista todas las asignaciones de responsabilidades.

**Parámetros:**
- `projectId` (query, requerido): ID del proyecto

**Respuesta:**
```json
[
  {
    "id": 1,
    "projectId": 1,
    "assignedRole": "Data Governance Lead",
    "assignedUser": "Juan Pérez",
    "responsibilities": "Responsible for: Data Governance",
    "createdAt": "2025-12-01T10:00:00",
    "updatedAt": "2025-12-01T10:00:00"
  }
]
```

#### `POST /api/v1/qms/accountability-assignments`
**Descripción:** Asigna una responsabilidad a una persona.

**Body:**
```json
{
  "projectId": 1,
  "assignedRole": "Data Governance Lead",
  "assignedUser": "Juan Pérez",
  "responsibilities": "Responsible for: Data Governance"
}
```

**Áreas típicas:**
- Data Governance
- Model Development
- Testing
- Deployment
- Monitoring

**Funcionamiento:**
1. Crea nueva entidad `QmsAccountabilityAssignment`
2. Guarda en tabla `GOVQMSACCOUNTABILITYASSIGNMENTS`
3. Retorna el DTO creado

---

## 🔄 FLUJO DE DATOS

```
Frontend → Controller → Business Service → Repository → JPA Entity → Database
                ↓
              DTO ← Domain Object ← JPA Entity
```

1. **Controller:** Recibe request, valida, convierte DTO → Domain Object
2. **Business Service:** Lógica de negocio, cálculos, validaciones
3. **Repository:** Consultas JPA a la base de datos
4. **JPA Entity:** Mapeo objeto-relacional
5. **Database:** Tablas PostgreSQL con prefijo `GOVQMS*`

---

## 📊 TABLAS DE BASE DE DATOS

| Módulo | Tabla | Entidad JPA |
|--------|-------|-------------|
| F | `GOVQMSDATAMANAGEMENT` | `QmsDataManagement` |
| G | `GOVQMSRISKREGISTER` | `QmsRiskRegister` |
| H | `GOVPOSTMARKETMONITORING` | `PostMarketMonitoring` (existente) |
| I | `GOVQMSSERIOUSINCIDENTS` | `QmsSeriousIncident` |
| J | `GOVQMSAUTHORITYCOMMUNICATIONS` | `QmsAuthorityCommunication` |
| K | `GOVQMSTECHNICALDOCUMENTS` | `QmsTechnicalDocument` |
| L | `GOVQMSRESOURCEMANAGEMENT` | `QmsResourceManagement` |
| M | `GOVQMSACCOUNTABILITYASSIGNMENTS` | `QmsAccountabilityAssignment` |

---

## ✅ VALIDACIONES Y REGLAS DE NEGOCIO

### **Módulo I (Serious Incidents):**
- ⚠️ **CRÍTICO:** Los incidentes graves deben notificarse a autoridades en **15 días** (Art. 73 EU AI Act)
- El sistema debe programar notificación automática al registrar un incidente

### **Módulo G (Risk Management):**
- El score se calcula como: `riesgos mitigados / total de riesgos`
- Si no hay riesgos, score = 0

### **Módulo L (Resource Management):**
- Score de adecuación se calcula automáticamente según recursos asignados
- Mínimo: 0.50 si hay recursos
- Óptimo: 1.00 con >= 5 humanos y >= 3 técnicos

### **Módulo M (Accountability):**
- Score máximo: 1.0 (con 5 o más asignaciones)
- Score = `min(assignments.size(), 5) / 5`

---

## 🚀 USO DESDE EL FRONTEND

### Ejemplo: Obtener gestión de datos
```typescript
const response = await fetch(`/api/governance/compliance/qms/data-management?projectId=${projectId}`);
const data = await response.json();
```

### Ejemplo: Registrar un riesgo
```typescript
const response = await fetch(`/api/governance/compliance/qms/risks`, {
  method: "POST",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify({
    projectId: 1,
    riskDescription: "Riesgo de sesgo",
    riskSeverity: "HIGH",
    riskLikelihood: "MEDIUM"
  })
});
```

---

## 📝 NOTAS IMPORTANTES

1. **Todos los endpoints son reactivos** (retornan `Mono<ResponseEntity<T>>`)
2. **Validación automática** con `@Valid` en request bodies
3. **Manejo de errores centralizado** con `onErrorResume`
4. **Conversión automática** de entidades JPA a DTOs en el controller
5. **Documentación Swagger** incluida con `@Operation` y `@ApiResponses`

---

## ✅ ESTADO DE IMPLEMENTACIÓN

- ✅ **Módulo F:** Gestión de Datos - 100% implementado
- ✅ **Módulo G:** Gestión de Riesgos - 100% implementado
- ✅ **Módulo H:** Post-Market Monitoring - 100% implementado
- ✅ **Módulo I:** Incidentes Graves - 100% implementado
- ✅ **Módulo J:** Comunicaciones Autoridades - 100% implementado
- ✅ **Módulo K:** Documentación Técnica - 100% implementado
- ✅ **Módulo L:** Gestión de Recursos - 100% implementado
- ✅ **Módulo M:** Accountability Framework - 100% implementado

**TOTAL: 8 endpoints REST implementados para módulos F-M**
