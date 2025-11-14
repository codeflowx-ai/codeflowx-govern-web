# Manual del Marketplace de Agentes · CodeflowX OS

## 1. Objetivo
Definir el proceso para publicar, versionar y retirar agentes en el marketplace interno de CodeflowX OS.

## 2. Roles y Permisos
- **Agent Publisher**: crea y actualiza agentes (requiere rol `AIOS_PUBLISHER`).
- **Reviewer**: valida artefactos técnicos y compliance (`AIOS_REVIEWER`).
- **Marketplace Admin**: aprueba publicación final y gestiona licencias (`AIOS_ADMIN`).
- **Consumer**: puede descubrir e instalar agentes autorizados (`AIOS_CONSUMER`).

## 3. Flujo de Publicación
1. **Preparación**  
   - Validar checklist QA interna.  
   - Completar metadatos: nombre, versión semver, sector, descripción, requisitos, políticas.  
   - Empaquetar artefactos (BPMN, reglas, servicios, UI, datasets).  
2. **Registro en Catalogo**  
   - Invocar `POST /api/v1/aios/components` con flag `marketplaceCandidate=true`.  
   - Adjuntar manifest YAML (`aios_component_manifest.yaml`).  
   - Subir documentación PDF/Markdown (manual, SLA, security).  
3. **Revisión Técnica**  
   - Flujo BPMN `ai-marketplace-review` asigna Reviewer.  
   - Revisar pruebas, telemetría, cumplimiento de prompts.  
   - Documentar resultado en `ImmutableLog` (status, observaciones).  
4. **Compliance & Pricing**  
   - Admin revisa políticas aplicables (Drools).  
   - Definir licencia (free, internal, by usage) y condiciones de distribución.  
5. **Publicación**  
   - Procesos `ai-marketplace-publish-v1.bpmn`: activa versión, notifica workspaces autorizados.  
   - Registro en marketplace UI (`codeflowx-portal`).  

## 4. Versionado y Releases
- Versionado semántico (MAJOR.MINOR.PATCH).  
- Se admite `pre-release` (ej. `1.1.0-rc1`) para pruebas limitadas.  
- Cambios MAJOR requieren aprobación adicional de Compliance y notificación a consumidores.  
- Mantener changelog (`docs/CHANGELOG.md`) y actualización en documento marketplace.  
- API `PATCH /api/v1/aios/components/{uuid}` para actualizar metadatos; `POST /versions` para nuevas versiones.

## 5. Instalación y Distribución
- Consumidores solicitan acceso vía portal (workflow `marketplace-access-request`).  
- Admin asigna licencias y determina workspaces habilitados.  
- Instalación dispara pipelines:
  - Sincroniza artefactos al workspace destino.  
  - Configura políticas y telemetría.  
  - Ejecución de pruebas smoke post instalación.

## 6. Retiro y Deprecación
- Motivos: vulnerabilidad, incumplimiento, obsolescencia, reemplazo.  
- Pasos:  
  1. Marcar versión como `Deprecated`.  
  2. Notificar consumidores (webhook + correo).  
  3. Definir fecha fin de soporte.  
  4. Ofrecer alternativa (nueva versión, agente distinto).  
  5. Eliminar de portal al expirar soporte.  
- Mantener registros en `ImmutableLog`.

## 7. Métricas y Reporting
- `marketplace_agents_total`, `marketplace_installs_total`, `marketplace_active_workspaces`.  
- Seguimiento de rating y feedback (si habilitado).  
- Reporte mensual con agentes más instalados, compliance status, tickets asociados.

## 8. Buenas Prácticas
- Incluir demos y datasets sintéticos.  
- Documentar integración paso a paso (`INTEGRACION_CLIENTES_CODEFLOWX_OS`).  
- Proveer scripts de rollback desatendidos.  
- Revisar dependencias de terceros (licencias) y declarar en manifest.  
- Mantener autor y contacto para soporte específico.

## 9. Referencias
- `QA_INTERNA_CODEFLOWX_OS.md`.  
- `PROMPTS_003_AI_OS_GOV.md` (proceso de publicación).  
- `ORQUESTACION_AGENTES.md`, `agent_execution_log.md` para seguimiento.  
- Portal CodeflowX (sección Marketplace) para UI.



