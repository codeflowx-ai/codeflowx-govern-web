# Plugins Completos - Procesos, Pantallas y Reglas

Los plugins completos te permiten añadir procesos BPMN, pantallas ZKoss y reglas Drools a CodeflowX.

---

## 🎯 ¿Qué es un Plugin Completo?

Un plugin completo (`FULL_EXTENSION`) puede incluir:
- ✅ **Procesos BPMN** - Workflows completos
- ✅ **Pantallas ZKoss** - Interfaces de usuario
- ✅ **Reglas Drools** - Reglas de negocio
- ✅ **Hooks MCP** - Puntos de interceptación

---

## 📦 Estructura

```
plugin-completo.zip
├── manifest.json
├── code/plugin.py
├── processes/
│   └── mi-proceso.bpmn
├── delegates/
│   └── MiDelegate.java
├── ui/
│   ├── zul/
│   │   └── mi-pantalla.zul
│   └── viewmodels/
│       └── MiViewModel.java
├── rules/
│   └── mi-reglas.drl
├── facts/
│   └── MiFact.java
└── README.md
```

---

## 📝 manifest.json Completo

```json
{
  "name": "partner-approval-plugin",
  "version": "1.0.0",
  "author": "Acme Corp",
  "description": "Plugin con proceso de aprobación",
  "type": "FULL_EXTENSION",
  "hooks": ["preInvoke"],
  "capabilities": {
    "language": "python",
    "domains": ["approval"]
  },
  "processes": [
    {
      "file": "processes/partner-approval.bpmn",
      "process_id": "partner-approval",
      "delegates": {
        "notifyTask": "com.acme.delegates.PartnerNotifyDelegate"
      }
    }
  ],
  "screens": [
    {
      "file": "ui/zul/review-request.zul",
      "route": "/partner/review-request",
      "viewmodel": "com.acme.viewmodels.ReviewRequestViewModel"
    }
  ],
  "rules": [
    {
      "file": "rules/partner-policy.drl",
      "package": "com.acme.rules",
      "kbase": "partner-rules"
    }
  ],
  "signature": "abc123...",
  "signature_algorithm": "HMAC-SHA256"
}
```

---

## 🔄 Procesos BPMN

### Crear Proceso

```xml
<!-- processes/partner-approval.bpmn -->
<process id="partner-approval" name="Partner Approval">
    <startEvent id="start"/>
    <userTask id="review" name="Review Request"/>
    <serviceTask id="notify"
                 flowable:delegateExpression="${partnerNotifyDelegate}"/>
    <endEvent id="end"/>
</process>
```

### Java Delegate

```java
// delegates/PartnerNotifyDelegate.java
@Component("partnerNotifyDelegate")
public class PartnerNotifyDelegate implements JavaDelegate {
    public void execute(DelegateExecution execution) {
        // Tu lógica aquí
    }
}
```

---

## 🖥️ Pantallas ZKoss

### Crear Pantalla

```xml
<!-- ui/zul/review-request.zul -->
<window title="Review Request"
        apply="com.acme.viewmodels.ReviewRequestViewModel">
    <!-- Tu UI aquí -->
</window>
```

### ViewModel

```java
// ui/viewmodels/ReviewRequestViewModel.java
public class ReviewRequestViewModel {
    @Command
    public void approve() {
        // Lógica de aprobación
    }
}
```

---

## 📜 Reglas Drools

### Crear Reglas

```drl
// rules/partner-policy.drl
package com.acme.rules;

rule "Auto Approve Low Risk"
    when
        Request(amount < 1000)
    then
        request.setStatus("APPROVED");
end
```

### Fact

```java
// facts/PartnerRequest.java
@Data
public class PartnerRequest {
    private String requestId;
    private Double amount;
    private String status;
}
```

---

## 🚀 Cómo Usar

1. **Crear plugin** con estructura completa
2. **Empaquetar** en `.cfx-plugin`
3. **Subir** a Developer Console
4. **El sistema carga automáticamente:**
   - ✅ Proceso BPMN disponible
   - ✅ Pantalla disponible en ruta especificada
   - ✅ Reglas disponibles en kbase
   - ✅ Hooks activos

---

## 📚 Más Información

- [Ejemplo Completo](../EJEMPLO_PLUGIN_COMPLETO.md)
- [Plugin Model](PLUGIN_MODEL.md)
- [Guía de Plugins](GUIA_PLUGINS.md)
