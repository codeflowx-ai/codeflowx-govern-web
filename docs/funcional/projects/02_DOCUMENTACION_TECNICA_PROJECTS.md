# 📊 PROJECTS - DOCUMENTACIÓN TÉCNICA

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación técnica completa del módulo Projects

---

## 🎯 RESUMEN EJECUTIVO

El módulo **Projects** proporciona gestión completa de proyectos, recursos, costos y portfolios con:
- **18 JPA Entities** para persistencia
- **47 ViewModels** para lógica de presentación
- **15+ SQL Views** para reportes
- **8+ SQL Functions** para cálculos
- **5+ SQL Procedures** para procesos

---

## 🗄️ ENTIDADES JPA

### **1. Project**
**Tabla:** `prj_projects`  
**Propósito:** Entidad principal de proyectos

```java
@Entity
@Table(name = "prj_projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prj_id")
    private Long id;
    
    @NotNull
    @Column(name = "prj_name", length = 255)
    private String name;
    
    @Column(name = "prj_description", columnDefinition = "TEXT")
    private String description;
    
    @NotNull
    @Column(name = "prj_project_code", length = 50, unique = true)
    private String projectCode;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "prj_project_type", length = 50)
    private ProjectType projectType; // INTERNAL, CLIENT, RESEARCH, PRODUCT
    
    @Enumerated(EnumType.STRING)
    @Column(name = "prj_status", length = 50)
    private ProjectStatus status; // PLANNING, ACTIVE, ON_HOLD, COMPLETED, CANCELLED
    
    @Enumerated(EnumType.STRING)
    @Column(name = "prj_priority", length = 20)
    private Priority priority; // LOW, MEDIUM, HIGH, CRITICAL
    
    @Column(name = "prj_owner_id")
    private Long ownerId;
    
    @Column(name = "prj_client_id")
    private Long clientId;
    
    @Column(name = "prj_department_id")
    private Long departmentId;
    
    @Column(name = "prj_start_date")
    private LocalDate startDate;
    
    @Column(name = "prj_end_date")
    private LocalDate endDate;
    
    @Column(name = "prj_budget", precision = 15, scale = 2)
    private BigDecimal budget;
    
    @Column(name = "prj_is_active")
    private Boolean isActive;
    
    @Column(name = "prj_created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "prj_updated_at")
    private LocalDateTime updatedAt;
    
    // Relaciones
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectMember> members;
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectTask> tasks;
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectDocument> documents;
}
```

---

### **2. ProjectMember**
**Tabla:** `prj_project_members`  
**Propósito:** Miembros del equipo del proyecto

```java
@Entity
@Table(name = "prj_project_members")
public class ProjectMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prj_id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "prj_project_id")
    private Project project;
    
    @Column(name = "prj_user_id")
    private Long userId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "prj_role", length = 50)
    private ProjectRole role; // PROJECT_MANAGER, DEVELOPER, ANALYST, TESTER
    
    @Column(name = "prj_allocation_percentage", precision = 5, scale = 2)
    private BigDecimal allocationPercentage;
    
    @Column(name = "prj_hourly_rate", precision = 10, scale = 2)
    private BigDecimal hourlyRate;
    
    @Column(name = "prj_start_date")
    private LocalDate startDate;
    
    @Column(name = "prj_end_date")
    private LocalDate endDate;
    
    @Column(name = "prj_is_active")
    private Boolean isActive;
}
```

---

### **3. ProjectTask**
**Tabla:** `prj_project_tasks`  
**Propósito:** Tareas del proyecto

```java
@Entity
@Table(name = "prj_project_tasks")
public class ProjectTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prj_id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "prj_project_id")
    private Project project;
    
    @Column(name = "prj_task_code", length = 50)
    private String taskCode;
    
    @NotNull
    @Column(name = "prj_title", length = 255)
    private String title;
    
    @Column(name = "prj_description", columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "prj_status", length = 50)
    private TaskStatus status; // TODO, IN_PROGRESS, REVIEW, DONE, BLOCKED
    
    @Enumerated(EnumType.STRING)
    @Column(name = "prj_priority", length = 20)
    private Priority priority;
    
    @Column(name = "prj_assigned_to")
    private Long assignedTo;
    
    @Column(name = "prj_estimated_hours", precision = 8, scale = 2)
    private BigDecimal estimatedHours;
    
    @Column(name = "prj_actual_hours", precision = 8, scale = 2)
    private BigDecimal actualHours;
    
    @Column(name = "prj_start_date")
    private LocalDate startDate;
    
    @Column(name = "prj_due_date")
    private LocalDate dueDate;
    
    @Column(name = "prj_completed_date")
    private LocalDate completedDate;
    
    @Column(name = "prj_parent_task_id")
    private Long parentTaskId;
}
```

---

### **4. ProjectTimeTracking**
**Tabla:** `prj_time_tracking`  
**Propósito:** Seguimiento de tiempo trabajado

```java
@Entity
@Table(name = "prj_time_tracking")
public class ProjectTimeTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prj_id")
    private Long id;
    
    @Column(name = "prj_project_id")
    private Long projectId;
    
    @Column(name = "prj_task_id")
    private Long taskId;
    
    @Column(name = "prj_user_id")
    private Long userId;
    
    @Column(name = "prj_work_date")
    private LocalDate workDate;
    
    @Column(name = "prj_hours_worked", precision = 5, scale = 2)
    private BigDecimal hoursWorked;
    
    @Column(name = "prj_description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "prj_is_billable")
    private Boolean isBillable;
    
    @Column(name = "prj_created_at")
    private LocalDateTime createdAt;
}
```

---

### **5. ProjectDocument**
**Tabla:** `prj_project_documents`  
**Propósito:** Documentos del proyecto

```java
@Entity
@Table(name = "prj_project_documents")
public class ProjectDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prj_id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "prj_project_id")
    private Project project;
    
    @NotNull
    @Column(name = "prj_title", length = 255)
    private String title;
    
    @Column(name = "prj_description", columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "prj_document_type", length = 50)
    private DocumentType documentType; // SPEC, DESIGN, TEST, REPORT
    
    @Column(name = "prj_file_path", length = 500)
    private String filePath;
    
    @Column(name = "prj_file_size")
    private Long fileSize;
    
    @Column(name = "prj_uploaded_by")
    private Long uploadedBy;
    
    @Column(name = "prj_version", length = 20)
    private String version;
    
    @Column(name = "prj_created_at")
    private LocalDateTime createdAt;
}
```

---

### **6. ProjectInvoice**
**Tabla:** `prj_project_invoices`  
**Propósito:** Facturas del proyecto

```java
@Entity
@Table(name = "prj_project_invoices")
public class ProjectInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prj_id")
    private Long id;
    
    @Column(name = "prj_project_id")
    private Long projectId;
    
    @NotNull
    @Column(name = "prj_invoice_number", length = 50, unique = true)
    private String invoiceNumber;
    
    @Column(name = "prj_invoice_date")
    private LocalDate invoiceDate;
    
    @Column(name = "prj_due_date")
    private LocalDate dueDate;
    
    @Column(name = "prj_subtotal", precision = 15, scale = 2)
    private BigDecimal subtotal;
    
    @Column(name = "prj_tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount;
    
    @Column(name = "prj_total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "prj_status", length = 50)
    private InvoiceStatus status; // DRAFT, SENT, PAID, OVERDUE, CANCELLED
    
    @Column(name = "prj_payment_date")
    private LocalDate paymentDate;
    
    @Column(name = "prj_notes", columnDefinition = "TEXT")
    private String notes;
}
```

---

### **7. ProjectBillingDetail**
**Tabla:** `prj_billing_details`  
**Propósito:** Detalles de facturación del proyecto

```java
@Entity
@Table(name = "prj_billing_details")
public class ProjectBillingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prj_id")
    private Long id;
    
    @Column(name = "prj_project_id")
    private Long projectId;
    
    @Column(name = "prj_invoice_id")
    private Long invoiceId;
    
    @Column(name = "prj_description", length = 500)
    private String description;
    
    @Column(name = "prj_quantity", precision = 10, scale = 2)
    private BigDecimal quantity;
    
    @Column(name = "prj_unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;
    
    @Column(name = "prj_total_price", precision = 15, scale = 2)
    private BigDecimal totalPrice;
    
    @Column(name = "prj_billing_date")
    private LocalDate billingDate;
}
```

---

### **8. ProjectROI**
**Tabla:** `prj_project_roi`  
**Propósito:** ROI (Return on Investment) del proyecto

```java
@Entity
@Table(name = "prj_project_roi")
public class ProjectROI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prj_id")
    private Long id;
    
    @Column(name = "prj_project_id")
    private Long projectId;
    
    @Column(name = "prj_total_investment", precision = 15, scale = 2)
    private BigDecimal totalInvestment;
    
    @Column(name = "prj_total_revenue", precision = 15, scale = 2)
    private BigDecimal totalRevenue;
    
    @Column(name = "prj_net_profit", precision = 15, scale = 2)
    private BigDecimal netProfit;
    
    @Column(name = "prj_roi_percentage", precision = 7, scale = 2)
    private BigDecimal roiPercentage;
    
    @Column(name = "prj_payback_period_months", precision = 5, scale = 1)
    private BigDecimal paybackPeriodMonths;
    
    @Column(name = "prj_calculation_date")
    private LocalDate calculationDate;
}
```

---

### **9. ProjectCostEstimator**
**Tabla:** `prj_cost_estimators`  
**Propósito:** Estimaciones de costos del proyecto

```java
@Entity
@Table(name = "prj_cost_estimators")
public class ProjectCostEstimator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prj_id")
    private Long id;
    
    @Column(name = "prj_project_id")
    private Long projectId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "prj_cost_category", length = 50)
    private CostCategory costCategory; // LABOR, INFRASTRUCTURE, LICENSE, TRAVEL
    
    @Column(name = "prj_description", length = 500)
    private String description;
    
    @Column(name = "prj_estimated_cost", precision = 15, scale = 2)
    private BigDecimal estimatedCost;
    
    @Column(name = "prj_actual_cost", precision = 15, scale = 2)
    private BigDecimal actualCost;
    
    @Column(name = "prj_variance", precision = 15, scale = 2)
    private BigDecimal variance;
    
    @Column(name = "prj_variance_percentage", precision = 7, scale = 2)
    private BigDecimal variancePercentage;
}
```

---

### **10. ProjectResourceConsumption**
**Tabla:** `prj_resource_consumption`  
**Propósito:** Consumo de recursos del proyecto

```java
@Entity
@Table(name = "prj_resource_consumption")
public class ProjectResourceConsumption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prj_id")
    private Long id;
    
    @Column(name = "prj_project_id")
    private Long projectId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "prj_resource_type", length = 50)
    private ResourceType resourceType; // CPU, MEMORY, STORAGE, NETWORK
    
    @Column(name = "prj_consumption_date")
    private LocalDate consumptionDate;
    
    @Column(name = "prj_quantity", precision = 15, scale = 2)
    private BigDecimal quantity;
    
    @Column(name = "prj_unit", length = 20)
    private String unit;
    
    @Column(name = "prj_cost", precision = 10, scale = 2)
    private BigDecimal cost;
}
```

---

### **11. ProjectLicense**
**Tabla:** `prj_project_licenses`  
**Propósito:** Licencias del proyecto

```java
@Entity
@Table(name = "prj_project_licenses")
public class ProjectLicense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prj_id")
    private Long id;
    
    @Column(name = "prj_project_id")
    private Long projectId;
    
    @NotNull
    @Column(name = "prj_license_name", length = 255)
    private String licenseName;
    
    @Column(name = "prj_license_key", length = 500)
    private String licenseKey;
    
    @Column(name = "prj_vendor", length = 255)
    private String vendor;
    
    @Column(name = "prj_license_type", length = 50)
    private String licenseType; // PERPETUAL, SUBSCRIPTION, TRIAL
    
    @Column(name = "prj_purchase_date")
    private LocalDate purchaseDate;
    
    @Column(name = "prj_expiration_date")
    private LocalDate expirationDate;
    
    @Column(name = "prj_cost", precision = 10, scale = 2)
    private BigDecimal cost;
    
    @Column(name = "prj_seats")
    private Integer seats;
}
```

---

### **12. ProjectArtifact**
**Tabla:** `prj_project_artifacts`  
**Propósito:** Artefactos del proyecto

```java
@Entity
@Table(name = "prj_project_artifacts")
public class ProjectArtifact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prj_id")
    private Long id;
    
    @Column(name = "prj_project_id")
    private Long projectId;
    
    @NotNull
    @Column(name = "prj_artifact_name", length = 255)
    private String artifactName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "prj_artifact_type", length = 50)
    private ArtifactType artifactType; // JAR, WAR, ZIP, DOCKER_IMAGE
    
    @Column(name = "prj_version", length = 50)
    private String version;
    
    @Column(name = "prj_file_path", length = 500)
    private String filePath;
    
    @Column(name = "prj_file_size")
    private Long fileSize;
    
    @Column(name = "prj_checksum", length = 128)
    private String checksum;
    
    @Column(name = "prj_created_at")
    private LocalDateTime createdAt;
}
```

---

### **13. ProjectStack**
**Tabla:** `prj_project_stacks`  
**Propósito:** Stack tecnológico del proyecto

```java
@Entity
@Table(name = "prj_project_stacks")
public class ProjectStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prj_id")
    private Long id;
    
    @Column(name = "prj_project_id")
    private Long projectId;
    
    @NotNull
    @Column(name = "prj_stack_name", length = 255)
    private String stackName;
    
    @Column(name = "prj_description", columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "prj_stack_type", length = 50)
    private StackType stackType; // FRONTEND, BACKEND, DATABASE, INFRASTRUCTURE
    
    @Column(name = "prj_version", length = 50)
    private String version;
    
    @Column(name = "prj_is_active")
    private Boolean isActive;
}
```

---

### **14. ProjectTechnology**
**Tabla:** `prj_project_technologies`  
**Propósito:** Tecnologías utilizadas en el proyecto

```java
@Entity
@Table(name = "prj_project_technologies")
public class ProjectTechnology {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prj_id")
    private Long id;
    
    @Column(name = "prj_project_id")
    private Long projectId;
    
    @Column(name = "prj_stack_id")
    private Long stackId;
    
    @NotNull
    @Column(name = "prj_technology_name", length = 255)
    private String technologyName;
    
    @Column(name = "prj_version", length = 50)
    private String version;
    
    @Column(name = "prj_category", length = 100)
    private String category; // Language, Framework, Library, Tool
    
    @Column(name = "prj_purpose", columnDefinition = "TEXT")
    private String purpose;
}
```

---

### **15. ProjectToken**
**Tabla:** `prj_project_tokens`  
**Propósito:** Tokens de acceso del proyecto

```java
@Entity
@Table(name = "prj_project_tokens")
public class ProjectToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prj_id")
    private Long id;
    
    @Column(name = "prj_project_id")
    private Long projectId;
    
    @NotNull
    @Column(name = "prj_token_name", length = 255)
    private String tokenName;
    
    @Column(name = "prj_token_value", length = 500)
    private String tokenValue;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "prj_token_type", length = 50)
    private TokenType tokenType; // API_KEY, ACCESS_TOKEN, SECRET_KEY
    
    @Column(name = "prj_expiration_date")
    private LocalDateTime expirationDate;
    
    @Column(name = "prj_is_active")
    private Boolean isActive;
    
    @Column(name = "prj_created_at")
    private LocalDateTime createdAt;
}
```

---

### **16. ProjectRequirement**
**Tabla:** `prj_project_requirements`  
**Propósito:** Requisitos del proyecto

```java
@Entity
@Table(name = "prj_project_requirements")
public class ProjectRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prj_id")
    private Long id;
    
    @Column(name = "prj_project_id")
    private Long projectId;
    
    @NotNull
    @Column(name = "prj_requirement_code", length = 50)
    private String requirementCode;
    
    @NotNull
    @Column(name = "prj_title", length = 255)
    private String title;
    
    @Column(name = "prj_description", columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "prj_requirement_type", length = 50)
    private RequirementType requirementType; // FUNCTIONAL, NON_FUNCTIONAL, TECHNICAL
    
    @Enumerated(EnumType.STRING)
    @Column(name = "prj_priority", length = 20)
    private Priority priority;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "prj_status", length = 50)
    private RequirementStatus status; // DRAFT, APPROVED, IMPLEMENTED, TESTED
}
```

---

### **17. ProjectVersion**
**Tabla:** `prj_project_versions`  
**Propósito:** Versiones del proyecto

```java
@Entity
@Table(name = "prj_project_versions")
public class ProjectVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prj_id")
    private Long id;
    
    @Column(name = "prj_project_id")
    private Long projectId;
    
    @NotNull
    @Column(name = "prj_version_number", length = 50)
    private String versionNumber;
    
    @Column(name = "prj_release_date")
    private LocalDate releaseDate;
    
    @Column(name = "prj_release_notes", columnDefinition = "TEXT")
    private String releaseNotes;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "prj_status", length = 50)
    private VersionStatus status; // DEVELOPMENT, TESTING, RELEASED, DEPRECATED
    
    @Column(name = "prj_is_current")
    private Boolean isCurrent;
}
```

---

### **18. ProjectDomain**
**Tabla:** `prj_project_domains`  
**Propósito:** Dominios de proyectos

```java
@Entity
@Table(name = "prj_project_domains")
public class ProjectDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prj_id")
    private Long id;
    
    @NotNull
    @Column(name = "prj_domain_name", length = 255)
    private String domainName;
    
    @Column(name = "prj_description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "prj_parent_domain_id")
    private Long parentDomainId;
    
    @Column(name = "prj_is_active")
    private Boolean isActive;
    
    @Column(name = "prj_created_at")
    private LocalDateTime createdAt;
}
```

---

## 📊 VISTAS SQL

### **1. Vista de Proyectos Activos**
```sql
CREATE OR REPLACE VIEW prj_v_active_projects AS
SELECT 
    p.prj_id,
    p.prj_name,
    p.prj_project_code,
    p.prj_status,
    p.prj_priority,
    p.prj_start_date,
    p.prj_end_date,
    p.prj_budget,
    COUNT(DISTINCT pm.prj_id) AS team_size,
    COUNT(DISTINCT pt.prj_id) AS total_tasks,
    COUNT(DISTINCT pt.prj_id) FILTER (WHERE pt.prj_status = 'DONE') AS completed_tasks,
    SUM(pt.prj_actual_hours) AS total_hours_worked,
    (COUNT(DISTINCT pt.prj_id) FILTER (WHERE pt.prj_status = 'DONE')::DECIMAL / 
     NULLIF(COUNT(DISTINCT pt.prj_id), 0)) * 100 AS completion_percentage
FROM prj_projects p
LEFT JOIN prj_project_members pm ON p.prj_id = pm.prj_project_id AND pm.prj_is_active = true
LEFT JOIN prj_project_tasks pt ON p.prj_id = pt.prj_project_id
WHERE p.prj_is_active = true
GROUP BY p.prj_id, p.prj_name, p.prj_project_code, p.prj_status, p.prj_priority, 
         p.prj_start_date, p.prj_end_date, p.prj_budget;
```

### **2. Vista de ROI de Proyectos**
```sql
CREATE OR REPLACE VIEW prj_v_project_roi AS
SELECT 
    p.prj_id AS project_id,
    p.prj_name AS project_name,
    pr.prj_total_investment,
    pr.prj_total_revenue,
    pr.prj_net_profit,
    pr.prj_roi_percentage,
    pr.prj_payback_period_months,
    CASE 
        WHEN pr.prj_roi_percentage >= 20 THEN 'EXCELLENT'
        WHEN pr.prj_roi_percentage >= 10 THEN 'GOOD'
        WHEN pr.prj_roi_percentage >= 0 THEN 'ACCEPTABLE'
        ELSE 'POOR'
    END AS roi_rating
FROM prj_projects p
JOIN prj_project_roi pr ON p.prj_id = pr.prj_project_id
WHERE p.prj_is_active = true;
```

### **3. Vista de Costos por Proyecto**
```sql
CREATE OR REPLACE VIEW prj_v_project_costs AS
SELECT 
    p.prj_id AS project_id,
    p.prj_name AS project_name,
    SUM(pce.prj_estimated_cost) AS total_estimated_cost,
    SUM(pce.prj_actual_cost) AS total_actual_cost,
    SUM(pce.prj_variance) AS total_variance,
    AVG(pce.prj_variance_percentage) AS avg_variance_percentage,
    SUM(pce.prj_actual_cost) FILTER (WHERE pce.prj_cost_category = 'LABOR') AS labor_cost,
    SUM(pce.prj_actual_cost) FILTER (WHERE pce.prj_cost_category = 'INFRASTRUCTURE') AS infrastructure_cost,
    SUM(pce.prj_actual_cost) FILTER (WHERE pce.prj_cost_category = 'LICENSE') AS license_cost
FROM prj_projects p
LEFT JOIN prj_cost_estimators pce ON p.prj_id = pce.prj_project_id
GROUP BY p.prj_id, p.prj_name;
```

### **4. Vista de Facturación por Proyecto**
```sql
CREATE OR REPLACE VIEW prj_v_project_billing AS
SELECT 
    p.prj_id AS project_id,
    p.prj_name AS project_name,
    COUNT(pi.prj_id) AS total_invoices,
    SUM(pi.prj_total_amount) AS total_invoiced,
    SUM(pi.prj_total_amount) FILTER (WHERE pi.prj_status = 'PAID') AS total_paid,
    SUM(pi.prj_total_amount) FILTER (WHERE pi.prj_status = 'OVERDUE') AS total_overdue,
    (SUM(pi.prj_total_amount) FILTER (WHERE pi.prj_status = 'PAID') / 
     NULLIF(SUM(pi.prj_total_amount), 0)) * 100 AS payment_rate_percentage
FROM prj_projects p
LEFT JOIN prj_project_invoices pi ON p.prj_id = pi.prj_project_id
GROUP BY p.prj_id, p.prj_name;
```

### **5. Vista de Recursos por Proyecto**
```sql
CREATE OR REPLACE VIEW prj_v_project_resources AS
SELECT 
    p.prj_id AS project_id,
    p.prj_name AS project_name,
    prc.prj_resource_type,
    DATE_TRUNC('month', prc.prj_consumption_date) AS month,
    SUM(prc.prj_quantity) AS total_quantity,
    SUM(prc.prj_cost) AS total_cost,
    AVG(prc.prj_cost / NULLIF(prc.prj_quantity, 0)) AS avg_unit_cost
FROM prj_projects p
JOIN prj_resource_consumption prc ON p.prj_id = prc.prj_project_id
GROUP BY p.prj_id, p.prj_name, prc.prj_resource_type, DATE_TRUNC('month', prc.prj_consumption_date);
```

---

## ⚙️ FUNCIONES SQL

### **1. Calcular Progreso del Proyecto**
```sql
CREATE OR REPLACE FUNCTION prj_fn_calculate_project_progress(p_project_id BIGINT)
RETURNS DECIMAL(5,2)
LANGUAGE plpgsql
AS $$
DECLARE
    v_total_tasks INTEGER;
    v_completed_tasks INTEGER;
    v_progress DECIMAL(5,2);
BEGIN
    SELECT 
        COUNT(*),
        COUNT(*) FILTER (WHERE prj_status = 'DONE')
    INTO v_total_tasks, v_completed_tasks
    FROM prj_project_tasks
    WHERE prj_project_id = p_project_id;
    
    IF v_total_tasks = 0 THEN
        RETURN 0;
    END IF;
    
    v_progress := (v_completed_tasks::DECIMAL / v_total_tasks) * 100;
    
    RETURN ROUND(v_progress, 2);
END;
$$;
```

### **2. Calcular ROI del Proyecto**
```sql
CREATE OR REPLACE FUNCTION prj_fn_calculate_roi(
    p_project_id BIGINT,
    p_total_revenue DECIMAL(15,2)
)
RETURNS TABLE (
    net_profit DECIMAL(15,2),
    roi_percentage DECIMAL(7,2)
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_total_cost DECIMAL(15,2);
    v_net_profit DECIMAL(15,2);
    v_roi_percentage DECIMAL(7,2);
BEGIN
    -- Calcular costo total del proyecto
    SELECT COALESCE(SUM(prj_actual_cost), 0)
    INTO v_total_cost
    FROM prj_cost_estimators
    WHERE prj_project_id = p_project_id;
    
    -- Calcular profit neto
    v_net_profit := p_total_revenue - v_total_cost;
    
    -- Calcular ROI percentage
    IF v_total_cost = 0 THEN
        v_roi_percentage := 0;
    ELSE
        v_roi_percentage := (v_net_profit / v_total_cost) * 100;
    END IF;
    
    RETURN QUERY SELECT v_net_profit, v_roi_percentage;
END;
$$;
```

### **3. Obtener Horas Facturables**
```sql
CREATE OR REPLACE FUNCTION prj_fn_get_billable_hours(
    p_project_id BIGINT,
    p_start_date DATE,
    p_end_date DATE
)
RETURNS TABLE (
    user_id BIGINT,
    total_hours DECIMAL(10,2),
    billable_hours DECIMAL(10,2),
    non_billable_hours DECIMAL(10,2)
)
LANGUAGE sql
AS $$
    SELECT 
        prj_user_id AS user_id,
        SUM(prj_hours_worked) AS total_hours,
        SUM(prj_hours_worked) FILTER (WHERE prj_is_billable = true) AS billable_hours,
        SUM(prj_hours_worked) FILTER (WHERE prj_is_billable = false) AS non_billable_hours
    FROM prj_time_tracking
    WHERE prj_project_id = p_project_id
    AND prj_work_date BETWEEN p_start_date AND p_end_date
    GROUP BY prj_user_id;
$$;
```

---

## 🔄 PROCEDIMIENTOS ALMACENADOS

### **1. Crear Proyecto Completo**
```sql
CREATE OR REPLACE PROCEDURE prj_sp_create_project(
    p_name VARCHAR(255),
    p_project_code VARCHAR(50),
    p_project_type VARCHAR(50),
    p_owner_id BIGINT,
    p_budget DECIMAL(15,2),
    OUT p_project_id BIGINT
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Insertar proyecto
    INSERT INTO prj_projects (
        prj_name, prj_project_code, prj_project_type, prj_status,
        prj_owner_id, prj_budget, prj_is_active, prj_created_at
    )
    VALUES (
        p_name, p_project_code, p_project_type, 'PLANNING',
        p_owner_id, p_budget, true, CURRENT_TIMESTAMP
    )
    RETURNING prj_id INTO p_project_id;
    
    -- Agregar owner como project manager
    INSERT INTO prj_project_members (
        prj_project_id, prj_user_id, prj_role, 
        prj_allocation_percentage, prj_is_active
    )
    VALUES (
        p_project_id, p_owner_id, 'PROJECT_MANAGER', 
        100.00, true
    );
END;
$$;
```

### **2. Cerrar Proyecto**
```sql
CREATE OR REPLACE PROCEDURE prj_sp_close_project(
    p_project_id BIGINT
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Actualizar estado del proyecto
    UPDATE prj_projects
    SET prj_status = 'COMPLETED',
        prj_end_date = CURRENT_DATE,
        prj_updated_at = CURRENT_TIMESTAMP
    WHERE prj_id = p_project_id;
    
    -- Completar todas las tareas pendientes
    UPDATE prj_project_tasks
    SET prj_status = 'DONE',
        prj_completed_date = CURRENT_DATE
    WHERE prj_project_id = p_project_id
    AND prj_status != 'DONE';
    
    -- Desactivar miembros del equipo
    UPDATE prj_project_members
    SET prj_is_active = false,
        prj_end_date = CURRENT_DATE
    WHERE prj_project_id = p_project_id
    AND prj_is_active = true;
END;
$$;
```

---

## 🎯 VIEWMODELS

El módulo **Projects** cuenta con **47 ViewModels** para lógica de presentación:

### **ViewModels Principales:**

1. **ProjectOverviewViewModel** - Listado general de proyectos
2. **ProjectDetailViewModel** - Detalle completo de proyecto
3. **ProjectMemberOverviewViewModel** - Gestión de miembros
4. **ProjectTaskOverviewViewModel** - Gestión de tareas
5. **ProjectTimeTrackingOverviewViewModel** - Tracking de tiempo
6. **ProjectInvoiceOverviewViewModel** - Gestión de facturas
7. **ProjectBillingDetailOverviewViewModel** - Detalles de facturación
8. **ProjectROIOverviewViewModel** - Análisis de ROI
9. **ProjectCostEstimatorOverviewViewModel** - Estimación de costos
10. **ProjectResourceConsumptionOverviewViewModel** - Consumo de recursos

**...y 37 ViewModels adicionales** para funcionalidades específicas.

---

## 🎯 CONCLUSIÓN

El módulo **Projects** proporciona:

- ✅ **18 Entidades JPA** completas
- ✅ **47 ViewModels** para presentación
- ✅ **15+ Vistas SQL** para reportes
- ✅ **8+ Funciones SQL** para cálculos
- ✅ **5+ Procedimientos SQL** para procesos
- ✅ **Gestión completa** de proyectos, recursos y costos

**Sistema robusto y completo de gestión de proyectos enterprise.**
