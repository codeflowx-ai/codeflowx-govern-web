# 📦 REFERENCIA RÁPIDA ARQUITECTURA MVP STARTER

**Fecha:** Octubre 30, 2025  
**Propósito:** Guía rápida para chats IA generando código

---

## 🏗️ STACK TECNOLÓGICO

```
Frontend:  ZKoss Framework 10.x (MVVM, Java-based UI)
Backend:   Spring Boot 3.x + BusinessService (framework EnArt)
ORM:       JPA/Hibernate con framework EnArt
Database:  PostgreSQL
Build:     Maven
```

---

## 📁 ESTRUCTURA DE DIRECTORIOS

### **Entidades JPA:**
```
Ubicación: /mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service/nocode.service.entitys/
Paquete:   com.codeflowx.govern.entity.[module]/[EntityName].java

Ejemplos:
  com.codeflowx.govern.entity.models.Model
  com.codeflowx.govern.entity.models.ModelApproval
  com.codeflowx.govern.entity.evaluation.ModelBiasAnalysis
```

### **ViewModels:**
```
Ubicación: suinsit.nova.web/src/main/java/com/codeflowx/platform/viewmodel/
Paquete:   com.codeflowx.platform.viewmodel.[module]/[EntityName]OverviewViewModel.java

Ejemplos:
  com.codeflowx.platform.viewmodel.models.ModelOverviewViewModel
  com.codeflowx.platform.viewmodel.models.ModelDetailViewModel
  com.codeflowx.platform.viewmodel.governance.ComplianceAssessmentOverviewViewModel
```

### **ZUL Files:**
```
Ubicación: suinsit.nova.web/src/main/webapp/console/platform/
Path:      [module]/[entity]/[action].zul

Ejemplos:
  platform/models/overview/page.zul
  platform/models/create/page.zul
  platform/models/bias-analysis/overview.zul
  platform/governance/compliance/page.zul
```

---

## 🗃️ NAMING CONVENTIONS

### **Tablas PostgreSQL:**
```
Formato:   PREFIX_ENTITYNAME
Ejemplos:
  MODMODELS                - Tabla de modelos
  MODMODELAPPROVALS        - Aprobaciones de modelos
  MODMODELBIASANALYSES     - Análisis de sesgo
  MODMODELPERFORMANCES     - Métricas de performance
```

### **Primary Keys:**
```
Formato:   IDX + nombre
Ejemplos:
  IDXMODEL               (Long)
  IDXMODELAPPROVAL       (Long)
  IDXMODELBIASANALYSIS   (Long)
```

### **Campos de Tablas:**
```
Formato:   PREFIX + nombre
Ejemplos:
  MODNAME         - Nombre del modelo
  MODTYPE         - Tipo de modelo
  MODSTATUS       - Estado del modelo
  MODFRAMEWORK    - Framework usado
  MODRISKLEVEL    - Nivel de riesgo
```

---

## ✅ ENTIDADES JPA YA EXISTENTES

### **Model** ✅
```java
Paquete:  com.codeflowx.govern.entity.models.Model
Tabla:    MODMODELS
PK:       IDXMODEL (Long)

Campos principales:
  MODNAME          String(255)  - Nombre modelo
  MODDESCRIPTION   CLOB         - Descripción
  MODTYPE          String       - Tipo (Classification, LLM, CV, NLP, etc.)
  MODFRAMEWORK     String       - Framework (TensorFlow, PyTorch, etc.)
  MODVERSION       String(50)   - Versión
  MODSTATUS        String       - Estado (DRAFT, IN_REVIEW, APPROVED, REJECTED, PRODUCTION)
  MODRISKLEVEL     String       - Riesgo (LOW, MEDIUM, HIGH)
```

### **ModelApproval** ✅
```java
Paquete:  com.codeflowx.govern.entity.models.ModelApproval
Tabla:    MODMODELAPPROVALS
PK:       IDXMODELAPPROVAL (Long)

Campos principales:
  MODAPPROVALTYPE          String(50)  - NEW_MODEL, VERSION_UPDATE, REDEPLOYMENT
  MODAPPROVALSTATUS        String(50)  - PENDING, UNDER_REVIEW, APPROVED, REJECTED, CONDITIONAL
  MODTARGETENVIRONMENT     String(50)  - DEVELOPMENT, STAGING, PRODUCTION
  MODREQUESTREASON         CLOB        - Razón solicitud
  MODAPPROVALCOMMENTS      CLOB        - Comentarios aprobador
  MODREJECTIONREASON       CLOB        - Razón rechazo
  MODREQUESTEDDATE         Timestamp   - Fecha solicitud
  MODAPPROVEDREJECTEDDATE  Timestamp   - Fecha decisión
  
  FK: ManyToOne → Model
```

### **ModelBiasAnalysis** ✅
```java
Paquete:  com.codeflowx.govern.entity.evaluation.ModelBiasAnalysis
Tabla:    MODMODELBIASANALYSES
PK:       IDXMODELBIASANALYSIS (Long)

Campos principales:
  MODBIASTYPE         String  - Tipo de sesgo detectado
  MODSEVERITY         String  - NO_BIAS, LOW, MODERATE, HIGH, CRITICAL
  MODBIASDESCRIPTION  CLOB    - Descripción detallada
  MODBIASMETRICS      CLOB    - Métricas en JSON
  MODANALYSISDATE     Timestamp
  
  FK: ManyToOne → Model
```

### **ModelPerformance** ✅
```java
Paquete:  com.codeflowx.govern.entity.evaluation.ModelPerformance
Tabla:    MODMODELPERFORMANCES
PK:       IDXMODELPERFORMANCE (Long)

Campos principales:
  MODACCURACY    BigDecimal
  MODPRECISION   BigDecimal
  MODRECALL      BigDecimal
  MODF1SCORE     BigDecimal
  
  FK: ManyToOne → Model
```

---

## 🔧 PATRONES DE CÓDIGO

### **ViewModel ZKoss (Patrón MVVM):**

```java
package com.codeflowx.platform.viewmodel.models;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ExampleOverviewViewModel extends MasterPage {
    
    @WireVariable
    private BusinessService businessService;
    
    private List<Entity> items;
    
    @Init
    public void init() {
        log.info("Inicializando ViewModel");
        loadData();
    }
    
    @Command
    @NotifyChange("items")
    public void loadData() {
        // Usar BusinessService para consultar
        PageParams pageParams = new PageParams();
        PageResult result = businessService.find(Entity.class, buildCriterias(), pageParams);
        items = result.getResultList();
    }
    
    @Command
    @NotifyChange("items")
    public void saveItem() {
        // Usar BusinessService para guardar
        businessService.save(currentItem);
        loadData();
    }
    
    private Criterias buildCriterias() {
        Criterias criterias = new Criterias();
        // Agregar criterios de búsqueda
        return criterias;
    }
}
```

### **ZUL File (Patrón MVVM):**

```xml
<zk xmlns:d="desktop"
    xmlns:h="xhtml">
    
    <div apply="org.zkoss.bind.BindComposer"
         viewModel="@id('vm') @init('com.codeflowx.platform.viewmodel.models.ExampleOverviewViewModel')">
        
        <!-- Título -->
        <h:h2>Lista de Items</h:h2>
        
        <!-- Filtros -->
        <div class="filters">
            <textbox placeholder="Buscar..." 
                     value="@bind(vm.searchTerm)" />
            <button label="Buscar" 
                    onClick="@command('loadData')" />
        </div>
        
        <!-- Grid -->
        <listbox model="@load(vm.items)" 
                 selectedItem="@bind(vm.selectedItem)">
            <listhead>
                <listheader label="Nombre" />
                <listheader label="Estado" />
                <listheader label="Acciones" />
            </listhead>
            <template name="model" var="item">
                <listitem>
                    <listcell label="@load(item.modname)" />
                    <listcell label="@load(item.modstatus)" />
                    <listcell>
                        <button label="Ver" 
                                onClick="@command('viewItemDetails', itemId=item.idxmodel)" />
                    </listcell>
                </listitem>
            </template>
        </listbox>
    </div>
</zk>
```

---

## 🚫 ERRORES COMUNES A EVITAR

### **❌ NO HACER:**

```java
// ❌ NO usar EntityManager directo
@PersistenceContext
private EntityManager em;
em.persist(entity);

// ❌ NO usar @Repository directo
@Autowired
private ModelRepository modelRepository;

// ❌ NO crear entidades JPA (ya existen)
@Entity
@Table(name = "MODMODELS")
public class Model { ... }

// ❌ NO crear SQL scripts para tablas existentes
CREATE TABLE MODMODELS ( ... );
```

### **✅ SÍ HACER:**

```java
// ✅ Usar BusinessService
@WireVariable
private BusinessService businessService;

// Guardar
businessService.save(entity);

// Consultar
PageResult result = businessService.find(Entity.class, criterias, pageParams);

// Buscar por ID
Entity entity = businessService.findById(Entity.class, id);

// ✅ Usar entidades existentes
import com.codeflowx.govern.entity.models.Model;
Model model = new Model();
model.setModname("Test Model");
businessService.save(model);
```

---

## 🔗 RELACIONES JPA

### **Model → ModelApproval (OneToMany):**
```java
// En Model.java
@OneToMany(mappedBy = "model")
private List<ModelApproval> approvals;

// En ModelApproval.java
@ManyToOne
@JoinColumn(name = "IDXMODEL")
private Model model;
```

### **Model → ModelBiasAnalysis (OneToMany):**
```java
// En Model.java
@OneToMany(mappedBy = "model")
private List<ModelBiasAnalysis> biasAnalyses;

// En ModelBiasAnalysis.java
@ManyToOne
@JoinColumn(name = "IDXMODEL")
private Model model;
```

---

## 📊 CONSULTAS CON CRITERIAS

### **Filtrar por estado:**
```java
Criterias criterias = new Criterias();

Criteria statusCriteria = new Criteria(Operation.AND, Evaluation.EQUALS, "modstatus");
statusCriteria.setValues(new Object[]{"APPROVED"});
criterias.addCriteria(statusCriteria);

PageResult result = businessService.find(Model.class, criterias, pageParams);
```

### **Búsqueda por texto:**
```java
Criteria searchCriteria = new Criteria(Operation.AND, Evaluation.LIKE, "modname");
searchCriteria.setValues(new Object[]{"%search%"});
criterias.addCriteria(searchCriteria);
```

### **Filtrar por rango de fechas:**
```java
Criteria dateCriteria = new Criteria(Operation.AND, Evaluation.BETWEEN, "modcreateddate");
dateCriteria.setValues(new Object[]{startDate, endDate});
criterias.addCriteria(dateCriteria);
```

---

## 🎯 CHECKLIST ANTES DE GENERAR CÓDIGO

```
☐ Verificar que entidad JPA YA EXISTE (no crear)
☐ Usar BusinessService (no EntityManager)
☐ Usar nombres de tabla correctos (MODMODELS, MODMODELAPPROVALS)
☐ Usar nombres de PK correctos (IDXMODEL, IDXMODELAPPROVAL)
☐ Usar nombres de campos correctos (MODNAME, MODSTATUS)
☐ Paquetes correctos:
  ├─ Entidades: com.codeflowx.govern.entity.[module]
  ├─ ViewModels: com.codeflowx.platform.viewmodel.[module]
  └─ ZULs: src/main/webapp/console/platform/[module]
☐ Logging con @Slf4j
☐ Validaciones con @NotNull, @NotBlank
☐ @NotifyChange en comandos que modifican datos
```

---

## 🔍 VERIFICACIÓN POST-GENERACIÓN

### **Verificar ViewModel:**
```bash
# Verificar que el ViewModel existe
ls -la src/main/java/com/codeflowx/platform/viewmodel/models/ModelExampleViewModel.java

# Buscar errores de compilación
mvn compile
```

### **Verificar ZUL:**
```bash
# Verificar que el ZUL existe
ls -la src/main/webapp/console/platform/models/example/page.zul

# Verificar binding correcto
grep "@init.*ViewModel" src/main/webapp/console/platform/models/example/page.zul
```

### **Verificar Entidad:**
```bash
# Verificar que NO creaste entidad duplicada
find . -name "Model.java" | grep -v nocode.service.entitys
# ↑ NO debe encontrar nada en suinsit.nova.web
```

---

## 📞 CONTACTO Y SOPORTE

- **Proyecto Principal:** `/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/`
- **Proyecto Entidades:** `/mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service/nocode.service.entitys/`
- **Documentación MVP:** `/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/docs/mvp/`

---

**IMPORTANTE:** Copiar esta referencia a cada chat IA antes de generar código para evitar errores.

