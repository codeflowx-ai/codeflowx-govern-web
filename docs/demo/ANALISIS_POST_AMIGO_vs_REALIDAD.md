# 📊 ANÁLISIS POST AMIGO vs REALIDAD CODEFLOWX
## ¿Qué tenemos vs qué necesitamos para demo próxima semana?

**Fecha:** 5 Noviembre 2025  
**Contexto:** Amigo publicará post LinkedIn + demo próxima semana  
**Urgencia:** 🔴 **CRÍTICA** (7 días para preparar demo)

---

## 🎯 RESUMEN EJECUTIVO

**Post amigo menciona:**
- ✅ **5 cosas que CodeflowX aporta HOY** (mayoría las tenemos)
- ⚠️ **4 artefactos concretos** que quiere ver (algunos faltantes)
- 🔴 **8 puntos lista "enséñame" 20 min** (varios gaps críticos)

**Estado general:**
- ✅ **70% de lo que menciona:** YA lo tenemos
- ⚠️ **20% pendiente:** Fácil implementar (3-4 días)
- 🔴 **10% crítico:** Necesita trabajo urgente (kill-switch, Art. 71 export)

---

## ✅ PARTE 1: QUÉ APORTA HOY (5 puntos)

### **1. "Estandariza el ciclo: experimentación → evaluación → aprobación → despliegue"**

**¿Lo tenemos?** ✅ **SÍ - 100%**

**Evidencia:**
- ✅ BPMN workflows (deployment-automation, model-approval)
- ✅ Entities: EXPERIMENTS, EVALUATIONS, APPROVALS, DEPLOYMENTS
- ✅ Estado: PROMPTS_03 completo (7/7)

**Demo:**
- Mostrar workflow completo en Camunda/Motor BPMN
- Dashboard con stages (Experiment → Evaluation → Approval → Deployment)

---

### **2. "Traza cambios por versión (modelo, datos, config) y firma releases"**

**¿Lo tenemos?** ✅ **90%**

**Evidencia:**
- ✅ Versionado modelos: MODELS.MODVERSION
- ✅ Lineage experimentos: TRNEXPERIMENTLINEAGE (datasets, base models, prompts)
- ✅ Git tracking: TRNENVIRONMENTS (git_commit, git_branch, git_diff)
- ⚠️ **Firma releases:** Parcial (hash immutable, falta firma digital criptográfica)

**Gap:**
- ⚠️ Firma criptográfica releases (GPG signature, X.509 certificate)

**Demo:**
- Mostrar lineage tree (modelo → datasets → config)
- Mostrar versioning (v1, v2, v3 con diff)
- **Gap visible:** Firma digital (mencionar roadmap)

---

### **3. "Acelera la auditoría con audit pack exportable"**

**¿Lo tenemos?** ⚠️ **30% (CRÍTICO PARA DEMO)**

**Evidencia:**
- ✅ Metadata existe: MODELS, EVALUATIONS, COMPLIANCE, LOGS
- ⚠️ **Export audit pack PDF/JSON:** NO implementado (PROMPTS_02 #1 pendiente)
- ⚠️ **AuditPack_v1.12.pdf:** NO existe

**Gap CRÍTICO:**
- 🔴 Microservicio leka-server-documents (genera Anexo IV PDF)
- 🔴 Export consolidado audit pack (PDF + JSON)

**Artefacto esperado:**
```
AuditPack_v1.12.pdf (50-80 páginas):
├─ Sección 1: Descripción sistema IA
├─ Sección 2: Training data (origen, tamaño, calidad)
├─ Sección 3: Arquitectura modelo
├─ Sección 4: Métricas rendimiento
├─ Sección 5: Bias analysis
├─ Sección 6: Risk assessment (FRIA)
├─ Sección 7: Medidas mitigación
├─ Sección 8: Cybersecurity measures
├─ Sección 9: Human oversight (HITL)
├─ Sección 10: Monitoring plan
├─ Sección 11: Logs audit trail
└─ Sección 12: Approval signatures

audit.json (structured data):
{
  "model_id": 123,
  "model_name": "Credit Scoring v2",
  "risk_level": "HIGH_RISK",
  "metrics": {...},
  "bias_analysis": {...},
  "fria_assessment": {...},
  "approvals": [...]
}
```

**Acción URGENTE:** Implementar export básico (aunque sea simplificado) para demo.

---

### **4. "Políticas as code: reglas verificables por modelo/caso de uso/entorno"**

**¿Lo tenemos?** ✅ **80%**

**Evidencia:**
- ✅ Entities: POLICIES, RULES
- ✅ Granularidad: Global → Proyecto → Modelo → Deployment (4 niveles)
- ⚠️ **Export PolicyRegister.csv:** NO implementado

**Gap:**
- ⚠️ Export CSV políticas (fácil implementar)

**Artefacto esperado:**
```csv
PolicyRegister.csv:
policy_id,policy_name,scope,model_id,environment,rule,threshold,approver,approved_date
1,Bias Threshold Global,GLOBAL,ALL,ALL,demographic_parity<0.1,0.1,CTO,2025-01-15
2,Hallucination Credit Scoring,MODEL,123,PRODUCTION,hallucination_rate<0.05,0.05,Compliance,2025-02-20
3,Latency SLA Production,ENVIRONMENT,ALL,PRODUCTION,p95_latency<200ms,200,DevOps,2025-01-10
```

**Acción:** Script export PolicyRegister.csv (2 horas implementación).

---

### **5. "Workflows para FRIA/DPIA/AI Act, plantillas y trazabilidad documental"**

**¿Lo tenemos?** ✅ **100%**

**Evidencia:**
- ✅ BPMN: fria-assessment-workflow (implementado PROMPTS_03)
- ✅ FRIA wizard UI
- ✅ DPIA integrada FRIA (Art. 27.4)
- ✅ Templates documentales

**Demo:**
- Mostrar FRIA wizard funcionando
- Workflow BPMN con aprobaciones
- PDF FRIA generado

---

## ⚠️ PARTE 2: ARTEFACTOS QUE QUIERE VER (4 artefactos)

### **Artefacto 1: AuditPack_v1.12.pdf + audit.json**

**¿Lo tenemos?** 🔴 **10% (CRÍTICO)**

**Estado:**
- ✅ Datos existen (MODELS, EVALUATIONS, FRIA, LOGS)
- 🔴 **Export PDF/JSON:** NO implementado (PROMPTS_02 #1)

**Acción URGENTE (antes demo):**
- Implementar export básico Anexo IV (aunque sea simplificado)
- Endpoint: `GET /api/models/{id}/export-audit-pack`
- Response: PDF + JSON

**Estimación:** 2-3 días (1 chat urgente)

---

### **Artefacto 2: PolicyRegister.csv**

**¿Lo tenemos?** ⚠️ **70%**

**Estado:**
- ✅ Políticas existen en BD (POLICIES, RULES)
- ⚠️ **Export CSV:** NO implementado (30 min)

**Acción URGENTE:**
```sql
-- Query export policies
COPY (
    SELECT 
        p.idxpolicy AS policy_id,
        p.polpolicy_name AS policy_name,
        p.polscope AS scope,
        p.polmodel_id AS model_id,
        p.polenvironment AS environment,
        r.rulrule_expression AS rule,
        r.rulthreshold AS threshold,
        p.polapprover AS approver,
        p.polapproved_at AS approved_date
    FROM POLICIES p
    LEFT JOIN RULES r ON r.rulpolicy_id = p.idxpolicy
    ORDER BY p.polcreated_at DESC
) TO '/tmp/PolicyRegister.csv' WITH CSV HEADER;
```

**Acción:** Script Java/Python export CSV (2 horas).

---

### **Artefacto 3: EU-AI-DB_AnnexVIII_export.zip**

**¿Lo tenemos?** ⚠️ **85%**

**Estado:**
- ✅ Campos Anexo VIII preparados (PROMPTS_03 entity RegistrationPreparation)
- ✅ 3 secciones: System info (13 campos), Provider info (9), Conformity (5)
- ⚠️ **Export ZIP:** NO implementado
- ⚠️ **API Comisión Europea:** Pendiente (externa, fuera control)

**Artefacto esperado:**
```
EU-AI-DB_AnnexVIII_export.zip:
├─ system_info.json (13 campos Anexo VIII Parte A)
├─ provider_info.json (9 campos Anexo VIII Parte B)
├─ conformity_declaration.pdf (Art. 47)
├─ instructions_for_use.pdf (Art. 13)
├─ technical_documentation.pdf (Anexo IV resumen)
└─ certificates/ (si aplica)
```

**Acción:** Script export ZIP consolidado (3-4 horas).

---

### **Artefacto 4: egress-manifest.yml + dpia_fria_bundle/**

**¿Lo tenemos?** 🔴 **20% (NUEVO CONCEPTO)**

**Estado:**
- ❌ **egress-manifest.yml:** NO existe (concepto nuevo - excelente idea)
- ✅ **dpia_fria_bundle:** Parcial (FRIA existe, DPIA integrada)

**Artefacto esperado egress-manifest.yml:**
```yaml
# egress-manifest.yml
# Transparencia conectividad externa por componente

components:
  - name: leka-llm-evaluation
    egress_enabled: true
    external_domains:
      - api.openai.com:443         # OpenAI GPT-4 evaluations
      - api.anthropic.com:443      # Claude evaluations
    telemetry_enabled: false       # No telemetry
    data_sent: "prompts_only"      # Solo prompts evaluación (NO metadata sensible)
    data_residency: "EEA"          # Azure OpenAI región UE opcional
    fallback_local: true           # Fallback a modelo local si external falla
  
  - name: leka-bias-detection
    egress_enabled: false          # 100% local
    external_domains: []
    telemetry_enabled: false
  
  - name: backend-java
    egress_enabled: false          # 100% local
    external_domains: []
    telemetry_enabled: false
  
  - name: postgres-timescaledb
    egress_enabled: false          # 100% local
    external_domains: []
```

**Acción:** Generar egress-manifest.yml desde configuración (4-5 horas).

---

## 🔴 PARTE 3: LISTA "ENSÉÑAME" 20 MIN (8 puntos)

### **1. Export 1:1 para registro Art. 71 (Anexo VIII)**

**Estado:** ⚠️ 85% (preparado, falta export)  
**Acción:** Export ZIP (3-4 horas)  
**Prioridad:** 🔴 DEMO

---

### **2. Kill-switch/HITL en runtime + circuit breakers**

**Estado:** ⚠️ **50% (GAP CRÍTICO DEMO)**

**Lo que tenemos:**
- ✅ HITL en workflows (aprobaciones pre-deployment)
- ✅ Stop button conceptual

**Lo que FALTA:**
- 🔴 **Kill-switch RUNTIME** (parar modelo en producción mientras ejecuta)
- 🔴 **Circuit breakers** (auto-stop si threshold)

**Artefacto esperado:**
```python
# API endpoint kill-switch runtime
POST /api/models/{model_id}/emergency-stop
{
  "reason": "Bias detected >15% in production",
  "stopped_by_user_id": 123,
  "override_code": "EMERGENCY_STOP_AUTH_CODE"
}

# Response:
{
  "status": "STOPPED",
  "inference_blocked": true,
  "timestamp": "2025-11-05T14:30:00Z",
  "affected_deployments": ["prod-deployment-1", "prod-deployment-2"]
}

# Circuit breaker automático
class CircuitBreaker:
    def check_threshold(self, model_id, metric, value):
        threshold = get_policy_threshold(model_id, metric)
        
        if value > threshold:
            # Auto-stop modelo
            emergency_stop(model_id, reason=f"{metric} exceeded {threshold}")
            alert_compliance_team(model_id, metric, value)
```

**Acción URGENTE:** Implementar kill-switch básico (1-2 días).

---

### **3. Manifiesto egress/telemetría conmutables**

**Estado:** 🔴 **0% (NUEVO - EXCELENTE IDEA)**

**Acción:** Generar egress-manifest.yml (4-5 horas).

---

### **4. DPIA con pseudonimización, datos sintéticos en baterías prueba**

**Estado:** ✅ **90%**

**Lo que tenemos:**
- ✅ DPIA integrada FRIA (Art. 27.4)
- ✅ Presidio PII detection
- ✅ k-anonymity, l-diversity, t-closeness

**Lo que FALTA:**
- ⚠️ Datos sintéticos generación (mencionar disponible, no crítico demo)

**Demo:**
- Mostrar DPIA wizard
- Mostrar PII detection funcionando
- Mostrar pseudonimización k-anonymity

---

### **5. Política LLM externos: SCC/TIA o region pinning + fallback local**

**Estado:** ⚠️ **60%**

**Lo que tenemos:**
- ✅ Entity TransferMechanisms (SCCs tracking)
- ✅ Configuración modelos (Azure OpenAI región UE)

**Lo que FALTA:**
- ⚠️ Fallback automático a modelo local si external falla
- ⚠️ Policy: "Si OpenAI falla → Usar Llama local"

**Artefacto esperado:**
```yaml
# Política LLM externos
llm_external_policy:
  provider: OpenAI
  region: eu-west-1  # Azure OpenAI UE
  transfer_mechanism: DPA_Microsoft
  fallback_enabled: true
  fallback_model: llama-3-8b-local
  telemetry: false
  data_sent: prompts_only
```

**Acción:** Documentar política + mostrar config (2 horas).

---

### **6. Audit pack por versión con hashes/firmas**

**Estado:** ⚠️ **40% (CRÍTICO DEMO)**

**Lo que tenemos:**
- ✅ Hashes: AUDITLOGS.AUDHASHCHAIN (immutable)
- ⚠️ **Export audit pack PDF:** NO (PROMPTS_02 #1)

**Acción:** Export básico para demo (2-3 días).

---

### **7. Instrucciones de uso (alto riesgo): propósito, limitaciones, contextos no aptos**

**Estado:** ⚠️ **70%**

**Lo que tenemos:**
- ✅ Entity MODELS con campos: MODINTENDED_PURPOSE, MODLIMITATIONS
- ⚠️ **Auto-generación instructions for use PDF:** NO (PROMPTS_02 #1)

**Artefacto esperado:**
```
instructions_for_use_credit_scoring_v2.pdf:

1. PROPÓSITO PREVISTO:
   Evaluación riesgo crediticio para préstamos personales <€50,000

2. LIMITACIONES CONOCIDAS:
   - No usar para hipotecas (fuera alcance)
   - Accuracy degradada si solicitante <25 años (datos insuficientes)
   - No usar decisión única (requiere revisión humana)

3. CONTEXTOS NO APTOS:
   - Préstamos empresariales
   - Evaluación seguros
   - Scoring empleados

4. MEDIDAS SUPERVISIÓN HUMANA:
   - Revisión manual si score borderline (0.45-0.55)
   - Aprobación obligatoria >€30,000
   - Stop button disponible Compliance Officer
```

**Acción:** Template básico instructions for use (1 día).

---

### **8. RBAC/ABAC, segregación por entorno, gestión secretos**

**Estado:** ✅ **95%**

**Lo que tenemos:**
- ✅ RBAC: Entities USERS, ROLES, PERMISSIONS
- ✅ Segregación entornos: DEPLOYMENTS.DEPENVIRONMENT (DEV, STAGING, PRODUCTION)
- ✅ Gestión secretos: Encriptados en BD (API tokens, credentials)

**Demo:**
- Mostrar roles (ML Engineer, Compliance Officer, Auditor)
- Mostrar environments separados
- Mostrar credentials management

---

## 🔥 GAPS CRÍTICOS PARA DEMO (7 DÍAS)

### **GAP 1: Export Audit Pack PDF + JSON** 🔴

**Prioridad:** MÁXIMA (lo menciona 3 veces)

**Qué implementar:**
```python
# Endpoint básico (simplificado para demo)
@app.get("/api/models/{model_id}/export-audit-pack")
async def export_audit_pack(model_id: int):
    """Generate Anexo IV audit pack (básico para demo)"""
    
    # 1. Fetch data
    model = db.get_model(model_id)
    evaluations = db.get_evaluations(model_id)
    fria = db.get_fria(model_id)
    logs = db.get_audit_logs(model_id)
    
    # 2. Generate JSON
    audit_json = {
        "model_id": model_id,
        "model_name": model.name,
        "version": model.version,
        "risk_level": model.risk_level,
        "intended_purpose": model.intended_purpose,
        "metrics": {
            "accuracy": evaluations.accuracy,
            "bias_demographic_parity": evaluations.bias_score,
            "hallucination_rate": evaluations.hallucination
        },
        "fria_assessment": fria.to_dict(),
        "approvals": [a.to_dict() for a in model.approvals],
        "audit_logs_count": len(logs)
    }
    
    # 3. Generate PDF (básico - usar template HTML → PDF)
    pdf_bytes = generate_pdf_from_template(
        template="audit_pack_basic.html",
        data=audit_json
    )
    
    # 4. Create ZIP
    zip_buffer = io.BytesIO()
    with zipfile.ZipFile(zip_buffer, 'w') as zip_file:
        zip_file.writestr(f"AuditPack_v{model.version}.pdf", pdf_bytes)
        zip_file.writestr("audit.json", json.dumps(audit_json, indent=2))
    
    return StreamingResponse(
        io.BytesIO(zip_buffer.getvalue()),
        media_type="application/zip",
        headers={"Content-Disposition": f"attachment; filename=AuditPack_Model_{model_id}.zip"}
    )
```

**Estimación:** 2-3 días (implementación básica para demo)

---

### **GAP 2: Kill-switch Runtime** 🔴

**Prioridad:** ALTA (lo menciona específicamente)

**Qué implementar:**
```python
# Endpoint emergency stop
@app.post("/api/models/{model_id}/emergency-stop")
async def emergency_stop_model(
    model_id: int,
    reason: str,
    stopped_by_user_id: int,
    override_code: str
):
    """Emergency stop modelo en producción (kill-switch)"""
    
    # 1. Verify override code
    if not verify_override_code(override_code, stopped_by_user_id):
        raise HTTPException(status_code=403, detail="Invalid override code")
    
    # 2. Get deployments activos
    deployments = db.get_active_deployments(model_id)
    
    # 3. Stop each deployment
    for deployment in deployments:
        # Set flag STOPPED en BD
        deployment.status = "EMERGENCY_STOPPED"
        deployment.stopped_at = datetime.now()
        deployment.stopped_by = stopped_by_user_id
        deployment.stop_reason = reason
        db.save(deployment)
        
        # Bloquear inferencias (feature flag)
        redis_client.set(f"model:{model_id}:blocked", "true")
    
    # 4. Log inmutable (Art. 19)
    log_audit_event(
        event_type="EMERGENCY_STOP",
        model_id=model_id,
        user_id=stopped_by_user_id,
        reason=reason,
        affected_deployments=[d.id for d in deployments]
    )
    
    # 5. Alert compliance team
    send_alert_compliance(
        title=f"EMERGENCY STOP: Model {model_id}",
        reason=reason,
        stopped_by=stopped_by_user_id
    )
    
    return {
        "status": "STOPPED",
        "model_id": model_id,
        "affected_deployments": len(deployments),
        "timestamp": datetime.now().isoformat()
    }

# Wrapper inference check kill-switch
@app.post("/api/models/{model_id}/infer")
async def inference_with_killswitch(model_id: int, input_data: dict):
    """Inference con check kill-switch"""
    
    # 1. Check si modelo bloqueado
    if redis_client.get(f"model:{model_id}:blocked") == "true":
        raise HTTPException(
            status_code=403,
            detail="Model emergency stopped. Contact compliance team."
        )
    
    # 2. Check circuit breaker
    if check_circuit_breaker_triggered(model_id):
        raise HTTPException(
            status_code=503,
            detail="Circuit breaker triggered. Model temporarily unavailable."
        )
    
    # 3. Execute inference
    result = execute_inference(model_id, input_data)
    
    return result
```

**Estimación:** 1.5-2 días

---

### **GAP 3: egress-manifest.yml** 🟡

**Prioridad:** MEDIA-ALTA (transparencia excelente)

**Acción:** Generar desde configuración componentes (4-5 horas).

---

### **GAP 4: Circuit breakers por umbral** 🔴

**Prioridad:** ALTA

**Qué implementar:**
```python
# Circuit breaker automático
class CircuitBreakerService:
    def check_and_trigger(self, model_id: int):
        """Check métricas vs políticas, auto-stop si excede"""
        
        # 1. Get políticas modelo
        policies = get_model_policies(model_id)
        
        # 2. Get métricas actuales (últimas 100 inferencias)
        recent_metrics = get_recent_metrics(model_id, limit=100)
        
        # 3. Check thresholds
        for policy in policies:
            metric_value = recent_metrics.get(policy.metric_name)
            
            if metric_value and metric_value > policy.threshold:
                # TRIGGER CIRCUIT BREAKER
                logger.error(
                    f"Circuit breaker triggered: {policy.metric_name} "
                    f"= {metric_value} > {policy.threshold}"
                )
                
                # Auto-stop modelo
                emergency_stop_model(
                    model_id=model_id,
                    reason=f"Circuit breaker: {policy.metric_name} exceeded threshold",
                    stopped_by_user_id=SYSTEM_USER_ID,
                    override_code=SYSTEM_OVERRIDE_CODE
                )
                
                return True
        
        return False

# Ejecutar cada 5 min
@scheduler.scheduled(interval=300)  # 5 min
def check_all_circuit_breakers():
    active_models = get_active_production_models()
    
    for model in active_models:
        circuit_breaker_service.check_and_trigger(model.id)
```

**Estimación:** 1-1.5 días

---

## 📋 PLAN URGENTE DEMO (7 DÍAS)

### **Día 1-2: Export Audit Pack (GAP 1)** 🔴

**Prioridad:** MÁXIMA (lo menciona 3 veces)

```
Chat 1 (Python):
- Microservicio básico leka-server-documents
- Endpoint: /api/models/{id}/export-audit-pack
- Template HTML → PDF (básico)
- Export JSON + PDF en ZIP
- AuditPack_v1.12.pdf generado

Artefacto demo:
✅ Download audit pack ZIP
✅ Mostrar PDF (aunque sea simplificado)
✅ Mostrar JSON structured
```

**Estimación:** 2 días (1 chat Python urgente)

---

### **Día 3: Kill-switch + Circuit Breakers (GAP 2)** 🔴

**Prioridad:** ALTA (lo menciona explícitamente)

```
Chat 2 (Python):
- Endpoint POST /api/models/{id}/emergency-stop
- Redis flag model:blocked
- Circuit breaker service (check thresholds cada 5 min)
- Auto-stop si excede umbral
- Alert compliance team

Demo:
✅ Botón "Emergency Stop" en UI
✅ Modelo se bloquea inmediatamente
✅ Inferencias rechazadas con error
✅ Log inmutable del stop
```

**Estimación:** 1.5 días

---

### **Día 4: Exports + Manifests (GAPs 3, 4)** 🟡

```
Chat 3 (Scripts):
- Script export PolicyRegister.csv
- Script export EU-AI-DB Anexo VIII ZIP
- Generar egress-manifest.yml desde config

Artefactos demo:
✅ PolicyRegister.csv download
✅ EU-AI-DB_AnnexVIII_export.zip
✅ egress-manifest.yml visible
```

**Estimación:** 1 día

---

### **Día 5: Instructions for Use Template (GAP 7)** ⚠️

```
Chat 4 (Templates):
- Template instructions_for_use.pdf
- Auto-generación desde MODELS metadata
- Endpoint: /api/models/{id}/export-instructions

Demo:
✅ Generate instructions for use
✅ Mostrar PDF con propósito, limitaciones, contextos no aptos
```

**Estimación:** 1 día

---

### **Día 6-7: Polish + Testing Demo** ✅

```
- Testing end-to-end todos los artefactos
- Preparar script demo (secuencia mostrar)
- Screenshots/videos backup
- Troubleshooting potential issues
```

**Estimación:** 1-2 días

---

## 📊 RESUMEN GAPS vs REALIDAD

| Punto post amigo | Estado | Gap | Urgencia demo | Estimación |
|------------------|--------|-----|---------------|------------|
| **1. Estandariza ciclo** | ✅ 100% | Ninguno | ✅ Listo | 0h |
| **2. Traza cambios + firma** | ✅ 90% | Firma digital | ⚠️ Mencionar roadmap | 0h |
| **3. Audit pack exportable** | ⚠️ 30% | Export PDF/JSON | 🔴 CRÍTICO | 16-24h |
| **4. Políticas as code** | ✅ 80% | Export CSV | ⚠️ Nice-to-have | 2h |
| **5. Workflows FRIA/DPIA** | ✅ 100% | Ninguno | ✅ Listo | 0h |
| **Artefacto: AuditPack PDF** | 🔴 10% | Generación PDF | 🔴 CRÍTICO | 16-24h |
| **Artefacto: PolicyRegister.csv** | ⚠️ 70% | Export CSV | ⚠️ Nice-to-have | 2h |
| **Artefacto: EU-AI-DB ZIP** | ⚠️ 85% | Export ZIP | ⚠️ Nice-to-have | 4h |
| **Artefacto: egress-manifest** | 🔴 0% | Generar YAML | 🟡 Impresionante | 4-5h |
| **Artefacto: dpia_fria_bundle** | ✅ 90% | Bundle ZIP | ⚠️ Nice-to-have | 3h |
| **Lista: Art. 71 export** | ⚠️ 85% | ZIP export | ⚠️ Nice-to-have | 4h |
| **Lista: Kill-switch runtime** | 🔴 50% | Endpoint stop | 🔴 CRÍTICO | 12-16h |
| **Lista: egress-manifest** | 🔴 0% | Generar | 🟡 Impresionante | 4-5h |
| **Lista: DPIA pseudonimización** | ✅ 90% | Sintéticos | ⚠️ OK mencionar | 0h |
| **Lista: LLM externos fallback** | ⚠️ 60% | Fallback auto | ⚠️ Documentar | 2h |
| **Lista: Audit pack hashes** | ⚠️ 40% | Export | 🔴 CRÍTICO | 16-24h |
| **Lista: Instructions for use** | ⚠️ 70% | PDF template | 🟡 Importante | 8h |
| **Lista: RBAC/ABAC** | ✅ 95% | Ninguno | ✅ Listo | 0h |

---

## 🚨 TOP 5 GAPS CRÍTICOS DEMO (Orden prioridad)

| # | Gap | Impacto demo | Esfuerzo | Prioridad |
|---|-----|--------------|----------|-----------|
| **1** | Export Audit Pack PDF + JSON | 🔴 Máximo (lo menciona 3x) | 16-24h | 🔴 DÍA 1-2 |
| **2** | Kill-switch runtime + circuit breakers | 🔴 Alto (específico lista) | 12-16h | 🔴 DÍA 3 |
| **3** | egress-manifest.yml | 🟡 Impresionante (transparencia) | 4-5h | 🟡 DÍA 4 |
| **4** | Instructions for use template | 🟡 Importante (Art. 13) | 8h | 🟡 DÍA 5 |
| **5** | PolicyRegister.csv export | ⚠️ Nice (validación) | 2h | ⚠️ DÍA 4 |

**Total esfuerzo:** 42-55 horas → **5-7 días con 1 chat** o **2-3 días con 2-3 chats paralelos**

---

## ✅ PLAN EJECUCIÓN URGENTE (7 DÍAS HASTA DEMO)

### **SPRINT DEMO (Semana única):**

**Lunes-Martes (Día 1-2):**
- 🔴 **Chat 1 Python:** Export Audit Pack (PROMPTS_02 #1 urgente)
  - Microservicio leka-server-documents básico
  - Template HTML → PDF
  - Export ZIP (PDF + JSON)
  - **Artefacto:** AuditPack_v1.12.pdf ✅

**Miércoles (Día 3):**
- 🔴 **Chat 2 Python:** Kill-switch + Circuit breakers
  - Endpoint emergency-stop
  - Circuit breaker service
  - Redis blocking flags
  - **Artefacto:** Kill-switch funcionando ✅

**Jueves (Día 4):**
- 🟡 **Chat 3 Scripts:** Exports varios
  - PolicyRegister.csv export
  - EU-AI-DB Anexo VIII ZIP
  - egress-manifest.yml generator
  - **Artefactos:** 3 exports funcionando ✅

**Viernes (Día 5):**
- 🟡 **Chat 4 Templates:** Instructions for use
  - Template PDF instructions
  - Auto-generación desde metadata
  - **Artefacto:** instructions_for_use.pdf ✅

**Sábado-Domingo (Día 6-7):**
- ✅ Testing E2E
- ✅ Preparar script demo (secuencia mostrar)
- ✅ Backup screenshots/videos

---

## 🎯 SCRIPT DEMO 20 MIN (Sugerido)

**Secuencia mostrar al amigo:**

**Min 0-3: Introducción + Ciclo completo**
- Dashboard overview
- Ciclo: Experiment → Evaluation → Approval → Deployment
- ✅ YA lo tenemos

**Min 3-6: Trazabilidad + Lineage**
- Lineage tree (modelo → datasets → config)
- Versionado (v1, v2, v3)
- ✅ YA lo tenemos

**Min 6-10: Audit Pack (CRÍTICO)**
- Click "Export Audit Pack"
- Download AuditPack_v1.12.pdf + audit.json
- Mostrar PDF (12 secciones)
- 🔴 IMPLEMENTAR URGENTE

**Min 10-13: Políticas as Code**
- Mostrar políticas (global, modelo, environment)
- Export PolicyRegister.csv
- ⚠️ Implementar export

**Min 13-16: Kill-switch + Circuit breakers**
- Botón "Emergency Stop"
- Modelo se bloquea
- Inferencias rechazadas
- Circuit breaker auto-triggered
- 🔴 IMPLEMENTAR URGENTE

**Min 16-18: Transparencia (egress-manifest)**
- Mostrar egress-manifest.yml
- Componentes con/sin egress
- Telemetry on/off
- 🟡 Impresionante si lo tienes

**Min 18-20: Preguntas + Roadmap**
- Responder preguntas
- Mostrar roadmap (lo que falta)

---

## 📞 DECISIÓN URGENTE

**Tienes 7 días para demo.**

**Opciones:**

**Opción A: Implementar gaps críticos AHORA (recomendado)**
- Día 1-2: Audit Pack export
- Día 3: Kill-switch
- Día 4-5: Resto exports + polish
- **Resultado:** Demo 100% lo que amigo espera ✅

**Opción B: Demo con lo que tienes + roadmap transparente**
- Mostrar: Workflows, FRIA, lineage, dashboards (70% OK)
- Mencionar: Audit pack PDF, kill-switch en roadmap Q4 2025
- **Resultado:** Demo buena pero sin wow factors ⚠️

**Opción C: Hybrid (gaps críticos mínimos)**
- Día 1-2: Audit Pack export (solo este, es el más mencionado)
- Resto: Roadmap transparente
- **Resultado:** Demo 80% con 1 artefacto wow ⚠️

---

**¿Qué prefieres?** 
1. **Implementar urgente gaps (Opción A)** - 5-7 días trabajo intenso
2. **Demo con roadmap transparente (Opción B)** - Sin código nuevo
3. **Audit Pack solo (Opción C)** - 2 días trabajo

**Dime y arrancamos.** ⏰
