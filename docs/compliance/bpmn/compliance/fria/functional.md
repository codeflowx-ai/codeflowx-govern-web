# FRIA Process (Art. 27) – Guía Funcional

## Objetivo
Realizar la Evaluación de Impacto en Derechos Fundamentales (FRIA) antes de desplegar sistemas de alto riesgo en entornos cubiertos por el Art. 27 del EU AI Act.

## Roles
- **Deployer / Project Owner**: inicia FRIA y completa wizard.
- **Ethics Committee & Legal**: revisan casos de alto impacto.
- **Market Surveillance Liaison**: notifica autoridad competente.

## Flujo
1. **Start** → User Task "FRIA Wizard Step 1‑6" (recoge datos Art. 27.1 a-f).
2. **Generate FRIA Document** (service): produce documento y calcula `complianceScore`, `art27Compliant`.
3. **Cross-Validate FRIA vs Technical Metrics** 🆕 (INC-007): valida consistencia entre FRIA documental y métricas técnicas reales.
   - Si `consistencyScore < 0.70` → requiere justificación del usuario.
   - Genera alertas si hay inconsistencias significativas.
4. **Gateway FRIA Complete?**
   - NO (<0.90) → User Task "Complete Missing Elements" (loop).
   - YES → `Analyze Fundamental Rights Impact`.
5. **Analyze Fundamental Rights Impact**: determina `charterArticlesAffected`, `impactSeverity`.
6. **Gateway High Impact?**
   - YES → User Task "Enhanced Review" (Ethics/Legal) → sub-gateway (Approve / Reject / Modify).
   - NO → continúa.
7. **User Task "Deployer Approval"** → decide `Proceed?`.
8. **Gateway Proceed?**
   - YES → Parallel:
     - `Notify Market Surveillance Authority` (Art. 27.3).
     - `Register FRIA in Database`.
     - `Update Project Status`.
   - NO → `Cancel Deployment`.
9. **End** → "FRIA Completed & Notified" o "Deployment Cancelled".

## Variables
- `friaDataComplete`, `friaDocumentUrl`.
- `complianceScore`, `art27Compliant`.
- `consistencyScore` 🆕, `isConsistent` 🆕, `inconsistencies` 🆕 (validación cruzada).
- `charterArticlesAffected`, `impactSeverity`.
- `notificationId`, `notificationDate`.
- `friaApproved`, `project.readyForDeployment`.

## SLA
- FRIA Wizard: ≤ 5 días laborables.
- Enhanced Review (alto impacto): ≤ 10 días.
- Notificación Art. 27.3: inmediata tras aprobación.

## Evidencias
- Documento FRIA PDF.
- Registro en tabla `FRIAFUNDAMENTALRIGHTSASSESSMENTS`.
- Mensajes a `ImmutableLog`.

