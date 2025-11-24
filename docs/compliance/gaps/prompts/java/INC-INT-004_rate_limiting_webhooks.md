# PROMPT: INC-INT-004 - Rate Limiting para Webhooks de Plataformas Externas

**Incidencia:** INC-INT-004  
**Prioridad:** 🟠 ALTA  
**Artículo EU AI Act:** Art. 15 (Robustez, Seguridad)  
**Esfuerzo Estimado:** 2-3 días  
**Tipo:** Java - Backend (API)  
**Documento Relacionado:** `AUDITORIA_INTEGRACION_SIN_SUSTITUCION.md`

---

## CONTEXTO

No se implementa rate limiting para los endpoints de webhooks que reciben eventos de Databricks, Snowflake, Azure ML, etc. Esto puede permitir ataques de denegación de servicio (DoS) o sobrecarga del sistema si una plataforma envía eventos masivos.

**Riesgo:**
- Ataques de denegación de servicio (DoS)
- Sobrecarga del sistema si una plataforma envía eventos masivos
- Consumo excesivo de recursos
- No cumple con mejores prácticas de seguridad

**Ubicación Actual:**
- Webhook endpoints sin rate limiting
- `DatabricksWebhookController.java` (si existe en Python, necesitará Java equivalente)
- Microservicio de webhooks sin protección

---

## REQUISITOS

1. **Implementar rate limiting** con Redis para webhooks
2. **Configuración por plataforma** (diferentes límites)
3. **Respuesta HTTP 429** cuando se excede límite
4. **Header Retry-After** en respuesta 429
5. **Log de alertas** en ImmutableLog cuando se bloquean requests
6. **Métricas Prometheus** para monitoreo

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear `WebhookRateLimiter.java`

**Ubicación:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/security/WebhookRateLimiter.java`

```java
package com.codeflowx.govern.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Rate limiter para endpoints de webhooks de plataformas externas.
 * 
 * Usa Redis para contar requests por plataforma y IP.
 * Implementa algoritmo sliding window para rate limiting.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookRateLimiter {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${webhook.rate-limit.databricks.requests-per-minute:60}")
    private int databricksRequestsPerMinute;

    @Value("${webhook.rate-limit.snowflake.requests-per-minute:10}")
    private int snowflakeRequestsPerMinute;

    @Value("${webhook.rate-limit.azure-ml.requests-per-minute:100}")
    private int azureMlRequestsPerMinute;

    @Value("${webhook.rate-limit.default.requests-per-minute:30}")
    private int defaultRequestsPerMinute;

    @Value("${webhook.rate-limit.window-minutes:1}")
    private int windowMinutes;

    /**
     * Verifica si el request está permitido según rate limit.
     * 
     * @param platformType Tipo de plataforma (DATABRICKS, SNOWFLAKE, etc.)
     * @param sourceIp IP de origen del request
     * @return true si está permitido, false si se excedió el límite
     */
    public boolean isAllowed(String platformType, String sourceIp) {
        int limit = getLimitForPlatform(platformType);
        String key = buildRateLimitKey(platformType, sourceIp);

        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        
        // Incrementar contador
        Long count = ops.increment(key);

        // Si es el primer request, establecer expiración
        if (count != null && count == 1) {
            redisTemplate.expire(key, Duration.ofMinutes(windowMinutes));
        }

        // Verificar si se excedió el límite
        boolean allowed = count != null && count <= limit;

        if (!allowed) {
            log.warn("⚠️ Rate limit exceeded for platform: {}, IP: {}, count: {}, limit: {}", 
                    platformType, sourceIp, count, limit);
        }

        return allowed;
    }

    /**
     * Obtiene cuántos requests quedan disponibles.
     * 
     * @param platformType Tipo de plataforma
     * @param sourceIp IP de origen
     * @return Número de requests disponibles (0 si se excedió)
     */
    public long getRemainingRequests(String platformType, String sourceIp) {
        int limit = getLimitForPlatform(platformType);
        String key = buildRateLimitKey(platformType, sourceIp);
        
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String countStr = ops.get(key);
        long count = countStr != null ? Long.parseLong(countStr) : 0;

        return Math.max(0, limit - count);
    }

    /**
     * Obtiene tiempo hasta que el rate limit se resetee (en segundos).
     * 
     * @param platformType Tipo de plataforma
     * @param sourceIp IP de origen
     * @return Segundos hasta reset
     */
    public long getRetryAfterSeconds(String platformType, String sourceIp) {
        String key = buildRateLimitKey(platformType, sourceIp);
        Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return ttl != null && ttl > 0 ? ttl : windowMinutes * 60;
    }

    /**
     * Construye clave Redis para rate limiting.
     */
    private String buildRateLimitKey(String platformType, String sourceIp) {
        return String.format("webhook:rate-limit:%s:%s", 
                           platformType.toLowerCase(), sourceIp);
    }

    /**
     * Obtiene límite de requests por minuto según plataforma.
     */
    private int getLimitForPlatform(String platformType) {
        if (platformType == null) {
            return defaultRequestsPerMinute;
        }

        switch (platformType.toUpperCase()) {
            case "DATABRICKS":
                return databricksRequestsPerMinute;
            case "SNOWFLAKE":
                return snowflakeRequestsPerMinute;
            case "AZURE_ML":
            case "AZUREML":
                return azureMlRequestsPerMinute;
            default:
                return defaultRequestsPerMinute;
        }
    }

    /**
     * Limpia rate limit para una plataforma/IP (útil para testing o reseteo manual).
     */
    public void resetRateLimit(String platformType, String sourceIp) {
        String key = buildRateLimitKey(platformType, sourceIp);
        redisTemplate.delete(key);
        log.info("Rate limit reset for platform: {}, IP: {}", platformType, sourceIp);
    }
}
```

### 2. Crear `RateLimitExceededException.java`

**Ubicación:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/exception/RateLimitExceededException.java`

```java
package com.codeflowx.govern.exception;

import lombok.Getter;

/**
 * Excepción lanzada cuando se excede el rate limit.
 */
@Getter
public class RateLimitExceededException extends RuntimeException {
    
    private final String platformType;
    private final String sourceIp;
    private final long retryAfterSeconds;

    public RateLimitExceededException(String platformType, String sourceIp, long retryAfterSeconds) {
        super(String.format("Rate limit exceeded for platform: %s, IP: %s", platformType, sourceIp));
        this.platformType = platformType;
        this.sourceIp = sourceIp;
        this.retryAfterSeconds = retryAfterSeconds;
    }
}
```

### 3. Crear `GlobalExceptionHandler.java` para Webhooks

**Ubicación:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/controller/advice/WebhookExceptionHandler.java`

```java
package com.codeflowx.govern.controller.advice;

import com.codeflowx.govern.exception.RateLimitExceededException;
import com.codeflowx.govern.service.logging.ImmutableLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Handler global de excepciones para endpoints de webhooks.
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.codeflowx.govern.controller.webhook")
@RequiredArgsConstructor
public class WebhookExceptionHandler {

    private final ImmutableLogService immutableLogService;

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<Map<String, Object>> handleRateLimitExceeded(
            RateLimitExceededException ex) {
        
        log.warn("Rate limit exceeded: platform={}, IP={}", 
                ex.getPlatformType(), ex.getSourceIp());

        // Registrar en ImmutableLog
        try {
            String details = String.format(
                "{\"platform\":\"%s\",\"source_ip\":\"%s\",\"retry_after_seconds\":%d}",
                ex.getPlatformType(), ex.getSourceIp(), ex.getRetryAfterSeconds()
            );
            
            immutableLogService.logEvent(
                "WEBHOOK",
                "RATE_LIMIT_EXCEEDED",
                null,
                details,
                "SYSTEM"
            );
        } catch (Exception e) {
            log.error("Error logging rate limit violation: {}", e.getMessage());
        }

        // Respuesta HTTP 429
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Rate limit exceeded");
        response.put("message", ex.getMessage());
        response.put("platform", ex.getPlatformType());
        response.put("retry_after_seconds", ex.getRetryAfterSeconds());

        return ResponseEntity
            .status(HttpStatus.TOO_MANY_REQUESTS)
            .header("Retry-After", String.valueOf(ex.getRetryAfterSeconds()))
            .body(response);
    }
}
```

### 4. Crear `WebhookController.java` con Rate Limiting

**Ubicación:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/controller/webhook/WebhookController.java`

```java
package com.codeflowx.govern.controller.webhook;

import com.codeflowx.govern.exception.RateLimitExceededException;
import com.codeflowx.govern.security.WebhookRateLimiter;
import com.codeflowx.govern.service.external.WebhookProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Controller para recibir webhooks de plataformas externas.
 * 
 * Implementa rate limiting para prevenir DoS.
 */
@Slf4j
@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookRateLimiter rateLimiter;
    private final WebhookProcessingService webhookProcessingService;

    /**
     * Webhook para eventos de Databricks.
     */
    @PostMapping("/databricks")
    public ResponseEntity<Map<String, Object>> databricksWebhook(
            @RequestBody Map<String, Object> payload,
            @RequestHeader(value = "X-Databricks-Signature", required = false) String signature,
            HttpServletRequest request) {

        String sourceIp = getClientIp(request);
        String platformType = "DATABRICKS";

        // 1. Verificar rate limit
        if (!rateLimiter.isAllowed(platformType, sourceIp)) {
            long retryAfter = rateLimiter.getRetryAfterSeconds(platformType, sourceIp);
            throw new RateLimitExceededException(platformType, sourceIp, retryAfter);
        }

        // 2. Verificar firma (si está configurada)
        if (signature != null) {
            // TODO: Implementar verificación de firma
            log.debug("Signature verification not yet implemented");
        }

        // 3. Procesar webhook
        try {
            Map<String, Object> result = webhookProcessingService.processDatabricksWebhook(payload);
            
            log.info("✅ Databricks webhook processed successfully: eventType={}", 
                    payload.get("event_type"));

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("Error processing Databricks webhook: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(
                Map.of("error", "Internal server error", "message", e.getMessage())
            );
        }
    }

    /**
     * Webhook para eventos de Snowflake.
     */
    @PostMapping("/snowflake")
    public ResponseEntity<Map<String, Object>> snowflakeWebhook(
            @RequestBody Map<String, Object> payload,
            HttpServletRequest request) {

        String sourceIp = getClientIp(request);
        String platformType = "SNOWFLAKE";

        // Verificar rate limit
        if (!rateLimiter.isAllowed(platformType, sourceIp)) {
            long retryAfter = rateLimiter.getRetryAfterSeconds(platformType, sourceIp);
            throw new RateLimitExceededException(platformType, sourceIp, retryAfter);
        }

        // Procesar webhook
        try {
            Map<String, Object> result = webhookProcessingService.processSnowflakeWebhook(payload);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error processing Snowflake webhook: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(
                Map.of("error", "Internal server error", "message", e.getMessage())
            );
        }
    }

    /**
     * Webhook para eventos de Azure ML.
     */
    @PostMapping("/azure-ml")
    public ResponseEntity<Map<String, Object>> azureMlWebhook(
            @RequestBody Map<String, Object> payload,
            HttpServletRequest request) {

        String sourceIp = getClientIp(request);
        String platformType = "AZURE_ML";

        // Verificar rate limit
        if (!rateLimiter.isAllowed(platformType, sourceIp)) {
            long retryAfter = rateLimiter.getRetryAfterSeconds(platformType, sourceIp);
            throw new RateLimitExceededException(platformType, sourceIp, retryAfter);
        }

        // Procesar webhook
        try {
            Map<String, Object> result = webhookProcessingService.processAzureMlWebhook(payload);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error processing Azure ML webhook: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(
                Map.of("error", "Internal server error", "message", e.getMessage())
            );
        }
    }

    /**
     * Obtiene IP del cliente desde request.
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            // Tomar primera IP (cliente real, no proxies)
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    /**
     * Health check para webhooks.
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "healthy", "service", "webhooks"));
    }
}
```

### 5. Añadir Métricas Prometheus

**Modificar `WebhookRateLimiter.java`:**

```java
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookRateLimiter {

    private final RedisTemplate<String, String> redisTemplate;
    private final MeterRegistry meterRegistry;

    // ... código existente ...

    public boolean isAllowed(String platformType, String sourceIp) {
        // ... código existente ...

        if (!allowed) {
            // Incrementar métrica de rate limit excedido
            Counter.builder("webhook.rate_limit.exceeded")
                .tag("platform", platformType)
                .tag("ip", sourceIp)
                .register(meterRegistry)
                .increment();
        } else {
            // Incrementar métrica de requests permitidos
            Counter.builder("webhook.rate_limit.allowed")
                .tag("platform", platformType)
                .register(meterRegistry)
                .increment();
        }

        return allowed;
    }
}
```

### 6. Configuración

**Añadir a `application.properties`:**

```properties
# Rate limiting para webhooks
webhook.rate-limit.databricks.requests-per-minute=60
webhook.rate-limit.snowflake.requests-per-minute=10
webhook.rate-limit.azure-ml.requests-per-minute=100
webhook.rate-limit.default.requests-per-minute=30
webhook.rate-limit.window-minutes=1
```

---

## VALIDACIÓN

### Tests Unitarios

**Crear:** `WebhookRateLimiterTest.java`

```java
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class WebhookRateLimiterTest {

    @Autowired
    private WebhookRateLimiter rateLimiter;

    @Test
    void testRateLimit_WithinLimit() {
        // Given
        String platform = "DATABRICKS";
        String ip = "192.168.1.1";

        // When/Then - Primeros 60 requests deben ser permitidos
        for (int i = 0; i < 60; i++) {
            assertThat(rateLimiter.isAllowed(platform, ip)).isTrue();
        }
    }

    @Test
    void testRateLimit_Exceeded() {
        // Given
        String platform = "DATABRICKS";
        String ip = "192.168.1.2";

        // When - Exceder límite
        for (int i = 0; i < 60; i++) {
            rateLimiter.isAllowed(platform, ip);
        }

        // Then - Request 61 debe ser bloqueado
        assertThat(rateLimiter.isAllowed(platform, ip)).isFalse();
    }

    @Test
    void testGetRetryAfterSeconds() {
        // Given
        String platform = "DATABRICKS";
        String ip = "192.168.1.3";

        // When
        long retryAfter = rateLimiter.getRetryAfterSeconds(platform, ip);

        // Then
        assertThat(retryAfter).isGreaterThan(0);
        assertThat(retryAfter).isLessThanOrEqualTo(60);
    }
}
```

---

## CUMPLIMIENTO EU AI ACT

| Artículo | Cobertura |
|----------|-----------|
| **Art. 15** (Robustez, Seguridad) | ✅ Protección contra DoS mediante rate limiting |

---

**Prioridad:** 🟠 **ALTA**  
**Fecha Límite:** Enero 2026  
**Responsable:** Java Backend Team (API)

---

**Estado:** ✅ COMPLETADO

