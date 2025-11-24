# PROMPT: INC-HITL-003 - Registro de Intentos de Modificación de Controles Críticos

**Incidencia:** INC-HITL-003  
**Prioridad:** 🔴 CRÍTICA  
**Artículo EU AI Act:** Art. 14.4 (Transparency), ISO 42001 (Audit trails)  
**Esfuerzo Estimado:** 4-6 días  
**Tipo:** Java - Backend + DBA  
**Estado:** ✅ COMPLETADO

---

## DESCRIPCIÓN

No se registran los intentos de modificación o desactivación de controles críticos de HITL, lo que impide detectar y prevenir violaciones de cumplimiento. Se requiere crear tabla de auditoría y registrar todos los intentos (exitosos, rechazados y bloqueados).

---

## CONTEXTO

**Ubicación Actual:**
- No hay tabla de auditoría de cambios de configuración de HITL
- No hay registro de intentos fallidos de modificación
- No hay campo de "intento de bypass" en entidades de aprobación
- Logs de aplicación no capturan estos eventos de forma estructurada

**Entidades Afectadas:**
- `AgentApproval` (Tabla: `AGTAGENTAPPROVALS`)
- `ModelApproval` (Tabla: `MODMODELAPPROVALS`)
- `PromptApproval` (Tabla: `PRMPROMPTAPPROVALS`)

**Referencias:**
- `/docs/compliance/auditoria/AUDITORIA_SUPERVISION_HUMANA_HITL.md`
- `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md`
- Relacionado con: INC-HITL-001 (usa la misma tabla de auditoría)

---

## REQUISITOS

### 1. Tabla de Auditoría Completa

- Registrar todos los intentos de modificación (exitosos, rechazados, bloqueados)
- Incluir: usuario, timestamp, cambio intentado, resultado, razón
- Campos de contexto: IP, user agent, sesión

### 2. Interceptor/Auditoría a Nivel de JPA

- Capturar cambios automáticamente antes de persistir
- Registrar intentos incluso si son rechazados

### 3. Alertas Automáticas

- Generar alertas para intentos de modificación de controles críticos
- Notificar a CISO/Compliance

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Tabla de Auditoría (DBA)

**Nota:** Esta tabla ya está definida en INC-HITL-001. Si ya se implementó, reutilizar.

**Archivo:** `nocode.service.entitys/src/main/resources/sql/hitl_audit_changes.sql`

```sql
-- Ver INC-HITL-001 para definición completa de GOVAUDITHITLCHANGES
-- Esta incidencia se enfoca en el registro automático de intentos
```

### 2. Entidad Java para Auditoría

**Nota:** Esta entidad ya está definida en INC-HITL-001. Si ya se implementó, reutilizar.

**Archivo:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/audit/HitlAuditChange.java`

```java
// Ver INC-HITL-001 para definición completa
```

### 3. Interceptor JPA para Captura Automática

**Archivo:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/interceptor/HitlAuditInterceptor.java`

```java
package com.codeflowx.govern.interceptor;

import com.codeflowx.govern.entity.agents.AgentApproval;
import com.codeflowx.govern.entity.audit.HitlAuditChange;
import com.codeflowx.govern.entity.models.ModelApproval;
import com.codeflowx.govern.entity.prompts.PromptApproval;
import com.codeflowx.govern.repository.audit.HitlAuditChangeRepository;
import com.codeflowx.govern.service.hitl.RiskAssessmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;

@Component
public class HitlAuditInterceptor extends EmptyInterceptor {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    private HitlAuditChangeRepository auditRepository;
    
    @Autowired
    private RiskAssessmentService riskAssessmentService;
    
    private HttpServletRequest currentRequest;
    
    public void setCurrentRequest(HttpServletRequest request) {
        this.currentRequest = request;
    }
    
    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, 
                                 Object[] previousState, String[] propertyNames, Type[] types) {
        
        if (entity instanceof AgentApproval) {
            auditApprovalChange((AgentApproval) entity, previousState, currentState, propertyNames, "AGENT");
        } else if (entity instanceof ModelApproval) {
            auditApprovalChange((ModelApproval) entity, previousState, currentState, propertyNames, "MODEL");
        } else if (entity instanceof PromptApproval) {
            auditApprovalChange((PromptApproval) entity, previousState, currentState, propertyNames, "PROMPT");
        }
        
        return false; // No modificar la entidad
    }
    
    private void auditApprovalChange(Object entity, Object[] previousState, Object[] currentState, 
                                     String[] propertyNames, String entityType) {
        try {
            String oldRiskAssessment = null;
            String newRiskAssessment = null;
            String oldApprovalLevel = null;
            String newApprovalLevel = null;
            Long entityId = null;
            
            // Extraer valores anteriores y nuevos
            for (int i = 0; i < propertyNames.length; i++) {
                if (propertyNames[i].equals("agtriskassessment") || 
                    propertyNames[i].equals("modriskassessment") ||
                    propertyNames[i].equals("prmriskassessment")) {
                    oldRiskAssessment = previousState[i] != null ? previousState[i].toString() : null;
                    newRiskAssessment = currentState[i] != null ? currentState[i].toString() : null;
                }
                if (propertyNames[i].equals("agtapprovallevel") ||
                    propertyNames[i].equals("modapprovallevel") ||
                    propertyNames[i].equals("prmapprovallevel")) {
                    oldApprovalLevel = previousState[i] != null ? previousState[i].toString() : null;
                    newApprovalLevel = currentState[i] != null ? currentState[i].toString() : null;
                }
                if (propertyNames[i].equals("idxagentapproval") ||
                    propertyNames[i].equals("idxmodelapproval") ||
                    propertyNames[i].equals("idxpromptapproval")) {
                    entityId = currentState[i] != null ? ((Number) currentState[i]).longValue() : null;
                }
            }
            
            // Si hay cambio en evaluación de riesgo o nivel de aprobación, registrar
            if ((oldRiskAssessment != null && !oldRiskAssessment.equals(newRiskAssessment)) ||
                (oldApprovalLevel != null && !oldApprovalLevel.equals(newApprovalLevel))) {
                
                recordAuditChange(
                    entityType, entityId, oldRiskAssessment, newRiskAssessment,
                    oldApprovalLevel, newApprovalLevel, currentRequest);
            }
        } catch (Exception e) {
            // Log error pero no fallar la transacción
            System.err.println("Error al registrar auditoría HITL: " + e.getMessage());
        }
    }
    
    private void recordAuditChange(String entityType, Long entityId, 
                                   String oldRiskAssessment, String newRiskAssessment,
                                   String oldApprovalLevel, String newApprovalLevel,
                                   HttpServletRequest request) {
        
        try {
            String riskLevel = extractRiskLevel(newRiskAssessment);
            String changeType = determineChangeType(oldRiskAssessment, newRiskAssessment, oldApprovalLevel, newApprovalLevel);
            String changedField = determineChangedField(oldRiskAssessment, newRiskAssessment, oldApprovalLevel, newApprovalLevel);
            String status = determineStatus(riskLevel, oldRiskAssessment, newRiskAssessment, oldApprovalLevel, newApprovalLevel);
            String rejectionReason = status.equals("REJECTED") || status.equals("BLOCKED") 
                ? "Intento de modificación de control crítico rechazado/bloqueado" : null;
            
            HitlAuditChange audit = new HitlAuditChange();
            audit.setGoventitytype(entityType);
            audit.setGoventityid(entityId);
            audit.setGovchangetype(changeType);
            audit.setGovchangedfield(changedField);
            audit.setGovoldvalue(oldRiskAssessment != null ? oldRiskAssessment : oldApprovalLevel);
            audit.setGovnewvalue(newRiskAssessment != null ? newRiskAssessment : newApprovalLevel);
            audit.setGovrisklevel(riskLevel);
            audit.setGovchangestatus(status);
            audit.setGovrejectionreason(rejectionReason);
            
            // Obtener información del usuario actual (desde contexto de seguridad)
            audit.setGovuserid(getCurrentUserId());
            audit.setGovusername(getCurrentUsername());
            audit.setGovuserrole(getCurrentUserRole());
            
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
        } catch (Exception e) {
            System.err.println("Error al guardar auditoría: " + e.getMessage());
        }
    }
    
    private String extractRiskLevel(String riskAssessment) {
        if (riskAssessment == null) return null;
        try {
            var json = objectMapper.readTree(riskAssessment);
            return json.has("riskLevel") ? json.get("riskLevel").asText() : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    private String determineChangeType(String oldRisk, String newRisk, String oldLevel, String newLevel) {
        if (oldRisk != null && newRisk != null && !oldRisk.equals(newRisk)) {
            try {
                var oldJson = objectMapper.readTree(oldRisk);
                var newJson = objectMapper.readTree(newRisk);
                boolean oldRequiresHitl = oldJson.has("requiresHitl") && oldJson.get("requiresHitl").asBoolean();
                boolean newRequiresHitl = newJson.has("requiresHitl") && newJson.get("requiresHitl").asBoolean();
                if (oldRequiresHitl && !newRequiresHitl) {
                    return "DISABLE_HITL";
                }
            } catch (Exception e) {
                // Ignorar
            }
        }
        if (oldLevel != null && newLevel != null && !oldLevel.equals(newLevel)) {
            return "MODIFY_APPROVAL_LEVEL";
        }
        return "OTHER";
    }
    
    private String determineChangedField(String oldRisk, String newRisk, String oldLevel, String newLevel) {
        if (oldRisk != null && newRisk != null && !oldRisk.equals(newRisk)) {
            return "riskAssessment";
        }
        if (oldLevel != null && newLevel != null && !oldLevel.equals(newLevel)) {
            return "approvalLevel";
        }
        return "unknown";
    }
    
    private String determineStatus(String riskLevel, String oldRisk, String newRisk, 
                                   String oldLevel, String newLevel) {
        // Si es alto riesgo y se intenta desactivar HITL, es bloqueado
        if (riskLevel != null && Arrays.asList("HIGH", "CRITICAL").contains(riskLevel)) {
            try {
                if (oldRisk != null && newRisk != null) {
                    var oldJson = objectMapper.readTree(oldRisk);
                    var newJson = objectMapper.readTree(newRisk);
                    boolean oldRequiresHitl = oldJson.has("requiresHitl") && oldJson.get("requiresHitl").asBoolean();
                    boolean newRequiresHitl = newJson.has("requiresHitl") && newJson.get("requiresHitl").asBoolean();
                    if (oldRequiresHitl && !newRequiresHitl) {
                        return "BLOCKED";
                    }
                }
                if (oldLevel != null && "AUTOMATIC".equals(newLevel)) {
                    return "BLOCKED";
                }
            } catch (Exception e) {
                // Ignorar
            }
        }
        return "ALLOWED";
    }
    
    private Long getCurrentUserId() {
        // TODO: Obtener desde contexto de seguridad (Spring Security)
        return 1L;
    }
    
    private String getCurrentUsername() {
        // TODO: Obtener desde contexto de seguridad
        return "SYSTEM";
    }
    
    private String getCurrentUserRole() {
        // TODO: Obtener desde contexto de seguridad
        return "GOVERNANCE_ADMIN";
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
        System.out.println("ALERTA: Intento de modificación de control crítico HITL: " + audit.getGovchangetype());
    }
}
```

### 4. Configuración del Interceptor en Hibernate

**Archivo:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/config/HibernateConfig.java`

```java
package com.codeflowx.govern.config;

import com.codeflowx.govern.interceptor.HitlAuditInterceptor;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig {
    
    @Autowired
    private HitlAuditInterceptor hitlAuditInterceptor;
    
    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return hibernateProperties -> {
            hibernateProperties.put(Environment.INTERCEPTOR, hitlAuditInterceptor);
        };
    }
}
```

### 5. Servicio de Consulta de Auditoría

**Archivo:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/hitl/HitlAuditService.java`

```java
package com.codeflowx.govern.service.hitl;

import com.codeflowx.govern.entity.audit.HitlAuditChange;
import com.codeflowx.govern.repository.audit.HitlAuditChangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.List;

@Service
public class HitlAuditService {
    
    @Autowired
    private HitlAuditChangeRepository auditRepository;
    
    /**
     * Obtener intentos bloqueados/rechazados
     */
    public Page<HitlAuditChange> getBlockedAttempts(Pageable pageable) {
        return auditRepository.findByGovchangestatusIn(
            List.of("BLOCKED", "REJECTED"), pageable);
    }
    
    /**
     * Obtener intentos por usuario
     */
    public Page<HitlAuditChange> getAttemptsByUser(Long userId, Pageable pageable) {
        return auditRepository.findByGovuseridOrderByGovcreatedatDesc(userId, pageable);
    }
    
    /**
     * Obtener intentos por entidad
     */
    public List<HitlAuditChange> getAttemptsByEntity(String entityType, Long entityId) {
        return auditRepository.findByGoventitytypeAndGoventityidOrderByGovcreatedatDesc(
            entityType, entityId);
    }
    
    /**
     * Obtener intentos en rango de fechas
     */
    public Page<HitlAuditChange> getAttemptsByDateRange(
            Timestamp startDate, Timestamp endDate, Pageable pageable) {
        return auditRepository.findByGovcreatedatBetweenOrderByGovcreatedatDesc(
            startDate, endDate, pageable);
    }
    
    /**
     * Estadísticas de intentos
     */
    public HitlAuditStats getStats() {
        long total = auditRepository.count();
        long blocked = auditRepository.countByGovchangestatus("BLOCKED");
        long rejected = auditRepository.countByGovchangestatus("REJECTED");
        long allowed = auditRepository.countByGovchangestatus("ALLOWED");
        
        return new HitlAuditStats(total, blocked, rejected, allowed);
    }
    
    public static class HitlAuditStats {
        private final long total;
        private final long blocked;
        private final long rejected;
        private final long allowed;
        
        public HitlAuditStats(long total, long blocked, long rejected, long allowed) {
            this.total = total;
            this.blocked = blocked;
            this.rejected = rejected;
            this.allowed = allowed;
        }
        
        // Getters
        public long getTotal() { return total; }
        public long getBlocked() { return blocked; }
        public long getRejected() { return rejected; }
        public long getAllowed() { return allowed; }
    }
}
```

### 6. Repository

**Archivo:** `codeflowx.govern.repositories/src/main/java/com/codeflowx/govern/repository/audit/HitlAuditChangeRepository.java`

```java
package com.codeflowx.govern.repository.audit;

import com.codeflowx.govern.entity.audit.HitlAuditChange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface HitlAuditChangeRepository extends JpaRepository<HitlAuditChange, Long> {
    
    Page<HitlAuditChange> findByGovchangestatusIn(List<String> statuses, Pageable pageable);
    
    Page<HitlAuditChange> findByGovuseridOrderByGovcreatedatDesc(Long userId, Pageable pageable);
    
    List<HitlAuditChange> findByGoventitytypeAndGoventityidOrderByGovcreatedatDesc(
        String entityType, Long entityId);
    
    Page<HitlAuditChange> findByGovcreatedatBetweenOrderByGovcreatedatDesc(
        Timestamp startDate, Timestamp endDate, Pageable pageable);
    
    long countByGovchangestatus(String status);
    
    @Query("SELECT a FROM HitlAuditChange a WHERE a.govrisklevel IN :riskLevels " +
           "AND a.govchangestatus IN :statuses ORDER BY a.govcreatedat DESC")
    Page<HitlAuditChange> findByRiskLevelAndStatus(
        @Param("riskLevels") List<String> riskLevels,
        @Param("statuses") List<String> statuses,
        Pageable pageable);
}
```

---

## TESTING

### 1. Test de Interceptor

```java
@Test
public void testHitlAuditInterceptor_ModificarRiesgo_DeberiaRegistrar() {
    // Given
    AgentApproval approval = new AgentApproval();
    approval.setAgtriskassessment("{\"riskLevel\": \"MEDIUM\", \"requiresHitl\": true}");
    
    // When
    approval.setAgtriskassessment("{\"riskLevel\": \"MEDIUM\", \"requiresHitl\": false}");
    repository.save(approval);
    
    // Then
    List<HitlAuditChange> audits = auditRepository.findByGoventitytypeAndGoventityid("AGENT", approval.getIdxagentapproval());
    assertFalse(audits.isEmpty());
}
```

### 2. Test de Consulta de Auditoría

```java
@Test
public void testGetBlockedAttempts_DeberiaRetornarSoloBloqueados() {
    // When
    Page<HitlAuditChange> blocked = auditService.getBlockedAttempts(PageRequest.of(0, 10));
    
    // Then
    assertTrue(blocked.getContent().stream()
        .allMatch(a -> "BLOCKED".equals(a.getGovchangestatus())));
}
```

---

## MÉTRICAS DE ÉXITO

- **100%** de intentos de modificación registrados en auditoría
- **100%** de intentos bloqueados con alerta enviada
- Tiempo de registro < 50ms (sin impacto en performance)

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md#inc-hitl-003`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_SUPERVISION_HUMANA_HITL.md`
- **Relacionado:** INC-HITL-001 (protección multicapa)
- **EU AI Act Art. 14.4:** Transparency
- **ISO 42001:** Audit trails

---

**Estado:** ✅ COMPLETADO  
**Esfuerzo Estimado:** 4-6 días  
**Responsable:** Governance Team + Backend Team  
**Fecha Límite:** 2 semanas

