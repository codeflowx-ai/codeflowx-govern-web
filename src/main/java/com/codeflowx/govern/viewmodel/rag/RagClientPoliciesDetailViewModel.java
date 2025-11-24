package com.codeflowx.govern.viewmodel.rag;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.enartframework.web.exception.UiException;
import org.enartframework.web.zk.page.MasterPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.govern.entity.rag.RagClientPolicy;
import com.codeflowx.govern.service.rag.RagClientPolicyService;

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel para DETALLE/EDICIÓN/CREACIÓN de políticas de cliente RAG
 * 
 * Responsabilidades:
 * - Creación de nuevas políticas de cliente RAG
 * - Edición de políticas de cliente RAG existentes
 * - Validación de reglas de política (JSONB)
 * - Gestión de scores y pesos
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class RagClientPoliciesDetailViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;
    
    // ========== Servicios y contexto Spring ==========
    @WireVariable
    private RagClientPolicyService ragClientPolicyService;
    
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
    private Long policyId;
    private boolean editing = false;
    private String pageTitle = "Detalle de Política de Cliente RAG";
    
    // ========== Datos de la política ==========
    private RagClientPolicy currentPolicy;
    
    // ========== Inicialización ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        // Obtener parámetros de navegación
        mode = (String) Executions.getCurrent().getParameter("mode");
        String policyIdStr = Executions.getCurrent().getParameter("policyId");
        
        if (policyIdStr != null) {
            policyId = Long.parseLong(policyIdStr);
        }
        
        log.info("Inicializando RagClientPoliciesDetailViewModel - mode: {}, policyId: {}", mode, policyId);
        
        if ("create".equals(mode)) {
            initNewPolicy();
        } else if ("edit".equals(mode) && policyId != null) {
            loadPolicy(policyId);
        } else {
            log.error("Modo inválido o falta policyId");
            Executions.sendRedirect("/gobierno/rag/rag-client-policies-overview.zul");
        }
    }
    
    /**
     * Inicializa una nueva política con valores por defecto
     * @throws UiException 
     */
    private void initNewPolicy() throws UiException {
        log.debug("Inicializando nueva política de cliente RAG");
        currentPolicy = new RagClientPolicy();
        currentPolicy.setRcpcreatedat(new Timestamp(System.currentTimeMillis()));
        currentPolicy.setRcpupdatedat(new Timestamp(System.currentTimeMillis()));
        currentPolicy.setRcpenabled(true);
        currentPolicy.setRcpminscore(BigDecimal.valueOf(70.0)); // Default 70
        currentPolicy.setRcpweight(BigDecimal.valueOf(1.0)); // Default peso 1.0
        currentPolicy.setRcppolicyrules("{}"); // JSONB vacío por defecto
        
        editing = false;
        pageTitle = "Crear Nueva Política de Cliente RAG";
    }
    
    /**
     * Carga política existente desde BD
     */
    private void loadPolicy(Long id) {
        try {
            log.debug("Cargando política de cliente RAG ID={}", id);
            
            currentPolicy = ragClientPolicyService.findById(id);
            
            if (currentPolicy == null) {
                log.error("Política de cliente RAG no encontrada: ID={}", id);
                Messagebox.show("Política de cliente RAG no encontrada", "Error", 
                    Messagebox.OK, Messagebox.ERROR);
                Executions.sendRedirect("/gobierno/rag/rag-client-policies-overview.zul");
                return;
            }
            
            log.info("Política de cliente RAG cargada: {}", currentPolicy.getRcppolicyname());
            
            editing = true;
            pageTitle = "Editar Política de Cliente RAG: " + currentPolicy.getRcppolicyname();
            
        } catch (Exception e) {
            log.error("Error al cargar política de cliente RAG ID={}", id, e);
            Messagebox.show("Error al cargar política de cliente RAG: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
            Executions.sendRedirect("/gobierno/rag/rag-client-policies-overview.zul");
        }
    }
    
    // ========== Comandos CRUD ==========
    
    @Command
    @NotifyChange("*")
    public void savePolicy() {
        try {
            log.info("Guardando política de cliente RAG: {}", currentPolicy.getRcppolicyname());
            
            // Validaciones de negocio
            if (currentPolicy.getRcppolicyname() == null || currentPolicy.getRcppolicyname().trim().isEmpty()) {
                Messagebox.show("El nombre de la política es requerido",
                    "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            if (currentPolicy.getRcpclientid() == null) {
                Messagebox.show("El ID del cliente es requerido",
                    "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            if (currentPolicy.getRcppolicytype() == null || currentPolicy.getRcppolicytype().trim().isEmpty()) {
                Messagebox.show("El tipo de política es requerido",
                    "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            if (currentPolicy.getRcppolicyrules() == null || currentPolicy.getRcppolicyrules().trim().isEmpty()) {
                Messagebox.show("Las reglas de política son requeridas",
                    "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }
            
            // Validar formato JSON (básico)
            try {
                String rules = currentPolicy.getRcppolicyrules().trim();
                if (!rules.startsWith("{") && !rules.startsWith("[")) {
                    // Intentar convertir a JSON válido
                    currentPolicy.setRcppolicyrules("{}");
                }
            } catch (Exception e) {
                log.warn("Error validando formato JSON de reglas, usando vacío");
                currentPolicy.setRcppolicyrules("{}");
            }
            
            if (currentPolicy.getIdxragclientpolicy() == null) {
                currentPolicy = ragClientPolicyService.create(currentPolicy);
                log.info("Política de cliente RAG creada exitosamente: ID={}, nombre={}",
                    currentPolicy.getIdxragclientpolicy(), currentPolicy.getRcppolicyname());
                Messagebox.show("Política de cliente RAG creada exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            } else {
                currentPolicy = ragClientPolicyService.update(currentPolicy);
                log.info("Política de cliente RAG actualizada exitosamente: ID={}, nombre={}",
                    currentPolicy.getIdxragclientpolicy(), currentPolicy.getRcppolicyname());
                Messagebox.show("Política de cliente RAG actualizada exitosamente",
                    "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }
            
            Executions.sendRedirect("/gobierno/rag/rag-client-policies-overview.zul");
            
        } catch (Exception e) {
            log.error("Error al guardar política de cliente RAG", e);
            Messagebox.show("Error al guardar política de cliente RAG: " + e.getMessage(),
                "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    public void cancelEdit() {
        log.debug("Cancelando edición/creación de política de cliente RAG, volviendo a overview");
        Executions.sendRedirect("/gobierno/rag/rag-client-policies-overview.zul");
    }

    /**
     * Libera recursos y limpia referencias para ayudar al GC
     * Se llama automáticamente cuando el ViewModel se destruye
     */
    @Destroy
    public void destroy() {
        log.debug("[Destroy] Liberando recursos del ViewModel {}", this.getClass().getSimpleName());
        
        try {
            // Limpiar política actual
            currentPolicy = null;
            
            // Limpiar Service
            ragClientPolicyService = null;
            
            log.debug("[Destroy] Recursos liberados correctamente");
        } catch (Exception e) {
            log.warn("[Destroy] Error al liberar recursos: {}", e.getMessage());
        }
    }

}

