package com.codeflowx.platform.viewmodel.rag;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.entity.rag.RagDataSource;
import com.codeflowx.govern.entity.rag.RagSystem;
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
public class RagDataSourceViewModel extends BaseFront<RagDataSourceViewModel> {
    private static final long serialVersionUID = 1L;
    
    @Override
    public void setBeans(Object bean) {}
    
    // Paginación
    private PageParams pageParams;
    private PageResult<RagDataSource> pageResult;
    
    // Filtros
    private String searchText = "";
    private String filterRagSystem = "";
    private String filterType = "";
    private String filterStatus = "";
    
    // Datos
    private List<RagDataSource> sourcesList = new ArrayList<>();
    private List<RagSystem> ragSystemsList = new ArrayList<>();
    
    // Métricas
    private int totalSources = 0;
    private int indexedSources = 0;
    private int indexingSources = 0;
    private long totalDocuments = 0L;
    
    // Modal de creación/edición
    private boolean showModal = false;
    private boolean isEditMode = false;
    private Long currentSourceId = null;
    
    // Campos del formulario
    private Long sourceRagSystemId;
    private String sourceName;
    private String sourceType = "FILE_SYSTEM";
    private String sourceUrl;
    private String sourceIndexStatus = "PENDING";
    private String sourceDataClass = "INTERNAL";
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        
        pageParams = PageParams.builder()
            .maxRows(50)
            .pageActual(1)
            .rowActual(0)
            .build();
        
        loadRagSystems();
        loadSources();
        loadMetrics();
    }
    
    @Command
    @NotifyChange("*")
    public void loadRagSystems() {
        try {
            PageParams params = PageParams.builder().maxRows(100).pageActual(1).build();
            PageResult<RagSystem> result = businessService.findAllEntity(RagSystem.class, params, new Criterias());
            
            if (result != null && result.getContent() != null) {
                ragSystemsList = result.getContent();
            }
        } catch (Exception e) {
            log.error("Error al cargar sistemas RAG", e);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void loadSources() {
        try {
            Criterias criterias = buildCriterias();
            
            pageResult = businessService.findAllEntity(
                RagDataSource.class,
                pageParams,
                criterias
            );
            
            if (pageResult != null && pageResult.getContent() != null) {
                sourcesList = pageResult.getContent();
                totalSources = pageResult.getTotalRows();
                
                // Auditar búsqueda
                logActivity("BUSCAR", "RAGDATASOURCES", null, 
                    "Búsqueda fuentes: " + sourcesList.size() + " resultados");
                
                log.info("Cargadas {} fuentes de datos RAG", sourcesList.size());
            } else {
                sourcesList = new ArrayList<>();
                totalSources = 0;
            }
        } catch (Exception e) {
            log.error("Error al cargar fuentes", e);
            Messagebox.show("Error al cargar fuentes: " + e.getMessage(), 
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange("*")
    public void loadMetrics() {
        try {
            indexedSources = (int) sourcesList.stream()
                .filter(s -> "COMPLETED".equals(s.getRagdsindexstatus()))
                .count();
                
            indexingSources = (int) sourcesList.stream()
                .filter(s -> "IN_PROGRESS".equals(s.getRagdsindexstatus()))
                .count();
                
            totalDocuments = sourcesList.stream()
                .mapToLong(s -> s.getRagdsdocumentcount() != null ? s.getRagdsdocumentcount() : 0)
                .sum();
        } catch (Exception e) {
            log.error("Error al cargar métricas", e);
        }
    }
    
    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();
        
        if (searchText != null && !searchText.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.LIKE, "ragdssourcename", searchText));
        }
        
        if (filterRagSystem != null && !filterRagSystem.trim().isEmpty()) {
            try {
                Long systemId = Long.parseLong(filterRagSystem);
                criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "ragSystem.idxragsystem", systemId));
            } catch (NumberFormatException e) {
                log.warn("Invalid RAG system ID filter: {}", filterRagSystem);
            }
        }
        
        if (filterType != null && !filterType.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "ragdssourcetype", filterType));
        }
        
        if (filterStatus != null && !filterStatus.trim().isEmpty()) {
            criterias.addCriteria(new Criteria(Operation.AND, Evaluation.EQUALS, "ragdsindexstatus", filterStatus));
        }
        
        return criterias;
    }
    
    @Command
    @NotifyChange("*")
    public void searchSources() {
        pageParams.setPageActual(1);
        loadSources();
        loadMetrics();
    }
    
    @Command
    @NotifyChange("*")
    public void applyFilters() {
        pageParams.setPageActual(1);
        loadSources();
        loadMetrics();
    }
    
    @Command
    @NotifyChange("*")
    public void refreshSources() {
        loadSources();
        loadMetrics();
    }
    
    @Command
    @NotifyChange("*")
    public void showCreateModal() {
        isEditMode = false;
        currentSourceId = null;
        clearForm();
        showModal = true;
    }
    
    @Command
    @NotifyChange("*")
    public void editSource(@BindingParam("source") RagDataSource source) {
        isEditMode = true;
        currentSourceId = source.getIdxragdatasource();
        
        // Cargar datos al formulario
        sourceRagSystemId = source.getRagSystem() != null ? source.getRagSystem().getIdxragsystem() : null;
        sourceName = source.getRagdssourcename();
        sourceType = source.getRagdssourcetype();
        sourceUrl = source.getRagdssourceurl();
        sourceIndexStatus = source.getRagdsindexstatus();
        sourceDataClass = source.getRagdsdataclass();
        
        showModal = true;
    }
    
    @Command
    @NotifyChange("*")
    public void saveSource() {
        try {
            // Validaciones
            if (sourceRagSystemId == null) {
                Messagebox.show("Debe seleccionar un sistema RAG", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            if (sourceName == null || sourceName.trim().isEmpty()) {
                Messagebox.show("El nombre es requerido", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            RagDataSource source;
            
            if (isEditMode && currentSourceId != null) {
                // Editar
                source = businessService.findEntity(RagDataSource.class, currentSourceId);
                if (source == null) {
                    Messagebox.show("Fuente no encontrada", "Error", Messagebox.OK, Messagebox.ERROR);
                    return;
                }
                source.setRagdsupdatedat(new Timestamp(System.currentTimeMillis()));
            } else {
                // Crear
                source = new RagDataSource();
                source.setRagdscreatedat(new Timestamp(System.currentTimeMillis()));
                source.setRagdsdocumentcount(0);
            }
            
            // Actualizar campos
            RagSystem ragSystem = businessService.findEntity(RagSystem.class, sourceRagSystemId);
            if (ragSystem == null) {
                Messagebox.show("Sistema RAG no encontrado", "Error", Messagebox.OK, Messagebox.ERROR);
                return;
            }
            
            source.setRagSystem(ragSystem);
            source.setRagdssourcename(sourceName);
            source.setRagdssourcetype(sourceType);
            source.setRagdssourceurl(sourceUrl);
            source.setRagdsindexstatus(sourceIndexStatus);
            source.setRagdsdataclass(sourceDataClass);
            
            businessService.save(source);
            
            // Auditar
            logActivity(isEditMode ? "EDITAR" : "CREAR", "RAGDATASOURCES", source.getIdxragdatasource(), 
                (isEditMode ? "Editada" : "Creada") + " fuente: " + source.getRagdssourcename());
            
            showModal = false;
            loadSources();
            loadMetrics();
            
            Messagebox.show("Fuente guardada exitosamente", 
                "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                
        } catch (Exception e) {
            log.error("Error al guardar fuente", e);
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
    public void viewSource(@BindingParam("source") RagDataSource source) {
        log.info("Ver fuente: {}", source.getRagdssourcename());
        // TODO: Navegar a vista detallada
    }
    
    @Command
    @NotifyChange("*")
    public void reindexSource(@BindingParam("source") RagDataSource source) {
        log.info("Reindexar fuente: {}", source.getRagdssourcename());
        
        // TODO: Integración con leka-server para reindexación
        Messagebox.show("Funcionalidad de reindexación en desarrollo. Requiere integración con leka-server.", 
            "Info", Messagebox.OK, Messagebox.INFORMATION);
        
        // Auditar
        logActivity("EDITAR", "RAGDATASOURCES", source.getIdxragdatasource(), 
            "Reindexación solicitada: " + source.getRagdssourcename());
    }
    
    @Command
    @NotifyChange("*")
    public void deleteSource(@BindingParam("source") RagDataSource source) {
        Messagebox.show("¿Está seguro de eliminar la fuente: " + source.getRagdssourcename() + "?",
            "Confirmar", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_OK.equals(event.getName())) {
                    try {
                        businessService.removeFromID(source);
                        
                        // Auditar eliminación
                        logActivity("ELIMINAR", "RAGDATASOURCES", source.getIdxragdatasource(), 
                            "Fuente eliminada: " + source.getRagdssourcename());
                        
                        loadSources();
                        loadMetrics();
                        Messagebox.show("Fuente eliminada exitosamente", 
                            "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                    } catch (Exception e) {
                        log.error("Error al eliminar fuente", e);
                        Messagebox.show("Error: " + e.getMessage(), 
                            "Error", Messagebox.OK, Messagebox.ERROR);
                    }
                }
            });
    }
    
    private void clearForm() {
        sourceRagSystemId = null;
        sourceName = null;
        sourceType = "FILE_SYSTEM";
        sourceUrl = null;
        sourceIndexStatus = "PENDING";
        sourceDataClass = "INTERNAL";
    }
    
    public String getRagSystemName(RagSystem system) {
        return system != null ? system.getRagsystemname() : "-";
    }
    
    public String getStatusColor(String status) {
        if (status == null) return "badge bg-secondary";
        switch (status) {
            case "COMPLETED": return "badge bg-success";
            case "IN_PROGRESS": return "badge bg-warning";
            case "PENDING": return "badge bg-secondary";
            case "FAILED": return "badge bg-danger";
            default: return "badge bg-secondary";
        }
    }
    
    public String getClassificationColor(String classification) {
        if (classification == null) return "badge bg-secondary";
        switch (classification) {
            case "PUBLIC": return "badge bg-success";
            case "INTERNAL": return "badge bg-info";
            case "CONFIDENTIAL": return "badge bg-warning";
            case "RESTRICTED": return "badge bg-danger";
            default: return "badge bg-secondary";
        }
    }
    
    public String formatDate(Timestamp timestamp) {
        if (timestamp == null) return "-";
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(timestamp);
    }
    
    @Destroy
    public void destroy() {
        if (sourcesList != null) { 
            sourcesList.clear(); 
            sourcesList = null; 
        }
        if (ragSystemsList != null) {
            ragSystemsList.clear();
            ragSystemsList = null;
        }
        pageResult = null;
        pageParams = null;
        businessService = null;
    }
}

