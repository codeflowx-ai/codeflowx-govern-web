-- ============================================================================
-- PATCH: Agregar Campos de Negocio al Módulo CORE
-- ============================================================================
-- Fecha: 2025-10-15
-- Descripción: Agrega todos los campos de negocio faltantes en las tablas CORE
-- Impacto: ALTO - Modifica 6 tablas, agrega ~30 campos
-- Rollback: Ver 01_core_rollback_business_fields.sql
-- ============================================================================

BEGIN;

-- ============================================================================
-- 1. CORDEPARTMENTS - Agregar campos de negocio
-- ============================================================================
ALTER TABLE CORDEPARTMENTS 
    ADD COLUMN IF NOT EXISTS CORNAME VARCHAR(100) NOT NULL DEFAULT 'Departamento Sin Nombre',
    ADD COLUMN IF NOT EXISTS DESCRIPTION TEXT,
    ADD COLUMN IF NOT EXISTS CODE VARCHAR(50),
    ADD COLUMN IF NOT EXISTS MANAGERID BIGINT,
    ADD COLUMN IF NOT EXISTS PARENTID BIGINT,
    ADD COLUMN IF NOT EXISTS ISACTIVE BOOLEAN DEFAULT TRUE;

-- Índices para CORDEPARTMENTS
CREATE UNIQUE INDEX IF NOT EXISTS idx_cordepartments_code ON CORDEPARTMENTS(CODE) WHERE CODE IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_cordepartments_manager ON CORDEPARTMENTS(MANAGERID);
CREATE INDEX IF NOT EXISTS idx_cordepartments_parent ON CORDEPARTMENTS(PARENTID);
CREATE INDEX IF NOT EXISTS idx_cordepartments_active ON CORDEPARTMENTS(ISACTIVE);

-- ============================================================================
-- 2. CORROLES - Agregar campos de negocio
-- ============================================================================
ALTER TABLE CORROLES 
    ADD COLUMN IF NOT EXISTS ROLENAME VARCHAR(100) NOT NULL DEFAULT 'Rol Sin Nombre',
    ADD COLUMN IF NOT EXISTS DESCRIPTION TEXT,
    ADD COLUMN IF NOT EXISTS ISACTIVE BOOLEAN DEFAULT TRUE,
    ADD COLUMN IF NOT EXISTS ISSYSTEM BOOLEAN DEFAULT FALSE;

-- Índices para CORROLES
CREATE UNIQUE INDEX IF NOT EXISTS idx_corroles_name ON CORROLES(ROLENAME);
CREATE INDEX IF NOT EXISTS idx_corroles_active ON CORROLES(ISACTIVE);
CREATE INDEX IF NOT EXISTS idx_corroles_system ON CORROLES(ISSYSTEM);

-- ============================================================================
-- 3. CORPERMISSIONS - Agregar campos de negocio
-- ============================================================================
ALTER TABLE CORPERMISSIONS 
    ADD COLUMN IF NOT EXISTS PERMISSIONNAME VARCHAR(100) NOT NULL DEFAULT 'Permiso Sin Nombre',
    ADD COLUMN IF NOT EXISTS DESCRIPTION TEXT,
    ADD COLUMN IF NOT EXISTS RESOURCE VARCHAR(100),
    ADD COLUMN IF NOT EXISTS ACTION VARCHAR(50),
    ADD COLUMN IF NOT EXISTS ISACTIVE BOOLEAN DEFAULT TRUE;

-- Índices para CORPERMISSIONS
CREATE UNIQUE INDEX IF NOT EXISTS idx_corpermissions_name ON CORPERMISSIONS(PERMISSIONNAME);
CREATE INDEX IF NOT EXISTS idx_corpermissions_resource ON CORPERMISSIONS(RESOURCE);
CREATE INDEX IF NOT EXISTS idx_corpermissions_active ON CORPERMISSIONS(ISACTIVE);

-- ============================================================================
-- 4. CORMENUS - Agregar campos de negocio
-- ============================================================================
ALTER TABLE CORMENUS 
    ADD COLUMN IF NOT EXISTS MENUNAME VARCHAR(100) NOT NULL DEFAULT 'Menú Sin Nombre',
    ADD COLUMN IF NOT EXISTS LABEL VARCHAR(100),
    ADD COLUMN IF NOT EXISTS ICON VARCHAR(50),
    ADD COLUMN IF NOT EXISTS URL VARCHAR(255),
    ADD COLUMN IF NOT EXISTS ORDINAL INT DEFAULT 0,
    ADD COLUMN IF NOT EXISTS ISACTIVE BOOLEAN DEFAULT TRUE,
    ADD COLUMN IF NOT EXISTS ISVISIBLE BOOLEAN DEFAULT TRUE;

-- Índices para CORMENUS
CREATE INDEX IF NOT EXISTS idx_cormenus_parent ON CORMENUS(PARENTID);
CREATE INDEX IF NOT EXISTS idx_cormenus_ordinal ON CORMENUS(ORDINAL);
CREATE INDEX IF NOT EXISTS idx_cormenus_active ON CORMENUS(ISACTIVE);

-- ============================================================================
-- 5. CORUSERS - Agregar campos de negocio
-- ============================================================================
ALTER TABLE CORUSERS 
    ADD COLUMN IF NOT EXISTS USERNAME VARCHAR(100),
    ADD COLUMN IF NOT EXISTS EMAIL VARCHAR(255),
    ADD COLUMN IF NOT EXISTS PHONE VARCHAR(20),
    ADD COLUMN IF NOT EXISTS ISACTIVE BOOLEAN DEFAULT TRUE,
    ADD COLUMN IF NOT EXISTS ISLOCKED BOOLEAN DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS FAILEDLOGINS INT DEFAULT 0,
    ADD COLUMN IF NOT EXISTS DEPARTMENTID BIGINT;

-- Índices para CORUSERS
CREATE UNIQUE INDEX IF NOT EXISTS idx_corusers_username ON CORUSERS(USERNAME) WHERE USERNAME IS NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS idx_corusers_email ON CORUSERS(EMAIL) WHERE EMAIL IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_corusers_department ON CORUSERS(DEPARTMENTID);
CREATE INDEX IF NOT EXISTS idx_corusers_active ON CORUSERS(ISACTIVE);
CREATE INDEX IF NOT EXISTS idx_corusers_locked ON CORUSERS(ISLOCKED);

-- ============================================================================
-- 6. CORLOGINATTEMPTS - Agregar campos de negocio
-- ============================================================================
ALTER TABLE CORLOGINATTEMPTS 
    ADD COLUMN IF NOT EXISTS USERNAME VARCHAR(100),
    ADD COLUMN IF NOT EXISTS USERID BIGINT,
    ADD COLUMN IF NOT EXISTS REASON TEXT;

-- Índices para CORLOGINATTEMPTS
CREATE INDEX IF NOT EXISTS idx_corloginattempts_username ON CORLOGINATTEMPTS(USERNAME);
CREATE INDEX IF NOT EXISTS idx_corloginattempts_userid ON CORLOGINATTEMPTS(USERID);
CREATE INDEX IF NOT EXISTS idx_corloginattempts_time ON CORLOGINATTEMPTS(ATTEMPTTIME);
CREATE INDEX IF NOT EXISTS idx_corloginattempts_successful ON CORLOGINATTEMPTS(ISSUCCESSFUL);

-- ============================================================================
-- 7. Foreign Keys
-- ============================================================================

-- CORDEPARTMENTS
ALTER TABLE CORDEPARTMENTS 
    DROP CONSTRAINT IF EXISTS FK_DEPT_MANAGER,
    ADD CONSTRAINT FK_DEPT_MANAGER 
        FOREIGN KEY (MANAGERID) REFERENCES CORUSERS(IDXUSER) ON DELETE SET NULL;

ALTER TABLE CORDEPARTMENTS 
    DROP CONSTRAINT IF EXISTS FK_DEPT_PARENT,
    ADD CONSTRAINT FK_DEPT_PARENT 
        FOREIGN KEY (PARENTID) REFERENCES CORDEPARTMENTS(IDXDEPARTMENT) ON DELETE CASCADE;

-- CORUSERS
ALTER TABLE CORUSERS 
    DROP CONSTRAINT IF EXISTS FK_USER_DEPT,
    ADD CONSTRAINT FK_USER_DEPT 
        FOREIGN KEY (DEPARTMENTID) REFERENCES CORDEPARTMENTS(IDXDEPARTMENT) ON DELETE SET NULL;

-- CORLOGINATTEMPTS
ALTER TABLE CORLOGINATTEMPTS 
    DROP CONSTRAINT IF EXISTS FK_LOGIN_USER,
    ADD CONSTRAINT FK_LOGIN_USER 
        FOREIGN KEY (USERID) REFERENCES CORUSERS(IDXUSER) ON DELETE CASCADE;

-- ============================================================================
-- 8. Tablas Intermedias (Many-to-Many)
-- ============================================================================

-- CORROLEUSERS (Usuarios-Roles)
CREATE TABLE IF NOT EXISTS CORROLEUSERS (
    iduuid UUID UNIQUE DEFAULT gen_random_uuid(),
    IDXROLEUSER BIGSERIAL PRIMARY KEY,
    USERID BIGINT NOT NULL,
    ROLEID BIGINT NOT NULL,
    ASSIGNEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ASSIGNEDBY BIGINT,
    CREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_ROLEUSER_USER FOREIGN KEY (USERID) REFERENCES CORUSERS(IDXUSER) ON DELETE CASCADE,
    CONSTRAINT FK_ROLEUSER_ROLE FOREIGN KEY (ROLEID) REFERENCES CORROLES(IDXROLE) ON DELETE CASCADE,
    CONSTRAINT FK_ROLEUSER_ASSIGNEDBY FOREIGN KEY (ASSIGNEDBY) REFERENCES CORUSERS(IDXUSER) ON DELETE SET NULL,
    UNIQUE(USERID, ROLEID)
);

CREATE INDEX IF NOT EXISTS idx_corroleusers_user ON CORROLEUSERS(USERID);
CREATE INDEX IF NOT EXISTS idx_corroleusers_role ON CORROLEUSERS(ROLEID);

-- CORROLEPERMISSIONS (Roles-Permisos)
CREATE TABLE IF NOT EXISTS CORROLEPERMISSIONS (
    iduuid UUID UNIQUE DEFAULT gen_random_uuid(),
    IDXROLEPERMISSION BIGSERIAL PRIMARY KEY,
    ROLEID BIGINT NOT NULL,
    PERMISSIONID BIGINT NOT NULL,
    GRANTEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    GRANTEDBY BIGINT,
    CREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_ROLEPERM_ROLE FOREIGN KEY (ROLEID) REFERENCES CORROLES(IDXROLE) ON DELETE CASCADE,
    CONSTRAINT FK_ROLEPERM_PERM FOREIGN KEY (PERMISSIONID) REFERENCES CORPERMISSIONS(IDXPERMISSION) ON DELETE CASCADE,
    CONSTRAINT FK_ROLEPERM_GRANTEDBY FOREIGN KEY (GRANTEDBY) REFERENCES CORUSERS(IDXUSER) ON DELETE SET NULL,
    UNIQUE(ROLEID, PERMISSIONID)
);

CREATE INDEX IF NOT EXISTS idx_corrolepermissions_role ON CORROLEPERMISSIONS(ROLEID);
CREATE INDEX IF NOT EXISTS idx_corrolepermissions_perm ON CORROLEPERMISSIONS(PERMISSIONID);

-- CORMENUACCESS (Menús-Roles)
CREATE TABLE IF NOT EXISTS CORMENUACCESS (
    iduuid UUID UNIQUE DEFAULT gen_random_uuid(),
    IDXMENUACCESS BIGSERIAL PRIMARY KEY,
    MENUID BIGINT NOT NULL,
    ROLEID BIGINT NOT NULL,
    CANVIEW BOOLEAN NOT NULL DEFAULT TRUE,
    CREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_MENUACCESS_MENU FOREIGN KEY (MENUID) REFERENCES CORMENUS(IDXMENU) ON DELETE CASCADE,
    CONSTRAINT FK_MENUACCESS_ROLE FOREIGN KEY (ROLEID) REFERENCES CORROLES(IDXROLE) ON DELETE CASCADE,
    UNIQUE(MENUID, ROLEID)
);

CREATE INDEX IF NOT EXISTS idx_cormenuaccess_menu ON CORMENUACCESS(MENUID);
CREATE INDEX IF NOT EXISTS idx_cormenuaccess_role ON CORMENUACCESS(ROLEID);

-- ============================================================================
-- 9. Triggers para UPDATEDAT
-- ============================================================================

-- Function para actualizar UPDATEDAT
CREATE OR REPLACE FUNCTION update_updatedat_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.UPDATEDAT = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Triggers
DROP TRIGGER IF EXISTS update_cordepartments_updatedat ON CORDEPARTMENTS;
CREATE TRIGGER update_cordepartments_updatedat BEFORE UPDATE ON CORDEPARTMENTS FOR EACH ROW EXECUTE FUNCTION update_updatedat_column();

DROP TRIGGER IF EXISTS update_corroles_updatedat ON CORROLES;
CREATE TRIGGER update_corroles_updatedat BEFORE UPDATE ON CORROLES FOR EACH ROW EXECUTE FUNCTION update_updatedat_column();

DROP TRIGGER IF EXISTS update_corpermissions_updatedat ON CORPERMISSIONS;
CREATE TRIGGER update_corpermissions_updatedat BEFORE UPDATE ON CORPERMISSIONS FOR EACH ROW EXECUTE FUNCTION update_updatedat_column();

DROP TRIGGER IF EXISTS update_cormenus_updatedat ON CORMENUS;
CREATE TRIGGER update_cormenus_updatedat BEFORE UPDATE ON CORMENUS FOR EACH ROW EXECUTE FUNCTION update_updatedat_column();

DROP TRIGGER IF EXISTS update_corusers_updatedat ON CORUSERS;
CREATE TRIGGER update_corusers_updatedat BEFORE UPDATE ON CORUSERS FOR EACH ROW EXECUTE FUNCTION update_updatedat_column();

DROP TRIGGER IF EXISTS update_corroleusers_updatedat ON CORROLEUSERS;
CREATE TRIGGER update_corroleusers_updatedat BEFORE UPDATE ON CORROLEUSERS FOR EACH ROW EXECUTE FUNCTION update_updatedat_column();

DROP TRIGGER IF EXISTS update_corrolepermissions_updatedat ON CORROLEPERMISSIONS;
CREATE TRIGGER update_corrolepermissions_updatedat BEFORE UPDATE ON CORROLEPERMISSIONS FOR EACH ROW EXECUTE FUNCTION update_updatedat_column();

DROP TRIGGER IF EXISTS update_cormenuaccess_updatedat ON CORMENUACCESS;
CREATE TRIGGER update_cormenuaccess_updatedat BEFORE UPDATE ON CORMENUACCESS FOR EACH ROW EXECUTE FUNCTION update_updatedat_column();

-- ============================================================================
-- 10. Comentarios en Tablas y Columnas
-- ============================================================================

COMMENT ON TABLE CORDEPARTMENTS IS 'Departamentos de la organización';
COMMENT ON COLUMN CORDEPARTMENTS.CORNAME IS 'Nombre del departamento';
COMMENT ON COLUMN CORDEPARTMENTS.CODE IS 'Código único del departamento';
COMMENT ON COLUMN CORDEPARTMENTS.MANAGERID IS 'ID del gerente del departamento';
COMMENT ON COLUMN CORDEPARTMENTS.PARENTID IS 'ID del departamento padre';

COMMENT ON TABLE CORROLES IS 'Roles del sistema';
COMMENT ON COLUMN CORROLES.ROLENAME IS 'Nombre del rol';
COMMENT ON COLUMN CORROLES.ISSYSTEM IS 'Indica si es un rol del sistema (no eliminable)';

COMMENT ON TABLE CORPERMISSIONS IS 'Permisos del sistema';
COMMENT ON COLUMN CORPERMISSIONS.PERMISSIONNAME IS 'Nombre del permiso';
COMMENT ON COLUMN CORPERMISSIONS.RESOURCE IS 'Recurso al que aplica el permiso';
COMMENT ON COLUMN CORPERMISSIONS.ACTION IS 'Acción permitida (READ, WRITE, DELETE, etc.)';

COMMENT ON TABLE CORMENUS IS 'Menús del sistema';
COMMENT ON COLUMN CORMENUS.MENUNAME IS 'Nombre del menú';
COMMENT ON COLUMN CORMENUS.LABEL IS 'Etiqueta visible del menú';
COMMENT ON COLUMN CORMENUS.URL IS 'URL o ruta del menú';
COMMENT ON COLUMN CORMENUS.ORDINAL IS 'Orden de visualización';

COMMENT ON TABLE CORUSERS IS 'Usuarios del sistema';
COMMENT ON COLUMN CORUSERS.USERNAME IS 'Nombre de usuario único';
COMMENT ON COLUMN CORUSERS.EMAIL IS 'Correo electrónico único';
COMMENT ON COLUMN CORUSERS.FAILEDLOGINS IS 'Contador de intentos fallidos de login';
COMMENT ON COLUMN CORUSERS.ISLOCKED IS 'Indica si la cuenta está bloqueada';

COMMENT ON TABLE CORROLEUSERS IS 'Relación Many-to-Many entre Usuarios y Roles';
COMMENT ON TABLE CORROLEPERMISSIONS IS 'Relación Many-to-Many entre Roles y Permisos';
COMMENT ON TABLE CORMENUACCESS IS 'Control de acceso de Roles a Menús';

COMMIT;

-- ============================================================================
-- FIN DEL PATCH
-- ============================================================================
-- Para verificar la aplicación exitosa:
-- SELECT * FROM pg_attribute WHERE attrelid = 'CORDEPARTMENTS'::regclass;
-- SELECT * FROM pg_attribute WHERE attrelid = 'CORROLES'::regclass;
-- ============================================================================

