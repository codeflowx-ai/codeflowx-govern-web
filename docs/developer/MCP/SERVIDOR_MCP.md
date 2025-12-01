# Servidor MCP de CodeflowX

CodeflowX implementa un servidor MCP que expone sus funcionalidades como herramientas para agentes de IA.

---

## 🏗️ Arquitectura

```
┌─────────────────────┐
│  AI Agent           │  (Claude Desktop, ChatGPT, etc.)
│  (MCP Client)       │
└──────────┬──────────┘
           │ JSON-RPC 2.0
           │ (stdio/HTTP/WebSocket)
           ↓
┌─────────────────────┐
│  MCP Server         │  (CodeflowX)
│  codeflowx-mcp      │
└──────────┬──────────┘
           │
           ↓
┌─────────────────────┐
│  Backend CodeflowX  │  (Spring Boot)
│  REST API           │
└─────────────────────┘
```

---

## 📦 Instalación

### NPM (Recomendado)

```bash
npm install -g @codeflowx/mcp-server
```

### Desde Código

```bash
git clone https://github.com/codeflowx-ai/codeflowx-mcp-server.git
cd codeflowx-mcp-server
npm install
npm run build
```

---

## ⚙️ Configuración

### Variables de Entorno

```bash
export CODEFLOWX_API_KEY="cfx_sk_live_..."
export CODEFLOWX_BASE_URL="https://gateway.codeflowx.ai"
export CODEFLOWX_TIMEOUT="30"
```

### Claude Desktop

Edita `~/Library/Application Support/Claude/claude_desktop_config.json`:

```json
{
  "mcpServers": {
    "codeflowx": {
      "command": "npx",
      "args": ["-y", "@codeflowx/mcp-server"],
      "env": {
        "CODEFLOWX_API_KEY": "cfx_sk_live_..."
      }
    }
  }
}
```

---

## 🔧 Herramientas Disponibles

### Gestión de Agentes

#### `codeflowx_register_agent`
Registra un nuevo agente externo.

**Parámetros:**
- `agent_name` (string) - Nombre del agente
- `agent_type` (string) - Tipo: "custom", "llm", etc.
- `workspace_uuid` (string) - UUID del workspace
- `capabilities` (object) - Capacidades del agente

**Retorna:**
- `agent_id` (string) - ID del agente
- `agent_token` (string) - Token para operaciones

---

#### `codeflowx_get_agent_status`
Obtiene estado de un agente.

**Parámetros:**
- `agent_id` (string) - ID del agente

**Retorna:**
- `status` (string) - Estado: "ACTIVE", "PAUSED", etc.

---

### Telemetría

#### `codeflowx_send_telemetry`
Envía telemetría de un componente.

**Parámetros:**
- `component_uuid` (string) - UUID del componente
- `event_type` (string) - Tipo de evento
- `payload` (object) - Datos del evento

---

#### `codeflowx_get_metrics`
Obtiene métricas de un componente.

**Parámetros:**
- `component_uuid` (string) - UUID del componente
- `time_range` (string) - Rango: "1h", "24h", "7d"

---

### Memoria

#### `codeflowx_memory_get`
Obtiene valor de memoria.

**Parámetros:**
- `agent_id` (string) - ID del agente
- `namespace` (string) - Namespace
- `key` (string) - Clave

---

#### `codeflowx_memory_set`
Guarda valor en memoria.

**Parámetros:**
- `agent_id` (string) - ID del agente
- `namespace` (string) - Namespace
- `key` (string) - Clave
- `value` (any) - Valor

---

### Políticas

#### `codeflowx_policy_check`
Verifica políticas antes de ejecutar acción.

**Parámetros:**
- `component_uuid` (string) - UUID del componente
- `action` (string) - Acción: "INVOKE", "TRAIN", etc.
- `context` (object) - Contexto de la acción

**Retorna:**
- `allowed` (boolean) - Si está permitido
- `reason` (string) - Razón si no está permitido

---

### Supervisor

#### `codeflowx_supervisor_status`
Obtiene estado del supervisor.

**Parámetros:**
- `agent_id` (string) - ID del agente

**Retorna:**
- `state` (string) - Estado: "ACTIVE", "PAUSED", etc.
- `messages` (array) - Mensajes pendientes

---

### Compliance

#### `codeflowx_audit_log`
Registra evidencia inmutable.

**Parámetros:**
- `component_uuid` (string) - UUID del componente
- `operation` (string) - Operación realizada
- `actor` (string) - Quién realizó la operación
- `metadata` (object) - Metadata adicional

**Retorna:**
- `log_uuid` (string) - UUID del log
- `hash` (string) - Hash de la evidencia

---

## 📝 Ejemplo de Uso

### Desde Claude Desktop

```
Usuario: "Registra un agente llamado 'customer-service' y verifica si puedo invocarlo"

Claude:
1. Usa codeflowx_register_agent
   → Agente registrado: agent-uuid

2. Usa codeflowx_policy_check
   → Política verificada: allowed=true

3. Responde al usuario con el resultado
```

---

## 🔗 Más Información

- [Introducción MCP](INTRODUCCION_MCP.md)
- [Guía MCP](GUIA_MCP.md)
- [Especificación MCP](../../specs/mcp.yaml)
