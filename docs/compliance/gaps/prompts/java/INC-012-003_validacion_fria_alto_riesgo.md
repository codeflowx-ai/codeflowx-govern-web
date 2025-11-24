# PROMPT: INC-012-003 - Validación Pre-Despliegue de FRIA para Sistemas Alto Riesgo

**Incidencia:** INC-012-003  
**Prioridad:** 🔴 CRÍTICA  
**Artículo EU AI Act:** Art. 27 EU AI Act  
**Esfuerzo Estimado:** 3 días  
**Tipo:** Java + BPMN

---

## CONTEXTO

Para sistemas de IA de alto riesgo, el Art. 27 EU AI Act requiere una evaluación de impacto en derechos fundamentales (FRIA - Fundamental Rights Impact Assessment) antes del despliegue. El proceso actual no valida que el FRIA esté completado y aprobado antes de permitir despliegue.

**Ubicación Actual:**
- Proceso BPMN: `model-approval-v1.bpmn`
- Delegate: `PreDeploymentCheckDelegate.java`
- Entidad: `FriaAssessment` (existe pero no está integrada en workflow)
- Reglas Drools: `model-approval-scoring.drl`

**Problema:**
- Violación Art. 27 EU AI Act para sistemas alto riesgo
- Riesgo legal de despliegue sin FRIA aprobado
- Falta de cumplimiento regulatorio

---

## REQUISITOS

1. **Validar FRIA aprobado** antes de permitir despliegue para sistemas alto riesgo
2. **Añadir validación en PreDeploymentCheckDelegate**
3. **Añadir regla Drools** para rechazar si FRIA no aprobado
4. **Mostrar advertencia en UI** si modelo es alto riesgo y FRIA no aprobado
5. **Integrar con checklist** de controles pre-despliegue (INC-012-001)

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Modificar Delegate: `PreDeploymentCheckDelegate.java`

**Ubicación:** `com.codeflowx.govern.delegate.bpmn.PreDeploymentCheckDelegate`

Añadir validación de FRIA:

```java
package com.codeflowx.govern.delegate.bpmn;

import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.entity.fria.FriaAssessment;
import com.codeflowx.govern.service.BusinessService;
import com.codeflowx.govern.service.fria.FriaAssessmentService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.delegate.BpmnError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Delegate para validar condiciones pre-despliegue
 * Requisito: INC-012-003 - Art. 27 EU AI Act (FRIA para alto riesgo)
 */
@Component("preDeploymentCheckDelegate")
public class PreDeploymentCheckDelegate implements JavaDelegate {
    
    private static final Logger log = LoggerFactory.getLogger(PreDeploymentCheckDelegate.class);
    
    @Autowired
    private BusinessService businessService;
    
    @Autowired
    private FriaAssessmentService friaAssessmentService;
    
    @Override
    public void execute(DelegateExecution execution) {
        Long modelId = (Long) execution.getVariable("modelId");
        
        if (modelId == null) {
            throw new BpmnError("MISSING_MODEL_ID", "Model ID is required for pre-deployment check");
        }
        
        log.info("Ejecutando pre-deployment check para modelo: {}", modelId);
        
        // Obtener modelo
        Model model = businessService.findById(Model.class, modelId);
        if (model == null) {
            throw new BpmnError("MODEL_NOT_FOUND", "Model not found: " + modelId);
        }
        
        // Validar recursos e infraestructura (validación existente)
        validateInfrastructure(execution, model);
        
        // NUEVA VALIDACIÓN: FRIA para sistemas alto riesgo (INC-012-003)
        if (model.getModishighrisk() != null && model.getModishighrisk()) {
            validateFriaForHighRisk(modelId, model);
        }
        
        log.info("Pre-deployment check completado exitosamente para modelo: {}", modelId);
    }
    
    /**
     * Valida que el FRIA esté aprobado para sistemas de alto riesgo
     * Requisito: Art. 27 EU AI Act
     * Incidencia: INC-012-003
     */
    private void validateFriaForHighRisk(Long modelId, Model model) {
        log.info("Validando FRIA para modelo alto riesgo: {}", modelId);
        
        // Buscar FRIA asociado al modelo
        FriaAssessment fria = friaAssessmentService.getByModelId(modelId);
        
        if (fria == null) {
            String errorMsg = String.format(
                "FRIA no encontrado para modelo alto riesgo (ID: %d). " +
                "Según Art. 27 EU AI Act, los sistemas de IA de alto riesgo requieren " +
                "una evaluación de impacto en derechos fundamentales (FRIA) aprobada antes del despliegue.",
                modelId
            );
            
            log.error(errorMsg);
            throw new BpmnError(
                "FRIA_NOT_FOUND",
                errorMsg
            );
        }
        
        // Validar que FRIA esté aprobado
        if (fria.getFriaapproved() == null || !fria.getFriaapproved()) {
            String errorMsg = String.format(
                "FRIA no aprobado para modelo alto riesgo (ID: %d, FRIA ID: %d). " +
                "Según Art. 27 EU AI Act, el FRIA debe estar aprobado antes del despliegue de sistemas de alto riesgo.",
                modelId,
                fria.getIdxfria()
            );
            
            log.error(errorMsg);
            throw new BpmnError(
                "FRIA_NOT_APPROVED",
                errorMsg
            );
        }
        
        log.info("FRIA validado exitosamente para modelo alto riesgo: {} (FRIA ID: {})", 
            modelId, fria.getIdxfria());
    }
    
    /**
     * Validación de infraestructura (método existente)
     */
    private void validateInfrastructure(DelegateExecution execution, Model model) {
        // Validación existente de recursos, políticas, quotas, etc.
        // TODO: Mantener lógica existente
    }
}
```

### 2. Crear/Modificar Service: `FriaAssessmentService.java`

**Ubicación:** `com.codeflowx.govern.service.fria.FriaAssessmentService`

```java
package com.codeflowx.govern.service.fria;

import com.codeflowx.govern.entity.fria.FriaAssessment;
import com.codeflowx.govern.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestionar evaluaciones FRIA
 * Requisito: INC-012-003
 */
@Service
@Transactional
public class FriaAssessmentService {
    
    @Autowired
    private BusinessService businessService;
    
    /**
     * Obtiene FRIA asociado a un modelo
     */
    public FriaAssessment getByModelId(Long modelId) {
        String query = "SELECT * FROM FRIAASSESSMENTS WHERE IDXMODEL = ? ORDER BY IDXFRIA DESC LIMIT 1";
        List<FriaAssessment> assessments = businessService.findBySQL(FriaAssessment.class, query, modelId);
        return assessments.isEmpty() ? null : assessments.get(0);
    }
    
    /**
     * Verifica si FRIA está aprobado para un modelo
     */
    public boolean isApproved(Long modelId) {
        FriaAssessment fria = getByModelId(modelId);
        return fria != null && 
               fria.getFriaapproved() != null && 
               fria.getFriaapproved();
    }
}
```

### 3. Modificar Reglas Drools: `model-approval-scoring.drl`

**Ubicación:** `/src/main/resources/rules/model-approval-scoring.drl`

Añadir regla para validar FRIA:

```drl
package com.codeflowx.govern.rules.modelapproval

import com.codeflowx.govern.entity.models.Model
import com.codeflowx.govern.entity.models.ModelApproval
import com.codeflowx.govern.entity.fria.FriaAssessment

/**
 * Regla: FRIA Requerido para Sistemas Alto Riesgo
 * Requisito: Art. 27 EU AI Act
 * Incidencia: INC-012-003
 */
rule "FRIA Required for High Risk"
    when
        $model : Model(isHighRisk == true)
        not FriaAssessment(approved == true, modelId == $model.idxmodel)
    then
        $decision = "REJECTED";
        $justification = "FRIA not approved for high-risk system (Art. 27 EU AI Act). " +
                        "High-risk AI systems require a Fundamental Rights Impact Assessment " +
                        "to be completed and approved before deployment.";
        System.out.println("REJECTED: FRIA not approved for high-risk model " + $model.idxmodel);
end

/**
 * Regla: FRIA Aprobado para Alto Riesgo - Permitir Aprobación
 */
rule "FRIA Approved for High Risk - Allow Approval"
    when
        $model : Model(isHighRisk == true)
        $fria : FriaAssessment(approved == true, modelId == $model.idxmodel)
        $approval : ModelApproval(modelId == $model.idxmodel)
        $performanceScore : Double() from $approval.performanceScore
        $biasScore : Double() from $approval.biasScore
        $complianceScore : Double() from $approval.complianceScore
        $driftRisk : Double() from $approval.driftRisk
        eval($performanceScore >= 0.85)
        eval($biasScore <= 0.10)
        eval($complianceScore >= 0.90)
        eval($driftRisk < 0.15)
    then
        $decision = "APPROVED";
        $justification = "All conditions met including FRIA approval for high-risk system.";
        System.out.println("APPROVED: High-risk model " + $model.idxmodel + " with approved FRIA");
end
```

### 4. Integrar con Checklist Pre-Despliegue

Modificar `PreDeploymentControlsViewModel.java` (INC-012-001) para incluir validación FRIA:

```java
// En método loadControls(), añadir después de validación de alto riesgo:

// 8. FRIA Aprobado (si es alto riesgo) - Art. 27
if (model != null && model.getModishighrisk() != null && model.getModishighrisk()) {
    FriaAssessment fria = friaAssessmentService.getByModelId(modelId);
    boolean friaApproved = fria != null && 
        fria.getFriaapproved() != null && 
        fria.getFriaapproved();
    controls.add(new ControlCheck(
        "FRIA Aprobado (Art. 27)",
        "Art. 27 - Evaluación Impacto Derechos Fundamentales",
        friaApproved,
        friaApproved ? "Aprobado" : (fria != null ? "Pendiente de aprobación" : "No encontrado"),
        friaApproved ? "Cumplido" : "No cumplido",
        true // crítico
    ));
}
```

### 5. Añadir Validación en UI

Modificar pantalla de solicitud de aprobación para mostrar advertencia si es alto riesgo y FRIA no aprobado:

```java
// En ModelApprovalReminderViewModel.java

/**
 * Verifica si es modelo alto riesgo y FRIA no aprobado
 */
public boolean isHighRiskWithoutFria() {
    if (model == null || model.getModishighrisk() == null || !model.getModishighrisk()) {
        return false;
    }
    
    FriaAssessment fria = friaAssessmentService.getByModelId(model.getIdxmodel());
    return fria == null || fria.getFriaapproved() == null || !fria.getFriaapproved();
}

/**
 * Obtiene mensaje de advertencia FRIA
 */
public String getFriaWarningMessage() {
    if (!isHighRiskWithoutFria()) {
        return null;
    }
    
    return "⚠️ ADVERTENCIA: Este modelo es de ALTO RIESGO y requiere una evaluación FRIA " +
           "(Fundamental Rights Impact Assessment) aprobada según Art. 27 EU AI Act antes del despliegue. " +
           "Por favor, complete y apruebe el FRIA antes de solicitar aprobación.";
}
```

---

## VALIDACIONES ADICIONALES RECOMENDADAS

1. **Validar que FRIA esté completo** (no solo aprobado, sino con todos los campos requeridos)
2. **Mostrar fecha de aprobación FRIA** en checklist
3. **Validar vigencia del FRIA** (si aplica según políticas)
4. **Integrar con workflow de FRIA** para notificar cuando esté listo

---

## PRUEBAS REQUERIDAS

1. **Test 1:** Intentar desplegar modelo alto riesgo sin FRIA → Debe lanzar BpmnError "FRIA_NOT_FOUND"
2. **Test 2:** Intentar desplegar modelo alto riesgo con FRIA no aprobado → Debe lanzar BpmnError "FRIA_NOT_APPROVED"
3. **Test 3:** Intentar desplegar modelo alto riesgo con FRIA aprobado → Debe permitir continuar
4. **Test 4:** Intentar desplegar modelo NO alto riesgo sin FRIA → Debe permitir (no aplica validación)
5. **Test 5:** Verificar regla Drools rechaza modelo alto riesgo sin FRIA aprobado
6. **Test 6:** Verificar que checklist pre-despliegue muestra validación FRIA para alto riesgo

---

## REFERENCIAS

- **Art. 27 EU AI Act:** Evaluación de Impacto en Derechos Fundamentales (FRIA)
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_012_PUESTA_PRODUCCION.md#inc-012-003`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_012_PUESTA_PRODUCCION.md`
- **Entidad FRIA:** `FriaAssessment.java`

---

## NOTAS DE IMPLEMENTACIÓN

- Ajustar nombres de campos según estructura real de entidad `FriaAssessment`
- Verificar que relación Model-FriaAssessment esté correctamente mapeada
- Considerar hacer validación también en frontend antes de iniciar despliegue
- Añadir logs inmutables cuando se detecta falta de FRIA para alto riesgo
- Considerar notificar al propietario del modelo cuando FRIA es requerido

---

**Estado:** ✅ COMPLETADO

