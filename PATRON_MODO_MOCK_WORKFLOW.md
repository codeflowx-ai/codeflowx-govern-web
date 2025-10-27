# Patrón Modo MOCK - ViewModels Workflow

## Objetivo

Permitir ejecutar los viewmodels del workflow en modo simulado/demo para presentaciones y videos, sin necesidad de:
- Tener procesos BPMN reales ejecutándose
- Conectar con servicios externos
- Tener datos reales en base de datos

## Implementación

### 1. Parámetro de Activación

El modo MOCK se activa pasando el parámetro `mock=true` en la URL:

```
/workflow/task-inbox.zul?mock=true
/workflow/agent-approval.zul?taskId=123&mock=true
```

### 2. Estructura del ViewModel

```java
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class MiWorkflowViewModel extends MasterPage {
    
    // ========== Modo MOCK ==========
    private boolean mockMode = false;
    
    // ========== Servicios Flowable ==========
    @WireVariable
    private TaskService taskService;
    
    // ========== Datos ==========
    private List<TaskDto> tasks = new ArrayList<>();
    
    // ========== Inicialización ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        // Detectar modo MOCK
        String mockParam = Executions.getCurrent().getParameter("mock");
        mockMode = "true".equalsIgnoreCase(mockParam);
        
        log.info("🚀 Inicializando MiWorkflowViewModel - MOCK MODE: {}", mockMode);
        
        if (mockMode) {
            loadMockData();
        } else {
            loadRealData();
        }
    }
    
    // ========== Carga de Datos ==========
    
    private void loadMockData() {
        log.info("🎭 Cargando datos MOCK para demo...");
        
        // Crear datos de prueba
        tasks.add(createMockTask(1L, "Aprobar Agente IA Cliente Banco", "HIGH", "PENDING"));
        tasks.add(createMockTask(2L, "Revisar Sesgo en Modelo Crédito", "CRITICAL", "PENDING"));
        tasks.add(createMockTask(3L, "Evaluar Prompt Marketing", "MEDIUM", "IN_PROGRESS"));
        
        log.info("✅ {} tareas MOCK cargadas", tasks.size());
    }
    
    private TaskDto createMockTask(Long id, String name, String priority, String status) {
        TaskDto task = new TaskDto();
        task.setId(id);
        task.setName(name);
        task.setPriority(priority);
        task.setStatus(status);
        task.setAssignee("demo.user@codeflowx.com");
        task.setCreated(new Date());
        return task;
    }
    
    private void loadRealData() {
        log.info("💼 Cargando datos reales desde Flowable...");
        
        try {
            List<Task> flowableTasks = taskService.createTaskQuery()
                .taskAssignee(getUser().getUsername())
                .list();
            
            // Convertir a DTOs
            tasks = flowableTasks.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
                
            log.info("✅ {} tareas reales cargadas", tasks.size());
        } catch (Exception e) {
            log.error("Error cargando tareas reales", e);
        }
    }
    
    // ========== Comandos ==========
    
    @Command
    public void approve() {
        if (mockMode) {
            // Simular aprobación
            log.info("🎭 MOCK: Simulando aprobación...");
            Messagebox.show("✅ DEMO: Aprobación simulada exitosamente\n\n(Modo MOCK activado)", 
                           "Demo - Aprobación Exitosa", 
                           Messagebox.OK, Messagebox.INFORMATION,
                           e -> Executions.sendRedirect("task-inbox.zul?mock=true"));
        } else {
            // Lógica real
            try {
                taskService.complete(taskId);
                logActivity("APROBACION", "WORKFLOW_TASK", taskId != null ? Long.parseLong(taskId) : null, 
                           "Tarea aprobada");
                Messagebox.show("Tarea completada", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } catch (Exception e) {
                log.error("Error aprobando tarea", e);
                Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
            }
        }
    }
}
```

### 3. DTO para Datos MOCK

Crear DTOs simples para representar datos de demo:

```java
@Getter
@Setter
public class TaskDto {
    private Long id;
    private String name;
    private String description;
    private String priority;
    private String status;
    private String assignee;
    private Date created;
    private Map<String, Object> variables = new HashMap<>();
}
```

## Ejemplos por Tipo de ViewModel

### Task Inbox (Lista de Tareas)

```java
private void loadMockData() {
    tasks.add(createMockTask(1L, "Aprobar Agente IA - Cliente Banco XYZ", "HIGH", "PENDING", "governance-admin"));
    tasks.add(createMockTask(2L, "Revisar Sesgo Detectado - Modelo Crédito", "CRITICAL", "PENDING", "data-scientist"));
    tasks.add(createMockTask(3L, "Evaluar Prompt Marketing v2.1", "MEDIUM", "IN_PROGRESS", "prompt-engineer"));
    tasks.add(createMockTask(4L, "Compliance Check - RAG Sistema Legal", "HIGH", "PENDING", "compliance-officer"));
    tasks.add(createMockTask(5L, "Ethics Review - Chatbot Atención Cliente", "CRITICAL", "PENDING", "ethics-committee"));
}
```

### Agent Approval (Detalle de Aprobación)

```java
private void loadMockData() {
    // Datos del agente
    agentName = "AgentIA_ClienteBanco_v2.3";
    agentType = "CONVERSATIONAL";
    riskLevel = "HIGH";
    
    // Resultados de análisis
    complianceScore = 85;
    biasScore = 92;
    safetyScore = 88;
    
    // Variables del proceso
    processVariables.put("aiActCompliant", true);
    processVariables.put("biasDetected", false);
    processVariables.put("requiresEthicsReview", true);
    processVariables.put("estimatedImpact", "50000 usuarios/mes");
}
```

### Prompt Safety Review

```java
private void loadMockData() {
    promptContent = "Eres un asistente de atención al cliente bancario. " +
                   "Ayuda a los usuarios con consultas sobre saldos, " +
                   "transferencias y productos financieros.";
    
    safetyScore = 95;
    jailbreakDetected = false;
    injectionDetected = false;
    maliciousContentDetected = false;
    
    safetyRisks.add("✅ No se detectaron intentos de jailbreak");
    safetyRisks.add("✅ Sin inyección de prompts maliciosos");
    safetyRisks.add("⚠️ Recomendación: Agregar validación de datos personales");
}
```

### Bias Review

```java
private void loadMockData() {
    modelName = "Modelo_Scoring_Credito_v3.2";
    biasType = "DEMOGRAPHIC";
    affectedGroups.add("Género: Femenino (-5% aprobación)");
    affectedGroups.add("Edad: 18-25 años (-8% aprobación)");
    
    severityLevel = "HIGH";
    confidenceScore = 87.5;
    
    recommendations.add("🔧 Rebalancear dataset de entrenamiento");
    recommendations.add("🔧 Aplicar fairness constraints en modelo");
    recommendations.add("🔧 Auditoría mensual de métricas de sesgo");
}
```

## Indicadores Visuales para Modo MOCK

### 1. Banner en la Pantalla

Agregar en el ZUL un banner que se muestre solo en modo MOCK:

```xml
<div sclass="alert alert-warning" visible="@load(vm.mockMode)">
    🎭 <b>MODO DEMO</b> - Datos simulados para presentación. No se guardarán cambios reales.
</div>
```

### 2. Botones con Comportamiento MOCK

```xml
<button label="@load(vm.mockMode ? '🎭 Aprobar (DEMO)' : 'Aprobar')" 
        onClick="@command('approve')" 
        sclass="@load(vm.mockMode ? 'btn-warning' : 'btn-success')"/>
```

### 3. Logs Diferenciados

```java
if (mockMode) {
    log.info("🎭 MOCK: Acción simulada - {}", action);
} else {
    log.info("💼 REAL: Acción ejecutada - {}", action);
}
```

## Ventajas del Modo MOCK

1. ✅ **Demos sin dependencias** - No requiere Flowable, DB, servicios externos
2. ✅ **Datos consistentes** - Siempre los mismos datos para demos
3. ✅ **Sin side effects** - No modifica datos reales
4. ✅ **Rápido** - No hay latencia de servicios
5. ✅ **Grabación de videos** - Escenarios reproducibles
6. ✅ **Testing manual** - Verificar UI sin backend completo

## Implementación Paso a Paso

### Para cada ViewModel Workflow:

1. Agregar campo `private boolean mockMode = false;`
2. En `@AfterCompose`, detectar parámetro `mock`
3. Crear método `loadMockData()`
4. En cada `@Command`, verificar `if (mockMode)` y simular
5. Agregar datos de prueba realistas
6. Agregar indicadores visuales en ZUL

---

**Aplicar este patrón a los 25 viewmodels del workflow para tener capacidad de demo completa.**

