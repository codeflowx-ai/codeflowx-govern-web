package com.codeflowx.platform.viewmodel.core;
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
import com.codeflowx.govern.entity.core.User;
import com.codeflowx.govern.entity.monitoring.AuditLog;
import com.codeflowx.govern.entity.platform.UpdateSchedule;
import com.codeflowx.govern.entity.core.UserSession;
import com.codeflowx.govern.service.core.UserService;
import com.codeflowx.govern.service.monitoring.AuditLogService;
import com.codeflowx.govern.service.platform.UpdateScheduleService;
import com.codeflowx.govern.service.core.UserSessionService;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de User
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class UserDetailViewModel extends BaseFront<UserDetailViewModel>{

    @WireVariable
    private UserService userService;
    @WireVariable
    private AuditLogService auditLogService;
    @WireVariable
    private UserSessionService userSessionService;
    @WireVariable
    private UpdateScheduleService updateScheduleService;
    @WireVariable
    private BusinessService businessService; // Mantener para logActivity

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
    private Long idxuser;
    private boolean editing = false;
    private String pageTitle = "Detalle";

    // ========== Datos ==========
    private User currentUser;

    // ========== Validadores ==========
    private UniqueValidator unique;

    private String originalFirstname = null;
    private String originalLastname = null;
    private String originalUsername = null;
    private String originalEmail = null;

    // ========== Listas para combos (FK) ==========

    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========

    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    private List<AuditLog> subauditlogs = new ArrayList<>();
    private List<UserSession> subcorusersessions = new ArrayList<>();
    private List<UpdateSchedule> subupdateschedules = new ArrayList<>();
    private boolean subauditlogsLoaded = false;
    private boolean subcorusersessionsLoaded = false;
    private boolean subupdateschedulesLoaded = false;

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
            idxuser = Long.valueOf(String.valueOf(dataParam));
        }

        log.info("Inicializando UserDetailViewModel - mode: {}, idxuser: {}", mode, idxuser);

        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxuser != null) {
            loadItem(idxuser);
        } else {
            log.error("Modo inválido o falta idxuser");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/core/core-overview.zul", page.getFellow(IDDESKTOP), params);
        }

        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentUser, businessService);
    }

    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentUser = new User();
        editing = false;
        pageTitle = "Crear Nuevo";
    }

    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);

            // findById siempre recibe Long id (el PK)
            currentUser = userService.findById(id);

            if (currentUser == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error",
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/core/core-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }

            editing = true;
            pageTitle = "Editar: " + currentUser.getFirstname();

            // Cargar tags/roles existentes desde JSON

            // Guardar valores originales para validación de unicidad
            originalFirstname = currentUser.getFirstname();
            originalLastname = currentUser.getLastname();
            originalUsername = currentUser.getUsername();
            originalEmail = currentUser.getEmail();

            // Auditar carga de registro
            logActivity("CONSULTA", "CORUSERS", id, "Consulta: " + currentUser.getFirstname());

        } catch (GovernanceServiceException e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/core/core-overview.zul", page.getFellow(IDDESKTOP), params);
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

            boolean isNew = currentUser.getIdxuser() == null;

            if (isNew) {
                currentUser = userService.create(currentUser);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "CORUSERS", currentUser.getIdxuser(),
                    "Creado: " + currentUser.getFirstname());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentUser = userService.update(currentUser);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "CORUSERS", currentUser.getIdxuser(),
                    "Actualizado: " + currentUser.getFirstname());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }

            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/core/core-overview.zul", page.getFellow(IDDESKTOP), params);

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

        if (currentUser.getPasswordhash() == null || currentUser.getPasswordhash().trim().isEmpty()) {
            errors.append("- Passwordhash\n");
        }
        if (currentUser.getPasswordhash() != null && currentUser.getPasswordhash().length() > 100) {
            errors.append("- Passwordhash no puede exceder 100 caracteres\n");
        }
        if (currentUser.getCreatedat() == null) {
            errors.append("- Created At\n");
        }
        if (currentUser.getUpdatedat() == null) {
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
        params.put("dataParam", idxuser);
        params.put("action", Action.LOAD);
        appendPage("plataforma/core/core-overview.zul", page.getFellow(IDDESKTOP), params);
    }

    private void loadSubauditlogs() {
        try {
            if (currentUser != null && currentUser.getIdxuser() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "user");
                criteria.setValues(new Object[]{currentUser.getIdxuser()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<AuditLog> result = auditLogService.findAll(collectionParams, criterias);
                subauditlogs = result != null ? result.getContent() : new ArrayList<>();
                subauditlogsLoaded = true;
                log.debug("Cargados {} subauditlogs", subauditlogs.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subauditlogs", e);
            subauditlogs = new ArrayList<>();
        }
    }

    private void loadSubcorusersessions() {
        try {
            if (currentUser != null && currentUser.getIdxuser() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "user");
                criteria.setValues(new Object[]{currentUser.getIdxuser()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<UserSession> result = userSessionService.findAll(collectionParams, criterias);
                subcorusersessions = result != null ? result.getContent() : new ArrayList<>();
                subcorusersessionsLoaded = true;
                log.debug("Cargados {} subcorusersessions", subcorusersessions.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subcorusersessions", e);
            subcorusersessions = new ArrayList<>();
        }
    }

    private void loadSubupdateschedules() {
        try {
            if (currentUser != null && currentUser.getIdxuser() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "initiatedBy");
                criteria.setValues(new Object[]{currentUser.getIdxuser()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<UpdateSchedule> result = updateScheduleService.findAll(collectionParams, criterias);
                subupdateschedules = result != null ? result.getContent() : new ArrayList<>();
                subupdateschedulesLoaded = true;
                log.debug("Cargados {} subupdateschedules", subupdateschedules.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subupdateschedules", e);
            subupdateschedules = new ArrayList<>();
        }
    }

    @Command
    @NotifyChange("subauditlogs")
    public void onSelectSubauditlogsTab() {
        if (!subauditlogsLoaded) {
            loadSubauditlogs();
        }
    }

    @Command
    @NotifyChange("subcorusersessions")
    public void onSelectSubcorusersessionsTab() {
        if (!subcorusersessionsLoaded) {
            loadSubcorusersessions();
        }
    }

    @Command
    @NotifyChange("subupdateschedules")
    public void onSelectSubupdateschedulesTab() {
        if (!subupdateschedulesLoaded) {
            loadSubupdateschedules();
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
            currentUser = null;

            // Limpiar listas de FK

            // Limpiar listas de LIST_STRING

            // Limpiar colecciones @OneToMany
            if (subauditlogs != null) {
                subauditlogs.clear();
                subauditlogs = null;
            }
            subauditlogsLoaded = false;
            if (subcorusersessions != null) {
                subcorusersessions.clear();
                subcorusersessions = null;
            }
            subcorusersessionsLoaded = false;
            if (subupdateschedules != null) {
                subupdateschedules.clear();
                subupdateschedules = null;
            }
            subupdateschedulesLoaded = false;

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
