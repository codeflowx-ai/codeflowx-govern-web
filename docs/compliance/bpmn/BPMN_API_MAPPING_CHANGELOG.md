# Changelog - BPMN_API_MAPPING.md

## 2024-03-21: Actualización Arquitectura de Telemetría

### Cambios Principales

1. **Separación de Telemetría**: La telemetría se ha movido a un microservicio separado (`codeflowx-aios-telemetry`) debido al alto volumen de tráfico.

2. **Worker de Telemetría**: El worker `codeflowx-aios-telemetry-worker` es ahora el responsable de disparar procesos BPMN cuando detecta problemas en el análisis de gobernanza.

3. **Actualización de Referencias**:
   - Sección "Telemetría y Monitoreo" actualizada para reflejar la nueva arquitectura
   - Referencias al endpoint `POST /api/v1/aios/telemetry/events` marcadas como DEPRECATED
   - Nuevas referencias al worker `codeflowx-aios-telemetry-worker`

4. **Resumen Ejecutivo Actualizado**:
   - Endpoints que deben disparar procesos: **14 endpoints** (1 movido a worker)
   - Componentes que deben disparar procesos: **1 worker** (`codeflowx-aios-telemetry-worker`)
   - Total a implementar: **28 endpoints + 1 worker**

### Documentos Relacionados

- `GOVERNANCE_REALTIME_VS_IMMUTABLELOGS.md`: Detalles sobre gobernanza en tiempo real
- `TELEMETRY_MICROSERVICE_ARCHITECTURE.md`: Arquitectura completa del microservicio de telemetría
- `codeflowx-aios-telemetry/README.md`: Documentación del microservicio REST
- `codeflowx-aios-telemetry-worker/README.md`: Documentación del worker

### Notas Técnicas

- El endpoint `POST /api/v1/aios/telemetry/events` en el API principal está marcado como DEPRECATED
- Los agentes externos deben usar el microservicio `codeflowx-aios-telemetry` directamente
- El worker consume de RabbitMQ y dispara procesos BPMN según análisis de gobernanza
- Los procesos BPMN disparados desde telemetría incluyen: `bias-detection-v1`, `alert-response-v1`, `performance-degradation-v1`, `drift-detection-v1`, `ai-runtime-health-v1`, `ai-policy-review-v1`

