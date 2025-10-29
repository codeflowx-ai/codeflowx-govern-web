# 🔄 PROCESOS BPMN - MÓDULO RAG

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación de procesos BPMN del módulo RAG

---

## 🎯 RESUMEN EJECUTIVO

El módulo **RAG** implementa **1 proceso BPMN principal** para la **evaluación y revisión** de sistemas RAG, con **User Tasks**, **Service Tasks** y **integración con Drools** para automatización de decisiones.

---

## 📋 PROCESO PRINCIPAL

### **rag-evaluation-review-v1.bpmn**

#### **Descripción del Proceso:**
Proceso automatizado para evaluación y revisión de sistemas RAG, incluyendo validación de calidad, análisis de rendimiento y aprobación de cambios.

#### **Elementos del Proceso:**

**Start Event:**
- `rag-evaluation-start` - Inicio del proceso de evaluación

**User Tasks:**
1. `review-rag-quality` - Revisión manual de calidad
2. `approve-rag-changes` - Aprobación de cambios
3. `validate-rag-performance` - Validación de rendimiento

**Service Tasks:**
1. `evaluate-rag-system` - Evaluación automática del sistema
2. `analyze-rag-metrics` - Análisis de métricas
3. `generate-rag-report` - Generación de reportes
4. `update-rag-status` - Actualización de estado

**End Events:**
- `rag-evaluation-completed` - Evaluación completada
- `rag-evaluation-rejected` - Evaluación rechazada

#### **Flujo del Proceso:**
```
1. Inicio → Evaluación automática del sistema
2. Análisis de métricas → Revisión manual de calidad
3. Validación de rendimiento → Aprobación de cambios
4. Generación de reportes → Actualización de estado
5. Finalización (aprobado/rechazado)
```

---

## 🔧 DELEGATES Y LÓGICA DE NEGOCIO

### **1. RagEvaluationDelegate**
```java
@Component
public class RagEvaluationDelegate implements JavaDelegate {
    
    @Override
    public void execute(DelegateExecution execution) {
        Long systemId = (Long) execution.getVariable("systemId");
        String evaluationType = (String) execution.getVariable("evaluationType");
        
        // Ejecutar evaluación del sistema RAG
        RagEvaluationResult result = ragService.evaluateSystem(systemId, evaluationType);
        
        // Establecer variables del proceso
        execution.setVariable("evaluationResult", result.getScore());
        execution.setVariable("evaluationStatus", result.getStatus());
        execution.setVariable("evaluationMetrics", result.getMetrics());
    }
}
```

### **2. RagMetricsAnalysisDelegate**
```java
@Component
public class RagMetricsAnalysisDelegate implements JavaDelegate {
    
    @Override
    public void execute(DelegateExecution execution) {
        Long systemId = (Long) execution.getVariable("systemId");
        
        // Analizar métricas del sistema RAG
        RagMetricsAnalysis analysis = metricsService.analyzeMetrics(systemId);
        
        // Establecer variables del proceso
        execution.setVariable("accuracy", analysis.getAccuracy());
        execution.setVariable("latency", analysis.getLatency());
        execution.setVariable("throughput", analysis.getThroughput());
        execution.setVariable("qualityScore", analysis.getQualityScore());
    }
}
```

### **3. RagReportGenerationDelegate**
```java
@Component
public class RagReportGenerationDelegate implements JavaDelegate {
    
    @Override
    public void execute(DelegateExecution execution) {
        Long systemId = (Long) execution.getVariable("systemId");
        String evaluationType = (String) execution.getVariable("evaluationType");
        
        // Generar reporte de evaluación
        RagEvaluationReport report = reportService.generateReport(systemId, evaluationType);
        
        // Establecer variables del proceso
        execution.setVariable("reportId", report.getId());
        execution.setVariable("reportUrl", report.getUrl());
        execution.setVariable("reportStatus", report.getStatus());
    }
}
```

---

## 🎯 REGLAS DROOLS

### **1. Evaluación de Calidad RAG**
```java
rule "Evaluate RAG Quality"
when
    $evaluation : RagEvaluation(evaluationType == "QUALITY")
    $metrics : RagMetrics(accuracy >= 0.8, latency <= 200)
then
    $evaluation.setStatus("QUALITY_PASSED");
    $evaluation.setQualityScore(calculateQualityScore($metrics));
    update($evaluation);
end
```

### **2. Detección de Degradación**
```java
rule "Detect RAG Degradation"
when
    $system : RagSystem(status == "PRODUCTION")
    $currentMetrics : RagMetrics(accuracy < 0.7)
    $baselineMetrics : RagMetrics(accuracy >= 0.8)
then
    $system.setStatus("DEGRADED");
    createAlert("RAG_DEGRADATION", $system.getId());
    update($system);
end
```

### **3. Aprobación Automática**
```java
rule "Auto Approve RAG Changes"
when
    $evaluation : RagEvaluation(
        evaluationType == "PERFORMANCE",
        accuracy >= 0.85,
        latency <= 150,
        qualityScore >= 0.8
    )
then
    $evaluation.setStatus("AUTO_APPROVED");
    $evaluation.setApprovalReason("Meets all quality criteria");
    update($evaluation);
end
```

---

## 📊 VARIABLES DEL PROCESO

### **Variables de Entrada:**
- `systemId` (Long) - ID del sistema RAG
- `evaluationType` (String) - Tipo de evaluación
- `evaluatorId` (String) - ID del evaluador
- `evaluationCriteria` (Map) - Criterios de evaluación

### **Variables de Salida:**
- `evaluationResult` (Double) - Resultado de la evaluación
- `evaluationStatus` (String) - Estado de la evaluación
- `evaluationMetrics` (Map) - Métricas de evaluación
- `reportId` (Long) - ID del reporte generado
- `reportUrl` (String) - URL del reporte
- `approvalStatus` (String) - Estado de aprobación

---

## 🔄 INTEGRACIÓN CON OTROS MÓDULOS

### **Módulo de Agentes:**
- **Tracking de uso** por agente durante evaluación
- **Métricas de rendimiento** por agente
- **Alertas** cuando agentes usan sistemas degradados

### **Módulo de Modelos:**
- **Validación** de modelos utilizados en RAG
- **Compatibilidad** entre modelos y sistemas RAG
- **Optimización** conjunta de modelos y RAG

### **Módulo de Compliance:**
- **Verificación** de compliance en evaluaciones
- **Auditoría** de procesos de evaluación
- **Reportes** de compliance automáticos

---

## ✅ CONCLUSIÓN

El **proceso BPMN del módulo RAG** proporciona:

- 🔄 **1 proceso principal** de evaluación y revisión
- 👥 **3 User Tasks** para intervención humana
- ⚙️ **4 Service Tasks** para automatización
- 🎯 **3 reglas Drools** para decisiones automáticas
- 🔗 **Integración completa** con otros módulos

**Este proceso está diseñado** para garantizar la calidad y rendimiento de sistemas RAG mediante evaluación automatizada y revisión humana cuando es necesario.
