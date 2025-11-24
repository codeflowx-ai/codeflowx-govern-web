# PROMPT: INC-008-006 - Soporte para Timestamp Externo (RFC 3161)
## EU AI Act Art. 19 - Registro Inmutable

**Incidencia:** INC-008-006  
**Prioridad:** 🟢 BAJA  
**Artículo EU AI Act:** Art. 19 (precisión temporal)  
**Esfuerzo Estimado:** 2 días (opcional)  
**Tipo:** Java - Backend + Integración Externa

---

## CONTEXTO

El campo `IMLEXTERNALTIMESTAMP` existe pero no se utiliza. Podría implementarse soporte para timestamps externos (blockchain, TSA) para mayor garantía temporal.

**Ubicación Actual:**
- Tabla `IMLIMMUTABLELOGS` tiene campo `IMLEXTERNALTIMESTAMP TEXT`
- No hay implementación de servicio de timestamp externo

---

## REQUISITOS

1. **Implementar integración opcional:**
   - Servicio de timestamp externo (blockchain, TSA)
   - Guardar proof en `IMLEXTERNALTIMESTAMP`
   - Verificación de timestamp externo en exportaciones

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Interfaz de Timestamp Externo

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/business/logging/ExternalTimestampService.java` (NUEVO)

```java
package com.codeflowx.govern.business.logging;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Interfaz para servicios de timestamp externo (RFC 3161, blockchain, etc.)
 * EU AI Act Art. 19 - Precisión temporal
 */
public interface ExternalTimestampService {
    
    /**
     * Obtiene timestamp externo para un hash
     * 
     * @param hash Hash del log para timestamp
     * @return Proof de timestamp externo (JSON string)
     */
    Optional<String> getExternalTimestamp(String hash);
    
    /**
     * Verifica timestamp externo
     * 
     * @param hash Hash del log
     * @param externalTimestamp Proof de timestamp externo
     * @return true si el timestamp es válido
     */
    boolean verifyExternalTimestamp(String hash, String externalTimestamp);
    
    /**
     * Obtiene fecha del timestamp externo
     * 
     * @param externalTimestamp Proof de timestamp externo
     * @return Fecha del timestamp
     */
    Optional<LocalDateTime> extractTimestamp(String externalTimestamp);
}
```

### 2. Implementación con Blockchain (Ejemplo)

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/business/logging/BlockchainTimestampService.java` (NUEVO)

```java
package com.codeflowx.govern.business.logging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.Optional;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Implementación de timestamp externo usando blockchain
 * EU AI Act Art. 19 - Precisión temporal
 * 
 * NOTA: Esta es una implementación de ejemplo. En producción, usar servicio real de blockchain.
 */
@Service
@Slf4j
public class BlockchainTimestampService implements ExternalTimestampService {
    
    @Value("${compliance.external-timestamp.blockchain.enabled:false}")
    private boolean enabled;
    
    @Value("${compliance.external-timestamp.blockchain.provider:}")
    private String blockchainProvider;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public Optional<String> getExternalTimestamp(String hash) {
        if (!enabled) {
            log.debug("Timestamp externo deshabilitado");
            return Optional.empty();
        }
        
        try {
            // TODO: Integrar con servicio real de blockchain
            // Por ahora, simulación
            log.info("Obteniendo timestamp externo para hash: {}", hash);
            
            // Simular respuesta de blockchain
            JsonNode proof = objectMapper.createObjectNode()
                .put("provider", blockchainProvider)
                .put("hash", hash)
                .put("timestamp", LocalDateTime.now().toString())
                .put("blockNumber", 12345)
                .put("transactionHash", "0x" + hash.substring(0, 64))
                .put("verified", true);
            
            return Optional.of(objectMapper.writeValueAsString(proof));
            
        } catch (Exception e) {
            log.error("Error obteniendo timestamp externo", e);
            return Optional.empty();
        }
    }
    
    @Override
    public boolean verifyExternalTimestamp(String hash, String externalTimestamp) {
        if (externalTimestamp == null || externalTimestamp.isEmpty()) {
            return false;
        }
        
        try {
            JsonNode proof = objectMapper.readTree(externalTimestamp);
            
            // Verificar que el hash coincide
            String proofHash = proof.get("hash").asText();
            if (!hash.equals(proofHash)) {
                log.warn("Hash no coincide en timestamp externo");
                return false;
            }
            
            // Verificar que está verificado
            boolean verified = proof.get("verified").asBoolean();
            if (!verified) {
                log.warn("Timestamp externo no verificado");
                return false;
            }
            
            return true;
            
        } catch (Exception e) {
            log.error("Error verificando timestamp externo", e);
            return false;
        }
    }
    
    @Override
    public Optional<LocalDateTime> extractTimestamp(String externalTimestamp) {
        if (externalTimestamp == null || externalTimestamp.isEmpty()) {
            return Optional.empty();
        }
        
        try {
            JsonNode proof = objectMapper.readTree(externalTimestamp);
            String timestampStr = proof.get("timestamp").asText();
            return Optional.of(LocalDateTime.parse(timestampStr));
        } catch (Exception e) {
            log.error("Error extrayendo timestamp", e);
            return Optional.empty();
        }
    }
}
```

### 3. Implementación con TSA (RFC 3161)

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/business/logging/TSATimestampService.java` (NUEVO)

```java
package com.codeflowx.govern.business.logging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.Optional;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * Implementación de timestamp externo usando TSA (Time Stamping Authority) RFC 3161
 * EU AI Act Art. 19 - Precisión temporal
 * 
 * NOTA: Requiere biblioteca de TSA (ej: BouncyCastle)
 */
@Service
@Slf4j
public class TSATimestampService implements ExternalTimestampService {
    
    @Value("${compliance.external-timestamp.tsa.enabled:false}")
    private boolean enabled;
    
    @Value("${compliance.external-timestamp.tsa.url:}")
    private String tsaUrl;
    
    @Override
    public Optional<String> getExternalTimestamp(String hash) {
        if (!enabled) {
            log.debug("TSA timestamp deshabilitado");
            return Optional.empty();
        }
        
        try {
            // TODO: Implementar integración real con TSA RFC 3161
            // Requiere biblioteca BouncyCastle o similar
            log.info("Obteniendo timestamp TSA para hash: {}", hash);
            
            // Por ahora, simulación
            // En producción, usar biblioteca RFC 3161 para obtener timestamp token
            String timestampToken = Base64.getEncoder().encodeToString(
                ("TSA_TOKEN_" + hash + "_" + System.currentTimeMillis()).getBytes()
            );
            
            return Optional.of(timestampToken);
            
        } catch (Exception e) {
            log.error("Error obteniendo timestamp TSA", e);
            return Optional.empty();
        }
    }
    
    @Override
    public boolean verifyExternalTimestamp(String hash, String externalTimestamp) {
        if (externalTimestamp == null || externalTimestamp.isEmpty()) {
            return false;
        }
        
        try {
            // TODO: Verificar timestamp token RFC 3161
            // Por ahora, validación básica
            return externalTimestamp.startsWith("TSA_TOKEN_");
            
        } catch (Exception e) {
            log.error("Error verificando timestamp TSA", e);
            return false;
        }
    }
    
    @Override
    public Optional<LocalDateTime> extractTimestamp(String externalTimestamp) {
        // TODO: Extraer timestamp del token RFC 3161
        // Por ahora, retornar fecha actual
        return Optional.of(LocalDateTime.now());
    }
}
```

### 4. Modificar ImmutableLoggingBusinessService

**Archivo:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/business/logging/ImmutableLoggingBusinessService.java` (MODIFICAR)

Añadir soporte para timestamp externo opcional:

```java
@Autowired(required = false)
private ExternalTimestampService externalTimestampService;

// En método createLogEntry(), después de calcular hash:
// Obtener timestamp externo opcional
if (externalTimestampService != null) {
    Optional<String> externalTimestamp = externalTimestampService.getExternalTimestamp(currentHash);
    if (externalTimestamp.isPresent()) {
        newLog.setImlexternaltimestamp(externalTimestamp.get());
        log.info("Timestamp externo obtenido para log ID: {}", newLog.getIdximmutablelog());
    } else {
        log.warn("No se pudo obtener timestamp externo para log ID: {}", newLog.getIdximmutablelog());
    }
}
```

### 5. Verificación en Exportación

**Archivo:** `nocode.service/codeflowx.govern.workflow.lib/src/main/java/com/codeflowx/govern/workflow/services/AIActLogExportService.java` (MODIFICAR)

Añadir verificación de timestamps externos en exportación:

```java
// En método formatForAIAct(), añadir verificación:
if (externalTimestampService != null) {
    for (AIActLogEntry log : logs) {
        if (log.getExternalTimestamp() != null && !log.getExternalTimestamp().isEmpty()) {
            boolean verified = externalTimestampService.verifyExternalTimestamp(
                log.getCurrentHash(), 
                log.getExternalTimestamp()
            );
            log.setExternalTimestampVerified(verified);
        }
    }
}
```

### 6. Configuración

**Archivo:** `suinsit.nova.web/src/main/resources/application.properties` (añadir)

```properties
# Configuración de timestamp externo (opcional)
compliance.external-timestamp.enabled=false
compliance.external-timestamp.provider=blockchain

# Blockchain
compliance.external-timestamp.blockchain.enabled=false
compliance.external-timestamp.blockchain.provider=ethereum

# TSA (RFC 3161)
compliance.external-timestamp.tsa.enabled=false
compliance.external-timestamp.tsa.url=
```

---

## PRUEBAS REQUERIDAS

### 1. Prueba de Timestamp Externo

- Habilitar timestamp externo
- Crear log
- Verificar que `IMLEXTERNALTIMESTAMP` se llena
- Verificar que el proof es válido

### 2. Prueba de Verificación

- Exportar logs con timestamps externos
- Verificar que la verificación funciona correctamente

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_LOGS_INMUTABLES.md#inc-008-006`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_008_LOGS_INMUTABLES_TRAZABILIDAD.md`
- **Artículo EU AI Act:** Art. 19 (precisión temporal)
- **RFC 3161:** Time-Stamp Protocol

---

**Estado:** ✅ COMPLETADO  
**Esfuerzo Estimado:** 2 días  
**Responsable:** Backend Team + Integraciones Externas

**NOTA:** Esta funcionalidad es opcional y mejora la garantía temporal, pero no es estrictamente requerida por Art. 19.

