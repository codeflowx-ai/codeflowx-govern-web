# 🔗 INTEGRACIÓN BACKEND - EVALUACIÓN DE CONFORMIDAD

**Módulo:** Compliance - Conformity Assessment
**Artículo EU AI Act:** Art. 43 - Evaluación de Conformidad + Anexo VI
**Fecha:** Diciembre 2025

---

## 📋 RESUMEN DE INTEGRACIÓN

Este documento describe cómo integrar el frontend de Evaluación de Conformidad con el backend Java existente.

---

## 🏗️ ARQUITECTURA BACKEND

### **Entidad JPA: ComplianceAssessment**

**Tabla:** `COMCOMPLIANCEASSESSMENTS` (prefijo `COM`)

**Ubicación Backend:**
```
nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/ComplianceAssessment.java
```

**Campos Principales:**
- `IDXCOMPLIANCEASSESSMENT` (Long, PK) - ID autonumérico
- `IDXPROJECT` (Long, FK) - Referencia a proyecto (tabla `prjprojects`)
- `COMASSESSMENTTYPE` (String) - Tipo de evaluación
- `COMASSESSMENTDATE` (Timestamp) - Fecha de evaluación
- `COMSTEP1SCORE` (BigDecimal) - Score step 1 (0.00 - 1.00)
- `COMSTEP2QMSSCORE` (BigDecimal) - Score step 2 QMS (0.00 - 1.00)
- `COMSTEP3DOCSCORE` (BigDecimal) - Score step 3 Documentación (0.00 - 1.00)
- `COMSTEP4CONSISTENCYSCORE` (BigDecimal) - Score step 4 Consistencia (0.00 - 1.00)
- `COMOVERALLSCORE` (BigDecimal) - Score overall (promedio ponderado)
- `COMREADYFORCERTIFICATION` (Boolean) - Listo para certificación (overallScore >= 0.80)
- `COMANNEXVICOMPLIANT` (Boolean) - Cumple Anexo VI
- `COMCERTIFICATEID` (String) - ID de certificado (si existe)
- `COMCREATEDAT` (Timestamp) - Fecha creación
- `COMCREATEDBY` (String) - Usuario creador
- `COMUPDATEDAT` (Timestamp) - Fecha actualización
- `COMUPDATEDBY` (String) - Usuario actualizador
- `IDUUID` (String) - UUID único

---

## 🔌 SERVICIOS BACKEND DISPONIBLES

### **1. ComplianceAssessmentBusinessService**

**Ubicación:**
```
codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/ComplianceAssessmentBusinessService.java
```

**Métodos Disponibles:**

#### **createAssessment**
```java
ComplianceAssessment createAssessment(Long projectId, String assessmentType, String executedBy)
```

**Lógica:**
1. Valida que el proyecto sea de alto riesgo: `SELECT * FROM prjprojects WHERE idxproject = ? AND prjishighrisk = true`
2. Verifica que tenga FRIA completada
3. Verifica que tenga clasificación Anexo III
4. Crea registro en `comcomplianceassessments`
5. Ejecuta Step 1 automáticamente (verificación inicial)

**Request:**
```json
POST /api/governance/compliance/assessments
{
  "projectId": 1,
  "assessmentType": "FULL",
  "executedBy": "user@example.com"
}
```

**Response:**
```json
{
  "id": 1,
  "projectId": 1,
  "assessmentType": "FULL",
  "status": "IN_PROGRESS",
  "step1Score": 1.0,
  "createdAt": "2025-12-01T10:00:00Z",
  "createdBy": "user@example.com"
}
```

---

#### **executeStep2QmsCheck**
```java
void executeStep2QmsCheck(Long assessmentId, String executedBy)
```

**Lógica:**
1. Obtiene la evaluación por ID
2. Obtiene el proyecto asociado
3. Llama a `qualityManagementSystemBusinessService.calculateQmsComplianceScore(projectId)`
4. Actualiza `COMSTEP2QMSSCORE`
5. Actualiza `COMUPDATEDBY` y `COMUPDATEDAT`

**Request:**
```json
POST /api/governance/compliance/assessments/{id}/execute-step2
{
  "executedBy": "user@example.com"
}
```

**Response:**
```json
{
  "id": 1,
  "step2QmsScore": 0.85,
  "updatedAt": "2025-12-01T10:15:00Z",
  "updatedBy": "user@example.com"
}
```

---

#### **executeStep3DocReview**
```java
void executeStep3DocReview(Long assessmentId, String executedBy)
```

**Lógica:**
1. Obtiene la evaluación por ID
2. Obtiene el proyecto asociado
3. Llama a `technicalDocumentationBusinessService.calculateDocumentationScore(projectId)`
4. Actualiza `COMSTEP3DOCSCORE`
5. Actualiza `COMUPDATEDBY` y `COMUPDATEDAT`

**Request:**
```json
POST /api/governance/compliance/assessments/{id}/execute-step3
{
  "executedBy": "user@example.com"
}
```

**Response:**
```json
{
  "id": 1,
  "step3DocScore": 0.80,
  "updatedAt": "2025-12-01T10:30:00Z",
  "updatedBy": "user@example.com"
}
```

---

#### **calculateOverallScore**
```java
BigDecimal calculateOverallScore(Long assessmentId)
```

**Lógica:**
1. Obtiene la evaluación por ID
2. Calcula: `overallScore = (step2QmsScore + step3DocScore + step4ConsistencyScore) / 3`
3. Determina: `readyForCertification = (overallScore >= 0.80)`
4. Actualiza `COMOVERALLSCORE` y `COMREADYFORCERTIFICATION`
5. Actualiza `COMANNEXVICOMPLIANT`

**Request:**
```json
POST /api/governance/compliance/assessments/{id}/calculate-overall
```

**Response:**
```json
{
  "id": 1,
  "overallScore": 0.85,
  "readyForCertification": true,
  "annexViCompliant": true,
  "updatedAt": "2025-12-01T10:45:00Z"
}
```

---

### **2. ComplianceAssessmentService (CRUD)**

**Ubicación:**
```
codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/compliance/ComplianceAssessmentService.java
```

**Métodos CRUD:**
- `List<ComplianceAssessment> findAll()` - Obtiene todas las evaluaciones
- `ComplianceAssessment findById(Long id)` - Obtiene evaluación por ID
- `ComplianceAssessment save(ComplianceAssessment assessment)` - Guarda/actualiza evaluación
- `void delete(Long id)` - Elimina evaluación

**Endpoints CRUD:**

```typescript
GET    /api/governance/compliance/assessments           // findAll()
GET    /api/governance/compliance/assessments/{id}      // findById()
PUT    /api/governance/compliance/assessments/{id}      // save()
DELETE /api/governance/compliance/assessments/{id}      // delete()
```

---

### **3. ComplianceDashboardService**

**Ubicación:**
```
codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/ComplianceDashboardService.java
```

**Métodos:**

#### **getConformityMetrics**
```java
ConformityMetricsDTO getConformityMetrics()
```

**Lógica:**
```sql
SELECT
  COUNT(*) as total,
  SUM(CASE WHEN COMOVERALLSCORE IS NOT NULL THEN 1 ELSE 0 END) as completed,
  SUM(CASE WHEN COMREADYFORCERTIFICATION = true THEN 1 ELSE 0 END) as readyForCertification,
  AVG(COMOVERALLSCORE) as averageScore
FROM comcomplianceassessments
```

**Request:**
```json
GET /api/governance/compliance/dashboard/metrics
```

**Response:**
```json
{
  "total": 25,
  "completed": 18,
  "readyForCertification": 12,
  "averageScore": 0.82,
  "inProgress": 5,
  "pending": 2
}
```

---

## 🔄 SERVICIOS FRONTEND

### **1. Crear Servicio TypeScript: `conformityAssessmentService.ts`**

**Ubicación:** `app/(app)/governance/services/conformityAssessmentService.ts`

```typescript
import { governanceService } from './governanceService';

export interface ConformityAssessment {
  id: number;
  projectId: number;
  projectName?: string;
  assessmentDate: string;
  assessmentType: string;
  step1Score: number | null;
  step2QmsScore: number | null;
  step3DocScore: number | null;
  step4ConsistencyScore: number | null;
  overallScore: number | null;
  readyForCertification: boolean;
  annexViCompliant: boolean;
  certificateId: string | null;
  status: "PENDING" | "IN_PROGRESS" | "COMPLETED" | "REJECTED";
  createdBy: string;
  createdAt: string;
  updatedBy?: string;
  updatedAt?: string;
}

export interface ConformityMetrics {
  total: number;
  completed: number;
  readyForCertification: number;
  averageScore: number;
  inProgress: number;
  pending: number;
}

class ConformityAssessmentService {
  private baseUrl = '/api/governance/compliance';

  async getAll(): Promise<ConformityAssessment[]> {
    const response = await fetch(`${this.baseUrl}/assessments`);
    if (!response.ok) throw new Error('Failed to fetch assessments');
    return response.json();
  }

  async getById(id: number): Promise<ConformityAssessment> {
    const response = await fetch(`${this.baseUrl}/assessments/${id}`);
    if (!response.ok) throw new Error('Failed to fetch assessment');
    return response.json();
  }

  async create(projectId: number, assessmentType: string, executedBy: string): Promise<ConformityAssessment> {
    const response = await fetch(`${this.baseUrl}/assessments`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ projectId, assessmentType, executedBy }),
    });
    if (!response.ok) throw new Error('Failed to create assessment');
    return response.json();
  }

  async executeStep2(assessmentId: number, executedBy: string): Promise<ConformityAssessment> {
    const response = await fetch(`${this.baseUrl}/assessments/${assessmentId}/execute-step2`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ executedBy }),
    });
    if (!response.ok) throw new Error('Failed to execute step 2');
    return response.json();
  }

  async executeStep3(assessmentId: number, executedBy: string): Promise<ConformityAssessment> {
    const response = await fetch(`${this.baseUrl}/assessments/${assessmentId}/execute-step3`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ executedBy }),
    });
    if (!response.ok) throw new Error('Failed to execute step 3');
    return response.json();
  }

  async calculateOverallScore(assessmentId: number): Promise<ConformityAssessment> {
    const response = await fetch(`${this.baseUrl}/assessments/${assessmentId}/calculate-overall`, {
      method: 'POST',
    });
    if (!response.ok) throw new Error('Failed to calculate overall score');
    return response.json();
  }

  async getMetrics(): Promise<ConformityMetrics> {
    const response = await fetch(`${this.baseUrl}/dashboard/metrics`);
    if (!response.ok) throw new Error('Failed to fetch metrics');
    return response.json();
  }
}

export const conformityAssessmentService = new ConformityAssessmentService();
```

---

## 🛣️ API ROUTES NEXT.JS (Opcional - Mock)

Si el backend no está disponible aún, crear API routes mock en Next.js:

**Ubicación:** `app/api/governance/compliance/assessments/route.ts`

```typescript
import { NextRequest, NextResponse } from 'next/server';
import { mockConformityAssessments, mockConformityMetrics } from '@/app/(app)/governance/data/mockConformityAssessment';

export async function GET(request: NextRequest) {
  // TODO: Reemplazar con llamada real al backend
  return NextResponse.json(mockConformityAssessments);
}

export async function POST(request: NextRequest) {
  // TODO: Reemplazar con llamada real al backend
  const body = await request.json();
  const newAssessment = {
    id: mockConformityAssessments.length + 1,
    ...body,
    status: 'IN_PROGRESS',
    createdAt: new Date().toISOString(),
  };
  return NextResponse.json(newAssessment);
}
```

---

## 📊 MAPEO DE CAMPOS FRONTEND ↔️ BACKEND

| Frontend (TypeScript) | Backend (Java/DB) | Tipo |
|----------------------|-------------------|------|
| `id` | `IDXCOMPLIANCEASSESSMENT` | Long |
| `projectId` | `IDXPROJECT` | Long |
| `projectName` | (Join con `prjprojects`) | String |
| `assessmentDate` | `COMASSESSMENTDATE` | Timestamp → ISO String |
| `assessmentType` | `COMASSESSMENTTYPE` | String |
| `step1Score` | `COMSTEP1SCORE` | BigDecimal → number (0-1) |
| `step2QmsScore` | `COMSTEP2QMSSCORE` | BigDecimal → number (0-1) |
| `step3DocScore` | `COMSTEP3DOCSCORE` | BigDecimal → number (0-1) |
| `step4ConsistencyScore` | `COMSTEP4CONSISTENCYSCORE` | BigDecimal → number (0-1) |
| `overallScore` | `COMOVERALLSCORE` | BigDecimal → number (0-1) |
| `readyForCertification` | `COMREADYFORCERTIFICATION` | Boolean |
| `annexViCompliant` | `COMANNEXVICOMPLIANT` | Boolean |
| `certificateId` | `COMCERTIFICATEID` | String |
| `status` | Calculado desde scores | String enum |
| `createdBy` | `COMCREATEDBY` | String |
| `createdAt` | `COMCREATEDAT` | Timestamp → ISO String |
| `updatedBy` | `COMUPDATEDBY` | String |
| `updatedAt` | `COMUPDATEDAT` | Timestamp → ISO String |

---

## 🔐 AUTENTICACIÓN Y AUTORIZACIÓN

**Todas las peticiones deben incluir:**

1. **Header de autenticación:**
```typescript
headers: {
  'Authorization': `Bearer ${token}`,
  'Content-Type': 'application/json'
}
```

2. **Usuario ejecutor:**
   - Todos los métodos que modifican datos requieren `executedBy` (email del usuario actual)
   - Se obtiene del token de autenticación en el backend

---

## 🚀 PASOS DE INTEGRACIÓN

### **Fase 1: Preparación**
1. ✅ Crear servicio TypeScript `conformityAssessmentService.ts`
2. ✅ Crear interfaces TypeScript que coincidan con la entidad Java
3. ⏳ Verificar que el backend exponga los endpoints REST necesarios

### **Fase 2: Integración Básica**
1. Reemplazar llamadas a mock data con llamadas al servicio
2. Mapear respuestas del backend al formato del frontend
3. Manejar errores y estados de carga

### **Fase 3: Funcionalidades Avanzadas**
1. Implementar ejecución de steps 2, 3, 4 desde el frontend
2. Implementar cálculo de overall score
3. Integrar con generación de declaraciones de conformidad

### **Fase 4: Optimización**
1. Añadir caché para métricas del dashboard
2. Implementar actualización en tiempo real (WebSockets opcional)
3. Añadir validaciones en frontend antes de llamar al backend

---

## 🧪 TESTING

**Endpoints a probar:**
- ✅ GET `/api/governance/compliance/assessments` - Lista todas las evaluaciones
- ✅ GET `/api/governance/compliance/assessments/{id}` - Obtiene evaluación por ID
- ✅ POST `/api/governance/compliance/assessments` - Crea nueva evaluación
- ✅ POST `/api/governance/compliance/assessments/{id}/execute-step2` - Ejecuta Step 2
- ✅ POST `/api/governance/compliance/assessments/{id}/execute-step3` - Ejecuta Step 3
- ✅ POST `/api/governance/compliance/assessments/{id}/calculate-overall` - Calcula score overall
- ✅ GET `/api/governance/compliance/dashboard/metrics` - Obtiene métricas del dashboard

---

## 📝 NOTAS IMPORTANTES

1. **Formato de Scores:** Los scores en el backend son `BigDecimal` (0.00 - 1.00), en el frontend se muestran como porcentajes (0-100%)

2. **Estado (Status):** El estado se calcula automáticamente:
   - `PENDING`: Solo tiene step1Score
   - `IN_PROGRESS`: Tiene step2 o step3 pero no overallScore
   - `COMPLETED`: Tiene overallScore
   - `REJECTED`: Marcado manualmente

3. **Cálculo de Overall Score:**
   ```typescript
   overallScore = (step2QmsScore + step3DocScore + step4ConsistencyScore) / 3
   readyForCertification = overallScore >= 0.80
   ```

4. **Validaciones Backend:**
   - Solo proyectos de alto riesgo pueden tener evaluaciones
   - Step 2 requiere que Step 1 esté completado
   - Step 3 requiere que Step 2 esté completado
   - Overall score requiere Steps 2, 3 y 4 completados

---

**Última actualización:** Diciembre 2025
**Estado:** Listo para integración



