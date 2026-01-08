# Scripts SQL para FRIA

Este directorio contiene los scripts SQL necesarios para la configuración de la base de datos del módulo FRIA.

## 📋 Índice de Scripts

1. **01_create_indexes.sql** - Creación de índices para optimizar consultas
2. **02_create_unique_constraints.sql** - Constraints únicos para integridad de datos
3. **03_create_triggers.sql** - Triggers para auditoría, validación y lógica de negocio
4. **04_create_functions.sql** - Funciones para cálculos y validaciones
5. **05_create_stored_procedures.sql** - Procedimientos almacenados para operaciones complejas

## 🚀 Instalación

### Orden de Ejecución

Los scripts deben ejecutarse en el siguiente orden:

```bash
# 1. Índices (requiere que la tabla exista)
psql -U usuario -d database -f 01_create_indexes.sql

# 2. Constraints únicos (requiere que la tabla exista)
psql -U usuario -d database -f 02_create_unique_constraints.sql

# 3. Triggers (requiere funciones y tabla de auditoría)
psql -U usuario -d database -f 03_create_triggers.sql

# 4. Funciones (requiere que la tabla exista)
psql -U usuario -d database -f 04_create_functions.sql

# 5. Procedimientos almacenados (requiere funciones)
psql -U usuario -d database -f 05_create_stored_procedures.sql
```

### Ejecución Automática con JPA

Si se usa `hibernate.hbm2ddl.auto=update` o `validate`, JPA creará automáticamente:

- ✅ Índices definidos en `@Table(indexes = {...})`
- ✅ Constraints únicos definidos en `@Table(uniqueConstraints = {...})`
- ✅ Constraints únicos en columnas con `@Column(unique = true)`

**NOTA:** Los triggers, funciones y procedimientos almacenados **NO** se crean automáticamente y deben ejecutarse manualmente.

## 📝 Descripción de Scripts

### 01_create_indexes.sql

Crea índices simples y compuestos para optimizar consultas frecuentes:

- Índices en FKs (IDXPROJECT, IDXUSER)
- Índices en campos de fecha (FRIACREATEDAT, FRIAUPDATEDAT)
- Índices en campos booleanos (FRIANOTIFIED, FRIAAPPROVED, FRIAART27COMPLIANT)
- Índices compuestos para consultas por proyecto y estado

### 02_create_unique_constraints.sql

Define constraints únicos:

- `UK_FRIA_UUID` - Garantiza unicidad del UUID
- `UK_FRIA_NOTIFICATION_ID` - Garantiza unicidad del ID de notificación

### 03_create_triggers.sql

Crea triggers para:

- **Actualización automática de timestamps** - Actualiza `FRIAUPDATEDAT` en UPDATE
- **Validación de notificación** - Valida que notificación tenga ID y fecha
- **Validación de aprobación** - Valida que aprobación tenga usuario y fecha
- **Validación de compliance** - Valida que compliance tenga score >= 1.00
- **Auditoría de cambios** - Registra cambios importantes en tabla de auditoría

### 04_create_functions.sql

Funciones para:

- `calculate_fria_completeness_score()` - Calcula score de completitud
- `validate_fria_art27_compliance()` - Valida compliance Art. 27
- `get_latest_fria_by_project()` - Obtiene última FRIA de un proyecto
- `count_frias_by_project()` - Cuenta FRIAs por proyecto
- `get_fria_statistics_by_project()` - Estadísticas agregadas por proyecto
- `can_create_new_fria()` - Verifica si se puede crear nueva FRIA

### 05_create_stored_procedures.sql

Procedimientos almacenados para:

- `update_fria_completeness_score()` - Actualiza score de completitud
- `notify_fria_authority()` - Marca FRIA como notificada
- `approve_fria()` - Marca FRIA como aprobada
- `create_fria_version()` - Crea nueva versión de FRIA

## 🔧 Uso desde Java

### Llamar Funciones

```java
@Query(value = "SELECT calculate_fria_completeness_score(:friaId)", nativeQuery = true)
BigDecimal calculateCompletenessScore(@Param("friaId") Long friaId);
```

### Llamar Procedimientos Almacenados

```java
@Procedure("update_fria_completeness_score")
void updateCompletenessScore(@Param("p_fria_id") Long friaId);
```

## ⚠️ Consideraciones

1. **Backup antes de ejecutar** - Siempre hacer backup de la base de datos antes de ejecutar scripts
2. **Entorno de pruebas** - Probar primero en entorno de desarrollo/staging
3. **Permisos** - Asegurar que el usuario tiene permisos para crear índices, triggers, funciones y procedimientos
4. **Rendimiento** - Los índices mejoran consultas pero ralentizan INSERT/UPDATE
5. **Mantenimiento** - Revisar periódicamente el uso de índices y eliminar los no utilizados

## 📚 Referencias

- [PostgreSQL Documentation - Indexes](https://www.postgresql.org/docs/current/indexes.html)
- [PostgreSQL Documentation - Triggers](https://www.postgresql.org/docs/current/triggers.html)
- [PostgreSQL Documentation - Functions](https://www.postgresql.org/docs/current/functions.html)
- [PostgreSQL Documentation - Stored Procedures](https://www.postgresql.org/docs/current/plpgsql.html)

