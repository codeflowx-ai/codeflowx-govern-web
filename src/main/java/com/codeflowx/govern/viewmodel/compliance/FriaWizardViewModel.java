package com.codeflowx.govern.viewmodel.compliance;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.entity.compliance.FriaAssessment;
import com.codeflowx.govern.service.projects.ProjectService;
import com.codeflowx.govern.entity.projects.Project;

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para Wizard FRIA (Fundamental Rights Impact Assessment) según EU AI Act Art. 27
 * 
 * Wizard de 6 pasos correspondientes a los 6 elementos mandatorios del Art. 27.1:
 * 
 * Step 1: Descripción de procesos donde se usa el sistema IA (Art. 27.1.a)
 * Step 2: Período y frecuencia de uso (Art. 27.1.b)
 * Step 3: Categorías de personas afectadas (Art. 27.1.c)
 * Step 4: Riesgos específicos a derechos fundamentales (Art. 27.1.d)
 * Step 5: Medidas de supervisión humana (Art. 27.1.e)
 * Step 6: Medidas de mitigación (Art. 27.1.f)
 * 
 * Al finalizar, genera FRIA completo, calcula score de completitud y puede notificar autoridades.
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class FriaWizardViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    
    // ========== Servicios Spring ==========
    @WireVariable
    private ProjectService projectService;
    
    @WireVariable
    public Environment environment;
    
    @WireVariable("context")
    protected GenericApplicationContext contexto;
    
    @WireVariable("ctxBean")
    protected Context ctxBean;
    
    protected void initDao() {
        // Ya no es necesario inicializar BusinessService manualmente
        // El Service se inyecta automáticamente mediante @WireVariable
    }
    }
    
    @Override
    public void setBeans(Object bean) {
        // Not needed
    }

    // ========== Control del Wizard ==========
    private int currentStep = 1;
    private static final int TOTAL_STEPS = 6;
    private boolean[] stepCompleted = new boolean[TOTAL_STEPS]; // Track completed steps
    
    // ========== Datos del proyecto ==========
    private Long projectId;
    private Project currentProject;
    private String projectName = "";
    
    // ========== STEP 1: Descripción Procesos (Art. 27.1.a) ==========
    private String processDescription = "";
    
    // ========== STEP 2: Período y Frecuencia (Art. 27.1.b) ==========
    private String usagePeriod = "";
    private String usageFrequency = "";
    private String usageStartDate = "";
    private String usageEndDate = "";
    
    // ========== STEP 3: Categorías Personas Afectadas (Art. 27.1.c) ==========
    private List<String> affectedCategories = new ArrayList<>();
    private List<Map<String, String>> availableCategories = new ArrayList<>();
    private boolean vulnerableGroupsIncluded = false;
    private String customCategory = "";
    
    // ========== STEP 4: Riesgos Específicos (Art. 27.1.d) ==========
    private List<Map<String, Object>> risks = new ArrayList<>();
    private String riskDescription = "";
    private String riskAffectedGroup = "";
    private String riskSeverity = "MEDIUM";
    private String riskProbability = "MEDIUM";
    private String riskImpact = "";
    
    // ========== STEP 5: Supervisión Humana (Art. 27.1.e) ==========
    private String humanOversightDescription = "";
    private boolean hitlEnabled = false;
    private boolean overrideCapability = false;
    private boolean humanTrainingProvided = false;
    private String oversightMeasures = "";
    
    // ========== STEP 6: Medidas Mitigación (Art. 27.1.f) ==========
    private List<Map<String, String>> mitigationMeasures = new ArrayList<>();
    private String mitigationDescription = "";
    private String mitigationType = "PREVENTIVE"; // PREVENTIVE, DETECTIVE, CORRECTIVE
    private String mitigationResponsible = "";
    
    // ========== Resultado FRIA ==========
    private FriaAssessment generatedFria;
    private BigDecimal completenessScore = BigDecimal.ZERO;
    private BigDecimal qualityScore = BigDecimal.ZERO;
    private boolean friaCompliant = false;
    
    // ========== Estado ==========
    private boolean generating = false;
    private boolean loading = false;
    private String wizardTitle = "Step 1: Process Description";
    private int progressPercent = 16; // 100/6 ≈ 16% per step

    // ========== Inicialización ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        log.info("Inicializando FriaWizardViewModel");
        
        initializeAvailableCategories();
        updateWizardState();
        
        // Si hay projectId en parámetros, cargar proyecto
        if (projectId != null) {
            loadProject();
        }
    }
    
    /**
     * Inicializa categorías de personas predefinidas
     */
    private void initializeAvailableCategories() {
        availableCategories.clear();
        
        addCategory("GENERAL_PUBLIC", "General Public");
        addCategory("EMPLOYEES", "Employees");
        addCategory("CUSTOMERS", "Customers / Users");
        addCategory("STUDENTS", "Students / Learners");
        addCategory("JOB_APPLICANTS", "Job Applicants");
        addCategory("VULNERABLE_ELDERLY", "Vulnerable Groups - Elderly");
        addCategory("VULNERABLE_CHILDREN", "Vulnerable Groups - Children");
        addCategory("VULNERABLE_DISABILITIES", "Vulnerable Groups - People with Disabilities");
        addCategory("MIGRANTS", "Migrants / Refugees");
        addCategory("ACCUSED_PERSONS", "Accused Persons");
        
        log.debug("Cargadas {} categorías de personas", availableCategories.size());
    }
    
    private void addCategory(String value, String label) {
        Map<String, String> cat = new HashMap<>();
        cat.put("value", value);
        cat.put("label", label);
        availableCategories.add(cat);
    }
    
    /**
     * Carga proyecto por ID
     */
    @Command
    @NotifyChange({"currentProject", "projectName"})
    public void loadProject() {
        if (projectId == null) {
            return;
        }
        
        try {
            loading = true;
            log.info("Cargando proyecto ID: {}", projectId);
            
            currentProject = projectService.findById(projectId);
            
            if (currentProject != null) {
                projectName = currentProject.getName() != null ? currentProject.getName() : "";
                log.info("Proyecto cargado: {}", projectName);
            }
            
        } catch (Exception e) {
            log.error("Error cargando proyecto", e);
            Messagebox.show("Error al cargar proyecto: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        } finally {
            loading = false;
        }
    }

    // ========== Navegación del Wizard ==========
    
    /**
     * Actualiza estado del wizard (título, progreso)
     */
    private void updateWizardState() {
        progressPercent = (int) ((currentStep / (double) TOTAL_STEPS) * 100);
        
        switch (currentStep) {
            case 1:
                wizardTitle = "Step 1: Process Description (Art. 27.1.a)";
                break;
            case 2:
                wizardTitle = "Step 2: Usage Period & Frequency (Art. 27.1.b)";
                break;
            case 3:
                wizardTitle = "Step 3: Affected Categories (Art. 27.1.c)";
                break;
            case 4:
                wizardTitle = "Step 4: Specific Risks (Art. 27.1.d)";
                break;
            case 5:
                wizardTitle = "Step 5: Human Oversight (Art. 27.1.e)";
                break;
            case 6:
                wizardTitle = "Step 6: Mitigation Measures (Art. 27.1.f)";
                break;
            default:
                wizardTitle = "FRIA Wizard";
        }
        
        log.debug("Wizard estado actualizado: Step {}/{} ({}%)", currentStep, TOTAL_STEPS, progressPercent);
    }
    
    /**
     * Navega al siguiente paso
     */
    @Command
    @NotifyChange({"currentStep", "wizardTitle", "progressPercent", "stepCompleted"})
    public void goNext() {
        log.info("Wizard: Avanzando desde step {}", currentStep);
        
        // Validar step actual
        if (!validateCurrentStep()) {
            return;
        }
        
        // Marcar step como completado
        stepCompleted[currentStep - 1] = true;
        
        // Avanzar
        if (currentStep < TOTAL_STEPS) {
            currentStep++;
            updateWizardState();
        } else {
            // Finalizar - generar FRIA
            generateFria();
        }
    }
    
    /**
     * Navega al paso anterior
     */
    @Command
    @NotifyChange({"currentStep", "wizardTitle", "progressPercent"})
    public void goPrevious() {
        if (currentStep > 1) {
            currentStep--;
            updateWizardState();
            log.info("Wizard: Retrocediendo a step {}", currentStep);
        }
    }
    
    /**
     * Valida que el step actual esté completo
     */
    private boolean validateCurrentStep() {
        String errorMessage = null;
        
        switch (currentStep) {
            case 1: // Process Description
                if (processDescription == null || processDescription.trim().length() < 100) {
                    errorMessage = "Process description must be at least 100 characters.\n\n" +
                        "Please provide a detailed description of the processes where the AI system is used.";
                }
                break;
                
            case 2: // Period & Frequency
                if ((usagePeriod == null || usagePeriod.trim().isEmpty()) &&
                    (usageStartDate == null || usageStartDate.trim().isEmpty())) {
                    errorMessage = "Please specify the usage period or start/end dates.";
                }
                if (usageFrequency == null || usageFrequency.trim().isEmpty()) {
                    errorMessage = "Please specify the usage frequency (e.g., 'Daily', 'Per transaction', '100 times/day').";
                }
                break;
                
            case 3: // Affected Categories
                if (affectedCategories.isEmpty()) {
                    errorMessage = "Please select at least one category of affected persons.";
                }
                break;
                
            case 4: // Specific Risks
                if (risks.isEmpty()) {
                    errorMessage = "Please add at least one specific risk to fundamental rights.\n\n" +
                        "Use the form below to describe risks to rights such as privacy, non-discrimination, " +
                        "freedom of expression, etc.";
                }
                break;
                
            case 5: // Human Oversight
                if (humanOversightDescription == null || humanOversightDescription.trim().length() < 50) {
                    errorMessage = "Human oversight description must be at least 50 characters.\n\n" +
                        "Describe how human supervision is implemented in the AI system.";
                }
                break;
                
            case 6: // Mitigation Measures
                if (mitigationMeasures.isEmpty()) {
                    errorMessage = "Please add at least one mitigation measure.\n\n" +
                        "Describe preventive, detective, or corrective measures to address the identified risks.";
                }
                break;
        }
        
        if (errorMessage != null) {
            Messagebox.show(errorMessage, "Validation - Step " + currentStep, 
                Messagebox.OK, Messagebox.EXCLAMATION);
            return false;
        }
        
        return true;
    }

    // ========== STEP 4: Gestión de Riesgos ==========
    
    /**
     * Añade nuevo riesgo a la lista
     */
    @Command
    @NotifyChange({"risks", "riskDescription", "riskAffectedGroup", "riskSeverity", "riskProbability", "riskImpact"})
    public void addRisk() {
        if (riskDescription == null || riskDescription.trim().isEmpty()) {
            Messagebox.show("Please provide a risk description", "Validation", 
                Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        
        Map<String, Object> risk = new HashMap<>();
        risk.put("description", riskDescription);
        risk.put("affectedGroup", riskAffectedGroup);
        risk.put("severity", riskSeverity);
        risk.put("probability", riskProbability);
        risk.put("impact", riskImpact);
        risk.put("id", System.currentTimeMillis()); // Temporal ID
        
        risks.add(risk);
        
        log.info("Riesgo añadido: {} (Severity: {}, Probability: {})", riskDescription, riskSeverity, riskProbability);
        
        // Limpiar form
        riskDescription = "";
        riskAffectedGroup = "";
        riskSeverity = "MEDIUM";
        riskProbability = "MEDIUM";
        riskImpact = "";
    }
    
    /**
     * Elimina riesgo de la lista
     */
    @Command
    @NotifyChange("risks")
    public void removeRisk(Long riskId) {
        risks.removeIf(r -> riskId.equals(r.get("id")));
        log.info("Riesgo eliminado: ID {}", riskId);
    }

    // ========== STEP 6: Gestión de Medidas Mitigación ==========
    
    /**
     * Añade medida de mitigación
     */
    @Command
    @NotifyChange({"mitigationMeasures", "mitigationDescription", "mitigationType", "mitigationResponsible"})
    public void addMitigationMeasure() {
        if (mitigationDescription == null || mitigationDescription.trim().isEmpty()) {
            Messagebox.show("Please provide a mitigation measure description", "Validation", 
                Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        
        Map<String, String> measure = new HashMap<>();
        measure.put("description", mitigationDescription);
        measure.put("type", mitigationType);
        measure.put("responsible", mitigationResponsible);
        measure.put("id", String.valueOf(System.currentTimeMillis()));
        
        mitigationMeasures.add(measure);
        
        log.info("Medida mitigación añadida: {} (Type: {})", mitigationDescription, mitigationType);
        
        // Limpiar form
        mitigationDescription = "";
        mitigationType = "PREVENTIVE";
        mitigationResponsible = "";
    }
    
    /**
     * Elimina medida de mitigación
     */
    @Command
    @NotifyChange("mitigationMeasures")
    public void removeMitigationMeasure(String measureId) {
        mitigationMeasures.removeIf(m -> measureId.equals(m.get("id")));
        log.info("Medida mitigación eliminada: ID {}", measureId);
    }

    // ========== Generación FRIA ==========
    
    /**
     * Genera FRIA completo
     */
    @Command
    @NotifyChange("*")
    public void generateFria() {
        log.info("Generando FRIA para proyecto: {}", projectId);
        
        try {
            generating = true;
            
            // Crear FriaAssessment
            FriaAssessment fria = new FriaAssessment();
            fria.setProject(currentProject);
            fria.setDeployerUser(null); // TODO: Set deployer user if available
            
            // Art. 27.1.a - Process Description
            fria.setFriaprocessdescription(processDescription);
            
            // Art. 27.1.b - Period & Frequency
            fria.setFriausageperiod(usagePeriod + " | " + usageStartDate + " - " + usageEndDate);
            fria.setFriausagefrequency(usageFrequency);
            
            // Art. 27.1.c - Affected Categories
            fria.setFriaaffectedcategories(convertListToJson(affectedCategories));
            fria.setFriavulnerablegroupsincluded(vulnerableGroupsIncluded);
            
            // Art. 27.1.d - Risks
            fria.setFriarisks(convertRisksToJson(risks));
            
            // Art. 27.1.e - Human Oversight
            fria.setFriahumanoversight(humanOversightDescription + "\n\nMeasures: " + oversightMeasures);
            fria.setFriahitlenabled(hitlEnabled);
            
            // Art. 27.1.f - Mitigation Measures
            fria.setFriamiti gationmeasures(convertMitigationToJson(mitigationMeasures));
            
            // Calculate scores
            completenessScore = calculateCompletenessScore();
            qualityScore = calculateQualityScore();
            friaCompliant = completenessScore.compareTo(new BigDecimal("80")) >= 0;
            
            fria.setFriacompletenesscore(completenessScore);
            fria.setFriaqualscore(qualityScore);
            fria.setFriaart27compliant(friaCompliant);
            
            // Severity assessment
            String severity = assessOverallSeverity();
            fria.setFriaimpactseverity(severity);
            
            // Charter articles (simplified)
            fria.setFriacharterarticles("[\"Art. 1\", \"Art. 3\", \"Art. 7\", \"Art. 8\", \"Art. 21\"]");
            
            // Timestamps
            fria.setFriacreatedat(new Timestamp(System.currentTimeMillis()));
            
            // Guardar en BD
            businessService.persist(fria);
            generatedFria = fria;
            
            log.info("FRIA generado exitosamente. ID: {}, Completeness: {}%, Quality: {}%", 
                fria.getIdxfriaassessment(), completenessScore, qualityScore);
            
            generating = false;
            
            // Mostrar resultado
            showFriaResult();
            
            // Si es obligatorio, notificar autoridad
            if (shouldNotifyAuthority()) {
                promptAuthorityNotification();
            }
            
        } catch (Exception e) {
            log.error("Error generando FRIA", e);
            generating = false;
            Messagebox.show("Error al generar FRIA: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    /**
     * Calcula score de completitud (todos los campos obligatorios completos)
     */
    private BigDecimal calculateCompletenessScore() {
        int totalFields = 6; // 6 elementos mandatorios Art. 27.1
        int completedFields = 0;
        
        if (processDescription != null && processDescription.length() >= 100) completedFields++;
        if (usagePeriod != null && !usagePeriod.isEmpty()) completedFields++;
        if (!affectedCategories.isEmpty()) completedFields++;
        if (!risks.isEmpty()) completedFields++;
        if (humanOversightDescription != null && humanOversightDescription.length() >= 50) completedFields++;
        if (!mitigationMeasures.isEmpty()) completedFields++;
        
        return BigDecimal.valueOf((completedFields / (double) totalFields) * 100).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    /**
     * Calcula score de calidad (profundidad del análisis)
     */
    private BigDecimal calculateQualityScore() {
        double score = 0.0;
        
        // Process description quality (length as proxy)
        if (processDescription.length() > 200) score += 15;
        else if (processDescription.length() > 100) score += 10;
        
        // Risks quality (number and detail)
        score += Math.min(risks.size() * 10, 30); // Max 30 points
        
        // Mitigation measures quality
        score += Math.min(mitigationMeasures.size() * 10, 25); // Max 25 points
        
        // Human oversight detail
        if (humanOversightDescription.length() > 150) score += 15;
        else if (humanOversightDescription.length() > 50) score += 10;
        
        // Vulnerable groups consideration
        if (vulnerableGroupsIncluded) score += 15;
        
        return BigDecimal.valueOf(Math.min(score, 100)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    /**
     * Evalúa severidad general del impacto
     */
    private String assessOverallSeverity() {
        int highCount = 0;
        int criticalCount = 0;
        
        for (Map<String, Object> risk : risks) {
            String severity = (String) risk.get("severity");
            if ("HIGH".equals(severity)) highCount++;
            if ("CRITICAL".equals(severity)) criticalCount++;
        }
        
        if (criticalCount > 0 || highCount > 2) return "CRITICAL";
        if (highCount > 0) return "HIGH";
        if (risks.size() > 3) return "MEDIUM";
        return "LOW";
    }
    
    /**
     * Determina si es obligatorio notificar a autoridad
     */
    private boolean shouldNotifyAuthority() {
        // Art. 27.3: Obligatorio notificar si impacto negativo significativo
        return "CRITICAL".equals(assessOverallSeverity()) || 
               "HIGH".equals(assessOverallSeverity());
    }
    
    /**
     * Prompt para notificar autoridad
     */
    private void promptAuthorityNotification() {
        Messagebox.show(
            "Authority Notification Required\n\n" +
            "According to Art. 27.3, due to the HIGH/CRITICAL impact severity, " +
            "this FRIA must be notified to the market surveillance authority.\n\n" +
            "Do you want to proceed with the notification now?",
            "Authority Notification",
            new Messagebox.Button[] {Messagebox.Button.YES, Messagebox.Button.NO},
            Messagebox.QUESTION,
            event -> {
                if (Messagebox.Button.YES.equals(event.getButton())) {
                    notifyAuthority();
                }
            }
        );
    }
    
    /**
     * Notifica autoridad
     */
    private void notifyAuthority() {
        try {
            // TODO: Implementar notificación real a autoridad
            
            generatedFria.setFrianotified(true);
            generatedFria.setFrianotificationid("NOTIF-" + System.currentTimeMillis());
            generatedFria.setFrianotificationdate(new Timestamp(System.currentTimeMillis()));
            
            businessService.persist(generatedFria);
            
            log.info("Autoridad notificada. Notification ID: {}", generatedFria.getFrianotificationid());
            
            Messagebox.show(
                "Authority notified successfully\n\n" +
                "Notification ID: " + generatedFria.getFrianotificationid() + "\n" +
                "Date: " + generatedFria.getFrianotificationdate(),
                "Success",
                Messagebox.OK,
                Messagebox.INFORMATION
            );
            
        } catch (Exception e) {
            log.error("Error notificando autoridad", e);
            Messagebox.show("Error notifying authority: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    /**
     * Muestra resultado FRIA
     */
    private void showFriaResult() {
        Messagebox.show(
            "FRIA Generated Successfully\n\n" +
            "FRIA ID: " + generatedFria.getIdxfriaassessment() + "\n" +
            "Project: " + projectName + "\n\n" +
            "Completeness Score: " + completenessScore + "%\n" +
            "Quality Score: " + qualityScore + "%\n" +
            "Art. 27 Compliant: " + (friaCompliant ? "YES ✓" : "NO ✗") + "\n" +
            "Impact Severity: " + generatedFria.getFriaimpactseverity() + "\n\n" +
            "The FRIA document has been saved and is ready for review and approval.\n\n" +
            (shouldNotifyAuthority() ? "⚠ Authority notification is REQUIRED due to high impact." : ""),
            "FRIA Complete",
            Messagebox.OK,
            friaCompliant ? Messagebox.INFORMATION : Messagebox.EXCLAMATION,
            event -> {
                // Redirigir a vista de FRIA o dashboard
                // Executions.sendRedirect("/console/gobierno/compliance/fria-review.zul?id=" + generatedFria.getIdxfriaassessment());
            }
        );
    }
    
    // ========== Utilidades ==========
    
    private String convertListToJson(List<String> list) {
        if (list == null || list.isEmpty()) return "[]";
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            json.append("\"").append(list.get(i)).append("\"");
            if (i < list.size() - 1) json.append(",");
        }
        json.append("]");
        return json.toString();
    }
    
    private String convertRisksToJson(List<Map<String, Object>> riskList) {
        // TODO: Implementar serialización JSON apropiada
        return "[]"; // Placeholder
    }
    
    private String convertMitigationToJson(List<Map<String, String>> measures) {
        // TODO: Implementar serialización JSON apropiada
        return "[]"; // Placeholder
    }
    
    /**
     * Cancela wizard
     */
    @Command
    public void cancel() {
        Messagebox.show(
            "Are you sure you want to cancel?\n\nAll progress will be lost.",
            "Cancel FRIA",
            new Messagebox.Button[] {Messagebox.Button.YES, Messagebox.Button.NO},
            Messagebox.QUESTION,
            event -> {
                if (Messagebox.Button.YES.equals(event.getButton())) {
                    log.info("FRIA wizard cancelado");
                    // Redirigir o cerrar
                    // Executions.sendRedirect("/console/gobierno/compliance/");
                }
            }
        );
    }

    // ========== Cleanup ==========
    
    @Destroy
    public void destroy() {
        log.debug("[Destroy] Liberando recursos de FriaWizardViewModel");
        try {
            if (affectedCategories != null) {
                affectedCategories.clear();
                affectedCategories = null;
            }
            if (risks != null) {
                risks.clear();
                risks = null;
            }
            if (mitigationMeasures != null) {
                mitigationMeasures.clear();
                mitigationMeasures = null;
            }
            if (availableCategories != null) {
                availableCategories.clear();
                availableCategories = null;
            }
            
            currentProject = null;
            generatedFria = null;
            projectService = null;
            
            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }
}

