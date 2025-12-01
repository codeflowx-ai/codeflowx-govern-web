# PROMPT: INC-014 - Retención Histórica Evaluaciones

**Incidencia:** INC-014
**Prioridad:** 🟡 MEDIA
**Artículo EU AI Act:** Art. 12, Art. 18
**Esfuerzo Estimado:** 2 días
**Tipo:** Java - Backend + DBA
**Referencia:** GAP-014

---

## CONTEXTO

El sistema debe mantener un historial completo de todas las evaluaciones realizadas, permitiendo consultar el historial completo, calcular estadísticas y tendencias, y gestionar políticas de retención según Art. 12 y Art. 18 del EU AI Act.

**Estado Actual:**
- ✅ Existe `EvaluationHistoryService` que consulta tablas de evaluaciones existentes
- ❌ No hay tabla dedicada para historial consolidado
- ❌ No hay gestión de políticas de retención configurables
- ❌ No hay archivado de evaluaciones antiguas

---

## REQUISITOS

1. Tabla dedicada para historial consolidado de evaluaciones
2. Soporte para múltiples tipos de evaluaciones (Modelo, Dataset, RAG, Compliance)
3. Políticas de retención configurables
4. Archivado automático de evaluaciones antiguas
5. Consultas optimizadas para histórico

---

## IMPLEMENTACIÓN REQUERIDA

### PASO 1 - CREAR JSON EnArt:

**Ubicación:** `sources/json/tables/EVHEVALUATIONHISTORIES.json`

```json
{
  "namespace": "evaluation",
  "name": "EVHEVALUATIONHISTORIES",
  "type": "TABLE",
  "labelMonitor": "evhevaluationtype",
  "description": "Historial completo de evaluaciones según EU AI Act Art. 12, Art. 18",
  "fields": [
    {
      "name": "idxevaluationhistory",
      "type": "LONG",
      "pk": true,
      "required": false,
      "label": "ID History",
      "criteria": true,
      "filter": true
    },
    {
      "name": "iduuid",
      "type": "VARCHAR",
      "size": 36,
      "required": true,
      "label": "UUID",
      "criteria": true,
      "filter": true
    },
    {
      "name": "evhevaluationtype",
      "type": "VARCHAR",
      "size": 50,
      "required": true,
      "label": "Tipo Evaluación",
      "criteria": true,
      "filter": true,
      "values": ["MODEL", "DATASET", "RAG", "COMPLIANCE", "FRIA", "QMS"]
    },
    {
      "name": "evhentitytype",
      "type": "VARCHAR",
      "size": 50,
      "required": true,
      "label": "Tipo Entidad",
      "criteria": true,
      "filter": true,
      "values": ["PROJECT", "MODEL", "DATASET", "RAGSYSTEM", "COMPLIANCE_ASSESSMENT"]
    },
    {
      "name": "evhentityid",
      "type": "LONG",
      "required": true,
      "label": "ID Entidad",
      "criteria": true,
      "filter": true
    },
    {
      "name": "evhevaluationid",
      "type": "LONG",
      "required": false,
      "label": "ID Evaluación Original",
      "criteria": true,
      "filter": true,
      "comment": "FK a la evaluación original (ModelEvaluation, RagEvaluation, etc.)"
    },
    {
      "name": "evhevaluationdate",
      "type": "TIMESTAMP",
      "required": true,
      "label": "Fecha Evaluación",
      "criteria": true,
      "filter": true
    },
    {
      "name": "evhmetrics",
      "type": "JSONB",
      "required": true,
      "label": "Métricas",
      "criteria": false,
      "filter": false,
      "comment": "JSONB con todas las métricas de la evaluación"
    },
    {
      "name": "evhresult",
      "type": "VARCHAR",
      "size": 20,
      "required": true,
      "label": "Resultado",
      "criteria": true,
      "filter": true,
      "values": ["PASS", "FAIL", "WARNING", "REVIEW_REQUIRED"]
    },
    {
      "name": "evhoverallscore",
      "type": "DECIMAL",
      "required": false,
      "label": "Score Overall",
      "criteria": true,
      "filter": true,
      "comment": "Score numérico si aplica (0.00-1.00)"
    },
    {
      "name": "evhjustification",
      "type": "CLOB",
      "required": false,
      "label": "Justificación",
      "criteria": false,
      "filter": false
    },
    {
      "name": "evhevaluatedby",
      "type": "VARCHAR",
      "size": 255,
      "required": false,
      "label": "Evaluado Por",
      "criteria": true,
      "filter": true
    },
    {
      "name": "evharchived",
      "type": "BOOLEAN",
      "required": true,
      "label": "Archivado",
      "criteria": true,
      "filter": true,
      "default": false
    },
    {
      "name": "evharchiveddate",
      "type": "TIMESTAMP",
      "required": false,
      "label": "Fecha Archivado",
      "criteria": true,
      "filter": true
    },
    {
      "name": "evhretentionpolicy",
      "type": "VARCHAR",
      "size": 50,
      "required": false,
      "label": "Política Retención",
      "criteria": true,
      "filter": true,
      "values": ["STANDARD", "EXTENDED", "PERMANENT"],
      "default": "STANDARD"
    },
    {
      "name": "evhmetadata",
      "type": "JSONB",
      "required": false,
      "label": "Metadata",
      "criteria": false,
      "filter": false,
      "comment": "Metadata adicional (versión, configuración, etc.)"
    },
    {
      "name": "evhcreatedat",
      "type": "TIMESTAMP",
      "required": true,
      "label": "Fecha Creación",
      "criteria": true,
      "filter": true
    },
    {
      "name": "evhupdatedat",
      "type": "TIMESTAMP",
      "required": false,
      "label": "Fecha Actualización",
      "criteria": true,
      "filter": true
    }
  ]
}
```

### PASO 2 - GENERAR Entity.java (Automático con generador Python):

**Ejecutar generador:**
```bash
python src/generators/java_entity_generator.py sources/json/tables/EVHEVALUATIONHISTORIES.json
```

**Genera automáticamente:**
`sources/jpa/evaluation/EvaluationHistory.java`

### PASO 3 - CREAR BusinessService:

**Archivo:** `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/evaluation/EvaluationHistoryBusinessService.java`

```java
package com.codeflowx.govern.business.evaluation;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.enartframework.nocode.datamodel.dao.DAO;
import lombok.extern.slf4j.Slf4j;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;

/**
 * BusinessService para gestión de historial de evaluaciones
 * EU AI Act Art. 12, Art. 18 - Retención histórica
 */
@Service
@Slf4j
public class EvaluationHistoryBusinessService {

    @Autowired
    private DAO dao;

    /**
     * Almacena evaluación en historial
     */
    public EvaluationHistory storeEvaluationHistory(
            String evaluationType,
            String entityType,
            Long entityId,
            Long evaluationId,
            Map<String, Object> metrics,
            String result,
            Double overallScore,
            String justification,
            String evaluatedBy
    ) {
        EvaluationHistory history = new EvaluationHistory();
        history.setIduuid(UUID.randomUUID().toString());
        history.setEvhevaluationtype(evaluationType);
        history.setEvhentitytype(entityType);
        history.setEvhentityid(entityId);
        history.setEvhevaluationid(evaluationId);
        history.setEvhevaluationdate(new Timestamp(System.currentTimeMillis()));
        history.setEvhmetrics(serializeToJson(metrics));
        history.setEvhresult(result);
        history.setEvhoverallscore(overallScore);
        history.setEvhjustification(justification);
        history.setEvhevaluatedby(evaluatedBy);
        history.setEvharchived(false);
        history.setEvhretentionpolicy("STANDARD");
        history.setEvhcreatedat(new Timestamp(System.currentTimeMillis()));

        dao.insert(history);
        log.info("Stored evaluation history: {} - {} - {}", evaluationType, entityType, entityId);
        return history;
    }

    /**
     * Obtiene historial de evaluaciones por entidad
     */
    public List<EvaluationHistory> getHistoryByEntity(String entityType, Long entityId, Integer limit) {
        String query = "SELECT * FROM EVHEVALUATIONHISTORIES " +
                      "WHERE EVHENTITYTYPE = ? AND EVHENTITYID = ? " +
                      "AND EVHARCHIVED = false " +
                      "ORDER BY EVHEVALUATIONDATE DESC";
        if (limit != null && limit > 0) {
            query += " LIMIT " + limit;
        }
        return dao.findBySQL(EvaluationHistory.class, query, entityType, entityId);
    }

    /**
     * Archiva evaluaciones antiguas según política de retención
     */
    public int archiveOldEvaluations(int retentionDays) {
        String query = "UPDATE EVHEVALUATIONHISTORIES " +
                      "SET EVHARCHIVED = true, EVHARCHIVEDDATE = CURRENT_TIMESTAMP " +
                      "WHERE EVHEVALUATIONDATE < CURRENT_TIMESTAMP - INTERVAL '" + retentionDays + " days' " +
                      "AND EVHARCHIVED = false " +
                      "AND EVHRETENTIONPOLICY = 'STANDARD'";

        int count = dao.executeUpdate(query);
        log.info("Archived {} old evaluations (retention: {} days)", count, retentionDays);
        return count;
    }

    /**
     * Obtiene estadísticas de historial
     */
    public Map<String, Object> getHistoryStatistics(String entityType, Long entityId) {
        String query = "SELECT " +
                      "COUNT(*) as total, " +
                      "AVG(EVHOVERALLSCORE) as avg_score, " +
                      "MAX(EVHEVALUATIONDATE) as last_date, " +
                      "MAX(EVHOVERALLSCORE) as max_score " +
                      "FROM EVHEVALUATIONHISTORIES " +
                      "WHERE EVHENTITYTYPE = ? AND EVHENTITYID = ? " +
                      "AND EVHARCHIVED = false";

        // Ejecutar query y construir resultado
        Map<String, Object> stats = new HashMap<>();
        // ... implementación con dao.findBySQL para estadísticas
        return stats;
    }

    private String serializeToJson(Map<String, Object> data) {
        // Implementar serialización JSON
        // Usar ObjectMapper o similar
        return ""; // Placeholder
    }
}
```

### PASO 4 - CREAR Script SQL:

**Archivo:** `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/configuration/evaluation_history.sql`

```sql
-- =====================================================
-- EVALUATION HISTORY (EVH) - EU AI Act Art. 12, Art. 18
-- =====================================================
-- Tabla: EVHEVALUATIONHISTORIES
-- Prefijo: EVH
-- Entidad: com.codeflowx.govern.entity.evaluation.EvaluationHistory
--
-- Almacena historial completo de todas las evaluaciones realizadas
-- para trazabilidad y auditoría según EU AI Act Art. 12 y Art. 18
-- =====================================================

CREATE TABLE IF NOT EXISTS EVHEVALUATIONHISTORIES (
    IDXEVALUATIONHISTORY BIGSERIAL PRIMARY KEY,
    iduuid VARCHAR(36) UNIQUE NOT NULL,

    -- Tipo de evaluación
    EVHEVALUATIONTYPE VARCHAR(50) NOT NULL CHECK (EVHEVALUATIONTYPE IN ('MODEL', 'DATASET', 'RAG', 'COMPLIANCE', 'FRIA', 'QMS')),
    EVHENTITYTYPE VARCHAR(50) NOT NULL CHECK (EVHENTITYTYPE IN ('PROJECT', 'MODEL', 'DATASET', 'RAGSYSTEM', 'COMPLIANCE_ASSESSMENT')),
    EVHENTITYID BIGINT NOT NULL,
    EVHEVALUATIONID BIGINT,

    -- Datos de la evaluación
    EVHEVALUATIONDATE TIMESTAMP NOT NULL,
    EVHMETRICS JSONB NOT NULL,
    EVHRESULT VARCHAR(20) NOT NULL CHECK (EVHRESULT IN ('PASS', 'FAIL', 'WARNING', 'REVIEW_REQUIRED')),
    EVHOVERALLSCORE NUMERIC(5,2),
    EVHJUSTIFICATION TEXT,
    EVHEVALUATEDBY VARCHAR(255),

    -- Retención y archivado
    EVHARCHIVED BOOLEAN NOT NULL DEFAULT FALSE,
    EVHARCHIVEDDATE TIMESTAMP,
    EVHRETENTIONPOLICY VARCHAR(50) DEFAULT 'STANDARD' CHECK (EVHRETENTIONPOLICY IN ('STANDARD', 'EXTENDED', 'PERMANENT')),

    -- Metadata
    EVHMETADATA JSONB,

    -- Fechas de Auditoría
    EVHCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    EVHUPDATEDAT TIMESTAMP
);

-- Índices
CREATE INDEX IDX_EVH_ENTITY ON EVHEVALUATIONHISTORIES(EVHENTITYTYPE, EVHENTITYID);
CREATE INDEX IDX_EVH_EVALUATION_TYPE ON EVHEVALUATIONHISTORIES(EVHEVALUATIONTYPE);
CREATE INDEX IDX_EVH_DATE ON EVHEVALUATIONHISTORIES(EVHEVALUATIONDATE DESC);
CREATE INDEX IDX_EVH_ARCHIVED ON EVHEVALUATIONHISTORIES(EVHARCHIVED) WHERE EVHARCHIVED = FALSE;
CREATE INDEX IDX_EVH_RESULT ON EVHEVALUATIONHISTORIES(EVHRESULT);

-- Índice GIN para búsqueda en JSONB
CREATE INDEX IDX_EVH_METRICS_GIN ON EVHEVALUATIONHISTORIES USING GIN (EVHMETRICS);

-- Comentarios
COMMENT ON TABLE EVHEVALUATIONHISTORIES IS 'Historial completo de evaluaciones según EU AI Act Art. 12, Art. 18';
COMMENT ON COLUMN EVHEVALUATIONHISTORIES.EVHEVALUATIONTYPE IS 'Tipo: MODEL, DATASET, RAG, COMPLIANCE, FRIA, QMS';
COMMENT ON COLUMN EVHEVALUATIONHISTORIES.EVHRETENTIONPOLICY IS 'STANDARD (365 días), EXTENDED (1095 días), PERMANENT';
COMMENT ON COLUMN EVHEVALUATIONHISTORIES.EVHMETRICS IS 'JSONB con todas las métricas de la evaluación';
```

---

## INTEGRACIÓN CON SERVICIOS EXISTENTES

### Modificar EvaluationHistoryService

**Archivo:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/evaluation/EvaluationHistoryService.java`

Agregar método para almacenar en historial:

```java
@Autowired
private EvaluationHistoryBusinessService evaluationHistoryBusinessService;

public void storeInHistory(ModelEvaluation evaluation) {
    Map<String, Object> metrics = new HashMap<>();
    metrics.put("accuracy", evaluation.getAccuracy());
    metrics.put("precision", evaluation.getPrecisionScore());
    metrics.put("recall", evaluation.getRecallScore());
    // ... más métricas

    evaluationHistoryBusinessService.storeEvaluationHistory(
        "MODEL",
        "MODEL",
        evaluation.getIdxmodel(),
        evaluation.getIdxmodelevaluation(),
        metrics,
        evaluation.getEvlresult(),
        evaluation.getEvloverallscore(),
        evaluation.getEvljustification(),
        evaluation.getEvlevaluatedby()
    );
}
```

---

## VALIDACIONES

1. ✅ JSON EnArt creado según convenciones
2. ✅ Entity.java generado automáticamente
3. ✅ BusinessService creado con métodos CRUD
4. ✅ Script SQL con índices apropiados
5. ✅ Integración con servicios de evaluación existentes
6. ✅ Políticas de retención configurables

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-014`
- **Arquitectura EnArt:** `/docs/compliance/PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md`
- **Servicio Existente:** `EvaluationHistoryService.java`

---

**Estado:** 🔴 PENDIENTE
**Última Actualización:** 25 de noviembre de 2025
