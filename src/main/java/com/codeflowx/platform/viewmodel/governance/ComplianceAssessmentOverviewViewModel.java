package com.codeflowx.platform.viewmodel.governance;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.event.PagingEvent;
import com.codeflowx.govern.entity.governance.ComplianceAssessment;
import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.entity.models.ModelApproval;
import com.codeflowx.govern.entity.evaluation.ModelBiasAnalysis;
import com.codeflowx.govern.entity.evaluation.ModelPerformance;
import com.codeflowx.admin.Ssoractividad;
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
 * ViewModel para Compliance Dashboard
 * Muestra KPIs, modelos non-compliant y métricas de compliance EU AI Act
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ComplianceAssessmentOverviewViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    private static final String IDDESKTOP = "contenedor";
    
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
    
    // ========== Paginación ==========
    private PageParams pageParams;
    private PageResult<Model> pageResult;
    
    // ========== Filtros ==========
    private String searchTerm = "";
    private String riskLevelFilter = "ALL";
    
    // ========== Datos Dashboard ==========
    private List<Model> nonCompliantModels = new ArrayList<>();
    private List<Model> allModels = new ArrayList<>();
    
    // ========== KPIs Dashboard ==========
    private Integer totalModels = 0;
    private String compliancePercentage = "0%";
    private Integer modelsInProductionNonCompliant = 0;
    private Integer biasAnalysisThisMonth = 0;
    private Integer compliantModelsCount = 0;
    private Integer nonCompliantModelsCount = 0;
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        pageParams = PageParams.builder()
            .maxRows(20)
            .pageActual(1)
            .rowActual(0)
            .build();
        
        loadData();
    }
    
    @Command
    @NotifyChange("*")
    public void loadData() {
        try {
            log.info("=== CARGANDO COMPLIANCE DASHBOARD ===");
            
            // 1. Cargar todos los modelos
            loadAllModels();
            
            // 2. Calcular KPIs
            calculateKPIs();
            
            // 3. Cargar modelos non-compliant (con filtros)
            loadNonCompliantModels();
            
            // 4. Cargar análisis de sesgo del mes
            loadBiasAnalysisThisMonth();
            
            // Auditar acceso al dashboard
            logActivity("VIEW", "COMPLIANCE_DASHBOARD", null, 
                "Dashboard accedido - " + totalModels + " modelos, " + nonCompliantModelsCount + " non-compliant");
            
            log.info("Dashboard cargado: {} modelos totales, {} non-compliant, {}% compliance", 
                totalModels, nonCompliantModelsCount, compliancePercentage);
                
        } catch (Exception e) {
            log.error("Error al cargar dashboard de compliance", e);
            Messagebox.show("Error al cargar dashboard: " + e.getMessage(), 
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    /**
     * Carga todos los modelos del sistema
     */
    private void loadAllModels() {
        try {
            PageParams largePageParams = PageParams.builder()
                .maxRows(10000)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            PageResult<Model> result = businessService.findAllEntity(
                Model.class,
                largePageParams,
                new Criterias()
            );
            
            if (result != null && result.getContent() != null) {
                allModels = result.getContent();
                totalModels = allModels.size();
                log.debug("Cargados {} modelos totales", totalModels);
            } else {
                allModels = new ArrayList<>();
                totalModels = 0;
            }
        } catch (Exception e) {
            log.error("Error al cargar modelos", e);
            allModels = new ArrayList<>();
            totalModels = 0;
        }
    }
    
    /**
     * Calcula los KPIs del dashboard
     */
    private void calculateKPIs() {
        try {
            if (allModels == null || allModels.isEmpty()) {
                log.debug("No hay modelos para calcular KPIs");
                return;
            }
            
            // Contar modelos compliant vs non-compliant
            compliantModelsCount = 0;
            nonCompliantModelsCount = 0;
            modelsInProductionNonCompliant = 0;
            
            for (Model model : allModels) {
                boolean isCompliant = isModelCompliant(model);
                
                if (isCompliant) {
                    compliantModelsCount++;
                } else {
                    nonCompliantModelsCount++;
                    
                    // Contar modelos en producción sin compliance
                    if ("PRODUCTION".equals(model.getModstatus())) {
                        modelsInProductionNonCompliant++;
                    }
                }
            }
            
            // Calcular porcentaje compliance
            if (totalModels > 0) {
                double percentage = (compliantModelsCount * 100.0) / totalModels;
                compliancePercentage = String.format("%.1f%%", percentage);
            } else {
                compliancePercentage = "0%";
            }
            
            log.debug("KPIs calculados: {} compliant, {} non-compliant, {} en producción non-compliant", 
                compliantModelsCount, nonCompliantModelsCount, modelsInProductionNonCompliant);
                
        } catch (Exception e) {
            log.error("Error al calcular KPIs", e);
        }
    }
    
    /**
     * Determina si un modelo es compliant (6/6 checks)
     */
    private boolean isModelCompliant(Model model) {
        if (model == null) return false;
        
        int checksCount = 0;
        
        // Check 1: Risk Classification documentada
        if (model.getModrisklevel() != null && !model.getModrisklevel().trim().isEmpty()) {
            checksCount++;
        }
        
        // Check 2: Dataset Quality validado (si existe descripción del dataset)
        if (model.getModdatasetdescription() != null && !model.getModdatasetdescription().trim().isEmpty()) {
            checksCount++;
        }
        
        // Check 3: Bias Analysis realizado (verificar si tiene análisis)
        if (hasBiasAnalysis(model)) {
            checksCount++;
        }
        
        // Check 4: Performance Metrics documentadas
        if (hasPerformanceMetrics(model)) {
            checksCount++;
        }
        
        // Check 5: Modelo aprobado
        if ("APPROVED".equals(model.getModstatus()) || "PRODUCTION".equals(model.getModstatus())) {
            checksCount++;
        }
        
        // Check 6: Audit Trail disponible (si tiene fecha creación)
        if (model.getModcreateddate() != null) {
            checksCount++;
        }
        
        // Compliant si tiene 6/6 checks
        return checksCount == 6;
    }
    
    /**
     * Verifica si el modelo tiene análisis de sesgo
     */
    private boolean hasBiasAnalysis(Model model) {
        try {
            Criterias criterias = new Criterias();
            Criteria modelCriteria = new Criteria(Operation.AND, Evaluation.EQUALS, "model.idxmodel");
            modelCriteria.setValues(new Object[]{model.getIdxmodel()});
            criterias.addCriteria(modelCriteria);
            
            PageParams params = PageParams.builder().maxRows(1).pageActual(1).rowActual(0).build();
            PageResult<ModelBiasAnalysis> result = businessService.findAllEntity(
                ModelBiasAnalysis.class, params, criterias
            );
            
            return result != null && result.getContent() != null && !result.getContent().isEmpty();
        } catch (Exception e) {
            log.debug("Error verificando bias analysis para modelo {}: {}", model.getIdxmodel(), e.getMessage());
            return false;
        }
    }
    
    /**
     * Verifica si el modelo tiene métricas de performance
     */
    private boolean hasPerformanceMetrics(Model model) {
        try {
            Criterias criterias = new Criterias();
            Criteria modelCriteria = new Criteria(Operation.AND, Evaluation.EQUALS, "model.idxmodel");
            modelCriteria.setValues(new Object[]{model.getIdxmodel()});
            criterias.addCriteria(modelCriteria);
            
            PageParams params = PageParams.builder().maxRows(1).pageActual(1).rowActual(0).build();
            PageResult<ModelPerformance> result = businessService.findAllEntity(
                ModelPerformance.class, params, criterias
            );
            
            return result != null && result.getContent() != null && !result.getContent().isEmpty();
        } catch (Exception e) {
            log.debug("Error verificando performance metrics para modelo {}: {}", model.getIdxmodel(), e.getMessage());
            return false;
        }
    }
    
    /**
     * Carga modelos non-compliant con filtros aplicados
     */
    private void loadNonCompliantModels() {
        try {
            if (allModels == null || allModels.isEmpty()) {
                nonCompliantModels = new ArrayList<>();
                return;
            }
            
            // Filtrar modelos non-compliant
            nonCompliantModels = allModels.stream()
                .filter(model -> !isModelCompliant(model))
                .filter(model -> applySearchFilter(model))
                .filter(model -> applyRiskLevelFilter(model))
                .collect(Collectors.toList());
            
            log.debug("Filtrados {} modelos non-compliant", nonCompliantModels.size());
            
        } catch (Exception e) {
            log.error("Error al cargar modelos non-compliant", e);
            nonCompliantModels = new ArrayList<>();
        }
    }
    
    /**
     * Aplica filtro de búsqueda por texto
     */
    private boolean applySearchFilter(Model model) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return true;
        }
        
        String searchLower = searchTerm.toLowerCase();
        return (model.getModname() != null && model.getModname().toLowerCase().contains(searchLower)) ||
               (model.getModowner() != null && model.getModowner().toLowerCase().contains(searchLower));
    }
    
    /**
     * Aplica filtro por risk level
     */
    private boolean applyRiskLevelFilter(Model model) {
        if ("ALL".equals(riskLevelFilter)) {
            return true;
        }
        return riskLevelFilter.equals(model.getModrisklevel());
    }
    
    /**
     * Carga cantidad de análisis de sesgo realizados este mes
     */
    private void loadBiasAnalysisThisMonth() {
        try {
            LocalDate now = LocalDate.now();
            LocalDate firstDayOfMonth = now.withDayOfMonth(1);
            
            Timestamp startOfMonth = Timestamp.from(
                firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant()
            );
            
            Criterias criterias = new Criterias();
            Criteria dateCriteria = new Criteria(Operation.AND, Evaluation.GREATER_OR_EQUALS, "modanalysisdate");
            dateCriteria.setValues(new Object[]{startOfMonth});
            criterias.addCriteria(dateCriteria);
            
            PageParams params = PageParams.builder().maxRows(10000).pageActual(1).rowActual(0).build();
            PageResult<ModelBiasAnalysis> result = businessService.findAllEntity(
                ModelBiasAnalysis.class, params, criterias
            );
            
            biasAnalysisThisMonth = (result != null && result.getContent() != null) 
                ? result.getContent().size() 
                : 0;
            
            log.debug("Análisis de sesgo este mes: {}", biasAnalysisThisMonth);
            
        } catch (Exception e) {
            log.error("Error al cargar análisis de sesgo del mes", e);
            biasAnalysisThisMonth = 0;
        }
    }
    
    /**
     * Retorna el compliance score de un modelo específico (ej: "4/6")
     */
    public String getComplianceScoreForModel(Model model) {
        if (model == null) return "0/6";
        
        int checksCount = 0;
        
        if (model.getModrisklevel() != null && !model.getModrisklevel().trim().isEmpty()) checksCount++;
        if (model.getModdatasetdescription() != null && !model.getModdatasetdescription().trim().isEmpty()) checksCount++;
        if (hasBiasAnalysis(model)) checksCount++;
        if (hasPerformanceMetrics(model)) checksCount++;
        if ("APPROVED".equals(model.getModstatus()) || "PRODUCTION".equals(model.getModstatus())) checksCount++;
        if (model.getModcreateddate() != null) checksCount++;
        
        return checksCount + "/6";
    }
    
    /**
     * Retorna texto con lo que falta para compliance de un modelo
     */
    public String getMissingComplianceItems(Model model) {
        if (model == null) return "Todo";
        
        List<String> missing = new ArrayList<>();
        
        if (model.getModrisklevel() == null || model.getModrisklevel().trim().isEmpty()) {
            missing.add("Risk Classification");
        }
        if (model.getModdatasetdescription() == null || model.getModdatasetdescription().trim().isEmpty()) {
            missing.add("Dataset Quality");
        }
        if (!hasBiasAnalysis(model)) {
            missing.add("Bias Analysis");
        }
        if (!hasPerformanceMetrics(model)) {
            missing.add("Performance Metrics");
        }
        if (!"APPROVED".equals(model.getModstatus()) && !"PRODUCTION".equals(model.getModstatus())) {
            missing.add("Aprobación");
        }
        if (model.getModcreateddate() == null) {
            missing.add("Audit Trail");
        }
        
        if (missing.isEmpty()) {
            return "✅ Completo";
        }
        
        return String.join(", ", missing);
    }
    
    @Command
    @NotifyChange("*")
    public void applyFilters() {
        log.debug("Aplicando filtros: searchTerm='{}', riskLevel='{}'", searchTerm, riskLevelFilter);
        loadNonCompliantModels();
    }
    
    @Command
    @NotifyChange("*")
    public void clearFilters() {
        log.debug("Limpiando filtros");
        searchTerm = "";
        riskLevelFilter = "ALL";
        loadNonCompliantModels();
    }
    
    @Command
    public void viewModelDetails(@BindingParam("modelId") Long modelId) {
        log.info("Navegando a detalle modelo ID={}", modelId);
        Map<String, Object> params = new HashMap<>();
        params.put("dataParam", modelId);
        params.put("action", Action.LOAD);
        appendPage("plataforma/models/models-detail.zul", page.getFellow(IDDESKTOP), params);
    }
    
    @Command
    public void navigateToModels() {
        log.info("Navegando a lista de modelos");
        appendPage("plataforma/models/models-overview.zul", page.getFellow(IDDESKTOP), new HashMap<>());
    }
    
    @Command
    public void navigateToBiasAnalysis() {
        log.info("Navegando a análisis de sesgo");
        appendPage("plataforma/models/bias-analysis/overview.zul", page.getFellow(IDDESKTOP), new HashMap<>());
    }
    
    @Command
    public void navigateToApprovals() {
        log.info("Navegando a aprobaciones pendientes");
        appendPage("plataforma/models/approval/overview.zul", page.getFellow(IDDESKTOP), new HashMap<>());
    }
    
    @Command
    public void exportComplianceReport() {
        log.info("Exportando reporte de compliance");
        try {
            Messagebox.show(
                "Funcionalidad de exportación en desarrollo.\n\n" +
                "Próximamente podrá exportar:\n" +
                "- Reporte PDF con todos los KPIs\n" +
                "- Lista detallada de modelos non-compliant\n" +
                "- Recomendaciones de compliance", 
                "Exportar Reporte", 
                Messagebox.OK, 
                Messagebox.INFORMATION
            );
        } catch (Exception e) {
            log.error("Error mostrando mensaje de exportación", e);
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
            // Limpiar listas
            if (nonCompliantModels != null) {
                nonCompliantModels.clear();
                nonCompliantModels = null;
            }
            
            if (allModels != null) {
                allModels.clear();
                allModels = null;
            }
            
            // Limpiar PageResult
            if (pageResult != null) {
                if (pageResult.getContent() != null) {
                    pageResult.getContent().clear();
                }
                pageResult = null;
            }
            
            // Limpiar PageParams
            pageParams = null;
            
            // Limpiar BusinessService
            businessService = null;
            
            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }
}
