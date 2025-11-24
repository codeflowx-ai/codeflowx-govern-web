# PROMPT: INC-HITL-008 - Campo de IP/Origen en Solicitudes de Aprobación

**Incidencia:** INC-HITL-008  
**Prioridad:** 🟡 MEDIUM  
**Artículo EU AI Act:** ISO 42001 (Audit trails)  
**Esfuerzo Estimado:** 2-3 días  
**Tipo:** Java - Backend  
**Estado:** ✅ COMPLETADO

---

## DESCRIPCIÓN

Las solicitudes de aprobación no registran la dirección IP u origen de la solicitud, lo que limita la capacidad de auditoría y detección de anomalías. Se requiere agregar campos `AGTSOURCEIP` y `AGTUSERAGENT` en entidades de aprobación.

---

## REQUISITOS

1. Agregar campos `AGTSOURCEIP`, `AGTUSERAGENT` en entidades de aprobación
2. Capturar información de origen en interceptores/auditores de JPA
3. Registrar información de sesión (si aplica)
4. Incluir en reportes de auditoría

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Agregar Campos en Entidades

**Archivo:** Modificar `AgentApproval.java`, `ModelApproval.java`, `PromptApproval.java`

```java
@Column(name = "AGTSOURCEIP", length = 45)
@Field(criteria = true, auditar = false, filter = true, label = "Source IP", type = "STRING")
private String agtsourceip;

@Column(name = "AGTUSERAGENT", columnDefinition = "TEXT")
@Field(criteria = true, auditar = false, filter = true, label = "User Agent", type = "CLOB")
private String agtuseragent;
```

### 2. Interceptor para Captura Automática

**Archivo:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/interceptor/SourceInfoInterceptor.java`

```java
package com.codeflowx.govern.interceptor;

import com.codeflowx.govern.entity.agents.AgentApproval;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Component
public class SourceInfoInterceptor extends EmptyInterceptor {
    
    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (entity instanceof AgentApproval) {
            captureSourceInfo((AgentApproval) entity, state, propertyNames);
        }
        return false;
    }
    
    private void captureSourceInfo(AgentApproval approval, Object[] state, String[] propertyNames) {
        HttpServletRequest request = getCurrentRequest();
        if (request != null) {
            for (int i = 0; i < propertyNames.length; i++) {
                if ("agtsourceip".equals(propertyNames[i])) {
                    state[i] = getClientIpAddress(request);
                } else if ("agtuseragent".equals(propertyNames[i])) {
                    state[i] = request.getHeader("User-Agent");
                }
            }
        }
    }
    
    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = 
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        return request.getRemoteAddr();
    }
}
```

### 3. Uso en BusinessService

**Archivo:** Modificar `AgentApprovalBusinessService.java`

```java
public AgentApproval createApproval(AgentApprovalDto dto, HttpServletRequest request) {
    AgentApproval approval = new AgentApproval();
    // ... mapeo de campos ...
    
    if (request != null) {
        approval.setAgtsourceip(getClientIpAddress(request));
        approval.setAgtuseragent(request.getHeader("User-Agent"));
    }
    
    return repository.save(approval);
}
```

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md#inc-hitl-008`
- **ISO 42001:** Audit trails

---

**Estado:** ✅ COMPLETADO  
**Esfuerzo Estimado:** 2-3 días  
**Responsable:** Backend Team  
**Fecha Límite:** 2 meses

