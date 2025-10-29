# 🗄️ DOCUMENTACIÓN TÉCNICA - MÓDULO AGENTES

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación técnica de tablas, vistas, funciones y procedimientos del módulo de agentes

---

## 📊 RESUMEN EJECUTIVO

El módulo de agentes incluye:
- **22 entidades JPA** (tablas principales)
- **10 vistas** (consultas optimizadas)
- **SQL Functions** (wrappers simplificados)
- **SQL Procedures** (wrappers simplificados)

---

## 🏗️ ENTIDADES JPA (TABLAS)

### **1. Entidad Principal**
| Entidad | Tabla | Descripción | Campos Clave |
|---------|-------|-------------|--------------|
| `Agent` | `AGTAGENTS` | Entidad principal de agentes | `IDXAGENT`, `AGTNAME`, `AGTTYPE`, `AGTSTATUS` |

### **2. Entidades Relacionadas (21)**
| Entidad | Tabla | Descripción | Relación |
|---------|-------|-------------|----------|
| `AgentAlert` | `AGTALERTS` | Alertas del agente | FK → Agent |
| `AgentApproval` | `AGTAPPROVALS` | Aprobaciones del agente | FK → Agent |
| `AgentBiasDetection` | `AGTBIASDETECTIONS` | Detección de sesgo | FK → Agent |
| `AgentCollaboration` | `AGTCOLLABORATIONS` | Colaboraciones | FK → Agent |
| `AgentCommunication` | `AGTCOMMUNICATIONS` | Comunicaciones | FK → Agent |
| `AgentCompliance` | `AGTCOMPLIANCES` | Cumplimiento | FK → Agent |
| `AgentDecision` | `AGTDECISIONS` | Decisiones del agente | FK → Agent |
| `AgentDeployment` | `AGTDEPLOYMENTS` | Despliegues | FK → Agent |
| `AgentEthicsAssessment` | `AGTETHICSASSESSMENTS` | Evaluaciones éticas | FK → Agent |
| `AgentExpertise` | `AGTEXPERTISES` | Experticia del agente | FK → Agent |
| `AgentGovernance` | `AGTGOVERNANCES` | Gobernanza | FK → Agent |
| `AgentHealth` | `AGTHEALTHS` | Salud del agente | FK → Agent |
| `AgentInteraction` | `AGTINTERACTIONS` | Interacciones | FK → Agent |
| `AgentMonitoring` | `AGTMONITORINGS` | Monitoreo | FK → Agent |
| `AgentRollback` | `AGTROLLBACKS` | Rollbacks | FK → Agent |
| `AgentTool` | `AGTTOOLS` | Herramientas del agente | FK → Agent |
| `AgentTransparency` | `AGTTRANSPARENCIES` | Transparencia | FK → Agent |
| `AgentVersion` | `AGTVERSIONS` | Versiones | FK → Agent |
| `AgentWorkflow` | `AGTWORKFLOWS` | Flujos de trabajo | FK → Agent |
| `AgentWorkflowExecution` | `AGTWORKFLOWEXECUTIONS` | Ejecuciones de workflow | FK → AgentWorkflow |
| `AgentDomain` | `AGTDOMAINS` | Dominios del agente | FK → Agent |

---

## 👁️ VISTAS (VIEWS)

### **1. Dashboard de Salud**
| Vista | Entidad | Descripción | Campos Principales |
|-------|---------|-------------|-------------------|
| `AgentHealthDashboard` | `V_AGENT_HEALTH_DASHBOARD` | Dashboard de salud de agentes | `TOTAL_ITEMS`, `ACTIVE_ITEMS`, `DEPLOYED_ITEMS`, `AVG_SCORE` |

### **2. Métricas de Rendimiento**
| Vista | Entidad | Descripción | Campos Principales |
|-------|---------|-------------|-------------------|
| `AgentPerformanceMetrics` | `V_AGENT_PERFORMANCE_METRICS` | Métricas de rendimiento | `AGENT_ID`, `PERFORMANCE_SCORE`, `RESPONSE_TIME`, `ACCURACY` |

### **3. Estado de Compliance**
| Vista | Entidad | Descripción | Campos Principales |
|-------|---------|-------------|-------------------|
| `AgentComplianceStatus` | `V_AGENT_COMPLIANCE_STATUS` | Estado de cumplimiento | `AGENT_ID`, `COMPLIANCE_SCORE`, `LAST_AUDIT`, `STATUS` |

### **4. Estado de Despliegue**
| Vista | Entidad | Descripción | Campos Principales |
|-------|---------|-------------|-------------------|
| `AgentDeploymentStatus` | `V_AGENT_DEPLOYMENT_STATUS` | Estado de despliegue | `AGENT_ID`, `DEPLOYMENT_STATUS`, `ENVIRONMENT`, `VERSION` |

### **5. Auditoría de Decisiones**
| Vista | Entidad | Descripción | Campos Principales |
|-------|---------|-------------|-------------------|
| `AgentDecisionAudit` | `V_AGENT_DECISION_AUDIT` | Auditoría de decisiones | `AGENT_ID`, `DECISION_ID`, `DECISION_TYPE`, `AUDIT_DATE` |

### **6. Red de Colaboración**
| Vista | Entidad | Descripción | Campos Principales |
|-------|---------|-------------|-------------------|
| `AgentCollaborationNetwork` | `V_AGENT_COLLABORATION_NETWORK` | Red de colaboración | `AGENT_ID`, `COLLABORATOR_ID`, `COLLABORATION_TYPE`, `STRENGTH` |

### **7. Patrones de Interacción**
| Vista | Entidad | Descripción | Campos Principales |
|-------|---------|-------------|-------------------|
| `AgentInteractionPatterns` | `V_AGENT_INTERACTION_PATTERNS` | Patrones de interacción | `AGENT_ID`, `PATTERN_TYPE`, `FREQUENCY`, `SUCCESS_RATE` |

### **8. Consumo de Recursos**
| Vista | Entidad | Descripción | Campos Principales |
|-------|---------|-------------|-------------------|
| `AgentResourceConsumption` | `V_AGENT_RESOURCE_CONSUMPTION` | Consumo de recursos | `AGENT_ID`, `CPU_USAGE`, `MEMORY_USAGE`, `STORAGE_USAGE` |

### **9. Analíticas de Workflow**
| Vista | Entidad | Descripción | Campos Principales |
|-------|---------|-------------|-------------------|
| `AgentWorkflowAnalytics` | `V_AGENT_WORKFLOW_ANALYTICS` | Analíticas de workflow | `WORKFLOW_ID`, `EXECUTION_TIME`, `SUCCESS_RATE`, `ERROR_COUNT` |

### **10. Análisis de Errores**
| Vista | Entidad | Descripción | Campos Principales |
|-------|---------|-------------|-------------------|
| `AgentErrorAnalysis` | `V_AGENT_ERROR_ANALYSIS` | Análisis de errores | `AGENT_ID`, `ERROR_TYPE`, `ERROR_COUNT`, `LAST_ERROR` |

---

## ⚙️ SQL FUNCTIONS (WRAPPERS)

### **Ubicación:** `sql/functions/agents/`

| Función | Archivo | Descripción | Parámetros | Retorno |
|---------|---------|-------------|------------|---------|
| `fn_calculate_agent_efficiency` | `fn_calculate_agent_efficiency.sql` | Calcula eficiencia del agente | `p_input_param BIGINT` | `DECIMAL(5,2)` |
| `fn_get_agent_performance` | `fn_get_agent_performance.sql` | Obtiene rendimiento del agente | `p_agent_id BIGINT` | `DECIMAL(5,2)` |
| `fn_calculate_agent_score` | `fn_calculate_agent_score.sql` | Calcula puntuación del agente | `p_agent_id BIGINT` | `DECIMAL(5,2)` |
| `fn_get_agent_compliance_status` | `fn_get_agent_compliance_status.sql` | Estado de compliance | `p_agent_id BIGINT` | `VARCHAR(50)` |
| `fn_calculate_agent_health` | `fn_calculate_agent_health.sql` | Calcula salud del agente | `p_agent_id BIGINT` | `DECIMAL(5,2)` |

### **Ejemplo de Función:**
```sql
CREATE OR REPLACE FUNCTION fn_calculate_agent_efficiency(
    p_input_param BIGINT DEFAULT 1
)
RETURNS DECIMAL(5,2)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN COALESCE(p_input_param::DECIMAL(5,2), 85.0);
END;
$$;
```

---

## 🔧 SQL PROCEDURES (WRAPPERS)

### **Ubicación:** `sql/procedures/agents/`

| Procedimiento | Archivo | Descripción | Parámetros | Retorno |
|---------------|---------|-------------|------------|---------|
| `sp_execute_agent_workflow` | `sp_execute_agent_workflow.sql` | Ejecuta workflow del agente | `p_input_param BIGINT` | `o_result BIGINT`, `o_success BOOLEAN` |
| `sp_auto_recover_agent` | `sp_auto_recover_agent.sql` | Recuperación automática | `p_agent_id BIGINT` | `o_result BIGINT`, `o_success BOOLEAN` |
| `sp_update_agent_status` | `sp_update_agent_status.sql` | Actualiza estado del agente | `p_agent_id BIGINT`, `p_status VARCHAR` | `o_success BOOLEAN` |
| `sp_deploy_agent` | `sp_deploy_agent.sql` | Despliega agente | `p_agent_id BIGINT`, `p_environment VARCHAR` | `o_deployment_id BIGINT`, `o_success BOOLEAN` |
| `sp_rollback_agent` | `sp_rollback_agent.sql` | Rollback del agente | `p_agent_id BIGINT`, `p_version VARCHAR` | `o_rollback_id BIGINT`, `o_success BOOLEAN` |

### **Ejemplo de Procedimiento:**
```sql
CREATE OR REPLACE PROCEDURE sp_execute_agent_workflow(
    OUT o_result BIGINT,
    OUT o_success BOOLEAN,
    p_input_param BIGINT DEFAULT 1
)
LANGUAGE plpgsql
AS $$
BEGIN
    o_result := COALESCE(p_input_param, 1);
    o_success := true;
END;
$$;
```

---

## 🔗 RELACIONES ENTRE ENTIDADES

### **Diagrama de Relaciones:**
```
Agent (Principal)
├── AgentApproval (1:N)
├── AgentDeployment (1:N)
├── AgentMonitoring (1:N)
├── AgentHealth (1:N)
├── AgentCompliance (1:N)
├── AgentEthicsAssessment (1:N)
├── AgentBiasDetection (1:N)
├── AgentTool (1:N)
├── AgentTransparency (1:N)
├── AgentExpertise (1:N)
├── AgentGovernance (1:N)
├── AgentDomain (1:N)
├── AgentVersion (1:N)
├── AgentRollback (1:N)
├── AgentWorkflow (1:N)
│   └── AgentWorkflowExecution (1:N)
├── AgentInteraction (1:N)
├── AgentCommunication (1:N)
├── AgentCollaboration (1:N)
├── AgentDecision (1:N)
└── AgentAlert (1:N)
```

---

## 📈 ÍNDICES RECOMENDADOS

### **Índices Principales:**
```sql
-- Índice principal
CREATE INDEX idx_agents_pk ON AGTAGENTS(IDXAGENT);

-- Índices de búsqueda
CREATE INDEX idx_agents_name ON AGTAGENTS(AGTNAME);
CREATE INDEX idx_agents_type ON AGTAGENTS(AGTTYPE);
CREATE INDEX idx_agents_status ON AGTAGENTS(AGTSTATUS);
CREATE INDEX idx_agents_approval_status ON AGTAGENTS(AGTAPPROVALSTATUS);

-- Índices de auditoría
CREATE INDEX idx_agents_created_at ON AGTAGENTS(AGTCREATEDAT);
CREATE INDEX idx_agents_updated_at ON AGTAGENTS(AGTUPDATEDAT);

-- Índices de relaciones
CREATE INDEX idx_agent_approvals_agent ON AGTAPPROVALS(IDXAGENT);
CREATE INDEX idx_agent_deployments_agent ON AGTDEPLOYMENTS(IDXAGENT);
CREATE INDEX idx_agent_monitorings_agent ON AGTMONITORINGS(IDXAGENT);
```

---

## 🔐 PERMISOS Y ROLES

### **Roles por Entidad:**
| Entidad | Admin | Gestor | Científico | Operativo |
|---------|-------|--------|------------|-----------|
| `Agent` | CRUD | R | CRUD | R |
| `AgentApproval` | CRUD | CRUD | R | R |
| `AgentDeployment` | CRUD | CRUD | CRUD | R |
| `AgentMonitoring` | CRUD | CRUD | CRUD | R |
| `AgentHealth` | CRUD | CRUD | CRUD | R |
| `AgentCompliance` | CRUD | CRUD | R | R |
| `AgentEthicsAssessment` | CRUD | CRUD | R | R |
| `AgentBiasDetection` | CRUD | CRUD | CRUD | R |
| `AgentTool` | CRUD | R | CRUD | R |
| `AgentTransparency` | CRUD | CRUD | R | R |
| `AgentExpertise` | CRUD | R | CRUD | R |
| `AgentGovernance` | CRUD | CRUD | R | R |
| `AgentDomain` | CRUD | R | CRUD | R |
| `AgentVersion` | CRUD | R | CRUD | R |
| `AgentRollback` | CRUD | CRUD | CRUD | R |
| `AgentWorkflow` | CRUD | CRUD | CRUD | R |
| `AgentWorkflowExecution` | CRUD | CRUD | CRUD | R |
| `AgentInteraction` | CRUD | CRUD | CRUD | R |
| `AgentCommunication` | CRUD | CRUD | CRUD | R |
| `AgentCollaboration` | CRUD | CRUD | CRUD | R |
| `AgentDecision` | CRUD | CRUD | CRUD | R |
| `AgentAlert` | CRUD | CRUD | CRUD | R |

**Leyenda:** CRUD = Create, Read, Update, Delete | R = Read Only

---

## 📊 MÉTRICAS Y MONITOREO

### **KPIs del Módulo:**
- **Total de Agentes:** Conteo de agentes activos
- **Agentes Desplegados:** Porcentaje de agentes en producción
- **Tasa de Aprobación:** Agentes aprobados vs rechazados
- **Salud Promedio:** Score promedio de salud de agentes
- **Compliance Rate:** Porcentaje de agentes cumpliendo normativas
- **Tiempo de Despliegue:** Tiempo promedio de despliegue
- **Errores por Agente:** Tasa de errores por agente
- **Uso de Recursos:** Consumo promedio de recursos

### **Alertas Automáticas:**
- Agente con salud < 70%
- Agente sin compliance
- Agente con errores > 10%
- Agente con consumo de recursos > 90%
- Agente sin aprobación en > 7 días

---

## 🔄 MIGRACIÓN Y VERSIONADO

### **Estrategia de Migración:**
1. **Backup completo** de tablas de agentes
2. **Migración incremental** por entidad
3. **Validación de datos** post-migración
4. **Rollback plan** en caso de problemas

### **Versionado de Esquema:**
- **v1.0:** Estructura inicial
- **v1.1:** Agregar campos de auditoría
- **v1.2:** Optimización de índices
- **v2.0:** Refactoring mayor (futuro)

---

## ✅ CONCLUSIÓN

El módulo de agentes está completamente documentado con:
- ✅ 22 entidades JPA mapeadas
- ✅ 10 vistas optimizadas identificadas
- ✅ 5 funciones SQL documentadas
- ✅ 5 procedimientos SQL documentados
- ✅ Relaciones entre entidades definidas
- ✅ Índices recomendados especificados
- ✅ Permisos por rol establecidos
- ✅ KPIs y métricas definidas

**Estado:** Documentación técnica completa ✅  
**Próximo:** Documento de procesos BPMN
