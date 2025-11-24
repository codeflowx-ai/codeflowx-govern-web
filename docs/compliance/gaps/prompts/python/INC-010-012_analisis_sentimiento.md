# PROMPT: INC-010-012 - Análisis de Sentimiento en Feedback

**Incidencia:** INC-010-012  
**Prioridad:** 🟢 MEDIA  
**Artículo EU AI Act:** Art. 72  
**Esfuerzo Estimado:** 1 día  
**Tipo:** Python - Microservicio  
**Referencia:** GAP-017

---

## CONTEXTO

Falta análisis de sentimiento en feedback. Se requiere integrar servicio de análisis de sentimiento, clasificar feedback (positivo, neutro, negativo) y calcular score de satisfacción según Art. 72.

**Estado Actual:**
- ✅ Entidad `UserFeedback` creada (INC-010-010)
- ✅ Campo `USFSENTIMENTSCORE` y `USFSENTIMENTLABEL` disponibles
- ❌ Análisis de sentimiento no implementado completamente
- ❌ No hay servicio dedicado de análisis de sentimiento

---

## REQUISITOS

1. Integrar servicio de análisis de sentimiento
2. Clasificar feedback (positivo, neutro, negativo)
3. Calcular score de satisfacción
4. API REST para análisis de sentimiento
5. Integración con sistema de feedback

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear Microservicio de Análisis de Sentimiento

```python
# sentiment-analysis-service/main.py
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from transformers import pipeline
import torch
from typing import Optional
import logging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = FastAPI(title="Sentiment Analysis Service", version="1.0.0")

# Cargar modelo de análisis de sentimiento
# Usar modelo multilingüe para soportar español e inglés
try:
    sentiment_analyzer = pipeline(
        "sentiment-analysis",
        model="nlptown/bert-base-multilingual-uncased-sentiment",
        device=0 if torch.cuda.is_available() else -1
    )
    logger.info("Modelo de análisis de sentimiento cargado exitosamente")
except Exception as e:
    logger.error(f"Error cargando modelo: {e}")
    sentiment_analyzer = None

class SentimentRequest(BaseModel):
    text: str
    language: Optional[str] = "es"  # es, en, etc.

class SentimentResponse(BaseModel):
    score: float  # -1.0 a 1.0
    label: str  # POSITIVE, NEUTRAL, NEGATIVE
    confidence: float  # 0.0 a 1.0
    raw_result: Optional[dict] = None

@app.get("/health")
async def health_check():
    """Health check endpoint"""
    return {
        "status": "healthy" if sentiment_analyzer is not None else "unhealthy",
        "model_loaded": sentiment_analyzer is not None
    }

@app.post("/api/sentiment/analyze", response_model=SentimentResponse)
async def analyze_sentiment(request: SentimentRequest):
    """
    Analiza el sentimiento del texto y retorna score y label
    
    Args:
        request: Contiene el texto a analizar y opcionalmente el idioma
        
    Returns:
        SentimentResponse con score (-1.0 a 1.0), label y confidence
    """
    if sentiment_analyzer is None:
        raise HTTPException(status_code=503, detail="Servicio de análisis no disponible")
    
    if not request.text or not request.text.strip():
        raise HTTPException(status_code=400, detail="Texto vacío")
    
    try:
        # Analizar sentimiento
        result = sentiment_analyzer(request.text)[0]
        
        logger.info(f"Análisis completado: {result}")
        
        # Mapear resultado del modelo a nuestro formato
        label_mapping = {
            "POSITIVE": "POSITIVE",
            "NEGATIVE": "NEGATIVE",
            "NEUTRAL": "NEUTRAL",
            "LABEL_0": "NEGATIVE",  # Modelo específico
            "LABEL_1": "NEGATIVE",
            "LABEL_2": "NEUTRAL",
            "LABEL_3": "POSITIVE",
            "LABEL_4": "POSITIVE"
        }
        
        # Obtener label
        raw_label = result.get('label', 'NEUTRAL')
        label = label_mapping.get(raw_label, "NEUTRAL")
        
        # Calcular score -1.0 a 1.0
        # Si el modelo retorna 0-4, mapear a -1.0 a 1.0
        if raw_label.startswith("LABEL_"):
            label_num = int(raw_label.split("_")[1])
            score = (label_num - 2) / 2.0  # -1.0, -0.5, 0.0, 0.5, 1.0
        else:
            # Si es positivo, score positivo; si negativo, score negativo
            confidence = result.get('score', 0.5)
            if label == "POSITIVE":
                score = confidence
            elif label == "NEGATIVE":
                score = -confidence
            else:
                score = 0.0
        
        # Confidence del resultado
        confidence = result.get('score', 0.5)
        
        return SentimentResponse(
            score=round(score, 3),
            label=label,
            confidence=round(confidence, 3),
            raw_result=result
        )
        
    except Exception as e:
        logger.error(f"Error analizando sentimiento: {e}", exc_info=True)
        raise HTTPException(status_code=500, detail=f"Error en análisis: {str(e)}")

@app.post("/api/sentiment/batch")
async def analyze_sentiment_batch(texts: list[str]):
    """
    Analiza sentimiento de múltiples textos en batch
    """
    if sentiment_analyzer is None:
        raise HTTPException(status_code=503, detail="Servicio de análisis no disponible")
    
    results = []
    for text in texts:
        try:
            result = await analyze_sentiment(SentimentRequest(text=text))
            results.append(result.dict())
        except Exception as e:
            logger.error(f"Error analizando texto: {e}")
            results.append({
                "error": str(e),
                "text": text[:50] + "..." if len(text) > 50 else text
            })
    
    return {"results": results}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8080)
```

### 2. Crear Cliente Java para Servicio de Sentimiento

```java
package com.codeflowx.govern.services.integration;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SentimentAnalysisService {
    
    private final RestTemplate restTemplate;
    
    @Value("${services.sentiment-analysis.url:http://sentiment-analysis-service:8080}")
    private String sentimentServiceUrl;
    
    @Data
    public static class SentimentRequest {
        private String text;
        private String language = "es";
    }
    
    @Data
    public static class SentimentResponse {
        private Double score; // -1.0 a 1.0
        private String label; // POSITIVE, NEUTRAL, NEGATIVE
        private Double confidence;
    }
    
    /**
     * Analiza el sentimiento del texto
     */
    public SentimentResponse analyze(String text) {
        return analyze(text, "es");
    }
    
    /**
     * Analiza el sentimiento del texto con idioma específico
     */
    public SentimentResponse analyze(String text, String language) {
        try {
            log.info("Analizando sentimiento de texto (length: {})", text.length());
            
            SentimentRequest request = new SentimentRequest();
            request.setText(text);
            request.setLanguage(language);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<SentimentRequest> entity = new HttpEntity<>(request, headers);
            
            ResponseEntity<SentimentResponse> response = restTemplate.postForEntity(
                sentimentServiceUrl + "/api/sentiment/analyze",
                entity,
                SentimentResponse.class
            );
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Sentimiento analizado: {} (score: {})", 
                    response.getBody().getLabel(), response.getBody().getScore());
                return response.getBody();
            } else {
                throw new RuntimeException("Error en respuesta del servicio de sentimiento");
            }
            
        } catch (Exception e) {
            log.error("Error analizando sentimiento", e);
            // Retornar valor por defecto en caso de error
            SentimentResponse defaultResponse = new SentimentResponse();
            defaultResponse.setScore(0.0);
            defaultResponse.setLabel("NEUTRAL");
            defaultResponse.setConfidence(0.5);
            return defaultResponse;
        }
    }
}
```

### 3. Crear Dockerfile para Microservicio

```dockerfile
# sentiment-analysis-service/Dockerfile
FROM python:3.11-slim

WORKDIR /app

# Instalar dependencias del sistema
RUN apt-get update && apt-get install -y \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

# Instalar dependencias Python
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copiar código
COPY . .

# Exponer puerto
EXPOSE 8080

# Comando de inicio
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8080"]
```

### 4. Crear requirements.txt

```txt
fastapi==0.104.1
uvicorn[standard]==0.24.0
pydantic==2.5.0
transformers==4.35.0
torch==2.1.0
sentencepiece==0.1.99
```

### 5. Integrar con UserFeedbackService

```java
// Modificar método analyzeSentiment en UserFeedbackService

private void analyzeSentiment(UserFeedback feedback) {
    try {
        SentimentAnalysisService.SentimentResponse result = 
            sentimentService.analyze(feedback.getUsffeedbacktext());
        
        feedback.setUsfsentimentscore(BigDecimal.valueOf(result.getScore()));
        feedback.setUsfsentimentlabel(
            mapToSentimentLabel(BigDecimal.valueOf(result.getScore())));
        
        log.info("Sentimiento analizado: {} (score: {}, confidence: {})", 
            feedback.getUsfsentimentlabel(), 
            feedback.getUsfsentimentscore(),
            result.getConfidence());
    } catch (Exception e) {
        log.error("Error analizando sentimiento", e);
        // Continuar sin sentimiento
    }
}
```

---

## VALIDACIONES

1. ✅ Microservicio de análisis de sentimiento implementado
2. ✅ API REST funcional
3. ✅ Clasificación de feedback (positivo, neutro, negativo) funcionando
4. ✅ Cálculo de score de satisfacción implementado
5. ✅ Integración con sistema de feedback completa

---

## NOTAS

- Usar modelo multilingüe para soportar español e inglés
- El servicio puede ser pesado (requiere GPU recomendado)
- Considerar caché para textos repetidos
- Manejar errores gracefully
- Considerar batch processing para múltiples textos

