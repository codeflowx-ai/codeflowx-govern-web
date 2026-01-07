# Scripts de Base de Datos - Conformity Declaration

Este directorio contiene todos los scripts SQL necesarios para configurar la base de datos del módulo **Conformity Declaration** (Declaración de Conformidad).

## 📋 Contenido

1. **01_create_indexes.sql** - Índices y claves únicas
2. **02_create_triggers.sql** - Triggers de validación y actualización automática
3. **03_create_functions.sql** - Funciones almacenadas
4. **04_create_views.sql** - Vistas para consultas complejas
5. **05_create_procedures.sql** - Procedimientos almacenados

## 🚀 Instalación

### Orden de Ejecución

Los scripts deben ejecutarse en el siguiente orden:

```bash
# 1. Índices y constraints (JPA puede crear esto automáticamente con ddl-auto=update)
psql -U nocode -d nocode -f 01_create_indexes.sql

# 2. Triggers
psql -U nocode -d nocode -f 02_create_triggers.sql

# 3. Funciones
psql -U nocode -d nocode -f 03_create_functions.sql

# 4. Vistas
psql -U nocode -d nocode -f 04_create_views.sql

# 5. Procedimientos
psql -U nocode -d nocode -f 05_create_procedures.sql
```

### Ejecución en una sola transacción

```bash
psql -U nocode -d nocode << EOF
\i 01_create_indexes.sql
\i 02_create_triggers.sql
\i 03_create_functions.sql
\i 04_create_views.sql
\i 05_create_procedures.sql
EOF
```

## ⚙️ Configuración JPA

### Actualización Automática del Schema

JPA puede crear automáticamente las tablas, índices y constraints definidos en las anotaciones `@Table` de las entidades.

**Configuración en `application.yml`:**

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update  # o 'validate' en producción
```

**Qué crea JPA automáticamente:**
- ✅ Tablas (`@Entity`, `@Table`)
- ✅ Columnas (`@Column`)
- ✅ Índices (`@Index` en `@Table`)
- ✅ Constraints únicos (`@UniqueConstraint` en `@Table`)
- ✅ Foreign Keys (`@JoinColumn`, `@ManyToOne`, etc.)

**Qué NO crea JPA automáticamente:**
- ❌ Triggers (deben ejecutarse manualmente: `02_create_triggers.sql`)
- ❌ Funciones (deben ejecutarse manualmente: `03_create_functions.sql`)
- ❌ Vistas (deben ejecutarse manualmente: `04_create_views.sql`)
- ❌ Procedimientos (deben ejecutarse manualmente: `05_create_procedures.sql`)

## 📊 Estructura de la Tabla

### Tabla Principal: `GOVCONFORMITYDECLARATIONS`

La tabla se crea automáticamente por JPA con la siguiente estructura:

- **PK**: `IDX_DECLARATION` (BIGINT, auto-incremental)
- **FK**: `ASSESSMENT_ID` → `COMPLIANCEASSESSMENTS.IDXCOMPLIANCEASSESSMENT`
- **Índices**: Ver `01_create_indexes.sql`
- **Constraints**: Ver `01_create_indexes.sql`

## 🔍 Verificación

### Verificar que los índices se crearon:

```sql
SELECT indexname, indexdef
FROM pg_indexes
WHERE tablename = 'govconformitydeclarations'
ORDER BY indexname;
```

### Verificar que los triggers se crearon:

```sql
SELECT trigger_name, event_manipulation, event_object_table
FROM information_schema.triggers
WHERE event_object_table = 'govconformitydeclarations';
```

### Verificar que las funciones se crearon:

```sql
SELECT routine_name, routine_type
FROM information_schema.routines
WHERE routine_schema = 'public'
  AND routine_name LIKE '%conformity%declaration%'
ORDER BY routine_name;
```

### Verificar que las vistas se crearon:

```sql
SELECT table_name, view_definition
FROM information_schema.views
WHERE table_schema = 'public'
  AND table_name LIKE 'v_%conformity%'
ORDER BY table_name;
```

### Verificar que los procedimientos se crearon:

```sql
SELECT routine_name
FROM information_schema.routines
WHERE routine_schema = 'public'
  AND routine_type = 'PROCEDURE'
  AND routine_name LIKE '%conformity%declaration%'
ORDER BY routine_name;
```

## 🔄 Actualizaciones y Migraciones

### Agregar nuevos índices

1. Agregar la anotación `@Index` en la entidad JPA
2. Ejecutar el script SQL manualmente o dejar que JPA lo cree (si `ddl-auto=update`)
3. Documentar en `01_create_indexes.sql`

### Agregar nuevos triggers

1. Crear la función en `02_create_triggers.sql`
2. Crear el trigger en `02_create_triggers.sql`
3. Ejecutar el script manualmente

### Agregar nuevas funciones/vistas/procedimientos

1. Agregar al script correspondiente (`03_`, `04_`, o `05_`)
2. Ejecutar el script manualmente

## ⚠️ Notas Importantes

1. **Producción**: En producción, usar `ddl-auto: validate` y ejecutar los scripts manualmente
2. **Desarrollo**: En desarrollo, `ddl-auto: update` es útil pero los triggers/funciones/vistas/procedimientos deben ejecutarse manualmente
3. **Backup**: Siempre hacer backup antes de ejecutar scripts en producción
4. **Transacciones**: Los scripts están diseñados para ejecutarse en transacciones individuales
5. **Idempotencia**: Los scripts usan `CREATE OR REPLACE` cuando es posible para ser idempotentes

## 📚 Referencias

- [Documentación JPA - Schema Generation](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#schema-generation)
- [PostgreSQL - CREATE INDEX](https://www.postgresql.org/docs/current/sql-createindex.html)
- [PostgreSQL - CREATE TRIGGER](https://www.postgresql.org/docs/current/sql-createtrigger.html)
- [PostgreSQL - CREATE FUNCTION](https://www.postgresql.org/docs/current/sql-createfunction.html)
- [PostgreSQL - CREATE VIEW](https://www.postgresql.org/docs/current/sql-createview.html)
- [PostgreSQL - CREATE PROCEDURE](https://www.postgresql.org/docs/current/sql-createprocedure.html)

## 🐛 Troubleshooting

### Error: "relation already exists"

Los scripts usan `CREATE OR REPLACE` cuando es posible. Si aparece este error, puede ser necesario usar `DROP` primero.

### Error: "permission denied"

Asegúrate de tener permisos suficientes en la base de datos:

```sql
GRANT ALL PRIVILEGES ON DATABASE nocode TO nocode;
GRANT ALL PRIVILEGES ON SCHEMA public TO nocode;
```

### JPA no crea índices automáticamente

Verifica que:
1. `ddl-auto: update` está configurado
2. Las anotaciones `@Index` están correctamente definidas en `@Table`
3. Los nombres de columnas en `columnList` coinciden exactamente con los nombres en `@Column(name = "...")`
