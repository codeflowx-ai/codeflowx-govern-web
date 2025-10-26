package com.codeflowx.platform.viewmodel.notifications;

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
import com.codeflowx.govern.entity.notifications.NotificationTemplate;
import com.codeflowx.govern.entity.notifications.Notification;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de NotificationTemplate
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class NotificationTemplateDetailViewModel extends MasterPage {
    
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
    private Long idxnotificationtemplate;
    private boolean editing = false;
    private String pageTitle = "Detalle";
    
    // ========== Datos ==========
    private NotificationTemplate currentNotificationTemplate;
    
    // ========== Validadores ==========
    private UniqueValidator unique;
    
    private String originalNtftemplatename = null;
    private String originalNtftemplatecode = null;
    
    // ========== Listas para combos (FK) ==========
    private List<String> availableNtftemplatetypes = new ArrayList<>();
    
    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========
    
    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    private List<Notification> subntfnotifications = new ArrayList<>();
    private boolean subntfnotificationsLoaded = false;
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        // Obtener parámetros de navegación
        mode = (String) super.action.name();
        
        // dataParam siempre contiene el ID (PK de tipo Long)
        if (dataParam != null) {
            idxnotificationtemplate = Long.valueOf(String.valueOf(dataParam));
        }
        
        log.info("Inicializando NotificationTemplateDetailViewModel - mode: {}, idxnotificationtemplate: {}", mode, idxnotificationtemplate);
        
        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxnotificationtemplate != null) {
            loadItem(idxnotificationtemplate);
        } else {
            log.error("Modo inválido o falta idxnotificationtemplate");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("gobierno/notifications/notifications-overview.zul", page.getFellow(IDDESKTOP), params);
        }
        
        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentNotificationTemplate, businessService);
    }
    
    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentNotificationTemplate = new NotificationTemplate();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadNtftemplatetypes();
    }
    
    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);
            
            // findById siempre recibe Long id (el PK)
            currentNotificationTemplate = businessService.findById(NotificationTemplate.class, id);
            
            if (currentNotificationTemplate == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/notifications/notifications-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }
            
            editing = true;
            pageTitle = "Editar: " + currentNotificationTemplate.getNtftemplatename();
        loadNtftemplatetypes();
            
            // Cargar tags/roles existentes desde JSON
            
            // Guardar valores originales para validación de unicidad
            originalNtftemplatename = currentNotificationTemplate.getNtftemplatename();
            originalNtftemplatecode = currentNotificationTemplate.getNtftemplatecode();
            
            // Auditar carga de registro
            logActivity("CONSULTA", "NTFTEMPLATES", id, "Consulta: " + currentNotificationTemplate.getNtftemplatename());
            
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
            
            boolean isNew = currentNotificationTemplate.getIdxnotificationtemplate() == null;
            
            if (isNew) {
                businessService.save(currentNotificationTemplate);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "NTFTEMPLATES", currentNotificationTemplate.getIdxnotificationtemplate(), 
                    "Creado: " + currentNotificationTemplate.getNtftemplatename());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                businessService.update(currentNotificationTemplate);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "NTFTEMPLATES", currentNotificationTemplate.getIdxnotificationtemplate(), 
                    "Actualizado: " + currentNotificationTemplate.getNtftemplatename());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }
            
            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/notifications/notifications-overview.zul", page.getFellow(IDDESKTOP), params);
            
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
        
        if (currentNotificationTemplate.getNtftemplatename() == null || currentNotificationTemplate.getNtftemplatename().trim().isEmpty()) {
            errors.append("- Ntftemplatename\n");
        }
        if (currentNotificationTemplate.getNtftemplatename() != null && currentNotificationTemplate.getNtftemplatename().length() > 255) {
            errors.append("- Ntftemplatename no puede exceder 255 caracteres\n");
        }
        if (currentNotificationTemplate.getNtftemplatecode() == null || currentNotificationTemplate.getNtftemplatecode().trim().isEmpty()) {
            errors.append("- Ntftemplatecode\n");
        }
        if (currentNotificationTemplate.getNtftemplatecode() != null && currentNotificationTemplate.getNtftemplatecode().length() > 100) {
            errors.append("- Ntftemplatecode no puede exceder 100 caracteres\n");
        }
        if (currentNotificationTemplate.getNtftemplatebody() == null || currentNotificationTemplate.getNtftemplatebody().trim().isEmpty()) {
            errors.append("- Ntftemplatebody\n");
        }
        if (currentNotificationTemplate.getNtftemplatetype() == null || currentNotificationTemplate.getNtftemplatetype().trim().isEmpty()) {
            errors.append("- Ntftemplatetype\n");
        }
        if (currentNotificationTemplate.getNtftemplatecreatedat() == null) {
            errors.append("- Ntftemplatecreatedat\n");
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
        params.put("dataParam", idxnotificationtemplate);
        params.put("action", Action.LOAD);
        appendPage("plataforma/notifications/notifications-overview.zul", page.getFellow(IDDESKTOP), params);
    }
    
    private void loadNtftemplatetypes() {
        // TODO: Cargar valores desde configuración o BD
        availableNtftemplatetypes.add("OPTION_1");
        availableNtftemplatetypes.add("OPTION_2");
        availableNtftemplatetypes.add("OPTION_3");
    }
    
    private void loadSubntfnotifications() {
        try {
            if (currentNotificationTemplate != null && currentNotificationTemplate.getIdxnotificationtemplate() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "template");
                criteria.setValues(new Object[]{currentNotificationTemplate.getIdxnotificationtemplate()});
                criterias.addCriteria(criteria);
                
                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();
                
                PageResult<Notification> result = businessService.findAllEntity(Notification.class, collectionParams, criterias);
                subntfnotifications = result != null ? result.getContent() : new ArrayList<>();
                subntfnotificationsLoaded = true;
                log.debug("Cargados {} subntfnotifications", subntfnotifications.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar subntfnotifications", e);
            subntfnotifications = new ArrayList<>();
        }
    }
    
    @Command
    @NotifyChange("subntfnotifications")
    public void onSelectSubntfnotificationsTab() {
        if (!subntfnotificationsLoaded) {
            loadSubntfnotifications();
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
            currentNotificationTemplate = null;
            
            // Limpiar listas de FK
            
            // Limpiar listas de LIST_STRING
            if (availableNtftemplatetypes != null) {
                availableNtftemplatetypes.clear();
                availableNtftemplatetypes = null;
            }
            
            // Limpiar colecciones @OneToMany
            if (subntfnotifications != null) {
                subntfnotifications.clear();
                subntfnotifications = null;
            }
            subntfnotificationsLoaded = false;
            
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
