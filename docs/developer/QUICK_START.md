# Quick Start Guide - CodeflowX SDK

Empieza a usar CodeflowX en 5 minutos. Esta guía te mostrará cómo integrar agentes externos con CodeflowX AI OS.

---

## 🎯 Objetivo

Al final de esta guía podrás:
- ✅ Registrar un agente externo en CodeflowX
- ✅ Enviar telemetría desde tu aplicación
- ✅ Usar memoria centralizada
- ✅ Verificar políticas antes de ejecutar

---

## 📋 Prerrequisitos

- Cuenta en CodeflowX (obtén API key desde Developer Console)
- Python 3.8+ / Node.js 16+ / Java 11+ (según SDK)
- Aplicación con agente existente (opcional)

---

## 🚀 Paso 1: Instalar SDK

### Python
```bash
pip install codeflowx-sdk-python
```

### TypeScript
```bash
npm install codeflowx-sdk-typescript
```

### Java (Maven)
```xml
<dependency>
    <groupId>com.codeflowx</groupId>
    <artifactId>codeflowx-sdk-java</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

## 🔑 Paso 2: Obtener API Key

1. Accede a **Developer Console** en CodeflowX
2. Ve a la pestaña **"API Keys"**
3. Haz clic en **"Generar API Key"**
4. Copia la API key (solo se muestra una vez)

**Ejemplo:** `cfx_sk_live_abc123def456...`

---

## 📝 Paso 3: Registrar Agente

### Python
```python
from codeflowx_sdk import AgentClient

# Inicializar cliente con tu API key
client = AgentClient(api_key="cfx_sk_live_abc123...")

# Registrar agente (solo primera vez)
registration = client.register_agent(
    agent_name="mi-agente-customer-service",
    agent_type="custom",
    workspace_uuid="tu-workspace-uuid",
    capabilities={
        "language": "es",
        "domains": ["customer_service"]
    },
    callback_url="https://mi-app.com/webhooks/codeflowx"  # Opcional
)

print(f"Agent ID: {registration['agent_id']}")
print(f"Agent Token: {registration['agent_token']}")

# Guarda el agent_token para uso futuro
agent_token = registration['agent_token']
agent_id = registration['agent_id']
```

### TypeScript
```typescript
import { AgentClient } from 'codeflowx-sdk-typescript';

const client = new AgentClient('cfx_sk_live_abc123...');

const registration = await client.registerAgent(
    'mi-agente-customer-service',
    'custom',
    'tu-workspace-uuid',
    {
        language: 'es',
        domains: ['customer_service']
    },
    'https://mi-app.com/webhooks/codeflowx'
);

console.log('Agent ID:', registration.agent_id);
console.log('Agent Token:', registration.agent_token);

const agentToken = registration.agent_token;
const agentId = registration.agent_id;
```

### Java
```java
import com.codeflowx.sdk.AgentClient;
import com.codeflowx.sdk.model.AgentRegistration;
import java.util.*;

AgentClient client = new AgentClient("cfx_sk_live_abc123...");

Map<String, Object> capabilities = new HashMap<>();
capabilities.put("language", "es");
capabilities.put("domains", Arrays.asList("customer_service"));

AgentRegistration registration = client.registerAgent(
    "mi-agente-customer-service",
    "custom",
    "tu-workspace-uuid",
    capabilities,
    "https://mi-app.com/webhooks/codeflowx",
    null
);

System.out.println("Agent ID: " + registration.getAgentId());
System.out.println("Agent Token: " + registration.getAgentToken());

String agentToken = registration.getAgentToken();
String agentId = registration.getAgentId();
```

---

## 🔄 Paso 4: Usar Agent Token

Después del registro, usa el `agent_token` (no la API key original) para todas las operaciones:

### Python
```python
# Inicializar cliente con agent_token
agent_client = AgentClient(api_key=agent_token)
```

### TypeScript
```typescript
const agentClient = new AgentClient(agentToken);
```

### Java
```java
AgentClient agentClient = new AgentClient(agentToken);
```

---

## 📊 Paso 5: Enviar Telemetría

Cada vez que tu agente procesa una solicitud, envía telemetría:

### Python
```python
def invoke_agent(prompt: str):
    # Tu lógica de agente aquí
    response = my_agent.invoke(prompt)
    latency_ms = 245

    # Enviar telemetría
    agent_client.send_telemetry(
        component_uuid=agent_id,
        event_type="AGENT_INVOCATION",
        payload={
            "prompt": prompt,
            "response": response,
            "latency_ms": latency_ms,
            "tokens_used": 150
        }
    )

    return response
```

### TypeScript
```typescript
async function invokeAgent(prompt: string) {
    // Tu lógica de agente
    const response = await myAgent.invoke(prompt);
    const latencyMs = 245;

    // Enviar telemetría
    await agentClient.sendTelemetry(
        agentId,
        'AGENT_INVOCATION',
        {
            prompt,
            response,
            latency_ms: latencyMs,
            tokens_used: 150
        }
    );

    return response;
}
```

### Java
```java
public String invokeAgent(String prompt) {
    // Tu lógica de agente
    String response = myAgent.invoke(prompt);
    int latencyMs = 245;

    // Enviar telemetría
    Map<String, Object> payload = new HashMap<>();
    payload.put("prompt", prompt);
    payload.put("response", response);
    payload.put("latency_ms", latencyMs);
    payload.put("tokens_used", 150);

    agentClient.sendTelemetry(
        agentId,
        "AGENT_INVOCATION",
        payload,
        null,
        true
    );

    return response;
}
```

---

## 💾 Paso 6: Usar Memoria Centralizada

Guarda y recupera contexto compartido:

### Python
```python
# Guardar contexto
agent_client.memory_set(
    agent_id=agent_id,
    namespace="conversation",
    key="user_preferences",
    value={"theme": "dark", "language": "es"}
)

# Recuperar contexto
preferences = agent_client.memory_get(
    agent_id=agent_id,
    namespace="conversation",
    key="user_preferences"
)
```

### TypeScript
```typescript
// Guardar
await agentClient.memorySet(
    agentId,
    'conversation',
    'user_preferences',
    { theme: 'dark', language: 'es' }
);

// Recuperar
const preferences = await agentClient.memoryGet(
    agentId,
    'conversation',
    'user_preferences'
);
```

### Java
```java
// Guardar
Map<String, Object> preferences = new HashMap<>();
preferences.put("theme", "dark");
preferences.put("language", "es");

agentClient.memorySet(
    agentId,
    "conversation",
    "user_preferences",
    preferences,
    null
);

// Recuperar
Object prefs = agentClient.memoryGet(
    agentId,
    "conversation",
    "user_preferences"
);
```

---

## 🛡️ Paso 7: Verificar Políticas

Antes de ejecutar acciones críticas, verifica políticas:

### Python
```python
def invoke_with_policy_check(prompt: str):
    # Verificar política
    result = agent_client.policy_check(
        component_uuid=agent_id,
        action="INVOKE",
        context={"prompt": prompt, "user_id": "user123"}
    )

    if not result["allowed"]:
        raise Exception(f"Política violada: {result['reason']}")

    # Ejecutar agente
    return my_agent.invoke(prompt)
```

### TypeScript
```typescript
async function invokeWithPolicyCheck(prompt: string) {
    const result = await agentClient.policyCheck(
        agentId,
        'INVOKE',
        { prompt, user_id: 'user123' }
    );

    if (!result.allowed) {
        throw new Error(`Política violada: ${result.reason}`);
    }

    return await myAgent.invoke(prompt);
}
```

### Java
```java
public String invokeWithPolicyCheck(String prompt) {
    Map<String, Object> context = new HashMap<>();
    context.put("prompt", prompt);
    context.put("user_id", "user123");

    PolicyCheckResult result = agentClient.policyCheck(
        agentId,
        "INVOKE",
        context
    );

    if (!result.isAllowed()) {
        throw new RuntimeException("Política violada: " + result.getReason());
    }

    return myAgent.invoke(prompt);
}
```

---

## ✅ Ejemplo Completo

### Python
```python
from codeflowx_sdk import AgentClient

# 1. Inicializar
client = AgentClient(api_key="cfx_sk_live_abc123...")

# 2. Registrar (solo primera vez)
registration = client.register_agent(
    agent_name="mi-agente",
    agent_type="custom",
    workspace_uuid="workspace-uuid",
    capabilities={"language": "es"}
)

agent_token = registration['agent_token']
agent_id = registration['agent_id']

# 3. Usar agente
agent_client = AgentClient(api_key=agent_token)

def process_request(user_input: str):
    # Verificar política
    policy = agent_client.policy_check(
        component_uuid=agent_id,
        action="INVOKE",
        context={"prompt": user_input}
    )

    if not policy["allowed"]:
        return "Lo siento, no puedo procesar esa solicitud."

    # Ejecutar agente
    response = my_agent.invoke(user_input)

    # Guardar contexto
    agent_client.memory_set(
        agent_id=agent_id,
        namespace="conversation",
        key="last_input",
        value=user_input
    )

    # Enviar telemetría
    agent_client.send_telemetry(
        component_uuid=agent_id,
        event_type="AGENT_INVOCATION",
        payload={
            "prompt": user_input,
            "response": response,
            "latency_ms": 200
        }
    )

    return response
```

---

## 🎯 Siguientes Pasos

- 📖 [SDK Python - Guía Completa](SDKS/SDK_PYTHON.md)
- 📖 [SDK TypeScript - Guía Completa](SDKS/SDK_TYPESCRIPT.md)
- 📖 [SDK Java - Guía Completa](SDKS/SDK_JAVA.md)
- 🔌 [Crear Plugins](PLUGINS/GUIA_PLUGINS.md)
- 🎣 [Usar MCP Hooks](MCP/GUIA_MCP.md)

---

## ❓ Problemas Comunes

**Error: "API key is required"**
- Verifica que estés usando la API key correcta
- Asegúrate de que la variable de entorno `CODEFLOWX_API_KEY` esté configurada

**Error: "Invalid workspace UUID"**
- Obtén el workspace UUID desde Developer Console
- Verifica que el formato sea UUID válido

**Error: "Network error"**
- Verifica tu conexión a internet
- Verifica que la URL base sea correcta: `https://gateway.codeflowx.ai`

---

**¿Necesitas ayuda?** Consulta [Troubleshooting](TROUBLESHOOTING.md) o [FAQ](FAQ.md)
