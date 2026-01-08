-- ============================================================================
-- SCRIPTS DE BASE DE DATOS - CONFORMITY DECLARATION
-- ============================================================================
-- Módulo: Conformity Declaration (Declaración de Conformidad)
-- Fecha: Enero 2025
-- Base Legal: EU AI Act Art. 48, Annex V
-- ============================================================================

-- ============================================================================
-- ÍNDICES PARA TABLA GOVCONFORMITYDECLARATIONS
-- ============================================================================
-- Estos índices son creados automáticamente por JPA si ddl-auto=update
-- Este script es para referencia y para aplicar manualmente si es necesario
-- ============================================================================

-- Índice en FK a Assessment (usado en JOINs frecuentes)
CREATE INDEX IF NOT EXISTS IDX_DECL_ASSESSMENT 
ON GOVCONFORMITYDECLARATIONS(ASSESSMENT_ID);

-- Índice en Status (usado en filtros y queries)
CREATE INDEX IF NOT EXISTS IDX_DECL_STATUS 
ON GOVCONFORMITYDECLARATIONS(STATUS);

-- Índice en CreatedAt para ordenamiento descendente (queries más comunes)
CREATE INDEX IF NOT EXISTS IDX_DECL_CREATED_AT 
ON GOVCONFORMITYDECLARATIONS(CREATED_AT DESC);

-- Índice compuesto: Assessment + CreatedAt (query: findByAssessment...OrderByCreatedAtDesc)
CREATE INDEX IF NOT EXISTS IDX_DECL_ASSESSMENT_CREATED 
ON GOVCONFORMITYDECLARATIONS(ASSESSMENT_ID, CREATED_AT DESC);

-- Índice compuesto: Status + CreatedAt (query: findByStatusOrderByCreatedAtDesc)
CREATE INDEX IF NOT EXISTS IDX_DECL_STATUS_CREATED 
ON GOVCONFORMITYDECLARATIONS(STATUS, CREATED_AT DESC);

-- Índice en Version (búsquedas por versión)
CREATE INDEX IF NOT EXISTS IDX_DECL_VERSION 
ON GOVCONFORMITYDECLARATIONS(AI_SYSTEM_VERSION);

-- Índice en SignatureDate (filtros por fecha de firma)
CREATE INDEX IF NOT EXISTS IDX_DECL_SIGNATURE_DATE 
ON GOVCONFORMITYDECLARATIONS(SIGNATURE_DATE);

-- Índice en PDFGeneratedAt (filtros por fecha de generación de PDF)
CREATE INDEX IF NOT EXISTS IDX_DECL_PDF_GENERATED_AT 
ON GOVCONFORMITYDECLARATIONS(PDF_GENERATED_AT);

-- Índice en ProviderName (búsquedas por proveedor)
CREATE INDEX IF NOT EXISTS IDX_DECL_PROVIDER_NAME 
ON GOVCONFORMITYDECLARATIONS(PROVIDER_NAME);

-- Índice en AiSystemName (búsquedas por sistema IA)
CREATE INDEX IF NOT EXISTS IDX_DECL_AI_SYSTEM_NAME 
ON GOVCONFORMITYDECLARATIONS(AI_SYSTEM_NAME);

-- Índice en SignedBy (filtros por firmante)
CREATE INDEX IF NOT EXISTS IDX_DECL_SIGNED_BY 
ON GOVCONFORMITYDECLARATIONS(SIGNED_BY);

-- Índice en UpdatedAt (auditoría y ordenamiento)
CREATE INDEX IF NOT EXISTS IDX_DECL_UPDATED_AT 
ON GOVCONFORMITYDECLARATIONS(UPDATED_AT DESC);

-- ============================================================================
-- CLAVE ÚNICA
-- ============================================================================

-- Constraint única: Assessment + Versión (evita duplicados)
-- NOTA: Esta constraint puede ser opcional si se permite múltiples declaraciones con misma versión
-- Si se requiere, descomentar:
-- ALTER TABLE GOVCONFORMITYDECLARATIONS
-- ADD CONSTRAINT UK_DECL_ASSESSMENT_VERSION 
-- UNIQUE (ASSESSMENT_ID, AI_SYSTEM_VERSION);

-- ============================================================================
-- COMENTARIOS SOBRE ÍNDICES
-- ============================================================================
-- 
-- Los índices compuestos (multi-columna) son especialmente importantes para:
-- 1. Queries que filtran por múltiples columnas
-- 2. Queries que ordenan por una columna después de filtrar por otra
-- 3. Mejora de performance en JOINs complejos
--
-- Los índices con DESC son importantes para queries que ordenan descendente,
-- que es el caso más común en este módulo (últimas declaraciones primero).
--
-- ============================================================================
