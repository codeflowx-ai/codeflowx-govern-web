# AI Marketplace Publish – Guía Técnica

## Artefactos
- **BPMN**: `processes/aios/ai-marketplace-publish-v1.bpmn`
- **Reglas**: `rules/aios/marketplace/ai-marketplace-publish.drl`
- **Fact**: `MarketplacePublicationFact`

## Delegates
| Delegate | Ubicación | Función |
| --- | --- | --- |
| `verifyCertificationArtifactsDelegate` | `aios.marketplace.VerifyCertificationArtifactsDelegate` | Comprueba existencia de artefactos requeridos. |
| `marketplaceScoringDelegate` | Calcula `marketplaceScore` combinando compliance/quality/incidentes. |
| `marketplacePublicationRulesDelegate` | Ejecuta Drools para decidir `PUBLIC/RESTRICTED/DENIED`. |
| `publishComponentMarketplaceDelegate` | Marca publicación y fecha. |
| `notifyMarketplaceDenialDelegate` | `// @todo` cliente de notificaciones para rechazos. |

## Variables
- Entrada: `componentId`, `marketplaceTier`, `certificationSet`, `legalApproval`, `openIncidents`.
- Salida: `marketplaceScore`, `publicationDecision`, `marketplaceStatus`, `marketplaceJustification`.

## Reglas Drools
1. **Public**: sin findings críticos, score ≥85, sin incidentes abiertos, aprobación legal.
2. **Restricted**: score ≥65 y legal approval, pero incidentes o puntos pendientes.
3. **Denied**: demás escenarios.

## Integraciones
- Notificaciones al owner (`notifyMarketplaceDenialDelegate`) – pendiente.
- API Marketplace para sincronizar status (`publishComponentMarketplaceDelegate` puede ampliarse con llamada REST).

## Operativa
- Sesión Drools: `ai-marketplace-publish-session`.
- Para nuevos tiers, ampliar `MarketplacePublicationFact` y la tabla de decisión.

