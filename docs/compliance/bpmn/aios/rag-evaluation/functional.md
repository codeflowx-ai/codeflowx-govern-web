# RAG Evaluation v1 – Guía Funcional

## Objetivo
Evaluar sistemas de Retrieval‑Augmented Generation (RAG) antes de su despliegue, asegurando calidad de recuperación, precisión de respuestas y cumplimiento de políticas.

## Roles
- **RAG Engineer**: diseña el pipeline y lanza la evaluación.
- **Knowledge Management**: valida la calidad de las fuentes y el index.
- **Compliance**: revisa uso de datos sensibles y control de alucinaciones.

## Flujo
1. **Start** – se especifica `ragSystemId`, `documentIndex`, `retrievers`.
2. **Execute RAG Evaluation**:
   - Métricas de recuperación (MRR, Recall@k).
   - Métricas de generación (exact match, faithfulness).
3. **Store Results** – guarda métricas y ejemplos.
4. **Business Rule Task** – determina `APPROVE` / `HITL` / `REJECT`.
5. **Acción**:
   - `APPROVE`: se habilita el sistema para uso.
   - `HITL`: se requiere revisión humana o ajustes.
   - `REJECT`: se devuelven cambios solicitados.
6. **Registro** en `ImmutableLog` y notificación.

## Variables
- `ragSystemId`, `knowledgeBaseId`, `retrievalStrategy`.
- `retrievalMetrics`, `answerQuality`, `hallucinationRate`.
- `decision`, `reviewer`, `notes`.

## SLA
- Evaluación automática: ~1‑2 h (depende del corpora).
- Revisión HUM: 48 h.

## Consideraciones
- Los resultados deben incluir ejemplos de respuestas correctas/incorrectas.
- Importante validar políticas de uso de datos (PII) en la capa de retrieval.


