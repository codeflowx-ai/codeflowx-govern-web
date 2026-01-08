-- =====================================================
-- EU REGISTRATION - ÍNDICES ADICIONALES
-- =====================================================
-- Módulo: EU AI Act Compliance
-- Propósito: Índices compuestos y de fechas para optimizar consultas
-- Fecha: Diciembre 2025
-- =====================================================

-- Índices compuestos para búsquedas frecuentes
CREATE INDEX IF NOT EXISTS idx_reg_project_status ON REGEUREGISTRATIONS(IDXPROJECT, REGSTATUS);
CREATE INDEX IF NOT EXISTS idx_reg_type_status ON REGEUREGISTRATIONS(REGREGISTRATIONTYPE, REGSTATUS);
CREATE INDEX IF NOT EXISTS idx_reg_project_type ON REGEUREGISTRATIONS(IDXPROJECT, REGREGISTRATIONTYPE);

-- Índices de fechas para ordenamiento
CREATE INDEX IF NOT EXISTS idx_reg_submission_date ON REGEUREGISTRATIONS(REGSUBMISSIONDATE);
CREATE INDEX IF NOT EXISTS idx_reg_registration_date ON REGEUREGISTRATIONS(REGREGISTRATIONDATE);
CREATE INDEX IF NOT EXISTS idx_reg_created_at ON REGEUREGISTRATIONS(REGCREATEDAT);
CREATE INDEX IF NOT EXISTS idx_reg_updated_at ON REGEUREGISTRATIONS(REGUPDATEDAT);
CREATE INDEX IF NOT EXISTS idx_reg_last_attempt_at ON REGEUREGISTRATIONS(REGLASTATTEMPTAT);

-- Índices adicionales
CREATE INDEX IF NOT EXISTS idx_reg_attempts ON REGEUREGISTRATIONS(REGATTEMPTS);
CREATE INDEX IF NOT EXISTS idx_reg_pmm_plan ON REGEUREGISTRATIONS(IDXPMMPLAN);
CREATE INDEX IF NOT EXISTS idx_reg_national_reg_id ON REGEUREGISTRATIONS(REGNATIONALREGISTRATIONID);

-- Índice parcial para registros activos (no borrados lógicamente)
CREATE INDEX IF NOT EXISTS idx_reg_active ON REGEUREGISTRATIONS(IDXPROJECT, REGSTATUS) 
WHERE REGSTATUS NOT IN ('DELETED', 'CANCELLED');

-- Índice parcial para registros pendientes de envío
CREATE INDEX IF NOT EXISTS idx_reg_pending_submission ON REGEUREGISTRATIONS(IDXPROJECT, REGCREATEDAT) 
WHERE REGSTATUS IN ('DRAFT', 'PENDING');


