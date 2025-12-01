package com.codeflowx.platform.viewmodel.platform;
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
import org.zkoss.bind.annotation.Init;
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
import com.codeflowx.govern.entity.platform.PlatformNode;
import com.codeflowx.govern.entity.platform.UpdateInstallation;
import com.codeflowx.framework.validators.UniqueValidator;
import com.codeflowx.govern.service.platform.PlatformNodeService;
import com.codeflowx.govern.service.platform.UpdateInstallationService;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de PlatformNode
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class PlatformNodeDetailViewModel extends BaseFront<PlatformNodeDetailViewModel> {

    @WireVariable
    private BusinessService businessService;

    @WireVariable
    private PlatformNodeService platformNodeService;

    @WireVariable
    private UpdateInstallationService updateInstallationService;

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
    private Long idxplatformnode;
    private boolean editing = false;
    private String pageTitle = "Detalle";

    // ========== Datos ==========
    private PlatformNode currentPlatformNode;

    // ========== Validadores ==========
    private UniqueValidator unique;

    private String originalNodename = null;

    // ========== Listas para combos (FK) ==========

    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========

    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    private List<UpdateInstallation> subupdateinstallations = new ArrayList<>();
    private boolean subupdateinstallationsLoaded = false;

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
            idxplatformnode = Long.valueOf(String.valueOf(dataParam));
        }

        log.info("Inicializando PlatformNodeDetailViewModel - mode: {}, idxplatformnode: {}", mode, idxplatformnode);

        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxplatformnode != null) {
            loadItem(idxplatformnode);
        } else {
            log.error("Modo inválido o falta idxplatformnode");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/platform/platform-overview.zul", page.getFellow(IDDESKTOP), params);
        }

        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentPlatformNode, businessService);
    }

    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentPlatformNode = new PlatformNode();
        editing = false;
        pageTitle = "Crear Nuevo";
    }

    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);

            // findById siempre recibe Long id (el PK)
            currentPlatformNode = platformNodeService.findById(id);

            if (currentPlatformNode == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error",
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/platform/platform-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }

            editing = true;
            pageTitle = "Editar: " + currentPlatformNode.getNodename();

            // Cargar tags/roles existentes desde JSON

            // Guardar valores originales para validación de unicidad
            originalNodename = currentPlatformNode.getNodename();

            // Auditar carga de registro
            logActivity("CONSULTA", "PLATFORMNODES", id, "Consulta: " + currentPlatformNode.getNodename());

        } catch (GovernanceServiceException e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/platform/platform-overview.zul", page.getFellow(IDDESKTOP), params);
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

            boolean isNew = currentPlatformNode.getIdxplatformnode() == null;

            if (isNew) {
                currentPlatformNode = platformNodeService.create(currentPlatformNode);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "PLATFORMNODES", currentPlatformNode.getIdxplatformnode(),
                    "Creado: " + currentPlatformNode.getNodename());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentPlatformNode = platformNodeService.update(currentPlatformNode);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "PLATFORMNODES", currentPlatformNode.getIdxplatformnode(),
                    "Actualizado: " + currentPlatformNode.getNodename());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }

            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/platform/platform-overview.zul", page.getFellow(IDDESKTOP), params);

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

        if (currentPlatformNode.getNodename() == null || currentPlatformNode.getNodename().trim().isEmpty()) {
            errors.append("- Nodename\n");
        }
        if (currentPlatformNode.getNodename() != null && currentPlatformNode.getNodename().length() > 100) {
            errors.append("- Nodename no puede exceder 100 caracteres\n");
        }
        if (currentPlatformNode.getLastheartbeat() == null) {
            errors.append("- Lastheartbeat\n");
        }
        if (currentPlatformNode.getCreatedat() == null) {
            errors.append("- Created At\n");
        }
        if (currentPlatformNode.getUpdatedat() == null) {
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
        params.put("dataParam", idxplatformnode);
        params.put("action", Action.LOAD);
        appendPage("plataforma/platform/platform-overview.zul", page.getFellow(IDDESKTOP), params);
    }

    private void loadSubupdateinstallations() {
        try {
            if (currentPlatformNode != null && currentPlatformNode.getIdxplatformnode() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "node");
                criteria.setValues(new Object[]{currentPlatformNode.getIdxplatformnode()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<UpdateInstallation> result = updateInstallationService.findAll(collectionParams, criterias);
                subupdateinstallations = result != null ? result.getContent() : new ArrayList<>();
                subupdateinstallationsLoaded = true;
                log.debug("Cargados {} subupdateinstallations", subupdateinstallations.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subupdateinstallations", e);
            subupdateinstallations = new ArrayList<>();
        }
    }

    @Command
    @NotifyChange("subupdateinstallations")
    public void onSelectSubupdateinstallationsTab() {
        if (!subupdateinstallationsLoaded) {
            loadSubupdateinstallations();
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
            currentPlatformNode = null;

            // Limpiar listas de FK

            // Limpiar listas de LIST_STRING

            // Limpiar colecciones @OneToMany
            if (subupdateinstallations != null) {
                subupdateinstallations.clear();
                subupdateinstallations = null;
            }
            subupdateinstallationsLoaded = false;

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
