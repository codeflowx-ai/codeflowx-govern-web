# 🗄️ DOCUMENTACIÓN TÉCNICA - MÓDULO PROMPTS

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación técnica completa de entidades, vistas, funciones y procedimientos del módulo de prompts

---

## 🎯 RESUMEN EJECUTIVO

El módulo de **prompts** está compuesto por **4 entidades JPA principales**, **8 vistas optimizadas**, **4 funciones SQL** y **10 procedimientos SQL**, diseñados para gestionar completamente el ciclo de vida de prompts de IA desde su creación hasta su optimización y monitoreo.

### **Componentes Técnicos:**
- **Entidades JPA:** 4 tablas principales con relaciones complejas
- **Vistas:** 8 vistas optimizadas para consultas frecuentes
- **Funciones:** 4 funciones para cálculos y análisis
- **Procedimientos:** 10 procedimientos para operaciones complejas
- **Índices:** Optimizados para consultas de alto rendimiento
- **Permisos:** Configurados por roles y funcionalidades

---

## 🗄️ ENTIDADES JPA PRINCIPALES

### **1. Prompt (PRMPROMPTS)**

**Propósito:** Entidad principal que almacena la información básica de los prompts de IA.

#### **Campos Principales:**
| Campo | Tipo | Descripción | Restricciones |
|-------|------|-------------|---------------|
| `idxprompt` | LONG | ID único del prompt | PK, Auto-increment |
| `prmname` | VARCHAR(255) | Nombre del prompt | NOT NULL, UNIQUE |
| `prmdescription` | CLOB | Descripción detallada | Opcional |
| `prmtype` | VARCHAR(50) | Tipo de prompt | NOT NULL, Enum |
| `prmcategory` | VARCHAR(100) | Categoría del prompt | Opcional |
| `prmversion` | VARCHAR(50) | Versión actual | NOT NULL |
| `prmstatus` | VARCHAR(50) | Estado del prompt | NOT NULL, Enum |
| `prmcontent` | CLOB | Contenido del prompt | Opcional |
| `prmparameters` | JSONB | Parámetros configurables | Opcional |
| `prmmetadata` | JSONB | Metadatos adicionales | Opcional |
| `prmapprovalstatus` | VARCHAR(50) | Estado de aprobación | Opcional |
| `prmapprovedby` | VARCHAR(255) | Usuario que aprobó | Opcional |
| `prmapprovedat` | TIMESTAMP | Fecha de aprobación | Opcional |
| `prmcreatedby` | VARCHAR(255) | Usuario creador | NOT NULL |
| `prmcreatedat` | TIMESTAMP | Fecha de creación | NOT NULL |
| `prmupdatedby` | VARCHAR(255) | Usuario que actualizó | Opcional |
| `prmupdatedat` | TIMESTAMP | Fecha de actualización | Opcional |

#### **Relaciones:**
- **OneToMany** con `PromptVersion` (subprmpromptversions)
- **OneToMany** con `PromptValidation` (subprmpromptvalidations)
- **OneToMany** con `PromptApproval` (subprmpromptapprovals)

#### **Enums y Valores:**
- **prmtype:** `TEXT_GENERATION`, `TEXT_CLASSIFICATION`, `TEXT_SUMMARIZATION`, `QUESTION_ANSWERING`, `CODE_GENERATION`, `TRANSLATION`
- **prmstatus:** `DRAFT`, `PENDING_APPROVAL`, `APPROVED`, `ACTIVE`, `INACTIVE`, `DEPRECATED`, `ARCHIVED`
- **prmapprovalstatus:** `PENDING`, `UNDER_REVIEW`, `APPROVED`, `REJECTED`, `REQUIRES_CHANGES`

#### **Índices:**
```sql
CREATE INDEX idx_prompt_name ON PRMPROMPTS(prmname);
CREATE INDEX idx_prompt_type ON PRMPROMPTS(prmtype);
CREATE INDEX idx_prompt_status ON PRMPROMPTS(prmstatus);
CREATE INDEX idx_prompt_approval_status ON PRMPROMPTS(prmapprovalstatus);
CREATE INDEX idx_prompt_created_at ON PRMPROMPTS(prmcreatedat);
CREATE INDEX idx_prompt_category ON PRMPROMPTS(prmcategory);
```

---

### **2. PromptVersion (PRMPROMPTVERSIONS)**

**Propósito:** Almacena las diferentes versiones de un prompt, permitiendo versionado y rollback.

#### **Campos Principales:**
| Campo | Tipo | Descripción | Restricciones |
|-------|------|-------------|---------------|
| `idxpromptversion` | LONG | ID único de la versión | PK, Auto-increment |
| `prmversion` | VARCHAR(50) | Número de versión | NOT NULL |
| `prmdescription` | CLOB | Descripción de cambios | Opcional |
| `prmcontent` | CLOB | Contenido de la versión | Opcional |
| `prmparameters` | JSONB | Parámetros de la versión | Opcional |
| `prmchanges` | CLOB | Descripción de cambios | Opcional |
| `prmstatus` | VARCHAR(50) | Estado de la versión | NOT NULL |
| `prmcreatedby` | VARCHAR(255) | Usuario creador | NOT NULL |
| `prmcreatedat` | TIMESTAMP | Fecha de creación | NOT NULL |
| `prmupdatedby` | VARCHAR(255) | Usuario que actualizó | Opcional |
| `prmupdatedat` | TIMESTAMP | Fecha de actualización | Opcional |
| `idprmprompts0` | LONG | FK al prompt padre | NOT NULL |

#### **Relaciones:**
- **ManyToOne** con `Prompt` (prompt)

#### **Enums y Valores:**
- **prmstatus:** `DRAFT`, `PENDING_VALIDATION`, `VALIDATED`, `ACTIVE`, `DEPRECATED`, `ARCHIVED`

#### **Índices:**
```sql
CREATE INDEX idx_prompt_version_prompt ON PRMPROMPTVERSIONS(idprmprompts0);
CREATE INDEX idx_prompt_version_number ON PRMPROMPTVERSIONS(prmversion);
CREATE INDEX idx_prompt_version_status ON PRMPROMPTVERSIONS(prmstatus);
CREATE INDEX idx_prompt_version_created_at ON PRMPROMPTVERSIONS(prmcreatedat);
```

---

### **3. PromptValidation (PRMPROMPTVALIDATIONS)**

**Propósito:** Almacena los resultados de validaciones realizadas sobre prompts.

#### **Campos Principales:**
| Campo | Tipo | Descripción | Restricciones |
|-------|------|-------------|---------------|
| `idxpromptvalidation` | LONG | ID único de la validación | PK, Auto-increment |
| `prmvalidationtype` | VARCHAR(50) | Tipo de validación | NOT NULL |
| `prmvalidationresult` | VARCHAR(50) | Resultado de la validación | NOT NULL |
| `prmvalidationscore` | DECIMAL(5,2) | Score de la validación | Opcional |
| `prmvalidationdetails` | JSONB | Detalles de la validación | Opcional |
| `prmissuesfound` | JSONB | Issues encontrados | Opcional |
| `prmrecommendations` | JSONB | Recomendaciones | Opcional |
| `prmstatus` | VARCHAR(50) | Estado de la validación | NOT NULL |
| `prmvalidatedby` | VARCHAR(255) | Usuario que validó | Opcional |
| `prmvalidatedat` | TIMESTAMP | Fecha de validación | Opcional |
| `prmcreatedby` | VARCHAR(255) | Usuario creador | NOT NULL |
| `prmcreatedat` | TIMESTAMP | Fecha de creación | NOT NULL |
| `prmupdatedby` | VARCHAR(255) | Usuario que actualizó | Opcional |
| `prmupdatedat` | TIMESTAMP | Fecha de actualización | Opcional |
| `idprmprompts0` | LONG | FK al prompt | NOT NULL |

#### **Relaciones:**
- **ManyToOne** con `Prompt` (prompt)

#### **Enums y Valores:**
- **prmvalidationtype:** `SAFETY_CHECK`, `COMPLIANCE_CHECK`, `PERFORMANCE_TEST`, `BIAS_DETECTION`, `QUALITY_ASSURANCE`
- **prmvalidationresult:** `PASS`, `FAIL`, `WARNING`, `REQUIRES_REVIEW`
- **prmstatus:** `PENDING`, `IN_PROGRESS`, `COMPLETED`, `FAILED`, `CANCELLED`

#### **Índices:**
```sql
CREATE INDEX idx_prompt_validation_prompt ON PRMPROMPTVALIDATIONS(idprmprompts0);
CREATE INDEX idx_prompt_validation_type ON PRMPROMPTVALIDATIONS(prmvalidationtype);
CREATE INDEX idx_prompt_validation_result ON PRMPROMPTVALIDATIONS(prmvalidationresult);
CREATE INDEX idx_prompt_validation_status ON PRMPROMPTVALIDATIONS(prmstatus);
CREATE INDEX idx_prompt_validation_score ON PRMPROMPTVALIDATIONS(prmvalidationscore);
```

---

### **4. PromptApproval (PRMPROMPTAPPROVALS)**

**Propósito:** Gestiona el proceso de aprobación de prompts con workflow completo.

#### **Campos Principales:**
| Campo | Tipo | Descripción | Restricciones |
|-------|------|-------------|---------------|
| `idxpromptapproval` | LONG | ID único de la aprobación | PK, Auto-increment |
| `prmapprovaltype` | VARCHAR(50) | Tipo de aprobación | NOT NULL |
| `prmapprovalstatus` | VARCHAR(50) | Estado de la aprobación | NOT NULL |
| `prmrequestreason` | TEXT | Razón de la solicitud | Opcional |
| `prmsafetycheck` | JSONB | Resultado de verificación de seguridad | Opcional |
| `prmcompliancecheck` | JSONB | Resultado de verificación de compliance | Opcional |
| `prmreview` | JSONB | Información de revisión | Opcional |
| `prmapprovalnotes` | TEXT | Notas de aprobación | Opcional |
| `prmrejectionreason` | TEXT | Razón de rechazo | Opcional |
| `prmapproverid` | VARCHAR(255) | ID del aprobador | Opcional |
| `prmapprovername` | VARCHAR(255) | Nombre del aprobador | Opcional |
| `prmapprovedat` | TIMESTAMP | Fecha de aprobación | Opcional |
| `prmcreatedby` | VARCHAR(255) | Usuario creador | NOT NULL |
| `prmcreatedat` | TIMESTAMP | Fecha de creación | NOT NULL |
| `prmupdatedby` | VARCHAR(255) | Usuario que actualizó | Opcional |
| `prmupdatedat` | TIMESTAMP | Fecha de actualización | Opcional |
| `fkidxprompt` | LONG | FK al prompt | NOT NULL |

#### **Relaciones:**
- **ManyToOne** con `Prompt` (prompt)

#### **Enums y Valores:**
- **prmapprovaltype:** `NEW_PROMPT`, `UPDATE`, `REACTIVATION`, `DEPLOYMENT`
- **prmapprovalstatus:** `PENDING`, `UNDER_REVIEW`, `APPROVED`, `REJECTED`, `REQUIRES_CHANGES`

#### **Índices:**
```sql
CREATE INDEX idx_prompt_approval_prompt ON PRMPROMPTAPPROVALS(fkidxprompt);
CREATE INDEX idx_prompt_approval_type ON PRMPROMPTAPPROVALS(prmapprovaltype);
CREATE INDEX idx_prompt_approval_status ON PRMPROMPTAPPROVALS(prmapprovalstatus);
CREATE INDEX idx_prompt_approval_approver ON PRMPROMPTAPPROVALS(prmapproverid);
CREATE INDEX idx_prompt_approval_created_at ON PRMPROMPTAPPROVALS(prmcreatedat);
```

---

## 👁️ VISTAS OPTIMIZADAS

### **1. PromptsOverview (V_PROMPTS_OVERVIEW)**

**Propósito:** Vista consolidada para dashboard principal de prompts.

#### **Campos:**
- `idxprompt`, `prmname`, `prmtype`, `prmcategory`, `prmstatus`, `prmapprovalstatus`
- `prmcreatedat`, `prmcreatedby`, `prmupdatedat`, `prmupdatedby`
- `version_count`, `validation_count`, `approval_count`
- `last_validation_score`, `last_validation_date`

#### **Optimizaciones:**
- Agregaciones precalculadas para conteos
- JOIN optimizado con tablas relacionadas
- Filtros por estado y tipo

### **2. PromptsMetricsSummary (V_PROMPTS_METRICS_SUMMARY)**

**Propósito:** Resumen de métricas de rendimiento de prompts.

#### **Campos:**
- `idxprompt`, `prmname`, `prmtype`
- `total_executions`, `success_rate`, `average_response_time`
- `cost_per_execution`, `total_cost`, `last_execution_date`
- `performance_score`, `quality_score`

### **3. PromptVersionHistory (V_PROMPT_VERSION_HISTORY)**

**Propósito:** Historial completo de versiones de prompts.

#### **Campos:**
- `idxpromptversion`, `idxprompt`, `prmname`, `prmversion`
- `prmdescription`, `prmchanges`, `prmstatus`
- `prmcreatedat`, `prmcreatedby`
- `version_number`, `is_current_version`

### **4. PromptUsageStatistics (V_PROMPT_USAGE_STATISTICS)**

**Propósito:** Estadísticas de uso de prompts.

#### **Campos:**
- `idxprompt`, `prmname`, `prmtype`
- `daily_usage`, `weekly_usage`, `monthly_usage`
- `peak_usage_hour`, `average_session_duration`
- `user_count`, `success_rate`

### **5. PromptTestResults (V_PROMPT_TEST_RESULTS)**

**Propósito:** Resultados de pruebas de prompts.

#### **Campos:**
- `idxpromptvalidation`, `idxprompt`, `prmname`
- `prmvalidationtype`, `prmvalidationresult`, `prmvalidationscore`
- `test_duration`, `test_date`, `test_environment`
- `issues_count`, `recommendations_count`

### **6. PromptPerformanceComparison (V_PROMPT_PERFORMANCE_COMPARISON)**

**Propósito:** Comparación de rendimiento entre versiones.

#### **Campos:**
- `idxprompt`, `prmname`, `version_a`, `version_b`
- `performance_difference`, `accuracy_difference`
- `cost_difference`, `speed_difference`
- `recommendation`

### **7. PromptOptimizationOpportunities (V_PROMPT_OPTIMIZATION_OPPORTUNITIES)**

**Propósito:** Oportunidades de optimización identificadas.

#### **Campos:**
- `idxprompt`, `prmname`, `optimization_type`
- `current_value`, `optimized_value`, `improvement_percentage`
- `effort_level`, `priority`, `estimated_savings`
- `recommendation`, `implementation_notes`

### **8. PromptCostAnalysis (V_PROMPT_COST_ANALYSIS)**

**Propósito:** Análisis detallado de costos de prompts.

#### **Campos:**
- `idxprompt`, `prmname`, `prmtype`
- `total_tokens`, `total_cost`, `cost_per_token`
- `daily_cost`, `monthly_cost`, `yearly_cost`
- `cost_trend`, `optimization_potential`

---

## ⚙️ FUNCIONES SQL

### **1. CalculatePromptEffectiveness**

**Propósito:** Calcula la efectividad de un prompt basado en métricas de rendimiento.

```sql
CREATE OR REPLACE FUNCTION calculate_prompt_effectiveness(
    p_prompt_id BIGINT,
    p_time_period INTEGER DEFAULT 30
) RETURNS DECIMAL(5,2) AS $$
DECLARE
    effectiveness_score DECIMAL(5,2);
BEGIN
    -- Cálculo basado en accuracy, speed, cost y user satisfaction
    SELECT 
        (COALESCE(avg_accuracy, 0) * 0.4 + 
         COALESCE(speed_score, 0) * 0.3 + 
         COALESCE(cost_efficiency, 0) * 0.2 + 
         COALESCE(user_satisfaction, 0) * 0.1) * 100
    INTO effectiveness_score
    FROM (
        -- Subconsulta con métricas agregadas
        SELECT 
            AVG(prmvalidationscore) as avg_accuracy,
            -- Cálculos adicionales...
        FROM PRMPROMPTVALIDATIONS 
        WHERE idprmprompts0 = p_prompt_id
        AND prmcreatedat >= CURRENT_DATE - INTERVAL '1 day' * p_time_period
    ) metrics;
    
    RETURN COALESCE(effectiveness_score, 0);
END;
$$ LANGUAGE plpgsql;
```

### **2. CalculatePromptCost**

**Propósito:** Calcula el costo total de un prompt basado en tokens y pricing.

```sql
CREATE OR REPLACE FUNCTION calculate_prompt_cost(
    p_prompt_id BIGINT,
    p_execution_count INTEGER DEFAULT 1
) RETURNS DECIMAL(10,4) AS $$
DECLARE
    total_cost DECIMAL(10,4);
    token_count INTEGER;
    cost_per_token DECIMAL(8,6);
BEGIN
    -- Obtener conteo de tokens del prompt
    SELECT estimate_prompt_tokens(p_prompt_id) INTO token_count;
    
    -- Obtener costo por token (configurable por tipo de modelo)
    SELECT prmcost_per_token INTO cost_per_token
    FROM PRMPROMPTS p
    JOIN PRMPROMPTMODELS m ON p.prmmodel_id = m.idxpromptmodel
    WHERE p.idxprompt = p_prompt_id;
    
    -- Calcular costo total
    total_cost := token_count * cost_per_token * p_execution_count;
    
    RETURN total_cost;
END;
$$ LANGUAGE plpgsql;
```

### **3. EstimatePromptTokens**

**Propósito:** Estima el número de tokens de un prompt.

```sql
CREATE OR REPLACE FUNCTION estimate_prompt_tokens(
    p_prompt_id BIGINT
) RETURNS INTEGER AS $$
DECLARE
    token_count INTEGER;
    prompt_content TEXT;
BEGIN
    -- Obtener contenido del prompt
    SELECT prmcontent INTO prompt_content
    FROM PRMPROMPTS
    WHERE idxprompt = p_prompt_id;
    
    -- Estimación simple basada en longitud (1 token ≈ 4 caracteres)
    token_count := CEIL(LENGTH(prompt_content) / 4.0);
    
    RETURN COALESCE(token_count, 0);
END;
$$ LANGUAGE plpgsql;
```

### **4. SuggestPromptOptimization**

**Propósito:** Sugiere optimizaciones para un prompt basado en análisis.

```sql
CREATE OR REPLACE FUNCTION suggest_prompt_optimization(
    p_prompt_id BIGINT
) RETURNS JSONB AS $$
DECLARE
    suggestions JSONB;
BEGIN
    suggestions := jsonb_build_object(
        'token_optimization', jsonb_build_object(
            'current_tokens', estimate_prompt_tokens(p_prompt_id),
            'suggested_reduction', '15%',
            'reason', 'Remove redundant phrases'
        ),
        'performance_optimization', jsonb_build_object(
            'current_score', calculate_prompt_effectiveness(p_prompt_id),
            'target_score', 85.0,
            'suggestions', jsonb_build_array(
                'Add more specific instructions',
                'Include examples in prompt'
            )
        ),
        'cost_optimization', jsonb_build_object(
            'current_cost', calculate_prompt_cost(p_prompt_id),
            'potential_savings', '20%',
            'method', 'Token reduction and model optimization'
        )
    );
    
    RETURN suggestions;
END;
$$ LANGUAGE plpgsql;
```

---

## 🔧 PROCEDIMIENTOS SQL

### **1. VersionPrompt**

**Propósito:** Crea una nueva versión de un prompt.

```sql
CREATE OR REPLACE PROCEDURE version_prompt(
    p_prompt_id BIGINT,
    p_new_content TEXT,
    p_changes_description TEXT,
    p_created_by VARCHAR(255)
) AS $$
DECLARE
    v_next_version VARCHAR(50);
    v_current_version VARCHAR(50);
BEGIN
    -- Obtener versión actual
    SELECT prmversion INTO v_current_version
    FROM PRMPROMPTS
    WHERE idxprompt = p_prompt_id;
    
    -- Calcular siguiente versión
    v_next_version := increment_version(v_current_version);
    
    -- Crear nueva versión
    INSERT INTO PRMPROMPTVERSIONS (
        idprmprompts0, prmversion, prmcontent, prmchanges,
        prmstatus, prmcreatedby, prmcreatedat
    ) VALUES (
        p_prompt_id, v_next_version, p_new_content, p_changes_description,
        'DRAFT', p_created_by, CURRENT_TIMESTAMP
    );
    
    -- Actualizar versión actual en prompt principal
    UPDATE PRMPROMPTS 
    SET prmversion = v_next_version,
        prmupdatedby = p_created_by,
        prmupdatedat = CURRENT_TIMESTAMP
    WHERE idxprompt = p_prompt_id;
    
END;
$$ LANGUAGE plpgsql;
```

### **2. ValidatePromptSafety**

**Propósito:** Ejecuta validaciones de seguridad en un prompt.

```sql
CREATE OR REPLACE PROCEDURE validate_prompt_safety(
    p_prompt_id BIGINT,
    p_validation_type VARCHAR(50) DEFAULT 'SAFETY_CHECK'
) AS $$
DECLARE
    v_validation_id BIGINT;
    v_safety_score DECIMAL(5,2);
    v_issues JSONB;
BEGIN
    -- Crear registro de validación
    INSERT INTO PRMPROMPTVALIDATIONS (
        idprmprompts0, prmvalidationtype, prmstatus,
        prmcreatedby, prmcreatedat
    ) VALUES (
        p_prompt_id, p_validation_type, 'IN_PROGRESS',
        'SYSTEM', CURRENT_TIMESTAMP
    ) RETURNING idxpromptvalidation INTO v_validation_id;
    
    -- Ejecutar validaciones de seguridad
    -- (Implementación específica de validaciones)
    
    -- Actualizar resultado
    UPDATE PRMPROMPTVALIDATIONS
    SET prmvalidationresult = 'PASS',
        prmvalidationscore = v_safety_score,
        prmvalidationdetails = jsonb_build_object(
            'safety_checks', v_issues,
            'validation_date', CURRENT_TIMESTAMP
        ),
        prmstatus = 'COMPLETED',
        prmvalidatedat = CURRENT_TIMESTAMP
    WHERE idxpromptvalidation = v_validation_id;
    
END;
$$ LANGUAGE plpgsql;
```

### **3. OptimizePromptTokens**

**Propósito:** Optimiza un prompt para reducir tokens manteniendo calidad.

```sql
CREATE OR REPLACE PROCEDURE optimize_prompt_tokens(
    p_prompt_id BIGINT,
    p_target_reduction DECIMAL(5,2) DEFAULT 15.0
) AS $$
DECLARE
    v_current_content TEXT;
    v_optimized_content TEXT;
    v_current_tokens INTEGER;
    v_optimized_tokens INTEGER;
BEGIN
    -- Obtener contenido actual
    SELECT prmcontent INTO v_current_content
    FROM PRMPROMPTS
    WHERE idxprompt = p_prompt_id;
    
    -- Calcular tokens actuales
    v_current_tokens := estimate_prompt_tokens(p_prompt_id);
    
    -- Aplicar optimizaciones
    v_optimized_content := apply_token_optimizations(v_current_content, p_target_reduction);
    v_optimized_tokens := CEIL(LENGTH(v_optimized_content) / 4.0);
    
    -- Crear nueva versión optimizada
    CALL version_prompt(
        p_prompt_id,
        v_optimized_content,
        'Token optimization: ' || (v_current_tokens - v_optimized_tokens) || ' tokens reduced',
        'SYSTEM'
    );
    
END;
$$ LANGUAGE plpgsql;
```

### **4. LogPromptExecution**

**Propósito:** Registra la ejecución de un prompt para análisis.

```sql
CREATE OR REPLACE PROCEDURE log_prompt_execution(
    p_prompt_id BIGINT,
    p_execution_data JSONB,
    p_user_id VARCHAR(255)
) AS $$
BEGIN
    INSERT INTO PRMPROMPTEXECUTIONS (
        idprmprompts0, prmexecutiondata, prmuserid,
        prmcreatedat, prmcreatedby
    ) VALUES (
        p_prompt_id, p_execution_data, p_user_id,
        CURRENT_TIMESTAMP, p_user_id
    );
    
    -- Actualizar estadísticas de uso
    UPDATE PRMPROMPTS
    SET prmlast_execution = CURRENT_TIMESTAMP,
        prmexecution_count = prmexecution_count + 1
    WHERE idxprompt = p_prompt_id;
    
END;
$$ LANGUAGE plpgsql;
```

### **5. GeneratePromptReport**

**Propósito:** Genera reporte completo de un prompt.

```sql
CREATE OR REPLACE PROCEDURE generate_prompt_report(
    p_prompt_id BIGINT,
    p_report_type VARCHAR(50) DEFAULT 'COMPREHENSIVE'
) AS $$
DECLARE
    v_report_data JSONB;
BEGIN
    v_report_data := jsonb_build_object(
        'prompt_info', (
            SELECT jsonb_build_object(
                'name', prmname,
                'type', prmtype,
                'status', prmstatus,
                'version', prmversion
            )
            FROM PRMPROMPTS
            WHERE idxprompt = p_prompt_id
        ),
        'metrics', (
            SELECT jsonb_build_object(
                'effectiveness', calculate_prompt_effectiveness(p_prompt_id),
                'cost', calculate_prompt_cost(p_prompt_id),
                'tokens', estimate_prompt_tokens(p_prompt_id)
            )
        ),
        'validations', (
            SELECT jsonb_agg(
                jsonb_build_object(
                    'type', prmvalidationtype,
                    'result', prmvalidationresult,
                    'score', prmvalidationscore,
                    'date', prmvalidatedat
                )
            )
            FROM PRMPROMPTVALIDATIONS
            WHERE idprmprompts0 = p_prompt_id
        ),
        'optimizations', suggest_prompt_optimization(p_prompt_id)
    );
    
    -- Guardar reporte
    INSERT INTO PRMPROMPTREPORTS (
        idprmprompts0, prmreporttype, prmreportdata,
        prmcreatedat, prmcreatedby
    ) VALUES (
        p_prompt_id, p_report_type, v_report_data,
        CURRENT_TIMESTAMP, 'SYSTEM'
    );
    
END;
$$ LANGUAGE plpgsql;
```

---

## 🔐 PERMISOS Y SEGURIDAD

### **Roles y Permisos:**

#### **PROMPT_ADMIN:**
- **Lectura:** Todas las tablas
- **Escritura:** Todas las tablas
- **Eliminación:** Todas las tablas
- **Ejecución:** Todos los procedimientos

#### **PROMPT_MANAGER:**
- **Lectura:** Todas las tablas
- **Escritura:** PRMPROMPTS, PRMPROMPTVERSIONS, PRMPROMPTVALIDATIONS
- **Eliminación:** Ninguna
- **Ejecución:** Procedimientos de gestión

#### **PROMPT_ENGINEER:**
- **Lectura:** PRMPROMPTS, PRMPROMPTVERSIONS, PRMPROMPTVALIDATIONS
- **Escritura:** PRMPROMPTVERSIONS, PRMPROMPTVALIDATIONS
- **Eliminación:** Ninguna
- **Ejecución:** Procedimientos de validación

#### **PROMPT_VIEWER:**
- **Lectura:** Solo vistas optimizadas
- **Escritura:** Ninguna
- **Eliminación:** Ninguna
- **Ejecución:** Solo funciones de consulta

### **Políticas de Seguridad:**

#### **Row Level Security (RLS):**
```sql
-- Política para prompts por organización
CREATE POLICY prompt_org_policy ON PRMPROMPTS
    FOR ALL TO prompt_users
    USING (prmorganization_id = current_setting('app.current_org_id')::bigint);

-- Política para versiones por prompt
CREATE POLICY prompt_version_policy ON PRMPROMPTVERSIONS
    FOR ALL TO prompt_users
    USING (idprmprompts0 IN (
        SELECT idxprompt FROM PRMPROMPTS 
        WHERE prmorganization_id = current_setting('app.current_org_id')::bigint
    ));
```

---

## 📊 MÉTRICAS Y KPIs

### **Métricas Principales:**

#### **Efectividad:**
- **Accuracy Score:** Precisión de respuestas (0-100%)
- **Response Quality:** Calidad de respuestas generadas
- **User Satisfaction:** Satisfacción del usuario final

#### **Rendimiento:**
- **Response Time:** Tiempo de respuesta promedio
- **Throughput:** Número de ejecuciones por minuto
- **Success Rate:** Porcentaje de ejecuciones exitosas

#### **Costo:**
- **Cost per Execution:** Costo promedio por ejecución
- **Token Efficiency:** Tokens utilizados vs. tokens necesarios
- **ROI:** Retorno de inversión del prompt

#### **Calidad:**
- **Validation Score:** Score promedio de validaciones
- **Issue Rate:** Porcentaje de issues encontrados
- **Compliance Rate:** Porcentaje de cumplimiento normativo

---

## ✅ CONCLUSIÓN

La **arquitectura técnica del módulo prompts** proporciona una **base sólida** para la gestión completa del ciclo de vida de prompts de IA, con:

- 🗄️ **Entidades bien diseñadas** con relaciones claras
- 👁️ **Vistas optimizadas** para consultas frecuentes
- ⚙️ **Funciones especializadas** para cálculos complejos
- 🔧 **Procedimientos robustos** para operaciones críticas
- 🔐 **Seguridad granular** por roles y organizaciones
- 📊 **Métricas completas** para monitoreo y optimización

**Esta arquitectura está preparada** para escalar y soportar las necesidades futuras del gobierno de prompts de IA.
