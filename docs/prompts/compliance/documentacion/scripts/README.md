# 📁 Scripts SQL - Technical Documentation

**Módulo:** Compliance - Technical Documentation
**Tabla:** `GOVAIACTTECHNICALDOCS`
**Base de Datos:** PostgreSQL
**Fecha:** Diciembre 2025

---

## 📋 Descripción

Este directorio contiene los scripts SQL necesarios para la configuración completa de la base de datos del módulo de Technical Documentation, incluyendo:

- ✅ Índices y constraints únicos
- ✅ Triggers para actualización automática
- ✅ Funciones para cálculos y validaciones
- ✅ Vistas para consultas frecuentes

---

## 📚 Scripts Disponibles

### 1. `01_create_indexes_and_constraints.sql`

**Propósito:** Crear índices y constraints únicos definidos en la entidad JPA.

**Contenido:**
- 11 índices (simples y compuestos) para optimizar consultas
- 1 constraint único (`uk_techdocs_entity`) que garantiza una sola documentación por entidad

**Índices creados:**
- `idx_techdocs_entity` (compuesto): ENTITY_TYPE, ENTITY_ID
- `idx_techdocs_entity_type`: ENTITY_TYPE
- `idx_techdocs_entity_id`: ENTITY_ID
- `idx_techdocs_status`: STATUS
- `idx_techdocs_system_name`: SYSTEM_NAME
- `idx_techdocs_version`: VERSION
- `idx_techdocs_created_at`: CREATED_AT
- `idx_techdocs_updated_at`: UPDATED_AT
- `idx_techdocs_generated_at`: GENERATED_AT
- `idx_techdocs_status_entity` (compuesto): STATUS, ENTITY_TYPE
- `idx_techdocs_status_created` (compuesto): STATUS, CREATED_AT

**Uso:**
```bash
psql -U usuario -d nombre_base_datos -f 01_create_indexes_and_constraints.sql
```

---

### 2. `02_create_triggers.sql`

**Propósito:** Crear triggers para actualización automática de campos y validaciones.

**Contenido:**
- Trigger `trg_techdocs_update_updated_at`: Actualiza UPDATED_AT automáticamente antes de UPDATE
- Trigger `trg_techdocs_validate_entity_unique`: Valida unicidad de ENTITY_TYPE + ENTITY_ID (redundante con constraint, pero útil para mensajes personalizados)
- Trigger `trg_techdocs_set_created_at`: Establece CREATED_AT y UPDATED_AT si no están definidos en INSERT

**Funciones:**
- `update_techdocs_updated_at()`: Actualiza UPDATED_AT
- `validate_techdocs_entity_unique()`: Valida unicidad
- `set_techdocs_created_at()`: Establece timestamps en INSERT

**Uso:**
```bash
psql -U usuario -d nombre_base_datos -f 02_create_triggers.sql
```

**Nota:** Los triggers proporcionan una capa adicional de seguridad a nivel de BD, aunque la entidad JPA ya maneja estos campos con `@PrePersist` y `@PreUpdate`.

---

### 3. `03_create_functions.sql`

**Propósito:** Crear funciones útiles para operaciones sobre documentación técnica.

**Funciones creadas:**

#### `calculate_techdocs_completeness_score(p_doc_id BIGINT)`
- **Retorna:** `NUMERIC(5,4)` (0.00 - 1.00)
- **Descripción:** Calcula el score de completitud basado en las 11 secciones del Anexo IV
- **Uso:**
```sql
SELECT calculate_techdocs_completeness_score(1001);
```

#### `is_techdocs_complete(p_doc_id BIGINT)`
- **Retorna:** `BOOLEAN`
- **Descripción:** Retorna TRUE si la documentación está 100% completa (score = 1.00)
- **Uso:**
```sql
SELECT is_techdocs_complete(1001);
```

#### `get_techdocs_by_entity(p_entity_type VARCHAR, p_entity_id BIGINT)`
- **Retorna:** Tabla con información resumida de documentación
- **Descripción:** Obtiene documentación por tipo e ID de entidad
- **Uso:**
```sql
SELECT * FROM get_techdocs_by_entity('MODEL', 1001);
```

#### `get_incomplete_techdocs(p_entity_type VARCHAR, p_min_score NUMERIC)`
- **Retorna:** Tabla con documentaciones incompletas
- **Descripción:** Lista documentaciones incompletas, opcionalmente filtradas
- **Uso:**
```sql
-- Todas las incompletas
SELECT * FROM get_incomplete_techdocs();

-- Solo modelos incompletos
SELECT * FROM get_incomplete_techdocs('MODEL');

-- Solo con score >= 0.5
SELECT * FROM get_incomplete_techdocs(NULL, 0.5);
```

**Uso:**
```bash
psql -U usuario -d nombre_base_datos -f 03_create_functions.sql
```

---

### 4. `04_create_views.sql`

**Propósito:** Crear vistas para consultas frecuentes.

**Vistas creadas:**

#### `vw_techdocs_model_summary`
- **Descripción:** Resumen de documentaciones técnicas por modelo
- **Uso:**
```sql
SELECT * FROM vw_techdocs_model_summary WHERE model_id = 1001;
```

#### `vw_techdocs_pending`
- **Descripción:** Documentaciones incompletas con prioridad y días desde actualización
- **Uso:**
```sql
SELECT * FROM vw_techdocs_pending WHERE priority = 'CRITICAL';
```

#### `vw_techdocs_statistics_by_entity_type`
- **Descripción:** Estadísticas de completitud agrupadas por tipo de entidad
- **Uso:**
```sql
SELECT * FROM vw_techdocs_statistics_by_entity_type;
```

#### `vw_techdocs_approved_for_conformity`
- **Descripción:** Documentaciones aprobadas y completas, listas para evaluación de conformidad
- **Uso:**
```sql
SELECT * FROM vw_techdocs_approved_for_conformity;
```

**Uso:**
```bash
psql -U usuario -d nombre_base_datos -f 04_create_views.sql
```

---

## 🚀 Orden de Ejecución

Ejecutar los scripts en el siguiente orden:

1. **Primero:** `01_create_indexes_and_constraints.sql`
2. **Segundo:** `02_create_triggers.sql`
3. **Tercero:** `03_create_functions.sql`
4. **Cuarto:** `04_create_views.sql`

**Script completo (ejecutar todos):**
```bash
psql -U usuario -d nombre_base_datos -f 01_create_indexes_and_constraints.sql
psql -U usuario -d nombre_base_datos -f 02_create_triggers.sql
psql -U usuario -d nombre_base_datos -f 03_create_functions.sql
psql -U usuario -d nombre_base_datos -f 04_create_views.sql
```

O ejecutar todos en una sola transacción:
```bash
psql -U usuario -d nombre_base_datos << EOF
\i 01_create_indexes_and_constraints.sql
\i 02_create_triggers.sql
\i 03_create_functions.sql
\i 04_create_views.sql
EOF
```

---

## ✅ Verificación

Después de ejecutar los scripts, verificar que todo se creó correctamente:

### Verificar Índices
```sql
SELECT
    indexname,
    indexdef
FROM pg_indexes
WHERE tablename = 'govaiacttechnicaldocs'
ORDER BY indexname;
```

### Verificar Constraints Únicos
```sql
SELECT
    conname AS constraint_name,
    contype AS constraint_type,
    pg_get_constraintdef(oid) AS constraint_definition
FROM pg_constraint
WHERE conrelid = 'govaiacttechnicaldocs'::regclass
  AND contype = 'u';
```

### Verificar Triggers
```sql
SELECT
    trigger_name,
    event_manipulation,
    event_object_table,
    action_statement
FROM information_schema.triggers
WHERE event_object_table = 'govaiacttechnicaldocs'
ORDER BY trigger_name;
```

### Verificar Funciones
```sql
SELECT
    routine_name,
    routine_type,
    data_type AS return_type
FROM information_schema.routines
WHERE routine_schema = 'public'
  AND routine_name LIKE '%techdocs%'
ORDER BY routine_name;
```

### Verificar Vistas
```sql
SELECT
    table_name AS view_name,
    view_definition
FROM information_schema.views
WHERE table_schema = 'public'
  AND table_name LIKE '%techdocs%'
ORDER BY table_name;
```

---

## 🔄 Actualización Automática del Schema con JPA

Si se usa JPA/Hibernate con `ddl-auto: update` o `create`, los índices y constraints definidos en la entidad se crearán automáticamente. Sin embargo, es recomendable ejecutar estos scripts manualmente en producción para:

1. Mayor control sobre la creación
2. Versionamiento de cambios
3. Aplicación de scripts adicionales (triggers, funciones, vistas)

---

## ⚠️ Notas Importantes

1. **Backup:** Siempre hacer backup antes de ejecutar scripts en producción
2. **Permisos:** Asegurar que el usuario tenga permisos para crear índices, triggers, funciones y vistas
3. **Transacciones:** Los scripts están diseñados para ejecutarse en transacciones
4. **Rollback:** Si ocurre un error, se puede hacer rollback de la transacción
5. **Testing:** Probar primero en ambiente de desarrollo/staging

---

## 📚 Referencias

- **Entidad JPA:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/governance/AIActTechnicalDocumentation.java`
- **Developer Guide Backend:** `DEVELOPER_GUIDE_BACKEND.md`
- **Base de Datos:** PostgreSQL 12+

---

**Última actualización:** Diciembre 2025

