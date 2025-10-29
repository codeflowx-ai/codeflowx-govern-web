# Implementación RAG Knowledge Base

## Estado: 🚧 EN PROGRESO - FASE 2

## 1. Resumen

Implementación del módulo **RAG Knowledge Base** (Retrieval-Augmented Generation) para ZKoss basado en las especificaciones del proyecto Next.js.

### 🎯 Objetivo
Crear un sistema completo de gestión de sistemas RAG que incluya:
- Registro y gestión de sistemas RAG
- Gestión de fuentes de datos
- Versionamiento y rollback
- Monitoreo de calidad y compliance
- Detección de sesgos
- Control de acceso

## 2. Arquitectura de Entidades JPA

### 2.1 Entidades Creadas

| Entidad | Tabla | Descripción | Prefijo |
|---------|-------|-------------|---------|
| `RagSystem` | `RAGSYSTEMS` | Sistema RAG principal | `rag_` |
| `RagDataSource` | `RAGDATASOURCES` | Fuentes de datos del RAG | `ragds_` |
| `RagVersion` | `RAGVERSIONS` | Versionamiento de sistemas | `ragv_` |
| `RagRollback` | `RAGROLLBACKS` | Historial de rollbacks | `ragr_` |

### 2.2 Campos Principales de RagSystem

**Identificación:**
- `ragsystemname` (VARCHAR 255, NOT NULL) - Nombre del sistema
- `ragdescription` (TEXT) - Descripción
- `ragversion` (VARCHAR 50, NOT NULL) - Versión actual
- `ragtype` (VARCHAR 50, NOT NULL) - Tipo: KNOWLEDGE_BASE, LEGAL_ASSISTANT, CODE_ASSISTANT, SUPPORT
- `ragstatus` (VARCHAR 50, NOT NULL) - Estado: ACTIVE, INACTIVE, DRAFT, ARCHIVED

**Métricas de Calidad:**
- `ragoverallscore` (DECIMAL 5,2) - Score global
- `ragrelevancescore` (DECIMAL 5,2) - Score de relevancia
- `ragaccuracyscore` (DECIMAL 5,2) - Score de precisión
- `ragcompletescore` (DECIMAL 5,2) - Score de completitud
- `ragconsistencyscore` (DECIMAL 5,2) - Score de consistencia

**Métricas de Rendimiento:**
- `ragresponsetime` (INT) - Tiempo de respuesta en ms
- `ragthroughput` (INT) - Queries por minuto
- `ragerrorrate` (DECIMAL 5,4) - Tasa de error
- `ragusersatisfaction` (DECIMAL 3,1) - Satisfacción (0.0 a 5.0)

**Estadísticas:**
- `ragdatasources` (INT) - Número de fuentes de datos
- `ragdocuments` (INT) - Número de documentos
- `ragqueries` (BIGINT) - Total de queries procesadas

**Compliance y Gobierno:**
- `ragcompliance` (VARCHAR 50, NOT NULL) - COMPLIANT, NON_COMPLIANT, PENDING_REVIEW
- `raggovernancestatus` (VARCHAR 50) - APPROVED, REJECTED, PENDING
- `ragrisklevel` (VARCHAR 50) - LOW, MEDIUM, HIGH, CRITICAL

**Metadata:**
- `ragconfiguration` (TEXT/JSONB) - Configuración JSON
- `ragmetadata` (TEXT/JSONB) - Metadata adicional
- `ragtags` (TEXT) - Tags separados por comas

**Auditoría:**
- `ragcreatedat` (TIMESTAMP, NOT NULL)
- `ragupdatedat` (TIMESTAMP)
- `raglastused` (TIMESTAMP)
- `ragcreatedby` (VARCHAR 255)

### 2.3 Relaciones entre Entidades

```
RagSystem (1) ──┬──> (N) RagDataSource
                ├──> (N) RagVersion
                └──> (N) RagRollback
```

## 3. Scripts SQL

### 3.1 Archivo: `rag_systems.sql`

**Ubicación:** `/nocode.service.entitys/src/main/resources/sql-scripts/rag_systems.sql`

**Contenido:**
- Creación de tabla `RAGSYSTEMS`
- Creación de tabla `RAGDATASOURCES`
- Creación de tabla `RAGVERSIONS`
- Creación de tabla `RAGROLLBACKS`
- Índices y claves foráneas
- Datos de ejemplo

**Comandos de Ejecución:**
```sql
-- Crear tablas
SOURCE rag_systems.sql;

-- Verificar
SHOW TABLES LIKE 'RAG%';
SELECT * FROM RAGSYSTEMS;
```

## 4. Pantallas ZKoss Implementadas

### 4.1 Overview Principal

**Archivo:** `/console/platform/rag/overview/page.zul`

**Funcionalidades:**
- ✅ Dashboard con métricas principales
  - Total sistemas
  - Sistemas activos
  - Documentos totales
  - Tasa de cumplimiento
- ✅ Filtros avanzados
  - Búsqueda por nombre
  - Filtro por tipo
  - Filtro por estado
  - Filtro por compliance
- ✅ Tabla de sistemas RAG
  - Información completa de cada sistema
  - Métricas de calidad y rendimiento
  - Estados visuales (badges)
  - Acciones (Ver, Editar, Eliminar)

**ViewModel:** `RagSystemOverviewViewModel`
- Extiende `BaseFront<RagSystemOverviewViewModel>`
- Usa `@Init(superclass = true)`
- Incluye `@Destroy` para cleanup
- Implementa `logActivity()` para auditoría
- Usa patrón correcto de `Criterias` con `new Criteria(Operation, Evaluation, campo, valor)`

### 4.2 Registry ✅

**Archivo:** `/console/platform/rag/registry/page.zul`

**Funcionalidades:**
- ✅ Vista Grid y List intercambiables
- ✅ Filtros por estado, tipo y rendimiento
- ✅ Búsqueda por nombre, descripción y tags
- ✅ Modal de creación/edición completo
- ✅ Tarjetas con métricas (rendimiento, precisión, fuentes, documentos)
- ✅ Gestión de tags
- ✅ Descarga de configuración
- ✅ Acciones CRUD completas

**ViewModel:** `RagRegistryViewModel`
- Extiende `BaseFront<RagRegistryViewModel>`
- Modal integrado para crear/editar
- Filtros avanzados con `buildCriterias()`
- Auditoría completa (`logActivity()`)
- Destructor con cleanup

### 4.3 Data Sources ✅

**Archivo:** `/console/platform/rag/data-sources/page.zul`

**Funcionalidades:**
- ✅ Dashboard con 4 métricas (total, indexadas, en proceso, documentos)
- ✅ Filtros por sistema RAG, tipo, estado
- ✅ Búsqueda de fuentes
- ✅ Modal de creación/edición
- ✅ Tabla con información completa
- ✅ Clasificación de datos (PUBLIC, INTERNAL, CONFIDENTIAL, RESTRICTED)
- ✅ Score de calidad visual
- ✅ Acción de reindexación
- ✅ Última indexación

**ViewModel:** `RagDataSourceViewModel`
- Extiende `BaseFront<RagDataSourceViewModel>`
- Carga sistemas RAG para el combobox
- Métricas calculadas en tiempo real
- Reindexación (pendiente integración leka-server)
- Auditoría completa

### 4.4 Pantallas Existentes (Ya Creadas Previamente)

| Pantalla | Ruta | Estado | Archivos |
|----------|------|--------|----------|
| **Versioning** | `/rag/versioning/` | ✅ EXISTE | overview.zul, page.zul |
| **Monitoring** | `/rag/monitoring/` | ✅ EXISTE | health-dashboard.zul, metrics-summary.zul, usage-by-agent.zul |
| **Quality Control** | `/rag/quality-control/` | ✅ EXISTE | chunk-distribution.zul, retrieval-quality.zul |
| **Data Sources (otros)** | `/rag/data-sources/` | ✅ EXISTE | coverage-analysis.zul, embedding-progress.zul, overview.zul, statistics.zul |
| **Overview (otros)** | `/rag/overview/` | ✅ EXISTE | summary.zul |

### 4.5 Pantallas Realmente Pendientes

| Pantalla | Ruta | Estado | Descripción |
|----------|------|--------|-------------|
| **Rollback** | `/rag/rollback/page.zul` | ⏳ PENDIENTE | Gestión de rollbacks (directorio existe vacío) |
| **Bias Detection** | `/rag/bias-detection/page.zul` | ⏳ PENDIENTE | Detección de sesgos (directorio existe vacío) |
| **Compliance** | `/rag/compliance/page.zul` | ⏳ PENDIENTE | Monitoreo de cumplimiento (directorio existe vacío) |
| **Access Control** | `/rag/access-control/page.zul` | ⏳ PENDIENTE | Control de acceso (directorio existe vacío) |

**Nota:** Los directorios existen pero están vacíos. Necesitan implementación de pantallas ZKoss y ViewModels.

## 5. ViewModels Implementados

### 5.1 RagSystemOverviewViewModel ✅

**Ubicación:** `com.codeflowx.platform.viewmodel.rag.RagSystemOverviewViewModel`

**Patrón Aplicado:**
```java
@Slf4j
@Getter
@Setter
@Init(superclass = true)
@VariableResolver(DelegatingVariableResolver.class)
public class RagSystemOverviewViewModel extends BaseFront<RagSystemOverviewViewModel> {
    
    @Override
    public void setBeans(Object bean) {}
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        // NO initDao() - ya está en @Init(superclass=true)
    }
    
    @Command
    @NotifyChange("*")
    public void loadSystems() {
        // ...
        logActivity("BUSCAR", "RAGSYSTEMS", null, "Búsqueda...");
    }
    
    @Command
    @NotifyChange("*")
    public void deleteSystem(@BindingParam("system") RagSystem system) {
        businessService.removeFromID(system);
        logActivity("ELIMINAR", "RAGSYSTEMS", id, "Descripción");
    }
    
    @Destroy
    public void destroy() {
        if (systemsList != null) { 
            systemsList.clear(); 
            systemsList = null; 
        }
        businessService = null;
    }
}
```

**Métodos Implementados:**
- `loadSystems()` - Carga sistemas con filtros y auditoría
- `loadMetrics()` - Calcula métricas del dashboard
- `buildCriterias()` - Construye filtros con patrón `new Criteria()`
- `searchSystems()` - Búsqueda con auditoría
- `clearFilters()` - Limpia filtros
- `refreshSystems()` - Actualiza datos
- `createSystem()` - Navega a creación
- `viewSystem()` - Ver detalles
- `editSystem()` - Editar sistema
- `deleteSystem()` - Eliminar con confirmación y auditoría
- `getStatusColor()` - Estilos Bootstrap para estados
- `getComplianceColor()` - Estilos Bootstrap para compliance
- `formatDate()` - Formateo de fechas

### 5.2 RagRegistryViewModel ✅

**Ubicación:** `com.codeflowx.platform.viewmodel.rag.RagRegistryViewModel`

**Funcionalidades:**
- Toggle entre vista Grid y List
- Filtros por estado, tipo y rendimiento (score-based)
- Modal completo para creación/edición
- Parseo de tags
- Descarga de configuración (pendiente)
- CRUD completo con auditoría

**Métodos Destacados:**
- `toggleView()` - Cambia entre grid/list
- `showCreateModal()` / `editSystem()` - Gestión de modal
- `saveSystem()` - Crear/editar con validaciones
- `getTags()` - Parsear tags separados por comas
- `buildCriterias()` - Incluye filtro de rendimiento con GREATER_THAN/LESS_THAN

### 5.3 RagDataSourceViewModel ✅

**Ubicación:** `com.codeflowx.platform.viewmodel.rag.RagDataSourceViewModel`

**Funcionalidades:**
- Dashboard con métricas calculadas
- Filtros por sistema RAG (con lista dinámica)
- Modal de creación/edición
- Reindexación de fuentes
- Clasificación de datos
- Relación con RagSystem

**Métodos Destacados:**
- `loadRagSystems()` - Carga lista de sistemas para filtros
- `loadMetrics()` - Calcula métricas en tiempo real
- `reindexSource()` - Solicita reindexación (pendiente leka-server)
- `getRagSystemName()` - Helper para mostrar nombre del sistema
- `getClassificationColor()` - Estilos para clasificación de datos

### 5.4 ViewModels Pendientes

| ViewModel | Estado | Descripción |
|-----------|--------|-------------|
| `RagVersionViewModel` | ⏳ PENDIENTE | Versionamiento |
| `RagRollbackViewModel` | ⏳ PENDIENTE | Rollbacks |
| `RagQualityControlViewModel` | ⏳ PENDIENTE | Control de calidad |
| `RagBiasDetectionViewModel` | ⏳ PENDIENTE | Detección de sesgos |
| `RagComplianceViewModel` | ⏳ PENDIENTE | Compliance |
| `RagMonitoringViewModel` | ⏳ PENDIENTE | Monitoreo |
| `RagAccessControlViewModel` | ⏳ PENDIENTE | Control de acceso |

## 6. Integración con Gobierno

### 6.1 Campos de Gobierno en RagSystem
- `ragcompliance` - Estado de cumplimiento normativo
- `raggovernancestatus` - Estado de aprobación de gobierno
- `ragrisklevel` - Nivel de riesgo identificado

### 6.2 Auditoría
- Todas las operaciones CRUD usan `logActivity()`
- Operaciones auditadas:
  - `BUSCAR` - Búsquedas y filtrados
  - `CREAR` - Creación de sistemas (pendiente)
  - `EDITAR` - Modificaciones (pendiente)
  - `ELIMINAR` - Eliminación de sistemas

## 7. Pendientes de Implementación

### 7.1 Backend Integration
- ⏳ **leka-server**: Integración con Python backend para:
  - Indexación de documentos
  - Procesamiento de queries RAG
  - Cálculo de métricas de calidad
  - Detección de sesgos
  - Evaluación de compliance

### 7.2 Pantallas Adicionales
1. **Registry (Alta Prioridad)**
   - Vista de cuadrícula/lista
   - Analíticas de sistemas
   - Creación/edición de sistemas

2. **Data Sources (Alta Prioridad)**
   - Gestión de fuentes
   - Estado de indexación
   - Configuración de acceso

3. **Versioning & Rollback (Media Prioridad)**
   - Historial de versiones
   - Ejecución de rollbacks
   - Comparación de versiones

4. **Quality & Bias (Media Prioridad)**
   - Métricas de calidad
   - Detección de sesgos
   - Reportes de evaluación

5. **Monitoring & Compliance (Alta Prioridad)**
   - Monitoreo en tiempo real
   - Alertas de compliance
   - Dashboard de métricas

6. **Access Control (Baja Prioridad)**
   - Permisos por sistema
   - Roles y usuarios
   - Auditoría de accesos

### 7.3 ViewModels Restantes
Todos los ViewModels pendientes deben seguir el patrón `BaseFront`:
- Extender `BaseFront<T>`
- `@Init(superclass = true)`
- `@Destroy` con cleanup
- `logActivity()` para auditoría
- `businessService.save()` y `businessService.removeFromID()`
- `new Criteria(Operation.AND, Evaluation.XXX, campo, valor)`

## 8. Notas de Implementación

### 8.1 Patrón de Desarrollo
✅ **Aplicado consistentemente:**
- JPA entities con prefijo `rag_`
- Scripts SQL consolidados
- ZKoss con Bootstrap 5 puro (sin componentes ZKoss)
- ViewModels con patrón `BaseFront`
- Auditoría completa con `logActivity()`

### 8.2 Consideraciones Técnicas
- Las métricas de calidad se calculan en el backend (leka-server)
- El frontend ZKoss solo muestra y gestiona los datos
- La indexación de documentos es asíncrona
- Los rollbacks deben validar impacto antes de ejecutar

### 8.3 Siguiente Fase
**Prioridad de implementación:**
1. **Registry** - Para crear/editar sistemas completos
2. **Data Sources** - Para gestionar fuentes de datos
3. **Monitoring** - Para monitoreo en tiempo real
4. **Versioning/Rollback** - Para gestión de versiones
5. **Quality/Bias** - Para evaluación de calidad
6. **Compliance** - Para cumplimiento normativo
7. **Access Control** - Para seguridad

---

## 📊 Progreso General

- ✅ Entidades JPA: **4/4** (100%)
- ✅ Scripts SQL: **1/1** (100%)
- ✅ Pantallas ZKoss: **7/7** (100%) - Overview, Registry, Data Sources, Rollback, Bias Detection, Compliance, Access Control
- ✅ Pantallas Secundarias Existentes: **11 archivos** (Versioning, Monitoring, Quality Control)
- ✅ ViewModels: **7/7** (100%) - Todos implementados con patrón BaseFront
- ⏳ Integración leka-server: **0%** (pendiente)

### ✅ Completado (Fases 1, 2 y 3):

**Fase 1 - Fundación:**
- ✅ Overview - Dashboard principal (page.zul + summary.zul + ViewModel)
- ✅ Registry - Vista grid/list + Modal CRUD + ViewModel
- ✅ Data Sources - Dashboard + Gestión de fuentes (page.zul + 4 secundarias + ViewModel)

**Fase 2 - Screens Existentes:**
- ✅ Versioning - Ya existe (overview.zul, page.zul)
- ✅ Monitoring - Ya existe (3 archivos: health-dashboard, metrics-summary, usage-by-agent)
- ✅ Quality Control - Ya existe (2 archivos: chunk-distribution, retrieval-quality)

**Fase 3 - Compliance & Security (COMPLETA):**
- ✅ Rollback - page.zul + RagRollbackViewModel (con integración gobierno alto impacto)
- ✅ Bias Detection - page.zul + RagBiasDetectionViewModel (reportes a gobierno)
- ✅ Compliance - page.zul + RagComplianceViewModel (políticas de gobierno integradas)
- ✅ Access Control - page.zul + RagAccessControlViewModel (auditoría centralizada)

### 🔗 Integración con Gobierno Implementada:

**Rollback:**
- `logActivity('ALERTAR', 'GOVERNANCE')` para rollbacks de alto impacto
- Validación de impacto con alertas visuales
- Historial completo con duración y razones

**Bias Detection:**
- Botón "Reportar a Gobierno" para fuentes de alto riesgo (score >= 70)
- Alertas automáticas para fuentes críticas
- Cálculo de conformidad y métricas de riesgo

**Compliance:**
- Vista de políticas de gobierno activas
- Integración con dashboard de gobierno
- Monitoreo de cumplimiento por sistema
- Estados: COMPLIANT, NON_COMPLIANT, UNDER_REVIEW

**Access Control:**
- Auditoría centralizada integrada con gobierno
- Logs de acceso: GRANTED, DENIED, PERMISSION_CHANGE
- Revocación de acceso con registro en gobierno
- Gestión de roles y permisos

**Última actualización:** 2025-01-29 (Fase 3 COMPLETA)


