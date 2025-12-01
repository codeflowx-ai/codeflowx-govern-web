# Guía de Plugins - CodeflowX

Los plugins te permiten extender CodeflowX sin modificar el código core. Puedes crear plugins simples (solo hooks) o plugins completos (procesos, pantallas, reglas).

---

## 🎯 ¿Qué es un Plugin?

Un plugin es un archivo `.cfx-plugin` (ZIP) que contiene código, configuración y recursos que extienden CodeflowX.

**Tipos de Plugin:**
1. **Plugin Simple (HOOKS_ONLY)** - Solo implementa hooks MCP
2. **Plugin Completo (FULL_EXTENSION)** - Procesos BPMN + Pantallas ZKoss + Reglas Drools

---

## 🚀 Crear tu Primer Plugin

### Paso 1: Crear Estructura

```
mi-plugin/
├── manifest.json
├── code/
│   └── plugin.py
└── README.md
```

### Paso 2: Escribir manifest.json

```json
{
  "name": "mi-primer-plugin",
  "version": "1.0.0",
  "author": "Tu Nombre",
  "description": "Mi primer plugin para CodeflowX",
  "type": "HOOKS_ONLY",
  "hooks": ["preInvoke"],
  "capabilities": {
    "language": "python",
    "domains": ["validation"]
  }
}
```

### Paso 3: Implementar Hook

```python
# code/plugin.py
def pre_invoke(payload):
    """Hook que se ejecuta antes de invocar modelo/agente"""
    prompt = payload.get("prompt", "")

    # Validar prompt
    if "spam" in prompt.lower():
        return {
            "allowed": False,
            "reason": "Prompt contiene spam"
        }

    # Permitir invocación
    return {
        "allowed": True
    }
```

### Paso 4: Empaquetar

```bash
zip -r mi-primer-plugin-v1.0.0.cfx-plugin \
    manifest.json \
    code/ \
    README.md
```

### Paso 5: Subir a Developer Console

1. Accede a Developer Console
2. Ve a la pestaña **"Plugins"**
3. Haz clic en **"Registrar Plugin"**
4. Sube el archivo `.cfx-plugin`
5. El plugin se registra automáticamente

---

## 🔌 Plugin Simple (HOOKS_ONLY)

### Hooks Disponibles

#### preInvoke
Se ejecuta **antes** de invocar modelo/agente.

```python
def pre_invoke(payload):
    prompt = payload["prompt"]
    context = payload["context"]

    # Validar
    if contains_spam(prompt):
        return {"allowed": False, "reason": "Contiene spam"}

    # Transformar
    enhanced_prompt = f"[Validated] {prompt}"

    return {
        "allowed": True,
        "transformed_prompt": enhanced_prompt
    }
```

#### postInvoke
Se ejecuta **después** de invocar modelo/agente.

```python
def post_invoke(payload):
    response = payload["response"]

    # Filtrar contenido
    filtered = filter_toxic_content(response)

    return {
        "transformed_response": filtered
    }
```

#### onPolicyViolation
Se ejecuta cuando se detecta violación de política.

```python
def on_policy_violation(payload):
    violation_type = payload["violation_type"]
    severity = payload["severity"]

    if severity == "CRITICAL":
        # Bloquear inmediatamente
        return {"should_block": True}

    # Enviar alerta
    send_alert(f"Violación: {violation_type}")

    return {"should_block": False}
```

#### onTelemetry
Se ejecuta para procesar telemetría.

```python
def on_telemetry(payload):
    metrics = payload["metrics"]

    # Enriquecer
    enriched = {
        **metrics,
        "region": get_user_region(),
        "tier": get_user_tier()
    }

    return {"enriched_metrics": enriched}
```

#### onOnboard
Se ejecuta cuando se registra un componente.

```python
def on_onboard(payload):
    component_uuid = payload["component_uuid"]

    # Enviar notificación
    send_slack_notification(f"Nuevo componente: {component_uuid}")

    return {"success": True}
```

---

## 🔧 Plugin Completo (FULL_EXTENSION)

Para crear plugins con procesos, pantallas y reglas, ver:
- [Plugins Completos](PLUGINS_COMPLETOS.md)
- [Ejemplo Completo](../EJEMPLO_PLUGIN_COMPLETO.md)

---

## 📝 Estructura de manifest.json

### Campos Requeridos

- `name` - Nombre único del plugin
- `version` - Versión semver (ej: "1.0.0")
- `author` - Nombre del autor
- `description` - Descripción del plugin
- `type` - "HOOKS_ONLY" | "FULL_EXTENSION"
- `hooks` - Array de hooks implementados
- `capabilities` - Objeto con capacidades
  - `language` - "python" | "typescript" | "javascript" | "java"
  - `domains` - Array de dominios

### Campos Opcionales

- `signature` - Firma digital (recomendado)
- `signature_algorithm` - Algoritmo (default: "HMAC-SHA256")

---

## 🎨 Ejemplos de Plugins

### Validador de Prompts

```python
# code/plugin.py
def pre_invoke(payload):
    prompt = payload["prompt"]

    # Validar longitud
    if len(prompt) > 10000:
        return {"allowed": False, "reason": "Prompt demasiado largo"}

    # Validar palabras prohibidas
    forbidden = ["spam", "scam", "phishing"]
    for word in forbidden:
        if word in prompt.lower():
            return {"allowed": False, "reason": f"Contiene palabra prohibida: {word}"}

    return {"allowed": True}
```

### Traductor Automático

```python
# code/plugin.py
def pre_invoke(payload):
    prompt = payload["prompt"]
    context = payload["context"]

    # Detectar idioma y traducir
    if context.get("user_language") == "es":
        translated = translate_to_english(prompt)
        return {
            "allowed": True,
            "transformed_prompt": translated
        }

    return {"allowed": True}
```

### Filtro de Contenido

```python
# code/plugin.py
def post_invoke(payload):
    response = payload["response"]

    # Filtrar contenido tóxico
    if is_toxic(response):
        return {
            "transformed_response": "Lo siento, no puedo generar esa respuesta.",
            "metadata": {"filtered": True}
        }

    return {"transformed_response": response}
```

---

## 🔒 Seguridad

### Firma Digital

Los plugins pueden incluir firma digital para verificación:

```json
{
  "signature": "abc123...",
  "signature_algorithm": "HMAC-SHA256"
}
```

### Sandbox

Los plugins se ejecutan en modo sandbox:
- Contenedor aislado
- Sin acceso a datos sensibles
- Timeout configurable
- Recursos limitados

---

## 📚 Más Información

- [Cómo Funcionan los Plugins](COMO_FUNCIONAN.md)
- [Plugin Model](PLUGIN_MODEL.md)
- [Plugins Completos](PLUGINS_COMPLETOS.md)
- [Ejemplos](EJEMPLOS.md)

---

## ❓ FAQ

**P: ¿Puedo actualizar un plugin?**
R: Sí, sube una nueva versión con el mismo nombre y versión mayor.

**P: ¿Los plugins afectan el rendimiento?**
R: Los hooks tienen timeout configurable (default: 5s). Si exceden, se ignoran.

**P: ¿Puedo desactivar un plugin?**
R: Sí, desde Developer Console puedes desactivar/activar plugins.

---

**Siguiente:** [Cómo Funcionan los Plugins](COMO_FUNCIONAN.md)
