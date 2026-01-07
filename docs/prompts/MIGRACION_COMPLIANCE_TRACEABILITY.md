# Prompt de Migración - Compliance - Traceability Evidence (Art. 12)

## Contexto del Módulo

**Módulo:** Compliance
**Tipo de Pantalla:** Dashboard de Trazabilidad
**Total de Pantallas:** 1
**Artículo EU AI Act:** Art. 12 - Record-Keeping

---

## Arquitectura del Módulo

### ViewModel Identificado

#### TraceabilityEvidenceViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.compliance`
- **Archivo:** `com/codeflowx/govern/viewmodel/compliance/TraceabilityEvidenceViewModel.java`
- **Servicios Usados:**
  - `ImmutableLoggingBusinessService` - Logs inmutables
  - `BusinessService` - Servicios generales
- **Funcionalidades:**
  - Consolidar logs, prompts, outputs, decisiones
  - Mostrar trazabilidad completa modelo-dataset-output
  - Integración con telemetría (opcional)

### Entidades JPA Principales

- **ImmutableLog** - Logs inmutables (Art. 19, Art. 12)
- **Model** - Modelos de IA
- **Project** - Proyectos

### Business Services Disponibles

- **ImmutableLoggingBusinessService** ✅
  - `getEntityLogsWithVerification(String entityType, Long entityId)`
  - `verifyIntegrity(Long startId, Long endId)`

---

## Pantalla a Migrar

- **Archivo ZUL:** `console/gobierno/compliance/traceability-evidence.zul`
- **ViewModel Asociado:** `TraceabilityEvidenceViewModel.java`
- **Ruta Next.js:** `/governance/compliance/traceability-evidence`

---

## Estrategia de Migración

### Dashboard de Trazabilidad:
- Vista consolidada de evidencias
- Trazabilidad modelo-dataset-output
- Filtros por entidad, fecha, tipo
- Exportación de evidencias

### Mock Data:
```typescript
export const mockTraceabilityData = {
  model: { id: 123, name: "Credit Scoring Model" },
  dataset: { id: 456, name: "Training Dataset v1.0" },
  logs: [...],
  decisions: [...],
  outputs: [...]
};
```

---

**Última actualización:** Noviembre 2025
**Estado:** Listo para implementación
