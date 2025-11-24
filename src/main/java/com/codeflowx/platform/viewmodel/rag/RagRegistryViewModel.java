package com.codeflowx.platform.viewmodel.rag;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.entity.rag.RagSystem;
import com.codeflowx.govern.service.rag.RagSystemService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import codeflowx.nocode.persist.PageParams;
import codeflowx.nocode.persist.PageResult;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Init(superclass = true)
@VariableResolver(DelegatingVariableResolver.class)
public class RagRegistryViewModel extends BaseFront<RagRegistryViewModel> {
    private static final long serialVersionUID = 1L;

    @org.zkoss.zk.ui.select.annotation.WireVariable
    private RagSystemService ragSystemService;

    @Override
    public void setBeans(Object bean) {}

    // Paginación
    private PageParams pageParams;
    private PageResult<RagSystem> pageResult;

    // Vista
    private String viewMode = "grid"; // grid o list

    // Filtros
    private String searchText = "";
    private String filterStatus = "";
    private String filterType = "";
    private String filterPerformance = "";

    // Datos
    private List<RagSystem> registryList = new ArrayList<>();

    // Modal de creación/edición
    private boolean showModal = false;
    private boolean isEditMode = false;
    private Long currentSystemId = null;

    // Campos del formulario
    private String systemName;
    private String systemDescription;
    private String systemVersion;
    private String systemType = "KNOWLEDGE_BASE";
    private String systemStatus = "ACTIVE";
    private String systemRiskLevel = "LOW";
    private String systemTags;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);

        pageParams = PageParams.builder()
            .maxRows(50)
            .pageActual(1)
            .rowActual(0)
            .build();

        loadRegistry();
    }

    @Command
    @NotifyChange("*")
    public void loadRegistry() {
        try {
            Criterias criterias = buildCriterias();

            pageResult = ragSystemService.findAll(
                pageParams,
                criterias
            );

            if (pageResult != null && pageResult.getContent() != null) {
                registryList = pageResult.getContent();

                // Auditar búsqueda
                logActivity("BUSCAR", "RAGSYSTEMS", null,
                    "Búsqueda registry: " + registryList.size() + " sistemas");

                log.info("Cargados {} sistemas en registry", registryList.size());
            } else {
                registryList = new ArrayList<>();
            }
        } catch (GovernanceServiceException e) {
            log.error("Error al cargar registry", e);
            Messagebox.show("Error al cargar sistemas: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();

        if (searchText != null && !searchText.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.LIKE, "ragsystemname", searchText));
        }

        if (filterStatus != null && !filterStatus.trim().isEmpty()) {
            String status = filterStatus.toUpperCase();
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "ragstatus", status));
        }

        if (filterType != null && !filterType.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "ragtype", filterType));
        }

        // Filtro de rendimiento (basado en ragoverallscore)
        if (filterPerformance != null && !filterPerformance.trim().isEmpty()) {
            switch (filterPerformance) {
                case "excellent":
                    criterias.addCriteria(new Criteria(Operation.AND, Evaluation.GREATER_THAN, "ragoverallscore", 90));
                    break;
                case "good":
                    criterias.addCriteria(new Criteria(Operation.AND, Evaluation.GREATER_THAN, "ragoverallscore", 70));
                    criterias.addCriteria(new Criteria(Operation.AND, Evaluation.LESS_THAN_EQUALS, "ragoverallscore", 90));
                    break;
                case "poor":
                    criterias.addCriteria(new Criteria(Operation.AND, Evaluation.LESS_THAN, "ragoverallscore", 70));
                    break;
            }
        }

        return criterias;
    }

    @Command
    @NotifyChange("*")
    public void searchRegistry() {
        pageParams.setPageActual(1);
        loadRegistry();
    }

    @Command
    @NotifyChange("*")
    public void applyFilters() {
        pageParams.setPageActual(1);
        loadRegistry();
    }

    @Command
    @NotifyChange("*")
    public void clearFilters() {
        searchText = "";
        filterStatus = "";
        filterType = "";
        filterPerformance = "";
        pageParams.setPageActual(1);
        loadRegistry();
    }

    @Command
    @NotifyChange("viewMode")
    public void toggleView(@BindingParam("view") String view) {
        this.viewMode = view;
    }

    @Command
    @NotifyChange("*")
    public void showCreateModal() {
        isEditMode = false;
        currentSystemId = null;
        clearForm();
        showModal = true;
    }

    @Command
    @NotifyChange("*")
    public void editSystem(@BindingParam("system") RagSystem system) {
        isEditMode = true;
        currentSystemId = system.getIdxragsystem();

        // Cargar datos al formulario
        systemName = system.getRagsystemname();
        systemDescription = system.getRagdescription();
        systemVersion = system.getRagversion();
        systemType = system.getRagtype();
        systemStatus = system.getRagstatus();
        systemRiskLevel = system.getRagrisklevel();
        systemTags = system.getRagtags();

        showModal = true;
    }

    @Command
    @NotifyChange("*")
    public void saveSystem() {
        try {
            // Validaciones
            if (systemName == null || systemName.trim().isEmpty()) {
                Messagebox.show("El nombre es requerido", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }

            if (systemVersion == null || systemVersion.trim().isEmpty()) {
                Messagebox.show("La versión es requerida", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }

            RagSystem system;

            if (isEditMode && currentSystemId != null) {
                // Editar
                system = ragSystemService.findById(currentSystemId);
                if (system == null) {
                    Messagebox.show("Sistema no encontrado", "Error", Messagebox.OK, Messagebox.ERROR);
                    return;
                }
                system.setRagupdatedat(new Timestamp(System.currentTimeMillis()));
            } else {
                // Crear
                system = new RagSystem();
                system.setRagcreatedat(new Timestamp(System.currentTimeMillis()));
                system.setRagcreatedby("System"); // TODO: Obtener usuario actual

                // Inicializar valores por defecto
                system.setRagcompliance("PENDING_REVIEW");
                system.setRaggovernancestatus("PENDING");
                system.setRagdatasources(0);
                system.setRagdocuments(0);
                system.setRagqueries(0L);
            }

            // Actualizar campos
            system.setRagsystemname(systemName);
            system.setRagdescription(systemDescription);
            system.setRagversion(systemVersion);
            system.setRagtype(systemType);
            system.setRagstatus(systemStatus);
            system.setRagrisklevel(systemRiskLevel);
            system.setRagtags(systemTags);

            if (isEditMode) {
                ragSystemService.update(system);
            } else {
                ragSystemService.create(system);
            }

            // Auditar
            logActivity(isEditMode ? "EDITAR" : "CREAR", "RAGSYSTEMS", system.getIdxragsystem(),
                (isEditMode ? "Editado" : "Creado") + " sistema: " + system.getRagsystemname());

            showModal = false;
            loadRegistry();

            Messagebox.show("Sistema guardado exitosamente",
                "Éxito", Messagebox.OK, Messagebox.INFORMATION);

        } catch (GovernanceServiceException e) {
            log.error("Error al guardar sistema", e);
            Messagebox.show("Error al guardar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange("*")
    public void closeModal() {
        showModal = false;
        clearForm();
    }

    @Command
    public void viewSystem(@BindingParam("system") RagSystem system) {
        log.info("Ver sistema: {}", system.getRagsystemname());
        // TODO: Navegar a vista detallada
    }

    @Command
    @NotifyChange("*")
    public void deleteSystem(@BindingParam("system") RagSystem system) {
        Messagebox.show("¿Está seguro de eliminar el sistema: " + system.getRagsystemname() + "?",
            "Confirmar", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    try {
                        ragSystemService.deleteById(system.getIdxragsystem());

                        // Auditar eliminación
                        logActivity("ELIMINAR", "RAGSYSTEMS", system.getIdxragsystem(),
                            "Sistema eliminado: " + system.getRagsystemname());

                        loadRegistry();
                        Messagebox.show("Sistema eliminado exitosamente",
                            "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                    } catch (Exception e) {
                        log.error("Error al eliminar sistema", e);
                        Messagebox.show("Error: " + e.getMessage(),
                            "Error", Messagebox.OK, Messagebox.ERROR);
                    }
                }
            });
    }

    @Command
    public void downloadConfig(@BindingParam("system") RagSystem system) {
        log.info("Descargar configuración: {}", system.getRagsystemname());
        // TODO: Implementar descarga de configuración JSON
        Messagebox.show("Funcionalidad de descarga en desarrollo",
            "Info", Messagebox.OK, Messagebox.INFORMATION);
    }

    private void clearForm() {
        systemName = null;
        systemDescription = null;
        systemVersion = null;
        systemType = "KNOWLEDGE_BASE";
        systemStatus = "ACTIVE";
        systemRiskLevel = "LOW";
        systemTags = null;
    }

    public List<String> getTags(String tagsString) {
        if (tagsString == null || tagsString.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(tagsString.split(","));
    }

    public String getStatusColor(String status) {
        if (status == null) return "badge bg-secondary";
        switch (status.toUpperCase()) {
            case "ACTIVE": return "badge bg-success";
            case "INACTIVE": return "badge bg-secondary";
            case "DRAFT": return "badge bg-warning";
            case "ARCHIVED": return "badge bg-dark";
            default: return "badge bg-secondary";
        }
    }

    public String formatDate(Timestamp timestamp) {
        if (timestamp == null) return "-";
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(timestamp);
    }

    @Destroy
    public void destroy() {
        if (registryList != null) {
            registryList.clear();
            registryList = null;
        }
        pageResult = null;
        pageParams = null;
        businessService = null;
    }
}
