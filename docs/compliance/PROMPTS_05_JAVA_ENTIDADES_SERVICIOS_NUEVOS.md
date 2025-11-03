# PROMPTS - CREACIÓN NUEVAS ENTIDADES Y SERVICIOS JAVA
## EU AI ACT COMPLIANCE - Nuevas Tablas EnArt, BusinessServices y ViewModels ZKoss

**Equipo:** Java Team - Nuevas Entidades  
**Fecha:** 15 de noviembre de 2025  
**Objetivo:** Crear nuevas entidades EnArt, BusinessServices y ViewModels ZKoss para funcionalidades EU AI Act  
**Esfuerzo Estimado:** 12-15 días (con 3-4 chats en paralelo)

---

## 🏗️ ARQUITECTURA ENART (NO HIBERNATE ESTÁNDAR)

### **Stack Tecnológico:**
```
❌ NO USAR:
- Hibernate JPA estándar
- Spring Data JpaRepository
- @Entity JPA estándar
- REST Controllers (eso es para SDK después)

✅ SÍ USAR:
- Framework EnArt (propio)
- Entidades JSON (archivos .json)
- BusinessService (Spring @Service)
- ViewModel ZKoss (MVVM pattern)
- Generador Java entities desde JSON
```

### **Flujo Arquitectura EnArt:**
```
1. Definir Entity en JSON (archivo .json)
2. Generador Python crea Entity.java automáticamente
3. BusinessService usa Entity (con DAO EnArt)
4. ViewModel ZKoss usa BusinessService
5. Pantalla ZUL binds a ViewModel
```

---

## ⚠️ IMPORTANTE - CÓMO LEER ESTOS PROMPTS

**Los prompts A.2, A.3, B.1, B.2 contienen código JPA de EJEMPLO para entender campos necesarios.**

**PERO debes implementar siguiendo el patrón EnArt del Prompt A.1:**
1. ✅ Crear JSON EnArt (como A.1)
2. ✅ Ejecutar generador Python
3. ✅ BusinessService con DAO (NO Repository)
4. ✅ ViewModel ZKoss
5. ❌ NO crear Repository
6. ❌ NO crear REST Controllers

**El Prompt A.1 es el PATRÓN CORRECTO. Los demás prompts muestran QUÉ campos necesitan, NO CÓMO implementar.**

---

## 🏗️ CONVENCIONES Y ESTÁNDARES

### **Nomenclatura ENART (OBLIGATORIO):**

**User Rules aplicables:**
> "La definición de tablas debe contener un prefijo de tres caracteres asociado al módulo funcional (ejemplo: proyectos prj_, core cor_), siempre una PK autonumérica, definición de modelos en tercera forma normal, y aplicar siempre KISS y reglas SOLID y arquitectura hexagonal."

```
TABLAS: 
- Formato: PREFIJO (3 chars) + NOMBRE (MAYÚSCULAS, sin guiones bajos)
- Ejemplo: COMCOMPLIANCEASSESSMENTS, FRIAFUNDAMENTALRIGHTSASSESSMENTS

COLUMNAS:
- Formato: PREFIJO + NOMBRE
- Ejemplo: COMASSESSMENTDATE, COMCOMPLIANCESCORE

PRIMARY KEY:
- Formato: IDX + NOMBRE_SINGULAR
- Ejemplo: IDXCOMPLIANCEASSESSMENT
- Tipo: BIGSERIAL (autonumérico)

UUID:
- Todas las tablas: campo "iduuid" (VARCHAR 36, UNIQUE)

FOREIGN KEYS:
- Formato: IDX + ENTIDAD_REFERENCIADA
- Ejemplo: IDXPROJECT, IDXMODEL, IDXUSER
```

### **Prefijos Módulos Funcionales:**

```
COM - Compliance
FRIA - Fundamental Rights Impact Assessment
IML - Immutable Logs
COR - Corrective Actions
REG - Registration (EU Database)
ANN - Annex (para catalogos Anexo III, etc.)
QMS - Quality Management System
ADV - Adversarial (tests, robustness)
DOC - Documentation
```

---

## 📋 GRUPOS DE PROMPTS

### **GRUPO A: ENTIDADES COMPLIANCE CORE (4-5 días)**

---

### **PROMPT A.1 - Entidad ComplianceAssessment (EnArt)**

**Objetivo:** Entidad para almacenar evaluaciones de conformidad (Art. 43, Anexo VI).

**Prompt Específico:**

```
Necesito CREAR entidad EnArt ComplianceAssessment para almacenar evaluaciones de conformidad según Anexo VI del EU AI Act.

ESPECIFICACIONES:
- Framework: EnArt (NO Hibernate/JPA estándar)
- Namespace: compliance
- Tabla: COMCOMPLIANCEASSESSMENTS
- Prefijo: COM
- 3FN: Tercera Forma Normal
- Arquitectura: EnArt + BusinessService + ViewModel ZKoss

PASO 1 - CREAR JSON EnArt:

Ubicación: sources/json/tables/COMCOMPLIANCEASSESSMENTS.json

{
  "namespace": "compliance",
  "name": "COMCOMPLIANCEASSESSMENTS",
  "type": "TABLE",
  "labelMonitor": "comassessmenttype",
  "description": "Evaluaciones de conformidad según Anexo VI - Art. 43 EU AI Act",
  "fields": [
    {
      "name": "idxcomplianceassessment",
      "type": "LONG",
      "pk": true,
      "required": false,
      "label": "ID Assessment",
      "criteria": true,
      "filter": true
    },
    {
      "name": "iduuid",
      "type": "VARCHAR",
      "size": 36,
      "required": true,
      "label": "UUID",
      "criteria": true,
      "filter": true
    },
    {
      "name": "idxproject",
      "type": "LONG",
      "required": true,
      "label": "Proyecto",
      "criteria": true,
      "filter": true,
      "fk": {
        "table": "PRJPROJECTS",
        "field": "IDXPROJECT"
      }
    },
    {
      "name": "idxuser",
      "type": "LONG",
      "required": false,
      "label": "Usuario Iniciador",
      "criteria": true,
      "filter": true,
      "fk": {
        "table": "CORUSERS",
        "field": "IDXUSER"
      }
    },
    {
      "name": "comassessmenttype",
      "type": "VARCHAR",
      "size": 30,
      "required": true,
      "label": "Tipo Assessment",
      "criteria": true,
      "filter": true,
      "values": ["SELF_ASSESSMENT", "NOTIFIED_BODY"]
    },
    {
      "name": "comassessmentdate",
      "type": "TIMESTAMP",
      "required": true,
      "label": "Fecha Assessment",
      "criteria": true,
      "filter": true
    },
    {
      "name": "comannexvicompliant",
      "type": "BOOLEAN",
      "required": true,
      "label": "Anexo VI Compliant",
      "criteria": true,
      "filter": true
    },
    {
      "name": "comstep2qmsscore",
      "type": "DECIMAL",
      "required": false,
      "label": "Score QMS Step 2",
      "criteria": true,
      "filter": true,
      "comment": "Score 0.00-1.00"
    },
    {
      "name": "comstep3docscore",
      "type": "DECIMAL",
      "required": false,
      "label": "Score Doc Step 3",
      "criteria": true,
      "filter": true
    },
    {
      "name": "comstep4consistencyscore",
      "type": "DECIMAL",
      "required": false,
      "label": "Score Consistency Step 4",
      "criteria": true,
      "filter": true
    },
    {
      "name": "comoverallscore",
      "type": "DECIMAL",
      "required": false,
      "label": "Score Overall",
      "criteria": true,
      "filter": true
    },
    {
      "name": "comgaps",
      "type": "JSONB",
      "required": false,
      "label": "Gaps Detectados",
      "criteria": false,
      "filter": false,
      "comment": "Array de gaps: ['gap1', 'gap2']"
    },
    {
      "name": "comresults",
      "type": "JSONB",
      "required": false,
      "label": "Resultados Detallados",
      "criteria": false,
      "filter": false
    },
    {
      "name": "comreadyforcertification",
      "type": "BOOLEAN",
      "required": false,
      "label": "Listo para Certificación",
      "criteria": true,
      "filter": true
    },
    {
      "name": "comcertificateid",
      "type": "VARCHAR",
      "size": 100,
      "required": false,
      "label": "ID Certificado",
      "criteria": true,
      "filter": true
    },
    {
      "name": "comreporturl",
      "type": "VARCHAR",
      "size": 500,
      "required": false,
      "label": "URL Reporte",
      "criteria": false,
      "filter": false
    },
    {
      "name": "comcreatedat",
      "type": "TIMESTAMP",
      "required": true,
      "label": "Fecha Creación",
      "criteria": true,
      "filter": true
    },
    {
      "name": "comupdatedat",
      "type": "TIMESTAMP",
      "required": false,
      "label": "Fecha Actualización",
      "criteria": true,
      "filter": true
    }
  ]
}

PASO 2 - GENERAR Entity.java (Automático con generador Python):

Ejecutar generador:
python src/generators/java_entity_generator.py sources/json/tables/COMCOMPLIANCEASSESSMENTS.json

Genera automáticamente:
sources/jpa/compliance/ComplianceAssessment.java

PASO 3 - CREAR BusinessService:

package com.codeflowx.govern.business.compliance;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.enartframework.nocode.datamodel.dao.DAO;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ComplianceAssessmentBusinessService {
    
    @Autowired
    private DAO dao;
    
    @Autowired
    private QualityManagementSystemBusinessService qmsBusinessService;
    
    @Autowired
    private TechnicalDocumentationBusinessService docBusinessService;
    
    /**
     * Crea nueva evaluación de conformidad
     */
    public ComplianceAssessment createAssessment(Long projectId, String assessmentType) {
        ComplianceAssessment assessment = new ComplianceAssessment();
        assessment.setIdxproject(projectId);
        assessment.setComassessmenttype(assessmentType);
        assessment.setComassessmentdate(new Timestamp(System.currentTimeMillis()));
        assessment.setComannexvicompliant(false);
        assessment.setComcreatedat(new Timestamp(System.currentTimeMillis()));
        assessment.setIduuid(UUID.randomUUID().toString());
        
        dao.insert(assessment);
        return assessment;
    }
    
    /**
     * Ejecuta step 2 - QMS compliance check
     */
    public void executeStep2QmsCheck(Long assessmentId) {
        ComplianceAssessment assessment = dao.findById(ComplianceAssessment.class, assessmentId);
        BigDecimal qmsScore = qmsBusinessService.calculateQmsComplianceScore(assessment.getIdxproject());
        assessment.setComstep2qmsscore(qmsScore);
        dao.update(assessment);
    }
    
    /**
     * Ejecuta step 3 - Technical doc review
     */
    public void executeStep3DocReview(Long assessmentId) {
        ComplianceAssessment assessment = dao.findById(ComplianceAssessment.class, assessmentId);
        BigDecimal docScore = docBusinessService.calculateDocumentationScore(assessment.getIdxproject());
        assessment.setComstep3docscore(docScore);
        dao.update(assessment);
    }
    
    /**
     * Calcula score overall
     */
    public BigDecimal calculateOverallScore(Long assessmentId) {
        ComplianceAssessment assessment = dao.findById(ComplianceAssessment.class, assessmentId);
        BigDecimal avg = (assessment.getComstep2qmsscore()
            .add(assessment.getComstep3docscore())
            .add(assessment.getComstep4consistencyscore()))
            .divide(new BigDecimal(3));
        assessment.setComoverallscore(avg);
        dao.update(assessment);
        return avg;
    }
    
    /**
     * Obtiene latest assessment de proyecto
     */
    public ComplianceAssessment getLatestAssessment(Long projectId) {
        String query = "SELECT * FROM COMCOMPLIANCEASSESSMENTS WHERE IDXPROJECT = ? ORDER BY COMASSESSMENTDATE DESC LIMIT 1";
        return dao.findBySQL(ComplianceAssessment.class, query, projectId);
    }
}

PASO 4 - CREAR ViewModel ZKoss:

VIEWMODEL (ZKoss - para UI interna):

package com.codeflowx.govern.viewmodel.compliance;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

@VariableResolver(DelegatingVariableResolver.class)
public class ComplianceAssessmentDetailViewModel extends SelectorComposer<Component> {
    
    @WireVariable
    private ComplianceAssessmentBusinessService complianceAssessmentBusinessService;
    
    private ComplianceAssessment currentAssessment;
    private Project project;
    
    @Listen("onCreate = #winComplianceAssessment")
    public void doAfterCompose(Component comp) {
        // Cargar assessment si existe
        currentAssessment = complianceAssessmentBusinessService.getLatestAssessment(project.getId());
    }
    
    @Listen("onClick = #btnExecuteStep2")
    public void executeStep2() {
        complianceAssessmentBusinessService.executeStep2QmsCheck(currentAssessment.getId());
        refreshView();
    }
    
    @Listen("onClick = #btnExecuteStep3")
    public void executeStep3() {
        complianceAssessmentBusinessService.executeStep3DocReview(currentAssessment.getId());
        refreshView();
    }
    
    // ... métodos adicionales
}

ZUL (pantalla):
compliance_assessment_detail.zul - Para CRUD y gestión compliance assessments

PASO 5 - CREAR Migration SQL:

CREATE TABLE COMCOMPLIANCEASSESSMENTS (
    IDXCOMPLIANCEASSESSMENT BIGSERIAL PRIMARY KEY,
    iduuid VARCHAR(36) UNIQUE NOT NULL,
    IDXPROJECT BIGINT NOT NULL,
    IDXUSER BIGINT,
    COMASSESSMENTTYPE VARCHAR(30) NOT NULL,
    COMASSESSMENTDATE TIMESTAMP NOT NULL,
    COMANNEXVICOMPLIANT BOOLEAN NOT NULL DEFAULT FALSE,
    COMSTEP2QMSSCORE NUMERIC(5,2),
    COMSTEP3DOCSCORE NUMERIC(5,2),
    COMSTEP4CONSISTENCYSCORE NUMERIC(5,2),
    COMOVERALLSCORE NUMERIC(5,2),
    COMGAPS JSONB,
    COMRESULTS JSONB,
    COMREADYFORCERTIFICATION BOOLEAN DEFAULT FALSE,
    COMCERTIFICATEID VARCHAR(100),
    COMREPORTURL VARCHAR(500),
    COMCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    COMUPDATEDAT TIMESTAMP
);

CREATE INDEX idx_com_project ON COMCOMPLIANCEASSESSMENTS(IDXPROJECT);
CREATE INDEX idx_com_type ON COMCOMPLIANCEASSESSMENTS(COMASSESSMENTTYPE);

ARQUITECTURA COMPLETA:
1. JSON EnArt → 2. Generador Python → 3. Entity.java → 4. BusinessService → 5. ViewModel ZKoss → 6. Pantalla ZUL

NO CREAR en esta fase:
- Repository (EnArt usa DAO)
- Controllers REST (para SDK después)

DTO (solo si necesario para ViewModel):

package com.codeflowx.govern.dto.compliance;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ComplianceAssessmentDTO {
    private Long id;
    private String uuid;
    private Long projectId;
    private String projectName;
    private String assessmentType;
    private LocalDateTime assessmentDate;
    private Boolean annexVICompliant;
    private BigDecimal step2QmsScore;
    private BigDecimal step3DocScore;
    private BigDecimal step4ConsistencyScore;
    private BigDecimal overallScore;
    private java.util.List<String> gaps;
    private Boolean readyForCertification;
    private String reportUrl;
}

MIGRATION SQL (Liquibase/Flyway):

CREATE TABLE COMCOMPLIANCEASSESSMENTS (
    IDXCOMPLIANCEASSESSMENT BIGSERIAL PRIMARY KEY,
    iduuid VARCHAR(36) UNIQUE NOT NULL,
    IDXPROJECT BIGINT NOT NULL REFERENCES PROJECT(IDXPROJECT),
    IDXUSER BIGINT REFERENCES USERS(IDXUSER),
    COMASSESSMENTTYPE VARCHAR(30) NOT NULL,
    COMASSESSMENTDATE TIMESTAMP NOT NULL,
    COMANNEXVICOMPLIANT BOOLEAN NOT NULL DEFAULT FALSE,
    COMSTEP2QMSSCORE NUMERIC(3,2),
    COMSTEP3DOCSCORE NUMERIC(3,2),
    COMSTEP4CONSISTENCYSCORE NUMERIC(3,2),
    COMOVERALLSCORE NUMERIC(3,2),
    COMGAPS JSONB,
    COMRESULTS JSONB,
    COMREADYFORCERTIFICATION BOOLEAN DEFAULT FALSE,
    COMCERTIFICATEID VARCHAR(100),
    COMREPORTURL VARCHAR(500),
    COMCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    COMUPDATEDAT TIMESTAMP
);

CREATE INDEX idx_com_project ON COMCOMPLIANCEASSESSMENTS(IDXPROJECT);
CREATE INDEX idx_com_type ON COMCOMPLIANCEASSESSMENTS(COMASSESSMENTTYPE);
CREATE INDEX idx_com_date ON COMCOMPLIANCEASSESSMENTS(COMASSESSMENTDATE);

SEGUIR:
- Nomenclatura ENART estricta
- 3FN
- SOLID
- Arquitectura hexagonal (entity, repository, service, controller separados)
```

**Artículos Cubiertos:** Art. 43, Anexo VI  
**Esfuerzo:** 2 días  
**Prioridad:** 🔴 Crítica

---

### **PROMPT A.2 - Entidad FriaAssessment.java**

**Objetivo:** Entidad para FRIA (Fundamental Rights Impact Assessment) según Art. 27.

**Prompt Específico:**

```
Necesito CREAR entidad JPA FriaAssessment.java para almacenar Evaluaciones de Impacto en Derechos Fundamentales según Art. 27.

ESPECIFICACIONES:
- Package: com.codeflowx.govern.entity.compliance
- Tabla: FRIAFUNDAMENTALRIGHTSASSESSMENTS
- Prefijo: FRIA

DEFINICIÓN ENTIDAD:

@Entity
@Table(name = "FRIAFUNDAMENTALRIGHTSASSESSMENTS")
@Getter
@Setter
@NoArgsConstructor
public class FriaAssessment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXFRIAASSESSMENT")
    private Long id;
    
    @Column(name = "iduuid", unique = true, nullable = false, length = 36)
    private String uuid;
    
    // FKs
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDXPROJECT", nullable = false)
    private Project project;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDXUSER")  // Responsable despliegue que hizo FRIA
    private User deployerUser;
    
    // Art. 27.1.a - Descripción procesos
    @Column(name = "FRIAPROCESSDESCRIPTION", columnDefinition = "TEXT", nullable = false)
    private String processDescription;
    
    // Art. 27.1.b - Período y frecuencia
    @Column(name = "FRIAUSAGEPERIOD", length = 200)
    private String usagePeriod;
    
    @Column(name = "FRIAUSAGEFREQUENCY", length = 200)
    private String usageFrequency;
    
    // Art. 27.1.c - Categorías personas afectadas
    @Column(name = "FRIAAFFECTEDCATEGORIES", columnDefinition = "JSONB")
    @Type(JsonBinaryType.class)
    private java.util.List<String> affectedCategories;
    
    @Column(name = "FRIAVULNERABLEGROUPSINCLUDED")
    private Boolean vulnerableGroupsIncluded = false;
    
    // Art. 27.1.d - Riesgos específicos
    @Column(name = "FRIARISKS", columnDefinition = "JSONB", nullable = false)
    @Type(JsonBinaryType.class)
    private java.util.List<FriaRisk> risks;  // Custom type
    
    // Art. 27.1.e - Supervisión humana
    @Column(name = "FRIAHUMANOVERSIGHT", columnDefinition = "TEXT", nullable = false)
    private String humanOversightDescription;
    
    @Column(name = "FRIAHITLENABLED")
    private Boolean hitlEnabled = false;
    
    // Art. 27.1.f - Medidas mitigación
    @Column(name = "FRIAMITIGATION MEASURES", columnDefinition = "JSONB", nullable = false)
    @Type(JsonBinaryType.class)
    private java.util.List<MitigationMeasure> mitigationMeasures;
    
    // Compliance y Quality
    @Column(name = "FRIAART27COMPLIANT")
    private Boolean art27Compliant = false;
    
    @Column(name = "FRIACOMPLETENESS SCORE", precision = 3, scale = 2)
    private BigDecimal completenessScore;
    
    @Column(name = "FRIAQUALSCORE", precision = 3, scale = 2)
    private BigDecimal qualityScore;
    
    // Charter UE Analysis
    @Column(name = "FRIACHARTERARTICLES", columnDefinition = "JSONB")
    @Type(JsonBinaryType.class)
    private java.util.List<String> charterArticlesAffected;  // ["Art. 3", "Art. 21", ...]
    
    @Column(name = "FRIAIMPACTSEVERITY", length = 20)
    private String impactSeverity;  // LOW, MEDIUM, HIGH, CRITICAL
    
    // Notificación Autoridad (Art. 27.3)
    @Column(name = "FRIANOTIFIED")
    private Boolean authorityNotified = false;
    
    @Column(name = "FRIANOTIFICATIONID", length = 100)
    private String notificationId;
    
    @Column(name = "FRIANOTIFICATIONDATE")
    private LocalDateTime notificationDate;
    
    // Documentación generada
    @Column(name = "FRIADOCUMENTURL", length = 500)
    private String documentUrl;
    
    @Column(name = "FRIADOCUMENTVERSION", length = 20)
    private String documentVersion;
    
    // Integración DPIA (Art. 27.4)
    @Column(name = "FRIADPIAINTEGRATED")
    private Boolean dpiaIntegrated = false;
    
    @Column(name = "FRIADPIAID", length = 100)
    private String dpiaId;  // FK a DPIA si existe
    
    // Aprobación
    @Column(name = "FRIAAPPROVED")
    private Boolean approved = false;
    
    @Column(name = "FRIAAPPROVEDBY")
    private Long approvedBy;  // FK User
    
    @Column(name = "FRIAAPPROVALDATE")
    private LocalDateTime approvalDate;
    
    // Audit
    @Column(name = "FRIACREATEDAT", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "FRIAUPDATEDAT")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (uuid == null) uuid = java.util.UUID.randomUUID().toString();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

CLASES AUXILIARES (en mismo package):

@Embeddable
@Data
public class FriaRisk {
    private String riskDescription;
    private String affectedGroup;
    private String severity;  // HIGH, MEDIUM, LOW
    private String probability;
    private String impact;
}

@Embeddable
@Data
public class MitigationMeasure {
    private String measureType;  // PREVENTIVE, DETECTIVE, CORRECTIVE
    private String description;
    private String responsible;
    private LocalDateTime dueDate;
    private String status;  // PLANNED, IN_PROGRESS, COMPLETED
}

REPOSITORY + SERVICE + CONTROLLER:
- Crear con patrón similar a ComplianceAssessment (Prompt A.1)
- Métodos:
  - createFria(projectId, deployerUserId)
  - updateFriaSection(friaId, section, data)
  - calculateCompletenessScore(friaId)
  - submitToAuthority(friaId)
  - integateWithDpia(friaId, dpiaId)

MIGRATION SQL:
- Crear tabla FRIAFUNDAMENTALRIGHTSASSESSMENTS
- Índices: projectId, uuid, notified, approved
- FK constraints
```

**Artículos Cubiertos:** Art. 27 (completo - 6 elementos)  
**Esfuerzo:** 2-3 días  
**Prioridad:** 🔴 Crítica

---

### **PROMPT A.3 - Entidad ImmutableLog.java**

**Objetivo:** Entidad para logs inmutables con hash chains según Art. 19.

**Prompt Específico:**

```
Necesito CREAR entidad JPA ImmutableLog.java para logs inmutables con cryptographic hash chains según Art. 19 EU AI Act.

ESPECIFICACIONES:
- Package: com.codeflowx.govern.entity.logging
- Tabla: IMLIMMUTABLELOGS
- Prefijo: IML
- CRÍTICO: Tabla APPEND-ONLY (nunca UPDATE ni DELETE)

DEFINICIÓN ENTIDAD:

@Entity
@Table(name = "IMLIMMUTABLELOGS", indexes = {
    @Index(name = "idx_iml_entity", columnList = "IMLENTITYTYPE,IMLENTITYID"),
    @Index(name = "idx_iml_timestamp", columnList = "IMLTIMESTAMP"),
    @Index(name = "idx_iml_hash", columnList = "IMLCURRENTHASH")
})
@Getter
@Setter(AccessLevel.PRIVATE)  // Solo private setters - immutabilidad
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImmutableLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXIMMUTABLELOG")
    private Long id;
    
    @Column(name = "iduuid", unique = true, nullable = false, length = 36)
    private String uuid;
    
    // Hash Chain (blockchain-style)
    @Column(name = "IMLPREVIOUSHASH", length = 64, nullable = false)
    private String previousHash;  // SHA-256 del log anterior
    
    @Column(name = "IMLCURRENTHASH", length = 64, unique = true, nullable = false)
    private String currentHash;  // SHA-256 de este log
    
    // Timestamp
    @Column(name = "IMLTIMESTAMP", nullable = false)
    private LocalDateTime timestamp;
    
    @Column(name = "IMLTIMESTAMPEPOCH", nullable = false)
    private Long timestampEpoch;  // Unix timestamp para sorting
    
    // Entity logging (qué se loggea)
    @Column(name = "IMLENTITYTYPE", length = 50, nullable = false)
    private String entityType;  // PROJECT, MODEL, EVALUATION, AGENT, etc.
    
    @Column(name = "IMLENTITYID", nullable = false)
    private Long entityId;
    
    // Action
    @Column(name = "IMLACTION", length = 100, nullable = false)
    private String action;  // CREATE, UPDATE, DELETE, EVALUATE, APPROVE, DEPLOY, etc.
    
    // User
    @Column(name = "IMLUSERID", nullable = false)
    private Long userId;
    
    @Column(name = "IMLUSERNAME", length = 100)
    private String userName;  // Desnormalizado para inmutabilidad
    
    // Data (snapshot completo)
    @Column(name = "IMLDATA", columnDefinition = "JSONB", nullable = false)
    @Type(JsonBinaryType.class)
    private java.util.Map<String, Object> data;
    
    // Integrity Verification
    @Column(name = "IMLVERIFIED")
    private Boolean verified = false;
    
    @Column(name = "IMLINTEGRITYSTATUS", length = 20)
    private String integrityStatus;  // VALID, TAMPERED, UNVERIFIED, PENDING
    
    @Column(name = "IMLLASTVERIFICATIONDATE")
    private LocalDateTime lastVerificationDate;
    
    // Optional: External Timestamp (RFC 3161)
    @Column(name = "IMLEXTERNALTIMESTAMP", columnDefinition = "TEXT")
    private String externalTimestamp;  // Blockchain/TSA proof
    
    // Metadata
    @Column(name = "IMLIPADDRESS", length = 45)
    private String ipAddress;
    
    @Column(name = "IMLUSERAGENT", length = 200)
    private String userAgent;
    
    // NUNCA se modifica después de creación
    @Column(name = "IMLCREATEDAT", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        timestamp = LocalDateTime.now();
        timestampEpoch = System.currentTimeMillis();
        if (uuid == null) uuid = java.util.UUID.randomUUID().toString();
    }
    
    // NO @PreUpdate - logs NUNCA se actualizan
}

REPOSITORY:

@Repository
public interface ImmutableLogRepository extends JpaRepository<ImmutableLog, Long> {
    
    // Obtener último log (para hash chain)
    Optional<ImmutableLog> findTopByOrderByIdDesc();
    
    // Logs por entidad
    List<ImmutableLog> findByEntityTypeAndEntityIdOrderByTimestampEpochAsc(String entityType, Long entityId);
    
    // Logs en rango (para verificación)
    List<ImmutableLog> findByIdBetweenOrderByIdAsc(Long startId, Long endId);
    
    // Logs no verificados
    List<ImmutableLog> findByVerifiedFalse();
    
    // Logs con tampering
    List<ImmutableLog> findByIntegrityStatus(String status);
    
    // PROHIBIDO: No métodos delete, update
}

SERVICE (ImmutableLoggingService.java):

@Service
@RequiredArgsConstructor
public class ImmutableLoggingService {
    
    private final ImmutableLogRepository repository;
    
    /**
     * Crea log entry inmutable
     * CRÍTICO: Solo INSERT, nunca UPDATE
     */
    public ImmutableLog createLogEntry(String entityType, Long entityId, String action, 
                                       Long userId, Map<String, Object> data) {
        // Obtener hash del log anterior
        Optional<ImmutableLog> lastLog = repository.findTopByOrderByIdDesc();
        String previousHash = lastLog.map(ImmutableLog::getCurrentHash)
                                      .orElse("0000000000000000000000000000000000000000000000000000000000000000");
        
        // Crear nuevo log
        ImmutableLog log = new ImmutableLog();
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setAction(action);
        log.setUserId(userId);
        log.setData(data);
        log.setPreviousHash(previousHash);
        
        // Calcular current hash
        String currentHash = calculateHash(log);
        log.setCurrentHash(currentHash);
        log.setIntegrityStatus("UNVERIFIED");
        
        // Guardar (solo INSERT)
        return repository.save(log);
    }
    
    /**
     * Calcula SHA-256 hash del log
     */
    private String calculateHash(ImmutableLog log) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // Concatenar campos en orden determinista
            String hashInput = log.getPreviousHash() +
                               log.getTimestampEpoch() +
                               log.getEntityType() +
                               log.getEntityId() +
                               log.getAction() +
                               log.getUserId() +
                               serializeDataDeterministically(log.getData());
            
            byte[] hashBytes = digest.digest(hashInput.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
    
    /**
     * Verifica integridad de hash chain
     */
    public LogIntegrityReport verifyIntegrity(Long startId, Long endId) {
        List<ImmutableLog> logs = repository.findByIdBetweenOrderByIdAsc(startId, endId);
        
        LogIntegrityReport report = new LogIntegrityReport();
        report.setTotalLogsChecked(logs.size());
        report.setIntegrityValid(true);
        
        String expectedPreviousHash = null;
        for (ImmutableLog log : logs) {
            // Verificar hash actual
            String calculatedHash = calculateHash(log);
            if (!calculatedHash.equals(log.getCurrentHash())) {
                report.setIntegrityValid(false);
                report.addCorruptedLog(log.getId(), "Current hash mismatch");
            }
            
            // Verificar chain
            if (expectedPreviousHash != null && !log.getPreviousHash().equals(expectedPreviousHash)) {
                report.setIntegrityValid(false);
                report.addCorruptedLog(log.getId(), "Chain broken");
            }
            
            expectedPreviousHash = log.getCurrentHash();
        }
        
        return report;
    }
    
    /**
     * Obtiene logs de una entidad con verificación
     */
    public List<ImmutableLog> getEntityLogsWithVerification(String entityType, Long entityId) {
        List<ImmutableLog> logs = repository.findByEntityTypeAndEntityIdOrderByTimestampEpochAsc(entityType, entityId);
        
        // Verificar integridad si hay más de 1 log
        if (logs.size() > 1) {
            verifyIntegrity(logs.get(0).getId(), logs.get(logs.size()-1).getId());
        }
        
        return logs;
    }
}

MIGRATION SQL:

CREATE TABLE IMLIMMUTABLELOGS (
    IDXIMMUTABLELOG BIGSERIAL PRIMARY KEY,
    iduuid VARCHAR(36) UNIQUE NOT NULL,
    IMLPREVIOUSHASH VARCHAR(64) NOT NULL,
    IMLCURRENTHASH VARCHAR(64) UNIQUE NOT NULL,
    IMLTIMESTAMP TIMESTAMP NOT NULL,
    IMLTIMESTAMPEPOCH BIGINT NOT NULL,
    IMLENTITYTYPE VARCHAR(50) NOT NULL,
    IMLENTITYID BIGINT NOT NULL,
    IMLACTION VARCHAR(100) NOT NULL,
    IMLUSERID BIGINT NOT NULL,
    IMLUSERNAME VARCHAR(100),
    IMLDATA JSONB NOT NULL,
    IMLVERIFIED BOOLEAN DEFAULT FALSE,
    IMLINTEGRITYSTATUS VARCHAR(20) DEFAULT 'UNVERIFIED',
    IMLLASTVERIFICATIONDATE TIMESTAMP,
    IMLEXTERNALTIMESTAMP TEXT,
    IMLIPADDRESS VARCHAR(45),
    IMLUSERAGENT VARCHAR(200),
    IMLCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_iml_entity ON IMLIMMUTABLELOGS(IMLENTITYTYPE, IMLENTITYID);
CREATE INDEX idx_iml_timestamp ON IMLIMMUTABLELOGS(IMLTIMESTAMP);
CREATE INDEX idx_iml_epoch ON IMLIMMUTABLELOGS(IMLTIMESTAMPEPOCH);
CREATE INDEX idx_iml_hash ON IMLIMMUTABLELOGS(IMLCURRENTHASH);
CREATE INDEX idx_iml_user ON IMLIMMUTABLELOGS(IMLUSERID);

-- CRÍTICO: Deshabilitar UPDATE y DELETE
CREATE OR REPLACE FUNCTION prevent_immutable_log_modification()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'UPDATE') THEN
        RAISE EXCEPTION 'UPDATE not allowed on immutable logs';
    END IF;
    IF (TG_OP = 'DELETE') THEN
        RAISE EXCEPTION 'DELETE not allowed on immutable logs';
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_prevent_modification
BEFORE UPDATE OR DELETE ON IMLIMMUTABLELOGS
FOR EACH ROW EXECUTE FUNCTION prevent_immutable_log_modification();

IMPORTANTE:
- Logs NUNCA se modifican (protección a nivel BD)
- Hash chain garantiza integridad
- Performance: índices apropiados para queries frecuentes
- Particionamiento: considerar por fecha si volumen alto
```

**Artículos Cubiertos:** Art. 19 (completo), Art. 12  
**Esfuerzo:** 2 días  
**Prioridad:** 🔴 MUY CRÍTICA

---

### **GRUPO B: ENTIDADES CATALOGOS (2-3 días)**

---

### **PROMPT B.1 - Entidad AnnexIIICategory.java**

**Objetivo:** Catálogo de categorías Anexo III (8 categorías, 25 subcategorías).

**Prompt Específico:**

```
Necesito CREAR entidad JPA AnnexIIICategory.java para catálogo de categorías de sistemas de alto riesgo según Anexo III.

ESPECIFICACIONES:
- Package: com.codeflowx.govern.entity.catalogs
- Tabla: ANNANNEXIIICATEGORIES
- Prefijo: ANN

DEFINICIÓN ENTIDAD:

@Entity
@Table(name = "ANNANNEXIIICATEGORIES")
@Getter
@Setter
@NoArgsConstructor
public class AnnexIIICategory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXANNEXIIICATEGORY")
    private Long id;
    
    @Column(name = "iduuid", unique = true, nullable = false)
    private String uuid;
    
    // Category Info
    @Column(name = "ANNCATEGORYCODE", length = 10, unique = true, nullable = false)
    private String categoryCode;  // III.1, III.2, ..., III.8
    
    @Column(name = "ANNCATEGORYNAME", length = 200, nullable = false)
    private String categoryName;  // "Biometría", "Infraestructuras críticas", etc.
    
    @Column(name = "ANNCATEGORYDESCRIPTION", columnDefinition = "TEXT")
    private String categoryDescription;
    
    // Subcategorías
    @Column(name = "ANNSUBCATEGORYCODE", length = 10)
    private String subcategoryCode;  // III.4.a, III.4.b, etc. (NULL si es categoría padre)
    
    @Column(name = "ANNSUBCATEGORYNAME", length = 300)
    private String subcategoryName;
    
    @Column(name = "ANNSUBCATEGORYDESCRIPTION", columnDefinition = "TEXT")
    private String subcategoryDescription;
    
    // Jerarquía
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ANNPARENTCATEGORY")
    private AnnexIIICategory parentCategory;  // NULL si es categoría root
    
    @Column(name = "ANNISLEVEL1")
    private Boolean isLevel1 = false;  // TRUE para 8 categorías principales
    
    @Column(name = "ANNISLEVEL2")
    private Boolean isLevel2 = false;  // TRUE para subcategorías
    
    // EU AI Act Reference
    @Column(name = "ANNANNEXIIISECTION", length = 500)
    private String annexIIISectionText;  // Texto literal del Anexo III
    
    @Column(name = "ANNARTICLEREFERENCE", length = 100)
    private String articleReference;  // "Art. 6.2 + Anexo III punto 4"
    
    // Keywords para clasificación automática IA
    @Column(name = "ANNKEYWORDS", columnDefinition = "JSONB")
    @Type(JsonBinaryType.class)
    private java.util.List<String> keywords;
    
    // Metadata
    @Column(name = "ANNACTIVE")
    private Boolean active = true;
    
    @Column(name = "ANNDISPLAYORDER")
    private Integer displayOrder;
    
    @Column(name = "ANNCREATEDAT")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (uuid == null) uuid = java.util.UUID.randomUUID().toString();
    }
}

DATA SEED (Liquibase/Flyway):

-- Categoría 1: Biometría
INSERT INTO ANNANNEXIIICATEGORIES (iduuid, ANNCATEGORYCODE, ANNCATEGORYNAME, ANNISLEVEL1, ANNDISPLAYORDER) 
VALUES (uuid_generate_v4(), 'III.1', 'Biometría', TRUE, 1);

-- Subcategorías biometría
INSERT INTO ANNANNEXIIICATEGORIES (iduuid, ANNCATEGORYCODE, ANNSUBCATEGORYCODE, ANNSUBCATEGORYNAME, ANNPARENTCATEGORY, ANNISLEVEL2, ANNDISPLAYORDER)
VALUES (uuid_generate_v4(), 'III.1', 'III.1.a', 'Sistemas de identificación biométrica remota', (SELECT IDXANNEXIIICATEGORY FROM ANNANNEXIIICATEGORIES WHERE ANNCATEGORYCODE='III.1'), TRUE, 1);

... (repetir para 8 categorías + 25 subcategorías del documento oficial)

REPOSITORY:

@Repository
public interface AnnexIIICategoryRepository extends JpaRepository<AnnexIIICategory, Long> {
    
    // Categorías nivel 1 (8 principales)
    List<AnnexIIICategory> findByIsLevel1TrueOrderByDisplayOrderAsc();
    
    // Subcategorías de una categoría
    List<AnnexIIICategory> findByParentCategoryOrderByDisplayOrderAsc(AnnexIIICategory parent);
    
    // Por código
    Optional<AnnexIIICategory> findByCategoryCode(String code);
    Optional<AnnexIIICategory> findBySubcategoryCode(String code);
    
    // Búsqueda por keywords (para clasificación IA)
    @Query("SELECT a FROM AnnexIIICategory a WHERE :keyword MEMBER OF a.keywords")
    List<AnnexIIICategory> findByKeyword(@Param("keyword") String keyword);
}

SERVICE:

@Service
@RequiredArgsConstructor
public class AnnexIIICategoryService {
    
    private final AnnexIIICategoryRepository repository;
    
    /**
     * Obtiene 8 categorías principales
     */
    public List<AnnexIIICategory> getMainCategories();
    
    /**
     * Obtiene subcategorías de una categoría
     */
    public List<AnnexIIICategory> getSubcategories(String categoryCode);
    
    /**
     * Sugiere categoría basado en descripción proyecto (usa keywords)
     */
    public List<CategorySuggestion> suggestCategories(String projectDescription);
}

CONTROLLER:

@RestController
@RequestMapping("/api/catalogs/annex-iii")
@RequiredArgsConstructor
public class AnnexIIICategoryController {
    
    @GetMapping("/categories")
    public ResponseEntity<List<AnnexIIICategoryDTO>> getMainCategories();
    
    @GetMapping("/categories/{code}/subcategories")
    public ResponseEntity<List<AnnexIIICategoryDTO>> getSubcategories(@PathVariable String code);
    
    @PostMapping("/suggest")
    public ResponseEntity<List<CategorySuggestionDTO>> suggestCategories(@RequestBody ProjectDescriptionDTO desc);
}

IMPORTANTE:
- Datos seed con las 8 categorías + 25 subcategorías del DOCUMENTO OFICIAL PDF
- Keywords para clasificación automática IA
- Textos oficiales del Anexo III (copiar del PDF)
```

**Artículos Cubiertos:** Anexo III (catálogo completo)  
**Esfuerzo:** 1-2 días  
**Prioridad:** 🔴 Crítica

---

### **PROMPT B.2 - Entidad EuRegistration.java**

**Objetivo:** Entidad para registro en Base de Datos UE según Art. 49.

**Prompt Específico:**

```
Necesito CREAR entidad JPA EuRegistration.java para almacenar registros en Base de Datos UE según Art. 49 y Anexo VIII.

ESPECIFICACIONES:
- Package: com.codeflowx.govern.entity.compliance
- Tabla: REGEUREGISTRATIONS
- Prefijo: REG

DEFINICIÓN ENTIDAD:

@Entity
@Table(name = "REGEUREGISTRATIONS")
@Getter
@Setter
@NoArgsConstructor
public class EuRegistration {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXEUREGISTRATION")
    private Long id;
    
    @Column(name = "iduuid", unique = true, nullable = false)
    private String uuid;
    
    // FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDXPROJECT", nullable = false)
    private Project project;
    
    // Tipo de registro (3 secciones Anexo VIII)
    @Column(name = "REGREGISTRATIONTYPE", length = 20, nullable = false)
    private String registrationType;  // SECTION_A, SECTION_B, SECTION_C
    
    // Art. 49 apartado
    @Column(name = "REGART49SECTION", length = 10)
    private String art49Section;  // 49.1, 49.2, 49.3
    
    // EU Registration ID (asignado por sistema UE)
    @Column(name = "REGEUREGISTRATIONID", length = 100, unique = true)
    private String euRegistrationId;
    
    // Submission Data
    @Column(name = "REGSUBMISSIONDATA", columnDefinition = "JSONB", nullable = false)
    @Type(JsonBinaryType.class)
    private java.util.Map<String, Object> submissionData;  // Todos los campos Anexo VIII
    
    // Status
    @Column(name = "REGSTATUS", length = 20)
    private String status;  // DRAFT, PENDING, SUBMITTED, REGISTERED, REJECTED, ERROR
    
    @Column(name = "REGSUBMISSIONDATE")
    private LocalDateTime submissionDate;
    
    @Column(name = "REGREGISTRATIONDATE")
    private LocalDateTime registrationDate;
    
    // Response from EU API
    @Column(name = "REGEUAPIRESPONSE", columnDefinition = "JSONB")
    @Type(JsonBinaryType.class)
    private java.util.Map<String, Object> euApiResponse;
    
    @Column(name = "REGERRORMESSAGE", columnDefinition = "TEXT")
    private String errorMessage;
    
    // Retry tracking
    @Column(name = "REGATTEMPTS")
    private Integer attempts = 0;
    
    @Column(name = "REGLASTATTEMPTAT")
    private LocalDateTime lastAttemptAt;
    
    // Sensitive system (Art. 49.4 - registro no público)
    @Column(name = "REGISSENSITIVE")
    private Boolean isSensitive = false;  // TRUE para Anexo III.1, III.6, III.7
    
    // National registration (Art. 49.5 - infraestructuras críticas)
    @Column(name = "REGISNATIONAL")
    private Boolean isNational = false;
    
    @Column(name = "REGNATIONALREGISTRATIONID", length = 100)
    private String nationalRegistrationId;
    
    // Audit
    @Column(name = "REGCREATEDAT")
    private LocalDateTime createdAt;
    
    @Column(name = "REGUPDATEDAT")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (uuid == null) uuid = java.util.UUID.randomUUID().toString();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

REPOSITORY + SERVICE + CONTROLLER:
- Patrón estándar
- Métodos:
  - createRegistration(projectId, type)
  - submitToEuDatabase(registrationId)
  - checkSubmissionStatus(registrationId)
  - retrySubmission(registrationId)

IMPORTANTE:
- Diferentes datos según sección (A: 13 campos, B: 9 campos, C: 5 campos)
- Manejo de sensitive systems (registro no público)
- Retry logic con exponential backoff
- Logging de todos los attempts
```

**Artículos Cubiertos:** Art. 49, Anexo VIII  
**Esfuerzo:** 2 días  
**Prioridad:** 🔴 Crítica

---

## 📊 RESUMEN PROMPTS - JAVA NUEVAS ENTIDADES

| Prompt | Entidad | Tabla | Componentes | Esfuerzo | Prioridad |
|--------|---------|-------|-------------|----------|-----------|
| **A.1** | ComplianceAssessment | COMCOMPLIANCEASSESSMENTS | JSON + Entity + BusinessService + ViewModel + ZUL | 2 días | 🔴 Crítica |
| **A.2** | FriaAssessment | FRIAFUNDAMENTALRIGHTSASSESSMENTS | JSON + Entity + BusinessService + ViewModel + ZUL | 2-3 días | 🔴 Crítica |
| **A.3** | ImmutableLog | IMLIMMUTABLELOGS | JSON + Entity + BusinessService + Trigger BD | 2 días | 🔴 MUY CRÍTICA |
| **B.1** | AnnexIIICategory | ANNANNEXIIICATEGORIES | JSON + Entity + BusinessService + Seed 33 registros | 1-2 días | 🔴 Crítica |
| **B.2** | EuRegistration | REGEUREGISTRATIONS | JSON + Entity + BusinessService + ViewModel + ZUL | 2 días | 🔴 Crítica |

**CADA ENTIDAD INCLUYE:**
- 1 JSON EnArt (definición)
- 1 Entity.java (generado automático)
- 1 BusinessService.java (usa DAO EnArt)
- 1 ViewModel.java (ZKoss)
- 1 Pantalla .zul
- 1 Migration SQL

**TOTAL ESFUERZO:** 9-12 días  
**CON 3 CHATS PARALELOS:** 3-4 días reales (o 20-30 min con velocidad actual 🚀)

---

## 🎯 DISTRIBUCIÓN TRABAJO PARALELO

### **CHAT JAVA-NEW-1 - Compliance Core:**
- Prompt A.1 (ComplianceAssessment)
- Prompt A.2 (FriaAssessment)
- **Esfuerzo:** 4-6 días

### **CHAT JAVA-NEW-2 - Logging & Security:**
- Prompt A.3 (ImmutableLog + trigger BD)
- **Esfuerzo:** 2 días

### **CHAT JAVA-NEW-3 - Catalogos:**
- Prompt B.1 (AnnexIIICategory + 33 registros seed)
- Prompt B.2 (EuRegistration)
- **Esfuerzo:** 3-4 días

**Timeline con 3 chats:** 4-6 días reales

---

## ⚠️ ARQUITECTURA ENART - INSTRUCCIONES CRÍTICAS

### **TODOS LOS PROMPTS ANTERIORES SIGUEN ESTE PATRÓN:**

```
ARQUITECTURA CORRECTA:
1. Definir JSON EnArt (sources/json/tables/NOMBREENTIDAD.json)
2. Ejecutar generador Python → Entity.java automático
3. Crear BusinessService (usa DAO EnArt, NO Repository)
4. Crear ViewModel ZKoss
5. Crear pantalla ZUL
6. Migration SQL (CREATE TABLE)

NO CREAR:
❌ Spring Data Repository (EnArt usa DAO)
❌ REST Controllers (se crean con SDK después)
❌ @Entity JPA estándar (usar generador EnArt)
```

### **Ejemplo Código BusinessService:**
```java
@Service
public class MyBusinessService {
    @Autowired
    private DAO dao;  // EnArt DAO, NO Repository
    
    public MyEntity create(MyEntity entity) {
        dao.insert(entity);
        return entity;
    }
    
    public MyEntity findById(Long id) {
        return dao.findById(MyEntity.class, id);
    }
    
    public List<MyEntity> findByProject(Long projectId) {
        String query = "SELECT * FROM MYTABLE WHERE IDXPROJECT = ?";
        return dao.findBySQL(MyEntity.class, query, projectId);
    }
}
```

---

## 📋 CHECKLIST CADA ENTIDAD NUEVA

Verificar que incluye:

**PASO 1 - JSON EnArt:**
- [ ] Archivo .json en sources/json/tables/
- [ ] Namespace correcto (compliance, catalogs, logging, etc.)
- [ ] name = tabla (MAYÚSCULAS, prefijo 3 chars)
- [ ] type = "TABLE"
- [ ] labelMonitor definido
- [ ] Fields array completo:
  - [ ] PK con "pk": true
  - [ ] iduuid campo obligatorio
  - [ ] FKs con "fk": {"table": "...", "field": "..."}
  - [ ] Tipos correctos (LONG, VARCHAR, TIMESTAMP, JSONB, BOOLEAN, DECIMAL)
  - [ ] Campos audit: createdAt, updatedAt

**PASO 2 - Entity Java (Generado):**
- [ ] Ejecutar generador Python
- [ ] Verificar Entity.java generado correctamente
- [ ] Ubicación: sources/jpa/{namespace}/NombreEntity.java

**PASO 3 - BusinessService:**
- [ ] Package: com.codeflowx.govern.business.{namespace}
- [ ] @Service annotation
- [ ] @Autowired DAO dao (EnArt DAO)
- [ ] Métodos CRUD usando dao.insert(), dao.update(), dao.findById()
- [ ] Queries custom con dao.findBySQL()
- [ ] Integración otros BusinessServices
- [ ] Logging @Slf4j

**PASO 4 - ViewModel ZKoss:**
- [ ] Package: com.codeflowx.govern.viewmodel.{namespace}
- [ ] Extends SelectorComposer<Component>
- [ ] @WireVariable para BusinessService
- [ ] @Listen para eventos UI
- [ ] Métodos binding datos pantalla

**PASO 5 - Pantalla ZUL:**
- [ ] Archivo .zul en resources
- [ ] apply="ruta.ViewModel"
- [ ] Componentes: Window, Grid, Listbox, etc.
- [ ] Bindings a ViewModel

**PASO 6 - Migration SQL:**
- [ ] CREATE TABLE completo
- [ ] PRIMARY KEY
- [ ] INDEXes apropiados
- [ ] Triggers si necesarios (ej: ImmutableLog)

---

**Fin Documento 5 de 5**

---

## 🎯 RESUMEN GENERAL - 5 DOCUMENTOS DE PROMPTS

| Doc | Equipo | Prompts | Arquitectura | Esfuerzo | Chats Paralelos |
|-----|--------|---------|--------------|----------|-----------------|
| **1** | Python Micros Existentes | 7 prompts | FastAPI stateless | 16-18 días | 3-4 chats → 4-6 días reales |
| **2** | Python Micros Nuevos | 5 prompts | FastAPI stateless | 12-15 días | 3 chats → 4-5 días reales |
| **3** | Java Backend Existente | 7 prompts | EnArt + ZKoss | 12-15 días | 3-4 chats → 3-5 días reales |
| **4** | BPMN Workflows | 4 prompts | Camunda + Delegates | 10-12 días | 2-3 chats → 5-6 días reales |
| **5** | Java Entidades Nuevas | 5 prompts | **EnArt JSON + DAO + ZKoss** | 9-12 días | 3-4 chats → 2-4 días reales |

**TOTALES:**
- **28 prompts específicos**
- **Arquitecturas:** EnArt (NO Hibernate estándar) + FastAPI + Camunda + ZKoss
- **59-72 días esfuerzo secuencial**
- **Con 15-18 chats paralelos:** 5-6 días reales máximo
- **Conservador con 12 chats:** 7-8 días reales

**CONCLUSIÓN: 65 gaps implementables en ~8 días reales con paralelización óptima.**

**ARQUITECTURA DOC 5:**
- ✅ JSON EnArt (sources/json/tables/)
- ✅ Generador Python → Entity.java
- ✅ BusinessService con DAO (NO Repository)
- ✅ ViewModel ZKoss
- ❌ NO Controllers REST (SDK después)

