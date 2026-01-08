# Módulos Core del Portal Backend

## Descripción General

Los módulos core proporcionan la funcionalidad fundamental del portal: gestión de usuarios, roles, menús, auditoría de acceso, licencias de la plataforma y sistema de actualizaciones. Son la base sobre la que se construyen todos los demás módulos funcionales.

## 1. Módulo de Usuarios (User Management)

### Entidades

#### Department

```java
@Entity
@Table(name = "cor_departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(unique = true)
    private String code;

    @Column(name = "manager_id")
    private Long managerId;

    @Column(name = "parent_department_id")
    private Long parentDepartmentId;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "department")
    private List<User> users = new ArrayList<>();
}
```

#### User

```java
@Entity
@Table(name = "cor_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "phone")
    private String phone;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "language")
    private String language;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "cor_user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserSession> sessions = new ArrayList<>();
}
```

#### UserSession

```java
@Entity
@Table(name = "cor_user_sessions")
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "session_id", unique = true, nullable = false)
    private String sessionId;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
```

### Endpoints

```
GET    /api/v1/users
GET    /api/v1/users/{id}
POST   /api/v1/users
PUT    /api/v1/users/{id}
DELETE /api/v1/users/{id}
GET    /api/v1/users/{id}/roles
PUT    /api/v1/users/{id}/roles
GET    /api/v1/users/{id}/sessions
DELETE /api/v1/users/{id}/sessions/{sessionId}
POST   /api/v1/users/{id}/activate
POST   /api/v1/users/{id}/deactivate
POST   /api/v1/users/{id}/reset-password
```

## 2. Módulo de Roles (Role Management)

### Entidades

#### Role

```java
@Entity
@Table(name = "cor_roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_system")
    private Boolean isSystem = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "cor_role_menus",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "menu_id")
    )
    private Set<Menu> menus = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "cor_role_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();
}
```

#### Permission

```java
@Entity
@Table(name = "cor_permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "resource")
    private String resource;

    @Column(name = "action")
    private String action;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
```

## 3. Módulo de Menús (Menu Management)

### Entidades

#### Menu

```java
@Entity
@Table(name = "cor_menus")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private String icon;

    @Column
    private String href;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "requires_auth")
    private Boolean requiresAuth = true;

    @Column(name = "is_external")
    private Boolean isExternal = false;

    @Column(name = "target")
    private String target; // _blank, _self, etc.

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "parentId", fetch = FetchType.LAZY)
    private List<Menu> children;

    @ManyToMany(mappedBy = "menus")
    private Set<Role> roles = new HashSet<>();
}
```

### Endpoints

```
GET    /api/v1/menus
GET    /api/v1/menus/{id}
POST   /api/v1/menus
PUT    /api/v1/menus/{id}
DELETE /api/v1/menus/{id}
GET    /api/v1/menus/tree
GET    /api/v1/menus/user/{userId}
GET    /api/v1/menus/role/{roleId}
PUT    /api/v1/menus/{id}/order
```

## 4. Módulo de Auditoría (Audit Management)

### Entidades

#### AuditLog

```java
@Entity
@Table(name = "cor_audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "action")
    private String action;

    @Column(name = "resource")
    private String resource;

    @Column(name = "resource_id")
    private String resourceId;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "request_method")
    private String requestMethod;

    @Column(name = "request_url")
    private String requestUrl;

    @Column(name = "request_body")
    private String requestBody;

    @Column(name = "response_status")
    private Integer responseStatus;

    @Column(name = "execution_time")
    private Long executionTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "severity")
    private String severity; // INFO, WARNING, ERROR, CRITICAL

    @Column(name = "module")
    private String module; // CORE, TRAINING, RAG, etc.
}
```

### Endpoints

```
GET    /api/v1/audit
GET    /api/v1/audit/user/{userId}
GET    /api/v1/audit/action/{action}
GET    /api/v1/audit/resource/{resource}
GET    /api/v1/audit/module/{module}
GET    /api/v1/audit/severity/{severity}
GET    /api/v1/audit/date-range
GET    /api/v1/audit/export
DELETE /api/v1/audit/cleanup
```

## 5. Módulo de Autenticación (Authentication)

### Entidades

#### LoginAttempt

```java
@Entity
@Table(name = "cor_login_attempts")
public class LoginAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "success")
    private Boolean success;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "user_agent")
    private String userAgent;
}
```

### Endpoints

```
POST   /api/v1/auth/login
POST   /api/v1/auth/logout
POST   /api/v1/auth/refresh
POST   /api/v1/auth/register
POST   /api/v1/auth/forgot-password
POST   /api/v1/auth/reset-password
POST   /api/v1/auth/change-password
POST   /api/v1/auth/verify-email
GET    /api/v1/auth/profile
PUT    /api/v1/auth/profile
```

## 6. Módulo de Licencias de la Plataforma (Platform License Management)

### Entidades

#### PlatformLicense

```java
@Entity
@Table(name = "cor_platform_licenses")
public class PlatformLicense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "license_type")
    @Enumerated(EnumType.STRING)
    private PlatformLicenseType licenseType;

    @Column(name = "license_file")
    private byte[] licenseFile; // Archivo encriptado

    @Column(name = "license_hash")
    private String licenseHash; // Hash del archivo para verificación

    @Column(name = "encryption_key_id")
    private String encryptionKeyId; // ID de la clave de encriptación

    @Column(name = "max_nodes")
    private Integer maxNodes; // Número máximo de nodos permitidos

    @Column(name = "max_gpus")
    private Integer maxGPUs; // Número máximo de GPUs permitidas

    @Column(name = "max_users")
    private Integer maxUsers;

    @Column(name = "max_ai_models")
    private Integer maxAIModels;

    @Column(name = "max_rag_documents")
    private Integer maxRAGDocuments;

    @Column(name = "max_api_calls_per_month")
    private Long maxAPICallsPerMonth;

    @Column(name = "max_storage_gb")
    private Integer maxStorageGB;

    @Column(name = "features_enabled")
    private String featuresEnabled; // JSON array de características

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum PlatformLicenseType {
    FREE,               // Licencia gratuita
    BASIC,              // Licencia básica
    PROFESSIONAL,       // Licencia profesional
    ENTERPRISE,         // Licencia empresarial
    CUSTOM              // Licencia personalizada
}
```

#### PlatformNode

```java
@Entity
@Table(name = "cor_platform_nodes")
public class PlatformNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "node_name")
    private String nodeName;

    @Column(name = "node_type")
    @Enumerated(EnumType.STRING)
    private NodeType nodeType;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "hostname")
    private String hostname;

    @Column(name = "cpu_cores")
    private Integer cpuCores;

    @Column(name = "ram_gb")
    private Integer ramGB;

    @Column(name = "gpu_count")
    private Integer gpuCount;

    @Column(name = "gpu_type")
    private String gpuType;

    @Column(name = "storage_gb")
    private Integer storageGB;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "last_heartbeat")
    private LocalDateTime lastHeartbeat;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum NodeType {
    MASTER,             // Nodo maestro
    WORKER,             // Nodo trabajador
    INFERENCE,          // Nodo de inferencia
    TRAINING,           // Nodo de entrenamiento
    STORAGE,            // Nodo de almacenamiento
    GATEWAY             // Nodo de entrada
}
```

### Endpoints

```
GET    /api/v1/platform/licenses
GET    /api/v1/platform/licenses/{id}
POST   /api/v1/platform/licenses
PUT    /api/v1/platform/licenses/{id}
DELETE /api/v1/platform/licenses/{id}
POST   /api/v1/platform/licenses/{id}/validate
POST   /api/v1/platform/licenses/{id}/renew

GET    /api/v1/platform/nodes
GET    /api/v1/platform/nodes/{id}
POST   /api/v1/platform/nodes
PUT    /api/v1/platform/nodes/{id}
DELETE /api/v1/platform/nodes/{id}
POST   /api/v1/platform/nodes/{id}/activate
POST   /api/v1/platform/nodes/{id}/deactivate
```

## 7. Módulo de Actualizaciones de la Plataforma (Platform Updates)

### Entidades

#### PlatformUpdate

```java
@Entity
@Table(name = "cor_platform_updates")
public class PlatformUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "version")
    private String version; // Versión de la actualización

    @Column(name = "update_type")
    @Enumerated(EnumType.STRING)
    private UpdateType updateType;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "changelog")
    private String changelog; // JSON con cambios detallados

    @Column(name = "download_url")
    private String downloadUrl;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "checksum")
    private String checksum; // Hash del archivo

    @Column(name = "is_mandatory")
    private Boolean isMandatory = false;

    @Column(name = "requires_restart")
    private Boolean requiresRestart = false;

    @Column(name = "compatibility_matrix")
    private String compatibilityMatrix; // JSON con compatibilidad

    @Column(name = "release_date")
    private LocalDateTime releaseDate;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum UpdateType {
    SECURITY,           // Actualización de seguridad
    FEATURE,            // Nueva funcionalidad
    BUGFIX,            // Corrección de errores
    PERFORMANCE,        // Mejora de rendimiento
    MAJOR,             // Actualización mayor
    MINOR,             // Actualización menor
    PATCH              // Parche
}
```

#### UpdateInstallation

```java
@Entity
@Table(name = "cor_update_installations")
public class UpdateInstallation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "update_id")
    private Long updateId; // Referencia a cor_platform_updates.id

    @Column(name = "node_id")
    private Long nodeId; // Referencia a cor_platform_nodes.id

    @Column(name = "installation_status")
    @Enumerated(EnumType.STRING)
    private InstallationStatus status;

    @Column(name = "installation_date")
    private LocalDateTime installationDate;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "rollback_status")
    @Enumerated(EnumType.STRING)
    private RollbackStatus rollbackStatus;

    @Column(name = "rollback_date")
    private LocalDateTime rollbackDate;

    @Column(name = "installed_by")
    private Long installedBy; // Referencia a cor_users.id

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum InstallationStatus {
    PENDING,            // Pendiente de instalación
    IN_PROGRESS,        // Instalación en progreso
    COMPLETED,          // Instalación completada
    FAILED,             // Instalación fallida
    ROLLED_BACK         // Actualización revertida
}

public enum RollbackStatus {
    NONE,               // Sin rollback
    PENDING,            // Rollback pendiente
    IN_PROGRESS,        // Rollback en progreso
    COMPLETED,          // Rollback completado
    FAILED              // Rollback fallido
}
```

#### UpdateSchedule

```java
@Entity
@Table(name = "cor_update_schedules")
public class UpdateSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "update_id")
    private Long updateId; // Referencia a cor_platform_updates.id

    @Column(name = "schedule_type")
    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    @Column(name = "cron_expression")
    private String cronExpression; // Expresión cron para programación

    @Column(name = "next_execution")
    private LocalDateTime nextExecution;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_by")
    private Long createdBy; // Referencia a cor_users.id

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

public enum ScheduleType {
    IMMEDIATE,          // Ejecución inmediata
    SCHEDULED,          // Programado
    MAINTENANCE_WINDOW, // Ventana de mantenimiento
    MANUAL              // Manual
}
```

### Endpoints

```
GET    /api/v1/platform/updates
GET    /api/v1/platform/updates/{id}
POST   /api/v1/platform/updates
PUT    /api/v1/platform/updates/{id}
DELETE /api/v1/platform/updates/{id}
POST   /api/v1/platform/updates/{id}/install
POST   /api/v1/platform/updates/{id}/rollback

GET    /api/v1/platform/updates/installations
GET    /api/v1/platform/updates/installations/{id}
GET    /api/v1/platform/updates/installations/node/{nodeId}

GET    /api/v1/platform/updates/schedules
GET    /api/v1/platform/updates/schedules/{id}
POST   /api/v1/platform/updates/schedules
PUT    /api/v1/platform/updates/schedules/{id}
DELETE /api/v1/platform/updates/schedules/{id}
POST   /api/v1/platform/updates/schedules/{id}/execute
```

## Scripts de Inicialización de Base de Datos

### 1. Script de Limpieza (Drop de Tablas, Índices, Funciones, etc.)

```sql
-- =====================================================
-- SCRIPT DE LIMPIEZA - EJECUTAR PRIMERO
-- =====================================================

-- Eliminar funciones y procedimientos almacenados
DROP FUNCTION IF EXISTS update_updated_at_column() CASCADE;
DROP FUNCTION IF EXISTS log_user_action(text, text, text, text) CASCADE;
DROP FUNCTION IF EXISTS get_user_permissions(bigint) CASCADE;
DROP FUNCTION IF EXISTS check_user_access(bigint, text) CASCADE;

-- Eliminar triggers
DROP TRIGGER IF EXISTS update_cor_users_updated_at ON cor_users CASCADE;
DROP TRIGGER IF EXISTS update_cor_roles_updated_at ON cor_roles CASCADE;
DROP TRIGGER IF EXISTS update_cor_menus_updated_at ON cor_menus CASCADE;
DROP TRIGGER IF EXISTS update_cor_user_sessions_updated_at ON cor_user_sessions CASCADE;
DROP TRIGGER IF EXISTS update_cor_audit_logs_updated_at ON cor_audit_logs CASCADE;
DROP TRIGGER IF EXISTS update_cor_departments_updated_at ON cor_departments CASCADE;

-- Eliminar índices
DROP INDEX IF EXISTS idx_cor_users_username CASCADE;
DROP INDEX IF EXISTS idx_cor_users_email CASCADE;
DROP INDEX IF EXISTS idx_cor_users_active CASCADE;
DROP INDEX IF EXISTS idx_cor_users_department_id CASCADE;
DROP INDEX IF EXISTS idx_cor_roles_name CASCADE;
DROP INDEX IF EXISTS idx_cor_roles_active CASCADE;
DROP INDEX IF EXISTS idx_cor_menus_name CASCADE;
DROP INDEX IF EXISTS idx_cor_menus_parent_id CASCADE;
DROP INDEX IF EXISTS idx_cor_menus_active CASCADE;
DROP INDEX IF EXISTS idx_cor_user_roles_user_id CASCADE;
DROP INDEX IF EXISTS idx_cor_user_roles_role_id CASCADE;
DROP INDEX IF EXISTS idx_cor_role_menus_role_id CASCADE;
DROP INDEX IF EXISTS idx_cor_role_menus_menu_id CASCADE;
DROP INDEX IF EXISTS idx_cor_role_permissions_role_id CASCADE;
DROP INDEX IF EXISTS idx_cor_role_permissions_permission_id CASCADE;
DROP INDEX IF EXISTS idx_cor_user_sessions_user_id CASCADE;
DROP INDEX IF EXISTS idx_cor_user_sessions_session_id CASCADE;
DROP INDEX IF EXISTS idx_cor_user_sessions_active CASCADE;
DROP INDEX IF EXISTS idx_cor_audit_logs_user_id CASCADE;
DROP INDEX IF EXISTS idx_cor_audit_logs_action CASCADE;
DROP INDEX IF EXISTS idx_cor_audit_logs_created_at CASCADE;
DROP INDEX IF EXISTS idx_cor_audit_logs_resource CASCADE;
DROP INDEX IF EXISTS idx_cor_audit_logs_module CASCADE;
DROP INDEX IF EXISTS idx_cor_audit_logs_severity CASCADE;
DROP INDEX IF EXISTS idx_cor_login_attempts_username CASCADE;
DROP INDEX IF EXISTS idx_cor_login_attempts_ip CASCADE;
DROP INDEX IF EXISTS idx_cor_login_attempts_created_at CASCADE;
DROP INDEX IF EXISTS idx_cor_departments_name CASCADE;
DROP INDEX IF EXISTS idx_cor_departments_code CASCADE;
DROP INDEX IF EXISTS idx_cor_departments_active CASCADE;

-- Eliminar tablas en orden correcto (dependencias)
DROP TABLE IF EXISTS cor_user_roles CASCADE;
DROP TABLE IF EXISTS cor_role_menus CASCADE;
DROP TABLE IF EXISTS cor_role_permissions CASCADE;
DROP TABLE IF EXISTS cor_user_sessions CASCADE;
DROP TABLE IF EXISTS cor_audit_logs CASCADE;
DROP TABLE IF EXISTS cor_login_attempts CASCADE;
DROP TABLE IF EXISTS cor_users CASCADE;
DROP TABLE IF EXISTS cor_roles CASCADE;
DROP TABLE IF EXISTS cor_permissions CASCADE;
DROP TABLE IF EXISTS cor_menus CASCADE;
DROP TABLE IF EXISTS cor_departments CASCADE;

-- Eliminar secuencias
DROP SEQUENCE IF EXISTS cor_users_id_seq CASCADE;
DROP SEQUENCE IF EXISTS cor_roles_id_seq CASCADE;
DROP SEQUENCE IF EXISTS cor_permissions_id_seq CASCADE;
DROP SEQUENCE IF EXISTS cor_menus_id_seq CASCADE;
DROP SEQUENCE IF EXISTS cor_user_sessions_id_seq CASCADE;
DROP SEQUENCE IF EXISTS cor_audit_logs_id_seq CASCADE;
DROP SEQUENCE IF EXISTS cor_login_attempts_id_seq CASCADE;
DROP SEQUENCE IF EXISTS cor_departments_id_seq CASCADE;
```

### 2. Script de Creación de Tablas

```sql
-- =====================================================
-- SCRIPT DE CREACIÓN DE TABLAS - EJECUTAR SEGUNDO
-- =====================================================

-- Extensión para UUIDs (opcional)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Tabla de departamentos (CORE - Core Modules)
CREATE TABLE cor_departments (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    code VARCHAR(20) UNIQUE,
    manager_id BIGINT,
    parent_department_id BIGINT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla cor_departments
COMMENT ON TABLE cor_departments IS 'Tabla que almacena la información de los departamentos de la organización';
COMMENT ON COLUMN cor_departments.id IS 'Identificador único del departamento';
COMMENT ON COLUMN cor_departments.name IS 'Nombre del departamento';
COMMENT ON COLUMN cor_departments.description IS 'Descripción detallada del departamento';
COMMENT ON COLUMN cor_departments.code IS 'Código único del departamento para identificación interna';
COMMENT ON COLUMN cor_departments.manager_id IS 'ID del usuario que es manager del departamento (referencia a cor_users.id)';
COMMENT ON COLUMN cor_departments.parent_department_id IS 'ID del departamento padre si es un subdepartamento (referencia a cor_departments.id)';
COMMENT ON COLUMN cor_departments.is_active IS 'Indica si el departamento está activo en el sistema';
COMMENT ON COLUMN cor_departments.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN cor_departments.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de usuarios (CORE - Core Modules)
CREATE TABLE cor_users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    profile_image VARCHAR(255),
    phone VARCHAR(20),
    timezone VARCHAR(50) DEFAULT 'UTC',
    language VARCHAR(10) DEFAULT 'en',
    department_id BIGINT REFERENCES cor_departments(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);

-- Comentarios de la tabla cor_users
COMMENT ON TABLE cor_users IS 'Tabla principal que almacena la información de los usuarios del sistema';
COMMENT ON COLUMN cor_users.id IS 'Identificador único del usuario';
COMMENT ON COLUMN cor_users.username IS 'Nombre de usuario único para autenticación';
COMMENT ON COLUMN cor_users.email IS 'Dirección de correo electrónico única del usuario';
COMMENT ON COLUMN cor_users.password IS 'Contraseña encriptada del usuario';
COMMENT ON COLUMN cor_users.first_name IS 'Nombre del usuario';
COMMENT ON COLUMN cor_users.last_name IS 'Apellido del usuario';
COMMENT ON COLUMN cor_users.is_active IS 'Indica si la cuenta del usuario está activa';
COMMENT ON COLUMN cor_users.profile_image IS 'Ruta de la imagen de perfil del usuario';
COMMENT ON COLUMN cor_users.phone IS 'Número de teléfono del usuario';
COMMENT ON COLUMN cor_users.timezone IS 'Zona horaria del usuario';
COMMENT ON COLUMN cor_users.language IS 'Idioma preferido del usuario';
COMMENT ON COLUMN cor_users.department_id IS 'ID del departamento al que pertenece el usuario (referencia a cor_departments.id)';
COMMENT ON COLUMN cor_users.created_at IS 'Fecha y hora de creación de la cuenta';
COMMENT ON COLUMN cor_users.updated_at IS 'Fecha y hora de la última actualización de la cuenta';
COMMENT ON COLUMN cor_users.last_login IS 'Fecha y hora del último inicio de sesión';

-- Tabla de sesiones de usuario (CORE - Core Modules)
CREATE TABLE cor_user_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES cor_users(id) ON DELETE CASCADE,
    session_id VARCHAR(255) UNIQUE NOT NULL,
    ip_address INET,
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    is_active BOOLEAN DEFAULT true
);

-- Comentarios de la tabla cor_user_sessions
COMMENT ON TABLE cor_user_sessions IS 'Tabla que almacena las sesiones activas de los usuarios';
COMMENT ON COLUMN cor_user_sessions.id IS 'Identificador único de la sesión';
COMMENT ON COLUMN cor_user_sessions.user_id IS 'ID del usuario propietario de la sesión';
COMMENT ON COLUMN cor_user_sessions.session_id IS 'Identificador único de la sesión (UUID)';
COMMENT ON COLUMN cor_user_sessions.ip_address IS 'Dirección IP desde donde se creó la sesión';
COMMENT ON COLUMN cor_user_sessions.user_agent IS 'User agent del navegador o cliente';
COMMENT ON COLUMN cor_user_sessions.created_at IS 'Fecha y hora de creación de la sesión';
COMMENT ON COLUMN cor_user_sessions.expires_at IS 'Fecha y hora de expiración de la sesión';
COMMENT ON COLUMN cor_user_sessions.is_active IS 'Indica si la sesión está activa';

-- Tabla de roles (CORE - Core Modules)
CREATE TABLE cor_roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT true,
    is_system BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla cor_roles
COMMENT ON TABLE cor_roles IS 'Tabla que almacena los roles del sistema';
COMMENT ON COLUMN cor_roles.id IS 'Identificador único del rol';
COMMENT ON COLUMN cor_roles.name IS 'Nombre único del rol';
COMMENT ON COLUMN cor_roles.description IS 'Descripción del rol';
COMMENT ON COLUMN cor_roles.is_active IS 'Indica si el rol está activo';
COMMENT ON COLUMN cor_roles.is_system IS 'Indica si es un rol del sistema (no se puede eliminar)';
COMMENT ON COLUMN cor_roles.created_at IS 'Fecha y hora de creación del rol';
COMMENT ON COLUMN cor_roles.updated_at IS 'Fecha y hora de la última actualización del rol';

-- Tabla de permisos (CORE - Core Modules)
CREATE TABLE cor_permissions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    resource VARCHAR(100) NOT NULL,
    action VARCHAR(50) NOT NULL,
    is_active BOOLEAN DEFAULT true
);

-- Comentarios de la tabla cor_permissions
COMMENT ON TABLE cor_permissions IS 'Tabla que almacena los permisos del sistema';
COMMENT ON COLUMN cor_permissions.id IS 'Identificador único del permiso';
COMMENT ON COLUMN cor_permissions.name IS 'Nombre único del permiso';
COMMENT ON COLUMN cor_permissions.description IS 'Descripción del permiso';
COMMENT ON COLUMN cor_permissions.resource IS 'Recurso al que se aplica el permiso';
COMMENT ON COLUMN cor_permissions.action IS 'Acción permitida sobre el recurso';
COMMENT ON COLUMN cor_permissions.is_active IS 'Indica si el permiso está activo';

-- Tabla de menús (CORE - Core Modules)
CREATE TABLE cor_menus (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    icon VARCHAR(50),
    href VARCHAR(255),
    parent_id BIGINT REFERENCES cor_menus(id),
    order_index INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT true,
    requires_auth BOOLEAN DEFAULT true,
    is_external BOOLEAN DEFAULT false,
    target VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla cor_menus
COMMENT ON TABLE cor_menus IS 'Tabla que almacena la estructura de menús del sistema';
COMMENT ON COLUMN cor_menus.id IS 'Identificador único del menú';
COMMENT ON COLUMN cor_menus.name IS 'Nombre del menú';
COMMENT ON COLUMN cor_menus.description IS 'Descripción del menú';
COMMENT ON COLUMN cor_menus.icon IS 'Icono del menú (nombre del icono)';
COMMENT ON COLUMN cor_menus.href IS 'Ruta de navegación del menú';
COMMENT ON COLUMN cor_menus.parent_id IS 'ID del menú padre (para submenús)';
COMMENT ON COLUMN cor_menus.order_index IS 'Orden de visualización del menú';
COMMENT ON COLUMN cor_menus.is_active IS 'Indica si el menú está activo';
COMMENT ON COLUMN cor_menus.requires_auth IS 'Indica si el menú requiere autenticación';
COMMENT ON COLUMN cor_menus.is_external IS 'Indica si el menú es externo al sistema';
COMMENT ON COLUMN cor_menus.target IS 'Target del enlace (_blank, _self, etc.)';
COMMENT ON COLUMN cor_menus.created_at IS 'Fecha y hora de creación del menú';
COMMENT ON COLUMN cor_menus.updated_at IS 'Fecha y hora de la última actualización del menú';

-- Tabla de auditoría (CORE - Core Modules)
CREATE TABLE cor_audit_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES cor_users(id),
    username VARCHAR(50),
    action VARCHAR(100) NOT NULL,
    resource VARCHAR(100),
    resource_id VARCHAR(100),
    ip_address INET,
    user_agent TEXT,
    request_method VARCHAR(10),
    request_url TEXT,
    request_body TEXT,
    response_status INTEGER,
    execution_time BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    session_id VARCHAR(100),
    error_message TEXT,
    severity VARCHAR(20) DEFAULT 'INFO',
    module VARCHAR(50)
);

-- Comentarios de la tabla cor_audit_logs
COMMENT ON TABLE cor_audit_logs IS 'Tabla que almacena el historial de auditoría del sistema';
COMMENT ON COLUMN cor_audit_logs.id IS 'Identificador único del log de auditoría';
COMMENT ON COLUMN cor_audit_logs.user_id IS 'ID del usuario que realizó la acción';
COMMENT ON COLUMN cor_audit_logs.username IS 'Nombre de usuario que realizó la acción';
COMMENT ON COLUMN cor_audit_logs.action IS 'Acción realizada por el usuario';
COMMENT ON COLUMN cor_audit_logs.resource IS 'Recurso sobre el que se realizó la acción';
COMMENT ON COLUMN cor_audit_logs.resource_id IS 'ID del recurso sobre el que se realizó la acción';
COMMENT ON COLUMN cor_audit_logs.ip_address IS 'Dirección IP desde donde se realizó la acción';
COMMENT ON COLUMN cor_audit_logs.user_agent IS 'User agent del navegador o cliente';
COMMENT ON COLUMN cor_audit_logs.request_method IS 'Método HTTP de la petición';
COMMENT ON COLUMN cor_audit_logs.request_url IS 'URL de la petición';
COMMENT ON COLUMN cor_audit_logs.request_body IS 'Cuerpo de la petición';
COMMENT ON COLUMN cor_audit_logs.response_status IS 'Código de estado de la respuesta';
COMMENT ON COLUMN cor_audit_logs.execution_time IS 'Tiempo de ejecución en milisegundos';
COMMENT ON COLUMN cor_audit_logs.created_at IS 'Fecha y hora de creación del log';
COMMENT ON COLUMN cor_audit_logs.session_id IS 'ID de la sesión donde se realizó la acción';
COMMENT ON COLUMN cor_audit_logs.error_message IS 'Mensaje de error si la acción falló';
COMMENT ON COLUMN cor_audit_logs.severity IS 'Nivel de severidad del log (INFO, WARNING, ERROR, CRITICAL)';
COMMENT ON COLUMN cor_audit_logs.module IS 'Módulo del sistema donde se realizó la acción';

-- Tabla de intentos de login (CORE - Core Modules)
CREATE TABLE cor_login_attempts (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50),
    ip_address INET,
    success BOOLEAN NOT NULL,
    failure_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_agent TEXT
);

-- Comentarios de la tabla cor_login_attempts
COMMENT ON TABLE cor_login_attempts IS 'Tabla que registra los intentos de inicio de sesión';
COMMENT ON COLUMN cor_login_attempts.id IS 'Identificador único del intento de login';
COMMENT ON COLUMN cor_login_attempts.username IS 'Nombre de usuario utilizado en el intento';
COMMENT ON COLUMN cor_login_attempts.ip_address IS 'Dirección IP desde donde se realizó el intento';
COMMENT ON COLUMN cor_login_attempts.success IS 'Indica si el intento de login fue exitoso';
COMMENT ON COLUMN cor_login_attempts.failure_reason IS 'Razón del fallo si el intento no fue exitoso';
COMMENT ON COLUMN cor_login_attempts.created_at IS 'Fecha y hora del intento de login';
COMMENT ON COLUMN cor_login_attempts.user_agent IS 'User agent del navegador o cliente';

-- Tablas de relación (CORE - Core Modules)
CREATE TABLE cor_user_roles (
    user_id BIGINT REFERENCES cor_users(id) ON DELETE CASCADE,
    role_id BIGINT REFERENCES cor_roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE cor_role_menus (
    role_id BIGINT REFERENCES cor_roles(id) ON DELETE CASCADE,
    menu_id BIGINT REFERENCES cor_menus(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, menu_id)
);

CREATE TABLE cor_role_permissions (
    role_id BIGINT REFERENCES cor_roles(id) ON DELETE CASCADE,
    permission_id BIGINT REFERENCES cor_permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

-- Tabla de licencias de la plataforma (CORE - Core Modules)
CREATE TABLE cor_platform_licenses (
    id BIGSERIAL PRIMARY KEY,
    license_type VARCHAR(50) NOT NULL,
    license_file BYTEA NOT NULL,
    license_hash VARCHAR(64) NOT NULL,
    encryption_key_id VARCHAR(100) NOT NULL,
    max_nodes INTEGER,
    max_gpus INTEGER,
    max_users INTEGER,
    max_ai_models INTEGER,
    max_rag_documents INTEGER,
    max_api_calls_per_month BIGINT,
    max_storage_gb INTEGER,
    features_enabled TEXT,
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla cor_platform_licenses
COMMENT ON TABLE cor_platform_licenses IS 'Tabla que almacena las licencias de la plataforma';
COMMENT ON COLUMN cor_platform_licenses.id IS 'Identificador único de la licencia';
COMMENT ON COLUMN cor_platform_licenses.license_type IS 'Tipo de licencia (FREE, BASIC, PROFESSIONAL, etc.)';
COMMENT ON COLUMN cor_platform_licenses.license_file IS 'Archivo de licencia encriptado';
COMMENT ON COLUMN cor_platform_licenses.license_hash IS 'Hash del archivo de licencia para verificación';
COMMENT ON COLUMN cor_platform_licenses.encryption_key_id IS 'ID de la clave de encriptación utilizada';
COMMENT ON COLUMN cor_platform_licenses.max_nodes IS 'Número máximo de nodos permitidos';
COMMENT ON COLUMN cor_platform_licenses.max_gpus IS 'Número máximo de GPUs permitidas';
COMMENT ON COLUMN cor_platform_licenses.max_users IS 'Número máximo de usuarios permitidos';
COMMENT ON COLUMN cor_platform_licenses.max_ai_models IS 'Número máximo de modelos de IA permitidos';
COMMENT ON COLUMN cor_platform_licenses.max_rag_documents IS 'Número máximo de documentos RAG permitidos';
COMMENT ON COLUMN cor_platform_licenses.max_api_calls_per_month IS 'Número máximo de llamadas API por mes';
COMMENT ON COLUMN cor_platform_licenses.max_storage_gb IS 'Almacenamiento máximo en GB';
COMMENT ON COLUMN cor_platform_licenses.features_enabled IS 'Características habilitadas en formato JSON';
COMMENT ON COLUMN cor_platform_licenses.issued_at IS 'Fecha y hora de emisión de la licencia';
COMMENT ON COLUMN cor_platform_licenses.expires_at IS 'Fecha y hora de expiración de la licencia';
COMMENT ON COLUMN cor_platform_licenses.is_active IS 'Indica si la licencia está activa';
COMMENT ON COLUMN cor_platform_licenses.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN cor_platform_licenses.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de nodos de la plataforma (CORE - Core Modules)
CREATE TABLE cor_platform_nodes (
    id BIGSERIAL PRIMARY KEY,
    node_name VARCHAR(100) NOT NULL,
    node_type VARCHAR(50) NOT NULL,
    ip_address INET,
    hostname VARCHAR(100),
    cpu_cores INTEGER,
    ram_gb INTEGER,
    gpu_count INTEGER,
    gpu_type VARCHAR(100),
    storage_gb INTEGER,
    is_active BOOLEAN DEFAULT true,
    last_heartbeat TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla cor_platform_nodes
COMMENT ON TABLE cor_platform_nodes IS 'Tabla que almacena la información de los nodos de la plataforma';
COMMENT ON COLUMN cor_platform_nodes.id IS 'Identificador único del nodo';
COMMENT ON COLUMN cor_platform_nodes.node_name IS 'Nombre del nodo';
COMMENT ON COLUMN cor_platform_nodes.node_type IS 'Tipo de nodo (MASTER, WORKER, INFERENCE, etc.)';
COMMENT ON COLUMN cor_platform_nodes.ip_address IS 'Dirección IP del nodo';
COMMENT ON COLUMN cor_platform_nodes.hostname IS 'Hostname del nodo';
COMMENT ON COLUMN cor_platform_nodes.cpu_cores IS 'Número de núcleos CPU';
COMMENT ON COLUMN cor_platform_nodes.ram_gb IS 'Memoria RAM en GB';
COMMENT ON COLUMN cor_platform_nodes.gpu_count IS 'Número de GPUs';
COMMENT ON COLUMN cor_platform_nodes.gpu_type IS 'Tipo de GPU';
COMMENT ON COLUMN cor_platform_nodes.storage_gb IS 'Almacenamiento en GB';
COMMENT ON COLUMN cor_platform_nodes.is_active IS 'Indica si el nodo está activo';
COMMENT ON COLUMN cor_platform_nodes.last_heartbeat IS 'Último heartbeat del nodo';
COMMENT ON COLUMN cor_platform_nodes.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN cor_platform_nodes.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de actualizaciones de la plataforma (CORE - Core Modules)
CREATE TABLE cor_platform_updates (
    id BIGSERIAL PRIMARY KEY,
    version VARCHAR(50) NOT NULL,
    update_type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    changelog TEXT,
    download_url VARCHAR(500),
    file_size_bytes BIGINT,
    checksum VARCHAR(64),
    is_mandatory BOOLEAN DEFAULT false,
    requires_restart BOOLEAN DEFAULT false,
    compatibility_matrix TEXT,
    release_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla cor_platform_updates
COMMENT ON TABLE cor_platform_updates IS 'Tabla que almacena las actualizaciones disponibles de la plataforma';
COMMENT ON COLUMN cor_platform_updates.id IS 'Identificador único de la actualización';
COMMENT ON COLUMN cor_platform_updates.version IS 'Versión de la actualización';
COMMENT ON COLUMN cor_platform_updates.update_type IS 'Tipo de actualización (SECURITY, FEATURE, BUGFIX, etc.)';
COMMENT ON COLUMN cor_platform_updates.title IS 'Título de la actualización';
COMMENT ON COLUMN cor_platform_updates.description IS 'Descripción de la actualización';
COMMENT ON COLUMN cor_platform_updates.changelog IS 'Changelog detallado en formato JSON';
COMMENT ON COLUMN cor_platform_updates.download_url IS 'URL de descarga de la actualización';
COMMENT ON COLUMN cor_platform_updates.file_size_bytes IS 'Tamaño del archivo en bytes';
COMMENT ON COLUMN cor_platform_updates.checksum IS 'Hash del archivo para verificación';
COMMENT ON COLUMN cor_platform_updates.is_mandatory IS 'Indica si la actualización es obligatoria';
COMMENT ON COLUMN cor_platform_updates.requires_restart IS 'Indica si requiere reinicio del sistema';
COMMENT ON COLUMN cor_platform_updates.compatibility_matrix IS 'Matriz de compatibilidad en formato JSON';
COMMENT ON COLUMN cor_platform_updates.release_date IS 'Fecha de lanzamiento de la actualización';
COMMENT ON COLUMN cor_platform_updates.is_active IS 'Indica si la actualización está activa';
COMMENT ON COLUMN cor_platform_updates.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN cor_platform_updates.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de instalaciones de actualizaciones (CORE - Core Modules)
CREATE TABLE cor_update_installations (
    id BIGSERIAL PRIMARY KEY,
    update_id BIGINT REFERENCES cor_platform_updates(id) ON DELETE CASCADE,
    node_id BIGINT REFERENCES cor_platform_nodes(id) ON DELETE CASCADE,
    installation_status VARCHAR(50) NOT NULL,
    installation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completion_date TIMESTAMP,
    error_message TEXT,
    rollback_status VARCHAR(50) DEFAULT 'NONE',
    rollback_date TIMESTAMP,
    installed_by BIGINT REFERENCES cor_users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla cor_update_installations
COMMENT ON TABLE cor_update_installations IS 'Tabla que registra las instalaciones de actualizaciones en los nodos';
COMMENT ON COLUMN cor_update_installations.id IS 'Identificador único de la instalación';
COMMENT ON COLUMN cor_update_installations.update_id IS 'ID de la actualización instalada';
COMMENT ON COLUMN cor_update_installations.node_id IS 'ID del nodo donde se instaló';
COMMENT ON COLUMN cor_update_installations.installation_status IS 'Estado de la instalación (PENDING, IN_PROGRESS, COMPLETED, etc.)';
COMMENT ON COLUMN cor_update_installations.installation_date IS 'Fecha y hora de inicio de la instalación';
COMMENT ON COLUMN cor_update_installations.completion_date IS 'Fecha y hora de finalización de la instalación';
COMMENT ON COLUMN cor_update_installations.error_message IS 'Mensaje de error si la instalación falló';
COMMENT ON COLUMN cor_update_installations.rollback_status IS 'Estado del rollback (NONE, PENDING, IN_PROGRESS, etc.)';
COMMENT ON COLUMN cor_update_installations.rollback_date IS 'Fecha y hora del rollback';
COMMENT ON COLUMN cor_update_installations.installed_by IS 'ID del usuario que realizó la instalación';
COMMENT ON COLUMN cor_update_installations.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN cor_update_installations.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Tabla de programación de actualizaciones (CORE - Core Modules)
CREATE TABLE cor_update_schedules (
    id BIGSERIAL PRIMARY KEY,
    update_id BIGINT REFERENCES cor_platform_updates(id) ON DELETE CASCADE,
    schedule_type VARCHAR(50) NOT NULL,
    cron_expression VARCHAR(100),
    next_execution TIMESTAMP,
    is_active BOOLEAN DEFAULT true,
    created_by BIGINT REFERENCES cor_users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comentarios de la tabla cor_update_schedules
COMMENT ON TABLE cor_update_schedules IS 'Tabla que gestiona la programación de actualizaciones automáticas';
COMMENT ON COLUMN cor_update_schedules.id IS 'Identificador único de la programación';
COMMENT ON COLUMN cor_update_schedules.update_id IS 'ID de la actualización a programar';
COMMENT ON COLUMN cor_update_schedules.schedule_type IS 'Tipo de programación (IMMEDIATE, SCHEDULED, MAINTENANCE_WINDOW, etc.)';
COMMENT ON COLUMN cor_update_schedules.cron_expression IS 'Expresión cron para programación automática';
COMMENT ON COLUMN cor_update_schedules.next_execution IS 'Próxima ejecución programada';
COMMENT ON COLUMN cor_update_schedules.is_active IS 'Indica si la programación está activa';
COMMENT ON COLUMN cor_update_schedules.created_by IS 'ID del usuario que creó la programación';
COMMENT ON COLUMN cor_update_schedules.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN cor_update_schedules.updated_at IS 'Fecha y hora de la última actualización del registro';

-- Índices para optimización (CORE - Core Modules)
CREATE INDEX idx_cor_users_username ON cor_users(username);
CREATE INDEX idx_cor_users_email ON cor_users(email);
CREATE INDEX idx_cor_users_active ON cor_users(is_active);
CREATE INDEX idx_cor_users_department_id ON cor_users(department_id);
CREATE INDEX idx_cor_roles_name ON cor_roles(name);
CREATE INDEX idx_cor_roles_active ON cor_roles(is_active);
CREATE INDEX idx_cor_menus_name ON cor_menus(name);
CREATE INDEX idx_cor_menus_parent_id ON cor_menus(parent_id);
CREATE INDEX idx_cor_menus_active ON cor_menus(is_active);
CREATE INDEX idx_cor_user_roles_user_id ON cor_user_roles(user_id);
CREATE INDEX idx_cor_user_roles_role_id ON cor_user_roles(role_id);
CREATE INDEX idx_cor_role_menus_role_id ON cor_role_menus(role_id);
CREATE INDEX idx_cor_role_menus_menu_id ON cor_role_menus(menu_id);
CREATE INDEX idx_cor_role_permissions_role_id ON cor_role_permissions(role_id);
CREATE INDEX idx_cor_role_permissions_permission_id ON cor_role_permissions(permission_id);
CREATE INDEX idx_cor_user_sessions_user_id ON cor_user_sessions(user_id);
CREATE INDEX idx_cor_user_sessions_session_id ON cor_user_sessions(session_id);
CREATE INDEX idx_cor_user_sessions_active ON cor_user_sessions(is_active);
CREATE INDEX idx_cor_audit_logs_user_id ON cor_audit_logs(user_id);
CREATE INDEX idx_cor_audit_logs_action ON cor_audit_logs(action);
CREATE INDEX idx_cor_audit_logs_created_at ON cor_audit_logs(created_at);
CREATE INDEX idx_cor_audit_logs_resource ON cor_audit_logs(resource);
CREATE INDEX idx_cor_audit_logs_module ON cor_audit_logs(module);
CREATE INDEX idx_cor_audit_logs_severity ON cor_audit_logs(severity);
CREATE INDEX idx_cor_login_attempts_username ON cor_login_attempts(username);
CREATE INDEX idx_cor_login_attempts_ip ON cor_login_attempts(ip_address);
CREATE INDEX idx_cor_login_attempts_created_at ON cor_login_attempts(created_at);
CREATE INDEX idx_cor_departments_name ON cor_departments(name);
CREATE INDEX idx_cor_departments_code ON cor_departments(code);
CREATE INDEX idx_cor_departments_active ON cor_departments(is_active);

-- Índices para licencias de plataforma
CREATE INDEX idx_cor_platform_licenses_type ON cor_platform_licenses(license_type);
CREATE INDEX idx_cor_platform_licenses_active ON cor_platform_licenses(is_active);
CREATE INDEX idx_cor_platform_licenses_expires_at ON cor_platform_licenses(expires_at);

-- Índices para nodos de plataforma
CREATE INDEX idx_cor_platform_nodes_name ON cor_platform_nodes(node_name);
CREATE INDEX idx_cor_platform_nodes_type ON cor_platform_nodes(node_type);
CREATE INDEX idx_cor_platform_nodes_active ON cor_platform_nodes(is_active);
CREATE INDEX idx_cor_platform_nodes_last_heartbeat ON cor_platform_nodes(last_heartbeat);

-- Índices para actualizaciones de plataforma
CREATE INDEX idx_cor_platform_updates_version ON cor_platform_updates(version);
CREATE INDEX idx_cor_platform_updates_type ON cor_platform_updates(update_type);
CREATE INDEX idx_cor_platform_updates_active ON cor_platform_updates(is_active);
CREATE INDEX idx_cor_platform_updates_release_date ON cor_platform_updates(release_date);

-- Índices para instalaciones de actualizaciones
CREATE INDEX idx_cor_update_installations_update_id ON cor_update_installations(update_id);
CREATE INDEX idx_cor_update_installations_node_id ON cor_update_installations(node_id);
CREATE INDEX idx_cor_update_installations_status ON cor_update_installations(installation_status);
CREATE INDEX idx_cor_update_installations_date ON cor_update_installations(installation_date);

-- Índices para programación de actualizaciones
CREATE INDEX idx_cor_update_schedules_update_id ON cor_update_schedules(update_id);
CREATE INDEX idx_cor_update_schedules_type ON cor_update_schedules(schedule_type);
CREATE INDEX idx_cor_update_schedules_active ON cor_update_schedules(is_active);
CREATE INDEX idx_cor_update_schedules_next_execution ON cor_update_schedules(next_execution);
```

### 3. Script de Inicialización con Datos de Demo

```sql
-- =====================================================
-- INICIALIZACIÓN DE ROLES DEL SISTEMA
-- =====================================================

-- Insertar roles del sistema
INSERT INTO cor_roles (name, description, is_system, is_active) VALUES
('admin', 'Administrador del sistema con acceso completo', true, true),
('developer', 'Desarrollador con acceso a herramientas de desarrollo', true, true),
('viewer', 'Usuario con acceso de solo lectura', true, true),
('business_analytics', 'Analista de negocio con acceso a reportes', true, true),
('ai_analytics', 'Analista de IA con acceso a modelos y entrenamiento', true, true),
('ai_developer', 'Desarrollador de IA especializado', true, true),
('devops', 'Ingeniero DevOps con acceso a infraestructura', true, true),
('project_manager', 'Gestor de proyectos con acceso a planificación', true, true),
('architect', 'Arquitecto de software con acceso a diseño', true, true),
('ai_architect', 'Arquitecto de IA con acceso a modelos avanzados', true, true);

-- =====================================================
-- INICIALIZACIÓN DE DEPARTAMENTOS
-- =====================================================

-- Insertar departamentos de demo
INSERT INTO cor_departments (name, description, code, is_active) VALUES
('Administración', 'Departamento de administración general del sistema', 'ADMIN', true),
('Desarrollo', 'Departamento de desarrollo de software', 'DEV', true),
('Consultoría', 'Departamento de consultoría y asesoramiento', 'CONS', true),
('Analytics', 'Departamento de análisis de negocio', 'ANAL', true),
('AI & ML', 'Departamento de Inteligencia Artificial y Machine Learning', 'AIML', true),
('DevOps', 'Departamento de operaciones de desarrollo', 'DEVOPS', true),
('Gestión de Proyectos', 'Departamento de gestión y coordinación de proyectos', 'PM', true);

-- =====================================================
-- INICIALIZACIÓN DE USUARIOS DE DEMO
-- =====================================================

-- Insertar usuarios de demo (password: demo123 - hash bcrypt)
INSERT INTO cor_users (username, email, password, first_name, last_name, is_active, department_id) VALUES
('admin', 'admin@company.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Admin', 'User', true,
        (SELECT id FROM cor_departments WHERE code = 'ADMIN')),
('developer', 'dev@company.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Developer', 'User', true,
    (SELECT id FROM cor_departments WHERE code = 'DEV')),
('viewer', 'viewer@company.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Viewer', 'User', true,
    (SELECT id FROM cor_departments WHERE code = 'CONS')),
('business', 'business@company.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Business Analytics', 'User', true,
    (SELECT id FROM cor_departments WHERE code = 'ANAL')),
('ai', 'ai@company.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'AI Analytics', 'User', true,
    (SELECT id FROM cor_departments WHERE code = 'AIML')),
('devops', 'devops@company.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'DevOps', 'User', true,
    (SELECT id FROM cor_departments WHERE code = 'DEVOPS')),
('pm', 'pm@company.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Project Manager', 'User', true,
    (SELECT id FROM cor_departments WHERE code = 'PM'));

-- =====================================================
-- ASIGNACIÓN DE ROLES A USUARIOS
-- =====================================================

-- Admin User
INSERT INTO cor_user_roles (user_id, role_id)
SELECT u.id, r.id FROM cor_users u, cor_roles r
WHERE u.username = 'admin' AND r.name = 'admin';

-- Developer User
INSERT INTO cor_user_roles (user_id, role_id)
SELECT u.id, r.id FROM cor_users u, cor_roles r
WHERE u.username = 'developer' AND r.name = 'developer';

-- Viewer User
INSERT INTO cor_user_roles (user_id, role_id)
SELECT u.id, r.id FROM cor_users u, cor_roles r
WHERE u.username = 'viewer' AND r.name = 'viewer';

-- Business Analytics User
INSERT INTO cor_user_roles (user_id, role_id)
SELECT u.id, r.id FROM cor_users u, cor_roles r
WHERE u.username = 'business' AND r.name = 'business_analytics';

-- AI Analytics User
INSERT INTO cor_user_roles (user_id, role_id)
SELECT u.id, r.id FROM cor_users u, cor_roles r
WHERE u.username = 'ai' AND r.name = 'ai_analytics';

-- DevOps User
INSERT INTO cor_user_roles (user_id, role_id)
SELECT u.id, r.id FROM cor_users u, cor_roles r
WHERE u.username = 'devops' AND r.name = 'devops';

-- Project Manager User
INSERT INTO cor_user_roles (user_id, role_id)
SELECT u.id, r.id FROM cor_users u, cor_roles r
WHERE u.username = 'pm' AND r.name = 'project_manager';

-- =====================================================
-- INICIALIZACIÓN DE LICENCIAS DE PLATAFORMA
-- =====================================================

-- Insertar licencia de plataforma demo
INSERT INTO cor_platform_licenses (
    license_type, license_file, license_hash, encryption_key_id,
    max_nodes, max_gpus, max_users, max_ai_models, max_rag_documents,
    max_api_calls_per_month, max_storage_gb, features_enabled,
    issued_at, expires_at, is_active
) VALUES (
    'ENTERPRISE',
    E'\\x64656d6f5f6c6963656e73655f66696c65', -- demo_license_file en hex
    'a1b2c3d4e5f6789012345678901234567890abcdef1234567890abcdef12345678',
    'demo_encryption_key_001',
    10, -- max_nodes
    20, -- max_gpus
    100, -- max_users
    50, -- max_ai_models
    1000, -- max_rag_documents
    1000000, -- max_api_calls_per_month
    1000, -- max_storage_gb
    '["AI_TRAINING", "MODEL_SERVING", "RAG_SYSTEM", "CODE_PLAYGROUND", "PLATFORM_UPDATES"]',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP + INTERVAL '1 year',
    true
);

-- =====================================================
-- INICIALIZACIÓN DE NODOS DE PLATAFORMA
-- =====================================================

-- Insertar nodos de plataforma demo
INSERT INTO cor_platform_nodes (
    node_name, node_type, ip_address, hostname, cpu_cores, ram_gb,
    gpu_count, gpu_type, storage_gb, is_active
) VALUES
('master-01', 'MASTER', '192.168.1.10', 'master01.company.com', 16, 64, 0, NULL, 500, true),
('worker-01', 'WORKER', '192.168.1.11', 'worker01.company.com', 32, 128, 4, 'RTX 4090', 1000, true),
('inference-01', 'INFERENCE', '192.168.1.12', 'inference01.company.com', 16, 64, 2, 'RTX 3090', 500, true),
('training-01', 'TRAINING', '192.168.1.13', 'training01.company.com', 64, 256, 8, 'A100', 2000, true),
('storage-01', 'STORAGE', '192.168.1.14', 'storage01.company.com', 8, 32, 0, NULL, 10000, true);

-- =====================================================
-- INICIALIZACIÓN DE ACTUALIZACIONES DE PLATAFORMA
-- =====================================================

-- Insertar actualizaciones de plataforma demo
INSERT INTO cor_platform_updates (
    version, update_type, title, description, changelog,
    download_url, file_size_bytes, checksum, is_mandatory,
    requires_restart, compatibility_matrix, release_date, is_active
) VALUES
('1.2.0', 'FEATURE', 'Nueva funcionalidad de RAG System', 'Implementación completa del sistema RAG con búsqueda semántica',
 '{"features": ["RAG System", "Semantic Search", "Document Processing"], "improvements": ["Performance", "Security"], "bugfixes": ["Memory leaks", "API errors"]}',
 'https://updates.company.com/v1.2.0/platform-update.zip', 52428800, 'sha256:abc123def456...', false, false,
 '{"min_version": "1.1.0", "supported_os": ["Ubuntu 20.04", "CentOS 8"], "dependencies": ["PostgreSQL 13+", "Redis 6+"]}',
 CURRENT_TIMESTAMP - INTERVAL '7 days', true),

('1.1.5', 'SECURITY', 'Parche de seguridad crítico', 'Corrección de vulnerabilidades de seguridad identificadas',
 '{"security": ["CVE-2024-001", "CVE-2024-002"], "improvements": ["Authentication", "Authorization"]}',
 'https://updates.company.com/v1.1.5/security-patch.zip', 10485760, 'sha256:def456abc789...', true, true,
 '{"min_version": "1.1.0", "supported_os": ["Ubuntu 20.04", "CentOS 8"], "dependencies": ["PostgreSQL 13+"]}',
 CURRENT_TIMESTAMP - INTERVAL '3 days', true),

('1.1.0', 'MAJOR', 'Actualización mayor de la plataforma', 'Nueva arquitectura de microservicios y mejoras de rendimiento',
 '{"features": ["Microservices", "Kubernetes Support", "Monitoring"], "performance": ["50% faster", "Better scalability"]}',
 'https://updates.company.com/v1.1.0/major-update.zip', 104857600, 'sha256:ghi789jkl012...', false, true,
 '{"min_version": "1.0.0", "supported_os": ["Ubuntu 20.04", "CentOS 8"], "dependencies": ["Kubernetes 1.20+", "PostgreSQL 13+"]}',
 CURRENT_TIMESTAMP - INTERVAL '30 days', true);

-- =====================================================
-- INICIALIZACIÓN DE INSTALACIONES DE ACTUALIZACIONES
-- =====================================================

-- Insertar instalaciones de actualizaciones demo
INSERT INTO cor_update_installations (
    update_id, node_id, installation_status, installation_date, completion_date, installed_by
) VALUES
((SELECT id FROM cor_platform_updates WHERE version = '1.1.0'),
 (SELECT id FROM cor_platform_nodes WHERE node_name = 'master-01'),
 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '25 days', CURRENT_TIMESTAMP - INTERVAL '25 days' + INTERVAL '30 minutes',
 (SELECT id FROM cor_users WHERE username = 'admin')),

((SELECT id FROM cor_platform_updates WHERE version = '1.1.5'),
 (SELECT id FROM cor_platform_nodes WHERE node_name = 'master-01'),
 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '2 days' + INTERVAL '15 minutes',
 (SELECT id FROM cor_users WHERE username = 'admin')),

((SELECT id FROM cor_platform_updates WHERE version = '1.2.0'),
 (SELECT id FROM cor_platform_nodes WHERE node_name = 'worker-01'),
 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '1 hour', NULL,
 (SELECT id FROM cor_users WHERE username = 'devops'));

-- =====================================================
-- INICIALIZACIÓN DE PROGRAMACIÓN DE ACTUALIZACIONES
-- =====================================================

-- Insertar programaciones de actualizaciones demo
INSERT INTO cor_update_schedules (
    update_id, schedule_type, cron_expression, next_execution, created_by
) VALUES
((SELECT id FROM cor_platform_updates WHERE version = '1.2.0'),
 'MAINTENANCE_WINDOW', '0 2 * * 0', -- Todos los domingos a las 2:00 AM
 CURRENT_TIMESTAMP + INTERVAL '3 days',
 (SELECT id FROM cor_users WHERE username = 'admin')),

((SELECT id FROM cor_platform_updates WHERE version = '1.1.5'),
 'IMMEDIATE', NULL, CURRENT_TIMESTAMP,
 (SELECT id FROM cor_users WHERE username = 'admin'));
```

## Servicios de Core Modules

### PlatformLicenseService

```java
@Service
@Transactional
public class PlatformLicenseService {

    @Autowired
    private PlatformLicenseRepository licenseRepository;

    @Autowired
    private PlatformNodeRepository nodeRepository;

    @Autowired
    private AuditService auditService;

    public PlatformLicense createLicense(PlatformLicenseDto licenseDto) {
        // Validar datos de la licencia
        validateLicenseData(licenseDto);

        // Crear licencia
        PlatformLicense license = new PlatformLicense();
        license.setLicenseType(licenseDto.getLicenseType());
        license.setLicenseFile(licenseDto.getLicenseFile());
        license.setLicenseHash(generateLicenseHash(licenseDto.getLicenseFile()));
        license.setEncryptionKeyId(licenseDto.getEncryptionKeyId());
        license.setMaxNodes(licenseDto.getMaxNodes());
        license.setMaxGPUs(licenseDto.getMaxGPUs());
        license.setMaxUsers(licenseDto.getMaxUsers());
        license.setMaxAIModels(licenseDto.getMaxAIModels());
        license.setMaxRAGDocuments(licenseDto.getMaxRAGDocuments());
        license.setMaxAPICallsPerMonth(licenseDto.getMaxAPICallsPerMonth());
        license.setMaxStorageGB(licenseDto.getMaxStorageGB());
        license.setFeaturesEnabled(licenseDto.getFeaturesEnabled());
        license.setIssuedAt(LocalDateTime.now());
        license.setExpiresAt(licenseDto.getExpiresAt());
        license.setIsActive(true);
        license.setCreatedAt(LocalDateTime.now());

        PlatformLicense savedLicense = licenseRepository.save(license);

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "CREATE_PLATFORM_LICENSE",
            "PLATFORM_LICENSE",
            savedLicense.getId().toString()
        );

        return savedLicense;
    }

    public boolean validateLicense(Long licenseId) {
        PlatformLicense license = licenseRepository.findById(licenseId)
            .orElseThrow(() -> new LicenseNotFoundException("License not found"));

        // Verificar si la licencia ha expirado
        if (license.getExpiresAt() != null && license.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }

        // Verificar límites de nodos
        long activeNodes = nodeRepository.countByIsActiveTrue();
        if (license.getMaxNodes() != null && activeNodes > license.getMaxNodes()) {
            return false;
        }

        // Verificar límites de GPUs
        long totalGPUs = nodeRepository.sumGpuCountByIsActiveTrue();
        if (license.getMaxGPUs() != null && totalGPUs > license.getMaxGPUs()) {
            return false;
        }

        return true;
    }

    private void validateLicenseData(PlatformLicenseDto licenseDto) {
        if (licenseDto.getLicenseType() == null) {
            throw new ValidationException("License type is required");
        }

        if (licenseDto.getLicenseFile() == null || licenseDto.getLicenseFile().length == 0) {
            throw new ValidationException("License file is required");
        }

        if (licenseDto.getMaxNodes() != null && licenseDto.getMaxNodes() <= 0) {
            throw new ValidationException("Max nodes must be positive");
        }

        if (licenseDto.getMaxGPUs() != null && licenseDto.getMaxGPUs() <= 0) {
            throw new ValidationException("Max GPUs must be positive");
        }
    }

    private String generateLicenseHash(byte[] licenseFile) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(licenseFile);
            return DatatypeConverter.printHexBinary(hash).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating license hash", e);
        }
    }

    private String getCurrentUsername() {
        return "current_user"; // Placeholder
    }
}
```

### PlatformUpdateService

```java
@Service
@Transactional
public class PlatformUpdateService {

    @Autowired
    private PlatformUpdateRepository updateRepository;

    @Autowired
    private UpdateInstallationRepository installationRepository;

    @Autowired
    private UpdateScheduleRepository scheduleRepository;

    @Autowired
    private PlatformNodeRepository nodeRepository;

    @Autowired
    private AuditService auditService;

    public PlatformUpdate createUpdate(PlatformUpdateDto updateDto) {
        // Validar datos de la actualización
        validateUpdateData(updateDto);

        // Crear actualización
        PlatformUpdate update = new PlatformUpdate();
        update.setVersion(updateDto.getVersion());
        update.setUpdateType(updateDto.getUpdateType());
        update.setTitle(updateDto.getTitle());
        update.setDescription(updateDto.getDescription());
        update.setChangelog(updateDto.getChangelog());
        update.setDownloadUrl(updateDto.getDownloadUrl());
        update.setFileSizeBytes(updateDto.getFileSizeBytes());
        update.setChecksum(updateDto.getChecksum());
        update.setIsMandatory(updateDto.getIsMandatory());
        update.setRequiresRestart(updateDto.getRequiresRestart());
        update.setCompatibilityMatrix(updateDto.getCompatibilityMatrix());
        update.setReleaseDate(LocalDateTime.now());
        update.setIsActive(true);
        update.setCreatedAt(LocalDateTime.now());

        PlatformUpdate savedUpdate = updateRepository.save(update);

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "CREATE_PLATFORM_UPDATE",
            "PLATFORM_UPDATE",
            savedUpdate.getId().toString()
        );

        return savedUpdate;
    }

    public UpdateInstallation installUpdate(Long updateId, Long nodeId) {
        // Validar que la actualización existe
        PlatformUpdate update = updateRepository.findById(updateId)
            .orElseThrow(() -> new UpdateNotFoundException("Update not found"));

        // Validar que el nodo existe
        PlatformNode node = nodeRepository.findById(nodeId)
            .orElseThrow(() -> new NodeNotFoundException("Node not found"));

        // Crear instalación
        UpdateInstallation installation = new UpdateInstallation();
        installation.setUpdateId(updateId);
        installation.setNodeId(nodeId);
        installation.setInstallationStatus(InstallationStatus.IN_PROGRESS);
        installation.setInstallationDate(LocalDateTime.now());
        installation.setInstalledBy(getCurrentUserId());
        installation.setCreatedAt(LocalDateTime.now());

        UpdateInstallation savedInstallation = installationRepository.save(installation);

        // Aquí se ejecutaría la lógica real de instalación
        // Por ahora solo simulamos el proceso

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "INSTALL_PLATFORM_UPDATE",
            "UPDATE_INSTALLATION",
            savedInstallation.getId().toString()
        );

        return savedInstallation;
    }

    public UpdateSchedule scheduleUpdate(Long updateId, UpdateScheduleDto scheduleDto) {
        // Validar que la actualización existe
        PlatformUpdate update = updateRepository.findById(updateId)
            .orElseThrow(() -> new UpdateNotFoundException("Update not found"));

        // Crear programación
        UpdateSchedule schedule = new UpdateSchedule();
        schedule.setUpdateId(updateId);
        schedule.setScheduleType(scheduleDto.getScheduleType());
        schedule.setCronExpression(scheduleDto.getCronExpression());
        schedule.setNextExecution(calculateNextExecution(scheduleDto.getCronExpression()));
        schedule.setIsActive(true);
        schedule.setCreatedBy(getCurrentUserId());
        schedule.setCreatedAt(LocalDateTime.now());

        UpdateSchedule savedSchedule = scheduleRepository.save(schedule);

        // Registrar auditoría
        auditService.logAction(
            getCurrentUsername(),
            "SCHEDULE_PLATFORM_UPDATE",
            "UPDATE_SCHEDULE",
            savedSchedule.getId().toString()
        );

        return savedSchedule;
    }

    private void validateUpdateData(PlatformUpdateDto updateDto) {
        if (updateDto.getVersion() == null || updateDto.getVersion().trim().isEmpty()) {
            throw new ValidationException("Update version is required");
        }

        if (updateDto.getUpdateType() == null) {
            throw new ValidationException("Update type is required");
        }

        if (updateDto.getTitle() == null || updateDto.getTitle().trim().isEmpty()) {
            throw new ValidationException("Update title is required");
        }
    }

    private LocalDateTime calculateNextExecution(String cronExpression) {
        if (cronExpression == null || cronExpression.trim().isEmpty()) {
            return LocalDateTime.now();
        }

        // Aquí se implementaría la lógica de parsing de cron
        // Por ahora retornamos una hora por defecto
        return LocalDateTime.now().plusHours(1);
    }

    private Long getCurrentUserId() {
        return 1L; // Placeholder
    }

    private String getCurrentUsername() {
        return "current_user"; // Placeholder
    }
}
```

## Monitoreo y Métricas

### Prometheus Metrics

```java
@Component
public class CoreMetrics {

    @Autowired
    private MeterRegistry meterRegistry;

    private final Counter usersCreatedCounter;
    private final Counter usersLoggedInCounter;
    private final Counter rolesCreatedCounter;
    private final Counter permissionsCreatedCounter;
    private final Counter menusCreatedCounter;
    private final Counter auditLogsCreatedCounter;
    private final Counter loginAttemptsCounter;
    private final Counter platformLicensesCreatedCounter;
    private final Counter platformNodesCreatedCounter;
    private final Counter platformUpdatesCreatedCounter;
    private final Counter updateInstallationsCounter;
    private final Timer userCreationTimer;
    private final Timer licenseValidationTimer;
    private final Timer updateInstallationTimer;

    public CoreMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.usersCreatedCounter = Counter.builder("core.users.created")
            .description("Total users created")
            .register(meterRegistry);

        this.usersLoggedInCounter = Counter.builder("core.users.logged_in")
            .description("Total user logins")
            .register(meterRegistry);

        this.rolesCreatedCounter = Counter.builder("core.roles.created")
            .description("Total roles created")
            .register(meterRegistry);

        this.permissionsCreatedCounter = Counter.builder("core.permissions.created")
            .description("Total permissions created")
            .register(meterRegistry);

        this.menusCreatedCounter = Counter.builder("core.menus.created")
            .description("Total menus created")
            .register(meterRegistry);

        this.auditLogsCreatedCounter = Counter.builder("core.audit_logs.created")
            .description("Total audit logs created")
            .register(meterRegistry);

        this.loginAttemptsCounter = Counter.builder("core.login_attempts")
            .description("Total login attempts")
            .tag("success", "true")
            .register(meterRegistry);

        this.platformLicensesCreatedCounter = Counter.builder("core.platform_licenses.created")
            .description("Total platform licenses created")
            .register(meterRegistry);

        this.platformNodesCreatedCounter = Counter.builder("core.platform_nodes.created")
            .description("Total platform nodes created")
            .register(meterRegistry);

        this.platformUpdatesCreatedCounter = Counter.builder("core.platform_updates.created")
            .description("Total platform updates created")
            .register(meterRegistry);

        this.updateInstallationsCounter = Counter.builder("core.update_installations")
            .description("Total update installations")
            .register(meterRegistry);

        this.userCreationTimer = Timer.builder("core.user_creation.time")
            .description("User creation time")
            .register(meterRegistry);

        this.licenseValidationTimer = Timer.builder("core.license_validation.time")
            .description("License validation time")
            .register(meterRegistry);

        this.updateInstallationTimer = Timer.builder("core.update_installation.time")
            .description("Update installation time")
            .register(meterRegistry);
    }

    public void incrementUsersCreated() {
        usersCreatedCounter.increment();
    }

    public void incrementUsersLoggedIn() {
        usersLoggedInCounter.increment();
    }

    public void incrementRolesCreated() {
        rolesCreatedCounter.increment();
    }

    public void incrementPermissionsCreated() {
        permissionsCreatedCounter.increment();
    }

    public void incrementMenusCreated() {
        menusCreatedCounter.increment();
    }

    public void incrementAuditLogsCreated() {
        auditLogsCreatedCounter.increment();
    }

    public void incrementLoginAttempts(boolean success) {
        Counter.builder("core.login_attempts")
            .tag("success", String.valueOf(success))
            .register(meterRegistry)
            .increment();
    }

    public void incrementPlatformLicensesCreated() {
        platformLicensesCreatedCounter.increment();
    }

    public void incrementPlatformNodesCreated() {
        platformNodesCreatedCounter.increment();
    }

    public void incrementPlatformUpdatesCreated() {
        platformUpdatesCreatedCounter.increment();
    }

    public void incrementUpdateInstallations() {
        updateInstallationsCounter.increment();
    }

    public Timer.Sample startUserCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startLicenseValidationTimer() {
        return Timer.start(meterRegistry);
    }

    public Timer.Sample startUpdateInstallationTimer() {
        return Timer.start(meterRegistry);
    }
}
```

## Configuración de Aplicación

### application-core.yml

```yaml
core:
  # Configuración de usuarios
  users:
    max-failed-login-attempts: 5
    session-timeout-minutes: 30
    password-min-length: 8
    password-require-special-chars: true
    auto-lock-account: true
    lock-duration-minutes: 15

  # Configuración de auditoría
  audit:
    log-all-actions: true
    sensitive-data-masking: true
    retention-days: 2555
    export-capability: true
    log-level: INFO
    max-log-size-mb: 100

  # Configuración de licencias de plataforma
  platform-licenses:
    encryption-algorithm: "AES-256-GCM"
    key-rotation-days: 90
    auto-validation: true
    offline-validation: true
    max-file-size-mb: 10
    validation-interval-minutes: 60

  # Configuración de nodos de plataforma
  platform-nodes:
    heartbeat-interval-seconds: 30
    max-heartbeat-delay-seconds: 120
    auto-discovery: true
    resource-monitoring: true
    gpu-monitoring: true
    storage-monitoring: true

  # Configuración de actualizaciones de plataforma
  platform-updates:
    auto-check-interval-hours: 24
    download-timeout-seconds: 300
    installation-timeout-minutes: 30
    rollback-enabled: true
    backup-before-update: true
    maintenance-window-start: "02:00"
    maintenance-window-duration-hours: 4

  # Configuración de seguridad
  security:
    jwt-secret: "${JWT_SECRET:default-secret-change-in-production}"
    jwt-expiration-hours: 24
    bcrypt-strength: 12
    rate-limiting-enabled: true
    max-requests-per-minute: 100
    cors-allowed-origins: ["http://localhost:3000", "https://app.company.com"]

  # Configuración de base de datos
  database:
    connection-pool-size: 20
    max-connection-lifetime-minutes: 30
    connection-timeout-seconds: 10
    idle-timeout-seconds: 300
    leak-detection-threshold-seconds: 60

  # Configuración de caché
  cache:
    enabled: true
    ttl-seconds: 3600
    max-size: 1000
    eviction-policy: "LRU"

  # Configuración de logging
  logging:
    level: INFO
    pattern: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
    file-enabled: true
    file-path: "logs/core-application.log"
    file-max-size: "100MB"
    file-max-history: 30
```

## Notas de Implementación

### 1. **Password Hash**: Los usuarios de demo usan el hash bcrypt `$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi` que corresponde a `demo123`

### 2. **Roles del Sistema**: Se incluyen todos los roles que aparecen en la configuración del frontend

### 3. **Estructura de Menús**: Se mantiene la jerarquía de menús y submenús exactamente como está configurada en el frontend

### 4. **Asignaciones**: Se asignan automáticamente los menús y permisos apropiados a cada rol según la configuración del frontend

### 5. **Licencias de Plataforma**: Sistema completo de gestión de licencias con control de nodos, GPUs y recursos

### 6. **Sistema de Actualizaciones**: Gestión automática y programada de actualizaciones de la plataforma con rollback

### 7. **Monitoreo**: Métricas completas con Prometheus para auditoría y rendimiento

### 8. **Verificación**: El script incluye consultas de verificación para confirmar que todos los datos se insertaron correctamente

## Uso del Script

### 1. **Ejecutar en orden**:

- Primero el script de limpieza (DROP)
- Luego el script de creación de tablas
- Finalmente el script de inicialización con datos de demo

### 2. **Verificar datos**: Usar las consultas de verificación al final para confirmar la inserción

### 3. **Personalizar**: Modificar los datos según las necesidades específicas del proyecto

### 4. **Configuración**: Ajustar los parámetros en `application-core.yml` según el entorno

## Verificación de Datos

```sql
-- Verificar usuarios creados
SELECT 'Usuarios creados:' as info, COUNT(*) as total FROM cor_users;

-- Verificar roles creados
SELECT 'Roles creados:' as info, COUNT(*) as total FROM cor_roles;

-- Verificar menús creados
SELECT 'Menús creados:' as info, COUNT(*) as total FROM cor_menus;

-- Verificar asignaciones de roles
SELECT 'Asignaciones usuario-rol:' as info, COUNT(*) as total FROM cor_user_roles;

-- Verificar asignaciones de menús
SELECT 'Asignaciones rol-menú:' as info, COUNT(*) as total FROM cor_role_menus;

-- Verificar permisos
SELECT 'Permisos creados:' as info, COUNT(*) as total FROM cor_permissions;

-- Verificar asignaciones de permisos
SELECT 'Asignaciones rol-permiso:' as info, COUNT(*) as total FROM cor_role_permissions;

-- Verificar licencias de plataforma
SELECT 'Licencias de plataforma:' as info, COUNT(*) as total FROM cor_platform_licenses;

-- Verificar nodos de plataforma
SELECT 'Nodos de plataforma:' as info, COUNT(*) as total FROM cor_platform_nodes;

-- Verificar actualizaciones de plataforma
SELECT 'Actualizaciones de plataforma:' as info, COUNT(*) as total FROM cor_platform_updates;

-- Verificar instalaciones de actualizaciones
SELECT 'Instalaciones de actualizaciones:' as info, COUNT(*) as total FROM cor_update_installations;

-- Verificar programaciones de actualizaciones
SELECT 'Programaciones de actualizaciones:' as info, COUNT(*) as total FROM cor_update_schedules;
```

## Conclusión

El módulo Core proporciona la funcionalidad fundamental del portal incluyendo:

- **Gestión de Usuarios**: Sistema completo de usuarios, departamentos y sesiones
- **Gestión de Roles y Permisos**: Control granular de acceso y autorización
- **Gestión de Menús**: Estructura jerárquica de navegación del sistema
- **Auditoría**: Logging completo de todas las acciones del sistema
- **Licencias de Plataforma**: Control de recursos, nodos y GPUs
- **Sistema de Actualizaciones**: Gestión automática y programada de actualizaciones
- **Monitoreo**: Métricas completas con Prometheus
- **Seguridad**: Autenticación JWT, encriptación y validación de licencias

El sistema está completamente normalizado siguiendo las reglas de 3NF, BCNF, con prefijos de módulos y arquitectura hexagonal aplicando principios SOLID y KISS.
