# 🎯 ARQUITECTURA DE MENÚS Y ROLES - CODEFLOWX GOVERN

**Fecha:** Octubre 2025  
**Versión:** 1.0  
**Propósito:** Definir la organización de menús y roles para la plataforma CodeflowX AI Governance

---

## 📋 RESUMEN EJECUTIVO

La plataforma CodeflowX Govern implementa un sistema de menús jerárquico basado en roles y permisos, organizando las funcionalidades en 4 categorías principales:

- **🏠 Dashboards** - Visualizaciones ejecutivas y operativas
- **⚙️ Platform** - CRUD de entidades del sistema
- **🏛️ Gobierno** - Funcionalidades específicas de gobernanza AI
- **🔄 BPMN** - Procesos de workflow y tareas

---

## 🏗️ ARQUITECTURA DE MENÚS

### 1. **Estructura Principal**

```
📱 CODEFLOWX GOVERN MENU
├── 🏠 DASHBOARDS
│   ├── Dashboard Principal
│   ├── Dashboard de Agentes
│   ├── Dashboard de Compliance
│   ├── Dashboard Ejecutivo
│   └── Dashboard de Costos
├── ⚙️ PLATFORM (CRUD)
│   ├── Agents (Agentes)
│   ├── Models (Modelos)
│   ├── Prompts (Prompts)
│   ├── RAG Systems
│   ├── Providers
│   ├── Infrastructure
│   └── Training
├── 🏛️ GOBIERNO
│   ├── Analytics
│   ├── Compliance
│   ├── Ethics
│   └── Core (Roles/Permisos)
└── 🔄 BPMN (Workflows)
    ├── Task Inbox
    ├── Agent Approval
    ├── Model Approval
    └── Compliance Review
```

### 2. **Organización por Directorios**

| Directorio | Propósito | Tipo | ViewModels |
|------------|-----------|------|------------|
| `console/dashboards/` | Visualizaciones | Dashboard | 10 ViewModels |
| `console/platform/` | CRUD Manual | Detail/Overview | 8 ViewModels |
| `console/gobierno/` | Gobernanza AI | Alta/Consulta | 9 ViewModels |
| `console/bpmn/` | Procesos BPMN | User Tasks | 25 ViewModels |

---

## 👥 SISTEMA DE ROLES

### 1. **Roles Principales**

#### **🔧 ADMINISTRADOR**
- **Acceso:** Completo a toda la plataforma
- **Menús:** Todos los dashboards, platform, gobierno y BPMN
- **Permisos:** CRUD completo, configuración de roles, auditoría

#### **👨‍💼 GESTOR DE GOBIERNO**
- **Acceso:** Dashboards + Gobierno + BPMN
- **Menús:** 
  - Dashboards ejecutivos y de compliance
  - Módulos de gobierno (analytics, compliance, ethics)
  - Bandeja de tareas BPMN
- **Permisos:** Lectura/escritura en gobierno, aprobación de procesos

#### **🔬 CIENTÍFICO DE DATOS**
- **Acceso:** Platform + Training + Dashboards técnicos
- **Menús:**
  - CRUD de Agents, Models, Prompts
  - Training y Experimentos
  - Dashboard de Agentes
- **Permisos:** Creación/modificación de entidades ML

#### **👤 USUARIO OPERATIVO**
- **Acceso:** Solo BPMN y dashboards básicos
- **Menús:**
  - Task Inbox (BPMN)
  - Dashboard principal
- **Permisos:** Solo ejecución de tareas asignadas

### 2. **Matriz de Permisos**

| Funcionalidad | Admin | Gestor | Científico | Operativo |
|---------------|-------|--------|------------|-----------|
| **Dashboards** | ✅ Todos | ✅ Ejecutivos | ✅ Técnicos | ✅ Básicos |
| **Platform CRUD** | ✅ Completo | ❌ | ✅ Agents/Models | ❌ |
| **Gobierno** | ✅ Completo | ✅ Completo | ❌ | ❌ |
| **BPMN Tasks** | ✅ Todas | ✅ Aprobaciones | ✅ Técnicas | ✅ Asignadas |
| **Configuración** | ✅ Completa | ❌ | ❌ | ❌ |

---

## 🎨 IMPLEMENTACIÓN TÉCNICA

### 1. **Componentes del Sistema**

#### **MenuViewModel.java**
```java
@Slf4j
@Getter @Setter
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MenuViewModel extends MasterBeanUI {
    private PageResult<Menu> pageResult;
    private String searchTerm = "";
    private Menu selectedMenu;
    
    @Command
    @NotifyChange("pageResult")
    public void loadData() {
        // Carga menús desde CORMENUS
    }
}
```

#### **Entidades Core**
- **Menu** (`CORMENUS`): Estructura jerárquica de menús
- **Role** (`CORROLES`): Definición de roles
- **Permission** (`CORPERMISOS`): Permisos específicos
- **User** (`CORUSUARIOS`): Usuarios del sistema

### 2. **Archivos de Menú**

#### **menuLeft.zul** (Menú Lateral)
```xml
<drawer id="drawer" class="draw-left" position="left">
    <div class="sidebar-left-logo">
        <h:img src="img/logo-discover-white.png" alt="Logo" />
    </div>
    <forEach items="@load(ds.menus)" varStatus="st">
        <a iconSclass="${each.iconClass}" href="#" class="menu-item"
           onClick="@command('onSelectMenu',item=each)">
            ${each.name}
        </a>
    </forEach>
</drawer>
```

#### **menuArquitecture.zul** (Menú Superior)
```xml
<menubar id="menubar" autodrop="true">
    <menu label="Modulos" iconSclass="mdi di-application-variable-outline">
        <!-- Submenús dinámicos -->
    </menu>
    <menu label="Modelo de datos" iconSclass="mdi mdi-database">
        <!-- Herramientas de datos -->
    </menu>
    <!-- Más menús... -->
</menubar>
```

---

## 🔐 CONTROL DE ACCESO

### 1. **Estrategia de Seguridad**

#### **Nivel 1: Autenticación**
- Login con usuario/contraseña
- Sesiones seguras con timeout
- Registro de intentos de acceso

#### **Nivel 2: Autorización por Roles**
- Asignación de roles a usuarios
- Permisos granulares por funcionalidad
- Herencia de permisos

#### **Nivel 3: Control de Vista**
- Menús dinámicos según rol
- Ocultación de opciones no autorizadas
- Redirección automática

### 2. **Implementación**

#### **En ViewModels**
```java
@Command
public void checkPermission(String action) {
    if (!hasPermission(action)) {
        Messagebox.show("No tiene permisos para esta acción");
        return;
    }
    // Ejecutar acción
}
```

#### **En ZUL**
```xml
<button visible="@load(vm.hasPermission('CREATE_AGENT'))"
        onClick="@command('createAgent')">
    Crear Agente
</button>
```

---

## 📊 ORGANIZACIÓN POR MÓDULOS

### 1. **Dashboards** (`console/dashboards/`)
- **Propósito:** Visualizaciones ejecutivas y operativas
- **Roles:** Todos (con diferentes niveles de detalle)
- **ViewModels:** 10 especializados

| Dashboard | Rol Principal | Descripción |
|-----------|---------------|-------------|
| `dashboard.zul` | Todos | Dashboard principal |
| `executive.zul` | Gestor/Admin | Vista ejecutiva |
| `agentdashboard.zul` | Científico | Métricas de agentes |
| `compliance.zul` | Gestor | Estado de compliance |
| `costdashboard.zul` | Admin | Análisis de costos |

### 2. **Platform** (`console/platform/`)
- **Propósito:** CRUD de entidades del sistema
- **Roles:** Admin, Científico
- **Patrón:** Overview + Detail por entidad

| Módulo | Entidades | ViewModels |
|--------|-----------|------------|
| `agents/` | Agent, AgentApproval, etc. | AgentsDetailViewModel |
| `models/` | Model, ModelVersion, etc. | ModelsDetailViewModel |
| `prompts/` | Prompt, PromptVersion | PromptsDetailViewModel |
| `rag/` | RagSystem, RagDataSource | RagSystemsDetailViewModel |

### 3. **Gobierno** (`console/gobierno/`)
- **Propósito:** Funcionalidades específicas de gobernanza
- **Roles:** Admin, Gestor
- **Enfoque:** Alta/Consulta especializada

| Módulo | Funcionalidad | ViewModels |
|--------|---------------|------------|
| `analytics/` | Métricas y reportes | AnalyticsOverviewViewModel |
| `compliance/` | Cumplimiento normativo | ComplianceAssessmentViewModel |
| `ethics/` | Evaluaciones éticas | EthicsAssessmentViewModel |
| `core/` | Roles y permisos | RoleDetailViewModel, PermissionDetailViewModel |

### 4. **BPMN** (`console/bpmn/`)
- **Propósito:** Procesos de workflow y tareas
- **Roles:** Todos (según asignación)
- **Enfoque:** User Tasks de procesos

| Proceso | Tarea | ViewModel |
|---------|-------|-----------|
| Agent Approval | Human Override | AgentApprovalWorkflowViewModel |
| Model Approval | Human Review | ModelApprovalWorkflowViewModel |
| Compliance | Review Decision | ComplianceReviewViewModel |
| Ethics | Committee Review | EthicsCommitteeViewModel |

---

## 🚀 RECOMENDACIONES DE IMPLEMENTACIÓN

### 1. **Fase 1: Estructura Base**
- ✅ Implementar sistema de roles básico
- ✅ Configurar menús principales
- ✅ Establecer permisos por módulo

### 2. **Fase 2: Menús Dinámicos**
- 🔄 Implementar carga dinámica desde BD
- 🔄 Menús contextuales por rol
- 🔄 Breadcrumbs inteligentes

### 3. **Fase 3: Seguridad Avanzada**
- 🔄 Auditoría de accesos
- 🔄 Permisos granulares
- 🔄 Integración SSO

### 4. **Fase 4: Personalización**
- 🔄 Dashboards personalizables
- 🔄 Menús favoritos
- 🔄 Notificaciones contextuales

---

## 📈 MÉTRICAS Y MONITOREO

### 1. **KPIs del Sistema**
- **Usuarios activos por rol**
- **Páginas más visitadas**
- **Tiempo promedio por sesión**
- **Errores de permisos**

### 2. **Alertas de Seguridad**
- Intentos de acceso no autorizado
- Cambios en roles críticos
- Accesos fuera de horario
- Sesiones sospechosas

---

## 🔧 CONFIGURACIÓN INICIAL

### 1. **Roles por Defecto**
```sql
INSERT INTO CORROLES (ROLENAME, ROLEDESC) VALUES 
('ADMIN', 'Administrador del sistema'),
('GESTOR_GOVERNANCE', 'Gestor de gobernanza AI'),
('DATA_SCIENTIST', 'Científico de datos'),
('OPERATIVE_USER', 'Usuario operativo');
```

### 2. **Menús Base**
```sql
INSERT INTO CORMENUS (MENUNAME, MENUORDER, ICONCLASS) VALUES
('Dashboard', 1, 'mdi mdi-view-dashboard'),
('Agentes', 2, 'mdi mdi-robot'),
('Modelos', 3, 'mdi mdi-brain'),
('Gobierno', 4, 'mdi mdi-shield-check'),
('Procesos', 5, 'mdi mdi-workflow');
```

---

## ✅ CONCLUSIÓN

La arquitectura de menús y roles de CodeflowX Govern proporciona:

1. **🎯 Organización Clara:** Separación lógica por funcionalidad
2. **🔐 Seguridad Robusta:** Control granular de accesos
3. **📱 Experiencia Intuitiva:** Menús contextuales por rol
4. **🔧 Flexibilidad:** Fácil adición de nuevos módulos
5. **📊 Escalabilidad:** Soporte para múltiples organizaciones

**Estado:** Documentación completa ✅  
**Próximo paso:** Implementación de menús dinámicos 🔄
