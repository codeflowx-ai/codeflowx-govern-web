package com.codeflowx.govern.workflow.viewmodels;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.govern.entity.prompts.Prompt;
import com.codeflowx.govern.entity.prompts.PromptApproval;
import com.codeflowx.govern.service.prompts.PromptApprovalService;
import com.codeflowx.govern.service.prompts.PromptService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para la pantalla de revisión humana de prompts
 * Integra con Flowable Task Service para completar tareas BPMN
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class PromptHumanReviewViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;

    // ========== Servicios y contexto Spring ==========
    @WireVariable
    private BusinessService businessService;

    @WireVariable
    private PromptService promptService;

    @WireVariable
    private PromptApprovalService promptApprovalService;

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
        // TODO Auto-generated method stub
    }

    // ========== Servicios Flowable ==========
    @WireVariable
    private TaskService taskService;

    private ObjectMapper objectMapper;

    // ========== Datos ==========
    private String taskId;
    private Prompt prompt;
    private PromptApproval approval;
    private Integer safetyScore;
    private Integer complianceScore;
    private Boolean jailbreakDetected;
    private Boolean injectionDetected;
    private Boolean maliciousContentDetected;
    private List<String> safetyRisks = new ArrayList<>();
    private String complianceRecommendation;
    private Boolean approved;
    private String approvalNotes;
    private String rejectionReason;

    private boolean mockMode = false;

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();

        log.info("🚀 Inicializando PromptHumanReviewViewModel");

        Map<String, String[]> params = Executions.getCurrent().getParameterMap();
     // Detectar mock mode desde parámetros URL
        if(System.getenv("MOCK_MODE")!=null) {
        	mockMode = Boolean.parseBoolean(System.getenv("MOCK_MODE").toString());
        }

        if (mockMode) {
            this.mockMode = true;
            loadMockData();
            log.info("🎭 Mock mode activado");
            return;
        }

        try {
            objectMapper = new ObjectMapper();

            // Obtener taskId de los parámetros de la URL
            taskId = Executions.getCurrent().getParameter("taskId");
            if (taskId == null) {
                showError("No se especificó el ID de la tarea");
                return;
            }

            loadTaskData();

        } catch (Exception e) {
            log.error("Error inicializando PromptHumanReviewViewModel", e);
            showError("Error al inicializar el formulario: " + e.getMessage());
        }
    }

    /**
     * Carga los datos de la tarea BPMN y los análisis
     */
    private void loadTaskData() {
        try {
            // 1. Obtener la tarea de Flowable
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null) {
                showError("Tarea no encontrada");
                return;
            }

            // 2. Obtener variables del proceso
            Map<String, Object> processVariables = taskService.getVariables(taskId);

            Long promptId = (Long) processVariables.get("promptId");
            Long approvalId = (Long) processVariables.get("approvalId");

            // 3. Cargar prompt y approval desde BBDD
            if (promptId != null) {
                prompt = promptService.findById(promptId);
            }
            if (approvalId != null) {
                approval = promptApprovalService.findById(approvalId);
            }

            // 4. Cargar resultados de safety check
            safetyScore = (Integer) processVariables.get("safetyScore");
            jailbreakDetected = (Boolean) processVariables.get("jailbreakDetected");
            injectionDetected = (Boolean) processVariables.get("injectionDetected");
            maliciousContentDetected = (Boolean) processVariables.get("maliciousContentDetected");

            // Parsear riesgos de seguridad desde el resultado JSON
            String safetyCheckResult = (String) processVariables.get("safetyCheckResult");
            if (safetyCheckResult != null) {
                try {
                    Map<String, Object> safetyData = objectMapper.readValue(safetyCheckResult, Map.class);
                    List<String> risks = (List<String>) safetyData.get("risks");
                    if (risks != null) {
                        safetyRisks = risks;
                    }
                } catch (Exception e) {
                    log.warn("Error parseando safety check result: {}", e.getMessage());
                }
            }

            // 5. Cargar resultados de compliance check
            complianceScore = (Integer) processVariables.get("complianceScore");

            String complianceCheckResult = (String) processVariables.get("complianceCheckResult");
            if (complianceCheckResult != null) {
                try {
                    Map<String, Object> complianceData = objectMapper.readValue(complianceCheckResult, Map.class);
                    complianceRecommendation = (String) complianceData.get("recommendation");
                } catch (Exception e) {
                    log.warn("Error parseando compliance check result: {}", e.getMessage());
                }
            }

            log.info("Datos de tarea cargados - Task ID: {}, Prompt: {}, Safety Score: {}, Compliance Score: {}",
                     taskId, prompt != null ? prompt.getPrmname() : "N/A", safetyScore, complianceScore);

        } catch (GovernanceServiceException e) {
            log.error("Error cargando datos de la tarea", e);
            showError("Error cargando datos: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error cargando datos de la tarea", e);
            showError("Error cargando datos: " + e.getMessage());
        }
    }

    /**
     * Comando para confirmar la decisión
     */
    @Command
    public void confirmDecision() {
        try {
            // Validar decisión
            if (!validateDecision()) {
                return;
            }

            if (mockMode) {
                log.info("🎭 Mock mode: Simulando confirmDecision - approved={}", approved);
                String accion = Boolean.TRUE.equals(approved) ? "MOCK_APPROVE_PROMPT" : "MOCK_REJECT_PROMPT";
                logActivity(accion, "PromptReview", null, "Simulación: " + (Boolean.TRUE.equals(approved) ? "APROBADO" : "RECHAZADO"));
                String message = Boolean.TRUE.equals(approved) ? "✅ [DEMO] Prompt aprobado" : "✅ [DEMO] Prompt rechazado";
                Messagebox.show(message, "Demo Mode", Messagebox.OK, Messagebox.INFORMATION, event -> closeWindow());
                return;
            }

            // 1. Actualizar PromptApproval en BBDD
            if (approval != null) {
                if (Boolean.TRUE.equals(approved)) {
                    approval.setPrmapprovalstatus("APPROVED");
                    approval.setPrmapprovalnotes(approvalNotes);
                    approval.setPrmapproverid(getUserId());
                    approval.setPrmapprovername(getUserName());
                    approval.setPrmapprovedat(Timestamp.valueOf(LocalDateTime.now()));
                } else {
                    approval.setPrmapprovalstatus("REJECTED");
                    approval.setPrmrejectionreason(rejectionReason);
                    approval.setPrmapprovalnotes(approvalNotes);
                }
                approval.setPrmupdatedat(Timestamp.valueOf(LocalDateTime.now()));
                promptApprovalService.update(approval);
            }

            // 2. Actualizar Prompt en BBDD
            if (prompt != null) {
                if (Boolean.TRUE.equals(approved)) {
                    prompt.setPrmapprovalstatus("APPROVED");
                    prompt.setPrmapprovedby(getUserId());
                    prompt.setPrmapprovedat(Timestamp.valueOf(LocalDateTime.now()));
                } else {
                    prompt.setPrmapprovalstatus("REJECTED");
                }
                prompt.setPrmupdatedat(Timestamp.valueOf(LocalDateTime.now()));
                promptService.update(prompt);
            }

            // 3. Completar tarea en Flowable con las variables de decisión
            Map<String, Object> taskVariables = new HashMap<>();
            taskVariables.put("approved", approved);
            taskVariables.put("approvalNotes", approvalNotes);
            taskVariables.put("rejectionReason", rejectionReason);
            taskVariables.put("reviewerId", getUserId());
            taskVariables.put("reviewerName", getUserName());
            taskVariables.put("reviewedAt", System.currentTimeMillis());

            taskService.complete(taskId, taskVariables);
            log.info("Tarea completada exitosamente - Task ID: {}, Approved: {}", taskId, approved);

            // 4. Mostrar mensaje de éxito y cerrar ventana
            String message = Boolean.TRUE.equals(approved)
                ? "Prompt aprobado exitosamente."
                : "Prompt rechazado exitosamente.";

            Messagebox.show(
                message,
                "Decisión Confirmada",
                Messagebox.OK,
                Messagebox.INFORMATION,
                event -> closeWindow()
            );

            // 5. Registrar actividad
            String accion = Boolean.TRUE.equals(approved) ? "APROBACION" : "RECHAZO";
            logActivity(accion, "PRMPROMPTAPPROVALS", approval != null ? approval.getIdxpromptapproval() : null,
                       "Revisión de prompt: " + (prompt != null ? prompt.getPrmname() : "N/A") + " - " +
                       (Boolean.TRUE.equals(approved) ? "APROBADO" : "RECHAZADO"));

        } catch (GovernanceServiceException e) {
            log.error("Error confirmando decisión", e);
            showError("Error al confirmar la decisión: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error confirmando decisión", e);
            showError("Error al confirmar la decisión: " + e.getMessage());
        }
    }

    private void loadMockData() {
        this.taskId = "mock-prompt-review-001";
        this.safetyScore = 85;
        this.complianceScore = 90;
        this.jailbreakDetected = false;
        this.injectionDetected = false;
        this.maliciousContentDetected = false;
        log.info("🎭 Mock data loaded for Prompt Human Review");
    }

    /**
     * Valida la decisión antes de confirmar
     */
    private boolean validateDecision() {
        if (approved == null) {
            Messagebox.show(
                "Debe seleccionar APROBAR o RECHAZAR",
                "Validación",
                Messagebox.OK,
                Messagebox.EXCLAMATION
            );
            return false;
        }

        if (Boolean.FALSE.equals(approved) &&
            (rejectionReason == null || rejectionReason.trim().isEmpty())) {
            Messagebox.show(
                "Debe proporcionar una razón del rechazo",
                "Validación",
                Messagebox.OK,
                Messagebox.EXCLAMATION
            );
            return false;
        }

        return true;
    }

    /**
     * Comando para cancelar y cerrar la ventana
     */
    @Command
    public void cancel() {
        closeWindow();
    }

    /**
     * Cierra la ventana actual
     */
    private void closeWindow() {
        getSelf().detach();
    }

    /**
     * Muestra un mensaje de error
     */
    public void showError(String message) {
        Messagebox.show(message, "Error", Messagebox.OK, Messagebox.ERROR);
    }

    /**
     * Obtiene el ID del usuario actual
     */
    private String getUserId() {
        try {
            return String.valueOf(super.getUser().getId());
        } catch (Exception e) {
            return "SYSTEM";
        }
    }

    /**
     * Obtiene el nombre del usuario actual
     */
    private String getUserName() {
        try {
            return getUser().getUsername();
        } catch (Exception e) {
            return "System User";
        }
    }

    /**
     * Registra la actividad del usuario en la base de datos
     */
    private void logActivity(String action, String model, Long pk, String mensaje) {
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
            log.error("Error registrando actividad del usuario", e);
        }
    }
}
