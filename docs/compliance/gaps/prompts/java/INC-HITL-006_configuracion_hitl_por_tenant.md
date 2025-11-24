# PROMPT: INC-HITL-006 - Configuración de HITL por Partner/Tenant

**Incidencia:** INC-HITL-006  
**Prioridad:** 🟠 HIGH  
**Artículo EU AI Act:** Art. 14 (Human oversight)  
**Esfuerzo Estimado:** 7-10 días  
**Tipo:** Java - Backend + Frontend  
**Estado:** ✅ COMPLETADO

---

## DESCRIPCIÓN

Todos los partners comparten la misma política de HITL. No hay capacidad de personalización por tenant, lo que limita la flexibilidad para diferentes sectores o casos de uso. Se requiere crear tabla de configuración por tenant con override de configuración global (con restricciones).

---

## REQUISITOS

1. Crear tabla `GOVHITLCONFIG` con configuración por tenant
2. Permitir override de configuración global con validación de restricciones
3. Mantener controles críticos como no modificables (incluso con override)
4. Implementar UI de configuración con validación de políticas

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Tabla de Configuración HITL por Tenant

**Archivo:** `nocode.service.entitys/src/main/resources/sql/hitl_config_tenant.sql`

```sql
CREATE TABLE GOVHITLCONFIG (
    IDXHITLCONFIG BIGSERIAL PRIMARY KEY,
    iduuid VARCHAR(36) UNIQUE NOT NULL DEFAULT gen_random_uuid()::text,
    GOVPARTNERID BIGINT NOT NULL, -- Referencia a partner/tenant
    GOVENTITYTYPE VARCHAR(50), -- AGENT, MODEL, PROMPT, o NULL para global
    GOVRISKLEVEL VARCHAR(50), -- HIGH, MEDIUM, LOW, o NULL para todos
    GOVREQUIRESHITL BOOLEAN NOT NULL DEFAULT true,
    GOVAPPROVALLEVEL VARCHAR(50), -- AUTOMATIC, SINGLE_APPROVER, DUAL_APPROVAL, COMMITTEE
    GOVTIMEOUTHOURS INTEGER, -- Timeout en horas
    GOVESCALATIONROLE VARCHAR(100), -- Rol al que escalar
    GOVISOVERRIDE BOOLEAN NOT NULL DEFAULT false, -- Si es override de configuración global
    GOVISCRITICAL BOOLEAN NOT NULL DEFAULT false, -- Si es control crítico (no modificable)
    GOVCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    GOVUPDATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_entity_type CHECK (GOVENTITYTYPE IN ('AGENT', 'MODEL', 'PROMPT', NULL)),
    CONSTRAINT chk_risk_level CHECK (GOVRISKLEVEL IN ('HIGH', 'CRITICAL', 'MEDIUM', 'LOW', NULL)),
    CONSTRAINT chk_approval_level CHECK (GOVAPPROVALLEVEL IN ('AUTOMATIC', 'SINGLE_APPROVER', 'DUAL_APPROVAL', 'COMMITTEE', NULL))
);

CREATE INDEX idx_gov_hitl_config_partner ON GOVHITLCONFIG(GOVPARTNERID);
CREATE INDEX idx_gov_hitl_config_entity ON GOVHITLCONFIG(GOVENTITYTYPE, GOVRISKLEVEL);
```

### 2. Entidad Java

**Archivo:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/config/HitlConfig.java`

```java
package com.codeflowx.govern.entity.config;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "GOVHITLCONFIG")
public class HitlConfig implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXHITLCONFIG")
    private Long idxhitlconfig;
    
    @NotNull
    @Column(name = "GOVPARTNERID", nullable = false)
    private Long govpartnerid;
    
    @Column(name = "GOVENTITYTYPE", length = 50)
    private String goventitytype;
    
    @Column(name = "GOVRISKLEVEL", length = 50)
    private String govrisklevel;
    
    @NotNull
    @Column(name = "GOVREQUIRESHITL", nullable = false)
    private Boolean govrequireshitl = true;
    
    @Column(name = "GOVAPPROVALLEVEL", length = 50)
    private String govapprovallevel;
    
    @Column(name = "GOVTIMEOUTHOURS")
    private Integer govtimeouthours;
    
    @Column(name = "GOVESCALATIONROLE", length = 100)
    private String govescalationrole;
    
    @NotNull
    @Column(name = "GOVISOVERRIDE", nullable = false)
    private Boolean govisoverride = false;
    
    @NotNull
    @Column(name = "GOVISCRITICAL", nullable = false)
    private Boolean goviscritical = false;
    
    @NotNull
    @Column(name = "GOVCREATEDAT", nullable = false, updatable = false)
    private Timestamp govcreatedat = new Timestamp(System.currentTimeMillis());
    
    @NotNull
    @Column(name = "GOVUPDATEDAT", nullable = false)
    private Timestamp govupdatedat = new Timestamp(System.currentTimeMillis());
}
```

### 3. Servicio de Configuración

**Archivo:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/hitl/HitlConfigService.java`

```java
package com.codeflowx.govern.service.hitl;

import com.codeflowx.govern.entity.config.HitlConfig;
import com.codeflowx.govern.repository.config.HitlConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HitlConfigService {
    
    @Autowired
    private HitlConfigRepository configRepository;
    
    /**
     * Obtener configuración HITL para un partner/tenant
     * Busca primero configuración específica, luego global
     */
    public HitlConfig getConfigForPartner(Long partnerId, String entityType, String riskLevel) {
        // Buscar configuración específica
        HitlConfig specific = configRepository.findByPartnerAndEntityAndRisk(
            partnerId, entityType, riskLevel);
        
        if (specific != null) {
            return specific;
        }
        
        // Buscar configuración global del partner
        HitlConfig global = configRepository.findByPartnerAndEntityAndRisk(
            partnerId, null, null);
        
        if (global != null) {
            return global;
        }
        
        // Retornar configuración por defecto
        return getDefaultConfig(entityType, riskLevel);
    }
    
    /**
     * Crear o actualizar configuración
     * Valida que controles críticos no se modifiquen
     */
    @Transactional
    public HitlConfig saveConfig(HitlConfig config) {
        // Validar que controles críticos no se modifiquen
        if (config.getGoviscritical() && config.getGovisoverride()) {
            throw new IllegalStateException(
                "No se puede modificar configuración de control crítico");
        }
        
        // Validar que alto riesgo siempre requiera HITL
        if (("HIGH".equals(config.getGovrisklevel()) || 
             "CRITICAL".equals(config.getGovrisklevel())) &&
            !config.getGovrequireshitl()) {
            throw new IllegalStateException(
                "HITL es obligatorio para sistemas de alto riesgo (EU AI Act Art. 14.1)");
        }
        
        config.setGovupdatedat(new Timestamp(System.currentTimeMillis()));
        return configRepository.save(config);
    }
    
    private HitlConfig getDefaultConfig(String entityType, String riskLevel) {
        HitlConfig defaultConfig = new HitlConfig();
        defaultConfig.setGovrequireshitl(true);
        
        if ("HIGH".equals(riskLevel) || "CRITICAL".equals(riskLevel)) {
            defaultConfig.setGovapprovallevel("CRITICAL".equals(riskLevel) ? "COMMITTEE" : "DUAL_APPROVAL");
            defaultConfig.setGovtimeouthours(24);
        } else if ("MEDIUM".equals(riskLevel)) {
            defaultConfig.setGovapprovallevel("SINGLE_APPROVER");
            defaultConfig.setGovtimeouthours(72);
        } else {
            defaultConfig.setGovapprovallevel("AUTOMATIC");
        }
        
        return defaultConfig;
    }
}
```

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md#inc-hitl-006`
- **EU AI Act Art. 14:** Human oversight

---

**Estado:** ✅ COMPLETADO  
**Esfuerzo Estimado:** 7-10 días  
**Responsable:** Backend Team + Frontend Team  
**Fecha Límite:** 1.5 meses

