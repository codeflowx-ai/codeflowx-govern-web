# 🏗️ ARQUITECTURA Y FLUJOS - MÓDULO CLASIFICACIÓN

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Módulo:** Compliance - Classification (Art. 6 + Anexo III EU AI Act)

---

## 📋 ÍNDICE

1. [Arquitectura de Componentes](#arquitectura-de-componentes)
2. [Diagrama de Capas](#diagrama-de-capas)
3. [Flujo de Clasificación Completo](#flujo-de-clasificación-completo)
4. [Flujo de Obtención de Categorías](#flujo-de-obtención-de-categorías)
5. [Flujo de Sugerencia con IA](#flujo-de-sugerencia-con-ia)
6. [Integraciones con Otros Módulos](#integraciones-con-otros-módulos)
7. [Flujo de Validaciones](#flujo-de-validaciones)

---

## 🏗️ ARQUITECTURA DE COMPONENTES

### Diagrama de Componentes

```
┌─────────────────────────────────────────────────────────────────┐
│                        FRONTEND (Next.js)                        │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │  Classification Page                                        │ │
│  │  - Clasificador de Alto Riesgo                             │ │
│  │  - Catálogo Categorías Anexo III                           │ │
│  │  - Lista Proyectos Clasificados                             │ │
│  └───────────────────────────────────────────────────────────┘ │
└──────────────────────────────┬──────────────────────────────────┘
                                │ HTTP REST
                                ↓
┌─────────────────────────────────────────────────────────────────┐
│                    BFF (Backend for Frontend)                    │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │  ClassificationController                                  │ │
│  │  - POST /api/compliance/classification/classify           │ │
│  │  - GET  /api/compliance/classification/categories         │ │
│  │  - GET  /api/compliance/classification/projects           │ │
│  │  - POST /api/compliance/classification/suggest            │ │
│  └───────────────────────────────────────────────────────────┘ │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │  ClassificationServiceImpl                                 │ │
│  │  - Agregación de datos                                     │ │
│  │  - Circuit Breaker / Retry                                 │ │
│  │  - Disparo workflows BPMN                                  │ │
│  └───────────────────────────────────────────────────────────┘ │
└──────────────────────────────┬──────────────────────────────────┘
                                │
                ┌───────────────┴───────────────┐
                ↓                               ↓
┌───────────────────────────────┐  ┌───────────────────────────────┐
│   Business Services Layer     │  │  Classification Microservice  │
│  ┌─────────────────────────┐ │  │  (Opcional)                   │
│  │ HighRiskClassifier      │ │  │  ┌─────────────────────────┐ │
│  │ BusinessService         │ │  │  │ ClassificationController│ │
│  └─────────────────────────┘ │  │  └─────────────────────────┘ │
│  ┌─────────────────────────┐ │  │  ┌─────────────────────────┐ │
│  │ AnnexIIICategory        │ │  │  │ AIClassificationService │ │
│  │ BusinessService         │ │  │  └─────────────────────────┘ │
│  └─────────────────────────┘ │  └───────────────────────────────┘
│  ┌─────────────────────────┐ │
│  │ ProhibitedSystem         │ │
│  │ BusinessService          │ │
│  └─────────────────────────┘ │
└───────────────────────────────┘
                ↓
┌───────────────────────────────┐
│     Repositories Layer         │
│  ┌─────────────────────────┐  │
│  │ ProjectRepository       │  │
│  └─────────────────────────┘  │
│  ┌─────────────────────────┐  │
│  │ AnnexIIICategory        │  │
│  │ Repository              │  │
│  └─────────────────────────┘  │
│  ┌─────────────────────────┐  │
│  │ ProhibitedSystem        │  │
│  │ Repository              │  │
│  └─────────────────────────┘  │
└───────────────────────────────┘
                ↓
┌───────────────────────────────┐
│      Database (PostgreSQL)    │
│  ┌─────────────────────────┐  │
│  │ PRJPROJECTS             │  │
│  └─────────────────────────┘  │
│  ┌─────────────────────────┐  │
│  │ ANNANNEXIIICATEGORIES   │  │
│  └─────────────────────────┘  │
│  ┌─────────────────────────┐  │
│  │ GOVPROHIBITEDSYSTEMS     │  │
│  └─────────────────────────┘  │
└───────────────────────────────┘
```

### Componentes Externos

```
┌─────────────────────────────────────────────────────────────────┐
│                    SERVICIOS EXTERNOS                            │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │  BPMN Workflow Engine                                      │ │
│  │  - Disparo de workflows después de clasificación          │ │
│  └───────────────────────────────────────────────────────────┘ │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │  AI Governance Client                                      │ │
│  │  - Sugerencias de categorías con LLM                      │ │
│  │  - GPT-4, Claude, Llama, etc.                              │ │
│  └───────────────────────────────────────────────────────────┘ │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │  Model Validation Service                                  │ │
│  │  - Validación de coherencia modelo-dataset                 │ │
│  └───────────────────────────────────────────────────────────┘ │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │  Technical Documentation Service                           │ │
│  │  - Validación de documentación técnica completa           │ │
│  └───────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📊 DIAGRAMA DE CAPAS

### Capas de la Aplicación

```
┌─────────────────────────────────────────────────────────────┐
│                    CAPA DE PRESENTACIÓN                      │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  Next.js Frontend                                      │  │
│  │  - React Components                                    │  │
│  │  - API Routes                                          │  │
│  │  - State Management                                    │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            ↓ HTTP REST
┌─────────────────────────────────────────────────────────────┐
│                    CAPA DE API (BFF)                         │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  Spring WebFlux (Reactivo)                             │  │
│  │  - Controllers                                         │  │
│  │  - Services (Agregación)                               │  │
│  │  - Circuit Breaker / Retry                             │  │
│  │  - Métricas y Observabilidad                           │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                    CAPA DE NEGOCIO                           │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  Business Services (Transaccional)                    │  │
│  │  - HighRiskClassifierBusinessService                  │  │
│  │  - AnnexIIICategoryBusinessService                    │  │
│  │  - ProhibitedSystemBusinessService                    │  │
│  │  - Validaciones y Reglas de Negocio                   │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                    CAPA DE PERSISTENCIA                      │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  Repositories (JPA)                                    │  │
│  │  - ProjectRepository                                  │  │
│  │  - AnnexIIICategoryRepository                        │  │
│  │  - GenericRepository<T, ID>                           │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                    CAPA DE DATOS                             │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  PostgreSQL Database                                  │  │
│  │  - PRJPROJECTS                                        │  │
│  │  - ANNANNEXIIICATEGORIES                             │  │
│  │  - GOVPROHIBITEDSYSTEMS                              │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

---

## 🔄 FLUJO DE CLASIFICACIÓN COMPLETO

### Diagrama de Secuencia

```
Frontend          BFF Controller      BFF Service        Business Service      Repository      Database
   │                   │                   │                    │                  │              │
   │ POST /classify    │                   │                    │                  │              │
   ├──────────────────>│                   │                    │                  │              │
   │                   │ classify()        │                    │                  │              │
   │                   ├───────────────────>│                    │                  │              │
   │                   │                   │ classifySystem()   │                  │              │
   │                   │                   ├───────────────────>│                  │              │
   │                   │                   │                   │ findById()       │              │
   │                   │                   │                   ├─────────────────>│              │
   │                   │                   │                   │                  │ SELECT *     │
   │                   │                   │                   │                  ├─────────────>│
   │                   │                   │                   │                  │<─────────────┤
   │                   │                   │                   │<─────────────────┤              │
   │                   │                   │                   │                  │              │
   │                   │                   │                   │ Validar categoría │              │
   │                   │                   │                   │ Validar subcats   │              │
   │                   │                   │                   │ Validar Art. 5    │              │
   │                   │                   │                   │ Validar justif.   │              │
   │                   │                   │                   │                  │              │
   │                   │                   │                   │ save(project)     │              │
   │                   │                   │                   ├─────────────────>│              │
   │                   │                   │                   │                  │ UPDATE       │
   │                   │                   │                   │                  ├─────────────>│
   │                   │                   │                   │                  │<─────────────┤
   │                   │                   │                   │<─────────────────┤              │
   │                   │                   │<──────────────────┤                  │              │
   │                   │                   │                   │                  │              │
   │                   │                   │ startWorkflow()   │                  │              │
   │                   │                   ├───────────────────┼──────────────────┼─────────────>│
   │                   │                   │                   │                  │              │ BPMN Engine
   │                   │                   │<──────────────────┼──────────────────┼─────────────┤
   │                   │                   │                   │                  │              │
   │                   │<──────────────────┤                   │                  │              │
   │                   │                   │                   │                  │              │
   │<──────────────────┤                   │                   │                  │              │
   │ 200 OK            │                   │                   │                  │              │
   │                   │                   │                   │                  │              │
```

### Pasos Detallados

1. **Frontend envía request**
   - `POST /api/compliance/classification/classify`
   - Body: `ClassificationRequestDto` con projectId, category, subcategories, justification, etc.

2. **BFF Controller recibe request**
   - Valida request con `@Valid`
   - Inicia timer de métricas
   - Llama a `ClassificationService.classify()`

3. **BFF Service procesa**
   - Convierte DTO a formato interno
   - Llama a `HighRiskClassifierBusinessService.classifySystem()`
   - Maneja errores con Circuit Breaker

4. **Business Service ejecuta lógica**
   - **Validar proyecto existe:** `projectRepository.findById(projectId)`
   - **Validar categoría:** `categoryBusinessService.getCategoryByCode(category)`
   - **Validar subcategorías:** `validateSubcategories(category, subcategories)`
   - **Validar Art. 5:** Verificar `prohibitedUseChecked == true`
   - **Validar justificación:** `validateJustificationQuality(justification, categoryName)`
   - **Crear JSONB:** `createAnnexIIICategoriesJSONB(category, subcategories)`
   - **Actualizar proyecto:** `project.setPrjishighrisk(true)`, `projectRepository.save(project)`

5. **Disparo de Workflow BPMN** (desde BFF)
   - `bpmnWorkflowClient.startClassificationWorkflow(projectId, category, isHighRisk)`
   - Variables: projectId, category, isHighRisk, classificationDate

6. **Retorno de respuesta**
   - BFF convierte resultado a `ClassificationResponseDto`
   - Incluye workflowInstanceId si se disparó workflow
   - Retorna `200 OK` con DTO

---

## 📋 FLUJO DE OBTENCIÓN DE CATEGORÍAS

### Diagrama de Secuencia

```
Frontend          BFF Controller      BFF Service        Business Service      Repository      Database
   │                   │                   │                    │                  │              │
   │ GET /categories   │                   │                    │                  │              │
   ├──────────────────>│                   │                    │                  │              │
   │                   │ getCategories()   │                    │                  │              │
   │                   ├───────────────────>│                    │                  │              │
   │                   │                   │ getMainCategories()│                  │              │
   │                   │                   ├───────────────────>│                  │              │
   │                   │                   │                   │ findMainCategories()│              │
   │                   │                   │                   ├─────────────────>│              │
   │                   │                   │                   │                  │ SELECT *     │
   │                   │                   │                   │                  ├─────────────>│
   │                   │                   │                   │                  │<─────────────┤
   │                   │                   │                   │<─────────────────┤              │
   │                   │                   │<──────────────────┤                  │              │
   │                   │<──────────────────┤                   │                  │              │
   │<──────────────────┤                   │                   │                  │              │
   │ 200 OK            │                   │                   │                  │              │
   │ [categories]      │                   │                   │                  │              │
```

### Pasos Detallados

1. **Frontend envía request**
   - `GET /api/compliance/classification/categories`
   - Query params: `expandSubcategories`, `categoryCode`, `activeOnly`

2. **BFF Controller recibe request**
   - Llama a `ClassificationService.getCategories()`

3. **BFF Service procesa**
   - Llama a `AnnexIIICategoryBusinessService.getMainCategories()`

4. **Business Service ejecuta**
   - `annexIIICategoryRepository.findMainCategories()`
   - Filtra por `ANNISLEVEL1 = true` y `ANNACTIVE = true`
   - Ordena por `ANNDISPLAYORDER`

5. **Retorno de respuesta**
   - BFF convierte entidades a DTOs
   - Si `expandSubcategories = true`, incluye subcategorías
   - Retorna `200 OK` con lista de categorías

---

## 🤖 FLUJO DE SUGERENCIA CON IA

### Diagrama de Secuencia

```
Frontend          BFF Controller      BFF Service        AI Service          AI Client        LLM
   │                   │                   │                  │                  │              │
   │ POST /suggest     │                   │                  │                  │              │
   ├──────────────────>│                   │                  │                  │              │
   │                   │ suggest()         │                  │                  │              │
   │                   ├───────────────────>│                  │                  │              │
   │                   │                   │ suggestCategory()│                  │              │
   │                   │                   ├──────────────────>│                  │              │
   │                   │                   │                   │ invokeLLM()      │              │
   │                   │                   │                   ├─────────────────>│              │
   │                   │                   │                   │                  │ API Call     │
   │                   │                   │                   │                  ├─────────────>│
   │                   │                   │                   │                  │<─────────────┤
   │                   │                   │                   │<─────────────────┤              │
   │                   │                   │                   │                  │              │
   │                   │                   │                   │ parseResponse()  │              │
   │                   │                   │                   │ calculateConfidence()│              │
   │                   │                   │                   │                  │              │
   │                   │                   │                   │ if confidence < 0.85│              │
   │                   │                   │                   │   fallback to keywords│              │
   │                   │                   │<──────────────────┤                  │              │
   │                   │<──────────────────┤                   │                  │              │
   │<──────────────────┤                   │                   │                  │              │
   │ 200 OK            │                   │                   │                  │              │
   │ [suggestions]     │                   │                   │                  │              │
```

### Pasos Detallados

1. **Frontend envía request**
   - `POST /api/compliance/classification/suggest`
   - Body: `CategorySuggestionRequestDto` con `projectDescription`

2. **BFF Controller recibe request**
   - Llama a `ClassificationService.suggest()`

3. **BFF Service procesa**
   - Llama a `AIClassificationService.suggestCategory(projectDescription)`

4. **AI Service ejecuta**
   - Construye prompt con descripción del proyecto
   - Invoca `AIGovernanceClient.invokeLLM(prompt)`
   - Analiza respuesta del LLM
   - Calcula score de confianza

5. **Validación de confianza**
   - Si `confidence >= 0.85`: Retorna sugerencia
   - Si `confidence < 0.85` o IA no disponible: Usa fallback con keywords

6. **Fallback (si aplica)**
   - Llama a `AnnexIIICategoryBusinessService.suggestCategories(projectDescription)`
   - Usa keywords para calcular relevancia

7. **Retorno de respuesta**
   - BFF convierte a `CategorySuggestionResponseDto`
   - Retorna top 3 sugerencias con scores

---

## 🔗 INTEGRACIONES CON OTROS MÓDULOS

### Integración con FRIA

**Propósito:** Si un proyecto es clasificado como alto riesgo, puede requerir una evaluación FRIA.

**Flujo:**

```
Classification Service
    ↓
Proyecto clasificado como alto riesgo
    ↓
BPMN Workflow
    ↓
FRIA Service
    ↓
Crear evaluación FRIA automática
```

**Implementación:**
- El workflow BPMN dispara creación de evaluación FRIA
- Variable del workflow: `requiresFRIA = true`

### Integración con PMM

**Propósito:** Proyectos de alto riesgo requieren monitoreo post-mercado.

**Flujo:**

```
Classification Service
    ↓
Proyecto clasificado como alto riesgo
    ↓
BPMN Workflow
    ↓
PMM Service
    ↓
Crear plan de monitoreo PMM
```

**Implementación:**
- El workflow BPMN dispara creación de plan PMM
- Variable del workflow: `requiresPMM = true`

### Integración con EU Registration

**Propósito:** Proyectos de alto riesgo deben registrarse según Art. 49.

**Flujo:**

```
Classification Service
    ↓
Proyecto clasificado como alto riesgo
    ↓
BPMN Workflow
    ↓
EU Registration Service
    ↓
Crear registro EU (si aplica)
```

**Implementación:**
- El workflow BPMN verifica si requiere registro EU
- Variable del workflow: `euRegistrationRequired = true`

### Integración con Model Validation

**Propósito:** Validar que modelos tienen dataset documentado antes de clasificar.

**Flujo:**

```
BFF Service
    ↓
Antes de clasificar
    ↓
Model Validation Service
    ↓
Validar coherencia modelo-dataset
    ↓
Si válido: Continuar clasificación
Si inválido: Bloquear clasificación
```

**Implementación:**
- Validación en BFF antes de llamar a `classifySystem()`
- Si falla: Retorna `400 Bad Request` con mensaje de error

### Integración con Technical Documentation

**Propósito:** Validar que documentación técnica está completa antes de clasificar.

**Flujo:**

```
BFF Service
    ↓
Antes de clasificar
    ↓
Technical Documentation Service
    ↓
Validar documentación completa (>= 90%)
    ↓
Si válido: Continuar clasificación
Si inválido: Bloquear clasificación
```

**Implementación:**
- Validación en BFF antes de llamar a `classifySystem()`
- Si falla: Retorna `400 Bad Request` con mensaje de error

---

## ✅ FLUJO DE VALIDACIONES

### Diagrama de Validaciones

```
Request de Clasificación
    ↓
┌─────────────────────────────────────┐
│ 1. Validar Proyecto Existe         │
│    - projectRepository.findById()   │
│    - Si no existe: 404 Not Found     │
└─────────────────────────────────────┘
    ↓
┌─────────────────────────────────────┐
│ 2. Validar Categoría               │
│    - categoryBusinessService        │
│      .getCategoryByCode()           │
│    - Debe ser Level 1 (III.1-III.8)│
│    - Si inválida: 400 Bad Request   │
└─────────────────────────────────────┘
    ↓
┌─────────────────────────────────────┐
│ 3. Validar Subcategorías            │
│    - validateSubcategories()         │
│    - Deben pertenecer a categoría   │
│    - Si inválidas: 400 Bad Request  │
└─────────────────────────────────────┘
    ↓
┌─────────────────────────────────────┐
│ 4. Validar Art. 5                  │
│    - prohibitedUseChecked == true    │
│    - Si no verificado: 400 Bad Request│
└─────────────────────────────────────┘
    ↓
┌─────────────────────────────────────┐
│ 5. Validar Justificación           │
│    - validateJustificationQuality() │
│    - Mínimo 100 caracteres          │
│    - Mencionar categoría             │
│    - 2+ palabras clave de riesgo   │
│    - Si inválida: 400 Bad Request   │
└─────────────────────────────────────┘
    ↓
┌─────────────────────────────────────┐
│ 6. Validar Modelo-Dataset (BFF)    │
│    - ModelValidationService         │
│    - Si inválido: 400 Bad Request   │
└─────────────────────────────────────┘
    ↓
┌─────────────────────────────────────┐
│ 7. Validar Documentación Técnica   │
│    - TechnicalDocumentationService  │
│    - >= 90% completitud             │
│    - Si inválida: 400 Bad Request   │
└─────────────────────────────────────┘
    ↓
┌─────────────────────────────────────┐
│ 8. Clasificar Proyecto              │
│    - Actualizar campos en BD        │
│    - Retornar resultado             │
└─────────────────────────────────────┘
```

### Orden de Validaciones

1. **Validaciones de existencia** (proyecto, categoría)
2. **Validaciones de estructura** (subcategorías)
3. **Validaciones de negocio** (Art. 5, justificación)
4. **Validaciones de compliance** (modelo-dataset, documentación técnica)
5. **Ejecución de clasificación**

---

## 📚 REFERENCIAS

### Documentación Relacionada

- `DEVELOPER_GUIDE_BACKEND.md` - Guía de desarrollo backend
- `DEVELOPER_GUIDE_FRONTEND.md` - Guía de desarrollo frontend
- `DATABASE_SCHEMA.md` - Esquema de base de datos
- `ESTADO_IMPLEMENTACION_CLASSIFICATION.md` - Estado de implementación

---

**Última Actualización:** Diciembre 2025
**Versión:** 1.0
**Estado:** ✅ Operativo y listo para producción
