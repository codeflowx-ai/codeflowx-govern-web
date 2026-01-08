-- =====================================================
-- EU REGISTRATION - TRIGGERS
-- =====================================================
-- Módulo: EU AI Act Compliance
-- Propósito: Triggers para auditoría, validación y lógica de negocio
-- Fecha: Diciembre 2025
-- =====================================================

-- Función para actualizar REGUPDATEDAT automáticamente
CREATE OR REPLACE FUNCTION trg_reg_update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.REGUPDATEDAT = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para actualizar timestamp en UPDATE
DROP TRIGGER IF EXISTS trg_reg_audit ON REGEUREGISTRATIONS;
CREATE TRIGGER trg_reg_audit
    BEFORE UPDATE ON REGEUREGISTRATIONS
    FOR EACH ROW
    EXECUTE FUNCTION trg_reg_update_timestamp();

-- Función para validar transiciones de estado
CREATE OR REPLACE FUNCTION trg_reg_validate_status()
RETURNS TRIGGER AS $$
BEGIN
    -- Validar transiciones de estado permitidas
    IF OLD.REGSTATUS IS NOT NULL AND NEW.REGSTATUS IS NOT NULL THEN
        -- DRAFT puede pasar a cualquier estado
        IF OLD.REGSTATUS = 'DRAFT' THEN
            -- Permitido
        -- PENDING solo puede pasar a SUBMITTED o DRAFT
        ELSIF OLD.REGSTATUS = 'PENDING' AND NEW.REGSTATUS NOT IN ('SUBMITTED', 'DRAFT', 'REJECTED') THEN
            RAISE EXCEPTION 'Invalid status transition from PENDING to %', NEW.REGSTATUS;
        -- SUBMITTED solo puede pasar a REGISTERED, REJECTED o ERROR
        ELSIF OLD.REGSTATUS = 'SUBMITTED' AND NEW.REGSTATUS NOT IN ('REGISTERED', 'REJECTED', 'ERROR') THEN
            RAISE EXCEPTION 'Invalid status transition from SUBMITTED to %', NEW.REGSTATUS;
        -- REGISTERED es estado final
        ELSIF OLD.REGSTATUS = 'REGISTERED' AND NEW.REGSTATUS != 'REGISTERED' THEN
            RAISE EXCEPTION 'Cannot change status from REGISTERED';
        -- REJECTED puede pasar a DRAFT o PENDING
        ELSIF OLD.REGSTATUS = 'REJECTED' AND NEW.REGSTATUS NOT IN ('DRAFT', 'PENDING') THEN
            RAISE EXCEPTION 'Invalid status transition from REJECTED to %', NEW.REGSTATUS;
        -- ERROR puede pasar a DRAFT o PENDING
        ELSIF OLD.REGSTATUS = 'ERROR' AND NEW.REGSTATUS NOT IN ('DRAFT', 'PENDING') THEN
            RAISE EXCEPTION 'Invalid status transition from ERROR to %', NEW.REGSTATUS;
        END IF;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para validar transiciones de estado
DROP TRIGGER IF EXISTS trg_reg_validate_status ON REGEUREGISTRATIONS;
CREATE TRIGGER trg_reg_validate_status
    BEFORE UPDATE ON REGEUREGISTRATIONS
    FOR EACH ROW
    WHEN (OLD.REGSTATUS IS DISTINCT FROM NEW.REGSTATUS)
    EXECUTE FUNCTION trg_reg_validate_status();

-- Función para validar formato de EU Registration ID
CREATE OR REPLACE FUNCTION trg_reg_validate_eu_id()
RETURNS TRIGGER AS $$
BEGIN
    -- Validar formato cuando se asigna EU Registration ID
    IF NEW.REGEUREGISTRATIONID IS NOT NULL THEN
        -- Formato esperado: EU-REG-YYYY-NNNNNN o similar
        IF NEW.REGEUREGISTRATIONID !~ '^EU-[A-Z0-9-]+$' THEN
            RAISE EXCEPTION 'Invalid EU Registration ID format: %. Expected format: EU-REG-YYYY-NNNNNN', NEW.REGEUREGISTRATIONID;
        END IF;
        
        -- Si se asigna EU Registration ID, el estado debe ser REGISTERED
        IF NEW.REGSTATUS != 'REGISTERED' THEN
            NEW.REGSTATUS = 'REGISTERED';
            NEW.REGREGISTRATIONDATE = CURRENT_TIMESTAMP;
        END IF;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para validar formato de EU Registration ID
DROP TRIGGER IF EXISTS trg_reg_eu_id_format ON REGEUREGISTRATIONS;
CREATE TRIGGER trg_reg_eu_id_format
    BEFORE INSERT OR UPDATE ON REGEUREGISTRATIONS
    FOR EACH ROW
    WHEN (NEW.REGEUREGISTRATIONID IS NOT NULL)
    EXECUTE FUNCTION trg_reg_validate_eu_id();

-- Función para incrementar contador de intentos
CREATE OR REPLACE FUNCTION trg_reg_increment_attempts()
RETURNS TRIGGER AS $$
BEGIN
    -- Si el estado cambia a ERROR o REJECTED, incrementar intentos
    IF NEW.REGSTATUS IN ('ERROR', 'REJECTED') AND OLD.REGSTATUS != NEW.REGSTATUS THEN
        NEW.REGATTEMPTS = COALESCE(OLD.REGATTEMPTS, 0) + 1;
        NEW.REGLASTATTEMPTAT = CURRENT_TIMESTAMP;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para incrementar contador de intentos
DROP TRIGGER IF EXISTS trg_reg_attempts ON REGEUREGISTRATIONS;
CREATE TRIGGER trg_reg_attempts
    BEFORE UPDATE ON REGEUREGISTRATIONS
    FOR EACH ROW
    WHEN (NEW.REGSTATUS IN ('ERROR', 'REJECTED') AND OLD.REGSTATUS != NEW.REGSTATUS)
    EXECUTE FUNCTION trg_reg_increment_attempts();


