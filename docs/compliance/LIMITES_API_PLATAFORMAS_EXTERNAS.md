# LÍMITES Y CUOTAS DE APIs EXTERNAS

**Fecha:** Noviembre 2025  
**Incidencia:** INC-INT-007  
**Prioridad:** 🟡 MEDIA  
**Tipo:** Documentación

---

## CONTEXTO

Este documento detalla los límites y cuotas de las APIs de las plataformas externas con las que CodeflowX se integra, y cómo CodeflowX los maneja para evitar consumo inesperado y costos adicionales.

---

## DATABRICKS

### Límites de API

| Métrica | Límite | Unidad |
|---------|--------|--------|
| **Requests por minuto** | 600 | requests/min |
| **Requests por hora** | 10,000 | requests/hora |
| **Concurrent requests** | 20 | requests simultáneos |
| **Rate limit (429)** | Después de 600 req/min | Retry-After: 60s |

### Costos

- **API calls**: No hay costo directo por request
- **Compute time**: Facturado por segundo de uso de clusters
- **Storage**: Facturado por GB almacenado en DBFS

### Cómo CodeflowX lo maneja

1. **Frecuencia de sync:** Configurable (default: 1 hora)
2. **Rate limiting preventivo:** Limitar a 60 requests/minuto por plataforma
3. **Retry con backoff:** 3 intentos con backoff exponencial si 429
4. **Cache:** Cachear resultados de listado de modelos durante sync window

### Configuración

```properties
# Databricks rate limiting
databricks.sync.frequency-hours=1
databricks.api.rate-limit-requests-per-minute=60
databricks.api.retry.max-attempts=3
databricks.api.retry.backoff-ms=1000
```

---

## SNOWFLAKE

### Límites de API

| Métrica | Límite | Unidad |
|---------|--------|--------|
| **Queries concurrentes** | Depende de warehouse size | queries simultáneas |
| **Queries por hora** | Sin límite estricto | queries/hora |
| **Query timeout** | 2 horas (configurable) | segundos |
| **JDBC connections** | Depende de warehouse | conexiones simultáneas |

### Costos

- **Compute time**: Facturado por segundo de uso de warehouse
- **Storage**: Facturado por GB almacenado
- **Data transfer**: Facturado por GB transferido (egress)

### Cómo CodeflowX lo maneja

1. **Frecuencia de sync:** Configurable (default: 6-24 horas)
2. **Sample queries:** Solo leer samples pequeños (1K-10K filas), NO full scan
3. **Connection pooling:** Reutilizar conexiones JDBC
4. **Query timeout:** 30 segundos máximo por query
5. **Cache metadata:** Cachear resultados de INFORMATION_SCHEMA

### Configuración

```properties
# Snowflake rate limiting
snowflake.sync.frequency-hours=6
snowflake.query.timeout-seconds=30
snowflake.query.sample-size-rows=1000
snowflake.connection.pool-size=5
```

---

## AZURE ML

### Límites de API

| Métrica | Límite | Unidad |
|---------|--------|--------|
| **API calls por minuto** | 100 | calls/min |
| **API calls por hora** | 6,000 | calls/hora |
| **Concurrent requests** | 10 | requests simultáneos |
| **Rate limit (429)** | Después de 100 calls/min | Retry-After: 60s |

### Costos

- **API calls**: No hay costo directo
- **Compute time**: Facturado por segundo de uso de compute
- **Storage**: Facturado por GB almacenado en workspace
- **Metrics queries**: Facturado por query a Azure Monitor

### Cómo CodeflowX lo maneja

1. **Frecuencia de sync:** Configurable (default: 1 hora)
2. **Rate limiting preventivo:** Limitar a 50 calls/minuto por workspace
3. **Batch metrics:** Agrupar múltiples métricas en una query
4. **Cache:** Cachear resultados de listado de deployments

### Configuración

```properties
# Azure ML rate limiting
azure-ml.sync.frequency-hours=1
azure-ml.api.rate-limit-calls-per-minute=50
azure-ml.metrics.batch-size=10
```

---

## AWS SAGEMAKER

### Límites de API

| Métrica | Límite | Unidad |
|---------|--------|--------|
| **API calls por segundo** | 100 | calls/seg |
| **Concurrent requests** | 20 | requests simultáneos |
| **Throttling (429)** | Después de 100 calls/seg | Retry-After: 1s |

### Costos

- **API calls**: No hay costo directo
- **Compute time**: Facturado por segundo de uso de endpoints
- **CloudWatch queries**: Facturado por query (1000 queries = $0.10)

### Cómo CodeflowX lo maneja

1. **Frecuencia de sync:** Configurable (default: 1 hora)
2. **Rate limiting preventivo:** Limitar a 50 calls/segundo
3. **CloudWatch batching:** Agrupar métricas para reducir queries

### Configuración

```properties
# SageMaker rate limiting
sagemaker.sync.frequency-hours=1
sagemaker.api.rate-limit-calls-per-second=50
sagemaker.cloudwatch.batch-metrics=true
```

---

## GOOGLE VERTEX AI

### Límites de API

| Métrica | Límite | Unidad |
|---------|--------|--------|
| **API calls por minuto** | 600 | calls/min |
| **API calls por hora** | 36,000 | calls/hora |
| **Concurrent requests** | 20 | requests simultáneos |

### Costos

- **API calls**: No hay costo directo
- **Compute time**: Facturado por segundo de uso
- **Storage**: Facturado por GB almacenado

### Cómo CodeflowX lo maneja

Similar a Azure ML / SageMaker.

---

## MICROSOFT GRAPH API (COPILOT)

### Límites de API

| Métrica | Límite | Unidad |
|---------|--------|--------|
| **Requests por segundo** | 10,000 | requests/seg |
| **Requests por minuto** | 600,000 | requests/min |
| **Concurrent requests** | 4 | requests simultáneos por app |
| **Throttling (429)** | Cuando se excede | Retry-After variable |

### Costos

- **API calls**: No hay costo directo
- **Storage**: Facturado por GB almacenado en OneDrive/SharePoint

### Cómo CodeflowX lo maneja

1. **Frecuencia de sync:** Configurable (default: 1 hora)
2. **Rate limiting preventivo:** Limitar a 100 requests/minuto
3. **Batch requests:** Agrupar múltiples queries cuando sea posible

---

## ESTRATEGIA GENERAL DE THROTTLING

### Prevención de Rate Limiting

1. **Configurar frecuencia de sync conservadora:**
   - Databricks: 1 hora (no cada minuto)
   - Snowflake: 6-24 horas (no cada hora)
   - Azure ML: 1 hora

2. **Implementar rate limiting preventivo:**
   - Limitar requests antes de llegar al límite de la plataforma
   - Usar 50-70% del límite para dejar margen

3. **Cachear resultados:**
   - Cachear metadata de modelos/datasets
   - Invalidar cache solo cuando sea necesario

4. **Batch requests:**
   - Agrupar múltiples operaciones en una request
   - Usar endpoints batch cuando estén disponibles

### Manejo de Rate Limiting (429)

1. **Respeta Retry-After header:**
   - Esperar tiempo especificado antes de reintentar
   - No hacer polling agresivo

2. **Backoff exponencial:**
   - Reintentar con delay creciente
   - Max 3-5 reintentos

3. **Alertar administradores:**
   - Si rate limiting es persistente, alertar
   - Puede indicar configuración incorrecta o uso excesivo

---

## MONITOREO DE CUOTAS

### Métricas a Monitorear

- **Requests por minuto/hora** por plataforma
- **Rate limit errors (429)** por plataforma
- **Costos estimados** de API calls (si aplicable)
- **Cumplimiento de límites** (% del límite usado)

### Dashboard Recomendado

Crear dashboard Grafana mostrando:
- Requests/minuto por plataforma
- Rate limit errors
- Tiempo de respuesta de APIs
- Alertas cuando se aproxima al límite (80% del límite)

---

## RECOMENDACIONES

1. **Configuración inicial conservadora:**
   - Empezar con frecuencias de sync bajas
   - Aumentar gradualmente si es necesario

2. **Monitoreo continuo:**
   - Revisar métricas de uso semanalmente
   - Ajustar configuración según necesidad

3. **Comunicación con clientes:**
   - Informar sobre límites y costos potenciales
   - Ofrecer configuración de frecuencias según necesidades

---

**Última Actualización:** Noviembre 2025  
**Próxima Revisión:** Enero 2026

