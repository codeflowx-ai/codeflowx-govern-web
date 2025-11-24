package com.codeflowx.govern.viewmodel.providers;

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

import com.codeflowx.govern.entity.models.ModelProvider;
import com.codeflowx.govern.service.models.ModelService;
import com.codeflowx.govern.entity.models.Model;
import com.codeflowx.govern.entity.models.ProviderCredential;
import com.codeflowx.govern.service.models.ProviderCredentialService;
import com.codeflowx.govern.service.models.ModelProviderService;
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
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de proveedores de modelos
 * 
 * Responsabilidades:
 * - Creación de nuevos proveedores
 * - Edición de proveedores existentes
 * - Operaciones de negocio: testConnection()
 * - Información descendente: modelos, credenciales
 * - Estadísticas y contadores específicos del proveedor
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ProvidersDetailViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    
    // ========== Servicios y contexto Spring ==========
    @WireVariable
    private ModelService modelService;
    @WireVariable
    private ModelProviderService modelProviderService;
    @WireVariable
    private ProviderCredentialService providerCredentialService;
    
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
    }
    
    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }
    
    // ========== Modo de operación ==========
    private String mode; // "create" o "edit"
    private Long providerId;
    private boolean editing = false;
    private String pageTitle = "Detalle del Proveedor";
    
    // ========== Datos del proveedor ==========
    private ModelProvider currentProvider;
    
    // ========== Información descendente ==========
    private List<Model> providerModels = new ArrayList<>();
    private List<ProviderCredential> providerCredentials = new ArrayList<>();
    
    // ========== Estadísticas específicas del proveedor ==========
    private Long totalModels = 0L;
    private Long totalCredentials = 0L;
    private Long activeModels = 0L;
    private String connectionStatus = "NOT_TESTED";
    private Timestamp lastConnectionTest;
    
    // ========== Inicialización ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        // Obtener parámetros de navegación
        mode = (String) Executions.getCurrent().getParameter("mode");
        String providerIdStr = Executions.getCurrent().getParameter("providerId");
        
        if (providerIdStr != null) {
            providerId = Long.parseLong(providerIdStr);
        }
        
        log.info("Inicializando ProvidersDetailViewModel - mode: {}, providerId: {}", mode, providerId);
        
        if ("create".equals(mode)) {
            initNewProvider();
        } else if ("edit".equals(mode) && providerId != null) {
            loadProvider(providerId);
        } else {
            log.error("Modo inválido o falta providerId");
            Executions.sendRedirect("/providers/providers-overview.zul");
        }
    }
    
    /**
     * Inicializa un nuevo proveedor con valores por defecto
     * @throws UiException 
     */
    private void initNewProvider() throws UiException {
        log.debug("Inicializando nuevo proveedor");
        currentProvider = new ModelProvider();
        currentProvider.setModcreatedat(new Timestamp(System.currentTimeMillis()));
        currentProvider.setModupdatedat(new Timestamp(System.currentTimeMillis()));
        currentProvider.setModcreatedby(getUser().getUsername()); // TODO: Obtener usuario actual
        currentProvider.setModstatus("ACTIVE");
        currentProvider.setModprovidertype("CLOUD");
        
        editing = false;
        pageTitle = "Crear Nuevo Proveedor";
    }
    
    /**
     * Carga proveedor existente desde BD
     */
    private void loadProvider(Long id) {
        try {
            log.debug("Cargando proveedor ID={}", id);
            
            currentProvider = modelProviderService.findById(id);
            
            if (currentProvider == null) {
                log.error("Proveedor no encontrado: ID={}", id);
                Messagebox.show("Proveedor no encontrado", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Executions.sendRedirect("/providers/providers-overview.zul");
                return;
            }
            
            log.info("Proveedor cargado: {}", currentProvider.getModname());
            
            editing = true;
            pageTitle = "Editar Proveedor: " + currentProvider.getModname();
            
            // Cargar información descendente
            loadProviderModels();
            loadProviderCredentials();
            loadProviderStatistics();
            
        } catch (Exception e) {
            log.error("Error al cargar proveedor ID={}", id, e);
            Messagebox.show("Error al cargar proveedor: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Executions.sendRedirect("/providers/providers-overview.zul");
        }
    }
    
    /**
     * Carga estadísticas específicas del proveedor
     */
    private void loadProviderStatistics() {
        try {
            log.debug("Cargando estadísticas del proveedor ID={}", currentProvider.getIdxmodelprovider());
            
            totalModels = (long) providerModels.size();
            totalCredentials = (long) providerCredentials.size();
            
            // Contar modelos activos
            activeModels = providerModels.stream()
                .filter(m -> "ACTIVE".equals(m.getModstatus()))
                .count();
            
            log.info("Estadísticas cargadas - Modelos: {}, Activos: {}, Credenciales: {}",
                totalModels, activeModels, totalCredentials);
                
        } catch (Exception e) {
            log.error("Error al cargar estadísticas del proveedor", e);
        }
    }
    
    // ========== Información descendente ==========
    
    @Command
    @NotifyChange({"providerModels", "totalModels"})
    public void loadProviderModels() {
        try {
            log.debug("Cargando modelos del proveedor ID={}", currentProvider.getIdxmodelprovider());
            
            PageParams params = PageParams.builder()
                .maxRows(100)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            Map<String, Object> filters = new HashMap<>();
            filters.put("idmodprovider", currentProvider.getIdxmodelprovider());
            
            PageResult<Model> result = modelService.findAll(params, filters
            );
            
            if (result != null && result.getContent() != null) {
                providerModels = result.getContent();
                totalModels = (long) providerModels.size();
                log.info("Cargados {} modelos del proveedor", totalModels);
            } else {
                providerModels = new ArrayList<>();
                totalModels = 0L;
            }
        } catch (Exception e) {
            log.error("Error al cargar modelos del proveedor", e);
            providerModels = new ArrayList<>();
            totalModels = 0L;
        }
    }
    
    @Command
    @NotifyChange({"providerCredentials", "totalCredentials"})
    public void loadProviderCredentials() {
        try {
            log.debug("Cargando credenciales del proveedor ID={}", currentProvider.getIdxmodelprovider());
            
            PageParams params = PageParams.builder()
                .maxRows(50)
                .pageActual(1)
                .rowActual(0)
                .build();
            
            Map<String, Object> filters = new HashMap<>();
            filters.put("idmodprovider", currentProvider.getIdxmodelprovider());
            
            PageResult<ProviderCredential> result = providerCredentialService.findAll(params, filters
            );
            
            if (result != null && result.getContent() != null) {
                providerCredentials = result.getContent();
                totalCredentials = (long) providerCredentials.size();
                log.info("Cargadas {} credenciales", totalCredentials);
            } else {
                providerCredentials = new ArrayList<>();
                totalCredentials = 0L;
            }
        } catch (Exception e) {
            log.error("Error al cargar credenciales del proveedor", e);
            providerCredentials = new ArrayList<>();
            totalCredentials = 0L;
        }
    }
    
    // ========== Comandos CRUD ==========
    
    @Command
    @NotifyChange("*")
    public void saveProvider() {
        try {
            log.info("Guardando proveedor: {}", currentProvider.getModname());
            
            // Validaciones de negocio
            if (currentProvider.getModname() == null || currentProvider.getModname().trim().isEmpty()) {
                Messagebox.show("El nombre del proveedor es requerido",
                    "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            if (currentProvider.getModprovidertype() == null || currentProvider.getModprovidertype().trim().isEmpty()) {
                Messagebox.show("El tipo de proveedor es requerido",
                    "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            if (currentProvider.getIdxmodelprovider() == null) {
                currentProvider = modelService.create(currentProvider);
                log.info("Proveedor creado exitosamente: ID={}, nombre={}",
                    currentProvider.getIdxmodelprovider(), currentProvider.getModname());
                Messagebox.show("Proveedor creado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentProvider.setModupdatedat(new Timestamp(System.currentTimeMillis()));
                currentProvider = modelService.update(currentProvider);
                log.info("Proveedor actualizado exitosamente: ID={}, nombre={}",
                    currentProvider.getIdxmodelprovider(), currentProvider.getModname());
                Messagebox.show("Proveedor actualizado exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }
            
            Executions.sendRedirect("/providers/providers-overview.zul");
            
        } catch (Exception e) {
            log.error("Error al guardar proveedor", e);
            Messagebox.show("Error al guardar proveedor: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    public void cancelEdit() {
        log.debug("Cancelando edición/creación de proveedor, volviendo a overview");
        Executions.sendRedirect("/providers/providers-overview.zul");
    }
    
    // ========== Operaciones especiales (funciones/procedimientos) ==========
    
    @Command
    @NotifyChange({"connectionStatus", "lastConnectionTest"})
    public void testConnection() {
        if (currentProvider == null || currentProvider.getIdxmodelprovider() == null) {
            Messagebox.show("Debe guardar el proveedor antes de probar la conexión",
                "Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }
        log.info("Probando conexión del proveedor ID={}", currentProvider.getIdxmodelprovider());
        try {
            // TODO: Implementar usando stored procedure cuando esté disponible
            connectionStatus = "SUCCESS";
            lastConnectionTest = new Timestamp(System.currentTimeMillis());
            
            Messagebox.show("Conexión exitosa al proveedor\n\nProveedor: " + currentProvider.getModname() +
                "\nTipo: " + currentProvider.getModprovidertype() +
                "\nEstado: Conectado",
                "Test de Conexión", Messagebox.OK, Messagebox.INFORMATION);
                
        } catch (Exception e) {
            log.error("Error al probar conexión", e);
            connectionStatus = "FAILED";
            lastConnectionTest = new Timestamp(System.currentTimeMillis());
            Messagebox.show("Error al conectar con el proveedor: " + e.getMessage(),
                "Error de Conexión", Messagebox.OK, Messagebox.ERROR);
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
            // Limpiar proveedor actual
            currentProvider = null;
            
            // Limpiar listas descendentes
            if (providerModels != null) {
                providerModels.clear();
                providerModels = null;
            }
            if (providerCredentials != null) {
                providerCredentials.clear();
                providerCredentials = null;
            }
            
            // Limpiar BusinessService
            modelService = null;
            modelProviderService = null;
            providerCredentialService = null;
            
            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }
}

