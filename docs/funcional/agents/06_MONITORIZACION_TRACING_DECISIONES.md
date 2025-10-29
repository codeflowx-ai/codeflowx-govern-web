# 🔍 MONITORIZACIÓN, TRACING Y REGISTRO DE DECISIONES - AGENTES

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentar cómo funciona la monitorización, tracing y registro de decisiones de agentes

---

## 📊 RESUMEN EJECUTIVO

El sistema de CodeflowX Govern implementa un **sistema completo de monitorización, tracing y auditoría** para agentes de IA, que incluye:

- **Monitorización continua** con métricas en tiempo real
- **Tracing completo** de decisiones y ejecuciones
- **Registro de auditoría** para compliance y trazabilidad
- **Procesos BPMN** automatizados para gestión de alertas

---

## 🔄 ARQUITECTURA DE MONITORIZACIÓN

### **Componentes Principales:**

```
┌─────────────────────────────────────────────────────┐
│  CAPA DE MONITORIZACIÓN                              │
│  • ComplianceMonitoringService                      │
│  • TaskManagementService (Bpmmonitor)              │
│  • AgentDecisionsLogViewModel                       │
└─────────────────────────────────────────────────────┘
                             ↓
┌─────────────────────────────────────────────────────┐
│  CAPA DE TRACING                                     │
│  • LogLowAlertDelegate                              │
│  • PolicyAuditLog (Ssoractividad)                  │
│  • AgentWorkflow (trazas de ejecución)             │
└─────────────────────────────────────────────────────┘
                             ↓
┌─────────────────────────────────────────────────────┐
│  CAPA DE PROCESOS BPMN                              │
│  • compliance-monitoring-v1.bpmn                    │
│  • Alertas automáticas                              │
│  • Escalamiento de incidencias                     │
└─────────────────────────────────────────────────────┘
```

---

## 📈 MONITORIZACIÓN CONTINUA

### **1. ComplianceMonitoringService**

**Ubicación:** `src/main/java/com/codeflowx/govern/workflow/services/ComplianceMonitoringService.java`

**Funcionalidades:**
- **Ejecución programada** cada 24 horas
- **Check automático** de compliance en agentes y modelos
- **Generación de alertas** por incumplimientos
- **Registro en ComplianceAssessment**

**Flujo de Monitorización:**
```java
public Map<String, Object> executeScheduledCompliance() {
    // 1. Obtener agentes activos
    List<Agent> agents = getActiveAgents();
    
    // 2. Ejecutar compliance check por agente
    for (Agent agent : agents) {
        Map<String, Object> checkResult = checkSystemCompliance("AGENT", agent.getIdxagent());
        
        // 3. Crear registro de ComplianceAssessment
        ComplianceAssessment assessment = new ComplianceAssessment();
        assessment.setAssessmentname("AGENT Compliance - ID: " + agent.getIdxagent());
        assessment.setComplianceframework("AI_GOVERNANCE");
        assessment.setStatus(checkResult.getStatus());
        
        // 4. Guardar en BBDD
        businessService.save(assessment);
    }
    
    // 5. Retornar métricas agregadas
    return result;
}
```

### **2. Métricas Monitoreadas**

| Métrica | Descripción | Umbral | Acción |
|---------|-------------|--------|--------|
| **Compliance Score** | Puntuación de cumplimiento (0-100) | < 80% | Alerta |
| **Response Time** | Tiempo de respuesta promedio | > 500ms | Warning |
| **Error Rate** | Tasa de errores | > 5% | Critical |
| **Availability** | Disponibilidad del agente | < 99% | Critical |
| **Resource Usage** | Uso de CPU/Memoria | > 90% | Warning |

---

## 🔍 TRACING DE DECISIONES

### **1. AgentDecisionsLogViewModel**

**Ubicación:** `src/main/java/com/codeflowx/govern/viewmodel/agents/AgentDecisionsLogViewModel.java`

**Funcionalidades:**
- **Log completo** de decisiones de agentes
- **Trazabilidad** de workflows ejecutados
- **Auditoría** de tareas y procesos
- **KPIs** de rendimiento y éxito

**Entidades Rastreadas:**
```java
// Tareas del agente
private List<AgentWorkflow> tasks = new ArrayList<>();

// Workflows ejecutados
private List<AgentWorkflow> workflows = new ArrayList<>();

// Logs de auditoría
private List<PolicyAuditLog> auditLogs = new ArrayList<>();

// KPIs calculados
private Long totalTasks = 0L;
private Long completedTasks = 0L;
private BigDecimal successRate = BigDecimal.ZERO;
```

### **2. Registro de Actividades**

**Método de Auditoría:**
```java
private void logActivity(String action, String model, Long pk, String mensaje) {
    Ssoractividad activityLog = new Ssoractividad();
    activityLog.setUsername(getUser().getUsername());
    activityLog.setAccion(action);
    activityLog.setAlta(new java.sql.Timestamp(System.currentTimeMillis()));
    activityLog.setModulo(model);
    activityLog.setIdtupla(pk != null ? pk.intValue() : 0);
    activityLog.setAplicacion(ctxBean.getApplicationName());
    activityLog.setValuetupla(mensaje);
    businessService.save(activityLog);
}
```

### **3. Trazabilidad Completa**

**Campos Rastreados:**
- **Usuario** que ejecutó la acción
- **Timestamp** de la acción
- **Módulo** afectado (AGENTS, MODELS, etc.)
- **ID de tupla** modificada
- **Descripción** de la acción
- **Aplicación** origen

---

## 🚨 GESTIÓN DE ALERTAS

### **1. LogLowAlertDelegate**

**Ubicación:** `src/main/java/com/codeflowx/govern/workflow/delegates/LogLowAlertDelegate.java`

**Funcionalidades:**
- **Registro de alertas** de severidad baja
- **Log estructurado** con tipo y severidad
- **Integración** con procesos BPMN

**Implementación:**
```java
@Override
public void execute(DelegateExecution execution) {
    String alertType = (String) execution.getVariable("alertType");
    String severity = (String) execution.getVariable("severity");
    String message = (String) execution.getVariable("alertMessage");
    
    log.info("ℹ️ LOW ALERT:");
    log.info("   Type: {} | Severity: {}", alertType, severity);
    log.info("   Message: {}", message);
    
    execution.setVariable("lowAlertLogged", true);
}
```

### **2. Proceso BPMN de Compliance**

**Archivo:** `src/main/resources/processes/compliance-monitoring-v1.bpmn`

**Flujo del Proceso:**
```
[Timer Start - 24h] → [Execute Compliance Check] → [Non-Compliance?]
                                                          ↓
[Create Alerts] ← [Non-Compliant] ← [Compliance Gateway]
     ↓
[Review Issues] → [Critical Issue?] → [Create Incident] / [Schedule Review]
     ↓
[Update Dashboard] → [End]
```

**Características:**
- **Timer automático** cada 24 horas
- **Escalamiento** de incidencias críticas
- **Review manual** de issues no críticas
- **Dashboard updates** automáticos

---

## 📊 REGISTRO DE DECISIONES

### **1. TaskManagementService**

**Ubicación:** `src/main/java/com/codeflowx/govern/workflow/services/TaskManagementService.java`

**Funcionalidades:**
- **Registro en Bpmmonitor** para cada tarea
- **Trazabilidad** de asignaciones
- **Historial** de tareas por usuario/rol

**Registro de Tareas:**
```java
private void registerTaskAssignment(Task task, String username) {
    Bpmmonitor monitor = new Bpmmonitor();
    monitor.setUsername(username);
    monitor.setProcesskey(task.getProcessDefinitionId());
    monitor.setInstanceprocess(task.getProcessInstanceId());
    monitor.setInstancetask(task.getId());
    monitor.setNametask(task.getName());
    monitor.setTaskey(task.getTaskDefinitionKey());
    monitor.setStart(new Timestamp(System.currentTimeMillis()));
    monitor.setAlta(new Timestamp(System.currentTimeMillis()));
    
    businessService.save(monitor);
}
```

### **2. Entidades de Registro**

| Entidad | Propósito | Campos Clave |
|---------|-----------|--------------|
| **Bpmmonitor** | Trazabilidad de tareas BPMN | `username`, `processkey`, `instancetask` |
| **Ssoractividad** | Auditoría de acciones | `username`, `accion`, `modulo`, `idtupla` |
| **ComplianceAssessment** | Evaluaciones de compliance | `assessmentname`, `status`, `overallscore` |
| **PolicyAuditLog** | Log de políticas | `policyname`, `action`, `timestamp` |

---

## 🔄 PROCESOS DE MONITORIZACIÓN

### **1. Proceso Principal: compliance-monitoring-v1**

**Trigger:** Timer cada 24 horas  
**Duración:** 2-4 horas  
**Actores:** Sistema automático + Compliance Officers

**Tareas del Proceso:**
| Tarea | Tipo | Asignado a | Descripción |
|-------|------|------------|-------------|
| `executeComplianceCheck` | Service Task | Sistema | Ejecuta checks automáticos |
| `createAlerts` | Service Task | Sistema | Genera alertas por incumplimientos |
| `reviewIssues` | User Task | Compliance Officers | Revisión manual de issues |
| `createIncident` | User Task | Compliance Officers | Creación de incidencias críticas |
| `scheduleReview` | Service Task | Sistema | Programa revisión no crítica |

### **2. Escalamiento Automático**

**Condiciones de Escalamiento:**
- **Compliance Score < 50%** → Incidencia crítica
- **Error Rate > 10%** → Alerta alta
- **Availability < 95%** → Alerta crítica
- **Review pendiente > 7 días** → Escalamiento automático

---

## 📈 DASHBOARDS Y MÉTRICAS

### **1. Dashboard de Monitorización**

**Métricas Principales:**
```json
{
  "complianceMetrics": {
    "totalSystemsChecked": 150,
    "compliantSystems": 135,
    "nonCompliantSystems": 15,
    "criticalIssues": 3,
    "complianceRate": 90.0
  },
  "performanceMetrics": {
    "averageResponseTime": 245.5,
    "throughput": 12500,
    "errorRate": 0.8,
    "availability": 99.9
  },
  "alertMetrics": {
    "activeAlerts": 5,
    "criticalAlerts": 1,
    "resolvedToday": 12,
    "avgResolutionTime": "2.5 hours"
  }
}
```

### **2. KPIs de Agentes**

**Métricas por Agente:**
- **Total de tareas** ejecutadas
- **Tasa de éxito** (completed/total)
- **Tiempo promedio** de ejecución
- **Errores** por tipo y frecuencia
- **Compliance score** histórico

---

## 🔐 AUDITORÍA Y COMPLIANCE

### **1. Trazabilidad Completa**

**Requisitos de Auditoría:**
- ✅ **Quién** ejecutó la acción (usuario)
- ✅ **Cuándo** se ejecutó (timestamp)
- ✅ **Qué** se modificó (entidad + ID)
- ✅ **Por qué** se ejecutó (justificación)
- ✅ **Dónde** se ejecutó (aplicación/módulo)

### **2. Retención de Datos**

**Políticas de Retención:**
- **Logs de auditoría:** 7 años
- **Métricas de performance:** 2 años
- **Alertas resueltas:** 1 año
- **Trazas de BPMN:** 5 años

### **3. Compliance Frameworks**

**Frameworks Soportados:**
- **GDPR:** Derecho al olvido, minimización de datos
- **ISO 27001:** Gestión de seguridad de la información
- **AI Act (EU):** Transparencia y explicabilidad
- **SOC 2:** Controles de seguridad y disponibilidad

---

## 🛠️ CONFIGURACIÓN Y MANTENIMIENTO

### **1. Configuración de Alertas**

**Umbrales Configurables:**
```yaml
monitoring:
  thresholds:
    compliance_score: 80
    response_time_ms: 500
    error_rate_percent: 5
    availability_percent: 99
    resource_usage_percent: 90
  
  schedules:
    compliance_check: "0 2 * * *"  # Diario a las 2 AM
    performance_check: "*/15 * * * *"  # Cada 15 minutos
    health_check: "*/5 * * * *"  # Cada 5 minutos
```

### **2. Mantenimiento**

**Tareas de Mantenimiento:**
- **Limpieza de logs** antiguos (automática)
- **Optimización de índices** (semanal)
- **Backup de auditoría** (diario)
- **Revisión de métricas** (mensual)

---

## ✅ CONCLUSIÓN

El sistema de CodeflowX Govern implementa un **ecosistema completo de monitorización** que incluye:

- ✅ **Monitorización continua** con ComplianceMonitoringService
- ✅ **Tracing completo** con AgentDecisionsLogViewModel
- ✅ **Registro de auditoría** con Ssoractividad y Bpmmonitor
- ✅ **Procesos BPMN** automatizados para gestión de alertas
- ✅ **Dashboards** con métricas en tiempo real
- ✅ **Compliance** con frameworks regulatorios

**Componentes Clave:**
- 1 servicio de monitorización principal
- 1 ViewModel de decisiones y tracing
- 1 proceso BPMN de compliance
- 4 entidades de registro y auditoría
- Múltiples delegados para gestión de alertas

**Estado:** Sistema de monitorización completamente documentado ✅
