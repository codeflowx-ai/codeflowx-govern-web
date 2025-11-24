# PROMPT: INC-HITL-001 - Protección Multicapa Contra Desactivación de Controles Críticos

**Incidencia:** INC-HITL-001  
**Prioridad:** 🔴 CRÍTICA  
**Artículo EU AI Act:** Art. 14.1 (Human oversight)  
**Esfuerzo Estimado:** 5-7 días  
**Tipo:** Java - Backend + DBA  
**Estado:** ✅ COMPLETADO

---

## DESCRIPCIÓN

Un partner con rol `GOVERNANCE_ADMIN` puede desactivar HITL para procesos de alto riesgo sin protección suficiente. La validación solo existe a nivel de aplicación y puede ser bypassed. Se requiere implementar protección multicapa (BD, aplicación, auditoría, alertas).

---

## CONTEXTO

**Ubicación Actual:**
- Validación solo en código Java (`@NotNull`, `@NotBlank`)
- No hay triggers de PostgreSQL que validen cambios críticos
- No hay CHECK constraints en tablas de configuración
- No hay registro de intentos de modificación de controles críticos
- No hay alertas automáticas cuando se detecta intento de desactivación

**Entidades Afectadas:**
- `AgentApproval` (Tabla: `AGTAGENTAPPROVALS`)
- `ModelApproval` (Tabla: `MODMODELAPPROVALS`)
- `PromptApproval` (Tabla: `PRMPROMPTAPPROVALS`)
- Configuración de HITL (pendiente de implementación)

**Referencias:**
- `/docs/compliance/auditoria/AUDITORIA_SUPERVISION_HUMANA_HITL.md`
- `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md`

---

## REQUISITOS

### 1. Protección a Nivel de Base de Datos

**Controles Críticos que NO pueden desactivarse:**
- Supervisión Humana Obligatoria para sistemas de alto riesgo
- Aprobación de Despliegue en Producción
- Aprobación de Cambios de Modelo
- Aprobación de Modificación de Datos de Entrenamiento
- Aprobación de Cambios de Umbral de Riesgo

### 2. Validación a Nivel de Aplicación

- Reglas de negocio que impidan desactivar HITL para procesos de alto riesgo
- Validación contra matriz de riesgo antes de permitir cambios

### 3. Auditoría y Registro

- Registro de todos los intentos de modificación de controles críticos
- Tabla de auditoría de cambios de configuración de HITL

### 4. Alertas Automáticas

- Notificaciones automáticas a CISO/Compliance cuando se detecta intento
- Bloqueo automático de cambios no autorizados

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear Tabla de Auditoría de Cambios HITL (DBA)

**Archivo:** `nocode.service.entitys/src/main/resources/sql/hitl_audit_changes.sql`

```sql
-- ============================================================================
-- TABLA DE AUDITORÍA DE CAMBIOS DE CONTROLES CRÍTICOS HITL
-- ============================================================================
CREATE TABLE GOVAUDITHITLCHANGES (
    IDXAUDITHITLCHANGE BIGSERIAL PRIMARY KEY,
    iduuid VARCHAR(36) UNIQUE NOT NULL DEFAULT gen_random_uuid()::text,
    GOVENTITYTYPE VARCHAR(50) NOT NULL, -- AGENT, MODEL, PROMPT, CONFIG
    GOVENTITYID BIGINT,
    GOVCHANGETYPE VARCHAR(50) NOT NULL, -- DISABLE_HITL, MODIFY_APPROVAL_LEVEL, CHANGE_RISK_THRESHOLD
    GOVCHANGEDFIELD VARCHAR(100) NOT NULL, -- Campo que se intentó modificar
    GOVOLDVALUE TEXT, -- Valor anterior (JSONB serializado)
    GOVNEWVALUE TEXT, -- Valor nuevo intentado (JSONB serializado)
    GOVRISKLEVEL VARCHAR(50), -- HIGH, CRITICAL, MEDIUM, LOW
    GOVCHANGESTATUS VARCHAR(50) NOT NULL, -- ALLOWED, REJECTED, BLOCKED
    GOVREJECTIONREASON TEXT, -- Razón de rechazo si aplica
    GOVUSERID BIGINT NOT NULL, -- Usuario que intentó el cambio
    GOVUSERNAME VARCHAR(255),
    GOVUSERROLE VARCHAR(100),
    GOVSOURCEIP VARCHAR(45), -- IP de origen
    GOVUSERAGENT TEXT, -- User agent
    GOVCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_entity_type CHECK (GOVENTITYTYPE IN ('AGENT', 'MODEL', 'PROMPT', 'CONFIG')),
    CONSTRAINT chk_change_type CHECK (GOVCHANGETYPE IN ('DISABLE_HITL', 'MODIFY_APPROVAL_LEVEL', 'CHANGE_RISK_THRESHOLD', 'ENABLE_AUTO_APPROVAL', 'OTHER')),
    CONSTRAINT chk_change_status CHECK (GOVCHANGESTATUS IN ('ALLOWED', 'REJECTED', 'BLOCKED'))
);

-- Índices para búsqueda eficiente
CREATE INDEX idx_gov_audit_hitl_entity ON GOVAUDITHITLCHANGES(GOVENTITYTYPE, GOVENTITYID);
CREATE INDEX idx_gov_audit_hitl_user ON GOVAUDITHITLCHANGES(GOVUSERID, GOVCREATEDAT DESC);
CREATE INDEX idx_gov_audit_hitl_status ON GOVAUDITHITLCHANGES(GOVCHANGESTATUS, GOVCREATEDAT DESC);
CREATE INDEX idx_gov_audit_hitl_risk ON GOVAUDITHITLCHANGES(GOVRISKLEVEL, GOVCREATEDAT DESC);
CREATE INDEX idx_gov_audit_hitl_type ON GOVAUDITHITLCHANGES(GOVCHANGETYPE, GOVCREATEDAT DESC);
```

### 2. Triggers de PostgreSQL para Protección (DBA)

**Archivo:** `nocode.service.entitys/src/main/resources/sql/hitl_protection_triggers.sql`

```sql
-- ============================================================================
-- FUNCIÓN: Validar que HITL no se desactive para alto riesgo
-- ============================================================================
CREATE OR REPLACE FUNCTION validate_hitl_critical_control()
RETURNS TRIGGER AS $$
DECLARE
    v_risk_level VARCHAR(50);
    v_old_requires_hitl BOOLEAN;
    v_new_requires_hitl BOOLEAN;
    v_old_approval_level VARCHAR(50);
    v_new_approval_level VARCHAR(50);
BEGIN
    -- Extraer nivel de riesgo del JSONB
    IF TG_TABLE_NAME = 'AGTAGENTAPPROVALS' THEN
        v_risk_level := COALESCE(NEW.AGTRISKASSESSMENT::jsonb->>'riskLevel', OLD.AGTRISKASSESSMENT::jsonb->>'riskLevel');
        v_old_requires_hitl := COALESCE((OLD.AGTRISKASSESSMENT::jsonb->>'requiresHitl')::boolean, true);
        v_new_requires_hitl := COALESCE((NEW.AGTRISKASSESSMENT::jsonb->>'requiresHitl')::boolean, true);
        v_old_approval_level := OLD.AGTAPPROVALLEVEL;
        v_new_approval_level := NEW.AGTAPPROVALLEVEL;
    ELSIF TG_TABLE_NAME = 'MODMODELAPPROVALS' THEN
        v_risk_level := COALESCE(NEW.MODRISKASSESSMENT::jsonb->>'riskLevel', OLD.MODRISKASSESSMENT::jsonb->>'riskLevel');
        v_old_requires_hitl := COALESCE((OLD.MODRISKASSESSMENT::jsonb->>'requiresHitl')::boolean, true);
        v_new_requires_hitl := COALESCE((NEW.MODRISKASSESSMENT::jsonb->>'requiresHitl')::boolean, true);
    ELSIF TG_TABLE_NAME = 'PRMPROMPTAPPROVALS' THEN
        v_risk_level := COALESCE(NEW.PRMRISKASSESSMENT::jsonb->>'riskLevel', OLD.PRMRISKASSESSMENT::jsonb->>'riskLevel');
        v_old_requires_hitl := COALESCE((OLD.PRMRISKASSESSMENT::jsonb->>'requiresHitl')::boolean, true);
        v_new_requires_hitl := COALESCE((NEW.PRMRISKASSESSMENT::jsonb->>'requiresHitl')::boolean, true);
    END IF;

    -- Si es alto riesgo (HIGH o CRITICAL) y se intenta desactivar HITL, BLOQUEAR
    IF (v_risk_level IN ('HIGH', 'CRITICAL')) AND v_old_requires_hitl = true AND v_new_requires_hitl = false THEN
        -- Registrar intento bloqueado
        INSERT INTO GOVAUDITHITLCHANGES (
            GOVENTITYTYPE, GOVENTITYID, GOVCHANGETYPE, GOVCHANGEDFIELD,
            GOVOLDVALUE, GOVNEWVALUE, GOVRISKLEVEL, GOVCHANGESTATUS,
            GOVREJECTIONREASON, GOVUSERID, GOVUSERNAME, GOVUSERROLE
        ) VALUES (
            CASE TG_TABLE_NAME
                WHEN 'AGTAGENTAPPROVALS' THEN 'AGENT'
                WHEN 'MODMODELAPPROVALS' THEN 'MODEL'
                WHEN 'PRMPROMPTAPPROVALS' THEN 'PROMPT'
            END,
            NEW.IDXAGENTAPPROVAL,
            'DISABLE_HITL',
            'requiresHitl',
            OLD.AGTRISKASSESSMENT::text,
            NEW.AGTRISKASSESSMENT::text,
            v_risk_level,
            'BLOCKED',
            'No se puede desactivar HITL para sistemas de alto riesgo según EU AI Act Art. 14.1',
            COALESCE(NEW.AGTUPDATEDBY, NEW.AGTCREATEDBY),
            COALESCE(NEW.AGTAPPROVERNAME, 'SYSTEM'),
            'GOVERNANCE_ADMIN'
        );
        
        RAISE EXCEPTION 'No se puede desactivar HITL para sistemas de alto riesgo (EU AI Act Art. 14.1). Nivel de riesgo: %', v_risk_level;
    END IF;

    -- Si es alto riesgo y se intenta cambiar a aprobación automática, BLOQUEAR
    IF (v_risk_level IN ('HIGH', 'CRITICAL')) AND 
       v_old_approval_level IS NOT NULL AND 
       v_new_approval_level = 'AUTOMATIC' THEN
        INSERT INTO GOVAUDITHITLCHANGES (
            GOVENTITYTYPE, GOVENTITYID, GOVCHANGETYPE, GOVCHANGEDFIELD,
            GOVOLDVALUE, GOVNEWVALUE, GOVRISKLEVEL, GOVCHANGESTATUS,
            GOVREJECTIONREASON, GOVUSERID, GOVUSERNAME, GOVUSERROLE
        ) VALUES (
            CASE TG_TABLE_NAME
                WHEN 'AGTAGENTAPPROVALS' THEN 'AGENT'
                WHEN 'MODMODELAPPROVALS' THEN 'MODEL'
                WHEN 'PRMPROMPTAPPROVALS' THEN 'PROMPT'
            END,
            NEW.IDXAGENTAPPROVAL,
            'MODIFY_APPROVAL_LEVEL',
            'approvalLevel',
            v_old_approval_level,
            v_new_approval_level,
            v_risk_level,
            'BLOCKED',
            'No se puede cambiar a aprobación automática para sistemas de alto riesgo',
            COALESCE(NEW.AGTUPDATEDBY, NEW.AGTCREATEDBY),
            COALESCE(NEW.AGTAPPROVERNAME, 'SYSTEM'),
            'GOVERNANCE_ADMIN'
        );
        
        RAISE EXCEPTION 'No se puede cambiar a aprobación automática para sistemas de alto riesgo. Nivel de riesgo: %', v_risk_level;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Aplicar trigger a tablas de aprobación
CREATE TRIGGER trg_validate_hitl_agent_approval
    BEFORE UPDATE ON AGTAGENTAPPROVALS
    FOR EACH ROW
    WHEN (OLD.AGTRISKASSESSMENT IS DISTINCT FROM NEW.AGTRISKASSESSMENT 
          OR OLD.AGTAPPROVALLEVEL IS DISTINCT FROM NEW.AGTAPPROVALLEVEL)
    EXECUTE FUNCTION validate_hitl_critical_control();

CREATE TRIGGER trg_validate_hitl_model_approval
    BEFORE UPDATE ON MODMODELAPPROVALS
    FOR EACH ROW
    WHEN (OLD.MODRISKASSESSMENT IS DISTINCT FROM NEW.MODRISKASSESSMENT)
    EXECUTE FUNCTION validate_hitl_critical_control();

CREATE TRIGGER trg_validate_hitl_prompt_approval
    BEFORE UPDATE ON PRMPROMPTAPPROVALS
    FOR EACH ROW
    WHEN (OLD.PRMRISKASSESSMENT IS DISTINCT FROM NEW.PRMRISKASSESSMENT)
    EXECUTE FUNCTION validate_hitl_critical_control();
```

### 3. Entidad Java para Auditoría

**Archivo:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/audit/HitlAuditChange.java`

```java
package com.codeflowx.govern.entity.audit;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "GOVAUDITHITLCHANGES")
@Entidad(
    namespace = "governance",
    type = "TABLE",
    name = "GOVAUDITHITLCHANGES",
    labelMonitor = "GOVCHANGETYPE",
    pk = "IDXAUDITHITLCHANGE"
)
public class HitlAuditChange implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXAUDITHITLCHANGE")
    @Field(criteria = true, auditar = false, filter = true, label = "ID", type = "LONG")
    private Long idxaudithitlchange;
    
    @NotNull
    @NotBlank
    @Column(name = "GOVENTITYTYPE", nullable = false, length = 50)
    @Field(criteria = true, auditar = false, filter = true, label = "Entity Type", type = "LIST_STRING")
    private String goventitytype; // AGENT, MODEL, PROMPT, CONFIG
    
    @Column(name = "GOVENTITYID")
    @Field(criteria = true, auditar = false, filter = true, label = "Entity ID", type = "LONG")
    private Long goventityid;
    
    @NotNull
    @NotBlank
    @Column(name = "GOVCHANGETYPE", nullable = false, length = 50)
    @Field(criteria = true, auditar = false, filter = true, label = "Change Type", type = "LIST_STRING")
    private String govchangetype; // DISABLE_HITL, MODIFY_APPROVAL_LEVEL, etc.
    
    @NotNull
    @NotBlank
    @Column(name = "GOVCHANGEDFIELD", nullable = false, length = 100)
    @Field(criteria = true, auditar = false, filter = true, label = "Changed Field", type = "STRING")
    private String govchangedfield;
    
    @Column(name = "GOVOLDVALUE", columnDefinition = "TEXT")
    @Field(criteria = true, auditar = false, filter = true, label = "Old Value", type = "CLOB")
    private String govoldvalue;
    
    @Column(name = "GOVNEWVALUE", columnDefinition = "TEXT")
    @Field(criteria = true, auditar = false, filter = true, label = "New Value", type = "CLOB")
    private String govnewvalue;
    
    @Column(name = "GOVRISKLEVEL", length = 50)
    @Field(criteria = true, auditar = false, filter = true, label = "Risk Level", type = "LIST_STRING")
    private String govrisklevel; // HIGH, CRITICAL, MEDIUM, LOW
    
    @NotNull
    @NotBlank
    @Column(name = "GOVCHANGESTATUS", nullable = false, length = 50)
    @Field(criteria = true, auditar = false, filter = true, label = "Change Status", type = "LIST_STRING")
    private String govchangestatus; // ALLOWED, REJECTED, BLOCKED
    
    @Column(name = "GOVREJECTIONREASON", columnDefinition = "TEXT")
    @Field(criteria = true, auditar = false, filter = true, label = "Rejection Reason", type = "CLOB")
    private String govrejectionreason;
    
    @NotNull
    @Column(name = "GOVUSERID", nullable = false)
    @Field(criteria = true, auditar = false, filter = true, label = "User ID", type = "LONG")
    private Long govuserid;
    
    @Column(name = "GOVUSERNAME", length = 255)
    @Field(criteria = true, auditar = false, filter = true, label = "Username", type = "STRING")
    private String govusername;
    
    @Column(name = "GOVUSERROLE", length = 100)
    @Field(criteria = true, auditar = false, filter = true, label = "User Role", type = "STRING")
    private String govuserrole;
    
    @Column(name = "GOVSOURCEIP", length = 45)
    @Field(criteria = true, auditar = false, filter = true, label = "Source IP", type = "STRING")
    private String govsourceip;
    
    @Column(name = "GOVUSERAGENT", columnDefinition = "TEXT")
    @Field(criteria = true, auditar = false, filter = true, label = "User Agent", type = "CLOB")
    private String govuseragent;
    
    @NotNull
    @Column(name = "GOVCREATEDAT", nullable = false, updatable = false)
    @Field(criteria = true, auditar = false, filter = true, label = "Created At", type = "TIMESTAMP")
    private Timestamp govcreatedat = new Timestamp(System.currentTimeMillis());
}
```

### 4. Servicio de Validación y Auditoría (Java)

**Archivo:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/hitl/HitlProtectionService.java`

```java
package com.codeflowx.govern.service.hitl;

import com.codeflowx.govern.entity.audit.HitlAuditChange;
import com.codeflowx.govern.repository.audit.HitlAuditChangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@Service
public class HitlProtectionService {
    
    @Autowired
    private HitlAuditChangeRepository auditRepository;
    
    /**
     * Valida si se puede modificar configuración de HITL según nivel de riesgo
     * @param riskLevel Nivel de riesgo (HIGH, CRITICAL, MEDIUM, LOW)
     * @param oldRequiresHitl Valor anterior de requiresHitl
     * @param newRequiresHitl Valor nuevo de requiresHitl
     * @param oldApprovalLevel Nivel de aprobación anterior
     * @param newApprovalLevel Nivel de aprobación nuevo
     * @throws IllegalStateException Si el cambio no está permitido
     */
    @Transactional
    public void validateHitlChange(
            String riskLevel,
            Boolean oldRequiresHitl,
            Boolean newRequiresHitl,
            String oldApprovalLevel,
            String newApprovalLevel,
            String entityType,
            Long entityId,
            Long userId,
            String username,
            String userRole,
            HttpServletRequest request) {
        
        // Si es alto riesgo y se intenta desactivar HITL, rechazar
        if ((riskLevel != null && (riskLevel.equals("HIGH") || riskLevel.equals("CRITICAL"))) 
            && Boolean.TRUE.equals(oldRequiresHitl) 
            && Boolean.FALSE.equals(newRequiresHitl)) {
            
            recordAuditChange(
                entityType, entityId, "DISABLE_HITL", "requiresHitl",
                String.valueOf(oldRequiresHitl), String.valueOf(newRequiresHitl),
                riskLevel, "REJECTED",
                "No se puede desactivar HITL para sistemas de alto riesgo según EU AI Act Art. 14.1",
                userId, username, userRole, request);
            
            throw new IllegalStateException(
                "No se puede desactivar HITL para sistemas de alto riesgo. Nivel de riesgo: " + riskLevel);
        }
        
        // Si es alto riesgo y se intenta cambiar a aprobación automática, rechazar
        if ((riskLevel != null && (riskLevel.equals("HIGH") || riskLevel.equals("CRITICAL")))
            && oldApprovalLevel != null
            && "AUTOMATIC".equals(newApprovalLevel)) {
            
            recordAuditChange(
                entityType, entityId, "MODIFY_APPROVAL_LEVEL", "approvalLevel",
                oldApprovalLevel, newApprovalLevel,
                riskLevel, "REJECTED",
                "No se puede cambiar a aprobación automática para sistemas de alto riesgo",
                userId, username, userRole, request);
            
            throw new IllegalStateException(
                "No se puede cambiar a aprobación automática para sistemas de alto riesgo. Nivel de riesgo: " + riskLevel);
        }
        
        // Si el cambio está permitido, registrar como ALLOWED
        if (oldRequiresHitl != newRequiresHitl || 
            (oldApprovalLevel != null && !oldApprovalLevel.equals(newApprovalLevel))) {
            recordAuditChange(
                entityType, entityId, "MODIFY_APPROVAL_LEVEL", 
                oldRequiresHitl != newRequiresHitl ? "requiresHitl" : "approvalLevel",
                oldRequiresHitl != null ? String.valueOf(oldRequiresHitl) : oldApprovalLevel,
                newRequiresHitl != null ? String.valueOf(newRequiresHitl) : newApprovalLevel,
                riskLevel, "ALLOWED", null,
                userId, username, userRole, request);
        }
    }
    
    private void recordAuditChange(
            String entityType, Long entityId, String changeType, String changedField,
            String oldValue, String newValue, String riskLevel, String status,
            String rejectionReason, Long userId, String username, String userRole,
            HttpServletRequest request) {
        
        HitlAuditChange audit = new HitlAuditChange();
        audit.setGoventitytype(entityType);
        audit.setGoventityid(entityId);
        audit.setGovchangetype(changeType);
        audit.setGovchangedfield(changedField);
        audit.setGovoldvalue(oldValue);
        audit.setGovnewvalue(newValue);
        audit.setGovrisklevel(riskLevel);
        audit.setGovchangestatus(status);
        audit.setGovrejectionreason(rejectionReason);
        audit.setGovuserid(userId);
        audit.setGovusername(username);
        audit.setGovuserrole(userRole);
        
        if (request != null) {
            audit.setGovsourceip(getClientIpAddress(request));
            audit.setGovuseragent(request.getHeader("User-Agent"));
        }
        
        audit.setGovcreatedat(new Timestamp(System.currentTimeMillis()));
        
        auditRepository.save(audit);
        
        // Si es bloqueado o rechazado, enviar alerta
        if ("BLOCKED".equals(status) || "REJECTED".equals(status)) {
            sendAlert(audit);
        }
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
    
    private void sendAlert(HitlAuditChange audit) {
        // TODO: Implementar envío de alerta a CISO/Compliance
        // Usar servicio de notificaciones (email, Slack, etc.)
        System.out.println("ALERTA: Intento de modificación de control crítico HITL bloqueado/rechazado: " + audit.getGovchangetype());
    }
}
```

### 5. Integración en BusinessServices

**Archivo:** Modificar `AgentApprovalBusinessService.java` (ejemplo)

```java
@Autowired
private HitlProtectionService hitlProtectionService;

public AgentApproval updateApproval(Long id, AgentApprovalDto dto, HttpServletRequest request) {
    AgentApproval existing = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Approval not found"));
    
    // Extraer nivel de riesgo del JSONB
    String riskLevel = extractRiskLevel(dto.getAgtriskassessment());
    Boolean oldRequiresHitl = extractRequiresHitl(existing.getAgtriskassessment());
    Boolean newRequiresHitl = extractRequiresHitl(dto.getAgtriskassessment());
    
    // Validar cambio antes de aplicar
    hitlProtectionService.validateHitlChange(
        riskLevel,
        oldRequiresHitl,
        newRequiresHitl,
        existing.getAgtapprovallevel(),
        dto.getAgtapprovallevel(),
        "AGENT",
        id,
        getCurrentUserId(),
        getCurrentUsername(),
        getCurrentUserRole(),
        request
    );
    
    // Si pasa validación, aplicar cambios
    // ... resto del código de actualización
}
```

---

## TESTING

### 1. Test de Trigger de Base de Datos

```sql
-- Intentar desactivar HITL para sistema de alto riesgo (debe fallar)
UPDATE AGTAGENTAPPROVALS
SET AGTRISKASSESSMENT = '{"riskLevel": "HIGH", "requiresHitl": false}'::jsonb
WHERE IDXAGENTAPPROVAL = 1;

-- Verificar que se registró el intento bloqueado
SELECT * FROM GOVAUDITHITLCHANGES 
WHERE GOVCHANGESTATUS = 'BLOCKED' 
ORDER BY GOVCREATEDAT DESC;
```

### 2. Test de Servicio Java

```java
@Test
public void testValidateHitlChange_AltoRiesgo_DesactivarHITL_DeberiaRechazar() {
    // Given
    String riskLevel = "HIGH";
    Boolean oldRequiresHitl = true;
    Boolean newRequiresHitl = false;
    
    // When/Then
    assertThrows(IllegalStateException.class, () -> {
        hitlProtectionService.validateHitlChange(
            riskLevel, oldRequiresHitl, newRequiresHitl,
            null, null, "AGENT", 1L, 1L, "test", "GOVERNANCE_ADMIN", null);
    });
}
```

---

## MÉTRICAS DE ÉXITO

- **0** intentos exitosos de desactivación de controles críticos
- **100%** de intentos bloqueados registrados en auditoría
- **100%** de alertas enviadas para intentos bloqueados
- Tiempo de respuesta de validación < 100ms

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md#inc-hitl-001`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_SUPERVISION_HUMANA_HITL.md`
- **EU AI Act Art. 14.1:** Human oversight

---

**Estado:** ✅ COMPLETADO  
**Esfuerzo Estimado:** 5-7 días  
**Responsable:** Governance Team + DBA + Backend Team  
**Fecha Límite:** 2 semanas

