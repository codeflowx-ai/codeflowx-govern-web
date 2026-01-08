# 🔧 DEVELOPER GUIDE - EU REGISTRATION BACKEND

**Módulo:** Compliance - EU Registration  
**Fecha:** Diciembre 2025  
**Versión:** 1.0

---

## 📋 ÍNDICE

1. [Estructura de Base de Datos](#estructura-de-base-de-datos)
2. [Scripts SQL](#scripts-sql)
3. [Índices y Claves Únicas](#índices-y-claves-únicas)
4. [Triggers](#triggers)
5. [Funciones](#funciones)
6. [Vistas](#vistas)
7. [Procedimientos Almacenados](#procedimientos-almacenados)
8. [Configuración JPA](#configuración-jpa)

---

## 🗄️ ESTRUCTURA DE BASE DE DATOS

### Tabla: `REGEUREGISTRATIONS`

**Prefijo:** `REG`  
**Propósito:** Almacena registros de sistemas IA en la Base de Datos UE según Art. 49 y Anexo VIII del EU AI Act

**Campos Principales:**
- `IDXEUREGISTRATION` (PK, BIGSERIAL) - ID autonumérico
- `iduuid` (VARCHAR(36), UNIQUE) - UUID único
- `IDXPROJECT` (FK → PRJPROJECTS) - Proyecto asociado
- `REGREGISTRATIONTYPE` (VARCHAR(20)) - Tipo: STANDARD, SENSITIVE, NATIONAL
- `REGEUREGISTRATIONID` (VARCHAR(100), UNIQUE) - ID asignado por EU Database
- `REGSTATUS` (VARCHAR(20)) - Estado: DRAFT, PENDING, SUBMITTED, REGISTERED, REJECTED, ERROR
- `REGSUBMISSIONDATA` (TEXT/JSONB) - Datos de envío según Anexo VIII
- `REGISSENSITIVE` (BOOLEAN) - Sistema sensible (Art. 49.4)
- `REGISNATIONAL` (BOOLEAN) - Registro nacional (Art. 49.5)
- `IDXPMMPLAN` (FK → PMMPOSTMARKETMONITORINGPLANS) - Plan PMM asociado

---

## 📜 SCRIPTS SQL

Todos los scripts SQL están ubicados en:
```
docs/prompts/compliance/eu-registration/sql/
```

### Scripts Disponibles:

1. **`01_create_table.sql`** - Creación de tabla e índices
2. **`02_create_indexes.sql`** - Índices adicionales
3. **`03_create_triggers.sql`** - Triggers de auditoría y validación
4. **`04_create_functions.sql`** - Funciones de validación y utilidad
5. **`05_create_views.sql`** - Vistas para reportes
6. **`06_create_procedures.sql`** - Procedimientos almacenados
7. **`07_data_migration.sql`** - Scripts de migración de datos

---

## 🔑 ÍNDICES Y CLAVES ÚNICAS

### Índices Simples

```sql
-- Índices básicos (creados automáticamente por JPA)
CREATE INDEX idx_reg_project ON REGEUREGISTRATIONS(IDXPROJECT);
CREATE INDEX idx_reg_type ON REGEUREGISTRATIONS(REGREGISTRATIONTYPE);
CREATE INDEX idx_reg_status ON REGEUREGISTRATIONS(REGSTATUS);
CREATE INDEX idx_reg_euregid ON REGEUREGISTRATIONS(REGEUREGISTRATIONID);
CREATE INDEX idx_reg_sensitive ON REGEUREGISTRATIONS(REGISSENSITIVE);
CREATE INDEX idx_reg_national ON REGEUREGISTRATIONS(REGISNATIONAL);
CREATE INDEX idx_reg_uuid ON REGEUREGISTRATIONS(iduuid);
```

### Índices Compuestos

```sql
-- Índices compuestos para búsquedas frecuentes
CREATE INDEX idx_reg_project_status ON REGEUREGISTRATIONS(IDXPROJECT, REGSTATUS);
CREATE INDEX idx_reg_type_status ON REGEUREGISTRATIONS(REGREGISTRATIONTYPE, REGSTATUS);
```

### Índices de Fechas

```sql
-- Índices para ordenamiento por fechas
CREATE INDEX idx_reg_submission_date ON REGEUREGISTRATIONS(REGSUBMISSIONDATE);
CREATE INDEX idx_reg_registration_date ON REGEUREGISTRATIONS(REGREGISTRATIONDATE);
CREATE INDEX idx_reg_created_at ON REGEUREGISTRATIONS(REGCREATEDAT);
CREATE INDEX idx_reg_updated_at ON REGEUREGISTRATIONS(REGUPDATEDAT);
```

### Claves Únicas

```sql
-- Clave única para UUID
ALTER TABLE REGEUREGISTRATIONS ADD CONSTRAINT uk_reg_uuid UNIQUE (iduuid);

-- Clave única para EU Registration ID (puede ser NULL)
CREATE UNIQUE INDEX uk_reg_euregistrationid ON REGEUREGISTRATIONS(REGEUREGISTRATIONID) 
WHERE REGEUREGISTRATIONID IS NOT NULL;
```

---

## ⚡ TRIGGERS

### 1. Trigger de Auditoría (`trg_reg_audit`)

**Propósito:** Actualiza automáticamente `REGUPDATEDAT` en cada UPDATE

```sql
-- Ver script completo en: sql/03_create_triggers.sql
```

### 2. Trigger de Validación (`trg_reg_validate_status`)

**Propósito:** Valida transiciones de estado permitidas

```sql
-- Ver script completo en: sql/03_create_triggers.sql
```

### 3. Trigger de EU Registration ID (`trg_reg_eu_id_format`)

**Propósito:** Valida formato del EU Registration ID cuando se asigna

```sql
-- Ver script completo en: sql/03_create_triggers.sql
```

---

## 🔧 FUNCIONES

### 1. `fn_reg_validate_submission_data(jsonb_data, registration_type)`

**Propósito:** Valida que el JSON de submission data contenga todos los campos requeridos según el tipo de registro

```sql
-- Ver script completo en: sql/04_create_functions.sql
```

### 2. `fn_reg_get_latest_by_project(project_id)`

**Propósito:** Obtiene el último registro de un proyecto

```sql
-- Ver script completo en: sql/04_create_functions.sql
```

### 3. `fn_reg_count_by_status(status)`

**Propósito:** Cuenta registros por estado

```sql
-- Ver script completo en: sql/04_create_functions.sql
```

### 4. `fn_reg_calculate_retry_delay(attempts)`

**Propósito:** Calcula el delay exponencial para retry basado en número de intentos

```sql
-- Ver script completo en: sql/04_create_functions.sql
```

---

## 👁️ VISTAS

### 1. Vista: `v_reg_registrations_summary`

**Propósito:** Resumen de registros por proyecto y estado

```sql
-- Ver script completo en: sql/05_create_views.sql
```

### 2. Vista: `v_reg_pending_submissions`

**Propósito:** Lista de registros pendientes de envío

```sql
-- Ver script completo en: sql/05_create_views.sql
```

### 3. Vista: `v_reg_failed_submissions`

**Propósito:** Lista de registros que fallaron en el envío

```sql
-- Ver script completo en: sql/05_create_views.sql
```

---

## 📦 PROCEDIMIENTOS ALMACENADOS

### 1. `sp_reg_cleanup_old_drafts()`

**Propósito:** Limpia registros en estado DRAFT más antiguos de 90 días

```sql
-- Ver script completo en: sql/06_create_procedures.sql
```

### 2. `sp_reg_retry_failed_submissions()`

**Propósito:** Reintenta envíos fallidos que no han alcanzado el máximo de intentos

```sql
-- Ver script completo en: sql/06_create_procedures.sql
```

---

## ⚙️ CONFIGURACIÓN JPA

### Entidad JPA: `EuRegistration`

**Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/EuRegistration.java`

**Anotaciones de Índices:**

```java
@Table(
    name = "REGEUREGISTRATIONS",
    indexes = {
        @Index(name = "idx_reg_project", columnList = "IDXPROJECT"),
        @Index(name = "idx_reg_type", columnList = "REGREGISTRATIONTYPE"),
        @Index(name = "idx_reg_status", columnList = "REGSTATUS"),
        @Index(name = "idx_reg_euregid", columnList = "REGEUREGISTRATIONID"),
        @Index(name = "idx_reg_sensitive", columnList = "REGISSENSITIVE"),
        @Index(name = "idx_reg_national", columnList = "REGISNATIONAL"),
        @Index(name = "idx_reg_uuid", columnList = "iduuid"),
        @Index(name = "idx_reg_project_status", columnList = "IDXPROJECT,REGSTATUS"),
        @Index(name = "idx_reg_type_status", columnList = "REGREGISTRATIONTYPE,REGSTATUS"),
        @Index(name = "idx_reg_submission_date", columnList = "REGSUBMISSIONDATE"),
        @Index(name = "idx_reg_registration_date", columnList = "REGREGISTRATIONDATE"),
        @Index(name = "idx_reg_created_at", columnList = "REGCREATEDAT"),
        @Index(name = "idx_reg_updated_at", columnList = "REGUPDATEDAT"),
        @Index(name = "idx_reg_attempts", columnList = "REGATTEMPTS"),
        @Index(name = "idx_reg_pmm_plan", columnList = "IDXPMMPLAN")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_reg_uuid", columnNames = {"iduuid"}),
        @UniqueConstraint(name = "uk_reg_euregistrationid", columnNames = {"REGEUREGISTRATIONID"})
    }
)
```

### Configuración de JPA para Auto-Update Schema

En `application.yml`:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update  # o 'validate' en producción
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        use_sql_comments: true
```

**Nota:** Los triggers, funciones, vistas y procedimientos almacenados NO se crean automáticamente por JPA. Deben ejecutarse manualmente usando los scripts SQL proporcionados.

---

## 🚀 DESPLIEGUE

### Orden de Ejecución de Scripts

1. **Crear tabla e índices básicos:**
   ```bash
   psql -d nocode -f sql/01_create_table.sql
   ```

2. **Crear índices adicionales:**
   ```bash
   psql -d nocode -f sql/02_create_indexes.sql
   ```

3. **Crear triggers:**
   ```bash
   psql -d nocode -f sql/03_create_triggers.sql
   ```

4. **Crear funciones:**
   ```bash
   psql -d nocode -f sql/04_create_functions.sql
   ```

5. **Crear vistas:**
   ```bash
   psql -d nocode -f sql/05_create_views.sql
   ```

6. **Crear procedimientos:**
   ```bash
   psql -d nocode -f sql/06_create_procedures.sql
   ```

### Verificación

```sql
-- Verificar índices
SELECT indexname, indexdef 
FROM pg_indexes 
WHERE tablename = 'regeuregistrations';

-- Verificar triggers
SELECT trigger_name, event_manipulation, event_object_table
FROM information_schema.triggers
WHERE event_object_table = 'regeuregistrations';

-- Verificar funciones
SELECT routine_name, routine_type
FROM information_schema.routines
WHERE routine_schema = 'public'
AND routine_name LIKE 'fn_reg_%' OR routine_name LIKE 'sp_reg_%';

-- Verificar vistas
SELECT table_name
FROM information_schema.views
WHERE table_schema = 'public'
AND table_name LIKE 'v_reg_%';
```

---

## 📚 REFERENCIAS

- **Entidad JPA:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/EuRegistration.java`
- **Repositorio:** `codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/compliance/EuRegistrationRepository.java`
- **Business Service:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/EuRegistrationBusinessService.java`
- **Documentación Arquitectura:** `docs/prompts/compliance/INTEGRACION_BACKEND_EU_REGISTRATION.md`

---

**Última actualización:** Diciembre 2025


