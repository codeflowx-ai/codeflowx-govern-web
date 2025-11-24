# PROMPT: INC-005 - Validación Sistemas Prohibidos (Art. 5)

**Incidencia:** INC-005  
**Prioridad:** 🔴 CRÍTICA  
**Artículo EU AI Act:** Art. 5, Anexo II  
**Esfuerzo Estimado:** 1 día  
**Tipo:** Java - Backend

---

## CONTEXTO

El campo `PRJPROHIBITEDUSECHECKED` existe pero no se valida antes de permitir clasificación. El sistema debe verificar contra Art. 5 (sistemas prohibidos) antes de clasificar.

**Ubicación Actual:**
- `Project.java` - campo `PRJPROHIBITEDUSECHECKED` (nullable)
- `HighRiskClassifierViewModel.java` - método `classifyAsHighRisk()`

---

## REQUISITOS

1. Hacer obligatoria verificación Art. 5 antes de clasificar
2. Crear checklist automático de sistemas prohibidos (Anexo II)
3. Bloquear clasificación si sistema está en lista prohibida
4. Generar alerta CRITICAL si sistema prohibido detectado

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear entidad/clase para sistemas prohibidos

**Archivo:** `com.codeflowx.govern.entity.compliance.ProhibitedSystem.java`

```java
package com.codeflowx.govern.entity.compliance;

import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Entidad para sistemas prohibidos según Art. 5 y Anexo II del EU AI Act
 */
@Entity
@Table(name = "GOVPROHIBITEDSYSTEMS")
@Data
public class ProhibitedSystem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXPROHIBITEDSYSTEM")
    private Long idxprohibitedsystem;
    
    @Column(name = "ARTICLECODE", nullable = false, length = 20)
    private String articlecode; // "Art.5.1.a", "Art.5.1.b", etc.
    
    @Column(name = "DESCRIPTION", nullable = false, length = 500)
    private String description;
    
    @Column(name = "KEYWORDS", length = 1000) // JSON array de keywords
    private String keywords;
    
    @Column(name = "ISACTIVE", nullable = false)
    private Boolean isactive = true;
    
    @Column(name = "CREATEDAT", nullable = false)
    private Timestamp createdat;
}
```

### 2. Crear servicio para verificación Art. 5

**Archivo:** `com.codeflowx.govern.business.compliance.ProhibitedSystemService.java`

```java
package com.codeflowx.govern.business.compliance;

import com.codeflowx.govern.entity.compliance.ProhibitedSystem;
import com.codeflowx.govern.entity.projects.Project;
import codeflowx.nocode.persist.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@Service
@Slf4j
public class ProhibitedSystemService {
    
    @Autowired
    private BusinessService businessService;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Verifica si un proyecto coincide con algún sistema prohibido
     */
    public ProhibitedSystemCheckResult checkProhibitedSystem(Project project) {
        ProhibitedSystemCheckResult result = new ProhibitedSystemCheckResult();
        result.setChecked(true);
        
        // Obtener lista de sistemas prohibidos activos
        List<ProhibitedSystem> prohibitedSystems = getActiveProhibitedSystems();
        
        // Verificar contra cada sistema prohibido
        for (ProhibitedSystem prohibited : prohibitedSystems) {
            if (matchesProhibitedSystem(project, prohibited)) {
                result.setIsProhibited(true);
                result.setProhibitedSystem(prohibited);
                result.setMatchReason("Sistema coincide con " + prohibited.getArticlecode());
                log.error("Sistema prohibido detectado: Project ID={}, Article={}", 
                    project.getIdxproject(), prohibited.getArticlecode());
                break;
            }
        }
        
        return result;
    }
    
    /**
     * Obtiene lista de sistemas prohibidos activos (Anexo II)
     */
    private List<ProhibitedSystem> getActiveProhibitedSystems() {
        String query = "SELECT * FROM GOVPROHIBITEDSYSTEMS WHERE ISACTIVE = true ORDER BY ARTICLECODE";
        List<ProhibitedSystem> systems = businessService.findListBySQL(ProhibitedSystem.class, query);
        
        // Si no hay en BD, usar lista hardcodeada inicial
        if (systems.isEmpty()) {
            systems = getDefaultProhibitedSystems();
        }
        
        return systems;
    }
    
    /**
     * Lista por defecto de sistemas prohibidos (Anexo II)
     */
    private List<ProhibitedSystem> getDefaultProhibitedSystems() {
        List<ProhibitedSystem> systems = new ArrayList<>();
        
        // Art. 5.1.a - Manipulación subliminal
        ProhibitedSystem ps1 = new ProhibitedSystem();
        ps1.setArticlecode("Art.5.1.a");
        ps1.setDescription("Sistemas que utilizan técnicas de manipulación subliminal");
        ps1.setKeywords("[\"subliminal\", \"manipulation\", \"subconscious\"]");
        systems.add(ps1);
        
        // Art. 5.1.b - Explotación de vulnerabilidades
        ProhibitedSystem ps2 = new ProhibitedSystem();
        ps2.setArticlecode("Art.5.1.b");
        ps2.setDescription("Sistemas que explotan vulnerabilidades de grupos específicos");
        ps2.setKeywords("[\"exploit\", \"vulnerability\", \"vulnerable groups\"]");
        systems.add(ps2);
        
        // Art. 5.1.c - Evaluación social (scoring)
        ProhibitedSystem ps3 = new ProhibitedSystem();
        ps3.setArticlecode("Art.5.1.c");
        ps3.setDescription("Sistemas de evaluación o scoring social por autoridades públicas");
        ps3.setKeywords("[\"social scoring\", \"social credit\", \"public authority\", \"evaluation\"]");
        systems.add(ps3);
        
        // Art. 5.1.d - Identificación biométrica remota en tiempo real
        ProhibitedSystem ps4 = new ProhibitedSystem();
        ps4.setArticlecode("Art.5.1.d");
        ps4.setDescription("Identificación biométrica remota en tiempo real en espacios de acceso público");
        ps4.setKeywords("[\"biometric\", \"remote\", \"real-time\", \"public space\"]");
        systems.add(ps4);
        
        return systems;
    }
    
    /**
     * Verifica si proyecto coincide con sistema prohibido
     */
    private boolean matchesProhibitedSystem(Project project, ProhibitedSystem prohibited) {
        String projectText = (project.getName() + " " + 
                             (project.getDescription() != null ? project.getDescription() : "") + " " +
                             (project.getMetadata() != null ? project.getMetadata() : "")).toLowerCase();
        
        try {
            // Parsear keywords
            List<String> keywords = objectMapper.readValue(prohibited.getKeywords(), List.class);
            
            // Verificar si alguna keyword aparece en el proyecto
            for (String keyword : keywords) {
                if (projectText.contains(keyword.toLowerCase())) {
                    return true;
                }
            }
            
            // Verificaciones específicas por artículo
            switch (prohibited.getArticlecode()) {
                case "Art.5.1.c":
                    // Scoring social por autoridades públicas
                    if (projectText.contains("scoring") && 
                        (projectText.contains("social") || projectText.contains("public authority"))) {
                        return true;
                    }
                    break;
                    
                case "Art.5.1.d":
                    // Biometría remota en tiempo real
                    if (projectText.contains("biometric") && 
                        projectText.contains("remote") && 
                        projectText.contains("real-time")) {
                        return true;
                    }
                    break;
            }
            
        } catch (Exception e) {
            log.warn("Error verificando sistema prohibido: {}", e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Clase para resultado de verificación
     */
    @Data
    public static class ProhibitedSystemCheckResult {
        private boolean checked = false;
        private boolean isProhibited = false;
        private ProhibitedSystem prohibitedSystem;
        private String matchReason;
    }
}
```

### 3. Modificar `HighRiskClassifierViewModel.java`

Añadir validación en método `classifyAsHighRisk()`:

```java
@Autowired
private ProhibitedSystemService prohibitedSystemService;

@Command
@NotifyChange("*")
public void classifyAsHighRisk() {
    log.info("Clasificando proyecto como ALTO RIESGO: {}", projectId);
    
    // Validaciones básicas existentes
    if (!validateClassification()) {
        return;
    }
    
    // NUEVA VALIDACIÓN: Verificación Art. 5 - Sistemas Prohibidos (INC-005)
    try {
        validateProhibitedSystemCheck();
    } catch (ValidationException e) {
        log.error("Validación Art. 5 fallida: {}", e.getMessage());
        return; // Bloquear clasificación
    }
    
    // ... resto de validaciones (INC-001, INC-003) ...
    
    try {
        classifying = true;
        // ... resto del código existente ...
```

Añadir método de validación:

```java
/**
 * Valida que se haya verificado contra Art. 5 (sistemas prohibidos)
 * Requisito: Art. 5 + Anexo II del EU AI Act
 */
private void validateProhibitedSystemCheck() {
    if (currentProject == null || projectId == null) {
        return;
    }
    
    // Validación 1: Campo PRJPROHIBITEDUSECHECKED debe ser true
    if (currentProject.getPrjprohibitedusechecked() == null || 
        !currentProject.getPrjprohibitedusechecked()) {
        String errorMsg = "CRITICAL: Debe verificar contra Art. 5 (sistemas prohibidos) antes de clasificar.\n\n" +
            "Según el Art. 5 del EU AI Act, ciertos sistemas de IA están prohibidos y no pueden " +
            "ser clasificados ni desplegados.\n\n" +
            "Por favor, complete la verificación contra sistemas prohibidos en la pestaña 'Compliance' " +
            "antes de clasificar este proyecto.";
        
        log.error("Validación fallida - Verificación Art. 5 no completada: Project ID={}", projectId);
        
        Messagebox.show(
            errorMsg,
            "Validación CRÍTICA - Verificación Art. 5 Requerida",
            Messagebox.OK,
            Messagebox.ERROR
        );
        
        throw new ValidationException("Verificación Art. 5 no completada");
    }
    
    // Validación 2: Verificar automáticamente contra lista prohibida
    ProhibitedSystemService.ProhibitedSystemCheckResult checkResult = 
        prohibitedSystemService.checkProhibitedSystem(currentProject);
    
    if (checkResult.isProhibited()) {
        ProhibitedSystem prohibited = checkResult.getProhibitedSystem();
        
        String errorMsg = String.format(
            "CRITICAL: Sistema PROHIBIDO detectado según Art. 5 del EU AI Act.\n\n" +
            "Este sistema coincide con un sistema prohibido:\n\n" +
            "Artículo: %s\n" +
            "Descripción: %s\n" +
            "Razón: %s\n\n" +
            "Los sistemas prohibidos NO pueden ser clasificados ni desplegados.\n\n" +
            "Por favor, revise el proyecto y elimine las características prohibidas " +
            "o contacte con el equipo de compliance.",
            prohibited.getArticlecode(),
            prohibited.getDescription(),
            checkResult.getMatchReason()
        );
        
        log.error("CRITICAL: Sistema prohibido detectado: Project ID={}, Article={}", 
            projectId, prohibited.getArticlecode());
        
        Messagebox.show(
            errorMsg,
            "CRITICAL - Sistema Prohibido Detectado",
            Messagebox.OK,
            Messagebox.ERROR
        );
        
        throw new ValidationException(
            String.format("Sistema prohibido según %s: %s", 
                prohibited.getArticlecode(), 
                prohibited.getDescription())
        );
    }
    
    log.info("Validación Art. 5 exitosa - Sistema no está prohibido: Project ID={}", projectId);
}
```

### 4. Script SQL para tabla de sistemas prohibidos

**Archivo:** `sql-scripts/patches/08_prohibited_systems_table.sql`

```sql
-- Tabla para sistemas prohibidos según Art. 5 y Anexo II
CREATE TABLE IF NOT EXISTS GOVPROHIBITEDSYSTEMS (
    IDXPROHIBITEDSYSTEM BIGSERIAL PRIMARY KEY,
    ARTICLECODE VARCHAR(20) NOT NULL UNIQUE,
    DESCRIPTION VARCHAR(500) NOT NULL,
    KEYWORDS VARCHAR(1000), -- JSON array
    ISACTIVE BOOLEAN NOT NULL DEFAULT true,
    CREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDATEDAT TIMESTAMP
);

-- Índices
CREATE INDEX idx_gov_prohibited_article ON GOVPROHIBITEDSYSTEMS(ARTICLECODE);
CREATE INDEX idx_gov_prohibited_active ON GOVPROHIBITEDSYSTEMS(ISACTIVE);

-- Insertar sistemas prohibidos por defecto (Anexo II)
INSERT INTO GOVPROHIBITEDSYSTEMS (ARTICLECODE, DESCRIPTION, KEYWORDS) VALUES
('Art.5.1.a', 'Sistemas que utilizan técnicas de manipulación subliminal', '["subliminal", "manipulation", "subconscious"]'),
('Art.5.1.b', 'Sistemas que explotan vulnerabilidades de grupos específicos', '["exploit", "vulnerability", "vulnerable groups"]'),
('Art.5.1.c', 'Sistemas de evaluación o scoring social por autoridades públicas', '["social scoring", "social credit", "public authority", "evaluation"]'),
('Art.5.1.d', 'Identificación biométrica remota en tiempo real en espacios de acceso público', '["biometric", "remote", "real-time", "public space"]')
ON CONFLICT (ARTICLECODE) DO NOTHING;
```

---

## PRUEBAS REQUERIDAS

1. **Test 1:** Intentar clasificar sin verificar Art. 5 → Debe bloquear
2. **Test 2:** Intentar clasificar sistema con "social scoring" → Debe detectar prohibido
3. **Test 3:** Intentar clasificar sistema normal con verificación completada → Debe permitir
4. **Test 4:** Verificar que se genera log inmutable cuando se detecta sistema prohibido

---

## REFERENCIAS

- **Art. 5 EU AI Act:** Sistemas Prohibidos
- **Anexo II:** Lista de Sistemas Prohibidos
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-005`

---

**Estado:** ✅ COMPLETADO

