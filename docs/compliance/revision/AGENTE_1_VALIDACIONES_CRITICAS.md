# GUÍA AGENTE 1 - VALIDACIONES CRÍTICAS

**Agente:** Backend Senior
**Equipo:** Equipo 1 - Validaciones Críticas
**Duración:** 12 horas
**Objetivo:** Implementar validaciones críticas que bloquean certificación EU AI Act

---

## 📋 INCIDENCIAS ASIGNADAS

### **Incidencias Completadas (Trabajo Nocturno):**
| ID | Descripción | Esfuerzo | Prioridad | Estado |
|----|-------------|----------|-----------|--------|
| **INC-005** | Validación sistemas prohibidos Art. 5 | 1h | 🔴 CRÍTICA | ✅ COMPLETADO |
| **INC-011** | Checklist completo validación modelos | 3h | 🔴 CRÍTICA | ✅ COMPLETADO |
| **INC-013** | Validación medidas mitigación implementadas | 2h | 🔴 CRÍTICA | ✅ COMPLETADO |

### **Incidencias Pendientes (Nuevas Asignaciones):**
| ID | Descripción | Esfuerzo | Prioridad | Estado Inicial |
|----|-------------|----------|-----------|----------------|
| **INC-007** | Validación cruzada FRIA vs métricas | 2h | 🔴 CRÍTICA | 🔴 PENDIENTE |
| **INC-005-DS** | Validación integridad datasets | 2h | 🟡 ALTA | ✅ COMPLETADO |
| **INC-002** | Validación confianza sugerencia IA | 1h | 🟡 MEDIA | 🔴 PENDIENTE |
| **INC-004** | Validación calidad justificación | 2h | 🟡 MEDIA | ✅ COMPLETADO |
| **INC-008** | Validación fórmula cálculo riesgo | 1h | 🟡 MEDIA | ✅ COMPLETADO |

**Total:** 5 incidencias pendientes, ~8 horas

---

## 📚 DOCUMENTOS DE REFERENCIA

### Prompts Específicos:
1. **INC-005:**
   - `/docs/compliance/gaps/prompts/java/INC-005_sistemas_prohibidos.md`
   - Artículo EU AI Act: **Art. 5, Anexo II**

2. **INC-011:**
   - `/docs/compliance/gaps/prompts/java/INC-011_checklist_modelos.md`
   - Artículo EU AI Act: **Art. 11, 15**

3. **INC-013:**
   - `/docs/compliance/gaps/prompts/java/INC-013_validacion_medidas.md`
   - Artículo EU AI Act: **Art. 27.1.f**

### Documentación General:
- **Arquitectura EnArt:** `/docs/compliance/PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md`
- **Convenciones:** `/docs/compliance/revision/PLAN_ACCION_INCIDENCIAS.md`
- **Seguimiento:** `/docs/compliance/gaps/SEGUIMIENTO_INCIDENCIAS.md`
- **Plan Nocturno:** `/docs/compliance/revision/PLAN_TRABAJO_NOCTURNO.md`

### Referencias Técnicas:
- **Entidades Existentes:** `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/`
- **BusinessServices Existentes:** `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/`
- **Scripts SQL:** `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/`
- **Tablas Catalog:** `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/tablas.md`

---

## 🏗️ ARQUITECTURA ENART - CONVENCIONES OBLIGATORIAS

### **Nomenclatura de Tablas:**
- **Prefijo 3 caracteres** según módulo funcional
- **Ejemplos:** `PRJ` (proyectos), `MOD` (modelos), `COM` (compliance), `FRIA` (FRIA), `PMM` (PMM)
- **Formato:** `PREFIJO` + `NOMBRE` (todo MAYÚSCULAS)
- **Ejemplo:** `PRJPROJECTS`, `MODMODELS`, `COMCOMPLIANCEASSESSMENTS`

### **Primary Key:**
- **Nombre:** `IDX` + `NOMBREENTIDAD` (ej: `IDXPROJECT`, `IDXMODEL`)
- **Tipo:** `BIGSERIAL` (autonumérico)
- **JPA:** `@Id @GeneratedValue(strategy = GenerationType.IDENTITY)`

### **UUID:**
- **Campo obligatorio:** `iduuid` (VARCHAR(36))
- **Generación automática:** `@PrePersist` con `UUID.randomUUID().toString()`

### **Campos de Auditoría:**
- `createdat` (TIMESTAMP)
- `updatedat` (TIMESTAMP)
- **Hooks:** `@PrePersist` y `@PreUpdate`

### **Foreign Keys:**
- **Formato:** `IDX` + `TABLAORIGEN` (ej: `IDXPROJECT`)
- **Relación:** `@ManyToOne(fetch = FetchType.LAZY)`
- **JoinColumn:** `@JoinColumn(name = "IDXPROJECT", nullable = false)`

---

## 📁 ESTRUCTURA DE DIRECTORIOS

### **Entidades JPA:**
```
/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/
├── models/              ← Entidades de modelos
│   └── Model.java       ← Ya existe
├── compliance/          ← Entidades de compliance
│   ├── ComplianceAssessment.java
│   ├── FriaAssessment.java
│   └── [NUEVAS AQUÍ]
└── governance/         ← Entidades de governance
    └── [NUEVAS AQUÍ]
```

### **BusinessServices:**
```
/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/
├── models/              ← Servicios de modelos
│   ├── ModelValidationService.java  ← Ya existe, MODIFICAR
│   └── [NUEVOS AQUÍ]
├── compliance/          ← Servicios de compliance
│   └── [NUEVOS AQUÍ]
└── governance/         ← Servicios de governance
    └── [NUEVOS AQUÍ]
```

### **Scripts SQL:**
```
/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/
├── [NUEVOS_SCRIPTS.sql]  ← Crear scripts DDL aquí
└── tablas.md             ← Actualizar este archivo
```

---

## 🔧 IMPLEMENTACIÓN POR INCIDENCIA

### **INC-005: Validación Sistemas Prohibidos Art. 5**

#### **Archivos a Modificar/Crear:**

1. **BusinessService (MODIFICAR):**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/models/ModelValidationService.java`
   - **Método a agregar:** `validateProhibitedSystems(Model model)`
   - **Lógica:** Verificar contra Anexo II del EU AI Act

2. **Entidad (SI ES NECESARIA):**
   - Verificar si existe entidad para categorías prohibidas
   - Si no existe: crear en `entity/catalogs/ProhibitedSystemCategory.java`
   - Tabla: `PRHPROHIBITEDSYSTEMCATEGORIES` (prefijo `PRH`)

3. **Script SQL (SI ES NECESARIA):**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/prohibited_system_categories.sql`

#### **Checklist:**
- [ ] Leer prompt completo: `INC-005_sistemas_prohibidos.md`
- [ ] Verificar entidades existentes relacionadas
- [ ] Crear/modificar BusinessService según prompt
- [ ] Crear entidad si es necesaria (seguir convenciones EnArt)
- [ ] Crear script SQL si es necesaria
- [ ] Actualizar `tablas.md`
- [ ] Compilar sin errores
- [ ] Actualizar `SEGUIMIENTO_INCIDENCIAS.md`

---

### **INC-011: Checklist Completo Validación Modelos**

#### **Archivos a Modificar/Crear:**

1. **BusinessService (MODIFICAR):**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/models/ModelValidationService.java`
   - **Método a agregar:** `validateModelChecklist(Model model)`
   - **Integración:** Usar `QualityManagementSystemBusinessService`, `TechnicalDocumentationBusinessService`

2. **Entidad (SI ES NECESARIA):**
   - Verificar si existe entidad para checklist items
   - Si no existe: crear en `entity/models/ModelValidationChecklist.java`
   - Tabla: `MVCMODELVALIDATIONCHECKLISTS` (prefijo `MVC`)

3. **Script SQL (SI ES NECESARIA):**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/model_validation_checklist.sql`

#### **Dependencias:**
- **Requiere:** INC-005 completada primero
- **Usa:** `QualityManagementSystemBusinessService`, `TechnicalDocumentationBusinessService`

#### **Checklist:**
- [ ] Leer prompt completo: `INC-011_checklist_modelos.md`
- [ ] Verificar que INC-005 está completada
- [ ] Revisar BusinessServices existentes relacionados
- [ ] Crear/modificar BusinessService según prompt
- [ ] Crear entidad si es necesaria
- [ ] Crear script SQL si es necesaria
- [ ] Actualizar `tablas.md`
- [ ] Compilar sin errores
- [ ] Actualizar `SEGUIMIENTO_INCIDENCIAS.md`

---

### **INC-013: Validación Medidas Mitigación Implementadas**

#### **Archivos a Modificar/Crear:**

1. **BusinessService (CREAR o MODIFICAR):**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/MitigationMeasuresValidationService.java`
   - **O modificar:** `FriaAssessmentBusinessService.java` si aplica

2. **Entidad (VERIFICAR):**
   - Verificar si existe: `entity/compliance/MitigationMeasure.java`
   - Si no existe: crear
   - Tabla: `MITMITIGATIONMEASURES` (prefijo `MIT`)

3. **Script SQL (SI ES NECESARIA):**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/mitigation_measures.sql`

#### **Dependencias:**
- **Requiere:** INC-011 completada
- **Usa:** `FriaAssessmentBusinessService`

#### **Checklist:**
- [ ] Leer prompt completo: `INC-013_validacion_medidas.md`
- [ ] Verificar que INC-011 está completada
- [ ] Revisar `FriaAssessmentBusinessService` existente
- [ ] Crear/modificar BusinessService según prompt
- [ ] Crear entidad si es necesaria
- [ ] Crear script SQL si es necesaria
- [ ] Actualizar `tablas.md`
- [ ] Compilar sin errores
- [ ] Actualizar `SEGUIMIENTO_INCIDENCIAS.md`

---

## 📝 PLANTILLA DE ENTIDAD ENART

```java
package com.codeflowx.govern.entity.[namespace];

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.enartframework.nocode.annotacion.Entidad;
import org.enartframework.nocode.annotacion.Field;
import com.codeflowx.govern.entity.projects.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "PREFIJONOMBRETABLA")
@Entidad(
    namespace = "[namespace]",
    type = "TABLE",
    name = "PREFIJONOMBRETABLA",
    labelMonitor = "CAMPO_LABEL",
    pk = "IDXNOMBREENTIDAD"
)
public class NombreEntidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDXNOMBREENTIDAD", nullable = false)
    @Field(criteria = true, auditar = false, filter = true, label = "ID", type = "LONG")
    private Long idxnombreentidad;

    @NotNull
    @Size(max = 36)
    @Column(name = "iduuid", unique = true, nullable = false, length = 36)
    @Field(criteria = true, auditar = false, filter = true, label = "UUID", type = "VARCHAR")
    private String iduuid;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDXPROJECT", nullable = false)
    private Project project;

    // Campos específicos aquí...

    @Column(name = "CREATEDAT")
    @Field(criteria = true, auditar = false, filter = true, label = "Created At", type = "TIMESTAMP")
    private Timestamp createdat;

    @Column(name = "UPDATEDAT")
    @Field(criteria = true, auditar = false, filter = true, label = "Updated At", type = "TIMESTAMP")
    private Timestamp updatedat;

    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        createdat = now;
        if (iduuid == null) {
            iduuid = java.util.UUID.randomUUID().toString();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedat = new Timestamp(System.currentTimeMillis());
    }
}
```

---

## 📝 PLANTILLA DE BUSINESS SERVICE

```java
package com.codeflowx.govern.business.[namespace];

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.enartframework.orm.exception.DaoException;
import lombok.extern.slf4j.Slf4j;
import com.codeflowx.govern.business.exception.BussinessException;
import codeflowx.nocode.persist.BusinessService;
import com.codeflowx.govern.entity.[namespace].NombreEntidad;
import java.util.List;

/**
 * BusinessService para [Descripción según prompt].
 *
 * <p><b>RESPONSABILIDAD:</b></p>
 * <p>[Descripción detallada según prompt]</p>
 *
 * <p><b>Artículo EU AI Act:</b> Art. [número]</p>
 *
 * @author CodeflowX Development Team
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Service
public class NombreBusinessService {

    @Autowired
    private BusinessService businessService;

    /**
     * [Método según prompt]
     *
     * @param [parámetros]
     * @return [retorno]
     * @throws BussinessException si ocurre un error
     */
    public [TipoRetorno] [nombreMetodo]([parámetros]) throws BussinessException {
        log.debug("[Mensaje log]");

        try {
            // Implementación según prompt
            // Usar: businessService.findById(), businessService.insert(),
            //       businessService.update(), businessService.queryfromParams()

        } catch (DaoException e) {
            log.error("Error [descripción]", e);
            throw new BussinessException("Error [descripción]", e);
        }
    }
}
```

---

## 📝 PLANTILLA DE SCRIPT SQL

```sql
-- ============================================================================
-- TABLA: PREFIJONOMBRETABLA
-- Descripción: [Descripción según prompt]
-- Artículo EU AI Act: Art. [número]
-- ============================================================================

CREATE TABLE IF NOT EXISTS PREFIJONOMBRETABLA (
    IDXNOMBREENTIDAD BIGSERIAL NOT NULL,
    iduuid VARCHAR(36) NOT NULL UNIQUE,
    IDXPROJECT BIGINT NOT NULL,

    -- Campos específicos aquí...

    CREATEDAT TIMESTAMP,
    UPDATEDAT TIMESTAMP,

    CONSTRAINT PK_PREFIJONOMBRETABLA PRIMARY KEY (IDXNOMBREENTIDAD),
    CONSTRAINT FK_PREFIJONOMBRETABLA_PROJECT FOREIGN KEY (IDXPROJECT)
        REFERENCES PRJPROJECTS(IDXPROJECT) ON DELETE CASCADE
);

-- Índices
CREATE INDEX IF NOT EXISTS IDX_PREFIJONOMBRETABLA_PROJECT
    ON PREFIJONOMBRETABLA(IDXPROJECT);
CREATE INDEX IF NOT EXISTS IDX_PREFIJONOMBRETABLA_UUID
    ON PREFIJONOMBRETABLA(iduuid);

-- Comentarios
COMMENT ON TABLE PREFIJONOMBRETABLA IS '[Descripción]';
COMMENT ON COLUMN PREFIJONOMBRETABLA.IDXNOMBREENTIDAD IS 'Primary key autonumérico';
COMMENT ON COLUMN PREFIJONOMBRETABLA.iduuid IS 'UUID único para trazabilidad';
```

---

## ✅ CHECKLIST FINAL POR INCIDENCIA

Para cada incidencia, verificar:

### **Antes de Empezar:**
- [ ] Leer prompt completo
- [ ] Leer artículo EU AI Act correspondiente
- [ ] Verificar entidades existentes relacionadas
- [ ] Verificar BusinessServices existentes relacionados
- [ ] Identificar dependencias con otras incidencias

### **Durante Implementación:**
- [ ] Crear/modificar entidad según convenciones EnArt
- [ ] Crear/modificar BusinessService según prompt
- [ ] Crear script SQL si es necesaria nueva tabla
- [ ] Seguir principios SOLID y arquitectura hexagonal
- [ ] Aplicar KISS (Keep It Simple, Stupid)
- [ ] Tercera forma normal en diseño de tablas

### **Después de Implementación:**
- [ ] Compilar sin errores: `mvn clean compile -DskipTests`
- [ ] Actualizar `tablas.md` con nueva tabla (si aplica)
- [ ] Actualizar `AUDITORIA_ENTIDADES.md` (si aplica)
- [ ] Actualizar `SEGUIMIENTO_INCIDENCIAS.md` (marcar como completado)
- [ ] **Actualizar documento de auditoría asociado** (ver sección siguiente)
- [ ] **Documentar BusinessService** en `/docs/developers/` (ver sección siguiente)
- [ ] **Registrar archivos creados** en este documento (ver sección siguiente)
- [ ] Commit con mensaje descriptivo: `[INC-XXX] Descripción breve`

---

## 📝 REGISTRO DE ARCHIVOS CREADOS

Al finalizar cada incidencia, **registrar aquí** todos los archivos creados o modificados:

### **INC-005: Validación Sistemas Prohibidos Art. 5**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] Entidad: `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/ProhibitedSystem.java`
- [x] Service CRUD: `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/compliance/ProhibitedSystemService.java`
- [x] BusinessService: `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/ProhibitedSystemBusinessService.java`
- [x] Script SQL: `nocode.service.entitys/src/main/resources/sql/prohibited_systems.sql`
- [x] Documentación: `docs/developers/compliance/ProhibitedSystemBusinessService.md`

**Archivos Modificados:**
- [x] `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/models/ModelValidationService.java` - Agregados métodos `validateProhibitedSystems(Model)` y `validateProhibitedSystems(Project)`
- [x] `docs/developers/models/ModelValidationService.md` - Actualizada documentación con nuevos métodos
- [x] `nocode.service.entitys/src/main/resources/tablas.md` - Agregada tabla `govprohibitedsystems`
- [x] `docs/compliance/gaps/SEGUIMIENTO_INCIDENCIAS.md` - Actualizado estado a COMPLETADO

**Fecha Finalización:** 2025-11-25

---

### **INC-011: Checklist Completo Validación Modelos**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] Documentación: `docs/developers/models/ModelValidationService.md` - Actualizada con nuevos métodos

**Archivos Modificados:**
- [x] `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/models/ModelValidationService.java` - Agregados métodos:
  - `validateModelComplete(Long modelId, ModelType type)` - Validación completa por tipo
  - `validateCommonRequirements()` - Checklist común
  - `validateOpenAISpecific()` - Validación específica OpenAI
  - `validateOpenSourceSpecific()` - Validación específica Open Source
  - `validateInternalSpecific()` - Validación específica modelos internos
  - `validateTechnicalDocumentation()` - Validación documentación técnica (Anexo IV)
  - `validateRobustness()` - Validación robustez adversarial (Art. 15)
  - Clases: `ModelValidationResult`, `ValidationIssue`, enum `ModelType`
- [x] `docs/developers/models/ModelValidationService.md` - Actualizada documentación completa
- [x] `docs/compliance/gaps/SEGUIMIENTO_INCIDENCIAS.md` - Actualizado estado a COMPLETADO

**Fecha Finalización:** 2025-11-25

---

### **INC-013: Validación Medidas Mitigación Implementadas**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] BusinessService: `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/MitigationMeasureValidationService.java`
- [x] Documentación: `docs/developers/compliance/MitigationMeasureValidationService.md`

**Archivos Modificados:**
- [x] `docs/compliance/gaps/SEGUIMIENTO_INCIDENCIAS.md` - Actualizado estado a COMPLETADO

**Nota:** La entidad `MitigationMeasure` ya existía como @Embeddable en `FriaAssessment`. No se requirió crear nueva entidad.

**Fecha Finalización:** 2025-11-25

---

### **INC-005-DS: Validación Integridad Datasets**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] Utilidad: `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/utils/DatasetHashUtil.java`
- [x] BusinessService: `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/governance/DatasetHashVerificationService.java`
- [x] Script SQL: `nocode.service.entitys/src/main/resources/sql/dataset_quality_hash_fields.sql`

**Archivos Modificados:**
- [x] `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/governance/DatasetQuality.java` - Agregados campos: `dqldatasethash`, `dqlhashalgorithm`, `dqlhashverified`, `dqlprevioushash`
- [x] `docs/compliance/gaps/SEGUIMIENTO_INCIDENCIAS.md` - Actualizado estado a COMPLETADO

**Fecha Finalización:** 2025-11-26

---

## 📋 ACTUALIZACIÓN DE DOCUMENTOS DE AUDITORÍA

Al finalizar cada incidencia, **actualizar** los siguientes documentos:

### **INC-005: Validación Sistemas Prohibidos Art. 5**

**Documentos a Actualizar:**
1. **`/docs/compliance/auditoria/AUDITORIA_CATALOGACION_CLASIFICACION_IA.md`**
   - Buscar sección relacionada con Art. 5
   - Actualizar estado de implementación
   - Añadir referencia a entidad/BusinessService creado

2. **`/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md`**
   - Buscar `INC-005`
   - Cambiar estado de `🔴 PENDIENTE` a `🟢 COMPLETADO`
   - Añadir fecha de finalización
   - Añadir notas sobre implementación

3. **`/docs/compliance/gaps/SEGUIMIENTO_INCIDENCIAS.md`**
   - Buscar `INC-005`
   - Actualizar estado, fecha fin, notas

**Formato de Actualización:**
```markdown
| INC-005 | Validación sistemas prohibidos Art. 5 | Art. 5, Anexo II | 🔴 CRÍTICA | 🟢 COMPLETADO | Agente 1 | 2025-11-25 | 2025-11-25 | ✅ Implementado: ProhibitedSystemCategory entity, ModelValidationService.validateProhibitedSystems() |
```

---

### **INC-011: Checklist Completo Validación Modelos**

**Documentos a Actualizar:**
1. **`/docs/compliance/auditoria/AUDITORIA_EVALUACION_MODELOS_EXTERNOS.md`**
   - Buscar sección relacionada con Art. 11, 15
   - Actualizar estado de implementación
   - Añadir referencia a entidad/BusinessService creado

2. **`/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md`**
   - Buscar `INC-011`
   - Cambiar estado de `🔴 PENDIENTE` a `🟢 COMPLETADO`
   - Añadir fecha de finalización
   - Añadir notas sobre implementación

3. **`/docs/compliance/gaps/SEGUIMIENTO_INCIDENCIAS.md`**
   - Buscar `INC-011`
   - Actualizar estado, fecha fin, notas

---

### **INC-013: Validación Medidas Mitigación Implementadas**

**Documentos a Actualizar:**
1. **`/docs/compliance/auditoria/AUDITORIA_FRIA_EVALUACIONES_TECNICAS.md`**
   - Buscar sección relacionada con Art. 27.1.f
   - Actualizar estado de implementación
   - Añadir referencia a entidad/BusinessService creado

2. **`/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md`**
   - Buscar `INC-013`
   - Cambiar estado de `🔴 PENDIENTE` a `🟢 COMPLETADO`
   - Añadir fecha de finalización
   - Añadir notas sobre implementación

3. **`/docs/compliance/gaps/SEGUIMIENTO_INCIDENCIAS.md`**
   - Buscar `INC-013`
   - Actualizar estado, fecha fin, notas

---

## 📚 DOCUMENTACIÓN DE BUSINESS SERVICES

**IMPORTANTE:** Todos los BusinessServices creados o modificados **DEBEN** ser documentados en `/docs/developers/`.

### **Proceso de Documentación:**

1. **Identificar categoría del servicio:**
   - `compliance/` - Servicios de compliance
   - `models/` - Servicios de modelos
   - `governance/` - Servicios de governance
   - `evaluation/` - Servicios de evaluación
   - `logging/` - Servicios de logging
   - `catalogs/` - Servicios de catálogos

2. **Crear documento Markdown:**
   - Ubicación: `/docs/developers/[categoria]/[NombreBusinessService].md`
   - Formato: Ver ejemplo en `/docs/developers/compliance/FriaAssessmentBusinessService.md`

3. **Estructura del documento:**
   ```markdown
   # [NombreBusinessService]

   ## Descripción Funcional
   [Qué hace y por qué existe]

   ## Responsabilidades
   [Qué debe hacer y qué NO debe hacer]

   ## Dependencias
   [Servicios, entidades, librerías]

   ## Arquitectura
   [Cómo se integra en el sistema]

   ## API Pública
   [Métodos públicos con ejemplos]

   ## Casos de Uso
   [Escenarios reales]

   ## Configuración
   [Properties, variables de entorno]

   ## Testing
   [Estrategias recomendadas]

   ## Evolución
   [Cómo extender o modificar]

   ## Referencias
   - Prompts: [INC-XXX]
   - Artículo EU AI Act: Art. [número]
   - ViewModels: [si aplica]
   ```

4. **Actualizar índice:**
   - Editar `/docs/developers/README.md`
   - Añadir entrada en la sección correspondiente

### **BusinessServices a Documentar:**

#### **INC-005:**
- [x] `ProhibitedSystemBusinessService` - Documentado en `/docs/developers/compliance/ProhibitedSystemBusinessService.md`
- [x] `ModelValidationService` - Actualizado documento existente en `/docs/developers/models/ModelValidationService.md`

#### **INC-011:**
- [x] `ModelValidationService` - Actualizado documento existente en `/docs/developers/models/ModelValidationService.md` con nuevos métodos

#### **INC-013:**
- [x] `MitigationMeasureValidationService` - Documentado en `/docs/developers/compliance/MitigationMeasureValidationService.md`

**Checklist de Documentación:**
- [x] Documento creado en `/docs/developers/compliance/ProhibitedSystemBusinessService.md`
- [x] Documento creado en `/docs/developers/compliance/MitigationMeasureValidationService.md`
- [x] Documento actualizado en `/docs/developers/models/ModelValidationService.md`
- [x] Todas las secciones completadas
- [x] Ejemplos de código incluidos
- [x] Referencias a prompts y artículos EU AI Act
- [x] Índice actualizado en `README.md`

---

## 🔄 COORDINACIÓN CON OTROS AGENTES

### **Con Agente 2:**
- **Compartir módulo:** `govern.business.models`
- **Coordinación:** Trabajar en archivos diferentes o coordinar por método
- **Testing:** Agente 2 ayudará con testing de INC-005 e INC-011

### **Evitar Conflictos:**
- **Branch separado:** `feature/agente1-validaciones-criticas`
- **Archivos propios:** No modificar archivos que Agente 2 está usando
- **Comunicación:** Reportar cada 2 horas en checkpoint

---

## 📊 REPORTE DE PROGRESO (Cada 2 horas)

```
AGENTE 1 - REPORTE HORA [X]
==========================
Incidencias Completadas:
- INC-005: [Estado: ✅/🟡/🔴] - [Notas]
- INC-011: [Estado: ✅/🟡/🔴] - [Notas]
- INC-013: [Estado: ✅/🟡/🔴] - [Notas]

Incidencias En Progreso:
- [Nombre]: [Progreso %] - [Bloqueos si hay]

Archivos Creados/Modificados:
- [Lista de archivos]

Bloqueos/Problemas:
- [Lista de bloqueos]

Tiempo Estimado Restante:
- [Horas estimadas]
```

---

## 🚨 GESTIÓN DE BLOQUEOS

### **Si encuentras bloqueos:**

1. **Dependencia con otra incidencia:**
   - Verificar estado en `SEGUIMIENTO_INCIDENCIAS.md`
   - Si está pendiente, implementar stub/mock temporal
   - Documentar TODO para integración posterior

2. **Entidad existente no encontrada:**
   - Buscar en `/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/`
   - Verificar `tablas.md` para nombre de tabla
   - Si no existe, crearla según convenciones

3. **Error de compilación:**
   - Revisar imports
   - Verificar que entidades están compiladas: `mvn clean install -f nocode.service.entitys/pom.xml`
   - Verificar convenciones de nombres

4. **Duda sobre implementación:**
   - Revisar prompt completo
   - Revisar entidades similares existentes como referencia
   - Documentar la duda en reporte de checkpoint

---

## 📚 REFERENCIAS RÁPIDAS

### **Comandos Útiles:**
```bash
# Compilar módulo de entidades
cd /mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service/nocode.service.entitys
mvn clean install -DskipTests

# Compilar módulo de business
cd /mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service/codeflowx.govern.business
mvn clean compile -DskipTests

# Verificar errores de compilación
mvn clean compile -DskipTests 2>&1 | grep ERROR
```

### **Rutas Importantes:**
- **Prompts:** `/git/suinsit.nova.web/docs/compliance/gaps/prompts/java/`
- **Entidades:** `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/`
- **BusinessServices:** `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/`
- **SQL:** `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/`
- **Documentación:** `/git/suinsit.nova.web/docs/compliance/`

---

**Última Actualización:** 25 de noviembre de 2025
**Próxima Revisión:** Al completar cada incidencia
