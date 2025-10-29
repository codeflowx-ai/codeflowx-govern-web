# 📊 PROJECTS - INTEGRACIÓN

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación de integración del módulo Projects

---

## 🎯 RESUMEN EJECUTIVO

El módulo **Projects** se integra con múltiples componentes de la plataforma CodeflowX Govern para proporcionar gestión completa de proyectos, recursos y costos.

**Integraciones Principales:**
- **Users & Departments:** Gestión de equipo
- **Models & Training:** Proyectos de ML
- **Monitoring:** Uso de recursos
- **Governance:** Compliance de proyectos
- **Financial Systems:** Facturación y contabilidad

---

## 🔗 INTEGRACIÓN CON USERS & DEPARTMENTS

### **Gestión de Equipo**

```java
@Service
public class ProjectTeamIntegration {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private DepartmentService departmentService;
    
    @Autowired
    private ProjectMemberService projectMemberService;
    
    public void addTeamMemberToProject(Long projectId, Long userId, ProjectRole role) {
        // Obtener información del usuario
        User user = userService.getUserById(userId);
        
        // Verificar que el usuario esté activo
        if (!user.isActive()) {
            throw new ValidationException("User is not active");
        }
        
        // Obtener departamento del usuario
        Department department = departmentService.getDepartmentById(user.getDepartmentId());
        
        // Obtener tasa horaria del departamento/usuario
        BigDecimal hourlyRate = user.getHourlyRate() != null 
            ? user.getHourlyRate() 
            : department.getDefaultHourlyRate();
        
        // Agregar miembro al proyecto
        ProjectMember member = ProjectMember.builder()
            .projectId(projectId)
            .userId(userId)
            .role(role)
            .allocationPercentage(new BigDecimal("100.00"))
            .hourlyRate(hourlyRate)
            .startDate(LocalDate.now())
            .isActive(true)
            .build();
            
        projectMemberService.save(member);
        
        // Notificar al usuario
        notificationService.sendProjectAssignmentNotification(user, projectId);
    }
}
```

### **Sincronización de Permisos**

```java
@Service
public class ProjectPermissionIntegration {
    
    @Autowired
    private RoleService roleService;
    
    public List<Permission> getProjectPermissions(Long userId, Long projectId) {
        // Obtener rol del usuario en el proyecto
        ProjectMember member = projectMemberService.findByProjectAndUser(projectId, userId);
        
        if (member == null) {
            return Collections.emptyList();
        }
        
        // Mapear rol de proyecto a permisos
        List<Permission> permissions = new ArrayList<>();
        
        switch (member.getRole()) {
            case PROJECT_MANAGER:
                permissions.addAll(Arrays.asList(
                    Permission.PROJECT_VIEW,
                    Permission.PROJECT_EDIT,
                    Permission.PROJECT_DELETE,
                    Permission.TEAM_MANAGE,
                    Permission.TASK_MANAGE,
                    Permission.FINANCIAL_VIEW
                ));
                break;
                
            case DEVELOPER:
                permissions.addAll(Arrays.asList(
                    Permission.PROJECT_VIEW,
                    Permission.TASK_VIEW,
                    Permission.TASK_EDIT,
                    Permission.TIME_TRACKING
                ));
                break;
                
            case ANALYST:
                permissions.addAll(Arrays.asList(
                    Permission.PROJECT_VIEW,
                    Permission.TASK_VIEW,
                    Permission.REQUIREMENT_MANAGE
                ));
                break;
        }
        
        return permissions;
    }
}
```

---

## 🔗 INTEGRACIÓN CON MODELS & TRAINING

### **Proyectos de ML**

```java
@Service
public class ProjectMLIntegration {
    
    @Autowired
    private ModelService modelService;
    
    @Autowired
    private TrainingService trainingService;
    
    public void linkModelToProject(Long projectId, Long modelId) {
        // Obtener proyecto y modelo
        Project project = projectService.getProject(projectId);
        Model model = modelService.getModel(modelId);
        
        // Crear link entre proyecto y modelo
        ProjectArtifact artifact = ProjectArtifact.builder()
            .projectId(projectId)
            .artifactName(model.getName())
            .artifactType(ArtifactType.ML_MODEL)
            .version(model.getVersion())
            .filePath(model.getArtifactUrl())
            .fileSize(model.getFileSize())
            .checksum(model.getChecksum())
            .createdAt(LocalDateTime.now())
            .build();
            
        projectArtifactService.save(artifact);
        
        // Actualizar metadata del modelo
        model.setProjectId(projectId);
        model.setProjectCode(project.getProjectCode());
        modelService.update(model);
    }
    
    public void trackTrainingCosts(Long projectId, Long trainingJobId) {
        // Obtener training job
        TrainingJob trainingJob = trainingService.getTrainingJob(trainingJobId);
        
        // Calcular costo de training
        BigDecimal computeCost = calculateComputeCost(
            trainingJob.getGpuHours(),
            trainingJob.getGpuType()
        );
        
        // Registrar como costo del proyecto
        ProjectCostEstimator costEstimator = ProjectCostEstimator.builder()
            .projectId(projectId)
            .costCategory(CostCategory.INFRASTRUCTURE)
            .description("ML Training - Job " + trainingJobId)
            .actualCost(computeCost)
            .build();
            
        projectCostEstimatorService.save(costEstimator);
    }
}
```

---

## 🔗 INTEGRACIÓN CON MONITORING

### **Tracking de Recursos**

```java
@Service
public class ProjectMonitoringIntegration {
    
    @Autowired
    private MonitoringService monitoringService;
    
    @Scheduled(fixedRate = 300000) // Cada 5 minutos
    public void collectProjectResourceUsage() {
        List<Project> activeProjects = projectService.getActiveProjects();
        
        for (Project project : activeProjects) {
            // Obtener uso de recursos del proyecto
            ResourceMetrics metrics = monitoringService.getProjectResourceMetrics(
                project.getId(),
                Duration.ofMinutes(5)
            );
            
            // Registrar consumo de CPU
            if (metrics.getCpuUsage() > 0) {
                ProjectResourceConsumption cpuConsumption = ProjectResourceConsumption.builder()
                    .projectId(project.getId())
                    .resourceType(ResourceType.CPU)
                    .consumptionDate(LocalDate.now())
                    .quantity(new BigDecimal(metrics.getCpuUsage()))
                    .unit("core-hours")
                    .cost(calculateCpuCost(metrics.getCpuUsage()))
                    .build();
                    
                resourceConsumptionService.save(cpuConsumption);
            }
            
            // Registrar consumo de memoria
            if (metrics.getMemoryUsage() > 0) {
                ProjectResourceConsumption memConsumption = ProjectResourceConsumption.builder()
                    .projectId(project.getId())
                    .resourceType(ResourceType.MEMORY)
                    .consumptionDate(LocalDate.now())
                    .quantity(new BigDecimal(metrics.getMemoryUsage()))
                    .unit("GB-hours")
                    .cost(calculateMemoryCost(metrics.getMemoryUsage()))
                    .build();
                    
                resourceConsumptionService.save(memConsumption);
            }
            
            // Verificar alertas de sobrecosto
            checkCostAlerts(project, metrics);
        }
    }
    
    private void checkCostAlerts(Project project, ResourceMetrics metrics) {
        // Calcular costo actual vs presupuesto
        BigDecimal totalCost = projectCostEstimatorService.getTotalActualCost(project.getId());
        BigDecimal budgetUtilization = totalCost
            .divide(project.getBudget(), 4, RoundingMode.HALF_UP)
            .multiply(new BigDecimal("100"));
        
        // Alertar si se excede el 80% del presupuesto
        if (budgetUtilization.compareTo(new BigDecimal("80")) > 0) {
            alertService.sendAlert(Alert.builder()
                .type(AlertType.BUDGET_ALERT)
                .severity(AlertSeverity.HIGH)
                .title("Budget Alert - " + project.getName())
                .message(String.format(
                    "Project %s has utilized %.2f%% of budget",
                    project.getName(),
                    budgetUtilization
                ))
                .projectId(project.getId())
                .build());
        }
    }
}
```

---

## 🔗 INTEGRACIÓN CON GOVERNANCE

### **Compliance de Proyectos**

```java
@Service
public class ProjectGovernanceIntegration {
    
    @Autowired
    private GovernanceService governanceService;
    
    public void validateProjectCompliance(Long projectId) {
        Project project = projectService.getProject(projectId);
        
        List<ComplianceViolation> violations = new ArrayList<>();
        
        // Verificar que tenga documentación requerida
        List<ProjectDocument> docs = projectDocumentService.findByProject(projectId);
        if (!hasRequiredDocuments(docs)) {
            violations.add(ComplianceViolation.builder()
                .type(ViolationType.MISSING_DOCUMENTATION)
                .severity(ViolationSeverity.MEDIUM)
                .description("Project missing required documentation")
                .build());
        }
        
        // Verificar que tenga análisis de riesgos
        if (!hasRiskAssessment(projectId)) {
            violations.add(ComplianceViolation.builder()
                .type(ViolationType.MISSING_RISK_ASSESSMENT)
                .severity(ViolationSeverity.HIGH)
                .description("Project missing risk assessment")
                .build());
        }
        
        // Verificar que tenga tracking de tiempo
        if (project.getStatus() == ProjectStatus.ACTIVE && !hasTimeTracking(projectId)) {
            violations.add(ComplianceViolation.builder()
                .type(ViolationType.NO_TIME_TRACKING)
                .severity(ViolationSeverity.LOW)
                .description("Active project with no time tracking entries")
                .build());
        }
        
        // Registrar violaciones si existen
        if (!violations.isEmpty()) {
            governanceService.createIncident(Incident.builder()
                .type(IncidentType.PROJECT_COMPLIANCE_VIOLATION)
                .projectId(projectId)
                .violations(violations)
                .build());
        }
    }
}
```

---

## 🔗 INTEGRACIÓN CON FINANCIAL SYSTEMS

### **Sincronización de Facturas**

```java
@Service
public class ProjectFinancialIntegration {
    
    @Autowired
    private ErpIntegrationService erpIntegrationService;
    
    public void syncInvoiceToERP(Long invoiceId) {
        // Obtener factura y proyecto
        ProjectInvoice invoice = projectInvoiceService.findById(invoiceId);
        Project project = projectService.getProject(invoice.getProjectId());
        
        // Obtener líneas de factura
        List<ProjectBillingDetail> billingDetails = 
            projectBillingDetailService.findByInvoice(invoiceId);
        
        // Construir invoice para ERP
        ErpInvoice erpInvoice = ErpInvoice.builder()
            .invoiceNumber(invoice.getInvoiceNumber())
            .invoiceDate(invoice.getInvoiceDate())
            .dueDate(invoice.getDueDate())
            .customerId(project.getClientId())
            .customerName(getClientName(project.getClientId()))
            .projectReference(project.getProjectCode())
            .subtotal(invoice.getSubtotal())
            .taxAmount(invoice.getTaxAmount())
            .totalAmount(invoice.getTotalAmount())
            .lineItems(mapToErpLineItems(billingDetails))
            .build();
        
        // Enviar a ERP
        String erpInvoiceId = erpIntegrationService.createInvoice(erpInvoice);
        
        // Actualizar referencia en BD
        invoice.setErpInvoiceId(erpInvoiceId);
        invoice.setErpSyncDate(LocalDateTime.now());
        projectInvoiceService.update(invoice);
    }
    
    @Scheduled(fixedRate = 3600000) // Cada hora
    public void syncPaymentStatus() {
        // Obtener facturas pendientes de pago
        List<ProjectInvoice> pendingInvoices = projectInvoiceService
            .findByStatus(InvoiceStatus.SENT);
        
        for (ProjectInvoice invoice : pendingInvoices) {
            if (invoice.getErpInvoiceId() != null) {
                // Consultar estado en ERP
                ErpInvoiceStatus erpStatus = erpIntegrationService
                    .getInvoiceStatus(invoice.getErpInvoiceId());
                
                // Actualizar si está pagada
                if (erpStatus.isPaid()) {
                    invoice.setStatus(InvoiceStatus.PAID);
                    invoice.setPaymentDate(erpStatus.getPaymentDate());
                    projectInvoiceService.update(invoice);
                    
                    // Notificar al project manager
                    notifyPaymentReceived(invoice);
                }
            }
        }
    }
}
```

---

## 🔗 INTEGRACIÓN CON JIRA/GITHUB

### **Sincronización de Tareas**

```java
@Service
public class ProjectJiraIntegration {
    
    @Autowired
    private JiraClient jiraClient;
    
    public void syncTaskToJira(Long taskId) {
        // Obtener tarea
        ProjectTask task = projectTaskService.findById(taskId);
        Project project = projectService.getProject(task.getProjectId());
        
        // Verificar si el proyecto tiene integración Jira
        if (project.getJiraProjectKey() == null) {
            return;
        }
        
        // Crear issue en Jira
        JiraIssue jiraIssue = JiraIssue.builder()
            .projectKey(project.getJiraProjectKey())
            .summary(task.getTitle())
            .description(task.getDescription())
            .issueType(mapTaskTypeToJiraIssueType(task.getPriority()))
            .priority(mapPriorityToJira(task.getPriority()))
            .assignee(getUserJiraUsername(task.getAssignedTo()))
            .dueDate(task.getDueDate())
            .build();
        
        String jiraIssueKey = jiraClient.createIssue(jiraIssue);
        
        // Actualizar referencia
        task.setJiraIssueKey(jiraIssueKey);
        projectTaskService.update(task);
    }
    
    @Scheduled(fixedRate = 600000) // Cada 10 minutos
    public void syncTaskStatus() {
        // Obtener tareas con integración Jira
        List<ProjectTask> jiraTasks = projectTaskService.findWithJiraIntegration();
        
        for (ProjectTask task : jiraTasks) {
            // Obtener estado actual en Jira
            JiraIssue jiraIssue = jiraClient.getIssue(task.getJiraIssueKey());
            
            // Sincronizar estado
            TaskStatus mappedStatus = mapJiraStatusToTaskStatus(jiraIssue.getStatus());
            if (task.getStatus() != mappedStatus) {
                task.setStatus(mappedStatus);
                if (mappedStatus == TaskStatus.DONE) {
                    task.setCompletedDate(LocalDate.now());
                }
                projectTaskService.update(task);
            }
        }
    }
}
```

---

## 🔗 INTEGRACIÓN CON SLACK/TEAMS

### **Notificaciones de Proyecto**

```java
@Service
public class ProjectNotificationIntegration {
    
    @Autowired
    private SlackClient slackClient;
    
    @Autowired
    private TeamsClient teamsClient;
    
    @EventListener
    public void onProjectCreated(ProjectCreatedEvent event) {
        Project project = event.getProject();
        
        // Enviar notificación a Slack
        if (project.getSlackChannelId() != null) {
            SlackMessage message = SlackMessage.builder()
                .channel(project.getSlackChannelId())
                .text("🎉 New project created: " + project.getName())
                .attachments(Arrays.asList(
                    SlackAttachment.builder()
                        .title("Project Details")
                        .fields(Arrays.asList(
                            SlackField.of("Code", project.getProjectCode()),
                            SlackField.of("Budget", formatCurrency(project.getBudget())),
                            SlackField.of("Start Date", formatDate(project.getStartDate()))
                        ))
                        .build()
                ))
                .build();
                
            slackClient.sendMessage(message);
        }
    }
    
    @EventListener
    public void onTaskCompleted(TaskCompletedEvent event) {
        ProjectTask task = event.getTask();
        Project project = projectService.getProject(task.getProjectId());
        
        // Notificar al project manager
        if (project.getSlackChannelId() != null) {
            slackClient.sendMessage(SlackMessage.builder()
                .channel(project.getSlackChannelId())
                .text(String.format("✅ Task completed: %s", task.getTitle()))
                .build());
        }
    }
    
    @EventListener
    public void onBudgetAlert(BudgetAlertEvent event) {
        Project project = event.getProject();
        
        // Enviar alerta crítica
        if (project.getSlackChannelId() != null) {
            slackClient.sendMessage(SlackMessage.builder()
                .channel(project.getSlackChannelId())
                .text("⚠️ Budget Alert!")
                .attachments(Arrays.asList(
                    SlackAttachment.builder()
                        .color("danger")
                        .title(String.format("Project %s exceeds 80%% of budget", project.getName()))
                        .fields(Arrays.asList(
                            SlackField.of("Budget", formatCurrency(project.getBudget())),
                            SlackField.of("Spent", formatCurrency(event.getTotalSpent())),
                            SlackField.of("Remaining", formatCurrency(event.getRemaining()))
                        ))
                        .build()
                ))
                .build());
        }
    }
}
```

---

## 🎯 CONCLUSIÓN

El módulo **Projects** está completamente integrado con:

- ✅ **Users & Departments** - Gestión de equipo
- ✅ **Models & Training** - Proyectos de ML
- ✅ **Monitoring** - Tracking de recursos
- ✅ **Governance** - Compliance de proyectos
- ✅ **Financial Systems** - ERP y contabilidad
- ✅ **Jira/GitHub** - Sincronización de tareas
- ✅ **Slack/Teams** - Notificaciones en tiempo real

**Integración completa end-to-end para gestión de proyectos enterprise.**
