# 📊 PROJECTS - API & SDK

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación de API y SDK del módulo Projects

---

## 🎯 RESUMEN EJECUTIVO

El módulo **Projects** proporciona APIs RESTful completas para:
- **Projects:** Gestión de proyectos
- **Team:** Gestión de equipo
- **Tasks:** Gestión de tareas
- **Financial:** Gestión financiera
- **Resources:** Gestión de recursos

**Total de Endpoints:** ~60  
**Formato:** REST JSON  
**Autenticación:** JWT Bearer Token

---

## 🌐 ENDPOINTS PRINCIPALES

### **1. PROJECTS API**

#### **GET /api/projects**
Obtener listado de proyectos

**Request:**
```bash
GET /api/projects?page=0&size=20&sort=startDate,desc&status=ACTIVE
Authorization: Bearer {token}
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "AI Platform Modernization",
      "projectCode": "AIPM-2025-001",
      "projectType": "CLIENT",
      "status": "ACTIVE",
      "priority": "HIGH",
      "ownerId": 101,
      "ownerName": "John Doe",
      "clientId": 501,
      "clientName": "Acme Corp",
      "startDate": "2025-01-15",
      "endDate": "2025-12-31",
      "budget": 500000.00,
      "teamSize": 12,
      "totalTasks": 156,
      "completedTasks": 89,
      "completionPercentage": 57.05,
      "totalHoursWorked": 2450.5
    }
  ],
  "totalElements": 45,
  "totalPages": 3
}
```

#### **POST /api/projects**
Crear nuevo proyecto

**Request:**
```bash
POST /api/projects
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "AI Platform Modernization",
  "projectCode": "AIPM-2025-001",
  "description": "Complete modernization of AI platform with latest technologies",
  "projectType": "CLIENT",
  "priority": "HIGH",
  "ownerId": 101,
  "clientId": 501,
  "departmentId": 10,
  "startDate": "2025-01-15",
  "endDate": "2025-12-31",
  "budget": 500000.00
}
```

**Response:**
```json
{
  "id": 1,
  "name": "AI Platform Modernization",
  "projectCode": "AIPM-2025-001",
  "status": "PLANNING",
  "createdAt": "2025-10-29T10:30:00Z",
  "message": "Project created successfully"
}
```

#### **PUT /api/projects/{id}**
Actualizar proyecto existente

**Request:**
```bash
PUT /api/projects/1
Authorization: Bearer {token}
Content-Type: application/json

{
  "status": "ACTIVE",
  "budget": 550000.00,
  "endDate": "2026-01-31"
}
```

#### **DELETE /api/projects/{id}**
Eliminar proyecto (soft delete)

**Request:**
```bash
DELETE /api/projects/1
Authorization: Bearer {token}
```

---

### **2. TEAM API**

#### **GET /api/projects/{id}/members**
Obtener miembros del equipo

**Request:**
```bash
GET /api/projects/1/members
Authorization: Bearer {token}
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "projectId": 1,
      "userId": 101,
      "userName": "John Doe",
      "userEmail": "john.doe@company.com",
      "role": "PROJECT_MANAGER",
      "allocationPercentage": 100.00,
      "hourlyRate": 150.00,
      "startDate": "2025-01-15",
      "isActive": true
    },
    {
      "id": 2,
      "projectId": 1,
      "userId": 102,
      "userName": "Jane Smith",
      "role": "DEVELOPER",
      "allocationPercentage": 80.00,
      "hourlyRate": 120.00,
      "startDate": "2025-01-20",
      "isActive": true
    }
  ],
  "totalElements": 12
}
```

#### **POST /api/projects/{id}/members**
Agregar miembro al equipo

**Request:**
```bash
POST /api/projects/1/members
Authorization: Bearer {token}
Content-Type: application/json

{
  "userId": 103,
  "role": "DEVELOPER",
  "allocationPercentage": 75.00,
  "hourlyRate": 110.00,
  "startDate": "2025-02-01"
}
```

---

### **3. TASKS API**

#### **GET /api/projects/{id}/tasks**
Obtener tareas del proyecto

**Request:**
```bash
GET /api/projects/1/tasks?status=IN_PROGRESS&assignedTo=102
Authorization: Bearer {token}
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "projectId": 1,
      "taskCode": "AIPM-TASK-001",
      "title": "Design database schema",
      "description": "Design complete database schema for new AI platform",
      "status": "IN_PROGRESS",
      "priority": "HIGH",
      "assignedTo": 102,
      "assignedToName": "Jane Smith",
      "estimatedHours": 40.00,
      "actualHours": 28.50,
      "startDate": "2025-01-20",
      "dueDate": "2025-02-05",
      "progressPercentage": 71.25
    }
  ],
  "totalElements": 156
}
```

#### **POST /api/projects/{id}/tasks**
Crear nueva tarea

**Request:**
```bash
POST /api/projects/1/tasks
Authorization: Bearer {token}
Content-Type: application/json

{
  "taskCode": "AIPM-TASK-002",
  "title": "Implement user authentication",
  "description": "Implement JWT-based authentication with OAuth2",
  "priority": "HIGH",
  "assignedTo": 102,
  "estimatedHours": 32.00,
  "startDate": "2025-02-06",
  "dueDate": "2025-02-20",
  "parentTaskId": null
}
```

#### **PUT /api/projects/{id}/tasks/{taskId}**
Actualizar estado de tarea

**Request:**
```bash
PUT /api/projects/1/tasks/1
Authorization: Bearer {token}
Content-Type: application/json

{
  "status": "DONE",
  "actualHours": 38.00,
  "completedDate": "2025-02-05"
}
```

---

### **4. TIME TRACKING API**

#### **POST /api/projects/{id}/time-tracking**
Registrar tiempo trabajado

**Request:**
```bash
POST /api/projects/1/time-tracking
Authorization: Bearer {token}
Content-Type: application/json

{
  "taskId": 1,
  "userId": 102,
  "workDate": "2025-02-05",
  "hoursWorked": 8.00,
  "description": "Completed database schema design and documentation",
  "isBillable": true
}
```

**Response:**
```json
{
  "id": 1,
  "projectId": 1,
  "taskId": 1,
  "userId": 102,
  "workDate": "2025-02-05",
  "hoursWorked": 8.00,
  "isBillable": true,
  "createdAt": "2025-02-05T18:30:00Z"
}
```

#### **GET /api/projects/{id}/time-tracking/report**
Obtener reporte de tiempo

**Request:**
```bash
GET /api/projects/1/time-tracking/report?startDate=2025-02-01&endDate=2025-02-28&userId=102
Authorization: Bearer {token}
```

**Response:**
```json
{
  "projectId": 1,
  "projectName": "AI Platform Modernization",
  "userId": 102,
  "userName": "Jane Smith",
  "startDate": "2025-02-01",
  "endDate": "2025-02-28",
  "totalHours": 168.50,
  "billableHours": 152.00,
  "nonBillableHours": 16.50,
  "dailyBreakdown": [
    {
      "date": "2025-02-01",
      "hours": 8.00,
      "billable": 8.00
    }
  ]
}
```

---

### **5. FINANCIAL API**

#### **GET /api/projects/{id}/financial/summary**
Obtener resumen financiero

**Request:**
```bash
GET /api/projects/1/financial/summary
Authorization: Bearer {token}
```

**Response:**
```json
{
  "projectId": 1,
  "projectName": "AI Platform Modernization",
  "budget": 500000.00,
  "totalEstimatedCost": 485000.00,
  "totalActualCost": 312500.00,
  "costVariance": -172500.00,
  "costVariancePercentage": -35.57,
  "budgetUtilization": 62.50,
  "estimatedRemaining": 172500.00,
  "projectedOverrun": false,
  "costBreakdown": {
    "LABOR": 250000.00,
    "INFRASTRUCTURE": 45000.00,
    "LICENSE": 15000.00,
    "TRAVEL": 2500.00
  }
}
```

#### **GET /api/projects/{id}/invoices**
Obtener facturas del proyecto

**Request:**
```bash
GET /api/projects/1/invoices?status=SENT
Authorization: Bearer {token}
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "projectId": 1,
      "invoiceNumber": "INV-2025-001",
      "invoiceDate": "2025-02-28",
      "dueDate": "2025-03-15",
      "subtotal": 125000.00,
      "taxAmount": 26250.00,
      "totalAmount": 151250.00,
      "status": "SENT",
      "paymentDate": null
    }
  ],
  "totalElements": 8,
  "totalInvoiced": 312500.00,
  "totalPaid": 151250.00,
  "totalOverdue": 0.00
}
```

#### **POST /api/projects/{id}/invoices**
Crear factura

**Request:**
```bash
POST /api/projects/1/invoices
Authorization: Bearer {token}
Content-Type: application/json

{
  "invoiceDate": "2025-03-31",
  "dueDate": "2025-04-15",
  "items": [
    {
      "description": "Development services - March 2025",
      "quantity": 320.00,
      "unitPrice": 125.00,
      "totalPrice": 40000.00
    },
    {
      "description": "Infrastructure costs - March 2025",
      "quantity": 1.00,
      "unitPrice": 5000.00,
      "totalPrice": 5000.00
    }
  ],
  "taxRate": 21.00,
  "notes": "Payment terms: Net 15 days"
}
```

---

### **6. ROI API**

#### **GET /api/projects/{id}/roi**
Obtener análisis de ROI

**Request:**
```bash
GET /api/projects/1/roi
Authorization: Bearer {token}
```

**Response:**
```json
{
  "projectId": 1,
  "projectName": "AI Platform Modernization",
  "totalInvestment": 485000.00,
  "totalRevenue": 750000.00,
  "netProfit": 265000.00,
  "roiPercentage": 54.64,
  "paybackPeriodMonths": 7.8,
  "roiRating": "EXCELLENT",
  "calculationDate": "2025-10-29"
}
```

#### **POST /api/projects/{id}/roi/calculate**
Calcular ROI del proyecto

**Request:**
```bash
POST /api/projects/1/roi/calculate
Authorization: Bearer {token}
Content-Type: application/json

{
  "totalRevenue": 750000.00
}
```

---

### **7. RESOURCES API**

#### **GET /api/projects/{id}/resources/consumption**
Obtener consumo de recursos

**Request:**
```bash
GET /api/projects/1/resources/consumption?resourceType=CPU&startDate=2025-02-01&endDate=2025-02-28
Authorization: Bearer {token}
```

**Response:**
```json
{
  "projectId": 1,
  "resourceType": "CPU",
  "startDate": "2025-02-01",
  "endDate": "2025-02-28",
  "totalQuantity": 1250.50,
  "unit": "core-hours",
  "totalCost": 125.05,
  "avgDailyCost": 4.47,
  "dailyBreakdown": [
    {
      "date": "2025-02-01",
      "quantity": 45.00,
      "cost": 4.50
    }
  ]
}
```

---

### **8. DOCUMENTS API**

#### **GET /api/projects/{id}/documents**
Obtener documentos del proyecto

**Request:**
```bash
GET /api/projects/1/documents?documentType=SPEC
Authorization: Bearer {token}
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "projectId": 1,
      "title": "Technical Specifications v2.0",
      "description": "Complete technical specifications for AI platform",
      "documentType": "SPEC",
      "filePath": "/projects/1/docs/tech-specs-v2.pdf",
      "fileSize": 2458624,
      "uploadedBy": 101,
      "uploadedByName": "John Doe",
      "version": "2.0",
      "createdAt": "2025-02-15T14:30:00Z"
    }
  ],
  "totalElements": 45
}
```

#### **POST /api/projects/{id}/documents**
Subir documento

**Request:**
```bash
POST /api/projects/1/documents
Authorization: Bearer {token}
Content-Type: multipart/form-data

title=Technical Specifications
documentType=SPEC
version=2.0
file=@tech-specs-v2.pdf
```

---

## 📦 SDK CLIENT

### **Java Client Example**

```java
// Configuración del cliente
CodeflowxProjectsClient client = CodeflowxProjectsClient.builder()
    .baseUrl("https://api.codeflowx.com")
    .apiKey("your-api-key")
    .build();

// Crear proyecto
ProjectRequest request = ProjectRequest.builder()
    .name("AI Platform Modernization")
    .projectCode("AIPM-2025-001")
    .projectType(ProjectType.CLIENT)
    .ownerId(101L)
    .budget(new BigDecimal("500000.00"))
    .build();

ProjectResponse project = client.projects().create(request);

// Agregar miembro al equipo
TeamMemberRequest memberReq = TeamMemberRequest.builder()
    .userId(102L)
    .role(ProjectRole.DEVELOPER)
    .allocationPercentage(new BigDecimal("80.00"))
    .hourlyRate(new BigDecimal("120.00"))
    .build();

client.projects().addMember(project.getId(), memberReq);

// Crear tarea
TaskRequest taskReq = TaskRequest.builder()
    .taskCode("AIPM-TASK-001")
    .title("Design database schema")
    .priority(Priority.HIGH)
    .assignedTo(102L)
    .estimatedHours(new BigDecimal("40.00"))
    .build();

TaskResponse task = client.projects().createTask(project.getId(), taskReq);

// Registrar tiempo
TimeTrackingRequest timeReq = TimeTrackingRequest.builder()
    .taskId(task.getId())
    .workDate(LocalDate.now())
    .hoursWorked(new BigDecimal("8.00"))
    .isBillable(true)
    .build();

client.projects().logTime(project.getId(), timeReq);

// Obtener resumen financiero
FinancialSummary summary = client.projects().getFinancialSummary(project.getId());
System.out.println("Budget utilization: " + summary.getBudgetUtilization() + "%");
```

### **Python Client Example**

```python
from codeflowx_projects import ProjectsClient

# Configuración del cliente
client = ProjectsClient(
    base_url="https://api.codeflowx.com",
    api_key="your-api-key"
)

# Crear proyecto
project = client.projects.create(
    name="AI Platform Modernization",
    project_code="AIPM-2025-001",
    project_type="CLIENT",
    owner_id=101,
    budget=500000.00
)

# Agregar miembro
member = client.projects.add_member(
    project_id=project.id,
    user_id=102,
    role="DEVELOPER",
    allocation_percentage=80.00,
    hourly_rate=120.00
)

# Crear tarea
task = client.projects.create_task(
    project_id=project.id,
    task_code="AIPM-TASK-001",
    title="Design database schema",
    priority="HIGH",
    assigned_to=102,
    estimated_hours=40.00
)

# Registrar tiempo
time_entry = client.projects.log_time(
    project_id=project.id,
    task_id=task.id,
    work_date="2025-02-05",
    hours_worked=8.00,
    is_billable=True
)

# Obtener resumen financiero
summary = client.projects.get_financial_summary(project.id)
print(f"Budget utilization: {summary.budget_utilization}%")
```

---

## 🔐 AUTENTICACIÓN

### **JWT Token Authentication**

```bash
# Obtener token
POST /api/auth/login
Content-Type: application/json

{
  "username": "user@company.com",
  "password": "secure_password"
}

# Response
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "expiresIn": 3600
}

# Usar token en requests
GET /api/projects
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

---

## 📊 RATE LIMITING

### **Límites por Endpoint:**
- **GET /projects:** 100 req/min
- **POST /projects:** 20 req/min
- **POST /time-tracking:** 200 req/min
- **GET /financial/summary:** 50 req/min

---

## 🎯 ERROR HANDLING

### **Error Response Format:**

```json
{
  "error": {
    "code": "PROJECT_NOT_FOUND",
    "message": "Project with ID 999 not found",
    "details": {
      "projectId": 999
    },
    "timestamp": "2025-10-29T14:30:00Z",
    "requestId": "req_789xyz"
  }
}
```

### **Common Error Codes:**
- `PROJECT_NOT_FOUND` - Proyecto no encontrado
- `MEMBER_ALREADY_EXISTS` - Miembro ya existe en el proyecto
- `TASK_NOT_FOUND` - Tarea no encontrada
- `INVALID_TIME_ENTRY` - Entrada de tiempo inválida
- `BUDGET_EXCEEDED` - Presupuesto excedido
- `INVOICE_ALREADY_PAID` - Factura ya pagada

---

## 🎯 CONCLUSIÓN

El módulo **Projects** proporciona APIs completas para:

- ✅ **Gestión de Proyectos** - CRUD completo
- ✅ **Gestión de Equipo** - Miembros y roles
- ✅ **Gestión de Tareas** - Planning y tracking
- ✅ **Time Tracking** - Registro de horas
- ✅ **Gestión Financiera** - Costos, facturas, ROI
- ✅ **Gestión de Recursos** - Consumo y costos
- ✅ **SDKs** - Java y Python
- ✅ **Autenticación** - JWT seguro

**Total de ~60 endpoints REST completamente documentados y funcionales.**
