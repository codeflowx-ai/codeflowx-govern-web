# 📊 Sistema Completo de Scripts SQL - CodeFlowX Govern

**Generado:** 2025-10-14  
**Framework:** CodeFlowX NoCode v1.1.0  
**Convención:** EnArt (UPPERCASE)

---

## 📦 Estructura de Directorios

```
sources/sql/
├── 00_drop_tables.sql          # DROP de todas las tablas
├── 01_create_tables.sql        # CREATE TABLE (161 tablas)
├── 02_indexes.sql              # Índices (740 índices)
├── 03_foreign_keys.sql         # Foreign Keys (93 FKs)
├── 04_comments.sql             # Documentación SQL
├── README.md                   # Guía de creación de BD
├── README_COMPLETO.md          # Esta guía
└── mock_data/
    ├── 01_core_mock_data.sql           # Usuarios, roles, departamentos
    ├── 02_projects_mock_data.sql       # Proyectos de IA/ML
    ├── 03_prompts_mock_data.sql        # Templates de prompts
    ├── 04_agents_mock_data.sql         # Agentes IA especializados
    ├── 05_models_mock_data.sql         # Modelos de ML
    ├── 06_training_mock_data.sql       # Experimentos y runs
    ├── 07_dashboard_mock_data.sql      # Métricas de dashboard
    ├── ejecutar_todos_mocks.sh         # Script maestro
    └── README_MOCKS.md                 # Documentación de mocks
```

---

## 🚀 Instalación Completa (Paso a Paso)

### 1️⃣ Crear Base de Datos

```bash
# Conectar a PostgreSQL
psql -U postgres

# Crear base de datos
CREATE DATABASE codeflowx_govern;

# Conectar a la nueva BD
\c codeflowx_govern

# Crear extensiones (si no se crean automáticamente)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgvector";
```

### 2️⃣ Crear Estructura de Tablas

```bash
cd /mnt/c/Users/ManuelGonzalez/git/codeflowx-nocode/sources/sql

# Ejecutar en orden
psql -U postgres -d codeflowx_govern -f 01_create_tables.sql
psql -U postgres -d codeflowx_govern -f 02_indexes.sql
psql -U postgres -d codeflowx_govern -f 03_foreign_keys.sql
psql -U postgres -d codeflowx_govern -f 04_comments.sql
```

### 3️⃣ Cargar Datos Mock (Demo)

```bash
cd mock_data
./ejecutar_todos_mocks.sh

# O manualmente:
psql -U postgres -d codeflowx_govern -f 01_core_mock_data.sql
psql -U postgres -d codeflowx_govern -f 02_projects_mock_data.sql
psql -U postgres -d codeflowx_govern -f 03_prompts_mock_data.sql
psql -U postgres -d codeflowx_govern -f 04_agents_mock_data.sql
psql -U postgres -d codeflowx_govern -f 05_models_mock_data.sql
psql -U postgres -d codeflowx_govern -f 06_training_mock_data.sql
psql -U postgres -d codeflowx_govern -f 07_dashboard_mock_data.sql
```

---

## 📊 Resumen de Componentes

### Estructura de Base de Datos

| Componente | Cantidad | Descripción |
|------------|----------|-------------|
| **Tablas** | 161 | Tablas del sistema |
| **Índices** | 740 | Optimización de consultas |
| **Foreign Keys** | 93 | Integridad referencial |
| **Módulos** | 17 | Agrupación funcional |

### Módulos Funcionales

| Módulo | Tablas | Descripción |
|--------|--------|-------------|
| **agents** | 22 | Agentes IA, interacciones, governance |
| **analytics** | 2 | Métricas y reportes de analytics |
| **artefacto** | 3 | Artefactos y dependencias |
| **core** | 7 | Usuarios, roles, permisos |
| **dashboard** | 7 | Métricas y visualizaciones |
| **evaluation** | 8 | Evaluaciones de modelos, bias |
| **governance** | 14 | Políticas, compliance, seguridad |
| **infrastructure** | 12 | Cloud, K8s, recursos |
| **models** | 14 | Catálogo de modelos, versiones |
| **monitoring** | 6 | Auditorías, alertas, métricas |
| **notifications** | 5 | Notificaciones y canales |
| **platform** | 6 | Licencias, nodos, actualizaciones |
| **projects** | 18 | Proyectos, facturas, ROI |
| **prompts** | 3 | Prompts y validaciones |
| **rag** | 3 | Sistemas RAG, datasources |
| **serving** | 10 | Deployments, predictions |
| **training** | 21 | Experimentos, runs, métricas |

### Datos Mock Generados

| Script | Registros | Módulo |
|--------|-----------|--------|
| **01_core** | 23 | Usuarios, roles, departamentos |
| **02_projects** | 5 | Proyectos de IA/ML |
| **03_prompts** | 8 | Templates de prompts |
| **04_agents** | 8 | Agentes especializados |
| **05_models** | 6 | Modelos de ML |
| **06_training** | 20+ | Experimentos y runs |
| **07_dashboard** | 274 | Métricas completas |

---

## 🎯 Datos Mock de Dashboard

El módulo de dashboard incluye datos realistas para:

### DSHMODULESTATS (Estadísticas)
- 10 módulos con métricas completas
- Success rates: 87-96%
- Scores promedio: 84-93%
- Alertas activas por módulo
- Custom metrics en JSONB

### DSHDISTRIBUTIONS (Gráficos)
- 25 distribuciones para visualización
- Modelos por estado (Production, Staging, etc.)
- Agentes por tipo (Conversational, Analytical, etc.)
- Governance por nivel de riesgo
- Prompts por categoría
- Compliance por framework (EU AI Act, GDPR, etc.)

### DSHTIMESERIES (Tendencias)
- 180 registros (30 días × 6 métricas)
- Modelos desplegados
- Requests de API
- Success rate
- Latencia promedio
- Agentes activos
- Compliance score

### DSHMODULEACTIVITY (Actividad)
- 10 actividades recientes
- Deployments, evaluaciones, auditorías
- Timestamps realistas

### DSHTOKENMETRICS (Tokens)
- 7 días de métricas
- 2.5M - 4.5M tokens/día
- Costos calculados

### DSHCOSTMETRICS (Costos)
- 6 módulos con breakdown de costos
- Inference, execution, storage, compute
- Proyecciones vs costos reales

### DSHQUICKACTIONS (Acciones)
- 8 acciones principales del sistema
- Deploy, Create, Audit, Monitor
- URLs e iconos configurados

---

## 🔍 Verificación Post-Instalación

```sql
-- Verificar tablas creadas
SELECT schemaname, tablename 
FROM pg_tables 
WHERE schemaname = 'public' 
ORDER BY tablename;

-- Contar registros por tabla (mocks)
SELECT 'CORUSERS' as tabla, COUNT(*) as registros FROM CORUSERS
UNION ALL
SELECT 'PRJPROJECTS', COUNT(*) FROM PRJPROJECTS
UNION ALL
SELECT 'AGTAGENTS', COUNT(*) FROM AGTAGENTS
UNION ALL
SELECT 'MODMODELS', COUNT(*) FROM MODMODELS
UNION ALL
SELECT 'DSHMODULESTATS', COUNT(*) FROM DSHMODULESTATS
ORDER BY tabla;

-- Verificar Foreign Keys
SELECT conname, conrelid::regclass AS table_name, 
       confrelid::regclass AS referenced_table
FROM pg_constraint
WHERE contype = 'f'
ORDER BY table_name;

-- Verificar índices
SELECT indexname, tablename 
FROM pg_indexes 
WHERE schemaname = 'public'
ORDER BY tablename, indexname;
```

---

## ⚠️ Notas Importantes

### Producción
- **NO ejecutar** `00_drop_tables.sql` en producción
- Hacer backup antes de modificar estructura
- Los datos mock son solo para DEMO/DESARROLLO

### Desarrollo
- Ejecutar scripts en el orden especificado
- Verificar que no hay errores en los logs
- Validar Foreign Keys antes de insertar datos

### Nomenclatura
- Todas las tablas: **UPPERCASE**
- Todas las columnas: **UPPERCASE**
- PKs: `IDXTABLA` (BIGSERIAL)
- Prefijos de módulo: `AGT_`, `PRJ_`, `DSH_`, etc.

---

## 🛠️ Regeneración de Scripts

Para regenerar los scripts desde los JSONs EnArt:

```bash
cd /mnt/c/Users/ManuelGonzalez/git/codeflowx-nocode

# Regenerar estructura de BD
python3 src/generators/sql_complete_generator.py \
    --input sources/json \
    --output sources/sql

# Regenerar datos mock
python3 src/generators/mock_data_generator.py \
    --input sources/json \
    --output sources/sql/mock_data

# Regenerar datos de dashboard
python3 src/generators/dashboard_mock_generator.py \
    --output sources/sql/mock_data
```

---

## 📚 Documentación Relacionada

- **README.md** - Guía de creación de estructura
- **README_MOCKS.md** - Detalles de datos mock
- **JSONs EnArt** - `/sources/json/`
- **Generadores** - `/src/generators/`

---

## ✅ Checklist de Instalación

- [ ] Base de datos creada
- [ ] Extensiones instaladas (uuid-ossp, pgvector)
- [ ] Tablas creadas (01_create_tables.sql)
- [ ] Índices creados (02_indexes.sql)
- [ ] Foreign Keys creadas (03_foreign_keys.sql)
- [ ] Comentarios aplicados (04_comments.sql)
- [ ] Datos mock de core cargados
- [ ] Datos mock de proyectos cargados
- [ ] Datos mock de prompts cargados
- [ ] Datos mock de agentes cargados
- [ ] Datos mock de modelos cargados
- [ ] Datos mock de training cargados
- [ ] Datos mock de dashboard cargados
- [ ] Verificación de Foreign Keys OK
- [ ] Verificación de conteo de registros OK

---

**Generado por:** CodeFlowX NoCode Framework v1.1.0  
**Fecha:** 2025-10-14  
**Equipo:** CodeFlowX Team
