# PROMPT: INC-010-001 - Documentación Formal del Sistema PMM

**Incidencia:** INC-010-001  
**Prioridad:** 🔴 CRÍTICA (Certification Blocker)  
**Artículo EU AI Act:** Art. 16.g, Art. 72  
**Esfuerzo Estimado:** 3 días  
**Tipo:** Java - Backend + Documentación  
**Referencia:** GAP-017

---

## CONTEXTO

El sistema de Post Market Monitoring (PMM) está parcialmente implementado pero falta documentación formal según Art. 16.g del EU AI Act. Se requiere crear la entidad `PostMarketMonitoringPlan` y documentar el sistema PMM.

**Estado Actual:**
- ✅ Servicio `PostMarketMonitoringService` implementado
- ✅ Proceso BPMN `compliance-monitoring-v1.bpmn` documentado
- ❌ Falta entidad `PostMarketMonitoringPlan`
- ❌ Falta documento formal del plan PMM por proyecto/modelo

---

## REQUISITOS

1. Crear entidad JPA `PostMarketMonitoringPlan` con tabla `PMMPOSTMARKETMONITORINGPLANS`
2. Crear servicio `PostMarketMonitoringPlanService` con CRUD completo
3. Validación de planes según Art. 72
4. Vinculación con proyectos y modelos
5. Crear ViewModel y pantalla ZUL para gestión de planes PMM

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear Entidad JPA `PostMarketMonitoringPlan`

**Tabla SQL:**
```sql
CREATE TABLE PMMPOSTMARKETMONITORINGPLANS (
    iduuid UUID UNIQUE,
    IDXPMMPLAN BIGSERIAL PRIMARY KEY,
    PMMPLANNAME VARCHAR(255) NOT NULL,
    IDXPROJECT BIGINT NOT NULL,
    IDXMODEL BIGINT,
    PMMMONITORINGFREQUENCY VARCHAR(50) NOT NULL, -- DAILY, WEEKLY, MONTHLY
    PMMMETRICS JSONB NOT NULL, -- Lista de métricas a monitorear
    PMMALERTTHRESHOLDS JSONB NOT NULL, -- Thresholds por métrica
    PMMREPORTINGFREQUENCY VARCHAR(50) NOT NULL, -- DAILY, WEEKLY, MONTHLY
    PMMSTATUS TEXT[] NOT NULL, -- DRAFT, ACTIVE, SUSPENDED
    PMMCREATEDAT TIMESTAMP NOT NULL,
    PMMUPDATEDAT TIMESTAMP,
    CONSTRAINT FK_PMM_PROJECT FOREIGN KEY (IDXPROJECT) REFERENCES PRJPROJECTS(IDXPROJECT),
    CONSTRAINT FK_PMM_MODEL FOREIGN KEY (IDXMODEL) REFERENCES MODMODELS(IDXMODEL)
);

CREATE INDEX IDX_PMM_PROJECT ON PMMPOSTMARKETMONITORINGPLANS(IDXPROJECT);
CREATE INDEX IDX_PMM_MODEL ON PMMPOSTMARKETMONITORINGPLANS(IDXMODEL);
CREATE INDEX IDX_PMM_STATUS ON PMMPOSTMARKETMONITORINGPLANS USING GIN(PMMSTATUS);
```

**Entidad Java:**
```java
package com.codeflowx.govern.entities.compliance;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "PMMPOSTMARKETMONITORINGPLANS")
@Data
public class PostMarketMonitoringPlan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXPMMPLAN")
    private Long idxpmmplan;
    
    @Column(name = "iduuid", unique = true, nullable = false)
    private UUID iduuid = UUID.randomUUID();
    
    @Column(name = "PMMPLANNAME", nullable = false, length = 255)
    private String pmmplanname;
    
    @Column(name = "IDXPROJECT", nullable = false)
    private Long idxproject;
    
    @Column(name = "IDXMODEL")
    private Long idxmodel;
    
    @Column(name = "PMMMONITORINGFREQUENCY", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private MonitoringFrequency pmmmonitoringfrequency;
    
    @Column(name = "PMMMETRICS", nullable = false, columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> pmmmetrics; // Lista de métricas a monitorear
    
    @Column(name = "PMMALERTTHRESHOLDS", nullable = false, columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> pmmalertthresholds; // Thresholds por métrica
    
    @Column(name = "PMMREPORTINGFREQUENCY", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ReportingFrequency pmmreportingfrequency;
    
    @Column(name = "PMMSTATUS", nullable = false, columnDefinition = "text[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private Set<String> pmmstatus; // DRAFT, ACTIVE, SUSPENDED
    
    @Column(name = "PMMCREATEDAT", nullable = false)
    private LocalDateTime pmmcreatedat = LocalDateTime.now();
    
    @Column(name = "PMMUPDATEDAT")
    private LocalDateTime pmmupdatedat;
    
    public enum MonitoringFrequency {
        DAILY, WEEKLY, MONTHLY
    }
    
    public enum ReportingFrequency {
        DAILY, WEEKLY, MONTHLY
    }
}
```

### 2. Crear Servicio `PostMarketMonitoringPlanService`

```java
package com.codeflowx.govern.services.compliance;

import com.codeflowx.govern.entities.compliance.PostMarketMonitoringPlan;
import com.codeflowx.govern.repositories.compliance.PostMarketMonitoringPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostMarketMonitoringPlanService {
    
    private final PostMarketMonitoringPlanRepository repository;
    
    /**
     * Crea un nuevo plan PMM
     * Valida que el plan cumpla con Art. 72
     */
    @Transactional
    public PostMarketMonitoringPlan createPlan(PostMarketMonitoringPlan plan) {
        log.info("Creando plan PMM: {}", plan.getPmmplanname());
        
        // Validación según Art. 72
        validatePlan(plan);
        
        plan.setPmmstatus(Set.of("DRAFT"));
        plan.setPmmcreatedat(LocalDateTime.now());
        
        return repository.save(plan);
    }
    
    /**
     * Activa un plan PMM
     * Solo puede haber un plan activo por proyecto
     */
    @Transactional
    public PostMarketMonitoringPlan activatePlan(Long planId) {
        PostMarketMonitoringPlan plan = repository.findById(planId)
            .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado"));
        
        // Desactivar otros planes del mismo proyecto
        repository.findByIdxprojectAndPmmstatusContaining(plan.getIdxproject(), "ACTIVE")
            .forEach(p -> {
                Set<String> status = p.getPmmstatus();
                status.remove("ACTIVE");
                status.add("SUSPENDED");
                p.setPmmupdatedat(LocalDateTime.now());
                repository.save(p);
            });
        
        // Activar este plan
        plan.getPmmstatus().remove("DRAFT");
        plan.getPmmstatus().add("ACTIVE");
        plan.setPmmupdatedat(LocalDateTime.now());
        
        return repository.save(plan);
    }
    
    /**
     * Valida que el plan cumpla con Art. 72
     */
    private void validatePlan(PostMarketMonitoringPlan plan) {
        if (plan.getPmmplanname() == null || plan.getPmmplanname().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del plan es obligatorio");
        }
        
        if (plan.getIdxproject() == null) {
            throw new IllegalArgumentException("El proyecto es obligatorio");
        }
        
        if (plan.getPmmmetrics() == null || plan.getPmmmetrics().isEmpty()) {
            throw new IllegalArgumentException("El plan debe incluir al menos una métrica a monitorear");
        }
        
        if (plan.getPmmalertthresholds() == null || plan.getPmmalertthresholds().isEmpty()) {
            throw new IllegalArgumentException("El plan debe incluir thresholds de alertas");
        }
    }
    
    /**
     * Obtiene el plan activo de un proyecto
     */
    public Optional<PostMarketMonitoringPlan> getActivePlanByProject(Long projectId) {
        return repository.findByIdxprojectAndPmmstatusContaining(projectId, "ACTIVE")
            .stream()
            .findFirst();
    }
    
    /**
     * Obtiene todos los planes de un proyecto
     */
    public List<PostMarketMonitoringPlan> getPlansByProject(Long projectId) {
        return repository.findByIdxproject(projectId);
    }
    
    /**
     * Actualiza un plan PMM
     */
    @Transactional
    public PostMarketMonitoringPlan updatePlan(Long planId, PostMarketMonitoringPlan updatedPlan) {
        PostMarketMonitoringPlan plan = repository.findById(planId)
            .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado"));
        
        plan.setPmmplanname(updatedPlan.getPmmplanname());
        plan.setPmmmonitoringfrequency(updatedPlan.getPmmmonitoringfrequency());
        plan.setPmmmetrics(updatedPlan.getPmmmetrics());
        plan.setPmmalertthresholds(updatedPlan.getPmmalertthresholds());
        plan.setPmmreportingfrequency(updatedPlan.getPmmreportingfrequency());
        plan.setPmmupdatedat(LocalDateTime.now());
        
        validatePlan(plan);
        
        return repository.save(plan);
    }
    
    /**
     * Elimina un plan PMM (soft delete)
     */
    @Transactional
    public void deletePlan(Long planId) {
        PostMarketMonitoringPlan plan = repository.findById(planId)
            .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado"));
        
        plan.getPmmstatus().clear();
        plan.getPmmstatus().add("SUSPENDED");
        plan.setPmmupdatedat(LocalDateTime.now());
        
        repository.save(plan);
    }
}
```

### 3. Crear Repository

```java
package com.codeflowx.govern.repositories.compliance;

import com.codeflowx.govern.entities.compliance.PostMarketMonitoringPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostMarketMonitoringPlanRepository extends JpaRepository<PostMarketMonitoringPlan, Long> {
    
    List<PostMarketMonitoringPlan> findByIdxproject(Long projectId);
    
    List<PostMarketMonitoringPlan> findByIdxprojectAndPmmstatusContaining(Long projectId, String status);
    
    Optional<PostMarketMonitoringPlan> findByIdxprojectAndIdxmodelAndPmmstatusContaining(
        Long projectId, Long modelId, String status);
}
```

### 4. Crear ViewModel (ZUL)

```java
package com.codeflowx.govern.viewmodels.compliance;

import com.codeflowx.govern.entities.compliance.PostMarketMonitoringPlan;
import com.codeflowx.govern.services.compliance.PostMarketMonitoringPlanService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.List;

@Getter
@Setter
@Slf4j
public class PostMarketMonitoringPlanViewModel {
    
    @WireVariable
    private PostMarketMonitoringPlanService pmmPlanService;
    
    private List<PostMarketMonitoringPlan> plans;
    private PostMarketMonitoringPlan selectedPlan;
    private PostMarketMonitoringPlan newPlan = new PostMarketMonitoringPlan();
    
    private Long projectId;
    
    @Init
    public void init(@ContextParam(ContextType.VIEW) org.zkoss.zk.ui.Component view) {
        // Obtener projectId de parámetros o contexto
        loadPlans();
    }
    
    @Command
    public void loadPlans() {
        if (projectId != null) {
            plans = pmmPlanService.getPlansByProject(projectId);
        }
    }
    
    @Command
    public void createPlan() {
        try {
            newPlan.setIdxproject(projectId);
            pmmPlanService.createPlan(newPlan);
            newPlan = new PostMarketMonitoringPlan();
            loadPlans();
            org.zkoss.zk.ui.util.Clients.showNotification("Plan PMM creado exitosamente");
        } catch (Exception e) {
            log.error("Error creando plan PMM", e);
            org.zkoss.zk.ui.util.Clients.showNotification("Error: " + e.getMessage(), "error", null, null, 5000);
        }
    }
    
    @Command
    public void activatePlan(@BindingParam("plan") PostMarketMonitoringPlan plan) {
        try {
            pmmPlanService.activatePlan(plan.getIdxpmmplan());
            loadPlans();
            org.zkoss.zk.ui.util.Clients.showNotification("Plan PMM activado");
        } catch (Exception e) {
            log.error("Error activando plan PMM", e);
            org.zkoss.zk.ui.util.Clients.showNotification("Error: " + e.getMessage(), "error", null, null, 5000);
        }
    }
    
    @Command
    public void updatePlan() {
        try {
            pmmPlanService.updatePlan(selectedPlan.getIdxpmmplan(), selectedPlan);
            loadPlans();
            org.zkoss.zk.ui.util.Clients.showNotification("Plan PMM actualizado");
        } catch (Exception e) {
            log.error("Error actualizando plan PMM", e);
            org.zkoss.zk.ui.util.Clients.showNotification("Error: " + e.getMessage(), "error", null, null, 5000);
        }
    }
}
```

### 5. Documentación del Sistema PMM

Crear documento técnico en `docs/compliance/pmm/SISTEMA_PMM.md`:

**Secciones requeridas:**
1. Introducción al sistema PMM
2. Arquitectura del sistema
3. Componentes principales
4. Flujo de monitoreo
5. Configuración de planes PMM
6. Procedimientos operativos
7. Integración con Art. 49

---

## VALIDACIONES

1. ✅ Entidad JPA creada con todas las columnas
2. ✅ Servicio con CRUD completo
3. ✅ Validación según Art. 72
4. ✅ Vinculación con proyectos/modelos
5. ✅ ViewModel y pantalla ZUL funcional
6. ✅ Documentación técnica creada

---

## NOTAS

- Aplicar reglas SOLID y arquitectura hexagonal
- Usar prefijo `PMM` para tabla
- PK autonumérica `IDXPMMPLAN`
- Tercera forma normal
- KISS principle


---

**Estado:** ✅ COMPLETADO






