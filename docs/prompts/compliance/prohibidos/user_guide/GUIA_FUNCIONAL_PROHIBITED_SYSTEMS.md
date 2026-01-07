# 📘 GUÍA FUNCIONAL - PROHIBITED SYSTEMS (SISTEMAS PROHIBIDOS)

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Usuarios finales, Compliance Officers, Project Managers

---

## 🎯 ¿QUÉ ES PROHIBITED SYSTEMS?

Prohibited Systems es un sistema de **detección automática** de sistemas de Inteligencia Artificial que están **prohibidos** según el **EU AI Act Art. 5**. Su objetivo es prevenir el despliegue de sistemas de IA que violan las prohibiciones establecidas en la regulación europea.

### 🎯 Propósito Principal

El sistema de Prohibited Systems permite:

1. **Detección Automática:** Identificación proactiva de sistemas prohibidos en proyectos de IA
2. **Prevención de Despliegue:** Bloqueo automático de despliegues de sistemas prohibidos
3. **Cumplimiento Normativo:** Cumplimiento con EU AI Act Art. 5, Anexo II
4. **Catálogo Centralizado:** Gestión de un catálogo de sistemas prohibidos según categorías del Art. 5
5. **Integración con Clasificación:** Verificación automática durante el proceso de clasificación de proyectos

---

## 🌍 BASE LEGAL Y NORMATIVA

### EU AI Act - Artículo 5

El **Art. 5** del EU AI Act establece que **están prohibidos** los siguientes sistemas de IA:

#### Art. 5.1.a - Manipulación Subliminal
Sistemas de IA que utilizan técnicas subliminales más allá de la conciencia de una persona para distorsionar materialmente su comportamiento de manera que cause o pueda causar daño físico o psicológico.

#### Art. 5.1.b - Explotación de Vulnerabilidades
Sistemas de IA que explotan las vulnerabilidades de un grupo específico de personas debido a su edad, discapacidad física o mental, o situación social o económica, de manera que cause o pueda causar daño físico o psicológico.

#### Art. 5.1.c - Social Scoring
Sistemas de IA que evalúan o clasifican la confiabilidad de las personas naturales durante un cierto período de tiempo basándose en su comportamiento social o características personales conocidas o inferidas, con el resultado de:
- Tratar a las personas de manera desfavorable o desventajosa en contextos sociales que no están relacionados con los contextos en los que se generaron o recopilaron los datos
- Tratar a las personas de manera desfavorable o desventajosa de manera injustificada o desproporcionada en relación con su comportamiento social

#### Art. 5.1.d - Identificación Biométrica Remota
Sistemas de IA destinados a ser utilizados por las autoridades encargadas de hacer cumplir la ley para la identificación biométrica remota de personas físicas en tiempo real en espacios de acceso público, excepto en casos específicos y limitados.

**Excepciones (Art. 5.2):**
- Búsqueda de víctimas de delitos graves
- Prevención de amenazas específicas, sustanciales e inminentes
- Búsqueda de sospechosos de delitos graves

---

## 🚀 ¿PARA QUÉ SIRVE PROHIBITED SYSTEMS?

### 1. **Para TODOS los Proyectos de IA (Obligatorio)**

Según el **EU AI Act**, **TODOS** los sistemas de IA deben ser verificados para asegurar que no están prohibidos según Art. 5. Esto incluye:

- ✅ Sistemas de alto riesgo (Anexo III)
- ✅ Sistemas de riesgo limitado
- ✅ Sistemas de riesgo mínimo
- ✅ Sistemas de IA generales

**En todos los casos, la verificación de sistemas prohibidos es OBLIGATORIA por ley.**

### 2. **Integración con Clasificación**

El sistema está **integrado automáticamente** en el proceso de clasificación de proyectos:

- ✅ Verificación automática al cargar un proyecto para clasificar
- ✅ Verificación automática antes de proceder con la clasificación
- ✅ Bloqueo de clasificación si se detecta un sistema prohibido
- ✅ Alertas visuales para informar al usuario

### 3. **Prevención de Despliegue**

El sistema **bloquea automáticamente** el despliegue de proyectos que usan sistemas prohibidos:

- ✅ Campo `PRJDEPLOYMENTBLOCKED` en la entidad Project
- ✅ Razón del bloqueo (`PRJBLOCKREASON`)
- ✅ Integración con workflows BPMN para revisión

---

## 📋 FUNCIONALIDADES PRINCIPALES

### 1. **Detección Automática**

El sistema detecta automáticamente sistemas prohibidos mediante:

- **Keywords Matching:** Búsqueda de palabras clave en nombre y descripción del proyecto
- **Catálogo de Sistemas Prohibidos:** Comparación con un catálogo centralizado
- **Categorización:** Clasificación según Art. 5.1.a, b, c, d

**Ejemplo:**
Si un proyecto contiene las palabras "social scoring" o "behavioral evaluation", el sistema lo detectará como posible sistema prohibido según Art. 5.1.c.

### 2. **Catálogo de Sistemas Prohibidos**

El catálogo contiene:

- **Sistemas Prohibidos Definidos:** Lista de sistemas conocidos como prohibidos
- **Categorías:** Clasificación según Art. 5.1.a, b, c, d
- **Keywords:** Palabras clave para detección automática
- **Descripciones:** Explicaciones detalladas de por qué están prohibidos

**Nota:** El catálogo es de **solo lectura** para usuarios finales. La gestión del catálogo es responsabilidad de administradores del sistema.

### 3. **Verificación en Clasificación**

Durante el proceso de clasificación de un proyecto:

1. El sistema verifica automáticamente si el proyecto usa sistemas prohibidos
2. Si se detecta un sistema prohibido:
   - Se muestra un **Alert** con la información
   - Se **bloquea** la posibilidad de continuar con la clasificación
   - Se sugiere revisar el proyecto antes de continuar
3. Si no se detecta ningún sistema prohibido:
   - El proceso de clasificación continúa normalmente

### 4. **Bloqueo de Despliegue**

Cuando se detecta un sistema prohibido:

- El proyecto se marca con `PRJDEPLOYMENTBLOCKED = true`
- Se registra la razón del bloqueo en `PRJBLOCKREASON`
- Se inicia un workflow BPMN para revisión (opcional)
- Se notifica a los stakeholders relevantes

### 5. **Gestión de Detecciones**

El sistema permite:

- **Ver Detecciones:** Listado de todos los sistemas detectados
- **Ver Detalle:** Información completa de cada detección
- **Marcar como Falso Positivo:** Si la detección es incorrecta
- **Revisar y Resolver:** Proceso de revisión y resolución

---

## 🔄 FLUJO DE TRABAJO

### Flujo 1: Verificación Automática en Clasificación

```
1. Usuario carga proyecto para clasificar
   ↓
2. Sistema verifica automáticamente sistemas prohibidos
   ↓
3. ¿Se detecta sistema prohibido?
   ├─ SÍ → Mostrar Alert y bloquear clasificación
   └─ NO → Continuar con clasificación normal
```

### Flujo 2: Verificación Manual

```
1. Usuario accede a "Sistemas Prohibidos"
   ↓
2. Sistema muestra listado de detecciones
   ↓
3. Usuario puede:
   ├─ Ver detalle de una detección
   ├─ Ver catálogo de sistemas prohibidos
   └─ Verificar todos los proyectos
```

### Flujo 3: Bloqueo de Despliegue

```
1. Sistema detecta sistema prohibido
   ↓
2. Sistema bloquea despliegue automáticamente
   ↓
3. Workflow BPMN inicia (opcional)
   ↓
4. Compliance Officers revisan
   ↓
5. Decisión:
   ├─ Falso Positivo → Desbloquear
   ├─ Confirmado → Bloquear permanentemente
   └─ Requiere Modificaciones → Solicitar cambios
```

---

## 📊 CATEGORÍAS DE SISTEMAS PROHIBIDOS

### Art. 5.1.a - Manipulación Subliminal

**Ejemplos:**
- Sistemas que utilizan técnicas subliminales para influir en el comportamiento
- Sistemas que distorsionan la percepción sin conocimiento del usuario

**Keywords comunes:**
- "subliminal"
- "manipulación"
- "influencia subconsciente"
- "persuasión oculta"

### Art. 5.1.b - Explotación de Vulnerabilidades

**Ejemplos:**
- Sistemas que se aprovechan de vulnerabilidades de grupos específicos
- Sistemas que explotan edad, discapacidad, o situación económica

**Keywords comunes:**
- "explotación"
- "vulnerabilidad"
- "grupo vulnerable"
- "targeting vulnerable"

### Art. 5.1.c - Social Scoring

**Ejemplos:**
- Sistemas de evaluación de confiabilidad basados en comportamiento social
- Sistemas de clasificación de personas por características personales
- Sistemas de scoring social para propósitos generales

**Keywords comunes:**
- "social scoring"
- "behavioral evaluation"
- "trustworthiness score"
- "social credit"
- "citizen scoring"

### Art. 5.1.d - Identificación Biométrica Remota

**Ejemplos:**
- Sistemas de reconocimiento facial en tiempo real en espacios públicos
- Sistemas de identificación biométrica remota por autoridades

**Keywords comunes:**
- "remote biometric identification"
- "real-time facial recognition"
- "biometric surveillance"
- "remote identification"

---

## ⚠️ IMPORTANTE: FALSOS POSITIVOS

El sistema utiliza **detección por keywords**, lo que puede generar **falsos positivos**. Por ejemplo:

- Un proyecto que menciona "social scoring" en su documentación pero no lo implementa
- Un proyecto que estudia sistemas prohibidos pero no los despliega

**Por eso:**
- ✅ El sistema **bloquea** el despliegue automáticamente
- ✅ Los **Compliance Officers** pueden revisar y marcar como falso positivo
- ✅ El workflow BPMN permite un proceso de revisión estructurado

---

## 🎯 CASOS DE USO

### Caso 1: Proyecto con Social Scoring

**Escenario:**
Un proyecto de IA contiene las palabras "social scoring" y "behavioral evaluation" en su descripción.

**Flujo:**
1. Sistema detecta keywords: "social scoring", "behavioral evaluation"
2. Sistema marca proyecto como posible Art. 5.1.c
3. Sistema bloquea despliegue automáticamente
4. Compliance Officer revisa el proyecto
5. Si es falso positivo: se desbloquea
6. Si es confirmado: se mantiene bloqueado

### Caso 2: Proyecto Limpio

**Escenario:**
Un proyecto de IA no contiene ningún keyword relacionado con sistemas prohibidos.

**Flujo:**
1. Sistema verifica proyecto
2. No se detectan keywords
3. Sistema permite continuar con clasificación y despliegue

### Caso 3: Verificación Manual

**Escenario:**
Un Compliance Officer quiere verificar todos los proyectos existentes.

**Flujo:**
1. Compliance Officer accede a "Sistemas Prohibidos"
2. Hace clic en "Verificar Todos los Proyectos"
3. Sistema verifica todos los proyectos
4. Sistema muestra listado de detecciones

---

## 📚 REFERENCIAS

- **Base Legal:** EU AI Act Art. 5, Anexo II
- **Guía de Pantallas:** Ver `GUIA_USO_PANTALLAS_PROHIBITED_SYSTEMS.md`
- **Documentación Técnica:** Ver `DEVELOPER_GUIDE_BACKEND.md` y `DEVELOPER_GUIDE_FRONTEND.md`
- **Guía BPMN:** Ver `BPMN_WORKFLOW_GUIDE.md`
