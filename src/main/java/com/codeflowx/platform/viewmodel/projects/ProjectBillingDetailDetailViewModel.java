package com.codeflowx.platform.viewmodel.projects;
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
import com.codeflowx.govern.entity.projects.ProjectBillingDetail;
import com.codeflowx.govern.service.projects.ProjectBillingDetailService;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de ProjectBillingDetail
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ProjectBillingDetailDetailViewModel extends BaseFront<ProjectBillingDetailDetailViewModel>{

    @WireVariable
    private ProjectBillingDetailService projectBillingDetailService;

    @WireVariable
    private BusinessService businessService; // Mantener para auditoría (Ssoractividad) y UniqueValidator

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
    private Long idxprojectbillingdetail;
    private boolean editing = false;
    private String pageTitle = "Detalle";

    // ========== Datos ==========
    private ProjectBillingDetail currentProjectBillingDetail;

    // ========== Validadores ==========
    private UniqueValidator unique;

    private String originalPrjresourcename = null;

    // ========== Listas para combos (FK) ==========
    private List<String> availablePrjresourcetypes = new ArrayList<>();

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
            idxprojectbillingdetail = Long.valueOf(String.valueOf(dataParam));
        }

        log.info("Inicializando ProjectBillingDetailDetailViewModel - mode: {}, idxprojectbillingdetail: {}", mode, idxprojectbillingdetail);

        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxprojectbillingdetail != null) {
            loadItem(idxprojectbillingdetail);
        } else {
            log.error("Modo inválido o falta idxprojectbillingdetail");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/projects/projects-overview.zul", page.getFellow(IDDESKTOP), params);
        }

        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentProjectBillingDetail, businessService);
    }

    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentProjectBillingDetail = new ProjectBillingDetail();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadPrjresourcetypes();
    }

    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);

            // findById siempre recibe Long id (el PK)
            currentProjectBillingDetail = projectBillingDetailService.findById(id);

            if (currentProjectBillingDetail == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error",
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/projects/projects-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }

            editing = true;
            pageTitle = "Editar: " + currentProjectBillingDetail.getPrjresourcename();
        loadPrjresourcetypes();

            // Cargar tags/roles existentes desde JSON

            // Guardar valores originales para validación de unicidad
            originalPrjresourcename = currentProjectBillingDetail.getPrjresourcename();

            // Auditar carga de registro
            logActivity("CONSULTA", "PRJBILLINGDETAILS", id, "Consulta: " + currentProjectBillingDetail.getPrjresourcename());

        } catch (GovernanceServiceException e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/projects/projects-overview.zul", page.getFellow(IDDESKTOP), params);
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

            boolean isNew = currentProjectBillingDetail.getIdxprojectbillingdetail() == null;

            if (isNew) {
                currentProjectBillingDetail = projectBillingDetailService.create(currentProjectBillingDetail);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "PRJBILLINGDETAILS", currentProjectBillingDetail.getIdxprojectbillingdetail(),
                    "Creado: " + currentProjectBillingDetail.getPrjresourcename());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentProjectBillingDetail = projectBillingDetailService.update(currentProjectBillingDetail);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "PRJBILLINGDETAILS", currentProjectBillingDetail.getIdxprojectbillingdetail(),
                    "Actualizado: " + currentProjectBillingDetail.getPrjresourcename());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }

            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/projects/projects-overview.zul", page.getFellow(IDDESKTOP), params);

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

        if (currentProjectBillingDetail.getPrjbillingdate() == null) {
            errors.append("- Billingdate\n");
        }
        if (currentProjectBillingDetail.getPrjcostcategory() == null || currentProjectBillingDetail.getPrjcostcategory().trim().isEmpty()) {
            errors.append("- Costcategory\n");
        }
        if (currentProjectBillingDetail.getPrjcostcategory() != null && currentProjectBillingDetail.getPrjcostcategory().length() > 50) {
            errors.append("- Costcategory no puede exceder 50 caracteres\n");
        }
        if (currentProjectBillingDetail.getPrjcreatedat() == null) {
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
        params.put("dataParam", idxprojectbillingdetail);
        params.put("action", Action.LOAD);
        appendPage("plataforma/projects/projects-overview.zul", page.getFellow(IDDESKTOP), params);
    }

    private void loadPrjresourcetypes() {
        // TODO: Cargar valores desde configuración o BD
        availablePrjresourcetypes.add("OPTION_1");
        availablePrjresourcetypes.add("OPTION_2");
        availablePrjresourcetypes.add("OPTION_3");
    }

    /**
     * audita las acciones de un usuario
     * @param action - buscar, edicion ,borrar,creacion ...
     * @param model - nombre del modulo/tabla
     * @param pk  - clave primaria del registro
     * @param mensaje  -- mensaje aclaratorio, ejemplo ha creado      // No lanzar excepción para que no interrumpa el flujo normal
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
            currentProjectBillingDetail = null;

            // Limpiar listas de FK

            // Limpiar listas de LIST_STRING
            if (availablePrjresourcetypes != null) {
                availablePrjresourcetypes.clear();
                availablePrjresourcetypes = null;
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
