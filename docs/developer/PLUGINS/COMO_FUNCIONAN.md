# Cómo Funcionan los Plugins

Explicación detallada de cómo funcionan los plugins en CodeflowX.

---

## 🔄 Flujo Sin Plugin

```
Usuario → CodeflowX → Modelo/Agente → Respuesta → Usuario
```

**Ejemplo:**
1. Usuario: "¿Cuál es el precio de X?"
2. CodeflowX invoca modelo
3. Modelo: "El precio es $100"
4. Usuario recibe respuesta

---

## 🔌 Flujo Con Plugin

```
Usuario → CodeflowX → [PLUGIN preInvoke] → Modelo → [PLUGIN postInvoke] → Respuesta → Usuario
```

**Ejemplo con Plugin:**
1. Usuario: "¿Cuál es el precio de X?"
2. CodeflowX llama **Plugin preInvoke**:
   - Plugin valida el prompt
   - Plugin transforma: "[Customer Service] ¿Cuál es el precio de X?"
   - Plugin permite la invocación
3. Modelo recibe prompt transformado
4. Modelo: "El precio es $100"
5. CodeflowX llama **Plugin postInvoke**:
   - Plugin agrega disclaimer
   - Plugin retorna: "El precio es $100\n\n[Validado por Customer Service Plugin]"
6. Usuario recibe respuesta transformada

---

## 🎣 Hooks: Puntos de Interceptación

Los plugins se "enganchan" en momentos específicos:

### 1. onOnboard
**Cuándo:** Al registrar un componente
**Uso:** Notificaciones, inicialización

### 2. preInvoke
**Cuándo:** Antes de invocar modelo/agente
**Uso:** Validación, transformación de prompts

### 3. postInvoke
**Cuándo:** Después de invocar modelo/agente
**Uso:** Filtrado, transformación de respuestas

### 4. onPolicyViolation
**Cuándo:** Cuando se detecta violación
**Uso:** Alertas, acciones personalizadas

### 5. onTelemetry
**Cuándo:** Antes de almacenar telemetría
**Uso:** Enriquecimiento, filtrado

---

## 📦 Estructura de Plugin

```
plugin.zip
├── manifest.json    ← Metadata
├── code/
│   └── plugin.py   ← Código del plugin
└── README.md
```

### manifest.json
```json
{
  "name": "mi-plugin",
  "version": "1.0.0",
  "hooks": ["preInvoke", "postInvoke"]
}
```

### code/plugin.py
```python
def pre_invoke(payload):
    # Tu lógica aquí
    return {"allowed": True}

def post_invoke(payload):
    # Tu lógica aquí
    return {"transformed_response": "..."}
```

---

## 🔀 Flujo Completo

```
1. Usuario envía prompt
   ↓
2. CodeflowX verifica plugins registrados
   ↓
3. Ejecuta Plugin.preInvoke()
   ├─ Si allowed=false → BLOQUEA
   └─ Si allowed=true → Continúa con prompt transformado
   ↓
4. Invoca modelo/agente
   ↓
5. Modelo genera respuesta
   ↓
6. Ejecuta Plugin.postInvoke()
   └─ Transforma respuesta si es necesario
   ↓
7. Retorna respuesta al usuario
```

---

## 🆚 SDKs vs Plugins

| Aspecto | SDKs | Plugins |
|---------|------|---------|
| **Dónde se ejecuta** | En tu app | En CodeflowX |
| **Conexión** | API REST | Hooks internos |
| **Caso de uso** | Integrar agentes | Extender funcionalidad |
| **Control** | Tú controlas | CodeflowX controla |

---

## 📚 Más Información

- [Guía de Plugins](GUIA_PLUGINS.md)
- [Plugin Model](PLUGIN_MODEL.md)
- [Ejemplos](EJEMPLOS.md)
