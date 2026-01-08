# 📊 ESTADO ACTUAL - DISEÑADOR BPMN

**Fecha de actualización:** Diciembre 2025
**Basado en:** Revisión del código fuente actual
**Última revisión:** 2025-12-19

---

## 📋 RESUMEN EJECUTIVO

El módulo BPMN Designer está en un estado avanzado de implementación. La mayoría de las funcionalidades principales están implementadas, incluyendo:

- ✅ Diseñador visual completo basado en bpmn-js
- ✅ Propiedades personalizadas para elementos Flowable
- ✅ Renderizado personalizado de iconos para tareas especializadas
- ✅ Exportación a PNG y XML
- ✅ Auto-layout para procesos sin información de diagrama
- ✅ Paleta personalizada de componentes
- ✅ Panel de propiedades colapsable

**Estado general:** Funcional con mejoras pendientes en la carga/lectura de propiedades desde XML.

---

## 🏗️ ARQUITECTURA DEL MÓDULO

### Estructura de Directorios

```
app/(app)/bpmn-designer/
├── page.tsx                           # Componente principal del diseñador
├── bpmn-styles.css                    # Estilos personalizados BPMN
├── components/
│   └── CustomPalette.tsx              # Paleta personalizada de componentes
├── modules/
│   ├── custom-properties-provider.ts           # Provider de propiedades personalizadas
│   ├── custom-properties-provider-module.ts    # Módulo del provider
│   ├── mail-task-renderer.ts                   # Renderer para tareas personalizadas
│   ├── mail-task-renderer-module.ts            # Módulo del renderer
│   ├── custom-palette-provider.ts              # Provider de paleta (no usado actualmente)
│   └── flowable-moddle-extension.json          # Extensiones Flowable para moddle
└── utils/
    ├── autoLayout.ts                  # Auto-layout para procesos sin DI
    ├── bpmnParser.ts                  # Parser de BPMN (si existe)
    └── bpmnGenerator.ts               # Generador de BPMN (si existe)
```

---

## ✅ FUNCIONALIDADES IMPLEMENTADAS

### 1. Diseñador Visual (bpmn-js)

**Estado:** ✅ Completamente funcional

- Integración completa con bpmn-js Modeler
- Canvas interactivo con zoom, pan, y selección
- Controles de zoom (acercar, alejar, ajustar a ventana)
- Grid de fondo opcional (activado/desactivado)
- Multi-selección con Shift+Click
- Ocultación de paleta nativa de bpmn-js
- Eliminación de watermark/enlace a bpmn.io

**Archivos principales:**
- `page.tsx` (líneas 60-336: inicialización del modeler)

### 2. Paleta Personalizada de Componentes

**Estado:** ✅ Implementada y funcional

- Paleta colapsable con categorías:
  - Eventos (Inicio, Fin, Intermedios, Timer, Mensaje, Error)
  - Tareas (Usuario, Servicio, Script, Manual, Envío, Recepción, Regla de Negocio)
  - Tareas Personalizadas (Agente IA, Operación BD, Email, HTTP)
  - Gateways (Exclusivo, Paralelo, Inclusivo, Basado en Eventos)
  - Subprocesos (Subproceso, Actividad de Llamada)
  - Artefactos (Objeto de Datos, Almacén de Datos, Anotación)

- Iconos personalizados para cada tipo de elemento
- Creación de elementos en el centro del viewport
- Configuración automática de businessObject para elementos personalizados

**Archivos principales:**
- `components/CustomPalette.tsx`
- `page.tsx` (líneas 826-955: integración de paleta)

### 3. Propiedades Personalizadas

**Estado:** ⚠️ Implementado pero con problemas en la lectura desde XML

**Tipos de elementos soportados:**

#### 3.1. UserTask (`bpmn:UserTask`)
- ✅ `flowable:assignee` - Usuario asignado
- ✅ `flowable:candidateUsers` - Usuarios candidatos
- ✅ `flowable:candidateGroups` - Grupos candidatos
- ✅ `flowable:formKey` - Clave del formulario
- ✅ `flowable:dueDate` - Fecha límite
- ✅ `flowable:priority` - Prioridad (0-100)

#### 3.2. ServiceTask (`bpmn:ServiceTask`)
- ✅ `flowable:class` - Clase Java
- ✅ `flowable:expression` - Expresión
- ✅ `flowable:delegateExpression` - Expresión delegada
- ✅ `flowable:resultVariable` - Variable de resultado

#### 3.3. ScriptTask (`bpmn:ScriptTask`)
- ✅ `scriptFormat` - Formato de script (javascript, groovy, python, etc.)
- ✅ `script` - Contenido del script (CDATA) - **textarea**
- ✅ `flowable:resultVariable` - Variable de resultado (Flowable)

#### 3.4. BusinessRuleTask (`bpmn:BusinessRuleTask`)
- ✅ `flowable:ruleVariablesInput` - Variables de entrada (textarea)
- ✅ `flowable:rules` - Reglas Drools (textarea)
- ✅ `flowable:resultVariable` - Variable de resultado
- ✅ `flowable:exclude` - Excluir reglas por defecto

#### 3.5. SendTask / ReceiveTask / ManualTask / CallActivity
- ✅ Propiedades básicas implementadas

#### 3.6. Tareas Personalizadas (Mail, AI Agent, DB Operation, HTTP)

**Mail Task (flowable:type="mail"):**
- ✅ `flowable:to` - Destinatario (expression o string)
- ✅ `flowable:from` - Remitente (string)
- ✅ `flowable:subject` - Asunto (expression o string)
- ✅ `flowable:html` - Cuerpo HTML (textarea) - **Campo largo**

**AI Agent (flowable:type="ai-agent"):**
- ✅ `flowable:agentUuid` - UUID del agente
- ✅ `flowable:prompt` - Prompt (textarea)
- ✅ `flowable:inputVariables` - Variables de entrada JSON (textarea)
- ✅ `flowable:outputVariablePrefix` - Prefijo variables salida

**DB Operation (flowable:type="db-operation"):**
- ✅ `flowable:query` - Consulta SQL (textarea)
- ✅ `flowable:resultVariable` - Variable de resultado

**HTTP Service (flowable:type="http"):**
- ✅ `flowable:requestUrl` - URL del endpoint
- ✅ `flowable:requestMethod` - Método HTTP
- ✅ `flowable:requestHeaders` - Headers HTTP JSON (textarea)
- ✅ `flowable:requestBody` - Cuerpo de petición (textarea)

**Problemas identificados:**

1. **⚠️ CRÍTICO: Lectura de propiedades desde XML**
   - Los valores de las propiedades no se cargan correctamente desde el XML
   - Especialmente problemático para Mail Tasks que usan `extensionElements/flowable:field`
   - El método `getValue()` intenta múltiples formas de acceso pero puede fallar
   - Logs muestran valores vacíos aunque existan en el XML

2. **⚠️ Campo HTML en Mail Task**
   - El campo `html` se implementa como textarea (correcto)
   - Pero la lectura/escritura puede no funcionar correctamente debido al problema #1

**Archivos principales:**
- `modules/custom-properties-provider.ts` (líneas 107-381: getValue/setValue)
- `modules/flowable-moddle-extension.json` (definiciones de tipos)

### 4. Renderizado Personalizado de Iconos

**Estado:** ✅ Implementado

**Tipos de tareas con iconos personalizados:**

1. **Mail Task** (flowable:type="mail")
   - Icono: Sobre/envelope
   - Renderizado en canvas: ✅ Funcional
   - Renderizado en panel de propiedades: ✅ Funcional (via CSS data attribute)

2. **AI Agent** (flowable:type="ai-agent")
   - Icono: Robot con antena
   - Renderizado en canvas: ✅ Funcional
   - Renderizado en panel de propiedades: ✅ Funcional (via CSS data attribute)

3. **DB Operation** (flowable:type="db-operation")
   - Icono: Cilindro de base de datos
   - Renderizado en canvas: ✅ Funcional
   - Renderizado en panel de propiedades: ✅ Funcional (via CSS data attribute)

4. **HTTP Service** (flowable:type="http")
   - Icono: Globo con flecha
   - Renderizado en canvas: ✅ Funcional
   - Renderizado en panel de propiedades: ✅ Funcional (via CSS data attribute)

**Implementación:**
- Los iconos se dibujan usando SVG (tiny-svg) directamente en el canvas
- Los iconos en el panel de propiedades se muestran mediante CSS usando `data-codeflowx-selected` attribute
- El renderer extiende `BaseRenderer` de diagram-js con alta prioridad (1500)

**Archivos principales:**
- `modules/mail-task-renderer.ts` (renderizado de iconos en canvas)
- `bpmn-styles.css` (líneas 386-452: iconos en panel de propiedades)
- `page.tsx` (líneas 253-269: detección de tipo para data attribute)

### 5. Exportación

**Estado:** ✅ Funcional con mejoras recientes

#### 5.1. Exportación a XML
- ✅ Exporta el diagrama completo en formato BPMN 2.0
- ✅ Formato con indentación
- ✅ Incluye todas las propiedades y extensionElements

#### 5.2. Exportación a PNG
- ✅ Exporta el diagrama como imagen PNG
- ✅ Fondo blanco con padding (20px)
- ✅ Conversión desde SVG
- ✅ Resolución completa del canvas

**Mejoras implementadas:**
- ✅ Fondo blanco explícito en el SVG antes de convertir
- ✅ Padding alrededor del diagrama
- ✅ Conversión correcta SVG → Canvas → PNG

**Archivos principales:**
- `page.tsx` (líneas 532-610: handleExportPNG)

### 6. Auto-layout

**Estado:** ✅ Funcional

- Detecta procesos sin información de diagrama (BPMNShape/BPMNEdge)
- Genera layout jerárquico automático usando BFS desde start events
- Calcula posiciones basándose en sequence flows
- Crea BPMNShape y BPMNEdge en el XML

**Características:**
- Espaciado horizontal: 200px
- Espaciado vertical entre niveles: 180px
- Offset inicial: (100, 100)
- Dimensiones estándar para cada tipo de elemento
- Soporte para elementos no conectados

**Logs de depuración:**
- Muestra advertencias cuando algunos edges no se pueden crear (missing positions/refs)
- Total de flows procesados vs edges creados

**Archivos principales:**
- `utils/autoLayout.ts`
- `page.tsx` (líneas 408-419: detección y aplicación de auto-layout)

### 7. Guardado de Procesos

**Estado:** ✅ Funcional

- Guardado en backend vía API: `/api/bpmn/processes/local/{processId}`
- Guardado temporal en localStorage (para procesos `temp_*`)
- Notificaciones de éxito/error
- Estados de carga (saving)

**Archivos principales:**
- `page.tsx` (líneas 453-504: handleSave)

### 8. Panel de Propiedades

**Estado:** ✅ Funcional

- Panel colapsable (ancho: 300px expandido, 40px colapsado)
- Integración con @bpmn-io/properties-panel
- Iconos personalizados en el header según el tipo de elemento seleccionado
- Transición suave al expandir/colapsar

**Archivos principales:**
- `page.tsx` (líneas 972-1043: panel de propiedades)
- `bpmn-styles.css` (iconos personalizados en header)

### 9. Migración de Namespace: Activiti → Flowable

**Estado:** ✅ Implementado

- El diseñador escribe en namespace `flowable:*`
- Compatibilidad de lectura con `activiti:*` (legacy) - vía fallback en getValue()
- Extensiones moddle definidas para Flowable
- Scripts de migración mencionados en documentación (no presentes en este módulo)

**Archivos principales:**
- `modules/flowable-moddle-extension.json`
- `modules/custom-properties-provider.ts` (fallbacks a activiti:* en getValue)

---

## 🔴 PROBLEMAS CONOCIDOS

### 1. CRÍTICO: Carga de Valores de Propiedades desde XML

**Severidad:** 🔴 Alta

**Descripción:**
Los valores de las propiedades personalizadas no se cargan correctamente desde el XML, especialmente para:

- Mail Tasks que almacenan valores en `extensionElements/flowable:field`
- Propiedades que usan `flowable:string` o `flowable:expression` dentro de fields

**Síntomas:**
- Los campos aparecen vacíos aunque existan valores en el XML
- Los logs muestran `getValue - flowable:assignee = ` (vacío)
- El método `getValue()` intenta múltiples estrategias pero falla

**Código afectado:**
- `modules/custom-properties-provider.ts` (líneas 193-357: método getValue)

**Estrategias actuales de lectura:**
1. Para Mail Tasks: `getMailFieldValue()` (líneas 107-150)
2. Múltiples intentos de acceso:
   - `bo.get(prop.modelProperty)` con nombre completo
   - `bo.get(propName)` sin prefijo
   - Acceso directo `bo[propName]`
   - Acceso via `$attrs[prop.modelProperty]`
   - Acceso via `extensionElements` y `flowable:Field`

**Posible causa:**
- La estructura de `extensionElements.values` y cómo moddle parsea los `flowable:field` con hijos `flowable:string`/`flowable:expression`
- El valor puede estar en `$body`, `body`, `value`, o como hijos en `$children`

**Acción requerida:**
1. Agregar logs detallados para inspeccionar la estructura real de `extensionElements`
2. Verificar cómo moddle parsea los fields con string/expression
3. Implementar lectura correcta basándose en la estructura real observada

### 2. Escritura de Propiedades para Mail Tasks

**Severidad:** 🟡 Media

**Descripción:**
Aunque `setMailFieldValue()` está implementado, puede no funcionar correctamente si la lectura no funciona, ya que no se puede verificar que los valores se escriban correctamente.

**Código afectado:**
- `modules/custom-properties-provider.ts` (líneas 152-191: método setMailFieldValue)

### 3. Iconos en Canvas - Verificación Necesaria

**Severidad:** 🟢 Baja

**Descripción:**
Los iconos personalizados están implementados y deberían funcionar, pero necesita verificación en procesos reales que contengan estas tareas.

**Código afectado:**
- `modules/mail-task-renderer.ts`

### 4. Auto-layout - Edges Omitidos

**Severidad:** 🟡 Media

**Descripción:**
Algunos edges se marcan como "skipped" en los logs cuando faltan posiciones o referencias. Esto puede indicar problemas con:
- Elementos referenciados que no se detectan en el parsing
- Sequence flows que apuntan a elementos que no existen en el XML

**Código afectado:**
- `utils/autoLayout.ts` (líneas 405-413: logs de advertencia)

---

## 📝 MEJORAS PENDIENTES

### Alta Prioridad

1. **Arreglar carga de valores de propiedades desde XML**
   - Prioridad: 🔴 CRÍTICA
   - Impacto: Sin esto, las propiedades no son funcionales
   - Archivos: `modules/custom-properties-provider.ts`
   - Tareas:
     - Agregar logs detallados para debugging
     - Inspeccionar estructura real de extensionElements en procesos de ejemplo
     - Implementar lectura correcta basada en estructura observada
     - Probar con procesos reales (agent-approval-v1.bpmn)

2. **Verificar y arreglar escritura de propiedades para Mail Tasks**
   - Prioridad: 🔴 Alta (depende de #1)
   - Archivos: `modules/custom-properties-provider.ts`

3. **Mejorar auto-layout para manejar todos los casos**
   - Prioridad: 🟡 Media
   - Archivos: `utils/autoLayout.ts`
   - Tareas:
     - Revisar por qué algunos edges se marcan como "skipped"
     - Manejar mejor elementos no detectados en el parsing
     - Mejorar manejo de gateways complejos

### Media Prioridad

4. **Verificar iconos personalizados en procesos reales**
   - Prioridad: 🟡 Media
   - Archivos: `modules/mail-task-renderer.ts`, `bpmn-styles.css`
   - Tareas:
     - Cargar procesos con mail/ai-agent/db-operation/http tasks
     - Verificar que los iconos se muestren correctamente en canvas y panel

5. **Optimizar rendimiento del diseñador**
   - Prioridad: 🟡 Baja
   - Tareas:
     - Revisar si los MutationObservers se pueden optimizar
     - Verificar si hay memory leaks

6. **Agregar soporte para más tipos de tareas Flowable**
   - Prioridad: 🟡 Baja
   - Si se requieren tipos adicionales no contemplados

### Baja Prioridad

7. **Mejorar manejo de errores**
   - Agregar mejor feedback al usuario cuando hay errores de carga/guardado

8. **Agregar validación de procesos**
   - Validar que los procesos sean válidos antes de guardar

9. **Agregar atajos de teclado**
   - Ctrl+S para guardar, Ctrl+Z para deshacer, etc.

---

## 📁 ARCHIVOS CLAVE

### Archivos Principales

| Archivo | Líneas | Propósito | Estado |
|---------|--------|-----------|--------|
| `page.tsx` | 1048 | Componente principal, inicialización, UI | ✅ Funcional |
| `modules/custom-properties-provider.ts` | 514 | Provider de propiedades personalizadas | ⚠️ Problema en lectura |
| `modules/mail-task-renderer.ts` | 189 | Renderizado de iconos personalizados | ✅ Funcional |
| `modules/flowable-moddle-extension.json` | 208 | Extensiones Flowable para moddle | ✅ Funcional |
| `utils/autoLayout.ts` | 424 | Auto-layout para procesos sin DI | ✅ Funcional |
| `components/CustomPalette.tsx` | 586 | Paleta personalizada de componentes | ✅ Funcional |
| `bpmn-styles.css` | 453 | Estilos personalizados BPMN | ✅ Funcional |

### Archivos de Soporte

| Archivo | Propósito | Estado |
|---------|-----------|--------|
| `modules/custom-properties-provider-module.ts` | Registro del provider | ✅ Funcional |
| `modules/mail-task-renderer-module.ts` | Registro del renderer | ✅ Funcional |
| `modules/custom-palette-provider.ts` | Provider de paleta (no usado) | ⚠️ No usado |

---

## 🔍 NOTAS TÉCNICAS

### Stack Tecnológico

- **Framework:** Next.js 14+ (App Router)
- **React:** 18+ con TypeScript
- **BPMN Library:** bpmn-js (diagram-js, bpmn-moddle)
- **Properties Panel:** @bpmn-io/properties-panel
- **SVG:** tiny-svg
- **Iconos:** Lucide React (para paleta personalizada)

### Arquitectura

- **Modeler:** bpmn-js Modeler con módulos adicionales
- **Extensiones Moddle:** `flowable-moddle-extension.json` define tipos Flowable
- **Renderizado:** Custom renderer con alta prioridad (1500) para iconos personalizados
- **Propiedades:** Custom provider registrado con prioridad media (500)

### Flujo de Datos

1. **Carga de Proceso:**
   - XML → API o localStorage
   - Verificación de DI (BPMNShape/BPMNEdge)
   - Auto-layout si es necesario
   - Importación a modeler

2. **Edición:**
   - Usuario edita en canvas o panel de propiedades
   - Cambios se reflejan en businessObject
   - Modeler mantiene el estado interno

3. **Guardado:**
   - Modeler exporta a XML
   - XML se envía al backend o se guarda en localStorage

### Estructura de ExtensionElements para Mail Tasks

```xml
<bpmn:extensionElements>
  <flowable:field name="to">
    <flowable:expression>${email}</flowable:expression>
  </flowable:field>
  <flowable:field name="from">
    <flowable:string>noreply@example.com</flowable:string>
  </flowable:field>
  <flowable:field name="subject">
    <flowable:expression>Asunto del correo</flowable:expression>
  </flowable:field>
  <flowable:field name="html">
    <flowable:expression><![CDATA[<p>Contenido HTML</p>]]></flowable:expression>
  </flowable:field>
</bpmn:extensionElements>
```

**Estructura en Moddle:**
- `extensionElements.values[]` contiene objetos `flowable:Field`
- Cada `Field` tiene `name` (string) y puede tener:
  - `string` (objeto moddle con `value` o `$body`)
  - `expression` (objeto moddle con `value` o `$body`)
  - `$children[]` (array con los hijos parseados)

---

## ✅ CHECKLIST DE VERIFICACIÓN

Para verificar el estado del módulo:

### Funcionalidades Básicas
- [x] El diseñador se carga correctamente
- [x] Se pueden crear elementos desde la paleta
- [x] Se pueden editar propiedades en el panel
- [x] Se puede guardar el proceso
- [x] Se puede exportar a XML
- [x] Se puede exportar a PNG
- [x] El auto-layout funciona para procesos sin DI

### Propiedades
- [ ] Los valores de propiedades se cargan correctamente desde el XML ⚠️
- [ ] Los valores se guardan correctamente al editar ⚠️
- [x] Los campos largos (HTML, SQL, etc.) usan textarea
- [ ] Los valores por defecto se aplican correctamente

### Renderizado
- [ ] Los iconos personalizados se muestran en el canvas (necesita verificación)
- [x] Los iconos personalizados se muestran en el panel de propiedades
- [x] Las tareas personalizadas se identifican correctamente (flowable:type)

### Integración
- [x] No hay errores en la consola del navegador (básicos)
- [x] Los procesos existentes se pueden cargar sin errores
- [x] La paleta personalizada funciona correctamente
- [x] El panel de propiedades se expande/colapsa correctamente

---

## 🚀 PRÓXIMOS PASOS RECOMENDADOS

1. **Paso 1: Arreglar carga de propiedades (CRÍTICO)**
   - Agregar logs detallados en `getValue()` para mail tasks
   - Cargar proceso `agent-approval-v1.bpmn` y verificar estructura
   - Implementar lectura correcta basada en estructura observada
   - Probar que los valores se cargan y se muestran

2. **Paso 2: Verificar escritura de propiedades**
   - Una vez que la lectura funcione, verificar que la escritura también funciona
   - Probar edición y guardado de propiedades de mail tasks

3. **Paso 3: Verificar iconos en procesos reales**
   - Cargar procesos con tareas personalizadas
   - Verificar que los iconos se muestren en canvas y panel

4. **Paso 4: Optimización y limpieza**
   - Eliminar código no usado (custom-palette-provider.ts si no se usa)
   - Optimizar logs de depuración
   - Documentar casos de uso específicos

---

## 📚 REFERENCIAS

### Documentación del Proyecto
- `codeflowx-studio/docs/ARQUITECTURA_FRONTEND.md` - Arquitectura frontend
- `codeflowx-studio/docs/prompts/governance/data/GUIA_PROCESOS_BPMN.md` - Guías BPMN
- `codeflowx-studio/docs/prompts/governance/models/GUIA_TIPOS_TAREAS_FLOWABLE.md` - Tipos de tareas Flowable

### Documentación Externa
- [bpmn-js](https://github.com/bpmn-io/bpmn-js) - Librería principal
- [Flowable](https://www.flowable.com/) - Motor de workflow
- [Moddle](https://github.com/bpmn-io/moddle) - Para extensiones
- [@bpmn-io/properties-panel](https://github.com/bpmn-io/properties-panel) - Panel de propiedades

### Procesos de Ejemplo
- `codeflowx-studio/data/bpmn-processes/aios/agent-approval-v1.bpmn` - Proceso con mail tasks

---

*Este documento refleja el estado actual del código fuente revisado en Diciembre 2025. Para actualizaciones, revisar los archivos fuente mencionados.*
