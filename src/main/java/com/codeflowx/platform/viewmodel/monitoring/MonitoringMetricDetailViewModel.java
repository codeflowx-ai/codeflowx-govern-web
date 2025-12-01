package com.codeflowx.platform.viewmodel.monitoring;
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
import com.codeflowx.govern.entity.monitoring.MonitoringMetric;
import com.codeflowx.govern.service.monitoring.MonitoringMetricService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de MonitoringMetric
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class MonitoringMetricDetailViewModel extends BaseFront<MonitoringMetricDetailViewModel>{

    @WireVariable
    private MonitoringMetricService monitoringMetricService;

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
    private Long idxmonitoringmetric;
    private boolean editing = false;
    private String pageTitle = "Detalle";

    // ========== Datos ==========
    private MonitoringMetric currentMonitoringMetric;

    // ========== Validadores ==========
    private UniqueValidator unique;

    private String originalMonmetricname = null;

    // ========== Listas para combos (FK) ==========
    private List<String> availableMonmetrictypes = new ArrayList<>();

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
            idxmonitoringmetric = Long.valueOf(String.valueOf(dataParam));
        }

        log.info("Inicializando MonitoringMetricDetailViewModel - mode: {}, idxmonitoringmetric: {}", mode, idxmonitoringmetric);

        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxmonitoringmetric != null) {
            loadItem(idxmonitoringmetric);
        } else {
            log.error("Modo inválido o falta idxmonitoringmetric");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("monitoring/dashboard/summary.zul", page.getFellow(IDDESKTOP), params);
        }

        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentMonitoringMetric, businessService);
    }

    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentMonitoringMetric = new MonitoringMetric();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadMonmetrictypes();
    }

    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);

            // findById siempre recibe Long id (el PK)
            currentMonitoringMetric = monitoringMetricService.findById(id);

            if (currentMonitoringMetric == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error",
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/monitoring/monitoring-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }

            editing = true;
            pageTitle = "Editar: " + currentMonitoringMetric.getMonmetricname();
        loadMonmetrictypes();

            // Cargar tags/roles existentes desde JSON

            // Guardar valores originales para validación de unicidad
            originalMonmetricname = currentMonitoringMetric.getMonmetricname();

            // Auditar carga de registro
            logActivity("CONSULTA", "MONMONITORINGMETRICS", id, "Consulta: " + currentMonitoringMetric.getMonmetricname());

        } catch (GovernanceServiceException e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/monitoring/monitoring-overview.zul", page.getFellow(IDDESKTOP), params);
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

            boolean isNew = currentMonitoringMetric.getIdxmonitoringmetric() == null;

            if (isNew) {
                currentMonitoringMetric = monitoringMetricService.create(currentMonitoringMetric);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "MONMONITORINGMETRICS", currentMonitoringMetric.getIdxmonitoringmetric(),
                    "Creado: " + currentMonitoringMetric.getMonmetricname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentMonitoringMetric = monitoringMetricService.update(currentMonitoringMetric);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "MONMONITORINGMETRICS", currentMonitoringMetric.getIdxmonitoringmetric(),
                    "Actualizado: " + currentMonitoringMetric.getMonmetricname());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }

            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/monitoring/monitoring-overview.zul", page.getFellow(IDDESKTOP), params);

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

        if (currentMonitoringMetric.getMonmetricname() == null || currentMonitoringMetric.getMonmetricname().trim().isEmpty()) {
            errors.append("- Metric Name\n");
        }
        if (currentMonitoringMetric.getMonmetricname() != null && currentMonitoringMetric.getMonmetricname().length() > 255) {
            errors.append("- Metric Name no puede exceder 255 caracteres\n");
        }
        if (currentMonitoringMetric.getMonmetrictype() == null || currentMonitoringMetric.getMonmetrictype().trim().isEmpty()) {
            errors.append("- Metric Type\n");
        }
        if (currentMonitoringMetric.getMonmetricvalue() == null) {
            errors.append("- Metric Value\n");
        }
        if (currentMonitoringMetric.getMoncreatedby() == null || currentMonitoringMetric.getMoncreatedby().trim().isEmpty()) {
            errors.append("- Created By\n");
        }
        if (currentMonitoringMetric.getMoncreatedby() != null && currentMonitoringMetric.getMoncreatedby().length() > 255) {
            errors.append("- Created By no puede exceder 255 caracteres\n");
        }
        if (currentMonitoringMetric.getMoncreatedat() == null) {
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
        params.put("dataParam", idxmonitoringmetric);
        params.put("action", Action.LOAD);
        appendPage("plataforma/monitoring/monitoring-overview.zul", page.getFellow(IDDESKTOP), params);
    }

    private void loadMonmetrictypes() {
        // TODO: Cargar valores desde configuración o BD
        availableMonmetrictypes.add("OPTION_1");
        availableMonmetrictypes.add("OPTION_2");
        availableMonmetrictypes.add("OPTION_3");
    }

    /**
     * audita las acciones de un usuario
     * @param action - buscar, edicion ,borrar,creacion ...
     * @param model - nombre del modulo/tabla
     * @param pk  - clave primaria del registro
     * @param mensaje  -- mensaje aclaratorio, ejemplo ha creado el  // No lanzar excepción para que no interrumpa el flujo normal
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
            currentMonitoringMetric = null;

            // Limpiar listas de FK

            // Limpiar listas de LIST_STRING
            if (availableMonmetrictypes != null) {
                availableMonmetrictypes.clear();
                availableMonmetrictypes = null;
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
