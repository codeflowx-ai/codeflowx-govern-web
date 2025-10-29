# 📊 PROJECTS - MONITOREO

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Documentación de monitoreo del módulo Projects

---

## 🎯 RESUMEN EJECUTIVO

El módulo **Projects** incluye monitoreo completo de:
- **Project Health:** Estado de salud de proyectos
- **Budget Tracking:** Seguimiento de presupuesto
- **Team Performance:** Rendimiento del equipo
- **Task Progress:** Progreso de tareas
- **Resource Usage:** Uso de recursos

---

## 📊 MÉTRICAS PRINCIPALES

### **1. MÉTRICAS DE PROYECTO**

#### **Project Health Score**
```sql
-- Vista para health score de proyectos
CREATE OR REPLACE VIEW prj_v_project_health AS
SELECT 
    p.prj_id AS project_id,
    p.prj_name,
    p.prj_status,
    -- Progress score (0-100)
    prj_fn_calculate_project_progress(p.prj_id) AS progress_score,
    -- Budget score (0-100)
    CASE 
        WHEN p.prj_budget = 0 THEN 100
        ELSE GREATEST(0, 100 - ((SUM(pce.prj_actual_cost) / p.prj_budget) * 100))
    END AS budget_score,
    -- Timeline score (0-100)
    CASE 
        WHEN p.prj_end_date IS NULL THEN 100
        WHEN CURRENT_DATE > p.prj_end_date THEN 0
        WHEN CURRENT_DATE < p.prj_start_date THEN 100
        ELSE GREATEST(0, 100 - (
            (EXTRACT(DAY FROM CURRENT_DATE - p.prj_start_date)::DECIMAL /
             NULLIF(EXTRACT(DAY FROM p.prj_end_date - p.prj_start_date), 0)) * 100
        ))
    END AS timeline_score,
    -- Team score (0-100)
    CASE 
        WHEN COUNT(DISTINCT pm.prj_id) = 0 THEN 0
        WHEN COUNT(DISTINCT pm.prj_id) FILTER (WHERE pm.prj_is_active = false) > 0 THEN 50
        ELSE 100
    END AS team_score,
    -- Overall health score
    (
        prj_fn_calculate_project_progress(p.prj_id) * 0.4 +
        CASE 
            WHEN p.prj_budget = 0 THEN 100
            ELSE GREATEST(0, 100 - ((SUM(pce.prj_actual_cost) / p.prj_budget) * 100))
        END * 0.3 +
        CASE 
            WHEN p.prj_end_date IS NULL THEN 100
            WHEN CURRENT_DATE > p.prj_end_date THEN 0
            ELSE 100
        END * 0.2 +
        CASE 
            WHEN COUNT(DISTINCT pm.prj_id) = 0 THEN 0
            ELSE 100
        END * 0.1
    ) AS overall_health_score,
    CASE 
        WHEN (
            prj_fn_calculate_project_progress(p.prj_id) * 0.4 +
            CASE 
                WHEN p.prj_budget = 0 THEN 100
                ELSE GREATEST(0, 100 - ((SUM(pce.prj_actual_cost) / p.prj_budget) * 100))
            END * 0.3 +
            CASE 
                WHEN p.prj_end_date IS NULL THEN 100
                WHEN CURRENT_DATE > p.prj_end_date THEN 0
                ELSE 100
            END * 0.2 +
            CASE 
                WHEN COUNT(DISTINCT pm.prj_id) = 0 THEN 0
                ELSE 100
            END * 0.1
        ) >= 80 THEN 'HEALTHY'
        WHEN (
            prj_fn_calculate_project_progress(p.prj_id) * 0.4 +
            CASE 
                WHEN p.prj_budget = 0 THEN 100
                ELSE GREATEST(0, 100 - ((SUM(pce.prj_actual_cost) / p.prj_budget) * 100))
            END * 0.3 +
            CASE 
                WHEN p.prj_end_date IS NULL THEN 100
                WHEN CURRENT_DATE > p.prj_end_date THEN 0
                ELSE 100
            END * 0.2 +
            CASE 
                WHEN COUNT(DISTINCT pm.prj_id) = 0 THEN 0
                ELSE 100
            END * 0.1
        ) >= 60 THEN 'AT_RISK'
        ELSE 'CRITICAL'
    END AS health_status
FROM prj_projects p
LEFT JOIN prj_project_members pm ON p.prj_id = pm.prj_project_id
LEFT JOIN prj_cost_estimators pce ON p.prj_id = pce.prj_project_id
WHERE p.prj_is_active = true
GROUP BY p.prj_id, p.prj_name, p.prj_status, p.prj_budget, p.prj_start_date, p.prj_end_date;
```

#### **Budget Tracking**
```sql
-- Vista para tracking de presupuesto
CREATE OR REPLACE VIEW prj_v_budget_tracking AS
SELECT 
    p.prj_id AS project_id,
    p.prj_name,
    p.prj_budget AS budget,
    SUM(pce.prj_estimated_cost) AS total_estimated,
    SUM(pce.prj_actual_cost) AS total_spent,
    p.prj_budget - SUM(pce.prj_actual_cost) AS remaining,
    (SUM(pce.prj_actual_cost) / NULLIF(p.prj_budget, 0)) * 100 AS budget_utilization_percent,
    SUM(pce.prj_variance) AS total_variance,
    AVG(pce.prj_variance_percentage) AS avg_variance_percent,
    CASE 
        WHEN (SUM(pce.prj_actual_cost) / NULLIF(p.prj_budget, 0)) * 100 > 100 THEN 'OVER_BUDGET'
        WHEN (SUM(pce.prj_actual_cost) / NULLIF(p.prj_budget, 0)) * 100 > 80 THEN 'AT_RISK'
        ELSE 'ON_TRACK'
    END AS budget_status
FROM prj_projects p
LEFT JOIN prj_cost_estimators pce ON p.prj_id = pce.prj_project_id
WHERE p.prj_is_active = true
GROUP BY p.prj_id, p.prj_name, p.prj_budget;
```

#### **Timeline Tracking**
```sql
-- Vista para tracking de timeline
CREATE OR REPLACE VIEW prj_v_timeline_tracking AS
SELECT 
    p.prj_id AS project_id,
    p.prj_name,
    p.prj_start_date,
    p.prj_end_date,
    EXTRACT(DAY FROM p.prj_end_date - p.prj_start_date) AS total_duration_days,
    EXTRACT(DAY FROM CURRENT_DATE - p.prj_start_date) AS elapsed_days,
    EXTRACT(DAY FROM p.prj_end_date - CURRENT_DATE) AS remaining_days,
    prj_fn_calculate_project_progress(p.prj_id) AS completion_percentage,
    (EXTRACT(DAY FROM CURRENT_DATE - p.prj_start_date)::DECIMAL /
     NULLIF(EXTRACT(DAY FROM p.prj_end_date - p.prj_start_date), 0)) * 100 AS time_elapsed_percent,
    CASE 
        WHEN CURRENT_DATE > p.prj_end_date THEN 'OVERDUE'
        WHEN prj_fn_calculate_project_progress(p.prj_id) < 
             ((EXTRACT(DAY FROM CURRENT_DATE - p.prj_start_date)::DECIMAL /
               NULLIF(EXTRACT(DAY FROM p.prj_end_date - p.prj_start_date), 0)) * 100) - 10 
        THEN 'BEHIND_SCHEDULE'
        WHEN prj_fn_calculate_project_progress(p.prj_id) > 
             ((EXTRACT(DAY FROM CURRENT_DATE - p.prj_start_date)::DECIMAL /
               NULLIF(EXTRACT(DAY FROM p.prj_end_date - p.prj_start_date), 0)) * 100) + 10 
        THEN 'AHEAD_OF_SCHEDULE'
        ELSE 'ON_SCHEDULE'
    END AS schedule_status
FROM prj_projects p
WHERE p.prj_is_active = true
AND p.prj_start_date IS NOT NULL
AND p.prj_end_date IS NOT NULL;
```

---

### **2. MÉTRICAS DE EQUIPO**

#### **Team Performance**
```sql
-- Vista para performance del equipo
CREATE OR REPLACE VIEW prj_v_team_performance AS
SELECT 
    pm.prj_project_id AS project_id,
    pm.prj_user_id AS user_id,
    u.cor_full_name AS user_name,
    pm.prj_role,
    COUNT(DISTINCT pt.prj_id) AS total_tasks,
    COUNT(DISTINCT pt.prj_id) FILTER (WHERE pt.prj_status = 'DONE') AS completed_tasks,
    (COUNT(DISTINCT pt.prj_id) FILTER (WHERE pt.prj_status = 'DONE')::DECIMAL /
     NULLIF(COUNT(DISTINCT pt.prj_id), 0)) * 100 AS completion_rate,
    SUM(pt.prj_estimated_hours) AS total_estimated_hours,
    SUM(pt.prj_actual_hours) AS total_actual_hours,
    SUM(pt.prj_actual_hours) - SUM(pt.prj_estimated_hours) AS hour_variance,
    (SUM(pt.prj_actual_hours) / NULLIF(SUM(pt.prj_estimated_hours), 0)) * 100 AS estimation_accuracy,
    SUM(ptt.prj_hours_worked) AS total_logged_hours,
    pm.prj_hourly_rate,
    SUM(ptt.prj_hours_worked) * pm.prj_hourly_rate AS total_cost
FROM prj_project_members pm
JOIN cor_users u ON pm.prj_user_id = u.cor_id
LEFT JOIN prj_project_tasks pt ON pm.prj_project_id = pt.prj_project_id 
    AND pm.prj_user_id = pt.prj_assigned_to
LEFT JOIN prj_time_tracking ptt ON pm.prj_project_id = ptt.prj_project_id 
    AND pm.prj_user_id = ptt.prj_user_id
WHERE pm.prj_is_active = true
GROUP BY pm.prj_project_id, pm.prj_user_id, u.cor_full_name, pm.prj_role, pm.prj_hourly_rate;
```

#### **Time Utilization**
```sql
-- Vista para utilización de tiempo
CREATE OR REPLACE VIEW prj_v_time_utilization AS
SELECT 
    pm.prj_project_id AS project_id,
    pm.prj_user_id AS user_id,
    DATE_TRUNC('week', ptt.prj_work_date) AS week_start,
    pm.prj_allocation_percentage,
    -- Horas esperadas (40h/semana * allocation)
    40 * (pm.prj_allocation_percentage / 100) AS expected_hours,
    SUM(ptt.prj_hours_worked) AS actual_hours,
    SUM(ptt.prj_hours_worked) FILTER (WHERE ptt.prj_is_billable = true) AS billable_hours,
    SUM(ptt.prj_hours_worked) FILTER (WHERE ptt.prj_is_billable = false) AS non_billable_hours,
    (SUM(ptt.prj_hours_worked) /
     NULLIF(40 * (pm.prj_allocation_percentage / 100), 0)) * 100 AS utilization_percentage
FROM prj_project_members pm
JOIN prj_time_tracking ptt ON pm.prj_project_id = ptt.prj_project_id 
    AND pm.prj_user_id = ptt.prj_user_id
WHERE pm.prj_is_active = true
AND ptt.prj_work_date >= CURRENT_DATE - INTERVAL '3 months'
GROUP BY pm.prj_project_id, pm.prj_user_id, DATE_TRUNC('week', ptt.prj_work_date), 
         pm.prj_allocation_percentage;
```

---

### **3. MÉTRICAS DE TAREAS**

#### **Task Completion Rate**
```sql
-- Vista para tasa de completación de tareas
CREATE OR REPLACE VIEW prj_v_task_completion AS
SELECT 
    pt.prj_project_id AS project_id,
    DATE_TRUNC('month', pt.prj_completed_date) AS month,
    COUNT(*) AS total_tasks_completed,
    AVG(EXTRACT(DAY FROM pt.prj_completed_date - pt.prj_start_date)) AS avg_completion_days,
    AVG(pt.prj_actual_hours / NULLIF(pt.prj_estimated_hours, 0)) AS avg_estimation_ratio,
    COUNT(*) FILTER (WHERE pt.prj_completed_date > pt.prj_due_date) AS overdue_tasks,
    (COUNT(*) FILTER (WHERE pt.prj_completed_date > pt.prj_due_date)::DECIMAL /
     COUNT(*)) * 100 AS overdue_percentage
FROM prj_project_tasks pt
WHERE pt.prj_status = 'DONE'
AND pt.prj_completed_date >= CURRENT_DATE - INTERVAL '12 months'
GROUP BY pt.prj_project_id, DATE_TRUNC('month', pt.prj_completed_date);
```

#### **Task Backlog**
```sql
-- Vista para backlog de tareas
CREATE OR REPLACE VIEW prj_v_task_backlog AS
SELECT 
    pt.prj_project_id AS project_id,
    p.prj_name AS project_name,
    COUNT(*) AS total_backlog_tasks,
    COUNT(*) FILTER (WHERE pt.prj_priority = 'CRITICAL') AS critical_tasks,
    COUNT(*) FILTER (WHERE pt.prj_priority = 'HIGH') AS high_priority_tasks,
    COUNT(*) FILTER (WHERE pt.prj_due_date < CURRENT_DATE) AS overdue_tasks,
    AVG(EXTRACT(DAY FROM CURRENT_DATE - pt.prj_start_date)) AS avg_age_days,
    MAX(EXTRACT(DAY FROM CURRENT_DATE - pt.prj_start_date)) AS oldest_task_age_days
FROM prj_project_tasks pt
JOIN prj_projects p ON pt.prj_project_id = p.prj_id
WHERE pt.prj_status IN ('TODO', 'IN_PROGRESS', 'REVIEW', 'BLOCKED')
GROUP BY pt.prj_project_id, p.prj_name;
```

---

### **4. MÉTRICAS DE RECURSOS**

#### **Resource Consumption Trends**
```sql
-- Vista para tendencias de consumo de recursos
CREATE OR REPLACE VIEW prj_v_resource_trends AS
SELECT 
    prc.prj_project_id AS project_id,
    prc.prj_resource_type,
    DATE_TRUNC('month', prc.prj_consumption_date) AS month,
    SUM(prc.prj_quantity) AS total_quantity,
    AVG(prc.prj_quantity) AS avg_daily_quantity,
    SUM(prc.prj_cost) AS total_cost,
    AVG(prc.prj_cost) AS avg_daily_cost,
    -- Comparación con mes anterior
    LAG(SUM(prc.prj_cost)) OVER (
        PARTITION BY prc.prj_project_id, prc.prj_resource_type 
        ORDER BY DATE_TRUNC('month', prc.prj_consumption_date)
    ) AS previous_month_cost,
    SUM(prc.prj_cost) - LAG(SUM(prc.prj_cost)) OVER (
        PARTITION BY prc.prj_project_id, prc.prj_resource_type 
        ORDER BY DATE_TRUNC('month', prc.prj_consumption_date)
    ) AS cost_change
FROM prj_resource_consumption prc
WHERE prc.prj_consumption_date >= CURRENT_DATE - INTERVAL '12 months'
GROUP BY prc.prj_project_id, prc.prj_resource_type, DATE_TRUNC('month', prc.prj_consumption_date);
```

---

## 🎯 DASHBOARDS Y VISUALIZACIONES

### **Dashboard Principal de Projects**

#### **Pantallas ZUL:**
1. **`project-portfolio-dashboard-overview.zul`** - Dashboard ejecutivo
2. **`project-overview.zul`** - Vista de proyectos
3. **`project-financial-summary-overview.zul`** - Resumen financiero
4. **`project-risk-assessment-overview.zul`** - Evaluación de riesgos
5. **`project-resource-allocation-overview.zul`** - Asignación de recursos

---

## 🚨 ALERTAS Y NOTIFICACIONES

### **Configuración de Alertas**

```java
@Service
public class ProjectAlertService {
    
    @Scheduled(fixedRate = 300000) // Cada 5 minutos
    public void checkProjectAlerts() {
        List<Project> activeProjects = projectService.getActiveProjects();
        
        for (Project project : activeProjects) {
            // Check budget alerts
            checkBudgetAlerts(project);
            
            // Check timeline alerts
            checkTimelineAlerts(project);
            
            // Check team alerts
            checkTeamAlerts(project);
            
            // Check task alerts
            checkTaskAlerts(project);
        }
    }
    
    private void checkBudgetAlerts(Project project) {
        BigDecimal totalSpent = costEstimatorService.getTotalActualCost(project.getId());
        BigDecimal utilization = totalSpent
            .divide(project.getBudget(), 4, RoundingMode.HALF_UP)
            .multiply(new BigDecimal("100"));
        
        // Alerta al 80%
        if (utilization.compareTo(new BigDecimal("80")) > 0 && 
            utilization.compareTo(new BigDecimal("90")) <= 0) {
            alertService.sendAlert(Alert.builder()
                .type(AlertType.BUDGET_WARNING)
                .severity(AlertSeverity.MEDIUM)
                .title("Budget Warning - " + project.getName())
                .message(String.format(
                    "Project has utilized %.2f%% of budget",
                    utilization
                ))
                .projectId(project.getId())
                .build());
        }
        
        // Alerta crítica al 90%
        if (utilization.compareTo(new BigDecimal("90")) > 0) {
            alertService.sendAlert(Alert.builder()
                .type(AlertType.BUDGET_CRITICAL)
                .severity(AlertSeverity.HIGH)
                .title("Budget Critical - " + project.getName())
                .message(String.format(
                    "Project has utilized %.2f%% of budget - Immediate action required",
                    utilization
                ))
                .projectId(project.getId())
                .build());
        }
    }
    
    private void checkTimelineAlerts(Project project) {
        if (project.getEndDate() == null) {
            return;
        }
        
        long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), project.getEndDate());
        BigDecimal progress = projectService.calculateProgress(project.getId());
        
        // Alerta si progreso < tiempo transcurrido
        if (daysRemaining < 30 && progress.compareTo(new BigDecimal("80")) < 0) {
            alertService.sendAlert(Alert.builder()
                .type(AlertType.TIMELINE_AT_RISK)
                .severity(AlertSeverity.HIGH)
                .title("Timeline At Risk - " + project.getName())
                .message(String.format(
                    "Project is %.2f%% complete with %d days remaining",
                    progress,
                    daysRemaining
                ))
                .projectId(project.getId())
                .build());
        }
        
        // Alerta si está overdue
        if (daysRemaining < 0) {
            alertService.sendAlert(Alert.builder()
                .type(AlertType.PROJECT_OVERDUE)
                .severity(AlertSeverity.CRITICAL)
                .title("Project Overdue - " + project.getName())
                .message(String.format(
                    "Project is %d days overdue",
                    Math.abs(daysRemaining)
                ))
                .projectId(project.getId())
                .build());
        }
    }
    
    private void checkTaskAlerts(Project project) {
        // Tareas bloqueadas
        List<ProjectTask> blockedTasks = projectTaskService.findBlockedTasks(project.getId());
        if (!blockedTasks.isEmpty()) {
            alertService.sendAlert(Alert.builder()
                .type(AlertType.TASKS_BLOCKED)
                .severity(AlertSeverity.MEDIUM)
                .title("Blocked Tasks - " + project.getName())
                .message(String.format(
                    "%d tasks are currently blocked",
                    blockedTasks.size()
                ))
                .projectId(project.getId())
                .build());
        }
        
        // Tareas overdue
        List<ProjectTask> overdueTasks = projectTaskService.findOverdueTasks(project.getId());
        if (overdueTasks.size() > 5) {
            alertService.sendAlert(Alert.builder()
                .type(AlertType.TASKS_OVERDUE)
                .severity(AlertSeverity.HIGH)
                .title("Multiple Overdue Tasks - " + project.getName())
                .message(String.format(
                    "%d tasks are overdue",
                    overdueTasks.size()
                ))
                .projectId(project.getId())
                .build());
        }
    }
}
```

### **Tipos de Alertas:**

| Tipo | Severidad | Descripción |
|------|-----------|-------------|
| **BUDGET_WARNING** | MEDIUM | Presupuesto al 80% |
| **BUDGET_CRITICAL** | HIGH | Presupuesto al 90% |
| **BUDGET_EXCEEDED** | CRITICAL | Presupuesto excedido |
| **TIMELINE_AT_RISK** | HIGH | Timeline en riesgo |
| **PROJECT_OVERDUE** | CRITICAL | Proyecto vencido |
| **TASKS_BLOCKED** | MEDIUM | Tareas bloqueadas |
| **TASKS_OVERDUE** | HIGH | Múltiples tareas vencidas |
| **TEAM_UNDERUTILIZED** | LOW | Equipo subutilizado |
| **RESOURCE_SPIKE** | MEDIUM | Pico en uso de recursos |

---

## 📊 REPORTES

### **Reportes Disponibles:**

1. **Project Health Report** - Estado general de proyectos
2. **Budget Analysis Report** - Análisis de presupuesto
3. **Team Performance Report** - Rendimiento del equipo
4. **ROI Analysis Report** - Análisis de retorno de inversión
5. **Resource Utilization Report** - Utilización de recursos
6. **Timeline Analysis Report** - Análisis de timeline

---

## 🎯 CONCLUSIÓN

El módulo **Projects** incluye monitoreo completo con:

- ✅ **Métricas de Proyecto** - Health, budget, timeline
- ✅ **Métricas de Equipo** - Performance, utilización
- ✅ **Métricas de Tareas** - Completion, backlog
- ✅ **Métricas de Recursos** - Consumo y costos
- ✅ **Alertas Automáticas** - 9 tipos de alertas
- ✅ **Dashboards** - 5 dashboards especializados
- ✅ **Vistas SQL** - 10+ vistas especializadas

**Monitoreo end-to-end para gestión completa de proyectos enterprise.**
