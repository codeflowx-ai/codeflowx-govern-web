# PROMPT: INC-010-010 - Integración con Sistema de Feedback

**Incidencia:** INC-010-010  
**Prioridad:** 🟡 ALTA  
**Artículo EU AI Act:** Art. 72  
**Esfuerzo Estimado:** 3 días  
**Tipo:** Java - Backend + Python (Microservicio)  
**Referencia:** GAP-017

---

## CONTEXTO

No hay integración con sistema de feedback de usuarios para cálculo de satisfacción. Se requiere integrar con sistema de tickets/incidentes y calcular métrica de satisfacción según Art. 72.

**Estado Actual:**
- ✅ Métrica `userSatisfactionScore` en `PostMarketMonitoringService` (mock)
- ❌ No hay integración con sistema de feedback
- ❌ No hay análisis de sentimiento
- ❌ No hay tracking de quejas y escalaciones

---

## REQUISITOS

1. Crear entidad `UserFeedback`
2. Integrar con sistema de tickets/incidentes
3. Implementar análisis de sentimiento
4. Calcular métrica de satisfacción
5. Integrar con `PostMarketMonitoringService`

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear Entidad JPA `UserFeedback`

**Tabla SQL:**
```sql
CREATE TABLE USFUSERFEEDBACKS (
    iduuid UUID UNIQUE,
    IDXUSFFEEDBACK BIGSERIAL PRIMARY KEY,
    IDXPROJECT BIGINT NOT NULL,
    IDXMODEL BIGINT,
    USFFEEDBACKTYPE VARCHAR(50) NOT NULL, -- RATING, COMMENT, COMPLAINT, ESCALATION
    USFFEEDBACKTEXT TEXT,
    USFRATING INTEGER, -- 1-5 stars
    USFSENTIMENTSCORE DECIMAL, -- -1.0 a 1.0
    USFSENTIMENTLABEL VARCHAR(50), -- POSITIVE, NEUTRAL, NEGATIVE
    USFUSERID VARCHAR(255),
    USFSOURCE VARCHAR(100), -- TICKET_SYSTEM, API, UI
    USFSTATUS TEXT[] NOT NULL, -- PENDING, PROCESSED, ARCHIVED
    USFCREATEDAT TIMESTAMP NOT NULL,
    USFUPDATEDAT TIMESTAMP,
    CONSTRAINT FK_USF_PROJECT FOREIGN KEY (IDXPROJECT) REFERENCES PRJPROJECTS(IDXPROJECT),
    CONSTRAINT FK_USF_MODEL FOREIGN KEY (IDXMODEL) REFERENCES MODMODELS(IDXMODEL)
);

CREATE INDEX IDX_USF_PROJECT ON USFUSERFEEDBACKS(IDXPROJECT);
CREATE INDEX IDX_USF_MODEL ON USFUSERFEEDBACKS(IDXMODEL);
CREATE INDEX IDX_USF_CREATED ON USFUSERFEEDBACKS(USFCREATEDAT);
CREATE INDEX IDX_USF_STATUS ON USFUSERFEEDBACKS USING GIN(USFSTATUS);
```

**Entidad Java:**
```java
package com.codeflowx.govern.entities.compliance;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "USFUSERFEEDBACKS")
@Data
public class UserFeedback {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXUSFFEEDBACK")
    private Long idxusffeedback;
    
    @Column(name = "iduuid", unique = true, nullable = false)
    private UUID iduuid = UUID.randomUUID();
    
    @Column(name = "IDXPROJECT", nullable = false)
    private Long idxproject;
    
    @Column(name = "IDXMODEL")
    private Long idxmodel;
    
    @Column(name = "USFFEEDBACKTYPE", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private FeedbackType usffeedbacktype;
    
    @Column(name = "USFFEEDBACKTEXT", columnDefinition = "TEXT")
    private String usffeedbacktext;
    
    @Column(name = "USFRATING")
    private Integer usfrating; // 1-5
    
    @Column(name = "USFSENTIMENTSCORE", precision = 3, scale = 2)
    private BigDecimal usfsentimentscore; // -1.0 a 1.0
    
    @Column(name = "USFSENTIMENTLABEL", length = 50)
    @Enumerated(EnumType.STRING)
    private SentimentLabel usfsentimentlabel;
    
    @Column(name = "USFUSERID", length = 255)
    private String usfuserid;
    
    @Column(name = "USFSOURCE", length = 100)
    private String usfsource;
    
    @Column(name = "USFSTATUS", nullable = false, columnDefinition = "text[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private Set<String> usfstatus; // PENDING, PROCESSED, ARCHIVED
    
    @Column(name = "USFCREATEDAT", nullable = false)
    private LocalDateTime usfcreatedat = LocalDateTime.now();
    
    @Column(name = "USFUPDATEDAT")
    private LocalDateTime usfupdatedat;
    
    public enum FeedbackType {
        RATING, COMMENT, COMPLAINT, ESCALATION
    }
    
    public enum SentimentLabel {
        POSITIVE, NEUTRAL, NEGATIVE
    }
}
```

### 2. Crear Servicio de Feedback

```java
package com.codeflowx.govern.services.compliance;

import com.codeflowx.govern.entities.compliance.UserFeedback;
import com.codeflowx.govern.repositories.compliance.UserFeedbackRepository;
import com.codeflowx.govern.services.integration.SentimentAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserFeedbackService {
    
    private final UserFeedbackRepository repository;
    private final SentimentAnalysisService sentimentService;
    
    /**
     * Crea un nuevo feedback y analiza sentimiento
     */
    @Transactional
    public UserFeedback createFeedback(UserFeedback feedback) {
        log.info("Creando feedback para proyecto: {}", feedback.getIdxproject());
        
        feedback.setUsfstatus(Set.of("PENDING"));
        feedback.setUsfcreatedat(LocalDateTime.now());
        
        // Si hay texto, analizar sentimiento
        if (feedback.getUsffeedbacktext() != null && !feedback.getUsffeedbacktext().trim().isEmpty()) {
            analyzeSentiment(feedback);
        }
        
        // Si hay rating, calcular score
        if (feedback.getUsfrating() != null) {
            calculateRatingScore(feedback);
        }
        
        feedback.setUsfstatus(Set.of("PROCESSED"));
        feedback.setUsfupdatedat(LocalDateTime.now());
        
        return repository.save(feedback);
    }
    
    /**
     * Analiza sentimiento del texto usando servicio externo
     */
    private void analyzeSentiment(UserFeedback feedback) {
        try {
            SentimentAnalysisResult result = sentimentService.analyze(feedback.getUsffeedbacktext());
            
            feedback.setUsfsentimentscore(result.getScore());
            feedback.setUsfsentimentlabel(mapToSentimentLabel(result.getScore()));
            
            log.info("Sentimiento analizado: {} (score: {})", 
                feedback.getUsfsentimentlabel(), feedback.getUsfsentimentscore());
        } catch (Exception e) {
            log.error("Error analizando sentimiento", e);
            // Continuar sin sentimiento
        }
    }
    
    /**
     * Calcula score basado en rating
     */
    private void calculateRatingScore(UserFeedback feedback) {
        // Convertir rating 1-5 a score -1.0 a 1.0
        // 1 = -1.0, 2 = -0.5, 3 = 0.0, 4 = 0.5, 5 = 1.0
        double score = (feedback.getUsfrating() - 3.0) / 2.0;
        feedback.setUsfsentimentscore(BigDecimal.valueOf(score));
        
        if (feedback.getUsfsentimentlabel() == null) {
            feedback.setUsfsentimentlabel(mapToSentimentLabel(BigDecimal.valueOf(score)));
        }
    }
    
    private UserFeedback.SentimentLabel mapToSentimentLabel(BigDecimal score) {
        if (score.compareTo(BigDecimal.valueOf(0.1)) > 0) {
            return UserFeedback.SentimentLabel.POSITIVE;
        } else if (score.compareTo(BigDecimal.valueOf(-0.1)) < 0) {
            return UserFeedback.SentimentLabel.NEGATIVE;
        } else {
            return UserFeedback.SentimentLabel.NEUTRAL;
        }
    }
    
    /**
     * Calcula métrica de satisfacción para proyecto/modelo
     */
    public BigDecimal calculateSatisfactionScore(Long projectId, Long modelId, int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        
        List<UserFeedback> feedbacks = repository.findByProjectAndModelAndCreatedAtAfter(
            projectId, modelId, since);
        
        if (feedbacks.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        // Calcular promedio de scores
        double averageScore = feedbacks.stream()
            .filter(f -> f.getUsfsentimentscore() != null)
            .mapToDouble(f -> f.getUsfsentimentscore().doubleValue())
            .average()
            .orElse(0.0);
        
        // Normalizar a 0.0 - 1.0 (de -1.0 a 1.0)
        double normalizedScore = (averageScore + 1.0) / 2.0;
        
        return BigDecimal.valueOf(normalizedScore);
    }
    
    /**
     * Obtiene feedbacks por proyecto
     */
    public List<UserFeedback> getFeedbacksByProject(Long projectId) {
        return repository.findByIdxproject(projectId);
    }
}
```

### 3. Crear Servicio de Análisis de Sentimiento (Python)

```python
# En microservicio de análisis de sentimiento
from fastapi import FastAPI
from pydantic import BaseModel
from transformers import pipeline

app = FastAPI()

# Cargar modelo de análisis de sentimiento
sentiment_analyzer = pipeline("sentiment-analysis", 
                             model="nlptown/bert-base-multilingual-uncased-sentiment")

class SentimentRequest(BaseModel):
    text: str

class SentimentResponse(BaseModel):
    score: float  # -1.0 a 1.0
    label: str  # POSITIVE, NEUTRAL, NEGATIVE

@app.post("/api/sentiment/analyze", response_model=SentimentResponse)
async def analyze_sentiment(request: SentimentRequest):
    """
    Analiza el sentimiento del texto y retorna score y label
    """
    result = sentiment_analyzer(request.text)[0]
    
    # Convertir resultado del modelo a nuestro formato
    label_mapping = {
        "POSITIVE": "POSITIVE",
        "NEGATIVE": "NEGATIVE",
        "NEUTRAL": "NEUTRAL"
    }
    
    # Calcular score -1.0 a 1.0
    if result['label'] == 'POSITIVE':
        score = result['score']
    elif result['label'] == 'NEGATIVE':
        score = -result['score']
    else:
        score = 0.0
    
    return SentimentResponse(
        score=score,
        label=label_mapping.get(result['label'], "NEUTRAL")
    )
```

### 4. Integrar con PostMarketMonitoringService

```java
package com.codeflowx.govern.services.compliance;

// Modificar método checkProjectMetrics en PostMarketMonitoringService

public PostMarketResult checkProjectMetrics(Long projectId) {
    log.info("Verificando métricas post-market para proyecto: {}", projectId);
    
    PostMarketResult result = new PostMarketResult();
    
    // ... otras métricas ...
    
    // Calcular satisfacción de usuario
    BigDecimal satisfactionScore = userFeedbackService.calculateSatisfactionScore(
        projectId, null, 30); // Últimos 30 días
    
    result.setUserSatisfactionScore(satisfactionScore);
    
    // Detectar caída de satisfacción (threshold: 0.7)
    boolean satisfactionDrop = satisfactionScore.compareTo(BigDecimal.valueOf(0.7)) < 0;
    result.setUserSatisfactionDrop(satisfactionDrop);
    
    return result;
}
```

### 5. Integrar con Sistema de Tickets

```java
package com.codeflowx.govern.services.integration;

import com.codeflowx.govern.entities.compliance.UserFeedback;
import com.codeflowx.govern.services.compliance.UserFeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketSystemIntegrationService {
    
    private final TicketSystemClient ticketClient;
    private final UserFeedbackService feedbackService;
    
    /**
     * Sincroniza tickets del sistema externo como feedbacks
     * Se ejecuta cada hora
     */
    @Scheduled(fixedRate = 3600000) // 1 hora
    public void syncTicketsAsFeedback() {
        log.info("Sincronizando tickets como feedbacks");
        
        // Obtener tickets nuevos desde última sincronización
        List<Ticket> newTickets = ticketClient.getNewTickets();
        
        for (Ticket ticket : newTickets) {
            UserFeedback feedback = new UserFeedback();
            feedback.setIdxproject(ticket.getProjectId());
            feedback.setIdxmodel(ticket.getModelId());
            feedback.setUsffeedbacktype(mapTicketTypeToFeedbackType(ticket.getType()));
            feedback.setUsffeedbacktext(ticket.getDescription());
            feedback.setUsfsource("TICKET_SYSTEM");
            feedback.setUsfuserid(ticket.getUserId());
            
            feedbackService.createFeedback(feedback);
        }
        
        log.info("Sincronizados {} tickets como feedbacks", newTickets.size());
    }
    
    private UserFeedback.FeedbackType mapTicketTypeToFeedbackType(String ticketType) {
        return switch (ticketType) {
            case "COMPLAINT" -> UserFeedback.FeedbackType.COMPLAINT;
            case "ESCALATION" -> UserFeedback.FeedbackType.ESCALATION;
            default -> UserFeedback.FeedbackType.COMMENT;
        };
    }
}
```

---

## VALIDACIONES

1. ✅ Entidad `UserFeedback` creada
2. ✅ Integración con sistema de tickets funcional
3. ✅ Análisis de sentimiento implementado
4. ✅ Cálculo de métrica de satisfacción funcional
5. ✅ Integración con `PostMarketMonitoringService` completa

---

## NOTAS

- Usar prefijo `USF` para tabla
- PK autonumérica `IDXUSFFEEDBACK`
- Tercera forma normal
- KISS principle
- Análisis de sentimiento puede ser asíncrono para mejor performance

---

**Estado:** ✅ COMPLETADO

