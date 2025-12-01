# Ejemplo: Plugin Completo con Procesos, Pantallas y Reglas

## Estructura del Plugin

```
mi-plugin-completo-v1.0.0.cfx-plugin (ZIP)
├── manifest.json
├── code/
│   └── plugin.py
├── processes/
│   └── partner-approval.bpmn
├── delegates/
│   └── PartnerNotifyDelegate.java
├── ui/
│   ├── zul/
│   │   └── review-request.zul
│   └── viewmodels/
│       └── ReviewRequestViewModel.java
├── rules/
│   └── partner-policy.drl
├── facts/
│   └── PartnerRequest.java
└── README.md
```

---

## manifest.json

```json
{
  "name": "partner-approval-plugin",
  "version": "1.0.0",
  "author": "Acme Corp",
  "description": "Plugin completo con proceso de aprobación, pantalla y reglas",
  "type": "FULL_EXTENSION",
  "hooks": ["preInvoke"],
  "capabilities": {
    "language": "python",
    "domains": ["approval", "governance"]
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

## 1. Proceso BPMN

### processes/partner-approval.bpmn

```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
             xmlns:flowable="http://flowable.org/bpmn"
             targetNamespace="http://acme.com/bpmn">

    <process id="partner-approval" name="Partner Approval Process" isExecutable="true">

        <startEvent id="start" name="Start"/>

        <userTask id="reviewTask" name="Review Request"
                  flowable:assignee="${reviewer}"
                  flowable:formKey="partner:review-request">
            <documentation>Revisar solicitud del partner</documentation>
        </userTask>

        <serviceTask id="notifyTask" name="Notify Partner"
                     flowable:delegateExpression="${partnerNotifyDelegate}">
        </serviceTask>

        <exclusiveGateway id="decision" name="Approved?"/>

        <endEvent id="approved" name="Approved"/>
        <endEvent id="rejected" name="Rejected"/>

        <sequenceFlow id="flow1" sourceRef="start" targetRef="reviewTask"/>
        <sequenceFlow id="flow2" sourceRef="reviewTask" targetRef="notifyTask"/>
        <sequenceFlow id="flow3" sourceRef="notifyTask" targetRef="decision"/>
        <sequenceFlow id="flow4" sourceRef="decision" targetRef="approved">
            <conditionExpression>${approved == true}</conditionExpression>
        </sequenceFlow>
        <sequenceFlow id="flow5" sourceRef="decision" targetRef="rejected">
            <conditionExpression>${approved == false}</conditionExpression>
        </sequenceFlow>

    </process>
</definitions>
```

---

## 2. Java Delegate

### delegates/PartnerNotifyDelegate.java

```java
package com.acme.delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("partnerNotifyDelegate")
public class PartnerNotifyDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        String partnerName = (String) execution.getVariable("partnerName");
        String requestId = (String) execution.getVariable("requestId");

        log.info("Notificando partner {} sobre solicitud {}", partnerName, requestId);

        // Lógica de notificación
        sendNotification(partnerName, requestId);

        execution.setVariable("notified", true);
    }

    private void sendNotification(String partner, String requestId) {
        // Implementar notificación
    }
}
```

---

## 3. Pantalla ZKoss

### ui/zul/review-request.zul

```xml
<?xml version="1.0" encoding="UTF-8"?>
<window title="Review Partner Request"
        width="600px"
        apply="com.acme.viewmodels.ReviewRequestViewModel"
        xmlns="http://www.zkoss.org/2005/zul">

    <vbox>
        <label value="Partner Request Review" sclass="h2"/>

        <grid>
            <rows>
                <row>
                    <label value="Partner:"/>
                    <label value="@load(vm.request.partnerName)"/>
                </row>
                <row>
                    <label value="Request ID:"/>
                    <label value="@load(vm.request.requestId)"/>
                </row>
                <row>
                    <label value="Description:"/>
                    <textbox value="@load(vm.request.description)"
                             rows="5"
                             readonly="true"/>
                </row>
            </rows>
        </grid>

        <separator/>

        <hbox>
            <button label="Approve"
                    onClick="@command('approve')"
                    sclass="btn-primary"/>
            <button label="Reject"
                    onClick="@command('reject')"
                    sclass="btn-danger"/>
        </hbox>
    </vbox>
</window>
```

### ui/viewmodels/ReviewRequestViewModel.java

```java
package com.acme.viewmodels;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestViewModel {

    @Wire
    private Window reviewRequestWin;

    private PartnerRequest request;

    @Init
    public void init(@ContextParam(ContextType.EXECUTION) org.flowable.engine.delegate.DelegateExecution execution) {
        // Obtener datos del proceso BPMN
        String requestId = (String) execution.getVariable("requestId");
        // Cargar request...
    }

    @Command
    public void approve() {
        // Aprobar solicitud
        // Continuar proceso BPMN
    }

    @Command
    public void reject() {
        // Rechazar solicitud
        // Continuar proceso BPMN
    }
}
```

---

## 4. Reglas Drools

### rules/partner-policy.drl

```drl
package com.acme.rules;

import com.acme.facts.PartnerRequest;

global org.slf4j.Logger logger;

dialect "mvel"

rule "Auto Approve Low Risk"
    when
        $request : PartnerRequest(
            riskLevel == "LOW",
            amount < 1000
        )
    then
        logger.info("Auto-aprobando solicitud de bajo riesgo: {}", $request.getRequestId());
        $request.setStatus("AUTO_APPROVED");
        $request.setApproved(true);
end

rule "Require Manual Review High Risk"
    when
        $request : PartnerRequest(
            riskLevel == "HIGH",
            amount >= 10000
        )
    then
        logger.info("Requerir revisión manual para solicitud de alto riesgo: {}", $request.getRequestId());
        $request.setStatus("MANUAL_REVIEW_REQUIRED");
        $request.setApproved(false);
end

rule "Reject Invalid Request"
    when
        $request : PartnerRequest(
            partnerName == null || partnerName.isEmpty()
        )
    then
        logger.warn("Rechazando solicitud inválida: {}", $request.getRequestId());
        $request.setStatus("REJECTED");
        $request.setApproved(false);
end
```

---

## 5. Fact para Drools

### facts/PartnerRequest.java

```java
package com.acme.facts;

import lombok.Data;

@Data
public class PartnerRequest {
    private String requestId;
    private String partnerName;
    private String description;
    private Double amount;
    private String riskLevel;
    private String status;
    private Boolean approved;
}
```

---

## 6. Hook MCP (Opcional)

### code/plugin.py

```python
def pre_invoke(payload):
    """Hook que valida antes de invocar"""
    prompt = payload.get("prompt", "")

    # Validar con reglas del plugin
    if "spam" in prompt.lower():
        return {"allowed": False, "reason": "Contiene spam"}

    return {"allowed": True}
```

---

## Cómo Usar el Plugin

1. **Empaquetar:**
```bash
zip -r partner-approval-plugin-v1.0.0.cfx-plugin \
    manifest.json \
    code/ \
    processes/ \
    delegates/ \
    ui/ \
    rules/ \
    facts/
```

2. **Subir a Developer Console:**
   - Ir a Developer Console
   - Tab "Plugins"
   - Subir archivo `.cfx-plugin`

3. **El sistema automáticamente:**
   - ✅ Valida manifest
   - ✅ Despliega proceso BPMN en Flowable
   - ✅ Copia pantalla ZKoss a webapp
   - ✅ Carga reglas Drools en KieBase
   - ✅ Registra hooks MCP

4. **Usar:**
   - Proceso BPMN disponible: `partner-approval`
   - Pantalla disponible: `/partner/review-request`
   - Reglas disponibles en kbase: `partner-rules`
   - Hooks activos: `preInvoke`

---

## Ventajas del Plugin Completo

✅ **Extensión completa** - No solo hooks, sino workflows, UI y reglas
✅ **Aislamiento** - Todo en un paquete
✅ **Versionado** - Cada plugin tiene su versión
✅ **Reutilizable** - Mismo plugin en múltiples workspaces
✅ **Mantenible** - Fácil actualizar sin tocar core
