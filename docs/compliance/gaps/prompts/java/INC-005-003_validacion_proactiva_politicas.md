# PROMPT: INC-005-003 - Validación Proactiva de Políticas del Cliente (Backend Java)

**Incidencia:** INC-005-003  
**Prioridad:** 🔴 CRÍTICA (P0)  
**Artículo EU AI Act:** Art. 15 (Transparencia)  
**Esfuerzo Estimado:** 2-3 días  
**Tipo:** Java - Backend

---

## CONTEXTO

La validación de alineación con políticas del cliente se realiza después de generar la respuesta, no de forma proactiva. Además, la validación es binaria (cumple/no cumple) sin gradación de alineación.

**Ubicación Actual:**
- Configuración de políticas: `cfg_client_policies`
- Validación post-generación en servicios RAG

---

## REQUISITOS

1. Implementar validación proactiva:
   - Pre-validación de contexto antes de generación
   - Filtrado de chunks que violan políticas
   - Rechazo temprano de queries problemáticas
2. Sistema de scoring de alineación:
   - Score continuo (0-100) en lugar de binario
   - Múltiples dimensiones (tono, contenido, ética, valores)
   - Umbrales configurables por cliente
3. Aprendizaje adaptativo:
   - Feedback loop de cliente sobre alineación
   - Ajuste automático de validación según feedback
   - Personalización por cliente

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear Entidad para Políticas de Cliente

**Archivo:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/rag/RagClientPolicy.java`

```java
package com.codeflowx.govern.entity.rag;

import com.codeflowx.govern.entity.base.BaseEntity;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Políticas de cliente para validación RAG
 * Tabla: rag_client_policies
 * Prefijo: rcp_
 */
@Entity
@Table(name = "rag_client_policies", schema = "public")
public class RagClientPolicy extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx_rag_client_policy")
    private Long idxRagClientPolicy;

    @Column(name = "rcp_client_id", nullable = false)
    private Long rcpClientId;

    @Column(name = "rcp_policy_name", nullable = false, length = 255)
    private String rcpPolicyName;

    @Column(name = "rcp_policy_type", nullable = false, length = 50)
    private String rcpPolicyType; // TONE, CONTENT, ETHICS, VALUES, LANGUAGE

    @Column(name = "rcp_policy_rules", columnDefinition = "jsonb", nullable = false)
    private String rcpPolicyRules; // Reglas en JSON

    @Column(name = "rcp_min_score", precision = 5, scale = 2)
    private BigDecimal rcpMinScore; // Score mínimo requerido (0-100)

    @Column(name = "rcp_weight", precision = 5, scale = 2)
    private BigDecimal rcpWeight; // Peso en score total

    @Column(name = "rcp_enabled", nullable = false)
    private Boolean rcpEnabled = true;

    // Getters y Setters...
}
```

### 2. Crear Servicio de Validación Proactiva

**Archivo:** `nocode.service/src/main/java/com/codeflowx/govern/service/rag/RagPolicyValidationService.java`

```java
package com.codeflowx.govern.service.rag;

import com.codeflowx.govern.entity.rag.RagClientPolicy;
import com.codeflowx.govern.repository.rag.RagClientPolicyRepository;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Servicio para validación proactiva de políticas RAG
 */
@Named
public class RagPolicyValidationService {

    private static final Logger log = LoggerFactory.getLogger(RagPolicyValidationService.class);

    @Inject
    private RagClientPolicyRepository policyRepository;

    @Inject
    private RagPolicyScoringService scoringService; // Microservicio Python

    /**
     * Valida contexto antes de generación
     */
    public PolicyValidationResult validateContext(
            Long clientId,
            List<String> contextChunks,
            String query) {
        
        try {
            // 1. Obtener políticas activas del cliente
            List<RagClientPolicy> policies = policyRepository
                    .findByRcpClientIdAndRcpEnabledTrue(clientId);

            if (policies.isEmpty()) {
                return PolicyValidationResult.allowed(); // Sin políticas = permitido
            }

            // 2. Validar cada chunk contra políticas
            PolicyValidationResult result = new PolicyValidationResult();
            
            for (String chunk : contextChunks) {
                PolicyValidationResult chunkResult = validateChunk(chunk, policies);
                result.merge(chunkResult);
            }

            // 3. Validar query
            PolicyValidationResult queryResult = validateQuery(query, policies);
            result.merge(queryResult);

            // 4. Calcular score total
            BigDecimal overallScore = scoringService.calculateAlignmentScore(
                    contextChunks, query, policies);

            result.setOverallScore(overallScore);

            // 5. Determinar si está permitido
            BigDecimal minScore = calculateMinScore(policies);
            result.setAllowed(overallScore.compareTo(minScore) >= 0);

            if (!result.isAllowed()) {
                log.warn("Contexto rechazado por políticas: clientId={}, score={}, min={}",
                        clientId, overallScore, minScore);
            }

            return result;

        } catch (Exception e) {
            log.error("Error validando políticas", e);
            // En caso de error, permitir pero registrar
            return PolicyValidationResult.allowed().withWarning("Error en validación: " + e.getMessage());
        }
    }

    /**
     * Valida un chunk individual
     */
    private PolicyValidationResult validateChunk(
            String chunk,
            List<RagClientPolicy> policies) {
        
        PolicyValidationResult result = new PolicyValidationResult();

        for (RagClientPolicy policy : policies) {
            // Llamar a microservicio Python para scoring
            BigDecimal score = scoringService.scoreChunk(chunk, policy);
            
            if (score.compareTo(policy.getRcpMinScore()) < 0) {
                result.addViolation(policy.getRcpPolicyName(), 
                        "Chunk viola política: " + policy.getRcpPolicyName());
            }
        }

        return result;
    }

    /**
     * Valida query
     */
    private PolicyValidationResult validateQuery(
            String query,
            List<RagClientPolicy> policies) {
        
        PolicyValidationResult result = new PolicyValidationResult();

        for (RagClientPolicy policy : policies) {
            BigDecimal score = scoringService.scoreQuery(query, policy);
            
            if (score.compareTo(policy.getRcpMinScore()) < 0) {
                result.addViolation(policy.getRcpPolicyName(),
                        "Query viola política: " + policy.getRcpPolicyName());
                result.setQueryRejected(true);
            }
        }

        return result;
    }

    /**
     * Calcula score mínimo requerido
     */
    private BigDecimal calculateMinScore(List<RagClientPolicy> policies) {
        return policies.stream()
                .map(RagClientPolicy::getRcpMinScore)
                .min(BigDecimal::compareTo)
                .orElse(new BigDecimal("70")); // Default 70
    }
}
```

### 3. Clase de Resultado de Validación

```java
public class PolicyValidationResult {
    private boolean allowed = true;
    private BigDecimal overallScore = BigDecimal.ZERO;
    private List<PolicyViolation> violations = new ArrayList<>();
    private boolean queryRejected = false;
    private String warning;

    public static PolicyValidationResult allowed() {
        return new PolicyValidationResult();
    }

    public PolicyValidationResult withWarning(String warning) {
        this.warning = warning;
        return this;
    }

    public void merge(PolicyValidationResult other) {
        if (!other.isAllowed()) {
            this.allowed = false;
        }
        this.violations.addAll(other.getViolations());
        if (other.isQueryRejected()) {
            this.queryRejected = true;
        }
    }

    // Getters y Setters...
}
```

### 4. Interceptar en Servicio RAG

**Archivo:** Modificar servicio RAG existente

```java
@Inject
private RagPolicyValidationService policyValidationService;

public RagResponse generateResponse(RagRequest request) {
    // 1. VALIDACIÓN PROACTIVA ANTES DE GENERAR
    PolicyValidationResult validation = policyValidationService.validateContext(
            request.getClientId(),
            request.getContextChunks(),
            request.getQuery()
    );

    if (!validation.isAllowed()) {
        throw new PolicyViolationException(
                "Contexto rechazado por políticas del cliente. Score: " + 
                validation.getOverallScore() + 
                ". Violaciones: " + validation.getViolations()
        );
    }

    if (validation.isQueryRejected()) {
        throw new PolicyViolationException(
                "Query rechazada por políticas del cliente"
        );
    }

    // 2. Filtrar chunks que violan políticas
    List<String> filteredChunks = filterChunksByPolicy(
            request.getContextChunks(),
            request.getClientId()
    );

    // 3. Generar respuesta con chunks filtrados
    RagResponse response = llmService.generate(filteredChunks, request.getQuery());

    // 4. Validación post-generación (mantener para retrocompatibilidad)
    PolicyValidationResult postValidation = policyValidationService.validateResponse(
            response.getText(),
            request.getClientId()
    );

    response.setPolicyScore(postValidation.getOverallScore());
    response.setPolicyViolations(postValidation.getViolations());

    return response;
}
```

---

## REFERENCIAS

- **Art. 15 EU AI Act:** Transparencia
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_005_EVALUACION_RAG.md#inc-005-003`

---

**Estado:** ✅ COMPLETADO

