# 📊 PROJECTS - DOCUMENTO TÉCNICO CTO

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Audiencia:** CTOs, Arquitectos de Software, Líderes Técnicos  
**Propósito:** Especificación técnica detallada del módulo Projects

---

## 🎯 RESUMEN EJECUTIVO TÉCNICO

**CodeflowX Govern Projects** es una plataforma de gestión de proyectos enterprise-grade construida sobre **arquitectura modular**, con **control financiero en tiempo real**, **integración bidireccional** con herramientas de desarrollo y **analytics predictivo**.

### **Stack Tecnológico:**
- **Backend:** Java 17, Spring Boot 3.2, JPA/Hibernate
- **Frontend:** ZKoss 9.6, JavaScript
- **Base de Datos:** PostgreSQL 15 (principal)
- **Cache:** Redis 7.0
- **Integration:** REST APIs, Webhooks, Message Queue

---

## 🏗️ ARQUITECTURA TÉCNICA

### **1. Arquitectura General**

```
┌─────────────────────────────────────────────────────────────┐
│                    Frontend Layer (ZKoss)                   │
│              (49 ZUL Screens, 47 ViewModels)                │
└─────────────────────┬───────────────────────────────────────┘
                      │
         ┌────────────┴──────────────┐
         ▼                           ▼
┌──────────────────┐        ┌──────────────────┐
│  REST API Layer  │        │  WebSocket       │
│  (Spring MVC)    │        │  (Real-time)     │
└────────┬─────────┘        └────────┬─────────┘
         │                           │
    ┌────▼───────────────────────────▼────┐
    │      Service Layer                   │
    │  (Business Logic & Orchestration)    │
    └────┬───────────────────────────┬────┘
         │                           │
    ┌────▼────┐                 ┌────▼────┐
    │ Data    │                 │ External│
    │ Layer   │                 │ APIs    │
    │ (JPA)   │                 │         │
    └────┬────┘                 └────┬────┘
         │                           │
    ┌────▼───────────────────────────▼────┐
    │         PostgreSQL Database          │
    │  (18 Tables, 15+ Views, Functions)   │
    └──────────────────────────────────────┘
```

### **2. Modelo de Datos**

#### **Esquema Principal**

```sql
-- Tabla principal de proyectos
CREATE TABLE prj_projects (
    prj_id BIGSERIAL PRIMARY KEY,
    prj_name VARCHAR(255) NOT NULL,
    prj_description TEXT,
    prj_project_code VARCHAR(50) UNIQUE NOT NULL,
    prj_project_type VARCHAR(50) NOT NULL,
    prj_status VARCHAR(50) NOT NULL,
    prj_priority VARCHAR(20),
    prj_owner_id BIGINT NOT NULL,
    prj_client_id BIGINT,
    prj_department_id BIGINT,
    prj_start_date DATE,
    prj_end_date DATE,
    prj_budget DECIMAL(15,2),
    prj_is_active BOOLEAN DEFAULT true,
    prj_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    prj_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_prj_owner FOREIGN KEY (prj_owner_id) REFERENCES cor_users(cor_id),
    CONSTRAINT fk_prj_client FOREIGN KEY (prj_client_id) REFERENCES cor_clients(cor_id),
    CONSTRAINT fk_prj_department FOREIGN KEY (prj_department_id) REFERENCES cor_departments(cor_id)
);

-- Índices para optimización
CREATE INDEX idx_prj_projects_code ON prj_projects(prj_project_code);
CREATE INDEX idx_prj_projects_status ON prj_projects(prj_status);
CREATE INDEX idx_prj_projects_owner ON prj_projects(prj_owner_id);
CREATE INDEX idx_prj_projects_dates ON prj_projects(prj_start_date, prj_end_date);

-- Tabla de miembros del equipo
CREATE TABLE prj_project_members (
    prj_id BIGSERIAL PRIMARY KEY,
    prj_project_id BIGINT NOT NULL,
    prj_user_id BIGINT NOT NULL,
    prj_role VARCHAR(50) NOT NULL,
    prj_allocation_percentage DECIMAL(5,2),
    prj_hourly_rate DECIMAL(10,2),
    prj_start_date DATE,
    prj_end_date DATE,
    prj_is_active BOOLEAN DEFAULT true,
    CONSTRAINT fk_prj_member_project FOREIGN KEY (prj_project_id) REFERENCES prj_projects(prj_id),
    CONSTRAINT fk_prj_member_user FOREIGN KEY (prj_user_id) REFERENCES cor_users(cor_id),
    CONSTRAINT uk_prj_member UNIQUE (prj_project_id, prj_user_id)
);

CREATE INDEX idx_prj_members_project ON prj_project_members(prj_project_id);
CREATE INDEX idx_prj_members_user ON prj_project_members(prj_user_id);

-- Tabla de tareas
CREATE TABLE prj_project_tasks (
    prj_id BIGSERIAL PRIMARY KEY,
    prj_project_id BIGINT NOT NULL,
    prj_task_code VARCHAR(50),
    prj_title VARCHAR(255) NOT NULL,
    prj_description TEXT,
    prj_status VARCHAR(50) NOT NULL,
    prj_priority VARCHAR(20),
    prj_assigned_to BIGINT,
    prj_estimated_hours DECIMAL(8,2),
    prj_actual_hours DECIMAL(8,2),
    prj_start_date DATE,
    prj_due_date DATE,
    prj_completed_date DATE,
    prj_parent_task_id BIGINT,
    CONSTRAINT fk_prj_task_project FOREIGN KEY (prj_project_id) REFERENCES prj_projects(prj_id),
    CONSTRAINT fk_prj_task_assigned FOREIGN KEY (prj_assigned_to) REFERENCES cor_users(cor_id),
    CONSTRAINT fk_prj_task_parent FOREIGN KEY (prj_parent_task_id) REFERENCES prj_project_tasks(prj_id)
);

CREATE INDEX idx_prj_tasks_project ON prj_project_tasks(prj_project_id);
CREATE INDEX idx_prj_tasks_assigned ON prj_project_tasks(prj_assigned_to);
CREATE INDEX idx_prj_tasks_status ON prj_project_tasks(prj_status);

-- Tabla de time tracking
CREATE TABLE prj_time_tracking (
    prj_id BIGSERIAL PRIMARY KEY,
    prj_project_id BIGINT NOT NULL,
    prj_task_id BIGINT,
    prj_user_id BIGINT NOT NULL,
    prj_work_date DATE NOT NULL,
    prj_hours_worked DECIMAL(5,2) NOT NULL,
    prj_description TEXT,
    prj_is_billable BOOLEAN DEFAULT true,
    prj_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_prj_time_project FOREIGN KEY (prj_project_id) REFERENCES prj_projects(prj_id),
    CONSTRAINT fk_prj_time_task FOREIGN KEY (prj_task_id) REFERENCES prj_project_tasks(prj_id),
    CONSTRAINT fk_prj_time_user FOREIGN KEY (prj_user_id) REFERENCES cor_users(cor_id)
);

CREATE INDEX idx_prj_time_project ON prj_time_tracking(prj_project_id);
CREATE INDEX idx_prj_time_user ON prj_time_tracking(prj_user_id);
CREATE INDEX idx_prj_time_date ON prj_time_tracking(prj_work_date);

-- Particionamiento por fecha para escalabilidad
CREATE TABLE prj_time_tracking_y2025m10 PARTITION OF prj_time_tracking
    FOR VALUES FROM ('2025-10-01') TO ('2025-11-01');
```

---

## 🔧 COMPONENTES PRINCIPALES

### **1. Project Service**

```java
@Service
@Transactional
public class ProjectService {
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private ProjectMemberRepository memberRepository;
    
    @Autowired
    private EventPublisher eventPublisher;
    
    @Autowired
    private CacheManager cacheManager;
    
    /**
     * Crea un proyecto con validaciones de negocio
     */
    public Project createProject(ProjectRequest request) {
        // Validar código único
        if (projectRepository.existsByProjectCode(request.getProjectCode())) {
            throw new ValidationException("Project code already exists");
        }
        
        // Validar owner
        User owner = userService.findById(request.getOwnerId())
            .orElseThrow(() -> new ValidationException("Owner not found"));
        
        // Crear proyecto
        Project project = Project.builder()
            .name(request.getName())
            .projectCode(request.getProjectCode())
            .projectType(request.getProjectType())
            .status(ProjectStatus.PLANNING)
            .priority(request.getPriority())
            .ownerId(request.getOwnerId())
            .clientId(request.getClientId())
            .departmentId(request.getDepartmentId())
            .startDate(request.getStartDate())
            .endDate(request.getEndDate())
            .budget(request.getBudget())
            .isActive(true)
            .createdAt(LocalDateTime.now())
            .build();
        
        project = projectRepository.save(project);
        
        // Agregar owner como Project Manager
        ProjectMember ownerMember = ProjectMember.builder()
            .projectId(project.getId())
            .userId(owner.getId())
            .role(ProjectRole.PROJECT_MANAGER)
            .allocationPercentage(new BigDecimal("100.00"))
            .hourlyRate(owner.getHourlyRate())
            .startDate(LocalDate.now())
            .isActive(true)
            .build();
        
        memberRepository.save(ownerMember);
        
        // Publicar evento
        eventPublisher.publish(new ProjectCreatedEvent(project));
        
        // Invalidar caché
        cacheManager.evictCache("active-projects");
        
        return project;
    }
    
    /**
     * Calcula el progreso del proyecto
     */
    @Cacheable(value = "project-progress", key = "#projectId")
    public BigDecimal calculateProgress(Long projectId) {
        List<ProjectTask> tasks = taskRepository.findByProjectId(projectId);
        
        if (tasks.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        long completedTasks = tasks.stream()
            .filter(t -> t.getStatus() == TaskStatus.DONE)
            .count();
        
        return new BigDecimal(completedTasks)
            .divide(new BigDecimal(tasks.size()), 2, RoundingMode.HALF_UP)
            .multiply(new BigDecimal("100"));
    }
    
    /**
     * Calcula health score del proyecto
     */
    public ProjectHealthScore calculateHealthScore(Long projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new NotFoundException("Project not found"));
        
        // Progress score (40%)
        BigDecimal progressScore = calculateProgress(projectId);
        
        // Budget score (30%)
        BigDecimal budgetScore = calculateBudgetScore(projectId, project.getBudget());
        
        // Timeline score (20%)
        BigDecimal timelineScore = calculateTimelineScore(project);
        
        // Team score (10%)
        BigDecimal teamScore = calculateTeamScore(projectId);
        
        // Overall score
        BigDecimal overallScore = progressScore.multiply(new BigDecimal("0.4"))
            .add(budgetScore.multiply(new BigDecimal("0.3")))
            .add(timelineScore.multiply(new BigDecimal("0.2")))
            .add(teamScore.multiply(new BigDecimal("0.1")));
        
        HealthStatus status;
        if (overallScore.compareTo(new BigDecimal("80")) >= 0) {
            status = HealthStatus.HEALTHY;
        } else if (overallScore.compareTo(new BigDecimal("60")) >= 0) {
            status = HealthStatus.AT_RISK;
        } else {
            status = HealthStatus.CRITICAL;
        }
        
        return ProjectHealthScore.builder()
            .projectId(projectId)
            .progressScore(progressScore)
            .budgetScore(budgetScore)
            .timelineScore(timelineScore)
            .teamScore(teamScore)
            .overallScore(overallScore)
            .status(status)
            .calculatedAt(LocalDateTime.now())
            .build();
    }
}
```

### **2. Time Tracking Service**

```java
@Service
public class TimeTrackingService {
    
    @Autowired
    private TimeTrackingRepository timeTrackingRepository;
    
    @Autowired
    private ProjectMemberRepository memberRepository;
    
    @Autowired
    private MetricsService metricsService;
    
    /**
     * Registra tiempo trabajado con validaciones
     */
    @Transactional
    public ProjectTimeTracking logTime(TimeTrackingRequest request) {
        // Validar que el usuario es miembro del proyecto
        ProjectMember member = memberRepository
            .findByProjectIdAndUserId(request.getProjectId(), request.getUserId())
            .orElseThrow(() -> new ValidationException("User is not a member of the project"));
        
        if (!member.isActive()) {
            throw new ValidationException("User is not an active member");
        }
        
        // Validar horas (máximo 16h por día)
        if (request.getHoursWorked().compareTo(new BigDecimal("16")) > 0) {
            throw new ValidationException("Cannot log more than 16 hours per day");
        }
        
        // Verificar no duplicados
        BigDecimal existingHours = timeTrackingRepository
            .sumHoursByUserAndDate(
                request.getUserId(),
                request.getWorkDate()
            );
        
        if (existingHours.add(request.getHoursWorked())
            .compareTo(new BigDecimal("24")) > 0) {
            throw new ValidationException("Total hours for the day would exceed 24 hours");
        }
        
        // Crear entrada
        ProjectTimeTracking timeEntry = ProjectTimeTracking.builder()
            .projectId(request.getProjectId())
            .taskId(request.getTaskId())
            .userId(request.getUserId())
            .workDate(request.getWorkDate())
            .hoursWorked(request.getHoursWorked())
            .description(request.getDescription())
            .isBillable(request.isBillable())
            .createdAt(LocalDateTime.now())
            .build();
        
        timeEntry = timeTrackingRepository.save(timeEntry);
        
        // Actualizar métricas
        metricsService.incrementTimeTracked(
            request.getProjectId(),
            request.getHoursWorked()
        );
        
        // Actualizar horas de tarea
        if (request.getTaskId() != null) {
            updateTaskActualHours(request.getTaskId());
        }
        
        return timeEntry;
    }
    
    /**
     * Genera reporte de horas con cálculo de costos
     */
    public TimeTrackingReport generateReport(
        Long projectId,
        LocalDate startDate,
        LocalDate endDate
    ) {
        List<ProjectTimeTracking> entries = timeTrackingRepository
            .findByProjectAndDateRange(projectId, startDate, endDate);
        
        Map<Long, List<ProjectTimeTracking>> byUser = entries.stream()
            .collect(Collectors.groupingBy(ProjectTimeTracking::getUserId));
        
        List<UserTimeReport> userReports = new ArrayList<>();
        BigDecimal totalCost = BigDecimal.ZERO;
        
        for (Map.Entry<Long, List<ProjectTimeTracking>> entry : byUser.entrySet()) {
            Long userId = entry.getKey();
            List<ProjectTimeTracking> userEntries = entry.getValue();
            
            ProjectMember member = memberRepository
                .findByProjectIdAndUserId(projectId, userId)
                .orElseThrow();
            
            BigDecimal totalHours = userEntries.stream()
                .map(ProjectTimeTracking::getHoursWorked)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            BigDecimal billableHours = userEntries.stream()
                .filter(ProjectTimeTracking::isBillable)
                .map(ProjectTimeTracking::getHoursWorked)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            BigDecimal cost = totalHours.multiply(member.getHourlyRate());
            totalCost = totalCost.add(cost);
            
            userReports.add(UserTimeReport.builder()
                .userId(userId)
                .totalHours(totalHours)
                .billableHours(billableHours)
                .hourlyRate(member.getHourlyRate())
                .totalCost(cost)
                .build());
        }
        
        return TimeTrackingReport.builder()
            .projectId(projectId)
            .startDate(startDate)
            .endDate(endDate)
            .userReports(userReports)
            .totalCost(totalCost)
            .build();
    }
}
```

### **3. Financial Service**

```java
@Service
public class ProjectFinancialService {
    
    @Autowired
    private CostEstimatorRepository costEstimatorRepository;
    
    @Autowired
    private InvoiceRepository invoiceRepository;
    
    @Autowired
    private TimeTrackingService timeTrackingService;
    
    /**
     * Genera resumen financiero completo
     */
    public FinancialSummary generateFinancialSummary(Long projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new NotFoundException("Project not found"));
        
        // Calcular costos
        List<ProjectCostEstimator> costs = costEstimatorRepository
            .findByProjectId(projectId);
        
        BigDecimal totalEstimated = costs.stream()
            .map(ProjectCostEstimator::getEstimatedCost)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalActual = costs.stream()
            .map(ProjectCostEstimator::getActualCost)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal variance = totalActual.subtract(totalEstimated);
        
        // Calcular facturación
        List<ProjectInvoice> invoices = invoiceRepository.findByProjectId(projectId);
        
        BigDecimal totalInvoiced = invoices.stream()
            .map(ProjectInvoice::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalPaid = invoices.stream()
            .filter(inv -> inv.getStatus() == InvoiceStatus.PAID)
            .map(ProjectInvoice::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calcular ROI
        BigDecimal revenue = totalPaid;
        BigDecimal netProfit = revenue.subtract(totalActual);
        BigDecimal roiPercentage = totalActual.compareTo(BigDecimal.ZERO) > 0
            ? netProfit.divide(totalActual, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"))
            : BigDecimal.ZERO;
        
        // Budget utilization
        BigDecimal budgetUtilization = project.getBudget().compareTo(BigDecimal.ZERO) > 0
            ? totalActual.divide(project.getBudget(), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"))
            : BigDecimal.ZERO;
        
        return FinancialSummary.builder()
            .projectId(projectId)
            .budget(project.getBudget())
            .totalEstimated(totalEstimated)
            .totalActual(totalActual)
            .variance(variance)
            .variancePercentage(calculateVariancePercentage(variance, totalEstimated))
            .budgetUtilization(budgetUtilization)
            .totalInvoiced(totalInvoiced)
            .totalPaid(totalPaid)
            .revenue(revenue)
            .netProfit(netProfit)
            .roiPercentage(roiPercentage)
            .build();
    }
    
    /**
     * Genera factura automática desde time tracking
     */
    @Transactional
    public ProjectInvoice generateInvoiceFromTimeTracking(
        Long projectId,
        LocalDate startDate,
        LocalDate endDate
    ) {
        // Obtener reporte de horas
        TimeTrackingReport report = timeTrackingService.generateReport(
            projectId, startDate, endDate
        );
        
        // Crear factura
        String invoiceNumber = generateInvoiceNumber(projectId);
        
        BigDecimal subtotal = report.getTotalCost();
        BigDecimal taxRate = new BigDecimal("21.00"); // 21% IVA
        BigDecimal taxAmount = subtotal.multiply(taxRate)
            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal totalAmount = subtotal.add(taxAmount);
        
        ProjectInvoice invoice = ProjectInvoice.builder()
            .projectId(projectId)
            .invoiceNumber(invoiceNumber)
            .invoiceDate(LocalDate.now())
            .dueDate(LocalDate.now().plusDays(15))
            .subtotal(subtotal)
            .taxAmount(taxAmount)
            .totalAmount(totalAmount)
            .status(InvoiceStatus.DRAFT)
            .build();
        
        invoice = invoiceRepository.save(invoice);
        
        // Crear líneas de factura
        for (UserTimeReport userReport : report.getUserReports()) {
            ProjectBillingDetail detail = ProjectBillingDetail.builder()
                .projectId(projectId)
                .invoiceId(invoice.getId())
                .description(String.format(
                    "Professional services - %s (%s hours)",
                    getUserName(userReport.getUserId()),
                    userReport.getBillableHours()
                ))
                .quantity(userReport.getBillableHours())
                .unitPrice(userReport.getHourlyRate())
                .totalPrice(userReport.getTotalCost())
                .billingDate(LocalDate.now())
                .build();
                
            billingDetailRepository.save(detail);
        }
        
        return invoice;
    }
}
```

---

## 📊 INTEGRACIÓN Y APIs

### **REST API Endpoints**

```java
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    
    @GetMapping
    public PageResponse<ProjectDTO> getProjects(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(required = false) ProjectStatus status
    ) {
        return projectService.getProjects(page, size, status);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDTO createProject(@Valid @RequestBody ProjectRequest request) {
        return projectService.createProject(request);
    }
    
    @GetMapping("/{id}/financial/summary")
    public FinancialSummary getFinancialSummary(@PathVariable Long id) {
        return financialService.generateFinancialSummary(id);
    }
    
    @PostMapping("/{id}/time-tracking")
    public TimeTrackingDTO logTime(
        @PathVariable Long id,
        @Valid @RequestBody TimeTrackingRequest request
    ) {
        request.setProjectId(id);
        return timeTrackingService.logTime(request);
    }
}
```

---

## 🎯 CONCLUSIONES TÉCNICAS

### **Fortalezas Arquitectónicas:**
1. ✅ **Modular:** Separación clara de responsabilidades
2. ✅ **Escalable:** Particionamiento de tablas, caché
3. ✅ **Integrable:** APIs RESTful estándar
4. ✅ **Auditable:** Event sourcing y audit trail
5. ✅ **Performante:** Índices optimizados, caching

### **Decisiones de Diseño:**
1. **PostgreSQL:** ACID compliance, views complejas
2. **JPA/Hibernate:** ORM estándar Java
3. **Redis:** Caché de alta performance
4. **Event-driven:** Arquitectura desacoplada
5. **REST APIs:** Integración estándar

### **Escalabilidad:**
- **Vertical:** Optimización de queries
- **Horizontal:** Particionamiento de datos
- **Caché:** Redis para datos frecuentes
- **Async:** Message queue para procesos pesados

---

## 📞 CONTACTO TÉCNICO

**Arquitectura:** architecture@codeflowx.com  
**Soporte Técnico:** support@codeflowx.com  
**Documentación:** docs.codeflowx.com/projects  
**GitHub:** github.com/codeflowx/projects
