# Verificación del Backend de Telemetría

## Resumen
Este documento verifica que el backend está preparado para proporcionar toda la información necesaria para el frontend de telemetría.

## Estado de los DTOs

### ✅ TelemetryKpisResponseDto
- `totalEvents`: Long ✅
- `totalCostUsd`: Double ✅
- `totalTokens`: Double ✅
- `avgLatencyMs`: Double ✅
- `componentCount`: Integer ✅
- `startTime`: LocalDateTime ✅
- `endTime`: LocalDateTime ✅

**Estado**: Completo y correcto.

### ✅ TelemetryEventDto
- `id`: Long ✅
- `uuid`: String ✅
- `timestamp`: Timestamp ✅
- `componentUuid`: String ✅
- `agentExternalId`: String ✅
- `eventType`: String ✅
- `severity`: String ✅
- `traceId`: String ✅
- `runId`: String ✅
- `metrics`: String (JSONB) ✅
- `payload`: String (JSONB) ✅
- `sourceTool`: String ✅
- `biasChecked`: Boolean ✅
- `toxicityChecked`: Boolean ✅
- `piiDetected`: Boolean ✅
- `secretDetected`: Boolean ✅
- `analysisResults`: String (JSONB) ✅

**Estado**: Completo y correcto. Todos los campos de la entidad `AioTelemetry` están mapeados.

### ✅ TelemetryEventsResponseDto
- `events`: List<TelemetryEventDto> ✅
- `total`: Long ✅
- `page`: Integer ✅ (AGREGADO)
- `size`: Integer ✅ (AGREGADO)

**Estado**: Actualizado para incluir paginación.

### ✅ ComponentStatisticsDto
- `componentUuid`: String ✅
- `totalEvents`: Long ✅
- `avgLatencyMs`: Double ✅
- `totalTokens`: Double ✅
- `totalCostUsd`: Double ✅

**Estado**: Completo y correcto.

### ✅ ComponentStatisticsListResponseDto
- `statistics`: List<ComponentStatisticsDto> ✅
- `total`: Integer ✅

**Estado**: Completo y correcto.

## Estado de los Endpoints

### ✅ GET /api/v1/telemetry-analytics/kpis
**Microservicio**: `codeflowx-governance-telemetry-analytics-service`
**BFF**: `codeflowx.govern.bff.telemetry`

**Parámetros**:
- `startTime` (opcional): LocalDateTime
- `endTime` (opcional): LocalDateTime

**Respuesta**: `TelemetryKpisResponseDto`

**Implementación**:
- ✅ Repositorio: `getComponentStatistics()` extrae métricas del JSONB `TELMETRICS`
- ✅ Servicio de negocio: Agrega estadísticas por componente
- ✅ Controlador: Retorna DTO tipado

**Estado**: Funcional. El backend puede proporcionar todos los KPIs necesarios.

### ✅ GET /api/v1/telemetry-analytics/search/content
**Microservicio**: `codeflowx-governance-telemetry-analytics-service`
**BFF**: `codeflowx.govern.bff.telemetry`

**Parámetros**:
- `searchText`: String (requerido)
- `startTime` (opcional): LocalDateTime
- `endTime` (opcional): LocalDateTime
- `limit` (opcional, default: 100): Integer

**Respuesta**: `TelemetryEventsResponseDto`

**Implementación**:
- ✅ Repositorio: `searchByContent()` usa búsqueda ILIKE en JSONB `TELPAYLOAD`
- ✅ Servicio de negocio: Convierte entidades a DTOs
- ✅ Controlador: Retorna DTO tipado

**⚠️ Limitación**: Actualmente usa `limit` en lugar de paginación completa (`page`/`size`). El frontend puede adaptarse usando `limit = size` y calculando el offset manualmente, pero sería mejor implementar paginación real.

**Estado**: Funcional, pero mejorable con paginación completa.

### ✅ GET /api/v1/telemetry-analytics/search/project
**Microservicio**: `codeflowx-governance-telemetry-analytics-service`
**BFF**: `codeflowx.govern.bff.telemetry`

**Parámetros**:
- `projectId`: Long (requerido)
- `startTime` (opcional): LocalDateTime
- `endTime` (opcional): LocalDateTime

**Respuesta**: `TelemetryEventsResponseDto`

**Implementación**:
- ✅ Servicio de negocio: Obtiene componentes del proyecto desde BD de governance, luego busca eventos
- ✅ Repositorio: `findByComponentUuidsAndTimeRange()` busca eventos por lista de UUIDs
- ✅ Controlador: Retorna DTO tipado

**⚠️ Limitación**: No tiene paginación. Retorna todos los eventos encontrados.

**Estado**: Funcional, pero mejorable con paginación.

### ✅ GET /api/v1/telemetry-analytics/components/{componentUuid}/events
**Microservicio**: `codeflowx-governance-telemetry-analytics-service`
**BFF**: `codeflowx.govern.bff.telemetry`

**Parámetros**:
- `componentUuid`: String (path variable)
- `startTime` (opcional): LocalDateTime
- `endTime` (opcional): LocalDateTime

**Respuesta**: `TelemetryEventsResponseDto`

**Implementación**:
- ✅ Repositorio: `findByComponentUuidAndTimeRange()` busca eventos por componente
- ✅ Servicio de negocio: Convierte entidades a DTOs
- ✅ Controlador: Retorna DTO tipado

**⚠️ Limitación**: No tiene paginación. Retorna todos los eventos encontrados.

**Estado**: Funcional, pero mejorable con paginación.

### ✅ GET /api/v1/telemetry-analytics/components/statistics
**Microservicio**: `codeflowx-governance-telemetry-analytics-service`
**BFF**: `codeflowx.govern.bff.telemetry`

**Parámetros**:
- `startTime` (opcional): LocalDateTime
- `endTime` (opcional): LocalDateTime

**Respuesta**: `ComponentStatisticsListResponseDto`

**Implementación**:
- ✅ Repositorio: `getComponentStatistics()` usa SQL nativo para agregar métricas del JSONB
- ✅ Servicio de negocio: Convierte resultados a DTOs
- ✅ Controlador: Retorna DTO tipado

**Estado**: Funcional. El backend puede proporcionar todas las estadísticas necesarias.

## Extracción de Métricas del JSONB

El repositorio extrae métricas del campo JSONB `TELMETRICS` usando SQL nativo:

```sql
AVG((TELMETRICS->>'latency_ms')::numeric) as avgLatencyMs
SUM((TELMETRICS->>'tokens_used')::numeric) as totalTokens
SUM((TELMETRICS->>'cost_usd')::numeric) as totalCostUsd
```

**Formato esperado del JSONB**:
```json
{
  "latency_ms": 1200,
  "tokens_used": 450,
  "cost_usd": 0.012
}
```

**Estado**: ✅ Correcto. El backend puede extraer todas las métricas necesarias.

## Campos de la Entidad AioTelemetry

Todos los campos de la entidad están mapeados correctamente en el DTO `TelemetryEventDto`:

- ✅ `idxtelemetry` → `id`
- ✅ `iduuid` → `uuid`
- ✅ `teltimestamp` → `timestamp`
- ✅ `telcomponentuuid` → `componentUuid`
- ✅ `telagentexternalid` → `agentExternalId`
- ✅ `televenttype` → `eventType`
- ✅ `telseverity` → `severity`
- ✅ `teltraceid` → `traceId`
- ✅ `telrunid` → `runId`
- ✅ `telmetrics` → `metrics` (JSONB como String)
- ✅ `telpayload` → `payload` (JSONB como String)
- ✅ `telsourcetool` → `sourceTool`
- ✅ `telbiaschecked` → `biasChecked`
- ✅ `teltoxicitychecked` → `toxicityChecked`
- ✅ `telpiidetected` → `piiDetected`
- ✅ `telsecretdetected` → `secretDetected`
- ✅ `telanalysisresults` → `analysisResults` (JSONB como String)

**Estado**: ✅ Todos los campos están mapeados correctamente.

## Limitaciones Identificadas

### 1. Paginación Incompleta
- **Problema**: Los endpoints de búsqueda usan `limit` en lugar de paginación completa (`page`/`size`).
- **Impacto**: El frontend puede adaptarse, pero sería mejor tener paginación real.
- **Solución recomendada**: Implementar paginación usando `Pageable` de Spring Data o agregar parámetros `page` y `size` manualmente.

### 2. Búsqueda por Proyecto sin Paginación
- **Problema**: `searchByProject` retorna todos los eventos sin límite.
- **Impacto**: Puede ser lento con muchos eventos.
- **Solución recomendada**: Agregar paginación.

### 3. Eventos por Componente sin Paginación
- **Problema**: `getEventsByComponent` retorna todos los eventos sin límite.
- **Impacto**: Puede ser lento con muchos eventos.
- **Solución recomendada**: Agregar paginación.

## Conclusión

### ✅ Datos que el Backend PUEDE Proporcionar

1. **KPIs Agregados**: ✅ Totalmente funcional
   - Total de eventos
   - Costo total en USD
   - Total de tokens
   - Latencia promedio
   - Número de componentes

2. **Estadísticas por Componente**: ✅ Totalmente funcional
   - Eventos por componente
   - Latencia promedio por componente
   - Tokens totales por componente
   - Costo total por componente

3. **Eventos Individuales**: ✅ Totalmente funcional
   - Todos los campos de la entidad están disponibles
   - Métricas extraídas del JSONB
   - Payload completo disponible
   - Resultados de análisis de gobernanza disponibles

4. **Búsquedas**: ✅ Funcional con limitaciones
   - Búsqueda por contenido en payload
   - Búsqueda por proyecto
   - Búsqueda por componente

### ⚠️ Mejoras Recomendadas

1. Implementar paginación completa (`page`/`size`) en todos los endpoints de búsqueda
2. Agregar límites por defecto para evitar respuestas muy grandes
3. Considerar agregar filtros adicionales (por severity, eventType, etc.) en el repositorio

### ✅ Estado General

**El backend está preparado para proporcionar toda la información necesaria para el frontend**, aunque con algunas limitaciones menores en la paginación que pueden mejorarse en el futuro.

Los mocks del frontend reflejan correctamente la estructura de datos que el backend proporciona.
