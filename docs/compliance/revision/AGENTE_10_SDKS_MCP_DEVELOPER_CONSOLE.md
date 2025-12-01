# GUÍA AGENTE 10 - SDKs, MCP Y DEVELOPER CONSOLE

**Agente:** Full Stack Senior - Python/TypeScript + ZKoss
**Equipo:** Equipo 10 - Extensibility Layer
**Duración:** 20 horas
**Objetivo:** Producir SDKs Python/TypeScript, especificación MCP estándar y Developer Console mínimo viable para partners

---

## 📋 TAREA ASIGNADA

### **Prompt 2 — Extensibility Layer → SDKs, MCP estándar y Developer Console (Plugin model)**

**Estado:** 🔴 PENDIENTE

**Objetivo:** Producir el modelo SDK + MCP (Model Control Points) estándar + Developer Console mínimo viable que permita a partners construir conectores/add-ons sin necesidad de reescribir core, manteniendo integración con tu framework FaaS y metamodelos sectoriales.

**Esfuerzo Estimado:** 20 horas

---

## 📚 DOCUMENTOS DE REFERENCIA

### Prompt Principal:
- **`/docs/compliance/EVOLUCION_AIOS_OVERLAY.md`** - Sección "Prompt 2 — Extensibility Layer → SDKs, MCP estándar y Developer Console"

### Documentación General:
- **Cliente Java Existente:** `/eclipse-workspace/nocode.service/codeflowx.govern.nocode.client/`
- **API OpenAPI:** `/eclipse-workspace/nocode.service/codeflowx-aios-api/src/main/resources/openapi/aios-api.yaml`
- **Framework FaaS:** `/eclipse-workspace/nocode.service/codeflowx.govern.faas/`
- **ZKoss ViewModels:** `/git/suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodels/`
- **ZKoss ZUL:** `/git/suinsit.nova.web/src/main/webapp/console/gobierno/`

### Referencias Técnicas:
- **AioComponent:** Entidad existente en `nocode.service.entitys`
- **AioMarketplaceEntry:** Entidad existente para plugins
- **Metamodelos:** `/eclipse-workspace/nocode.service/codeflowx.govern.faas/src/main/resources/metamodel/`

---

## 🏗️ ARQUITECTURA - CONVENCIONES OBLIGATORIAS

### **SDKs:**
- **Python SDK:** Repositorio separado o módulo en proyecto existente
- **TypeScript SDK:** Repositorio separado o módulo en proyecto existente
- **Estructura:** Seguir patrón de `codeflowx.govern.nocode.client` (mismos endpoints, misma estructura)

### **MCP (Model Control Points):**
- **Especificación:** JSON/YAML en `/specs/mcp.yaml`
- **Extiende AioComponent:** type="MCP" con metadata específica
- **Hooks:** onOnboard, preInvoke, postInvoke, onPolicyViolation, onTelemetry

### **Developer Console:**
- **Framework:** ZKoss (ViewModel + ZUL)
- **Ubicación ViewModels:** `/git/suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodels/developer/`
- **Ubicación ZUL:** `/git/suinsit.nova.web/src/main/webapp/console/gobierno/developer/`

### **Plugin Model:**
- **Formato:** `.cfx-plugin` (ZIP con manifest.json + code + tests)
- **Registro:** Como `AioMarketplaceEntry`
- **Sandbox:** Modo sandbox contenedorizado (no producción inicialmente)

---

## 📁 ESTRUCTURA DE DIRECTORIOS

### **Python SDK:**
```
codeflowx-sdk-python/
├── codeflowx_sdk/
│   ├── __init__.py
│   ├── agent_client.py          ← AgentClient principal
│   ├── config.py                ← Configuración
│   ├── resilience.py            ← Retry, Circuit Breaker, Rate Limiting
│   ├── cache.py                 ← Caché local y offline mode
│   ├── pii_filter.py            ← Filtrado de PII
│   ├── events.py                ← Eventos y callbacks
│   ├── decorators.py            ← Decoradores para frameworks
│   ├── metrics.py               ← Métricas y observabilidad
│   └── validation.py            ← Validación de datos
├── tests/
│   ├── test_agent_client.py
│   ├── test_resilience.py
│   └── test_integration.py
├── examples/
│   ├── quickstart.py
│   ├── fastapi_example.py
│   └── jupyter_example.py
├── docs/
│   ├── README.md
│   ├── QUICKSTART.md
│   └── API_REFERENCE.md
└── setup.py
```

### **TypeScript SDK:**
```
codeflowx-sdk-typescript/
├── src/
│   ├── index.ts
│   ├── agent-client.ts
│   ├── config.ts
│   ├── resilience.ts
│   ├── cache.ts
│   ├── pii-filter.ts
│   ├── events.ts
│   ├── decorators.ts
│   ├── metrics.ts
│   └── validation.ts
├── tests/
├── examples/
└── package.json
```

### **MCP Spec:**
```
specs/
└── mcp.yaml                     ← Especificación MCP
```

### **Developer Console (ZKoss):**
```
suinsit.nova.web/
├── src/main/java/com/codeflowx/govern/viewmodels/developer/
│   ├── DeveloperConsoleViewModel.java
│   ├── PluginRegistryViewModel.java
│   ├── ApiKeyManagementViewModel.java
│   └── SandboxPlaygroundViewModel.java
└── src/main/webapp/console/gobierno/developer/
    ├── developer-console.zul
    ├── plugin-registry.zul
    ├── api-key-management.zul
    └── sandbox-playground.zul
```

---

## 🔧 IMPLEMENTACIÓN

### **Fase 1: Especificación MCP**

#### **1.1. Crear mcp.yaml:**
```yaml
# specs/mcp.yaml
version: "1.0.0"
name: "Model Control Points"
description: "Especificación estándar para puntos de control de modelos y agentes"

hooks:
  onOnboard:
    description: "Hook ejecutado cuando se registra un componente"
    payload:
      component_uuid: string
      component_type: string
      metadata: object

  preInvoke:
    description: "Hook ejecutado antes de invocar modelo/agente"
    payload:
      component_uuid: string
      prompt: string
      context: object
    returns:
      allowed: boolean
      reason: string

  postInvoke:
    description: "Hook ejecutado después de invocar modelo/agente"
    payload:
      component_uuid: string
      response: string
      metrics: object

  onPolicyViolation:
    description: "Hook ejecutado cuando se detecta violación de política"
    payload:
      component_uuid: string
      violation_type: string
      severity: string
      details: object

  onTelemetry:
    description: "Hook ejecutado para enviar telemetría"
    payload:
      component_uuid: string
      event_type: string
      metrics: object

integration:
  extends: "AioComponent"
  type: "MCP"
  metadata:
    hooks: array
    endpoints: object
    version: string
```

### **Fase 2: Python SDK**

#### **2.1. AgentClient Principal:**
```python
# codeflowx_sdk/agent_client.py
from typing import Optional, Dict, List, Any
from codeflowx_sdk.config import Config
from codeflowx_sdk.resilience import RetryConfig, CircuitBreakerConfig, RateLimiterConfig
from codeflowx_sdk.cache import CacheConfig, OfflineQueue
from codeflowx_sdk.pii_filter import PIIFilter

class AgentClient:
    def __init__(
        self,
        api_key: str,
        base_url: str = "https://gateway.codeflowx.ai",
        retry_config: Optional[RetryConfig] = None,
        circuit_breaker_config: Optional[CircuitBreakerConfig] = None,
        rate_limiter_config: Optional[RateLimiterConfig] = None,
        cache_config: Optional[CacheConfig] = None,
        offline_queue: Optional[OfflineQueue] = None
    ):
        """Inicializa cliente con agent_token recibido en registro"""
        self.api_key = api_key
        self.base_url = base_url
        # ... inicializar configuraciones

    def register_agent(
        self,
        agent_name: str,
        agent_type: str,
        workspace_uuid: str,
        capabilities: Dict[str, Any],
        callback_url: Optional[str] = None,
        endpoint: Optional[str] = None
    ) -> Dict[str, Any]:
        """Registra un agente externo"""
        # Implementar según prompt

    def send_telemetry(
        self,
        component_uuid: str,
        event_type: str,
        payload: Dict[str, Any],
        pii_filter: Optional[PIIFilter] = None
    ) -> None:
        """Envía telemetría (filtra PII automáticamente)"""
        # Implementar según prompt

    # ... otros métodos según prompt
```

#### **2.2. Funcionalidades de Resiliencia:**
```python
# codeflowx_sdk/resilience.py
from dataclasses import dataclass
from typing import List

@dataclass
class RetryConfig:
    max_attempts: int = 3
    initial_delay_ms: int = 1000
    max_delay_ms: int = 10000
    multiplier: float = 2.0
    retryable_status_codes: List[int] = [503, 502, 429, 500]

@dataclass
class CircuitBreakerConfig:
    failure_threshold: int = 5
    timeout_seconds: int = 60
    half_open_max_calls: int = 3

@dataclass
class RateLimiterConfig:
    requests_per_second: int = 100
    burst_size: int = 200
```

### **Fase 3: TypeScript SDK**

#### **3.1. AgentClient TypeScript:**
```typescript
// src/agent-client.ts
import { Config } from './config';
import { RetryConfig, CircuitBreakerConfig, RateLimiterConfig } from './resilience';

export class AgentClient {
    constructor(
        apiKey: string,
        baseUrl: string = 'https://gateway.codeflowx.ai',
        retryConfig?: RetryConfig,
        circuitBreakerConfig?: CircuitBreakerConfig,
        rateLimiterConfig?: RateLimiterConfig
    ) {
        // Inicializar cliente
    }

    async registerAgent(
        agentName: string,
        agentType: string,
        workspaceUuid: string,
        capabilities: Record<string, any>,
        callbackUrl?: string,
        endpoint?: string
    ): Promise<AgentRegistration> {
        // Implementar según prompt
    }

    // ... otros métodos según prompt
}
```

### **Fase 4: Developer Console (ZKoss)**

#### **4.1. DeveloperConsoleViewModel.java:**
```java
package com.codeflowx.govern.viewmodels.developer;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;
import lombok.extern.slf4j.Slf4j;
import com.codeflowx.govern.business.developer.DeveloperConsoleService;

@Slf4j
public class DeveloperConsoleViewModel {

    @Wire
    private Window developerConsoleWin;

    @Autowired
    private DeveloperConsoleService developerConsoleService;

    // Métodos para:
    // - Registrar plugins
    // - Generar API keys
    // - Ver logs
    // - Test endpoints
    // - Playground interactivo
}
```

#### **4.2. developer-console.zul:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<window id="developerConsoleWin"
        title="Developer Console"
        width="100%"
        height="100%"
        apply="com.codeflowx.govern.viewmodels.developer.DeveloperConsoleViewModel">

    <tabbox>
        <tabs>
            <tab label="Plugins" />
            <tab label="API Keys" />
            <tab label="Logs" />
            <tab label="Playground" />
        </tabs>
        <tabpanels>
            <!-- Plugin Registry -->
            <tabpanel>
                <!-- Implementar según prompt -->
            </tabpanel>
            <!-- ... otros tabpanels -->
        </tabpanels>
    </tabbox>
</window>
```

### **Fase 5: Plugin Model**

#### **5.1. Estructura .cfx-plugin:**
```
plugin-name-v1.0.0.cfx-plugin (ZIP)
├── manifest.json
├── code/
│   └── plugin.py (o plugin.js)
├── tests/
│   └── test_plugin.py
└── README.md
```

#### **5.2. manifest.json:**
```json
{
  "name": "plugin-name",
  "version": "1.0.0",
  "author": "Partner Name",
  "description": "Plugin description",
  "hooks": ["onOnboard", "preInvoke", "postInvoke"],
  "capabilities": {
    "language": "python",
    "domains": ["customer_service"]
  },
  "signature": "base64-encoded-signature"
}
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### **Antes de Empezar:**
- [ ] Leer prompt completo en `EVOLUCION_AIOS_OVERLAY.md`
- [ ] Revisar `codeflowx.govern.nocode.client` como referencia
- [ ] Revisar `codeflowx-aios-api/openapi.yaml` para endpoints
- [ ] Revisar entidades `AioComponent` y `AioMarketplaceEntry`

### **Fase 1: MCP Spec**
- [ ] Crear `specs/mcp.yaml` con especificación completa
- [ ] Documentar todos los hooks y payloads
- [ ] Validar que extiende `AioComponent` (type="MCP")

### **Fase 2: Python SDK**
- [ ] Crear estructura del proyecto
- [ ] Implementar `AgentClient` con todos los métodos
- [ ] Implementar resiliencia (retry, circuit breaker, rate limiting)
- [ ] Implementar caché y offline mode
- [ ] Implementar filtrado de PII
- [ ] Implementar eventos y callbacks
- [ ] Implementar decoradores
- [ ] Implementar métricas
- [ ] Crear tests (unit + integration)
- [ ] Crear ejemplos (quickstart, FastAPI, Jupyter)
- [ ] Documentación completa

### **Fase 3: TypeScript SDK**
- [ ] Crear estructura del proyecto
- [ ] Implementar `AgentClient` con todos los métodos
- [ ] Implementar resiliencia
- [ ] Implementar caché y offline mode
- [ ] Crear tests
- [ ] Crear ejemplos
- [ ] Documentación completa

### **Fase 4: Developer Console (ZKoss)**
- [ ] Crear `DeveloperConsoleViewModel`
- [ ] Crear `PluginRegistryViewModel`
- [ ] Crear `ApiKeyManagementViewModel`
- [ ] Crear `SandboxPlaygroundViewModel`
- [ ] Crear ZULs correspondientes
- [ ] Integrar Swagger UI embebido
- [ ] Tests de ViewModels

### **Fase 5: Plugin Model**
- [ ] Definir estructura `.cfx-plugin`
- [ ] Crear validador de manifest.json
- [ ] Crear loader server-side
- [ ] Implementar sandbox contenedorizado
- [ ] Integración con `AioMarketplaceEntry`

### **Fase 6: Documentación**
- [ ] Quickstart guide para partners
- [ ] Ejemplos de integración (M365, SAP mock)
- [ ] Guía de creación de plugins
- [ ] API Reference completa

### **Después de Implementación:**
- [ ] SDKs pasan smoke tests
- [ ] Developer Console muestra 3 ejemplos
- [ ] Plugin packaging valida firma y manifest
- [ ] Plugins se registran como `AioMarketplaceEntry`
- [ ] Documentación completa
- [ ] Commit con mensaje: `[AGENTE-10] SDKs, MCP y Developer Console`

---

## 📝 REGISTRO DE ARCHIVOS CREADOS

**Estado:** 🔴 PENDIENTE

**Archivos a Crear:**

**MCP Spec:**
- [ ] `specs/mcp.yaml`

**Python SDK:**
- [ ] `codeflowx-sdk-python/codeflowx_sdk/__init__.py`
- [ ] `codeflowx-sdk-python/codeflowx_sdk/agent_client.py`
- [ ] `codeflowx-sdk-python/codeflowx_sdk/config.py`
- [ ] `codeflowx-sdk-python/codeflowx_sdk/resilience.py`
- [ ] `codeflowx-sdk-python/codeflowx_sdk/cache.py`
- [ ] `codeflowx-sdk-python/codeflowx_sdk/pii_filter.py`
- [ ] `codeflowx-sdk-python/codeflowx_sdk/events.py`
- [ ] `codeflowx-sdk-python/codeflowx_sdk/decorators.py`
- [ ] `codeflowx-sdk-python/codeflowx_sdk/metrics.py`
- [ ] `codeflowx-sdk-python/codeflowx_sdk/validation.py`
- [ ] `codeflowx-sdk-python/setup.py`
- [ ] `codeflowx-sdk-python/README.md`

**TypeScript SDK:**
- [ ] `codeflowx-sdk-typescript/src/index.ts`
- [ ] `codeflowx-sdk-typescript/src/agent-client.ts`
- [ ] `codeflowx-sdk-typescript/src/config.ts`
- [ ] `codeflowx-sdk-typescript/src/resilience.ts`
- [ ] `codeflowx-sdk-typescript/package.json`
- [ ] `codeflowx-sdk-typescript/README.md`

**Developer Console:**
- [ ] `suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodels/developer/DeveloperConsoleViewModel.java`
- [ ] `suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodels/developer/PluginRegistryViewModel.java`
- [ ] `suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodels/developer/ApiKeyManagementViewModel.java`
- [ ] `suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodels/developer/SandboxPlaygroundViewModel.java`
- [ ] `suinsit.nova.web/src/main/webapp/console/gobierno/developer/developer-console.zul`
- [ ] `suinsit.nova.web/src/main/webapp/console/gobierno/developer/plugin-registry.zul`
- [ ] `suinsit.nova.web/src/main/webapp/console/gobierno/developer/api-key-management.zul`
- [ ] `suinsit.nova.web/src/main/webapp/console/gobierno/developer/sandbox-playground.zul`

**Plugin Model:**
- [ ] Validador de manifest.json
- [ ] Loader server-side
- [ ] Sandbox contenedorizado

**Fecha Inicio:** [FECHA]
**Fecha Finalización:** [FECHA]

---

## 🚨 GESTIÓN DE BLOQUEOS

### **Si encuentras bloqueos:**

1. **Endpoints no documentados en OpenAPI:**
   - Revisar `codeflowx-aios-api` para endpoints reales
   - Actualizar OpenAPI spec si es necesario

2. **AioComponent no tiene campos necesarios:**
   - Revisar entidad existente
   - Agregar campos en metadata si es necesario

3. **ZKoss ViewModels no compilan:**
   - Verificar imports
   - Revisar ViewModels existentes como referencia

4. **Duda sobre implementación:**
   - Revisar prompt completo en `EVOLUCION_AIOS_OVERLAY.md`
   - Revisar `codeflowx.govern.nocode.client` como referencia
   - Documentar la duda en reporte

---

## 📊 REPORTE DE PROGRESO (Cada 2 horas)

```
AGENTE 10 - REPORTE HORA [X]
===========================
Fase Actual: [Fase 1/2/3/4/5/6]
Progreso: [%]

Tareas Completadas:
- [Lista de tareas]

Tareas En Progreso:
- [Tarea]: [Progreso %] - [Bloqueos si hay]

Archivos Creados/Modificados:
- [Lista de archivos]

Bloqueos/Problemas:
- [Lista de bloqueos]

Tiempo Estimado Restante:
- [Horas estimadas]
```

---

## 📚 REFERENCIAS RÁPIDAS

### **Comandos Útiles:**
```bash
# Python SDK
cd codeflowx-sdk-python
python -m pytest tests/
python setup.py install

# TypeScript SDK
cd codeflowx-sdk-typescript
npm install
npm test
npm run build

# Compilar ZKoss ViewModels
cd /mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web
mvn clean compile -DskipTests
```

### **Rutas Importantes:**
- **Prompt:** `/git/suinsit.nova.web/docs/compliance/EVOLUCION_AIOS_OVERLAY.md`
- **Cliente Java:** `/eclipse-workspace/nocode.service/codeflowx.govern.nocode.client/`
- **OpenAPI:** `/eclipse-workspace/nocode.service/codeflowx-aios-api/src/main/resources/openapi/aios-api.yaml`
- **ViewModels:** `/git/suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodels/`
- **ZULs:** `/git/suinsit.nova.web/src/main/webapp/console/gobierno/`

---

**Última Actualización:** [FECHA]
**Próxima Revisión:** Al completar cada fase
