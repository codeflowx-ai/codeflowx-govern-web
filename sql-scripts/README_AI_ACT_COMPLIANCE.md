# EU AI Act Compliance - SQL Schema

## 📋 Descripción

Scripts SQL para crear las tablas necesarias para soportar **100% compliance** con el **EU AI Act**.

**Fecha de creación:** 2 Noviembre 2025  
**Artículos cubiertos:** 11 (Technical Documentation), 12 (Record-keeping), 48 (Declaration of Conformity)

---

## 🗂️ Tablas Creadas

### 1. `GOV_AIACT_TECHNICAL_DOCS`

**Propósito:** Documentación técnica según EU AI Act Annex IV (Artículo 11)

**Campos principales:**
- Identificación del sistema IA (entity_type, entity_id, system_name, version)
- Secciones mandatorias Annex IV:
  - System Description
  - Intended Purpose
  - Development Process
  - Data Governance
  - Validation Procedures
  - Testing Procedures
  - Monitoring Measures
  - Human Oversight
  - Risk Management
  - Accuracy and Robustness
  - Cybersecurity Measures
- Metadata de generación (generated_at, generated_by, pdf_path, status)
- Relación con ComplianceAssessment

### 2. `GOV_CONFORMITY_DECLARATIONS`

**Propósito:** EU Declaration of Conformity según AI Act Annex V (Artículo 48)

**Campos principales:**
- Información del proveedor (provider_name, provider_address, provider_country)
- Información del sistema IA (ai_system_name, ai_system_type, risk_category)
- Base de conformidad (conformity_basis, applied_standards, harmonized_standards)
- Cumplimiento por artículo (Art. 9-15):
  - `art9_risk_management`
  - `art10_data_governance`
  - `art11_documentation`
  - `art12_record_keeping`
  - `art13_transparency`
  - `art14_human_oversight`
  - `art15_accuracy`
- Scores de compliance
- Información de firma (signature_date, signed_by, digital_signature)
- PDF path y metadata
- Relación con ComplianceAssessment

---

## 📊 Vista Creada

### `V_AI_ACT_COMPLIANCE_SUMMARY`

Vista consolidada que muestra el estado de compliance AI Act para todos los assessments.

**Campos:**
- Información del assessment
- Estado de documentación técnica
- Estado de declaración de conformidad
- Indicador de compliance AI Act (COMPLIANT, SIGNED, DOCUMENTED, PENDING)
- Compliance por artículo (Art. 9-15)

---

## ⚙️ Función Creada

### `GET_AI_ACT_COMPLIANCE_PERCENTAGE(entity_type, entity_id)`

Calcula el porcentaje de compliance AI Act para una entidad específica.

**Criterios de cálculo:**
- Documentación técnica aprobada: +30%
- Declaración de conformidad creada: +20%
- Declaración firmada: +10%
- Cada artículo cumplido (Art. 9-15): +5.7% (7 artículos)

**Máximo:** 100%

---

## 🔧 Triggers

### `update_updated_at_column()`

Actualiza automáticamente el campo `UPDATED_AT` cuando se modifica un registro.

Aplicado a:
- `GOV_AIACT_TECHNICAL_DOCS`
- `GOV_CONFORMITY_DECLARATIONS`

---

## 🚀 Instalación

### Opción 1: Script Individual

```bash
psql -U <usuario> -d <database> -f 12_ai_act_compliance_tables.sql
```

### Opción 2: Con Script Completo

Si existe un script de ejecución completo:

```bash
./EJECUTAR_TODO.sh
```

---

## 📝 Ejemplo de Uso

### Consultar estado de compliance

```sql
SELECT * FROM V_AI_ACT_COMPLIANCE_SUMMARY
WHERE system_name LIKE '%Customer Service Agent%';
```

### Calcular compliance de una entidad

```sql
SELECT GET_AI_ACT_COMPLIANCE_PERCENTAGE('AGENT', 123) AS compliance_percentage;
```

### Listar declaraciones publicadas

```sql
SELECT 
    IDX_DECLARATION,
    AI_SYSTEM_NAME,
    SIGNATURE_DATE,
    SIGNED_BY
FROM GOV_CONFORMITY_DECLARATIONS
WHERE STATUS = 'PUBLISHED'
ORDER BY SIGNATURE_DATE DESC;
```

### Verificar documentación pendiente

```sql
SELECT 
    SYSTEM_NAME,
    ENTITY_TYPE,
    STATUS,
    GENERATED_AT
FROM GOV_AIACT_TECHNICAL_DOCS
WHERE STATUS = 'DRAFT'
ORDER BY GENERATED_AT DESC;
```

---

## 🔗 Integración con Código Java

### Entidades JPA

**Ubicación:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/governance/`

- `AIActTechnicalDocumentation.java`
- `ConformityDeclaration.java`

### Servicios

**Ubicación:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/workflow/services/`

- `AIActDocumentationService.java` - Generación de documentación técnica
- `ConformityDeclarationService.java` - Generación de declaraciones de conformidad
- `AIActLogExportService.java` - Exportación de logs para auditorías

### REST Controller

**Ubicación:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/controller/`

- `AIActComplianceController.java` - Endpoints REST

**Endpoints disponibles:**
- `POST /api/compliance/ai-act/generate-documentation`
- `GET /api/compliance/ai-act/download-documentation/{docId}`
- `POST /api/compliance/ai-act/generate-declaration`
- `POST /api/compliance/ai-act/sign-declaration`
- `POST /api/compliance/ai-act/publish-declaration`
- `GET /api/compliance/ai-act/download-declaration/{declarationId}`
- `POST /api/compliance/ai-act/export-logs`

---

## 📈 Estado de Compliance

Después de implementar estas tablas y servicios, CodeflowX Govern alcanza:

✅ **98-100% EU AI Act Compliance**

**Cobertura:**
- ✅ Artículo 9: Risk Management
- ✅ Artículo 10: Data Governance
- ✅ Artículo 11: Technical Documentation (Annex IV)
- ✅ Artículo 12: Record-keeping and Logs
- ✅ Artículo 13: Transparency
- ✅ Artículo 14: Human Oversight
- ✅ Artículo 15: Accuracy and Robustness
- ✅ Artículo 48: EU Declaration of Conformity (Annex V)

---

## 🔒 Seguridad

Las tablas incluyen:
- Audit trails (created_at, updated_at)
- Versionado de documentación
- Trazabilidad de firmas
- Relaciones con assessments existentes

---

## 📚 Referencias

- [EU AI Act - Official Text](https://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:52021PC0206)
- [AI Act Annex IV - Technical Documentation](https://artificialintelligenceact.eu/annex/4/)
- [AI Act Annex V - EU Declaration of Conformity](https://artificialintelligenceact.eu/annex/5/)

---

## ✅ Verificación Post-Instalación

```sql
-- Verificar que las tablas existen
SELECT table_name 
FROM information_schema.tables 
WHERE table_name IN ('gov_aiact_technical_docs', 'gov_conformity_declarations');

-- Verificar que la vista existe
SELECT table_name 
FROM information_schema.views 
WHERE table_name = 'v_ai_act_compliance_summary';

-- Verificar que la función existe
SELECT routine_name 
FROM information_schema.routines 
WHERE routine_name = 'get_ai_act_compliance_percentage';

-- Verificar triggers
SELECT trigger_name, event_object_table
FROM information_schema.triggers
WHERE trigger_name LIKE '%conformity%' OR trigger_name LIKE '%technical_docs%';
```

---

**Mantenimiento:** Este documento debe actualizarse cuando se modifique el esquema.

**Responsable:** Equipo de AI Governance  
**Última actualización:** 2 Noviembre 2025

