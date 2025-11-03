# PROMPTS - MLOPS ADAPTERS & FINE-TUNING
## EU AI ACT COMPLIANCE - GPAI Downstream Providers (Art. 51-55)

**Equipo:** MLOps Team - Adaptación Modelos  
**Fecha:** 15 de noviembre de 2025  
**Objetivo:** Gobierno completo de adaptación modelos (Adapters, Merge, Quantization, Fine-Tuning) con compliance GPAI  
**Esfuerzo Estimado:** 6-8 días (con 2-3 chats en paralelo)

---

## 🎯 CONTEXTO ESTRATÉGICO

### **Por Qué Es Crítico:**

**EU AI Act Art. 51-55 - GPAI Downstream Providers:**
```
Si modificas un GPAI (GPT-4, Llama, etc.) mediante:
→ Fine-tuning
→ Adapters (LoRA, QLoRA)
→ Model merging
→ ERES PROVEEDOR DOWNSTREAM

Obligaciones:
✅ Art. 53: Documentar modificaciones
✅ Art. 53.1.c: Copyright training data
✅ Anexo XII: Transparency info
✅ Art. 10: Data governance
✅ Art. 15: Accuracy monitoring
```

### **Value Prop Comercial:**
```
"PYMES pueden usar modelos open-source + adapters 
SIN entrenar desde cero.
CodeflowX garantiza compliance GPAI.

Resultado:
→ 90% menos costo vs entrenar desde cero
→ 95% menos CO2
→ 100% compliance garantizado"
```

---

## 🏗️ ARQUITECTURA ADAPTERS

### **Flujo:**
```
1. Usuario quiere modelo para caso uso específico
2. CodeflowX recomienda estrategia (Adapter > Merge > Quantization > Fine-Tuning)
3. Usuario aprueba o modifica estrategia
4. BPMN workflow ejecuta validaciones compliance
5. Si aprobado → Crear adapter/merge/etc.
6. Governance tracking completo (linaje, costs, CO2, compliance)
```

### **Jerarquía Modelos:**
```
Base Model (GPAI) ─→ Adapter ─→ Adapter Específico
    │                   │
    ├─→ Fine-Tuned ─────┤
    │                   │
    └─→ Quantized       └─→ Merged
```

---

## 📋 GRUPOS DE PROMPTS

### **GRUPO A: EXTENSIÓN MODEL.JAVA + NUEVA ENTIDAD (2-3 días)**

---

### **PROMPT A.1 - Extensión Model.java (Campos Adaptación)**

**Contexto:**
Extender entidad Model.java existente con campos adaptación GPAI.

**Prompt Específico:**

```
Necesito EXTENDER JSON EnArt de Model.java (MODMODELS.json) con campos de adaptación y compliance GPAI según EU AI Act Art. 51-55.

ESPECIFICACIONES:
- JSON Existente: sources/json/tables/MODMODELS.json
- Añadir 12 campos nuevos
- Regenerar Entity con generador Python
- Arquitectura: EnArt

CAMPOS A AÑADIR AL JSON:

// Tipo de Adaptación
{
  "name": "modisadapter",
  "type": "BOOLEAN",
  "required": false,
  "label": "Es Adapter (LoRA/QLoRA)",
  "criteria": true,
  "filter": true
},
{
  "name": "modisfinetuned",
  "type": "BOOLEAN",
  "required": false,
  "label": "Es Fine-Tuned",
  "criteria": true,
  "filter": true
},
{
  "name": "modisquantized",
  "type": "BOOLEAN",
  "required": false,
  "label": "Es Quantized",
  "criteria": true,
  "filter": true
},
{
  "name": "modismerged",
  "type": "BOOLEAN",
  "required": false,
  "label": "Es Merged",
  "criteria": true,
  "filter": true
},

// Estrategia Adaptación
{
  "name": "modadaptationstrategy",
  "type": "VARCHAR",
  "size": 50,
  "required": false,
  "label": "Estrategia Adaptación",
  "criteria": true,
  "filter": true,
  "values": ["ADAPTER_LORA", "ADAPTER_QLORA", "FINE_TUNING", "QUANTIZATION", "MERGE", "NONE"]
},

// Modelo Base (FK self-referencing)
{
  "name": "idmodbasemodel",
  "type": "LONG",
  "required": false,
  "label": "Modelo Base",
  "criteria": true,
  "filter": true,
  "fk": {
    "table": "MODMODELS",
    "field": "IDXMODEL"
  },
  "comment": "FK al modelo original (si es adaptación)"
},

// Configuraciones (JSONB)
{
  "name": "modadapterconfig",
  "type": "JSONB",
  "required": false,
  "label": "Configuración Adapter",
  "criteria": false,
  "filter": false,
  "comment": "Config LoRA/QLoRA: {rank, alpha, target_modules, ...}"
},
{
  "name": "modfinetuningconfig",
  "type": "JSONB",
  "required": false,
  "label": "Configuración Fine-Tuning",
  "criteria": false,
  "filter": false
},
{
  "name": "modquantizationconfig",
  "type": "JSONB",
  "required": false,
  "label": "Configuración Quantization",
  "criteria": false,
  "filter": false,
  "comment": "Config: {bits: 4, method: 'bitsandbytes', ...}"
},
{
  "name": "modmergeconfig",
  "type": "JSONB",
  "required": false,
  "label": "Configuración Merge",
  "criteria": false,
  "filter": false,
  "comment": "Config: {method: 'SLERP', models: [...], weights: [...]}"
},

// Metadata Adaptación
{
  "name": "modadaptationmetadata",
  "type": "JSONB",
  "required": false,
  "label": "Metadata Adaptación",
  "criteria": false,
  "filter": false,
  "comment": "Cost, CO2, time, performance_gain, etc."
},

// Compliance GPAI (Art. 53)
{
  "name": "modgpaimodificationsdocurl",
  "type": "VARCHAR",
  "size": 500,
  "required": false,
  "label": "URL Doc Modificaciones GPAI",
  "criteria": false,
  "filter": false,
  "comment": "Anexo XII transparency info si es GPAI modificado"
}

INSTRUCCIONES:
1. Abrir sources/json/tables/MODMODELS.json
2. Añadir 12 campos al array "fields"
3. Ejecutar generador:
   python src/generators/java_entity_generator.py sources/json/tables/MODMODELS.json
4. Verificar Model.java regenerado con nuevos campos
5. Migration SQL (ALTER TABLE):
   ALTER TABLE MODMODELS ADD COLUMN MODISADAPTER BOOLEAN DEFAULT FALSE;
   ALTER TABLE MODMODELS ADD COLUMN IDMODBASEMODEL BIGINT;
   ALTER TABLE MODMODELS ADD CONSTRAINT fk_basemodel FOREIGN KEY (IDMODBASEMODEL) REFERENCES MODMODELS(IDXMODEL);
   ... (resto 12 campos)
6. Extender ModelBusinessService con métodos:
   - createAdapter(baseModelId, adapterConfig)
   - createFineTuned(baseModelId, finetuningConfig)
   - createQuantized(baseModelId, quantizationConfig)
   - createMerged(modelIds, mergeConfig)
   - getModelLineage(modelId) - árbol completo
```

**Artículos Cubiertos:** Art. 51, 53, Anexo XII  
**Esfuerzo:** 1 día  
**Prioridad:** 🔴 Crítica GPAI

---

### **PROMPT A.2 - Nueva Entidad ModelAdaptationStrategy.java**

**Contexto:**
Tabla para tracking de estrategias de adaptación recomendadas y resultados.

**Prompt Específico:**

```
Necesito CREAR entidad EnArt ModelAdaptationStrategy para tracking de estrategias adaptación según EU AI Act compliance.

ESPECIFICACIONES:
- Framework: EnArt
- Namespace: models
- Tabla: MODADAPTATIONSTRATEGIES
- Prefijo: MOD
- Arquitectura: JSON EnArt + BusinessService + ViewModel

PASO 1 - CREAR JSON EnArt:

Ubicación: sources/json/tables/MODADAPTATIONSTRATEGIES.json

{
  "namespace": "models",
  "name": "MODADAPTATIONSTRATEGIES",
  "type": "TABLE",
  "labelMonitor": "modusecase",
  "description": "Historial estrategias adaptación modelos - Tracking recomendaciones EU AI Act GPAI",
  "fields": [
    {
      "name": "idxadaptationstrategy",
      "type": "LONG",
      "pk": true,
      "required": false,
      "label": "ID Estrategia",
      "criteria": true,
      "filter": true
    },
    {
      "name": "iduuid",
      "type": "VARCHAR",
      "size": 36,
      "required": true,
      "label": "UUID"
    },
    {
      "name": "idxproject",
      "type": "LONG",
      "required": false,
      "label": "Proyecto",
      "criteria": true,
      "filter": true,
      "fk": {"table": "PRJPROJECTS", "field": "IDXPROJECT"}
    },
    {
      "name": "modusecase",
      "type": "CLOB",
      "required": true,
      "label": "Caso de Uso",
      "criteria": true,
      "filter": true
    },
    {
      "name": "modtargettask",
      "type": "VARCHAR",
      "size": 100,
      "required": false,
      "label": "Tarea Objetivo",
      "values": ["QA", "SUMMARIZATION", "CODE_GENERATION", "TRANSLATION", "CLASSIFICATION"]
    },
    {
      "name": "modbudgetusd",
      "type": "DECIMAL",
      "required": false,
      "label": "Presupuesto USD"
    },
    {
      "name": "modtimedays",
      "type": "INTEGER",
      "required": false,
      "label": "Tiempo Disponible (días)"
    },
    {
      "name": "modtargetperformance",
      "type": "DECIMAL",
      "required": false,
      "label": "Performance Objetivo (%)"
    },
    {
      "name": "modrecommendations",
      "type": "JSONB",
      "required": true,
      "label": "Recomendaciones IA",
      "comment": "Array estrategias rankeadas: [{strategy, score, cost, time, co2, pros, cons}]"
    },
    {
      "name": "modselectedstrategy",
      "type": "VARCHAR",
      "size": 50,
      "required": false,
      "label": "Estrategia Seleccionada",
      "values": ["ADAPTER", "MERGE", "QUANTIZATION", "FINETUNING", "NONE"]
    },
    {
      "name": "modselectedreason",
      "type": "CLOB",
      "required": false,
      "label": "Razón Selección"
    },
    {
      "name": "modresultingmodelid",
      "type": "LONG",
      "required": false,
      "label": "Modelo Resultante",
      "fk": {"table": "MODMODELS", "field": "IDXMODEL"}
    },
    {
      "name": "modactualcost",
      "type": "DECIMAL",
      "required": false,
      "label": "Costo Real USD"
    },
    {
      "name": "modactualco2kg",
      "type": "DECIMAL",
      "required": false,
      "label": "CO2 Real (kg)"
    },
    {
      "name": "modactualtimehours",
      "type": "INTEGER",
      "required": false,
      "label": "Tiempo Real (horas)"
    },
    {
      "name": "modactualperformance",
      "type": "DECIMAL",
      "required": false,
      "label": "Performance Real (%)"
    },
    {
      "name": "modcreatedat",
      "type": "TIMESTAMP",
      "required": true,
      "label": "Fecha Creación"
    }
  ]
}

PASO 2 - GENERAR Entity:
python src/generators/java_entity_generator.py sources/json/tables/MODADAPTATIONSTRATEGIES.json

PASO 3 - BusinessService:

@Service
@Slf4j
public class ModelAdaptationBusinessService {
    
    @Autowired
    private DAO dao;
    
    @Autowired
    private PythonMicroserviceClient pythonClient;
    
    /**
     * Recomienda estrategia adaptación óptima
     */
    public AdaptationRecommendation recommendStrategy(AdaptationRequest request) {
        // Llamar micro Python leka-model-wrapper o nuevo
        String endpoint = "http://localhost:8006/api/model/recommend-adaptation";
        return pythonClient.post(endpoint, request, AdaptationRecommendation.class);
    }
    
    /**
     * Crea adapter desde base model
     */
    public Model createAdapter(Long baseModelId, AdapterConfig config) {
        Model baseModel = dao.findById(Model.class, baseModelId);
        
        Model adapter = new Model();
        adapter.setIdmodbasemodel(baseModelId);
        adapter.setModisadapter(true);
        adapter.setModadaptationstrategy("ADAPTER_LORA");
        adapter.setModadapterconfig(config.toJson());
        adapter.setModisgpai(baseModel.getModisgpai()); // Hereda de base
        
        dao.insert(adapter);
        return adapter;
    }
    
    /**
     * Obtiene lineage tree completo de un modelo
     */
    public ModelLineageTree getModelLineage(Long modelId) {
        String query = "SELECT * FROM V_MODEL_LINEAGE_TREE WHERE root_model_id = ? OR model_id = ?";
        List<Map> results = dao.findBySQL(query, modelId, modelId);
        return buildTree(results);
    }
}

PASO 4 - ViewModel:
ModelAdaptationRecommendationViewModel.java + adaptation_recommendation.zul

PASO 5 - Migration SQL:
CREATE TABLE MODADAPTATIONSTRATEGIES (...);
CREATE INDEX idx_mod_project ON MODADAPTATIONSTRATEGIES(IDXPROJECT);
```

**Artículos Cubiertos:** Art. 51, 53, Anexo XII  
**Esfuerzo:** 2 días  
**Prioridad:** 🔴 Crítica

---

### **GRUPO B: VISTA LINEAGE TREE (1 día)**

---

### **PROMPT B.1 - Vista Recursiva Model Lineage**

**Contexto:**
Vista SQL recursiva para árbol completo de linaje de modelos.

**Prompt Específico:**

```
Necesito CREAR vista PostgreSQL recursiva V_MODEL_LINEAGE_TREE para visualizar árbol linaje modelos según governance.

SQL VIEW:

CREATE OR REPLACE VIEW V_MODEL_LINEAGE_TREE AS
WITH RECURSIVE model_tree AS (
    -- Base case: modelos sin parent (root models)
    SELECT 
        IDXMODEL as model_id,
        MODNAME as model_name,
        IDMODBASEMODEL as parent_id,
        MODADAPTATIONSTRATEGY as strategy,
        MODADAPTERCONFIG as config,
        1 as depth,
        ARRAY[IDXMODEL] as path,
        IDXMODEL as root_model_id
    FROM MODMODELS
    WHERE IDMODBASEMODEL IS NULL
    
    UNION ALL
    
    -- Recursive case: modelos con parent
    SELECT 
        m.IDXMODEL,
        m.MODNAME,
        m.IDMODBASEMODEL,
        m.MODADAPTATIONSTRATEGY,
        m.MODADAPTERCONFIG,
        mt.depth + 1,
        mt.path || m.IDXMODEL,
        mt.root_model_id
    FROM MODMODELS m
    INNER JOIN model_tree mt ON m.IDMODBASEMODEL = mt.model_id
    WHERE NOT m.IDXMODEL = ANY(mt.path)  -- Evitar ciclos
)
SELECT 
    model_id,
    model_name,
    parent_id,
    strategy,
    config,
    depth,
    path,
    root_model_id,
    array_length(path, 1) as lineage_length,
    CASE 
        WHEN depth = 1 THEN 'ROOT'
        WHEN depth = 2 THEN 'DERIVATIVE'
        ELSE 'NESTED_DERIVATIVE'
    END as lineage_type
FROM model_tree
ORDER BY root_model_id, path;

BUSINESSSERVICE:

@Service
public class ModelLineageBusinessService {
    
    @Autowired
    private DAO dao;
    
    /**
     * Obtiene árbol lineage completo
     */
    public List<ModelLineageNode> getLineageTree(Long modelId) {
        String query = "SELECT * FROM V_MODEL_LINEAGE_TREE WHERE root_model_id = (SELECT root_model_id FROM V_MODEL_LINEAGE_TREE WHERE model_id = ?)";
        return dao.findBySQL(ModelLineageNode.class, query, modelId);
    }
    
    /**
     * Verifica si crear adaptación crearía ciclo
     */
    public boolean wouldCreateCycle(Long baseModelId, Long newModelId) {
        String query = "SELECT COUNT(*) FROM V_MODEL_LINEAGE_TREE WHERE root_model_id = ? AND ? = ANY(path)";
        Long count = dao.findBySQL(query, newModelId, baseModelId);
        return count > 0;
    }
}

VIEWMODEL ZKoss:
ModelLineageTreeViewModel.java
- Visualización tipo tree ZKoss
- Pantalla model_lineage_tree.zul
- Navegación interactiva árbol
```

**Artículos Cubiertos:** Governance interno, trazabilidad  
**Esfuerzo:** 1 día  
**Prioridad:** 🟡 Media-Alta

---

### **GRUPO C: BPMN WORKFLOWS ADAPTACIÓN (3-4 días)**

---

### **PROMPT C.1 - BPMN adapter-creation-approval-v1**

**Contexto:**
Workflow aprobación creación adapters (LoRA/QLoRA) con validaciones compliance GPAI.

**Prompt Específico:**

```
Necesito CREAR BPMN adapter-creation-approval-v1.bpmn para aprobación de adapters según EU AI Act Art. 53.

ESPECIFICACIONES:
- Archivo: adapter-creation-approval-v1.bpmn20.xml
- ID: adapter_creation_approval_v1
- Trigger: Manual (usuario solicita crear adapter)

FLUJO BPMN:

[Start Event: Request Adapter Creation]
    ↓
[User Task: Define Adapter Parameters]
    - Base model selection
    - Adapter type (LoRA, QLoRA)
    - Target modules
    - Rank, alpha
    - Training dataset
    - ViewModel: DefineAdapterParametersViewModel.java
    ↓
[Service Task: Validate GPAI Compliance]
    - Delegate: ValidateGpaiComplianceDelegate.java
    - Checks:
      - Base model es GPAI? → Obligaciones Art. 53
      - Copyright training data OK? (Art. 53.1.c)
      - Documentación modificaciones preparada? (Anexo XII)
    - Output:
      - gpaiCompliant (Boolean)
      - complianceGaps (List<String>)
    ↓
[Exclusive Gateway: GPAI Compliant?]
    ├─ NO → [User Task: Fix Compliance Gaps] → Loop back
    └─ YES → continuar
    ↓
[Service Task: Estimate Cost/CO2/Time]
    - Delegate: EstimateAdapterCostDelegate.java
    - Llama Python: leka-model-wrapper/estimate-adapter-cost
    - Output:
      - estimatedCost (USD)
      - estimatedCO2 (kg)
      - estimatedTime (hours)
      - performanceExpected (%)
    ↓
[Service Task: Compare vs Fine-Tuning]
    - Delegate: CompareAdapterVsFineTuningDelegate.java
    - Calcula savings:
      - Cost savings: 90%
      - CO2 savings: 95%
      - Time savings: 85%
    - Output: comparisonReport
    ↓
[User Task: Review and Approve]
    - Mostrar comparison report
    - Roles: ml-engineers, tech-leads
    - ViewModel: ReviewAdapterApprovalViewModel.java
    - Decision: Approve / Modify / Reject
    ↓
[Exclusive Gateway: Approved?]
    ├─ YES → [Service Task: Create Adapter Record]
    │           - Create Model entity con MODISADAPTER = true
    │           - Link base model (IDMODBASEMODEL)
    │           - Save adapter config
    │           - Create ImmutableLog entry
    │           └─→ [Service Task: Trigger Adapter Training Job]
    │                 └─→ [End: Adapter Approved]
    │
    └─ NO → [End: Adapter Rejected]

DELEGATES A CREAR:
1. ValidateGpaiComplianceDelegate.java
2. EstimateAdapterCostDelegate.java
3. CompareAdapterVsFineTuningDelegate.java
4. CreateAdapterRecordDelegate.java
5. TriggerAdapterTrainingJobDelegate.java

VIEWMODELS + ZUL:
1. DefineAdapterParametersViewModel.java + define_adapter.zul
2. FixComplianceGapsViewModel.java + fix_gaps.zul
3. ReviewAdapterApprovalViewModel.java + review_adapter.zul

IMPORTANTE:
- Si base model es GPAI → Validación Art. 53 obligatoria
- Copyright training data verificado antes
- Documentar modificaciones GPAI (Anexo XII)
- ImmutableLog de decisión aprobación
```

**Artículos Cubiertos:** Art. 53, Anexo XII  
**Esfuerzo:** 2 días  
**Prioridad:** 🔴 Crítica

---

### **PROMPT C.2 - BPMN finetuning-approval-v1 (RESTRICTIVO)**

**Contexto:**
Workflow RESTRICTIVO para fine-tuning (requiere justificación por qué NO usar adapter).

**Prompt Específico:**

```
Necesito CREAR BPMN finetuning-approval-v1.bpmn RESTRICTIVO para fine-tuning según EU AI Act (prioriza adapters sobre fine-tuning completo).

ESPECIFICACIONES:
- Archivo: finetuning-approval-v1.bpmn20.xml
- ID: finetuning_approval_v1
- CARÁCTER: RESTRICTIVO (dificultar fine-tuning, priorizar adapters)

FLUJO BPMN:

[Start Event: Request Fine-Tuning]
    ↓
[User Task: Justify Why NOT Adapter]
    - OBLIGATORIO: Explicar por qué adapter NO es suficiente
    - Min 100 caracteres justificación
    - Casos válidos:
      - Cambio arquitectura modelo necesario
      - Performance crítico y adapter insuficiente
      - Regulación específica requiere fine-tuning
    - ViewModel: JustifyFineTuningViewModel.java
    ↓
[Service Task: Evaluate Justification with AI]
    - Delegate: EvaluateFineTuningJustificationDelegate.java
    - Llama micro Python NLP para analizar justificación
    - Output:
      - justificationValid (Boolean)
      - justificationScore (0-1)
      - suggestedAlternative ("Consider adapter instead...")
    ↓
[Exclusive Gateway: Justification Score > 0.7?]
    ├─ NO (score bajo) → [User Task: Improve Justification]
    │                      - Mostrar sugerencia IA
    │                      - Opciones: Reescribir, Switch to Adapter, Cancel
    │                      └─→ Loop back o Cancel
    │
    └─ YES → continuar
    ↓
[Service Task: Calculate Full Cost (Fine-Tuning)]
    - Delegate: CalculateFineTuningCostDelegate.java
    - Estima:
      - Cost: 10-20x más que adapter
      - CO2: 20-50x más que adapter
      - Time: 5-10x más que adapter
    ↓
[Service Task: Compare Adapter vs Fine-Tuning]
    - Mostrar comparison side-by-side
    - Highlight: "¿Seguro que adapter no es suficiente?"
    ↓
[User Task: Final Confirmation (Senior Approval)]
    - Roles: tech-leads, ml-architects (NO ml-engineers - escalado)
    - Mostrar:
      - Justificación original
      - Cost comparison (Adapter vs Fine-Tuning)
      - CO2 impact
    - Checkboxes confirmar:
      - "He evaluado adapters y NO son suficientes"
      - "Entiendo el costo adicional (10-20x)"
      - "Entiendo el impacto CO2 (20-50x)"
    - ViewModel: FinalFineTuningApprovalViewModel.java
    - Decision: Approve Fine-Tuning / Switch to Adapter / Reject
    ↓
[Exclusive Gateway: Final Decision?]
    ├─ APPROVE FT → [Service Task: Validate GPAI Compliance]
    │                 - Validación Art. 53 completa
    │                 - Copyright check (Art. 53.1.c)
    │                 - Doc Anexo XII
    │                 └─→ [Service Task: Create Fine-Tuning Record]
    │                       └─→ [End: Fine-Tuning Approved]
    │
    ├─ SWITCH ADAPTER → [Call Activity: adapter-creation-approval-v1]
    │                     └─→ [End: Switched to Adapter]
    │
    └─ REJECT → [End: Fine-Tuning Rejected]

FILOSOFÍA:
- Fine-tuning debe ser ÚLTIMA OPCIÓN
- Proceso largo y tedioso (intencional)
- Múltiples confirmaciones
- Escalado a senior approval
- Mostrar impacto ambiental (guilt trip sostenibilidad)

DROOLS RULE:
adapter-vs-finetuning-decision.drl
- Si justification score < 0.7 → Auto-reject
- Si budget < 1000 USD → Suggest adapter
- Si CO2 concern → Strongly suggest adapter
```

**Artículos Cubiertos:** Art. 53, Anexo XII, Governance sostenibilidad  
**Esfuerzo:** 2 días  
**Prioridad:** 🟡 Media-Alta

---

### **GRUPO D: PYTHON MICROSERVICIO ADAPTACIÓN (2 días)**

---

### **PROMPT D.1 - Extensión leka-model-wrapper (Adaptation Recommender)**

**Contexto:**
Extender leka-model-wrapper con recomendador de estrategias adaptación.

**Prompt Específico:**

```
Necesito EXTENDER leka-model-wrapper con funcionalidad de recomendación de estrategias de adaptación según best practices MLOps y EU AI Act.

MICROSERVICIO ACTUAL:
- Ubicación: /mnt/c/Users/ManuelGonzalez/git/leka-model-wrapper
- Puerto: 8006
- Funcionalidad actual: Wrapper modelos, benchmarking, drift

NUEVOS ENDPOINTS:

1. POST /api/model/recommend-adaptation
   Input: {
     "use_case": "customer_support_chatbot",
     "base_model_options": ["llama-2-7b", "mistral-7b", "phi-3"],
     "dataset_size": 10000,
     "budget_usd": 500,
     "time_days": 7,
     "hardware": ["cpu", "gpu_t4"],
     "target_performance": 0.85,
     "priority": "cost"  // cost, time, co2, performance
   }
   Output: {
     "recommended_strategy": "ADAPTER_LORA",
     "recommended_base_model": "llama-2-7b",
     "confidence": 0.92,
     "justification": "LoRA adapter on Llama-2-7b provides best cost/performance balance...",
     "alternatives_ranked": [
       {
         "strategy": "ADAPTER_LORA",
         "base_model": "llama-2-7b",
         "score": 0.92,
         "estimated_cost_usd": 150,
         "estimated_time_hours": 24,
         "estimated_co2_kg": 2.5,
         "estimated_performance": 0.87,
         "pros": ["Lowest cost", "Fast training", "Low CO2"],
         "cons": ["Slightly lower performance vs fine-tuning"]
       },
       {
         "strategy": "QUANTIZATION",
         "base_model": "llama-2-7b",
         "score": 0.78,
         "estimated_cost_usd": 50,
         "estimated_time_hours": 2,
         "estimated_co2_kg": 0.5,
         "estimated_performance": 0.82,
         "pros": ["Fastest", "Cheapest", "Minimal CO2"],
         "cons": ["Lower performance", "Precision loss"]
       },
       {
         "strategy": "FINE_TUNING",
         "base_model": "llama-2-7b",
         "score": 0.45,
         "estimated_cost_usd": 3000,
         "estimated_time_hours": 240,
         "estimated_co2_kg": 50,
         "estimated_performance": 0.91,
         "pros": ["Highest performance"],
         "cons": ["20x cost", "20x CO2", "10x time - NOT RECOMMENDED"]
       }
     ],
     "sustainability_analysis": {
       "adapter_vs_finetuning_co2_savings": "95%",
       "adapter_vs_finetuning_cost_savings": "95%",
       "adapter_vs_finetuning_time_savings": "90%"
     },
     "recommendation": "STRONGLY RECOMMEND ADAPTER - 95% cost/CO2 savings, performance acceptable"
   }
   
   Análisis:
   - Multi-criteria scoring (weighted by priority)
   - Heurísticas MLOps best practices
   - Benchmarks modelos conocidos
   - Estimaciones costo/CO2 basadas en specs GPU/dataset
   - SIEMPRE prioriza: Adapter > Quantization > Merge > Fine-Tuning

2. POST /api/model/validate-adapter-config
   Input: {
     "base_model": "llama-2-7b",
     "adapter_type": "LORA",
     "config": {
       "rank": 8,
       "alpha": 16,
       "target_modules": ["q_proj", "v_proj"],
       "dropout": 0.1
     }
   }
   Output: {
     "config_valid": bool,
     "validation_errors": [],
     "recommendations": [],
     "estimated_params_trainable": 4200000,  // 4.2M params vs 7B base
     "trainable_percentage": 0.06  // 0.06% del modelo
   }

3. POST /api/model/estimate-adaptation-cost
   Input: {
     "strategy": "ADAPTER_LORA",
     "base_model": "llama-2-7b",
     "dataset_size": 10000,
     "epochs": 3,
     "gpu_type": "t4"
   }
   Output: {
     "cost_breakdown": {
       "gpu_hours": 24,
       "gpu_cost_per_hour": 0.35,
       "total_gpu_cost": 8.4,
       "storage_cost": 2,
       "total_cost_usd": 10.4
     },
     "co2_estimate_kg": 2.1,
     "time_estimate_hours": 24,
     "comparison_finetuning": {
       "cost_usd": 180,
       "co2_kg": 42,
       "time_hours": 240,
       "savings_adapter": {
         "cost": "94%",
         "co2": "95%",
         "time": "90%"
       }
     }
   }

REQUISITOS TÉCNICOS:
- Heurísticas adaptación (papers LoRA, QLoRA)
- Benchmarks modelos (HuggingFace leaderboard)
- Calculadora CO2 (CodeCarbon estimations)
- Cost calculator por GPU type
- Validación configs adapter (peft library compatibility)
- NO persistencia (stateless)
```

**Artículos Cubiertos:** Art. 51, 53, Governance sostenibilidad  
**Esfuerzo:** 2 días  
**Prioridad:** 🔴 Crítica

---

## 📊 RESUMEN PROMPTS - MLOPS ADAPTERS

| Prompt | Componente | Tipo | Funcionalidad | Esfuerzo | Prioridad |
|--------|------------|------|---------------|----------|-----------|
| **A.1** | Model.java extension | JSON EnArt | 12 campos adaptación + FK base model | 1 día | 🔴 Crítica |
| **A.2** | ModelAdaptationStrategy | Entity nueva | Tracking estrategias + resultados | 2 días | 🔴 Crítica |
| **B.1** | V_MODEL_LINEAGE_TREE | Vista SQL | Árbol recursivo linaje modelos | 1 día | 🟡 Media-Alta |
| **C.1** | adapter-creation-approval-v1 | BPMN | Workflow aprobación adapters | 2 días | 🔴 Crítica |
| **C.2** | finetuning-approval-v1 | BPMN | Workflow RESTRICTIVO fine-tuning | 2 días | 🟡 Media-Alta |
| **D.1** | leka-model-wrapper extension | Python Micro | Recommender + validators | 2 días | 🔴 Crítica |

**TOTAL ESFUERZO:** 10-12 días  
**CON 2 CHATS PARALELOS:** 5-6 días reales (o 1-2h con velocidad actual 🚀)

---

## 🎯 DISTRIBUCIÓN TRABAJO PARALELO

### **CHAT MLOPS-1 - Entities & Data:**
- Prompt A.1 (Model.java extension)
- Prompt A.2 (ModelAdaptationStrategy)
- Prompt B.1 (Vista lineage tree)
- **Esfuerzo:** 4 días (o 40-60 min actual)

### **CHAT MLOPS-2 - BPMNs:**
- Prompt C.1 (adapter-creation BPMN)
- Prompt C.2 (finetuning BPMN restrictivo)
- **Esfuerzo:** 4 días (o 40-60 min actual)

### **CHAT MLOPS-3 - Python Micro:**
- Prompt D.1 (leka-model-wrapper extension)
- **Esfuerzo:** 2 días (o 20-30 min actual)

**PARALELO TOTAL:** 4-6 días (o **1-2 horas con velocidad actual** 🚀)

---

## 💰 VALUE PROP COMERCIAL

### **Mensaje Post-Implementación:**

```
🌱 CodeflowX - Sostenibilidad + Compliance

¿Tu empresa usa IA pero preocupa el impacto ambiental?

CodeflowX te permite:
✅ Adaptar modelos existentes (95% menos CO2 vs entrenar)
✅ Reutilizar 3M+ modelos open-source
✅ Cumplir EU AI Act Art. 51-55 (GPAI)
✅ Governance completo adaptación

Resultado:
💰 90% menos costo
🌍 95% menos CO2
⚡ 90% menos tiempo
✅ 100% compliance

[CTA: Ver Dashboard Sostenibilidad]
```

### **Casos Uso Potenciados:**

**Agencias Marketing:**
> "Usa Llama-2-7b + adapter marketing → 10€ vs 200€ fine-tuning completo"

**PYMES Tech:**
> "Adapta Mistral para tu dominio → 50€ vs 1000€ entrenar desde cero"

**Consultoras:**
> "Ofrece a clientes: modelos custom + compliance + sostenibilidad certificable"

---

## 🚀 **DECISIÓN INMEDIATA**

### **OPCIÓN A: Incluir AHORA (Recomendado)**
```
Añadir Doc 6 a implementación actual
→ Lanzar 3 chats adicionales en paralelo
→ En 1-2h terminado
→ Lanzamiento martes CON adapters governance
→ Value prop MÁS FUERTE
```

### **OPCIÓN B: Post-Lanzamiento**
```
Implementar semana después del martes
→ Lanzamiento básico martes
→ Upgrade siguiente semana
→ Anuncio "NEW: Adapters Governance" como feature añadida
```

---

## ⏰ **CON VELOCIDAD ACTUAL (1h por doc):**

```
AHORA:
⏳ Doc 5: 20-30 min más

SI AÑADIMOS DOC 6:
⏳ Doc 6: 1-2h adicional (3 chats en paralelo)

LUEGO:
⏳ Docs 3+4: 1-2h (6 chats en paralelo)

TOTAL CON DOC 6: 4-5 HORAS (vs 3h sin Doc 6)
```

**¿Vale la pena 1-2h más para tener governance adapters completo?** 

**SÍ** → Porque:
- ✅ Compliance GPAI (Art. 51-55) completo
- ✅ Value prop sostenibilidad 
- ✅ Casos uso PYMES/agencias más fuertes
- ✅ Diferenciación vs Marco aumenta

---

## 🎯 **MI RECOMENDACIÓN: INCLUIR DOC 6 AHORA**

**Timeline actualizado:**
```
✅ HORA 0-2:   Docs 1+2 Python → DONE
⏳ HORA 2-3:   Doc 5 Java entities → EN PROGRESO
⏳ HORA 3-5:   Doc 6 Adapters → NUEVO (3 chats)
⏳ HORA 5-7:   Docs 3+4 en paralelo → ÚLTIMO

TOTAL: 7 HORAS → 100% COMPLIANCE + ADAPTERS GOVERNANCE ✅
```

**¿Lanzamos Doc 6 cuando termine Doc 5?** 🚀

O prefieres **solo compliance básico** (5h) y adapters después del martes? 🤔
