# PROMPT: INC-005-003 - Validación Proactiva de Políticas (Microservicio Python)

**Incidencia:** INC-005-003  
**Prioridad:** 🔴 CRÍTICA (P0)  
**Artículo EU AI Act:** Art. 15 (Transparencia)  
**Esfuerzo Estimado:** 2-3 días  
**Tipo:** Python - Microservicio ML

---

## CONTEXTO

Este microservicio proporciona scoring de alineación con políticas del cliente usando modelos de NLP.

**Microservicio:** `leka-rag-policy-validation` (Port 80XX)

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Endpoint de Scoring

**Archivo:** `app/api/routes/policy_validation.py`

```python
from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from typing import List, Dict, Any
from app.services.policy_scoring_service import PolicyScoringService

router = APIRouter()

class PolicyScoringRequest(BaseModel):
    text: str
    policy_type: str  # TONE, CONTENT, ETHICS, VALUES, LANGUAGE
    policy_rules: Dict[str, Any]
    client_id: int

class PolicyScoringResponse(BaseModel):
    score: float  # 0-100
    confidence: float
    violations: List[str]
    details: Dict[str, Any]

@router.post("/api/policy-validation/score", response_model=PolicyScoringResponse)
async def score_policy_alignment(request: PolicyScoringRequest):
    """Calcula score de alineación con política"""
    service = PolicyScoringService()
    result = await service.score(request.text, request.policy_type, request.policy_rules)
    return result
```

### 2. Servicio de Scoring

**Archivo:** `app/services/policy_scoring_service.py`

```python
from typing import Dict, Any, List
from sentence_transformers import SentenceTransformer
import torch

class PolicyScoringService:
    def __init__(self):
        self.model = SentenceTransformer('all-MiniLM-L6-v2')
    
    async def score(self, text: str, policy_type: str, rules: Dict[str, Any]) -> Dict[str, Any]:
        """Calcula score de alineación"""
        if policy_type == "TONE":
            return await self._score_tone(text, rules)
        elif policy_type == "CONTENT":
            return await self._score_content(text, rules)
        elif policy_type == "ETHICS":
            return await self._score_ethics(text, rules)
        elif policy_type == "VALUES":
            return await self._score_values(text, rules)
        elif policy_type == "LANGUAGE":
            return await self._score_language(text, rules)
        else:
            return {"score": 50.0, "confidence": 0.5, "violations": [], "details": {}}
    
    async def _score_tone(self, text: str, rules: Dict[str, Any]) -> Dict[str, Any]:
        """Score de tono (formal, informal, etc.)"""
        # Implementar análisis de tono usando NLP
        # Comparar con tono esperado en rules
        return {"score": 75.0, "confidence": 0.8, "violations": [], "details": {}}
    
    async def _score_content(self, text: str, rules: Dict[str, Any]) -> Dict[str, Any]:
        """Score de contenido (temas permitidos/prohibidos)"""
        # Verificar temas prohibidos
        prohibited_topics = rules.get("prohibited_topics", [])
        violations = []
        for topic in prohibited_topics:
            if topic.lower() in text.lower():
                violations.append(f"Contiene tema prohibido: {topic}")
        
        score = 100.0 - (len(violations) * 20.0)
        return {
            "score": max(0.0, score),
            "confidence": 0.85,
            "violations": violations,
            "details": {"prohibited_topics_found": len(violations)}
        }
    
    async def _score_ethics(self, text: str, rules: Dict[str, Any]) -> Dict[str, Any]:
        """Score de ética"""
        # Implementar validación ética
        return {"score": 80.0, "confidence": 0.75, "violations": [], "details": {}}
    
    async def _score_values(self, text: str, rules: Dict[str, Any]) -> Dict[str, Any]:
        """Score de valores corporativos"""
        # Comparar con valores en rules
        return {"score": 70.0, "confidence": 0.7, "violations": [], "details": {}}
    
    async def _score_language(self, text: str, rules: Dict[str, Any]) -> Dict[str, Any]:
        """Score de idioma"""
        allowed_languages = rules.get("allowed_languages", [])
        # Detectar idioma y verificar
        return {"score": 90.0, "confidence": 0.9, "violations": [], "details": {}}
```

---

## REFERENCIAS

- **Art. 15 EU AI Act:** Transparencia
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_005_EVALUACION_RAG.md#inc-005-003`

