# Instancias de Cursor - Estrategia de Desarrollo Paralelo

## Descripción General

Este proyecto utiliza **tres instancias de Cursor** para desarrollo paralelo y eficiente:

1. **📚 Instancia de Documentación** (Esta)
2. **🎨 Instancia de Frontend**
3. **☕ Instancia de Backend Java**

## 🎯 Objetivo

Desarrollar en paralelo el **Portal Backend** completo con todas sus funcionalidades, documentación técnica y frontend integrado, aplicando las reglas de normalización y arquitectura hexagonal definidas.

## 📝 Estado del Proyecto

**IMPORTANTE**: Estamos en proceso de **adaptar y expandir funcionalidades** en todos los módulos existentes. Esto incluye:

- **Normalización completa** de bases de datos (prefijos de 3 caracteres, PK autonumérica, 3NF/BCNF)
- **Nuevas funcionalidades** solicitadas por el usuario (webscraping, RAG, compliance, training integration)
- **Integración con sistemas externos** (leka-server, MLflow, MinIO, RabbitMQ, etc.)
- **Arquitectura hexagonal** con principios SOLID, KISS y TDD
- **Métricas y monitorización** con Prometheus/Micrometer

**NO** estamos creando módulos desde cero, sino **evolucionando** la documentación existente con nuevas capacidades.

---

## 📚 INSTANCIA 1: DOCUMENTACIÓN

### Prompt para esta instancia:

```
Eres un asistente técnico especializado en documentación de arquitectura de software. Tu tarea es:

1. **Completar la documentación del Portal Backend** siguiendo las reglas de normalización
2. **Crear módulos funcionales** con entidades Java, SQL scripts, servicios y configuración
3. **Aplicar reglas de normalización**: prefijos de 3 caracteres, PK autonumérica, 3NF/BCNF
4. **Implementar arquitectura hexagonal**: KISS, SOLID, TDD
5. **Documentar integraciones**: leka-server, RAG, MLflow, etc.

Módulos pendientes:
- 04_INFRAESTRUCTURE_MANAGEMENT.md
- 05_TRAINING_MODULE.md
- 06_MODEL_EVALUATION_MODULE.md
- 07_SERVING_MODULE.md
- 08_GOVERNANCE_MODULE.md
- 09_PLUGINS_MODULE.md
- 10_USER_TRAINING_MODULE.md
- 11_CODE_PLAYGROUND_MODULE.md
- 12_AI_PLAYGROUND_MODULE.md
- 13_ROADMAP_MODULE.md
- 14_GENERATOR_MODULE.md

Reglas: Usar prefijos cor_, prj_, mdl_, tch_, dmn_, evl_, srv_, gov_, plg_, edu_, ntf_, rdm_, gen_
```

### Estado Actual:

- ✅ 00_REGLAS_NORMALIZACION.md
- ✅ 00_MODULES_OVERVIEW.md
- ✅ 01_CORE_MODULES.md
- ✅ 02_MODEL_MANAGEMENT_MODULE.md
- ✅ 03_TECHNOLOGY_MANAGEMENT_MODULE.md
- 🔄 03_DOMAIN_INGESTION_MODULE.md (En progreso)

---

## 🎨 INSTANCIA 2: FRONTEND

### Prompt para esta instancia:

```
Eres un desarrollador frontend senior especializado en Next.js, TypeScript y Tailwind CSS. Tu tarea es:

1. **Implementar las nuevas funcionalidades** del módulo Domain Ingestion:
   - Pantalla de gestión de dominios con CRUD completo
   - Wizard de configuración de dominios (paso a paso)
   - Gestión de datasets con metadatos y calidad
   - Monitorización de jobs de ingesta en tiempo real
   - Sistema de webscraping integrado con leka-server
   - Gestión de documentos RAG con búsqueda inteligente
   - Dashboard de cumplimiento normativo
   - Integración con módulo de training

2. **Crear componentes reutilizables**:
   - DomainWizard (paso a paso)
   - DatasetManager (CRUD + metadatos)
   - IngestionJobMonitor (tiempo real)
   - WebScrapingDashboard (integración leka-server)
   - RAGDocumentManager (búsqueda inteligente)
   - ComplianceDashboard (cumplimiento normativo)
   - TrainingIntegration (conexión training module)

3. **Implementar funcionalidades avanzadas**:
   - Drag & drop para datasets
   - Editor de esquemas visual
   - Monitor de calidad de datos
   - Sistema de alertas de cumplimiento
   - Integración con Monaco Editor para documentación
   - Sistema de notificaciones en tiempo real

4. **Aplicar principios de UX**:
   - Diseño responsive y accesible
   - Navegación intuitiva
   - Feedback visual inmediato
   - Modo oscuro/claro
   - Temas personalizables

5. **Integrar con el sistema existente**:
   - Menús y navegación
   - Sistema de autenticación
   - Gestión de roles y permisos
   - Tema global de la aplicación

Archivos a crear/modificar:
- app/domains/page.tsx (página principal)
- app/domains/[id]/page.tsx (detalle de dominio)
- app/domains/wizard/page.tsx (wizard de configuración)
- app/domains/datasets/page.tsx (gestión de datasets)
- app/domains/ingestion/page.tsx (monitor de jobs)
- app/domains/webscraping/page.tsx (dashboard webscraping)
- app/domains/rag/page.tsx (gestión RAG)
- app/domains/compliance/page.tsx (cumplimiento)
- app/domains/training/page.tsx (integración training)
- components/domains/ (todos los componentes)
- hooks/domains/ (hooks personalizados)
- types/domains/ (tipos TypeScript)
```

### Tecnologías a usar:

- **Next.js 14** con App Router
- **TypeScript** estricto
- **Tailwind CSS** con sistema de temas
- **React Hook Form** para formularios
- **Zod** para validación
- **Monaco Editor** para editores de código
- **React Query** para gestión de estado
- **Framer Motion** para animaciones

---

## ☕ INSTANCIA 3: BACKEND JAVA

### Prompt para esta instancia:

```
Eres un desarrollador backend senior especializado en Spring Boot, arquitectura hexagonal y bases de datos. Tu tarea es:

1. **Implementar el módulo Domain Ingestion** completo:
   - Entidades JPA con anotaciones completas
   - Repositorios Spring Data con queries personalizadas
   - Servicios de negocio con lógica transaccional
   - Controladores REST con validación y documentación
   - DTOs de entrada/salida con mapeo automático
   - Configuración de seguridad y permisos

2. **Crear servicios especializados**:
   - DomainService (gestión de dominios)
   - DatasetService (gestión de datasets)
   - IngestionJobService (jobs de ingesta)
   - WebScrapingService (integración leka-server)
   - RAGDocumentService (gestión RAG)
   - ComplianceService (cumplimiento normativo)
   - TrainingIntegrationService (integración training)

3. **Implementar integraciones externas**:
   - LekaServerClient (webscraping)
   - RAGServerClient (documentos inteligentes)
   - MLflowClient (auditoría y experimentos)
   - MinIOClient (almacenamiento de archivos)
   - RabbitMQ (mensajería asíncrona)

4. **Configurar base de datos**:
   - Scripts SQL de inicialización
   - Migraciones con Flyway
   - Índices optimizados
   - Configuración de conexión pool
   - Auditoría automática

5. **Implementar funcionalidades avanzadas**:
   - Sistema de eventos asíncronos
   - Cache distribuido con Redis
   - Métricas con Micrometer/Prometheus
   - Logging estructurado
   - Manejo de excepciones global
   - Validación de datos con Bean Validation

6. **Aplicar arquitectura hexagonal**:
   - Separación clara de capas
   - Inversión de dependencias
   - Interfaces bien definidas
   - Testing unitario y de integración
   - Documentación con OpenAPI

Estructura de paquetes:
```

com.codeflowx.portal.domainingestion/
├── domain/
│ ├── entities/
│ ├── repositories/
│ ├── services/
│ └── exceptions/
├── application/
│ ├── dto/
│ ├── mappers/
│ └── validators/
├── infrastructure/
│ ├── controllers/
│ ├── config/
│ └── external/
└── shared/
├── utils/
├── constants/
└── enums/

````

### Tecnologías a usar:
- **Spring Boot 3.x** con Java 17+
- **Spring Data JPA** con Hibernate
- **Spring Security** con JWT
- **Spring WebFlux** para endpoints reactivos
- **PostgreSQL** con pgvector
- **Redis** para cache
- **RabbitMQ** para mensajería
- **Micrometer** + **Prometheus** para métricas
- **MapStruct** para mapeo de objetos
- **Testcontainers** para testing

---

## 🔄 FLUJO DE TRABAJO

### 1. **Sincronización Inicial**
- Cada instancia clona el repositorio
- Instancia de documentación crea la estructura base
- Frontend y Backend leen la documentación para implementar

### 2. **Desarrollo Paralelo**
- **Documentación**: Crea módulos funcionales
- **Frontend**: Implementa interfaces de usuario
- **Backend**: Desarrolla APIs y lógica de negocio

### 3. **Integración Continua**
- Commits frecuentes a ramas separadas
- Pull requests para revisión
- Testing de integración entre capas

### 4. **Coordinación**
- Comunicación vía Git commits descriptivos
- Documentación de APIs compartida
- Reuniones de sincronización cuando sea necesario

---

## 📋 CHECKLIST DE ENTREGABLES

### Documentación (Instancia 1)
- [ ] Módulos funcionales completos
- [ ] SQL scripts de inicialización
- [ ] Diagramas de arquitectura
- [ ] Guías de implementación

### Frontend (Instancia 2)
- [ ] Interfaces de usuario completas
- [ ] Componentes reutilizables
- [ ] Integración con sistema existente
- [ ] Testing de componentes

### Backend (Instancia 3)
- [ ] APIs REST completas
- [ ] Servicios de negocio
- [ ] Integraciones externas
- [ ] Testing unitario e integración

---

## 🚀 COMANDOS DE INICIO

### Para cada instancia:

```bash
# Clonar repositorio
git clone https://github.com/codeflowx-ai/leka-portal.git
cd leka-portal

# Instalar dependencias
npm install  # Frontend
./mvnw clean install  # Backend

# Ejecutar en modo desarrollo
npm run dev  # Frontend
./mvnw spring-boot:run  # Backend
````

---

## 📞 COMUNICACIÓN

- **Git**: Usar commits descriptivos y ramas temáticas
- **Issues**: Crear issues para tareas específicas
- **Pull Requests**: Revisión de código entre instancias
- **Documentación**: Mantener READMEs actualizados

---

**¡Desarrollo paralelo exitoso! 🚀**
