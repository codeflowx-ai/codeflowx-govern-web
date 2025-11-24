# Índice de Documentos de Migración de ViewModels

Este directorio contiene documentos para migrar ViewModels a usar servicios dedicados.
Cada documento contiene entre 25-50 ViewModels agrupados por módulo para trabajo paralelo.

## Documentos Disponibles


### Agents

- [MIGRACION_AGENTS_01.md](MIGRACION_AGENTS_01.md) - 56 ViewModels

### Analytics

- [MIGRACION_ANALYTICS_01.md](MIGRACION_ANALYTICS_01.md) - 11 ViewModels

### Artefacto

- [MIGRACION_ARTEFACTO_01.md](MIGRACION_ARTEFACTO_01.md) - 6 ViewModels

### Core

- [MIGRACION_CORE_01.md](MIGRACION_CORE_01.md) - 23 ViewModels

### Dashboard

- [MIGRACION_DASHBOARD_01.md](MIGRACION_DASHBOARD_01.md) - 15 ViewModels

### Datasources

- [MIGRACION_DATASOURCES_01.md](MIGRACION_DATASOURCES_01.md) - 10 ViewModels

### Domainingestion

- [MIGRACION_DOMAININGESTION_01.md](MIGRACION_DOMAININGESTION_01.md) - 7 ViewModels

### Evaluation

- [MIGRACION_EVALUATION_01.md](MIGRACION_EVALUATION_01.md) - 18 ViewModels

### Governance

- [MIGRACION_GOVERNANCE_01.md](MIGRACION_GOVERNANCE_01.md) - 41 ViewModels

### Infrastructure

- [MIGRACION_INFRASTRUCTURE_01.md](MIGRACION_INFRASTRUCTURE_01.md) - 31 ViewModels

### Models

- [MIGRACION_MODELS_01.md](MIGRACION_MODELS_01.md) - 30 ViewModels

### Monitoring

- [MIGRACION_MONITORING_01.md](MIGRACION_MONITORING_01.md) - 17 ViewModels

### Notifications

- [MIGRACION_NOTIFICATIONS_01.md](MIGRACION_NOTIFICATIONS_01.md) - 10 ViewModels

### Platform

- [MIGRACION_PLATFORM_01.md](MIGRACION_PLATFORM_01.md) - 12 ViewModels

### Playground

- [MIGRACION_PLAYGROUND_01.md](MIGRACION_PLAYGROUND_01.md) - 7 ViewModels

### Projects

- [MIGRACION_PROJECTS_01.md](MIGRACION_PROJECTS_01.md) - 47 ViewModels

### Prompts

- [MIGRACION_PROMPTS_01.md](MIGRACION_PROMPTS_01.md) - 15 ViewModels

### Providers

- [MIGRACION_PROVIDERS_01.md](MIGRACION_PROVIDERS_01.md) - 3 ViewModels

### Rag

- [MIGRACION_RAG_01.md](MIGRACION_RAG_01.md) - 23 ViewModels

### Root

- [MIGRACION_ROOT_01.md](MIGRACION_ROOT_01.md) - 2 ViewModels

### Serving

- [MIGRACION_SERVING_01.md](MIGRACION_SERVING_01.md) - 29 ViewModels

### Training

- [MIGRACION_TRAINING_01.md](MIGRACION_TRAINING_01.md) - 54 ViewModels

## Instrucciones Generales

1. Cada agente debe trabajar en un documento diferente
2. Seguir las instrucciones detalladas en cada documento
3. Verificar compilación después de cada migración
4. Marcar como completado cuando termine

## Ubicación de Proyectos

### Servicios (Services)
- **Proyecto**: `nocode.service/codeflowx.govern.services`
- **Ubicación**: `/mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service/codeflowx.govern.services/`
- **Paquete**: `com.codeflowx.govern.service.[module].[EntityName]Service`
- **Total**: 333 servicios (220 entidades + 113 views)

### Entidades JPA
- **Proyecto**: `nocode.service/nocode.service.entitys`
- **Ubicación**: `/mnt/c/Users/ManuelGonzalez/eclipse-workspace/nocode.service/nocode.service.entitys/`
- **Paquete**: `com.codeflowx.govern.entity.[module].[EntityName]`
- **Views**: `com.codeflowx.govern.entity.views.[module].[ViewName]`

### ViewModels
- **Proyecto**: `suinsit.nova.web`
- **Ubicación**: `/mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/`
- **Paquete**: `com.codeflowx.govern.viewmodel.[module]/[EntityName]ViewModel.java`

### Pantallas ZUL
- **Proyecto**: `suinsit.nova.web`
- **Ubicación**: `src/main/webapp/console/platform/[module]/` o `src/main/webapp/console/governance/[module]/` o `src/main/webapp/console/gobierno/[module]/`
- **Patrones**: `overview/page.zul`, `detail/page.zul`, `[entity]/page.zul`

## Plantilla de Instrucciones

Ver [PROMPT_TEMPLATE_MIGRACION_VIEWMODELS.md](../PROMPT_TEMPLATE_MIGRACION_VIEWMODELS.md) para instrucciones detalladas.
