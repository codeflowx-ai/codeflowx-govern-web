# PROMPT: INC-005-005 - Evaluación de Sesgo en Embeddings (Microservicio Python)

**Incidencia:** INC-005-005  
**Prioridad:** 🟡 ALTA (P1)  
**Artículo EU AI Act:** Art. 10  
**Esfuerzo Estimado:** 2-3 días  
**Tipo:** Python - Microservicio ML

---

## IMPLEMENTACIÓN REQUERIDA

### 1. WEAT Test

```python
class BiasEvaluationService:
    def weat_test(
        self,
        target_words: List[str],
        attribute_words_1: List[str],
        attribute_words_2: List[str],
        embeddings_model
    ) -> Dict[str, float]:
        """Word Embedding Association Test"""
        # Calcular asociaciones
        # Retornar effect size y p-value
        return {"effect_size": 0.5, "p_value": 0.01, "is_biased": True}
    
    def evaluate_gender_bias(self, embeddings_model) -> Dict[str, Any]:
        """Evalúa sesgo de género"""
        # WEAT con palabras de género
        return self.weat_test(
            target_words=["doctor", "nurse"],
            attribute_words_1=["he", "man", "male"],
            attribute_words_2=["she", "woman", "female"],
            embeddings_model=embeddings_model
        )
```

### 2. Debiasing

```python
def debias_embeddings(embeddings, bias_direction):
    """Aplica debiasing a embeddings"""
    # Proyección ortogonal para remover sesgo
    # Retornar embeddings debiased
    return debiased_embeddings
```

---

## REFERENCIAS

- **WEAT Paper:** https://arxiv.org/abs/1608.07187
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_005_EVALUACION_RAG.md#inc-005-005`

