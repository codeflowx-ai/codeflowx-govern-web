# PROMPT: INC-HITL-005 - Firma Digital en Aprobaciones

**Incidencia:** INC-HITL-005  
**Prioridad:** 🟠 HIGH  
**Artículo EU AI Act:** Art. 14.4 (Transparency), ISO 42001 (Integrity)  
**Esfuerzo Estimado:** 3-5 días  
**Tipo:** Java - Backend  
**Estado:** ✅ COMPLETADO

---

## DESCRIPCIÓN

Las aprobaciones humanas no incluyen firma digital o certificado que garantice la integridad y no repudio de las decisiones. Se requiere agregar campo de hash/certificado y calcular hash SHA-256 del registro completo al aprobar.

---

## REQUISITOS

1. Agregar campo `AGTHASHSIGNATURE` en entidades de aprobación
2. Calcular hash SHA-256 de registro completo al aprobar
3. Integrar con servicio de firma electrónica (opcional, para alta seguridad)
4. Implementar validación de integridad en consultas de auditoría

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Agregar Campo de Hash en Entidades

**Archivo:** Modificar `AgentApproval.java`, `ModelApproval.java`, `PromptApproval.java`

```java
@Column(name = "AGTHASHSIGNATURE", length = 64)
@Field(criteria = true, auditar = false, filter = true, label = "Hash Signature", type = "STRING")
private String agthashsignature; // SHA-256 hash en hexadecimal
```

### 2. Servicio de Cálculo de Hash

**Archivo:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/hitl/HashSignatureService.java`

```java
package com.codeflowx.govern.service.hitl;

import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Timestamp;

@Service
public class HashSignatureService {
    
    /**
     * Calcular hash SHA-256 del registro completo de aprobación
     */
    public String calculateHash(AgentApproval approval) {
        StringBuilder data = new StringBuilder();
        data.append(approval.getIdxagentapproval());
        data.append(approval.getAgtapprovaltype());
        data.append(approval.getAgtapprovalstatus());
        data.append(approval.getAgtcreatedat());
        data.append(approval.getAgtapprovedat());
        data.append(approval.getAgtcreatedby());
        data.append(approval.getAgtapprovername());
        data.append(approval.getAgtapproverrole());
        data.append(approval.getAgtrequestreason());
        data.append(approval.getAgtrequestdetails());
        data.append(approval.getAgtriskassessment());
        data.append(approval.getAgtcompliancecheck());
        data.append(approval.getAgttechnicalreview());
        data.append(approval.getAgtethicalreview());
        data.append(approval.getAgtapprovalnotes());
        
        return calculateSHA256(data.toString());
    }
    
    private String calculateSHA256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error calculating hash", e);
        }
    }
    
    /**
     * Validar integridad de registro
     */
    public boolean validateIntegrity(AgentApproval approval) {
        String calculatedHash = calculateHash(approval);
        return calculatedHash.equals(approval.getAgthashsignature());
    }
}
```

### 3. Listener JPA para Cálculo Automático

**Archivo:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/listener/HashSignatureListener.java`

```java
package com.codeflowx.govern.listener;

import com.codeflowx.govern.entity.agents.AgentApproval;
import com.codeflowx.govern.service.hitl.HashSignatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Component
public class HashSignatureListener {
    
    private static HashSignatureService hashService;
    
    @Autowired
    public void setHashSignatureService(HashSignatureService service) {
        HashSignatureListener.hashService = service;
    }
    
    @PrePersist
    @PreUpdate
    public void calculateHashBeforeSave(Object entity) {
        if (entity instanceof AgentApproval) {
            AgentApproval approval = (AgentApproval) entity;
            // Solo calcular hash si está aprobado
            if ("APPROVED".equals(approval.getAgtapprovalstatus()) && 
                approval.getAgthashsignature() == null) {
                String hash = hashService.calculateHash(approval);
                approval.setAgthashsignature(hash);
            }
        }
    }
}
```

---

## TESTING

```java
@Test
public void testCalculateHash_DeberiaGenerarHashConsistente() {
    AgentApproval approval = new AgentApproval();
    // ... setear campos ...
    approval.setAgtapprovalstatus("APPROVED");
    
    String hash1 = hashService.calculateHash(approval);
    String hash2 = hashService.calculateHash(approval);
    
    assertEquals(hash1, hash2);
    assertEquals(64, hash1.length()); // SHA-256 en hex = 64 caracteres
}
```

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md#inc-hitl-005`
- **EU AI Act Art. 14.4:** Transparency
- **ISO 42001:** Integrity

---

**Estado:** ✅ COMPLETADO  
**Esfuerzo Estimado:** 3-5 días  
**Responsable:** Backend Team + Security Team  
**Fecha Límite:** 1 mes

