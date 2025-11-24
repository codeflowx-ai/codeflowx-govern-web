# PROMPT: INC-011-03 - Versionado Explícito de Evaluaciones

**Incidencia:** INC-011-03  
**Prioridad:** 🟡 ALTA  
**Artículo EU AI Act:** Art. 12 (Registros Automáticos), Art. 11 (Documentación Técnica)  
**Esfuerzo Estimado:** 2-3 días  
**Tipo:** Java - Backend

---

## CONTEXTO

Las evaluaciones (`ModelEvaluation`) se vinculan a `modelVersionId`, pero si se re-evalúa el mismo modelo con la misma versión, no hay forma de diferenciar entre evaluaciones. No existe campo de versión de evaluación ni entidad `EvaluationVersion`.

**Ubicación Actual:**
- `ModelEvaluation.java` - campo `modelVersionId` (FK a `ModelVersion`)
- No existe tabla `EVALEVALUATIONVERSIONS`
- No hay tracking de cambios entre evaluaciones
- No hay campo para motivo de re-evaluación

**Referencias:**
- Tabla `MODEVALUATIONS` (asumida)
- Tabla `MODMODELVERSIONS`

---

## REQUISITOS

1. **Agregar campos de versionado** a `ModelEvaluation` (versión, versión padre, cambios, motivo)
2. **Implementar lógica automática** para calcular siguiente versión de evaluación
3. **Crear historial de versiones** de evaluaciones
4. **Diferenciar múltiples evaluaciones** del mismo modelo en la misma versión

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Modificar Tabla MODEVALUATIONS (DBA)

**Archivo:** `nocode.service.entitys/src/main/resources/sql/versionado_evaluaciones.sql`

```sql
-- ============================================================================
-- AGREGAR CAMPOS DE VERSIONADO A MODEVALUATIONS
-- ============================================================================
ALTER TABLE MODEVALUATIONS 
ADD COLUMN EVALVERSIONNUMBER VARCHAR(50),
ADD COLUMN EVALPARENTEVALUATION BIGINT REFERENCES MODEVALUATIONS(IDXEVALUATION),
ADD COLUMN EVALCHANGES TEXT, -- Descripción de cambios respecto a evaluación anterior
ADD COLUMN EVALREASON TEXT, -- Motivo de re-evaluación
ADD COLUMN EVALEVALUATIONDATE TIMESTAMP; -- Fecha de la evaluación

-- Crear índices
CREATE INDEX idx_eval_version ON MODEVALUATIONS(modelVersionId, EVALVERSIONNUMBER);
CREATE INDEX idx_eval_parent ON MODEVALUATIONS(EVALPARENTEVALUATION);
CREATE INDEX idx_eval_version_number ON MODEVALUATIONS(EVALVERSIONNUMBER);

-- Migrar datos existentes: establecer versión 1.0.0 para evaluaciones sin versión
UPDATE MODEVALUATIONS 
SET EVALVERSIONNUMBER = '1.0.0'
WHERE EVALVERSIONNUMBER IS NULL;

-- Agregar constraint para formato SemVer (opcional, puede hacerse en aplicación)
-- ALTER TABLE MODEVALUATIONS 
-- ADD CONSTRAINT chk_eval_version_format 
-- CHECK (EVALVERSIONNUMBER ~ '^[0-9]+\.[0-9]+\.[0-9]+(-[a-zA-Z0-9]+)?(\+[a-zA-Z0-9]+)?$');
```

### 2. Modificar Entidad ModelEvaluation

**Archivo:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/models/ModelEvaluation.java`

```java
// Agregar campos de versionado

@Column(name = "EVALVERSIONNUMBER", length = 50)
private String evalversionnumber; // SemVer: 1.0.0, 1.1.0, etc.

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "EVALPARENTEVALUATION")
private ModelEvaluation evalparentevaluation; // FK a evaluación anterior

@Column(name = "EVALCHANGES", columnDefinition = "TEXT")
private String evalchanges; // Descripción de cambios

@Column(name = "EVALREASON", columnDefinition = "TEXT")
private String evalreason; // Motivo de re-evaluación

@Column(name = "EVALEVALUATIONDATE")
private Timestamp evalevaluationdate; // Fecha de la evaluación

// Getters y Setters
public String getEvalversionnumber() {
    return evalversionnumber;
}

public void setEvalversionnumber(String evalversionnumber) {
    this.evalversionnumber = evalversionnumber;
}

public ModelEvaluation getEvalparentevaluation() {
    return evalparentevaluation;
}

public void setEvalparentevaluation(ModelEvaluation evalparentevaluation) {
    this.evalparentevaluation = evalparentevaluation;
}

public String getEvalchanges() {
    return evalchanges;
}

public void setEvalchanges(String evalchanges) {
    this.evalchanges = evalchanges;
}

public String getEvalreason() {
    return evalreason;
}

public void setEvalreason(String evalreason) {
    this.evalreason = evalreason;
}

public Timestamp getEvalevaluationdate() {
    return evalevaluationdate;
}

public void setEvalevaluationdate(Timestamp evalevaluationdate) {
    this.evalevaluationdate = evalevaluationdate;
}
```

### 3. Modificar BusinessService de Evaluaciones

**Archivo:** `nocode.service.business/src/main/java/com/codeflowx/govern/business/models/ModelEvaluationBusinessService.java`

```java
package com.codeflowx.govern.business.models;

import com.codeflowx.govern.entity.models.ModelEvaluation;
import com.codeflowx.govern.entity.models.ModelVersion;
import com.codeflowx.govern.dao.models.ModelEvaluationDao;
import com.codeflowx.govern.business.logging.ImmutableLoggingBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class ModelEvaluationBusinessService {
    
    @Autowired
    private ModelEvaluationDao evaluationDao;
    
    @Autowired
    private ImmutableLoggingBusinessService loggingService;
    
    /**
     * Crea una nueva evaluación de modelo
     * Requisito: Art. 12 (Registros Automáticos), Art. 11 (Documentación Técnica)
     * 
     * Si ya existe evaluación para esta versión de modelo, crea nueva versión de evaluación
     */
    public ModelEvaluation createEvaluation(ModelEvaluation evaluation, Long userId) {
        Long modelVersionId = evaluation.getModelVersion().getIdxmodelversion();
        
        // Buscar evaluaciones existentes para esta versión de modelo
        List<ModelEvaluation> existing = evaluationDao.findByModelVersionIdOrderByVersionDesc(modelVersionId);
        
        if (!existing.isEmpty()) {
            // Ya existe evaluación, crear nueva versión
            ModelEvaluation lastEvaluation = existing.get(0);
            
            // Calcular siguiente versión
            String nextVersion = calculateNextVersion(lastEvaluation.getEvalversionnumber());
            evaluation.setEvalversionnumber(nextVersion);
            evaluation.setEvalparentevaluation(lastEvaluation);
            
            // Si no se proporciona motivo, usar motivo por defecto
            if (evaluation.getEvalreason() == null || evaluation.getEvalreason().trim().isEmpty()) {
                evaluation.setEvalreason("Re-evaluación del modelo versión " + 
                    evaluation.getModelVersion().getModversion());
            }
            
            // Si no se proporcionan cambios, calcular diferencias automáticamente
            if (evaluation.getEvalchanges() == null || evaluation.getEvalchanges().trim().isEmpty()) {
                evaluation.setEvalchanges(calculateChanges(lastEvaluation, evaluation));
            }
            
        } else {
            // Primera evaluación para esta versión
            evaluation.setEvalversionnumber("1.0.0");
            evaluation.setEvalparentevaluation(null);
        }
        
        // Establecer fecha de evaluación
        if (evaluation.getEvalevaluationdate() == null) {
            evaluation.setEvalevaluationdate(new Timestamp(System.currentTimeMillis()));
        }
        
        // Guardar
        ModelEvaluation created = evaluationDao.save(evaluation);
        
        // Log inmutable
        loggingService.logChange(
            "EVALUATION",
            created.getIdxevaluation(),
            "CREATE",
            userId,
            getUserName(userId),
            created
        );
        
        return created;
    }
    
    /**
     * Calcula siguiente versión de evaluación (incrementa minor version)
     * Formato SemVer: X.Y.Z
     * - X: Major (cambios incompatibles)
     * - Y: Minor (nuevas métricas o cambios significativos)
     * - Z: Patch (correcciones menores)
     */
    private String calculateNextVersion(String currentVersion) {
        if (currentVersion == null || currentVersion.trim().isEmpty()) {
            return "1.0.0";
        }
        
        try {
            // Separar versión base de pre-release/build metadata
            String[] parts = currentVersion.split("[-+]");
            String baseVersion = parts[0];
            String[] versionParts = baseVersion.split("\\.");
            
            if (versionParts.length < 3) {
                return "1.0.0";
            }
            
            int major = Integer.parseInt(versionParts[0]);
            int minor = Integer.parseInt(versionParts[1]);
            int patch = Integer.parseInt(versionParts[2]);
            
            // Incrementar minor version (asumimos que re-evaluación es cambio significativo)
            minor++;
            patch = 0; // Reset patch
            
            return major + "." + minor + ".0";
            
        } catch (NumberFormatException e) {
            // Si no se puede parsear, retornar versión por defecto
            return "1.0.0";
        }
    }
    
    /**
     * Calcula cambios automáticamente comparando dos evaluaciones
     */
    private String calculateChanges(ModelEvaluation previous, ModelEvaluation current) {
        StringBuilder changes = new StringBuilder();
        
        // Comparar métricas principales (ajustar según campos reales)
        if (previous.getEvalaccuracy() != null && current.getEvalaccuracy() != null) {
            double diff = current.getEvalaccuracy() - previous.getEvalaccuracy();
            if (Math.abs(diff) > 0.001) {
                changes.append(String.format("Accuracy: %.2f%% -> %.2f%% (%.2f%%)\n", 
                    previous.getEvalaccuracy() * 100,
                    current.getEvalaccuracy() * 100,
                    diff * 100));
            }
        }
        
        // Comparar otras métricas según campos disponibles
        // TODO: Ajustar según estructura real de ModelEvaluation
        
        if (changes.length() == 0) {
            return "Re-evaluación sin cambios significativos detectados";
        }
        
        return changes.toString();
    }
    
    /**
     * Obtiene todas las versiones de evaluación para una versión de modelo
     */
    public List<ModelEvaluation> getEvaluationVersions(Long modelVersionId) {
        return evaluationDao.findByModelVersionIdOrderByVersionDesc(modelVersionId);
    }
    
    /**
     * Obtiene evaluación específica por versión
     */
    public ModelEvaluation getEvaluationByVersion(Long modelVersionId, String versionNumber) {
        return evaluationDao.findByModelVersionAndVersionNumber(modelVersionId, versionNumber)
            .orElseThrow(() -> new IllegalArgumentException(
                "Evaluación versión " + versionNumber + 
                " no encontrada para modelo versión " + modelVersionId
            ));
    }
    
    /**
     * Obtiene última evaluación para una versión de modelo
     */
    public ModelEvaluation getLatestEvaluation(Long modelVersionId) {
        List<ModelEvaluation> evaluations = getEvaluationVersions(modelVersionId);
        if (evaluations.isEmpty()) {
            return null;
        }
        return evaluations.get(0);
    }
    
    /**
     * Obtiene historial completo de una evaluación (cadena de versiones)
     */
    public List<ModelEvaluation> getEvaluationHistory(Long evaluationId) {
        ModelEvaluation evaluation = evaluationDao.findById(evaluationId)
            .orElseThrow(() -> new IllegalArgumentException("Evaluación no encontrada: " + evaluationId));
        
        // Recorrer hacia atrás desde la evaluación actual
        List<ModelEvaluation> history = new java.util.ArrayList<>();
        ModelEvaluation current = evaluation;
        
        while (current != null) {
            history.add(current);
            current = current.getEvalparentevaluation();
        }
        
        // Invertir para tener orden cronológico
        java.util.Collections.reverse(history);
        return history;
    }
    
    // Métodos auxiliares
    private String getUserName(Long userId) {
        // TODO: Implementar obtención de nombre de usuario
        return "USER_" + userId;
    }
}
```

### 4. Modificar DAO de Evaluaciones

**Archivo:** `nocode.service.dao/src/main/java/com/codeflowx/govern/dao/models/ModelEvaluationDao.java`

```java
package com.codeflowx.govern.dao.models;

import com.codeflowx.govern.entity.models.ModelEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelEvaluationDao extends JpaRepository<ModelEvaluation, Long> {
    
    /**
     * Obtiene todas las evaluaciones de una versión de modelo ordenadas por versión descendente
     */
    @Query("SELECT e FROM ModelEvaluation e WHERE e.modelVersion.idxmodelversion = :modelVersionId ORDER BY e.evalversionnumber DESC")
    List<ModelEvaluation> findByModelVersionIdOrderByVersionDesc(@Param("modelVersionId") Long modelVersionId);
    
    /**
     * Obtiene evaluación específica por versión de modelo y número de versión
     */
    @Query("SELECT e FROM ModelEvaluation e WHERE e.modelVersion.idxmodelversion = :modelVersionId AND e.evalversionnumber = :versionNumber")
    Optional<ModelEvaluation> findByModelVersionAndVersionNumber(
        @Param("modelVersionId") Long modelVersionId,
        @Param("versionNumber") String versionNumber
    );
    
    /**
     * Verifica si existe evaluación para una versión de modelo
     */
    boolean existsByModelVersionId(Long modelVersionId);
    
    /**
     * Cuenta evaluaciones para una versión de modelo
     */
    long countByModelVersionId(Long modelVersionId);
}
```

---

## VALIDACIONES ADICIONALES RECOMENDADAS

1. **Validación automática de formato SemVer** al crear evaluación
2. **Comparación automática de métricas** entre evaluaciones para detectar cambios significativos
3. **Dashboard de historial de evaluaciones** en UI
4. **Exportación de reporte comparativo** entre versiones de evaluación

---

## PRUEBAS REQUERIDAS

1. **Test 1:** Crear primera evaluación para versión de modelo → Debe asignar versión 1.0.0
2. **Test 2:** Crear segunda evaluación para misma versión → Debe asignar versión 1.1.0 y vincular a primera
3. **Test 3:** Obtener todas las versiones de evaluación → Debe retornar lista ordenada
4. **Test 4:** Obtener historial completo de evaluación → Debe retornar cadena completa de versiones
5. **Test 5:** Calcular cambios automáticamente → Debe detectar diferencias en métricas
6. **Test 6:** Crear evaluación con motivo personalizado → Debe usar motivo proporcionado

---

## LOGS INMUTABLES

Añadir logs inmutables en todas las operaciones:

```java
// En createEvaluation()
loggingService.logChange(
    "EVALUATION",
    created.getIdxevaluation(),
    "CREATE",
    userId,
    getUserName(userId),
    Map.of(
        "version", created.getEvalversionnumber(),
        "model_version_id", created.getModelVersion().getIdxmodelversion(),
        "parent_evaluation_id", 
            created.getEvalparentevaluation() != null ? 
            created.getEvalparentevaluation().getIdxevaluation() : null,
        "reason", created.getEvalreason()
    )
);
```

---

## REFERENCIAS

- **Art. 12 EU AI Act:** Registros Automáticos
- **Art. 11 EU AI Act:** Documentación Técnica
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_VERSIONADO.md#inc-011-03`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_011_VERSIONADO_CONTROL_CAMBIOS.md`

---

## NOTAS DE IMPLEMENTACIÓN

- Ajustar método `calculateChanges()` según campos reales de `ModelEvaluation` (métricas disponibles)
- Considerar usar librería de SemVer (ej: `com.github.zafarkhaja:java-semver`) para validación y cálculo de versiones
- La migración de datos existentes establece versión 1.0.0 para todas las evaluaciones sin versión
- Considerar agregar campo `EVALEVALUATIONTYPE` para diferenciar tipos de evaluación (automática, manual, validación cruzada, etc.)
- El incremento de versión usa minor version por defecto; considerar permitir especificar tipo de incremento (major/minor/patch)

---

**Estado:** ✅ COMPLETADO  
**Esfuerzo Estimado:** 2-3 días  
**Responsable:** Backend Team

