# PROMPT: INC-011-02 - Versionado Explícito de Datasets

**Incidencia:** INC-011-02  
**Prioridad:** 🔴 CRÍTICA  
**Artículo EU AI Act:** Art. 10 (Gobernanza de Datos), Art. 11 (Documentación Técnica), GDPR Art. 30  
**Esfuerzo Estimado:** 4-5 días  
**Tipo:** Java - Backend + DBA

---

## CONTEXTO

Los datasets no tienen entidad de versionado propia. Solo existe un campo `DATASETVERSION` en `TrainingExecution`, lo que permite saber qué versión se usó en una ejecución, pero **no hay historial completo** de versiones del dataset, cambios, checksums ni trazabilidad completa.

**Ubicación Actual:**
- `TrainingExecution.java` - campo `datasetversion` (VARCHAR, no FK)
- No existe tabla `DATADATASETVERSIONS`
- No hay checksums para verificar integridad de datasets
- No hay changelog de cambios en datasets

**Referencias:**
- `TrainingExecution.java` - línea 228-238
- Tabla `DATADATASETS` (asumida)
- Tabla `TRNTRAININGEXECUTIONS`

---

## REQUISITOS

1. **Crear entidad DatasetVersion** similar a `ModelVersion` y `PromptVersion`
2. **Implementar tabla `DATADATASETVERSIONS`** con versionado SemVer, checksums, changelog
3. **Modificar TrainingExecution** para usar FK a `DatasetVersion` en lugar de VARCHAR
4. **Crear BusinessService** para gestionar versiones de datasets
5. **Calcular checksums SHA-256** para verificación de integridad

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear Tabla DatasetVersion (DBA)

**Archivo:** `nocode.service.entitys/src/main/resources/sql/versionado_datasets.sql`

```sql
-- ============================================================================
-- TABLA DE VERSIONADO DE DATASETS
-- ============================================================================
CREATE TABLE DATADATASETVERSIONS (
    IDXDATASETVERSION BIGSERIAL PRIMARY KEY,
    iduuid VARCHAR(36) UNIQUE NOT NULL DEFAULT gen_random_uuid()::text,
    IDXDATASET BIGINT NOT NULL REFERENCES DATADATASETS(IDXDATASET) ON DELETE CASCADE,
    DATAVERSIONNUMBER VARCHAR(50) NOT NULL, -- SemVer: 1.2.0
    DATAPARENTVERSION BIGINT REFERENCES DATADATASETVERSIONS(IDXDATASETVERSION),
    DATADESCRIPTION TEXT,
    DATACHANGELOG TEXT, -- Descripción de cambios
    DATAHASH VARCHAR(64) NOT NULL, -- SHA-256 checksum del archivo/conjunto de archivos
    DATAFILESIZE BIGINT, -- Tamaño en bytes
    DATAFILECOUNT INTEGER, -- Número de archivos
    DATAROWCOUNT BIGINT, -- Número de filas/registros
    DATASTATISTICS JSONB, -- Estadísticas del dataset (distribución, métricas, etc.)
    DATASTATUS VARCHAR(50) NOT NULL DEFAULT 'DRAFT', -- DRAFT, VALIDATED, APPROVED, ARCHIVED
    DATACREATEDBY VARCHAR(255) NOT NULL,
    DATACREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    DATAVALIDATEDBY VARCHAR(255),
    DATAVALIDATEDAT TIMESTAMP,
    DATAUPDATEDBY VARCHAR(255),
    DATAUPDATEDAT TIMESTAMP,
    CONSTRAINT uk_dataset_version UNIQUE (IDXDATASET, DATAVERSIONNUMBER),
    CONSTRAINT chk_version_format CHECK (DATAVERSIONNUMBER ~ '^[0-9]+\.[0-9]+\.[0-9]+(-[a-zA-Z0-9]+)?(\+[a-zA-Z0-9]+)?$')
);

-- Índices
CREATE INDEX idx_data_version_dataset ON DATADATASETVERSIONS(IDXDATASET);
CREATE INDEX idx_data_version_number ON DATADATASETVERSIONS(DATAVERSIONNUMBER);
CREATE INDEX idx_data_version_status ON DATADATASETVERSIONS(DATASTATUS);
CREATE INDEX idx_data_version_hash ON DATADATASETVERSIONS(DATAHASH);
CREATE INDEX idx_data_version_parent ON DATADATASETVERSIONS(DATAPARENTVERSION);

-- ============================================================================
-- MODIFICAR TRNTRAININGEXECUTIONS PARA USAR FK
-- ============================================================================
-- Agregar columna FK (mantener campo original para migración)
ALTER TABLE TRNTRAININGEXECUTIONS 
ADD COLUMN IDXDATASETVERSION BIGINT REFERENCES DATADATASETVERSIONS(IDXDATASETVERSION);

-- Crear índice
CREATE INDEX idx_trn_execution_dataset_version ON TRNTRAININGEXECUTIONS(IDXDATASETVERSION);

-- Migrar datos existentes (si aplica)
-- UPDATE TRNTRAININGEXECUTIONS SET IDXDATASETVERSION = ... 
-- WHERE DATASETVERSION IS NOT NULL;
-- (Requiere lógica de migración específica según datos existentes)
```

### 2. Crear Entidad JPA DatasetVersion

**Archivo:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/data/DatasetVersion.java`

```java
package com.codeflowx.govern.entity.data;

import com.codeflowx.govern.entity.base.BaseEntity;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "DATADATASETVERSIONS")
public class DatasetVersion implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXDATASETVERSION", nullable = false)
    private Long idxdatasetversion;
    
    @Column(name = "iduuid", unique = true, nullable = false, length = 36)
    private String iduuid;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDXDATASET", nullable = false)
    private Dataset dataset;
    
    @Column(name = "DATAVERSIONNUMBER", nullable = false, length = 50)
    private String dataversionnumber; // SemVer: 1.2.0
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DATAPARENTVERSION")
    private DatasetVersion dataparentversion;
    
    @Column(name = "DATADESCRIPTION", columnDefinition = "TEXT")
    private String datadescription;
    
    @Column(name = "DATACHANGELOG", columnDefinition = "TEXT")
    private String datachangelog; // Descripción de cambios
    
    @Column(name = "DATAHASH", nullable = false, length = 64)
    private String datahash; // SHA-256 checksum
    
    @Column(name = "DATAFILESIZE")
    private Long datafilesize; // Tamaño en bytes
    
    @Column(name = "DATAFILECOUNT")
    private Integer datafilecount; // Número de archivos
    
    @Column(name = "DATAROWCOUNT")
    private Long datarowcount; // Número de filas/registros
    
    @Column(name = "DATASTATISTICS", columnDefinition = "JSONB")
    private String datastatistics; // Estadísticas JSON
    
    @Column(name = "DATASTATUS", nullable = false, length = 50)
    private String datastatus; // DRAFT, VALIDATED, APPROVED, ARCHIVED
    
    @Column(name = "DATACREATEDBY", nullable = false, length = 255)
    private String datacreatedby;
    
    @Column(name = "DATACREATEDAT", nullable = false)
    private Timestamp datacreatedat;
    
    @Column(name = "DATAVALIDATEDBY", length = 255)
    private String datavalidatedby;
    
    @Column(name = "DATAVALIDATEDAT")
    private Timestamp datavalidatedat;
    
    @Column(name = "DATAUPDATEDBY", length = 255)
    private String dataupdatedby;
    
    @Column(name = "DATAUPDATEDAT")
    private Timestamp dataupdatedat;
    
    // Getters y Setters
    // ...
}
```

### 3. Modificar TrainingExecution para usar FK

**Archivo:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/training/TrainingExecution.java`

```java
// Agregar relación ManyToOne a DatasetVersion
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "IDXDATASETVERSION")
private DatasetVersion datasetVersion;

// Mantener campo original para compatibilidad (opcional)
@Column(name = "DATASETVERSION", nullable = true)
private String datasetversion; // Campo legacy, mantener para migración

// Getters y Setters
public DatasetVersion getDatasetVersion() {
    return datasetVersion;
}

public void setDatasetVersion(DatasetVersion datasetVersion) {
    this.datasetVersion = datasetVersion;
    // Sincronizar campo legacy
    if (datasetVersion != null) {
        this.datasetversion = datasetVersion.getDataversionnumber();
    }
}
```

### 4. Crear BusinessService para DatasetVersion

**Archivo:** `nocode.service.business/src/main/java/com/codeflowx/govern/business/data/DatasetVersionBusinessService.java`

```java
package com.codeflowx.govern.business.data;

import com.codeflowx.govern.entity.data.Dataset;
import com.codeflowx.govern.entity.data.DatasetVersion;
import com.codeflowx.govern.dao.data.DatasetVersionDao;
import com.codeflowx.govern.business.logging.ImmutableLoggingBusinessService;
import com.codeflowx.govern.exception.VersionExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class DatasetVersionBusinessService {
    
    @Autowired
    private DatasetVersionDao datasetVersionDao;
    
    @Autowired
    private ImmutableLoggingBusinessService loggingService;
    
    /**
     * Crea una nueva versión de dataset
     * Requisito: Art. 10 (Gobernanza de Datos), Art. 11 (Documentación Técnica)
     */
    public DatasetVersion createVersion(Long datasetId, DatasetVersion version, Long userId) {
        // Calcular hash del dataset
        String hash = calculateDatasetHash(version.getDatafiles());
        version.setDatahash(hash);
        
        // Verificar que versión no existe
        if (datasetVersionDao.existsByDatasetAndVersion(datasetId, version.getDataversionnumber())) {
            throw new VersionExistsException(
                "La versión " + version.getDataversionnumber() + 
                " ya existe para este dataset. Use un número de versión diferente."
            );
        }
        
        // Establecer datos básicos
        version.setDataset(getDataset(datasetId));
        version.setDatacreatedby(getUserName(userId));
        version.setDatastatus("DRAFT");
        version.setDatacreatedat(new Timestamp(System.currentTimeMillis()));
        version.setIduuid(java.util.UUID.randomUUID().toString());
        
        // Si hay versión padre, establecer relación
        if (version.getDataparentversion() != null) {
            DatasetVersion parent = datasetVersionDao.findById(
                version.getDataparentversion().getIdxdatasetversion()
            ).orElseThrow(() -> new IllegalArgumentException("Versión padre no encontrada"));
            version.setDataparentversion(parent);
        }
        
        DatasetVersion created = datasetVersionDao.save(version);
        
        // Log inmutable
        loggingService.logChange(
            "DATASET",
            created.getIdxdatasetversion(),
            "CREATE",
            userId,
            getUserName(userId),
            created
        );
        
        return created;
    }
    
    /**
     * Calcula hash SHA-256 del conjunto de archivos del dataset
     * Requisito: Verificación de integridad (Art. 10, GDPR Art. 30)
     */
    private String calculateDatasetHash(List<String> filePaths) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // Si hay múltiples archivos, concatenar hashes
            if (filePaths != null && !filePaths.isEmpty()) {
                for (String filePath : filePaths) {
                    Path path = Paths.get(filePath);
                    if (Files.exists(path)) {
                        try (InputStream is = Files.newInputStream(path)) {
                            byte[] buffer = new byte[8192];
                            int bytesRead;
                            while ((bytesRead = is.read(buffer)) != -1) {
                                digest.update(buffer, 0, bytesRead);
                            }
                        }
                    }
                }
            }
            
            byte[] hashBytes = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
            
        } catch (Exception e) {
            throw new RuntimeException("Error calculando hash del dataset: " + e.getMessage(), e);
        }
    }
    
    /**
     * Valida una versión de dataset
     * Requisito: Art. 10 (Gobernanza de Datos)
     */
    public void validateVersion(Long versionId, Long userId) {
        DatasetVersion version = datasetVersionDao.findById(versionId)
            .orElseThrow(() -> new IllegalArgumentException("Versión no encontrada: " + versionId));
        
        // Verificar integridad del hash
        String currentHash = calculateDatasetHash(version.getDatafiles());
        if (!currentHash.equals(version.getDatahash())) {
            throw new IllegalStateException(
                "Integridad del dataset comprometida. Hash calculado no coincide con hash almacenado."
            );
        }
        
        // Cambiar status a VALIDATED
        version.setDatastatus("VALIDATED");
        version.setDatavalidatedby(getUserName(userId));
        version.setDatavalidatedat(new Timestamp(System.currentTimeMillis()));
        datasetVersionDao.save(version);
        
        // Log inmutable
        loggingService.logChange(
            "DATASET",
            versionId,
            "VALIDATE",
            userId,
            getUserName(userId),
            version
        );
    }
    
    /**
     * Aprueba una versión de dataset
     */
    public void approveVersion(Long versionId, Long userId) {
        DatasetVersion version = datasetVersionDao.findById(versionId)
            .orElseThrow(() -> new IllegalArgumentException("Versión no encontrada: " + versionId));
        
        if (!version.getDatastatus().equals("VALIDATED")) {
            throw new IllegalStateException(
                "Solo se pueden aprobar versiones validadas. Estado actual: " + version.getDatastatus()
            );
        }
        
        version.setDatastatus("APPROVED");
        version.setDataupdatedby(getUserName(userId));
        version.setDataupdatedat(new Timestamp(System.currentTimeMillis()));
        datasetVersionDao.save(version);
        
        // Log inmutable
        loggingService.logChange(
            "DATASET",
            versionId,
            "APPROVE",
            userId,
            getUserName(userId),
            version
        );
    }
    
    /**
     * Obtiene todas las versiones de un dataset
     */
    public List<DatasetVersion> getVersionsByDataset(Long datasetId) {
        return datasetVersionDao.findByDatasetIdOrderByVersionDesc(datasetId);
    }
    
    /**
     * Obtiene versión específica por número de versión
     */
    public DatasetVersion getVersionByNumber(Long datasetId, String versionNumber) {
        return datasetVersionDao.findByDatasetAndVersion(datasetId, versionNumber)
            .orElseThrow(() -> new IllegalArgumentException(
                "Versión " + versionNumber + " no encontrada para dataset " + datasetId
            ));
    }
    
    /**
     * Calcula siguiente versión automáticamente (incrementa patch)
     */
    public String calculateNextVersion(Long datasetId) {
        List<DatasetVersion> versions = getVersionsByDataset(datasetId);
        if (versions.isEmpty()) {
            return "1.0.0";
        }
        
        String lastVersion = versions.get(0).getDataversionnumber();
        String[] parts = lastVersion.split("\\.");
        if (parts.length < 3) {
            return "1.0.0";
        }
        
        try {
            int patch = Integer.parseInt(parts[2].split("-")[0]) + 1;
            return parts[0] + "." + parts[1] + "." + patch;
        } catch (NumberFormatException e) {
            return "1.0.0";
        }
    }
    
    // Métodos auxiliares
    private Dataset getDataset(Long datasetId) {
        // TODO: Implementar obtención de dataset
        return null;
    }
    
    private String getUserName(Long userId) {
        // TODO: Implementar obtención de nombre de usuario
        return "USER_" + userId;
    }
}
```

### 5. Crear DAO para DatasetVersion

**Archivo:** `nocode.service.dao/src/main/java/com/codeflowx/govern/dao/data/DatasetVersionDao.java`

```java
package com.codeflowx.govern.dao.data;

import com.codeflowx.govern.entity.data.DatasetVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DatasetVersionDao extends JpaRepository<DatasetVersion, Long> {
    
    /**
     * Verifica si existe versión para un dataset
     */
    boolean existsByDatasetAndVersion(Long datasetId, String versionNumber);
    
    /**
     * Obtiene versión por dataset y número de versión
     */
    @Query("SELECT dv FROM DatasetVersion dv WHERE dv.dataset.idxdataset = :datasetId AND dv.dataversionnumber = :versionNumber")
    Optional<DatasetVersion> findByDatasetAndVersion(
        @Param("datasetId") Long datasetId,
        @Param("versionNumber") String versionNumber
    );
    
    /**
     * Obtiene todas las versiones de un dataset ordenadas por versión descendente
     */
    @Query("SELECT dv FROM DatasetVersion dv WHERE dv.dataset.idxdataset = :datasetId ORDER BY dv.dataversionnumber DESC")
    List<DatasetVersion> findByDatasetIdOrderByVersionDesc(@Param("datasetId") Long datasetId);
    
    /**
     * Obtiene versión por hash (para verificación de integridad)
     */
    Optional<DatasetVersion> findByDatahash(String hash);
}
```

### 6. Crear Exception Personalizada

**Archivo:** `nocode.service.business/src/main/java/com/codeflowx/govern/exception/VersionExistsException.java`

```java
package com.codeflowx.govern.exception;

public class VersionExistsException extends RuntimeException {
    public VersionExistsException(String message) {
        super(message);
    }
}
```

---

## VALIDACIONES ADICIONALES RECOMENDADAS

1. **Validación automática de formato SemVer** al crear versión
2. **Verificación de integridad periódica** comparando hash actual con hash almacenado
3. **Estadísticas automáticas** del dataset (distribución, métricas de calidad)
4. **Migración de datos existentes** desde campo `DATASETVERSION` VARCHAR a FK

---

## PRUEBAS REQUERIDAS

1. **Test 1:** Crear nueva versión de dataset → Debe calcular hash y guardar correctamente
2. **Test 2:** Intentar crear versión duplicada → Debe lanzar `VersionExistsException`
3. **Test 3:** Validar versión → Debe verificar hash y cambiar status a VALIDATED
4. **Test 4:** Aprobar versión sin validar → Debe lanzar excepción
5. **Test 5:** Obtener versiones por dataset → Debe retornar lista ordenada
6. **Test 6:** Calcular siguiente versión automáticamente → Debe incrementar patch correctamente
7. **Test 7:** Verificar integridad con hash incorrecto → Debe detectar corrupción

---

## LOGS INMUTABLES

Añadir logs inmutables en todas las operaciones:

```java
// En createVersion()
loggingService.logChange(
    "DATASET",
    created.getIdxdatasetversion(),
    "CREATE",
    userId,
    getUserName(userId),
    Map.of(
        "version", created.getDataversionnumber(),
        "hash", created.getDatahash(),
        "file_count", created.getDatafilecount(),
        "row_count", created.getDatarowcount()
    )
);

// En validateVersion()
loggingService.logChange(
    "DATASET",
    versionId,
    "VALIDATE",
    userId,
    getUserName(userId),
    Map.of(
        "hash_verified", true,
        "previous_status", version.getDatastatus()
    )
);
```

---

## REFERENCIAS

- **Art. 10 EU AI Act:** Gobernanza de Datos
- **Art. 11 EU AI Act:** Documentación Técnica
- **GDPR Art. 30:** Registro de actividades de tratamiento
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_VERSIONADO.md#inc-011-02`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_011_VERSIONADO_CONTROL_CAMBIOS.md`

---

## NOTAS DE IMPLEMENTACIÓN

- Ajustar método `calculateDatasetHash()` según ubicación real de archivos de datasets (storage, S3, etc.)
- Implementar método `getDataset()` según DAO real de datasets
- Considerar usar librería de SemVer (ej: `com.github.zafarkhaja:java-semver`) para validación y cálculo de versiones
- La migración de datos existentes requiere script SQL específico según estructura de datos actual
- Considerar agregar campo `DATALOCATION` (path/URL) para almacenar ubicación de archivos del dataset

---

**Estado:** ✅ COMPLETADO  
**Esfuerzo Estimado:** 4-5 días  
**Responsable:** Backend Team + DBA Team

