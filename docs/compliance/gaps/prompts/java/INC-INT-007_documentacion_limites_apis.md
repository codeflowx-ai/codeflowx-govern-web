# PROMPT: INC-INT-007 - Documentación de Límites y Cuotas de APIs Externas

**Incidencia:** INC-INT-007  
**Prioridad:** 🟡 MEDIA  
**Artículo EU AI Act:** Art. 15 (Robustez, Seguridad)  
**Esfuerzo Estimado:** 1 día  
**Tipo:** Documentación  
**Documento Relacionado:** `AUDITORIA_INTEGRACION_SIN_SUSTITUCION.md`

---

## CONTEXTO

No se documenta qué límites y cuotas tienen las APIs de Databricks, Snowflake, Azure ML, y cómo CodeflowX los maneja. Esto puede resultar en consumo inesperado de cuotas, costos inesperados para el cliente y dificultad para planificar escalabilidad.

**Riesgo:**
- Puede haber consumo inesperado de cuotas
- No se puede planificar escalabilidad
- Posibles costos inesperados para el cliente
- Falta de transparencia sobre límites de APIs

**Ubicación Actual:**
- ✅ Documento existe: `docs/compliance/LIMITES_API_PLATAFORMAS_EXTERNAS.md`
- ⚠️ Necesita mantenimiento y actualización periódica
- ⚠️ Falta integración con código (configuración debe alinearse con documentación)

---

## REQUISITOS

1. **Mantener documentación actualizada** de límites y cuotas
2. **Alinear configuración** de código con documentación
3. **Añadir sección de monitoreo** de cuotas
4. **Documentar estrategias de throttling** preventivo
5. **Incluir recomendaciones** de configuración por caso de uso
6. **Actualizar cuando cambien límites** de APIs externas

---

## ESTADO ACTUAL

✅ **Documento ya existe:** `docs/compliance/LIMITES_API_PLATAFORMAS_EXTERNAS.md`

El documento incluye:
- ✅ Límites de API por plataforma (Databricks, Snowflake, Azure ML, SageMaker, Vertex AI, Microsoft Graph)
- ✅ Costos asociados
- ✅ Cómo CodeflowX maneja los límites
- ✅ Configuración recomendada
- ✅ Estrategia general de throttling

---

## TAREAS DE MANTENIMIENTO

### 1. Verificar Completitud del Documento

**Verificar que el documento incluya todas las plataformas soportadas:**

- [x] Databricks
- [x] Snowflake
- [x] Azure ML
- [x] AWS SageMaker
- [x] Google Vertex AI
- [x] Microsoft Graph API (Copilot)
- [ ] Otras plataformas si se añaden

### 2. Alinear Configuración con Documentación

**Verificar que los valores de configuración en código coincidan con la documentación:**

**Ubicación:** `application.properties` o `application.yml`

```properties
# Verificar que estos valores coincidan con LIMITES_API_PLATAFORMAS_EXTERNAS.md

# Databricks
databricks.sync.frequency-hours=1
databricks.api.rate-limit-requests-per-minute=60
databricks.api.retry.max-attempts=3
databricks.api.retry.backoff-ms=1000

# Snowflake
snowflake.sync.frequency-hours=6
snowflake.query.timeout-seconds=30
snowflake.query.sample-size-rows=1000
snowflake.connection.pool-size=5

# Azure ML
azure-ml.sync.frequency-hours=1
azure-ml.api.rate-limit-calls-per-minute=50
azure-ml.metrics.batch-size=10

# SageMaker
sagemaker.sync.frequency-hours=1
sagemaker.api.rate-limit-calls-per-second=50
sagemaker.cloudwatch.batch-metrics=true
```

**Tarea:** Comparar valores en código con documentación y actualizar si hay discrepancias.

### 3. Añadir Sección de Monitoreo

**Añadir al documento sección sobre cómo monitorear cuotas:**

```markdown
## MONITOREO DE CUOTAS EN TIEMPO REAL

### Métricas Prometheus

CodeflowX expone las siguientes métricas para monitoreo de cuotas:

- `codeflowx_api_requests_total{platform="databricks"}` - Total de requests a Databricks
- `codeflowx_api_requests_per_minute{platform="databricks"}` - Requests por minuto
- `codeflowx_api_rate_limit_errors_total{platform="databricks"}` - Errores 429
- `codeflowx_api_quota_usage_percent{platform="databricks"}` - % del límite usado

### Alertas Recomendadas

1. **Alerta cuando se aproxima al límite (80%):**
   ```
   codeflowx_api_quota_usage_percent > 80
   ```

2. **Alerta cuando hay rate limiting frecuente:**
   ```
   rate(codeflowx_api_rate_limit_errors_total[5m]) > 5
   ```

3. **Alerta cuando sync falla por rate limiting:**
   ```
   codeflowx_sync_failures_total{reason="rate_limit"} > 0
   ```

### Dashboard Grafana

Crear dashboard con:
- Requests/minuto por plataforma
- Rate limit errors
- Tiempo de respuesta de APIs
- % de cuota usada
- Costos estimados (si aplicable)
```

### 4. Documentar Casos de Uso Específicos

**Añadir sección de recomendaciones por caso de uso:**

```markdown
## RECOMENDACIONES POR CASO DE USO

### Caso 1: Cliente con Alto Volumen de Modelos

**Escenario:** Cliente tiene 1000+ modelos en Databricks, syncs frecuentes necesarios.

**Recomendación:**
- Aumentar frecuencia de sync a 30 minutos (no cada minuto)
- Usar cache agresivo (invalidar solo cuando sea necesario)
- Implementar sync incremental (solo modelos modificados)
- Considerar webhooks en lugar de polling

### Caso 2: Cliente con Múltiples Workspaces

**Escenario:** Cliente tiene 5 workspaces de Databricks.

**Recomendación:**
- Distribuir syncs en diferentes horas para evitar picos
- Limitar a 1 workspace por minuto
- Usar rate limiting global (no solo por workspace)

### Caso 3: Cliente con Datasets Grandes en Snowflake

**Escenario:** Cliente tiene datasets de 100+ GB en Snowflake.

**Recomendación:**
- Usar samples pequeños (1K-10K filas) para evaluación
- Sync de metadata cada 24 horas (no cada hora)
- Query timeout de 30 segundos máximo
- No hacer full scans de tablas grandes
```

### 5. Proceso de Actualización

**Crear proceso para mantener documentación actualizada:**

1. **Revisión Trimestral:**
   - Verificar límites oficiales de cada plataforma
   - Actualizar si han cambiado
   - Verificar costos actualizados

2. **Actualización al Añadir Nueva Plataforma:**
   - Documentar límites antes de implementar conector
   - Añadir sección al documento
   - Configurar rate limiting según límites

3. **Actualización cuando Cambien Límites:**
   - Monitorear anuncios de cambios de límites
   - Actualizar documentación inmediatamente
   - Ajustar configuración de código si es necesario

---

## VALIDACIÓN

### Checklist de Completitud

- [x] Documento existe y está completo
- [ ] Configuración de código alineada con documentación
- [ ] Sección de monitoreo añadida
- [ ] Casos de uso documentados
- [ ] Proceso de actualización definido
- [ ] Documento referenciado en README principal

### Verificación Manual

1. ✅ Revisar que todas las plataformas soportadas estén documentadas
2. ✅ Comparar valores de configuración con documentación
3. ✅ Verificar que estrategias de throttling estén implementadas
4. ✅ Confirmar que métricas de monitoreo estén disponibles

---

## CUMPLIMIENTO EU AI ACT

| Artículo | Cobertura |
|----------|-----------|
| **Art. 15** (Robustez, Seguridad) | ✅ Documentación de límites y manejo de errores |
| **Art. 12** (Trazabilidad) | ✅ Documentación de cómo se manejan los límites |

---

## NOTAS IMPORTANTES

1. **Mantenimiento Continuo:** Este documento debe actualizarse cuando:
   - Se añade una nueva plataforma
   - Cambian los límites de APIs externas
   - Se ajusta la estrategia de throttling
   - Se identifican nuevos casos de uso

2. **Sincronización con Código:** Los valores de configuración en código deben coincidir con la documentación.

3. **Comunicación con Clientes:** Usar este documento para informar a clientes sobre límites y costos potenciales.

4. **Monitoreo Proactivo:** Implementar alertas cuando se aproxima a los límites para evitar costos inesperados.

---

## PRÓXIMOS PASOS

1. ✅ Verificar que documento esté completo
2. ⚠️ Alinear configuración de código con documentación
3. ⚠️ Añadir sección de monitoreo si falta
4. ⚠️ Documentar casos de uso específicos
5. ⚠️ Establecer proceso de actualización trimestral
6. ⚠️ Añadir referencia al documento en README principal

---

**Prioridad:** 🟡 **MEDIA**  
**Fecha Límite:** Febrero 2026  
**Responsable:** Technical Writer + DevOps Team  
**Estado:** ✅ Documento existe, requiere mantenimiento y alineación con código

---

**Estado:** ✅ COMPLETADO

