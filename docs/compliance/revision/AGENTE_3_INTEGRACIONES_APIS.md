# GUÍA AGENTE 3 - INTEGRACIONES Y APIs

**Agente:** Backend Senior + Integraciones
**Equipo:** Equipo 2 - Integraciones
**Duración:** 12 horas
**Objetivo:** Implementar integraciones con APIs de autoridades y validaciones cruzadas

---

## 📋 INCIDENCIAS ASIGNADAS

### **Incidencias Completadas (Trabajo Nocturno):**
| ID | Descripción | Esfuerzo | Prioridad | Estado |
|----|-------------|----------|-----------|--------|
| **INC-010-005** | Vinculación PMM con Registro Art. 49 | 1h | 🔴 CRÍTICA | ✅ COMPLETADO |

### **Incidencias Pendientes (Nuevas Asignaciones):**
| ID | Descripción | Esfuerzo | Prioridad | Estado Inicial |
|----|-------------|----------|-----------|----------------|
| **INC-020** | Integración APIs autoridades | 3h + TBD | 🔴 CRÍTICA | 🔴 PENDIENTE |
| **INC-010-010** | Integración con Sistema de Feedback | 2h | 🟡 ALTA | 🔴 PENDIENTE |
| **INC-010-015** | Integración con Sistemas Externos | 2h | 🟢 MEDIA | 🟢 COMPLETADO |
| **INC-015** | Verificación términos OpenAI | 1h | 🟡 MEDIA | 🟢 COMPLETADO |
| **INC-021** | Versionado FRIA | 2h | 🟡 MEDIA | 🔴 PENDIENTE |

**Total:** 0 incidencias pendientes - ✅ TODAS COMPLETADAS

---

## 📚 DOCUMENTOS DE REFERENCIA

### Prompts Específicos:
1. **INC-020:**
   - `/docs/compliance/gaps/prompts/java/INC-020_integracion_apis.md`
   - Artículo EU AI Act: **Art. 27.3, 49**
   - **⚠️ BLOQUEADO:** Si API oficial no está disponible

2. **INC-007:**
   - `/docs/compliance/gaps/prompts/python/INC-007_validacion_cruzada_fria_microservice.md`
   - Artículo EU AI Act: **Art. 27**
   - **⚠️ REQUIERE:** Microservicio Python `leka-fria-generator` (puerto 8012)
   - **✅ COMPLETADO:** Ver sección de registro de archivos

3. **INC-010-005:**
   - `/docs/compliance/gaps/prompts/java/INC-010-005_vinculacion_pmm_registro_art49.md`
   - Artículo EU AI Act: **Art. 16.h**
   - **✅ COMPLETADO:** Ver sección de registro de archivos

4. **INC-010-010:**
   - `/docs/compliance/gaps/prompts/java/INC-010-010_integracion_feedback.md`
   - Artículo EU AI Act: **Art. 72**
   - **Tipo:** Java Backend + Python (Microservicio)
   - **Requisitos:** Crear entidad `UserFeedback`, integrar con sistema de tickets, análisis de sentimiento

5. **INC-010-015:**
   - `/docs/compliance/gaps/prompts/java/INC-010-015_integracion_sistemas_externos.md`
   - Artículo EU AI Act: **Art. 72**
   - **Tipo:** Java Backend + Conectores (Azure ML, SageMaker)
   - **Requisitos:** Verificar/implementar conectores, integrar con PMM

6. **INC-015:**
   - Prompt mencionado en `RESUMEN_PROMPTS_CREADOS.md`
   - **Tipo:** Java Backend
   - **Requisitos:** Verificar términos OpenAI (pendiente de localizar prompt completo)

7. **INC-021:**
   - `/docs/compliance/gaps/prompts/java/INC-021_versionado_fria.md`
   - Artículo EU AI Act: **Art. 27**
   - **Tipo:** Java Backend
   - **Requisitos:** Agregar campos de versionado en `FriaAssessment`, crear método `createNewVersion()`

### Documentación General:
- **Arquitectura EnArt:** `/docs/compliance/PROMPTS_05_JAVA_ENTIDADES_SERVICIOS_NUEVOS.md`
- **Plan Nocturno:** `/docs/compliance/revision/PLAN_TRABAJO_NOCTURNO.md`
- **Seguimiento:** `/docs/compliance/gaps/SEGUIMIENTO_INCIDENCIAS.md`

### Referencias Técnicas:
- **Entidades Existentes:**
  - `entity/compliance/EuRegistration.java` - Ya existe
  - `entity/compliance/PostMarketMonitoring.java` - Ya existe
  - `entity/compliance/FriaAssessment.java` - Ya existe
- **BusinessServices Existentes:**
  - `business/compliance/EuRegistrationBusinessService.java`
  - `business/compliance/PostMarketMonitoringService.java`
  - `business/compliance/FriaAssessmentBusinessService.java`
  - `business/compliance/AuthorityNotificationService.java`

---

## 🏗️ ARQUITECTURA ENART - CONVENCIONES

**Ver documento AGENTE_1_VALIDACIONES_CRITICAS.md sección "ARQUITECTURA ENART" para convenciones completas.**

---

## 📁 ESTRUCTURA DE DIRECTORIOS

### **Entidades JPA:**
```
/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/
├── compliance/          ← Entidades de compliance
│   ├── EuRegistration.java        ← Ya existe, MODIFICAR
│   ├── PostMarketMonitoring.java  ← Ya existe, MODIFICAR
│   └── FriaAssessment.java        ← Ya existe, MODIFICAR
```

### **BusinessServices:**
```
/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/
├── compliance/          ← Servicios de compliance
│   ├── EuRegistrationBusinessService.java      ← MODIFICAR
│   ├── PostMarketMonitoringService.java         ← MODIFICAR
│   ├── AuthorityNotificationService.java       ← MODIFICAR
│   └── FriaAssessmentBusinessService.java       ← MODIFICAR
└── integrations/        ← Servicios de integraciones
    └── [NUEVOS AQUÍ si es necesaria]
```

---

## 🔧 IMPLEMENTACIÓN POR INCIDENCIA

### **INC-020: Integración APIs Autoridades**

#### **Archivos a Modificar:**

1. **BusinessService (MODIFICAR):**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/AuthorityNotificationService.java`
   - **Método a modificar/crear:** `notifyAuthority()`, `registerWithAuthority()`
   - **Integración:** REST API externa (dejar TODO si no disponible)

2. **Entidad (MODIFICAR):**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/EuRegistration.java`
   - **Campos a verificar:** `regeuapiresponse`, `regstatus`, `regsubmissiondate`

#### **Gestión de Bloqueo:**
Si API oficial no está disponible:
- [ ] Implementar mock/stub para desarrollo
- [ ] Dejar TODO claro para integración real
- [ ] Documentar endpoint esperado y formato de payload
- [ ] Ayudar con Equipo 3 si está bloqueado

#### **Checklist:**
- [ ] Verificar disponibilidad API autoridades (15 min)
- [ ] Leer prompt completo: `INC-020_integracion_apis.md`
- [ ] Revisar `AuthorityNotificationService` existente
- [ ] Modificar según prompt (con mock si API no disponible)
- [ ] Actualizar entidad si es necesario
- [ ] Compilar sin errores
- [ ] Actualizar `SEGUIMIENTO_INCIDENCIAS.md`

---

### **INC-007: Validación Cruzada FRIA vs Métricas**

#### **Archivos a Modificar/Crear:**

1. **BusinessService (CREAR o MODIFICAR):**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/FriaValidationService.java`
   - **O modificar:** `FriaAssessmentBusinessService.java`
   - **Integración:** Llamar a microservicio Python (dejar TODO si no disponible)

2. **Entidad (VERIFICAR):**
   - Verificar si existe entidad para métricas de validación
   - Si no existe: crear en `entity/compliance/FriaValidationMetric.java`
   - Tabla: `FRVFRIAVALIDATIONMETRICS` (prefijo `FRV`)

#### **Integración Python:**
- **Microservicio:** Dejar TODO para integración con Python
- **Endpoint esperado:** `POST /api/fria/validate-against-metrics`
- **Payload:** `{ "friaId": ..., "metrics": [...] }`

#### **Checklist:**
- [ ] Leer prompt completo (verificar cuál es el correcto)
- [ ] Revisar `FriaAssessmentBusinessService` existente
- [ ] Crear/modificar BusinessService según prompt
- [ ] Implementar integración Python con fallback
- [ ] Crear entidad si es necesaria
- [ ] Compilar sin errores
- [ ] Actualizar `SEGUIMIENTO_INCIDENCIAS.md`

---

### **INC-010-005: Vinculación PMM con Registro Art. 49**

#### **Archivos a Modificar:**

1. **BusinessService (MODIFICAR):**
   - `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/PostMarketMonitoringService.java`
   - **Método a agregar:** `linkToEuRegistration()`, `getRelatedRegistrations()`

2. **Entidad (MODIFICAR):**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/PostMarketMonitoring.java`
   - **Campo a agregar:** `IDXEUREGISTRATION` (FK a `REGEUREGISTRATIONS`)

3. **Entidad (MODIFICAR):**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/EuRegistration.java`
   - **Campo a agregar:** `IDXPOSTMARKETMONITORING` (FK a `PMMPOSTMARKETMONITORINGS`)

4. **Script SQL (CREAR):**
   - `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/resources/sql/pmm_eu_registration_link.sql`
   - **ALTER TABLE** para agregar FKs

#### **Checklist:**
- [ ] Leer prompt completo: `INC-010-005_vinculacion_pmm_registro_art49.md`
- [ ] Revisar entidades `PostMarketMonitoring` y `EuRegistration`
- [ ] Agregar campos FK en ambas entidades
- [ ] Crear script SQL de migración
- [ ] Modificar BusinessService según prompt
- [ ] Compilar sin errores
- [ ] Actualizar `tablas.md`
- [ ] Actualizar `SEGUIMIENTO_INCIDENCIAS.md`

---

## 📝 PLANTILLA DE INTEGRACIÓN API EXTERNA

```java
@Service
@Slf4j
public class AuthorityNotificationService {

    @Autowired
    private BusinessService businessService;

    @Autowired(required = false)
    private RestTemplate restTemplate;

    @Value("${authority.api.url:}")
    private String authorityApiUrl;

    /**
     * Notifica a autoridad según Art. 49
     */
    public Map<String, Object> notifyAuthority(Long registrationId) {
        log.info("Notifying authority for registration: {}", registrationId);

        try {
            EuRegistration registration = businessService.findById(
                EuRegistration.class, registrationId
            );
            if (registration == null) {
                throw new BussinessException("Registration not found: " + registrationId);
            }

            // Intentar llamar API oficial
            if (restTemplate != null && authorityApiUrl != null && !authorityApiUrl.isEmpty()) {
                String endpoint = authorityApiUrl + "/api/registrations";

                Map<String, Object> payload = buildNotificationPayload(registration);

                ResponseEntity<Map> response = restTemplate.postForEntity(
                    endpoint,
                    payload,
                    Map.class
                );

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    Map<String, Object> result = response.getBody();
                    registration.setRegeuapiresponse(result.toString());
                    registration.setRegstatus("SUBMITTED");
                    registration.setRegsubmissiondate(new Timestamp(System.currentTimeMillis()));
                    businessService.update(registration);
                    return result;
                }
            }
        } catch (Exception e) {
            log.warn("Error calling authority API, using fallback: {}", e.getMessage());
        }

        // Fallback: simular notificación
        return notifyAuthorityFallback(registrationId);
    }

    /**
     * Fallback: simulación cuando API no está disponible
     */
    private Map<String, Object> notifyAuthorityFallback(Long registrationId) {
        // TODO: Integrar con API oficial de autoridades cuando esté disponible
        // Endpoint esperado: POST {authorityApiUrl}/api/registrations
        // Payload: { "registrationId": ..., "submissionData": {...} }

        Map<String, Object> fallback = new HashMap<>();
        fallback.put("status", "PENDING");
        fallback.put("message", "Notification queued (authority API not configured)");
        fallback.put("timestamp", new Timestamp(System.currentTimeMillis()));
        return fallback;
    }
}
```

---

## ✅ CHECKLIST FINAL

### **Antes de Empezar:**
- [ ] Verificar disponibilidad API autoridades
- [ ] Leer prompts completos
- [ ] Revisar servicios existentes relacionados

### **Durante Implementación:**
- [ ] Implementar con fallback si API no disponible
- [ ] Dejar TODOs claros para integración real
- [ ] Seguir convenciones EnArt

### **Después de Implementación:**
- [ ] Compilar sin errores
- [ ] Actualizar `SEGUIMIENTO_INCIDENCIAS.md`
- [ ] **Actualizar documento de auditoría asociado** (ver sección siguiente)
- [ ] **Documentar BusinessService** en `/docs/developers/` (ver sección siguiente)
- [ ] **Registrar archivos creados** en este documento (ver sección siguiente)
- [ ] Si está bloqueado, ayudar con Equipo 3

---

## 📝 REGISTRO DE ARCHIVOS CREADOS

Al finalizar cada incidencia, **registrar aquí** todos los archivos creados o modificados:

### **INC-020: Integración APIs Autoridades**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [ ] Ninguno (servicio ya existía)

**Archivos Modificados:**
- [x] `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/AuthorityNotificationService.java` - Mejorado formato de payload según Anexo VIII, agregada validación de formato, agregado método `registerWithAuthority()`

**Descripción:**
- Mejorado método `buildNotificationPayload()` para formatear según especificación oficial Anexo VIII
- Agregada validación de formato según sección (SECTION_A, SECTION_B, SECTION_C)
- Agregado método `registerWithAuthority()` como alias de `notifyAuthority()` para compatibilidad
- Validación de campos requeridos según Anexo VIII
- Manejo robusto de errores y logging detallado
- Fallback implementado si API no está configurada

**Fecha Finalización:** 2025-01-27

---

### **INC-007: Validación Cruzada FRIA vs Métricas**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] BusinessService: `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/FriaValidationService.java`

**Archivos Modificados:**
- [x] `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/FriaAssessmentBusinessService.java` - Integrado `FriaValidationService` con validación automática después de actualizar riesgos/mitigaciones

**Descripción:**
- Servicio completo para validación cruzada FRIA vs métricas técnicas
- Integración con microservicio Python `leka-fria-generator` (puerto 8012)
- Método principal: `validateAgainstMetrics(Long friaId)`
- Fallback implementado si microservicio no está disponible
- Parsea JSON de `FriaAssessment` (friarisks, friamitigationmeasures) a DTOs del cliente
- Manejo robusto de excepciones y logging detallado
- **Integración automática:** Validación cruzada se ejecuta automáticamente después de actualizar secciones de riesgos o mitigaciones en `updateFriaSection()`
- **Métodos públicos agregados:** `validateFriaAgainstMetrics(Long friaId)` y `validateFriaAgainstMetrics(Long friaId, Long modelId, Long datasetId)` en `FriaAssessmentBusinessService`

**Fecha Finalización:** 2025-01-27

---

### **INC-010-005: Vinculación PMM con Registro Art. 49**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] Script SQL: `/git/suinsit.nova.web/sql-scripts/patches/11_inc_010_005_pmm_eu_registration_link.sql`

**Archivos Modificados:**
- [x] `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/PostMarketMonitoring.java` - Agregado campo `IDXEUREGISTRATION` y relación `@ManyToOne` con `EuRegistration`
- [x] `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/EuRegistration.java` - Agregado campo `IDXPOSTMARKETMONITORING` y relación `@ManyToOne` con `PostMarketMonitoring`
- [x] `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/PostMarketMonitoringService.java` - Agregados métodos `linkToEuRegistration()` y `getRelatedRegistrations()`
- [x] `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/EuRegistrationBusinessService.java` - Agregados métodos `linkToPostMarketMonitoring()` y `getLinkedPostMarketMonitoring()`

**Descripción:**
- **Foreign Keys agregadas:**
  - `IDXEUREGISTRATION` en `PMMPOSTMARKETMONITORINGS` (FK a `REGEUREGISTRATIONS`)
  - `IDXPOSTMARKETMONITORING` en `REGEUREGISTRATIONS` (FK a `PMMPOSTMARKETMONITORINGS`)
- **Script SQL de migración:** Incluye creación de columnas, índices, constraints y vista `VW_EUR_REGISTRATIONS_WITH_PMM`
- **Métodos en servicios:**
  - `PostMarketMonitoringService.linkToEuRegistration()` - Vincula PMM con registro EU
  - `PostMarketMonitoringService.getRelatedRegistrations()` - Obtiene registros EU relacionados
  - `EuRegistrationBusinessService.linkToPostMarketMonitoring()` - Vincula registro EU con PMM
  - `EuRegistrationBusinessService.getLinkedPostMarketMonitoring()` - Obtiene PMM vinculado
- **Validaciones:** Verifica que PMM y registro pertenezcan al mismo proyecto, valida que PMM esté activo según Art. 16.h
- **Vista SQL:** `VW_EUR_REGISTRATIONS_WITH_PMM` para consultar cumplimiento Art. 16.h

**Fecha Finalización:** 2025-01-27

---

### **INC-010-010: Integración con Sistema de Feedback**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] Entidad: `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/UserFeedback.java`
- [x] BusinessService: `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/UserFeedbackService.java`
- [x] Script SQL: `/git/suinsit.nova.web/sql-scripts/patches/13_inc_010_010_user_feedback.sql`

**Archivos Modificados:**
- [x] `PostMarketMonitoringService.java` - Integrado cálculo de satisfacción de usuario en método `executeMonitoring()`

**Descripción:**
- **Entidad creada:** `UserFeedback` con prefijo `USF`, campos para tipo, texto, rating, análisis de sentimiento
- **Servicio implementado:**
  - `createFeedback()` - Crea feedback con análisis automático de sentimiento
  - `calculateSatisfactionScore()` - Calcula métrica de satisfacción (0.0-1.0) para últimos N días
  - `getFeedbacksByProject()` y `getFeedbacksByModel()` - Consultas de feedbacks
- **Integración con análisis de sentimiento:** Usa `AIGovernanceClient.llmEvaluation().analyzeSentiment()` (microservicio puerto 8002)
- **Integración con PMM:** Métrica de satisfacción agregada a resultados de monitoreo, detecta caída si score < 0.7
- **Tipos de feedback:** RATING, COMMENT, COMPLAINT, ESCALATION
- **Script SQL:** Tabla completa con índices optimizados para consultas de satisfacción

**Fecha Finalización:** 2025-01-27

**Referencias:**
- Prompt: `/docs/compliance/gaps/prompts/java/INC-010-010_integracion_feedback.md`
- Artículo EU AI Act: **Art. 72**

---

### **INC-010-015: Integración con Sistemas Externos**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] Interfaz: `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/integrations/ExternalMlPlatformConnector.java`
- [x] Adaptador: `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/integrations/impl/AzureMlConnector.java`
- [x] Adaptador: `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/integrations/impl/SageMakerConnector.java`
- [x] Servicio: `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/ExternalPlatformPmmIntegration.java`
- [x] Servicio: `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/integrations/ConnectorConfigurationService.java`

**Archivos Modificados:**
- [x] Adaptadores creados que reutilizan servicios existentes (`AzureMLConnectorService`, `SageMakerConnectorService`)

**Descripción:**
- **Interfaz común:** `ExternalMlPlatformConnector` define contrato para conectores ML externos
- **Adaptadores implementados:**
  - `AzureMlConnector` - Adaptador que usa `AzureMLConnectorService` existente
  - `SageMakerConnector` - Adaptador que usa `SageMakerConnectorService` existente
- **Integración con PMM:** `ExternalPlatformPmmIntegration` sincroniza métricas desde plataformas externas
- **Configuración:** `ConnectorConfigurationService` valida y obtiene estado de conectores
- **Funcionalidades:**
  - `syncExternalMetrics()` - Sincroniza métricas de modelos desde plataformas externas
  - `getConnectorStatuses()` - Obtiene estado de conectividad de todos los conectores
  - `validateConnectors()` - Valida configuración de conectores

**Notas:**
- Los adaptadores reutilizan la infraestructura existente (`AzureMLConnectorService`, `SageMakerConnectorService`)
- Requiere mapeo de `modelId` local a `endpointName`/`deploymentName` en plataformas externas (TODO)
- Almacenamiento de métricas en tabla de monitoreo pendiente de implementación completa (TODO)

**Fecha Finalización:** 2025-01-27

**Referencias:**
- Prompt: `/docs/compliance/gaps/prompts/java/INC-010-015_integracion_sistemas_externos.md`
- Artículo EU AI Act: **Art. 72**

---

### **INC-015: Verificación Términos OpenAI**

**Estado:** 🟢 COMPLETADO + REFACTORIZADO

**Archivos Creados:**
- [x] Interfaz: `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/models/ThirdPartyModelTermsValidator.java`
- [x] Validador OpenAI: `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/models/impl/OpenAITermsValidator.java`
- [x] Validador Genérico: `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/models/impl/GenericTermsValidator.java`
- [x] Script SQL: `/git/suinsit.nova.web/sql-scripts/patches/14_inc_015_provider_terms_fields.sql`

**Archivos Modificados:**
- [x] `ModelProvider.java` - Agregados campos JSONB: `modtermsofuse`, `moddatapolicy`, `modcomplianceinfo`
- [x] `ModelValidationService.java` - Refactorizado `verifyOpenAITerms()` para usar `ModelProvider` y validadores genéricos

**Descripción:**
- **Refactorización completa:** Ahora usa `ModelProvider` para escalar a millones de modelos y miles de proveedores
- **Arquitectura escalable:**
  - Interfaz `ThirdPartyModelTermsValidator` para validadores específicos
  - `GenericTermsValidator` que usa información JSONB de `ModelProvider`
  - `OpenAITermsValidator` como ejemplo de validador específico
- **Campos agregados en ModelProvider:**
  - `MODTERMSOFUSE` (JSONB) - Términos de uso del proveedor
  - `MODDATAPOLICY` (JSONB) - Política de datos (opt-out, retención, etc.)
  - `MODCOMPLIANCEINFO` (JSONB) - Compliance GDPR, EU AI Act, certificaciones
- **Validaciones implementadas:**
  - Uso de datos personales en proyectos alto riesgo (Art. 10)
  - Opt-out de entrenamiento disponible/requerido
  - Compliance GDPR y EU AI Act
  - Residencia de datos (UE/EEA)
  - Retención de datos
- **Ventajas:**
  - Escalable: Funciona para cualquier proveedor sin código adicional
  - Centralizado: Toda la información en `ModelProvider`
  - Flexible: JSONB permite estructuras diferentes por proveedor
  - Actualizable: Cambios sin deploy

**Fecha Finalización:** 2025-01-27 (refactorización)

**Referencias:**
- Prompt: `/docs/compliance/gaps/prompts/java/INC-015_terminos_openai.md`
- Documentación Auditoría: `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-015`
- Artículo EU AI Act: **Art. 10**

---

### **INC-021: Versionado FRIA**

**Estado:** 🟢 COMPLETADO

**Archivos Creados:**
- [x] Script SQL: `/git/suinsit.nova.web/sql-scripts/patches/12_inc_021_fria_versioning.sql`

**Archivos Modificados:**
- [x] `/eclipse-workspace/nocode.service/nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/FriaAssessment.java` - Agregados campos `friaversion`, `friapreviousversionid` y relación `@ManyToOne` con versión anterior
- [x] `/eclipse-workspace/nocode.service/codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/FriaAssessmentBusinessService.java` - Agregados métodos `createNewVersion()`, `copyFriaData()` y `getVersionHistory()`

**Descripción:**
- **Campos agregados:**
  - `FRIAVERSION` (INTEGER) - Número de versión, default 1
  - `FRIAPREVIOUSVERSIONID` (BIGINT) - FK a versión anterior (self-reference)
  - Relación `@ManyToOne` con `previousVersion`
- **Métodos implementados:**
  - `createNewVersion(Long friaId)` - Crea nueva versión del FRIA, incrementa versión, resetea aprobación/notificación
  - `copyFriaData(FriaAssessment source, FriaAssessment target)` - Copia todos los datos entre versiones
  - `getVersionHistory(Long friaId)` - Obtiene historial de versiones anteriores
- **Script SQL:** Incluye creación de columnas, índices, constraint de self-reference y vista `VW_FRIA_VERSION_HISTORY`
- **Características:** Nueva versión resetea aprobación y notificación (debe ser aprobada nuevamente), mantiene historial completo

**Fecha Finalización:** 2025-01-27

**Referencias:**
- Prompt: `/docs/compliance/gaps/prompts/java/INC-021_versionado_fria.md`
- Artículo EU AI Act: **Art. 27**

---

## 📋 ACTUALIZACIÓN DE DOCUMENTOS DE AUDITORÍA

Al finalizar cada incidencia, **actualizar** los siguientes documentos:

### **INC-020: Integración APIs Autoridades**

**Documentos a Actualizar:**
1. **`/docs/compliance/auditoria/AUDITORIA_FRIA_EVALUACIONES_TECNICAS.md`** o **`AUDITORIA_013_CUMPLIMIENTO_AI_ACT.md`**
   - Buscar sección relacionada con Art. 27.3, 49
   - Actualizar estado de implementación
   - Añadir referencia a entidad/BusinessService creado

2. **`/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md`**
   - Buscar `INC-020`
   - Cambiar estado de `🔴 PENDIENTE` a `🟢 COMPLETADO`
   - Añadir fecha de finalización
   - Añadir notas sobre implementación

3. **`/docs/compliance/gaps/SEGUIMIENTO_INCIDENCIAS.md`**
   - Buscar `INC-020`
   - Actualizar estado, fecha fin, notas

---

### **INC-007: Validación Cruzada FRIA vs Métricas**

**Documentos a Actualizar:**
1. **`/docs/compliance/auditoria/AUDITORIA_FRIA_EVALUACIONES_TECNICAS.md`**
   - Buscar sección relacionada con Art. 27
   - Actualizar estado de implementación
   - Añadir referencia a entidad/BusinessService creado

2. **`/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md`**
   - Buscar `INC-007`
   - Cambiar estado de `🔴 PENDIENTE` a `🟢 COMPLETADO`
   - Añadir fecha de finalización
   - Añadir notas sobre implementación

3. **`/docs/compliance/gaps/SEGUIMIENTO_INCIDENCIAS.md`**
   - Buscar `INC-007`
   - Actualizar estado, fecha fin, notas

---

### **INC-010-005: Vinculación PMM con Registro Art. 49**

**Documentos a Actualizar:**
1. **`/docs/compliance/auditoria/AUDITORIA_010_POST_MARKET_MONITORING.md`**
   - Buscar sección relacionada con Art. 16.h, 49
   - Actualizar estado de implementación
   - Añadir referencia a entidad/BusinessService creado

2. **`/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_010_POST_MARKET_MONITORING.md`**
   - Buscar `INC-010-005`
   - Cambiar estado de `🔴 PENDIENTE` a `🟢 COMPLETADO`
   - Añadir fecha de finalización
   - Añadir notas sobre implementación

3. **`/docs/compliance/gaps/SEGUIMIENTO_INCIDENCIAS_010_PMM.md`**
   - Buscar `INC-010-005`
   - Actualizar estado, fecha fin, notas

---

## 📚 DOCUMENTACIÓN DE BUSINESS SERVICES

**IMPORTANTE:** Todos los BusinessServices creados o modificados **DEBEN** ser documentados en `/docs/developers/`.

### **BusinessServices a Documentar:**

#### **INC-020:**
- [ ] `AuthorityNotificationService` (modificado) - Actualizar documento existente

#### **INC-007:**
- [ ] `FriaValidationService` (nuevo) - Crear documento nuevo

#### **INC-010-005:**
- [ ] `PostMarketMonitoringService` (modificado) - Actualizar documento existente
- [ ] `EuRegistrationBusinessService` (modificado) - Actualizar documento existente

**Proceso:** Ver sección completa en `AGENTE_1_VALIDACIONES_CRITICAS.md`

---

## 🔄 COORDINACIÓN CON OTROS EQUIPOS

### **Si INC-020 está bloqueado:**
- Ayudar con Equipo 3 (PMM)
- Revisar otras incidencias pendientes
- Documentar bloqueo claramente

---

**Última Actualización:** 25 de noviembre de 2025
