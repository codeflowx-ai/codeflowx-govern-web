# INCIDENCIAS Y RECOMENDACIONES - VERSIONADO Y CONTROL DE CAMBIOS
**Auditoría 011 - EU AI ACT COMPLIANCE**

**Fecha:** Diciembre 2025  
**Prioridad:** 🔴 CRÍTICO  
**Ámbito:** Versionado de datasets, modelos, prompts, RAG, evaluaciones y prevención de cambios no autorizados

---

## 📋 RESUMEN DE INCIDENCIAS

**Total de Incidencias:** 4  
- 🔴 **Críticas:** 2
- 🟡 **Altas:** 2
- 🟢 **Medias:** 0

---

## 🔴 INCIDENCIAS CRÍTICAS

### **INC-011-01: Falta Control de Acceso Basado en Roles para Prevenir Cambios por Partners**

**Prioridad:** 🔴 **CRÍTICA**  
**Severidad:** **ALTA**  
**Impacto:** Riesgo de cambios no autorizados en versiones aprobadas por partners externos

#### **Descripción:**
El sistema actual permite que cualquier usuario (incluyendo partners externos) modifique versiones aprobadas de modelos, prompts o configuraciones RAG. Aunque existe trazabilidad mediante logs inmutables, **no existe control proactivo** que **prevenga** los cambios antes de que ocurran.

#### **Evidencia:**
- No existe tabla `CORROLES` o sistema de roles
- No existe middleware de autorización en BusinessServices
- Las versiones con status `APPROVED` o `PRODUCTION` pueden ser modificadas directamente
- No hay diferenciación entre usuarios internos y partners en permisos

#### **Requisito Normativo:**
- **EU AI Act Art. 12:** "Los sistemas de IA de alto riesgo deberán diseñarse y desarrollarse con capacidades que permitan el registro automático de eventos"
- **EU AI Act Art. 19:** "Los registros serán completos, precisos e inalterables"
- **ISO/IEC 27001:** Control de acceso basado en roles (RBAC)

#### **Recomendaciones de Implementación:**

**1. Crear Sistema de Roles:**
```sql
-- Tabla de Roles
CREATE TABLE CORROLES (
    IDXROLE BIGSERIAL PRIMARY KEY,
    iduuid VARCHAR(36) UNIQUE NOT NULL,
    CORROLECODE VARCHAR(50) NOT NULL UNIQUE,
    CORROLENAME VARCHAR(100) NOT NULL,
    CORDESCRIPTION TEXT,
    CORISINTERNAL BOOLEAN NOT NULL DEFAULT FALSE,
    CORPERMISSIONS JSONB, -- Permisos por entidad/acción
    CORCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Roles estándar
INSERT INTO CORROLES (CORROLECODE, CORROLENAME, CORISINTERNAL, CORPERMISSIONS) VALUES
('INTERNAL_ADMIN', 'Administrador Interno', true, '{"*":["*"]}'::jsonb),
('PARTNER_READONLY', 'Partner Solo Lectura', false, '{"*":["READ"]}'::jsonb),
('PARTNER_EDITOR', 'Partner Editor', false, '{"models":["CREATE","READ"],"prompts":["CREATE","READ"],"rag":["CREATE","READ"]}'::jsonb),
('PARTNER_APPROVER', 'Partner Aprobador', false, '{"models":["*"],"prompts":["*"],"rag":["*"]}'::jsonb);

-- Tabla de Asignación Usuario-Rol
CREATE TABLE CORUSERROLES (
    IDXUSERROLE BIGSERIAL PRIMARY KEY,
    iduuid VARCHAR(36) UNIQUE NOT NULL,
    IDXUSER BIGINT NOT NULL REFERENCES CORUSERS(IDXUSER),
    IDXROLE BIGINT NOT NULL REFERENCES CORROLES(IDXROLE),
    CORPARTNERID BIGINT, -- Si es partner, referencia a partner
    CORCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

**2. Implementar Workflow de Aprobación:**
```sql
-- Tabla de Aprobaciones de Versiones
CREATE TABLE VERSVERSIONAPPROVALS (
    IDXVERSIONAPPROVAL BIGSERIAL PRIMARY KEY,
    iduuid VARCHAR(36) UNIQUE NOT NULL,
    VERSENTITYTYPE VARCHAR(50) NOT NULL, -- MODEL, PROMPT, RAG, DATASET
    VERSENTITYID BIGINT NOT NULL,
    VERSVERSIONID BIGINT NOT NULL,
    VERSAPPROVALSTATUS VARCHAR(20) NOT NULL, -- PENDING, APPROVED, REJECTED
    VERSAPPROVEDBY BIGINT REFERENCES CORUSERS(IDXUSER),
    VERSAPPROVALDATE TIMESTAMP,
    VERSAPPROVALNOTES TEXT,
    VERSREJECTIONREASON TEXT,
    CORCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CORUPDATEDAT TIMESTAMP
);
```

**3. Modificar BusinessServices para Verificar Roles:**
```java
@Service
public class ModelVersionBusinessService {
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private ImmutableLoggingBusinessService loggingService;
    
    public ModelVersion createVersion(ModelVersion version, Long userId) {
        // Verificar que usuario tiene rol PARTNER_EDITOR o INTERNAL_ADMIN
        if (!roleService.hasPermission(userId, "models", "CREATE")) {
            throw new UnauthorizedException("Usuario no tiene permisos para crear versiones de modelos");
        }
        
        // Crear versión
        ModelVersion created = modelVersionDao.save(version);
        
        // Log inmutable
        loggingService.logChange("MODEL", created.getIdxmodelversion(), "CREATE", userId, created);
        
        return created;
    }
    
    public ModelVersion updateVersion(Long versionId, ModelVersion updates, Long userId) {
        ModelVersion existing = modelVersionDao.findById(versionId);
        
        // Verificar que versión NO está aprobada o en producción
        if (existing.getModstatus().equals("APPROVED") || existing.getModstatus().equals("PRODUCTION")) {
            // Verificar que usuario tiene rol INTERNAL_ADMIN o PARTNER_APPROVER
            if (!roleService.hasPermission(userId, "models", "UPDATE_APPROVED")) {
                throw new UnauthorizedException("No se pueden modificar versiones aprobadas. Crear nueva versión.");
            }
        }
        
        // Verificar permisos básicos
        if (!roleService.hasPermission(userId, "models", "UPDATE")) {
            throw new UnauthorizedException("Usuario no tiene permisos para modificar versiones");
        }
        
        // Actualizar
        ModelVersion updated = modelVersionDao.update(versionId, updates);
        
        // Log inmutable
        loggingService.logChange("MODEL", versionId, "UPDATE", userId, updated);
        
        return updated;
    }
    
    public void approveVersion(Long versionId, Long approverId, String notes) {
        // Verificar que aprobador tiene rol PARTNER_APPROVER o INTERNAL_ADMIN
        if (!roleService.hasPermission(approverId, "models", "APPROVE")) {
            throw new UnauthorizedException("Usuario no tiene permisos para aprobar versiones");
        }
        
        ModelVersion version = modelVersionDao.findById(versionId);
        version.setModstatus("APPROVED");
        
        // Crear registro de aprobación
        VersionApproval approval = new VersionApproval();
        approval.setVersentitytype("MODEL");
        approval.setVersversionid(versionId);
        approval.setVersapprovalstatus("APPROVED");
        approval.setVersapprovedby(approverId);
        approval.setVersapprovaldate(new Timestamp(System.currentTimeMillis()));
        approval.setVersapprovalnotes(notes);
        versionApprovalDao.save(approval);
        
        // Log inmutable
        loggingService.logChange("MODEL", versionId, "APPROVE", approverId, version);
    }
}
```

**4. Bloquear UPDATE/DELETE en Versiones Aprobadas (Triggers PostgreSQL):**
```sql
-- Trigger para prevenir UPDATE en versiones aprobadas (salvo admins)
CREATE OR REPLACE FUNCTION prevent_approved_version_update()
RETURNS TRIGGER AS $$
DECLARE
    user_role VARCHAR(50);
BEGIN
    -- Obtener rol del usuario (desde sesión o contexto)
    -- Por simplicidad, asumimos que se pasa en CURRENT_SETTING('app.user_role')
    user_role := COALESCE(CURRENT_SETTING('app.user_role', true), 'PARTNER_READONLY');
    
    -- Si la versión está aprobada y usuario NO es admin, forzar creación de nueva versión
    IF OLD.modstatus IN ('APPROVED', 'PRODUCTION') AND user_role NOT IN ('INTERNAL_ADMIN', 'PARTNER_APPROVER') THEN
        RAISE EXCEPTION 'No se pueden modificar versiones aprobadas. Crear nueva versión con MODPARENTVERSION = %', OLD.idxmodelversion;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_prevent_approved_model_version_update
BEFORE UPDATE ON MODMODELVERSIONS
FOR EACH ROW
EXECUTE FUNCTION prevent_approved_version_update();

-- Similar para PROMPTVERSIONS y RAGVERSIONS
```

#### **Esfuerzo Estimado:**
- **Desarrollo:** 3-5 días
- **Testing:** 1-2 días
- **Documentación:** 0.5 días
- **Total:** 4.5-7.5 días

#### **Beneficios:**
- ✅ Previene cambios no autorizados **proactivamente**
- ✅ Cumple requisitos de control de acceso (ISO 27001)
- ✅ Proporciona auditoría clara de aprobaciones
- ✅ Mantiene trazabilidad completa mediante logs inmutables

---

### **INC-011-02: Falta Versionado Explícito de Datasets**

**Prioridad:** 🔴 **CRÍTICA**  
**Severidad:** **ALTA**  
**Impacto:** Imposibilidad de rastrear cambios históricos en datasets y cumplir Art. 10 (Data Governance)

#### **Descripción:**
Los datasets no tienen entidad de versionado propia. Solo existe un campo `DATASETVERSION` en `TrainingExecution`, lo que permite saber qué versión se usó en una ejecución, pero **no hay historial completo** de versiones del dataset, cambios, checksums ni trazabilidad completa.

#### **Evidencia:**
- No existe tabla `DATADATASETVERSIONS`
- Campo `DATASETVERSION` en `TrainingExecution` es solo VARCHAR (no FK)
- No hay checksums para verificar integridad de datasets
- No hay changelog de cambios en datasets

#### **Requisito Normativo:**
- **EU AI Act Art. 10:** "Los proveedores de sistemas de IA de alto riesgo gestionarán... datos de entrenamiento, validación y prueba"
- **EU AI Act Art. 11 + Anexo IV:** "Documentación técnica incluirá... información sobre datos de entrenamiento"
- **GDPR Art. 30:** Registro de actividades de tratamiento

#### **Recomendaciones de Implementación:**

**1. Crear Entidad DatasetVersion:**
```sql
-- Tabla de Versionado de Datasets
CREATE TABLE DATADATASETVERSIONS (
    IDXDATASETVERSION BIGSERIAL PRIMARY KEY,
    iduuid VARCHAR(36) UNIQUE NOT NULL,
    IDXDATASET BIGINT NOT NULL REFERENCES DATADATASETS(IDXDATASET),
    DATAVERSIONNUMBER VARCHAR(50) NOT NULL, -- SemVer: 1.2.0
    DATAPARENTVERSION BIGINT REFERENCES DATADATASETVERSIONS(IDXDATASETVERSION),
    DATADESCRIPTION TEXT,
    DATACHANGELOG TEXT, -- Descripción de cambios
    DATAHASH VARCHAR(64), -- SHA-256 checksum del archivo
    DATAFILESIZE BIGINT, -- Tamaño en bytes
    DATAFILECOUNT INTEGER, -- Número de archivos
    DATAROWCOUNT BIGINT, -- Número de filas/registros
    DATASTATISTICS JSONB, -- Estadísticas del dataset
    DATASTATUS VARCHAR(50) NOT NULL, -- DRAFT, VALIDATED, APPROVED, ARCHIVED
    DATACREATEDBY VARCHAR(255) NOT NULL,
    DATACREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    DATAVALIDATEDBY VARCHAR(255),
    DATAVALIDATEDAT TIMESTAMP,
    CONSTRAINT uk_dataset_version UNIQUE (IDXDATASET, DATAVERSIONNUMBER)
);

-- Índices
CREATE INDEX idx_data_version_dataset ON DATADATASETVERSIONS(IDXDATASET);
CREATE INDEX idx_data_version_number ON DATADATASETVERSIONS(DATAVERSIONNUMBER);
CREATE INDEX idx_data_version_status ON DATADATASETVERSIONS(DATASTATUS);
CREATE INDEX idx_data_version_hash ON DATADATASETVERSIONS(DATAHASH);
```

**2. Modificar TrainingExecution para usar FK:**
```sql
-- Agregar FK a DATADATASETVERSIONS
ALTER TABLE TRNTRAININGEXECUTIONS 
ADD COLUMN IDXDATASETVERSION BIGINT REFERENCES DATADATASETVERSIONS(IDXDATASETVERSION);

-- Migrar datos existentes (si aplica)
-- UPDATE TRNTRAININGEXECUTIONS SET IDXDATASETVERSION = ... WHERE DATASETVERSION IS NOT NULL;

-- Opcionalmente, mantener campo DATASETVERSION como denormalización
```

**3. Crear BusinessService para DatasetVersion:**
```java
@Service
public class DatasetVersionBusinessService {
    
    @Autowired
    private DatasetVersionDao datasetVersionDao;
    
    @Autowired
    private ImmutableLoggingBusinessService loggingService;
    
    public DatasetVersion createVersion(Long datasetId, DatasetVersion version, Long userId) {
        // Calcular hash del dataset
        String hash = calculateDatasetHash(version.getDatafiles());
        version.setDatahash(hash);
        
        // Verificar que versión no existe
        if (datasetVersionDao.existsByDatasetAndVersion(datasetId, version.getDataversionnumber())) {
            throw new VersionExistsException("La versión " + version.getDataversionnumber() + " ya existe");
        }
        
        version.setIdxdataset(datasetId);
        version.setDatacreatedby(getUserName(userId));
        version.setDatastatus("DRAFT");
        
        DatasetVersion created = datasetVersionDao.save(version);
        
        // Log inmutable
        loggingService.logChange("DATASET", created.getIdxdatasetversion(), "CREATE", userId, created);
        
        return created;
    }
    
    private String calculateDatasetHash(List<File> files) {
        // Implementar cálculo de SHA-256 del conjunto de archivos
        // ...
    }
}
```

#### **Esfuerzo Estimado:**
- **Desarrollo:** 2-3 días
- **Testing:** 1 día
- **Migración datos:** 0.5 días
- **Documentación:** 0.5 días
- **Total:** 4-5 días

#### **Beneficios:**
- ✅ Cumple Art. 10 (Data Governance)
- ✅ Permite trazabilidad completa de cambios en datasets
- ✅ Verificación de integridad mediante checksums
- ✅ Historial completo para auditorías

---

## 🟡 INCIDENCIAS ALTAS

### **INC-011-03: Falta Versionado Explícito de Evaluaciones**

**Prioridad:** 🟡 **ALTA**  
**Severidad:** **MEDIA**  
**Impacto:** Dificultad para diferenciar múltiples evaluaciones del mismo modelo en la misma versión

#### **Descripción:**
Las evaluaciones (`ModelEvaluation`) se vinculan a `modelVersionId`, pero si se re-evalúa el mismo modelo con la misma versión, no hay forma de diferenciar entre evaluaciones. No existe campo de versión de evaluación ni entidad `EvaluationVersion`.

#### **Evidencia:**
- No existe tabla `EVALEVALUATIONVERSIONS`
- Campo `modelVersionId` en `ModelEvaluation` no diferencia múltiples evaluaciones
- No hay tracking de cambios entre evaluaciones

#### **Recomendaciones de Implementación:**

**1. Agregar Versionado a ModelEvaluation:**
```sql
-- Agregar campos de versionado
ALTER TABLE MODEVALUATIONS 
ADD COLUMN EVALVERSIONNUMBER VARCHAR(50),
ADD COLUMN EVALPARENTEVALUATION BIGINT REFERENCES MODEVALUATIONS(IDXEVALUATION),
ADD COLUMN EVALCHANGES TEXT,
ADD COLUMN EVALREASON TEXT, -- Motivo de re-evaluación

-- Crear índice
CREATE INDEX idx_eval_version ON MODEVALUATIONS(modelVersionId, EVALVERSIONNUMBER);
```

**2. Lógica en BusinessService:**
```java
public ModelEvaluation createEvaluation(ModelEvaluation evaluation, Long userId) {
    // Si ya existe evaluación para esta versión, crear nueva versión
    List<ModelEvaluation> existing = evaluationDao.findByModelVersionId(evaluation.getModelVersionId());
    
    if (!existing.isEmpty()) {
        String nextVersion = calculateNextVersion(existing);
        evaluation.setEvalversionnumber(nextVersion);
        evaluation.setEvalparentevaluation(existing.get(0).getIdxevaluation());
    } else {
        evaluation.setEvalversionnumber("1.0.0");
    }
    
    return evaluationDao.save(evaluation);
}
```

#### **Esfuerzo Estimado:**
- **Desarrollo:** 1-2 días
- **Testing:** 0.5 días
- **Migración datos:** 0.5 días
- **Total:** 2-3 días

---

### **INC-011-04: Falta Validación Automática de Formato SemVer en Versiones**

**Prioridad:** 🟡 **ALTA**  
**Severidad:** **BAJA**  
**Impacto:** Inconsistencias en numeración de versiones, dificulta comparación y ordenamiento

#### **Descripción:**
Los campos de versión (`MODVERSION`, `PRMVERSION`, `RAGVVERSIONNUMBER`) son VARCHAR sin validación de formato. No se valida que sigan formato SemVer (X.Y.Z) ni se calculan versiones automáticas.

#### **Recomendaciones de Implementación:**

**1. Agregar Constraints en BD:**
```sql
-- Validar formato SemVer con regex
ALTER TABLE MODMODELVERSIONS 
ADD CONSTRAINT chk_version_format 
CHECK (MODVERSION ~ '^[0-9]+\.[0-9]+\.[0-9]+(-[a-zA-Z0-9]+)?(\+[a-zA-Z0-9]+)?$');

-- Similar para PRMPROMPTVERSIONS y RAGRAGVERSIONS
```

**2. Validación en BusinessService:**
```java
private static final Pattern SEMVER_PATTERN = Pattern.compile(
    "^[0-9]+\\.[0-9]+\\.[0-9]+(-[a-zA-Z0-9]+)?(\\+[a-zA-Z0-9]+)?$"
);

public ModelVersion createVersion(ModelVersion version) {
    // Validar formato
    if (!SEMVER_PATTERN.matcher(version.getModversion()).matches()) {
        throw new InvalidVersionFormatException("Versión debe seguir formato SemVer (X.Y.Z)");
    }
    
    // Auto-calcular si no se proporciona
    if (version.getModversion() == null) {
        version.setModversion(calculateNextVersion(version.getModel()));
    }
    
    return modelVersionDao.save(version);
}

private String calculateNextVersion(Model model) {
    List<ModelVersion> versions = modelVersionDao.findByModelOrderByVersionDesc(model);
    if (versions.isEmpty()) {
        return "1.0.0";
    }
    // Incrementar patch version
    String lastVersion = versions.get(0).getModversion();
    String[] parts = lastVersion.split("\\.");
    int patch = Integer.parseInt(parts[2]) + 1;
    return parts[0] + "." + parts[1] + "." + patch;
}
```

#### **Esfuerzo Estimado:**
- **Desarrollo:** 1 día
- **Testing:** 0.5 días
- **Migración datos existentes:** 1 día (si hay versiones sin formato)
- **Total:** 2.5 días

---

## 📊 RESUMEN DE PRIORIZACIÓN

| Incidencia | Prioridad | Esfuerzo | Impacto | Fecha Objetivo |
|------------|-----------|----------|---------|----------------|
| **INC-011-01** | 🔴 Crítica | 4.5-7.5 días | Alto | Inmediato |
| **INC-011-02** | 🔴 Crítica | 4-5 días | Alto | 1 semana |
| **INC-011-03** | 🟡 Alta | 2-3 días | Medio | 2 semanas |
| **INC-011-04** | 🟡 Alta | 2.5 días | Bajo | 3 semanas |

**Esfuerzo Total Estimado:** 12.5-18.5 días

---

**Fin del Documento de Incidencias y Recomendaciones**







