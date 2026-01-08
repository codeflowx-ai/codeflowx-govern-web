# 📁 SCRIPTS DE BASE DE DATOS - MÓDULO CLASIFICACIÓN

**Módulo:** Compliance - Classification (Art. 6 + Anexo III EU AI Act)
**Fecha:** Diciembre 2025

---

## 📋 ÍNDICE DE SCRIPTS

### 1. **classification_database_triggers.sql**
Scripts de triggers de base de datos para validaciones y automatizaciones.

**Triggers incluidos:**
- `trigger_validate_annex_iii_categories` - Valida formato JSONB de categorías
- `trigger_update_classification_date` - Actualiza fecha de clasificación automáticamente
- `trigger_validate_classification_coherence` - Valida coherencia de datos de clasificación
- `trigger_validate_art5_before_classification` - Valida Art. 5 antes de clasificar
- `trigger_update_annex_iii_category_timestamp` - Actualiza timestamps en categorías
- `trigger_validate_category_hierarchy` - Valida jerarquía de categorías

**Uso:**
```sql
\i classification_database_triggers.sql
```

---

### 2. **classification_database_functions.sql**
Funciones de base de datos para consultas y validaciones.

**Funciones incluidas:**
- `get_project_main_category(BIGINT)` - Obtiene categoría principal de un proyecto
- `get_project_subcategories(BIGINT)` - Obtiene subcategorías de un proyecto
- `is_project_in_category(BIGINT, VARCHAR)` - Valida si proyecto está en categoría
- `count_projects_by_category(VARCHAR)` - Cuenta proyectos por categoría
- `get_classification_statistics(TIMESTAMP, TIMESTAMP)` - Estadísticas de clasificaciones
- `validate_annex_iii_jsonb_format(JSONB)` - Valida formato JSONB
- `get_category_name_by_code(VARCHAR)` - Obtiene nombre de categoría
- `get_subcategories_by_category(VARCHAR)` - Obtiene subcategorías de categoría

**Uso:**
```sql
\i classification_database_functions.sql

-- Ejemplo de uso
SELECT get_project_main_category(1001);
SELECT * FROM get_subcategories_by_category('III.5');
```

---

### 3. **classification_database_procedures.sql**
Procedimientos almacenados para operaciones complejas.

**Procedimientos incluidos:**
- `reclasify_project(BIGINT, VARCHAR, TEXT[], VARCHAR)` - Reclasifica proyecto
- `unclassify_project(BIGINT)` - Desclasifica proyecto
- `migrate_old_category_format()` - Migra formato antiguo de categorías
- `validate_and_fix_classification_data()` - Valida y corrige inconsistencias
- `generate_classification_report(TIMESTAMP, TIMESTAMP, OUT JSONB)` - Genera reporte

**Uso:**
```sql
\i classification_database_procedures.sql

-- Ejemplo de uso
CALL reclasify_project(1001, 'III.5', ARRAY['III.5.b', 'III.5.c'], 'user@example.com');
CALL unclassify_project(1001);
```

---

## 🚀 INSTALACIÓN

### Orden de Ejecución

1. **Primero:** Ejecutar `classification_database_functions.sql`
   - Las funciones pueden ser usadas por triggers y procedimientos

2. **Segundo:** Ejecutar `classification_database_triggers.sql`
   - Los triggers usan las funciones definidas

3. **Tercero:** Ejecutar `classification_database_procedures.sql`
   - Los procedimientos usan funciones y pueden ser llamados desde la aplicación

### Ejemplo de Instalación Completa

```sql
-- Conectar a la base de datos
\c governance

-- Ejecutar scripts en orden
\i classification_database_functions.sql
\i classification_database_triggers.sql
\i classification_database_procedures.sql

-- Verificar instalación
\df get_project_main_category
\df validate_annex_iii_jsonb_format
\dt+ PRJPROJECTS
```

---

## 📝 NOTAS IMPORTANTES

### Permisos Requeridos

Los scripts requieren permisos de:
- `CREATE FUNCTION`
- `CREATE TRIGGER`
- `CREATE PROCEDURE`
- `EXECUTE` en funciones y procedimientos

### Compatibilidad

- **PostgreSQL:** 14+
- **Extensiones requeridas:** `plpgsql` (incluida por defecto)

### Rollback

Para desinstalar todos los objetos creados:

```sql
-- Eliminar triggers
DROP TRIGGER IF EXISTS trigger_validate_annex_iii_categories ON PRJPROJECTS;
DROP TRIGGER IF EXISTS trigger_update_classification_date ON PRJPROJECTS;
DROP TRIGGER IF EXISTS trigger_validate_classification_coherence ON PRJPROJECTS;
DROP TRIGGER IF EXISTS trigger_validate_art5_before_classification ON PRJPROJECTS;
DROP TRIGGER IF EXISTS trigger_update_annex_iii_category_timestamp ON ANNANNEXIIICATEGORIES;
DROP TRIGGER IF EXISTS trigger_validate_category_hierarchy ON ANNANNEXIIICATEGORIES;

-- Eliminar funciones
DROP FUNCTION IF EXISTS validate_annex_iii_categories_jsonb();
DROP FUNCTION IF EXISTS update_classification_date();
DROP FUNCTION IF EXISTS validate_classification_coherence();
DROP FUNCTION IF EXISTS validate_art5_before_classification();
DROP FUNCTION IF EXISTS update_annex_iii_category_timestamp();
DROP FUNCTION IF EXISTS validate_category_hierarchy();
DROP FUNCTION IF EXISTS get_project_main_category(BIGINT);
DROP FUNCTION IF EXISTS get_project_subcategories(BIGINT);
DROP FUNCTION IF EXISTS is_project_in_category(BIGINT, VARCHAR);
DROP FUNCTION IF EXISTS count_projects_by_category(VARCHAR);
DROP FUNCTION IF EXISTS get_classification_statistics(TIMESTAMP, TIMESTAMP);
DROP FUNCTION IF EXISTS validate_annex_iii_jsonb_format(JSONB);
DROP FUNCTION IF EXISTS get_category_name_by_code(VARCHAR);
DROP FUNCTION IF EXISTS get_subcategories_by_category(VARCHAR);

-- Eliminar procedimientos
DROP PROCEDURE IF EXISTS reclasify_project(BIGINT, VARCHAR, TEXT[], VARCHAR);
DROP PROCEDURE IF EXISTS unclassify_project(BIGINT);
DROP PROCEDURE IF EXISTS migrate_old_category_format();
DROP PROCEDURE IF EXISTS validate_and_fix_classification_data();
DROP PROCEDURE IF EXISTS generate_classification_report(TIMESTAMP, TIMESTAMP, OUT JSONB);
```

---

## 🔍 VERIFICACIÓN

### Verificar Triggers

```sql
SELECT
    trigger_name,
    event_object_table,
    action_timing,
    event_manipulation
FROM information_schema.triggers
WHERE trigger_schema = 'public'
  AND event_object_table IN ('PRJPROJECTS', 'ANNANNEXIIICATEGORIES')
ORDER BY event_object_table, trigger_name;
```

### Verificar Funciones

```sql
SELECT
    routine_name,
    routine_type,
    data_type
FROM information_schema.routines
WHERE routine_schema = 'public'
  AND routine_name LIKE '%classification%'
  OR routine_name LIKE '%annex%'
ORDER BY routine_name;
```

### Verificar Procedimientos

```sql
SELECT
    routine_name,
    routine_type
FROM information_schema.routines
WHERE routine_schema = 'public'
  AND routine_type = 'PROCEDURE'
  AND (routine_name LIKE '%classif%' OR routine_name LIKE '%migrate%')
ORDER BY routine_name;
```

---

## 📚 REFERENCIAS

- `DEVELOPER_GUIDE_BACKEND.md` - Guía de desarrollo backend
- `DATABASE_SCHEMA.md` - Esquema de base de datos
- `ARCHITECTURE_FLOWS.md` - Arquitectura y flujos

---

**Última Actualización:** Diciembre 2025
**Versión:** 1.0
