# Prompt de Migración - Compliance - Technical Docs (Art. 11)

## Contexto del Módulo

**Módulo:** Compliance
**Tipo de Pantalla:** Generador de Documentación Técnica
**Total de Pantallas:** 1
**Artículo EU AI Act:** Art. 11 - Technical Documentation (Anexo IV)

---

## Arquitectura del Módulo

### ViewModel Identificado

#### AIActDocumentationGeneratorViewModel
- **Paquete:** `com.codeflowx.govern.viewmodel.compliance`
- **Archivo:** `com/codeflowx/govern/viewmodel/compliance/AIActDocumentationGeneratorViewModel.java`
- **Servicios Usados:**
  - `ModelService` - Gestión de modelos
- **Entidades Usadas:**
  - `Model` - Modelo de IA
  - Campos: `MODTECHNICALDOCURL`, `MODTECHNICALDOCCOMPLETE`, `MODTECHNICALDOCSCORE`

### Entidades JPA Principales

- **Model** - `com.codeflowx.govern.entity.models.Model`
  - `MODTECHNICALDOCURL` - URL documentación técnica
  - `MODTECHNICALDOCCOMPLETE` - Boolean si Anexo IV completo
  - `MODTECHNICALDOCSCORE` - Score completitud (0-1)

### Business Services Disponibles

- **TechnicalDocumentationBusinessService** ✅
  - `calculateDocumentationScore(Long modelId)` - Calcula score documentación
  - `validateAnexoIVCompleteness(Long modelId)` - Valida completitud Anexo IV
  - **11 secciones Anexo IV:**
    1. General description
    2. System architecture
    3. Data governance
    4. Risk management
    5. Human oversight
    6. Accuracy & robustness
    7. Cybersecurity
    8. Quality control
    9. Post-market monitoring
    10. Conformity assessment
    11. Record-keeping

---

## Pantalla a Migrar

- **Archivo ZUL:** `console/gobierno/compliance/ai-act-documentation-generator.zul`
- **ViewModel Asociado:** `AIActDocumentationGeneratorViewModel.java`
- **Ruta Next.js:** `/governance/compliance/technical-docs`

---

## Estrategia de Migración

### Generador de Documentación:
- Lista de 11 secciones Anexo IV
- Validación de completitud por sección
- Score overall de documentación
- Generación de PDF
- Upload de documentos

### Mock Data:
```typescript
export const mockTechnicalDocs = {
  modelId: 123,
  modelName: "Credit Scoring Model",
  sections: [
    { id: 1, name: "General description", complete: true, score: 1.0 },
    { id: 2, name: "System architecture", complete: true, score: 0.95 },
    { id: 3, name: "Data governance", complete: false, score: 0.60 },
    // ... 8 secciones más
  ],
  overallScore: 0.82,
  isComplete: false
};
```

---

**Última actualización:** Noviembre 2025
**Estado:** Listo para implementación
