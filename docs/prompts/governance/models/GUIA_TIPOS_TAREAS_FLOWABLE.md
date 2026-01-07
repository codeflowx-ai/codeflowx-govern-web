# Guía de Tipos de Tareas Flowable para el Diseñador BPMN

## Objetivo

Este documento explica cómo definir y representar correctamente los diferentes tipos de tareas soportados por Flowable en el diseñador BPMN, basándose en la documentación oficial de Flowable Open Source.

## Referencia

Documentación oficial: https://www.flowable.com/open-source/docs/bpmn/ch07b-BPMN-Constructs

## Tipos de Tareas Flowable

### 1. User Task (Tarea de Usuario)

**Descripción:** Tarea que requiere intervención humana para su ejecución. Puede asociarse a formularios predefinidos o dinámicos que permiten al usuario ingresar datos durante la ejecución del proceso.

**Representación XML básica:**
```xml
<userTask id="userTask1" name="Revisar Documento">
  <documentation>El usuario debe revisar y aprobar el documento</documentation>
  <extensionElements>
    <flowable:assignee>${assignee}</flowable:assignee>
    <flowable:candidateUsers>user1, user2</flowable:candidateUsers>
    <flowable:candidateGroups>group1, group2</flowable:candidateGroups>
    <flowable:dueDate>${dueDate}</flowable:dueDate>
    <flowable:priority>${priority}</flowable:priority>
    <flowable:formKey>approvalForm</flowable:formKey>
  </extensionElements>
</userTask>
```

**Atributos importantes:**
- `flowable:assignee`: Usuario asignado directamente
- `flowable:candidateUsers`: Lista de usuarios candidatos
- `flowable:candidateGroups`: Lista de grupos candidatos
- `flowable:dueDate`: Fecha límite
- `flowable:priority`: Prioridad (0-100)
- `flowable:formKey`: Clave del formulario asociado (predefinido o dinámico)

#### Formularios en User Tasks

Los User Tasks pueden tener dos tipos de formularios:

##### A. Formularios Predefinidos

Formularios estáticos con estructura fija diseñados previamente. Se referencian mediante `flowable:formKey`:

```xml
<userTask id="reviewTask" name="Revisar Solicitud">
  <extensionElements>
    <flowable:formKey>approvalForm</flowable:formKey>
  </extensionElements>
</userTask>
```

##### B. Formularios Dinámicos

Formularios que se generan o modifican en tiempo de ejecución, permitiendo adaptarse a diferentes contextos. Se definen directamente en el XML usando `formProperties` o mediante modelos JSON.

**Definición de Formulario Dinámico con formProperties (Método 1):**

```xml
<userTask id="dynamicFormTask" name="Completar Información">
  <extensionElements>
    <flowable:formProperty id="customerName" name="Nombre del Cliente"
                           type="string" required="true" readable="true" writable="true"/>
    <flowable:formProperty id="email" name="Email"
                           type="string" required="true" readable="true" writable="true"/>
    <flowable:formProperty id="amount" name="Monto"
                           type="long" required="true" readable="true" writable="true"/>
    <flowable:formProperty id="approved" name="Aprobado"
                           type="boolean" required="false" readable="true" writable="true"/>
    <flowable:formProperty id="approvalDate" name="Fecha de Aprobación"
                           type="date" required="false" readable="true" writable="true"/>
    <flowable:formProperty id="priority" name="Prioridad"
                           type="enum" required="false" readable="true" writable="true">
      <flowable:value id="low" name="Baja"/>
      <flowable:value id="medium" name="Media"/>
      <flowable:value id="high" name="Alta"/>
    </flowable:formProperty>
    <flowable:formProperty id="comments" name="Comentarios"
                           type="string" required="false" readable="true" writable="true"/>
  </extensionElements>
</userTask>
```

**Tipos de Campos Disponibles en formProperties:**

- `string`: Campo de texto
- `long`: Número entero largo
- `double`: Número decimal
- `date`: Fecha
- `boolean`: Checkbox (verdadero/falso)
- `enum`: Lista desplegable con valores predefinidos

**Atributos de formProperty:**

- `id`: Identificador único del campo (se mapea a variable de proceso)
- `name`: Etiqueta visible del campo
- `type`: Tipo de dato del campo
- `required`: Si es `true`, el campo es obligatorio
- `readable`: Si es `true`, el campo es visible/legible
- `writable`: Si es `true`, el campo es editable
- `variable`: Nombre de la variable de proceso a la que se mapea (opcional, por defecto usa `id`)
- `expression`: Expresión para calcular el valor del campo dinámicamente
- `default`: Valor por defecto del campo

**Definición de Formulario Dinámico con JSON (Método 2 - Recomendado en Flowable 6+):**

```xml
<userTask id="jsonFormTask" name="Formulario Dinámico JSON">
  <extensionElements>
    <flowable:formKey>dynamic-form-json</flowable:formKey>
  </extensionElements>
</userTask>
```

El formulario JSON se almacena como un recurso separado. Estructura básica:

```json
{
  "key": "dynamic-form-json",
  "name": "Formulario Dinámico",
  "fields": [
    {
      "id": "customerName",
      "type": "text",
      "label": "Nombre del Cliente",
      "required": true,
      "placeholder": "Ingrese el nombre",
      "validation": {
        "minLength": 3,
        "maxLength": 100,
        "pattern": "^[A-Za-z\\s]+$"
      }
    },
    {
      "id": "email",
      "type": "email",
      "label": "Email",
      "required": true,
      "validation": {
        "pattern": "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"
      }
    },
    {
      "id": "amount",
      "type": "number",
      "label": "Monto",
      "required": true,
      "validation": {
        "min": 0,
        "max": 1000000
      }
    },
    {
      "id": "approved",
      "type": "boolean",
      "label": "Aprobado",
      "defaultValue": false
    },
    {
      "id": "approvalDate",
      "type": "date",
      "label": "Fecha de Aprobación",
      "required": false,
      "dateFormat": "dd/MM/yyyy"
    },
    {
      "id": "priority",
      "type": "dropdown",
      "label": "Prioridad",
      "required": false,
      "options": [
        { "id": "low", "name": "Baja" },
        { "id": "medium", "name": "Media" },
        { "id": "high", "name": "Alta" }
      ],
      "defaultValue": "medium"
    },
    {
      "id": "comments",
      "type": "textarea",
      "label": "Comentarios",
      "required": false,
      "rows": 4,
      "maxLength": 500
    }
  ]
}
```

**Tipos de Campos en JSON:**

- `text`: Campo de texto simple
- `textarea`: Área de texto multilínea
- `number`: Campo numérico
- `email`: Campo de email con validación
- `date`: Selector de fecha
- `datetime`: Selector de fecha y hora
- `boolean`: Checkbox
- `dropdown`: Lista desplegable
- `radio`: Botones de opción
- `checkbox`: Múltiples selecciones
- `file`: Carga de archivos
- `people`: Selector de usuarios
- `group`: Selector de grupos

#### Mapeo de Variables de Entrada y Salida

Los valores ingresados en el formulario se asignan automáticamente a variables de proceso con el mismo nombre que el `id` del campo. También se pueden definir mapeos explícitos:

**Mapeo de Variables de Entrada (Input Mapping):**

Permite pasar variables del proceso a los campos del formulario como valores iniciales:

```xml
<userTask id="taskWithInputMapping" name="Tarea con Mapeo de Entrada">
  <extensionElements>
    <flowable:formProperty id="customerName" name="Nombre" type="string"/>
    <flowable:formProperty id="amount" name="Monto" type="long"/>
    <flowable:formProperty id="customerName" variable="processCustomerName"
                           expression="${processCustomerName}"/>
    <flowable:formProperty id="amount" variable="processAmount"
                           expression="${processAmount}"/>
  </extensionElements>
</userTask>
```

**Mapeo de Variables de Salida (Output Mapping):**

Permite mapear los valores del formulario a variables de proceso con nombres diferentes:

```xml
<userTask id="taskWithOutputMapping" name="Tarea con Mapeo de Salida">
  <extensionElements>
    <flowable:formProperty id="approvalStatus" name="Estado" type="enum">
      <flowable:value id="approved" name="Aprobado"/>
      <flowable:value id="rejected" name="Rechazado"/>
    </flowable:formProperty>
    <flowable:formProperty id="approvalStatus" variable="taskApprovalStatus"/>
  </extensionElements>
</userTask>
```

**Mapeo Completo con Input y Output:**

```xml
<userTask id="taskWithFullMapping" name="Tarea con Mapeo Completo">
  <extensionElements>
    <!-- Input Mapping: Variables del proceso → Campos del formulario -->
    <flowable:formProperty id="requestId" name="ID Solicitud" type="string"
                           variable="processRequestId"
                           expression="${processRequestId}"/>

    <!-- Output Mapping: Campos del formulario → Variables del proceso -->
    <flowable:formProperty id="approverComments" name="Comentarios del Aprobador"
                           type="string" variable="approvalComments"/>

    <flowable:formProperty id="approved" name="Aprobado"
                           type="boolean" variable="isApproved"/>
  </extensionElements>
</userTask>
```

#### Visibilidad Condicional de Campos

Los campos pueden mostrarse u ocultarse dinámicamente basándose en variables del proceso o valores de otros campos:

**Usando expresiones en formProperties:**

```xml
<userTask id="conditionalFormTask" name="Formulario Condicional">
  <extensionElements>
    <flowable:formProperty id="requestType" name="Tipo de Solicitud"
                           type="enum" required="true">
      <flowable:value id="standard" name="Estándar"/>
      <flowable:value id="urgent" name="Urgente"/>
    </flowable:formProperty>

    <!-- Campo visible solo si requestType == "urgent" -->
    <flowable:formProperty id="urgencyReason" name="Motivo de Urgencia"
                           type="string"
                           readable="${requestType == 'urgent'}"
                           writable="${requestType == 'urgent'}"/>

    <!-- Campo con valor por defecto calculado -->
    <flowable:formProperty id="dueDate" name="Fecha Límite"
                           type="date"
                           expression="${requestType == 'urgent' ? addDays(now(), 1) : addDays(now(), 7)}"/>
  </extensionElements>
</userTask>
```

**En formularios JSON, usando expresiones:**

```json
{
  "id": "urgencyReason",
  "type": "text",
  "label": "Motivo de Urgencia",
  "required": true,
  "visible": "${requestType == 'urgent'}",
  "enabled": "${requestType == 'urgent'}"
}
```

#### Validaciones de Campos

**Validaciones en formProperties:**

```xml
<userTask id="validatedFormTask" name="Formulario con Validaciones">
  <extensionElements>
    <!-- Validación de rango numérico -->
    <flowable:formProperty id="age" name="Edad"
                           type="long" required="true"/>

    <!-- Validación mediante expresión -->
    <flowable:formProperty id="age"
                           expression="${age >= 18 && age <= 100 ? age : null}"/>
  </extensionElements>
</userTask>
```

**Validaciones en JSON:**

```json
{
  "id": "email",
  "type": "email",
  "label": "Email",
  "required": true,
  "validation": {
    "pattern": "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
    "message": "El email no es válido"
  }
},
{
  "id": "amount",
  "type": "number",
  "label": "Monto",
  "required": true,
  "validation": {
    "min": 100,
    "max": 10000,
    "message": "El monto debe estar entre 100 y 10,000"
  }
},
{
  "id": "password",
  "type": "text",
  "label": "Contraseña",
  "required": true,
  "validation": {
    "minLength": 8,
    "maxLength": 20,
    "pattern": "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
    "message": "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número"
  }
}
```

#### Ejemplo Completo de User Task con Formulario Dinámico

```xml
<userTask id="approvalTask" name="Aprobar Solicitud de Crédito">
  <documentation>
    El usuario debe revisar y aprobar o rechazar la solicitud de crédito.
    Los campos del formulario se mapean a variables del proceso.
  </documentation>
  <extensionElements>
    <!-- Asignación -->
    <flowable:candidateGroups>credit-approvers, managers</flowable:candidateGroups>
    <flowable:dueDate>${addDays(now(), 3)}</flowable:dueDate>
    <flowable:priority>${priority}</flowable:priority>

    <!-- Formulario Dinámico -->
    <flowable:formProperty id="requestId" name="ID Solicitud"
                           type="string"
                           variable="processRequestId"
                           expression="${processRequestId}"
                           readable="true" writable="false"/>

    <flowable:formProperty id="customerName" name="Nombre del Cliente"
                           type="string"
                           variable="processCustomerName"
                           expression="${processCustomerName}"
                           readable="true" writable="false"/>

    <flowable:formProperty id="requestedAmount" name="Monto Solicitado"
                           type="long"
                           variable="processRequestedAmount"
                           expression="${processRequestedAmount}"
                           readable="true" writable="false"/>

    <flowable:formProperty id="approvalDecision" name="Decisión"
                           type="enum" required="true">
      <flowable:value id="approved" name="Aprobar"/>
      <flowable:value id="rejected" name="Rechazar"/>
      <flowable:value id="requiresMoreInfo" name="Requiere Más Información"/>
    </flowable:formProperty>

    <flowable:formProperty id="approvalComments" name="Comentarios"
                           type="string"
                           required="true"
                           variable="taskApprovalComments"/>

    <flowable:formProperty id="approvalDate" name="Fecha de Aprobación"
                           type="date"
                           expression="${now()}"
                           readable="true" writable="false"/>

    <!-- Campo condicional: solo visible si se rechaza -->
    <flowable:formProperty id="rejectionReason" name="Motivo del Rechazo"
                           type="string"
                           readable="${approvalDecision == 'rejected'}"
                           writable="${approvalDecision == 'rejected'}"
                           required="${approvalDecision == 'rejected'}"/>
  </extensionElements>
</userTask>
```

#### Resumen de Atributos para Formularios Dinámicos

**Para formProperties:**

| Atributo | Tipo | Descripción | Ejemplo |
|----------|------|-------------|---------|
| `id` | string | Identificador único del campo (mapea a variable) | `"customerName"` |
| `name` | string | Etiqueta visible del campo | `"Nombre del Cliente"` |
| `type` | string | Tipo de dato (string, long, double, date, boolean, enum) | `"string"` |
| `required` | boolean | Si el campo es obligatorio | `true` |
| `readable` | boolean/expression | Si el campo es visible | `true` o `"${condition}"` |
| `writable` | boolean/expression | Si el campo es editable | `true` o `"${condition}"` |
| `variable` | string | Nombre de la variable de proceso (opcional) | `"processCustomerName"` |
| `expression` | string | Expresión para calcular valor o validación | `"${processCustomerName}"` |
| `default` | string | Valor por defecto | `"Valor por defecto"` |

**Para formularios JSON:**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | string | Identificador único del campo |
| `type` | string | Tipo de campo (text, number, email, date, boolean, dropdown, etc.) |
| `label` | string | Etiqueta visible |
| `required` | boolean | Si es obligatorio |
| `visible` | string | Expresión para visibilidad condicional |
| `enabled` | string | Expresión para habilitar/deshabilitar |
| `defaultValue` | any | Valor por defecto |
| `validation` | object | Objeto con reglas de validación (pattern, min, max, minLength, maxLength) |
| `options` | array | Para dropdown/radio, lista de opciones con `id` y `name` |

### 2. Script Task (Tarea de Script)

**Descripción:** Tarea que ejecuta un script (JavaScript, Groovy, etc.).

**Representación XML:**
```xml
<scriptTask id="scriptTask1" name="Calcular Total" scriptFormat="javascript">
  <script>
    <![CDATA[
      var total = 0;
      for (var i = 0; i < items.length; i++) {
        total += items[i].price;
      }
      execution.setVariable("total", total);
    ]]>
  </script>
</scriptTask>
```

**Atributos importantes:**
- `scriptFormat`: Formato del script (javascript, groovy, juel, etc.)
- `script`: Contenido del script a ejecutar

### 3. Java Service Task (Tarea de Servicio Java)

**Descripción:** Tarea que invoca una clase Java o expresión.

**Representación XML:**
```xml
<serviceTask id="javaServiceTask1" name="Procesar Datos"
             flowable:class="com.example.ProcessDataService">
</serviceTask>

<!-- O usando expresión -->
<serviceTask id="javaServiceTask2" name="Procesar Datos"
             flowable:expression="${dataProcessor.process(data)}">
</serviceTask>

<!-- O usando delegateExpression -->
<serviceTask id="javaServiceTask3" name="Procesar Datos"
             flowable:delegateExpression="${dataProcessorBean}">
</serviceTask>
```

**Atributos importantes:**
- `flowable:class`: Nombre completo de la clase Java
- `flowable:expression`: Expresión que invoca un método
- `flowable:delegateExpression`: Expresión que resuelve a un bean/objeto
- `flowable:async`: Si es true, ejecuta de forma asíncrona
- `flowable:exclusive`: Si es true (default), ejecuta de forma exclusiva

### 4. Web Service Task (Tarea de Servicio Web)

**Descripción:** Tarea que invoca un servicio web SOAP.

**Representación XML:**
```xml
<serviceTask id="webServiceTask1" name="Llamar Servicio Web"
             flowable:class="org.flowable.engine.impl.webservice.SyncWebServiceActivityBehavior">
  <extensionElements>
    <flowable:field name="operationRef">
      <flowable:string>http://example.com/wsdl#operation1</flowable:string>
    </flowable:field>
    <flowable:field name="operationImplementationRef">
      <flowable:string>http://example.com/wsdl#operation1Impl</flowable:string>
    </flowable:field>
  </extensionElements>
</serviceTask>
```

### 5. Business Rule Task (Tarea de Reglas de Negocio)

**Descripción:** Tarea que ejecuta reglas de negocio (Drools, etc.).

**Representación XML:**
```xml
<businessRuleTask id="businessRuleTask1" name="Aplicar Reglas"
                  flowable:ruleVariablesInput="${rulesInput}"
                  flowable:resultVariable="rulesOutput">
  <extensionElements>
    <flowable:rule>
      <flowable:ruleId>rule1</flowable:ruleId>
      <flowable:ruleId>rule2</flowable:ruleId>
    </flowable:rule>
  </extensionElements>
</businessRuleTask>
```

**Atributos importantes:**
- `flowable:ruleVariablesInput`: Variables de entrada para las reglas
- `flowable:resultVariable`: Variable donde se almacena el resultado
- `flowable:rule`: Lista de IDs de reglas a ejecutar

### 6. Email Task (Tarea de Email)

**Descripción:** Tarea que envía un email.

**Representación XML:**
```xml
<serviceTask id="emailTask1" name="Enviar Notificación"
             flowable:type="mail">
  <extensionElements>
    <flowable:field name="to">
      <flowable:string>${recipient}</flowable:string>
    </flowable:field>
    <flowable:field name="from">
      <flowable:string>noreply@example.com</flowable:string>
    </flowable:field>
    <flowable:field name="subject">
      <flowable:string>Notificación de Proceso</flowable:string>
    </flowable:field>
    <flowable:field name="html">
      <flowable:string><![CDATA[<html><body>Contenido del email</body></html>]]></flowable:string>
    </flowable:field>
  </extensionElements>
</serviceTask>
```

**Campos importantes:**
- `to`: Destinatario(s)
- `from`: Remitente
- `subject`: Asunto
- `html` o `text`: Contenido del email

### 7. Http Task (Tarea HTTP)

**Descripción:** Tarea que realiza una petición HTTP.

**Representación XML:**
```xml
<serviceTask id="httpTask1" name="Llamar API REST"
             flowable:type="http">
  <extensionElements>
    <flowable:field name="requestMethod">
      <flowable:string>GET</flowable:string>
    </flowable:field>
    <flowable:field name="requestUrl">
      <flowable:string>https://api.example.com/data</flowable:string>
    </flowable:field>
    <flowable:field name="requestHeaders">
      <flowable:expression>${headers}</flowable:expression>
    </flowable:field>
    <flowable:field name="requestBody">
      <flowable:expression>${requestBody}</flowable:expression>
    </flowable:field>
    <flowable:field name="responseVariableName">
      <flowable:string>httpResponse</flowable:string>
    </flowable:field>
  </extensionElements>
</serviceTask>
```

**Campos importantes:**
- `requestMethod`: Método HTTP (GET, POST, PUT, DELETE, etc.)
- `requestUrl`: URL del endpoint
- `requestHeaders`: Headers HTTP (opcional)
- `requestBody`: Cuerpo de la petición (opcional)
- `responseVariableName`: Variable donde se almacena la respuesta

### 8. Camel Task (Tarea Camel)

**Descripción:** Tarea que integra con Apache Camel.

**Representación XML:**
```xml
<serviceTask id="camelTask1" name="Procesar con Camel"
             flowable:type="camel">
  <extensionElements>
    <flowable:field name="camelContext">
      <flowable:string>camelContext</flowable:string>
    </flowable:field>
  </extensionElements>
</serviceTask>
```

### 9. Manual Task (Tarea Manual)

**Descripción:** Tarea que se realiza manualmente fuera del sistema.

**Representación XML:**
```xml
<manualTask id="manualTask1" name="Revisión Física">
  <documentation>Esta tarea se realiza fuera del sistema</documentation>
</manualTask>
```

### 10. Java Receive Task (Tarea de Recepción Java)

**Descripción:** Tarea que espera recibir un mensaje Java.

**Representación XML:**
```xml
<receiveTask id="receiveTask1" name="Esperar Confirmación"
             flowable:class="com.example.WaitForConfirmation">
</receiveTask>
```

### 11. Shell Task (Tarea de Shell)

**Descripción:** Tarea que ejecuta un comando de shell.

**Representación XML:**
```xml
<serviceTask id="shellTask1" name="Ejecutar Script"
             flowable:type="shell">
  <extensionElements>
    <flowable:field name="command">
      <flowable:string>echo "Hello World"</flowable:string>
    </flowable:field>
    <flowable:field name="arg1">
      <flowable:string>argument1</flowable:string>
    </flowable:field>
    <flowable:field name="wait">
      <flowable:boolean>true</flowable:boolean>
    </flowable:field>
  </extensionElements>
</serviceTask>
```

### 12. External Worker Task (Tarea de Trabajador Externo)

**Descripción:** Tarea que se ejecuta en un trabajador externo.

**Representación XML:**
```xml
<serviceTask id="externalWorkerTask1" name="Procesar Externo"
             flowable:type="externalWorker">
  <extensionElements>
    <flowable:field name="topic">
      <flowable:string>processing</flowable:string>
    </flowable:field>
  </extensionElements>
</serviceTask>
```

## Tareas Personalizadas (Custom Tasks)

Flowable permite crear tareas completamente personalizadas para ejecutar lógica específica de negocio, como ejecutar agentes de IA, realizar operaciones en bases de datos, o llamar a APIs REST personalizadas. Existen varias formas de implementar tareas personalizadas:

### Método 1: JavaDelegate (Recomendado para la mayoría de casos)

**Descripción:** Implementa la interfaz `JavaDelegate` para crear tareas personalizadas. Es el método más común y sencillo.

**Implementación Java:**

```java
package com.codeflowx.govern.workflow.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class AIAgentDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        // Obtener variables del proceso
        String agentId = (String) execution.getVariable("agentId");
        String prompt = (String) execution.getVariable("prompt");
        Object context = execution.getVariable("context");

        // Ejecutar lógica personalizada (ej: llamar a agente de IA)
        String result = executeAIAgent(agentId, prompt, context);

        // Guardar resultado en variable de proceso
        execution.setVariable("agentResult", result);
        execution.setVariable("executionStatus", "completed");
    }

    private String executeAIAgent(String agentId, String prompt, Object context) {
        // Lógica para ejecutar el agente de IA
        // Ejemplo: llamada a servicio REST, base de datos, etc.
        return "Resultado del agente";
    }
}
```

**Representación XML en BPMN:**

```xml
<serviceTask id="aiAgentTask" name="Ejecutar Agente de IA"
             flowable:class="com.codeflowx.govern.workflow.delegate.AIAgentDelegate"
             flowable:async="true">
</serviceTask>
```

**O usando delegateExpression (con Spring):**

```xml
<serviceTask id="aiAgentTask" name="Ejecutar Agente de IA"
             flowable:delegateExpression="${aiAgentDelegate}"
             flowable:async="true">
</serviceTask>
```

### Método 2: ActivityBehavior (Para comportamientos complejos)

**Descripción:** Implementa `ActivityBehavior` para tener control total sobre el comportamiento de la tarea, incluyendo el flujo de ejecución.

**Implementación Java:**

```java
package com.codeflowx.govern.workflow.behavior;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.flowable.engine.impl.delegate.ActivityBehavior;

public class DatabaseOperationBehavior extends AbstractBpmnActivityBehavior
        implements ActivityBehavior {

    @Override
    public void execute(DelegateExecution execution) {
        // Obtener parámetros de configuración
        String operation = (String) execution.getVariable("dbOperation");
        String query = (String) execution.getVariable("dbQuery");
        Object parameters = execution.getVariable("dbParameters");

        // Ejecutar operación en base de datos
        Object result = executeDatabaseOperation(operation, query, parameters);

        // Guardar resultado
        execution.setVariable("dbResult", result);

        // Continuar con el flujo
        leave(execution);
    }

    private Object executeDatabaseOperation(String operation, String query, Object parameters) {
        // Lógica para ejecutar operación en base de datos
        // Ejemplo: INSERT, UPDATE, DELETE, SELECT
        return null;
    }
}
```

**Representación XML en BPMN:**

```xml
<serviceTask id="dbOperationTask" name="Operación en Base de Datos"
             flowable:class="com.codeflowx.govern.workflow.behavior.DatabaseOperationBehavior">
  <extensionElements>
    <flowable:field name="dbOperation">
      <flowable:string>SELECT</flowable:string>
    </flowable:field>
    <flowable:field name="dbQuery">
      <flowable:string>SELECT * FROM users WHERE id = ?</flowable:string>
    </flowable:field>
  </extensionElements>
</serviceTask>
```

### Método 3: Expresiones (Para lógica simple)

**Descripción:** Usa expresiones para invocar métodos de beans de Spring directamente.

**Representación XML en BPMN:**

```xml
<serviceTask id="simpleOperationTask" name="Operación Simple"
             flowable:expression="${dataService.processData(data)}">
</serviceTask>
```

### Ejemplos Específicos de Tareas Personalizadas

#### Ejemplo 1: Tarea para Ejecutar Agente de IA (Configurable en Tiempo de Diseño)

Este es un tipo de tarea personalizada específico para ejecutar agentes de IA predefinidos en el sistema. La configuración se realiza completamente en tiempo de diseño en el diseñador BPMN, y siempre utiliza la misma clase de servicio genérica.

**Clase Java Genérica (Siempre la misma):**

```java
package com.codeflowx.govern.workflow.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.delegate.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Delegate genérico para ejecutar agentes de IA predefinidos en el sistema.
 * La configuración (agente, prompt, variables) se define en tiempo de diseño
 * mediante extensionElements en el BPMN.
 */
@Component("executeAIAgentDelegate")
public class ExecuteAIAgentDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(ExecuteAIAgentDelegate.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private String aiServiceBaseUrl; // Inyectado desde configuración (ej: http://ai-service:8080)

    // Expresiones inyectadas desde el BPMN
    private Expression agentUuidExpression;
    private Expression promptExpression;
    private Expression inputVariablesExpression;
    private Expression outputVariablePrefixExpression;

    @Override
    public void execute(DelegateExecution execution) {
        logger.info("Ejecutando agente de IA para proceso: {}", execution.getProcessInstanceId());

        try {
            // 1. Obtener configuración desde extensionElements (definida en tiempo de diseño)
            String agentUuid = getStringValue(agentUuidExpression, execution, "agentUuid");
            String prompt = getStringValue(promptExpression, execution, "prompt");
            String inputVariablesJson = getStringValue(inputVariablesExpression, execution, "inputVariables");
            String outputVariablePrefix = getStringValue(outputVariablePrefixExpression, execution, "outputVariablePrefix");

            if (agentUuid == null || agentUuid.isEmpty()) {
                throw new IllegalArgumentException("agentUuid es requerido");
            }

            if (prompt == null || prompt.isEmpty()) {
                throw new IllegalArgumentException("prompt es requerido");
            }

            // 2. Construir el contexto con las variables de entrada definidas en tiempo de diseño
            Map<String, Object> context = buildContextFromInputVariables(execution, inputVariablesJson);

            // 3. Preparar petición al servicio de agentes
            Map<String, Object> request = new HashMap<>();
            request.put("agentUuid", agentUuid);
            request.put("prompt", prompt);
            request.put("context", context);

            logger.debug("Ejecutando agente {} con prompt: {}", agentUuid, prompt);

            // 4. Llamar al servicio de agentes de IA
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                aiServiceBaseUrl + "/api/v1/agents/" + agentUuid + "/execute",
                HttpMethod.POST,
                entity,
                Map.class
            );

            // 5. Procesar respuesta y mapear variables de salida
            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null) {
                throw new RuntimeException("Respuesta vacía del servicio de agentes");
            }

            // 6. Guardar variables de salida según la configuración de tiempo de diseño
            mapOutputVariables(execution, responseBody, outputVariablePrefix);

            // 7. Variables de control
            execution.setVariable("agentExecutionStatus", "success");
            execution.setVariable("agentExecutionTime", responseBody.get("executionTime"));

            logger.info("Agente {} ejecutado exitosamente", agentUuid);

        } catch (Exception e) {
            logger.error("Error ejecutando agente de IA", e);
            execution.setVariable("agentExecutionStatus", "error");
            execution.setVariable("agentErrorMessage", e.getMessage());
            throw new RuntimeException("Error ejecutando agente de IA: " + e.getMessage(), e);
        }
    }

    /**
     * Construye el contexto a partir de las variables de entrada definidas en tiempo de diseño.
     * El formato esperado es JSON: {"variable1": "processVar1", "variable2": "processVar2"}
     */
    private Map<String, Object> buildContextFromInputVariables(DelegateExecution execution, String inputVariablesJson) {
        Map<String, Object> context = new HashMap<>();

        if (inputVariablesJson == null || inputVariablesJson.trim().isEmpty()) {
            return context;
        }

        try {
            // Parsear JSON de variables de entrada
            // Formato: {"inputVar1": "processVar1", "inputVar2": "processVar2"}
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, String> inputMapping = mapper.readValue(inputVariablesJson, Map.class);

            // Mapear variables del proceso al contexto
            for (Map.Entry<String, String> entry : inputMapping.entrySet()) {
                String contextKey = entry.getKey();
                String processVariableName = entry.getValue();
                Object processVariableValue = execution.getVariable(processVariableName);

                if (processVariableValue != null) {
                    context.put(contextKey, processVariableValue);
                } else {
                    logger.warn("Variable de proceso '{}' no encontrada, usando null", processVariableName);
                    context.put(contextKey, null);
                }
            }
        } catch (Exception e) {
            logger.error("Error parseando variables de entrada: {}", inputVariablesJson, e);
            throw new IllegalArgumentException("Formato inválido de inputVariables: " + inputVariablesJson, e);
        }

        return context;
    }

    /**
     * Mapea las variables de salida de la respuesta del agente a variables del proceso.
     * El formato esperado es JSON: {"outputVar1": "processVar1", "outputVar2": "processVar2"}
     */
    private void mapOutputVariables(DelegateExecution execution, Map<String, Object> responseBody, String outputVariablePrefix) {
        // Si hay un prefijo, todas las variables de salida lo tendrán
        String prefix = (outputVariablePrefix != null && !outputVariablePrefix.isEmpty())
            ? outputVariablePrefix + "_"
            : "";

        // Por defecto, mapear toda la respuesta del agente
        // La respuesta típicamente tiene: response, metadata, executionTime, etc.
        Object agentResponse = responseBody.get("response");
        if (agentResponse != null) {
            execution.setVariable(prefix + "agentResponse", agentResponse);
        }

        // Si la respuesta es un mapa, mapear cada campo
        if (agentResponse instanceof Map) {
            Map<String, Object> responseMap = (Map<String, Object>) agentResponse;
            for (Map.Entry<String, Object> entry : responseMap.entrySet()) {
                execution.setVariable(prefix + entry.getKey(), entry.getValue());
            }
        }

        // Mapear metadata si existe
        if (responseBody.containsKey("metadata")) {
            execution.setVariable(prefix + "agentMetadata", responseBody.get("metadata"));
        }
    }

    private String getStringValue(Expression expression, DelegateExecution execution, String defaultValue) {
        if (expression != null) {
            Object value = expression.getValue(execution);
            return value != null ? value.toString() : null;
        }
        return execution.getVariable(defaultValue) != null
            ? execution.getVariable(defaultValue).toString()
            : null;
    }

    // Setters para inyección de expresiones desde Flowable
    public void setAgentUuidExpression(Expression agentUuidExpression) {
        this.agentUuidExpression = agentUuidExpression;
    }

    public void setPromptExpression(Expression promptExpression) {
        this.promptExpression = promptExpression;
    }

    public void setInputVariablesExpression(Expression inputVariablesExpression) {
        this.inputVariablesExpression = inputVariablesExpression;
    }

    public void setOutputVariablePrefixExpression(Expression outputVariablePrefixExpression) {
        this.outputVariablePrefixExpression = outputVariablePrefixExpression;
    }
}
```

**Representación XML en BPMN (Configuración en Tiempo de Diseño):**

```xml
<serviceTask id="executeAIAgentTask" name="Ejecutar Agente de Análisis"
             flowable:type="ai-agent"
             flowable:delegateExpression="${executeAIAgentDelegate}"
             flowable:async="true"
             flowable:exclusive="true">
  <extensionElements>
    <!-- Agente predefinido a ejecutar (UUID del agente en el sistema) -->
    <flowable:field name="agentUuid">
      <flowable:string>550e8400-e29b-41d4-a716-446655440000</flowable:string>
    </flowable:field>

    <!-- Prompt a enviar al agente (puede incluir expresiones) -->
    <flowable:field name="prompt">
      <flowable:string>Analiza el siguiente documento y extrae las entidades principales: ${documentContent}</flowable:string>
    </flowable:field>

    <!-- Variables de entrada: JSON que mapea nombres en el contexto del agente
         a variables del proceso BPMN -->
    <flowable:field name="inputVariables">
      <flowable:string>{"document": "documentContent", "language": "documentLanguage", "userId": "currentUserId"}</flowable:string>
    </flowable:field>

    <!-- Prefijo opcional para variables de salida (evita conflictos de nombres) -->
    <flowable:field name="outputVariablePrefix">
      <flowable:string>analysis</flowable:string>
    </flowable:field>
  </extensionElements>
</serviceTask>
```

**Formato de Variables de Entrada (`inputVariables`):**

El campo `inputVariables` es un JSON que mapea los nombres de variables que el agente espera recibir (claves del JSON) a las variables del proceso BPMN (valores del JSON):

```json
{
  "nombreEnContextoAgente": "nombreVariableProcesoBPMN",
  "document": "documentContent",
  "language": "documentLanguage",
  "userId": "currentUserId",
  "metadata": "requestMetadata"
}
```

**Formato de Variables de Salida:**

Las variables de salida se mapean automáticamente desde la respuesta del agente. Si se especifica `outputVariablePrefix`, todas las variables tendrán ese prefijo:

- Sin prefijo: `agentResponse`, `agentMetadata`, `executionTime`
- Con prefijo "analysis": `analysis_agentResponse`, `analysis_agentMetadata`, `analysis_executionTime`

Si la respuesta del agente es un objeto JSON, cada campo se mapea como variable individual:

```json
{
  "response": {
    "entities": ["entity1", "entity2"],
    "confidence": 0.95,
    "summary": "Resumen del análisis"
  }
}
```

Se mapean como:
- `analysis_agentResponse` (objeto completo)
- `analysis_entities` (array)
- `analysis_confidence` (número)
- `analysis_summary` (string)

#### Ejemplo 2: Tarea para Operaciones en Base de Datos

**Clase Java:**

```java
package com.codeflowx.govern.workflow.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DatabaseOperationDelegate implements JavaDelegate {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void execute(DelegateExecution execution) {
        // Obtener configuración de la tarea
        String operation = (String) execution.getVariable("dbOperation");
        String query = (String) execution.getVariable("dbQuery");
        List<Object> parameters = (List<Object>) execution.getVariable("dbParameters");

        Object result = null;

        switch (operation.toUpperCase()) {
            case "SELECT":
                if (parameters != null && !parameters.isEmpty()) {
                    result = jdbcTemplate.queryForList(query, parameters.toArray());
                } else {
                    result = jdbcTemplate.queryForList(query);
                }
                execution.setVariable("dbResult", result);
                break;

            case "INSERT":
            case "UPDATE":
            case "DELETE":
                int rowsAffected;
                if (parameters != null && !parameters.isEmpty()) {
                    rowsAffected = jdbcTemplate.update(query, parameters.toArray());
                } else {
                    rowsAffected = jdbcTemplate.update(query);
                }
                execution.setVariable("rowsAffected", rowsAffected);
                break;

            default:
                throw new IllegalArgumentException("Operación no soportada: " + operation);
        }

        execution.setVariable("dbOperationStatus", "completed");
    }
}
```

**Representación XML:**

```xml
<serviceTask id="dbSelectTask" name="Consultar Base de Datos"
             flowable:delegateExpression="${databaseOperationDelegate}"
             flowable:async="true">
  <extensionElements>
    <flowable:field name="dbOperation">
      <flowable:string>SELECT</flowable:string>
    </flowable:field>
    <flowable:field name="dbQuery">
      <flowable:string>SELECT * FROM agents WHERE status = ?</flowable:string>
    </flowable:field>
    <flowable:field name="dbParameters">
      <flowable:expression>${java.util.Arrays.asList("ACTIVE")}</flowable:expression>
    </flowable:field>
  </extensionElements>
</serviceTask>
```

#### Ejemplo 3: Tarea para Llamada a API REST Personalizada

**Clase Java:**

```java
package com.codeflowx.govern.workflow.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class CustomRestApiDelegate implements JavaDelegate {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void execute(DelegateExecution execution) {
        // Obtener configuración
        String apiUrl = (String) execution.getVariable("apiUrl");
        String httpMethod = (String) execution.getVariable("httpMethod");
        Map<String, String> headers = (Map<String, String>) execution.getVariable("headers");
        Object requestBody = execution.getVariable("requestBody");

        // Preparar headers HTTP
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            headers.forEach(httpHeaders::set);
        }
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // Crear entidad HTTP
        HttpEntity<Object> entity = new HttpEntity<>(requestBody, httpHeaders);

        // Ejecutar petición
        ResponseEntity<Map> response = restTemplate.exchange(
            apiUrl,
            HttpMethod.valueOf(httpMethod),
            entity,
            Map.class
        );

        // Guardar respuesta
        execution.setVariable("apiResponse", response.getBody());
        execution.setVariable("apiStatusCode", response.getStatusCodeValue());
        execution.setVariable("apiStatus", "success");
    }
}
```

**Representación XML:**

```xml
<serviceTask id="customApiTask" name="Llamar API REST Personalizada"
             flowable:delegateExpression="${customRestApiDelegate}"
             flowable:async="true">
  <extensionElements>
    <flowable:field name="apiUrl">
      <flowable:expression>${apiBaseUrl + '/api/v1/custom/endpoint'}</flowable:expression>
    </flowable:field>
    <flowable:field name="httpMethod">
      <flowable:string>POST</flowable:string>
    </flowable:field>
    <flowable:field name="headers">
      <flowable:expression>${java.util.Map.of('Authorization', 'Bearer ' + apiToken)}</flowable:expression>
    </flowable:field>
    <flowable:field name="requestBody">
      <flowable:expression>${requestData}</flowable:expression>
    </flowable:field>
  </extensionElements>
</serviceTask>
```

### Definir Tipos de Tareas Personalizados en el Diseñador

Para que el diseñador BPMN pueda representar y editar tareas personalizadas, se debe:

1. **Registrar el Tipo Personalizado:**
   - Definir un identificador único para el tipo (ej: `ai-agent`, `db-operation`, `custom-api`)
   - Asociar el tipo con la clase Java correspondiente

2. **Representación XML:**
   ```xml
   <serviceTask id="customTask1" name="Tarea Personalizada"
                flowable:type="ai-agent"
                flowable:class="com.example.AIAgentDelegate"
                flowable:async="true">
     <extensionElements>
       <!-- Campos específicos del tipo personalizado -->
       <flowable:field name="agentUuid">
         <flowable:expression>${agentUuid}</flowable:expression>
       </flowable:field>
     </extensionElements>
   </serviceTask>
   ```

3. **Panel de Propiedades en el Diseñador:**
   - Mostrar campos específicos según el tipo de tarea personalizada
   - Validar campos requeridos
   - Permitir editar configuración específica del tipo

### Representación de "Ejecutar Agente de IA" en el Diseñador BPMN

Esta sección detalla cómo implementar la representación gráfica y el panel de propiedades para el tipo de tarea "Ejecutar Agente de IA" en el diseñador BPMN.

#### 1. Identificación del Tipo de Tarea

Para que el diseñador identifique y represente correctamente este tipo de tarea, se debe usar la propiedad `flowable:type="ai-agent"` en el XML:

```xml
<serviceTask id="executeAIAgentTask" name="Ejecutar Agente de IA"
             flowable:type="ai-agent"
             flowable:delegateExpression="${executeAIAgentDelegate}"
             flowable:async="true"
             flowable:exclusive="true">
  <!-- ... -->
</serviceTask>
```

**Propiedad clave:** `flowable:type="ai-agent"` es la propiedad que identifica este tipo de tarea personalizada.

#### 2. Registro del Tipo de Tarea en el Diseñador

El diseñador debe registrar el tipo `ai-agent` como un tipo personalizado con icono y propiedades específicas:

```typescript
// Definición del tipo de tarea personalizada
import { Bot } from 'lucide-react'; // o el icono que prefieras

const AI_AGENT_TASK_TYPE = {
  id: 'ai-agent',
  name: 'Ejecutar Agente de IA',
  icon: Bot, // Icono personalizado tipo bot
  iconColor: '#8b5cf6', // Color púrpura para diferenciarlo
  category: 'custom',
  paletteLabel: 'Agente de IA',
  description: 'Ejecuta un agente de IA predefinido del sistema',
  implementation: {
    delegateExpression: '${executeAIAgentDelegate}',
    async: true,
    exclusive: true
  },
  // Propiedades personalizadas que se mostrarán en el panel
  customProperties: [
    'agentUuid',
    'prompt',
    'inputVariables',
    'outputVariablePrefix'
  ]
}
```

#### 3. Configuración del Icono Personalizado en bpmn-js

Para que bpmn-js muestre el icono personalizado, se debe configurar el renderizado:

```typescript
// Configuración de iconos personalizados en bpmn-js
import { CustomRenderer } from 'bpmn-js/lib/draw/CustomRenderer';
import { Bot } from 'lucide-react';

class AIAgentRenderer extends CustomRenderer {
  constructor(eventBus, bpmnRenderer) {
    super(eventBus, bpmnRenderer);
  }

  canRender(element) {
    // Identificar tareas con flowable:type="ai-agent"
    return element.type === 'bpmn:ServiceTask' &&
           element.businessObject?.get('flowable:type') === 'ai-agent';
  }

  drawShape(parentNode, element) {
    const shape = this.bpmnRenderer.drawShape(parentNode, element);

    // Añadir icono personalizado
    const iconGroup = this.bpmnRenderer.handlers.create('g', {
      class: 'ai-agent-icon',
      transform: 'translate(10, 10)'
    });

    // Crear SVG del icono Bot
    const iconSvg = this.bpmnRenderer.handlers.create('svg', {
      width: '24',
      height: '24',
      viewBox: '0 0 24 24',
      fill: 'none',
      stroke: '#8b5cf6',
      'stroke-width': '2'
    }, iconGroup);

    // Path del icono Bot (simplificado)
    this.bpmnRenderer.handlers.create('path', {
      d: 'M12 8V4H8a2 2 0 0 0-2 2v4c0 1.1.9 2 2 2h4v4a2 2 0 0 0 2 2h4a2 2 0 0 0 2-2v-4a2 2 0 0 0-2-2h-4V8z'
    }, iconSvg);

    shape.appendChild(iconGroup);

    return shape;
  }
}

// Registrar el renderer
export default {
  __init__: ['aiAgentRenderer'],
  aiAgentRenderer: ['type', AIAgentRenderer]
};
```

**Alternativa más simple con CSS:**

```css
/* Estilos para identificar visualmente las tareas de agente de IA */
.bpmn-container .djs-element[data-type="bpmn:ServiceTask"]
  [data-flowable-type="ai-agent"] {
  /* Añadir icono de fondo o badge */
  position: relative;
}

.bpmn-container .djs-element[data-type="bpmn:ServiceTask"]
  [data-flowable-type="ai-agent"]::before {
  content: '🤖';
  position: absolute;
  top: 5px;
  right: 5px;
  font-size: 16px;
  z-index: 10;
}

/* O usar un icono SVG personalizado */
.bpmn-container .ai-agent-icon {
  position: absolute;
  top: 8px;
  right: 8px;
  width: 20px;
  height: 20px;
  color: #8b5cf6;
}
```

#### 4. Detección del Tipo en el Diseñador

El diseñador debe detectar el tipo de tarea al cargar o seleccionar un elemento:

```typescript
// Función para detectar si una tarea es de tipo "ai-agent"
function isAIAgentTask(element: any): boolean {
  if (!element || element.type !== 'bpmn:ServiceTask') {
    return false;
  }

  // Obtener el tipo desde el modelo BPMN
  const businessObject = element.businessObject;
  const flowableType = businessObject?.get('flowable:type');

  return flowableType === 'ai-agent';
}

// Al seleccionar un elemento en el canvas
eventBus.on('element.click', (event: any) => {
  const element = event.element;

  if (isAIAgentTask(element)) {
    // Mostrar panel de propiedades personalizado
    showAIAgentPropertiesPanel(element);
  } else {
    // Mostrar panel de propiedades estándar
    showStandardPropertiesPanel(element);
  }
});
```

#### 5. Configuración Completa de la Paleta del Diseñador

La paleta del diseñador debe mostrar todos los componentes BPMN estándar y los tipos personalizados de Flowable. Aquí se detalla cómo configurarla completamente:

**A. Módulo de Personalización de Paleta:**

```typescript
// modules/custom-palette-provider.ts
import { PaletteProvider } from 'bpmn-js/lib/features/palette/PaletteProvider';
import { Bot, Database, Globe, Mail, Code, Users, FileText, Settings } from 'lucide-react';

export default class CustomPaletteProvider extends PaletteProvider {
  constructor(palette: any, create: any, elementFactory: any, spaceTool: any,
              lassoTool: any, handTool: any, globalConnect: any, translate: any) {
    super(palette, create, elementFactory, spaceTool, lassoTool, handTool, globalConnect, translate);

    this.create = create;
    this.elementFactory = elementFactory;
    this.translate = translate;
  }

  getPaletteEntries(): any {
    // Obtener entradas estándar de BPMN
    const entries = super.getPaletteEntries();

    // Añadir grupo personalizado para tareas de Flowable
    entries['flowable-separator'] = {
      group: 'flowable',
      separator: true
    };

    // Tareas personalizadas de Flowable
    entries['create.ai-agent'] = this.createAIAgentEntry();
    entries['create.db-operation'] = this.createDatabaseOperationEntry();
    entries['create.email-task'] = this.createEmailTaskEntry();
    entries['create.http-task'] = this.createHttpTaskEntry();
    entries['create.business-rule-task'] = this.createBusinessRuleTaskEntry();
    entries['create.script-task'] = this.createScriptTaskEntry();

    return entries;
  }

  private createAIAgentEntry(): any {
    return {
      group: 'flowable',
      className: 'bpmn-icon-service-task ai-agent-palette-icon',
      title: 'Ejecutar Agente de IA',
      action: {
        dragstart: (event: any) => this.createAIAgentTask(event),
        click: (event: any) => this.createAIAgentTask(event)
      }
    };
  }

  private createAIAgentTask(event: any): any {
    const shape = this.elementFactory.createShape({
      type: 'bpmn:ServiceTask',
      businessObject: {
        type: 'bpmn:ServiceTask',
        name: 'Ejecutar Agente de IA',
        'flowable:type': 'ai-agent',
        'flowable:delegateExpression': '${executeAIAgentDelegate}',
        'flowable:async': true,
        'flowable:exclusive': true
      }
    });

    this.create.start(event, shape);
    return shape;
  }

  private createDatabaseOperationEntry(): any {
    return {
      group: 'flowable',
      className: 'bpmn-icon-service-task db-operation-palette-icon',
      title: 'Operación en Base de Datos',
      action: {
        dragstart: (event: any) => this.createDatabaseOperationTask(event),
        click: (event: any) => this.createDatabaseOperationTask(event)
      }
    };
  }

  private createDatabaseOperationTask(event: any): any {
    const shape = this.elementFactory.createShape({
      type: 'bpmn:ServiceTask',
      businessObject: {
        type: 'bpmn:ServiceTask',
        name: 'Operación en Base de Datos',
        'flowable:type': 'db-operation',
        'flowable:delegateExpression': '${databaseOperationDelegate}',
        'flowable:async': true
      }
    });

    this.create.start(event, shape);
    return shape;
  }

  private createEmailTaskEntry(): any {
    return {
      group: 'flowable',
      className: 'bpmn-icon-service-task email-task-palette-icon',
      title: 'Enviar Email',
      action: {
        dragstart: (event: any) => this.createEmailTask(event),
        click: (event: any) => this.createEmailTask(event)
      }
    };
  }

  private createEmailTask(event: any): any {
    const shape = this.elementFactory.createShape({
      type: 'bpmn:ServiceTask',
      businessObject: {
        type: 'bpmn:ServiceTask',
        name: 'Enviar Email',
        'flowable:type': 'mail'
      }
    });

    this.create.start(event, shape);
    return shape;
  }

  private createHttpTaskEntry(): any {
    return {
      group: 'flowable',
      className: 'bpmn-icon-service-task http-task-palette-icon',
      title: 'Llamada HTTP',
      action: {
        dragstart: (event: any) => this.createHttpTask(event),
        click: (event: any) => this.createHttpTask(event)
      }
    };
  }

  private createHttpTask(event: any): any {
    const shape = this.elementFactory.createShape({
      type: 'bpmn:ServiceTask',
      businessObject: {
        type: 'bpmn:ServiceTask',
        name: 'Llamada HTTP',
        'flowable:type': 'http'
      }
    });

    this.create.start(event, shape);
    return shape;
  }

  private createBusinessRuleTaskEntry(): any {
    return {
      group: 'flowable',
      className: 'bpmn-icon-business-rule-task',
      title: 'Tarea de Reglas de Negocio (Drools)',
      action: {
        dragstart: (event: any) => this.createBusinessRuleTask(event),
        click: (event: any) => this.createBusinessRuleTask(event)
      }
    };
  }

  private createBusinessRuleTask(event: any): any {
    const shape = this.elementFactory.createShape({
      type: 'bpmn:BusinessRuleTask',
      businessObject: {
        type: 'bpmn:BusinessRuleTask',
        name: 'Aplicar Reglas'
      }
    });

    this.create.start(event, shape);
    return shape;
  }

  private createScriptTaskEntry(): any {
    return {
      group: 'flowable',
      className: 'bpmn-icon-script-task',
      title: 'Tarea de Script',
      action: {
        dragstart: (event: any) => this.createScriptTask(event),
        click: (event: any) => this.createScriptTask(event)
      }
    };
  }

  private createScriptTask(event: any): any {
    const shape = this.elementFactory.createShape({
      type: 'bpmn:ScriptTask',
      businessObject: {
        type: 'bpmn:ScriptTask',
        name: 'Script Task',
        scriptFormat: 'javascript'
      }
    });

    this.create.start(event, shape);
    return shape;
  }
}

// Registrar el provider
export default {
  __init__: ['customPaletteProvider'],
  customPaletteProvider: ['type', CustomPaletteProvider]
};
```

**B. Estructura Completa de la Paleta:**

La paleta debe organizarse en los siguientes grupos:

```
┌─────────────────────────────┐
│ Paleta BPMN                 │
├─────────────────────────────┤
│ 🎯 Eventos                  │
│   • Start Event (None)      │
│   • Start Event (Timer)     │
│   • Start Event (Message)   │
│   • Start Event (Signal)    │
│   • Intermediate Event      │
│   • End Event (None)        │
│   • End Event (Error)       │
│   • End Event (Terminate)   │
│                             │
│ 📋 Tareas                   │
│   • User Task               │
│   • Service Task            │
│   • Script Task             │
│   • Business Rule Task      │
│   • Manual Task             │
│   • Receive Task            │
│                             │
│ 🔀 Gateways                 │
│   • Exclusive Gateway       │
│   • Parallel Gateway        │
│   • Inclusive Gateway       │
│   • Event-based Gateway     │
│                             │
│ 📦 Subprocesos              │
│   • Sub-Process             │
│   • Call Activity           │
│   • Event Sub-Process       │
│                             │
│ ─────────────────────────── │
│ ⚙️ Flowable Personalizado   │
│   • 🤖 Agente de IA         │
│   • 💾 Operación BD         │
│   • 📧 Enviar Email          │
│   • 🌐 Llamada HTTP          │
│   • 📜 Script Task           │
│   • ⚖️ Business Rules        │
└─────────────────────────────┘
```

**C. CSS para Iconos Personalizados en la Paleta:**

```css
/* Iconos personalizados en la paleta */
.palette-entry.ai-agent-palette-icon::before {
  content: '🤖';
  font-size: 20px;
  display: inline-block;
  margin-right: 5px;
}

.palette-entry.db-operation-palette-icon::before {
  content: '💾';
  font-size: 20px;
  display: inline-block;
  margin-right: 5px;
}

.palette-entry.email-task-palette-icon::before {
  content: '📧';
  font-size: 20px;
  display: inline-block;
  margin-right: 5px;
}

.palette-entry.http-task-palette-icon::before {
  content: '🌐';
  font-size: 20px;
  display: inline-block;
  margin-right: 5px;
}

/* O usar iconos SVG de lucide-react */
.palette-entry.ai-agent-palette-icon svg {
  width: 20px;
  height: 20px;
  color: #8b5cf6;
  margin-right: 5px;
}
```

**D. Configuración del Modeler con Paleta Personalizada:**

```typescript
// Configuración del BpmnModeler
import BpmnModeler from 'bpmn-js/lib/Modeler';
import CustomPaletteProvider from './modules/custom-palette-provider';

const modeler = new BpmnModeler({
  container: '#canvas',
  additionalModules: [
    CustomPaletteProvider
  ],
  // La paleta se muestra automáticamente en el lado izquierdo
});
```

**E. Lista Completa de Componentes en la Paleta:**

| Grupo | Componente | Tipo BPMN | Icono | Descripción |
|-------|------------|-----------|-------|-------------|
| **Eventos** | Start Event (None) | `bpmn:StartEvent` | ⭕ | Evento de inicio |
| | Start Event (Timer) | `bpmn:StartEvent` | ⏰ | Inicio con temporizador |
| | Start Event (Message) | `bpmn:StartEvent` | ✉️ | Inicio con mensaje |
| | Start Event (Signal) | `bpmn:StartEvent` | 📡 | Inicio con señal |
| | Intermediate Event | `bpmn:IntermediateCatchEvent` | ⚪ | Evento intermedio |
| | End Event (None) | `bpmn:EndEvent` | ⭕ | Evento de fin |
| | End Event (Error) | `bpmn:EndEvent` | ❌ | Fin con error |
| | End Event (Terminate) | `bpmn:EndEvent` | 🛑 | Fin con terminación |
| **Tareas** | User Task | `bpmn:UserTask` | 👤 | Tarea de usuario |
| | Service Task | `bpmn:ServiceTask` | ⚙️ | Tarea de servicio |
| | Script Task | `bpmn:ScriptTask` | 📜 | Tarea de script |
| | Business Rule Task | `bpmn:BusinessRuleTask` | ⚖️ | Reglas de negocio |
| | Manual Task | `bpmn:ManualTask` | 📝 | Tarea manual |
| | Receive Task | `bpmn:ReceiveTask` | 📥 | Tarea de recepción |
| **Gateways** | Exclusive Gateway | `bpmn:ExclusiveGateway` | ❌ | Gateway exclusivo |
| | Parallel Gateway | `bpmn:ParallelGateway` | ➕ | Gateway paralelo |
| | Inclusive Gateway | `bpmn:InclusiveGateway` | ◯ | Gateway inclusivo |
| | Event-based Gateway | `bpmn:EventBasedGateway` | ⚡ | Gateway basado en eventos |
| **Subprocesos** | Sub-Process | `bpmn:SubProcess` | 📦 | Subproceso |
| | Call Activity | `bpmn:CallActivity` | 📞 | Actividad de llamada |
| | Event Sub-Process | `bpmn:SubProcess` | 📦 | Subproceso de eventos |
| **Flowable** | Agente de IA | `bpmn:ServiceTask` | 🤖 | Ejecutar agente de IA |
| | Operación BD | `bpmn:ServiceTask` | 💾 | Operación en base de datos |
| | Enviar Email | `bpmn:ServiceTask` | 📧 | Enviar email |
| | Llamada HTTP | `bpmn:ServiceTask` | 🌐 | Llamada HTTP REST |
| | Business Rules | `bpmn:BusinessRuleTask` | ⚖️ | Reglas Drools |

**F. Herramientas Adicionales en la Paleta:**

Además de los elementos, la paleta debe incluir herramientas:

- **Hand Tool** (🖐️): Para mover el canvas
- **Lasso Tool** (🪃): Para seleccionar múltiples elementos
- **Space Tool** (⬜): Para crear espacio entre elementos
- **Global Connect** (🔗): Para crear conexiones

**G. Implementación React/Next.js:**

```typescript
// En el componente del diseñador
import { useEffect, useRef } from 'react';
import BpmnModeler from 'bpmn-js/lib/Modeler';
import CustomPaletteProvider from './modules/custom-palette-provider';

export default function BPMNDesigner() {
  const containerRef = useRef<HTMLDivElement>(null);
  const modelerRef = useRef<any>(null);

  useEffect(() => {
    if (!containerRef.current || modelerRef.current) return;

    const modeler = new BpmnModeler({
      container: containerRef.current,
      additionalModules: [
        CustomPaletteProvider
      ]
    });

    modelerRef.current = modeler;

    // La paleta se muestra automáticamente en el lado izquierdo
    // bpmn-js la gestiona internamente

    return () => {
      modeler.destroy();
    };
  }, []);

  return <div ref={containerRef} style={{ width: '100%', height: '100%' }} />;
}
```

#### 2. Representación Gráfica en el Canvas

- **Icono:** Usar un icono distintivo (ej: Bot, Brain, Sparkles) para diferenciarlo de otras tareas
- **Color:** Color específico (ej: púrpura/azul) para identificar visualmente tareas de IA
- **Badge:** Mostrar el UUID del agente o nombre del agente como badge en la tarea
- **Tooltip:** Al pasar el mouse, mostrar: "Ejecutar Agente: [nombre del agente]"

#### 3. Panel de Propiedades Completo

El panel de propiedades debe tener las siguientes secciones:

**Sección 1: Información Básica**
- **ID:** Identificador único de la tarea (generado automáticamente, editable)
- **Nombre:** Nombre descriptivo de la tarea (ej: "Ejecutar Agente de Análisis")
- **Documentación:** Campo de texto multilínea para documentar la tarea

**Sección 2: Configuración del Agente**

- **Agente a Ejecutar:**
  - Tipo: Selector/Dropdown con búsqueda
  - Fuente de datos: Llamada a API `/api/v1/agents` para obtener lista de agentes disponibles
  - Formato: `{ uuid: string, name: string, description: string }`
  - Validación: Campo requerido
  - Visualización: Mostrar nombre del agente seleccionado, con opción de ver detalles

**Sección 3: Prompt**

- **Prompt:**
  - Tipo: Textarea multilínea (mínimo 3 filas)
  - Placeholder: "Ingrese el prompt para el agente. Puede usar expresiones ${variableName}"
  - Validación: Campo requerido, mínimo 10 caracteres
  - Ayuda: Botón de ayuda mostrando ejemplos de prompts y cómo usar expresiones
  - Vista previa: Mostrar preview del prompt con variables resueltas (si es posible)

**Sección 4: Variables de Entrada**

- **Editor de Variables de Entrada:**
  - Tipo: Tabla editable con dos columnas:
    - **Nombre en Contexto del Agente** (columna izquierda): Nombre que el agente espera recibir
    - **Variable del Proceso BPMN** (columna derecha): Variable del proceso que se mapea
  - Funcionalidades:
    - Botón "+ Agregar Variable" para añadir nuevas filas
    - Botón "🗑️" en cada fila para eliminar
    - Autocompletado en la columna derecha con variables disponibles en el proceso
    - Validación: No permitir nombres duplicados en la columna izquierda
    - Validación: La variable del proceso debe existir o ser válida
  - Formato de salida: JSON automático
    ```json
    {
      "document": "documentContent",
      "language": "documentLanguage",
      "userId": "currentUserId"
    }
    ```

**Sección 5: Variables de Salida**

- **Prefijo de Variables de Salida:**
  - Tipo: Input de texto
  - Placeholder: "analysis" (ejemplo)
  - Descripción: "Todas las variables de salida del agente tendrán este prefijo"
  - Validación: Solo letras, números y guiones bajos
  - Ayuda: Mostrar ejemplos de variables generadas (ej: `analysis_agentResponse`, `analysis_entities`)

- **Vista Previa de Variables de Salida:**
  - Mostrar lista de variables que se crearán automáticamente:
    - `{prefijo}_agentResponse`
    - `{prefijo}_agentMetadata`
    - `{prefijo}_executionTime`
    - Y cualquier campo adicional de la respuesta del agente

**Sección 6: Configuración Avanzada (Opcional, Colapsable)**

- **Async:** Checkbox (marcado por defecto)
- **Exclusive:** Checkbox (marcado por defecto)
- **Timeout:** Input numérico en segundos (opcional)
- **Retry Policy:** Configuración de reintentos (opcional)

#### 4. Ejemplo de Interfaz del Panel de Propiedades

```
┌─────────────────────────────────────────────────────────┐
│ Propiedades: Ejecutar Agente de IA                     │
├─────────────────────────────────────────────────────────┤
│                                                         │
│ Información Básica                                      │
│ ┌───────────────────────────────────────────────────┐ │
│ │ ID: executeAIAgentTask                           │ │
│ │ Nombre: Ejecutar Agente de Análisis              │ │
│ │ Documentación: [textarea]                        │ │
│ └───────────────────────────────────────────────────┘ │
│                                                         │
│ Configuración del Agente                               │
│ ┌───────────────────────────────────────────────────┐ │
│ │ Agente: [🔍 Buscar Agente...]                    │ │
│ │   Seleccionado: "Analizador de Documentos"       │ │
│ │   UUID: 550e8400-e29b-41d4-a716-446655440000     │ │
│ └───────────────────────────────────────────────────┘ │
│                                                         │
│ Prompt                                                 │
│ ┌───────────────────────────────────────────────────┐ │
│ │ [textarea con 5 filas]                          │ │
│ │ Analiza el siguiente documento y extrae las     │ │
│ │ entidades principales: ${documentContent}        │ │
│ └───────────────────────────────────────────────────┘ │
│                                                         │
│ Variables de Entrada                                   │
│ ┌───────────────────────────────────────────────────┐ │
│ │ Nombre en Agente │ Variable del Proceso          │ │
│ ├──────────────────┼───────────────────────────────┤ │
│ │ document         │ documentContent        [🗑️]   │ │
│ │ language         │ documentLanguage      [🗑️]   │ │
│ │ userId           │ currentUserId         [🗑️]   │ │
│ └───────────────────────────────────────────────────┘ │
│ [+ Agregar Variable]                                  │
│                                                         │
│ Variables de Salida                                    │
│ ┌───────────────────────────────────────────────────┐ │
│ │ Prefijo: [analysis________________]               │ │
│ │                                                    │ │
│ │ Variables que se crearán:                         │ │
│ │ • analysis_agentResponse                          │ │
│ │ • analysis_agentMetadata                         │ │
│ │ • analysis_executionTime                         │ │
│ └───────────────────────────────────────────────────┘ │
│                                                         │
│ [▼] Configuración Avanzada                            │
│   ☑ Async                                             │
│   ☑ Exclusive                                         │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

#### 5. Validaciones en el Diseñador

El diseñador debe validar:

1. **Agente seleccionado:** Debe existir y estar activo
2. **Prompt:** No puede estar vacío, mínimo 10 caracteres
3. **Variables de entrada:**
   - No puede haber nombres duplicados en la columna izquierda
   - Las variables del proceso deben ser válidas (pueden no existir aún, pero deben tener formato válido)
4. **Prefijo de salida:** Solo caracteres alfanuméricos y guiones bajos
5. **JSON de variables:** Debe ser JSON válido antes de guardar

#### 6. Generación del XML BPMN

Cuando el usuario guarda la tarea, el diseñador debe generar el siguiente XML:

```xml
<serviceTask id="executeAIAgentTask" name="Ejecutar Agente de Análisis"
             flowable:type="ai-agent"
             flowable:delegateExpression="${executeAIAgentDelegate}"
             flowable:async="true"
             flowable:exclusive="true">
  <extensionElements>
    <flowable:field name="agentUuid">
      <flowable:string>550e8400-e29b-41d4-a716-446655440000</flowable:string>
    </flowable:field>
    <flowable:field name="prompt">
      <flowable:string>Analiza el siguiente documento y extrae las entidades principales: ${documentContent}</flowable:string>
    </flowable:field>
    <flowable:field name="inputVariables">
      <flowable:string>{"document": "documentContent", "language": "documentLanguage", "userId": "currentUserId"}</flowable:string>
    </flowable:field>
    <flowable:field name="outputVariablePrefix">
      <flowable:string>analysis</flowable:string>
    </flowable:field>
  </extensionElements>
</serviceTask>
```

#### 7. Carga de Configuración desde XML

Al cargar un proceso BPMN existente, el diseñador debe:

1. Detectar el tipo `flowable:type="ai-agent"`
2. Leer los campos de `extensionElements`:
   - `agentUuid` → Cargar en selector de agente
   - `prompt` → Cargar en textarea
   - `inputVariables` → Parsear JSON y cargar en tabla
   - `outputVariablePrefix` → Cargar en input de prefijo
3. Validar que el agente existe (mostrar advertencia si no)
4. Mostrar la configuración en el panel de propiedades

#### 8. API para Obtener Agentes Disponibles

El diseñador debe llamar a una API para obtener la lista de agentes:

```typescript
// Ejemplo de llamada API
async function getAvailableAgents(): Promise<Agent[]> {
  const response = await fetch('/api/v1/agents?status=ACTIVE');
  return response.json();
}

interface Agent {
  uuid: string;
  name: string;
  description: string;
  status: 'ACTIVE' | 'INACTIVE';
  category?: string;
}
```

#### 9. Integración Completa con bpmn-js

Para integrar completamente este tipo personalizado en bpmn-js con icono personalizado y propiedades, se necesita:

**A. Módulo de Renderizado Personalizado (Icono Bot):**

```typescript
// modules/ai-agent-renderer.ts
import { BaseRenderer } from 'diagram-js/lib/draw/BaseRenderer';

export default class AIAgentRenderer extends BaseRenderer {
  static $inject = ['eventBus', 'bpmnRenderer', 'config'];

  constructor(eventBus: any, bpmnRenderer: any, config: any) {
    super(eventBus, 1500); // Prioridad alta

    this.bpmnRenderer = bpmnRenderer;
    this.config = config;
  }

  canRender(element: any): boolean {
    // Identificar tareas con flowable:type="ai-agent"
    return element.type === 'bpmn:ServiceTask' &&
           this.isAIAgentTask(element);
  }

  drawShape(parentNode: any, element: any): any {
    // Primero renderizar la forma base
    const shape = this.bpmnRenderer.drawShape(parentNode, element);

    // Añadir icono personalizado tipo bot
    const icon = this.createBotIcon();
    shape.appendChild(icon);

    // Añadir clase CSS para estilos personalizados
    shape.classList.add('ai-agent-task');

    return shape;
  }

  private isAIAgentTask(element: any): boolean {
    const businessObject = element.businessObject;
    return businessObject?.get('flowable:type') === 'ai-agent';
  }

  private createBotIcon(): SVGElement {
    const iconGroup = document.createElementNS('http://www.w3.org/2000/svg', 'g');
    iconGroup.setAttribute('class', 'ai-agent-icon');
    iconGroup.setAttribute('transform', 'translate(10, 10)');

    // Crear SVG del icono Bot
    const svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
    svg.setAttribute('width', '24');
    svg.setAttribute('height', '24');
    svg.setAttribute('viewBox', '0 0 24 24');
    svg.setAttribute('fill', 'none');
    svg.style.stroke = '#8b5cf6';
    svg.style.strokeWidth = '2';

    // Path del icono Bot (ajustar según icono real de lucide-react Bot)
    const path = document.createElementNS('http://www.w3.org/2000/svg', 'path');
    path.setAttribute('d', 'M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm-1-13h2v6h-2zm0 8h2v2h-2z');

    svg.appendChild(path);
    iconGroup.appendChild(svg);

    return iconGroup;
  }
}

// Registrar el módulo
export default {
  __init__: ['aiAgentRenderer'],
  aiAgentRenderer: ['type', AIAgentRenderer]
};
```

**B. Módulo de Propiedades Personalizado:**

```typescript
// modules/ai-agent-properties.ts
import { PropertiesPanel } from 'bpmn-js-properties-panel';
import { PropertiesProvider } from '@bpmn-io/properties-panel';

export default class AIAgentPropertiesProvider extends PropertiesProvider {
  constructor(eventBus: any, bpmnFactory: any, canvas: any) {
    super(eventBus, bpmnFactory, canvas);
  }

  getGroups(element: any): any[] {
    const groups = super.getGroups(element);

    // Si es una tarea de agente de IA, añadir grupo personalizado
    if (this.isAIAgentTask(element)) {
      groups.push(this.createAIAgentGroup(element));
    }

    return groups;
  }

  private isAIAgentTask(element: any): boolean {
    const businessObject = element.businessObject;
    return businessObject?.get('flowable:type') === 'ai-agent';
  }

  private createAIAgentGroup(element: any): any {
    return {
      id: 'ai-agent-config',
      label: 'Configuración del Agente de IA',
      entries: [
        {
          id: 'agentUuid',
          label: 'Agente',
          type: 'select',
          modelProperty: 'flowable:agentUuid',
          get: (element: any) => {
            return element.businessObject?.get('flowable:agentUuid');
          },
          set: (element: any, value: any) => {
            element.businessObject.set('flowable:agentUuid', value);
          },
          selectOptions: this.getAvailableAgents()
        },
        {
          id: 'prompt',
          label: 'Prompt',
          type: 'textarea',
          modelProperty: 'flowable:prompt',
          get: (element: any) => {
            return element.businessObject?.get('flowable:prompt');
          },
          set: (element: any, value: any) => {
            element.businessObject.set('flowable:prompt', value);
          }
        },
        {
          id: 'inputVariables',
          label: 'Variables de Entrada (JSON)',
          type: 'text',
          modelProperty: 'flowable:inputVariables',
          get: (element: any) => {
            return element.businessObject?.get('flowable:inputVariables');
          },
          set: (element: any, value: any) => {
            element.businessObject.set('flowable:inputVariables', value);
          }
        },
        {
          id: 'outputVariablePrefix',
          label: 'Prefijo Variables de Salida',
          type: 'text',
          modelProperty: 'flowable:outputVariablePrefix',
          get: (element: any) => {
            return element.businessObject?.get('flowable:outputVariablePrefix');
          },
          set: (element: any, value: any) => {
            element.businessObject.set('flowable:outputVariablePrefix', value);
          }
        }
      ]
    };
  }

  private getAvailableAgents(): any[] {
    // Llamar a API para obtener agentes disponibles
    // Retornar formato: [{ value: 'uuid', label: 'Nombre' }]
    return [];
  }
}

// Registrar el provider
export default {
  __init__: ['aiAgentPropertiesProvider'],
  aiAgentPropertiesProvider: ['type', AIAgentPropertiesProvider]
};
```

**C. Configuración del Modeler:**

```typescript
// Configuración del BpmnModeler con módulos personalizados
import BpmnModeler from 'bpmn-js/lib/Modeler';
import AIAgentRenderer from './modules/ai-agent-renderer';
import AIAgentPropertiesProvider from './modules/ai-agent-properties';

const modeler = new BpmnModeler({
  container: '#canvas',
  additionalModules: [
    AIAgentRenderer,
    AIAgentPropertiesProvider
  ],
  propertiesPanel: {
    parent: '#properties-panel'
  }
});
```

**D. CSS para Estilos Personalizados (Icono y Visualización):**

```css
/* Estilos para tareas de agente de IA */
.bpmn-container .ai-agent-task {
  border: 2px solid #8b5cf6 !important;
  border-radius: 4px;
}

.bpmn-container .ai-agent-task .djs-visual > :first-child {
  fill: #f3e8ff !important; /* Fondo púrpura claro */
}

.bpmn-container .ai-agent-icon {
  pointer-events: none;
  z-index: 10;
}

.bpmn-container .ai-agent-icon svg {
  filter: drop-shadow(0 2px 4px rgba(139, 92, 246, 0.3));
}

/* Badge con nombre del agente */
.bpmn-container .ai-agent-task::after {
  content: attr(data-agent-name);
  position: absolute;
  bottom: -20px;
  left: 50%;
  transform: translateX(-50%);
  background: #8b5cf6;
  color: white;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 10px;
  white-space: nowrap;
}
```

**E. Resumen de Propiedades para Identificación:**

- **Propiedad XML:** `flowable:type="ai-agent"` - Identifica el tipo de tarea
- **Icono:** Bot personalizado renderizado en el canvas
- **Color:** Púrpura (#8b5cf6) para diferenciación visual
- **Panel de Propiedades:** Grupo personalizado "Configuración del Agente de IA" con campos específicos

### Mejores Prácticas para Tareas Personalizadas

1. **Usar JavaDelegate para la mayoría de casos:** Es más simple y suficiente para la mayoría de necesidades.

2. **Usar ActivityBehavior solo cuando sea necesario:** Cuando necesites control total sobre el flujo de ejecución.

3. **Manejo de Errores:**
   ```java
   try {
       // Lógica de la tarea
   } catch (Exception e) {
       execution.setVariable("errorMessage", e.getMessage());
       execution.setVariable("errorStatus", "failed");
       throw new BpmnError("CUSTOM_ERROR", e.getMessage());
   }
   ```

4. **Tareas Asíncronas:**
   - Usar `flowable:async="true"` para tareas que pueden tardar
   - Usar `flowable:exclusive="true"` (default) para evitar problemas de concurrencia

5. **Inyección de Dependencias:**
   - Usar `delegateExpression` con beans de Spring en lugar de `class` directa
   - Permite inyección de dependencias y testing más fácil

6. **Logging:**
   ```java
   import org.slf4j.Logger;
   import org.slf4j.LoggerFactory;

   private static final Logger logger = LoggerFactory.getLogger(MyDelegate.class);

   @Override
   public void execute(DelegateExecution execution) {
       logger.info("Ejecutando tarea personalizada para proceso: {}",
                   execution.getProcessInstanceId());
       // ...
   }
   ```

7. **Variables de Proceso:**
   - Usar nombres descriptivos para variables
   - Documentar qué variables espera y produce la tarea
   - Validar variables requeridas al inicio

### Registro de Tipos Personalizados en Flowable

Para que Flowable reconozca tipos personalizados, puedes extender el parser de BPMN:

```java
package com.codeflowx.govern.workflow.parser;

import org.flowable.bpmn.model.ServiceTask;
import org.flowable.engine.impl.bpmn.parser.BpmnParse;
import org.flowable.engine.impl.bpmn.parser.handler.ServiceTaskParseHandler;

public class CustomServiceTaskParseHandler extends ServiceTaskParseHandler {

    @Override
    protected void executeParse(BpmnParse bpmnParse, ServiceTask serviceTask) {
        String customType = serviceTask.getAttributeValue("http://flowable.org/bpmn", "type");

        if ("ai-agent".equals(customType)) {
            // Configurar comportamiento específico para tipo ai-agent
            serviceTask.setImplementationType("delegateExpression");
            serviceTask.setImplementation("${executeAIAgentDelegate}");
        } else if ("db-operation".equals(customType)) {
            // Configurar comportamiento específico para tipo db-operation
            serviceTask.setImplementationType("delegateExpression");
            serviceTask.setImplementation("${databaseOperationDelegate}");
        }

        // Llamar al handler padre para procesamiento estándar
        super.executeParse(bpmnParse, serviceTask);
    }
}
```

## Extensiones Flowable Comunes

### Asynchronous Continuations (Continuaciones Asíncronas)

Permite ejecutar tareas de forma asíncrona:

```xml
<serviceTask id="asyncTask" name="Tarea Asíncrona"
             flowable:async="true"
             flowable:exclusive="true">
</serviceTask>
```

**Atributos:**
- `flowable:async="true"`: Ejecuta la tarea de forma asíncrona
- `flowable:exclusive="true"`: Ejecuta de forma exclusiva (default)

### Fail Retry (Reintentos en caso de fallo)

Configura reintentos automáticos:

```xml
<serviceTask id="retryTask" name="Tarea con Reintentos"
             flowable:async="true">
  <extensionElements>
    <flowable:failedJobRetryTimeCycle>R3/PT10S</flowable:failedJobRetryTimeCycle>
  </extensionElements>
</serviceTask>
```

### Execution Listeners (Listeners de Ejecución)

Ejecuta código antes/después de la ejecución:

```xml
<serviceTask id="taskWithListener" name="Tarea con Listener">
  <extensionElements>
    <flowable:executionListener event="start"
                                class="com.example.StartListener"/>
    <flowable:executionListener event="end"
                                expression="${endHandler.handle(execution)}"/>
  </extensionElements>
</serviceTask>
```

### Task Listeners (Listeners de Tarea)

Solo para User Tasks, ejecuta código en eventos de la tarea:

```xml
<userTask id="taskWithTaskListener" name="Tarea con Task Listener">
  <extensionElements>
    <flowable:taskListener event="create"
                           class="com.example.TaskCreateListener"/>
    <flowable:taskListener event="assignment"
                           expression="${assignmentHandler.handle(task)}"/>
    <flowable:taskListener event="complete"
                           delegateExpression="${completeHandlerBean}"/>
  </extensionElements>
</userTask>
```

### Multi-Instance (Para Cada / Multi-Instancia)

Ejecuta la tarea múltiples veces:

```xml
<userTask id="multiInstanceTask" name="Revisar Múltiples Items">
  <multiInstanceLoopCharacteristics
      flowable:collection="${items}"
      flowable:elementVariable="item">
    <completionCondition>${nrOfCompletedInstances == nrOfInstances}</completionCondition>
  </multiInstanceLoopCharacteristics>
</userTask>
```

**Atributos importantes:**
- `flowable:collection`: Colección a iterar
- `flowable:elementVariable`: Variable que contiene cada elemento
- `flowable:isSequential`: Si es true, ejecuta secuencialmente (default: false)
- `completionCondition`: Condición para completar todas las instancias

## Cómo Representar en el Diseñador BPMN

### Identificación de Tipos

El diseñador debe identificar el tipo de tarea basándose en:

1. **Elemento XML base:**
   - `<userTask>` → User Task
   - `<scriptTask>` → Script Task
   - `<serviceTask>` → Service Task (requiere verificar `flowable:type` o `flowable:class`)
   - `<businessRuleTask>` → Business Rule Task
   - `<manualTask>` → Manual Task
   - `<receiveTask>` → Receive Task

2. **Atributo `flowable:type`** (para Service Tasks):
   - `mail` → Email Task
   - `http` → Http Task
   - `camel` → Camel Task
   - `shell` → Shell Task
   - `externalWorker` → External Worker Task
   - Tipos personalizados (ej: `ai-agent`, `db-operation`, `custom-api`) → Custom Task
   - Sin tipo o con `flowable:class`/`flowable:expression` → Java Service Task

3. **Atributo `flowable:class`** (para Service Tasks):
   - Si existe → Java Service Task o Custom Task (depende del tipo)

4. **Atributo `flowable:expression`** (para Service Tasks):
   - Si existe → Java Service Task (expresión)

5. **Atributo `flowable:delegateExpression`** (para Service Tasks):
   - Si existe → Java Service Task o Custom Task (usando bean de Spring)

### Representación Visual

Cada tipo de tarea debe tener:

1. **Icono distintivo** en el diseñador
2. **Panel de propiedades** específico con campos relevantes
3. **Validación** de campos requeridos según el tipo

### Campos Comunes en el Panel de Propiedades

Todos los tipos de tareas deben mostrar:
- **ID**: Identificador único
- **Name**: Nombre de la tarea
- **Documentation**: Documentación/descripción

### Campos Específicos por Tipo

#### User Task

**Campos Básicos:**
- Assignee
- Candidate Users
- Candidate Groups
- Due Date
- Priority
- Form Key (para formularios predefinidos)

**Formularios Dinámicos:**

El diseñador debe proporcionar una interfaz para crear y editar formularios dinámicos directamente en la User Task. Esto incluye:

1. **Selector de Tipo de Formulario:**
   - Formulario Predefinido (usando `flowable:formKey`)
   - Formulario Dinámico (usando `formProperties` en XML)

2. **Editor de Campos del Formulario Dinámico:**
   - **Agregar Campo:** Botón para añadir nuevos campos al formulario
   - **Lista de Campos:** Tabla o lista mostrando todos los campos definidos
   - **Editar Campo:** Permite modificar las propiedades de cada campo
   - **Eliminar Campo:** Permite eliminar campos del formulario
   - **Reordenar Campos:** Permite cambiar el orden de los campos (drag & drop)

3. **Propiedades de Cada Campo:**
   - **ID:** Identificador único (mapea a variable de proceso)
   - **Name/Label:** Etiqueta visible del campo
   - **Type:** Selector de tipo (string, long, double, date, boolean, enum)
   - **Required:** Checkbox para marcar como obligatorio
   - **Readable:** Checkbox o expresión para visibilidad
   - **Writable:** Checkbox o expresión para editabilidad
   - **Variable:** Nombre de variable de proceso (opcional, por defecto usa ID)
   - **Expression:** Expresión para valor calculado o validación
   - **Default Value:** Valor por defecto del campo
   - **Validations:** Para tipos numéricos: min, max. Para strings: minLength, maxLength, pattern

4. **Campos Específicos por Tipo:**
   - **Enum/Dropdown:** Editor para agregar opciones (id, name)
   - **Date:** Selector de formato de fecha
   - **Number:** Campos para min/max
   - **String:** Campos para minLength/maxLength/pattern

5. **Visibilidad Condicional:**
   - Editor de expresiones para `readable` y `writable`
   - Autocompletado de variables de proceso disponibles
   - Validación de sintaxis de expresiones

6. **Mapeo de Variables:**
   - **Input Mapping:** Tabla mostrando variables de proceso → campos del formulario
   - **Output Mapping:** Tabla mostrando campos del formulario → variables de proceso
   - Editor para agregar/editar/eliminar mapeos

7. **Vista Previa del Formulario:**
   - Renderizado visual del formulario tal como se verá en tiempo de ejecución
   - Actualización en tiempo real al modificar campos

**Ejemplo de Interfaz del Diseñador para Formulario Dinámico:**

```
┌─────────────────────────────────────────────────────────┐
│ User Task: Aprobar Solicitud                            │
├─────────────────────────────────────────────────────────┤
│                                                         │
│ [X] Usar Formulario Dinámico                          │
│                                                         │
│ Campos del Formulario:                                 │
│ ┌───────────────────────────────────────────────────┐ │
│ │ ID        │ Label          │ Type    │ Required │ │
│ ├───────────┼────────────────┼─────────┼──────────┤ │
│ │ requestId │ ID Solicitud   │ string  │ [✓]      │ │
│ │ decision  │ Decisión       │ enum    │ [✓]      │ │
│ │ comments  │ Comentarios    │ string  │ [✓]      │ │
│ └───────────────────────────────────────────────────┘ │
│                                                         │
│ [+ Agregar Campo]                                      │
│                                                         │
│ Campo Seleccionado: "decision"                         │
│ ┌───────────────────────────────────────────────────┐ │
│ │ ID: decision                                       │ │
│ │ Label: Decisión                                    │ │
│ │ Type: [enum ▼]                                    │ │
│ │ Required: [✓]                                     │ │
│ │ Variable: approvalDecision                        │ │
│ │                                                    │ │
│ │ Opciones:                                          │ │
│ │   ID: approved  Name: Aprobar    [✕]             │ │
│ │   ID: rejected  Name: Rechazar  [✕]             │ │
│ │   [+ Agregar Opción]                              │ │
│ └───────────────────────────────────────────────────┘ │
│                                                         │
│ Mapeo de Variables:                                    │
│ ┌───────────────────────────────────────────────────┐ │
│ │ Input:  processRequestId → requestId              │ │
│ │ Output: decision → approvalDecision              │ │
│ └───────────────────────────────────────────────────┘ │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

**Validaciones que el Diseñador debe Realizar:**

1. **ID único:** Cada campo debe tener un ID único dentro del formulario
2. **Tipo válido:** El tipo seleccionado debe ser compatible con Flowable
3. **Expresiones válidas:** Las expresiones en `readable`, `writable`, `expression` deben tener sintaxis válida
4. **Enum con opciones:** Los campos de tipo `enum` deben tener al menos una opción definida
5. **Variables existentes:** Si se especifica `variable`, debe validarse que la variable esté definida en el proceso (opcional, puede ser una advertencia)
6. **Validaciones numéricas:** Para tipos numéricos, `min` debe ser menor que `max`
7. **Validaciones de string:** Para strings, `minLength` debe ser menor o igual que `maxLength`

#### Script Task
- Script Format (javascript, groovy, etc.)
- Script Content

#### Java Service Task
- Implementation Type (Class, Expression, Delegate Expression)
- Class/Expression/Delegate Expression value
- Async
- Exclusive

#### Email Task
- To
- From
- Subject
- HTML/Text Content

#### Http Task
- Request Method
- Request URL
- Request Headers
- Request Body
- Response Variable Name

#### Business Rule Task
- Rule Variables Input
- Result Variable
- Rules (lista de IDs)

#### Shell Task
- Command
- Arguments
- Wait

#### External Worker Task
- Topic

#### Custom Tasks (Tareas Personalizadas)

El diseñador debe permitir crear y editar tareas personalizadas. Para cada tipo personalizado:

1. **Registro de Tipos Personalizados:**
   - El diseñador debe tener un catálogo de tipos personalizados disponibles
   - Cada tipo debe tener:
     - ID único (ej: `ai-agent`, `db-operation`, `custom-api`)
     - Nombre descriptivo
     - Icono representativo
     - Clase Java asociada o delegateExpression
     - Campos específicos del tipo

2. **Panel de Propiedades para Custom Tasks:**
   - **Tipo de Tarea:** Selector del tipo personalizado
   - **Implementación:**
     - Opción 1: Clase Java (`flowable:class`)
     - Opción 2: Delegate Expression (`flowable:delegateExpression`) - Recomendado
   - **Configuración Específica:** Campos dinámicos según el tipo seleccionado
   - **Async:** Checkbox para ejecución asíncrona
   - **Exclusive:** Checkbox para ejecución exclusiva

3. **Ejemplo de Configuración para "AI Agent" Task:**
   - Agent UUID (variable o expresión)
   - Prompt (variable o expresión)
   - Context (variable o expresión)
   - Timeout (opcional)
   - Retry Policy (opcional)

4. **Ejemplo de Configuración para "Database Operation" Task:**
   - Operation Type (SELECT, INSERT, UPDATE, DELETE)
   - Query/SQL Statement
   - Parameters (lista de parámetros)
   - Result Variable Name

5. **Ejemplo de Configuración para "Custom REST API" Task:**
   - API URL (variable o expresión)
   - HTTP Method (GET, POST, PUT, DELETE, etc.)
   - Headers (mapa de headers)
   - Request Body (variable o expresión)
   - Response Variable Name

6. **Validaciones:**
   - Verificar que la clase Java existe (si se usa `flowable:class`)
   - Verificar que el delegateExpression resuelve a un bean válido (si se usa `flowable:delegateExpression`)
   - Validar campos requeridos según el tipo personalizado
   - Validar expresiones si se usan en campos

7. **Representación Visual:**
   - Icono distintivo para cada tipo personalizado
   - Badge o etiqueta mostrando el tipo personalizado
   - Color diferenciado en el canvas

## Ejemplo Completo

```xml
<process id="exampleProcess" name="Proceso de Ejemplo" isExecutable="true">
  <startEvent id="start"/>

  <!-- User Task -->
  <userTask id="reviewTask" name="Revisar Documento">
    <extensionElements>
      <flowable:candidateGroups>reviewers</flowable:candidateGroups>
      <flowable:dueDate>${dueDate}</flowable:dueDate>
    </extensionElements>
  </userTask>

  <!-- Java Service Task -->
  <serviceTask id="processTask" name="Procesar Datos"
               flowable:class="com.example.DataProcessor"
               flowable:async="true">
  </serviceTask>

  <!-- Email Task -->
  <serviceTask id="notifyTask" name="Enviar Notificación"
               flowable:type="mail">
    <extensionElements>
      <flowable:field name="to">
        <flowable:string>${recipient}</flowable:string>
      </flowable:field>
      <flowable:field name="subject">
        <flowable:string>Proceso Completado</flowable:string>
      </flowable:field>
    </extensionElements>
  </serviceTask>

  <!-- Business Rule Task -->
  <businessRuleTask id="rulesTask" name="Aplicar Reglas">
    <extensionElements>
      <flowable:rule>
        <flowable:ruleId>approvalRule</flowable:ruleId>
      </flowable:rule>
    </extensionElements>
  </businessRuleTask>

  <endEvent id="end"/>

  <sequenceFlow id="flow1" sourceRef="start" targetRef="reviewTask"/>
  <sequenceFlow id="flow2" sourceRef="reviewTask" targetRef="processTask"/>
  <sequenceFlow id="flow3" sourceRef="processTask" targetRef="notifyTask"/>
  <sequenceFlow id="flow4" sourceRef="notifyTask" targetRef="rulesTask"/>
  <sequenceFlow id="flow5" sourceRef="rulesTask" targetRef="end"/>
</process>
```

## Notas Importantes

1. **Namespace Flowable:** Todos los atributos y elementos personalizados de Flowable usan el prefijo `flowable:`
2. **Compatibilidad:** Flowable también soporta el prefijo `activiti:` para compatibilidad hacia atrás
3. **Extension Elements:** Los campos personalizados se definen dentro de `<extensionElements>`
4. **Expresiones:** Se pueden usar expresiones `${...}` en la mayoría de los campos
5. **Async por defecto:** Las tareas asíncronas son exclusivas por defecto para evitar problemas de concurrencia

## Referencias

- Documentación oficial Flowable: https://www.flowable.com/open-source/docs/bpmn/ch07b-BPMN-Constructs
- BPMN 2.0 Specification: http://www.omg.org/spec/BPMN/2.0/
