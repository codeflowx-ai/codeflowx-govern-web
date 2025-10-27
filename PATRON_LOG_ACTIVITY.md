# Patrón logActivity - Registro de Actividad del Usuario

## Descripción

El método `logActivity` se utiliza para registrar cada acción que realiza el usuario en la aplicación, guardando un log en la tabla `SSORACTIVIDAD`.

## Firma del Método

```java
private void logActivity(String action, String model, Long pk, String mensaje)
```

### Parámetros:

1. **action** (String): Tipo de acción realizada
   - `"CONSULTA"` - Cuando se consulta/lee un registro
   - `"CREACION"` - Cuando se crea un nuevo registro
   - `"EDICION"` - Cuando se modifica un registro existente
   - `"BORRAR"` - Cuando se elimina un registro
   - `"BUSCAR"` - Cuando se realiza una búsqueda
   - `"APROBACION"` - Cuando se aprueba algo
   - `"RECHAZO"` - Cuando se rechaza algo

2. **model** (String): Nombre de la tabla/entidad sobre la que se actúa
   - Ejemplos: `"AGTAGENTS"`, `"PRMPROMPTS"`, `"MODMODELS"`, etc.

3. **pk** (Long): ID del registro (Primary Key)
   - Puede ser `null` para operaciones de búsqueda general

4. **mensaje** (String): Descripción de la actividad
   - Debe ser descriptivo y claro
   - Incluir información relevante del contexto

## Implementación Completa

### 1. Import necesario:

```java
import com.codeflowx.admin.Ssoractividad;
```

### 2. Método privado en el ViewModel:

```java
/**
 * Registra la actividad del usuario en la base de datos
 */
private void logActivity(String action, String model, Long pk, String mensaje) {
    try {
        Ssoractividad log = new Ssoractividad();
        log.setUsername(getUser().getUsername());
        log.setAccion(action);
        log.setAlta(new java.sql.Timestamp(System.currentTimeMillis()));
        log.setModulo(model);
        log.setIdtupla(pk != null ? pk.intValue() : 0);
        log.setAplicacion(ctxBean.getApplicationName());
        log.setValuetupla(mensaje);
        businessService.save(log);
    } catch (Exception e) {
        log.error("Error registrando actividad del usuario", e);
    }
}
```

## Ejemplos de Uso

### Ejemplo 1: Consulta de un registro

```java
@AfterCompose
public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
    // ... inicialización ...
    
    Long agentId = getParameter("id");
    if (agentId != null) {
        currentAgent = businessService.findById(Agent.class, agentId);
        logActivity("CONSULTA", "AGTAGENTS", agentId, 
                   "Consulta detalle de agente: " + currentAgent.getAgtname());
    }
}
```

### Ejemplo 2: Creación de un registro

```java
@Command
@NotifyChange("*")
public void save() {
    try {
        // ... validación ...
        
        Agent agent = new Agent();
        agent.setAgtname(name);
        // ... setear otros campos ...
        
        businessService.save(agent);
        
        logActivity("CREACION", "AGTAGENTS", agent.getIdxagent(), 
                   "Creado nuevo agente: " + agent.getAgtname());
        
        Messagebox.show("Agente creado exitosamente", "Éxito", 
                       Messagebox.OK, Messagebox.INFORMATION);
    } catch (Exception e) {
        log.error("Error creando agente", e);
    }
}
```

### Ejemplo 3: Edición de un registro

```java
@Command
@NotifyChange("*")
public void update() {
    try {
        // ... validación ...
        
        currentAgent.setAgtname(name);
        currentAgent.setAgtupdatedat(new Timestamp(System.currentTimeMillis()));
        
        businessService.save(currentAgent);
        
        logActivity("EDICION", "AGTAGENTS", currentAgent.getIdxagent(), 
                   "Editado agente: " + currentAgent.getAgtname());
        
        Messagebox.show("Agente actualizado exitosamente", "Éxito", 
                       Messagebox.OK, Messagebox.INFORMATION);
    } catch (Exception e) {
        log.error("Error actualizando agente", e);
    }
}
```

### Ejemplo 4: Eliminación de un registro

```java
@Command
@NotifyChange("*")
public void delete(@BindingParam("item") Agent agent) {
    try {
        Messagebox.show("¿Confirma eliminar el agente?", "Confirmación", 
            Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_YES.equals(event.getName())) {
                    Long agentId = agent.getIdxagent();
                    String agentName = agent.getAgtname();
                    
                    // businessService.delete(agent); // Si existe el método
                    
                    logActivity("BORRAR", "AGTAGENTS", agentId, 
                               "Eliminado agente: " + agentName);
                    
                    loadData(); // Recargar lista
                }
            }
        );
    } catch (Exception e) {
        log.error("Error eliminando agente", e);
    }
}
```

### Ejemplo 5: Búsqueda

```java
@Command
@NotifyChange("*")
public void search() {
    try {
        // ... realizar búsqueda ...
        
        logActivity("BUSCAR", "AGTAGENTS", null, 
                   "Búsqueda de agentes con criterio: " + searchTerm);
        
        loadData();
    } catch (Exception e) {
        log.error("Error en búsqueda", e);
    }
}
```

### Ejemplo 6: Aprobación/Rechazo (Workflow)

```java
@Command
public void approve() {
    try {
        // ... lógica de aprobación ...
        
        approval.setStatus("APPROVED");
        businessService.save(approval);
        
        logActivity("APROBACION", "PRMPROMPTAPPROVALS", approval.getIdxpromptapproval(), 
                   "Aprobado prompt: " + prompt.getPrmname() + " - Usuario: " + getUser().getUsername());
        
        Messagebox.show("Prompt aprobado exitosamente", "Éxito", 
                       Messagebox.OK, Messagebox.INFORMATION);
    } catch (Exception e) {
        log.error("Error aprobando prompt", e);
    }
}
```

## Mapeo de Tablas por Módulo

Para el parámetro `model`, usar el nombre de la tabla correspondiente:

| Módulo | Prefijo | Ejemplo de Tabla |
|--------|---------|------------------|
| Agents | AGT | AGTAGENTS, AGTAGENTAPPROVALS |
| Models | MOD | MODMODELS, MODMODELVERSIONS |
| Prompts | PRM | PRMPROMPTS, PRMPROMPTAPPROVALS |
| RAG | RAG | RAGRAGSYSTEMS, RAGRAGVERSIONS |
| Governance | GOV | GOVCOMPLIANCEASSESSMENTS, GOVPOLICIES |
| Ethics | ETH | ETHETHICSREVIEWS |
| Analytics | ANL | ANLBIASANALYSES, ANLFAIRNESSMETRICS |
| Core | COR | CORDEPARTMENTS, CORMENUS |
| Security | SSO | SSOUSUARIOS, SSOROLES |

## Mejores Prácticas

### ✅ HACER:
- Registrar todas las acciones CRUD importantes
- Usar mensajes descriptivos y claros
- Incluir el nombre o identificador del registro en el mensaje
- Capturar excepciones sin interrumpir el flujo principal
- Usar el nombre correcto de la tabla

### ❌ NO HACER:
- Registrar operaciones de lectura masiva (listas completas)
- Hacer que el log sea demasiado verbose
- Dejar que una excepción del log rompa la operación principal
- Usar nombres de tabla incorrectos o inventados
- Omitir información relevante del contexto

## Cuándo Registrar Actividad

### Siempre Registrar:
- ✅ Creación de registros
- ✅ Edición de registros
- ✅ Eliminación de registros
- ✅ Aprobaciones/Rechazos en workflows
- ✅ Consultas de registros individuales (detalle)
- ✅ Acciones administrativas importantes

### Opcional/No Necesario:
- ❌ Cargas de listas completas (loadData inicial)
- ❌ Filtros simples
- ❌ Navegación entre páginas
- ❌ Operaciones de solo lectura masivas

## Estructura de la Tabla SSORACTIVIDAD

La entidad `Ssoractividad` tiene los siguientes campos:

```java
private String username;      // Usuario que realiza la acción
private String accion;         // Tipo de acción (CONSULTA, CREACION, etc.)
private Timestamp alta;        // Fecha/hora de la acción
private String modulo;         // Tabla/entidad sobre la que se actúa
private Integer idtupla;       // ID del registro (0 si no aplica)
private String aplicacion;     // Nombre de la aplicación
private String valuetupla;     // Descripción/mensaje de la actividad
```

## Notas Importantes

1. **Nunca fallar la operación principal:** El método `logActivity` tiene un `try-catch` interno para que si falla el registro del log, no afecte la operación que el usuario estaba realizando.

2. **Usar información del contexto:** Aprovechar `ctxBean` y `getUser()` para obtener información automática del usuario y aplicación.

3. **Mensajes descriptivos:** Los mensajes deben permitir entender qué pasó sin necesidad de ver otros logs o el código.

4. **Performance:** El registro de actividad es asíncrono desde el punto de vista del usuario (no bloquea), pero sincrónico en la implementación (se guarda inmediatamente).

---

**Aplicar este patrón en TODOS los ViewModels que realicen operaciones CRUD o de workflow.**

