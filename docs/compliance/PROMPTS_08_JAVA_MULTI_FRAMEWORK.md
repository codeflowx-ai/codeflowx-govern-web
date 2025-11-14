# PROMPTS 08 - JAVA MULTI-FRAMEWORK (JPA Entities + ViewModels + ZUL)
## Entidades JPA, ViewModels, Pantallas - 100% Compliance

**Fecha:** Noviembre 2025  
**Objetivo:** Implementaciones JAVA para ISO 42001, ISO 38507, OECD, GDPR  
**Arquitectura:** JPA Entities → ViewModels → ZUL (SIN JSON, SIN BusinessServices, SIN Controllers)  
**Total Prompts:** 22

**CRÍTICO:** Revisar contra código existente antes de implementar. Pueden existir módulos similares.

---

## 🏗️ ARQUITECTURA JAVA CORRECTA

```
1. Crear Entity JPA DIRECTAMENTE (@Entity)
2. ViewModel ZKoss usa Entity directamente (con DAO EnArt si existe)
3. Pantalla ZUL binds a ViewModel
```

**SÍ CREAR:**
- ✅ Entities JPA (@Entity, @Table, @Column)
- ✅ ViewModels ZKoss
- ✅ Pantallas ZUL

**NO CREAR (se hará después para API/SDK):**
- ❌ JSON entity definitions
- ❌ BusinessServices
- ❌ REST Controllers
- ❌ Python generators

---

## 📋 ÍNDICE PROMPTS JAVA

---

## 📌 Estado de ejecución (actualizado noviembre 2025)

### ✅ Implementado
- **J1.1 AI Objectives Management**: entidad `nocode/services/entitys/governance/AIMObjectives.java`, ViewModel `com/codeflowx/govern/viewmodel/governance/AIObjectivesViewModel.java`, pantalla ZUL `src/main/webapp/console/platform/governance/ai_objectives_management.zul`, migración `sql-scripts/extensions/03_iso42001_ai_objectives.sql`.
- **J1.2 AI Competence Management**: entidad `nocode/services/entitys/governance/AICCompetence.java` y registros de entrenamiento `nocode/services/entitys/governance/AITrainingRecord.java`; ViewModel `com/codeflowx/govern/viewmodel/governance/AICompetenceViewModel.java`; pantalla `src/main/webapp/console/platform/governance/ai_competence_management.zul`; migraciones `04_iso42001_ai_competence.sql` y `05_iso42001_ai_training_records.sql`.
- **J1.3 AI Training Records**: integrado en el mismo ViewModel y ZUL de competencias; se soportan altas, filtros por rol, registro de awareness y gestión de certificados.
- **J1.4 AI System Inventory Extension**: entidad extendida `nocode/services/entitys/projects/PRJProject.java`, migración `06_iso42001_prjprojects_extension.sql`; ViewModel `com/codeflowx/govern/viewmodel/projects/ProjectAIInventoryViewModel.java`; vista `src/main/webapp/console/platform/projects/project-ai-inventory.zul` embebida en `project-detail.zul`.
- **J1.5 AIMS Performance**: entidad `nocode/services/entitys/governance/APSAimsPerformance.java`; ViewModel `com/codeflowx/govern/viewmodel/governance/AIMSPerformanceViewModel.java`; pantalla `src/main/webapp/console/platform/governance/aims_performance.zul`; migración `07_iso42001_aims_performance.sql`.
- **J1.6 AIMS Non-Conformity**: entidad `nocode/services/entitys/governance/ANCNonConformity.java`; ViewModel `com/codeflowx/govern/viewmodel/governance/AIMSNonConformityViewModel.java`; pantalla `src/main/webapp/console/platform/governance/aims_nonconformity.zul`; migración `08_iso42001_aims_nonconformity.sql`.
- **J1.7 AIMS Improvement**: entidad `nocode/services/entitys/governance/AIMImprovement.java`; ViewModel `com/codeflowx/govern/viewmodel/governance/AIMSImprovementViewModel.java`; pantalla `src/main/webapp/console/platform/governance/aims_improvement.zul`; migración `09_iso42001_aims_improvement.sql`.
- **J1.8 ISO 42001 Controls**: entidad `nocode/services/entitys/governance/ICOISO42001Control.java`; ViewModel `com/codeflowx/govern/viewmodel/governance/ISO42001ControlsViewModel.java`; pantalla `src/main/webapp/console/platform/governance/iso42001_controls.zul`; migración `10_iso42001_iso_controls.sql` con semilla inicial de 39 controles.

### ⏳ Pendiente
- **Grupo J2 (ISO 38507)**: generar entidades, migraciones, ViewModels y ZUL para J2.1–J2.8, manteniendo el patrón definido (EntityManager directo, sin BusinessServices/REST).
- **Grupo J3 (OECD + GDPR)**: implementar las seis entidades, pantallas y scripts correspondientes.
- **Automatización complementaria**: integrar pruebas de carga ZK y validar ejecución de migraciones en todos los entornos antes de pasar a los grupos J2/J3.

---

### **GRUPO J1: ISO 42001 (8 prompts)**
- J1.1: AI Objectives Entity + ViewModel + ZUL
- J1.2: AI Competence Entity + ViewModel + ZUL
- J1.3: AI Training Records Entity + ViewModel + ZUL
- J1.4: AI System Inventory Extension (PROJECT) + ViewModel + ZUL
- J1.5: AIMS Performance Entity + ViewModel + ZUL
- J1.6: AIMS Non-Conformity Entity + ViewModel + ZUL
- J1.7: AIMS Improvement Entity + ViewModel + ZUL
- J1.8: ISO 42001 Controls Entity + ViewModel + ZUL

### **GRUPO J2: ISO 38507 (8 prompts)**
- J2.1: Board Report Entity + ViewModel + ZUL
- J2.2: Ethics Committee Entity + ViewModel + ZUL
- J2.3: Stakeholder Management Entity + ViewModel + ZUL
- J2.4: AI Strategic Plan Entity + ViewModel + ZUL
- J2.5: Board Decision Entity + ViewModel + ZUL
- J2.6: AI Investment Entity + ViewModel + ZUL
- J2.7: Third-Party AI Risk Entity + ViewModel + ZUL
- J2.8: AI Sustainability Metrics Entity + ViewModel + ZUL

### **GRUPO J3: OECD + GDPR (6 prompts)**
- J3.1: OECD Sustainability Entity + ViewModel + ZUL
- J3.2: OECD Stakeholder Report Entity + ViewModel + ZUL
- J3.3: GDPR Privacy Notice Entity + ViewModel + ZUL
- J3.4: GDPR Data Subject Request Entity + ViewModel + ZUL
- J3.5: GDPR ROPA (Records Processing) Entity + ViewModel + ZUL
- J3.6: OECD Human Rights Assessment Entity + ViewModel + ZUL

---

# GRUPO J1: ISO 42001 ENTITIES

---

## PROMPT J1.1 - AI Objectives Management

**REVISAR PRIMERO:** ¿Existe tabla con objectives/goals?

**Entidad JPA:**

Package: `com.codeflowx.govern.entity.governance`  
Class: `AIMObjectives.java`

```java
package com.codeflowx.govern.entity.governance;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "AIMOBJECTIVES")
public class AIMObjectives {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idxaiobjective")
    private Long idxaiobjective;
    
    @Column(name = "iduuid", nullable = false, length = 36, unique = true)
    private String iduuid;
    
    @Column(name = "aiodescription", nullable = false, columnDefinition = "TEXT")
    private String aiodescription;
    
    @Column(name = "aiocategory", nullable = false, length = 50)
    private String aiocategory;  // TRANSPARENCY, FAIRNESS, ROBUSTNESS, PRIVACY, SAFETY, ACCOUNTABILITY
    
    @Column(name = "aiometric", nullable = false, length = 200)
    private String aiometric;
    
    @Column(name = "aiotargetvalue", nullable = false, precision = 10, scale = 2)
    private BigDecimal aiotargetvalue;
    
    @Column(name = "aiocurrentvalue", precision = 10, scale = 2)
    private BigDecimal aiocurrentvalue;
    
    @Column(name = "aiostatus", nullable = false, length = 20)
    private String aiostatus;  // ACTIVE, ACHIEVED, REVISED, DISCONTINUED
    
    @Column(name = "aioresponsible", nullable = false, length = 100)
    private String aioresponsible;
    
    @Column(name = "aioreviewfrequency", nullable = false, length = 20)
    private String aioreviewfrequency;  // MONTHLY, QUARTERLY, ANNUAL
    
    @Column(name = "aiolastreviewed")
    private Timestamp aiolastreviewed;
    
    @Column(name = "aionextreview")
    private Timestamp aionextreview;
    
    @Column(name = "aiorelatedpolicy", columnDefinition = "TEXT")
    private String aiorelatedpolicy;
    
    @Column(name = "aiocreatedat", nullable = false)
    private Timestamp aiocreatedat;
    
    // Getters y Setters
    public Long getIdxaiobjective() {
        return idxaiobjective;
    }
    
    public void setIdxaiobjective(Long idxaiobjective) {
        this.idxaiobjective = idxaiobjective;
    }
    
    public String getIduuid() {
        return iduuid;
    }
    
    public void setIduuid(String iduuid) {
        this.iduuid = iduuid;
    }
    
    public String getAiodescription() {
        return aiodescription;
    }
    
    public void setAiodescription(String aiodescription) {
        this.aiodescription = aiodescription;
    }
    
    public String getAiocategory() {
        return aiocategory;
    }
    
    public void setAiocategory(String aiocategory) {
        this.aiocategory = aiocategory;
    }
    
    public String getAiometric() {
        return aiometric;
    }
    
    public void setAiometric(String aiometric) {
        this.aiometric = aiometric;
    }
    
    public BigDecimal getAiotargetvalue() {
        return aiotargetvalue;
    }
    
    public void setAiotargetvalue(BigDecimal aiotargetvalue) {
        this.aiotargetvalue = aiotargetvalue;
    }
    
    public BigDecimal getAiocurrentvalue() {
        return aiocurrentvalue;
    }
    
    public void setAiocurrentvalue(BigDecimal aiocurrentvalue) {
        this.aiocurrentvalue = aiocurrentvalue;
    }
    
    public String getAiostatus() {
        return aiostatus;
    }
    
    public void setAiostatus(String aiostatus) {
        this.aiostatus = aiostatus;
    }
    
    public String getAioresponsible() {
        return aioresponsible;
    }
    
    public void setAioresponsible(String aioresponsible) {
        this.aioresponsible = aioresponsible;
    }
    
    public String getAioreviewfrequency() {
        return aioreviewfrequency;
    }
    
    public void setAioreviewfrequency(String aioreviewfrequency) {
        this.aioreviewfrequency = aioreviewfrequency;
    }
    
    public Timestamp getAiolastreviewed() {
        return aiolastreviewed;
    }
    
    public void setAiolastreviewed(Timestamp aiolastreviewed) {
        this.aiolastreviewed = aiolastreviewed;
    }
    
    public Timestamp getAionextreview() {
        return aionextreview;
    }
    
    public void setAionextreview(Timestamp aionextreview) {
        this.aionextreview = aionextreview;
    }
    
    public String getAiorelatedpolicy() {
        return aiorelatedpolicy;
    }
    
    public void setAiorelatedpolicy(String aiorelatedpolicy) {
        this.aiorelatedpolicy = aiorelatedpolicy;
    }
    
    public Timestamp getAiocreatedat() {
        return aiocreatedat;
    }
    
    public void setAiocreatedat(Timestamp aiocreatedat) {
        this.aiocreatedat = aiocreatedat;
    }
}
```

**Migration SQL:**

```sql
CREATE TABLE AIMOBJECTIVES (
    idxaiobjective BIGSERIAL PRIMARY KEY,
    iduuid VARCHAR(36) UNIQUE NOT NULL,
    aiodescription TEXT NOT NULL,
    aiocategory VARCHAR(50) NOT NULL,
    aiometric VARCHAR(200) NOT NULL,
    aiotargetvalue DECIMAL(10,2) NOT NULL,
    aiocurrentvalue DECIMAL(10,2),
    aiostatus VARCHAR(20) NOT NULL,
    aioresponsible VARCHAR(100) NOT NULL,
    aioreviewfrequency VARCHAR(20) NOT NULL,
    aiolastreviewed TIMESTAMP,
    aionextreview TIMESTAMP,
    aiorelatedpolicy TEXT,
    aiocreatedat TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_aio_category ON AIMOBJECTIVES(aiocategory);
CREATE INDEX idx_aio_status ON AIMOBJECTIVES(aiostatus);
CREATE INDEX idx_aio_responsible ON AIMOBJECTIVES(aioresponsible);
```

**DAO (EnArt - si existe):**

Si ya existe DAO genérico EnArt, usarlo. Si no, acceso directo vía EntityManager:

```java
// En ViewModel, si no hay DAO EnArt:
@PersistenceContext
private EntityManager entityManager;

// CRUD básico
public List<AIMObjectives> findAll() {
    return entityManager.createQuery("FROM AIMObjectives", AIMObjectives.class).getResultList();
}

public AIMObjectives findById(Long id) {
    return entityManager.find(AIMObjectives.class, id);
}

public void save(AIMObjectives obj) {
    if (obj.getIdxaiobjective() == null) {
        entityManager.persist(obj);
    } else {
        entityManager.merge(obj);
    }
}
```

**ViewModel:**

Package: `com.codeflowx.govern.viewmodel.governance`  
Class: `AIObjectivesViewModel`

```java
package com.codeflowx.govern.viewmodel.governance;

import com.codeflowx.govern.entity.governance.AIMObjectives;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.util.Clients;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AIObjectivesViewModel {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private List<AIMObjectives> objectives;
    private AIMObjectives selectedObjective;
    private AIMObjectives newObjective;
    
    @Init
    public void init() {
        loadObjectives();
        newObjective = new AIMObjectives();
    }
    
    @Command
    @NotifyChange({"objectives"})
    public void loadObjectives() {
        objectives = entityManager.createQuery("FROM AIMObjectives ORDER BY aiocreatedat DESC", AIMObjectives.class)
                                  .getResultList();
    }
    
    @Command
    @NotifyChange({"objectives", "newObjective"})
    public void createObjective() {
        // Validar
        if (newObjective.getAiodescription() == null || newObjective.getAiodescription().isEmpty()) {
            Clients.showNotification("Description is required", "error", null, "top_center", 3000);
            return;
        }
        
        // Set defaults
        newObjective.setIduuid(UUID.randomUUID().toString());
        newObjective.setAiocreatedat(new Timestamp(System.currentTimeMillis()));
        newObjective.setAiostatus("ACTIVE");
        
        // Calculate next review
        newObjective.setAionextreview(calculateNextReview(newObjective.getAioreviewfrequency()));
        
        // Persist
        entityManager.persist(newObjective);
        
        // Reload
        loadObjectives();
        newObjective = new AIMObjectives();
        
        Clients.showNotification("Objective created successfully", "info", null, "top_center", 3000);
    }
    
    @Command
    @NotifyChange({"objectives"})
    public void updateProgress(@BindingParam("objective") AIMObjectives obj, @BindingParam("value") BigDecimal value) {
        obj.setAiocurrentvalue(value);
        
        // Check if achieved
        if (value.compareTo(obj.getAiotargetvalue()) >= 0) {
            obj.setAiostatus("ACHIEVED");
        }
        
        entityManager.merge(obj);
        loadObjectives();
        
        Clients.showNotification("Progress updated", "info", null, "top_center", 3000);
    }
    
    @Command
    @NotifyChange({"objectives"})
    public void reviewObjective(@BindingParam("objective") AIMObjectives obj) {
        obj.setAiolastreviewed(new Timestamp(System.currentTimeMillis()));
        obj.setAionextreview(calculateNextReview(obj.getAioreviewfrequency()));
        
        entityManager.merge(obj);
        loadObjectives();
        
        Clients.showNotification("Objective reviewed successfully", "info", null, "top_center", 3000);
    }
    
    @Command
    public void exportReport() {
        // Calculate metrics
        long total = objectives.size();
        long achieved = objectives.stream().filter(o -> "ACHIEVED".equals(o.getAiostatus())).count();
        double achievementRate = total > 0 ? (double) achieved / total * 100 : 0;
        
        // Generate PDF/Excel (implementation depends on library used)
        // Map<String, Object> reportData = ...
        // exportService.generatePDF(reportData);
        
        Clients.showNotification("Report exported successfully", "info", null, "top_center", 3000);
    }
    
    @Command
    @NotifyChange({"objectives"})
    public void deleteObjective(@BindingParam("objective") AIMObjectives obj) {
        entityManager.remove(entityManager.merge(obj));
        loadObjectives();
        Clients.showNotification("Objective deleted", "info", null, "top_center", 3000);
    }
    
    private Timestamp calculateNextReview(String frequency) {
        Calendar cal = Calendar.getInstance();
        switch (frequency) {
            case "MONTHLY": cal.add(Calendar.MONTH, 1); break;
            case "QUARTERLY": cal.add(Calendar.MONTH, 3); break;
            case "ANNUAL": cal.add(Calendar.YEAR, 1); break;
            default: cal.add(Calendar.MONTH, 3);
        }
        return new Timestamp(cal.getTimeInMillis());
    }
    
    // Getters and Setters
    public List<AIMObjectives> getObjectives() {
        return objectives;
    }
    
    public void setObjectives(List<AIMObjectives> objectives) {
        this.objectives = objectives;
    }
    
    public AIMObjectives getSelectedObjective() {
        return selectedObjective;
    }
    
    public void setSelectedObjective(AIMObjectives selectedObjective) {
        this.selectedObjective = selectedObjective;
    }
    
    public AIMObjectives getNewObjective() {
        return newObjective;
    }
    
    public void setNewObjective(AIMObjectives newObjective) {
        this.newObjective = newObjective;
    }
    
    public double getAchievementRate() {
        if (objectives == null || objectives.isEmpty()) return 0;
        long achieved = objectives.stream().filter(o -> "ACHIEVED".equals(o.getAiostatus())).count();
        return (double) achieved / objectives.size() * 100;
    }
    
    public long getActiveCount() {
        if (objectives == null) return 0;
        return objectives.stream().filter(o -> "ACTIVE".equals(o.getAiostatus())).count();
    }
}
```

**Pantalla ZUL:**

Archivo: `console/zul/governance/ai_objectives_management.zul`

```xml
<window title="AI Objectives Management - ISO 42001" border="normal" 
        viewModel="@id('vm') @init('com.codeflowx.govern.viewmodel.governance.AIObjectivesViewModel')">
    
    <hlayout>
        <button label="New Objective" onClick="@command('createObjective')" />
        <button label="Export Report" onClick="@command('exportReport')" />
    </hlayout>
    
    <separator height="10px" />
    
    <!-- Dashboard KPIs -->
    <hlayout>
        <groupbox title="Achievement Rate" width="200px">
            <label value="@load(vm.achievementRate)" style="font-size: 24px; font-weight: bold;" />
            <label value="% Achieved" />
        </groupbox>
        
        <groupbox title="Active Objectives" width="200px">
            <label value="@load(vm.activeCount)" style="font-size: 24px; font-weight: bold;" />
            <label value="objectives" />
        </groupbox>
    </hlayout>
    
    <separator height="10px" />
    
    <!-- Lista objectives -->
    <listbox model="@load(vm.objectives)" selectedItem="@bind(vm.selectedObjective)" height="400px">
        <listhead>
            <listheader label="Category" width="120px" />
            <listheader label="Description" />
            <listheader label="Metric" width="200px" />
            <listheader label="Target" width="80px" />
            <listheader label="Current" width="80px" />
            <listheader label="Progress" width="120px" />
            <listheader label="Status" width="100px" />
            <listheader label="Responsible" width="120px" />
            <listheader label="Next Review" width="100px" />
            <listheader label="Actions" width="150px" />
        </listhead>
        <template name="model">
            <listitem>
                <listcell label="@load(each.aiocategory)" />
                <listcell label="@load(each.aiodescription)" />
                <listcell label="@load(each.aiometric)" />
                <listcell label="@load(each.aiotargetvalue)" />
                <listcell label="@load(each.aiocurrentvalue)" />
                <listcell>
                    <progressmeter value="@load((each.aiocurrentvalue / each.aiotargetvalue) * 100)" />
                </listcell>
                <listcell>
                    <label value="@load(each.aiostatus)" 
                           style="@load(each.aiostatus eq 'ACHIEVED' ? 'color: green;' : 'color: orange;')" />
                </listcell>
                <listcell label="@load(each.aioresponsible)" />
                <listcell label="@load(each.aionextreview) @converter('formattedDate', format='dd/MM/yyyy')" />
                <listcell>
                    <button label="Update" onClick="@command('updateProgress', id=each.idxaiobjective)" />
                    <button label="Review" onClick="@command('reviewObjective', id=each.idxaiobjective)" />
                </listcell>
            </listitem>
        </template>
    </listbox>
    
</window>
```

**Esfuerzo:** 2 días  
**Prioridad:** 🔴 CRÍTICA  
**Referencias:** ISO 42001 Clause 6.2

---

## PROMPT J1.2 - AI Competence Management

**REVISAR PRIMERO:** ¿Existe tabla USERS o EMPLOYEES con competencias?

**Entidad JSON:**

Archivo: `sources/json/tables/AICOMPETENCE.json`

```json
{
  "namespace": "governance",
  "name": "AICCOMPETENCE",
  "type": "TABLE",
  "labelMonitor": "aicpersonname",
  "description": "AI Competence Management según ISO 42001 Clause 7.2",
  "fields": [
    {
      "name": "idxaiccompetence",
      "type": "LONG",
      "pk": true,
      "required": false,
      "label": "ID Competence"
    },
    {
      "name": "iduuid",
      "type": "VARCHAR",
      "size": 36,
      "required": true,
      "label": "UUID"
    },
    {
      "name": "aicpersonname",
      "type": "VARCHAR",
      "size": 100,
      "required": true,
      "label": "Person Name"
    },
    {
      "name": "aicpersonemail",
      "type": "VARCHAR",
      "size": 100,
      "required": true,
      "label": "Email"
    },
    {
      "name": "aicrole",
      "type": "VARCHAR",
      "size": 50,
      "required": true,
      "label": "Role",
      "values": ["AI_DEVELOPER", "AI_ENGINEER", "DATA_SCIENTIST", "MLOPS", "QA", "COMPLIANCE", "MANAGER"]
    },
    {
      "name": "aicrequiredcompetencies",
      "type": "TEXT",
      "required": true,
      "label": "Required Competencies (JSON)"
    },
    {
      "name": "aiccurrentcompetencies",
      "type": "TEXT",
      "required": false,
      "label": "Current Competencies (JSON)"
    },
    {
      "name": "aicgapanalysis",
      "type": "TEXT",
      "required": false,
      "label": "Gap Analysis"
    },
    {
      "name": "aictrainingplan",
      "type": "TEXT",
      "required": false,
      "label": "Training Plan"
    },
    {
      "name": "aiclastawarenesstrained",
      "type": "TIMESTAMP",
      "required": false,
      "label": "Last Awareness Training"
    },
    {
      "name": "aicawarenesslevel",
      "type": "VARCHAR",
      "size": 20,
      "required": false,
      "label": "Awareness Level",
      "values": ["NONE", "BASIC", "INTERMEDIATE", "ADVANCED"]
    },
    {
      "name": "aiccertifications",
      "type": "TEXT",
      "required": false,
      "label": "Certifications (JSON)"
    },
    {
      "name": "aiccreatedat",
      "type": "TIMESTAMP",
      "required": true,
      "label": "Created At"
    }
  ]
}
```

**BusinessService:**

Class: `AICompetenceBusinessService`

Métodos:
- `assessCompetenceGap(personId)` - Gap analysis automático
- `createTrainingPlan(personId, gaps)` - Plan formación
- `recordAwarenessTraining(personId)` - Registro awareness
- `generateCompetenceReport()` - Reporte ISO 42001
- `getPersonsByRole(role)` - Filtro por rol
- `getPersonsDueTraining()` - Awareness vencido (>1 año)

**ViewModel + ZUL:**

Pantalla: `console/zul/governance/ai_competence_management.zul`

Funcionalidades:
- CRUD competencias por persona
- Gap analysis automático (required vs current)
- Training plan generator
- Awareness training scheduler
- Certificates repository
- Export competence matrix

**Esfuerzo:** 2 días  
**Prioridad:** 🔴 CRÍTICA

---

## PROMPT J1.3 - AI Training Records

**Entidad JSON:**

Archivo: `sources/json/tables/AITRAININGRECORD.json`

Campos:
- idxaittraining (PK)
- aitpersonid (FK → AICCOMPETENCE)
- aittrainingtype (AWARENESS, TECHNICAL, COMPLIANCE, CERTIFICATION)
- aittrainingtitle
- aittrainingdate
- aitduration (horas)
- aitprovider
- aitcertificateobtained (BOOLEAN)
- aitcertificateurl

**BusinessService:**

Class: `AITrainingBusinessService`

Métodos:
- `recordTraining(training)` - Registrar training
- `getTrainingsByPerson(personId)` - Historial persona
- `getCertificates(personId)` - Certificados activos
- `generateTrainingReport()` - Reporte compliance training

**ViewModel + ZUL:**

Pantalla: Integrada en `ai_competence_management.zul` (pestaña Training History)

**Esfuerzo:** 1 día  
**Prioridad:** 🔴 CRÍTICA

---

## PROMPT J1.4 - AI System Inventory Extension

**REVISAR PRIMERO:** Tabla PRJPROJECTS existente

**Modificación:** ALTER TABLE existente

```sql
ALTER TABLE PRJPROJECTS 
ADD COLUMN prjaisystemtype VARCHAR(50),  -- MODEL, AGENT, RAG, PROMPT, HYBRID
ADD COLUMN prjailifecyclestage VARCHAR(50) DEFAULT 'DESIGN',  -- DESIGN, DEVELOPMENT, TESTING, DEPLOYMENT, MONITORING, DECOMMISSIONED
ADD COLUMN prjairisklevel VARCHAR(20),  -- HIGH_RISK, LIMITED_RISK, MINIMAL_RISK, PROHIBITED
ADD COLUMN prjaipurpose TEXT,
ADD COLUMN prjaiintendeduse TEXT,
ADD COLUMN prjaiusers TEXT,
ADD COLUMN prjaicontrolsapplied TEXT,  -- JSON array ISO 42001 Annex A controls
ADD COLUMN prjaiperformancemetrics TEXT,  -- JSON metrics
ADD COLUMN prjailastinventoryreview TIMESTAMP,
ADD COLUMN prjainextinventoryreview TIMESTAMP;

CREATE INDEX idx_prj_aisystemtype ON PRJPROJECTS(prjaisystemtype);
CREATE INDEX idx_prj_ailifecyclestage ON PRJPROJECTS(prjailifecyclestage);
CREATE INDEX idx_prj_airisklevel ON PRJPROJECTS(prjairisklevel);
```

**BusinessService Extension:**

Class: `ProjectBusinessService` (extender existente)

Nuevos métodos:
- `updateAIInventoryMetadata(projectId, metadata)` - Actualizar metadata ISO 42001
- `reviewAISystemInventory(projectId)` - Marcar revisado
- `generateAIInventoryReport()` - Reporte inventario completo
- `getSystemsByLifecycleStage(stage)` - Filtrar por etapa
- `getSystemsByRiskLevel(risk)` - Filtrar por riesgo

**ViewModel + ZUL Extension:**

Extender: `console/zul/projects/project_detail.zul`

Nueva pestaña: "ISO 42001 Inventory"

Campos adicionales en form + Export inventory Excel/PDF

**Esfuerzo:** 1.5 días  
**Prioridad:** 🔴 MUY CRÍTICA

---

---

## ⚠️ PROMPTS J1.2 - J1.8 (RESUMIDOS - MISMO PATRÓN)

**TODOS siguen mismo patrón que J1.1:**
1. Entity JPA directamente (@Entity, @Table, @Column)
2. Migration SQL (CREATE TABLE)
3. ViewModel ZKoss (EntityManager directo, sin BusinessService)
4. Pantalla ZUL

**NO crear:** JSON entities, BusinessServices, Controllers

---

*[J1.2 - J1.8 resumidos por espacio - patrones similares a J1.1]*

**J1.2:** AI Competence (AICCOMPETENCE entity JPA + ViewModel + ZUL) - 2 días  
**J1.3:** AI Training Records (AITTRAININGRECORD entity JPA + ViewModel + ZUL) - 1 día  
**J1.4:** AI System Inventory Extension (ALTER TABLE PRJPROJECTS + ViewModel extension) - 1.5 días  
**J1.5:** AIMS Performance (APSAIMSPERFORMANCE entity JPA + ViewModel + ZUL) - 2 días  
**J1.6:** AIMS Non-Conformity (ANCNONCONFORMITY entity JPA + ViewModel + ZUL + corrective actions) - 2 días  
**J1.7:** AIMS Improvement (AIMPROVEMENT entity JPA + ViewModel + ZUL + tracking) - 2 días  
**J1.8:** ISO 42001 Controls (ICOISO42001CONTROL entity JPA + 39 controles seed data + ViewModel + ZUL) - 2 días

---

# GRUPO J2: ISO 38507 ENTITIES (8 prompts)

**⚠️ MISMO PATRÓN:** Entity JPA + ViewModel + ZUL (sin BusinessService, sin Controllers)

**J2.1:** Board Report (BRDBOARDREPORT entity JPA + ViewModel + ZUL dashboard EDM) - 2 días  
**J2.2:** Ethics Committee (ETHCOMMITTEE entity JPA + ViewModel + ZUL decisions) - 2 días  
**J2.3:** Stakeholder Management (STKSTAKEHOLDER entity JPA + ViewModel + ZUL engagement) - 2 días  
**J2.4:** AI Strategic Plan (STRSTRATEGICPLAN entity JPA + ViewModel + ZUL objectives) - 2 días  
**J2.5:** Board Decision (BDCBOARDDECISION entity JPA + ViewModel + ZUL audit trail) - 1.5 días  
**J2.6:** AI Investment (AIVINVESTMENT entity JPA + ViewModel + ZUL portfolio) - 2 días  
**J2.7:** Third-Party AI Risk (TPARISK entity JPA + ViewModel + ZUL assessment) - 2 días  
**J2.8:** AI Sustainability (SUSTSUSTAINABILITY entity JPA + ViewModel + ZUL CO2/energy) - 2 días

---

# GRUPO J3: OECD + GDPR ENTITIES (6 prompts)

**⚠️ MISMO PATRÓN:** Entity JPA + ViewModel + ZUL (sin BusinessService, sin Controllers)

**J3.1:** OECD Sustainability (OECSUSTAINABILITY entity JPA + ViewModel + ZUL metrics) - 1.5 días  
**J3.2:** OECD Stakeholder Report (OECSTKREPORT entity JPA + ViewModel + ZUL transparency) - 1.5 días  
**J3.3:** GDPR Privacy Notice (GPNPRIVACYNOTICE entity JPA + ViewModel + ZUL auto-generate) - 2 días  
**J3.4:** GDPR Data Subject Request (GDSREQUEST entity JPA + ViewModel + ZUL workflow) - 2 días  
**J3.5:** GDPR ROPA (GROPA entity JPA + ViewModel + ZUL auto-generate) - 2 días  
**J3.6:** OECD Human Rights (OHRHUMANRIGHTS entity JPA + ViewModel + ZUL FRIA extension) - 2 días

---

## 📊 RESUMEN JAVA

| Grupo | Entities JPA | ViewModels | ZUL | Esfuerzo |
|-------|--------------|------------|-----|----------|
| **J1 ISO 42001** | 8 | 8 | 8 | 14 días |
| **J2 ISO 38507** | 8 | 8 | 8 | 13 días |
| **J3 OECD+GDPR** | 6 | 6 | 6 | 9 días |
| **TOTAL** | **22** | **22** | **22** | **36 días** |

**NOTA:** BusinessServices y Controllers se crearán DESPUÉS para API/SDK (Fase separada).

---

## ✅ CHECKLIST IMPLEMENTACIÓN JAVA

Para cada prompt:

1. **REVISAR CÓDIGO EXISTENTE:**
   - [ ] Buscar entidad JPA similar existente (`entity/`)
   - [ ] Buscar ViewModel con funcionalidad parecida (`viewmodel/`)
   - [ ] Buscar pantalla ZUL relacionada (`console/zul/`)

2. **SI NO EXISTE:**
   - [ ] Crear Entity JPA directamente (@Entity, @Table, @Column)
   - [ ] Crear Migration SQL (CREATE TABLE)
   - [ ] Crear ViewModel ZKoss (acceso directo EntityManager)
   - [ ] Crear ZUL (console/zul/)
   - [ ] Testing Entity + ViewModel + ZUL

3. **SI EXISTE PARCIALMENTE:**
   - [ ] Extender entity existente (ALTER TABLE + nuevos @Column)
   - [ ] Extender ViewModel (nuevos @Command)
   - [ ] Añadir pestaña/sección ZUL existente

4. **POST-IMPLEMENTACIÓN:**
   - [ ] Actualizar documentación
   - [ ] Crear datos seed si aplica
   - [ ] Integrar con workflows BPMN (si aplica)
   - [ ] Testing completo CRUD

**IMPORTANTE:**
- NO crear BusinessServices (se hará en fase API/SDK después)
- NO crear REST Controllers (se hará en fase API/SDK después)
- ViewModels acceden a entidades directamente con EntityManager o DAO EnArt (si existe)

---

## 🚀 PRÓXIMOS PASOS

1. **AHORA:** Revisar este documento Java
2. **ASIGNAR CHAT:** Este documento a chat especializado Java/JPA/ZKoss
3. **CHAT REVISA:** Contra código existente en:
   - `entity/` (Entities JPA)
   - `viewmodel/` (ViewModels ZKoss)
   - `console/zul/` (Pantallas ZUL)
4. **CHAT REPORTA:** Qué existe, qué falta, qué extender
5. **IMPLEMENTAR:** Solo lo que falta, evitar duplicados

**RECORDAR AL CHAT:**
- ❌ NO crear JSON entities
- ❌ NO crear BusinessServices (fase API/SDK después)
- ❌ NO crear REST Controllers (fase API/SDK después)
- ✅ SÍ crear Entity JPA directamente
- ✅ SÍ crear ViewModel con EntityManager directo
- ✅ SÍ crear pantallas ZUL

---

**Documento completo Java (corregido sin BusinessServices). Siguiente: PROMPTS_09_BPMN + PROMPTS_10_PYTHON**

