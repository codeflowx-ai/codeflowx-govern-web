# Patrón de ViewModels para Workflow

## ✅ TaskInboxViewModel - CORREGIDO

El `TaskInboxViewModel` ha sido corregido para seguir el patrón estándar de viewmodels.

## 📋 Patrón Correcto para todos los ViewModels

### 1. Imports necesarios
```java
import javax.sql.DataSource;

import org.enartframework.nocode.dao.IEntityLocal;
import org.enartframework.suinsit.Context;
import org.enartframework.web.zk.page.MasterPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
```

### 2. Anotaciones de clase
```java
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class XxxViewModel extends MasterPage {
    
    private static final long serialVersionUID = 1L;
```

### 3. Variables Spring obligatorias
```java
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
```

### 4. Método initDao() obligatorio
```java
    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }
```

### 5. Método setBeans() obligatorio
```java
    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }
```

### 6. Método @AfterCompose obligatorio
```java
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        log.info("🚀 Inicializando XxxViewModel");
        
        // Aquí va la lógica de inicialización específica del viewmodel
        // ...
    }
```

### 7. NO usar @Init para inicialización
❌ INCORRECTO:
```java
@Init
public void init() {
    // No usar esto
}
```

✅ CORRECTO:
```java
@AfterCompose
public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
    Selectors.wireComponents(view, this, false);
    super.doAfterCompose(view);
    initDao();
    // Lógica aquí
}
```

## 📝 ViewModels del Workflow que necesitan corrección (24 pendientes)

1. ✅ TaskInboxViewModel.java - **CORREGIDO**
2. ❌ AlertResponseViewModel.java
3. ❌ HitlSlaReminderViewModel.java
4. ❌ AgentApprovalHumanOverrideViewModel.java
5. ❌ DatasetReviewReminderViewModel.java
6. ❌ ModelApprovalReminderViewModel.java
7. ❌ BiasUrgentDecisionViewModel.java
8. ❌ PerformanceReviewDecisionViewModel.java
9. ❌ EthicsReviewReminderViewModel.java
10. ❌ DriftReviewDecisionViewModel.java
11. ❌ ComplianceReviewDecisionViewModel.java
12. ❌ RagEvaluationReviewViewModel.java
13. ❌ PromptHumanReviewViewModel.java
14. ❌ PromptApprovalRequestViewModel.java
15. ❌ PerformanceInterventionViewModel.java
16. ❌ ModelEvaluationReviewViewModel.java
17. ❌ LlmEvaluationReviewViewModel.java
18. ❌ ModelApprovalHumanOverrideViewModel.java
19. ❌ EthicsReviewRequestViewModel.java
20. ❌ EthicsMitigationPlanViewModel.java
21. ❌ EthicsCommitteeReviewViewModel.java
22. ❌ DriftAnalysisViewModel.java
23. ❌ ComplianceReviewViewModel.java
24. ❌ BiasReviewViewModel.java
25. ❌ BiasMitigationPlanViewModel.java

## 🔧 Cambios necesarios para cada ViewModel

Para cada ViewModel pendiente:

1. **Cambiar la declaración de clase:**
   - Agregar: `extends MasterPage`
   - Agregar: `@VariableResolver(DelegatingVariableResolver.class)`
   - Agregar: `@Init(superclass = true)`
   - Agregar: `@Getter` y `@Setter` a nivel de clase
   - Agregar: `private static final long serialVersionUID = 1L;`

2. **Agregar variables Spring:**
   - BusinessService
   - IEntityLocal dao
   - Environment
   - GenericApplicationContext
   - Context
   - DataSource

3. **Agregar métodos obligatorios:**
   - `initDao()`
   - `setBeans(Object bean)`

4. **Reemplazar @Init por @AfterCompose:**
   - Quitar cualquier método anotado con solo `@Init`
   - Agregar método `@AfterCompose` con la firma correcta
   - Llamar a `Selectors.wireComponents(view, this, false)`
   - Llamar a `super.doAfterCompose(view)`
   - Llamar a `initDao()`

5. **Actualizar imports:**
   - Agregar imports del patrón estándar
   - Mantener imports específicos del viewmodel (servicios Flowable, etc.)

## ✅ Ejemplo completo: TaskInboxViewModel

Ver el archivo actual de `TaskInboxViewModel.java` como referencia del patrón correcto implementado.

## 📌 Notas importantes

- Todos los viewmodels deben extender `MasterPage`
- Todos deben tener las anotaciones `@VariableResolver` y `@Init(superclass = true)`
- El método de inicialización debe ser `@AfterCompose`, no `@Init`
- Las variables Spring (`BusinessService`, `DataSource`, etc.) son obligatorias
- Los métodos `initDao()` y `setBeans()` son obligatorios

