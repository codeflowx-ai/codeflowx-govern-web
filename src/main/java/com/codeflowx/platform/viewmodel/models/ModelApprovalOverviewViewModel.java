package com.codeflowx.platform.viewmodel.models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.orm.exception.DaoException;
import org.enartframework.suinsit.Context;
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
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.entity.models.ModelApproval;
import com.codeflowx.govern.service.models.ModelService;
import com.codeflowx.govern.service.models.ModelApprovalService;
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
 * ViewModel para gestión de aprobaciones de modelos ML
 * Permite aprobar/rechazar modelos pendientes de revisión
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ModelApprovalOverviewViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    private static final String IDDESKTOP = "contenedor";

    @WireVariable
    private ModelService modelService;
    @WireVariable
    private ModelApprovalService modelApprovalService;
    @WireVariable
    private BusinessService businessService; // Mantener para Ssoractividad

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

    // ========== Datos ==========
    private List<ModelApproval> pendingApprovals = new ArrayList<>();
    private List<Model> pendingModels = new ArrayList<>();

    // ========== Filtros ==========
    private String riskLevelFilter = "ALL";
    private String ownerFilter = "ALL";

    // ========== Modal de aprobación/rechazo ==========
    private ModelApproval selectedApproval;
    private Model selectedModel;
    private String approvalComments = "";
    private String rejectionReason = "";
    private boolean showApprovalModal = false;
    private boolean showRejectionModal = false;

    // ========== Métricas ==========
    private int totalPending = 0;
    private int totalApproved = 0;
    private int totalRejected = 0;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();

        log.info("Inicializando ModelApprovalOverviewViewModel");
        loadPendingApprovals();
        loadMetrics();
    }

    /**
     * Carga la lista de aprobaciones pendientes (PENDING o UNDER_REVIEW)
     */
    @Command
    @NotifyChange("*")
    public void loadPendingApprovals() {
        try {
            log.debug("Cargando aprobaciones pendientes");

            // Buscar todos los modelos con status IN_REVIEW
            Criterias criterias = new Criterias();
            Criteria statusCriteria = new Criteria(Operation.AND, Evaluation.EQUALS, "modstatus");
            statusCriteria.setValues(new Object[]{"IN_REVIEW"});
            criterias.addCriteria(statusCriteria);

            // Aplicar filtros adicionales
            if (!"ALL".equals(riskLevelFilter)) {
                Criteria riskCriteria = new Criteria(Operation.AND, Evaluation.EQUALS, "modrisklevel");
                riskCriteria.setValues(new Object[]{riskLevelFilter});
                criterias.addCriteria(riskCriteria);
            }

            if (!"ALL".equals(ownerFilter)) {
                Criteria ownerCriteria = new Criteria(Operation.AND, Evaluation.EQUALS, "modcreatedby");
                ownerCriteria.setValues(new Object[]{ownerFilter});
                criterias.addCriteria(ownerCriteria);
            }

            PageParams pageParams = PageParams.builder()
                .maxRows(1000)
                .pageActual(1)
                .rowActual(0)
                .build();

            PageResult<Model> result = modelService.findAll(pageParams, criterias);
            pendingModels = result != null ? result.getContent() : new ArrayList<>();
            totalPending = pendingModels.size();

            log.info("Cargadas {} aprobaciones pendientes", totalPending);

            // Auditar búsqueda
            logActivity("BUSCAR", "MODMODELAPPROVALS", null,
                "Consulta aprobaciones pendientes: " + totalPending + " resultados");

        } catch (GovernanceServiceException e) {
            log.error("Error al cargar aprobaciones pendientes", e);
            Messagebox.show("Error al cargar aprobaciones: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            pendingModels = new ArrayList<>();
        } catch (Exception e) {
            log.error("Error inesperado al cargar aprobaciones pendientes", e);
            Messagebox.show("Error al cargar aprobaciones: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            pendingModels = new ArrayList<>();
        }
    }

    /**
     * Carga métricas de aprobaciones
     */
    private void loadMetrics() {
        try {
            log.debug("Cargando métricas de aprobaciones");

            // Total aprobados
            Criterias approvedCriterias = new Criterias();
            Criteria approvedCriteria = new Criteria(Operation.AND, Evaluation.EQUALS, "modstatus");
            approvedCriteria.setValues(new Object[]{"APPROVED"});
            approvedCriterias.addCriteria(approvedCriteria);

            PageParams countParams = PageParams.builder()
                .maxRows(1)
                .pageActual(1)
                .rowActual(0)
                .build();

            PageResult<Model> approvedResult = modelService.findAll(countParams, approvedCriterias);
            totalApproved = approvedResult != null ? approvedResult.getTotalRows() : 0;

            // Total rechazados
            Criterias rejectedCriterias = new Criterias();
            Criteria rejectedCriteria = new Criteria(Operation.AND, Evaluation.EQUALS, "modstatus");
            rejectedCriteria.setValues(new Object[]{"REJECTED"});
            rejectedCriterias.addCriteria(rejectedCriteria);

            PageResult<Model> rejectedResult = modelService.findAll(countParams, rejectedCriterias);
            totalRejected = rejectedResult != null ? rejectedResult.getTotalRows() : 0;

            log.debug("Métricas cargadas - Aprobados: {}, Rechazados: {}, Pendientes: {}",
                totalApproved, totalRejected, totalPending);

        } catch (Exception e) {
            log.error("Error al cargar métricas", e);
        }
    }

    /**
     * Abre el modal de aprobación para un modelo
     */
    @Command
    @NotifyChange("*")
    public void openApprovalModal(@BindingParam("model") Model model) {
        try {
            log.info("Abriendo modal de aprobación para modelo ID={}", model.getIdxmodel());
            selectedModel = model;
            selectedApproval = findOrCreateApproval(model);
            approvalComments = "";
            showApprovalModal = true;
        } catch (Exception e) {
            log.error("Error al abrir modal de aprobación", e);
            Messagebox.show("Error al abrir modal: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Abre el modal de rechazo para un modelo
     */
    @Command
    @NotifyChange("*")
    public void openRejectionModal(@BindingParam("model") Model model) {
        try {
            log.info("Abriendo modal de rechazo para modelo ID={}", model.getIdxmodel());
            selectedModel = model;
            selectedApproval = findOrCreateApproval(model);
            rejectionReason = "";
            showRejectionModal = true;
        } catch (Exception e) {
            log.error("Error al abrir modal de rechazo", e);
            Messagebox.show("Error al abrir modal: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Cierra el modal de aprobación
     */
    @Command
    @NotifyChange("*")
    public void closeApprovalModal() {
        showApprovalModal = false;
        selectedModel = null;
        selectedApproval = null;
        approvalComments = "";
    }

    /**
     * Cierra el modal de rechazo
     */
    @Command
    @NotifyChange("*")
    public void closeRejectionModal() {
        showRejectionModal = false;
        selectedModel = null;
        selectedApproval = null;
        rejectionReason = "";
    }

    /**
     * Aprueba un modelo
     */
    @Command
    @NotifyChange("*")
    public void approveModel() {
        try {
            if (selectedModel == null || selectedApproval == null) {
                Messagebox.show("No hay modelo seleccionado",
                    "Error", Messagebox.OK, Messagebox.ERROR);
                return;
            }

            log.info("Aprobando modelo ID={} por usuario={}",
                selectedModel.getIdxmodel(), getUser().getUsername());

            // Validar que el usuario tenga rol GOVERNANCE_ADMIN
            // TODO: Implementar validación de roles cuando esté disponible
            // if (!hasRole("GOVERNANCE_ADMIN")) {
            //     Messagebox.show("No tiene permisos para aprobar modelos",
            //         "Error", Messagebox.OK, Messagebox.ERROR);
            //     return;
            // }

            // Actualizar el estado del modelo
            selectedModel.setModstatus("APPROVED");
            selectedModel.setModapprovalstatus("APPROVED");
            selectedModel.setModapprovedby(getUser().getUsername());
            selectedModel.setModapprovedat(new Timestamp(System.currentTimeMillis()));
            selectedModel.setModupdatedby(getUser().getUsername());
            selectedModel.setModupdatedat(new Timestamp(System.currentTimeMillis()));

            modelService.update(selectedModel);

            // Actualizar el registro de aprobación
            selectedApproval.setModapprovalstatus("APPROVED");
            selectedApproval.setModapprovalnotes(approvalComments);
            selectedApproval.setModapproverid(getUser().getUsername());
            selectedApproval.setModapprovername(getUser().getFullname());
            selectedApproval.setModapproverrole("AI_GOVERNANCE_ADMIN");
            selectedApproval.setModapprovedat(new Timestamp(System.currentTimeMillis()));
            selectedApproval.setModupdatedby(getUser().getUsername());
            selectedApproval.setModupdatedat(new Timestamp(System.currentTimeMillis()));

            modelApprovalService.update(selectedApproval);

            log.info("Modelo aprobado exitosamente: ID={}", selectedModel.getIdxmodel());

            // Auditar aprobación
            logActivity("APROBACION", "MODMODELAPPROVALS", selectedApproval.getIdxmodelapproval(),
                "Modelo aprobado: " + selectedModel.getModname());

            // Notificar al owner (simulado con log)
            sendApprovalNotification(selectedModel, true);

            Messagebox.show("Modelo aprobado exitosamente",
                "Éxito", Messagebox.OK, Messagebox.INFORMATION);

            // Cerrar modal y recargar
            closeApprovalModal();
            loadPendingApprovals();
            loadMetrics();

        } catch (GovernanceServiceException e) {
            log.error("Error al aprobar modelo", e);
            Messagebox.show("Error al aprobar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        } catch (Exception e) {
            log.error("Error inesperado al aprobar modelo", e);
            Messagebox.show("Error al aprobar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Rechaza un modelo
     */
    @Command
    @NotifyChange("*")
    public void rejectModel() {
        try {
            if (selectedModel == null || selectedApproval == null) {
                Messagebox.show("No hay modelo seleccionado",
                    "Error", Messagebox.OK, Messagebox.ERROR);
                return;
            }

            // Validar que se proporcione razón de rechazo
            if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
                Messagebox.show("La razón del rechazo es obligatoria",
                    "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }

            log.info("Rechazando modelo ID={} por usuario={}",
                selectedModel.getIdxmodel(), getUser().getUsername());

            // Validar que el usuario tenga rol GOVERNANCE_ADMIN
            // TODO: Implementar validación de roles cuando esté disponible

            // Actualizar el estado del modelo
            selectedModel.setModstatus("REJECTED");
            selectedModel.setModapprovalstatus("REJECTED");
            selectedModel.setModupdatedby(getUser().getUsername());
            selectedModel.setModupdatedat(new Timestamp(System.currentTimeMillis()));

            modelService.update(selectedModel);

            // Actualizar el registro de aprobación
            selectedApproval.setModapprovalstatus("REJECTED");
            selectedApproval.setModrejectionreason(rejectionReason);
            selectedApproval.setModapproverid(getUser().getUsername());
            selectedApproval.setModapprovername(getUser().getFullname());
            selectedApproval.setModapproverrole("AI_GOVERNANCE_ADMIN");
            selectedApproval.setModapprovedat(new Timestamp(System.currentTimeMillis()));
            selectedApproval.setModupdatedby(getUser().getUsername());
            selectedApproval.setModupdatedat(new Timestamp(System.currentTimeMillis()));

            modelApprovalService.update(selectedApproval);

            log.info("Modelo rechazado exitosamente: ID={}", selectedModel.getIdxmodel());

            // Auditar rechazo
            logActivity("RECHAZO", "MODMODELAPPROVALS", selectedApproval.getIdxmodelapproval(),
                "Modelo rechazado: " + selectedModel.getModname() + " - Razón: " + rejectionReason);

            // Notificar al owner (simulado con log)
            sendApprovalNotification(selectedModel, false);

            Messagebox.show("Modelo rechazado",
                "Éxito", Messagebox.OK, Messagebox.INFORMATION);

            // Cerrar modal y recargar
            closeRejectionModal();
            loadPendingApprovals();
            loadMetrics();

        } catch (GovernanceServiceException e) {
            log.error("Error al rechazar modelo", e);
            Messagebox.show("Error al rechazar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        } catch (Exception e) {
            log.error("Error inesperado al rechazar modelo", e);
            Messagebox.show("Error al rechazar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Encuentra o crea un registro de aprobación para un modelo
     */
    private ModelApproval findOrCreateApproval(Model model) throws Exception {
        // Buscar aprobación existente
        Criterias criterias = new Criterias();
        Criteria modelCriteria = new Criteria(Operation.AND, Evaluation.EQUALS, "model.idxmodel");
        modelCriteria.setValues(new Object[]{model.getIdxmodel()});
        criterias.addCriteria(modelCriteria);

        Criteria statusCriteria = new Criteria(Operation.AND, Evaluation.IN, "modapprovalstatus");
        statusCriteria.setValues(new Object[]{"PENDING", "UNDER_REVIEW"});
        criterias.addCriteria(statusCriteria);

        PageParams pageParams = PageParams.builder()
            .maxRows(1)
            .pageActual(1)
            .rowActual(0)
            .build();

        PageResult<ModelApproval> result = modelApprovalService.findAll(pageParams, criterias);

        if (result != null && result.getContent() != null && !result.getContent().isEmpty()) {
            return result.getContent().get(0);
        }

        // Si no existe, crear nueva aprobación
        ModelApproval approval = new ModelApproval();
        approval.setModel(model);
        approval.setModapprovaltype("NEW_MODEL");
        approval.setModapprovalstatus("UNDER_REVIEW");
        approval.setModtargetenvironment("PRODUCTION");
        approval.setModrequestreason("Solicitud de aprobación para modelo: " + model.getModname());
        approval.setModcreatedby(model.getModcreatedby());
        approval.setModcreatedat(new Timestamp(System.currentTimeMillis()));

        modelApprovalService.create(approval);

        return approval;
    }

    /**
     * Envía notificación al owner del modelo (simulado con log)
     */
    private void sendApprovalNotification(Model model, boolean approved) {
        try {
            String status = approved ? "APROBADO" : "RECHAZADO";
            String subject = "Modelo " + status + ": " + model.getModname();
            String message = String.format(
                "El modelo '%s' ha sido %s.\n\nOwner: %s\nFecha: %s",
                model.getModname(),
                status,
                model.getModcreatedby(),
                new Timestamp(System.currentTimeMillis())
            );

            log.info("📧 NOTIFICACIÓN EMAIL (simulada):");
            log.info("   Para: {}", model.getModcreatedby());
            log.info("   Asunto: {}", subject);
            log.info("   Mensaje: {}", message);

            // TODO: Implementar envío real de email cuando esté disponible el servicio

        } catch (Exception e) {
            log.error("Error al enviar notificación", e);
            // No lanzar excepción para no interrumpir el flujo
        }
    }

    /**
     * Navega al detalle de un modelo
     */
    @Command
    public void viewModelDetails(@BindingParam("modelId") Long modelId) {
        try {
            log.info("Navegando a detalle de modelo ID={}", modelId);
            Map<String, Object> params = new HashMap<>();
            params.put("dataParam", modelId);
            params.put("action", Action.LOAD);
            appendPage("plataforma/models/models-detail.zul", page.getFellow(IDDESKTOP), params);
        } catch (Exception e) {
            log.error("Error al navegar a detalle", e);
        }
    }

    /**
     * Aplica los filtros seleccionados
     */
    @Command
    @NotifyChange("*")
    public void applyFilters() {
        log.debug("Aplicando filtros - Risk: {}, Owner: {}", riskLevelFilter, ownerFilter);
        loadPendingApprovals();
    }

    /**
     * Limpia todos los filtros
     */
    @Command
    @NotifyChange("*")
    public void clearFilters() {
        log.debug("Limpiando filtros");
        riskLevelFilter = "ALL";
        ownerFilter = "ALL";
        loadPendingApprovals();
    }

    /**
     * Obtiene el badge CSS class para el risk level
     */
    public String getRiskLevelBadgeClass(String riskLevel) {
        if (riskLevel == null) return "badge badge-secondary";

        switch (riskLevel.toUpperCase()) {
            case "LOW":
                return "badge badge-success";
            case "MEDIUM":
                return "badge badge-warning";
            case "HIGH":
                return "badge badge-danger";
            default:
                return "badge badge-secondary";
        }
    }

    /**
     * Obtiene el badge CSS class para el status
     */
    public String getStatusBadgeClass(String status) {
        if (status == null) return "badge badge-secondary";

        switch (status.toUpperCase()) {
            case "IN_REVIEW":
                return "badge badge-info";
            case "APPROVED":
                return "badge badge-success";
            case "REJECTED":
                return "badge badge-danger";
            case "DRAFT":
                return "badge badge-secondary";
            default:
                return "badge badge-secondary";
        }
    }

    /**
     * Audita las acciones de un usuario
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
     */
    @Destroy
    public void destroy() {
        log.debug("[Destroy] Liberando recursos del ViewModel {}", this.getClass().getSimpleName());

        try {
            if (pendingApprovals != null) {
                pendingApprovals.clear();
                pendingApprovals = null;
            }

            if (pendingModels != null) {
                pendingModels.clear();
                pendingModels = null;
            }

            selectedApproval = null;
            selectedModel = null;
            modelService = null;
            modelApprovalService = null;
            businessService = null; // Mantener para Ssoractividad

            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }
}
