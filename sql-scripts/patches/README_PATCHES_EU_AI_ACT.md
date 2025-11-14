# PATCHES EU AI ACT - GUÍA DE EJECUCIÓN
**Versión:** 1.0  
**Fecha:** 2 nov 2025  
**Autor:** Java Team

---

## 📋 CONTENIDO

Este directorio contiene los patches SQL para extender el schema de base de datos con campos requeridos por el EU AI Act.

### Patches Incluidos

| Patch | Archivo | Descripción | Artículos |
|-------|---------|-------------|-----------|
| **06** | `06_eu_ai_act_model_extensions.sql` | Extensión tabla MODMODELS (13 campos) | Art. 6, 11, 15, 51 |
| **07** | `07_eu_ai_act_project_extensions.sql` | Extensión tabla PRJPROJECTS (11 campos) | Art. 5, 6, 49, Anexos I-III |
| **08** | `08_eu_ai_act_evaluation_extensions.sql` | Extensión tabla GOVMODELEVALUATIONS (7 campos) | Art. 15.4, 15.5 |
| **09** | `09_eu_ai_act_immutable_logs_table.sql` | Tabla IMLIMMUTABLELOGS + triggers | Art. 19 |
| **10** | `10_eu_ai_act_fria_assessment_table.sql` | Tabla FRIAFUNDAMENTALRIGHTSASSESSMENTS + vista | Art. 27 |

---

## 🚀 EJECUCIÓN RÁPIDA

### Opción 1: Script Maestro (Recomendado)

```bash
cd /mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/sql-scripts/patches

# Configurar variables de entorno
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=codeflowx_govern
export DB_USER=postgres

# Ejecutar todos los patches
./00_EJECUTAR_PATCHES_EU_AI_ACT.sh
```

### Opción 2: Ejecución Manual

```bash
cd /mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/sql-scripts/patches

# Configurar conexión
DB_HOST=localhost
DB_PORT=5432
DB_NAME=codeflowx_govern
DB_USER=postgres

# Ejecutar patches uno por uno
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 06_eu_ai_act_model_extensions.sql
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 07_eu_ai_act_project_extensions.sql
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 08_eu_ai_act_evaluation_extensions.sql
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 09_eu_ai_act_immutable_logs_table.sql
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 10_eu_ai_act_fria_assessment_table.sql
```

---

## 🔍 VERIFICACIÓN POST-EJECUCIÓN

### 1. Verificar Columnas Añadidas

```sql
-- MODMODELS (13 columnas nuevas)
SELECT column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'modmodels' 
  AND column_name LIKE 'MODTECHNICAL%' 
   OR column_name LIKE 'MODACCURACY%'
   OR column_name LIKE 'MODISHIGHRISK%'
   OR column_name LIKE 'MODANNEXIII%'
   OR column_name LIKE 'MODGPAI%';

-- PRJPROJECTS (11 columnas nuevas)
SELECT column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'prjprojects' 
  AND column_name LIKE 'PRJISHIGHRISK%'
   OR column_name LIKE 'PRJANNEX%'
   OR column_name LIKE 'PRJPROHIBITED%'
   OR column_name LIKE 'PRJEUREG%';

-- GOVMODELEVALUATIONS (7 columnas nuevas)
SELECT column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'govmodelevaluations' 
  AND column_name LIKE 'EVALADVERSARIAL%'
   OR column_name LIKE 'EVALFEEDBACK%'
   OR column_name LIKE 'EVALCOMPLIANCE%';
```

### 2. Verificar Tablas Nuevas

```sql
-- Tabla IMLIMMUTABLELOGS
SELECT EXISTS (
    SELECT FROM information_schema.tables 
    WHERE table_name = 'imlimmutablelogs'
);

-- Tabla FRIAFUNDAMENTALRIGHTSASSESSMENTS
SELECT EXISTS (
    SELECT FROM information_schema.tables 
    WHERE table_name = 'friafundamentalrightsassessments'
);
```

### 3. Verificar Índices

```sql
-- Índices en MODMODELS
SELECT indexname, indexdef 
FROM pg_indexes 
WHERE tablename = 'modmodels' 
  AND indexname LIKE '%highrisk%' 
   OR indexname LIKE '%gpai%'
   OR indexname LIKE '%annexiii%';

-- Índices en PRJPROJECTS
SELECT indexname, indexdef 
FROM pg_indexes 
WHERE tablename = 'prjprojects' 
  AND indexname LIKE '%highrisk%' 
   OR indexname LIKE '%eureg%'
   OR indexname LIKE '%annexiii%';

-- Índices GIN (JSONB)
SELECT indexname, indexdef 
FROM pg_indexes 
WHERE indexdef LIKE '%GIN%' 
  AND (tablename = 'prjprojects' OR tablename = 'friafundamentalrightsassessments');
```

### 4. Verificar Triggers (CRÍTICO para Inmutabilidad)

```sql
-- Triggers en IMLIMMUTABLELOGS
SELECT trigger_name, event_manipulation, action_statement
FROM information_schema.triggers
WHERE event_object_table = 'imlimmutablelogs';

-- Debe mostrar:
-- - trigger_prevent_update (BEFORE UPDATE)
-- - trigger_prevent_delete (BEFORE DELETE)
```

### 5. Verificar Vista FRIA

```sql
-- Vista vw_fria_pending_approval
SELECT * FROM vw_fria_pending_approval LIMIT 1;
```

---

## 🧪 TESTING DE INTEGRIDAD

### Test 1: Intentar UPDATE en ImmutableLog (debe fallar)

```sql
-- Este comando DEBE fallar con mensaje de error
UPDATE IMLIMMUTABLELOGS SET IMLACTION = 'TEST' WHERE IDXIMMUTABLELOG = 1;

-- Resultado esperado:
-- ERROR: IMMUTABLE LOGS: UPDATE not allowed on IMLIMMUTABLELOGS table. Art. 19 EU AI Act.
```

### Test 2: Intentar DELETE en ImmutableLog (debe fallar)

```sql
-- Este comando DEBE fallar con mensaje de error
DELETE FROM IMLIMMUTABLELOGS WHERE IDXIMMUTABLELOG = 1;

-- Resultado esperado:
-- ERROR: IMMUTABLE LOGS: DELETE not allowed on IMLIMMUTABLELOGS table. Art. 19 EU AI Act.
```

### Test 3: Insertar en ImmutableLog (debe funcionar)

```sql
-- Este comando DEBE funcionar
INSERT INTO IMLIMMUTABLELOGS (
    iduuid, 
    IMLPREVIOUSHASH, 
    IMLCURRENTHASH, 
    IMLTIMESTAMP, 
    IMLTIMESTAMPEPOCH,
    IMLENTITYTYPE, 
    IMLENTITYID, 
    IMLACTION, 
    IMLUSERID, 
    IMLDATA,
    IMLCREATEDAT
) VALUES (
    gen_random_uuid()::text,
    '0000000000000000000000000000000000000000000000000000000000000000',
    'test_hash_1234567890abcdef1234567890abcdef1234567890abcdef12345678',
    CURRENT_TIMESTAMP,
    extract(epoch from CURRENT_TIMESTAMP)::bigint * 1000,
    'PROJECT',
    1,
    'CREATE',
    1,
    '{"test": "data"}',
    CURRENT_TIMESTAMP
);

-- Verificar
SELECT * FROM IMLIMMUTABLELOGS ORDER BY IDXIMMUTABLELOG DESC LIMIT 1;
```

---

## 📊 ESTADÍSTICAS POST-MIGRACIÓN

```sql
-- Contar campos nuevos por tabla
SELECT 
    'MODMODELS' as tabla,
    COUNT(*) as campos_nuevos
FROM information_schema.columns 
WHERE table_name = 'modmodels' 
  AND (column_name LIKE 'MODTECHNICAL%' 
    OR column_name LIKE 'MODACCURACY%'
    OR column_name LIKE 'MODISHIGHRISK%'
    OR column_name LIKE 'MODANNEXIII%'
    OR column_name LIKE 'MODGPAI%')

UNION ALL

SELECT 
    'PRJPROJECTS' as tabla,
    COUNT(*) as campos_nuevos
FROM information_schema.columns 
WHERE table_name = 'prjprojects' 
  AND (column_name LIKE 'PRJISHIGHRISK%'
    OR column_name LIKE 'PRJANNEX%'
    OR column_name LIKE 'PRJPROHIBITED%'
    OR column_name LIKE 'PRJEUREG%')

UNION ALL

SELECT 
    'GOVMODELEVALUATIONS' as tabla,
    COUNT(*) as campos_nuevos
FROM information_schema.columns 
WHERE table_name = 'govmodelevaluations' 
  AND (column_name LIKE 'EVALADVERSARIAL%'
    OR column_name LIKE 'EVALFEEDBACK%'
    OR column_name LIKE 'EVALCOMPLIANCE%');
```

---

## ⚠️ NOTAS IMPORTANTES

### Inmutabilidad de Logs
- La tabla `IMLIMMUTABLELOGS` es **APPEND-ONLY**
- Triggers previenen UPDATE y DELETE
- **NO INTENTAR desactivar estos triggers** (violaría Art. 19)

### Índices GIN (JSONB)
- Pueden tardar en crearse si hay datos existentes
- Mejoran búsquedas en campos JSONB significativamente
- Ocupan más espacio en disco

### Backward Compatibility
- Todos los campos nuevos son `nullable = true`
- No se rompen queries existentes
- Datos existentes permanecen intactos

### Performance
- Índices añadidos para campos de búsqueda frecuente
- Considerar particionamiento de `IMLIMMUTABLELOGS` si alto volumen
- Monitorear tamaño de índices GIN

---

## 🔄 ROLLBACK (Si Necesario)

### Opción 1: Rollback Completo

```bash
# Crear script de rollback
cd /mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/sql-scripts/patches

cat > 99_rollback_eu_ai_act.sql << 'EOF'
-- ROLLBACK PATCHES EU AI ACT

-- Drop triggers IMLIMMUTABLELOGS
DROP TRIGGER IF EXISTS trigger_prevent_update ON IMLIMMUTABLELOGS;
DROP TRIGGER IF EXISTS trigger_prevent_delete ON IMLIMMUTABLELOGS;
DROP FUNCTION IF EXISTS prevent_immutable_log_update();
DROP FUNCTION IF EXISTS prevent_immutable_log_delete();

-- Drop vista FRIA
DROP VIEW IF EXISTS vw_fria_pending_approval;

-- Drop tablas nuevas
DROP TABLE IF EXISTS FRIAFUNDAMENTALRIGHTSASSESSMENTS CASCADE;
DROP TABLE IF EXISTS IMLIMMUTABLELOGS CASCADE;

-- Drop columnas GOVMODELEVALUATIONS
ALTER TABLE GOVMODELEVALUATIONS DROP COLUMN IF EXISTS EVALCOMPLIANCESCORE;
ALTER TABLE GOVMODELEVALUATIONS DROP COLUMN IF EXISTS EVALFEEDBACKLOOPRESULTS;
ALTER TABLE GOVMODELEVALUATIONS DROP COLUMN IF EXISTS EVALFEEDBACKLOOPBIASSCORE;
ALTER TABLE GOVMODELEVALUATIONS DROP COLUMN IF EXISTS EVALFEEDBACKLOOPTESTED;
ALTER TABLE GOVMODELEVALUATIONS DROP COLUMN IF EXISTS EVALADVERSARIALRESULTS;
ALTER TABLE GOVMODELEVALUATIONS DROP COLUMN IF EXISTS EVALADVERSARIALROBUSTNESS;
ALTER TABLE GOVMODELEVALUATIONS DROP COLUMN IF EXISTS EVALADVERSARIALTESTED;

-- Drop columnas PRJPROJECTS
ALTER TABLE PRJPROJECTS DROP COLUMN IF EXISTS PRJEUREGISTRATIONSTATUS;
ALTER TABLE PRJPROJECTS DROP COLUMN IF EXISTS PRJEUREGISTRATIONDATE;
ALTER TABLE PRJPROJECTS DROP COLUMN IF EXISTS PRJEUREGISTRATIONID;
ALTER TABLE PRJPROJECTS DROP COLUMN IF EXISTS PRJPROHIBITEDUSEJUSTIFICATION;
ALTER TABLE PRJPROJECTS DROP COLUMN IF EXISTS PRJPROHIBITEDUSECHECKED;
ALTER TABLE PRJPROJECTS DROP COLUMN IF EXISTS PRJANNEXILEGISLATION;
ALTER TABLE PRJPROJECTS DROP COLUMN IF EXISTS PRJREGULATEDSECTOR;
ALTER TABLE PRJPROJECTS DROP COLUMN IF EXISTS PRJCLASSIFICATIONAUTHOR;
ALTER TABLE PRJPROJECTS DROP COLUMN IF EXISTS PRJCLASSIFICATIONDATE;
ALTER TABLE PRJPROJECTS DROP COLUMN IF EXISTS PRJANNEXIIICATEGORIES;
ALTER TABLE PRJPROJECTS DROP COLUMN IF EXISTS PRJISHIGHRISK;

-- Drop columnas MODMODELS
ALTER TABLE MODMODELS DROP COLUMN IF EXISTS MODGPAISYSTEMICRISK;
ALTER TABLE MODMODELS DROP COLUMN IF EXISTS MODGPAIFLOPSTRAINING;
ALTER TABLE MODMODELS DROP COLUMN IF EXISTS MODISGPAI;
ALTER TABLE MODMODELS DROP COLUMN IF EXISTS MODRISKCATEGORYJUSTIFICATION;
ALTER TABLE MODMODELS DROP COLUMN IF EXISTS MODANNEXIIISUBCATEGORY;
ALTER TABLE MODMODELS DROP COLUMN IF EXISTS MODANNEXIIICATEGORY;
ALTER TABLE MODMODELS DROP COLUMN IF EXISTS MODISHIGHRISK;
ALTER TABLE MODMODELS DROP COLUMN IF EXISTS MODACCURACYLEVEL;
ALTER TABLE MODMODELS DROP COLUMN IF EXISTS MODTECHNICALDOCSCORE;
ALTER TABLE MODMODELS DROP COLUMN IF EXISTS MODTECHNICALDOCCOMPLETE;
ALTER TABLE MODMODELS DROP COLUMN IF EXISTS MODTECHNICALDOCVERSION;
ALTER TABLE MODMODELS DROP COLUMN IF EXISTS MODTECHNICALDOCURL;

COMMIT;
EOF

# Ejecutar rollback
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f 99_rollback_eu_ai_act.sql
```

---

## 📚 DOCUMENTACIÓN ADICIONAL

- **Documento Implementación:** `../../docs/compliance/CAMBIOS_REALIZADOS_EU_AI_ACT.md`
- **Especificaciones:** `../../docs/compliance/PROMPTS_03_JAVA_BACKEND_EXISTENTE.md`
- **Entidades JPA:** `/mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service/nocode.service.entitys/`

---

## ❓ TROUBLESHOOTING

### Error: "relation already exists"

```sql
-- Verificar si la tabla ya existe
SELECT tablename FROM pg_tables WHERE tablename = 'imlimmutablelogs';

-- Si existe, el patch es idempotente y puede re-ejecutarse
```

### Error: "column already exists"

```sql
-- Verificar columnas existentes
SELECT column_name FROM information_schema.columns WHERE table_name = 'modmodels';

-- Los patches usan IF NOT EXISTS, son idempotentes
```

### Triggers no funcionan

```sql
-- Verificar triggers
SELECT * FROM information_schema.triggers WHERE event_object_table = 'imlimmutablelogs';

-- Recrear triggers manualmente si necesario (ejecutar patch 09 de nuevo)
```

---

## 📞 SOPORTE

Para dudas o problemas, contactar:

- **Equipo:** Java Team - Backend Existente
- **Proyecto:** CodeflowX Govern
- **Documentación:** `/docs/compliance/`

---

**Fin del Documento**

