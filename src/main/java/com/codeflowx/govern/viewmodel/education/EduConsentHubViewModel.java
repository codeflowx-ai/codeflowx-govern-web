package com.codeflowx.govern.viewmodel.education;

import java.util.HashMap;
import java.util.Map;

import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
import org.flowable.engine.RuntimeService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para iniciar el proceso edu_consent_hub (consentimiento educativo)
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class EduConsentHubViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    private static final String PROCESS_KEY = "edu_consent_hub";
    private static final String DEFAULT_SECTOR_CODE = "EDU_SERVICES";

    @WireVariable
    private RuntimeService runtimeService;

    @WireVariable("ctxBean")
    protected Context ctxBean;

    // Campos del formulario
    private String useCaseCode = "EDU_PROCTORING_AI";
    private String[] useCaseOptions = { "EDU_PROCTORING_AI", "EDU_DIGITAL_CONTENT_AI", "EDU_ASSESSMENT_AI" };
    private Integer studentAge;
    private String studentIdentifier;
    private String studentName;
    private String guardianContact;
    private String contentId;
    private String justification;

    @Command
    @NotifyChange({ "studentAge", "studentIdentifier", "studentName", "guardianContact", "contentId", "justification" })
    public void startConsentWorkflow() {
        if (runtimeService == null) {
            Messagebox.show("RuntimeService no disponible", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }

        if (studentAge == null || studentAge < 1) {
            Messagebox.show("Indica la edad del estudiante", "Validación", Messagebox.OK, Messagebox.INFORMATION);
            return;
        }
        if (contentId == null || contentId.isBlank()) {
            Messagebox.show("Indica el identificador del contenido a evaluar", "Validación", Messagebox.OK, Messagebox.INFORMATION);
            return;
        }

        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("sector_code", DEFAULT_SECTOR_CODE);
            variables.put("use_case_code", useCaseCode);
            variables.put("studentAge", studentAge);
            variables.put("studentIdentifier", studentIdentifier);
            variables.put("studentName", studentName);
            variables.put("guardianContact", guardianContact);
            variables.put("contentId", contentId);
            variables.put("justification", justification);
            variables.put("initiatedBy", resolveUser());
            variables.put("initiatedAt", System.currentTimeMillis());

            log.info("Iniciando proceso {} con variables {}", PROCESS_KEY, variables);
            var processInstance = runtimeService.startProcessInstanceByKey(PROCESS_KEY, variables);

            Messagebox.show(
                    "Proceso iniciado correctamente. ID: " + processInstance.getId(),
                    "Consentimiento educativo",
                    Messagebox.OK,
                    Messagebox.INFORMATION);

            resetForm();
        } catch (Exception ex) {
            log.error("Error iniciando el proceso {}", PROCESS_KEY, ex);
            Messagebox.show("Error al iniciar el proceso: " + ex.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({ "studentAge", "studentIdentifier", "studentName", "guardianContact", "contentId", "justification" })
    public void resetForm() {
        studentAge = null;
        studentIdentifier = null;
        studentName = null;
        guardianContact = null;
        contentId = null;
        justification = null;
    }

    private String resolveUser() {
        if (ctxBean != null && ctxBean.getUser() != null && ctxBean.getUser().getUsuname() != null) {
            return ctxBean.getUser().getUsuname();
        }
        return "SYSTEM";
    }
}
