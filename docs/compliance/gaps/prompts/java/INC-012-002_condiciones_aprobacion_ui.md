# PROMPT: INC-012-002 - Condiciones de Aprobación Documentadas Explícitamente en UI

**Incidencia:** INC-012-002  
**Prioridad:** 🔴 CRÍTICA  
**Artículo EU AI Act:** Art. 11, Art. 15  
**Esfuerzo Estimado:** 3 días  
**Tipo:** Java + Frontend (ZUL)

---

## CONTEXTO

Las condiciones que deben cumplirse para aprobación están definidas en reglas Drools (`model-approval-scoring.drl`) pero no son visibles en la interfaz de usuario. Los usuarios no pueden ver qué condiciones deben cumplirse antes de solicitar aprobación, lo que viola los requisitos de transparencia del EU AI Act.

**Ubicación Actual:**
- Reglas Drools en: `model-approval-scoring.drl`
- Pantalla de solicitud: `model-approval-reminder-form.zul`
- ViewModel: `ModelApprovalReminderViewModel.java`

**Problema:**
- Usuarios no saben qué condiciones deben cumplir
- Solicitudes de aprobación rechazadas sin explicación previa
- Falta de transparencia en proceso de aprobación

---

## REQUISITOS

1. **Mostrar condiciones de aprobación** en pantalla de solicitud
2. **Mostrar umbrales y valores actuales** para cada condición
3. **Indicar estado visual** (✓ cumplido / ✗ no cumplido)
4. **Mostrar advertencias** si alguna condición no cumplida
5. **Incluir tooltips** con explicación de cada métrica
6. **Bloquear solicitud** si condiciones críticas no cumplidas

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Modificar ViewModel: `ModelApprovalReminderViewModel.java`

**Ubicación:** `com.codeflowx.govern.viewmodel.bpmn.ModelApprovalReminderViewModel`

Añadir método para obtener condiciones de aprobación:

```java
/**
 * Obtiene las condiciones de aprobación y su estado actual
 * Requisito: INC-012-002 - Art. 11, 15 EU AI Act
 */
public Map<String, ConditionStatus> getApprovalConditions() {
    Map<String, ConditionStatus> conditions = new HashMap<>();
    
    if (model == null || approval == null) {
        return conditions;
    }
    
    // 1. Performance Score >= 0.85
    Double performanceScore = extractScore(approval.getModperformancevalidation(), "score");
    conditions.put("Performance Score", new ConditionStatus(
        ">= 0.85",
        performanceScore,
        performanceScore != null && performanceScore >= 0.85,
        "Métrica de rendimiento del modelo (accuracy, precision, recall, F1). Requisito mínimo: 0.85 según Art. 15 EU AI Act."
    ));
    
    // 2. Bias Score <= 0.10
    Double biasScore = extractScore(approval.getModbiasdetection(), "score");
    conditions.put("Bias Score", new ConditionStatus(
        "<= 0.10",
        biasScore,
        biasScore != null && biasScore <= 0.10,
        "Medida de sesgo detectado en el modelo. Requisito máximo: 0.10 según Art. 10 EU AI Act."
    ));
    
    // 3. Compliance Score >= 0.90
    Double complianceScore = extractScore(approval.getModcompliancecheck(), "score");
    conditions.put("Compliance Score", new ConditionStatus(
        ">= 0.90",
        complianceScore,
        complianceScore != null && complianceScore >= 0.90,
        "Score de cumplimiento normativo y documentación técnica. Requisito mínimo: 0.90 según Art. 11 EU AI Act."
    ));
    
    // 4. Drift Risk < 0.15
    Double driftRisk = approval.getModdriftrisk();
    conditions.put("Drift Risk", new ConditionStatus(
        "< 0.15",
        driftRisk,
        driftRisk != null && driftRisk < 0.15,
        "Riesgo de desviación del modelo en producción. Requisito máximo: 0.15 según Art. 15 EU AI Act."
    ));
    
    // 5. Documentación Técnica Completa
    boolean techDocComplete = model.getModtechnicaldoccomplete() != null && 
        model.getModtechnicaldoccomplete();
    Double techDocScore = model.getModtechnicaldocscore();
    conditions.put("Documentación Técnica", new ConditionStatus(
        "Completa (Score >= 0.90)",
        techDocScore,
        techDocComplete && (techDocScore == null || techDocScore >= 0.90),
        "Documentación técnica completa según Anexo IV EU AI Act. Requisito: completitud >= 0.90 según Art. 11."
    ));
    
    return conditions;
}

/**
 * Extrae score de campo JSONB
 */
private Double extractScore(String jsonb, String key) {
    if (jsonb == null || jsonb.trim().isEmpty()) {
        return null;
    }
    try {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(jsonb, Map.class);
        Object score = map.get(key);
        if (score instanceof Number) {
            return ((Number) score).doubleValue();
        }
    } catch (Exception e) {
        log.warn("Error extrayendo score de JSONB: {}", e.getMessage());
    }
    return null;
}

/**
 * Verifica si todas las condiciones críticas están cumplidas
 */
public boolean areAllConditionsMet() {
    return getApprovalConditions().values().stream()
        .allMatch(ConditionStatus::isMet);
}

/**
 * Obtiene condiciones no cumplidas
 */
public List<String> getUnmetConditions() {
    return getApprovalConditions().entrySet().stream()
        .filter(e -> !e.getValue().isMet())
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());
}

/**
 * Clase para representar estado de una condición
 */
public static class ConditionStatus {
    private String threshold;
    private Double currentValue;
    private boolean met;
    private String description;
    
    public ConditionStatus(String threshold, Double currentValue, boolean met, String description) {
        this.threshold = threshold;
        this.currentValue = currentValue;
        this.met = met;
        this.description = description;
    }
    
    // Getters
    public String getThreshold() { return threshold; }
    public Double getCurrentValue() { return currentValue; }
    public boolean isMet() { return met; }
    public String getDescription() { return description; }
    
    public String getStatusIcon() {
        return met ? "✅" : "❌";
    }
    
    public String getStatusText() {
        if (currentValue == null) {
            return "No evaluado";
        }
        return met ? "Cumplido" : "No cumplido";
    }
    
    public String getFormattedValue() {
        if (currentValue == null) {
            return "N/A";
        }
        return String.format("%.2f", currentValue);
    }
}
```

### 2. Modificar Pantalla ZUL: `model-approval-reminder-form.zul`

**Ubicación:** `/src/main/webapp/console/bpmn/model-approval-reminder-form.zul`

Añadir sección de condiciones antes del formulario de decisión:

```xml
<window title="Recordatorio: Aprobación Pendiente - SLA 3 Días"
        apply="org.zkoss.bind.BindComposer"
        viewModel="@id('vm') @init('com.codeflowx.govern.viewmodel.bpmn.ModelApprovalReminderViewModel', taskId=arg.taskId)">
    
    <vlayout spacing="15" hflex="1" vflex="1">
        
        <!-- Información del Modelo -->
        <groupbox title="Información del Modelo" mold="3d">
            <grid>
                <rows>
                    <row>
                        <cell label="Modelo:"/>
                        <cell label="${vm.modelName}"/>
                    </row>
                    <row>
                        <cell label="Versión:"/>
                        <cell label="${vm.modelVersion}"/>
                    </row>
                </rows>
            </grid>
        </groupbox>
        
        <!-- NUEVA SECCIÓN: Condiciones de Aprobación -->
        <groupbox title="Condiciones para Aprobación" mold="3d">
            <vlayout spacing="10">
                
                <!-- Resumen -->
                <hlayout>
                    <label value="Estado General:" 
                           style="font-weight: bold;"/>
                    <label value="${vm.areAllConditionsMet() ? '✅ Todas las condiciones cumplidas' : '❌ Faltan condiciones por cumplir'}"
                           style="font-weight: bold; color: ${vm.areAllConditionsMet() ? 'green' : 'red'};"/>
                </hlayout>
                
                <!-- Lista de Condiciones -->
                <listbox model="@bind(vm.approvalConditions)" 
                         vflex="1" 
                         checkmark="false"
                         emptyMessage="No hay condiciones definidas">
                    <listhead>
                        <listheader label="Estado" width="80px"/>
                        <listheader label="Condición" hflex="2"/>
                        <listheader label="Umbral" hflex="1"/>
                        <listheader label="Valor Actual" hflex="1"/>
                        <listheader label="Estado" hflex="1"/>
                    </listhead>
                    <template name="model">
                        <listitem>
                            <listcell>
                                <label value="${each.value.statusIcon}" 
                                       style="font-size: 18px;"/>
                            </listcell>
                            <listcell>
                                <label value="${each.key}"/>
                                <tooltip label="${each.value.description}"/>
                            </listcell>
                            <listcell>
                                <label value="${each.value.threshold}"/>
                            </listcell>
                            <listcell>
                                <label value="${each.value.formattedValue}"/>
                            </listcell>
                            <listcell>
                                <label value="${each.value.statusText}" 
                                       style="color: ${each.value.met ? 'green' : 'red'};"/>
                            </listcell>
                        </listitem>
                    </template>
                </listbox>
                
                <!-- Advertencia si condiciones no cumplidas -->
                <div if="${not vm.areAllConditionsMet()}" 
                     style="background-color: #fff3cd; padding: 10px; border: 1px solid #ffc107; border-radius: 4px;">
                    <label value="⚠️ Advertencia: Las siguientes condiciones no están cumplidas:" 
                           style="font-weight: bold;"/>
                    <listbox model="@bind(vm.unmetConditions)" 
                             style="margin-top: 5px;">
                        <template name="model">
                            <listitem>
                                <listcell label="${each}"/>
                            </listitem>
                        </template>
                    </listbox>
                    <label value="Por favor, corrija estas condiciones antes de solicitar aprobación." 
                           style="margin-top: 5px; font-style: italic;"/>
                </div>
                
            </vlayout>
        </groupbox>
        
        <!-- Formulario de Decisión (existente) -->
        <groupbox title="Decisión" mold="3d">
            <vlayout>
                <radiogroup selectedItem="@bind(vm.selectedDecision)">
                    <radio value="APPROVE_NOW" label="✅ Aprobar Inmediatamente"/>
                    <radio value="ESCALATE" label="🚨 Escalar a Manager/Director"/>
                    <radio value="REQUEST_INFO" label="📋 Solicitar Más Información"/>
                </radiogroup>
                
                <textbox rows="3" 
                         value="@bind(vm.notes)" 
                         placeholder="Notas adicionales..."
                         style="margin-top: 10px;"/>
                
                <hlayout style="margin-top: 10px;">
                    <button label="Enviar" 
                            onClick="@command('submitDecision')"
                            disabled="@bind(not vm.areAllConditionsMet() and vm.selectedDecision == 'APPROVE_NOW')"/>
                    <button label="Cancelar" 
                            onClick="window.detach()"/>
                </hlayout>
            </vlayout>
        </groupbox>
        
    </vlayout>
    
</window>
```

### 3. Modificar Método de Envío

Añadir validación en método `submitDecision()`:

```java
@Command
public void submitDecision() {
    // Si intenta aprobar sin cumplir condiciones, bloquear
    if ("APPROVE_NOW".equals(selectedDecision) && !areAllConditionsMet()) {
        List<String> unmet = getUnmetConditions();
        String message = "No se puede aprobar el modelo. Las siguientes condiciones no están cumplidas:\n\n" +
            String.join("\n", unmet) +
            "\n\nPor favor, corrija estas condiciones antes de aprobar.";
        
        Messagebox.show(
            message,
            "Aprobación Bloqueada",
            Messagebox.OK,
            Messagebox.ERROR
        );
        return;
    }
    
    // Continuar con lógica existente...
    // ...
}
```

### 4. Añadir Imports Necesarios

```java
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;
```

---

## VALIDACIONES ADICIONALES RECOMENDADAS

1. **Mostrar tiempo restante de SLA** para cada evaluación pendiente
2. **Permitir solicitar re-evaluación** directamente desde la pantalla
3. **Mostrar historial de cambios** en cada métrica
4. **Añadir enlaces a documentación** de cada métrica

---

## PRUEBAS REQUERIDAS

1. **Test 1:** Abrir pantalla con modelo que cumple todas las condiciones → Debe mostrar "✅ Todas las condiciones cumplidas"
2. **Test 2:** Abrir pantalla con modelo que NO cumple condiciones → Debe mostrar advertencia y lista de condiciones no cumplidas
3. **Test 3:** Intentar aprobar sin cumplir condiciones → Debe bloquear y mostrar mensaje de error
4. **Test 4:** Verificar tooltips muestran descripción correcta de cada métrica
5. **Test 5:** Verificar que valores actuales se muestran correctamente

---

## REFERENCIAS

- **Art. 11 EU AI Act:** Documentación Técnica
- **Art. 15 EU AI Act:** Precisión y Robustez
- **Reglas Drools:** `model-approval-scoring.drl`
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_012_PUESTA_PRODUCCION.md#inc-012-002`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_012_PUESTA_PRODUCCION.md`

---

## NOTAS DE IMPLEMENTACIÓN

- Ajustar extracción de scores según estructura real de campos JSONB
- Considerar hacer condiciones configurables desde base de datos
- Añadir validación en backend también (no solo UI)
- Considerar mostrar condiciones en otras pantallas relacionadas (dashboard, detalle modelo)

---

**Estado:** ✅ COMPLETADO

