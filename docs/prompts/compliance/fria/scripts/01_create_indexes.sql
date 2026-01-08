-- ============================================================================
-- SCRIPT DE CREACIÓN DE ÍNDICES PARA FRIA
-- ============================================================================
-- Módulo: FRIA (Fundamental Rights Impact Assessment)
-- Tabla: FRIAFUNDAMENTALRIGHTSASSESSMENTS
-- Fecha: Diciembre 2025
-- Descripción: Índices para optimizar consultas frecuentes en FRIA
-- ============================================================================

-- NOTA: Estos índices se crean automáticamente mediante JPA cuando se usa
-- hibernate.hbm2ddl.auto=update o validate. Este script es para referencia
-- o para aplicar manualmente en producción.

-- ============================================================================
-- ÍNDICES SIMPLES
-- ============================================================================

-- Índice en proyecto (FK) - Consultas por proyecto
CREATE INDEX IF NOT EXISTS IDX_FRIA_PROJECT
ON FRIAFUNDAMENTALRIGHTSASSESSMENTS(IDXPROJECT);

-- Índice en usuario (FK) - Consultas por usuario desplegador
CREATE INDEX IF NOT EXISTS IDX_FRIA_USER
ON FRIAFUNDAMENTALRIGHTSASSESSMENTS(IDXUSER);

-- Índice en fecha de creación - Ordenamiento por fecha
CREATE INDEX IF NOT EXISTS IDX_FRIA_CREATED_AT
ON FRIAFUNDAMENTALRIGHTSASSESSMENTS(FRIACREATEDAT DESC);

-- Índice en fecha de actualización - Ordenamiento por última modificación
CREATE INDEX IF NOT EXISTS IDX_FRIA_UPDATED_AT
ON FRIAFUNDAMENTALRIGHTSASSESSMENTS(FRIAUPDATEDAT DESC);

-- Índice en notificación - Filtros por estado de notificación
CREATE INDEX IF NOT EXISTS IDX_FRIA_NOTIFIED
ON FRIAFUNDAMENTALRIGHTSASSESSMENTS(FRIANOTIFIED)
WHERE FRIANOTIFIED = TRUE;

-- Índice en aprobación - Filtros por estado de aprobación
CREATE INDEX IF NOT EXISTS IDX_FRIA_APPROVED
ON FRIAFUNDAMENTALRIGHTSASSESSMENTS(FRIAAPPROVED)
WHERE FRIAAPPROVED = TRUE;

-- Índice en compliance Art. 27 - Filtros por compliance
CREATE INDEX IF NOT EXISTS IDX_FRIA_ART27_COMPLIANT
ON FRIAFUNDAMENTALRIGHTSASSESSMENTS(FRIAART27COMPLIANT)
WHERE FRIAART27COMPLIANT = TRUE;

-- Índice en fecha de notificación - Ordenamiento por fecha de notificación
CREATE INDEX IF NOT EXISTS IDX_FRIA_NOTIFICATION_DATE
ON FRIAFUNDAMENTALRIGHTSASSESSMENTS(FRIANOTIFICATIONDATE DESC);

-- Índice en fecha de aprobación - Ordenamiento por fecha de aprobación
CREATE INDEX IF NOT EXISTS IDX_FRIA_APPROVAL_DATE
ON FRIAFUNDAMENTALRIGHTSASSESSMENTS(FRIAAPPROVALDATE DESC);

-- Índice en ID de notificación - Búsquedas por ID de notificación
CREATE INDEX IF NOT EXISTS IDX_FRIA_NOTIFICATION_ID
ON FRIAFUNDAMENTALRIGHTSASSESSMENTS(FRIANOTIFICATIONID)
WHERE FRIANOTIFICATIONID IS NOT NULL;

-- Índice en DPIA ID - Búsquedas por DPIA integrado
CREATE INDEX IF NOT EXISTS IDX_FRIA_DPIA_ID
ON FRIAFUNDAMENTALRIGHTSASSESSMENTS(FRIADPIAID)
WHERE FRIADPIAID IS NOT NULL;

-- Índice en severidad de impacto - Filtros por severidad
CREATE INDEX IF NOT EXISTS IDX_FRIA_IMPACT_SEVERITY
ON FRIAFUNDAMENTALRIGHTSASSESSMENTS(FRIAIMPACTSEVERITY);

-- Índice en riesgo final - Filtros y ordenamiento por riesgo
CREATE INDEX IF NOT EXISTS IDX_FRIA_FINAL_RISK
ON FRIAFUNDAMENTALRIGHTSASSESSMENTS(FRIAFINALRISK DESC);

-- Índice en score de completitud - Filtros y ordenamiento por completitud
CREATE INDEX IF NOT EXISTS IDX_FRIA_COMPLETENESS_SCORE
ON FRIAFUNDAMENTALRIGHTSASSESSMENTS(FRIACOMPLETENESSCORE DESC);

-- ============================================================================
-- ÍNDICES COMPUESTOS
-- ============================================================================

-- Índice compuesto: Proyecto + Fecha de creación
-- Optimiza: findByProjectIdOrderByCreatedAtDesc
CREATE INDEX IF NOT EXISTS IDX_FRIA_PROJECT_CREATED
ON FRIAFUNDAMENTALRIGHTSASSESSMENTS(IDXPROJECT, FRIACREATEDAT DESC);

-- Índice compuesto: Proyecto + Notificado
-- Optimiza: findByProjectIdAndNotified
CREATE INDEX IF NOT EXISTS IDX_FRIA_PROJECT_NOTIFIED
ON FRIAFUNDAMENTALRIGHTSASSESSMENTS(IDXPROJECT, FRIANOTIFIED)
WHERE FRIANOTIFIED = TRUE;

-- Índice compuesto: Proyecto + Aprobado
-- Optimiza: Consultas por proyecto y estado de aprobación
CREATE INDEX IF NOT EXISTS IDX_FRIA_PROJECT_APPROVED
ON FRIAFUNDAMENTALRIGHTSASSESSMENTS(IDXPROJECT, FRIAAPPROVED)
WHERE FRIAAPPROVED = TRUE;

-- ============================================================================
-- COMENTARIOS SOBRE RENDIMIENTO
-- ============================================================================
--
-- Los índices parciales (WHERE clause) se usan para campos booleanos
-- que tienen valores mayoritariamente FALSE, optimizando el espacio
-- y mejorando el rendimiento de consultas que filtran por TRUE.
--
-- Los índices compuestos optimizan consultas que filtran por múltiples
-- columnas, especialmente cuando se combinan con ordenamiento.
--
-- ============================================================================

