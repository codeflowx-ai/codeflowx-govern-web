# PROMPT: INC-INT-003 - Validación de Integridad de Metadata Sincronizada

**Incidencia:** INC-INT-003  
**Prioridad:** 🟠 ALTA  
**Artículo EU AI Act:** Art. 19 (Registros), Art. 12 (Trazabilidad)  
**Esfuerzo Estimado:** 2-3 días  
**Tipo:** Java - Backend  
**Documento Relacionado:** `AUDITORIA_INTEGRACION_SIN_SUSTITUCION.md`

---

## CONTEXTO

No existe mecanismo para validar que la metadata sincronizada desde plataformas externas (Databricks, Snowflake, Azure ML) no ha sido manipulada o corrompida durante el proceso de sync. Esto compromete la trazabilidad para auditorías regulatorias.

**Riesgo:**
- Si la metadata está corrompida, las evaluaciones de compliance pueden ser incorrectas
- No se puede detectar si Databricks/Snowflake devuelve datos inconsistentes
- Trazabilidad comprometida para auditorías regulatorias

**Ubicación Actual:**
- `ExternalModel.java` - tabla `EXMEXTERNALMODELS` sin campo de hash
- `DatabricksConnectorService.java` - sync sin validación de integridad
- `SnowflakeConnectorService.java` - sync sin validación de integridad

---

## REQUISITOS

1. **Implementar hash SHA-256** de metadata sincronizada
2. **Validar integridad** antes/después de cada sync
3. **Alertar** si hash no coincide (posible corrupción)
4. **Registrar en ImmutableLog** si se detecta inconsistencia
5. **Añadir campo** `EXMMETADATA_HASH` a tabla `EXMEXTERNALMODELS`
6. **Añadir campo** `EXDMETADATA_HASH` a tabla `EXDEXTERNALDATASETS`

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear `MetadataIntegrityService.java`

**Ubicación:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/security/MetadataIntegrityService.java`

```java
package com.codeflowx.govern.security;

import com.codeflowx.govern.entity.integrations.ExternalModel;
import com.codeflowx.govern.entity.integrations.ExternalDataset;
import com.codeflowx.govern.service.logging.ImmutableLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

/**
 * Servicio para validar integridad de metadata sincronizada desde plataformas externas.
 * 
 * Usa hash SHA-256 para detectar corrupción o manipulación de metadata.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MetadataIntegrityService {

    private final ObjectMapper objectMapper;
    private final ImmutableLogService immutableLogService;

    /**
     * Calcula hash SHA-256 de metadata de modelo externo.
     * 
     * @param model Modelo externo
     * @return Hash SHA-256 en hexadecimal
     */
    public String calculateMetadataHash(ExternalModel model) {
        if (model == null || model.getExmmetadata() == null) {
            return null;
        }

        try {
            // Normalizar JSON para asegurar consistencia (orden de campos)
            String metadataJson = objectMapper.writeValueAsString(
                objectMapper.readTree(model.getExmmetadata())
            );

            // Calcular hash SHA-256
            return DigestUtils.sha256Hex(metadataJson);

        } catch (Exception e) {
            log.error("Error calculating metadata hash for model {}: {}", 
                     model.getExmexternalid(), e.getMessage(), e);
            return null;
        }
    }

    /**
     * Calcula hash SHA-256 de metadata de dataset externo.
     * 
     * @param dataset Dataset externo
     * @return Hash SHA-256 en hexadecimal
     */
    public String calculateMetadataHash(ExternalDataset dataset) {
        if (dataset == null || dataset.getExdmetadata() == null) {
            return null;
        }

        try {
            // Normalizar JSON para asegurar consistencia
            String metadataJson = objectMapper.writeValueAsString(
                objectMapper.readTree(dataset.getExdmetadata())
            );

            // Calcular hash SHA-256
            return DigestUtils.sha256Hex(metadataJson);

        } catch (Exception e) {
            log.error("Error calculating metadata hash for dataset {}: {}", 
                     dataset.getExdexternalid(), e.getMessage(), e);
            return null;
        }
    }

    /**
     * Verifica integridad de metadata de modelo externo.
     * 
     * @param model Modelo externo
     * @return true si hash coincide, false si hay inconsistencia
     */
    public boolean verifyIntegrity(ExternalModel model) {
        if (model == null) {
            return false;
        }

        String currentHash = calculateMetadataHash(model);
        String storedHash = model.getExmmetadataHash();

        // Si no hay hash almacenado, considerar válido (primer sync)
        if (storedHash == null || storedHash.isEmpty()) {
            log.debug("No stored hash for model {}, assuming valid (first sync)", 
                     model.getExmexternalid());
            return true;
        }

        // Comparar hashes
        boolean isValid = currentHash != null && currentHash.equals(storedHash);

        if (!isValid) {
            log.warn("⚠️ Metadata integrity violation detected for model {}: storedHash={}, currentHash={}", 
                    model.getExmexternalid(), storedHash, currentHash);
            
            // Registrar en ImmutableLog
            registerIntegrityViolation(model, storedHash, currentHash);
        }

        return isValid;
    }

    /**
     * Verifica integridad de metadata de dataset externo.
     * 
     * @param dataset Dataset externo
     * @return true si hash coincide, false si hay inconsistencia
     */
    public boolean verifyIntegrity(ExternalDataset dataset) {
        if (dataset == null) {
            return false;
        }

        String currentHash = calculateMetadataHash(dataset);
        String storedHash = dataset.getExdmetadataHash();

        if (storedHash == null || storedHash.isEmpty()) {
            log.debug("No stored hash for dataset {}, assuming valid (first sync)", 
                     dataset.getExdexternalid());
            return true;
        }

        boolean isValid = currentHash != null && currentHash.equals(storedHash);

        if (!isValid) {
            log.warn("⚠️ Metadata integrity violation detected for dataset {}: storedHash={}, currentHash={}", 
                    dataset.getExdexternalid(), storedHash, currentHash);
            
            registerIntegrityViolation(dataset, storedHash, currentHash);
        }

        return isValid;
    }

    /**
     * Actualiza hash de metadata después de sync.
     * 
     * @param model Modelo externo actualizado
     */
    public void updateMetadataHash(ExternalModel model) {
        String hash = calculateMetadataHash(model);
        model.setExmmetadataHash(hash);
        log.debug("Updated metadata hash for model {}: {}", 
                 model.getExmexternalid(), hash);
    }

    /**
     * Actualiza hash de metadata después de sync.
     * 
     * @param dataset Dataset externo actualizado
     */
    public void updateMetadataHash(ExternalDataset dataset) {
        String hash = calculateMetadataHash(dataset);
        dataset.setExdmetadataHash(hash);
        log.debug("Updated metadata hash for dataset {}: {}", 
                 dataset.getExdexternalid(), hash);
    }

    /**
     * Registra violación de integridad en ImmutableLog.
     */
    private void registerIntegrityViolation(ExternalModel model, 
                                            String storedHash, 
                                            String currentHash) {
        try {
            String violationDetails = String.format(
                "{\"entity_type\":\"EXTERNAL_MODEL\",\"entity_id\":\"%s\",\"platform\":\"%s\"," +
                "\"stored_hash\":\"%s\",\"current_hash\":\"%s\",\"violation_type\":\"METADATA_INTEGRITY\"}",
                model.getExmexternalid(),
                model.getPlatform() != null ? model.getPlatform().getEplplatformname() : "UNKNOWN",
                storedHash,
                currentHash
            );

            immutableLogService.logEvent(
                "EXTERNAL_MODEL",
                "METADATA_INTEGRITY_VIOLATION",
                model.getIduuid(),
                violationDetails,
                "SYSTEM"
            );

            log.info("✅ Integrity violation logged in ImmutableLog for model: {}", 
                    model.getExmexternalid());

        } catch (Exception e) {
            log.error("Error logging integrity violation: {}", e.getMessage(), e);
        }
    }

    /**
     * Registra violación de integridad en ImmutableLog.
     */
    private void registerIntegrityViolation(ExternalDataset dataset, 
                                            String storedHash, 
                                            String currentHash) {
        try {
            String violationDetails = String.format(
                "{\"entity_type\":\"EXTERNAL_DATASET\",\"entity_id\":\"%s\",\"platform\":\"%s\"," +
                "\"stored_hash\":\"%s\",\"current_hash\":\"%s\",\"violation_type\":\"METADATA_INTEGRITY\"}",
                dataset.getExdexternalid(),
                dataset.getPlatform() != null ? dataset.getPlatform().getEplplatformname() : "UNKNOWN",
                storedHash,
                currentHash
            );

            immutableLogService.logEvent(
                "EXTERNAL_DATASET",
                "METADATA_INTEGRITY_VIOLATION",
                dataset.getIduuid(),
                violationDetails,
                "SYSTEM"
            );

            log.info("✅ Integrity violation logged in ImmutableLog for dataset: {}", 
                    dataset.getExdexternalid());

        } catch (Exception e) {
            log.error("Error logging integrity violation: {}", e.getMessage(), e);
        }
    }
}
```

### 2. Modificar Entidades JPA

**Modificar `ExternalModel.java`:**

```java
@Entity
@Table(name = "EXMEXTERNALMODELS")
public class ExternalModel {
    
    // ... campos existentes ...

    @Column(name = "EXMMETADATA_HASH", length = 64)
    private String exmmetadataHash;  // SHA-256 hash de metadata

    // Getters/setters
    public String getExmmetadataHash() {
        return exmmetadataHash;
    }

    public void setExmmetadataHash(String exmmetadataHash) {
        this.exmmetadataHash = exmmetadataHash;
    }
}
```

**Modificar `ExternalDataset.java`:**

```java
@Entity
@Table(name = "EXDEXTERNALDATASETS")
public class ExternalDataset {
    
    // ... campos existentes ...

    @Column(name = "EXDMETADATA_HASH", length = 64)
    private String exdmetadataHash;  // SHA-256 hash de metadata

    // Getters/setters
    public String getExdmetadataHash() {
        return exdmetadataHash;
    }

    public void setExdmetadataHash(String exdmetadataHash) {
        this.exdmetadataHash = exdmetadataHash;
    }
}
```

### 3. Actualizar Servicios de Sincronización

**Modificar `DatabricksConnectorService.java`:**

```java
@Service
@RequiredArgsConstructor
public class DatabricksConnectorService {

    private final MetadataIntegrityService integrityService;

    private void processModel(ExternalPlatformIntegration platform, JsonNode modelNode) {
        // ... código existente ...

        // Guardar metadata
        String metadataJson = writeMetadata(modelNode);
        model.setExmmetadata(metadataJson);

        // 1. Verificar integridad si es modelo existente
        if (existing != null) {
            boolean isValid = integrityService.verifyIntegrity(model);
            if (!isValid) {
                log.warn("⚠️ Metadata integrity violation detected for model: {}", 
                        model.getExmexternalname());
                // Continuar con sync pero alertar
            }
        }

        // 2. Actualizar hash después de guardar metadata
        integrityService.updateMetadataHash(model);

        // Guardar modelo
        integrationService.saveExternalModel(model);

        log.info("✅ Model synced with integrity validation: {}", model.getExmexternalname());
    }
}
```

**Modificar `SnowflakeConnectorService.java`:**

```java
@Service
@RequiredArgsConstructor
public class SnowflakeConnectorService {

    private final MetadataIntegrityService integrityService;

    public List<ExternalDataset> catalogDatasetsFromSnowflake(...) {
        // ... código existente ...

        // Para cada dataset
        ExternalDataset dataset = new ExternalDataset();
        dataset.setExdmetadata(metadataJson);

        // 1. Verificar integridad si es dataset existente
        if (existing != null) {
            boolean isValid = integrityService.verifyIntegrity(dataset);
            if (!isValid) {
                log.warn("⚠️ Metadata integrity violation detected for dataset: {}", 
                        dataset.getExdname());
            }
        }

        // 2. Actualizar hash
        integrityService.updateMetadataHash(dataset);

        businessService.save(dataset);
    }
}
```

### 4. Crear SQL Migration

**Ubicación:** `nocode.service.entitys/src/main/resources/sql/migrations/V1_XX__add_metadata_hash.sql`

```sql
-- Añadir campo de hash de metadata para validación de integridad

-- Tabla: External Models
ALTER TABLE EXMEXTERNALMODELS 
ADD COLUMN IF NOT EXISTS EXMMETADATA_HASH VARCHAR(64);  -- SHA-256

-- Tabla: External Datasets
ALTER TABLE EXDEXTERNALDATASETS 
ADD COLUMN IF NOT EXISTS EXDMETADATA_HASH VARCHAR(64);  -- SHA-256

-- Índices para búsqueda rápida
CREATE INDEX IF NOT EXISTS idx_external_models_metadata_hash 
ON EXMEXTERNALMODELS(EXMMETADATA_HASH);

CREATE INDEX IF NOT EXISTS idx_external_datasets_metadata_hash 
ON EXDEXTERNALDATASETS(EXDMETADATA_HASH);

-- Comentarios
COMMENT ON COLUMN EXMEXTERNALMODELS.EXMMETADATA_HASH IS 
'SHA-256 hash de metadata para validación de integridad';

COMMENT ON COLUMN EXDEXTERNALDATASETS.EXDMETADATA_HASH IS 
'SHA-256 hash de metadata para validación de integridad';
```

### 5. Crear Job de Verificación Programada

**Crear:** `MetadataIntegrityVerificationJob.java`

```java
@Component
@Slf4j
@RequiredArgsConstructor
public class MetadataIntegrityVerificationJob {

    private final ExternalIntegrationBusinessService integrationService;
    private final MetadataIntegrityService integrityService;

    /**
     * Job programado para verificar integridad de metadata sincronizada.
     * Ejecuta diariamente a las 3 AM.
     */
    @Scheduled(cron = "0 0 3 * * ?")  // Diario a las 3 AM
    public void verifyAllMetadataIntegrity() {
        log.info("Starting metadata integrity verification job...");

        int violations = 0;

        // Verificar modelos externos
        List<ExternalModel> models = integrationService.findAllExternalModels();
        for (ExternalModel model : models) {
            if (!integrityService.verifyIntegrity(model)) {
                violations++;
            }
        }

        // Verificar datasets externos
        List<ExternalDataset> datasets = integrationService.findAllExternalDatasets();
        for (ExternalDataset dataset : datasets) {
            if (!integrityService.verifyIntegrity(dataset)) {
                violations++;
            }
        }

        log.info("Metadata integrity verification completed: {} violations detected", violations);

        if (violations > 0) {
            // Enviar alerta a administradores
            sendAlertToAdmins(violations);
        }
    }

    private void sendAlertToAdmins(int violations) {
        // TODO: Implementar notificación a administradores
        log.warn("⚠️ {} metadata integrity violations detected", violations);
    }
}
```

---

## VALIDACIÓN

### Tests Unitarios

**Crear:** `MetadataIntegrityServiceTest.java`

```java
@ExtendWith(MockitoExtension.class)
class MetadataIntegrityServiceTest {

    @Mock
    private ImmutableLogService immutableLogService;

    @InjectMocks
    private MetadataIntegrityService integrityService;

    @Test
    void testCalculateMetadataHash_Success() {
        // Given
        ExternalModel model = new ExternalModel();
        model.setExmmetadata("{\"name\":\"test-model\",\"version\":\"1.0\"}");

        // When
        String hash = integrityService.calculateMetadataHash(model);

        // Then
        assertThat(hash).isNotNull();
        assertThat(hash).hasSize(64); // SHA-256 = 64 caracteres hex
    }

    @Test
    void testVerifyIntegrity_Success() {
        // Given
        ExternalModel model = new ExternalModel();
        model.setExmmetadata("{\"name\":\"test-model\"}");
        integrityService.updateMetadataHash(model);
        String storedHash = model.getExmmetadataHash();

        // When
        boolean isValid = integrityService.verifyIntegrity(model);

        // Then
        assertThat(isValid).isTrue();
        assertThat(model.getExmmetadataHash()).isEqualTo(storedHash);
    }

    @Test
    void testVerifyIntegrity_Violation() {
        // Given
        ExternalModel model = new ExternalModel();
        model.setExmmetadata("{\"name\":\"test-model\"}");
        integrityService.updateMetadataHash(model);
        
        // Modificar metadata (simular corrupción)
        model.setExmmetadata("{\"name\":\"corrupted-model\"}");

        // When
        boolean isValid = integrityService.verifyIntegrity(model);

        // Then
        assertThat(isValid).isFalse();
        verify(immutableLogService).logEvent(
            eq("EXTERNAL_MODEL"),
            eq("METADATA_INTEGRITY_VIOLATION"),
            anyString(),
            anyString(),
            eq("SYSTEM")
        );
    }
}
```

---

## CUMPLIMIENTO EU AI ACT

| Artículo | Cobertura |
|----------|-----------|
| **Art. 19** (Registros) | ✅ Validación de integridad de metadata registrada |
| **Art. 12** (Trazabilidad) | ✅ Hash permite verificar que metadata no ha sido manipulada |

---

**Prioridad:** 🟠 **ALTA**  
**Fecha Límite:** Enero 2026  
**Responsable:** Java Backend Team

---

**Estado:** ✅ COMPLETADO

