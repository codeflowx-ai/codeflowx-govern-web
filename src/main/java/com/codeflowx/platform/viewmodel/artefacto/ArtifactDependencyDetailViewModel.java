package com.codeflowx.platform.viewmodel.artefacto;

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
import com.codeflowx.govern.entity.artefacto.ArtifactDependency;
import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.framework.validators.UniqueValidator;
import com.codeflowx.govern.service.artefacto.ArtifactDependencyService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de ArtifactDependency
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ArtifactDependencyDetailViewModel extends BaseFront<ArtifactDependencyDetailViewModel> {

    @WireVariable
    private BusinessService businessService;

    @WireVariable
    private ArtifactDependencyService artifactDependencyService;

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
    private Long idxartifactdependency;
    private boolean editing = false;
    private String pageTitle = "Detalle";

    // ========== Datos ==========
    private ArtifactDependency currentArtifactDependency;

    // ========== Validadores ==========
    private UniqueValidator unique;


    // ========== Listas para combos (FK) ==========
    private List<String> availableCatdeptypes = new ArrayList<>();

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
            idxartifactdependency = Long.valueOf(String.valueOf(dataParam));
        }

        log.info("Inicializando ArtifactDependencyDetailViewModel - mode: {}, idxartifactdependency: {}", mode, idxartifactdependency);

        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxartifactdependency != null) {
            loadItem(idxartifactdependency);
        } else {
            log.error("Modo inválido o falta idxartifactdependency");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/artefacto/artefacto-overview.zul", page.getFellow(IDDESKTOP), params);
        }

        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentArtifactDependency, businessService);
    }

    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentArtifactDependency = new ArtifactDependency();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadCatdeptypes();
    }

    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);

            // findById siempre recibe Long id (el PK)
            currentArtifactDependency = artifactDependencyService.findById(id);

            if (currentArtifactDependency == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error",
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/artefacto/artefacto-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }

            editing = true;
            pageTitle = "Editar: " + currentArtifactDependency.getCatdepdescription();
        loadCatdeptypes();

            // Cargar tags/roles existentes desde JSON

            // Guardar valores originales para validación de unicidad

            // Auditar carga de registro
            logActivity("CONSULTA", "CATARTIFACTDEPENDENCIES", id, "Consulta: " + currentArtifactDependency.getCatdepdescription());

        } catch (GovernanceServiceException e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/artefacto/artefacto-overview.zul", page.getFellow(IDDESKTOP), params);
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

            boolean isNew = currentArtifactDependency.getIdxartifactdependency() == null;

            if (isNew) {
                currentArtifactDependency = artifactDependencyService.create(currentArtifactDependency);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "CATARTIFACTDEPENDENCIES", currentArtifactDependency.getIdxartifactdependency(),
                    "Creado: " + currentArtifactDependency.getCatdepdescription());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentArtifactDependency = artifactDependencyService.update(currentArtifactDependency);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "CATARTIFACTDEPENDENCIES", currentArtifactDependency.getIdxartifactdependency(),
                    "Actualizado: " + currentArtifactDependency.getCatdepdescription());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }

            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/artefacto/artefacto-overview.zul", page.getFellow(IDDESKTOP), params);

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

        if (currentArtifactDependency.getCatdepsourceartifactid() == null) {
            errors.append("- Catdepsourceartifactid\n");
        }
        if (currentArtifactDependency.getCatdeptargetartifactid() == null) {
            errors.append("- Catdeptargetartifactid\n");
        }
        if (currentArtifactDependency.getCatdeptype() == null || currentArtifactDependency.getCatdeptype().trim().isEmpty()) {
            errors.append("- Catdeptype\n");
        }
        if (currentArtifactDependency.getCatdepcreatedat() == null) {
            errors.append("- Catdepcreatedat\n");
        }
        if (currentArtifactDependency.getCatdepupdatedat() == null) {
            errors.append("- Catdepupdatedat\n");
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
        params.put("dataParam", idxartifactdependency);
        params.put("action", Action.LOAD);
        appendPage("plataforma/artefacto/artefacto-overview.zul", page.getFellow(IDDESKTOP), params);
    }

    private void loadCatdeptypes() {
        // TODO: Cargar valores desde configuración o BD
        availableCatdeptypes.add("OPTION_1");
        availableCatdeptypes.add("OPTION_2");
        availableCatdeptypes.add("OPTION_3");
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
            currentArtifactDependency = null;

            // Limpiar listas de FK

            // Limpiar listas de LIST_STRING
            if (availableCatdeptypes != null) {
                availableCatdeptypes.clear();
                availableCatdeptypes = null;
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
