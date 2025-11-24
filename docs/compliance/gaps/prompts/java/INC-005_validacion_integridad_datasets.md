# PROMPT: INC-005 - Validación de Integridad de Datasets

**Incidencia:** INC-005  
**Prioridad:** 🟠 HIGH  
**Artículo:** ISO 42001 8.2.2 (data governance)  
**Esfuerzo Estimado:** 1-2 días  
**Tipo:** Java - Backend + Python - Microservicio

---

## CONTEXTO

El sistema no valida checksums o hashes de datasets subidos, lo que impide detectar corrupción de datos o manipulación durante transferencia.

**Ubicación Actual:**
- `bias-detection-service/main.py:307-315` - Solo valida tamaño y formato CSV
- `DatasetQuality.java` - No tiene campo para hash
- No hay validación de integridad (SHA-256, MD5, etc.)

---

## REQUISITOS

1. Calcular hash SHA-256 del dataset al subir
2. Almacenar hash en `DQLDATASETQUALITIES.DQLDATASETHASH`
3. Validar hash antes de procesar evaluación
4. Comparar hash con versión original si existe
5. Generar alerta si hash no coincide

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Extender Entidad `DatasetQuality.java`

**Archivo:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/governance/DatasetQuality.java`

**Añadir campo:**

```java
// Hash SHA-256 del dataset para validación de integridad
@Column(name = "DQLDATASETHASH", length = 64)
private String dqldatasethash;  // SHA-256 hash (64 caracteres hex)

@Column(name = "DQLHASHALGORITHM", length = 20)
private String dqlhashalgorithm = "SHA-256";  // Algoritmo usado

@Column(name = "DQLHASHVERIFIED", nullable = false)
private Boolean dqlhashverified = false;  // Si hash fue verificado

@Column(name = "DQLPREVIOUSHASH", length = 64)
private String dqlprevioushash;  // Hash de versión anterior para comparación
```

### 2. Script SQL para añadir campos

**Archivo:** `sql-scripts/patches/11_dataset_quality_hash_fields.sql`

```sql
-- Añadir campos de hash a DQLDATASETQUALITY
ALTER TABLE DQLDATASETQUALITY 
ADD COLUMN IF NOT EXISTS DQLDATASETHASH VARCHAR(64),
ADD COLUMN IF NOT EXISTS DQLHASHALGORITHM VARCHAR(20) DEFAULT 'SHA-256',
ADD COLUMN IF NOT EXISTS DQLHASHVERIFIED BOOLEAN DEFAULT FALSE,
ADD COLUMN IF NOT EXISTS DQLPREVIOUSHASH VARCHAR(64);

-- Índice para búsqueda por hash
CREATE INDEX IF NOT EXISTS IDX_DQLDATASETQUALITY_HASH 
ON DQLDATASETQUALITY(DQLDATASETHASH);

-- Comentarios
COMMENT ON COLUMN DQLDATASETQUALITY.DQLDATASETHASH IS 'SHA-256 hash del dataset para validación de integridad';
COMMENT ON COLUMN DQLDATASETQUALITY.DQLHASHALGORITHM IS 'Algoritmo de hash usado (SHA-256, MD5, etc.)';
COMMENT ON COLUMN DQLDATASETQUALITY.DQLHASHVERIFIED IS 'Si el hash fue verificado correctamente';
COMMENT ON COLUMN DQLDATASETQUALITY.DQLPREVIOUSHASH IS 'Hash de versión anterior para comparación';
```

### 3. Crear Utilidad Java para Hash

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/utils/DatasetHashUtil.java`

```java
package com.codeflowx.govern.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilidad para calcular hash SHA-256 de datasets
 * ISO 42001 8.2.2 - Data governance
 */
@Slf4j
@Component
public class DatasetHashUtil {
    
    private static final String ALGORITHM = "SHA-256";
    
    /**
     * Calcula hash SHA-256 de un InputStream
     * 
     * @param inputStream Stream del dataset
     * @return Hash SHA-256 en hexadecimal (64 caracteres)
     */
    public String calculateHash(InputStream inputStream) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] buffer = new byte[8192];  // 8 KB buffer
            int bytesRead;
            
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
            
            byte[] hashBytes = digest.digest();
            return bytesToHex(hashBytes);
            
        } catch (Exception e) {
            log.error("Error calculating hash: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to calculate dataset hash", e);
        }
    }
    
    /**
     * Calcula hash SHA-256 de un array de bytes
     */
    public String calculateHash(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hashBytes = digest.digest(data);
            return bytesToHex(hashBytes);
            
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-256 algorithm not available: {}", e.getMessage());
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
    
    /**
     * Verifica que un hash coincide con el esperado
     */
    public boolean verifyHash(String calculatedHash, String expectedHash) {
        if (calculatedHash == null || expectedHash == null) {
            return false;
        }
        return calculatedHash.equalsIgnoreCase(expectedHash);
    }
    
    /**
     * Convierte bytes a hexadecimal
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
```

### 4. Modificar Delegate Java para calcular hash

**Archivo:** `nocode.service/codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/delegates/dataset/ValidateDatasetFormatDelegate.java`

```java
@Autowired
private DatasetHashUtil datasetHashUtil;

@Override
public void execute(DelegateExecution execution) {
    String datasetPath = (String) execution.getVariable("datasetPath");
    String datasetId = (String) execution.getVariable("datasetId");
    
    log.info("Validating dataset format and calculating hash for dataset: {}", datasetId);
    
    try {
        // Leer archivo
        byte[] fileContent = readDatasetFile(datasetPath);
        
        // Calcular hash
        String hash = datasetHashUtil.calculateHash(fileContent);
        log.info("Calculated hash for dataset {}: {}", datasetId, hash);
        
        // Guardar hash en variable BPMN
        execution.setVariable("datasetHash", hash);
        execution.setVariable("hashAlgorithm", "SHA-256");
        
        // Verificar si existe versión anterior
        String previousHash = getPreviousHash(datasetId);
        if (previousHash != null) {
            boolean hashMatches = datasetHashUtil.verifyHash(hash, previousHash);
            execution.setVariable("hashMatchesPrevious", hashMatches);
            
            if (!hashMatches) {
                log.warn("Dataset {} hash does not match previous version. "
                        + "Current: {}, Previous: {}", datasetId, hash, previousHash);
                execution.setVariable("hashWarning", true);
            }
        }
        
    } catch (Exception e) {
        log.error("Error validating dataset format: {}", e.getMessage(), e);
        execution.setVariable("validationError", e.getMessage());
        throw new BpmnError("DATASET_VALIDATION_ERROR", e.getMessage());
    }
}
```

### 5. Modificar StoreEvaluationDelegate para guardar hash

**Archivo:** `nocode.service/codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/delegates/StoreEvaluationDelegate.java`

```java
@Override
public void execute(DelegateExecution execution) {
    // ... código existente ...
    
    // Obtener hash de variables BPMN
    String datasetHash = (String) execution.getVariable("datasetHash");
    String hashAlgorithm = (String) execution.getVariable("hashAlgorithm");
    Boolean hashVerified = (Boolean) execution.getVariable("hashMatchesPrevious");
    String previousHash = (String) execution.getVariable("previousHash");
    
    // Guardar en DatasetQuality
    DatasetQuality quality = new DatasetQuality();
    // ... campos existentes ...
    
    // Hash fields
    quality.setDqldatasethash(datasetHash);
    quality.setDqlhashalgorithm(hashAlgorithm != null ? hashAlgorithm : "SHA-256");
    quality.setDqlhashverified(hashVerified != null ? hashVerified : false);
    quality.setDqlprevioushash(previousHash);
    
    businessService.save(quality);
    
    log.info("Saved dataset quality evaluation with hash: {}", datasetHash);
}
```

### 6. Modificar Microservicio Python para calcular hash

**Archivo:** `bias-detection-service/main.py`

```python
import hashlib
from typing import Optional

def calculate_sha256_hash(file_content: bytes) -> str:
    """
    Calcula hash SHA-256 de contenido de archivo
    
    Args:
        file_content: Contenido del archivo en bytes
        
    Returns:
        Hash SHA-256 en hexadecimal (64 caracteres)
    """
    sha256_hash = hashlib.sha256(file_content)
    return sha256_hash.hexdigest()

@app.post("/api/data-quality/validate")
async def validate_data_quality(
    file: UploadFile = File(...),
    ...
):
    """
    Validación de calidad con cálculo de hash
    """
    try:
        # Leer archivo
        contents = await file.read()
        
        # Calcular hash
        dataset_hash = calculate_sha256_hash(contents)
        logger.info(f"Calculated hash for dataset: {dataset_hash}")
        
        # Validar hash si se proporciona uno esperado
        expected_hash = form_data.get("expectedHash") if "expectedHash" in form_data else None
        if expected_hash:
            if dataset_hash != expected_hash:
                raise HTTPException(
                    status_code=400,
                    detail=f"Dataset hash mismatch. Expected: {expected_hash}, Got: {dataset_hash}"
                )
            logger.info("Hash verification passed")
        
        # Procesar validación
        result = await process_validation(contents, ...)
        
        # Añadir hash al resultado
        result["dataset_hash"] = dataset_hash
        result["hash_algorithm"] = "SHA-256"
        result["hash_verified"] = expected_hash is not None and dataset_hash == expected_hash
        
        return result
        
    except Exception as e:
        logger.error(f"Error in validation: {str(e)}")
        raise HTTPException(status_code=500, detail=f"Validation failed: {str(e)}")
```

### 7. Crear Service Java para verificación de hash

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/business/governance/DatasetHashVerificationService.java`

```java
package com.codeflowx.govern.business.governance;

import com.codeflowx.govern.entity.governance.DatasetQuality;
import com.codeflowx.govern.utils.DatasetHashUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * Service para verificación de integridad de datasets
 * ISO 42001 8.2.2 - Data governance
 */
@Slf4j
@Service
public class DatasetHashVerificationService {
    
    @Autowired
    private DatasetHashUtil datasetHashUtil;
    
    @Autowired
    private BusinessService businessService;
    
    /**
     * Verifica integridad de un dataset comparando hash
     */
    public HashVerificationResult verifyDatasetIntegrity(
        String datasetId,
        InputStream datasetStream
    ) {
        // Obtener evaluación anterior
        DatasetQuality previousEvaluation = getPreviousEvaluation(datasetId);
        
        if (previousEvaluation == null) {
            log.info("No previous evaluation found for dataset: {}", datasetId);
            return HashVerificationResult.noPrevious();
        }
        
        // Calcular hash actual
        String currentHash = datasetHashUtil.calculateHash(datasetStream);
        String previousHash = previousEvaluation.getDqldatasethash();
        
        // Verificar
        boolean matches = datasetHashUtil.verifyHash(currentHash, previousHash);
        
        HashVerificationResult result = new HashVerificationResult();
        result.setCurrentHash(currentHash);
        result.setPreviousHash(previousHash);
        result.setMatches(matches);
        result.setVerified(true);
        
        if (!matches) {
            log.warn("Hash mismatch for dataset {}: current={}, previous={}", 
                    datasetId, currentHash, previousHash);
            result.setWarning("Dataset hash does not match previous version. "
                            + "Dataset may have been modified or corrupted.");
        }
        
        return result;
    }
    
    private DatasetQuality getPreviousEvaluation(String datasetId) {
        // Buscar última evaluación del dataset
        String query = "SELECT dq FROM DatasetQuality dq "
                      + "WHERE dq.dqldatasetname = :datasetId "
                      + "ORDER BY dq.dqlcreatedat DESC";
        
        List<DatasetQuality> results = businessService.findByQuery(
            DatasetQuality.class, 
            query, 
            Map.of("datasetId", datasetId),
            1
        );
        
        return results.isEmpty() ? null : results.get(0);
    }
}
```

---

## VALIDACIONES

1. ✅ Hash SHA-256 se calcula correctamente
2. ✅ Hash se almacena en BD
3. ✅ Hash se compara con versión anterior si existe
4. ✅ Alerta si hash no coincide
5. ✅ Hash se incluye en respuesta de API

---

## TESTING

```java
// tests/DatasetHashUtilTest.java

@Test
public void testCalculateHash() {
    byte[] data = "test dataset content".getBytes();
    String hash = datasetHashUtil.calculateHash(data);
    assertNotNull(hash);
    assertEquals(64, hash.length());  // SHA-256 = 64 hex chars
}

@Test
public void testVerifyHash() {
    String hash1 = datasetHashUtil.calculateHash("test".getBytes());
    String hash2 = datasetHashUtil.calculateHash("test".getBytes());
    assertTrue(datasetHashUtil.verifyHash(hash1, hash2));
}
```

---

## DOCUMENTACIÓN

Actualizar:
- `DatasetQuality.java` - JavaDoc de campos hash
- `docs/compliance/auditoria/AUDITORIA_EVALUACION_DATASETS.md` - Validación de integridad
- `bias-detection-service/README.md` - Hash en respuesta API

---

## CUMPLIMIENTO ISO 42001

**8.2.2:** Data governance
- ✅ Validación de integridad de datos
- ✅ Trazabilidad de cambios en datasets
- ✅ Detección de corrupción o manipulación

---

**Última actualización:** Noviembre 2025  
**Versión:** 1.0  
**Responsable:** Backend Team

---

**Estado:** ✅ COMPLETADO

