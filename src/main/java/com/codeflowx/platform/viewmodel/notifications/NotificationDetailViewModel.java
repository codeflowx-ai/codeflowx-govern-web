package com.codeflowx.platform.viewmodel.notifications;
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
import com.codeflowx.govern.entity.notifications.Notification;
import com.codeflowx.govern.entity.notifications.NotificationLog;
import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.framework.validators.UniqueValidator;
import com.codeflowx.govern.service.notifications.NotificationService;
import com.codeflowx.govern.service.notifications.NotificationLogService;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de Notification
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class NotificationDetailViewModel extends BaseFront<NotificationDetailViewModel>{

    @WireVariable
    private BusinessService businessService;

    @WireVariable
    private NotificationService notificationService;

    @WireVariable
    private NotificationLogService notificationLogService;

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
    private Long idxnotification;
    private boolean editing = false;
    private String pageTitle = "Detalle";

    // ========== Datos ==========
    private Notification currentNotification;

    // ========== Validadores ==========
    private UniqueValidator unique;


    // ========== Listas para combos (FK) ==========
    private List<String> availableNtfnotificationtypes = new ArrayList<>();
    private List<String> availableNtfnotificationprioritys = new ArrayList<>();
    private List<String> availableNtfnotificationstatuss = new ArrayList<>();

    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========

    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    private List<NotificationLog> subntflogs = new ArrayList<>();
    private boolean subntflogsLoaded = false;

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
            idxnotification = Long.valueOf(String.valueOf(dataParam));
        }

        log.info("Inicializando NotificationDetailViewModel - mode: {}, idxnotification: {}", mode, idxnotification);

        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxnotification != null) {
            loadItem(idxnotification);
        } else {
            log.error("Modo inválido o falta idxnotification");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/notifications/notifications-overview.zul", page.getFellow(IDDESKTOP), params);
        }

        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentNotification, businessService);
    }

    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentNotification = new Notification();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadNtfnotificationtypes();
        loadNtfnotificationprioritys();
        loadNtfnotificationstatuss();
    }

    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);

            // findById siempre recibe Long id (el PK)
            currentNotification = notificationService.findById(id);

            if (currentNotification == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error",
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/notifications/notifications-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }

            editing = true;
            pageTitle = "Editar: " + currentNotification.getNtfnotificationtitle();
        loadNtfnotificationtypes();
        loadNtfnotificationprioritys();
        loadNtfnotificationstatuss();

            // Cargar tags/roles existentes desde JSON

            // Guardar valores originales para validación de unicidad

            // Auditar carga de registro
            logActivity("CONSULTA", "NTFNOTIFICATIONS", id, "Consulta: " + currentNotification.getNtfnotificationtitle());

        } catch (GovernanceServiceException e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/notifications/notifications-overview.zul", page.getFellow(IDDESKTOP), params);
        } catch (Exception e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/notifications/notifications-overview.zul", page.getFellow(IDDESKTOP), params);
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

            boolean isNew = currentNotification.getIdxnotification() == null;

            if (isNew) {
                currentNotification = notificationService.create(currentNotification);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "NTFNOTIFICATIONS", currentNotification.getIdxnotification(),
                    "Creado: " + currentNotification.getNtfnotificationtitle());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentNotification = notificationService.update(currentNotification);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "NTFNOTIFICATIONS", currentNotification.getIdxnotification(),
                    "Actualizado: " + currentNotification.getNtfnotificationtitle());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }

            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/notifications/notifications-overview.zul", page.getFellow(IDDESKTOP), params);

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

        if (currentNotification.getNtfnotificationtitle() == null || currentNotification.getNtfnotificationtitle().trim().isEmpty()) {
            errors.append("- Ntfnotificationtitle\n");
        }
        if (currentNotification.getNtfnotificationtitle() != null && currentNotification.getNtfnotificationtitle().length() > 255) {
            errors.append("- Ntfnotificationtitle no puede exceder 255 caracteres\n");
        }
        if (currentNotification.getNtfnotificationmessage() == null || currentNotification.getNtfnotificationmessage().trim().isEmpty()) {
            errors.append("- Ntfnotificationmessage\n");
        }
        if (currentNotification.getNtfnotificationtype() == null || currentNotification.getNtfnotificationtype().trim().isEmpty()) {
            errors.append("- Ntfnotificationtype\n");
        }
        if (currentNotification.getNtfnotificationpriority() == null || currentNotification.getNtfnotificationpriority().trim().isEmpty()) {
            errors.append("- Ntfnotificationpriority\n");
        }
        if (currentNotification.getNtfnotificationstatus() == null || currentNotification.getNtfnotificationstatus().trim().isEmpty()) {
            errors.append("- Ntfnotificationstatus\n");
        }
        if (currentNotification.getNtfnotificationuserid() == null || currentNotification.getNtfnotificationuserid().trim().isEmpty()) {
            errors.append("- Ntfnotificationuserid\n");
        }
        if (currentNotification.getNtfnotificationuserid() != null && currentNotification.getNtfnotificationuserid().length() > 255) {
            errors.append("- Ntfnotificationuserid no puede exceder 255 caracteres\n");
        }
        if (currentNotification.getNtfnotificationcreatedat() == null) {
            errors.append("- Ntfnotificationcreatedat\n");
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
        params.put("dataParam", idxnotification);
        params.put("action", Action.LOAD);
        appendPage("plataforma/notifications/notifications-overview.zul", page.getFellow(IDDESKTOP), params);
    }

    private void loadNtfnotificationtypes() {
        // TODO: Cargar valores desde configuración o BD
        availableNtfnotificationtypes.add("OPTION_1");
        availableNtfnotificationtypes.add("OPTION_2");
        availableNtfnotificationtypes.add("OPTION_3");
    }

    private void loadNtfnotificationprioritys() {
        // TODO: Cargar valores desde configuración o BD
        availableNtfnotificationprioritys.add("OPTION_1");
        availableNtfnotificationprioritys.add("OPTION_2");
        availableNtfnotificationprioritys.add("OPTION_3");
    }

    private void loadNtfnotificationstatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableNtfnotificationstatuss.add("OPTION_1");
        availableNtfnotificationstatuss.add("OPTION_2");
        availableNtfnotificationstatuss.add("OPTION_3");
    }

    private void loadSubntflogs() {
        try {
            if (currentNotification != null && currentNotification.getIdxnotification() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "notification");
                criteria.setValues(new Object[]{currentNotification.getIdxnotification()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<NotificationLog> result = notificationLogService.findAll(collectionParams, criterias);
                subntflogs = result != null ? result.getContent() : new ArrayList<>();
                subntflogsLoaded = true;
                log.debug("Cargados {} subntflogs", subntflogs.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subntflogs", e);
            subntflogs = new ArrayList<>();
        } catch (Exception e) {
            log.error("Error al cargar subntflogs", e);
            subntflogs = new ArrayList<>();
        }
    }

    @Command
    @NotifyChange("subntflogs")
    public void onSelectSubntflogsTab() {
        if (!subntflogsLoaded) {
            loadSubntflogs();
        }
    }

    /**
     * audita las acciones de un usuario
     * @param action - buscar, edicion ,borrar,creacion ...
     * @param model - nombre del modulo/tabla
     * @param pk  - clave primaria del registro
     * @param mensaje  -- mensaje aclaratorio, ejemplo ha creado el mode No lanzar excepción para que no interrumpa el flujo normal
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
            currentNotification = null;

            // Limpiar listas de FK

            // Limpiar listas de LIST_STRING
            if (availableNtfnotificationtypes != null) {
                availableNtfnotificationtypes.clear();
                availableNtfnotificationtypes = null;
            }
            if (availableNtfnotificationprioritys != null) {
                availableNtfnotificationprioritys.clear();
                availableNtfnotificationprioritys = null;
            }
            if (availableNtfnotificationstatuss != null) {
                availableNtfnotificationstatuss.clear();
                availableNtfnotificationstatuss = null;
            }

            // Limpiar colecciones @OneToMany
            if (subntflogs != null) {
                subntflogs.clear();
                subntflogs = null;
            }
            subntflogsLoaded = false;

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
