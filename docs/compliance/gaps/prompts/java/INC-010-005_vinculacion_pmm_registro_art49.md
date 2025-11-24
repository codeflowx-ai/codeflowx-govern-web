# PROMPT: INC-010-005 - Vinculación PMM con Registro Art. 49

**Incidencia:** INC-010-005  
**Prioridad:** 🔴 CRÍTICA (Certification Blocker)  
**Artículo EU AI Act:** Art. 16.h  
**Esfuerzo Estimado:** 2 días  
**Tipo:** Java - Backend  
**Referencia:** GAP-017

---

## CONTEXTO

El sistema PMM no está vinculado con el registro en Base de Datos UE según Art. 49. Art. 16.h requiere registrar el sistema de conformidad, incluyendo el sistema PMM.

**Estado Actual:**
- ✅ Entidad `EuRegistration` implementada
- ❌ Falta campo en `EuRegistration` para vincular PMM
- ❌ Falta validación de PMM en proceso de registro

---

## REQUISITOS

1. Extender entidad `EuRegistration` con campos para PMM
2. Validar que PMM esté activo antes de registrar
3. Incluir información de PMM en submission a BD UE
4. Actualizar registro si PMM cambia
5. Crear vista de relación registro + PMM plan
6. Dashboard de cumplimiento Art. 16.h

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Extender Entidad `EuRegistration`

**Modificar tabla SQL:**
```sql
ALTER TABLE EURREGISTRATIONS ADD COLUMN REGPMMPLANID BIGINT;
ALTER TABLE EURREGISTRATIONS ADD COLUMN REGPMMSTATUS VARCHAR(50); -- ACTIVE, SUSPENDED
ALTER TABLE EURREGISTRATIONS ADD CONSTRAINT FK_EUR_PMM_PLAN 
    FOREIGN KEY (REGPMMPLANID) REFERENCES PMMPOSTMARKETMONITORINGPLANS(IDXPMMPLAN);

CREATE INDEX IDX_EUR_PMM_PLAN ON EURREGISTRATIONS(REGPMMPLANID);
CREATE INDEX IDX_EUR_PMM_STATUS ON EURREGISTRATIONS(REGPMMSTATUS);
```

**Modificar entidad Java:**
```java
package com.codeflowx.govern.entities.compliance;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "EURREGISTRATIONS")
@Data
public class EuRegistration {
    
    // ... campos existentes ...
    
    @Column(name = "REGPMMPLANID")
    private Long regpmmplanid;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REGPMMPLANID", insertable = false, updatable = false)
    private PostMarketMonitoringPlan pmmPlan;
    
    @Column(name = "REGPMMSTATUS", length = 50)
    @Enumerated(EnumType.STRING)
    private PmmStatus regpmmstatus;
    
    public enum PmmStatus {
        ACTIVE, SUSPENDED
    }
}
```

### 2. Actualizar Servicio `EuRegistrationService`

```java
package com.codeflowx.govern.services.compliance;

import com.codeflowx.govern.entities.compliance.EuRegistration;
import com.codeflowx.govern.entities.compliance.PostMarketMonitoringPlan;
import com.codeflowx.govern.repositories.compliance.EuRegistrationRepository;
import com.codeflowx.govern.repositories.compliance.PostMarketMonitoringPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EuRegistrationService {
    
    private final EuRegistrationRepository registrationRepository;
    private final PostMarketMonitoringPlanRepository pmmPlanRepository;
    
    /**
     * Crea o actualiza un registro en BD UE
     * Valida que PMM esté activo según Art. 16.h
     */
    @Transactional
    public EuRegistration createOrUpdateRegistration(EuRegistration registration) {
        log.info("Creando/actualizando registro UE para proyecto: {}", registration.getIdxproject());
        
        // Validar que PMM esté activo
        if (registration.getRegpmmplanid() != null) {
            validatePmmPlan(registration.getRegpmmplanid());
        } else {
            // Buscar plan PMM activo del proyecto
            Optional<PostMarketMonitoringPlan> activePlan = 
                pmmPlanRepository.findByIdxprojectAndPmmstatusContaining(
                    registration.getIdxproject(), "ACTIVE")
                .stream()
                .findFirst();
            
            if (activePlan.isPresent()) {
                registration.setRegpmmplanid(activePlan.get().getIdxpmmplan());
                registration.setRegpmmstatus(EuRegistration.PmmStatus.ACTIVE);
            } else {
                throw new IllegalStateException(
                    "No se puede registrar sin un plan PMM activo según Art. 16.h");
            }
        }
        
        // Incluir información de PMM en submission
        registration = enrichRegistrationWithPmmData(registration);
        
        return registrationRepository.save(registration);
    }
    
    /**
     * Valida que el plan PMM esté activo
     */
    private void validatePmmPlan(Long pmmPlanId) {
        PostMarketMonitoringPlan plan = pmmPlanRepository.findById(pmmPlanId)
            .orElseThrow(() -> new IllegalArgumentException("Plan PMM no encontrado"));
        
        if (!plan.getPmmstatus().contains("ACTIVE")) {
            throw new IllegalStateException(
                "El plan PMM debe estar activo para registrar según Art. 16.h");
        }
    }
    
    /**
     * Enriquece el registro con información de PMM para submission a BD UE
     */
    private EuRegistration enrichRegistrationWithPmmData(EuRegistration registration) {
        if (registration.getRegpmmplanid() != null) {
            PostMarketMonitoringPlan plan = pmmPlanRepository.findById(registration.getRegpmmplanid())
                .orElseThrow();
            
            // Agregar información de PMM al JSON de submission
            Map<String, Object> submissionData = registration.getRegsubmissiondata();
            if (submissionData == null) {
                submissionData = new HashMap<>();
            }
            
            Map<String, Object> pmmInfo = new HashMap<>();
            pmmInfo.put("pmmPlanId", plan.getIdxpmmplan());
            pmmInfo.put("pmmPlanName", plan.getPmmplanname());
            pmmInfo.put("monitoringFrequency", plan.getPmmmonitoringfrequency().name());
            pmmInfo.put("reportingFrequency", plan.getPmmreportingfrequency().name());
            pmmInfo.put("metrics", plan.getPmmmetrics());
            pmmInfo.put("status", "ACTIVE");
            
            submissionData.put("postMarketMonitoring", pmmInfo);
            registration.setRegsubmissiondata(submissionData);
        }
        
        return registration;
    }
    
    /**
     * Actualiza el registro si el PMM cambia
     */
    @Transactional
    public void updateRegistrationOnPmmChange(Long projectId, Long oldPmmPlanId, Long newPmmPlanId) {
        log.info("Actualizando registros UE por cambio de PMM plan para proyecto: {}", projectId);
        
        registrationRepository.findByIdxproject(projectId)
            .forEach(registration -> {
                if (registration.getRegpmmplanid() != null && 
                    registration.getRegpmmplanid().equals(oldPmmPlanId)) {
                    
                    // Actualizar con nuevo plan
                    registration.setRegpmmplanid(newPmmPlanId);
                    registration = enrichRegistrationWithPmmData(registration);
                    registration.setRegupdatedat(LocalDateTime.now());
                    
                    registrationRepository.save(registration);
                    
                    // Si el registro ya fue enviado, puede requerir actualización en BD UE
                    if (registration.getRegstatus().contains("SUBMITTED")) {
                        log.warn("Registro ya enviado a BD UE requiere actualización por cambio de PMM");
                        // TODO: Implementar actualización en BD UE si es necesario
                    }
                }
            });
    }
    
    /**
     * Obtiene registros con información de PMM
     */
    public List<EuRegistration> getRegistrationsWithPmm(Long projectId) {
        return registrationRepository.findByIdxproject(projectId);
    }
}
```

### 3. Crear Vista de Relación Registro + PMM

**Vista SQL:**
```sql
CREATE OR REPLACE VIEW VW_EUR_REGISTRATIONS_WITH_PMM AS
SELECT 
    eur.IDXEURREGISTRATION,
    eur.IDXPROJECT,
    eur.REGSTATUS,
    eur.REGPMMPLANID,
    eur.REGPMMSTATUS,
    pmm.PMMPLANNAME,
    pmm.PMMMONITORINGFREQUENCY,
    pmm.PMMREPORTINGFREQUENCY,
    pmm.PMMSTATUS AS PMMPLANSTATUS,
    CASE 
        WHEN eur.REGPMMPLANID IS NOT NULL AND pmm.PMMSTATUS @> ARRAY['ACTIVE']::text[] 
        THEN 'COMPLIANT'
        ELSE 'NON_COMPLIANT'
    END AS ART16H_COMPLIANCE_STATUS
FROM EURREGISTRATIONS eur
LEFT JOIN PMMPOSTMARKETMONITORINGPLANS pmm ON eur.REGPMMPLANID = pmm.IDXPMMPLAN;
```

### 4. Crear Dashboard de Cumplimiento Art. 16.h

**ViewModel:**
```java
package com.codeflowx.govern.viewmodels.compliance;

import com.codeflowx.govern.services.compliance.EuRegistrationService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Slf4j
public class Art16hComplianceViewModel {
    
    @WireVariable
    private EuRegistrationService registrationService;
    
    private List<Map<String, Object>> registrationsWithPmm;
    private Long projectId;
    
    @Init
    public void init(@ContextParam(ContextType.VIEW) org.zkoss.zk.ui.Component view) {
        loadData();
    }
    
    @Command
    public void loadData() {
        if (projectId != null) {
            registrationsWithPmm = registrationService.getRegistrationsWithPmm(projectId)
                .stream()
                .map(this::mapToDisplay)
                .collect(Collectors.toList());
        }
    }
    
    private Map<String, Object> mapToDisplay(EuRegistration reg) {
        Map<String, Object> map = new HashMap<>();
        map.put("registrationId", reg.getIdxeurregistration());
        map.put("projectId", reg.getIdxproject());
        map.put("status", reg.getRegstatus());
        map.put("pmmPlanId", reg.getRegpmmplanid());
        map.put("pmmStatus", reg.getRegpmmstatus());
        map.put("compliant", reg.getRegpmmplanid() != null && 
            reg.getRegpmmstatus() == EuRegistration.PmmStatus.ACTIVE);
        return map;
    }
}
```

---

## VALIDACIONES

1. ✅ Campo `REGPMMPLANID` agregado a `EuRegistration`
2. ✅ Campo `REGPMMSTATUS` agregado
3. ✅ Validación de PMM activo antes de registrar
4. ✅ Información de PMM incluida en submission a BD UE
5. ✅ Actualización automática si PMM cambia
6. ✅ Vista de relación creada
7. ✅ Dashboard de cumplimiento Art. 16.h funcional

---

## NOTAS

- Aplicar reglas SOLID y arquitectura hexagonal
- Validar siempre que PMM esté activo según Art. 16.h
- Incluir información de PMM en submission a BD UE
- Mantener sincronización entre registro y PMM plan

---

**Estado:** ✅ COMPLETADO

