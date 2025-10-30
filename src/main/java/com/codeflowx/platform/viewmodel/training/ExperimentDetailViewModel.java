package com.codeflowx.platform.viewmodel.training;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.enartframework.suinsit.Context;
import javax.sql.DataSource;
import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.web.annotation.Action;
import org.enartframework.web.zk.page.MasterPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import com.codeflowx.govern.entity.training.Experiment;
import com.codeflowx.govern.entity.training.ExperimentLineage;
import com.codeflowx.govern.entity.training.HPOExperiment;
import com.codeflowx.govern.entity.training.Run;
import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.framework.validators.UniqueValidator;
import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import org.enartframework.orm.exception.DaoException;
import org.zkoss.zk.ui.UiException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de Experiment
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ExperimentDetailViewModel extends MasterPage {
    
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
    private Long idxexperiment;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private Experiment currentExperiment;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    private String originalTrnname = null;
    
    // ========== Listas para combos (FK) ==========
    
    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    private List<String> selectedTrntags = new ArrayList<>();
    private String newTrntag = "";
    
    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    private List<ExperimentLineage> subtrnexperimentlineage = new ArrayList<>();
    private List<HPOExperiment> subtrnhpoexperiments = new ArrayList<>();
    private List<Run> subtrnruns = new ArrayList<>();
    private boolean subtrnexperimentlineageLoaded = false;
    private boolean subtrnhpoexperimentsLoaded = false;
    private boolean subtrnrunsLoaded = false;
    
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
        
        // dataParam siempre contiene el ID (PK de tipo Long)
        if (dataParam != null) {
            idxexperiment = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando ExperimentDetailViewModel - mode: {}, idxexperiment: {}", mode, idxexperiment);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxexperiment != null) {
            loadItem(idxexperiment);
        } else {
            log.error("Modo inválido o falta idxexperiment");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/training/training-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentExperiment, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentExperiment = new Experiment();
        editing = false;
        pageTitle = "Crear Nuevo";
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentExperiment = businessService.findById(Experiment.class, id);
            
            if (currentExperiment == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/training/training-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentExperiment.getTrnname();
            
            // Cargar tags/roles existentes desde JSON
            selectedTrntags = convertJsonToList(currentExperiment.getTrntags());
            
            // Guardar valores originales para validación de unicidad
            originalTrnname = currentExperiment.getTrnname();
            
            // Auditar carga de registro
            logActivity("CONSULTA", "TRNEXPERIMENTS", id, "Consulta: " + currentExperiment.getTrnname());
            
        } catch (Exception e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/training/training-overview.zul", page.getFellow(IDDESKTOP), params);
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
            
            boolean isNew = currentExperiment.getIdxexperiment() == null;
            
            if (isNew) {
                businessService.save(currentExperiment);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "TRNEXPERIMENTS", currentExperiment.getIdxexperiment(), 
                    "Creado: " + currentExperiment.getTrnname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                businessService.update(currentExperiment);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "TRNEXPERIMENTS", currentExperiment.getIdxexperiment(), 
                    "Actualizado: " + currentExperiment.getTrnname());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }
            
            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/training/training-overview.zul", page.getFellow(IDDESKTOP), params);
            
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
        
        if (currentExperiment.getTrnname() == null || currentExperiment.getTrnname().trim().isEmpty()) {
            errors.append("- Name\n");
        }
        if (currentExperiment.getTrnname() != null && currentExperiment.getTrnname().length() > 255) {
            errors.append("- Name no puede exceder 255 caracteres\n");
        }
        if (currentExperiment.getTrnartifactlocation() == null || currentExperiment.getTrnartifactlocation().trim().isEmpty()) {
            errors.append("- Artifact Location\n");
        }
        if (currentExperiment.getTrnartifactlocation() != null && currentExperiment.getTrnartifactlocation().length() > 500) {
            errors.append("- Artifact Location no puede exceder 500 caracteres\n");
        }
        if (currentExperiment.getTrnlifecyclestage() == null || currentExperiment.getTrnlifecyclestage().trim().isEmpty()) {
            errors.append("- Lifecyclestage\n");
        }
        if (currentExperiment.getTrnlifecyclestage() != null && currentExperiment.getTrnlifecyclestage().length() > 50) {
            errors.append("- Lifecyclestage no puede exceder 50 caracteres\n");
        }
        if (currentExperiment.getTrncreatedat() == null) {
            errors.append("- Created At\n");
        }
        if (currentExperiment.getTrnupdatedat() == null) {
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
        params.put("dataParam", idxexperiment);
        params.put("action", Action.LOAD);
        appendPage("plataforma/training/training-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    @Command
    @NotifyChange("{'selectedTrntags', 'currentExperiment'}")
    public void addTrntag() {
        if (newTrntag != null && !newTrntag.trim().isEmpty() && !selectedTrntags.contains(newTrntag.trim())) {
            selectedTrntags.add(newTrntag.trim());
            newTrntag = "";
            // Convertir lista a JSON y actualizar en currentExperiment
            currentExperiment.setTrntags(convertListToJson(selectedTrntags));
        }
    }
    
    @Command
    @NotifyChange("{'selectedTrntags', 'currentExperiment'}")
    public void removeTrntag(@BindingParam("tag") String tag) {
        selectedTrntags.remove(tag);
        // Convertir lista a JSON y actualizar en currentExperiment
        currentExperiment.setTrntags(convertListToJson(selectedTrntags));
    }
    
    private String convertListToJson(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) json.append(",");
            json.append("\"").append(list.get(i)).append("\"");
        }
        json.append("]");
        return json.toString();
    }
    
    private List<String> convertJsonToList(String json) {
        List<String> result = new ArrayList<>();
        if (json == null || json.trim().isEmpty() || json.equals("[]")) {
            return result;
        }
        // Simplificación: parseo básico de JSON array de strings
        String cleaned = json.replace("[", "").replace("]", "").replace("\"", "");
        if (!cleaned.isEmpty()) {
            for (String item : cleaned.split(",")) {
                result.add(item.trim());
            }
        }
        return result;
    }
    
    private void loadSubtrnexperimentlineage() {
        try {
            if (currentExperiment != null && currentExperiment.getIdxexperiment() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "experiment");
                criteria.setValues(new Object[]{currentExperiment.getIdxexperiment()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<ExperimentLineage> result = businessService.findAllEntity(ExperimentLineage.class, collectionParams, criterias);
                subtrnexperimentlineage = result != null ? result.getContent() : new ArrayList<>();
                subtrnexperimentlineageLoaded = true;
                log.debug("Cargados {} subtrnexperimentlineage", subtrnexperimentlineage.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subtrnexperimentlineage", e);
            subtrnexperimentlineage = new ArrayList<>();
        }
    }
    
    private void loadSubtrnhpoexperiments() {
        try {
            if (currentExperiment != null && currentExperiment.getIdxexperiment() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "experiment");
                criteria.setValues(new Object[]{currentExperiment.getIdxexperiment()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<HPOExperiment> result = businessService.findAllEntity(HPOExperiment.class, collectionParams, criterias);
                subtrnhpoexperiments = result != null ? result.getContent() : new ArrayList<>();
                subtrnhpoexperimentsLoaded = true;
                log.debug("Cargados {} subtrnhpoexperiments", subtrnhpoexperiments.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subtrnhpoexperiments", e);
            subtrnhpoexperiments = new ArrayList<>();
        }
    }
    
    private void loadSubtrnruns() {
        try {
            if (currentExperiment != null && currentExperiment.getIdxexperiment() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "experiment");
                criteria.setValues(new Object[]{currentExperiment.getIdxexperiment()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<Run> result = businessService.findAllEntity(Run.class, collectionParams, criterias);
                subtrnruns = result != null ? result.getContent() : new ArrayList<>();
                subtrnrunsLoaded = true;
                log.debug("Cargados {} subtrnruns", subtrnruns.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subtrnruns", e);
            subtrnruns = new ArrayList<>();
        }
    }
    
    @Command
    @NotifyChange("subtrnexperimentlineage")
    public void onSelectSubtrnexperimentlineageTab() {
        if (!subtrnexperimentlineageLoaded) {
            loadSubtrnexperimentlineage();
        }
    }
    
    @Command
    @NotifyChange("subtrnhpoexperiments")
    public void onSelectSubtrnhpoexperimentsTab() {
        if (!subtrnhpoexperimentsLoaded) {
            loadSubtrnhpoexperiments();
        }
    }
    
    @Command
    @NotifyChange("subtrnruns")
    public void onSelectSubtrnrunsTab() {
        if (!subtrnrunsLoaded) {
            loadSubtrnruns();
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
            currentExperiment = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            
            // Limpiar colecciones @OneToMany
            if (subtrnexperimentlineage != null) {
                subtrnexperimentlineage.clear();
                subtrnexperimentlineage = null;
            }
            subtrnexperimentlineageLoaded = false;
            if (subtrnhpoexperiments != null) {
                subtrnhpoexperiments.clear();
                subtrnhpoexperiments = null;
            }
            subtrnhpoexperimentsLoaded = false;
            if (subtrnruns != null) {
                subtrnruns.clear();
                subtrnruns = null;
            }
            subtrnrunsLoaded = false;
            
            // Limpiar tags/roles JSONB
            if (selectedTrntags != null) {
                selectedTrntags.clear();
                selectedTrntags = null;
            }
            newTrntag = null;
            
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
