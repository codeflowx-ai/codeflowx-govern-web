# Arquitectura: Separación Microservicio BPMN

## 📋 Contexto

El motor BPMN (Flowable) actualmente está embebido en el WAR del frontend ZK. Para la demo y producción, se separará en un microservicio independiente para:

- **Escalabilidad**: Escalar independientemente el engine BPMN según carga de procesos
- **Resiliencia**: Aislar fallos del motor BPMN del frontend
- **Reutilización**: Mismo engine para frontend ZK, APIs REST, SDKs
- **Observabilidad**: Métricas y logs separados

## 🏗️ Arquitectura Propuesta

```
┌─────────────────────────────────────────────────────────────┐
│              FRONTEND ZK (suinsit.nova.web)                  │
│                                                              │
│  - TaskInboxViewModel (Bandeja de Tareas)                   │
│  - Formularios ZUL para User Tasks                          │
│  - BpmnClient (REST client interno)                         │
│                                                              │
│  Puerto: 8080                                                │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       │ HTTP interno (mismo namespace K8s)
                       │ No requiere autenticación
                       │
┌──────────────────────▼──────────────────────────────────────┐
│          MICROSERVICIO BPMN (codeflowx-bpmn-service)         │
│                                                              │
│  - Flowable Engine (TaskService, RuntimeService)            │
│  - TaskManagementService                                     │
│  - REST Controllers (/api/v1/tasks/*)                       │
│  - PostgreSQL (ACT_* tables)                                 │
│                                                              │
│  Puerto: 8081                                                │
└─────────────────────────────────────────────────────────────┘
```

## 🔒 Seguridad - Comunicación Interna

### Modelo de Seguridad

**NO se requieren tokens JWT/OAuth** porque:

1. ✅ Microservicios en el **mismo namespace de Kubernetes**
2. ✅ Comunicación vía **Service Name interno** (no expuesto públicamente)
3. ✅ NetworkPolicy K8s limita acceso solo entre pods autorizados
4. ✅ El frontend ya validó la sesión del usuario (SSO/LDAP)

### Configuración de Red

```yaml
# NetworkPolicy K8s
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: bpmn-service-policy
  namespace: codeflowx
spec:
  podSelector:
    matchLabels:
      app: codeflowx-bpmn-service
  ingress:
    - from:
      - podSelector:
          matchLabels:
            app: codeflowx-frontend  # Solo el frontend puede llamar
      ports:
        - protocol: TCP
          port: 8081
```

## 📡 API Contract REST

### Endpoints Principales

#### 1. Obtener Tareas Pendientes
```http
GET http://codeflowx-bpmn-service:8081/api/v1/tasks/pending?username={username}

Response 200:
{
  "tasks": [
    {
      "id": "task-123",
      "name": "Aprobar Modelo IA",
      "description": "Revisar modelo de clasificación v2.1",
      "assignee": "john.doe",
      "priority": 90,
      "createTime": "2025-10-27T10:00:00Z",
      "processDefinitionId": "model-approval-v1",
      "formKey": "/workflow/model-approval-override.zul"
    }
  ],
  "total": 15
}
```

#### 2. Reclamar Tarea
```http
POST http://codeflowx-bpmn-service:8081/api/v1/tasks/{taskId}/claim
Content-Type: application/json

{
  "username": "john.doe"
}

Response 200:
{
  "success": true,
  "message": "Tarea reclamada exitosamente"
}
```

#### 3. Liberar Tarea
```http
POST http://codeflowx-bpmn-service:8081/api/v1/tasks/{taskId}/release

Response 200:
{
  "success": true
}
```

#### 4. Completar Tarea
```http
POST http://codeflowx-bpmn-service:8081/api/v1/tasks/{taskId}/complete
Content-Type: application/json

{
  "variables": {
    "approved": true,
    "comments": "Modelo aprobado para producción"
  }
}

Response 200:
{
  "success": true,
  "message": "Tarea completada"
}
```

#### 5. Asignar Tarea a Usuario
```http
POST http://codeflowx-bpmn-service:8081/api/v1/tasks/{taskId}/assign
Content-Type: application/json

{
  "username": "jane.smith"
}

Response 200:
{
  "success": true
}
```

#### 6. Obtener Variables de Tarea
```http
GET http://codeflowx-bpmn-service:8081/api/v1/tasks/{taskId}/variables

Response 200:
{
  "variables": {
    "modelId": "model-123",
    "accuracy": 0.95,
    "priority": "HIGH"
  }
}
```

## 💻 Implementación Frontend

### Cliente REST Interno

```java
// BpmnClient.java
@Service
public class BpmnClient {
    
    @Value("${bpmn.service.url}")
    private String bpmnServiceUrl; // http://codeflowx-bpmn-service:8081
    
    private final RestTemplate restTemplate;
    
    public BpmnClient(RestTemplateBuilder builder) {
        this.restTemplate = builder
            .setConnectTimeout(Duration.ofSeconds(5))
            .setReadTimeout(Duration.ofSeconds(30))
            .build();
    }
    
    public List<TaskDTO> getPendingTasks(String username) {
        String url = String.format("%s/api/v1/tasks/pending?username=%s", 
                                    bpmnServiceUrl, username);
        TaskListResponse response = restTemplate.getForObject(url, TaskListResponse.class);
        return response != null ? response.getTasks() : new ArrayList<>();
    }
    
    public boolean claimTask(String taskId, String username) {
        String url = String.format("%s/api/v1/tasks/%s/claim", bpmnServiceUrl, taskId);
        ClaimRequest request = new ClaimRequest(username);
        try {
            restTemplate.postForObject(url, request, ApiResponse.class);
            return true;
        } catch (Exception e) {
            log.error("Error claiming task: {}", e.getMessage());
            return false;
        }
    }
    
    public boolean releaseTask(String taskId) {
        String url = String.format("%s/api/v1/tasks/%s/release", bpmnServiceUrl, taskId);
        try {
            restTemplate.postForObject(url, null, ApiResponse.class);
            return true;
        } catch (Exception e) {
            log.error("Error releasing task: {}", e.getMessage());
            return false;
        }
    }
    
    public boolean assignTask(String taskId, String username) {
        String url = String.format("%s/api/v1/tasks/%s/assign", bpmnServiceUrl, taskId);
        AssignRequest request = new AssignRequest(username);
        try {
            restTemplate.postForObject(url, request, ApiResponse.class);
            return true;
        } catch (Exception e) {
            log.error("Error assigning task: {}", e.getMessage());
            return false;
        }
    }
}

// DTOs
@Data
@AllArgsConstructor
public class ClaimRequest {
    private String username;
}

@Data
@AllArgsConstructor
public class AssignRequest {
    private String username;
}

@Data
public class TaskListResponse {
    private List<TaskDTO> tasks;
    private int total;
}

@Data
public class ApiResponse {
    private boolean success;
    private String message;
}
```

### ViewModel Adaptado

```java
// TaskInboxViewModel.java
@Slf4j
@Getter
@Setter
public class TaskInboxViewModel extends MasterPage {
    
    @WireVariable
    private BpmnClient bpmnClient; // Cliente REST, no TaskService directo
    
    @Getter
    private boolean mockMode = false;
    
    @Command
    @NotifyChange({"allTasks", "filteredTasks", "totalTasks"})
    public void loadTasks() {
        if (mockMode) {
            loadMockTasks(); // Para demos
            return;
        }
        
        try {
            log.info("📥 Cargando tareas desde microservicio BPMN");
            allTasks = bpmnClient.getPendingTasks(currentUsername);
            filteredTasks = new ArrayList<>(allTasks);
            totalTasks = allTasks.size();
            
            calculateStats();
            
            log.info("✅ Cargadas {} tareas desde microservicio", totalTasks);
        } catch (Exception e) {
            log.error("❌ Error cargando tareas desde microservicio: {}", e.getMessage(), e);
            Messagebox.show("Error conectando con motor BPMN: " + e.getMessage(), 
                            "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    public void claimTask(@BindingParam("task") TaskDTO task) {
        try {
            boolean success = bpmnClient.claimTask(task.getId(), currentUsername);
            if (success) {
                Messagebox.show("Tarea reclamada exitosamente", "Éxito", 
                                Messagebox.OK, Messagebox.INFORMATION);
                loadTasks();
            } else {
                Messagebox.show("Error reclamando tarea", "Error", 
                                Messagebox.OK, Messagebox.ERROR);
            }
        } catch (Exception e) {
            log.error("❌ Error reclamando tarea: {}", e.getMessage(), e);
            Messagebox.show("Error: " + e.getMessage(), "Error", 
                            Messagebox.OK, Messagebox.ERROR);
        }
    }
}
```

## ⚙️ Configuración

### Frontend (application.yml)

```yaml
# application.yml - Frontend
spring:
  application:
    name: codeflowx-frontend
    
# BPMN Microservice
bpmn:
  service:
    url: http://codeflowx-bpmn-service:8081
    enabled: true  # false para modo MOCK en demos
    
# Deshabilitar Flowable embebido
flowable:
  enabled: false  # NO cargar engine localmente
  process-definition-location-prefix: classpath*:/processes/  # Ignorado
```

### Microservicio BPMN (application.yml)

```yaml
# application.yml - BPMN Microservice
spring:
  application:
    name: codeflowx-bpmn-service
  datasource:
    url: jdbc:postgresql://postgres:5432/flowable
    username: ${DB_USER:flowable}
    password: ${DB_PASSWORD:flowable}
    driver-class-name: org.postgresql.Driver
    
flowable:
  enabled: true
  async-executor-activate: true
  database-schema-update: true
  process-definition-location-prefix: classpath*:/processes/
  
server:
  port: 8081
  
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,info
  metrics:
    export:
      prometheus:
        enabled: true
```

## 🐳 Despliegue Kubernetes

### Frontend Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: codeflowx-frontend
  namespace: codeflowx
spec:
  replicas: 2
  selector:
    matchLabels:
      app: codeflowx-frontend
  template:
    metadata:
      labels:
        app: codeflowx-frontend
    spec:
      containers:
      - name: frontend
        image: codeflowx/frontend:1.1.0
        ports:
        - containerPort: 8080
        env:
        - name: BPMN_SERVICE_URL
          value: "http://codeflowx-bpmn-service:8081"
        - name: SPRING_PROFILES_ACTIVE
          value: "production"
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1000m"
---
apiVersion: v1
kind: Service
metadata:
  name: codeflowx-frontend
  namespace: codeflowx
spec:
  selector:
    app: codeflowx-frontend
  ports:
  - port: 8080
    targetPort: 8080
  type: LoadBalancer  # Expuesto públicamente
```

### BPMN Service Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: codeflowx-bpmn-service
  namespace: codeflowx
spec:
  replicas: 3  # Escalado horizontal
  selector:
    matchLabels:
      app: codeflowx-bpmn-service
  template:
    metadata:
      labels:
        app: codeflowx-bpmn-service
    spec:
      containers:
      - name: bpmn-service
        image: codeflowx/bpmn-service:1.0.0
        ports:
        - containerPort: 8081
        env:
        - name: SPRING_DATASOURCE_URL
          value: "jdbc:postgresql://postgres:5432/flowable"
        - name: SPRING_PROFILES_ACTIVE
          value: "production"
        resources:
          requests:
            memory: "1Gi"
            cpu: "1000m"
          limits:
            memory: "2Gi"
            cpu: "2000m"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8081
          initialDelaySeconds: 60
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 8081
          initialDelaySeconds: 30
          periodSeconds: 5
---
apiVersion: v1
kind: Service
metadata:
  name: codeflowx-bpmn-service
  namespace: codeflowx
spec:
  selector:
    app: codeflowx-bpmn-service
  ports:
  - port: 8081
    targetPort: 8081
  type: ClusterIP  # Solo interno, no expuesto
```

## 📊 Observabilidad

### Métricas Prometheus

```yaml
# Métricas BPMN específicas
flowable_process_instances_active{process="model-approval-v1"} 45
flowable_process_instances_completed{process="model-approval-v1"} 1203
flowable_tasks_pending{process="model-approval-v1"} 12
flowable_tasks_avg_duration_seconds{process="model-approval-v1"} 3600
```

### Logs Estructurados

```json
{
  "timestamp": "2025-10-27T10:15:30Z",
  "service": "codeflowx-bpmn-service",
  "level": "INFO",
  "message": "Task claimed",
  "taskId": "task-123",
  "username": "john.doe",
  "processInstanceId": "proc-456",
  "duration_ms": 45
}
```

## 🚀 Plan de Migración

### Fase 1: Demo (Actual)
- ✅ Frontend con mockMode=true
- ✅ Bandeja de tareas MOCK (24 tareas demo)
- ✅ Flowable deshabilitado en frontend
- ✅ Sin microservicio BPMN aún

### Fase 2: Desarrollo Microservicio
- Crear repo `codeflowx-bpmn-service`
- Mover `TaskManagementService`
- Implementar REST controllers
- Tests unitarios e integración

### Fase 3: Integración
- Implementar `BpmnClient` en frontend
- Probar comunicación interna K8s
- Pruebas de carga

### Fase 4: Producción
- Desplegar ambos servicios
- Monitoreo y alertas
- Documentación operativa

## 🎯 Beneficios Específicos CodeflowX

1. **AI Governance**: Procesos largos (compliance, drift) no bloquean UI
2. **Multi-tenant**: Escalar BPMN por cliente independientemente
3. **Self-hosting**: Clientes instalan solo lo que necesitan
4. **SDK Reutilizable**: Java/Python usan mismo engine BPMN
5. **Costes**: Optimizar recursos según carga real de cada componente

---

**Versión**: 1.0  
**Fecha**: 2025-10-27  
**Estado**: Planificación - Demo con MOCK Mode


