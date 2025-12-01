# Hooks MCP - Documentación Técnica

**Nota:** Los "hooks" en este contexto se refieren a puntos de extensión dentro de CodeflowX que pueden ser invocados vía MCP, no a los hooks del protocolo MCP estándar.

CodeflowX expone funcionalidades como herramientas MCP que pueden ser invocadas por agentes de IA.

---

## onOnboard

**Propósito:** Ejecutado al registrar componente
**Payload:**
- `component_uuid` - UUID del componente
- `component_type` - Tipo (AGENT, MODEL, etc.)
- `workspace_uuid` - UUID del workspace

**Ejemplo:**
```python
def on_onboard(payload):
    component_uuid = payload["component_uuid"]
    send_notification(f"Nuevo componente: {component_uuid}")
    return {"success": True}
```

---

## preInvoke

**Propósito:** Ejecutado antes de invocar
**Payload:**
- `prompt` - Prompt del usuario
- `context` - Contexto adicional
- `component_uuid` - UUID del componente

**Response:**
- `allowed` - bool (requerido)
- `reason` - string (si allowed=false)
- `transformed_prompt` - string (opcional)

**Ejemplo:**
```python
def pre_invoke(payload):
    prompt = payload["prompt"]
    if "spam" in prompt.lower():
        return {"allowed": False, "reason": "Contiene spam"}
    return {"allowed": True}
```

---

## postInvoke

**Propósito:** Ejecutado después de invocar
**Payload:**
- `response` - Respuesta del modelo
- `context` - Contexto adicional
- `component_uuid` - UUID del componente

**Response:**
- `transformed_response` - string (opcional)

**Ejemplo:**
```python
def post_invoke(payload):
    response = payload["response"]
    filtered = filter_toxic(response)
    return {"transformed_response": filtered}
```

---

## onPolicyViolation

**Propósito:** Ejecutado cuando hay violación
**Payload:**
- `violation_type` - Tipo de violación
- `severity` - Severidad (LOW, MEDIUM, HIGH, CRITICAL)
- `component_uuid` - UUID del componente

**Response:**
- `should_block` - bool (opcional)

**Ejemplo:**
```python
def on_policy_violation(payload):
    if payload["severity"] == "CRITICAL":
        return {"should_block": True}
    send_alert(payload["violation_type"])
    return {"should_block": False}
```

---

## onTelemetry

**Propósito:** Ejecutado antes de almacenar telemetría
**Payload:**
- `metrics` - Métricas a almacenar
- `component_uuid` - UUID del componente

**Response:**
- `enriched_metrics` - dict (opcional)

**Ejemplo:**
```python
def on_telemetry(payload):
    metrics = payload["metrics"]
    enriched = {
        **metrics,
        "region": get_region(),
        "tier": get_tier()
    }
    return {"enriched_metrics": enriched}
```

---

**Más:** [Guía MCP](GUIA_MCP.md)
