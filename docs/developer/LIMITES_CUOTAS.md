# Límites y Cuotas - CodeflowX

Límites y cuotas del sistema CodeflowX.

---

## 📊 Límites de API

### Rate Limiting
- **Límite:** 100 requests/segundo por API key
- **Burst:** 200 requests
- **Header:** `Retry-After` en respuesta 429

### Timeouts
- **Request timeout:** 30 segundos (configurable)
- **Plugin hook timeout:** 5 segundos (configurable)

---

## 💾 Límites de Memoria

- **Tamaño por key:** 1 MB
- **Máximo de keys:** 1000 por agente
- **TTL máximo:** 365 días

---

## 🔌 Límites de Plugins

- **Plugins activos:** 50 por workspace
- **Tamaño máximo:** 100 MB por plugin
- **Hooks por plugin:** Ilimitado

---

## 📈 Cuotas por Plan

### Free
- 10,000 requests/mes
- 1 workspace
- 5 plugins

### Pro
- 100,000 requests/mes
- 10 workspaces
- 50 plugins

### Enterprise
- Ilimitado
- Ilimitado
- Ilimitado

---

## ⚠️ Exceder Límites

Si excedes límites:
- Recibirás error 429 (Rate Limit)
- Header `Retry-After` indica cuándo reintentar
- Implementa retry con backoff

---

**Más:** [Troubleshooting](TROUBLESHOOTING.md)
