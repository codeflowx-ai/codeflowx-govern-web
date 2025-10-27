# Patrón Completo de ViewModels - Guía de Referencia

## Estructura Completa de un ViewModel

Todo ViewModel debe incluir los siguientes elementos en este orden:

### 1. Anotaciones de Clase

```java
@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class MiViewModel extends MasterPage {
    
    private static final long serialVersionUID = 1L;
```

### 2. Inyección de Dependencias Spring

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
    
    protected void initDao() {
        if (businessService == null) {
            businessService = new BusinessService((DataSource) environment.getProperty("APPLICATION_DS", DataSource.class));
        }
    }
    
    @Override
    public void setBeans(Object bean) {
        // TODO Auto-generated method stub
    }
```

### 3. Servicios Adicionales (Flowable, etc.)

```java
    // ========== Servicios Flowable (si aplica) ==========
    @WireVariable
    private RuntimeService runtimeService;
    
    @WireVariable
    private TaskService taskService;
```

### 4. Datos del ViewModel

```java
    // ========== Datos ==========
    private List<MiEntidad> lista = new ArrayList<>();
    private MiEntidad selected;
    // ... otros campos
```

### 5. Inicialización con @AfterCompose

```java
    // ========== Inicialización ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        log.info("🚀 Inicializando MiViewModel");
        
        try {
            loadData();
            calculateKPIs(); // si aplica
        } catch (Exception e) {
            log.error("Error inicializando MiViewModel", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
```

### 6. Carga de Datos con BusinessService

```java
    // ========== Carga de Datos ==========
    
    private void loadData() {
        try {
            // PATRÓN CORRECTO: findByParams con SQL nativo
            String sql = "SELECT * FROM TABLENAME WHERE COL = :param ORDER BY CREATEDAT DESC LIMIT 20";
            Map<String, Object> params = new HashMap<>();
            params.put("param", valor);
            
            List<MiEntidad> result = businessService.findByParams(MiEntidad.class, sql, params);
            
            if (result != null) {
                lista = result;
                log.info("Cargados {} registros", lista.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar datos", e);
        }
    }
```

### 7. Comandos ZKoss

```java
    // ========== Comandos ==========
    
    @Command
    @NotifyChange("*")
    public void save() {
        try {
            // Validar
            if (!validate()) {
                return;
            }
            
            // Guardar con BusinessService
            businessService.save(selected);
            
            // Registrar actividad
            logActivity("CREACION", "TABLENAME", selected.getId(), 
                       "Creado registro: " + selected.getNombre());
            
            // Recargar y notificar
            loadData();
            Messagebox.show("Guardado correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            
        } catch (Exception e) {
            log.error("Error al guardar", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
```

### 8. Método logActivity (OBLIGATORIO)

```java
    // ========== Auditoría ==========
    
    /**
     * Registra la actividad del usuario en el sistema de auditoría
     * 
     * @param action  - Tipo de acción: CONSULTA, CREACION, EDICION, BORRAR, BUSCAR, APROBACION, RECHAZO
     * @param model   - Nombre de la tabla/módulo (ej: "AGTAGENTS", "PRMPROMPTS")
     * @param pk      - Primary key del registro (puede ser null)
     * @param mensaje - Descripción de la actividad
     */
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
            // No lanzar excepción para que no interrumpa el flujo normal
        }
    }
```

### 9. Método @Destroy (OBLIGATORIO)

```java
    @org.zkoss.bind.annotation.Destroy
    public void destroy() {
        // Limpiar listas
        if (lista != null) { lista.clear(); lista = null; }
        if (otraLista != null) { otraLista.clear(); otraLista = null; }
        
        // Limpiar objetos
        selected = null;
        
        // Limpiar servicios
        businessService = null;
        runtimeService = null; // si aplica
    }
}
```

---

## Imports Necesarios

```java
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.zkoss.bind.annotation.Destroy; // ← IMPORTANTE
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

// Entidades
import com.codeflowx.admin.Ssoractividad; // ← IMPORTANTE para logActivity

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
```

---

## Reglas Críticas

### ❌ NO USAR (Métodos inexistentes):

1. `businessService.findAllEntity()` → **NO EXISTE**
2. `businessService.createQuery()` → **NO EXISTE**
3. `businessService.searchCriteria()` → **NO EXISTE**
4. `businessService.persistEntity()` → **NO EXISTE**
5. `businessService.removeFromID()` → **NO EXISTE**
6. `businessService.findByParams(Class, sql, params, pageParams)` → **4 parámetros NO EXISTEN**
7. `businessService.save(Class, Object)` → **Firma incorrecta**

### ✅ USAR (Métodos correctos):

1. `businessService.findByParams(Class, sql, params)` → **3 parámetros**
2. `businessService.findById(Class, id)` → **Correcto**
3. `businessService.save(Object)` → **Correcto**

### ❌ NO USAR (Tipos inexistentes):

1. `PageResult<T>` → **NO EXISTE**
2. `PageParams` → **NO EXISTE** (usar LIMIT en SQL)
3. `Criterias` → **NO EXISTE** (usar WHERE en SQL)

### ✅ USAR (Tipos correctos):

1. `List<T>` → **Para resultados de queries**
2. `Map<String, Object>` → **Para parámetros SQL**

---

## Acciones de logActivity

### Acciones estándar:
- `"CONSULTA"` - Al cargar un registro específico
- `"CREACION"` - Al crear un nuevo registro
- `"EDICION"` - Al modificar un registro existente
- `"BORRAR"` - Al eliminar un registro
- `"BUSCAR"` - Al realizar una búsqueda
- `"APROBACION"` - Al aprobar algo
- `"RECHAZO"` - Al rechazar algo

### Cuándo llamar a logActivity:

```java
// Al cargar un registro (Detail)
logActivity("CONSULTA", "AGTAGENTS", agentId, "Consulta agente: " + agent.getName());

// Al crear
logActivity("CREACION", "AGTAGENTS", agent.getId(), "Creado agente: " + agent.getName());

// Al editar
logActivity("EDICION", "AGTAGENTS", agent.getId(), "Modificado agente: " + agent.getName());

// Al eliminar
logActivity("BORRAR", "AGTAGENTS", agentId, "Eliminado agente ID: " + agentId);

// Al buscar (Overview)
logActivity("BUSCAR", "AGTAGENTS", null, "Búsqueda de agentes con término: " + searchTerm);

// Al aprobar/rechazar (Workflow)
logActivity("APROBACION", "AGTAGENTAPPROVALS", approval.getId(), "Aprobado agente: " + agent.getName());
```

---

## Ejemplo Completo

```java
package com.codeflowx.govern.viewmodel.example;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.zkoss.bind.annotation.Destroy;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Messagebox;

import com.codeflowx.admin.Ssoractividad;
import com.codeflowx.govern.entity.agents.Agent;

import codeflowx.nocode.persist.BusinessService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
@Init(superclass = true)
public class ExampleViewModel extends MasterPage {
    
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

    // ========== Datos ==========
    private List<Agent> agentsList = new ArrayList<>();
    private Agent selectedAgent;

    // ========== Inicialización ==========
    
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
        Selectors.wireComponents(view, this, false);
        super.doAfterCompose(view);
        initDao();
        
        log.info("🚀 Inicializando ExampleViewModel");
        
        try {
            loadData();
        } catch (Exception e) {
            log.error("Error inicializando ExampleViewModel", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    // ========== Carga de Datos ==========
    
    private void loadData() {
        try {
            String sql = "SELECT * FROM AGTAGENTS ORDER BY AGTCREATEDAT DESC LIMIT 20";
            List<Agent> result = businessService.findByParams(Agent.class, sql, null);
            
            if (result != null) {
                agentsList = result;
                log.info("Cargados {} agentes", agentsList.size());
            }
        } catch (Exception e) {
            log.error("Error al cargar datos", e);
        }
    }

    // ========== Comandos ==========
    
    @Command
    @NotifyChange("*")
    public void save() {
        try {
            businessService.save(selectedAgent);
            
            logActivity("CREACION", "AGTAGENTS", selectedAgent.getIdxagent(), 
                       "Creado agente: " + selectedAgent.getAgtname());
            
            loadData();
            Messagebox.show("Guardado correctamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            
        } catch (Exception e) {
            log.error("Error al guardar", e);
            Messagebox.show("Error: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    // ========== Auditoría ==========
    
    /**
     * Registra la actividad del usuario en el sistema de auditoría
     */
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
            // No lanzar excepción para que no interrumpa el flujo normal
        }
    }
    
    // ========== Limpieza ==========
    
    @Destroy
    public void destroy() {
        if (agentsList != null) { agentsList.clear(); agentsList = null; }
        selectedAgent = null;
        businessService = null;
    }
}
```

---

## Checklist de Verificación

Para cada ViewModel, verificar:

- [ ] Extiende `MasterPage`
- [ ] Tiene `@VariableResolver(DelegatingVariableResolver.class)`
- [ ] Tiene `@Init(superclass = true)`
- [ ] Tiene todas las `@WireVariable` necesarias
- [ ] Tiene `initDao()` y `setBeans()`
- [ ] Usa `@AfterCompose` en lugar de `@Init` para inicialización
- [ ] Usa `businessService.findByParams(Class, sql, params)` (3 parámetros)
- [ ] Usa `List<T>` en lugar de `PageResult<T>`
- [ ] Usa `businessService.save(Object)` sin el `Class`
- [ ] Tiene método `logActivity(action, model, pk, mensaje)` implementado
- [ ] Llama a `logActivity()` en operaciones CRUD
- [ ] Tiene método `@Destroy` que limpia recursos
- [ ] Importa `Ssoractividad` de `com.codeflowx.govern.entity.security`

---

## Referencia Rápida - Nombres de Tablas

| Entidad | Tabla SQL |
|---------|-----------|
| Agent | AGTAGENTS |
| AgentApproval | AGTAGENTAPPROVALS |
| Model | MODMODELS |
| ModelApproval | MODMODELAPPROVALS |
| Prompt | PRMPROMPTS |
| PromptApproval | PRMPROMPTAPPROVALS |
| RagSystem | RAGRAGSYSTEMS |
| ComplianceAssessment | GOVCOMPLIANCEASSESSMENTS |
| PolicyAuditLog | GOVPOLICYAUDITLOGS |
| EthicsReview | ETHETHICSREVIEWS |
| Department | CORDEPARTMENTS |
| User | SSOUSUARIOS |
| Role | SSOROLES |
| Permission | CORPERMISSIONS |
| Menu | CORMENUS |

**Regla:** El nombre de tabla tiene el prefijo del módulo (3 chars) seguido del nombre de la entidad en plural.

---

**Archivo de referencia completo:** `TaskInboxViewModel.java`  
**Última actualización:** 25 Octubre 2025

