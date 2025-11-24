# PROMPT: INC-012-006 - Validación de Post-Market Monitoring Plan Pre-Despliegue

**Incidencia:** INC-012-006  
**Prioridad:** 🟡 MEDIA  
**Artículo EU AI Act:** Art. 61 EU AI Act  
**Esfuerzo Estimado:** 4 días  
**Tipo:** Java + BPMN

---

## CONTEXTO

El Art. 61 EU AI Act requiere un plan de monitoreo post-mercado para sistemas de IA de alto riesgo. El proceso actual no valida que este plan esté definido antes de permitir despliegue, lo que puede llevar a despliegues sin cumplir requisitos regulatorios.

**Ubicación Actual:**
- Proceso BPMN: `model-approval-v1.bpmn`
- Delegate: `PreDeploymentCheckDelegate.java`
- No existe entidad `PostMarketMonitoringPlan` (debe crearse)

**Problema:**
- Falta de cumplimiento Art. 61 EU AI Act
- Riesgo de despliegue sin plan de monitoreo
- No hay validación pre-despliegue de plan de monitoreo

---

## REQUISITOS

1. **Crear entidad PostMarketMonitoringPlan** si no existe
2. **Añadir validación en PreDeploymentCheckDelegate** para sistemas alto riesgo
3. **Añadir condición en checklist** pre-despliegue (INC-012-001)
4. **Integrar con workflow BPMN** para bloquear despliegue si falta plan
5. **Mostrar advertencia en UI** si modelo es alto riesgo y plan no definido

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear Entidad: `PostMarketMonitoringPlan.java`

**Ubicación:** `com.codeflowx.govern.entity.monitoring.PostMarketMonitoringPlan`

```java
package com.codeflowx.govern.entity.monitoring;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Entidad para plan de monitoreo post-mercado
 * Requisito: Art. 61 EU AI Act
 * Incidencia: INC-012-006
 */
@Entity
@Table(name = "PMMPOSTMARKETMONITORINGPLANS")
public class PostMarketMonitoringPlan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXPLAN")
    private Long idxplan;
    
    @Column(name = "IDXMODEL", nullable = false)
    private Long idxmodel;
    
    @Column(name = "PMMPLANNAME", length = 255)
    private String pmmplanname;
    
    @Column(name = "PMMDESCRIPTION", columnDefinition = "TEXT")
    private String pmmdescription;
    
    @Column(name = "PMMMONITORINGFREQUENCY", length = 50)
    private String pmmmonitoringfrequency; // DAILY, WEEKLY, MONTHLY, etc.
    
    @Column(name = "PMMMETRICS", columnDefinition = "jsonb")
    private String pmmetrics; // JSON con métricas a monitorear
    
    @Column(name = "PMMALERTTHRESHOLDS", columnDefinition = "jsonb")
    private String pmmalertthresholds; // JSON con umbrales de alerta
    
    @Column(name = "PMMRESPONSIBLE", length = 255)
    private String pmmresponsible; // Persona responsable
    
    @Column(name = "PMMAPPROVED")
    private Boolean pmmapproved;
    
    @Column(name = "PMMAPPROVEDBY", length = 255)
    private String pmmapprovedby;
    
    @Column(name = "PMMAPPROVEDAT")
    private Timestamp pmmapprovedat;
    
    @Column(name = "PMMCREATEDAT")
    private Timestamp pmmcreatedat;
    
    @Column(name = "PMMUPDATEDAT")
    private Timestamp pmmupdatedat;
    
    // Getters y Setters
    // ...
}
```

### 2. Crear Tabla en Base de Datos

**Script SQL:**

```sql
CREATE TABLE PMMPOSTMARKETMONITORINGPLANS (
    IDXPLAN BIGSERIAL PRIMARY KEY,
    IDXMODEL BIGINT NOT NULL,
    PMMPLANNAME VARCHAR(255),
    PMMDESCRIPTION TEXT,
    PMMMONITORINGFREQUENCY VARCHAR(50),
    PMMMETRICS JSONB,
    PMMALERTTHRESHOLDS JSONB,
    PMMRESPONSIBLE VARCHAR(255),
    PMMAPPROVED BOOLEAN,
    PMMAPPROVEDBY VARCHAR(255),
    PMMAPPROVEDAT TIMESTAMP,
    PMMCREATEDAT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PMMUPDATEDAT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_PMM_MODEL FOREIGN KEY (IDXMODEL) REFERENCES MODMODELS(IDXMODEL)
);

CREATE INDEX IDX_PMM_MODEL ON PMMPOSTMARKETMONITORINGPLANS(IDXMODEL);
CREATE INDEX IDX_PMM_APPROVED ON PMMPOSTMARKETMONITORINGPLANS(PMMAPPROVED);
```

### 3. Crear Service: `PostMarketMonitoringPlanService.java`

**Ubicación:** `com.codeflowx.govern.service.monitoring.PostMarketMonitoringPlanService`

```java
package com.codeflowx.govern.service.monitoring;

import com.codeflowx.govern.entity.monitoring.PostMarketMonitoringPlan;
import com.codeflowx.govern.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestionar planes de monitoreo post-mercado
 * Requisito: INC-012-006 - Art. 61 EU AI Act
 */
@Service
@Transactional
public class PostMarketMonitoringPlanService {
    
    @Autowired
    private BusinessService businessService;
    
    /**
     * Obtiene plan de monitoreo para un modelo
     */
    public PostMarketMonitoringPlan getByModelId(Long modelId) {
        String query = "SELECT * FROM PMMPOSTMARKETMONITORINGPLANS " +
                      "WHERE IDXMODEL = ? ORDER BY IDXPLAN DESC LIMIT 1";
        List<PostMarketMonitoringPlan> plans = businessService.findBySQL(
            PostMarketMonitoringPlan.class, query, modelId);
        return plans.isEmpty() ? null : plans.get(0);
    }
    
    /**
     * Verifica si existe plan aprobado para un modelo
     */
    public boolean hasApprovedPlan(Long modelId) {
        PostMarketMonitoringPlan plan = getByModelId(modelId);
        return plan != null && 
               plan.getPmmapproved() != null && 
               plan.getPmmapproved();
    }
    
    /**
     * Verifica si existe plan (aprobado o no) para un modelo
     */
    public boolean hasPlan(Long modelId) {
        PostMarketMonitoringPlan plan = getByModelId(modelId);
        return plan != null;
    }
}
```

### 4. Modificar Delegate: `PreDeploymentCheckDelegate.java`

**Ubicación:** `com.codeflowx.govern.delegate.bpmn.PreDeploymentCheckDelegate`

Añadir validación de plan de monitoreo:

```java
@Autowired
private PostMarketMonitoringPlanService postMarketMonitoringPlanService;

/**
 * Valida que exista plan de monitoreo post-mercado para sistemas alto riesgo
 * Requisito: Art. 61 EU AI Act
 * Incidencia: INC-012-006
 */
private void validatePostMarketMonitoringPlan(Long modelId, Model model) {
    if (model.getModishighrisk() == null || !model.getModishighrisk()) {
        return; // No aplica para modelos no alto riesgo
    }
    
    log.info("Validando plan de monitoreo post-mercado para modelo alto riesgo: {}", modelId);
    
    // Verificar que existe plan
    if (!postMarketMonitoringPlanService.hasPlan(modelId)) {
        String errorMsg = String.format(
            "Plan de monitoreo post-mercado no encontrado para modelo alto riesgo (ID: %d). " +
            "Según Art. 61 EU AI Act, los sistemas de IA de alto riesgo requieren " +
            "un plan de monitoreo post-mercado antes del despliegue.",
            modelId
        );
        
        log.error(errorMsg);
        throw new BpmnError(
            "MONITORING_PLAN_NOT_FOUND",
            errorMsg
        );
    }
    
    // Verificar que plan está aprobado
    if (!postMarketMonitoringPlanService.hasApprovedPlan(modelId)) {
        String errorMsg = String.format(
            "Plan de monitoreo post-mercado no aprobado para modelo alto riesgo (ID: %d). " +
            "El plan debe estar aprobado antes del despliegue según Art. 61 EU AI Act.",
            modelId
        );
        
        log.error(errorMsg);
        throw new BpmnError(
            "MONITORING_PLAN_NOT_APPROVED",
            errorMsg
        );
    }
    
    log.info("Plan de monitoreo post-mercado validado exitosamente para modelo: {}", modelId);
}

// Llamar en método execute():
if (model.getModishighrisk() != null && model.getModishighrisk()) {
    validatePostMarketMonitoringPlan(modelId, model);
}
```

### 5. Integrar con Checklist Pre-Despliegue

Modificar `PreDeploymentControlsViewModel.java` (INC-012-001):

```java
@WireVariable
private PostMarketMonitoringPlanService postMarketMonitoringPlanService;

// En método loadControls(), añadir:

// 10. Post-Market Monitoring Plan (si es alto riesgo) - Art. 61
if (model != null && model.getModishighrisk() != null && model.getModishighrisk()) {
    boolean hasMonitoringPlan = postMarketMonitoringPlanService.hasPlan(modelId);
    boolean planApproved = postMarketMonitoringPlanService.hasApprovedPlan(modelId);
    controls.add(new ControlCheck(
        "Post-Market Monitoring Plan (Art. 61)",
        "Art. 61 - Plan de Monitoreo Post-Mercado",
        planApproved,
        planApproved ? "Aprobado" : (hasMonitoringPlan ? "Pendiente de aprobación" : "No definido"),
        planApproved ? "Cumplido" : "No cumplido",
        true // crítico
    ));
}
```

### 6. Añadir Regla Drools (Opcional)

**Ubicación:** `model-approval-scoring.drl`

```drl
/**
 * Regla: Post-Market Monitoring Plan Requerido para Alto Riesgo
 * Requisito: Art. 61 EU AI Act
 * Incidencia: INC-012-006
 */
rule "Post-Market Monitoring Plan Required for High Risk"
    when
        $model : Model(isHighRisk == true)
        not PostMarketMonitoringPlan(approved == true, modelId == $model.idxmodel)
    then
        $decision = "REJECTED";
        $justification = "Post-market monitoring plan not approved for high-risk system (Art. 61 EU AI Act). " +
                        "High-risk AI systems require a post-market monitoring plan to be defined and approved before deployment.";
        System.out.println("REJECTED: Post-market monitoring plan not approved for high-risk model " + $model.idxmodel);
end
```

---

## VALIDACIONES ADICIONALES RECOMENDADAS

1. **Validar que plan esté completo** (no solo aprobado, sino con todos los campos requeridos)
2. **Validar métricas definidas** en plan sean coherentes con modelo
3. **Validar umbrales de alerta** sean razonables
4. **Añadir workflow de aprobación** para planes de monitoreo

---

## PRUEBAS REQUERIDAS

1. **Test 1:** Intentar desplegar modelo alto riesgo sin plan → Debe lanzar BpmnError "MONITORING_PLAN_NOT_FOUND"
2. **Test 2:** Intentar desplegar modelo alto riesgo con plan no aprobado → Debe lanzar BpmnError "MONITORING_PLAN_NOT_APPROVED"
3. **Test 3:** Intentar desplegar modelo alto riesgo con plan aprobado → Debe permitir continuar
4. **Test 4:** Intentar desplegar modelo NO alto riesgo sin plan → Debe permitir (no aplica validación)
5. **Test 5:** Verificar que checklist pre-despliegue muestra validación de plan para alto riesgo
6. **Test 6:** Verificar creación y aprobación de plan de monitoreo

---

## REFERENCIAS

- **Art. 61 EU AI Act:** Plan de Monitoreo Post-Mercado
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_012_PUESTA_PRODUCCION.md#inc-012-006`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_012_PUESTA_PRODUCCION.md`

---

## NOTAS DE IMPLEMENTACIÓN

- Ajustar estructura de entidad según requisitos específicos del plan
- Considerar hacer campos del plan configurables según tipo de modelo
- Añadir UI para crear y gestionar planes de monitoreo
- Integrar con sistema de alertas para ejecutar monitoreo según frecuencia definida
- Considerar hacer plan obligatorio también para modelos no alto riesgo (mejora continua)

---

**Estado:** ✅ COMPLETADO

