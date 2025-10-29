# 🔌 API & SDK - MÓDULO COMPLIANCE

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación de APIs y SDK del módulo compliance

---

## 🎯 RESUMEN EJECUTIVO

El módulo **compliance** proporciona **APIs REST** y **SDK** para la gestión de compliance regulatorio, incluyendo verificaciones automáticas, gestión de hallazgos, auditoría y reportes de compliance.

---

## 📋 ENDPOINTS REST API

### **1. Compliance Assessment**

#### **GET /api/compliance/assessments**
- **Descripción:** Obtener lista de evaluaciones de compliance
- **Parámetros:**
  - `framework` (opcional): Framework de compliance (AI_ACT, GDPR, SOX, ISO27001)
  - `status` (opcional): Estado de la evaluación (PENDING, COMPLETED, FAILED)
  - `page` (opcional): Número de página
  - `size` (opcional): Tamaño de página
- **Respuesta:**
```json
{
  "content": [
    {
      "id": "assessment-001",
      "framework": "AI_ACT",
      "status": "COMPLETED",
      "score": 85.5,
      "createdDate": "2025-10-25T10:00:00Z",
      "completedDate": "2025-10-25T11:30:00Z"
    }
  ],
  "totalElements": 150,
  "totalPages": 15
}
```

#### **POST /api/compliance/assessments**
- **Descripción:** Crear nueva evaluación de compliance
- **Body:**
```json
{
  "framework": "AI_ACT",
  "scope": "AGENT_SYSTEM",
  "entityId": "agent-001",
  "requirements": ["TRANSPARENCY", "ACCOUNTABILITY", "HUMAN_OVERSIGHT"]
}
```

#### **GET /api/compliance/assessments/{id}**
- **Descripción:** Obtener detalle de evaluación específica
- **Respuesta:**
```json
{
  "id": "assessment-001",
  "framework": "AI_ACT",
  "status": "COMPLETED",
  "score": 85.5,
  "findings": [
    {
      "id": "finding-001",
      "requirement": "TRANSPARENCY",
      "status": "COMPLIANT",
      "evidence": "Documentation provided",
      "severity": "LOW"
    }
  ],
  "recommendations": [
    {
      "id": "rec-001",
      "description": "Improve documentation",
      "priority": "MEDIUM"
    }
  ]
}
```

### **2. Compliance Findings**

#### **GET /api/compliance/findings**
- **Descripción:** Obtener hallazgos de compliance
- **Parámetros:**
  - `severity` (opcional): Severidad (CRITICAL, HIGH, MEDIUM, LOW)
  - `status` (opcional): Estado (OPEN, IN_PROGRESS, RESOLVED, CLOSED)
  - `framework` (opcional): Framework de compliance
- **Respuesta:**
```json
{
  "content": [
    {
      "id": "finding-001",
      "requirement": "TRANSPARENCY",
      "severity": "HIGH",
      "status": "OPEN",
      "description": "Missing transparency documentation",
      "createdDate": "2025-10-25T10:00:00Z",
      "dueDate": "2025-11-01T10:00:00Z"
    }
  ]
}
```

#### **PUT /api/compliance/findings/{id}**
- **Descripción:** Actualizar hallazgo de compliance
- **Body:**
```json
{
  "status": "IN_PROGRESS",
  "assignedTo": "compliance-officer-001",
  "notes": "Working on documentation update"
}
```

### **3. Compliance Requirements**

#### **GET /api/compliance/requirements**
- **Descripción:** Obtener requisitos de compliance por framework
- **Parámetros:**
  - `framework` (requerido): Framework de compliance
- **Respuesta:**
```json
{
  "framework": "AI_ACT",
  "requirements": [
    {
      "id": "req-001",
      "code": "TRANSPARENCY",
      "title": "Transparency Requirements",
      "description": "AI systems must be transparent",
      "category": "FUNDAMENTAL_RIGHTS",
      "mandatory": true
    }
  ]
}
```

### **4. Compliance Reports**

#### **GET /api/compliance/reports/executive**
- **Descripción:** Generar reporte ejecutivo de compliance
- **Parámetros:**
  - `period` (opcional): Período (LAST_MONTH, LAST_QUARTER, LAST_YEAR)
  - `framework` (opcional): Framework específico
- **Respuesta:**
```json
{
  "period": "LAST_QUARTER",
  "summary": {
    "totalAssessments": 45,
    "complianceRate": 87.5,
    "criticalFindings": 3,
    "resolvedFindings": 28
  },
  "byFramework": {
    "AI_ACT": {
      "assessments": 15,
      "complianceRate": 90.0,
      "findings": 5
    }
  },
  "trends": {
    "complianceImprovement": 5.2,
    "findingsReduction": 12.8
  }
}
```

---

## 🔧 SDK JAVA

### **1. ComplianceService**

```java
@Service
public class ComplianceService {
    
    /**
     * Ejecutar evaluación de compliance
     */
    public ComplianceAssessmentResult executeAssessment(
        String framework, 
        String entityId, 
        ComplianceScope scope) {
        
        ComplianceAssessment assessment = ComplianceAssessment.builder()
            .framework(framework)
            .entityId(entityId)
            .scope(scope)
            .status(AssessmentStatus.PENDING)
            .build();
            
        return complianceEngine.executeAssessment(assessment);
    }
    
    /**
     * Obtener hallazgos por severidad
     */
    public List<ComplianceFinding> getFindingsBySeverity(
        String framework, 
        FindingSeverity severity) {
        
        return complianceRepository.findByFrameworkAndSeverity(framework, severity);
    }
    
    /**
     * Generar reporte de compliance
     */
    public ComplianceReport generateReport(
        String framework, 
        ReportPeriod period) {
        
        return complianceReportingService.generateReport(framework, period);
    }
}
```

### **2. ComplianceAssessment**

```java
@Entity
@Table(name = "com_assessment")
public class ComplianceAssessment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "assessment_id", unique = true)
    private String assessmentId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "framework")
    private ComplianceFramework framework;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AssessmentStatus status;
    
    @Column(name = "score")
    private Double score;
    
    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL)
    private List<ComplianceFinding> findings;
    
    // Getters y setters
}
```

### **3. ComplianceFinding**

```java
@Entity
@Table(name = "com_finding")
public class ComplianceFinding {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "finding_id", unique = true)
    private String findingId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id")
    private ComplianceAssessment assessment;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "severity")
    private FindingSeverity severity;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FindingStatus status;
    
    @Column(name = "description", length = 1000)
    private String description;
    
    @Column(name = "evidence", length = 2000)
    private String evidence;
    
    // Getters y setters
}
```

---

## 🔄 INTEGRACIÓN CON BPMN

### **1. Compliance Monitoring Process**

```java
@Component
public class ComplianceMonitoringDelegate implements JavaDelegate {
    
    @Autowired
    private ComplianceService complianceService;
    
    @Override
    public void execute(DelegateExecution execution) {
        // Ejecutar verificaciones de compliance
        List<ComplianceFramework> frameworks = Arrays.asList(
            ComplianceFramework.AI_ACT,
            ComplianceFramework.GDPR,
            ComplianceFramework.SOX
        );
        
        boolean hasNonCompliance = false;
        List<ComplianceFinding> criticalFindings = new ArrayList<>();
        
        for (ComplianceFramework framework : frameworks) {
            ComplianceAssessmentResult result = 
                complianceService.executeAssessment(framework, "SYSTEM", ComplianceScope.SYSTEM);
            
            if (!result.isCompliant()) {
                hasNonCompliance = true;
                criticalFindings.addAll(
                    result.getFindings().stream()
                        .filter(f -> f.getSeverity() == FindingSeverity.CRITICAL)
                        .collect(Collectors.toList())
                );
            }
        }
        
        execution.setVariable("hasNonCompliance", hasNonCompliance);
        execution.setVariable("criticalFindings", criticalFindings);
    }
}
```

---

## 📊 MÉTRICAS Y MONITOREO

### **1. Compliance Metrics**

```java
@Component
public class ComplianceMetricsService {
    
    /**
     * Calcular tasa de compliance por framework
     */
    public ComplianceRate calculateComplianceRate(String framework, LocalDate from, LocalDate to) {
        List<ComplianceAssessment> assessments = complianceRepository
            .findByFrameworkAndDateRange(framework, from, to);
            
        long totalAssessments = assessments.size();
        long compliantAssessments = assessments.stream()
            .filter(a -> a.getScore() >= 80.0)
            .count();
            
        double complianceRate = totalAssessments > 0 ? 
            (double) compliantAssessments / totalAssessments * 100 : 0.0;
            
        return ComplianceRate.builder()
            .framework(framework)
            .totalAssessments(totalAssessments)
            .compliantAssessments(compliantAssessments)
            .complianceRate(complianceRate)
            .build();
    }
    
    /**
     * Obtener tendencias de compliance
     */
    public ComplianceTrends getComplianceTrends(String framework, int months) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(months);
        
        List<ComplianceRate> rates = new ArrayList<>();
        for (int i = 0; i < months; i++) {
            LocalDate monthStart = startDate.plusMonths(i);
            LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
            
            ComplianceRate rate = calculateComplianceRate(framework, monthStart, monthEnd);
            rates.add(rate);
        }
        
        return ComplianceTrends.builder()
            .framework(framework)
            .rates(rates)
            .trendDirection(calculateTrendDirection(rates))
            .build();
    }
}
```

---

## 🎯 BENEFICIOS DE LA API/SDK

### **Para Desarrolladores:**
- **APIs REST** estándar para integración
- **SDK Java** completo para desarrollo interno
- **Integración BPMN** automatizada
- **Métricas** en tiempo real

### **Para Compliance Officers:**
- **Verificaciones automáticas** programadas
- **Gestión de hallazgos** centralizada
- **Reportes** ejecutivos automatizados
- **Alertas** proactivas de no conformidad

### **Para la Organización:**
- **Cumplimiento regulatorio** automatizado
- **Visibilidad** completa de compliance
- **Reducción de riesgos** regulatorios
- **Auditoría** simplificada

---

## 🎯 CONCLUSIÓN

El módulo Compliance proporciona **APIs y SDK robustos** que permiten:

- 🔌 **Integración** con sistemas externos
- 🤖 **Automatización** de verificaciones
- 📊 **Monitoreo** en tiempo real
- 📋 **Gestión** centralizada de compliance
- 📈 **Reportes** ejecutivos automatizados

**Esta API/SDK está diseñada** para facilitar el cumplimiento regulatorio y la gestión efectiva de compliance en sistemas de IA.
