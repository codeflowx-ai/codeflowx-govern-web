# PROMPT: INC-012-005 - SLA Tracking Visible en Dashboard Principal

**Incidencia:** INC-012-005  
**Prioridad:** 🟡 MEDIA  
**Artículo EU AI Act:** Art. 14 EU AI Act  
**Esfuerzo Estimado:** 2 días  
**Tipo:** Java + Frontend (ZUL)

---

## CONTEXTO

Los SLA de aprobación (24h ML Engineer, 72h Governance) no son visibles en el dashboard principal. Los usuarios no pueden ver fácilmente qué aprobaciones están próximas a exceder SLA, lo que puede llevar a violaciones de SLA sin notificación visible.

**Ubicación Actual:**
- Dashboard: `console/platform/models/approval/overview.zul`
- ViewModel: `ModelApprovalOverviewViewModel.java`
- SLA definidos en workflow BPMN pero no visibles en UI

**Problema:**
- Riesgo de exceder SLA sin notificación visible
- Falta de visibilidad de urgencia
- No hay alertas visuales para aprobaciones próximas a vencer

---

## REQUISITOS

1. **Añadir columna "SLA Restante"** en dashboard de aprobaciones
2. **Mostrar alertas visuales** si SLA < 24h
3. **Color coding:**
   - Verde: SLA > 50% restante
   - Amarillo: SLA 25-50% restante
   - Rojo: SLA < 25% restante
4. **Calcular tiempo restante** basado en SLA definido
5. **Mostrar tiempo en formato legible** (ej: "18h 30m restantes")

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Modificar ViewModel: `ModelApprovalOverviewViewModel.java`

**Ubicación:** `com.codeflowx.govern.viewmodel.models.approval.ModelApprovalOverviewViewModel`

Añadir métodos para calcular SLA:

```java
package com.codeflowx.govern.viewmodel.models.approval;

import com.codeflowx.govern.entity.models.ModelApproval;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * ViewModel para dashboard de aprobaciones
 * Requisito: INC-012-005 - Art. 14 EU AI Act (SLA Tracking)
 */
public class ModelApprovalOverviewViewModel {
    
    // ... código existente ...
    
    /**
     * Calcula tiempo restante de SLA para ML Engineer Review
     * SLA: 24 horas desde creación de aprobación
     */
    public String getMlEngineerSlaRemaining(ModelApproval approval) {
        if (approval == null || approval.getModcreatedat() == null) {
            return "N/A";
        }
        
        // Verificar si ya fue aprobado
        if (approval.getModmlengineerapproval() != null && 
            "APPROVED".equals(approval.getModmlengineerapproval())) {
            return "✅ Completado";
        }
        
        LocalDateTime createdAt = convertToLocalDateTime(approval.getModcreatedat());
        LocalDateTime deadline = createdAt.plusHours(24); // SLA: 24 horas
        LocalDateTime now = LocalDateTime.now();
        
        if (now.isAfter(deadline)) {
            Duration overdue = Duration.between(deadline, now);
            return "❌ Vencido hace " + formatDuration(overdue);
        }
        
        Duration remaining = Duration.between(now, deadline);
        return formatDuration(remaining) + " restantes";
    }
    
    /**
     * Calcula tiempo restante de SLA para Governance Review
     * SLA: 72 horas desde creación de aprobación
     */
    public String getGovernanceSlaRemaining(ModelApproval approval) {
        if (approval == null || approval.getModcreatedat() == null) {
            return "N/A";
        }
        
        // Verificar si ya fue aprobado
        if (approval.getModapprovalstatus() != null && 
            ("APPROVED".equals(approval.getModapprovalstatus()) || 
             "CONDITIONAL_APPROVAL".equals(approval.getModapprovalstatus()))) {
            return "✅ Completado";
        }
        
        LocalDateTime createdAt = convertToLocalDateTime(approval.getModcreatedat());
        LocalDateTime deadline = createdAt.plusHours(72); // SLA: 72 horas
        LocalDateTime now = LocalDateTime.now();
        
        if (now.isAfter(deadline)) {
            Duration overdue = Duration.between(deadline, now);
            return "❌ Vencido hace " + formatDuration(overdue);
        }
        
        Duration remaining = Duration.between(now, deadline);
        return formatDuration(remaining) + " restantes";
    }
    
    /**
     * Obtiene color según porcentaje de SLA restante
     */
    public String getSlaColor(ModelApproval approval, String slaType) {
        if (approval == null || approval.getModcreatedat() == null) {
            return "gray";
        }
        
        LocalDateTime createdAt = convertToLocalDateTime(approval.getModcreatedat());
        long totalHours = "ML_ENGINEER".equals(slaType) ? 24 : 72;
        LocalDateTime deadline = createdAt.plusHours(totalHours);
        LocalDateTime now = LocalDateTime.now();
        
        if (now.isAfter(deadline)) {
            return "red"; // Vencido
        }
        
        Duration remaining = Duration.between(now, deadline);
        long remainingHours = remaining.toHours();
        double percentage = (double) remainingHours / totalHours;
        
        if (percentage > 0.5) {
            return "green"; // > 50% restante
        } else if (percentage > 0.25) {
            return "orange"; // 25-50% restante
        } else {
            return "red"; // < 25% restante
        }
    }
    
    /**
     * Verifica si SLA está próximo a vencer (< 24h restantes)
     */
    public boolean isSlaUrgent(ModelApproval approval, String slaType) {
        if (approval == null || approval.getModcreatedat() == null) {
            return false;
        }
        
        LocalDateTime createdAt = convertToLocalDateTime(approval.getModcreatedat());
        long totalHours = "ML_ENGINEER".equals(slaType) ? 24 : 72;
        LocalDateTime deadline = createdAt.plusHours(totalHours);
        LocalDateTime now = LocalDateTime.now();
        
        if (now.isAfter(deadline)) {
            return true; // Ya vencido
        }
        
        Duration remaining = Duration.between(now, deadline);
        return remaining.toHours() < 24; // Menos de 24h restantes
    }
    
    /**
     * Formatea duración en formato legible
     */
    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        } else {
            return minutes + "m";
        }
    }
    
    /**
     * Convierte Date a LocalDateTime
     */
    private LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
    }
}
```

### 2. Modificar Pantalla ZUL: `overview.zul`

**Ubicación:** `/src/main/webapp/console/platform/models/approval/overview.zul`

Añadir columnas de SLA en listbox:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<zk xmlns="http://www.zkoss.org/2005/zul"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.zkoss.org/2005/zul http://www.zkoss.org/2005/zul/zul.xsd">
    
    <window title="Dashboard de Aprobaciones" 
            apply="org.zkoss.bind.BindComposer"
            viewModel="@id('vm') @init('com.codeflowx.govern.viewmodel.models.approval.ModelApprovalOverviewViewModel')">
        
        <vlayout hflex="1" vflex="1" spacing="10">
            
            <!-- Filtros (existente) -->
            <!-- ... -->
            
            <!-- Lista de Aprobaciones -->
            <listbox model="@bind(vm.approvals)" vflex="1" checkmark="false">
                <listhead>
                    <listheader label="ID" width="80px"/>
                    <listheader label="Modelo" hflex="2"/>
                    <listheader label="Estado" hflex="1"/>
                    <listheader label="Creado" hflex="1"/>
                    <!-- NUEVAS COLUMNAS SLA -->
                    <listheader label="SLA ML Engineer" hflex="1"/>
                    <listheader label="SLA Governance" hflex="1"/>
                    <listheader label="Acciones" hflex="1"/>
                </listhead>
                <template name="model">
                    <listitem>
                        <listcell label="${each.idxapproval}"/>
                        <listcell label="${each.model.modname}"/>
                        <listcell label="${each.modapprovalstatus}"/>
                        <listcell label="${each.modcreatedat}"/>
                        
                        <!-- Columna SLA ML Engineer -->
                        <listcell>
                            <label value="${vm.mlEngineerSlaRemaining(each)}"
                                   style="color: ${vm.slaColor(each, 'ML_ENGINEER')}; 
                                          font-weight: ${vm.slaUrgent(each, 'ML_ENGINEER') ? 'bold' : 'normal'};"/>
                            <div if="${vm.slaUrgent(each, 'ML_ENGINEER')}" 
                                 style="background-color: #ffebee; padding: 2px; border-radius: 3px;">
                                <label value="⚠️ URGENTE" style="font-size: 10px; color: red;"/>
                            </div>
                        </listcell>
                        
                        <!-- Columna SLA Governance -->
                        <listcell>
                            <label value="${vm.governanceSlaRemaining(each)}"
                                   style="color: ${vm.slaColor(each, 'GOVERNANCE')}; 
                                          font-weight: ${vm.slaUrgent(each, 'GOVERNANCE') ? 'bold' : 'normal'};"/>
                            <div if="${vm.slaUrgent(each, 'GOVERNANCE')}" 
                                 style="background-color: #ffebee; padding: 2px; border-radius: 3px;">
                                <label value="⚠️ URGENTE" style="font-size: 10px; color: red;"/>
                            </div>
                        </listcell>
                        
                        <listcell>
                            <button label="Ver Detalle" onClick="@command('viewDetail', approval=each)"/>
                        </listcell>
                    </listitem>
                </template>
            </listbox>
            
            <!-- Panel de Alertas SLA -->
            <groupbox title="⚠️ Alertas SLA" mold="3d" 
                     visible="@bind(not empty vm.urgentApprovals)">
                <listbox model="@bind(vm.urgentApprovals)" 
                         style="max-height: 200px;">
                    <listhead>
                        <listheader label="Modelo"/>
                        <listheader label="SLA"/>
                        <listheader label="Tiempo Restante"/>
                    </listhead>
                    <template name="model">
                        <listitem style="background-color: #fff3cd;">
                            <listcell label="${each.model.modname}"/>
                            <listcell label="${each.slaType}"/>
                            <listcell label="${each.remainingTime}" 
                                     style="color: red; font-weight: bold;"/>
                        </listitem>
                    </template>
                </listbox>
            </groupbox>
            
        </vlayout>
        
    </window>
    
</zk>
```

### 3. Añadir Método para Obtener Aprobaciones Urgentes

En ViewModel:

```java
/**
 * Obtiene aprobaciones con SLA urgente (< 24h restantes)
 */
public List<ApprovalSlaAlert> getUrgentApprovals() {
    return approvals.stream()
        .filter(a -> isSlaUrgent(a, "ML_ENGINEER") || isSlaUrgent(a, "GOVERNANCE"))
        .map(a -> {
            ApprovalSlaAlert alert = new ApprovalSlaAlert();
            alert.setModel(a.getModel());
            alert.setSlaType(isSlaUrgent(a, "ML_ENGINEER") ? "ML Engineer" : "Governance");
            alert.setRemainingTime(isSlaUrgent(a, "ML_ENGINEER") ? 
                getMlEngineerSlaRemaining(a) : getGovernanceSlaRemaining(a));
            return alert;
        })
        .collect(Collectors.toList());
}

/**
 * Clase para alertas SLA
 */
public static class ApprovalSlaAlert {
    private Model model;
    private String slaType;
    private String remainingTime;
    
    // Getters y Setters
    // ...
}
```

---

## VALIDACIONES ADICIONALES RECOMENDADAS

1. **Añadir notificaciones push** cuando SLA está próximo a vencer
2. **Enviar email** a responsables cuando SLA está próximo a vencer
3. **Mostrar estadísticas** de cumplimiento de SLA en dashboard
4. **Añadir filtro** para mostrar solo aprobaciones urgentes

---

## PRUEBAS REQUERIDAS

1. **Test 1:** Verificar que columna SLA muestra tiempo correcto
2. **Test 2:** Verificar que color cambia según porcentaje restante
3. **Test 3:** Verificar que muestra "URGENTE" cuando < 24h restantes
4. **Test 4:** Verificar que muestra "Vencido" cuando SLA expiró
5. **Test 5:** Verificar que panel de alertas muestra aprobaciones urgentes
6. **Test 6:** Verificar cálculo correcto para ML Engineer (24h) y Governance (72h)

---

## REFERENCIAS

- **Art. 14 EU AI Act:** Supervisión Humana
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_012_PUESTA_PRODUCCION.md#inc-012-005`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_012_PUESTA_PRODUCCION.md`

---

## NOTAS DE IMPLEMENTACIÓN

- Ajustar zona horaria según configuración del sistema
- Considerar hacer SLA configurables desde base de datos
- Añadir actualización automática cada minuto para tiempo restante
- Considerar usar WebSocket para actualizaciones en tiempo real

---

**Estado:** ✅ COMPLETADO

