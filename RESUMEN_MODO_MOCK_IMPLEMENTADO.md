# Modo MOCK Implementado - Resumen

## ✅ IMPLEMENTADO

### TaskInboxViewModel - Bandeja de Tareas MOCK

**Funcionalidad:**
- Detecta parámetro `mock=true` en URL
- Carga 24 tareas simuladas (una por cada pantalla workflow)
- Cada tarea tiene su URL con `mock=true` preconfigurada
- Estadísticas calculadas correctamente (asignadas vs grupos)
- Filtros funcionan con datos MOCK

**Acceso:**
```
/workflow/task-inbox.zul?mock=true
```

**Tareas Creadas (24):**
1. ✅ AgentApprovalHumanOverride - mock-1
2. ✅ AlertResponse - mock-2  
3. ✅ BiasMitigationPlan - mock-3
4. ✅ BiasReview - mock-4
5. ✅ BiasUrgentDecision - mock-5
6. ✅ ComplianceReviewDecision - mock-6
7. ✅ ComplianceReview - mock-7
8. ✅ DatasetReviewReminder - mock-8
9. ✅ DriftAnalysis - mock-9
10. ✅ DriftReviewDecision - mock-10
11. ✅ EthicsCommitteeReview - mock-11
12. ✅ EthicsMitigationPlan - mock-12
13. ✅ EthicsReviewReminder - mock-13
14. ✅ EthicsReviewRequest - mock-14
15. ✅ HitlSlaReminder - mock-15
16. ✅ LlmEvaluationReview - mock-16
17. ✅ ModelApprovalHumanOverride - mock-17
18. ✅ ModelApprovalReminder - mock-18
19. ✅ ModelEvaluationReview - mock-19
20. ✅ PerformanceIntervention - mock-20
21. ✅ PerformanceReviewDecision - mock-21
22. ✅ PromptHumanReview - mock-23
23. ✅ RagEvaluationReview - mock-24
24. ✅ (TaskInbox es la bandeja misma)

---

## 🔄 SIGUIENTE PASO

Ahora cada ViewModel individual debe:

1. **Detectar modo MOCK** en `@AfterCompose`
2. **Cargar datos simulados** si `mockMode = true`
3. **Simular acciones** en comandos (@Command)
4. **Mostrar indicador visual** "MODO DEMO"

---

## Patrón para Cada ViewModel

```java
@AfterCompose
public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
    Selectors.wireComponents(view, this, false);
    super.doAfterCompose(view);
    initDao();
    
    // Detectar modo MOCK
    String mockParam = Executions.getCurrent().getParameter("mock");
    mockMode = "true".equalsIgnoreCase(mockParam);
    
    log.info("🚀 Inicializando MiViewModel - MOCK: {}", mockMode);
    
    if (mockMode) {
        loadMockData();
    } else {
        loadRealData();
    }
}

private void loadMockData() {
    log.info("🎭 Cargando datos MOCK...");
    
    // Datos simulados específicos de la pantalla
    taskId = Executions.getCurrent().getParameter("taskId");
    agentName = "AgentIA_ClienteBanco_v2.3";
    riskLevel = "HIGH";
    complianceScore = 85;
    biasDetected = false;
    // ... más datos realistas
}

@Command
public void approve() {
    if (mockMode) {
        log.info("🎭 MOCK: Simulando aprobación...");
        Messagebox.show(
            "✅ DEMO: Aprobación simulada exitosamente\n\n" +
            "Agente: " + agentName + "\n" +
            "Risk: " + riskLevel + "\n\n" +
            "(Modo MOCK - No se guardó en BD)", 
            "Demo - Aprobación Exitosa", 
            Messagebox.OK, Messagebox.INFORMATION,
            e -> Executions.sendRedirect("/workflow/task-inbox.zul?mock=true"));
    } else {
        // Lógica real
        taskService.complete(taskId);
        logActivity("APROBACION", "AGTAGENTAPPROVALS", taskId, "Agente aprobado");
    }
}
```

---

## Ejemplo de Datos MOCK por Tipo

### Agent Approval
```java
agentName = "AgentIA_ClienteBanco_v2.3";
agentType = "CONVERSATIONAL";
riskLevel = "HIGH";
complianceScore = 85;
biasScore = 92;
safetyScore = 88;
estimatedUsers = "50,000 usuarios/mes";
```

### Bias Review
```java
modelName = "Modelo_Scoring_Credito_v3.2";
biasType = "DEMOGRAPHIC";
affectedGroups = ["Género: Femenino (-5%)", "Edad: 18-25 (-8%)"];
severityLevel = "HIGH";
confidenceScore = 87.5;
```

### Prompt Review
```java
promptContent = "Eres un asistente de marketing...";
safetyScore = 95;
complianceScore = 88;
jailbreakDetected = false;
injectionDetected = false;
```

### Ethics Review
```java
systemName = "Chatbot Atención Médica v1.0";
systemType = "MEDICAL_AI";
impactLevel = "HIGH";
estimatedImpact = "10,000 pacientes/mes";
ethicalRisks = ["Privacidad datos médicos", "Sesgo diagnóstico"];
```

### Performance
```java
endpointName = "/api/v1/inference";
baselineLatency = 300;
currentLatency = 1200;
degradationPercent = 300;
criticalityLevel = "CRITICAL";
```

---

## Uso para Demos y Videos

### Flujo de Demo Completo:

1. **Inicio:** Acceder a `/workflow/task-inbox.zul?mock=true`
2. **Mostrar bandeja:** 24 tareas variadas por tipo y prioridad
3. **Navegar:** Click en cualquier tarea → Se abre pantalla con mock=true
4. **Interactuar:** Ver datos simulados, simular aprobaciones/rechazos
5. **Volver:** Automáticamente regresa a bandeja MOCK
6. **Repetir:** Probar diferentes pantallas

### Para Grabación de Video:

- Todas las pantallas tienen datos consistentes
- No hay errores de servicios no disponibles
- Navegación fluida entre pantallas
- Datos realistas y profesionales

---

## Estado Actual

✅ **TaskInboxViewModel:** Modo MOCK completamente implementado  
🔄 **Resto de ViewModels:** Pendiente implementar modo MOCK en cada uno

**Total pantallas accesibles desde bandeja MOCK:** 24/24 (URLs configuradas)  
**Total pantallas con datos MOCK implementados:** 1/24 (TaskInbox)

---

**Próximo paso:** Implementar `loadMockData()` y comandos simulados en las 23 pantallas restantes.

