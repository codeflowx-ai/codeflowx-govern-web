# Guía MCP - Model Context Protocol

**MCP (Model Context Protocol)** es el protocolo estándar de **Anthropic** para comunicación cliente/servidor entre agentes de IA y herramientas externas.

CodeflowX implementa un **MCP Server** que expone sus funcionalidades como herramientas MCP, permitiendo que agentes de IA (Claude, ChatGPT, etc.) se conecten e interactúen con CodeflowX.

**Referencia oficial:** https://docs.anthropic.com/mcp

---

## 🏗️ Arquitectura

```
AI Agent (Claude Desktop, ChatGPT, etc.)
         ↓
    MCP Client
         ↓ JSON-RPC 2.0
    MCP Server (CodeflowX)
         ↓
    Backend CodeflowX
```

---

## 🔧 Herramientas MCP Expuestas

CodeflowX expone las siguientes herramientas MCP:

### Gestión de Agentes
- `codeflowx_register_agent` - Registrar agente externo
- `codeflowx_get_agent_status` - Estado del agente

### Telemetría
- `codeflowx_send_telemetry` - Enviar telemetría
- `codeflowx_get_metrics` - Obtener métricas

### Memoria
- `codeflowx_memory_get` - Obtener valor
- `codeflowx_memory_set` - Guardar valor
- `codeflowx_memory_snapshot` - Crear snapshot

### Políticas
- `codeflowx_policy_check` - Verificar políticas
- `codeflowx_policy_pre_check` - Pre-check

### Supervisor
- `codeflowx_supervisor_status` - Estado
- `codeflowx_supervisor_action` - Ejecutar acción

### Compliance
- `codeflowx_audit_log` - Registrar evidencia
- `codeflowx_verify_evidence` - Verificar

---

## 📝 Ejemplo de Configuración

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

### Uso desde Claude

```
Usuario: "Registra un nuevo agente llamado 'customer-service'"

Claude usa herramienta MCP:
→ codeflowx_register_agent(
    agent_name: "customer-service",
    agent_type: "custom",
    workspace_uuid: "..."
  )

Resultado: Agente registrado con ID "agent-uuid"
```

---

## 🔗 Más Información

- [Introducción MCP](INTRODUCCION_MCP.md) - Conceptos básicos
- [Hooks MCP](HOOKS.md) - Documentación técnica
- [Especificación MCP](../../specs/mcp.yaml) - Especificación completa
- [Anthropic MCP Docs](https://docs.anthropic.com/mcp) - Documentación oficial
