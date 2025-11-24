package com.codeflowx.platform.viewmodel.governance;

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
import com.codeflowx.govern.entity.governance.Policy;
import com.codeflowx.govern.entity.governance.PolicyAuditLog;
import com.codeflowx.govern.entity.governance.PolicyChecklistItem;
import com.codeflowx.govern.entity.governance.PolicyEvaluation;
import com.codeflowx.govern.entity.governance.PolicyRule;
import com.codeflowx.govern.service.governance.PolicyService;
import com.codeflowx.govern.service.governance.PolicyAuditLogService;
import com.codeflowx.govern.service.governance.PolicyChecklistItemService;
import com.codeflowx.govern.service.governance.PolicyEvaluationService;
import com.codeflowx.govern.service.governance.PolicyRuleService;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de Policy
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class PolicyDetailViewModel extends MasterPage {

    @WireVariable
    private PolicyService policyService;
    @WireVariable
    private PolicyAuditLogService policyAuditLogService;
    @WireVariable
    private PolicyChecklistItemService policyChecklistItemService;
    @WireVariable
    private PolicyEvaluationService policyEvaluationService;
    @WireVariable
    private PolicyRuleService policyRuleService;
    @WireVariable
    private BusinessService businessService; // Mantener para procedimientos almacenados y auditoría

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
    private Long idxpolicy;
    private boolean editing = false;
    private String pageTitle = "Detalle";

    // ========== Datos ==========
    private Policy currentPolicy;

    // ========== Validadores ==========
    private UniqueValidator unique;

    private String originalName = null;

    // ========== Listas para combos (FK) ==========
    private List<String> availableEnforcementlevels = new ArrayList<>();
    private List<String> availablePolicytypes = new ArrayList<>();
    private List<String> availableStatuss = new ArrayList<>();

    // ========== Tags/Roles JSONB (selección múltiple con chips) ==========

    // ========== Colecciones descendientes (tabs con lazy loading) ==========
    private List<PolicyAuditLog> subgovpolicyauditlogs = new ArrayList<>();
    private List<PolicyChecklistItem> subgovpolicychecklistitems = new ArrayList<>();
    private List<PolicyEvaluation> subgovpolicyevaluations = new ArrayList<>();
    private List<PolicyRule> subgovpolicyrules = new ArrayList<>();
    private boolean subgovpolicyauditlogsLoaded = false;
    private boolean subgovpolicychecklistitemsLoaded = false;
    private boolean subgovpolicyevaluationsLoaded = false;
    private boolean subgovpolicyrulesLoaded = false;

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
            idxpolicy = Long.valueOf(String.valueOf(dataParam));
        }

        log.info("Inicializando PolicyDetailViewModel - mode: {}, idxpolicy: {}", mode, idxpolicy);

        if ("NEW".equals(mode)) {
            initNew();
        } else if ("LOAD".equals(mode) && idxpolicy != null) {
            loadItem(idxpolicy);
        } else {
            log.error("Modo inválido o falta idxpolicy");
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/governance/governance-overview.zul", page.getFellow(IDDESKTOP), params);
        }

        // Inicializar validador de unicidad
        unique = new UniqueValidator(currentPolicy, businessService);
    }

    private void initNew() {
        log.debug("Inicializando nuevo registro");
        currentPolicy = new Policy();
        editing = false;
        pageTitle = "Crear Nuevo";
        loadEnforcementlevels();
        loadPolicytypes();
        loadStatuss();
    }

    private void loadItem(Long id) {
        try {
            log.debug("Cargando registro ID={}", id);

            // findById siempre recibe Long id (el PK)
            currentPolicy = policyService.findById(id);

            if (currentPolicy == null) {
                log.error("Registro no encontrado: ID={}", id);
                Messagebox.show("Registro no encontrado", "Error",
                    Messagebox.OK, Messagebox.ERROR);
                Map<String, Object> params = new HashMap<>();
                params.put("action", Action.LOAD);
                appendPage("plataforma/governance/governance-overview.zul", page.getFellow(IDDESKTOP), params);
                return;
            }

            editing = true;
            pageTitle = "Editar: " + currentPolicy.getName();
        loadEnforcementlevels();
        loadPolicytypes();
        loadStatuss();

            // Cargar tags/roles existentes desde JSON

            // Guardar valores originales para validación de unicidad
            originalName = currentPolicy.getName();

            // Auditar carga de registro
            logActivity("CONSULTA", "GOVPOLICIES", id, "Consulta: " + currentPolicy.getName());

        } catch (GovernanceServiceException e) {
            log.error("Error al cargar registro ID={}", id, e);
            Messagebox.show("Error al cargar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/governance/governance-overview.zul", page.getFellow(IDDESKTOP), params);
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

            boolean isNew = currentPolicy.getIdxpolicy() == null;

            if (isNew) {
                currentPolicy = policyService.create(currentPolicy);
                log.info("Registro creado exitosamente");
                logActivity("CREACION", "GOVPOLICIES", currentPolicy.getIdxpolicy(),
                    "Creado: " + currentPolicy.getName());
                Messagebox.show("Registro creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentPolicy = policyService.update(currentPolicy);
                log.info("Registro actualizado exitosamente");
                logActivity("EDICION", "GOVPOLICIES", currentPolicy.getIdxpolicy(),
                    "Actualizado: " + currentPolicy.getName());
                Messagebox.show("Registro actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }

            // Regresar al overview
            Map<String, Object> params = new HashMap<>();
            params.put("action", Action.LOAD);
            appendPage("plataforma/governance/governance-overview.zul", page.getFellow(IDDESKTOP), params);

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

        if (currentPolicy.getName() == null || currentPolicy.getName().trim().isEmpty()) {
            errors.append("- Name\n");
        }
        if (currentPolicy.getName() != null && currentPolicy.getName().length() > 100) {
            errors.append("- Name no puede exceder 100 caracteres\n");
        }
        if (currentPolicy.getCategory() == null || currentPolicy.getCategory().trim().isEmpty()) {
            errors.append("- Category\n");
        }
        if (currentPolicy.getCategory() != null && currentPolicy.getCategory().length() > 100) {
            errors.append("- Category no puede exceder 100 caracteres\n");
        }
        if (currentPolicy.getEnforcementlevel() == null || currentPolicy.getEnforcementlevel().trim().isEmpty()) {
            errors.append("- Enforcementlevel\n");
        }
        if (currentPolicy.getPolicytype() == null || currentPolicy.getPolicytype().trim().isEmpty()) {
            errors.append("- Policytype\n");
        }
        if (currentPolicy.getStatus() == null || currentPolicy.getStatus().trim().isEmpty()) {
            errors.append("- Status\n");
        }
        if (currentPolicy.getVersion() == null) {
            errors.append("- Version\n");
        }
        if (currentPolicy.getCreatedby() == null) {
            errors.append("- Created By\n");
        }
        if (currentPolicy.getCreatedat() == null) {
            errors.append("- Created At\n");
        }
        if (currentPolicy.getUpdatedat() == null) {
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
        params.put("dataParam", idxpolicy);
        params.put("action", Action.LOAD);
        appendPage("plataforma/governance/governance-overview.zul", page.getFellow(IDDESKTOP), params);
    }

    private void loadEnforcementlevels() {
        // TODO: Cargar valores desde configuración o BD
        availableEnforcementlevels.add("OPTION_1");
        availableEnforcementlevels.add("OPTION_2");
        availableEnforcementlevels.add("OPTION_3");
    }

    private void loadPolicytypes() {
        // TODO: Cargar valores desde configuración o BD
        availablePolicytypes.add("OPTION_1");
        availablePolicytypes.add("OPTION_2");
        availablePolicytypes.add("OPTION_3");
    }

    private void loadStatuss() {
        // TODO: Cargar valores desde configuración o BD
        availableStatuss.add("OPTION_1");
        availableStatuss.add("OPTION_2");
        availableStatuss.add("OPTION_3");
    }

    private void loadSubgovpolicyauditlogs() {
        try {
            if (currentPolicy != null && currentPolicy.getIdxpolicy() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "policy");
                criteria.setValues(new Object[]{currentPolicy.getIdxpolicy()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<PolicyAuditLog> result = policyAuditLogService.findAll(collectionParams, criterias);
                subgovpolicyauditlogs = result != null ? result.getContent() : new ArrayList<>();
                subgovpolicyauditlogsLoaded = true;
                log.debug("Cargados {} subgovpolicyauditlogs", subgovpolicyauditlogs.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subgovpolicyauditlogs", e);
            subgovpolicyauditlogs = new ArrayList<>();
        }
    }

    private void loadSubgovpolicychecklistitems() {
        try {
            if (currentPolicy != null && currentPolicy.getIdxpolicy() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "policy");
                criteria.setValues(new Object[]{currentPolicy.getIdxpolicy()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<PolicyChecklistItem> result = policyChecklistItemService.findAll(collectionParams, criterias);
                subgovpolicychecklistitems = result != null ? result.getContent() : new ArrayList<>();
                subgovpolicychecklistitemsLoaded = true;
                log.debug("Cargados {} subgovpolicychecklistitems", subgovpolicychecklistitems.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subgovpolicychecklistitems", e);
            subgovpolicychecklistitems = new ArrayList<>();
        }
    }

    private void loadSubgovpolicyevaluations() {
        try {
            if (currentPolicy != null && currentPolicy.getIdxpolicy() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "policy");
                criteria.setValues(new Object[]{currentPolicy.getIdxpolicy()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<PolicyEvaluation> result = policyEvaluationService.findAll(collectionParams, criterias);
                subgovpolicyevaluations = result != null ? result.getContent() : new ArrayList<>();
                subgovpolicyevaluationsLoaded = true;
                log.debug("Cargados {} subgovpolicyevaluations", subgovpolicyevaluations.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subgovpolicyevaluations", e);
            subgovpolicyevaluations = new ArrayList<>();
        }
    }

    private void loadSubgovpolicyrules() {
        try {
            if (currentPolicy != null && currentPolicy.getIdxpolicy() != null) {
                Criterias criterias = new Criterias();
                Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "policy");
                criteria.setValues(new Object[]{currentPolicy.getIdxpolicy()});
                criterias.addCriteria(criteria);

                // PageParams para colecciones (sin límite de paginación)
                PageParams collectionParams = PageParams.builder()
                    .maxRows(1000)
                    .pageActual(1)
                    .rowActual(0)
                    .build();

                PageResult<PolicyRule> result = policyRuleService.findAll(collectionParams, criterias);
                subgovpolicyrules = result != null ? result.getContent() : new ArrayList<>();
                subgovpolicyrulesLoaded = true;
                log.debug("Cargados {} subgovpolicyrules", subgovpolicyrules.size());
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar subgovpolicyrules", e);
            subgovpolicyrules = new ArrayList<>();
        }
    }

    @Command
    @NotifyChange("subgovpolicyauditlogs")
    public void onSelectSubgovpolicyauditlogsTab() {
        if (!subgovpolicyauditlogsLoaded) {
            loadSubgovpolicyauditlogs();
        }
    }

    @Command
    @NotifyChange("subgovpolicychecklistitems")
    public void onSelectSubgovpolicychecklistitemsTab() {
        if (!subgovpolicychecklistitemsLoaded) {
            loadSubgovpolicychecklistitems();
        }
    }

    @Command
    @NotifyChange("subgovpolicyevaluations")
    public void onSelectSubgovpolicyevaluationsTab() {
        if (!subgovpolicyevaluationsLoaded) {
            loadSubgovpolicyevaluations();
        }
    }

    @Command
    @NotifyChange("subgovpolicyrules")
    public void onSelectSubgovpolicyrulesTab() {
        if (!subgovpolicyrulesLoaded) {
            loadSubgovpolicyrules();
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
            currentPolicy = null;

            // Limpiar listas de FK

            // Limpiar listas de LIST_STRING
            if (availableEnforcementlevels != null) {
                availableEnforcementlevels.clear();
                availableEnforcementlevels = null;
            }
            if (availablePolicytypes != null) {
                availablePolicytypes.clear();
                availablePolicytypes = null;
            }
            if (availableStatuss != null) {
                availableStatuss.clear();
                availableStatuss = null;
            }

            // Limpiar colecciones @OneToMany
            if (subgovpolicyauditlogs != null) {
                subgovpolicyauditlogs.clear();
                subgovpolicyauditlogs = null;
            }
            subgovpolicyauditlogsLoaded = false;
            if (subgovpolicychecklistitems != null) {
                subgovpolicychecklistitems.clear();
                subgovpolicychecklistitems = null;
            }
            subgovpolicychecklistitemsLoaded = false;
            if (subgovpolicyevaluations != null) {
                subgovpolicyevaluations.clear();
                subgovpolicyevaluations = null;
            }
            subgovpolicyevaluationsLoaded = false;
            if (subgovpolicyrules != null) {
                subgovpolicyrules.clear();
                subgovpolicyrules = null;
            }
            subgovpolicyrulesLoaded = false;

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
