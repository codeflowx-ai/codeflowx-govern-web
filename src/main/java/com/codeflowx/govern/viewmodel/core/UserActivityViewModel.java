package com.codeflowx.govern.viewmodel.core;
import com.codeflowx.framework.zkoss.BaseFront;

import java.util.List;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import com.codeflowx.govern.entity.views.core.UserActivitySummary;
import com.codeflowx.govern.service.core.UserActivitySummaryService;
import com.codeflowx.govern.service.exception.GovernanceServiceException;

import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.Criteria;
import codeflowx.nocode.persist.Criterias;
import codeflowx.nocode.persist.Evaluation;
import codeflowx.nocode.persist.Operation;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Dashboard de Actividad de Usuarios
 */
@Slf4j
@Getter
@Setter
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class UserActivityViewModel extends BaseFront<UserActivityViewModel>{

    @WireVariable
    private UserActivitySummaryService userActivitySummaryService;
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

    private Long totalItems = 0L;
    private Long activeItems = 0L;
    private Long deployedItems = 0L;
    private Long trainingItems = 0L;
    private Long offlineItems = 0L;
    private java.math.BigDecimal avgScore = java.math.BigDecimal.ZERO;
    private String activityRate = "0";

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        doAfterCompose(view);
        initDao();
        loadActivityData();
    }

    @Command
    @NotifyChange("*")
    public void loadActivityData() {
        try {
            log.debug("Cargando actividad de usuarios");
            List<UserActivitySummary> activityData = userActivitySummaryService.findAll();

            if (activityData != null && !activityData.isEmpty()) {
                UserActivitySummary activity = activityData.get(0);
                log.debug("Actividad cargada - Total users: {}, Active: {}", activity.getTotalItems(), activity.getActiveItems());

                // Mapear datos
                this.totalItems = activity.getTotalItems() != null ? activity.getTotalItems() : 0L;
                this.activeItems = activity.getActiveItems() != null ? activity.getActiveItems() : 0L;
                this.deployedItems = activity.getDeployedItems() != null ? activity.getDeployedItems() : 0L;
                this.trainingItems = activity.getTrainingItems() != null ? activity.getTrainingItems() : 0L;
                this.offlineItems = activity.getOfflineItems() != null ? activity.getOfflineItems() : 0L;
                this.avgScore = activity.getAvgScore() != null ? activity.getAvgScore() : java.math.BigDecimal.ZERO;

                // Calcular tasa de actividad
                if (totalItems != null && totalItems > 0 && activeItems != null) {
                    this.activityRate = String.format("%.1f", (activeItems * 100.0 / totalItems));
                } else {
                    this.activityRate = "0";
                }

                log.info("Actividad de usuarios - Total: {}, Activos: {}, Tasa: {}%",
                         totalItems, activeItems, activityRate);
            }
        } catch (GovernanceServiceException e) {
            log.error("Error cargando actividad de usuarios", e);
        }
    }

    @Command
    @NotifyChange("*")
    public void refresh() {
        log.debug("Refrescando actividad de usuarios");
        loadActivityData();
    }

    public String getActivityLevel() {
        if (activityRate == null || activityRate.isEmpty()) return "Desconocido";
        double rate = Double.parseDouble(activityRate);
        if (rate >= 80) return "Muy Alto";
        if (rate >= 60) return "Alto";
        if (rate >= 40) return "Medio";
        if (rate >= 20) return "Bajo";
        return "Muy Bajo";
    }

    public String getActivityColor() {
        String level = getActivityLevel();
        switch (level) {
            case "Muy Alto": return "success";
            case "Alto": return "primary";
            case "Medio": return "info";
            case "Bajo": return "warning";
            case "Muy Bajo": return "danger";
            default: return "secondary";
        }
    }



    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }
}
