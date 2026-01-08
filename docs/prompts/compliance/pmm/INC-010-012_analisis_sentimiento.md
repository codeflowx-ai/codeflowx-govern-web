# INC-010-012: Análisis de Sentimiento en Feedback de Usuarios

**Incidencia:** INC-010-012
**Módulo:** Compliance - Post-Market Monitoring (PMM)
**Prioridad:** 🟢 **MEDIA**
**Estado:** ⚠️ **PARCIALMENTE IMPLEMENTADO** (básico funcional)
**Base Legal:** EU AI Act Art. 72

---

## 📋 DESCRIPCIÓN

Implementar un microservicio Python especializado para análisis de sentimiento en feedback de usuarios del sistema PMM. El servicio debe analizar el texto del feedback y determinar el sentimiento (POSITIVE, NEUTRAL, NEGATIVE) con un score de confianza.

**Contexto Actual:**
- ✅ Existe implementación básica en Java (`SimpleSentimentAnalysisService`) que usa palabras clave
- ✅ La interfaz `SentimentAnalysisService` está definida en el backend Java
- ✅ El servicio se integra con `UserFeedbackService` para análisis automático
- ⚠️ Se requiere microservicio Python con modelos ML avanzados para producción

---

## 🎯 OBJETIVOS

1. **Microservicio Python REST** para análisis de sentimiento
2. **Integración con modelos ML** vía wrappers (transformers, spaCy, etc.)
3. **API REST** compatible con el backend Java existente
4. **Análisis multilingüe** (español, inglés, francés, alemán, italiano, portugués)
5. **Score de confianza** para cada análisis
6. **Caché de resultados** para optimizar rendimiento

---

## 🏗️ ARQUITECTURA

### Estructura del Microservicio

```
codeflowx-sentiment-analysis-service/
├── app/
│   ├── __init__.py
│   ├── main.py                 # FastAPI application
│   ├── config.py              # Configuración
│   ├── models/
│   │   ├── __init__.py
│   │   ├── sentiment_model.py  # Wrapper para modelos ML
│   │   └── model_loader.py     # Carga de modelos
│   ├── services/
│   │   ├── __init__.py
│   │   ├── sentiment_service.py  # Lógica de negocio
│   │   └── cache_service.py      # Caché de resultados
│   ├── api/
│   │   ├── __init__.py
│   │   ├── routes.py            # Endpoints REST
│   │   └── schemas.py           # Pydantic schemas
│   └── utils/
│       ├── __init__.py
│       ├── text_preprocessing.py
│       └── language_detection.py
├── tests/
│   ├── __init__.py
│   ├── test_sentiment_service.py
│   └── test_api.py
├── requirements.txt
├── Dockerfile
├── docker-compose.yml
└── README.md
```

---

## 🔧 IMPLEMENTACIÓN

### 1. Dependencias (requirements.txt)

```txt
# Framework web
fastapi==0.104.1
uvicorn[standard]==0.24.0
pydantic==2.5.0
pydantic-settings==2.1.0

# Modelos ML - Opción 1: Transformers (Hugging Face)
transformers==4.35.0
torch==2.1.0
sentencepiece==0.1.99

# Modelos ML - Opción 2: spaCy (alternativa ligera)
spacy==3.7.2
# python -m spacy download es_core_news_sm
# python -m spacy download en_core_web_sm
# python -m spacy download fr_core_news_sm
# python -m spacy download de_core_news_sm
# python -m spacy download it_core_news_sm
# python -m spacy download pt_core_news_sm

# Modelos ML - Opción 3: TextBlob (muy ligero, multilingüe)
textblob==0.17.1

# Procesamiento de texto
nltk==3.8.1
langdetect==1.0.9

# Caché
redis==5.0.1
hazelcast-python-client==5.3.0  # Alternativa a Redis

# Logging y monitoreo
loguru==0.7.2
prometheus-client==0.19.0

# Testing
pytest==7.4.3
pytest-asyncio==0.21.1
httpx==0.25.2
```

---

### 2. Configuración (app/config.py)

```python
from pydantic_settings import BaseSettings
from typing import Literal

class Settings(BaseSettings):
    # Servidor
    app_name: str = "CodeflowX Sentiment Analysis Service"
    app_version: str = "1.0.0"
    host: str = "0.0.0.0"
    port: int = 8085

    # Modelo ML
    model_provider: Literal["transformers", "spacy", "textblob", "hybrid"] = "transformers"
    model_name: str = "cardiffnlp/twitter-xlm-roberta-base-sentiment"  # Multilingüe
    # Alternativas:
    # - "nlptown/bert-base-multilingual-uncased-sentiment" (multilingüe)
    # - "distilbert-base-uncased-finetuned-sst-2-english" (solo inglés)
    # - "finiteautomata/bertweet-base-sentiment-analysis" (español/inglés)

    # Caché
    cache_enabled: bool = True
    cache_type: Literal["redis", "memory", "hazelcast"] = "redis"
    cache_ttl: int = 3600  # 1 hora
    redis_host: str = "localhost"
    redis_port: int = 6379
    redis_db: int = 0

    # Performance
    batch_size: int = 32
    max_text_length: int = 512
    use_gpu: bool = False

    # Logging
    log_level: str = "INFO"

    class Config:
        env_file = ".env"
        env_file_encoding = "utf-8"

settings = Settings()
```

---

### 3. Wrapper de Modelo ML (app/models/sentiment_model.py)

```python
from abc import ABC, abstractmethod
from typing import Dict, List, Optional
from dataclasses import dataclass
from enum import Enum

class SentimentLabel(str, Enum):
    POSITIVE = "POSITIVE"
    NEUTRAL = "NEUTRAL"
    NEGATIVE = "NEGATIVE"

@dataclass
class SentimentResult:
    label: SentimentLabel
    score: float  # 0.0 - 1.0
    confidence: float  # 0.0 - 1.0
    model_used: str

class SentimentModelWrapper(ABC):
    """Interfaz abstracta para wrappers de modelos ML"""

    @abstractmethod
    def analyze(self, text: str, language: Optional[str] = None) -> SentimentResult:
        """Analiza el sentimiento de un texto"""
        pass

    @abstractmethod
    def analyze_batch(self, texts: List[str], language: Optional[str] = None) -> List[SentimentResult]:
        """Analiza múltiples textos en batch"""
        pass

    @abstractmethod
    def is_available(self) -> bool:
        """Verifica si el modelo está disponible"""
        pass

# ============================================================================
# OPCIÓN 1: Transformers (Hugging Face) - RECOMENDADO
# ============================================================================

class TransformersSentimentModel(SentimentModelWrapper):
    """
    Wrapper para modelos de Transformers (Hugging Face)

    Ventajas:
    - Modelos pre-entrenados de alta calidad
    - Soporte multilingüe
    - Fácil cambio de modelo
    - Mejor precisión

    Desventajas:
    - Mayor uso de memoria
    - Requiere GPU para mejor rendimiento
    """

    def __init__(self, model_name: str, use_gpu: bool = False):
        from transformers import AutoTokenizer, AutoModelForSequenceClassification
        from transformers import pipeline
        import torch

        self.model_name = model_name
        self.device = "cuda" if use_gpu and torch.cuda.is_available() else "cpu"

        # Cargar modelo y tokenizer
        self.tokenizer = AutoTokenizer.from_pretrained(model_name)
        self.model = AutoModelForSequenceClassification.from_pretrained(
            model_name,
            device_map="auto" if self.device == "cuda" else None
        )

        # Crear pipeline de análisis de sentimiento
        self.pipeline = pipeline(
            "sentiment-analysis",
            model=self.model,
            tokenizer=self.tokenizer,
            device=0 if self.device == "cuda" else -1
        )

    def analyze(self, text: str, language: Optional[str] = None) -> SentimentResult:
        """Analiza sentimiento usando Transformers"""
        try:
            result = self.pipeline(text)[0]

            # Mapear labels del modelo a nuestros enums
            label_str = result['label'].upper()
            if 'POSITIVE' in label_str or 'POS' in label_str:
                label = SentimentLabel.POSITIVE
            elif 'NEGATIVE' in label_str or 'NEG' in label_str:
                label = SentimentLabel.NEGATIVE
            else:
                label = SentimentLabel.NEUTRAL

            score = result['score']

            return SentimentResult(
                label=label,
                score=score,
                confidence=score,
                model_used=f"transformers:{self.model_name}"
            )
        except Exception as e:
            # Fallback a neutral en caso de error
            return SentimentResult(
                label=SentimentLabel.NEUTRAL,
                score=0.5,
                confidence=0.0,
                model_used=f"transformers:{self.model_name}:error"
            )

    def analyze_batch(self, texts: List[str], language: Optional[str] = None) -> List[SentimentResult]:
        """Análisis en batch para mejor rendimiento"""
        results = self.pipeline(texts)
        sentiment_results = []

        for result in results:
            label_str = result['label'].upper()
            if 'POSITIVE' in label_str or 'POS' in label_str:
                label = SentimentLabel.POSITIVE
            elif 'NEGATIVE' in label_str or 'NEG' in label_str:
                label = SentimentLabel.NEGATIVE
            else:
                label = SentimentLabel.NEUTRAL

            sentiment_results.append(SentimentResult(
                label=label,
                score=result['score'],
                confidence=result['score'],
                model_used=f"transformers:{self.model_name}"
            ))

        return sentiment_results

    def is_available(self) -> bool:
        return self.model is not None and self.tokenizer is not None

# ============================================================================
# OPCIÓN 2: spaCy (Alternativa ligera)
# ============================================================================

class SpacySentimentModel(SentimentModelWrapper):
    """
    Wrapper para análisis de sentimiento con spaCy

    Ventajas:
    - Más ligero que Transformers
    - Soporte multilingüe con modelos específicos
    - Rápido para procesamiento

    Desventajas:
    - Requiere modelos específicos por idioma
    - Menor precisión que Transformers
    - Requiere entrenamiento adicional para sentimiento
    """

    def __init__(self, language: str = "es"):
        import spacy

        self.language = language
        # Cargar modelo spaCy (requiere descarga previa)
        try:
            self.nlp = spacy.load(f"{language}_core_news_sm")
        except OSError:
            # Fallback a modelo en inglés si no está disponible
            self.nlp = spacy.load("en_core_web_sm")

        # spaCy no tiene análisis de sentimiento nativo, usar TextBlob como complemento
        from textblob import TextBlob
        self.textblob = TextBlob

    def analyze(self, text: str, language: Optional[str] = None) -> SentimentResult:
        """Analiza sentimiento usando spaCy + TextBlob"""
        try:
            # Usar TextBlob para análisis de sentimiento (spaCy no lo tiene nativo)
            blob = self.textblob(text)
            polarity = blob.sentiment.polarity  # -1.0 a 1.0

            if polarity > 0.1:
                label = SentimentLabel.POSITIVE
                score = (polarity + 1) / 2  # Normalizar a 0-1
            elif polarity < -0.1:
                label = SentimentLabel.NEGATIVE
                score = (1 - polarity) / 2  # Normalizar a 0-1
            else:
                label = SentimentLabel.NEUTRAL
                score = 0.5

            return SentimentResult(
                label=label,
                score=score,
                confidence=abs(polarity),
                model_used=f"spacy+textblob:{self.language}"
            )
        except Exception as e:
            return SentimentResult(
                label=SentimentLabel.NEUTRAL,
                score=0.5,
                confidence=0.0,
                model_used=f"spacy+textblob:{self.language}:error"
            )

    def analyze_batch(self, texts: List[str], language: Optional[str] = None) -> List[SentimentResult]:
        return [self.analyze(text, language) for text in texts]

    def is_available(self) -> bool:
        return self.nlp is not None

# ============================================================================
# OPCIÓN 3: TextBlob (Muy ligero)
# ============================================================================

class TextBlobSentimentModel(SentimentModelWrapper):
    """
    Wrapper para TextBlob (muy ligero, multilingüe básico)

    Ventajas:
    - Muy ligero
    - Fácil de usar
    - Multilingüe básico

    Desventajas:
    - Menor precisión
    - Mejor para inglés
    """

    def __init__(self):
        from textblob import TextBlob
        self.TextBlob = TextBlob

    def analyze(self, text: str, language: Optional[str] = None) -> SentimentResult:
        try:
            blob = self.TextBlob(text)
            polarity = blob.sentiment.polarity

            if polarity > 0.1:
                label = SentimentLabel.POSITIVE
                score = (polarity + 1) / 2
            elif polarity < -0.1:
                label = SentimentLabel.NEGATIVE
                score = (1 - polarity) / 2
            else:
                label = SentimentLabel.NEUTRAL
                score = 0.5

            return SentimentResult(
                label=label,
                score=score,
                confidence=abs(polarity),
                model_used="textblob"
            )
        except Exception:
            return SentimentResult(
                label=SentimentLabel.NEUTRAL,
                score=0.5,
                confidence=0.0,
                model_used="textblob:error"
            )

    def analyze_batch(self, texts: List[str], language: Optional[str] = None) -> List[SentimentResult]:
        return [self.analyze(text, language) for text in texts]

    def is_available(self) -> bool:
        return True

# ============================================================================
# Factory para crear el modelo según configuración
# ============================================================================

def create_sentiment_model(provider: str, model_name: str = None, use_gpu: bool = False) -> SentimentModelWrapper:
    """Factory para crear el modelo de sentimiento según configuración"""

    if provider == "transformers":
        model_name = model_name or "cardiffnlp/twitter-xlm-roberta-base-sentiment"
        return TransformersSentimentModel(model_name, use_gpu)

    elif provider == "spacy":
        language = model_name or "es"
        return SpacySentimentModel(language)

    elif provider == "textblob":
        return TextBlobSentimentModel()

    elif provider == "hybrid":
        # Usar múltiples modelos y combinar resultados
        # Implementación avanzada: combinar Transformers + TextBlob
        primary = TransformersSentimentModel(
            model_name or "cardiffnlp/twitter-xlm-roberta-base-sentiment",
            use_gpu
        )
        fallback = TextBlobSentimentModel()
        return HybridSentimentModel(primary, fallback)

    else:
        raise ValueError(f"Provider no soportado: {provider}")

class HybridSentimentModel(SentimentModelWrapper):
    """Combina múltiples modelos para mayor precisión"""

    def __init__(self, primary: SentimentModelWrapper, fallback: SentimentModelWrapper):
        self.primary = primary
        self.fallback = fallback

    def analyze(self, text: str, language: Optional[str] = None) -> SentimentResult:
        try:
            result = self.primary.analyze(text, language)
            if result.confidence < 0.7:
                # Si la confianza es baja, usar fallback y promediar
                fallback_result = self.fallback.analyze(text, language)
                # Combinar resultados (promedio ponderado)
                combined_score = (result.score * 0.7) + (fallback_result.score * 0.3)
                combined_confidence = (result.confidence * 0.7) + (fallback_result.confidence * 0.3)

                # Determinar label basado en score combinado
                if combined_score > 0.6:
                    label = SentimentLabel.POSITIVE
                elif combined_score < 0.4:
                    label = SentimentLabel.NEGATIVE
                else:
                    label = SentimentLabel.NEUTRAL

                return SentimentResult(
                    label=label,
                    score=combined_score,
                    confidence=combined_confidence,
                    model_used=f"hybrid:{result.model_used}+{fallback_result.model_used}"
                )
            return result
        except Exception:
            return self.fallback.analyze(text, language)

    def analyze_batch(self, texts: List[str], language: Optional[str] = None) -> List[SentimentResult]:
        return [self.analyze(text, language) for text in texts]

    def is_available(self) -> bool:
        return self.primary.is_available() or self.fallback.is_available()
```

---

### 4. Servicio de Análisis (app/services/sentiment_service.py)

```python
from typing import List, Optional
from app.models.sentiment_model import (
    SentimentModelWrapper,
    SentimentResult,
    create_sentiment_model
)
from app.utils.language_detection import detect_language
from app.utils.text_preprocessing import preprocess_text
from app.services.cache_service import CacheService
from app.config import settings
from loguru import logger

class SentimentService:
    """Servicio principal de análisis de sentimiento"""

    def __init__(self):
        self.model = create_sentiment_model(
            settings.model_provider,
            settings.model_name,
            settings.use_gpu
        )
        self.cache = CacheService() if settings.cache_enabled else None
        logger.info(f"SentimentService inicializado con modelo: {settings.model_provider}")

    async def analyze_sentiment(
        self,
        text: str,
        language: Optional[str] = None,
        use_cache: bool = True
    ) -> SentimentResult:
        """
        Analiza el sentimiento de un texto

        Args:
            text: Texto a analizar
            language: Idioma del texto (opcional, se detecta automáticamente)
            use_cache: Si usar caché de resultados

        Returns:
            SentimentResult con label, score y confidence
        """
        # Validar entrada
        if not text or len(text.strip()) == 0:
            return SentimentResult(
                label=SentimentLabel.NEUTRAL,
                score=0.5,
                confidence=0.0,
                model_used="none:empty_text"
            )

        # Truncar texto si es muy largo
        if len(text) > settings.max_text_length:
            text = text[:settings.max_text_length]
            logger.warning(f"Texto truncado a {settings.max_text_length} caracteres")

        # Verificar caché
        if use_cache and self.cache:
            cached_result = await self.cache.get_sentiment(text)
            if cached_result:
                logger.debug(f"Resultado obtenido de caché para texto: {text[:50]}...")
                return cached_result

        # Detectar idioma si no se proporciona
        if not language:
            language = detect_language(text)

        # Preprocesar texto
        processed_text = preprocess_text(text, language)

        # Analizar sentimiento
        try:
            result = self.model.analyze(processed_text, language)
            logger.info(f"Sentimiento analizado: {result.label} (score: {result.score:.2f}, confidence: {result.confidence:.2f})")

            # Guardar en caché
            if use_cache and self.cache:
                await self.cache.set_sentiment(text, result)

            return result

        except Exception as e:
            logger.error(f"Error analizando sentimiento: {e}")
            # Retornar resultado neutral en caso de error
            return SentimentResult(
                label=SentimentLabel.NEUTRAL,
                score=0.5,
                confidence=0.0,
                model_used=f"{settings.model_provider}:error"
            )

    async def analyze_batch(
        self,
        texts: List[str],
        language: Optional[str] = None
    ) -> List[SentimentResult]:
        """Analiza múltiples textos en batch"""
        results = []
        for text in texts:
            result = await self.analyze_sentiment(text, language, use_cache=True)
            results.append(result)
        return results

    def is_available(self) -> bool:
        """Verifica si el servicio está disponible"""
        return self.model.is_available()
```

---

### 5. API REST (app/api/routes.py)

```python
from fastapi import APIRouter, HTTPException, Depends
from pydantic import BaseModel, Field
from typing import List, Optional
from app.services.sentiment_service import SentimentService
from app.models.sentiment_model import SentimentLabel
from app.api.schemas import SentimentAnalysisRequest, SentimentAnalysisResponse

router = APIRouter(prefix="/api/v1/sentiment", tags=["Sentiment Analysis"])

# Dependencia para el servicio
def get_sentiment_service() -> SentimentService:
    return SentimentService()

@router.post("/analyze", response_model=SentimentAnalysisResponse)
async def analyze_sentiment(
    request: SentimentAnalysisRequest,
    service: SentimentService = Depends(get_sentiment_service)
):
    """
    Analiza el sentimiento de un texto

    Compatible con la interfaz Java SentimentAnalysisService
    """
    if not service.is_available():
        raise HTTPException(status_code=503, detail="Sentiment analysis service unavailable")

    result = await service.analyze_sentiment(
        text=request.text,
        language=request.language
    )

    return SentimentAnalysisResponse(
        sentiment=result.label.value,
        score=result.score,
        confidence=result.confidence,
        modelUsed=result.model_used
    )

@router.post("/analyze/batch", response_model=List[SentimentAnalysisResponse])
async def analyze_sentiment_batch(
    texts: List[str],
    language: Optional[str] = None,
    service: SentimentService = Depends(get_sentiment_service)
):
    """Analiza múltiples textos en batch"""
    if not service.is_available():
        raise HTTPException(status_code=503, detail="Sentiment analysis service unavailable")

    results = await service.analyze_batch(texts, language)

    return [
        SentimentAnalysisResponse(
            sentiment=r.label.value,
            score=r.score,
            confidence=r.confidence,
            modelUsed=r.model_used
        )
        for r in results
    ]

@router.get("/health")
async def health_check(service: SentimentService = Depends(get_sentiment_service)):
    """Health check del servicio"""
    return {
        "status": "healthy" if service.is_available() else "unhealthy",
        "model_provider": service.model.__class__.__name__,
        "available": service.is_available()
    }
```

---

### 6. Schemas (app/api/schemas.py)

```python
from pydantic import BaseModel, Field
from typing import Optional

class SentimentAnalysisRequest(BaseModel):
    """Request para análisis de sentimiento"""
    text: str = Field(..., description="Texto a analizar", min_length=1, max_length=5000)
    language: Optional[str] = Field(None, description="Idioma del texto (es, en, fr, de, it, pt)")

class SentimentAnalysisResponse(BaseModel):
    """Response del análisis de sentimiento"""
    sentiment: str = Field(..., description="Sentimiento: POSITIVE, NEUTRAL, NEGATIVE")
    score: float = Field(..., description="Score del sentimiento (0.0 - 1.0)", ge=0.0, le=1.0)
    confidence: float = Field(..., description="Confianza del análisis (0.0 - 1.0)", ge=0.0, le=1.0)
    modelUsed: str = Field(..., description="Modelo utilizado para el análisis")
```

---

### 7. Aplicación Principal (app/main.py)

```python
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.api.routes import router
from app.config import settings
from loguru import logger

app = FastAPI(
    title=settings.app_name,
    version=settings.app_version,
    description="Microservicio de análisis de sentimiento para feedback de usuarios PMM"
)

# CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Routers
app.include_router(router)

@app.get("/")
async def root():
    return {
        "service": settings.app_name,
        "version": settings.app_version,
        "status": "running"
    }

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(
        "app.main:app",
        host=settings.host,
        port=settings.port,
        reload=True
    )
```

---

## 🔌 INTEGRACIÓN CON BACKEND JAVA

### Actualizar SimpleSentimentAnalysisService.java

```java
@Service
@Slf4j
public class SimpleSentimentAnalysisService implements SentimentAnalysisService {

    @Value("${services.sentiment-analysis.enabled:true}")
    private boolean sentimentAnalysisEnabled;

    @Value("${services.sentiment-analysis.base-url:http://localhost:8085}")
    private String sentimentAnalysisBaseUrl;

    private final RestTemplate restTemplate;

    public SimpleSentimentAnalysisService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Sentiment analyzeSentiment(String text) {
        if (!sentimentAnalysisEnabled) {
            // Fallback a análisis básico local
            return analyzeSentimentLocal(text);
        }

        try {
            String url = sentimentAnalysisBaseUrl + "/api/v1/sentiment/analyze";

            Map<String, Object> request = new HashMap<>();
            request.put("text", text);

            SentimentAnalysisResponseDto response = restTemplate.postForObject(
                url,
                request,
                SentimentAnalysisResponseDto.class
            );

            if (response != null) {
                return Sentiment.valueOf(response.getSentiment());
            }
        } catch (Exception e) {
            log.warn("Error calling sentiment analysis service, using fallback: {}", e.getMessage());
        }

        // Fallback a análisis local
        return analyzeSentimentLocal(text);
    }

    private Sentiment analyzeSentimentLocal(String text) {
        // Análisis básico por palabras clave (implementación actual)
        // ... código existente ...
    }
}
```

---

## 🐳 DOCKER

### Dockerfile

```dockerfile
FROM python:3.11-slim

WORKDIR /app

# Instalar dependencias del sistema
RUN apt-get update && apt-get install -y \
    gcc \
    g++ \
    && rm -rf /var/lib/apt/lists/*

# Copiar requirements
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Descargar modelos spaCy (si se usa)
# RUN python -m spacy download es_core_news_sm
# RUN python -m spacy download en_core_web_sm

# Copiar código
COPY app/ ./app/

# Exponer puerto
EXPOSE 8085

# Comando
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8085"]
```

---

## 📊 RECOMENDACIONES

### Modelos Recomendados por Caso de Uso

1. **Producción (Alta Precisión):**
   - `cardiffnlp/twitter-xlm-roberta-base-sentiment` (multilingüe, mejor precisión)
   - `nlptown/bert-base-multilingual-uncased-sentiment` (multilingüe, balanceado)

2. **Desarrollo/Testing (Ligero):**
   - `textblob` (muy rápido, básico)
   - `spacy + textblob` (balanceado)

3. **Híbrido (Mejor de ambos):**
   - `hybrid` mode: Transformers como primario + TextBlob como fallback

### Configuración Recomendada

```env
# .env
MODEL_PROVIDER=transformers
MODEL_NAME=cardiffnlp/twitter-xlm-roberta-base-sentiment
USE_GPU=true
CACHE_ENABLED=true
CACHE_TYPE=redis
REDIS_HOST=redis
REDIS_PORT=6379
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

- [ ] Crear estructura del microservicio
- [ ] Instalar dependencias (requirements.txt)
- [ ] Implementar wrappers de modelos (Transformers, spaCy, TextBlob)
- [ ] Implementar servicio de análisis
- [ ] Implementar API REST (FastAPI)
- [ ] Implementar caché (Redis)
- [ ] Agregar tests unitarios
- [ ] Crear Dockerfile
- [ ] Configurar docker-compose
- [ ] Actualizar backend Java para llamar al microservicio
- [ ] Documentar API (OpenAPI/Swagger)
- [ ] Configurar monitoreo y logging
- [ ] Desplegar en entorno de desarrollo
- [ ] Testing de integración
- [ ] Desplegar en producción

---

## 📚 REFERENCIAS

- **Hugging Face Transformers:** https://huggingface.co/transformers/
- **spaCy:** https://spacy.io/
- **TextBlob:** https://textblob.readthedocs.io/
- **FastAPI:** https://fastapi.tiangolo.com/
- **Modelos de Sentimiento Multilingüe:** https://huggingface.co/models?search=sentiment+multilingual

---

**Nota:** Este prompt proporciona una implementación completa usando wrappers de modelos ML. Los modelos se pueden cambiar fácilmente modificando la configuración sin cambiar el código.
