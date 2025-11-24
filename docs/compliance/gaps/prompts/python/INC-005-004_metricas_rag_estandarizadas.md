# PROMPT: INC-005-004 - Métricas RAG Estandarizadas (Microservicio Python)

**Incidencia:** INC-005-004  
**Prioridad:** 🟡 ALTA (P1)  
**Artículo EU AI Act:** Art. 10, 13  
**Esfuerzo Estimado:** 2-3 días  
**Tipo:** Python - Microservicio ML

---

## CONTEXTO

Falta suite completa de métricas RAG estandarizadas (RAGAS, ARES) y benchmarking periódico.

**Microservicio:** `leka-rag-metrics` (Port 80XX)

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Integrar RAGAS Framework

```python
from ragas import evaluate
from ragas.metrics import (
    faithfulness,
    answer_relevancy,
    context_precision,
    context_recall
)
from datasets import Dataset

class RagasMetricsService:
    async def evaluate_rag(
        self,
        questions: List[str],
        answers: List[str],
        contexts: List[List[str]],
        ground_truths: List[str]
    ) -> Dict[str, float]:
        """Evalúa RAG usando RAGAS"""
        dataset = Dataset.from_dict({
            "question": questions,
            "answer": answers,
            "contexts": contexts,
            "ground_truth": ground_truths
        })
        
        result = evaluate(
            dataset,
            metrics=[
                faithfulness,
                answer_relevancy,
                context_precision,
                context_recall
            ]
        )
        
        return {
            "faithfulness": result["faithfulness"],
            "answer_relevancy": result["answer_relevancy"],
            "context_precision": result["context_precision"],
            "context_recall": result["context_recall"]
        }
```

### 2. Benchmarking con BEIR/MTEB

```python
from beir import util, LoggingHandler
from beir.datasets.data_loader import GenericDataLoader
from beir.retrieval.evaluation import EvaluateRetrieval

class BenchmarkingService:
    async def benchmark_retrieval(self, model_name: str) -> Dict[str, float]:
        """Benchmark con BEIR"""
        # Cargar dataset BEIR
        dataset = "nfcorpus"  # O otro dataset BEIR
        url = f"https://public.ukp.informatik.tu-darmstadt.de/thakur/BEIR/datasets/{dataset}.zip"
        
        # Evaluar modelo
        # Retornar métricas: NDCG@10, Recall@10, etc.
        return {"ndcg@10": 0.45, "recall@10": 0.60}
```

---

## REFERENCIAS

- **RAGAS:** https://github.com/explodinggradients/ragas
- **BEIR:** https://github.com/beir-cellar/beir
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_005_EVALUACION_RAG.md#inc-005-004`

