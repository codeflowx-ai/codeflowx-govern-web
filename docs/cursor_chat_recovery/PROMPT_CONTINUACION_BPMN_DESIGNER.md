# 🔄 PROMPT DE CONTINUACIÓN - DISEÑADOR BPMN

**Fecha de creación:** Diciembre 2025
**Basado en:** Conversación ID `633e68f7-e9f1-48a9-83fa-7d14e8dd9f57`
**Última actualización:** 2025-12-19 21:31:21
**Estado:** Trabajo en progreso

---

## 📋 CONTEXTO DEL TRABAJO

Este documento resume el estado actual del trabajo realizado en el **Diseñador BPMN** basado en bpmn-js y Flowable. El trabajo se enfoca en:

1. Implementación de propiedades personalizadas para elementos BPMN
2. Migración de namespace `activiti:*` a `flowable:*`
3. Soporte para tipos de tareas personalizadas (Mail, Script, IA Agent, BD, HTTP)
4. Renderizado personalizado de iconos y propiedades
5. Exportación de diagramas a PNG
6. Auto-layout para procesos sin información de diagrama

---

## 🎯 OBJETIVOS PRINCIPALES

### 1. Propiedades Personalizadas de Elementos BPMN

**Componente principal:** `custom-properties-provider.ts`

**Tareas completadas:**
- ✅ Implementación básica de CustomPropertiesProvider
- ✅ Soporte para `bpmn:UserTask` con propiedades: assignee, candidateUsers, candidateGroups, formKey, dueDate, priority
- ✅ Renderizado de propiedades usando componentes bpmn-js (TextfieldEntry, TextareaEntry)

**Tareas pendientes:**
- ⚠️ **CRÍTICO:** Los valores de las propiedades no se están recuperando correctamente del XML
- ⚠️ Los valores se muestran vacíos aunque existan en el XML (ver logs: `getValue - flowable:assignee = `)
- ⚠️ Necesita revisar cómo se accede a `extensionElements` y `activiti:field` / `flowable:field`

### 2. Migración de Namespace: Activiti → Flowable

**Estado:** En progreso

**Tareas completadas:**
- ✅ Scripts para reemplazar `activiti:*` por `flowable:*` en procesos BPMN
- ✅ Conversión de procesos existentes al nuevo namespace

**Nota importante:**
- El motor de workflow es **Flowable** (fork propio del proyecto)
- El diseñador debe escribir en `flowable:*`
- Compatibilidad de lectura con `activiti:*` solo para legacy

### 3. Tipos de Tareas Personalizadas

#### 3.1. Tareas de Mail (ServiceTask con flowable:type="mail")

**Estado:** Parcialmente implementado

**Archivos clave:**
- `flowable-moddle-extension.json` - Definición de extensiones Flowable
- `mail-task-renderer.ts` - Renderer personalizado para tareas de mail
- `custom-properties-provider.ts` - Propiedades personalizadas

**Propiedades requeridas:**
- `to` - Destinatario (expression o string)
- `from` - Remitente (string)
- `subject` - Asunto (expression o string)
- `html` - Cuerpo HTML (debe ser textarea, no textfield)

**Problemas identificados:**
- ❌ El icono del sobre no se muestra correctamente (se muestra como servicio genérico)
- ❌ Los valores de las propiedades no se cargan desde el XML
- ❌ El campo `html` se muestra como textfield en lugar de textarea
- ❌ El icono en el panel de propiedades muestra una rueda en lugar del sobre

**Archivos de ejemplo:**
- `codeflowx-studio/data/bpmn-processes/aios/agent-approval-v1.bpmn` - Contiene ejemplo de tarea de mail con extensionElements

#### 3.2. Tareas de Script (ScriptTask)

**Estado:** Implementado básicamente

**Problemas identificados:**
- ⚠️ El valor del script (`<script><![CDATA[...]]></script>`) no se muestra en propiedades

#### 3.3. Tareas de IA Agent

**Estado:** Pendiente

**Requerimiento:** Similar a tareas de mail, necesita renderizado personalizado e icono específico

#### 3.4. Operaciones de BD

**Estado:** Pendiente

**Requerimiento:** Renderizado personalizado e icono específico

#### 3.5. Servicios HTTP

**Estado:** Pendiente

**Requerimiento:** Renderizado personalizado e icono específico

### 4. Exportación a PNG

**Estado:** Implementado

**Problema identificado:**
- ❌ La imagen exportada tiene fondo negro y no se ven las líneas
- ⚠️ Necesita ajustar el estilo del canvas antes de exportar

### 5. Auto-layout

**Estado:** Implementado parcialmente

**Archivo:** `autoLayout.ts`

**Funcionalidad:**
- Detecta cuando un proceso no tiene shapes/edges de diagrama
- Genera automáticamente el layout usando información de sequence flows
- Crea los edges faltantes

**Problemas identificados:**
- ⚠️ Algunos edges se marcan como "skipped" en el log
- ⚠️ Pueden faltar conexiones entre tareas en algunos casos

---

## 📁 ARCHIVOS CLAVE DEL PROYECTO

### Archivos Principales

```
app/(app)/bpmn-designer/
├── page.tsx                           # Componente principal del diseñador
├── custom-properties-provider.ts      # Provider de propiedades personalizadas
├── mail-task-renderer.ts              # Renderer para tareas de mail
├── mail-task-renderer-module.ts       # Módulo del renderer
├── properties-panel-icon-config-module.ts  # Configuración de iconos en panel
├── flowable-moddle-extension.json     # Extensiones Flowable para moddle
├── autoLayout.ts                      # Auto-layout para procesos sin DI
├── bpmn-styles.css                    # Estilos personalizados BPMN
├── add-di-to-bpmn.js                  # Script para agregar información de diagrama
└── add-di-to-bpmn-missing.js          # Script para procesos con DI faltante
```

### Archivos de Datos

```
data/bpmn-processes/
├── aios/
│   ├── agent-approval-v1.bpmn         # Proceso de ejemplo con tareas de mail
│   └── adapter-creation-approval-v1.bpmn20.xml  # Otro proceso de ejemplo
```

---

## 🔍 PROBLEMAS CRÍTICOS A RESOLVER

### 1. **CRÍTICO: Valores de propiedades no se cargan**

**Síntoma:** Los campos de propiedades aparecen vacíos aunque existan valores en el XML.

**Logs relevantes:**
```
[EntryComponent] getValue - flowable:assignee =
[EntryComponent] getValue - flowable:candidateUsers =
[EntryComponent] getValue - flowable:formKey =
[EntryComponent] Value is empty or null
```

**Posible causa:**
- El método `getValue` no está accediendo correctamente a los `extensionElements`
- Necesita revisar cómo se accede a `activiti:field` / `flowable:field` dentro de `extensionElements`
- Verificar si se está usando `businessObject.get()` correctamente

**Acción requerida:**
1. Revisar `custom-properties-provider.ts` método `getValue`
2. Inspeccionar la estructura de `extensionElements` en el proceso de ejemplo
3. Implementar lectura correcta de `flowable:field` con sus expresiones/strings

### 2. **Icono de mail task no se muestra**

**Síntoma:** Las tareas de tipo mail se muestran con el icono genérico de servicio (rueda).

**Archivos involucrados:**
- `mail-task-renderer.ts`
- `bpmn-styles.css`
- `properties-panel-icon-config-module.ts`

**Acción requerida:**
1. Verificar que el renderer está correctamente registrado
2. Verificar los estilos CSS para el icono
3. Verificar que el icono en el panel de propiedades use el renderer correcto

### 3. **Campo HTML muestra como textfield**

**Síntoma:** El campo `html` de las tareas de mail se muestra como textfield en lugar de textarea.

**Acción requerida:**
1. Verificar en `custom-properties-provider.ts` que se usa `TextareaEntry` para el campo `html`
2. Verificar la configuración en `flowable-moddle-extension.json`

### 4. **Error: property <body> already defined**

**Error:**
```
Error: property <body> already defined; override of <flowable:string#flowable:body> by <flowable:expression#flowable:body> not allowed without redefines
```

**Acción requerida:**
1. Revisar `flowable-moddle-extension.json`
2. Asegurar que no haya definiciones duplicadas de `body`
3. Usar `redefines` si es necesario

---

## 📝 TAREAS PENDIENTES PRIORITARIAS

### Alta Prioridad

1. **Arreglar carga de valores de propiedades desde XML**
   - Prioridad: CRÍTICA
   - Impacto: Sin esto, las propiedades no son funcionales
   - Archivos: `custom-properties-provider.ts`

2. **Arreglar icono de mail task**
   - Prioridad: Alta
   - Impacto: UX - los usuarios no pueden identificar visualmente tareas de mail
   - Archivos: `mail-task-renderer.ts`, `bpmn-styles.css`

3. **Cambiar campo HTML a textarea**
   - Prioridad: Alta
   - Impacto: UX - campo HTML es largo y necesita textarea
   - Archivos: `custom-properties-provider.ts`

### Media Prioridad

4. **Implementar renderer para IA Agent tasks**
   - Similar a mail task
   - Archivos: Crear `agent-task-renderer.ts` y módulo correspondiente

5. **Implementar renderer para BD operations**
   - Similar a mail task
   - Archivos: Crear `database-task-renderer.ts` y módulo correspondiente

6. **Implementar renderer para HTTP services**
   - Similar a mail task
   - Archivos: Crear `http-task-renderer.ts` y módulo correspondiente

7. **Arreglar exportación PNG (fondo negro)**
   - Archivos: `page.tsx` (función de exportación)

8. **Mejorar auto-layout**
   - Revisar por qué algunos edges se marcan como "skipped"
   - Archivos: `autoLayout.ts`

### Baja Prioridad

9. **Eliminar enlace a bpmn.io del canvas** (si aún existe)
10. **Mejorar manejo de procesos sin información de diagrama**

---

## 🔗 REFERENCIAS Y DOCUMENTACIÓN

### Documentación del Proyecto

- **Arquitectura Frontend:** `codeflowx-studio/docs/ARQUITECTURA_FRONTEND.md`
- **Guías BPMN:** `codeflowx-studio/docs/prompts/governance/data/GUIA_PROCESOS_BPMN.md`
- **Tipos de Tareas Flowable:** `codeflowx-studio/docs/prompts/governance/models/GUIA_TIPOS_TAREAS_FLOWABLE.md`

### Documentación Externa

- **bpmn-js:** https://github.com/bpmn-io/bpmn-js
- **Flowable:** https://www.flowable.com/
- **Moddle:** https://github.com/bpmn-io/moddle (para extensiones)

### Procesos de Ejemplo

- `codeflowx-studio/data/bpmn-processes/aios/agent-approval-v1.bpmn`
  - Contiene tareas de mail con extensionElements
  - Ejemplo de uso de `flowable:field` con expresiones

---

## 💡 NOTAS PARA EL AGENTE

### Contexto Técnico

1. **Motor de Workflow:** Flowable (fork propio)
   - Usar namespace `flowable:*` para escritura
   - Lectura compatible con `activiti:*` solo para legacy

2. **Arquitectura:**
   - Next.js 14+ con App Router
   - React 18+ con TypeScript
   - bpmn-js como librería base del diseñador

3. **Estructura de Propiedades:**
   - Las propiedades personalizadas se definen en `flowable-moddle-extension.json`
   - Se acceden a través de `extensionElements` en el XML BPMN
   - Se usan `flowable:field` dentro de `extensionElements`

### Estilo de Código

- Seguir las reglas SOLID y arquitectura hexagonal
- Mantener código simple (KISS)
- Usar TypeScript estricto
- Documentar funciones complejas

### Proceso de Desarrollo

1. **Antes de empezar:**
   - Revisar los logs de consola compartidos en la conversación original
   - Cargar el proceso `agent-approval-v1` para probar
   - Inspeccionar la estructura del XML con las propiedades

2. **Al trabajar en propiedades:**
   - Agregar logs detallados para debugging
   - Verificar la estructura de `businessObject` y `extensionElements`
   - Probar con diferentes tipos de valores (strings, expressions)

3. **Al trabajar en renderers:**
   - Verificar que el renderer está registrado correctamente
   - Revisar los estilos CSS asociados
   - Probar en el canvas y en el panel de propiedades

---

## ✅ CHECKLIST DE VERIFICACIÓN

Al completar una tarea, verificar:

- [ ] Los valores de propiedades se cargan correctamente desde el XML
- [ ] Los valores se guardan correctamente al editar
- [ ] Los iconos personalizados se muestran correctamente en el canvas
- [ ] Los iconos personalizados se muestran correctamente en el panel de propiedades
- [ ] Los campos largos (HTML) usan textarea
- [ ] La exportación PNG tiene fondo blanco y se ven las líneas
- [ ] El auto-layout genera todas las conexiones necesarias
- [ ] No hay errores en la consola del navegador
- [ ] Los procesos existentes se pueden cargar sin errores

---

## 🚀 PRÓXIMOS PASOS SUGERIDOS

1. **Empezar por el problema crítico:** Arreglar la carga de valores de propiedades
   - Revisar el XML del proceso de ejemplo
   - Inspeccionar cómo bpmn-js parsea los extensionElements
   - Implementar la lectura correcta

2. **Una vez funcionen las propiedades básicas:** Arreglar iconos y renderers
3. **Finalmente:** Implementar los tipos de tareas faltantes

---

*Este documento fue generado a partir de la conversación recuperada. Las respuestas del asistente no están disponibles, pero se pueden inferir los problemas y soluciones necesarias a partir de los prompts del usuario y los logs compartidos.*
