#!/usr/bin/env python3
"""
Script para generar documentos de migración de ViewModels agrupados por módulos.
Cada documento contendrá entre 25-50 ViewModels para trabajo paralelo.
"""

import re
from collections import defaultdict
from pathlib import Path

# Leer el reporte
REPORT_FILE = Path("viewmodels_validation_report.txt")
OUTPUT_DIR = Path("docs/migration/prompts")
OUTPUT_DIR.mkdir(parents=True, exist_ok=True)

TEMPLATE_HEADER = """# Migración de ViewModels a Servicios Dedicados - {module_name}

## Información del Documento
- **Módulo**: {module_name}
- **Total ViewModels**: {total_vms}
- **Documento**: {doc_num} de {total_docs}
- **ViewModels en este documento**: {vm_count}

## Instrucciones de Migración

### 1. Objetivo
Migrar los ViewModels listados para que usen servicios dedicados en lugar de `businessService` directamente.

### 2. Contexto
Se han creado 333 servicios dedicados para todas las entidades y views del sistema. Los ViewModels deben usar estos servicios en lugar de llamar directamente a `businessService`.

### 2.1. Ubicación de Proyectos

**Servicios (Services)**:
- **Proyecto**: `nocode.service/codeflowx.govern.services`
- **Ubicación**: `/mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service/codeflowx.govern.services/`
- **Paquete**: `com.codeflowx.govern.service.[module].[EntityName]Service`
- **Ejemplo**: `com.codeflowx.govern.service.models.ModelService`
- **Total**: 333 servicios (220 entidades + 113 views)

**Entidades JPA**:
- **Proyecto**: `nocode.service/nocode.service.entitys`
- **Ubicación**: `/mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service/nocode.service.entitys/`
- **Paquete**: `com.codeflowx.govern.entity.[module].[EntityName]`
- **Ejemplo**: `com.codeflowx.govern.entity.models.Model`
- **Views**: `com.codeflowx.govern.entity.views.[module].[ViewName]`
- **Ejemplo**: `com.codeflowx.govern.entity.views.training.HpoProgressDashboard`

**ViewModels**:
- **Proyecto**: `suinsit.nova.web`
- **Ubicación**: `/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/`
- **Paquete**: `com.codeflowx.govern.viewmodel.[module]/[EntityName]ViewModel.java`
- **Ejemplo**: `com.codeflowx.govern.viewmodel.agents.AgentsDetailViewModel`

**Pantallas ZUL**:
- **Proyecto**: `suinsit.nova.web`
- **Ubicación**: `src/main/webapp/console/platform/[module]/[entity]/`
- **Ejemplos**:
  - `src/main/webapp/console/platform/agents/overview/page.zul`
  - `src/main/webapp/console/platform/models/detail/page.zul`
  - `src/main/webapp/console/governance/compliance/page.zul`

### 3. Pasos de Migración

#### Paso 1: Identificar el Servicio Correcto
- Para entidades: `EntityName` → `EntityNameService`
- Para views: `ViewName` → `ViewNameService` (**TODAS las views tienen servicio - 113 servicios creados**)
- Los servicios están en: `com.codeflowx.govern.service.*`
- **IMPORTANTE**: Todas las views tienen servicios dedicados (113 servicios de views creados)

#### Paso 2: Inyectar el Servicio
```java
@WireVariable
private EntityNameService entityNameService;
```

#### Paso 3: Reemplazar Llamadas

**Antes (INCORRECTO):**
```java
// findAllEntity
PageResult<Entity> result = businessService.findAllEntity(Entity.class, pageParams, criterias);

// findById
Entity entity = businessService.findById(Entity.class, id);

// save
Entity saved = businessService.save(entity);

// update
Entity updated = businessService.update(entity);

// removeFromID
boolean deleted = businessService.removeFromID(Entity.class, id);

// findAllView (SIEMPRE usar servicio de view si existe)
List<View> views = businessService.findAllView(View.class);
PageResult<View> result = businessService.findAllView(View.class, pageParams, criterias);
```

**Después (CORRECTO):**
```java
// findAllEntity → findAll
PageResult<Entity> result = entityService.findAll(pageParams, criterias);

// findById → findById
Entity entity = entityService.findById(id);

// save → create
Entity saved = entityService.create(entity);

// update → update
Entity updated = entityService.update(entity);

// removeFromID → deleteById
boolean deleted = entityService.deleteById(id);

// findAllView → findAll (para views) - TODAS las views tienen servicio
List<View> views = viewService.findAll();
PageResult<View> result = viewService.findAll(pageParams, criterias);
// NOTA: Todas las views tienen servicios dedicados (113 servicios de views creados)
```

#### Paso 4: Manejo de Excepciones
Todos los servicios lanzan `GovernanceServiceException` (checked exception):
```java
try {{
    Entity entity = entityService.findById(id);
}} catch (GovernanceServiceException e) {{
    log.error("Error al buscar entidad", e);
    // Manejar error
}}
```

#### Paso 5: Imports Necesarios
```java
import com.codeflowx.govern.service.exception.GovernanceServiceException;
import com.codeflowx.govern.service.[module].[EntityName]Service;
```

### 4. Métodos Disponibles en Servicios

#### Para Entidades (CRUD completo):
- `create(Entity entity) throws GovernanceServiceException`
- `update(Entity entity) throws GovernanceServiceException`
- `saveOrUpdate(Entity entity) throws GovernanceServiceException`
- `findById(Long id) throws GovernanceServiceException`
- `deleteById(Long id) throws GovernanceServiceException`
- `delete(Entity entity) throws GovernanceServiceException`
- `findAll(PageParams pageParams, Criterias criterias) throws GovernanceServiceException`
- `findAll(PageParams pageParams) throws GovernanceServiceException`
- `findAll() throws GovernanceServiceException`

#### Para Views (solo lectura):
- `findAll(PageParams pageParams, Criterias criterias) throws GovernanceServiceException`
- `findAll(PageParams pageParams) throws GovernanceServiceException`
- `findAll() throws GovernanceServiceException`

### 5. Verificación Post-Migración
- ✅ Eliminar todas las referencias a `businessService.findAllEntity`, `businessService.findById`, etc.
- ✅ Verificar que los imports del servicio sean correctos
- ✅ Verificar que se manejen las excepciones `GovernanceServiceException`
- ✅ Compilar y verificar que no hay errores
- ✅ Probar funcionalidad básica del ViewModel

### 5.1. Revisión de Entidades y Servicios

Antes de migrar, **revisar** las entidades y servicios correspondientes:

1. **Revisar la Entidad JPA**:
   - Ubicación: `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/[module]/[EntityName].java`
   - Verificar campos: ID, createdat, updatedat, name
   - Verificar getters/setters correctos

2. **Revisar el Servicio**:
   - Ubicación: `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/[module]/[EntityName]Service.java`
   - Verificar métodos disponibles
   - Verificar manejo de excepciones

3. **Para Views**:
   - Ubicación entidad: `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/views/[module]/[ViewName].java`
   - Ubicación servicio: `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/[module]/[ViewName]Service.java`
   - Verificar que solo tiene métodos `findAll()` (sin CRUD)

### 5.2. Revisión de Pantallas ZUL

**IMPORTANTE**: Después de migrar el ViewModel, **revisar la pantalla ZUL asociada**:

1. **Localizar el archivo ZUL**:
   - Buscar en: `suinsit.nova.web/src/main/webapp/console/platform/[module]/`
   - O en: `suinsit.nova.web/src/main/webapp/console/governance/[module]/`
   - O en: `suinsit.nova.web/src/main/webapp/console/gobierno/[module]/`
   - Patrones comunes:
     - `platform/[module]/overview/page.zul` - Para ViewModels Overview
     - `platform/[module]/detail/page.zul` - Para ViewModels Detail
     - `platform/[module]/[entity]/overview.zul` - Para ViewModels específicos
     - `platform/[module]/[entity]/page.zul` - Para ViewModels específicos
     - `governance/[module]/page.zul` - Para ViewModels de gobernanza
     - `gobierno/[module]/[entity]-overview.zul` - Para ViewModels en español
     - `gobierno/[module]/[entity]-detail.zul` - Para ViewModels detail en español
   - **Ejemplos reales**:
     - `AgentsDetailViewModel` → `platform/agents/detail/page.zul` o `gobierno/agents/agents-detail.zul`
     - `AgentsOverviewViewModel` → `platform/agents/overview/page.zul` o `gobierno/agents/agents-overview.zul`
     - `ModelBiasAnalysisViewModel` → `platform/evaluation/model-bias-analysis/overview.zul`

2. **Verificar en el ZUL**:
   - ✅ Que los bindings a métodos del ViewModel sigan funcionando
   - ✅ Que los eventos `@Command` estén correctamente vinculados
   - ✅ Que los datos se muestren correctamente después de la migración
   - ✅ Que la paginación funcione correctamente
   - ✅ Que los filtros sigan funcionando

3. **Probar la pantalla**:
   - Ejecutar la aplicación
   - Navegar a la pantalla ZUL
   - Verificar que carga datos correctamente
   - Probar operaciones CRUD si aplica
   - Verificar que no hay errores en consola

### 6. Ejemplo Completo

**Antes:**
```java
@WireVariable
private BusinessService businessService;

public void loadData() {{
    try {{
        PageParams pageParams = PageParams.builder()
            .maxRows(20)
            .pageActual(1)
            .rowActual(0)
            .build();

        PageResult<Model> result = businessService.findAllEntity(
            Model.class,
            pageParams,
            new Criterias()
        );

        this.models = result.getContent();
    }} catch (Exception e) {{
        log.error("Error", e);
    }}
}}
```

**Después:**
```java
@WireVariable
private ModelService modelService;

public void loadData() {{
    try {{
        PageParams pageParams = PageParams.builder()
            .maxRows(20)
            .pageActual(1)
            .rowActual(0)
            .build();

        PageResult<Model> result = modelService.findAll(pageParams);

        this.models = result.getContent();
    }} catch (GovernanceServiceException e) {{
        log.error("Error al cargar modelos", e);
    }}
}}
```

### 7. Notas Importantes sobre Preservación de Lógica de Negocio

**CRÍTICO**: Los ViewModels pueden tener lógica de negocio compleja que DEBE mantenerse:

1. **Llamadas a Múltiples Servicios Relacionados**:
   - Si un ViewModel necesita datos de múltiples entidades, puede inyectar múltiples servicios
   - Ejemplo: `ModelService`, `ModelVersionService`, `ModelPerformanceService`
   - Mantener todas las llamadas a servicios relacionados

2. **Uso de Views**:
   - **TODAS las views tienen servicios dedicados** (113 servicios de views creados)
   - **SIEMPRE usar el servicio de view** en lugar de `businessService.findAllView()`
   - Los servicios de views están en los mismos paquetes que las entidades relacionadas
   - Ejemplo: `HpoProgressDashboard` → `HpoProgressDashboardService` en `com.codeflowx.govern.service.training`

3. **Procedimientos Almacenados**:
   - Si el ViewModel llama a procedimientos almacenados, **MANTENER** `businessService` para esas llamadas
   - Los procedimientos no tienen servicios dedicados
   - Ejemplo: `businessService.executeProcedure("sp_name", params)` se mantiene

4. **Microservicios Python**:
   - **MANTENER** las llamadas a microservicios Python sin cambios
   - Solo migrar las llamadas a `businessService` para entidades/views
   - Ejemplo: `pythonServiceClient.call("/api/endpoint", request)` se mantiene

5. **Transformaciones y Validaciones**:
   - Mantener toda la lógica de transformación de datos
   - Mantener todas las validaciones de negocio
   - Mantener reglas de negocio específicas del ViewModel

6. **No eliminar `businessService` completamente**:
   - Mantenerlo si se usa para procedimientos almacenados
   - Mantenerlo si se usa para entidades sin servicio dedicado
   - Mantenerlo si se usa para operaciones especiales no cubiertas por servicios

7. **Testing Post-Migración**:
   - Verificar que toda la funcionalidad sigue funcionando correctamente
   - Probar llamadas a servicios relacionados
   - Probar uso de views
   - Probar procedimientos almacenados
   - Probar integraciones con microservicios Python

---

## ViewModels a Migrar

"""

TEMPLATE_VM_ITEM = """
### {vm_name}

**Archivo**: `{vm_path}`

**Problemas encontrados**:
{issues}

**Servicios a usar**:
{services}

---

"""

def extract_module_from_path(path):
    """Extrae el módulo del path del ViewModel"""
    # Patrón: src/main/java/com/codeflowx/govern/viewmodel/[module]/...
    # o: src/main/java/com/codeflowx/platform/viewmodel/[module]/...
    match = re.search(r'/viewmodel/([^/]+)/', path)
    if match:
        return match.group(1)
    return "root"

def parse_report():
    """Parsea el reporte y extrae ViewModels con problemas"""
    viewmodels = []
    current_vm = None
    current_issues = []

    try:
        with open(REPORT_FILE, 'r', encoding='utf-8', errors='ignore') as f:
            content = f.read()
    except Exception as e:
        print(f"Error leyendo reporte: {e}")
        return []

    for line in content.split('\n'):
        line = line.strip()

        # Nueva entrada de ViewModel
        if line.startswith('src/main/java/'):
            if current_vm:
                viewmodels.append((current_vm, current_issues))
            current_vm = line
            current_issues = []

        # Issue encontrado
        elif line.startswith('Línea') and 'businessService' in line:
            current_issues.append(line)

        # Servicio esperado
        elif '-> Debería usar:' in line:
            current_issues.append(line)

    if current_vm:
        viewmodels.append((current_vm, current_issues))

    return viewmodels

def group_by_module(viewmodels):
    """Agrupa ViewModels por módulo"""
    modules = defaultdict(list)

    for vm_path, issues in viewmodels:
        module = extract_module_from_path(vm_path)
        vm_name = Path(vm_path).stem
        modules[module].append({
            'name': vm_name,
            'path': vm_path,
            'issues': issues
        })

    return modules

def extract_services_from_issues(issues):
    """Extrae los servicios mencionados en los issues"""
    services = set()
    for issue in issues:
        if '-> Debería usar:' in issue:
            service = issue.split('-> Debería usar:')[-1].strip()
            if service and service != '(NO EXISTE)':
                services.add(service)
    return sorted(services)

def create_documents(modules):
    """Crea documentos agrupados por módulo, con 25-50 ViewModels cada uno"""
    all_docs = []

    for module, vms in sorted(modules.items()):
        # Dividir en chunks de 25-50
        min_size = 25
        max_size = 50
        target_size = 40

        chunks = []
        current_chunk = []

        for vm in vms:
            current_chunk.append(vm)

            # Si el chunk alcanza el tamaño objetivo, crear nuevo chunk
            if len(current_chunk) >= target_size:
                chunks.append(current_chunk)
                current_chunk = []

        # Agregar último chunk si tiene elementos
        if current_chunk:
            # Si el último chunk es muy pequeño y hay chunks anteriores, combinarlo
            if len(chunks) > 0 and len(current_chunk) < min_size:
                chunks[-1].extend(current_chunk)
            else:
                chunks.append(current_chunk)

        # Asegurar que ningún chunk sea muy pequeño (combinar con el anterior)
        final_chunks = []
        for chunk in chunks:
            if len(final_chunks) > 0 and len(chunk) < min_size and len(final_chunks[-1]) + len(chunk) <= max_size:
                final_chunks[-1].extend(chunk)
            else:
                final_chunks.append(chunk)

        chunks = final_chunks

        for doc_num, chunk in enumerate(chunks, 1):
            total_docs = len(chunks)
            module_display = module.replace('_', ' ').title() if module != 'root' else 'Root'

            doc_content = TEMPLATE_HEADER.format(
                module_name=module_display,
                total_vms=len(vms),
                doc_num=doc_num,
                total_docs=total_docs,
                vm_count=len(chunk)
            )

            # Agregar ViewModels
            for vm in chunk:
                services = extract_services_from_issues(vm['issues'])
                services_list = '\n'.join(f'- `{s}`' for s in services) if services else '- *Verificar servicio en el código*'

                issues_text = '\n'.join(f'- {issue}' for issue in vm['issues'][:10])  # Máximo 10 issues
                if len(vm['issues']) > 10:
                    issues_text += f'\n- ... y {len(vm["issues"]) - 10} más'

                doc_content += TEMPLATE_VM_ITEM.format(
                    vm_name=vm['name'],
                    vm_path=vm['path'],
                    issues=issues_text,
                    services=services_list
                )

            # Guardar documento
            filename = f"MIGRACION_{module.upper()}_{doc_num:02d}.md"
            if module == 'root':
                filename = f"MIGRACION_ROOT_{doc_num:02d}.md"

            filepath = OUTPUT_DIR / filename
            with open(filepath, 'w', encoding='utf-8') as f:
                f.write(doc_content)

            all_docs.append({
                'module': module,
                'doc_num': doc_num,
                'filename': filename,
                'vm_count': len(chunk)
            })

            print(f"Creado: {filename} ({len(chunk)} ViewModels)")

    return all_docs

def create_index(docs):
    """Crea un índice de todos los documentos"""
    index_content = """# Índice de Documentos de Migración de ViewModels

Este directorio contiene documentos para migrar ViewModels a usar servicios dedicados.
Cada documento contiene entre 25-50 ViewModels agrupados por módulo para trabajo paralelo.

## Documentos Disponibles

"""

    # Agrupar por módulo
    by_module = defaultdict(list)
    for doc in docs:
        by_module[doc['module']].append(doc)

    for module, module_docs in sorted(by_module.items()):
        module_display = module.replace('_', ' ').title() if module != 'root' else 'Root'
        index_content += f"\n### {module_display}\n\n"

        for doc in sorted(module_docs, key=lambda x: x['doc_num']):
            index_content += f"- [{doc['filename']}]({doc['filename']}) - {doc['vm_count']} ViewModels\n"

    index_content += """
## Instrucciones Generales

1. Cada agente debe trabajar en un documento diferente
2. Seguir las instrucciones detalladas en cada documento
3. Verificar compilación después de cada migración
4. Marcar como completado cuando termine

## Plantilla de Instrucciones

Ver [PROMPT_TEMPLATE_MIGRACION_VIEWMODELS.md](../PROMPT_TEMPLATE_MIGRACION_VIEWMODELS.md) para instrucciones detalladas.
"""

    with open(OUTPUT_DIR / "README.md", 'w', encoding='utf-8') as f:
        f.write(index_content)

    print(f"\nCreado índice: README.md")

def main():
    print("=" * 80)
    print("GENERACIÓN DE DOCUMENTOS DE MIGRACIÓN")
    print("=" * 80)
    print()

    # Parsear reporte
    print("Parseando reporte...")
    viewmodels = parse_report()
    print(f"Encontrados {len(viewmodels)} ViewModels con problemas")
    print()

    # Agrupar por módulo
    print("Agrupando por módulos...")
    modules = group_by_module(viewmodels)
    print(f"Encontrados {len(modules)} módulos:")
    for module, vms in sorted(modules.items()):
        print(f"  - {module}: {len(vms)} ViewModels")
    print()

    # Crear documentos
    print("Creando documentos...")
    docs = create_documents(modules)
    print()

    # Crear índice
    print("Creando índice...")
    create_index(docs)
    print()

    print("=" * 80)
    print(f"✅ Completado: {len(docs)} documentos creados")
    print(f"📁 Ubicación: {OUTPUT_DIR}")
    print("=" * 80)

if __name__ == "__main__":
    main()
