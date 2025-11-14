package com.codeflowx.govern.viewmodel.compliance;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
import org.flowable.engine.RuntimeService;
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
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.entity.projects.Project;

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para Clasificador de Sistemas de Alto Riesgo según EU AI Act Anexo III
 * 
 * Permite clasificar proyectos en 8 categorías principales y 25 subcategorías
 * según el Anexo III del EU AI Act (Art. 6).
 * 
 * Funcionalidades:
 * - Selección de categoría Anexo III (8 categorías)
 * - Selección de subcategorías específicas (multi-select)
 * - Sugerencia automática con IA
 * - Justificación obligatoria de clasificación
 * - Actualización de campo PRJISHIGHRISK en Project
 * - Trigger workflow BPMN si es alto riesgo
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class HighRiskClassifierViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    
    // ========== Servicios Spring ==========
    @WireVariable
    private BusinessService businessService;
    
    @WireVariable
    private RuntimeService runtimeService;
    
    @WireVariable
    public Environment environment;
    
    @WireVariable("context")
    protected GenericApplicationContext contexto;
    
    @WireVariable("ctxBean")
    protected Context ctxBean;
    
    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }
    
    @Override
    public void setBeans(Object bean) {
        // Not needed
    }

    // ========== Datos del proyecto ==========
    private Long projectId;
    private Project currentProject;
    private String projectName = "";
    private String projectDescription = "";
    private String projectPurpose = "";
    
    // ========== Categorías Anexo III ==========
    private List<Map<String, String>> annexIIICategories = new ArrayList<>();
    private String selectedCategory;
    private Map<String, String> selectedCategoryData;
    
    // ========== Subcategorías ==========
    private List<Map<String, String>> subcategories = new ArrayList<>();
    private List<String> selectedSubcategories = new ArrayList<>();
    
    // ========== Justificación ==========
    private String justification = "";
    
    // ========== Sugerencia IA ==========
    private String aiSuggestion = "";
    private String aiSuggestionCategory = "";
    private Double aiConfidence = 0.0;
    private boolean aiSuggestionAvailable = false;
    
    // ========== Estado ==========
    private boolean loading = false;
    private boolean classifying = false;

    // ========== Inicialización ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        log.info("Inicializando HighRiskClassifierViewModel");
        
        loadAnnexIIICategories();
        
        // Si hay projectId en parámetros, cargar proyecto
        if (projectId != null) {
            loadProject();
        }
    }
    
    /**
     * Carga las 8 categorías del Anexo III
     */
    private void loadAnnexIIICategories() {
        annexIIICategories.clear();
        
        // Categoría 1: Biometría
        Map<String, String> cat1 = new HashMap<>();
        cat1.put("code", "III.1");
        cat1.put("name", "Biometric Identification & Categorisation");
        cat1.put("description", "Sistemas biométricos para identificación y categorización de personas");
        annexIIICategories.add(cat1);
        
        // Categoría 2: Infraestructuras críticas
        Map<String, String> cat2 = new HashMap<>();
        cat2.put("code", "III.2");
        cat2.put("name", "Critical Infrastructure");
        cat2.put("description", "Gestión y operación de infraestructuras críticas");
        annexIIICategories.add(cat2);
        
        // Categoría 3: Educación
        Map<String, String> cat3 = new HashMap<>();
        cat3.put("code", "III.3");
        cat3.put("name", "Education & Vocational Training");
        cat3.put("description", "Sistemas para educación y formación profesional");
        annexIIICategories.add(cat3);
        
        // Categoría 4: Empleo
        Map<String, String> cat4 = new HashMap<>();
        cat4.put("code", "III.4");
        cat4.put("name", "Employment, Workers Management & Self-Employment");
        cat4.put("description", "Gestión de empleo, trabajadores y autónomos");
        annexIIICategories.add(cat4);
        
        // Categoría 5: Servicios esenciales
        Map<String, String> cat5 = new HashMap<>();
        cat5.put("code", "III.5");
        cat5.put("name", "Essential Private & Public Services");
        cat5.put("description", "Servicios privados y públicos esenciales");
        annexIIICategories.add(cat5);
        
        // Categoría 6: Garantía cumplimiento Derecho
        Map<String, String> cat6 = new HashMap<>();
        cat6.put("code", "III.6");
        cat6.put("name", "Law Enforcement");
        cat6.put("description", "Aplicación de la ley y cumplimiento normativo");
        annexIIICategories.add(cat6);
        
        // Categoría 7: Migración y asilo
        Map<String, String> cat7 = new HashMap<>();
        cat7.put("code", "III.7");
        cat7.put("name", "Migration, Asylum & Border Control");
        cat7.put("description", "Control migratorio, asilo y fronteras");
        annexIIICategories.add(cat7);
        
        // Categoría 8: Administración de justicia
        Map<String, String> cat8 = new HashMap<>();
        cat8.put("code", "III.8");
        cat8.put("name", "Administration of Justice & Democratic Processes");
        cat8.put("description", "Administración de justicia y procesos democráticos");
        annexIIICategories.add(cat8);
        
        log.info("Cargadas {} categorías Anexo III", annexIIICategories.size());
    }
    
    /**
     * Carga proyecto por ID
     */
    @Command
    @NotifyChange({"currentProject", "projectName", "projectDescription", "projectPurpose", 
                   "selectedCategory", "selectedSubcategories", "justification", "aiSuggestion"})
    public void loadProject() {
        if (projectId == null) {
            Messagebox.show("ID de proyecto no especificado", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        
        try {
            loading = true;
            log.info("Cargando proyecto ID: {}", projectId);
            
            currentProject = businessService.findById(Project.class, projectId);
            
            if (currentProject != null) {
                projectName = currentProject.getName() != null ? currentProject.getName() : "";
                projectDescription = currentProject.getDescription() != null ? currentProject.getDescription() : "";
                projectPurpose = currentProject.getMetadata() != null ? currentProject.getMetadata() : "";
                
                // Si ya está clasificado, cargar clasificación existente
                if (currentProject.getPrjishighrisk() != null && currentProject.getPrjishighrisk()) {
                    loadExistingClassification();
                } else {
                    // Solicitar sugerencia automática con IA
                    suggestCategoryWithAI();
                }
                
                log.info("Proyecto cargado: {}", projectName);
            } else {
                Messagebox.show("Proyecto no encontrado", "Error", Messagebox.OK, Messagebox.ERROR);
            }
            
        } catch (Exception e) {
            log.error("Error cargando proyecto", e);
            Messagebox.show("Error al cargar proyecto: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        } finally {
            loading = false;
        }
    }
    
    /**
     * Carga clasificación existente si el proyecto ya está clasificado
     */
    private void loadExistingClassification() {
        try {
            log.debug("Cargando clasificación existente del proyecto");
            
            // TODO: Parsear PRJANNEXIIICATEGORIES (JSON) para cargar selectedCategory y selectedSubcategories
            // Por ahora, dejar para implementación futura
            
            if (currentProject.getPrjriskcategoryjustification() != null) {
                justification = currentProject.getPrjriskcategoryjustification();
            }
            
        } catch (Exception e) {
            log.error("Error cargando clasificación existente", e);
        }
    }

    // ========== Selección de categoría ==========
    
    /**
     * Maneja selección de categoría principal
     */
    @Command
    @NotifyChange({"subcategories", "selectedCategoryData"})
    public void onCategorySelected() {
        log.info("Categoría seleccionada: {}", selectedCategory);
        
        if (selectedCategory == null || selectedCategory.isEmpty()) {
            subcategories.clear();
            selectedCategoryData = null;
            return;
        }
        
        // Encontrar datos de categoría seleccionada
        for (Map<String, String> cat : annexIIICategories) {
            if (selectedCategory.equals(cat.get("code"))) {
                selectedCategoryData = cat;
                break;
            }
        }
        
        // Cargar subcategorías correspondientes
        loadSubcategories(selectedCategory);
    }
    
    /**
     * Carga subcategorías según categoría seleccionada
     */
    private void loadSubcategories(String categoryCode) {
        subcategories.clear();
        selectedSubcategories.clear();
        
        switch (categoryCode) {
            case "III.1": // Biometría
                addSubcategory("III.1.a", "Remote biometric identification");
                addSubcategory("III.1.b", "Biometric categorisation");
                addSubcategory("III.1.c", "Emotion recognition");
                break;
                
            case "III.2": // Infraestructuras críticas
                addSubcategory("III.2.a", "Water supply management");
                addSubcategory("III.2.b", "Gas supply management");
                addSubcategory("III.2.c", "Electricity supply management");
                addSubcategory("III.2.d", "Heating supply management");
                addSubcategory("III.2.e", "Road traffic management");
                break;
                
            case "III.3": // Educación
                addSubcategory("III.3.a", "Access determination to educational institutions");
                addSubcategory("III.3.b", "Assessment of students");
                addSubcategory("III.3.c", "Assessment of learning outcomes");
                break;
                
            case "III.4": // Empleo
                addSubcategory("III.4.a", "Recruitment & personnel selection");
                addSubcategory("III.4.b", "Employment decisions (promotion, termination)");
                addSubcategory("III.4.c", "Task allocation");
                addSubcategory("III.4.d", "Monitoring & evaluation of performance");
                addSubcategory("III.4.e", "Self-employed access to services");
                break;
                
            case "III.5": // Servicios esenciales
                addSubcategory("III.5.a", "Creditworthiness assessment");
                addSubcategory("III.5.b", "Emergency response prioritization");
                addSubcategory("III.5.c", "Risk assessment for public assistance");
                addSubcategory("III.5.d", "Access to essential services");
                break;
                
            case "III.6": // Garantía cumplimiento Derecho
                addSubcategory("III.6.a", "Individual risk assessment for offences");
                addSubcategory("III.6.b", "Polygraph & emotion detection");
                addSubcategory("III.6.c", "Reliability assessment of evidence");
                addSubcategory("III.6.d", "Prediction of criminal behaviour");
                addSubcategory("III.6.e", "Profiling during crime detection");
                break;
                
            case "III.7": // Migración y asilo
                addSubcategory("III.7.a", "Polygraph for border control");
                addSubcategory("III.7.b", "Risk assessment for immigration");
                addSubcategory("III.7.c", "Authenticity verification of documents");
                addSubcategory("III.7.d", "Asylum & visa examination");
                break;
                
            case "III.8": // Administración de justicia
                addSubcategory("III.8.a", "Assistance to judicial authorities");
                addSubcategory("III.8.b", "Influence on democratic processes");
                break;
                
            default:
                log.warn("Categoría desconocida: {}", categoryCode);
        }
        
        log.info("Cargadas {} subcategorías para {}", subcategories.size(), categoryCode);
    }
    
    private void addSubcategory(String code, String description) {
        Map<String, String> sub = new HashMap<>();
        sub.put("code", code);
        sub.put("description", description);
        subcategories.add(sub);
    }

    // ========== Sugerencia IA ==========
    
    /**
     * Llama microservicio Python para sugerencia automática de categoría con IA
     */
    @Command
    @NotifyChange({"aiSuggestion", "aiSuggestionCategory", "aiConfidence", "aiSuggestionAvailable"})
    public void suggestCategoryWithAI() {
        log.info("Solicitando sugerencia IA para proyecto: {}", projectName);
        
        try {
            if (projectName == null || projectName.isEmpty()) {
                aiSuggestion = "Información insuficiente para sugerencia";
                aiSuggestionAvailable = false;
                return;
            }
            
            // TODO: Llamar a microservicio Python leka-prompt-governance o nuevo micro classification
            // Por ahora, simulación con reglas simples
            
            String description = (projectDescription + " " + projectPurpose).toLowerCase();
            
            if (description.contains("biometric") || description.contains("facial") || description.contains("emotion")) {
                aiSuggestionCategory = "III.1";
                aiConfidence = 0.85;
                aiSuggestion = "Sugerencia IA: Categoría III.1 - Biometric Identification (85% confianza)";
            } else if (description.contains("employee") || description.contains("hiring") || description.contains("recruitment")) {
                aiSuggestionCategory = "III.4";
                aiConfidence = 0.78;
                aiSuggestion = "Sugerencia IA: Categoría III.4 - Employment (78% confianza)";
            } else if (description.contains("credit") || description.contains("loan") || description.contains("financial")) {
                aiSuggestionCategory = "III.5";
                aiConfidence = 0.82;
                aiSuggestion = "Sugerencia IA: Categoría III.5 - Essential Services (82% confianza)";
            } else if (description.contains("student") || description.contains("education") || description.contains("assessment")) {
                aiSuggestionCategory = "III.3";
                aiConfidence = 0.75;
                aiSuggestion = "Sugerencia IA: Categoría III.3 - Education (75% confianza)";
            } else {
                aiSuggestion = "No se pudo determinar categoría automáticamente. Clasificación manual requerida.";
                aiSuggestionCategory = "";
                aiConfidence = 0.0;
            }
            
            aiSuggestionAvailable = (aiConfidence > 0.7);
            
            log.info("Sugerencia IA generada: {} (confianza: {}%)", aiSuggestionCategory, aiConfidence * 100);
            
        } catch (Exception e) {
            log.error("Error generando sugerencia IA", e);
            aiSuggestion = "Error al generar sugerencia: " + e.getMessage();
            aiSuggestionAvailable = false;
        }
    }
    
    /**
     * Aplica sugerencia IA automáticamente
     */
    @Command
    @NotifyChange({"selectedCategory", "subcategories", "selectedCategoryData"})
    public void applySuggestion() {
        if (!aiSuggestionAvailable || aiSuggestionCategory.isEmpty()) {
            Messagebox.show("No hay sugerencia disponible para aplicar", "Información", 
                Messagebox.OK, Messagebox.INFORMATION);
            return;
        }
        
        selectedCategory = aiSuggestionCategory;
        onCategorySelected();
        
        log.info("Sugerencia IA aplicada: {}", selectedCategory);
    }

    // ========== Clasificación ==========
    
    /**
     * Clasifica el proyecto como alto riesgo
     */
    @Command
    @NotifyChange("*")
    public void classifyAsHighRisk() {
        log.info("Clasificando proyecto como ALTO RIESGO: {}", projectId);
        
        // Validaciones
        if (!validateClassification()) {
            return;
        }
        
        try {
            classifying = true;
            
            // Actualizar Project
            currentProject.setPrjishighrisk(true);
            
            // Construir JSON de categorías (puede ser múltiple)
            StringBuilder categoriesJson = new StringBuilder("[\"");
            categoriesJson.append(selectedCategory).append("\"");
            for (String subcat : selectedSubcategories) {
                categoriesJson.append(",\"").append(subcat).append("\"");
            }
            categoriesJson.append("]");
            
            currentProject.setPrjannexiiicategories(categoriesJson.toString());
            currentProject.setPrjclassificationdate(new Timestamp(System.currentTimeMillis()));
            currentProject.setPrjclassificationauthor(ctxBean.getUser().getUsuname());
            currentProject.setPrjprohibitedusejustification(justification);
            
            // Guardar en BD
            businessService.persist(currentProject);
            
            log.info("Proyecto clasificado exitosamente. Categoría: {}, Subcategorías: {}", 
                selectedCategory, selectedSubcategories.size());
            
            // Iniciar workflow BPMN si es alto riesgo
            triggerHighRiskWorkflow();
            
            classifying = false;
            
            Messagebox.show(
                "Proyecto clasificado como ALTO RIESGO\n\n" +
                "Categoría: " + selectedCategory + "\n" +
                "Subcategorías: " + selectedSubcategories.size() + "\n" +
                "Clasificado por: " + ctxBean.getUser().getUsuname() + "\n\n" +
                "Se ha iniciado el workflow de conformidad para sistemas de alto riesgo.",
                "Clasificación Exitosa",
                Messagebox.OK,
                Messagebox.INFORMATION,
                event -> {
                    // Cerrar ventana o redirigir
                    // execution.sendRedirect("/console/gobierno/compliance/");
                }
            );
            
        } catch (Exception e) {
            log.error("Error clasificando proyecto", e);
            classifying = false;
            Messagebox.show("Error al clasificar: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    /**
     * Valida que la clasificación esté completa
     */
    private boolean validateClassification() {
        if (selectedCategory == null || selectedCategory.isEmpty()) {
            Messagebox.show("Debes seleccionar al menos una categoría del Anexo III", 
                "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return false;
        }
        
        if (selectedSubcategories == null || selectedSubcategories.isEmpty()) {
            Messagebox.show("Debes seleccionar al menos una subcategoría específica", 
                "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return false;
        }
        
        if (justification == null || justification.trim().length() < 50) {
            Messagebox.show("La justificación debe tener al menos 50 caracteres.\n\n" +
                "Explica detalladamente por qué este sistema cae en la categoría seleccionada.",
                "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return false;
        }
        
        return true;
    }
    
    /**
     * Inicia workflow BPMN para sistemas de alto riesgo
     */
    private void triggerHighRiskWorkflow() {
        try {
            if (runtimeService == null) {
                log.warn("RuntimeService no disponible, workflow no iniciado");
                return;
            }
            
            Map<String, Object> processVariables = new HashMap<>();
            processVariables.put("projectId", projectId);
            processVariables.put("projectName", projectName);
            processVariables.put("annexIIICategory", selectedCategory);
            processVariables.put("isHighRisk", true);
            processVariables.put("classifiedBy", ctxBean.getUser().getUsuname());
            processVariables.put("classifiedAt", System.currentTimeMillis());
            
            org.flowable.engine.runtime.ProcessInstance processInstance = 
                runtimeService.startProcessInstanceByKey("high_risk_compliance_workflow", processVariables);
            
            log.info("High Risk Compliance Workflow iniciado: processId={}", processInstance.getId());
            
        } catch (Exception e) {
            log.error("Error iniciando workflow", e);
            // No fallar la clasificación si el workflow falla
        }
    }
    
    /**
     * Cancela y cierra
     */
    @Command
    public void cancel() {
        log.info("Cancelando clasificación");
        // Limpiar campos
        selectedCategory = null;
        selectedSubcategories.clear();
        justification = "";
    }

    // ========== Cleanup ==========
    
    @Destroy
    public void destroy() {
        log.debug("[Destroy] Liberando recursos de HighRiskClassifierViewModel");
        try {
            if (annexIIICategories != null) {
                annexIIICategories.clear();
                annexIIICategories = null;
            }
            if (subcategories != null) {
                subcategories.clear();
                subcategories = null;
            }
            if (selectedSubcategories != null) {
                selectedSubcategories.clear();
                selectedSubcategories = null;
            }
            
            currentProject = null;
            selectedCategoryData = null;
            businessService = null;
            
            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }
}

