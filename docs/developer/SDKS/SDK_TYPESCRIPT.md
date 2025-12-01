# SDK TypeScript - Guía Completa

SDK oficial TypeScript para integración con CodeflowX AI OS.

---

## 📦 Instalación

```bash
npm install codeflowx-sdk-typescript
```

**Requisitos:** Node.js 16+, TypeScript 5.0+

---

## 🚀 Inicio Rápido

```typescript
import { AgentClient } from 'codeflowx-sdk-typescript';

// Inicializar cliente
const client = new AgentClient('cfx_sk_live_abc123...');

// Registrar agente
const registration = await client.registerAgent(
    'mi-agente',
    'custom',
    'workspace-uuid',
    { language: 'es' }
);

// Usar agent_token
const agentClient = new AgentClient(registration.agent_token);
```

---

## 📚 API Reference

### AgentClient

#### Constructor

```typescript
new AgentClient(
    apiKey: string,
    baseUrl?: string,
    retryConfig?: RetryConfig,
    circuitBreakerConfig?: CircuitBreakerConfig,
    rateLimiterConfig?: RateLimiterConfig,
    cacheConfig?: CacheConfig,
    offlineQueue?: OfflineQueue,
    piiFilter?: PIIFilter,
    metricsCollector?: MetricsCollector
)
```

#### Métodos Principales

##### Registro y Onboarding

```typescript
registerAgent(
    agentName: string,
    agentType: string,
    workspaceUuid: string,
    capabilities: Record<string, any>,
    callbackUrl?: string,
    endpoint?: string
): Promise<AgentRegistration>
```

**Ejemplo:**
```typescript
const registration = await client.registerAgent(
    'customer-service-agent',
    'custom',
    'workspace-uuid',
    {
        language: 'es',
        domains: ['customer_service']
    },
    'https://mi-app.com/webhooks/codeflowx'
);

const agentId = registration.agent_id;
const agentToken = registration.agent_token;
```

---

##### Telemetría

```typescript
sendTelemetry(
    componentUuid: string,
    eventType: string,
    payload: Record<string, any>,
    piiFilter?: PIIFilter,
    compress?: boolean
): Promise<void>
```

**Ejemplo:**
```typescript
await agentClient.sendTelemetry(
    agentId,
    'AGENT_INVOCATION',
    {
        prompt: 'Hello',
        response: 'Hi there',
        latency_ms: 245,
        tokens_used: 150
    }
);
```

**Batch:**
```typescript
const events: TelemetryEvent[] = [
    {
        component_uuid: agentId,
        event_type: 'AGENT_INVOCATION',
        payload: { prompt: 'Hello', response: 'Hi' }
    }
];
await agentClient.sendTelemetryBatch(events);
```

---

##### Memoria Centralizada

```typescript
// Obtener valor
const value = await agentClient.memoryGet(
    agentId,
    'conversation',
    'user_preferences'
);

// Guardar valor
await agentClient.memorySet(
    agentId,
    'conversation',
    'user_preferences',
    { theme: 'dark', language: 'es' },
    30  // expires_in_days (opcional)
);

// Eliminar valor
await agentClient.memoryDelete(agentId, 'conversation', 'user_preferences');

// Batch
await agentClient.memoryBatchSet(
    agentId,
    'conversation',
    {
        key1: 'value1',
        key2: 'value2'
    }
);

// Snapshots
const snapshot = await agentClient.memoryCreateSnapshot(agentId);
await agentClient.memoryRestoreSnapshot(agentId, snapshot.snapshot_id);
```

---

##### Supervisor

```typescript
// Polling de estado
const status = await agentClient.supervisorGetStatus(agentId);
// status = {
//     state: 'ACTIVE' | 'PAUSED' | 'TERMINATED' | 'ERROR',
//     last_update: '2025-01-01T00:00:00Z',
//     messages: ['...'],
//     actions_required: ['...']
// }

// Manejar webhook
await agentClient.supervisorHandleWebhook(
    'PAUSE',  // PAUSE, RESUME, TERMINATE
    { reason: 'Maintenance' }
);
```

---

##### Compliance

```typescript
// Registrar evidencia inmutable
const logResult = await agentClient.auditLogImmutable(
    agentId,
    'INVOKE',
    'user123',
    {
        decision: 'APPROVED',
        reason: 'Policy check passed'
    }
);

// Verificar evidencia
const verification = await agentClient.verifyEvidence(logResult.log_uuid);
```

---

##### Policy Runtime

```typescript
// Verificar políticas
const result = await agentClient.policyCheck(
    agentId,
    'INVOKE',
    {
        prompt: 'Hello',
        user_id: 'user123'
    }
);

if (!result.allowed) {
    throw new Error(`Política violada: ${result.reason}`);
}

// Pre-check
const preCheck = await agentClient.policyPreCheck(
    agentId,
    'INVOKE',
    { prompt: 'Hello' }
);
```

---

##### Health Checks

```typescript
// Health check
const health = await agentClient.health();
// health = {
//     status: 'healthy' | 'degraded' | 'unhealthy',
//     version: '1.0.0',
//     uptime_seconds: 3600
// }

// Readiness (K8s)
const isReady = await agentClient.ready();  // boolean
```

---

## ⚙️ Configuración

### Retry

```typescript
import { AgentClient, RetryConfig } from 'codeflowx-sdk-typescript';

const retryConfig = new RetryConfig();
retryConfig.maxAttempts = 5;
retryConfig.initialDelayMs = 1000;
retryConfig.maxDelayMs = 10000;

const client = new AgentClient('...', '...', retryConfig);
```

### Caché

```typescript
import { AgentClient, CacheConfig } from 'codeflowx-sdk-typescript';

const cacheConfig = new CacheConfig();
cacheConfig.enabled = true;
cacheConfig.ttlSeconds = 300;
cacheConfig.cachePolicyChecks = true;

const client = new AgentClient('...', '...', undefined, undefined, undefined, cacheConfig);
```

### Filtrado PII

```typescript
import { AgentClient, PIIFilter } from 'codeflowx-sdk-typescript';

const piiFilter = new PIIFilter();
piiFilter.enabled = true;
piiFilter.redactionMode = 'MASK';
piiFilter.patterns = ['email', 'phone', 'ssn'];

const client = new AgentClient('...', '...', undefined, undefined, undefined, undefined, undefined, piiFilter);
```

---

## 🎨 Integración con Frameworks

### Express.js

```typescript
import express from 'express';
import { AgentClient } from 'codeflowx-sdk-typescript';

const app = express();
const client = AgentClient.fromEnv();
const agentId = 'agent-uuid';

app.post('/agent/invoke', async (req, res) => {
    const { prompt } = req.body;

    // Verificar política
    const policy = await client.policyCheck(agentId, 'INVOKE', { prompt });
    if (!policy.allowed) {
        return res.status(403).json({ error: policy.reason });
    }

    // Ejecutar agente
    const response = await myAgent.invoke(prompt);

    // Enviar telemetría
    await client.sendTelemetry(agentId, 'AGENT_INVOCATION', {
        prompt,
        response,
        latency_ms: 200
    });

    res.json({ response });
});
```

### Next.js

```typescript
// app/api/agent/route.ts
import { NextRequest, NextResponse } from 'next/server';
import { AgentClient } from 'codeflowx-sdk-typescript';

const client = AgentClient.fromEnv();
const agentId = 'agent-uuid';

export async function POST(request: NextRequest) {
    const { prompt } = await request.json();

    const response = await myAgent.invoke(prompt);

    await client.sendTelemetry(agentId, 'AGENT_INVOCATION', {
        prompt,
        response
    });

    return NextResponse.json({ response });
}
```

---

## 📊 Ejemplos Completos

Ver [Ejemplos TypeScript](EJEMPLOS_TYPESCRIPT.md) para más casos de uso.

---

## 🔗 Enlaces

- [API Reference Completa](API_REFERENCE.md)
- [Troubleshooting](../TROUBLESHOOTING.md)
- [FAQ](../FAQ.md)
