package com.codeflowx.platform.viewmodel.training;
import com.codeflowx.framework.zkoss.BaseFront;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.enartframework.suinsit.Context;
import javax.sql.DataSource;
import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.web.annotation.Action;
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
import com.codeflowx.govern.entity.training.TrainingGovernance;
import com.codeflowx.framework.validators.UniqueValidator;
import com.codeflowx.govern.service.training.TrainingGovernanceService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de TrainingGovernance
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class TrainingGovernanceDetailViewModel extends BaseFront<TrainingGovernanceDetailViewModel>{

    @WireVariable
    private BusinessService businessService;

    @WireVariable
    private TrainingGovernanceService trainingGovernanceService;

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
    private Long idxtraininggovernance;
    private boolean editing = false;
    private String pageTitle = "Detalle";

    // ========== Datos ==========
    private TrainingGovernance currentTrainingGovernance;

    // ========== Validadores ==========
    private UniqueValidator unique;


    // ========== Listas para combos (FK) ==========
    private List<String> availableGovernancetypes = new ArrayList<>();
    private List<String> availableCompliancestatuss = new ArrayList<>();
    private List<String> availableLicensetypes = new ArrayList<>();

    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========

    // ========== Colecciones descendientes (tabs con lazy loading) ==========

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
            idxtraininggovernance = Long.valueOf(String.valueOf(dataParam));
        }

        log.info("Inicializando TrainingGovernanceDetailViewModel - mode: {}, idxtraininggovernance: {}", mode, idxtraininggovernance);

        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxtraininggovernance != null) {
            loadItem(idxtraininggovernance);
        } else {
            log.error("Modo inválido o falta idxtraininggovernance");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/training/training-overview.zul", page.getFellow(IDDESKTOP), params);
        }

        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentTrainingGovernance, businessService);
    }

    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentTrainingGovernance = new TrainingGovernance();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadGovernancetypes();
        loadCompliancestatuss();
        loadLicensetypes();
    }

    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);

            // findById siempre recibe Long id (el PK)
            currentTrainingGovernance = trainingGovernanceService.findById(id);

            if (currentTrainingGovernance == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error",
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/training/training-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }

            editing = true;
            pageTitle = "Editar: " + currentTrainingGovernance.getIdxtraininggovernance();
        loadGovernancetypes();
        loadCompliancestatuss();
        loadLicensetypes();

            // Cargar tags/roles existentes desde JSON

            // Guardar valores originales para validación de unicidad

            // Auditar carga de registro
            logActivity("CONSULTA", "TRNTRAININGGOVERNANCE", id, "Consulta: " + currentTrainingGovernance.getIdxtraininggovernance());

        } catch (GovernanceServiceException e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/training/training-overview.zul", page.getFellow(IDDESKTOP), params);
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

            boolean isNew = currentTrainingGovernance.getIdxtraininggovernance() == null;

            if (isNew) {
                currentTrainingGovernance = trainingGovernanceService.create(currentTrainingGovernance);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "TRNTRAININGGOVERNANCE", currentTrainingGovernance.getIdxtraininggovernance(),
                    "Creado: " + currentTrainingGovernance.getIdxtraininggovernance());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentTrainingGovernance = trainingGovernanceService.update(currentTrainingGovernance);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "TRNTRAININGGOVERNANCE", currentTrainingGovernance.getIdxtraininggovernance(),
                    "Actualizado: " + currentTrainingGovernance.getIdxtraininggovernance());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }

            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/training/training-overview.zul", page.getFellow(IDDESKTOP), params);

        } catch (GovernanceServiceException e) {
            log.error("Error al guardar", e);
            Messagebox.show("Error al guardar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
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

        if (currentTrainingGovernance.getGovernancetype() == null || currentTrainingGovernance.getGovernancetype().trim().isEmpty()) {
            errors.append("- Governance Type\n");
        }
        if (currentTrainingGovernance.getCompliancestatus() == null || currentTrainingGovernance.getCompliancestatus().trim().isEmpty()) {
            errors.append("- Compliance Status\n");
        }
        if (currentTrainingGovernance.getCreatedat() == null) {
            errors.append("- Created At\n");
        }
        if (currentTrainingGovernance.getUpdatedat() == null) {
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
        params.put("dataParam", idxtraininggovernance);
        params.put("action", Action.LOAD);
        appendPage("plataforma/training/training-overview.zul", page.getFellow(IDDESKTOP), params);
    }

    private void loadGovernancetypes() {
        // TODO: Cargar valores desde configuración o BD
        availableGovernancetypes.add("OPTION_1");
        availableGovernancetypes.add("OPTION_2");
        availableGovernancetypes.add("OPTION_3");
    }

    private void loadCompliancestatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableCompliancestatuss.add("OPTION_1");
        availableCompliancestatuss.add("OPTION_2");
        availableCompliancestatuss.add("OPTION_3");
    }

    private void loadLicensetypes() {
        // TODO: Cargar valores desde configuración o BD
        availableLicensetypes.add("OPTION_1");
        availableLicensetypes.add("OPTION_2");
        availableLicensetypes.add("OPTION_3");
    }

    /**
     * audita las acciones de un usuario
     * @param action - buscar, edicion ,borrar,creacion ...
     * @param model - nombre del modulo/tabla
     * @param pk  - clave primaria del registro
     * @param mensaje  -- mensaje aclaratorio, ejemplo ha creado e    // No lanzar excepción para que no interrumpa el flujo normal
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
            currentTrainingGovernance = null;

            // Limpiar listas de FK

            // Limpiar listas de LIST_STRING
            if (availableGovernancetypes != null) {
                availableGovernancetypes.clear();
                availableGovernancetypes = null;
            }
            if (availableCompliancestatuss != null) {
                availableCompliancestatuss.clear();
                availableCompliancestatuss = null;
            }
            if (availableLicensetypes != null) {
                availableLicensetypes.clear();
                availableLicensetypes = null;
            }

            // Limpiar colecciones @OneToMany

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
