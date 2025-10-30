package com.codeflowx.platform.viewmodel.governance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.orm.exception.DaoException;
import org.enartframework.suinsit.Context;
import org.enartframework.web.annotation.Action;
import org.enartframework.web.zk.page.MasterPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.framework.validators.UniqueValidator;
import com.codeflowx.govern.entity.governance.ComplianceAssessment;
import com.codeflowx.govern.entity.governance.ComplianceFinding;
import com.codeflowx.govern.entity.governance.ComplianceRequirement;
import com.codeflowx.admin.Ssoractividad;

import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de ComplianceAssessment
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ComplianceAssessmentDetailViewModel extends MasterPage {
    
    @WireVariable
    private BusinessService businessService;
    
    @Autowired
    protected IEntityLocal dao;
    
    @WireVariable
    public Environment environment;
    
    @WireVariable("context")
    protected GenericApplicationContext contexto;
    
    @WireVariable("ctxBean")
    protected Context ctxBean;
    
    @WireVariable("APPLICATION_DS")
    protected DataSource ds;
    
    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }
    
    @Override
    public void setBeans(Object bean) {
        // Auto-generated method stub
    }
    
    private static final long serialVersionUID = 1L;
    private static final String IDDESKTOP = "contenedor";
    
    // ========== Modo de operación ==========
    private String mode;
    private Long id;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private ComplianceAssessment currentComplianceAssessment;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    private String originalAssessmentname = null;
    private String originalAssessorname = null;
    private String originalAssessoremail = null;
    
    // ========== Listas para combos (FK) ==========
    private List<String> availableComplianceframeworks = new ArrayList<>();
    private List<String> availableStatuss = new ArrayList<>();
    
    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    
    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    private List<ComplianceFinding> subgovcompliancefindings = new ArrayList<>();
    private List<ComplianceRequirement> subgovcompliancerequirements = new ArrayList<>();
    private boolean subgovcompliancefindingsLoaded = false;
    private boolean subgovcompliancerequirementsLoaded = false;
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        // Obtener parámetros de navegación - con protección para action null

        
        if (super.action != null) {

        
            mode = super.action.name();

        
        } else {

        
            mode = (dataParam != null) ? "LOAD" : "NEW";

        
            log.warn("Action es null, infiriendo modo: {}", mode);

        
        }
        
        if (dataParam != null) {
            id = Long.parseLong(String.valueOf(dataParam));
        }
        
        log.info("Inicializando ComplianceAssessmentDetailViewModel - mode: {}, id: {}", mode, id);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && id != null) {
            loadItem(id);
        } else {
            log.error("Modo inválido o falta id");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/governance/governance-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentComplianceAssessment, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentComplianceAssessment = new ComplianceAssessment();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadComplianceframeworks();
        loadStatuss();
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            currentComplianceAssessment = businessService.findById(ComplianceAssessment.class, id);
            
            if (currentComplianceAssessment == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/governance/governance-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentComplianceAssessment.getAssessmentname();
        loadComplianceframeworks();
        loadStatuss();
            
            // Cargar tags/roles existentes desde JSON
            
            // Guardar valores originales para validación de unicidad
            originalAssessmentname = currentComplianceAssessment.getAssessmentname();
            originalAssessorname = currentComplianceAssessment.getAssessorname();
            originalAssessoremail = currentComplianceAssessment.getAssessoremail();
            
            // Auditar carga de registro
            logActivity("CONSULTA", "GOVCOMPLIANCEASSESSMENTS", id, "Consulta: " + currentComplianceAssessment.getAssessmentname());
            
        } catch (Exception e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/governance/governance-overview.zul", page.getFellow(IDDESKTOP), params);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void saveItem() {
        try {
            log.info("Guardando registro");
            
            // Validar campos obligatorios
            if (!validateRequiredFields()) {
                return;
            }
            
            boolean isNew = currentComplianceAssessment.getIdxcomplianceassessment() == null;
            
            if (isNew) {
                businessService.save(currentComplianceAssessment);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "GOVCOMPLIANCEASSESSMENTS", currentComplianceAssessment.getIdxcomplianceassessment(), 
                    "Creado: " + currentComplianceAssessment.getAssessmentname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                businessService.update(currentComplianceAssessment);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "GOVCOMPLIANCEASSESSMENTS", currentComplianceAssessment.getIdxcomplianceassessment(), 
                    "Actualizado: " + currentComplianceAssessment.getAssessmentname());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }
            
            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/governance/governance-overview.zul", page.getFellow(IDDESKTOP), params);
            
        } catch (Exception e) {
            log.error("Error al guardar", e);
            Messagebox.show("Error al guardar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    /**
     * Valida que todos los campos obligatorios estén completos
     * @return true si la validación es exitosa
     */
    private boolean validateRequiredFields() {
        StringBuilder errors = new StringBuilder();
        
        if (currentComplianceAssessment.getAssessmentname() == null || currentComplianceAssessment.getAssessmentname().trim().isEmpty()) {
            errors.append("- Assessment Name\n");
        }
        if (currentComplianceAssessment.getAssessmentname() != null && currentComplianceAssessment.getAssessmentname().length() > 100) {
            errors.append("- Assessment Name no puede exceder 100 caracteres\n");
        }
        if (currentComplianceAssessment.getDescription() == null || currentComplianceAssessment.getDescription().trim().isEmpty()) {
            errors.append("- Description\n");
        }
        if (currentComplianceAssessment.getComplianceframework() == null || currentComplianceAssessment.getComplianceframework().trim().isEmpty()) {
            errors.append("- Compliance Framework\n");
        }
        if (currentComplianceAssessment.getStatus() == null || currentComplianceAssessment.getStatus().trim().isEmpty()) {
            errors.append("- Status\n");
        }
        if (currentComplianceAssessment.getOverallscore() == null) {
            errors.append("- Overall Score\n");
        }
        if (currentComplianceAssessment.getAssessmentdate() == null) {
            errors.append("- Assessment Date\n");
        }
        if (currentComplianceAssessment.getValiduntil() == null) {
            errors.append("- Validuntil\n");
        }
        if (currentComplianceAssessment.getAssessorname() == null || currentComplianceAssessment.getAssessorname().trim().isEmpty()) {
            errors.append("- Assessorname\n");
        }
        if (currentComplianceAssessment.getAssessorname() != null && currentComplianceAssessment.getAssessorname().length() > 100) {
            errors.append("- Assessorname no puede exceder 100 caracteres\n");
        }
        if (currentComplianceAssessment.getAssessoremail() == null || currentComplianceAssessment.getAssessoremail().trim().isEmpty()) {
            errors.append("- Assessoremail\n");
        }
        if (currentComplianceAssessment.getAssessoremail() != null && currentComplianceAssessment.getAssessoremail().length() > 100) {
            errors.append("- Assessoremail no puede exceder 100 caracteres\n");
        }
        if (currentComplianceAssessment.getCreatedat() == null) {
            errors.append("- Created At\n");
        }
        if (currentComplianceAssessment.getUpdatedat() == null) {
            errors.append("- Updated At\n");
        }
        
        if (errors.length() > 0) {
            Messagebox.show("Por favor complete los siguientes campos:\n" + errors.toString(),
                "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
            return false;
        }
        
        return true;
    }
    
    @Command
    public void cancelEdit() {
        log.debug("Cancelando edición");
        Map<String, Object> params = new HashMap<>();
        params.put("dataParam", id);
        params.put("action", Action.LOAD);
        appendPage("plataforma/governance/governance-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    private void loadComplianceframeworks() {
        // TODO: Cargar valores desde configuración o BD
        availableComplianceframeworks.add("OPTION_1");
        availableComplianceframeworks.add("OPTION_2");
        availableComplianceframeworks.add("OPTION_3");
    }
    
    private void loadStatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableStatuss.add("OPTION_1");
        availableStatuss.add("OPTION_2");
        availableStatuss.add("OPTION_3");
    }
    
    private void loadSubgovcompliancefindings() {
        try {
            if (currentComplianceAssessment != null && currentComplianceAssessment.getIdxcomplianceassessment() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "assessment");
                criteria.setValues(new Object[]{currentComplianceAssessment.getIdxcomplianceassessment()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ComplianceFinding> result = businessService.findAllEntity(ComplianceFinding.class, collectionParams, criterias);
                subgovcompliancefindings = result != null ? result.getContent() : new ArrayList<>();
                subgovcompliancefindingsLoaded = true;
                log.debug("Cargados {} subgovcompliancefindings", subgovcompliancefindings.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subgovcompliancefindings", e);
            subgovcompliancefindings = new ArrayList<>();
        }
    }
    
    private void loadSubgovcompliancerequirements() {
        try {
            if (currentComplianceAssessment != null && currentComplianceAssessment.getIdxcomplianceassessment() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "assessment");
                criteria.setValues(new Object[]{currentComplianceAssessment.getIdxcomplianceassessment()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ComplianceRequirement> result = businessService.findAllEntity(ComplianceRequirement.class, collectionParams, criterias);
                subgovcompliancerequirements = result != null ? result.getContent() : new ArrayList<>();
                subgovcompliancerequirementsLoaded = true;
                log.debug("Cargados {} subgovcompliancerequirements", subgovcompliancerequirements.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subgovcompliancerequirements", e);
            subgovcompliancerequirements = new ArrayList<>();
        }
    }
    
    @Command
    @NotifyChange("subgovcompliancefindings")
    public void onSelectSubgovcompliancefindingsTab() {
        if (!subgovcompliancefindingsLoaded) {
            loadSubgovcompliancefindings();
        }
    }
    
    @Command
    @NotifyChange("subgovcompliancerequirements")
    public void onSelectSubgovcompliancerequirementsTab() {
        if (!subgovcompliancerequirementsLoaded) {
            loadSubgovcompliancerequirements();
        }
    }
    
    /**
     * audita las acciones de un usuario
     * @param action - buscar, edicion ,borrar,creacion ...
     * @param model - nombre del modulo/tabla
     * @param pk  - clave primaria del registro
     * @param mensaje  -- mensaje aclaratorio, ejemplo ha creado el modelo XXXX
     * @throws DaoException
     * @throws UiException
     */
    private void logActivity(String action, String model, Long pk, String mensaje) throws DaoException, UiException {
        try {
            Ssoractividad log = new Ssoractividad();
            log.setUsername(getUser().getUsername());
            log.setAccion(action);
            log.setAlta(new java.sql.Timestamp(System.currentTimeMillis()));
            log.setModulo(model);
            log.setIdtupla(pk != null ? pk.intValue() : 0);
            log.setAplicacion(ctxBean.getApplicationName());
            log.setValuetupla(mensaje);
            businessService.save(log);
        } catch (Exception e) {
            log.error("Error al auditar acción: {} en módulo: {}", action, model, e);
            // No lanzar excepción para que no interrumpa el flujo normal
        }
    }
    
    /**
     * Libera recursos y limpia referencias para ayudar al GC
     * Se llama automáticamente cuando el ViewModel se destruye
     */
    @Destroy
    public void destroy() {
        log.debug("[Destroy] Liberando recursos del ViewModel {}", this.getClass().getSimpleName());
        
        try {
            // Limpiar entidad actual
            currentComplianceAssessment = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            if (availableComplianceframeworks != null) {
                availableComplianceframeworks.clear();
                availableComplianceframeworks = null;
            }
            if (availableStatuss != null) {
                availableStatuss.clear();
                availableStatuss = null;
            }
            
            // Limpiar colecciones @OneToMany
            if (subgovcompliancefindings != null) {
                subgovcompliancefindings.clear();
                subgovcompliancefindings = null;
            }
            subgovcompliancefindingsLoaded = false;
            if (subgovcompliancerequirements != null) {
                subgovcompliancerequirements.clear();
                subgovcompliancerequirements = null;
            }
            subgovcompliancerequirementsLoaded = false;
            
            // Limpiar tags/roles JSONB
            
            // Limpiar validadores
            unique = null;
            
            // Limpiar BusinessService
            businessService = null;
            
            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }
}
