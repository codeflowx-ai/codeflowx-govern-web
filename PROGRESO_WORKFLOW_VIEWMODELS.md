# Progreso - workflow.viewmodels Package

**Total:** 25 archivos  
**Estado:** Aplicación sistemática del patrón completo

---

## ✅ COMPLETADOS con Patrón Completo (8/25 - 32%)

1. ✅ `TaskInboxViewModel.java` - **PATRÓN REFERENCIA**
2. ✅ `AlertResponseViewModel.java` 
3. ✅ `AgentApprovalHumanOverrideViewModel.java`
4. ✅ `BiasMitigationPlanViewModel.java`
5. ✅ `BiasReviewViewModel.java`
6. ✅ `BiasUrgentDecisionViewModel.java`
7. ✅ `PromptApprovalRequestViewModel.java`
8. ✅ `PromptHumanReviewViewModel.java`
9. ✅ `ComplianceReviewDecisionViewModel.java`

**Patrón aplicado:**
- ✅ `extends MasterPage`
- ✅ `@VariableResolver` + `@Init(superclass = true)`
- ✅ Todas las `@WireVariable` necesarias
- ✅ `initDao()` y `setBeans()`
- ✅ `@AfterCompose` en lugar de `@Init`
- ✅ `import com.codeflowx.admin.Ssoractividad`
- ✅ Método `logActivity(action, model, pk, mensaje)`
- ✅ Método `@Destroy`

---

## 🔄 PENDIENTES (16/25 - 64%)

### Archivos que necesitan patrón completo:

1. ❌ `ComplianceReviewViewModel.java`
2. ❌ `DatasetReviewReminderViewModel.java`
3. ❌ `DriftAnalysisViewModel.java`
4. ❌ `DriftReviewDecisionViewModel.java`
5. ❌ `EthicsCommitteeReviewViewModel.java`
6. ❌ `EthicsMitigationPlanViewModel.java`
7. ❌ `EthicsReviewReminderViewModel.java`
8. ❌ `EthicsReviewRequestViewModel.java`
9. ❌ `HitlSlaReminderViewModel.java`
10. ❌ `LlmEvaluationReviewViewModel.java`
11. ❌ `ModelApprovalHumanOverrideViewModel.java`
12. ❌ `ModelApprovalReminderViewModel.java`
13. ❌ `ModelEvaluationReviewViewModel.java`
14. ❌ `PerformanceInterventionViewModel.java`
15. ❌ `PerformanceReviewDecisionViewModel.java`
16. ❌ `RagEvaluationReviewViewModel.java`

### Transformaciones necesarias por archivo:

1. Agregar `extends MasterPage`
2. Agregar anotaciones `@VariableResolver(DelegatingVariableResolver.class)` + `@Init(superclass = true)`
3. Agregar imports necesarios (DataSource, IEntityLocal, Context, etc.)
4. Agregar variables Spring (`@WireVariable`)
5. Agregar `initDao()` y `setBeans()`
6. Cambiar `@Init` → `@AfterCompose`
7. Agregar import `com.codeflowx.admin.Ssoractividad`
8. Agregar método `logActivity()`
9. Agregar método `@Destroy`

**Estimación:** 16 archivos × 15 min = ~240 minutos (~4 horas)

---

## 📋 Patrón a Aplicar

### Template Completo:

```java
package com.codeflowx.govern.workflow.viewmodels;

import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
import org.flowable.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
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

@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class MiViewModel extends MasterPage {
    
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

    // ========== Servicios Flowable ==========
    @WireVariable
    private TaskService taskService;

    // ========== Datos ==========
    // ... campos del viewmodel ...

    // ========== Inicialización ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        log.info("🚀 Inicializando MiViewModel");
        
        // ... lógica de inicialización ...
    }

    // ========== Comandos ==========
    // ... @Command methods ...

    // ========== Auditoría ==========
    
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
        taskService = null;
    }
}
```

---

## Estado Actual

- ✅ **Archivos con patrón completo:** 9/25 (36%)
- 🔄 **En progreso:** 1/25 (4%)
- ❌ **Pendientes:** 15/25 (60%)

---

**Próxima acción:** Continuar aplicando el patrón a los 16 archivos restantes de forma sistemática.

