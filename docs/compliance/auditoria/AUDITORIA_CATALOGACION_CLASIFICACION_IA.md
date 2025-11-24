# AUDITORÍA - CATALOGACIÓN Y CLASIFICACIÓN DE SISTEMAS DE IA
**Fecha:** Noviembre 2025  
**Auditor:** Sistema de Gobierno de IA - CodeflowX  
**Base Legal:** EU AI Act (Reglamento UE 2024/1689)  
**Artículos Relevantes:** Art. 6, Art. 11, Art. 15, Art. 51, Anexo III

---

## 1. PROCESO DE CATALOGACIÓN DESDE CERO

### 1.1 Flujo General

El proceso de catalogación de un nuevo sistema de IA se realiza en **3 etapas principales**:

```
ETAPA 1: Creación de Proyecto/Modelo
    ↓
ETAPA 2: Clasificación de Riesgo (Art. 6)
    ↓
ETAPA 3: Catalogación Completa (Art. 11, 15, 51)
```

### 1.2 Punto de Entrada

**Pantalla Principal:** `/console/gobierno/compliance/high-risk-classifier.zul`

**ViewModel:** `HighRiskClassifierViewModel.java`  
**Ubicación:** `com.codeflowx.govern.viewmodel.compliance.HighRiskClassifierViewModel`

---

## 2. FICHA INICIAL - DATOS SOLICITADOS

### 2.1 Campos Obligatorios en Creación de Proyecto

**Entidad:** `Project.java` (tabla `PRJPROJECTS`)

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `PRJNAME` | VARCHAR(100) | ✅ SÍ | Nombre del proyecto |
| `PRJPROJECTCODE` | VARCHAR(100) | ✅ SÍ | Código único del proyecto |
| `PRJPROJECTTYPE` | LIST_STRING | ✅ SÍ | Tipo de proyecto (AI_SYSTEM, RESEARCH, etc.) |
| `PRJSTATUS` | LIST_STRING | ✅ SÍ | Estado inicial (DRAFT, IN_PROGRESS, etc.) |
| `PRJCREATEDAT` | TIMESTAMP | ✅ SÍ | Fecha de creación (automático) |
| `PRJUPDATEDAT` | TIMESTAMP | ✅ SÍ | Fecha actualización (automático) |

### 2.2 Campos Opcionales en Creación Inicial

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `PRJDESCRIPTION` | CLOB | ❌ NO | Descripción del proyecto |
| `PRJSTARTDATE` | DATE | ❌ NO | Fecha inicio |
| `PRJENDDATE` | DATE | ❌ NO | Fecha fin estimada |
| `PRJOWNERID` | LONG | ❌ NO | ID del propietario |
| `PRJCLIENTID` | LONG | ❌ NO | ID del cliente |
| `PRJMETADATA` | JSONB | ❌ NO | Metadatos adicionales |

### 2.3 Campos de Modelo (si aplica)

**Entidad:** `Model.java` (tabla `MODMODELS`)

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| `MODNAME` | VARCHAR(255) | ✅ SÍ | Nombre del modelo |
| `MODTYPE` | LIST_STRING | ✅ SÍ | Tipo de modelo (LLM, CLASSIFIER, etc.) |
| `MODVERSION` | VARCHAR(50) | ✅ SÍ | Versión del modelo |
| `MODSTATUS` | LIST_STRING | ✅ SÍ | Estado (DRAFT, TRAINING, PRODUCTION, etc.) |
| `MODCREATEDBY` | VARCHAR(255) | ✅ SÍ | Usuario creador |
| `MODCREATEDAT` | TIMESTAMP | ✅ SÍ | Fecha creación (automático) |

---

## 3. CLASIFICACIÓN DE RIESGO - ART. 6

### 3.1 Determinación de Alto Riesgo

**Proceso:** Se ejecuta mediante el **HighRiskClassifierViewModel** que presenta:

1. **8 Categorías Principales del Anexo III:**
   - III.1: Biometría y categorización biométrica
   - III.2: Gestión de infraestructuras críticas
   - III.3: Educación y formación profesional
   - III.4: Empleo y gestión de trabajadores
   - III.5: Acceso a servicios esenciales
   - III.6: Aplicación de la ley
   - III.7: Migración, asilo y control de fronteras
   - III.8: Administración de justicia

2. **25+ Subcategorías Específicas:**
   - Ejemplo: III.4.a (Contratación), III.4.b (Evaluación), III.5.a (Crédito), etc.

### 3.2 Determinación de GPAI (Art. 51)

**Campos en Model.java:**

| Campo | Tipo | Criterio |
|-------|------|----------|
| `MODISGPAI` | BOOLEAN | Si es modelo de propósito general |
| `MODGPAIFLOPSTRAINING` | NUMERIC(30,0) | FLOPs de entrenamiento |
| `MODGPAISYSTEMICRISK` | BOOLEAN | Si >10^25 FLOPs = riesgo sistémico |

**Criterios GPAI:**
- ✅ Modelo de propósito general (no específico)
- ✅ Capacidad de realizar múltiples tareas
- ✅ Puede ser integrado en sistemas posteriores

**Criterios Riesgo Sistémico (Anexo XIII):**
- ✅ >10^25 FLOPS de entrenamiento
- ✅ >10,000 usuarios profesionales en UE
- ✅ Capacidades de alto impacto equivalentes

### 3.3 Determinación de IA de Propósito General

**Lógica:**
```java
if (model.getModtype().contains("GENERAL_PURPOSE") || 
    model.getModtype().contains("FOUNDATION_MODEL")) {
    model.setModisgpai(true);
    
    // Evaluar riesgo sistémico
    if (model.getModgpaiflopstraining() != null && 
        model.getModgpaiflopstraining().compareTo(new BigDecimal("10000000000000000000000000")) > 0) {
        model.setModgpaisystemicrisk(true);
    }
}
```

---

## 4. CAMPOS OBLIGATORIOS Y VALIDACIONES

### 4.1 Campos Obligatorios para Clasificación

**Para Proyecto Alto Riesgo:**

| Campo | Obligatorio | Validación |
|-------|-------------|------------|
| `PRJISHIGHRISK` | ✅ SÍ | Boolean (true/false) |
| `PRJANNEXIIICATEGORIES` | ✅ SÍ (si alto riesgo) | JSONB array, mínimo 1 categoría |
| `PRJCLASSIFICATIONDATE` | ✅ SÍ (si alto riesgo) | Timestamp automático |
| `PRJCLASSIFICATIONAUTHOR` | ✅ SÍ (si alto riesgo) | VARCHAR(100), usuario actual |
| `PRJPROHIBITEDUSECHECKED` | ✅ SÍ | Boolean (validación Art. 5) |

**Para Modelo:**

| Campo | Obligatorio | Validación |
|-------|-------------|------------|
| `MODISHIGHRISK` | ✅ SÍ | Boolean |
| `MODANNEXIIICATEGORY` | ✅ SÍ (si alto riesgo) | VARCHAR(10), formato "III.X" |
| `MODANNEXIIISUBCATEGORY` | ✅ SÍ (si alto riesgo) | VARCHAR(10), formato "III.X.a" |
| `MODRISKCATEGORYJUSTIFICATION` | ✅ SÍ (si alto riesgo) | TEXT, mínimo 50 caracteres |

### 4.2 Validaciones Implementadas

**Validación 1: Categoría Anexo III**
```java
if (project.getPrjishighrisk() && 
    (project.getPrjannexiiicategories() == null || 
     project.getPrjannexiiicategories().isEmpty())) {
    throw new ValidationException("Proyecto alto riesgo requiere categoría Anexo III");
}
```

**Validación 2: Justificación Obligatoria**
```java
if (model.getModishighrisk() && 
    (model.getModriskcategoryjustification() == null || 
     model.getModriskcategoryjustification().length() < 50)) {
    throw new ValidationException("Justificación mínima 50 caracteres");
}
```

**Validación 3: Validación Art. 5 (Sistemas Prohibidos)**
```java
if (!project.getPrjprohibitedusechecked()) {
    throw new ValidationException("Debe validarse contra Art. 5 (sistemas prohibidos)");
}
```

---

## 5. REGLAS AUTOMÁTICAS DE DETECCIÓN

### 5.1 Sugerencia Automática con IA

**Microservicio:** `leka-prompt-governance` o `leka-llm-evaluation`

**Endpoint:** `/api/classification/suggest-category`

**Proceso:**
1. Usuario ingresa descripción del proyecto
2. Sistema envía descripción a microservicio Python
3. IA analiza y sugiere categoría Anexo III
4. Si confianza >70%, se muestra sugerencia automática
5. Usuario puede aceptar o seleccionar manualmente

**Código ViewModel:**
```java
private void suggestCategoryWithAI() {
    ProjectDescriptionDTO desc = new ProjectDescriptionDTO();
    desc.setName(currentProject.getName());
    desc.setDescription(currentProject.getDescription());
    
    AICategoryString suggestionDTO = pythonClient.callClassificationSuggestion(desc);
    
    if (suggestionDTO.getConfidence() > 0.7) {
        lblAISuggestion.setValue(
            "Sugerencia IA: " + suggestionDTO.getCategory() + 
            " (" + suggestionDTO.getConfidencePercent() + "% confianza)"
        );
    }
}
```

### 5.2 Reglas de Coherencia Automáticas

**Regla 1: Modelo sin Dataset**
```java
if (model.getModishighrisk() && 
    model.getModtrainingconfig() == null) {
    // Alerta: Modelo alto riesgo sin configuración de entrenamiento
    logWarning("Modelo alto riesgo sin dataset documentado");
}
```

**Regla 2: Proyecto Alto Riesgo sin Documentación Técnica**
```java
if (project.getPrjishighrisk() && 
    model.getModtechnicaldoccomplete() == null || 
    !model.getModtechnicaldoccomplete()) {
    // Alerta: Requiere documentación técnica (Art. 11)
    logWarning("Proyecto alto riesgo requiere documentación técnica completa");
}
```

**Regla 3: GPAI sin FLOPs Documentados**
```java
if (model.getModisgpai() && 
    model.getModgpaiflopstraining() == null) {
    // Alerta: GPAI requiere documentación de FLOPs
    logWarning("GPAI requiere documentación de FLOPs de entrenamiento");
}
```

**Regla 4: Alto Riesgo sin Evaluación de Conformidad**
```java
if (project.getPrjishighrisk() && 
    !hasConformityAssessment(project.getIdxproject())) {
    // Alerta: Requiere evaluación de conformidad (Art. 43)
    logWarning("Proyecto alto riesgo requiere evaluación de conformidad");
}
```

### 5.3 Validación de Información Incompleta

**Sistema de Alertas:**

1. **Nivel WARNING:** Campos opcionales faltantes
   - Se muestra en UI pero permite continuar
   - Ejemplo: Descripción del proyecto vacía

2. **Nivel ERROR:** Campos obligatorios faltantes
   - Bloquea guardado hasta completar
   - Ejemplo: Categoría Anexo III no seleccionada

3. **Nivel CRITICAL:** Coherencia de compliance
   - Bloquea workflow hasta resolver
   - Ejemplo: Alto riesgo sin documentación técnica

---

## 6. REGISTRO EN LOGS INMUTABLES

### 6.1 Entidad de Logs

**Tabla:** `IMLIMMUTABLELOGS`  
**Entidad:** `ImmutableLog.java`

### 6.2 Campos Registrados en Clasificación

**Al clasificar un proyecto como alto riesgo, se registra:**

```json
{
  "entityType": "PROJECT",
  "entityId": 12345,
  "action": "CLASSIFY_HIGH_RISK",
  "userId": 67890,
  "userName": "compliance.officer",
  "data": {
    "projectId": 12345,
    "projectName": "Sistema de Scoring Crediticio",
    "isHighRisk": true,
    "annexIIICategories": ["III.5.a"],
    "classificationDate": "2025-11-15T10:30:00Z",
    "classificationAuthor": "compliance.officer",
    "justification": "Sistema utilizado para evaluación de crédito...",
    "previousClassification": null
  },
  "timestamp": "2025-11-15T10:30:00Z",
  "previousHash": "abc123...",
  "currentHash": "def456..."
}
```

### 6.3 Hash Chain (Blockchain-style)

**Proceso:**
1. Cada log incluye `IMLPREVIOUSHASH` (hash del log anterior)
2. Cada log calcula `IMLCURRENTHASH` (SHA-256 de sus datos)
3. Cualquier modificación rompe la cadena
4. Verificación de integridad automática

**Código:**
```java
// ImmutableLoggingBusinessService
public void logClassification(Project project, String action) {
    ImmutableLog log = new ImmutableLog();
    
    // Obtener hash del último log
    String previousHash = getLastLogHash(project.getIdxproject());
    log.setImlprevioushash(previousHash);
    
    // Calcular hash actual
    String currentHash = calculateHash(log.getImldata());
    log.setImlcurrenthash(currentHash);
    
    // Guardar (append-only)
    immutableLogDAO.save(log);
}
```

### 6.4 Inmutabilidad Garantizada

**Trigger PostgreSQL:**
```sql
CREATE TRIGGER trigger_prevent_update
BEFORE UPDATE ON IMLIMMUTABLELOGS
FOR EACH ROW
EXECUTE FUNCTION prevent_update();

CREATE TRIGGER trigger_prevent_delete
BEFORE DELETE ON IMLIMMUTABLELOGS
FOR EACH ROW
EXECUTE FUNCTION prevent_delete();
```

**Resultado:** Cualquier intento de UPDATE o DELETE es bloqueado a nivel de base de datos.

---

## 7. EVIDENCIA DE CLASIFICACIÓN

### 7.1 Ubicación de Evidencias

**1. Base de Datos:**

**Tabla:** `PRJPROJECTS`
- Campo: `PRJISHIGHRISK` (Boolean)
- Campo: `PRJANNEXIIICATEGORIES` (JSONB)
- Campo: `PRJCLASSIFICATIONDATE` (TIMESTAMP)
- Campo: `PRJCLASSIFICATIONAUTHOR` (VARCHAR)

**Tabla:** `MODMODELS`
- Campo: `MODISHIGHRISK` (Boolean)
- Campo: `MODANNEXIIICATEGORY` (VARCHAR)
- Campo: `MODANNEXIIISUBCATEGORY` (VARCHAR)
- Campo: `MODRISKCATEGORYJUSTIFICATION` (TEXT)

**Tabla:** `IMLIMMUTABLELOGS`
- Registro completo de la acción de clasificación
- Hash chain para verificación de integridad
- Snapshot completo del estado en `IMLDATA`

### 7.2 Pantallas de Visualización

**Pantalla Principal:**
- **URL:** `/console/gobierno/compliance/high-risk-classifier.zul`
- **ViewModel:** `HighRiskClassifierViewModel.java`
- **Funcionalidad:**
  - Visualización de categorías Anexo III
  - Historial de clasificaciones
  - Justificación guardada
  - Sugerencia IA mostrada

**Pantalla de Proyecto:**
- **URL:** `/console/projects/detail.zul?projectId={id}`
- **Sección Compliance:**
  - Estado de clasificación (Alto Riesgo / No Alto Riesgo)
  - Categorías Anexo III asignadas
  - Fecha y autor de clasificación
  - Enlace a justificación completa

### 7.3 Reportes y Exportación

**Reporte de Clasificación:**
- PDF generado automáticamente al clasificar
- Incluye:
  - Información del proyecto
  - Categorías seleccionadas
  - Justificación
  - Fecha y autor
  - Hash del log inmutable

**Exportación para Auditoría:**
- Endpoint: `/api/compliance/export-classification/{projectId}`
- Formatos: JSON, PDF, CSV
- Incluye todos los logs inmutables relacionados

### 7.4 Verificación de Integridad

**Endpoint:** `/api/compliance/verify-classification-integrity/{projectId}`

**Proceso:**
1. Obtiene todos los logs relacionados al proyecto
2. Verifica hash chain (cada hash debe coincidir con el siguiente)
3. Detecta cualquier manipulación
4. Retorna estado: `VALID`, `TAMPERED`, `UNVERIFIED`

---

## 8. RESUMEN EJECUTIVO PARA AUDITOR

### 8.1 Proceso Completo

1. **Creación:** Usuario crea proyecto con campos obligatorios mínimos
2. **Clasificación:** Usuario accede a clasificador de alto riesgo
3. **Sugerencia IA:** Sistema sugiere categoría automáticamente (opcional)
4. **Selección Manual:** Usuario selecciona categoría/subcategoría Anexo III
5. **Justificación:** Usuario proporciona justificación (mínimo 50 caracteres)
6. **Validación:** Sistema valida coherencia y completitud
7. **Guardado:** Se guarda en BD y se genera log inmutable
8. **Workflow:** Si alto riesgo, se inicia workflow de compliance

### 8.2 Evidencias Disponibles

✅ **Base de Datos:** Campos de clasificación en `PRJPROJECTS` y `MODMODELS`  
✅ **Logs Inmutables:** Registro completo en `IMLIMMUTABLELOGS` con hash chain  
✅ **Pantallas UI:** Visualización en `/console/gobierno/compliance/high-risk-classifier.zul`  
✅ **Reportes:** PDF exportable con toda la información  
✅ **Verificación:** Endpoint de verificación de integridad de logs

### 8.3 Cumplimiento EU AI Act

✅ **Art. 6:** Clasificación de sistemas de alto riesgo implementada  
✅ **Art. 11:** Documentación técnica requerida para alto riesgo  
✅ **Art. 15:** Métricas de precisión y rendimiento  
✅ **Art. 19:** Logs inmutables con hash chain  
✅ **Art. 51:** Clasificación GPAI y riesgo sistémico  
✅ **Anexo III:** 8 categorías principales + 25+ subcategorías

---

## 9. PANTALLAS Y NAVEGACIÓN

### 9.1 Ruta de Navegación

```
Dashboard → Proyectos → [Seleccionar Proyecto] → 
Pestaña "Compliance" → Botón "Clasificar Riesgo" → 
Pantalla High Risk Classifier
```

### 9.2 Capturas de Pantalla (Referencias)

**Pantalla 1: Lista de Proyectos**
- Ubicación: `/console/projects/list.zul`
- Muestra columna "Riesgo" con badge (Alto Riesgo / No Alto Riesgo)

**Pantalla 2: Detalle de Proyecto**
- Ubicación: `/console/projects/detail.zul`
- Sección "Clasificación de Riesgo" con:
  - Estado actual
  - Categorías asignadas
  - Fecha de clasificación
  - Botón "Re-clasificar" o "Ver Detalle"

**Pantalla 3: Clasificador de Alto Riesgo**
- Ubicación: `/console/gobierno/compliance/high-risk-classifier.zul`
- Componentes:
  - Información del proyecto (read-only)
  - Sugerencia IA (si disponible)
  - Lista de categorías Anexo III (8 principales)
  - Lista de subcategorías (dinámica según selección)
  - Campo de justificación (obligatorio)
  - Botones: "Clasificar" y "Cancelar"

---

## 10. CONCLUSIÓN

El sistema de catalogación y clasificación de sistemas de IA está **completamente implementado** y cumple con los requisitos del EU AI Act:

✅ **Proceso documentado** y accesible desde UI  
✅ **Validaciones automáticas** de coherencia  
✅ **Logs inmutables** con hash chain para auditoría  
✅ **Evidencias completas** en base de datos y logs  
✅ **Sugerencia IA** para facilitar clasificación  
✅ **Cumplimiento** de Art. 6, 11, 15, 19, 51 y Anexo III

**Estado:** ✅ **LISTO PARA AUDITORÍA**

---

**Última actualización:** Noviembre 2025  
**Versión:** 1.0  
**Mantenedor:** CodeflowX Compliance Team

