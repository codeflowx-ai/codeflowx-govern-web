# Plugins: Procesos, Pantallas y Reglas

## ❓ PREGUNTA: ¿Los plugins pueden añadir procesos, pantallas y reglas?

### **Respuesta Corta:**
- **Plugins actuales (MCP hooks):** ❌ NO pueden añadir procesos, pantallas ni reglas
- **Solo implementan hooks simples:** preInvoke, postInvoke, etc.
- **Para procesos/pantallas/reglas:** Necesitas extender el sistema

---

## 🔍 QUÉ PUEDEN HACER LOS PLUGINS ACTUALES

### **Solo Hooks (MCP)**
Los plugins actuales solo pueden implementar **hooks** que se ejecutan en momentos específicos:

```python
# Plugin actual - Solo hooks
def pre_invoke(payload):
    # Validar/transformar prompt
    return {"allowed": True, "transformed_prompt": ...}

def post_invoke(payload):
    # Transformar respuesta
    return {"transformed_response": ...}
```

**Limitaciones:**
- ❌ No pueden crear procesos BPMN
- ❌ No pueden crear pantallas ZKoss
- ❌ No pueden crear reglas Drools
- ❌ Solo interceptan invocaciones de modelos/agentes

---

## 🎯 QUÉ FALTA PARA PROCESOS, PANTALLAS Y REGLAS

Para que partners puedan añadir **procesos BPMN, pantallas ZKoss y reglas Drools**, necesitas extender el sistema de plugins:

### **1. Procesos BPMN** ❌
**Estado actual:** No soportado

**Qué se necesita:**
- Plugin puede incluir archivos `.bpmn`
- Sistema carga procesos BPMN desde plugin
- Plugin puede incluir Java Delegates
- Sistema registra procesos en Flowable/Activiti

**Estructura propuesta:**
```
plugin.zip
├── manifest.json
├── code/
│   └── plugin.py
├── processes/          ← NUEVO
│   └── mi-proceso.bpmn
├── delegates/          ← NUEVO
│   └── MiDelegate.java
└── README.md
```

---

### **2. Pantallas ZKoss** ❌
**Estado actual:** No soportado

**Qué se necesita:**
- Plugin puede incluir archivos `.zul`
- Plugin puede incluir ViewModels Java
- Sistema registra pantallas en rutas del plugin
- Sistema inyecta dependencias Spring

**Estructura propuesta:**
```
plugin.zip
├── manifest.json
├── code/
│   └── plugin.py
├── ui/                 ← NUEVO
│   ├── zul/
│   │   └── mi-pantalla.zul
│   └── viewmodels/
│       └── MiViewModel.java
└── README.md
```

---

### **3. Reglas Drools** ❌
**Estado actual:** No soportado

**Qué se necesita:**
- Plugin puede incluir archivos `.drl`
- Sistema carga reglas en KieBase
- Plugin puede definir Facts (POJOs)
- Sistema registra reglas en paquete del plugin

**Estructura propuesta:**
```
plugin.zip
├── manifest.json
├── code/
│   └── plugin.py
├── rules/              ← NUEVO
│   └── mi-reglas.drl
├── facts/              ← NUEVO
│   └── MiFact.java
└── README.md
```

---

## 🚀 PROPUESTA: PLUGINS EXTENDIDOS

### **Tipos de Plugin**

#### **Tipo 1: Plugin Simple (Actual) - Solo Hooks**
```json
{
  "name": "mi-plugin",
  "type": "HOOKS_ONLY",
  "hooks": ["preInvoke", "postInvoke"]
}
```

**Puede:**
- ✅ Implementar hooks MCP
- ❌ No puede añadir procesos
- ❌ No puede añadir pantallas
- ❌ No puede añadir reglas

---

#### **Tipo 2: Plugin Completo (Propuesto) - Procesos + Pantallas + Reglas**
```json
{
  "name": "mi-plugin-completo",
  "type": "FULL_EXTENSION",
  "hooks": ["preInvoke"],
  "processes": ["mi-proceso.bpmn"],
  "screens": ["mi-pantalla.zul"],
  "rules": ["mi-reglas.drl"]
}
```

**Puede:**
- ✅ Implementar hooks MCP
- ✅ Añadir procesos BPMN
- ✅ Añadir pantallas ZKoss
- ✅ Añadir reglas Drools

---

## 📦 ESTRUCTURA DE PLUGIN COMPLETO

```
mi-plugin-completo-v1.0.0.cfx-plugin (ZIP)
├── manifest.json
│   {
│     "name": "mi-plugin-completo",
│     "type": "FULL_EXTENSION",
│     "hooks": ["preInvoke"],
│     "processes": ["mi-proceso.bpmn"],
│     "screens": ["mi-pantalla.zul"],
│     "rules": ["mi-reglas.drl"]
│   }
│
├── code/
│   └── plugin.py              ← Hooks MCP
│
├── processes/                  ← PROCESOS BPMN
│   └── mi-proceso.bpmn
│
├── delegates/                  ← JAVA DELEGATES
│   └── MiProcesoDelegate.java
│
├── ui/                         ← PANTALLAS ZKOSS
│   ├── zul/
│   │   └── mi-pantalla.zul
│   └── viewmodels/
│       └── MiPantallaViewModel.java
│
├── rules/                      ← REGLAS DROOLS
│   └── mi-reglas.drl
│
├── facts/                      ← FACTS DROOLS
│   └── MiFact.java
│
└── README.md
```

---

## 🔧 IMPLEMENTACIÓN NECESARIA

### **1. Extender manifest.json**

```json
{
  "name": "plugin-name",
  "version": "1.0.0",
  "type": "FULL_EXTENSION",  ← NUEVO
  "hooks": ["preInvoke"],

  "processes": [              ← NUEVO
    {
      "file": "processes/mi-proceso.bpmn",
      "process_id": "mi-proceso",
      "delegates": {
        "serviceTask1": "com.partner.delegates.MiDelegate"
      }
    }
  ],

  "screens": [                 ← NUEVO
    {
      "file": "ui/zul/mi-pantalla.zul",
      "route": "/partner/mi-pantalla",
      "viewmodel": "com.partner.viewmodels.MiViewModel"
    }
  ],

  "rules": [                   ← NUEVO
    {
      "file": "rules/mi-reglas.drl",
      "package": "com.partner.rules",
      "kbase": "partner-rules"
    }
  ]
}
```

---

### **2. Extender PluginLoaderService**

```java
public class PluginLoaderService {

    // Cargar procesos BPMN
    public void loadBpmnProcesses(Map<String, Object> manifest, Path pluginPath) {
        List<Map<String, Object>> processes = (List<Map<String, Object>>) manifest.get("processes");
        for (Map<String, Object> processDef : processes) {
            String bpmnFile = (String) processDef.get("file");
            Path bpmnPath = pluginPath.resolve(bpmnFile);

            // Cargar en Flowable/Activiti
            repositoryService.createDeployment()
                .addInputStream(bpmnFile, Files.newInputStream(bpmnPath))
                .deploy();
        }
    }

    // Cargar pantallas ZKoss
    public void loadZkossScreens(Map<String, Object> manifest, Path pluginPath) {
        List<Map<String, Object>> screens = (List<Map<String, Object>>) manifest.get("screens");
        for (Map<String, Object> screenDef : screens) {
            String zulFile = (String) screenDef.get("file");
            String route = (String) screenDef.get("route");

            // Copiar ZUL a webapp
            Path zulPath = pluginPath.resolve(zulFile);
            Path targetPath = Paths.get("webapp", route + ".zul");
            Files.copy(zulPath, targetPath);

            // Compilar ViewModel
            compileViewModel((String) screenDef.get("viewmodel"));
        }
    }

    // Cargar reglas Drools
    public void loadDroolsRules(Map<String, Object> manifest, Path pluginPath) {
        List<Map<String, Object>> rules = (List<Map<String, Object>>) manifest.get("rules");
        for (Map<String, Object> ruleDef : rules) {
            String drlFile = (String) ruleDef.get("file");
            String packageName = (String) ruleDef.get("package");

            // Cargar en KieBase
            KieServices kieServices = KieServices.Factory.get();
            KieFileSystem kfs = kieServices.newKieFileSystem();

            Path drlPath = pluginPath.resolve(drlFile);
            kfs.write("src/main/resources/" + drlFile,
                     Files.readAllBytes(drlPath));

            KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
            KieContainer kieContainer = kieServices.newKieContainer(
                kieBuilder.getKieModule().getReleaseId()
            );

            // Registrar en sistema
            registerKieBase((String) ruleDef.get("kbase"), kieContainer);
        }
    }
}
```

---

## 📋 COMPARACIÓN: Plugin Simple vs Completo

| Característica | Plugin Simple (Actual) | Plugin Completo (Propuesto) |
|----------------|------------------------|----------------------------|
| **Hooks MCP** | ✅ Sí | ✅ Sí |
| **Procesos BPMN** | ❌ No | ✅ Sí |
| **Pantallas ZKoss** | ❌ No | ✅ Sí |
| **Reglas Drools** | ❌ No | ✅ Sí |
| **Java Delegates** | ❌ No | ✅ Sí |
| **ViewModels** | ❌ No | ✅ Sí |
| **Complejidad** | Baja | Alta |
| **Uso** | Validación simple | Extensión completa |

---

## 🎯 CASOS DE USO

### **Plugin Simple (Actual)**
**Caso:** Partner quiere validar prompts antes de invocar
```python
def pre_invoke(payload):
    if "spam" in payload["prompt"]:
        return {"allowed": False}
    return {"allowed": True}
```

---

### **Plugin Completo (Propuesto)**
**Caso:** Partner quiere añadir proceso completo de aprobación

**1. Proceso BPMN:**
```xml
<!-- processes/partner-approval.bpmn -->
<process id="partner-approval" name="Partner Approval">
    <startEvent id="start"/>
    <userTask id="review" name="Review Request"/>
    <serviceTask id="notify" camunda:delegateExpression="${partnerNotifyDelegate}"/>
    <endEvent id="end"/>
</process>
```

**2. Delegate:**
```java
// delegates/PartnerNotifyDelegate.java
@Component
public class PartnerNotifyDelegate implements JavaDelegate {
    public void execute(DelegateExecution execution) {
        // Lógica de notificación
    }
}
```

**3. Pantalla ZKoss:**
```xml
<!-- ui/zul/review-request.zul -->
<window title="Review Request">
    <!-- Formulario de revisión -->
</window>
```

**4. Reglas Drools:**
```drl
// rules/partner-policy.drl
package com.partner.rules;

rule "Partner Auto Approve"
    when
        Request(amount < 1000)
    then
        request.setStatus("APPROVED");
end
```

---

## 🚨 CONSIDERACIONES DE SEGURIDAD

### **Plugins Completos requieren:**
- ✅ **Sandbox más estricto** - Contenedor aislado
- ✅ **Revisión de código** - Antes de aprobar
- ✅ **Firma digital** - Obligatoria
- ✅ **Whitelist de partners** - Solo partners verificados
- ✅ **Límites de recursos** - CPU, memoria, tiempo
- ✅ **Auditoría completa** - Todas las acciones registradas

---

## 📝 RECOMENDACIÓN

### **Fase 1: Plugins Simples (Actual)** ✅
- Ya implementado
- Solo hooks MCP
- Bajo riesgo
- Fácil de usar

### **Fase 2: Plugins Completos (Futuro)** ⏳
- Extender sistema
- Procesos + Pantallas + Reglas
- Mayor riesgo
- Requiere más infraestructura

**Sugerencia:** Empezar con plugins simples, luego extender según necesidad.

---

## ❓ PREGUNTAS FRECUENTES

**P: ¿Puedo añadir un proceso BPMN con un plugin actual?**
R: ❌ No, solo hooks. Necesitas plugin completo.

**P: ¿Puedo crear una pantalla nueva con un plugin?**
R: ❌ No, solo hooks. Necesitas plugin completo.

**P: ¿Puedo añadir reglas Drools con un plugin?**
R: ❌ No, solo hooks. Necesitas plugin completo.

**P: ¿Cómo añado procesos/pantallas/reglas ahora?**
R: Debes modificar el código core de CodeflowX directamente (no vía plugin).

---

## 🎯 CONCLUSIÓN

**Plugins actuales (MCP hooks):**
- ✅ Útiles para validación/transformación simple
- ❌ NO pueden añadir procesos, pantallas ni reglas

**Para añadir procesos/pantallas/reglas:**
- Necesitas extender el sistema de plugins
- Crear tipo "FULL_EXTENSION"
- Implementar loaders para BPMN, ZUL, DRL
- Considerar seguridad y sandbox

**¿Quieres que implemente el sistema de plugins completos?**
