# 🚀 PROMPTS MULTI-CHAT PARALELO - MVP STARTER

**Objetivo:** Completar MVP Starter en 48 horas con 7 chats trabajando en paralelo  
**Fecha Inicio:** Octubre 30, 2025 (noche)  
**Fecha Entrega:** Noviembre 1, 2025 (viernes tarde)

---

## 📦 ARQUITECTURA Y CONVENCIONES (LEER ANTES DE USAR PROMPTS)

### **Stack Tecnológico:**
```
Frontend:  ZKoss Framework (MVVM, Java-based UI)
Backend:   Spring Boot + BusinessService (framework EnArt)
ORM:       JPA/Hibernate con framework EnArt
Database:  PostgreSQL
```

### **Estructura de Paquetes:**
```
Entidades JPA:    com.codeflowx.govern.entity.[module].[EntityName]
ViewModels:       com.codeflowx.platform.viewmodel.[module].[EntityName]OverviewViewModel
ZUL Files:        src/main/webapp/console/platform/[module]/[entity]/[action].zul
```

### **Naming Conventions:**
```
Tablas:    PREFIX_ENTITYNAME (ej: MODMODELS, MODMODELAPPROVALS)
PKs:       IDX + nombre (ej: IDXMODEL, IDXMODELAPPROVAL)
Campos:    PREFIX + nombre (ej: MODNAME, MODSTATUS, MODTYPE)
```

### **Entidades JPA YA EXISTENTES (NO CREAR):**
```
✅ Model               → MODMODELS
✅ ModelApproval       → MODMODELAPPROVALS
✅ ModelBiasAnalysis   → MODMODELBIASANALYSES
✅ ModelPerformance    → MODMODELPERFORMANCES
```

### **Persistencia:**
```
✅ Usar: BusinessService.save(), BusinessService.find()
❌ NO usar: EntityManager, @Repository directo
```

---

## 📋 DISTRIBUCIÓN DE TRABAJO

```
CHAT 1: Detalle Modelo (modo VIEW)
CHAT 2: Análisis Sesgo ViewModel
CHAT 3: Sistema Aprobaciones Completo
CHAT 4: Compliance Dashboard Fix
CHAT 5: Python Service Verificación + Cliente REST Java ⚠️ (Servicio YA EXISTE)
CHAT 6: Testing Scripts + Data
CHAT 7: Documentación + Demo Script
```

---

## 💬 CHAT 1: DETALLE MODELO (Modo VIEW)

### **Contexto para dar al chat:**

```
CONTEXTO:
Estoy desarrollando plataforma AI Governance en Java Spring Boot + ZKoss Framework.

Tengo:
- ✅ Pantalla crear modelo: src/main/webapp/console/platform/models/create/page.zul
- ✅ ViewModel: com.codeflowx.platform.viewmodel.models.ModelDetailViewModel.java
- ✅ Entidad JPA: com.codeflowx.govern.entity.models.Model

Necesito:
- Pantalla detalle modelo (solo lectura) con compliance checklist

Arquitectura:
- Framework: ZKoss MVVM
- Backend: Spring Boot + BusinessService (framework propio)
- Base datos: PostgreSQL
- ORM: JPA/Hibernate con framework enart
```

### **PROMPT ESPECÍFICO:**

```
Necesito crear pantalla detalle modelo en ZKoss Framework con las siguientes características:

OPCIÓN 1 (Preferida - Reutilizar):
Modificar create/page.zul para soportar dos modos:
- mode=EDIT (actual - permite editar)
- mode=VIEW (nuevo - solo lectura)

OPCIÓN 2 (Alternativa):
Crear nueva pantalla detail/page.zul dedicada

FUNCIONALIDAD REQUERIDA:

1. INFORMACIÓN MODELO (Solo lectura):
   - Nombre modelo
   - Descripción
   - Tipo (Classification, LLM, etc.)
   - Framework (TensorFlow, PyTorch, etc.)
   - Use Case
   - Risk Level (badge con color)
   - Estado (badge: Draft, Approved, Production, Rejected)
   - Owner (usuario creador)
   - Fecha creación

2. SECCIÓN COMPLIANCE CHECKLIST:
   Mostrar checklist 6 items EU AI Act:
   ☐ Risk Classification documentada (✅/❌)
   ☐ Dataset Quality validado (✅/❌)
   ☐ Bias Analysis realizado (✅/❌)
   ☐ Performance Metrics documentadas (✅/❌)
   ☐ Modelo aprobado (✅/❌)
   ☐ Audit Trail disponible (✅/❌)
   
   Score: X/6 checks passed
   Badge: 🟢 COMPLIANT (6/6) | 🟡 PARTIAL (4-5) | 🔴 NON-COMPLIANT (<4)

3. SECCIÓN ACCIONES:
   Botones:
   - "Editar Modelo" (va a create/page.zul?mode=EDIT)
   - "Analizar Sesgo" (va a bias-analysis)
   - "Enviar a Aprobación" (si estado = DRAFT)
   - "Ver Audit Trail"
   - "Eliminar" (confirmación)

4. NAVEGACIÓN:
   - Breadcrumbs: Home > Modelos > [Nombre Modelo]
   - Botón volver a lista

VIEWMODEL:
Reutilizar ModelDetailViewModel.java existente:
- Ubicación: com.codeflowx.platform.viewmodel.models.ModelDetailViewModel

Métodos adicionales necesarios:
- getComplianceChecklist() → retorna List<ComplianceCheck>
- getComplianceScore() → retorna "5/6"
- getComplianceStatus() → retorna COMPLIANT/PARTIAL/NON_COMPLIANT

ENTIDAD JPA:
- Paquete: com.codeflowx.govern.entity.models.Model
- Tabla: MODMODELS
- PK: IDXMODEL (Long)
- Campos importantes:
  * MODNAME (String) - Nombre modelo
  * MODTYPE (String) - Tipo (Classification, LLM, etc.)
  * MODFRAMEWORK (String) - Framework (TensorFlow, PyTorch, etc.)
  * MODSTATUS (String) - Estado (DRAFT, IN_REVIEW, APPROVED, REJECTED, PRODUCTION)
  * MODRISKLEVEL (String) - Nivel riesgo (LOW, MEDIUM, HIGH)
  * MODDESCRIPTION (CLOB) - Descripción

Por favor genera:
1. ZUL file completo (opción que prefieras)
2. Modificaciones necesarias en ViewModel
3. Ejemplo ComplianceCheck DTO si necesario
4. Instrucciones integración

Hazlo siguiendo buenas prácticas ZKoss MVVM y Spring Boot.
```

---

## 💬 CHAT 2: ANÁLISIS SESGO VIEWMODEL

### **PROMPT ESPECÍFICO:**

```
Necesito crear ViewModel ZKoss para análisis de sesgo en modelos ML.

CONTEXTO:
- Framework: ZKoss MVVM + Spring Boot + BusinessService (framework propio)
- ZUL existente: src/main/webapp/console/platform/models/bias-analysis/overview.zul
- Python Service endpoint: POST http://localhost:8001/api/bias-analysis/analyze
- Entidad JPA YA EXISTE: com.codeflowx.govern.entity.evaluation.ModelBiasAnalysis
- Tabla: MODMODELBIASANALYSES (YA EXISTE)

FUNCIONALIDAD REQUERIDA:

1. SELECCIÓN MODELO:
   - Dropdown con lista modelos del sistema
   - Mostrar: nombre, tipo, owner

2. UPLOAD CSV:
   - File upload component (ZKoss fileupload)
   - Validar: Debe tener columnas [y_true, y_pred, protected_attribute]
   - Mostrar preview primeras 5 filas

3. CONFIGURACIÓN ANÁLISIS:
   - Protected attribute: Dropdown (gender, age, race, geography, etc.)
   - Favorable outcome: Textbox (ej: "1" = aprobado)
   - Threshold fairness: Slider o textbox (default 0.8)

4. EJECUTAR ANÁLISIS:
   - Botón "Analizar Sesgo"
   - Envía CSV + config a Python service
   - Muestra loading mientras procesa
   - Timeout: 30 segundos

5. MOSTRAR RESULTADOS:
   Después de análisis, mostrar:
   
   A) MÉTRICAS FAIRNESS:
      - Demographic Parity Difference: [valor]
      - Equal Opportunity Difference: [valor]
      - Disparate Impact Ratio: [valor]
   
   B) CLASIFICACIÓN:
      - Badge: NO_BIAS / LOW / MODERATE / HIGH / CRITICAL
      - Color: Verde/Amarillo/Naranja/Rojo según severidad
   
   C) GRÁFICO (simple):
      - Bar chart: Performance por grupo protegido
      - Usa Chart.js o ZKoss Charts
   
   D) RECOMENDACIONES:
      - Texto simple: "Sesgo detectado en grupo X. Recomendar reentrenamiento."

6. GUARDAR ANÁLISIS:
   - Botón "Guardar Análisis"
   - Persiste usando BusinessService en tabla MODMODELBIASANALYSES
   - Entidad: ModelBiasAnalysis
   - Campos clave:
     * IDXMODELBIASANALYSIS (PK, Long)
     * MODBIASTYPE (String) - Tipo de sesgo
     * MODSEVERITY (String) - Severidad (NO_BIAS, LOW, MODERATE, HIGH, CRITICAL)
     * MODBIASDESCRIPTION (CLOB) - Descripción y recomendaciones
     * MODBIASMETRICS (JSON/CLOB) - Métricas detalladas
   - FK a modelo: Relación ManyToOne con Model

PYTHON SERVICE RESPONSE EJEMPLO:
```json
{
  "metrics": {
    "demographic_parity_difference": 0.15,
    "equal_opportunity_difference": 0.12,
    "disparate_impact_ratio": 0.78
  },
  "classification": "MODERATE",
  "recommendations": "Detected bias in gender attribute. Consider retraining with balanced dataset.",
  "groups_analysis": [
    {"group": "male", "accuracy": 0.85, "count": 1000},
    {"group": "female", "accuracy": 0.70, "count": 800}
  ]
}
```

Por favor genera:
1. ModelBiasAnalysisOverviewViewModel.java completo
   - Ubicación: com.codeflowx.platform.viewmodel.models.ModelBiasAnalysisOverviewViewModel
   - Usar BusinessService para persistencia
   - Usar entidad ModelBiasAnalysis (ya existe)
2. Métodos CRUD usando BusinessService (no JPA directo)
3. Integración con PythonMLServiceClient (Chat 5):
   - @Autowired private PythonMLServiceClient mlServiceClient
   - Usar mlServiceClient.analyzeBias(csvFile, modelId, protectedAttribute)
   - NO crear llamadas HTTP directas (usar cliente de Chat 5)
4. Manejo errores (service no disponible, timeout, CSV inválido)
5. Logging con @Slf4j (Lombok)

⚠️ NOTA: Chat 5 creará PythonMLServiceClient. Este ViewModel debe INYECTARLO y usarlo.

IMPORTANTE:
- NO crear entidades JPA (ya existen)
- NO crear SQL scripts (tabla ya existe)
- Usar BusinessService.save() para persistir
- Usar BusinessService.find() para consultar

Usa buenas prácticas ZKoss MVVM, manejo excepciones, y logging.
```

---

## 💬 CHAT 3: SISTEMA APROBACIONES COMPLETO

### **PROMPT ESPECÍFICO:**

```
Necesito implementar sistema completo de aprobaciones de modelos ML.

CONTEXTO:
- Framework: ZKoss MVVM + Spring Boot + BusinessService
- Entidad Modelo: com.codeflowx.govern.entity.models.Model (Tabla: MODMODELS)
- Entidad Aprobación YA EXISTE: com.codeflowx.govern.entity.models.ModelApproval (Tabla: MODMODELAPPROVALS)
- Roles: ML_ENGINEER (crea modelos), GOVERNANCE_ADMIN (aprueba)

WORKFLOW APROBACIÓN:
1. ML Engineer crea modelo (estado = DRAFT)
2. ML Engineer click "Enviar a Aprobación" → estado = IN_REVIEW
3. Governance Admin ve lista modelos IN_REVIEW
4. Governance Admin aprueba/rechaza
5. Estado cambia: IN_REVIEW → APPROVED o REJECTED
6. (Opcional) Email notificación a ML Engineer

COMPONENTES A CREAR:

1. VIEWMODEL: ModelApprovalOverviewViewModel.java
   
   Métodos necesarios:
   - @Init: Cargar modelos con status=IN_REVIEW
   - List<Model> getPendingApprovals()
   - @Command approveModel(Long modelId, String comments)
   - @Command rejectModel(Long modelId, String rejectionReason)
   - @NotifyChange actualizaciones automáticas
   
   Validaciones:
   - Solo usuarios con rol GOVERNANCE_ADMIN pueden aprobar
   - Rejection reason es OBLIGATORIO al rechazar
   - Comments opcional al aprobar

2. ZUL: src/main/webapp/console/platform/models/approval/overview.zul
   
   Layout:
   - Título: "Modelos Pendientes de Aprobación"
   - Tabla/Grid con columnas:
     ├─ Nombre modelo (Model.MODNAME)
     ├─ Tipo (Model.MODTYPE)
     ├─ Owner (quién lo creó)
     ├─ Risk Level (Model.MODRISKLEVEL - badge)
     ├─ Fecha solicitud (ModelApproval.MODREQUESTEDDATE)
     ├─ Estado aprobación (ModelApproval.MODAPPROVALSTATUS)
     └─ Acciones: [Ver Detalle] [Aprobar] [Rechazar]
   
   Botón "Aprobar":
   - Click → Modal confirmación
   - Textarea comentarios (opcional)
   - Botones: "Confirmar Aprobación" / "Cancelar"
   
   Botón "Rechazar":
   - Click → Modal con textarea OBLIGATORIA
   - Label: "Razón del rechazo (obligatorio)"
   - Botones: "Confirmar Rechazo" / "Cancelar"
   
   Filtros (opcional):
   - Por risk level
   - Por owner

3. INTEGRACIÓN EN DETALLE MODELO:
   
   Añadir a ModelDetailViewModel.java:
   - @Command submitForApproval()
     └─ Cambiar status: DRAFT → IN_REVIEW
     └─ Guardar en BD
     └─ Notificar (opcional)
   
   Añadir a detail/page.zul:
   - Botón "Enviar a Aprobación"
   - Solo visible si: status = DRAFT && currentUser = owner
   - Click → Confirmación → submitForApproval()

4. EMAIL NOTIFICACIÓN (opcional, puede ser log):
   - Al aprobar: Email a model owner
   - Al rechazar: Email con razón rechazo
   - Template simple texto plano

ENTIDAD MODELAPPROVAL (YA EXISTE):
- Tabla: MODMODELAPPROVALS
- PK: IDXMODELAPPROVAL (Long)
- Campos clave:
  * MODAPPROVALTYPE (String) - NEW_MODEL, VERSION_UPDATE, REDEPLOYMENT
  * MODAPPROVALSTATUS (String) - PENDING, UNDER_REVIEW, APPROVED, REJECTED, CONDITIONAL
  * MODTARGETENVIRONMENT (String) - DEVELOPMENT, STAGING, PRODUCTION
  * MODREQUESTREASON (CLOB) - Razón solicitud
  * MODAPPROVALCOMMENTS (CLOB) - Comentarios aprobador
  * MODREJECTIONREASON (CLOB) - Razón rechazo
  * MODREQUESTEDDATE (Timestamp) - Fecha solicitud
  * MODAPPROVEDREJECTEDDATE (Timestamp) - Fecha decisión
  * FK: Relación ManyToOne con Model

Por favor genera:
1. ModelApprovalOverviewViewModel.java completo
   - Ubicación: com.codeflowx.platform.viewmodel.models.ModelApprovalOverviewViewModel
   - Usar BusinessService para persistencia
   - Listar ModelApproval con status=PENDING o UNDER_REVIEW
2. approval/overview.zul completo
   - Ubicación: src/main/webapp/console/platform/models/approval/overview.zul
3. Modificaciones ModelDetailViewModel.java (submitForApproval)
   - Crear registro ModelApproval
   - Cambiar Model.MODSTATUS → IN_REVIEW
4. (Opcional) Email service simple o logging

IMPORTANTE:
- NO crear entidades JPA (ModelApproval ya existe)
- NO crear SQL scripts (tabla ya existe)
- Usar BusinessService.save() para persistir
- Usar BusinessService.find() con Criterias para consultar

Usa buenas prácticas, manejo excepciones, validaciones.
```

---

## 💬 CHAT 4: COMPLIANCE DASHBOARD FIX

### **PROMPT ESPECÍFICO:**

```
Necesito verificar y corregir dashboard de compliance que puede tener errores de binding.

CONTEXTO:
- Framework: ZKoss MVVM + BusinessService
- ZUL: src/main/webapp/console/platform/governance/compliance/page.zul (EXISTE)
- ViewModel: com.codeflowx.platform.viewmodel.governance.ComplianceAssessmentOverviewViewModel.java (EXISTE)

PROBLEMA:
Posible desajuste entre propiedades ZUL y ViewModel, no sabemos si funciona.

TAREA:

1. ANÁLISIS:
   Por favor analiza estos dos archivos (los proporcionaré) y verifica:
   - ¿Binding ZUL → ViewModel es correcto?
   - ¿Propiedades referenciadas en ZUL existen en ViewModel?
   - ¿Métodos @Command tienen @NotifyChange correcto?
   - ¿Hay errores sintaxis ZUL?

2. CORRECCIÓN:
   Si encuentras errores, genera versiones corregidas de:
   - ZUL file (con bindings correctos)
   - ViewModel (con propiedades/métodos faltantes)

3. FUNCIONALIDAD DEBE SER:
   
   DASHBOARD MUESTRA:
   - KPI 1: Total modelos registrados (número)
   - KPI 2: % Modelos compliant (ej: "75%")
   - KPI 3: Modelos en producción sin compliance (número, alerta roja)
   - KPI 4: Análisis sesgo realizados este mes (número)
   
   TABLA: Modelos NON-COMPLIANT
   - Columnas: Nombre, Owner, Compliance Score (X/6), Qué falta
   - Click fila → Va a detalle modelo
   
   GRÁFICO (opcional):
   - Pie chart: Compliant vs Non-Compliant
   - Datos desde ViewModel

4. VIEWMODEL DEBE TENER:
   - @Init cargar datos
   - getTotalModels() → Integer
   - getCompliancePercentage() → String "75%"
   - getNonCompliantModels() → List<Model>
   - getModelsInProductionNonCompliant() → Integer

Por favor:
1. Lista errores potenciales que encuentres
2. Genera versiones corregidas
3. Incluye comentarios explicando cambios

[DESPUÉS PEGA AQUÍ LOS ARCHIVOS ZUL Y VIEWMODEL EXISTENTES]
```

**NOTA:** Una vez chat responda, copia los archivos actuales y el chat los analizará.

---

## 💬 CHAT 5: PYTHON BIAS SERVICE - VERIFICACIÓN E INTEGRACIÓN

### **CONTEXTO CRÍTICO:**

```
⚠️ EL SERVICIO PYTHON YA EXISTE Y ESTÁ 100% COMPLETO - PRODUCCIÓN READY

Ubicación: /mnt/c/Users/ManuelGonzalez/git/leka-bias-detection-service/

Estado Actual (Ver DELIVERABLES_V2_COMPLETE.md):
✅ Servicio FastAPI completo con 10 módulos
✅ 12 endpoints funcionales (NO placeholders)
✅ 4 módulos 100% funcionales: Bias, Drift, Quality, Explainability
✅ 6 módulos documentados (ready para implementación completa)
✅ Endpoint bias: POST http://localhost:8001/api/bias-analysis/analyze
✅ Docker compose configurado y probado
✅ Documentación completa en API_COMPLETE_DOCS.md (644 líneas)
✅ OpenAPI docs en http://localhost:8001/docs
✅ Postman Collection: 26 archivos (20 CSVs test incluidos)
✅ Sin placeholders - Todo implementado
✅ EU AI Act + GDPR compliant

Archivos Existentes:
- main.py (1,500+ líneas - FastAPI app principal)
- services/drift_detection_service.py (288 líneas)
- services/data_quality_service.py (378 líneas)
- services/explainability_service.py (278 líneas)
- services/__init__.py
- requirements.txt (dependencias básicas funcionales)
- Dockerfile (production-ready)
- docker-compose.yml
- API_COMPLETE_DOCS.md (644 líneas)
- Postman collection + 20 CSVs de prueba
- 6 documentos MD de documentación
```

### **PROMPT ESPECÍFICO:**

```
Necesito VERIFICAR que el servicio Python funciona y crear CLIENTE REST JAVA para integrarlo con el backend Spring Boot.

TAREA 1: VERIFICAR SERVICIO PYTHON FUNCIONA

1. VERIFICAR SERVICIO ESTÁ CORRIENDO:
   - Comando: docker-compose up -d (o python main_v2_complete.py)
   - URL: http://localhost:8001/docs
   - Verificar endpoint POST /api/bias-analysis/analyze existe

2. PROBAR ENDPOINT CON DATOS TEST:
   - Crear CSV test mínimo (10 filas):
     * Columnas: y_true, y_pred, protected_attribute (gender)
     * y_true: 0 o 1
     * y_pred: 0 o 1 (con sesgo)
     * gender: "male" o "female"
   
   - Llamada curl:
     ```bash
     curl -X POST "http://localhost:8001/api/bias-analysis/analyze" \
       -F "file=@test_bias.csv" \
       -F "model_id=test_model_123" \
       -F "protected_attribute=gender"
     ```
   
   - Verificar response JSON correcto:
     ```json
     {
       "metrics": {...},
       "classification": "MODERATE" | "LOW" | etc.,
       "recommendations": "...",
       "groups_analysis": [...]
     }
     ```

3. VERIFICAR OTROS ENDPOINTS FUNCIONALES (opcional):
   - POST /api/data-quality/validate
   - POST /api/drift/detect
   - POST /api/explainability/explain

TAREA 2: CREAR CLIENTE REST JAVA

Necesito clase Java que llame al servicio Python desde Spring Boot.

REQUISITOS:
- Framework: Spring Boot + RestTemplate o WebClient
- Configuración: URL base desde application.properties
- Manejo errores: timeout, service down, invalid response
- Logging: @Slf4j con Lombok

CLASE A CREAR:
- Nombre: PythonMLServiceClient.java
- Paquete: com.codeflowx.platform.integration.ml
- Métodos necesarios:

  1. analyzeBias(MultipartFile csvFile, String modelId, String protectedAttribute) 
     → ModelBiasAnalysisResponse
   
  2. validateDataQuality(MultipartFile csvFile, String targetColumn)
     → DataQualityResponse (opcional, para futuro)
   
  3. detectDrift(MultipartFile referenceFile, MultipartFile currentFile, ...)
     → DriftDetectionResponse (opcional, para futuro)

DTOs A CREAR:
- ModelBiasAnalysisResponse.java:
  ```java
  public class ModelBiasAnalysisResponse {
    private Map<String, Double> metrics;
    private String classification; // NO_BIAS, LOW, MODERATE, HIGH, CRITICAL
    private String recommendations;
    private List<GroupAnalysis> groupsAnalysis;
    // getters/setters
  }
  
  public class GroupAnalysis {
    private String group;
    private Double accuracy;
    private Double precision;
    private Double recall;
    private Integer count;
    // getters/setters
  }
  ```

CONFIGURACIÓN application.properties:
```properties
# Python ML Service
ml.service.python.url=http://localhost:8001
ml.service.python.timeout=30000
ml.service.python.enabled=true
```

MANEJO ERRORES:
- TimeoutException → Log error + retry 1 vez (opcional)
- HttpClientErrorException (400, 413) → Validar CSV antes de enviar
- HttpServerErrorException (500, 502) → Log error + notificar admin
- ConnectException → Service no disponible, mensaje claro a usuario

TAREA 3: INTEGRAR CON VIEWMODEL

Modificar ModelBiasAnalysisOverviewViewModel.java (Chat 2) para usar el cliente:

```java
@Autowired
private PythonMLServiceClient mlServiceClient;

@Command
public void executeBiasAnalysis() {
  // Usar mlServiceClient.analyzeBias(csvFile, modelId, protectedAttribute)
  // Guardar resultado usando BusinessService
}
```

Por favor genera:
1. PythonMLServiceClient.java completo
2. DTOs: ModelBiasAnalysisResponse.java, GroupAnalysis.java
3. Configuración application.properties (sección ML service)
4. Script test_bias.csv (CSV mínimo 10 filas con sesgo MODERATE)
5. Test unitario PythonMLServiceClientTest.java (mock RestTemplate)
6. Documentación integración: cómo usar cliente en ViewModels

IMPORTANTE:
- NO modificar código Python (ya funciona)
- Solo crear cliente Java + DTOs
- Usar RestTemplate o WebClient (Spring Boot estándar)
- Manejo errores robusto
- Logging completo

Ejemplo uso en ViewModel:
```java
@Autowired
private PythonMLServiceClient mlServiceClient;

@Command
public void analyzeBias() {
  try {
    ModelBiasAnalysisResponse response = mlServiceClient.analyzeBias(
      csvFile, model.getId().toString(), protectedAttribute
    );
    
    // Persistir resultado
    ModelBiasAnalysis bias = new ModelBiasAnalysis();
    bias.setSeverity(response.getClassification());
    bias.setDescription(response.getRecommendations());
    // ... mapear response → entity
    
    businessService.save(bias);
  } catch (Exception e) {
    log.error("Error calling Python service", e);
    // Mostrar mensaje error a usuario
  }
}
```

Usa buenas prácticas Spring Boot, manejo excepciones, y logging.
```

---

## 💬 CHAT 6: TESTING SCRIPTS + DATA

### **PROMPT ESPECÍFICO:**

```
Necesito scripts de testing y datos de prueba para MVP de plataforma AI Governance.

COMPONENTES A TESTEAR:

1. BACKEND API ENDPOINTS:
   - POST /api/models (crear modelo)
   - GET /api/models (listar)
   - GET /api/models/{id} (detalle)
   - PUT /api/models/{id} (actualizar)
   - POST /api/models/{id}/submit-approval (enviar a aprobación)
   - POST /api/approval/approve/{id}
   - POST /api/approval/reject/{id}
   - POST /api/bias-analysis/analyze

2. DATOS TEST NECESARIOS:

   A) CSV BIAS ANALYSIS (test_bias_data.csv):
      Generar CSV con 1000 filas:
      - y_true: 0 o 1 (random pero balanceado)
      - y_pred: 0 o 1 (con sesgo hacia grupo)
      - gender: "male" o "female" (50/50)
      - Debe mostrar MODERATE bias cuando se analiza
   
   B) USUARIOS TEST (SQL insert):
      - admin@test.com (rol: ADMIN)
      - mlengineer@test.com (rol: ML_ENGINEER)
      - governance@test.com (rol: GOVERNANCE_ADMIN)
      - viewer@test.com (rol: VIEWER)
      Password: "test123" (todos)
   
   C) MODELOS TEST (SQL insert):
      - 3 modelos DRAFT (para probar aprobación)
      - 2 modelos APPROVED
      - 1 modelo IN_REVIEW
      - Asignados a diferentes owners

3. SCRIPTS TESTING:

   A) test_api_endpoints.py (pytest):
      - Test crear modelo
      - Test enviar a aprobación
      - Test aprobar modelo
      - Test análisis sesgo
   
   B) test_workflow_completo.sh (bash):
      - Secuencia completa:
        1. Login ML Engineer
        2. Crear modelo
        3. Analizar sesgo (upload CSV)
        4. Enviar a aprobación
        5. Login Governance
        6. Aprobar modelo
        7. Verificar estado = APPROVED
   
   C) demo_data_loader.sql:
      - Script SQL para cargar datos demo
      - Trunca tablas (safe)
      - Inserta usuarios + modelos + análisis ejemplo

Por favor genera:
1. CSV test con sesgo MODERATE
2. SQL script usuarios y modelos test
3. pytest script testing APIs
4. Bash script test workflow completo
5. README cómo ejecutar tests

Todo listo para ejecutar y verificar MVP funciona.
```

---

## 💬 CHAT 7: DOCUMENTACIÓN Y DEMO SCRIPT

### **PROMPT ESPECÍFICO:**

```
Necesito documentación y script de demo para MVP de AI Governance Platform.

CONTEXTO:
Plataforma permite:
- Registrar modelos ML
- Analizar sesgo automáticamente
- Aprobar/rechazar modelos
- Ver compliance dashboard

DELIVERABLES:

1. DEMO SCRIPT 15 MINUTOS (Markdown):
   
   Estructura:
   ```
   MINUTO 0-2: Problema (slides)
   MINUTO 2-4: Login + Dashboard
   MINUTO 4-7: Crear modelo + Analizar sesgo
   MINUTO 7-10: Workflow aprobación
   MINUTO 10-12: Compliance dashboard
   MINUTO 12-15: Pricing + Q&A
   ```
   
   Para cada sección:
   - Qué decir (script verbal)
   - Qué hacer (acciones en plataforma)
   - Qué mostrar (pantallas específicas)
   - Backup plan (si algo falla)

2. QUICK START GUIDE (Markdown):
   
   Para cliente nuevo:
   - Cómo hacer login
   - Cómo registrar primer modelo
   - Cómo analizar sesgo
   - Cómo aprobar modelo
   - Cómo ver compliance
   
   Con screenshots placeholders [SCREENSHOT: nombre]

3. API DOCUMENTATION (básica):
   
   Para cada endpoint crítico:
   - URL, método HTTP
   - Headers requeridos
   - Request body ejemplo
   - Response ejemplo
   - Códigos error posibles

4. ONE-PAGER COMERCIAL:
   
   1 página PDF-ready (Markdown → PDF):
   - Problema (3 bullets)
   - Solución (nuestra plataforma)
   - Features MVP Starter (lista)
   - Pricing: €20-25K/año
   - Early adopter: 50% descuento
   - Call to action: "Piloto gratis 30 días"

5. EMAIL TEMPLATES:
   
   A) Email prospección:
      Subject: "EU AI Act Compliance - Piloto Gratis 30 Días"
      Body: Corto, problema, solución, CTA
   
   B) Email follow-up demo:
      Después de demo, agradecer + próximos pasos
   
   C) Email propuesta comercial:
      Adjuntar one-pager + pricing

Por favor genera todos estos documentos listos para usar.
Formato profesional, claro, conciso.
```

---

## 💬 CHAT COORDINADOR (TÚ): PLAN DE INTEGRACIÓN

### **Tu Workflow de Integración:**

```
FASE 1: GENERACIÓN CÓDIGO (Hoy noche, 4 horas)

20:00 - 20:30:
└─ Lanzar 7 chats con prompts
   └─ Monitorizar que entiendan bien

20:30 - 24:00:
└─ Revisar código generado por cada chat
└─ Hacer preguntas aclaratorias
└─ Pedir ajustes si necesario
└─ Descargar/copiar código

FASE 2: INTEGRACIÓN (Mañana, 8 horas)

09:00 - 13:00:
├─ Integrar ViewModels generados
├─ Integrar ZULs generados
├─ Ejecutar SQL migrations
├─ Configurar routing
└─ Primera pasada lanzamiento

14:00 - 18:00:
├─ LANZAR APLICACIÓN
├─ Verificar pantalla por pantalla
├─ Anotar TODOS los errores
├─ Distribuir errores entre chats
└─ Chats generan fixes en paralelo

19:00 - 21:00:
├─ Aplicar fixes generados
├─ Relanzar app
├─ Verificar fixes funcionan
└─ Repetir ciclo si quedan errores

FASE 3: TESTING FINAL (Viernes, 6 horas)

09:00 - 12:00:
└─ Test funcional completo:
   ├─ Flujo crear → sesgo → aprobar → compliance
   ├─ Diferentes roles
   └─ Casos edge

13:00 - 15:00:
└─ Preparar demo:
   ├─ Cargar datos test
   ├─ Rehearsal demo 15 min
   └─ Screenshots/video

15:00 - 17:00:
└─ Buffer para bugs de última hora
```

---

## 📊 TRACKING PROGRESS (Google Sheet o Notion)

### **Crea tabla con:**

```
CHAT | TAREA | ESTADO | CÓDIGO RECIBIDO | INTEGRADO | FUNCIONA | ERRORES
-----|-------|--------|-----------------|-----------|----------|--------
  1  | Detalle modelo | ⏳ En progreso | ☐ | ☐ | ☐ | -
  2  | Sesgo ViewModel | ⏳ En progreso | ☐ | ☐ | ☐ | -
  3  | Aprobaciones | ⏳ En progreso | ☐ | ☐ | ☐ | -
  4  | Compliance fix | ⏳ En progreso | ☐ | ☐ | ☐ | -
  5  | Python service | ⏳ En progreso | ☐ | ☐ | ☐ | -
  6  | Testing | ⏳ En progreso | ☐ | ☐ | ☐ | -
  7  | Docs | ⏳ En progreso | ☐ | ☐ | ☐ | -
```

**Actualiza cada vez que:**
- Chat entrega código (marca "Código recibido")
- Integras en proyecto (marca "Integrado")
- Lanzas y funciona (marca "Funciona")
- Encuentras errores (anota en columna "Errores")

---

## 🎯 CRITERIOS DONE (Checklist Final)

### **MVP está LISTO cuando puedas marcar todas:**

```
☐ Login funciona (admin, ml_engineer, governance roles)
☐ Dashboard principal carga sin errores
☐ Puedo crear modelo nuevo y aparece en lista
☐ Puedo ver detalle modelo con compliance checklist
☐ Puedo analizar sesgo (upload CSV, ver resultados)
☐ Puedo enviar modelo a aprobación (estado cambia)
☐ Como governance, veo lista pendientes aprobación
☐ Puedo aprobar modelo (estado → APPROVED)
☐ Puedo rechazar modelo con razón (estado → REJECTED)
☐ Compliance dashboard muestra KPIs correctos
☐ Flujo completo sin crashes: crear → sesgo → aprobar → compliance
☐ Demo 15 min funciona sin errores mayores
☐ Python bias service responde en <10 seg
```

**Cuando 12/13 = ✅ → MVP LISTO PARA VENTA**

---

## 📞 COORDINACIÓN MULTI-CHAT

### **Tips para Máxima Eficiencia:**

```
1. USA NOMBRES CLAROS para cada chat:
   - "Chat Detalle Modelo"
   - "Chat Sesgo"
   - "Chat Aprobaciones"
   etc.

2. MANTÉN CONTEXTO:
   - Cada chat debe saber:
     * Framework (ZKoss MVVM)
     * Tabla base (MODMODELS)
     * Roles sistema
     * Convenciones nombres

3. COMPARTE CÓDIGO ENTRE CHATS si necesario:
   - Ej: Chat 3 (aprobaciones) necesita saber nombres columnas
   - Copia DDL tabla MODMODELS a ese chat

4. TESTING INCREMENTAL:
   - No esperes tener todo
   - Integra chat por chat
   - Verifica cada uno funciona antes de siguiente

5. ERRORES COMUNES:
   - Nombres clases no coinciden
   - Paths ZUL incorrectos
   - Bindings typos
   - FK tablas no creadas
   
   → Copia error exacto a chat, pedirá fix específico
```

---

## 🚀 ORDEN DE EJECUCIÓN RECOMENDADO

### **Secuencia óptima:**

```
PARALELO GRUPO 1 (Independientes):
├─ Chat 5: Verificar Python Service + Cliente REST Java (servicio YA EXISTE ✅)
├─ Chat 6: Testing scripts (puede preparar solo)
└─ Chat 7: Documentación (puede escribir solo)

PARALELO GRUPO 2 (Dependen de MODMODELS):
├─ Chat 1: Detalle modelo (solo lectura, simple)
├─ Chat 2: Sesgo ViewModel (upload + llamar Python)
└─ Chat 4: Compliance fix (dashboard KPIs)

SECUENCIAL (Depende de anteriores):
└─ Chat 3: Aprobaciones (último, depende detalle modelo estar listo)

INTEGRACIÓN:
Tú vas integrando en orden:
1. Cliente REST Java (Chat 5) - puede ir primero, independiente
2. Detalle modelo (Chat 1 - simple)
3. Sesgo ViewModel (Chat 2 - usa Cliente REST de Chat 5)
4. Compliance dashboard (Chat 4 - depende sesgo)
5. Aprobaciones (Chat 3 - depende detalle modelo)
6. Testing scripts (Chat 6 - al final)
7. Docs (Chat 7 - última)

⚠️ NOTA: Python Service ya existe y funciona, solo necesitas verificar que corre y crear cliente Java.
```

---

## 💡 PROMPTS ADICIONALES SI NECESITAS

### **Si Chat no entiende:**

```
PROMPT CLARIFICACIÓN:

"Déjame darte más contexto sobre la arquitectura:

STACK COMPLETO:
- Frontend: ZKoss Framework (Java-based UI)
- Backend: Spring Boot + BusinessService (framework EnArt)
- ORM: JPA/Hibernate con framework EnArt
- Base datos: PostgreSQL
- Naming convention:
  * Tablas: PREFIX_ENTITYNAME (ej: MODMODELS, MODMODELAPPROVALS)
  * PKs: Siempre IDXMODEL, IDXMODELAPPROVAL, etc. (formato: IDX + nombre)
  * Entidades JPA: com.codeflowx.govern.entity.[module].[EntityName]
  * ViewModels: com.codeflowx.platform.viewmodel.[module].[Entity]OverviewViewModel
  * ZULs: src/main/webapp/console/platform/[module]/[entity]/[action].zul
  * Persistencia: BusinessService (NO usar EntityManager directo)

¿Con esto puedes generar el código correctamente?"
```

### **Si código generado tiene errores:**

```
PROMPT FIX:

"El código que generaste tiene este error al ejecutar:

[COPIAR ERROR EXACTO DE CONSOLA]

¿Puedes generar la versión corregida?

Contexto adicional:
- [Info relevante del error]
- [Archivos relacionados]"
```

### **Si necesitas ajustar después:**

```
PROMPT ITERACIÓN:

"Funciona, pero necesito ajustar:

1. [Cambio específico 1]
2. [Cambio específico 2]

¿Puedes generar versión ajustada?"
```

---

## 🎯 SUCCESS METRICS

### **Al final de 48 horas deberías tener:**

```
✅ 7 pantallas 100% funcionales
✅ Python bias service corriendo
✅ Testing scripts ejecutables
✅ Demo 15 min rehearsed
✅ Documentación básica lista
✅ Datos test cargados
✅ 0 errores críticos
✅ 1-2 errores menores aceptables

RESULTADO:
└─ MVP VENDIBLE LUNES 4 NOVIEMBRE
```

---

## 📞 SIGUIENTE PASO

**AHORA MISMO (Esta noche):**

1. Abre 7 chats (Cursor, Claude, ChatGPT, etc.)
2. Nombra cada uno claramente
3. Copia prompt correspondiente a cada chat
4. Monitoriza respuestas
5. Descarga código generado
6. Organiza en carpeta `/mvp-code-generated/`

**MAÑANA (Día completo):**
- Integración masiva
- Testing iterativo
- Fixes paralelos

**VIERNES:**
- Polish final
- Demo ready

---

**¿Empezamos? ¿Necesitas que ajuste algún prompt antes de lanzar los chats?** 🚀

