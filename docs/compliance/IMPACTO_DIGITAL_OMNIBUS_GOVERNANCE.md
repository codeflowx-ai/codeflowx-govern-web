# IMPACTO DIGITAL OMNIBUS EN APLICACIÓN DE GOBIERNO Y CUMPLIMIENTO

**Fecha:** Noviembre 2025
**Documento Base:** COM(2025) 837 - Digital Omnibus
**Aplicación:** CodeflowX Govern - Plataforma AI Governance & Compliance
**Alcance:** Análisis de impacto en sistemas de gobierno y cumplimiento

---

## 📋 RESUMEN EJECUTIVO

El **Digital Omnibus** (COM(2025) 837) introduce cambios técnicos que afectan directamente a la aplicación de gobierno y cumplimiento, principalmente en:

1. **Single-Entry Point para Reportes de Incidentes** - Impacto **ALTO** 🔴
2. **Clarificaciones GDPR sobre Procesamiento de Datos para IA** - Impacto **MEDIO** 🟡
3. **Extensión de Exenciones a Small Mid-Caps (SMCs)** - Impacto **BAJO** 🟢
4. **Directrices Futuras de Aplicación** - Impacto **MEDIO** 🟡

---

## 🔴 IMPACTO ALTO: SINGLE-ENTRY POINT PARA REPORTES DE INCIDENTES

### **Cambio Normativo**

El Digital Omnibus establece un **single-entry point** gestionado por ENISA para consolidar todas las obligaciones de reporte de incidentes bajo un único mecanismo ("report once, share many").

**Citas textuales del documento COM(2025) 837:**

> "The amendments presented in this Regulation will introduce a single-entry point through which entities can simultaneously fulfil their incident reporting obligations under multiple legal acts. Through fostering a 'report once, share many' principle, the single-entry point will reduce administrative burden for entities, while ensuring effective and secure flow of information about security incidents to the recipients defined in respective legislation."

> "The proposal establishes the obligation on ENISA to develop the single entry-point, taking into account the single-reporting platform for notifications of actively exploited vulnerabilities and sever incidents under Regulation (EU) 2024/2847 (the Cyber Resilience Act (CRA)). It mandates specific requirements for the tool, as a secure conduit of information reported by entities and dispatched to the competent authorities. It leaves unchanged the underlying legal requirements for incident reporting but optimizes significantly the workflow and the resources required from entities."

> "The proposal also mandates the use of the single-entry point for a series of closely interconnected incident reporting obligations set in the Directive (EU) 2022/2555 (NIS2 Directive), Regulation (EU) 2016/679 (GDPR), Regulation (EU) 2022/2554 (DORA), Regulation (EU) 910/2014 (eIDAS Regulation), and Directive (EU) 2022/2557 (CER Directive)."

**Regulaciones afectadas:**
- NIS2 Directive (EU) 2022/2555
- GDPR (EU) 2016/679
- DORA (EU) 2022/2554
- eIDAS Regulation (EU) 910/2014
- CER Directive (EU) 2022/2557
- **AI Act (EU) 2024/1689** - Art. 73 (Serious incidents) *(implícito en el contexto de "multiple legal acts")*

**Nota:** El documento menciona que el single-entry point debe tener en cuenta la plataforma del Cyber Resilience Act (CRA) para vulnerabilidades activamente explotadas e incidentes severos.

### **Impacto en CodeflowX Govern**

#### **1. Workflows BPMN de Notificación**

**Archivos afectados:**
- `processes/compliance/incident-reporting-process.bpmn20.xml`
- `workflow.delegates.incident.NotifyMarketSurveillanceAuthorityDelegate`
- Todos los workflows sectoriales con `incident-reporting-process.bpmn`

**Cambios requeridos:**

```java
// ANTES: Notificación directa a autoridad específica
public class NotifyMarketSurveillanceAuthorityDelegate {
    public void execute(DelegateExecution execution) {
        // POST directo a autoridad nacional
        restTemplate.post(aiActAuthorityUrl, incidentPayload);
    }
}

// DESPUÉS: Notificación a single-entry point ENISA
public class NotifyMarketSurveillanceAuthorityDelegate {
    public void execute(DelegateExecution execution) {
        // POST a single-entry point ENISA
        // ENISA distribuye automáticamente a autoridades competentes
        restTemplate.post(enisaSingleEntryPointUrl, consolidatedPayload);
    }
}
```

#### **2. Payload Consolidado**

El single-entry point requiere un **payload consolidado** que incluya información de múltiples marcos regulatorios:

**Estructura requerida:**
```json
{
  "incidentId": "uuid",
  "reportedAt": "timestamp",
  "reportingEntity": {
    "name": "CodeflowX",
    "country": "ES",
    "sector": "ICT"
  },
  "incidentDetails": {
    "type": "AI_SYSTEM_MALFUNCTION",
    "severity": "HIGH",
    "affectedUsers": 1500,
    "description": "..."
  },
  "regulatoryFrameworks": [
    {
      "framework": "AI_ACT",
      "article": "73",
      "authority": "Market Surveillance Authority",
      "payload": { /* AI Act specific */ }
    },
    {
      "framework": "GDPR",
      "article": "33",
      "authority": "Data Protection Authority",
      "payload": { /* GDPR specific */ }
    },
    {
      "framework": "NIS2",
      "article": "23",
      "authority": "CSIRT",
      "payload": { /* NIS2 specific */ }
    }
  ],
  "metadata": {
    "source": "CODEFLOWX_GOVERN",
    "workflowInstanceId": "bpmn-instance-id"
  }
}
```

#### **3. Integración con ENISA Single-Entry Point**

**Nuevo servicio requerido:**

```java
@Service
public class EnisaSingleEntryPointService {

    @Value("${enisa.single-entry-point.url}")
    private String enisaEndpoint;

    @Value("${enisa.api-key}")
    private String apiKey;

    /**
     * Reporta incidente consolidado al single-entry point ENISA
     * ENISA distribuye automáticamente a autoridades competentes según framework
     */
    public EnisaReportResponse reportIncident(ConsolidatedIncidentReport report) {
        // 1. Consolidar payloads de múltiples frameworks
        ConsolidatedPayload payload = consolidateFrameworks(report);

        // 2. Enviar a single-entry point
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-ENISA-API-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<EnisaReportResponse> response = restTemplate.postForEntity(
            enisaEndpoint + "/api/v1/incidents/report",
            new HttpEntity<>(payload, headers),
            EnisaReportResponse.class
        );

        // 3. Registrar en ImmutableLog
        immutableLogService.log(
            "ENISA_SINGLE_ENTRY_POINT_REPORT",
            report.getIncidentId(),
            response.getBody()
        );

        return response.getBody();
    }

    /**
     * Consulta estado de reporte enviado
     */
    public EnisaReportStatus getReportStatus(String enisaReportId) {
        // GET /api/v1/incidents/{reportId}/status
    }
}
```

#### **4. Actualización de Delegates BPMN**

**Modificar:** `NotifyMarketSurveillanceAuthorityDelegate`

```java
@Component
public class NotifyMarketSurveillanceAuthorityDelegate implements JavaDelegate {

    @Autowired
    private EnisaSingleEntryPointService enisaService;

    @Autowired
    private ImmutableLogService immutableLogService;

    @Override
    public void execute(DelegateExecution execution) {
        // Obtener datos del incidente
        String incidentId = (String) execution.getVariable("incidentId");
        String severity = (String) execution.getVariable("severity");
        boolean isSerious = (Boolean) execution.getVariable("isSerious");

        // Construir reporte consolidado
        ConsolidatedIncidentReport report = buildConsolidatedReport(execution);

        // Enviar a single-entry point ENISA
        EnisaReportResponse response = enisaService.reportIncident(report);

        // Guardar respuesta en variables BPMN
        execution.setVariable("enisaReportId", response.getReportId());
        execution.setVariable("authorityNotificationId", response.getReportId());
        execution.setVariable("authorityNotificationTimestamp", Instant.now());
        execution.setVariable("notificationStatus", response.getStatus());

        // Log inmutable
        immutableLogService.log(
            "AI_ACT_SERIOUS_INCIDENT_REPORTED",
            incidentId,
            Map.of(
                "enisaReportId", response.getReportId(),
                "frameworks", report.getRegulatoryFrameworks(),
                "status", response.getStatus()
            )
        );
    }
}
```

#### **5. Configuración de Propiedades**

**Nuevas propiedades requeridas en `application.properties`:**

```properties
# ENISA Single-Entry Point
enisa.single-entry-point.url=https://single-entry-point.enisa.europa.eu
enisa.api-key=${ENISA_API_KEY}
enisa.timeout=30000

# Frameworks habilitados para reporte consolidado
enisa.frameworks.enabled=AI_ACT,GDPR,NIS2,DORA
```

### **Acciones Requeridas**

- [ ] **Crear servicio `EnisaSingleEntryPointService`** con integración REST
- [ ] **Modificar `NotifyMarketSurveillanceAuthorityDelegate`** para usar single-entry point
- [ ] **Actualizar workflows BPMN** para incluir variables `enisaReportId`
- [ ] **Crear DTOs** para payload consolidado (`ConsolidatedIncidentReport`, `EnisaReportResponse`)
- [ ] **Actualizar documentación técnica** de procesos de notificación
- [ ] **Testing E2E** con entorno sandbox ENISA (cuando esté disponible)
- [ ] **Migración de reportes históricos** (si aplica)

**Timeline estimado:** 2-3 semanas
**Prioridad:** 🔴 **ALTA** (requerido antes de entrada en vigor)

---

## 🟡 IMPACTO MEDIO: CLARIFICACIONES GDPR SOBRE PROCESAMIENTO DE DATOS PARA IA

### **Cambio Normativo**

El Digital Omnibus clarifica aspectos del GDPR relacionados con:
- Procesamiento de datos personales para **entrenamiento y desarrollo de sistemas de IA**
- Criterios para determinar si datos pseudonimizados constituyen datos personales
- Compatibilidad de procesamiento para fines científicos

**Citas textuales del documento COM(2025) 837:**

> "In this context, the amendments contained in this proposal aim to address those concerns notably by clarifying certain key definitions, for instance the notions of personal data; by facilitating compliance, for instance by supporting controllers with respect to the criteria and means to determine whether data resulting from pseudonymisation does not constitute personal data, in relation to information requirements and data breach notifications to supervisory authorities; **as well as by clarifying certain aspects as to the processing of data for AI training and development.**"

> "The proposed amendments address also the lack of clarity about the conditions for scientific research by providing a definition of scientific research, further clarifying that further processing for scientific purposes is compatible with the initial purpose of processing and by clarifying that scientific research constitutes a legitimate interest."

### **Impacto en CodeflowX Govern**

#### **1. Procesamiento de Datos para Entrenamiento de IA**

**Clarificación:** El procesamiento de datos personales para entrenamiento de IA es compatible con el propósito inicial si:
- Se aplican medidas técnicas y organizativas apropiadas
- Se respetan los principios de minimización y limitación de finalidad
- Se mantiene documentación técnica (Art. 11 AI Act)

**Archivos afectados:**
- `services/compliance/GDPRPrivacyService.java`
- `services/compliance/DataPortabilityService.java`
- Workflows de procesamiento de datos para entrenamiento

**Cambios requeridos:**

```java
@Service
public class GDPRPrivacyService {

    /**
     * Valida procesamiento de datos personales para entrenamiento de IA
     * según clarificaciones Digital Omnibus
     */
    public ProcessingLegality validateAITrainingProcessing(
            PersonalDataProcessingRequest request) {

        ProcessingLegality legality = new ProcessingLegality();

        // 1. Verificar medidas técnicas y organizativas
        boolean hasTechnicalMeasures = validateTechnicalMeasures(request);
        boolean hasOrganizationalMeasures = validateOrganizationalMeasures(request);

        // 2. Verificar minimización de datos
        boolean dataMinimized = validateDataMinimization(request);

        // 3. Verificar documentación técnica (Art. 11 AI Act)
        boolean hasTechnicalDocumentation = validateTechnicalDocumentation(request);

        // 4. Determinar base legal
        if (hasTechnicalMeasures && hasOrganizationalMeasures &&
            dataMinimized && hasTechnicalDocumentation) {
            // Compatible con propósito inicial según Digital Omnibus
            legality.setLegalBasis(LegalBasis.COMPATIBLE_PURPOSE);
            legality.setCompatible(true);
            legality.setJustification(
                "Procesamiento compatible según clarificaciones Digital Omnibus " +
                "para entrenamiento de IA con medidas técnicas y organizativas apropiadas"
            );
        } else {
            legality.setCompatible(false);
            legality.setRequiredActions(identifyGaps(request));
        }

        return legality;
    }

    /**
     * Determina si datos pseudonimizados constituyen datos personales
     * según criterios clarificados en Digital Omnibus
     */
    public boolean isPseudonymizedDataPersonal(PseudonymizedData data) {
        // Criterios según Digital Omnibus:
        // 1. ¿Existe información adicional que permita re-identificación?
        // 2. ¿El responsable tiene acceso a la clave de pseudonimización?
        // 3. ¿Es razonablemente probable la re-identificación?

        if (data.hasReidentificationKey() &&
            data.getController().hasAccessToKey()) {
            return true; // Sigue siendo dato personal
        }

        if (data.hasAdditionalInformation() &&
            isReidentificationReasonablyLikely(data)) {
            return true;
        }

        return false; // No constituye dato personal
    }
}
```

#### **2. Documentación de Procesamiento para IA**

**Nuevo campo requerido en registros de procesamiento:**

```sql
-- Extensión tabla de registros de procesamiento
ALTER TABLE GDPR_PROCESSING_RECORDS
ADD COLUMN PRCAITRAININGPURPOSE BOOLEAN DEFAULT FALSE;

ALTER TABLE GDPR_PROCESSING_RECORDS
ADD COLUMN PRCAITRAININGMEASURES JSONB;

ALTER TABLE GDPR_PROCESSING_RECORDS
ADD COLUMN PRCAITRAININGCOMPATIBILITY TEXT;
```

#### **3. Workflows de Entrenamiento de Modelos**

**Actualizar workflows BPMN** que procesan datos para entrenamiento:

```xml
<serviceTask id="validateAITrainingProcessing"
             name="Validar procesamiento para entrenamiento IA"
             flowable:delegateExpression="${gdprPrivacyService.validateAITrainingProcessing}">
  <documentation>
    Valida procesamiento de datos personales para entrenamiento de IA
    según clarificaciones Digital Omnibus
  </documentation>
</serviceTask>
```

### **Acciones Requeridas**

- [ ] **Extender `GDPRPrivacyService`** con métodos de validación para entrenamiento IA
- [ ] **Actualizar registros de procesamiento** con campos de entrenamiento IA
- [ ] **Modificar workflows** de procesamiento de datos para incluir validación
- [ ] **Actualizar documentación** de políticas de privacidad
- [ ] **Crear guías** para usuarios sobre procesamiento compatible

**Timeline estimado:** 1-2 semanas
**Prioridad:** 🟡 **MEDIA**

---

## 🟢 IMPACTO BAJO: EXTENSIÓN DE EXENCIONES A SMALL MID-CAPS

### **Cambio Normativo**

El Digital Omnibus extiende las exenciones que ya aplicaban a **SMEs** (Small and Medium Enterprises) a **SMCs** (Small Mid-Cap companies) en:
- Data legislation
- **AI Act (EU) 2024/1689**

**Cita textual del documento COM(2025) 837:**

> "The amendments seek to streamline the rules, reducing the number or laws and harmonising provisions. They cut administrative costs by simplifying provisions and procedures. **They relieve small mid-caps from certain obligations across the data legislation and Regulation (EU) 2024/1689 (the Artificial Intelligence Act), in addition to small and micro-enterprises already covered by a special regime.**"

> "In addition, to further assist smaller businesses, the rules that facilitate compliance with the EU data legislation for small and medium-sized enterprises (SMEs) are extended to include small mid-cap companies (SMCs)."

### **Impacto en CodeflowX Govern**

#### **1. Clasificación de Clientes**

**Nuevo campo requerido:**

```sql
-- Extensión tabla de organizaciones/clientes
ALTER TABLE ORGORGANIZATIONS
ADD COLUMN ORGISSMC BOOLEAN DEFAULT FALSE;

ALTER TABLE ORGORGANIZATIONS
ADD COLUMN ORGSMCCLASSIFICATIONDATE TIMESTAMP;
```

#### **2. Lógica de Exenciones**

**Actualizar servicios de compliance:**

```java
@Service
public class ComplianceExemptionService {

    /**
     * Determina si organización tiene exenciones según Digital Omnibus
     */
    public ComplianceExemptions getExemptions(Organization org) {
        ComplianceExemptions exemptions = new ComplianceExemptions();

        // Exenciones aplicables a SMEs y SMCs según Digital Omnibus
        if (org.isSME() || org.isSMC()) {
            exemptions.setAiActExemptions(getAiActExemptions(org));
            exemptions.setDataActExemptions(getDataActExemptions(org));
        }

        return exemptions;
    }

    private List<AiActExemption> getAiActExemptions(Organization org) {
        List<AiActExemption> exemptions = new ArrayList<>();

        // Exenciones específicas para SMEs/SMCs según Digital Omnibus
        // (detalles pendientes de publicación de propuesta específica AI Act)

        return exemptions;
    }
}
```

### **Acciones Requeridas**

- [ ] **Extender entidad `Organization`** con campo `isSMC`
- [ ] **Actualizar servicios** de clasificación de organizaciones
- [ ] **Modificar lógica de exenciones** en workflows de compliance
- [ ] **Actualizar documentación** de exenciones aplicables

**Timeline estimado:** 1 semana
**Prioridad:** 🟢 **BAJA** (afecta principalmente a clientes, no a la plataforma)

---

## 🟡 IMPACTO MEDIO: DIRECTRICES FUTURAS DE APLICACIÓN

### **Cambio Normativo**

La Comisión priorizará la emisión de **directrices** para apoyar la aplicación uniforme del AI Act, incluyendo:
- Directrices sobre aspectos específicos del AI Act
- Clarificaciones sobre definiciones
- Guías de interpretación

**Cita textual del documento COM(2025) 837:**

> "Stakeholders have stressed repeatedly that, in many instances, the simplification effort is less about modifying the rules, and more about providing clarity on their application. The Commission is prioritising a series of guidelines aimed at supporting the uniform application of the rules, without prejudice to the interpretations of the Court of Justice."

> "**To support the application of the Artificial Intelligence Act, the Commission continues to prioritise issuing guidelines on several aspects, as further detailed in the explanatory memorandum for the Digital Omnibus proposal amending the Artificial Intelligence Act.**"

### **Impacto en CodeflowX Govern**

#### **1. Sistema de Actualización de Directrices**

**Nuevo módulo requerido:**

```java
@Service
public class AiActGuidelinesService {

    /**
     * Obtiene directrices vigentes del AI Act
     */
    public List<AiActGuideline> getActiveGuidelines() {
        // Consultar base de datos de directrices
        // Priorizar directrices más recientes
    }

    /**
     * Notifica cambios en directrices a usuarios relevantes
     */
    public void notifyGuidelineUpdates(AiActGuideline newGuideline) {
        // Notificar a compliance officers
        // Actualizar workflows afectados
    }
}
```

#### **2. Integración con Workflows**

Las directrices pueden requerir actualizaciones en:
- Criterios de clasificación de alto riesgo
- Requisitos de documentación técnica
- Procedimientos de evaluación de conformidad

### **Acciones Requeridas**

- [ ] **Monitorear publicación de directrices** por la Comisión
- [ ] **Crear sistema de gestión de directrices** en la plataforma
- [ ] **Actualizar workflows** según directrices publicadas
- [ ] **Notificar a usuarios** sobre cambios en directrices

**Timeline estimado:** Continuo (según publicación de directrices)
**Prioridad:** 🟡 **MEDIA**

---

## 📊 MATRIZ DE IMPACTO RESUMIDA

| Área | Impacto | Componentes Afectados | Timeline | Prioridad |
|------|---------|----------------------|----------|-----------|
| **Single-Entry Point ENISA** | 🔴 ALTO | Workflows BPMN, Delegates, Servicios REST | 2-3 semanas | 🔴 ALTA |
| **Clarificaciones GDPR IA** | 🟡 MEDIO | GDPRPrivacyService, Workflows entrenamiento | 1-2 semanas | 🟡 MEDIA |
| **Exenciones SMCs** | 🟢 BAJO | Entidad Organization, Servicios clasificación | 1 semana | 🟢 BAJA |
| **Directrices Futuras** | 🟡 MEDIO | Sistema gestión directrices, Notificaciones | Continuo | 🟡 MEDIA |

---

## 🚀 PLAN DE ACCIÓN RECOMENDADO

### **Fase 1: Single-Entry Point (Inmediato)**
1. Diseñar integración con ENISA single-entry point
2. Crear servicio `EnisaSingleEntryPointService`
3. Modificar delegates BPMN
4. Testing con sandbox ENISA

### **Fase 2: Clarificaciones GDPR (Corto Plazo)**
1. Extender `GDPRPrivacyService` con validaciones IA
2. Actualizar registros de procesamiento
3. Modificar workflows de entrenamiento

### **Fase 3: Exenciones SMCs (Corto Plazo)**
1. Extender entidad `Organization`
2. Actualizar lógica de exenciones

### **Fase 4: Directrices (Continuo)**
1. Implementar sistema de gestión de directrices
2. Monitorear publicaciones de la Comisión
3. Actualizar workflows según directrices

---

## 📚 REFERENCIAS

### **Documentos Oficiales**

- **Documento Base:** COM(2025) 837 final - Proposal for a REGULATION OF THE EUROPEAN PARLIAMENT AND OF THE COUNCIL amending Regulations (EU) 2016/679, (EU) 2018/1724, (EU) 2018/1725, (EU) 2023/2854 and Directives 2002/58/EC, (EU) 2022/2555 and (EU) 2022/2557 as regards the simplification of the digital legislative framework (Digital Omnibus)
- **Fecha:** Brussels, 19.11.2025
- **Archivo local:** `COM_2025_837_1_EN_ACT_part1_v8_7PfpA6lmEBKkufln4cTC4PEy3Ac_121742.pdf`

### **Regulaciones Referenciadas**

- **AI Act:** Regulation (EU) 2024/1689
- **GDPR:** Regulation (EU) 2016/679
- **NIS2:** Directive (EU) 2022/2555
- **DORA:** Regulation (EU) 2022/2554
- **eIDAS:** Regulation (EU) 910/2014
- **CER:** Directive (EU) 2022/2557
- **CRA:** Regulation (EU) 2024/2847 (Cyber Resilience Act)

### **Citas Textuales Clave del Documento**

**Single-Entry Point:**
- "The proposal establishes the obligation on ENISA to develop the single entry-point, taking into account the single-reporting platform for notifications of actively exploited vulnerabilities and sever incidents under Regulation (EU) 2024/2847 (the Cyber Resilience Act (CRA))."
- "The amendments presented in this Regulation will introduce a single-entry point through which entities can simultaneously fulfil their incident reporting obligations under multiple legal acts."

**Clarificaciones GDPR:**
- "The proposed amendments address also the lack of clarity about the conditions for scientific research by providing a definition of scientific research, further clarifying that further processing for scientific purposes is compatible with the initial purpose of processing and by clarifying that scientific research constitutes a legitimate interest."
- "as well as by clarifying certain aspects as to the processing of data for AI training and development."

**Exenciones SMCs:**
- "They relieve small mid-caps from certain obligations across the data legislation and Regulation (EU) 2024/1689 (the Artificial Intelligence Act), in addition to small and micro-enterprises already covered by a special regime."

**Directrices:**
- "To support the application of the Artificial Intelligence Act, the Commission continues to prioritise issuing guidelines on several aspects, as further detailed in the explanatory memorandum for the Digital Omnibus proposal amending the Artificial Intelligence Act."

---

**Última actualización:** Noviembre 2025
**Próxima revisión:** Tras publicación de propuesta específica de enmiendas al AI Act
**Fuente verificada:** ✅ Citas extraídas directamente del documento COM(2025) 837
