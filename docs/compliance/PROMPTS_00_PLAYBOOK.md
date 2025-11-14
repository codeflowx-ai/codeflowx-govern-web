# PROMPTS 00 - PLAYBOOK DE EJECUCIÓN PARA AGENTES (GOBERNANZA IA)
## Objetivo
Establecer el orden y las instrucciones que deben seguir los agentes de Cursor para generar entidades JPA, servicios REST, vistas ZUL y configuraciones asociadas a CodeflowX utilizando exclusivamente los prompts vigentes. Todo el flujo referencia el metamodelo (`PROMPTS_26`) y los prompts sectoriales (`PROMPTS_27` a `PROMPTS_36`), evitando documentos obsoletos.

---

## Secuencia general (por sector o iniciativa)
1. **Preparar contexto de metamodelo**
   - Leer `PROMPTS_26_METAMODELO_SECTORIAL.md`.
   - Ejecutar prompts de catálogo (`PROMPTS_26.1` a `PROMPTS_26.4`) para registrar sector, framework FaaS, políticas y validaciones API.

2. **Seleccionar sector**
   - Ubicar el sector correspondiente en `docs/compliance/<sector>/`.
   - Revisar el `01_requisitos.md` para confirmar alcance funcional/técnico.
   - Abrir el documento `PROMPTS_3X_<SECTOR>.md` y seguir sus prompts numerados.

3. **Ejecución de prompts sectoriales**
   - **Prompts .1 (framework FaaS):** generan configuraciones en catálogos (`FAAS_FRAMEWORKS`) y FRIA.
   - **Prompts .2-.7:** parametrizan subperfiles y políticas. Cada prompt indica explícitamente:
     - Entidades/metadatos a reutilizar.
     - Reglas Drools y workflows BPMN/CMMN referenciados.
     - Dashboards y APIs afectados.
   - **Prompts .8 o especiales:** definen controles transversales (consentimiento, pricing, PRL, privilegio legal, etc.).

4. **Generación de artefactos técnicos**
   - Para cada prompt sectorial, el agente debe producir:
     - **Entidades JPA Java:** nombre de tabla con prefijo de tres letras según módulo (`gov_`, `prj_`, etc.), claves primarias autonuméricas, 3FN, siguiendo KISS/SOLID y arquitectura hexagonal. Si la entidad ya existe en metamodelo, ajustar catálogo en lugar de crear tabla.
     - **Servicios y REST (Java EnArt):** implementar casos de uso usando las APIs descritas en `PROMPTS_15_GOVERNANCE_API_REST.md` y `PROMPTS_16_WEBHOOKS_Y_DELIVERIES.md`.
     - **ViewModels y ZUL (codeflowx-portal):** formularios, dashboards y tablas usando los metadatos definidos; documentar en el prompt la vista requerida.
     - **Python (solo IA/MLOps/RAG):** cuando se requiera, aplicar prompts desde `PROMPTS_01`/`PROMPTS_02` si siguen vigentes; en general, priorizar reutilizar servicios existentes.
   - Registrar cualquier nuevo prompt ejecutado en `TRACKING_PROMPTS_IMPLEMENTACION.md`.

5. **Validación**
   - Ejecutar pruebas/linting indicadas en el prompt.
   - Actualizar el estado de la fila en `TRACKING_PROMPTS_IMPLEMENTACION.md`.

---

## Guía rápida por archivos vigentes
- `PROMPTS_01` a `PROMPTS_14`: base histórica aún en ejecución (microservicios python, BPMN, multi-framework, conectores, datos mock). Usarlos solo cuando el plan indique expresamente que siguen pendientes; documentar cualquier ejecución adicional en el tracking.
- `PROMPTS_15` a `PROMPTS_18`: núcleo de API REST, webhooks, MCP y tablas de gobernanza.
- `PROMPTS_26`: metamodelo base (catálogos sectoriales + FaaS + políticas API).
- `PROMPTS_27` a `PROMPTS_45`: sectoriales ordenados alfabéticamente.
- `TRACKING_PROMPTS_IMPLEMENTACION.md`: estado y porcentaje.

---

## Política de saneamiento documental
- Mantener en `docs/compliance/` todos los `PROMPTS_01` a `PROMPTS_36` hasta que el tracking indique su completa implementación y cierre.
- Sólo mover a `docs/compliance/_deprecated/` documentación auxiliar obsoleta (matrices externas, guías previas, resúmenes duplicados) cuando se confirme que no impacta en la ejecución de prompts pendientes.
- Registrar en `TRACKING_PROMPTS_IMPLEMENTACION.md` la fecha en que un prompt queda definitivamente cerrado para programar su archivado futuro.

---

## Recomendaciones adicionales
- Mantener actualizado `README.md` con enlaces al playbook y al metamodelo.
- Para nuevos países/sectores, derivar desde `PROMPTS_26` sin modificar tablas; agregar nuevas filas en catálogos JSONB.
- Evitar mezclar instrucciones en un mismo prompt: si un sector requiere más de 8-9 prompts, dividir en subsecciones adicionales siguiendo la numeración secuencial.

---

**Última actualización:** Noviembre 2025  
**Responsable:** Arquitectura de Cumplimiento CodeflowX (Manuel & Equipo)  
**Contacto:** governance@codeflowx.internal

