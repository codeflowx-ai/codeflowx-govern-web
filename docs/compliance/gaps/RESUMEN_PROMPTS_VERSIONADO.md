# RESUMEN - PROMPTS VERSIONADO Y CONTROL DE CAMBIOS

**Fecha Creación:** Diciembre 2025  
**Total Prompts Creados:** 4  
**Estado:** ✅ Todos los prompts creados  
**Auditoría:** AUDITORIA_011_VERSIONADO_CONTROL_CAMBIOS.md

---

## PROMPTS CREADOS POR PRIORIDAD

### 🔴 CRÍTICAS (2 prompts)

| ID | Archivo | Tipo | Esfuerzo | Responsable |
|----|---------|------|----------|-------------|
| **INC-011-01** | `prompts/java/INC-011-01_control_acceso_roles.md` | Java + DBA | 4.5-7.5 días | Backend + DBA |
| **INC-011-02** | `prompts/java/INC-011-02_versionado_explicito_datasets.md` | Java + DBA | 4-5 días | Backend + DBA |

**Descripción:**
- **INC-011-01:** Sistema de roles (CORROLES, CORUSERROLES), workflow de aprobación (VERSVERSIONAPPROVALS), triggers PostgreSQL para bloquear updates en versiones aprobadas
- **INC-011-02:** Crear entidad DatasetVersion con versionado SemVer, checksums SHA-256, changelog, estadísticas

---

### 🟡 ALTAS (2 prompts)

| ID | Archivo | Tipo | Esfuerzo | Responsable |
|----|---------|------|----------|-------------|
| **INC-011-03** | `prompts/java/INC-011-03_versionado_explicito_evaluaciones.md` | Java | 2-3 días | Backend |
| **INC-011-04** | `prompts/java/INC-011-04_validacion_semver.md` | Java + DBA | 2.5 días | Backend + DBA |

**Descripción:**
- **INC-011-03:** Agregar campos de versionado a ModelEvaluation (EVALVERSIONNUMBER, EVALPARENTEVALUATION, EVALCHANGES, EVALREASON)
- **INC-011-04:** Validación automática de formato SemVer con constraints en BD y utilidad Java compartida

---

## DISTRIBUCIÓN POR TIPO DE AGENTE

### Java (Backend) - 4 prompts
- INC-011-01: Control de acceso basado en roles (requiere DBA también)
- INC-011-02: Versionado explícito de datasets (requiere DBA también)
- INC-011-03: Versionado explícito de evaluaciones
- INC-011-04: Validación SemVer (requiere DBA también)

### DBA - 3 prompts (compartidos)
- INC-011-01: Tablas CORROLES, CORUSERROLES, VERSVERSIONAPPROVALS, triggers
- INC-011-02: Tabla DATADATASETVERSIONS, migración de datos
- INC-011-04: Constraints SemVer, migración de versiones existentes

---

## ESFUERZO TOTAL ESTIMADO

| Prioridad | Prompts | Esfuerzo Total |
|-----------|---------|----------------|
| 🔴 Críticas | 2 | 8.5-12.5 días |
| 🟡 Altas | 2 | 4.5-5.5 días |
| **TOTAL** | **4** | **13-18 días** |

---

## ESTRUCTURA DE ARCHIVOS CREADOS

```
gaps/prompts/
└── java/
    ├── INC-011-01_control_acceso_roles.md ✅
    ├── INC-011-02_versionado_explicito_datasets.md ✅
    ├── INC-011-03_versionado_explicito_evaluaciones.md ✅
    └── INC-011-04_validacion_semver.md ✅
```

---

## PRÓXIMOS PASOS

### Fase 1: Críticas (Inmediato)
1. ✅ INC-011-01: Control de acceso basado en roles (Backend + DBA)
2. ✅ INC-011-02: Versionado explícito de datasets (Backend + DBA)

### Fase 2: Altas (1-2 semanas)
3. ✅ INC-011-03: Versionado explícito de evaluaciones (Backend)
4. ✅ INC-011-04: Validación SemVer (Backend + DBA)

---

## CUMPLIMIENTO NORMATIVO

### EU AI Act
- ✅ **Art. 10:** Gobernanza de Datos (INC-011-02)
- ✅ **Art. 11:** Documentación Técnica (INC-011-02, INC-011-03, INC-011-04)
- ✅ **Art. 12:** Registros Automáticos (INC-011-01, INC-011-03)
- ✅ **Art. 19:** Registros Inalterables (INC-011-01)

### ISO/IEC 27001
- ✅ **RBAC:** Control de acceso basado en roles (INC-011-01)

### GDPR
- ✅ **Art. 30:** Registro de actividades de tratamiento (INC-011-02)

---

## DEPENDENCIAS ENTRE PROMPTS

1. **INC-011-04** debe ejecutarse antes o en paralelo con **INC-011-02** y **INC-011-03** (validación SemVer aplica a todos los versionados)
2. **INC-011-01** es independiente pero complementa a todos los demás (control de acceso aplica a todas las operaciones de versionado)
3. **INC-011-02** y **INC-011-03** son independientes entre sí

---

## NOTAS IMPORTANTES

1. **Migración de datos:**
   - INC-011-02 requiere migración de campo `DATASETVERSION` VARCHAR a FK
   - INC-011-04 requiere normalización de versiones existentes a formato SemVer
   - INC-011-03 requiere establecer versión 1.0.0 para evaluaciones existentes

2. **Integraciones:**
   - INC-011-01 requiere integración con sistema de autenticación existente
   - INC-011-02 requiere acceso a storage de archivos de datasets para calcular hash

3. **Testing:**
   - Todos los prompts incluyen sección de testing
   - Validaciones específicas por incidencia
   - Tests de integridad para checksums (INC-011-02)

---

## REFERENCIAS

### Documentos de Auditoría
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_011_VERSIONADO_CONTROL_CAMBIOS.md`
- **Incidencias:** `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_VERSIONADO.md`

### Especificaciones
- **SemVer:** https://semver.org/
- **ISO/IEC 27001:** Control de acceso basado en roles

---

**Última actualización:** Diciembre 2025  
**Versión:** 1.0  
**Mantenedor:** CodeflowX Compliance Team

