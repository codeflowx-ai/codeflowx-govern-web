# Códigos de Error - CodeflowX

Referencia completa de códigos de error y cómo manejarlos.

---

## 🔴 Errores HTTP

### 400 Bad Request
**Causa:** Request inválido
**Solución:** Verifica formato de payload

### 401 Unauthorized
**Causa:** API key inválida o expirada
**Solución:** Verifica API key o genera nueva

### 403 Forbidden
**Causa:** Política violada o sin permisos
**Solución:** Revisa políticas o permisos

### 404 Not Found
**Causa:** Recurso no encontrado
**Solución:** Verifica UUID o ID

### 429 Rate Limit Exceeded
**Causa:** Demasiadas requests
**Solución:** Implementa retry con backoff

### 500 Internal Server Error
**Causa:** Error del servidor
**Solución:** Reintenta o contacta soporte

### 502 Bad Gateway
**Causa:** Problema de red
**Solución:** Reintenta más tarde

### 503 Service Unavailable
**Causa:** Servicio no disponible
**Solución:** Reintenta más tarde

---

## ⚠️ Errores de SDK

### AuthenticationError
**Causa:** API key inválida
**Solución:** Verifica API key

### NetworkError
**Causa:** Problema de conectividad
**Solución:** Verifica conexión

### RateLimitError
**Causa:** Rate limit excedido
**Solución:** Espera y reintenta

### ValidationError
**Causa:** Datos inválidos
**Solución:** Verifica formato

### PolicyViolationError
**Causa:** Política violada
**Solución:** Revisa políticas

---

## 🔧 Manejo de Errores

### Python
```python
from codeflowx_sdk.exceptions import (
    AuthenticationError,
    RateLimitError,
    PolicyViolationError
)

try:
    result = client.policy_check(...)
except AuthenticationError:
    # Manejar autenticación
except RateLimitError as e:
    # Esperar y reintentar
    time.sleep(e.retry_after)
except PolicyViolationError as e:
    # Manejar violación
    print(f"Violación: {e.violations}")
```

### TypeScript
```typescript
import {
    AuthenticationError,
    RateLimitError,
    PolicyViolationError
} from 'codeflowx-sdk-typescript';

try {
    const result = await client.policyCheck(...);
} catch (error) {
    if (error instanceof AuthenticationError) {
        // Manejar autenticación
    } else if (error instanceof RateLimitError) {
        // Esperar y reintentar
        await sleep(error.retryAfter);
    } else if (error instanceof PolicyViolationError) {
        // Manejar violación
        console.log('Violación:', error.violations);
    }
}
```

---

**Más:** [Troubleshooting](TROUBLESHOOTING.md)
