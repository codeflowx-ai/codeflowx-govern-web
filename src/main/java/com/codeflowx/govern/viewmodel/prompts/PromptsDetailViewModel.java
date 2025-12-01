package com.codeflowx.govern.viewmodel.prompts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.enartframework.suinsit.Context;
import javax.sql.DataSource;

import org.enartframework.web.exception.UiException;
import com.codeflowx.framework.zkoss.BaseFront;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.enartframework.nocode.dao.IEntityLocal;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.entity.prompts.Prompt;
import com.codeflowx.govern.service.prompts.PromptService;
import com.codeflowx.govern.entity.prompts.PromptVersion;
import com.codeflowx.govern.entity.prompts.PromptValidation;
import com.codeflowx.govern.entity.functions.prompts.CalculatePromptEffectiveness;
import com.codeflowx.govern.entity.procedures.prompts.VersionPrompt;
import com.codeflowx.govern.service.prompts.PromptValidationService;
import com.codeflowx.govern.service.prompts.PromptVersionService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;
import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.zkoss.bind.annotation.Destroy;

/**
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de prompts
 *
 * Responsabilidades:
 * - Creación de nuevos prompts
 * - Edición de prompts existentes
 * - Operaciones de negocio: validatePrompt(), calculateEffectiveness()
 * - Información descendente: versiones, validaciones
 * - Estadísticas y contadores específicos del prompt
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class PromptsDetailViewModel extends BaseFront<PromptsDetailViewModel> {

    private static final long serialVersionUID = 1L;

    // ========== Servicios y contexto Spring ==========
    @WireVariable
    private PromptService promptService;
    @WireVariable
    private PromptVersionService promptVersionService;
    @WireVariable
    private PromptValidationService promptValidationService;
    @WireVariable
    private BusinessService businessService; // Mantener para procedimientos almacenados (callProcedure)

    @Autowired
    protected IEntityLocal dao;

    @WireVariable
    public Environment environment;

    @WireVariable("context")
    protected GenericApplicationContext contexto;

    @WireVariable("ctxBean")
    protected Context ctxBean;


    protected void initDao() {
        // Ya no es necesario inicializar BusinessService manualmente
        // El Service se inyecta automáticamente mediante @WireVariable
    }

    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }

    // ========== Modo de operación ==========
    private String mode; // "create" o "edit"
    private Long promptId;
    private boolean editing = false;
    private String pageTitle = "Detalle del Prompt";

    // ========== Datos del prompt ==========
    private Prompt currentPrompt;

    // ========== Información descendente ==========
    private List<PromptVersion> promptVersions = new ArrayList<>();
    private List<PromptValidation> promptValidations = new ArrayList<>();

    // ========== Estadísticas específicas del prompt ==========
    private Long totalVersions = 0L;
    private Long totalValidations = 0L;
    private BigDecimal effectivenessScore = BigDecimal.ZERO;
    private Long successfulValidations = 0L;
    private Timestamp lastValidationDate;

    // ========== Inicialización ==========

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();

        // Obtener parámetros de navegación
        mode = (String) Executions.getCurrent().getParameter("mode");
        String promptIdStr = Executions.getCurrent().getParameter("promptId");

        if (promptIdStr != null) {
            promptId = Long.parseLong(promptIdStr);
        }

        log.info("Inicializando PromptsDetailViewModel - mode: {}, promptId: {}", mode, promptId);

        if ("create".equals(mode)) {
            initNewPrompt();
        } else if ("edit".equals(mode) && promptId != null) {
            loadPrompt(promptId);
        } else {
            log.error("Modo inválido o falta promptId");
            Executions.sendRedirect("/prompts/prompts-overview.zul");
        }
    }

    /**
     * Inicializa un nuevo prompt con valores por defecto
     * @throws UiException
     */
    private void initNewPrompt() throws UiException {
        log.debug("Inicializando nuevo prompt");
        currentPrompt = new Prompt();
        currentPrompt.setPrmcreatedat(new Timestamp(System.currentTimeMillis()));
        currentPrompt.setPrmupdatedat(new Timestamp(System.currentTimeMillis()));
        currentPrompt.setPrmcreatedby(getUser().getUsername()); // Usuario actual
        currentPrompt.setPrmstatus("DRAFT"); // LIST_STRING - valor simple
        currentPrompt.setPrmapprovalstatus("PENDING"); // LIST_STRING - valor simple
        currentPrompt.setPrmversion("1.0"); // VARCHAR - versión inicial

        editing = false;
        pageTitle = "Crear Nuevo Prompt";
    }

    /**
     * Carga prompt existente desde BD
     */
    private void loadPrompt(Long id) {
        try {
            log.debug("Cargando prompt ID={}", id);

            currentPrompt = promptService.findById(id);

            if (currentPrompt == null) {
                log.error("Prompt no encontrado: ID={}", id);
                Messagebox.show("Prompt no encontrado", "Error",
                    Messagebox.OK, Messagebox.ERROR);
                Executions.sendRedirect("/prompts/prompts-overview.zul");
                return;
            }

            log.info("Prompt cargado: {}", currentPrompt.getPrmname());

            editing = true;
            pageTitle = "Editar Prompt: " + currentPrompt.getPrmname();

            // Cargar información descendente
            loadPromptVersions();
            loadPromptValidations();
            loadPromptStatistics();

        } catch (GovernanceServiceException e) {
            log.error("Error al cargar prompt ID={}", id, e);
            Messagebox.show("Error al cargar prompt: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Executions.sendRedirect("/prompts/prompts-overview.zul");
        } catch (Exception e) {
            log.error("Error inesperado al cargar prompt ID={}", id, e);
            Messagebox.show("Error al cargar prompt: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Executions.sendRedirect("/prompts/prompts-overview.zul");
        }
    }

    /**
     * Carga estadísticas específicas del prompt
     */
    private void loadPromptStatistics() {
        try {
            log.debug("Cargando estadísticas del prompt ID={}", currentPrompt.getIdxprompt());

            totalVersions = (long) promptVersions.size();
            totalValidations = (long) promptValidations.size();

            // Calcular validaciones exitosas
            successfulValidations = promptValidations.stream()
                .filter(v -> "SUCCESS".equals(v.getPrmstatus()))
                .count();

            // Calcular effectiveness score
            if (totalValidations > 0) {
                effectivenessScore = BigDecimal.valueOf(successfulValidations)
                    .divide(BigDecimal.valueOf(totalValidations), 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            } else {
                effectivenessScore = BigDecimal.ZERO;
            }

            log.info("Estadísticas cargadas - Versiones: {}, Validaciones: {}, Effectiveness: {}%",
                totalVersions, totalValidations, effectivenessScore);

        } catch (Exception e) {
            log.error("Error al cargar estadísticas del prompt", e);
        }
    }

    // ========== Información descendente ==========

    @Command
    @NotifyChange({"promptVersions", "totalVersions"})
    public void loadPromptVersions() {
        try {
            log.debug("Cargando versiones del prompt ID={}", currentPrompt.getIdxprompt());

            PageParams params = PageParams.builder()
                .maxRows(50)
                .pageActual(1)
                .rowActual(0)
                .build();

            Criterias criterias = new Criterias();
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "prompt");
            criteria.setValues(new Object[]{currentPrompt.getIdxprompt()});
            criterias.addCriteria(criteria);

            PageResult<PromptVersion> result = promptVersionService.findAll(params, criterias);

            if (result != null && result.getContent() != null) {
                promptVersions = result.getContent();
                totalVersions = (long) promptVersions.size();
                log.info("Cargadas {} versiones del prompt", totalVersions);
            } else {
                promptVersions = new ArrayList<>();
                totalVersions = 0L;
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar versiones del prompt", e);
            promptVersions = new ArrayList<>();
            totalVersions = 0L;
        } catch (Exception e) {
            log.error("Error inesperado al cargar versiones del prompt", e);
            promptVersions = new ArrayList<>();
            totalVersions = 0L;
        }
    }

    @Command
    @NotifyChange({"promptValidations", "totalValidations"})
    public void loadPromptValidations() {
        try {
            log.debug("Cargando validaciones del prompt ID={}", currentPrompt.getIdxprompt());

            PageParams params = PageParams.builder()
                .maxRows(50)
                .pageActual(1)
                .rowActual(0)
                .build();

            Criterias criterias = new Criterias();
            Criteria criteria = new Criteria(Operation.AND, Evaluation.EQUALS, "prompt");
            criteria.setValues(new Object[]{currentPrompt.getIdxprompt()});
            criterias.addCriteria(criteria);

            PageResult<PromptValidation> result = promptValidationService.findAll(params, criterias);

            if (result != null && result.getContent() != null) {
                promptValidations = result.getContent();
                totalValidations = (long) promptValidations.size();
                log.info("Cargadas {} validaciones del prompt", totalValidations);
            } else {
                promptValidations = new ArrayList<>();
                totalValidations = 0L;
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar validaciones del prompt", e);
            promptValidations = new ArrayList<>();
            totalValidations = 0L;
        } catch (Exception e) {
            log.error("Error inesperado al cargar validaciones del prompt", e);
            promptValidations = new ArrayList<>();
            totalValidations = 0L;
        }
    }

    // ========== Comandos CRUD ==========

    @Command
    @NotifyChange("*")
    public void savePrompt() {
        try {
            log.info("Guardando prompt: {}", currentPrompt.getPrmname());

            // Validaciones de negocio
            if (currentPrompt.getPrmname() == null || currentPrompt.getPrmname().trim().isEmpty()) {
                Messagebox.show("El nombre del prompt es requerido",
                    "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }

            if (currentPrompt.getPrmcontent() == null || currentPrompt.getPrmcontent().trim().isEmpty()) {
                Messagebox.show("El contenido del prompt es requerido",
                    "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }

            if (currentPrompt.getIdxprompt() == null) {
                currentPrompt = promptService.create(currentPrompt);
                log.info("Prompt creado exitosamente: ID={}, nombre={}",
                    currentPrompt.getIdxprompt(), currentPrompt.getPrmname());
                Messagebox.show("Prompt creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentPrompt.setPrmupdatedat(new Timestamp(System.currentTimeMillis()));
                currentPrompt = promptService.update(currentPrompt);
                log.info("Prompt actualizado exitosamente: ID={}, nombre={}",
                    currentPrompt.getIdxprompt(), currentPrompt.getPrmname());
                Messagebox.show("Prompt actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }

            Executions.sendRedirect("/prompts/prompts-overview.zul");

        } catch (GovernanceServiceException e) {
            log.error("Error al guardar prompt", e);
            Messagebox.show("Error al guardar prompt: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        } catch (Exception e) {
            log.error("Error inesperado al guardar prompt", e);
            Messagebox.show("Error al guardar prompt: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    public void cancelEdit() {
        log.debug("Cancelando edición/creación de prompt, volviendo a overview");
        Executions.sendRedirect("/prompts/prompts-overview.zul");
    }

    // ========== Operaciones especiales (funciones/procedimientos) ==========

    @Command
    @NotifyChange({"currentPrompt", "effectivenessScore"})
    public void validatePrompt() {
        if (currentPrompt == null || currentPrompt.getIdxprompt() == null) {
            Messagebox.show("Debe guardar el prompt antes de validarlo",
                "Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        log.info("Validando prompt ID={}", currentPrompt.getIdxprompt());
        try {
            // TODO: Implementar validación usando stored procedure
            Messagebox.show("Validación de prompt completada exitosamente\n\nPróximamente se mostrarán métricas detalladas",
                "Validación Completada", Messagebox.OK, Messagebox.INFORMATION);
            loadPromptValidations();
            loadPromptStatistics();
        } catch (Exception e) {
            log.error("Error al validar prompt", e);
            Messagebox.show("Error al validar prompt: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"promptVersions", "totalVersions"})
    public void createNewVersion() {
        if (currentPrompt == null || currentPrompt.getIdxprompt() == null) {
            Messagebox.show("Debe guardar el prompt antes de crear una versión",
                "Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        log.info("Creando nueva versión para prompt ID={}", currentPrompt.getIdxprompt());
        try {
            VersionPrompt procedure = new VersionPrompt();
            procedure.setPInputParam(currentPrompt.getIdxprompt()); // Usar parámetro correcto del JPA

            procedure = businessService.callProcedure(procedure);

            if (procedure.getOSuccess() != null && procedure.getOSuccess()) {
                Messagebox.show("Nueva versión creada exitosamente\nVersion ID: " + procedure.getOResult(),
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                loadPromptVersions();
                loadPromptStatistics();
            } else {
                Messagebox.show("No se pudo crear la nueva versión",
                    "Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
            }
        } catch (Exception e) {
            log.error("Error al crear nueva versión", e);
            Messagebox.show("Error al crear nueva versión: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
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
            // Limpiar prompt actual
            currentPrompt = null;

            // Limpiar listas descendentes
            if (promptVersions != null) {
                promptVersions.clear();
                promptVersions = null;
            }
            if (promptValidations != null) {
                promptValidations.clear();
                promptValidations = null;
            }

            // Limpiar BusinessService
            promptService = null;
            promptVersionService = null;
            promptValidationService = null;

            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }

}
