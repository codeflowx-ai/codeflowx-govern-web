# 🚀 PROMPTS: REST API + SDKs + MCP SERVER - CODEFLOWX

**Objetivo:** Exponer plataforma CodeflowX al mundo con arquitectura dual  
**Fecha:** 1 Noviembre 2025  
**Timeline:** 2-3 días con desarrollo asistido IA

---

## 📋 DISTRIBUCIÓN DE TRABAJO

```
CHAT API-1: REST API Controllers (Spring Boot)
CHAT API-2: SDK Python Client
CHAT API-3: SDK Java Client  
CHAT API-4: MCP Server Adapter
CHAT API-5: OpenAPI Documentation + Testing
CHAT API-6: Examples & Integration Guides
```

---

## 📐 ARQUITECTURA GENERAL

### **Stack Tecnológico:**

```
BACKEND (Ya existe):
├─ Spring Boot + BusinessService (Framework EnArt)
├─ Entidades JPA (Model, ModelApproval, etc.)
├─ PostgreSQL
└─ Python ML Service (puerto 8001)

CAPA API (A crear):
├─ REST Controllers (@RestController)
├─ DTOs Request/Response
├─ Exception Handlers
├─ OpenAPI/Swagger docs
└─ CORS configuration

SDKs (A crear):
├─ Python SDK (requests + wrapper)
├─ Java SDK (RestTemplate + wrapper)
└─ Ambos: Retry logic, auth, error handling

MCP SERVER (A crear):
└─ Adapter Spring Boot → MCP Protocol
```

---

## 🎯 ENDPOINTS CRÍTICOS MVP

### **Prioridad ALTA (Implementar primero):**

```
1. Models Management
   POST   /api/v1/models                    - Crear modelo
   GET    /api/v1/models                    - Listar modelos
   GET    /api/v1/models/{id}               - Detalle modelo
   PUT    /api/v1/models/{id}               - Actualizar modelo
   DELETE /api/v1/models/{id}               - Eliminar modelo

2. Approvals Workflow
   POST   /api/v1/models/{id}/submit        - Enviar a aprobación
   GET    /api/v1/approvals/pending         - Listar pendientes
   POST   /api/v1/approvals/{id}/approve    - Aprobar
   POST   /api/v1/approvals/{id}/reject     - Rechazar

3. Bias Analysis
   POST   /api/v1/bias/analyze              - Analizar sesgo
   GET    /api/v1/bias/results/{modelId}    - Resultados sesgo

4. Compliance Dashboard
   GET    /api/v1/compliance/dashboard      - KPIs compliance
   GET    /api/v1/compliance/models/non-compliant - Modelos no compliant

5. Health & Status
   GET    /api/health                       - Health check
   GET    /api/version                      - Version info
```

---

## 💬 CHAT API-1: REST API CONTROLLERS

### **PROMPT ESPECÍFICO:**

```
Necesito crear REST API Controllers para plataforma AI Governance en Spring Boot.

CONTEXTO:
- Framework: Spring Boot 3.x + Framework EnArt
- Persistencia: BusinessService (NO usar @Repository directo)
- Entidades JPA: Model, ModelApproval, ModelBiasAnalysis (YA EXISTEN)
- Base datos: PostgreSQL
- Puerto API: 8080
- Base path: /api/v1

ARQUITECTURA EXISTENTE:
- Paquete entidades: com.codeflowx.govern.entity.[module].[EntityName]
- BusinessService: com.enart.core.service.BusinessService
- Métodos: save(), find(), delete(), etc.

ENDPOINTS A CREAR (Prioridad ALTA):

═══════════════════════════════════════════
1. MODELS MANAGEMENT
═══════════════════════════════════════════

Controller: ModelController.java
Package: com.codeflowx.platform.api.controller
Base Path: /api/v1/models

Endpoints:

POST /api/v1/models
Request Body:
{
  "name": "fraud-detection-v1",
  "type": "CLASSIFICATION",
  "framework": "TENSORFLOW",
  "description": "Fraud detection model",
  "useCase": "Financial fraud detection",
  "riskLevel": "HIGH",
  "owner": "user@company.com"
}
Response: 201 Created
{
  "id": 123,
  "name": "fraud-detection-v1",
  "status": "DRAFT",
  "createdAt": "2025-11-01T10:00:00Z",
  ...
}

GET /api/v1/models?status=DRAFT&page=0&size=20
Response: 200 OK
{
  "content": [
    { "id": 123, "name": "...", ... }
  ],
  "page": 0,
  "totalElements": 45,
  "totalPages": 3
}

GET /api/v1/models/{id}
Response: 200 OK
{
  "id": 123,
  "name": "fraud-detection-v1",
  "status": "DRAFT",
  "complianceScore": "4/6",
  "complianceStatus": "PARTIAL",
  ...
}

PUT /api/v1/models/{id}
Request Body: (campos a actualizar)
Response: 200 OK

DELETE /api/v1/models/{id}
Response: 204 No Content

═══════════════════════════════════════════
2. APPROVALS WORKFLOW
═══════════════════════════════════════════

Controller: ApprovalController.java
Package: com.codeflowx.platform.api.controller
Base Path: /api/v1/approvals

POST /api/v1/models/{id}/submit-approval
Request Body:
{
  "approvalType": "NEW_MODEL",
  "targetEnvironment": "PRODUCTION",
  "requestReason": "Model ready for production deployment"
}
Response: 201 Created
{
  "approvalId": 456,
  "modelId": 123,
  "status": "PENDING",
  "requestedDate": "2025-11-01T10:00:00Z"
}

GET /api/v1/approvals/pending?page=0&size=20
Response: 200 OK
{
  "content": [
    {
      "approvalId": 456,
      "model": { "id": 123, "name": "..." },
      "status": "PENDING",
      "requestedDate": "..."
    }
  ],
  ...
}

POST /api/v1/approvals/{id}/approve
Request Body:
{
  "comments": "Approved after review. Monitoring enabled."
}
Response: 200 OK

POST /api/v1/approvals/{id}/reject
Request Body:
{
  "rejectionReason": "Model accuracy below threshold. Retrain required."
}
Response: 200 OK

═══════════════════════════════════════════
3. BIAS ANALYSIS
═══════════════════════════════════════════

Controller: BiasAnalysisController.java
Package: com.codeflowx.platform.api.controller
Base Path: /api/v1/bias

POST /api/v1/bias/analyze
Request: multipart/form-data
- file: CSV file (y_true, y_pred, protected_attribute)
- modelId: Long
- protectedAttribute: String (ej: "gender")
- favorableOutcome: String (ej: "1")
- threshold: Float (default 0.8)

Process:
1. Validar CSV
2. Llamar Python ML Service (puerto 8001)
3. Guardar resultado en ModelBiasAnalysis
4. Retornar respuesta

Response: 200 OK
{
  "analysisId": 789,
  "modelId": 123,
  "metrics": {
    "demographicParityDifference": 0.15,
    "equalOpportunityDifference": 0.12,
    "disparateImpactRatio": 0.78
  },
  "classification": "MODERATE",
  "recommendations": "Detected moderate bias...",
  "groupsAnalysis": [
    {"group": "male", "accuracy": 0.85, "count": 1000},
    {"group": "female", "accuracy": 0.70, "count": 800}
  ]
}

GET /api/v1/bias/results/{modelId}
Response: 200 OK (último análisis del modelo)

═══════════════════════════════════════════
4. COMPLIANCE DASHBOARD
═══════════════════════════════════════════

Controller: ComplianceController.java
Package: com.codeflowx.platform.api.controller
Base Path: /api/v1/compliance

GET /api/v1/compliance/dashboard
Response: 200 OK
{
  "totalModels": 45,
  "compliantModels": 34,
  "nonCompliantModels": 11,
  "compliancePercentage": 75.6,
  "modelsInProductionNonCompliant": 2,
  "biasAnalysesThisMonth": 23,
  "pendingApprovals": 5
}

GET /api/v1/compliance/models/non-compliant?page=0&size=20
Response: 200 OK
{
  "content": [
    {
      "modelId": 123,
      "name": "fraud-detection-v1",
      "complianceScore": "4/6",
      "missingItems": ["Bias Analysis", "Performance Validation"]
    }
  ],
  ...
}

═══════════════════════════════════════════
COMPONENTES ADICIONALES A CREAR:
═══════════════════════════════════════════

1. DTOs (Data Transfer Objects)
Package: com.codeflowx.platform.api.dto

- ModelCreateRequest.java
- ModelUpdateRequest.java
- ModelResponse.java
- ApprovalRequest.java
- ApprovalResponse.java
- BiasAnalysisRequest.java
- BiasAnalysisResponse.java
- ComplianceDashboardResponse.java
- PagedResponse.java (genérico)
- ErrorResponse.java

2. Exception Handlers
Package: com.codeflowx.platform.api.exception

@RestControllerAdvice
class GlobalExceptionHandler {
  
  @ExceptionHandler(ResourceNotFoundException.class)
  → 404 Not Found
  
  @ExceptionHandler(ValidationException.class)
  → 400 Bad Request
  
  @ExceptionHandler(UnauthorizedException.class)
  → 401 Unauthorized
  
  @ExceptionHandler(Exception.class)
  → 500 Internal Server Error
}

3. Configuration
Package: com.codeflowx.platform.api.config

- CorsConfiguration.java
  └─ Permitir orígenes específicos
  
- OpenApiConfiguration.java
  └─ Swagger/OpenAPI 3.0 setup
  
- SecurityConfiguration.java (básico)
  └─ API Key authentication (opcional MVP)

═══════════════════════════════════════════
REQUISITOS TÉCNICOS:
═══════════════════════════════════════════

✅ Usar BusinessService para persistencia (NO @Repository)
✅ Validación con @Valid y @Validated
✅ Paginación con Pageable (Spring Data)
✅ Logging con @Slf4j (Lombok)
✅ Exception handling global
✅ CORS configurado
✅ OpenAPI/Swagger documentado
✅ Response status codes correctos:
   - 200 OK (success)
   - 201 Created (resource created)
   - 204 No Content (deleted)
   - 400 Bad Request (validation error)
   - 404 Not Found (resource not found)
   - 500 Internal Server Error (unexpected)

✅ Nombrar clases consistente:
   - Controllers: [Entity]Controller
   - DTOs: [Entity][Action]Request/Response
   - Exceptions: [Type]Exception

Por favor genera:
1. Todos los Controllers completos
2. Todos los DTOs necesarios
3. GlobalExceptionHandler completo
4. Configuraciones CORS y OpenAPI
5. README.md con ejemplos curl

Usa buenas prácticas Spring Boot, clean code, y documentación JavaDoc.
```

---

## 💬 CHAT API-2: SDK PYTHON CLIENT

### **PROMPT ESPECÍFICO:**

```
Necesito crear SDK Python para consumir REST API de plataforma AI Governance.

CONTEXTO:
- API Base URL: http://localhost:8080/api/v1
- Authentication: API Key (header: X-API-Key)
- Response format: JSON
- Errors: HTTP status codes + JSON error body

OBJETIVO:
SDK Python production-ready para que clientes puedan integrar CodeflowX fácilmente.

FUNCIONALIDADES REQUERIDAS:

═══════════════════════════════════════════
1. ESTRUCTURA PROYECTO
═══════════════════════════════════════════

codeflowx-sdk-python/
├── codeflowx/
│   ├── __init__.py
│   ├── client.py              # Cliente principal
│   ├── models.py              # Data classes (Model, Approval, etc.)
│   ├── exceptions.py          # Custom exceptions
│   ├── resources/
│   │   ├── __init__.py
│   │   ├── models.py          # ModelResource
│   │   ├── approvals.py       # ApprovalResource
│   │   ├── bias.py            # BiasAnalysisResource
│   │   └── compliance.py      # ComplianceResource
│   └── utils.py               # Helpers
├── setup.py
├── requirements.txt
├── README.md
├── examples/
│   ├── create_model.py
│   ├── approval_workflow.py
│   ├── bias_analysis.py
│   └── compliance_dashboard.py
└── tests/
    ├── test_client.py
    └── test_resources.py

═══════════════════════════════════════════
2. CLIENTE PRINCIPAL (client.py)
═══════════════════════════════════════════

from typing import Optional
import requests
from .resources import ModelResource, ApprovalResource, BiasAnalysisResource, ComplianceResource

class CodeFlowXClient:
    """
    CodeFlowX AI Governance Platform SDK
    
    Usage:
        client = CodeFlowXClient(api_key="your-api-key")
        model = client.models.create(name="fraud-detection", type="CLASSIFICATION")
    """
    
    def __init__(
        self,
        api_key: str,
        base_url: str = "http://localhost:8080/api/v1",
        timeout: int = 30,
        retry: int = 3
    ):
        self.api_key = api_key
        self.base_url = base_url.rstrip('/')
        self.timeout = timeout
        self.retry = retry
        
        # Session con retry logic
        self.session = self._create_session()
        
        # Resources
        self.models = ModelResource(self)
        self.approvals = ApprovalResource(self)
        self.bias = BiasAnalysisResource(self)
        self.compliance = ComplianceResource(self)
    
    def _create_session(self):
        # Implementar session con retry y timeout
        pass
    
    def _request(self, method, endpoint, **kwargs):
        # Wrapper para requests con auth y error handling
        pass

═══════════════════════════════════════════
3. RESOURCES
═══════════════════════════════════════════

class ModelResource:
    """Gestión de modelos ML"""
    
    def create(self, name, type, framework, **kwargs) -> Model:
        """Crear nuevo modelo"""
        pass
    
    def list(self, status=None, page=0, size=20) -> List[Model]:
        """Listar modelos"""
        pass
    
    def get(self, model_id: int) -> Model:
        """Obtener detalle modelo"""
        pass
    
    def update(self, model_id: int, **kwargs) -> Model:
        """Actualizar modelo"""
        pass
    
    def delete(self, model_id: int):
        """Eliminar modelo"""
        pass

class ApprovalResource:
    """Workflow de aprobaciones"""
    
    def submit(self, model_id: int, approval_type: str, **kwargs) -> Approval:
        """Enviar modelo a aprobación"""
        pass
    
    def pending(self, page=0, size=20) -> List[Approval]:
        """Listar aprobaciones pendientes"""
        pass
    
    def approve(self, approval_id: int, comments: str = None) -> Approval:
        """Aprobar modelo"""
        pass
    
    def reject(self, approval_id: int, reason: str) -> Approval:
        """Rechazar modelo"""
        pass

class BiasAnalysisResource:
    """Análisis de sesgo"""
    
    def analyze(
        self,
        model_id: int,
        csv_file: str,
        protected_attribute: str,
        favorable_outcome: str = "1",
        threshold: float = 0.8
    ) -> BiasAnalysis:
        """Analizar sesgo en modelo"""
        pass
    
    def get_results(self, model_id: int) -> BiasAnalysis:
        """Obtener último análisis de sesgo"""
        pass

class ComplianceResource:
    """Dashboard compliance"""
    
    def dashboard(self) -> ComplianceDashboard:
        """Obtener métricas compliance"""
        pass
    
    def non_compliant_models(self, page=0, size=20) -> List[Model]:
        """Listar modelos no compliant"""
        pass

═══════════════════════════════════════════
4. DATA CLASSES (models.py)
═══════════════════════════════════════════

from dataclasses import dataclass
from typing import Optional, List
from datetime import datetime

@dataclass
class Model:
    id: int
    name: str
    type: str
    framework: str
    status: str
    risk_level: str
    compliance_score: str
    created_at: datetime
    # ... otros campos

@dataclass
class Approval:
    id: int
    model_id: int
    status: str
    requested_date: datetime
    # ... otros campos

@dataclass
class BiasAnalysis:
    id: int
    model_id: int
    metrics: dict
    classification: str
    recommendations: str
    groups_analysis: List[dict]

@dataclass
class ComplianceDashboard:
    total_models: int
    compliant_models: int
    compliance_percentage: float
    # ... otros campos

═══════════════════════════════════════════
5. EXCEPTIONS (exceptions.py)
═══════════════════════════════════════════

class CodeFlowXError(Exception):
    """Base exception"""
    pass

class AuthenticationError(CodeFlowXError):
    """401 Unauthorized"""
    pass

class NotFoundError(CodeFlowXError):
    """404 Not Found"""
    pass

class ValidationError(CodeFlowXError):
    """400 Bad Request"""
    pass

class APIError(CodeFlowXError):
    """500 Internal Server Error"""
    pass

═══════════════════════════════════════════
6. FEATURES AVANZADOS
═══════════════════════════════════════════

✅ Retry logic con exponential backoff
✅ Timeout configurable
✅ Logging (configurable nivel)
✅ Pagination helper (iterate all pages)
✅ Async support (asyncio) - opcional
✅ Type hints completos
✅ Docstrings comprehensivos
✅ Unit tests con pytest + mocking

═══════════════════════════════════════════
7. EJEMPLOS DE USO
═══════════════════════════════════════════

# examples/create_model.py
from codeflowx import CodeFlowXClient

client = CodeFlowXClient(api_key="your-api-key")

# Crear modelo
model = client.models.create(
    name="fraud-detection-v1",
    type="CLASSIFICATION",
    framework="TENSORFLOW",
    description="Fraud detection model",
    risk_level="HIGH"
)
print(f"Model created: {model.id}")

# Listar modelos
models = client.models.list(status="DRAFT")
for m in models:
    print(f"- {m.name} ({m.status})")

# examples/approval_workflow.py
# Enviar a aprobación
approval = client.approvals.submit(
    model_id=123,
    approval_type="NEW_MODEL",
    target_environment="PRODUCTION",
    request_reason="Ready for production"
)

# Listar pendientes
pending = client.approvals.pending()
for a in pending:
    print(f"- Model {a.model_id}: {a.status}")

# Aprobar
client.approvals.approve(
    approval_id=456,
    comments="Approved after review"
)

# examples/bias_analysis.py
# Analizar sesgo
result = client.bias.analyze(
    model_id=123,
    csv_file="data/predictions.csv",
    protected_attribute="gender",
    favorable_outcome="1"
)

print(f"Classification: {result.classification}")
print(f"Metrics: {result.metrics}")
print(f"Recommendations: {result.recommendations}")

Por favor genera:
1. Estructura completa del SDK
2. Cliente principal con session management
3. Todos los Resources
4. Data classes con type hints
5. Exception handling
6. setup.py y requirements.txt
7. README.md con instalación y ejemplos
8. Tests básicos con pytest

Usa buenas prácticas Python (PEP 8), type hints, docstrings.
```

---

## 💬 CHAT API-3: SDK JAVA CLIENT

### **PROMPT ESPECÍFICO:**

```
Necesito crear SDK Java para consumir REST API de plataforma AI Governance.

CONTEXTO:
- API Base URL: http://localhost:8080/api/v1
- Authentication: API Key (header: X-API-Key)
- Framework: Spring Boot RestTemplate o WebClient
- Response format: JSON
- Java version: 11+

OBJETIVO:
SDK Java production-ready para que clientes Spring Boot puedan integrar CodeflowX.

FUNCIONALIDADES REQUERIDAS:

═══════════════════════════════════════════
1. ESTRUCTURA PROYECTO
═══════════════════════════════════════════

codeflowx-sdk-java/
├── src/main/java/com/codeflowx/sdk/
│   ├── CodeFlowXClient.java       # Cliente principal
│   ├── config/
│   │   └── ClientConfiguration.java
│   ├── model/
│   │   ├── Model.java
│   │   ├── Approval.java
│   │   ├── BiasAnalysis.java
│   │   └── ComplianceDashboard.java
│   ├── request/
│   │   ├── ModelCreateRequest.java
│   │   ├── ApprovalRequest.java
│   │   └── BiasAnalysisRequest.java
│   ├── response/
│   │   ├── PagedResponse.java
│   │   └── ErrorResponse.java
│   ├── service/
│   │   ├── ModelService.java
│   │   ├── ApprovalService.java
│   │   ├── BiasAnalysisService.java
│   │   └── ComplianceService.java
│   ├── exception/
│   │   ├── CodeFlowXException.java
│   │   ├── AuthenticationException.java
│   │   ├── NotFoundException.java
│   │   └── ValidationException.java
│   └── util/
│       └── RestTemplateUtils.java
├── src/main/resources/
│   └── codeflowx-sdk.properties
├── src/test/java/
│   └── ...
├── pom.xml
└── README.md

═══════════════════════════════════════════
2. CLIENTE PRINCIPAL
═══════════════════════════════════════════

package com.codeflowx.sdk;

/**
 * CodeFlowX AI Governance Platform SDK
 * 
 * Usage:
 * <pre>
 * CodeFlowXClient client = new CodeFlowXClient("your-api-key");
 * Model model = client.models().create(
 *     ModelCreateRequest.builder()
 *         .name("fraud-detection")
 *         .type("CLASSIFICATION")
 *         .build()
 * );
 * </pre>
 */
public class CodeFlowXClient {
    
    private final String apiKey;
    private final String baseUrl;
    private final RestTemplate restTemplate;
    
    private final ModelService modelService;
    private final ApprovalService approvalService;
    private final BiasAnalysisService biasAnalysisService;
    private final ComplianceService complianceService;
    
    public CodeFlowXClient(String apiKey) {
        this(apiKey, "http://localhost:8080/api/v1");
    }
    
    public CodeFlowXClient(String apiKey, String baseUrl) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.restTemplate = createRestTemplate();
        
        // Inicializar services
        this.modelService = new ModelService(restTemplate, baseUrl);
        this.approvalService = new ApprovalService(restTemplate, baseUrl);
        this.biasAnalysisService = new BiasAnalysisService(restTemplate, baseUrl);
        this.complianceService = new ComplianceService(restTemplate, baseUrl);
    }
    
    private RestTemplate createRestTemplate() {
        // Configurar RestTemplate con:
        // - Interceptor para API Key header
        // - Error handler custom
        // - Timeout configuration
        // - Retry logic (opcional)
    }
    
    public ModelService models() {
        return modelService;
    }
    
    public ApprovalService approvals() {
        return approvalService;
    }
    
    public BiasAnalysisService biasAnalysis() {
        return biasAnalysisService;
    }
    
    public ComplianceService compliance() {
        return complianceService;
    }
}

═══════════════════════════════════════════
3. SERVICES
═══════════════════════════════════════════

public class ModelService {
    
    private final RestTemplate restTemplate;
    private final String baseUrl;
    
    /**
     * Crear nuevo modelo
     */
    public Model create(ModelCreateRequest request) {
        String url = baseUrl + "/models";
        return restTemplate.postForObject(url, request, Model.class);
    }
    
    /**
     * Listar modelos con paginación
     */
    public PagedResponse<Model> list(String status, int page, int size) {
        String url = baseUrl + "/models?status={status}&page={page}&size={size}";
        // Implementar con ParameterizedTypeReference para generics
    }
    
    /**
     * Obtener detalle modelo
     */
    public Model get(Long modelId) {
        String url = baseUrl + "/models/{id}";
        return restTemplate.getForObject(url, Model.class, modelId);
    }
    
    /**
     * Actualizar modelo
     */
    public Model update(Long modelId, ModelUpdateRequest request) {
        String url = baseUrl + "/models/{id}";
        restTemplate.put(url, request, modelId);
        return get(modelId);
    }
    
    /**
     * Eliminar modelo
     */
    public void delete(Long modelId) {
        String url = baseUrl + "/models/{id}";
        restTemplate.delete(url, modelId);
    }
}

public class ApprovalService {
    
    /**
     * Enviar modelo a aprobación
     */
    public Approval submit(Long modelId, ApprovalRequest request) {
        // Implementar
    }
    
    /**
     * Listar aprobaciones pendientes
     */
    public PagedResponse<Approval> pending(int page, int size) {
        // Implementar
    }
    
    /**
     * Aprobar modelo
     */
    public Approval approve(Long approvalId, String comments) {
        // Implementar
    }
    
    /**
     * Rechazar modelo
     */
    public Approval reject(Long approvalId, String reason) {
        // Implementar
    }
}

public class BiasAnalysisService {
    
    /**
     * Analizar sesgo
     */
    public BiasAnalysis analyze(BiasAnalysisRequest request) {
        // Multipart request con CSV file
        // Implementar con MultiValueMap
    }
    
    /**
     * Obtener resultados análisis
     */
    public BiasAnalysis getResults(Long modelId) {
        // Implementar
    }
}

public class ComplianceService {
    
    /**
     * Dashboard compliance
     */
    public ComplianceDashboard getDashboard() {
        // Implementar
    }
    
    /**
     * Modelos no compliant
     */
    public PagedResponse<Model> getNonCompliantModels(int page, int size) {
        // Implementar
    }
}

═══════════════════════════════════════════
4. MODELS (POJOs)
═══════════════════════════════════════════

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Model {
    private Long id;
    private String name;
    private String type;
    private String framework;
    private String status;
    private String riskLevel;
    private String complianceScore;
    private LocalDateTime createdAt;
    // ... otros campos
}

@Data
@Builder
public class Approval {
    private Long id;
    private Long modelId;
    private String status;
    private LocalDateTime requestedDate;
    // ...
}

@Data
public class BiasAnalysis {
    private Long id;
    private Long modelId;
    private Map<String, Double> metrics;
    private String classification;
    private String recommendations;
    private List<GroupAnalysis> groupsAnalysis;
}

@Data
public class ComplianceDashboard {
    private Integer totalModels;
    private Integer compliantModels;
    private Double compliancePercentage;
    // ...
}

═══════════════════════════════════════════
5. EXCEPTION HANDLING
═══════════════════════════════════════════

public class CodeFlowXException extends RuntimeException {
    private final int statusCode;
    private final String errorCode;
    
    // Constructor, getters
}

public class AuthenticationException extends CodeFlowXException {
    // 401
}

public class NotFoundException extends CodeFlowXException {
    // 404
}

public class ValidationException extends CodeFlowXException {
    // 400
}

// Custom ErrorHandler para RestTemplate
public class CodeFlowXErrorHandler implements ResponseErrorHandler {
    
    @Override
    public void handleError(ClientHttpResponse response) {
        int statusCode = response.getStatusCode().value();
        
        switch (statusCode) {
            case 401:
                throw new AuthenticationException("Invalid API key");
            case 404:
                throw new NotFoundException("Resource not found");
            case 400:
                throw new ValidationException("Validation error");
            default:
                throw new CodeFlowXException("API error");
        }
    }
}

═══════════════════════════════════════════
6. CONFIGURATION
═══════════════════════════════════════════

// Interceptor para API Key
public class ApiKeyInterceptor implements ClientHttpRequestInterceptor {
    
    private final String apiKey;
    
    @Override
    public ClientHttpResponse intercept(
        HttpRequest request,
        byte[] body,
        ClientHttpRequestExecution execution
    ) throws IOException {
        request.getHeaders().set("X-API-Key", apiKey);
        return execution.execute(request, body);
    }
}

═══════════════════════════════════════════
7. EJEMPLOS DE USO
═══════════════════════════════════════════

// Ejemplo 1: Crear modelo
CodeFlowXClient client = new CodeFlowXClient("your-api-key");

Model model = client.models().create(
    ModelCreateRequest.builder()
        .name("fraud-detection-v1")
        .type("CLASSIFICATION")
        .framework("TENSORFLOW")
        .description("Fraud detection model")
        .riskLevel("HIGH")
        .build()
);
System.out.println("Model created: " + model.getId());

// Ejemplo 2: Workflow aprobación
Approval approval = client.approvals().submit(
    123L,
    ApprovalRequest.builder()
        .approvalType("NEW_MODEL")
        .targetEnvironment("PRODUCTION")
        .requestReason("Ready for production")
        .build()
);

PagedResponse<Approval> pending = client.approvals().pending(0, 20);
pending.getContent().forEach(a -> 
    System.out.println("Pending: " + a.getModelId())
);

client.approvals().approve(456L, "Approved after review");

// Ejemplo 3: Análisis sesgo
BiasAnalysis result = client.biasAnalysis().analyze(
    BiasAnalysisRequest.builder()
        .modelId(123L)
        .csvFile(new File("data/predictions.csv"))
        .protectedAttribute("gender")
        .favorableOutcome("1")
        .build()
);

System.out.println("Classification: " + result.getClassification());
System.out.println("Recommendations: " + result.getRecommendations());

Por favor genera:
1. CodeFlowXClient completo
2. Todos los Services
3. POJOs con Lombok
4. Exception handling completo
5. Interceptors y configuración RestTemplate
6. pom.xml con dependencias
7. README.md con instalación Maven y ejemplos
8. Tests con MockRestServiceServer

Usa buenas prácticas Java, Lombok annotations, JavaDoc.
```

---

## 💬 CHAT API-4: MCP SERVER ADAPTER

### **PROMPT ESPECÍFICO:**

```
Necesito crear MCP Server Adapter para exponer plataforma CodeflowX vía Model Context Protocol.

CONTEXTO:
- Backend: Spring Boot (ya existe)
- MCP Spec: Anthropic Model Context Protocol
- Docker: MCP Gateway de Docker disponible
- Objetivo: AI agents pueden usar CodeFlowX directamente

ARQUITECTURA MCP:

```
AI Agent (Claude, ChatGPT)
         ↓
Docker MCP Gateway
         ↓
MCP Server (A crear)
         ↓
Spring Boot Backend (Ya existe)
         ↓
PostgreSQL
```

FUNCIONALIDADES REQUERIDAS:

═══════════════════════════════════════════
1. MCP SERVER ESTRUCTURA
═══════════════════════════════════════════

Project: codeflowx-mcp-server

Opciones implementación:
A) Python FastAPI (recomendado - más fácil)
B) Java Spring Boot (si prefieres mantener stack)

Recomiendo OPCIÓN A (Python FastAPI).

═══════════════════════════════════════════
2. MCP TOOLS A EXPONER
═══════════════════════════════════════════

Tool: models/register
Description: "Register a new ML model in governance platform"
Parameters:
  - name (string, required)
  - type (string, required): CLASSIFICATION | REGRESSION | LLM | etc.
  - framework (string, required): TENSORFLOW | PYTORCH | etc.
  - description (string)
  - riskLevel (string): LOW | MEDIUM | HIGH
Returns: Model ID and status

Tool: models/list
Description: "List all registered ML models"
Parameters:
  - status (string, optional): DRAFT | APPROVED | etc.
  - page (int, default 0)
  - size (int, default 20)
Returns: List of models with details

Tool: models/get_details
Description: "Get detailed information about a specific model"
Parameters:
  - modelId (int, required)
Returns: Full model details including compliance status

Tool: approvals/submit
Description: "Submit model for approval to deploy in production"
Parameters:
  - modelId (int, required)
  - targetEnvironment (string): DEVELOPMENT | STAGING | PRODUCTION
  - requestReason (string, required)
Returns: Approval request ID and status

Tool: approvals/approve
Description: "Approve a model for deployment (requires GOVERNANCE_ADMIN role)"
Parameters:
  - approvalId (int, required)
  - comments (string, optional)
Returns: Approval confirmation

Tool: approvals/reject
Description: "Reject a model deployment request"
Parameters:
  - approvalId (int, required)
  - rejectionReason (string, required)
Returns: Rejection confirmation

Tool: bias/analyze
Description: "Analyze bias in model predictions"
Parameters:
  - modelId (int, required)
  - csvData (string, required): CSV content with y_true, y_pred, protected_attribute
  - protectedAttribute (string, required): Column name (gender, age, race, etc.)
  - favorableOutcome (string, default "1")
Returns: Bias analysis results with metrics and recommendations

Tool: compliance/check
Description: "Check compliance status of a model"
Parameters:
  - modelId (int, required)
Returns: Compliance score, missing items, and overall status

Tool: compliance/dashboard
Description: "Get overall compliance dashboard metrics"
Parameters: None
Returns: Total models, compliance percentage, non-compliant count, etc.

═══════════════════════════════════════════
3. MCP RESOURCES A EXPONER
═══════════════════════════════════════════

Resource: model://{id}
Description: "Access to model details and metadata"
URI Template: model://{modelId}
MIME Type: application/json
Returns: Full model representation

Resource: compliance-report://{id}
Description: "Compliance report for a specific model"
URI Template: compliance-report://{modelId}
MIME Type: application/json
Returns: Detailed compliance assessment

Resource: bias-analysis://{id}
Description: "Latest bias analysis results for model"
URI Template: bias-analysis://{modelId}
MIME Type: application/json
Returns: Bias metrics and recommendations

═══════════════════════════════════════════
4. IMPLEMENTACIÓN PYTHON FASTAPI
═══════════════════════════════════════════

# File: mcp_server.py

from fastapi import FastAPI
from mcp import MCPServer, Tool, Resource
import requests

app = FastAPI()
mcp = MCPServer()

# Backend CodeFlowX API
BACKEND_URL = "http://localhost:8080/api/v1"
API_KEY = "your-api-key"

@mcp.tool(
    name="models/register",
    description="Register a new ML model in governance platform",
    parameters={
        "name": {"type": "string", "required": True},
        "type": {"type": "string", "required": True},
        "framework": {"type": "string", "required": True},
        "description": {"type": "string"},
        "riskLevel": {"type": "string", "enum": ["LOW", "MEDIUM", "HIGH"]}
    }
)
async def register_model(name, type, framework, description=None, riskLevel="MEDIUM"):
    """Register new ML model"""
    
    response = requests.post(
        f"{BACKEND_URL}/models",
        json={
            "name": name,
            "type": type,
            "framework": framework,
            "description": description,
            "riskLevel": riskLevel
        },
        headers={"X-API-Key": API_KEY}
    )
    
    if response.status_code == 201:
        model = response.json()
        return {
            "success": True,
            "modelId": model["id"],
            "message": f"Model '{name}' registered successfully with ID {model['id']}"
        }
    else:
        return {
            "success": False,
            "error": response.json().get("message", "Unknown error")
        }

@mcp.tool(
    name="models/list",
    description="List all registered ML models",
    parameters={
        "status": {"type": "string", "enum": ["DRAFT", "IN_REVIEW", "APPROVED", "REJECTED", "PRODUCTION"]},
        "page": {"type": "integer", "default": 0},
        "size": {"type": "integer", "default": 20}
    }
)
async def list_models(status=None, page=0, size=20):
    """List models with optional filtering"""
    
    params = {"page": page, "size": size}
    if status:
        params["status"] = status
    
    response = requests.get(
        f"{BACKEND_URL}/models",
        params=params,
        headers={"X-API-Key": API_KEY}
    )
    
    if response.status_code == 200:
        data = response.json()
        models = data["content"]
        return {
            "success": True,
            "models": models,
            "totalElements": data["totalElements"],
            "totalPages": data["totalPages"],
            "currentPage": data["page"]
        }
    else:
        return {"success": False, "error": "Failed to fetch models"}

@mcp.tool(
    name="approvals/submit",
    description="Submit model for approval",
    parameters={
        "modelId": {"type": "integer", "required": True},
        "targetEnvironment": {"type": "string", "enum": ["DEVELOPMENT", "STAGING", "PRODUCTION"]},
        "requestReason": {"type": "string", "required": True}
    }
)
async def submit_approval(modelId, targetEnvironment, requestReason):
    """Submit model for approval workflow"""
    
    response = requests.post(
        f"{BACKEND_URL}/models/{modelId}/submit-approval",
        json={
            "approvalType": "NEW_MODEL",
            "targetEnvironment": targetEnvironment,
            "requestReason": requestReason
        },
        headers={"X-API-Key": API_KEY}
    )
    
    if response.status_code == 201:
        approval = response.json()
        return {
            "success": True,
            "approvalId": approval["approvalId"],
            "status": approval["status"],
            "message": f"Model {modelId} submitted for approval"
        }
    else:
        return {"success": False, "error": response.json().get("message")}

@mcp.tool(
    name="bias/analyze",
    description="Analyze bias in model predictions",
    parameters={
        "modelId": {"type": "integer", "required": True},
        "csvData": {"type": "string", "required": True},
        "protectedAttribute": {"type": "string", "required": True},
        "favorableOutcome": {"type": "string", "default": "1"}
    }
)
async def analyze_bias(modelId, csvData, protectedAttribute, favorableOutcome="1"):
    """Analyze bias in model"""
    
    # Convert csvData string to file-like object
    # Send multipart request to backend
    
    # ... implementation
    
    return {
        "success": True,
        "classification": "MODERATE",
        "metrics": {...},
        "recommendations": "..."
    }

@mcp.tool(
    name="compliance/dashboard",
    description="Get overall compliance dashboard",
    parameters={}
)
async def compliance_dashboard():
    """Get compliance metrics"""
    
    response = requests.get(
        f"{BACKEND_URL}/compliance/dashboard",
        headers={"X-API-Key": API_KEY}
    )
    
    if response.status_code == 200:
        return {
            "success": True,
            **response.json()
        }
    else:
        return {"success": False, "error": "Failed to fetch dashboard"}

# Resources

@mcp.resource(
    uri_template="model://{modelId}",
    mime_type="application/json",
    description="Access model details"
)
async def get_model_resource(modelId: int):
    """Get model as MCP resource"""
    
    response = requests.get(
        f"{BACKEND_URL}/models/{modelId}",
        headers={"X-API-Key": API_KEY}
    )
    
    if response.status_code == 200:
        return response.json()
    else:
        return {"error": "Model not found"}

@mcp.resource(
    uri_template="compliance-report://{modelId}",
    mime_type="application/json",
    description="Compliance report for model"
)
async def get_compliance_report(modelId: int):
    """Get compliance report as MCP resource"""
    
    # Fetch model + compliance data
    # Generate comprehensive report
    
    return {
        "modelId": modelId,
        "complianceScore": "5/6",
        "status": "PARTIAL",
        "missingItems": ["Performance Validation"],
        "recommendations": "..."
    }

═══════════════════════════════════════════
5. DOCKER CONFIGURATION
═══════════════════════════════════════════

# Dockerfile
FROM python:3.11-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 8002

CMD ["uvicorn", "mcp_server:app", "--host", "0.0.0.0", "--port", "8002"]

# docker-compose.yml (añadir a existente)
services:
  codeflowx-mcp:
    build: ./mcp-server
    ports:
      - "8002:8002"
    environment:
      - BACKEND_URL=http://codeflowx-backend:8080/api/v1
      - API_KEY=${CODEFLOWX_API_KEY}
    depends_on:
      - codeflowx-backend

═══════════════════════════════════════════
6. TESTING CON CLAUDE DESKTOP
═══════════════════════════════════════════

# claude_desktop_config.json
{
  "mcpServers": {
    "codeflowx": {
      "command": "docker",
      "args": [
        "run",
        "-i",
        "--rm",
        "-p", "8002:8002",
        "codeflowx-mcp-server"
      ]
    }
  }
}

# Test en Claude Desktop:
User: "Register a new model named fraud-detection-v1, 
       type CLASSIFICATION, framework TENSORFLOW, 
       risk level HIGH"

Claude → MCP Tool: models/register
         → CodeFlowX Backend
         → Response: Model created with ID 123

═══════════════════════════════════════════
DELIVERABLES:
═══════════════════════════════════════════

Por favor genera:
1. mcp_server.py completo con todos los tools
2. Implementación de resources
3. requirements.txt con dependencias MCP
4. Dockerfile y docker-compose.yml
5. README.md con:
   - Instalación
   - Configuración Docker MCP Gateway
   - Configuración Claude Desktop
   - Ejemplos de uso
6. Tests básicos

Usa FastAPI, MCP SDK, requests, buenas prácticas Python.
```

---

## 💬 CHAT API-5: OPENAPI DOCUMENTATION + TESTING

### **PROMPT ESPECÍFICO:**

```
Necesito documentación OpenAPI completa y suite de testing para REST API CodeFlowX.

CONTEXTO:
- API: Spring Boot REST API (ya generada por Chat API-1)
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

OBJETIVO:
Documentación profesional OpenAPI 3.0 + Testing comprehensivo.

═══════════════════════════════════════════
1. OPENAPI CONFIGURATION (Spring Boot)
═══════════════════════════════════════════

@Configuration
public class OpenApiConfiguration {
    
    @Bean
    public OpenAPI codeFlowXOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("CodeFlowX AI Governance API")
                .version("1.0.0")
                .description("""
                    REST API for CodeFlowX AI Governance Platform.
                    
                    Features:
                    - ML Model Management
                    - Approval Workflows
                    - Bias Analysis
                    - Compliance Monitoring
                    - EU AI Act Compliance
                    
                    Authentication: API Key (header: X-API-Key)
                    """)
                .contact(new Contact()
                    .name("CodeFlowX Support")
                    .email("support@codeflowx.com")
                    .url("https://www.codeflowx.com"))
                .license(new License()
                    .name("Commercial")
                    .url("https://www.codeflowx.com/license")))
            .externalDocs(new ExternalDocumentation()
                .description("Full Documentation")
                .url("https://docs.codeflowx.com"))
            .servers(Arrays.asList(
                new Server()
                    .url("http://localhost:8080")
                    .description("Local development"),
                new Server()
                    .url("https://api.codeflowx.com")
                    .description("Production")
            ))
            .addSecurityItem(new SecurityRequirement().addList("API Key"))
            .components(new Components()
                .addSecuritySchemes("API Key", new SecurityScheme()
                    .type(SecurityScheme.Type.APIKEY)
                    .in(SecurityScheme.In.HEADER)
                    .name("X-API-Key")));
    }
}

═══════════════════════════════════════════
2. CONTROLLER ANNOTATIONS
═══════════════════════════════════════════

@RestController
@RequestMapping("/api/v1/models")
@Tag(name = "Models", description = "ML Model Management")
public class ModelController {
    
    @PostMapping
    @Operation(
        summary = "Create new ML model",
        description = "Register a new ML model in the governance platform",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Model created successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ModelResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid request",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized - Invalid API Key"
            )
        }
    )
    public ResponseEntity<ModelResponse> createModel(
        @Parameter(description = "Model creation request", required = true)
        @Valid @RequestBody ModelCreateRequest request
    ) {
        // Implementation
    }
    
    @GetMapping
    @Operation(
        summary = "List ML models",
        description = "Get paginated list of ML models with optional filtering",
        parameters = {
            @Parameter(
                name = "status",
                description = "Filter by model status",
                schema = @Schema(
                    type = "string",
                    allowableValues = {"DRAFT", "IN_REVIEW", "APPROVED", "REJECTED", "PRODUCTION"}
                )
            ),
            @Parameter(name = "page", description = "Page number (0-indexed)", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", description = "Page size", schema = @Schema(type = "integer", defaultValue = "20"))
        }
    )
    public ResponseEntity<PagedResponse<ModelResponse>> listModels(
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        // Implementation
    }
    
    // ... otros métodos con @Operation annotations
}

═══════════════════════════════════════════
3. DTO SCHEMAS
═══════════════════════════════════════════

@Schema(description = "Model creation request")
@Data
public class ModelCreateRequest {
    
    @Schema(
        description = "Model name (unique identifier)",
        example = "fraud-detection-v1",
        required = true,
        minLength = 3,
        maxLength = 100
    )
    @NotBlank
    @Size(min = 3, max = 100)
    private String name;
    
    @Schema(
        description = "Model type",
        example = "CLASSIFICATION",
        required = true,
        allowableValues = {"CLASSIFICATION", "REGRESSION", "LLM", "GENERATIVE", "CLUSTERING"}
    )
    @NotBlank
    private String type;
    
    @Schema(
        description = "ML framework used",
        example = "TENSORFLOW",
        required = true,
        allowableValues = {"TENSORFLOW", "PYTORCH", "SCIKIT_LEARN", "KERAS", "XGBOOST"}
    )
    @NotBlank
    private String framework;
    
    @Schema(
        description = "Model description and purpose",
        example = "Credit card fraud detection model using transaction patterns"
    )
    private String description;
    
    @Schema(
        description = "Risk level assessment",
        example = "HIGH",
        defaultValue = "MEDIUM",
        allowableValues = {"LOW", "MEDIUM", "HIGH"}
    )
    private String riskLevel;
    
    // ... otros campos con @Schema annotations
}

═══════════════════════════════════════════
4. POSTMAN COLLECTION (Auto-generación)
═══════════════════════════════════════════

Script para generar Postman collection desde OpenAPI:

#!/bin/bash
# generate-postman-collection.sh

# 1. Descargar OpenAPI spec
curl http://localhost:8080/v3/api-docs -o openapi.json

# 2. Convertir a Postman collection
npx openapi-to-postman \
  -s openapi.json \
  -o codeflowx-api.postman_collection.json \
  -p

# 3. Importar en Postman:
#    - Open Postman
#    - Import → codeflowx-api.postman_collection.json
#    - Set environment variable: API_KEY = your-key

═══════════════════════════════════════════
5. INTEGRATION TESTS
═══════════════════════════════════════════

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ModelControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private static final String API_KEY = "test-api-key";
    
    @Test
    void createModel_Success() throws Exception {
        ModelCreateRequest request = ModelCreateRequest.builder()
            .name("test-model")
            .type("CLASSIFICATION")
            .framework("TENSORFLOW")
            .riskLevel("MEDIUM")
            .build();
        
        mockMvc.perform(post("/api/v1/models")
                .header("X-API-Key", API_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value("test-model"))
            .andExpect(jsonPath("$.status").value("DRAFT"));
    }
    
    @Test
    void createModel_InvalidRequest_BadRequest() throws Exception {
        ModelCreateRequest request = ModelCreateRequest.builder()
            .name("") // Invalid: blank name
            .type("CLASSIFICATION")
            .build();
        
        mockMvc.perform(post("/api/v1/models")
                .header("X-API-Key", API_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    void getModel_NotFound() throws Exception {
        mockMvc.perform(get("/api/v1/models/99999")
                .header("X-API-Key", API_KEY))
            .andExpect(status().isNotFound());
    }
    
    @Test
    void listModels_WithPagination() throws Exception {
        mockMvc.perform(get("/api/v1/models")
                .header("X-API-Key", API_KEY)
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.page").value(0))
            .andExpect(jsonPath("$.size").value(10));
    }
    
    // ... más tests para todos los endpoints
}

═══════════════════════════════════════════
6. API TESTING SCRIPT (bash + curl)
═══════════════════════════════════════════

#!/bin/bash
# test-api.sh

API_URL="http://localhost:8080/api/v1"
API_KEY="your-api-key"

echo "==================================="
echo "Testing CodeFlowX API"
echo "==================================="

# Test 1: Create Model
echo "1. Creating model..."
RESPONSE=$(curl -s -X POST "$API_URL/models" \
  -H "X-API-Key: $API_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "test-fraud-detection",
    "type": "CLASSIFICATION",
    "framework": "TENSORFLOW",
    "riskLevel": "HIGH"
  }')

MODEL_ID=$(echo $RESPONSE | jq -r '.id')
echo "✓ Model created with ID: $MODEL_ID"

# Test 2: Get Model
echo "2. Getting model details..."
curl -s -X GET "$API_URL/models/$MODEL_ID" \
  -H "X-API-Key: $API_KEY" | jq .
echo "✓ Model retrieved"

# Test 3: Submit Approval
echo "3. Submitting for approval..."
curl -s -X POST "$API_URL/models/$MODEL_ID/submit-approval" \
  -H "X-API-Key: $API_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "approvalType": "NEW_MODEL",
    "targetEnvironment": "PRODUCTION",
    "requestReason": "Ready for production"
  }' | jq .
echo "✓ Approval submitted"

# Test 4: List Pending Approvals
echo "4. Listing pending approvals..."
curl -s -X GET "$API_URL/approvals/pending" \
  -H "X-API-Key: $API_KEY" | jq '.content[] | {approvalId, modelId, status}'
echo "✓ Pending approvals listed"

# Test 5: Compliance Dashboard
echo "5. Getting compliance dashboard..."
curl -s -X GET "$API_URL/compliance/dashboard" \
  -H "X-API-Key: $API_KEY" | jq .
echo "✓ Dashboard retrieved"

echo "==================================="
echo "All tests completed!"
echo "==================================="

═══════════════════════════════════════════
DELIVERABLES:
═══════════════════════════════════════════

Por favor genera:
1. OpenApiConfiguration completa
2. Annotations @Operation en todos controllers
3. @Schema en todos DTOs
4. Integration tests (JUnit + MockMvc)
5. test-api.sh (bash script testing)
6. generate-postman-collection.sh
7. README_TESTING.md con:
   - Cómo ejecutar tests
   - Cómo generar Postman collection
   - Cómo usar Swagger UI
   - Ejemplos curl para cada endpoint

Usa Springdoc OpenAPI, JUnit 5, AssertJ, buenas prácticas testing.
```

---

## 💬 CHAT API-6: EXAMPLES & INTEGRATION GUIDES

### **PROMPT ESPECÍFICO:**

```
Necesito documentación completa de ejemplos y guías de integración para CodeFlowX API.

OBJETIVO:
Documentación que facilite a clientes integrar CodeFlowX en minutos.

═══════════════════════════════════════════
ESTRUCTURA DOCUMENTACIÓN:
═══════════════════════════════════════════

docs/
├── README.md                        # Overview general
├── quickstart.md                    # Quick start 5 min
├── authentication.md                # API Key setup
├── examples/
│   ├── python/
│   │   ├── basic_usage.py
│   │   ├── approval_workflow.py
│   │   ├── bias_analysis.py
│   │   └── compliance_monitoring.py
│   ├── java/
│   │   ├── BasicUsage.java
│   │   ├── ApprovalWorkflow.java
│   │   └── BiasAnalysis.java
│   ├── curl/
│   │   └── all_endpoints.sh
│   └── javascript/
│       └── nodejs_client.js
├── integration-guides/
│   ├── ci-cd-integration.md         # Jenkins, GitHub Actions
│   ├── jupyter-notebooks.md         # Data scientists
│   ├── mlops-platforms.md           # MLflow, Kubeflow
│   └── monitoring-alerts.md         # Prometheus, Grafana
└── use-cases/
    ├── fraud-detection-model.md
    ├── credit-scoring-model.md
    └── recommendation-system.md

═══════════════════════════════════════════
1. QUICKSTART.md
═══════════════════════════════════════════

# CodeFlowX API Quickstart

Get started with CodeFlowX in 5 minutes.

## 1. Get API Key

```bash
# Sign up at https://www.codeflowx.com
# Navigate to Settings → API Keys
# Generate new key
export CODEFLOWX_API_KEY="your-api-key"
```

## 2. Create Your First Model

```bash
curl -X POST https://api.codeflowx.com/api/v1/models \
  -H "X-API-Key: $CODEFLOWX_API_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "my-first-model",
    "type": "CLASSIFICATION",
    "framework": "TENSORFLOW",
    "description": "My first governed model"
  }'
```

## 3. Submit for Approval

```bash
# Use the model ID from previous response
curl -X POST https://api.codeflowx.com/api/v1/models/123/submit-approval \
  -H "X-API-Key: $CODEFLOWX_API_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "approvalType": "NEW_MODEL",
    "targetEnvironment": "PRODUCTION"
  }'
```

## 4. Check Compliance

```bash
curl https://api.codeflowx.com/api/v1/compliance/dashboard \
  -H "X-API-Key: $CODEFLOWX_API_KEY"
```

Done! Your model is now under governance.

═══════════════════════════════════════════
2. PYTHON EXAMPLES
═══════════════════════════════════════════

# examples/python/complete_workflow.py

"""
Complete workflow example: Register → Analyze → Approve → Monitor
"""

from codeflowx import CodeFlowXClient
import time

# Initialize client
client = CodeFlowXClient(api_key="your-api-key")

print("=== CodeFlowX Complete Workflow ===\n")

# Step 1: Register Model
print("1. Registering model...")
model = client.models.create(
    name="fraud-detection-v2",
    type="CLASSIFICATION",
    framework="TENSORFLOW",
    description="Credit card fraud detection",
    risk_level="HIGH",
    owner="data-science-team@company.com"
)
print(f"✓ Model registered: ID {model.id}\n")

# Step 2: Analyze Bias
print("2. Analyzing bias...")
bias_result = client.bias.analyze(
    model_id=model.id,
    csv_file="data/predictions.csv",
    protected_attribute="gender",
    favorable_outcome="1"
)
print(f"✓ Bias analysis complete:")
print(f"  - Classification: {bias_result.classification}")
print(f"  - Demographic Parity: {bias_result.metrics['demographic_parity_difference']:.3f}")
print(f"  - Recommendations: {bias_result.recommendations}\n")

# Step 3: Submit for Approval
print("3. Submitting for approval...")
approval = client.approvals.submit(
    model_id=model.id,
    approval_type="NEW_MODEL",
    target_environment="PRODUCTION",
    request_reason="Model ready: bias analysis passed, performance validated"
)
print(f"✓ Approval submitted: ID {approval.id}\n")

# Step 4: Monitor Status (polling)
print("4. Monitoring approval status...")
for i in range(10):  # Poll for 5 minutes
    approval = client.approvals.get(approval.id)
    print(f"   Status: {approval.status}")
    
    if approval.status in ["APPROVED", "REJECTED"]:
        break
    
    time.sleep(30)  # Wait 30 seconds

if approval.status == "APPROVED":
    print("\n✓ Model APPROVED for production!")
else:
    print(f"\n✗ Model {approval.status}")

# Step 5: Compliance Dashboard
print("\n5. Checking compliance dashboard...")
dashboard = client.compliance.dashboard()
print(f"✓ Compliance Status:")
print(f"  - Total Models: {dashboard.total_models}")
print(f"  - Compliant: {dashboard.compliant_models} ({dashboard.compliance_percentage:.1f}%)")
print(f"  - Non-Compliant: {dashboard.non_compliant_models}")

print("\n=== Workflow Complete ===")

═══════════════════════════════════════════
3. CI/CD INTEGRATION GUIDE
═══════════════════════════════════════════

# integration-guides/ci-cd-integration.md

# CI/CD Integration Guide

Integrate CodeFlowX governance into your ML deployment pipeline.

## GitHub Actions

```yaml
# .github/workflows/ml-deploy.yml

name: ML Model Deployment with Governance

on:
  push:
    branches: [main]
    paths: ['models/**']

jobs:
  governance-check:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Register Model in CodeFlowX
        run: |
          MODEL_ID=$(curl -X POST https://api.codeflowx.com/api/v1/models \
            -H "X-API-Key: ${{ secrets.CODEFLOWX_API_KEY }}" \
            -H "Content-Type: application/json" \
            -d '{
              "name": "model-${{ github.sha }}",
              "type": "CLASSIFICATION",
              "framework": "TENSORFLOW"
            }' | jq -r '.id')
          echo "MODEL_ID=$MODEL_ID" >> $GITHUB_ENV
      
      - name: Run Bias Analysis
        run: |
          python scripts/generate_predictions.py
          
          curl -X POST https://api.codeflowx.com/api/v1/bias/analyze \
            -H "X-API-Key: ${{ secrets.CODEFLOWX_API_KEY }}" \
            -F "file=@predictions.csv" \
            -F "modelId=${{ env.MODEL_ID }}" \
            -F "protectedAttribute=gender"
      
      - name: Submit for Approval
        run: |
          APPROVAL_ID=$(curl -X POST https://api.codeflowx.com/api/v1/models/${{ env.MODEL_ID }}/submit-approval \
            -H "X-API-Key: ${{ secrets.CODEFLOWX_API_KEY }}" \
            -H "Content-Type: application/json" \
            -d '{
              "approvalType": "NEW_MODEL",
              "targetEnvironment": "PRODUCTION",
              "requestReason": "Automated deployment from commit ${{ github.sha }}"
            }' | jq -r '.approvalId')
          echo "APPROVAL_ID=$APPROVAL_ID" >> $GITHUB_ENV
      
      - name: Wait for Approval (or fail)
        run: |
          python scripts/wait_for_approval.py \
            --approval-id ${{ env.APPROVAL_ID }} \
            --timeout 300  # 5 minutes
      
      - name: Deploy to Production
        if: success()
        run: |
          echo "Deploying model ${{ env.MODEL_ID }} to production..."
          # Your deployment steps here
```

## Jenkins Pipeline

```groovy
// Jenkinsfile

pipeline {
    agent any
    
    environment {
        CODEFLOWX_API_KEY = credentials('codeflowx-api-key')
        CODEFLOWX_URL = 'https://api.codeflowx.com/api/v1'
    }
    
    stages {
        stage('Register Model') {
            steps {
                script {
                    def response = sh(
                        script: """
                            curl -X POST ${CODEFLOWX_URL}/models \
                              -H 'X-API-Key: ${CODEFLOWX_API_KEY}' \
                              -H 'Content-Type: application/json' \
                              -d '{
                                "name": "model-${BUILD_NUMBER}",
                                "type": "CLASSIFICATION",
                                "framework": "TENSORFLOW"
                              }'
                        """,
                        returnStdout: true
                    ).trim()
                    
                    def json = readJSON text: response
                    env.MODEL_ID = json.id
                }
            }
        }
        
        stage('Bias Analysis') {
            steps {
                sh """
                    curl -X POST ${CODEFLOWX_URL}/bias/analyze \
                      -H 'X-API-Key: ${CODEFLOWX_API_KEY}' \
                      -F 'file=@predictions.csv' \
                      -F 'modelId=${MODEL_ID}' \
                      -F 'protectedAttribute=gender'
                """
            }
        }
        
        stage('Submit Approval') {
            steps {
                script {
                    def response = sh(
                        script: """
                            curl -X POST ${CODEFLOWX_URL}/models/${MODEL_ID}/submit-approval \
                              -H 'X-API-Key: ${CODEFLOWX_API_KEY}' \
                              -H 'Content-Type: application/json' \
                              -d '{
                                "approvalType": "NEW_MODEL",
                                "targetEnvironment": "PRODUCTION"
                              }'
                        """,
                        returnStdout: true
                    ).trim()
                    
                    def json = readJSON text: response
                    env.APPROVAL_ID = json.approvalId
                }
            }
        }
        
        stage('Deploy') {
            when {
                expression { env.APPROVAL_ID != null }
            }
            steps {
                echo "Deploying model ${MODEL_ID}..."
                // Your deployment steps
            }
        }
    }
}
```

═══════════════════════════════════════════
DELIVERABLES:
═══════════════════════════════════════════

Por favor genera:
1. README.md general (overview completo)
2. quickstart.md (5 min para empezar)
3. authentication.md (setup API Key)
4. Ejemplos Python completos (5+ scripts)
5. Ejemplos Java completos (3+ clases)
6. Ejemplos curl (script bash completo)
7. CI/CD integration guides:
   - GitHub Actions
   - Jenkins
   - GitLab CI
8. Use cases reales con código completo:
   - Fraud detection model lifecycle
   - Credit scoring compliance
9. MLOps integration:
   - MLflow integration
   - Kubeflow pipelines
10. Troubleshooting guide

Todo con código funcionando, copy-paste ready.
```

---

## 📋 ORDEN DE EJECUCIÓN RECOMENDADO

```
DÍA 1 (Sábado):
├─ Chat API-1: REST API Controllers (6-8h)
└─ Chat API-5: OpenAPI + Testing básico (2-3h)

DÍA 2 (Domingo):
├─ Chat API-2: SDK Python (4-6h)
├─ Chat API-3: SDK Java (4-6h)
└─ Chat API-4: MCP Server (3-4h) [paralelo]

DÍA 3 (Lunes):
└─ Chat API-6: Examples + Guides (3-4h)
└─ Testing integración completa (2-3h)

TOTAL: 2-3 días con IA
```

---

## ✅ CRITERIOS DONE

```
☐ REST API Controllers compilando sin errores
☐ OpenAPI docs generados y visibles en /swagger-ui.html
☐ SDK Python instalable con pip
☐ SDK Java instalable con Maven
☐ MCP Server arrancando con Docker
☐ Tests integración pasando (90%+ coverage)
☐ Ejemplos ejecutándose correctamente
☐ CI/CD pipeline GitHub Actions funcionando
☐ Postman collection importable
☐ Documentación completa y clara

CUANDO 10/10 = ✅ → APIs/SDKs/MCP READY FOR CUSTOMERS
```

---

**🔥 CON ESTOS 6 CHATS TENDRÁS LA PLATAFORMA CODEFLOWX COMPLETAMENTE EXPUESTA VÍA REST API + SDKs + MCP SERVER**

**Última actualización:** 1 Noviembre 2025  
**Status:** 📝 PROMPTS LISTOS PARA USAR

