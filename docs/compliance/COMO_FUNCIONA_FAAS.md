# CÓMO FUNCIONA FaaS (Framework as a Software)

**Fecha:** Noviembre 2025  
**Propósito:** Explicar el funcionamiento práctico de FaaS mediante un ejemplo completo

---

## 🎯 Concepto General

FaaS permite adaptar CodeflowX a diferentes sectores (banca, educación, salud, etc.) **sin modificar el esquema de base de datos**. En lugar de crear tablas específicas por sector, usamos:

1. **Catálogos** (`cor_catalogue`, `cor_catalogitem`) para definir sectores y sus atributos
2. **Configuraciones** (`cor_configs`) para almacenar frameworks FaaS en YAML/JSON
3. **Políticas Drools** (`cor_policyregistry`) para reglas sectoriales versionadas
4. **Metadatos JSONB** en tablas core (`gov_governance_events.metadata`, `gov_governance_results.result_payload`)

---

## 🔄 Flujo de Funcionamiento

### 1. Inicialización (Startup)

Al arrancar la aplicación, `SectorMetamodelService` carga:

```
┌─────────────────────────────────────┐
│  SectorMetamodelService             │
│  @PostConstruct                      │
└──────────────┬──────────────────────┘
               │
               ├─> Carga sector_catalogue.json
               │   └─> Lista de sectores (EDU_SERVICES, FIN_BANKING, etc.)
               │
               ├─> Carga faas_frameworks.yaml
               │   └─> Configuración de cada framework (workflows, reglas, microservicios)
               │
               └─> Carga policy_registry.json
                   └─> Políticas Drools por sector
```

**Resultado:** Mapas en memoria (`sectorsByCode`, `frameworksByKey`, `frameworksBySector`)

### 2. Activación de un Framework

Cuando un usuario selecciona un sector (ej. Educación), el sistema:

1. **Consulta el catálogo:**
   ```java
   SectorMetamodelService.findSector("EDU_SERVICES")
   // Retorna: SectorDefinition con atributos, regulaciones, risk_profile
   ```

2. **Carga el framework asociado:**
   ```java
   SectorMetamodelService.findFramework("EDU_SERVICES_v1.0.0")
   // Retorna: FaasFrameworkConfig con:
   //   - workflows: [compliance_monitoring_v1, fria_process_v1, ...]
   //   - rules: [EDU_SERVICES_POLICY_BASE_v1.0.0]
   //   - microservices: [leka-conformity-assessment, leka-fria-generator, ...]
   //   - dashboards: [/console/gobierno/compliance/sector-dashboard.zul]
   ```

3. **Activa workflows BPMN:**
   - Flowable carga los BPMN definidos en `workflows[].bpmn`
   - Los workflows se parametrizan con `sector_code` y `use_case_code`

4. **Carga reglas Drools:**
   - KieSession carga las reglas del `policy_code` asociado
   - Las reglas validan eventos de gobernanza según el sector

### 3. Ejecución de un Evento de Gobernanza

Cuando ocurre un evento (ej. "Iniciar evaluación de conformidad"):

```
┌─────────────────────────────────────────────────────────────┐
│  1. Usuario inicia evaluación desde ZUL                     │
│     └─> InitiateConformityAssessmentViewModel                 │
└──────────────┬──────────────────────────────────────────────┘
               │
               ├─> 2. Flowable inicia proceso BPMN
               │   └─> conformity-assessment-process.bpmn20.xml
               │       (parametrizado con sector_code: "EDU_SERVICES")
               │
               ├─> 3. Service Task ejecuta delegate
               │   └─> EvaluateConformityDelegate
               │       └─> Crea gov_governance_events con:
               │           {
               │             "event_id": "...",
               │             "sector_code": "EDU_SERVICES",
               │             "use_case_code": "EDU_PROCTORING_AI",
               │             "metadata": {
               │               "regulation_refs": ["EU AI Act Art. 6", "RGPD"],
               │               "geo_scope": ["EU", "LATAM"]
               │             }
               │           }
               │
               ├─> 4. Drools valida el evento
               │   └─> KieSession evalúa reglas de EDU_SERVICES_POLICY_BASE_v1.0.0
               │       └─> Verifica: sector_code válido, regulation_refs presentes, etc.
               │
               ├─> 5. Microservicio Python procesa evaluación
               │   └─> leka-conformity-assessment
               │       └─> POST /api/v1/assess
               │           {
               │             "sector_code": "EDU_SERVICES",
               │             "model_id": "...",
               │             "use_case": "proctoring"
               │           }
               │
               └─> 6. Resultado se guarda en gov_governance_results
                   └─> result_payload (JSONB) contiene métricas específicas del sector
```

### 4. Visualización en Dashboard

El dashboard sectorial (`sector-dashboard.zul`) muestra:

1. **Sectores disponibles** (desde `sector_catalogue.json`)
2. **Frameworks activos** (desde `faas_frameworks.yaml`)
3. **Métricas del sector** (desde `gov_governance_results` filtrado por `sector_code`)

```java
// ViewModel consulta el metamodelo
List<SectorDefinition> sectors = metamodelService.listSectors();
List<FaasFrameworkConfig> frameworks = metamodelService.listFrameworksBySector("EDU_SERVICES");

// Y luego consulta métricas reales desde la BD
List<GovernanceResult> results = governanceResultRepository
    .findBySectorCode("EDU_SERVICES", last30Days);
```

---

## 📦 Componentes de un Framework FaaS

### 1. Definición en `faas_frameworks.yaml`

```yaml
- config_key: EDU_SERVICES_v1.0.0
  sector_code: EDU_SERVICES
  workflows:
    - id: edu_consent_hub
      bpmn: processes/education/edu-consent-hub.bpmn20.xml
      sla_profile: high_risk_default
  rules:
    - policy_code: EDU_SERVICES_POLICY_BASE_v1.0.0
      package: com.codeflowx.rules.faas.education
  microservices:
    - id: leka-llm-evaluation
      activation: mandatory
  dashboards:
    - view: '/console/gobierno/compliance/education/edu-dashboard.zul'
      dataset_id: governance_metrics_edu_services
```

### 2. Workflow BPMN Específico

El workflow `edu-consent-hub.bpmn20.xml` puede tener tareas específicas del sector:

- **User Task:** "Solicitar consentimiento parental" (específico de educación)
- **Service Task:** "Validar edad del estudiante" (delegate Java)
- **Service Task:** "Evaluar sesgo en contenido educativo" (llama a microservicio Python)

### 3. Reglas Drools Específicas

```java
package com.codeflowx.rules.faas.education;

rule "EDU - Consentimiento parental obligatorio para menores de 16"
    when
        $event : Map(
            this["sector_code"] == "EDU_SERVICES",
            this["use_case_code"] contains "STUDENT",
            this["metadata"]["student_age"] < 16,
            this["metadata"]["parental_consent"] == null
        )
    then
        policyViolations.add("MISSING_PARENTAL_CONSENT");
    end
```

### 4. ViewModel/ZUL Específico

`EduDashboardViewModel.java` puede tener lógica específica:

```java
@Command
public void initiateConsentWorkflow() {
    // Inicia workflow específico de educación
    runtimeService.startProcessInstanceByKey(
        "edu_consent_hub",
        variables.put("sector_code", "EDU_SERVICES")
    );
}
```

---

## 🎓 Ejemplo Práctico: Framework de Educación

Hemos implementado `EDU_SERVICES_v1.0.0` completamente como ejemplo:

1. ✅ **Definición en YAML** (`faas_frameworks.yaml` - actualizado con workflow específico)
2. ✅ **BPMN específico** (`processes/education/edu-consent-hub.bpmn20.xml`)
3. ✅ **Reglas Drools** (`rules/faas/education/education-policies.drl`)
4. ✅ **Delegates Java** (`ValidateStudentAgeDelegate`, `EvaluateContentBiasDelegate`, `ApplyBiasMitigationDelegate`, `RegisterConsentDelegate`)
5. ⏳ **ViewModel/ZUL** (`EduDashboardViewModel.java`, `edu-dashboard.zul`) - Pendiente
6. ✅ **Integración con microservicios** (llamadas desde delegates configuradas)

### Componentes Implementados

#### 1. BPMN Workflow (`edu-consent-hub.bpmn20.xml`)
- **Validación de edad** del estudiante (RGPD Art. 8)
- **Solicitud de consentimiento parental** (menores de 16)
- **Solicitud de consentimiento del estudiante** (mayores de 16)
- **Evaluación de sesgo** en contenido educativo
- **Mitigación de sesgo** si se detecta
- **Registro de consentimiento** para trazabilidad

#### 2. Reglas Drools (`education-policies.drl`)
- Consentimiento parental obligatorio para menores de 16 (RGPD Art. 8)
- Consentimiento del estudiante para mayores de 16 (RGPD Art. 6)
- Transparencia en uso de IA para evaluación (LOMLOE)
- Detección de sesgo alto en contenido educativo
- Requisitos de privacidad para proctoring (RGPD Art. 25)
- Política de retención de datos (RGPD Art. 5.1.e)
- Accesibilidad universal (LOMLOE)

#### 3. Delegates Java
- `ValidateStudentAgeDelegate`: Valida edad y determina si requiere consentimiento parental
- `EvaluateContentBiasDelegate`: Llama al microservicio de detección de sesgo
- `ApplyBiasMitigationDelegate`: Aplica técnicas de mitigación de sesgo
- `RegisterConsentDelegate`: Registra consentimiento en `gov_governance_events` y `gov_governance_results`

### Cómo Usar el Framework

1. **Iniciar el workflow desde ZUL:**
   ```java
   runtimeService.startProcessInstanceByKey(
       "edu_consent_hub",
       variables.put("sector_code", "EDU_SERVICES")
                  .put("use_case_code", "EDU_PROCTORING_AI")
                  .put("studentAge", 15)
   );
   ```

2. **El workflow ejecuta automáticamente:**
   - Valida la edad del estudiante
   - Solicita consentimiento apropiado (parental o del estudiante)
   - Evalúa sesgo en contenido
   - Aplica mitigación si es necesario
   - Registra todo en eventos de gobernanza

3. **Las reglas Drools validan:**
   - Que el evento tenga `sector_code: "EDU_SERVICES"`
   - Que tenga consentimiento apropiado según la edad
   - Que cumpla con RGPD y LOMLOE

---

## 🔍 Ventajas de este Enfoque

1. **Sin migraciones DB:** Nuevos sectores se añaden editando YAML/JSON
2. **Versionado:** Cada framework tiene versión (`v1.0.0`, `v1.1.0`)
3. **Reutilización:** Workflows base se parametrizan por sector
4. **Extensibilidad:** Nuevos sectores no requieren cambios en código core
5. **Trazabilidad:** Todo queda registrado en `gov_governance_events` con `sector_code`

---

## 📝 Próximos Pasos

1. Implementar framework completo de Educación como ejemplo
2. Documentar cómo crear un nuevo framework desde cero
3. Crear tests de integración que validen el flujo completo

