# Guía de Instalación - CodeflowX SDKs

Guía completa para instalar y configurar los SDKs de CodeflowX en diferentes entornos.

---

## 📦 SDK Python

### Instalación

#### Desde PyPI (Recomendado)
```bash
pip install codeflowx-sdk-python
```

#### Desde código fuente
```bash
git clone https://github.com/codeflowx-ai/codeflowx-sdk-python.git
cd codeflowx-sdk-python
pip install -e .
```

### Requisitos

- Python 3.8 o superior
- pip 20.0 o superior

### Dependencias

El SDK instala automáticamente:
- `httpx>=0.25.0` - Cliente HTTP
- `pydantic>=2.5.0` - Validación de datos

### Verificar Instalación

```python
python -c "import codeflowx_sdk; print(codeflowx_sdk.__version__)"
```

### Configuración

#### Variables de Entorno
```bash
export CODEFLOWX_API_KEY="cfx_sk_live_abc123..."
export CODEFLOWX_BASE_URL="https://gateway.codeflowx.ai"
export CODEFLOWX_TIMEOUT="30"
```

#### Desde Código
```python
from codeflowx_sdk import AgentClient, Config

# Opción 1: Desde variables de entorno
client = AgentClient.from_env()

# Opción 2: Desde Config
config = Config.from_env()
client = AgentClient.from_config(config)

# Opción 3: Directo
client = AgentClient(api_key="cfx_sk_live_abc123...")
```

---

## 📦 SDK TypeScript

### Instalación

#### Desde npm
```bash
npm install codeflowx-sdk-typescript
```

#### Desde código fuente
```bash
git clone https://github.com/codeflowx-ai/codeflowx-sdk-typescript.git
cd codeflowx-sdk-typescript
npm install
npm run build
```

### Requisitos

- Node.js 16.0 o superior
- npm 7.0 o superior

### Dependencias

El SDK instala automáticamente:
- `axios>=1.6.0` - Cliente HTTP

### Verificar Instalación

```typescript
import { AgentClient } from 'codeflowx-sdk-typescript';
console.log('SDK instalado correctamente');
```

### Configuración

#### Variables de Entorno
```bash
export CODEFLOWX_API_KEY="cfx_sk_live_abc123..."
export CODEFLOWX_BASE_URL="https://gateway.codeflowx.ai"
export CODEFLOWX_TIMEOUT="30"
```

#### Desde Código
```typescript
import { AgentClient, Config } from 'codeflowx-sdk-typescript';

// Opción 1: Desde variables de entorno
const client = AgentClient.fromEnv();

// Opción 2: Directo
const client = new AgentClient('cfx_sk_live_abc123...');
```

### TypeScript

Si usas TypeScript, el SDK incluye tipos completos:
```typescript
import { AgentClient, AgentRegistration, PolicyCheckResult } from 'codeflowx-sdk-typescript';
```

---

## 📦 SDK Java

### Instalación

#### Desde Maven Central
```xml
<dependency>
    <groupId>com.codeflowx</groupId>
    <artifactId>codeflowx-sdk-java</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### Desde código fuente
```bash
git clone https://github.com/codeflowx-ai/codeflowx-sdk-java.git
cd codeflowx-sdk-java
mvn clean install
```

### Requisitos

- Java 11 o superior
- Maven 3.6 o superior

### Dependencias

El SDK incluye automáticamente:
- `okhttp3>=4.12.0` - Cliente HTTP
- `jackson-databind>=2.16.0` - JSON
- `lombok>=1.18.30` - Reducción de boilerplate

### Verificar Instalación

```java
import com.codeflowx.sdk.AgentClient;

public class Test {
    public static void main(String[] args) {
        AgentClient client = new AgentClient("test");
        System.out.println("SDK instalado correctamente");
    }
}
```

### Configuración

#### Variables de Entorno
```bash
export CODEFLOWX_API_KEY="cfx_sk_live_abc123..."
export CODEFLOWX_BASE_URL="https://gateway.codeflowx.ai"
export CODEFLOWX_TIMEOUT="30"
```

#### Desde Código
```java
import com.codeflowx.sdk.AgentClient;
import com.codeflowx.sdk.config.Config;

// Opción 1: Desde variables de entorno
AgentClient client = AgentClient.fromEnv();

// Opción 2: Desde Config
Config config = Config.fromEnv();
AgentClient client = AgentClient.fromConfig(config);

// Opción 3: Directo
AgentClient client = new AgentClient("cfx_sk_live_abc123...");
```

### Spring Boot

Si usas Spring Boot, puedes inyectar el cliente:

```java
@Configuration
public class CodeflowXConfig {

    @Bean
    public AgentClient agentClient() {
        return AgentClient.fromEnv();
    }
}

@Service
public class MyService {

    @Autowired
    private AgentClient agentClient;

    // Usar agentClient...
}
```

---

## 🔧 Configuración Avanzada

### Timeout Personalizado

#### Python
```python
from codeflowx_sdk import AgentClient, Config

config = Config(
    api_key="cfx_sk_live_abc123...",
    timeout_seconds=60  # 60 segundos
)
client = AgentClient.from_config(config)
```

#### TypeScript
```typescript
import { AgentClient, Config } from 'codeflowx-sdk-typescript';

const config = new Config({
    apiKey: 'cfx_sk_live_abc123...',
    timeoutSeconds: 60
});
const client = new AgentClient(config.apiKey, config.baseUrl);
```

#### Java
```java
AgentClient client = new AgentClient(
    "cfx_sk_live_abc123...",
    "https://gateway.codeflowx.ai"
    // Timeout se configura en OkHttpClient internamente
);
```

### Retry y Resiliencia

#### Python
```python
from codeflowx_sdk import AgentClient, RetryConfig, CircuitBreakerConfig

retry_config = RetryConfig(
    max_attempts=5,
    initial_delay_ms=1000,
    max_delay_ms=10000
)

circuit_breaker = CircuitBreakerConfig(
    failure_threshold=5,
    timeout_seconds=60
)

client = AgentClient(
    api_key="cfx_sk_live_abc123...",
    retry_config=retry_config,
    circuit_breaker_config=circuit_breaker
)
```

### Caché y Offline Mode

#### Python
```python
from codeflowx_sdk import AgentClient, CacheConfig, OfflineQueue

cache_config = CacheConfig(
    enabled=True,
    ttl_seconds=300,
    cache_policy_checks=True
)

offline_queue = OfflineQueue(
    max_queue_size=10000,
    persist_to_disk=True
)

client = AgentClient(
    api_key="cfx_sk_live_abc123...",
    cache_config=cache_config,
    offline_queue=offline_queue
)
```

---

## 🐳 Docker

### Python
```dockerfile
FROM python:3.11-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

CMD ["python", "app.py"]
```

### TypeScript/Node.js
```dockerfile
FROM node:18-alpine

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

CMD ["npm", "start"]
```

### Java
```dockerfile
FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/app.jar .

CMD ["java", "-jar", "app.jar"]
```

---

## ☁️ Cloud Providers

### AWS Lambda (Python)
```python
import json
from codeflowx_sdk import AgentClient

def lambda_handler(event, context):
    client = AgentClient.from_env()
    # Tu código aquí
    return {"statusCode": 200, "body": json.dumps("OK")}
```

### Azure Functions (Python)
```python
import azure.functions as func
from codeflowx_sdk import AgentClient

def main(req: func.HttpRequest) -> func.HttpResponse:
    client = AgentClient.from_env()
    # Tu código aquí
    return func.HttpResponse("OK")
```

### Google Cloud Functions (Python)
```python
from codeflowx_sdk import AgentClient

def cloud_function(request):
    client = AgentClient.from_env()
    # Tu código aquí
    return "OK"
```

---

## 🔒 Seguridad

### Almacenar API Keys

**❌ NO HACER:**
```python
# NUNCA hardcodear API keys
client = AgentClient(api_key="cfx_sk_live_abc123...")
```

**✅ HACER:**
```python
# Usar variables de entorno
import os
client = AgentClient(api_key=os.getenv("CODEFLOWX_API_KEY"))
```

### Secrets Management

#### AWS Secrets Manager
```python
import boto3
from codeflowx_sdk import AgentClient

secrets_client = boto3.client('secretsmanager')
secret = secrets_client.get_secret_value(SecretId='codeflowx/api-key')
api_key = secret['SecretString']

client = AgentClient(api_key=api_key)
```

#### HashiCorp Vault
```python
import hvac
from codeflowx_sdk import AgentClient

client_vault = hvac.Client(url='https://vault.example.com')
secret = client_vault.secrets.kv.v2.read_secret_version(path='codeflowx/api-key')
api_key = secret['data']['data']['api_key']

client = AgentClient(api_key=api_key)
```

---

## ✅ Verificación Post-Instalación

### Test de Conexión

#### Python
```python
from codeflowx_sdk import AgentClient

client = AgentClient.from_env()
health = client.health()
print(f"Status: {health['status']}")
```

#### TypeScript
```typescript
import { AgentClient } from 'codeflowx-sdk-typescript';

const client = AgentClient.fromEnv();
const health = await client.health();
console.log('Status:', health.status);
```

#### Java
```java
AgentClient client = AgentClient.fromEnv();
HealthResponse health = client.health();
System.out.println("Status: " + health.getStatus());
```

---

## 🐛 Troubleshooting

### Error: "Module not found"
**Solución:** Verifica que el SDK esté instalado correctamente
```bash
# Python
pip list | grep codeflowx

# TypeScript
npm list codeflowx-sdk-typescript

# Java
mvn dependency:tree | grep codeflowx
```

### Error: "SSL certificate verification failed"
**Solución:** Verifica certificados SSL o usa `verify_ssl=False` (solo desarrollo)
```python
config = Config(api_key="...", verify_ssl=False)  # Solo desarrollo
```

### Error: "Connection timeout"
**Solución:** Aumenta timeout o verifica conectividad
```python
config = Config(api_key="...", timeout_seconds=60)
```

---

**Siguiente:** [Quick Start Guide](QUICK_START.md)
