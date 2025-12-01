-- ============================================================================
-- PATCH 13: INC-010-010 - Integración con Sistema de Feedback
-- ============================================================================
--
-- **Incidencia:** INC-010-010
-- **Prioridad:** 🟡 ALTA
-- **Artículo EU AI Act:** Art. 72
-- **Fecha:** 2025-01-27
--
-- **Descripción:**
-- Crea tabla para almacenar feedback de usuarios sobre sistemas de IA.
-- Integra con análisis de sentimiento para cálculo de satisfacción de usuario
-- según Art. 72 (Post Market Monitoring).
--
-- **Cambios:**
-- 1. Crear tabla USFUSERFEEDBACKS
-- 2. Agregar foreign keys a PRJPROJECTS y MODMODELS
-- 3. Crear índices para optimizar consultas
-- 4. Agregar constraints
--
-- ============================================================================

-- ============================================================================
-- 1. CREAR TABLA USFUSERFEEDBACKS
-- ============================================================================

CREATE TABLE IF NOT EXISTS USFUSERFEEDBACKS (
    IDXUSFFEEDBACK BIGSERIAL PRIMARY KEY,
    iduuid VARCHAR(36) UNIQUE NOT NULL DEFAULT gen_random_uuid()::text,

    -- Foreign Keys
    IDXPROJECT BIGINT NOT NULL,
    IDXMODEL BIGINT,

    -- Tipo y contenido de feedback
    USFFEEDBACKTYPE VARCHAR(50) NOT NULL, -- RATING, COMMENT, COMPLAINT, ESCALATION
    USFFEEDBACKTEXT TEXT,
    USFRATING INTEGER, -- 1-5 stars

    -- Análisis de sentimiento
    USFSENTIMENTSCORE DECIMAL(3,2), -- -1.0 a 1.0
    USFSENTIMENTLABEL VARCHAR(50), -- POSITIVE, NEUTRAL, NEGATIVE

    -- Metadata
    USFUSERID VARCHAR(255),
    USFSOURCE VARCHAR(100), -- TICKET_SYSTEM, API, UI
    USFSTATUS VARCHAR(50) NOT NULL DEFAULT 'PENDING', -- PENDING, PROCESSED, ARCHIVED

    -- Audit fields
    USFCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    USFUPDATEDAT TIMESTAMP,

    -- Foreign key constraints
    CONSTRAINT FK_USF_PROJECT FOREIGN KEY (IDXPROJECT) REFERENCES PRJPROJECTS(IDXPROJECT) ON DELETE CASCADE,
    CONSTRAINT FK_USF_MODEL FOREIGN KEY (IDXMODEL) REFERENCES MODMODELS(IDXMODEL) ON DELETE SET NULL,

    -- Check constraints
    CONSTRAINT CHK_USF_RATING CHECK (USFRATING IS NULL OR (USFRATING >= 1 AND USFRATING <= 5)),
    CONSTRAINT CHK_USF_SENTIMENT_SCORE CHECK (USFSENTIMENTSCORE IS NULL OR (USFSENTIMENTSCORE >= -1.0 AND USFSENTIMENTSCORE <= 1.0)),
    CONSTRAINT CHK_USF_FEEDBACK_TYPE CHECK (USFFEEDBACKTYPE IN ('RATING', 'COMMENT', 'COMPLAINT', 'ESCALATION')),
    CONSTRAINT CHK_USF_STATUS CHECK (USFSTATUS IN ('PENDING', 'PROCESSED', 'ARCHIVED'))
);

-- ============================================================================
-- 2. CREAR ÍNDICES
-- ============================================================================

-- Índice por proyecto
CREATE INDEX IF NOT EXISTS IDX_USF_PROJECT
ON USFUSERFEEDBACKS(IDXPROJECT);

-- Índice por modelo
CREATE INDEX IF NOT EXISTS IDX_USF_MODEL
ON USFUSERFEEDBACKS(IDXMODEL);

-- Índice por fecha de creación (para consultas de últimos N días)
CREATE INDEX IF NOT EXISTS IDX_USF_CREATED
ON USFUSERFEEDBACKS(USFCREATEDAT DESC);

-- Índice por estado
CREATE INDEX IF NOT EXISTS IDX_USF_STATUS
ON USFUSERFEEDBACKS(USFSTATUS);

-- Índice compuesto para búsqueda de satisfacción (proyecto + modelo + fecha + estado)
CREATE INDEX IF NOT EXISTS IDX_USF_SATISFACTION
ON USFUSERFEEDBACKS(IDXPROJECT, IDXMODEL, USFCREATEDAT DESC, USFSTATUS)
WHERE USFSTATUS = 'PROCESSED' AND USFSENTIMENTSCORE IS NOT NULL;

-- Índice por tipo de feedback
CREATE INDEX IF NOT EXISTS IDX_USF_TYPE
ON USFUSERFEEDBACKS(USFFEEDBACKTYPE);

-- ============================================================================
-- 3. COMENTARIOS DE COLUMNAS
-- ============================================================================

COMMENT ON TABLE USFUSERFEEDBACKS IS
'Tabla para almacenar feedback de usuarios sobre sistemas de IA. Integra con análisis de sentimiento para cálculo de satisfacción según Art. 72 (INC-010-010)';

COMMENT ON COLUMN USFUSERFEEDBACKS.IDXUSFFEEDBACK IS 'Primary key autonumérico';
COMMENT ON COLUMN USFUSERFEEDBACKS.iduuid IS 'UUID único para identificación externa';
COMMENT ON COLUMN USFUSERFEEDBACKS.IDXPROJECT IS 'Foreign key a PRJPROJECTS';
COMMENT ON COLUMN USFUSERFEEDBACKS.IDXMODEL IS 'Foreign key a MODMODELS (opcional)';
COMMENT ON COLUMN USFUSERFEEDBACKS.USFFEEDBACKTYPE IS 'Tipo de feedback: RATING, COMMENT, COMPLAINT, ESCALATION';
COMMENT ON COLUMN USFUSERFEEDBACKS.USFFEEDBACKTEXT IS 'Texto del feedback (opcional)';
COMMENT ON COLUMN USFUSERFEEDBACKS.USFRATING IS 'Rating numérico de 1 a 5 estrellas (opcional)';
COMMENT ON COLUMN USFUSERFEEDBACKS.USFSENTIMENTSCORE IS 'Score de sentimiento de -1.0 (negativo) a 1.0 (positivo)';
COMMENT ON COLUMN USFUSERFEEDBACKS.USFSENTIMENTLABEL IS 'Label de sentimiento: POSITIVE, NEUTRAL, NEGATIVE';
COMMENT ON COLUMN USFUSERFEEDBACKS.USFUSERID IS 'ID del usuario que proporcionó el feedback';
COMMENT ON COLUMN USFUSERFEEDBACKS.USFSOURCE IS 'Origen del feedback: TICKET_SYSTEM, API, UI';
COMMENT ON COLUMN USFUSERFEEDBACKS.USFSTATUS IS 'Estado del feedback: PENDING, PROCESSED, ARCHIVED';
COMMENT ON COLUMN USFUSERFEEDBACKS.USFCREATEDAT IS 'Fecha/hora de creación';
COMMENT ON COLUMN USFUSERFEEDBACKS.USFUPDATEDAT IS 'Fecha/hora de última actualización';

-- ============================================================================
-- 4. VERIFICACIÓN
-- ============================================================================

-- Verificar que la tabla se creó correctamente
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.tables
        WHERE table_name = 'USFUSERFEEDBACKS'
    ) THEN
        RAISE EXCEPTION 'Tabla USFUSERFEEDBACKS no encontrada';
    END IF;

    RAISE NOTICE 'Patch 13 (INC-010-010) aplicado correctamente';
END $$;

-- ============================================================================
-- FIN DEL PATCH
-- ============================================================================
