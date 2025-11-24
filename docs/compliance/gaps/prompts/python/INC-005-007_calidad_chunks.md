# PROMPT: INC-005-007 - Evaluación de Calidad de Chunks (Microservicio Python)

**Incidencia:** INC-005-007  
**Prioridad:** 🟢 MEDIA (P2)  
**Artículo EU AI Act:** Art. 13  
**Esfuerzo Estimado:** 1-2 días  
**Tipo:** Python - Microservicio ML

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Métricas de Calidad

```python
class ChunkQualityService:
    async def evaluate_chunk_quality(
        self,
        chunk: str,
        original_text: str
    ) -> Dict[str, float]:
        """Evalúa calidad de chunk"""
        return {
            "coherence_score": self._calculate_coherence(chunk),
            "completeness_score": self._calculate_completeness(chunk, original_text),
            "semantic_break_score": self._detect_semantic_breaks(chunk)
        }
    
    def _calculate_coherence(self, chunk: str) -> float:
        """Calcula coherencia semántica"""
        # Usar modelo de coherencia
        return 0.8
    
    def _calculate_completeness(self, chunk: str, original: str) -> float:
        """Verifica completitud de unidades semánticas"""
        # Verificar si chunk rompe frases/párrafos
        return 0.9
    
    def _detect_semantic_breaks(self, chunk: str) -> float:
        """Detecta rupturas semánticas"""
        # Detectar si chunk corta en medio de frase/párrafo
        return 0.1  # Bajo = menos rupturas
```

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_005_EVALUACION_RAG.md#inc-005-007`

