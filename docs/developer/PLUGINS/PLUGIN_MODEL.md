# Plugin Model - CodeflowX

## Formato .cfx-plugin

Un plugin de CodeflowX es un archivo ZIP con extensión `.cfx-plugin` que contiene:

```
plugin-name-v1.0.0.cfx-plugin (ZIP)
├── manifest.json          ← Manifest con metadata del plugin
├── code/                  ← Código del plugin
│   └── plugin.py (o .js, .java)
├── tests/                 ← Tests del plugin
│   └── test_plugin.py
└── README.md              ← Documentación del plugin
```

## manifest.json

### Plugin Simple (HOOKS_ONLY)

```json
{
  "name": "plugin-name",
  "version": "1.0.0",
  "author": "Partner Name",
  "description": "Plugin description",
  "type": "HOOKS_ONLY",
  "hooks": ["onOnboard", "preInvoke", "postInvoke"],
  "capabilities": {
    "language": "python",
    "domains": ["customer_service"]
  },
  "signature": "base64-encoded-signature",
  "signature_algorithm": "HMAC-SHA256"
}
```

### Plugin Completo (FULL_EXTENSION)

```json
{
  "name": "plugin-completo",
  "version": "1.0.0",
  "author": "Partner Name",
  "description": "Plugin con procesos, pantallas y reglas",
  "type": "FULL_EXTENSION",
  "hooks": ["preInvoke"],
  "capabilities": {
    "language": "python",
    "domains": ["approval"]
  },
  "processes": [
    {
      "file": "processes/mi-proceso.bpmn",
      "process_id": "mi-proceso",
      "delegates": {
        "serviceTask1": "com.partner.delegates.MiDelegate"
      }
    }
  ],
  "screens": [
    {
      "file": "ui/zul/mi-pantalla.zul",
      "route": "/partner/mi-pantalla",
      "viewmodel": "com.partner.viewmodels.MiViewModel"
    }
  ],
  "rules": [
    {
      "file": "rules/mi-reglas.drl",
      "package": "com.partner.rules",
      "kbase": "partner-rules"
    }
  ],
  "signature": "base64-encoded-signature",
  "signature_algorithm": "HMAC-SHA256"
}
```

### Campos Requeridos

- `name`: Nombre único del plugin
- `version`: Versión semver (ej: "1.0.0")
- `author`: Nombre del autor/partner
- `description`: Descripción del plugin
- `type`: "HOOKS_ONLY" | "FULL_EXTENSION" (default: "HOOKS_ONLY")
- `hooks`: Array de hooks soportados
- `capabilities`: Objeto con capacidades
  - `language`: "python" | "typescript" | "javascript" | "java"
  - `domains`: Array de dominios de aplicación

### Campos Opcionales (Solo FULL_EXTENSION)

- `processes`: Array de procesos BPMN
  - `file`: Ruta al archivo .bpmn
  - `process_id`: ID del proceso
  - `delegates`: Mapeo de service tasks a delegates
- `screens`: Array de pantallas ZKoss
  - `file`: Ruta al archivo .zul
  - `route`: Ruta URL de la pantalla
  - `viewmodel`: Clase ViewModel (opcional)
- `rules`: Array de reglas Drools
  - `file`: Ruta al archivo .drl
  - `package`: Paquete Drools
  - `kbase`: Nombre del KieBase
- `signature`: Firma del plugin (obligatorio para FULL_EXTENSION)
- `signature_algorithm`: Algoritmo de firma (default: "HMAC-SHA256")

## Ejemplo de Plugin Python

### manifest.json
```json
{
  "name": "customer-service-validator",
  "version": "1.0.0",
  "author": "Acme Corp",
  "description": "Validador de prompts para customer service",
  "hooks": ["preInvoke", "postInvoke"],
  "capabilities": {
    "language": "python",
    "domains": ["customer_service"]
  }
}
```

### code/plugin.py
```python
def pre_invoke(payload):
    """
    Hook preInvoke - Valida prompt antes de invocar
    """
    prompt = payload.get("prompt", "")
    context = payload.get("context", {})

    # Validar que el prompt no contenga palabras prohibidas
    forbidden_words = ["spam", "scam"]
    for word in forbidden_words:
        if word.lower() in prompt.lower():
            return {
                "allowed": False,
                "reason": f"Prompt contiene palabra prohibida: {word}"
            }

    # Transformar prompt (ej: agregar contexto)
    transformed_prompt = f"[Customer Service] {prompt}"

    return {
        "allowed": True,
        "transformed_prompt": transformed_prompt,
        "metadata": {
            "validated_by": "customer-service-validator"
        }
    }

def post_invoke(payload):
    """
    Hook postInvoke - Procesa respuesta
    """
    response = payload.get("response", "")

    # Agregar disclaimer
    transformed_response = f"{response}\n\n[Este mensaje fue procesado por Customer Service Validator]"

    return {
        "transformed_response": transformed_response
    }
```

## Registro de Plugin

1. **Subir .cfx-plugin** a Developer Console
2. **Validar manifest.json** y firma
3. **Extraer código** a directorio sandbox
4. **Crear AioMarketplaceEntry** con metadata
5. **Crear AioComponent** con type="MCP"
6. **Registrar hooks** en sistema de eventos

## Sandbox

Los plugins se ejecutan en modo sandbox:
- Contenedor aislado
- Sin acceso a datos sensibles
- Timeout configurable
- Recursos limitados

## Seguridad

- Validación de firma HMAC
- Ejecución en sandbox
- Revisión de código (opcional)
- Whitelist de autores
