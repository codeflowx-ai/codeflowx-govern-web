# Guía Interna de Verificación CodeflowX OS

## 1. Objetivo
Establecer el protocolo para revisar código generado por agentes y asegurar que los artefactos AI OS (servicios, BPMN, reglas, UI, datasets) cumplen estándares CodeflowX antes de incorporarse a un release.

## 2. Alcance
- Módulos: `faas-core`, `faas-workflow-lib`, `faas-frameworks/*`, `codeflowx-govern-backend`, `codeflowx-studio`, proyectos satélite (ej. marketplace, connectors).
- Artefactos: Java/Kotlin/Python, BPMN, Drools, YAML/JSON metamodel, ZUL/UI, SQL (Flyway), prompts Markdown.
- Aplica a agentes sectoriales y genéricos (AI OS marketplace, runtime utilidades, analíticas globales) operando vía `PROMPTS_001/002/003` y SDK (PROMPTS_15-17).

## 3. Checklist de Revisión de Código
- **Naming & Prefijos**: tabalas `xxx_<modulo>`, campos `id` BIGSERIAL, DTOs CamelCase, paquetes `com.codeflowx`.
- **Arquitectura**: hexagonal, puertos/adaptadores separados, sin lógica en controladores ni repos.
- **Dependencias**: usar BOM (`faas-bom`), prohibidas libs no aprobadas.
- **DTO/Entidad**: MapStruct presente, validaciones Bean Validation, DTO = entidad Python cuando aplique.
- **Procesos BPMN**: extensión `.bpmn`, namespace `codeflowx`, tasks con delegates `Faas*` o `Aio*`, listeners configurados.
- **Reglas Drools**: `package` correcto, imports limpios, agenda-group documentada, evita lógica duplicada.
- **Recursos YAML/JSON**: schema válido, rutas a procesos/reglas dentro de módulo correspondiente (sectorial o genérico), sin referencias absolutas.
- **UI ZK**: ViewModel en `com.codeflowx.faas.ui`, ZUL con etiquetas estándar, sin estilos `btn-` en iconos.
- **Tests**: mínimo unitarios (JUnit/Testcontainers), verificación de repos/services, tests BPMN (Flowable) cuando haya delegates nuevos.
- **Documentación**: README sectorial/genérico actualizado, prompts referencian rutas correctas, `agent_execution_log.md` poblado si hubo run.

## 4. Validaciones Automatizadas
1. `mvn -pl <modulos> -am verify`.
2. `npm/yarn test` para UI si aplica.
3. Analizador estático (`spotbugs`, `checkstyle`, `eslint`).
4. Validación BPMN (`flowable-validate` script).
5. Validación Drools (`kie-maven-plugin`).
6. Tests Python (`pytest`) cuando se generen micros.

Registrar resultados en `agent_execution_log.md` con hash del commit y agente responsable.

## 5. Validación Funcional
- **Deploy sandbox**: usar entorno `aios-sbx` (Kubernetes namespace).
- **Cargar pruebas**: ejecutar procesos clave (`ai-component-onboarding`, `ai-marketplace-publish`).
- **Verificar telemetría**: dashboards Grafana (`monitoring/grafana/*`), métricas Prometheus.
- **Auditoría**: verificar entradas en `ImmutableLog` y `cor_auditlog`.
- **Webhooks**: simular receptores (WireMock) y confirmar reintentos.
- **MCP**: cliente de prueba (`governance-mcp-adapter-it`) para handshake y métodos.

## 6. Criterios de Aceptación
- Tests `verify` en verde.
- Cobertura > 70% en nuevo código crítico (servicios, reglas).
- BPMN ejecuta end-to-end sin incidentes.
- Webhooks entregan decisión < 5s p95.
- Sin vulnerabilidades críticas (OWASP Dependency Check).
- Documentación/README/prompt actualizado.

## 7. Gestión de Incidencias
- Registrar defectos en Jira `AIOS-*`.
- Etiquetas recomendadas: `compliance`, `runtime`, `integration`.
- Prioridad:
  - P0: ruptura runtime/seguridad.
  - P1: incumplimiento normativo/telemetría.
  - P2: bug funcional menor.
- Adjuntar evidencias: logs, screenshot dashboards, payloads.

## 8. Roles y Responsables
- **AI OS Lead**: aprobación final, definición estándares.
- **QA Técnico**: ejecución checklist, automatización pruebas.
- **Arquitecto Sectorial**: validador de metamodelos y prompts.
- **Dev Sectorial**: responsable de fixes y actualizaciones.
- **Compliance Officer**: verificación Art. 19/12 y matrices normativas.

## 9. Flujo de Trabajo
1. Agente genera artefacto → se almacena en módulo correspondiente.
2. Developer ejecuta verificación automática (Sección 4).
3. QA técnico valida checklist + funcional.
4. Se documenta en `agent_execution_log.md` y se adjunta `knowledge_gap_report.md` si falta normativa.
5. Merge sólo tras aprobar en PR con reviewers obligatorios (Lead + QA + Compliance).

## 10. Referencias
- `docs/PROMPTS_001_AI_OS_ARCH.md`, `PROMPTS_002_AI_OS_RUNTIME.md`, `PROMPTS_003_AI_OS_GOV.md`.
- `docs/PROMPTS_FAAS_*` para generación por sector.
- `docs/templates/*` para Flyway, MapStruct, Repos.
- `docs/AI_OS_OVERVIEW.md` para visión global.
- `docs/aios/INTEGRACION_CLIENTES_CODEFLOWX_OS.md` para guías clientes.


