# SDK Java - Guía Completa

SDK oficial Java para integración con CodeflowX AI OS.

---

## 📦 Instalación

### Maven

```xml
<dependency>
    <groupId>com.codeflowx</groupId>
    <artifactId>codeflowx-sdk-java</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```gradle
implementation 'com.codeflowx:codeflowx-sdk-java:1.0.0'
```

**Requisitos:** Java 11+

---

## 🚀 Inicio Rápido

```java
import com.codeflowx.sdk.AgentClient;
import com.codeflowx.sdk.model.AgentRegistration;
import java.util.*;

// Inicializar cliente
AgentClient client = new AgentClient("cfx_sk_live_abc123...");

// Registrar agente
Map<String, Object> capabilities = new HashMap<>();
capabilities.put("language", "es");
capabilities.put("domains", Arrays.asList("customer_service"));

AgentRegistration registration = client.registerAgent(
    "mi-agente",
    "custom",
    "workspace-uuid",
    capabilities,
    null,
    null
);

// Usar agent_token
AgentClient agentClient = new AgentClient(registration.getAgentToken());
```

---

## 📚 API Reference

### AgentClient

#### Constructor

```java
AgentClient(String apiKey)
AgentClient(String apiKey, String baseUrl)
AgentClient(
    String apiKey,
    String baseUrl,
    RetryConfig retryConfig,
    CircuitBreakerConfig circuitBreakerConfig,
    RateLimiterConfig rateLimiterConfig,
    CacheConfig cacheConfig,
    OfflineQueue offlineQueue,
    PIIFilter piiFilter,
    MetricsCollector metricsCollector
)
```

#### Métodos Principales

##### Registro y Onboarding

```java
AgentRegistration registerAgent(
    String agentName,
    String agentType,
    String workspaceUuid,
    Map<String, Object> capabilities,
    String callbackUrl,
    String endpoint
) throws CodeflowXException
```

**Ejemplo:**
```java
Map<String, Object> capabilities = new HashMap<>();
capabilities.put("language", "es");
capabilities.put("domains", Arrays.asList("customer_service"));

AgentRegistration registration = client.registerAgent(
    "customer-service-agent",
    "custom",
    "workspace-uuid",
    capabilities,
    "https://mi-app.com/webhooks/codeflowx",
    null
);

String agentId = registration.getAgentId();
String agentToken = registration.getAgentToken();
```

---

##### Telemetría

```java
void sendTelemetry(
    String componentUuid,
    String eventType,
    Map<String, Object> payload,
    PIIFilter customPiiFilter,
    boolean compress
) throws CodeflowXException
```

**Ejemplo:**
```java
Map<String, Object> payload = new HashMap<>();
payload.put("prompt", "Hello");
payload.put("response", "Hi there");
payload.put("latency_ms", 245);

agentClient.sendTelemetry(
    agentId,
    "AGENT_INVOCATION",
    payload,
    null,
    true
);
```

**Batch:**
```java
List<TelemetryEvent> events = Arrays.asList(
    new TelemetryEvent(agentId, "AGENT_INVOCATION", payload1),
    new TelemetryEvent(agentId, "AGENT_COMPLETION", payload2)
);
agentClient.sendTelemetryBatch(events);
```

---

##### Memoria Centralizada

```java
// Obtener valor
Object value = agentClient.memoryGet(agentId, "conversation", "user_preferences");

// Guardar valor
Map<String, Object> preferences = new HashMap<>();
preferences.put("theme", "dark");
preferences.put("language", "es");

agentClient.memorySet(
    agentId,
    "conversation",
    "user_preferences",
    preferences,
    30  // expires_in_days (opcional)
);

// Eliminar valor
agentClient.memoryDelete(agentId, "conversation", "user_preferences");

// Batch
Map<String, Object> entries = new HashMap<>();
entries.put("key1", "value1");
entries.put("key2", "value2");
agentClient.memoryBatchSet(agentId, "conversation", entries);

// Snapshots
Map<String, Object> snapshot = agentClient.memoryCreateSnapshot(agentId);
agentClient.memoryRestoreSnapshot(agentId, (String) snapshot.get("snapshot_id"));
```

---

##### Supervisor

```java
// Polling de estado
SupervisorStatus status = agentClient.supervisorGetStatus(agentId);
// status.getState() = "ACTIVE" | "PAUSED" | "TERMINATED" | "ERROR"

// Manejar webhook
Map<String, Object> payload = new HashMap<>();
payload.put("reason", "Maintenance");
agentClient.supervisorHandleWebhook("PAUSE", payload);
```

---

##### Compliance

```java
// Registrar evidencia inmutable
Map<String, Object> metadata = new HashMap<>();
metadata.put("decision", "APPROVED");
metadata.put("reason", "Policy check passed");

Map<String, Object> logResult = agentClient.auditLogImmutable(
    agentId,
    "INVOKE",
    "user123",
    metadata
);

// Verificar evidencia
Map<String, Object> verification = agentClient.verifyEvidence(
    (String) logResult.get("log_uuid")
);
```

---

##### Policy Runtime

```java
// Verificar políticas
Map<String, Object> context = new HashMap<>();
context.put("prompt", "Hello");
context.put("user_id", "user123");

PolicyCheckResult result = agentClient.policyCheck(
    agentId,
    "INVOKE",
    context
);

if (!result.isAllowed()) {
    throw new RuntimeException("Política violada: " + result.getReason());
}

// Pre-check
PolicyCheckResult preCheck = agentClient.policyPreCheck(
    agentId,
    "INVOKE",
    context
);
```

---

##### Health Checks

```java
// Health check
HealthResponse health = agentClient.health();
// health.getStatus() = "healthy" | "degraded" | "unhealthy"

// Readiness (K8s)
boolean isReady = agentClient.ready();
```

---

## ⚙️ Configuración

### Retry

```java
import com.codeflowx.sdk.resilience.RetryConfig;

RetryConfig retryConfig = new RetryConfig();
retryConfig.setMaxAttempts(5);
retryConfig.setInitialDelayMs(1000);
retryConfig.setMaxDelayMs(10000);

AgentClient client = new AgentClient(
    "api-key",
    "https://gateway.codeflowx.ai",
    retryConfig,
    null,
    null,
    null,
    null,
    null,
    null
);
```

### Caché

```java
import com.codeflowx.sdk.cache.CacheConfig;

CacheConfig cacheConfig = new CacheConfig();
cacheConfig.setEnabled(true);
cacheConfig.setTtlSeconds(300);
cacheConfig.setCachePolicyChecks(true);

AgentClient client = new AgentClient(
    "api-key",
    "https://gateway.codeflowx.ai",
    null,
    null,
    null,
    cacheConfig,
    null,
    null,
    null
);
```

---

## 🎨 Integración con Spring Boot

### Configuración

```java
@Configuration
public class CodeflowXConfig {

    @Bean
    @ConditionalOnProperty(name = "codeflowx.enabled", havingValue = "true")
    public AgentClient agentClient(
            @Value("${codeflowx.api-key}") String apiKey,
            @Value("${codeflowx.base-url:https://gateway.codeflowx.ai}") String baseUrl
    ) {
        return new AgentClient(apiKey, baseUrl);
    }
}
```

### application.yml

```yaml
codeflowx:
  enabled: true
  api-key: ${CODEFLOWX_API_KEY}
  base-url: https://gateway.codeflowx.ai
```

### Uso en Service

```java
@Service
public class MyAgentService {

    @Autowired
    private AgentClient agentClient;

    public String invokeAgent(String prompt, String agentId) {
        // Verificar política
        Map<String, Object> context = new HashMap<>();
        context.put("prompt", prompt);

        PolicyCheckResult result = agentClient.policyCheck(
            agentId,
            "INVOKE",
            context
        );

        if (!result.isAllowed()) {
            throw new RuntimeException("Política violada");
        }

        // Ejecutar agente
        String response = myAgent.invoke(prompt);

        // Enviar telemetría
        Map<String, Object> payload = new HashMap<>();
        payload.put("prompt", prompt);
        payload.put("response", response);

        agentClient.sendTelemetry(agentId, "AGENT_INVOCATION", payload, null, true);

        return response;
    }
}
```

---

## 📊 Ejemplos Completos

Ver [Ejemplos Java](EJEMPLOS_JAVA.md) para más casos de uso.

---

## 🔗 Enlaces

- [API Reference Completa](API_REFERENCE.md)
- [Troubleshooting](../TROUBLESHOOTING.md)
- [FAQ](../FAQ.md)
