# AI Marketplace Publish – Guía Funcional

## Objetivo
Controlar la publicación de componentes en el marketplace AI‑OS, asegurando que cuentan con certificaciones, evidencias y aprobaciones legales antes de exponerse a clientes internos/externos.

## Roles
- **AIOS Admin**: inicia solicitud, carga evidencias.
- **Legal / Compliance**: revisa casos `RESTRICTED`.
- **Product Owner**: recibe notificaciones de publicación o rechazo.

## Flujo funcional
1. **Solicitud de publicación** (`componentId`, `marketplaceTier`).
2. **Verificación de artefactos**: se comprueba que existen declaración UE, technical doc, reportes FRIA/Conformity. Si faltan, se asigna tarea “Upload Missing Evidence”.
3. **Cálculo de marketplace score** (pesa compliance, calidad y incidentes abiertos).
4. **Aplicación de reglas** para decidir el tier:
   - `PUBLIC`: publicación inmediata.
   - `RESTRICTED`: requiere revisión legal antes de estar visible.
   - `DENIED`: se notifica al propietario para subsanar.
5. **Acciones finales**:
   - `PUBLIC`: el componente queda disponible y se registra `marketplacePublishedAt`.
   - `RESTRICTED`: tarea humana define condiciones y puede continuar manualmente.
   - `DENIED`: notificación automática con justificación.

## Entradas
- `certificationSet`, `hasCriticalFindings`, `legalApproval`, `openIncidents`.

## Salidas
- `publicationDecision`, `marketplaceJustification`.
- `marketplaceStatus` (`PUBLIC`, `RESTRICTED`, `DENIED`).
- `marketplacePublishedAt` o `ownerEmail` notificado.

## SLA
- Carga de evidencias: 24h.
- Revisión legal: 48h.
- Publicación/denegación: máximo 72h desde la solicitud.

