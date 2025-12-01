# Troubleshooting - Solución de Problemas

## Errores Comunes

### "API key is required"
**Causa:** API key no configurada
**Solución:**
```python
# Verificar variable de entorno
import os
print(os.getenv("CODEFLOWX_API_KEY"))

# O pasar directamente
client = AgentClient(api_key="cfx_sk_live_...")
```

### "Network error" / "Connection timeout"
**Causa:** Problemas de conectividad
**Solución:**
- Verifica URL base: `https://gateway.codeflowx.ai`
- Aumenta timeout: `Config(timeout_seconds=60)`
- Verifica firewall/proxy

### "Rate limit exceeded"
**Causa:** Demasiadas requests
**Solución:**
- Implementa retry con backoff
- Usa batch endpoints cuando sea posible
- Límite: 100 req/s

### "Invalid workspace UUID"
**Causa:** UUID incorrecto
**Solución:**
- Obtén UUID desde Developer Console
- Verifica formato UUID válido

### "Policy violation"
**Causa:** Acción bloqueada por políticas
**Solución:**
- Revisa políticas en Developer Console
- Verifica contexto enviado

## Plugins

### Plugin no se carga
**Solución:**
- Verifica estructura del ZIP
- Valida manifest.json
- Revisa logs en Developer Console

### Hook no se ejecuta
**Solución:**
- Verifica que hook esté en manifest.json
- Revisa timeout (default: 5s)
- Verifica logs de ejecución

## Más Ayuda

- [FAQ](FAQ.md)
- [API Reference](SDKS/API_REFERENCE.md)
- Email: dev@codeflowx.com
