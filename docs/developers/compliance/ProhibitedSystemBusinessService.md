# ProhibitedSystemBusinessService

**Ubicación:** `com.codeflowx.govern.business.compliance.ProhibitedSystemBusinessService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

El `ProhibitedSystemBusinessService` gestiona la validación de sistemas de IA contra la lista de sistemas prohibidos según el **Art. 5 y Anexo II del EU AI Act**. Los sistemas prohibidos NO pueden ser clasificados ni desplegados.

### Propósito

Este servicio permite:
- Verificar si un proyecto coincide con algún sistema prohibido
- Obtener lista de sistemas prohibidos activos (Anexo II)
- Inicializar sistemas prohibidos por defecto si no existen en BD
- Validar automáticamente proyectos contra sistemas prohibidos

---

## 🎯 Responsabilidades

### ✅ Qué Hace Este Servicio

1. **Validación de Sistemas Prohibidos**
   - Verifica automáticamente proyectos contra lista de sistemas prohibidos
   - Compara nombre, descripción y metadata contra keywords
   - Detecta sistemas prohibidos según Art. 5.1.a, 5.1.b, 5.1.c, 5.1.d

2. **Gestión de Catálogo de Sistemas Prohibidos**
   - Obtiene sistemas prohibidos activos desde BD
   - Inicializa lista por defecto del Anexo II si no existen
   - Mantiene keywords para búsqueda automática

3. **Integración con Validaciones**
   - Usado por `ModelValidationService` para validar antes de clasificar
   - Integrado en flujo de clasificación de alto riesgo

### ❌ Qué NO Hace Este Servicio

- **NO** gestiona CRUD de sistemas prohibidos (eso lo hace `ProhibitedSystemService`)
- **NO** modifica la lista de sistemas prohibidos (solo lectura para validación)
- **NO** bloquea directamente la clasificación (solo detecta y reporta)

---

## 🔗 Dependencias

### Entidades

```java
import com.codeflowx.govern.entity.compliance.ProhibitedSystem;
import com.codeflowx.govern.entity.projects.Project;
```

### Servicios

```java
@Autowired
private ProhibitedSystemService prohibitedSystemService; // Service CRUD
```

### Librerías

- `org.springframework.stereotype.Service`
- `com.fasterxml.jackson.databind.ObjectMapper`
- `lombok.extern.slf4j.Slf4j`

---

## 🏗️ Arquitectura

### Integración en el Sistema

```
┌─────────────────────────────────┐
│  HighRiskClassifierViewModel    │
│  - Clasificación de proyectos   │
└──────────────┬──────────────────┘
               │ @WireVariable
               ▼
┌─────────────────────────────────┐
│  ModelValidationService          │
│  - validateProhibitedSystems()   │
└──────────────┬──────────────────┘
               │ Usa
               ▼
┌─────────────────────────────────┐
│  ProhibitedSystemBusinessService │
│  - checkProhibitedSystem()       │
└──────────────┬──────────────────┘
               │ Usa
               ▼
┌─────────────────────────────────┐
│  ProhibitedSystemService         │
│  - findActive() (Service CRUD)   │
└──────────────┬──────────────────┘
               │ BusinessService (DAO)
               ▼
┌─────────────────────────────────┐
│   GOVPROHIBITEDSYSTEMS           │
│   (Tabla PostgreSQL)            │
└─────────────────────────────────┘
```

---

## 📚 API Pública

### Métodos Principales

#### 1. `checkProhibitedSystem(Project project)`

Verifica si un proyecto coincide con algún sistema prohibido.

**Parámetros:**
- `project`: Proyecto a verificar (no puede ser null)

**Retorna:** `ProhibitedSystemCheckResult` con:
- `checked`: true si se realizó la verificación
- `isProhibited`: true si se detectó sistema prohibido
- `prohibitedSystem`: Sistema prohibido detectado (si aplica)
- `matchReason`: Razón de la coincidencia

**Uso:**
```java
@WireVariable
private ProhibitedSystemBusinessService prohibitedSystemBusinessService;

ProhibitedSystemCheckResult result =
    prohibitedSystemBusinessService.checkProhibitedSystem(project);

if (result.isProhibited()) {
    // Bloquear clasificación
    throw new ValidationException("Sistema prohibido detectado");
}
```

#### 2. `getActiveProhibitedSystems()`

Obtiene lista de sistemas prohibidos activos (Anexo II).

**Retorna:** `List<ProhibitedSystem>` - Lista de sistemas prohibidos activos

**Uso:**
```java
List<ProhibitedSystem> systems =
    prohibitedSystemBusinessService.getActiveProhibitedSystems();
```

---

## 🔍 Detalles de Implementación

### Sistemas Prohibidos del Anexo II

El servicio inicializa automáticamente los siguientes sistemas prohibidos:

1. **Art. 5.1.a** - Manipulación subliminal
   - Keywords: ["subliminal", "manipulation", "subconscious"]

2. **Art. 5.1.b** - Explotación de vulnerabilidades
   - Keywords: ["exploit", "vulnerability", "vulnerable groups"]

3. **Art. 5.1.c** - Scoring social por autoridades públicas
   - Keywords: ["social scoring", "social credit", "public authority", "evaluation"]

4. **Art. 5.1.d** - Identificación biométrica remota en tiempo real
   - Keywords: ["biometric", "remote", "real-time", "public space"]

### Algoritmo de Detección

1. Obtiene sistemas prohibidos activos desde BD
2. Si no hay en BD, inicializa lista por defecto
3. Para cada sistema prohibido:
   - Parsea keywords desde JSON
   - Compara keywords contra nombre, descripción y metadata del proyecto
   - Realiza verificaciones específicas por artículo
4. Retorna resultado con sistema prohibido detectado (si aplica)

---

## 📖 Referencias

- **Art. 5 EU AI Act:** Sistemas Prohibidos
- **Anexo II:** Lista de Sistemas Prohibidos
- **Prompt:** INC-005
- **Service CRUD:** `ProhibitedSystemService`
- **Entidad:** `ProhibitedSystem`

---

**Última actualización:** 25 de noviembre de 2025
