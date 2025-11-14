-- ============================================================================
-- EU AI ACT COMPLIANCE TABLES
-- ============================================================================
-- Fecha: 2025-11-02
-- Propósito: Tablas para soporte de EU AI Act compliance (100%)
-- Artículos: 11 (Technical Documentation), 12 (Record-keeping), 48 (Declaration of Conformity)
-- ============================================================================

-- ============================================================================
-- TABLA: GOV_AIACT_TECHNICAL_DOCS
-- Documentación técnica según EU AI Act Annex IV (Artículo 11)
-- ============================================================================

CREATE TABLE IF NOT EXISTS GOV_AIACT_TECHNICAL_DOCS (
    -- Primary Key
    IDX_TECHNICAL_DOC BIGSERIAL PRIMARY KEY,
    
    -- Identificación del Sistema IA
    ENTITY_TYPE VARCHAR(50) NOT NULL,  -- MODEL, AGENT, PROMPT, RAG_SYSTEM
    ENTITY_ID BIGINT NOT NULL,
    SYSTEM_NAME VARCHAR(200) NOT NULL,
    VERSION VARCHAR(50) NOT NULL,
    
    -- Secciones Mandatorias Annex IV
    SYSTEM_DESCRIPTION TEXT,
    INTENDED_PURPOSE TEXT,
    DEVELOPMENT_PROCESS TEXT,
    DATA_GOVERNANCE TEXT,
    VALIDATION_PROCEDURES TEXT,
    TESTING_PROCEDURES TEXT,
    MONITORING_MEASURES TEXT,
    HUMAN_OVERSIGHT TEXT,
    RISK_MANAGEMENT TEXT,
    ACCURACY_ROBUSTNESS TEXT,
    CYBERSECURITY_MEASURES TEXT,
    
    -- Generation Metadata
    GENERATED_AT TIMESTAMP,
    GENERATED_BY VARCHAR(100),
    PDF_PATH VARCHAR(500),
    STATUS VARCHAR(50),  -- DRAFT, APPROVED, PUBLISHED
    
    -- Audit Fields
    CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign Keys (opcional)
    ASSESSMENT_ID BIGINT,
    
    -- Constraints
    CONSTRAINT fk_technical_doc_assessment 
        FOREIGN KEY (ASSESSMENT_ID) 
        REFERENCES GOVCOMPLIANCEASSESSMENTS(IDXGOVCOMPLIANCEASSESSMENTS)
        ON DELETE SET NULL
);

-- Índices para GOV_AIACT_TECHNICAL_DOCS
CREATE INDEX idx_technical_docs_entity ON GOV_AIACT_TECHNICAL_DOCS(ENTITY_TYPE, ENTITY_ID);
CREATE INDEX idx_technical_docs_status ON GOV_AIACT_TECHNICAL_DOCS(STATUS);
CREATE INDEX idx_technical_docs_generated_at ON GOV_AIACT_TECHNICAL_DOCS(GENERATED_AT DESC);
CREATE INDEX idx_technical_docs_assessment ON GOV_AIACT_TECHNICAL_DOCS(ASSESSMENT_ID);

-- Comentarios
COMMENT ON TABLE GOV_AIACT_TECHNICAL_DOCS IS 'Documentación técnica EU AI Act Annex IV (Art. 11)';
COMMENT ON COLUMN GOV_AIACT_TECHNICAL_DOCS.ENTITY_TYPE IS 'Tipo de entidad: MODEL, AGENT, PROMPT, RAG_SYSTEM';
COMMENT ON COLUMN GOV_AIACT_TECHNICAL_DOCS.STATUS IS 'Estado: DRAFT, APPROVED, PUBLISHED';

-- ============================================================================
-- TABLA: GOV_CONFORMITY_DECLARATIONS
-- EU Declaration of Conformity según AI Act Annex V (Artículo 48)
-- ============================================================================

CREATE TABLE IF NOT EXISTS GOV_CONFORMITY_DECLARATIONS (
    -- Primary Key
    IDX_DECLARATION BIGSERIAL PRIMARY KEY,
    
    -- Provider Information (Mandatory Annex V)
    PROVIDER_NAME VARCHAR(200) NOT NULL,
    PROVIDER_ADDRESS TEXT,
    PROVIDER_COUNTRY VARCHAR(100),
    PROVIDER_CONTACT VARCHAR(200),
    
    -- AI System Information
    AI_SYSTEM_NAME VARCHAR(200) NOT NULL,
    AI_SYSTEM_TYPE VARCHAR(50) NOT NULL,
    AI_SYSTEM_VERSION VARCHAR(50),
    INTENDED_PURPOSE TEXT,
    RISK_CATEGORY VARCHAR(50),  -- HIGH_RISK, LIMITED_RISK
    
    -- Conformity Basis
    CONFORMITY_BASIS TEXT,
    APPLIED_STANDARDS TEXT,  -- JSON array
    HARMONIZED_STANDARDS TEXT,
    
    -- Compliance per Article (Boolean)
    ART9_RISK_MANAGEMENT BOOLEAN,
    ART10_DATA_GOVERNANCE BOOLEAN,
    ART11_DOCUMENTATION BOOLEAN,
    ART12_RECORD_KEEPING BOOLEAN,
    ART13_TRANSPARENCY BOOLEAN,
    ART14_HUMAN_OVERSIGHT BOOLEAN,
    ART15_ACCURACY BOOLEAN,
    
    -- Scores
    OVERALL_COMPLIANCE_SCORE DECIMAL(5,2),
    COMPLIANCE_PERCENTAGE DECIMAL(5,2),
    
    -- Signature
    SIGNATURE_DATE TIMESTAMP,
    SIGNED_BY VARCHAR(200),
    DIGITAL_SIGNATURE TEXT,  -- For eIDAS (fase 2)
    
    -- PDF Generation
    PDF_PATH VARCHAR(500),
    PDF_GENERATED_AT TIMESTAMP,
    
    -- Status
    STATUS VARCHAR(50),  -- DRAFT, SIGNED, PUBLISHED, REVOKED
    
    -- Foreign Keys
    ASSESSMENT_ID BIGINT,
    
    -- Audit Fields
    CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT fk_conformity_declaration_assessment 
        FOREIGN KEY (ASSESSMENT_ID) 
        REFERENCES GOVCOMPLIANCEASSESSMENTS(IDXGOVCOMPLIANCEASSESSMENTS)
        ON DELETE SET NULL
);

-- Índices para GOV_CONFORMITY_DECLARATIONS
CREATE INDEX idx_conformity_decl_system ON GOV_CONFORMITY_DECLARATIONS(AI_SYSTEM_NAME);
CREATE INDEX idx_conformity_decl_type ON GOV_CONFORMITY_DECLARATIONS(AI_SYSTEM_TYPE);
CREATE INDEX idx_conformity_decl_status ON GOV_CONFORMITY_DECLARATIONS(STATUS);
CREATE INDEX idx_conformity_decl_signature_date ON GOV_CONFORMITY_DECLARATIONS(SIGNATURE_DATE DESC);
CREATE INDEX idx_conformity_decl_assessment ON GOV_CONFORMITY_DECLARATIONS(ASSESSMENT_ID);
CREATE INDEX idx_conformity_decl_risk_category ON GOV_CONFORMITY_DECLARATIONS(RISK_CATEGORY);

-- Comentarios
COMMENT ON TABLE GOV_CONFORMITY_DECLARATIONS IS 'EU Declaration of Conformity AI Act Annex V (Art. 48)';
COMMENT ON COLUMN GOV_CONFORMITY_DECLARATIONS.STATUS IS 'Estado: DRAFT, SIGNED, PUBLISHED, REVOKED';
COMMENT ON COLUMN GOV_CONFORMITY_DECLARATIONS.RISK_CATEGORY IS 'Categoría de riesgo: HIGH_RISK, LIMITED_RISK';
COMMENT ON COLUMN GOV_CONFORMITY_DECLARATIONS.ART9_RISK_MANAGEMENT IS 'Cumplimiento Artículo 9: Gestión de Riesgos';
COMMENT ON COLUMN GOV_CONFORMITY_DECLARATIONS.ART10_DATA_GOVERNANCE IS 'Cumplimiento Artículo 10: Gobernanza de Datos';
COMMENT ON COLUMN GOV_CONFORMITY_DECLARATIONS.ART11_DOCUMENTATION IS 'Cumplimiento Artículo 11: Documentación';
COMMENT ON COLUMN GOV_CONFORMITY_DECLARATIONS.ART12_RECORD_KEEPING IS 'Cumplimiento Artículo 12: Registro de Datos';
COMMENT ON COLUMN GOV_CONFORMITY_DECLARATIONS.ART13_TRANSPARENCY IS 'Cumplimiento Artículo 13: Transparencia';
COMMENT ON COLUMN GOV_CONFORMITY_DECLARATIONS.ART14_HUMAN_OVERSIGHT IS 'Cumplimiento Artículo 14: Supervisión Humana';
COMMENT ON COLUMN GOV_CONFORMITY_DECLARATIONS.ART15_ACCURACY IS 'Cumplimiento Artículo 15: Precisión';

-- ============================================================================
-- VISTA: V_AI_ACT_COMPLIANCE_SUMMARY
-- Vista consolidada de estado de compliance AI Act
-- ============================================================================

CREATE OR REPLACE VIEW V_AI_ACT_COMPLIANCE_SUMMARY AS
SELECT 
    ca.IDXGOVCOMPLIANCEASSESSMENTS AS assessment_id,
    ca.ASSESSMENTNAME AS system_name,
    ca.COMPLIANCEFRAMEWORK AS framework,
    ca.OVERALLSCORE AS overall_score,
    ca.COMPLIANCEPERCENTAGE AS compliance_percentage,
    ca.STATUS AS assessment_status,
    
    -- Documentación técnica
    td.IDX_TECHNICAL_DOC AS doc_id,
    td.STATUS AS doc_status,
    td.GENERATED_AT AS doc_generated_at,
    td.PDF_PATH AS doc_pdf_path,
    
    -- Declaración de conformidad
    cd.IDX_DECLARATION AS declaration_id,
    cd.STATUS AS declaration_status,
    cd.SIGNATURE_DATE AS declaration_signature_date,
    cd.SIGNED_BY AS declaration_signed_by,
    cd.PDF_PATH AS declaration_pdf_path,
    
    -- Indicadores de compliance
    CASE 
        WHEN cd.IDX_DECLARATION IS NOT NULL AND cd.STATUS = 'PUBLISHED' THEN 'COMPLIANT'
        WHEN cd.IDX_DECLARATION IS NOT NULL AND cd.STATUS = 'SIGNED' THEN 'SIGNED'
        WHEN td.IDX_TECHNICAL_DOC IS NOT NULL THEN 'DOCUMENTED'
        ELSE 'PENDING'
    END AS ai_act_status,
    
    -- Compliance per article (desde declaration)
    cd.ART9_RISK_MANAGEMENT,
    cd.ART10_DATA_GOVERNANCE,
    cd.ART11_DOCUMENTATION,
    cd.ART12_RECORD_KEEPING,
    cd.ART13_TRANSPARENCY,
    cd.ART14_HUMAN_OVERSIGHT,
    cd.ART15_ACCURACY
    
FROM GOVCOMPLIANCEASSESSMENTS ca
LEFT JOIN GOV_AIACT_TECHNICAL_DOCS td ON ca.IDXGOVCOMPLIANCEASSESSMENTS = td.ASSESSMENT_ID
LEFT JOIN GOV_CONFORMITY_DECLARATIONS cd ON ca.IDXGOVCOMPLIANCEASSESSMENTS = cd.ASSESSMENT_ID
WHERE ca.COMPLIANCEFRAMEWORK LIKE '%AI_ACT%' OR ca.COMPLIANCEFRAMEWORK LIKE '%EU_AI_ACT%';

COMMENT ON VIEW V_AI_ACT_COMPLIANCE_SUMMARY IS 'Vista consolidada de estado de compliance EU AI Act';

-- ============================================================================
-- FUNCIÓN: GET_AI_ACT_COMPLIANCE_PERCENTAGE
-- Calcula porcentaje de compliance AI Act para una entidad
-- ============================================================================

CREATE OR REPLACE FUNCTION GET_AI_ACT_COMPLIANCE_PERCENTAGE(
    p_entity_type VARCHAR,
    p_entity_id BIGINT
) RETURNS DECIMAL(5,2) AS $$
DECLARE
    v_compliance_percentage DECIMAL(5,2);
    v_has_documentation BOOLEAN;
    v_has_declaration BOOLEAN;
    v_declaration_signed BOOLEAN;
    v_articles_compliant INTEGER;
BEGIN
    -- Verificar documentación técnica
    SELECT COUNT(*) > 0 INTO v_has_documentation
    FROM GOV_AIACT_TECHNICAL_DOCS
    WHERE ENTITY_TYPE = p_entity_type 
      AND ENTITY_ID = p_entity_id
      AND STATUS IN ('APPROVED', 'PUBLISHED');
    
    -- Verificar declaración de conformidad
    SELECT 
        COUNT(*) > 0,
        MAX(CASE WHEN STATUS IN ('SIGNED', 'PUBLISHED') THEN TRUE ELSE FALSE END)
    INTO v_has_declaration, v_declaration_signed
    FROM GOV_CONFORMITY_DECLARATIONS cd
    JOIN GOV_AIACT_TECHNICAL_DOCS td ON cd.ASSESSMENT_ID = td.ASSESSMENT_ID
    WHERE td.ENTITY_TYPE = p_entity_type 
      AND td.ENTITY_ID = p_entity_id;
    
    -- Contar artículos cumplidos
    SELECT 
        COALESCE(
            (CASE WHEN ART9_RISK_MANAGEMENT THEN 1 ELSE 0 END) +
            (CASE WHEN ART10_DATA_GOVERNANCE THEN 1 ELSE 0 END) +
            (CASE WHEN ART11_DOCUMENTATION THEN 1 ELSE 0 END) +
            (CASE WHEN ART12_RECORD_KEEPING THEN 1 ELSE 0 END) +
            (CASE WHEN ART13_TRANSPARENCY THEN 1 ELSE 0 END) +
            (CASE WHEN ART14_HUMAN_OVERSIGHT THEN 1 ELSE 0 END) +
            (CASE WHEN ART15_ACCURACY THEN 1 ELSE 0 END),
            0
        ) INTO v_articles_compliant
    FROM GOV_CONFORMITY_DECLARATIONS cd
    JOIN GOV_AIACT_TECHNICAL_DOCS td ON cd.ASSESSMENT_ID = td.ASSESSMENT_ID
    WHERE td.ENTITY_TYPE = p_entity_type 
      AND td.ENTITY_ID = p_entity_id
    ORDER BY cd.CREATED_AT DESC
    LIMIT 1;
    
    -- Calcular porcentaje
    v_compliance_percentage := 0;
    
    IF v_has_documentation THEN
        v_compliance_percentage := v_compliance_percentage + 30;
    END IF;
    
    IF v_has_declaration THEN
        v_compliance_percentage := v_compliance_percentage + 20;
    END IF;
    
    IF v_declaration_signed THEN
        v_compliance_percentage := v_compliance_percentage + 10;
    END IF;
    
    -- Artículos: 7 artículos, cada uno vale ~5.7%
    v_compliance_percentage := v_compliance_percentage + (v_articles_compliant * 5.7);
    
    RETURN LEAST(v_compliance_percentage, 100.00);
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION GET_AI_ACT_COMPLIANCE_PERCENTAGE IS 'Calcula porcentaje de compliance EU AI Act para una entidad';

-- ============================================================================
-- TRIGGER: UPDATE TIMESTAMP ON UPDATE
-- Actualiza automáticamente el campo UPDATED_AT
-- ============================================================================

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.UPDATED_AT = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para GOV_AIACT_TECHNICAL_DOCS
DROP TRIGGER IF EXISTS trg_update_technical_docs_updated_at ON GOV_AIACT_TECHNICAL_DOCS;
CREATE TRIGGER trg_update_technical_docs_updated_at
    BEFORE UPDATE ON GOV_AIACT_TECHNICAL_DOCS
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Trigger para GOV_CONFORMITY_DECLARATIONS
DROP TRIGGER IF EXISTS trg_update_conformity_decl_updated_at ON GOV_CONFORMITY_DECLARATIONS;
CREATE TRIGGER trg_update_conformity_decl_updated_at
    BEFORE UPDATE ON GOV_CONFORMITY_DECLARATIONS
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- ============================================================================
-- PERMISOS (OPCIONAL - ajustar según usuario de BD)
-- ============================================================================

-- GRANT SELECT, INSERT, UPDATE, DELETE ON GOV_AIACT_TECHNICAL_DOCS TO codeflowx_app;
-- GRANT SELECT, INSERT, UPDATE, DELETE ON GOV_CONFORMITY_DECLARATIONS TO codeflowx_app;
-- GRANT SELECT ON V_AI_ACT_COMPLIANCE_SUMMARY TO codeflowx_app;
-- GRANT EXECUTE ON FUNCTION GET_AI_ACT_COMPLIANCE_PERCENTAGE TO codeflowx_app;

-- ============================================================================
-- FIN DEL SCRIPT
-- ============================================================================

-- Verificación
SELECT 'Tablas AI Act creadas exitosamente' AS status;
SELECT COUNT(*) AS technical_docs_count FROM GOV_AIACT_TECHNICAL_DOCS;
SELECT COUNT(*) AS declarations_count FROM GOV_CONFORMITY_DECLARATIONS;

