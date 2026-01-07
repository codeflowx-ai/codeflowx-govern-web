# 📱 GUÍA DE USO DE PANTALLAS - IMMUTABLE LOGS

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Usuarios finales

---

## 📋 ÍNDICE

1. [Pantalla de Búsqueda de Logs](#pantalla-de-búsqueda-de-logs)
2. [Pantalla de Detalle de Log](#pantalla-de-detalle-de-log)
3. [Funcionalidades Comunes](#funcionalidades-comunes)

---

## 🔍 PANTALLA DE BÚSQUEDA DE LOGS

### Acceso

**Ruta:** `/governance/compliance/immutable-logs`

### Descripción General

Pantalla principal para buscar y gestionar logs inmutables. Permite búsqueda avanzada, verificación de integridad y exportación.

---

### Sección 1: Header y Estadísticas

#### Header
- **Título:** "Logs Inmutables"
- **Subtítulo:** "Búsqueda y verificación de logs inmutables según Art. 19 de la ley europea sobre la IA"

#### Tarjetas de Estadísticas

**Total de Logs:**
- Muestra el número total de logs encontrados en la búsqueda actual

**Página Actual:**
- Muestra la página actual y el total de páginas (ej: "1 / 5")

**Estado de Integridad:**
- Muestra el score de integridad si se ha verificado
- Icono verde: INTEGRITY_OK
- Icono amarillo: INTEGRITY_PARTIAL
- Icono rojo: INTEGRITY_BROKEN

**Logs Verificados:**
- Muestra el número de logs verificados en la última verificación

---

### Sección 2: Búsqueda Avanzada

#### Búsqueda de Texto Libre

**Campo de búsqueda:**
- Busca en todos los campos del log
- Búsqueda en tiempo real mientras escribes
- Busca en: tipo de log, entidad, usuario, datos del log

#### Botón "Filtros"

**Acción:** Expande/contrae panel de filtros avanzados

**Filtros Disponibles:**

1. **Tipo de Log:**
   - Dropdown con tipos disponibles
   - Ejemplos: MODEL_DEPLOYMENT, AGENT_EXECUTION, PROMPT_CHANGE
   - Opción "Todos" para no filtrar

2. **Tipo de Entidad:**
   - Dropdown con tipos de entidad
   - Ejemplos: Model, Agent, Prompt, Project
   - Opción "Todos" para no filtrar

3. **ID de Entidad:**
   - Campo numérico
   - Busca logs de una entidad específica

4. **Usuario:**
   - Campo de texto
   - Busca por ID o email de usuario

5. **Fecha Inicio:**
   - Selector de fecha
   - Filtra logs desde esta fecha

6. **Fecha Fin:**
   - Selector de fecha
   - Filtra logs hasta esta fecha

7. **Hash:**
   - Campo de texto
   - Busca por hash actual o anterior

#### Ordenamiento

**Ordenar por:**
- Dropdown: Fecha, Tipo de Log, Tipo de Entidad

**Orden:**
- Dropdown: Ascendente, Descendente

#### Botón "Limpiar"

**Acción:** Limpia todos los filtros y restablece búsqueda

---

### Sección 3: Resultado de Verificación de Integridad

**Visible solo cuando se ha realizado una verificación**

#### Tarjeta de Resultado

**Título:** "Verificación de Integridad"
- Icono según estado (✓ verde, ⚠ amarillo, ✗ rojo)

**Métricas:**
- **Score de Integridad:** Porcentaje (ej: "95.50%")
- **Logs Verificados:** "X / Y" (ej: "95 / 100")
- **Estado:** INTEGRITY_OK, INTEGRITY_PARTIAL, INTEGRITY_BROKEN
- **Rango:** "X - Y" (ej: "1 - 100")

**Cadenas Rotas (si aplica):**
- Lista de logs corruptos
- Muestra: Log ID, Hash esperado, Hash actual

---

### Sección 4: Acciones

#### Botón "Verificar Integridad"

**Ubicación:** Lado izquierdo de la barra de acciones

**Acción:**
- Verifica la integridad de todos los logs en los resultados de búsqueda
- Muestra resultado en tarjeta de verificación
- Dispara workflow BPMN si se detecta corrupción

**Estado:**
- Habilitado cuando hay resultados
- Deshabilitado cuando no hay resultados o está verificando

#### Botones de Exportación

**Ubicación:** Lado derecho de la barra de acciones

**Botones:**
1. **Exportar CSV:**
   - Exporta todos los resultados en formato CSV
   - Incluye: ID, Tipo, Entidad, Usuario, Fecha, Hash, Hash Anterior

2. **Exportar JSON:**
   - Exporta todos los resultados en formato JSON
   - Incluye todos los datos completos

3. **Exportar Hash Chain:**
   - Exporta solo la cadena de hash
   - Incluye: ID, Hash, Hash Anterior, Timestamp
   - Incluye metadatos: fecha de exportación, criterios de búsqueda

---

### Sección 5: Tabla de Resultados

#### Columnas

1. **ID:**
   - ID numérico del log

2. **Tipo:**
   - Badge con tipo de log
   - Color según tipo

3. **Entidad:**
   - Tipo de entidad (ej: "Model")
   - ID de entidad (ej: "#123")
   - Nombre de entidad si está disponible

4. **Usuario:**
   - ID o email del usuario

5. **Fecha:**
   - Fecha y hora formateada
   - Formato local según configuración

6. **Hash:**
   - Hash truncado (primeros 16 caracteres + "...")
   - Tooltip con hash completo al pasar el mouse

7. **Acciones:**
   - Botón "Ver" para ir al detalle

#### Paginación

**Ubicación:** Parte inferior de la tabla

**Elementos:**
- Texto: "Mostrando X - Y de Z"
- Botón "Anterior" (deshabilitado en primera página)
- Texto: "Página X / Y"
- Botón "Siguiente" (deshabilitado en última página)

---

## 📄 PANTALLA DE DETALLE DE LOG

### Acceso

**Ruta:** `/governance/compliance/immutable-logs/[id]`

**Navegación:**
- Desde pantalla de búsqueda: Botón "Ver" en la tabla
- Desde navegación en cadena: Botones "Log Anterior" / "Log Siguiente"

---

### Sección 1: Header

#### Botón "Volver"

**Ubicación:** Lado izquierdo

**Acción:** Regresa a la pantalla de búsqueda

#### Título

**Texto:** "Detalle de Log Inmutable"
- Icono de candado (🔒)

**Subtítulo:**
- Muestra ID del log (ej: "ID: 123")

#### Botón "Exportar"

**Ubicación:** Lado derecho

**Acción:** Exporta el log completo en formato JSON

---

### Sección 2: Navegación en la Cadena

#### Tarjeta de Navegación

**Botón "Log Anterior":**
- Navega al log anterior en la cadena
- Deshabilitado si es el primer log

**Información Central:**
- Texto: "Posición en la cadena"
- Número: "X / Y" (ej: "5 / 10")

**Botón "Log Siguiente":**
- Navega al log siguiente en la cadena
- Deshabilitado si es el último log

---

### Sección 3: Información General

#### Tarjeta "Información General"

**Campos:**
- **Tipo de Log:** Badge con tipo
- **Tipo de Entidad:** Texto
- **ID de Entidad:** "#123"
- **Usuario:** ID o email
- **Fecha y Hora:** Formato local + ISO 8601
- **Integridad:**
  - Icono ✓ verde: "Verificado"
  - Icono ⚠ rojo: "No Verificado"

---

### Sección 4: Hash Chain

#### Tarjeta "Hash Chain"

**Hash Actual (SHA-256):**
- Campo de texto con hash completo
- Botón de copiar al portapapeles
- Icono cambia a ✓ cuando se copia

**Hash Anterior:**
- Campo de texto con hash anterior
- Muestra "Genesis (Primer log)" si es "0"
- Botón de copiar si no es genesis

**Información de Cadena:**
- **Longitud:** Número total de logs en la cadena
- **Posición:** Posición actual en la cadena

---

### Sección 5: Datos del Log y Visualización de Cadena

#### Layout: 2 Columnas (50% cada una)

**Columna Izquierda: Datos del Log**

**Tarjeta "Datos del Log":**
- JSON formateado con todos los datos
- Scroll si el contenido es largo
- Fondo gris para mejor legibilidad

**Columna Derecha: Visualización de la Cadena**

**Tarjeta "Visualización de la Cadena":**
- Lista de todos los logs en la cadena
- Scroll si hay muchos logs
- Cada log muestra:
  - Número de posición (círculo)
  - Tipo de log
  - Entidad y ID
  - Fecha (en pantallas grandes)
  - Hash truncado (en pantallas grandes)
  - Badge "Actual" si es el log actual
  - Botón "Ver" para navegar a otros logs
- Líneas verticales conectando los logs
- Log actual resaltado con borde púrpura

---

## 🔧 FUNCIONALIDADES COMUNES

### Copiar al Portapapeles

**Dónde:**
- Hash actual en pantalla de detalle
- Hash anterior en pantalla de detalle

**Cómo:**
1. Hacer clic en el botón de copiar (icono de copiar)
2. El icono cambia a ✓ verde
3. El hash se copia al portapapeles
4. Después de 2 segundos, el icono vuelve a normal

### Exportación

**Formatos Disponibles:**
- **CSV:** Para análisis en Excel o herramientas similares
- **JSON:** Para procesamiento programático
- **Hash Chain:** Para verificación externa de integridad

**Proceso:**
1. Hacer clic en el botón de exportación deseado
2. El archivo se descarga automáticamente
3. Nombre del archivo incluye timestamp

### Verificación de Integridad

**Proceso:**
1. Realizar búsqueda de logs
2. Hacer clic en "Verificar Integridad"
3. Esperar a que se complete la verificación
4. Revisar resultado en tarjeta de verificación

**Resultados Posibles:**
- **INTEGRITY_OK:** Todos los logs están íntegros (score = 1.00)
- **INTEGRITY_PARTIAL:** Algunos logs tienen problemas (score < 1.00 pero >= 0.90)
- **INTEGRITY_BROKEN:** Hay corrupción significativa (score < 0.90)

---

## 💡 CONSEJOS Y MEJORES PRÁCTICAS

### Búsqueda Eficiente

1. **Usa filtros específicos:**
   - Filtra por tipo de log si buscas eventos específicos
   - Filtra por entidad si investigas un modelo/agente específico
   - Usa rango de fechas para limitar resultados

2. **Combina filtros:**
   - Múltiples filtros se combinan con AND
   - Usa búsqueda de texto para búsquedas amplias

3. **Ordena resultados:**
   - Ordena por fecha descendente para ver los más recientes primero
   - Ordena por tipo para agrupar eventos similares

### Verificación de Integridad

1. **Verifica regularmente:**
   - Verifica integridad después de eventos importantes
   - Verifica antes de auditorías

2. **Revisa cadenas rotas:**
   - Si se detecta corrupción, revisa los logs corruptos
   - Exporta evidencia para investigación

### Exportación

1. **Elige el formato correcto:**
   - CSV para análisis en Excel
   - JSON para procesamiento programático
   - Hash Chain para verificación externa

2. **Exporta antes de auditorías:**
   - Exporta hash chain completa
   - Verifica integridad antes de exportar

---

## ❓ PREGUNTAS FRECUENTES

### ¿Qué es un hash chain?

Un hash chain es una secuencia de hashes donde cada hash incluye el hash anterior. Esto garantiza que cualquier alteración en un log rompe toda la cadena, permitiendo detectar modificaciones.

### ¿Puedo modificar o eliminar logs?

No. Los logs son APPEND-ONLY, lo que significa que solo se pueden crear nuevos logs. No se pueden modificar ni eliminar logs existentes.

### ¿Qué significa "Genesis (Primer log)"?

Es el primer log de una entidad. Su hash anterior es "0" porque no hay log anterior.

### ¿Cómo sé si un log ha sido alterado?

Usa la función "Verificar Integridad". Si el hash calculado no coincide con el hash almacenado, el log ha sido alterado.

### ¿Puedo exportar todos los logs?

Sí. La exportación incluye todos los resultados de la búsqueda actual, no solo la página visible.

---

**Última Actualización:** Diciembre 2025
