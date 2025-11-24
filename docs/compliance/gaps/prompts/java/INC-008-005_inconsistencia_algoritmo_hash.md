# PROMPT: INC-008-005 - Inconsistencia en Algoritmo de Hash (Java vs PostgreSQL)
## EU AI Act Art. 19 - Registro Inmutable

**Incidencia:** INC-008-005  
**Prioridad:** 🟡 MEDIA  
**Artículo EU AI Act:** Art. 19 (precisión)  
**Esfuerzo Estimado:** 0.5 días  
**Tipo:** Java - Backend + SQL

---

## CONTEXTO

Java usa `timestampEpoch` (Long) en hash, PostgreSQL usa `timestamp::TEXT`. Ambos son válidos, pero deberían ser consistentes para evitar confusión.

**Ubicación Actual:**
- `ImmutableLoggingBusinessService.java` - método `calculateHash()` usa `timestampEpoch` (Long)
- Trigger PostgreSQL `iml_calculate_hash()` usa `timestamp::TEXT`

---

## REQUISITOS

1. **Estandarizar algoritmo:**
   - Usar mismo orden de campos en ambos
   - Usar mismo formato de timestamp (epoch o ISO 8601)
   - Documentar formalmente el algoritmo

2. **Crear función SQL de referencia:**
   - Función SQL que implemente el mismo algoritmo que Java
   - Usar para verificación y documentación

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Función SQL de Referencia

**Archivo:** `nocode.service.entitys/src/main/resources/sql/functions/iml_calculate_hash_reference.sql` (NUEVO)

```sql
-- Función SQL de referencia para cálculo de hash
-- Debe coincidir exactamente con ImmutableLoggingBusinessService.calculateHash()
-- EU AI Act Art. 19 - Precisión

CREATE OR REPLACE FUNCTION iml_calculate_hash_reference(
    prev_hash VARCHAR(64),
    timestamp_epoch BIGINT,
    entity_type VARCHAR(50),
    entity_id BIGINT,
    action VARCHAR(100),
    user_id BIGINT,
    data TEXT
) RETURNS VARCHAR(64) AS $$
BEGIN
    -- Algoritmo: SHA-256 de concatenación de campos
    -- Orden: previousHash || timestampEpoch || entityType || entityId || action || userId || data
    -- IMPORTANTE: timestamp_epoch es BIGINT (no TEXT) para coincidir con Java
    
    RETURN encode(
        digest(
            prev_hash ||
            timestamp_epoch::TEXT ||  -- Convertir BIGINT a TEXT para concatenación
            entity_type ||
            entity_id::TEXT ||
            action ||
            user_id::TEXT ||
            COALESCE(data, ''),
            'sha256'
        ),
        'hex'
    );
END;
$$ LANGUAGE plpgsql IMMUTABLE;

-- Comentario de documentación
COMMENT ON FUNCTION iml_calculate_hash_reference IS 
'Función de referencia para cálculo de hash SHA-256 de logs inmutables. 
Implementa el mismo algoritmo que ImmutableLoggingBusinessService.calculateHash() en Java.
Orden de campos: previousHash || timestampEpoch || entityType || entityId || action || userId || data
EU AI Act Art. 19 - Precisión';
```

### 2. Actualizar Trigger PostgreSQL

**Archivo:** `nocode.service.entitys/src/main/resources/sql/immutable_log.sql` (MODIFICAR)

Modificar función `iml_calculate_hash()` para usar el mismo algoritmo:

```sql
CREATE OR REPLACE FUNCTION iml_calculate_hash()
RETURNS TRIGGER AS $$
DECLARE
    last_hash VARCHAR(64);
    hash_input TEXT;
BEGIN
    -- Obtener último hash
    SELECT IMLCURRENTHASH INTO last_hash
    FROM IMLIMMUTABLELOGS
    ORDER BY IDXIMMUTABLELOG DESC
    LIMIT 1;

    IF last_hash IS NULL THEN
        last_hash := 'GENESIS_BLOCK_CODEFLOWX_GOVERN';
    END IF;

    NEW.IMLPREVIOUSHASH := last_hash;

    -- Calcular hash usando función de referencia (mismo algoritmo que Java)
    -- Orden: previousHash || timestampEpoch || entityType || entityId || action || userId || data
    NEW.IMLCURRENTHASH := iml_calculate_hash_reference(
        last_hash,
        NEW.IMLTIMESTAMPEPOCH,  -- BIGINT (no timestamp::TEXT)
        NEW.IMLENTITYTYPE,
        NEW.IMLENTITYID,
        NEW.IMLACTION,
        NEW.IMLUSERID,
        COALESCE(NEW.IMLDATA::TEXT, '')
    );
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
```

### 3. Actualizar Java para Usar Mismo Algoritmo

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/business/logging/ImmutableLoggingBusinessService.java` (MODIFICAR)

Asegurar que el método `calculateHash()` use el mismo orden y formato:

```java
/**
 * Calcula SHA-256 hash del log
 * 
 * Algoritmo: SHA-256(previousHash || timestampEpoch || entityType || entityId || action || userId || data)
 * 
 * IMPORTANTE: Este algoritmo debe coincidir exactamente con la función SQL
 * iml_calculate_hash_reference() para garantizar consistencia.
 * 
 * EU AI Act Art. 19 - Precisión
 */
private String calculateHash(ImmutableLog log) {
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        
        // Concatenar campos en orden determinista (mismo que SQL)
        // Orden: previousHash || timestampEpoch || entityType || entityId || action || userId || data
        String hashInput = 
            (log.getImlprevioushash() != null ? log.getImlprevioushash() : "GENESIS_BLOCK_CODEFLOWX_GOVERN") +
            (log.getImltimestampepoch() != null ? log.getImltimestampepoch().toString() : "") +
            (log.getImlentitytype() != null ? log.getImlentitytype() : "") +
            (log.getImlentityid() != null ? log.getImlentityid().toString() : "") +
            (log.getImlaction() != null ? log.getImlaction() : "") +
            (log.getImluserid() != null ? log.getImluserid().toString() : "") +
            (log.getImldata() != null ? log.getImldata() : "");
        
        byte[] hashBytes = digest.digest(hashInput.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashBytes);
        
    } catch (NoSuchAlgorithmException e) {
        log.error("SHA-256 algorithm not available", e);
        throw new RuntimeException("SHA-256 not available", e);
    }
}
```

### 4. Documentación del Algoritmo

**Archivo:** `suinsit.nova.web/docs/compliance/HASH_ALGORITHM_SPECIFICATION.md` (NUEVO)

```markdown
# ESPECIFICACIÓN DEL ALGORITMO DE HASH PARA LOGS INMUTABLES
## EU AI Act Art. 19 - Precisión

**Versión:** 1.0  
**Fecha:** 2025-11-17  
**Estándar:** SHA-256

---

## ALGORITMO

### Función Hash
**SHA-256** (Secure Hash Algorithm 256 bits)

### Input
Concatenación de campos en orden determinista:

```
hashInput = previousHash || timestampEpoch || entityType || entityId || action || userId || data
```

### Orden de Campos

1. **previousHash** (VARCHAR(64))
   - Hash del log anterior
   - Genesis block: `GENESIS_BLOCK_CODEFLOWX_GOVERN`

2. **timestampEpoch** (BIGINT)
   - Timestamp en formato epoch (milliseconds desde 1970-01-01)
   - Convertido a TEXT para concatenación

3. **entityType** (VARCHAR(50))
   - Tipo de entidad (MODEL, DATASET, PREDICTION, etc.)

4. **entityId** (BIGINT)
   - ID de la entidad
   - Convertido a TEXT para concatenación

5. **action** (VARCHAR(100))
   - Acción realizada (TRAIN, USE, PREDICT, etc.)

6. **userId** (BIGINT)
   - ID del usuario que realizó la acción
   - Convertido a TEXT para concatenación

7. **data** (TEXT)
   - Datos serializados en JSON
   - Puede ser NULL (se usa cadena vacía)

### Output
Hexadecimal de 64 caracteres (256 bits)

---

## IMPLEMENTACIONES

### Java
**Clase:** `ImmutableLoggingBusinessService.calculateHash()`

```java
String hashInput = 
    previousHash +
    timestampEpoch.toString() +
    entityType +
    entityId.toString() +
    action +
    userId.toString() +
    (data != null ? data : "");

byte[] hashBytes = MessageDigest.getInstance("SHA-256")
    .digest(hashInput.getBytes(StandardCharsets.UTF_8));
return bytesToHex(hashBytes);
```

### PostgreSQL
**Función:** `iml_calculate_hash_reference()`

```sql
RETURN encode(
    digest(
        prev_hash ||
        timestamp_epoch::TEXT ||
        entity_type ||
        entity_id::TEXT ||
        action ||
        user_id::TEXT ||
        COALESCE(data, ''),
        'sha256'
    ),
    'hex'
);
```

---

## VERIFICACIÓN DE CONSISTENCIA

### Test de Consistencia Java vs SQL

```sql
-- Crear log de prueba
INSERT INTO IMLIMMUTABLELOGS (
    iduuid, IMLPREVIOUSHASH, IMLTIMESTAMP, IMLTIMESTAMPEPOCH,
    IMLENTITYTYPE, IMLENTITYID, IMLACTION, IMLUSERID, IMLDATA
) VALUES (
    'test-uuid', 'GENESIS_BLOCK_CODEFLOWX_GOVERN', 
    CURRENT_TIMESTAMP, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)::BIGINT * 1000,
    'MODEL', 1, 'TRAIN', 1, '{"test": "data"}'
);

-- Calcular hash con función SQL
SELECT iml_calculate_hash_reference(
    'GENESIS_BLOCK_CODEFLOWX_GOVERN',
    EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)::BIGINT * 1000,
    'MODEL',
    1,
    'TRAIN',
    1,
    '{"test": "data"}'
) AS sql_hash;

-- Comparar con hash calculado por Java (debe ser igual)
```

---

## NOTAS IMPORTANTES

1. **Orden de campos es crítico:** Cualquier cambio en el orden producirá hashes diferentes
2. **Formato de timestamp:** Siempre usar epoch (BIGINT), convertir a TEXT solo para concatenación
3. **Valores NULL:** Se usan cadenas vacías o valores por defecto
4. **Encoding:** UTF-8 para strings, hexadecimal para output

---

## CAMBIOS DE VERSIÓN

### Versión 1.0 (2025-11-17)
- Algoritmo inicial estandarizado
- Consistencia Java/PostgreSQL garantizada

---

**Última Actualización:** 2025-11-17  
**Responsable:** CodeflowX Compliance Team
```

### 5. Test de Consistencia

**Archivo:** `suinsit.nova.web/src/test/java/com/codeflowx/govern/business/logging/HashAlgorithmConsistencyTest.java` (NUEVO)

```java
package com.codeflowx.govern.business.logging;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.enartframework.nocode.datamodel.dao.DAO;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de consistencia entre algoritmo Java y SQL
 */
@SpringBootTest
public class HashAlgorithmConsistencyTest {
    
    @Autowired
    private ImmutableLoggingBusinessService immutableLoggingService;
    
    @Autowired
    private DAO dao;
    
    @Test
    public void testHashConsistencyJavaVsSQL() {
        // Crear log de prueba
        ImmutableLog log = new ImmutableLog();
        log.setImlprevioushash("GENESIS_BLOCK_CODEFLOWX_GOVERN");
        log.setImltimestampepoch(System.currentTimeMillis());
        log.setImlentitytype("MODEL");
        log.setImlentityid(1L);
        log.setImlaction("TRAIN");
        log.setImluserid(1L);
        log.setImldata("{\"test\": \"data\"}");
        
        // Calcular hash con Java
        String javaHash = calculateHashJava(log);
        
        // Calcular hash con SQL
        String sqlHash = calculateHashSQL(log);
        
        // Deben ser iguales
        assertEquals(javaHash, sqlHash, "Hash calculado por Java debe coincidir con SQL");
    }
    
    private String calculateHashJava(ImmutableLog log) {
        // Usar reflexión para acceder al método privado
        // O mejor: hacer el método protected para testing
        return immutableLoggingService.calculateHashForTesting(log);
    }
    
    private String calculateHashSQL(ImmutableLog log) {
        String query = "SELECT iml_calculate_hash_reference(?, ?, ?, ?, ?, ?, ?) AS hash";
        return dao.findBySQL(String.class, query,
            log.getImlprevioushash(),
            log.getImltimestampepoch(),
            log.getImlentitytype(),
            log.getImlentityid(),
            log.getImlaction(),
            log.getImluserid(),
            log.getImldata()
        );
    }
}
```

---

## PRUEBAS REQUERIDAS

### 1. Verificar Función SQL

```sql
-- Probar función de referencia
SELECT iml_calculate_hash_reference(
    'GENESIS_BLOCK_CODEFLOWX_GOVERN',
    1699999999999,
    'MODEL',
    1,
    'TRAIN',
    1,
    '{"test": "data"}'
) AS hash;
```

### 2. Verificar Consistencia

- Crear log desde Java
- Obtener hash calculado por trigger PostgreSQL
- Comparar con hash calculado por Java
- Deben ser idénticos

### 3. Ejecutar Test de Consistencia

```bash
mvn test -Dtest=HashAlgorithmConsistencyTest
```

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_LOGS_INMUTABLES.md#inc-008-005`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_008_LOGS_INMUTABLES_TRAZABILIDAD.md`
- **Artículo EU AI Act:** Art. 19 (precisión)

---

**Estado:** ✅ COMPLETADO  
**Esfuerzo Estimado:** 0.5 días  
**Responsable:** Backend Team + DBA Team

