# Prompts de Migración - ZKoss a Next.js

Este directorio contiene prompts organizados para la migración de pantallas ZKoss a Next.js 14, separados por módulos y tipos de pantalla.

## Estructura de Prompts

Los prompts están organizados por:
- **Módulo** (agents, models, prompts, rag, etc.)
- **Tipo de Pantalla** (dashboards, overviews, details, forms)

## Lista de Prompts Generados

### Agents (Agentes)
- `MIGRACION_AGENTS_DASHBOARDS.md` - 1 pantalla
- `MIGRACION_AGENTS_OVERVIEWS.md` - 21 pantallas
- **Documentación Funcional:** `suinsit.nova.web/docs/funcional/agents/01_REORGANIZACION_PANTALLAS_AGENTES.md`

### Models (Modelos)
- `MIGRACION_MODELS_OVERVIEWS.md` - 13 pantallas
- **Documentación Funcional:** `suinsit.nova.web/docs/funcional/models/01_REORGANIZACION_PANTALLAS_MODELS.md`

### Prompts
- `MIGRACION_PROMPTS_OVERVIEWS.md` - 2 pantallas
- `MIGRACION_PROMPTS_FORMS.md` - 1 pantalla
- **Documentación Funcional:** `suinsit.nova.web/docs/funcional/prompts/01_REORGANIZACION_PANTALLAS_PROMPTS.md`

### RAG
- `MIGRACION_RAG_DASHBOARDS.md` - 1 pantalla
- `MIGRACION_RAG_OVERVIEWS.md` - 2 pantallas
- **Documentación Funcional:** `suinsit.nova.web/docs/funcional/rag/01_REORGANIZACION_PANTALLAS_RAG.md`

### Governance
- `MIGRACION_GOVERNANCE_OVERVIEWS.md` - 1 pantalla
- `MIGRACION_GOVERNANCE_DETAILS.md` - 1 pantalla
- **Documentación Funcional:** `suinsit.nova.web/docs/funcional/governance/01_REORGANIZACION_PANTALLAS_GOVERNANCE.md`

### Core
- `MIGRACION_CORE_DASHBOARDS.md` - 1 pantalla
- `MIGRACION_CORE_OVERVIEWS.md` - 10 pantallas
- `MIGRACION_CORE_DETAILS.md` - 7 pantallas

### Serving
- `MIGRACION_SERVING_OVERVIEWS.md` - 16 pantallas
- `MIGRACION_SERVING_DETAILS.md` - 10 pantallas
- **Documentación Funcional:** `suinsit.nova.web/docs/funcional/serving/03_PROCESOS_BPMN_SERVING.md`

### Training
- `MIGRACION_TRAINING_DASHBOARDS.md` - 2 pantallas
- `MIGRACION_TRAINING_OVERVIEWS.md` - 21 pantallas
- **Documentación Funcional:** `suinsit.nova.web/docs/funcional/training/01_REORGANIZACION_PANTALLAS_TRAINING.md`

### Projects
- `MIGRACION_PROJECTS_DASHBOARDS.md` - 1 pantalla
- `MIGRACION_PROJECTS_OVERVIEWS.md` - 27 pantallas
- `MIGRACION_PROJECTS_DETAILS.md` - 18 pantallas
- **Documentación Funcional:** `suinsit.nova.web/docs/funcional/projects/01_REORGANIZACION_PANTALLAS_PROJECTS.md`

### Monitoring
- `MIGRACION_MONITORING_OVERVIEWS.md` - 8 pantallas
- `MIGRACION_MONITORING_DETAILS.md` - 7 pantallas
- `MIGRACION_MONITORING_FORMS.md` - 3 pantallas
- **Documentación Funcional:** `suinsit.nova.web/docs/funcional/monitoring/01_REORGANIZACION_PANTALLAS_MONITORING.md`

### Analytics
- `MIGRACION_ANALYTICS_DASHBOARDS.md` - 1 pantalla
- `MIGRACION_ANALYTICS_OVERVIEWS.md` - 3 pantallas
- `MIGRACION_ANALYTICS_DETAILS.md` - 2 pantallas

### Infrastructure
- `MIGRACION_INFRASTRUCTURE_DASHBOARDS.md` - 1 pantalla
- `MIGRACION_INFRASTRUCTURE_OVERVIEWS.md` - 17 pantallas
- `MIGRACION_INFRASTRUCTURE_DETAILS.md` - 12 pantallas

### Providers
- `MIGRACION_PROVIDERS_OVERVIEWS.md` - 2 pantallas

### Compliance
- `MIGRACION_COMPLIANCE_DASHBOARDS.md` - 3 pantallas
- `MIGRACION_COMPLIANCE_FORMS.md` - 2 pantallas
- `MIGRACION_COMPLIANCE_CLASSIFICATION.md` - 1 pantalla (Art. 6)
- `MIGRACION_COMPLIANCE_FRIA.md` - 1 pantalla (Art. 27)
- `MIGRACION_COMPLIANCE_EU_REGISTRATION.md` - 1 pantalla (Art. 49)
- `MIGRACION_COMPLIANCE_IMMUTABLE_LOGS.md` - 1 pantalla (Art. 19)
- `MIGRACION_COMPLIANCE_TRACEABILITY.md` - 1 pantalla (Art. 12)
- `MIGRACION_COMPLIANCE_QMS.md` - 1 pantalla (Art. 17)
- `MIGRACION_COMPLIANCE_TECHNICAL_DOCS.md` - 1 pantalla (Art. 11)
- `MIGRACION_COMPLIANCE_HITL.md` - 1 pantalla (Art. 14)
- `PROMPT_COMPLIANCE_SECTOR.md` - 1 pantalla (Anexo I - Sectores Regulados)
- **Documentación Funcional:** `suinsit.nova.web/docs/funcional/compliance/01_REORGANIZACION_PANTALLAS_COMPLIANCE.md`
- **Total Compliance:** 13 pantallas (3 dashboards + 2 forms + 8 específicas EU AI Act)

## Total

**40 prompts generados** cubriendo todos los módulos principales (excluyendo BPMN que ya está migrado).

**Compliance:** 13 pantallas (10 prompts) - Incluye todas las pantallas específicas del EU AI Act

---

## 📋 PROMPTS DE LÓGICA DE NEGOCIO

**Fecha:** Diciembre 2025
**Objetivo:** Definir la lógica de negocio completa para cada módulo con operaciones CRUD, validaciones de auditoría, consultas BBDD y llamadas a microservicios Python (comentadas)

### **Prompts de Lógica de Negocio Creados:**

1. **`BUSINESS_LOGIC_MODELS.md`** - Módulo Models (Modelos de IA)
   - Operaciones CRUD de modelos
   - Clasificación y compliance (Art. 6, 11, 15, 51)
   - Evaluación de modelos
   - Gestión de dependencias
   - Validaciones: INC-001, INC-003, INC-005

2. **`BUSINESS_LOGIC_PROJECTS.md`** - Módulo Projects (Proyectos)
   - Operaciones CRUD de proyectos
   - Clasificación y compliance
   - Gestión de equipo y recursos
   - Gestión financiera (ROI, costos)
   - Validaciones: INC-005

3. **`BUSINESS_LOGIC_AGENTS.md`** - Módulo Agents (Agentes de IA)
   - Operaciones CRUD de agentes
   - Aprobación y rechazo de agentes
   - Monitoreo de interacciones y decisiones
   - Evaluación ética
   - Versionado y rollback
   - Validaciones: INC-005

4. **`BUSINESS_LOGIC_COMPLIANCE.md`** - Módulo Compliance (Cumplimiento EU AI Act)
   - Gestión de FRIAs (Art. 27)
   - Evaluaciones de conformidad (Art. 43, Anexo VI)
   - Registro en Base de Datos UE (Art. 49)
   - Cálculo de riesgo (Anexo IX)
   - Validaciones: INC-007, INC-008

5. **`BUSINESS_LOGIC_TRAINING.md`** - Módulo Training (Entrenamiento)
   - Gestión de experimentos y runs
   - Registro de métricas y artefactos
   - Cálculo de costos de entrenamiento
   - Integración con MLflow (comentada)

### **Características Comunes de los Prompts:**

- ✅ **Operaciones CRUD completas** con validaciones
- ✅ **Consultas BBDD específicas** documentadas con SQL
- ✅ **Validaciones de auditoría** según incidencias identificadas
- ✅ **Llamadas a microservicios Python** todas comentadas y marcadas como pendientes
- ✅ **Checklist de implementación** por fases
- ✅ **Referencias a prompts Java** y documentación de auditoría
- ✅ **Notas importantes** sobre arquitectura y mejores prácticas

### **Estado:**
- ✅ **5 prompts creados** y pusheados
- ⏳ **Pendientes:** Serving, Governance, Core, RAG, Prompts, Infrastructure, Analytics

## Estructura de Cada Prompt

Cada prompt incluye:

1. **Contexto del Módulo**
   - Tipo de pantallas
   - Total de pantallas
   - Documentación funcional asociada

2. **Arquitectura del Módulo**
   - ViewModels identificados con servicios y entidades usadas
   - Servicios backend disponibles
   - Entidades JPA
   - Microservicios Java disponibles

3. **Pantallas a Migrar**
   - Lista de archivos ZUL
   - ViewModels asociados
   - Servicios backend
   - Entidades JPA

4. **Estrategia de Migración**
   - Patrones por tipo de pantalla (dashboard, overview, detail, form)
   - Configuración de mock centralizado
   - Mapeo de servicios backend → API routes
   - Checklist de migración

5. **Ejemplo de Migración**
   - Estructura de archivos
   - Mock data
   - Desactivación de mock

## Uso de los Prompts

### Para Migración en Paralelo

1. **Asignar prompts a diferentes agentes/desarrolladores**
   - Cada prompt es independiente y puede trabajarse en paralelo
   - Los prompts están organizados por módulo y tipo para facilitar la distribución

2. **Seguir la estrategia de mock**
   - Implementar mock data desde el inicio
   - Usar variable de entorno `NEXT_PUBLIC_USE_MOCK=true`
   - Cuando esté operativo, solo cambiar a `false`

3. **Identificar componentes backend**
   - ViewModel Java → Identificar servicios inyectados
   - Servicios → Identificar entidades JPA usadas
   - Microservicios → Identificar clientes Java disponibles

4. **Documentar dependencias**
   - Cada pantalla debe documentar:
     - ViewModel asociado
     - Servicios backend usados
     - Entidades JPA
     - Microservicios (si aplica)

### Desactivación de Mock

Cuando la migración esté completa y se quiera poner operativa:

```bash
# Solo cambiar esta variable en .env.local
NEXT_PUBLIC_USE_MOCK=false
```

El código debe estar preparado para usar APIs reales automáticamente sin revisar cada pantalla.

## Script de Generación

Los prompts fueron generados automáticamente usando:
- `scripts/generate_migration_prompts.py`

Para regenerar los prompts:
```bash
python3 scripts/generate_migration_prompts.py
```

## Notas Importantes

1. **NO migrar formularios BPMN** - Ya están migrados (ver `BPMN_FORM_MIGRATION.md`)
2. **Mantener estructura de carpetas** según documentación funcional
3. **Usar mock data consistente** para facilitar testing
4. **Documentar servicios backend** para integración futura
5. **Identificar todas las dependencias** antes de migrar

---

**Última actualización:** Diciembre 2025
**Total de pantallas a migrar:** ~200+ (excluyendo BPMN)
