package com.codeflowx.govern.workflow.viewmodels;

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
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.admin.Ssoractividad;

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ViewModel: Ethics Review Request (Inicio de Proceso)
 * 
 * BPMN Process: ethics-review-v1
 * Type: Formulario de inicio de proceso (NO es User Task)
 * Access: Directamente desde menú o dashboards
 * 
 * Funcionalidad:
 * - Formulario para solicitar revisión ética de un sistema de IA
 * - Capturar información del sistema (nombre, tipo, impacto)
 * - Describir preocupaciones éticas específicas
 * - Iniciar proceso BPMN de revisión ética
 * - Registrar solicitud en base de datos
 * 
 * Input Variables (formulario usuario):
 * - systemId: Long - ID del sistema (opcional, puede ser nuevo)
 * - systemName: String - Nombre del sistema
 * - systemType: String - Tipo de sistema IA
 * - impactLevel: String - Nivel de impacto estimado
 * - estimatedUsers: String - Usuarios afectados
 * - ethicalConcerns: String - Preocupaciones éticas descritas
 * - requestReason: String - Razón de la solicitud
 * 
 * Output Variables (al iniciar proceso):
 * - Inicia proceso: ethics-review-v1
 * - processInstanceId: String - ID del proceso iniciado
 * - requestedBy: String - Usuario solicitante
 * - requestDate: Timestamp
 * 
 * Modo MOCK:
 * - URL: /workflow/ethics-review-request.zul?mock=true
 * - Datos simulados: Formulario prellenado con datos de ejemplo
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class EthicsReviewRequestViewModel extends MasterPage {

    private static final long serialVersionUID = 1L;

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

    @WireVariable
    private RuntimeService runtimeService;

    @Getter
    private boolean mockMode = false;

    private String systemName;
    private String systemType;
    private String impactLevel;
    private String estimatedUsers;
    private String ethicalConcerns;
    private String requestReason;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
     // Detectar mock mode desde parámetros URL
        if(System.getenv("MOCK_MODE")!=null) {
        	mockMode = Boolean.parseBoolean(System.getenv("MOCK_MODE").toString());
        }
       
     
        
        log.info("🚀 Inicializando EthicsReviewRequestViewModel - MOCK MODE: {}", mockMode);
        
        if (mockMode) {
            loadMockData();
        }
    }
    
    private void loadMockData() {
        systemName = "Sistema IA Recursos Humanos - Selección Automática";
        systemType = "PREDICTIVE";
        impactLevel = "HIGH";
        estimatedUsers = "500 candidatos/mes";
        ethicalConcerns = "Posible sesgo en selección de candidatos por género y edad. " +
                         "Falta de transparencia en criterios de evaluación.";
        requestReason = "Cumplimiento de normativa interna de RRHH y regulación EU AI Act";
        log.info("✅ Datos MOCK cargados - Formulario prellenado");
    }

    @Command
    public void submitRequest() {
        if (mockMode) {
            Messagebox.show("✅ DEMO: Solicitud de Ethics Review enviada\n\nSistema: " + systemName + "\nTipo: " + systemType + "\n\n(Modo MOCK)", 
                "Demo - Solicitud Enviada", Messagebox.OK, Messagebox.INFORMATION,
                e -> Executions.sendRedirect("/workflow/task-inbox.zul?mock=true"));
            return;
        }
        
        try {
            Map<String, Object> vars = new HashMap<>();
            vars.put("systemName", systemName);
            vars.put("systemType", systemType);
            vars.put("impactLevel", impactLevel);
            vars.put("estimatedUsers", estimatedUsers);
            vars.put("ethicalConcerns", ethicalConcerns);
            vars.put("requestReason", requestReason);
            vars.put("requestedBy", getUser().getUsername());
            vars.put("requestDate", new java.sql.Timestamp(System.currentTimeMillis()));

            ProcessInstance process = runtimeService.startProcessInstanceByKey("ethics-review-v1", vars);
            
            logActivity("SOLICITUD", "ETHETHICSREVIEWS", null, "Solicitud ethics review: " + systemName);

            Messagebox.show("Solicitud enviada - Proceso ID: " + process.getId(), "Éxito", 
                Messagebox.OK, Messagebox.INFORMATION);

        } catch (Exception e) {
            log.error("Error enviando solicitud", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    public void cancel() {
        Executions.sendRedirect(mockMode ? "/workflow/task-inbox.zul?mock=true" : "/console/govern/ethics-dashboard.zul");
    }
    
    private void logActivity(String action, String model, Long pk, String mensaje) {
        try {
            Ssoractividad activityLog = new Ssoractividad();
            activityLog.setUsername(getUser().getUsername());
            activityLog.setAccion(action);
            activityLog.setAlta(new java.sql.Timestamp(System.currentTimeMillis()));
            activityLog.setModulo(model);
            activityLog.setIdtupla(pk != null ? pk.intValue() : 0);
            activityLog.setAplicacion(ctxBean.getApplicationName());
            activityLog.setValuetupla(mensaje);
            businessService.save(activityLog);
        } catch (Exception e) {
            log.error("Error al auditar acción: {} en módulo: {}", action, model, e);
        }
    }
    
    @org.zkoss.bind.annotation.Destroy
    public void destroy() {
        businessService = null;
        runtimeService = null;
    }
}
