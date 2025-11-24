# AUDITORÍA - SUPERVISIÓN HUMANA (HITL)
**Fecha:** Diciembre 2025  
**Auditor:** Sistema de Gobierno de IA - CodeflowX  
**Base Legal:** EU AI Act Art. 14 (Human oversight), ISO 42001 (Human oversight and AI system management)  
**Artículos Relevantes:** EU AI Act Art. 14 (Human oversight), Art. 15 (Accuracy and robustness), Art. 10 (Quality criteria)

---

## 1. RESUMEN EJECUTIVO

### 1.1 Objetivo de la Auditoría

Evaluar la implementación de Supervisión Humana (Human In The Loop - HITL) obligatoria según EU AI Act Art. 14, verificando:
- Acciones que requieren aprobación humana
- Registro y trazabilidad de aprobaciones
- Evidencia para auditorías externas
- Configuración por proceso y por nivel de riesgo
- Protección contra desactivación de controles críticos

### 1.2 Alcance

**Entidades Evaluadas:**
- `AgentApproval` (Tabla: `AGTAGENTAPPROVALS`)
- `ModelApproval` (Tabla: `MODMODELAPPROVALS`)
- `PromptApproval` (Tabla: `PRMPROMPTAPPROVALS`)
- Sistema BPMN (Workflow Engine)
- Configuración de controles críticos

**Microservicios Evaluados:**
- `codeflowx.govern.workflow.engine` (BPMN Engine)
- Servicios de aprobación y gobierno

---

## 2. ACCIONES QUE REQUIEREN APROBACIÓN HUMANA

### 2.1 Tipos de Aprobación Identificados

#### 2.1.1 Aprobación de Agentes (AgentApproval)

**Entidad:** `com.codeflowx.govern.entity.agents.AgentApproval`  
**Tabla:** `AGTAGENTAPPROVALS`

**Tipos de Aprobación (`AGTAPPROVALTYPE`):**
- `DEPLOYMENT` - Despliegue de agente en producción
- `CONFIGURATION_CHANGE` - Cambios en configuración crítica
- `MODEL_UPDATE` - Actualización de modelo asociado
- `RISK_THRESHOLD_CHANGE` - Modificación de umbrales de riesgo
- `AUTOMATION_ENABLE` - Activación de automatización completa

**Campos de Evaluación:**
```java
@Column(name = "AGTRISKASSESSMENT") // JSONB - Evaluación de riesgo
@Column(name = "AGTCOMPLIANCECHECK") // JSONB - Verificación de cumplimiento
@Column(name = "AGTTECHNICALREVIEW") // JSONB - Revisión técnica
@Column(name = "AGTETHICALREVIEW") // JSONB - Revisión ética
```

**Niveles de Aprobación (`AGTAPPROVALLEVEL`):**
- `AUTOMATIC` - Aprobación automática (solo para cambios de bajo riesgo)
- `SINGLE_APPROVER` - Un aprobador requerido
- `DUAL_APPROVAL` - Dos aprobadores independientes
- `COMMITTEE` - Requiere comité de aprobación

#### 2.1.2 Aprobación de Modelos (ModelApproval)

**Entidad:** `com.codeflowx.govern.entity.models.ModelApproval`  
**Tabla:** `MODMODELAPPROVALS`

**Tipos de Aprobación (`MODAPPROVALTYPE`):**
- `NEW_MODEL` - Nuevo modelo de IA
- `VERSION_UPDATE` - Actualización de versión
- `REDEPLOYMENT` - Redespliegue en producción

**Entornos Requeridos (`MODTARGETENVIRONMENT`):**
- `DEVELOPMENT` - No requiere aprobación
- `STAGING` - Requiere aprobación técnica
- `PRODUCTION` - Requiere aprobación completa (técnica + ética + cumplimiento)

#### 2.1.3 Aprobación de Prompts (PromptApproval)

**Entidad:** `com.codeflowx.govern.entity.prompts.PromptApproval`  
**Tabla:** `PRMPROMPTAPPROVALS`

**Casos que Requieren Aprobación:**
- Prompts que afectan decisiones críticas
- Prompts con contenido sensible (datos personales, decisiones financieras)
- Modificaciones a prompts en producción

### 2.2 Criterios de Activación de HITL

**Según Nivel de Riesgo (EU AI Act Art. 6):**

| Nivel de Riesgo | Aprobación Requerida | Nivel de Aprobación |
|-----------------|----------------------|---------------------|
| **Minimal Risk** | No requerida | Automática |
| **Limited Risk** | Opcional | Single Approver |
| **High Risk** | **OBLIGATORIA** | Dual Approval |
| **Prohibited** | Bloqueada | N/A |

**Según Tipo de Acción:**

| Acción | Aprobación | Justificación |
|--------|------------|---------------|
| Despliegue en Producción | **OBLIGATORIA** | EU AI Act Art. 14.1 |
| Cambio de Umbral de Riesgo | **OBLIGATORIA** | EU AI Act Art. 14.2 |
| Activación de Automatización Completa | **OBLIGATORIA** | EU AI Act Art. 14.3 |
| Modificación de Datos de Entrenamiento | **OBLIGATORIA** | EU AI Act Art. 10 |
| Cambio de Configuración de Sesgo | **OBLIGATORIA** | EU AI Act Art. 15 |

---

## 3. REGISTRO Y TRAZABILIDAD

### 3.1 Estructura de Registro

#### 3.1.1 Campos de Auditoría Obligatorios

**En todas las entidades de aprobación:**

```java
@Column(name = "AGTCREATEDBY") // Usuario que solicita
@Column(name = "AGTCREATEDAT") // Timestamp de solicitud
@Column(name = "AGTAPPROVERID") // ID del aprobador
@Column(name = "AGTAPPROVERNAME") // Nombre del aprobador
@Column(name = "AGTAPPROVERROLE") // Rol del aprobador
@Column(name = "AGTAPPROVEDAT") // Timestamp de aprobación
@Column(name = "AGTUPDATEDBY") // Usuario que modifica
@Column(name = "AGTUPDATEDAT") // Timestamp de modificación
```

#### 3.1.2 Información de Contexto Registrada

**Solicitud:**
- `AGTREQUESTREASON` (CLOB) - Razón de la solicitud
- `AGTREQUESTDETAILS` (JSONB) - Detalles técnicos de la solicitud
- `AGTAPPROVALCRITERIA` (JSONB) - Criterios aplicados para evaluación

**Evaluación:**
- `AGTRISKASSESSMENT` (JSONB) - Evaluación de riesgo estructurada
- `AGTCOMPLIANCECHECK` (JSONB) - Verificación de cumplimiento normativo
- `AGTTECHNICALREVIEW` (JSONB) - Revisión técnica detallada
- `AGTETHICALREVIEW` (JSONB) - Revisión ética

**Decisión:**
- `AGTAPPROVALSTATUS` - Estado: PENDING, APPROVED, REJECTED, CONDITIONAL
- `AGTAPPROVALNOTES` (CLOB) - Notas del aprobador
- `AGTCONDITIONS` (JSONB) - Condiciones para aprobación condicional
- `AGTREJECTIONREASON` (CLOB) - Razón de rechazo (si aplica)

**Escalación:**
- `AGTESCALATEDAT` - Timestamp de escalación
- `AGTESCALATEDTO` - Usuario/rol al que se escaló
- `AGTESCALATIONREASON` (CLOB) - Razón de escalación

### 3.2 Integridad de Registros

**Garantías:**
- Campos `@NotNull` y `@NotBlank` en campos críticos
- Timestamps automáticos (`@Column(nullable = false)`)
- Relación con entidad padre (ej: `Agent`) mediante `@ManyToOne`
- PK autonumérica (`IDXAGENTAPPROVAL`) para trazabilidad única

**Limitaciones Detectadas:**
- ❌ No hay campo de hash/certificado digital para integridad
- ❌ No hay campo de IP/origen de la solicitud
- ⚠️ Campos JSONB no tienen validación de esquema

---

## 4. EVIDENCIA PARA AUDITORÍAS EXTERNAS

### 4.1 Reportes Disponibles

#### 4.1.1 Vista de Analytics

**Entidad:** `com.codeflowx.govern.entity.views.governance.AutoApprovalAnalytics`

**Métricas Disponibles:**
- Tasa de aprobaciones automáticas vs manuales
- Tiempo promedio de aprobación
- Distribución por tipo de aprobación
- Tasa de rechazo por categoría

#### 4.1.2 Consultas SQL para Auditoría

**Aprobaciones Pendientes:**
```sql
SELECT 
    agt.idxagentapproval,
    agt.agtapprovaltype,
    agt.agtapprovalstatus,
    agt.agtcreatedat,
    agt.agtcreatedby,
    agt.agtapproverid,
    agt.agtapprovedat,
    agt.agtapprovalnotes
FROM AGTAGENTAPPROVALS agt
WHERE agt.agtapprovalstatus = 'PENDING'
ORDER BY agt.agtcreatedat DESC;
```

**Aprobaciones por Período:**
```sql
SELECT 
    DATE_TRUNC('month', agt.agtapprovedat) as mes,
    agt.agtapprovaltype,
    COUNT(*) as total,
    COUNT(CASE WHEN agt.agtapprovalstatus = 'APPROVED' THEN 1 END) as aprobadas,
    COUNT(CASE WHEN agt.agtapprovalstatus = 'REJECTED' THEN 1 END) as rechazadas
FROM AGTAGENTAPPROVALS agt
WHERE agt.agtapprovedat >= CURRENT_DATE - INTERVAL '12 months'
GROUP BY mes, agt.agtapprovaltype
ORDER BY mes DESC;
```

**Trazabilidad Completa de Aprobación:**
```sql
SELECT 
    agt.idxagentapproval,
    a.agtname as agent_name,
    agt.agtapprovaltype,
    agt.agtapprovalstatus,
    agt.agtcreatedby as solicitado_por,
    agt.agtcreatedat as fecha_solicitud,
    agt.agtapprovername as aprobado_por,
    agt.agtapprovedat as fecha_aprobacion,
    agt.agtapprovalnotes,
    agt.agtrejectionreason,
    agt.agtriskassessment,
    agt.agtcompliancecheck
FROM AGTAGENTAPPROVALS agt
JOIN AGTAGENTS a ON agt.idxagents0 = a.idxagent
WHERE agt.agtapprovedat IS NOT NULL
ORDER BY agt.agtapprovedat DESC;
```

### 4.2 Documentación de Evidencia

**Elementos que Deben Estar Disponibles:**

1. **Registro de Solicitud:**
   - Usuario solicitante
   - Timestamp
   - Razón y detalles técnicos
   - Contexto de riesgo

2. **Proceso de Evaluación:**
   - Evaluación de riesgo (JSONB estructurado)
   - Verificación de cumplimiento
   - Revisión técnica
   - Revisión ética (si aplica)

3. **Decisión:**
   - Aprobador identificado
   - Timestamp de decisión
   - Justificación (notas)
   - Condiciones (si aprobación condicional)

4. **Historial de Cambios:**
   - Campos `AGTUPDATEDBY` y `AGTUPDATEDAT`
   - Trazabilidad de modificaciones

### 4.3 Gaps en Evidencia

**Faltantes Identificados:**
- ❌ No hay exportación automática a formato auditoría (PDF/XML)
- ❌ No hay firma digital de aprobaciones
- ❌ No hay registro de intentos de bypass de controles
- ⚠️ Campos JSONB requieren parseo manual para auditoría

---

## 5. CONFIGURACIÓN HITL POR PROCESO Y POR RIESGO

### 5.1 Configuración por Proceso (BPMN)

#### 5.1.1 Integración con BPMN Engine

**Microservicio:** `codeflowx.govern.workflow.engine`

**Procesos BPMN con HITL:**
- `compliance-monitoring-v1.bpmn` - Monitoreo de cumplimiento
- `education/edu-consent-hub.bpmn20.xml` - Consentimiento educativo
- Procesos de aprobación de modelos y agentes

**Tareas de Aprobación Humana:**
```xml
<userTask id="humanApprovalTask" name="Human Approval Required">
  <extensionElements>
    <flowable:assignee>${approverId}</flowable:assignee>
    <flowable:formProperty id="approvalDecision" type="enum">
      <flowable:value id="APPROVED">Approved</flowable:value>
      <flowable:value id="REJECTED">Rejected</flowable:value>
    </flowable:formProperty>
  </extensionElements>
</userTask>
```

**Configuración de HITL en BPMN:**
- Tareas de usuario (`userTask`) para aprobación manual
- Gateways condicionales basados en nivel de riesgo
- Timers para escalación automática
- Servicios de tarea para integración con entidades de aprobación

#### 5.1.2 Configuración por Tipo de Proceso

**Procesos de Alto Riesgo:**
- Requieren aprobación dual
- Timeout de 48 horas para respuesta
- Escalación automática a comité

**Procesos de Riesgo Medio:**
- Requieren aprobación simple
- Timeout de 72 horas
- Escalación a supervisor

**Procesos de Bajo Riesgo:**
- Aprobación automática con revisión posterior
- Notificación al aprobador
- Revisión mensual de aprobaciones automáticas

### 5.2 Configuración por Nivel de Riesgo

#### 5.2.1 Matriz de Configuración HITL

| Nivel de Riesgo | Aprobación Requerida | Nivel | Timeout | Escalación |
|-----------------|----------------------|-------|---------|------------|
| **CRITICAL** | OBLIGATORIA | Committee | 24h | Automática a CISO |
| **HIGH** | OBLIGATORIA | Dual Approval | 48h | Automática a Director |
| **MEDIUM** | OBLIGATORIA | Single Approver | 72h | Manual a Supervisor |
| **LOW** | Opcional | Auto con notificación | N/A | Revisión mensual |

#### 5.2.2 Configuración en Código

**Campo de Evaluación de Riesgo:**
```java
@Column(name = "AGTRISKASSESSMENT", nullable = true)
@Field(type = "JSONB")
private String agtriskassessment;
```

**Estructura JSON Esperada:**
```json
{
  "riskLevel": "HIGH|MEDIUM|LOW|CRITICAL",
  "riskScore": 0.0-1.0,
  "riskFactors": [
    {
      "factor": "DATA_SENSITIVITY",
      "severity": "HIGH",
      "description": "Procesa datos personales sensibles"
    }
  ],
  "requiresHitl": true,
  "hitlLevel": "DUAL_APPROVAL",
  "hitlTimeout": "PT48H"
}
```

#### 5.2.3 Configuración Dinámica

**Ubicación de Configuración:**
- Variables de entorno por microservicio
- Tabla de configuración de gobierno (pendiente de implementación)
- Reglas Drools para evaluación de riesgo

**Ejemplo de Regla Drools:**
```drl
rule "Require Dual Approval for High Risk"
when
    $approval : AgentApproval(riskAssessment.riskLevel == "HIGH")
then
    $approval.setApprovalLevel("DUAL_APPROVAL");
    $approval.setRequiresHitl(true);
end
```

### 5.3 Configuración por Partner/Tenant

**Limitación Actual:**
- ⚠️ No hay configuración específica por partner/tenant
- ⚠️ Todos los partners comparten la misma política de HITL
- ❌ No hay capacidad de personalización por partner

**Recomendación:**
- Implementar tabla `GOVHITLCONFIG` con configuración por tenant
- Permitir override de configuración global (con restricciones)

---

## 6. PROTECCIÓN CONTRA DESACTIVACIÓN DE CONTROLES CRÍTICOS

### 6.1 Controles Críticos Identificados

**Según EU AI Act Art. 14:**

1. **Supervisión Humana Obligatoria** - No puede desactivarse
2. **Aprobación de Despliegue en Producción** - Crítico
3. **Aprobación de Cambios de Modelo** - Crítico
4. **Aprobación de Modificación de Datos de Entrenamiento** - Crítico
5. **Aprobación de Cambios de Umbral de Riesgo** - Crítico

### 6.2 Mecanismos de Protección Actuales

#### 6.2.1 Validaciones en Código

**En entidades de aprobación:**
- Campos `@NotNull` y `@NotBlank` en campos críticos
- Validación de estado antes de permitir cambios

**Limitación:**
- ❌ No hay validación a nivel de base de datos (CHECK constraints)
- ❌ No hay triggers de protección
- ⚠️ Validación solo a nivel de aplicación

#### 6.2.2 Control de Acceso

**Roles Requeridos:**
- `GOVERNANCE_ADMIN` - Para modificar configuración de HITL
- `APPROVER` - Para aprobar solicitudes
- `AUDITOR` - Solo lectura

**Limitación:**
- ⚠️ No hay verificación de que un partner no pueda modificar controles críticos
- ❌ No hay registro de intentos de modificación de controles críticos

### 6.3 Escenario: Partner Desactiva Control Crítico

#### 6.3.1 Análisis de Riesgo

**Escenario:**
Un partner con rol `GOVERNANCE_ADMIN` intenta desactivar HITL para un proceso de alto riesgo.

**Protecciones Actuales:**
- ⚠️ Validación a nivel de aplicación (puede ser bypassed)
- ❌ No hay protección a nivel de base de datos
- ❌ No hay alertas automáticas
- ❌ No hay auditoría de intentos de modificación

**Impacto:**
- 🔴 **CRÍTICO** - Violación de EU AI Act Art. 14
- 🔴 **CRÍTICO** - Riesgo legal y regulatorio
- 🔴 **CRÍTICO** - Pérdida de trazabilidad

#### 6.3.2 Recomendaciones de Protección

**Nivel 1: Validación de Negocio**
- Implementar reglas de negocio que impidan desactivar HITL para procesos de alto riesgo
- Validar contra matriz de riesgo antes de permitir cambios

**Nivel 2: Protección de Base de Datos**
- Triggers de PostgreSQL que validen cambios críticos
- CHECK constraints en tablas de configuración
- Foreign keys con restricciones

**Nivel 3: Auditoría y Alertas**
- Registro de todos los intentos de modificación de controles críticos
- Alertas automáticas a CISO/Compliance cuando se detecta intento
- Bloqueo automático de cambios no autorizados

**Nivel 4: Separación de Responsabilidades**
- Requerir aprobación de múltiples roles para cambios críticos
- Implementar "four-eyes principle" para configuración de HITL
- Bloqueo de auto-aprobación para cambios de controles

---

## 7. CUMPLIMIENTO CON EU AI ACT ART. 14

### 7.1 Requisitos del Artículo 14

**Art. 14.1 - Supervisión Humana:**
- ✅ Sistema permite supervisión humana
- ⚠️ No está garantizado que siempre esté activa
- ❌ Falta validación obligatoria para sistemas de alto riesgo

**Art. 14.2 - Capacidad de Intervención:**
- ✅ Sistema permite intervención humana (aprobación/rechazo)
- ✅ Registro de intervenciones
- ⚠️ No hay garantía de tiempo de respuesta

**Art. 14.3 - Capacidad de Desactivación:**
- ✅ Sistema permite desactivación/parada
- ❌ No hay mecanismo automático de desactivación por riesgo
- ⚠️ Desactivación requiere aprobación (puede ser lenta)

**Art. 14.4 - Transparencia:**
- ✅ Registro de decisiones humanas
- ⚠️ No hay explicación automática de por qué se requiere HITL
- ❌ Falta documentación de criterios de supervisión

### 7.2 Gaps de Cumplimiento

| Requisito | Estado | Gap |
|-----------|--------|-----|
| Supervisión humana obligatoria | ⚠️ Parcial | Falta validación obligatoria |
| Registro de intervenciones | ✅ Cumple | - |
| Capacidad de desactivación | ✅ Cumple | - |
| Transparencia de criterios | ❌ No cumple | Falta documentación |
| Protección contra bypass | ❌ No cumple | Falta protección robusta |

---

## 8. CONCLUSIONES

### 8.1 Fortalezas Identificadas

1. ✅ **Estructura de Entidades Robusta:**
   - Entidades de aprobación bien diseñadas
   - Campos completos para trazabilidad
   - Soporte para múltiples niveles de aprobación

2. ✅ **Integración con BPMN:**
   - Workflows configurables
   - Soporte para tareas de aprobación humana
   - Escalación automática

3. ✅ **Registro Completo:**
   - Timestamps en todas las acciones
   - Información de contexto (JSONB)
   - Historial de cambios

### 8.2 Debilidades Críticas

1. 🔴 **Falta de Protección Robusta:**
   - No hay protección a nivel de BD contra desactivación de controles
   - Validación solo a nivel de aplicación
   - No hay alertas automáticas

2. 🔴 **Falta de Validación Obligatoria:**
   - No está garantizado que HITL esté siempre activa para alto riesgo
   - Falta validación automática contra matriz de riesgo

3. ⚠️ **Gaps en Evidencia:**
   - No hay exportación automática para auditorías
   - No hay firma digital
   - Campos JSONB requieren parseo manual

4. ⚠️ **Configuración Limitada:**
   - No hay configuración por partner/tenant
   - Políticas hardcodeadas en código

### 8.3 Priorización de Mejoras

**Prioridad ALTA (Crítico):**
1. Implementar protección a nivel de BD contra desactivación de controles críticos
2. Validación obligatoria de HITL para sistemas de alto riesgo
3. Alertas automáticas para intentos de modificación de controles

**Prioridad MEDIA:**
4. Exportación automática de evidencia para auditorías
5. Configuración por partner/tenant con restricciones
6. Firma digital de aprobaciones

**Prioridad BAJA:**
7. Mejora de documentación de criterios de supervisión
8. Dashboard de métricas de HITL
9. Integración con sistemas de firma electrónica

---

## 9. ANEXOS

### 9.1 Entidades Relacionadas

- `AgentApproval` - `/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/agents/AgentApproval.java`
- `ModelApproval` - `/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/models/ModelApproval.java`
- `PromptApproval` - `/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/prompts/PromptApproval.java`
- `AutoApprovalAnalytics` - `/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/views/governance/AutoApprovalAnalytics.java`

### 9.2 Referencias Normativas

- **EU AI Act Art. 14** - Human oversight
- **EU AI Act Art. 15** - Accuracy and robustness
- **EU AI Act Art. 10** - Quality criteria for training data
- **ISO 42001** - Human oversight and AI system management

### 9.3 Documentación Relacionada

- `PROMPTS_47_BPMN_ENGINE_MODULE.md` - Configuración de BPMN Engine
- `INDEX_EU_AI_ACT_COMPLIANCE.md` - Índice de cumplimiento EU AI Act
- `EU_AI_ACT_ENTITIES_README.md` - Documentación de entidades

---

**Fin del Informe de Auditoría**







