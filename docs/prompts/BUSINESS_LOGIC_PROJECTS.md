# PROMPT DE LÓGICA DE NEGOCIO - MÓDULO PROJECTS

**Fecha:** Diciembre 2025
**Módulo:** Projects (Proyectos de IA)
**Objetivo:** Definir la lógica de negocio completa para el módulo de gestión de proyectos
**Esfuerzo Estimado:** 4-5 días

> **⚠️ IMPORTANTE:** Todas las llamadas a microservicios Python están **COMENTADAS** en este prompt.
> Las operaciones CRUD y consultas BBDD están implementadas, pero las integraciones con microservicios externos están pendientes de implementar cuando estén disponibles.

---

## 📋 CONTEXTO DEL MÓDULO

### **Descripción Funcional**
El módulo Projects gestiona el ciclo de vida completo de proyectos de IA, incluyendo:
- Creación y gestión de proyectos
- Gestión de equipos y recursos
- Control financiero y costos
- Planificación y seguimiento
- Gestión de tecnologías y stacks
- Compliance y governance
- Integración con sistemas externos (Jira, GitHub, ERP)

### **Pantallas Asociadas**

#### **ViewModels Identificados (47 ViewModels)**
**Referencia:** `suinsit.nova.web/docs/migration/prompts/MIGRACION_PROJECTS_01.md`

**ViewModels Principales:**
- `ProjectAIInventoryViewModel` - Inventario de sistemas IA del proyecto
- `ProjectsDashboardViewModel` - Dashboard de proyectos
- `ProjectOverviewViewModel` - Vista general de proyectos
- `ProjectDetailViewModel` - Detalles de proyecto
- `ProjectMemberOverviewViewModel` - Miembros del equipo
- `ProjectMemberDetailViewModel` - Detalles de miembro
- `ProjectResourceConsumptionOverviewViewModel` - Consumo de recursos
- `ProjectCostEstimatorOverviewViewModel` - Estimadores de costos
- `ProjectBillingDetailOverviewViewModel` - Facturación
- `ProjectTechnologyOverviewViewModel` - Tecnologías
- Y 37 ViewModels adicionales para gestión completa de proyectos

#### **Pantallas ZUL Identificadas (49 pantallas)**
**Referencia:** `suinsit.nova.web/docs/funcional/projects/01_REORGANIZACION_PANTALLAS_PROJECTS.md`

- **46 pantallas** en `console/platform/projects/`:
  - Core (4): `project-overview.zul`, `project-detail.zul`, `project-domain-overview.zul`, `project-domain-detail.zul`
  - Team (4): `project-member-overview.zul`, `project-member-detail.zul`, `project-resource-allocation-overview.zul`, `project-time-tracking-overview.zul`
  - Resources (6): `project-resource-consumption-overview.zul`, `project-resource-consumption-detail.zul`, `project-artifact-overview.zul`, `project-artifact-detail.zul`, `project-document-overview.zul`, `project-document-detail.zul`
  - Financial (14): Pantallas de costos, facturación, ROI, estimadores
  - Planning (8): Pantallas de planificación y seguimiento
  - Technology (6): Pantallas de tecnologías y stacks
  - Management (4): Pantallas de gestión y administración
- **3 pantallas** en `console/gobierno/projects/` (governance)

#### **Pantallas Next.js Migradas (20+ pantallas)**
**Ubicación:** `app/(app)/projects/`

**Pantallas Principales Migradas:**
- ✅ `app/(app)/projects/page.tsx` - Dashboard principal
- ✅ `app/(app)/projects/community/page.tsx` - Comunidad
- ✅ `app/(app)/projects/conversational/page.tsx` - Proyectos conversacionales
- ✅ `app/(app)/projects/conversational/new/page.tsx` - Nuevo proyecto conversacional
- ✅ `app/(app)/projects/generation/page.tsx` - Generación
- ✅ `app/(app)/projects/domains/page.tsx` - Dominios
- ✅ `app/(app)/projects/domains/[id]/page.tsx` - Detalle de dominio
- ✅ `app/(app)/projects/artifacts/page.tsx` - Artefactos
- ✅ `app/(app)/projects/artifacts/[id]/page.tsx` - Detalle de artefacto
- ✅ `app/(app)/projects/documents/page.tsx` - Documentos
- ✅ `app/(app)/projects/documents/[id]/page.tsx` - Detalle de documento
- ✅ `app/(app)/projects/cost-estimator/page.tsx` - Estimadores de costo
- ✅ `app/(app)/projects/cost-estimator/[id]/page.tsx` - Detalle de estimador
- ✅ `app/(app)/projects/billing-detail/page.tsx` - Detalles de facturación
- ✅ `app/(app)/projects/billing-detail/[id]/page.tsx` - Detalle de facturación
- ✅ `app/(app)/projects/billing-status/page.tsx` - Estado de facturación
- ✅ `app/(app)/projects/invoices/page.tsx` - Facturas
- ✅ `app/(app)/projects/invoice-aging/page.tsx` - Envejecimiento de facturas
- ✅ `app/(app)/projects/cost-breakdown/page.tsx` - Desglose de costos
- ✅ `app/(app)/projects/financial-summary/page.tsx` - Resumen financiero
- ✅ `app/(app)/projects/client-profitability/page.tsx` - Rentabilidad de clientes

**Referencia:** `codeflowx-studio/docs/PLAN_MIGRACION_ZUL_VIEWMODELS.md`

### **Entidades JPA Principales**
- `Project` - Entidad principal de proyectos
- `ProjectMember` - Miembros del equipo
- `ProjectTask` - Tareas del proyecto
- `ProjectResourceConsumption` - Consumo de recursos
- `ProjectCostEstimator` - Estimadores de costos
- `ProjectBillingDetail` - Detalles de facturación
- `ProjectInvoice` - Facturas
- `ProjectROI` - Retorno de inversión
- `ProjectTechnology` - Tecnologías del proyecto
- `ProjectStack` - Stacks tecnológicos
- `ProjectArtifact` - Artefactos del proyecto
- `ProjectDocument` - Documentos
- `ProjectDomain` - Dominios del proyecto
- `ProjectVersion` - Versiones del proyecto
- `ProjectRequirement` - Requisitos
- `ProjectTimeTracking` - Seguimiento de tiempo
- `ProjectLicense` - Licencias
- `ProjectToken` - Tokens de API

---

## 🏗️ ARQUITECTURA Y DEPENDENCIAS

### **Business Services Existentes**
1. **ProhibitedSystemBusinessService** (✅ Creado)
   - `checkProhibitedSystem(Project)` - Verifica si proyecto es sistema prohibido
   - `getActiveProhibitedSystems()` - Obtiene sistemas prohibidos activos

### **Servicios CRUD (codeflowx.govern.services)**
- `ProjectService` - CRUD básico de proyectos
- `ProjectMemberService` - CRUD de miembros
- `ProjectTaskService` - CRUD de tareas
- `ProjectResourceConsumptionService` - CRUD de consumo de recursos
- `ProjectCostEstimatorService` - CRUD de estimadores
- `ProjectBillingDetailService` - CRUD de facturación
- `ProjectTechnologyService` - CRUD de tecnologías

### **Microservicios Python Disponibles (COMENTADOS)**
- `codeflowx-governance-api` - API de governance
- `codeflowx-aios-telemetry` - Telemetría y métricas
- Integraciones externas (Jira, GitHub, ERP)

---

## 📚 REFERENCIAS DE PROMPTS JAVA

### **Prompt A.2 - Extensión Entidad Project.java** (PROMPTS_03_JAVA_BACKEND_EXISTENTE.md)
**Campos EU AI Act añadidos:**
- `PRJISHIGHRISK` - Boolean si proyecto es alto riesgo (Art. 6)
- `PRJANNEXIIICATEGORIES` - Array categorías Anexo III (JSON)
- `PRJCLASSIFICATIONDATE` - Timestamp clasificación
- `PRJCLASSIFICATIONAUTHOR` - Quién clasificó
- `PRJREGULATEDSECTOR` - Boolean si sector regulado (Anexo I)
- `PRJANNEXILEGISLATION` - Array legislaciones Anexo I (JSON)
- `PRJPROHIBITEDUSECHECKED` - Boolean si verificó Art. 5

**Métodos BusinessService requeridos:**
- `classifyProject(projectId, category)` - Clasificar proyecto
- `verifyProhibitedSystems(projectId)` - Verificar sistemas prohibidos
- `checkRegulatedSector(projectId)` - Verificar sector regulado

---

## 🔍 VALIDACIONES DE AUDITORÍA

### **INC-005: Falta Validación de Sistemas Prohibidos (Art. 5) Antes de Clasificación**
**Prioridad:** 🔴 CRÍTICA
**Artículo:** Art. 5, Anexo II

**Validación Requerida:**
```java
// Validar que se haya verificado contra sistemas prohibidos antes de clasificar
if (project.getPrjprohibitedusechecked() == null || !project.getPrjprohibitedusechecked()) {
    throw new ValidationException(
        "CRITICAL: Debe verificar contra Art. 5 (sistemas prohibidos) antes de clasificar. " +
        "Complete verificación en pestaña 'Compliance'."
    );
}

// Verificar automáticamente contra lista prohibida
List<ProhibitedSystem> prohibitedSystems = prohibitedSystemBusinessService.getActiveProhibitedSystems();
for (ProhibitedSystem prohibited : prohibitedSystems) {
    if (matchesProhibitedSystem(project, prohibited)) {
        throw new ValidationException(
            "CRITICAL: Proyecto coincide con sistema prohibido según Art. 5: " +
            prohibited.getDescription() + ". No puede ser clasificado ni desplegado."
        );
    }
}
```

**Consulta BBDD:**
```sql
SELECT ps.idxprohibitedsystem, ps.prodescription, ps.proarticlecode
FROM govprohibitedsystems ps
WHERE ps.proisactive = true
```

---

## 💼 LÓGICA DE NEGOCIO - BUSINESS SERVICES

### **1. ProjectBusinessService**

#### **1.1. Operaciones CRUD Básicas**

**Método: `createProject(Project project, String createdBy)`**
```java
/**
 * Crea un nuevo proyecto con validaciones iniciales
 *
 * Validaciones:
 * - Nombre único
 * - Código único (si aplica)
 * - Usuario creador válido
 * - Fechas válidas (inicio <= fin)
 *
 * Consulta BBDD:
 * SELECT COUNT(*) FROM prjprojects WHERE prjname = ? AND prjcode = ?
 */
public Project createProject(Project project, String createdBy) {
    // 1. Validar nombre único
    validateUniqueName(project.getPrjname());

    // 2. Validar código único (si aplica)
    if (project.getPrjcode() != null && !project.getPrjcode().isEmpty()) {
        validateUniqueCode(project.getPrjcode());
    }

    // 3. Validar fechas
    if (project.getPrjstartdate() != null && project.getPrjenddate() != null) {
        if (project.getPrjstartdate().after(project.getPrjenddate())) {
            throw new ValidationException("Fecha de inicio no puede ser posterior a fecha de fin");
        }
    }

    // 4. Establecer valores por defecto
    project.setPrjstatus("DRAFT");
    project.setPrjcreatedby(createdBy);
    project.setPrjcreatedat(new Timestamp(System.currentTimeMillis()));

    // 5. Generar UUID si no existe
    if (project.getIduuid() == null) {
        project.setIduuid(UUID.randomUUID().toString());
    }

    // 6. Inicializar campos de compliance
    project.setPrjishighrisk(false);
    project.setPrjprohibitedusechecked(false);
    project.setPrjregulatedsector(false);

    // 7. Guardar
    return noCodeClient.save(project);
}
```

**Método: `updateProject(Long projectId, Project updates, String updatedBy)`**
```java
/**
 * Actualiza un proyecto existente con validaciones
 *
 * Validaciones:
 * - Proyecto existe
 * - No está en estado CLOSED (requiere workflow)
 * - Campos críticos de compliance no pueden ser modificados si está clasificado
 *
 * Consulta BBDD:
 * SELECT * FROM prjprojects WHERE idxproject = ?
 */
public Project updateProject(Long projectId, Project updates, String updatedBy) {
    Project existing = noCodeClient.findById(Project.class, projectId);
    if (existing == null) {
        throw new EntityNotFoundException("Proyecto no encontrado: " + projectId);
    }

    // Validar que no esté cerrado
    if ("CLOSED".equals(existing.getPrjstatus())) {
        throw new BusinessException("No se puede modificar proyecto cerrado. Use workflow de reapertura.");
    }

    // Validar campos de compliance (no se pueden modificar si está clasificado)
    if (existing.getPrjishighrisk() != null && existing.getPrjishighrisk()) {
        if (updates.getPrjishighrisk() != null && !updates.getPrjishighrisk().equals(existing.getPrjishighrisk())) {
            throw new BusinessException("No se puede cambiar clasificación de alto riesgo. Use workflow de reclasificación.");
        }
    }

    // Actualizar campos permitidos
    if (updates.getPrjname() != null) {
        existing.setPrjname(updates.getPrjname());
    }
    if (updates.getPrjdescription() != null) {
        existing.setPrjdescription(updates.getPrjdescription());
    }
    // ... más campos

    existing.setPrjupdatedby(updatedBy);
    existing.setPrjupdatedat(new Timestamp(System.currentTimeMillis()));

    return noCodeClient.save(existing);
}
```

**Método: `deleteProject(Long projectId, String deletedBy)`**
```java
/**
 * Elimina un proyecto (soft delete o hard delete según estado)
 *
 * Validaciones:
 * - No tiene modelos desplegados asociados
 * - No tiene agentes en producción
 * - No tiene facturas pendientes
 * - No tiene tareas activas
 *
 * Consultas BBDD:
 * SELECT COUNT(*) FROM modmodels WHERE idxproject = ? AND modstatus = 'DEPLOYED'
 * SELECT COUNT(*) FROM prjprojecttasks WHERE idxproject = ? AND prjtaskstatus = 'ACTIVE'
 * SELECT COUNT(*) FROM prjprojectinvoices WHERE idxproject = ? AND prjinvoicestatus = 'PENDING'
 */
public void deleteProject(Long projectId, String deletedBy) {
    Project project = noCodeClient.findById(Project.class, projectId);
    if (project == null) {
        throw new EntityNotFoundException("Proyecto no encontrado: " + projectId);
    }

    // Validar modelos desplegados
    long deployedModels = countDeployedModels(projectId);
    if (deployedModels > 0) {
        throw new BusinessException(
            "No se puede eliminar proyecto con " + deployedModels + " modelos desplegados"
        );
    }

    // Validar tareas activas
    long activeTasks = countActiveTasks(projectId);
    if (activeTasks > 0) {
        throw new BusinessException(
            "No se puede eliminar proyecto con " + activeTasks + " tareas activas"
        );
    }

    // Validar facturas pendientes
    long pendingInvoices = countPendingInvoices(projectId);
    if (pendingInvoices > 0) {
        throw new BusinessException(
            "No se puede eliminar proyecto con " + pendingInvoices + " facturas pendientes"
        );
    }

    // Soft delete (marcar como eliminado)
    project.setPrjstatus("DELETED");
    project.setPrjupdatedby(deletedBy);
    project.setPrjupdatedat(new Timestamp(System.currentTimeMillis()));
    noCodeClient.save(project);
}
```

#### **1.2. Operaciones de Clasificación y Compliance**

**Método: `classifyProject(Long projectId, String category, String justification, String classifiedBy)`**
```java
/**
 * Clasifica un proyecto como alto riesgo según Art. 6 EU AI Act
 *
 * Validaciones (INC-005):
 * - Verificación contra sistemas prohibidos (Art. 5) completada
 * - No es sistema prohibido
 *
 * Consultas BBDD:
 * - Verificar prohibidos: SELECT * FROM govprohibitedsystems WHERE proisactive = true
 * - Verificar verificación: SELECT prjprohibitedusechecked FROM prjprojects WHERE idxproject = ?
 *
 * Llamada Microservicio Python (COMENTADA - PENDIENTE):
 * - codeflowx-governance-api: POST /api/v1/projects/{projectId}/classify-high-risk
 */
public Project classifyProject(Long projectId, String category, String justification, String classifiedBy) {
    Project project = noCodeClient.findById(Project.class, projectId);
    if (project == null) {
        throw new EntityNotFoundException("Proyecto no encontrado: " + projectId);
    }

    // VALIDACIÓN 1: Verificación contra sistemas prohibidos (INC-005)
    if (project.getPrjprohibitedusechecked() == null || !project.getPrjprohibitedusechecked()) {
        throw new ValidationException(
            "CRITICAL: Debe verificar contra Art. 5 (sistemas prohibidos) antes de clasificar. " +
            "Complete verificación en pestaña 'Compliance'."
        );
    }

    // VALIDACIÓN 2: Verificar automáticamente contra lista prohibida
    List<ProhibitedSystem> prohibitedSystems = prohibitedSystemBusinessService.getActiveProhibitedSystems();
    for (ProhibitedSystem prohibited : prohibitedSystems) {
        if (matchesProhibitedSystem(project, prohibited)) {
            throw new ValidationException(
                "CRITICAL: Proyecto coincide con sistema prohibido según Art. 5: " +
                prohibited.getDescription() + ". No puede ser clasificado ni desplegado."
            );
        }
    }

    // Clasificar como alto riesgo
    project.setPrjishighrisk(true);
    project.setPrjannexiiicategories(parseCategories(category)); // JSON array
    project.setPrjclassificationdate(new Timestamp(System.currentTimeMillis()));
    project.setPrjclassificationauthor(classifiedBy);
    project.setPrjriskcategoryjustification(justification);
    project.setPrjupdatedby(classifiedBy);
    project.setPrjupdatedat(new Timestamp(System.currentTimeMillis()));

    // TODO: Llamar a microservicio Python para registro en EU Database
    // PENDIENTE: Implementar cuando microservicio esté disponible
    /*
    try {
        governanceApiClient.classifyProjectAsHighRisk(projectId, category, justification);
    } catch (Exception e) {
        log.error("Error registrando clasificación en EU Database", e);
        // No fallar, pero registrar error
    }
    */

    return noCodeClient.save(project);
}
```

**Método: `verifyProhibitedSystems(Long projectId, String verifiedBy)`**
```java
/**
 * Verifica proyecto contra sistemas prohibidos (Art. 5, Anexo II)
 *
 * Consulta BBDD:
 * SELECT * FROM govprohibitedsystems WHERE proisactive = true
 */
public ProhibitedSystemCheckResult verifyProhibitedSystems(Long projectId, String verifiedBy) {
    Project project = noCodeClient.findById(Project.class, projectId);
    if (project == null) {
        throw new EntityNotFoundException("Proyecto no encontrado: " + projectId);
    }

    // Obtener sistemas prohibidos activos
    List<ProhibitedSystem> prohibitedSystems = prohibitedSystemBusinessService.getActiveProhibitedSystems();

    // Verificar contra cada sistema prohibido
    ProhibitedSystemCheckResult result = new ProhibitedSystemCheckResult();
    for (ProhibitedSystem prohibited : prohibitedSystems) {
        if (matchesProhibitedSystem(project, prohibited)) {
            result.addMatch(prohibited);
        }
    }

    // Marcar como verificado
    project.setPrjprohibitedusechecked(true);
    project.setPrjupdatedby(verifiedBy);
    project.setPrjupdatedat(new Timestamp(System.currentTimeMillis()));
    noCodeClient.save(project);

    return result;
}
```

#### **1.3. Operaciones de Gestión de Equipo**

**Método: `addProjectMember(Long projectId, Long userId, String role, String addedBy)`**
```java
/**
 * Añade un miembro al equipo del proyecto
 *
 * Validaciones:
 * - Usuario existe
 * - Rol válido
 * - No está ya en el equipo
 *
 * Consultas BBDD:
 * SELECT COUNT(*) FROM prjprojectmembers WHERE idxproject = ? AND idxuser = ?
 * INSERT INTO prjprojectmembers (...)
 */
public ProjectMember addProjectMember(Long projectId, Long userId, String role, String addedBy) {
    // Validar que no esté ya en el equipo
    long existingMembers = countProjectMembers(projectId, userId);
    if (existingMembers > 0) {
        throw new BusinessException("Usuario ya está en el equipo del proyecto");
    }

    ProjectMember member = new ProjectMember();
    member.setProject(noCodeClient.findById(Project.class, projectId));
    member.setPrjmemberrole(role);
    member.setPrjmemberaddedby(addedBy);
    member.setPrjmemberaddedat(new Timestamp(System.currentTimeMillis()));

    return noCodeClient.save(member);
}
```

**Método: `removeProjectMember(Long projectId, Long userId, String removedBy)`**
```java
/**
 * Elimina un miembro del equipo del proyecto
 *
 * Validaciones:
 * - Miembro existe
 * - No es el único administrador
 *
 * Consultas BBDD:
 * SELECT * FROM prjprojectmembers WHERE idxproject = ? AND idxuser = ?
 * SELECT COUNT(*) FROM prjprojectmembers WHERE idxproject = ? AND prjmemberrole = 'ADMIN'
 */
public void removeProjectMember(Long projectId, Long userId, String removedBy) {
    ProjectMember member = findProjectMember(projectId, userId);
    if (member == null) {
        throw new EntityNotFoundException("Miembro no encontrado en el proyecto");
    }

    // Validar que no sea el único administrador
    if ("ADMIN".equals(member.getPrjmemberrole())) {
        long adminCount = countProjectMembersByRole(projectId, "ADMIN");
        if (adminCount <= 1) {
            throw new BusinessException("No se puede eliminar el único administrador del proyecto");
        }
    }

    // Soft delete o hard delete según política
    noCodeClient.delete(member);
}
```

#### **1.4. Operaciones de Gestión Financiera**

**Método: `calculateProjectROI(Long projectId)`**
```java
/**
 * Calcula el ROI (Return on Investment) del proyecto
 *
 * Consultas BBDD:
 * SELECT SUM(prjcostamount) FROM prjprojectcosts WHERE idxproject = ?
 * SELECT SUM(prjinvoiceamount) FROM prjprojectinvoices WHERE idxproject = ? AND prjinvoicestatus = 'PAID'
 * SELECT * FROM prjprojectroi WHERE idxproject = ? ORDER BY prjroidate DESC LIMIT 1
 */
public ProjectROI calculateProjectROI(Long projectId) {
    Project project = noCodeClient.findById(Project.class, projectId);
    if (project == null) {
        throw new EntityNotFoundException("Proyecto no encontrado: " + projectId);
    }

    // Calcular costos totales
    BigDecimal totalCosts = calculateTotalCosts(projectId);

    // Calcular ingresos totales
    BigDecimal totalRevenue = calculateTotalRevenue(projectId);

    // Calcular ROI: ((Revenue - Costs) / Costs) * 100
    BigDecimal roi = BigDecimal.ZERO;
    if (totalCosts.compareTo(BigDecimal.ZERO) > 0) {
        roi = totalRevenue.subtract(totalCosts)
            .divide(totalCosts, 4, RoundingMode.HALF_UP)
            .multiply(new BigDecimal("100"));
    }

    // Guardar cálculo de ROI
    ProjectROI projectROI = new ProjectROI();
    projectROI.setProject(project);
    projectROI.setPrjroicosts(totalCosts);
    projectROI.setPrjroirevenue(totalRevenue);
    projectROI.setPrjroipercentage(roi);
    projectROI.setPrjroidate(new Timestamp(System.currentTimeMillis()));

    return noCodeClient.save(projectROI);
}
```

**Método: `estimateProjectCost(Long projectId, CostEstimationConfig config, String estimatedBy)`**
```java
/**
 * Estima costos del proyecto
 *
 * Consultas BBDD:
 * SELECT * FROM prjprojectcostestimators WHERE idxproject = ? ORDER BY prjestimatordate DESC
 * INSERT INTO prjprojectcostestimators (...)
 */
public ProjectCostEstimator estimateProjectCost(Long projectId, CostEstimationConfig config, String estimatedBy) {
    Project project = noCodeClient.findById(Project.class, projectId);
    if (project == null) {
        throw new EntityNotFoundException("Proyecto no encontrado: " + projectId);
    }

    ProjectCostEstimator estimator = new ProjectCostEstimator();
    estimator.setProject(project);
    estimator.setPrjestimatortype(config.getEstimationType());
    estimator.setPrjestimatoramount(config.getEstimatedAmount());
    estimator.setPrjestimatordate(new Timestamp(System.currentTimeMillis()));
    estimator.setPrjestimatorcreatedby(estimatedBy);

    // Calcular desglose de costos
    estimator.setPrjestimatorbreakdown(config.getBreakdown().toJson());

    return noCodeClient.save(estimator);
}
```

#### **1.5. Operaciones de Gestión de Recursos**

**Método: `trackResourceConsumption(Long projectId, ResourceConsumptionData data, String trackedBy)`**
```java
/**
 * Registra consumo de recursos del proyecto
 *
 * Consultas BBDD:
 * INSERT INTO prjprojectresourceconsumptions (...)
 * UPDATE prjprojects SET prjtotalresourceconsumption = ? WHERE idxproject = ?
 */
public ProjectResourceConsumption trackResourceConsumption(Long projectId, ResourceConsumptionData data, String trackedBy) {
    Project project = noCodeClient.findById(Project.class, projectId);
    if (project == null) {
        throw new EntityNotFoundException("Proyecto no encontrado: " + projectId);
    }

    ProjectResourceConsumption consumption = new ProjectResourceConsumption();
    consumption.setProject(project);
    consumption.setPrjresourcetype(data.getResourceType());
    consumption.setPrjresourceamount(data.getAmount());
    consumption.setPrjresourceunit(data.getUnit());
    consumption.setPrjresourcetrackedat(new Timestamp(System.currentTimeMillis()));
    consumption.setPrjresourcetrackedby(trackedBy);

    // Actualizar total de consumo del proyecto
    updateTotalResourceConsumption(projectId);

    return noCodeClient.save(consumption);
}
```

---

## 📊 CONSULTAS BBDD ESPECÍFICAS

### **1. Obtener proyectos con verificación de sistemas prohibidos pendiente**
```sql
SELECT p.idxproject, p.prjname, p.prjishighrisk, p.prjprohibitedusechecked
FROM prjprojects p
WHERE p.prjishighrisk = true
  AND (p.prjprohibitedusechecked IS NULL OR p.prjprohibitedusechecked = false)
ORDER BY p.prjcreatedat DESC;
```

### **2. Obtener proyectos con modelos desplegados**
```sql
SELECT p.idxproject, p.prjname, COUNT(m.idxmodel) AS deployed_models
FROM prjprojects p
LEFT JOIN modmodels m ON m.idxproject = p.idxproject AND m.modstatus = 'DEPLOYED'
WHERE p.prjstatus != 'DELETED'
GROUP BY p.idxproject, p.prjname
HAVING COUNT(m.idxmodel) > 0
ORDER BY deployed_models DESC;
```

### **3. Obtener miembros del equipo de un proyecto**
```sql
SELECT pm.idxprojectmember, u.idxuser, u.usuname, pm.prjmemberrole, pm.prjmemberaddedat
FROM prjprojectmembers pm
JOIN corusers u ON pm.idxuser = u.idxuser
WHERE pm.idxproject = ?
ORDER BY pm.prjmemberaddedat DESC;
```

### **4. Obtener costos totales por proyecto**
```sql
SELECT p.idxproject, p.prjname,
       COALESCE(SUM(pc.prjcostamount), 0) AS total_costs,
       COALESCE(SUM(pi.prjinvoiceamount), 0) AS total_revenue
FROM prjprojects p
LEFT JOIN prjprojectcosts pc ON pc.idxproject = p.idxproject
LEFT JOIN prjprojectinvoices pi ON pi.idxproject = p.idxproject AND pi.prjinvoicestatus = 'PAID'
WHERE p.idxproject = ?
GROUP BY p.idxproject, p.prjname;
```

### **5. Obtener consumo de recursos por tipo**
```sql
SELECT prc.prjresourcetype,
       SUM(prc.prjresourceamount) AS total_consumption,
       prc.prjresourceunit
FROM prjprojectresourceconsumptions prc
WHERE prc.idxproject = ?
GROUP BY prc.prjresourcetype, prc.prjresourceunit
ORDER BY total_consumption DESC;
```

### **6. Obtener tareas activas del proyecto**
```sql
SELECT pt.idxprojecttask, pt.prjtaskname, pt.prjtaskstatus,
       pt.prjtaskassignedto, pt.prjtaskduedate
FROM prjprojecttasks pt
WHERE pt.idxproject = ?
  AND pt.prjtaskstatus IN ('ACTIVE', 'IN_PROGRESS', 'PENDING')
ORDER BY pt.prjtaskduedate ASC;
```

---

## 🔗 INTEGRACIÓN CON MICROSERVICIOS PYTHON

> **⚠️ NOTA IMPORTANTE:** Todas las llamadas a microservicios Python están **COMENTADAS** y pendientes de implementación.
> Se deben implementar cuando los microservicios estén disponibles y operativos.

### **1. codeflowx-governance-api**

**Endpoint: `POST /api/v1/projects/{projectId}/classify-high-risk`**
```java
// TODO: PENDIENTE - Implementar cuando microservicio esté disponible
/*
public void classifyProjectAsHighRisk(Long projectId, String category, String justification) {
    String url = governanceApiBaseUrl + "/api/v1/projects/" + projectId + "/classify-high-risk";
    Map<String, Object> payload = Map.of(
        "category", category,
        "justification", justification,
        "timestamp", System.currentTimeMillis()
    );
    restTemplate.postForObject(url, payload, Void.class);
}
*/
```

### **2. Integraciones Externas (Jira, GitHub, ERP)**

**Endpoint: `POST /api/v1/integrations/jira/sync-project`**
```java
// TODO: PENDIENTE - Implementar cuando integración esté disponible
/*
public void syncProjectWithJira(Long projectId) {
    String url = integrationBaseUrl + "/api/v1/integrations/jira/sync-project";
    Map<String, Object> payload = Map.of("projectId", projectId);
    restTemplate.postForObject(url, payload, Void.class);
}
*/
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### **Fase 1: Business Service Base**
- [ ] Crear `ProjectBusinessService.java`
- [ ] Implementar operaciones CRUD básicas
- [ ] Implementar validaciones de auditoría (INC-005)
- [ ] Implementar métodos de clasificación y compliance

### **Fase 2: Gestión de Equipo y Recursos**
- [ ] Implementar gestión de miembros del equipo
- [ ] Implementar gestión de tareas
- [ ] Implementar seguimiento de recursos
- [ ] Implementar cálculo de ROI

### **Fase 3: Integración con Microservicios (PENDIENTE)**
- [ ] ⚠️ **PENDIENTE:** Configurar clientes REST para microservicios Python
- [ ] ⚠️ **PENDIENTE:** Implementar llamadas a `codeflowx-governance-api`
- [ ] ⚠️ **PENDIENTE:** Implementar integraciones externas (Jira, GitHub, ERP)
- [ ] **NOTA:** Todas las llamadas a microservicios están comentadas en el código

### **Fase 4: Consultas BBDD**
- [ ] Implementar consultas específicas de validación
- [ ] Implementar consultas de equipo y recursos
- [ ] Implementar consultas financieras

### **Fase 5: Testing y Validación**
- [ ] Crear tests unitarios para cada método
- [ ] Validar integración con microservicios
- [ ] Validar consultas BBDD
- [ ] Validar cumplimiento de auditoría

---

## 📝 NOTAS IMPORTANTES

1. **Arquitectura EnArt:** Usar `NoCodeClient` (no Repository) para acceso a datos
2. **Validaciones Críticas:** Implementar todas las validaciones de auditoría antes de permitir operaciones
3. **Microservicios Python:** ⚠️ **TODAS LAS LLAMADAS ESTÁN COMENTADAS** - Pendientes de implementar cuando microservicios estén disponibles
4. **Logging:** Registrar todas las operaciones críticas para auditoría
5. **Transacciones:** Usar `@Transactional` para operaciones que modifican múltiples entidades
6. **Entidades JPA:** Revisar entidades en `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/projects/` para campos disponibles
7. **Gestión Financiera:** Los cálculos de ROI y costos deben ser precisos y auditables

---

**Última actualización:** Diciembre 2025
**Estado:** Pendiente de implementación
