# PROMPT: INC-005-006 - Prevención Proactiva de Errores de Grounding (Microservicio Python)

**Incidencia:** INC-005-006  
**Prioridad:** 🟡 ALTA (P1)  
**Artículo EU AI Act:** Art. 13  
**Esfuerzo Estimado:** 2 días  
**Tipo:** Python - Microservicio ML

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Validación Pre-Generación

```python
class GroundingValidationService:
    async def validate_chunks_before_generation(
        self,
        chunks: List[str],
        query: str,
        min_confidence: float = 0.7
    ) -> Dict[str, Any]:
        """Valida calidad de chunks antes de generación"""
        scores = []
        for chunk in chunks:
            score = await self._calculate_chunk_quality(chunk, query)
            scores.append(score)
        
        avg_score = sum(scores) / len(scores) if scores else 0.0
        
        if avg_score < min_confidence:
            return {
                "allowed": False,
                "reason": f"Chunks de baja calidad. Score promedio: {avg_score}",
                "scores": scores
            }
        
        return {"allowed": True, "scores": scores}
    
    async def _calculate_chunk_quality(self, chunk: str, query: str) -> float:
        """Calcula calidad de chunk"""
        # Similitud semántica con query
        # Completitud del chunk
        # Relevancia
        return 0.85  # Placeholder
```

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_005_EVALUACION_RAG.md#inc-005-006`

