# Comparación: MLflow Directo vs Sistema Propio para CodeflowX

**Fecha:** Enero 2025

---

## 🎯 Resumen Ejecutivo

**El problema principal:** La interfaz gráfica de MLflow es limitada y no se integra bien con nuestro sistema de governance.

**Recomendación:** **Enfoque Híbrido** - Usar MLflow como backend de almacenamiento y tracking, pero crear nuestra propia UI integrada en CodeflowX.

---

## 📊 Comparación Detallada

### Opción 1: Usar MLflow Directamente (Sin Desarrollo Propio)

#### ✅ Ventajas:
1. **Cero desarrollo backend**
   - MLflow ya existe y funciona
   - Tracking server ya configurado
   - API REST completa y estable

2. **Funcionalidad completa de tracking**
   - Experiments, runs, métricas, parámetros, artefactos
   - Historial temporal de métricas
   - Comparación de runs

3. **Estándar de la industria**
   - Muchos equipos ya lo conocen
   - Compatible con herramientas MLOps

#### ❌ Desventajas:

1. **UI Limitada y No Personalizable** ⚠️ **PROBLEMA PRINCIPAL**
   - UI básica de MLflow (tracking UI)
   - No se puede personalizar para governance
   - No integrada con el resto de CodeflowX
   - Usuarios tendrían que cambiar de aplicación

2. **Datos Separados**
   - MLflow tiene su propia base de datos (SQLite, PostgreSQL, etc.)
   - Datos separados de nuestro modelo de governance
   - Dificulta queries cruzadas (ej: "dame todos los runs de modelos aprobados")

3. **Integración Compleja con Governance**
   - No integrado con workflows BPMN
   - No integrado con sistema de aprobaciones
   - No integrado con análisis de bias/explainability existentes
   - No hay relación directa con `Model`, `ModelBiasAnalysis`, etc.

4. **Sin Contexto de Governance**
   - MLflow no entiende conceptos de governance (aprobaciones, compliance, riesgo)
   - No hay trazabilidad automática con políticas de governance
   - No integrado con FRIA, compliance checks, etc.

5. **Autenticación y Autorización Separada**
   - MLflow tiene su propio sistema de auth (o no tiene)
   - No integrado con nuestro sistema de permisos
   - Acceso no controlado por roles de governance

---

### Opción 2: Sistema Propio (Como en la Propuesta)

#### ✅ Ventajas:

1. **UI Personalizada e Integrada** ✅ **SOLUCIÓN AL PROBLEMA**
   - UI nativa de CodeflowX
   - Integrada con el resto del sistema
   - Puede mostrar contexto de governance (aprobaciones, compliance, etc.)
   - Misma experiencia de usuario

2. **Datos Integrados**
   - Todo en nuestra base de datos PostgreSQL
   - Relaciones directas con `Model`, análisis, workflows
   - Queries eficientes cruzadas

3. **Integración Nativa con Governance**
   - Integrado con workflows BPMN
   - Integrado con aprobaciones
   - Relaciones directas con `ModelBiasAnalysis`, `ModelExplainability`
   - Trazabilidad completa

4. **Control Total**
   - Podemos agregar campos específicos de governance
   - Podemos crear workflows personalizados
   - Extensible según necesidades

#### ❌ Desventajas:

1. **Desarrollo Completo**
   - Necesitamos crear entidades, repositorios, servicios, DTOs, endpoints
   - Necesitamos crear toda la UI
   - Mantenimiento a largo plazo

2. **Rueda de Reinventar**
   - MLflow ya existe y funciona bien
   - Podríamos duplicar funcionalidad

---

## 🎯 Opción 3: Enfoque Híbrido (RECOMENDADO)

### Idea: Usar MLflow como Backend, CodeflowX como Frontend

**Backend:** MLflow Tracking Server
- Almacena experiments, runs, métricas, parámetros, artefactos
- Usamos la API REST de MLflow para leer/escribir

**Frontend:** CodeflowX UI
- UI personalizada que consume la API de MLflow
- Integrada con el resto de CodeflowX
- Muestra contexto de governance adicional

**Sincronización:**
- Cuando creamos un run en CodeflowX → Lo creamos también en MLflow
- Cuando se ejecuta análisis → Guardamos en MLflow + referencias en nuestra BD
- Mantenemos una tabla de mapeo: `CodeflowX Run ID` ↔ `MLflow Run ID`

### Estructura Propuesta:

```java
@Entity
@Table(name = "mod_run")
public class ModelRun {
    @Id
    private Long idxrun; // ID de CodeflowX

    // Mapeo con MLflow
    @Column(name = "modrunmlflowid", length = 255)
    private String modrunmlflowid; // MLflow run_id

    @Column(name = "modrunmlflowexperimentid", length = 255)
    private String modrunmlflowexperimentid; // MLflow experiment_id

    // Relaciones con governance
    @ManyToOne
    @JoinColumn(name = "idxmodel")
    private Model model;

    @ManyToOne
    @JoinColumn(name = "idxbiasanalysis")
    private ModelBiasAnalysis biasAnalysis; // Referencia a nuestro análisis

    // Metadata adicional de governance
    @Column(name = "modrunapprovalstatus", length = 50)
    private String modrunapprovalstatus; // PENDING, APPROVED, REJECTED

    @Column(name = "modrunworkflowinstanceid", length = 255)
    private String modrunworkflowinstanceid; // BPMN workflow instance

    // Campos básicos (se sincronizan con MLflow)
    @Column(name = "modrunname", length = 255)
    private String modrunname;

    @Column(name = "modrunstatus", length = 50)
    private String modrunstatus; // RUNNING, COMPLETED, FAILED

    // Timestamps (sincronizados con MLflow)
    @Column(name = "modrunstartedat")
    private Date modrunstartedat;

    @Column(name = "modrunendedat")
    private Date modrunendedat;
}
```

### Flujo de Trabajo:

1. **Usuario crea run en CodeflowX UI**
   ```java
   // 1. Crear run en MLflow
   MLflowClient.createRun(experimentId, runName, parameters, tags);

   // 2. Guardar referencia en nuestra BD
   ModelRun run = new ModelRun();
   run.setModrunmlflowid(mlflowRunId);
   run.setModel(model);
   run.setModrunname(runName);
   // ... otros campos
   modelRunRepository.save(run);
   ```

2. **Usuario ejecuta análisis de bias**
   ```java
   // 1. Ejecutar análisis (ya existe)
   ModelBiasAnalysis analysis = biasAnalysisService.create(...);

   // 2. Actualizar run en MLflow con métricas
   MLflowClient.logMetric(mlflowRunId, "fairness_score", analysis.getModfairnessscore());
   MLflowClient.logMetric(mlflowRunId, "demographic_parity", analysis.getModdemographicparity());

   // 3. Guardar referencia en nuestro run
   run.setBiasAnalysis(analysis);
   run.setModrunstatus("COMPLETED");
   modelRunRepository.save(run);
   ```

3. **UI de CodeflowX muestra run**
   ```typescript
   // 1. Obtener run de nuestra BD
   const run = await getModelRun(runId);

   // 2. Obtener métricas detalladas de MLflow
   const mlflowRun = await mlflowClient.getRun(run.modrunmlflowid);

   // 3. Combinar datos para UI
   const runData = {
     ...run, // Datos de governance (aprobación, workflow, etc.)
     metrics: mlflowRun.metrics, // Métricas de MLflow
     parameters: mlflowRun.parameters,
     artifacts: mlflowRun.artifacts
   };
   ```

### Endpoints Propuestos:

```
# Crear run (crea en MLflow + referencia en CodeflowX)
POST /api/v1/models/{modelId}/runs
{
  "experimentId": "exp-123", // MLflow experiment ID
  "name": "Bias Analysis Run",
  "parameters": {...},
  "tags": {...}
}
→ Crea en MLflow, guarda referencia en CodeflowX
→ Retorna: { id: 456, mlflowId: "run-abc123", ... }

# Obtener run completo (combina MLflow + CodeflowX)
GET /api/v1/models/runs/{id}
→ Obtiene de CodeflowX BD
→ Obtiene detalles de MLflow
→ Combina y retorna

# Listar runs (con filtros de governance)
GET /api/v1/models/{modelId}/runs?approvalStatus=APPROVED
→ Filtra en CodeflowX BD (governance)
→ Obtiene detalles de MLflow para cada run
→ Retorna lista combinada
```

---

## 📊 Tabla Comparativa Final

| Aspecto | MLflow Directo | Sistema Propio | **Híbrido (Recomendado)** |
|---------|---------------|----------------|---------------------------|
| **UI Personalizada** | ❌ No | ✅ Sí | ✅ Sí |
| **Integración Governance** | ❌ No | ✅ Sí | ✅ Sí |
| **Datos Integrados** | ❌ Separados | ✅ Integrados | ⚠️ Mapeo |
| **Desarrollo Backend** | ✅ 0% | ❌ 100% | ✅ 30% (solo mapeo) |
| **Desarrollo Frontend** | ❌ 0% (usa MLflow UI) | ❌ 100% | ✅ 100% |
| **Funcionalidad Tracking** | ✅ Completa | ⚠️ Debe crearse | ✅ Completa (MLflow) |
| **Mantenimiento** | ✅ Mínimo | ❌ Alto | ⚠️ Medio |
| **Estándar Industria** | ✅ Sí | ❌ No | ✅ Sí (backend) |
| **Extensibilidad** | ❌ Limitada | ✅ Total | ✅ Total (frontend) |
| **Reproducibilidad** | ✅ Sí (MLflow) | ⚠️ Debe implementarse | ✅ Sí (MLflow) |
| **Integración MLOps** | ✅ Nativa | ⚠️ Manual | ✅ Nativa (MLflow) |

---

## 🎯 Recomendación Final

### **Opción Recomendada: Enfoque Híbrido**

**Razones:**

1. ✅ **Soluciona el problema de UI**: Tenemos UI personalizada integrada
2. ✅ **Aprovecha MLflow**: Usamos su backend robusto y probado
3. ✅ **Integración Governance**: Mantenemos control sobre datos de governance
4. ✅ **Menos desarrollo**: No reinventamos la rueda del tracking
5. ✅ **Extensibilidad**: Podemos agregar campos y funcionalidades de governance

**Implementación:**

1. **Fase 1 (Backend)**:
   - Crear entidad `ModelRun` con mapeo a MLflow
   - Crear servicio que sincroniza con MLflow API
   - Endpoints que combinan datos de CodeflowX + MLflow

2. **Fase 2 (Integración Análisis)**:
   - Modificar servicios de análisis para crear/actualizar runs en MLflow
   - Guardar referencias cruzadas

3. **Fase 3 (Frontend)**:
   - UI personalizada que consume nuestros endpoints
   - Muestra datos de MLflow + contexto de governance
   - Integrada con resto de CodeflowX

---

## 💡 Alternativa: MLflow UI Embedding (No Recomendado)

Podríamos embebed la UI de MLflow en CodeflowX con iframe, pero:

- ❌ No se puede personalizar
- ❌ Problemas de autenticación
- ❌ No integrado con governance
- ❌ UX inconsistente

**No recomendado.**

---

## ✅ Conclusión

**El enfoque híbrido es la mejor solución:**
- Soluciona el problema de UI (tenemos UI propia)
- Aprovecha MLflow como backend (no reinventamos)
- Integra governance (control sobre datos críticos)
- Balance entre desarrollo y funcionalidad

**Próximos pasos:**
1. Validar arquitectura híbrida con el equipo
2. Implementar mapeo CodeflowX ↔ MLflow
3. Crear UI personalizada en CodeflowX
4. Integrar con análisis existentes
