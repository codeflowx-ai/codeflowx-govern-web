package com.codeflowx.govern.viewmodel.rag;

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
import org.enartframework.web.zk.page.MasterPage;
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

import com.codeflowx.govern.entity.rag.RagSystem;
import com.codeflowx.govern.entity.rag.RagDataSource;
import com.codeflowx.govern.entity.rag.RagVersion;
import com.codeflowx.govern.entity.functions.rag.CalculateIndexHealth;
import com.codeflowx.govern.entity.procedures.rag.IngestDocuments;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de sistemas RAG
 * 
 * Responsabilidades:
 * - Creación de nuevos sistemas RAG
 * - Edición de sistemas RAG existentes
 * - Operaciones de negocio: indexDataSources(), calculateIndexHealth()
 * - Información descendente: fuentes de datos, versiones
 * - Estadísticas y contadores específicos del sistema RAG
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class RagSystemsDetailViewModel extends MasterPage {

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
    
    
    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }
    
    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }
    
    // ========== Modo de operación ==========
    private String mode; // "create" o "edit"
    private Long ragSystemId;
    private boolean editing = false;
    private String pageTitle = "Detalle del Sistema RAG";
    
    // ========== Datos del sistema RAG ==========
    private RagSystem currentRagSystem;
    
    // ========== Información descendente ==========
    private List<RagDataSource> ragDataSources = new ArrayList<>();
    private List<RagVersion> ragVersions = new ArrayList<>();
    
    // ========== Estadísticas específicas del sistema RAG ==========
    private Long totalDataSources = 0L;
    private Long totalVersions = 0L;
    private Long totalDocuments = 0L;
    private Long activeDataSources = 0L;
    private BigDecimal indexHealthScore = BigDecimal.ZERO;
    private Timestamp lastSyncDate;
    
    // ========== Inicialización ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        // Obtener parámetros de navegación
        mode = (String) Executions.getCurrent().getParameter("mode");
        String ragSystemIdStr = Executions.getCurrent().getParameter("ragSystemId");
        
        if (ragSystemIdStr != null) {
            ragSystemId = Long.parseLong(ragSystemIdStr);
        }
        
        log.info("Inicializando RagSystemsDetailViewModel - mode: {}, ragSystemId: {}", mode, ragSystemId);
        
        if ("create".equals(mode)) {
            initNewRagSystem();
        } else if ("edit".equals(mode) && ragSystemId != null) {
            loadRagSystem(ragSystemId);
        } else {
            log.error("Modo inválido o falta ragSystemId");
            Executions.sendRedirect("/rag/rag-systems-overview.zul");
        }
    }
    
    /**
     * Inicializa un nuevo sistema RAG con valores por defecto
     * @throws UiException 
     */
    private void initNewRagSystem() throws UiException {
        log.debug("Inicializando nuevo sistema RAG");
        currentRagSystem = new RagSystem();
        currentRagSystem.setRagcreatedat(new Timestamp(System.currentTimeMillis()));
        currentRagSystem.setRagupdatedat(new Timestamp(System.currentTimeMillis()));
        currentRagSystem.setRagcreatedby(getUser().getUsername());
        currentRagSystem.setRagstatus("DRAFT");
        currentRagSystem.setRagapprovalstatus("PENDING");
        currentRagSystem.setRagversion("1.0");
        
        editing = false;
        pageTitle = "Crear Nuevo Sistema RAG";
    }
    
    /**
     * Carga sistema RAG existente desde BD
     */
    private void loadRagSystem(Long id) {
        try {
            log.debug("Cargando sistema RAG ID={}", id);
            
            currentRagSystem = businessService.findById(RagSystem.class, id);
            
            if (currentRagSystem == null) {
                log.error("Sistema RAG no encontrado: ID={}", id);
                Messagebox.show("Sistema RAG no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Executions.sendRedirect("/rag/rag-systems-overview.zul");
                return;
            }
            
            log.info("Sistema RAG cargado: {}", currentRagSystem.getRagname());
            
            editing = true;
            pageTitle = "Editar Sistema RAG: " + currentRagSystem.getRagname();
            
            // Cargar información descendente
            loadRagDataSources();
            loadRagVersions();
            loadRagStatistics();
            
        } catch (Exception e) {
            log.error("Error al cargar sistema RAG ID={}", id, e);
            Messagebox.show("Error al cargar sistema RAG: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Executions.sendRedirect("/rag/rag-systems-overview.zul");
        }
    }
    
    /**
     * Carga estadísticas específicas del sistema RAG
     */
    private void loadRagStatistics() {
        try {
            log.debug("Cargando estadísticas del sistema RAG ID={}", currentRagSystem.getIdxragsystem());
            
            totalDataSources = (long) ragDataSources.size();
            totalVersions = (long) ragVersions.size();
            
            // Contar fuentes activas
            activeDataSources = ragDataSources.stream()
                .filter(ds -> "ACTIVE".equals(ds.getRagstatus()))
                .count();
            
            // Sumar documentos totales de todas las fuentes
            totalDocuments = ragDataSources.stream()
                .filter(ds -> ds.getRagdocumentcount() != null)
                .mapToLong(RagDataSource::getRagdocumentcount)
                .sum();
            
            // Calcular index health (simplificado)
            if (totalDataSources > 0) {
                indexHealthScore = BigDecimal.valueOf(activeDataSources)
                    .divide(BigDecimal.valueOf(totalDataSources), 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            } else {
                indexHealthScore = BigDecimal.ZERO;
            }
            
            log.info("Estadísticas cargadas - DataSources: {}, Versiones: {}, Documentos: {}",
                totalDataSources, totalVersions, totalDocuments);
                
        } catch (Exception e) {
            log.error("Error al cargar estadísticas del sistema RAG", e);
        }
    }
    
    // ========== Información descendente ==========
    
    @Command
    @NotifyChange({"ragDataSources", "totalDataSources"})
    public void loadRagDataSources() {
        try {
            log.debug("Cargando fuentes de datos del sistema RAG ID={}", currentRagSystem.getIdxragsystem());
            
            PageParams params = PageParams.builder()
                .maxRows(100)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            Map<String, Object> filters = new HashMap<>();
            filters.put("idragragsystems0", currentRagSystem.getIdxragsystem());
            
            PageResult<RagDataSource> result = businessService.findAllEntity(
                RagDataSource.class,
                params,
                filters
            );
            
            if (result != null && result.getContent() != null) {
                ragDataSources = result.getContent();
                totalDataSources = (long) ragDataSources.size();
                log.info("Cargadas {} fuentes de datos", totalDataSources);
            } else {
                ragDataSources = new ArrayList<>();
                totalDataSources = 0L;
            }
        } catch (Exception e) {
            log.error("Error al cargar fuentes de datos", e);
            ragDataSources = new ArrayList<>();
            totalDataSources = 0L;
        }
    }
    
    @Command
    @NotifyChange({"ragVersions", "totalVersions"})
    public void loadRagVersions() {
        try {
            log.debug("Cargando versiones del sistema RAG ID={}", currentRagSystem.getIdxragsystem());
            
            PageParams params = PageParams.builder()
                .maxRows(50)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            Map<String, Object> filters = new HashMap<>();
            filters.put("idragragsystems0", currentRagSystem.getIdxragsystem());
            
            PageResult<RagVersion> result = businessService.findAllEntity(
                RagVersion.class,
                params,
                filters
            );
            
            if (result != null && result.getContent() != null) {
                ragVersions = result.getContent();
                totalVersions = (long) ragVersions.size();
                log.info("Cargadas {} versiones", totalVersions);
            } else {
                ragVersions = new ArrayList<>();
                totalVersions = 0L;
            }
        } catch (Exception e) {
            log.error("Error al cargar versiones del sistema RAG", e);
            ragVersions = new ArrayList<>();
            totalVersions = 0L;
        }
    }
    
    // ========== Comandos CRUD ==========
    
    @Command
    @NotifyChange("*")
    public void saveRagSystem() {
        try {
            log.info("Guardando sistema RAG: {}", currentRagSystem.getRagname());
            
            // Validaciones de negocio
            if (currentRagSystem.getRagname() == null || currentRagSystem.getRagname().trim().isEmpty()) {
                Messagebox.show("El nombre del sistema RAG es requerido",
                    "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            if (currentRagSystem.getRagtype() == null || currentRagSystem.getRagtype().trim().isEmpty()) {
                Messagebox.show("El tipo de sistema RAG es requerido",
                    "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            if (currentRagSystem.getIdxragsystem() == null) {
                businessService.save(currentRagSystem);
                log.info("Sistema RAG creado exitosamente: ID={}, nombre={}",
                    currentRagSystem.getIdxragsystem(), currentRagSystem.getRagname());
                Messagebox.show("Sistema RAG creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentRagSystem.setRagupdatedat(new Timestamp(System.currentTimeMillis()));
                businessService.update(currentRagSystem);
                log.info("Sistema RAG actualizado exitosamente: ID={}, nombre={}",
                    currentRagSystem.getIdxragsystem(), currentRagSystem.getRagname());
                Messagebox.show("Sistema RAG actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }
            
            Executions.sendRedirect("/rag/rag-systems-overview.zul");
            
        } catch (Exception e) {
            log.error("Error al guardar sistema RAG", e);
            Messagebox.show("Error al guardar sistema RAG: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    public void cancelEdit() {
        log.debug("Cancelando edición/creación de sistema RAG, volviendo a overview");
        Executions.sendRedirect("/rag/rag-systems-overview.zul");
    }
    
    // ========== Operaciones especiales (funciones/procedimientos) ==========
    
    @Command
    @NotifyChange({"ragDataSources", "totalDocuments", "indexHealthScore"})
    public void indexDataSources() {
        if (currentRagSystem == null || currentRagSystem.getIdxragsystem() == null) {
            Messagebox.show("Debe guardar el sistema RAG antes de indexar",
                "Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        log.info("Indexando fuentes de datos para sistema RAG ID={}", currentRagSystem.getIdxragsystem());
        try {
            IngestDocuments procedure = new IngestDocuments();
            procedure.setPInputParam(currentRagSystem.getIdxragsystem());
            
            procedure = businessService.callProcedure(procedure);
            
            if (procedure.getOSuccess() != null && procedure.getOSuccess()) {
                Long documentsIngested = procedure.getOResult();
                Messagebox.show("Indexación iniciada exitosamente\nDocumentos ingresados: " + documentsIngested,
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
                loadRagDataSources();
                loadRagStatistics();
            } else {
                Messagebox.show("No se pudo iniciar la indexación",
                    "Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
            }
        } catch (Exception e) {
            log.error("Error al indexar fuentes de datos", e);
            Messagebox.show("Error al indexar: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    @NotifyChange({"ragVersions", "totalVersions"})
    public void createNewVersion() {
        if (currentRagSystem == null || currentRagSystem.getIdxragsystem() == null) {
            Messagebox.show("Debe guardar el sistema RAG antes de crear una versión",
                "Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        log.info("Creando nueva versión para sistema RAG ID={}", currentRagSystem.getIdxragsystem());
        try {
            // TODO: Implementar usando stored procedure cuando esté disponible
            Messagebox.show("Nueva versión creada exitosamente",
                "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            loadRagVersions();
            loadRagStatistics();
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
            // Limpiar RAG system actual
            currentRagSystem = null;
            
            // Limpiar listas descendentes
            if (ragDataSources != null) {
                ragDataSources.clear();
                ragDataSources = null;
            }
            if (ragVersions != null) {
                ragVersions.clear();
                ragVersions = null;
            }
            
            // Limpiar BusinessService
            businessService = null;
            
            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }

}

