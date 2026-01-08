# Arquitectura de Gobernanza de Prompts - EU AI Act Compliance

## Contexto del Sistema

En un escenario de gobernanza de IA y cumplimiento de la **EU AI Act**, con:
- **Project** como núcleo principal
- **HITL** (Human in the Loop) para supervisión
- **AI OS** (AI Operating System) para gestión de infraestructura
- **Agentes** de IA
- **Modelos** de IA

## Propuesta de Organización de Prompts

### 1. Relaciones Principales (JPA)

#### Prompt → Project (OBLIGATORIO)
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "IDXPROJECT", nullable = false)
private Project project;
```

**Justificación:**
- **Trazabilidad**: Todo prompt debe estar asociado a un proyecto para cumplimiento EU AI Act
- **Clasificación**: El proyecto determina la categoría de riesgo (B.1, B.2, C, D, Prohibido)
- **FRIA**: Los prompts de sistemas de alto riesgo requieren evaluación FRIA asociada al proyecto
- **Auditoría**: Permite rastrear todos los prompts usados en un proyecto específico

#### Prompt → Agent (OPCIONAL)
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "IDXAGENT", nullable = true)
private Agent agent;
```

**Justificación:**
- **Especificidad**: Si un prompt es específico de un agente
- **Monitoreo**: Permite rastrear qué prompts usa cada agente
- **Versionado**: Agentes pueden tener múltiples versiones de prompts

#### Prompt → Model (OPCIONAL)
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "IDXMODEL", nullable = true)
private Model model;
```

**Justificación:**
- **Optimización**: Prompts optimizados para modelos específicos
- **Parámetros**: Configuración específica del modelo (temperature, max_tokens, etc.)
- **Rendimiento**: Tracking de rendimiento por modelo

### 2. Estructura de Versionado

#### Prompt (Entidad Principal)
```
PRMPROMPTS
├── IDXPROMPT (PK)
├── IDXPROJECT (FK) - OBLIGATORIO
├── IDXAGENT (FK) - OPCIONAL
├── IDXMODEL (FK) - OPCIONAL
├── PRMNAME
├── PRMDESCRIPTION
├── PRMTYPE (SYSTEM, USER, ASSISTANT, FUNCTION)
├── PRMCATEGORY
├── PRMSTATUS (DRAFT, ACTIVE, INACTIVE, DEPRECATED)
├── PRMAPPROVALSTATUS (PENDING, APPROVED, REJECTED)
└── [campos de auditoría]
```

#### PromptVersion (Historial de Versiones)
```
PRMPROMPTVERSIONS
├── IDXPROMPTVERSION (PK)
├── IDPRMPROMPTS0 (FK → PRMPROMPTS)
├── PRMVERSION (semántica: 1.0.0, 1.1.0, 2.0.0)
├── PRMCONTENT (contenido del prompt)
├── PRMPARAMETERS (JSONB: temperature, max_tokens, etc.)
├── PRMCHANGES (changelog)
├── PRMSTATUS
└── [campos de auditoría]
```

**Estrategia de Versionado:**
- **Semántico**: Mayor.Menor.Patch (1.0.0, 1.1.0, 2.0.0)
- **Inmutable**: Las versiones no se modifican, solo se crean nuevas
- **Activo**: Solo una versión puede estar ACTIVE por prompt
- **Historial completo**: Todas las versiones se mantienen para auditoría

### 3. Flujo de Aprobación y Validación

#### PromptApproval (Aprobaciones)
```
PRMPROMPTAPPROVALS
├── IDXPROMPTAPPROVAL (PK)
├── FKIDXPROMPT (FK → PRMPROMPTS)
├── PRMAPPROVALTYPE (NEW_PROMPT, UPDATE, REACTIVATION)
├── PRMAPPROVALSTATUS (PENDING, UNDER_REVIEW, APPROVED, REJECTED)
├── PRMSAFETYCHECK (JSONB)
├── PRMCOMPLIANCECHECK (JSONB)
├── PRMAPPROVERID
└── [campos de auditoría]
```

**Integración con HITL:**
- Prompts de sistemas de **alto riesgo** requieren aprobación humana
- Prompts de sistemas **prohibidos** son bloqueados automáticamente
- Prompts de sistemas de **riesgo limitado** pueden auto-aprobarse con validación

#### PromptValidation (Validaciones)
```
PRMPROMPTVALIDATIONS
├── IDXPROMPTVALIDATION (PK)
├── IDPRMPROMPTS0 (FK → PRMPROMPTS)
├── PRMVALIDATIONTYPE (SAFETY, COMPLIANCE, PERFORMANCE, BIAS)
├── PRMVALIDATIONRESULT (PASSED, FAILED, WARNING)
├── PRMVALIDATIONSCORE (0-10)
├── PRMVALIDATIONDETAILS (JSONB)
├── PRMISSUESFOUND (JSONB)
└── [campos de auditoría]
```

**Tipos de Validación:**
1. **SAFETY**: Validación de seguridad (prompt injection, jailbreak)
2. **COMPLIANCE**: Cumplimiento EU AI Act
3. **PERFORMANCE**: Rendimiento y eficiencia
4. **BIAS**: Detección de sesgos

### 4. Integración con EU AI Act

#### Clasificación por Proyecto
- El **Project** determina la categoría de riesgo del prompt
- Prompts en proyectos **B.1, B.2** requieren validaciones estrictas
- Prompts en proyectos **C, D** tienen validaciones estándar
- Prompts en proyectos **Prohibidos** no pueden ser aprobados

#### Trazabilidad
- Cada prompt debe tener:
  - **Project** asociado (obligatorio)
  - **Historial completo** de versiones
  - **Registro de aprobaciones**
  - **Validaciones realizadas**
  - **Uso en producción** (si aplica)

#### Documentación Técnica
- Los prompts deben documentarse en **AIActTechnicalDocumentation**
- Deben incluirse en la **ConformityDeclaration** del proyecto
- Deben estar disponibles para auditorías

### 5. Casos de Uso

#### Caso 1: Prompt para Agente en Proyecto de Alto Riesgo
```
1. Crear Prompt → Asociar a Project (B.1)
2. Crear PromptVersion (1.0.0) → Estado: DRAFT
3. Ejecutar Validaciones (SAFETY, COMPLIANCE, BIAS)
4. Crear PromptApproval → Requiere aprobación HITL
5. Si aprobado → Activar versión → Estado: ACTIVE
6. Agente puede usar el prompt
```

#### Caso 2: Actualización de Prompt
```
1. Crear nueva PromptVersion (1.1.0) del Prompt existente
2. Comparar con versión anterior (diff)
3. Ejecutar Validaciones incrementales
4. Si cambios significativos → Nueva aprobación HITL
5. Si cambios menores → Auto-aprobación con validación
6. Activar nueva versión, desactivar anterior
```

#### Caso 3: Prompt Compartido entre Múltiples Agentes
```
1. Prompt asociado a Project (sin Agent específico)
2. Múltiples Agent pueden referenciar el mismo Prompt
3. Versionado centralizado
4. Validaciones aplicadas a todos los usos
```

### 6. Recomendaciones de Implementación

#### Modificaciones a Entidades JPA

**Prompt.java:**
```java
// Agregar relaciones
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "IDXPROJECT", nullable = false)
private Project project;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "IDXAGENT", nullable = true)
private Agent agent;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "IDXMODEL", nullable = true)
private Model model;

// Agregar índices
@Table(
    name = "PRMPROMPTS",
    indexes = {
        @Index(name = "idx_prm_project", columnList = "IDXPROJECT"),
        @Index(name = "idx_prm_agent", columnList = "IDXAGENT"),
        @Index(name = "idx_prm_model", columnList = "IDXMODEL"),
        @Index(name = "idx_prm_status", columnList = "PRMSTATUS"),
        @Index(name = "idx_prm_approval", columnList = "PRMAPPROVALSTATUS")
    }
)
```

**PromptVersion.java:**
```java
// Mantener relación existente con Prompt
// Agregar campo para versión activa
@Column(name = "ISACTIVE", nullable = false)
private Boolean isactive = false;

// Agregar constraint: solo una versión activa por prompt
// (implementar en lógica de negocio o constraint de BD)
```

#### Reglas de Negocio

1. **Creación de Prompt:**
   - Debe tener Project asociado (obligatorio)
   - Primera versión se crea automáticamente (1.0.0)
   - Estado inicial: DRAFT

2. **Activación de Versión:**
   - Solo una versión puede estar ACTIVE por prompt
   - Al activar nueva versión, desactivar anterior
   - Requiere aprobación si el proyecto es de alto riesgo

3. **Validaciones:**
   - Automáticas antes de aprobación
   - Obligatorias para proyectos B.1, B.2
   - Opcionales para proyectos C, D

4. **Aprobaciones:**
   - HITL obligatorio para proyectos de alto riesgo
   - Auto-aprobación para proyectos de bajo riesgo (con validación exitosa)
   - Bloqueo automático para proyectos prohibidos

### 7. Vistas y Consultas Recomendadas

```sql
-- Vista: Prompts por Proyecto
CREATE VIEW v_prompts_by_project AS
SELECT
    p.idxproject,
    pr.idxprompt,
    pr.prmname,
    pr.prmstatus,
    COUNT(DISTINCT pv.idxpromptversion) as total_versions,
    COUNT(DISTINCT pvl.idxpromptvalidation) as total_validations
FROM prmprompts pr
JOIN prjprojects p ON pr.idxproject = p.idxproject
LEFT JOIN prmpromptversions pv ON pv.idprmprompts0 = pr.idxprompt
LEFT JOIN prmpromptvalidations pvl ON pvl.idprmprompts0 = pr.idxprompt
GROUP BY p.idxproject, pr.idxprompt, pr.prmname, pr.prmstatus;

-- Vista: Versión Activa de cada Prompt
CREATE VIEW v_active_prompt_versions AS
SELECT
    pr.idxprompt,
    pr.idxproject,
    pv.idxpromptversion,
    pv.prmversion,
    pv.prmcontent,
    pv.prmcreatedat
FROM prmprompts pr
JOIN prmpromptversions pv ON pv.idprmprompts0 = pr.idxprompt
WHERE pv.isactive = true;
```

### 8. Consideraciones de AI OS

- **Despliegue**: Los prompts deben estar disponibles en AI OS para uso en producción
- **Sincronización**: Cambios en prompts deben sincronizarse con AI OS
- **Monitoreo**: AI OS debe reportar uso y rendimiento de prompts
- **Rollback**: Capacidad de revertir a versión anterior desde AI OS

### 9. Integración con HITL

- **Decisiones críticas**: Prompts que afectan decisiones críticas requieren supervisión HITL
- **Alertas**: Notificaciones cuando un prompt requiere revisión humana
- **Workflow**: Integración con BPMN para flujos de aprobación
- **Auditoría**: Registro de todas las decisiones HITL relacionadas con prompts

## Resumen

**Estructura propuesta:**
```
Project (núcleo)
  ├── Prompt (obligatorio)
  │   ├── PromptVersion (historial inmutable)
  │   ├── PromptValidation (validaciones)
  │   └── PromptApproval (aprobaciones HITL)
  ├── Agent (opcional, puede usar múltiples prompts)
  └── Model (opcional, prompts optimizados)
```

**Principios:**
1. **Project como núcleo**: Todo prompt debe estar asociado a un proyecto
2. **Versionado inmutable**: Historial completo para auditoría
3. **Aprobación basada en riesgo**: HITL para alto riesgo, auto-aprobación para bajo riesgo
4. **Validación continua**: Validaciones automáticas antes de aprobación
5. **Trazabilidad completa**: Registro de todos los cambios y decisiones
