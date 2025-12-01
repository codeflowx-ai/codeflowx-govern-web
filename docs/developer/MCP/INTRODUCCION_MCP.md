# Introducción a MCP - Model Context Protocol

## ¿Qué es MCP?

**MCP (Model Context Protocol)** es un protocolo estándar desarrollado por **Anthropic** que permite comunicación cliente/servidor entre agentes de IA y herramientas/fuentes de datos externas.

**Referencia oficial:** https://docs.anthropic.com/mcp

---

## 🏗️ Arquitectura MCP

```
┌─────────────────┐
│  AI Agent       │  (Claude, ChatGPT, etc.)
│  (MCP Client)   │
└────────┬────────┘
         │ JSON-RPC 2.0
         ↓
┌─────────────────┐
│  MCP Server     │  (CodeflowX)
│  (Herramientas) │
└────────┬────────┘
         │
         ↓
┌─────────────────┐
│  Backend        │  (Spring Boot)
│  CodeflowX      │
└─────────────────┘
```

---

## 🎯 ¿Cómo Funciona?

### 1. Cliente MCP (AI Agent)
- Agente de IA (Claude, ChatGPT, etc.)
- Se conecta al servidor MCP
- Solicita herramientas disponibles
- Invoca herramientas cuando las necesita

### 2. Servidor MCP (CodeflowX)
- Expone funcionalidades como herramientas MCP
- Responde a solicitudes del cliente
- Ejecuta operaciones en el backend
- Retorna resultados al cliente

### 3. Protocolo
- **Transporte:** stdio, HTTP, WebSocket
- **Formato:** JSON-RPC 2.0
- **Mensajes:** Request/Response

---

## 🔧 Herramientas MCP en CodeflowX

CodeflowX expone las siguientes herramientas MCP:

### Gestión de Agentes
- `codeflowx_register_agent` - Registrar agente externo
- `codeflowx_get_agent_status` - Obtener estado del agente

### Telemetría
- `codeflowx_send_telemetry` - Enviar telemetría
- `codeflowx_get_metrics` - Obtener métricas

### Memoria
- `codeflowx_memory_get` - Obtener valor de memoria
- `codeflowx_memory_set` - Guardar valor en memoria
- `codeflowx_memory_create_snapshot` - Crear snapshot

### Políticas
- `codeflowx_policy_check` - Verificar políticas
- `codeflowx_policy_pre_check` - Pre-check rápido

### Supervisor
- `codeflowx_supervisor_get_status` - Estado del supervisor
- `codeflowx_supervisor_handle_action` - Ejecutar acción

### Compliance
- `codeflowx_audit_log` - Registrar evidencia inmutable
- `codeflowx_verify_evidence` - Verificar evidencia

---

## 📝 Ejemplo de Uso

### Desde Claude Desktop

```json
{
  "mcpServers": {
    "codeflowx": {
      "command": "npx",
      "args": [
        "-y",
        "@codeflowx/mcp-server"
      ],
      "env": {
        "CODEFLOWX_API_KEY": "cfx_sk_live_..."
      }
    }
  }
}
```

### Desde Código

```python
from mcp import ClientSession, StdioServerParameters
from mcp.client.stdio import stdio_client

async def main():
    server_params = StdioServerParameters(
        command="codeflowx-mcp-server",
        env={"CODEFLOWX_API_KEY": "cfx_sk_live_..."}
    )

    async with stdio_client(server_params) as (read, write):
        async with ClientSession(read, write) as session:
            # Listar herramientas
            tools = await session.list_tools()

            # Invocar herramienta
            result = await session.call_tool(
                "codeflowx_policy_check",
                arguments={
                    "component_uuid": "agent-uuid",
                    "action": "INVOKE",
                    "context": {"prompt": "Hello"}
                }
            )
```

---

## 🔗 Más Información

- [Guía MCP](GUIA_MCP.md) - Guía completa
- [Hooks MCP](HOOKS.md) - Documentación de hooks
- [Especificación MCP](../../specs/mcp.yaml) - Especificación técnica

---

**Referencias:**
- [Anthropic MCP Documentation](https://docs.anthropic.com/mcp)
- [MCP Specification](https://modelcontextprotocol.io)
