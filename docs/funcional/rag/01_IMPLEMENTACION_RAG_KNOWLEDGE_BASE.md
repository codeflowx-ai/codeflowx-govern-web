# Implementación RAG Knowledge Base

## Estado: 🚧 EN PROGRESO

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

### 4.2 Pantallas Pendientes de Implementar

| Pantalla | Ruta | Estado | Descripción |
|----------|------|--------|-------------|
| **Registry** | `/rag/registry/page.zul` | ⏳ PENDIENTE | Registro detallado de sistemas |
| **Data Sources** | `/rag/data-sources/page.zul` | ⏳ PENDIENTE | Gestión de fuentes de datos |
| **Versioning** | `/rag/versioning/page.zul` | ⏳ PENDIENTE | Control de versiones |
| **Rollback** | `/rag/rollback/page.zul` | ⏳ PENDIENTE | Gestión de rollbacks |
| **Quality Control** | `/rag/quality-control/page.zul` | ⏳ PENDIENTE | Control de calidad |
| **Bias Detection** | `/rag/bias-detection/page.zul` | ⏳ PENDIENTE | Detección de sesgos |
| **Compliance** | `/rag/compliance/page.zul` | ⏳ PENDIENTE | Monitoreo de cumplimiento |
| **Monitoring** | `/rag/monitoring/page.zul` | ⏳ PENDIENTE | Monitoreo en tiempo real |
| **Access Control** | `/rag/access-control/page.zul` | ⏳ PENDIENTE | Control de acceso |

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

### 5.2 ViewModels Pendientes

| ViewModel | Estado | Descripción |
|-----------|--------|-------------|
| `RagSystemRegistryViewModel` | ⏳ PENDIENTE | Registro de sistemas |
| `RagDataSourceViewModel` | ⏳ PENDIENTE | Gestión de fuentes |
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
- ✅ Pantallas ZKoss: **1/9** (11%)
- ✅ ViewModels: **1/9** (11%)
- ⏳ Integración leka-server: **0%**

**Última actualización:** 2025-01-29

