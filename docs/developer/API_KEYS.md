# Gestión de API Keys

Guía para generar, usar y gestionar API keys en CodeflowX.

---

## 🔑 Generar API Key

### Desde Developer Console

1. Accede a **Developer Console**
2. Ve a pestaña **"API Keys"**
3. Haz clic en **"Generar API Key"**
4. Copia la key (solo se muestra una vez)

### Tipos de Keys

- **API Key Principal:** `cfx_sk_live_...` - Para registro de agentes
- **Agent Token:** `cfx_agent_...` - Para operaciones del agente (generado automáticamente)

---

## 🔒 Seguridad

### Almacenar Keys

**❌ NO:**
```python
# NUNCA hardcodear
client = AgentClient(api_key="cfx_sk_live_abc123...")
```

**✅ SÍ:**
```python
# Usar variables de entorno
import os
client = AgentClient(api_key=os.getenv("CODEFLOWX_API_KEY"))
```

### Rotación

- Rotar keys periódicamente
- Revocar keys comprometidas inmediatamente
- Usar diferentes keys por entorno (dev/prod)

---

## 🔄 Revocar Key

1. Ve a Developer Console → API Keys
2. Selecciona key a revocar
3. Haz clic en **"Revocar"**
4. La key deja de funcionar inmediatamente

---

## 📊 Límites

- **Rate Limit:** 100 requests/segundo por key
- **Máximo de keys:** 10 por workspace
- **Expiración:** No expiran (revocar manualmente)

---

**Más:** [Developer Console](DEVELOPER_CONSOLE.md)
