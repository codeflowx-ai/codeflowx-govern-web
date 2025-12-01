package com.codeflowx.govern.viewmodel.compliance;

import com.codeflowx.framework.zkoss.BaseFront;
import com.codeflowx.govern.business.compliance.PmmFrequencyService;
import com.codeflowx.govern.business.compliance.PostMarketMonitoringPlanService;
import com.codeflowx.govern.business.exception.BussinessException;
import com.codeflowx.govern.entity.compliance.PostMarketMonitoringPlan;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.util.Arrays;
import java.util.List;

/**
 * ViewModel para configuración de frecuencias de monitoreo PMM.
 * Art. 72 EU AI Act - Post-market monitoring
 *
 * Permite configurar frecuencias personalizadas de monitoreo por proyecto.
 */
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class PmmFrequencyViewModel extends BaseFront<PmmFrequencyViewModel> {

    private static final long serialVersionUID = 1L;

    @WireVariable
    private PmmFrequencyService frequencyService;

    @WireVariable
    private PostMarketMonitoringPlanService planService;

    private PostMarketMonitoringPlan plan;
    private String selectedFrequency;
    private Integer customHours;

    // Lista de frecuencias disponibles
    private List<String> availableFrequencies = Arrays.asList(
        "HOURLY", "DAILY", "WEEKLY", "MONTHLY", "CUSTOM"
    );

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view,
                             @BindingParam("planId") Long planId) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);

        if (planId != null) {
            loadPlan(planId);
        }
    }

    @Command
    public void loadPlan(@BindingParam("planId") Long planId) {
        try {
            plan = planService.findById(planId);
            if (plan != null) {
                selectedFrequency = plan.getPmmmonitoringfrequency();
                customHours = plan.getPmmcustomfrequencyhours();
            } else {
                org.zkoss.zk.ui.util.Clients.showNotification(
                    "Plan PMM no encontrado", "error", null, null, 5000);
            }
        } catch (BussinessException e) {
            log.error("Error cargando plan PMM", e);
            org.zkoss.zk.ui.util.Clients.showNotification(
                "Error cargando plan: " + e.getMessage(), "error", null, null, 5000);
        }
    }

    @Command
    @NotifyChange("customHours")
    public void saveFrequency() {
        if (plan == null) {
            org.zkoss.zk.ui.util.Clients.showNotification(
                "No hay plan seleccionado", "error", null, null, 5000);
            return;
        }

        try {
            frequencyService.updateMonitoringFrequency(
                plan.getIdxpmmplan(), selectedFrequency, customHours);

            org.zkoss.zk.ui.util.Clients.showNotification(
                "Frecuencia actualizada exitosamente", "info", null, null, 3000);

            // Recargar plan actualizado
            loadPlan(plan.getIdxpmmplan());

        } catch (BussinessException e) {
            log.error("Error actualizando frecuencia", e);
            org.zkoss.zk.ui.util.Clients.showNotification(
                "Error: " + e.getMessage(), "error", null, null, 5000);
        }
    }

    @Command
    @NotifyChange("customHours")
    public void onFrequencyChange() {
        // Si no es CUSTOM, limpiar customHours
        if (!"CUSTOM".equals(selectedFrequency)) {
            customHours = null;
        } else if (customHours == null) {
            // Si es CUSTOM y no tiene valor, establecer por defecto 24 horas
            customHours = 24;
        }
    }

    /**
     * Verifica si la frecuencia seleccionada requiere horas personalizadas
     */
    public boolean isCustomFrequency() {
        return "CUSTOM".equals(selectedFrequency);
    }
}
