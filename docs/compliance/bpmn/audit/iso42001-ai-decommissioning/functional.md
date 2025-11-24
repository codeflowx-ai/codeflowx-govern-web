# ISO 42001 AI Decommissioning – Guía Funcional

## Objetivo
Orquestar la retirada controlada de sistemas de IA (cláusula 8.1 y anexos), garantizando cierre seguro, preservación de evidencias, borrado conforme y actualización de registros.

## Roles
- **AI Ops Lead**: inicia proceso y coordina tareas técnicas.
- **Data Steward**: gestiona retención/eliminación de datos.
- **Compliance**: valida logs y documentación.
- **Security**: asegura revocación de credenciales/accesos.

## Flujo propuesto
1. **Start Event** – trigger manual con `systemId`, motivo, fecha planificada.
2. **User Task – Plan Decommissioning**: define alcance (infra, datos, dependencias).
3. **Service Task – Freeze Deployments**: evita nuevos despliegues/versiones.
4. **User Task – Notify Stakeholders**: comunica a equipos y clientes afectados.
5. **Parallel Gateway – Execution**:
   - `Archive Artifacts` (modelos, configuraciones, evidencias).
   - `Data Retention / Deletion` (aplica políticas).
   - `Disable Integrations & Credentials`.
6. **User Task – Compliance Review**: valida que se cumplieron requisitos (logs, reportes).
7. **Service Task – Update Registers**: marca sistema como `DECOMMISSIONED` en inventarios (AI Catalog, EU DB si aplica).
8. **End Event** – se genera informe final.

## Variables
- `decommissioningId`, `systemId`, `reason`.
- `planDocumentUrl`, `stakeholderNotifications`.
- `archiveLocation`, `dataDeletionEvidence`.
- `complianceSignOff`, `decommissionedAt`.

## SLA
- Planificación: ≤ 10 días antes de ejecución.
- Ejecución técnica: según criticidad (preferente off‑peak).
- Revisión compliance: ≤ 3 días tras ejecución.

## Evidencias
- Plan e informe en repositorio (`decommissioningReports`).
- Logs de retención/borrado.
- Actualización en catálogos/registros regulatorios.

