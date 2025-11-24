# PROMPT: INC-005-002 - Detección Insuficiente de Hallucinaciones (Microservicio Python)

**Incidencia:** INC-005-002  
**Prioridad:** 🔴 CRÍTICA (P0)  
**Artículo EU AI Act:** Art. 13 (Robustez, Seguridad y Precisión)  
**Esfuerzo Estimado:** 3-4 días  
**Tipo:** Python - Microservicio ML/AI

---

## CONTEXTO

El sistema actual detecta alucinaciones de forma básica mediante verificación de fuentes y coherencia, pero no utiliza modelos especializados ni tiene validación humana sistemática. Esto puede resultar en respuestas incorrectas que afecten decisiones críticas.

**Microservicios Involucrados:**
- `leka-rag-evaluation` (Port 80XX) - Evaluación RAG
- Este nuevo microservicio: `leka-rag-hallucination-detection` (Port 80XX)

---

## REQUISITOS

1. Integrar modelos especializados de detección de alucinaciones:
   - SelfCheckGPT para auto-verificación
   - FactScore para validación de hechos
   - Entailment models para verificación de coherencia
2. Implementar validación humana sistemática:
   - Muestreo aleatorio de respuestas para revisión
   - Priorización de respuestas de alto riesgo
   - Dashboard para validadores humanos
3. Establecer proceso de retroalimentación:
   - Corrección de falsos positivos/negativos
   - Mejora continua del modelo de detección
   - Métricas de precisión de detección

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Estructura del Microservicio

```
leka-rag-hallucination-detection/
├── app/
│   ├── __init__.py
│   ├── main.py
│   ├── config.py
│   ├── api/
│   │   ├── __init__.py
│   │   └── routes/
│   │       └── hallucination_detection.py
│   ├── services/
│   │   ├── __init__.py
│   │   ├── selfcheck_service.py
│   │   ├── factscore_service.py
│   │   ├── entailment_service.py
│   │   └── hallucination_aggregator.py
│   ├── models/
│   │   ├── __init__.py
│   │   └── hallucination_result.py
│   └── utils/
│       ├── __init__.py
│       └── text_utils.py
├── requirements.txt
└── Dockerfile
```

### 2. Endpoint Principal - Detección de Alucinaciones

**Archivo:** `app/api/routes/hallucination_detection.py`

```python
"""
Endpoints para detección de alucinaciones en respuestas RAG
Requisito: EU AI Act Art. 13
"""
from fastapi import APIRouter, HTTPException, Depends
from pydantic import BaseModel, Field
from typing import List, Optional, Dict, Any
from datetime import datetime
import logging

from app.services.selfcheck_service import SelfCheckService
from app.services.factscore_service import FactScoreService
from app.services.entailment_service import EntailmentService
from app.services.hallucination_aggregator import HallucinationAggregator

router = APIRouter()
logger = logging.getLogger(__name__)


class HallucinationDetectionRequest(BaseModel):
    """Request para detectar alucinaciones"""
    response_text: str = Field(..., description="Texto de la respuesta generada")
    context_chunks: List[str] = Field(..., description="Chunks de contexto usados para generar respuesta")
    query: Optional[str] = None
    rag_system_id: Optional[int] = None
    user_id: Optional[str] = None
    session_id: Optional[str] = None
    use_selfcheck: bool = Field(True, description="Usar SelfCheckGPT")
    use_factscore: bool = Field(True, description="Usar FactScore")
    use_entailment: bool = Field(True, description="Usar Entailment models")


class HallucinationScore(BaseModel):
    """Score de alucinación de un método específico"""
    method: str  # "selfcheck", "factscore", "entailment"
    score: float  # 0.0 (sin alucinación) - 1.0 (alucinación clara)
    confidence: float  # 0.0 - 1.0
    details: Dict[str, Any]


class HallucinationDetectionResponse(BaseModel):
    """Response de detección de alucinaciones"""
    has_hallucination: bool
    overall_score: float  # 0.0 (sin alucinación) - 1.0 (alucinación clara)
    confidence: float
    method_scores: List[HallucinationScore]
    detected_sentences: List[Dict[str, Any]]  # Frases detectadas como alucinaciones
    recommendations: List[str]
    requires_human_review: bool
    risk_level: str  # "LOW", "MEDIUM", "HIGH", "CRITICAL"


@router.post("/api/hallucination-detection/detect", response_model=HallucinationDetectionResponse)
async def detect_hallucinations(
    request: HallucinationDetectionRequest,
    selfcheck_service: SelfCheckService = Depends(),
    factscore_service: FactScoreService = Depends(),
    entailment_service: EntailmentService = Depends(),
    aggregator: HallucinationAggregator = Depends()
):
    """
    Detecta alucinaciones en respuesta RAG usando múltiples métodos
    
    Args:
        request: Datos de la respuesta a analizar
    
    Returns:
        HallucinationDetectionResponse con resultado de detección
    """
    try:
        method_scores = []
        
        # 1. SelfCheckGPT - Auto-verificación
        if request.use_selfcheck:
            try:
                selfcheck_result = await selfcheck_service.detect(
                    response=request.response_text,
                    context=request.context_chunks
                )
                method_scores.append(HallucinationScore(
                    method="selfcheck",
                    score=selfcheck_result["hallucination_score"],
                    confidence=selfcheck_result["confidence"],
                    details=selfcheck_result.get("details", {})
                ))
            except Exception as e:
                logger.error(f"Error en SelfCheckGPT: {e}")
        
        # 2. FactScore - Validación de hechos
        if request.use_factscore:
            try:
                factscore_result = await factscore_service.detect(
                    response=request.response_text,
                    context=request.context_chunks
                )
                method_scores.append(HallucinationScore(
                    method="factscore",
                    score=factscore_result["hallucination_score"],
                    confidence=factscore_result["confidence"],
                    details=factscore_result.get("details", {})
                ))
            except Exception as e:
                logger.error(f"Error en FactScore: {e}")
        
        # 3. Entailment - Verificación de coherencia
        if request.use_entailment:
            try:
                entailment_result = await entailment_service.detect(
                    response=request.response_text,
                    context=request.context_chunks
                )
                method_scores.append(HallucinationScore(
                    method="entailment",
                    score=entailment_result["hallucination_score"],
                    confidence=entailment_result["confidence"],
                    details=entailment_result.get("details", {})
                ))
            except Exception as e:
                logger.error(f"Error en Entailment: {e}")
        
        # 4. Agregar scores de múltiples métodos
        aggregated_result = aggregator.aggregate(method_scores)
        
        # 5. Determinar si requiere revisión humana
        requires_human_review = (
            aggregated_result["overall_score"] > 0.5 or
            aggregated_result["confidence"] < 0.7 or
            len(aggregated_result["detected_sentences"]) > 0
        )
        
        # 6. Determinar nivel de riesgo
        risk_level = "LOW"
        if aggregated_result["overall_score"] > 0.8:
            risk_level = "CRITICAL"
        elif aggregated_result["overall_score"] > 0.6:
            risk_level = "HIGH"
        elif aggregated_result["overall_score"] > 0.4:
            risk_level = "MEDIUM"
        
        # 7. Generar recomendaciones
        recommendations = []
        if aggregated_result["overall_score"] > 0.5:
            recommendations.append("Respuesta contiene posibles alucinaciones. Revisar manualmente.")
        if aggregated_result["confidence"] < 0.7:
            recommendations.append("Confianza baja en detección. Validar con métodos adicionales.")
        if len(aggregated_result["detected_sentences"]) > 0:
            recommendations.append(f"Se detectaron {len(aggregated_result['detected_sentences'])} frases sospechosas.")
        
        response = HallucinationDetectionResponse(
            has_hallucination=aggregated_result["overall_score"] > 0.5,
            overall_score=aggregated_result["overall_score"],
            confidence=aggregated_result["confidence"],
            method_scores=method_scores,
            detected_sentences=aggregated_result["detected_sentences"],
            recommendations=recommendations,
            requires_human_review=requires_human_review,
            risk_level=risk_level
        )
        
        # 8. Registrar en base de datos (llamar a servicio Java)
        # TODO: Llamar a API Java para persistir en tabla aud_rag_hallucinations
        
        logger.info(f"Hallucination detection completed: score={aggregated_result['overall_score']}, risk={risk_level}")
        
        return response
        
    except Exception as e:
        logger.error(f"Error detectando alucinaciones: {e}", exc_info=True)
        raise HTTPException(status_code=500, detail=f"Error detectando alucinaciones: {str(e)}")


class HumanValidationRequest(BaseModel):
    """Request para validación humana"""
    hallucination_id: int
    is_hallucination: bool
    validator_notes: Optional[str] = None
    validator_id: str


class HumanValidationResponse(BaseModel):
    """Response de validación humana"""
    validation_id: int
    hallucination_id: int
    validated_at: datetime
    is_correct_detection: bool


@router.post("/api/hallucination-detection/validate-human", response_model=HumanValidationResponse)
async def validate_human(
    request: HumanValidationRequest,
    aggregator: HallucinationAggregator = Depends()
):
    """
    Registra validación humana de detección de alucinación
    
    Args:
        request: Resultado de validación humana
    
    Returns:
        HumanValidationResponse
    """
    try:
        # Registrar validación humana
        validation_result = await aggregator.record_human_validation(
            hallucination_id=request.hallucination_id,
            is_hallucination=request.is_hallucination,
            validator_notes=request.validator_notes,
            validator_id=request.validator_id
        )
        
        # TODO: Persistir en base de datos
        
        return HumanValidationResponse(
            validation_id=validation_result["validation_id"],
            hallucination_id=request.hallucination_id,
            validated_at=datetime.utcnow(),
            is_correct_detection=validation_result["is_correct"]
        )
        
    except Exception as e:
        logger.error(f"Error registrando validación humana: {e}", exc_info=True)
        raise HTTPException(status_code=500, detail=f"Error registrando validación: {str(e)}")
```

### 3. Servicio SelfCheckGPT

**Archivo:** `app/services/selfcheck_service.py`

```python
"""
Servicio para detección de alucinaciones usando SelfCheckGPT
"""
import logging
from typing import Dict, Any, List
from transformers import AutoTokenizer, AutoModelForCausalLM
import torch

logger = logging.getLogger(__name__)


class SelfCheckService:
    """Servicio para detección usando SelfCheckGPT"""
    
    def __init__(self):
        self.model_name = "google/gemma-2b"  # O modelo específico de SelfCheck
        self.device = "cuda" if torch.cuda.is_available() else "cpu"
        self._load_model()
    
    def _load_model(self):
        """Carga modelo SelfCheckGPT"""
        try:
            self.tokenizer = AutoTokenizer.from_pretrained(self.model_name)
            self.model = AutoModelForCausalLM.from_pretrained(self.model_name)
            self.model.to(self.device)
            self.model.eval()
            logger.info(f"SelfCheckGPT model loaded: {self.model_name}")
        except Exception as e:
            logger.error(f"Error loading SelfCheckGPT model: {e}")
            raise
    
    async def detect(
        self,
        response: str,
        context: List[str]
    ) -> Dict[str, Any]:
        """
        Detecta alucinaciones usando SelfCheckGPT
        
        Args:
            response: Texto de respuesta
            context: Chunks de contexto
        
        Returns:
            Dict con score de alucinación y detalles
        """
        try:
            # SelfCheckGPT genera múltiples versiones de la respuesta
            # y compara consistencia
            
            # 1. Generar múltiples pasadas del modelo
            num_samples = 5
            samples = []
            
            for _ in range(num_samples):
                # Generar respuesta con temperatura alta para variación
                inputs = self.tokenizer(
                    f"Context: {' '.join(context)}\nResponse: {response}",
                    return_tensors="pt",
                    truncation=True,
                    max_length=512
                ).to(self.device)
                
                with torch.no_grad():
                    outputs = self.model.generate(
                        **inputs,
                        max_length=512,
                        temperature=0.9,
                        do_sample=True,
                        num_return_sequences=1
                    )
                
                sample = self.tokenizer.decode(outputs[0], skip_special_tokens=True)
                samples.append(sample)
            
            # 2. Calcular consistencia entre muestras
            consistency_score = self._calculate_consistency(samples, response)
            
            # 3. Score de alucinación (inverso de consistencia)
            hallucination_score = 1.0 - consistency_score
            
            # 4. Detectar frases específicas con problemas
            problematic_sentences = self._detect_problematic_sentences(response, context)
            
            return {
                "hallucination_score": float(hallucination_score),
                "confidence": 0.8 if num_samples >= 5 else 0.6,
                "details": {
                    "consistency_score": float(consistency_score),
                    "num_samples": num_samples,
                    "problematic_sentences": problematic_sentences
                }
            }
            
        except Exception as e:
            logger.error(f"Error in SelfCheckGPT detection: {e}")
            # Retornar score conservador si hay error
            return {
                "hallucination_score": 0.5,
                "confidence": 0.3,
                "details": {"error": str(e)}
            }
    
    def _calculate_consistency(self, samples: List[str], original: str) -> float:
        """Calcula consistencia entre muestras"""
        # Implementar cálculo de similitud semántica
        # Usar embeddings o BLEU score
        from sentence_transformers import SentenceTransformer
        
        model = SentenceTransformer('all-MiniLM-L6-v2')
        embeddings = model.encode(samples + [original])
        
        # Calcular similitud promedio
        original_emb = embeddings[-1]
        similarities = []
        for emb in embeddings[:-1]:
            similarity = torch.nn.functional.cosine_similarity(
                torch.tensor(original_emb).unsqueeze(0),
                torch.tensor(emb).unsqueeze(0)
            ).item()
            similarities.append(similarity)
        
        return sum(similarities) / len(similarities) if similarities else 0.0
    
    def _detect_problematic_sentences(
        self,
        response: str,
        context: List[str]
    ) -> List[Dict[str, Any]]:
        """Detecta frases específicas con problemas"""
        # Dividir respuesta en frases
        sentences = response.split('.')
        problematic = []
        
        # Verificar cada frase contra contexto
        for i, sentence in enumerate(sentences):
            if len(sentence.strip()) < 10:  # Ignorar frases muy cortas
                continue
            
            # Verificar si frase está respaldada por contexto
            is_supported = self._is_sentence_supported(sentence, context)
            
            if not is_supported:
                problematic.append({
                    "sentence_index": i,
                    "sentence": sentence.strip(),
                    "reason": "Not supported by context"
                })
        
        return problematic
    
    def _is_sentence_supported(self, sentence: str, context: List[str]) -> bool:
        """Verifica si frase está respaldada por contexto"""
        from sentence_transformers import SentenceTransformer
        
        model = SentenceTransformer('all-MiniLM-L6-v2')
        sentence_emb = model.encode([sentence])[0]
        context_emb = model.encode(context)
        
        # Calcular similitud máxima con contexto
        max_similarity = max([
            torch.nn.functional.cosine_similarity(
                torch.tensor(sentence_emb).unsqueeze(0),
                torch.tensor(emb).unsqueeze(0)
            ).item()
            for emb in context_emb
        ])
        
        return max_similarity > 0.6  # Umbral de similitud
```

### 4. Servicio FactScore

**Archivo:** `app/services/factscore_service.py`

```python
"""
Servicio para detección de alucinaciones usando FactScore
"""
import logging
from typing import Dict, Any, List

logger = logging.getLogger(__name__)


class FactScoreService:
    """Servicio para detección usando FactScore"""
    
    def __init__(self):
        # TODO: Cargar modelo FactScore
        # FactScore requiere modelo específico entrenado
        pass
    
    async def detect(
        self,
        response: str,
        context: List[str]
    ) -> Dict[str, Any]:
        """
        Detecta alucinaciones usando FactScore
        
        Args:
            response: Texto de respuesta
            context: Chunks de contexto
        
        Returns:
            Dict con score de alucinación
        """
        try:
            # FactScore valida hechos específicos en la respuesta
            # contra el contexto
            
            # 1. Extraer hechos de la respuesta
            facts = self._extract_facts(response)
            
            # 2. Verificar cada hecho contra contexto
            verified_facts = []
            unverified_facts = []
            
            for fact in facts:
                is_verified = self._verify_fact(fact, context)
                if is_verified:
                    verified_facts.append(fact)
                else:
                    unverified_facts.append(fact)
            
            # 3. Calcular score (proporción de hechos no verificados)
            total_facts = len(facts)
            if total_facts == 0:
                hallucination_score = 0.0
            else:
                hallucination_score = len(unverified_facts) / total_facts
            
            return {
                "hallucination_score": float(hallucination_score),
                "confidence": 0.85,
                "details": {
                    "total_facts": total_facts,
                    "verified_facts": len(verified_facts),
                    "unverified_facts": len(unverified_facts),
                    "unverified_fact_list": unverified_facts
                }
            }
            
        except Exception as e:
            logger.error(f"Error in FactScore detection: {e}")
            return {
                "hallucination_score": 0.5,
                "confidence": 0.3,
                "details": {"error": str(e)}
            }
    
    def _extract_facts(self, text: str) -> List[str]:
        """Extrae hechos del texto"""
        # Usar NER o modelo específico para extraer hechos
        # Por ahora, implementación simplificada
        import spacy
        
        nlp = spacy.load("en_core_web_sm")
        doc = nlp(text)
        
        facts = []
        for sent in doc.sents:
            # Extraer entidades y relaciones
            entities = [ent.text for ent in sent.ents]
            if entities:
                facts.append(sent.text)
        
        return facts
    
    def _verify_fact(self, fact: str, context: List[str]) -> bool:
        """Verifica si hecho está respaldado por contexto"""
        from sentence_transformers import SentenceTransformer
        
        model = SentenceTransformer('all-MiniLM-L6-v2')
        fact_emb = model.encode([fact])[0]
        context_emb = model.encode(context)
        
        # Calcular similitud máxima
        max_similarity = max([
            torch.nn.functional.cosine_similarity(
                torch.tensor(fact_emb).unsqueeze(0),
                torch.tensor(emb).unsqueeze(0)
            ).item()
            for emb in context_emb
        ])
        
        return max_similarity > 0.7
```

### 5. Servicio Entailment

**Archivo:** `app/services/entailment_service.py`

```python
"""
Servicio para detección usando Entailment models
"""
import logging
from typing import Dict, Any, List
from transformers import AutoTokenizer, AutoModelForSequenceClassification
import torch

logger = logging.getLogger(__name__)


class EntailmentService:
    """Servicio para detección usando modelos de Entailment"""
    
    def __init__(self):
        self.model_name = "microsoft/deberta-v3-base"  # O modelo específico de NLI
        self.device = "cuda" if torch.cuda.is_available() else "cpu"
        self._load_model()
    
    def _load_model(self):
        """Carga modelo de Entailment"""
        try:
            self.tokenizer = AutoTokenizer.from_pretrained(self.model_name)
            self.model = AutoModelForSequenceClassification.from_pretrained(self.model_name)
            self.model.to(self.device)
            self.model.eval()
            logger.info(f"Entailment model loaded: {self.model_name}")
        except Exception as e:
            logger.error(f"Error loading Entailment model: {e}")
            raise
    
    async def detect(
        self,
        response: str,
        context: List[str]
    ) -> Dict[str, Any]:
        """
        Detecta alucinaciones usando Entailment
        
        Args:
            response: Texto de respuesta
            context: Chunks de contexto
        
        Returns:
            Dict con score de alucinación
        """
        try:
            # Entailment verifica si respuesta es consecuencia lógica del contexto
            
            # Combinar contexto
            combined_context = " ".join(context)
            
            # Verificar entailment
            inputs = self.tokenizer(
                combined_context,
                response,
                return_tensors="pt",
                truncation=True,
                max_length=512
            ).to(self.device)
            
            with torch.no_grad():
                outputs = self.model(**inputs)
                probs = torch.nn.functional.softmax(outputs.logits, dim=-1)
                
                # Clases: ENTAILMENT, NEUTRAL, CONTRADICTION
                entailment_prob = probs[0][0].item()  # ENTAILMENT
                contradiction_prob = probs[0][2].item()  # CONTRADICTION
            
            # Score de alucinación basado en contradicción
            hallucination_score = contradiction_prob + (1 - entailment_prob) * 0.5
            
            return {
                "hallucination_score": float(hallucination_score),
                "confidence": 0.75,
                "details": {
                    "entailment_prob": float(entailment_prob),
                    "contradiction_prob": float(contradiction_prob),
                    "is_entailed": entailment_prob > 0.7,
                    "is_contradiction": contradiction_prob > 0.5
                }
            }
            
        except Exception as e:
            logger.error(f"Error in Entailment detection: {e}")
            return {
                "hallucination_score": 0.5,
                "confidence": 0.3,
                "details": {"error": str(e)}
            }
```

### 6. Agregador de Resultados

**Archivo:** `app/services/hallucination_aggregator.py`

```python
"""
Agrega resultados de múltiples métodos de detección
"""
import logging
from typing import List, Dict, Any
from statistics import mean, median

logger = logging.getLogger(__name__)


class HallucinationAggregator:
    """Agrega scores de múltiples métodos"""
    
    def aggregate(self, method_scores: List) -> Dict[str, Any]:
        """
        Agrega scores de múltiples métodos
        
        Args:
            method_scores: Lista de HallucinationScore
        
        Returns:
            Dict con resultado agregado
        """
        if not method_scores:
            return {
                "overall_score": 0.5,
                "confidence": 0.0,
                "detected_sentences": []
            }
        
        # 1. Calcular score promedio ponderado por confianza
        weighted_scores = []
        total_confidence = 0
        
        for score in method_scores:
            weight = score.confidence
            weighted_scores.append(score.score * weight)
            total_confidence += weight
        
        if total_confidence > 0:
            overall_score = sum(weighted_scores) / total_confidence
        else:
            overall_score = mean([s.score for s in method_scores])
        
        # 2. Confianza promedio
        avg_confidence = mean([s.confidence for s in method_scores])
        
        # 3. Recopilar frases detectadas
        detected_sentences = []
        for score in method_scores:
            if "problematic_sentences" in score.details:
                detected_sentences.extend(score.details["problematic_sentences"])
            if "unverified_fact_list" in score.details:
                detected_sentences.extend([
                    {"fact": fact, "method": score.method}
                    for fact in score.details["unverified_fact_list"]
                ])
        
        return {
            "overall_score": float(overall_score),
            "confidence": float(avg_confidence),
            "detected_sentences": detected_sentences
        }
    
    async def record_human_validation(
        self,
        hallucination_id: int,
        is_hallucination: bool,
        validator_notes: str,
        validator_id: str
    ) -> Dict[str, Any]:
        """Registra validación humana"""
        # TODO: Persistir en base de datos
        return {
            "validation_id": 1,
            "is_correct": is_hallucination
        }
```

---

## CONFIGURACIÓN

**Archivo:** `app/config.py`

```python
from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    # Modelos
    SELFCHECK_MODEL: str = "google/gemma-2b"
    FACTSCORE_MODEL: str = "factscore-model"
    ENTAILMENT_MODEL: str = "microsoft/deberta-v3-base"
    
    # Umbrales
    HALLUCINATION_THRESHOLD: float = 0.5
    CONFIDENCE_THRESHOLD: float = 0.7
    HUMAN_REVIEW_THRESHOLD: float = 0.6
    
    class Config:
        env_file = ".env"

settings = Settings()
```

---

## PRUEBAS REQUERIDAS

1. **Test 1:** Detectar alucinación clara → Debe detectar con score alto
2. **Test 2:** Respuesta correcta → Debe tener score bajo
3. **Test 3:** Múltiples métodos → Debe agregar correctamente
4. **Test 4:** Validación humana → Debe registrar feedback

---

## REFERENCIAS

- **Art. 13 EU AI Act:** Robustez, Seguridad y Precisión
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_005_EVALUACION_RAG.md#inc-005-002`
- **SelfCheckGPT Paper:** https://arxiv.org/abs/2303.08896
- **FactScore Paper:** https://arxiv.org/abs/2305.14251

