# PROMPT: INC-011-01 - Control de Acceso Basado en Roles para Prevenir Cambios por Partners

**Incidencia:** INC-011-01  
**Prioridad:** 🔴 CRÍTICA  
**Artículo EU AI Act:** Art. 12 (Registros Automáticos), Art. 19 (Registros Inalterables), ISO/IEC 27001 (RBAC)  
**Esfuerzo Estimado:** 4.5-7.5 días  
**Tipo:** Java - Backend + DBA

---

## CONTEXTO

El sistema actual permite que cualquier usuario (incluyendo partners externos) modifique versiones aprobadas de modelos, prompts o configuraciones RAG. Aunque existe trazabilidad mediante logs inmutables, **no existe control proactivo** que **prevenga** los cambios antes de que ocurran.

**Ubicación Actual:**
- No existe tabla `CORROLES` o sistema de roles
- No existe middleware de autorización en BusinessServices
- Las versiones con status `APPROVED` o `PRODUCTION` pueden ser modificadas directamente
- No hay diferenciación entre usuarios internos y partners en permisos

**Referencias:**
- `ModelVersionBusinessService.java` - métodos `createVersion()`, `updateVersion()`
- `PromptVersionBusinessService.java` - métodos similares
- `RagVersionBusinessService.java` - métodos similares
- Tablas: `MODMODELVERSIONS`, `PRMPROMPTVERSIONS`, `RAGRAGVERSIONS`

---

## REQUISITOS

1. **Crear sistema de roles** con tabla `CORROLES` y asignación usuario-rol
2. **Implementar workflow de aprobación** para versiones (tabla `VERSVERSIONAPPROVALS`)
3. **Modificar BusinessServices** para verificar roles antes de operaciones
4. **Bloquear UPDATE/DELETE** en versiones aprobadas mediante triggers PostgreSQL
5. **Forzar creación de nueva versión** en lugar de modificar versiones aprobadas

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear Tablas de Roles (DBA)

**Archivo:** `nocode.service.entitys/src/main/resources/sql/versionado_control_acceso_roles.sql`

```sql
-- ============================================================================
-- TABLA DE ROLES
-- ============================================================================
CREATE TABLE CORROLES (
    IDXROLE BIGSERIAL PRIMARY KEY,
    iduuid VARCHAR(36) UNIQUE NOT NULL DEFAULT gen_random_uuid()::text,
    CORROLECODE VARCHAR(50) NOT NULL UNIQUE,
    CORROLENAME VARCHAR(100) NOT NULL,
    CORDESCRIPTION TEXT,
    CORISINTERNAL BOOLEAN NOT NULL DEFAULT FALSE,
    CORPERMISSIONS JSONB, -- Permisos por entidad/acción: {"models":["CREATE","READ"],"prompts":["*"]}
    CORCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_role_code CHECK (CORROLECODE ~ '^[A-Z_]+$')
);

-- Índices
CREATE INDEX idx_cor_roles_code ON CORROLES(CORROLECODE);
CREATE INDEX idx_cor_roles_internal ON CORROLES(CORISINTERNAL);

-- ============================================================================
-- TABLA DE ASIGNACIÓN USUARIO-ROL
-- ============================================================================
CREATE TABLE CORUSERROLES (
    IDXUSERROLE BIGSERIAL PRIMARY KEY,
    iduuid VARCHAR(36) UNIQUE NOT NULL DEFAULT gen_random_uuid()::text,
    IDXUSER BIGINT NOT NULL REFERENCES CORUSERS(IDXUSER) ON DELETE CASCADE,
    IDXROLE BIGINT NOT NULL REFERENCES CORROLES(IDXROLE) ON DELETE CASCADE,
    CORPARTNERID BIGINT, -- Si es partner, referencia a partner (tabla PARTNERS si existe)
    CORCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_user_role UNIQUE (IDXUSER, IDXROLE)
);

-- Índices
CREATE INDEX idx_cor_user_roles_user ON CORUSERROLES(IDXUSER);
CREATE INDEX idx_cor_user_roles_role ON CORUSERROLES(IDXROLE);
CREATE INDEX idx_cor_user_roles_partner ON CORUSERROLES(CORPARTNERID);

-- ============================================================================
-- ROLES ESTÁNDAR
-- ============================================================================
INSERT INTO CORROLES (CORROLECODE, CORROLENAME, CORISINTERNAL, CORPERMISSIONS) VALUES
('INTERNAL_ADMIN', 'Administrador Interno', true, '{"*":["*"]}'::jsonb),
('PARTNER_READONLY', 'Partner Solo Lectura', false, '{"*":["READ"]}'::jsonb),
('PARTNER_EDITOR', 'Partner Editor', false, '{"models":["CREATE","READ"],"prompts":["CREATE","READ"],"rag":["CREATE","READ"],"datasets":["CREATE","READ"]}'::jsonb),
('PARTNER_APPROVER', 'Partner Aprobador', false, '{"models":["*"],"prompts":["*"],"rag":["*"],"datasets":["*"]}'::jsonb);

-- ============================================================================
-- TABLA DE APROBACIONES DE VERSIONES
-- ============================================================================
CREATE TABLE VERSVERSIONAPPROVALS (
    IDXVERSIONAPPROVAL BIGSERIAL PRIMARY KEY,
    iduuid VARCHAR(36) UNIQUE NOT NULL DEFAULT gen_random_uuid()::text,
    VERSENTITYTYPE VARCHAR(50) NOT NULL, -- MODEL, PROMPT, RAG, DATASET
    VERSENTITYID BIGINT NOT NULL,
    VERSVERSIONID BIGINT NOT NULL,
    VERSAPPROVALSTATUS VARCHAR(20) NOT NULL, -- PENDING, APPROVED, REJECTED
    VERSAPPROVEDBY BIGINT REFERENCES CORUSERS(IDXUSER),
    VERSAPPROVALDATE TIMESTAMP,
    VERSAPPROVALNOTES TEXT,
    VERSREJECTIONREASON TEXT,
    CORCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CORUPDATEDAT TIMESTAMP,
    CONSTRAINT chk_approval_status CHECK (VERSAPPROVALSTATUS IN ('PENDING', 'APPROVED', 'REJECTED'))
);

-- Índices
CREATE INDEX idx_vers_approvals_entity ON VERSVERSIONAPPROVALS(VERSENTITYTYPE, VERSENTITYID);
CREATE INDEX idx_vers_approvals_version ON VERSVERSIONAPPROVALS(VERSVERSIONID);
CREATE INDEX idx_vers_approvals_status ON VERSVERSIONAPPROVALS(VERSAPPROVALSTATUS);
CREATE INDEX idx_vers_approvals_approver ON VERSVERSIONAPPROVALS(VERSAPPROVEDBY);
```

### 2. Crear Entidades JPA

**Archivo:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/core/Role.java`

```java
package com.codeflowx.govern.entity.core;

import com.codeflowx.govern.entity.base.BaseEntity;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "CORROLES")
public class Role implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXROLE", nullable = false)
    private Long idxrole;
    
    @Column(name = "iduuid", unique = true, nullable = false, length = 36)
    private String iduuid;
    
    @Column(name = "CORROLECODE", nullable = false, length = 50, unique = true)
    private String corrolecode;
    
    @Column(name = "CORROLENAME", nullable = false, length = 100)
    private String corrolename;
    
    @Column(name = "CORDESCRIPTION", columnDefinition = "TEXT")
    private String cordescription;
    
    @Column(name = "CORISINTERNAL", nullable = false)
    private Boolean corisinternal = false;
    
    @Column(name = "CORPERMISSIONS", columnDefinition = "JSONB")
    private String corpermissions; // JSON string
    
    @Column(name = "CORCREATEDAT", nullable = false)
    private Timestamp corcreatedat;
    
    // Getters y Setters
    // ...
}
```

**Archivo:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/core/UserRole.java`

```java
package com.codeflowx.govern.entity.core;

import com.codeflowx.govern.entity.users.User;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "CORUSERROLES")
public class UserRole implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXUSERROLE", nullable = false)
    private Long idxuserrole;
    
    @Column(name = "iduuid", unique = true, nullable = false, length = 36)
    private String iduuid;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDXUSER", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDXROLE", nullable = false)
    private Role role;
    
    @Column(name = "CORPARTNERID")
    private Long corpartnerid;
    
    @Column(name = "CORCREATEDAT", nullable = false)
    private Timestamp corcreatedat;
    
    // Getters y Setters
    // ...
}
```

**Archivo:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/versioning/VersionApproval.java`

```java
package com.codeflowx.govern.entity.versioning;

import com.codeflowx.govern.entity.users.User;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "VERSVERSIONAPPROVALS")
public class VersionApproval implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXVERSIONAPPROVAL", nullable = false)
    private Long idxversionapproval;
    
    @Column(name = "iduuid", unique = true, nullable = false, length = 36)
    private String iduuid;
    
    @Column(name = "VERSENTITYTYPE", nullable = false, length = 50)
    private String versentitytype; // MODEL, PROMPT, RAG, DATASET
    
    @Column(name = "VERSENTITYID", nullable = false)
    private Long versentityid;
    
    @Column(name = "VERSVERSIONID", nullable = false)
    private Long versversionid;
    
    @Column(name = "VERSAPPROVALSTATUS", nullable = false, length = 20)
    private String versapprovalstatus; // PENDING, APPROVED, REJECTED
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VERSAPPROVEDBY")
    private User versapprovedby;
    
    @Column(name = "VERSAPPROVALDATE")
    private Timestamp versapprovaldate;
    
    @Column(name = "VERSAPPROVALNOTES", columnDefinition = "TEXT")
    private String versapprovalnotes;
    
    @Column(name = "VERSREJECTIONREASON", columnDefinition = "TEXT")
    private String versrejectionreason;
    
    @Column(name = "CORCREATEDAT", nullable = false)
    private Timestamp corcreatedat;
    
    @Column(name = "CORUPDATEDAT")
    private Timestamp corupdatedat;
    
    // Getters y Setters
    // ...
}
```

### 3. Crear Service de Roles

**Archivo:** `nocode.service.business/src/main/java/com/codeflowx/govern/business/core/RoleService.java`

```java
package com.codeflowx.govern.business.core;

import com.codeflowx.govern.entity.core.Role;
import com.codeflowx.govern.entity.core.UserRole;
import com.codeflowx.govern.dao.core.RoleDao;
import com.codeflowx.govern.dao.core.UserRoleDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {
    
    @Autowired
    private RoleDao roleDao;
    
    @Autowired
    private UserRoleDao userRoleDao;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Verifica si un usuario tiene un permiso específico
     * @param userId ID del usuario
     * @param entityType Tipo de entidad (models, prompts, rag, datasets)
     * @param action Acción (CREATE, READ, UPDATE, DELETE, APPROVE, UPDATE_APPROVED, *)
     * @return true si tiene permiso
     */
    public boolean hasPermission(Long userId, String entityType, String action) {
        List<UserRole> userRoles = userRoleDao.findByUserId(userId);
        
        for (UserRole userRole : userRoles) {
            Role role = userRole.getRole();
            String permissionsJson = role.getCorpermissions();
            
            if (permissionsJson == null) {
                continue;
            }
            
            try {
                JsonNode permissions = objectMapper.readTree(permissionsJson);
                
                // Verificar permiso global (*)
                JsonNode globalPerms = permissions.get("*");
                if (globalPerms != null && globalPerms.isArray()) {
                    for (JsonNode perm : globalPerms) {
                        if (perm.asText().equals("*") || perm.asText().equals(action)) {
                            return true;
                        }
                    }
                }
                
                // Verificar permiso específico de entidad
                JsonNode entityPerms = permissions.get(entityType);
                if (entityPerms != null && entityPerms.isArray()) {
                    for (JsonNode perm : entityPerms) {
                        if (perm.asText().equals("*") || perm.asText().equals(action)) {
                            return true;
                        }
                    }
                }
                
            } catch (Exception e) {
                // Log error y continuar con siguiente rol
                continue;
            }
        }
        
        return false;
    }
    
    /**
     * Verifica si un usuario tiene un rol específico
     */
    public boolean hasRole(Long userId, String roleCode) {
        List<UserRole> userRoles = userRoleDao.findByUserId(userId);
        return userRoles.stream()
            .anyMatch(ur -> ur.getRole().getCorrolecode().equals(roleCode));
    }
    
    /**
     * Verifica si un usuario es interno (no partner)
     */
    public boolean isInternalUser(Long userId) {
        List<UserRole> userRoles = userRoleDao.findByUserId(userId);
        return userRoles.stream()
            .anyMatch(ur -> ur.getRole().getCorisinternal());
    }
    
    /**
     * Obtiene todos los roles de un usuario
     */
    public List<String> getUserRoleCodes(Long userId) {
        List<UserRole> userRoles = userRoleDao.findByUserId(userId);
        return userRoles.stream()
            .map(ur -> ur.getRole().getCorrolecode())
            .collect(Collectors.toList());
    }
}
```

### 4. Modificar ModelVersionBusinessService

**Archivo:** `nocode.service.business/src/main/java/com/codeflowx/govern/business/models/ModelVersionBusinessService.java`

```java
package com.codeflowx.govern.business.models;

import com.codeflowx.govern.business.core.RoleService;
import com.codeflowx.govern.business.logging.ImmutableLoggingBusinessService;
import com.codeflowx.govern.entity.models.ModelVersion;
import com.codeflowx.govern.entity.versioning.VersionApproval;
import com.codeflowx.govern.dao.models.ModelVersionDao;
import com.codeflowx.govern.dao.versioning.VersionApprovalDao;
import com.codeflowx.govern.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@Transactional
public class ModelVersionBusinessService {
    
    @Autowired
    private ModelVersionDao modelVersionDao;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private ImmutableLoggingBusinessService loggingService;
    
    @Autowired
    private VersionApprovalDao versionApprovalDao;
    
    /**
     * Crea una nueva versión de modelo
     * Requisito: Usuario debe tener rol PARTNER_EDITOR o INTERNAL_ADMIN
     */
    public ModelVersion createVersion(ModelVersion version, Long userId) {
        // Verificar permisos
        if (!roleService.hasPermission(userId, "models", "CREATE")) {
            throw new UnauthorizedException(
                "Usuario no tiene permisos para crear versiones de modelos. " +
                "Se requiere rol PARTNER_EDITOR o INTERNAL_ADMIN."
            );
        }
        
        // Crear versión
        version.setModstatus("DRAFT");
        version.setModcreatedby(getUserName(userId));
        version.setModcreatedat(new Timestamp(System.currentTimeMillis()));
        
        ModelVersion created = modelVersionDao.save(version);
        
        // Log inmutable
        loggingService.logChange(
            "MODEL",
            created.getIdxmodelversion(),
            "CREATE",
            userId,
            getUserName(userId),
            created
        );
        
        return created;
    }
    
    /**
     * Actualiza una versión de modelo
     * Requisito: No se pueden modificar versiones aprobadas (crear nueva versión)
     */
    public ModelVersion updateVersion(Long versionId, ModelVersion updates, Long userId) {
        ModelVersion existing = modelVersionDao.findById(versionId)
            .orElseThrow(() -> new IllegalArgumentException("Versión no encontrada: " + versionId));
        
        // Verificar que versión NO está aprobada o en producción
        if (existing.getModstatus().equals("APPROVED") || existing.getModstatus().equals("PRODUCTION")) {
            // Solo INTERNAL_ADMIN o PARTNER_APPROVER pueden modificar versiones aprobadas
            if (!roleService.hasPermission(userId, "models", "UPDATE_APPROVED")) {
                throw new UnauthorizedException(
                    "No se pueden modificar versiones aprobadas o en producción. " +
                    "Por favor, cree una nueva versión con MODPARENTVERSION = " + versionId
                );
            }
        }
        
        // Verificar permisos básicos
        if (!roleService.hasPermission(userId, "models", "UPDATE")) {
            throw new UnauthorizedException(
                "Usuario no tiene permisos para modificar versiones de modelos. " +
                "Se requiere rol PARTNER_EDITOR o INTERNAL_ADMIN."
            );
        }
        
        // Actualizar campos permitidos
        if (updates.getModdescription() != null) {
            existing.setModdescription(updates.getModdescription());
        }
        if (updates.getModperformancemetrics() != null) {
            existing.setModperformancemetrics(updates.getModperformancemetrics());
        }
        // No permitir cambiar MODSTATUS directamente (usar approveVersion)
        
        existing.setModupdatedby(getUserName(userId));
        existing.setModupdatedat(new Timestamp(System.currentTimeMillis()));
        
        ModelVersion updated = modelVersionDao.save(existing);
        
        // Log inmutable
        loggingService.logChange(
            "MODEL",
            versionId,
            "UPDATE",
            userId,
            getUserName(userId),
            updated
        );
        
        return updated;
    }
    
    /**
     * Aprueba una versión de modelo
     * Requisito: Usuario debe tener rol PARTNER_APPROVER o INTERNAL_ADMIN
     */
    public void approveVersion(Long versionId, Long approverId, String notes) {
        // Verificar permisos
        if (!roleService.hasPermission(approverId, "models", "APPROVE")) {
            throw new UnauthorizedException(
                "Usuario no tiene permisos para aprobar versiones. " +
                "Se requiere rol PARTNER_APPROVER o INTERNAL_ADMIN."
            );
        }
        
        ModelVersion version = modelVersionDao.findById(versionId)
            .orElseThrow(() -> new IllegalArgumentException("Versión no encontrada: " + versionId));
        
        // Cambiar status
        version.setModstatus("APPROVED");
        version.setModupdatedby(getUserName(approverId));
        version.setModupdatedat(new Timestamp(System.currentTimeMillis()));
        modelVersionDao.save(version);
        
        // Crear registro de aprobación
        VersionApproval approval = new VersionApproval();
        approval.setIduuid(java.util.UUID.randomUUID().toString());
        approval.setVersentitytype("MODEL");
        approval.setVersentityid(version.getModel().getIdxmodel());
        approval.setVersversionid(versionId);
        approval.setVersapprovalstatus("APPROVED");
        approval.setVersapprovedby(getUser(approverId));
        approval.setVersapprovaldate(new Timestamp(System.currentTimeMillis()));
        approval.setVersapprovalnotes(notes);
        approval.setCorcreatedat(new Timestamp(System.currentTimeMillis()));
        versionApprovalDao.save(approval);
        
        // Log inmutable
        loggingService.logChange(
            "MODEL",
            versionId,
            "APPROVE",
            approverId,
            getUserName(approverId),
            version
        );
    }
    
    // Métodos auxiliares
    private String getUserName(Long userId) {
        // TODO: Implementar obtención de nombre de usuario
        return "USER_" + userId;
    }
    
    private com.codeflowx.govern.entity.users.User getUser(Long userId) {
        // TODO: Implementar obtención de usuario
        return null;
    }
}
```

### 5. Crear Triggers PostgreSQL para Bloquear Updates

**Archivo:** `nocode.service.entitys/src/main/resources/sql/versionado_triggers_prevent_update.sql`

```sql
-- ============================================================================
-- FUNCIÓN: Prevenir UPDATE en versiones aprobadas (salvo admins)
-- ============================================================================
CREATE OR REPLACE FUNCTION prevent_approved_version_update()
RETURNS TRIGGER AS $$
DECLARE
    user_role_code VARCHAR(50);
    user_roles TEXT[];
BEGIN
    -- Obtener roles del usuario desde contexto de sesión
    -- Se asume que se establece con: SET LOCAL app.user_roles = '["INTERNAL_ADMIN"]';
    user_roles := string_to_array(
        COALESCE(
            current_setting('app.user_roles', true),
            '["PARTNER_READONLY"]'
        ),
        ','
    );
    
    -- Si la versión está aprobada y usuario NO es admin/approver, forzar creación de nueva versión
    IF OLD.modstatus IN ('APPROVED', 'PRODUCTION') THEN
        -- Verificar si usuario tiene rol INTERNAL_ADMIN o PARTNER_APPROVER
        IF NOT (
            'INTERNAL_ADMIN' = ANY(user_roles) OR 
            'PARTNER_APPROVER' = ANY(user_roles)
        ) THEN
            RAISE EXCEPTION 
                'No se pueden modificar versiones aprobadas. Crear nueva versión con MODPARENTVERSION = %', 
                OLD.idxmodelversion;
        END IF;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- ============================================================================
-- TRIGGER: ModelVersions
-- ============================================================================
CREATE TRIGGER trg_prevent_approved_model_version_update
BEFORE UPDATE ON MODMODELVERSIONS
FOR EACH ROW
WHEN (OLD.modstatus IN ('APPROVED', 'PRODUCTION'))
EXECUTE FUNCTION prevent_approved_version_update();

-- ============================================================================
-- TRIGGER: PromptVersions
-- ============================================================================
CREATE TRIGGER trg_prevent_approved_prompt_version_update
BEFORE UPDATE ON PRMPROMPTVERSIONS
FOR EACH ROW
WHEN (OLD.prmstatus IN ('APPROVED', 'PRODUCTION'))
EXECUTE FUNCTION prevent_approved_version_update();

-- ============================================================================
-- TRIGGER: RagVersions
-- ============================================================================
CREATE TRIGGER trg_prevent_approved_rag_version_update
BEFORE UPDATE ON RAGRAGVERSIONS
FOR EACH ROW
WHEN (OLD.ragvstatus IN ('APPROVED', 'PRODUCTION'))
EXECUTE FUNCTION prevent_approved_version_update();
```

### 6. Crear Exception Personalizada

**Archivo:** `nocode.service.business/src/main/java/com/codeflowx/govern/exception/UnauthorizedException.java`

```java
package com.codeflowx.govern.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
    
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

---

## VALIDACIONES ADICIONALES RECOMENDADAS

1. **Middleware de autorización** en endpoints REST para verificar roles antes de procesar requests
2. **Notificaciones automáticas** cuando partner crea nueva versión (notificar a aprobadores)
3. **Dashboard de aprobaciones pendientes** para aprobadores
4. **Historial de aprobaciones** visible en UI

---

## PRUEBAS REQUERIDAS

1. **Test 1:** Usuario PARTNER_READONLY intenta crear versión → Debe bloquear
2. **Test 2:** Usuario PARTNER_EDITOR crea versión DRAFT → Debe permitir
3. **Test 3:** Usuario PARTNER_EDITOR intenta modificar versión APPROVED → Debe bloquear
4. **Test 4:** Usuario INTERNAL_ADMIN modifica versión APPROVED → Debe permitir
5. **Test 5:** Usuario PARTNER_APPROVER aprueba versión → Debe permitir y crear registro en VERSVERSIONAPPROVALS
6. **Test 6:** Trigger PostgreSQL bloquea UPDATE directo en versión APPROVED → Debe lanzar excepción

---

## LOGS INMUTABLES

Añadir logs inmutables en todas las operaciones:

```java
// En createVersion()
loggingService.logChange(
    "MODEL",
    created.getIdxmodelversion(),
    "CREATE",
    userId,
    getUserName(userId),
    Map.of(
        "role", roleService.getUserRoleCodes(userId).toString(),
        "status", "DRAFT"
    )
);

// En approveVersion()
loggingService.logChange(
    "MODEL",
    versionId,
    "APPROVE",
    approverId,
    getUserName(approverId),
    Map.of(
        "approval_notes", notes,
        "previous_status", version.getModstatus()
    )
);
```

---

## REFERENCIAS

- **Art. 12 EU AI Act:** Registros Automáticos
- **Art. 19 EU AI Act:** Registros Inalterables
- **ISO/IEC 27001:** Control de acceso basado en roles (RBAC)
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_VERSIONADO.md#inc-011-01`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_011_VERSIONADO_CONTROL_CAMBIOS.md`

---

## NOTAS DE IMPLEMENTACIÓN

- Ajustar método `getUserName()` y `getUser()` según implementación real de autenticación
- Considerar usar Spring Security para integración con sistema de autenticación existente
- Los triggers PostgreSQL requieren establecer contexto de sesión con roles del usuario antes de ejecutar UPDATE
- Considerar implementar caché de roles en `RoleService` para mejorar rendimiento
- Aplicar misma lógica a `PromptVersionBusinessService` y `RagVersionBusinessService`

---

**Estado:** ✅ COMPLETADO  
**Esfuerzo Estimado:** 4.5-7.5 días  
**Responsable:** Backend Team + DBA Team

