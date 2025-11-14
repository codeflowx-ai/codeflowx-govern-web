# PROMPT 001 – AI-OS-ARCH (Arquitecto AI OS)

## Contexto
- Proyecto: CodeflowX Govern, plataforma de gobierno, cumplimiento EU AI Act y orquestación de modelos, prompts, agentes, RAG y datasets.
- Repositorios principales: `nocode.service.entitys`, `codeflowx.govern.workflow.lib`, `codeflowx-faas-platform`.
- Documentación de referencia: `docs/arquitectura/ARQUITECTURA_AI_OS.md`, `EU_AI_ACT_ENTITIES_README.md`, `ARQUITECTURA_CODEFLOWX_GOVERN.md`.
- Convenciones obligatorias: prefijos de tres caracteres por módulo, PK autonumérica (`BIGSERIAL`), `iduuid` único, 3FN, arquitectura hexagonal, SOLID y KISS.

## Objetivo
Diseñar e implementar el modelo de dominio AI OS asegurando alineación total con las entidades regulatorias existentes.

### Alcance
1. Definir entidades JPA `AioComponent`, `AioCapability`, `AioPolicyBinding`, `AioWorkspace` y otras derivadas según `ARQUITECTURA_AI_OS.md`.
2. Generar scripts SQL PostgreSQL (`*.sql`) respetando nomenclatura ENART y relaciones 3FN.
3. Crear DTOs, servicios y controladores REST (`/api/v1/aios/*`) que expongan CRUD e integren con `ImmutableLog`.
4. Documentar dependencias con `ComplianceAssessment`, `FriaAssessment`, `EuRegistration`, `AnnexIIICategory`.

## Restricciones
- Usar únicamente dependencias ya presentes en el proyecto.
- Código y documentación en español técnico.
- Mantener los artefactos en ASCII.
- Reutilizar `@Entidad` y anotaciones ENART donde corresponda.

## Entregables
- Entidades, repositorios, servicios y controladores en `nocode.service.entitys`.
- Scripts SQL en `src/main/resources/sql`.
- Tests unitarios básicos para repositorios y servicios (`@DataJpaTest`, mocks).
- Actualización incremental de `docs/arquitectura/ARQUITECTURA_AI_OS.md` con decisión final de modelo.
- Registro de cada cambio relevante en `IMLIMMUTABLELOGS`.

## Métricas de éxito
- Cobertura de todas las tablas listadas en el dominio `Governance Core` y `Experience Layer`.
- Pruebas unitarias verdes.
- Integración documentada con componentes existentes sin romper compatibilidad.
