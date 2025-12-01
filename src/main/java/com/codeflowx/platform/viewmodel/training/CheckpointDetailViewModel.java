package com.codeflowx.platform.viewmodel.training;

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
import com.codeflowx.framework.zkoss.BaseFront;
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
import com.codeflowx.govern.entity.training.Checkpoint;
import com.codeflowx.framework.validators.UniqueValidator;
import com.codeflowx.govern.service.training.CheckpointService;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de Checkpoint
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class CheckpointDetailViewModel extends BaseFront<CheckpointDetailViewModel> {

    @WireVariable
    private CheckpointService checkpointService;
    @WireVariable
    private BusinessService businessService; // Mantener para logActivity y UniqueValidator

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
    private Long idxcheckpoint;
    private boolean editing = false;
    private String pageTitle = "Detalle";

    // ========== Datos ==========
    private Checkpoint currentCheckpoint;

    // ========== Validadores ==========
    private UniqueValidator unique;

    private String originalTrncheckpointname = null;

    // ========== Listas para combos (FK) ==========
    private List<String> availableTrncheckpointtypes = new ArrayList<>();

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
            idxcheckpoint = Long.valueOf(String.valueOf(dataParam));
        }

        log.info("Inicializando CheckpointDetailViewModel - mode: {}, idxcheckpoint: {}", mode, idxcheckpoint);

        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxcheckpoint != null) {
            loadItem(idxcheckpoint);
        } else {
            log.error("Modo inválido o falta idxcheckpoint");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/training/training-overview.zul", page.getFellow(IDDESKTOP), params);
        }

        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentCheckpoint, businessService);
    }

    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentCheckpoint = new Checkpoint();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadTrncheckpointtypes();
    }

    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);

            // findById siempre recibe Long id (el PK)
            currentCheckpoint = checkpointService.findById(id);

            if (currentCheckpoint == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error",
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/training/training-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }

            editing = true;
            pageTitle = "Editar: " + currentCheckpoint.getTrncheckpointname();
        loadTrncheckpointtypes();

            // Cargar tags/roles existentes desde JSON

            // Guardar valores originales para validación de unicidad
            originalTrncheckpointname = currentCheckpoint.getTrncheckpointname();

            // Auditar carga de registro
            logActivity("CONSULTA", "TRNCHECKPOINTS", id, "Consulta: " + currentCheckpoint.getTrncheckpointname());

        } catch (GovernanceServiceException e) {
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
    public void saveCheckpoint() {
        saveItem();
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

            boolean isNew = currentCheckpoint.getIdxcheckpoint() == null;

            if (isNew) {
                currentCheckpoint = checkpointService.create(currentCheckpoint);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "TRNCHECKPOINTS", currentCheckpoint.getIdxcheckpoint(),
                    "Creado: " + currentCheckpoint.getTrncheckpointname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentCheckpoint = checkpointService.update(currentCheckpoint);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "TRNCHECKPOINTS", currentCheckpoint.getIdxcheckpoint(),
                    "Actualizado: " + currentCheckpoint.getTrncheckpointname());
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
        }
    }

    /**
     * Valida que todos los campos obligatorios estén completos
     * @return true si la validación es exitosa
     */
    private boolean validateRequiredFields() {
        StringBuilder errors = new StringBuilder();

        if (currentCheckpoint.getTrncheckpointname() == null || currentCheckpoint.getTrncheckpointname().trim().isEmpty()) {
            errors.append("- Checkpointname\n");
        }
        if (currentCheckpoint.getTrncheckpointname() != null && currentCheckpoint.getTrncheckpointname().length() > 255) {
            errors.append("- Checkpointname no puede exceder 255 caracteres\n");
        }
        if (currentCheckpoint.getTrncheckpointtype() == null || currentCheckpoint.getTrncheckpointtype().trim().isEmpty()) {
            errors.append("- Checkpointtype\n");
        }
        if (currentCheckpoint.getTrnpath() == null || currentCheckpoint.getTrnpath().trim().isEmpty()) {
            errors.append("- Path\n");
        }
        if (currentCheckpoint.getTrnpath() != null && currentCheckpoint.getTrnpath().length() > 500) {
            errors.append("- Path no puede exceder 500 caracteres\n");
        }
        if (currentCheckpoint.getTrncreatedat() == null) {
            errors.append("- Created At\n");
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
        params.put("dataParam", idxcheckpoint);
        params.put("action", Action.LOAD);
        appendPage("plataforma/training/training-overview.zul", page.getFellow(IDDESKTOP), params);
    }

    private void loadTrncheckpointtypes() {
        // TODO: Cargar valores desde configuración o BD
        availableTrncheckpointtypes.add("OPTION_1");
        availableTrncheckpointtypes.add("OPTION_2");
        availableTrncheckpointtypes.add("OPTION_3");
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
            currentCheckpoint = null;

            // Limpiar listas de FK

            // Limpiar listas de LIST_STRING
            if (availableTrncheckpointtypes != null) {
                availableTrncheckpointtypes.clear();
                availableTrncheckpointtypes = null;
            }

            // Limpiar colecciones @OneToMany

            // Limpiar tags/roles JSONB

            // Limpiar validadores
            unique = null;

            // Limpiar servicios
            checkpointService = null;
            businessService = null;

            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }
}
