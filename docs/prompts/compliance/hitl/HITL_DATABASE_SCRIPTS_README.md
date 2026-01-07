# 📊 Scripts SQL para Módulo HITL

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Módulo:** HITL Supervision (EU AI Act Art. 14)

---

## 📋 ÍNDICE

1. [Descripción General](#descripción-general)
2. [Índices y Claves Únicas](#índices-y-claves-únicas)
3. [Disparadores (Triggers)](#disparadores-triggers)
4. [Funciones](#funciones)
5. [Vistas](#vistas)
6. [Procedimientos Almacenados](#procedimientos-almacenados)
7. [Instalación](#instalación)
8. [Mantenimiento](#mantenimiento)

---

## 📖 DESCRIPCIÓN GENERAL

Este documento describe los scripts SQL adicionales para el módulo HITL que no pueden ser generados automáticamente por JPA. Los índices y claves únicas básicos están definidos en las anotaciones JPA de las entidades y se crean automáticamente cuando JPA actualiza el schema.

### Archivos

- **`HITL_DATABASE_SCRIPTS.sql`**: Contiene todos los scripts SQL (triggers, funciones, vistas, procedimientos)
- **`HITL_DATABASE_SCRIPTS_README.md`**: Este documento de documentación

### Ubicación

```
docs/prompts/compliance/hitl/
├── HITL_DATABASE_SCRIPTS.sql
└── HITL_DATABASE_SCRIPTS_README.md
```

---

## 🔍 ÍNDICES Y CLAVES ÚNICAS

### Tabla: `GOVHITLSUPERVISIONS`

Los índices y claves únicas están definidos en la entidad JPA `HitlSupervision.java`:

#### Índices

| Nombre | Columnas | Propósito |
|--------|----------|-----------|
| `idx_hitl_supervision_project` | `IDXPROJECT` | Búsquedas por proyecto |
| `idx_hitl_supervision_type` | `HITLSUPERVISIONTYPE` | Búsquedas por tipo de supervisión |
| `idx_hitl_supervision_entity` | `HITLENTITYTYPE, IDXENTITY` | Búsquedas por entidad (tipo + ID) |
| `idx_hitl_supervision_created` | `HITLCREATEDAT` | Ordenamiento temporal |
| `idx_hitl_supervision_project_type` | `IDXPROJECT, HITLSUPERVISIONTYPE` | Búsquedas compuestas |
| `idx_hitl_supervision_sla` | `HITLSLA` | Alertas de vencimiento |

#### Claves Únicas

| Nombre | Columnas | Propósito |
|--------|----------|-----------|
| `uk_hitl_supervision_uuid` | `IDUUID` | UUID único |
| `uk_hitl_supervision_entity` | `IDXPROJECT, HITLENTITYTYPE, IDXENTITY` | Una supervisión única por proyecto, tipo e ID de entidad |

### Tabla: `GOVHITLDECISIONS`

Los índices y claves únicas están definidos en la entidad JPA `HitlDecision.java`:

#### Índices

| Nombre | Columnas | Propósito |
|--------|----------|-----------|
| `idx_hitl_decision_supervision` | `IDXHITLSUPERVISION` | Búsquedas por supervisión (FK) |
| `idx_hitl_decision_entity_type` | `HITLENTITYTYPE` | Búsquedas por tipo de entidad |
| `idx_hitl_decision_entity` | `HITLENTITYTYPE, IDXENTITY` | Búsquedas por entidad |
| `idx_hitl_decision_type` | `HITLDECISION` | Búsquedas por decisión (APPROVED, REJECTED, MODIFIED) |
| `idx_hitl_decision_user` | `HITLUSERID` | Búsquedas por usuario |
| `idx_hitl_decision_date` | `HITLDECISIONDATE` | Ordenamiento temporal |
| `idx_hitl_decision_entity_decision` | `HITLENTITYTYPE, HITLDECISION` | Búsquedas compuestas |
| `idx_hitl_decision_response_time` | `HITLRESPONSETIME` | Métricas de tiempo de respuesta |

#### Claves Únicas

| Nombre | Columnas | Propósito |
|--------|----------|-----------|
| `uk_hitl_decision_uuid` | `IDUUID` | UUID único |
| `uk_hitl_decision_supervision` | `IDXHITLSUPERVISION` | Una decisión única por supervisión |

---

## ⚡ DISPARADORES (TRIGGERS)

### 1. `trg_hitl_supervision_updated_at`

**Función:** `update_hitl_supervision_updated_at()`

**Propósito:** Actualiza automáticamente el campo `HITLUPDATEDAT` cuando se modifica un registro en `GOVHITLSUPERVISIONS`.

**Cuándo se ejecuta:** `BEFORE UPDATE`

**Nota:** Aunque JPA ya actualiza este campo con `@PreUpdate`, este trigger asegura la integridad a nivel de base de datos.

### 2. `trg_hitl_decision_updated_at`

**Función:** `update_hitl_decision_updated_at()`

**Propósito:** Actualiza automáticamente el campo `HITLUPDATEDAT` cuando se modifica un registro en `GOVHITLDECISIONS`.

**Cuándo se ejecuta:** `BEFORE UPDATE`

### 3. `trg_validate_single_decision`

**Función:** `validate_single_decision_per_supervision()`

**Propósito:** Valida que una supervisión solo tenga una decisión. Previene la inserción de múltiples decisiones para la misma supervisión.

**Cuándo se ejecuta:** `BEFORE INSERT`

**Nota:** La clave única `uk_hitl_decision_supervision` ya previene esto, pero este trigger proporciona un mensaje de error más descriptivo.

### 4. `trg_calculate_response_time`

**Función:** `calculate_hitl_response_time()`

**Propósito:** Calcula automáticamente el tiempo de respuesta (en minutos) cuando se inserta o actualiza una decisión, basándose en la diferencia entre la fecha de creación de la supervisión y la fecha de decisión.

**Cuándo se ejecuta:** `BEFORE INSERT OR UPDATE` (solo si `HITLDECISIONDATE` no es NULL)

---

## 🔧 FUNCIONES

### 1. `get_hitl_metrics_for_project(p_project_id BIGINT)`

**Descripción:** Calcula métricas agregadas de HITL para un proyecto específico.

**Parámetros:**
- `p_project_id`: ID del proyecto

**Retorna:**
- `average_response_time_hours`: Tiempo promedio de respuesta en horas
- `approval_rate`: Tasa de aprobación (0.0 - 1.0)
- `sla_compliance_rate`: Cumplimiento de SLA (0.0 - 1.0)
- `total_interventions`: Total de intervenciones
- `pending_interventions`: Intervenciones pendientes
- `approved_decisions`: Decisiones aprobadas
- `rejected_decisions`: Decisiones rechazadas
- `modified_decisions`: Decisiones modificadas

**Ejemplo de uso:**
```sql
SELECT * FROM get_hitl_metrics_for_project(123);
```

### 2. `get_pending_interventions_with_expired_sla()`

**Descripción:** Retorna intervenciones que están pendientes y cuyo SLA ha vencido. Útil para alertas y notificaciones.

**Retorna:**
- `supervision_id`: ID de la supervisión
- `project_id`: ID del proyecto
- `entity_type`: Tipo de entidad (AGENT, MODEL, PROMPT)
- `entity_id`: ID de la entidad
- `entity_name`: Nombre de la entidad
- `supervision_type`: Tipo de supervisión
- `sla_hours`: SLA en horas
- `created_at`: Fecha de creación
- `hours_overdue`: Horas de retraso

**Ejemplo de uso:**
```sql
SELECT * FROM get_pending_interventions_with_expired_sla();
```

### 3. `get_decision_stats_by_entity_type()`

**Descripción:** Retorna estadísticas agregadas de decisiones agrupadas por tipo de entidad (AGENT, MODEL, PROMPT).

**Retorna:**
- `entity_type`: Tipo de entidad
- `total_decisions`: Total de decisiones
- `approved_count`: Cantidad de aprobadas
- `rejected_count`: Cantidad de rechazadas
- `modified_count`: Cantidad de modificadas
- `average_response_time_hours`: Tiempo promedio de respuesta en horas
- `sla_compliance_rate`: Tasa de cumplimiento de SLA

**Ejemplo de uso:**
```sql
SELECT * FROM get_decision_stats_by_entity_type();
```

---

## 👁️ VISTAS

### 1. `v_hitl_interventions_summary`

**Descripción:** Vista que agrega información de supervisión y decisión para facilitar consultas y reportes.

**Columnas principales:**
- Información de supervisión (ID, UUID, proyecto, tipo, entidad)
- Estado (PENDING o RESOLVED)
- Información de decisión (si existe)
- Cálculo de horas de retraso y urgencia

**Ejemplo de uso:**
```sql
SELECT * FROM v_hitl_interventions_summary
WHERE status = 'PENDING'
ORDER BY hours_overdue DESC;
```

### 2. `v_hitl_dashboard_metrics`

**Descripción:** Vista agregada para el dashboard principal con todas las métricas necesarias.

**Métricas incluidas:**
- Total de intervenciones
- Intervenciones pendientes
- Total de decisiones
- Tiempo promedio de respuesta
- Tasa de aprobación
- Cumplimiento de SLA
- Intervenciones por tipo de entidad
- Decisiones por tipo

**Ejemplo de uso:**
```sql
SELECT * FROM v_hitl_dashboard_metrics;
```

---

## 📦 PROCEDIMIENTOS ALMACENADOS

### 1. `cleanup_old_hitl_decisions(p_days_to_keep INTEGER)`

**Descripción:** Limpia decisiones antiguas (más de N días) manteniendo solo las estadísticas agregadas. Útil para mantenimiento de base de datos.

**Parámetros:**
- `p_days_to_keep`: Días a mantener (por defecto: 365)

**Ejemplo de uso:**
```sql
CALL cleanup_old_hitl_decisions(365);
```

**Nota:** Este procedimiento debe ejecutarse con precaución y solo después de hacer backup de los datos.

### 2. `update_sla_compliance_metrics()`

**Descripción:** Recalcula y actualiza métricas de cumplimiento de SLA en la configuración de supervisión (campo `HITLCONFIGURATION` JSONB).

**Ejemplo de uso:**
```sql
CALL update_sla_compliance_metrics();
```

**Nota:** Este procedimiento puede tomar tiempo si hay muchas supervisiones. Se recomienda ejecutarlo en horarios de bajo tráfico.

---

## 🚀 INSTALACIÓN

### Opción 1: Ejecución Manual

1. Conectarse a la base de datos PostgreSQL:
```bash
psql -U usuario -d nombre_base_datos
```

2. Ejecutar el script:
```sql
\i HITL_DATABASE_SCRIPTS.sql
```

### Opción 2: Usando Flyway/Liquibase

Si se usa Flyway o Liquibase para migraciones, crear un archivo de migración:

**Flyway:**
```
V{version}__HITL_database_scripts.sql
```

**Liquibase:**
Agregar el script en un changelog XML.

### Opción 3: Integración con JPA

Los índices y claves únicas se crean automáticamente cuando JPA actualiza el schema (`ddl-auto: update` o `create`). Los triggers, funciones, vistas y procedimientos deben ejecutarse manualmente o mediante migraciones.

---

## 🔧 MANTENIMIENTO

### Verificar Triggers

```sql
SELECT trigger_name, event_manipulation, event_object_table
FROM information_schema.triggers
WHERE event_object_table IN ('GOVHITLSUPERVISIONS', 'GOVHITLDECISIONS');
```

### Verificar Funciones

```sql
SELECT routine_name, routine_type
FROM information_schema.routines
WHERE routine_schema = 'public'
AND routine_name LIKE '%hitl%';
```

### Verificar Vistas

```sql
SELECT table_name
FROM information_schema.views
WHERE table_schema = 'public'
AND table_name LIKE '%hitl%';
```

### Verificar Índices

```sql
SELECT indexname, tablename
FROM pg_indexes
WHERE tablename IN ('GOVHITLSUPERVISIONS', 'GOVHITLDECISIONS')
ORDER BY tablename, indexname;
```

### Mantenimiento Periódico

1. **Semanal:** Ejecutar `update_sla_compliance_metrics()` para actualizar métricas
2. **Mensual:** Revisar y limpiar decisiones antiguas con `cleanup_old_hitl_decisions()`
3. **Trimestral:** Revisar rendimiento de índices y optimizar si es necesario

---

## ⚠️ NOTAS IMPORTANTES

1. **Backup:** Siempre hacer backup antes de ejecutar scripts de mantenimiento
2. **Permisos:** Asegurarse de tener permisos suficientes para crear triggers, funciones, vistas y procedimientos
3. **Testing:** Probar todos los scripts en un entorno de desarrollo antes de producción
4. **Versionado:** Mantener versionado de los scripts SQL junto con el código
5. **Documentación:** Actualizar este documento cuando se agreguen nuevos scripts

---

## 📚 REFERENCIAS

- **Entidades JPA:**
  - `HitlSupervision.java`
  - `HitlDecision.java`
- **Developer Guide Backend:** `DEVELOPER_GUIDE_BACKEND_HITL.md`
- **PostgreSQL Documentation:** https://www.postgresql.org/docs/

---

**Última actualización:** Diciembre 2025
**Versión del documento:** 1.0

