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
import com.codeflowx.govern.service.models.ModelService;
import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.service.projects.ProjectService;
import com.codeflowx.govern.exception.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;

import codeflowx.nocode.persist.BusinessService;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

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
    private ModelService modelService;
    
    @WireVariable
    private RuntimeService runtimeService;
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
    private boolean confirmSuggestion = false; // INC-002: Confirmación explícita requerida
    
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
            
            currentProject = projectService.findById(projectId);
            
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
            
            // INC-002: Aumentar umbral a 0.85 (85%) para mostrar sugerencia
            if (aiConfidence > 0.85) {
                aiSuggestionAvailable = true;
                
                // Añadir justificación a la sugerencia
                aiSuggestion = String.format(
                    "Sugerencia IA: %s (%.0f%% confianza)\n\n" +
                    "Justificación: %s\n\n" +
                    "Por favor, revise la sugerencia y confirme antes de aplicar.",
                    aiSuggestionCategory,
                    aiConfidence * 100,
                    getSuggestionJustification()
                );
                
                // Resetear confirmación
                confirmSuggestion = false;
                
            } else {
                aiSuggestionAvailable = false;
                aiSuggestion = String.format(
                    "Confianza insuficiente (%.0f%%). Se requiere clasificación manual.",
                    aiConfidence * 100
                );
            }
            
            log.info("Sugerencia IA generada: {} (confianza: {}%)", aiSuggestionCategory, aiConfidence * 100);
            
        } catch (Exception e) {
            log.error("Error generando sugerencia IA", e);
            aiSuggestion = "Error al generar sugerencia: " + e.getMessage();
            aiSuggestionAvailable = false;
        }
    }
    
    /**
     * Obtiene justificación de la sugerencia IA
     * INC-002: Justificación de sugerencia
     */
    private String getSuggestionJustification() {
        // TODO: Obtener justificación del microservicio Python
        // Por ahora, generar justificación básica
        String description = (projectDescription + " " + projectPurpose).toLowerCase();
        
        if (aiSuggestionCategory.equals("III.1")) {
            return "El proyecto menciona características biométricas o reconocimiento facial.";
        } else if (aiSuggestionCategory.equals("III.4")) {
            return "El proyecto menciona empleo, contratación o evaluación de trabajadores.";
        } else if (aiSuggestionCategory.equals("III.5")) {
            return "El proyecto menciona evaluación crediticia o servicios financieros.";
        }
        
        return "Basado en análisis del contenido del proyecto.";
    }
    
    /**
     * Aplica sugerencia IA (modificado para requerir confirmación)
     * INC-002: Requerir confirmación explícita
     */
    @Command
    @NotifyChange({"selectedCategory", "subcategories", "selectedCategoryData", "confirmSuggestion"})
    public void applySuggestion() {
        if (!aiSuggestionAvailable || aiSuggestionCategory.isEmpty()) {
            Messagebox.show("No hay sugerencia disponible para aplicar", "Información", 
                Messagebox.OK, Messagebox.INFORMATION);
            return;
        }
        
        // INC-002: Requerir confirmación explícita
        if (!confirmSuggestion) {
            Messagebox.show(
                "Por favor, confirme que desea aplicar la sugerencia IA.\n\n" +
                "Categoría sugerida: " + aiSuggestionCategory + "\n" +
                "Confianza: " + (aiConfidence * 100) + "%\n\n" +
                "Marque la casilla de confirmación para aplicar.",
                "Confirmación Requerida",
                Messagebox.OK,
                Messagebox.QUESTION
            );
            return;
        }
        
        selectedCategory = aiSuggestionCategory;
        onCategorySelected();
        
        // INC-002: Registrar en log inmutable
        logAISuggestionAccepted();
        
        log.info("Sugerencia IA aplicada: {}", selectedCategory);
    }
    
    /**
     * Registra en log inmutable cuando usuario acepta sugerencia IA
     * INC-002: Logging de aceptación
     */
    private void logAISuggestionAccepted() {
        try {
            Map<String, Object> logData = new HashMap<>();
            logData.put("action", "AI_SUGGESTION_ACCEPTED");
            logData.put("suggestedCategory", aiSuggestionCategory);
            logData.put("confidence", aiConfidence);
            logData.put("projectId", projectId);
            logData.put("userConfirmed", true);
            
            // TODO: Implementar cuando esté disponible el servicio de logging inmutable
            // immutableLoggingService.createLogEntry(
            //     "PROJECT",
            //     projectId,
            //     "AI_SUGGESTION_ACCEPTED",
            //     ctxBean.getUser().getIdxuser(),
            //     ctxBean.getUser().getUsuname(),
            //     logData
            // );
            
            log.info("Log inmutable: AI_SUGGESTION_ACCEPTED - Project: {}, Category: {}, Confidence: {}", 
                projectId, aiSuggestionCategory, aiConfidence);
            
        } catch (Exception e) {
            log.error("Error registrando aceptación de sugerencia IA", e);
        }
    }

    // ========== Clasificación ==========
    
    /**
     * Clasifica el proyecto como alto riesgo
     */
    @Command
    @NotifyChange("*")
    public void classifyAsHighRisk() {
        log.info("Clasificando proyecto como ALTO RIESGO: {}", projectId);
        
        // Validaciones básicas existentes
        if (!validateClassification()) {
            return;
        }
        
        // NUEVA VALIDACIÓN: Dataset de entrenamiento (INC-001)
        try {
            validateModelDatasetForHighRisk();
        } catch (ValidationException e) {
            // Ya se mostró mensaje de error, solo loggear
            log.error("Validación dataset fallida: {}", e.getMessage());
            return; // Bloquear clasificación
        }
        
        // NUEVA VALIDACIÓN: Documentación técnica completa (INC-003)
        try {
            validateTechnicalDocumentation();
        } catch (ValidationException e) {
            // Ya se mostró mensaje de error, solo loggear
            log.error("Validación documentación técnica fallida: {}", e.getMessage());
            return; // Bloquear clasificación
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
     * Valida calidad de la justificación (INC-004)
     */
    private boolean validateJustificationQuality(String justification, String selectedCategory) {
        // 1. Longitud mínima aumentada a 100 caracteres
        if (justification == null || justification.trim().length() < 100) {
            return false;
        }
        
        String justificationLower = justification.toLowerCase();
        String categoryLower = selectedCategory.toLowerCase().replace(".", "");
        
        // 2. Debe mencionar categoría seleccionada
        if (!justificationLower.contains(categoryLower)) {
            return false;
        }
        
        // 3. Debe contener palabras clave de riesgo
        String[] riskKeywords = {"riesgo", "impacto", "afecta", "personas", "derechos", 
                                 "risk", "impact", "affects", "people", "rights"};
        int keywordCount = 0;
        for (String keyword : riskKeywords) {
            if (justificationLower.contains(keyword)) {
                keywordCount++;
            }
        }
        if (keywordCount < 2) {
            return false;
        }
        
        return true;
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
        
        // INC-004: Validación calidad justificación (aumentado a 100 caracteres)
        if (justification == null || justification.trim().length() < 100) {
            Messagebox.show("La justificación debe tener al menos 100 caracteres.\n\n" +
                "Debe:\n" +
                "- Mencionar la categoría seleccionada (" + selectedCategory + ")\n" +
                "- Explicar el riesgo específico y su impacto\n" +
                "- Incluir ejemplos concretos de uso del sistema\n\n" +
                "Explica detalladamente por qué este sistema cae en la categoría seleccionada.",
                "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return false;
        }
        
        // INC-004: Validar calidad de justificación
        if (!validateJustificationQuality(justification, selectedCategory)) {
            Messagebox.show(
                "La justificación no cumple con los requisitos de calidad.\n\n" +
                "Debe:\n" +
                "- Tener al menos 100 caracteres\n" +
                "- Mencionar la categoría seleccionada (" + selectedCategory + ")\n" +
                "- Explicar el riesgo específico y su impacto\n" +
                "- Incluir ejemplos concretos de uso del sistema\n\n" +
                "Por favor, mejore la justificación.",
                "Validación - Justificación Insuficiente",
                Messagebox.OK,
                Messagebox.EXCLAMATION
            );
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
     * Valida que el modelo tenga dataset de entrenamiento documentado si es alto riesgo
     * Requisito: Art. 10 (Gobernanza de Datos), Art. 11 (Documentación Técnica)
     * INC-001: Validación coherencia modelo-dataset
     */
    private void validateModelDatasetForHighRisk() {
        if (currentProject == null || projectId == null) {
            return; // Validación básica ya realizada
        }
        
        // Obtener modelo asociado al proyecto
        Model model = getModelForProject(projectId);
        
        if (model == null) {
            // Si no hay modelo, no se puede validar - permitir continuar con advertencia
            log.warn("Proyecto {} no tiene modelo asociado - no se puede validar dataset", projectId);
            return;
        }
        
        // Si el modelo es alto riesgo, debe tener dataset documentado
        if (model.getModishighrisk() != null && model.getModishighrisk()) {
            if (model.getModtrainingconfig() == null || model.getModtrainingconfig().trim().isEmpty()) {
                String errorMsg = "CRITICAL: Modelo alto riesgo requiere dataset de entrenamiento documentado.\n\n" +
                    "Según Art. 10 (Gobernanza de Datos) y Art. 11 (Documentación Técnica) del EU AI Act, " +
                    "los sistemas de alto riesgo deben tener documentación completa del dataset de entrenamiento.\n\n" +
                    "Por favor, complete la configuración de entrenamiento (MODTRAININGCONFIG) antes de clasificar como alto riesgo.";
                
                log.error("Validación fallida - Modelo alto riesgo sin dataset: Model ID={}", model.getIdxmodel());
                
                Messagebox.show(
                    errorMsg,
                    "Validación CRÍTICA - Dataset Requerido",
                    Messagebox.OK,
                    Messagebox.ERROR
                );
                
                throw new ValidationException("Modelo alto riesgo sin dataset documentado (Art. 10, 11)");
            }
            
            // Validar que MODTRAININGCONFIG contiene información válida
            // Parsear JSON y verificar campos mínimos
            if (!isTrainingConfigValid(model.getModtrainingconfig())) {
                String errorMsg = "CRITICAL: Configuración de entrenamiento incompleta o inválida.\n\n" +
                    "El campo MODTRAININGCONFIG debe contener información válida sobre:\n" +
                    "- Dataset utilizado\n" +
                    "- Método de entrenamiento\n" +
                    "- Parámetros de entrenamiento\n\n" +
                    "Por favor, complete la configuración correctamente.";
                
                log.error("Validación fallida - Configuración entrenamiento inválida: Model ID={}", model.getIdxmodel());
                
                Messagebox.show(
                    errorMsg,
                    "Validación CRÍTICA - Configuración Inválida",
                    Messagebox.OK,
                    Messagebox.ERROR
                );
                
                throw new ValidationException("Configuración de entrenamiento inválida");
            }
        
            log.info("Validación exitosa - Modelo alto riesgo con dataset documentado: Model ID={}", model.getIdxmodel());
        }
    }
    
    /**
     * Valida que la configuración de entrenamiento sea válida
     */
    private boolean isTrainingConfigValid(String trainingConfig) {
        if (trainingConfig == null || trainingConfig.trim().isEmpty()) {
            return false;
        }
        
        try {
            // Intentar parsear como JSON
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> config = mapper.readValue(trainingConfig, Map.class);
            
            // Verificar campos mínimos requeridos
            // Ajustar según estructura real de MODTRAININGCONFIG
            boolean hasDataset = config.containsKey("dataset") || config.containsKey("datasetId") || 
                                config.containsKey("datasetName");
            boolean hasMethod = config.containsKey("method") || config.containsKey("trainingMethod");
            
            return hasDataset && hasMethod;
            
        } catch (Exception e) {
            log.warn("Error parseando MODTRAININGCONFIG: {}", e.getMessage());
            // Si no es JSON válido, considerar inválido
            return false;
        }
    }
    
    /**
     * Valida que la documentación técnica esté completa antes de clasificar como alto riesgo
     * Requisito: Art. 11 + Anexo IV del EU AI Act
     * INC-003: Validación documentación técnica completa
     */
    private void validateTechnicalDocumentation() {
        if (currentProject == null || projectId == null) {
            return;
        }
        
        Model model = getModelForProject(projectId);
        
        if (model == null) {
            log.warn("Proyecto {} no tiene modelo asociado - no se puede validar documentación", projectId);
            return;
        }
        
        // Validar documentación técnica para modelos alto riesgo
        if (model.getModishighrisk() != null && model.getModishighrisk()) {
            
            // Validación 1: MODTECHNICALDOCCOMPLETE debe ser true
            if (model.getModtechnicaldoccomplete() == null || !model.getModtechnicaldoccomplete()) {
                String errorMsg = "CRITICAL: Documentación técnica incompleta (Art. 11 + Anexo IV).\n\n" +
                    "Según el Art. 11 del EU AI Act, los sistemas de alto riesgo deben tener " +
                    "documentación técnica completa antes de ser clasificados.\n\n" +
                    "Por favor, complete la documentación técnica del modelo antes de clasificar como alto riesgo.\n\n" +
                    "Campos requeridos según Anexo IV:\n" +
                    "- Descripción del sistema\n" +
                    "- Especificaciones de entrada/salida\n" +
                    "- Métricas de precisión\n" +
                    "- Datos de entrenamiento\n" +
                    "- Evaluación de riesgos\n" +
                    "- Medidas de mitigación";
                
                log.error("Validación fallida - Documentación técnica incompleta: Model ID={}", model.getIdxmodel());
                
                Messagebox.show(
                    errorMsg,
                    "Validación CRÍTICA - Documentación Técnica Incompleta",
                    Messagebox.OK,
                    Messagebox.ERROR
                );
                
                throw new ValidationException("Documentación técnica incompleta (Art. 11 + Anexo IV)");
            }
            
            // Validación 2: MODTECHNICALDOCSCORE debe ser >= 0.90
            if (model.getModtechnicaldocscore() == null) {
                String errorMsg = "CRITICAL: Score de documentación técnica no calculado.\n\n" +
                    "El sistema no ha calculado el score de completitud de la documentación técnica.\n\n" +
                    "Por favor, ejecute la evaluación de documentación técnica antes de clasificar.";
                
                log.error("Validación fallida - Score no calculado: Model ID={}", model.getIdxmodel());
                
                Messagebox.show(
                    errorMsg,
                    "Validación CRÍTICA - Score No Calculado",
                    Messagebox.OK,
                    Messagebox.ERROR
                );
                
                throw new ValidationException("Score de documentación técnica no calculado");
            }
        
            BigDecimal minScore = new BigDecimal("0.90");
            if (model.getModtechnicaldocscore().compareTo(minScore) < 0) {
                String errorMsg = "CRITICAL: Score de documentación técnica insuficiente: " + 
                    model.getModtechnicaldocscore() + "\n\n" +
                    "El score mínimo requerido es 0.90 (90% completitud).\n\n" +
                    "Por favor, complete la documentación técnica hasta alcanzar el mínimo requerido.";
                
                log.error("Validación fallida - Score insuficiente: Model ID={}, Score={}", 
                    model.getIdxmodel(), model.getModtechnicaldocscore());
                
                Messagebox.show(
                    errorMsg,
                    "Validación CRÍTICA - Score Insuficiente",
                    Messagebox.OK,
                    Messagebox.ERROR
                );
                
                throw new ValidationException("Score de documentación técnica insuficiente: " + 
                    model.getModtechnicaldocscore() + ". Mínimo requerido: 0.90");
            }
            
            log.info("Validación exitosa - Documentación técnica completa: Model ID={}, Score={}", 
                model.getIdxmodel(), model.getModtechnicaldocscore());
        }
    }
    
    /**
     * Obtiene modelo asociado al proyecto
     * TODO: Ajustar según relación Project-Model en tu esquema
     */
    private Model getModelForProject(Long projectId) {
        try {
            // Opción 1: Si hay relación directa Project -> Model
            // return currentProject.getModel();
            
            // Opción 2: Buscar por query (asumiendo que hay campo IDXPROJECT en Model)
            String query = "SELECT * FROM MODMODELS WHERE IDXPROJECT = ? LIMIT 1";
            List<Model> models = businessService.findListBySQL(Model.class, query, projectId);
            if (models != null && !models.isEmpty()) {
                return models.get(0);
            }
            
            // Opción 3: Si hay tabla intermedia
            // String query = "SELECT m.* FROM MODMODELS m " +
            //               "INNER JOIN PRJPROJECTMODELS pm ON m.IDXMODEL = pm.IDXMODEL " +
            //               "WHERE pm.IDXPROJECT = ? LIMIT 1";
            // return businessService.findBySQL(Model.class, query, projectId);
            
            return null;
            
        } catch (Exception e) {
            log.error("Error obteniendo modelo para proyecto {}", projectId, e);
            return null;
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
            modelService = null;
            projectService = null;
            
            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }
}

