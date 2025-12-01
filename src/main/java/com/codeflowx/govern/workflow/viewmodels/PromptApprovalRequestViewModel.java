package com.codeflowx.govern.workflow.viewmodels;
import com.codeflowx.framework.zkoss.BaseFront;

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
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
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

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para la pantalla de solicitud de aprobación de prompts
 * Integra con Flowable BPMN para iniciar el proceso de aprobación
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class PromptApprovalRequestViewModel extends BaseFront<PromptApprovalRequestViewModel>{

    private static final long serialVersionUID = 1L;

    // ========== Servicios y contexto Spring ==========
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
        // TODO Auto-generated method stub
    }

    // ========== Servicios Flowable ==========
    @WireVariable
    private RuntimeService runtimeService;

    // ========== Datos ==========
    private List<Prompt> availablePrompts = new ArrayList<>();
    private Prompt selectedPrompt;
    private String approvalType;
    private String requestReason;
    private String promptPreview;

    private boolean mockMode = false;

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();

        log.info("🚀 Inicializando PromptApprovalRequestViewModel");

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
            loadAvailablePrompts();

        } catch (Exception e) {
            log.error("Error inicializando PromptApprovalRequestViewModel", e);
            showError("Error al inicializar el formulario: " + e.getMessage());
        }
    }

    /**
     * Carga los prompts disponibles para aprobación
     */
    private void loadAvailablePrompts() {
        try {
            // Cargar prompts usando SQL nativo
            String sql = "SELECT * FROM PRMPROMPTS WHERE PRMAPPROVALSTATUS IS NULL OR PRMAPPROVALSTATUS IN ('DRAFT', 'PENDING') LIMIT 100";
            List<Prompt> prompts = businessService.findByParams(Prompt.class, sql, null);

            if (prompts != null) {
                availablePrompts = prompts;
                log.info("Cargados {} prompts disponibles", availablePrompts.size());
            }

        } catch (Exception e) {
            log.error("Error cargando prompts disponibles", e);
            showError("Error cargando prompts: " + e.getMessage());
        }
    }

    /**
     * Comando ejecutado cuando el usuario selecciona un prompt
     */
    @Command
    @NotifyChange({"selectedPrompt", "promptPreview"})
    public void onPromptSelected() {
        if (selectedPrompt != null) {
            // Generar vista previa del contenido
            String content = selectedPrompt.getPrmcontent();
            if (content != null && content.length() > 500) {
                promptPreview = content.substring(0, 500) + "...";
            } else {
                promptPreview = content;
            }

            log.info("Prompt seleccionado: {} (ID: {})",
                     selectedPrompt.getPrmname(), selectedPrompt.getIdxprompt());
        }
    }

    /**
     * Comando para enviar la solicitud de aprobación
     */
    @Command
    public void submitRequest() {
        try {
            // Validar campos
            if (!validateForm()) {
                return;
            }

            if (mockMode) {
                log.info("🎭 Mock mode: Simulando submitRequest");
                logActivity("MOCK_PROMPT_REQUEST", "PromptApproval", null, "Simulación solicitud aprobación");
                Messagebox.show("✅ [DEMO] Solicitud enviada exitosamente\nID Proceso: mock-prompt-001", "Demo Mode",
                    Messagebox.OK, Messagebox.INFORMATION, event -> closeWindow());
                return;
            }

            // 1. Crear registro de PromptApproval en BBDD
            PromptApproval approval = new PromptApproval();
            approval.setPrompt(selectedPrompt);
            approval.setPrmapprovaltype(approvalType);
            approval.setPrmapprovalstatus("PENDING");
            approval.setPrmrequestreason(requestReason);
            approval.setPrmcreatedby(getUserId());
            approval.setPrmcreatedat(Timestamp.valueOf(LocalDateTime.now()));

            businessService.save(approval);
            log.info("PromptApproval creado con ID: {}", approval.getIdxpromptapproval());

            // 2. Iniciar proceso BPMN Flowable
            Map<String, Object> processVariables = new HashMap<>();
            processVariables.put("promptId", selectedPrompt.getIdxprompt());
            processVariables.put("approvalId", approval.getIdxpromptapproval());
            processVariables.put("promptContent", selectedPrompt.getPrmcontent());
            processVariables.put("approvalType", approvalType);
            processVariables.put("requestReason", requestReason);
            processVariables.put("requesterId", getUserId());
            processVariables.put("requesterName", getUserName());

            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                "prompt-approval-process",
                processVariables
            );

            log.info("Proceso BPMN iniciado: processInstanceId={}", processInstance.getId());

            // 3. Mostrar mensaje de éxito y cerrar ventana
            Messagebox.show(
                "Solicitud de aprobación enviada exitosamente.\n" +
                "ID de Proceso: " + processInstance.getId() + "\n\n" +
                "El sistema ejecutará automáticamente los análisis de seguridad y compliance.",
                "Solicitud Enviada",
                Messagebox.OK,
                Messagebox.INFORMATION,
                event -> closeWindow()
            );

            // 4. Registrar actividad
            logActivity("CREACION", "PRMPROMPTAPPROVALS", approval.getIdxpromptapproval(),
                       "Solicitud de aprobación de prompt: " + selectedPrompt.getPrmname());

        } catch (Exception e) {
            log.error("Error enviando solicitud de aprobación", e);
            Messagebox.show("Error al enviar la solicitud: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    private void loadMockData() {
        log.info("🎭 Mock data loaded for Prompt Approval Request");
    }

    /**
     * Valida el formulario
     */
    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        if (selectedPrompt == null) {
            errors.append("- Debe seleccionar un prompt\n");
        }
        if (approvalType == null || approvalType.trim().isEmpty()) {
            errors.append("- Debe seleccionar el tipo de aprobación\n");
        }
        if (requestReason == null || requestReason.trim().isEmpty()) {
            errors.append("- Debe proporcionar la razón de la solicitud\n");
        } else if (requestReason.length() < 20) {
            errors.append("- La razón de la solicitud debe tener al menos 20 caracteres\n");
        }

        if (errors.length() > 0) {
            Messagebox.show(
                "Por favor complete los siguientes campos:\n" + errors.toString(),
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
     * Registra la actividad del usuario en el sistema de auditoría
     */

    @org.zkoss.bind.annotation.Destroy
    public void destroy() {
        if (availablePrompts != null) { availablePrompts.clear(); availablePrompts = null; }
        selectedPrompt = null;
        runtimeService = null;
        businessService = null;
    }
}
