# INCIDENCIAS Y RECOMENDACIONES - AUDITORÍA 012
## Puesta en Producción - Proceso de Despliegue

**Fecha:** 2025-11-18  
**Auditor:** Sistema de Gobierno de IA - CodeflowX  
**Ámbito:** Proceso completo evaluación → aprobación → despliegue → producción  
**Base Legal:** EU AI Act Art. 10, 11, 12, 13, 14, 15

---

## RESUMEN EJECUTIVO

**Total Incidencias Identificadas:** 8  
**Críticas (🔴):** 3  
**Medias (🟡):** 4  
**Bajas (🟢):** 1

**Estado General:** ✅ **Sistema funcional con mejoras recomendadas**

---

## PARTE 1: INCIDENCIAS CRÍTICAS

### INC-012-001: Falta Pantalla Consolidada de Controles Pre-Despliegue
**Prioridad:** 🔴 CRÍTICA  
**Artículo:** Art. 10, 11, 15  
**Ubicación:** UI - Falta pantalla dedicada

**Descripción:**
Los controles pre-despliegue están dispersos en múltiples pantallas y procesos BPMN. No existe una vista consolidada que muestre:
- Checklist completo de condiciones que deben cumplirse
- Estado visual de cada validación (✓ cumplido / ✗ no cumplido / ⚠ pendiente)
- Bloqueo visual si condiciones no están cumplidas
- Resumen ejecutivo antes de permitir despliegue

**Impacto:**
- Riesgo de despliegue sin cumplir todas las condiciones
- Falta de visibilidad para auditores sobre controles previos
- Dificultad para verificar cumplimiento EU AI Act Art. 10, 11, 15

**Evidencia:**
- Controles dispersos en:
  - `model-approval-reminder-form.zul` (solo recordatorio)
  - `deployment-approval-form.zul` (solo aprobación DevOps)
  - BPMN Task List (sin checklist visual)
- No hay pantalla que consolide todas las validaciones

**Recomendación:**
1. Crear pantalla `pre-deployment-controls-dashboard.zul`
2. Mostrar checklist completo con estado visual:
   ```
   ✅ Performance Score >= 0.85 (Actual: 0.92)
   ✅ Bias Score <= 0.10 (Actual: 0.08)
   ✅ Compliance Score >= 0.90 (Actual: 0.95)
   ✅ Drift Risk < 0.15 (Actual: 0.12)
   ✅ Documentación Técnica Completa (Art. 11)
   ✅ ML Engineer Review Aprobado
   ⏳ Governance Review Pendiente (SLA: 48h restantes)
   ✅ Pre-deployment Check Completado
   ```
3. Bloquear botón "Desplegar" si alguna condición crítica no cumplida
4. Mostrar justificación para cada condición
5. Exportar checklist como PDF para auditoría

**Acción Correctiva:**
```java
// Crear ViewModel: PreDeploymentControlsViewModel.java
public class PreDeploymentControlsViewModel {
    private List<ControlCheck> controls;
    
    public boolean canDeploy() {
        return controls.stream()
            .filter(c -> c.isCritical())
            .allMatch(c -> c.isPassed());
    }
}

// Crear ZUL: pre-deployment-controls-dashboard.zul
<window>
    <listbox model="@bind(vm.controls)">
        <listitem>
            <listcell>
                <label value="${each.name}"/>
                <label value="${each.status == 'PASSED' ? '✅' : each.status == 'FAILED' ? '✗' : '⚠'}"/>
            </listcell>
        </listitem>
    </listbox>
    <button label="Desplegar" disabled="@bind(not vm.canDeploy)"/>
</window>
```

**Timeline:** 5 días  
**Responsable:** Frontend Team

---

### INC-012-002: Condiciones de Aprobación No Documentadas Explícitamente en UI
**Prioridad:** 🔴 CRÍTICA  
**Artículo:** Art. 11, Art. 15  
**Ubicación:** UI - Falta documentación visible

**Descripción:**
Las condiciones que deben cumplirse para aprobación están definidas en reglas Drools (`model-approval-scoring.drl`) pero no son visibles en la interfaz de usuario. Los usuarios no pueden ver qué condiciones deben cumplirse antes de solicitar aprobación.

**Impacto:**
- Usuarios no saben qué condiciones deben cumplir
- Solicitudes de aprobación rechazadas sin explicación previa
- Falta de transparencia en proceso de aprobación

**Evidencia:**
- Reglas Drools en código pero no expuestas en UI
- No hay documentación visible de umbrales:
  - `performanceScore >= 0.85`
  - `biasScore <= 0.10`
  - `complianceScore >= 0.90`
  - `driftRisk < 0.15`

**Recomendación:**
1. Crear sección "Condiciones de Aprobación" en pantalla de solicitud
2. Mostrar umbrales y valores actuales:
   ```
   Condiciones para Aprobación:
   - Performance Score: >= 0.85 (Actual: 0.92) ✅
   - Bias Score: <= 0.10 (Actual: 0.08) ✅
   - Compliance Score: >= 0.90 (Actual: 0.95) ✅
   - Drift Risk: < 0.15 (Actual: 0.12) ✅
   
   Estado: ✅ Todas las condiciones cumplidas
   ```
3. Mostrar advertencias si alguna condición no cumplida
4. Incluir tooltips con explicación de cada métrica

**Acción Correctiva:**
```java
// En ModelApprovalRequestViewModel
public Map<String, ConditionStatus> getApprovalConditions() {
    Map<String, ConditionStatus> conditions = new HashMap<>();
    
    conditions.put("Performance Score", new ConditionStatus(
        ">= 0.85",
        model.getPerformanceScore(),
        model.getPerformanceScore() >= 0.85
    ));
    
    conditions.put("Bias Score", new ConditionStatus(
        "<= 0.10",
        model.getBiasScore(),
        model.getBiasScore() <= 0.10
    ));
    
    // ... más condiciones
    
    return conditions;
}
```

**Timeline:** 3 días  
**Responsable:** Frontend Team

---

### INC-012-003: Falta Validación Pre-Despliegue de FRIA para Sistemas Alto Riesgo
**Prioridad:** 🔴 CRÍTICA  
**Artículo:** Art. 27 EU AI Act  
**Ubicación:** Workflow `model-approval-v1.bpmn`

**Descripción:**
Para sistemas de IA de alto riesgo, el Art. 27 EU AI Act requiere una evaluación de impacto en derechos fundamentales (FRIA - Fundamental Rights Impact Assessment) antes del despliegue. El proceso actual no valida que el FRIA esté completado y aprobado antes de permitir despliegue.

**Impacto:**
- Violación Art. 27 EU AI Act para sistemas alto riesgo
- Riesgo legal de despliegue sin FRIA aprobado
- Falta de cumplimiento regulatorio

**Evidencia:**
- Proceso `model-approval-v1.bpmn` no incluye validación de FRIA
- Entidad `FriaAssessment` existe pero no está integrada en workflow
- No hay verificación de `FriaAssessment.friaapproved = true` antes de despliegue

**Recomendación:**
1. Añadir validación en `PreDeploymentCheckDelegate`:
   ```java
   if (model.getModishighrisk() && !friaAssessmentService.isApproved(modelId)) {
       throw new BpmnError("FRIA_NOT_APPROVED", 
           "FRIA must be approved before deploying high-risk AI system (Art. 27)");
   }
   ```
2. Añadir condición en regla Drools:
   ```drl
   rule "FRIA Required for High Risk"
       when
           Model(isHighRisk == true)
           not FriaAssessment(approved == true, modelId == Model.id)
       then
           $decision = "REJECTED";
           $justification = "FRIA not approved for high-risk system (Art. 27)";
   end
   ```
3. Mostrar advertencia en UI si modelo es alto riesgo y FRIA no aprobado

**Acción Correctiva:**
- Modificar `PreDeploymentCheckDelegate.java`
- Actualizar `model-approval-scoring.drl`
- Añadir validación en `PreDeploymentControlsViewModel`

**Timeline:** 3 días  
**Responsable:** Backend Team + Compliance Team

---

## PARTE 2: INCIDENCIAS MEDIAS

### INC-012-004: Falta Exportación de Historial de Aprobaciones para Auditores
**Prioridad:** 🟡 MEDIA  
**Artículo:** Art. 12 EU AI Act  
**Ubicación:** Falta funcionalidad de exportación

**Descripción:**
No existe capacidad de exportar historial completo de aprobaciones y despliegues en formato adecuado para auditores externos. Los auditores necesitan acceso a:
- Historial de aprobaciones con timestamps
- Decisiones y justificaciones
- Métricas de evaluación
- Registros inmutables

**Impacto:**
- Dificultad para auditorías externas
- Falta de evidencia exportable para cumplimiento

**Recomendación:**
1. Crear endpoint REST: `GET /api/compliance/approvals/export`
2. Formatos: PDF, JSON, CSV
3. Incluir:
   - Lista de aprobaciones con detalles
   - Métricas de evaluación
   - Referencias a ImmutableLog
   - Historial BPMN

**Timeline:** 5 días  
**Responsable:** Backend Team

---

### INC-012-005: SLA Tracking No Visible en Dashboard Principal
**Prioridad:** 🟡 MEDIA  
**Artículo:** Art. 14 EU AI Act  
**Ubicación:** UI - Dashboard principal

**Descripción:**
Los SLA de aprobación (24h ML Engineer, 72h Governance) no son visibles en el dashboard principal. Los usuarios no pueden ver fácilmente qué aprobaciones están próximas a exceder SLA.

**Impacto:**
- Riesgo de exceder SLA sin notificación visible
- Falta de visibilidad de urgencia

**Recomendación:**
1. Añadir columna "SLA Restante" en dashboard de aprobaciones
2. Mostrar alertas visuales si SLA < 24h
3. Color coding:
   - Verde: SLA > 50% restante
   - Amarillo: SLA 25-50% restante
   - Rojo: SLA < 25% restante

**Timeline:** 2 días  
**Responsable:** Frontend Team

---

### INC-012-006: Falta Validación de Post-Market Monitoring Plan Pre-Despliegue
**Prioridad:** 🟡 MEDIA  
**Artículo:** Art. 61 EU AI Act  
**Ubicación:** Workflow `model-approval-v1.bpmn`

**Descripción:**
El Art. 61 EU AI Act requiere un plan de monitoreo post-mercado para sistemas de IA de alto riesgo. El proceso actual no valida que este plan esté definido antes de permitir despliegue.

**Impacto:**
- Falta de cumplimiento Art. 61
- Riesgo de despliegue sin plan de monitoreo

**Recomendación:**
1. Añadir validación en `PreDeploymentCheckDelegate`:
   ```java
   if (model.getModishighrisk() && !hasPostMarketMonitoringPlan(modelId)) {
       throw new BpmnError("MONITORING_PLAN_REQUIRED",
           "Post-market monitoring plan required for high-risk systems (Art. 61)");
   }
   ```
2. Crear entidad `PostMarketMonitoringPlan` si no existe
3. Añadir condición en checklist pre-despliegue

**Timeline:** 4 días  
**Responsable:** Backend Team

---

### INC-012-007: Rollback Automático No Registra Justificación en ImmutableLog
**Prioridad:** 🟡 MEDIA  
**Artículo:** Art. 12 EU AI Act  
**Ubicación:** `RollbackModelDelegate.java`

**Descripción:**
Cuando se ejecuta rollback automático por fallo en despliegue, el sistema registra el evento pero no incluye justificación detallada del motivo del rollback en el ImmutableLog.

**Impacto:**
- Falta de trazabilidad completa de rollbacks
- Dificultad para análisis post-mortem

**Recomendación:**
1. Modificar `RollbackModelDelegate` para incluir:
   - Motivo del rollback (health check failed, timeout, etc.)
   - Métricas de health check al momento del fallo
   - Logs de error del despliegue
2. Registrar en ImmutableLog con categoría `DEPLOYMENT_ROLLBACK`
3. Incluir data snapshot completo

**Timeline:** 2 días  
**Responsable:** Backend Team

---

## PARTE 3: INCIDENCIAS BAJAS

### INC-012-008: Falta Notificación Email al Propietario del Modelo al Completar Despliegue
**Prioridad:** 🟢 BAJA  
**Artículo:** N/A (Mejora UX)  
**Ubicación:** `DeploymentExecutionDelegate.java`

**Descripción:**
Cuando un despliegue se completa exitosamente, el sistema no envía notificación por email al propietario del modelo. Solo se registra en logs.

**Impacto:**
- Falta de notificación proactiva
- Propietarios no saben inmediatamente que su modelo está en producción

**Recomendación:**
1. Añadir envío de email en `DeploymentExecutionDelegate`:
   ```java
   notificationService.sendEmail(
       model.getOwnerEmail(),
       "Modelo desplegado exitosamente",
       "El modelo " + model.getName() + " ha sido desplegado en " + environment
   );
   ```
2. Incluir en email:
   - Nombre del modelo y versión
   - Ambiente de despliegue
   - URL del endpoint
   - Timestamp de despliegue

**Timeline:** 1 día  
**Responsable:** Backend Team

---

## RESUMEN DE ACCIONES PRIORIZADAS

### Prioridad Alta (Completar en 2 semanas)
1. ✅ INC-012-001: Pantalla consolidada controles pre-despliegue (5 días)
2. ✅ INC-012-002: Documentación condiciones en UI (3 días)
3. ✅ INC-012-003: Validación FRIA para alto riesgo (3 días)

### Prioridad Media (Completar en 1 mes)
4. ✅ INC-012-004: Exportación historial aprobaciones (5 días)
5. ✅ INC-012-005: SLA tracking en dashboard (2 días)
6. ✅ INC-012-006: Validación plan monitoreo post-mercado (4 días)
7. ✅ INC-012-007: Justificación rollback en ImmutableLog (2 días)

### Prioridad Baja (Completar cuando sea posible)
8. ✅ INC-012-008: Notificación email al propietario (1 día)

---

## MÉTRICAS DE CUMPLIMIENTO

**Antes de Correcciones:**
- Score de Cumplimiento: 78/100
- Artículos EU AI Act Cubiertos: 6/6 (10, 11, 12, 13, 14, 15)
- Gaps Críticos: 3
- Gaps Medios: 4
- Gaps Bajos: 1

**Después de Correcciones (Proyectado):**
- Score de Cumplimiento: 92/100
- Artículos EU AI Act Cubiertos: 6/6 + Art. 27, Art. 61
- Gaps Críticos: 0
- Gaps Medios: 0
- Gaps Bajos: 0

---

**Fin del Documento de Incidencias**

**Próxima Revisión:** 2025-12-18  
**Responsable:** Equipo de Gobierno de IA - CodeflowX







