# RAG Evaluation v1 – Guía Técnica

## Artefactos
- **BPMN**: `processes/aios/rag-evaluation-v1.bpmn`
- **Delegates**:
  - `RagEvaluationDelegate`
  - `StoreRagEvaluationDelegate`
  - `CreateRagAlertDelegate`
- **Reglas**: `rules/aios/evaluation/rag-evaluation.drl`
- **Fact**: `RagEvaluationFact`

## Variables
- `ragSystemId`, `indexId`, `retrieverConfig`, `llmConfig`.
- `retrievalMetrics` (precision, recall, MRR), `generationMetrics` (exactMatch, BLEU, ROUGE, hallucinationRate).
- `evaluationResult`, `justification`, `reviewArtifacts`.
- `alertId` (cuando se detectan hallazgos críticos).

## Integraciones
- **RAG Evaluation Service** (propio o `leka-rag-evaluation`).
- **Vector DB / Knowledge Store** (para ejecutar queries de prueba).
- **Artifact Store** (guardar ejemplos, logs).
- **Notification / Issue Tracking** (cuando se genera `CreateRagAlert`).

## Flujo técnico
1. `RagEvaluationDelegate` ejecuta los tests (puede lanzar un job asíncrono).  
2. Resultados se guardan en `retrievalMetrics`/`generationMetrics`.  
3. Reglas deciden si el sistema es aceptable o requiere revisión.  
4. `StoreRagEvaluationDelegate` persiste información en `EvaluationRepository`.  
5. `CreateRagAlertDelegate` se dispara cuando `hallucinationRate` o `faithfulnessScore` están por debajo del umbral.

## Pendientes
- Crear `RagEvaluationFact` y archivo Drools con umbrales configurables por dominio.
- Añadir suporte a evaluación continua (por lotes, en producción).
- Pruebas integradas con dataset sintético para asegurar reproducibilidad.


