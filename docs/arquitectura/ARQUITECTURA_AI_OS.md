# 🧠 Arquitectura Capa AI OS – CodeflowX Govern

**Versión:** 0.1.0
**Fecha:** Noviembre 2025
**Audiencia:** Arquitectura, Plataformas, Compliance
**Confidencial:** Uso interno CodeflowX

---

## 🎯 Propósito

Formalizar la capa “AI Operating System” (AI OS) dentro de CodeflowX Govern para orquestar modelos, prompts, agentes, pipelines RAG y datasets bajo las mismas reglas de gobierno y cumplimiento EU AI Act ya implementadas. El objetivo es convertir la gobernanza documental en gobernanza ejecutable, automatizando políticas, despliegues y observabilidad extremo a extremo.

---

## 🔍 Contexto Actual

- **Entidades EU AI Act (`nocode.service.entitys`)** cubren conformidad (Art. 43), FRIAs (Art. 27), registro UE (Art. 49), catálogo Anexo III y trazabilidad inmutable (Art. 19/12).
- **Procesos BPMN (`codeflowx.govern.workflow.lib`)** gestionan aprobación de agentes, modelos y prompts, detección de sesgos, automatización de monitoreo y flujos de governance con Drools y Java Delegates.
- **Frameworks sectoriales FaaS** reutilizan prompts y playbooks específicos por industria, pero dependen de un runtime homogéneo.

👉 Falta una capa operativa que coordine inventario, despliegues, runtime de agentes y consumo de recursos bajo políticas comunes.

---

## 🏛️ Principios de Diseño

- Arquitectura hexagonal estricta (puertos/adaptadores).
- Dominios desacoplados: `Governance Core`, `Orchestration Runtime`, `Experience Layer`.
- Tablas en 3FN, prefijo de tres letras por módulo (`AIO`), PK autonumérica (`BIGSERIAL`), `iduuid` único.
- Reutilizar `IMLIMMUTABLELOGS` para trazabilidad y enlazar con procesos BPMN existentes.
- Exponer capacidades vía APIs internas (REST/gRPC) sin acoplar UI.

---

## 🧱 Modelo de Dominio Propuesto

### 1. Contexto `Governance Core`

| Entidad | Tabla | Descripción |
|---------|-------|-------------|
| `AioComponent` | `AIOCOMPONENTS` | Registro maestro de cualquier activo AI (modelo, prompt, agente, dataset, pipeline). |
| `AioCapability` | `AIOCAPABILITIES` | Funcionalidades declarativas expuestas por un componente (inferencias, embeddings, retrievers, orquestaciones). |
| `AioPolicyBinding` | `AIOPOLICYBINDINGS` | Relaciona componentes con políticas/reglas (compliance, uso, residencia de datos). |
| `AioWorkspace` | `AIOWORKSPACES` | Aislamiento lógico multi-tenant (organización, producto, cliente). |

#### Especificaciones clave

```text
Tabla: AIOCOMPONENTS
- PK: IDXAIOCOMPONENT BIGSERIAL
- UUID: aiocomponentuuid (VARCHAR 36, UNIQUE)
- aiocomponenttype (ENUM-like: MODEL | AGENT | PROMPT | DATASET | PIPELINE | SERVICE)
- aiocomponentnamespace (VARCHAR 120) → vínculo con PRJPROJECTS
- aiocomponentstate (VARCHAR 20) → DRAFT | VALIDATED | READY | ACTIVE | SUNSET
- idxproject (FK → PRJPROJECTS)
- idxworkspace (FK → AIOWORKSPACES)
- metadatasettings JSONB (config específica)
```

3FN se garantiza separando capacidades, políticas y workspaces en tablas relacionadas.

### 2. Contexto `Orchestration Runtime`

| Entidad | Tabla | Descripción |
|---------|-------|-------------|
| `AioDeployment` | `AIODEPLOYMENTS` | Instancias desplegadas (ambiente, versión, escala, endpoint). |
| `AioServiceBinding` | `AIOSERVICEBINDINGS` | Enlaces entre despliegues y servicios subyacentes (K8s, Lambda, VM, edge). |
| `AioEvent` | `AIOEVENTS` | Telemetría de ejecución y eventos operativos (start, scale, fail, retire). |
| `AioSchedule` | `AIOSCHEDULES` | Programaciones y triggers (cron, SLA, condicionales BPMN). |

Sujeto a integración directa con workflows Flowable:

- Cada `AioDeployment` tiene relación `1:N` con `AioEvent`.
- `AioSchedule` alimenta timers BPMN; se expone como `TimerStart` dinámico.
- `AIOSERVICEBINDINGS` abstrae adaptadores (Kubernetes, Docker, VM, serverless).

### 3. Contexto `Experience Layer`

- `AioAccessToken` (`AIOACCESSTOKENS`): controla API Keys y permisos sobre componentes/entornos.
- `AioConsumption` (`AIOCONSUMPTIONS`): telemetría de uso (requests, tokens, latencias) por workspace y componente.
- `AioMarketplaceEntry` (`AIOMARKETPLACEENTRIES`): catálogo interno para discovery y reutilización entre tenants (solo metadatos).

---

## 🔗 Integración con Entidades Existentes

- `ImmutableLog`: crear entradas `entityType = "AIO_COMPONENT" | "AIO_DEPLOYMENT"` en cada cambio de estado.
- `ComplianceAssessment` / `FriaAssessment`: nuevos campos `idxaiocomponent` para enlazar evaluaciones con componentes AI OS.
- `EuRegistration`: usar `aiocomponentuuid` para generar payloads de registro cuando corresponda (alto riesgo).
- `AnnexIIICategory`: mapear categoría → `aiocomponentriskprofile`.

---

## ⚙️ Servicios y Puertos Hexagonales

| Servicio | Responsabilidad | Adaptadores |
|----------|-----------------|-------------|
| `AioInventoryService` | CRUD de componentes, capacidades y workspaces. | REST (internal), BPMN delegate, CLI. |
| `AioDeploymentService` | Gestionar despliegues, escalamientos, rollback. | Kubernetes Operator, Terraform provider, Webhook. |
| `AioPolicyService` | Aplicar políticas dinámicas (acceso, compliance, coste). | Drools, Policy-as-Code (OPA), BPMN gateways. |
| `AioTelemetryService` | Capturar métricas y eventos para monitoreo. | Prometheus, OpenTelemetry, ImmutableLog. |
| `AioMarketplaceService` | Publicar/certificar assets reutilizables. | Portal UI, API GraphQL opcional. |

Todos los servicios expondrán puertos en la capa dominio y adaptadores hacia infraestructura concreta cumpliendo SOLID.

---

## 🧩 Extensiones BPMN

1. **Nuevos procesos**
   - `ai-component-onboarding-v1.bpmn`: altas automatizadas con validación de políticas.
   - `ai-runtime-health-v1.bpmn`: monitoreo continuo, reintentos y alertas.
   - `ai-marketplace-publish-v1.bpmn`: certificación y publicación controlada.

2. **Actualizaciones**
   - `agent-approval`: agregar `aiocomponentuuid` y tareas de despliegue automático vía `AioDeploymentService`.
   - `model-approval`: enlazar con `AIODEPLOYMENTS` para promover versiones.
   - `prompt-approval`: registrar `AioCapability` y generar versión controlada en el marketplace.

3. **Delegates adicionales**
   - `AioPolicyEnforcementDelegate`
   - `AioDeploymentOrchestratorDelegate`
   - `AioTelemetryCollectorDelegate`

---

## 📊 Observabilidad y Cumplimiento

- **Trazabilidad**: cada operación registra hash chain en `IMLIMMUTABLELOGS`.
- **KPIs clave**: `uptime`, `latency`, `complianceScore`, `fairnessScore`, `usageByWorkspace`.
- **Alertas**: integración con procesos de detección para escalado y suspensión preventiva.
- **Retención**: enlazar con `ComplianceAssessment` para auditorías periódicas y generación de reportes EU AI Act.

---

## 🔐 Seguridad y Acceso

- Control centralizado a través de `AIOACCESSTOKENS` con scopes (`READ_COMPONENT`, `DEPLOY`, `EXECUTE`, `MARKETPLACE`).
- Integración con `CORUSERS` y RBAC existente (roles: `AIOS_ADMIN`, `AIOS_OPERATOR`, `AIOS_CONSUMER`).
- Segregación por workspace obligatoria; ningún componente puede desplegarse sin `idxworkspace`.
- Cifrado de secretos vía Vault/KMS; bindings almacenan `secretRef` en lugar de secretos en claro.

---

## 🔄 Ciclo de Vida

| Fase | Responsable | Artefactos |
|------|-------------|------------|
| Registro | Equipo producto | `AioComponent`, `AioCapability` |
| Gobernanza | Oficina IA | `ComplianceAssessment`, `FriaAssessment`, políticas vinculadas |
| Deploy | Plataforma | `AioDeployment`, `AioServiceBinding`, BPMN runtime |
| Operación | SRE/Plataforma | `AioEvent`, `AioTelemetryService`, monitoreo |
| Retiro | Oficina IA + Plataforma | Cambio estado → `SUNSET`, limpieza bindings |

---

## 🛠️ Roadmap de Implementación

1. **Fase 1 – Inventario y Policies (4 semanas)**
   - Implementar `AIOCOMPONENTS`, `AIOCAPABILITIES`, `AIOWORKSPACES`.
   - API REST (`/api/v1/aios/components`) + integración ImmutableLog.

2. **Fase 2 – Runtime & Deploy (6 semanas)**
   - `AIODEPLOYMENTS`, `AIOSERVICEBINDINGS`, `AIOEVENTS`.
   - Adaptador Kubernetes (helm operator) y hooks BPMN.

3. **Fase 3 – Experience Layer (4 semanas)**
   - Marketplace interno, telemetría (`AIOCONSUMPTIONS`), API Tokens.
   - Dashboards y prompts de operación.

4. **Fase 4 – Sectoriales & Automatización (continuo)**
   - Vincular frameworks FaaS sectoriales, publicar plantillas en marketplace.
   - Actualizar prompts de compliance para validar políticas AI OS.

---

## 📂 Impacto en Repositorios

- `nocode.service.entitys`: nuevas entidades `Aio*` + repositorios, servicios y controladores.
- `codeflowx.govern.workflow.lib`: procesos BPMN, delegates y reglas Drools extendidas.
- `codeflowx-faas-platform`: prompts sectoriales actualizados para consumir API AI OS y registrar metadatos.
- `docs`: mantener este documento + actualizar `ARQUITECTURA_CODEFLOWX_GOVERN.md` y `QUICK_REFERENCE_CODEFLOWX.md` con el resumen.

---

## ✅ Checklist de Cumplimiento

- Art. 12 / 19: logs inmutables conectados a cada cambio de estado.
- Art. 27: FRIAs vinculadas antes de pasar a `READY`.
- Art. 43: Evaluaciones de conformidad consumen información runtime real.
- Art. 49: Generación automática de payload cuando el componente es alto riesgo.
- DORA/GDPR: refuerzo de segregación por workspace, cifrado de secretos y trazabilidad.

---

## 📞 Contacto

**Arquitecto responsable:** Manuel González
**Equipo:** CodeflowX Plataforma y Cumplimiento
**Estado:** Propuesta inicial pendiente de validación

---

> Cualquier cambio estructural debe alinearse con las convenciones ENART (prefijos, nomenclatura y 3FN) y registrarse en ImmutableLog.
