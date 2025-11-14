# PROMPT: Implementación de Microservicios FaaS Sectoriales

## 1. Objetivo
Diseñar e implementar microservicios de analítica y cumplimiento que den soporte a los frameworks FaaS sectoriales de CodeflowX, expuestos como artefactos reutilizables para clientes y partners. El micro debe seguir arquitectura hexagonal, exponer APIs contract-first y publicar métricas/KPIs asociadas al framework del catálogo.

## 2. Alcance
- Microservicios por normativa o dominio (ENS, ISO 27001, Agroalimentario, Sanidad, etc.).
- Gestión de métricas y analíticas en tiempo real para los frameworks definidos en `codeflowx.govern.faas`.
- Registro y consulta de evidencias/auditoría.
- Integración con discovery (endpoints y credenciales) y con el data lake corporativo.

## 3. Requisitos Funcionales
1. `GET /api/faas/{framework}/metrics`
   - Devuelve catálogo y valores actuales de KPIs.
   - Parámetros: `tenantId` (header `X-Tenant-Id`), `geo`, filtros opcionales.
2. `GET /api/faas/{framework}/metrics/{kpiId}`
   - Serie temporal (intervalo configurable) + agregados (current, previous, delta).
3. `POST /api/faas/{framework}/events`
   - Ingesta de eventos `governance_event_v1` para recalcular métricas.
4. `POST /api/faas/{framework}/evidences`
   - Adjunta evidencias o referencias externas (URL, hash, metadata).
5. `GET /api/faas/{framework}/capabilities`
   - Responde con lista de normativas soportadas y estado (`READY`, `PENDING`, `DEGRADED`).

## 4. Modelo de Datos (contract-first)
- Usar OpenAPI 3.1 (`docs/openapi/faas/{framework}-micro.yaml`).
- DTOs (Java records) reflejando:
  ```json
  {
    "framework": "ENV_SERVICES_v1.0.0",
    "kpis": [
      {
        "id": "governance.kpi.env_services.carbon_intensity",
        "label": "Índice de intensidad de carbono",
        "unit": "kgCO2e/MWh",
        "value": 34.7,
        "trend": "DOWN",
        "last_updated": "2025-11-10T09:15:00Z",
        "metadata": {"geo": "EU", "criticality": "HIGH"}
      }
    ],
    "sources": ["iot.scada", "erp.energy"]
  }
  ```
- JSON Schema base en `codeflowx.govern.faas/src/main/resources/jsonschema/faas_metric_payload.json` (crear si no existe).

## 5. Arquitectura Técnica
- **Stack**: Java 21, Spring Boot 3, Gradle/Maven multi-module.
- **Módulos**:
  - `application`: casos de uso (`CalculateMetricsUseCase`, `IngestEventsUseCase`).
  - `domain`: agregados (`Metric`, `Evidence`, `FrameworkCapability`).
  - `infrastructure`: adapters REST, JPA, mensajería.
- **Persistencia**:
  - PostgreSQL (`cor_metrics_registry`, `cor_evidences`).
  - Redis como caché de KPIs (TTL configurable).
- **Mensajería**: Kafka tópico `govern.faas.events` para cálculos asincrónicos.
- **Seguridad**: OAuth2 client credentials, scopes `faas.metrics.read`, `faas.metrics.write`.
- **Observabilidad**: Micrometer + Prometheus (`faas_metric_total`, `faas_event_processed_total`).

## 6. Integraciones
- Discovery (`GET /api/v1/discovery/frameworks/{code}`) para obtener endpoints externos.
- Data Lake: publicar eventos normalizados a través de Kafka.
- MCP/SKD: exponer cliente Java en `codeflowx.govern.faas` (`FaasMetricsClient`).

## 7. Guía de Implementación
1. Generar proyecto con arquetipo `codeflowx-archetype-faas-micro` (si no existe, crear prompt aparte).
2. Definir OpenAPI y generar stubs con Spring Cloud Contract + MapStruct para mapeo DTO ↔ dominio.
3. Implementar casos de uso siguiendo SOLID/KISS.
4. Configurar pipelines CI (maven verify, pruebas contractuales).
5. Empaquetar Helm chart con values para endpoints y credenciales.

## 8. Entregables
- Código fuente del micro (repositorio dedicado) + documentación README.
- Artefacto Docker publicado en registry corporativo.
- OpenAPI versionado y publicable a clientes.
- Tests: unitarios (JUnit5), integración (Testcontainers + PostgreSQL + Redis), contract tests.

## 9. Consideraciones de Calidad
- Latencia objetivo: <200ms para consultas de métricas.
- Resiliencia: circuit breaker (Resilience4j) hacia fuentes externas.
- Auditoría: logs estructurados con `traceId`, `tenantId`, `framework`.
- Cumplir normativa ENS/ISO (logs, backup, cifrado en tránsito y reposo).

## 10. Checklist para Entrega
- [ ] OpenAPI validado (`spectral lint`).
- [ ] Documentación de despliegue (k8s + docker-compose).
- [ ] Scripts de inicialización de tablas + flyway.
- [ ] Dashboards de Grafana (JSON) exportables.
- [ ] Registro de capabilities actualizado en `metamodel/frameworks/...`.
