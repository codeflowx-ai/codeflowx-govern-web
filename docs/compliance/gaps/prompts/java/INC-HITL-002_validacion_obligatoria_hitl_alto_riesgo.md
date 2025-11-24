# PROMPT: INC-HITL-002 - Validación Obligatoria de HITL para Sistemas de Alto Riesgo

**Incidencia:** INC-HITL-002  
**Prioridad:** 🔴 CRÍTICA  
**Artículo EU AI Act:** Art. 14.1 (Human oversight), Art. 6 (Risk classification)  
**Esfuerzo Estimado:** 3-5 días  
**Tipo:** Java - Backend  
**Estado:** ✅ COMPLETADO

---

## DESCRIPCIÓN

El sistema no garantiza automáticamente que HITL esté siempre activa para sistemas clasificados como de alto riesgo. La activación de HITL depende de configuración manual y puede omitirse. Se requiere validación automática en punto de entrada (deployment, configuración).

---

## CONTEXTO

**Ubicación Actual:**
- No hay validación automática contra matriz de riesgo al desplegar
- Campo `AGTRISKASSESSMENT` es opcional (`nullable = true`)
- No hay constraint que relacione nivel de riesgo con requerimiento de HITL
- Procesos BPMN pueden ejecutarse sin validar nivel de riesgo

**Entidades Afectadas:**
- `AgentApproval` (Tabla: `AGTAGENTAPPROVALS`)
- `ModelApproval` (Tabla: `MODMODELAPPROVALS`)
- `PromptApproval` (Tabla: `PRMPROMPTAPPROVALS`)
- Procesos BPMN de despliegue

**Referencias:**
- `/docs/compliance/auditoria/AUDITORIA_SUPERVISION_HUMANA_HITL.md`
- `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md`

---

## REQUISITOS

### 1. Validación Automática en Punto de Entrada

- Validar nivel de riesgo antes de permitir despliegue
- Garantizar que HITL esté activa para sistemas de alto riesgo
- Bloquear despliegue si HITL no está configurada correctamente

### 2. Constraint de Base de Datos

- Relacionar `riskLevel = HIGH|CRITICAL` con `requiresHitl = true`
- Hacer obligatorio campo `AGTRISKASSESSMENT` para sistemas de alto riesgo

### 3. Integración en BPMN Engine

- Validar nivel de riesgo antes de ejecutar procesos
- Forzar aprobación humana para procesos de alto riesgo

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Constraint de Base de Datos (DBA)

**Archivo:** `nocode.service.entitys/src/main/resources/sql/hitl_obligatory_constraints.sql`

```sql
-- ============================================================================
-- CONSTRAINT: HITL obligatorio para alto riesgo
-- ============================================================================

-- Función para validar que HITL esté activa para alto riesgo
CREATE OR REPLACE FUNCTION validate_hitl_for_high_risk(risk_assessment JSONB)
RETURNS BOOLEAN AS $$
DECLARE
    v_risk_level VARCHAR(50);
    v_requires_hitl BOOLEAN;
BEGIN
    -- Extraer nivel de riesgo y requiresHitl del JSONB
    v_risk_level := risk_assessment->>'riskLevel';
    v_requires_hitl := COALESCE((risk_assessment->>'requiresHitl')::boolean, false);
    
    -- Si es alto riesgo, HITL debe estar activa
    IF v_risk_level IN ('HIGH', 'CRITICAL') AND v_requires_hitl = false THEN
        RETURN false;
    END IF;
    
    RETURN true;
END;
$$ LANGUAGE plpgsql;

-- CHECK constraint para AgentApproval
ALTER TABLE AGTAGENTAPPROVALS
ADD CONSTRAINT chk_hitl_high_risk_agent
CHECK (
    AGTRISKASSESSMENT IS NULL 
    OR validate_hitl_for_high_risk(AGTRISKASSESSMENT::jsonb) = true
);

-- CHECK constraint para ModelApproval
ALTER TABLE MODMODELAPPROVALS
ADD CONSTRAINT chk_hitl_high_risk_model
CHECK (
    MODRISKASSESSMENT IS NULL 
    OR validate_hitl_for_high_risk(MODRISKASSESSMENT::jsonb) = true
);

-- CHECK constraint para PromptApproval
ALTER TABLE PRMPROMPTAPPROVALS
ADD CONSTRAINT chk_hitl_high_risk_prompt
CHECK (
    PRMRISKASSESSMENT IS NULL 
    OR validate_hitl_for_high_risk(PRMRISKASSESSMENT::jsonb) = true
);
```

### 2. Servicio de Validación de Riesgo (Java)

**Archivo:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/hitl/RiskAssessmentService.java`

```java
package com.codeflowx.govern.service.hitl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class RiskAssessmentService {
    
    private static final List<String> HIGH_RISK_LEVELS = Arrays.asList("HIGH", "CRITICAL");
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Valida que HITL esté configurada correctamente según nivel de riesgo
     * @param riskAssessment JSONB con evaluación de riesgo
     * @throws IllegalStateException Si HITL no está configurada para alto riesgo
     */
    public void validateHitlForRiskLevel(String riskAssessment) {
        if (riskAssessment == null || riskAssessment.trim().isEmpty()) {
            return; // Sin evaluación de riesgo, no validar
        }
        
        try {
            JsonNode riskJson = objectMapper.readTree(riskAssessment);
            String riskLevel = riskJson.has("riskLevel") ? riskJson.get("riskLevel").asText() : null;
            boolean requiresHitl = riskJson.has("requiresHitl") && riskJson.get("requiresHitl").asBoolean();
            
            if (HIGH_RISK_LEVELS.contains(riskLevel) && !requiresHitl) {
                throw new IllegalStateException(
                    String.format("HITL es obligatorio para sistemas de alto riesgo. Nivel de riesgo: %s (EU AI Act Art. 14.1)", riskLevel));
            }
        } catch (Exception e) {
            if (e instanceof IllegalStateException) {
                throw e;
            }
            throw new IllegalArgumentException("Error al parsear evaluación de riesgo: " + e.getMessage(), e);
        }
    }
    
    /**
     * Garantiza que HITL esté activa para sistemas de alto riesgo
     * Si no está configurada, la activa automáticamente
     * @param riskAssessment JSONB con evaluación de riesgo
     * @return JSONB actualizado con requiresHitl = true si es alto riesgo
     */
    public String ensureHitlForHighRisk(String riskAssessment) {
        if (riskAssessment == null || riskAssessment.trim().isEmpty()) {
            return riskAssessment;
        }
        
        try {
            JsonNode riskJson = objectMapper.readTree(riskAssessment);
            String riskLevel = riskJson.has("riskLevel") ? riskJson.get("riskLevel").asText() : null;
            
            if (HIGH_RISK_LEVELS.contains(riskLevel)) {
                // Crear objeto mutable
                com.fasterxml.jackson.databind.node.ObjectNode mutableJson = 
                    (com.fasterxml.jackson.databind.node.ObjectNode) riskJson.deepCopy();
                mutableJson.put("requiresHitl", true);
                
                // Si no tiene hitlLevel, establecer según nivel de riesgo
                if (!mutableJson.has("hitlLevel")) {
                    if ("CRITICAL".equals(riskLevel)) {
                        mutableJson.put("hitlLevel", "COMMITTEE");
                    } else {
                        mutableJson.put("hitlLevel", "DUAL_APPROVAL");
                    }
                }
                
                return objectMapper.writeValueAsString(mutableJson);
            }
        } catch (Exception e) {
            // Si hay error, retornar original
            return riskAssessment;
        }
        
        return riskAssessment;
    }
    
    /**
     * Determina si un nivel de riesgo requiere HITL obligatorio
     */
    public boolean requiresHitl(String riskLevel) {
        return HIGH_RISK_LEVELS.contains(riskLevel);
    }
}
```

### 3. Interceptor/Listener JPA para Validación Automática

**Archivo:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/listener/HitlValidationListener.java`

```java
package com.codeflowx.govern.listener;

import com.codeflowx.govern.entity.agents.AgentApproval;
import com.codeflowx.govern.entity.models.ModelApproval;
import com.codeflowx.govern.entity.prompts.PromptApproval;
import com.codeflowx.govern.service.hitl.RiskAssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Component
public class HitlValidationListener {
    
    private static RiskAssessmentService riskAssessmentService;
    
    @Autowired
    public void setRiskAssessmentService(RiskAssessmentService service) {
        HitlValidationListener.riskAssessmentService = service;
    }
    
    @PrePersist
    @PreUpdate
    public void validateHitlBeforeSave(Object entity) {
        if (entity instanceof AgentApproval) {
            AgentApproval approval = (AgentApproval) entity;
            if (approval.getAgtriskassessment() != null) {
                // Garantizar que HITL esté activa para alto riesgo
                String updatedRisk = riskAssessmentService.ensureHitlForHighRisk(approval.getAgtriskassessment());
                approval.setAgtriskassessment(updatedRisk);
                // Validar
                riskAssessmentService.validateHitlForRiskLevel(updatedRisk);
            }
        } else if (entity instanceof ModelApproval) {
            ModelApproval approval = (ModelApproval) entity;
            if (approval.getModriskassessment() != null) {
                String updatedRisk = riskAssessmentService.ensureHitlForHighRisk(approval.getModriskassessment());
                approval.setModriskassessment(updatedRisk);
                riskAssessmentService.validateHitlForRiskLevel(updatedRisk);
            }
        } else if (entity instanceof PromptApproval) {
            PromptApproval approval = (PromptApproval) entity;
            if (approval.getPrmriskassessment() != null) {
                String updatedRisk = riskAssessmentService.ensureHitlForHighRisk(approval.getPrmriskassessment());
                approval.setPrmriskassessment(updatedRisk);
                riskAssessmentService.validateHitlForRiskLevel(updatedRisk);
            }
        }
    }
}
```

### 4. Integración en BusinessServices

**Archivo:** Modificar `AgentApprovalBusinessService.java`

```java
@Autowired
private RiskAssessmentService riskAssessmentService;

public AgentApproval createApproval(AgentApprovalDto dto) {
    AgentApproval approval = new AgentApproval();
    // ... mapeo de campos ...
    
    // Si tiene evaluación de riesgo, validar y garantizar HITL
    if (dto.getAgtriskassessment() != null) {
        String ensuredRisk = riskAssessmentService.ensureHitlForHighRisk(dto.getAgtriskassessment());
        approval.setAgtriskassessment(ensuredRisk);
        riskAssessmentService.validateHitlForRiskLevel(ensuredRisk);
    }
    
    return repository.save(approval);
}

public AgentApproval updateApproval(Long id, AgentApprovalDto dto) {
    AgentApproval existing = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Approval not found"));
    
    // Si se actualiza evaluación de riesgo, validar
    if (dto.getAgtriskassessment() != null) {
        String ensuredRisk = riskAssessmentService.ensureHitlForHighRisk(dto.getAgtriskassessment());
        existing.setAgtriskassessment(ensuredRisk);
        riskAssessmentService.validateHitlForRiskLevel(ensuredRisk);
    }
    
    // ... resto de actualización ...
    return repository.save(existing);
}
```

### 5. Integración en BPMN Engine

**Archivo:** `codeflowx.govern.workflow.engine/src/main/java/com/codeflowx/govern/workflow/validator/RiskLevelValidator.java`

```java
package com.codeflowx.govern.workflow.validator;

import com.codeflowx.govern.service.hitl.RiskAssessmentService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RiskLevelValidator implements JavaDelegate {
    
    @Autowired
    private RiskAssessmentService riskAssessmentService;
    
    @Override
    public void execute(DelegateExecution execution) {
        String riskAssessment = (String) execution.getVariable("riskAssessment");
        
        if (riskAssessment != null) {
            // Validar que HITL esté configurada
            try {
                riskAssessmentService.validateHitlForRiskLevel(riskAssessment);
            } catch (IllegalStateException e) {
                // Si no está configurada, bloquear proceso
                execution.setVariable("validationError", e.getMessage());
                throw new RuntimeException("Proceso bloqueado: " + e.getMessage(), e);
            }
        }
    }
}
```

**Uso en BPMN:**

```xml
<serviceTask id="validateRiskLevel" name="Validar Nivel de Riesgo" 
             flowable:delegateExpression="${riskLevelValidator}">
</serviceTask>

<sequenceFlow id="flow1" sourceRef="validateRiskLevel" targetRef="checkHitlRequired">
  <conditionExpression xsi:type="tFormalExpression">
    ${validationError == null}
  </conditionExpression>
</sequenceFlow>
```

---

## TESTING

### 1. Test de Constraint de Base de Datos

```sql
-- Intentar crear aprobación con alto riesgo sin HITL (debe fallar)
INSERT INTO AGTAGENTAPPROVALS (
    AGTAPPROVALTYPE, AGTAPPROVALSTATUS, AGTRISKASSESSMENT
) VALUES (
    'DEPLOYMENT', 'PENDING', 
    '{"riskLevel": "HIGH", "requiresHitl": false}'::jsonb
);
-- Debe fallar con constraint violation
```

### 2. Test de Servicio Java

```java
@Test
public void testValidateHitlForRiskLevel_AltoRiesgo_SinHITL_DeberiaRechazar() {
    // Given
    String riskAssessment = "{\"riskLevel\": \"HIGH\", \"requiresHitl\": false}";
    
    // When/Then
    assertThrows(IllegalStateException.class, () -> {
        riskAssessmentService.validateHitlForRiskLevel(riskAssessment);
    });
}

@Test
public void testEnsureHitlForHighRisk_AltoRiesgo_DeberiaActivarHITL() {
    // Given
    String riskAssessment = "{\"riskLevel\": \"HIGH\", \"requiresHitl\": false}";
    
    // When
    String result = riskAssessmentService.ensureHitlForHighRisk(riskAssessment);
    
    // Then
    JsonNode json = objectMapper.readTree(result);
    assertTrue(json.get("requiresHitl").asBoolean());
    assertEquals("DUAL_APPROVAL", json.get("hitlLevel").asText());
}
```

---

## MÉTRICAS DE ÉXITO

- **100%** de sistemas de alto riesgo con HITL activa automáticamente
- **0** despliegues permitidos sin HITL para alto riesgo
- **100%** de validaciones automáticas en punto de entrada

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md#inc-hitl-002`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_SUPERVISION_HUMANA_HITL.md`
- **EU AI Act Art. 14.1:** Human oversight
- **EU AI Act Art. 6:** Risk classification

---

**Estado:** ✅ COMPLETADO  
**Esfuerzo Estimado:** 3-5 días  
**Responsable:** Governance Team + Backend Team  
**Fecha Límite:** 2 semanas

